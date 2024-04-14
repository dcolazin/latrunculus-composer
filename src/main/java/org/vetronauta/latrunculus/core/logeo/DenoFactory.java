/*
 * Copyright (C) 2002, 2005 Gérard Milmeister
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
import org.vetronauta.latrunculus.core.exception.DomainException;
import org.vetronauta.latrunculus.core.math.element.generic.StringMap;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.element.impl.Modulus;
import org.vetronauta.latrunculus.core.math.element.impl.Rational;
import org.vetronauta.latrunculus.core.math.element.impl.Real;
import org.vetronauta.latrunculus.core.math.element.impl.ZInteger;
import org.vetronauta.latrunculus.core.math.element.generic.ModuleElement;
import org.vetronauta.latrunculus.core.math.module.impl.ZRing;
import org.vetronauta.latrunculus.core.math.morphism.ModuleMorphism;
import org.vetronauta.latrunculus.core.math.yoneda.FormDenotatorTypeEnum;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ColimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.LimitDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.ListDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.NameDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.PowerDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.SimpleDenotator;
import org.vetronauta.latrunculus.core.math.yoneda.diagram.FormDiagram;
import org.vetronauta.latrunculus.core.math.yoneda.form.ColimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.core.math.yoneda.form.LimitForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.ListForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.PowerForm;
import org.vetronauta.latrunculus.core.math.yoneda.form.SimpleForm;
import org.vetronauta.latrunculus.core.math.yoneda.map.ListMorphismMap;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Static utility class for building denotators.
 *
 * @author Gérard Milmeister
 */
public final class DenoFactory {

    // Build simple denotators.

    /**
     * Builds a simple denotator consisting of an RElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param d the real number for the RElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(NameDenotator name, Form form, double d) {
        try {
            return new SimpleDenotator(name, (SimpleForm)form, new Real((d)));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of an RElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param d the real number for the RElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(String name, Form form, double d) {
        try {
            return new SimpleDenotator(NameDenotator.make(name), (SimpleForm)form, new Real((d)));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds an anonymous simple denotator consisting of an RElement.
     * @param form the form of the new denotator
     * @param d the real number for the RElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(Form form, double d) {
        try {
            return new SimpleDenotator(null, (SimpleForm)form, new Real((d)));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of a ZElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param i the integer for the ZElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(NameDenotator name, Form form, int i) {
        try {
            return new SimpleDenotator(name, (SimpleForm)form, new ZInteger(i));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of a ZElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param i the integer for the ZElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(String name, Form form, int i) {
        try {
            return new SimpleDenotator(NameDenotator.make(name), (SimpleForm)form, new ZInteger(i));
        } 
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds an anonymous simple denotator consisting of a ZElement.
     * @param form the form of the new denotator
     * @param i the integer for the ZElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(Form form, int i) {
        try {
            return new SimpleDenotator(null, (SimpleForm)form, new ZInteger(i));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of a ZnElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param i the modular integer for the ZnElement
     * @param p the modulus for the ZnElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(NameDenotator name, Form form, int i, int p) {
        try {
            return new SimpleDenotator(name, (SimpleForm)form, new Modulus(i, p));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Build a simple denotator consisting of a ZnElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param i the modular integer for the ZnElement
     * @param p the modulus for the ZnElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(String name, Form form, int i, int p) {
        try {
            return new SimpleDenotator(NameDenotator.make(name), (SimpleForm)form, new Modulus(i, p));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds an anonymous simple denotator consisting of a ZnElement.
     * @param form the form of the new denotator
     * @param i the modular integer for the ZnElement
     * @param p the modulus for the ZnElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(Form form, int i, int p) {
        try {
            return new SimpleDenotator(null, (SimpleForm)form, new Modulus(i, p));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of a QElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param r the rational number for the QElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(NameDenotator name, Form form, Rational r) {
        try {
            return new SimpleDenotator(name, (SimpleForm)form, r);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of a QElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param r the rational number for the QElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(String name, Form form, Rational r) {
        try {
            return new SimpleDenotator(NameDenotator.make(name), (SimpleForm)form, r);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds an anonymous simple denotator consisting of a QElement.
     * @param form the form of the new denotator
     * @param r the rational number for the QElement
     */
    public static SimpleDenotator makeDenotator(Form form, Rational r) {
        try {
            return new SimpleDenotator(null, (SimpleForm)form, r);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of an CElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param c the complex number for the CElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(String name, Form form, Complex c) {
        try {
            return new SimpleDenotator(NameDenotator.make(name), (SimpleForm)form, c);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of a ZStringElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param s the rational number for the ZStringElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(NameDenotator name, Form form, String s) {
        try {
            return new SimpleDenotator(name, (SimpleForm)form, new StringMap<>(ZRing.ring, s));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator consisting of a ZStringElement.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param s the rational number for the ZStringElement
     * @return null if unsuccessful
     */
    public static SimpleDenotator makeDenotator(String name, Form form, String s) {
        try {
            return new SimpleDenotator(NameDenotator.make(name), (SimpleForm)form, new StringMap<>(ZRing.ring, s));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds an anonymous simple denotator consisting of a ZStringElement.
     * @param form the form of the new denotator
     * @param s the rational number for the ZStringElement
     */
    public static SimpleDenotator makeDenotator(Form form, String s) {
        try {
            return new SimpleDenotator(null, (SimpleForm)form, new StringMap<>(ZRing.ring, s));
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator containing the given module element.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param element the module element
     */
    public static SimpleDenotator makeDenotator(NameDenotator name, Form form, ModuleElement element) {
        try {
            return new SimpleDenotator(name, (SimpleForm)form, element);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator containing the given module element.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param element the module element
     */
    public static SimpleDenotator makeDenotator(String name, Form form, ModuleElement element) {        
        try {
            return new SimpleDenotator(NameDenotator.make(name), (SimpleForm)form, element);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator containing the given module element.
     * @param form the form of the new denotator
     * @param element the module element
     */
    public static SimpleDenotator makeDenotator(Form form, ModuleElement element) {
        try {
            return new SimpleDenotator(null, (SimpleForm)form, element);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator containing the given module morphism.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param morphism the module morphism
     */
    public static SimpleDenotator makeDenotator(NameDenotator name, Form form, ModuleMorphism morphism) {
        try {
            return new SimpleDenotator(name, (SimpleForm)form, morphism);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator containing the given module morphism.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param morphism the module morphism
     */
    public static SimpleDenotator makeDenotator(String name, Form form, ModuleMorphism morphism) {        
        try {
            return new SimpleDenotator(NameDenotator.make(name), (SimpleForm)form, morphism);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a simple denotator containing the given module morphism.
     * @param form the form of the new denotator
     * @param morphism the module morphism
     */
    public static SimpleDenotator makeDenotator(Form form, ModuleMorphism morphism) {
        try {
            return new SimpleDenotator(null, (SimpleForm)form, morphism);
        }
        catch (ClassCastException | DomainException e) {
            return null;
        }
    }

    /**
     * Builds a general denotator.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param denoList the coordinate list of the new denotator
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(NameDenotator name, Form form, List<Denotator> denoList) {
        try {
            switch (form.getType()) {
            case LIST: {
                return new ListDenotator(name, (ListForm)form, denoList);
            }
            case POWER: {
                return new PowerDenotator(name, (PowerForm)form, denoList);
            }
            case LIMIT: {
                return new LimitDenotator(name, (LimitForm)form, denoList);
            }
            default: {
                return null;
            }
            }
        }
        catch (RubatoException e) {
            return null;
        }
    }

    
    /**
     * Builds a general denotator.
     * @param name the name string of the new denotator
     * @param form the form of the new denotator
     * @param denotators the coordinate list of the new denotator
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(NameDenotator name, Form form, Denotator ... denotators) {
        LinkedList<Denotator> denoList = new LinkedList<>(Arrays.asList(denotators));
        return makeDenotator(name, form, denoList);
    }
    

    /**
     * Builds a general denotator.
     * @param name the name string of the new denotator
     * @param form the form of the new denotator
     * @param denoList the coordinate list of the new denotator
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(String name, Form form, List<Denotator> denoList) {
        return makeDenotator(NameDenotator.make(name), form, denoList);
    }
    

    /**
     * Builds a general denotator.
     * @param name the name string of the new denotator
     * @param form the form of the new denotator
     * @param denotators array of coordinate denotators
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(String name, Form form, Denotator ... denotators) {
        LinkedList<Denotator> denoList = new LinkedList<>(Arrays.asList(denotators));
        return makeDenotator(name, form, denoList);
    }
    

    /**
     * Builds a general anonymous denotator.
     * @param form the form of the new denotator
     * @param denoList the coordinate list of the new denotator
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(Form form, List<Denotator> denoList) {
        return makeDenotator((NameDenotator)null, form, denoList);
    }

    
    /**
     * Builds a general anonymous denotator.
     * @param form the form of the new denotator
     * @param denotators array of coordinate denotators
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(Form form, Denotator ... denotators) {
        return makeDenotator((NameDenotator)null, form, denotators);
    }

    
    /**
     * Builds a colimit denotator.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param i the index of the new denotator
     * @param d the coordinate denotator
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(NameDenotator name, Form form, int i, Denotator d) {
        if (form instanceof ColimitForm) {
            try {
                return new ColimitDenotator(name, (ColimitForm)form, i, d);
            }
            catch (RubatoException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    
    /**
     * Builds a colimit denotator.
     * @param name the name of the new denotator
     * @param form the form of the new denotator
     * @param i the index of the new denotator
     * @param d the coordinate denotator
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(String name, Form form, int i, Denotator d) {
        return makeDenotator(NameDenotator.make(name), form, i, d);
    }

    
    /**
     * Builds an anonymous colimit denotator.
     * @param form the form of the new denotator
     * @param i the index of the new denotator
     * @param d the coordinate denotator
     * @return null if unsuccessful
     */
    public static Denotator makeDenotator(Form form, int i, Denotator d) {
        return makeDenotator((NameDenotator)null, form, i, d);
    }


    /**
     * For forms of type list or power, extend the coordinate list.
     * @param d the denotator whose coordinate list is extended
     * @param denotators array of the coordinate denotator that are added
     * @throws RubatoException
     */
    public static void extendCoordinate(Denotator d, Denotator ... denotators)
            throws RubatoException {
        Form form = d.getForm();
        switch (form.getType()) {
            case POWER :
            case LIST : {
                FormDiagram dia = (FormDiagram)d.getForm().getIdentifier().getCodomainDiagram();
                ListMorphismMap map = (ListMorphismMap)d.getCoordinate().getMap();
                for (Denotator deno : denotators) {
                    if (!dia.getForm(0).equals(deno.getForm())) {
                        throw new RubatoException("DenoFactory.extendCoordinate: Expected "+
                                                  "form %1, but got form ", dia.getForm(0), deno.getForm());
                    }
                    map.appendFactor(deno);
                }
                if (form.getType() == FormDenotatorTypeEnum.POWER) {
                    map.sort();
                    map.removeDuplicates();
                }
                break;
            }
            default :
                throw new RubatoException("DenoFactory.extendCoordinate: Cannot extend denotator of type " + form.getType());
        }
    }
    
    
    private DenoFactory() { /* pure static class */ }
}
