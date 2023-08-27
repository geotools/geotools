package org.geotools.metadata;

import java.util.regex.Pattern;

public class InternationalizationReplacements {

    public static void main(String[] args) {
        //        Errors errors = Errors.getResources(Locale.ENGLISH);
        //        Map<String, String> errorProperties =
        //                Arrays.stream(ErrorKeys.class.getFields())
        //                        .filter(f -> int.class.equals(f.getType()))
        //                        .filter(f -> Modifier.isStatic(f.getModifiers()))
        //                        .collect(
        //                                Collectors.toMap(
        //                                        f -> "ErrorKeys." + f.getName(),
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
        //                                "        <replaceregexp byline=\"true\" flags=\"g\">\n"
        //                                        + "            <regexp pattern=\""
        //                                        + cleanKey(k)
        //                                        + "\"/>\n"
        //                                        + "            <substitution expression=\"&quot;"
        //                                        + cleanValue(v)
        //                                        + "&quot;\"/>\n"
        //                                        + "            <fileset
        // refid=\"errorKeys.files\"/>\n"
        //                                        + "        </replaceregexp>"));
    }

    private static String cleanKey(String k) {
        return Pattern.quote("Errors.getPattern(" + k + ")");
    }

    private static String cleanValue(String v) {
        return v.replace("''", "'").replace("\"", "\\\\\\\\&quot;");
    }
}
