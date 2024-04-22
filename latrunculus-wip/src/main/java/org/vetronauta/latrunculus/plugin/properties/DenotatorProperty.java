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

import org.rubato.composer.components.JSelectDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.repository.Repository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DenotatorProperty extends PluginProperty<Denotator> implements ActionListener {

    private JSelectDenotator selectDenotator;

    private static Repository rep = Repository.systemRepository();

    public DenotatorProperty(String key, String name, Denotator value) {
        super(key, name, value);
    }

    public DenotatorProperty(DenotatorProperty prop) {
        super(prop);
    }

    public JComponent getJComponent() {
        selectDenotator = new JSelectDenotator(rep);
        selectDenotator.disableBorder();
        selectDenotator.addActionListener(this);
        selectDenotator.setDenotator(value);
        return selectDenotator;
    }
    
    public void actionPerformed(ActionEvent e) {
        tmpValue = selectDenotator.getDenotator();
    }

    public void revert() {
        tmpValue = value;
        selectDenotator.setDenotator(tmpValue);
    }
    
    @Override
    public DenotatorProperty deepCopy() {
        return new DenotatorProperty(this);
    }

    public String toString() {
        return "DenotatorProperty["+getOrder()+","+getKey()+","+getName()+","+value+"]";
    }

}
