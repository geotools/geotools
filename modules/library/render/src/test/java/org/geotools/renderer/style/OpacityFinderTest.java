package org.geotools.renderer.style;

import java.net.URL;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.lite.OpacityFinder;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;
import org.geotools.xml.styling.SLDParser;
import org.junit.Test;

public class OpacityFinderTest {

    @Test
    public void testRasterOpacity() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL styleURL = TestData.getResource(this, "raster.sld");

        SLDParser stylereader = new SLDParser(factory, styleURL);

        Style style = stylereader.readXML()[0];

        OpacityFinder opacityFinder = new OpacityFinder(new Class[] {RasterSymbolizer.class});

        style.accept(opacityFinder);

        org.junit.Assert.assertTrue(opacityFinder.hasOpacity);
    }

    @Test
    public void testColorMapOpacity() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL styleURL = TestData.getResource(this, "raster-cmalpha.sld");

        SLDParser stylereader = new SLDParser(factory, styleURL);

        Style style = stylereader.readXML()[0];

        OpacityFinder opacityFinder = new OpacityFinder(new Class[] {RasterSymbolizer.class});

        style.accept(opacityFinder);

        org.junit.Assert.assertTrue(opacityFinder.hasOpacity);
    }
}
