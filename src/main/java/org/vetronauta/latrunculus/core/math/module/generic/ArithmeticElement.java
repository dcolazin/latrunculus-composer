/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.module.generic;

import lombok.Getter;
import lombok.NonNull;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.exception.LatrunculusUnsupportedException;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.lang.reflect.Array;

/**
 * @author vetronauta
 */
@Getter
public abstract class ArithmeticElement<T extends ArithmeticElement<T,N>, N extends ArithmeticNumber<N>> extends RingElement<T> {

    //TODO ditch ArithmeticNumber and just use ArithmeticElement?

    @NonNull
    private N value;

    protected ArithmeticElement(@NonNull N value) {
        this.value = value;
    }

    protected abstract T valueOf(@NonNull N value);

    @Override
    public FreeElement<?, T> resize(int n) {
        if (n == 1) {
            return this;
        }
        T[] array = (T[]) Array.newInstance(value.getClass(), n);
        if (array.length > 0) {
            array[0] = this.deepCopy();
        }
        T zero = getRing().getZero();
        for (int i = 1; i < array.length; i++) {
            array[i] = zero;
        }
        return new ArithmeticMultiElement<>(array);
    }

    @Override
    public boolean isZero() {
        return value.isZero();
    }

    @Override
    public T scaled(@NonNull T element) {
        return product(element);
    }

    @Override
    public void scale(@NonNull T element) {
        multiply(element);
    }

    @Override
    public T sum(@NonNull T element) {
        return valueOf(value.sum(element.getValue()));
    }

    @Override
    public void add(@NonNull T element) {
        value = value.sum(element.getValue());
    }

    @Override
    public T difference(@NonNull T element) {
        return valueOf(value.difference(element.getValue()));
    }

    @Override
    public void subtract(@NonNull T element) {
        value = value.difference(element.getValue());
    }

    @Override
    public T negated() {
        return valueOf(value.neg());
    }

    @Override
    public void negate() {
        value = value.neg();
    }

    @Override
    public String stringRep(boolean... parens) {
        String representation = value.toString();
        if (parens.length > 0 && !Character.isDigit(representation.charAt(0))) {
            return TextUtils.parenthesize(representation);
        }
        return representation;
    }

    @Override
    public boolean isOne() {
        return value.isOne();
    }

    @Override
    public T product(@NonNull  T element) {
        return valueOf(value.product(element.getValue()));
    }

    @Override
    public void multiply(@NonNull T element) {
        value = value.product(element.getValue());
    }

    @Override
    public boolean isInvertible() {
        return value.isInvertible();
    }

    @Override
    public T inverse() {
        return valueOf(value.inverse());
    }

    @Override
    public void invert() {
        value = value.inverse();
    }

    @Override
    public T quotient(@NonNull T element) throws DivisionException {
        return valueOf(value.quotient(element.getValue()));
    }

    @Override
    public void divide(@NonNull T element) throws DivisionException {
        value = value.quotient(element.getValue());
    }

    @Override
    public boolean divides(@NonNull RingElement<?> element) {
        return (element instanceof ArithmeticElement) && (value.divides(((ArithmeticElement<?,?>) element).value));
    }

    public boolean isFieldElement() {
        return value.isFieldElement();
    }

    @Override
    public T deepCopy() {
        return valueOf(value.deepCopy());
    }

    @Override
    public int compareTo(ModuleElement object) {
        if (object instanceof ArithmeticElement) {
            Object otherValue = ((ArithmeticElement<?,?>)object).value;
            if (value.getClass().isAssignableFrom(otherValue.getClass())) {
                return value.compareTo((N) otherValue);
            }
        }
        return super.compareTo(object);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof ArithmeticElement) {
            return getValue().equals(((ArithmeticElement<?,?>)object).getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("ArithmeticElement<%s>[%s]", value.getClass().getSimpleName(), value);
    }

    public String getElementTypeName() {
        return value.getClass().getSimpleName();
    }

}
