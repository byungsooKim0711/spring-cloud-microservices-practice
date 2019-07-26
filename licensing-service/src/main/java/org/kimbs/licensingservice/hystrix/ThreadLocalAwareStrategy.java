package org.kimbs.licensingservice.hystrix;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import org.kimbs.licensingservice.utils.UserContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy hystrixConcurrencyStrategy;

    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy hystrixConcurrencyStrategy) {
        this.hystrixConcurrencyStrategy = hystrixConcurrencyStrategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return hystrixConcurrencyStrategy != null
                ? hystrixConcurrencyStrategy.getBlockingQueue(maxQueueSize)
                : super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return hystrixConcurrencyStrategy != null
                ? hystrixConcurrencyStrategy.wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext()))
                : super.wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext()));
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return hystrixConcurrencyStrategy != null
                ? hystrixConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue)
                : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return hystrixConcurrencyStrategy != null
                ? hystrixConcurrencyStrategy.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }
}
