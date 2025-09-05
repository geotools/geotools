package org.geotools.data.flatgeobuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.test.TestData;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

public class FeatureCollectionConversionsTest {

    @Test
    public void countriesTest() throws IOException, URISyntaxException {
        URL url = TestData.url(FlatGeobufDataStore.class, "countries.fgb");
        File file = Paths.get(url.toURI()).toFile();
        try (InputStream stream = new FileInputStream(file)) {
            Iterator<SimpleFeature> it =
                    FeatureCollectionConversions.deserialize(stream).iterator();
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
        URL url = TestData.url(FlatGeobufDataStore.class, "countries.fgb");
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

    @Test
    public void countriesTestFilterFid() throws IOException, URISyntaxException {
        URL url = TestData.url(FlatGeobufDataStore.class, "countries.fgb");
        File file = Paths.get(url.toURI()).toFile();
        try (InputStream stream = new FileInputStream(file)) {
            long[] fids = {0, 1, 2, 45, 46, 178};
            Iterator<SimpleFeature> it =
                    FeatureCollectionConversions.deserialize(stream, fids).iterator();
            SimpleFeature simpleFeature = it.next();
            assertEquals("unknown.0", simpleFeature.getID());
            assertEquals("ATA", simpleFeature.getAttribute(1));
            simpleFeature = it.next();
            assertEquals("unknown.1", simpleFeature.getID());
            assertEquals("ATF", simpleFeature.getAttribute(1));
            simpleFeature = it.next();
            assertEquals("unknown.2", simpleFeature.getID());
            assertEquals("NAM", simpleFeature.getAttribute(1));
            simpleFeature = it.next();
            assertEquals("unknown.45", simpleFeature.getID());
            assertEquals("NLD", simpleFeature.getAttribute(1));
            simpleFeature = it.next();
            assertEquals("unknown.46", simpleFeature.getID());
            assertEquals("DNK", simpleFeature.getAttribute(1));
            simpleFeature = it.next();
            assertEquals("unknown.178", simpleFeature.getID());
            assertEquals("FLK", simpleFeature.getAttribute(1));
        }
    }
}
