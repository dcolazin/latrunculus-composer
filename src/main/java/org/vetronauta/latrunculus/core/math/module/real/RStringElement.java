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
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;

/**
 * Elements in the ring of strings with real factors.
 * @see RStringRing
 * 
 * @author Gérard Milmeister
 */
public final class RStringElement extends ArithmeticStringElement<ArithmeticDouble> {

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

    public RStringElement(EntryList<String,ArithmeticDouble> entryList) {
        super(entryList);
    }

}
