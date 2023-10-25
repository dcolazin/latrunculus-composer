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

import org.vetronauta.latrunculus.core.math.exception.DomainException;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic basis morphism is defined by the values at the basis
 * elements of the domain module.
 * 
 * @author Gérard Milmeister
 */
public class GenericBasisMorphism<A extends FreeElement<A,R>, B extends ModuleElement<B,R>, R extends RingElement<R>>
        extends ModuleMorphism<A,B,R,R> {

    private final List<B> fi;
    private final List<B> fit;

    private GenericBasisMorphism(FreeModule<A,R> domain, Module<B,R> codomain, List<B> fi) {
        super(domain, codomain);
        this.fi = fi;
        this.fit = new ArrayList<>();
        fit.add(fi.get(0));
        for (int i = 1; i < fi.size(); i++) {
            fit.add(fi.get(i).difference(fi.get(0)));
        }
    }

    /**
     * Creates generic basis morphism with the free module <code>domain</code>
     * and any <code>codomain</code>. Both modules must be over the same ring.
     * The array <code>fi</code> of module elements contains the element in
     * the codomain that the vectors
     * (0,0,0,0...), (1,0,0,0,...), (0,1,0,0,...), ...
     * in this order are mapped to. 
     * 
     * @throws DomainException if rings don't match
     */
    public static <X extends FreeElement<X,T>, Y extends ModuleElement<Y,T>, T extends RingElement<T>>
    GenericBasisMorphism<X,Y,T> make(FreeModule<X,T> domain, Module<Y,T> codomain, List<Y> fi) throws DomainException {
        List<Y> fis = new ArrayList<>(domain.getDimension()+1);
        for (int i = 0; i < domain.getDimension()+1; i++) {
            fis.add(codomain.getZero());
        }
        for (int i = 0; i < Math.min(fis.size(), fi.size()); i++) {
            if (codomain.hasElement(fi.get(i))) {
                fis.set(i, fi.get(i));
            }
            else {
                throw new DomainException(codomain, fi.get(i).getModule());
            }
        }
        return new GenericBasisMorphism<>(domain, codomain, fis);
    }
    
    @Override
    public B map(A x) throws MappingException {
        if (inDomain(x)) {
            B res = fit.get(0).deepCopy();
            for (int i = 0; i < x.getLength(); i++) {
                B tmp = fit.get(i+1).scaled(x.getRingElement(i));
                res.add(tmp);
            }
            return res;
        }
        else {
            throw new MappingException("GenericBasisMorphism.map: ", x, this);
        }
    }

    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }
    
    @Override
    public boolean isRingHomomorphism() {
        return false;
    }
    
    @Override
    public ModuleMorphism<R,R,R,R> getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }
    
    @Override
    public boolean isLinear() {
        return fi.get(0).isZero();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GenericBasisMorphism)) {
            return false;
        }
        GenericBasisMorphism<?,?,?> morphism = (GenericBasisMorphism<?,?,?>) object;
        if (!getDomain().equals(morphism.getDomain()) || !getCodomain().equals(morphism.getCodomain())) {
            return false;
        }
        for (int i = 0; i < fit.size(); i++) {
            if (!fit.get(i).equals(morphism.fit.get(i))) {
                return false;
            }
        }
        return true;
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("GenericBasisMorphism[");
        buf.append(getDomain());
        buf.append(",");
        buf.append(getCodomain());
        buf.append(",");
        buf.append("(");
        buf.append(fi.get(0));
        for (int i = 1; i < fi.size(); i++) {
            buf.append(",");
            buf.append(fi.get(i));
        }
        buf.append(")]");
        return buf.toString();
    }

    public List<B> getModuleElements() {
        return fi;
    }

    public String getElementTypeName() {
        return "GenericBasisMorphism";
    }

}
