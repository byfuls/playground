package com.playground.application.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CoroutinePool(
    private val size: Int,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    // 버퍼를 제한해 백프레셔 (제한 없으면 무한 대기열 → 메모리 증가 위험)
    private val queue = Channel<suspend () -> Unit>(capacity = 1024)
    private val workers: List<Job>

    init {
        workers = List(size) {
            scope.launch {
                for (task in queue) {
                    try { task() }
                    catch (e: CancellationException) { throw e }
                    catch (t: Throwable) {
                        println("Error occurred in worker coroutine: $t")
                    }
                }
            }
        }
    }

    suspend fun submit(task: suspend () -> Unit) = queue.send(task)
    suspend fun closeAndJoin() {
        queue.close()
        workers.joinAll()
        scope.cancel()
    }
}
