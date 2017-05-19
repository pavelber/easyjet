package org.easyjet

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync

/**
 * Created by Pavel on 9/29/2015.
 */
@SpringBootApplication
@ComponentScan
@EnableJpaRepositories(basePackages = ["org.easyjet"])
@EnableAsync
@PropertySources([
        @PropertySource(value = "classpath:application.properties"),
        @PropertySource(value = 'file:${user.home}/properties/easy.properties')])
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    // Used when deploying to a standalone servlet container
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}