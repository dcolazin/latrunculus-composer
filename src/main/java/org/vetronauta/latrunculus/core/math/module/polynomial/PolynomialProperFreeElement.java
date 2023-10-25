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

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Elements in a free module of polynomials.
 * @see PolynomialProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class PolynomialProperFreeElement<B extends RingElement<B>> extends ProperFreeElement<PolynomialProperFreeElement<B>,PolynomialElement<B>> {

    public static <X extends RingElement<X>> FreeElement<?,PolynomialElement<X>> make(PolynomialRing ring, PolynomialElement[] v) {
        if (v.length == 1) {
            return v[0];
        }
        else {
            return new PolynomialProperFreeElement(ring, v);
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
    
    public PolynomialProperFreeElement<B> sum(PolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                PolynomialElement res[] = new PolynomialElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    res[i] = value[i].sum(element.value[i]);
                }
                return new PolynomialProperFreeElement(ring, res);
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(PolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                for (int i = 0; i < getLength(); i++) {
                    value[i].add(element.value[i]);
                }
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public PolynomialProperFreeElement<B> difference(PolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                PolynomialElement res[] = new PolynomialElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    res[i] = value[i].difference(element.value[i]);
                }
                return new PolynomialProperFreeElement(ring, res);
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void subtract(PolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                for (int i = 0; i < getLength(); i++) {
                    value[i].subtract(element.value[i]);
                }
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }        
    }
    
    public PolynomialProperFreeElement<B> productCW(PolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                PolynomialElement res[] = new PolynomialElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    res[i] = value[i].product(element.value[i]);
                }
                return new PolynomialProperFreeElement(ring, res);
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void multiplyCW(PolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                for (int i = 0; i < getLength(); i++) {
                    value[i].multiply(element.value[i]);
                }
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    

    public PolynomialProperFreeElement<B> negated() {
        PolynomialElement[] res = new PolynomialElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].negated();
        }
        return new PolynomialProperFreeElement(ring, res);
    }

    
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i].negate();
        }
    }
    
    //TODO algebra...
    public PolynomialProperFreeElement<B> scaled(PolynomialElement<B> element) throws DomainException {
        try {
            PolynomialElement res[] = new PolynomialElement[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i].scaled(element);
            }
            return new PolynomialProperFreeElement(ring, res);
        }
        catch (DomainException e) {
            throw new DomainException(this.getModule().getRing(), element.getRing());
        }
    }
    
    //TODO algebra...
    public void scale(PolynomialElement<B> element) throws DomainException {
        try {
            for (int i = 0; i < getLength(); i++) {
                value[i].scale(element);
            }
        }
        catch (DomainException e) {
            throw new DomainException(this.getModule().getRing(), element.getRing());
        }
    }

    
    public PolynomialElement getComponent(int i) {
        return value[i];
    }
    

    public PolynomialElement getRingElement(int i) {
        return value[i];
    }
    

    public int getLength() {
        return value.length;
    }
    

    public PolynomialProperFreeModule<B> getModule() {
        if (module == null) {
            module = (PolynomialProperFreeModule<B>) PolynomialProperFreeModule.make(ring, ring.getIndeterminate(), getLength());
        }
        return module;
    }
    

    public PolynomialElement[] getValue() {
        return value;
    }
    

    public PolynomialElement getValue(int i) {
        return value[i];
    }
    
    
    public Ring getCoefficientRing() {
        return ring.getCoefficientRing();
    }
    

    public String getIndeterminate() {
        return ring.getIndeterminate();
    }
    

    public FreeElement<?,PolynomialElement<B>> resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            PolynomialElement[] values = new PolynomialElement[n];
            for (int i = 0; i < minlen; i++) {
                values[i] = getValue(i);
            }
            for (int i = minlen; i < n; i++) {
                values[i] = ring.getZero();
            }
            return PolynomialProperFreeElement.make(ring, values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof PolynomialProperFreeElement) {
            PolynomialProperFreeElement e = (PolynomialProperFreeElement) object;
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
        if (object instanceof PolynomialProperFreeElement) {
            PolynomialProperFreeElement element = (PolynomialProperFreeElement)object;
            int c = ring.compareTo(element.ring);
            if (c != 0) {
                return c;
            }
            int l = getLength()-element.getLength();
            if (l != 0) {
                return l;
            }
            else {
                for (int i = 0; i < getLength(); i++) {
                    int d = value[i].compareTo(element.value[i]);
                    if (d != 0) {
                        return d;
                    }
                }
                return 0;
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    public String stringRep(boolean ... parens) {
        if (getLength() == 0) {
            return "Null";
        }
        else {
            StringBuilder buf = new StringBuilder(30);
            buf.append(getValue(0).stringRep());
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(getValue(i).stringRep());
            }
            if (parens.length > 0) {
                return TextUtils.parenthesize(buf.toString());
            }
            else {
                return buf.toString();
            }
        }
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("PolynomialFreeElement[");
        buf.append(getCoefficientRing());
        buf.append("][");
        if (getLength() > 0) {
            buf.append(getValue(0));
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(getValue(i));
            }
        }
        buf.append("]");
        return buf.toString();
    }
    

    public double[] fold(ModuleElement[] elements) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getElementTypeName() {
        return "PolynomialFreeElement";
    }
    
    
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= value[i].hashCode();
        }
        return val;
    }
    

    private PolynomialProperFreeElement(PolynomialRing ring, PolynomialElement[] value) {
        this.value = value;
        this.ring = ring;
    }


    private PolynomialElement[]  value;
    private PolynomialProperFreeModule<B> module = null;
    private PolynomialRing<B>       ring = null;

    @Override
    public ModuleElement deepCopy() {
        PolynomialElement[] v = new PolynomialElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            v[i] = value[i].deepCopy();
        }
        return new PolynomialProperFreeElement(ring, v);
    }
}
