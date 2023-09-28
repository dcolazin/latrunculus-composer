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

package org.vetronauta.latrunculus.math.arith.number;

import lombok.AllArgsConstructor;

/**
 * Wrapper int class to be used in a <code>ZString</code>.
 * @author vetronauta
 */
@AllArgsConstructor
public class ArithmeticInteger extends ArithmeticNumber<ArithmeticInteger> {

    private int value;

    @Override
    public int compareTo(ArithmeticInteger arithmeticInteger) {
        return value - arithmeticInteger.intValue();
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public float floatValue() {
        return intValue();
    }

    @Override
    public double doubleValue() {
        return intValue();
    }

    @Override
    public ArithmeticInteger deepCopy() {
        return new ArithmeticInteger(value);
    }
}
