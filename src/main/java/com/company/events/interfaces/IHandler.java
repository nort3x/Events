package com.company.events.interfaces;


import com.company.events.concrete.Token;

/** Handler is responsible for acting as a MessageBroker between publisher and listeners
 * anyone can emit an {@link IEvent} on Handler with any-type and point to a target and listeners should be able to retrieve it by registering on Handler
 * @author H. Ardaki
 */
public interface IHandler {
    /** will register provided listener in this Handler
     * @param IListener provided listener
     * @param <DataType> DataType which Listener will should handle
     * @return  token corresponding to this listener and this Handler
     */
    <DataType> Token<DataType> addListener(IListener<DataType> IListener);

    /** will remove corresponding {@link IListener} from this handler if exist
     * @param token corresponding token to {@link IListener}
     */

    @SuppressWarnings("rawtypes")
    void removeListener(Token token);


    @SuppressWarnings("rawtypes")
    void emit(IEvent IEvent);

}
