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
import org.vetronauta.latrunculus.core.math.matrix.ZnMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZnFreeAffineMorphism;

import java.util.Iterator;
import java.util.List;

/**
 * Free modules over integers mod <i>n</i>.
 * @see ZnProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class ZnProperFreeModule extends ProperFreeModule<ArithmeticMultiElement<ArithmeticModulus>, ArithmeticElement<ArithmeticModulus>> implements Modular {

    public static FreeModule<?, ArithmeticElement<ArithmeticModulus>> make(int dimension, int modulus) {
        dimension = Math.max(dimension, 0);
        if (dimension == 0) {
            return new ZnProperFreeModule(0, modulus);
        }
        else if (dimension == 1) {
            return ZnRing.make(modulus);
        }
        else {
            return new ZnProperFreeModule(dimension, modulus);
        }
    }

    
    public ZnProperFreeElement getZero() {
        int[] res = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = 0;
        }
        return (ZnProperFreeElement)ZnProperFreeElement.make(res, modulus);
    }
    

    public ZnProperFreeElement getUnitElement(int i) {
        int[] v = new int[getDimension()];
        v[i] = 1;
        return (ZnProperFreeElement)ZnProperFreeElement.make(v, getModulus());
    }

    
    public Module getNullModule() {
        return make(0, modulus);
    }
    
    
    public boolean isNullModule() {
        return getDimension() == 0;
    }


    public Module getComponentModule(int i) {
        if (componentModule == null) {
            componentModule = ZnRing.make(modulus);
        }
        return componentModule;
    }


    public Ring getRing() {
        return ZnRing.make(modulus);
    }


    public boolean isVectorSpace() {
        return getRing().isField();
    }
    
    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof ZnProperFreeElement &&
                element.getLength() == getDimension() &&
                ((ZnProperFreeElement)element).getModulus() == getModulus());
    }


    public int compareTo(Module object) {
        if (object instanceof ZnProperFreeModule) {
            ZnProperFreeModule module = (ZnProperFreeModule)object;
            int m = getModulus()-module.getModulus();
            if (m != 0) {
                return m;
            }
            else {
	            return getDimension()-module.getDimension();
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public ZnProperFreeElement createElement(List<ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }

        Iterator<ModuleElement<?, ?>> iter = elements.iterator();
        int[] values = new int[getDimension()];        
        for (int i = 0; i < getDimension(); i++) {
            ModuleElement castElement = iter.next().cast(getRing());
            if (castElement == null) {
                return null;
            }
            values[i] = ((ArithmeticElement<ArithmeticModulus>)castElement).getValue().getValue();
        }

        return (ZnProperFreeElement) ZnProperFreeElement.make(values, getModulus()); //TODO do not cast
    }


    public ZnProperFreeElement cast(ModuleElement element) {
        if (element.getLength() == getDimension()) {
            if (element instanceof DirectSumElement) {
                return (ZnProperFreeElement) element.cast(this);
            }
            else if (element instanceof ZProperFreeElement &&
                     ((ZnProperFreeElement)element).getModulus() == modulus) {
                return (ZnProperFreeElement) element;
            }
            else {   
                int[] elements = new int[getDimension()];
                for (int i = 0; i < getDimension(); i++) {
                    ModuleElement castElement = getRing().cast(element.getComponent(i));
                    if (castElement == null) {
                        return null;
                    }
                    elements[i] = ((ArithmeticElement<ArithmeticModulus>)castElement).getValue().getValue();
                }
                return (ZnProperFreeElement) ZnProperFreeElement.make(elements, modulus);
            }
        }
        else {
            return null;
        }
    }

    
    public boolean equals(Object object) {
        return (object instanceof ZnProperFreeModule &&
                getDimension() == ((ZnProperFreeModule)object).getDimension() &&
                modulus == ((ZnProperFreeModule)object).getModulus());
    }

    
    public ZnProperFreeElement parseString(String string) {
        string = TextUtils.unparenthesize(string);
        String[] components = string.trim().split(",");
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
            return (ZnProperFreeElement) ZnProperFreeElement.make(values, getModulus());
        }
    }
    
    
    public String toString() {
        return "ZnFreeModule("+getModulus()+")["+getDimension()+"]";
    }

    
    public String toVisualString() {
        return "Z_"+getModulus()+"^"+getDimension();
    }

    
    public int getModulus() {
        return modulus;
    }
    
    
    public String getElementTypeName() {
        return "ZnFreeElement";
    }

    public int hashCode() {
        return 37*37*basicHash+37*modulus+getDimension();
    }
    
    
    protected ModuleMorphism _getProjection(int index) {
        ZnMatrix A = new ZnMatrix(1, getDimension(), getModulus());
        A.set(0, index, 1);
        return ZnFreeAffineMorphism.make(A, new int[] { 0 });
    }
    
    
    protected ModuleMorphism _getInjection(int index) {
        ZnMatrix A = new ZnMatrix(getDimension(), 1, getModulus());
        A.set(index, 0, 1);
        int[] b = new int[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            b[i] = 0;
        }
        return ZnFreeAffineMorphism.make(A, b);
    }
    
    
    private ZnProperFreeModule(int dimension, int modulus) {
        super(dimension);
        this.modulus = modulus;
    }


    private static final int basicHash = "ZnFreeModule".hashCode();

    private int    modulus;
    private ZnRing componentModule = null;
}
