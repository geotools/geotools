/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.feature.simple;

import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Point;

/**
 * Tests for {@link SimpleFeatureImpl}.
 *
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class SimpleFeatureTypeImplTest {

    /**
     * Test that properties returned by getAttributeDescriptors() and getDescriptors() are the same and in the same
     * order.
     */
    @Test
    public void testConsistentIterationOrder() {
        SimpleFeatureType type = buildLocationCountType();
        Assert.assertEquals(
                "FeatureType and SimpleFeatureType APIs must return the same descriptors in the " + "same order",
                new ArrayList<>(type.getAttributeDescriptors()),
                new ArrayList<>(type.getDescriptors()));
    }

    /** Test that property order is significant to equals(). */
    @Test
    public void testOrderSignificantEquals() {
        SimpleFeatureType type1 = buildLocationCountType();
        SimpleFeatureType type2 = buildCountLocationType();
        assertNotEquals(
                "Simple feature types with properties in a different order must not be " + "equal", type1, type2);
    }

    /** Test that distinct instances identically constructed are equal (location/count version). */
    @Test
    public void testLocationCountEquals() {
        Assert.assertEquals(
                "Identical simple feature types must be equal", buildLocationCountType(), buildLocationCountType());
    }

    /** Test that distinct instances identically constructed are equal (count/location version). */
    @Test
    public void testCountLocationEquals() {
        Assert.assertEquals(
                "Identical simple feature types must be equal", buildCountLocationType(), buildCountLocationType());
    }

    /**
     * Test that calls to getType(int) from multiple threads will not throw an IndexOutOfBoundsException due to poor
     * synchronization
     */
    @Test
    public void testGetTypeThreadSafety() {
        SimpleFeatureTypeBuilder builder = buildPartialBuilder();
        builder.add("location", Point.class, (CoordinateReferenceSystem) null);
        // add attributes to increase the time spend adding types to the array,
        // increasing the chance of a threading issue
        for (int i = 0; i < 100; i++) {
            builder.add("" + i, String.class);
        }
        SimpleFeatureType schema = builder.buildFeatureType();
        Assert.assertNotNull(schema);
        Assert.assertEquals(SimpleFeatureTypeImpl.class, schema.getClass());

        final CountDownLatch latch = new CountDownLatch(8);
        class Task implements Runnable {
            @Override
            public void run() {
                // this should return, then count down the latch
                // if there is an exception, the latch won't be triggered
                schema.getType(99);
                latch.countDown();
            }
        }

        final ExecutorService exec = Executors.newFixedThreadPool(8);

        for (int i = 0; i < 8; i++) {
            final Task task = new Task();
            exec.submit(task);
        }
        exec.shutdown();
        try {
            exec.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Assert.fail("Interrupted");
        }

        // verify all threads ran successfully and triggered the latch
        Assert.assertEquals(0, latch.getCount());
    }

    /** @return a simple feature type with location before count */
    private SimpleFeatureType buildLocationCountType() {
        SimpleFeatureTypeBuilder builder = buildPartialBuilder();
        builder.add("location", Point.class, (CoordinateReferenceSystem) null);
        builder.add("count", Integer.class);
        return builder.buildFeatureType();
    }

    /** @return a simple feature type with count before location */
    private SimpleFeatureType buildCountLocationType() {
        SimpleFeatureTypeBuilder builder = buildPartialBuilder();
        builder.add("count", Integer.class);
        builder.add("location", Point.class, (CoordinateReferenceSystem) null);
        return builder.buildFeatureType();
    }

    /** @return a simple feature type builder that is ready for the addition of location and count properties */
    private SimpleFeatureTypeBuilder buildPartialBuilder() {
        String uri = "http://example.org/things";
        FeatureTypeFactoryImpl typeFactory = new FeatureTypeFactoryImpl();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(typeFactory);
        builder.addBinding(typeFactory.createGeometryType(
                new NameImpl(uri, "description"),
                String.class,
                null,
                false,
                false,
                Collections.emptyList(),
                null,
                null));
        builder.addBinding(typeFactory.createGeometryType(
                new NameImpl(uri, "location"), Point.class, null, false, false, Collections.emptyList(), null, null));
        builder.addBinding(typeFactory.createAttributeType(
                new NameImpl(uri, "count"), Integer.class, false, false, Collections.emptyList(), null, null));
        builder.setName("ThingsType");
        builder.setNamespaceURI(uri);
        builder.add("description", String.class);
        return builder;
    }
}
