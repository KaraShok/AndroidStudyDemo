package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 被观察者线程切换
 */
public class ObservableSubscribeOn<T> extends Observable<T> {

    private final Scheduler scheduler;
    private final ObservableSource<T> source;

    public ObservableSubscribeOn(Scheduler scheduler, ObservableSource<T> source) {
        this.scheduler = scheduler;
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observe<? super T> observer) {
        SubscribeOnObserve<T> parent = new SubscribeOnObserve<>(observer);
        observer.onSubscribe(parent);
        scheduler.scheduleDirect(new SubscribeOnTask<T>(source, parent, scheduler));
    }

    static final class SubscribeOnObserve<T> implements Observe<T>, Disposable {

        private final Observe<? super T> source;

        SubscribeOnObserve(Observe<? super T> source) {
            this.source = source;
        }

        @Override
        public void onSubscribe(Disposable d) {
            source.onSubscribe(d);
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

    static final class SubscribeOnTask<T> implements Runnable {

        private final ObservableSource<T> source;
        private final SubscribeOnObserve<T> parent;

        private final Scheduler scheduler;

        SubscribeOnTask(ObservableSource<T> source, SubscribeOnObserve<T> parent, Scheduler scheduler) {
            this.source = source;
            this.parent = parent;
            this.scheduler = scheduler;
        }

        @Override
        public void run() {
            source.subscribe(parent);
            scheduler.close();
        }
    }
}
