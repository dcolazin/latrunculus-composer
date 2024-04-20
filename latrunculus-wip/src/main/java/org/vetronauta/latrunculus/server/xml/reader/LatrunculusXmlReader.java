package org.vetronauta.latrunculus.server.xml.reader;

import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

/**
 * @author vetronauta
 */
public interface LatrunculusXmlReader<T> {

    T fromXML(Element element, Class<? extends T> clazz, XMLReader reader);

}
