package br.com.virtualab.utils;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class VirtualabResourceBundle {

    private static final String BUNDLE_NAME = "i18n.messages";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getMessage(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return String.format("??? %s ???", key);
        }
    }

    public static String getMessage(String key, Object... params) {
        try {
            String[] arguments = new String[params.length];
            for (int index = 0; index < params.length; index++) {
                if (params[index] instanceof String) {
                    arguments[index] = getMessage(params[index].toString());
                } else {
                    arguments[index] = params[index].toString();
                }
            }
            return MessageFormat.format(RESOURCE_BUNDLE.getString(key), (Object[]) arguments);
        } catch (MissingResourceException e) {
            return String.format("??? %s ???", key);
        }
    }
}
