package org.geotools.renderer.lite;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Tests the optimized data loading does merge the filters properly (was never released,
 * but a certain point in time only the first one was passed down to the datastore) 
 *
 * @source $URL$
 */
public class QueryOptimizeTest extends TestCase {
    
    private static final long TIME = 2000;
    
    SimpleFeatureSource squareFS;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;
    DefaultMapContext context;
    int count = 0;
    

    @Override
    protected void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "square.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        squareFS = ds.getFeatureSource("square");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
        renderer = new StreamingRenderer();
        context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        renderer.setContext(context);
        Map hints = new HashMap();
        hints.put("maxFiltersToSendToDatastore", 2);
        hints.put("optimizedDataLoadingEnabled", true);
        renderer.setRendererHints(hints);
                
//        System.setProperty("org.geotools.test.interactive", "true");
    }

    
    
    public void testLessFilters() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillSolidTwoRules.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(squareFS, style);
        
        renderer.setContext(mc);
        renderer.addRenderListener(new RenderListener() {
        
            public void featureRenderer(SimpleFeature feature) {
                count++;
            }
        
            public void errorOccurred(Exception e) {
            }
        });
        
        RendererBaseTest.showRender("OneSquare", renderer, TIME, bounds);
        assertEquals(2, count);
    }
}

