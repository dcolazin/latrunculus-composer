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

import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineRingMorphism;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

/**
 * Affine morphism in <i>R</i>.
 * The morphism <i>h</i> is such that <i>h(x) = a*x+b</i>
 * where <i>a</i> and <i>b</i> are reals.
 * 
 * @author Gérard Milmeister
 */
public final class RAffineMorphism extends ArithmeticAffineRingMorphism<Real> {

    public RAffineMorphism(double a, double b) {
        super(RRing.ring, new Real(a), new Real(b));
    }

}
