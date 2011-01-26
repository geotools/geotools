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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.opengis.metadata.citation.Citation;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.Citations;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link MetadataStandard} class.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class MetadataStandardTest {
    /**
     * Tests the shallow equals and copy methods.
     */
    @Test
    public void testEquals() {
        final MetadataStandard std = MetadataStandard.ISO_19115;
        Citation citation = Citations.EPSG;
        assertFalse(std.shallowEquals(citation, Citations.GEOTIFF, true ));
        assertFalse(std.shallowEquals(citation, Citations.GEOTIFF, false));
        assertTrue (std.shallowEquals(citation, Citations.EPSG,    false));

        citation = new CitationImpl();
        std.shallowCopy(Citations.EPSG, citation, true);
        assertFalse(std.shallowEquals(citation, Citations.GEOTIFF, true ));
        assertFalse(std.shallowEquals(citation, Citations.GEOTIFF, false));
        assertTrue (std.shallowEquals(citation, Citations.EPSG,    false));

        try {
            std.shallowCopy(citation, Citations.EPSG, true);
            fail("Citations.EPSG should be unmodifiable.");
        } catch (UnmodifiableMetadataException e) {
            // This is the expected exception.
        }
    }

    /**
     * Tests the {@link PropertyMap} implementation.
     */
    @Test
    public void testMap() {
        final Citation citation = new CitationImpl(Citations.EPSG);
        final Map<String,Object> map = MetadataStandard.ISO_19115.asMap(citation);
        assertFalse(map.isEmpty());
        assertTrue (map.size() > 1);

        final Set<String> keys = map.keySet();
        assertTrue ("Property exists and should be defined.",            keys.contains("title"));
        assertFalse("Property exists but undefined for Citations.EPSG.", keys.contains("ISBN"));
        assertFalse("Property do not exists.",                           keys.contains("dummy"));

        final String s = keys.toString();
        assertTrue (s.indexOf("title")       >= 0);
        assertTrue (s.indexOf("identifiers") >= 0);
        assertFalse(s.indexOf("ISBN")        >= 0);

        final Object identifiers = map.get("identifiers");
        assertTrue(identifiers instanceof Collection);
        assertTrue(PropertyAccessorTest.containsEPSG(identifiers));

        final Map<String,Object> copy = new HashMap<String,Object>(map);
        assertEquals(map, copy);

        // Note: AbstractCollection do not defines hashCode(); we have to wraps in a HashSet.
        final int hashCode = citation.hashCode();
        assertEquals("hashCode() should be as in a Set.", hashCode, new HashSet<Object>(map .values()).hashCode());
        assertEquals("hashCode() should be as in a Set.", hashCode, new HashSet<Object>(copy.values()).hashCode());

        map.remove("identifiers");
        final int newHashCode = citation.hashCode();
        assertFalse(map.equals(copy));
        assertFalse(hashCode == newHashCode);
        assertEquals(newHashCode, new HashSet<Object>(map.values()).hashCode());
    }
}
