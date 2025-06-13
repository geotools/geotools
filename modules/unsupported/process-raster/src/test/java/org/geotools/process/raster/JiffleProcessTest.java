package org.geotools.process.raster;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.function.RenderingTransformation;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.image.WorldImageReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.MapContent;
import org.geotools.process.Process;
import org.geotools.process.Processors;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.test.TestData;
import org.geotools.xml.styling.SLDParser;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class JiffleProcessTest {

    private static final GridCoverageFactory covFactory = CoverageFactoryFinder.getGridCoverageFactory(null);
    private static final float[][] ONE_DIAGONAL = {
        {1, 0, 0},
        {0, 1, 0},
        {0, 0, 1},
    };

    private static final float[][] TWO_SOLID = {
        {2, 2, 2},
        {2, 2, 2},
        {2, 2, 2},
    };

    private GridCoverage2D buildCoverage(float[][] data) {
        return buildCoverage(
                data, new ReferencedEnvelope(0, data[0].length, 0, data.length, DefaultEngineeringCRS.CARTESIAN_2D));
    }

    private GridCoverage2D buildCoverage(float[][] data, ReferencedEnvelope envelope) {
        return covFactory.create("coverage", data, envelope);
    }

    float[][] data(GridCoverage2D cov, int band) {
        Raster data = cov.getRenderedImage().getData();
        int w = data.getWidth();
        int h = data.getHeight();

        float[] pixel = new float[data.getNumBands()];
        float[][] grid = new float[h][w];
        for (int r = 0; r < h; r++) {
            grid[r] = new float[w];
            for (int c = 0; c < w; c++) {
                data.getPixel(c, r, pixel);
                grid[r][c] = pixel[band];
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
        assertCopy(inputs -> {
            inputs.put(JiffleProcess.IN_SOURCE_NAME, "abc");
            inputs.put(JiffleProcess.IN_SCRIPT, "dest = abc;");
        });
    }

    @Test
    public void testCopyCustomInOut() {
        assertCopy(inputs -> {
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
        float[][] resultData = data(result, 0);
        assertArrayEquals(inputData, resultData);
        assertEquals("jiffle", result.getSampleDimensions()[0].getDescription().toString());
    }

    @Test
    public void testMultiply() {
        GridCoverage2D c1 = buildCoverage(ONE_DIAGONAL);
        GridCoverage2D c2 = buildCoverage(TWO_SOLID);

        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_SOURCE_NAME, new String[] {"a", "b"});
        inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {c1, c2});
        inputs.put(JiffleProcess.IN_SCRIPT, "dest = a * b;");
        Map<String, Object> output = jiffle.execute(inputs, null);
        GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);
        float[][] resultData = data(result, 0);

        float[][] expected = {
            {2, 0, 0},
            {0, 2, 0},
            {0, 0, 2},
        };
        assertArrayEquals(expected, resultData);

        // the two coverages have the same grid geometry, no need to resample them via
        // GridCoverage2DRIA
        List sources = ((RenderedOp) result.getRenderedImage()).getSources();
        assertEquals(c1.getRenderedImage(), sources.get(0));
        assertEquals(c2.getRenderedImage(), sources.get(1));
    }

    @Test
    public void testSumNoData() {
        // prepare a diagonal image with a nodata value set to 1
        GridCoverage2D diagonal = buildCoverage(ONE_DIAGONAL);
        RenderedImage ri = diagonal.getRenderedImage();
        PlanarImage pi = PlanarImage.wrapRenderedImage(ri);
        pi.setProperty(NoDataContainer.GC_NODATA, new NoDataContainer(1));
        GridCoverage2D c1 = covFactory.create("coverage", pi, diagonal.getEnvelope());
        // the second coverage is a 2 everywhere
        GridCoverage2D c2 = buildCoverage(TWO_SOLID);

        // now perform a sum with the two images
        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_SOURCE_NAME, new String[] {"a", "b"});
        inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {c1, c2});
        inputs.put(JiffleProcess.IN_SCRIPT, "dest = a + b;");
        Map<String, Object> output = jiffle.execute(inputs, null);
        GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);
        float[][] resultData = data(result, 0);

        float[][] expected = {
            {Float.NaN, 2, 2},
            {2, Float.NaN, 2},
            {2, 2, Float.NaN},
        };
        assertArrayEquals(expected, resultData);

        // the two coverages have the same grid geometry, no need to resample them via
        // GridCoverage2DRIA
        List sources = ((RenderedOp) result.getRenderedImage()).getSources();
        assertEquals(c1.getRenderedImage(), sources.get(0));
        assertEquals(c2.getRenderedImage(), sources.get(1));
    }

    @Test
    public void testNdviTransformation() throws Exception {
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

        // this test is a bit unfortunate, in that we cannot do a straight comparison of values
        // computed, but the StreamingRenderer has produced a BufferedImage in output, only colors
        File expected = new File("src/test/resources/org/geotools/process/raster/test-data/ndvi-expected.png");
        ImageAssert.assertEquals(expected, bi, 0);
    }

    @Test
    public void testNdviTransformationLowLevel() throws Exception {
        File file = TestData.file(this, "s2_13bands.tif");

        // grab the original coverage, compute NDVI directly from it
        GeoTiffReader reader = new GeoTiffReader(file);
        GridCoverage2D original = reader.read();
        int[] allBands = new int[13];
        original.getRenderedImage().getData().getPixel(0, 0, allBands);
        double ndviOriginal = (allBands[7] - allBands[3]) / (double) (allBands[7] + allBands[3]);

        // get the style, and the tx from it
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL styleURL = TestData.getResource(this, "ndvi.sld");
        SLDParser stylereader = new SLDParser(factory, styleURL);
        Style style = stylereader.readXML()[0];
        RenderingTransformation tx =
                (RenderingTransformation) style.featureTypeStyles().get(0).getTransformation();

        // check the bands have been passed down to the reader
        GeneralParameterValue[] params = tx.customizeReadParams(reader, new GeneralParameterValue[0]);
        assertEquals(1, params.length);
        assertArrayEquals(new int[] {3, 7}, (int[]) ((ParameterValue) params[0]).getValue());

        // read with band selection
        GridCoverage2D selected = reader.read(params);
        assertEquals(2, selected.getRenderedImage().getSampleModel().getNumBands());

        // perform calculation
        GridCoverage2D ndvi = (GridCoverage2D) tx.evaluate(selected);
        double[] ndviBands = new double[1];
        ndvi.getRenderedImage().getData().getPixel(0, 0, ndviBands);

        // the value computed thought band selection and script remapping matches the one
        // computed from
        assertEquals(ndviOriginal, ndviBands[0], 0d);
    }

    @Test
    public void testNdviTransformationLowLevelWorldImage() throws Exception {
        File file = TestData.file(this, "s2_13bands_wld.tif");

        // grab the original coverage, compute NDVI directly from it
        WorldImageReader reader = new WorldImageReader(file);
        GridCoverage2D original = reader.read();
        int[] allBands = new int[13];
        original.getRenderedImage().getData().getPixel(0, 0, allBands);
        double ndviOriginal = (allBands[7] - allBands[3]) / (double) (allBands[7] + allBands[3]);

        // get the style, and the tx from it
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL styleURL = TestData.getResource(this, "ndvi.sld");
        SLDParser stylereader = new SLDParser(factory, styleURL);
        Style style = stylereader.readXML()[0];
        RenderingTransformation tx =
                (RenderingTransformation) style.featureTypeStyles().get(0).getTransformation();

        // check the bands will not be passed to the reader
        GeneralParameterValue[] params = tx.customizeReadParams(reader, new GeneralParameterValue[0]);
        assertEquals(0, params.length);

        // perform calculation
        GridCoverage2D ndvi = (GridCoverage2D) tx.evaluate(original);
        double[] ndviBands = new double[1];
        ndvi.getRenderedImage().getData().getPixel(0, 0, ndviBands);

        // the value computed thought band selection and script remapping matches the one
        // computed from
        assertEquals(ndviOriginal, ndviBands[0], 0d);
    }

    @Test
    public void testMultiband() {
        GridCoverage2D c1 = buildCoverage(ONE_DIAGONAL);
        GridCoverage2D c2 = buildCoverage(TWO_SOLID);

        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_SOURCE_NAME, new String[] {"a", "b"});
        inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {c1, c2});
        inputs.put(JiffleProcess.IN_SCRIPT, "dest[0] = a; dest[1] = b; dest[2] = b - a;");
        Map<String, Object> output = jiffle.execute(inputs, null);
        GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);

        // compare results band by band, first band, input a
        assertMultibandDifference(result);

        // check band names
        GridSampleDimension[] sampleDimensions = result.getSampleDimensions();
        assertEquals("jiffle1", sampleDimensions[0].getDescription().toString());
        assertEquals("jiffle2", sampleDimensions[1].getDescription().toString());
        assertEquals("jiffle3", sampleDimensions[2].getDescription().toString());
    }

    @Test
    public void testMultibandCustomizeNames() {
        GridCoverage2D c1 = buildCoverage(ONE_DIAGONAL);
        GridCoverage2D c2 = buildCoverage(TWO_SOLID);

        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_SOURCE_NAME, new String[] {"a", "b"});
        inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {c1, c2});
        inputs.put(JiffleProcess.IN_SCRIPT, "dest[0] = a; dest[1] = b; dest[2] = b - a;");
        inputs.put(JiffleProcess.OUTPUT_BAND_NAMES, "a,b,difference");
        Map<String, Object> output = jiffle.execute(inputs, null);
        GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);
        assertMultibandDifference(result);

        // check band names
        GridSampleDimension[] sampleDimensions = result.getSampleDimensions();
        assertEquals("a", sampleDimensions[0].getDescription().toString());
        assertEquals("b", sampleDimensions[1].getDescription().toString());
        assertEquals("difference", sampleDimensions[2].getDescription().toString());
    }

    private void assertMultibandDifference(GridCoverage2D result) {
        // compare results band by band, first band, input a
        assertArrayEquals(ONE_DIAGONAL, data(result, 0));
        // second band, input b
        assertArrayEquals(TWO_SOLID, data(result, 1));
        // thid band, difference
        assertArrayEquals(
                new float[][] {
                    {1, 2, 2},
                    {2, 1, 2},
                    {2, 2, 1},
                },
                data(result, 2));
    }

    @Test
    public void testDynamicBandsCount() {
        GridCoverage2D coverage =
                checkMultibandIndexExpressions(inputs -> inputs.put(JiffleProcess.OUTPUT_BAND_COUNT, 3));

        // check band names
        GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
        assertEquals("jiffle1", sampleDimensions[0].getDescription().toString());
        assertEquals("jiffle2", sampleDimensions[1].getDescription().toString());
        assertEquals("jiffle3", sampleDimensions[2].getDescription().toString());
    }

    @Test
    public void testDynamicBandsNames() {
        GridCoverage2D coverage =
                checkMultibandIndexExpressions(inputs -> inputs.put(JiffleProcess.OUTPUT_BAND_NAMES, "o1,o2,o3"));

        // check band names
        GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
        assertEquals("o1", sampleDimensions[0].getDescription().toString());
        assertEquals("o2", sampleDimensions[1].getDescription().toString());
        assertEquals("o3", sampleDimensions[2].getDescription().toString());
    }

    @Test
    public void testDynamicBandsCountNames() {
        GridCoverage2D coverage = checkMultibandIndexExpressions(inputs -> {
            inputs.put(JiffleProcess.OUTPUT_BAND_COUNT, 3);
            inputs.put(JiffleProcess.OUTPUT_BAND_NAMES, "o1,o2");
        });

        // check band names
        GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
        assertEquals("o1", sampleDimensions[0].getDescription().toString());
        assertEquals("o2", sampleDimensions[1].getDescription().toString());
        assertEquals("jiffle3", sampleDimensions[2].getDescription().toString());
    }

    private GridCoverage2D checkMultibandIndexExpressions(Consumer<Map<String, Object>> paramProvider) {
        GridCoverage2D c1 = buildCoverage(ONE_DIAGONAL);
        GridCoverage2D c2 = buildCoverage(TWO_SOLID);

        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_SOURCE_NAME, new String[] {"a", "b"});
        inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {c1, c2});
        String script = "dest[x() % 2 == 0 ? 0 : 1] = a;\n" + "dest[x() % 2 == 0 ? 1 : 0] = b;\n" + "dest[2] = b - a;";
        inputs.put(JiffleProcess.IN_SCRIPT, script);
        paramProvider.accept(inputs);
        Map<String, Object> output = jiffle.execute(inputs, null);
        GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);

        // compare results band by band, has a where x is even, b where x is odd
        assertArrayEquals(
                new float[][] {
                    {1, 2, 0},
                    {0, 2, 0},
                    {0, 2, 1},
                },
                data(result, 0));
        // compare results band by band, has b where x is even, a where x is odd
        assertArrayEquals(
                new float[][] {
                    {2, 0, 2},
                    {2, 1, 2},
                    {2, 0, 2},
                },
                data(result, 1));
        // thid band, difference
        assertArrayEquals(
                new float[][] {
                    {1, 2, 2},
                    {2, 1, 2},
                    {2, 2, 1},
                },
                data(result, 2));

        return result;
    }

    @Test
    public void testMultibandBandIndexExpressionsInvalidBandCount() {
        GridCoverage2D c1 = buildCoverage(ONE_DIAGONAL);
        GridCoverage2D c2 = buildCoverage(TWO_SOLID);

        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_SOURCE_NAME, new String[] {"a", "b"});
        inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {c1, c2});
        String script = "dest[x() % 2 == 0 ? 0 : 1] = a;\n" + "dest[x() % 2 == 0 ? 1 : 0] = b;\n" + "dest[2] = b - a;";
        inputs.put(JiffleProcess.IN_SCRIPT, script);
        // band count mandatory, but we're feeding in the wrong value
        inputs.put(JiffleProcess.OUTPUT_BAND_COUNT, 2);
        try {
            Map<String, Object> output = jiffle.execute(inputs, null);
            GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);

            // need to force fetch the data in order to trigger actual computation
            result.getRenderedImage().getData();
            fail("Should not have reached here");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertThat(e.getMessage(), CoreMatchers.containsString("2"));
        }
    }

    @Test
    public void testDynamicInputBands() throws Exception {
        File file = TestData.file(CropCoverageTest.class, "s2_13bands.tif");
        GridCoverage2D coverage = new GeoTiffReader(file).read();

        Process jiffle = Processors.createProcess(new NameImpl("ras", "Jiffle"));
        Map<String, Object> inputs = new HashMap<>();
        inputs.put(JiffleProcess.IN_COVERAGE, new GridCoverage2D[] {coverage});
        String script =
                "maxBand = src->bands - 1;\n" + "foreach (b in 0:maxBand) {\n" + "dest[b] = src[b] * 2\n;" + "}";
        inputs.put(JiffleProcess.IN_SCRIPT, script);
        inputs.put(JiffleProcess.OUTPUT_BAND_COUNT, 13);
        Map<String, Object> output = jiffle.execute(inputs, null);
        GridCoverage2D result = (GridCoverage2D) output.get(JiffleProcess.OUT_RESULT);
        assertEquals(13, result.getNumSampleDimensions());

        double[] srcPixel = new double[13];
        double[] dstPixel = new double[13];
        coverage.getRenderedImage().getData().getPixel(0, 0, srcPixel);
        result.getRenderedImage().getData().getPixel(0, 0, dstPixel);
        for (int b = 0; b < 13; b++) {
            assertEquals(srcPixel[b] * 2, dstPixel[b], 0d);
        }
    }
}
