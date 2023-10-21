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

import org.vetronauta.latrunculus.core.DeepCopyable;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.generic.EndomorphismWrapper;
import org.vetronauta.latrunculus.core.math.module.morphism.generic.Endomorphism;

import java.io.Serializable;

/**
 * The abstract base class for morphisms in modules.
 * @author Gérard Milmeister
 */
public abstract class ModuleMorphism<A extends ModuleElement<A, RA>, B extends ModuleElement<B, RB>, RA extends RingElement<RA>, RB extends RingElement<RB>>
    implements Comparable<ModuleMorphism<?,?,?,?>>, Serializable, MathDefinition, DeepCopyable<ModuleMorphism<A,B,?,?>> {

    private final Module<A,RA> domain;
    private final Module<B,RB> codomain;

    /**
     * Creates a new morphism with <code>domain</code>
     * and <code>codomain<code> as indicated.
     */
    protected ModuleMorphism(Module<A,RA> domain, Module<B,RB> codomain) {
        this.domain = domain;
        this.codomain = codomain;
    }

    /**
     * Maps the element <code>x</code>.
     * This must be implemented for each specific morphism type.
     * 
     * @return the result of mapping element <code>x</code>
     * @throws MappingException if mapping of <code>element<code> fails
     */
    public abstract B map(A x) throws MappingException;

    protected B unsafeMap(ModuleElement<?,?> x) throws MappingException {
        return map((A) x);
    }

    
    /**
     * Returns the composition this*<code>morphism</code>.
     * 
     * @throws CompositionException if composition could not be performed
     */
    public <C extends ModuleElement<C,RC>, RC extends RingElement<RC>> ModuleMorphism<C,B,RC,RB> compose(ModuleMorphism<C,A,RC,RA> morphism) throws CompositionException {
        return CompositionMorphism.make(this, morphism);
    }

    
    /**
     * Returns the sum of this module morphism and <code>morphism</code>.
     * 
     * @throws CompositionException if sum could not be performed
     */
    public ModuleMorphism<A,B,RA,RB> sum(ModuleMorphism<A,B,RA,RB> morphism) throws CompositionException {
        return SumMorphism.make(this, morphism);
    }

    
    /**
     * Returns the difference of this module morphism and <code>morphism</code>.
     * 
     * @throws CompositionException if difference could not be performed
     */
    public ModuleMorphism<A,B,RA,RB> difference(ModuleMorphism<A,B,RA,RB> morphism) throws CompositionException {
        return DifferenceMorphism.make(this, morphism);
    }

    
    /**
     * Returns this module morphism scaled by <code>element</code>.
     */
    public ModuleMorphism<A,B,RA,RB> scaled(RB element) throws CompositionException {
        ModuleMorphism<A,B,RA,RB> m = ScaledMorphism.make(this, element);
        if (m == null) {
            throw new CompositionException("ModuleMorphism.scaled: "+this+" cannot be scaled by "+element);
        }
        return m;
    }
    
    
    /**
     * Returns the value of the morphism evaluated at the zero of the domain.
     */
    public B atZero() {
        try {
            return map((getDomain().getZero()));
        } catch (MappingException e) {
            throw new AssertionError("This should never happen!");
        }
    }
    
    /**
     * Returns the identity morphism in <code>module</code>.
     */
    public static <X extends ModuleElement<X,RX>, RX extends RingElement<RX>> IdentityMorphism<X,RX> getIdentityMorphism(Module<X,RX> module) {
        return new IdentityMorphism<>(module);
    }


    /**
     * Returns the constant <code>value</code> morphism in <code>module</code>.
     */
    public static <X extends ModuleElement<X,RX>, Y extends ModuleElement<Y,RY>, RX extends RingElement<RX>, RY extends RingElement<RY>>
    ModuleMorphism<X,Y,RX,RY> getConstantMorphism(Module<X,RX> module, Y value) {
        return new ConstantMorphism<>(module, value);
    }


    /**
     * Returns a constant morphism with the domain of this
     * morphism that returns the specified constant <code>value</code>.
     */
    public static <X extends ModuleElement<X,RX>, RX extends RingElement<RX>> Endomorphism<X,RX> getConstantMorphism(X value) {
        return new EndomorphismWrapper<>(ConstantMorphism.make(value));
    }

    
    /**
     * Returns true iff this morphism is the identity morphism.
     */
    public boolean isIdentity() {
        return false;
    }

    
    /**
     * Returns true iff this morphism is constant.
     */
    public boolean isConstant() {
        return false;
    }

    
    /**
     * Returns the domain of this morphism.
     */
    public Module<A,RA> getDomain() {
        return domain;
    }

    
    /**
     * Returns the codomain of this morphism.
     */
    public Module<B,RB> getCodomain() {
        return codomain;
    }

    
    /**
     * If true, then this is a module homomorphism.
     */
    public boolean isModuleHomomorphism() {
        return false;
    }
    

    /**
     * If true, then this is a ring homomorphism.
     */
    public boolean isRingHomomorphism() {
        return false;
    }

    
    /**
     * If true, then this is a morphism between rings;
     */
    public boolean isRingMorphism() {
        return getDomain().isRing() && getCodomain().isRing();
    }

    
    /**
     * Returns the the ring morphism that transforms between
     * the rings of the domain and codomain modules. 
     */
    public abstract ModuleMorphism<RA,RB,RA,RB> getRingMorphism();
    
    
    /**
     * Returns true iff this is a linear morphism.
     */
    public boolean isLinear() {
        return false;
    }

    
    /**
     * Returns true iff the composition <code>f</code>*<code>g</code>
     * is possible.
     */
    public static <X extends ModuleElement<X,RX>, Y extends ModuleElement<Y,RY>, Z extends ModuleElement<Z,RZ>,
            RX extends RingElement<RX>, RY extends RingElement<RY>, RZ extends RingElement<RZ>>
    boolean composable(ModuleMorphism<Y,Z,RY,RZ> f, ModuleMorphism<X,Y,RX,RY> g) {
        return f.getDomain().equals(g.getCodomain());
    }
    
    
    /**
     * Returns true iff element <code>x</code> is in the domain of the morphism.
     */
    public boolean inDomain(A x) {
        return domain.hasElement(x);
    }

    
    /**
     * Compares two module morphisms.
     * Checks first for equality.
     * The default comparison is on names, subclasses may implement
     * a more meaningful comparison.
     */
    public int compareTo(ModuleMorphism morphism) {
        if (this.equals(morphism)) {
            return 0;
        }
        else {
            return toString().compareTo(morphism.toString());
        }
    }
    
    
    /**
     * Returns true iff this morphism is equal to <code>object</code>.
     * In general it is not possible to determine whether to functions
     * are the same, so this returns true iff both morphisms have
     * the same structure.
     */
    public abstract boolean equals(Object object);
    

    public ModuleMorphism<A,B,RA,RB> deepCopy() {
        return this;
    }

    /**
     * Returns a string representation of this morphism.
     * This string is used for generic comparison.
     */
    public abstract String toString();

}
