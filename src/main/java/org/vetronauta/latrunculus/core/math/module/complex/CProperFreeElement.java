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

/**
 * Elements in the free modules of complex numbers.
 * @see CProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class CProperFreeElement extends ProperFreeElement<CProperFreeElement,CElement> implements CFreeElement<CProperFreeElement> {

    /**
     * Creates a CFreeElement from an array of Complex.
     */
    public static CFreeElement<?> make(Complex[] v) {
        if (v.length == 1) {
            return new CElement(v[0]);
        }
        else {
            return new CProperFreeElement(v);
        }
    }

    @Override
    public boolean isZero() {
        for (Complex complex : value) {
            if (!complex.isZero()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public CProperFreeElement conjugated() {
        Complex[] res = new Complex[value.length];
        for (int i = 0; i < value.length; i++) {
            res[i] = value[i].conjugated();
        }
        return new CProperFreeElement(res);
    }

    @Override
    public void conjugate() {
        for (int i = 0; i < value.length; i++) {
            value[i] = value[i].conjugated();
        }
    }

    @Override
    public CProperFreeElement sum(CProperFreeElement element) throws DomainException {
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

    @Override
    public void add(CProperFreeElement element) throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i] = value[i].sum(v[i]);
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
    public CProperFreeElement difference(CProperFreeElement element) throws DomainException {
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

    @Override
    public void subtract(CProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i] = value[i].difference(v[i]);
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    @Override
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

    @Override
    public void multiplyCW(CProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Complex[] v = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i] = value[i].product(v[i]);
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    @Override
    public CProperFreeElement negated() {
        Complex[] res = new Complex[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].neg();
        }
        return new CProperFreeElement(res);
    }
    
    @Override
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i] = value[i].neg();
        }
    }

    @Override
    public CProperFreeElement scaled(CElement element) {
        Complex val = element.getValue();
        Complex[] res = new Complex[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].product(val);
        }
        return new CProperFreeElement(res);        
    }

    @Override
    public void scale(CElement element) {
        Complex val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value[i] = value[i].product(val);
        }
    }

    @Override
    public CElement getComponent(int i) {
        assert(i < getLength());
        return new CElement(value[i]);
    }

    @Override
    public CElement getRingElement(int i) {
        assert(i < getLength());
        return new CElement(value[i]);
    }

    @Override
    public int getLength() {
        return value.length;
    }
    
    @Override
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
    
    @Override
    public CFreeElement<?> resize(int n) {
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
    
    @Override
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
    
    @Override
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

    public <T extends ModuleElement<T,S>, S extends RingElement<S>> T cast(Module<T,S> module) {
        return module.cast(this);
    }

    @Override
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

    @Override
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
    
    @Override
    public double[] fold(ModuleElement<?,?>[] elements) {
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

    public String getElementTypeName() {
        return "CFreeElement";
    }
    
    @Override
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
    public CProperFreeElement deepCopy() {
        Complex[] v = new Complex[getLength()];
        for (int i = 0; i < getLength(); i++) {
            v[i] = value[i].deepCopy();
        }
        return new CProperFreeElement(v);
    }
}
