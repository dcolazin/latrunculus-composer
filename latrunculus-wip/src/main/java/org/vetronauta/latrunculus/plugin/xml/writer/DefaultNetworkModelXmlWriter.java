package org.vetronauta.latrunculus.plugin.xml.writer;

import org.rubato.composer.network.NetworkModel;
import org.rubato.composer.notes.NoteModel;
import org.rubato.composer.rubette.Link;
import org.rubato.composer.rubette.RubetteModel;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;

import java.awt.*;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.CLASS_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NAME_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.NETWORK;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.RUBETTE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.SERIAL_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.X_ATTR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.Y_ATTR;

public class DefaultNetworkModelXmlWriter implements LatrunculusXmlWriter<NetworkModel> {

    @Override
    public void toXML(NetworkModel networkModel, XMLWriter writer) {
        writer.openBlock(NETWORK, NAME_ATTR, networkModel.getName());
        int i = 0;
        for (RubetteModel rmodel : networkModel.getRubettes()) {
            String cls = rmodel.getRubette().getClass().getCanonicalName();
            String n = rmodel.getName();
            rmodel.setSerial(i);
            Point pt = rmodel.getLocation();
            Object[] attrs = new Object[6+((pt!=null)?4:0)];
            attrs[0] = NAME_ATTR;
            attrs[1] = n;
            attrs[2] = CLASS_ATTR;
            attrs[3] = cls;
            attrs[4] = SERIAL_ATTR;
            attrs[5] = i;
            if (pt != null) {
                attrs[6] = X_ATTR;
                attrs[7] = pt.x;
                attrs[8] = Y_ATTR;
                attrs[9] = pt.y;
            }
            writer.openBlock(RUBETTE, attrs);
            rmodel.toXML(writer);
            writer.closeBlock();
            i++;
        }
        for (RubetteModel rmodel : networkModel.getRubettes()) {
            for (int j = 0; j < rmodel.getInLinkCount(); j++) {
                Link link = rmodel.getInLink(j);
                link.toXML(writer);
            }
        }
        for (NoteModel note : networkModel.getNotes()) {
            note.toXML(writer);
        }
        writer.closeBlock();
    }

}
