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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.math.RoundingMode;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/** Created by mihaildoronin on 11/11/15. */
public abstract class BaseJtsModuleTest<T extends Geometry> {
    protected GeometryFactory gf = new GeometryFactory();
    private ObjectWriter writer;
    private ObjectMapper mapper;
    private T geometry;
    private String geometryAsGeoJson;

    protected BaseJtsModuleTest() {}

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(
                new JtsModule(
                        new GeometryFactory(),
                        getMaxDecimals(),
                        getMinDecimals(),
                        RoundingMode.HALF_UP));
        writer = mapper.writer();
        geometry = createGeometry();
        geometryAsGeoJson = createGeometryAsGeoJson();
    }

    protected int getMaxDecimals() {
        return 4;
    }

    protected int getMinDecimals() {
        return 1;
    }

    protected abstract Class<T> getType();

    protected abstract String createGeometryAsGeoJson();

    protected abstract T createGeometry();

    @Test
    public void shouldDeserializeConcreteType() throws Exception {
        T concreteGeometry = mapper.readValue(geometryAsGeoJson, getType());
        assertThat(toJson(concreteGeometry), equalTo(geometryAsGeoJson));
    }

    @Test
    public void shouldDeserializeAsInterface() throws Exception {
        assertRoundTrip(geometry);
        assertThat(toJson(geometry), equalTo(geometryAsGeoJson));
    }

    protected String toJson(Object value) throws IOException {
        return writer.writeValueAsString(value);
    }

    protected void assertRoundTrip(T geom) throws IOException {
        String json = writer.writeValueAsString(geom);

        Geometry regeom = mapper.readerFor(Geometry.class).readValue(json);
        assertThat(geom.equalsExact(regeom, Math.pow(10, -getMaxDecimals())), is(true));
    }
}
