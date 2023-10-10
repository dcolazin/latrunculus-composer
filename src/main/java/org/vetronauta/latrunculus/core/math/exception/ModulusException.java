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

package org.vetronauta.latrunculus.core.math.exception;

import org.vetronauta.latrunculus.core.exception.LatrunculusRuntimeException;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;

/**
 * @author vetronauta
 */
public class ModulusException extends LatrunculusRuntimeException {

    private static final String ERROR_MESSAGE = "Trying to perform operation between %s and %s";

    public ModulusException(ArithmeticModulus a, ArithmeticModulus b) {
        super(String.format(ERROR_MESSAGE, a, b));
    }

}
