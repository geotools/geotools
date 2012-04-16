package org.geotools.renderer.style;

import java.net.URL;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.lite.OpacityFinder;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;


public class OpacityFinderTest extends TestCase {

    public void testRasterOpacity() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL styleURL = TestData.getResource(this, "raster.sld");

        SLDParser stylereader = new SLDParser(factory, styleURL);

        Style style = stylereader.readXML()[0];

        OpacityFinder opacityFinder = new OpacityFinder(new Class[] { RasterSymbolizer.class });

        style.accept(opacityFinder);

        org.junit.Assert.assertTrue(opacityFinder.hasOpacity);
    }
    
    public void testColorMapOpacity() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL styleURL = TestData.getResource(this, "raster-cmalpha.sld");

        SLDParser stylereader = new SLDParser(factory, styleURL);

        Style style = stylereader.readXML()[0];

        OpacityFinder opacityFinder = new OpacityFinder(new Class[] { RasterSymbolizer.class });

        style.accept(opacityFinder);

        org.junit.Assert.assertTrue(opacityFinder.hasOpacity);
    }
}
