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

package org.vetronauta.latrunculus.core.math.morphism;

import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.morphism.endo.Endomorphism;

/**
 * Morphism that represents a translation in an arbitrary module.
 * 
 * @author Gérard Milmeister
 */
public final class TranslationMorphism<A extends ModuleElement<A,RA>, RA extends RingElement<RA>> extends Endomorphism<A,RA> {

    private final A translate;

    public TranslationMorphism(A translate) {
        super(translate.getModule());
        this.translate = translate;
    }

    @Override
    public A map(A x) {
        return x.sum(translate);
    }

    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }

    @Override
    public boolean isRingHomomorphism() {
        return getDomain().isRing() && translate.isZero(); 
    }
    
    @Override
    public boolean isLinear() {
        return translate.isZero();
    }
    
    @Override
    public boolean isIdentity() {
        return translate.isZero();
    }
    
    @Override
    public IdentityMorphism<RA, RA> getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }

    /**
     * Returns the translate <i>t</i> of <i>h(x) = x+t</i>.
     */
    public A getTranslate() {
        return translate;
    }


    @Override
    public int compareTo(ModuleMorphism object) {
        if (object instanceof TranslationMorphism) {
            TranslationMorphism<?,?> morphism = (TranslationMorphism<?,?>)object;
            return translate.compareTo(morphism.translate);
        }
        return super.compareTo(object);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof TranslationMorphism) {
            TranslationMorphism<?,?> morphism = (TranslationMorphism<?,?>)object;
            return translate.equals(morphism.translate);
        }
        else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "TranslationMorphism["+translate+"]";
    }

    public String getElementTypeName() {
        return "TranslationMorphism"; 
    }

}
