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
 * The class representing Scheme vector values.
 * 
 * @author Gérard Milmeister
 */
public final class SVector extends SExpr {

    /**
     * Creates a Scheme vector from the expression in the array <code>v</code>.
     */
    public SVector(SExpr[] v) {
        this.v = v;
    }


    @Override
    public SType type() {
        return SType.VECTOR;
    }

    public boolean eq_p(SExpr sexpr) {
        return this == sexpr;
    }
    
    
    public boolean eqv_p(SExpr sexpr) {
        if (sexpr instanceof SVector) {
            SVector a = (SVector)sexpr;
            if (v.length == a.v.length) {
                for (int i = 0; i < v.length; i++) {
                    if (!v[i].eqv_p(a.v[i])) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    
    
    public boolean equal_p(SExpr sexpr) {
        if (sexpr instanceof SVector) {
            SVector a = (SVector)sexpr;
            if (v.length == a.v.length) {
                for (int i = 0; i < v.length; i++) {
                    if (!v[i].equal_p(a.v[i])) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    
    
    public boolean equals(Object obj) {
        if (obj instanceof SVector) {
            SVector a = (SVector)obj;
            if (v.length == a.v.length) {
                for (int i = 0; i < v.length; i++) {
                    if (!v[i].equals(a.v[i])) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Returns the array in this Scheme vector.
     */
    public SExpr[] getArray() {
        return v;
    }
    
    
    private SExpr[] v;
}
