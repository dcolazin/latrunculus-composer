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

package org.vetronauta.latrunculus.core.math.yoneda.form;

import org.rubato.base.RubatoException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.ProperIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Power form class.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class PowerForm extends Form {

    /**
     * Generic power form constructor.
     */
    public PowerForm(NameDenotator name, YonedaMorphism identifier) {
        super(name, identifier);
    }


    /**
     * Builds a power identity form.
     */
    public PowerForm(NameDenotator name, Form form) {
        super(name, new ProperIdentityMorphism(new FormDiagram(form), FormDenotatorTypeEnum.POWER));
    }

    
    /**
     * Returns the type of the form.
     */
    public FormDenotatorTypeEnum getType() {
        return FormDenotatorTypeEnum.POWER;
    }


    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof PowerForm) {
            return equals((PowerForm)object);
        }
        else {
            return false;
        }
    }


    /**
     * Tests for equality of two power forms.
     */
    public boolean equals(PowerForm f) {
        if (registered && f.registered) {
            return getName().equals(f.getName());
        }
        if (this == f) {
            return true;
        }
        if (!getName().equals(f.getName())) {
            return false;
        }
        Map<Object,Object> map = new HashMap<>();
        map.put(this, f);
        return identifier.fullEquals(f.identifier, map);
    }
    
    /**
     * Returns the number of coordinate forms.
     */
    public int getFormCount() {
        return getFormDiagram().getFormCount();
    }
    

    /**
     * Returns a coordinate form.
     * @param i the coordinate position
     * @return the form at coordinate position i
     */
    public Form getForm(int i) {
        return getFormDiagram().getForm(0);
    }

    
    /**
     * Returns the single coordinate form.
     */
    public Form getForm() {
        return getForm(0);
    }


    public FormDiagram getFormDiagram() {
        return (FormDiagram)getIdentifier().getCodomainDiagram();
    }


    public List<Form> getDependencies(List<Form> list) {
        if (!list.contains(this)) {
            list.add(this);
            return identifier.getFormDependencies(list);
        }
        return list;
    }
        
    /**
     * Returns a default denotator of this power form.
     */
    public Denotator createDefaultDenotator() {
        Denotator res = null;
        try {
            res = new PowerDenotator(null, this, new LinkedList<>());
        } 
        catch (RubatoException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * Returns a default denotator of this power form with the given address.
     */
    public Denotator createDefaultDenotator(Module address) {
        Denotator res = null;
        try {
            res = new PowerDenotator(null, address, this, new LinkedList<>());
        } 
        catch (RubatoException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    public String toString() {
        return "["+getNameString()+":.power("+getForm().getNameString()+")]";
    }

    protected double getDimension(int maxDepth, int depth) {
        if (depth > maxDepth) return 1.0;
        
        Form f = getForm(0);
        return f.getDimension(maxDepth, depth + 1);
    }
}
