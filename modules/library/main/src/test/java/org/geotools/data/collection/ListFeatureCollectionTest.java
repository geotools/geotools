/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Unit tests for ListFeatureCollection.
 *
 * <p>Note: this class doesn't extend {@linkplain org.geotools.data.DataTestCase} because we are
 * working with JUnit 4 and because we want feature types with CRS defined.
 *
 * @author mbedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public class ListFeatureCollectionTest {
    private static final double TOL = 1.0e-8;

    private static final CoordinateReferenceSystem DEFAULT_CRS = DefaultEngineeringCRS.CARTESIAN_2D;

    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(-10.0, 10.0, -5.0, 5.0, DEFAULT_CRS);

    private static final SimpleFeatureType TYPE = createType();
    private static final GeometryFactory geomFactory = new GeometryFactory();
    private static final Random rand = new Random();

    private SimpleFeatureBuilder fb;
    private ArrayList<SimpleFeature> featureList;
    private ListFeatureCollection featureCollection;

    @Before
    public void setup() {
        featureList = new ArrayList<SimpleFeature>();
        fb = new SimpleFeatureBuilder(TYPE);
    }

    @Test
    public void size() {
        createPointFeatures(WORLD, 42);
        assertEquals(42, featureCollection.size());
    }

    @Test
    public void clear() {
        createPointFeatures(WORLD, 42);
        featureCollection.clear();
        assertTrue(featureCollection.isEmpty());
    }

    @Test
    public void getBounds() {
        createPointFeaturesAtCorners(WORLD);
        ReferencedEnvelope bounds = featureCollection.getBounds();
        assertTrue(WORLD.boundsEquals2D(bounds, TOL));
        assertEquals(DEFAULT_CRS, bounds.getCoordinateReferenceSystem());
    }

    @Test
    public void addFeatureIncrementsSize() {
        createPointFeatures(WORLD, 1);
        featureCollection.add(createPointFeature(WORLD.getMinX(), WORLD.getMinY()));
        assertEquals(2, featureCollection.size());
    }

    @Test
    public void addFeatureExpandsBounds() {
        createPointFeaturesAtCorners(WORLD);
        double x = WORLD.getMaxX() + WORLD.getWidth();
        double y = WORLD.getMaxY() + WORLD.getHeight();
        featureCollection.add(createPointFeature(x, y));

        ReferencedEnvelope bounds = featureCollection.getBounds();
        assertEquals(x, bounds.getMaxX(), TOL);
        assertEquals(y, bounds.getMaxY(), TOL);
    }

    @Test
    public void iterator() {
        createPointFeaturesAtCorners(WORLD);
        SimpleFeatureIterator iter = featureCollection.features();
        assertNotNull(iter);
        assertTrue(iter.hasNext());

        List<SimpleFeature> copy = new ArrayList<SimpleFeature>(featureList);
        while (iter.hasNext()) {
            SimpleFeature f = iter.next();
            assertTrue(copy.remove(f));
        }
        assertTrue(copy.isEmpty());
    }

    /** Test for ticket GEOT-5684 Bounds cache was wrong after features were removed from list */
    @Test
    public void removeAndAddFeatureBounds() {
        // create test points
        createPointFeatures(WORLD, 3);
        // remove last feature in collection
        List<SimpleFeature> copy = new ArrayList<SimpleFeature>(featureList);
        SimpleFeature f = copy.get(2);
        featureCollection.remove(f);
        // get the new bounds (removed feature)
        ReferencedEnvelope postRemoveFeatureBounds = featureCollection.getBounds();
        // add new feature
        SimpleFeature newFeature = createPointFeature(10, 4);
        featureCollection.add(newFeature);
        // get new bounds
        ReferencedEnvelope newFeatureBounds = featureCollection.getBounds();
        // compare new bounds with old
        boolean isContained = newFeatureBounds.contains((Envelope) postRemoveFeatureBounds);
        assertTrue(isContained);
        // ensure new point bounds don't equal over-all bounds
        assertNotEquals(newFeatureBounds, newFeature.getBounds());
    }

    /** Test for ticket GEOT-5682 ListFeatureCollection handling of ReferencedEnvelope3D */
    @Test
    public void threeDimensionalFeatureBounds() {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // initialize FC with test features
        featureCollection = new ListFeatureCollection(TYPE, featureList);
        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {"testFeature1", gf.createPoint(new Coordinate(10, 20, 30))},
                        null);
        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {"testFeature2", gf.createPoint(new Coordinate(10, 10, 60))},
                        null);
        SimpleFeature f3 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {"testFeature2", gf.createPoint(new Coordinate(1, 10, 6))},
                        null);
        featureCollection.add(f1);
        featureCollection.add(f2);

        // get the bounds with two features, remove and add another feature
        ReferencedEnvelope origBounds = featureCollection.getBounds();
        featureCollection.remove(f2);
        featureCollection.add(f3);
        ReferencedEnvelope postRemoveBounds = featureCollection.getBounds();
        assertNotEquals(origBounds, postRemoveBounds);
        assertNotEquals(postRemoveBounds, f3.getBounds());
        assertTrue(postRemoveBounds.contains(f1.getBounds()));
        assertTrue(postRemoveBounds.contains(f3.getBounds()));
    }

    /**
     * Creates point features at each corner of the specified envelope and puts them into the
     * feature collection (features field).
     *
     * @param env bounding envelope
     */
    private void createPointFeaturesAtCorners(ReferencedEnvelope env) {
        double[] xy = {
            env.getMinX(), env.getMinY(),
            env.getMinX(), env.getMaxY(),
            env.getMaxX(), env.getMinY(),
            env.getMaxX(), env.getMaxY()
        };

        for (int i = 0; i < xy.length; i += 2) {
            Coordinate c = new Coordinate(xy[i], xy[i + 1]);
            fb.add(geomFactory.createPoint(c));
            fb.add(Integer.valueOf(i));
            featureList.add(fb.buildFeature(null));
        }

        featureCollection = new ListFeatureCollection(TYPE, featureList);
    }

    /**
     * Creates {@code count} point features positioned randomly in the given envelope.
     *
     * @param env bounding envelope
     * @param count number of features to create
     */
    private void createPointFeatures(ReferencedEnvelope env, int count) {
        for (int i = 0; i < count; i++) {
            double x = WORLD.getMinX() + rand.nextDouble() * WORLD.getWidth();
            double y = WORLD.getMinY() + rand.nextDouble() * WORLD.getHeight();
            featureList.add(createPointFeature(x, y));
        }
        featureCollection = new ListFeatureCollection(TYPE, featureList);
    }

    /** Creates a point feature at position x,y. */
    private SimpleFeature createPointFeature(double x, double y) {
        fb.add(geomFactory.createPoint(new Coordinate(x, y)));
        fb.add(Integer.valueOf(featureList.size() + 1));

        return fb.buildFeature(null);
    }

    private static SimpleFeatureType createType() {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("foo");
        typeBuilder.add("point", Point.class, DEFAULT_CRS);
        typeBuilder.add("id", Integer.class);
        return typeBuilder.buildFeatureType();
    }
}
