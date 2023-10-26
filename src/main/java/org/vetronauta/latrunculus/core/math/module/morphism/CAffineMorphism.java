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

import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineMorphism;

/**
 * Affine morphism in <i>C</i>.
 * The morphism <code>h</code> is such that <i>h(x) = a*x+b</i>
 * where <code>a</code> and <code>b</code> are complex numbers.
 * 
 * @author Gérard Milmeister
 */
public final class CAffineMorphism extends ArithmeticAffineMorphism<Complex> {

    /**
     * Constructs an affine morphism <i>h(x) = a*x+b</i>.
     */
    public CAffineMorphism(Complex a, Complex b) {
        super(CRing.ring, a, b);
    }

}
