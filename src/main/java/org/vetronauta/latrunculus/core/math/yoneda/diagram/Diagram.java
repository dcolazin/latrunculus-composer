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

package org.vetronauta.latrunculus.core.math.yoneda.diagram;

import java.util.List;
import java.util.Map;

import org.vetronauta.latrunculus.core.repository.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.yoneda.Yoneda;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

/**
 * Abstract base class for diagrams.
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */

public interface Diagram extends Yoneda, MathDefinition {

    /**
     * Returns the vertex at position <code>i</code>.
     */
    Yoneda getVertex(int i);

    /**
     * Returns the number of vertexes.
     */
    int getVertexCount();

    /**
     * Deletes the vertex at position <code>i</code>.
     */
    void deleteVertex(int i);
    
    /**
     * Deletes all vertexes in this diagram.
     */
    default void deleteVertexes() {
        for (int i = 0; i < getVertexCount(); i++) {
            deleteVertex(0);
        }
    }

    /**
     * Returns the <code>n</code>-th arrow from vertex <code>i</code> to vertex <code>j</code>.
     */
    YonedaMorphism getArrow(int i, int j, int n);

    /**
     * Returns the number of arrows from vertex <code>i</code> to vertex <code>j</code>.
     */
    int getArrowCount(int i, int j);

    /**
     * Inserts an arrow from vertex <code>i</code> to vertex <code>j</code> at position <code>n</code>.
     */
    void insertArrow(int i, int j, int n, YonedaMorphism morphism);

    /**
     * Appends an arrow from vertex <code>i</code> to vertex <code>j</code>.
     */
    default void appendArrow(int i, int j, YonedaMorphism morphism) {
        insertArrow(i, j, getArrowCount(i, j), morphism);
    }

    /**
     * Deletes the <code>n</code>-th arrow from vertex <code>i</code> to vertex <code>j</code>.
     */
    void deleteArrow(int i, int j, int n);
    
    /**
     * Deletes all arrows from vertex <code>i</code> to vertex <code>j</code>.
     */
    default void deleteArrows(int i, int j) {
        for (int n = 0; n < getArrowCount(i, j); n++) {
            deleteArrow(i, j, 0);
        }
    }
    
    List<Form> getFormDependencies(List<Form> list);

    List<Denotator> getDenotatorDependencies(List<Denotator> list);
    
    /**
     * Resolve references resulting from parsing.
     * 
     * @return true iff all references have been resolved
     */
    boolean resolveReferences(RubatoDictionary dict, Map<Object,Object> history);
    
    int compareTo(Yoneda object);
    
    /**
     * Returns a hash code for this diagram.
     */
    int hashCode();
    
    boolean fullEquals(Diagram d, Map<Object,Object> s);
}
