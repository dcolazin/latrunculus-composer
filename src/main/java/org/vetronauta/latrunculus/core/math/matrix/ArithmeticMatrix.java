package org.vetronauta.latrunculus.core.math.matrix;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

public abstract class ArithmeticMatrix<N extends ArithmeticNumber<N>> extends Matrix<ArithmeticElement<N>> {

    protected ArithmeticMatrix(ArithmeticRing<N> ring, int rows, int columns) {
        super(ring, rows, columns);
    }

}
