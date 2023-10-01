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

package org.vetronauta.latrunculus.server.xml.reader;

import org.vetronauta.latrunculus.core.exception.LatrunculusUnsupportedException;
import org.vetronauta.latrunculus.core.math.yoneda.Diagram;
import org.vetronauta.latrunculus.core.math.yoneda.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.MathDiagram;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.w3c.dom.Element;

/**
 * @author vetronauta
 */
public class DefaultDiagramXmlReader implements LatrunculusXmlReader<Diagram> {

    @Override
    public Diagram fromXML(Element element, Class<? extends Diagram> clazz, XMLReader reader) {
        if (FormDiagram.class.isAssignableFrom(clazz)) {
            // TODO: not yet implemented
            throw new LatrunculusUnsupportedException();
        }
        if (MathDiagram.class.isAssignableFrom(clazz)) {
            // TODO: not yet implemented
            throw new LatrunculusUnsupportedException();
        }
        return null;
    }

}
