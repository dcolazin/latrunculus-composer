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

import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.exception.DomainException;

/**
 * Elements in the free module over a product ring.
 * @see ProductProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ProductProperFreeElement extends ProperFreeElement<ProductProperFreeElement,ProductElement> {

    /**
     * Creates a new product free element over the specified ring with given components.
     */
    public static FreeElement<?,ProductElement> make(ProductRing ring, ProductElement[] v) {
        if (v.length == 1) {
            return v[0];
        }
        else {
            return new ProductProperFreeElement(ring, v);
        }
    }
    

    /**
     * Creates a new product free element over the specified ring with given components.
     * In this case <code>v</code> must be of length > 0.
     */
    public static FreeElement<?,ProductElement> make(ProductElement[] v) {
        if (v.length == 1) {
            return v[0];
        }
        else {
            return new ProductProperFreeElement(v[0].getRing(), v);
        }
    }

    
    public boolean isZero() {
        for (int i = 0; i < getLength(); i++) {
            if (!getComponent(i).isZero()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the sum of this element with <code>element</code>.
     */
    public ProductProperFreeElement sum(ProductProperFreeElement element)
            throws DomainException {
        if (getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }        
        ProductElement[] res = new ProductElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].sum(element.value[i]);
        }
        return new ProductProperFreeElement(getRing(), res);
    }

    /**
     * Adds <code>element</code> to this this element.
     */
    public void add(ProductProperFreeElement element)
            throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }        
        for (int i = 0; i < getLength(); i++) {
            value[i].add(element.value[i]);
        }        
    }

    /**
     * Returns the difference of this element and <code>element</code>.
     */
    public ProductProperFreeElement difference(ProductProperFreeElement element)
            throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }        
        ProductElement[] res = new ProductElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].difference(element.value[i]);
        }
        return new ProductProperFreeElement(getRing(), res);
    }

    /**
     * Subtracts <code>element</code> from this element.
     */
    public void subtract(ProductProperFreeElement element)
            throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }        
        for (int i = 0; i < getLength(); i++) {
            value[i].subtract(element.value[i]);
        }        
    }

    public ProductProperFreeElement productCW(ProductProperFreeElement element)
            throws DomainException {
        if (getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }        
        ProductElement[] res = new ProductElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].product(element.value[i]);
        }
        return new ProductProperFreeElement(getRing(), res);        
    }
    
    public void multiplyCW(ProductProperFreeElement element)
            throws DomainException {
        if (!getModule().equals(element.getModule())) {
            throw new DomainException(this.getModule(), element.getModule());
        }        
        for (int i = 0; i < getLength(); i++) {
            value[i].multiply(element.value[i]);
        }        
    }


    public ProductProperFreeElement negated() {
        ProductElement[] res = new ProductElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].negated();
        }
        return new ProductProperFreeElement(getRing(), res);
    }
    

    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i].negate();
        }
    }

    /**
     * Returns this element scaled by <code>element</code>.
     */
    public ProductProperFreeElement scaled(ProductElement element)
            throws DomainException {
        if (!element.getRing().equals(getRing())) {
            throw new DomainException(ring, element.getRing());
        }
        ProductElement[] res = new ProductElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].scaled(element);
        }
        return new ProductProperFreeElement(getRing(), res);        
    }
    
    /**
     * Scales this element by </i>element</i>.
     */
    public void scale(ProductElement element)
            throws DomainException {
        if (!element.getRing().equals(getRing())) {
            throw new DomainException(ring, element.getRing());
        }
        for (int i = 0; i < getLength(); i++) {
            value[i].scale(element);
        }
    }
    
    
    /**
     * Returns the number of factors of the underlying product ring.
     */
    public int getFactorCount() {
        return ring.getFactorCount();
    }

    
    public ProductElement getComponent(int i) {
        assert(i < getLength());
        return value[i]; 
    }
    

    public ProductElement getRingElement(int i) {
        assert(i < getLength());
        return value[i]; 
    }
    

    public int getLength() {
        return value.length;
    }
    

    public FreeModule<ProductProperFreeElement, ProductElement> getModule() {
        if (module == null) {
            module = (FreeModule<ProductProperFreeElement, ProductElement>) ProductProperFreeModule.make(getRing(), getLength());
        }
        return module;
    }
    

    public ProductRing getRing() {
        return ring;
    }
    

    /**
     * Returns the product element components as an array.
     */
    public ProductElement[] getValue() {
        return value;
    }
    
    
    /**
     * Returns the <code>i</code>-th product element component.
     */
    public ProductElement getValue(int i) {
        return value[i];
    }
    

    public FreeElement<?,ProductElement> resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            ProductElement[] values = new ProductElement[n];
            for (int i = 0; i < minlen; i++) {
                values[i] = getValue(i);
            }
            for (int i = minlen; i < n; i++) {
                values[i] = getRing().getZero();
            }
            return ProductProperFreeElement.make(getRing(), values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof ProductProperFreeElement) {
            ProductProperFreeElement e = (ProductProperFreeElement)object;
            if (getLength() == e.getLength()) {
                for (int i = 0; i < getLength(); i++) {
                    if (value[i] != e.value[i]) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    

    public int compareTo(ModuleElement object) {
        if (object instanceof ProductProperFreeElement) {
            ProductProperFreeElement element = (ProductProperFreeElement)object;
            int l = getLength()-element.getLength();
            if (l < 0) {
                return -1;
            }
            else if (l > 0) {
                return 1;
            }
            else {
                for (int i = 0; i < getLength(); i++) {
                    int comp = value[i].compareTo(element.value[i]);
                    if (comp < 0) {
                        return -1;
                    }
                    else if (comp > 0) {
                        return 1;
                    }
                }
                return 0;
            }
        }
        else {
            return super.compareTo(object);
        }
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("ProductFreeElement[");
        buf.append(getLength());
        buf.append("][");
        if (getLength() > 0) {
            buf.append(value[0].toString());
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(value[i]);
            }
        }
        buf.append("]");
        return buf.toString();
    }

    
    public double[] fold(ModuleElement[] elements) {
        double[][] res = new double[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            res[i] = elements[i].fold(new ModuleElement[] { elements[i] });
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "ProductFreeElement";
    }
    
    
    public int hashCode() {
        int val = 31;
        for (int i = 0; i < getLength(); i++) {
            val = 7*val + value[i].hashCode();
        }
        return val;
    }

    
    private ProductProperFreeElement(ProductRing ring, ProductElement[] value) {
        this.value = value;
        this.ring = ring;
    }

    
    private final ProductElement[]  value;
    private FreeModule<ProductProperFreeElement, ProductElement> module;
    private ProductRing       ring;

    @Override
    public ProductProperFreeElement deepCopy() {
        ProductElement[] v = new ProductElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            v[i] = value[i].deepCopy();
        }
        return new ProductProperFreeElement(getRing(), v);
    }
}
