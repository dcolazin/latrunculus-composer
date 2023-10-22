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
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;
import org.vetronauta.latrunculus.core.math.module.repository.ArithmeticRingRepository;

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

    private final List<RingString<ArithmeticModulus>> value;
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

    @Override
    protected ArithmeticStringMultiElement<ZnStringElement, ArithmeticModulus> valueOf(@NonNull List<RingString<ArithmeticModulus>> value) {
        return new ZnStringProperFreeElement(value, value.get(0).getObjectOne().getModulus());
    }

    public int getModulus() {
        return modulus;
    }
    
    
    public FreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            List<RingString<ArithmeticModulus>> values = new ArrayList<>(n);
            for (int i = 0; i < minlen; i++) {
                values.add(getValue(i));
            }
            for (int i = minlen; i < n; i++) {
                values.add(RingString.getZero());
            }
            return ZnStringProperFreeElement.make(values, modulus);
        }
    }

    private ZnStringProperFreeElement(List<RingString<ArithmeticModulus>> value, int modulus) {
        super(ZnRing.make(modulus), value);
        this.value = value;
        this.modulus = modulus;
    }

}
