/*
 * Copyright (C) 2005 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.math.module.morphism;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_MORPHISM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

import java.util.LinkedList;
import java.util.List;

import org.vetronauta.latrunculus.core.math.matrix.CMatrix;
import org.vetronauta.latrunculus.core.math.matrix.QMatrix;
import org.vetronauta.latrunculus.core.math.matrix.RMatrix;
import org.vetronauta.latrunculus.core.math.matrix.ZMatrix;
import org.vetronauta.latrunculus.core.math.matrix.ZnMatrix;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.arith.number.Complex;
import org.vetronauta.latrunculus.core.math.arith.number.Rational;
import org.rubato.math.matrix.*;
import org.vetronauta.latrunculus.core.math.module.complex.CFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.server.xml.XMLInputOutput;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.core.math.module.rational.QFreeModule;
import org.vetronauta.latrunculus.core.math.module.real.RFreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.integer.ZFreeModule;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnFreeModule;
import org.w3c.dom.Element;

/**
 * A split morphism describes a morphism from a free module into itself,
 * where the module is split into several parts, for example a <i>R^9</i> into
 * <i>R^3</i>, <i>R^2</i> and <i>R^4</i>. For each part a morphism is given, in the example
 * three morphisms <i>f1:R^3->R^3</i>, <i>f2:R^2->R^2</i> and <i>f3:R^4->R^4</i>.
 * 
 * @author Gérard Milmeister
 */
public class SplitMorphism extends ModuleMorphism {

    /**
     * Creates a split morphism over <code>module</code> with
     * the given list of morphisms.
     */
    public static ModuleMorphism make(FreeModule module, List<ModuleMorphism> morphisms) {
        boolean info[] = new boolean[3];
        if (checkMorphisms(module, morphisms, info)) {
            if (morphisms.size() == 1) {
                // there is only one morphism, no need for split
                return morphisms.get(0);
            }
            else if (info[ALL_IDENTITY]) {
                return getIdentityMorphism(module);
            }
            else if (info[ALL_CONSTANT]) {
                LinkedList<ModuleElement> resList = new LinkedList<ModuleElement>();
                for (ModuleMorphism m : morphisms) {
                    Module domain = m.getDomain();
                    int dim = domain.getDimension();
                    try {
                        ModuleElement res = m.map(domain.getZero());
                        for (int k = 0; k < dim; k++) {
                            resList.add(res.getComponent(k));
                        }
                    }
                    catch (MappingException e) {
                        return null;
                    }
                }
                return getConstantMorphism(module, module.createElement(resList));
            }
            else if (module instanceof ZFreeModule) {
                for (ModuleMorphism m : morphisms) {
                    if (!(m instanceof ZFreeAffineMorphism) &&
                        !(m instanceof ZAffineMorphism)) {
                        return new SplitMorphism(module, morphisms);
                    }
                }
                return makeZFreeMorphism(((ZFreeModule)module).getDimension(), morphisms);
            }
            else if (module instanceof ZnFreeModule) {
                for (ModuleMorphism m : morphisms) {
                    if (!(m instanceof ZnFreeAffineMorphism) &&
                        !(m instanceof ZnAffineMorphism)) {
                        return new SplitMorphism(module, morphisms);
                    }
                }
                return makeZnFreeMorphism(((ZnFreeModule)module).getDimension(),
                                          ((ZnFreeModule)module).getModulus(), morphisms);
            }
            else if (module instanceof QFreeModule) {
                for (ModuleMorphism m : morphisms) {
                    if (!(m instanceof QFreeAffineMorphism) &&
                        !(m instanceof QAffineMorphism)) {
                        return new SplitMorphism(module, morphisms);
                    }
                }
                return makeQFreeMorphism(((QFreeModule)module).getDimension(), morphisms);
            }
            else if (module instanceof RFreeModule) {
                for (ModuleMorphism m : morphisms) {
                    if (!(m instanceof RFreeAffineMorphism) &&
                        !(m instanceof RAffineMorphism)) {
                        return new SplitMorphism(module, morphisms);
                    }
                }
                return makeRFreeMorphism(((RFreeModule)module).getDimension(), morphisms);
            }
            else if (module instanceof CFreeModule) {
                for (ModuleMorphism m : morphisms) {
                    if (!(m instanceof CFreeAffineMorphism) &&
                        !(m instanceof CAffineMorphism)) {
                        return new SplitMorphism(module, morphisms);
                    }
                }
                return makeCFreeMorphism(((CFreeModule)module).getDimension(), morphisms);
            }
            else {
                return new SplitMorphism(module, morphisms);
            }
        }
        else {
            return null;
        }
    }
    
    
    public ModuleElement map(ModuleElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            LinkedList<ModuleElement> resList = new LinkedList<ModuleElement>();
            int j = 0;
            for (int i = 0; i < morphisms.length; i++) {
                ModuleMorphism m = morphisms[i];
                Module domain = m.getDomain();
                int dim = domain.getDimension();
                LinkedList<ModuleElement> list = new LinkedList<ModuleElement>();
                for (int k = 0; k < dim; k++) {
                    list.add(x.getComponent(j));
                    j++;
                }
                ModuleElement res = m.map(domain.createElement(list));
                for (int k = 0; k < dim; k++) {
                    resList.add(res.getComponent(k));
                }
            }
            return getDomain().createElement(resList);
        }
        else {
            throw new MappingException("SplitMorphism.map: ", x, this);
        }
    }

    
    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }

    
    public boolean isModuleHomomorphism() {
        for (ModuleMorphism m : morphisms) {
            if (!m.isModuleHomomorphism()) {
                return false;
            }
        }
        return true;        
    }

    
    /**
     * Returns the morphisms that make up this split morphism
     * in the correct order.
     */
    public ModuleMorphism[] getMorphisms() {
        return morphisms;
    }
    
    
    public boolean equals(Object object) {
        if (this == object) {
            return true;

        }    
        else if (object instanceof SplitMorphism) {
            SplitMorphism m = (SplitMorphism)object;
            if (getDomain().equals(m.getDomain()) &&
                morphisms.length == m.morphisms.length) {
                for (int i = 0; i < morphisms.length; i++) {
                    if (!morphisms[i].equals(m.morphisms[i])) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }


    public String toString() {
        StringBuilder buf = new StringBuilder(50);
        buf.append("SplitMorphism[");
        buf.append(getDomain());
        buf.append(",");
        buf.append(morphisms[0]);
        for (int i = 1; i < morphisms.length; i++) {
            buf.append(",");
            buf.append(morphisms[i]);
        }
        buf.append("]");
        return buf.toString();
    }

    
    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(MODULE_MORPHISM, getElementTypeName());
        getDomain().toXML(writer);
        for (ModuleMorphism m : morphisms) {
            m.toXML(writer);
        }
        writer.closeBlock();
    }

    
    public ModuleMorphism fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            Module domain = reader.parseModule(childElement);
            if (domain == null) {
                return null;
            }
            if (!(domain instanceof FreeModule)) {
                reader.setError("Module in type %%1 must be a free module.", getElementTypeName());
                return null;
            }
            LinkedList<ModuleMorphism> morphismList = new LinkedList<ModuleMorphism>();
            childElement = XMLReader.getNextSibling(childElement, MODULE_MORPHISM);
            if (childElement == null) {
                reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(), MODULE_MORPHISM);
                return null;
            }
            while (childElement != null) {
                ModuleMorphism m = reader.parseModuleMorphism(childElement);
                if (m == null) {
                    return null;
                }
                morphismList.add(m);
                childElement = XMLReader.getNextSibling(childElement, MODULE_MORPHISM);
            }
            ModuleMorphism res = SplitMorphism.make((FreeModule)domain, morphismList);
            if (res == null) {
                reader.setError("Cannot create a split morphism.");
                return null;
            }
            else {
                return res;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(), MODULE);
            return null;
        }
    }
    
    
    private final static XMLInputOutput<ModuleMorphism> xmlIO =
        new SplitMorphism(ZRing.ring, new LinkedList<ModuleMorphism>());
    
    public static XMLInputOutput<ModuleMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "SplitMorphism";
    }
    
    
    private static boolean checkMorphisms(FreeModule module, List<ModuleMorphism> morphisms, boolean[] info) {
        info[ALL_CONSTANT] = true;
        info[ALL_IDENTITY] = true;
        Ring ring = module.getRing();
        int dim = 0;
        for (ModuleMorphism m : morphisms) {
            Module domain = m.getDomain();
            if (!domain.equals(m.getCodomain())) {
                return false;
            }
            if (!domain.getRing().equals(ring)) {
                return false;
            }
            if (!m.isConstant()) { info[ALL_CONSTANT] = false; }
            if (!m.isIdentity()) { info[ALL_IDENTITY] = false; }
            dim += domain.getDimension();
        }
        if (dim != module.getDimension()){
            return false;
        }
        return true;
    }
    
    
    private static ModuleMorphism makeZFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        ZMatrix A = new ZMatrix(dim, dim);
        int[] b = new int[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof ZFreeAffineMorphism) {
                ZMatrix A1 = ((ZFreeAffineMorphism)m).getMatrix();
                int[] b1 = ((ZFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof ZAffineMorphism) {
                A.set(i, i, ((ZAffineMorphism)m).getA());
                b[i] = ((ZAffineMorphism)m).getB();
            }
        }
        return ZFreeAffineMorphism.make(A, b);
    }
    
    
    private static ModuleMorphism makeZnFreeMorphism(int dim, int modulus, List<ModuleMorphism> morphisms) {
        ZnMatrix A = new ZnMatrix(dim, dim, modulus);
        int[] b = new int[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof ZnFreeAffineMorphism) {
                ZnMatrix A1 = ((ZnFreeAffineMorphism)m).getMatrix();
                int[] b1 = ((ZnFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof ZnAffineMorphism) {
                A.set(i, i, ((ZnAffineMorphism)m).getA());
                b[i] = ((ZnAffineMorphism)m).getB();
            }
        }
        return ZnFreeAffineMorphism.make(A, b);
    }
    
    
    private static ModuleMorphism makeQFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        QMatrix A = new QMatrix(dim, dim);
        Rational[] b = new Rational[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof QFreeAffineMorphism) {
                QMatrix A1 = ((QFreeAffineMorphism)m).getMatrix();
                Rational[] b1 = ((QFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof QAffineMorphism) {
                A.set(i, i, ((QAffineMorphism)m).getA());
                b[i] = ((QAffineMorphism)m).getB();
            }
        }
        return QFreeAffineMorphism.make(A, b);
    }
    
    
    private static ModuleMorphism makeRFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        RMatrix A = new RMatrix(dim, dim);
        double[] b = new double[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof RFreeAffineMorphism) {
                RMatrix A1 = ((RFreeAffineMorphism)m).getMatrix();
                double[] b1 = ((RFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof RAffineMorphism) {
                A.set(i, i, ((RAffineMorphism)m).getA());
                b[i] = ((RAffineMorphism)m).getB();
            }
        }
        return RFreeAffineMorphism.make(A, b);
    }
    
    
    private static ModuleMorphism makeCFreeMorphism(int dim, List<ModuleMorphism> morphisms) {
        CMatrix A = new CMatrix(dim, dim);
        Complex[] b = new Complex[dim];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            if (m instanceof CFreeAffineMorphism) {
                CMatrix A1 = ((CFreeAffineMorphism)m).getMatrix();
                Complex[] b1 = ((CFreeAffineMorphism)m).getVector();
                int d = b1.length;
                for (int j = 0; j < d; j++) {
                    for (int k = 0; k < d; k++) {
                        A.set(i+j, i+k, A1.get(j, k));
                    }
                    b[i+j] = b1[j];
                }
                i += d;
            }
            else if (m instanceof CAffineMorphism) {
                A.set(i, i, ((CAffineMorphism)m).getA());
                b[i] = ((CAffineMorphism)m).getB();
            }
        }
        return CFreeAffineMorphism.make(A, b);
    }
    
    
    private SplitMorphism(FreeModule module, List<ModuleMorphism> morphisms) {
        super(module, module);
        this.morphisms = new ModuleMorphism[morphisms.size()];
        int i = 0;
        for (ModuleMorphism m : morphisms) {
            this.morphisms[i++] = m;
        }
    }
    

    private ModuleMorphism[] morphisms;
    
    
    private final static int ALL_CONSTANT = 0;
    private final static int ALL_IDENTITY = 1;
}
