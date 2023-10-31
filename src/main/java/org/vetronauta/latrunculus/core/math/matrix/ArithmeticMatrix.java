package org.vetronauta.latrunculus.core.math.matrix;

import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;

public abstract class ArithmeticMatrix<N extends ArithmeticNumber<N>> extends Matrix<ArithmeticElement<N>> {

    protected ArithmeticMatrix(int rows, int columns) {
        super(rows, columns);
    }

    public abstract ArithmeticMatrix<N> product(Matrix<ArithmeticElement<N>> matrix);
    public abstract ArithmeticMatrix<N> sum(Matrix<ArithmeticElement<N>> matrix);
    public abstract ArithmeticMatrix<N> difference(Matrix<ArithmeticElement<N>> matrix);
    public abstract ArithmeticMatrix<N> scaled(ArithmeticElement<N> element);
    public abstract ArithmeticMatrix<N> inverse();

    //TODO handle the case of 1-dim elements...
    public abstract ArithmeticMultiElement<N> product(FreeElement<?,ArithmeticElement<N>> vector);
    public abstract ArithmeticMultiElement<N> getColumn(int j);
    public abstract ArithmeticMultiElement<N> getRow(int i);

}
