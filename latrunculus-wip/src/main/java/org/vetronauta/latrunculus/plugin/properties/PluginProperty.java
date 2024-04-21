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

import javax.swing.*;

public abstract class PluginProperty implements Comparable<PluginProperty>, DeepCopyable<PluginProperty> {

    protected PluginProperty(String key, String name) {
        this.key = key;
        this.name = name;
        this.order = sequence++;
    }
    
    
    protected PluginProperty(PluginProperty prop) {
        key = prop.key;
        name = prop.name;
        order = prop.order;
    }
    
    
    public String getKey() {
        return key;
    }
    
    
    public void setKey(String key) {
        this.key = key;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public int getOrder() {
        return order;
    }
    
    
    public int compareTo(PluginProperty obj) {
        return order-obj.order;
    }
    
    
    public abstract Object getValue(); //TODO generic
    
    public abstract void setValue(Object value);
    
    public abstract JComponent getJComponent();
    
    public abstract void apply();
    
    public abstract void revert();

    private String key;
    private String name;
    private int order;
    
    private static int sequence = 0;
}
