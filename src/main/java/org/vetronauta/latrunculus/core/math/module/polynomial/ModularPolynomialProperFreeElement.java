/*
 * Copyright (C) 2007 Gérard Milmeister
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

import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Elements in a free module of modular polynomials.
 * 
 * @author Gérard Milmeister
 */
public final class ModularPolynomialProperFreeElement<B extends RingElement<B>> extends ProperFreeElement<ModularPolynomialProperFreeElement<B>,ModularPolynomialElement<B>> {

    public static <X extends RingElement<X>> FreeElement<?,ModularPolynomialElement<X>> make(ModularPolynomialRing<X> ring, ModularPolynomialElement[] v) {
        if (v.length == 1) {
            return v[0];
        }
        else {
            return new ModularPolynomialProperFreeElement(ring, v);
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

    public ModularPolynomialProperFreeElement<B> sum(ModularPolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                ModularPolynomialElement res[] = new ModularPolynomialElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    res[i] = value[i].sum(element.value[i]);
                }
                return new ModularPolynomialProperFreeElement(ring, res);
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(ModularPolynomialProperFreeElement<B> element)
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
    
    public ModularPolynomialProperFreeElement<B> difference(ModularPolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                ModularPolynomialElement res[] = new ModularPolynomialElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    res[i] = value[i].difference(element.value[i]);
                }
                return new ModularPolynomialProperFreeElement(ring, res);
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void subtract(ModularPolynomialProperFreeElement<B> element)
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
    
    public ModularPolynomialProperFreeElement<B> productCW(ModularPolynomialProperFreeElement<B> element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            try {
                ModularPolynomialElement res[] = new ModularPolynomialElement[getLength()];
                for (int i = 0; i < getLength(); i++) {
                    res[i] = value[i].product(element.value[i]);
                }
                return new ModularPolynomialProperFreeElement(ring, res);
            }
            catch (DomainException e) {
                throw new DomainException(this.getModule(), element.getModule());
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void multiplyCW(ModularPolynomialProperFreeElement<B> element)
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
    

    public ModularPolynomialProperFreeElement<B> negated() {
        ModularPolynomialElement[] res = new ModularPolynomialElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].negated();
        }
        return new ModularPolynomialProperFreeElement(ring, res);
    }

    
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i].negate();
        }
    }
    
    //TODO should have parameter B?, implement algebra
    public ModularPolynomialProperFreeElement<B> scaled(ModularPolynomialElement<B> element)
            throws DomainException {
        try {
            ModularPolynomialElement res[] = new ModularPolynomialElement[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i].scaled(element);
            }
            return new ModularPolynomialProperFreeElement(ring, res);
        }
        catch (DomainException e) {
            throw new DomainException(this.getModule().getRing(), element.getRing());
        }
    }

    //TODO should have parameter B?, implement algebra
    public void scale(ModularPolynomialElement<B> element)
            throws DomainException {
        try {
            for (int i = 0; i < getLength(); i++) {
                value[i].scale(element);
            }
        }
        catch (DomainException e) {
            throw new DomainException(this.getModule().getRing(), element.getRing());
        }
    }

    
    public ModularPolynomialElement getComponent(int i) {
        return value[i];
    }
    

    public ModularPolynomialElement getRingElement(int i) {
        return value[i];
    }
    

    public int getLength() {
        return value.length;
    }
    

    public ModularPolynomialProperFreeModule<B> getModule() {
        if (module == null) {
            module = (ModularPolynomialProperFreeModule<B> ) ModularPolynomialProperFreeModule.make(ring.getModulus(), getLength());
        }
        return module;
    }
    

    public ModularPolynomialElement[] getValue() {
        return value;
    }
    

    public ModularPolynomialElement getValue(int i) {
        return value[i];
    }
    
    
    public Ring getCoefficientRing() {
        return ring.getCoefficientRing();
    }
    

    public String getIndeterminate() {
        return ring.getIndeterminate();
    }
    
    
    public PolynomialElement getModulus() {
        return ring.getModulus();
    }
    

    public FreeElement<?,ModularPolynomialElement<B>> resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            ModularPolynomialElement[] values = new ModularPolynomialElement[n];
            for (int i = 0; i < minlen; i++) {
                values[i] = getValue(i);
            }
            for (int i = minlen; i < n; i++) {
                values[i] = ring.getZero();
            }
            return ModularPolynomialProperFreeElement.make(ring, values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof ModularPolynomialProperFreeElement) {
            ModularPolynomialProperFreeElement e = (ModularPolynomialProperFreeElement) object;
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
        if (object instanceof ModularPolynomialProperFreeElement) {
            ModularPolynomialProperFreeElement element = (ModularPolynomialProperFreeElement)object;
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
    
    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("ModularPolynomialFreeElement[");
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
        return "ModularPolynomialFreeElement";
    }
    
    
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= value[i].hashCode();
        }
        return val;
    }
    

    private ModularPolynomialProperFreeElement(ModularPolynomialRing ring, ModularPolynomialElement[] value) {
        this.value = value;
        this.ring = ring;
    }


    private ModularPolynomialElement[]  value;
    private ModularPolynomialProperFreeModule<B>  module = null;
    private ModularPolynomialRing       ring = null;

    @Override
    public ModularPolynomialProperFreeElement deepCopy() {
        ModularPolynomialElement[] v = new ModularPolynomialElement[getLength()];
        for (int i = 0; i < getLength(); i++) {
            v[i] = value[i].deepCopy();
        }
        return new ModularPolynomialProperFreeElement(ring, v);
    }
}
