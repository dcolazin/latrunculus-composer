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

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiModule;

/**
 * Free modules over ZStringRing.
 *
 * @author Gérard Milmeister
 */
public final class ZStringProperFreeModule extends ArithmeticStringMultiModule<ArithmeticInteger> {

    public static final ZStringProperFreeModule nullModule = new ZStringProperFreeModule(0);

    public static FreeModule<?, ArithmeticStringElement<ArithmeticInteger>> make(int dimension) {
        dimension = Math.max(dimension, 0);
        if (dimension == 0) {
            return nullModule;
        }
        else if (dimension == 1) {
            return ZStringRing.ring;
        }
        else {
            return new ZStringProperFreeModule(dimension);
        }
    }

    
    private ZStringProperFreeModule(int dimension) {
        super(ZStringRing.ring, dimension);
    }

}
