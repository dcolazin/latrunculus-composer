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

package org.vetronauta.latrunculus.core.math.module.impl;

import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.math.element.generic.Arithmetic;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.generic.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.NumberRing;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;

import java.util.List;

/**
 * The ring of integers mod <i>n</i>.
 *
 * @author Gérard Milmeister
 */
public final class ZnRing extends Ring<Modulus> implements NumberRing {

    private final int modulus;

    private ZnRing(int modulus) {
        this.modulus = modulus;
    }

    /**
     * Constructs a ring of integers mod <code>modulus</code>.
     */
    public static ZnRing make(int modulus) {
        assert(modulus > 1);
        return new ZnRing(modulus);
    }

    @Override
    public Modulus getZero() {
        return new Modulus(0, modulus);
    }

    @Override
    public VectorModule<Modulus> getNullModule() {
        return new VectorModule<>(this, 0);
    }

    @Override
    public boolean hasRingElement(RingElement<?> element) {
        return element instanceof Modulus && modulus == ((Modulus) element).getModulus();
    }

    @Override
    public Modulus getOne() {
        return new Modulus(1, modulus);
    }

    @Override
    public boolean isField() {
        return NumberTheory.isPrime(modulus);
    }

    @Override
    protected boolean nonSingletonEquals(Object object) {
        return object instanceof ZnRing && modulus == ((ZnRing) object).getModulus();
    }

    @Override
    public int compareTo(Module object) {
        if (object == this) {
            return 0;
        }
        if (object instanceof NumberRing) {
            return order((NumberRing) object);
        }
        return super.compareTo(object);
    }

    @Override
    public Modulus createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return this.cast(elements.get(0));
        }
        else {
            return null;
        }
    }

    @Override
    public Modulus ringCast(ModuleElement<?,?> element) {
        if (element instanceof Arithmetic) {
            return new Modulus(((Arithmetic) element).intValue(), modulus);
        }
        if (element instanceof DirectSumElement) {
            return this.cast(element.flatComponentList().get(0));
        }
        return null;
    }

    public int getModulus() {
        return modulus;
    }

    
    public String toString() {
        return "ZnRing("+getModulus()+")";
    }

    
    public String toVisualString() {
        return "Z_"+getModulus();
    }
    
    public String getElementTypeName() {
        return "ZnRing";
    }

    @Override
    protected int nonSingletonHashCode() {
        return modulus;
    }

    @Override
    public int getNumberRingOrder() {
        return -100;
    }
}
