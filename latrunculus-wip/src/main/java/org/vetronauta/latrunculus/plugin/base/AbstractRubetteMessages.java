package org.vetronauta.latrunculus.plugin.base;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AbstractRubetteMessages {
    private static final String BUNDLE_NAME = "bundles/base/messages"; 

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private AbstractRubetteMessages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
