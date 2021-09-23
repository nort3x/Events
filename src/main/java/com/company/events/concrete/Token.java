package com.company.events.concrete;

import com.company.events.basics.WeakHandler;
import com.company.events.interfaces.IHandler;
import com.company.events.interfaces.IListener;

/**
 * Token can be considered as ownership of a {@link IListener}
 * different Handlers have different reaction to a token being lost by caller
 *
 * @param <DataType>
 * @author H. Ardaki
 * @see WeakHandler
 */
public final class Token<DataType> {
    final private IListener<DataType> correspondingIListener;
    final private IHandler correspondingHandler;

    /**
     * @param correspondingIListener listener which this token is being generated for
     * @param correspondingHandler   handler who generated this token
     */
    public Token(IListener<DataType> correspondingIListener, IHandler correspondingHandler) {
        this.correspondingIListener = correspondingIListener;
        this.correspondingHandler = correspondingHandler;
    }


    public IListener<DataType> getCorrespondingListener() {
        return correspondingIListener;
    }

    public IHandler getCorrespondingHandler() {
        return correspondingHandler;
    }


    /**
     * removes corresponding Listener from corresponding Handler
     */
    public void removeListener() {
        correspondingHandler.removeListener(this);
    }
}
