package com.company.events.concrete;

import com.company.events.basics.BasicHandler;
import com.company.events.interfaces.IEvent;
import com.company.events.interfaces.IHandler;
import com.company.events.interfaces.IListener;

/**{@inheritDoc}
 * @author H. Ardaki
 */
public class Event<DataType> implements IEvent<DataType> {

    Thread creatorThread;
    Thread publisherThread;

    private final Object emissionTarget;
    private final DataType data;

    /** encapsulate targeting object and data this event must carry, (it will be shared across all {@link IListener}s )
     * use supplier as Data if you need to supply distinct instances for each listener,
     * if this is just an invocation Event and doesn't need to provide any data use {@link Void} as parameter_type and pass null as data
     * @param emissionTarget
     * @param data
     */
    public Event(Object emissionTarget, DataType data){
        creatorThread = Thread.currentThread();
        this.emissionTarget = emissionTarget;
        this.data = data;
    }

    /**{@inheritDoc}
     */
    @Override
    public Object getEmissionTarget() {
        return emissionTarget;
    }

    /**{@inheritDoc}
     */
    @Override
    public DataType getData() {
        return data;
    }

    /**{@inheritDoc}
     */
    @Override
    public Thread getPublisherThread() {
        return publisherThread;
    }


    /**{@inheritDoc}
     */
    @Override
    public Thread getCreatorThread() {
        return creatorThread;
    }

    /** must be called manually if you are using custom {@link IHandler}
     * @param thread thread which is publishing this event
     * @see BasicHandler#emit(IEvent)
     */
    public void setPublisherThread(Thread thread) {
        publisherThread = thread;
    }
}
