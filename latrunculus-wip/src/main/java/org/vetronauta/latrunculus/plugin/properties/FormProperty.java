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

import org.rubato.composer.components.JSelectForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.repository.Repository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormProperty extends PluginProperty<Form> implements ActionListener {

    public FormProperty(String key, String name, Form value) {
        super(key, name, value);
    }
    
    public FormProperty(FormProperty prop) {
        super(prop);
    }

    public JComponent getJComponent() {
        selectForm = new JSelectForm(rep);
        selectForm.disableBorder();
        selectForm.addActionListener(this);
        selectForm.setForm(value);
        return selectForm;
    }

    
    public void actionPerformed(ActionEvent e) {
        tmpValue = selectForm.getForm();
    }

    public void revert() {
        tmpValue = value;
        selectForm.setForm(tmpValue);
    }
    
    @Override
    public FormProperty deepCopy() {
        return new FormProperty(this);
    }

    public String toString() {
        return "FormProperty["+getOrder()+","+getKey()+","+getName()+","+value+"]";
    }

    private JSelectForm selectForm;
    
    private static Repository rep = Repository.systemRepository();
}
