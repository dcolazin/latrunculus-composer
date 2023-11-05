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

import org.vetronauta.latrunculus.core.math.element.generic.Arithmetic;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;

import java.util.List;

/**
 * The ring of integers.
 *
 * @author Gérard Milmeister
 */
public final class ZRing extends Ring<ZInteger> implements NumberRing {

    /**
     * The unique instance of the ring of integers.
     */
    public static final ZRing ring = new ZRing();
    public static final VectorModule<ZInteger> nullModule = new VectorModule<>(ring, 0);

    @Override
    public ZInteger getZero() {
        return new ZInteger(0);
    }

    @Override
    public VectorModule<ZInteger> getNullModule() {
        return nullModule;
    }
    
    @Override
    public boolean hasElement(ModuleElement<?,?> element) {
        return element instanceof ZInteger;
    }

    @Override
    public ZInteger getOne() {
        return new ZInteger(1);
    }

    @Override
    public boolean isField() {
        return false;
    }

    public FreeModule<?, ZInteger> getFreeModule(int dimension) {
        return new VectorModule<>(ring, dimension);
    }

    @Override
    protected boolean nonSingletonEquals(Object object) {
        return false;
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
    public ZInteger createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return this.cast(elements.get(0));
        }
        return null;
    }

    @Override
    public ZInteger cast(ModuleElement<?,?> element) {
        if (element instanceof Arithmetic) {
            return new ZInteger(((Arithmetic) element).intValue());
        }
        if (element instanceof DirectSumElement) {
            return this.cast(element.flatComponentList().get(0));
        }
        return null;
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

    @Override
    protected int nonSingletonHashCode() {
        return 1;
    }

    public int getNumberRingOrder() {
        return 100;
    }

}
