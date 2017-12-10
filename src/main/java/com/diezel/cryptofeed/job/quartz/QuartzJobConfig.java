package com.diezel.cryptofeed.job.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;

/**
 * Created by zalet on 12/10/2017.
 */
//@Configuration
public class QuartzJobConfig {

    private static final Logger log = LoggerFactory.getLogger(QuartzJobConfig.class);

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public Scheduler scheduler(Trigger trigger, JobDetail job) throws Exception {
        try {
            StdSchedulerFactory factory = new StdSchedulerFactory();
            factory.initialize(new ClassPathResource("quartz.properties").getInputStream());

            Scheduler scheduler = factory.getScheduler();
            scheduler.setJobFactory(springBeanJobFactory());
            scheduler.scheduleJob(job, trigger);

            scheduler.start();
            return scheduler;
        } catch (IOException ex) {
            throw new Exception("Unable to find quartz.properties file in classpath.", ex);
        } catch (SchedulerException ex) {
            throw new Exception("Error creating scheduler.", ex);
        }
    }

    // Information about the job.
    @Bean
    public JobDetailFactoryBean jobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(QuartzJob.class);
        jobDetailFactory.setDescription("An example of a quartz job.");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    // Determines when and how often the job will run.
    @Bean
    public SimpleTriggerFactoryBean trigger(JobDetail job) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(job);

        trigger.setRepeatInterval(3600000); // 1 Hour in milliseconds
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }

    // Note: Included to allow new jobs to be fully integrated with Spring.
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

}
