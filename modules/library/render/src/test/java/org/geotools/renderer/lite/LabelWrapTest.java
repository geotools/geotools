package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.FeatureSource;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class LabelWrapTest extends TestCase {

    private static final long TIME = 10000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;

    @Override
    protected void setUp() throws Exception {
        bounds = new ReferencedEnvelope(0, 10, 0, 10, null);
        
//        System.setProperty("org.geotools.test.interactive", "true");
        
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("geom", Point.class);
        builder.add("label", String.class);
        builder.setName("labelWrap");
        SimpleFeatureType type = builder.buildFeatureType();
        
        GeometryFactory gf = new GeometryFactory();
        SimpleFeature f1 = SimpleFeatureBuilder.build(type, new Object[]{gf.createPoint(new Coordinate(5, 8)), "A long label, no newlines"}, null);
        SimpleFeature f2 = SimpleFeatureBuilder.build(type, new Object[]{gf.createPoint(new Coordinate(5, 4)), "A long label\nwith newlines"}, null);
        
        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(f1);
        data.addFeature(f2);
        fs = data.getFeatureSource("labelWrap");
        
    }
    
    public void testNoAutoWrap() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textWrapDisabled.sld");
        renderLabels(fs, style, "Label wrap disabled");
    }
    
    public void testAutoWrap() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textWrapEnabled.sld");
        renderLabels(fs, style, "Label wrap enabled");
    }

    private void renderLabels(FeatureSource fs, Style style, String title) throws Exception {
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        Map rendererParams = new HashMap();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);
        renderer.setContext(mc);
        
        RendererBaseTest.showRender(title, renderer, TIME, bounds);
    }
    
    
}
