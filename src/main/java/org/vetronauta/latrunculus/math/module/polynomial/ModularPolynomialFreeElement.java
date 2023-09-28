/*
 * Copyright (C) 2007 Gérard Milmeister
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

package org.vetronauta.latrunculus.math.module.polynomial;

import org.vetronauta.latrunculus.math.module.definition.Ring;
import org.vetronauta.latrunculus.math.module.definition.FreeElement;

/**
 * The interface for elements in a free module of modular polynomials.
 * 
 * @author Gérard Milmeister
 */
public interface ModularPolynomialFreeElement extends FreeElement {

    /**
     * Returns the ring of the coefficients of the modular polynomial.
     */
    Ring getCoefficientRing();
        
    /**
     * Returns the indeterminate of the modular polynomial.
     */
    String getIndeterminate();
    
    /**
     * Returns the modulus of the modular polynomial.
     */
    PolynomialElement getModulus();
}
