/*
 * Copyright (C) 2005 Gérard Milmeister
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

package org.vetronauta.latrunculus.plugin.base;

import lombok.extern.slf4j.Slf4j;
import org.vetronauta.latrunculus.server.exception.LatrunculusError;
import org.rubato.composer.rubette.Link;
import org.rubato.composer.rubette.RubetteModel;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.util.TextUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract base class for Rubettes.
 * Any implementation of a Rubette must derive from this class.
 * The methods are divided into four class:
 * <ol>
 * <li>Methods that must be implemented</li>
 * <li>Methods that can overridden, and provide default implementations</li>
 * <li>Methods that can be used, but cannot be overridden</li>
 * <li>Methods that are for internal use, and must not be used by an implementation</li>
 * </ol>
 * 
 * @author Gérard Milmeister
 */
@Slf4j
public abstract class AbstractRubette implements Rubette {

    private RubetteModel model;
    private Denotator[] output;
    private int inCount = 1;
    private int outCount = 1;
    private final List<String> errors = new LinkedList<>();

    // These methods can be overridden by a concrete Rubette

    /**
     * Returns a long description.
     * The long description is shown in the text area below the
     * Rubette list, if this Rubette is selected in the list.
     */
    public String getLongDescription() {
        return Messages.getString("AbstractRubette.nodescription")+getName()+".";
    }

    /**
     * The default constructor must/should perform initializations specfic to
     * each rubette instance, for example set the number of inputs and outputs, or
     * initializing the rubette state. 
     */
    protected AbstractRubette() { /* default does nothing */ }
    
    
    // These methods cannot be overridden but can be used by a concrete rubette. 

    /**
     * Creates a new instance from a protoype.
     */
    //TODO this should be final
    public Rubette newInstance() {
        try {
            return getClass().newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
            throw new LatrunculusError(e, "Unexpected internal error: consult the console log");
        }
    }

    
    /**
     * Sets the number of input connectors.
     * This method should be called with default value in the init() method.
     * It can be called afterwards if the number of connectors changes.
     */
    public final void setInCount(int n) {
        inCount = n;
    }
    

    /**
     * Returns the current number of input connectors.
     */
    public final int getInCount() {
        return inCount;
    }


    /**
     * Returns the input denotator at input connector number <code>i</code>. 
     * This is usually called at the beginning of the run() method
     * to get the input values. The run() method must check the
     * return value which may be null.
     */
    public final Denotator getInput(int i) {
        Link inLink = model.getInLink(i);
        if (inLink == null) {
            return null;
        }
        return inLink.getSrcModel().getRubette().getOutput(inLink.getSrcPos());
    }
    
    
    /**
     * Sets the number of output connectors.
     * This method should be called with default value in the init() method.
     * It can be called afterwards if the number of connectors changes.
     */
    public final void setOutCount(int n) {
        outCount = n;
        output = new Denotator[outCount];
        for (int j = 0; j < outCount; j++) {
            output[j] = null;
        }        
    }
    
    
    /**
     * Returns the current number of output connectors.
     */
    public final int getOutCount() {
        return outCount;
    }


    /**
     * Stores the output denotator <code>d</code> for output connector number <code>i</code>.
     * This is usually called at the end of the run() method to store
     * the result of the computation.
     */
    public final void setOutput(int i, Denotator d) {
        output[i] = d;
    }
    
    
    /**
     * Returns the output denotator of connectir number <code>i</code>.
     * This is usually called by the Runner.
     */
    public final Denotator getOutput(int i) {
        return output[i];
    }
    
    
    /**
     * Returns the RubetteModel, that this Rubette is attached to.
     * This may be useful to retrieve information about the
     * environment.
     */
    public final RubetteModel getModel() {
        return model;
    }
    
    
    /**
     * Adds an error string to the current error state.
     * If an error occurs in the run() method, this method
     * should be called with a short description of the error.
     */
    public final void addError(String msg, Object ... objects) {
        errors.add(TextUtils.replaceStrings(msg, objects));
    }


    /**
     * Adds an exception message to the current error state.
     * A period (".") is appended if there is none in the message.
     */
    public final void addError(Exception e) {
        String s = e.getMessage().trim();
        if (!s.substring(s.length()-1).equals(".")) { 
            s += "."; 
        }
        errors.add(s);
    }


    /**
     * Returns a list of the current errors.
     * This is called by the Runner. If no error has occurred, it
     * returns an empty list, but never null.
     */
    public final List<String> getErrors() {
        return errors;
    }


    /**
     * Removes all errors from the error list.
     * This is called by the Runner, before the run() method is
     * executed.
     */
    public final void clearErrors() {
        errors.clear();
    }


    /**
     * Returns true iff any error has occurred.
     * This is called by the Runner, after the run() method has
     * been executed.
     */
    public final boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    
    // These methods should not be used by a concrete Rubette.
    
    /**
     * Sets the model this Rubette is attached to.
     * This is called by the Composer, and should, in general,
     * never be used by a Rubette implementation.
     */
    //TODO is there a way to not have this public method?
    public final void setModel(RubetteModel model) {
        this.model = model;
    }

}
