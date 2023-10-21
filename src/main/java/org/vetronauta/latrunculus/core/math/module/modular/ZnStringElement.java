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

import lombok.NonNull;
import org.vetronauta.latrunculus.core.EntryList;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.ZeroDivisorException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Elements of the ring of strings with integer mod <i>n</i> factors.
 * @see ZnStringRing
 * 
 * @author Gérard Milmeister
 */
public final class ZnStringElement extends ArithmeticStringElement<ZnStringElement,ArithmeticModulus> {

    //TODO various consistency checks for modulus

    /**
     * Constructs a ZnStringElement from a <code>string</code> mod <code>modulus</code>.
     * The result is a ZnStringElement of the form 1*value.
     */
    public ZnStringElement(String string, int modulus) {
        super(new RingString<>(string));
        this.modulus = modulus;
    }

    public ZnStringElement(RingString<ArithmeticModulus> value) {
        super(value);
        this.modulus = extractModulus();
    }

    /**
     * Constructs a ZnStringElement from a ZnString <code>value</code>.
     */
    public ZnStringElement(RingString<ArithmeticModulus> value, int modulus) {
        super(value);
        this.modulus = modulus;
    }

    public ZnStringElement(EntryList<String, ArithmeticModulus> entryList, int modulus) {
        super(entryList);
        this.modulus = modulus;
    }

    @Override
    protected ZnStringElement valueOf(@NonNull RingString<ArithmeticModulus> value) {
        return new ZnStringElement(value);
    }

    public ZnStringRing getRing() {
        if (module == null) {
            module = ZnStringRing.make(modulus);
        }
        return module;
    }
    
    public FreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return ZnStringProperFreeElement.make(new ArrayList<>(), modulus);
        }
        else {
            List<RingString<ArithmeticModulus>> values = new ArrayList<>(n);
            values.add(getValue());
            for (int i = 1; i < n; i++) {
                values.add(RingString.getZero());
            }
            return ZnStringProperFreeElement.make(values, modulus);
        }
    }

    private int extractModulus() {
        return getValue().getFactors().stream().findFirst().map(ArithmeticModulus::getModulus).orElseThrow(ZeroDivisorException::new);
    }
    
    public int getModulus() {
        return modulus;
    }

    
    public String toString() {
        return "ZnStringElement("+getModulus()+")["+getValue()+"]";
    }
    
    public HashMap<String,RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<>();
        Set<String> strings = getValue().getStrings();
        for (String s : strings) {
            map.put(s, new ArithmeticElement<>(new ArithmeticInteger((Integer)getValue().getFactorForString(s))));
        }
        return map;
    }

    public String getElementTypeName() {
        return "ZnStringElement";
    }

    private int      modulus;
    private ZnStringRing   module = null;

}
