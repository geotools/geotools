/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * ValidatorTest<br>
 *
 * @author bowens<br>
 *     Created Jun 28, 2004<br>
 * @version <br>
 *     <b>Puropse:</b><br>
 *     <p><b>Description:</b><br>
 *     <p><b>Usage:</b><br>
 *     <p>
 */
public class ValidatorTest {
    TestFixture fixture;

    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        fixture = new TestFixture();
    }

    /*
     * @see TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        fixture = null;
    }

    @Test
    public void testRepositoryGeneration() throws Exception {
        // DefaultRepository dataRepository = new DefaultRepository();
        Assert.assertNotNull(fixture.repository.datastore("LAKES"));
        Assert.assertNotNull(fixture.repository.datastore("STREAMS"));
        Assert.assertNotNull(fixture.repository.datastore("SWAMPS"));
        Assert.assertNotNull(fixture.repository.datastore("RIVERS"));

        Assert.assertNotNull(fixture.repository.source("LAKES", "lakes"));
        Assert.assertNotNull(fixture.repository.source("STREAMS", "streams"));
        Assert.assertNotNull(fixture.repository.source("SWAMPS", "swamps"));
        Assert.assertNotNull(fixture.repository.source("RIVERS", "rivers"));
    }

    @Test
    public void testFeatureValidation() throws Exception {
        SimpleFeatureSource lakes = fixture.repository.source("LAKES", "lakes");
        SimpleFeatureCollection features = lakes.getFeatures();
        DefaultFeatureResults results = new DefaultFeatureResults();
        fixture.processor.runFeatureTests("LAKES", features, results);

        Assert.assertEquals("lakes test", 0, results.error.size());
    }

    public SimpleFeature createInvalidLake() throws Exception {
        SimpleFeatureSource lakes = fixture.repository.source("LAKES", "lakes");

        SimpleFeatureIterator features =
                lakes.getFeatures(new Query("lakes", Filter.INCLUDE, 1, (String[]) null, null))
                        .features();
        SimpleFeature feature = features.next();
        features.close();

        SimpleFeatureType LAKE = lakes.getSchema();
        Object array[] = new Object[LAKE.getAttributeCount()];
        for (int i = 0; i < LAKE.getAttributeCount(); i++) {
            AttributeDescriptor attr = LAKE.getDescriptor(i);
            // System.out.println( i+" "+attr.getType()+":"+attr.getName()+"="+feature.getAttribute(
            // i )  );
            if (LAKE.getGeometryDescriptor() == attr) {
                GeometryFactory factory = new GeometryFactory();
                Coordinate coords[] =
                        new Coordinate[] {
                            new Coordinate(1, 1),
                            new Coordinate(2, 2),
                            new Coordinate(2, 1),
                            new Coordinate(1, 2),
                            new Coordinate(1, 1),
                        };
                LinearRing ring = factory.createLinearRing(coords);
                Polygon poly = factory.createPolygon(ring, null);
                array[i] =
                        factory.createMultiPolygon(
                                new Polygon[] {
                                    poly,
                                });
            } else {
                array[i] = feature.getAttribute(i);
            }
        }
        return SimpleFeatureBuilder.build(LAKE, array, "splash");
    }

    @Test
    public void testFeatureValidation2() throws Exception {
        SimpleFeatureSource lakes = fixture.repository.source("LAKES", "lakes");
        SimpleFeature newFeature = createInvalidLake();

        SimpleFeatureCollection add =
                DataUtilities.collection(
                        new SimpleFeature[] {
                            newFeature,
                        });

        DefaultFeatureResults results = new DefaultFeatureResults();
        fixture.processor.runFeatureTests("LAKES", add, results);

        Assert.assertEquals("lakes test", 2, results.error.size());
    }

    @Test
    public void testIntegrityValidation() throws Exception {
        DefaultFeatureResults results = new DefaultFeatureResults();
        Set<Name> set = fixture.repository.getNames();

        Map<String, SimpleFeatureSource> map = new HashMap<>();

        for (Name name : set) {
            DataStore store = fixture.repository.dataStore(name);
            for (String typeName : store.getTypeNames()) {
                String typeRef = name.toString() + ":" + typeName;
                map.put(typeRef, fixture.repository.source(name.toString(), typeName));
            }
        }
        fixture.processor.runIntegrityTests(set, map, null, results);
        Assert.assertEquals("integrity test", 0, results.error.size());
    }

    @Test
    public void testValidator() throws Exception {
        Validator validator = new Validator(fixture.repository, fixture.processor);

        SimpleFeatureSource lakes = fixture.repository.source("LAKES", "lakes");
        SimpleFeatureCollection features = lakes.getFeatures();
        DefaultFeatureResults results = new DefaultFeatureResults();
        validator.featureValidation("LAKES", features, results);

        Assert.assertEquals(0, results.error.size());
    }

    @Test
    public void testValidator2() throws Exception {
        Validator validator = new Validator(fixture.repository, fixture.processor);

        SimpleFeatureSource lakes = fixture.repository.source("LAKES", "lakes");
        SimpleFeature newFeature = createInvalidLake();

        SimpleFeatureCollection add =
                DataUtilities.collection(
                        new SimpleFeature[] {
                            newFeature,
                        });
        DefaultFeatureResults results = new DefaultFeatureResults();
        fixture.processor.runFeatureTests("LAKES", add, results);

        // System.out.println(results.error);
        Assert.assertEquals("lakes test", 2, results.error.size());

        // results = new DefaultFeatureResults();
        validator.featureValidation("LAKES", add, results);
        Assert.assertEquals("lakes test2", 5, results.error.size());
    }

    @Test
    public void testIntegrityValidator() throws Exception {
        Validator validator = new Validator(fixture.repository, fixture.processor);

        DefaultFeatureResults results = new DefaultFeatureResults();
        Set<Name> set = fixture.repository.getNames();
        Map<Name, SimpleFeatureSource> map = new HashMap<>();

        for (Name name : set) {
            DataStore store = fixture.repository.dataStore(name);
            for (String typeName : store.getTypeNames()) {
                Name typeRef = new NameImpl(name.toString(), typeName);
                // String typeRef = name.toString()+":"+typeName;
                map.put(typeRef, fixture.repository.source(name.toString(), typeName));
            }
        }
        validator.integrityValidation(map, null, results);
        Assert.assertEquals("integrity test", 0, results.error.size());
    }

    @Test
    public void testIntegrityValidator2() throws Exception {
        Validator validator = new Validator(fixture.repository, fixture.processor);

        DefaultFeatureResults results = new DefaultFeatureResults();
        Set<String> set = new HashSet<>();
        Map<Name, SimpleFeatureSource> map = new HashMap<>();
        set.add("RIVERS:rivers");
        map.put(new NameImpl("RIVERS", "rivers"), fixture.repository.source("RIVERS", "rivers"));

        validator.integrityValidation(map, null, results);
        Assert.assertEquals("integrity test", 0, results.error.size());
    }
}
