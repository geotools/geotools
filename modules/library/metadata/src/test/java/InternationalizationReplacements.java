package org.geotools.metadata.i18n;

import java.util.regex.Pattern;

@SuppressWarnings("DefaultPackage") // Test utility class
public class InternationalizationReplacements {

    public static void main(String[] args) {
        //        Errors errors = Errors.getResources(Locale.ENGLISH);
        //        Map<String, String> errorProperties =
        //                Arrays.stream(ErrorKeys.class.getFields())
        //                        .filter(f -> int.class.equals(f.getType()))
        //                        .filter(f -> Modifier.isStatic(f.getModifiers()))
        //                        .collect(
        //                                Collectors.toMap(
        //                                        f -> f.getName(),
        //                                        f -> {
        //                                            try {
        //                                                return errors.getString(f.getInt(null));
        //                                            } catch (IllegalAccessException e) {
        //                                                throw new RuntimeException(e);
        //                                            }
        //                                        }));
        //        errorProperties.forEach(
        //                (k, v) ->
        //                        System.out.println(
        //                                "public static final String "
        //                                        + k
        //                                        + " = \""
        //                                        + cleanValue(v)
        //                                        + "\";"));
    }

    static String cleanKey(String k) {
        return Pattern.quote("Errors.getPattern(" + k + ")");
    }

    static String cleanValue(String v) {
        return v.replace("''", "'").replace("\"", "\\\"");
    }
}
