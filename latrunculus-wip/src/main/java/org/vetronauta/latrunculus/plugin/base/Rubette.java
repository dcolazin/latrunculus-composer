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

import org.vetronauta.latrunculus.client.plugin.icons.Icons;
import org.vetronauta.latrunculus.client.plugin.icons.PluginIcons;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;

import javax.swing.*;
import java.util.List;

/**
 * Interface for Rubettes.
 * Any implementation of a Rubette should derive AbstractRubette.
 * @see AbstractRubette
 * 
 * @author Gérard Milmeister
 */

public interface Rubette extends Plugin {

    String INPUT_CONNECTOR_FORMAT = "Input #%d";
    String OUTPUT_CONNECTOR_FORMAT = "Output #%d";

    /**
     * Returns the group this Rubette belongs to.
     */
    default String getGroup() {
        return RubatoConstants.OTHER_GROUP;
    }
    
    /**
     * Returns the name of the Rubette.
     */
    String getName();

    /**
     * Creates a new instance from a protoype.
     */
    Rubette newInstance();

    /**
     * Creates a duplicate from this rubette instance.
     * All properties are copied if possible.
     */
    Rubette duplicate();

    /**
     * Returns an icon for this Rubette.
     */
    default ImageIcon getIcon() {
        ImageIcon icon = PluginIcons.get(this);
        return icon != null ? icon : Icons.emptyIcon;
    }

    /**
     * Returns true iff this Rubette has a properties dialog.
     * Properties reflect the configuration of this Rubette,
     * any changes in the properties dialog may affect the
     * computation.
     */
    default boolean hasProperties() {
        return false;
    }

    /**
     * Returns the Swing component for the properties dialog.
     * If this Rubette has no properties dialog, simply return null.
     */
    default JComponent getProperties() {
        return null;
    }

    /**
     * Makes changes in the properties dialog permanent.
     * @return true iff the values in the properties dialog are correct
     */
    default boolean applyProperties() {
        return true;
    }

    /**
     * Reverts values in the properties dialog to the values in the Rubette.
     */
    default void revertProperties() {
        //do nothing
    }

    /**
     * Returns true iff this Rubette has a view.
     * A view should provide a visual (or aural) representation of the
     * configuration (resp. values) of the Rubette, but must never
     * affect the computation.
     */
    default boolean hasView() {
        return false;
    }

    /**
     * Returns the Swing component for the view.
     * If this Rubette has no view, simply return null.
     */
    default JComponent getView() {
        return null;
    }

    /**
     * Updates the view reflecting the changes of the values in the Rubette.
     * If there is no view, do nothing.
     */
    default void updateView() {
        //do nothing
    }

    /**
     * Returns true iff this Rubette has an info label.
     * The info label is a short string that is displayed
     * in the JRubette.
     */
    default boolean hasInfo() {
        return false;
    }

    /**
     * Returns the info string for the info label.
     * If this Rubette has no info label, simply return null.
     */
    default String getInfo() {
        return null;
    }

    /**
     * Returns a short description.
     * The short description is shown as a tooltip over the JRubette.
     */
    default String getShortDescription() {
        return getName();
    }

    /**
     * Returns a long description.
     * The long description is shown in the text area below the
     * Rubette list, if this Rubette is selected in the list.
     */
    String getLongDescription();

    /**
     * Returns the tooltip for the input connector number <code>i</code>. 
     */
    default String getInTip(int i) {
        return defaultInTip(i);
    }

    static String defaultInTip(int i) {
        return String.format(INPUT_CONNECTOR_FORMAT, i);
    }

    /**
     * Returns the tooltip for the output connector number <code>i</code>. 
     */
    default String getOutTip(int i) {
        return defaultOutTip(i);
    }

    static String defaultOutTip(int i) {
        return String.format(OUTPUT_CONNECTOR_FORMAT, i);
    }

    /**
     * Sets the number of input connectors.
     * This method should be called with default value in the init() method.
     * It can be called afterwards if the number of connectors changes.
     */
    void setInCount(int n);

    /**
     * Returns the current number of input connectors.
     */
    int getInCount();

    /**
     * Returns the input denotator at input connector number <code>i</code>. 
     * This is usually called at the beginning of the run() method
     * to get the input values. The run() method must check the
     * return value which may be null.
     */
    Denotator getInput(int i);

    /**
     * Sets the number of output connectors.
     * This method should be called with default value in the init() method.
     * It can be called afterwards if the number of connectors changes.
     */
    void setOutCount(int n);

    /**
     * Returns the current number of output connectors.
     */
    int getOutCount();

    /**
     * Stores the output denotator <code>d</code> for output connector number <code>i</code>.
     * This is usually called at the end of the run() method to store
     * the result of the computation.
     */
    void setOutput(int i, Denotator d);

    /**
     * Returns the output denotator of connectir number <code>i</code>.
     * This is usually called by the Runner.
     */
    Denotator getOutput(int i);

    /**
     * Adds an error string to the current error state.
     * If an error occurs in the run() method, this method
     * should be called with a short description of the error.
     */
    void addError(String msg, Object ... objects);

    /**
     * Returns a list of the current errors.
     * This is called by the Runner. If no error has occurred, it
     * returns an empty list, but never null.
     */
    List<String> getErrors();

    /**
     * Removes all errors from the error list.
     * This is called by the Runner, before the run() method is
     * executed.
     */
    void clearErrors();

    /**
     * Returns true iff any error has occurred.
     * This is called by the Runner, after the run() method has
     * been executed.
     */
    boolean hasErrors();

    /**
     * Sets the model this Rubette is attached to.
     * This is called by the Composer, and should, in general,
     * never be used by a Rubette implementation.
     */
    void setModel(PluginNode model);

    default Plugin getPlugin() {
        return NoopPlugin.INSTANCE;
    }

}