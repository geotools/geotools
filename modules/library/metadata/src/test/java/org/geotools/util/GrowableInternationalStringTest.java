package org.geotools.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;

public class GrowableInternationalStringTest {

    @Test
    public void testCopyConstructor() {
        SimpleInternationalString simpleInternationalString =
                new SimpleInternationalString("A simpleInternationalString");
        GrowableInternationalString growable =
                new GrowableInternationalString(simpleInternationalString);
        assertEquals(1, growable.getLocales().size());
        assertEquals("A simpleInternationalString", growable.toString(Locale.getDefault()));

        GrowableInternationalString toCopy = new GrowableInternationalString();
        toCopy.add(Locale.ENGLISH, "english text");
        toCopy.add(Locale.ITALIAN, "testo italiano");
        toCopy.add(Locale.FRENCH, "texte français");
        GrowableInternationalString newGrowable = new GrowableInternationalString(toCopy);
        assertEquals(3, newGrowable.getLocales().size());
        assertEquals("english text", newGrowable.toString(Locale.ENGLISH));
        assertEquals("testo italiano", newGrowable.toString(Locale.ITALIAN));
        assertEquals("texte français", newGrowable.toString(Locale.FRENCH));
    }

    @Test
    public void testEmptyLanguage() {
        GrowableInternationalString toCopy = new GrowableInternationalString();
        toCopy.add(Locale.ENGLISH, "english text");
        toCopy.add(null, "default text");
        GrowableInternationalString newGrowable = new GrowableInternationalString(toCopy);
        assertEquals(2, newGrowable.getLocales().size());
        assertEquals("english text", newGrowable.toString(Locale.ENGLISH));
        assertEquals("default text", newGrowable.toString(null));
    }

    @Test
    public void testGetLocales() {
        GrowableInternationalString gi18ns = new GrowableInternationalString();
        assertEquals(Collections.emptySet(), gi18ns.getLocales());

        gi18ns.add(null, "default value");
        assertEquals(Collections.singleton(null), gi18ns.getLocales());

        gi18ns.add(Locale.CANADA_FRENCH, "ca-FR value");
        assertEquals(newSet(null, Locale.CANADA_FRENCH), gi18ns.getLocales());

        gi18ns.add(Locale.GERMAN, "de value");
        assertEquals(newSet(null, Locale.CANADA_FRENCH, Locale.GERMAN), gi18ns.getLocales());
    }

    private Set<Locale> newSet(Locale... locales) {
        return new HashSet<>(Arrays.asList(locales));
    }
}
