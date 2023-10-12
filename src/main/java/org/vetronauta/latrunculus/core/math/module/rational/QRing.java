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

package org.vetronauta.latrunculus.core.math.module.rational;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.complex.CElement;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.core.math.module.real.RElement;

import java.util.List;

/**
 * The field of rationals.
 * @see QElement
 * 
 * @author Gérard Milmeister
 */
public final class QRing extends ArithmeticRing<QElement> implements NumberRing {

    private QRing() {
        super(new QElement(0), new QElement(1));
    }

    /**
     * The unique instance of the ring of rationals.
     */
    public static final QRing ring = new QRing();

    public QProperFreeModule getNullModule() {
        return QProperFreeModule.nullModule;
    }

    public boolean hasElement(ModuleElement element) {
        return element instanceof QElement;
    }

    public FreeModule<?,QElement> getFreeModule(int dimension) {
        return QProperFreeModule.make(dimension);
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
    
    public QElement createElement(List<ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return elements.get(0).cast(this);
        }
        else {
            return null;
        }
    }

    public QElement cast(ModuleElement element) {
        if (element instanceof ZElement) {
            return cast((ZElement)element);
        }
        else if (element instanceof ZnElement) {
            return cast((ZnElement)element);
        }
        else if (element instanceof QElement) {
            return (QElement)element;
        }
        else if (element instanceof RElement) {
            return cast((RElement)element);
        }
        else if (element instanceof CElement) {
            return cast((CElement)element);
        }
        else if (element instanceof DirectSumElement) {
            return (QElement)element.cast(this);
        }
        else {
            return null;
        }
    }

    
    public QElement cast(ZElement element) {
        return new QElement(element.getValue().intValue(), 1);
    }

    
    public QElement cast(ZnElement element) {
        return new QElement(element.getValue().getValue(), 1);
    }
    
    
    public QElement cast(RElement element) {
        return new QElement(new Rational(element.getValue().doubleValue()));
    }

    
    public QElement cast(CElement element) {
        return new QElement(new Rational(element.getValue().getReal()));
    }

    
    public String toString() {
        return "QRing";
    }
    
    
    public String toVisualString() {
        return "Q";
    }

    
    public QElement parseString(String string) {
    	try {
    		Rational value = Rational.parseRational(TextUtils.unparenthesize(string));
        	return new QElement(value);
    	}
    	catch (NumberFormatException e) {
    		return null;
    	}
    }
    
    public String getElementTypeName() {
        return "QRing";
    }

    public int hashCode() {
        return basicHash;
    }
    
    
    public int getNumberRingOrder() {
        return 200;
    }


    private static final int basicHash = "QRing".hashCode();

}
