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
import org.vetronauta.latrunculus.core.math.arith.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringRing;

/**
 * The ring of RString.
 *
 * @author Gérard Milmeister
 */
public final class RStringRing extends ArithmeticStringRing<Real> {

    public static final RStringRing ring = new RStringRing();

    public RStringProperFreeModule getNullModule() {
        return RStringProperFreeModule.nullModule;
    }
    
    public FreeModule getFreeModule(int dimension) {
        return RStringProperFreeModule.make(dimension);
    }
    
    public boolean equals(Object object) {
        return this == object;
    }
    
    public ArithmeticStringElement<Real> parseString(String string) {
        try {
            return new ArithmeticStringElement<>(ArithmeticParsingUtils.parseString(this, TextUtils.unparenthesize(string)));
        } catch (Exception e) {
            return null;
        }
    }

    public int hashCode() {
        return basicHash;
    }

    
    private static final int basicHash = "RStringRing".hashCode();

    private RStringRing() {
        super(RRing.ring);
    }
}
