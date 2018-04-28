/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio;

import org.junit.Assert;
import org.junit.Test;

/** Simple {@link Identification} class test */
public class IdentificationTest extends Assert {

    @Test
    public void testIdentification() {
        final String name = "mean sea level depth";
        Identification id = new Identification(name, null, null, "EPSG:5715");
        assertEquals(name, id.getName());
        assertEquals("EPSG:5715", id.getIdentifier());
        assertNull(id.getRemarks());
        assertNull(id.getAlias());
        final int length = id.length();
        assertEquals(20, length);

        final Identification id2 = (Identification) new Identification(name).subSequence(5, length);
        final Identification id3 = (Identification) id.subSequence(0, length);
        assertNotEquals(id, id2);
        assertSame(id, id3);
        assertEquals(id, id3);
    }
}
