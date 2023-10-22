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
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.LinkedList;

/**
 * The ring of ZString.
 * @see ZStringElement
 * 
 * @author Gérard Milmeister
 */
public final class ZStringRing extends StringRing<ArithmeticStringElement<ArithmeticInteger>> {

    public static final ZStringRing ring = new ZStringRing();

    public ArithmeticStringElement<ArithmeticInteger> getZero() {
        //TODO why doesn't it like with the <> operator?
        return new ArithmeticStringElement<ArithmeticInteger>(RingString.getZero());
    }

    
    public ArithmeticStringElement<ArithmeticInteger> getOne() {
        //TODO why doesn't it like with the <> operator?
        return new ArithmeticStringElement<ArithmeticInteger>(RingString.getOne());
    }

    
    public ZStringProperFreeModule getNullModule() {
        return ZStringProperFreeModule.nullModule;
    }
    
    
    public boolean isField() {
        return false;
    }
    
    
    public boolean isVectorSpace() {
        return false;
    }

    
    public ModuleMorphism getIdentityMorphism() {
        return ModuleMorphism.getIdentityMorphism(this);
    }

    
    public boolean hasElement(ModuleElement element) {
        return element instanceof ZStringElement;
    }

    
    public FreeModule<?,ArithmeticStringElement<ArithmeticInteger>> getFreeModule(int dimension) {
        return ZStringProperFreeModule.make(dimension);
    }

    
    public ZRing getFactorRing() {
        return ZRing.ring;
    }

    
    public boolean equals(Object object) {
        return this == object;
    }

    
    public ZStringElement cast(ModuleElement element) {
        if (element instanceof ZStringElement) {
            return (ZStringElement) element;
        }
        else if (element instanceof StringElement) {
            RingString rs = ((StringElement)element).getRingString();
            return new ZStringElement(new RingString<>(rs));
        }
        else {
            ArithmeticElement<ArithmeticInteger> e = ZRing.ring.cast(element);
            if (e == null) {
                return null;
            }
            else {
                return new ZStringElement(new RingString<>(e.getValue()));
            }
        }       
    }

    
    public String toString() {
        return "ZStringRing";
    }
    

    public String toVisualString() {
        return "Z-String";
    }

    public static RingString<ArithmeticInteger> parse(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<ArithmeticInteger> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            int f = Integer.parseInt(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new ArithmeticInteger(f));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

    public ArithmeticStringElement<ArithmeticInteger> parseString(String string) {
        try {
            return new ArithmeticStringElement<>(parse(TextUtils.unparenthesize(string)));
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public String getElementTypeName() {
        return "ZStringRing";
    }

    public int hashCode() {
        return basicHash;
    }

    
    private static final int basicHash = "ZStringRing".hashCode();

    private ZStringRing() { /* not allowed */ }
}
