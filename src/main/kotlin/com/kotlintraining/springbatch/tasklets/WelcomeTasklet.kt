package com.kotlintraining.springbatch.tasklets

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

@Component
class WelcomeTasklet: Tasklet {
    override fun execute(p0: StepContribution, p1: ChunkContext): RepeatStatus {
        println("\n*******************************")
        println("Welcome!")
        println("This is a Job with Kotlin!")
        println("*******************************\n")
        return RepeatStatus.FINISHED
    }
}