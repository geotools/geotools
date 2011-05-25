/**
 * 
 */
package org.geotools.arcsde.raster.gce;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RecyclingTileFactory;
import javax.media.jai.TileCache;

import org.geotools.arcsde.ArcSDERasterFormatFactory;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.jai.Registry;
import org.geotools.parameter.Parameter;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import com.vividsolutions.jts.util.Stopwatch;

/**
 * Tests over legacy data that should not be deleted
 * 
 *
 * @source $URL$
 */
@SuppressWarnings("nls")
public class ArcSDEGridCoverage2DReaderJAILegacyOnlineTest {

    private static final String RASTER_TEST_DEBUG_TO_DISK = "raster.test.debugToDisk";

    private static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.gce");

    /**
     * Whether to write the fetched rasters to disk or not
     */
    private static boolean DEBUG;

    static RasterTestData rasterTestData;

    private static String tableName;

    /**
     * If false, {@link #writeToDisk(RenderedImage, String)} is gonna write to a NULL OutputStream
     * instead of to a File
     */
    private boolean doWriteToDisk = true;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        rasterTestData = new RasterTestData();
        rasterTestData.setUp();
        DEBUG = Boolean
                .valueOf(rasterTestData.getRasterTestDataProperty(RASTER_TEST_DEBUG_TO_DISK));
        // rasterTestData.setOverrideExistingTestTables(false);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        rasterTestData.tearDown();
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // nothing to do
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // try {
        // LOGGER.info("tearDown: deleting " + tableName);
        // // wait I may delete an actual business table, comment out until this suite is fully
        // // based on fake data rasterTestData.deleteTable(tableName);
        // } catch (Exception e) {
        // LOGGER.log(Level.INFO, "Error deleting test table " + tableName, e);
        // }
    }

    /**
     * Test method for
     * {@link org.geotools.arcsde.raster.gce.ArcSDEGridCoverage2DReaderJAI#getInfo()}.
     */
    @Test
    @Ignore
    public void testGetInfo() {
        fail("Not yet implemented");
    }

    @Test
    public void testIMG_USGSQUAD_SGBASE() throws Exception {
        tableName = "SDE.IMG_USGSQUAD_SGBASE";
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final int count = 10;
        long time = 0;
        // warm up
        _testIMG_USGSQUAD_SGBASE(reader);
        for (int i = 0; i < count; i++) {
            time += _testIMG_USGSQUAD_SGBASE(reader);
        }
        System.err.println(count + " reads in " + time + "ms");
    }

    private long _testIMG_USGSQUAD_SGBASE(AbstractGridCoverage2DReader reader) throws Exception {

        // http://localhost:8080/geoserver/wms?WIDTH=256&LAYERS=sde%3AIMG_USGSQUAD_SGBASE&STYLES=&SRS=EPSG%3A26986&HEIGHT=256&FORMAT=image%2Fjpeg&TILED=true&TILESORIGIN=169118.35%2C874964.388&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&EXCEPTIONS=application%2Fvnd.ogc.se_inimage&BBOX=239038.74625,916916.62575,253022.8255,930900.705

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();

        final int reqWidth = 256;
        final int reqHeight = 256;

        GeneralEnvelope reqEnvelope = new GeneralEnvelope(originalEnvelope);
        reqEnvelope.setEnvelope(239038.74625, 916916.62575, 253022.8255, 930900.705);

        Stopwatch sw = new Stopwatch();
        sw.start();

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        RenderedImage image = coverage.getRenderedImage();
        writeToDisk(image, "testRead_" + tableName);
        sw.stop();
        return sw.getTime();
    }

    @Test
    public void testIMG_USGSQUAD_SGBASE2() throws Exception {
        initJAI();
        tableName = "SDE.IMG_USGSQUAD_SGBASE";
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        // http://localhost:8080/geoserver/wms?WIDTH=256&LAYERS=sde%3AIMG_USGSQUAD_SGBASE&STYLES=&SRS=EPSG%3A26986&HEIGHT=256&FORMAT=image%2Fjpeg&TILED=true&TILESORIGIN=169118.35%2C874964.388&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&EXCEPTIONS=application%2Fvnd.ogc.se_inimage&BBOX=239038.74625,916916.62575,253022.8255,930900.705

        final GeneralEnvelope requestEnvelope =  new GeneralEnvelope(reader.getOriginalEnvelope());
        final GridEnvelope originalGridRange = reader.getOriginalGridRange();

        final int reqWidth = 1000;// originalGridRange.getSpan(0) / 30;
        final int reqHeight = 447;//originalGridRange.getSpan(1) / 30;


        requestEnvelope.setEnvelope(249403.30079879,929431.93501181,251110.35576095,930194.98857989);
        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, requestEnvelope);
        assertNotNull("read coverage returned null", coverage);

        RenderedImage image = coverage.getRenderedImage();
        writeToDisk(image, "testRead_" + tableName);
    }

    @Test
    public void testCOLOROQ_TEST() throws Exception {
        tableName = "SDE.RASTER.COLOROQ_TEST";
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        System.out.println(reader.getInfo().getDescription());

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();

        final int reqWidth = 225;
        final int reqHeight = 512;

        GeneralEnvelope reqEnvelope = originalEnvelope;

        assertTrue(originalEnvelope.intersects(reqEnvelope, true));

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        writeToDisk(coverage, "testRead_" + tableName);

        RenderedImage image = coverage.view(ViewType.RENDERED).getRenderedImage();
        writeToDisk(image, tableName);
    }

    @Test
    public void testReadIMG_USGSQUADM_Buggy() throws Exception {
        // http://localhost:8080/geoserver/wms?HEIGHT=500&WIDTH=1200&LAYERS=sde:IMG_USGSQUADM&STYLES=&SRS=EPSG%3A26986&FORMAT=image%2Fjpeg&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&EXCEPTIONS=application%2Fvnd.ogc.se_inimage&BBOX=253178.45971681,872419.13604732,253521.78247071,872562.18719478
        // http://arcy.opengeo.org:8080/geoserver/wms?SRS=EPSG%3A26986&WIDTH=950&STYLES=&HEIGHT=400&LAYERS=massgis%3ASDE.IMG_USGSQUADM&FORMAT=image%2Fpng&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&EXCEPTIONS=application%2Fvnd.ogc.se_inimage&BBOX=290557.15234375,932233.3984375,325347.19140625,946881.8359375
        tableName = "SDE.IMG_USGSQUADM";
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        LOGGER.info("Original envelope: " + reader.getOriginalEnvelope());
        final GeneralEnvelope requestEnvelope = new GeneralEnvelope(reader.getOriginalEnvelope());
        requestEnvelope.setEnvelope(32168.0, 773720.0, 333224.0, 962136.0);
        final int reqWidth = 294;
        final int reqHeight =184;

        //requestEnvelope.setEnvelope(33000.0, 774000.0, 333002.0, 962000.0);
//        final int reqWidth = 150001;// 294;
//        final int reqHeight = 94000;// 184;

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, requestEnvelope);
        assertNotNull("read coverage returned null", coverage);

        System.err.println("Requested envelope " + requestEnvelope);
        System.err.println("Result envelope    " + coverage.getGridGeometry().getEnvelope());
        System.err.println("Result grid range  " + coverage.getGridGeometry().getGridRange());

        initJAI();
        // RenderedImage image = coverage.getRenderedImage();
        writeToDisk(coverage, "testRead_" + tableName);
    }

    private void initJAI() {
        final boolean tileRecycling = true;
        final double memoryFactor = 0.5;
        final float memoryThreshold = 0.75f;
        final boolean useImageIOCache = false;
        final int tileThreads = 7;
        final int tilePriority = 5;

        JAI jaiDef = JAI.getDefaultInstance();

        // setting JAI wide hints
        jaiDef.setRenderingHint(JAI.KEY_CACHED_TILE_RECYCLING_ENABLED, tileRecycling);

        // tile factory and recycler
        final RecyclingTileFactory recyclingFactory = new RecyclingTileFactory();
        jaiDef.setRenderingHint(JAI.KEY_TILE_FACTORY, recyclingFactory);
        jaiDef.setRenderingHint(JAI.KEY_TILE_RECYCLER, recyclingFactory);

        // Setting up Cache Capacity
        TileCache jaiCache = jaiDef.getTileCache();

        long jaiMemory = (long) (memoryFactor * Runtime.getRuntime().maxMemory());
        jaiCache.setMemoryCapacity(jaiMemory);

        // Setting up Cahce Threshold
        jaiCache.setMemoryThreshold(memoryThreshold);

        jaiDef.getTileScheduler().setParallelism(tileThreads);
        jaiDef.getTileScheduler().setPrefetchParallelism(7);
        jaiDef.getTileScheduler().setPriority(tilePriority);
        jaiDef.getTileScheduler().setPrefetchPriority(5);

        // ImageIO Caching
        ImageIO.setUseCache(useImageIOCache);

        // ImageIO.setCacheDirectory(new File("/Users/groldan/tmp/cache"));

        // Workaround for native mosaic BUG
        Registry.setNativeAccelerationAllowed("Mosaic", false, jaiDef);
    }

    public static void main(String[] argv) {
        try {
            ArcSDEGridCoverage2DReaderJAILegacyOnlineTest.setUpBeforeClass();
            ArcSDEGridCoverage2DReaderJAILegacyOnlineTest test = new ArcSDEGridCoverage2DReaderJAILegacyOnlineTest();
            test.setUp();

            test.testReadIMG_USGSQUADM();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Test
    public void testReadIMG_USGSQUADM() throws Exception {

        initJAI();

        doWriteToDisk = false;

        tableName = "SDE.RASTER.IMG_USGSQUADM";
        // tableName = "SDE.RASTER.IMG_USGSQUAD_SGBASE";
        getReader();

        final int count = 10;
        final int threads = 30;
        final AtomicInteger writeCount = new AtomicInteger();
        Stopwatch sw = new Stopwatch();
        sw.start();

        List<Callable<Object>> tasks = new ArrayList<Callable<Object>>(threads);

        final long[] stats = { Long.MAX_VALUE, Long.MIN_VALUE, 0, 0 };// min/max/total/max mem

        for (int t = 0; t < threads; t++) {
            tasks.add(new Callable<Object>() {
                public Object call() throws Exception {
                    try {

                        final AbstractGridCoverage2DReader reader = getReader();
                        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

                        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
                        final GridEnvelope originalGridRange = reader.getOriginalGridRange();

                        final int reqWidth = originalGridRange.getSpan(0) / 200;
                        final int reqHeight = originalGridRange.getSpan(1) / 200;

                        for (int i = 0; i < count; i++) {
                            final GeneralEnvelope reqEnv = new GeneralEnvelope(originalEnvelope);
                            {
                                double dx = (originalEnvelope.getSpan(0) / 2) / (2 * i + 1);
                                double dy = (originalEnvelope.getSpan(1) / 2) / (2 * i + 1);
                                reqEnv.setEnvelope(originalEnvelope.getMedian(0) - dx,
                                        originalEnvelope.getMedian(1) - dy,
                                        originalEnvelope.getMedian(0) + dx,
                                        originalEnvelope.getMedian(1) + dy);
                            }

                            final GridCoverage2D coverage = readCoverage(reader, reqWidth,
                                    reqHeight, reqEnv);
                            assertNotNull("read coverage returned null", coverage);

                            RenderedImage image = coverage.view(ViewType.PHOTOGRAPHIC)
                                    .getRenderedImage();
                            // RenderedImage image =
                            // coverage.view(ViewType.NATIVE).getRenderedImage();
                            Stopwatch sw = new Stopwatch();
                            sw.start();
                            writeToDisk(image, "testRead_" + tableName);
                            sw.stop();
                            long memMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                                    .freeMemory()) / 1024 / 1024;
                            System.err.println("wrote #" + writeCount.incrementAndGet() + " in "
                                    + sw.getTimeString() + ": Mem: " + memMB + "M. Thread: "
                                    + Thread.currentThread().getName() + ", iteration #" + i);

                            synchronized (stats) {
                                stats[0] = Math.min(stats[0], sw.getTime());
                                stats[1] = Math.max(stats[1], sw.getTime());
                                stats[2] = stats[2] + sw.getTime();
                                stats[3] = Math.max(stats[3], memMB);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                    return null;
                }
            });
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(threads);
        threadPool.invokeAll(tasks);

        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
            Thread.sleep(100);
        }
        sw.stop();
        System.err.println("____ Finished in " + sw.getTimeString() + ". Min: " + stats[0]
                + ", Max: " + stats[1] + ", total execution time: " + stats[2] + ", Avg: "
                + (stats[2] / (threads * count)) + ". Max mem: " + stats[3] + "MB");
        System.err.println("__________________\n  SNAPSHOT!!!!!!!!");
        // Thread.sleep(5000);
    }

    @Test
    public void testReadIMGCOQ_2005() throws Exception {
        tableName = "SDE.RASTER.IMG_COQ2005_CLIP_BOS";

        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
        GridEnvelope originalGridRange = reader.getOriginalGridRange();

        final int reqWidth = originalGridRange.getSpan(0) / 2;
        final int reqHeight = originalGridRange.getSpan(1) / 2;

        Envelope reqEnvelope = originalEnvelope;

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        GridGeometry2D gg = coverage.getGridGeometry();
        Envelope2D envelope2D = gg.getEnvelope2D();
        GridEnvelope gridRange = gg.getGridRange();

        System.out.println("requested size: " + reqWidth + "x" + reqHeight);
        System.out.println("result size   : " + gridRange.getSpan(0) + "x" + gridRange.getSpan(1));

        System.out.println("requested envelope: " + reqEnvelope);

        System.out.println("result envelope   : " + envelope2D);

        // RenderedImage image = coverage.getRenderedImage();
        // writeToDisk(coverage, "testRead_" + tableName);

        RenderedImage image = coverage.view(ViewType.RENDERED).getRenderedImage();
        writeToDisk(image, tableName);

        writeBand(image, new int[] { 0 }, "red");
        writeBand(image, new int[] { 1 }, "green");
        writeBand(image, new int[] { 2 }, "blue");
        writeBand(image, new int[] { 3 }, "alpha");

        writeBand(image, new int[] { 0, 1, 2 }, "rgb");
    }

    @Test
    public void testIMG_WIND_SPD30M_ADAMS() throws Exception {
        tableName = "SDE.RASTER.IMG_WIND_SPD30M_ADAMS";

        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
        GridEnvelope originalGridRange = reader.getOriginalGridRange();

        final int reqWidth = originalGridRange.getSpan(0);
        final int reqHeight = originalGridRange.getSpan(1);

        GeneralEnvelope reqEnvelope = originalEnvelope;
        reqEnvelope.setCoordinateReferenceSystem(originalEnvelope.getCoordinateReferenceSystem());

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        GridGeometry2D gg = coverage.getGridGeometry();
        Envelope2D envelope2D = gg.getEnvelope2D();
        GridEnvelope gridRange = gg.getGridRange();

        System.out.println("requested size: " + reqWidth + "x" + reqHeight);
        System.out.println("result size   : " + gridRange.getSpan(0) + "x" + gridRange.getSpan(1));

        System.out.println("requested envelope: " + reqEnvelope);

        System.out.println("result envelope   : " + envelope2D);

        // writeToDisk(coverage, "testRead_" + tableName);
    }

    @Test
    public void testReadNOAA_13006_1() throws Exception {
        tableName = "SDE.RASTER.NOAA_13006_1";

        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();

        final int reqWidth = 100;// 800;// originalGridRange.getSpan(0) / 8;
        final int reqHeight = 75;// 595;// originalGridRange.getSpan(1) / 8;

        GeneralEnvelope reqEnvelope = new GeneralEnvelope(new double[] { 274059, 837434 },
                new double[] { 355782, 898216 });
        reqEnvelope.setCoordinateReferenceSystem(originalEnvelope.getCoordinateReferenceSystem());

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        GridGeometry2D gg = coverage.getGridGeometry();
        Envelope2D envelope2D = gg.getEnvelope2D();
        GridEnvelope gridRange = gg.getGridRange();

        System.out.println("requested size: " + reqWidth + "x" + reqHeight);
        System.out.println("result size   : " + gridRange.getSpan(0) + "x" + gridRange.getSpan(1));

        System.out.println("requested envelope: " + reqEnvelope);

        System.out.println("result envelope   : " + envelope2D);

        writeToDisk(coverage, "testRead_" + tableName);

        RenderedImage image = coverage.getRenderedImage();
        ColorModel colorModel = image.getColorModel();

        assertTrue(colorModel instanceof IndexColorModel);
        IndexColorModel cm = ((IndexColorModel) colorModel);

        int numComponents = cm.getMapSize();
        byte[] r = new byte[numComponents];
        byte[] g = new byte[numComponents];
        byte[] b = new byte[numComponents];
        byte[] a = new byte[numComponents];
        cm.getReds(r);
        cm.getGreens(g);
        cm.getBlues(b);
        cm.getAlphas(a);
        for (int i = 0; i < numComponents; i++) {
            System.out.print(i + " = ");
            System.out.print((int) r[i] & 0xFF);
            System.out.print(',');
            System.out.print((int) g[i] & 0xFF);
            System.out.print(',');
            System.out.print((int) b[i] & 0xFF);
            System.out.print(',');
            System.out.print((int) a[i] & 0xFF);
            System.out.print('\n');
        }
    }

    private void writeBand(RenderedImage image, int[] bands, String channel) throws Exception {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(bands);
        PlanarImage alpha = JAI.create("bandSelect", pb);
        writeToDisk(alpha, tableName + "_" + channel);
    }

    @Test
    public void testReadIMGCOQ_2001() throws Exception {
        tableName = "SDE.RASTER.IMG_COQ2001_CLIP_BOS_1";
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
        GridEnvelope originalGridRange = reader.getOriginalGridRange();

        final int reqWidth = originalGridRange.getSpan(0) / 4;
        final int reqHeight = originalGridRange.getSpan(1) / 4;

        Envelope reqEnvelope = originalEnvelope;
        // GeneralEnvelope reqEnvelope = new GeneralEnvelope(originalEnvelope
        // .getCoordinateReferenceSystem());
        //
        // final double reqMinx = 235901.26048201;
        // final double reqMiny = 901552.0880242661;
        // final double reqMaxx = 236781.26048201;
        // final double reqMaxy = 902253.0880242661;
        //
        // reqEnvelope.setEnvelope(reqMinx, reqMiny, reqMaxx, reqMaxy);

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        GridGeometry2D gg = coverage.getGridGeometry();
        Envelope2D envelope2D = gg.getEnvelope2D();
        GridEnvelope gridRange = gg.getGridRange();

        System.out.println("requested size: " + reqWidth + "x" + reqHeight);
        System.out.println("result size   : " + gridRange.getSpan(0) + "x" + gridRange.getSpan(1));

        System.out.println("requested envelope: " + reqEnvelope);

        System.out.println("result envelope   : " + envelope2D);

        // RenderedImage image = coverage.getRenderedImage();
        // writeToDisk(coverage, "testRead_" + tableName);
    }

    private void writeToDisk(GridCoverage2D coverage, String fileName) throws Exception {

        OutputStream outStream;
        {
            String file = System.getProperty("user.home");
            file += File.separator + "arcsde_test" + File.separator + fileName + ".tiff";
            File path = new File(file);
            path.getParentFile().mkdirs();
            outStream = new FileOutputStream(path);
        }

        // good for all params
        final GeoTiffWriteParams wp = new GeoTiffWriteParams();
        wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
        wp.setCompressionType("LZW");
        wp.setCompressionQuality(0.75F);
        wp.setTilingMode(GeoToolsWriteParams.MODE_EXPLICIT);
        wp.setTiling(128, 128);
        GeoTiffFormat format = new GeoTiffFormat();
        final ParameterValueGroup writerParams = format.getWriteParameters();
        writerParams.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString())
                .setValue(wp);

        // write down
        GeoTiffWriter writer = (GeoTiffWriter) format.getWriter(outStream);
        try {
            GeneralParameterValue[] params = (GeneralParameterValue[]) writerParams.values()
                    .toArray(new GeneralParameterValue[1]);
            writer.write(coverage, params);
        } finally {
            writer.dispose();
            outStream.close();
        }
        
    }

    private static AtomicInteger wc = new AtomicInteger();

    private void writeToDisk(final RenderedImage image, String fileName) throws Exception {
        if (!DEBUG) {
            LOGGER.fine("DEBUG == false, not writing image to disk");
            return;
        }
        String file = System.getProperty("user.home");
        file += File.separator + "arcsde_test" + File.separator + fileName + "_"
                + wc.incrementAndGet() + ".tiff";
        File path = new File(file);
        path.getParentFile().mkdirs();

        // System.out.println("\n --- Writing to " + file);
        try {
            long t = System.currentTimeMillis();

            if (doWriteToDisk) {
                ImageIO.write(image, "TIFF", path);
                // ImageWorker iw = new ImageWorker(image);
                // iw.write(path);
            } else {
                OutputStream destination = new OutputStream() {
                    @Override
                    public void write(byte[] b, int o, int l) throws IOException {
                    }

                    @Override
                    public void write(int b) throws IOException {
                    }
                };
                ImageIO.write(image, "TIFF", destination);
            }

            t = System.currentTimeMillis() - t;
            // System.out.println(" - wrote in " + t + "ms" + file);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private GridCoverage2D readCoverage(final AbstractGridCoverage2DReader reader,
            final int reqWidth, final int reqHeight, final Envelope reqEnv) throws Exception {

        GeneralParameterValue[] requestParams = new Parameter[2];

        GridGeometry2D gg2d;
        gg2d = new GridGeometry2D(new GridEnvelope2D(new Rectangle(reqWidth, reqHeight)), reqEnv);

        requestParams[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D,
                gg2d);
        requestParams[1] = new Parameter<OverviewPolicy>(AbstractGridFormat.OVERVIEW_POLICY,
                OverviewPolicy.NEAREST);

        final GridCoverage2D coverage;
        coverage = (GridCoverage2D) reader.read(requestParams);

        return coverage;
    }

    private AbstractGridCoverage2DReader getReader() throws IOException {
        final ArcSDEConnectionConfig config = rasterTestData.getConnectionPool().getConfig();

        final String rgbUrl = "sde://" + config.getUserName() + ":" + config.getPassword() + "@"
                + config.getServerName() + ":" + config.getPortNumber() + "/"
                + config.getDatabaseName() + "#" + tableName
                + ";pool.minConnections=2;pool.maxConnections=2";
        // + config.getMinConnections() + ";pool.maxConnections=" + config.getMaxConnections();

        final ArcSDERasterFormat format = new ArcSDERasterFormatFactory().createFormat();

        AbstractGridCoverage2DReader reader = format.getReader(rgbUrl);
        return reader;
    }

    @Test
    public void testReadRUGGED_RD() throws Exception {
        tableName = "SDE.RASTER.RUGGED_RD";

        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
        GridEnvelope originalGridRange = reader.getOriginalGridRange();

        final int reqWidth = originalGridRange.getSpan(0) / 8;
        final int reqHeight = originalGridRange.getSpan(1) / 8;

        Envelope reqEnvelope = originalEnvelope;

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        GridGeometry2D gg = coverage.getGridGeometry();
        Envelope2D envelope2D = gg.getEnvelope2D();
        GridEnvelope gridRange = gg.getGridRange();

        System.out.println("requested size: " + reqWidth + "x" + reqHeight);
        System.out.println("result size   : " + gridRange.getSpan(0) + "x" + gridRange.getSpan(1));

        System.out.println("requested envelope: " + reqEnvelope);

        System.out.println("result envelope   : " + envelope2D);

        // RenderedImage image = coverage.getRenderedImage();
        // writeToDisk(coverage, "testRead_" + tableName);

        RenderedImage image = coverage.view(ViewType.RENDERED).getRenderedImage();
        writeToDisk(image, tableName);

        // writeBand(image, new int[] { 0 }, "band1");
    }

    @Test
    public void testDescribe() throws Exception {
        tableName = "SDE.IMG_USGSQUADM";

        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
        GridEnvelope originalGridRange = reader.getOriginalGridRange();
        LOGGER.info("original envelope  : " + originalEnvelope);
        LOGGER.info("original grid range: " + originalGridRange);

        int reqWidth = 294;
        int reqHeight = 184;
        GeneralEnvelope reqEnv = new GeneralEnvelope(
                originalEnvelope.getCoordinateReferenceSystem());
        // reqEnv.setEnvelope(236534.9499999986, 901973.4499999813, 236718.8499999986,
        // 902163.0499999814);
        //
        // reqEnv.setEnvelope(32680.0,774232.0,333736.0,962648.0);
        reqEnv.setEnvelope(33192.0, 774244.0, 333224.0, 962136.0);
        GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnv);

        GridGeometry2D gridGeometry = coverage.getGridGeometry();
        GridEnvelope gridRange = gridGeometry.getGridRange();
        Envelope2D envelope2d = gridGeometry.getEnvelope2D();

        System.out.println("requested size: " + reqWidth + "x" + reqHeight);
        System.out.println("result size   : " + gridRange.getSpan(0) + "x" + gridRange.getSpan(1));

        System.out.println("requested envelope: " + reqEnv);
        System.out.println("result envelope   : " + envelope2d);

        writeToDisk(coverage.getRenderedImage(), tableName);
    }

}
