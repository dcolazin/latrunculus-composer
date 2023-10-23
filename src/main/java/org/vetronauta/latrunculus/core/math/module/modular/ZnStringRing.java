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
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;

import java.util.LinkedList;

/**
 * The ring of ZnString.
 *
 * @author Gérard Milmeister
 */
public final class ZnStringRing extends ArithmeticStringRing<Modulus> {
    
    public static ZnStringRing make(int modulus) {
        assert(modulus > 1);
        return new ZnStringRing(modulus);
    }
    
    public ZnStringProperFreeModule getNullModule() {
        return (ZnStringProperFreeModule)ZnStringProperFreeModule.make(0, modulus);
    }

    public FreeModule getFreeModule(int dimension) {
        return ZnStringProperFreeModule.make(dimension, modulus);
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
    
    public int getModulus() {
        return modulus;
    }

    public ArithmeticStringElement<Modulus> parseString(String string) {
        try {
            return new ArithmeticStringElement<>(ArithmeticParsingUtils.parseString(this, TextUtils.unparenthesize(string)));
        } catch (Exception e) {
            return null;
        }
    }

    public int hashCode() {
        return 37*basicHash + modulus;
    }

    
    private ZnStringRing(int modulus) {
        super(ZnRing.make(modulus));
        this.modulus = modulus;
    }

    
    private static final int basicHash = "ZStringRing".hashCode();

    private int modulus;
}
