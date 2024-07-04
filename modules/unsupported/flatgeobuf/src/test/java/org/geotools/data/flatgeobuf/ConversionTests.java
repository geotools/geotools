package org.geotools.data.flatgeobuf;

import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConversionTests {

    @Test
    public void testWriteIndexData() throws IOException {
        String shp = "C:\\deploy\\ware4u\\gisdata\\shapefile\\road2_local.shp";
        String fgb = "C:\\deploy\\ware4u\\gisdata\\FlatGeobuf\\road2_local2.fgb";
        File file = new File(shp);
        Map<String, Object> params = new HashMap<>();
        params.put("url", file.toURI().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(params);
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource sfs = dataStore.getFeatureSource(typeName);

        SimpleFeatureCollection sfc = sfs.getFeatures();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Conversion.serialize(sfc, os);
        byte[] byteArray = os.toByteArray();
        Files.write(Path.of(fgb), byteArray);
        os.close();
    }
}
