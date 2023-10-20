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


    /**
     * Constructs a ZStringElement from the array of objects <code>objs</code>.
     * @param objs an array of objects where strings alternate with
     *             integers, the array should therefore be of even
     *             length
     */
    public ZStringElement(Object ... objs) {
        super(build(objs));
    }

    private static RingString<ArithmeticInteger> build(Object[] objs) {
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
        return new RingString<>(words, factors);
    }

    @Override
    protected ZStringElement valueOf(RingString<ArithmeticInteger> value) {
        return new ZStringElement(value);
    }
    
    public ZStringRing getRing() {
        return ZStringRing.ring;
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

    public String getElementTypeName() {
        return "ZStringElement";
    }

}
