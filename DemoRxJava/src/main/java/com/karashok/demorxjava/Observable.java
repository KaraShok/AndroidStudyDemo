package com.karashok.demorxjava;

/**
 * @author KaraShokZ
 * @since 05-07-2022
 * 被观察者
 */
public abstract class Observable<T> implements ObservableSource<T> {

    /**
     * 创建被观察者
     * @param source
     * @param <T>
     * @return
     */
    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        System.out.println("Observable---create");
        return new ObservableCreate<T>(source);
    }

    /**
     * 订阅观察者
     * @param observer
     */
    @Override
    public void subscribe(Observe<? super T> observer) {
        System.out.println(this.getClass().getSimpleName() + "---subscribe");
        subscribeActual(observer);
    }

    /**
     * 被观察者处理
     * @param observer
     */
    protected abstract void subscribeActual(Observe<? super T> observer);

    /**
     * 被观察者数据转换
     * @param function
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(Function<? super T, ? extends R> function) {
        System.out.println("Observable---map");
        return new ObservableMap<T, R>(function, this);
    }

    /**
     * 被观察者切换线程
     * @param scheduler
     * @return
     */
    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new ObservableSubscribeOn<>(scheduler, this);
    }

    /**
     * 观察者切换线程
     * @param scheduler
     * @return
     */
    public Observable<T> observeOn(Scheduler scheduler) {
        return new ObservableObserveOn<>(scheduler, this);
    }
}
