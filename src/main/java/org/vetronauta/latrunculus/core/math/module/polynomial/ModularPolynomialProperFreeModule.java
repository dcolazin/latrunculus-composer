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

package org.vetronauta.latrunculus.core.math.module.polynomial;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Free modules over modular polynomials.
 * 
 * @author Gérard Milmeister
 */
public final class ModularPolynomialProperFreeModule
		extends ProperFreeModule
		implements ModularPolynomialFreeModule {

    public static ModularPolynomialFreeModule make(PolynomialElement modulus, int dimension) {
        dimension = (dimension < 0)?0:dimension;
        if (dimension == 1) {
            return ModularPolynomialRing.make(modulus);
        }
        else {
            if (modulus.getCoefficientRing().isField()) {
                return new ModularPolynomialProperFreeModule(modulus, dimension);
            }
            else {
                return null;
            }
        }
    }

    
    public ModularPolynomialFreeElement getZero() {
        ModularPolynomialElement[] res = new ModularPolynomialElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = ring.getZero();
        }
        return ModularPolynomialProperFreeElement.make(ring, res);
    }
    
    
    public ModularPolynomialFreeElement getUnitElement(int i) {
        ModularPolynomialElement[] v = new ModularPolynomialElement[getDimension()];
        for (int j = 0; j < getDimension(); j++) {
            v[j] = getRing().getZero();
        }
        v[i] = getRing().getOne();
        return ModularPolynomialProperFreeElement.make(getRing(), v);
    }
    

    public ModularPolynomialFreeModule getNullModule() {
        return make(getModulus(), 0);
    }
    
    
    public boolean isNullModule() {
        return this.getDimension() == 0;
    }

    
    public ModularPolynomialRing getComponentModule(int i) {
        return ring;
    }

    
    public ModularPolynomialRing getRing() {
        return ring;
    }


    public Ring getCoefficientRing() {
        return ring.getCoefficientRing();
    }


    public String getIndeterminate() {
        return ring.getIndeterminate();
    }


    public boolean isVectorSpace() {
        return false;
    }

    
    public PolynomialElement getModulus() {
        return ring.getModulus();
    }
    
    
    public PolynomialRing getModulusRing() {
        return ring.getModulus().getRing();
    }
    
    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof ModularPolynomialProperFreeElement &&
                element.getLength() == getDimension() &&
                ring.equals(((ModularPolynomialElement)element).getRing()));
    }


    public int compareTo(Module object) {
        if (object instanceof ModularPolynomialProperFreeModule) {
            ModularPolynomialProperFreeModule module = (ModularPolynomialProperFreeModule)object;
            int c = getRing().compareTo(module.getRing());
            if (c != 0) {
                return c;
            }
            int d = getDimension()-module.getDimension();
            if (d != 0) {
                return d;
            }
            return 0;
        }
        else {
            return super.compareTo(object);
        }
    }

    
    public ModularPolynomialProperFreeElement createElement(List<ModuleElement> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        ModularPolynomialElement[] values = new ModularPolynomialElement[getDimension()];
        Iterator<ModuleElement> iter = elements.iterator();
        for (int i = 0; i < getDimension(); i++) {
            values[i] = ring.cast(iter.next());
            if (values[i] == null) {
                return null;
            }
        }
        return (ModularPolynomialProperFreeElement)ModularPolynomialProperFreeElement.make(ring, values);
    }
    
   
    public ModularPolynomialProperFreeElement cast(ModuleElement element) {
        int dim = element.getLength();
        if (dim != getDimension()) {
            return null;
        }
        ModularPolynomialElement[] values = new ModularPolynomialElement[dim];
        for (int i = 0; i < dim; i++) {
            values[i] = ring.cast(element.getComponent(i));
            if (values[i] == null) {
                return null;
            }
        }
        return (ModularPolynomialProperFreeElement)ModularPolynomialProperFreeElement.make(ring, values);
    }

    
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ModularPolynomialProperFreeModule) {
            ModularPolynomialProperFreeModule m = (ModularPolynomialProperFreeModule)object;
            if (getDimension() != m.getDimension()) {
                return false;
            }
            return getRing().equals(m.getRing());
        }
        return false;
    }

    
    public ModularPolynomialProperFreeElement parseString(String string) {
        ArrayList<String> strings = parse(TextUtils.unparenthesize(string));
        if (strings.size() < getDimension()) {
            return null;
        }
        ModularPolynomialElement[] values = new ModularPolynomialElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            String s = strings.get(i);
            values[i] = ring.parseString(s);
            if (values[i] == null) {
                return null;
            }            
        }
        return (ModularPolynomialProperFreeElement)ModularPolynomialProperFreeElement.make(ring, values);
    }
    
    
    private static ArrayList<String> parse(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<String>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == ',' && level == 0) {
                m.add(s.substring(lastpos, pos));                
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos).trim());
        return m;
    }

    
    public String toString() {
        return "ModularPolynomialFreeModule["+getRing()+","+getDimension()+"]";
    }

    
    public String toVisualString() {
        return "("+getRing()+")^"+getDimension();
    }

    public String getElementTypeName() {
        return "ModularPolynomialFreeModule";
    }
    
    
    public int hashCode() {
        return (37*basicHash + getDimension()) ^ ring.hashCode();
    }
    
    
    protected ModuleMorphism _getProjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), getDimension(), 1);
        m.setMatrix(0, index, getRing().getOne());
        return m;
    }
    
    
    protected ModuleMorphism _getInjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), 1, getDimension());
        m.setMatrix(index, 0, getRing().getOne());
        return m;
    }

    
    private ModularPolynomialProperFreeModule(PolynomialElement modulus, int dimension) {
        super(dimension);
        this.ring = ModularPolynomialRing.make(modulus);
        
    }

    
    private ModularPolynomialRing ring;
    
    private final static int basicHash = "PolynomialFreeModule".hashCode();    
}
