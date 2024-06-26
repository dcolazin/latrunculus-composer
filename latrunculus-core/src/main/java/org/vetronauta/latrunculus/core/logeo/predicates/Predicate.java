/*
 * Copyright (C) 2002 Gérard Milmeister
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

package org.vetronauta.latrunculus.core.logeo.predicates;

import java.util.Comparator;

import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;


/**
 * This is the general interface for predicates with an arbitrary
 * number of arguments.
 * Arguments are always denotators.
 * To implement a predicate, extend the AbstractPredicate class.
 *
 * @author Gérard Milmeister
 */
public interface Predicate extends Comparator<Predicate> {

    /**
     * Calls the predicate with one argument.
     * The predicate must have arity = 1.
     */
    boolean call(Denotator ... denotators) throws LatrunculusCheckedException;

    /**
     * Returns a predicate that is the conjuction of this and <code>p</code>.
     * Both predicates must have same arity.
     */
    Predicate and(Predicate p) throws LatrunculusCheckedException;
    
    /**
     * Returns a predicate that is the disjunction of this and <code>p</code>.
     * Both predicates must have same arity.
     */
    Predicate or(Predicate p) throws LatrunculusCheckedException;
    
    /**
     * Returns a predicate that is the negation of this.
     */    
    Predicate negated();

    /**
     * Returns the arity of the predicate.
     */
    int getArity();


    /**
     * Returns the <code>i</code>th argument form.
     */
    Form getInputForm(int i);
    

    /**
     * Returns the name of the predicate.
     */
    String getName();

    /**
     * Returns true if p is compatible to this.
     * Two predicates are compatible if both have the same
     * arity and argument forms.
     */
    boolean isCompatible(Predicate p);

}
