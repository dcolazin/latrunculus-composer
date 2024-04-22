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
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.server.parse.ArithmeticParsingUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComplexProperty extends PluginProperty<Complex> implements ActionListener, CaretListener {

    private JTextField textField = null;
    private Color bgColor = null;
    private static final UserPreferences prefs = UserPreferences.getUserPreferences();

    public ComplexProperty(String key, String name, Complex value) {
        super(key, name, value);
    }
    
    public ComplexProperty(ComplexProperty prop) {
        super(prop);
    }

    @Override
    public JComponent getJComponent() {
        textField = new JTextField();
        textField.setText(getValue().toString());
        textField.addCaretListener(this);
        textField.addActionListener(this);
        bgColor = textField.getBackground(); 
        return textField;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }
    
    @Override
    public void caretUpdate(CaretEvent e) {
        update();
    }

    public void update() {
        textField.setBackground(bgColor);
        String s = textField.getText();
        try {
            tmpValue = ArithmeticParsingUtils.parseComplex(s);
        }
        catch (NumberFormatException e) { /* do nothing */ }
        textField.setBackground(prefs.getEntryErrorColor());
    }

    @Override
    public void revert() {
        tmpValue = value;
        textField.setText(value.toString());
    }
    
    @Override
    public ComplexProperty deepCopy() {
        return new ComplexProperty(this);
    }

    @Override
    public String toString() {
        return "ComplexProperty["+getOrder()+","+getKey()+","+getName()+","+value+"]";
    }

}
