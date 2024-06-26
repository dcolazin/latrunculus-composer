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

import org.rubato.composer.components.JSelectForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.repository.Repository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormClientProperty extends ClientPluginProperty<Form> implements ActionListener {

    private JSelectForm selectForm;

    public FormClientProperty(String key, String name, Form value) {
        super(key, name, value);
    }
    
    public FormClientProperty(FormClientProperty prop) {
        super(prop.pluginProperty);
    }

    public JComponent getJComponent() {
        selectForm = new JSelectForm(Repository.systemRepository());
        selectForm.disableBorder();
        selectForm.addActionListener(this);
        selectForm.setForm(pluginProperty.getValue());
        return selectForm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pluginProperty.setTmpValue(selectForm.getForm());
    }

    @Override
    public void revert() {
        pluginProperty.revert(selectForm::setForm);
    }
    
    @Override
    public FormClientProperty deepCopy() {
        return new FormClientProperty(this);
    }
    
}
