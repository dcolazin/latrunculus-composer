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
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.form.FormReference;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.ProperIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.morphism.RepresentableIdentityMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.server.xml.XMLWriter;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.COLIMIT_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FORM;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.HIVALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LABEL;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LABELS;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LIMIT_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LIST_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.LOVALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NAME_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.POS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.POWER_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.SIMPLE_TYPE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

/**
 * @author vetronauta
 */
@RequiredArgsConstructor
public class DefaultFormXmlWriter implements LatrunculusXmlWriter<Form> {

    private final LatrunculusXmlWriter<MathDefinition> definitionWriter;

    @Override
    public void toXML(Form object, XMLWriter writer) {
        if (object instanceof ColimitForm) {
            write((ColimitForm) object, writer);
            return;
        }
        if (object instanceof LimitForm) {
            write((LimitForm) object, writer);
            return;
        }
        if (object instanceof ListForm) {
            write((ListForm) object, writer);
            return;
        }
        if (object instanceof PowerForm) {
            write((PowerForm) object, writer);
            return;
        }
        if (object instanceof SimpleForm) {
            write((SimpleForm) object, writer);
            return;
        }
        if (object instanceof FormReference) {
            throw new LatrunculusUnsupportedException();
        }
    }

    private void write(ColimitForm form, XMLWriter writer) {
        writer.openBlock(FORM, TYPE_ATTR, COLIMIT_TYPE_VALUE, NAME_ATTR, form.getNameString());

        if (form.getLabelMap() != null) {
            writer.openBlock(LABELS);
            for (String label : form.getLabelMap().keySet()) {
                int i = form.getLabelMap().get(label);
                writer.openInline(LABEL, NAME_ATTR, label, POS_ATTR, i);
            }
            writer.closeBlock();
        }

        if (form.getIdentifier() instanceof ProperIdentityMorphism) {
            ProperIdentityMorphism im = (ProperIdentityMorphism) form.getIdentifier();
            if (im.getDiagram() instanceof FormDiagram) {
                for (Form f : form.getForms()) {
                    writer.writeFormRef(f);
                }
                writer.closeBlock();
                return;
            }
        }

        definitionWriter.toXML(form.getIdentifier(), writer);

        writer.closeBlock();
    }

    private void write(LimitForm form, XMLWriter writer) {
        writer.openBlock(FORM, TYPE_ATTR, LIMIT_TYPE_VALUE, NAME_ATTR, form.getNameString());

        if (form.getLabelMap() != null) {
            writer.openBlock(LABELS);
            for (String label : form.getLabelMap().keySet()) {
                int i = form.getLabelMap().get(label);
                writer.empty(LABEL, NAME_ATTR, label, POS_ATTR, i);
            }
            writer.closeBlock();
        }

        if (form.getIdentifier() instanceof ProperIdentityMorphism) {
            ProperIdentityMorphism im = (ProperIdentityMorphism) form.getIdentifier();
            if (im.getDiagram() instanceof FormDiagram) {
                for (Form f : form.getForms()) {
                    writer.writeFormRef(f);
                }
                writer.closeBlock();
                return;
            }
        }

        definitionWriter.toXML(form.getIdentifier(), writer);
        writer.closeBlock();
    }

    private void write(ListForm form, XMLWriter writer) {
        writer.openBlock(FORM, TYPE_ATTR, LIST_TYPE_VALUE, NAME_ATTR, form.getNameString());
        if (form.getIdentifier() instanceof ProperIdentityMorphism) {
            ProperIdentityMorphism im = (ProperIdentityMorphism) form.getIdentifier();
            if (im.getDiagram() instanceof FormDiagram) {
                writer.writeFormRef(form.getForm());
                writer.closeBlock();
                return;
            }
        }
        definitionWriter.toXML(form.getIdentifier(), writer);
        writer.closeBlock();
    }

    private void write(PowerForm form, XMLWriter writer) {
        writer.openBlock(FORM, TYPE_ATTR, POWER_TYPE_VALUE, NAME_ATTR, form.getNameString());
        if (form.getIdentifier() instanceof ProperIdentityMorphism) {
            ProperIdentityMorphism im = (ProperIdentityMorphism) form.getIdentifier();
            if (im.getDiagram() instanceof FormDiagram) {
                writer.writeFormRef(form.getForm());
                writer.closeBlock();
                return;
            }
        }
        definitionWriter.toXML(form.getIdentifier(), writer);
        writer.closeBlock();
    }

    private void write(SimpleForm form, XMLWriter writer) {
        writer.openBlock(FORM, TYPE_ATTR, SIMPLE_TYPE_VALUE, NAME_ATTR, form.getNameString());
        definitionWriter.toXML(form.getModule(), writer);
        RepresentableIdentityMorphism morphism = (RepresentableIdentityMorphism) form.getIdentifier();
        if (morphism.hasBounds()) {
            writer.openBlock(LOVALUE);
            definitionWriter.toXML(morphism.getLowValue(), writer);
            writer.closeBlock();
            writer.openBlock(HIVALUE);
            definitionWriter.toXML(morphism.getHighValue(), writer);
            writer.closeBlock();
        }
        writer.closeBlock();
    }

}
