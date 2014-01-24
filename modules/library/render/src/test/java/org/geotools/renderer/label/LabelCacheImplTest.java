package org.geotools.renderer.label;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Font;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class LabelCacheImplTest {

    private static final Geometry L3 = geometry("LINESTRING(20 20, 30 30)");

    private static final Geometry L2 = geometry("LINESTRING(10 10, 20 20)");

    private static final Geometry L1 = geometry("LINESTRING(0 0, 10 10)");

    private static final String LAYER_ID = "layerId";

    SimpleFeatureType schema;

    SimpleFeatureBuilder fb;

    LabelCacheImpl cache;

    StyleBuilder sb;

    NumberRange<Double> ALL_SCALES = new NumberRange<Double>(Double.class,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

    @Before
    public void setupSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("name", String.class);
        tb.add("geom", LineString.class, DefaultGeographicCRS.WGS84);
        schema = tb.buildFeatureType();
        fb = new SimpleFeatureBuilder(schema);
        cache = new LabelCacheImpl();
        cache.startLayer(LAYER_ID);
        sb = new StyleBuilder();
    }

    @Test
    public void testSimpleGrouping() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        ts.getOptions().put(TextSymbolizer.GROUP_KEY, "true");
        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        cache.put(LAYER_ID, ts, f1, new LiteShape2((Geometry) f1.getDefaultGeometry(), null, null,
                false), ALL_SCALES);
        cache.put(LAYER_ID, ts, f2, new LiteShape2((Geometry) f2.getDefaultGeometry(), null, null,
                false), ALL_SCALES);

        // we have just one
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(1, labels.size());
        LabelCacheItem item = labels.get(0);
        assertEquals("label1", item.getLabel());
        assertEquals(2, item.getGeoms().size());
    }

    @Test
    public void testGroupDifferentSymbolizers() throws Exception {
        TextSymbolizer ts1 = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        ts1.getOptions().put(TextSymbolizer.GROUP_KEY, "true");
        TextSymbolizer ts2 = sb.createTextSymbolizer(Color.YELLOW, (Font) null, "name");
        ts2.getOptions().put(TextSymbolizer.GROUP_KEY, "true");

        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        cache.put(LAYER_ID, ts1, f1, new LiteShape2((Geometry) f1.getDefaultGeometry(), null, null,
                false), ALL_SCALES);
        cache.put(LAYER_ID, ts2, f2, new LiteShape2((Geometry) f2.getDefaultGeometry(), null, null,
                false), ALL_SCALES);

        // two different symbolizers, we should have two
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(2, labels.size());
    }
    
    @Test
    public void testMinNonGrouped() throws Exception {
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, (Font) null, "name");
        TextSymbolizer tsGroup = sb.createTextSymbolizer(Color.YELLOW, (Font) null, "name");
        tsGroup.getOptions().put(TextSymbolizer.GROUP_KEY, "true");

        SimpleFeature f1 = createFeature("label1", L1);
        SimpleFeature f2 = createFeature("label1", L2);
        SimpleFeature f3 = createFeature("label1", L3);
        cache.put(LAYER_ID, tsGroup, f1, new LiteShape2((Geometry) f1.getDefaultGeometry(), null, null,
                false), ALL_SCALES);
        cache.put(LAYER_ID, ts, f2, new LiteShape2((Geometry) f2.getDefaultGeometry(), null, null,
                false), ALL_SCALES);
        cache.put(LAYER_ID, tsGroup, f3, new LiteShape2((Geometry) f3.getDefaultGeometry(), null, null,
                false), ALL_SCALES);

        // two different symbolizers, we should have two (the first grouped with the third)
        List<LabelCacheItem> labels = cache.getActiveLabels();
        assertEquals(2, labels.size());
        LabelCacheItem item1 = labels.get(0);
        assertEquals("label1", item1.getLabel());
        assertEquals(Arrays.asList(L1, L3), item1.getGeoms());
        LabelCacheItem item2 = labels.get(1);
        assertEquals("label1", item2.getLabel());
        assertEquals(Arrays.asList(L2), item2.getGeoms());
    }

    private SimpleFeature createFeature(String label, Geometry geom) {
        fb.add(label);
        fb.add(geom);
        return fb.buildFeature(null);
    }

    private static Geometry geometry(String wkt) {
        try {
            return new WKTReader().read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
