package org.geotools.geopkg;

import com.vividsolutions.jts.geom.Point;
import org.geotools.data.DataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class GeoPkgDataStoreFactoryTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder(new File("target"));

    @Test
    public void testBaseDirectory() throws IOException {
        Map<String,Serializable> map = new HashMap<>();
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
