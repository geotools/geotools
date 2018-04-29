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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeobufFeatureTypeTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void encodeDecode() throws Exception {
        File file = temporaryFolder.newFile("featureType.pbf");
        SimpleFeatureType featureType =
                DataUtilities.createType("test2", "geom:Point,name:String,id:int");
        GeobufFeatureType geobufFeatureType = new GeobufFeatureType();
        OutputStream out = new FileOutputStream(file);
        geobufFeatureType.encode(featureType, out);
        out.close();
        InputStream inputStream = new FileInputStream(file);
        SimpleFeatureType decodedFeatureType = geobufFeatureType.decode("test2", inputStream);
        inputStream.close();
        // Without a feature, there is no way to know the type
        assertEquals(
                "geom:Geometry,name:String,id:String",
                DataUtilities.encodeType(decodedFeatureType));
    }
}
