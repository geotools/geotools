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

import static org.junit.Assert.*;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.vector.UnionFeatureCollection;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 
 *
 * @source $URL$
 */
public class UnionFeatureCollectionTest  {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testExecute() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Geometry.class);
        tb.add("integer", Integer.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        DefaultFeatureCollection features = new DefaultFeatureCollection(null, b.getFeatureType());
        DefaultFeatureCollection secondFeatures = new DefaultFeatureCollection(null, b
                .getFeatureType());
        Geometry[] firstArrayGeometry = new Geometry[5];
        Geometry[] secondArrayGeometry = new Geometry[5];
        for (int numFeatures = 0; numFeatures < 5; numFeatures++) {
            Coordinate firstArray[] = new Coordinate[5];
            for (int j = 0; j < 4; j++) {
                firstArray[j] = new Coordinate(j + numFeatures, j + numFeatures);
            }
            firstArray[4] = new Coordinate(0 + numFeatures, 0 + numFeatures);
            LinearRing shell = new LinearRing(firstArray, new PrecisionModel(), 0);
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            firstArrayGeometry[numFeatures] = gf.createPolygon(shell, null);
            features.add(b.buildFeature(numFeatures + ""));

        }
        for (int numFeatures = 0; numFeatures < 5; numFeatures++) {
            Coordinate array[] = new Coordinate[5];
            for (int j = 0; j < 4; j++) {
                array[j] = new Coordinate(j + numFeatures + 50, j + numFeatures + 50);
            }
            array[4] = new Coordinate(0 + numFeatures + 50, 0 + numFeatures + 50);
            LinearRing shell = new LinearRing(array, new PrecisionModel(), 0);
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            secondArrayGeometry[numFeatures] = gf.createPolygon(shell, null);
            secondFeatures.add(b.buildFeature(numFeatures + ""));

        }
        UnionFeatureCollection process = new UnionFeatureCollection();
        SimpleFeatureCollection output = process.execute(features, secondFeatures);
        assertEquals(5, output.size());

        Geometry[] union = new Geometry[10];
        for (int i = 0; i < firstArrayGeometry.length; i++) {
            union[i] = firstArrayGeometry[i];
        }
        for (int i = 0; i < secondArrayGeometry.length; i++) {
            union[i + 5] = secondArrayGeometry[i];
        }
        GeometryCollection unionCollection = new GeometryCollection(union, new GeometryFactory());
        SimpleFeatureIterator iterator = output.features();

        for (int h = 0; h < unionCollection.getNumGeometries(); h++) {
            Geometry expected = (Geometry) unionCollection.getGeometryN(h);
            SimpleFeature sf = iterator.next();
            assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
        }
    }
}
