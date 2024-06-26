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
 * The class representing Scheme character values.
 *  
 * @author Gérard Milmeister
 */
public final class SChar extends SExpr {

    /**
     * Creates a Scheme character from <code>c</code>.
     */
    public SChar(char c) {
        this.c = c;
    }


    @Override
    public SType type() {
        return SType.CHAR;
    }

    public boolean eq_p(SExpr sexpr) {
        return this == sexpr;
    }
    
    
    public boolean eqv_p(SExpr sexpr) {
        return (sexpr instanceof SChar) && (((SChar)sexpr).c == c);
    }
    
    
    public boolean equal_p(SExpr sexpr) {
        return (sexpr instanceof SChar) && (((SChar)sexpr).c == c);
    }
    
    
    public boolean equals(Object obj) {
        return (obj instanceof SChar) && (((SChar)obj).c == c);
    }

    /**
     * Returns the character in this Scheme value.
     */
    public char getChar() {
        return c;
    }
    
    
    private char c;
}
