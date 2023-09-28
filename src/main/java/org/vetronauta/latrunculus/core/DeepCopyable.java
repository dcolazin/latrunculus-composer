package org.vetronauta.latrunculus.core;

/**
 * Interface to enforce a Cloneable-like typed contract
 * @see java.lang.Cloneable
 * @author vetronauta
 */
public interface DeepCopyable<T> {

    T deepCopy();

}
