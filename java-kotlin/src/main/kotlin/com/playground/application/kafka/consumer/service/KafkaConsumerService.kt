package com.playground.application.kafka.consumer.service

import kotlinx.coroutines.reactor.mono
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

    fun consume(process: suspend (String) -> Unit) {
        val kafkaFlux: Flux<String> = kafkaReceiver
            .receive()
            .map { record ->
                val value = record.value()
                println("Received message: $value")
                record.receiverOffset().acknowledge()
                value
            }

        kafkaFlux
            .doOnNext { msg ->
                println("Received message: $msg")
            }
            .concatMap { msg ->
                mono { process(msg) }
            }
            .subscribe(
            { _ ->
                println("Message processed")
            },
            { error ->
                println("Error occurred: $error")
            },
            {
                println("Flux completed")
            }
        )
    }
}
