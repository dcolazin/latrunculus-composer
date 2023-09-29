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

package org.vetronauta.latrunculus.core.math.module.real;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.MODULE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TYPE_ATTR;

import org.vetronauta.latrunculus.core.math.arith.string.RString;
import org.vetronauta.latrunculus.core.math.arith.string.RingString;
import org.vetronauta.latrunculus.core.math.module.definition.FreeModule;
import org.vetronauta.latrunculus.core.math.module.definition.Module;
import org.vetronauta.latrunculus.core.math.module.definition.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.definition.Ring;
import org.vetronauta.latrunculus.core.math.module.definition.RingElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringElement;
import org.vetronauta.latrunculus.core.math.module.definition.StringRing;
import org.vetronauta.latrunculus.core.math.module.morphism.ModuleMorphism;
import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.server.xml.XMLInputOutput;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * The ring of RString.
 * @see RStringElement
 * 
 * @author Gérard Milmeister
 */
public final class RStringRing extends StringRing implements RStringFreeModule {

    public static final RStringRing ring = new RStringRing();

    public RStringElement getZero() {
        return new RStringElement(RString.getZero());
    }

    
    public RStringElement getOne() {
        return new RStringElement(RString.getOne());
    }

    
    public RStringProperFreeModule getNullModule() {
        return RStringProperFreeModule.nullModule;
    }
    
    
    public boolean isField() {
        return false;
    }
    
    
    public boolean isVectorSpace() {
        return false;
    }

    
    public ModuleMorphism getIdentityMorphism() {
        return ModuleMorphism.getIdentityMorphism(this);
    }

    
    public boolean hasElement(ModuleElement element) {
        return (element instanceof RStringElement);
    }

    
    public FreeModule getFreeModule(int dimension) {
        return RStringProperFreeModule.make(dimension);
    }

    
    public Ring getFactorRing() {
        return RRing.ring;
    }

    
    public boolean equals(Object object) {
        return this == object;
    }

    
    public ModuleElement cast(ModuleElement element) {
        if (element instanceof RStringElement) {
            return element;
        }
        else if (element instanceof StringElement) {
            RingString rs = ((StringElement)element).getRingString();
            return new RStringElement(new RString(rs));
        }
        else {
            RElement e = (RElement)RRing.ring.cast(element);
            if (e == null) {
                return null;
            }
            else {
                return new RStringElement(new RString(e.getValue()));
            }
        }       
    }

    
    public String toString() {
        return "RStringRing";
    }
    
    
    public String toVisualString() {
        return "R-String";
    }

    
    public RingElement parseString(String string) {
        try {
            return new RStringElement(RString.parseRString(TextUtils.unparenthesize(string)));
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
        return RStringRing.ring;
    }
    
    
    public String getElementTypeName() {
        return "RStringRing";
    }
    
    
    public static XMLInputOutput<Module> getXMLInputOutput() {
        return RStringRing.ring;
    }
    

    public int hashCode() {
        return basicHash;
    }

    
    private final static int basicHash = "RStringRing".hashCode();

    private RStringRing() { /* not allowed */ }
}
