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
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.List;

/**
 * The field of complex numbers.
 * @see CElement
 * 
 * @author Gérard Milmeister
 */
public final class CRing extends ArithmeticRing<Complex> implements NumberRing {

    /**
     * The unique instance of the ring of complex numbers.
     */
    public static final CRing ring = new CRing();

    @Override
    public CProperFreeModule getNullModule() {
        return CProperFreeModule.nullModule;
    }

    @Override
    public boolean hasElement(ModuleElement<?,?> element) {
        return (element instanceof CElement);
    }
    
    @Override
    public FreeModule<?, ArithmeticElement<Complex>> getFreeModule(int dimension) {
        return CProperFreeModule.make(dimension);
    }

    @Override
    public boolean equals(Object object) {
        return (this == object);
    }

    @Override
    public ArithmeticElement<Complex> createElement(List<ModuleElement<?,?>> elements) {
        if (!elements.isEmpty()) {
            return elements.get(0).cast(this);
        }
        return null;
    }
    
    @Override
    public ArithmeticElement<Complex> cast(ModuleElement<?,?> element) {
        if (element instanceof ArithmeticElement) {
            return cast(((ArithmeticElement<?>) element).getValue());
        }
        if (element instanceof DirectSumElement) {
            return element.cast(this);
        }
        return null;
    }

    public ArithmeticElement<Complex> cast(ArithmeticNumber<?> element) {
        return new ArithmeticElement<>(new Complex(element.doubleValue()));
    }

    
    public String toString() {
        return "CRing";
    }

    
    public String toVisualString() {
        return "C";
    }
    
    
    public ArithmeticElement<Complex> parseString(String string) {
    	try {
    		Complex value = ArithmeticParsingUtils.parseComplex(TextUtils.unparenthesize(string));
        	return new ArithmeticElement<>(value);
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
    
    public int getNumberRingOrder() {
        return 400;
    }


    private static final int basicHash = "CRing".hashCode();

    private CRing() {
        super(new Complex(0), new Complex(1));
    }
}
