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
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.matrix.ZMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZFreeAffineMorphism;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Free modules over integers.
 * @see ZProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class ZProperFreeModule extends ProperFreeModule<ArithmeticMultiElement<ArithmeticInteger>, ArithmeticElement<ArithmeticInteger>> {

    public static final ZProperFreeModule nullModule = new ZProperFreeModule(0);

    public static FreeModule<?,ArithmeticElement<ArithmeticInteger>> make(int dimension) {
        dimension = Math.max(dimension, 0);
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

    public ZProperFreeElement getZero() {
        int[] res = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = 0;
        }
        return (ZProperFreeElement) ZProperFreeElement.make(res); //TODO do not cast
    }
    
    
    public ZProperFreeElement getUnitElement(int i) {
        int[] v = new int[getDimension()];
        v[i] = 1;
        return (ZProperFreeElement) ZProperFreeElement.make(v); //TODO do not cast
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

    
    public ZProperFreeElement createElement(List<ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }

        Iterator<ModuleElement<?, ?>> iter = elements.iterator();
        List<ArithmeticInteger> values = new ArrayList<>(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            ModuleElement castElement = iter.next().cast(ZRing.ring);
            if (castElement == null) {
                return null;
            }
            values.add(((ArithmeticElement<ArithmeticInteger>)castElement).getValue());
        }

        return (ZProperFreeElement) ZProperFreeElement.make(values); //TODO do not cast
    }
    
   
    public ZProperFreeElement cast(ModuleElement element) {
        if (element.getLength() == getDimension()) {
            if (element instanceof DirectSumElement) {
                return (ZProperFreeElement)element.cast(this);
            }
            else if (element instanceof ZProperFreeElement) {
                return (ZProperFreeElement)element;
            }
            else {
                List<ArithmeticInteger> values = new ArrayList<>(getDimension());
                for (int i = 0; i < getDimension(); i++) {
                    ModuleElement castElement = element.getComponent(i).cast(ZRing.ring);
                    if (castElement == null) {
                        return null;
                    }
                    values.add(((ArithmeticElement<ArithmeticInteger>)castElement).getValue());
                }
                return (ZProperFreeElement) ZProperFreeElement.make(values);
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

    
    public ZProperFreeElement parseString(String string) {
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
            return (ZProperFreeElement) ZProperFreeElement.make(values);
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


    private static final int basicHash = "ZFreeModule".hashCode();
}
