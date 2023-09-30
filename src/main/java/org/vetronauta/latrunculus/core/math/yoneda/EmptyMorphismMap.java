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

package org.vetronauta.latrunculus.core.math.yoneda;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.MORPHISM_MAP;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

import java.util.IdentityHashMap;
import java.util.LinkedList;

import org.rubato.base.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;

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

    
    public void toXML(XMLWriter writer) {        
        writer.emptyWithType(MORPHISM_MAP, getElementTypeName());
    }
    
    
    public MorphismMap fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        return emptyMorphismMap;
    }
    
    
    public String getElementTypeName() {
        return "EmptyMorphismMap";
    }
    

    public Object clone() {
        return copy();
    }


    public EmptyMorphismMap copy() {
        return this;
    }


    public boolean equals(Object object) {
        if (object instanceof EmptyMorphismMap) {
            return true;
        }
        else {
            return false;
        }
    }
   

    public LinkedList<Form> getFormDependencies(LinkedList<Form> list) {
        return list;
    }
    
    
    public LinkedList<Denotator> getDenotatorDependencies(LinkedList<Denotator> list) {
        return list;
    }
    
    
    public boolean resolveReferences(RubatoDictionary dict, IdentityHashMap<?,?> history) {
        return true;
    }
    
    
    public boolean fullEquals(MorphismMap map, IdentityHashMap<Object,Object> s) {
        return this.equals(map);
    }
    

    public int hashCode() {
        return hashCode;
    }
    
    
    private EmptyMorphismMap() { /* not allowed */ }
    
    private int hashCode = "EmptyMorpismMap".hashCode();
}
