/*
 * Copyright (C) 2005 Gérard Milmeister
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

package org.rubato.rubettes.builtin;

import org.rubato.base.AbstractRubette;
import org.rubato.base.Repository;
import org.rubato.base.RubatoConstants;
import org.rubato.base.Rubette;
import org.rubato.composer.RunInfo;
import org.rubato.composer.components.JSelectDenotator;
import org.rubato.composer.icons.Icons;
import org.vetronauta.latrunculus.core.math.MathDefinition;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.server.xml.XMLReader;
import org.vetronauta.latrunculus.server.xml.XMLWriter;
import org.vetronauta.latrunculus.server.xml.writer.DefaultDefinitionXmlWriter;
import org.vetronauta.latrunculus.server.xml.writer.LatrunculusXmlWriter;
import org.w3c.dom.Element;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import static org.vetronauta.latrunculus.server.xml.XMLConstants.DENOTATOR;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.FALSE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.TRUE_VALUE;
import static org.vetronauta.latrunculus.server.xml.XMLConstants.VALUE_ATTR;


public class SourceRubette extends AbstractRubette {

    //TODO rubette writer
    private final LatrunculusXmlWriter<MathDefinition> definitionXmlWriter = new DefaultDefinitionXmlWriter();
    
    public SourceRubette() {
        setInCount(0);
        setOutCount(1);
    }

    
    public void run(RunInfo runInfo) {
        if (refreshable && denotator != null) {
            String s = denotator.getNameString();
            Denotator d = rep.getDenotator(s);
            if (d != null) {
                setDenotator(d);
            }
            else {
                addError(Messages.getString("SourceRubette.namenotavailable"), s); 
                return;
            }
        }
    }

    
    public Rubette duplicate() {
        SourceRubette rubette = new SourceRubette();
        rubette.setDenotator(denotator);
        rubette.setRefreshable(getRefreshable());
        return rubette;
    }
    
    
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }

    
    public String getName() {
        return "Source"; 
    }

    
    public boolean hasProperties() {
        return true;
    }
    

    public JComponent getProperties() {
        if (properties == null) {
            properties = new JPanel();            
            properties.setLayout(new BorderLayout());
            selector = new JSelectDenotator(Repository.systemRepository());
            selector.setDenotator(denotator);
            properties.add(selector, BorderLayout.CENTER);
            refreshBox = new JCheckBox(Messages.getString("SourceRubette.selfrefreshable")); 
            refreshBox.setToolTipText(Messages.getString("SourceRubette.selfrefreshtooltip")); 
            refreshBox.setSelected(refreshable);
            properties.add(refreshBox, BorderLayout.SOUTH);
        }
        return properties;
    }
    
    
    public boolean applyProperties() {
        setDenotator(selector.getDenotator());
        setRefreshable(refreshBox.isSelected());
        return true;
    }
    
    
    public void revertProperties() {
        selector.setDenotator(denotator);
        refreshBox.setSelected(getRefreshable());
    }

    
    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        return name;
    }

    
    public ImageIcon getIcon() {
        return icon;
    }

    
    public String getShortDescription() {
        return Messages.getString("SourceRubette.containsdenotator"); 
    }

    
    public String getLongDescription() {
        return "The Source Rubette stores a denotator."; 
    }
    
    
    public String getOutTip(int i) {
        return Messages.getString("SourceRubette.storeddenotator"); 
    }

    
    private static final String REFRESHABLE = "Refreshable";
    
    
    public void toXML(XMLWriter writer) {
        writer.empty(REFRESHABLE, VALUE_ATTR, refreshable?TRUE_VALUE:FALSE_VALUE);
        if (denotator != null) {
            definitionXmlWriter.toXML(denotator, writer);
        }
    }
    
    
    public Rubette fromXML(XMLReader reader, Element element) {
        Denotator d = null;
        boolean r = false; 
        Element child = XMLReader.getChild(element, REFRESHABLE);
        String val = child.getAttribute(VALUE_ATTR);
        if (val.equals(TRUE_VALUE)) {
            r = true;
        }
        child = XMLReader.getNextSibling(child, DENOTATOR);
        if (child != null) {
            d = reader.parseDenotator(child);
        }
        SourceRubette rubette = new SourceRubette();
        rubette.setRefreshable(r);
        rubette.setDenotator(d);
        return rubette;
    }
    
    
    private void setDenotator(Denotator d) {
        denotator = d;
        if (d == null) {
            name = " "; 
        }
        else {
            name = d.getNameString()+": "+d.getForm().getNameString(); 
        }
        setOutput(0, denotator);
    }
    
    
    private void setRefreshable(boolean b) {
        refreshable = b;
    }

    
    private boolean getRefreshable() {
        return refreshable;
    }

    
    private JPanel           properties = null;
    private Denotator        denotator = null;
    private JSelectDenotator selector;
    private JCheckBox        refreshBox;
    private String           name = " "; 
    private boolean          refreshable = false;
    private static final ImageIcon icon;
    private static final Repository rep = Repository.systemRepository();

    static {
        icon = Icons.loadIcon(SourceRubette.class, "/images/rubettes/builtin/sourceicon.png"); 
    }
}
