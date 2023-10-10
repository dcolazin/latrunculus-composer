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

package org.vetronauta.latrunculus.core.math.module.real;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.module.complex.CElement;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;

import java.util.List;

/**
 * The field of real numbers.
 * @see RElement
 * 
 * @author Gérard Milmeister
 */
public final class RRing extends Ring<RElement> implements RFreeModule<RElement>, NumberRing {

    /**
     * The unique instance of the ring of reals.
     */
    public static final RRing ring = new RRing();

    public RElement getZero() {
        return new RElement(0);
    }


    public RElement getOne() {
        return new RElement(1);
    }


    public RElement getUnitElement(int i) {
        return getOne();
    }

    
    public Module getNullModule() {
        return RProperFreeModule.nullModule;
    }
    
    
    public boolean isField() {
        return true;
    }
    
    
    public boolean isVectorSpace() {
        return true;
    }


    public ModuleMorphism getIdentityMorphism() {
        return ModuleMorphism.getIdentityMorphism(this);
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof RElement);
    }

    
    public FreeModule getFreeModule(int dimension) {
        return RProperFreeModule.make(dimension);
    }

    
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


    public RElement createElement(List<ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return elements.get(0).cast(this);
        }
        else {
            return null;
        }
    }

    
    public RElement cast(ModuleElement element) {
        if (element instanceof ZElement) {
            return cast((ZElement)element);
        }
        else if (element instanceof ZnElement) {
            return cast((ZnElement)element);
        }
        else if (element instanceof QElement) {
            return cast((QElement)element);
        }
        else if (element instanceof RElement) {
            return (RElement) element;
        }
        else if (element instanceof CElement) {
            return cast((CElement)element);
        }
        else if (element instanceof DirectSumElement) {
            return (RElement) element.cast(this);
        }
        else {
            return null;
        }
    }

    
    public RElement cast(ZElement element) {
        return new RElement(element.getValue().intValue());
    }

    
    public RElement cast(ZnElement element) {
        return new RElement(element.getValue().getValue());
    }
    
    
    public RElement cast(QElement element) {
        return new RElement(element.getValue().doubleValue());
    }

    
    public RElement cast(CElement element) {
        return new RElement(element.getValue().getReal());
    }

    
    public String toString() {
        return "RRing";
    }


    public String toVisualString() {
        return "R";
    }
    
    
    public RElement parseString(String string) {
    	try {
    		double value = Double.parseDouble(TextUtils.unparenthesize(string));
        	return new RElement(value);
    	}
    	catch (NumberFormatException e) {
    		return null;
    	}
    }
    
    public String getElementTypeName() {
        return "RRing";
    }

    public int hashCode() {
        return basicHash;
    }
    
    
    public int getNumberRingOrder() {
        return 300;
    }

    
    private final static int basicHash = "RRing".hashCode();

    private RRing() { /* not allowed */ }
}
