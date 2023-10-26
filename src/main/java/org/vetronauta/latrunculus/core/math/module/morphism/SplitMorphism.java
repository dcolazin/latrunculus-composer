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

import org.vetronauta.latrunculus.core.math.arith.number.Real;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.number.Modulus;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.exception.MappingException;
import org.vetronauta.latrunculus.core.math.matrix.CMatrix;
import org.vetronauta.latrunculus.core.math.matrix.QMatrix;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.matrix.ZMatrix;
import org.vetronauta.latrunculus.core.math.matrix.ZnMatrix;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.Modular;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineFreeMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.affine.ArithmeticAffineRingMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.EndomorphismWrapper;
import org.vetronauta.latrunculus.core.math.module.morphism.endo.Endomorphism;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.real.RRing;

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
    make(FreeModule<X,RX> module, List<ModuleMorphism> morphisms) {
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
        if (module.checkRingElement(ArithmeticElement.class)) {
            ArithmeticNumber<?> number = ((ArithmeticElement<?>) module.getZero()).getValue();
            if (number instanceof ArithmeticInteger && areAllZAffine(morphisms)) {
                return makeZFreeMorphism(module.getDimension(), morphisms);
            } else if (number instanceof Modulus && areAllZnAffine(morphisms)) {
                return makeZnFreeMorphism(module.getDimension(), ((Modular) module).getModulus(), morphisms); //TODO this might not work after the ArithmeticMultiModule refactoring
            } else if (number instanceof Rational && areAllQAffine(morphisms)) {
                return makeQFreeMorphism(module.getDimension(), morphisms);
            } else if (number instanceof Real && areAllRAffine(morphisms)) {
                return makeRFreeMorphism(module.getDimension(), morphisms);
            } else if (number instanceof Complex && areAllCAffine(morphisms)) {
                return makeCFreeMorphism((module).getDimension(), morphisms);
            }
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

    private static boolean areAllZAffine(List<ModuleMorphism> morphisms) {
        return morphisms.stream().allMatch(m -> isAffine(m) && m.getDomain().getRing() instanceof ZRing);
    }

    private static boolean areAllZnAffine(List<ModuleMorphism> morphisms) {
        return morphisms.stream().allMatch(m -> isAffine(m) && m.getDomain().getRing() instanceof ZnRing);
    }

    private static boolean areAllQAffine(List<ModuleMorphism> morphisms) {
        return morphisms.stream().allMatch(m -> isAffine(m) && m.getDomain().getRing() instanceof QRing);
    }

    private static boolean areAllRAffine(List<ModuleMorphism> morphisms) {
        return morphisms.stream().allMatch(m -> isAffine(m) && m.getDomain().getRing() instanceof RRing);
    }

    private static boolean areAllCAffine(List<ModuleMorphism> morphisms) {
        return morphisms.stream().allMatch(m -> isAffine(m) && m.getDomain().getRing() instanceof CRing);
    }

    private static boolean isAffine(ModuleMorphism<?,?,?,?> morphism) {
        return morphism instanceof ArithmeticAffineFreeMorphism || morphism instanceof ArithmeticAffineRingMorphism;
    }

    private static Endomorphism makeZFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        ZMatrix A = new ZMatrix(dim, dim);
        int[] b = new int[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof ZFreeAffineMorphism) {
                ZMatrix A1 = ((ZFreeAffineMorphism)m).getMatrix();
                int[] b1 = ((ZFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof ArithmeticAffineRingMorphism) {
                A.set(i, i, ((ArithmeticAffineRingMorphism<ArithmeticInteger>)m).getA().getValue().intValue());
                b[i] = ((ArithmeticAffineRingMorphism<ArithmeticInteger>)m).getB().getValue().intValue();
            }
        }
        return new EndomorphismWrapper(ZFreeAffineMorphism.make(A, b));
    }
    
    
    private static Endomorphism makeZnFreeMorphism(int dim, int modulus, List<ModuleMorphism> morphisms) {
        ZnMatrix A = new ZnMatrix(dim, dim, modulus);
        int[] b = new int[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof ZnFreeAffineMorphism) {
                ZnMatrix A1 = ((ZnFreeAffineMorphism)m).getMatrix();
                int[] b1 = ((ZnFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof ArithmeticAffineRingMorphism) {
                A.set(i, i, ((ArithmeticAffineRingMorphism<Modulus>)m).getA().getValue().intValue());
                b[i] = ((ArithmeticAffineRingMorphism<Modulus>)m).getB().getValue().intValue();
            }
        }
        return new EndomorphismWrapper(ZnFreeAffineMorphism.make(A, b));
    }
    
    
    private static Endomorphism makeQFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        QMatrix A = new QMatrix(dim, dim);
        Rational[] b = new Rational[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof QFreeAffineMorphism) {
                QMatrix A1 = ((QFreeAffineMorphism)m).getMatrix();
                Rational[] b1 = ((QFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof ArithmeticAffineRingMorphism) {
                A.set(i, i, ((ArithmeticAffineRingMorphism<Rational>)m).getA().getValue());
                b[i] = ((ArithmeticAffineRingMorphism<Rational>)m).getB().getValue();
            }
        }
        return new EndomorphismWrapper(QFreeAffineMorphism.make(A, b));
    }
    
    
    private static Endomorphism makeRFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        RMatrix A = new RMatrix(dim, dim);
        double[] b = new double[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof RFreeAffineMorphism) {
                RMatrix A1 = ((RFreeAffineMorphism)m).getMatrix();
                double[] b1 = ((RFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof ArithmeticAffineRingMorphism) {
                A.set(i, i, ((ArithmeticAffineRingMorphism<Real>)m).getA().getValue().doubleValue());
                b[i] = ((ArithmeticAffineRingMorphism<Real>)m).getB().getValue().doubleValue();
            }
        }
        return new EndomorphismWrapper(RFreeAffineMorphism.make(A, b));
    }
    
    
    private static Endomorphism makeCFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        CMatrix A = new CMatrix(dim, dim);
        Complex[] b = new Complex[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof CFreeAffineMorphism) {
                CMatrix A1 = ((CFreeAffineMorphism)m).getMatrix();
                Complex[] b1 = ((CFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.getNumber(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof ArithmeticAffineRingMorphism) {
                A.set(i, i, ((ArithmeticAffineRingMorphism<Complex>)m).getA().getValue());
                b[i] = ((ArithmeticAffineRingMorphism<Complex>)m).getB().getValue();
            }
        }
        return new EndomorphismWrapper(CFreeAffineMorphism.make(A, b));
    }
    
    private SplitMorphism(FreeModule<A,RA> module, List<ModuleMorphism> morphisms) {
        super(module);
        this.morphisms = morphisms;
    }

}
