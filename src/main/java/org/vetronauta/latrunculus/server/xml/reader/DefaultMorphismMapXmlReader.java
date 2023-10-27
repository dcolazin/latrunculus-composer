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

package org.vetronauta.latrunculus.server.xml.reader;

import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.map.AutoListMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.ConstantModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.map.EmptyMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.IndexMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.ListMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.ModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.map.MorphismMap;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.ArrayList;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.DENOTATOR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDEX_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_MORPHISM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MORPHISM_MAP;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * @author vetronauta
 */
public class DefaultMorphismMapXmlReader implements LatrunculusXmlReader<MorphismMap> {
    
    @Override
    public MorphismMap fromXML(Element element, Class<? extends MorphismMap> clazz, XMLReader reader) {
        if (AutoListMorphismMap.class.isAssignableFrom(clazz)) {
            return readAutoListMorphismMap(element, clazz, reader);
        }
        if (ConstantModuleMorphismMap.class.isAssignableFrom(clazz)) {
            return readConstantModuleMorphismMap(element, clazz, reader);
        }
        if (EmptyMorphismMap.class.isAssignableFrom(clazz)) {
            return readEmptyMorphismMap(element, clazz, reader);
        }
        if (IndexMorphismMap.class.isAssignableFrom(clazz)) {
            return readIndexMorphismMap(element, clazz, reader);
        }
        if (ListMorphismMap.class.isAssignableFrom(clazz)) {
            return readListMorphismMap(element, clazz, reader);
        }
        if (ModuleMorphismMap.class.isAssignableFrom(clazz)) {
            return readModuleMorphismMap(element, clazz, reader);
        }
        return null;
    }

    private MorphismMap readAutoListMorphismMap(Element element, Class<?> clazz, XMLReader reader) {
        ArrayList<Denotator> newList = new ArrayList<>();
        Element child = XMLReader.getChild(element, DENOTATOR);
        while (child != null) {
            Denotator denotator = reader.parseDenotator(child);
            if (denotator != null) {
                newList.add(denotator);
            }
            child = XMLReader.getNextSibling(element, DENOTATOR);
        }
        return new AutoListMorphismMap(newList);
    }

    private MorphismMap readConstantModuleMorphismMap(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement == null) {
            reader.setError("<%1> of type %%2 is missing child element <%3>.", MORPHISM_MAP, getElementTypeName(clazz), MODULE);
            return null;
        }
        Module dom = reader.parseModule(childElement);
        childElement = XMLReader.getNextSibling(childElement, MODULE_ELEMENT);
        if (childElement == null) {
            reader.setError("<%1> of type %%2 is missing child element <%3>.", MORPHISM_MAP, getElementTypeName(clazz), MODULE_ELEMENT);
            return null;
        }
        ModuleElement mElement = reader.parseModuleElement(childElement);
        if (mElement == null) { return null; }

        return new ConstantModuleMorphismMap(dom, mElement);
    }

    private MorphismMap readEmptyMorphismMap(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));
        return EmptyMorphismMap.emptyMorphismMap;
    }

    private MorphismMap readIndexMorphismMap(Element element, Class<?> clazz, XMLReader reader) {
        int i = XMLReader.getIntAttribute(element, INDEX_ATTR, 0);
        Element child = XMLReader.getChild(element, DENOTATOR);
        if (child == null) {
            reader.setError("Expected element of type <%1>", DENOTATOR);
            return null;
        }
        Denotator denotator = reader.parseDenotator(child);
        if (denotator == null) {
            return null;
        }
        return new IndexMorphismMap(i, denotator);
    }

    private MorphismMap readListMorphismMap(Element element, Class<?> clazz, XMLReader reader) {
        ArrayList<Denotator> newList = new ArrayList<>();
        Element child = XMLReader.getChild(element, DENOTATOR);
        while (child != null) {
            Denotator denotator = reader.parseDenotator(child);
            if (denotator != null) {
                newList.add(denotator);
            }
            child = XMLReader.getNextSibling(element, DENOTATOR);
        }
        return new ListMorphismMap(newList);
    }

    private MorphismMap readModuleMorphismMap(Element element, Class<?> clazz, XMLReader reader) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName(clazz)));

        Element childElement = XMLReader.getChild(element, MODULE_MORPHISM);
        if (childElement == null) {
            reader.setError("<%1> of type %%2 is missing child element <%3>.", MORPHISM_MAP, getElementTypeName(clazz), MODULE_MORPHISM);
            return null;
        }
        ModuleMorphism morphism = reader.parseModuleMorphism(childElement);
        if (morphism == null) {
            return null;
        }
        return ModuleMorphismMap.make(morphism);
    }

    //TODO name repository as some subclasses are serialized as superclasses
    private String getElementTypeName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
}
