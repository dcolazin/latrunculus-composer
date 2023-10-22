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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.LinkedList;

/**
 * The ring of ZnString.
 * @see ZnStringElement
 * 
 * @author Gérard Milmeister
 */
public final class ZnStringRing extends StringRing<ArithmeticStringElement<ArithmeticModulus>> {
    
    public static ZnStringRing make(int modulus) {
        assert(modulus > 1);
        return new ZnStringRing(modulus);
    }
    

    public ArithmeticStringElement<ArithmeticModulus> getZero() {
        return new ArithmeticStringElement<ArithmeticModulus>(RingString.getZero());
    }

    
    public ArithmeticStringElement<ArithmeticModulus> getOne() {
        return new ArithmeticStringElement<ArithmeticModulus>(RingString.getOne());
    }

    
    public ZnStringProperFreeModule getNullModule() {
        return (ZnStringProperFreeModule)ZnStringProperFreeModule.make(0, modulus);
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
        return (element instanceof ZnStringElement &&
                ((ZnStringElement)element).getModulus() == modulus);
    }

    
    public FreeModule getFreeModule(int dimension) {
        return ZnStringProperFreeModule.make(dimension, modulus);
    }

    
    public ZnRing getFactorRing() {
        return ZnRing.make(getModulus());
    }

    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        else if (object instanceof ZnStringRing) {
            return ((ZnStringRing)object).getModulus() == modulus;
        }
        else {
            return false;
        }
    }

    
    public ZnStringElement cast(ModuleElement element) {
        if (element instanceof StringElement) {
            RingString rs = ((StringElement)element).getRingString();
            return new ZnStringElement(new RingString<>(rs), modulus);
        }
        else {
            ArithmeticElement<ArithmeticInteger> e = ZRing.ring.cast(element);
            if (e == null) {
                return null;
            }
            else {
                return new ZnStringElement(new RingString<>(e.getValue()), modulus);
            }
        }       
    }

    
    public int getModulus() {
        return modulus;
    }

    
    public String toString() {
        return "ZnStringRing("+getModulus()+")";
    }

    
    public String toVisualString() {
        return "Z_"+getModulus()+"-String";
    }

    public static RingString<ArithmeticModulus> parse(String string, int modulus) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<ArithmeticModulus> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            int f = Integer.parseInt(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new ArithmeticModulus(f, modulus));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }

    public ArithmeticStringElement<ArithmeticModulus> parseString(String string) {
        try {
            return new ArithmeticStringElement<>(parse(TextUtils.unparenthesize(string), getModulus()));
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getElementTypeName() {
        return "ZnStringRing";
    }

    public int hashCode() {
        return 37*basicHash + modulus;
    }

    
    private ZnStringRing(int modulus) {
        this.modulus = modulus;
    }

    
    private static final int basicHash = "ZStringRing".hashCode();

    private int modulus;
}
