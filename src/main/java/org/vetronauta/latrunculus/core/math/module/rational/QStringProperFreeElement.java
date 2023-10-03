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
import org.vetronauta.latrunculus.core.math.arith.string.QString;
import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZFreeModule;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeModule;

/**
 * Elements in the free module of QString.
 * @see QStringProperFreeModule
 * 
 * @author Gérard Milmeister
 */
public final class QStringProperFreeElement extends ProperFreeElement<QStringProperFreeElement,QStringElement> implements QStringFreeElement<QStringProperFreeElement> {

    public static QStringFreeElement nullElement = new QStringProperFreeElement(new QString[0]);

    public static QStringFreeElement make(QString[] v) {
        assert(v != null);
        if (v.length == 0) {
            return nullElement;
        }
        else if (v.length == 1) {
            return new QStringElement(v[0]);
        }
        else {
            return new QStringProperFreeElement(v);
        }
    }

    
    public boolean isZero() {
        QString zero = QString.getZero();
        for (int i = 0; i < getLength(); i++) {
            if (!value[i].equals(zero)) {
                return false;
            }
        }
        return true;
    }
    
    public QStringProperFreeElement sum(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            QString res[] = new QString[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = (QString)value[i].sum(element.value[i]);
            }
            return new QStringProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void add(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value[i].add(element.value[i]);
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public QStringProperFreeElement difference(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            QString res[] = new QString[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = (QString)value[i].difference(element.value[i]);
            }
            return new QStringProperFreeElement(res);        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }

    public void subtract(QStringProperFreeElement element)
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
    
    public QStringProperFreeElement productCW(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            QString res[] = new QString[getLength()];
            for (int i = 0; i < getLength(); i++) {
                res[i] = (QString)value[i].product(element.value[i]);
            }
            return new QStringProperFreeElement(res);
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    
    public void multiplyCW(QStringProperFreeElement element)
            throws DomainException {
        if (getLength() == element.getLength()) {
            for (int i = 0; i < getLength(); i++) {
                value[i].multiply(element.value[i]);
            }        
        }
        else {
            throw new DomainException(this.getModule(), element.getModule());
        }
    }
    

    public QStringProperFreeElement negated() {
        QString[] res = new QString[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = (QString)value[i].negated();
        }
        return new QStringProperFreeElement(res);
    }

    
    public void negate() {
        for (int i = 0; i < getLength(); i++) {
            value[i].negate();
        }
    }

    public QStringProperFreeElement scaled(QStringElement element) {
        QString val = element.getValue();
        QString res[] = new QString[getLength()];
        for (int i = 0; i < getLength(); i++) {
            res[i] = (QString)value[i].product(val);
        }
        return new QStringProperFreeElement(res);        
    }

    public void scale(QStringElement element) {
        QString val = element.getValue();
        for (int i = 0; i < getLength(); i++) {
            value[i].multiply(val);
        }
    }

    
    public ModuleElement getComponent(int i) {
        assert(i < getLength());
        return new QStringElement(value[i]);
    }
    

    public RingElement getRingElement(int i) {
        assert(i < getLength());
        return new QStringElement(value[i]);
    }
    

    public int getLength() {
        return value.length;
    }
    

    public Module getModule() {
        if (module == null) {
            module = ZProperFreeModule.make(getLength());
        }
        return module;
    }
    

    public QString[] getValue() {
        return value;
    }
    

    public QString getValue(int i) {
        return value[i];
    }
    

    public FreeElement resize(int n) {
        if (n == getLength()) {
            return this;
        }
        else {
            int minlen = Math.min(n, getLength());
            QString[] values = new QString[n];
            for (int i = 0; i < minlen; i++) {
                values[i] = getValue(i);
            }
            for (int i = minlen; i < n; i++) {
                values[i] = QString.getZero();
            }
            return QStringProperFreeElement.make(values);
        }
    }
    

    public boolean equals(Object object) {
        if (object instanceof QStringProperFreeElement) {
            QStringProperFreeElement e = (QStringProperFreeElement) object;
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
            QStringProperFreeElement element = (QStringProperFreeElement)object;
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
            res.append(value[0].stringRep());
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(value[i].stringRep());
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
        StringBuilder res = new StringBuilder(30);
        res.append("QStringFreeElement[");
        res.append(getLength());
        res.append("][");
        if (getLength() > 0) {
            res.append(value[0]);
            for (int i = 1; i < getLength(); i++) {
                res.append(",");
                res.append(value[i]);
            }
        }
        res.append("]");
        return res.toString();
    }
    

    public double[] fold(ModuleElement[] elements) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getElementTypeName() {
        return "QStringFreeElement";
    }
    
    
    public int hashCode() {
        int val = 0;
        for (int i = 0; i < getLength(); i++) {
            val ^= value[i].hashCode();
        }
        return val;
    }
    

    private QStringProperFreeElement(QString[] value) {
        this.value = value;
    }


    private final QString[]   value;
    private ZFreeModule module = null;

    @Override
    public ModuleElement deepCopy() {
        QString[] v = new QString[getLength()];
        for (int i = 0; i < getLength(); i++) {
            v[i] = (QString)value[i].deepCopy();
        }
        return new QStringProperFreeElement(v);
    }
}
