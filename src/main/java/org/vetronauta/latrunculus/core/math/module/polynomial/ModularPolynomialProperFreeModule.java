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

import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.Iterator;
import java.util.List;

/**
 * Free modules over modular polynomials.
 * 
 * @author Gérard Milmeister
 */
public final class ModularPolynomialProperFreeModule<B extends RingElement<B>>
        extends ProperFreeModule<ModularPolynomialProperFreeElement<B>,ModularPolynomialElement<B>> {

    public static <X extends RingElement<X>> FreeModule<?,ModularPolynomialElement<X>> make(PolynomialElement<X> modulus, int dimension) {
        dimension = Math.max(dimension, 0);
        if (dimension == 1) {
            return ModularPolynomialRing.make(modulus);
        }
        else {
            if (modulus.getCoefficientRing().isField()) {
                return new ModularPolynomialProperFreeModule<>(modulus, dimension);
            }
            else {
                return null;
            }
        }
    }

    
    public ModularPolynomialProperFreeElement<B> getZero() {
        ModularPolynomialElement[] res = new ModularPolynomialElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = ring.getZero();
        }
        return (ModularPolynomialProperFreeElement<B>) ModularPolynomialProperFreeElement.make(ring, res);
    }
    
    
    public ModularPolynomialProperFreeElement<B> getUnitElement(int i) {
        ModularPolynomialElement[] v = new ModularPolynomialElement[getDimension()];
        for (int j = 0; j < getDimension(); j++) {
            v[j] = getRing().getZero();
        }
        v[i] = getRing().getOne();
        return (ModularPolynomialProperFreeElement<B>) ModularPolynomialProperFreeElement.make(getRing(), v);
    }
    

    public FreeModule<?,ModularPolynomialElement<B>>  getNullModule() {
        return make(getModulus(), 0);
    }
    
    
    public boolean isNullModule() {
        return this.getDimension() == 0;
    }

    
    public ModularPolynomialRing getComponentModule(int i) {
        return ring;
    }

    
    public ModularPolynomialRing getRing() {
        return ring;
    }


    public Ring getCoefficientRing() {
        return ring.getCoefficientRing();
    }


    public String getIndeterminate() {
        return ring.getIndeterminate();
    }


    public boolean isVectorSpace() {
        return false;
    }

    
    public PolynomialElement getModulus() {
        return ring.getModulus();
    }
    
    
    public PolynomialRing getModulusRing() {
        return ring.getModulus().getRing();
    }
    
    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof ModularPolynomialProperFreeElement &&
                element.getLength() == getDimension() &&
                ring.equals(((ModularPolynomialElement)element).getRing()));
    }


    public int compareTo(Module object) {
        if (object instanceof ModularPolynomialProperFreeModule) {
            ModularPolynomialProperFreeModule module = (ModularPolynomialProperFreeModule)object;
            int c = getRing().compareTo(module.getRing());
            if (c != 0) {
                return c;
            }
            int d = getDimension()-module.getDimension();
            if (d != 0) {
                return d;
            }
            return 0;
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public ModularPolynomialProperFreeElement<B> createElement(List<? extends ModuleElement<?,?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        ModularPolynomialElement[] values = new ModularPolynomialElement[getDimension()];
        Iterator<? extends ModuleElement<?, ?>> iter = elements.iterator();
        for (int i = 0; i < getDimension(); i++) {
            values[i] = ring.cast(iter.next());
            if (values[i] == null) {
                return null;
            }
        }
        return (ModularPolynomialProperFreeElement)ModularPolynomialProperFreeElement.make(ring, values);
    }
    
   
    public ModularPolynomialProperFreeElement cast(ModuleElement element) {
        int dim = element.getLength();
        if (dim != getDimension()) {
            return null;
        }
        ModularPolynomialElement[] values = new ModularPolynomialElement[dim];
        for (int i = 0; i < dim; i++) {
            values[i] = ring.cast(element.getComponent(i));
            if (values[i] == null) {
                return null;
            }
        }
        return (ModularPolynomialProperFreeElement)ModularPolynomialProperFreeElement.make(ring, values);
    }

    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ModularPolynomialProperFreeModule) {
            ModularPolynomialProperFreeModule m = (ModularPolynomialProperFreeModule)object;
            if (getDimension() != m.getDimension()) {
                return false;
            }
            return getRing().equals(m.getRing());
        }
        return false;
    }
    
    public String toString() {
        return "ModularPolynomialFreeModule["+getRing()+","+getDimension()+"]";
    }

    
    public String toVisualString() {
        return "("+getRing()+")^"+getDimension();
    }

    public String getElementTypeName() {
        return "ModularPolynomialFreeModule";
    }
    
    
    public int hashCode() {
        return (37*basicHash + getDimension()) ^ ring.hashCode();
    }
    
    
    protected ModuleMorphism _getProjection(int index) {
        GenericAffineMorphism m = GenericAffineMorphism.make(getRing(), getDimension(), 1);
        m.setMatrix(0, index, getRing().getOne());
        return m;
    }
    
    
    protected ModuleMorphism _getInjection(int index) {
        GenericAffineMorphism m = GenericAffineMorphism.make(getRing(), 1, getDimension());
        m.setMatrix(index, 0, getRing().getOne());
        return m;
    }

    
    private ModularPolynomialProperFreeModule(PolynomialElement modulus, int dimension) {
        super(dimension);
        this.ring = ModularPolynomialRing.make(modulus);
        
    }

    
    private ModularPolynomialRing ring;
    
    private static final int basicHash = "PolynomialFreeModule".hashCode();    
}
