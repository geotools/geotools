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
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeobufFeatureTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void encodeDecode() throws Exception {

        File file = temporaryFolder.newFile("feature.pbf");

        SimpleFeatureType featureType =
                DataUtilities.createType("test2", "geom:Point,name:String,id:int");
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        WKTReader wkt = new WKTReader();
        featureBuilder.set("geom", wkt.read("POINT (1 2)"));
        featureBuilder.set("name", "Name");
        featureBuilder.set("id", 5);
        SimpleFeature feature = featureBuilder.buildFeature("point.1");

        GeobufFeature geobufFeature = new GeobufFeature(new GeobufGeometry());
        OutputStream out = new FileOutputStream(file);
        geobufFeature.encode(feature, out);
        out.close();
        InputStream inputStream = new FileInputStream(file);
        SimpleFeature decodedFeature = geobufFeature.decode(inputStream);
        inputStream.close();
        assertEquals(
                feature.getDefaultGeometry().toString(),
                decodedFeature.getDefaultGeometry().toString());
        assertEquals(feature.getAttribute("name"), decodedFeature.getAttribute("name"));
        assertEquals(feature.getAttribute("id"), decodedFeature.getAttribute("id"));
    }
}
