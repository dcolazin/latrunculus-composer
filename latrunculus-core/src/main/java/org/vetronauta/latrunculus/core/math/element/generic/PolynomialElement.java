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

package org.vetronauta.latrunculus.core.math.element.generic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.PolynomialRing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Elements in a ring of polynomials.
 * @see PolynomialRing
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PolynomialElement<R extends RingElement<R>> extends RingElement<PolynomialElement<R>> {

    private PolynomialRing<R> ring;
    private List<R> coefficients;

    /**
     * Constructs a polynomial in a specified ring with given coefficents.
     * The array of <code>coefficients</code> contains the coefficient of power <code>n</code>
     * at index <code>n</code>.
     * @param ring the ring of polynomials
     * @param coefficient element of the coefficient ring
     */
    public PolynomialElement(PolynomialRing<R> ring, R coefficient) {
        this(ring, new ArrayList<>());
        coefficients.add(coefficient);
    }
    
    /**
     * Constructs a polynomial with given coefficents.
     * The array of <code>coefficients</code> contains the coefficient of power <code>n</code>
     * at index <code>n</code>.
     * The polynomial ring is inferred from the coefficients, which must all
     * be elements of the the same ring.
     * @param coefficients elements of the coefficient ring
     */
    public PolynomialElement(String indeterminate, List<R> coefficients) {
        this(PolynomialRing.make(coefficients.get(0).getRing(), indeterminate), coefficients);
    }

    public PolynomialElement(PolynomialRing<R> ring, List<R> coefficients) {
        this.ring = ring;
        this.coefficients = coefficients;
        normalize();
    }

    @Override
    public boolean isOne() {
        return (coefficients.size() == 1 && coefficients.get(0).isOne());
    }  
    
    @Override
    public boolean isZero() {
        return (coefficients.size() == 1 && coefficients.get(0).isZero());
    }

    //TODO better operation routines

    @Override
    public PolynomialElement<R> sum(PolynomialElement<R> element) {
        List<R> otherCoefficients = element.getCoefficients();
        int len = getCoefficients().size();
        int otherLen = otherCoefficients.size();
        int newLen = Math.max(len, otherLen);
        List<R> newCoefficients = new ArrayList<>(newLen);
        for (int i = 0; i < len; i++) {
            newCoefficients.add(coefficients.get(i).deepCopy());
        }
        if (newLen > len) {
            for (int i = len; i < newLen; i++) {
                newCoefficients.add(otherCoefficients.get(i).deepCopy());
            }
        }
        for (int i = 0; i < Math.min(len, otherLen); i++) {
            newCoefficients.get(i).add(otherCoefficients.get(i));
        }
        return new PolynomialElement<>(ring, newCoefficients);
    }

    @Override
    public void add(PolynomialElement<R> element) throws DomainException {
        coefficients = this.sum(element).coefficients;
    }

    @Override
    public PolynomialElement<R> difference(PolynomialElement<R> element) throws DomainException {
        List<R> otherCoefficients = element.getCoefficients();
        int len = getCoefficients().size();
        int otherLen = otherCoefficients.size();
        int newLen = Math.max(len, otherLen);
        List<R> newCoefficients = new ArrayList<>(newLen);
        for (int i = 0; i < len; i++) {
            newCoefficients.add(coefficients.get(i).deepCopy());
        }
        if (newLen > len) {
            for (int i = len; i < newLen; i++) {
                newCoefficients.add(otherCoefficients.get(i).deepCopy().negated());
            }
        }
        for (int i = 0; i < Math.min(len, otherLen); i++) {
            newCoefficients.get(i).subtract(otherCoefficients.get(i));
        }
        return new PolynomialElement<>(ring, newCoefficients);
    }

    @Override
    public void subtract(PolynomialElement<R> element) throws DomainException {
        coefficients = this.difference(element).coefficients;
    }

    @Override
    public PolynomialElement<R> negated() {
        List<R> newCoefficients = new ArrayList<>(coefficients.size());
        for (R coefficient : coefficients) {
            newCoefficients.add(coefficient.negated());
        }
        return new PolynomialElement<>(ring, newCoefficients);
    }

    @Override
    public void negate() {
        coefficients = this.negated().coefficients;
    }

    //TODO for scaled and scale, the element was intended to be in R, but this is a notion of algebra...
    @Override
    public PolynomialElement<R> scaled(PolynomialElement<R> element) throws DomainException {
        return product(element);
    }

    //TODO for scaled and scale, the element was intended to be in R, but this is a notion of algebra...
    @Override
    public void scale(PolynomialElement<R> element) throws DomainException {
        multiply(element);
    }

    @Override
    public PolynomialElement<R> product(PolynomialElement<R> element) throws DomainException {
        if (this.isZero()) {
            return this;
        }
        if (element.isZero()) {
            return element;
        }
        int newDegree = getDegree() + element.getDegree() + 1;
        List<R> newCoefficients = new ArrayList<>(newDegree);
        for (int i = 0; i < newDegree; i++) {
            newCoefficients.add(ring.getCoefficientRing().getZero());
        }
        for (int i = 0; i <= getDegree(); i++) {
            for (int j = 0; j <= element.getDegree(); j++) {
                R p = getCoefficient(i).product(element.getCoefficient(j));
                newCoefficients.get(i+j).add(p);
            }
        }
        return new PolynomialElement<>(ring, newCoefficients);
    }

    @Override
    public void multiply(PolynomialElement<R> element) throws DomainException {
        coefficients = this.product(element).coefficients;
    }

    @Override
    public boolean isInvertible() {
        return getDegree() == 0 && getCoefficient(0).isInvertible();
    }
    
    @Override
    public PolynomialElement<R> inverse() {
        if (isInvertible()) {
            return new PolynomialElement<>(getRing(), getCoefficient(0).inverse());
        }
        throw new InverseException("Inverse of "+this+" does not exist");
    }

    @Override
    public void invert() {
        coefficients = this.inverse().coefficients;
    }

    @Override
    public PolynomialElement<R> quotient(PolynomialElement<R> element) throws DomainException, DivisionException {
        PolynomialElement[] remainder = new PolynomialElement[1];
        PolynomialElement<R> q = quotientRemainder(element, remainder);
        if (remainder[0].getDegree() <= 0) {
            return q;
        }
        throw new DivisionException(this, element);
    }

    @Override
    public void divide(PolynomialElement<R> element) throws DomainException, DivisionException {
        coefficients = this.quotient(element).coefficients;
    }

    @Override
    public boolean divides(RingElement<?> element) {
        if (!ring.hasElement(element)) {
            return false;
        }
        PolynomialElement<R> pol = (PolynomialElement<R>)element;
        try {
            PolynomialElement<R> remainder = pol.remainder(this);
            return remainder.getDegree() <= 0;
        }
        catch (DomainException | DivisionException  e) {
            return false;
        }
    }

    public PolynomialElement<R> quotientRemainder(PolynomialElement<R> element, PolynomialElement[] remainder)
            throws DomainException, DivisionException {
        if (element.isZero()) {
            throw new DivisionException(this, element);
        }
        if (getDegree() < element.getDegree()) {
            remainder[0] = this;
            return getRing().getZero();
        }
        try {
            List<R> p = new ArrayList<>(coefficients.size());
            int pdeg = coefficients.size()-1;
            for (int i = 0; i <= pdeg; i++) {
                p.add(coefficients.get(i));
            }

            List<R> q = element.coefficients;
            int qdeg = q.size() - 1;


            LinkedList<R> quotient = new LinkedList<>();

            int pi = pdeg;
            while (pi >= qdeg) {
                R q1 = p.get(pi).quotient(q.get(qdeg));
                quotient.addFirst(q1);
                for (int i = pi, j = qdeg; j >= 0; i--, j--) {
                    p.set(i, p.get(i).difference(q1.product(q.get(j))));
                }
                pi--;
            }
            remainder[0] = new PolynomialElement<>(getRing(), p);
            return new PolynomialElement<>(getRing(), quotient);
        } catch (DivisionException e) {
            throw new DivisionException(this, element);
        }
    }

    //TODO proper dto
    public PolynomialElement<R> remainder(PolynomialElement<R> element) throws DomainException, DivisionException {
        PolynomialElement[] remainder = new PolynomialElement[1];
        quotientRemainder(element, remainder);
        return remainder[0];
    }

    
    /**
     * Returns the greatest common divisior
     * of <code>this</code> and <code>element</code>.
     */
    public PolynomialElement<R> gcd(PolynomialElement<R> element) throws DomainException, DivisionException {
        PolynomialElement<R> r0 = this;
        PolynomialElement<R> r1 = element;
        PolynomialElement<R> remainder;

        while (!r1.isZero()) {
            remainder = r0.remainder(r1);
            r0 = r1;
            r1 = remainder;
        }
        R lc = r0.coefficients.get(r0.coefficients.size()-1);
        if (!lc.isOne()) {
            for (int i = 0; i < r0.coefficients.size(); i++) {
                r0.coefficients.set(i,  r0.coefficients.get(i).quotient(lc));
            }
        }
        return r0;
    }

    //TODO proper dto
    /**
     * The extended Euclidean algorithm.
     * 
     * @param res is an array of polynomials that will contain two values such that:
     *            res[0]*x + res[1]*y = gcd(x,y)
     * @return the greatest common divisor of x and y, always non negative
     */
    public PolynomialElement<R> extendedGcd(PolynomialElement<R> y, PolynomialElement[] res)
            throws DomainException, DivisionException {
        PolynomialElement<R> x = this;
        PolynomialElement<R> q, r, s, t;
        PolynomialElement<R> r0 = x;
        PolynomialElement<R> s0 = getRing().getOne();
        PolynomialElement<R> t0 = getRing().getZero();
        PolynomialElement<R> r1 = y;
        PolynomialElement<R> s1 = getRing().getZero();
        PolynomialElement<R> t1 = getRing().getOne();

        PolynomialElement[] remainder = new PolynomialElement[1];
        while (!r1.isZero()) {
            q = r0.quotientRemainder(r1, remainder);
            r = r1;
            s = s1;
            t = t1;
            r1 = r0.difference(q.product(r1));
            s1 = s0.difference(q.product(s1));
            t1 = t0.difference(q.product(t1));
            r0 = r;
            s0 = s;
            t0 = t;
        }

        R lc = r0.coefficients.get(r0.coefficients.size()-1);
        if (!lc.isOne()) {
            for (int i = 0; i < r0.coefficients.size(); i++) {
                r0.coefficients.set(i, r0.coefficients.get(i).quotient(lc));
            }
            for (int i = 0; i < s0.coefficients.size(); i++) {
                s0.coefficients.set(i, s0.coefficients.get(i).quotient(lc));
            }
            for (int i = 0; i < t0.coefficients.size(); i++) {
                t0.coefficients.set(i, t0.coefficients.get(i).quotient(lc));
            }
        }

        res[0] = s0;
        res[1] = t0;
        return r0;
    }

    public R evaluate(R element) throws DomainException {
        if (getDegree() <= 0) {
            return getCoefficient(0);
        }
        R result = getLeadingCoefficient().deepCopy();
        for (int i = getDegree()-1; i >= 0; i--) {
            result.multiply(element);
            result.add(getCoefficient(i));
        }
        return result;
    }
    
    @Override
    public PolynomialRing<R> getModule() {
        return ring;
    }

    @Override
    public PolynomialRing<R> getRing() {
        return ring;
    }

    
    public List<R> getCoefficients() {
        return coefficients;
    }

    public R getCoefficient(int power) {
        return coefficients.get(power);
    }

    public R getLeadingCoefficient() {
        return coefficients.get(coefficients.size() - 1);
    }

    public int getDegree() {
        return isZero() ? Integer.MIN_VALUE : coefficients.size() - 1;
    }

    public Ring<R> getCoefficientRing() {
        return getRing().getCoefficientRing();
    }
    
    public String getIndeterminate() {
        return getRing().getIndeterminate();
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof PolynomialElement) {
            PolynomialElement<?> p = (PolynomialElement<?>)object;
            if (getDegree() != p.getDegree()) {
                return false;
            }
            for (int i = 0; i <= getDegree(); i++) {
                if (!getCoefficient(i).equals(p.getCoefficient(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected int sameClassCompare(PolynomialElement<R> other) {
        int d0 = coefficients.size()-1;
        int d1 = other.coefficients.size()-1;
        if (getRing().equals(other.getRing())) {
            for (int i = 0; i <= Math.min(d0, d1); i++) {
                int c = getCoefficient(i).compareTo(other.getCoefficient(i));
                if (c != 0) {
                    return c;
                }
            }
            return d0-d1;
        }
        return getRing().compareTo(other.getRing());
    }

    @Override
    public PolynomialElement<R> deepCopy() {
        List<R> newCoefficients = new ArrayList<>(coefficients.size());
        for (R coefficient : coefficients) {
            newCoefficients.add(coefficient.deepCopy());
        }
        return new PolynomialElement<>(ring, newCoefficients);
    }

    @Override
    public String toString() {
        return "Polynomial["+coefficients+"]";
    }

    public String getElementTypeName() {
        return "PolynomialElement";
    }

    @Override
    public int hashCode() {
        int hashCode = basicHash;
        for (R coefficient : coefficients) {
            hashCode ^= coefficient.hashCode();
        }
        return hashCode;
    }

    private void normalize() {
        if (getDegree() <= 0 || !getLeadingCoefficient().isZero()) {
            return;
        }
        int i = getDegree();
        while (getCoefficient(i).isZero() && i > 0) {
            i--;
        }
        List<R> newCoefficients = new ArrayList<>(i+1);
        for (int j = 0; j <= i; j++) {
            newCoefficients.add(getCoefficient(j));
        }
        coefficients = newCoefficients;
    }
    
    private static final int basicHash = "PolynomialElement".hashCode();
}
