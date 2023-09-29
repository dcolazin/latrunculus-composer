package org.vetronauta.latrunculus.math.arith.number;

import org.vetronauta.latrunculus.core.DeepCopyable;

/**
 * Ring elements available to be used in a <code>RingString</code> class.
 * @author vetronauta
 */
public abstract class ArithmeticNumber<T extends ArithmeticNumber<T>>
        extends Number implements Comparable<T>, DeepCopyable<T> {

    public abstract boolean isZero();

    /**
     * pure sum between this number and the parameter
     */
    public abstract T sum(T y);

    /**
     * pure difference between this number and the parameter
     */
    public abstract T difference(T y);

}
