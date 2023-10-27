package org.rubato.math.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.real.RRing;
import org.vetronauta.latrunculus.server.parse.ModuleElementParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PolynomialElementTest {

    private PolynomialRing<ArithmeticElement<ArithmeticInteger>> intPolRing;
    private PolynomialRing<ArithmeticElement<Real>> realPolRing;
    private PolynomialRing<ArithmeticElement<Rational>> ratPolRing;
    
    private PolynomialElement<ArithmeticElement<ArithmeticInteger>> i0, i1;
    private PolynomialElement<ArithmeticElement<Real>> r0, r1;
    private PolynomialElement<ArithmeticElement<Rational>> q0, q1;
    
    private PolynomialElement<ArithmeticElement<ArithmeticInteger>> ia, ib, ic, id;
    private PolynomialElement<ArithmeticElement<Real>> ra, rb, rc, rd;
    private PolynomialElement<ArithmeticElement<Rational>> qa, qb, qc, qd;

    @BeforeEach
    void setUp() {
        intPolRing  = PolynomialRing.make(ZRing.ring, "X");
        realPolRing = PolynomialRing.make(RRing.ring, "Y");
        ratPolRing  = PolynomialRing.make(QRing.ring, "Z");
        
        i0 = ModuleElementParser.parseElement(intPolRing, "0");
        i1 = ModuleElementParser.parseElement(intPolRing, "1");
        r0 = ModuleElementParser.parseElement(realPolRing, "0");
        r1 = ModuleElementParser.parseElement(realPolRing, "1");
        q0 = ModuleElementParser.parseElement(ratPolRing, "0");
        q1 = ModuleElementParser.parseElement(ratPolRing, "1");
        
        ia = ModuleElementParser.parseElement(intPolRing, "8*X^5+2*X^3+20");
        ib = ModuleElementParser.parseElement(intPolRing, "3*X^3+-8*X^2+-2");
        ic = ModuleElementParser.parseElement(intPolRing, "24*X^8+-64*X^7+-32*X^5+6*X^6+56*X^3+-160*X^2+-40");
        id = ModuleElementParser.parseElement(intPolRing, "10");

        ra = ModuleElementParser.parseElement(realPolRing, "8*Y^5+2*Y^3+20");
        rb = ModuleElementParser.parseElement(realPolRing, "3*Y^3+-8*Y^2+-2");
        rc = ModuleElementParser.parseElement(realPolRing, "24*Y^8+-64*Y^7+-32*Y^5+6*Y^6+56*Y^3+-160*Y^2+-40");
        rd = ModuleElementParser.parseElement(realPolRing, "2.3");

        qa = ModuleElementParser.parseElement(ratPolRing, "8*Z^5+2*Z^3+20");
        qb = ModuleElementParser.parseElement(ratPolRing, "3*Z^3+-8*Z^2+-2");
        qc = ModuleElementParser.parseElement(ratPolRing, "24*Z^8+-64*Z^7+-32*Z^5+6*Z^6+56*Z^3+-160*Z^2+-40");
        qd = ModuleElementParser.parseElement(ratPolRing, "3/2");
    }

    @Test
    void testIsOne() {
        assertFalse(i0.isOne());
        assertTrue(i1.isOne());
        assertFalse(r0.isOne());
        assertTrue(r1.isOne());
        assertFalse(q0.isOne());
        assertTrue(q1.isOne());
    }

    @Test
    void testIsZero() {
        assertTrue(i0.isZero());
        assertFalse(i1.isZero());
        assertTrue(r0.isZero());
        assertFalse(r1.isZero());
        assertTrue(q0.isZero());
        assertFalse(q1.isZero());
    }

    @Test
    void testProductRingElement() {
        try {
            assertEquals(ia.product(ib), ic);
            assertEquals(ia.product(i0), i0);
            assertEquals(i0.product(ia), i0);
            assertEquals(ia.product(i1), ia);
            assertEquals(i1.product(ia), ia);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }
        
        try {
            assertEquals(ra.product(rb), rc);
            assertEquals(ra.product(r0), r0);
            assertEquals(r0.product(ra), r0);
            assertEquals(ra.product(r1), ra);
            assertEquals(r1.product(ra), ra);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }

        try {
            assertEquals(qa.product(qb), qc);
            assertEquals(qa.product(q0), q0);
            assertEquals(q0.product(qa), q0);
            assertEquals(qa.product(q1), qa);
            assertEquals(q1.product(qa), qa);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testMultiplyRingElement() {
        try {
            ia.multiply(ib);
            assertEquals(ia, ic);
            ia.multiply(i0);
            assertEquals(ia, i0);
            ic.multiply(i1);
            assertEquals(ic, ic);
            i1.multiply(ic);
            assertEquals(i1, ic);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }

        try {
            ra.multiply(rb);
            assertEquals(ra, rc);

            ra.multiply(r0);
            assertEquals(ra, r0);
            rc.multiply(r1);
            assertEquals(rc, rc);
            r1.multiply(rc);
            assertEquals(r1, rc);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }

        try {
            qa.multiply(qb);
            assertEquals(qa, qc);
            qa.multiply(q0);
            assertEquals(qa, q0);
            qc.multiply(q1);
            assertEquals(qc, qc);
            q1.multiply(qc);
            assertEquals(q1, qc);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testIsInvertible() {
        assertTrue(i1.isInvertible());
        assertFalse(i0.isInvertible());
        assertFalse(ia.isInvertible());
        assertFalse(ib.isInvertible());
        assertFalse(ic.isInvertible());
        assertFalse(id.isInvertible());

        assertTrue(r1.isInvertible());
        assertFalse(r0.isInvertible());
        assertFalse(ra.isInvertible());
        assertFalse(rb.isInvertible());
        assertFalse(rc.isInvertible());
        assertTrue(rd.isInvertible());

        assertTrue(q1.isInvertible());
        assertFalse(q0.isInvertible());
        assertFalse(qa.isInvertible());
        assertFalse(qb.isInvertible());
        assertFalse(qc.isInvertible());
        assertTrue(qd.isInvertible());
    }

    @Test
    void testInverse() {
        assertEquals(i1.inverse(), i1);
        assertEquals(r1.inverse(), r1);
        assertEquals(q1.inverse(), q1);
        assertEquals(qd.inverse(), new PolynomialElement<>(ratPolRing, new ArithmeticElement<>(new Rational(2, 3))));
    }

    @Test
    void testInvert() {
        i1.invert();
        assertEquals(i1, new PolynomialElement<>(intPolRing, new ArithmeticElement<>(new ArithmeticInteger(1))));
        r1.invert();
        assertEquals(r1, new PolynomialElement<>(realPolRing, new ArithmeticElement<>(new Real(1))));
        q1.invert();
        assertEquals(q1, new PolynomialElement<>(ratPolRing, new ArithmeticElement<>(new Rational(1))));
        qd.invert();
        assertEquals(qd, new PolynomialElement<>(ratPolRing, new ArithmeticElement<>(new Rational(2, 3))));
    }

    @Test
    void testClone() {
        assertEquals(i0, i0.deepCopy());
        assertEquals(i1, i1.deepCopy());
        assertEquals(ia, ia.deepCopy());
        assertEquals(ib, ib.deepCopy());
        assertEquals(ic, ic.deepCopy());
        assertEquals(id, id.deepCopy());

        assertEquals(r0, r0.deepCopy());
        assertEquals(r1, r1.deepCopy());
        assertEquals(ra, ra.deepCopy());
        assertEquals(rb, rb.deepCopy());
        assertEquals(rc, rc.deepCopy());
        assertEquals(rd, rd.deepCopy());

        assertEquals(q0, q0.deepCopy());
        assertEquals(q1, q1.deepCopy());
        assertEquals(qa, qa.deepCopy());
        assertEquals(qb, qb.deepCopy());
        assertEquals(qc, qc.deepCopy());
        assertEquals(qd, qd.deepCopy());
    }

    //TODO this uses the algebra notion
    /*
    @Test
    void testScaled() {
        try {
            assertEquals(i0.scaled(new ZElement(12)), i0);
            assertEquals(i1.scaled(new ZElement(12)), new PolynomialElement(intPolRing, new ZElement(12)));
            assertEquals(ia.scaled(new ZElement(0)), i0);
            assertEquals(ia.scaled(new ZElement(1)), ia);
            assertEquals(ib.scaled(new ZElement(3)), new PolynomialElement(intPolRing, new ZElement(-6), new ZElement(0), new ZElement(-24), new ZElement(9)));
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }

        try {
            assertEquals(r0.scaled(new RElement(12)), r0);
            assertEquals(r1.scaled(new RElement(12)), new PolynomialElement(realPolRing, new RElement(12)));
            assertEquals(ra.scaled(new RElement(0)), r0);
            assertEquals(ra.scaled(new RElement(1)), ra);
            assertEquals(rb.scaled(new RElement(1.5)), new PolynomialElement(realPolRing, new RElement(-3), new RElement(0), new RElement(-12), new RElement(4.5)));
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }

        try {
            assertEquals(q0.scaled(new QElement(12)), q0);
            assertEquals(q1.scaled(new QElement(12)), new PolynomialElement(ratPolRing, new QElement(12)));
            assertEquals(qa.scaled(new QElement(0)), q0);
            assertEquals(qa.scaled(new QElement(1)), qa);
            assertEquals(qb.scaled(new QElement(3, 2)), new PolynomialElement(ratPolRing, new QElement(-3), new QElement(0), new QElement(-12), new QElement(9, 2)));
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testScale() {
        try {
            ib.scale(new ZElement(3));
            assertEquals(ib, new PolynomialElement(intPolRing, new ZElement(-6), new ZElement(0), new ZElement(-24), new ZElement(9)));
            ib.scale(new ZElement(0));
            assertEquals(ib, i0);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }

        try {
            rb.scale(new RElement(1.5));
            assertEquals(rb, new PolynomialElement(realPolRing, new RElement(-3), new RElement(0), new RElement(-12), new RElement(4.5)));
            rb.scale(new RElement(0));
            assertEquals(rb, r0);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }

        try {
            qb.scale(new QElement(3, 2));
            assertEquals(qb, new PolynomialElement(ratPolRing, new QElement(-3), new QElement(0), new QElement(-12), new QElement(9, 2)));
            qb.scale(new QElement(0));
            assertEquals(qb, q0);
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }
    }
    */

    @Test
    void testEvaluate() {
        try {
            assertEquals(i0.evaluate(new ArithmeticElement<>(new ArithmeticInteger(23))), new ArithmeticElement<>(new ArithmeticInteger(0)));
            assertEquals(i1.evaluate(new ArithmeticElement<>(new ArithmeticInteger(-12))), new ArithmeticElement<>(new ArithmeticInteger(1)));
            assertEquals(ib.evaluate(new ArithmeticElement<>(new ArithmeticInteger(-3))), new ArithmeticElement<>(new ArithmeticInteger(-155)));

            assertEquals(r0.evaluate(new ArithmeticElement<>(new Real(23))), new ArithmeticElement<>(new Real(0)));
            assertEquals(r1.evaluate(new ArithmeticElement<>(new Real(-12))), new ArithmeticElement<>(new Real(1)));
            assertEquals(rb.evaluate(new ArithmeticElement<>(new Real(-3.5))), new ArithmeticElement<>(new Real( -228.625)));

            assertEquals(q0.evaluate(new ArithmeticElement<>(new Rational(23))), new ArithmeticElement<>(new Rational(0)));
            assertEquals(q1.evaluate(new ArithmeticElement<>(new Rational(-12))), new ArithmeticElement<>(new Rational(1)));
            assertEquals(qb.evaluate(new ArithmeticElement<>(new Rational(1, 3))), new ArithmeticElement<>(new Rational(-25, 9)));
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetModule() {
        assertEquals(i0.getModule(), intPolRing);
        assertEquals(i1.getModule(), intPolRing);
        assertEquals(ia.getModule(), intPolRing);
        assertEquals(ib.getModule(), intPolRing);
        assertEquals(ic.getModule(), intPolRing);
        assertEquals(id.getModule(), intPolRing);
        assertNotSame(ia.getModule(), realPolRing);

        assertEquals(r0.getModule(), realPolRing);
        assertEquals(r1.getModule(), realPolRing);
        assertEquals(ra.getModule(), realPolRing);
        assertEquals(rb.getModule(), realPolRing);
        assertEquals(rc.getModule(), realPolRing);
        assertEquals(rd.getModule(), realPolRing);
        assertNotSame(ra.getModule(), ratPolRing);

        assertEquals(q0.getModule(), ratPolRing);
        assertEquals(q1.getModule(), ratPolRing);
        assertEquals(qa.getModule(), ratPolRing);
        assertEquals(qb.getModule(), ratPolRing);
        assertEquals(qc.getModule(), ratPolRing);
        assertEquals(qd.getModule(), ratPolRing);
        assertNotSame(qa.getModule(), intPolRing);
    }

    @Test
    void testGetPolynomialRing() {
        assertEquals(i0.getRing(), intPolRing);
        assertEquals(i1.getRing(), intPolRing);
        assertEquals(ia.getRing(), intPolRing);
        assertEquals(ib.getRing(), intPolRing);
        assertEquals(ic.getRing(), intPolRing);
        assertEquals(id.getRing(), intPolRing);
        assertNotSame(ia.getRing(), realPolRing);

        assertEquals(r0.getRing(), realPolRing);
        assertEquals(r1.getRing(), realPolRing);
        assertEquals(ra.getRing(), realPolRing);
        assertEquals(rb.getRing(), realPolRing);
        assertEquals(rc.getRing(), realPolRing);
        assertEquals(rd.getRing(), realPolRing);
        assertNotSame(ra.getRing(), ratPolRing);

        assertEquals(q0.getRing(), ratPolRing);
        assertEquals(q1.getRing(), ratPolRing);
        assertEquals(qa.getRing(), ratPolRing);
        assertEquals(qb.getRing(), ratPolRing);
        assertEquals(qc.getRing(), ratPolRing);
        assertEquals(qd.getRing(), ratPolRing);
        assertNotSame(qa.getRing(), intPolRing);
    }

    @Test
    void testGetCoefficient() {
        assertEquals(ia.getCoefficient(3), new ArithmeticElement<>(new ArithmeticInteger(2)));
        assertEquals(ic.getCoefficient(4), new ArithmeticElement<>(new ArithmeticInteger(0)));
        
        assertEquals(ra.getCoefficient(3), new ArithmeticElement<>(new Real(2)));
        assertEquals(rc.getCoefficient(4), new ArithmeticElement<>(new Real(0)));

        assertEquals(qa.getCoefficient(3), new ArithmeticElement<>(new Rational(2)));
        assertEquals(qc.getCoefficient(4), new ArithmeticElement<>(new Rational(0)));
    }

    @Test
    void testGetLeadingCoefficient() {
        assertEquals(i0.getLeadingCoefficient(), new ArithmeticElement<>(new ArithmeticInteger(0)));
        assertEquals(i1.getLeadingCoefficient(), new ArithmeticElement<>(new ArithmeticInteger(1)));
        assertEquals(ia.getLeadingCoefficient(), new ArithmeticElement<>(new ArithmeticInteger(8)));
        assertEquals(ib.getLeadingCoefficient(), new ArithmeticElement<>(new ArithmeticInteger(3)));
        assertEquals(ic.getLeadingCoefficient(), new ArithmeticElement<>(new ArithmeticInteger(24)));
        assertEquals(id.getLeadingCoefficient(), new ArithmeticElement<>(new ArithmeticInteger(10)));

        assertEquals(r0.getLeadingCoefficient(), new ArithmeticElement<>(new Real(0)));
        assertEquals(r1.getLeadingCoefficient(), new ArithmeticElement<>(new Real(1)));
        assertEquals(ra.getLeadingCoefficient(), new ArithmeticElement<>(new Real(8)));
        assertEquals(rb.getLeadingCoefficient(), new ArithmeticElement<>(new Real(3)));
        assertEquals(rc.getLeadingCoefficient(), new ArithmeticElement<>(new Real(24)));
        assertEquals(rd.getLeadingCoefficient(), new ArithmeticElement<>(new Real(2.3)));

        assertEquals(q0.getLeadingCoefficient(), new ArithmeticElement<>(new Rational(0)));
        assertEquals(q1.getLeadingCoefficient(), new ArithmeticElement<>(new Rational(1)));
        assertEquals(qa.getLeadingCoefficient(), new ArithmeticElement<>(new Rational(8)));
        assertEquals(qb.getLeadingCoefficient(), new ArithmeticElement<>(new Rational(3)));
        assertEquals(qc.getLeadingCoefficient(), new ArithmeticElement<>(new Rational(24)));
        assertEquals(qd.getLeadingCoefficient(), new ArithmeticElement<>(new Rational(3, 2)));
    }

    @Test
    void testGetDegree() {
        assertEquals(Integer.MIN_VALUE, i0.getDegree());
        assertEquals(0, i1.getDegree());
        assertEquals(5, ia.getDegree());
        assertEquals(3, ib.getDegree());
        assertEquals(8, ic.getDegree());
        assertEquals(0, id.getDegree());

        assertEquals(Integer.MIN_VALUE, r0.getDegree());
        assertEquals(0, r1.getDegree());
        assertEquals(5, ra.getDegree());
        assertEquals(3, rb.getDegree());
        assertEquals(8, rc.getDegree());
        assertEquals(0, rd.getDegree());

        assertEquals(Integer.MIN_VALUE, q0.getDegree());
        assertEquals(0, q1.getDegree());
        assertEquals(5, qa.getDegree());
        assertEquals(3, qb.getDegree());
        assertEquals(8, qc.getDegree());
        assertEquals(0, qd.getDegree());
    }

    @Test
    void testCast() {
        assertEquals(intPolRing.cast(ia), ia);
        assertEquals(realPolRing.cast(ia), ra);
        assertEquals(ratPolRing.cast(ia), qa);

        assertEquals(intPolRing.cast(ra), ia);
        assertEquals(realPolRing.cast(ra), ra);
        assertEquals(ratPolRing.cast(ra), qa);

        assertEquals(intPolRing.cast(qa), ia);
        assertEquals(realPolRing.cast(qa), ra);
        assertEquals(ratPolRing.cast(qa), qa);
    }

    @Test
    void testPower() {
        try {
            assertEquals(i0.power(10), i0);
            assertEquals(i1.power(10), i1);
            assertEquals(ia.power(3), ia.product(ia).product(ia));
            assertEquals(id.power(3), id.product(id).product(id));
            
            assertEquals(r0.power(10), r0);
            assertEquals(r1.power(10), r1);
            assertEquals(ra.power(3), ra.product(ra).product(ra));
            assertEquals(rd.power(3), rd.product(rd).product(rd));

            assertEquals(q0.power(10), q0);
            assertEquals(q1.power(10), q1);
            assertEquals(qa.power(3), qa.product(qa).product(qa));
            assertEquals(qd.power(3), qd.product(qd).product(qd));
        }
        catch (DomainException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetRing() {
        assertEquals(i0.getRing(), intPolRing);
        assertEquals(i1.getRing(), intPolRing);
        assertEquals(ia.getRing(), intPolRing);
        assertEquals(ib.getRing(), intPolRing);
        assertEquals(ic.getRing(), intPolRing);
        assertEquals(id.getRing(), intPolRing);
        assertNotSame(ia.getRing(), realPolRing);

        assertEquals(r0.getRing(), realPolRing);
        assertEquals(r1.getRing(), realPolRing);
        assertEquals(ra.getRing(), realPolRing);
        assertEquals(rb.getRing(), realPolRing);
        assertEquals(rc.getRing(), realPolRing);
        assertEquals(rd.getRing(), realPolRing);
        assertNotSame(ra.getRing(), ratPolRing);

        assertEquals(q0.getRing(), ratPolRing);
        assertEquals(q1.getRing(), ratPolRing);
        assertEquals(qa.getRing(), ratPolRing);
        assertEquals(qb.getRing(), ratPolRing);
        assertEquals(qc.getRing(), ratPolRing);
        assertEquals(qd.getRing(), ratPolRing);
        assertNotSame(qa.getRing(), intPolRing);
    }

}
