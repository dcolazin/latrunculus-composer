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

import lombok.NonNull;
import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.ZeroDivisorException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

/**
 * Elements in the ring of integers mod <i>n</i>.
 * @see ZnRing
 * 
 * @author Gérard Milmeister
 */
public final class ZnElement extends ArithmeticElement<ZnElement, ArithmeticModulus> {

    /**
     * Constructs a ZnElement <code>value</code> mod <code>modulus</code>.
     */
    public ZnElement(int value, int modulus) {
        super(new ArithmeticModulus(value, modulus));
    }

    @Override
    public ZnElement power(int n) {
        return new ZnElement(NumberTheory.powerMod(getValue().intValue(), getModulus(), n), getModulus());
    }
    public int getModulus() {
        return getValue().getModulus();
    }

    public String getElementTypeName() {
        return "ZnElement";
    }

}
