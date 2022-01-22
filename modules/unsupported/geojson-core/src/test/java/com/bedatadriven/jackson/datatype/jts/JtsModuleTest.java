package com.bedatadriven.jackson.datatype.jts;
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

public class JtsModuleTest {
    private ObjectMapper mapper;

    @Before
    public void setupMapper() {

        mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
    }

    @Test(expected = JsonMappingException.class)
    public void invalidGeometryType() throws IOException {
        String json = "{\"type\":\"Singularity\",\"coordinates\":[]}";
        mapper.readValue(json, Geometry.class);
    }

    @Test(expected = JsonMappingException.class)
    @Ignore
    public void unsupportedGeometry() throws IOException {
        Geometry unsupportedGeometry =
                EasyMock.createNiceMock("NonEuclideanGeometry", Geometry.class);
        EasyMock.replay(unsupportedGeometry);

        mapper.writeValue(System.out, unsupportedGeometry);
    }
}
