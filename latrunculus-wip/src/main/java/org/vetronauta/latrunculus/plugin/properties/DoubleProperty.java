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

public class DoubleProperty extends PluginProperty<Double> {

    private final double min;
    private final double max;

    public DoubleProperty(String key, String name, double value, double min, double max) {
        super(key, name, value);
        if (min > max) {
            double t = min;
            min = max;
            max = t;
        }
        this.min = min;
        this.max = max;
    }

    public DoubleProperty(String key, String name, double value) {
        this(key, name, value, Double.MIN_VALUE, Double.MAX_VALUE);
    }
    
    public DoubleProperty(DoubleProperty prop) {
        super(prop);
        this.min = prop.min;
        this.max = prop.max;
    }

    public boolean isAcceptable(Double newValue) {
        return newValue != null && newValue >= min && newValue <= max;
    }

    @Override
    protected void internalSet(Double newValue) {
        if (newValue < min) {
            newValue = min;
        } else if (newValue > max) {
            newValue = max;
        }
        this.value = newValue;
        this.tmpValue = newValue;
    }
    
    @Override
    public DoubleProperty deepCopy() {
        return new DoubleProperty(this);
    }

    public String toString() {
        return "DoubleProperty["+getOrder()+","+getKey()+","+getName()+","+value+","+min+","+max+"]";
    }

}
