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

package org.vetronauta.latrunculus.core.math.module.real;

import org.vetronauta.latrunculus.core.math.arith.string.RString;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;

import java.util.HashMap;
import java.util.Set;

/**
 * Elements in the ring of strings with real factors.
 * @see RStringRing
 * 
 * @author Gérard Milmeister
 */
public final class RStringElement extends StringElement implements RStringFreeElement {

    /**
     * Constructs an RStringElement from an RString <code>value</code>.
     */
    public RStringElement(RString value) {
        this.value = value;
    }

    
    /**
     * Constructs an RStringElement from a simple string <code>value</code>.
     * The result is an RStringElement of the form 1.0*value.
     */
    public RStringElement(String value) {
        this.value = new RString(value);
    }

    
    /**
     * Constructs an RStringElement from the array of objects <code>objs</code>.
     * @param objs an array of objects where strings alternate with
     *             doubles, the array should therefore be of even
     *             length
     */
    public RStringElement(Object ... objs) {
        int len = objs.length/2;
        String[] words = new String[len];
        double[] factors = new double[len];
        for (int i = 0; i < len*2; i += 2) {
            if (objs[i] instanceof String && objs[i+1] instanceof Double) {
                words[i/2] = (String)objs[i];
                factors[i/2] = (Double)objs[i+1];
            }
            else {
                words[i/2] = "";
                factors[i/2] = 0;
            }
        }
        this.value = new RString(words, factors);
    }

    
    public boolean isOne() {
        return value.equals(RString.getOne());
    }
       

    public boolean isZero() {
        return value.equals(RString.getZero());
    }
       

    public ModuleElement sum(ModuleElement element)
            throws DomainException {
        if (element instanceof RStringElement) {
            return sum((RStringElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }


    public RStringElement sum(RStringElement element) {
        return new RStringElement((RString)getValue().sum(element.getValue()));        
    }


    public void add(ModuleElement element)
            throws DomainException {
        if (element instanceof RStringElement) {
            add((RStringElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public void add(RStringElement element) {
        value.add(element.getValue());        
    }


    public ModuleElement difference(ModuleElement element)
            throws DomainException {
        if (element instanceof RStringElement) {
            return difference((RStringElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
        

    public RStringElement difference(RStringElement element) {
        return new RStringElement((RString)getValue().difference(element.getValue()));
    }
        

    public void subtract(ModuleElement element)
            throws DomainException {
        if (element instanceof RStringElement) {
            subtract((RStringElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }


    public void subtract(RStringElement element) {
        value.subtract(element.getValue());
    }


    public ModuleElement negated() {
        return new RStringElement((RString)getValue().negated());
    }

    
    public void negate() {
        value.negate();
    }


    public ModuleElement scaled(RingElement element)
            throws DomainException {
        return product(element);
    }
    

    public void scale(RingElement element)
            throws DomainException {
        multiply(element);
    }
    

    public RingElement product(RingElement element)
            throws DomainException {
        if (element instanceof RStringElement) {
            return product((RStringElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public RStringElement product(RStringElement element) {
        return new RStringElement(getValue().product(element.getValue()));
    }


    public void multiply(RingElement element)
            throws DomainException {
        if (element instanceof RStringElement) {
            multiply((RStringElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public void multiply(RStringElement element) {
        value.multiply(element.getValue());
    }

    
    public RingElement inverse() {
        throw new InverseException("Inverse of "+this+" does not exist.");
    }

    
    public void invert() {
        throw new InverseException("Inverse of "+this+" does not exist.");
    }

    
    public RingElement quotient(RingElement element)
            throws DomainException, DivisionException {
        if (element instanceof RStringElement) {
            // TODO: implement division where possible
            throw new DivisionException(this, element);
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }


    public void divide(RingElement element)
            throws DomainException, DivisionException {
        if (element instanceof RStringElement) {
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
        return RStringRing.ring;
    }


    public RString getValue() {
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
            return RStringProperFreeElement.make(new RString[0]);
        }
        else {
            RString[] values = new RString[n];
            values[0] = new RString(value);
            for (int i = 1; i < n; i++) {
                values[i] = RString.getZero();
            }
            return RStringProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof RStringElement) {
            return getValue().equals(((RStringElement)object).getValue());
        }
        else {
            return false;
        }
    }


    public int compareTo(ModuleElement object) {
        if (object instanceof RStringElement) {
            return getValue().compareTo(((RStringElement)object).getValue());
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public RingElement deepCopy() {
        return new RStringElement(getValue().deepCopy());
    }

    public String toString() {
        return "RStringElement["+value+"]";
    }

    
    public String stringRep(boolean ... parens) {
 		return getValue().stringRep();
    }

    
    public String getString() {
        return getValue().getString();
    }

    
    public HashMap<String,RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<String,RingElement>();
        Set<String> strings = value.getStrings();
        for (String s : strings) {
            map.put(s, new RElement(((Double)value.getFactorForString(s))));
        }
        return map;
    }
    

    public ModuleElement cast(Module module) {
        return module.cast(this);
    }

    public String getElementTypeName() {
        return "RStringElement";
    }
    
    
    public int hashCode() {
        return value.hashCode();
    }
    
    
    private RString value = null;
}
