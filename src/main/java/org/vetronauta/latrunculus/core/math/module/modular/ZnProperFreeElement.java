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

package org.vetronauta.latrunculus.core.math.module.modular;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.arith.Folding;
import org.vetronauta.latrunculus.core.math.arith.number.ArithmeticModulus;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticMultiElement;
import org.vetronauta.latrunculus.core.math.module.generic.ArithmeticRingRepository;

import java.util.List;

/**
 * Elements in a free module over integers mod <i>n</i>.
 *
 * @author Gérard Milmeister
 */
public class ZnProperFreeElement extends ArithmeticMultiElement<ArithmeticModulus> {

    public int getModulus() {
        return modulus;
    }
    
    public int compareTo(ModuleElement object) {
        if (object instanceof ZnProperFreeElement) {
            ZnProperFreeElement element = (ZnProperFreeElement)object;
            int m = getModulus()-element.getModulus();
            if (m < 0) {
                return -1;
            }
            else if (m > 0) {
                return 1;
            }
            else {
	            int l = getLength()-element.getLength();
                if (l != 0) {
                    return l;
                }
	            else {
	                for (int i = 0; i < getLength(); i++) {
	                    int d = getValue().get(i).getValue().intValue()-element.getValue().get(i).getValue().intValue();
                        if (d != 0) {
                            return d;
                        }
	                }
	                return 0;
	            }
            }
        }
        else {
            return super.compareTo(object);
        }
    }

    public String stringRep(boolean ... parens) {
        if (getLength() == 0) {
            return "Null";
        }
        else {
            StringBuilder res = new StringBuilder(30);
            res.append(getValue().get(0));
            for (int i = 1; i < getLength(); i++) {
                res.append(',');
                res.append(getValue().get(i));
            }
            if (parens.length > 0) {
                return TextUtils.parenthesize(res.toString());
            }
            else {
                return res.toString();
            }
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(30);        
        buf.append("ZnFreeElement(");
        buf.append(getModulus());
        buf.append(")[");
        buf.append(getLength());
        buf.append("][");
        if (getLength() > 0) {
            buf.append(getValue().get(0));
            for (int i = 1; i < getLength(); i++) {
                buf.append(",");
                buf.append(getValue().get(i));
            }
        }
        buf.append("]");
        return buf.toString();
    }

    public String getElementTypeName() {
        return "ZnFreeElement";
    }

    private ZnProperFreeElement(List<ArithmeticElement<ArithmeticModulus>> value, int modulus) {
        super(ArithmeticRingRepository.getModulusRing(modulus), value);
        this.modulus = modulus;
    }
    
    private int          modulus;

}
