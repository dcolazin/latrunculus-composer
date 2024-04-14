/*
 * Copyright (C) 2002, 2005 Gérard Milmeister
 * Copyright (C) 2002 Stefan Müller
 * Copyright (C) 2002 Stefan Göller
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

package org.vetronauta.latrunculus.core.math.yoneda.map;

import org.vetronauta.latrunculus.core.repository.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

import java.util.List;
import java.util.Map;

/**
 * Empty morphism map to avoid null references.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class EmptyMorphismMap implements MorphismMap {

    public static final EmptyMorphismMap emptyMorphismMap = new EmptyMorphismMap();
    
    public int compareTo(MorphismMap object) {
        if (object == this) {
            return 0;
        }
        else {
            return -1;
        }
    }

    
    public MorphismMap at(ModuleElement element) {
        return this;
    }


    public MorphismMap changeAddress(Module address) {
        return this;
    }


    public MorphismMap changeAddress(ModuleMorphism morphism) {
        return this;
    }
    
    public String getElementTypeName() {
        return "EmptyMorphismMap";
    }

    @Override
    public EmptyMorphismMap deepCopy() {
        return this; //TODO is this correct?
    }


    public boolean equals(Object object) {
        return object instanceof EmptyMorphismMap;
    }
   

    public List<Form> getFormDependencies(List<Form> list) {
        return list;
    }
    
    
    public List<Denotator> getDenotatorDependencies(List<Denotator> list) {
        return list;
    }
    
    
    public boolean resolveReferences(RubatoDictionary dict, Map<Object,Object> history) {
        return true;
    }
    
    
    public boolean fullEquals(MorphismMap map, Map<Object,Object> s) {
        return this.equals(map);
    }
    

    public int hashCode() {
        return hashCode;
    }
    
    
    private EmptyMorphismMap() { /* not allowed */ }
    
    private int hashCode = "EmptyMorpismMap".hashCode();
}
