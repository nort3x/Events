package com.company.events.basics;

import com.company.events.concrete.Event;
import com.company.events.interfaces.IEvent;
import com.company.events.interfaces.IHandler;
import com.company.events.interfaces.IListener;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author H. Ardaki
 */
@SuppressWarnings("rawtypes")
public abstract class BasicHandler implements IHandler {

    /**
     * @param observationTarget requested observationTarget
     * @return list of all registered {@link IListener}s observing it
     * @see IListener
     */
    abstract protected Iterable<IListener> supplyListeners(Object observationTarget);

    /**@return service for handling invocation of {@link IListener#accept(Object)}
     */
    abstract protected ExecutorService getExecutorService();



    /*
    given Generic IEvent is wrapped inside Event for fulfilling getPublisherThread
    and not breaking API
     */
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public  void emit(IEvent event) {
        Event wrappedEvent = new Event<>(event.getEmissionTarget(), event.getData());
        wrappedEvent.setPublisherThread(Thread.currentThread());
        emit(wrappedEvent);
    }


    /**
     * thread which call this thread will be automatically be assigned as publisher thread of given event
     * @param event to be publisher
     * @see BasicHandler#emit(IEvent)
     */
    @SuppressWarnings("rawtypes")
    public void emit(Event event) {
        event.setPublisherThread(Thread.currentThread());
        Iterable<IListener> IListeners = supplyListeners(event.getEmissionTarget());
        if (IListeners != null) {
            Iterator<IListener> it = IListeners.iterator();
            // todo: i left this as is, because i don't know whether modification of set will throw an-
            //  -Exception in enhanced for loop or not
            while (it.hasNext()) {
                IListener l = it.next();
                getExecutorService().submit(() -> l.accept(event));
            }
        }
    }
}
