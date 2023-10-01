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

import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.arith.string.ZnString;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringElement;

import java.util.HashMap;
import java.util.Set;

/**
 * Elements of the ring of strings with integer mod <i>n</i> factors.
 * @see ZnStringRing
 * 
 * @author Gérard Milmeister
 */
public final class ZnStringElement extends StringElement<ZnStringElement> implements ZnStringFreeElement<ZnStringElement> {

    /**
     * Constructs a ZnStringElement from a <code>string</code> mod <code>modulus</code>.
     * The result is a ZnStringElement of the form 1*value.
     */
    public ZnStringElement(String string, int modulus) {
        this.value = new ZnString(string, modulus);
        this.modulus = modulus;
    }

    
    /**
     * Constructs a ZnStringElement from a ZnString <code>value</code>.
     */
    public ZnStringElement(ZnString value) {
        this.value = value;
        this.modulus = value.getModulus();
    }


    /**
     * Constructs a ZnStringElement from the array of objects <code>objs</code>.
     * @param objs an array of objects where strings alternate with
     *             integers, the array should therefore be of even
     *             length
     */
    public ZnStringElement(int modulus, Object ... objs) {
        int len = objs.length/2;
        String[] words = new String[len];
        int[] factors = new int[len];
        for (int i = 0; i < len*2; i += 2) {
            if (objs[i] instanceof String && objs[i+1] instanceof Integer) {
                words[i/2] = (String)objs[i];
                factors[i/2] = (Integer)objs[i+1];
            }
            else {
                words[i/2] = "";
                factors[i/2] = 0;
            }
        }
        this.value = new ZnString(words, factors, modulus);
    }
    

    public boolean isOne() {
        return value.equals(ZnString.getOne(getModulus()));
    }
       

    public boolean isZero() {
        return value.equals(ZnString.getZero(getModulus()));
    }

    public ZnStringElement sum(ZnStringElement element) {
        return new ZnStringElement(value.sum(element.getValue()));
    }

    public void add(ZnStringElement element) {
        value.add(element.getValue());
    }

    public ZnStringElement difference(ZnStringElement element) {
        return new ZnStringElement(value.difference(element.getValue()));
    }

    public void subtract(ZnStringElement element) {
        value.subtract(element.getValue());
    }


    public ZnStringElement negated() {
        return new ZnStringElement(value.negated());
    }

    
    public void negate() {
        value.negate();
    }

    
    public ZnStringElement scaled(ZnStringElement element)
            throws DomainException {
        return product(element);
    }
    

    public void scale(ZnStringElement element)
            throws DomainException {
        multiply(element);
    }
    
    public ZnStringElement product(ZnStringElement element) {
        return new ZnStringElement(getValue().product(element.getValue()));
    }
    
    public void multiply(ZnStringElement element) {
        value.multiply(element.getValue());
    }

    
    public ZnStringElement inverse() {
        throw new InverseException("Inverse of "+this+" does not exist");
    }

    
    public void invert() {
        throw new InverseException("Inverse of "+this+" does not exist");
    }
    

    public ZnStringElement quotient(ZnStringElement element)
            throws DomainException, DivisionException {
        if (element instanceof ZnStringElement) {
            // TODO: implement division where possible
            throw new DivisionException(this, element);
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }


    public void divide(ZnStringElement element)
            throws DomainException, DivisionException {
        if (element instanceof ZnStringElement) {
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


    public Module getModule() {
        if (module == null) {
            module = ZnStringRing.make(modulus);
        }
        return module;
    }

    
    public ZnString getValue() {
        return value;
    }
    

    public RingString getRingString() {
        return value;
    }

    
    public FreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return ZnStringProperFreeElement.make(new ZnString[0], modulus);
        }
        else {
            ZnString[] values = new ZnString[n];
            values[0] = value;
            for (int i = 1; i < n; i++) {
                values[i] = ZnString.getZero(modulus);
            }
            return ZnStringProperFreeElement.make(values, modulus);
        }
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ZnStringElement) {
            return (value.equals(((ZnStringElement)object).getValue()) &&
                    	value.getModulus() == ((ZnStringElement)object).getModulus());
        }
        else {
            return false;
        }
    }


    public int compareTo(ModuleElement object) {
        if (object instanceof ZnStringElement) {
            ZnStringElement element = (ZnStringElement)object;
            return value.compareTo(element.getValue());
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public ZnStringElement deepCopy() {
        return new ZnStringElement((ZnString)value.deepCopy());
    }
    
    public int getModulus() {
        return modulus;
    }

    
    public String toString() {
        return "ZnStringElement("+getModulus()+")["+getValue()+"]";
    }

    
    public String stringRep(boolean ... parens) {
    	return value.stringRep();
    }
    
    
    public HashMap<String,RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<String,RingElement>();
        Set<String> strings = value.getStrings();
        for (String s : strings) {
            map.put(s, new ZElement(((Integer)value.getFactorForString(s))));
        }
        return map;
    }
    

    public ModuleElement cast(Module module) {
        return module.cast(this);
    }

    public String getElementTypeName() {
        return "ZnStringElement";
    }
    
    
    public int hashCode() {
        return value.hashCode();
    }
     
   
    private ZnString value;
    private int      modulus;
    private Module   module = null;
}
