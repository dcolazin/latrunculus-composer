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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author vetronauta
 */
@Getter
public class ArithmeticMultiElement<T extends ArithmeticNumber<T>>
        extends ProperFreeElement<ArithmeticMultiElement<T>, ArithmeticElement<T>> {

    private final T[] value;

    public ArithmeticMultiElement(T[] value) {
        if (value == null || value.length < 1) {
            throw new IllegalArgumentException("MultiElement must have length > 1");
        }
        this.value = value;
    }

    public static <I extends ArithmeticNumber<I>> FreeElement<?, ArithmeticElement<I>> make(I[] array) {
        if (array.length == 1) {
            return new ArithmeticElement<>(array[0]);
        }
        return new ArithmeticMultiElement<>(array);
    }

    @Override
    public ModuleElement<ArithmeticMultiElement<T>, ArithmeticElement<T>> deepCopy() {
        T[] v = Arrays.copyOf(value, value.length); //TODO is there a better way?
        for (int i = 0; i < getLength(); i++) {
            v[i] = value[i].deepCopy();
        }
        return new ArithmeticMultiElement<>(v);
    }

    @Override
    public RingElement<ArithmeticElement<T>> getRingElement(int i) {
        if (i >= value.length) {
            throw new IndexOutOfBoundsException(String.format("Cannot access index %d for MultiElement of length %d", i, value.length));
        }
        return new ArithmeticElement<>(value[i]);
    }

    @Override
    public ArithmeticMultiElement<T> productCW(ArithmeticMultiElement<T> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        T[] res = Arrays.copyOf(value, value.length); //TODO is there a better way?
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].product(element.value[i]);
        }
        return new ArithmeticMultiElement<>(res);
    }

    @Override
    public void multiplyCW(ArithmeticMultiElement<T> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value[i] = value[i].product(element.value[i]);
        }
    }

    @Override
    public FreeElement<?, ArithmeticElement<T>> resize(int n) {
        if (n == value.length) {
            return this;
        }
        T[] res = Arrays.copyOf(value, n);
        T zero = res[0].difference(res[0]); //TODO getRing might be useful for this...
        for (int i = value.length; i < n; i++) {
            res[i] = zero;
        }
        return ArithmeticMultiElement.make(res);
    }

    @Override
    public boolean isZero() {
        return Arrays.stream(value).allMatch(ArithmeticNumber::isZero);
    }

    @Override
    public ArithmeticMultiElement<T> scaled(ArithmeticElement<T> element) throws DomainException {
        T[] res = Arrays.copyOf(value, value.length); //TODO is there a better way?
        for (int i = 0; i < res.length; i++) {
            res[i] = res[i].product(element.getValue());
        }
        return new ArithmeticMultiElement<>(res);
    }

    @Override
    public void scale(ArithmeticElement<T> element) throws DomainException {
        for (int i = 0; i < value.length; i++) {
            value[i] = value[i].product(element.getValue());
        }
    }

    @Override
    public int getLength() {
        return value.length;
    }

    @Override
    public ModuleElement<?, ArithmeticElement<T>> getComponent(int i) {
        return getRingElement(i);
    }

    @Override
    public ArithmeticMultiElement<T> sum(ArithmeticMultiElement<T> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        T[] res = Arrays.copyOf(value, value.length); //TODO is there a better way?
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].sum(element.value[i]);
        }
        return new ArithmeticMultiElement<>(res);
    }

    @Override
    public void add(ArithmeticMultiElement<T> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value[i] = value[i].sum(element.value[i]);
        }
    }

    @Override
    public ArithmeticMultiElement<T> difference(ArithmeticMultiElement<T> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        T[] res = Arrays.copyOf(value, value.length); //TODO is there a better way?
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].difference(element.value[i]);
        }
        return new ArithmeticMultiElement<>(res);
    }

    @Override
    public void subtract(ArithmeticMultiElement<T> element) throws DomainException {
        if (getLength() != element.getLength()) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getLength(); i++) {
            value[i] = value[i].difference(element.value[i]);
        }
    }

    @Override
    public ArithmeticMultiElement<T> negated() {
        T[] res = Arrays.copyOf(value, value.length); //TODO is there a better way?
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].neg();
        }
        return new ArithmeticMultiElement<>(res);
    }

    @Override
    public void negate() {
        T[] res = Arrays.copyOf(value, value.length); //TODO is there a better way?
        for (int i = 0; i < getLength(); i++) {
            value[i] = value[i].neg();
        }
    }

    @Override
    public double[] fold(ModuleElement<?, ?>[] elements) {
        return new double[0]; //TODO
    }

    @Override
    public Module<ArithmeticMultiElement<T>, ArithmeticElement<T>> getModule() {
        return null; //TODO
    }

    @Override
    public String stringRep(boolean... parens) {
        return null; //TODO
    }

    @Override
    public int hashCode() {
        int val = 11;
        for (int i = 0; i < getLength(); i++) {
            val = value[i].hashCode()*17+val;
        }
        return val;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticMultiElement)) {
            return false;
        }
        ArithmeticMultiElement<?> otherElement = (ArithmeticMultiElement<?>) object;
        if (getLength() == otherElement.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                if (!Objects.equals(value[i], otherElement.value[i])) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

}
