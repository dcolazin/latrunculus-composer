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

package org.vetronauta.latrunculus.plugin.properties;

import org.vetronauta.latrunculus.core.util.DeepCopyable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class PluginProperties implements DeepCopyable<PluginProperties> {

    private final HashMap<String, PluginProperty<?>> properties;

    public PluginProperties() {
        properties = new HashMap<>();
    }
    
    public void put(PluginProperty<?> prop) {
        properties.put(prop.getKey(), prop);
    }

    public PluginProperty<?> get(String key) {
        return properties.get(key);
    }

    public Collection<PluginProperty<?>> getProperties() {
        return properties.values();
    }

    public void apply() {
        for (PluginProperty<?> prop : properties.values()) {
            prop.apply();
        }
    }

    public void revert() {
        for (PluginProperty<?> prop : properties.values()) {
            prop.revert();
        }
    }

    public PluginProperties deepCopy() {
        PluginProperties newProp = new PluginProperties();
        for (Entry<String, PluginProperty<?>> entry : properties.entrySet()) {
            newProp.properties.put(entry.getKey(), entry.getValue().deepCopy());
        }
        return newProp;
    }
    

    public String toString() {
        StringBuilder s;
        s = new StringBuilder("------------------------------------------------\n");
        for (PluginProperty<?> prop : properties.values()) {
            s.append(prop).append("\n");
        }
        s.append("------------------------------------------------\n");
        return s.toString();
    }

}
