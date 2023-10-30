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

package org.vetronauta.latrunculus.core.math.module.definition;

import org.vetronauta.latrunculus.core.exception.DivisionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.InverseException;

import java.util.Arrays;
import java.util.List;

/**
 * Elements in a product ring.
 * @see ProductRing
 * 
 * @author Gérard Milmeister
 */
public class ProductElement extends RingElement<ProductElement> {

    public static ProductElement make(RingElement<?>... elements) {
        if (elements.length < 2) {
            throw new IllegalArgumentException("A ProductRing must have at least 2 factors.");
        }
        return new ProductElement(elements);
    }
    
    /**
     * Creates a new product element from the collection <code>factors</code>.
     */
    public static ProductElement make(List<RingElement> factors) {
        RingElement[] f = new RingElement[factors.size()];
        int i = 0;
        for (RingElement factor : factors) {
            f[i++] = factor;
        }
        return make(f);
    }
        
    @Override
    public boolean isOne() {
        return Arrays.stream(factors).allMatch(RingElement::isOne);
    }
    
    @Override
    public boolean isZero() {
        return Arrays.stream(factors).allMatch(RingElement::isZero);
    }

    /**
     * Returns the sum of this element and <code>element</code>.
     */
    public ProductElement sum(ProductElement element)
            throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        RingElement[] newFactors = new RingElement[getFactorCount()];
        for (int i = 0; i < newFactors.length; i++) {
            RingElement currentValue = getValue(i);
            newFactors[i] = (RingElement) currentValue.getModule().cast(currentValue.sum(element.getValue(i)));
        }
        return new ProductElement(newFactors);
    }
    
    /**
     * Adds <code>element</code> to this element.
     */
    public void add(ProductElement element) throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getFactorCount(); i++) {
            RingElement currentValue = getValue(i);
            currentValue = (RingElement) currentValue.getModule().cast(currentValue.sum(element.getValue(i)));
            factors[i] = currentValue;
        }
    }

    /**
     * Returns the difference of this element and <code>element</code>.
     */
    public ProductElement difference(ProductElement element)
            throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        RingElement[] newFactors = new RingElement[getFactorCount()];
        for (int i = 0; i < newFactors.length; i++) {
            RingElement currentValue = getValue(i);
            newFactors[i] = (RingElement) currentValue.getModule().cast(currentValue.difference(element.getValue(i)));
        }
        return new ProductElement(newFactors);
    }
    
    /**
     * Subtracts <code>element</code> from this element.
     */
    public void subtract(ProductElement element)
            throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getFactorCount(); i++) {
            RingElement currentValue = getValue(i);
            currentValue = (RingElement) currentValue.getModule().cast(currentValue.difference(element.getValue(i)));
            factors[i] = currentValue;
        }
    }
    
    
    public ProductElement negated() {
        RingElement[] newFactors = new RingElement[getFactorCount()];
        for (int i = 0; i < newFactors.length; i++) {
            newFactors[i] = factors[i].negated();
        }
        return new ProductElement(newFactors);
    }

    
    public void negate() {
        for (int i = 0; i < getFactorCount(); i++) {
            factors[i].negate();
        }
    }

    
    public ProductElement scaled(ProductElement element)
            throws DomainException {
        return product(element);
    }
    

    public void scale(ProductElement element) throws DomainException {
        multiply(element);
    }
    
    /**
     * Returns the product of this element and <code>element</code>.
     */
    public ProductElement product(ProductElement element) throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        RingElement newFactors[] = new RingElement[getFactorCount()];
        for (int i = 0; i < newFactors.length; i++) {
            RingElement currentValue = getValue(i);
            newFactors[i] = currentValue.product((RingElement) currentValue.getModule().cast(element.getValue(i)));
        }
        return new ProductElement(newFactors);
    }

    /**
     * Multiplies this element with <code>element</code>.
     */
    public void multiply(ProductElement element)
            throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        for (int i = 0; i < getFactorCount(); i++) {
            RingElement currentValue = getValue(i);
            currentValue = currentValue.product((RingElement) currentValue.getModule().cast(element.getValue(i)));
            factors[i] = currentValue;
        }
    }

    
    public boolean isInvertible() {
        return Arrays.stream(factors).allMatch(RingElement::isInvertible);
    }
    
    
    public ProductElement inverse() {
        RingElement newFactors[] = new RingElement[getFactorCount()];
        for (int i = 0; i < newFactors.length; i++) {
            newFactors[i] = getValue(i).inverse();
        }
        return ProductElement.make(newFactors);
    }

    
    public void invert() {
        for (int i = 0; i < getFactorCount(); i++) {
            factors[i].invert();
        }
    }

    public ProductElement quotient(ProductElement element)
            throws DomainException, DivisionException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        RingElement newFactors[] = new RingElement[getFactorCount()];
        try {
            for (int i = 0; i < newFactors.length; i++) {
                RingElement currentValue = getValue(i);
                newFactors[i] = currentValue.quotient((RingElement) currentValue.getModule().cast(element.getValue(i)));
            }
        }
        catch (DivisionException e) {
            throw new DivisionException(this, element);
        }
        return new ProductElement(newFactors);
    }
    
    public void divide(ProductElement element)
            throws DomainException, DivisionException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }
        try {
            for (int i = 0; i < getFactorCount(); i++) {
                RingElement currentValue = getValue(i);
                currentValue = currentValue.quotient((RingElement) currentValue.getModule().cast(element.getValue(i)));
                factors[i] = currentValue;
            }
        }
        catch (DivisionException e) {
            throw new DivisionException(this, element);
        }
    }
    
    
    public boolean divides(RingElement<?> element) {
        if (!(element instanceof ProductElement)) {
            return false; //TODO is this correct?
        }
        if (getModule().equals(element.getModule())) {
            for (int i = 0; i < getFactorCount(); i++) {
                if (!(getValue(i)).divides(((ProductElement)element).getValue(i))) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }


    public ProductElement power(int n) {
        if (n == 0) {
            return getRing().getOne();
        }
        
        ProductElement factor;
        
        if (n < 0) {
            if (isInvertible()) {
                factor = inverse();
                n = -n;
            }
            else {
                throw new InverseException("Inverse of "+this+" does not exist.");
            }
        }
        else {
            factor = this.deepCopy();
        }
        
        // Finding leading bit in the exponent n
        int bpos = 31; // bits per int
        while ((n & (1 << bpos)) == 0) {
            bpos--;
        }

        ProductElement result = getRing().getOne();
        try {
            while (bpos >= 0) {
                result = result.product(result);
                if ((n & (1 << bpos)) != 0) {
                    result = result.product(factor);
                }
                bpos--;
            }
        }
        catch (DomainException e) {}

        return result;
    }
        

    public ProductRing getModule() {
        if (ring == null) {
            Ring[] rings = new Ring[factors.length];
            for (int i = 0; i < factors.length; i++) {
                rings[i] = factors[i].getRing();
            }
            ring = ProductRing.make(rings);
        }
        return ring;
    }

    
    public ProductRing getRing() {
        return getModule();
    }
    
    
    /**
     * Returns the number of factors of this product element.
     */
    public int getFactorCount() {
        return factors.length;
    }
    
    
    /**
     * Returns the factor at position <code>i</code> of this product element.
     */
    public RingElement<?> getFactor(int i) {
        return getValue(i);
    }

    
    /**
     * Returns the factor at position <code>i</code> of this product element.
     */
    public RingElement<?> getValue(int i) {
        return factors[i];
    }

    
    public FreeElement<?,ProductElement> resize(int n) {
        if (n == 1) {
            return this;
        }
        else if (n == 0) {
            return ProductProperFreeElement.make(getModule(), new ProductElement[0]);
        }
        else {
            ProductElement[] values = new ProductElement[n];
            values[0] = this;
            for (int i = 1; i < n; i++) {
                values[i] = getRing().getZero();
            }
            return ProductProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ProductElement) {
            ProductElement element = (ProductElement)object;
            if (element.getFactorCount() != getFactorCount()) {
                return false;
            }
            for (int i = 0; i < getFactorCount(); i++) {
                if (!getValue(i).equals(element.getValue(i))) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    
    public int compareTo(ModuleElement object) {
        if (object instanceof ProductElement) {
            ProductElement element = (ProductElement)object;
            int d = getLength()-element.getLength();
            if (d != 0) {
                return d;
            }
            else {
                for (int i = 0; i < getFactorCount(); i++) {
                    int comp = getValue(i).compareTo(element.getValue(i));
                    if (comp != 0) {
                        return comp;
                    }
                }
                return 0;
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    @Override
    public ProductElement deepCopy() {
        RingElement[] newFactors =  new RingElement[factors.length];
        for (int i = 0; i < factors.length; i++) {
            newFactors[i] = factors[i].deepCopy();
        }
        return ProductElement.make(newFactors);
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("ProductElement[");
        if (factors.length > 0) {
            buf.append(factors[0]);
        }
        for (int i = 1; i < factors.length; i++) {
            buf.append(",");
            buf.append(factors[i]);
        }
        buf.append("]");
        return buf.toString();
    }

    public String getElementTypeName() {
        return "ProductElement";
    }
    
    
    public int hashCode() {
        int hash = 7*basicHash;
        for (int i = 0; i < getLength(); i++) {
            hash = 37*hash+getValue(i).hashCode();
        }
        return hash;
    }
    
    
    protected ProductElement(RingElement[] factors) {
        this.factors = factors;
    }

    
    private RingElement<?>[] factors;
    private ProductRing   ring = null;

    private static final int basicHash = "ProductElement".hashCode();
}
