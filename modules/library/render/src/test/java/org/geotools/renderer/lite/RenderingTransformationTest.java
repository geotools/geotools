package org.geotools.renderer.lite;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.styling.Style;
import org.geotools.TestData;
import org.junit.Before;
import org.junit.Test;

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
