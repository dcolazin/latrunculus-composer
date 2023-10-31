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

package org.vetronauta.latrunculus.core.math.module.definition;

import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.Iterator;
import java.util.List;

/**
 * Free modules over a product ring.
 * @see ProductProperFreeElement
 * 
 * @author Gérard Milmeister
 */
public final class ProductProperFreeModule extends ProperFreeModule<ProductProperFreeElement,ProductElement> {

    public static FreeModule<?, ProductElement> make(Ring[] rings, int dimension) {
        dimension = Math.max(dimension, 0);
        if (dimension == 1) {
            return ProductRing.make(rings);
        }
        else {
            if (rings.length == 1) {
                return rings[0].getFreeModule(dimension);
            }
            else {
                return new ProductProperFreeModule(rings, dimension);
            }
        }
    }

    
    public static FreeModule<?, ProductElement> make(ProductRing ring, int dimension) {
        if (dimension == 1) {
            return ring;
        }
        else {
            return new ProductProperFreeModule(ring, dimension);
        }
    }

    
    public ProductProperFreeElement getZero() {
        ProductElement[] res = new ProductElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = ring.getZero();
        }
        return (ProductProperFreeElement) ProductProperFreeElement.make(ring, res);
    }


    public ProductProperFreeElement getUnitElement(int i) {
        ProductElement[] v = new ProductElement[getDimension()];
        for (int j = 0; j < getDimension(); j++) {
            v[j] = ProductElement.make(new RingElement[getDimension()]);
        }
        v[i] = getRing().getOne();
        return (ProductProperFreeElement)ProductProperFreeElement.make(getRing(), v);
    }
    

    public ProductProperFreeModule getNullModule() {
        return (ProductProperFreeModule)make(ring, 0);
    }
    
    
    public boolean isNullModule() {
        return getDimension() == 0;
    }

    
    public ProductRing getComponentModule(int i) {
        return ring;
    }


    public ProductRing getRing() {
        return ring;
    }


    public int getFactorCount() {
        return getRing().getFactorCount();
    }

    
    public Ring[] getFactors() {
        return getRing().getFactors();
    }

    
    public Ring getFactor(int i) {
        return getRing().getFactor(i);
    }

    
    public boolean isVectorSpace() {
        return ring.isField();
    }

    
    public boolean hasElement(ModuleElement element) {
        return element.getModule().equals(this);
    }


    public int compareTo(Module object) {
        if (object instanceof ProductProperFreeModule) {
            ProductProperFreeModule m = (ProductProperFreeModule)object;
            int c = getRing().compareTo(m.getRing());
            if (c != 0) {
                return c;
            }
            else {
                return getDimension()-m.getDimension();
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public ProductProperFreeElement createElement(List<? extends ModuleElement<?, ?>> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }

        ProductElement[] components = new ProductElement[getDimension()];
        Iterator<? extends ModuleElement<?, ?>> iter = elements.iterator();
        for (int i = 0; i < getDimension(); i++) {
            ModuleElement object = iter.next();
            if (object instanceof ProductElement) {
                ProductElement productElement = getRing().cast(object);
                if (productElement == null) {
                    return null;
                }
                else {
                    components[i] = productElement;
                }
            }
            else {
                return null;
            }
        }
        return (ProductProperFreeElement) ProductProperFreeElement.make(getRing(), components);
    }

    
    public ProductProperFreeElement cast(ModuleElement element) {
        if (element.getLength() >= getDimension()) {
            ProductElement[] components = new ProductElement[getDimension()];
            for (int i = 0; i < getDimension(); i++) {
                ModuleElement component = element.getComponent(i);
                ProductElement productElement = getRing().cast(component);
                if (productElement == null) {
                    return null;
                }
                components[i] = productElement;
            }
            return (ProductProperFreeElement)ProductProperFreeElement.make(getRing(), components);
        }
        else {
            return null;
        }
    }


    public boolean equals(Object object) {
        if (object instanceof ProductProperFreeModule) {
            ProductProperFreeModule m = (ProductProperFreeModule)object;
            if (getDimension() != m.getDimension()) {
                return false;
            }
            else if (!getRing().equals(m.getRing())) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    public String toString() {
        return "ProductFreeModule["+getDimension()+"]["+getRing()+"]";
    }

    
    public String toVisualString() {
        return "("+getRing().toVisualString()+")"+"^"+getDimension();
    }
    
    public String getElementTypeName() {
        return "ProductFreeModule";
    }

    public int hashCode() {
        if (hashcode == 0) {
            hashcode = 37*basicHash+7*ring.hashCode()+getDimension();
        }
        return hashcode;
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

    
    private ProductProperFreeModule(Ring[] rings, int dimension) {
        super(dimension);
        this.ring = ProductRing.make(rings);
    }


    private ProductProperFreeModule(ProductRing ring, int dimension) {
        super(dimension);
        this.ring = ring; 
    }

    
    private ProductRing ring;
    
    private static final int basicHash = "ProductFreeModule".hashCode();
    int hashcode = 0;
}
