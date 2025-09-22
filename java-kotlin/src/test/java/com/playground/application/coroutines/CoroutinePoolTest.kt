package com.playground.application.coroutines

import com.playground.application.kafka.consumer.config.KafkaConfiguration
import com.playground.application.kafka.consumer.service.KafkaConsumerService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import kotlin.test.Test

@SpringBootTest(classes = [KafkaConfiguration::class, KafkaConsumerService::class])
class CoroutinePoolTest {
    @Test
    fun testCoroutinePool() {
        runBlocking{
            val coroutinePool = CoroutinePool(3)

            coroutinePool.setHandler { msg ->
                println("Task started")
                println("Message received: $msg")
                println("Task finished")
            }

            coroutinePool.submit("Coroutine Pool Started")

            repeat(10) { i ->
                coroutinePool.submit("Task $i")
            }
            coroutinePool.closeAndJoin()
            println("Coroutine pool closed")
        }
        println("Main thread finished")
    }

    @Autowired
    lateinit var kafkaConsumerService: KafkaConsumerService
    @Test
    fun testCombineCoroutinePoolAndKafkaReactiveConsumer() {
        val coroutinePool = CoroutinePool(3)
        coroutinePool.setHandler { msg ->
            println("Task started")
            println("Message received: $msg")
            Thread.sleep(1000)
            println("Task finished")
        }

        kafkaConsumerService.consumeTo(coroutinePool)

        Thread.sleep(Duration.ofSeconds(30))
    }
}
