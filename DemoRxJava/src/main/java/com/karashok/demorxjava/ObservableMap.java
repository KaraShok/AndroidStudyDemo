package com.karashok.demorxjava;

import android.util.Log;


/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 被观察者数据转换
 */
public class ObservableMap<T, R> extends Observable<R> {

    private final Function<? super T, ? extends R> function;
    private final ObservableSource<T> source;

    public ObservableMap(Function<? super T, ? extends R> function, ObservableSource<T> source) {
        Log.d("DemoRxJava", "ObservableMap---constructor");
        this.function = function;
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observe<? super R> observer) {
        Log.d("DemoRxJava", "ObservableMap---subscribeActual");
        source.subscribe(new ObserveMap<T, R>(function, observer));
    }

    static final class ObserveMap<T, R> implements Observe<T>, Disposable {

        private final Function<? super T, ? extends R> function;
        private final Observe<? super R> source;

        ObserveMap(Function<? super T, ? extends R> function, Observe<? super R> source) {
            this.function = function;
            this.source = source;
        }

        @Override
        public void onSubscribe(Disposable d) {
            source.onSubscribe(d);
        }

        @Override
        public void onNext(T value) {
            if (!disposabled) {
                source.onNext(function.apply(value));
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

        private Boolean disposabled = false;

        @Override
        public Boolean isDisposable() {
            return disposabled;
        }

        @Override
        public void disposable() {
            disposabled = true;
        }
    }
}
