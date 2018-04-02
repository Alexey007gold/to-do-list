package com.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Oleksii_Kovetskyi on 3/28/2018.
 */
@Configuration
@EnableWebMvc
@ComponentScan(value = {"com"}, excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value = AppConfiguration.class)
})
public class AppConfiguration {
}
