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

package org.vetronauta.latrunculus.core.scheme;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This is the abstract base class for all types of Scheme values,
 * e.g., numbers, symbols, conses, closures, etc.
 * 
 * @author Gérard Milmeister
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SExpr {

    public abstract SType type();
    
    /**
     * Returns the cons of <code>car</code> and <code>cdr</code>.
     */
    public static SCons cons(SExpr car, SExpr cdr) { return new SCons(car, cdr); }

    /**
     * Returns the car of <code>sexpr</code>, which must be an SCons.
     */
    //TODO remove
    public static SExpr car(SExpr sexpr) { return sexpr.getCar(); }

    /**
     * Returns the car of <code>sexpr</code>, which must be an SCons.
     */
    //TODO remove
    public static SExpr cdr(SExpr sexpr) { return sexpr.getCdr(); }

    /**
     * Returns true iff this is equal to <code>sexpr</code> in the eq? sense.
     */
    public abstract boolean eq_p(SExpr sexpr);

    /**
     * Returns true iff this is equal to <code>sexpr</code> in the eqv? sense.
     */
    public abstract boolean eqv_p(SExpr sexpr);

    /**
     * Returns true iff this is equal to <code>sexpr</code> in the equal? sense.
     */
    public abstract boolean equal_p(SExpr sexpr);
    
    /**
     * Returns true iff this is equal to <code>obj</code>. Normally this
     * returns the same result as {@link #equal_p}.
     */
    public abstract boolean equals(Object obj);

    /**
     * Returns a string representation of this SExpr.
     */
    public abstract String toString();

    /**
     * Returns a string representation of this SExpr. This is the
     * representation intended to show the result of an evaluation,
     * and should be a valid Scheme expression if possible.
     */
    //TODO move to server
    public abstract String display();

    /**
     * Returns true iff this is a number.
     */
    public boolean isNumber() { return false; }

    /**
     * Returns true iff this is a list.
     */
    public boolean isList() { return type() == SType.CONS || type() == SType.NULL; }
    
    /**
     * Returns the car of this SExpr.
     * 
     * @return null if this is not a cons
     */
    //TODO evaluate if this should obly be a SCons method
    public SExpr getCar() { return null; }
    
    /**
     * Returns the cdr of this SExpr.
     * 
     * @return null if this is not a cons
     */
    //TODO evaluate if this should obly be a SCons method
    public SExpr getCdr() { return null; }
    
    /**
     * Sets the car of this SExpr. Does nothing if this is not a cons.
     */
    //TODO evaluate if this should obly be a SCons method
    public void setCar(SExpr sexpr) {}
    
    /**
     * Sets the cdr of this SExpr. Does nothing if this is not a cons.
     */
    //TODO evaluate if this should obly be a SCons method
    public void setCdr(SExpr sexpr) {}

    /**
     * Returns the length if this is a regular list, otherwise -1.
     */
    //TODO evaluate if this should obly be a SCons method
    public int getLength() { return -1; }
    
    /**
     * Returns the <code>i</code>-th element if this is a regular
     * list, otherwise null.
     */
    //TODO evaluate if this should obly be a SCons method
    public SExpr nth(int i) { return null; }

}
