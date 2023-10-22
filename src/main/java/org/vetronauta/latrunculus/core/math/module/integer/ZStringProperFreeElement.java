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

import lombok.NonNull;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;

import java.util.ArrayList;
import java.util.List;


/**
 * Elements in a free module of ZString.
 * @see ZStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ZStringProperFreeElement extends ArithmeticStringMultiElement<ZStringElement,ArithmeticInteger> {

    public static ZStringProperFreeElement nullElement = new ZStringProperFreeElement(new ArrayList<>());

    public static FreeElement<?,ZStringElement> make(List<RingString<ArithmeticInteger>> v) {
        assert(v != null);
        if (v.isEmpty()) {
            return nullElement;
        }
        else if (v.size() == 1) {
            return new ZStringElement(v.get(0));
        }
        else {
            return new ZStringProperFreeElement(v);
        }
    }

    public FreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            List<RingString<ArithmeticInteger>> values = new ArrayList<>(n);
            for (int i = 0; i < minlen; i++) {
                values.add(getValue(i));
            }
            for (int i = minlen; i < n; i++) {
                values.add(RingString.getZero());
            }
            return ZStringProperFreeElement.make(values);
        }
    }

    private ZStringProperFreeElement(List<RingString<ArithmeticInteger>> value) {
        super(ZRing.ring, value);
    }

    @Override
    protected ArithmeticStringMultiElement<ZStringElement, ArithmeticInteger> valueOf(@NonNull List<RingString<ArithmeticInteger>> value) {
        return new ZStringProperFreeElement(value);
    }

}
