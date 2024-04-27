package org.vetronauta.latrunculus.client.plugin.properties;

import lombok.Getter;
import org.vetronauta.latrunculus.core.util.DeepCopyable;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;

import javax.swing.*;

public abstract class ClientPluginProperty<T> implements DeepCopyable<ClientPluginProperty<T>>, Comparable<ClientPluginProperty<?>> {

    @Getter
    protected PluginProperty<T> pluginProperty;

    protected ClientPluginProperty(PluginProperty<T> pluginProperty) {
        this.pluginProperty = pluginProperty;
    }

    protected ClientPluginProperty(String key, String name, T value) {
        this.pluginProperty = new PluginProperty<>(key, name, value);
    }

    public abstract JComponent getJComponent();

    public abstract void revert();

    public String getKey() {
        return pluginProperty.getKey();
    }

    public String getName() {
        return pluginProperty.getName();
    }

    public void apply() {
        pluginProperty.apply();
    }

    public T getValue() {
        return pluginProperty.getValue();
    }

    @Override
    public int compareTo(ClientPluginProperty<?> obj) {
        return pluginProperty.getOrder() - obj.pluginProperty.getOrder();
    }

    @Override
    public String toString() {
        return pluginProperty.toString();
    }

}
