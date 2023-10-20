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


    /**
     * Constructs an RStringElement from the array of objects <code>objs</code>.
     * @param objs an array of objects where strings alternate with
     *             doubles, the array should therefore be of even
     *             length
     */
    public RStringElement(Object... objs) {
        super(build(objs));
    }

    private static RingString<ArithmeticDouble> build(Object[] objs) {
        int len = objs.length/2;
        String[] words = new String[len];
        ArithmeticDouble[] factors = new ArithmeticDouble[len];
        for (int i = 0; i < len*2; i += 2) {
            if (objs[i] instanceof String && objs[i+1] instanceof Double) {
                words[i/2] = (String)objs[i];
                factors[i/2] = new ArithmeticDouble((Double)objs[i+1]);
            }
            else {
                words[i/2] = "";
                factors[i/2] = new ArithmeticDouble(0);
            }
        }
        return new RingString<>(words, factors);
    }

    public RStringRing getRing() {
        return RStringRing.ring;
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
