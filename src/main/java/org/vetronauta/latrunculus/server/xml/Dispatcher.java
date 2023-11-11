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

import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.generic.Vector;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.generic.ProductRing;
import org.vetronauta.latrunculus.core.math.element.generic.RestrictedElement;
import org.vetronauta.latrunculus.core.math.module.generic.RestrictedModule;
import org.vetronauta.latrunculus.core.math.module.generic.StringRing;
import org.vetronauta.latrunculus.core.math.module.generic.StringVectorModule;
import org.vetronauta.latrunculus.core.math.module.generic.VectorModule;
import org.vetronauta.latrunculus.core.math.module.impl.CRing;
import org.vetronauta.latrunculus.core.math.module.impl.QRing;
import org.vetronauta.latrunculus.core.math.module.impl.RRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.module.impl.ZnRing;
import org.vetronauta.latrunculus.core.math.morphism.CanonicalMorphism;
import org.vetronauta.latrunculus.core.math.morphism.CastMorphism;
import org.vetronauta.latrunculus.core.math.morphism.CompositionMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ConjugationMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ConstantMorphism;
import org.vetronauta.latrunculus.core.math.morphism.DifferenceMorphism;
import org.vetronauta.latrunculus.core.math.morphism.EmbeddingMorphism;
import org.vetronauta.latrunculus.core.math.morphism.FoldingMorphism;
import org.vetronauta.latrunculus.core.math.morphism.GenericAffineMorphism;
import org.vetronauta.latrunculus.core.math.morphism.GenericBasisMorphism;
import org.vetronauta.latrunculus.core.math.morphism.IdentityMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ModuloMorphism;
import org.vetronauta.latrunculus.core.math.morphism.PolynomialMorphism;
import org.vetronauta.latrunculus.core.math.morphism.PowerMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ProductMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ProjectionMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ReorderMorphism;
import org.vetronauta.latrunculus.core.math.morphism.ScaledMorphism;
import org.vetronauta.latrunculus.core.math.morphism.SplitMorphism;
import org.vetronauta.latrunculus.core.math.morphism.SumMorphism;
import org.vetronauta.latrunculus.core.math.morphism.TranslationMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineFreeMorphism;
import org.vetronauta.latrunculus.core.math.morphism.affine.AffineRingMorphism;
import org.vetronauta.latrunculus.core.math.element.generic.ModularPolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.ModularPolynomialRing;
import org.vetronauta.latrunculus.core.math.element.generic.PolynomialElement;
import org.vetronauta.latrunculus.core.math.module.generic.PolynomialRing;
import org.vetronauta.latrunculus.core.math.yoneda.map.ConstantModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.ModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.MorphismMap;
import org.vetronauta.latrunculus.server.xml.reader.DefaultDefinitionXmlReader;
import org.vetronauta.latrunculus.server.xml.reader.LatrunculusRestrictedXmlReader;
import org.w3c.dom.Element;

import java.util.HashMap;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

public class Dispatcher {

    private static final Dispatcher DISPATCHER = new Dispatcher();

    private final HashMap<String, Class<? extends Module>> modules = new HashMap<>();
    private final HashMap<String, Class<? extends ModuleMorphism>> moduleMorphisms = new HashMap<>();
    private final HashMap<String, Class<? extends ModuleElement>> elements = new HashMap<>();
    private final HashMap<String, Class<? extends MorphismMap>> morphismMaps = new HashMap<>();

    //TODO constructor
    private final LatrunculusRestrictedXmlReader<MathDefinition> definitionReader = new DefaultDefinitionXmlReader();

    public static Dispatcher getDispatcher() {
        return DISPATCHER;
    }

    public Module resolveModule(XMLReader reader, Element moduleElement) {
        String moduleName = moduleElement.getAttribute(TYPE_ATTR);
        Class<? extends Module> clazz = modules.get(moduleName);
        return definitionReader.fromXML(moduleElement, clazz, Module.class, reader);
    }

    public ModuleMorphism resolveModuleMorphism(XMLReader reader, Element morphismElement) {
        String morphismName = morphismElement.getAttribute(TYPE_ATTR);
        Class<? extends ModuleMorphism> clazz = moduleMorphisms.get(morphismName);
        return definitionReader.fromXML(morphismElement, clazz, ModuleMorphism.class, reader);
    }

    public ModuleElement resolveElement(XMLReader reader, Element element) {
        String elementName = element.getAttribute(TYPE_ATTR);
        Class<? extends ModuleElement> elementClass = elements.get(elementName);
        return definitionReader.fromXML(element, elementClass, ModuleElement.class, reader);
    }

    public MorphismMap resolveMorphismMap(XMLReader reader, Element morphismMapElement) {
        String morphismMapName = morphismMapElement.getAttribute(TYPE_ATTR);
        Class<? extends MorphismMap> dispatch = morphismMaps.get(morphismMapName);
        return definitionReader.fromXML(morphismMapElement, dispatch, MorphismMap.class, reader);
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
        //TODO ProductProperFreeModule, ModularPolynomialProperFreeModule, ModularPolynomialRing, PolynomialFreeModule, PolynomialRing are not registered
        addModule(ZRing.class);
        addModule(ZnRing.class);
        addModule(RRing.class);
        addModule(QRing.class);
        addModule(CRing.class);
        
        addModule(StringRing.class);
        addModule(VectorModule.class);
        addModule(StringVectorModule.class);

        addModule(PolynomialRing.class);
        addModule(ModularPolynomialRing.class);

        addModule(ProductRing.class);
        
        addModule(RestrictedModule.class);
        
        // module elements
        //TODO DirectSumElement, ProductElement, ProductProperFreeElement are not registered
        addModuleElement(Modulus.class);
        addModuleElement(ZInteger.class);
        addModuleElement(Rational.class);
        addModuleElement(Real.class);
        addModuleElement(Complex.class);

        addModuleElement(Vector.class);
        addModuleElement(StringMap.class);

        addModuleElement(PolynomialElement.class);
        addModuleElement(ModularPolynomialElement.class);

        addModuleElement(RestrictedElement.class);
        
        // module morphisms
        addModuleMorphism(AffineRingMorphism.class);
        addModuleMorphism(AffineFreeMorphism.class);

        addModuleMorphism(CanonicalMorphism.class);
        addModuleMorphism(CastMorphism.class);
        addModuleMorphism(CompositionMorphism.class);
        addModuleMorphism(ConjugationMorphism.class);
        addModuleMorphism(ConstantMorphism.class);
        addModuleMorphism(DifferenceMorphism.class);
        addModuleMorphism(EmbeddingMorphism.class);
        addModuleMorphism(FoldingMorphism.class);
        addModuleMorphism(GenericAffineMorphism.class);
        addModuleMorphism(GenericBasisMorphism.class);
        addModuleMorphism(IdentityMorphism.class);
        addModuleMorphism(ModuloMorphism.class);
        addModuleMorphism(PolynomialMorphism.class);
        addModuleMorphism(PowerMorphism.class);
        addModuleMorphism(ProductMorphism.class);
        addModuleMorphism(ProjectionMorphism.class);
        addModuleMorphism(ReorderMorphism.class);
        addModuleMorphism(ScaledMorphism.class);
        addModuleMorphism(SplitMorphism.class);
        addModuleMorphism(SumMorphism.class);
        addModuleMorphism(TranslationMorphism.class);

        //TODO AutoListMorphismMap, EmptyMorphismMap, IndexMorphismMap, ListMorphismMap are not registered
        addMorphismMap(ModuleMorphismMap.class);
        addMorphismMap(ConstantModuleMorphismMap.class);
    }
    
        
    private Dispatcher() {
        init();
    }

}
