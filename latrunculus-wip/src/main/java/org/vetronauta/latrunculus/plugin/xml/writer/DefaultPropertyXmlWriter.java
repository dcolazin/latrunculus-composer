package org.vetronauta.latrunculus.plugin.xml.writer;

import org.vetronauta.latrunculus.client.properties.BooleanProperty;
import org.vetronauta.latrunculus.client.properties.DenotatorProperty;
import org.vetronauta.latrunculus.client.properties.FileProperty;
import org.vetronauta.latrunculus.client.properties.FormProperty;
import org.vetronauta.latrunculus.client.properties.RubetteProperty;
import org.vetronauta.latrunculus.client.properties.TextProperty;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;

import java.io.IOException;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.FALSE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TRUE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;

public class DefaultPropertyXmlWriter implements LatrunculusXmlWriter<RubetteProperty> {

    @Override
    public void toXML(RubetteProperty property, XMLWriter writer) {
        if (property instanceof BooleanProperty) {
            write((BooleanProperty) property, writer);
            return;
        }
        if (property instanceof DenotatorProperty) {
            write((DenotatorProperty) property, writer);
            return;
        }
        if (property instanceof FileProperty) {
            write((FileProperty) property, writer);
            return;
        }
        if (property instanceof FormProperty) {
            write((FormProperty) property, writer);
            return;
        }
        if (property instanceof TextProperty) {
            write((TextProperty) property, writer);
            return;
        }
        writeValue(property, writer);
    }

    private void writeValue(RubetteProperty property, XMLWriter writer) {
        writer.empty(property.getKey(), VALUE_ATTR, property.getValue());
    }

    private void write(BooleanProperty property, XMLWriter writer) {
        writer.empty(property.getKey(), VALUE_ATTR, (boolean) property.getValue() ? TRUE_VALUE : FALSE_VALUE);
    }

    private void write(DenotatorProperty property, XMLWriter writer) {
        writer.openBlock(property.getKey());
        if (property.getValue() != null) {
            writer.writeDenotator((Denotator) property.getValue());
        }
        writer.closeBlock();
    }

    private void write(FileProperty property, XMLWriter writer) {
        String canonicalPath = "";
        if (property.getFile() != null) {
            try {
                canonicalPath = property.getFile().getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.empty(property.getKey(), VALUE_ATTR, writer.toRelativePath(canonicalPath));
    }

    private void write(FormProperty property, XMLWriter writer) {
        writer.openBlock(property.getKey());
        if (property.getValue() != null) {
            writer.writeFormRef((Form) property.getValue());
        }
        writer.closeBlock();
    }

    private void write(TextProperty property, XMLWriter writer) {
        writer.openBlock(property.getKey());
        writer.writeTextNode((String) property.getValue());
        writer.closeBlock();
    }

}
