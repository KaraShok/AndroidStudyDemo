package com.karashok.demorxjava;

import android.util.Log;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 创建被观察者
 */
public class ObservableCreate<T> extends Observable<T> {
    final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        Log.d("DemoRxJava", "ObservableCreate---constructor");
        this.source = source;
    }

    /**
     * 绑定观察者
     * @param observer
     */
    @Override
    protected void subscribeActual(Observe<? super T> observer) {
        Log.d("DemoRxJava", "ObservableCreate---subscribeActual");
        CreateObservableEmitter<T> parent = new CreateObservableEmitter<T>(observer);
        observer.onSubscribe(parent);

        try {
            source.subscribe(parent);
        } catch (Exception e) {
            parent.onError(e);
        }
    }

    /**
     * 被观察者发射器
     * @param <T>
     */
    static final class CreateObservableEmitter<T> implements Emitter<T>, Disposable {

        private Boolean disposabled = false;
        private final Observe<? super T> source;

        CreateObservableEmitter(Observe<? super T> source) {
            this.source = source;
        }

        @Override
        public Boolean isDisposable() {
            return disposabled;
        }

        @Override
        public void disposable() {
            disposabled = true;
        }

        @Override
        public void onNext(T value) {
            if (!disposabled) {
                source.onNext(value);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (!disposabled) {
                source.onError(throwable);
            }
        }

        @Override
        public void onComplete() {
            if (!disposabled) {
                source.onComplete();
            }
        }
    }
}
