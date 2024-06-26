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

package org.vetronauta.latrunculus.core.logeo;

import java.util.LinkedList;
import java.util.List;

import org.vetronauta.latrunculus.core.exception.LatrunculusCheckedException;
import org.vetronauta.latrunculus.core.logeo.predicates.FormPredicate;
import org.vetronauta.latrunculus.core.logeo.predicates.Predicate;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;

/**
 * The select methods traverse a denotator and collect all parts of the
 * denotator that satisfy a predicate.
 * 
 * @author Gérard Milmeister
 */
public final class Select {

    /**
     * Returns the nodes from <code>d</code>
     * that satisfy predicate <code>p</code>.
     * @param p the predicate should be able to return a result for any
     *          form of denotator
     * @throws LatrunculusCheckedException
     */
    public static List<Denotator> select(Predicate p, Denotator d)
            throws LatrunculusCheckedException {
        LinkedList<Denotator> results = new LinkedList<Denotator>();
        select(p, d, results);
        return results;
    }


    /**
     * Returns the nodes of form <code>f<code> from <code>d</code>
     * that satisfy predicate <code>p</code>.
     * @param p the predicate needs only to return a result for an argument of
     *          form <code>f</code>
     * @throws LatrunculusCheckedException
     */
    public static List<Denotator> select(Form f, Predicate p, Denotator d)
            throws LatrunculusCheckedException {
        Predicate predicate = new FormPredicate(f).and(p);
        return select(predicate, d);
    }


    /**
     * Returns the nodes of form f from d.
     * @throws LatrunculusCheckedException
     */
    public static List<Denotator> select(Form f, Denotator d)
            throws LatrunculusCheckedException {
        Predicate predicate = new FormPredicate(f);
        return select(predicate, d);
    }
        

    private static void select(Predicate p, Denotator d, List<Denotator> denoList)
            throws LatrunculusCheckedException {        
        if (p.call(d)) {
            denoList.add(d);
        }
        else {
            for (Denotator deno : d) {
                if (deno != null) {
                    select(p, deno, denoList);
                }
            }
        }   
    }
    
    
    private Select() { /* not allowed */ }
}
