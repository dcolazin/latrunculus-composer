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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticDouble;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.LinkedList;

/**
 * The ring of RString.
 * @see RStringElement
 * 
 * @author Gérard Milmeister
 */
public final class RStringRing extends StringRing<RStringElement> {

    public static final RStringRing ring = new RStringRing();

    public RStringElement getZero() {
        return new RStringElement(RingString.getZero());
    }

    
    public RStringElement getOne() {
        return new RStringElement(RingString.getOne());
    }

    
    public RStringProperFreeModule getNullModule() {
        return RStringProperFreeModule.nullModule;
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
        return (element instanceof RStringElement);
    }

    
    public FreeModule getFreeModule(int dimension) {
        return RStringProperFreeModule.make(dimension);
    }

    
    public Ring getFactorRing() {
        return RRing.ring;
    }

    
    public boolean equals(Object object) {
        return this == object;
    }

    
    public RStringElement cast(ModuleElement element) {
        if (element instanceof RStringElement) {
            return (RStringElement) element;
        }
        else if (element instanceof StringElement) {
            RingString rs = ((StringElement)element).getRingString();
            return new RStringElement(new RingString<>(rs));
        }
        else {
            RElement e = RRing.ring.cast(element);
            if (e == null) {
                return null;
            }
            else {
                return new RStringElement(new RingString<>(e.getValue()));
            }
        }       
    }

    
    public String toString() {
        return "RStringRing";
    }
    
    
    public String toVisualString() {
        return "R-String";
    }

    public static RingString<ArithmeticDouble> parse(String string) {
        String[] terms = TextUtils.split(string.trim(), '+');
        if (terms.length == 0) {
            return RingString.getOne();
        }

        LinkedList<String> words = new LinkedList<>();
        LinkedList<ArithmeticDouble> factors = new LinkedList<>();
        for (int i = 0; i < terms.length; i++) {
            String[] term = TextUtils.split(terms[i].trim(), '*');
            if (term.length < 2) {
                throw new NumberFormatException();
            }
            double f = Double.parseDouble(term[0]);
            String w = TextUtils.unquote(term[1]);
            factors.add(new ArithmeticDouble(f));
            words.add(w);
        }

        return new RingString<>(words, factors);
    }
    
    public RStringElement parseString(String string) {
        try {
            return new RStringElement(parse(TextUtils.unparenthesize(string)));
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public String getElementTypeName() {
        return "RStringRing";
    }

    public int hashCode() {
        return basicHash;
    }

    
    private static final int basicHash = "RStringRing".hashCode();

    private RStringRing() { /* not allowed */ }
}
