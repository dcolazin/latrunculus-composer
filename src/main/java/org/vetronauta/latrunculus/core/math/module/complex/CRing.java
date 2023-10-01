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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;
import org.vetronauta.latrunculus.core.math.module.real.RElement;

import java.util.List;

/**
 * The field of complex numbers.
 * @see CElement
 * 
 * @author Gérard Milmeister
 */
public final class CRing extends NumberRing<CElement> implements CFreeModule<CElement> {

    /**
     * The unique instance of the ring of complex numbers.
     */
    public static final CRing ring = new CRing();

    @Override
    public CElement getZero() {
        return new CElement(0);
    }

    @Override
    public CElement getOne() {
        return new CElement(1);
    }
    
    @Override
    public CElement getUnitElement(int i) {
        return getOne();
    }

    @Override
    public CFreeModule<?> getNullModule() {
        return CProperFreeModule.nullModule;
    }

    @Override
    public boolean isField() {
        return true;
    }

    @Override
    public boolean isVectorSpace() {
        return true;
    }

    @Override
    public ModuleMorphism getIdentityMorphism() {
        return ModuleMorphism.getIdentityMorphism(this);
    }

    @Override
    public boolean hasElement(ModuleElement<?,?> element) {
        return (element instanceof CElement);
    }
    
    @Override
    public CFreeModule<?> getFreeModule(int dimension) {
        return CProperFreeModule.make(dimension);
    }

    
    public boolean equals(Object object) {
        return (this == object);
    }

    public int compareTo(Module object) {
        if (this == object) {
            return 0;
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public CElement createElement(List<ModuleElement<?,?>> elements) {
        if (!elements.isEmpty()) {
            return (CElement)elements.get(0).cast(this);
        }
        else {
            return null;
        }
    }
    
    @Override
    public CElement cast(ModuleElement<?,?> element) {
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
            return cast((RElement)element);
        }
        else if (element instanceof CElement) {
            return cast((CElement)element);
        }
        else if (element instanceof DirectSumElement) {
            return (CElement)element.cast(this);
        }
        else {
            return null;
        }
    }

    
    public CElement cast(ZElement element) {
        return new CElement(element.getValue());
    }

    
    public CElement cast(ZnElement element) {
        return new CElement(element.getValue());
    }
    
    
    public CElement cast(QElement element) {
        return new CElement(element.getValue().doubleValue());
    }

    
    public CElement cast(RElement element) {
        return new CElement(element.getValue());
    }

    
    public CElement cast(CElement element) {
        return element;
    }

    
    public String toString() {
        return "CRing";
    }

    
    public String toVisualString() {
        return "C";
    }
    
    
    public CElement parseString(String string) {
    	try {
    		Complex value = Complex.parseComplex(TextUtils.unparenthesize(string));
        	return new CElement(value);
    	}
    	catch (NumberFormatException e) {
    		return null;
    	}
    }

    public String getElementTypeName() {
        return "CRing";
    }

    public int hashCode() {
        return basicHash;
    }

    
    protected int getNumberRingOrder() {
        return 400;
    }


    private static final int basicHash = "CRing".hashCode();

    private CRing() { /* not allowed */ }
}
