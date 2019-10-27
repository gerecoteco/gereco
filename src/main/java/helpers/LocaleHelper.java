package helpers;

import javafx.util.StringConverter;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class LocaleHelper {
    private static List<Locale> implementedLocales = null;
    private static StringConverter<Locale> localeStringConverter = new StringConverter<Locale>() {
        @Override
        public String toString(Locale object) {
            return getLocaleDisplayName(object);
        }

        @Override
        public Locale fromString(String string) {
            return new Locale(string);
        }
    };

    public static void updateDefaultLang() {
        if(!implementedLocales.contains(Locale.getDefault()))
            Locale.setDefault(new Locale("en"));
    }

    public static List<Locale> getImplementedLocales() {
        if(implementedLocales == null) {
            implementedLocales = new LinkedList<>();
            for(Locale item : Locale.getAvailableLocales()) {
                if(item.getLanguage().equals("")) { // English language file does not have suffix and display name
                    implementedLocales.add(new Locale("en"));
                    continue;
                }

                String lang = "_" + item.getLanguage();
                String country = (item.getCountry().equals("") ? "" : "_") + item.getCountry();
                String path = String.format("/bundles/lang%s%s.properties", lang, country);
                URL url = LocaleHelper.class.getResource(path);
                if(url != null) {
                    implementedLocales.add(item);
                }
            }
        }
        return implementedLocales;
    }

    public static StringConverter<Locale> getLocaleStringConverter() {
        return localeStringConverter;
    }

    private static String getLocaleDisplayName(Locale locale) {
        if(locale.getCountry().equals(""))
            return String.format("%s", locale.getDisplayLanguage());
        return String.format("%s (%s)", locale.getDisplayLanguage(), locale.getDisplayCountry());
    }
}
