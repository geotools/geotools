package org.geotools.renderer.lite;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

import org.geotools.TestData;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.Style;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

public class RenderingTransformationTest {

    private static final long TIME = 4000;

    @Before
    public void setup() {
        // System.setProperty("org.geotools.test.interactive", "true");
    }

    @Test
    public void testTransformReprojectedGridReader() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "coverageCenter.sld");

        GeoTiffReader reader = new GeoTiffReader(TestData.copy(this, "geotiff/world.tiff"));

        MapContent mc = new MapContent();
        mc.addLayer(new GridReaderLayer(reader, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        
        ReferencedEnvelope reWgs84 = new ReferencedEnvelope(-70, 70, -160, 160, CRS.decode("EPSG:4326"));
        ReferencedEnvelope re = reWgs84.transform(CRS.decode("EPSG:3857"), true);

        BufferedImage image = RendererBaseTest.showRender("Lines with circle stroke", renderer,
                TIME, re);
        // if everything worked we are going to have a red dot in the middle of the map
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
        assertEquals(Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 2));
        assertEquals(Color.WHITE, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 4));
        assertEquals(Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 4));
    }
    
    @Test
    public void testTransformReprojectedGridCoverage() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "coverageCenter.sld");

        GeoTiffReader reader = new GeoTiffReader(TestData.copy(this, "geotiff/world.tiff"));

        MapContent mc = new MapContent();
        mc.addLayer(new GridCoverageLayer(reader.read(null), style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        
        ReferencedEnvelope reWgs84 = new ReferencedEnvelope(-70, 70, -160, 160, CRS.decode("EPSG:4326"));
        ReferencedEnvelope re = reWgs84.transform(CRS.decode("EPSG:3857"), true);

        BufferedImage image = RendererBaseTest.showRender("Lines with circle stroke", renderer,
                TIME, re);
        // if everything worked we are going to have a red dot in the middle of the map
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
        assertEquals(Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 2));
        assertEquals(Color.WHITE, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 4));
        assertEquals(Color.WHITE, getPixelColor(image, image.getWidth() / 4, image.getHeight() / 4));
    }
    
    @Test
    public void testTransformWithQueryNoInvert() throws Exception {
        testTransformWithQuery(false);
    }
    
    @Test
    public void testTransformWithQueryInvert() throws Exception {
        testTransformWithQuery(true);
    }


    private void testTransformWithQuery(boolean invert) throws IOException, URISyntaxException,
            CQLException, NoSuchAuthorityCodeException, FactoryException, Exception {
        // grab the style
        Style style = RendererBaseTest.loadStyle(this, invert ? "attributeRename.sld" : "attributeRenameNoInvert.sld");
        // grab the data
        File property = new File(TestData.getResource(this, "point.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        FeatureSource fs = ds.getFeatureSource("point");
        
        // prepare a feature layer with a query and the rendering tx
        FeatureLayer layer = new FeatureLayer(fs, style);
        layer.setQuery(new Query(null, CQL.toFilter("id > 5")));
        
        // render it
        MapContent mc = new MapContent();
        mc.addLayer(layer);
        StreamingRenderer renderer = new StreamingRenderer();
        final AtomicInteger counter = new AtomicInteger();
        renderer.addRenderListener(new RenderListener() {
            
            @Override
            public void featureRenderer(SimpleFeature feature) {
                counter.incrementAndGet();
            }
            
            @Override
            public void errorOccurred(Exception e) {
            }
        });
        renderer.setMapContent(mc);
        ReferencedEnvelope re = new ReferencedEnvelope(0, 12, 0, 12, CRS.decode("EPSG:4326"));
        BufferedImage image = RendererBaseTest.showRender("Lines with circle stroke", renderer,
                TIME, re);
        

        // if everything went fine we'll have a single red dot in the middle, and we rendered
        // just one feature
        assertEquals(1, counter.get());
        assertEquals(Color.RED, getPixelColor(image, image.getWidth() / 2, image.getHeight() / 2));
    }
    
    /**
     * Gets a specific pixel color from the specified buffered image
     * @param image
     * @param i
     * @param j
     * @param color
     * @return
     */
    protected Color getPixelColor(BufferedImage image, int i, int j) {
        ColorModel cm = image.getColorModel();
        Raster raster = image.getRaster();
        Object pixel = raster.getDataElements(i, j, null);
        
        Color actual;
        if(cm.hasAlpha()) {
            actual = new Color(cm.getRed(pixel), cm.getGreen(pixel), cm.getBlue(pixel), cm.getAlpha(pixel));
        } else {
            actual = new Color(cm.getRed(pixel), cm.getGreen(pixel), cm.getBlue(pixel), 255);
        }
        return actual;
    }
}
