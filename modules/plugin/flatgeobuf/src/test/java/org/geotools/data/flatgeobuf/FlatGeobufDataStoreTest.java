package org.geotools.data.flatgeobuf;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.Test;

public class FlatGeobufDataStoreTest {
    @Test
    public void testLoad() throws Exception {
        URL url =
                getClass()
                        .getClassLoader()
                        .getResource("org/geotools/data/flatgeobuf/countries.fgb");
        FlatGeobufDataStore store = new FlatGeobufDataStore(url);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        SimpleFeatureCollection sfc = fs.getFeatures(Query.ALL);
        assertEquals(179, sfc.size());
    }
}
