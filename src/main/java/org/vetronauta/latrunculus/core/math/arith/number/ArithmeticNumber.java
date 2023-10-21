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

    boolean divides(ArithmeticNumber<?> other);

    /**
     * pure sum between this number and the parameter
     */
    T sum(T other);

    /**
     * pure difference between this number and the parameter
     */
    T difference(T other);

    /**
     * pure product between this number and the parameter
     */
    T product(T other);

    /**
     * pure quotient between this number and the parameter
     */
    default T quotient(T other) throws DivisionException {
        return this.product(other.inverse());
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
