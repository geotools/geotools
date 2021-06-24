package org.geotools.util;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
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
}
