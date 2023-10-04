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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.NumberTheory;
import org.vetronauta.latrunculus.core.math.module.complex.CElement;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;
import org.vetronauta.latrunculus.core.math.module.real.RElement;

import java.util.List;

/**
 * The ring of integers mod <i>n</i>.
 * @see ZnElement
 * 
 * @author Gérard Milmeister
 */
public final class ZnRing extends Ring<ZnElement> implements ZnFreeModule<ZnElement>, NumberRing {

    /**
     * Constructs a ring of integers mod <code>modulus</code>.
     */
    public static ZnRing make(int modulus) {
        assert(modulus > 1);
        return new ZnRing(modulus);
    }
    
    
    public ZnElement getZero() {
        return new ZnElement(0, modulus);
    }

    
    public ZnElement getOne() {
        return new ZnElement(1, modulus);
    }

    
    public ZnElement getUnitElement(int i) {
        return getOne();
    }

    
    public Module getNullModule() {
        return ZnProperFreeModule.make(0, modulus);
    }
    
    
    public boolean isField() {
        return field;
    }
    
    
    public boolean isVectorSpace() {
        return isField();
    }


    public ModuleMorphism getIdentityMorphism() {
        return ModuleMorphism.getIdentityMorphism(this);
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof ZnElement) &&
        	    (((ZnElement)element).getModulus() == modulus);
    }

    
    public FreeModule getFreeModule(int dimension) {
        return ZnProperFreeModule.make(dimension, modulus);
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

    
    public ZnElement createElement(List<ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return elements.get(0).cast(this);
        }
        else {
            return null;
        }
    }

    
    public ZnElement cast(ModuleElement element) {
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
            return (ZnElement) element.cast(this);
        }
        else {
            return null;
        }
    }

    
    public ZnElement cast(ZElement element) {
        return new ZnElement(element.getValue(), modulus);
    }

    
    public ZnElement cast(ZnElement element) {
        if (element.getModulus() == getModulus()) {
            return element;
        }
        else {
            return new ZnElement(element.getValue(), modulus);
        }
    }
    
    
    public ZnElement cast(QElement element) {
        return new ZnElement((int)Math.round(element.getValue().doubleValue()), modulus);
    }

    
    public ZnElement cast(RElement element) {
        return new ZnElement((int)Math.round(element.getValue()), modulus);
    }

    
    public ZnElement cast(CElement element) {
        return new ZnElement((int)Math.round(element.getValue().getReal()), modulus);
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

    
    public ZnElement parseString(String s) {
    	try {
    		int value = Integer.parseInt(TextUtils.unparenthesize(s));
        	return new ZnElement(value, modulus);
    	}
    	catch (NumberFormatException e) {
    		return null;
    	}
    }
    
    public String getElementTypeName() {
        return "ZnRing";
    }

    public int hashCode() {
        return 37*basicHash + modulus;
    }

    private ZnRing(int modulus) {
        this.modulus = modulus;
        this.field = NumberTheory.isPrime(modulus);
    }

    
    private final static int basicHash = "ZnRing".hashCode();

    private int     modulus;
    private boolean field;

    @Override
    public int getNumberRingOrder() {
        return -100;
    }
}
