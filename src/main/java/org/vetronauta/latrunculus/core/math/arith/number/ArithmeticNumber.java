package org.vetronauta.latrunculus.core.math.arith.number;

import org.vetronauta.latrunculus.core.DeepCopyable;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;

/**
 * Ring elements available to be used in a <code>RingString</code> class.
 * @author vetronauta
 */
public abstract class ArithmeticNumber<T extends ArithmeticNumber<T>>
        extends Number implements Comparable<ArithmeticNumber<?>>, DeepCopyable<T> {

    //TODO mutable versions to improve array/matrix operations

    public abstract boolean isZero();

    public abstract boolean isOne();

    public abstract boolean isInvertible();

    public abstract boolean divides(ArithmeticNumber<?> y);

    /**
     * pure sum between this number and the parameter
     */
    public abstract T sum(T y);

    /**
     * pure difference between this number and the parameter
     */
    public abstract T difference(T y);

    /**
     * pure product between this number and the parameter
     */
    public abstract T product(T y);

    /**
     * pure quotient between this number and the parameter
     */
    public T quotient(T y) throws DivisionException {
        return this.product(y.inverse());
    }

    /**
     * pure negation of this number
     */
    public abstract T neg();

    /**
     * pure inversion of this number
     */
    public abstract T inverse();

    public abstract String toString();

}
