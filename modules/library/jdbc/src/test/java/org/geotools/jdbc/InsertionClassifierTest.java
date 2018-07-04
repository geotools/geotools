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
package org.geotools.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class InsertionClassifierTest extends TestCase {

    private GeometryFactory geometryFactory = new GeometryFactory();

    public void testSegregateSimple() throws Exception {
        SimpleFeatureType featureType = buildType();
        Collection<SimpleFeature> features = new ArrayList<>();
        features.add(createFeature(featureType, "toto", createLineString(), createPolygon()));
        features.add(createFeature(featureType, "tutu", createLineString(), createPolygon()));
        Map<InsertionClassifier, Collection<SimpleFeature>> actual =
                InsertionClassifier.classify(featureType, features);
        assertEquals(1, actual.size());
        for (InsertionClassifier kind : actual.keySet()) {
            assertEquals(false, kind.useExisting);
            assertEquals(2, kind.geometryTypes.size());
            assertEquals(LineString.class, kind.geometryTypes.get("geom1"));
            assertEquals(Polygon.class, kind.geometryTypes.get("geom2"));
            assertEquals(2, actual.get(kind).size());
        }
    }

    public void testSegregateMultipleGeomKinds() throws Exception {
        SimpleFeatureType featureType = buildType();
        Collection<SimpleFeature> features = new ArrayList<>();
        features.add(createFeature(featureType, "toto", createLineString(), createPolygon()));
        features.add(createFeature(featureType, "tutu", createLineString(), createLineString()));
        Map<InsertionClassifier, Collection<SimpleFeature>> actual =
                InsertionClassifier.classify(featureType, features);
        assertEquals(2, actual.size());
        Set<Class<? extends Geometry>> geom2Classes = new HashSet<>();
        for (InsertionClassifier kind : actual.keySet()) {
            assertEquals(false, kind.useExisting);
            assertEquals(2, kind.geometryTypes.size());
            assertEquals(LineString.class, kind.geometryTypes.get("geom1"));
            geom2Classes.add(kind.geometryTypes.get("geom2"));
            assertEquals(1, actual.get(kind).size());
        }
        assertEquals(new HashSet<>(Arrays.asList(LineString.class, Polygon.class)), geom2Classes);
    }

    public void testSegregateUseExisting() throws Exception {
        SimpleFeatureType featureType = buildType();
        Collection<SimpleFeature> features = new ArrayList<>();
        SimpleFeature f2 = createFeature(featureType, "toto", createLineString(), createPolygon());
        f2.getUserData().put(Hints.USE_PROVIDED_FID, true);
        features.add(f2);
        features.add(createFeature(featureType, "tutu", createLineString(), createPolygon()));
        Map<InsertionClassifier, Collection<SimpleFeature>> actual =
                InsertionClassifier.classify(featureType, features);
        assertEquals(2, actual.size());
        Set<Boolean> uses = new HashSet<>();
        for (InsertionClassifier kind : actual.keySet()) {
            uses.add(kind.useExisting);
            assertEquals(2, kind.geometryTypes.size());
            assertEquals(LineString.class, kind.geometryTypes.get("geom1"));
            assertEquals(Polygon.class, kind.geometryTypes.get("geom2"));
            assertEquals(1, actual.get(kind).size());
        }
        assertEquals(new HashSet<>(Arrays.asList(Boolean.FALSE, Boolean.TRUE)), uses);
    }

    public void testSegregateNullGeom() throws Exception {
        SimpleFeatureType featureType = buildType();
        Collection<SimpleFeature> features = new ArrayList<>();
        features.add(createFeature(featureType, "toto", createLineString(), createPolygon()));
        features.add(createFeature(featureType, "tutu", createLineString(), null));
        Map<InsertionClassifier, Collection<SimpleFeature>> actual =
                InsertionClassifier.classify(featureType, features);
        assertEquals(2, actual.size());
        Set<Class<? extends Geometry>> geom2Classes = new HashSet<>();
        for (InsertionClassifier kind : actual.keySet()) {
            assertEquals(false, kind.useExisting);
            assertEquals(2, kind.geometryTypes.size());
            assertEquals(LineString.class, kind.geometryTypes.get("geom1"));
            geom2Classes.add(kind.geometryTypes.get("geom2"));
            assertEquals(1, actual.get(kind).size());
        }
        assertEquals(new HashSet<>(Arrays.asList(null, Polygon.class)), geom2Classes);
    }

    private Polygon createPolygon() {
        return createPolygon(0, 0, 1, 1, 2, 2, 0, 0);
    }

    private LineString createLineString() {
        return createLineString(0, 1, 2, 3);
    }

    private SimpleFeature createFeature(
            SimpleFeatureType featureType, String name, LineString geom1, Geometry geom2) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        builder.add(name);
        builder.add(geom1);
        builder.add(geom2);
        return builder.buildFeature(name);
    }

    private Polygon createPolygon(float... coords) {
        LinearRing shell = new LinearRing(new LiteCoordinateSequence(coords), geometryFactory);
        return new Polygon(shell, null, geometryFactory);
    }

    private LineString createLineString(float... coords) {
        return new LineString(new LiteCoordinateSequence(coords), geometryFactory);
    }

    private static SimpleFeatureType buildType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Test");
        builder.add("name", String.class);
        builder.add("geom1", LineString.class);
        builder.add("geom2", Geometry.class);
        return builder.buildFeatureType();
    }
}
