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

package org.vetronauta.latrunculus.core.math.element.generic;

/**
 * @author vetronauta
 */
public interface Arithmetic {

    double abs();

    int intValue();

    double doubleValue();

    default long longValue() {
        return intValue();
    }

    default float floatValue() {
        return (float) doubleValue();
    }

    default byte byteValue() {
        return (byte) this.intValue();
    }

    default short shortValue() {
        return (short) this.intValue();
    }

}
