package com.company.events.interfaces;


import java.util.function.Consumer;

/** Listener will encapsulate any "Reaction" to an invocation whether received event is actually carrying Data or not
 *  it's implementor responsibility to catch all exceptions
 * @param <DataType>
 *     @author H. Ardaki
 */
public interface IListener<DataType> extends Consumer<IEvent<DataType>> {
    /** ObservationTarget is an Object instance which {@link IHandler} will figure out whether it should invoke this IListener or not based on
     * <br/>
     * Handler will determine coupling of Event-Listener based on equality of this object and incoming {@link IEvent#getEmissionTarget()}
     * @return Object instance which this IListener is observing on
     */
    Object getObservationTarget();
}
