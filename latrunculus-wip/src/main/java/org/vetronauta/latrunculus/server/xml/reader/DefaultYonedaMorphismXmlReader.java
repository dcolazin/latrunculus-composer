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

import org.vetronauta.latrunculus.core.exception.LatrunculusUnsupportedException;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.CompoundMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.ProperIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.RepresentableIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.YonedaMorphism;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE_ELEMENT;

/**
 * @author vetronauta
 */
public class DefaultYonedaMorphismXmlReader implements LatrunculusXmlReader<YonedaMorphism> {
    
    @Override
    public YonedaMorphism fromXML(Element element, Class<? extends YonedaMorphism> clazz, XMLReader reader) {
        if (CompoundMorphism.class.isAssignableFrom(clazz)) {
            throw new LatrunculusUnsupportedException(); // TODO: not yet implemented
        }
        if (ProperIdentityMorphism.class.isAssignableFrom(clazz)) {
            throw new LatrunculusUnsupportedException(); // TODO: not yet implemented
        }
        if (RepresentableIdentityMorphism.class.isAssignableFrom(clazz)) {
            return readRepresentableIdentityMorphism(element, reader);
        }
        return null;
    }

    public YonedaMorphism readRepresentableIdentityMorphism(Element element, XMLReader reader) {
        Element child = XMLReader.getChild(element, MODULE);
        if (child == null) {
            reader.setError("Expected element of type <%1>", MODULE);
            return null;
        }
        Module m = reader.parseModule(child);
        if (m == null) {
            return null;
        }
        ModuleElement l = null;
        ModuleElement h = null;
        child = XMLReader.getNextSibling(child, MODULE_ELEMENT);
        if (child != null) {
            l = reader.parseModuleElement(child);
            if (l != null) {
                child = XMLReader.getNextSibling(child, MODULE_ELEMENT);
                h = reader.parseModuleElement(child);
            }
        }
        if (l == null || h == null) {
            return new RepresentableIdentityMorphism(m);
        }
        else {
            return new RepresentableIdentityMorphism(m, l, h);
        }
    }
    
}
