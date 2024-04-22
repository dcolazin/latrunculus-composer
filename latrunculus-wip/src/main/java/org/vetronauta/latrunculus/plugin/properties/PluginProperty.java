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

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.vetronauta.latrunculus.core.util.DeepCopyable;

import javax.swing.*;

@Getter
@Setter
public abstract class PluginProperty<T> implements Comparable<PluginProperty<?>>, DeepCopyable<PluginProperty<T>> {

    private static int sequence = 0;

    private final int order;
    private final String key;
    private final String name;

    protected T value;
    protected T tmpValue;

    protected PluginProperty(String key, String name, T value) {
        this.key = key;
        this.name = name;
        this.value = value;
        this.tmpValue = value;
        this.order = sequence++;
    }
    
    protected PluginProperty(PluginProperty<T> prop) {
        this.key = prop.key;
        this.name = prop.name;
        this.value = prop.value;
        this.tmpValue = prop.tmpValue;
        this.order = prop.order;
    }

    @Override
    public int compareTo(PluginProperty obj) {
        return order - obj.order;
    }

    public void setValue(T newValue) {
        if (newValue != null) {
            internalSet(newValue);
        }
    }

    protected void internalSet(@NonNull T newValue) {
        this.value = newValue;
        this.tmpValue = newValue;
    }
    
    public abstract JComponent getJComponent();
    
    public void apply() {
        setValue(tmpValue);
    }
    
    public abstract void revert();

}
