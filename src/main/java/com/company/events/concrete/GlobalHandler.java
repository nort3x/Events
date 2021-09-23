package com.company.events.concrete;


import com.company.events.basics.WeakHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/** singleton class which will provide a {@link WeakHandler} backed with {@link ForkJoinPool#commonPool()}
 * @author H. Ardaki
 */
public class GlobalHandler extends WeakHandler {
    private GlobalHandler(){}
    static private final GlobalHandler instance;
    static {
        instance = new GlobalHandler();
    }
    public static GlobalHandler getInstance() {
        return instance;
    }

    @Override
    protected ExecutorService getExecutorService() {
        return ForkJoinPool.commonPool();
    }
}
