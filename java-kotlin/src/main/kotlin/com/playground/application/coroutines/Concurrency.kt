package com.playground.application.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Concurrency {
    fun tasteRunBlockingConcurrency() {
        runBlocking {
            println("Parent task started")

            launch {
                println("Task A started")
                delay(200)
                println("Task A finished")
            }

            launch {
                println("Task B started")
                delay(200)
                println("Task B finished")
            }

            delay(100)
            println("Parent task finished")
        }
        println("Shutting down...")
    }

    fun tasteCoroutineScopeConcurrencyT1() {
        runBlocking {
            coroutineScope {
                launch {
                    println("Task A started")
                    delay(200)
                    println("Task A finished")
                }

                launch {
                    println("Task B started")
                    delay(200)
                    println("Task B finished")
                }

                println("Custom parent scope end...")
            }
            println("Blocking scope end...")
        }
    }

    suspend fun tasteCoroutineScopeConcurrencyT2() {
        coroutineScope {
            launch {
                println("Task A started")
                delay(200)
                println("Task A finished")
            }

            launch {
                println("Task B started")
                delay(200)
                println("Task B finished")
            }

            println("Custom parent scope end...")
        }
        println("Blocking scope end...")
    }
}
