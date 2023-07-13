package com.kotlintraining.springbatch.configs

import com.kotlintraining.springbatch.tasklets.WelcomeTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableBatchProcessing
@Configuration
class SpringBatchConfiguration(private val jobBuilderFactory: JobBuilderFactory,
                               private val stepBuilderFactory: StepBuilderFactory,
                               private val welcomeTasklet: WelcomeTasklet) {
    companion object{
        const val MY_JOB_NAME = "MyFirstJob"
        const val TASKLET_NAME = "WelcomeText"
    }

    @Bean
    fun myJob(): Job{
        return jobBuilderFactory
            .get(MY_JOB_NAME)
            .start(printWelcomeText())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    fun printWelcomeText(): Step{
        return stepBuilderFactory.get(TASKLET_NAME)
            .tasklet(welcomeTasklet)
            .build()
    }
}