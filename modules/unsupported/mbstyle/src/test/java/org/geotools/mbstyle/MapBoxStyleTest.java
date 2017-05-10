/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.junit.Test;

public class MapBoxStyleTest {

    @Test
    public void validate() throws IOException, ParseException {
        Reader reader = MapboxTestUtils.readerTestStyle("fillStyleTest.json");
        List<Exception> problems = MapBoxStyle.validate(reader);
        
        assertTrue("fillStyleTest validate", problems.isEmpty() );
        
        reader = MapboxTestUtils.readerTestStyle("styleInvalidName.json");
        problems = MapBoxStyle.validate(reader);
        
        assertEquals("styleInvalidName 1 failure", 1, problems.size() );

        reader = MapboxTestUtils.readerTestStyle("styleInvalidLayers.json");
        problems = MapBoxStyle.validate(reader);
        
        assertEquals("styleInvalidLayers 2 failure", 2, problems.size() );
    }
}
