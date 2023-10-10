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

package org.vetronauta.latrunculus.core.math.module.modular;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.string.ZnString;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;

/**
 * Elements in a free modules over ZnString.
 * @see ZnStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ZnStringProperFreeElement extends ProperFreeElement<ZnStringProperFreeElement,ZnStringElement> implements ZnStringFreeElement<ZnStringProperFreeElement> {

    public static ZnStringFreeElement make(ZnString[] v, int modulus) {
        if (v == null || v.length == 0) {
            return new ZnStringProperFreeElement(new ZnString[0], modulus);
        }
        else if (v.length == 1) {
            if (v[0].getModulus() == modulus) {
                return new ZnStringElement(v[0]);
            }
            else {
                return null;
            }
        }
        else {
            for (int i = 0; i < v.length; i++) {
                if (v[i].getModulus() != modulus) {
                    return null;
                }
            }
            return new ZnStringProperFreeElement(v, modulus);
        }
    }

    
    public boolean isZero() {
        ZnString zero = ZnString.getZero(modulus);
        for (int i = 0; i < getLength(); i++) {
            if (!value[i].equals(zero)) {
                return false;
            }
        }
        return true;
    }
    
    public ZnStringProperFreeElement sum(ZnStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            ZnString res[] = new ZnString[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = (ZnString)value[i].sum(element.value[i]);
            }
            return new ZnStringProperFreeElement(res, modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(ZnStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            for (int i = 0; i < getLength(); i++) {
                value[i].add(element.value[i]);
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public ZnStringProperFreeElement difference(ZnStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            ZnString res[] = new ZnString[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = (ZnString)value[i].difference(element.value[i]);
            }
            return new ZnStringProperFreeElement(res, modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void subtract(ZnStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            for (int i = 0; i < getLength(); i++) {
                value[i].subtract(element.value[i]);
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public ZnStringProperFreeElement productCW(ZnStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            ZnString res[] = new ZnString[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = (ZnString)value[i].product(element.value[i]);
            }
            return new ZnStringProperFreeElement(res, modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void multiplyCW(ZnStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            for (int i = 0; i < getLength(); i++) {
                value[i].multiply(element.value[i]);
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    

    public ZnStringProperFreeElement negated() {
        ZnString[] res = new ZnString[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].negated();
        }
        return new ZnStringProperFreeElement(res, modulus);
    }

    
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i].negate();
        }
    }

    public ZnStringProperFreeElement scaled(ZnStringElement element)
            throws DomainException {
        if (element.getModulus() == modulus) {
            ZnString val = element.getValue();
            ZnString res[] = new ZnString[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = (ZnString)value[i].product(val);
            }
            return new ZnStringProperFreeElement(res, modulus);
        }
        else {
            throw new DomainException(this.getModule().getRing(), element.getRing());
        }
    }
    
    public void scale(ZnStringElement element)
            throws DomainException {
        if (element.getModulus() == modulus) {
            ZnString val = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i].multiply(val);
            }
        }
        else {
            throw new DomainException(this.getModule().getRing(), element.getRing());
        }
    }

    
    public ModuleElement getComponent(int i) {
        assert(i < getLength());
        return new ZnStringElement(value[i]);
    }
    

    public RingElement getRingElement(int i) {
        assert(i < getLength());
        return new ZnStringElement(value[i]);
    }
    

    public int getLength() {
        return value.length;
    }
    

    public Module getModule() {
        if (module == null) {
            module = (ZnProperFreeModule) ZnProperFreeModule.make(getLength(), modulus);
        }
        return module;
    }
    

    public ZnString[] getValue() {
        return value;
    }
    

    public ZnString getValue(int i) {
        return value[i];
    }
    

    public int getModulus() {
        return modulus;
    }
    
    
    public FreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            ZnString[] values = new ZnString[n];
            for (int i = 0; i < minlen; i++) {
                values[i] = getValue(i);
            }
            for (int i = minlen; i < n; i++) {
                values[i] = ZnString.getZero(modulus);
            }
            return ZnStringProperFreeElement.make(values, modulus);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof ZnStringProperFreeElement) {
            ZnStringProperFreeElement e = (ZnStringProperFreeElement) object;
            if (getLength() == e.getLength()) {
                for (int i = 0; i < getLength(); i++) {
                    if (!value[i].equals(e.value[i])) {
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
        if (object instanceof ZProperFreeElement) {
            ZnStringProperFreeElement element = (ZnStringProperFreeElement)object;
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
            StringBuilder res = new StringBuilder(30);
            res.append("(");
            res.append(value[0].stringRep());
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(value[i].stringRep());
            }
            res.append(")");
            if (parens.length > 0) {
                return TextUtils.parenthesize(res.toString());
            }
            else {
                return res.toString();
            }
        }
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("ZnStringFreeElement[");
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
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getElementTypeName() {
        return "ZnStringFreeElement";
    }
    
    
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= value[i].hashCode();
        }
        return val;
    }
    

    private ZnStringProperFreeElement(ZnString[] value, int modulus) {
        this.value = value;
        this.modulus = modulus;
    }


    private ZnString[]   value;
    private int          modulus;
    private ZnProperFreeModule module = null;

    @Override
    public ModuleElement deepCopy() {
        ZnString[] v = new ZnString[getLength()];
        for (int i = 0; i < getLength(); i++) {
            v[i] = value[i].deepCopy();
        }
        return new ZnStringProperFreeElement(v, modulus);
    }
}
