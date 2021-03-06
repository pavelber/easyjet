package org.easyjet.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar

/**
 * Created by Pavel on 9/29/2015.
 */
@Configuration
@EnableScheduling
class SchedulersConfig implements SchedulingConfigurer {

    static Logger logger = LoggerFactory.getLogger(SchedulersConfig.class)
    private static final long DELAY =  10 * 60 * 1000L


    @Autowired
    ApplicationContext factory


    @Autowired
    EasyJetCheckAndSend startDownloads

    @Autowired
    WowCheckAndSend wowCheck

    @Bean
    ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler()
        scheduler.setPoolSize(20)
        scheduler.setThreadNamePrefix("task-")
        scheduler.setAwaitTerminationSeconds(60)
        scheduler.setWaitForTasksToCompleteOnShutdown(true)
        return scheduler
    }

    @Override
    void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addFixedRateTask(startDownloads, DELAY)
   //     taskRegistrar.addFixedRateTask(wowCheck, DELAY)
    }
}
