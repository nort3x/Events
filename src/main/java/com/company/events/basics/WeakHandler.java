package com.company.events.basics;

import com.company.events.interfaces.IListener;
import com.company.events.concrete.Token;

import java.util.*;

/**
 * @author H. Ardaki
 */
@SuppressWarnings("rawtypes")
public abstract class WeakHandler extends BasicHandler {


    // outer map is synchronized
    final private Map<Object, WeakHashMap<Token, IListener>> listenerMap = new WeakHashMap<>();

    @Override
    public  <DataType> Token<DataType> addListener(IListener<DataType> listener) {
        synchronized (listenerMap) {
            Token<DataType> t = new Token<>(listener, this);
            listenerMap.computeIfAbsent(
                    listener.getObservationTarget(),
                    givenObservationTarget -> new WeakHashMap<>() // inner map is not
            ).put(t, listener);
            return t;
        }
    }

    @Override
    public void removeListener(Token token) {
        synchronized (listenerMap) {
            WeakHashMap<Token, IListener> map = listenerMap.get(token.getCorrespondingListener().getObservationTarget());
            if (map != null) {
                map.remove(token);
                if (map.size() == 0)
                    listenerMap.remove(token.getCorrespondingListener().getObservationTarget());
            }
        }
    }

    @Override
    protected Iterable<IListener> supplyListeners(Object observationTarget) {
        WeakHashMap<Token, IListener> registeredListeners = listenerMap.computeIfAbsent(
                observationTarget,
                givenObservationTarget -> new WeakHashMap<>()
        );
        return registeredListeners.values();
    }

    /**
     * @see WeakHashMap#clear()
     */
    @Override
    public  void clearAllListeners() {
        synchronized (listenerMap) {
            listenerMap.clear();
        }
    }

    public int getNumberOfListeners() {
        return listenerMap.values().stream().mapToInt(WeakHashMap::size).sum();
    }

    public int getNumberOfTargets() {
        return listenerMap.keySet().size();
    }
}
