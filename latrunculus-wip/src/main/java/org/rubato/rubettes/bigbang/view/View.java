package org.rubato.rubettes.bigbang.view;

import java.beans.PropertyChangeEvent;

public interface View {

    /**
     * Called by the controller when it needs to pass along a property change 
     * from a model.
     *
     * @param event The property change event from the model
     */
    void modelPropertyChange(PropertyChangeEvent event);
    
}