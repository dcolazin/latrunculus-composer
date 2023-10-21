/*
 * Copyright (C) 2001, 2005 Gérard Milmeister
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

import org.rubato.util.Pair;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Canonical morphisms are the "simplest" morphisms that map
 * an element from the domain to the codomain, e.g, identities,
 * embeddings or casts.
 * The {@link #make} method is used to create a canonical morphism.
 * 
 * @author Gérard Milmeister
 */
public abstract class CanonicalMorphism<A extends ModuleElement<A, RA>, B extends ModuleElement<B, RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphism<A,B,RA,RB> {

    private static final HashMap<Pair<Module<?,?>,Module<?,?>>,ModuleMorphism> canonicalMorphisms = new HashMap<>();

    protected CanonicalMorphism(Module<A,RA> domain, Module<B,RB> codomain) {
        super(domain, codomain);
    }

    /**
     * Creates a canonical morphism from <code>domain</code>
     * to </code>codomain</code>.
     * 
     * @return null iff no such morphism could be created
     */
    public static <X extends ModuleElement<X,RX>, Y extends ModuleElement<Y,RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<X,Y,RX,RY> make(Module<X,RX> domain, Module<Y,RY> codomain) {
        // check if the required morphism is in the cache
        Pair<Module<?,?>,Module<?,?>> pair = Pair.makePair(domain, codomain);
        ModuleMorphism<X,Y,RX,RY> morphism = canonicalMorphisms.get(pair);
        if (morphism == null) {
            // if not, try to create it
            morphism = makeCanonicalMorphism(domain, codomain);
            if (morphism != null) {
                // put it into the cache
                canonicalMorphisms.put(pair, morphism);
            }
        }
        return morphism;
    }
    
    
    /**
     * Creates a canonical morphism from <code>domain</code>
     * to <code>codomain</code>.
     * 
     * @return null iff noch such morphism could be created
     */
    private static <X extends ModuleElement<X,RX>, Y extends ModuleElement<Y,RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<X,Y,RX,RY> makeCanonicalMorphism(final Module<X,RX> domain, final Module<Y,RY> codomain) {
        ModuleMorphism<X,Y,RX,RY> morphism;
        if (domain.isNullModule()) {
            morphism = new NullDomainMorphism<>(domain, codomain);
        }
        else if (codomain.isNullModule()) {
            morphism = new NullCodomainMorphism<>(domain, codomain);
        }
        else if (domain instanceof Ring && codomain instanceof Ring) {
            // handle canonical mappings from rings to rings separately
            // including embeddings
            morphism = (ModuleMorphism<X,Y,RX,RY>) makeRingMorphism((Ring<RX>)domain, (Ring<RY>)codomain);
        }
        else {
            // try to create a non-ring, e.g., free module, embedding
            morphism = EmbeddingMorphism.make(domain, codomain);
        }
        
        if (morphism == null && domain instanceof FreeModule && codomain instanceof FreeModule) {
            // nothing from the above has succeeded
            // now try to create a canonical morphism on
            // the underlying rings of the domain and codomain modules
            // provided that the domain and codomain are free modules
            Ring<RX> domainRing = domain.getRing();
            Ring<RY> codomainRing = codomain.getRing();
            ModuleMorphism<RX,RY,RX,RY> ringMorphism = makeRingMorphism(domainRing, codomainRing);
            if (ringMorphism != null) {
                // now extend this mapping to the free modules
                morphism = (ModuleMorphism<X,Y,RX,RY>) makeFreeModuleMorphism(ringMorphism, domain.getDimension(), codomain.getDimension());
            }
        }

        // other types of morphisms, for example non-free modules, are
        // not yet supported
        
        return morphism;
    }
    
    
    /**
     * Creates a canonical morphism from the domain ring to the
     * codomain ring.
     * 
     * @return null iff no such morphism could be created
     */
    protected static <RX extends RingElement<RX>, RY extends RingElement<RY>> ModuleMorphism<RX,RY,RX,RY>
    makeRingMorphism(Ring<RX> domainRing, Ring<RY> codomainRing) {
        ModuleMorphism<RX,RY,RX,RY> morphism;
        
        if (domainRing instanceof ZRing && codomainRing instanceof ZnRing) {
            // the special case of the mapping of integers to modular integers
            // this must be handled seperately, since it is not an embedding
            return ModuloMorphism.make(((ZnRing)codomainRing).getModulus());
        }
        else {
            // first try to create an embedding between the rings
            morphism = EmbeddingMorphism.make(domainRing, codomainRing);
            if (morphism == null) {
                // if this doesn't work create a casting morphism as a last resort
                morphism = CastMorphism.make(domainRing, codomainRing); 
            }
        }
        
        return morphism;
    }
    
    
    /**
     * Creates a canonical morphism between the free module of dimension <code>dim</code>
     * over the ring <code>domainRing</code> to the free module of dimension <code>codim</code>
     * over the ring <code>codomainRing</code> using a provided canonical morphism
     * <code>ringMorphism</code> between the rings.
     * If the domain dimension is less than the codomain dimension, the mapped value
     * is filled up with zeros.
     * If the domain dimension is greater then the codomain dimension, the
     * excess components are omitted.  
     */
    private static <RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<? extends FreeElement<?,RX>, ? extends FreeElement<?,RY>, RX,RY> makeFreeModuleMorphism(
            final ModuleMorphism<RX,RY,RX,RY> ringMorphism, final int dim, final int codim) {
        final FreeModule<?, RX> domain = ringMorphism.getDomain().getRing().getFreeModule(dim);
        final FreeModule<?, RY> codomain = ringMorphism.getCodomain().getRing().getFreeModule(codim);
        return new FreeEmbedding(domain, codomain, ringMorphism); //TODO FIX
    }

    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }

    @Override
    public boolean isRingHomomorphism() {
        return isRingMorphism();
    }

    
    public boolean equals(Object object) {
        if (object instanceof CanonicalMorphism) {
            CanonicalMorphism<?,?,?,?> morphism = (CanonicalMorphism<?,?,?,?>)object;
            return getDomain().equals(morphism.getDomain()) &&
                   getCodomain().equals(morphism.getCodomain());
        }
        else {
            return false;
        }
    }

    
    public String toString() {
        return "CanonicalMorphism["+getDomain()+","+getCodomain()+"]";
    }

    public String getElementTypeName() {
        return "CanonicalMorphism";
    }

    private static class NullDomainMorphism<X extends ModuleElement<X,RX>, Y extends ModuleElement<Y,RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
            extends CanonicalMorphism<X,Y,RX,RY> {

        protected NullDomainMorphism(Module<X,RX> domain, Module<Y,RY> codomain) {
            super(domain, codomain);
        }

        public Y map(X x) throws MappingException {
            if (!getDomain().hasElement(x)) {
                throw new MappingException("CanonicalMorphism.map: ", x, this);
            }
            return zero;
        }

        public ModuleMorphism<RX,RY,RX,RY> getRingMorphism() {
            return makeRingMorphism(getDomain().getRing(), getCodomain().getRing());
        }

        private final Y zero = getCodomain().getZero();
    }

    private static class NullCodomainMorphism<X extends ModuleElement<X,RX>, Y extends ModuleElement<Y,RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
            extends CanonicalMorphism<X,Y,RX,RY> {

        protected NullCodomainMorphism(Module<X,RX> domain, Module<Y,RY> codomain) {
            super(domain, codomain);
        }

        public Y map(X x) throws MappingException {
            if (!getDomain().hasElement(x)) {
                throw new MappingException("CanonicalMorphism.map: ", x, this);
            }
            return zero;
        }
        public ModuleMorphism<RX,RY,RX,RY> getRingMorphism() {
            return makeRingMorphism(getDomain().getRing(), getCodomain().getRing());
        }
        private final Y zero = getCodomain().getZero();
    }

    private static class FreeEmbedding<X extends FreeElement<X,RX>, Y extends FreeElement<Y,RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
            extends CanonicalMorphism<X,Y,RX,RY> {

        private final ModuleMorphism<RX,RY,RX,RY> ringMorphism;

        protected FreeEmbedding(Module<X,RX> domain, Module<Y,RY> codomain, ModuleMorphism<RX,RY,RX,RY> ringMorphism) {
            super(domain, codomain);
            this.ringMorphism = ringMorphism;
        }

        public Y map(X x) throws MappingException {
            if (!getDomain().hasElement(x)) {
                throw new MappingException("CanonicalMorphism.map: ", x, this);
            }
            FreeElement<?,RX> e = x.resize(getCodomain().getDimension());
            List<ModuleElement<?,?>> elements = new LinkedList<>();
            for (RingElement<RX> element : e) {
                elements.add(ringMorphism.map((RX) element)); //TODO FIX
            }
            return getCodomain().createElement(elements);
        }

        public ModuleMorphism<RX,RY,RX,RY> getRingMorphism() {
            return ringMorphism;
        }

    }

}
