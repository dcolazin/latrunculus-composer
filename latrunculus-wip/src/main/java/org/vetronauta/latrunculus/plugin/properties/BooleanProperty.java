/*
 * Copyright (C) 2013 Florian Thalmann
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

package org.vetronauta.latrunculus.plugin.properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BooleanProperty extends PluginProperty<Boolean> implements ActionListener {

	private String[] allowedExtensions;
    private JCheckBox booleanCheckbox;

    public BooleanProperty(String key, String name, boolean value) {
        super(key, name, value);
    }

    public BooleanProperty(BooleanProperty property) {
        super(property);
        this.allowedExtensions = property.allowedExtensions;
    }

    @Override
    public JComponent getJComponent() {
        JPanel propertyPanel = new JPanel();
    	propertyPanel.setLayout(new BorderLayout(2, 0));
        this.booleanCheckbox = new JCheckBox();
        this.booleanCheckbox.setSelected(this.value);
        this.booleanCheckbox.addActionListener(this);
        propertyPanel.add(this.booleanCheckbox, BorderLayout.CENTER);
        
        return propertyPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.tmpValue = this.booleanCheckbox.isSelected();
    }

    @Override
    public void revert() {
        this.tmpValue = value;
        this.booleanCheckbox.setSelected(this.tmpValue);
    }
    
    @Override
    public BooleanProperty deepCopy() {
        return new BooleanProperty(this);
    }

    @Override
    public String toString() {
        return "BooleanProperty["+getOrder()+","+getKey()+","+getName()+","+value+"]";
    }

}
