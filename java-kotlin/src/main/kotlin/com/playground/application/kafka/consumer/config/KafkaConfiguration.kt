package com.playground.application.kafka.consumer.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class KafkaConfiguration {
    @Bean
    fun kafkaReceiverOptions(): ReceiverOptions<String, String> {
        return ReceiverOptions.create<String, String>(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "b-1.commonkafkadev.kor79u.c2.kafka.ap-northeast-2.amazonaws.com:9092,b-2.commonkafkadev.kor79u.c2.kafka.ap-northeast-2.amazonaws.com:9092",
                ConsumerConfig.GROUP_ID_CONFIG to "group-id",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
            )
        ).subscription(listOf("ent-notification-test"))
    }

    @Bean
    fun kafkaReceiver(receiverOptions: ReceiverOptions<String, String>): KafkaReceiver<String, String> =
        KafkaReceiver.create(receiverOptions)
}
