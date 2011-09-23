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

import org.junit.Before;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Unit tests for ListFeatureCollection.
 * <p>
 * Note: this class doesn't extend {@linkplain org.geotools.data.DataTestCase} 
 * because we are working with JUnit 4 and because we want feature types with
 * CRS defined.
 * 
 * @author mbedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public class ListFeatureCollectionTest {
    private static final double TOL = 1.0e-8;
    
    private static final CoordinateReferenceSystem DEFAULT_CRS = 
            DefaultEngineeringCRS.CARTESIAN_2D;
    
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

    /**
     * Creates point features at each corner of the specified envelope
     * and puts them into the feature collection (features field).
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
            Coordinate c = new Coordinate(xy[i], xy[i+1]);
            fb.add(geomFactory.createPoint(c));
            fb.add(Integer.valueOf(i));
            featureList.add(fb.buildFeature(null));
        }
        
        featureCollection = new ListFeatureCollection(TYPE, featureList);
    }

    /**
     * Creates {@code count} point features positioned randomly in the 
     * given envelope.
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
    
    /**
     * Creates a point feature at position x,y.
     */
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
