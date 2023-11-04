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

import org.apache.commons.collections4.CollectionUtils;
import org.vetronauta.latrunculus.core.exception.MappingException;
import org.vetronauta.latrunculus.core.math.arith.number.IntegerWrapper;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.matrix.ArrayMatrix;
import org.vetronauta.latrunculus.core.math.matrix.Matrix;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineFreeMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineMultiMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineRingMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.Endomorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.EndomorphismWrapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A split morphism describes a morphism from a free module into itself,
 * where the module is split into several parts, for example a <i>R^9</i> into
 * <i>R^3</i>, <i>R^2</i> and <i>R^4</i>. For each part a morphism is given, in the example
 * three morphisms <i>f1:R^3->R^3</i>, <i>f2:R^2->R^2</i> and <i>f3:R^4->R^4</i>.
 * 
 * @author Gérard Milmeister
 */
public class SplitMorphism <A extends FreeElement<A, RA>, RA extends RingElement<RA>> extends Endomorphism<A,RA> {

    private final List<ModuleMorphism> morphisms;

    /**
     * Creates a split morphism over <code>module</code> with
     * the given list of morphisms.
     */
    public static <X extends FreeElement<X,RX>, RX extends RingElement<RX>> Endomorphism<X,RX>
    make(FreeModule<X,RX> module, List<ModuleMorphism> morphisms) { //TODO proper signature
        if (!checkMorphisms(module, morphisms)) {
            return null;
        }
        if (morphisms.size() == 1) {
                // there is only one morphism, no need for split
            return (Endomorphism<X, RX>) morphisms.get(0);
        }
        if (areAllIdentity(morphisms)) {
            return getIdentityMorphism(module);
        }
        if (areAllConstants(morphisms)) {
            return buildSplitConstants(module, morphisms);
        }
        if (module instanceof VectorModule && areAllConsistentAffine(morphisms)) {
            return makeAffineFreeMorphism(module.getDimension(), morphisms);
        }
        return new SplitMorphism<>(module, morphisms);
    }

    private static <X extends FreeElement<X,RX>, RX extends RingElement<RX>> Endomorphism<X,RX>
    buildSplitConstants(FreeModule<X,RX> module, List<ModuleMorphism> morphisms) {
        List<ModuleElement<?, ?>> resList = new LinkedList<>();
        for (ModuleMorphism<?,?,?,?> m : morphisms) {
            Module<?,?> domain = m.getDomain();
            int dim = domain.getDimension();
            try {
                ModuleElement<?,?> res = m.unsafeMap(domain.getZero());
                for (int k = 0; k < dim; k++) {
                    resList.add(res.getComponent(k));
                }
            } catch (MappingException e) {
                return null;
            }
        }
        return getConstantMorphism(module.createElement(resList));
    }
    
    
    public A map(A x) throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("SplitMorphism.map: ", x, this);
        }
        LinkedList<ModuleElement<?,?>> resList = new LinkedList<>();
        for (ModuleMorphism<?, ?, RA, RA> m : morphisms) {
            Module<?, RA> domain = m.getDomain();
            int dim = domain.getDimension();
            List<ModuleElement<?, ?>> list = new LinkedList<>();
            for (int j = 0; j < dim; j++) {
                list.add(x.getComponent(j));
            }
            ModuleElement<?, RA> res = m.unsafeMap(domain.createElement(list));
            for (int k = 0; k < dim; k++) {
                resList.add(res.getComponent(k));
            }
        }
        return getDomain().createElement(resList);
    }

    public ModuleMorphism<RA,RA,RA,RA> getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }

    @Override
    public boolean isModuleHomomorphism() {
        for (ModuleMorphism<?,?,?,?> m : morphisms) {
            if (!m.isModuleHomomorphism()) {
                return false;
            }
        }
        return true;        
    }

    /**
     * Returns the morphisms that make up this split morphism
     * in the correct order.
     */
    public List<ModuleMorphism> getMorphisms() {
        return morphisms;
    }
    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }    
        if (!(object instanceof SplitMorphism)) {
            return false;
        }
        SplitMorphism<?,?> m = (SplitMorphism<?,?>)object;
        if (!getDomain().equals(m.getDomain()) || morphisms.size() != m.morphisms.size()) {
            return false;
        }
        for (int i = 0; i < morphisms.size(); i++) {
            if (!morphisms.get(i).equals(m.morphisms.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("SplitMorphism[");
        buf.append(getDomain());
        buf.append(",");
        buf.append(morphisms.get(0));
        for (int i = 1; i < morphisms.size(); i++) {
            buf.append(",");
            buf.append(morphisms.get(i));
        }
        buf.append("]");
        return buf.toString();
    }

    public String getElementTypeName() {
        return "SplitMorphism";
    }

    private static boolean checkMorphisms(FreeModule<?,?> module, List<ModuleMorphism> morphisms) {
        Ring<?> ring = module.getRing();
        int dim = 0;
        for (ModuleMorphism<?,?,?,?> m : morphisms) {
            Module<?,?> domain = m.getDomain();
            if (!domain.equals(m.getCodomain())) {
                return false;
            }
            if (!domain.getRing().equals(ring)) {
                return false;
            }
            dim += domain.getDimension();
        }
        return dim == module.getDimension();
    }

    private static boolean areAllConstants(List<ModuleMorphism> morphisms) {
        return morphisms.stream().allMatch(ModuleMorphism::isConstant);
    }

    private static boolean areAllIdentity(List<ModuleMorphism> morphisms) {
        return morphisms.stream().allMatch(ModuleMorphism::isIdentity);
    }

    private static boolean areAllConsistentAffine(List<ModuleMorphism> morphisms) {
        if (CollectionUtils.isEmpty(morphisms)) {
            return false;
        }
        Ring<?> baseRing = morphisms.get(0).getDomain().getRing();
        return morphisms.stream().allMatch(m -> isAffine(m) && baseRing.equals(m.getDomain().getRing()));
    }

    private static boolean isAffine(ModuleMorphism<?,?,?,?> morphism) {
        return morphism instanceof ArithmeticAffineFreeMorphism || morphism instanceof ArithmeticAffineRingMorphism;
    }

    private static Endomorphism makeAffineFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        ArithmeticRing ring = (ArithmeticRing) morphisms.get(0).getDomain().getRing();
        ArrayMatrix A = new ArrayMatrix(ring, dim, dim);
        List<RingElement> b = new ArrayList<>(dim);
        for (int i = 0; i < dim; i++) {
            b.add(null);
        }
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            //TODO injection/projection case
            if (m instanceof ArithmeticAffineMultiMorphism) {
                Matrix A1 = ((ArithmeticAffineMultiMorphism)m).getMatrix();
                Vector b1 = ((ArithmeticAffineMultiMorphism)m).getVector();
                int d = b1.getLength();
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b.set(i+j, b1.getComponent(j));
                }
                i += d;
            }
            else if (m instanceof ArithmeticAffineRingMorphism) {
                A.set(i, i, ((ArithmeticAffineRingMorphism)m).getA());
                b.set(i, ((ArithmeticAffineRingMorphism<IntegerWrapper>)m).getB());
            }
        }
        return new EndomorphismWrapper(ArithmeticAffineMultiMorphism.make(ring, A, new Vector<>(ring, b)));
    }

    private SplitMorphism(FreeModule<A,RA> module, List<ModuleMorphism> morphisms) {
        super(module);
        this.morphisms = morphisms;
    }

}
