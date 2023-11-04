/*
 * Copyright (C) 2001 Gérard Milmeister
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

package org.rubato.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;
import org.vetronauta.latrunculus.core.math.arith.number.RationalWrapper;
import org.vetronauta.latrunculus.core.exception.InverseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class RationalWrapperTest {
    
    private RationalWrapper r1;
    private RationalWrapper r2;
    private RationalWrapper r;

    @BeforeEach
    void setUp() {
        r1 = new RationalWrapper(2*3*5, 7*11);
        r2 = new RationalWrapper(-3*7, 5*11*13);
    }

    @Test
    void testRationalEquals() {
        RationalWrapper x = new RationalWrapper(-15, 35);
        RationalWrapper y = new RationalWrapper(6, -14);
        assertTrue(x.equals(y));
    } 

    @Test
    void testRationalSum() {
        r = r1.sum(r2);
        assertEquals(new RationalWrapper(1803, 5005), r);
        r = r1.sum(5);
        assertEquals(new RationalWrapper(415, 77), r);
    }

    @Test
    void testRationalAdd() {
        r = new RationalWrapper(r1);
        r = r.sum(r2);
        assertEquals(new RationalWrapper(1803, 5005), r);
        r = new RationalWrapper(r1);
        r = r.sum(5);
        assertEquals(new RationalWrapper(415, 77), r);
    }

    @Test
    void testRationalDifference() {
        r = r1.difference(r2);
        assertEquals(new RationalWrapper(2097, 5005), r);
        r = r1.difference(5);
        assertEquals(new RationalWrapper(-355, 77), r);
    }

    @Test
    void testRationalSubtract() {
        r = new RationalWrapper(r1);
        r = r.difference(r2);
        assertEquals(new RationalWrapper(2097, 5005), r);
        r = new RationalWrapper(r1);
        r = r.difference(5);
        assertEquals(new RationalWrapper(-355, 77), r);
    }

    @Test
    void testRationalProduct() {
        r = r1.product(r2);
        assertEquals(new RationalWrapper(-18, 1573), r);
        r = r1.product(-5);
        assertEquals(new RationalWrapper(-150, 77), r);
    }

    @Test
    void testRationalMultiply() {
        r = new RationalWrapper(r1);
        r = r.product(r2);
        assertEquals(new RationalWrapper(-18, 1573), r);
        r = new RationalWrapper(r1);
        r = r.product(-5);
        assertEquals(new RationalWrapper(-150, 77), r);
    }

    @Test
    void testRationalQuotient() {
        r = r1.quotient(r2);
        assertEquals(new RationalWrapper(-650, 49), r);
        r = r1.quotient(-5);
        assertEquals(new RationalWrapper(-6, 77), r);
        try {
            r = r1.quotient(0);
            fail("Should raise an ArithmeticException");
        } catch (ArithmeticException e) { /* continue */ }
    }

    @Test
    void testRationalDivide() {
        r = new RationalWrapper(r1);
        r = r.quotient(r2);
        assertEquals(new RationalWrapper(-650, 49), r);
        r = new RationalWrapper(r1);
        r = r.quotient(-5);
        assertEquals(new RationalWrapper(-6, 77), r);
        try {
            r = new RationalWrapper(r1);
            r = r.quotient(0);
            fail("Should raise an ArithmeticException");
        } catch (ArithmeticException e) { /* continue */ }
    }

    @Test
    void testRationalInverse() {
        r = r1.inverse();
        assertEquals(new RationalWrapper(7*11, 2*3*5), r);
        r = r2.inverse();
        assertEquals(new RationalWrapper(-5*11*13, 3*7), r);
        r = new RationalWrapper(0, 5);
        Assertions.assertThrows(InverseException.class, () -> r.inverse());
    }

    @Test
    void testRationalInvert() {
        r = new RationalWrapper(r1);
        r = r.inverse();
        assertEquals(new RationalWrapper(7*11, 2*3*5), r);
        r = new RationalWrapper(r2);
        r = r.inverse();
        assertEquals(new RationalWrapper(-5*11*13, 3*7), r);
        r = new RationalWrapper(0, 5);
        Assertions.assertThrows(InverseException.class, () -> r.inverse());
    }

    @Test
    void testRationalCompareTo() {
        int res;
        res = r1.compareTo(r2);
        assertTrue(res > 0);
        res = r2.compareTo(r1);
        assertTrue(res < 0);
        res = r1.compareTo(r1);
        assertEquals(0, res);
        res = r2.compareTo(r2);
        assertEquals(0, res);
        assertEquals(new RationalWrapper(0), RationalWrapper.getZero());
        assertEquals(new RationalWrapper(1), RationalWrapper.getOne());
    }

    @Test
    void testRationalQuant() {
        double d1;
        d1 = 47.125;
        assertEquals(new RationalWrapper(377, 8), new RationalWrapper(d1, 16));
        d1 = 47.324;
        assertEquals(new RationalWrapper(379, 8), new RationalWrapper(d1, 8));
        d1 = -47.125;
        assertEquals(new RationalWrapper(-377, 8), new RationalWrapper(d1, 16));
        d1 = -47.324;
        assertEquals(new RationalWrapper(-379, 8), new RationalWrapper(d1, 8));
    }

    @Test
    void testRationalToString() {
        String s = ArithmeticParsingUtils.parseRational("60/-154").toString();
        assertEquals("-30/77", s);
    }

}
