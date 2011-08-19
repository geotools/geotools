package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.test.TestData;
import org.junit.Test;

public class MultiLayerTest {

	@Test
	public void testRasterOpacity() throws Exception {
		// a polygon layer
        File property = new File(TestData.getResource(this, "buildings.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        SimpleFeatureSource fs = ds.getFeatureSource("buildings");
        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        StyleBuilder sb = new StyleBuilder();
        Style pst = sb.createStyle(sb.createPolygonSymbolizer(null, sb.createFill(Color.GRAY, 0.5)));
        
        // a raster layer
        BufferedImage bi = new BufferedImage(300, 300, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = bi.getGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 300, 300);
        g.dispose();
        GridCoverage2D coverage = new GridCoverageFactory().create("test_red", bi, bounds);
        
        Style rst = sb.createStyle(sb.createRasterSymbolizer());
		
		MapContext mc = new DefaultMapContext();
		mc.addLayer(new FeatureLayer(fs, pst));
		mc.addLayer(new GridCoverageLayer(coverage, rst));
		
		StreamingRenderer renderer = new StreamingRenderer();
		renderer.setContext(mc);
		BufferedImage img = RendererBaseTest.renderImage(renderer, bounds, null);
		
		// check the red image fully covered the vector (GEOT-3812)
		int[] pixel = new int[4];
		img.getData().getPixel(100, 100, pixel);
		assertEquals(255, pixel[0]);
		assertEquals(0, pixel[1]);
		assertEquals(0, pixel[2]);
		assertEquals(255, pixel[3]);
	}
}
