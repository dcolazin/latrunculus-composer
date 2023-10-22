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
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;

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

}
