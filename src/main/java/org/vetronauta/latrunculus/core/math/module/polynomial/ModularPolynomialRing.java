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

package org.vetronauta.latrunculus.core.math.module.polynomial;

import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The ring of polynomials with coefficients in a specified ring
 * modulo another polynomial.
 * @see PolynomialElement
 * 
 * @author Gérard Milmeister
 */
public final class ModularPolynomialRing<B extends RingElement<B>>
        extends Ring<ModularPolynomialElement<B>>
        implements ModularPolynomialFreeModule<ModularPolynomialElement<B>,B> {

    //TODO is this correct?
    private final Ring<B> coefficientRing;
    private final Ring<B> baseRing;
    private final PolynomialElement<B> modulus;
    private final PolynomialRing<B> modulusRing;
    private final String indeterminate;

    private ModularPolynomialRing(PolynomialElement<B> modulus) {
        this.modulus = modulus;
        this.indeterminate = modulus.getIndeterminate();
        this.coefficientRing = modulus.getCoefficientRing();
        this.baseRing = coefficientRing;
        this.modulusRing = modulus.getRing();
    }

    public static <X extends RingElement<X>> ModularPolynomialRing<X> make(PolynomialElement<X> modulus) {
        if (modulus.getCoefficientRing().isField()) {
            return new ModularPolynomialRing<>(modulus);
        }
        return null;
    }

    @Override
    public String getIndeterminate() {
        return indeterminate;
    }

    public boolean hasIndeterminate(String indet) {
        if (getIndeterminate().equals(indet)) {
            return true;
        }
        if (coefficientRing instanceof ModularPolynomialRing) {
            return ((ModularPolynomialRing<?>) coefficientRing).hasIndeterminate(indet);
        }
        if (coefficientRing instanceof PolynomialRing) {
            return ((PolynomialRing<?>) coefficientRing).hasIndeterminate(indet);
        }
        return false;
    }
    
    public List<String> getIndeterminates() {
        if (coefficientRing instanceof ModularPolynomialRing) {
            List<String> indeterminates = ((ModularPolynomialRing<?>)coefficientRing).getIndeterminates();
            indeterminates.add(0, getIndeterminate());
            return indeterminates;
        }
        else if (coefficientRing instanceof PolynomialRing) {
            List<String> indeterminates = ((PolynomialRing<?>)coefficientRing).getIndeterminates();
            indeterminates.add(0, getIndeterminate());
            return indeterminates;
        }
        else {
            List<String> indeterminates = new LinkedList<>();
            indeterminates.add(getIndeterminate());
            return indeterminates;
        }
    }

    public Ring<B> getCoefficientRing() {
        return coefficientRing;
    }
    
    public Ring<B> getBaseRing() {
        return baseRing;
    }

    public PolynomialElement<B> getModulus() {
        return modulus;
    }
    
    public PolynomialRing<B> getModulusRing() {
        return modulusRing;
    }
    
    public ModularPolynomialElement<B> getZero() {
        return new ModularPolynomialElement<>(this, getCoefficientRing().getZero());
    }

    public ModularPolynomialElement<B> getOne() {
        return new ModularPolynomialElement<>(this, getCoefficientRing().getOne());
    }

    public ModularPolynomialFreeModule getNullModule() {
        return ModularPolynomialProperFreeModule.make(getModulus(), 0);
    }

    public boolean isField() {
        return false;
    }
    
    public boolean isVectorSpace() {
        return false;
    }

    public boolean hasElement(ModuleElement element) {
        if (element instanceof ModularPolynomialElement) {
            return element.getModule().equals(this);
        }
        return false;
    }

    
    public ModularPolynomialFreeModule getFreeModule(int dimension) {
        return ModularPolynomialProperFreeModule.make(getModulus(), dimension);
    }

    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ModularPolynomialRing) {
            ModularPolynomialRing r = (ModularPolynomialRing)object;
            return getModulus().equals(r.getModulus());
        }
        else {
            return false;
        }
    }

    
    public int compareTo(Module object) {
        if (object instanceof ModularPolynomialRing) {
            ModularPolynomialRing p = (ModularPolynomialRing)object;
            int c;
            if ((c = getCoefficientRing().compareTo(p.getCoefficientRing())) != 0) {
                return c;
            }
            else {
                return getIndeterminate().compareTo(p.getIndeterminate());
            }
        }
        else {
            return super.compareTo(object);
        }
    }


    public ModularPolynomialElement createElement(List<ModuleElement<?,?>> elements) {
        if (elements.size() == 1) {
            if (hasElement(elements.get(0))) {
                return (ModularPolynomialElement)elements.get(0);
            }
        }
        RingElement[] coeffs = new RingElement[elements.size()];
        int i = 0;
        for (ModuleElement e : elements) {
            coeffs[i] = (RingElement)getCoefficientRing().cast(e);
            if (coeffs[i] == null) {
                return null;
            }
        }
        return new ModularPolynomialElement(this, coeffs);
    }

    
    public ModularPolynomialElement cast(ModuleElement element) {
        if (this.equals(element.getModule())) {
            return (ModularPolynomialElement)element;
        }
        else if (element instanceof PolynomialElement) {
            PolynomialElement p = (PolynomialElement)element;
            RingElement[] coeffs = p.getCoefficients();
            RingElement[] newCoeffs = new RingElement[coeffs.length];
            Ring ring = getCoefficientRing();
            for (int i = 0; i < coeffs.length; i++) {
                newCoeffs[i] = (RingElement)ring.cast(coeffs[i]);
                if (newCoeffs[i] == null) {
                    return null;
                }                
            }
            return new ModularPolynomialElement(this, newCoeffs);
        }
        else if (element instanceof RingElement) {
            RingElement newCoeff = (RingElement)getCoefficientRing().cast(element);
            if (newCoeff == null) {
                return null;
            }
            else {
                return new ModularPolynomialElement(this, new RingElement[] { newCoeff });
            }
        }
        else {
            return null;
        }
    }

    
    public String toString() {
        StringBuilder buf = new StringBuilder(40);
        buf.append("ModularPolynomialRing(");
        buf.append(getBaseRing());
        buf.append(")[");
        buf.append(modulus.stringRep());
        buf.append("]");
        return buf.toString();
    }

    
    public String toVisualString() {
        Ring baseRing = getBaseRing();
        String s = "";
        if (baseRing instanceof ProductRing) {
            s += "(";
            s += baseRing.toVisualString();
            s += ")";
        }
        else {
            s += baseRing.toVisualString();
        }
        s += "[";
        Iterator<String> iter = getIndeterminates().iterator();
        String in = iter.next();
        s += in;
        while (iter.hasNext()) {
            s += ","+iter.next();
        }
        s += "]/(";
        s += getModulus().stringRep();
        s += ")";
        return s;
    }
    
    
    public ModularPolynomialElement parseString(String string) {
        PolynomialElement p = modulusRing.parseString(string);
        if (p != null) {
            try {
                return new ModularPolynomialElement(this, p);
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }
    
    public String getElementTypeName() {
        return "ModularPolynomialRing";
    }

    
    public int hashCode() {
        int hashCode = basicHash;
        hashCode ^= modulus.hashCode();
        return hashCode;
    }

    
    private static final int basicHash = "ModularPolynomialRing".hashCode();

}
