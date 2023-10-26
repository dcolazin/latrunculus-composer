package org.vetronauta.latrunculus.core.math.matrix;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;

public abstract class ArithmeticMatrix<N extends ArithmeticNumber<N>> extends Matrix<N> {

    protected ArithmeticMatrix(int rows, int columns) {
        super(rows, columns);
    }

}
