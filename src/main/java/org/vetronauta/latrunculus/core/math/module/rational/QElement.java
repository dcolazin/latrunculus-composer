/*
 * Copyright (C) 2001, 2005 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.vetronauta.latrunculus.core.math.module.rational;

import lombok.NonNull;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;

/**
 * Elements in the field of rationals.
 * @see QRing
 * 
 * @author Gérard Milmeister
 */
public final class QElement extends ArithmeticElement<QElement, Rational> {

    /**
     * Constructs a QElement with rational number <code>value</code>.
     */
    public QElement(Rational value) {
        super(value);
    }

    @Override
    protected QElement valueOf(@NonNull Rational value) {
        return new QElement(value);
    }


    /**
     * Constructs a QElement with rational number <code>num/denom</code>.
     */
    public QElement(int num, int denom) {
        super(new Rational(num, denom));
    }

    
    /**
     * Constructs a QElement with integer <code>i</code>.
     */
    public QElement(int i) {
        super(new Rational(i));
    }
    
    public QRing getRing() {
        return QRing.ring;
    }

    public double[] fold(ModuleElement[] elements) {
        double[] res = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            QElement r = (QElement)elements[i];
            res[i] = r.getValue().doubleValue();
        }
        return res;
    }

}
