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
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.map.IndexMorphismMap;
import org.vetronauta.latrunculus.core.exception.LatrunculusAddressException;
import org.vetronauta.latrunculus.core.exception.LatrunculusFormException;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.CompoundMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.ProperIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Colimit denotator class.
 * 
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class ColimitDenotator extends Denotator implements FactorDenotator {

    private int index;

    /**
     * Creates a colimit denotator.
     * 
     * @param name    the name of the denotator
     * @param address the address of the denotator
     * @param form    the form of the denotator
     * @param index   the index within the colimit form
     * @param deno    the factor of the denotator at the given index
     * @throws LatrunculusFormException
     */
    public ColimitDenotator(NameDenotator name, Module address, ColimitForm form, int index, Denotator deno)
            throws LatrunculusCheckedException {
        super(name, form);
        setIndex(index);
        
        FormDiagram diagram = (FormDiagram)form.getIdentifier().getCodomainDiagram();
        
        checkDenotator(deno, diagram.getForm(index), address);

        IndexMorphismMap map = new IndexMorphismMap(index, deno);
        setCoordinates(new CompoundMorphism(address, new ProperIdentityMorphism(diagram, FormDenotatorTypeEnum.COLIMIT), map));
    }
    

    /**
     * Creates a colimit denotator that gets its address from the factor denotator.
     * 
     * @param name  the name of the denotator
     * @param form  the form of the denotator
     * @param index the index within the colimit form
     * @param deno  the factor of the denotator at the given index
     * @throws LatrunculusFormException
     */
    public ColimitDenotator(NameDenotator name, ColimitForm form, int index, Denotator deno)
            throws LatrunculusCheckedException {
        super(name, form);
        setIndex(index);
        
        FormDiagram diagram = (FormDiagram)form.getIdentifier().getCodomainDiagram();
        
        Module address = deno.getAddress();
        checkDenotator(deno, diagram.getForm(index), address);

        IndexMorphismMap map = new IndexMorphismMap(index, deno);
        setCoordinates(new CompoundMorphism(address, new ProperIdentityMorphism(diagram, FormDenotatorTypeEnum.COLIMIT), map));
    }
       
    
    /**
     * Returns the type of the denotator.
     * 
     * @return type as an integer
     */
    @Override
    public FormDenotatorTypeEnum getType() {
        return FormDenotatorTypeEnum.COLIMIT;
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
        return new ColimitDenotator(nameDenotator, getColimitForm(), getIndex(),
                                    coord, frameCoord);
    }

    
    /**
     * If not null-addressed returns a new denotator evaluated at address element.
     * If null-addressed, returns self.
     * 
     * @param element address
     * @return evaluated denotator
     * @throws MappingException if evaluation fails
     */
    @Override
    public Denotator at(ModuleElement element)
            throws MappingException {
        assert(element != null);
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
                    return new ColimitDenotator(null, getColimitForm(), getIndex(), newCoord, newCoord);
                }
            }
            else {
                YonedaMorphism newCoord = getCoordinate().at(element);
                YonedaMorphism newFCoord = getFrameCoordinate().at(element);
                if (newCoord == getCoordinate() && newFCoord == getFrameCoordinate()) {
                    return this;
                }
                else {
                    return new ColimitDenotator(null, getColimitForm(), getIndex(), newCoord, newFCoord);
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
                return new ColimitDenotator(null, getColimitForm(), index, newCoord, newCoord);
            }
        }
        else {
            YonedaMorphism newCoord = getCoordinate().changeAddress(newAddress);
            YonedaMorphism newFCoord = getFrameCoordinate().changeAddress(newAddress);
            if (newCoord == getCoordinate() && newFCoord == getFrameCoordinate()) {
                return this;
            }
            else {
                return new ColimitDenotator(null, getColimitForm(), index, newCoord, newFCoord);
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
                return new ColimitDenotator(null, getColimitForm(), index, newCoord, newCoord);
            }
        }
        else {
            YonedaMorphism newCoord = getCoordinate().changeAddress(morphism);
            YonedaMorphism newFCoord = getFrameCoordinate().changeAddress(morphism);
            if (newCoord == getCoordinate() && newFCoord == getFrameCoordinate()) {
                return this;
            }
            else {
                return new ColimitDenotator(null, getColimitForm(), index, newCoord, newFCoord);
            }
        }
    }

    
    @Override
    protected Denotator get(int[] path, int curpos)
            throws LatrunculusCheckedException {
        if (curpos == path.length) {
            return this;
        }
        else if (curpos > path.length) {
            throw new LatrunculusCheckedException("ColimitDenotator.get: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                                      curpos, path.length);
        }
        else {
            int i = path[curpos];
            if (i == getIndex()) {
                return getFactor(i).get(path, curpos+1);
            }
            else {
                return null;
            }
        }
    }
    
    
    @Override
    protected Denotator replace(int[] path, int currentPosition, Denotator d)
            throws LatrunculusCheckedException {
        if (currentPosition == path.length) {
        	//this.getForm().getForms().contains(d.getForm())
            if (d.hasForm(getForm())) {
                Denotator res = d;
                if (!d.getAddress().equals(getAddress())) {
                    res = d.changeAddress(getAddress());
                    if (res == null) {
                        throw new LatrunculusCheckedException("ColimitDenotator.replace: Could not change address "+
                                                  "from %1 to %2", d.getAddress(), getAddress());
                    }
                }
                return res;
            }
            else {
                throw new LatrunculusCheckedException("ColimitDenotator.replace: Expected denotator of "+
                                          "form %1, but got %2", getForm(), d.getForm());
            }
        }
        else if (currentPosition > path.length) {
            throw new LatrunculusCheckedException("ColimitDenotator.replace: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                    currentPosition, path.length);
        }
        else {
            int i = path[currentPosition];
            //florian replaced getFactorCount with getFormCount
            if (i >= 0 && i < this.getColimitForm().getFormCount()) {
                Denotator res = getFactor(i).replace(path, currentPosition +1, d);
                return _make_unsafe(null, getAddress(), getColimitForm(), i, res);
            }
            else {
                throw new LatrunculusCheckedException("ColimitDenotator.replace: Incompatible index in path,"+
                                          "expected 0 <= index < %1, but got %2", getFactorCount(), i); 
            }
        }
    }

    
    @Override
    protected Denotator map(int[] path, int currentPosition, ModuleMorphism morphism)
            throws LatrunculusCheckedException {
        if (currentPosition >= path.length) {
            throw new LatrunculusCheckedException("ColimitDenotator.map: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                    currentPosition, path.length);
        }
        if (path[currentPosition] == getIndex()) {
            Denotator factor = getFactor();
            Denotator newFactor = factor.map(path, currentPosition +1, morphism);
            if (newFactor != factor) {
                return DenoFactory.makeDenotator(getForm(), getIndex(), newFactor);
            }
        }
        return this;
    }
    
    
    @Override
    protected ModuleElement getElement(int[] path, int curpos)
            throws LatrunculusCheckedException {
        if (curpos >= path.length) {
            throw new LatrunculusCheckedException("ColimitDenotator.getElement: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                                      curpos, path.length);
        }
        int i = path[curpos];
        if (i == getIndex()) {
            return getFactor(i).getElement(path, curpos+1);
        }
        else {
            return null;
        }
    }

    
    @Override
    protected ModuleMorphism getModuleMorphism(int[] path, int curpos)
            throws LatrunculusCheckedException {
        if (curpos >= path.length) {
            throw new LatrunculusCheckedException("ColimitDenotator.getModuleMorphism: Incompatible path, "+
                                      "expected length >= %1, but got length %2",
                                      curpos, path.length);
        }
        int i = path[curpos];
        if (i == getIndex()) {
            return getFactor(i).getModuleMorphism(path, curpos+1);
        }
        else {
            return null;
        }
    }

    
    /**
     * Returns the form of the denotator.
     */
    public ColimitForm getColimitForm() {
        return (ColimitForm)getForm();
    }

    
    /**
     * Returns the number of coordinates of the denotator.
     * 
     * @return number of coordinates
     */
    public int getFactorCount() {
        return 1;
    }


    /**
     * Returns the factor in position <code>i</code>.
     * In this case, always returns the sole factor of the colimit denotator.
     * 
     * @param i factor <code>i</code> denotator
     * @return the sole factor
     */
    public Denotator getFactor(int i) {
        return getFactor();
    }


    /**
     * Returns the sole factor of the colimit denotator.
     */
    public Denotator getFactor() {
        return getIndexMorphismMap().getFactor();
    }
    

    /**
     * Returns an iterator over the factors.
     * In this case there is only one factor.
     */
    @Override
    public Iterator<Denotator> iterator() {
        LinkedList<Denotator> list = new LinkedList<>();
        list.add(getFactor());
        return list.iterator();
    }


    /**
     * Sets the factor <code>d</code> in position <code>i</code>.
     * This is a destructive operation; beware of aliasing.
     * @throws IllegalStateException if the position i is not in the required range
     * @throws LatrunculusFormException if d is not of the required form
     */
    public void setFactor(int i, Denotator d)
            throws LatrunculusCheckedException {
        IndexMorphismMap indexmap = (IndexMorphismMap)getCoordinate().getMap();
        if (i >= getForm().getFormCount()) {
            throw new IllegalStateException(""+i+" >= "+getForm().getFormCount());
        }
        checkDenotator(d, getForm().getForm(i), getAddress());
        indexmap.setFactor(i, d);
        setIndex(i);
    }

    
    /**
     * Sets the factor <code>d</code> at the given label.
     * This is a destructive operation; beware of aliasing.
     *
     * @throws LatrunculusCheckedException if d is not of the required form
     *         or the label does not exist
     */
    public void setFactor(String label, Denotator d)
            throws LatrunculusCheckedException {
        int i = getColimitForm().labelToIndex(label);
        if (i < 0) {
            throw new LatrunculusCheckedException("ColimitDenotator.setFactor: Label %%1 does not exist", label);            
        }
        IndexMorphismMap indexmap = (IndexMorphismMap)getCoordinate().getMap();
        if (i >= getForm().getFormCount()) {
            throw new LatrunculusCheckedException("ColimitDenotator.setFactor: Expected index "+
                                      "< %1, but got %2", getForm().getFormCount(), i);
        }
        checkDenotator(d, getForm().getForm(i), getAddress());
        indexmap.setFactor(i, d);
    }
    

    public boolean appendFactor(Denotator d) {
        return false;
    }

    
    /**
     * Returns a list of the factors of the denotator.
     * In this case there is only one factor.
     */
    public List<Denotator> getFactors() {
        LinkedList<Denotator> list = new LinkedList<>();
        list.add(getFactor());
        return list;
    }


    /**
     * Returns the index of the factor contained in a colimit denotator.
     */
    public int getIndex() {
        return index;
    }
    
    
    /**
     * Returns the index label of the factor contained in a colimit denotator.
     */
    public String getLabel() {
        return getColimitForm().indexToLabel(getIndex());
    }

    
    /**
     * Sets the index of the factor contained in a colimit denotator.
     */
    private void setIndex(int index) {
        this.index = index;
    }

       
    @Override
    public int compareTo(Denotator object) {
        if (this == object) {
            return 0;
        }
        if (object instanceof ColimitDenotator) {
            return compareTo((ColimitDenotator)object);
        }
        return getForm().compareTo(object.getForm());
    }


    /**
     * Compares two colimit denotators.
     */
    public int compareTo(ColimitDenotator other) {
        int c = getForm().compareTo(other.getForm());
        if (c == 0) {
            c = nameCompareTo(other);
            if (c == 0) {
                IndexMorphismMap aMap = this.getIndexMorphismMap();
                IndexMorphismMap bMap = other.getIndexMorphismMap();
                c = aMap.compareTo(bMap);
            }
        }
        return c;
    }


    /**
     * Checks for equality.
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ColimitDenotator){
            return equals((ColimitDenotator)object);
        }
        else {
            return false;
        }
    }


    /**
     * Checks denotators of the same type for equality.
     */
    public boolean equals(ColimitDenotator other) {
        if (formEquals(other) && nameEquals(other)) {
            return getIndexMorphismMap().equals(other.getIndexMorphismMap());
        }
        else {
            return false;
        }
    }
    
    
    public IndexMorphismMap getIndexMorphismMap() {
        return (IndexMorphismMap)getCoordinate().getMap();
    }

    
    public IndexMorphismMap getFrameIndexMorphismMap() {
        return (IndexMorphismMap)getFrameCoordinate().getMap();
    }
    

    @Override
    public boolean isConstant() {
        return getIndexMorphismMap().isConstant();
    }


    @Override
    public ColimitDenotator deepCopy() {
        YonedaMorphism coord;
        YonedaMorphism frameCoord;
        if (getCoordinate() == getFrameCoordinate()) {
            coord = frameCoord = getCoordinate().deepCopy();
        }
        else {
            coord = getCoordinate().deepCopy();
            frameCoord = getCoordinate().deepCopy();
        }
        return new ColimitDenotator(getName(), getColimitForm(), getIndex(),
                                    coord, frameCoord);
    }


    @Override
    public boolean check() {
        // any exception indicates inconsistency
        try {
            // is the form of type colimit?
            if (getForm() instanceof ColimitForm) {
                ColimitForm f = (ColimitForm)getForm();
                Module address = getAddress();
                if (getIndex() < f.getFormCount()) {
                    // check consistency of the factor
                    Denotator d = getFactor();
                    Form factorForm = f.getForm(getIndex());
                    if (!d.check()) {
                        // the factor is inconsistent
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
                else {
                    return false;
                }
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
     * Creates a new limit denotator without checking whatsoever.
     * The arguments must result in a correct denotator, otherwise
     * there may be nasty consequences.
     */
    @Unsafe
    @Internal
    public static ColimitDenotator _make_unsafe(NameDenotator name, Module address,
                                                ColimitForm form, int index, Denotator d) {
        IndexMorphismMap map = new IndexMorphismMap(index, d);
        FormDiagram diagram = (FormDiagram)form.getIdentifier().getCodomainDiagram();
        CompoundMorphism coordinate = new CompoundMorphism(address, new ProperIdentityMorphism(diagram, FormDenotatorTypeEnum.COLIMIT), map);
        ColimitDenotator res = new ColimitDenotator(name, form, index, coordinate, coordinate);
        assert(res._is_valid());
        return res;
    }

    @Internal
    private boolean _is_valid() {
        if (getIndex() >= getColimitForm().getFormCount()) {
            return false;
        }
        try {
            checkDenotator(getFactor(), getColimitForm().getForm(getIndex()), getAddress());
        } catch (LatrunculusAddressException | LatrunculusFormException e) {
            return false;
        }
        return true;
    }
    
    
    /**
     * Generic colimit denotator constructor.
     */
    private ColimitDenotator(NameDenotator name, ColimitForm form, int index,
                             YonedaMorphism coordinate, YonedaMorphism frameCoordinate) {
        super(name, form, coordinate, frameCoordinate);
        setIndex(index);
    }
    
}
