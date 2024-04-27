/*
 * Copyright (C) 2006 Gérard Milmeister
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

package org.vetronauta.latrunculus.plugin.base;

import lombok.Setter;
import org.vetronauta.latrunculus.client.plugin.properties.ClientPluginProperty;
import org.vetronauta.latrunculus.client.plugin.properties.ClientPropertiesFactory;
import org.vetronauta.latrunculus.client.plugin.properties.JRubettePropertiesDialog;
import org.vetronauta.latrunculus.client.plugin.properties.ClientPluginProperties;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;

import javax.swing.*;

public abstract class SimpleAbstractRubette extends AbstractRubette {

    //TODO separate the server component from the client one

    /**
     * Returns the rubette properties of this rubette.
     */
    public final ClientPluginProperties getRubetteProperties() {
        if (properties == null) {
            properties = new ClientPluginProperties();
        }
        return properties;
    }


    /**
     * Sets a new property.
     */
    public void putProperty(ClientPluginProperty<?> prop) {
        getRubetteProperties().put(prop);
    }

    public void putProperty(PluginProperty<?> prop) {
        getRubetteProperties().put(ClientPropertiesFactory.build(prop));
    }
    

    /**
     * Returns the property of the given <code>key</code>.
     * Returns null, iff no property with this key exists.
     */
    public ClientPluginProperty<?> getProperty(String key) {
        return getRubetteProperties().get(key);
    }

    
    public boolean hasProperties() {
        return properties != null;
    }

    
    public JComponent getProperties() {
        if (dialog == null) {
            dialog = new JRubettePropertiesDialog(properties);
        }
        return dialog;
    }

    
    public boolean applyProperties() {
        if (properties != null) {
            properties.apply();
        }
        return true;
    }
    
    
    public void revertProperties() {
        if (properties != null) {
            properties.revert();
        }
    }

    
    public Rubette duplicate() {
        SimpleAbstractRubette rubette = (SimpleAbstractRubette)newInstance();
        rubette.properties = properties.deepCopy();
        return rubette;
    }

    @Setter
    private ClientPluginProperties properties = null;
    private JRubettePropertiesDialog dialog = null;
}
