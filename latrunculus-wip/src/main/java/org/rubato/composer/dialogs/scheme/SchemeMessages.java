package org.rubato.composer.dialogs.scheme;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SchemeMessages {
    private static final String BUNDLE_NAME = "bundles/composer/dialogs/scheme/messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private SchemeMessages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
