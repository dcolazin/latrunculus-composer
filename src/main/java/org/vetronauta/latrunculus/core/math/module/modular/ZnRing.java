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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.List;

/**
 * The ring of integers mod <i>n</i>.
 *
 * @author Gérard Milmeister
 */
public final class ZnRing extends ArithmeticRing<ArithmeticModulus> implements NumberRing, Modular {

    private ZnRing(int modulus) {
        super(new ArithmeticModulus(0, modulus), new ArithmeticModulus(1, modulus));
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
    public ZnProperFreeModule getNullModule() {
        return (ZnProperFreeModule) ZnProperFreeModule.make(0, modulus);
    }

    @Override
    public boolean hasElement(ModuleElement<?,?> element) {
        if (!(element instanceof ArithmeticElement)) {
            return false;
        }
        ArithmeticNumber<?> number = ((ArithmeticElement<?>) element).getValue();
        return number instanceof ArithmeticModulus && (((ArithmeticModulus) number).getModulus() == modulus);
    }

    
    public ZnProperFreeModule getFreeModule(int dimension) {
        return (ZnProperFreeModule) ZnProperFreeModule.make(dimension, modulus);
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

    
    public ArithmeticElement<ArithmeticModulus> createElement(List<ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return elements.get(0).cast(this);
        }
        else {
            return null;
        }
    }

    public ArithmeticElement<ArithmeticModulus> cast(ModuleElement<?,?> element) {
        if (element instanceof ArithmeticElement) {
            return cast((ArithmeticElement<?>) element);
        }
        if (element instanceof DirectSumElement) {
            return element.cast(this);
        }
        return null;
    }

    public ArithmeticElement<ArithmeticModulus> cast(ArithmeticElement<?> element) {
        return new ArithmeticElement<>(new ArithmeticModulus(element.getValue().intValue(), modulus));
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

    
    public ArithmeticElement<ArithmeticModulus> parseString(String s) {
    	try {
    		int value = Integer.parseInt(TextUtils.unparenthesize(s));
        	return new ArithmeticElement<>(new ArithmeticModulus(value, modulus));
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


    private static final int basicHash = "ZnRing".hashCode();

    private int     modulus;

    @Override
    public int getNumberRingOrder() {
        return -100;
    }
}
