package io.github.newhoo.jvm.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * i18n
 *
 * @author huzunrong
 * @date 2020/11/19 10:09 AM
 * @since 1.0.2
 */
public class JvmParameterBundle {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages.jvm-parameter", getLocale());

    private static Locale getLocale() {
        String lang = Locale.getDefault().getLanguage();
        if (lang.equals(Locale.ENGLISH.getLanguage()) || lang.equals(Locale.CHINESE.getLanguage())) {
            return Locale.getDefault();
        }
        return Locale.ENGLISH;
    }

    public static String getMessage(String key) {
        return resourceBundle.getString(key).trim();
    }
}
