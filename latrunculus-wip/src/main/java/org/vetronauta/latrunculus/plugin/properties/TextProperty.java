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

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class TextProperty extends PluginProperty<String> implements CaretListener {

    public TextProperty(String key, String name, String value, int cols, int rows, boolean lineWrap, boolean wordWrap) {
        super(key, name, value);
        if (cols < 10) {
            cols = 10;
        }
        else if (cols > 200) {
            cols = 200;
        }
        if (rows < 2) {
            rows = 2;
        }
        else if (rows > 100) {
            rows = 100;
        }
        this.cols = cols;
        this.rows = rows;
        this.lineWrap = lineWrap;
        this.wordWrap = wordWrap;
    }
    
    
    public TextProperty(String key, String name, String value) {
        this(key, name, value, 40, 5, true, true);
    }
    
    
    public TextProperty(TextProperty prop) {
        super(prop);
        this.cols = prop.cols;
        this.rows = prop.rows;
    }

    public JComponent getJComponent() {
        textArea = new JTextArea(rows, cols);
        textArea.setText(value);
        textArea.addCaretListener(this);
        textArea.setEditable(true);
        textArea.setLineWrap(lineWrap);
        textArea.setWrapStyleWord(wordWrap);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }
    
    
    public void caretUpdate(CaretEvent e) {
        update();
    }
    
    
    public void update() {
        tmpValue = textArea.getText();
    }

    public void revert() {
        tmpValue = value;
        textArea.setText(value);
    }
    
    @Override
    public TextProperty deepCopy() {
        return new TextProperty(this);
    }

    public String toString() {
        return "TextProperty["+getOrder()+","+getKey()+","+getName()+","+value+","+rows+","+cols+"]";
    }

    
    private int cols;
    private int rows;
    private JTextArea textArea = null;
    private boolean lineWrap = true;
    private boolean wordWrap = true;
}
