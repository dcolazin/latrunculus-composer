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

import lombok.Getter;
import org.rubato.composer.components.JSelectDenotator;
import org.vetronauta.latrunculus.client.plugin.icons.Icons;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.repository.Repository;
import org.vetronauta.latrunculus.plugin.base.AbstractRubette;
import org.vetronauta.latrunculus.plugin.base.RubatoConstants;
import org.vetronauta.latrunculus.plugin.base.Rubette;
import org.vetronauta.latrunculus.plugin.base.RunInfo;

import javax.swing.*;
import java.awt.*;


public class SourceRubette extends AbstractRubette {

    public SourceRubette() {
        setInCount(0);
        setOutCount(1);
    }

    public SourceRubette(Denotator denotator, boolean refreshable) {
        this();
        this.denotator = denotator;
        this.refreshable = refreshable;
    }

    
    public void run(RunInfo runInfo) {
        if (refreshable && denotator != null) {
            String s = denotator.getNameString();
            Denotator d = rep.getDenotator(s);
            if (d != null) {
                setDenotator(d);
            }
            else {
                addError(BuiltinMessages.getString("SourceRubette.namenotavailable"), s);
                return;
            }
        }
    }

    
    public Rubette duplicate() {
        SourceRubette rubette = new SourceRubette();
        rubette.setDenotator(denotator);
        rubette.setRefreshable(isRefreshable());
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
            refreshBox = new JCheckBox(BuiltinMessages.getString("SourceRubette.selfrefreshable"));
            refreshBox.setToolTipText(BuiltinMessages.getString("SourceRubette.selfrefreshtooltip"));
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
        refreshBox.setSelected(isRefreshable());
    }

    
    public boolean hasInfo() {
        return true;
    }
    
    
    public String getInfo() {
        return name;
    }
    
    public String getShortDescription() {
        return BuiltinMessages.getString("SourceRubette.containsdenotator");
    }

    
    public String getLongDescription() {
        return "The Source Rubette stores a denotator."; 
    }
    
    
    public String getOutTip(int i) {
        return BuiltinMessages.getString("SourceRubette.storeddenotator");
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

    
    public boolean isRefreshable() {
        return refreshable;
    }

    
    private JPanel           properties = null;
    @Getter
    private Denotator        denotator = null;
    private JSelectDenotator selector;
    private JCheckBox        refreshBox;
    private String           name = " "; 
    private boolean          refreshable = false;
    private static final Repository rep = Repository.systemRepository();

}
