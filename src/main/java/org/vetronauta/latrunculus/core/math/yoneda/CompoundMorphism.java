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

import org.rubato.base.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.MappingException;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.IdentityHashMap;
import java.util.LinkedList;


/**
 * General morphism.
 * A morphism is characterized by its domain, codomain and map.
 * Domain and codomain are objects represented through identity morphisms.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class CompoundMorphism extends YonedaMorphism {

    /**
     * Creates a morphism between two general objects.
     * For morphisms between modules, see other constructors below.
     */
    public CompoundMorphism(IdentityMorphism domain, IdentityMorphism codomain, MorphismMap map) {
        this(map);
        this.domain = domain;
        this.codomain = codomain;
    }

    
    /**
     * Creates a morphism with a module domain and an arbitrary codomain.
     */
    public CompoundMorphism(Module domain, IdentityMorphism codomain, MorphismMap map) {
        this(new RepresentableIdentityMorphism(domain), codomain, map);
    }
    
    
    /**
     * Creates a morphism with module domain and codomain.
     */
    public CompoundMorphism(Module domain, Module codomain, MorphismMap map) {
        this(new RepresentableIdentityMorphism(domain), new RepresentableIdentityMorphism(codomain), map);
    }
    
    
    public Diagram getDomainDiagram() {
        return domain.getDiagram();
    }
    
    
    public Diagram getCodomainDiagram() {
        return codomain.getDiagram();
    }

    
    public Module getDomainModule() {
        return domain.getModule();
    }
    
    
    public Module getCodomainModule() {
        return codomain.getModule();
    }
    
    
    public int getDomainType() {
        return domain.getType();
    }
    
    
    public int getCodomainType() {
        return codomain.getType();
    }
    
    
    public YonedaMorphism changeAddress(Module address) {
        MorphismMap newMap = map.changeAddress(address);
        if (newMap != null) {
            return new CompoundMorphism(address, codomain, newMap);
        }
        else {
            return null;
        }
    }

    
    public YonedaMorphism changeAddress(ModuleMorphism morphism) {
        MorphismMap newMap = map.changeAddress(morphism);
        if (newMap != null) {
            return new CompoundMorphism(morphism.getDomain(), codomain, newMap);
        }
        else {
            return null;
        }
    }
    
    
    public MorphismMap getMap() {
        return map;
    }

    
    public void setMap(MorphismMap map) {
        this.map = map;
    }
    
    
    public YonedaMorphism at(ModuleElement element)
            throws MappingException {
        MorphismMap newMap = map.at(element);
        if (map == newMap) {
            return this;
        }
        else {
            // an evaluated morphism is null addressed
            return new CompoundMorphism(domain.getDomainModule().getNullModule(), codomain, newMap);
        }
    }

    
    public CompoundMorphism copy() {
        return new CompoundMorphism(domain, codomain, map.deepCopy());
    }

    
    public IdentityMorphism getDomain() {
        return domain;
    }
    
    
    public IdentityMorphism getCodomain() {
        return codomain;
    }
    
    
    public int compareTo(YonedaMorphism object) {
        if (this == object) {
            return 0;
        }
        else if (object instanceof CompoundMorphism) {
            int c = domain.compareTo(object.getDomain());
            if (c != 0) {
                return c;
            }
            c = codomain.compareTo(object.getCodomain());
            if (c != 0) {
                return c;
            }
            return map.compareTo(object.getMap());
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof CompoundMorphism) {
            CompoundMorphism m = (CompoundMorphism)object;
            return domain.equals(m.domain) &&
                   codomain.equals(m.codomain) &&
                   map.equals(m.map);
        }
        else {
            return false;
        }
    }
    
    
    public boolean fullEquals(YonedaMorphism m, IdentityHashMap<Object,Object> s) {
        if (this == m) {
            return true;
        }
        else if (!(m instanceof CompoundMorphism)) {
            return false;
        }
        else {
            CompoundMorphism cm = (CompoundMorphism)m;
            return domain.fullEquals(cm.domain, s) &&
                   codomain.fullEquals(cm.codomain, s) &&
                   map.fullEquals(cm.map, s);
        }
    }


    public LinkedList<Form> getFormDependencies(LinkedList<Form> list) {
        return map.getFormDependencies(list); 
    }
    
    
    public LinkedList<Denotator> getDenotatorDependencies(LinkedList<Denotator> list) {
        return map.getDenotatorDependencies(list); 
    }
    
    
    public String toString() {
        return "CompoundMorphism["+domain+","+codomain+","+map+"]";
    }

    public String getElementTypeName() {
        return "CompoundMorphism";
    }

    public int hashCode() {
        int hash = 7;
        hash = 37*hash + domain.hashCode();
        hash = 37*hash + codomain.hashCode();
        hash = 37*hash + map.hashCode();
        return hash;
    }
    

    public boolean resolveReferences(RubatoDictionary dict, IdentityHashMap<?,?> history) {
        return (domain.resolveReferences(dict, history) &&
                codomain.resolveReferences(dict, history) &&
                map.resolveReferences(dict, history));
    }
    
    
    /**
     * Creates a morphism with the given map.
     * If map == null, use an empty morphism map. 
     */
    protected CompoundMorphism(MorphismMap map) { 
        if (map != null) {
            this.map = map;
        }
        else {
            this.map = EmptyMorphismMap.emptyMorphismMap;
        }
    }
    

    protected int getMorphOrder() {
        return 0xAFFE; 
    }

    
    private IdentityMorphism domain;
    private IdentityMorphism codomain;    
    private MorphismMap      map;    
}
