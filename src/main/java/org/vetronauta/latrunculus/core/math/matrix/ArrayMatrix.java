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
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vetronauta
 */
public class ArrayMatrix<R extends RingElement<R>> extends Matrix<R> {

    //TODO deepcopy?

    private RingElement[][] internalMatrix;

    public ArrayMatrix(Ring<R> ring, int rows, int columns) {
        super(ring, rows, columns);
        this.internalMatrix = makeArray(ring, rows, columns);
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
        if (!sameSize(matrix)) {
            throw new ArithmeticException("Unmatched matrix dimensions.");
        }
        ArrayMatrix<R> sum = new ArrayMatrix<>(ring, rows, columns);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                sum.set(r, c, ((R) internalMatrix[r][c]).sum(matrix.get(r, c)));
            }
        }
        return sum;
    }

    @Override
    public Matrix<R> difference(Matrix<R> matrix) {
        if (!sameSize(matrix)) {
            throw new ArithmeticException("Unmatched matrix dimensions.");
        }
        ArrayMatrix<R> difference = new ArrayMatrix<>(ring, rows, columns);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                difference.set(r, c, ((R) internalMatrix[r][c]).difference(matrix.get(r, c)));
            }
        }
        return difference;
    }

    @Override
    public Matrix<R> scaled(R element) {
        ArrayMatrix<R> scaled = new ArrayMatrix<>(ring, rows, columns);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                scaled.set(r, c, ((R) internalMatrix[r][c]).product(element));
            }
        }
        return scaled;
    }

    @Override
    public Matrix<R> inverse() {
        return null; //TODO
    }

    @Override
    public FreeElement<?, R> product(FreeElement<?, R> vector) {
        if (columns != vector.getLength()) {
            throw new ArithmeticException("Unmatched matrix dimensions.");
        }
        List<RingElement<R>> res = new ArrayList<>(rows);
        for (int r = 0; r < rows; r++) {
            R sum = ring.getZero();
            for (int c = 0; c < columns; c++) {
                sum = sum.sum(((R)internalMatrix[r][c]).product(vector.getComponent(c)));
            }
            res.add(sum);
        }
        //TODO this will work properly after the ArithmeticNumber refactoring
        return ArithmeticMultiElement.make((ArithmeticRing) ring, (List) res);
    }

    @Override
    public R get(int i, int j) {
        return (R) internalMatrix[i][j];
    }

    @Override
    public FreeElement<?, R> getColumn(int j) {
        List<R> list = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            list.add((R) internalMatrix[i][j]);
        }
        //TODO this will work properly after the ArithmeticNumber refactoring
        return ArithmeticMultiElement.make((ArithmeticRing) ring, (List) list);
    }

    @Override
    public FreeElement<?, R> getRow(int i) {
        List<R> list = new ArrayList<>(columns);
        for (int j = 0; j < columns; j++) {
            list.add((R) internalMatrix[i][j]);
        }
        //TODO this will work properly after the ArithmeticNumber refactoring
        return ArithmeticMultiElement.make((ArithmeticRing) ring, (List) list);
    }

    @Override
    public void set(int row, int col, R element) {
        internalMatrix[row][col] = element;
    }

    @Override
    public void setRowCount(int rows) {
        if (this.rows == rows) {
            return;
        }
        RingElement[][] newInternalMatrix = makeArray(ring, rows, columns);
        int minRows = Math.min(rows, this.rows);
        for (int r = 0; r < minRows; r++) {
            System.arraycopy(this.internalMatrix[r], 0, newInternalMatrix[r], 0, columns);
        }
        this.rows = rows;
        this.internalMatrix = newInternalMatrix;
    }

    @Override
    public void setColumnCount(int cols) {
        if (this.columns == cols) {
            return;
        }
        RingElement[][] newInternalMatrix = makeArray(ring, rows, cols);
        int minCols = Math.min(cols, this.columns);
        for (int r = 0; r < rows; r++) {
            System.arraycopy(this.internalMatrix[r], 0, newInternalMatrix[r], 0, minCols);
        }
        this.columns = cols;
        this.internalMatrix = newInternalMatrix;
    }

    @Override
    public void setToZeroMatrix() {
        setToElementaryMatrix(ring.getZero());
    }

    @Override
    public void setToZero(int row, int col) {
        internalMatrix[row][col] = ring.getZero();
    }

    @Override
    public void setToOne(int row, int col) {
        internalMatrix[row][col] = ring.getOne();
    }

    @Override
    public void setToUnitMatrix() {
        if (rows > columns) {
            columns = rows;
        } else if (rows < columns) {
            rows = columns;
        }
        internalMatrix = makeArray(ring, rows, rows);
        for (int rc = 0; rc < rows; rc++) {
            internalMatrix[rc][rc] = ring.getOne();
        }
    }

    @Override
    public int rank() {
        return 0; //TODO
    }

    @Override
    public boolean isConstant() {
        RingElement<?> coeff = internalMatrix[0][0];
        return Arrays.stream(internalMatrix)
                .flatMap(Arrays::stream)
                .allMatch(coeff::equals);
    }

    @Override
    public boolean isZero() {
        return Arrays.stream(internalMatrix)
                .flatMap(Arrays::stream)
                .allMatch(RingElement::isZero);
    }

    @Override
    public boolean isUnit() {
        if (!isSquare()) {
            return false;
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if ((r == c && !internalMatrix[r][c].isOne()) ||
                        (r != c && !internalMatrix[r][c].isZero())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isRegular() {
        return !determinant().isZero();
    }

    @Override
    public boolean isZero(int row, int col) {
        return internalMatrix[row][col].isZero();
    }

    @Override
    public boolean isOne(int row, int col) {
        return internalMatrix[row][col].isOne();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(30);
        buf.append("ArrayMatrix<");
        buf.append(ring.toVisualString());
        buf.append(">[");
        buf.append(rows);
        buf.append(",");
        buf.append(columns);
        buf.append("][");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                buf.append(internalMatrix[i][j]);
                if (j < columns-1) { buf.append(" "); }
            }
            if (i < rows-1) { buf.append("; "); }
        }
        buf.append("]");
        return buf.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Matrix)) {
            return false;
        }
        Matrix<?> otherMatrix = (Matrix<?>) object;
        if (!sameSize(otherMatrix) || !ring.equals(otherMatrix.getRing())) {
            return false;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!internalMatrix[i][j].equals(otherMatrix.get(i, j))) {
                    return false;
                }
            }
        }
        return true;
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

    private void setToElementaryMatrix(R value) {
        for (int r = 0; r < rows; r++) {
            Arrays.fill(internalMatrix[r], value);
        }
    }

    //TODO make it public?
    private R determinant() {
        return null; //TODO
    }

}
