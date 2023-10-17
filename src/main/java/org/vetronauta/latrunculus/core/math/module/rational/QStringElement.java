/*
 * Copyright (C) 2001, 2005 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.vetronauta.latrunculus.core.math.module.rational;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringRing;

import java.util.HashMap;
import java.util.Set;

/**
 * Elements in the ring of strings with rational factors.
 * @see QStringRing
 * 
 * @author Gérard Milmeister
 */
public final class QStringElement extends StringElement<QStringElement> implements QStringFreeElement<QStringElement> {

    private final RingString<Rational> value;

    /**
     * Constructs a QStringElement from a QString <code>value</code>.
     */
    public QStringElement(RingString<Rational> value) {
        this.value = value;
    }


    /**
     * Constructs a QStringElement from a simple string <code>value</code>.
     * The result is a QStringElement of the form 1/1*value.
     */
    public QStringElement(String value) {
        this.value = new RingString<>(value);
    }

    
    /**
     * Constructs a QStringElement from the array of objects <code>objs</code>.
     * @param objs an array of objects where strings alternate with
     *             rationals, the array should therefore be of even
     *             length
     */
    public QStringElement(Object ... objs) {
        int len = objs.length/2;
        String[] words = new String[len];
        Rational[] factors = new Rational[len];
        for (int i = 0; i < len*2; i += 2) {
            if (objs[i] instanceof String && objs[i+1] instanceof Rational) {
                words[i/2] = (String)objs[i];
                factors[i/2] = (Rational)objs[i+1];
            }
            else {
                words[i/2] = "";
                factors[i/2] = Rational.getZero();
            }
        }
        this.value = new RingString<>(words, factors);
    }

    
    public boolean isOne() {
        return value.isOne();
    }
       

    public boolean isZero() {
        return value.isZero();
    }


    public QStringElement sum(QStringElement element) {
        return new QStringElement(getValue().sum(element.getValue()));
    }
    
    public void add(QStringElement element) {
        value.add(element.getValue());        
    }

    public QStringElement difference(QStringElement element) {
        return new QStringElement(getValue().difference(element.getValue()));
    }

    public void subtract(QStringElement element) {
        value.subtract(element.getValue());
    }


    public QStringElement negated() {
        return new QStringElement(getValue().negated());
    }

    
    public void negate() {
        value.negate();
    }


    public QStringElement scaled(QStringElement element)
            throws DomainException {
        return product(element);
    }
    

    public void scale(QStringElement element)
            throws DomainException {
        multiply(element);
    }

    
    public QStringElement product(QStringElement element) {
        return new QStringElement(getValue().product(element.getValue()));
    }

    
    public final void multiply(QStringElement element) {
        value.multiply(element.getValue());
    }

    
    public QStringElement inverse() {
        throw new InverseException("Inverse of "+this+" does not exist");
    }

    
    public void invert() {
        throw new InverseException("Inverse of "+this+" does not exist");
    }

    
    public QStringElement quotient(QStringElement element)
            throws DomainException, DivisionException {
        if (element instanceof QStringElement) {
            // TODO: implement division where possible
            throw new DivisionException(this, element);
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }


    public void divide(QStringElement element)
            throws DomainException, DivisionException {
        if (element instanceof QStringElement) {
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


    public RStringRing getRing() {
        return RStringRing.ring;
    }


    public RingString<Rational> getValue() {
        return value;
    }


    public RingString getRingString() {
        return getValue();
    }


    public FreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return QStringProperFreeElement.make(new RingString[0]);
        }
        else {
            RingString[] values = new RingString[n];
            values[0] = new RingString(value);
            for (int i = 1; i < n; i++) {
                values[i] = new RingString();
            }
            return QStringProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof QStringElement) {
            return getValue().equals(((QStringElement)object).getValue());
        }
        else {
            return false;
        }
    }


    public int compareTo(ModuleElement object) {
        if (object instanceof QStringElement) {
            return getValue().compareTo(((QStringElement)object).getValue());
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public QStringElement deepCopy() {
        return new QStringElement(getValue().deepCopy());
    }

    public String toString() {
        return "QStringElement["+value+"]";
    }

    
    public String stringRep(boolean ... parens) {
        if (parens.length > 0) {
            return TextUtils.parenthesize(getValue().stringRep());
        }
        else {
            return getValue().stringRep();
        }
    }

    
    public String getString() {
        return getValue().getString();
    }

    
    public HashMap<String,RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<>();
        Set<String> strings = value.getStrings();
        for (String s : strings) {
            map.put(s, new QElement(((Rational)value.getFactorForString(s))));
        }
        return map;
    }

    public String getElementTypeName() {
        return "QStringElement";
    }
    
    
    public int hashCode() {
        return value.hashCode();
    }

}
