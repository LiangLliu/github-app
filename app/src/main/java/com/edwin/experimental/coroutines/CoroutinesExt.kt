package com.edwin.experimental.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

suspend fun <T> Deferred<T>.awaitOrError(): Result<T> {
    return try {
        Result.of(await())
    } catch (e: Exception) {
        Result.of(e)
    }
}

class SuccessAndError<T : Any> {
    private var _onSuccess: T.() -> Unit = {}
    private var _onError: (Throwable) -> Unit = {}
    var context: CoroutineContext = Dispatchers.Default

    fun onSuccess(onSuccess: T.() -> Unit) {
        _onSuccess = onSuccess
    }

    fun onError(onError: (Throwable) -> Unit) {
        _onError = onError
    }

    fun excSuccess(t: T) = _onSuccess.invoke(t)

    fun excError(t: Throwable) = _onError.invoke(t)
}

fun Job.cancelByActive() {
    if (isActive) cancel()
}

fun launchUI(block: suspend () -> Unit): Job {
    return GlobalScope.launch(Dispatchers.Main) { block() }
}

fun launchIO(block: suspend () -> Unit): Job {
    return GlobalScope.launch(Dispatchers.IO) { block() }
}

fun launch(block: suspend () -> Unit): Job {
    return GlobalScope.launch { block() }
}

/**
 ** 外面需要使用launch或者async，例子：
 **
 **        launchUI {
 **            view.showLoading()
 **            kotlin.runCatching {
 **                view.showSleepMusics(dataRepository.getSleepMusicsTest().awaitUI())
 **                view.hideLoading()
 **            }.onFailure {
 **                view.hideLoading()
 **                it.message?.run {
 **                    view.showToast(this)
 **                    Log.e(TAG, this)
 **                }
 **            }
 **        }.addCoroutines() // addCoroutines方法是基类取消协程的操作
 */

