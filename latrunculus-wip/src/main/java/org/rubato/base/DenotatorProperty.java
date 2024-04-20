/*
 * Copyright (C) 2007 Gérard Milmeister
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

package org.rubato.base;

import org.rubato.composer.components.JSelectDenotator;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.DefaultDefinitionXmlWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;
import org.w3c.dom.Element;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DenotatorProperty extends RubetteProperty implements ActionListener {

    //TODO the writer for Rubettes and properties?
    private final LatrunculusXmlWriter<MathDefinition> definitionXmlWriter = new DefaultDefinitionXmlWriter();

    public DenotatorProperty(String key, String name, Denotator value) {
        super(key, name);
        this.value = value;
    }
    
    
    public DenotatorProperty(String key, String name) {
        this(key, name, null);
    }
    
    
    public DenotatorProperty(DenotatorProperty prop) {
        super(prop);
        this.value = prop.value;
    }
    
    
    public Object getValue() {
        return value;
    }
    
    
    public void setValue(Object value) {
        if (value instanceof Denotator) {
            setDenotator((Denotator)value);
        }
    }
    
    
    public Denotator getDenotator() {
        return value; 
    }
    
    
    public void setDenotator(Denotator value) {
        this.value = value;
        this.tmpValue = value;
    }
    
    
    public JComponent getJComponent() {
        selectDenotator = new JSelectDenotator(rep);
        selectDenotator.disableBorder();
        selectDenotator.addActionListener(this);
        selectDenotator.setDenotator(value);
        return selectDenotator;
    }

    
    public void actionPerformed(ActionEvent e) {
        tmpValue = selectDenotator.getDenotator();
    }
    
    
    public void apply() {
        setDenotator(tmpValue);
    }
    
    
    public void revert() {
        tmpValue = value;
        selectDenotator.setDenotator(tmpValue);
    }
    
    @Override
    public DenotatorProperty deepCopy() {
        return new DenotatorProperty(this);
    }
    
    
    public void toXML(XMLWriter writer) {
        writer.openBlock(getKey());
        if (value != null) {
            definitionXmlWriter.toXML(value, writer);
        }
        writer.closeBlock();
    }
    
    
    public RubetteProperty fromXML(XMLReader reader, Element element) {
        DenotatorProperty property = deepCopy();
        Element child = XMLReader.getChild(element, "Denotator"); 
        if (child == null) {
            property.setDenotator(null);
        }
        else {
            Denotator d = reader.parseDenotator(child);
            property.setDenotator(d);
        }
        return property;
    }

    
        public String toString() {
        return "DenotatorProperty["+getOrder()+","+getKey()+","+getName()+","+value+"]";
    }

    
    private Denotator value;
    private Denotator tmpValue;
    private JSelectDenotator selectDenotator;
    
    private static Repository rep = Repository.systemRepository();
}
