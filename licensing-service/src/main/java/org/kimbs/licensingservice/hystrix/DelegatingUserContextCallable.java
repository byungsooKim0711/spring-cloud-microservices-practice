package org.kimbs.licensingservice.hystrix;

import org.kimbs.licensingservice.utils.UserContext;
import org.kimbs.licensingservice.utils.UserContextHolder;

import java.util.concurrent.Callable;

public final class DelegatingUserContextCallable<V> implements Callable<V> {

    private final Callable<V> delegate;

    private UserContext originUserContext;

    public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
        this.delegate = delegate;
        this.originUserContext = userContext;
    }

    public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }

    @Override
    public V call() throws Exception {
        UserContextHolder.setContext(originUserContext);

        try {
            return delegate.call();
        } finally {
            this.originUserContext = null;
        }
    }
}
