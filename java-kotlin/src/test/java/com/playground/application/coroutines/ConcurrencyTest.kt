package com.playground.application.coroutines

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConcurrencyTest {
    @Test
    fun testConcurrency() {
        val concurrency = Concurrency()
        concurrency.tasteRunBlockingConcurrency()
    }

    @Test
    fun testCoroutineScopeConcurrency() {
        val concurrency = Concurrency()
        concurrency.tasteCoroutineScopeConcurrencyT1()
    }

    @Test
    fun testCoroutineScopeConcurrencyT2() {
        runBlocking {
            val concurrency = Concurrency()
            concurrency.tasteCoroutineScopeConcurrencyT2()
            println("Coroutine scope end...")
        }
    }
}
