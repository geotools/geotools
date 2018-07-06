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
package org.geotools.data.geobuf;

import static org.junit.Assert.assertEquals;

import java.io.*;
import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeobufFeatureCollectionTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void encodeDecode() throws Exception {
        File file = temporaryFolder.newFile("featurecollection.pbf");

        MemoryDataStore store = new MemoryDataStore();
        SimpleFeatureType featureType =
                DataUtilities.createType("points", "geom:Point,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("points");
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            gf.createPoint(new Coordinate(-8.349609375, 14.349547837185362)),
                            "ABC",
                            1
                        },
                        "location.1");
        SimpleFeature feature2 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            gf.createPoint(new Coordinate(-18.349609375, 24.349547837185362)),
                            "DEF",
                            2
                        },
                        "location.2");
        SimpleFeatureCollection collection =
                DataUtilities.collection(new SimpleFeature[] {feature1, feature2});
        featureStore.addFeatures(collection);

        GeobufFeatureCollection geobufFeatureCollection = new GeobufFeatureCollection();
        OutputStream out = new FileOutputStream(file);
        geobufFeatureCollection.encode(collection, out);
        out.close();
        InputStream inputStream = new FileInputStream(file);
        SimpleFeatureCollection decodedFeatureCollection =
                geobufFeatureCollection.decode(inputStream);
        inputStream.close();

        assertEquals(2, decodedFeatureCollection.size());
        SimpleFeatureIterator it = decodedFeatureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                if (c == 0) {
                    assertEquals("POINT (-8.349609 14.349548)", f.getDefaultGeometry().toString());
                    assertEquals(1, f.getAttribute("id"));
                    assertEquals("ABC", f.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals("POINT (-18.349609 24.349548)", f.getDefaultGeometry().toString());
                    assertEquals(2, f.getAttribute("id"));
                    assertEquals("DEF", f.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
    }
}
