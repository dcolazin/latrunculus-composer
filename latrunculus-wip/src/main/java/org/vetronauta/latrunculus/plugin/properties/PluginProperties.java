package org.vetronauta.latrunculus.plugin.properties;

import org.vetronauta.latrunculus.client.plugin.properties.ClientPluginProperty;
import org.vetronauta.latrunculus.core.util.DeepCopyable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PluginProperties implements DeepCopyable<PluginProperties> {

    private final HashMap<String, PluginProperty<?>> properties;

    public PluginProperties() {
        properties = new HashMap<>();
    }

    public void put(PluginProperty<?> prop) {
        properties.put(prop.getKey(), prop);
    }

    public PluginProperty<?> get(String key) {
        return properties.get(key);
    }

    public Collection<PluginProperty<?>> getProperties() {
        return properties.values();
    }

    public void apply() {
        for (PluginProperty<?> prop : properties.values()) {
            prop.apply();
        }
    }

    public PluginProperties deepCopy() {
        PluginProperties newProp = new PluginProperties();
        for (Map.Entry<String, PluginProperty<?>> entry : properties.entrySet()) {
            newProp.properties.put(entry.getKey(), entry.getValue().deepCopy());
        }
        return newProp;
    }


    public String toString() {
        StringBuilder s;
        s = new StringBuilder("------------------------------------------------\n");
        for (PluginProperty<?> prop : properties.values()) {
            s.append(prop).append("\n");
        }
        s.append("------------------------------------------------\n");
        return s.toString();
    }

}