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

package org.vetronauta.latrunculus.core.math.module.morphism;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiModule;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRingRepository;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnProperFreeElement;

/**
 * The function that takes an element <i>i</i> in <i>Z^d</i> 
 * to <i>i</i> mod <i>n</i> in <i>Z_n^d</i>.
 * To create instances use the {@link #make(int)} or 
 * {@link #make(int,int)} methods.
 * 
 * @author Gérard Milmeister
 */
public class ModuloMorphism extends ModuleMorphism {

    /**
     * Creates a map that takes an integer <i>i</i> to <i>i</i> mod <i>n</i>.
     * The domain is ℤ, the codomain ℤ<sub><i>n</i></sub>, where <i>n</i>
     * is the specified <code>modulus</code>.
     * 
     * @param modulus is at least 2, lower values will be set to 2
     */
    public static ModuloMorphism make(int modulus) {
        return make(1, modulus);
    }
    
    
    /**
     * Creates a map that takes an element <i>i</i> of Z^d to <i>i</i> mod <i>n</i>
     * in Z_n^d.
     * 
     * @param dim is at least 0, lower values will be set to 0
     * @param modulus is at least 2, lower values will be set to 2
     * 
     */
    public static ModuloMorphism make(int dim, int modulus) {
        if (modulus < 1) {
            modulus = 2;
        }
        if (dim < 0) {
            modulus = 0;
        }
        return new ModuloMorphism(dim, modulus);
    }
    
    
    public ModuleElement map(ModuleElement x)
            throws MappingException {
        if (dimension == 1) {
            if (x instanceof ArithmeticElement) {
                ArithmeticNumber<?> number = ((ArithmeticElement<?>) x).getValue();
                if (number instanceof ArithmeticInteger) {
                    return new ArithmeticElement<>(new ArithmeticModulus(number.intValue(), modulus));
                }
            }
        }
        else if (x instanceof ZProperFreeElement) {
            ZProperFreeElement e = (ZProperFreeElement)x;
            if (e.getLength() == dimension) {
                return ZnProperFreeElement.make(e.getValue(), modulus);
            }
        }
        throw new MappingException("ModuloMorphism.map: ", x, this);
    }

    
    public boolean isModuleHomomorphism() {
        return false;
    }

    
    public boolean isRingHomomorphism() {
        return getDimension() == 1;
    }
    
    
    public boolean isLinear() {
        return getDimension() == 0;
    }
    
    
    public boolean isConstant() {
        return getDimension() == 0;
    }

    
    /**
     * Returns the modulus of the codomain.
     */
    public final int getModulus() {
        return modulus;
    }
    
    
    /**
     * Returns the dimension of the domain and codomain.
     */
    public final int getDimension() {
        return dimension;
    }
    
    
    public ModuleMorphism getRingMorphism() {
        if (getDimension() == 1) {
            return this;
        }
        else {
            return ModuloMorphism.make(1, getModulus());
        }
    }

    
    public ModuleElement atZero() {
        return ArithmeticMultiModule.make(ArithmeticRingRepository.getModulusRing(getModulus()), getDimension()).getZero();
    }

    
    public int compareTo(ModuleMorphism object) {
        if (object instanceof ModuloMorphism) {
            ModuloMorphism m = (ModuloMorphism)object;
            return getModulus()-m.getModulus();
        }
        else {
            return super.compareTo(object);
        }
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof ModuloMorphism) {
            ModuloMorphism m = (ModuloMorphism)object;
            return getModulus() == m.getModulus() && getDimension() == m.getDimension();
        }
        else {
            return false;
        }
    }
    
    
    public String toString() {
        return "ModuloMorphism["+getDimension()+","+getModulus()+"]";
    }

    public String getElementTypeName() {
        return "ModuloMorphism";
    }
    
    
    private ModuloMorphism(int dim, int modulus) {
        super(ArithmeticMultiModule.make(ZRing.ring, dim), ArithmeticMultiModule.make(ArithmeticRingRepository.getModulusRing(modulus), dim));
        this.modulus = modulus;
        this.dimension = dim;
    }

    
    private final int modulus;
    private final int dimension;
}
