/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Point;

public class GeoPkgDataStoreFactoryTest {

    @Rule public TemporaryFolder tmp = new TemporaryFolder(new File("target"));

    @Test
    public void testBaseDirectory() throws IOException {
        Map<String, Serializable> map = new HashMap<>();
        map.put(GeoPkgDataStoreFactory.DBTYPE.key, "geopkg");
        map.put(GeoPkgDataStoreFactory.DATABASE.key, "foo.gpkg");

        GeoPkgDataStoreFactory factory = new GeoPkgDataStoreFactory();
        factory.setBaseDirectory(tmp.getRoot());

        // create some data to trigger file creation
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName("foo");
        b.setNamespaceURI("http://geotools.org");
        b.setSRS("EPSG:4326");
        b.add("geom", Point.class);
        b.add("name", String.class);

        DataStore data = factory.createDataStore(map);
        data.createSchema(b.buildFeatureType());
        data.dispose();

        assertTrue(new File(tmp.getRoot(), "foo.gpkg").exists());
    }
}
