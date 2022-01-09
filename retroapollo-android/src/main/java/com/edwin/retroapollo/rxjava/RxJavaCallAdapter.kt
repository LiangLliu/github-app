package com.edwin.retroapollo.rxjava

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.edwin.retroapollo.CallAdapter
import com.edwin.retroapollo.CallAdapter.Factory
import com.edwin.retroapollo.rxjava.RxReturnType.OBSERVABLE
import com.edwin.retroapollo.rxjava.RxReturnType.SINGLE
import com.edwin.retroapollo.utils.Utils
import rx.Observable
import rx.Scheduler
import rx.Single
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


enum class RxReturnType {
    OBSERVABLE, SINGLE
}

class RxJavaCallAdapter<T>(
    val rxReturnType: RxReturnType,
    val dataType: Type,
    val subscribeScheduler: Scheduler? = null,
    val observableScheduler: Scheduler? = null
) : CallAdapter<T, Any> {
    override fun responseType(): Type {
        return if (dataType is ParameterizedType) {
            Utils.getParameterUpperBound(0, dataType)
        } else {
            dataType
        }
    }

    override fun adapt(call: ApolloCall<T>): Any {
        val callFunc = CallExecuteOnSubscribe(call)
        var originalObservable = Observable.create(callFunc)
        originalObservable =
            subscribeScheduler?.let(originalObservable::subscribeOn) ?: originalObservable
        originalObservable =
            observableScheduler?.let(originalObservable::observeOn) ?: originalObservable

        val observable: Observable<*> =
            // Observable<Response<Data>>
            if (dataType is ParameterizedType) {
                originalObservable
            } else {
                originalObservable.map { it.data() }
            }

        return when (rxReturnType) {
            OBSERVABLE -> observable
            SINGLE -> observable.toSingle()
        }
    }
}

class RxJavaCallAdapterFactory : Factory() {

    private var subscribeScheduler: Scheduler? = null

    fun subscribeScheduler(scheduler: Scheduler): RxJavaCallAdapterFactory {
        subscribeScheduler = scheduler
        return this
    }

    private var observableScheduler: Scheduler? = null

    fun observableScheduler(scheduler: Scheduler): RxJavaCallAdapterFactory {
        observableScheduler = scheduler
        return this
    }

    override fun get(returnType: Type): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        val dataType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (dataType is ParameterizedType) {
            val responseType = getRawType(dataType)
            if (responseType != Response::class.java) {
                return null
            }
        }
        val rxReturnType = when (rawType) {
            Single::class.java -> SINGLE
            Observable::class.java -> OBSERVABLE
            else -> null
        } ?: return null
        return RxJavaCallAdapter<Any>(
            rxReturnType,
            dataType,
            subscribeScheduler,
            observableScheduler
        )
    }
}