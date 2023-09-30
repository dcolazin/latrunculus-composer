package org.vetronauta.latrunculus.server.xml;

import org.w3c.dom.Element;

/**
 * @author vetronauta
 */
@Deprecated
public interface XMLInput<T> {

    /**
     * Reads XML representation from <code>reader</code> starting with <code>element</code>.
     *
     * @return an object of type T or null if parsing failed
     */
    T fromXML(XMLReader reader, Element element);


    /**
     * Returns the value of the type attribute for this class.
     */
    String getElementTypeName();

}
