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

import org.rubato.base.RubatoDictionary;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.DenotatorReference;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

import java.util.List;
import java.util.Map;

/**
 * Morphism map containing a morphism and an index (for type colimit).
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class IndexMorphismMap implements MorphismMap {

    /**
     * Creates an IndexMorphismMap.
     */
    public IndexMorphismMap(int index, Denotator factor) {
        this.index = index;
        this.factor = factor;
    }
    
    
    /**
     * Creates an empty IndexMorphismMap.
     */
    public IndexMorphismMap() {
        index = -1;
        factor = null;
    }
    
    
    /**
     * Returns the index of the factor contained in the map.
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * Returns the factor contained in the map.
     */
    public Denotator getFactor() {
        return factor;
    }

    /**
     * Sets the factor with the given index in the map.
     */
    public void setFactor(int index, Denotator factor) {
        this.index = index;
        this.factor = factor;
    }   
    

    public int compareTo(MorphismMap object) {        
        if (this == object) {
            return 0;
        }
        else if (object instanceof IndexMorphismMap) {
            return compareTo((IndexMorphismMap)object);
        }
        else {
            return getClass().toString().compareTo(object.getClass().toString());
        }
    }

    
    public int compareTo(IndexMorphismMap other) {    
        final int aIndex = this.getIndex();        
        final int bIndex = other.getIndex();
        if (aIndex == bIndex) {
            return this.getFactor().compareTo(other.getFactor());        
        }
        else {
            return aIndex-bIndex;
        }
    }

    public boolean fullEquals(MorphismMap map, Map<Object,Object> s) {
        if (this == map) {
            return true;
        }
        else if (map instanceof IndexMorphismMap) {
            IndexMorphismMap m = (IndexMorphismMap)map;
            if (getIndex() != m.getIndex()) {
                return false;
            }
            return getFactor().equals(m.getFactor());
        }
        return false;
    }
    

    public MorphismMap at(ModuleElement element)
            throws MappingException {
        Denotator d = factor.at(element);
        if (d == factor) {            
            return this;
        }
        else {
            return new IndexMorphismMap(index, d);
        }
    }
    

    public MorphismMap changeAddress(Module address) {
        Denotator d = factor.changeAddress(address);
        if (d != null) {
            return new IndexMorphismMap(index, d);
        }
        else {
            return null;
        }
    }
    
    
    public MorphismMap changeAddress(ModuleMorphism morphism) {
        Denotator d = factor.changeAddress(morphism);
        if (d != null) {
            return new IndexMorphismMap(index, d);
        }
        else {
            return null;
        }
    }
    
    public String getElementTypeName() {
        return "IndexMorphismMap";
    }
    
    
    public List<Form> getFormDependencies(List<Form> list) {
        return list;
    }
    
    
    public List<Denotator> getDenotatorDependencies(List<Denotator> list) {
        return factor.getDependencies(list);
    }
    
    
    public boolean resolveReferences(RubatoDictionary dict, Map<Object,Object> history) {
        Denotator d = getFactor();
        if (d instanceof DenotatorReference) {
            Denotator newDenotator = dict.getDenotator(d.getNameString());
            if (newDenotator == null) {
                return false;
            }
            setFactor(getIndex(), newDenotator);
            return true;
        }
        else {
            return d.resolveReferences(dict, history);
        }
    }
    
    
    public boolean isConstant() {
        return factor.isConstant();
    }

    @Override
    public IndexMorphismMap deepCopy() {
        return new IndexMorphismMap(index, factor.deepCopy());
    }

    public boolean equals(Object object) {
        if (object instanceof IndexMorphismMap) {
            return equals((IndexMorphismMap)object);
        }
        else {
            return false;
        }
    }
    
    
    public boolean equals(IndexMorphismMap other) {
        return (getIndex() == other.getIndex() &&
                getFactor().equals(other.getFactor()));
    }

    
    public int hashCode() {
        int hash = 7;
        hash = 37*hash+index;
        hash = 37*hash+factor.hashCode();
        return hash;
    }


    private int       index;
    private Denotator factor;
}
