package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.matrix.ArithmeticMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.generic.ArithmeticFreeMorphism;

import java.util.List;

public abstract class ArithmeticAffineFreeMorphism<A extends FreeElement<A, ArithmeticElement<N>>, B extends FreeElement<B, ArithmeticElement<N>>, N extends ArithmeticNumber<N>>
    extends ArithmeticFreeMorphism<A,B,N> {

    public static <X extends ArithmeticNumber<X>> ModuleMorphism<?,?,ArithmeticElement<X>,ArithmeticElement<X>>
    make(ArithmeticRing<X> ring, ArithmeticMatrix<X> matrix, List<X> vector) {
        if (matrix.getRowCount() != vector.size()) {
            throw new IllegalArgumentException("Rows of A don't match length of b.");
        }
        if (matrix.getColumnCount() == 1 && matrix.getRowCount() == 1) {
            return new ArithmeticAffineRingMorphism<>(ring, matrix.get(0, 0), vector.get(0));
        }
        if (matrix.getColumnCount() == 1) {
            return new ArithmeticAffineInjection<>(ring, matrix.getColumn(0), vector);
        }
        if (matrix.getRowCount() == 1) {
            return new ArithmeticAffineProjection<>(ring, matrix.getRow(0), vector.get(0));
        }
        return new ArithmeticAffineMultiMorphism<>(ring, matrix, vector);
    }

    protected ArithmeticAffineFreeMorphism(Module<A, ArithmeticElement<N>> domain, Module<B, ArithmeticElement<N>> codomain) {
        super(domain, codomain);
    }

    public ArithmeticRing<N> getBaseRing() {
        return (ArithmeticRing<N>) getDomain().getRing();
    }

    @Override
    public boolean isModuleHomomorphism() {
        return true;
    }

    @Override
    public boolean isLinear() {
        return getVector().stream().allMatch(ArithmeticElement::isZero);
    }

    public abstract List<ArithmeticElement<N>> getVector();

}
