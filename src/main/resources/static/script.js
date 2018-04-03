var listContainerHandler;
var taskList;


window.onload = function() {
    init();
};

function init() {
    initListContainerHandler();
    loadTaskList();
    getById("new_task_text").focus();
}

function initListContainerHandler() {
    listContainerHandler = {};
    listContainerHandler.listContainer = getById("list_container");

    listContainerHandler.repopulate = function(taskList) {
        this.listContainer.innerHTML = "";

        for (var i = 0; i < taskList.length; i++) {
            var listElem = createNewTaskElement(i, taskList[i]);
            this.listContainer.append(listElem);
        }

        if (taskList.length === 0) {
            var el = getTemplate("no_elements");
            this.listContainer.append(el);
        }
    };

    listContainerHandler.addTask = function(id, task) {
        if (this.listContainer.firstChild !== null && this.listContainer.firstChild.tagName !== "DIV") {
            this.listContainer.innerHTML = "";
        }

        var listElem = createNewTaskElement(id, task);
        this.listContainer.append(listElem);
    };

    listContainerHandler.removeTask = function(id) {
        this.listContainer.removeChild(this.listContainer.childNodes[id]);
        while (id < this.listContainer.childNodes.length) {
            this.listContainer.childNodes[id].setAttribute("taskId", id);
            id++;
        }

        if (taskList.length === 0) {
            var el = getTemplate("no_elements");
            this.listContainer.append(el);
        }
    };

    listContainerHandler.updateTask = function(id) {
        getOneByClass("task_description", this.listContainer.childNodes[id]).innerHTML = taskList[id].description;
    };
}

//Listeners
document.onkeypress = function(e) {
    if (e.keyCode === 13) {
        onAddTaskClick();
    }
};

function onAddTaskClick() {
    var descriptionField = getById("new_task_text");

    var taskDescription = descriptionField.value.trim();
    descriptionField.value = "";
    descriptionField.focus();

    if (taskDescription.length > 0) {
        var newTask = {};
        newTask.description = taskDescription;
        addNewTask(newTask);
    }
}

function onEditTaskClick(element) {
    var id = parseInt(element.getAttribute("taskId"));
    var description = prompt("Enter new task description", taskList[id].description);

    if (description.trim().length > 0) {
        editTask(id, description);
    }

    getById("new_task_text").focus();
}

function onRemoveTaskClick(element) {
    getById("new_task_text").focus();

    removeTask(parseInt(element.getAttribute("taskId")));
}

function onCompletedCheckboxClick(element) {
    var checkbox = getOneByClass("completed_checkbox", element);
    var checked = checkbox.src.endsWith("img/checked.png");
    if (checked) {
        checkbox.src = "img/unchecked.png";
    } else {
        checkbox.src = "img/checked.png";
    }
    setTaskCompleted(parseInt(element.getAttribute("taskId")), !checked);
}
//

//server requests implementation
function addNewTask(newTask) {
    request("POST", "/addTask", JSON.stringify(newTask));

    listContainerHandler.addTask(taskList.length, newTask);
    taskList.push(newTask);
}

function editTask(id, newDescription) {
    if (id > -1 && id < taskList.length) {
        taskList[id].description = newDescription;

        request("POST", "/editTask", "{\"id\":" + id + ", \"task\":" + JSON.stringify(taskList[id]) + "}");

        listContainerHandler.updateTask(id);
    }
}
function setTaskCompleted(taskId, completed) {
    if (taskList[taskId].completed !== completed) {
        taskList[taskId].completed = completed;

        request("POST", "/editTask", "{\"id\":" + taskId + ", \"task\":" + JSON.stringify(taskList[taskId]) + "}");
    }
}

function removeTask(id) {
    if (id > -1 && id < taskList.length) {
        request("DELETE", "/deleteTask?id=" + id);

        taskList.splice(id, 1);
        listContainerHandler.removeTask(id);
    }
}

function loadTaskList() {
    request("GET", "/getAllTasks", null, function (request) {
        taskList = JSON.parse(request.responseText);
        listContainerHandler.repopulate(taskList);
    });
}

function request(method, url, body, onOk) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            if (onOk != null) {
                onOk(this);
            }
        }
    };
    xhttp.open(method, url, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send(body);
}
//


//utility methods
function createNewTaskElement(id, task) {
    var div = getTemplate("list_element");
    div.setAttribute("taskId", id);
    getOneByClass("task_description", div).innerText = task.description;
    getOneByClass("completed_checkbox", div).src = (task.completed == null || !task.completed)
        ? "img/unchecked.png" : "img/checked.png";
    return div;
}

function getTemplate(className) {
    var template = getOneByClass("template " + className);
    var result = template.cloneNode();
    result.innerHTML = template.innerHTML;
    result.classList.remove("template");

    return result;
}

function getById(id, rootElement) {
    return document.getElementById(id);
}

function getOneByClass(className, rootElement) {
    if (rootElement == null) {
        return document.getElementsByClassName(className)[0];
    } else {
        return rootElement.getElementsByClassName(className)[0];
    }
}
//