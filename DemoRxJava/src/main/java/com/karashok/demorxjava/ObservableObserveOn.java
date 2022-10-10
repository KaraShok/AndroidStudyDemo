package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 观察者线程切换
 */
public class ObservableObserveOn<T> extends Observable<T> {

    private final Scheduler scheduler;
    private final ObservableSource<T> source;

    public ObservableObserveOn(Scheduler scheduler, ObservableSource<T> source) {
        this.scheduler = scheduler;
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observe<? super T> observer) {
        ObserveOnObserve<T> parent = new ObserveOnObserve<>(observer,scheduler);
        observer.onSubscribe(parent);
        source.subscribe(parent);
    }

    static final class ObserveOnObserve<T> implements Observe<T>,Disposable {
        private final Observe<? super T> source;
        private final Scheduler scheduler;

        public ObserveOnObserve(Observe<? super T> source, Scheduler scheduler) {
            this.source = source;
            this.scheduler = scheduler;
        }


        @Override
        public void onSubscribe(Disposable d) {
            scheduler.scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    if (!disposabled) {
                        source.onSubscribe(d);
                    }
                }
            });
        }

        @Override
        public void onNext(T value) {
            scheduler.scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    if (!disposabled) {
                        source.onNext(value);
                    }
                }
            });
        }

        @Override
        public void onError(Throwable throwable) {
            scheduler.scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    if (!disposabled) {
                        source.onError(throwable);
                    }
                }
            });
        }

        @Override
        public void onComplete() {
            scheduler.scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    if (!disposabled) {
                        source.onComplete();
                    }
                }
            });
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
