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

import org.rubato.base.RubatoDictionary;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Math diagram class (vertexes are morphisms).
 *
 * @author Gérard Milmeister
 * @author Stefan Müller
 * @author Stefan Göller
 */
public class MathDiagram extends Diagram {

    /**
     * Creates a diagram with a single vertex.
     */
    public MathDiagram(YonedaMorphism vertex) {
        this(1);
        vertexes.add(1, vertex);
        arrows.add(1, new ArrayList<>());
    }

    
    /**
     * Creates a diagram with a list of vertexes.
     */
    public MathDiagram(List<YonedaMorphism> vertexes) {
        int size = vertexes.size();
        this.vertexes = new ArrayList<>(vertexes);
        this.arrows = new ArrayList<>(size*size);
        for (int i = 0; i < size*size; i++) {
            this.arrows.add(new ArrayList<>());
        }
    }
    

    /**
     * Creates a diagram with a list of vertexes and list of arrows.
     */
    public MathDiagram(List<YonedaMorphism> vertexes, List<ArrayList<YonedaMorphism>> arrows) {
        int size = vertexes.size();
        if(arrows.size() != size*size) {
            throw new IllegalArgumentException("mismatching vertex and arrow list sizes");
        }
        this.vertexes = new ArrayList<YonedaMorphism>(vertexes);
        this.arrows = new ArrayList<ArrayList<YonedaMorphism>>(size*size);
        for (int i = 0; i < size*size; i++) {
            this.arrows.add(new ArrayList<YonedaMorphism>(arrows.get(i)));
        }
    }
    

    /**
     * Returns the vertex at position <code>i</code>.
     */
    public Yoneda getVertex(int i) {
        return vertexes.get(i);
    }


    /**
     * Returns the number of vertexes.
     */
    public final int getVertexCount() {
        return vertexes.size();
    }


    /**
     * Sets the vertex at position <code>i</code> to <i>morphism<i>.
     */
    public void setVertex(int i, YonedaMorphism morphism) {
        set(i, morphism);
    }
    

    /**
     * Inserts the vertex <code>morphism</code> at position <code>i</code>.
     */
    public void insertVertex(int i, YonedaMorphism morphism) {
        insert(i, morphism);
    }
    
    
    public void insertVertex(int i, Yoneda yoneda) {
        insert(i, yoneda);
    }

    
    /**
     * Delete the vertex at position <code>i</code>.
     */
    public final void deleteVertex(int i) {
        delete(i);
    }
    

    /**
     * Returns the <code>n</code>-th arrow from vertex <code>i</code> to vertex <code>j</code>.
     */
    public final YonedaMorphism getArrow(int i, int j, int n) {
        return arrows.get(vertexes.size() * i + j).get(n);
    }


    /**
     * Returns the number of arrows from vertex <code>i</code> to vertex <code>j</code>.
     */
    public final int getArrowCount(int i, int j) {
        return arrows.get(vertexes.size() * i + j).size();
    }


    /**
     * Returns the number of arrows in the diagram.
     */
    public final int getArrowCount() {
        int c = 0;
        for (ArrayList<YonedaMorphism> a : arrows) {
            if (a != null) {
                c += a.size();
            }
        }
        return c;
    }

    
    /**
     * Inserts the arrow <code>morphism</code> from <code>i</code> to <code>j</code> at <code>n</code>.
     */
    public final void insertArrow(int i, int j, int n, YonedaMorphism morphism) {
        arrows.get(vertexes.size() * i + j).add(n, morphism);
    }


    /**
     * Deletes the <i>n<i>-th arrow from vertex <code>i</code> to vertex <i>j<i>.
     */
    public final void deleteArrow(int i, int j, int n) {
        arrows.get(vertexes.size() * i + j).remove(n);
    }


    public final void deleteVertexes() {
        vertexes.clear();
        arrows.clear();
    }


    public final void deleteArrows(int i, int j) {
        arrows.add(vertexes.size() * i + j, new ArrayList<YonedaMorphism>());
    }

    public MathDiagram deepCopy() {
        MathDiagram d = new MathDiagram(vertexes, arrows);
        
        //$$$RA how deep should we copy here?
        //int size = vertexes.size();
        //for (int i = 0; i < size; i++)
        //    d.vertexes.set(i, ((Yoneda)vertexes.get(i)).clone());
        //for (int i = 0; i < size*size; i++)
        //    d.arrows.set(i, ((Morphism)arrows.get(i)).clone());

        return d;
    }
    

    protected MathDiagram() {
        vertexes = new ArrayList<>();
        arrows = new ArrayList<>();
    }
        

    protected MathDiagram(int n) {
        vertexes = new ArrayList<YonedaMorphism>(n);
        arrows = new ArrayList<ArrayList<YonedaMorphism>>(n*n);
    }


    protected final ArrayList<YonedaMorphism> getVertexes() {
        return vertexes;
    }
    

    protected final ArrayList<ArrayList<YonedaMorphism>> getArrows() {
        return arrows;
    }    


    protected final void set(int i, Object object) {
        vertexes.set(i, (YonedaMorphism)object);
    }
    

    protected final void insert(int i, Object object) {
        int oldSize = vertexes.size();
        int newSize = oldSize+1;
        vertexes.add(i, (YonedaMorphism)object);
        ArrayList<ArrayList<YonedaMorphism>> a = new ArrayList<ArrayList<YonedaMorphism>>(newSize*newSize);
        ListIterator<ArrayList<YonedaMorphism>> m = arrows.listIterator();
        for (int ii = 0; ii < newSize; ii++) {
            for (int jj = 0; jj < newSize; jj++) {
                if((ii % newSize == i) || (jj % newSize == i)) {
                    a.add(new ArrayList<YonedaMorphism>());
                }
                else {
                    a.add(m.next());
                }
            }
        }
        arrows = a;
    }


    protected final void delete(int i) {
        int oldSize = vertexes.size();
        int newSize = oldSize-1;
        vertexes.remove(i);
        ArrayList<ArrayList<YonedaMorphism>> a = new ArrayList<ArrayList<YonedaMorphism>>(newSize*newSize);
        ListIterator<ArrayList<YonedaMorphism>> m = arrows.listIterator();
        for (int ii = 0; ii < oldSize; ii++) {
            for (int jj = 0; jj < oldSize; jj++) {
                if((ii % oldSize == i) || (jj % oldSize == i)) {
                    m.next();
                }
                else {
                    a.add(m.next());
                }
            }
        }    
        arrows = a;
    }
    
    
    public int compareTo(Yoneda object) {
        // TODO: not yet implemented
        throw new UnsupportedOperationException("Not implemented");
    }

    
    public int hashCode() {
        int hashcode = 7;
        ListIterator<YonedaMorphism> iter = vertexes.listIterator();
        while (iter.hasNext()) {
            hashcode = 37*hashcode + iter.next().hashCode();
        }
        return hashcode;
    }
    

    public boolean fullEquals(Diagram d, IdentityHashMap<Object,Object> s) {
        if (this == d) {
            return true;
        }
        else if (!(d instanceof MathDiagram)) {
            return false;
        }
        else {
            MathDiagram md = (MathDiagram) d;
            if (getVertexCount() != md.getVertexCount()) {
                return false;
            }
            for (int i = 0; i < getVertexCount(); i++) {
                if (!((YonedaMorphism)getVertex(i)).fullEquals((YonedaMorphism)md.getVertex(i), s)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    
    public boolean resolveReferences(RubatoDictionary dict, IdentityHashMap<?,?> history) {
        return true;
    }
    
    
    public LinkedList<Form> getFormDependencies(LinkedList<Form> list) {
        for (YonedaMorphism m : vertexes) {
            list = m.getFormDependencies(list);
        }
        for (ArrayList<YonedaMorphism> a : arrows) {
            for (YonedaMorphism m : a) {
                if (m != null) {
                    list = m.getFormDependencies(list);
                }
            }
        }
        return list;
    }
    

    public LinkedList<Denotator> getDenotatorDependencies(LinkedList<Denotator> list) {
        return list;
    }

    public String getElementTypeName() {
        return "MathDiagram";
    }
    

    private ArrayList<YonedaMorphism> vertexes;
    private ArrayList<ArrayList<YonedaMorphism>> arrows;
}
