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

import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.RepresentableIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple form class.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class SimpleForm extends Form {

    /**
     * Generic simple form constructor.
     */
    public SimpleForm(NameDenotator name, YonedaMorphism identifier) {
        super(name, identifier);
    }


    /**
     * simple identity form constructor.
     */
    public SimpleForm(NameDenotator name, Module m) {
        super(name, new RepresentableIdentityMorphism(m));
    }

    
    /**
     * simple identity form constructor with a range (for folding).
     */
    public SimpleForm(NameDenotator name, Module m, ModuleElement lo, ModuleElement hi) {
        super(name, new RepresentableIdentityMorphism(m, lo, hi));
    }
        

    /**
     * Returns the type of the form.
     */
    public FormDenotatorTypeEnum getType() {
        return FormDenotatorTypeEnum.SIMPLE;
    }
    

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof SimpleForm) {
            return equals((SimpleForm)object);
        }
        else {
            return false;
        }
    }


    public boolean equals(SimpleForm f) {
        if (registered && f.registered) {
            return getName().equals(f.getName());
        }
        if (this == f) {
            return true;
        }
        else if (!getName().equals(f.getName())) {
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
        return 0;
    }
    

    /**
     * Returns a coordinate form.
     * @param i the coordinate position
     * @return the form at coordinate position i
     */
    public Form getForm(int i) {
         throw new IllegalStateException("Forms of type simple have no coordinate forms");
    }

    
    /**
     * Returns a default denotator of this simple form.
     */
    public Denotator createDefaultDenotator() {
        Denotator res = null;
        try {
            res = new SimpleDenotator(null, this, getModule().getZero());
        }
        catch (DomainException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * Returns a default denotator of this simple form with the given address.
     */
    public Denotator createDefaultDenotator(Module address) {
        Denotator res = null;
        try {
            res = new SimpleDenotator(null, this, address, getModule().getZero());
        }
        catch (DomainException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * Returns the module of this simple form.
     */
    public Module getModule() {
       return getIdentifier().getCodomainModule();
    }


    public List<Form> getDependencies(List<Form> list) {
        if (!list.contains(this)) {
            list.add(this);
        }
        return list;
    }
        
    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("[");
        buf.append(getNameString());
        buf.append(":.simple(");
        buf.append(getModule());
        RepresentableIdentityMorphism morphism = (RepresentableIdentityMorphism)identifier;
        if (morphism.hasBounds()) {
            buf.append(",");
            buf.append(morphism.getLowValue());
            buf.append(",");
            buf.append(morphism.getHighValue());
        }
        buf.append(")]");
        return buf.toString();
    }
    
    protected double getDimension(int maxDepth, int depth) {
        return 1.0;
    }
}
