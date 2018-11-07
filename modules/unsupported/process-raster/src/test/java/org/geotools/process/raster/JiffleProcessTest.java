package org.geotools.process.raster;

import it.geosolutions.jaiext.JAIExt;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.MapContent;
import org.geotools.process.Process;
import org.geotools.process.Processors;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;
import org.geotools.xml.styling.SLDParser;
import org.junit.Assert;
import org.junit.Test;

public class JiffleProcessTest {

    private static final GridCoverageFactory covFactory =
            CoverageFactoryFinder.getGridCoverageFactory(null);

    private GridCoverage2D buildCoverage(float[][] data) {
        return buildCoverage(
                data,
                new ReferencedEnvelope(
                        0, data[0].length, 0, data.length, DefaultEngineeringCRS.CARTESIAN_2D));
    }

    private GridCoverage2D buildCoverage(float[][] data, ReferencedEnvelope envelope) {
        return covFactory.create("coverage", data, envelope);
    }

    float[][] data(GridCoverage2D cov) {
        Raster data = cov.getRenderedImage().getData();
        int w = data.getWidth();
        int h = data.getHeight();

        float[] pixel = new float[1];
        float[][] grid = new float[h][w];
        for (int r = 0; r < h; r++) {
            grid[r] = new float[w];
            for (int c = 0; c < w; c++) {
                data.getPixel(c, r, pixel);
                grid[r][c] = pixel[0];
            }
        }

        return grid;
    }

    @Test
    public void testCopy() {
        assertCopy(inputs -> inputs.put(JiffleProcess.IN_SCRIPT, "dest = src;"));
    }

    @Test
    public void testCopyCustomInput() {
        assertCopy(
                inputs -> {
                    inputs.put(JiffleProcess.IN_SOURCE_NAME, "abc");
                    inputs.put(JiffleProcess.IN_SCRIPT, "dest = abc;");
                });
    }

    @Test
    public void testCopyCustomInOut() {
        assertCopy(
                inputs -> {
                    inputs.put(JiffleProcess.IN_DEST_NAME, "result");
                    inputs.put(JiffleProcess.IN_SOURCE_NAME, "abc");
                    inputs.put(JiffleProcess.IN_SCRIPT, "result = abc;");
                });
    }

    private void assertCopy(Consumer<Map<String, Object>> inputsCustomizer) {
        float[][] inputData = {
            {2, 2, 0, 3},
            {0, 2, 0, 0},
            {0, 2, 2, 2},
            {1, 0, 0, 2}
        };
        GridCoverage2D coverage = buildCoverage(inputData);

        // run process though Processors to check registration, input conversion and the like
        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_COVERAGE, coverage); // testing conversion here
        inputsCustomizer.accept(inputs);
        Map<String, Object> output = jiffle.execute(inputs, null);
        GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);
        float[][] resultData = data(result);
        Assert.assertArrayEquals(inputData, resultData);
    }

    @Test
    public void testMultiply() {
        GridCoverage2D c1 =
                buildCoverage(
                        new float[][] {
                            {1, 0, 0},
                            {0, 1, 0},
                            {0, 0, 1},
                        });
        GridCoverage2D c2 =
                buildCoverage(
                        new float[][] {
                            {2, 2, 2},
                            {2, 2, 2},
                            {2, 2, 2},
                        });

        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_SOURCE_NAME, new String[] {"a", "b"});
        inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {c1, c2});
        inputs.put(JiffleProcess.IN_SCRIPT, "dest = a * b;");
        Map<String, Object> output = jiffle.execute(inputs, null);
        GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);
        float[][] resultData = data(result);

        float[][] expected =
                new float[][] {
                    {2, 0, 0},
                    {0, 2, 0},
                    {0, 0, 2},
                };
        Assert.assertArrayEquals(expected, resultData);
    }

    @Test
    public void testNdviTransformation() throws Exception {
        JAIExt.initJAIEXT(true, true);

        File file = TestData.file(this, "s2_13bands.tif");

        GeoTiffReader reader = new GeoTiffReader(file);

        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);

        java.net.URL styleURL = TestData.getResource(this, "ndvi.sld");
        SLDParser stylereader = new SLDParser(factory, styleURL);
        Style style = stylereader.readXML()[0];

        MapContent mc = new MapContent();
        mc.addLayer(new GridReaderLayer(reader, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        BufferedImage bi = new BufferedImage(256, 256, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = bi.createGraphics();
        renderer.paint(
                graphics,
                new Rectangle(bi.getWidth(), bi.getHeight()),
                ReferencedEnvelope.reference(reader.getOriginalEnvelope()));
        graphics.dispose();

        File expected =
                new File(
                        "src/test/resources/org/geotools/process/raster/test-data/ndvi-expected.png");
        ImageAssert.assertEquals(expected, bi, 0);
    }
}
