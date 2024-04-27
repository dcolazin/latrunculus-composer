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

package org.vetronauta.latrunculus.client.plugin.properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BooleanClientProperty extends ClientPluginProperty<Boolean> implements ActionListener {

    private JCheckBox booleanCheckbox;

    public BooleanClientProperty(String key, String name, boolean value) {
        super(key, name, value);
    }

    public BooleanClientProperty(BooleanClientProperty property) {
        super(property.pluginProperty);
    }

    @Override
    public JComponent getJComponent() {
        JPanel propertyPanel = new JPanel();
    	propertyPanel.setLayout(new BorderLayout(2, 0));
        this.booleanCheckbox = new JCheckBox();
        this.booleanCheckbox.setSelected(pluginProperty.getValue());
        this.booleanCheckbox.addActionListener(this);
        propertyPanel.add(this.booleanCheckbox, BorderLayout.CENTER);
        
        return propertyPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pluginProperty.setTmpValue(booleanCheckbox.isSelected());
    }

    @Override
    public void revert() {
        pluginProperty.revert(booleanCheckbox::setSelected);
    }
    
    @Override
    public BooleanClientProperty deepCopy() {
        return new BooleanClientProperty(this);
    }

}
