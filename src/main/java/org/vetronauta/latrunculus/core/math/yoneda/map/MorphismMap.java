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
import org.vetronauta.latrunculus.core.DeepCopyable;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The implementation or "formula" of a morphism.
 * Every morphism contains such a map.
 * 
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public interface MorphismMap extends DeepCopyable<MorphismMap>, Comparable<MorphismMap>, Serializable, MathDefinition {

    /**
     * Evaluates the map at an element.
     * This applies when the morphism map is not null addressed.
     * If it is null addressed, it is returned unchanged.
     * 
     * @param element evaluate at this element
     * @return a new morphism map if there has been any change
     * @throws MappingException if mapping fails
     */
    MorphismMap at(ModuleElement element)
        throws MappingException;

    /**
     * Makes an address change.
     * 
     * @param address the new address of the denotator
     * @return a copy of this morphism with address <code>newAddress</code>
     *         or null if address changed fails
     */
    MorphismMap changeAddress(Module address);
    
    /**
     * Makes an address change using a module morphism.
     * 
     * @param morphism the address changing morphism
     * @return a copy of this morphism with the new address
     *         or null if address change fails
     */
    MorphismMap changeAddress(ModuleMorphism morphism);

    boolean fullEquals(MorphismMap m, Map<Object,Object> s);

    List<Form> getFormDependencies(List<Form> list);

    List<Denotator> getDenotatorDependencies(List<Denotator> list);
   
    boolean resolveReferences(RubatoDictionary dict, Map<Object,Object> history);
}
