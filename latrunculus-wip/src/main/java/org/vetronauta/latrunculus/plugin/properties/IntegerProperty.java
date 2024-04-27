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

public class IntegerProperty extends PluginProperty<Integer> {

    private final int min;
    private final int max;

    public IntegerProperty(String key, String name, int value, int min, int max) {
        super(key, name, value);
        if (min > max) {
            int t = min;
            min = max;
            max = t;
        }
        this.min = min;
        this.max = max;
    }

    public IntegerProperty(String key, String name, int value) {
        this(key, name, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    public IntegerProperty(IntegerProperty prop) {
        super(prop);
        this.min = prop.min;
        this.max = prop.max;
    }

    @Override
    protected void internalSet(Integer value) {
        if (value < min) {
            value = min;
        }
        else if (value > max) {
            value = max;
        }
        this.value = value;
        this.tmpValue = value;
    }

    @Override
    public boolean isAcceptable(Integer i) {
        return i != null && i >= min && i <= max;
    }

    @Override
    public IntegerProperty deepCopy() {
        return new IntegerProperty(this);
    }

    @Override
    public String toString() {
        return "IntegerProperty["+getOrder()+","+getKey()+","+getName()+","+value+","+min+","+max+"]";
    }

}
