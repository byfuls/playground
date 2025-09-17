package com.playground.application.kafka.consumer.service

import com.playground.application.kafka.consumer.config.KafkaConfiguration
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOffset
import reactor.kafka.receiver.ReceiverRecord
import java.time.Duration

@SpringBootTest(classes = [KafkaConfiguration::class, KafkaConsumerService::class])
@TestPropertySource(properties = [
    "kafka.consumer.autostart=false"
])
class KafkaConsumerServiceTest {

    @Autowired
    lateinit var kafkaConsumerService: KafkaConsumerService

    @Test
    fun consumerTest() {
        // 테스트에서 명시적으로 consumer 시작
        kafkaConsumerService.consume()
        Thread.sleep(Duration.ofSeconds(30))
        println("Test end...")
    }

    @Test
    fun `consume acknowledges and processes messages`() {
        val receiver: KafkaReceiver<String, String> = mock()

        val offset1 = mock<ReceiverOffset>()
        val record1 = mock<ReceiverRecord<String, String>>()
        whenever(record1.value()).thenReturn("hello")
        whenever(record1.receiverOffset()).thenReturn(offset1)

        val offset2 = mock<ReceiverOffset>()
        val record2 = mock<ReceiverRecord<String, String>>()
        whenever(record2.value()).thenReturn("world")
        whenever(record2.receiverOffset()).thenReturn(offset2)

        whenever(receiver.receive()).thenReturn(Flux.just(record1, record2))

        val service = KafkaConsumerService(receiver)
        service.consume()

        // Allow async subscription to process
        Thread.sleep(Duration.ofMillis(100).toMillis())

        verify(offset1).acknowledge()
        verify(offset2).acknowledge()
    }
}
