/*
 * Copyright (C) 2005 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.module.morphism;

import org.vetronauta.latrunculus.core.exception.CompositionException;
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.matrix.ArrayMatrix;
import org.vetronauta.latrunculus.core.math.matrix.Matrix;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.util.ArrayList;
import java.util.List;


/**
 * This class handles affine morphisms between free modules
 * over a given ring, that are not covered by specialized
 * classes such as ZFreeAfineMorphism, etc.
 * 
 * @author Gérard Milmeister
 */
public final class GenericAffineMorphism<A extends FreeElement<A, RA>, B extends FreeElement<B, RA>, RA extends RingElement<RA>>
        extends ModuleMorphism<A,B,RA,RA> {

    private final Ring<RA> ring;
    private final int domainDimension;
    private final int codomainDimension;
    private final Matrix<RA> matrix;
    private final List<RA> vector;

    public static <T extends RingElement<T>> GenericAffineMorphism<?,?,T> make(Ring<T> ring, int dim, int codim) {
        return new GenericAffineMorphism<>(ring.getFreeModule(dim), ring.getFreeModule(codim));
    }

    private GenericAffineMorphism(FreeModule<A,RA> domain, FreeModule<B,RA> codomain) {
        super(domain, codomain);
        ring = domain.getRing();
        domainDimension = domain.getDimension();
        codomainDimension = codomain.getDimension();
        matrix = new ArrayMatrix<>(domain.getRing(), codomainDimension, domainDimension);
        vector = new ArrayList<>(codomainDimension);
        for (int i = 0; i < codomainDimension; i++) {
            for (int j = 0; j < domainDimension; j++) {
                matrix.setToZero(i,j); //TODO init
            }
            vector.add(ring.getOne());
        }
    }

    public int getCodomainDimension() {
        return codomainDimension;
    }

    public int getDomainDimension() {
        return domainDimension;
    }

    public Matrix<RA> getMatrix() {
        return matrix;
    }

    public List<RA> getVector() {
        return vector;
    }
    

    /**
     * Sets the <code>i</code>,</code>j</code>-element of the
     * matrix to the specified <code>element</code>.
     */
    public void setMatrix(int i, int j, RA element) {
        if (ring.hasElement(element)) {
            matrix.set(i, j, element);
        }
    }
    

    /**
     * Sets the </code>i</code>-th element of the translation
     * vector to </code>element</code>.
     */
    public void setVector(int i, RA element) {
        if (ring.hasElement(element)) {
            vector.set(i, element);
        }
    }

    @Override
    public FreeModule<A,RA> getDomain() {
        return (FreeModule<A, RA>) super.getDomain();
    }

    @Override
    public FreeModule<B,RA> getCodomain() {
        return (FreeModule<B, RA>) super.getCodomain();
    }
    
    @Override
    public B map(A x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("GenericAffineMorphism.map: ", x, this);
        }
        try {
            List<RA> v = new ArrayList<>(domainDimension);
            List<RA> res = new ArrayList<>(codomainDimension);
            for (int i = 0; i < domainDimension; i++) {
                v.add(x.getComponent(i));
            }
            for (int i = 0; i < codomainDimension; i++) { //TODO this is the usual matrix multiplication that should be in the matrix class
                RA r = ring.getZero();
                for (int j = 0; j < domainDimension; j++) {
                    r.add(matrix.get(i,j).product(v.get(j)));
                }
                r.add(vector.get(i));
                res.add(r);
            }
            return getCodomain().createElement(res);
        } catch (DomainException e) {
            throw new AssertionError("This should never happen!"); //TODO false?
        }
    }

    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }
    
    @Override
    public boolean isRingHomomorphism() {
        if (domainDimension == 1 && codomainDimension == 1) {
            return vector.get(0).isZero() && (matrix.get(0,0).isOne() || matrix.get(0,0).isZero());
        }
        return false;
    }
    
    @Override
    public boolean isLinear() {
        for (RA ringElement : vector) {
            if (!ringElement.isZero()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isIdentity() {
        if (domainDimension != codomainDimension) {
            return false;
        }
        if (!isLinear()) {
            return false;
        }
        for (int i = 0; i < codomainDimension; i++) {
            for (int j = 0; j < domainDimension; j++) {
                if (i == j) {
                    if (!matrix.get(i,i).isOne()) {
                        return false;
                    }
                } else {
                    if (!matrix.get(i,j).isZero()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isConstant() {
        for (int i = 0; i < codomainDimension; i++) {
            for (int j = 0; j < domainDimension; j++) {
                if (!matrix.get(i,j).isZero()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphism<C,B,RC,RA> compose(ModuleMorphism<C,A,RC,RA> morphism)
            throws CompositionException {
        if (!(morphism instanceof GenericAffineMorphism)) {
            return super.compose(morphism);
        }
        if (!getDomain().equals(morphism.getCodomain())) {
            throw new CompositionException("GenericAffineMorphism.compose: ", this, morphism);
        }
        GenericAffineMorphism<?,A,RA> m = (GenericAffineMorphism<?,A,RA>) morphism;
        GenericAffineMorphism<?,A,RA> res = new GenericAffineMorphism(m.getDomain(), getCodomain());
        try {
            int k = res.getCodomain().getDimension();
            int l = res.getDomain().getDimension();
            int o = getDomain().getDimension();
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < l; j++) {
                    for (int n = 0; n < o; n++) {
                        res.matrix.get(i,j).sum(matrix.get(i,n).sum(m.matrix.get(n,j)));
                    }
                }
            }
            k = getCodomain().getDimension();
            l = m.getCodomain().getDimension();
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < l; j++) {
                    res.vector.get(i).add(matrix.get(i,j).product(m.vector.get(j)));
                }
                res.vector.get(i).sum(vector.get(i));
            }
        } catch (DomainException e) {
            throw new AssertionError("This should never happen!");
        }
        return (ModuleMorphism<C, B, RC, RA>) res;
    }
    
    @Override
    public ModuleMorphism<A,B,RA,RA> sum(ModuleMorphism<A,B,RA,RA> morphism) throws CompositionException {
        if (morphism instanceof GenericAffineMorphism) {
            GenericAffineMorphism<A,B,RA> m = (GenericAffineMorphism<A,B,RA>) morphism;
            if (!getDomain().equals(m.getDomain()) || !getCodomain().equals(m.getCodomain())) {
                throw new CompositionException("GenericAffineMorphism.sum: ", this, m);
            }
            GenericAffineMorphism<A,B,RA> res = new GenericAffineMorphism<>(getDomain(), getCodomain());
            try {
                for (int i = 0; i < codomainDimension; i++) {
                    for (int j = 0; j < domainDimension; j++) {
                        res.matrix.get(i,j).add(m.matrix.get(i,j));
                    }
                    res.vector.get(i).add(m.vector.get(i));
                }
            } catch (DomainException e) {
                throw new AssertionError("This should never happen!");
            }
            return res;
        }
        else {
            return super.sum(morphism);
        }
    }

    @Override
    public ModuleMorphism<A,B,RA,RA> difference(ModuleMorphism<A,B,RA,RA> morphism) throws CompositionException {
        if (morphism instanceof GenericAffineMorphism) {
            GenericAffineMorphism<A,B,RA> m = (GenericAffineMorphism<A,B,RA>) morphism;
            if (!getDomain().equals(m.getDomain()) || !getCodomain().equals(m.getCodomain())) {
                throw new CompositionException("GenericAffineMorphism.sum: ", this, m);
            }
            GenericAffineMorphism<A,B,RA> res = new GenericAffineMorphism<>(getDomain(), getCodomain());
            try {
                for (int i = 0; i < codomainDimension; i++) {
                    for (int j = 0; j < domainDimension; j++) {
                        res.matrix.get(i,j).subtract(m.matrix.get(i,j));
                    }
                    res.vector.get(i).subtract(m.vector.get(i));
                }
            } catch (DomainException e) {
                throw new AssertionError("This should never happen!");
            }
            return res;
        }
        else {
            return super.difference(morphism);
        }
    }    
    
    
    public IdentityMorphism<RA, RA> getRingMorphism() {
        return ring.getIdentityMorphism();
    }


    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof GenericAffineMorphism) {
            GenericAffineMorphism<?,?,?> m = (GenericAffineMorphism<?,?,?>) object;
            if (getDomain() != m.getDomain() && getCodomain() != m.getCodomain()) {
                return false;
            }
            return matrix.equals(m.getMatrix());
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "GenericAffineMorphism["+getDomain()+","+getCodomain()+"]";
    }

    public String getElementTypeName() {
        return "GenericAffineMorphism";
    }

}
