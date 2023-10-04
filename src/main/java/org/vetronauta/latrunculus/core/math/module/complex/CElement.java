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

package org.vetronauta.latrunculus.core.math.module.complex;

import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.module.definition.ConjugableElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

/**
 * Elements in the field of complex numbers.
 * @see CRing
 *
 * @author Gérard Milmeister
 */
public final class CElement extends ArithmeticElement<Complex> implements ConjugableElement<ArithmeticElement<Complex>,ArithmeticElement<Complex>> {

    /**
     * Constructs a CElement with complex number <code>value</code>.
     */
    public CElement(Complex value) {
        super(value);
    }

    /**
     * Constructs a CElement with complex (real) number <code>value</code>.
     */
    public CElement(double value) {
        super(new Complex(value));
    }
    
    /**
     * Constructs a CElement with complex number <code>x</code> + i <code>y</code>.
     */
    public CElement(double x, double y) {
        super(new Complex(x, y));
    }

    public CElement conjugated() {
        return new CElement(getValue().conjugated());
    }

    public void conjugate() { //TODO hack
        Complex conj = getValue().conjugated();
        subtract(new ArithmeticElement<>(getValue()));
        add(new ArithmeticElement<>(conj));
    }

    public double[] fold(ModuleElement[] elements) {
        double[][] res = new double[elements.length][2];
        for (int i = 0; i < elements.length; i++) {
            CElement c = (CElement)elements[i];           
            res[i][0] = c.getValue().getReal();
            res[i][1] = c.getValue().getImag();
        }
        return Folding.fold(res);
    }

    @Override
    public CRing getRing() {
        return CRing.ring;
    }

}
