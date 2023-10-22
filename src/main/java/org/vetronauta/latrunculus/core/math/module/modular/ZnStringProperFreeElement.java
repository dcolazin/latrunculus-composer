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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Elements in a free modules over ZnString.
 * @see ZnStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ZnStringProperFreeElement extends ArithmeticStringMultiElement<ZnStringElement,ArithmeticModulus> {

    //TODO various consistency checks for modulus

    private final int modulus;

    public static FreeElement<?, ZnStringElement> make(List<RingString<ArithmeticModulus>> v, int modulus) {
        if (v == null || v.isEmpty()) {
            return new ZnStringProperFreeElement(new ArrayList<>(0), modulus);
        }
        else if (v.size() == 1) {
            return new ZnStringElement(v.get(0), modulus);
        }
        return new ZnStringProperFreeElement(v, modulus);
    }

    public int getModulus() {
        return modulus;
    }

    private ZnStringProperFreeElement(List<RingString<ArithmeticModulus>> value, int modulus) {
        super(ZnRing.make(modulus), value);
        this.modulus = modulus;
    }

}
