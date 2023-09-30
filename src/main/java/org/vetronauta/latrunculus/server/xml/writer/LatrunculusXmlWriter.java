package org.vetronauta.latrunculus.server.xml.writer;

import org.vetronauta.latrunculus.server.xml.XMLWriter;

/**
 * @author vetronauta
 */
public interface LatrunculusXmlWriter<T> {

    void toXML(T object, XMLWriter writer);

}
