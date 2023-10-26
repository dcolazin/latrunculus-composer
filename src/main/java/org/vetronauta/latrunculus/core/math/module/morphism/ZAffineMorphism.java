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

package org.vetronauta.latrunculus.core.math.module.morphism;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineRingMorphism;

/**
 * Affine morphism in the <i>Z</i>.
 * The morphism h is such that <i>h(x) = a*x+b</i>
 * where <i>a</i> and <i>b</i> are integers.
 * 
 * @author Gérard Milmeister
 */
public final class ZAffineMorphism extends ArithmeticAffineRingMorphism<ArithmeticInteger> {

    /**
     * Creates an affine morphism <i>h(x) = a*x+b</i>.
     */
    public ZAffineMorphism(int a, int b) {
        super(ZRing.ring, new ArithmeticInteger(a), new ArithmeticInteger(b));
    }

}
