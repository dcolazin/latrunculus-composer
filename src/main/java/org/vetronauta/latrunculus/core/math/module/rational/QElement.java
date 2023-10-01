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
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Elements in the field of rationals.
 * @see QRing
 * 
 * @author Gérard Milmeister
 */
public final class QElement extends RingElement<QElement> implements QFreeElement<QElement> {

    /**
     * Constructs a QElement with rational number <code>value</code>.
     */
    public QElement(Rational value) {
        this.value = value;
    }


    /**
     * Constructs a QElement with rational number <code>num/denom</code>.
     */
    public QElement(int num, int denom) {
        value = new Rational(num, denom);
    }

    
    /**
     * Constructs a QElement with integer <code>i</code>.
     */
    public QElement(int i) {
        value = new Rational(i);
    }

    
    public boolean isOne() {
        return value.isOne();
    }
    
    
    public boolean isZero() {
        return value.isZero();
    }

    public QElement sum(QElement element) {
        return new QElement(value.sum(element.getValue()));
    }

    public void add(QElement element) {
        value.add(element.getValue());        
    }

    public QElement difference(QElement element) {
        return new QElement(value.difference(element.getValue()));
    }
    
    public void subtract(QElement element) {
        value.subtract(element.getValue());        
    }

    
    public QElement negated() {
        return new QElement(value.neg());
    }

    
    public void negate() {
        value.negate();
    }

    
    public QElement scaled(QElement element)
            throws DomainException {
        return product(element);
    }

    
    public void scale(QElement element)
            throws DomainException {
        multiply(element);
    }
    
    public QElement product(QElement element) {
        return new QElement(value.product(element.getValue())); 
    }


    public void multiply(QElement element) {
        value.multiply(element.getValue());
    }


    public boolean isInvertible() {
        return !isZero();
    }
    
    
    public QElement inverse() {
        return new QElement(value.inverse());
    }


    public void invert() {
        value.invert();
    }

    public QElement quotient(QElement element)
            throws DivisionException {
        Rational r = element.getValue();
        if (!r.isZero()) {
            return new QElement(getValue().quotient(r));
        }
        else {
            throw new DivisionException(this, element);
        }
    }
    
    public void divide(QElement element)
            throws DivisionException {
        Rational r = element.getValue();
        if (!r.isZero()) {
            value.divide(r);
        }
        else {
            throw new DivisionException(this, element);
        }
    }

    
    public boolean divides(RingElement element) {
        return element instanceof QElement && !getValue().isZero();
    }

    
    public QElement power(int n) {
        if (n == 0) {
            return QRing.ring.getOne();
        }
        
        Rational factor;
        
        if (n < 0) {
            if (isInvertible()) {
                factor = value.inverse();
                n = -n;
            }
            else {
                throw new InverseException("Inverse of "+this+" does not exist.");
            }
        }
        else {
            factor = value;
        }
        
        // Finding leading bit in the exponent n
        int bpos = 31; // bits per int
        while ((n & (1 << bpos)) == 0) {
            bpos--;
        }

        Rational result = Rational.getOne();
        while (bpos >= 0) {
            result = result.product(result);
            if ((n & (1 << bpos)) != 0) {
                result = result.product(factor);
            }
            bpos--;
        }

        return new QElement(result);
    }
    
    
    public QRing getModule() {
        return QRing.ring;
    }

    
    public QElement getComponent(int i) {
        return this;
    }
    

    public QElement getRingElement(int i) {
        return this;
    }
    

    public Rational getValue() {
        return value;
    }

    
    public QFreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return QProperFreeElement.make(new Rational[0]);
        }
        else {
            Rational[] values = new Rational[n];
            values[0] = new Rational(value);
            for (int i = 1; i < n; i++) {
                values[i] = Rational.getZero();
            }
            return QProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof QElement) {
            return value.equals(((QElement)object).getValue());
        }
        else {
            return false;
        }
    }

    
    public int compareTo(ModuleElement object) {
        if (object instanceof QElement) {
            return value.compareTo(((QElement)object).value);
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public QElement deepCopy() {
        return new QElement(new Rational(value));
    }
    
    public String stringRep(boolean ... parens) {
        if (parens.length > 0 && value.compareTo(Rational.getZero()) < 0) {
            return TextUtils.parenthesize(value.toString());
        }
        else {
            return value.toString();
        }
    }

    
    public String toString() {
        return "QElement["+value+"]";
    }

    
    public double[] fold(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            QElement r = (QElement)elements[i];
            res[i] = r.getValue().doubleValue();
        }
        return res;
    }

    
    public ModuleElement cast(Module module) {
        return module.cast(this);
    }

    public String getElementTypeName() {
        return "QElement";
    }
    
    
    public int hashCode() {
        return value.hashCode();
    }

    
    private Rational value;
}
