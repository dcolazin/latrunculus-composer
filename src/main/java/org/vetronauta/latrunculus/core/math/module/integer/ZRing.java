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

package org.vetronauta.latrunculus.core.math.module.integer;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.module.complex.CElement;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;
import org.vetronauta.latrunculus.core.math.module.real.RElement;

import java.util.List;

/**
 * The ring of integers.
 * @see ZElement
 * 
 * @author Gérard Milmeister
 */
public final class ZRing extends ArithmeticRing<ZElement> implements NumberRing {

    private ZRing() {
        super(new ZElement(0), new ZElement(1));
    }


    /**
     * The unique instance of the ring of integers.
     */
    public static final ZRing ring = new ZRing();

    @Override
    public ZProperFreeModule getNullModule() {
        return ZProperFreeModule.nullModule;
    }
    
    @Override
    public boolean hasElement(ModuleElement<?,?> element) {
        return (element instanceof ZElement);
    }

    
    public FreeModule<?, ZElement> getFreeModule(int dimension) {
        return ZProperFreeModule.make(dimension);
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


    public ZElement createElement(List<ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return (ZElement) elements.get(0).cast(this);
        }
        return null;
    }

    
    public ZElement cast(ModuleElement element) {
        if (element instanceof ZElement) {
            return (ZElement)element;
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
            return (ZElement)element.cast(this);
        }
        else {
            return null;
        }
    }

    
    public ZElement cast(ZnElement element) {
        return new ZElement(element.getValue().getValue());
    }
    
    
    public ZElement cast(QElement element) {
        return new ZElement((int)Math.round(element.getValue().doubleValue()));
    }

    
    public ZElement cast(RElement element) {
        return new ZElement((int)Math.round(element.getValue()));
    }

    
    public ZElement cast(CElement element) {
        return new ZElement((int)Math.round(element.getValue().getReal()));
    }

    
    public String toString() {
        return "ZRing";
    }


    public String toVisualString() {
        return "Z";
    }
    
    
    public ZElement parseString(String s) {
    	try {
    		int value = Integer.parseInt(TextUtils.unparenthesize(s));
        	return new ZElement(value);
    	}
    	catch (NumberFormatException e) {
    		return null;
    	}
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

    
    private final static int basicHash = "ZRing".hashCode();

}
