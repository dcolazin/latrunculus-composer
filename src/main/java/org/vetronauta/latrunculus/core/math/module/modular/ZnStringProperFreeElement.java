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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRingRepository;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Elements in a free modules over ZnString.
 * @see ZnStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class ZnStringProperFreeElement extends ProperFreeElement<ZnStringProperFreeElement,ZnStringElement> implements FreeElement<ZnStringProperFreeElement,ZnStringElement> {

    //TODO various consistency checks for modulus

    private final List<RingString<ArithmeticModulus>> value;
    private final int modulus;
    private ArithmeticMultiModule<ArithmeticModulus> module;

    public static FreeElement<?, ZnStringElement> make(List<RingString<ArithmeticModulus>> v, int modulus) {
        if (v == null || v.isEmpty()) {
            return new ZnStringProperFreeElement(new ArrayList<>(0), modulus);
        }
        else if (v.size() == 1) {
            return new ZnStringElement(v.get(0), modulus);
        }
        return new ZnStringProperFreeElement(v, modulus);
    }
    
    public boolean isZero() {
        RingString<ArithmeticModulus> zero = RingString.getZero();
        for (int i = 0; i < getLength(); i++) {
            if (!value.get(i).equals(zero)) {
                return false;
            }
        }
        return true;
    }
    
    public ZnStringProperFreeElement sum(ZnStringProperFreeElement element) throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            List<RingString<ArithmeticModulus>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).sum(element.value.get(i)));
            }
            return new ZnStringProperFreeElement(res, modulus);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(ZnStringProperFreeElement element) throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            for (int i = 0; i < getLength(); i++) {
                value.get(i).add(element.value.get(i));
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public ZnStringProperFreeElement difference(ZnStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            List<RingString<ArithmeticModulus>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).difference(element.value.get(i)));
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
                value.get(i).subtract(element.value.get(i));
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public ZnStringProperFreeElement productCW(ZnStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength() && modulus == element.getModulus()) {
            List<RingString<ArithmeticModulus>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).product(element.value.get(i)));
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
                value.get(i).multiply(element.value.get(i));
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    

    public ZnStringProperFreeElement negated() {
        List<RingString<ArithmeticModulus>> res = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            res.add(value.get(i).negated());
        }
        return new ZnStringProperFreeElement(res, modulus);
    }

    
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value.get(i).negate();
        }
    }

    public ZnStringProperFreeElement scaled(ZnStringElement element)
            throws DomainException {
        if (element.getModulus() == modulus) {
            RingString<ArithmeticModulus> val = element.getValue();
            List<RingString<ArithmeticModulus>> res = new ArrayList<>(getLength());
            for (int i = 0; i < getLength(); i++) {
                res.add(value.get(i).product(val));
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
            RingString<ArithmeticModulus> val = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value.get(i).multiply(val);
            }
        }
        else {
            throw new DomainException(this.getModule().getRing(), element.getRing());
        }
    }

    
    public ModuleElement getComponent(int i) {
        assert(i < getLength());
        return new ZnStringElement(value.get(i));
    }
    

    public RingElement getRingElement(int i) {
        assert(i < getLength());
        return new ZnStringElement(value.get(i));
    }
    

    public int getLength() {
        return value.size();
    }
    

    public Module getModule() {
        if (module == null) {
            module = new ArithmeticMultiModule(ArithmeticRingRepository.getModulusRing(modulus), getLength());
        }
        return module;
    }
    

    public List<RingString<ArithmeticModulus>> getValue() {
        return value;
    }
    

    public RingString<ArithmeticModulus> getValue(int i) {
        return value.get(i);
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
            List<RingString<ArithmeticModulus>> values = new ArrayList<>(n);
            for (int i = 0; i < minlen; i++) {
                values.add(getValue(i));
            }
            for (int i = minlen; i < n; i++) {
                values.add(RingString.getZero());
            }
            return ZnStringProperFreeElement.make(values, modulus);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof ZnStringProperFreeElement) {
            ZnStringProperFreeElement e = (ZnStringProperFreeElement) object;
            if (getLength() == e.getLength()) {
                for (int i = 0; i < getLength(); i++) {
                    if (!value.get(i).equals(e.value.get(i))) {
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
                    int d = value.get(i).compareTo(element.value.get(i));
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
            res.append(value.get(0).stringRep());
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(value.get(i).stringRep());
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
            buf.append(value.get(0));
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(value.get(i));
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
            val ^= value.get(i).hashCode();
        }
        return val;
    }
    

    private ZnStringProperFreeElement(List<RingString<ArithmeticModulus>> value, int modulus) {
        this.value = value;
        this.modulus = modulus;
    }

    @Override
    public ZnStringProperFreeElement deepCopy() {
        List<RingString<ArithmeticModulus>> v = new ArrayList<>(getLength());
        for (int i = 0; i < getLength(); i++) {
            v.add(value.get(i).deepCopy());
        }
        return new ZnStringProperFreeElement(v, modulus);
    }
}
