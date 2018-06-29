/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author tkunicki@boundlessgeo.com */
public class FeatureTypeDBObjectTest {

    public FeatureTypeDBObjectTest() {}

    @Test
    public void testRoundTripConversion()
            throws FileNotFoundException, IOException, FactoryException {

        SimpleFeatureType original = buildDummyFeatureType("dummy");

        DBObject dbo = FeatureTypeDBObject.convert(original);

        // make sure we're dealing with proper BSON/JSON by round-tripping it
        // through serialization...
        StringBuilder jsonBuffer = new StringBuilder();
        JSON.serialize(dbo, jsonBuffer);
        String json = jsonBuffer.toString();
        Object o = JSON.parse(json);
        assertThat(o, is(instanceOf(DBObject.class)));
        dbo = (DBObject) o;

        SimpleFeatureType result = FeatureTypeDBObject.convert(dbo);

        compareFeatureTypes(original, result, false);
    }

    @Test
    public void crsFromGeoJSON() {
        DBObject crsDBO = FeatureTypeDBObject.encodeCRSToGeoJSON(DefaultGeographicCRS.WGS84);

        CoordinateReferenceSystem result = FeatureTypeDBObject.decodeCRSFromGeoJSON(crsDBO);

        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, result));
    }

    static SimpleFeatureType buildDummyFeatureType(String typeName) throws FactoryException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(typeName);

        builder.userData("mapping", "geometry");
        builder.userData("encoding", "GeoJSON");
        builder.add("geometry", MultiPolygon.class, CRS.decode("EPSG:4269", true));

        builder.userData("mapping", "child.prop1");
        builder.add("propString", String.class);

        builder.userData("mapping", "child.prop2");
        builder.add("propBoolean", Boolean.class);

        builder.userData("mapping", "child.prop3");
        builder.add("propFloat", Float.class);

        builder.userData("mapping", "child.prop4");
        builder.add("propDouble", Double.class);

        builder.userData("mapping", "child.prop5");
        builder.add("propByte", Byte.class);

        builder.userData("mapping", "child.prop6");
        builder.add("propShort", Short.class);

        builder.userData("mapping", "child.prop7");
        builder.add("propInteger", Integer.class);

        builder.userData("mapping", "child.prop8");
        builder.add("propLong", Long.class);

        builder.userData("mapping", "child.prop9");
        builder.add("propDate", Date.class);

        SimpleFeatureType original = builder.buildFeatureType();
        original.getUserData().put("sample-key", "sample-value");

        return original;
    }

    static void compareFeatureTypes(
            SimpleFeatureType left, SimpleFeatureType right, boolean strictGeometryClass) {

        assertThat(right.getTypeName(), is(equalTo(left.getTypeName())));
        // verify feature type user data persisted
        Map<?, ?> resultUserData = right.getUserData();
        Map<?, ?> originalUserData = left.getUserData();
        assertThat(resultUserData.size(), is(equalTo(originalUserData.size())));
        for (Map.Entry entry : resultUserData.entrySet()) {
            assertThat(
                    entry.getValue(), (Matcher) is(equalTo(originalUserData.get(entry.getKey()))));
        }

        // verify we persist and restore same number of attributes
        assertThat(right.getAttributeCount(), is(equalTo(left.getAttributeCount())));

        // verify we persist and restore geometry name
        String rgdName = right.getGeometryDescriptor().getLocalName();
        assertThat(rgdName, is(equalTo(left.getGeometryDescriptor().getLocalName())));
        // verify we persist and restore CRS (this should always be WGS84 in the wild)
        assertTrue(
                "CRS are equal",
                CRS.equalsIgnoreMetadata(
                        right.getCoordinateReferenceSystem(), left.getCoordinateReferenceSystem()));

        if (strictGeometryClass) {
            assertThat(
                    right.getGeometryDescriptor().getType().getBinding().getSimpleName(),
                    is(
                            equalTo(
                                    left.getGeometryDescriptor()
                                            .getType()
                                            .getBinding()
                                            .getSimpleName())));
        } else {
            // NOTE!  Geometry type is generalized when persisted...
            assertThat(
                    Geometry.class.isAssignableFrom(
                            right.getGeometryDescriptor().getType().getBinding()),
                    is(equalTo(true)));
            assertThat(
                    Geometry.class.isAssignableFrom(
                            left.getGeometryDescriptor().getType().getBinding()),
                    is(equalTo(true)));
        }

        for (AttributeDescriptor rad : right.getAttributeDescriptors()) {
            String radName = rad.getLocalName();
            AttributeDescriptor oad = left.getDescriptor(radName);
            assertThat(rad.getMinOccurs(), is(equalTo(oad.getMinOccurs())));
            assertThat(rad.getMaxOccurs(), is(equalTo(oad.getMaxOccurs())));
            assertThat(rad.getDefaultValue(), is(equalTo(oad.getDefaultValue())));
            if (!radName.equals(rgdName)) {
                assertThat(
                        rad.getType().getBinding().getSimpleName(),
                        is(equalTo(oad.getType().getBinding().getSimpleName())));
            }
            Map<?, ?> radUserData = rad.getUserData();
            Map<?, ?> oadUserData = oad.getUserData();
            assertThat(radUserData.size(), is(equalTo(oadUserData.size())));
            for (Map.Entry entry : radUserData.entrySet()) {
                assertThat(
                        entry.getValue(), (Matcher) is(equalTo(oadUserData.get(entry.getKey()))));
            }
        }
    }
}
