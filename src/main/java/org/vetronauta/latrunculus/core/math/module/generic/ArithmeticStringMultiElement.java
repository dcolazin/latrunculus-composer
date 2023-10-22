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
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vetronauta
 */
public class ArithmeticStringMultiElement<T extends ArithmeticStringElement<T,N>, N extends ArithmeticNumber<N>>
        extends ProperFreeElement<ArithmeticStringMultiElement<T,N>,T> {

    private final List<RingString<N>> value;
    private final ArithmeticRing<N> ring;
    private Module module;

    protected ArithmeticStringMultiElement(ArithmeticRing<N> ring, List<RingString<N>> value) {
        this.ring = ring;
        this.value = value;
    }

    public static <Y extends ArithmeticStringElement<Y,X>, X extends ArithmeticNumber<X>> FreeElement<?, Y> make(List<RingString<X>> v) {
        assert(v != null);
        return null; //TODO!!!
        /*
        if (v.isEmpty()) {
            return new ArithmeticStringMultiElement<>(new ArrayList<>());
        }
        else if (v.size() == 1) {
            return new ArithmeticStringElement<>(v.get(0));
        }
        return new ArithmeticStringMultiElement<>(StringRingRepository.getRing(v.get(0)), v);
         */
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
    public ArithmeticStringMultiElement<T,N> sum(ArithmeticStringMultiElement<T,N> element) throws DomainException {
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
            return new ArithmeticStringMultiElement<>(ring, res);
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
            return new ArithmeticStringMultiElement<>(ring, res);
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
    public FreeElement<?, T> resize(int n) {
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
        return make(values);
    }

    @Override
    public ArithmeticStringMultiElement<T,N> negated() {
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
    public Module getModule() { //TODO this is wrong, the meaning of getModule is different from usual getModules types
        if (module == null) {
            module = ArithmeticMultiModule.make(ring, getLength());
        }
        return module;
    }

    @Override
    public String stringRep(boolean... parens) {
        if (getLength() == 0) {
            return "Null";
        }
        StringBuilder res = new StringBuilder(30);
        res.append(value.get(0).stringRep());
        for (int i = 1; i < getLength(); i++) {
            res.append(',');
            res.append(value.get(i).stringRep());
        }
        return parens.length > 0 ? TextUtils.parenthesize(res.toString()) : res.toString();
    }

    @Override
    public ArithmeticStringMultiElement<T,N> scaled(T element) {
        RingString<N> val = element.getValue();
        List<RingString<N>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).product(val));
        }
        return new ArithmeticStringMultiElement<>(ring, res);
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

    @Override
    public ArithmeticStringElement<T,N> getComponent(int i) {
        assert(i < getLength());
        return new ArithmeticStringElement<>(value.get(i));
    }


    @Override
    public ArithmeticStringElement<T,N> getRingElement(int i) {
        assert(i < getLength());
        return new ArithmeticStringElement<>(value.get(i));
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
        return new ArithmeticStringMultiElement<>(ring, v);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArithmeticStringMultiElement)) {
            return false;
        }
        ArithmeticStringMultiElement<?,?> e = (ArithmeticStringMultiElement<?,?>) object;
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

    public double[] fold(ModuleElement[] elements) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int compareTo(ModuleElement object) {
        if (!(object instanceof ArithmeticStringMultiElement) || ! getModule().equals(object.getModule())) {
            return super.compareTo(object);
        }
        ArithmeticStringMultiElement<?,N> element = (ArithmeticStringMultiElement<?,N>)object;
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
