package com.mbtex.palpay.Helper;


/**
 * Abstract Activity State class
 */
abstract public class LocalActivityState {

    public LocalActivityState() {
    }

    public abstract void updateState(Object ... arguments);
}
