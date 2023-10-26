package org.vetronauta.latrunculus.core.math.matrix;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;

import java.util.List;

public abstract class ArithmeticMatrix<N extends ArithmeticNumber<N>> extends Matrix<N> {

    protected ArithmeticMatrix(int rows, int columns) {
        super(rows, columns);
    }

    public abstract N get(int i, int j);
    public abstract List<N> getColumn(int j);
    public abstract List<N> getRow(int i);

}
