package org.vetronauta.latrunculus.core.math.arith.number;

import org.vetronauta.latrunculus.core.DeepCopyable;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;

/**
 * Ring elements available to be used in a <code>RingString</code> class.
 * @author vetronauta
 */
public interface ArithmeticNumber<T extends ArithmeticNumber<T>> extends Comparable<T>, DeepCopyable<T> {

    //TODO mutable versions to improve array/matrix operations

    boolean isZero();

    boolean isOne();

    boolean isInvertible();

    boolean isFieldElement();

    boolean divides(ArithmeticNumber<?> y);

    /**
     * pure sum between this number and the parameter
     */
    T sum(T y);

    /**
     * pure difference between this number and the parameter
     */
    T difference(T y);

    /**
     * pure product between this number and the parameter
     */
    T product(T y);

    /**
     * pure quotient between this number and the parameter
     */
    default T quotient(T y) throws DivisionException {
        return this.product(y.inverse());
    }

    /**
     * pure negation of this number
     */
    T neg();

    /**
     * pure inversion of this number
     */
    T inverse();

    int intValue();

    long longValue();

    float floatValue();

    double doubleValue();

    default byte byteValue() {
        return (byte) this.intValue();
    }

    default short shortValue() {
        return (short) this.intValue();
    }

}
