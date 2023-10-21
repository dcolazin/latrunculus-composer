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

import lombok.NonNull;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vetronauta
 */
public abstract class ArithmeticStringMultiElement<T extends ArithmeticStringElement<T,N>, N extends ArithmeticNumber<N>>
        extends ProperFreeElement<ArithmeticStringMultiElement<T,N>,T> {

    private final List<RingString<N>> value;

    protected ArithmeticStringMultiElement(List<RingString<N>> value) {
        this.value = value;
    }

    protected abstract ArithmeticStringMultiElement<T,N> valueOf(@NonNull List<RingString<N>> value);

    @Override
    public boolean isZero() {
        RingString<N> zero = RingString.getZero();
        for (int i = 0; i < getLength(); i++) {
            if (!value.get(i).equals(zero)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ArithmeticStringMultiElement<T,N> sum(ArithmeticStringMultiElement<T,N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<N>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).sum(element.value.get(i)));
            }
            return valueOf(res);
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public void add(ArithmeticStringMultiElement<T,N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).add(element.value.get(i));
            }
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public ArithmeticStringMultiElement<T,N> difference(ArithmeticStringMultiElement<T,N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<N>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).difference(element.value.get(i)));
            }
            return valueOf(res);
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public void subtract(ArithmeticStringMultiElement<T,N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).subtract(element.value.get(i));
            }
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public ArithmeticStringMultiElement<T,N> productCW(ArithmeticStringMultiElement<T,N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<N>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).product(element.value.get(i)));
            }
            return valueOf(res);
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public void multiplyCW(ArithmeticStringMultiElement<T,N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).multiply(element.value.get(i));
            }
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public ArithmeticStringMultiElement<T,N> negated() {
        List<RingString<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).negated());
        }
        return valueOf(res);
    }

    @Override
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value.get(i).negate();
        }
    }

    @Override
    public ArithmeticStringMultiElement<T,N> scaled(T element) {
        RingString<N> val = element.getValue();
        List<RingString<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(val));
        }
        return valueOf(res);
    }

    @Override
    public void scale(T element) {
        RingString<N> val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value.get(i).multiply(val);
        }
    }

    @Override
    public int getLength() {
        return value.size();
    }

    public List<RingString<N>> getValue() {
        return value;
    }


    public RingString<N> getValue(int i) {
        return value.get(i);
    }

    @Override
    public ArithmeticStringMultiElement<T,N> deepCopy() {
        List<RingString<N>> v = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            v.add(value.get(i).deepCopy());
        }
        return valueOf(v);
    }

}
