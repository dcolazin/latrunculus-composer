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

package org.vetronauta.latrunculus.core.math.arith;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.math.arith.Complex;

/**
 * @author vetronauta
 */
class ComplexTest {

    @Test
    void constructorTest() {
        Complex c = new Complex();
        assertComponentsAreEquals(0.0, 0.0, c);

        c = new Complex(1.2, 3.4);
        assertComponentsAreEquals(1.2, 3.4, c);

        c = new Complex(c);
        assertComponentsAreEquals(1.2, 3.4, c);

        c = new Complex(1.2);
        assertComponentsAreEquals(1.2, 0.0, c);

        c = new Complex(1);
        assertComponentsAreEquals(1.0, 0.0, c);
    }

    private static void assertComponentsAreEquals(double a, double b, Complex c) {
        Assertions.assertEquals(a, c.getReal());
        Assertions.assertEquals(b, c.getImag());
    }
}
