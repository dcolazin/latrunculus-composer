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

package org.vetronauta.latrunculus.client.plugin.properties;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class TextClientProperty extends ClientPluginProperty<String> implements CaretListener {

    private final int cols;
    private final int rows;
    private JTextArea textArea = null;
    private boolean lineWrap = true;
    private boolean wordWrap = true;

    public TextClientProperty(String key, String name, String value, int cols, int rows, boolean lineWrap, boolean wordWrap) {
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

    public TextClientProperty(String key, String name, String value) {
        this(key, name, value, 40, 5, true, true);
    }

    public TextClientProperty(TextClientProperty prop) {
        super(prop.pluginProperty);
        this.cols = prop.cols;
        this.rows = prop.rows;
    }

    @Override
    public JComponent getJComponent() {
        textArea = new JTextArea(rows, cols);
        textArea.setText(pluginProperty.getValue());
        textArea.addCaretListener(this);
        textArea.setEditable(true);
        textArea.setLineWrap(lineWrap);
        textArea.setWrapStyleWord(wordWrap);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }
    
    @Override
    public void caretUpdate(CaretEvent e) {
        update();
    }

    private void update() {
        pluginProperty.setTmpValue(textArea.getText());
    }

    public void revert() {
        pluginProperty.revert(textArea::setText);
    }
    
    @Override
    public TextClientProperty deepCopy() {
        return new TextClientProperty(this);
    }

    public String toString() {
        return "TextProperty["+pluginProperty.getOrder()+","+pluginProperty.getKey()+","+pluginProperty.getName()+","+pluginProperty.getValue()+","+rows+","+cols+"]";
    }

}
