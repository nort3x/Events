package com.company.events.interfaces;


import java.util.function.Supplier;

/**Events can be assumed as carrier of a message between emitter to {@link IListener}(s)
 * in special case of when DataType is {@link Void} it can act as an invoker of a runnable as well
 * @param <DataType> Type of the Data Event is carrying on , null when {@link Void}
 * @author H. Ardaki
 */
public interface IEvent<DataType> {
    /**@return attached Data provided by emitter (it's shared between all listeners, pass {@link Supplier} if you need distinct data for each listener )
     */
    DataType getData();

    /**@return thread which published this event to {@link IHandler}
     */
    Thread getPublisherThread();

    /**@return thread which created this event
     */
    Thread getCreatorThread();

    /**EmissionTarget is an Object instance which {@link IHandler} will figure out corresponding {@link IListener} to invoke based on it
     * <br/>
     * Handler will determine coupling of Event-Listener based on equality of {@link IListener#getObservationTarget()} and provided Object by this method
     * @return Object instance which this event is targeting to
     */
    Object getEmissionTarget();

}
