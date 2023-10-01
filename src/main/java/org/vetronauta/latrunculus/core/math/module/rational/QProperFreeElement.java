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

package org.vetronauta.latrunculus.core.math.module.rational;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * Elements in a free module of rationals.
 * @see QProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class QProperFreeElement extends ProperFreeElement implements QFreeElement {

    public static QFreeElement nullElement = new QProperFreeElement(new Rational[0]);

    public static QFreeElement make(Rational[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new QElement(v[0]);
        }
        else {
            return new QProperFreeElement(v);
        }
    }

    
    public static QFreeElement make(int[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new QElement(v[0]);
        }
        else {
            Rational[] values = new Rational[v.length];
            for (int i = 0; i < v.length; i++) {
                values[i] = new Rational(v[i]);
            }
            return new QProperFreeElement(values);
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
    
    
    public QProperFreeElement sum(ModuleElement element)
            throws DomainException {
        if (element instanceof QProperFreeElement) {
            return sum((QProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public QProperFreeElement sum(QProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Rational[] v = element.getValue();
            Rational res[] = new Rational[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i].sum(v[i]);
            }
            return new QProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public void add(ModuleElement element)
            throws DomainException {
        if (element instanceof QProperFreeElement) {
            add((QProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public void add(QProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Rational[] v = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i].add(v[i]);
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public QProperFreeElement difference(ModuleElement element)
            throws DomainException {
        if (element instanceof QProperFreeElement) {
            return difference((QProperFreeElement)element);            
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public QProperFreeElement difference(QProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Rational[] v = element.getValue();
            Rational res[] = new Rational[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i].difference(v[i]);
            }
            return new QProperFreeElement(res);        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public void subtract(ModuleElement element)
            throws DomainException {
        if (element instanceof QProperFreeElement) {
            subtract((QProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    
    public void subtract(QProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value[i].subtract(element.value[i]);
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public QProperFreeElement productCW(ModuleElement element)
            throws DomainException {
        if (element instanceof QProperFreeElement) {
            return productCW((QProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }


    public QProperFreeElement productCW(QProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Rational[] v = element.getValue();
            Rational res[] = new Rational[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = value[i].product(v[i]);
            }
            return new QProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public void multiplyCW(ModuleElement element)
            throws DomainException {
        if (element instanceof QProperFreeElement) {
            multiplyCW((QProperFreeElement)element);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public void multiplyCW(QProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            Rational[] v = element.getValue();
            for (int i = 0; i < getLength(); i++) {
                value[i].multiply(v[i]);
            }
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    
    public QProperFreeElement negated() {
        Rational[] res = new Rational[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].neg();
        }
        return new QProperFreeElement(res);
    }
    

    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i].negate();
        }
    }

    
    public QProperFreeElement scaled(RingElement element)
            throws DomainException {
        if (element instanceof QElement) {
            return scaled((QElement)element);
        }
        else {
            throw new DomainException(QRing.ring, element.getRing());
        }
    }
    

    public QProperFreeElement scaled(QElement element) {
        Rational val = element.getValue();
        Rational res[] = new Rational[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = value[i].product(val);
        }
        return new QProperFreeElement(res);        
    }
    
    
    public void scale(RingElement element)
            throws DomainException {
        if (element instanceof QElement) {
            scale((QElement)element);
        }
        else {
            throw new DomainException(QRing.ring, element.getRing());
        }
    }

    
    public void scale(QElement element) {
        Rational val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value[i].multiply(val);
        }
    }

    
    public QElement getComponent(int i) {
        return new QElement(value[i]);
    }

    
    public QElement getRingElement(int i) {
        return new QElement(value[i]);
    }

    
    public int getLength() {
        return value.length;
    }

    
    public QProperFreeModule getModule() {
        if (module == null) {
            module = (QProperFreeModule)QProperFreeModule.make(getLength());
        }
        return module;
    }

    
    public Rational[] getValue() {
        return value;
    }


    public Rational getValue(int i) {
        return value[i];
    }
    
   
    public QFreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            Rational[] values = new Rational[n];
            for (int i = 0; i < minlen; i++) {
                values[i] = new Rational(getValue(i));
            }
            for (int i = minlen; i < n; i++) {
                values[i] = Rational.getZero();
            }
            return QProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof QProperFreeElement) {
            QProperFreeElement e = (QProperFreeElement)object;
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
        if (object instanceof QProperFreeElement) {
            QProperFreeElement element = (QProperFreeElement)object;
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
        buf.append("QFreeElement[");
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
        double[][] res = new double[elements.length][];
        // Create an array of double arrays corresponding
        // to the array of RFreeElements
        for (int i = 0; i < elements.length; i++) {
            Rational[] r = ((QProperFreeElement)elements[i]).getValue();
            res[i] = new double[elements.length];
            for (int j = 0; i < elements.length; j++) {
                res[i][j] = r[j].doubleValue();
            }
        }
        return Folding.fold(res);
    }

    public String getElementTypeName() {
        return "QFreeElement";
    }
    
    
    public int hashCode() {
        int hash = 7;
        for (int i = 0; i < getLength(); i++) {
            hash = 37*hash + value[i].hashCode();
        }
        return hash;
    }

    
    private QProperFreeElement(Rational[] value) {
        this.value = value;
    }


    private Rational[]        value;
    private QProperFreeModule module = null;

    @Override
    public ModuleElement deepCopy() {
        Rational[] v = new Rational[getLength()];
        for (int i = 0; i < getLength(); i++) {
            v[i] = value[i].deepCopy();
        }
        return new QProperFreeElement(v);
    }
}
