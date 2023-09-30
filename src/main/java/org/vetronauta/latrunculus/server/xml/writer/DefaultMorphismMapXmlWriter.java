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

package org.vetronauta.latrunculus.server.xml.writer;

import lombok.RequiredArgsConstructor;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.yoneda.AutoListMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.ConstantModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.EmptyMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.IndexMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.ListMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.ModuleMorphismMap;
import org.vetronauta.latrunculus.core.math.yoneda.MorphismMap;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDEX_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MORPHISM_MAP;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultMorphismMapXmlWriter implements LatrunculusXmlWriter<MorphismMap> {

    private final LatrunculusXmlWriter<MathDefinition> definitionWriter;

    @Override
    public void toXML(MorphismMap object, XMLWriter writer) {
        if (object instanceof AutoListMorphismMap) {
            write((AutoListMorphismMap) object, writer);
            return;
        }
        if (object instanceof ConstantModuleMorphismMap) {
            write((ConstantModuleMorphismMap) object, writer);
            return;
        }
        if (object instanceof EmptyMorphismMap) {
            write((EmptyMorphismMap) object, writer);
            return;
        }
        if (object instanceof IndexMorphismMap) {
            write((IndexMorphismMap) object, writer);
            return;
        }
        if (object instanceof ListMorphismMap) {
            write((ListMorphismMap) object, writer);
            return;
        }
        if (object instanceof ModuleMorphismMap) {
            write((ModuleMorphismMap) object, writer);
        }
    }

    private void write(AutoListMorphismMap element, XMLWriter writer) {
        writer.openBlockWithType(MORPHISM_MAP, element.getElementTypeName());
        for (Denotator d : element.getFactors()) {
            definitionWriter.toXML(d, writer);
        }
        writer.closeBlock();
    }

    private void write(ConstantModuleMorphismMap element, XMLWriter writer) {
        writer.openBlockWithType(MORPHISM_MAP, element.getElementTypeName());
        definitionWriter.toXML(element.getDomain(), writer);
        definitionWriter.toXML(element.getElement(), writer);
        writer.closeBlock();
    }

    private void write(EmptyMorphismMap element, XMLWriter writer) {
        writer.emptyWithType(MORPHISM_MAP, element.getElementTypeName());
    }

    private void write(IndexMorphismMap element, XMLWriter writer) {
        writer.openBlockWithType(MORPHISM_MAP, element.getElementTypeName(), INDEX_ATTR, element.getIndex());
        definitionWriter.toXML(element.getFactor(), writer);
        writer.closeBlock();
    }

    private void write(ListMorphismMap element, XMLWriter writer) {
        writer.openBlockWithType(MORPHISM_MAP, element.getElementTypeName());
        for (Denotator d : element.getFactors()) {
            definitionWriter.toXML(d, writer);
        }
        writer.closeBlock();
    }

    private void write(ModuleMorphismMap element, XMLWriter writer) {
        writer.openBlockWithType(MORPHISM_MAP, element.getElementTypeName());
        definitionWriter.toXML(element.getMorphism(), writer);
        writer.closeBlock();
    }

}
