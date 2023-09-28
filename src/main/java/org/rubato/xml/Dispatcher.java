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

package org.rubato.xml;

import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.vetronauta.latrunculus.math.module.complex.CElement;
import org.vetronauta.latrunculus.math.module.complex.CProperFreeElement;
import org.vetronauta.latrunculus.math.module.complex.CProperFreeModule;
import org.vetronauta.latrunculus.math.module.complex.CRing;
import org.vetronauta.latrunculus.math.module.polynomial.ModularPolynomialElement;
import org.vetronauta.latrunculus.math.module.polynomial.ModularPolynomialProperFreeElement;
import org.vetronauta.latrunculus.math.module.polynomial.ModularPolynomialProperFreeModule;
import org.vetronauta.latrunculus.math.module.polynomial.ModularPolynomialRing;
import org.vetronauta.latrunculus.math.module.definition.Module;
import org.rubato.math.yoneda.ConstantModuleMorphismMap;
import org.rubato.math.yoneda.ModuleMorphismMap;
import org.rubato.math.yoneda.MorphismMap;
import org.vetronauta.latrunculus.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.math.module.polynomial.PolynomialProperFreeElement;
import org.vetronauta.latrunculus.math.module.polynomial.PolynomialProperFreeModule;
import org.vetronauta.latrunculus.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.math.module.rational.QElement;
import org.vetronauta.latrunculus.math.module.rational.QProperFreeElement;
import org.vetronauta.latrunculus.math.module.rational.QProperFreeModule;
import org.vetronauta.latrunculus.math.module.rational.QRing;
import org.vetronauta.latrunculus.math.module.rational.QStringElement;
import org.vetronauta.latrunculus.math.module.rational.QStringProperFreeElement;
import org.vetronauta.latrunculus.math.module.rational.QStringProperFreeModule;
import org.vetronauta.latrunculus.math.module.rational.QStringRing;
import org.vetronauta.latrunculus.math.module.real.RElement;
import org.vetronauta.latrunculus.math.module.real.RProperFreeElement;
import org.vetronauta.latrunculus.math.module.real.RProperFreeModule;
import org.vetronauta.latrunculus.math.module.real.RRing;
import org.vetronauta.latrunculus.math.module.real.RStringElement;
import org.vetronauta.latrunculus.math.module.real.RStringProperFreeElement;
import org.vetronauta.latrunculus.math.module.real.RStringProperFreeModule;
import org.vetronauta.latrunculus.math.module.real.RStringRing;
import org.vetronauta.latrunculus.math.module.definition.RestrictedElement;
import org.vetronauta.latrunculus.math.module.definition.RestrictedModule;
import org.vetronauta.latrunculus.math.module.integer.ZElement;
import org.vetronauta.latrunculus.math.module.integer.ZProperFreeElement;
import org.vetronauta.latrunculus.math.module.integer.ZProperFreeModule;
import org.vetronauta.latrunculus.math.module.integer.ZRing;
import org.vetronauta.latrunculus.math.module.integer.ZStringElement;
import org.vetronauta.latrunculus.math.module.integer.ZStringProperFreeElement;
import org.vetronauta.latrunculus.math.module.integer.ZStringProperFreeModule;
import org.vetronauta.latrunculus.math.module.integer.ZStringRing;
import org.vetronauta.latrunculus.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.math.module.modular.ZnProperFreeElement;
import org.vetronauta.latrunculus.math.module.modular.ZnProperFreeModule;
import org.vetronauta.latrunculus.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.math.module.modular.ZnStringElement;
import org.vetronauta.latrunculus.math.module.modular.ZnStringProperFreeElement;
import org.vetronauta.latrunculus.math.module.modular.ZnStringProperFreeModule;
import org.vetronauta.latrunculus.math.module.modular.ZnStringRing;
import org.vetronauta.latrunculus.math.module.morphism.CAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.CFreeAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.CanonicalMorphism;
import org.vetronauta.latrunculus.math.module.morphism.CastMorphism;
import org.vetronauta.latrunculus.math.module.morphism.CompositionMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ConjugationMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ConstantMorphism;
import org.vetronauta.latrunculus.math.module.morphism.DifferenceMorphism;
import org.vetronauta.latrunculus.math.module.morphism.EmbeddingMorphism;
import org.vetronauta.latrunculus.math.module.morphism.FoldingMorphism;
import org.vetronauta.latrunculus.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.GenericBasisMorphism;
import org.vetronauta.latrunculus.math.module.morphism.IdentityMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ModuloMorphism;
import org.vetronauta.latrunculus.math.module.morphism.PolynomialMorphism;
import org.vetronauta.latrunculus.math.module.morphism.PowerMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ProductMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ProjectionMorphism;
import org.vetronauta.latrunculus.math.module.morphism.QAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.QFreeAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.RAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.RFreeAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ReorderMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ScaledMorphism;
import org.vetronauta.latrunculus.math.module.morphism.SplitMorphism;
import org.vetronauta.latrunculus.math.module.morphism.SumMorphism;
import org.vetronauta.latrunculus.math.module.morphism.TranslationMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ZAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ZFreeAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ZnAffineMorphism;
import org.vetronauta.latrunculus.math.module.morphism.ZnFreeAffineMorphism;
import org.w3c.dom.Element;

public class Dispatcher {

    public static Dispatcher getDispatcher() {
        return dispatcher;
    }

    
    public Module resolveModule(XMLReader reader, Element moduleElement) {
        Module module = null;
        String moduleName = moduleElement.getAttribute(TYPE_ATTR);
        XMLInputOutput<Module> dispatch = modules.get(moduleName);

        if (dispatch != null) {
            module = dispatch.fromXML(reader, moduleElement);
        }

        if (module == null) {
            try {
                Class<?> c = Class.forName("org.rubato.math.module."+moduleName); //$NON-NLS-1$
                Method m = c.getMethod("fromXML", new Class[] { XMLReader.class, Element.class }); //$NON-NLS-1$
                module = (Module)m.invoke(c, new Object[] { reader, moduleElement });
            }
            catch (Exception e) {
                reader.setError("Cannot build module from %%1.", moduleName);
            }
        }

        return module;
    }

    
    public ModuleMorphism resolveModuleMorphism(XMLReader reader, Element morphismElement) {
        ModuleMorphism morphism = null;
        String morphismName = morphismElement.getAttribute(TYPE_ATTR);
        XMLInputOutput<ModuleMorphism> dispatch = moduleMorphisms.get(morphismName);
        
        if (dispatch != null) {
            morphism = dispatch.fromXML(reader, morphismElement);
        }
        
        if (morphism == null) {
            try {
                Class<?> c = Class.forName("org.rubato.math.module."+morphismName); //$NON-NLS-1$
                Method m = c.getMethod("fromXML", new Class[] { XMLReader.class, Element.class }); //$NON-NLS-1$
                morphism = (ModuleMorphism)m.invoke(c, new Object[] { reader, morphismElement });
            }
            catch (Exception e) {
                reader.setError("Cannot build module morphism from %%1.", morphismName);
            }
        }

        return morphism;
    }

    
    public ModuleElement resolveElement(XMLReader reader, Element element) {
        ModuleElement moduleElement = null;
        String elementName = element.getAttribute(TYPE_ATTR);
        XMLInputOutput<ModuleElement> dispatch = elements.get(elementName);

        if (dispatch != null) {
            moduleElement = dispatch.fromXML(reader, element);
        }

        if (moduleElement == null) {
            try {
                Class<?> c = Class.forName("org.rubato.math.module."+elementName); //$NON-NLS-1$
                Method m = c.getMethod("fromXML", new Class[] { XMLReader.class, Element.class }); //$NON-NLS-1$
                moduleElement = (ModuleElement)m.invoke(c, new Object[] { reader, element });
            }
            catch (Exception e) {
                reader.setError("Cannot build module from %%1.", elementName);
            }
        }

        return moduleElement;
    }

    
    public MorphismMap resolveMorphismMap(XMLReader reader, Element morphismMapElement) {
        MorphismMap morphismMap = null;
        String morphismMapName = morphismMapElement.getAttribute(TYPE_ATTR);
        XMLInputOutput<MorphismMap> dispatch = morphismMaps.get(morphismMapName);

        if (dispatch != null) {
            morphismMap = dispatch.fromXML(reader, morphismMapElement);
        }

        if (morphismMap == null) {
            try {
                Class<?> c = Class.forName(morphismMapName);
                Method m = c.getMethod("fromXML", new Class[] { XMLReader.class, Element.class }); //$NON-NLS-1$
                morphismMap = (MorphismMap)m.invoke(c, new Object[] { reader, morphismMapElement });
            }
            catch (Exception e) {
                reader.setError("Cannot build morphism map from %%1.", morphismMapName);
            }
        }

        return morphismMap;
    }

    
    public void addModule(XMLInputOutput<Module> dispatch) {
        modules.put(dispatch.getElementTypeName(), dispatch);
    }
    
    
    public void addModuleMorphism(XMLInputOutput<ModuleMorphism> dispatch) {
        moduleMorphisms.put(dispatch.getElementTypeName(), dispatch);
    }
    
    
    public void addMorphismMap(XMLInputOutput<MorphismMap> dispatch) {
        morphismMaps.put(dispatch.getElementTypeName(), dispatch);
    }
    
    
    public void addModuleElement(XMLInputOutput<ModuleElement> dispatch) {
        elements.put(dispatch.getElementTypeName(), dispatch);
    }    
    

    /**
     * Every type of ModuleMorphism must be registered here
     * so that an XML representation can be read.
     */
    private void init() {
        // modules
        addModule(ZRing.getXMLInputOutput());
        addModule(ZnRing.getXMLInputOutput());
        addModule(RRing.getXMLInputOuput());
        addModule(QRing.getXMLInputOutput());
        addModule(CRing.getXMLInputOutput());
        
        addModule(ZStringRing.getXMLInputOutput());
        addModule(ZnStringRing.getXMLInputOutput());
        addModule(RStringRing.getXMLInputOutput());
        addModule(QStringRing.getXMLInputOutput());
        
        addModule(ZProperFreeModule.getXMLInputOutput());
        addModule(ZnProperFreeModule.getXMLInputOutput());
        addModule(RProperFreeModule.getXMLInputOutput());
        addModule(QProperFreeModule.getXMLInputOutput());
        addModule(CProperFreeModule.getXMLInputOutput());
        
        addModule(ZStringProperFreeModule.getXMLInputOutput());
        addModule(ZnStringProperFreeModule.getXMLInputOutput());
        addModule(RStringProperFreeModule.getXMLInputOutput());
        addModule(QStringProperFreeModule.getXMLInputOutput());

        addModule(PolynomialRing.getXMLInputOutput());
        addModule(PolynomialProperFreeModule.getXMLInputOutput());
        addModule(ModularPolynomialRing.getXMLInputOutput());
        addModule(ModularPolynomialProperFreeModule.getXMLInputOutput());
        
        addModule(ProductRing.getXMLInputOutput());
        
        addModule(RestrictedModule.getXMLInputOutput());
        
        // module elements
        addModuleElement(ZElement.getXMLInputOutput());
        addModuleElement(ZnElement.getXMLInputOutput());
        addModuleElement(RElement.getXMLInputOutput());
        addModuleElement(QElement.getXMLInputOutput());
        addModuleElement(CElement.getXMLInputOutput());
        
        addModuleElement(ZStringElement.getXMLInputOutput());
        addModuleElement(ZnStringElement.getXMLInputOutput());
        addModuleElement(RStringElement.getXMLInputOutput());
        addModuleElement(QStringElement.getXMLInputOutput());

        addModuleElement(ZProperFreeElement.getXMLInputOutput());
        addModuleElement(ZnProperFreeElement.getXMLInputOutput());
        addModuleElement(RProperFreeElement.getXMLInputOutput());
        addModuleElement(QProperFreeElement.getXMLInputOutput());
        addModuleElement(CProperFreeElement.getXMLInputOutput());
        
        addModuleElement(ZStringProperFreeElement.getXMLInputOutput());
        addModuleElement(ZnStringProperFreeElement.getXMLInputOutput());
        addModuleElement(RStringProperFreeElement.getXMLInputOutput());
        addModuleElement(QStringProperFreeElement.getXMLInputOutput());

        addModuleElement(PolynomialElement.getXMLInputOutput());
        addModuleElement(PolynomialProperFreeElement.getXMLInputOutput());
        addModuleElement(ModularPolynomialElement.getXMLInputOutput());
        addModuleElement(ModularPolynomialProperFreeElement.getXMLInputOutput());

        addModuleElement(RestrictedElement.getXMLInputOutput());
        
        // module morphisms
        addModuleMorphism(ZnAffineMorphism.getXMLInputOutput());
        addModuleMorphism(ZAffineMorphism.getXMLInputOutput());
        addModuleMorphism(QAffineMorphism.getXMLInputOutput());
        addModuleMorphism(RAffineMorphism.getXMLInputOutput());
        addModuleMorphism(CAffineMorphism.getXMLInputOutput());
        
        addModuleMorphism(ZnFreeAffineMorphism.getXMLInputOutput());
        addModuleMorphism(ZFreeAffineMorphism.getXMLInputOutput());
        addModuleMorphism(QFreeAffineMorphism.getXMLInputOutput());
        addModuleMorphism(RFreeAffineMorphism.getXMLInputOutput());
        addModuleMorphism(CFreeAffineMorphism.getXMLInputOutput());
        
        addModuleMorphism(CompositionMorphism.getXMLInputOutput());
        addModuleMorphism(ConstantMorphism.getXMLInputOutput());
        addModuleMorphism(DifferenceMorphism.getXMLInputOutput());
        addModuleMorphism(SumMorphism.getXMLInputOutput());
        addModuleMorphism(ProductMorphism.getXMLInputOutput());
        addModuleMorphism(FoldingMorphism.getXMLInputOutput());
        addModuleMorphism(IdentityMorphism.getXMLInputOutput());
        addModuleMorphism(PolynomialMorphism.getXMLInputOutput());
        addModuleMorphism(PowerMorphism.getXMLInputOutput());
        addModuleMorphism(ScaledMorphism.getXMLInputOutput());
        addModuleMorphism(TranslationMorphism.getXMLInputOutput());
        addModuleMorphism(ModuloMorphism.getXMLInputOutput());
        addModuleMorphism(EmbeddingMorphism.getXMLInputOutput());
        addModuleMorphism(ProjectionMorphism.getXMLInputOutput());
        addModuleMorphism(ConjugationMorphism.getXMLInputOutput());
        addModuleMorphism(GenericBasisMorphism.getXMLInputOutput());
        addModuleMorphism(ReorderMorphism.getXMLInputOutput());
        addModuleMorphism(CanonicalMorphism.getXMLInputOutput());
        addModuleMorphism(GenericAffineMorphism.getXMLInputOutput());
        addModuleMorphism(SplitMorphism.getXMLInputOutput());
        addModuleMorphism(CastMorphism.getXMLInputOutput());

        addMorphismMap(ModuleMorphismMap.getXMLInputOutput());
        addMorphismMap(ConstantModuleMorphismMap.getXMLInputOutput());
    }
    
        
    private Dispatcher() {
        init();
    }

    
    private HashMap<String,XMLInputOutput<Module>> modules = new HashMap<String,XMLInputOutput<Module>>();
    private HashMap<String,XMLInputOutput<ModuleMorphism>> moduleMorphisms = new HashMap<String,XMLInputOutput<ModuleMorphism>>();
    private HashMap<String,XMLInputOutput<ModuleElement>> elements = new HashMap<String,XMLInputOutput<ModuleElement>>();
    private HashMap<String,XMLInputOutput<MorphismMap>> morphismMaps = new HashMap<String,XMLInputOutput<MorphismMap>>();
    
    private final static Dispatcher dispatcher = new Dispatcher();    
}
