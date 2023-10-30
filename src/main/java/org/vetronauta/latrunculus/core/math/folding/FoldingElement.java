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

package org.vetronauta.latrunculus.core.math.folding;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticInteger;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.DirectSumElement;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticStringElement;

/**
 * @author vetronauta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FoldingElement {

    public static double[] fold(ModuleElement element, ModuleElement[] others) {
        if (element instanceof ArithmeticElement) {
            return FoldingModule.fold(((ArithmeticElement<?>) element).getRing(), others);
        }
        if (element instanceof ArithmeticMultiElement) {
            return FoldingModule.multiFold(((ArithmeticMultiElement<?>) element).getRing(), others, element.getLength());
        }
        if (element instanceof StringElement) {
            return foldString(others);
        }
        if (element instanceof DirectSumElement) {
            return foldDirect(element, others);
        }
        if (element instanceof ProductElement || element instanceof ProductProperFreeElement) {
            return foldProduct(others);
        }
        throw new UnsupportedOperationException("Not implemented");
    }

    private static double[] foldDirect(ModuleElement element, ModuleElement[] others) {
        int eltnr = others.length;
        int eltlen = element.getLength();
        double[][] res = new double[eltnr][eltlen];
        for (int i = 0; i < eltlen; i++) {
            double[] x;
            x = foldOneDirect(others, i);
            for (int j = 0; j < eltnr; i++) {
                res[j][i] = x[j];
            }
        }
        return Folding.fold(res);
    }

    private static double[] foldOneDirect(ModuleElement[] elements, int index) {
        ModuleElement[] x = new ModuleElement[elements.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = elements[i].getComponent(index);
        }
        return FoldingElement.fold(x[0], x);
    }

    private static double[] foldProduct(ModuleElement[] others) {
        double[][] res = new double[others.length][];
        for (int i = 0; i < others.length; i++) {
            res[i] = FoldingElement.fold(others[i], new ModuleElement[] { others[i] } );
        }
        return Folding.fold(res);
    }

    private static double[] foldString(ModuleElement[] others) {
        RingString[] relements = new RingString[others.length];
        for (int i = 0; i < others.length; i++) {
            relements[i] = ((ArithmeticStringElement<ArithmeticInteger>)others[i]).getRingString();
        }
        return RingString.fold(relements);
    }

}
