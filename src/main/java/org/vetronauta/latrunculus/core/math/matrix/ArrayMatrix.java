/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.matrix;

import org.vetronauta.latrunculus.core.math.module.definition.FreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * @author vetronauta
 */
public class ArrayMatrix<R extends RingElement<R>> extends Matrix<R> {

    private RingElement[][] internalMatrix;
    private final Ring<R> ring;

    public ArrayMatrix(Ring<R> ring, int rows, int columns) {
        super(rows, columns);
        this.internalMatrix = makeArray(ring, rows, columns);
        this.ring = ring;
    }

    @Override
    public Matrix<R> product(Matrix<R> matrix) {
        if (!productPossible(matrix)) {
            throw new ArithmeticException("Unmatched matrix dimensions.");
        }
        ArrayMatrix<R> product = new ArrayMatrix<>(ring, rows, matrix.columns);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < matrix.columns; c++) {
                R sum = ring.getZero();
                for (int i = 0; i < columns; i++) {
                    sum = sum.sum(((R) internalMatrix[r][i]).product(matrix.get(i,c)));
                }
                product.internalMatrix[r][c] = sum;
            }
        }
        return product;
    }

    @Override
    public Matrix<R> sum(Matrix<R> matrix) {
        return null; //TODO
    }

    @Override
    public Matrix<R> difference(Matrix<R> matrix) {
        return null; //TODO
    }

    @Override
    public Matrix<R> scaled(R element) {
        return null; //TODO
    }

    @Override
    public Matrix<R> inverse() {
        return null; //TODO
    }

    @Override
    public FreeElement<?, R> product(FreeElement<?, R> vector) {
        return null; //TODO
    }

    @Override
    public R get(int i, int j) {
        return null; //TODO
    }

    @Override
    public FreeElement<?, R> getColumn(int j) {
        return null; //TODO
    }

    @Override
    public FreeElement<?, R> getRow(int i) {
        return null; //TODO
    }

    @Override
    public void set(int row, int col, R element) {
        //TODO
    }

    @Override
    public void setRowCount(int rows) {
        //TODO
    }

    @Override
    public void setColumnCount(int cols) {
        //TODO
    }

    @Override
    public void setToZeroMatrix() {
        //TODO
    }

    @Override
    public void setToZero(int row, int col) {
        //TODO
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

    private static RingElement[][] makeArray(Ring<?> ring, int rows, int columns) {
        RingElement[][] res = new RingElement[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                res[r][c] = ring.getZero();
            }
        }
        return res;
    }

}
