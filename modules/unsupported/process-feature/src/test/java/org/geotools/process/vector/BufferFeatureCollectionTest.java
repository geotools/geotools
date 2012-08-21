/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import junit.framework.TestCase;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.vector.BufferFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.PrecisionModel;

public class BufferFeatureCollectionTest extends TestCase {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public void testExecutePoint() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Geometry.class);
        tb.add("integer", Integer.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        DefaultFeatureCollection features = new DefaultFeatureCollection(null, b.getFeatureType());
        for (int i = 0; i < 2; i++) {
            b.add(gf.createPoint(new Coordinate(i, i)));
            b.add(i);
            features.add(b.buildFeature(i + ""));
        }
        Double distance = new Double(500);
        BufferFeatureCollection process = new BufferFeatureCollection();
        SimpleFeatureCollection output = process.execute(features, distance, null);
        assertEquals(2, output.size());

        SimpleFeatureIterator iterator = output.features();
        for (int i = 0; i < 2; i++) {
            Geometry expected = gf.createPoint(new Coordinate(i, i)).buffer(distance);
            SimpleFeature sf = iterator.next();
            assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
        }
        
        assertEquals(new ReferencedEnvelope(-500, 501, -500, 501, null), output.getBounds());
        assertEquals(2, output.size());
    }

    public void testExecuteLineString() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Geometry.class);
        tb.add("integer", Integer.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        DefaultFeatureCollection features = new DefaultFeatureCollection(null, b.getFeatureType());
        for (int numFeatures = 0; numFeatures < 5; numFeatures++) {
            Coordinate array[] = new Coordinate[4];
            int j = 0;
            for (int i = 0 + numFeatures; i < 4 + numFeatures; i++) {
                array[j] = new Coordinate(i, i);
                j++;
            }
            b.add(gf.createLineString(array));
            b.add(0);
            features.add(b.buildFeature(numFeatures + ""));
        }
        Double distance = new Double(500);
        BufferFeatureCollection process = new BufferFeatureCollection();
        SimpleFeatureCollection output = process.execute(features, distance, null);
        assertEquals(5, output.size());
        SimpleFeatureIterator iterator = output.features();
        for (int numFeatures = 0; numFeatures < 5; numFeatures++) {
            Coordinate[] array = new Coordinate[4];
            int j = 0;
            for (int i = 0 + numFeatures; i < 4 + numFeatures; i++) {
                array[j] = new Coordinate(i, i);
                j++;
            }
            Geometry expected = gf.createLineString(array).buffer(distance);
            SimpleFeature sf = iterator.next();
            assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
        }
        
        assertEquals(new ReferencedEnvelope(-500, 507, -500, 507, null), output.getBounds());
        assertEquals(5, output.size());
    }

    public void testExecutePolygon() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Geometry.class);
        tb.add("integer", Integer.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        DefaultFeatureCollection features = new DefaultFeatureCollection(null, b.getFeatureType());
        for (int numFeatures = 0; numFeatures < 5; numFeatures++) {
            Coordinate array[] = new Coordinate[4];
            int j = 0;
            for (int i = 0 + numFeatures; i < 3 + numFeatures; i++) {
                array[j] = new Coordinate(i, i);
                j++;
            }
            array[3] = new Coordinate(numFeatures, numFeatures);
            LinearRing shell = new LinearRing(array, new PrecisionModel(), 0);
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            features.add(b.buildFeature(numFeatures + ""));
        }
        Double distance = new Double(500);
        BufferFeatureCollection process = new BufferFeatureCollection();
        SimpleFeatureCollection output = process.execute(features, distance, null);
        assertEquals(5, output.size());
        SimpleFeatureIterator iterator = output.features();
        for (int numFeatures = 0; numFeatures < 5; numFeatures++) {
            Coordinate[] array = new Coordinate[4];
            int j = 0;
            for (int i = 0 + numFeatures; i < 3 + numFeatures; i++) {
                array[j] = new Coordinate(i, i);
                j++;
            }
            array[3] = new Coordinate(numFeatures, numFeatures);
            LinearRing shell = new LinearRing(array, new PrecisionModel(), 0);
            Geometry expected = gf.createPolygon(shell, null).buffer(distance);

            SimpleFeature sf = iterator.next();
            assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
        }
        
        assertEquals(new ReferencedEnvelope(-500, 506, -500, 506, null), output.getBounds());
        assertEquals(5, output.size());
    }

    public void testExecuteBufferAttribute() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Geometry.class);
        tb.add("integer", Integer.class);
        tb.add("buffer", Double.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        ListFeatureCollection features = new ListFeatureCollection(b.getFeatureType());
        for (int numFeatures = 0; numFeatures < 5; numFeatures++) {
            Coordinate array[] = new Coordinate[4];
            int j = 0;
            for (int i = 0 + numFeatures; i < 3 + numFeatures; i++) {
                array[j] = new Coordinate(i, i);
                j++;
            }
            array[3] = new Coordinate(numFeatures, numFeatures);
            LinearRing shell = new LinearRing(array, new PrecisionModel(), 0);
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            b.add(numFeatures + 1);
            features.add(b.buildFeature(numFeatures + ""));
        }

        BufferFeatureCollection process = new BufferFeatureCollection();
        SimpleFeatureCollection output = process.execute(features, null, "buffer");
        assertEquals(5, output.size());
        SimpleFeatureIterator iterator = output.features();
        ReferencedEnvelope expectedBounds = new ReferencedEnvelope(output.getSchema().getCoordinateReferenceSystem());
        for (int numFeatures = 0; numFeatures < 5; numFeatures++) {
            Coordinate[] array = new Coordinate[4];
            int j = 0;
            for (int i = 0 + numFeatures; i < 3 + numFeatures; i++) {
                array[j] = new Coordinate(i, i);
                j++;
            }
            array[3] = new Coordinate(numFeatures, numFeatures);
            LinearRing shell = new LinearRing(array, new PrecisionModel(), 0);
            Geometry expected = gf.createPolygon(shell, null).buffer(numFeatures + 1);
            expectedBounds.expandToInclude(expected.getEnvelopeInternal());

            SimpleFeature sf = iterator.next();
            assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
        }
        
        assertEquals(expectedBounds, output.getBounds());
        assertEquals(5, output.size());
    }
}
