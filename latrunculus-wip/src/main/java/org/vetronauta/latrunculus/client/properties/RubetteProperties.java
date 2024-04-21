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

package org.vetronauta.latrunculus.client.properties;

import org.vetronauta.latrunculus.core.util.DeepCopyable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class RubetteProperties implements DeepCopyable<RubetteProperties> {

    private final HashMap<String,RubetteProperty> properties;

    public RubetteProperties() {
        properties = new HashMap<>();
    }
    
    public void put(RubetteProperty prop) {
        properties.put(prop.getKey(), prop);
    }

    public RubetteProperty get(String key) {
        return properties.get(key);
    }

    public Collection<RubetteProperty> getProperties() {
        return properties.values();
    }

    public void apply() {
        for (RubetteProperty prop : properties.values()) {
            prop.apply();
        }
    }

    public void revert() {
        for (RubetteProperty prop : properties.values()) {
            prop.revert();
        }
    }

    public RubetteProperties deepCopy() {
        RubetteProperties newProp = new RubetteProperties();
        for (Entry<String,RubetteProperty> entry : properties.entrySet()) {
            newProp.properties.put(entry.getKey(), entry.getValue().deepCopy());
        }
        return newProp;
    }
    

    public String toString() {
        StringBuilder s;
        s = new StringBuilder("------------------------------------------------\n");
        for (RubetteProperty prop : properties.values()) {
            s.append(prop).append("\n");
        }
        s.append("------------------------------------------------\n");
        return s.toString();
    }

}
