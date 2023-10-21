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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRingRepository;
import org.vetronauta.latrunculus.core.math.module.morphism.generic.ArithmeticAffineMorphism;

/**
 * Affine morphism in <i>Zn</i>.
 * The morphism <i>h</i> is such that <i>h(x) = a*x+b mod n</i>
 * where <i>a</i> and <i>b</i> are integers.
 * 
 * @author Gérard Milmeister
 */
public final class ZnAffineMorphism extends ArithmeticAffineMorphism<ArithmeticModulus> {

    public ZnAffineMorphism(int a, int b, int modulus) {
        super(ArithmeticRingRepository.getModulusRing(modulus), new ArithmeticModulus(a, modulus), new ArithmeticModulus(b, modulus));
    }

    public int getModulus() {
        return getDomain().getOne().getValue().getModulus();
    }

}
