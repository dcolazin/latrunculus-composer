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

import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;

import java.util.List;

/**
 * The ring of integers mod <i>n</i>.
 *
 * @author Gérard Milmeister
 */
public final class ZnRing extends ArithmeticRing<Modulus> implements NumberRing {

    private ZnRing(int modulus) {
        super(new Modulus(0, modulus), new Modulus(1, modulus));
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
    public VectorModule<ArithmeticElement<Modulus>> getNullModule() {
        return new VectorModule<>(this, 0);
    }

    @Override
    public boolean hasElement(ModuleElement<?,?> element) {
        if (!(element instanceof ArithmeticElement)) {
            return false;
        }
        ArithmeticNumber<?> number = ((ArithmeticElement<?>) element).getValue();
        return number instanceof Modulus && (((Modulus) number).getModulus() == modulus);
    }

    @Override
    public FreeModule<?, ArithmeticElement<Modulus>> getFreeModule(int dimension) {
        return new VectorModule<>(this, dimension);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        else if (object instanceof ZnRing) {
            return ((ZnRing)object).getModulus() == modulus;
        }
        else {
            return false;
        }
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
    public ArithmeticElement<Modulus> createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return this.cast(elements.get(0));
        }
        else {
            return null;
        }
    }

    @Override
    public ArithmeticElement<Modulus> cast(ModuleElement<?,?> element) {
        if (element instanceof ArithmeticElement) {
            return cast((ArithmeticElement<?>) element);
        }
        if (element instanceof DirectSumElement) {
            return this.cast(element.flatComponentList().get(0));
        }
        return null;
    }

    public ArithmeticElement<Modulus> cast(ArithmeticElement<?> element) {
        return new ArithmeticElement<>(new Modulus(element.getValue().intValue(), modulus));
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

    public int hashCode() {
        return 37*basicHash + modulus;
    }


    private static final int basicHash = "ZnRing".hashCode();

    private int     modulus;

    @Override
    public int getNumberRingOrder() {
        return -100;
    }
}
