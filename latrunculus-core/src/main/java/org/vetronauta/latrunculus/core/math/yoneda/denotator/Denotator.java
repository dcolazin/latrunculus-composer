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

package org.vetronauta.latrunculus.core.math.yoneda.denotator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.exception.LatrunculusAddressException;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.exception.LatrunculusFormException;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.AbstractConnectableYoneda;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;
import org.vetronauta.latrunculus.core.repository.Dictionary;
import org.vetronauta.latrunculus.core.util.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for denotators.
 * 
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
@Getter
@Setter
public abstract class Denotator extends AbstractConnectableYoneda implements Comparable<Denotator>, Iterable<Denotator>, MathDefinition {

    private final Form form;

    /**
     * Name of the denotator as a denotator.
     * A null name indicates an anonymous denotator.
     */
    private NameDenotator name;
    private YonedaMorphism coordinate;
    private YonedaMorphism frameCoordinate;

    /**
     * Makes a shallow copy this denotator and gives
     * it the specified <code>name</code>.
     */
    public abstract Denotator namedCopy(NameDenotator nameDenotator);

    /**
     * Returns the name of the denotator converted to a string.
     * An empty string indicates an anonymous denotator.
     */
    public String getNameString() {
        return name == null ? StringUtils.EMPTY : name.getNameString();
    }

    /**
     * Sets the name of the denotator as a string.
     * To make an anonymous denotator, pass an empty string.
     */
    public final void setNameString(String nameString) {
        name = StringUtils.isEmpty(nameString) ? NameDenotator.make(nameString) : null;
    }
    
    /**
     * Returns the type of the denotator.
     * @return type as an integer
     */
    public abstract FormDenotatorTypeEnum getType();

    /**
     * Sets both coordinates of the denotator.
     */
    protected final void setCoordinates(YonedaMorphism morphism) {
        coordinate = morphism;
        frameCoordinate = morphism;
    }
    
    /**
     * If not null-addressed returns a new denotator evaluated at address element.
     * If null-addressed, returns self.
     * 
     * @param element address
     * @return evaluated denotator
     * @throws MappingException if evaluation fails
     */
    public abstract Denotator at(ModuleElement element) throws MappingException;
    
    
    /**
     * Returns denotator evaluated at null address.
     * 
     * @return evaluated denotator
     */
    public final Denotator atNull() {
	    if (nullAddressed()) {
	        return this;
	    }
	    else {
            try {
                return at(getAddress().getZero());
            }
            catch (MappingException e) {
                throw new Error("Fatal error: Denotator.atNull should always succeed");
            }
	    }
    }
    

    /**
     * Returns the address of the denotator.
     */
    public final Module getAddress() {
        return getCoordinate().getDomainModule();
    }
        

    /**
     * Return true iff denotator is null-addressed.
     */
    public final boolean nullAddressed() {
        return getCoordinate().getDomainModule().isNullModule();
    }
    

    /**
     * Makes an address change.
     * 
     * @param newAddress the new address of the denotator
     * @return a copy of this denotator with the new address
     *         or null if address change fails
     */
    public abstract Denotator changeAddress(Module newAddress);
    
    
    /**
     * Makes an address change using a module morphism.
     * 
     * @param morphism the address changing morphism
     * @return a copy of this denotator with the new address
     *         or null if address change fails
     */
    public abstract Denotator changeAddress(ModuleMorphism morphism);
    
    
    /**
     * Retrieves a denotator by traversing the tree rooted
     * at this denotator.
     * 
     * @return null if denotator could not be retrieved along the path
     * @throws LatrunculusCheckedException
     */
    public final Denotator get(int[] path) throws LatrunculusCheckedException {
        return get(path, 0);
    }

    
    /**
     * Retrieves a denotator by traversing the tree rooted
     * at this denotator.
     * 
     * @param curpos the current position in the path
     * @return null if denotator could not be retrieved along the path
     * @throws LatrunculusCheckedException
     */
    protected abstract Denotator get(int[] path, int curpos) throws LatrunculusCheckedException;

    /**
     * Returns a denotator by replacing the denotator
     * at the given path in this denotator with <code>d</code>.
     * 
     * @param path the path at the end of which the denotator is replaced
     * @param d the denotator to put at the path 
     * @throws LatrunculusCheckedException if the specified denotator
     *         is of the wrong form or address
     */
    public final Denotator replace(int[] path, Denotator d)
            throws LatrunculusCheckedException {
        return replace(path, 0, d);
    }

    
    /**
     * Returns a denotator by replacing the denotator
     * at the given path in this denotator with <code>d</code>.
     * 
     * @param path the path at the end of which the denotator is replaced
     * @param currentPosition the current position in the path
     * @param d the denotator to put at the path 
     * @throws LatrunculusCheckedException if the specified denotator
     *         is of the wrong form or address
     */
    protected abstract Denotator replace(int[] path, int currentPosition
            , Denotator d)
        throws LatrunculusCheckedException;
    
    
    /**
     * Maps this denotator using the given <code>morphism</code>
     * along the specified <code>path</code>.
     * 
     * @throws LatrunculusCheckedException
     */
    public final Denotator map(int[] path, ModuleMorphism morphism)
            throws LatrunculusCheckedException {
        return map(path, 0, morphism);
    }
    
    
    /**
     * Maps this denotator using the given <code>morphism</code>
     * along the specified <code>path</code> given that the
     * current position is <code>currentPosition</code>.
     * 
     * @param currentPosition the current position in the path
     * @return null if element could not be retrieved along the path
     * @throws LatrunculusCheckedException
     */
    protected abstract Denotator map(int[] path, int currentPosition, ModuleMorphism morphism)
        throws LatrunculusCheckedException;

    
    /**
     * Retrieves the module element by traversing the denotator
     * tree. A denotator of type simple must be reached somewhere
     * along the path.
     * 
     * @return null if element could not be retrieved along the path
     * @throws LatrunculusCheckedException
     */
    public final ModuleElement getElement(int[] path)
            throws LatrunculusCheckedException {
        return getElement(path, 0);
    }
    
    
    /**
     * Retrieves the module element by traversing the denotator
     * tree. A denotator of type simple must be reached somewhere
     * along the path.
     * 
     * @param curpos the current position in the path
     * @return null if element could not be retrieved along the path
     * @throws LatrunculusCheckedException
     */
    protected abstract ModuleElement getElement(int[] path, int curpos)
        throws LatrunculusCheckedException;
    
    
    /**
     * Retrieves the module morphism by traversing the denotator
     * tree. A denotator of type simple must be reached somewhere
     * along the path.
     * 
     * @return null if morphism could not be retrieved along the path
     * @throws LatrunculusCheckedException
     */
    public final ModuleMorphism getModuleMorphism(int[] path)
            throws LatrunculusCheckedException {
        return getModuleMorphism(path, 0);
    }
    
    
    /**
     * Retrieves the module morphism by traversing the denotator
     * tree. A denotator of type simple must be reached somewhere
     * along the path.
     * 
     * @param curpos the current position in the path
     * @return null if morphism could not be retrieved along the path
     * @throws LatrunculusCheckedException
     */
    protected abstract ModuleMorphism getModuleMorphism(int[] path, int curpos)
        throws LatrunculusCheckedException;

    
    /**
     * Compares the names of two denotators.
     * The comparison is string comparison, where a null
     * name is equal to another null name, otherwise less
     * than every other name.
     */
    public int nameCompareTo(Denotator d) {
        if (getName() == null) {
            return (d.getName() == null)?0:-1;
        }
        else if (d.getName() == null) {
            return 1;
        }
        else {
            return getName().compareTo(d.getName());
        }
    }

    /**
     * Checks for equality.
     */
    @Override
    public abstract boolean equals(Object object);
    
    
    /**
     * Returns true iff this denotator has the same name
     * as the other denotator.
     */
    public boolean nameEquals(Denotator other) {
        if (getName() == null) {
            return other.getName() == null;
        }
        else {
            return getName().equals(other.getName());
        }
    }
    
    
    /**
     * Returns true iff this denotator has the same form
     * as the other denotator.
     */
    public boolean formEquals(Denotator other) {
        return getForm().equals(other.getForm());
    }

    
    /**
     * Returns true iff this is ultimately a constant denotator,
     * regardless of its address.
     */
    public abstract boolean isConstant();
    

    /**
     * Checks if the form of the denotator matches <code>f</code>.
     * A form matches <code>f</code> if they are equal.
     * 
     * @return boolean true iff form matches <code>f</code>
     */
    public final boolean hasForm(Form f) {
        return getForm().equals(f);
    }
    
    /**
     * Returns a deep copy of this denotator.
     * The copy is given the same name as the original.
     */
    public abstract Denotator deepCopy();

    
    /**
     * Returns true iff this denotator is consistent.
     * This can be used in assertions to validate a denotator,
     * for example if its constructed using unsafe methods
     * or changed inplace.
     */
    public abstract boolean check();
    
    
    /**
     * Returns a hash code for this denotator.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37*hash+getNameString().hashCode();
        hash = 37*hash+getCoordinate().hashCode();
        if (getCoordinate() != getFrameCoordinate()) {
            hash = 37*hash+getFrameCoordinate().hashCode();
        }
        return hash;
    }


    /**
     * Returns a string representation of this denotator.
     * This string is not parseable and does not contain
     * all information. It is only meant for information purposes.
     */
    @Override
    public String toString() {
        if (nullAddressed()) { 
            return "[0@"+getNameString()+":"+getForm()+"]";
        }
        else {
            return "["+getAddress()+"@"+getNameString()+":"+getForm()+"]";
        }
    }

    /**
     * Returns a list of the named denotators that this denotator depends on.
     * A named denotator always depends on itself.
     */
    public final List<Denotator> getDependencies() {
        return getDependencies(new ArrayList<>());
    }
    
    
    public abstract List<Denotator> getDependencies(List<Denotator> list);


    /**
     * Replaces reference denotators by respective name denotators.
     *  
     * @param dict contains the lookup table
     * @return true if all references have been resolved
     */
    public boolean resolveReferences(Dictionary dict) {
        Map<Object,Object> history = new HashMap<>();
        return resolveReferences(dict, history);
    }
    

    public boolean resolveReferences(Dictionary dict, Map<Object,Object> history) {
        if (history.containsKey(this)) {
            return true;
        }
        else {
            history.put(this, this);
            if (!getCoordinate().resolveReferences(dict, history)) {
                return false;
            }
            if (getCoordinate()!= getFrameCoordinate()) {
                return getFrameCoordinate().resolveReferences(dict, history);
            }
            return true;
        }
    }

    
    /**
     * Generic constructor for denotators.
     */
    protected Denotator(NameDenotator name, Form form,
                        YonedaMorphism coordinate, YonedaMorphism frameCoordinate) {
        this.name = name;
        this.form = form;
        this.coordinate = coordinate;
        this.frameCoordinate = frameCoordinate;
    }
	    

    /**
     * Generic constructor for denotators.
     */
    protected Denotator(NameDenotator name, Form form) {
        this.name = name;
        this.form = form;
    }


    protected static LatrunculusAddressException addressMismatchException(Denotator d, Module address) {
        String s = TextUtils.replaceStrings("Denotator has address %%1, but required address is %%2",
                                            d.getAddress(), address);
        return new LatrunculusAddressException(s, d.getAddress(), address);
    }


    protected static void checkDenotator(Denotator d, Form f)
            throws LatrunculusFormException {
        if (!d.hasForm(f)) {
            throw new LatrunculusFormException(d.getForm(), f, "Denotator.checkDenotator");
        }
    }


    protected static void checkDenotator(Denotator d, Form f, Module address)
            throws LatrunculusFormException, LatrunculusAddressException {
        if (!d.hasForm(f)) {
            throw new LatrunculusFormException(d.getForm(), f, "Denotator.checkDenotator");
        }
        else if (d.nullAddressed() && address.isNullModule()) {
            return;
        }
        else if (!address.equals(d.getAddress())) {
            throw addressMismatchException(d, address);
        }
        
    }

}
