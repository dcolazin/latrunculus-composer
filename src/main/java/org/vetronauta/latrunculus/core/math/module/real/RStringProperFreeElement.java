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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Elements in a free module over RString.
 * @see RStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class RStringProperFreeElement extends ArithmeticStringMultiElement<RStringElement,ArithmeticDouble> {

    public static final RStringProperFreeElement nullElement = new RStringProperFreeElement(new ArrayList<>());

    public static FreeElement<?, RStringElement> make(List<RingString<ArithmeticDouble>> v) {
        assert(v != null);
        if (v.isEmpty()) {
            return nullElement;
        }
        else if (v.size() == 1) {
            return new RStringElement(v.get(0));
        }
        else {
            return new RStringProperFreeElement(v);
        }
    }

    private RStringProperFreeElement(List<RingString<ArithmeticDouble>> value) {
        super(RRing.ring, value);
    }

}
