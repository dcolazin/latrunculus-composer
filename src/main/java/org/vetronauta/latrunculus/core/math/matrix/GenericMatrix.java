package org.vetronauta.latrunculus.core.math.matrix;


import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticNumber;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;

import java.util.Arrays;

public class GenericMatrix<A extends ArithmeticNumber<A>> extends ArithmeticMatrix<A> {

    private Object[][] internalMatrix;

    public GenericMatrix(int rows, int columns) {
        super(rows, columns);
    }

    @Override
    public ArithmeticMatrix<A> product(ArithmeticMatrix<A> matrix) {
        return null; //TODO
    }

    @Override
    public ArithmeticMatrix<A> sum(ArithmeticMatrix<A> matrix) {
        return null; //TODO
    }

    @Override
    public ArithmeticMatrix<A> difference(ArithmeticMatrix<A> matrix) {
        return null; //TODO
    }

    @Override
    public ArithmeticMatrix<A> scaled(ArithmeticElement<A> element) {
        return null; //TODO
    }

    @Override
    public ArithmeticMatrix<A> inverse() {
        return null; //TODO
    }

    @Override
    public ArithmeticMultiElement<A> product(ArithmeticMultiElement<A> vector) {
        return null; //TODO
    }

    public void set(int i, int j, ArithmeticElement<A> element) {
        internalMatrix[i][j] = element;
    }

    @Override
    public ArithmeticElement<A> get(int i, int j) {
        return null; //TODO
    }

    @Override
    public ArithmeticMultiElement<A> getColumn(int j) {
        return null; //TODO
    }

    @Override
    public ArithmeticMultiElement<A> getRow(int i) {
        return null; //TODO
    }

    @Override
    public void setRowCount(int newRows) {
        if (this.rows == newRows) {
            return;
        }
        Object[][] coeffs = new Object[newRows][columns];
        int minRows = Math.min(newRows, this.rows);
        for (int r = 0; r < minRows; r++) {
            System.arraycopy(this.internalMatrix[r], 0, coeffs[r], 0, columns);
        }
        this.rows = newRows;
        this.internalMatrix = coeffs;
    }

    @Override
    public void setColumnCount(int newColumns) {
        if (this.columns == newColumns) {
            return;
        }
        Object[][] coeffs = new Object[rows][newColumns];
        int minCols = Math.min(newColumns, this.columns);
        for (int r = 0; r < rows; r++) {
            System.arraycopy(this.internalMatrix[r], 0, coeffs[r], 0, minCols);
        }
        this.columns = newColumns;
        this.internalMatrix = coeffs;
    }

    public void setToElementaryMatrix(A value) {
        for (int r = 0; r < rows; r++) {
            Arrays.fill(internalMatrix[r], value);
        }
    }

    @Override
    public void setToZeroMatrix() {
        setToElementaryMatrix(null);
    }

    @Override
    public void setToZero(int row, int col) {
        internalMatrix[row][col] = null;
    }

    @Override
    public void setToOne(int row, int col) {
        //TODO
    }

    @Override
    public void setToUnitMatrix() {
        //TODO
    }

    @Override
    public int rank() {
        return 0; //TODO
    }

    @Override
    public boolean isConstant() {
        return false; //TODO
    }

    @Override
    public boolean isZero() {
        return false; //TODO
    }

    @Override
    public boolean isUnit() {
        return false; //TODO
    }

    @Override
    public boolean isRegular() {
        return false; //TODO
    }

    @Override
    public boolean isZero(int row, int col) {
        return false; //TODO
    }

    @Override
    public boolean isOne(int row, int col) {
        return false; //TODO
    }

    @Override
    public String toString() {
        return null; //TODO
    }

    @Override
    public boolean equals(Object object) {
        return false; //TODO
    }

}
