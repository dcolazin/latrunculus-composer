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
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;

/**
 * @author vetronauta
 */
public class TempRingMatrix<R extends RingElement<R>> extends RingMatrix<R> {

    //TODO

    public TempRingMatrix(int rows, int columns) {
        super(rows, columns);
    }

    @Override
    public void setRowCount(int rows) {

    }

    @Override
    public void setColumnCount(int cols) {

    }

    @Override
    public void setToZeroMatrix() {

    }

    @Override
    public void setToZero(int row, int col) {

    }

    @Override
    public void setToOne(int row, int col) {

    }

    @Override
    public void setToUnitMatrix() {

    }

    @Override
    public int rank() {
        return 0;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isUnit() {
        return false;
    }

    @Override
    public boolean isRegular() {
        return false;
    }

    @Override
    public boolean isZero(int row, int col) {
        return false;
    }

    @Override
    public boolean isOne(int row, int col) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }

    @Override
    public RingMatrix<R> product(RingMatrix<R> matrix) {
        return null;
    }

    @Override
    public RingMatrix<R> sum(RingMatrix<R> matrix) {
        return null;
    }

    @Override
    public RingMatrix<R> difference(RingMatrix<R> matrix) {
        return null;
    }

    @Override
    public RingMatrix<R> scaled(R element) {
        return null;
    }

    @Override
    public RingMatrix<R> inverse() {
        return null;
    }

    @Override
    public RingMatrix<R> product(FreeElement<?, R> vector) {
        return null;
    }

    @Override
    public R get(int i, int j) {
        return null;
    }

    @Override
    public FreeElement<?, R> getColumn(int j) {
        return null;
    }

    @Override
    public FreeElement<?, R> getRow(int i) {
        return null;
    }

    @Override
    public void set(int row, int col, R element) {

    }
}
