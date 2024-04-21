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

public class BooleanProperty extends PluginProperty implements ActionListener {
	
	private boolean value;
	private boolean tmpValue;
	private String[] allowedExtensions;
	private JPanel propertyPanel;
	private JCheckBox booleanCheckbox;
	
    
    public BooleanProperty(String key, String name, boolean value) {
        super(key, name);
        this.value = value;
    }
    
    
    public BooleanProperty(BooleanProperty property) {
        super(property);
        this.allowedExtensions = property.allowedExtensions;
        this.value = property.value;
    }
    
    
    public Object getValue() {
        return this.value;
    }
    
    
    public void setValue(Object value) {
        if (value instanceof Boolean) {
            this.setBoolean((Boolean)value);
        }
    }
    
    
    public boolean getBoolean() {
        return this.value; 
    }
    
    
    public void setBoolean(boolean value) {
        this.value = value;
        this.tmpValue = value;
    }
    
    
    public JComponent getJComponent() {
    	this.propertyPanel = new JPanel();
    	this.propertyPanel.setLayout(new BorderLayout(2, 0));        
        /*if (this.getName() != null) {
        	this.propertyPanel.setBorder(makeTitledBorder(this.getName()));
        } else {
        	this.propertyPanel.setBorder(makeTitledBorder("Boolean:"));
        }*/
        
        this.booleanCheckbox = new JCheckBox();
        this.booleanCheckbox.setSelected(this.value);
        this.booleanCheckbox.addActionListener(this);
        this.propertyPanel.add(this.booleanCheckbox, BorderLayout.CENTER);
        
        return this.propertyPanel;
    }

    
    public void actionPerformed(ActionEvent e) {
        this.tmpValue = this.booleanCheckbox.isSelected();
    }
    
    
    public void apply() {
        this.setBoolean(this.tmpValue);
    }
    
    
    public void revert() {
        this.tmpValue = value;
        this.booleanCheckbox.setSelected(this.tmpValue);
    }
    
    @Override
    public BooleanProperty deepCopy() {
        return new BooleanProperty(this);
    }
    
    public String toString() {
        return "BooleanProperty["+getOrder()+","+getKey()+","+getName()+","+value+"]";
    }

}