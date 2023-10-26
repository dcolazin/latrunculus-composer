package org.vetronauta.latrunculus.core.math.module.morphism.affine;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.morphism.generic.ArithmeticFreeMorphism;

public abstract class ArithmeticAffineFreeMorphism<A extends FreeElement<A, ArithmeticElement<N>>, B extends FreeElement<B, ArithmeticElement<N>>, N extends ArithmeticNumber<N>>
    extends ArithmeticFreeMorphism<A,B,N> {

    protected ArithmeticAffineFreeMorphism(Module<A, ArithmeticElement<N>> domain, Module<B, ArithmeticElement<N>> codomain) {
        super(domain, codomain);
    }

}
