/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;

/** Tests that measurements coordinates (M) are correctly handled. */
public abstract class JDBCMeasuredGeometriesOnlineTest extends JDBCTestSupport {

    public void testRetrievingPointM() {
        // get all the points
        List<SimpleFeature> features = getFeatures("points_m", Query.ALL);
        // each point coordinate should have dimension 3 and measures 1
        checkPointCoordinates(findOne(features, "description", "point_m_a"), 3, 1, -2, 1, 0.5);
        checkPointCoordinates(findOne(features, "description", "point_m_b"), 3, 1, 3, 1, -1);
        checkPointCoordinates(findOne(features, "description", "point_m_c"), 3, 1, 3, 5, 2);
        checkPointCoordinates(findOne(features, "description", "point_m_d"), 3, 1, -2, 5, -3.5);
    }

    public void testRetrievingPointZM() {
        // get all the points
        List<SimpleFeature> features = getFeatures("points_zm", Query.ALL);
        // each point coordinate should have dimension 4, elevation 1 and measures 1
        checkPointCoordinates(findOne(features, "description", "point_zm_a"), 4, 1, -2, 1, 10, 0.5);
        checkPointCoordinates(findOne(features, "description", "point_zm_b"), 4, 1, 3, 1, 15, -1);
        checkPointCoordinates(findOne(features, "description", "point_zm_c"), 4, 1, 3, 5, 20, 2);
        checkPointCoordinates(
                findOne(features, "description", "point_zm_d"), 4, 1, -2, 5, 25, -3.5);
    }

    public void testRetrievingLineM() {
        // get all the lines
        List<SimpleFeature> features = getFeatures("lines_m", Query.ALL);
        // each line coordinate should have dimension 3 and measures 1
        double[] ordinates = new double[] {-2, 1, 0.5, 3, 1, -1, 3, 5, 2, -2, 5, 3.5};
        checkLineCoordinates(findOne(features, "description", "line_m_a"), 3, 1, ordinates);
    }

    public void testRetrievingLineZM() {
        // get all the lines
        List<SimpleFeature> features = getFeatures("lines_zm", Query.ALL);
        // each line coordinate should should have dimension 4, elevation 1 and measures 1
        double[] ordinates =
                new double[] {-2, 1, 10, 0.5, 3, 1, 15, -1, 3, 5, 20, 2, -2, 5, 25, 3.5};
        checkLineCoordinates(findOne(features, "description", "line_zm_a"), 4, 1, ordinates);
    }

    /**
     * Checks that the provided feature default geometry is a linestring and has the expected
     * dimension, measures and ordinates.
     */
    private void checkLineCoordinates(
            SimpleFeature feature, int dimension, int measures, double... ordinates) {
        // get the feature point geometry
        Object candidate = feature.getDefaultGeometry();
        assertThat(candidate, instanceOf(LineString.class));
        LineString line = (LineString) candidate;
        // check the coordinate sequence dimension and measures
        CoordinateSequence coordinateSequence = line.getCoordinateSequence();
        assertThat(coordinateSequence.getDimension(), is(dimension));
        assertThat(coordinateSequence.getMeasures(), is(measures));
        // check the we have the expected ordinates
        List<Double> candidatesOrdinates = getOrdinates(coordinateSequence);
        assertThat(candidatesOrdinates.size(), is(ordinates.length));
        for (int i = 0; i < ordinates.length; i++) {
            assertThat(candidatesOrdinates.get(i), is(ordinates[i]));
        }
    }

    /**
     * Checks that the provided feature default geometry is a point and has the expected dimension,
     * measures and ordinates.
     */
    private void checkPointCoordinates(
            SimpleFeature feature, int dimension, int measures, double... ordinates) {
        // get the feature point geometry
        Object candidate = feature.getDefaultGeometry();
        assertThat(candidate, instanceOf(Point.class));
        Point point = (Point) candidate;
        // check the coordinate sequence dimension and measures
        CoordinateSequence coordinateSequence = point.getCoordinateSequence();
        assertThat(coordinateSequence.getDimension(), is(dimension));
        assertThat(coordinateSequence.getMeasures(), is(measures));
        // check the we have the expected ordinates
        List<Double> candidatesOrdinates = getOrdinates(coordinateSequence);
        assertThat(candidatesOrdinates.size(), is(ordinates.length));
        for (int i = 0; i < ordinates.length; i++) {
            assertThat(candidatesOrdinates.get(i), is(ordinates[i]));
        }
    }

    /** Helper method that converts an array of coordinates to an array of ordinates. */
    private List<Double> getOrdinates(CoordinateSequence coordinateSequence) {
        List<Double> ordinates = new ArrayList<>();
        for (Coordinate coordinate : coordinateSequence.toCoordinateArray()) {
            // let's get all the coordinates ordinates
            for (int i = 0; i < coordinateSequence.getDimension(); i++) {
                ordinates.add(coordinate.getOrdinate(i));
            }
        }
        return ordinates;
    }

    /** Helper method that dets the features of the provided table that match the provided query. */
    private List<SimpleFeature> getFeatures(String tableName, Query query) {
        try {
            ContentFeatureSource featureSource = dataStore.getFeatureSource(tname(tableName));
            SimpleFeatureCollection collection = featureSource.getFeatures(query);
            return getFeatures(collection);
        } catch (Exception exception) {
            // something bad happen, let's abort
            throw new RuntimeException(
                    String.format(
                            "Error reading features from table '%s' using query '%s'.",
                            tableName, query.toString()),
                    exception);
        }
    }

    /** Helper method that crestes a lsit of features from a collection (iterator) of features. */
    private List<SimpleFeature> getFeatures(SimpleFeatureCollection collection) {
        List<SimpleFeature> features = new ArrayList<>();
        try (SimpleFeatureIterator iterator = collection.features()) {
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
        }
        return features;
    }

    /**
     * Gets the features that match the provided attributes values and checks that only one feature
     * was found.
     */
    private SimpleFeature findOne(List<SimpleFeature> features, Object... attributesValues) {
        List<SimpleFeature> found = find(features, attributesValues);
        assertThat(found.size(), is(1));
        return found.get(0);
    }

    /** Gets all the features that match the provided attributes values. */
    private List<SimpleFeature> find(List<SimpleFeature> features, Object... attributesValues) {
        List<SimpleFeature> found = new ArrayList<>();
        for (SimpleFeature feature : features) {
            boolean matches = true;
            // check that this feature attributes match the provided attributes values
            for (int i = 0; i < attributesValues.length; i += 2) {
                String attribute = (String) attributesValues[i];
                Object value = feature.getAttribute(attribute);
                matches = Objects.equals(value, attributesValues[i + 1]);
            }
            if (matches) {
                // we got a match
                found.add(feature);
            }
        }
        return found;
    }
}
