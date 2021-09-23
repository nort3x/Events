package com.company.events.basics;

import com.company.events.interfaces.IListener;
import com.company.events.concrete.Token;

import java.util.*;

/**
 * @author H. Ardaki
 */
@SuppressWarnings("rawtypes")
public abstract class WeakHandler extends BasicHandler {

    Map<Object, WeakHashMap<Token, IListener>> listenerMap = Collections.synchronizedMap(new WeakHashMap<>());

    @Override
    public <DataType> Token<DataType> addListener(IListener<DataType> listener) {
        Token<DataType> t = new Token<>(listener, this);
        listenerMap.computeIfAbsent(
                listener.getObservationTarget(),
                givenObservationTarget -> new WeakHashMap<>()
        ).put(t, listener);
        return t;
    }

    @Override
    public void removeListener(Token token) {
        listenerMap.computeIfAbsent(
                token.getCorrespondingListener().getObservationTarget(),
                givenObservationTarget -> new WeakHashMap<>()
        ).remove(token);
    }

    @Override
    protected Iterable<IListener> supplyListeners(Object observationTarget) {
        WeakHashMap<Token, IListener> registeredListeners = listenerMap.computeIfAbsent(
                observationTarget,
                givenObservationTarget -> new WeakHashMap<>()
        );
        return registeredListeners.values();
    }
}
