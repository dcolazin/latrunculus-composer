/*
 * Copyright (C) 2001 Gérard Milmeister
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

package org.rubato.base;

import org.rubato.util.TextUtils;
import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;


/**
 * Base exception for all Rubato code.
 */
public class RubatoException extends LatrunculusCheckedException {

    //TODO remove...

    public RubatoException() { super(); }

    public RubatoException(String msg) { super(msg); }

    public RubatoException(String msg, Object ... objects) {
        super(TextUtils.replaceStrings(msg, objects));
    }

    private static final long serialVersionUID = 8095298542192044628L;
}
