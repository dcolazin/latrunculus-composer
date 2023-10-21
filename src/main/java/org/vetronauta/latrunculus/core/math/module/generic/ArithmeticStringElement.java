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
import org.vetronauta.latrunculus.core.EntryList;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.repository.StringRingRepository;

/**
 * @author vetronauta
 */
@Getter
public abstract class ArithmeticStringElement<T extends ArithmeticStringElement<T,N>, N extends ArithmeticNumber<N>> extends StringElement<T>
        implements FreeElement<T,T> {

    private final RingString<N> value;

    /**
     * Constructs an RStringElement from an RString <code>value</code>.
     */
    protected ArithmeticStringElement(RingString<N> value) {
        this.value = value;
    }

    protected ArithmeticStringElement(EntryList<String,N> entryList) {
        this(new RingString<>(entryList.getKeys(), entryList.getValues()));
    }

    /**
     * Constructs an RStringElement from a simple string <code>value</code>.
     * The result is an RStringElement of the form 1.0*value.
     */
    protected ArithmeticStringElement(String value) {
        this.value = new RingString<>(value);
    }

    protected abstract T valueOf(@NonNull RingString<N> value);

    public boolean isOne() {
        return value.equals(RingString.getOne());
    }

    public boolean isZero() {
        return value.equals(RingString.getZero());
    }

    public T sum(T element) {
        return valueOf(getValue().sum(element.getValue()));
    }

    public void add(T element) {
        value.add(element.getValue());
    }

    public T difference(T element) {
        return valueOf(getValue().difference(element.getValue()));
    }

    public void subtract(T element) {
        value.subtract(element.getValue());
    }

    public T negated() {
        return valueOf(getValue().negated());
    }

    public void negate() {
        value.negate();
    }

    public T scaled(T element) throws DomainException {
        return product(element);
    }

    public void scale(T element) throws DomainException {
        multiply(element);
    }

    public T product(T element) {
        return valueOf(getValue().product(element.getValue()));
    }

    public void multiply(T element) {
        value.multiply(element.getValue());
    }


    public T inverse() {
        throw new InverseException("Inverse of "+this+" does not exist.");
    }


    public void invert() {
        throw new InverseException("Inverse of "+this+" does not exist.");
    }


    public T quotient(T element) throws DomainException, DivisionException {
        if (this.getClass().isAssignableFrom(element.getClass())) {
            // TODO: implement division where possible
            throw new DivisionException(this, element);
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }

    public void divide(T element) throws DomainException, DivisionException {
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

    public StringRing<T> getRing() {
        return StringRingRepository.getRing(this);
    }

    public RingString<N> getRingString() {
        return getValue();
    }

    public String stringRep(boolean... parens) {
        return getValue().stringRep();
    }

    public String getString() {
        return getValue().getString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ArithmeticStringElement) {
            return getValue().equals(((ArithmeticStringElement<?,?>)object).getValue());
        }
        else {
            return false;
        }
    }

    public int compareTo(ModuleElement object) { //TODO probably not correct
        if (object instanceof ArithmeticStringElement) {
            return getValue().compareTo(((ArithmeticStringElement<?,N>)object).getValue());
        }
        else {
            return super.compareTo(object);
        }
    }

    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public T deepCopy() {
        return valueOf(getValue().deepCopy());
    }

}
