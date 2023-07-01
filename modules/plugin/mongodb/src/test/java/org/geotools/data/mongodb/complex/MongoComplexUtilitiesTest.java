/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb.complex;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import java.io.IOException;
import java.util.Collections;
import org.geotools.data.mongodb.MongoFeature;
import org.geotools.data.mongodb.MongoGeometryBuilder;
import org.geotools.data.mongodb.MongoTestSetup;
import org.geotools.data.mongodb.MongoTestSupport;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;

public abstract class MongoComplexUtilitiesTest extends MongoTestSupport {

    protected MongoComplexUtilitiesTest(MongoTestSetup testSetup) {
        super(testSetup);
    }

    /**
     * Checks no exception occurs when an empty list is evaluated by the complex utilities get value
     * method.
     */
    public void testGetValueEmptyList() throws IOException {
        final BasicDBList list = new BasicDBList();
        Object value =
                MongoComplexUtilities.getValue(list, Collections.emptyMap(), "path1.path2", null);
        // no exception thrown, the value is null
        assertNull(value);
    }

    public void testFeatureAttributeValueIsReturnedFromJsonPath()
            throws IOException, FactoryException, TransformException {
        // test that in case of a jsonpath can be resolved against the feature,
        // the feature value is picked up instead of the one from the DBObject
        SimpleFeatureSource source = dataStore.getFeatureSource("ft3");
        try (FeatureIterator<SimpleFeature> it = source.getFeatures().features()) {
            GeometryCoordinateSequenceTransformer transformer =
                    new GeometryCoordinateSequenceTransformer();
            CoordinateReferenceSystem original = source.getSchema().getCoordinateReferenceSystem();
            CoordinateReferenceSystem target = CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326");
            transformer.setMathTransform(CRS.findMathTransform(original, target, false));
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                // transforming the geometry so that the SimpleFeature geometry will be different
                // from
                // the one in the DBObject
                Geometry geom = transformer.transform((Geometry) f.getDefaultGeometry());
                f.setDefaultGeometry(geom);
                Geometry geometry2 = (Geometry) MongoComplexUtilities.getValue(f, "geometry");
                assertEquals(geom, geometry2);
                MongoFeature mongoFeature = (MongoFeature) f;
                Geometry dbGeom =
                        new MongoGeometryBuilder()
                                .toGeometry(
                                        (DBObject) mongoFeature.getMongoObject().get("geometry"));
                assertFalse(geometry2.equals(dbGeom));
            }
        }
    }

    public void testReprojectedValuesNotIgnored()
            throws IOException, FactoryException, TransformException, SchemaException {
        SimpleFeatureSource source = dataStore.getFeatureSource("ft3");
        try (FeatureIterator<SimpleFeature> it = source.getFeatures().features()) {
            GeometryCoordinateSequenceTransformer transformer =
                    new GeometryCoordinateSequenceTransformer();
            CoordinateReferenceSystem original = source.getSchema().getCoordinateReferenceSystem();
            CoordinateReferenceSystem target = CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326");
            transformer.setMathTransform(CRS.findMathTransform(original, target, false));
            SimpleFeatureType ft = FeatureTypes.transform(source.getSchema(), target);
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                Geometry geom = transformer.transform((Geometry) f.getDefaultGeometry());
                f.setDefaultGeometry(geom);
                // Mocking the case when a feature is reprojected
                // and the MongoFeature in userData is copied in the newly built feature
                SimpleFeature rep = SimpleFeatureBuilder.build(ft, f.getAttributes(), f.getID());
                if (f.hasUserData()) {
                    rep.getUserData().putAll(f.getUserData());
                }
                Geometry geometry2 = (Geometry) MongoComplexUtilities.getValue(rep, "geometry");
                assertEquals(geom, geometry2);
            }
        }
    }
}
