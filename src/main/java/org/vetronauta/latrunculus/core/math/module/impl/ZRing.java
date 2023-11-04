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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
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
 * The ring of integers.
 *
 * @author Gérard Milmeister
 */
public final class ZRing extends ArithmeticRing<ArithmeticInteger> implements NumberRing {

    /**
     * The unique instance of the ring of integers.
     */
    public static final ZRing ring = new ZRing();
    public static final VectorModule<ArithmeticElement<ArithmeticInteger>> nullModule = new VectorModule<>(ring, 0);

    private ZRing() {
        super(new ArithmeticInteger(0), new ArithmeticInteger(1));
    }

    @Override
    public VectorModule<ArithmeticElement<ArithmeticInteger>> getNullModule() {
        return nullModule;
    }
    
    @Override
    public boolean hasElement(ModuleElement<?,?> element) {
        return (element instanceof ArithmeticElement) && (((ArithmeticElement<?>)element).getValue() instanceof ArithmeticInteger);
    }

    
    public FreeModule<?, ArithmeticElement<ArithmeticInteger>> getFreeModule(int dimension) {
        return new VectorModule<>(ring, dimension);
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
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
    public ArithmeticElement<ArithmeticInteger> createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return this.cast(elements.get(0));
        }
        return null;
    }

    @Override
    public ArithmeticElement<ArithmeticInteger> cast(ModuleElement<?,?> element) {
        if (element instanceof ArithmeticElement) {
            return cast((ArithmeticElement<?>) element);
        }
        if (element instanceof DirectSumElement) {
            return this.cast(element.flatComponentList().get(0));
        }
        return null;
    }

    
    public ArithmeticElement<ArithmeticInteger> cast(ArithmeticElement<?> element) {
        return new ArithmeticElement<>(new ArithmeticInteger(element.getValue().intValue()));
    }
    
    public String toString() {
        return "ZRing";
    }


    public String toVisualString() {
        return "Z";
    }
    
    public String getElementTypeName() {
        return "ZRing";
    }

    public int hashCode() {
        return basicHash;
    }

    public int getNumberRingOrder() {
        return 100;
    }

    private static final int basicHash = "ZRing".hashCode();

}
