package org.geotools.renderer.lite;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;

public class SpatialFilterTest {

    private static final long TIME = 2000;

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    SimpleFeatureSource squareFS;

    ReferencedEnvelope bounds;

    StreamingRenderer renderer;

    MapContext context;

    int errorCount = 0;

    Set<String> renderedIds = new HashSet<String>();

    RenderListener listener;

    SimpleFeatureSource pointFS;

    @Before
    public void setUp() throws Exception {
        CRS.reset("all");
        Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        
        // the following is only to make the test work in Eclipse, where the test
        // classpath is tainted by the test classpath of dependent modules (whilst in Maven it's not)
        Set<CRSAuthorityFactory> factories = ReferencingFactoryFinder.getCRSAuthorityFactories(null);
        for (CRSAuthorityFactory factory : factories) {
            if(factory.getClass().getSimpleName().equals("EPSGCRSAuthorityFactory")) {
                ReferencingFactoryFinder.removeAuthorityFactory(factory);
            }
        }
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(CRS.decode("urn:ogc:def:crs:EPSG::4326")));
        
        // setup data
        File property = new File(TestData.getResource(this, "square.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        squareFS = ds.getFeatureSource("square");
        pointFS = ds.getFeatureSource("point");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // prepare the renderer
        renderer = new StreamingRenderer();
        context = new DefaultMapContext(DefaultGeographicCRS.WGS84);

        renderer.setContext(context);
        renderer.addRenderListener(new RenderListener() {

            public void featureRenderer(SimpleFeature feature) {
                renderedIds.add(feature.getID());
            }

            public void errorOccurred(Exception e) {
                errorCount++;
            }
        });
        
      // System.setProperty("org.geotools.test.interactive", "true");
    }
    
    @After
    public void tearDown() {
        Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
    }

    @Test
    public void testSpatialNoReprojection() throws Exception {
        // a spatial filter in the same SRS as the geometry
        StyleBuilder sb = new StyleBuilder();
        PolygonSymbolizer ps = sb.createPolygonSymbolizer();
        Style style = sb.createStyle(ps);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        rule.setFilter(ff.bbox("geom", 1, 1, 4, 4, "EPSG:4326"));

        context.addLayer(new FeatureLayer(squareFS, style));

        RendererBaseTest.showRender("Spatial with default CRS", renderer, TIME, bounds);
        assertEquals(2, renderedIds.size());
    }
    
    @Test
    public void testSpatialDefaulter() throws Exception {
        // a spatial filter in the same SRS as the geometry
        StyleBuilder sb = new StyleBuilder();
        PolygonSymbolizer ps = sb.createPolygonSymbolizer();
        Style style = sb.createStyle(ps);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        rule.setFilter(ff.bbox("geom", 1, 1, 4, 4, null));

        context.addLayer(new FeatureLayer(squareFS, style));

        RendererBaseTest.showRender("Spatial without CRS", renderer, TIME, bounds);
        assertEquals(2, renderedIds.size());
    }
    
    @Test
    public void testSpatialDefaulterForceEPSG() throws Exception {
        // a spatial filter in the same SRS as the geometry... but with a different axis order
        // interpretation, if we assume lat/lon we should pick point.4 
        StyleBuilder sb = new StyleBuilder();
        Symbolizer ps = sb.createPointSymbolizer();
        Style style = sb.createStyle(ps);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        rule.setFilter(ff.bbox("geom", 5, 1, 7, 3, null));
        
        // force EPSG axis order interpretation
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.FORCE_EPSG_AXIS_ORDER_KEY, true));

        context.addLayer(new FeatureLayer(pointFS, style));

        RendererBaseTest.showRender("Spatial in EPSG order", renderer, TIME, bounds);
        assertEquals(1, renderedIds.size());
        assertEquals("point.4", renderedIds.iterator().next());
    }
    
    @Test
    public void testReprojectedBBOX() throws Exception {
        // a spatial filter in a different SRS
        CoordinateReferenceSystem utm31n = CRS.decode("EPSG:32631");
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326");
        ReferencedEnvelope envWgs84 = new ReferencedEnvelope(1, 3, 5, 7, wgs84);
        ReferencedEnvelope envUTM31N = envWgs84.transform(utm31n, true);
        
        StyleBuilder sb = new StyleBuilder();
        Symbolizer ps = sb.createPointSymbolizer();
        Style style = sb.createStyle(ps);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        rule.setFilter(ff.bbox("geom", envUTM31N.getMinX(), envUTM31N.getMinY(), envUTM31N.getMaxX(), envUTM31N.getMaxY(), "EPSG:32631"));
        
        // force EPSG axis order interpretation
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.FORCE_EPSG_AXIS_ORDER_KEY, true));

        context.addLayer(new FeatureLayer(pointFS, style));

        RendererBaseTest.showRender("Spatial in EPSG order", renderer, TIME, bounds);
        assertEquals(1, renderedIds.size());
        assertEquals("point.4", renderedIds.iterator().next());
    }
    
    @Test
    public void testReprojectedPolygon() throws Exception {
        // a spatial filter in a different SRS
        CoordinateReferenceSystem utm31n = CRS.decode("EPSG:32631");
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326");
        ReferencedEnvelope envWgs84 = new ReferencedEnvelope(1, 3, 5, 7, wgs84);
        ReferencedEnvelope envUTM31N = envWgs84.transform(utm31n, true);
        
        StyleBuilder sb = new StyleBuilder();
        Symbolizer ps = sb.createPointSymbolizer();
        Style style = sb.createStyle(ps);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        Polygon polygon = JTS.toGeometry((Envelope) envUTM31N);
        polygon.setUserData(utm31n);
        rule.setFilter(ff.intersects(ff.property("geom"), ff.literal(polygon)));
        
        // force EPSG axis order interpretation
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.FORCE_EPSG_AXIS_ORDER_KEY, true));

        context.addLayer(new FeatureLayer(pointFS, style));

        RendererBaseTest.showRender("Spatial in EPSG order", renderer, TIME, bounds);
        assertEquals(1, renderedIds.size());
        assertEquals("point.4", renderedIds.iterator().next());
    }
    
    @Test
    public void testReprojectedPolygonFromSLD() throws Exception {
        // same as above, but with the style in SLD form
        Style style = RendererBaseTest.loadStyle(this, "spatialFilter.sld");
        
        // force EPSG axis order interpretation
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.FORCE_EPSG_AXIS_ORDER_KEY, true));

        context.addLayer(new FeatureLayer(pointFS, style));

        RendererBaseTest.showRender("Spatial in EPSG order", renderer, TIME, bounds);
        assertEquals(1, renderedIds.size());
        assertEquals("point.4", renderedIds.iterator().next());
    }
    
    
    
    
}
