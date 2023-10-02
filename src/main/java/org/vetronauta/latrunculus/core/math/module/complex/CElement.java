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

package org.vetronauta.latrunculus.core.math.module.complex;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Elements in the field of complex numbers.
 * @see CRing
 *
 * @author Gérard Milmeister
 */
public final class CElement extends RingElement<CElement> implements CFreeElement<CElement> {

    private final Complex value;

    /**
     * Constructs a CElement with complex number <code>value</code>.
     */
    public CElement(Complex value) {
        this.value = value;
    }


    /**
     * Constructs a CElement with complex (real) number <code>value</code>.
     */
    public CElement(double value) {
        this.value = new Complex(value);
    }

    
    /**
     * Constructs a CElement with complex number <code>x</code> + i <code>y</code>.
     */
    public CElement(double x, double y) {
        this.value = new Complex(x, y);
    }

    @Override
    public boolean isOne() {
        return value.isOne();
    }

    @Override
    public boolean isZero() {
        return value.isZero();
    }

    @Override
    public CElement sum(CElement element) {
        return new CElement(value.sum(element.getValue()));
    }
    
    @Override
    public void add(CElement element) {
        value.add(element.getValue());
    }

    @Override
    public CElement difference(CElement element) {
        return new CElement(value.difference(element.getValue()));
    }
    
    @Override
    public void subtract(CElement element) {
        value.subtract(element.getValue());        
    }
    
    @Override
    public CElement negated() {
        return new CElement(value.neg());
    }

    @Override
    public void negate() {
        value.negate();
    }
    
    @Override
    public CElement conjugated() {
        return new CElement(value.conjugated());
    }

    @Override
    public void conjugate() {
        value.conjugate();
    }

    @Override
    public CElement scaled(CElement element) {
        return product(element);
    }

    @Override
    public void scale(CElement element) {
        multiply(element);
    }

    @Override
    public CElement product(CElement element) {
        return new CElement(value.product(element.getValue()));
    }

    @Override
    public void multiply(CElement element) {
        value.multiply(element.getValue());
    }

    @Override
    public boolean isInvertible() {
        return !isZero();
    }
    
    @Override
    public CElement inverse() {
    	return new CElement(value.inverse());
    }
    
    @Override
    public void invert() {
        value.invert();
    }
    
    @Override
    public CElement quotient(CElement element) throws DivisionException {
        Complex c = element.getValue();
        if (!c.isZero()) {
            return new CElement(getValue().quotient(c));
        }
        else {
            throw new DivisionException(this, element);
        }
    }

    @Override
    public void divide(CElement element) throws DivisionException {
        Complex c = element.getValue();
        if (!c.isZero()) {
            value.divide(c);
        }
        else {
            throw new DivisionException(this, element);
        }
    }

    @Override
    public boolean divides(RingElement<?> element) {
        return element instanceof CElement && !getValue().isZero();
    }

    @Override
    public CElement power(int n) {
        if (n == 0) {
            return CRing.ring.getOne();
        }
        
        Complex factor;
        
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

        Complex result = Complex.getOne();
        while (bpos >= 0) {
            result = result.product(result);
            if ((n & (1 << bpos)) != 0) {
                result = result.product(factor);
            }
            bpos--;
        }

        return new CElement(result);
    }
    
    @Override
    public CRing getModule() {
        return CRing.ring;
    }


    @Override
    public CElement getComponent(int i) {
        return this;
    }


    @Override
    public CElement getRingElement(int i) {
        return this;
    }
    

    /**
     * Returns the complex value contained in this element.
     */
    public Complex getValue() {
        return value;
    }


    public CFreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return CProperFreeElement.make(new Complex[0]);
        }
        else {
            Complex[] values = new Complex[n];
            values[0] = new Complex(value);
            for (int i = 1; i < n; i++) {
                values[i] = Complex.getZero();
            }
            return CProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof CElement) {
            return value.equals(((CElement)object).value);
        }
        else {
            return false;
        }
    }


    @Override
    public int compareTo(ModuleElement object) {
        if (object instanceof CElement) {
            return value.compareTo(((CElement)object).value);
        }
        else {
            return super.compareTo(object);
        }
    }

    public String stringRep(boolean ... parens) {
        if (parens.length > 0) {
            return TextUtils.parenthesize(value.toString());
        }
        else {
            return value.toString();
        }
    }


    public String toString() {
        return "CElement["+value+"]";
    }


    public double[] fold(ModuleElement[] elements) {
        double[][] res = new double[elements.length][2];
        for (int i = 0; i < elements.length; i++) {
            CElement c = (CElement)elements[i];           
            res[i][0] = c.getValue().getReal();
            res[i][1] = c.getValue().getImag();
        }
        return Folding.fold(res);
    }


    public ModuleElement cast(Module module) {
        return module.cast(this);
    }

    public String getElementTypeName() {
        return "CElement";
    }
    
    
    public int hashCode() {
        return value.hashCode();
    }


    @Override
    public CElement deepCopy() {
        return new CElement(value.deepCopy());
    }
}
