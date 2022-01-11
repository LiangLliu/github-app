
package retrofit2.adapter.rxjava;

import androidx.annotation.Nullable;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;

final class RxJavaCallAdapter2<R> implements CallAdapter<R, Object> {
    private final Type responseType;
    private final @Nullable
    Scheduler schedulerSubscribeOn;
    private final @Nullable
    Scheduler schedulerObserveOn;
    private final boolean isAsync;
    private final boolean isResult;
    private final boolean isBody;
    private final boolean isPaging;
    private final boolean isSingle;
    private final boolean isCompletable;

    public RxJavaCallAdapter2(Type responseType, @Nullable Scheduler schedulerSubscribeOn, @Nullable Scheduler schedulerObserveOn, boolean isAsync,
                       boolean isResult, boolean isBody, boolean isPaging, boolean isSingle, boolean isCompletable) {
        this.responseType = responseType;
        this.schedulerSubscribeOn = schedulerSubscribeOn;
        this.schedulerObserveOn = schedulerObserveOn;
        this.isAsync = isAsync;
        this.isResult = isResult;
        this.isBody = isBody;
        this.isPaging = isPaging;
        this.isSingle = isSingle;
        this.isCompletable = isCompletable;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Object adapt(Call<R> call) {
        OnSubscribe<Response<R>> callFunc = isAsync
                ? new CallEnqueueOnSubscribe<>(call)
                : new CallExecuteOnSubscribe<>(call);

        OnSubscribe<?> func;
        if (isResult) {
            func = new ResultOnSubscribe<>(callFunc);
        } else if (isBody) {
            func = new BodyOnSubscribe<>(callFunc);
        } else if (isPaging) {
            func = new GitHubListOnSubscribe<>(callFunc);
        } else {
            func = callFunc;
        }
        Observable<?> observable = Observable.create(func);

        if (schedulerSubscribeOn != null) {
            observable = observable.subscribeOn(schedulerSubscribeOn);
        }

        if (schedulerObserveOn != null) {
            observable = observable.observeOn(schedulerObserveOn);
        }

        if (isSingle) {
            return observable.toSingle();
        }
        if (isCompletable) {
            return observable.toCompletable();
        }
        return observable;
    }
}
