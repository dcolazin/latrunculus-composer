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

import org.vetronauta.latrunculus.core.util.Internal;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.util.Unsafe;
import org.vetronauta.latrunculus.core.logeo.DenoFactory;
import org.vetronauta.latrunculus.core.logeo.Sets;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.exception.LatrunculusAddressException;
import org.vetronauta.latrunculus.core.exception.LatrunculusFormException;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.map.AutoListMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.CompoundMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.ProperIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Power denotator class.
 * 
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class PowerDenotator extends Denotator implements FactorDenotator {

    /**
     * Creates a new power denotator.
     * 
     * @param name    the name of the denotator, null if denotator is anomymous
     * @param address the address of the denotator, null if null-addressed
     * @param form    the form of the denotator
     * @param cds     a list of coordinate denotators
     * @throws LatrunculusCheckedException
     */
    public PowerDenotator(NameDenotator name, Module address, PowerForm form, List<Denotator> cds)
            throws LatrunculusCheckedException {
        super(name, form);
        
        FormDiagram diagram = form.getFormDiagram();
        Form baseForm = form.getForm();
        AutoListMorphismMap map = new AutoListMorphismMap();
        for (Denotator d : cds) {
            checkDenotator(d, baseForm, address);
            map.appendFactor(d);
        }
        setCoordinates(new CompoundMorphism(address, new ProperIdentityMorphism(diagram, FormDenotatorTypeEnum.POWER), map));
        _normalize();
    }


    /**
     * Creates a new power denotator that takes its address from the coordinates.
     * 
     * @param name the name of the denotator, null if denotator is anomymous
     * @param form the form of the denotator
     * @param cds  a list of coordinate denotators
     * @throws LatrunculusCheckedException
     */
    public PowerDenotator(NameDenotator name, PowerForm form, List<Denotator> cds)
            throws LatrunculusCheckedException {
        super(name, form);
        
        FormDiagram diagram = form.getFormDiagram();
        Form baseForm = form.getForm();
        AutoListMorphismMap map = new AutoListMorphismMap();
        Module address;
        
        if (cds.size() > 0) {
            address = cds.get(0).getAddress();
            for (Denotator d : cds) {
                checkDenotator(d, baseForm, address);
                map.appendFactor(d);
            }
        }
        else {
            address = ZRing.nullModule;
        }
        setCoordinates(new CompoundMorphism(address, new ProperIdentityMorphism(diagram, FormDenotatorTypeEnum.POWER), map));
        _normalize();
    }

    
    /**
     * Returns the type of the denotator.
     * 
     * @return type as an integer
     */
    @Override
    public FormDenotatorTypeEnum getType() {
        return FormDenotatorTypeEnum.POWER;
    }
    
    
    @Override
    public Denotator namedCopy(NameDenotator nameDenotator) {
        YonedaMorphism coord;
        YonedaMorphism frameCoord;
        if (getCoordinate() == getFrameCoordinate()) {
            coord = frameCoord = getCoordinate().deepCopy();
        }
        else {
            coord = getCoordinate().deepCopy();
            frameCoord = getCoordinate().deepCopy();
        }
        return new PowerDenotator(nameDenotator, getPowerForm(), coord, frameCoord);
    }

    
    /**
     * If not null-addressed returns a new denotator evaluated at address element.
     * If null-addressed, returns itself.
     * 
     * @param element address
     * @return evaluated denotator
     * @throws MappingException if evaluation fails
     */
    @Override
    public Denotator at(ModuleElement element)
            throws MappingException {
        if (nullAddressed()) {
            return this;
        }
        else {
            if (getCoordinate() == getFrameCoordinate()) {
                YonedaMorphism newCoord = getCoordinate().at(element);
                if (newCoord == getCoordinate()) {
                    return this;
                }
                else {
                    return new PowerDenotator(null, getPowerForm(), newCoord, newCoord);
                }
            }
            else {
                YonedaMorphism newCoord = getCoordinate().at(element);
                YonedaMorphism newFCoord = getFrameCoordinate().at(element);
                if (newCoord == getCoordinate() && newFCoord == getFrameCoordinate()) {
                    return this;
                }
                else {
                    return new PowerDenotator(null, getPowerForm(), newCoord, newFCoord);
                }
            }
        }
    }
    
    
    @Override
    public Denotator changeAddress(Module newAddress) {
        if (getAddress().equals(newAddress)) {
            return this;
        }
        else if (getCoordinate() == getFrameCoordinate()) {
            YonedaMorphism newCoord = getCoordinate().changeAddress(newAddress);
            if (newCoord == getCoordinate()) {
                return this;
            }
            else {
                return new PowerDenotator(null, getPowerForm(), newCoord, newCoord);
            }
        }
        else {
            YonedaMorphism newCoord = getCoordinate().changeAddress(newAddress);
            YonedaMorphism newFCoord = getFrameCoordinate().changeAddress(newAddress);
            if (newCoord == getCoordinate() && newFCoord == getFrameCoordinate()) {
                return this;
            }
            else {
                return new PowerDenotator(null, getPowerForm(), newCoord, newFCoord);
            }
        }
    }
    
    
    @Override
    public Denotator changeAddress(ModuleMorphism morphism) {
        if (getCoordinate() == getFrameCoordinate()) {
            YonedaMorphism newCoord = getCoordinate().changeAddress(morphism);
            if (newCoord == getCoordinate()) {
                return this;
            }
            else {
                return new PowerDenotator(null, getPowerForm(), newCoord, newCoord);
            }
        }
        else {
            YonedaMorphism newCoord = getCoordinate().changeAddress(morphism);
            YonedaMorphism newFCoord = getFrameCoordinate().changeAddress(morphism);
            if (newCoord == getCoordinate() && newFCoord == getFrameCoordinate()) {
                return this;
            }
            else {
                return new PowerDenotator(null, getPowerForm(), newCoord, newFCoord);
            }
        }
    }

    
    @Override
    protected Denotator get(int[] path, int curpos) throws LatrunculusCheckedException {
        if (curpos == path.length) {
            return this;
        }
        if (curpos > path.length) {
            throw new LatrunculusCheckedException("PowerDenotator.get: Incompatible path, expected length >= %1, but got length %2", curpos, path.length);
        }
        if (getFactorCount() == 1 && path[curpos] == 0) {
            return getFactor(0).get(path, curpos+1);
        }
        //florian needs this code! (if you got factors you might as well do this)
        if (getFactorCount() > path[curpos]) {
        	return getFactor(path[curpos]).get(path, curpos+1);
        }
        return null;
    }
    
    
    @Override
    protected Denotator replace(int[] path, int currentPosition, Denotator d)
            throws LatrunculusCheckedException {
        if (currentPosition == path.length) {
            if (d.hasForm(getForm())) {
                Denotator res = d;
                if (!d.getAddress().equals(getAddress())) {
                    res = d.changeAddress(getAddress());
                    if (res == null) {
                        throw new LatrunculusCheckedException("PowerDenotator.replace: Could not change address "+
                                                  "from %1 to %2", d.getAddress(), getAddress());
                    }
                }
                return res;
            }
            else {
                throw new LatrunculusCheckedException("PowerDenotator.replace: Expected denotator of "+
                                          "form %1, but got %2", getForm(), d.getForm());
            }
        }
        else if (currentPosition > path.length) {
            throw new LatrunculusCheckedException("PowerDenotator.replace: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                    currentPosition, path.length);
        }
        else if (currentPosition == path.length-1 && path[currentPosition] == 0) {
            if (d.hasForm(getPowerForm().getForm())) {
                Denotator res = d;
                if (!d.getAddress().equals(getAddress())) {
                    res = d.changeAddress(getAddress());
                    if (res == null) {
                        throw new LatrunculusCheckedException("PowerDenotator.replace: Could not change address "+
                                                  "from %1 to %2", d.getAddress(), getAddress());
                    }
                }
                List<Denotator> denoList = new LinkedList<Denotator>();
                denoList.add(res);
                return _make_unsafe(null, getAddress(), getPowerForm(), denoList);
            }
            throw new LatrunculusCheckedException("ListDenotator.replace: Expected denotator of "+
                                      "form %1, but got %2", getPowerForm().getForm(), d.getForm());
        }
        //florian braucht diesen code! (wenn man factors hat kann man ja auch gleich dies machen)
        else if (getFactorCount() > path[currentPosition]) {
        	Denotator oldDenotator = getFactor(path[currentPosition]);
        	Denotator newDenotator = oldDenotator.replace(path, currentPosition +1, d);
        	AutoListMorphismMap listmap = getListMorphismMap();
        	listmap.replaceFactor(oldDenotator, newDenotator);
        	return _make_unsafe(null, getAddress(), getPowerForm(), getFactors());
        }
        else {
            LinkedList<Denotator> denoList = new LinkedList<Denotator>();
            for (Denotator deno : getFactors()) {
                denoList.add(deno.replace(path, currentPosition +1, d));
            }
            return DenoFactory.makeDenotator(getPowerForm(), denoList);
        }
    }    

    
    @Override
    protected Denotator map(int[] path, int currentPosition, ModuleMorphism morphism)
            throws LatrunculusCheckedException {
        if (currentPosition >= path.length) {
            throw new LatrunculusCheckedException("PowerDenotator.map: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                    currentPosition, path.length);
        }
        if (path[currentPosition] == 0) {
            boolean changed = false;
            LinkedList<Denotator> newFactors = new LinkedList<Denotator>();        
            for (Denotator factor : getFactors()) {
                Denotator newFactor = factor.map(path, currentPosition +1, morphism);
                if (newFactor != factor) {
                    changed = true;
                }
                newFactors.add(newFactor);
                 
            }
            if (changed) {
                return DenoFactory.makeDenotator(getForm(), newFactors);
            }
            else {
                return this;
            }
        }
        return this;
    }

    
    @Override
    protected ModuleElement getElement(int[] path, int curpos)
            throws LatrunculusCheckedException {
        if (curpos >= path.length) {
            throw new LatrunculusCheckedException("PowerDenotator.getElement: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                                      curpos, path.length);
        }
        if (getFactorCount() == 1 && path[curpos] == 0) {
            return getFactor(0).getElement(path, curpos+1);
        }
        else {
            return null;
        }
    }
    
    
    @Override
    protected ModuleMorphism getModuleMorphism(int[] path, int curpos)
            throws LatrunculusCheckedException {
        if (curpos >= path.length) {
            throw new LatrunculusCheckedException("PowerDenotator.getModuleMorphism: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                                      curpos, path.length);
        }
        if (getFactorCount() == 1 && path[curpos] == 0) {
            return getFactor(0).getModuleMorphism(path, curpos+1);
        }
        else {
            return null;
        }
    }
    
    
    /**
     * Returns the form of this denotator.
     */
    public PowerForm getPowerForm() {
        return (PowerForm)getForm();
    }
    
    
    /**
     * Returns the number of coordinates of the denotator.
     * 
     * @return number of coordinates
     */
    public int getFactorCount() {
        return getListMorphismMap().getFactorCount();
    }


    /**
     * Returns the factor at position <code>i</code>.
     * 
     * @param i denotator at position <code>i</code>
     */
    public Denotator getFactor(int i) {
        return getListMorphismMap().getFactor(i);
    }


    /**
     * Sets the factor <code>d</code> at position <code>i</code>.
     * This is a destructive operation; beware of aliasing.
     * 
     * @throws LatrunculusCheckedException if <code>d</code> is not of the required form
     *         or <code>i</code> is out of range
     */
    public void setFactor(int i, Denotator d)
            throws LatrunculusCheckedException {
        AutoListMorphismMap listmap = getListMorphismMap();
        if (i < 0 || i >= listmap.getFactorCount()) {
            throw new LatrunculusCheckedException("PowerDenotator.setFactor: Expected index "+
                                      "0 <= index < %1, but got ", listmap.getFactorCount(), i);
        }
        checkDenotator(d, getPowerForm().getForm(), getAddress());
        Denotator oldFactor = listmap.getFactor(i); 
        listmap.replaceFactor(oldFactor, d);
    }


    /**
     * Appends a factor.
     * This is a destructive operation; beware of aliasing.
     * 
     * @throws LatrunculusCheckedException if d is not of the required form
     */
    public boolean appendFactor(Denotator d)
            throws LatrunculusCheckedException {
        checkDenotator(d, getPowerForm().getForm(), getAddress());
        getListMorphismMap().appendFactor(d);
        _normalize();
        return true;
    }
    
    /**
     * Removes a factor.
     * This is a destructive operation; beware of aliasing.
     */
    public Denotator removeFactor(int index) {
    	Denotator d = getListMorphismMap().removeFactor(index);
        _normalize();
        return d;
    }
    
    /*public boolean insertFactor(int index, Denotator d) {
    	getListMorphismMap().insertFactor(index, d);
        _normalize();
        return true;
    }*/


    /**
     * Sets the factor list to a new list of denotators.
     * 
     * @param denoList the list of factor denotators
     * @throws LatrunculusCheckedException the list of denotators doesn't match
     *         the form of the denotator.
     */
    public void replaceFactors(List<Denotator> denoList)
            throws LatrunculusCheckedException {
        Form baseForm = getPowerForm().getForm();
        AutoListMorphismMap newMap = new AutoListMorphismMap();

        for (Denotator d : denoList) {
            checkDenotator(d, baseForm, getAddress());
            newMap.appendFactor(d);
        }
        getCoordinate().setMap(newMap);
        _normalize();
    }
    

    /**
     * Returns a (new) list of the coordinates of the denotator.
     */
    public List<Denotator> getFactors() {
        return new LinkedList<Denotator>(getListMorphismMap().getFactors());
    }

    
    
    /**
     * Returns the set union of this power denotator and <code>d</code>.
     * @throws LatrunculusCheckedException
     */
    public PowerDenotator union(PowerDenotator d)
            throws LatrunculusCheckedException {
        return Sets.union(this, d);
    }


    /**
     * Returns the set intersection of this power denotator and <code>d</code>.
     * @throws LatrunculusCheckedException
     */
    public PowerDenotator intersection(PowerDenotator d)
            throws LatrunculusCheckedException {
        return Sets.intersection(this, d);
    }

    
    /**
     * Returns the set difference of this power denotator and <code>d</code>.
     * 
     * @throws LatrunculusCheckedException
     */
    public PowerDenotator difference(PowerDenotator d)
            throws LatrunculusCheckedException {
        return Sets.difference(this, d);
    }

    
    /**
     * Returns true iff this power denotator is a subset of <code>d</code>.
     */
    public boolean subset(PowerDenotator d) {
        return Sets.subset(this, d);
    }

    
    /**
     * Returns true iff this power denotator contains <code>d</code>.
     */
    public boolean contains(Denotator d) {
        return Sets.contains(this, d);
    }
    
    /**
     * Returns the internal index of denotator <code>d</code>.
     */
    public int indexOf(Denotator d) {
    	return getListMorphismMap().indexOf(d);
    }

    
    /**
     * Returns an iterator over the coordinates.
     */
    @Override
    public Iterator<Denotator> iterator() {
        return getListMorphismMap().iterator();
    }


    public AutoListMorphismMap getListMorphismMap() {
        return (AutoListMorphismMap)getCoordinate().getMap();
    }
    
    
    public AutoListMorphismMap getFrameListMorphismMap() {
        return (AutoListMorphismMap)getFrameCoordinate().getMap();
    }

    
    @Override
    public boolean isConstant() {
        return getListMorphismMap().isConstant();
    }
    
    
    @Override
    public int compareTo(Denotator object) {
        if (this == object) {
            return 0;
        }
        if (object instanceof PowerDenotator) {
            return compareTo((PowerDenotator)object);
        }
        return getForm().compareTo(object.getForm());
    }


    /**
     * Compares two power denotators.
     */
    public int compareTo(PowerDenotator other) {
        int c = getForm().compareTo(other.getForm());
        if (c == 0) {
            c = nameCompareTo(other);
            if (c == 0) {
                c = getListMorphismMap().compareTo(other.getListMorphismMap());
            }
        }
        return c;
    }


    /**
     * Checks for equality.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof PowerDenotator) {
            return equals((PowerDenotator)object);
        }
        else {
            return false;
        }
    }


    /**
     * Checks denotators of the same form for equality.
     */
    public boolean equals(PowerDenotator other) {
        if (formEquals(other) && nameEquals(other)) {
            return getListMorphismMap().equals(other.getListMorphismMap());
        }
        else {
            return false;
        }
    }


    @Override
    public PowerDenotator deepCopy() {
        YonedaMorphism coord;
        YonedaMorphism frameCoord;
        if (getCoordinate() == getFrameCoordinate()) {
            coord = frameCoord = getCoordinate().deepCopy();
        }
        else {
            coord = getCoordinate().deepCopy();
            frameCoord = getCoordinate().deepCopy();
        }
        return new PowerDenotator(getName(), getPowerForm(), coord, frameCoord);
    }

    
    @Override
    public boolean check() {
        // any exception indicates inconsistency
        try {
            // is the form of type power?
            if (getForm() instanceof PowerForm) {
                PowerForm f = (PowerForm)getForm();
                Form factorForm = f.getForm();
                Module address = getAddress();
                List<Denotator> factors = getFactors();
                // check consistency of the factors                
                for (Denotator d : factors) {
                    if (!d.check()) {
                        // the current factor is inconsistent
                        return false;
                    }
                    else {
                        // has the current factor the required form?
                        if (!d.hasForm(factorForm)) {
                            return false;
                        }
                        // is the address of the current factor correct?
                        else if (!d.getAddress().equals(address)) {
                            return false;
                        }
                        else {
                            return true;
                        }
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
    }

    
    @Override
    public List<Denotator> getDependencies(List<Denotator> list) {
        if (!list.contains(this)) {
            list.add(this);
            list = getCoordinate().getDenotatorDependencies(list);
            if (getCoordinate() != getFrameCoordinate()) {
                list = getFrameCoordinate().getDenotatorDependencies(list);
            }
        }
        return list;
    }
    
    /**
     * Normalizes power denotator.
     * This ensures that, for a denotator of type power, the underlying
     * list is sorted and contains no duplicates.
     */
    @Internal
    public void _normalize() {
        /*ListMorphismMap map = getListMorphismMap();
        map.sort();
        map.removeDuplicates();
        if (map != getFrameListMorphismMap()) {
            map = getFrameListMorphismMap();
            map.sort();
            map.removeDuplicates();
        }*/
    }


    /**
     * Creates a new power denotator without checking whatsoever.
     * The arguments must result in a correct denotator, otherwise
     * there may be nasty consequences.
     */
    @Unsafe
    @Internal
    public static PowerDenotator _make_unsafe(NameDenotator name, Module address,
                                              PowerForm form, List<Denotator> denoList) {
        AutoListMorphismMap map = new AutoListMorphismMap(denoList);
        FormDiagram diagram = form.getFormDiagram();
        CompoundMorphism coordinate = new CompoundMorphism(address, new ProperIdentityMorphism(diagram, FormDenotatorTypeEnum.POWER), map);
        PowerDenotator res = new PowerDenotator(name, form, coordinate, coordinate);
        assert(res._is_valid());
        return res;
    }

    
    /**
     * Returns true iff this denotator is correctly built.
     */
    @Internal
    public boolean _is_valid() {
        
            for (Denotator d : this) {
                try {
                    checkDenotator(d, getPowerForm().getForm(), getAddress());
                }
                catch (LatrunculusAddressException e) {
                    return false;
                }
                catch (LatrunculusFormException e) {
                    return false;
                }
            }
            return true;
        
    }
    
    
    /**
     * Generic power denotator constructor.
     */
    private PowerDenotator(NameDenotator name, PowerForm form,
                           YonedaMorphism coordinate, YonedaMorphism frameCoordinate) {
        super(name, form, coordinate, frameCoordinate);
    }
}
