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

import org.rubato.base.RubatoException;
import org.vetronauta.latrunculus.core.math.module.definition.Module;

import java.io.PrintStream;
import java.util.IdentityHashMap;
import java.util.LinkedList;

/**
 * List form class.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public class ListForm extends Form {

    /**
     * Generic form constructor.
     */
    public ListForm(NameDenotator name, YonedaMorphism identifier) {
        super(name, identifier);
    }


    /**
     * Builds a list identity form.
     */
    public ListForm(NameDenotator name, Form form) {
        super(name, new ProperIdentityMorphism(new FormDiagram(form), LIST));
    }
    

    /**
     * Returns the type of the form.
     */
    public int getType() {
        return LIST;
    }


    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ListForm) {
            return equals((ListForm)object);
        }
        else {
            return false;
        }
    }


    public boolean equals(ListForm f) {
        if (registered && f.registered) {
            return getName().equals(f.getName());
        }
        else {
            return fullEquals(f);
        }
    }


    public boolean fullEquals(ListForm f) {
        return fullEquals(f, new IdentityHashMap<Object,Object>());
    }


    public boolean fullEquals(ListForm f, IdentityHashMap<Object,Object> s) {
        if (this == f) {
            return true;
        }
        else if (!getName().equals(f.getName())) {
            return false;
        }
        s.put(this, f);
        return identifier.fullEquals(f.identifier, s);
    }

    
    /**
     * Returns the number of coordinate forms.
     */
    public int getFormCount() {
        return 1;
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
    
    
    protected LinkedList<Form> getDependencies(LinkedList<Form> list) {
        if (!list.contains(this)) {
            list.add(this);
            return identifier.getFormDependencies(list);
        }
        return list;
    }

    /**
     * Returns a default denotator of this list form.
     */
    public Denotator createDefaultDenotator() {
        Denotator res = null;
        try {
            res = new ListDenotator(null, this, new LinkedList<Denotator>());
        }
        catch (RubatoException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * Returns a default denotator of this list form with the given address.
     */
    public Denotator createDefaultDenotator(Module address) {
        Denotator res = null;
        try {
            res = new ListDenotator(null, address, this, new LinkedList<Denotator>());
        }
        catch (RubatoException e) {
            e.printStackTrace();
        }
        return res;
    }

    
    public String toString() {
        return "["+getNameString()+":.list("+getForm().getNameString()+")]";
    }
    
    
    protected void display(PrintStream out, LinkedList<Form> recursionCheckStack, int indent) {
        indent(out, indent);
        out.print("Name: \""+getNameString()+"\"");
        out.println("; Type: list");

        indent += 4;
    
        if (recursionCheck(recursionCheckStack)) {
            indent(out, indent);
            out.println("...");
        }
        else {
            recursionCheckStack.addFirst(this);
            getForm().display(out, recursionCheckStack, indent);
            recursionCheckStack.removeFirst();                
        }
    }    

    
    protected double getDimension(int maxDepth, int depth) {
        // $$$RA really?
        return 1.0;
    }
}
