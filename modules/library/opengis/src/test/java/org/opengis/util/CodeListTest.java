/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.opengis.metadata.identification.CharacterSet;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests every {@link CodeList}.
 *
 * @author Martin desruisseaux (IRD)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/test/java/org/opengis/util/CodeListTest.java $
 */
public final class CodeListTest {
    /**
     * The logger to use.
     */
    private static final Logger LOGGER = Logger.getLogger("org.opengis");

    /**
     * For avoiding to pollute the output stream if {@code ArrayList.capacity()}
     * method invocation failed.
     */
    private static boolean capacityFailed = false;

    /**
     * Tests the {@link CharacterSet} code list. At the difference of other code lists,
     * its {@link CodeList#matches} method is overriden.
     */
    @Test
    public void testCharacterSet() {
        final CodeList code = CharacterSet.UTF_8;
        assertEquals ("UTF_8", code.name());
        assertEquals ("utf8",  code.identifier());
        assertTrue   (code.matches("UTF8"));
        assertTrue   (code.matches("UTF_8"));
        assertTrue   (code.matches("UTF-8"));
        assertFalse  (code.matches("UTF 8"));
        assertSame   (code, CharacterSet.valueOf("UTF_8"));
        assertSame   (code, CharacterSet.valueOf("UTF-8"));
        assertSame   (code, CharacterSet.valueOf("UTF8"));
        assertSame   (code, CharacterSet.valueOf("utf8"));
        assertNotSame(code, CharacterSet.valueOf("UTF_7"));
    }

    /**
     * Tests the instantiation of every code lists.
     */
    @Test
    public void testAll() {
        int count = 0;
        final Class<CodeList> base = CodeList.class;
        final ClassScanner scanner = new ClassScanner();
        while (scanner.hasNext()) {
            final Class<?> candidate = scanner.next();
            if (!base.equals(candidate) && base.isAssignableFrom(candidate)) {
                // SimpleEnumeratioType is a special case to avoid for now.
                final String name = candidate.getName();
                if (name.equals("org.opengis.util.SimpleEnumerationType")) {
                    continue;
                }
                if (name.equals("org.opengis.filter.sort.SortOrder")) {
                    continue;
                }
                assertValid(candidate.asSubclass(CodeList.class));
                count++;
            }
        }
        LOGGER.fine("Found " + count + " code lists.");
        if (count == 0) {
            LOGGER.warning("No CodeList found.");
        }
    }

    /**
     * Ensures that the name declared in the code list match the field names.
     */
    private static void assertValid(final Class<? extends CodeList> classe) {
        Method method;
        int modifiers;
        String fullName;
        /*
         * Gets the values() method, which should public and static.
         * Then gets every CodeList instances returned by values().
         */
        final String className = classe.getName();
        fullName = className + ".values()";
        try {
            method = classe.getMethod("values", (Class[]) null);
        } catch (NoSuchMethodException e) {
            fail(fullName + " method is missing.");
            return;
        }
        assertNotNull(method);
        modifiers = method.getModifiers();
        assertTrue(fullName + " is not public.", Modifier.isPublic(modifiers));
        assertTrue(fullName + " is not static.", Modifier.isStatic(modifiers));
        final CodeList[] values;
        try {
            values = (CodeList[]) method.invoke(null, (Object[]) null);
        } catch (IllegalAccessException e) {
            fail(fullName + " is not accessible.");
            return;
        } catch (InvocationTargetException e) {
            fail("Call to " + fullName + " failed.\n" + e.getTargetException());
            return;
        }
        assertNotNull(fullName + " returned null.", values);
        /*
         * Gets the family() method, to be used when we will test every
         * code list instances.
         */
        fullName = className + ".family()";
        try {
            method = classe.getMethod("family", (Class[]) null);
        } catch (NoSuchMethodException e) {
            fail(fullName + " method is missing.");
            return;
        }
        assertNotNull(method);
        modifiers = method.getModifiers();
        assertTrue (fullName + " is not public.", Modifier.isPublic(modifiers));
        assertFalse(fullName + " is static.",     Modifier.isStatic(modifiers));
        /*
         * Tests every CodeList instances returned by values().
         * Every field should be public, static and final.
         */
        for (final CodeList value : values) {
            final String name = value.name();
            fullName = className + '.' + name;
            assertTrue(fullName + ": unexpected type.", classe.isInstance(value));
            final Field field;
            try {
                field = classe.getField(name);
            } catch (NoSuchFieldException e) {
                final Class<? extends CodeList> valueClass = value.getClass();
                if (!classe.equals(valueClass) && classe.isAssignableFrom(valueClass)) {
                    // Do not fails if valueClass is a subclass of classe.
                    continue;
                }
                fail(fullName + " field not found.");
                continue;
            }
            assertNotNull(field);
            modifiers = field.getModifiers();
            assertEquals(fullName + ": unexpected name mismatch.", name, field.getName());
            assertTrue  (fullName + " is not public.", Modifier.isPublic(modifiers));
            assertTrue  (fullName + " is not static.", Modifier.isStatic(modifiers));
            assertTrue  (fullName + " is not final.",  Modifier.isFinal (modifiers));
            Object constant;
            try {
                constant = field.get(null);
            } catch (IllegalAccessException e) {
                fail(fullName + " is not accessible.");
                continue;
            }
            assertSame(fullName + " is not the expected instance.", value, constant);
            final CodeList[] family;
            try {
                family = (CodeList[]) method.invoke(constant, (Object[]) null);
            } catch (IllegalAccessException e) {
                fail(className + ".family() is not accessible.");
                return;
            } catch (InvocationTargetException e) {
                fail("Call to " + className + ".family() failed.\n" + e.getTargetException());
                return;
            }
            assertTrue(className + ".family() mismatch.", Arrays.equals(values, family));
        }
        /*
         * Gets the private VALUES field only if CodeList is the direct parent.
         */
        if (classe.getSuperclass().equals(CodeList.class)) {
            fullName = className + ".VALUES";
            final Field field;
            try {
                field = classe.getDeclaredField("VALUES");
            } catch (NoSuchFieldException e) {
                fail(fullName + " private list is missing.");
                return;
            }
            modifiers = field.getModifiers();
            assertTrue (Modifier.isStatic   (modifiers));
            assertTrue (Modifier.isFinal    (modifiers));
            assertFalse(Modifier.isPublic   (modifiers));
            assertFalse(Modifier.isProtected(modifiers));
            field.setAccessible(true);
            final ArrayList<?> asList;
            try {
                final Object candidate = field.get(null);
                assertEquals(fullName + " is not an ArrayList.", ArrayList.class, candidate.getClass());
                asList = (ArrayList<?>) candidate;
            } catch (IllegalAccessException e) {
                fail(className + ".VALUES is not accessible.");
                return;
            }
            assertEquals(Arrays.asList(values), asList);
            /*
             * Verifies if the VALUES ArrayList size was properly sized. We need to access to
             * private ArrayList.elementData field in order to perform this check.  Tested on
             * Sun's JSE 6.0. It is not mandatory to have the VALUES list properly dimensioned;
             * it just avoid a little bit of memory reallocation at application startup time.
             */
            if (!capacityFailed) {
                final int capacity;
                try {
                    final Field candidate = ArrayList.class.getDeclaredField("elementData");
                    candidate.setAccessible(true);
                    final Object array = candidate.get(asList);
                    capacity = ((Object[]) array).length;
                } catch (Exception e) {
                    // Not an error, since this test relies on an implementation-specific method.
                    capacityFailed = true;
                    final LogRecord record = new LogRecord(Level.WARNING, e.toString());
                    record.setThrown(e);
                    record.setLoggerName(LOGGER.getName());
                    LOGGER.log(record);
                    return;
                }
                assertEquals(fullName + " not properly sized.", asList.size(), capacity);
            }
        }
        /*
         * Tries to create a new element.
         */
        try {
            method = classe.getMethod("valueOf", String.class);
        } catch (NoSuchMethodException e) {
            return;
        }
        final CodeList value;
        try {
            value = classe.cast(method.invoke(null, "Dummy"));
        } catch (IllegalAccessException e) {
            fail(e.toString());
            return;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail(e.getTargetException().toString());
            return;
        }
        assertEquals("Dummy", value.name());
    }
}
