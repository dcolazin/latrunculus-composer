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
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Free modules over QStringRing.
 * @see QStringRing
 * 
 * @author Gérard Milmeister
 */
public final class QStringProperFreeModule extends ProperFreeModule<QStringProperFreeElement,QStringElement> implements QStringFreeModule<QStringProperFreeElement> {

    public static final QStringProperFreeModule nullModule = new QStringProperFreeModule(0);

    public static QStringFreeModule make(int dimension) {
        dimension = (dimension < 0)?0:dimension;
        if (dimension == 0) {
            return nullModule;
        }
        else if (dimension == 1) {
            return QStringRing.ring;
        }
        else {
            return new QStringProperFreeModule(dimension);
        }
    }
    
    
    public QStringProperFreeElement getZero() {
        List<RingString<Rational>> res = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            res.set(i, RingString.getZero());
        }
        return (QStringProperFreeElement) QStringProperFreeElement.make(res); //TODO do not cast
    }
    
    
    public QStringProperFreeElement getUnitElement(int i) {
        assert(i >= 0 && i < getDimension());
        QStringProperFreeElement unit = getZero();
        unit.setValue(i, RingString.getOne());
        return unit;
    }
    

    public Module getNullModule() {
        return nullModule;
    }
    
    
    public boolean isNullModule() {
        return this == nullModule;
    }

    
    public Module getComponentModule(int i) {
        return QStringRing.ring;
    }

    
    public Ring getRing() {
        return QStringRing.ring;
    }


    public boolean isVectorSpace() {
        return false;
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof QStringProperFreeElement &&
                element.getLength() == getDimension());
    }


    public int compareTo(Module object) {
        if (object instanceof QStringProperFreeModule) {
            QStringProperFreeModule module = (QStringProperFreeModule)object;
            return getDimension()-module.getDimension();
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public QStringProperFreeElement createElement(List<ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        Iterator<ModuleElement<?, ?>> iter = elements.iterator();
        List<RingString<Rational>> values = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            Object object = iter.next();
            if (object instanceof QStringElement) {
                values.set(i, ((QStringElement) object).getValue());
            }
            else {
                return null;
            }
        }
        return (QStringProperFreeElement) QStringProperFreeElement.make(values);
    }
    
   
    public QStringProperFreeElement cast(ModuleElement element) {
        if (element.getLength() > getDimension()) {
            QStringRing ring = QStringRing.ring;
            List<ModuleElement<?,?>> elementList = new LinkedList<>();
            for (int i = 0; i < getDimension(); i++) {
                ModuleElement e = ring.cast(element.getComponent(i));
                if (e != null) {
                    elementList.add(e);
                }
                else {
                    return null;
                }
            }
            return createElement(elementList);
        }
        return null;
    }

    
    public boolean equals(Object object) {
        return (object instanceof QStringProperFreeModule &&
                getDimension() == ((QStringProperFreeModule)object).getDimension());
    }

    
    public QStringProperFreeElement parseString(String string) {
        string = TextUtils.unparenthesize(string);
        if (string.equals("Null")) {
            return (QStringProperFreeElement) QStringProperFreeElement.make(new ArrayList<>());
        }
        if (string.charAt(0) == '(' && string.charAt(string.length()-1) == ')') {
            string = string.substring(1, string.length()-1);
            String[] strings = TextUtils.split(string, ',');
            if (strings.length != getDimension()) {
                return null;
            }
            else {
                List<RingString<Rational>> qstrings = new ArrayList<>(getDimension());
                for (int i = 0; i < strings.length; i++) {
                    qstrings.set(i, QStringRing.parse(strings[i]));
                }
                return (QStringProperFreeElement) QStringProperFreeElement.make(qstrings);
            }            
        }
        else {
            return null;
        }
    }
    
    
    public String toString() {
        return "QStringFreeModule["+getDimension()+"]";
    }


    public String toVisualString() {
        return "(C-String)^"+getDimension();
    }

    public String getElementTypeName() {
        return "QStringFreeModule";
    }

    
    public int hashCode() {
        return 37*basicHash + getDimension();
    }
    
    
    protected ModuleMorphism _getProjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), getDimension(), 1);
        m.setMatrix(0, index, getRing().getOne());
        return m;
    }
    
    
    protected ModuleMorphism _getInjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), 1, getDimension());
        m.setMatrix(index, 0, getRing().getOne());
        return m;
    }

    
    private QStringProperFreeModule(int dimension) {
        super(dimension);
    }


    private static final int basicHash = "QStringFreeModule".hashCode();
}
