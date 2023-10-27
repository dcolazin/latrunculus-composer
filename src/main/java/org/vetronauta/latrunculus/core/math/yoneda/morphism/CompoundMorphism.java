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

package org.vetronauta.latrunculus.core.math.yoneda.morphism;

import org.rubato.base.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.map.EmptyMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.MorphismMap;

import java.util.List;
import java.util.Map;


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

    private final IdentityMorphism domain;
    private final IdentityMorphism codomain;
    private MorphismMap map;

    /**
     * Creates a morphism between two general objects.
     * For morphisms between modules, see other constructors below.
     */
    public CompoundMorphism(IdentityMorphism domain, IdentityMorphism codomain, MorphismMap map) {
        this.map = map != null ? map : EmptyMorphismMap.emptyMorphismMap;
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
    
    
    public FormDenotatorTypeEnum getDomainType() {
        return domain.getType();
    }
    
    
    public FormDenotatorTypeEnum getCodomainType() {
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

    @Override
    public CompoundMorphism deepCopy() {
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
    
    
    public boolean fullEquals(YonedaMorphism m, Map<Object,Object> s) {
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


    public List<Form> getFormDependencies(List<Form> list) {
        return map.getFormDependencies(list); 
    }
    
    
    public List<Denotator> getDenotatorDependencies(List<Denotator> list) {
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
    

    public boolean resolveReferences(RubatoDictionary dict, Map<Object,Object> history) {
        return (domain.resolveReferences(dict, history) &&
                codomain.resolveReferences(dict, history) &&
                map.resolveReferences(dict, history));
    }


    protected int getMorphOrder() {
        return 0xAFFE; 
    }

}
