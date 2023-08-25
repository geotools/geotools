package org.geotools.metadata;

import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class InternationalizationReplacements {

    public static void main(String[] args) {
        Vocabulary vocabulary = Vocabulary.getResources(Locale.ENGLISH);
        Map<String, String> vocabularyKeys = Arrays.stream(VocabularyKeys.class.getFields())
                .filter(f -> int.class.equals(f.getType()))
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .collect(Collectors.toMap(f -> "VocabularyKeys." + f.getName(), f -> {
                    try {
                        return vocabulary.getString(f.getInt(null));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }));
        vocabularyKeys.forEach((k, v) -> System.out.println(k + " = " + v));
    }
}
