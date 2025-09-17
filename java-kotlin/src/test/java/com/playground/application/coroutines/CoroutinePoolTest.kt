package com.playground.application.coroutines

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class CoroutinePoolTest {
    @Test
    fun testCoroutinePool() {
        runBlocking{
            val coroutinePool = CoroutinePool(3)
            repeat(10) { i ->
                coroutinePool.submit {
                    println("Task $i started")
                    Thread.sleep(1000)
                    println("Task $i finished")
                }
            }
            coroutinePool.closeAndJoin()
            println("Coroutine pool closed")
        }
        println("Main thread finished")
    }
}
