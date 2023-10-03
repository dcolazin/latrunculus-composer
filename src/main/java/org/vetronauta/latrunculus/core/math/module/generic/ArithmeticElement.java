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
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.exception.LatrunculusUnsupportedException;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.lang.reflect.Array;

/**
 * @author vetronauta
 */
@Getter
public class ArithmeticElement<T extends ArithmeticNumber<T>> extends RingElement<ArithmeticElement<T>> {

    private T value;

    public ArithmeticElement(T value) {
        this.value = value;
    }

    @Override
    public FreeElement<?, ArithmeticElement<T>> resize(int n) {
        if (n == 1) {
            return this;
        }
        T[] array = (T[]) Array.newInstance(value.getClass(), n);
        T zero = value.difference(value); //TODO getRing...
        if (array.length > 0) {
            array[0] = value;
        }
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
    public ArithmeticElement<T> scaled(ArithmeticElement<T> element) {
        return product(element);
    }

    @Override
    public void scale(ArithmeticElement<T> element) {
        multiply(element);
    }

    @Override
    public ArithmeticElement<T> sum(ArithmeticElement<T> element) {
        return new ArithmeticElement<>(value.sum(element.value));
    }

    @Override
    public void add(ArithmeticElement<T> element) {
        value = value.sum(element.value);
    }

    @Override
    public ArithmeticElement<T> difference(ArithmeticElement<T> element) {
        return new ArithmeticElement<>(value.difference(element.value));
    }

    @Override
    public void subtract(ArithmeticElement<T> element) {
        value = value.difference(element.value);
    }

    @Override
    public ArithmeticElement<T> negated() {
        return new ArithmeticElement<>(value.neg());
    }

    @Override
    public void negate() {
        value = value.neg();
    }

    @Override
    public double[] fold(ModuleElement<?, ?>[] elements) {
        throw new LatrunculusUnsupportedException(); //TODO
    }

    @Override
    public Module<ArithmeticElement<T>, ArithmeticElement<T>> getModule() {
        throw new LatrunculusUnsupportedException(); //TODO
    }

    @Override
    public String stringRep(boolean... parens) {
        return parens.length > 0 ? TextUtils.parenthesize(value.toString()) : value.toString();
    }

    @Override
    public boolean isOne() {
        return value.isOne();
    }

    @Override
    public ArithmeticElement<T> product(ArithmeticElement<T> element) {
        return new ArithmeticElement<>(value.product(element.value));
    }

    @Override
    public void multiply(ArithmeticElement<T> element) {
        value = value.product(element.value);
    }

    @Override
    public boolean isInvertible() {
        return value.isInvertible();
    }

    @Override
    public ArithmeticElement<T> inverse() {
        return new ArithmeticElement<>(value.inverse());
    }

    @Override
    public void invert() {
        value = value.inverse();
    }

    @Override
    public ArithmeticElement<T> quotient(ArithmeticElement<T> element) throws DivisionException {
        return new ArithmeticElement<>(value.quotient(element.value));
    }

    @Override
    public void divide(ArithmeticElement<T> element) throws DivisionException {
        value = value.quotient(element.value);
    }

    @Override
    public boolean divides(RingElement<?> element) {
        return (element instanceof ArithmeticElement) && (value.divides(((ArithmeticElement<?>) element).value));
    }

    @Override
    public ArithmeticElement<T> deepCopy() {
        return new ArithmeticElement<>(value.deepCopy());
    }

}
