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
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;

import java.util.List;

/**
 * The field of complex numbers.
 * @author Gérard Milmeister
 */
public final class CRing extends Ring<Complex> implements NumberRing {

    /**
     * The unique instance of the ring of complex numbers.
     */
    public static final CRing ring = new CRing();
    public static final VectorModule<Complex> nullModule = new VectorModule<>(ring, 0);

    @Override
    public Complex getZero() {
        return new Complex();
    }

    @Override
    public VectorModule<Complex> getNullModule() {
        return nullModule;
    }

    @Override
    public boolean hasRingElement(RingElement<?> ringElement) {
        return ringElement instanceof Complex;
    }

    @Override
    public Complex getOne() {
        return new Complex(1);
    }

    @Override
    public boolean isField() {
        return true;
    }

    @Override
    public Complex createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return this.cast(elements.get(0));
        }
        return null;
    }
    
    @Override
    public Complex ringCast(ModuleElement<?,?> element) {
        if (element instanceof Arithmetic) {
            return new Complex(((Arithmetic) element).doubleValue());
        }
        if (element instanceof DirectSumElement) {
            return this.cast(element.flatComponentList().get(0));
        }
        return null;
    }
    
    public String toString() {
        return "CRing";
    }

    
    public String toVisualString() {
        return "C";
    }

    public String getElementTypeName() {
        return "CRing";
    }

    @Override
    protected int nonSingletonHashCode() {
        return 1;
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
    protected boolean nonSingletonEquals(Object object) {
        return false;
    }

    public int getNumberRingOrder() {
        return 400;
    }

}
