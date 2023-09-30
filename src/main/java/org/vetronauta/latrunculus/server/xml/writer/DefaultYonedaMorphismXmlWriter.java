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
import org.vetronauta.latrunculus.core.math.yoneda.CompoundMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.Form;
import org.vetronauta.latrunculus.core.math.yoneda.ProperIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.RepresentableIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.YonedaMorphism;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORMTYPE_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.MORPHISM;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultYonedaMorphismXmlWriter implements LatrunculusXmlWriter<YonedaMorphism> {

    private final LatrunculusXmlWriter<MathDefinition> definitionWriter;

    @Override
    public void toXML(YonedaMorphism object, XMLWriter writer) {
        if (object instanceof CompoundMorphism) {
            write((CompoundMorphism) object, writer);
            return;
        }
        if (object instanceof ProperIdentityMorphism) {
            write((ProperIdentityMorphism) object, writer);
            return;
        }
        if (object instanceof RepresentableIdentityMorphism) {
            write((RepresentableIdentityMorphism) object, writer);
        }
    }

    private void write(CompoundMorphism morphism, XMLWriter writer) {
        writer.openBlock(MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getDomain(), writer);
        definitionWriter.toXML(morphism.getCodomainModule(), writer);
        definitionWriter.toXML(morphism.getMap(), writer);
        writer.close();
    }

    private void write(ProperIdentityMorphism morphism, XMLWriter writer) {
        writer.openBlockWithType(MORPHISM, morphism.getElementTypeName(), FORMTYPE_ATTR, Form.typeToString(morphism.getType()).toLowerCase());
        definitionWriter.toXML(morphism.getDiagram(), writer);
        writer.closeBlock();
    }

    private void write(RepresentableIdentityMorphism morphism, XMLWriter writer) {
        writer.openBlock(MORPHISM, morphism.getElementTypeName());
        definitionWriter.toXML(morphism.getModule(), writer);
        if (morphism.getLowValue() != null && morphism.getHighValue() != null) {
            definitionWriter.toXML(morphism.getLowValue(), writer);
            definitionWriter.toXML(morphism.getHighValue(), writer);
        }
        writer.closeBlock();
    }

}
