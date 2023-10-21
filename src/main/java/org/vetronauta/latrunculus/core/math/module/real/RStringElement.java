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

import lombok.NonNull;
import org.vetronauta.latrunculus.core.EntryList;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Elements in the ring of strings with real factors.
 * @see RStringRing
 * 
 * @author Gérard Milmeister
 */
public final class RStringElement extends ArithmeticStringElement<RStringElement,ArithmeticDouble> {

    /**
     * Constructs an RStringElement from an RString <code>value</code>.
     */
    public RStringElement(RingString<ArithmeticDouble> value) {
        super(value);
    }

    
    /**
     * Constructs an RStringElement from a simple string <code>value</code>.
     * The result is an RStringElement of the form 1.0*value.
     */
    public RStringElement(String value) {
        super(value);
    }

    @Override
    protected RStringElement valueOf(@NonNull RingString<ArithmeticDouble> value) {
        return new RStringElement(value);
    }

    public RStringElement(EntryList<String,ArithmeticDouble> entryList) {
        super(entryList);
    }

    public FreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return RStringProperFreeElement.make(new ArrayList<>());
        }
        else {
            List<RingString<ArithmeticDouble>> values = new ArrayList<>(n);
            values.add(new RingString<>(getValue()));
            for (int i = 1; i < n; i++) {
                values.add(RingString.getZero());
            }
            return RStringProperFreeElement.make(values);
        }
    }

    public String toString() {
        return "RStringElement["+getValue()+"]";
    }
    
    public HashMap<String,RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<>();
        Set<String> strings = getValue().getStrings();
        for (String s : strings) {
            map.put(s, new ArithmeticElement<>(new ArithmeticDouble((Double)getValue().getFactorForString(s))));
        }
        return map;
    }

    public String getElementTypeName() {
        return "RStringElement";
    }

}
