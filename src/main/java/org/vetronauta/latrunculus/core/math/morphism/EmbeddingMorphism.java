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

package org.vetronauta.latrunculus.core.math.morphism;

import org.rubato.util.Pair;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.generic.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.FreeModule;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.NumberRing;
import org.vetronauta.latrunculus.core.math.element.generic.ProductElement;
import org.vetronauta.latrunculus.core.math.module.generic.ProductRing;
import org.vetronauta.latrunculus.core.math.module.generic.Ring;
import org.vetronauta.latrunculus.core.math.element.generic.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.element.generic.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.PolynomialRing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Morphism that embeds one module into another.
 * The following embeddings are covered:<br>
 * - embeddings within the number hierarchy Z -> Q -> R -> C<br> 
 * - embeddings of number rings in polynomial rings<br>
 * - embeddings between polynomial rings<br>
 * - embeddings of number rings in string rings<br>
 * - embeddings between string rings<br>
 * - embeddings of rings in product rings<br>
 * - embeddings between product rings<br>
 * - embeddings between free modules if their rings embed<br>
 * 
 * @author Gérard Milmeister
 */
public abstract class EmbeddingMorphism<A extends ModuleElement<A, RA>, B extends ModuleElement<B, RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphism<A,B,RA,RB> {

    private static final HashMap<Pair<Module<?,?>,Module<?,?>>,ModuleMorphism> embeddingsCache = new HashMap<>();

    protected EmbeddingMorphism(final Module<A,RA> domain, final Module<B,RB> codomain) {
        super(domain, codomain);
    }

    /**
     * Creates an embedding from a module <code>domain</code>
     * into a module <code>codomain</code>.
     * 
     * @return null if no embedding of the requested kind can be built
     */
    public static <X extends ModuleElement<X, RX>, Y extends ModuleElement<Y, RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<X,Y,RX,RY> make(Module<X,RX> domain, Module<Y,RY> codomain) {
        Pair<Module<?,?>,Module<?,?>> pair = Pair.makePair(domain, codomain);
        ModuleMorphism m = embeddingsCache.get(pair);
        if (m != null) {
            return m;
        }
        if (domain instanceof FreeModule && codomain instanceof FreeModule) {
            m = make((FreeModule)domain, (FreeModule)codomain);
        } else {
            // TODO: embeddings for modules other than free modules
        }
        if (m != null) {
            embeddingsCache.put(pair, m);
        }
        return m;
    }

    /**
     * Creates an embedding from a free module <code>domain</code>
     * into a free module <code>codomain</code>.
     * 
     * @return null if no embedding of the requested kind can be built
     */
    private static <X extends FreeElement<X, RX>, Y extends FreeElement<Y, RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<X,Y,RX,RY> make(FreeModule<X,RX> domain, FreeModule<Y,RY> codomain) {
        if (domain.equals(codomain)) {
            // identity for equal domain and codomain
            return (ModuleMorphism<X,Y,RX,RY>) new IdentityMorphism<>(domain);
        }
        if (domain instanceof Ring && codomain instanceof Ring) {
            // ring embedding
            return make((Ring)domain, (Ring)codomain);
        }
        // free module (non-ring) embedding
        return makeFreeModuleEmbedding(domain, codomain);
    }

    /**
     * Creates an embedding from a ring <code>domain</code>
     * into a ring <code>codomain</code>.
     * 
     * @return null if no embedding of the requested kind can be built
     */
    private static <RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<RX,RY,RX,RY> make(Ring<RX> domain, Ring<RY> codomain) {
        if (domain.equals(codomain)) {
            // identity for equal domain and codomain
            return (ModuleMorphism<RX,RY,RX,RY>) new IdentityMorphism<>(domain);
        }
        if (codomain instanceof StringRing) {
            // embedding of a ring in a string ring
            return makeStringEmbedding(domain, (StringRing)codomain);
        }
        if (codomain instanceof ProductRing) {
            // embedding of a ring in a product ring
            return (ModuleMorphism<RX,RY,RX,RY>) makeProductRingEmbedding(domain, (ProductRing)codomain);
        }
        if (codomain instanceof PolynomialRing) {
            // embedding of a ring in a polynomial ring
            return makePolynomialEmbedding(domain, (PolynomialRing)codomain);
        }
        return makeRingEmbedding(domain, codomain);
    }


    public final B map(A x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("EmbeddingMorphism.map: ", x, this);
        }
        return mapValue(x);
    }
        
    /**
     * The low-level map method.
     * This must be implemented by subclasses.
     */
    public abstract B mapValue(A element);
    
    /**
     * Embeddings are always module homomorphisms, except for
     * embeddings of a Z_n ring into another ring.
     */
    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }


    @Override
    public boolean isRingHomomorphism() {
        return isRingMorphism();
    }

    
    public ModuleMorphism<RA,RB,RA,RB> getRingMorphism() {
        if (getDomain().isRing() && getCodomain().isRing()) {
            return (ModuleMorphism<RA,RB,RA,RB>) this;
        }
        return make(getDomain().getRing(), getCodomain().getRing());
    }

    @Override
    public final int compareTo(ModuleMorphism object) {
        if (object instanceof EmbeddingMorphism) {
            EmbeddingMorphism<?,?,?,?> m = (EmbeddingMorphism<?,?,?,?>)object;
            int comp = getDomain().compareTo(m.getDomain());
            if (comp == 0) {
                comp = getCodomain().compareTo(m.getCodomain());
            }
            return comp;
        }
        return super.compareTo(object);
    }
    
    @Override
    public final boolean equals(Object object) {
        if (object instanceof EmbeddingMorphism) {
            EmbeddingMorphism<?,?,?,?> m = (EmbeddingMorphism<?,?,?,?>)object;
            return getDomain().equals(m.getDomain()) && getCodomain().equals(m.getCodomain());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return 2 * getDomain().hashCode() + 3 * getCodomain().hashCode();
    }
    
    @Override
    public final String toString() {
        return "EmbeddingMorphism["+getDomain()+","+getCodomain()+"]";
    }

    public String getElementTypeName() {
        return "EmbeddingMorphism";
    }
    
    // Embeddings are inner classes
    //TODO proper classes...
    
    /**
     * Creates an embedding of a ring in another ring.
     */
    private static <RX extends RingElement<RX>, RY extends RingElement<RY>> EmbeddingMorphism<RX,RY,RX,RY>
    makeRingEmbedding(final Ring<RX> domain, final Ring<RY> codomain) {
        // domain is ZnRing
        if (domain instanceof ZnRing) {
            return (EmbeddingMorphism<RX,RY,RX,RY>) makeZnRingEmbeeding((ZnRing)domain, codomain);
        }
        // domain < codomain and both are Z, Q, R or C
        else if (areArithmeticCompatible(domain, codomain)) {
            return new RingEmbeddingMorphism<>(domain, codomain);
        }
        return null;
    }

    private static boolean areArithmeticCompatible(Ring<?> domain, Ring<?> codomain) {
        if (domain instanceof NumberRing && codomain instanceof NumberRing) {
            return domain.compareTo(codomain) < 0;
        }
        return false;
    }

    /**
     * Creates an embedding of a Z_n ring in another ring.
     * These are neither ring nor module homomorphisms.
     */
    private static <RX extends RingElement<RX>> EmbeddingMorphism<Modulus,RX, Modulus,RX> makeZnRingEmbeeding(final ZnRing domain, final Ring codomain) {
        if (codomain instanceof NumberRing) {
            // Zn -> Z, Q, R, C
            return new ZnRingEmbeddingMorphism<>(domain, codomain);
        }
        return null;
    }

    
    /**
     * Creates an embedding of a free module in another free module.
     */
    private static <X extends FreeElement<X, RX>, Y extends FreeElement<Y, RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    EmbeddingMorphism<X,Y,RX,RY> makeFreeModuleEmbedding(final FreeModule<X,RX> domain, final FreeModule<Y,RY> codomain) {
        // embeddings of free modules only if the dimension m of the codomain is
        // greater or equal than the dimension n of the domain.
        if (domain.getDimension() > codomain.getDimension()) {            
            return null;
        }

        // Free modules over number rings
        if (domain instanceof VectorModule &&
                codomain instanceof VectorModule &&
                areArithmeticCompatible(domain.getRing(), codomain.getRing())) {
            return (EmbeddingMorphism<X, Y, RX, RY>) new VectorEmbedding<>((VectorModule)domain, (VectorModule)codomain);
        }
        // Other free modules
        Ring<RX> domainRing = domain.getRing();
        Ring<RY> codomainRing = codomain.getRing();
        final ModuleMorphism<RX,RY,RX,RY> ringMorphism = make(domainRing, codomainRing);
        return ringMorphism != null ? new GenericFreeEmbedding<>(ringMorphism, domain, codomain) : null;
    }
    
    /**
     * Creates an embedding of a ring in a product ring, but only into one of the factors
     * @author Florian Thalmann 2013
     * 
     * @param domain a ring
     * @param codomain a product ring
     * @param index the index of the codomain factor where the ring should be embedded
     * @return an embedding or null if such an embedding cannot be constructed
     */
    public static <RX extends RingElement<RX>>
    EmbeddingMorphism<RX,ProductElement,RX,ProductElement> makeProductRingEmbedding(final Ring<RX> domain, final ProductRing codomain, final int index) {
        if (domain instanceof ProductRing) {
            return (EmbeddingMorphism<RX,ProductElement,RX,ProductElement>) makeProductRingEmbedding((ProductRing)domain, codomain);
        }
        return new ProductRingIndexEmbedding<>(make(domain, codomain.getFactors()[index]), index, domain, codomain);
    }

    
    /**
     * Creates an embedding of a ring in a product ring.
     * 
     * @param domain a ring
     * @param codomain a product ring
     * @return an embedding or null if such an embedding cannot be constructed
     */
    private static <RX extends RingElement<RX>> EmbeddingMorphism<RX,ProductElement,RX,ProductElement> makeProductRingEmbedding(final Ring<RX> domain, final ProductRing codomain) {
        if (domain instanceof ProductRing) {
            return (EmbeddingMorphism<RX,ProductElement,RX,ProductElement>) makeProductRingEmbedding((ProductRing)domain, codomain);
        }
        Ring[] factors = codomain.getFactors();
        final int len = factors.length;
        final ModuleMorphism[] morphisms = new ModuleMorphism[len];
        for (int i = 0; i < len; i++) {
            morphisms[i] = make(domain, factors[i]);
            if (morphisms[i] == null) {
                return null;
            }
        }
        return new ProductRingFullEmbedding<>(morphisms, domain, codomain);
    }
    
    
    /**
     * Creates an embedding of a product ring in a product ring.
     * 
     * @param domain a product ring
     * @param codomain a product ring
     * @return an embedding or null if such an embedding cannot be constructed
     */
    private static EmbeddingMorphism<ProductElement,ProductElement,ProductElement,ProductElement> makeProductRingEmbedding(ProductRing domain, ProductRing codomain) {
        if (domain.getFactorCount() != codomain.getFactorCount()) {
            return null;
        }
        final ModuleMorphism[] ems = new ModuleMorphism[domain.getFactorCount()];
        for (int i = 0; i < domain.getFactorCount(); i++) {
            Ring<?> d = domain.getFactor(i);
            Ring<?> c = codomain.getFactor(i);
            ModuleMorphism em = make(d, c);
            if (em == null) {
                return null;
            }
            ems[i] = em;
        }
        return new ProductRingEmbedding(ems, domain, codomain);
    }
    
    /**
     * Creates an embedding of a ring in a polynomial ring.
     * 
     * @param domain a ring
     * @param codomain a polynomial ring
     * @return an embedding or null if such an embedding cannot be constructed
     */
    private static <RX extends RingElement<RX>, RY extends RingElement<RY>>
    EmbeddingMorphism<RX,PolynomialElement<RY>,RX,PolynomialElement<RY>> makePolynomialEmbedding(final Ring<RX> domain, final PolynomialRing<RY> codomain) {
        Ring<RY> coeffRing = codomain.getCoefficientRing();
        if (domain instanceof PolynomialRing) {
            // TODO: not yet implemented
            throw new UnsupportedOperationException("Not implemented");
        } else if (coeffRing.equals(domain)) {
            return new ConstantPolynomialEmbedding<>(domain, (PolynomialRing) codomain);
        }
        final ModuleMorphism<RX,RY,RX,RY> ringMorphism = make(domain, coeffRing);
        if (ringMorphism == null) {
            return null;
        }
        return new RingConstantPolynomialEmbedding<>(ringMorphism, domain, codomain);
    }

    /**
     * Creates an embedding of ring in a string ring.
     * 
     * @param domain a ring
     * @param codomain a string ring
     * @return an embedding or null if such an embedding cannot be constructed
     */
    private static <RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<RX, StringMap<RY>,RX,StringMap<RY>> makeStringEmbedding(final Ring<RX> domain, final StringRing<RY> codomain) {
        if (domain instanceof StringRing) {
            return make(((StringRing<?>)domain).getFactorRing(), codomain.getFactorRing()) != null ?
                    new RingEmbeddingMorphism<>(domain, codomain) : null;
        }
        final Ring<?> coeffRing = codomain.getFactorRing();
        final ModuleMorphism<RX,?,RX,?> ringMorphism = make(domain, coeffRing);
        return ringMorphism != null ? new StringRingEmbedding<>(ringMorphism, domain, codomain) : null;
    }

    private static class RingEmbeddingMorphism<RX extends RingElement<RX>, RY extends RingElement<RY>>
        extends EmbeddingMorphism<RX,RY,RX,RY> {

        protected RingEmbeddingMorphism(Ring<RX> domain, Ring<RY> codomain) {
            super(domain, codomain);
        }

        @Override
        public RY mapValue(RX element) {
            return getCodomain().cast(element);
        }
    }

    private static class ZnRingEmbeddingMorphism<RX extends RingElement<RX>>
            extends RingEmbeddingMorphism<Modulus,RX> {

        protected ZnRingEmbeddingMorphism(ZnRing domain, Ring<RX> codomain) {
            super(domain, codomain);
        }

        @Override
        public boolean isRingHomomorphism() {
            return false;
        }

        @Override
        public boolean isModuleHomomorphism() {
            return false;
        }
    }

    private static class VectorEmbedding<RX extends RingElement<RX>, RY extends RingElement<RY>>
        extends EmbeddingMorphism<Vector<RX>,Vector<RY>,RX,RY> {

        protected VectorEmbedding(VectorModule<RX> domain, VectorModule<RY> codomain) {
            super(domain, codomain);
        }

        @Override
        public Vector<RY> mapValue(Vector<RX> element) {
            Ring<RY> ring = getCodomain().getRing();
            List<RY> list = element.getValue().stream()
                    .map(ring::cast)
                    .collect(Collectors.toList());
            return new Vector<>(ring, list);
        }
    }

    private static class GenericFreeEmbedding<X extends FreeElement<X, RX>, Y extends FreeElement<Y, RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
        extends EmbeddingMorphism<X,Y,RX,RY>{

        private final ModuleMorphism<RX,RY,RX,RY> ringMorphism;

        protected GenericFreeEmbedding(ModuleMorphism<RX,RY,RX,RY> ringMorphism, FreeModule<X, RX> domain, FreeModule<Y, RY> codomain) {
            super(domain, codomain);
            this.ringMorphism = ringMorphism;
        }

        @Override
        public Y mapValue(X element) {
            try {
                FreeElement<?,RX> fe = element.resize(getCodomain().getDimension());
                List<ModuleElement<?,?>> elements = new LinkedList<>();
                for (RX e : fe) {
                    elements.add(ringMorphism.map(e));
                }
                return getCodomain().createElement(elements);
            }
            catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }

        @Override
        public boolean isModuleHomomorphism() {
            return ringMorphism.isRingHomomorphism();
        }

    }

    private static class ProductRingIndexEmbedding<RA extends RingElement<RA>, RI extends RingElement<RI>>
        extends EmbeddingMorphism<RA,ProductElement,RA,ProductElement> {

        private final ModuleMorphism<RA,RI,RA,RI> morphism;
        private final int index;

        protected ProductRingIndexEmbedding(ModuleMorphism<RA,RI,RA,RI> morphism, int index, Ring<RA> domain, ProductRing codomain) {
            super(domain, codomain);
            this.morphism = morphism;
            this.index = index;
        }

        public ProductRing getCodomain() {
            return (ProductRing) super.getCodomain();
        }

        @Override
        public ProductElement mapValue(RA element) {
            RingElement[] factors0 = new RingElement[getCodomain().getFactorCount()];
            try {
                for (int i = 0; i < factors0.length; i++) {
                    factors0[i] = (RingElement) getCodomain().getFactor(i).getZero();
                }
                factors0[index] = morphism.map(element);
                return ProductElement.make(factors0);
            } catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }
    }

    private static class ProductRingFullEmbedding<RA extends RingElement<RA>>
            extends EmbeddingMorphism<RA,ProductElement,RA,ProductElement> {

        private final ModuleMorphism[] embeddings;

        protected ProductRingFullEmbedding(ModuleMorphism[] embeddings, Ring<RA> domain, ProductRing codomain) {
            super(domain, codomain);
            this.embeddings = embeddings;
        }

        @Override
        public ProductElement mapValue(RA element) {
            RingElement[] factors0 = new RingElement[((ProductRing)getCodomain()).getFactorCount()];
            try {
                for (int i = 0; i < factors0.length; i++) {
                    factors0[i] = (RingElement)embeddings[i].map(element);
                }
                return ProductElement.make(factors0);
            }
            catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }
    }

    private static class ProductRingEmbedding extends EmbeddingMorphism<ProductElement,ProductElement,ProductElement,ProductElement> {

        private final ModuleMorphism[] embeddings;

        protected ProductRingEmbedding(ModuleMorphism[] embeddings, ProductRing domain, ProductRing codomain) {
            super(domain, codomain);
            this.embeddings = embeddings;
        }

        @Override
        public ProductElement mapValue(ProductElement element) {
            RingElement[] factors = new RingElement[element.getFactorCount()];
            try {
                for (int i = 0; i < element.getFactorCount(); i++) {
                    factors[i] = (RingElement) embeddings[i].map(element.getFactor(i));
                }
                return ProductElement.make(factors);
            } catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }
    }

    private static class ConstantPolynomialEmbedding<RA extends RingElement<RA>> extends EmbeddingMorphism<RA,PolynomialElement<RA>,RA,PolynomialElement<RA>> {

        protected ConstantPolynomialEmbedding(Ring<RA> domain, PolynomialRing<RA> codomain) {
            super(domain, codomain);
        }

        @Override
        public PolynomialElement<RA> mapValue(RA element) {
            return new PolynomialElement<>((PolynomialRing<RA>) getCodomain(), element);
        }
    }

    private static class RingConstantPolynomialEmbedding<RA extends RingElement<RA>,RB extends RingElement<RB>>
        extends EmbeddingMorphism<RA,PolynomialElement<RB>,RA,PolynomialElement<RB>> {

        private final ModuleMorphism<RA,RB,RA,RB> ringMorphism;

        protected RingConstantPolynomialEmbedding(ModuleMorphism<RA,RB,RA,RB> ringMorphism, Ring<RA> domain, PolynomialRing<RB> codomain) {
            super(domain, codomain);
            this.ringMorphism = ringMorphism;
        }

        @Override
        public PolynomialElement<RB> mapValue(RA element) {
            try {
                RB coeff = ringMorphism.map(element);
                return new PolynomialElement<>((PolynomialRing<RB>) getCodomain(), coeff);
            } catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }
    }

    private static class StringRingEmbedding<RA extends RingElement<RA>,RB extends RingElement<RB>>
        extends EmbeddingMorphism<RA,StringMap<RB>,RA,StringMap<RB>>{

        private final ModuleMorphism<RA,?,RA,?> ringMorphism;

        protected StringRingEmbedding(ModuleMorphism<RA,?,RA,?> ringMorphism, Ring<RA> domain, StringRing<RB> codomain) {
            super(domain, codomain);
            this.ringMorphism = ringMorphism;
        }

        @Override
        public StringMap<RB> mapValue(RA element) {
            try {
                RingElement coeff = (RingElement)ringMorphism.map(element);
                return getCodomain().cast(coeff);
            } catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }
    }

}
