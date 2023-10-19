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

package org.vetronauta.latrunculus.core.math.module.integer;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Elements of the ring of strings with integer factors.
 * @see ZStringRing
 * 
 * @author Gérard Milmeister
 */
public final class ZStringElement extends StringElement<ZStringElement> implements FreeElement<ZStringElement,ZStringElement> {

    private RingString<ArithmeticInteger> value = null;
    private String simpleString = null;

    /**
     * Constructs a ZStringElement from an ordinary String <code>string</code>.
     * The result is a ZStringElement of the form 1*value.
     */
    public ZStringElement(String string) {
        simpleString = string;
    }


    /**
     * Constructs a ZStringElement from a ZString <code>value</code>.
     */
    public ZStringElement(RingString<ArithmeticInteger> value) {
        this.value = value;
    }


    /**
     * Constructs a ZStringElement from the array of objects <code>objs</code>.
     * @param objs an array of objects where strings alternate with
     *             integers, the array should therefore be of even
     *             length
     */
    public ZStringElement(Object ... objs) {
        int len = objs.length/2;
        String[] words = new String[len];
        ArithmeticInteger[] factors = new ArithmeticInteger[len];
        for (int i = 0; i < len*2; i += 2) {
            if (objs[i] instanceof String && objs[i+1] instanceof Integer) {
                words[i/2] = (String)objs[i];
                factors[i/2] = new ArithmeticInteger((Integer)objs[i+1]);
            }
            else {
                words[i/2] = "";
                factors[i/2] = new ArithmeticInteger(0);
            }
        }
        this.value = new RingString<>(words, factors);
    }
    

    public boolean isOne() {
        if (value != null) {
            return value.equals(RingString.getOne());
        }
        else {
            return false;
        }
    }
       

    public boolean isZero() {
        if (value != null) {
            return value.equals(RingString.getZero());
        }
        else {
            return false;
        }
    }

    public ZStringElement sum(ZStringElement element) {
        return new ZStringElement(getValue().sum(element.getValue()));
    }
    
    public void add(ZStringElement element) {
        zstringify();
        value.add(element.getValue());        
    }

    public ZStringElement difference(ZStringElement element) {
        return new ZStringElement(getValue().difference(element.getValue()));
    }

    public void subtract(ZStringElement element) {
        zstringify();
        value.subtract(element.getValue());
    }


    public ZStringElement negated() {
        return new ZStringElement(getValue().negated());
    }

    
    public void negate() {
        zstringify();
        value.negate();
    }


    public ZStringElement scaled(ZStringElement element)
            throws DomainException {
        return product(element);
    }
    

    public void scale(ZStringElement element)
            throws DomainException {
        multiply(element);
    }

    
    public ZStringElement product(ZStringElement element) {
        return new ZStringElement(getValue().product(element.getValue()));
    }

    
    public void multiply(ZStringElement element) {
        value.multiply(element.getValue());
    }

    
    public ZStringElement inverse() {
        throw new InverseException("Inverse of "+this+" does not exist.");
    }

    
    public void invert() {
        throw new InverseException("Inverse of "+this+" does not exist.");
    }


    public ZStringElement quotient(ZStringElement element)
            throws DomainException, DivisionException {
        if (element instanceof ZStringElement) {
            // TODO: implement division where possible
            throw new DivisionException(this, element);
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }
    
    
    public void divide(ZStringElement element)
            throws DomainException, DivisionException {
        if (element instanceof ZStringElement) {
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

    
    public ZStringRing getRing() {
        return ZStringRing.ring;
    }


    public RingString<ArithmeticInteger> getValue() {
        if (simpleString != null) {
            return new RingString<>(simpleString);
        }
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
            return ZStringProperFreeElement.make(new ArrayList<>(0));
        }
        else {
            List<RingString<ArithmeticInteger>> values = new ArrayList<>(n);
            values.set(0, new RingString<>(value));
            for (int i = 1; i < n; i++) {
                values.set(i, RingString.getZero());
            }
            return ZStringProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ZStringElement) {
            ZStringElement element = (ZStringElement)object;
            if (simpleString != null && element.simpleString != null) {
                return simpleString.equals(element.simpleString);
            }
            else {
                return getValue().equals(element.getValue());
            }
        }
        else {
            return false;
        }
    }


    public int compareTo(ModuleElement object) {
        if (object instanceof ZStringElement) {
	        ZStringElement element = (ZStringElement)object;
	        if (simpleString != null && element.simpleString != null) {
	            return simpleString.compareTo(element.simpleString);
	        }
	        else {
	            return getValue().compareTo(element.getValue());
	        }
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public ZStringElement deepCopy() {
        if (simpleString != null) {
            return new ZStringElement(simpleString);
        }
        else {
            return new ZStringElement(getValue().deepCopy());
        }
    }

    public String toString() {
        if (isSimple()) {
            return "ZStringElement[[1*\""+simpleString+"\"]]";
        }
        else {
            return "ZStringElement["+value+"]";
        }
    }

    
    public String stringRep(boolean ... parens) {
    	if (isSimple()) {
    		return "\""+simpleString+"\"";
    	}
    	else {
    		return getValue().stringRep();
    	}
    }

    public String getSimpleString() {
        return simpleString;
    }

    
    public String getString() {
        if (isSimple()) {
            return simpleString;
        }
        else {
            return getValue().getString();
        }
    }

    
    public HashMap<String,RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<>();
        if (value != null) {
            Set<String> strings = value.getStrings();
            for (String s : strings) {
                map.put(s, new ZElement(((Integer)value.getFactorForString(s))));
            }
        }
        else {
            map.put(simpleString, new ZElement(1));
        }
        return map;
    }

    public String getElementTypeName() {
        return "ZStringElement";
    }

    
    public int hashCode() {
        return value.hashCode();
    }
    
    
    private boolean isSimple() {
        return (simpleString != null);
    }
    
    
    private void zstringify() {
        if (simpleString != null) {
            value = new RingString<>(simpleString);
        }
        simpleString = null;
    }

}
