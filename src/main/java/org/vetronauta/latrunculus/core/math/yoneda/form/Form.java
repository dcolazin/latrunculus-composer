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

import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.core.repository.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.yoneda.AbstractConnectableYoneda;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for forms.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public abstract class Form extends AbstractConnectableYoneda implements Comparable<Form>, MathDefinition {

    private static final int DEFAULT_MAX_DEPTH = 10;

    protected NameDenotator name;
    protected YonedaMorphism identifier;

    private boolean hasHashcode = false;
    private int hashcode = 0;
    protected boolean registered = false;

    /**
     * Generic form constructor.
     */
    protected Form(NameDenotator name, YonedaMorphism identifier) {
        this.name = name;
        this.identifier = identifier;
    }


    /**
     * Returns the name of the form as a denotator.
     */
    public NameDenotator getName() {
        return name;
    }
    

    /**
     * Returns the name of the form as a string.
     */
    public String getNameString() {
        return name.getNameString();
    }

    /**
     * Returns the type of the form.
     */
    public abstract FormDenotatorTypeEnum getType();


    /**
     * Returns the type of the form as a string.
     */
    public String getTypeString() {
        return getType().toString();
    }
    

    /**
     * Returns the identifier of the form.
     */
    public YonedaMorphism getIdentifier() {
        return identifier;
    }


    /**
     * Sets the name of the form as a denotator.
     */
    public void setName(NameDenotator name) {
        this.name = name;
        this.hasHashcode = false;
    }


    /**
     * Sets the name of the form as a denotator.
     */
    public void setName(String name) {
        setName(NameDenotator.make(name));
    }


    /**
     * Sets the identifier of the form.
     */
    public void setIdentifier(YonedaMorphism identifier) {
        this.identifier = identifier;
        this.hasHashcode = false;
    }


    /**
     * Returns the dimension of a form using default maximal depth.
     */
    public double getDimension() {
        return getDimension(DEFAULT_MAX_DEPTH, 0);
    }


    /**
     * Returns the dimension of a form.
     */
    public double getDimension(int maxDepth) {
        return getDimension(maxDepth, 0);
    }

    
    protected abstract double getDimension(int maxDepth, int depth);

    /**
     * Returns a default denotator of this form.
     */
    public abstract Denotator createDefaultDenotator();
    
    /**
     * Returns a default denotator of this form with the given address.
     */
    public abstract Denotator createDefaultDenotator(Module address);

    /**
     * Compares two forms.
     * Comparison is done on the names of two forms.
     */
    public int compareTo(Form other) {
        if (equals(other)) {
            return 0;
        }
        else if (getType() == other.getType()) {
            return getName().compareTo(other.getName());
        }
        else {
            return getType().ordinal()-other.getType().ordinal();
        }
    }
    

    /**
     * Form object cannot be cloned.
     */
    public Form deepCopy() {
        throw new UnsupportedOperationException("Form objects cannot be cloned");
    }


    /**
     * Returns true iff this form is equal to the specified object.
     */
    public abstract boolean equals(Object object);


    /**
     * Returns true iff this form is equal to the specified form.
     */
    public boolean equals(Form f) {
        if (registered && f.registered) {
            return getName().equals(f.getName());
        }
        else {
            return fullEquals(f);
        }
    }


    /**
     * Returns true iff this form is <i>structurally</i> equal to <code>f</code>.
     */
    public boolean fullEquals(Form f) {
        return fullEquals(f, new IdentityHashMap<>());
    }


    /**
     * Returns true iff this form is <i>structurally</i> equal to <code>f</code>.
     * @param f the form to compare to
     * @param s a map containing a history of already encountered forms,
     *          used to break recursion
     */
    public boolean fullEquals(Form f, Map<Object,Object> s) {
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
    public abstract int getFormCount();
    

    /**
     * Returns a coordinate form.
     * 
     * @param i the coordinate position
     * @return the form at coordinate position i
     */
    public abstract Form getForm(int i);
    

    /**
     * Returns a list of the coordinate forms.
     */
    public List<Form> getForms() {
        LinkedList<Form> list = new LinkedList<>();
        for (int i = 0; i < getFormCount(); i++) {
            list.add(getForm(i));
        }
        
        return list;
    }

    /**
     * Returns a string representation of this form.
     * This string is not parseable and does not contain
     * all information. It is only meant for information purposes.
     */
    public abstract String toString();

    /**
     * Returns a list of the forms that this form depends on.
     * A form always depends on itself.
     */
    public final List<Form> getDependencies() {
        return getDependencies(new ArrayList<>());
    }
    
    
    /**
     * Adds the the forms that this form depends on to <code>list</code>.
     * @param list the dependency list, updated by this method.
     * 
     * @return the changed list
     */
    public abstract List<Form> getDependencies(List<Form> list);

    
    /**
     * Returns a hash code for this form.
     */
    public int hashCode() {
        if (!hasHashcode) {
            computeHashcode();
        }
        return hashcode;
    }


    /**
     * Registers this form with the specified repository.
     * This is for internal use only. Forms must be registered
     * by using the {@link Repository#register(Form)} method.
     */
    public Form _register(Repository repository, boolean builtin) {
        if (registered) {
            return this;
        }
        else {
            registered = true;
            Form form = repository.register(this, builtin);
            if (form == null) {
                registered = false;
            }
            return form;
        }
    }
    
    
    /**
     * Returns true iff this form is already registered.
     */
    public boolean isRegistered() {
        return registered;
    }
    

    private void computeHashcode() {
        hasHashcode = true;
        hashcode = getNameString().hashCode();
        hashcode = 37*hashcode + (getType().ordinal()+1);
        if (identifier != null) {
            hashcode = 37*hashcode + identifier.hashCode();
        }
    }

    
    /**
     * Resolves the references resulting from parsing.
     * 
     * @return true iff all references have been resolved.
     */
    public boolean resolveReferences(RubatoDictionary dict) {
        return resolveReferences(dict, new IdentityHashMap<>());
    }
    
    
    public boolean resolveReferences(RubatoDictionary dict, Map<Object,Object> history) {
        if (history.containsKey(this)) {
            return true;
        }
        else {
            history.put(this, this);
            return identifier.resolveReferences(dict, history);
        }
    }

}
