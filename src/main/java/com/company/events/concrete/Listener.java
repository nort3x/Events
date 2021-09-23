package com.company.events.concrete;

import com.company.events.interfaces.IEvent;
import com.company.events.interfaces.IListener;

import java.util.function.Consumer;


/**{@inheritDoc}
 * @author H. Ardaki
 */
public class Listener<DataType> implements IListener<DataType> {

    private final Object observationTarget;
    private final Consumer<IEvent<DataType>> onEvent;

    /**
     * encapsulates observationTarget and reaction to events targeting it
     * @param observationTarget
     * @param onEvent
     */
    public Listener(Object observationTarget , Consumer<IEvent<DataType>> onEvent){
        this.observationTarget = observationTarget;
        this.onEvent = onEvent;
    }

    /**
     * invoking event can contain data depending on emitter
     * its implementor responsibility to type check provided data by Event, and catch all exception in this method,
     * otherwise it can result in UB and concurrency issues
     * @param IEvent invoking event
     */
    @Override
    public void accept(IEvent<DataType> IEvent) {
        onEvent.accept(IEvent);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Object getObservationTarget() {
        return observationTarget;
    }
}
