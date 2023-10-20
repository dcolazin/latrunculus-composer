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
import org.vetronauta.latrunculus.core.math.module.FoldingModule;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vetronauta
 */
@Getter
public class ArithmeticElement<N extends ArithmeticNumber<N>> extends RingElement<ArithmeticElement<N>> {

    //TODO ditch ArithmeticNumber and just use ArithmeticElement?

    @NonNull
    private N value;

    public ArithmeticElement(@NonNull N value) {
        this.value = value;
    }

    @Override
    public FreeElement<?, ArithmeticElement<N>> resize(int n) {
        if (n == 1) {
            return this;
        }
        List<ArithmeticElement<N>> res = new ArrayList<>(n);
        if (n > 0) {
            res.add(this.deepCopy());
        }
        ArithmeticElement<N> zero = getRing().getZero();
        for (int i = 1; i < n; i++) {
            res.add(zero);
        }
        return new ArithmeticMultiElement<>(res);
    }

    @Override
    public boolean isZero() {
        return value.isZero();
    }

    @Override
    public ArithmeticElement<N> scaled(@NonNull ArithmeticElement<N> element) {
        return product(element);
    }

    @Override
    public void scale(@NonNull ArithmeticElement<N> element) {
        multiply(element);
    }

    @Override
    public ArithmeticElement<N> sum(@NonNull ArithmeticElement<N> element) {
        return new ArithmeticElement<>(value.sum(element.getValue()));
    }

    @Override
    public void add(@NonNull ArithmeticElement<N> element) {
        value = value.sum(element.getValue());
    }

    @Override
    public ArithmeticElement<N> difference(@NonNull ArithmeticElement<N> element) {
        return new ArithmeticElement<>(value.difference(element.getValue()));
    }

    @Override
    public void subtract(@NonNull ArithmeticElement<N> element) {
        value = value.difference(element.getValue());
    }

    @Override
    public ArithmeticElement<N> negated() {
        return new ArithmeticElement<>(value.neg());
    }

    @Override
    public void negate() {
        value = value.neg();
    }

    @Override
    public double[] fold(ModuleElement<?, ?>[] elements) {
        return FoldingModule.fold(this.getRing(), elements);
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
    public ArithmeticElement<N> product(@NonNull ArithmeticElement<N> element) {
        return new ArithmeticElement<>(value.product(element.getValue()));
    }

    @Override
    public void multiply(@NonNull ArithmeticElement<N> element) {
        value = value.product(element.getValue());
    }

    @Override
    public boolean isInvertible() {
        return value.isInvertible();
    }

    @Override
    public ArithmeticElement<N> inverse() {
        return new ArithmeticElement<>(value.inverse());
    }

    @Override
    public void invert() {
        value = value.inverse();
    }

    @Override
    public ArithmeticElement<N> quotient(@NonNull ArithmeticElement<N> element) throws DivisionException {
        return new ArithmeticElement<>(value.quotient(element.getValue()));
    }

    @Override
    public void divide(@NonNull ArithmeticElement<N> element) throws DivisionException {
        value = value.quotient(element.getValue());
    }

    @Override
    public boolean divides(@NonNull RingElement<?> element) {
        return (element instanceof ArithmeticElement) && (value.divides(((ArithmeticElement<?>) element).value));
    }

    @Override
    public ArithmeticRing<N> getRing() {
        return ArithmeticRingRepository.getRing(this);
    }

    public boolean isFieldElement() {
        return value.isFieldElement();
    }

    @Override
    public ArithmeticElement<N> deepCopy() {
        return new ArithmeticElement<>(value.deepCopy());
    }

    @Override
    public int compareTo(ModuleElement object) {
        if (object instanceof ArithmeticElement) {
            Object otherValue = ((ArithmeticElement<?>)object).value;
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
            return getValue().equals(((ArithmeticElement<?>)object).getValue());
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
