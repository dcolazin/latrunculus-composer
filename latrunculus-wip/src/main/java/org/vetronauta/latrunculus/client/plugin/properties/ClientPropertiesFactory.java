package org.vetronauta.latrunculus.client.plugin.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vetronauta.latrunculus.core.math.element.impl.Complex;
import org.vetronauta.latrunculus.core.math.yoneda.denotator.Denotator;
import org.vetronauta.latrunculus.core.math.yoneda.form.Form;
import org.vetronauta.latrunculus.plugin.properties.DoubleProperty;
import org.vetronauta.latrunculus.plugin.properties.IntegerProperty;
import org.vetronauta.latrunculus.plugin.properties.PluginProperties;
import org.vetronauta.latrunculus.plugin.properties.PluginProperty;
import org.vetronauta.latrunculus.plugin.properties.RationalProperty;
import org.vetronauta.latrunculus.plugin.properties.StringProperty;

import java.io.File;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientPropertiesFactory {

    public static ClientPluginProperties build(PluginProperties properties) {
        ClientPluginProperties clientPluginProperties = new ClientPluginProperties();
        properties.getProperties()
            .stream()
            .map(ClientPropertiesFactory::build)
            .filter(Objects::nonNull)
            .forEach(clientPluginProperties::put);
        return clientPluginProperties;
    }

    public static ClientPluginProperty<?> build(PluginProperty<?> property) {
        if (property instanceof DoubleProperty) {
            return new DoubleClientProperty((DoubleProperty) property);
        }
        if (property instanceof IntegerProperty) {
            return new IntegerClientProperty((IntegerProperty) property);
        }
        if (property instanceof RationalProperty) {
            return new RationalClientProperty((RationalProperty) property);
        }
        if (property instanceof StringProperty) {
            return new StringClientProperty((StringProperty) property);
        }
        Object value = property.getValue();
        if (value instanceof Boolean) {
            return new BooleanClientProperty(property.getKey(), property.getName(), (Boolean) value);
        }
        if (value instanceof Complex) {
            return new ComplexClientProperty(property.getKey(), property.getName(), (Complex) value);
        }
        if (value instanceof Denotator) {
            return new DenotatorClientProperty(property.getKey(), property.getName(), (Denotator) value);
        }
        if (value instanceof File) {
            return new FileClientProperty(property.getKey(), property.getName(), (File) value);
        }
        if (value instanceof Form) {
            return new FormClientProperty(property.getKey(), property.getName(), (Form) value);
        }
        if (value instanceof String) {
            return new TextClientProperty(property.getKey(), property.getName(), (String) value);
        }
        return null;
    }

}
