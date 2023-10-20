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
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.List;

/**
 * The field of rationals.
 *
 * @author Gérard Milmeister
 */
public final class QRing extends ArithmeticRing<Rational> implements NumberRing {

    private QRing() {
        super(new Rational(0), new Rational(1));
    }

    /**
     * The unique instance of the ring of rationals.
     */
    public static final QRing ring = new QRing();

    public QProperFreeModule getNullModule() {
        return QProperFreeModule.nullModule;
    }

    public boolean hasElement(ModuleElement element) {
        return element instanceof ArithmeticElement && ((ArithmeticElement<?>) element).getValue() instanceof Rational;
    }

    public FreeModule<?,ArithmeticElement<Rational>> getFreeModule(int dimension) {
        return ArithmeticMultiModule.make(QRing.ring, dimension);
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
    
    public ArithmeticElement<Rational> createElement(List<ModuleElement<?, ?>> elements) {
        if (!elements.isEmpty()) {
            return elements.get(0).cast(this);
        }
        else {
            return null;
        }
    }

    public ArithmeticElement<Rational> cast(ModuleElement<?,?> element) {
        if (element instanceof ArithmeticElement) {
            return cast((ArithmeticElement<?>) element);
        }
        if (element instanceof DirectSumElement) {
            return element.cast(this);
        }
        return null;
    }

    public ArithmeticElement<Rational> cast(ArithmeticElement<?> element) {
        return new ArithmeticElement<>(new Rational(element.getValue().doubleValue()));
    }
    
    public String toString() {
        return "QRing";
    }
    
    
    public String toVisualString() {
        return "Q";
    }

    
    public ArithmeticElement<Rational> parseString(String string) {
    	try {
    		Rational value = ArithmeticParsingUtils.parseRational(TextUtils.unparenthesize(string));
        	return new ArithmeticElement<>(value);
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
