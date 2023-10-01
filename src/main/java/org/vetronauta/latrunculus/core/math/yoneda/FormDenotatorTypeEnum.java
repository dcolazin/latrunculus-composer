/*
 * latrunculus-composer
 * Copyright (C) 2023 vetronauta
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.vetronauta.latrunculus.core.math.yoneda;

/**
 * @author vetronauta
 */
public enum FormDenotatorTypeEnum {

    SIMPLE, LIMIT, COLIMIT, LIST, POWER;

    public static Class<? extends Denotator> denotatorClassOf(String type) {
        if (type == null) {
            return null;
        }
        return denotatorClassOf(FormDenotatorTypeEnum.valueOf(type.toUpperCase()));
    }

    public static Class<? extends Denotator> denotatorClassOf(FormDenotatorTypeEnum type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case SIMPLE: return SimpleDenotator.class;
            case LIMIT: return LimitDenotator.class;
            case COLIMIT: return ColimitDenotator.class;
            case LIST: return ListDenotator.class;
            case POWER: return PowerDenotator.class;
        }
        return null;
    }

    public static Class<? extends Form> formClassOf(String type) {
        if (type == null) {
            return null;
        }
        return formClassOf(FormDenotatorTypeEnum.valueOf(type.toUpperCase()));
    }

    public static Class<? extends Form> formClassOf(FormDenotatorTypeEnum type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case SIMPLE: return SimpleForm.class;
            case LIMIT: return LimitForm.class;
            case COLIMIT: return ColimitForm.class;
            case LIST: return ListForm.class;
            case POWER: return PowerForm.class;
        }
        return null;
    }

}
