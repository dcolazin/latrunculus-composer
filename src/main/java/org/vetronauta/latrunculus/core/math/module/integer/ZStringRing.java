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
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.LinkedList;

/**
 * The ring of ZString.
 *
 * @author Gérard Milmeister
 */
public final class ZStringRing extends ArithmeticStringRing<ArithmeticInteger> {

    public static final ZStringRing ring = new ZStringRing();

    public ZStringProperFreeModule getNullModule() {
        return ZStringProperFreeModule.nullModule;
    }
    
    public FreeModule<?,ArithmeticStringElement<ArithmeticInteger>> getFreeModule(int dimension) {
        return ZStringProperFreeModule.make(dimension);
    }
    
    public boolean equals(Object object) {
        return this == object;
    }

    public ArithmeticStringElement<ArithmeticInteger> parseString(String string) {
        try {
            return new ArithmeticStringElement<>(ArithmeticParsingUtils.parseString(this, TextUtils.unparenthesize(string)));
        }
        catch (Exception e) {
            return null;
        }
    }

    public int hashCode() {
        return basicHash;
    }

    
    private static final int basicHash = "ZStringRing".hashCode();

    private ZStringRing() {
        super(ZRing.ring);
    }
}
