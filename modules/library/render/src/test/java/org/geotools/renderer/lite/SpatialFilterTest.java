package org.geotools.renderer.lite;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.test.TestData;
import org.junit.Before;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;

public class SpatialFilterTest {

    private static final long TIME = 2000;

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    SimpleFeatureSource squareFS;

    ReferencedEnvelope bounds;

    StreamingRenderer renderer;

    MapContent content;

    int errorCount = 0;

    Set<String> renderedIds = new HashSet<String>();

    RenderListener listener;

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "square.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        squareFS = ds.getFeatureSource("square");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // prepare the renderer
        renderer = new StreamingRenderer();
        content = new MapContent();
        content.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

        renderer.addRenderListener(new RenderListener() {

            public void featureRenderer(SimpleFeature feature) {
                renderedIds.add(feature.getID());
            }

            public void errorOccurred(Exception e) {
                errorCount++;
            }
        });
    }

    public void testSpatialNoReprojection() throws Exception {
        // a spatial filter in the same SRS as the geometry
        StyleBuilder sb = new StyleBuilder();
        PolygonSymbolizer ps = sb.createPolygonSymbolizer();
        Style style = sb.createStyle(ps);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        rule.setFilter(ff.bbox("geom", 1, 1, 4, 4, "EPSG:4326)"));

        content.addLayer(new FeatureLayer(squareFS, style));

        RendererBaseTest.showRender("OneSquare", renderer, TIME, bounds);
        assertEquals(2, renderedIds.size());
    }
}
