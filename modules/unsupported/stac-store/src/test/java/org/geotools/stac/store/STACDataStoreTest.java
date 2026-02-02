/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import tools.jackson.databind.node.ObjectNode;

public class STACDataStoreTest extends AbstractSTACStoreTest {

    @Test
    public void testTypeNames() throws Exception {
        String[] typeNames = store.getTypeNames();
        assertArrayEquals(TYPE_NAMES, typeNames);
    }

    @Test
    public void testNames() throws Exception {
        List<Name> expectedList =
                Arrays.stream(TYPE_NAMES).map(n -> (Name) new NameImpl(n)).collect(Collectors.toList());
        assertEquals(expectedList, store.getNames());
    }

    @Test
    public void testSchema() throws Exception {
        // basic consistency
        SimpleFeatureType schema = store.getSchema(MAJA);
        assertEquals(MAJA, schema.getTypeName());
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        assertEquals(fs.getSchema(), store.getSchema(MAJA));

        // geometry
        GeometryDescriptor geometryDescriptor = schema.getGeometryDescriptor();
        assertEquals(Polygon.class, geometryDescriptor.getType().getBinding());
        assertEquals(DefaultGeographicCRS.WGS84, geometryDescriptor.getCoordinateReferenceSystem());

        // other attributes
        assertEquals(Date.class, getBinding(schema, "created"));
        assertEquals(Date.class, getBinding(schema, "updated"));
        assertEquals(Date.class, getBinding(schema, "datetime"));
        assertEquals(String.class, getBinding(schema, "platform"));
        assertEquals(List.class, getBinding(schema, "instruments"));
        assertEquals(Double.class, getBinding(schema, "gsd"));
        assertEquals(Object.class, getBinding(schema, "processing:software"));
        assertEquals(String.class, getBinding(schema, "sar:frequency_band"));
        assertEquals(ObjectNode.class, getBinding(schema, "assets"));
    }

    private Class<?> getBinding(SimpleFeatureType schema, String name) {
        return schema.getDescriptor(name).getType().getBinding();
    }
}
