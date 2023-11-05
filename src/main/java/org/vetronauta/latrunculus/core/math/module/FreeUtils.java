/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.module;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeUtils {

    //TODO temp method...
    public static boolean isStringArithmetic(Module<?,?> module) {
        if (module instanceof VectorModule) {
            return isStringArithmetic(module.getRing());
        }
        return module instanceof StringRing && isArithmetic(((StringRing<?>) module).getFactorRing());
    }

    public static boolean isArithmetic(Module<?,?> module) {
        if (module instanceof VectorModule) {
            return isArithmetic(module.getRing());
        }
        return module instanceof ZRing ||
                module instanceof QRing ||
                module instanceof RRing ||
                module instanceof CRing ||
                module instanceof ZnRing;
    }

}
