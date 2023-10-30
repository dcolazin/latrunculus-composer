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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vetronauta
 */
public class ArithmeticStringMultiElement<N extends ArithmeticNumber<N>>
        extends ProperFreeElement<ArithmeticStringMultiElement<N>,ArithmeticStringElement<N>> {

    //TODO various consistency checks for modulus

    private final List<RingString<N>> value;
    private final ArithmeticStringRing<N> ring;
    private ArithmeticStringMultiModule<N> module;

    protected ArithmeticStringMultiElement(ArithmeticStringRing<N> ring, List<RingString<N>> value) {
        this.ring = ring;
        this.value = value;
    }

    public static <X extends ArithmeticNumber<X>> FreeElement<?, ArithmeticStringElement<X>> make(ArithmeticStringRing<X> ring, List<RingString<X>> v) {
        assert(v != null && ring != null);
        if (v.isEmpty()) {
            return new ArithmeticStringMultiElement<>(ring, new ArrayList<>());
        }
        else if (v.size() == 1) {
            return new ArithmeticStringElement<>(ring, v.get(0));
        }
        return new ArithmeticStringMultiElement<>(ring, v);

    }

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
    public ArithmeticStringMultiElement<N> sum(ArithmeticStringMultiElement<N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<N>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).sum(element.value.get(i)));
            }
            return new ArithmeticStringMultiElement<>(ring, res);
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public void add(ArithmeticStringMultiElement<N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).add(element.value.get(i));
            }
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public ArithmeticStringMultiElement<N> difference(ArithmeticStringMultiElement<N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<N>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).difference(element.value.get(i)));
            }
            return new ArithmeticStringMultiElement<>(ring, res);
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public void subtract(ArithmeticStringMultiElement<N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).subtract(element.value.get(i));
            }
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public ArithmeticStringMultiElement<N> productCW(ArithmeticStringMultiElement<N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            List<RingString<N>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).product(element.value.get(i)));
            }
            return new ArithmeticStringMultiElement<>(ring, res);
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public void multiplyCW(ArithmeticStringMultiElement<N> element) throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).multiply(element.value.get(i));
            }
        }
        throw new DomainException(this.getModule(), element.getModule());
    }

    @Override
    public FreeElement<?, ArithmeticStringElement<N>> resize(int n) {
        if (n == getLength()) {
            return this;
        }
        int minlen = Math.min(n, getLength());
        List<RingString<N>> values = new ArrayList<>(n);
        for (int i = 0; i < minlen; i++) {
            values.add(getValue(i));
        }
        for (int i = minlen; i < n; i++) {
            values.add(RingString.getZero());
        }
        return make(ring, values);
    }

    @Override
    public ArithmeticStringMultiElement<N> negated() {
        List<RingString<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).negated());
        }
        return new ArithmeticStringMultiElement<>(ring, res);
    }

    @Override
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value.get(i).negate();
        }
    }

    @Override
    public ArithmeticStringMultiModule<N> getModule() {
        if (module == null) {
            module = (ArithmeticStringMultiModule<N>) ArithmeticStringMultiModule.make(ring, getLength());
        }
        return module;
    }

    @Override
    public ArithmeticStringMultiElement<N> scaled(ArithmeticStringElement<N> element) {
        RingString<N> val = element.getValue();
        List<RingString<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(val));
        }
        return new ArithmeticStringMultiElement<>(ring, res);
    }

    @Override
    public void scale(ArithmeticStringElement<N> element) {
        RingString<N> val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value.get(i).multiply(val);
        }
    }

    @Override
    public int getLength() {
        return value.size();
    }

    @Override
    public ArithmeticStringElement<N> getComponent(int i) {
        assert(i < getLength());
        return new ArithmeticStringElement<>(ring, value.get(i));
    }


    @Override
    public ArithmeticStringElement<N> getRingElement(int i) {
        assert(i < getLength());
        return new ArithmeticStringElement<>(ring, value.get(i));
    }

    public List<RingString<N>> getValue() {
        return value;
    }


    public RingString<N> getValue(int i) {
        return value.get(i);
    }

    @Override
    public ArithmeticStringMultiElement<N> deepCopy() {
        List<RingString<N>> v = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            v.add(value.get(i).deepCopy());
        }
        return new ArithmeticStringMultiElement<>(ring, v);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticStringMultiElement)) {
            return false;
        }
        ArithmeticStringMultiElement<?> e = (ArithmeticStringMultiElement<?>) object;
        if (getLength() != e.getLength()) {
            return false;
        }
        for (int i = 0; i < getLength(); i++) {
            if (!value.get(i).equals(e.value.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= value.get(i).hashCode();
        }
        return val;
    }

    public String getElementTypeName() {
        return String.format("ArithmeticStringMultiElement<%s>", getModule().getRing().toVisualString());
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("ArithmeticStringMultiElement<");
        buf.append(getModule().getRing().toVisualString());
        buf.append(">[");
        buf.append(getLength());
        buf.append("][");
        if (getLength() > 0) {
            buf.append(value.get(0));
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(value.get(i));
            }
        }
        buf.append("]");
        return buf.toString();
    }

    @Override
    public int compareTo(ModuleElement object) {
        if (!(object instanceof ArithmeticStringMultiElement) || ! getModule().equals(object.getModule())) {
            return super.compareTo(object);
        }
        ArithmeticStringMultiElement<N> element = (ArithmeticStringMultiElement<N>)object;
        int l = getLength()-element.getLength();
        if (l != 0) {
            return l;
        }
        for (int i = 0; i < getLength(); i++) {
            int d = value.get(i).compareTo(element.value.get(i));
            if (d != 0) {
                return d;
            }
        }
        return 0;
    }

}
