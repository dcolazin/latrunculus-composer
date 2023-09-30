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
import org.rubato.base.LatrunculusError;
import org.vetronauta.latrunculus.core.math.yoneda.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.DenotatorReference;
import org.vetronauta.latrunculus.core.math.yoneda.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.SimpleDenotator;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.COLIMIT_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.DENOTATOR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.INDEX_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LIMIT_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LIST_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NAME_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.POWER_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.SIMPLE_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultDenotatorXmlWriter implements LatrunculusXmlWriter<Denotator> {

    //TODO common logic

    private final DefinitionXmlWriter definitionWriter;

    @Override
    public void toXML(Denotator object, XMLWriter writer) {
        if (object instanceof ColimitDenotator) {
            write((ColimitDenotator) object, writer);
            return;
        }
        if (object instanceof LimitDenotator) {
            write((LimitDenotator) object, writer);
            return;
        }
        if (object instanceof ListDenotator) {
            write((ListDenotator) object, writer);
            return;
        }
        if (object instanceof PowerDenotator) {
            write((PowerDenotator) object, writer);
            return;
        }
        if (object instanceof SimpleDenotator) {
            write((SimpleDenotator) object, writer);
            return;
        }
        if (object instanceof DenotatorReference) {
            throw new LatrunculusError("Fatal error: trying to deserialize DenotatorReference");
        }
    }

    private void write(ColimitDenotator denotator, XMLWriter writer) {
        Object[] attrs = new Object[6 + (denotator.getName() != null ? 2 : 0)];
        attrs[0] = TYPE_ATTR;
        attrs[1] = COLIMIT_TYPE_VALUE;
        attrs[2] = FORM_ATTR;
        attrs[3] = denotator.getForm().getNameString();
        attrs[4] = INDEX_ATTR;
        attrs[5] = denotator.getIndex();
        if (denotator.getName() != null) {
            attrs[6] = NAME_ATTR;
            attrs[7] = denotator.getNameString();
        }
        writer.openBlock(DENOTATOR, attrs);

        for (Denotator d : denotator.getFactors()) {
            writer.writeDenotatorRef(d);
        }

        writer.closeBlock();
    }

    private void write(LimitDenotator denotator, XMLWriter writer) {
        Object[] attrs = new Object[4+(denotator.getName()!=null?2:0)];
        attrs[0] = TYPE_ATTR;
        attrs[1] = LIMIT_TYPE_VALUE;
        attrs[2] = FORM_ATTR;
        attrs[3] = denotator.getForm().getNameString();
        if (denotator.getName() != null) {
            attrs[4] = NAME_ATTR;
            attrs[5] = denotator.getNameString();
        }
        writer.openBlock(DENOTATOR, attrs);

        for (Denotator d : denotator.getFactors()) {
            writer.writeDenotatorRef(d);
        }

        writer.closeBlock();
    }

    private void write(ListDenotator denotator, XMLWriter writer) {
        Object[] attrs = new Object[4+(denotator.getName()!=null?2:0)];
        attrs[0] = TYPE_ATTR;
        attrs[1] = LIST_TYPE_VALUE;
        attrs[2] = FORM_ATTR;
        attrs[3] = denotator.getForm().getNameString();
        if (denotator.getName() != null) {
            attrs[4] = NAME_ATTR;
            attrs[5] = denotator.getNameString();
        }
        writer.openBlock(DENOTATOR, attrs);

        for (Denotator d : denotator.getFactors()) {
            writer.writeDenotatorRef(d);
        }

        writer.closeBlock();
    }

    private void write(PowerDenotator denotator, XMLWriter writer) {
        Object[] attrs = new Object[4+(denotator.getName()!=null?2:0)];
        attrs[0] = TYPE_ATTR;
        attrs[1] = POWER_TYPE_VALUE;
        attrs[2] = FORM_ATTR;
        attrs[3] = denotator.getForm().getNameString();
        if (denotator.getName() != null) {
            attrs[4] = NAME_ATTR;
            attrs[5] = denotator.getNameString();
        }
        writer.openBlock(DENOTATOR, attrs);

        for (Denotator d : denotator.getFactors()) {
            writer.writeDenotatorRef(d);
        }

        writer.closeBlock();
    }

    private void write(SimpleDenotator denotator, XMLWriter writer) {
        Object[] attrs = new Object[4+(denotator.getName()!=null?2:0)];
        attrs[0] = TYPE_ATTR;
        attrs[1] = SIMPLE_TYPE_VALUE;
        attrs[2] = FORM_ATTR;
        attrs[3] = denotator.getForm().getNameString();
        if (denotator.getName() != null) {
            attrs[4] = NAME_ATTR;
            attrs[5] = denotator.getNameString();
        }
        writer.openBlock(DENOTATOR, attrs);

        // write the coordinate
        definitionWriter.toXML(denotator.getModuleMorphismMap(), writer);
        if (denotator.getCoordinate() != denotator.getFrameCoordinate()) {
            // write the frame coordinate, if not identical to the coordinate
            definitionWriter.toXML(denotator.getFrameModuleMorphismMap(), writer);
        }

        writer.closeBlock();
    }

}