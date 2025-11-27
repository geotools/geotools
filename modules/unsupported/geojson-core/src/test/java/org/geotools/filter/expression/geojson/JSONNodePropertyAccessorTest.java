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
package org.geotools.filter.expression.geojson;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.geojson.GeoJSONReaderTest;
import org.geotools.filter.expression.PropertyAccessor;
import org.geotools.filter.expression.SimpleFeaturePropertyAccessorFactory;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.node.StringNode;

public class JSONNodePropertyAccessorTest {

    SimpleFeatureType type;
    SimpleFeature feature;
    PropertyAccessor accessor = SimpleFeaturePropertyAccessorFactory.ATTRIBUTE_ACCESS;

    @Before
    public void setUp() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "multilevel.json");
        String jsonString =
                new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
        feature = GeoJSONReader.parseFeature(jsonString);
        accessor = JSONNodePropertyAccessorFactory.JSONNODEPROPERTY;
    }

    @Test
    public void testCanHandle() {
        // Root element is not an ObjectNode or ArrayNode
        Assert.assertFalse(accessor.canHandle(feature, "aString", null));
        Assert.assertTrue(accessor.canHandle(feature, "aThreeLevelObject/secondLevel", null));
    }

    @Test
    public void testGetMultiLevel() {
        Assert.assertEquals(
                Integer.valueOf(1), accessor.get(feature, "aThreeLevelObject/secondLevel/firstLevel", null));
        Assert.assertEquals(
                Double.valueOf(1.3), accessor.get(feature, "aThreeLevelObjectWithDouble/secondLevel/firstLevel", null));
        Assert.assertEquals(
                ObjectNode.class,
                accessor.get(feature, "aTwoLevelObjectNoChildren/firstLevel", null)
                        .getClass());
    }

    @Test
    public void testGetDateAsText() {
        Date date = accessor.get(feature, "aThreeLevelObject/secondLevel/firstLevelDate", null);
        Assert.assertEquals(1658180897000L, date.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrongPath() {
        accessor.get(feature, "aTwoLevelObject/secondLevel/firstLevel", null);
    }

    @Test
    public void testObjectsListsOutsideOfProperties() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "stac.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeature feature = reader.getFeature();

            assertEquals("1.0.0", ((StringNode) accessor.get(feature, "stac_version", null)).asString());
            assertEquals("simple-collection", ((StringNode) accessor.get(feature, "collection", null)).asString());
            assertEquals(
                    "https://stac-extensions.github.io/eo/v1.0.0/schema.json",
                    accessor.get(feature, "stac_extensions/0", null));
            assertEquals("./collection.json", accessor.get(feature, "links/0/href", null));
            assertEquals(
                    "https://storage.googleapis.com/open-cogs/stac-examples/20201211_223832_CS2.jpg",
                    accessor.get(feature, "assets/thumbnail/href", null));
        }
    }
}
