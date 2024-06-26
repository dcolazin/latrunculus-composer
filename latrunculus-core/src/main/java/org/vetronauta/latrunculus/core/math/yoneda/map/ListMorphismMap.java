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

import org.vetronauta.latrunculus.core.repository.Dictionary;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.DenotatorReference;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Morphism map containing a list of morphisms (for limit and power types).
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public final class ListMorphismMap implements MorphismMap {
    
    /**
     * Creates a ListMorphismMap of initial capacity <code>n</code>.
     */
    public ListMorphismMap(int n) {
        list = new ArrayList<>(n);
    }
    

    /**
     * Creates a ListMorphismMap.
     */
    public ListMorphismMap(Collection<Denotator> denotators) {
        list = new ArrayList<>(denotators);
    }

    
    /**
     * Creates a ListMorphismMap of initial capacity 0.
     */
    public ListMorphismMap() {       
        this(0);        
    }
    

    /**
     * Returns the factor at position <code>index</code>.
     */
    public Denotator getFactor(int index) {
        return list.get(index);
    }


    /**
     * Sets the factor at position <code>index</code> to <code>d</code>.
     */
    public void setFactor(int index, Denotator d) {
        list.set(index, d); 
    }
    

    /**
     * Inserts the factor <code>d</code> at position <code>index</code>.
     */
    public void insertFactor(int index, Denotator d) {
        list.add(index, d); 
    }
    

    /**
     * Appends the factor <code>d</code>.
     */
    public void appendFactor(Denotator d) {
        list.add(d);
    }
    
    /**
     * Removes the factor at <code>index</code>.
     */
    public Denotator removeFactor(int index) {
    	if (index < list.size()) {
            return list.remove(index);
    	}
    	throw new IndexOutOfBoundsException(index+" > "+(list.size()-1));
    }
    

    /**
     * Prepends the factor <code>d</code>.
     */
    public void prependFactor(Denotator d) {
        list.add(0, d);
    }
    

    /**
     * Returns the number of factors.
     */
    public int getFactorCount() {
        return list.size();
    }

    
    /**
     * Returns the list of factors.
     */
    public ArrayList<Denotator> getFactors() {
        return list;
    }
    
    /**
     * Returns the position index of factor <code>d</code>
     */
    public int indexOf(Denotator d) {
    	return list.indexOf(d);
    }
    

    /**
     * Sorts the list.
     */
    public void sort() {
        Collections.sort(list);
    }
    
    
    /**
     * Returns true iff the list is actually a set, i.e.,
     * without null objects, sorted and without duplicates.
     */
    public boolean isSet() {
        if (list.size() < 2) {
            return true;
        }
        else {
            Iterator<Denotator> iter = list.iterator();
            Denotator d = iter.next();
            if (d == null) {
                return false;
            }
            while (iter.hasNext()) {
                Denotator next = iter.next();
                if (next == null || d.compareTo(next) >= 0) {
                    return false;
                }
                d = next;
            }
            return true;
        }
    }
    

    /**
     * Removes duplicates from the list.
     * Requires that the list has been sorted before.
     */
    public void removeDuplicates() {
        int size = list.size();
        if (size < 2) return;
        Denotator d = list.get(0);
        ArrayList<Denotator> newlist = new ArrayList<>();
        newlist.add(d);
        for (int i = 1; i < size; i++) {
            Denotator next = list.get(i);
            if (!d.equals(next)) {
                newlist.add(next);
                d = next;
            }
        }
        list = newlist;
    }


    /**
     * Returns an iterator over the factors.
     */
    public Iterator<Denotator> iterator() {
        return list.listIterator();    
    }


    public int compareTo(MorphismMap object) {
        if (this == object) {
            return 0;
        }
        else if (object instanceof ListMorphismMap) {
            return compareTo((ListMorphismMap)object);
        }
        else {
            return getClass().toString().compareTo(object.getClass().toString());
        }
    }
    
    
    public int compareTo(ListMorphismMap other) {
        int aCount = getFactorCount();
        int bCount = other.getFactorCount();   
        ArrayList<Denotator> aList = list;
        ArrayList<Denotator> bList = other.list;
        int c;
        for (int i = 0; i < Math.min(aCount, bCount); i++) {
            c = aList.get(i).compareTo(bList.get(i));
            if (c != 0) {
                return c;
            }
        }
        return bCount-aCount;
    }
    

    /**
     * Returns the list morphism map evaluated at address <code>element</code>.
     * 
     * @throws MappingException if evaluation fails
     */
    public MorphismMap at(ModuleElement element)
            throws MappingException {
        ArrayList<Denotator> newList = new ArrayList<Denotator>(list);
        boolean changed = false;
        for (int i = 0; i < newList.size(); i++) {            
            Denotator oldD = newList.get(i);
            Denotator newD = oldD.at(element);
            if (oldD != newD) {
                changed = true;
            }
            newList.set(i, newD);
        }
        if (changed) {
            return new ListMorphismMap(newList);
        }
        else {
            return this;
        }
    }

    
    public MorphismMap changeAddress(Module address) {
        ArrayList<Denotator> newList = new ArrayList<Denotator>();
        for (Denotator d : list) {
            Denotator newD = d.changeAddress(address);
            if (newD == null) {
                return null;
            }
            newList.add(newD);
        }
        return new ListMorphismMap(newList);
    }
    
    
    public MorphismMap changeAddress(ModuleMorphism morphism) {
        ArrayList<Denotator> newList = new ArrayList<Denotator>();
        for (Denotator d : list) {
            Denotator newD = d.changeAddress(morphism);
            if (newD == null) {
                return null;
            }
            newList.add(newD);
        }
        return new ListMorphismMap(newList);
    }
    
    
    public boolean isConstant() {
        for (Denotator d : list) {
            if (!d.isConstant()) {
                return false;
            }
        }
        return true;
    }
    
    public String getElementTypeName() {
        return "ListMorphismMap";
    }

    @Override
    public ListMorphismMap deepCopy() {
        ArrayList<Denotator> newList = new ArrayList<>(list.size());
        for (Denotator d : list) {
            newList.add(d.deepCopy());
        }
        return new ListMorphismMap(newList);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ListMorphismMap) {
            ListMorphismMap m = (ListMorphismMap)object;
            if (getFactorCount() != m.getFactorCount()) {
                return false;
            }
            else {
                for (int i = 0; i < getFactorCount(); i++) {
                    if (!getFactor(i).equals(m.getFactor(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    
    public boolean fullEquals(MorphismMap map, Map<Object,Object> s) {
        if (this == map) {
            return true;
        }
        else if (map instanceof ListMorphismMap) {
            ListMorphismMap lm = (ListMorphismMap)map;
            if (getFactorCount() != lm.getFactorCount()) {
                return false;
            }
            for (int i = 0; i < getFactorCount(); i++) {
                if (!(getFactor(i).equals(lm.getFactor(i)))) {
                   return false;
                }
            }
            return true;      
        }
        else {
            return false;
        }
    }


    public List<Form> getFormDependencies(List<Form> dependencyList) {
        return dependencyList;
    }

    
    public List<Denotator> getDenotatorDependencies(List<Denotator> depList) {
        for (Denotator d : list) {
            depList = d.getDependencies(depList);
        }
        return depList;
    }
    
    
    /**
     * Resolves all references.
     * @return true iff all references have been resolved.
     */
    public boolean resolveReferences(Dictionary dict, Map<Object,Object> history) {
        for (int i = 0; i < getFactorCount(); i++) {
            Denotator d = getFactor(i);
            if (d instanceof DenotatorReference) {
                Denotator newDenotator = dict.getDenotator(d.getNameString());
                if (newDenotator == null) {
                    return false;
                }
                setFactor(i, newDenotator);
            }
            else {
                return d.resolveReferences(dict, history);
            }
        }
        return true;
    }
    
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("ListMorphismMap[");
        if (list.size() > 0) {
           buf.append(list.get(0)); 
           for (int i = 1; i < list.size(); i++) {
               buf.append(",");
               buf.append(list.get(i));
           }
        }
        buf.append("]");
        return buf.toString();
    }

    
    /**
     * Returns a hash code of this list morphism map.
     */
    public int hashCode() {
        int hash = 7;
        for (Denotator d : list) {
            hash = 37*hash + d.hashCode();
        }
        return hash;
    }


    private ListMorphismMap(ArrayList<Denotator> list) {
        this.list = list;
    }
    
    
    private ArrayList<Denotator> list;
}
