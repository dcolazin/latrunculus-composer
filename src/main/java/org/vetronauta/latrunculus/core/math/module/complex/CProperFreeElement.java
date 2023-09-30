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

package org.vetronauta.latrunculus.core.math.module.complex;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUES_ATTR;

/**
 * Elements in the free modules of complex numbers.
 * @see CProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class CProperFreeElement extends ProperFreeElement implements CFreeElement {

    /**
     * Creates a CFreeElement from an array of Complex.
     */
    public static CFreeElement make(Complex[] v) {
        if (v.length == 1) {
            return new CElement(v[0]);
        }
        else {
            return new CProperFreeElement(v);
        }
    }

    
    public boolean isZero() {
        for (int i = 0; i < value.length; i++) {
            if (!value[i].isZero()) {
                return false;
            }
        }
        return true;
    }
    
    
    public CProperFreeElement conjugated() {
        Complex[] res = new Complex[value.length];
        for (int i = 0; i < value.length; i++) {
            res[i] = value[i].conjugated();
        }
        return new CProperFreeElement(res);
    }


    public void conjugate() {
        for (int i = 0; i < value.length; i++) {
            value[i].conjugate();
        }
    }


    public CProperFreeElement sum(ModuleElement element)
            throws DomainException {
        if (element instanceof CProperFreeElement) {
            return sum((CProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    

    /**
     * Returns the sum of this and <code>element</code>.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    public CProperFreeElement sum(CProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            Complex[] res = new Complex[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i].sum(v[i]);
            }
            return new CProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

        
    public void add(ModuleElement element)
            throws DomainException {
        if (element instanceof CProperFreeElement) {
            add((CProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    

    /**
     * Adds <code>element</code> to this.
     * This is a destructive operation.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    public void add(CProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i].add(v[i]);
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public CProperFreeElement difference(ModuleElement element)
            throws DomainException {
        if (element instanceof CProperFreeElement) {
            return difference((CProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    

    public CProperFreeElement difference(CProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            Complex[] res = new Complex[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i].difference(v[i]);
            }
            return new CProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public void subtract(ModuleElement element)
            throws DomainException {
        if (element instanceof CProperFreeElement) {
            subtract((CProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    /**
     * Subtracts <code>element</code> from this.
     * This is a destructive operation.
     * 
     * @throws DomainException if <code>element</code> is not in domain
     */
    public void subtract(CProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i].subtract(v[i]);
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public CProperFreeElement productCW(ModuleElement element)
            throws DomainException {
        if (element instanceof CProperFreeElement) {
            return productCW((CProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }


    public CProperFreeElement productCW(CProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            Complex[] res = new Complex[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i].product(v[i]);
            }
            return new CProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public void multiplyCW(ModuleElement element)
            throws DomainException {
        if (element instanceof CProperFreeElement) {
            multiplyCW((CProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }


    public void multiplyCW(CProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i].multiply(v[i]);
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public CProperFreeElement negated() {
        Complex[] res = new Complex[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].neg();
        }
        return new CProperFreeElement(res);
    }
    

    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i].negate();
        }
    }
    

    public CProperFreeElement scaled(RingElement element)
            throws DomainException {
        if (element instanceof CElement) {
            return scaled((CElement)element);
        }
        else {
            throw new DomainException(CRing.ring, element.getModule());
        }
    }
    

    public CProperFreeElement scaled(CElement element) {
        Complex val = element.getValue();
        Complex res[] = new Complex[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].product(val);
        }
        return new CProperFreeElement(res);        
    }
    
    
    public void scale(RingElement element) 
            throws DomainException {
        if (element instanceof CElement) {
            scale((CElement)element);
        }
        else {
            throw new DomainException(CRing.ring, element.getModule());
        }
    }

    
    public void scale(CElement element) {
        Complex val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value[i].multiply(val);
        }
    }

    
    public CElement getComponent(int i) {
        assert(i < getLength());
        return new CElement(value[i]);
    }

    
    public CElement getRingElement(int i) {
        assert(i < getLength());
        return new CElement(value[i]);
    }

    
    public int getLength() {
        return value.length;
    }
    

    public CProperFreeModule getModule() {
        if (module == null) {
            module = (CProperFreeModule)CProperFreeModule.make(getLength());
        }
        return module;
    }
    

    /**
     * Returns the array of Complex contained in this element.
     */
    public Complex[] getValue() {
        return value;
    }
    
    
    /**
     * Returns the Complex at index <code>i</code>.
     */
    public Complex getValue(int i) {
        return value[i];
    }
    

    public CFreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            Complex[] values = new Complex[n];
            for (int i = 0; i < minlen; i++) {
                values[i] = new Complex(getValue(i));
            }
            for (int i = minlen; i < n; i++) {
                values[i] = Complex.getZero();
            }
            return CProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof CProperFreeElement) {
            CProperFreeElement c = (CProperFreeElement)object;
            if (getLength() == c.getLength()) {
                for (int i = 0; i < getLength(); i++) {
                    if (value[i] != c.value[i]) {
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
        if (object instanceof CProperFreeElement) {
            CProperFreeElement element = (CProperFreeElement)object;
            int l = getLength()-element.getLength();
            if (l != 0) {
                return l;
            }
            else {
                for (int i = 0; i < getLength(); i++) {
                    int c = value[i].compareTo(element.value[i]);
                    if (c != 0) {
                        return c;
                    }
                }
                return 0;
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public ModuleElement cast(Module module) {
        return module.cast(this);
    }

    
    public String stringRep(boolean ... parens) {
        if (getLength() == 0) {
            return "Null";
        }
        else {
            StringBuilder res = new StringBuilder(value[0].toString());
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(value[i]);
            }
            if (parens.length > 0) {
                return TextUtils.parenthesize(res.toString());
            }
            else {
                return res.toString();
            }
        }
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("CFreeElement[");
        buf.append(getLength());
        buf.append("][");
        if (getLength() > 0) {
            buf.append(value[0]);
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(value[i]);
            }
        }
        buf.append("]");
        return buf.toString();
    }
    

    public double[] fold(ModuleElement[] elements) {
        double[][] res = new double[elements.length][getLength()*2];
        for (int i = 0; i < elements.length; i++) {
            Complex[] c = ((CProperFreeElement)elements[i]).getValue();
            for (int j = 0; j < getLength(); j++) {
                res[i][2*j] = c[j].getReal();
                res[i][2*j+1] = c[j].getImag();
            }
        }
        return Folding.fold(res);
    }
    
    public ModuleElement fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        if (!element.hasAttribute(VALUES_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(), VALUES_ATTR);
            return null;
        }
        
        String[] values = element.getAttribute(VALUES_ATTR).split(",");
        Complex[] complexValues = new Complex[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                complexValues[i] = Complex.parseComplex(values[i]);
            }
            catch (NumberFormatException e) {
                reader.setError("Values in type %%1 must be a comma-separated list of rationals.", getElementTypeName());
                return null;
            }
        }
        
        return CProperFreeElement.make(complexValues);
    }

    public String getElementTypeName() {
        return "CFreeElement";
    }
    
    
    public int hashCode() {
        int val = 11;
        for (int i = 0; i < getLength(); i++) {
            val = value[i].hashCode()*17+val;            
        }
        return val;
    }

    
    private CProperFreeElement(Complex[] value) {
        this.value = value;
    }
   
    
    private final Complex[]         value;
    private CProperFreeModule module = null;

    @Override
    public ModuleElement deepCopy() {
        Complex[] v = new Complex[getLength()];
        for (int i = 0; i < getLength(); i++) {
            v[i] = value[i].deepCopy();
        }
        return new CProperFreeElement(v);
    }
}
