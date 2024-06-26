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

package org.vetronauta.latrunculus.core.scheme.expression;


/**
 * The class representing Scheme string values.
 * 
 * @author Gérard Milmeister
 */
public final class SString extends SExpr {

    /**
     * Creates a Scheme string from the given string <code>s</code>.
     */
    public SString(String s) {
        this.s = s;
    }

    @Override
    public SType type() {
        return SType.STRING;
    }

    public boolean eq_p(SExpr sexpr) {
        return this == sexpr;
    }
    
    public boolean eqv_p(SExpr sexpr) {
        return this == sexpr;
    }
    
    public boolean equal_p(SExpr sexpr) {
        return (sexpr instanceof SString) && ((SString)sexpr).s.equals(s);
    }
    
    public boolean equals(Object obj) {
        return (obj instanceof SString) && ((SString)obj).s.equals(s);
    }

    /**
     * Returns the string in this Scheme value.
     */
    public String getString() {
        return s;
    }
    
    private String s;
}
