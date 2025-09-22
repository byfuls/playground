package com.playground.application.kafka.consumer.service

import kotlinx.coroutines.reactor.mono
import com.playground.application.coroutines.CoroutinePool
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver

@Service
class KafkaConsumerService(
    private val kafkaReceiver: KafkaReceiver<String, String>,
//    @Value("\${kafka.consumer.autostart:true}") private val autostart: Boolean
) {
//    @PostConstruct
//    fun init() {
//        if (autostart) {
//            consume()
//        }
//    }

    fun consume() {
        val kafkaFlux: Flux<String> = kafkaReceiver
            .receive()
            .map { record ->
                val value = record.value()
                println("Received message: $value")
                record.receiverOffset().acknowledge()
                value
            }

        kafkaFlux.subscribe(
            { msg ->
                println("Received message: $msg")
            },
            { error ->
                println("Error occurred: $error")
            },
            {
                println("Flux completed")
            }
        )
    }

    fun consumeTo(pool: CoroutinePool) {
        val kafkaFlux = kafkaReceiver
            .receive()
            .concatMap { record ->
                val value = record.value()
                println("Received message: $value")
                mono {
                    pool.submit(value)
                    // 메시지를 풀에 안전히 큐잉한 후 ack
                    record.receiverOffset().acknowledge()
                }
            }

        kafkaFlux.subscribe(
            { _ -> println("Message enqueued to CoroutinePool") },
            { error -> println("Error occurred: $error") },
            { println("Flux completed") }
        )
    }
}
