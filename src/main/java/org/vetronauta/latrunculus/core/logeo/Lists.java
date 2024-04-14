/*
 * Copyright (C) 2002, 2006 Gérard Milmeister
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

import org.vetronauta.latrunculus.core.exception.RubatoException;
import org.vetronauta.latrunculus.core.logeo.predicates.Predicate;
import org.vetronauta.latrunculus.core.math.module.generic.Module;
import org.vetronauta.latrunculus.core.math.yoneda.Address;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.map.ListMorphismMap;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


/**
 * This class implements various operations on denotators of type list.
 * Arguments to these methods must not be null.
 * 
 * @author Gérard Milmeister
 */
public final class Lists {

    /**
     * Returns a list denotator that is the
     * concatenation of <code>d1</code> and <code>d2</code>.
     * @throws RubatoException if <code>d1</code> or <code>d2</code>
     *                         is not of the required form
     */
    public static ListDenotator concat(ListDenotator d1, ListDenotator d2)
            throws RubatoException {
        Form form = processArguments(d1, d2);
        if (form == null) {
            throw new RubatoException("Lists.concat: "+d1+" and "+d2+" do not have the same base form");
        }

        List<Denotator> factors1 = d1.getFactors();
        List<Denotator> factors2 = d2.getFactors();
        if (!d1.getAddress().equals(d2.getAddress())) {
            Module newAddress = Address.getCommonModule(d1.getAddress(), d2.getAddress());
            if (newAddress == null) {
                throw new RubatoException("Lists.concat: Could not find a common address for "+d1+" and "+d2);
            }
            factors1 = readdress(factors1, newAddress);
            factors2 = readdress(factors2, newAddress);
            List<Denotator> factors = new LinkedList<Denotator>(factors1);
            factors.addAll(factors2);
            return new ListDenotator(null, d1.getListForm(), factors);        
        }
        else {
            List<Denotator> factors = new LinkedList<Denotator>(factors1);
            factors.addAll(factors2);
            return ListDenotator._make_unsafe(null, d1.getAddress(), d1.getListForm(), factors);
        }
    }

    
    /**
     * Returns a list denotator that is the concatenation
     * of the argument list denotators.
     * @throws RubatoException if the denotators do not have the required form
     */
    public static ListDenotator concat(ListDenotator ... denoList)
            throws RubatoException {
        if (denoList.length == 0) {
            return null;
        }
        ListDenotator result = denoList[0];
        for (int i = 1; i < denoList.length; i++) {
            result = concat(result, denoList[i]);
        }
        return result;
    }

    
    /**
     * Appends the elements of <code>d2</code> to <code>d1</code>.
     * This operation is destructive on the first argument.
     * @throws RubatoException if <code>d1</code> or <code>d2</code>
     *                         is not of the required form, or if they
     *                         do not have the same address
     */
    public static void appendTo(ListDenotator d1, ListDenotator d2)
            throws RubatoException {
        Form form = processArguments(d1, d2);
        if (form == null) {
            // different base forms
            throw new RubatoException("Lists.appendTo: "+d1+" and "+d2+" do not have the same base form");
        }
        else if (d1.getAddress().equals(d2.getAddress())) {
            // different addresses
            throw new RubatoException("Lists.appendTo: "+d1+" and "+d2+" do not have the same address");
        }
        else {
            ListMorphismMap m1 = (ListMorphismMap)d1.getCoordinate().getMap();
            ListMorphismMap m2 = (ListMorphismMap)d2.getCoordinate().getMap();
            for (int i = 0; i < m2.getFactorCount(); i++) {
                m1.appendFactor(m2.getFactor(i));
            }
        }
    }

    
    /**
     * Appends an element denotator to a list denotator.
     */
    public static ListDenotator appendElement(ListDenotator d, Denotator element)
            throws RubatoException {
        ListForm form = d.getListForm();
        
        if (!form.getForm().equals(element.getForm())) {
            throw new RubatoException("Lists.appendElement: Expected form "+
                                      "%1, but got form %2", form.getForm(), element.getForm());            
        }
        
        List<Denotator> factors = d.getFactors();
        Denotator factor = element;
        if (!d.getAddress().equals(element.getAddress())) {
            Module newAddress = Address.getCommonModule(d.getAddress(), element.getAddress());
            if (newAddress == null) {
                throw new RubatoException("List.appendElement: Could not find a common address for "+d+" and "+element);
            }
            factors = readdress(factors, newAddress);
            factor = factor.changeAddress(newAddress);
            factors.add(factor);
            return new ListDenotator(null, d.getListForm(), factors);        
        }
        else {
            factors.add(factor);
            return ListDenotator._make_unsafe(null, d.getAddress(), form, factors);
        }
    }
    
    /**
     * Prepend the elements of <i>d2</i> to <i>d1</i>.
     * This operation is destructive on the first argument.
     * @throws RubatoException if d1 or d2 is not of the required form
     */
    public static void prependTo(ListDenotator d1, ListDenotator d2)
            throws RubatoException {
        Form form = processArguments(d1, d2);

        if (form == null) {
            throw new RubatoException("Lists.prepentTo: "+d1+" and "+d2+" have not the same factor form");
        }

        ListMorphismMap m1 = (ListMorphismMap)d1.getCoordinate().getMap();
        ListMorphismMap m2 = (ListMorphismMap)d2.getCoordinate().getMap();

        for (int i = 0; i < m2.getFactorCount(); i++) {
            m1.prependFactor(m2.getFactor(i));
        }
    }

    
    /**
     * Prepends a denotator to a list denotator.
     */
    public static ListDenotator prependElement(ListDenotator d, Denotator element)
            throws RubatoException {
        ListForm form = d.getListForm();
        
        if (!form.getForm().equals(element.getForm())) {
            throw new RubatoException("Lists.prependElement: Expected element of form "+
                                      "%1, but got %2", form.getForm(), element.getForm());            
        }
        
        List<Denotator> factors = d.getFactors();
        Denotator factor = element;
        if (!d.getAddress().equals(element.getAddress())) {
            Module newAddress = Address.getCommonModule(d.getAddress(), element.getAddress());
            if (newAddress == null) {
                throw new RubatoException("Lists.prepentElement: Could not find a common address for "+d+" and "+element);
            }
            factors = readdress(factors, newAddress);
            factor = factor.changeAddress(newAddress);
            factors.add(0, factor);
            return new ListDenotator(null, d.getListForm(), factors);        
        }
        else {
            factors.add(0, factor);
            return ListDenotator._make_unsafe(null, d.getAddress(), form, factors);
        }
    }

    /**
     * Returns a denotator, where only the elements from the argument denotator
     * are contained that satisfy predicate p.
     * @param p the predicate that the elements must satisfy, must have arity 1
     * @throws RubatoException if <code>d</code> has not the required form or
     *                                  <code>p</code> has arity != 1
     */
    public static ListDenotator select(Predicate p, ListDenotator d)
            throws RubatoException {
        ListForm form = d.getListForm();
   
        if (p.getArity() != 1) {
            throw new RubatoException("Lists.select: Expected arity "+
                                      "1, but got %1", p.getArity());
        }

        ListMorphismMap map = (ListMorphismMap)d.getCoordinate().getMap();
        LinkedList<Denotator> denoList = new LinkedList<Denotator>();

        boolean changed = false;
        for (int i = 0; i < map.getFactorCount(); i++) {
            Denotator deno = map.getFactor(i);
            if (p.call(deno)) {
                denoList.add(deno);
            }
            else {
                changed = true;
            }
        }

        if (!changed) {
            // select returns the whole denotator
            return d;
        }

        return new ListDenotator(null, form, denoList);        
    }

    /**
     * Sorts the list denotator according to canonical order.
     */
    public static ListDenotator sort(ListDenotator d) {
        List<Denotator> denoList = d.getFactors();
        Collections.sort(denoList);
        return ListDenotator._make_unsafe(null, d.getAddress(), d.getListForm(), denoList);
    }
    
    
    /**
     * Sorts the list denotator according to order induced by
     * the comparator <code>c</code>, which can also be a Predicate.
     */
    public static ListDenotator sort(ListDenotator d, Comparator<Denotator> c) {
        List<Denotator> denoList = d.getFactors();
        Collections.sort(denoList, c);
        return ListDenotator._make_unsafe(null, d.getAddress(), d.getListForm(), denoList);
    }


    static private Form processArguments(ListDenotator d1, ListDenotator d2) {
        Form form1 = d1.getListForm().getForm();
        Form form2 = d2.getListForm().getForm();
        if (!form1.equals(form2)) {
            return null;
        }
        else {
            return form1;
        }
    }
    
    
    static private List<Denotator> readdress(List<Denotator> denotators, Module address) {
        List<Denotator> res = new LinkedList<Denotator>();
        for (Denotator d : denotators) {
            res.add(d.changeAddress(address));
        }
        return res;
    }

    
    private Lists() { /* not allowed */ }
}
