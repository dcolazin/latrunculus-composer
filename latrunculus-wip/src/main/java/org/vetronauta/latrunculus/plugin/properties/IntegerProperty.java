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

package org.vetronauta.latrunculus.plugin.properties;

import org.rubato.composer.preferences.UserPreferences;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntegerProperty extends PluginProperty<Integer> implements ActionListener, CaretListener {

    public IntegerProperty(String key, String name, int value, int min, int max) {
        super(key, name, value);
        if (min > max) {
            int t = min;
            min = max;
            max = t;
        }
        this.min = min;
        this.max = max;
    }
    
    
    public IntegerProperty(String key, String name, int value) {
        this(key, name, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    
    public IntegerProperty(IntegerProperty prop) {
        super(prop);
        this.min = prop.min;
        this.max = prop.max;
    }

    @Override
    protected void internalSet(Integer value) {
        if (value < min) {
            value = min;
        }
        else if (value > max) {
            value = max;
        }
        this.value = value;
        this.tmpValue = value;
    }
    
    public JComponent getJComponent() {
        textField = new JTextField();
        textField.setText(Integer.toString(getValue()));
        textField.addCaretListener(this);
        textField.addActionListener(this);
        bgColor = textField.getBackground(); 
        return textField;
    }
    
    
    public void actionPerformed(ActionEvent e) {
        update();
    }
    
    
    public void caretUpdate(CaretEvent e) {
        update();
    }
    
    
    public void update() {
        textField.setBackground(bgColor);
        String s = textField.getText();
        try {
            int i = Integer.parseInt(s);
            if (i >= min && i <= max) {
                tmpValue = i;
                return;
            }
        }
        catch (NumberFormatException e) { /* do nothing */ }
        textField.setBackground(prefs.getEntryErrorColor());
    }
    
    public void revert() {
        tmpValue = value;
        textField.setText(Integer.toString(value));
    }
    
    @Override
    public IntegerProperty deepCopy() {
        return new IntegerProperty(this);
    }
    
    public String toString() {
        return "IntegerProperty["+getOrder()+","+getKey()+","+getName()+","+value+","+min+","+max+"]";
    }

    
    private int min;
    private int max;
    private JTextField textField = null;
    
    private Color bgColor = null;
    private static final UserPreferences prefs = UserPreferences.getUserPreferences();
}
