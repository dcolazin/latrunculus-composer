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

package org.vetronauta.latrunculus.core.math.module.polynomial;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.exception.DivisionException;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.InverseException;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Elements in a ring of polynomials.
 * @see PolynomialRing
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PolynomialElement<R extends RingElement<R>> extends RingElement<PolynomialElement<R>> implements PolynomialFreeElement<PolynomialElement<R>,R> {

    private PolynomialRing<R> ring;
    private List<R> coefficients;

    /**
     * Constructs a polynomial in a specified ring with given coefficents.
     * The array of <code>coefficients</code> contains the coefficient of power <code>n</code>
     * at index <code>n</code>.
     * @param ring the ring of polynomials
     * @param coefficients elements of the coefficient ring
     */
    public PolynomialElement(PolynomialRing<R> ring, R... coefficients) { //TODO use only list constructor
        this(ring, Arrays.stream(coefficients).collect(Collectors.toList()));
    }
    
    /**
     * Constructs a polynomial with given coefficents.
     * The array of <code>coefficients</code> contains the coefficient of power <code>n</code>
     * at index <code>n</code>.
     * The polynomial ring is inferred from the coefficients, which must all
     * be elements of the the same ring.
     * @param coefficients elements of the coefficient ring
     */
    public PolynomialElement(String indeterminate, R... coefficients) { //TODO use only list constructor
        this(PolynomialRing.make(coefficients[0].getRing(), indeterminate), coefficients);
    }

    public PolynomialElement(PolynomialRing<R> ring, List<R> coefficients) {
        assert(CollectionUtils.size(coefficients) > 0);
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
        List<R> otherCoefficients = element.getCoefficients();
        int len = getCoefficients().size();
        int otherLen = otherCoefficients.size();
        int newLen = Math.max(len, otherLen);
        List<R> newCoefficients;
        if (newLen > len) {
            newCoefficients = new ArrayList<>(newLen);
            for (int i = 0; i < len; i++) {
                newCoefficients.add(coefficients.get(i));
            }
            for (int i = len; i < newLen; i++) {
                newCoefficients.add(otherCoefficients.get(i));
            }
        } else {
            newCoefficients = coefficients;
        }
        for (int i = 0; i < Math.min(len, otherLen); i++) {
            newCoefficients.get(i).add(otherCoefficients.get(i));
        }
        coefficients = newCoefficients;
        normalize();
    }
    
    public PolynomialElement<R> difference(PolynomialElement<R> element) throws DomainException {
        List<R> otherCoefficients = element.getCoefficients();
        int len = getCoefficients().size();
        int otherLen = otherCoefficients.size();
        int newLen = Math.max(len, otherLen);
        RingElement[] newCoefficients = new RingElement[newLen];
        for (int i = 0; i < len; i++) {
            newCoefficients[i] = coefficients[i].deepCopy();
        }
        if (newLen > len) {
            for (int i = len; i < newLen; i++) {
                newCoefficients[i] = otherCoefficients[i].deepCopy();
                newCoefficients[i].negate();
            }
        }
        for (int i = 0; i < Math.min(len, otherLen); i++) {
            newCoefficients[i].subtract(otherCoefficients[i]);
        }
        PolynomialElement res = new PolynomialElement();
        res.setPolynomialRing(ring);
        res.setCoefficients(newCoefficients);
        res.normalize();
        return res;
    }
    
    public void subtract(PolynomialElement<R> element)
            throws DomainException {
        if (getRing().equals(element.getRing())) {
            try {
                RingElement[] otherCoefficients = element.getCoefficients();
                int len = getCoefficients().length;
                int otherLen = otherCoefficients.length;
                int newLen = Math.max(len, otherLen);
                RingElement[] newCoefficients;
                if (newLen > len) {
                    newCoefficients = new RingElement[newLen];
                    for (int i = 0; i < len; i++) {
                        newCoefficients[i] = coefficients[i];
                    }
                    for (int i = len; i < newLen; i++) {
                        newCoefficients[i] = (RingElement)otherCoefficients[i].negated();
                    }
                }
                else {
                    newCoefficients = coefficients;
                }
                for (int i = 0; i < Math.min(len, otherLen); i++) {
                    newCoefficients[i].subtract(otherCoefficients[i]);
                }
                coefficients = newCoefficients;
                normalize();
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public PolynomialElement<R> negated() {
        RingElement[] newCoefficients = new RingElement[coefficients.length];
        for (int i = 0; i < coefficients.length; i++) {
            newCoefficients[i] = (RingElement)coefficients[i].negated();
        }
        PolynomialElement res = new PolynomialElement();
        res.setPolynomialRing(ring);
        res.setCoefficients(newCoefficients);
        return res;
    }

    
    public void negate() {
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i].negate();
        }
    }
    
    //TODO FIX!!! the element should be in R, but this is a notion of algebra...
    public PolynomialElement<R> scaled(PolynomialElement<R> element)
            throws DomainException {
        if (ring.getCoefficientRing().hasElement(element)) {
            return product(element);
        }
        else {
            throw new DomainException(ring.getCoefficientRing(), element.getRing());
        }
    }

    //TODO FIX!!! the element should be in R, but this is a notion of algebra...
    public void scale(PolynomialElement<R> element)
            throws DomainException {
        if (ring.getCoefficientRing().hasElement(element)) {
            multiply(element);
        }
        else {
            throw new DomainException(ring.getCoefficientRing(), element.getRing());
        }
    }
    
    public PolynomialElement<R> product(PolynomialElement<R> element)
            throws DomainException {
        if (getRing().equals(element.getRing())) {
            if (isZero()) { return this; }
            if (element.isZero()) { return element; }
            RingElement[] newCoefficients = new RingElement[getDegree()+element.getDegree()+1];
            int i, j;
            for (i = 0; i < newCoefficients.length; i++) {
                newCoefficients[i] = (RingElement) ring.getCoefficientRing().getZero();
            }
            for (i = 0; i <= getDegree(); i++) {
                for (j = 0; j <= element.getDegree(); j++) {
                    RingElement p = getCoefficient(i).product(element.getCoefficient(j));
                    newCoefficients[i+j].add(p);
                }
            }
            PolynomialElement res = new PolynomialElement();
            res.setPolynomialRing(ring);
            res.setCoefficients(newCoefficients);
            res.normalize();
            return res;
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void multiply(PolynomialElement<R> element)
            throws DomainException {
        if (getRing().equals(element.getRing())) {
            int d0 = Math.max(getDegree(), 0);
            int d1 = Math.max(element.getDegree(), 0);
            RingElement[] newCoefficients = new RingElement[d0+d1+1];
            int i, j;
            for (i = 0; i < newCoefficients.length; i++) {
                newCoefficients[i] = (RingElement) ring.getCoefficientRing().getZero();
            }
            for (i = 0; i <= d0; i++) {
                for (j = 0; j <= d1; j++) {
                    RingElement p = getCoefficient(i).product(element.getCoefficient(j));
                    newCoefficients[i+j].add(p);
                }
            }
            setPolynomialRing(ring);
            setCoefficients(newCoefficients);
            normalize();
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public boolean isInvertible() {
        return getDegree() == 0 && getCoefficient(0).isInvertible();
    }
    
    
    public PolynomialElement inverse() {
        if (isInvertible()) {
            return new PolynomialElement(getRing(), getCoefficient(0).inverse());
        }
        else {
            throw new InverseException("Inverse of "+this+" does not exist");
        }
    }

    
    public void invert() {
        if (isInvertible()) {
            setCoefficients(new RingElement[] {getCoefficient(0).inverse()});
        }
        else {
            throw new InverseException("Inverse of "+this+" does not exist");
        }
    }
    
    public PolynomialElement<R> quotient(PolynomialElement<R> element)
            throws DomainException, DivisionException {
        if (getRing().equals(element.getRing())) {
            PolynomialElement[] remainder = new PolynomialElement[1];
            PolynomialElement q = quorem(element, remainder);
            if (remainder[0].getDegree() <= 0) {
                return q;
            }
            else {
                throw new DivisionException(this, element);
            }
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }

    public void divide(PolynomialElement<R> element)
            throws DomainException, DivisionException {
        if (getRing().equals(element.getRing())) {
            PolynomialElement[] remainder = new PolynomialElement[1];
            PolynomialElement q = quorem(element, remainder);
            if (remainder[0].getDegree() <= 0) {
                coefficients = q.coefficients;
            }
            else {
                throw new DivisionException(this, element);
            }
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }


    public boolean divides(RingElement element) {
        if (element instanceof PolynomialElement) {
            PolynomialElement pol = (PolynomialElement)element;
            try {
                PolynomialElement remainder = pol.rem(this);
                if (remainder.getDegree() <= 0) {
                    return true;
                }
                else {
                    return false;
                }
            }
            catch (DomainException e) {
                return false;
            }
            catch (DivisionException e) {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public PolynomialElement<R> quorem(PolynomialElement<R> element, PolynomialElement[] remainder)
            throws DomainException, DivisionException {
        if (getRing().hasElement(element)) {
            if (element.isZero()) {
                throw new DivisionException(this, element);
            }
            else if (getDegree() < element.getDegree()) {
                remainder[0] = this;
                return getRing().getZero();
            }
            try {
                RingElement[] p = new RingElement[coefficients.length];
                int pdeg = p.length-1;
                for (int i = 0; i <= pdeg; i++) { p[i] = coefficients[i]; }

                RingElement[] q = element.coefficients;
                int qdeg = q.length-1;


                RingElement[] quotient = new RingElement[pdeg-qdeg+1];
                int quotientdeg = quotient.length-1;

                int pi = pdeg;
                while (pi >= qdeg) {
                    RingElement q1 = p[pi].quotient(q[qdeg]);
                    quotient[quotientdeg] = q1;
                    for (int i = pi, j = qdeg; j >= 0; i--, j--) {
                        p[i] = (RingElement) p[i].difference(q1.product(q[j]));
                    }
                    pi--;
                    quotientdeg--;
                }
                remainder[0] = new PolynomialElement(getRing(), p);
                return new PolynomialElement(getRing(), quotient);
            }
            catch (DivisionException e) {
                throw new DivisionException(this, element);
            }
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }
    
    
    public PolynomialElement quo(PolynomialElement element)
            throws DomainException, DivisionException {
        if (getRing().hasElement(element)) {
            if (element.isZero()) {
                throw new DivisionException(this, element);
            }
            else if (getDegree() < element.getDegree()) {
                return getRing().getZero();
            }
            try {
                RingElement[] p = new RingElement[coefficients.length];
                int pdeg = p.length-1;
                for (int i = 0; i <= pdeg; i++) { p[i] = coefficients[i]; }

                RingElement[] q = element.coefficients;
                int qdeg = q.length-1;


                RingElement[] quotient = new RingElement[pdeg-qdeg+1];
                int quotientdeg = quotient.length-1;

                int pi = pdeg;
                while (pi >= qdeg) {
                    RingElement q1 = p[pi].quotient(q[qdeg]);
                    quotient[quotientdeg] = q1;
                    for (int i = pi, j = qdeg; j >= 0; i--, j--) {
                        p[i] = (RingElement) p[i].difference(q1.product(q[j]));
                    }
                    pi--;
                    quotientdeg--;
                }
                return new PolynomialElement(getRing(), quotient);
            }
            catch (DivisionException e) {
                throw new DivisionException(this, element);
            }
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }

    
    public PolynomialElement rem(PolynomialElement element)
            throws DomainException, DivisionException {
        if (getRing().hasElement(element)) {
            if (element.isZero()) {
                throw new DivisionException(this, element);
            }
            else if (getDegree() < element.getDegree()) {
                return this;
            }
            try {
                RingElement[] p = new RingElement[coefficients.length];
                int pdeg = p.length-1;
                for (int i = 0; i <= pdeg; i++) { p[i] = coefficients[i]; }

                RingElement[] q = element.coefficients;
                int qdeg = q.length-1;

                int pi = pdeg;
                while (pi >= qdeg) {
                    RingElement q1 = p[pi].quotient(q[qdeg]);
                    for (int i = pi, j = qdeg; j >= 0; i--, j--) {
                        p[i] = (RingElement) p[i].difference(q1.product(q[j]));
                    }
                    pi--;
                }
                return new PolynomialElement(getRing(), p);
            }
            catch (DivisionException e) {
                throw new DivisionException(this, element);
            }
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }

    
    /**
     * Returns the greatest common divisior
     * of <code>this</code> and <code>element</code>.
     */
    public PolynomialElement gcd(PolynomialElement element)
            throws DomainException, DivisionException {
        if (getModule().equals(element.getModule())) {
            PolynomialElement r0 = this;
            PolynomialElement r1 = element;
            PolynomialElement remainder;
            
            while (!r1.isZero()) {
                remainder = r0.rem(r1);
                r0 = r1;
                r1 = remainder;
            }
            RingElement lc = r0.coefficients[r0.coefficients.length-1];
            if (!lc.isOne()) {
                for (int i = 0; i < r0.coefficients.length; i++) {
                    r0.coefficients[i] = r0.coefficients[i].quotient(lc);
                }
            }
            return r0;
        }
        else {
            throw new DomainException(getRing(), element.getRing());
        }
    }
    
    /**
     * The extended Euclidean algorithm.
     * 
     * @param res is an array of polynomials that will contain two values such that:
     *            res[0]*x + res[1]*y = gcd(x,y)
     * @return the greatest common divisor of x and y, always non negative
     */
    public PolynomialElement<R> exgcd(PolynomialElement<R> y, PolynomialElement res[])
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
            q = r0.quorem(r1, remainder);
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

        RingElement lc = r0.coefficients[r0.coefficients.length-1];
        if (!lc.isOne()) {
            for (int i = 0; i < r0.coefficients.length; i++) {
                r0.coefficients[i] = r0.coefficients[i].quotient(lc);
            }
            for (int i = 0; i < s0.coefficients.length; i++) {
                s0.coefficients[i] = s0.coefficients[i].quotient(lc);
            }
            for (int i = 0; i < t0.coefficients.length; i++) {
                t0.coefficients[i] = t0.coefficients[i].quotient(lc);
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

    @Override
    public Ring<R> getCoefficientRing() {
        return getRing().getCoefficientRing();
    }
    
    @Override
    public String getIndeterminate() {
        return getRing().getIndeterminate();
    }
    
    @Override
    public PolynomialFreeElement resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return PolynomialProperFreeElement.make(getRing(), new PolynomialElement[0]);
        }
        else {
            PolynomialElement[] values = new PolynomialElement[n];
            values[0] = this;
            for (int i = 1; i < n; i++) {
                values[i] = getRing().getZero();
            }
            return PolynomialProperFreeElement.make(getRing(), values);
        }
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
    public int compareTo(ModuleElement object) {
        if (object instanceof PolynomialElement) {
            PolynomialElement<?> p = (PolynomialElement<?>)object;
            int d0 = coefficients.size()-1;
            int d1 = p.coefficients.size()-1;
            if (getRing().equals(p.getRing())) {
                for (int i = 0; i <= Math.min(d0, d1); i++) {
                    int c = getCoefficient(i).compareTo(p.getCoefficient(i));
                    if (c != 0) {
                        return c;
                    }
                }
                return d0-d1;
            }
            return getRing().compareTo(p.getRing());
        }
        else {
            return super.compareTo(object);
        }
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
    public String stringRep(boolean ... parens) {
        if (isZero()) {
            return "0";
        }
        if (isOne()) {
            return "1";
        }
        boolean paren = getRing().getCoefficientRing() instanceof PolynomialRing ||
                getRing().getCoefficientRing() instanceof CRing;
        StringBuilder buf = new StringBuilder(30);
        String ind = ring.getIndeterminate();
        if (paren) {
            buf.append("(");
        }
        buf.append(coefficients.get(coefficients.size()-1).stringRep(true));
        if (paren) {
            buf.append(")");
        }
        if (coefficients.size()-1 > 1) {
            buf.append("*");
            buf.append(ind);
            buf.append("^");
            buf.append(coefficients.size()-1);
        }
        else if (coefficients.size()-1 == 1) {
            buf.append("*");
            buf.append(ind);
        }
        for (int i = coefficients.size()-2; i >= 0; i--) {
            if (!coefficients.get(i).isZero()) {
                buf.append("+");
                if (paren) {
                    buf.append("(");
                }
                buf.append(coefficients.get(i).stringRep(true));
                if (paren) {
                    buf.append(")");
                }
                if (i > 1) {
                    buf.append("*");
                    buf.append(ind);
                    buf.append("^");
                    buf.append(i);
                }
                else if (i == 1) {
                    buf.append("*");
                    buf.append(ind);
                }
            }
        }
        if (parens.length > 0) {
            return TextUtils.parenthesize(buf.toString());
        }
        else {
            return buf.toString();
        }
    }

    @Override
    public String toString() {
        return "Polynomial["+stringRep()+"]";
    }
    
    @Override
    public double[] fold(ModuleElement[] elements) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getElementTypeName() {
        return "PolynomialElement";
    }

    @Override
    public int hashCode() {
        int hashCode = basicHash;
        for (int i = 0; i < coefficients.size(); i++) {
            hashCode ^= coefficients.get(i).hashCode();
        }
        return hashCode;
    }

    private void setPolynomialRing(PolynomialRing<R> ring) {
        this.ring = ring;
    }

    private void setCoefficients(List<R> coefficients) {
        this.coefficients = coefficients;
    }

    private void normalize() {
        if (getDegree() > 0) {
            int i = getDegree();
            while (getCoefficient(i).isZero() && i > 0) {
                i--;
            }
            if (i != getDegree()) {
                R[] newCoefficients = (R[]) new RingElement[i+1];
                for (int j = 0; j <= i; j++) {
                    newCoefficients[j] = getCoefficient(j);
                }
                setCoefficients(newCoefficients);
            }
        }
    }
    
    private static final int basicHash = "PolynomialElement".hashCode();
}
