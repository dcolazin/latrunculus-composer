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

import lombok.extern.slf4j.Slf4j;
import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * The abstract base class for ring elements.
 * Ring elements always have length 1.
 * @see Ring
 * 
 * @author Gérard Milmeister
 */
@Slf4j
public abstract class RingElement<R extends RingElement<R>> implements FreeElement<R,R> {

    @Override
    public FreeElement<?, R> resize(int n) {
        if (n == 1) {
            return this;
        }
        List<R> res = new ArrayList<>(n);
        if (n > 0) {
            res.add(this.deepCopy());
        }
        for (int i = 1; i < n; i++) {
            res.add(getRing().getZero());
        }
        return new Vector<>(getRing(), res);
    }
    
    /**
     * Returns true if this ring element is one.
     */
    public abstract boolean isOne();

    /**
     * Returns the product of this ring element with <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    public abstract R product(R element) throws DomainException;

    
    /**
     * Multiplies this ring element with <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    public abstract void multiply(R element) throws DomainException;

    @Override
    public R productCW(R element) throws DomainException {
        return product(element);
    }

    @Override
    public void multiplyCW(R element) throws DomainException {
        multiply(element);
    }

    @Override
    public R scaled(R element) throws DomainException {
        return product(element);
    }

    @Override
    public void scale(R element) throws DomainException {
        multiply(element);
    }
    
    
    /**
     * Returns true if this ring element is invertible.
     */
    public abstract boolean isInvertible();
    
    
    /**
     * Returns the inverse of this ring element, if it has an inverse.
     */
    public abstract R inverse();
    
    
    /**
     * Inverts this ring element, if it has an inverse.
     */
    public abstract void invert();

    
    /**
     * Returns the solution <i>x</i> of
     * <code>element</code>*<i>x</i> = <code>this</code>,
     * if it exists, otherwise a DivisionException is thrown. 
     */
    public abstract R quotient(R element) throws DomainException, DivisionException;
    
    
    /**
     * Replaces <code>this</code> by the solution <i>x</i> of
     * <code>element</code>*<i>x</i> = <code>this</code>, if it exists,
     * otherwise a DivisionException is thrown.
     */
    public abstract void divide(R element) throws DomainException, DivisionException;


    /**
     * Return true iff the solution <i>x</i> of 
     * <code>this</code>*<i>x</i> = <code>element</code>
     * exists.
     */
    public abstract boolean divides(RingElement<?> element);

    
    /**
     * Raises this ring element to the power <code>n</code>.
     */
    public R power(int n) {
        if (n == 0) {
            return getRing().getOne();
        }

        R factor = this.deepCopy();
        
        if (n < 0) {
            factor.invert();
            n = -n;
        }
        
        // Finding leading bit in the exponent n
        int bpos = 31; // bits per int
        while ((n & (1 << bpos)) == 0) {
            bpos--;
        }

        R result = getRing().getOne();
        try {
            while (bpos >= 0) {
                result = result.product(result);
                if ((n & (1 << bpos)) != 0) {
                    result = result.product(factor);
                }
                bpos--;
            }
        }
        catch (DomainException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    
    /**
     * Returns the length of this ring element.
     * @return always 1
     */
    @Override
    public int getLength() {
        return 1;
    }
    
    @Override
    public R getComponent(int i) {
        return (R) this;
    }

    @Override
    public R getRingElement(int i) {
        return (R) this;
    }

    @Override
    public Iterator<R> iterator() {
        return Collections.singleton((R) this).iterator();
    }
    
    
    /**
     * Returns the ring this element is a member of.
     */
    public abstract Ring<R> getRing();

    @Override
    public Ring<R> getModule() {
        return getRing();
    }
    
    @Override
    public final int compareTo(ModuleElement object) {
        if (this.getClass().isAssignableFrom(object.getClass())) {
            return sameClassCompare((R) object);
        }
        return getModule().compareTo(object.getModule());
    }

    protected abstract int sameClassCompare(R other);

    @Override
    public abstract R deepCopy();

}
