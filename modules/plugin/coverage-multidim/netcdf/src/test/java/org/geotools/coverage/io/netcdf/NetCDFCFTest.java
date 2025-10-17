/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import org.geotools.coverage.io.netcdf.cf.Entry;
import org.geotools.coverage.io.netcdf.cf.NetCDFCFParser;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;

public final class NetCDFCFTest extends Assert {

    @Test
    public void testCFParsing() throws FileNotFoundException, IOException, JAXBException {
        final File file = TestData.file(this, "cf-reduced-standard-name-table.xml");
        NetCDFCFParser parser = NetCDFCFParser.unmarshallXml(file);
        assertTrue(parser.hasEntryId("air_potential_temperature"));
        assertTrue(parser.hasAliasId("atmosphere_sulfate_content"));

        Entry entry = parser.getEntry("air_potential_temperature");
        assertNotNull(entry);
        assertEquals("K", entry.getCanonicalUnits());
        assertEquals("13", entry.getGrib());
        assertEquals("theta", entry.getAmip());

        assertNull(parser.getEntry("MissingENTRY"));
        assertFalse(parser.hasAliasId("MissingAlias"));

        assertNull(parser.getEntryFromAlias("MissingAliasEntry"));
        entry = parser.getEntryFromAlias("atmosphere_sulfate_content");
        assertSame(entry, parser.getEntry("atmosphere_mass_content_of_sulfate"));

        // Testing entries
        Set<String> entries = parser.getEntryIds();
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
        assertTrue(entries.contains("air_potential_temperature"));
    }
}
