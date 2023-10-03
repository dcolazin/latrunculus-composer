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

package org.vetronauta.latrunculus.core.math.module.modular;

import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.ZeroDivisorException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Elements in the ring of integers mod <i>n</i>.
 * @see ZnRing
 * 
 * @author Gérard Milmeister
 */
public final class ZnElement extends RingElement<ZnElement> implements ZnFreeElement<ZnElement> {

    /**
     * Constructs a ZnElement <code>value</code> mod <code>modulus</code>.
     */
    public ZnElement(int value, int modulus) {
        this.value = NumberTheory.mod(value, modulus);
        this.modulus = modulus;
    }
    
    
    public boolean isOne() {
        return value == 1;
    }
    
    
    public boolean isZero() {
        return value == 0;
    }
    
    public ZnElement sum(ZnElement element)
            throws DomainException {
        if (getModulus() == element.getModulus()) {
            return new ZnElement(value+element.getValue(), modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(ZnElement element)
            throws DomainException {
        if (getModulus() == element.getModulus()) {
            value = NumberTheory.mod(value+element.getValue(), modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());            
        }
    }
    
    public ZnElement difference(ZnElement element)
            throws DomainException {
        if (getModulus() == element.getModulus()) {
            return new ZnElement(value-element.getValue(), modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());            
        }
    }
    
    public void subtract(ZnElement element)
            throws DomainException {
        if (getModulus() == element.getModulus()) {
            value = NumberTheory.mod(value-element.getValue(), modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());            
        }
    }
    
    
    public ZnElement negated() {
        return new ZnElement(-value, modulus);
    }

    
    public void negate() {
        value = NumberTheory.mod(-value, modulus);
    }

    
    public ZnElement scaled(ZnElement element)
            throws DomainException {
        return product(element);
    }
    

    public void scale(ZnElement element)
            throws DomainException {
        multiply(element);
    }
    
    public ZnElement product(ZnElement element)
            throws DomainException {
        if (getModulus() == element.getModulus()) {
            return new ZnElement(value*element.getValue(), modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void multiply(ZnElement element)
            throws DomainException {
        if (getModulus() == element.getModulus()) {
            value = NumberTheory.mod(value*element.getValue(), modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public boolean isInvertible() {
        try {
            NumberTheory.inverseMod(value, modulus);
            return true;
        }
        catch (ZeroDivisorException e) {
            return false;
        }
    }
    
    public ZnElement inverse() {
        int v = NumberTheory.inverseMod(value, modulus);
        return new ZnElement(v, modulus);
    }

    
    public void invert() {
        value = NumberTheory.inverseMod(value, modulus);
    }

    public ZnElement quotient(ZnElement element)
            throws DivisionException {
        try {
            int i = NumberTheory.divideMod(getValue(), element.getValue(), getModulus());
            return new ZnElement(i, getModulus());
        }
        catch (ZeroDivisorException e) {
            throw new DivisionException(this, element);
        }
    }

    public void divide(ZnElement element)
            throws DivisionException {
        try {
            value = NumberTheory.divideMod(getValue(), element.getValue(), getModulus());
        }
        catch (ZeroDivisorException e) {
            throw new DivisionException(this, element);
        }
    }

    
    public boolean divides(RingElement element) {
        return element instanceof ZnElement &&
               NumberTheory.gcd(getValue(), getModulus()) == 1;
    }

    
    public ZnElement power(int n) {
        return new ZnElement(NumberTheory.powerMod(value, modulus, n), modulus);
    }
    
    
    public Module getModule() {
        if (module == null) {
            module = ZnRing.make(modulus);
        }
        return module;
    }

    
    public ZnElement getComponent(int i) {
        return this;
    }
    
    
    public ZnElement getRingElement(int i) {
        return this;
    }
    
    
    public int getValue() {
        return value;
    }

    
    public int getModulus() {
        return modulus;
    }

    
    public ZnFreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return ZnProperFreeElement.make(new int[0], modulus);
        }
        else {
            int[] values = new int[n];
            values[0] = value;
            for (int i = 1; i < n; i++) {
                values[i] = 0;
            }
            return ZnProperFreeElement.make(values, modulus);
        }
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ZnElement) {
            ZnElement element = (ZnElement)object;
            return value == element.value && modulus == element.modulus;
        }
        else {
            return false;
        }
    }

    
    public int compareTo(ModuleElement object) {
        if (object instanceof ZnElement) {
            ZnElement e = (ZnElement)object;
            return value-e.value;
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public ZnElement deepCopy() {
        return new ZnElement(value, modulus);
    }
    
    public String stringRep(boolean ... parens) {
    	return Integer.toString(value);
    }

    
    public String toString() {
        return "ZnElement("+getModulus()+")["+getValue()+"]";
    }

    
    public double[] fold(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ZnElement e = (ZnElement)elements[i];
            res[i] = e.getValue();
        }
        return res;
    }

    public String getElementTypeName() {
        return "ZnElement";
    }
    
    
    public int hashCode() {
        return value;
    }

    
    private int    value;
    private int    modulus;
    private ZnRing module = null;
}
