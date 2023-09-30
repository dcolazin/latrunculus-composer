/*
 * Copyright (C) 2001, 2005 Gérard Milmeister
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.vetronauta.latrunculus.core.math.module.rational;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.arith.string.QString;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.server.xml.XMLInputOutput;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.w3c.dom.Element;

/**
 * The ring of QString.
 * @see QStringElement
 * 
 * @author Gérard Milmeister
 */
public final class QStringRing
        extends StringRing
        implements QStringFreeModule {

    public static final QStringRing ring = new QStringRing();

    public QStringElement getZero() {
        return new QStringElement(QString.getZero());
    }

    
    public QStringElement getOne() {
        return new QStringElement(QString.getOne());
    }

    
    public QStringProperFreeModule getNullModule() {
        return QStringProperFreeModule.nullModule;
    }
    
    
    public boolean isField() {
        return false;
    }
    
    
    public boolean isVectorSpace() {
        return false;
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof QStringElement);
    }

    
    public FreeModule getFreeModule(int dimension) {
        return QStringProperFreeModule.make(dimension);
    }

    
    public Ring getFactorRing() {
        return QRing.ring;
    }

    
    public boolean equals(Object object) {
        return this == object;
    }

    
    public int compareTo(Module object) {
        if (this == object) {
            return 0;
        }
        else {
            return super.compareTo(object);
        }
    }


    public ModuleElement cast(ModuleElement element) {
        if (element instanceof QStringElement) {
            return element;
        }
        else if (element instanceof StringElement) {
            RingString rs = ((StringElement)element).getRingString();
            return new QStringElement(new QString(rs));
        }
        else {
            QElement e = QRing.ring.cast(element);
            if (e == null) {
                return null;
            }
            else {
                return new QStringElement(new QString(e.getValue()));
            }
        }       
    }

    
    public String toString() {
        return "QStringRing";
    }
    
    
    public String toVisualString() {
        return "C-String";
    }

    
    public RingElement parseString(String string) {
        try {
            return new QStringElement(QString.parseQString(TextUtils.unparenthesize(string)));
        }
        catch (Exception e) {
            return null;
        }
    }

    
    public void toXML(XMLWriter writer) {
        writer.emptyWithType(MODULE, getElementTypeName());
    }
    
    
    public Module fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        return QStringRing.ring;
    }

    public String getElementTypeName() {
        return "QStringRing";
    }
    
    
    public int hashCode() {
        return basicHash;
    }

    
    private final static int basicHash = "QStringRing".hashCode();

    private QStringRing() { /* not allowed */ }
}