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

public class DoubleProperty extends PluginProperty<Double> implements ActionListener, CaretListener {

    public DoubleProperty(String key, String name, double value, double min, double max) {
        super(key, name, value);
        if (min > max) {
            double t = min;
            min = max;
            max = t;
        }
        this.min = min;
        this.max = max;
    }

    public DoubleProperty(String key, String name, double value) {
        this(key, name, value, Double.MIN_VALUE, Double.MAX_VALUE);
    }
    
    public DoubleProperty(DoubleProperty prop) {
        super(prop);
        this.min = prop.min;
        this.max = prop.max;
    }

    @Override
    protected void internalSet(Double newValue) {
        if (newValue < min) {
            newValue = min;
        } else if (newValue > max) {
            newValue = max;
        }
        this.value = newValue;
        this.tmpValue = newValue;
    }

    public JComponent getJComponent() {
        textField = new JTextField();
        textField.setText(Double.toString(getValue()));
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
            double d = Double.parseDouble(s);
            if (d >= min && d <= max) {
                tmpValue = d;
                return;
            }
        }
        catch (NumberFormatException e) { /* do nothing */ }
        textField.setBackground(prefs.getEntryErrorColor());
    }

    public void revert() {
        tmpValue = value;
        textField.setText(Double.toString(value));
    }
    
    @Override
    public DoubleProperty deepCopy() {
        return new DoubleProperty(this);
    }

    public String toString() {
        return "DoubleProperty["+getOrder()+","+getKey()+","+getName()+","+value+","+min+","+max+"]";
    }

    private double min;
    private double max;
    private JTextField textField = null;
    
    private Color bgColor = null;
    private static final UserPreferences prefs = UserPreferences.getUserPreferences();
}
