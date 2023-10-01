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

package org.vetronauta.latrunculus.core.math.module.integer;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.matrix.ZMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZFreeAffineMorphism;

import java.util.Iterator;
import java.util.List;

/**
 * Free modules over integers.
 * @see ZProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class ZProperFreeModule
		extends ProperFreeModule
		implements ZFreeModule {

    public static final ZProperFreeModule nullModule = new ZProperFreeModule(0);

    public static ZFreeModule make(int dimension) {
        dimension = (dimension < 0)?0:dimension;
        if (dimension == 0) {
            return nullModule;
        }
        else if (dimension == 1) {
            return ZRing.ring;
        }
        else {
            return new ZProperFreeModule(dimension);
        }
    }
    
    
    public ZFreeElement getZero() {
        int[] res = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = 0;
        }
        return ZProperFreeElement.make(res);
    }
    
    
    public ZFreeElement getUnitElement(int i) {
        int[] v = new int[getDimension()];
        v[i] = 1;
        return ZProperFreeElement.make(v);
    }
    

    public ZProperFreeModule getNullModule() {
        return nullModule;
    }
    
    
    public boolean isNullModule() {
        return this == nullModule;
    }

    
    public ZRing getComponentModule(int i) {
        return ZRing.ring;
    }

    
    public ZRing getRing() {
        return ZRing.ring;
    }


    public boolean isVectorSpace() {
        return false;
    }


    public boolean hasElement(ModuleElement element) {
        return (element instanceof ZProperFreeElement &&
                element.getLength() == getDimension());
    }


    public int compareTo(Module object) {
        if (object instanceof ZProperFreeModule) {
            ZProperFreeModule module = (ZProperFreeModule)object;
            return getDimension()-module.getDimension();
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public ZFreeElement createElement(List<ModuleElement> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }

        Iterator<ModuleElement> iter = elements.iterator();
        int[] values = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            ModuleElement castElement = iter.next().cast(ZRing.ring);
            if (castElement == null) {
                return null;
            }
            values[i] = ((ZElement)castElement).getValue();
        }

        return ZProperFreeElement.make(values);
    }
    
   
    public ZFreeElement cast(ModuleElement element) {
        if (element.getLength() == getDimension()) {
            if (element instanceof DirectSumElement) {
                return (ZFreeElement)element.cast(this);
            }
            else if (element instanceof ZProperFreeElement) {
                return (ZFreeElement)element;
            }
            else {   
                int[] elements = new int[getDimension()];
                for (int i = 0; i < getDimension(); i++) {
                    ModuleElement castElement = element.getComponent(i).cast(ZRing.ring);
                    if (castElement == null) {
                        return null;
                    }
                    elements[i] = ((ZElement)castElement).getValue();
                }
                return ZProperFreeElement.make(elements);
            }
        }
        else {
            return null;
        }
    }

    
    public boolean equals(Object object) {
        return (object instanceof ZProperFreeModule &&
                getDimension() == ((ZProperFreeModule)object).getDimension());
    }

    
    public ZFreeElement parseString(String string) {
        string = TextUtils.unparenthesize(string);
        String[] components = string.split(",");
        if (components.length != getDimension()) {
            return null;
        }
        else {
            int[] values = new int[components.length];
            for (int i = 0; i < values.length; i++) {
                try {
                    values[i] = Integer.parseInt(components[i]);
                }
                catch (NumberFormatException e) {
                    return null;
                }
            }
            return ZProperFreeElement.make(values);
        }
    }
    
    
    public String toString() {
        return "ZFreeModule["+getDimension()+"]";
    }

    
    public String toVisualString() {
        return "Z^"+getDimension();
    }

    public String getElementTypeName() {
        return "ZFreeModule";
    }
    
    public int hashCode() {
        return 37*basicHash + getDimension();
    }
    
    
    protected ModuleMorphism _getProjection(int index) {
        ZMatrix A = new ZMatrix(1, getDimension());
        A.set(0, index, 1);
        return ZFreeAffineMorphism.make(A, new int[] { 0 });
    }
    
    
    protected ModuleMorphism _getInjection(int index) {
        ZMatrix A = new ZMatrix(getDimension(), 1);
        A.set(index, 0, 1);
        int[] b = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            b[i] = 0;
        }
        return ZFreeAffineMorphism.make(A, b);
    }
    
    
    private ZProperFreeModule(int dimension) {
        super(dimension);
    }

    
    private final static int basicHash = "ZFreeModule".hashCode();    
}
