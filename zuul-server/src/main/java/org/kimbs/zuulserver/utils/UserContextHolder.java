package org.kimbs.zuulserver.utils;

import org.springframework.util.Assert;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContexts = new ThreadLocal<>();

    public static final UserContext getContext() {
        UserContext context = userContexts.get();

        if (context == null) {
            context = createEmptyContext();
            userContexts.set(context);
        }
        return userContexts.get();
    }

    public static final void setContext(UserContext userContext) {
        Assert.notNull(userContext, "Only non-null UserContext instances are permitted");
        userContexts.set(userContext);
    }
    public static final UserContext createEmptyContext() {
        return new UserContext();
    }
}
