package com.italankin.qr

import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ImmediateFuture<T>(private val result: T) : ListenableFuture<T> {

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean = false

    override fun isCancelled(): Boolean = false

    override fun isDone(): Boolean = true

    override fun get(): T = result

    override fun get(timeout: Long, unit: TimeUnit): T = result

    override fun addListener(listener: Runnable, executor: Executor) {
        executor.execute(listener)
    }
}
