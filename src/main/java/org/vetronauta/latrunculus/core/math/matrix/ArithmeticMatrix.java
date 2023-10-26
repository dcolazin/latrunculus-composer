package org.vetronauta.latrunculus.core.math.matrix;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;

import java.util.List;

public abstract class ArithmeticMatrix<N extends ArithmeticNumber<N>> extends Matrix<N> {

    //TODO make this a ModuleElement...

    protected ArithmeticMatrix(int rows, int columns) {
        super(rows, columns);
    }

    public abstract ArithmeticMatrix<N> product(ArithmeticMatrix<N> matrix);
    public abstract ArithmeticMatrix<N> sum(ArithmeticMatrix<N> matrix);
    public abstract ArithmeticMatrix<N> difference(ArithmeticMatrix<N> matrix);
    public abstract ArithmeticMatrix<N> scaled(ArithmeticElement<N> element);

    public abstract ArithmeticMultiElement<N> product(ArithmeticMultiElement<N> vector);
    public abstract ArithmeticElement<N> get(int i, int j);
    public abstract ArithmeticMultiElement<N> getColumn(int j);
    public abstract ArithmeticMultiElement<N> getRow(int i);

}
