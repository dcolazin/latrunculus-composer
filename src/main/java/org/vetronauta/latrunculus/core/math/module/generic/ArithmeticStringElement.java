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
import org.vetronauta.latrunculus.core.util.EntryList;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.repository.StringRingRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author vetronauta
 */
@Getter
public class ArithmeticStringElement<N extends ArithmeticNumber<N>> extends StringElement<ArithmeticStringElement<N>> {

    private final RingString<N> value;
    private final ArithmeticStringRing<N> ring;

    /**
     * Constructs an RStringElement from an RString <code>value</code>.
     */
    public ArithmeticStringElement(ArithmeticStringRing<N> ring, RingString<N> value) {
        this.value = value;
        this.ring = ring;
    }

    /**
     * Constructs an RStringElement from an RString <code>value</code>.
     */
    public ArithmeticStringElement(ArithmeticRing<N> factorRing, RingString<N> value) {
        this(StringRingRepository.getRing(factorRing), value);
    }

    protected ArithmeticStringElement(ArithmeticRing<N> factorRing, EntryList<String,N> entryList) {
        this(factorRing, new RingString<>(entryList.getKeys(), entryList.getValues()));
    }

    /**
     * Constructs an RStringElement from a simple string <code>value</code>.
     * The result is an RStringElement of the form 1.0*value.
     */
    public ArithmeticStringElement(ArithmeticRing<N> factorRing, String value) {
        this(factorRing, new RingString<>(value));
    }

    public boolean isOne() {
        return value.equals(RingString.getOne());
    }

    public boolean isZero() {
        return value.equals(RingString.getZero());
    }

    public ArithmeticStringElement<N> sum(ArithmeticStringElement<N> element) {
        return new ArithmeticStringElement<>(ring, getValue().sum(element.getValue()));
    }

    public void add(ArithmeticStringElement<N> element) {
        value.add(element.getValue());
    }

    public ArithmeticStringElement<N> difference(ArithmeticStringElement<N> element) {
        return new ArithmeticStringElement<>(ring, getValue().difference(element.getValue()));
    }

    public void subtract(ArithmeticStringElement<N> element) {
        value.subtract(element.getValue());
    }

    public ArithmeticStringElement<N> negated() {
        return new ArithmeticStringElement<>(ring, getValue().negated());
    }

    public void negate() {
        value.negate();
    }

    public ArithmeticStringElement<N> scaled(ArithmeticStringElement<N> element) throws DomainException {
        return product(element);
    }

    public void scale(ArithmeticStringElement<N> element) throws DomainException {
        multiply(element);
    }

    public ArithmeticStringElement<N> product(ArithmeticStringElement<N> element) {
        return new ArithmeticStringElement<>(ring, getValue().product(element.getValue()));
    }

    public void multiply(ArithmeticStringElement<N> element) {
        value.multiply(element.getValue());
    }


    public ArithmeticStringElement<N> inverse() {
        throw new InverseException("Inverse of "+this+" does not exist.");
    }


    public void invert() {
        throw new InverseException("Inverse of "+this+" does not exist.");
    }


    public ArithmeticStringElement<N> quotient(ArithmeticStringElement<N> element) throws DomainException, DivisionException {
        if (this.getClass().isAssignableFrom(element.getClass())) {
            // TODO: implement division where possible
            throw new DivisionException(this, element);
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }

    public void divide(ArithmeticStringElement<N> element) throws DomainException, DivisionException {
        if (this.getClass().isAssignableFrom(element.getClass())) {
            // TODO: implement division where possible
            throw new DivisionException(this, element);
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }

    public boolean divides(RingElement element) {
        // TODO: implement division where possible
        return false;
    }

    public RingString<N> getRingString() {
        return getValue();
    }

    @Override
    public Map<String, RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<>();
        Set<String> strings = getValue().getStrings();
        for (String s : strings) {
            map.put(s, new ArithmeticElement<>(getValue().getFactorForString(s)));
        }
        return map;
    }

    public String getString() {
        return getValue().getString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ArithmeticStringElement) {
            return getValue().equals(((ArithmeticStringElement<?>)object).getValue());
        }
        else {
            return false;
        }
    }

    @Override
    public int compareTo(ModuleElement object) { //TODO probably not correct
        if (object instanceof ArithmeticStringElement) {
            return getValue().compareTo(((ArithmeticStringElement<N>)object).getValue());
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public ArithmeticStringElement<N> deepCopy() {
        return new ArithmeticStringElement<>(ring, getValue().deepCopy());
    }

    @Override
    public FreeElement<?, ArithmeticStringElement<N>> resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return ArithmeticStringMultiElement.make(ring, new ArrayList<>());
        }
        else {
            List<RingString<N>> values = new ArrayList<>(n);
            values.add(new RingString<>(getValue()));
            for (int i = 1; i < n; i++) {
                values.add(new RingString<>());
            }
            return ArithmeticStringMultiElement.make(ring, values);
        }
    }

    public String getElementTypeName() {
        return String.format("ArithmeticStringElement<%s>", getRing().toVisualString());
    }

    @Override
    public String toString() {
        return String.format("ArithmeticStringElement<%s>[%s]", getRing().toVisualString(), getValue());
    }


}
