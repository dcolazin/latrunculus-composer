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
import org.vetronauta.latrunculus.core.exception.LatrunculusUnsupportedException;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.MathDiagram;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.ARROW;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DIAGRAM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FROM_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.REF_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TO_ATTR;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultDiagramXmlWriter implements LatrunculusXmlWriter<Diagram> {

    private final LatrunculusXmlWriter<MathDefinition> definitionWriter;

    @Override
    public void toXML(Diagram object, XMLWriter writer) {
        if (object instanceof FormDiagram) {
            write((FormDiagram) object, writer);
            return;
        }
        if (object instanceof MathDiagram) {
            // TODO: not yet implemented
            throw new LatrunculusUnsupportedException();
        }
    }

    private void write(FormDiagram diagram, XMLWriter writer) {
        writer.openBlockWithType(DIAGRAM, diagram.getElementTypeName());
        for (int i = 0; i < diagram.getFormCount(); i++) {
            writer.empty(FORM, REF_ATTR, diagram.getForm(i).getNameString());
        }
        for (int i = 0; i < diagram.getFormCount(); i++) {
            for (int j = 0; j < diagram.getFormCount(); j++) {
                if (diagram.getArrowCount(i, j) > 0) {
                    for (int k = 0; k < diagram.getArrowCount(i, j); k++) {
                        writer.openBlock(ARROW, FROM_ATTR, i, TO_ATTR, j);
                        definitionWriter.toXML(diagram.getArrow(i, j, k), writer);
                        writer.closeBlock();
                    }
                }
            }
        }
        writer.closeBlock();
    }

}
