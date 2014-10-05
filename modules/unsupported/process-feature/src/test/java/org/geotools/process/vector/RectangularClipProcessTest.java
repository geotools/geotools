package org.geotools.process.vector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

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
        SimpleFeatureCollection result = cp.execute(features, new ReferencedEnvelope(0.0, 3339584.7, 0, 3503549.8, CRS.decode("EPSG:3857")), false);
        assertEquals(4, result.size());
    }
}
