package org.vetronauta.latrunculus.client.plugin.prop;

import org.vetronauta.latrunculus.core.util.DeepCopyable;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;

import javax.swing.*;

public abstract class ClientPluginProperty<T> implements DeepCopyable<ClientPluginProperty<T>> {

    protected PluginProperty<T> pluginProperty;

    protected ClientPluginProperty(PluginProperty<T> pluginProperty) {
        this.pluginProperty = pluginProperty;
    }

    public abstract JComponent getJComponent();

    public abstract void revert();

    @Override
    public String toString() {
        return pluginProperty.toString();
    }

}
