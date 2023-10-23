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
import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringMultiModule;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.repository.ArithmeticRingRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Free modules over ZnStringRing.
 *
 * @author Gérard Milmeister
 */
public final class ZnStringProperFreeModule extends ArithmeticStringMultiModule<Modulus> {

    public static FreeModule<?, ArithmeticStringElement<Modulus>> make(int dimension, int modulus) {
        dimension = Math.max(dimension, 0);
        if (dimension == 1) {
            return ZnStringRing.make(modulus);
        }
        else {
            return new ZnStringProperFreeModule(dimension, modulus);
        }
    }
    
    private ZnStringProperFreeModule(int dimension, int modulus) {
        super(ZnStringRing.make(modulus), dimension);
    }

}
