package com.anxin.kitchen.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EventBusFactory extends de.greenrobot.event.EventBus {
    private static volatile EventBusFactory defaultInstance;
    private final ScheduledExecutorService mExecutorService;

    private EventBusFactory() {
        super();
        mExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public ScheduledFuture<Object> postDelayed(Object event, long delay) {
        return mExecutorService.schedule(new PostEventCallable(this, event), delay, TimeUnit.MILLISECONDS);
    }

    private class PostEventCallable implements Callable<Object> {
        private final EventBusFactory mEventBus;
        private final Object mEvent;

        public PostEventCallable(EventBusFactory eventBus, Object event) {
            mEventBus = eventBus;
            mEvent = event;
        }

        @Override
        public Object call() throws Exception {
            mEventBus.post(mEvent);
            return null;
        }
    }

    public static EventBusFactory getInstance() {
        if (defaultInstance == null) {
            synchronized (EventBusFactory.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBusFactory();
                }
            }
        }
        return defaultInstance;
    }

    public static void postEvent(Object event) {
        getInstance().post(event);
    }

    public static void postStickyEvent(Object event) {
        getInstance().postSticky(event);
    }

    public static <T> T getSticky(Class<T> eventType) {
        return getInstance().getStickyEvent(eventType);
    }
}