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

import org.vetronauta.latrunculus.core.exception.RubatoException;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;


/**
 * Several functions used for combining predicates.
 *
 * @author Gérard Milmeister
 */
public final class Predicates {

    /**
     * Returns a predicate that is the
     * conjuction of <code>p</code> and <code>q</code>.
     * Both predicates must have same arity.
     */
    public static Predicate and(Predicate p, Predicate q)
            throws RubatoException {
        return new Conjunction(p, q);
    }
    
    
    /**
     * Returns a predicate that is the
     * disjunction of <code>p</code> and <code>p</code>.
     * Both predicates must have same arity.
     */
    public static Predicate or(Predicate p, Predicate q)
            throws RubatoException {
        return new Disjunction(p, q);
    }
        

    /**
     * Returns a predicate that is the negation of <code>p</code>.
     */    
    public static Predicate not(Predicate p) {
        return new Negation(p);
    }


    //
    // private classes
    //

    private static class Conjunction extends AbstractPredicate {

        private final Predicate p;
        private final Predicate q;

        public Conjunction(Predicate p, Predicate q) throws RubatoException {
            if (!p.isCompatible(q)) {
                throw new RubatoException("Both predicates must be compatible");
            }
            this.p = p;
            this.q = q;
        }
        
        public boolean call(Denotator... denotators) throws RubatoException {
            return p.call(denotators) && q.call(denotators);
        }
        
        public int getArity() {
            return p.getArity();
        }
        
        public Form getInputForm(int i) {
            return p.getInputForm(i);
        }
        
        public String getName() {
            return "("+p.getName()+" && "+q.getName()+")";    
        }

    }

    private static class Disjunction extends AbstractPredicate {

        private final Predicate p;
        private final Predicate q;

        public Disjunction(Predicate p, Predicate q) throws RubatoException {
            if (!p.isCompatible(q)) {
                throw new RubatoException("Both predicates must be compatible.");
            }
            this.p = p;
            this.q = q;
        }
        
        public boolean call(Denotator... denotators) throws RubatoException {
            return p.call(denotators) || q.call(denotators);
        }
        
        public int getArity() {
            return p.getArity();
        }
        
        public Form getInputForm(int i) {
            return p.getInputForm(i);
        }

        public String getName() {
            return "("+p.getName()+" || "+q.getName()+")";    
        }

    }

    private static class Negation extends AbstractPredicate {

        private final Predicate p;

        public Negation(Predicate p) {
            this.p = p;
        }
        
        public boolean call(Denotator... denotators) throws RubatoException {
            return !p.call(denotators);
        }
        
        public int getArity() {
            return p.getArity();
        }
        
        public Form getInputForm(int i) {
            return p.getInputForm(i);
        }

        public String getName() {
            return "!"+p.getName();    
        }
    }

}
