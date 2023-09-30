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

package org.vetronauta.latrunculus.server.xml;

import org.vetronauta.latrunculus.core.math.yoneda.ConstantModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.ModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.MorphismMap;
import org.vetronauta.latrunculus.core.math.module.complex.CElement;
import org.vetronauta.latrunculus.core.math.module.complex.CProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.complex.CProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.complex.CRing;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.ProductRing;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedElement;
import org.vetronauta.latrunculus.core.math.module.definition.RestrictedModule;
import org.vetronauta.latrunculus.core.math.module.integer.ZElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.integer.ZRing;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.integer.ZStringRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.modular.ZnRing;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.modular.ZnStringRing;
import org.vetronauta.latrunculus.core.math.module.morphism.CAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CanonicalMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CastMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.CompositionMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ConjugationMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ConstantMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.DifferenceMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.EmbeddingMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.FoldingMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.GenericBasisMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.IdentityMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuloMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.PolynomialMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.PowerMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ProductMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ProjectionMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.QAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.QFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.RAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.RFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ReorderMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ScaledMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.SplitMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.SumMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.TranslationMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZnAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.morphism.ZnFreeAffineMorphism;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.polynomial.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.polynomial.PolynomialRing;
import org.vetronauta.latrunculus.core.math.module.rational.QElement;
import org.vetronauta.latrunculus.core.math.module.rational.QProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.rational.QProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.rational.QRing;
import org.vetronauta.latrunculus.core.math.module.rational.QStringElement;
import org.vetronauta.latrunculus.core.math.module.rational.QStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.rational.QStringProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.rational.QStringRing;
import org.vetronauta.latrunculus.core.math.module.real.RElement;
import org.vetronauta.latrunculus.core.math.module.real.RProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.real.RProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.real.RRing;
import org.vetronauta.latrunculus.core.math.module.real.RStringElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringProperFreeElement;
import org.vetronauta.latrunculus.core.math.module.real.RStringProperFreeModule;
import org.vetronauta.latrunculus.core.math.module.real.RStringRing;
import org.vetronauta.latrunculus.server.xml.reader.LatrunculusXmlReader;
import org.w3c.dom.Element;

import java.util.HashMap;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

public class Dispatcher {

    private static final Dispatcher DISPATCHER = new Dispatcher();

    private final HashMap<String, Class<? extends Module>> modules = new HashMap<>();
    private final HashMap<String, Class<? extends ModuleMorphism>> moduleMorphisms = new HashMap<>();
    private final HashMap<String, Class<? extends ModuleElement>> elements = new HashMap<>();
    private final HashMap<String, Class<? extends MorphismMap>> morphismMaps = new HashMap<>();

    private LatrunculusXmlReader<Module> moduleReader; //TODO constructor and a DefinitionModuleReader
    private LatrunculusXmlReader<ModuleMorphism> moduleMorphismReader; //TODO constructor and a DefinitionModuleReader
    private LatrunculusXmlReader<ModuleElement> moduleElementReader; //TODO constructor and a DefinitionModuleReader
    private LatrunculusXmlReader<MorphismMap> mapReader; //TODO constructor and a DefinitionModuleReader

    public static Dispatcher getDispatcher() {
        return DISPATCHER;
    }

    public Module resolveModule(XMLReader reader, Element moduleElement) {
        String moduleName = moduleElement.getAttribute(TYPE_ATTR);
        Class<? extends Module> clazz = modules.get(moduleName);
        return moduleReader.fromXML(moduleElement, clazz, reader);
    }

    public ModuleMorphism resolveModuleMorphism(XMLReader reader, Element morphismElement) {
        String morphismName = morphismElement.getAttribute(TYPE_ATTR);
        Class<? extends ModuleMorphism> clazz = moduleMorphisms.get(morphismName);
        return moduleMorphismReader.fromXML(morphismElement, clazz, reader);
    }

    public ModuleElement resolveElement(XMLReader reader, Element element) {
        String elementName = element.getAttribute(TYPE_ATTR);
        Class<? extends ModuleElement> elementClass = elements.get(elementName);
        return moduleElementReader.fromXML(element, elementClass, reader);
    }

    public MorphismMap resolveMorphismMap(XMLReader reader, Element morphismMapElement) {
        String morphismMapName = morphismMapElement.getAttribute(TYPE_ATTR);
        Class<? extends MorphismMap> dispatch = morphismMaps.get(morphismMapName);
        return mapReader.fromXML(morphismMapElement, dispatch, reader);
    }

    public void addModule(Class<? extends Module> clazz) {
        modules.put(clazz.getSimpleName(), clazz);
    }

    public void addModuleMorphism(Class<? extends ModuleMorphism> clazz) {
        moduleMorphisms.put(clazz.getSimpleName(), clazz);
    }

    public void addMorphismMap(Class<? extends MorphismMap> clazz) {
        morphismMaps.put(clazz.getSimpleName(), clazz);
    }

    public void addModuleElement(Class<? extends ModuleElement> clazz) {
        elements.put(clazz.getSimpleName(), clazz);
    }    

    /**
     * Every type of ModuleMorphism must be registered here
     * so that an XML representation can be read.
     */
    //TODO annotate this classes for autodetection
    private void init() {
        // modules
        addModule(ZRing.class);
        addModule(ZnRing.class);
        addModule(RRing.class);
        addModule(QRing.class);
        addModule(CRing.class);
        
        addModule(ZStringRing.class);
        addModule(ZnStringRing.class);
        addModule(RStringRing.class);
        addModule(QStringRing.class);
        
        addModule(ZProperFreeModule.class);
        addModule(ZnProperFreeModule.class);
        addModule(RProperFreeModule.class);
        addModule(QProperFreeModule.class);
        addModule(CProperFreeModule.class);
        
        addModule(ZStringProperFreeModule.class);
        addModule(ZnStringProperFreeModule.class);
        addModule(RStringProperFreeModule.class);
        addModule(QStringProperFreeModule.class);

        addModule(PolynomialRing.class);
        addModule(PolynomialProperFreeModule.class);
        addModule(ModularPolynomialRing.class);
        addModule(ModularPolynomialProperFreeModule.class);
        
        addModule(ProductRing.class);
        
        addModule(RestrictedModule.class);
        
        // module elements
        //TODO DirectSumElement, ProductElement, ProductProperFreeElement are not registered
        addModuleElement(ZElement.class);
        addModuleElement(ZnElement.class);
        addModuleElement(RElement.class);
        addModuleElement(QElement.class);
        addModuleElement(CElement.class);
        
        addModuleElement(ZStringElement.class);
        addModuleElement(ZnStringElement.class);
        addModuleElement(RStringElement.class);
        addModuleElement(QStringElement.class);

        addModuleElement(ZProperFreeElement.class);
        addModuleElement(ZnProperFreeElement.class);
        addModuleElement(RProperFreeElement.class);
        addModuleElement(QProperFreeElement.class);
        addModuleElement(CProperFreeElement.class);
        
        addModuleElement(ZStringProperFreeElement.class);
        addModuleElement(ZnStringProperFreeElement.class);
        addModuleElement(RStringProperFreeElement.class);
        addModuleElement(QStringProperFreeElement.class);

        addModuleElement(PolynomialElement.class);
        addModuleElement(PolynomialProperFreeElement.class);
        addModuleElement(ModularPolynomialElement.class);
        addModuleElement(ModularPolynomialProperFreeElement.class);

        addModuleElement(RestrictedElement.class);
        
        // module morphisms
        addModuleMorphism(ZnAffineMorphism.class);
        addModuleMorphism(ZAffineMorphism.class);
        addModuleMorphism(QAffineMorphism.class);
        addModuleMorphism(RAffineMorphism.class);
        addModuleMorphism(CAffineMorphism.class);
        
        addModuleMorphism(ZnFreeAffineMorphism.class);
        addModuleMorphism(ZFreeAffineMorphism.class);
        addModuleMorphism(QFreeAffineMorphism.class);
        addModuleMorphism(RFreeAffineMorphism.class);
        addModuleMorphism(CFreeAffineMorphism.class);
        
        addModuleMorphism(CompositionMorphism.class);
        addModuleMorphism(ConstantMorphism.class);
        addModuleMorphism(DifferenceMorphism.class);
        addModuleMorphism(SumMorphism.class);
        addModuleMorphism(ProductMorphism.class);
        addModuleMorphism(FoldingMorphism.class);
        addModuleMorphism(IdentityMorphism.class);
        addModuleMorphism(PolynomialMorphism.class);
        addModuleMorphism(PowerMorphism.class);
        addModuleMorphism(ScaledMorphism.class);
        addModuleMorphism(TranslationMorphism.class);
        addModuleMorphism(ModuloMorphism.class);
        addModuleMorphism(EmbeddingMorphism.class);
        addModuleMorphism(ProjectionMorphism.class);
        addModuleMorphism(ConjugationMorphism.class);
        addModuleMorphism(GenericBasisMorphism.class);
        addModuleMorphism(ReorderMorphism.class);
        addModuleMorphism(CanonicalMorphism.class);
        addModuleMorphism(GenericAffineMorphism.class);
        addModuleMorphism(SplitMorphism.class);
        addModuleMorphism(CastMorphism.class);

        //TODO AutoListMorphismMap, EmptyMorphismMap, IndexMorphismMap, ListMorphismMap are not registered
        addMorphismMap(ModuleMorphismMap.class);
        addMorphismMap(ConstantModuleMorphismMap.class);
    }
    
        
    private Dispatcher() {
        init();
    }

}