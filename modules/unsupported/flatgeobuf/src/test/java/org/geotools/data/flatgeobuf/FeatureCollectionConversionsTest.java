package org.geotools.data.flatgeobuf;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;

public class FeatureCollectionConversionsTest {
    @Test
    public void countriesTest() throws IOException, URISyntaxException {
        URL url =
                getClass()
                        .getClassLoader()
                        .getResource("org/geotools/data/flatgeobuf/countries.fgb");
        File file = Paths.get(url.toURI()).toFile();
        try (InputStream stream = new FileInputStream(file)) {
            Iterator<SimpleFeature> it =
                    FeatureCollectionConversions.deserialize(stream, null).iterator();
            int count = 0;
            while (it.hasNext()) {
                it.next();
                count++;
            }
            assertEquals(179, count);
        }
    }

    @Test
    public void countriesTestFilter() throws IOException, URISyntaxException {
        URL url =
                getClass()
                        .getClassLoader()
                        .getResource("org/geotools/data/flatgeobuf/countries.fgb");
        File file = Paths.get(url.toURI()).toFile();
        try (InputStream stream = new FileInputStream(file)) {
            Envelope rect = new Envelope(12, 12, 56, 56);
            Iterator<SimpleFeature> it =
                    FeatureCollectionConversions.deserialize(stream, rect).iterator();
            int count = 0;
            while (it.hasNext()) {
                it.next();
                count++;
            }
            assertEquals(3, count);
        }
    }
}
