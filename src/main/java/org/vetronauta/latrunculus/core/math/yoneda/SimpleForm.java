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

import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.RepresentableIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

import java.io.PrintStream;
import java.util.IdentityHashMap;
import java.util.LinkedList;

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
    public SimpleForm(NameDenotator name, Module m,
                      ModuleElement lo, ModuleElement hi) {
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
        else {
            return fullEquals(f);
        }
    }


    public boolean fullEquals(SimpleForm f) {
        return fullEquals(f, new IdentityHashMap<Object,Object>());
    }


    public boolean fullEquals(SimpleForm f, IdentityHashMap<Object,Object> s) {
        if (this == f) {
            return true;
        }
        else if (!getName().equals(f.getName())) {
            return false;
        }
        s.put(this, f);
        return identifier.fullEquals(f.identifier, s);
    }
    

    public boolean iscomplete() {
        return identifier != null;
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
    

    protected LinkedList<Form> getDependencies(LinkedList<Form> list) {
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
    
    
    protected void display(PrintStream out, LinkedList<Form> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \""+getNameString()+"\"");
        out.println("; Type: simple");
        indent(out, indent+4);
        out.println("Module: "+getModule());
    }    

    
    protected double getDimension(int maxDepth, int depth) {
        return 1.0;
    }
}
