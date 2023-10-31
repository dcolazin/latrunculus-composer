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
public abstract class RingMatrix<R extends RingElement<R>> extends Matrix<R> {

    //TODO make this a ModuleElement...

    protected RingMatrix(int rows, int columns) {
        super(rows, columns);
    }

    public abstract RingMatrix<R> product(RingMatrix<R> matrix);
    public abstract RingMatrix<R> sum(RingMatrix<R> matrix);
    public abstract RingMatrix<R> difference(RingMatrix<R> matrix);
    public abstract RingMatrix<R> scaled(R element);
    public abstract RingMatrix<R> inverse();

    public abstract RingMatrix<R> product(FreeElement<?,R> vector);
    public abstract R get(int i, int j);
    public abstract FreeElement<?,R> getColumn(int j);
    public abstract FreeElement<?,R> getRow(int i);

    public abstract void set(int row, int col, R element);

}
