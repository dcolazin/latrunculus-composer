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
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

/**
 * Elements in the ring of integers.
 * @see ZRing
 * 
 * @author Gérard Milmeister
 */
public final class ZElement extends ArithmeticElement<ArithmeticInteger> {

    /**
     * Constructs a ZElement with integer <code>value</code>.
     */
    public ZElement(int value) {
        super(new ArithmeticInteger(value));
    }

    @Override
    public ZRing getRing() {
        return ZRing.ring;
    }

    @Override
    public double[] fold(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            ZElement e = (ZElement)elements[i];
            res[i] = e.getValue().intValue();
        }
        return res;
    }

}
