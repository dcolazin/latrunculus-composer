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

import java.util.IdentityHashMap;
import java.util.LinkedList;

import org.rubato.base.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.MathDefinition;

/**
 * Abstract base class for diagrams.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */

public abstract class Diagram implements Yoneda, MathDefinition {

    /**
     * Returns the vertex at position <code>i</code>.
     */
    public abstract Yoneda getVertex(int i);

    /**
     * Returns the number of vertexes.
     */
    public abstract int getVertexCount();

    /**
     * Deletes the vertex at position <code>i</code>.
     */
    public abstract void deleteVertex(int i);
    
    /**
     * Deletes all vertexes in this diagram.
     */
    public void deleteVertexes() {
        for (int i = 0; i < getVertexCount(); i++) {
            deleteVertex(0);
        }
    }

    /**
     * Returns the <code>n</code>-th arrow from vertex <code>i</code> to vertex <code>j</code>.
     */
    public abstract YonedaMorphism getArrow(int i, int j, int n);

    /**
     * Returns the number of arrows from vertex <code>i</code> to vertex <code>j</code>.
     */
    public abstract int getArrowCount(int i, int j);

    /**
     * Inserts an arrow from vertex <code>i</code> to vertex <code>j</code> at position <code>n</code>.
     */
    public abstract void insertArrow(int i, int j, int n, YonedaMorphism morphism);

    /**
     * Appends an arrow from vertex <code>i</code> to vertex <code>j</code>.
     */
    public void appendArrow(int i, int j, YonedaMorphism morphism) {
        insertArrow(i, j, getArrowCount(i, j), morphism);
    }

    /**
     * Deletes the <code>n</code>-th arrow from vertex <code>i</code> to vertex <code>j</code>.
     */
    public abstract void deleteArrow(int i, int j, int n);
    
    /**
     * Deletes all arrows from vertex <code>i</code> to vertex <code>j</code>.
     */
    public void deleteArrows(int i, int j) {
        for (int n = 0; n < getArrowCount(i, j); n++) {
            deleteArrow(i, j, 0);
        }
    }
    
    public abstract LinkedList<Form> getFormDependencies(LinkedList<Form> list);
    
    public abstract LinkedList<Denotator> getDenotatorDependencies(LinkedList<Denotator> list);
    
    /**
     * Resolve references resulting from parsing.
     * 
     * @return true iff all references have been resolved
     */
    abstract boolean resolveReferences(RubatoDictionary dict, IdentityHashMap<?,?> history);    
    
    public abstract int compareTo(Yoneda object);

    /**
     * Returns a copy of this diagram.
     */
    public abstract Object clone();
    
    /**
     * Returns a hash code for this diagram.
     */
    public abstract int hashCode();
    
    /**
     * Returns true if this diagram is equal to the specified object.
     */
    public boolean equals(Object object) { 
        return (compareTo((Yoneda)object) == 0);
    }
    
    public abstract boolean fullEquals(Diagram d, IdentityHashMap<Object,Object> s);
}
