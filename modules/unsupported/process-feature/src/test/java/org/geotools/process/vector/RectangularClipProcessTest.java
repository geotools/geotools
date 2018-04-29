package org.geotools.process.vector;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RectangularClipProcessTest extends Assert {

    private SimpleFeatureSource fsPolylines;

    @Before
    public void setUp() throws Exception {
        PropertyDataStore store = new PropertyDataStore(TestData.file(this, ""));
        fsPolylines = store.getFeatureSource("polyline");
    }

    @Test
    public void testClipEnvelopeReprojection() throws Exception {
        SimpleFeatureCollection features = fsPolylines.getFeatures();
        RectangularClipProcess cp = new RectangularClipProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        features,
                        new ReferencedEnvelope(
                                0.0, 3339584.7, 0, 3503549.8, CRS.decode("EPSG:3857")),
                        false);
        assertEquals(4, result.size());
    }
}
