package org.vetronauta.latrunculus.plugin.xml.writer;

import org.vetronauta.latrunculus.client.plugin.properties.BooleanClientProperty;
import org.vetronauta.latrunculus.client.plugin.properties.ClientPluginProperty;
import org.vetronauta.latrunculus.client.plugin.properties.DenotatorClientProperty;
import org.vetronauta.latrunculus.client.plugin.properties.FileClientProperty;
import org.vetronauta.latrunculus.client.plugin.properties.FormClientProperty;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;
import org.vetronauta.latrunculus.client.plugin.properties.TextClientProperty;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;

import java.io.IOException;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.FALSE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TRUE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;

public class DefaultPropertyXmlWriter implements LatrunculusXmlWriter<ClientPluginProperty<?>> {

    @Override
    public void toXML(ClientPluginProperty<?> property, XMLWriter writer) {
        if (property instanceof BooleanClientProperty) {
            write((BooleanClientProperty) property, writer);
            return;
        }
        if (property instanceof DenotatorClientProperty) {
            write((DenotatorClientProperty) property, writer);
            return;
        }
        if (property instanceof FileClientProperty) {
            write((FileClientProperty) property, writer);
            return;
        }
        if (property instanceof FormClientProperty) {
            write((FormClientProperty) property, writer);
            return;
        }
        if (property instanceof TextClientProperty) {
            write((TextClientProperty) property, writer);
            return;
        }
        writeValue(property, writer);
    }

    private void writeValue(ClientPluginProperty<?> property, XMLWriter writer) {
        writer.empty(property.getKey(), VALUE_ATTR, property.getValue());
    }

    private void write(BooleanClientProperty property, XMLWriter writer) {
        writer.empty(property.getKey(), VALUE_ATTR, property.getValue() ? TRUE_VALUE : FALSE_VALUE);
    }

    private void write(DenotatorClientProperty property, XMLWriter writer) {
        writer.openBlock(property.getKey());
        if (property.getValue() != null) {
            writer.writeDenotator(property.getValue());
        }
        writer.closeBlock();
    }

    private void write(FileClientProperty property, XMLWriter writer) {
        String canonicalPath = "";
        if (property.getValue() != null) {
            try {
                canonicalPath = property.getValue().getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.empty(property.getKey(), VALUE_ATTR, writer.toRelativePath(canonicalPath));
    }

    private void write(FormClientProperty property, XMLWriter writer) {
        writer.openBlock(property.getKey());
        if (property.getValue() != null) {
            writer.writeFormRef((Form) property.getValue());
        }
        writer.closeBlock();
    }

    private void write(TextClientProperty property, XMLWriter writer) {
        writer.openBlock(property.getKey());
        writer.writeTextNode((String) property.getValue());
        writer.closeBlock();
    }

}
