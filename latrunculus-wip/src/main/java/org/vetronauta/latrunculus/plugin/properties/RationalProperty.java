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

import org.vetronauta.latrunculus.core.math.element.impl.Rational;

public class RationalProperty extends PluginProperty<Rational> {

    private final Rational min;
    private final Rational max;

    public RationalProperty(String key, String name, Rational value, Rational min, Rational max) {
        super(key, name, value);
        this.value = value;
        this.tmpValue = value;
        if (min.compareTo(max) > 0) {
            Rational t = min;
            min = max;
            max = t;
        }
        this.min = min;
        this.max = max;
    }

    public RationalProperty(String key, String name, Rational value) {
        this(key, name, value, new Rational(Integer.MIN_VALUE), new Rational(Integer.MAX_VALUE));
    }

    public RationalProperty(RationalProperty prop) {
        super(prop);
        this.min = prop.min;
        this.max = prop.max;
    }

    @Override
    protected void internalSet(Rational value) {
        if (value.compareTo(min) < 0) {
            value = min;
        }
        else if (value.compareTo(max) > 0) {
            value = max;
        }
        this.value = value;
        this.tmpValue = value;
    }

    @Override
    public boolean isAcceptable(Rational newValue) {
        return newValue != null && newValue.compareTo(min) >= 0 && newValue.compareTo(max) <= 0;
    }

    @Override
    public RationalProperty deepCopy() {
        return new RationalProperty(this);
    }

    @Override
    public String toString() {
        return "RationalProperty["+getOrder()+","+getKey()+","+getName()+","+value+","+min+","+max+"]";
    }

}
