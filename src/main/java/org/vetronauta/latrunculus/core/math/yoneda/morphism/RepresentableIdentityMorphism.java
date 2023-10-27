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
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

import java.util.List;
import java.util.Map;

/**
 * Identity morphism representing a module "object".
 * Thus it is "representable". 
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class RepresentableIdentityMorphism<A extends ModuleElement<A,R>, R extends RingElement<R>> extends IdentityMorphism {

    private final Module<A,R> module;
    private final ModuleElement<A,R> lowValue;
    private final ModuleElement<A,R> highValue;

    /**
     * Creates an identity morphism representing the given module.
     */
    public RepresentableIdentityMorphism(Module<A,R> module) {
        this.module = module;
        this.lowValue = null;
        this.highValue = null;
    }

    /**
     * Creates an identity morphism representing the given module.
     * This variant specifies lower and upper bounds for the values
     * contained in the module.
     */
    public RepresentableIdentityMorphism(Module<A,R> module, ModuleElement<A,R> lowValue, ModuleElement<A,R> highValue) {
        this.module = module;
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    public Diagram getDiagram() {
        return FormDiagram.emptyFormDiagram;
    }


    public Module<A,R> getModule() {
        return module;
    }
    

    public ModuleElement<A,R> getLowValue() {
        return lowValue;
    }

    
    public ModuleElement<A,R> getHighValue() {
        return highValue;
    }

    public boolean hasBounds() {
        return (lowValue != null) && (highValue != null);
    }

    public FormDenotatorTypeEnum getType() {
        return FormDenotatorTypeEnum.SIMPLE;
    }

    public boolean isRepresentable() {
        return true;
    }
    

    public YonedaMorphism at(ModuleElement element) {
        return this;
    }

    
    public YonedaMorphism changeAddress(Module address) {
        return this;
    }


    public YonedaMorphism changeAddress(ModuleMorphism morphism) {
        return this;
    }

    @Override
    public RepresentableIdentityMorphism<A,R> deepCopy() {
        return new RepresentableIdentityMorphism<A,R>(module, lowValue, highValue);
    }

    @Override
    public int compareTo(YonedaMorphism object) {
        if (this == object) {
            return 0;
        }
        else if (object instanceof RepresentableIdentityMorphism) {
            RepresentableIdentityMorphism m = (RepresentableIdentityMorphism)object;
            int c = module.compareTo(m.getModule());
            if (c == 0) {
                if (highValue != null) {
                    if (m.highValue != null) {
                        int comp1 = highValue.compareTo(m.highValue);
                        if (comp1 == 0) {
                            return lowValue.compareTo(m.lowValue);
                        }
                        else {
                            return comp1;
                        }
                    }
                    else {
                        return 1;
                    }                    
                }
            }
            return c;
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        else if (object instanceof RepresentableIdentityMorphism) {
            RepresentableIdentityMorphism m = (RepresentableIdentityMorphism)object;
            if (module.equals(m.module)) {
                return compareBounds(m);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    
    public boolean fullEquals(YonedaMorphism m, Map<Object,Object> s) {
        return equals(m);
    }
    
    
    public List<Form> getFormDependencies(List<Form> list) {
        return list;
    }
    
    
    public List<Denotator> getDenotatorDependencies(List<Denotator> list) {
        return list;
    }
    
    
    public String toString() {
        return "RepresentableIdentityMorphism["+module+"]";
    }
    
    public String getElementTypeName() {
        return "RepresentableIdentityMorphism";
    }
    
    
    public int hashCode() {
        int hash = 7;
        hash = 37*hash + module.hashCode();
        if (lowValue != null) {
            hash = 37*hash + lowValue.hashCode();
        }
        if (highValue != null) {
            hash = 37*hash + highValue.hashCode();
        }
        return hash;
    }
    

    public int getMorphOrder() {
        return 0xEDDA;
    }


    public boolean resolveReferences(RubatoDictionary dict, Map<Object,Object> history) {
        return true;
    }
    
    
    private boolean compareBounds(RepresentableIdentityMorphism m) {
        if (hasBounds() && m.hasBounds()) {
            return lowValue.equals(m.lowValue) && highValue.equals(m.highValue);
        }
        else {
            return hasBounds() == m.hasBounds();
        }
    }

}
