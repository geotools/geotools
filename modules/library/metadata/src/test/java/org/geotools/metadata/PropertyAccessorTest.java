/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.metadata;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.util.InternationalString;

import org.geotools.util.SimpleInternationalString;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.Citations;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link PropertyAccessor} class.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class PropertyAccessorTest {
    /**
     * Creates a property accessor for the given citation.
     */
    private static PropertyAccessor createPropertyAccessor(final Citation citation) {
        final Class<?> implementation = citation.getClass();
        final Class<?> type = PropertyAccessor.getType(implementation, "org.opengis.metadata");
        assertNotNull(type);
        return new PropertyAccessor(implementation, type);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        final Citation citation = Citations.EPSG;
        PropertyAccessor accessor;
        assertNull("No dummy interface expected.",
                PropertyAccessor.getType(citation.getClass(), "org.opengis.dummy"));
        accessor = createPropertyAccessor(citation);
        assertTrue("Count of 'get' methods.", accessor.count() >= 13);
    }

    /**
     * Tests the {@code indexOf} and {code name} methods.
     */
    @Test
    public void testName() {
        final Citation citation = Citations.EPSG;
        final PropertyAccessor accessor = createPropertyAccessor(citation);
        assertEquals("Non-existent property",   -1,  accessor.indexOf("dummy"));
        assertEquals("getTitle() property", "title", accessor.name(accessor.indexOf("title")));
        assertEquals("getTitle() property", "title", accessor.name(accessor.indexOf("TITLE")));
        assertEquals("getISBN() property",  "ISBN",  accessor.name(accessor.indexOf("ISBN")));
        assertNull(accessor.name(-1));
    }

    /**
     * Tests the get method.
     */
    @Test
    public void testGet() {
        Citation citation = Citations.EPSG;
        final PropertyAccessor accessor = createPropertyAccessor(citation);
        final int index = accessor.indexOf("identifiers");
        assertTrue(index >= 0);
        final Object identifiers = accessor.get(index, citation);
        assertNotNull(identifiers);
        assertTrue(containsEPSG(identifiers));
    }

    /**
     * Returns {@code true} if the specified identifiers contains the {@code "EPSG"} code.
     */
    static boolean containsEPSG(final Object identifiers) {
        assertTrue(identifiers instanceof Collection);
        @SuppressWarnings("unchecked")
        final Collection<Identifier> collection = (Collection) identifiers;
        for (final Identifier id : collection) {
            if (id.getCode().equals("EPSG")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests the set method.
     */
    @Test
    public void testSet() {
        Citation citation = new CitationImpl();
        final PropertyAccessor accessor = createPropertyAccessor(citation);

        // Tries with ISBN, which expect a String.
        Object value = "Random number";
        int index = accessor.indexOf("ISBN");
        assertTrue(index >= 0);
        assertNull(accessor.set(index, citation, value));
        assertSame(value, accessor.get(index, citation));
        assertSame(value, citation.getISBN());

        // Tries with the title. Automatic conversion from String to InternationalString expected.
        index = accessor.indexOf("title");
        assertTrue(index >= 0);
        assertNull(accessor.set(index, citation, "A random title"));
        value = accessor.get(index, citation);
        assertTrue(value instanceof InternationalString);
        assertEquals("A random title", value.toString());
        assertSame(value, citation.getTitle());

        // Tries with an element to be added in a collection.
        index = accessor.indexOf("alternateTitle");
        assertTrue(index >= 0);

        value = accessor.get(index, citation);
        assertTrue(value instanceof Collection);
        assertTrue(((Collection) value).isEmpty());

        value = accessor.set(index, citation, "An other title");
        assertTrue(value instanceof Collection);
        assertEquals(1, ((Collection) value).size());

        value = accessor.set(index, citation, "Yet an other title");
        assertTrue(value instanceof Collection);
        assertEquals(2, ((Collection) value).size());
    }

    /**
     * Tests the shallow equals and copy methods.
     */
    @Test
    public void testEquals() {
        Citation citation = Citations.EPSG;
        final PropertyAccessor accessor = createPropertyAccessor(citation);
        assertFalse(accessor.shallowEquals(citation, Citations.GEOTIFF, true ));
        assertFalse(accessor.shallowEquals(citation, Citations.GEOTIFF, false));
        assertTrue (accessor.shallowEquals(citation, Citations.EPSG,    false));

        citation = new CitationImpl();
        assertTrue (accessor.shallowCopy  (Citations.EPSG, citation,    true ));
        assertFalse(accessor.shallowEquals(citation, Citations.GEOTIFF, true ));
        assertFalse(accessor.shallowEquals(citation, Citations.GEOTIFF, false));
        assertTrue (accessor.shallowEquals(citation, Citations.EPSG,    false));

        final int index = accessor.indexOf("identifiers");
        final Object source = accessor.get(index, Citations.EPSG);
        final Object target = accessor.get(index, citation);
        assertNotNull(source);
        assertNotNull(target);
        assertNotSame(source, target);
        assertEquals (source, target);
        assertTrue(containsEPSG(target));

        assertSame(target, accessor.set(index, citation, null));
        final Object value = accessor.get(index, citation);
        assertNotNull(value);
        assertTrue(((Collection) value).isEmpty());

        try {
            accessor.shallowCopy(citation, Citations.EPSG, true);
            fail("Citations.EPSG should be unmodifiable.");
        } catch (UnmodifiableMetadataException e) {
            // This is the expected exception.
        }
    }

    /**
     * Tests the hash code computation.
     */
    @Test
    public void testHashCode() {
        final CitationImpl citation = new CitationImpl();
        final PropertyAccessor accessor = createPropertyAccessor(citation);
        int hashCode = accessor.hashCode(citation);
        assertEquals("Empty metadata.", 0, hashCode);

        final String ISBN = "Dummy ISBN";
        citation.setISBN(ISBN);
        hashCode = accessor.hashCode(citation);
        assertEquals("Metadata with a single String value.", ISBN.hashCode(), hashCode);

        final Set<Object> set = new HashSet<Object>();
        assertEquals("By Set.hashCode() contract.", 0, set.hashCode());
        assertTrue(set.add(ISBN));
        assertEquals("Expected Metadata.hashCode() == Set.hashCode().", set.hashCode(), hashCode);

        final InternationalString title = new SimpleInternationalString("Dummy title");
        citation.setTitle(title);
        hashCode = accessor.hashCode(citation);
        assertEquals("Metadata with two values.", ISBN.hashCode() + title.hashCode(), hashCode);
        assertTrue(set.add(title));
        assertEquals("Expected Metadata.hashCode() == Set.hashCode().", set.hashCode(), hashCode);
        assertEquals("CitationsImpl.hashCode() should delegate.", hashCode, citation.hashCode());

        final Collection<Object> values = citation.asMap().values();
        assertEquals(hashCode, new HashSet<Object>(values).hashCode());
        assertTrue(values.containsAll(set));
        assertTrue(set.containsAll(values));
    }
}
