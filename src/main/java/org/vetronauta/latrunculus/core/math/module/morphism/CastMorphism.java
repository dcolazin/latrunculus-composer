/*
 * Copyright (C) 2006 Gérard Milmeister
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
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.NumberRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;

import java.util.HashMap;
import java.util.Map;

/**
 * Morphism that casts elements from a ring to another ring.
 * The {@link #make} method is used to create a canonical morphism.
 * Only <i>proper</i> cast morphisms are created, not identities,
 * nor embeddings.
 * Therefore casting morphism should generally not be created directly
 * using this class, but through the {@link CanonicalMorphism}
 * class, this also takes care of free modules.
 * 
 * @author Gérard Milmeister
 */
public abstract class CastMorphism<A extends ModuleElement<A, RA>, B extends ModuleElement<B, RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
        extends ModuleMorphism<A,B,RA,RB> {


    private static final Map<Pair<Module<?,?>,Module<?,?>>,ModuleMorphism> castingMorphisms = new HashMap<>();

    protected CastMorphism(Module<A,RA> domain, Module<B,RB> codomain) {
        super(domain, codomain);
    }

    /**
     * Creates a casting morphism from <code>domain</code>
     * to </code>codomain</code>.
     * 
     * @return null iff no such morphism could be created
     */
    public static <RX extends RingElement<RX>, RY extends RingElement<RY>> ModuleMorphism<RX,RY,RX,RY> make(Ring<RX> domain, Ring<RY> codomain) {
        // check if the required morphism is in the cache
        Pair<Module<?,?>,Module<?,?>> pair = Pair.makePair(domain, codomain);
        ModuleMorphism morphism = castingMorphisms.get(pair);
        if (morphism == null) {
            // if not, try to create it
            morphism = makeCastingMorphism(domain, codomain);
            if (morphism != null) {
                // put it into the cache
                castingMorphisms.put(pair, morphism);
            }
        }
        return morphism;
    }

    public final RB map(RA x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("CastMorphism.map: ", x, this);
        }
        return mapValue(x);
    }

    /**
     * The low-level map method.
     * This must be implemented by subclasses.
     */
    public abstract RB mapValue(RA element);

    
    /**
     * Creates a casting morphism from <code>domain</code>
     * to <code>codomain</code>.
     * 
     * @return null iff noch such morphism could be created
     */
    private static <RX extends RingElement<RX>, RY extends RingElement<RY>> ModuleMorphism<RX,RY,RX,RY>
    makeCastingMorphism(final Ring<RX> domain, final Ring<RY> codomain) {
        if (domain instanceof StringRing) {
            return (ModuleMorphism<RX, RY, RX, RY>) makeStringRingMorphism((StringRing<?>)domain, codomain);
        }
        if (areArithmeticCompatible(domain, codomain)) {
            return new RingCastMorphism<>(domain, codomain);
        }
        return null;
    }

    private static boolean areArithmeticCompatible(Ring<?> domain, Ring<?> codomain) {
        if (domain instanceof NumberRing && codomain instanceof NumberRing) {
            return domain.compareTo(codomain) > 0;
        }
        return false;
    }

    private static <RX extends RingElement<RX>, RY extends RingElement<RY>> ModuleMorphism<StringMap<RX>,RY,StringMap<RX>,RY>
    makeStringRingMorphism(final StringRing<RX> domain, final Ring<RY> codomain) {
        if (codomain instanceof StringRing) {
            return new RingCastMorphism<>(domain, codomain);
        }
        final ModuleMorphism m = CanonicalMorphism.make(domain.getFactorRing(), codomain);
        if (m == null) {
            return null;
        }
        return new StringCastMorphism<>(m, domain, codomain);
    }


    /**
     * Casting morphisms are never module homomorphisms.
     */
    public boolean isModuleHomomorphism() {
        return false;
    }
    
    
    /**
     * Casting morphisms are never ring homomorphisms.
     */
    public boolean isRingHomomorphism() {
        return isRingMorphism();
    }

    
    public ModuleMorphism getRingMorphism() {
        return this;
    }
    
    
    public boolean equals(Object object) {
        if (object instanceof CastMorphism) {
            CastMorphism morphism = (CastMorphism)object;
            return getDomain().equals(morphism.getDomain()) &&
                   getCodomain().equals(morphism.getCodomain());
        }
        else {
            return false;
        }
    }

    public String toString() {
        return "CastMorphism["+getDomain()+","+getCodomain()+"]";
    }

    public String getElementTypeName() {
        return "CastMorphism";
    }

    private static class RingCastMorphism<RX extends RingElement<RX>, RY extends RingElement<RY>> extends CastMorphism<RX,RY,RX,RY> {

        protected RingCastMorphism(Ring<RX> domain, Ring<RY> codomain) {
            super(domain, codomain);
        }

        @Override
        public RY mapValue(RX element) {
            return getCodomain().getRing().cast(element);
        }

    }

    private static class StringCastMorphism<RX extends RingElement<RX>, RY extends RingElement<RY>, T extends RingElement<T>>
            extends CastMorphism<StringMap<RX>,RY,StringMap<RX>,RY> {

        private final ModuleMorphism<T,RY,T,RY> internalMorphism;

        protected StringCastMorphism(ModuleMorphism internalMorphism, StringRing<RX> domain, Ring<RY> codomain) {
            super(domain, codomain);
            this.internalMorphism = internalMorphism;
        }

        public StringRing<RX> getDomain() {
            return (StringRing<RX>) super.getDomain();
        }

        public RY mapValue(StringMap<RX> element) {
            T s = (T) getDomain().getFactorRing().getZero();
            Map<String, RX> terms = element.getTerms();
            try {
                for (RingElement x : terms.values()) {
                    s.add((T) x);
                }
                return internalMorphism.map(s);
            } catch (Exception ex) {
                throw new RuntimeException("This should never happen!");
            }
        }
    }

}
