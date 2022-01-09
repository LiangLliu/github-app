package retrofit2.adapter.rxjava;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Completable;
import rx.Observable;
import rx.Scheduler;
import rx.Single;

public final class RxJavaCallAdapterFactory2 extends CallAdapter.Factory {
    /**
     * Returns an instance which creates synchronous observables that do not operate on any scheduler
     * by default.
     */
    public static RxJavaCallAdapterFactory2 create() {
        return new RxJavaCallAdapterFactory2(null, null, false);
    }

    /**
     * Returns an instance which creates asynchronous observables. Applying
     * {@link Observable#subscribeOn} has no effect on stream types created by this factory.
     */
    public static RxJavaCallAdapterFactory2 createAsync() {
        return new RxJavaCallAdapterFactory2(null, null, true);
    }

    /**
     * Returns an instance which creates synchronous observables that
     * {@linkplain Observable#subscribeOn(Scheduler) subscribe on} {@code scheduler} by default.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static RxJavaCallAdapterFactory2 createWithScheduler(Scheduler schedulerSubscribeOn) {
        if (schedulerSubscribeOn == null)
            throw new NullPointerException("schedulerSubscribeOn == null");
        return new RxJavaCallAdapterFactory2(schedulerSubscribeOn, null, false);
    }

    /**
     * Returns an instance which creates synchronous observables that
     * {@linkplain Observable#subscribeOn(Scheduler) subscribe on} {@code scheduler} by default.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static RxJavaCallAdapterFactory2 createWithSchedulers(Scheduler schedulerSubscribeOn, Scheduler schedulerObserveOn) {
        if (schedulerSubscribeOn == null)
            throw new NullPointerException("schedulerSubscribeOn == null");
        if (schedulerObserveOn == null)
            throw new NullPointerException("schedulerObserveOn == null");
        return new RxJavaCallAdapterFactory2(schedulerSubscribeOn, schedulerObserveOn, false);
    }

    private final @Nullable
    Scheduler schedulerSubscribeOn;
    private final @Nullable
    Scheduler schedulerObserveOn;
    private final boolean isAsync;

    private RxJavaCallAdapterFactory2(@Nullable Scheduler schedulerSubscribeOn, @Nullable Scheduler schedulerObserveOn, boolean isAsync) {
        this.schedulerSubscribeOn = schedulerSubscribeOn;
        this.schedulerObserveOn = schedulerObserveOn;
        this.isAsync = isAsync;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        boolean isSingle = rawType == Single.class;
        boolean isCompletable = rawType == Completable.class;
        if (rawType != Observable.class && !isSingle && !isCompletable) {
            return null;
        }

        if (isCompletable) {
            return new RxJavaCallAdapter2(Void.class, schedulerSubscribeOn, schedulerObserveOn, isAsync, false, true, false, false, true);
        }

        boolean isResult = false;
        boolean isBody = false;
        boolean isPaging = false;
        Type responseType;
        if (!(returnType instanceof ParameterizedType)) {
            String name = isSingle ? "Single" : "Observable";
            throw new IllegalStateException(name + " return type must be parameterized"
                    + " as " + name + "<Foo> or " + name + "<? extends Foo>");
        }

        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType == Response.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parameterized"
                        + " as Response<Foo> or Response<? extends Foo>");
            }
            responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
        } else if (rawObservableType == Result.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Result must be parameterized"
                        + " as Result<Foo> or Result<? extends Foo>");
            }
            responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            isResult = true;
        } else if (rawObservableType == GitHubPaging.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Result must be parameterized"
                        + " as GitHubPaging<Foo> or GitHubPaging<? extends Foo>");
            }
            responseType = observableType;
            isPaging = true;
        } else if (PagingWrapper.class.isAssignableFrom(rawObservableType)) {
            responseType = observableType;
            isPaging = true;
        } else {
            responseType = observableType;
            isBody = true;
        }

        return new RxJavaCallAdapter2(responseType, schedulerSubscribeOn, schedulerObserveOn, isAsync, isResult, isBody, isPaging, isSingle,
                false);
    }
}
