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

import org.vetronauta.latrunculus.core.EntryList;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
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
 * Elements of the ring of strings with integer factors.
 * @see ZStringRing
 * 
 * @author Gérard Milmeister
 */
public final class ZStringElement extends ArithmeticStringElement<ZStringElement,ArithmeticInteger> {

    /**
     * Constructs a ZStringElement from an ordinary String <code>string</code>.
     * The result is a ZStringElement of the form 1*value.
     */
    public ZStringElement(String string) {
        super(string);
    }

    /**
     * Constructs a ZStringElement from a ZString <code>value</code>.
     */
    public ZStringElement(RingString<ArithmeticInteger> value) {
        super(value);
    }

    public ZStringElement(EntryList<String,ArithmeticInteger> entryList) {
        super(entryList);
    }

    @Override
    protected ZStringElement valueOf(RingString<ArithmeticInteger> value) {
        return new ZStringElement(value);
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
            values.add(new RingString<>(getValue()));
            for (int i = 1; i < n; i++) {
                values.add(RingString.getZero());
            }
            return ZStringProperFreeElement.make(values);
        }
    }

    public String toString() {
        return "ZStringElement["+getValue()+"]";
    }

    
    public HashMap<String,RingElement> getTerms() {
        HashMap<String,RingElement> map = new HashMap<>();
        Set<String> strings = getValue().getStrings();
        for (String s : strings) {
            map.put(s, new ArithmeticElement<>(new ArithmeticInteger((Integer)getValue().getFactorForString(s))));
        }
        return map;
    }

}
