/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2021, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.image;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sun.media.imageioimpl.common.PackageUtil;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import it.geosolutions.jaiext.lookup.LookupTable;
import it.geosolutions.jaiext.lookup.LookupTableFactory;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.Histogram;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.Warp;
import javax.media.jai.WarpAffine;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.MosaicDescriptor;
import org.geotools.TestData;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.Viewer;
import org.geotools.coverage.processing.GridProcessingTestBase;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ComponentColorModelJAI;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.WarpBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

/**
 * Tests the {@link ImageWorker} implementation.
 *
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @author Martin Desruisseaux (Geomatys)
 */
public final class ImageWorkerTest extends GridProcessingTestBase {

    private static final String GOOGLE_MERCATOR_WKT = "PROJCS[\"WGS 84 / Pseudo-Mercator\","
            + "GEOGCS[\"Popular Visualisation CRS\","
            + "DATUM[\"Popular_Visualisation_Datum\","
            + "SPHEROID[\"Popular Visualisation Sphere\",6378137,0,"
            + "AUTHORITY[\"EPSG\",\"7059\"]],"
            + "TOWGS84[0,0,0,0,0,0,0],"
            + "AUTHORITY[\"EPSG\",\"6055\"]],"
            + "PRIMEM[\"Greenwich\",0,"
            + "AUTHORITY[\"EPSG\",\"8901\"]],"
            + "UNIT[\"degree\",0.01745329251994328,"
            + "AUTHORITY[\"EPSG\",\"9122\"]],"
            + "AUTHORITY[\"EPSG\",\"4055\"]],"
            + "UNIT[\"metre\",1,"
            + "AUTHORITY[\"EPSG\",\"9001\"]],"
            + "PROJECTION[\"Mercator_1SP\"],"
            + "PARAMETER[\"central_meridian\",0],"
            + "PARAMETER[\"scale_factor\",1],"
            + "PARAMETER[\"false_easting\",0],"
            + "PARAMETER[\"false_northing\",0],"
            + "AUTHORITY[\"EPSG\",\"3785\"],"
            + "AXIS[\"X\",EAST],"
            + "AXIS[\"Y\",NORTH]]";

    /** Image to use for testing purpose. */
    private static RenderedImage sstImage,
            worldImage,
            chlImage,
            bathy,
            smallWorld,
            gray,
            grayAlpha,
            imageWithNodata,
            imageWithNodata2,
            imageWithNan;

    /** {@code true} if the image should be visualized. */
    private static final boolean SHOW = TestData.isInteractiveTest();

    private static BufferedImage worldDEMImage = null;

    /** Creates a simple 128x128 {@link RenderedImage} for testing purposes. */
    private static RenderedImage getSynthetic(final double maximum) {
        final int width = 128;
        final int height = 128;
        final WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_DOUBLE, width, height, 1, null);
        // random, but repeatable, by using a fixed seed value
        final Random random = new Random(1);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, Math.ceil(random.nextDouble() * maximum));
            }
        }
        final ColorModel cm = new ComponentColorModelJAI(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE, DataBuffer.TYPE_DOUBLE);
        final BufferedImage image = new BufferedImage(cm, raster, false, null);
        return image;
    }

    /**
     * Creates a test image in RGB with either {@link ComponentColorModel} or {@link DirectColorModel}.
     *
     * @param direct <code>true</code> when we request a {@link DirectColorModel}, <code>false
     *     </code> otherwise.
     */
    private static BufferedImage getSyntheticRGB(final boolean direct) {
        final int width = 128;
        final int height = 128;
        final BufferedImage image = direct
                ? new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR)
                : new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final WritableRaster raster = (WritableRaster) image.getData();
        final Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, random.nextInt(256));
            }
        }
        return image;
    }

    private static BufferedImage getSyntheticRGB(Color color) {
        return getSyntheticRGB(color, 128);
    }

    /** Creates a test image in RGB with either {@link ComponentColorModel} or {@link DirectColorModel}. */
    private static BufferedImage getSyntheticRGB(Color color, int sideSize) {
        final int width = sideSize;
        final int height = sideSize;
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final WritableRaster raster = image.getRaster();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, color.getRed());
                raster.setSample(x, y, 1, color.getGreen());
                raster.setSample(x, y, 2, color.getBlue());
            }
        }
        return image;
    }

    private static BufferedImage getSyntheticSolidGray(byte gray) {
        return getSyntheticSolidGray(gray, 128);
    }

    /** Creates a test image in RGB with either {@link ComponentColorModel} or {@link DirectColorModel}. */
    private static BufferedImage getSyntheticSolidGray(byte gray, int sideSize) {
        final int width = sideSize;
        final int height = sideSize;
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final WritableRaster raster = image.getRaster();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, gray);
            }
        }
        return image;
    }

    /** Creates a test paletted image with translucency. */
    private static BufferedImage getSyntheticTranslucentIndexed() {
        final byte[] bb = new byte[256];
        for (int i = 0; i < 256; i++) bb[i] = (byte) i;
        final IndexColorModel icm = new IndexColorModel(8, 256, bb, bb, bb, bb);
        final WritableRaster raster =
                RasterFactory.createWritableRaster(icm.createCompatibleSampleModel(1024, 1024), null);
        for (int i = raster.getMinX(); i < raster.getMinX() + raster.getWidth(); i++)
            for (int j = raster.getMinY(); j < raster.getMinY() + raster.getHeight(); j++)
                raster.setSample(i, j, 0, (i + j) / 32);
        return new BufferedImage(icm, raster, false, null);
    }

    /** Creates a test paletted image with nodata and no transparency */
    private static RenderedImage getIndexedRGBNodata() {
        // a palette with just the first 200 entries filled, the others are all zero (but present!)
        final byte[] bb = new byte[256];
        for (int i = 0; i < 256; i++) {
            if (i < 200) {
                bb[i] = (byte) i;
            } else {
                bb[i] = (byte) 0;
            }
        }
        int noDataValue = 200;
        NoDataContainer noData = new NoDataContainer(noDataValue);
        final IndexColorModel icm = new IndexColorModel(8, 256, bb, bb, bb);
        final WritableRaster raster =
                RasterFactory.createWritableRaster(icm.createCompatibleSampleModel(1024, 1024), null);
        for (int i = raster.getMinX(); i < raster.getMinX() + raster.getWidth(); i++) {
            for (int j = raster.getMinY(); j < raster.getMinY() + raster.getHeight(); j++) {
                if (i - raster.getMinX() < raster.getWidth() / 2) {
                    raster.setSample(i, j, 0, (i + j) / 32);
                } else {
                    raster.setSample(i, j, 0, 200);
                }
            }
        }
        BufferedImage bi = new BufferedImage(icm, raster, false, null);
        PlanarImage planarImage = PlanarImage.wrapRenderedImage(bi);
        planarImage.setProperty(NoDataContainer.GC_NODATA, noData);
        return planarImage;
    }

    /** Creates a test paletted image with a given number of entries in the map */
    private static BufferedImage getSyntheticGrayIndexed(int entries) {
        final byte[] bb = new byte[entries];
        for (int i = 0; i < entries; i++) bb[i] = (byte) i;
        final IndexColorModel icm = new IndexColorModel(8, entries, bb, bb, bb, bb);
        final WritableRaster raster =
                RasterFactory.createWritableRaster(icm.createCompatibleSampleModel(512, 512), null);
        for (int i = raster.getMinX(); i < raster.getMinX() + raster.getWidth(); i++)
            for (int j = raster.getMinY(); j < raster.getMinY() + raster.getHeight(); j++)
                raster.setSample(i, j, 0, (i + j) / 32);
        return new BufferedImage(icm, raster, false, null);
    }

    /**
     * Loads the image (if not already loaded) and creates the worker instance.
     *
     * @throws IOException If the image was not found.
     */
    @Before
    public void setUp() throws IOException {
        if (sstImage == null) {
            try (InputStream input = TestData.openStream(GridCoverage2D.class, "QL95209.png")) {
                sstImage = ImageIO.read(input);
            }
        }
        if (worldImage == null) {
            try (InputStream input = TestData.openStream(GridCoverage2D.class, "world.png")) {
                worldImage = ImageIO.read(input);
            }
        }
        if (worldDEMImage == null) {
            try (InputStream input = TestData.openStream(GridCoverage2D.class, "world_dem.gif")) {
                worldDEMImage = ImageIO.read(input);
            }
        }
        if (chlImage == null) {
            try (InputStream input = TestData.openStream(GridCoverage2D.class, "CHL01195.png")) {
                chlImage = ImageIO.read(input);
            }
        }
        if (bathy == null) {
            try (InputStream input = TestData.openStream(GridCoverage2D.class, "BATHY.png")) {
                bathy = ImageIO.read(input);
            }
        }

        if (smallWorld == null) {
            try (InputStream input = TestData.openStream(GridCoverage2D.class, "small_world.png")) {
                smallWorld = ImageIO.read(input);
            }
        }

        if (gray == null) {
            try (InputStream input = TestData.openStream(GridCoverage2D.class, "gray.png")) {
                gray = ImageIO.read(input);
            }
        }

        if (grayAlpha == null) {
            try (InputStream input = TestData.openStream(GridCoverage2D.class, "gray-alpha.png")) {
                grayAlpha = ImageIO.read(input);
            }
        }

        if (imageWithNodata == null) {
            try (InputStream input = org.geotools.test.TestData.openStream(this, "nodataD.tiff")) {
                imageWithNodata = ImageIO.read(input);
            }
        }

        if (imageWithNan == null) {
            try (InputStream input = org.geotools.test.TestData.openStream(this, "nodataNan.tiff")) {
                imageWithNan = ImageIO.read(input);
            }
        }

        if (imageWithNodata2 == null) {
            try (InputStream input = org.geotools.test.TestData.openStream(this, "nodata.tiff")) {
                imageWithNodata2 = ImageIO.read(input);
            }
        }
    }

    @Test
    public void testBitmask() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(sstImage);

        worker.forceBitmaskIndexColorModel();
        assertEquals(1, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isBinary());
        assertTrue(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertFalse(worker.isTranslucent());
        assertNoData(worker, null);

        final BufferedImage directRGB = getSyntheticRGB(true);
        worker = new ImageWorker(directRGB);
        worker.forceBitmaskIndexColorModel();
        assertEquals(1, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isBinary());
        assertTrue(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertFalse(worker.isTranslucent());
        assertNoData(worker, null);

        final BufferedImage componentRGB = getSyntheticRGB(false);
        worker = new ImageWorker(componentRGB);
        worker.forceBitmaskIndexColorModel();
        assertEquals(1, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isBinary());
        assertTrue(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertFalse(worker.isTranslucent());
        assertNoData(worker, null);

        final BufferedImage translucentIndexed = getSyntheticTranslucentIndexed();
        worker = new ImageWorker(translucentIndexed);
        assertTrue(worker.isBytes());
        assertFalse(worker.isBinary());
        assertTrue(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertTrue(worker.isTranslucent());
        assertNoData(worker, null);

        worker.forceIndexColorModelForGIF(true);
        assertEquals(1, worker.getNumBands());
        assertEquals(0, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isBinary());
        assertTrue(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isTranslucent());
        assertNoData(worker, null);
    }

    /**
     * Tests capability to write GIF image.
     *
     * @throws IOException If an error occured while writting the image.
     */
    @Test
    public void testGIFImageWrite() throws IOException {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // Get the image of the world with transparency.
        ImageWorker worker = new ImageWorker(worldDEMImage);
        show(worker, "Input GIF");
        RenderedImage image = worker.getRenderedImage();
        ColorModel cm = image.getColorModel();
        assertTrue("wrong color model", cm instanceof IndexColorModel);
        assertEquals("wrong transparency model", Transparency.OPAQUE, cm.getTransparency());
        // Writes it out as GIF on a file using index color model with
        final File outFile = TestData.temp(this, "temp.gif");
        worker.writeGIF(outFile, "LZW", 0.75f);

        // Read it back
        final ImageWorker readWorker = new ImageWorker(ImageIO.read(outFile));
        show(readWorker, "GIF to file");
        image = readWorker.getRenderedImage();
        cm = image.getColorModel();
        assertTrue("wrong color model", cm instanceof IndexColorModel);
        assertEquals("wrong transparency model", Transparency.OPAQUE, cm.getTransparency());

        // Write on an output streams.
        try (OutputStream os = new FileOutputStream(outFile)) {
            worker = new ImageWorker(worldImage);
            worker.forceIndexColorModelForGIF(true);
            worker.writeGIF(os, "LZW", 0.75f);

            // Read it back.
            readWorker.setImage(ImageIO.read(outFile));
            show(readWorker, "GIF to output stream");
            image = readWorker.getRenderedImage();
            cm = image.getColorModel();
            assertTrue("wrong color model", cm instanceof IndexColorModel);
            assertEquals("wrong transparency model", Transparency.BITMASK, cm.getTransparency());
            assertEquals("wrong transparent color index", 255, ((IndexColorModel) cm).getTransparentPixel());
        }
        outFile.delete();
    }

    /**
     * Testing JPEG capabilities.
     *
     * @throws IOException If an error occured while writting the image.
     */
    @Test
    public void testJPEGWrite() throws IOException {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // get the image of the world with transparency
        final ImageWorker worker = new ImageWorker(getSyntheticRGB(true));
        show(worker, "Input JPEG");

        // /////////////////////////////////////////////////////////////////////
        // nativeJPEG  with compression JPEG-LS
        // ////////////////////////////////////////////////////////////////////
        final File outFile = TestData.temp(this, "temp.jpeg");
        ImageWorker readWorker;
        if (PackageUtil.isCodecLibAvailable()) {
            worker.writeJPEG(outFile, "JPEG-LS", 0.75f, true);
            readWorker = new ImageWorker(ImageIO.read(outFile));
            show(readWorker, "Native JPEG LS");
        } else {
            try {
                worker.writeJPEG(outFile, "JPEG-LS", 0.75f, true);
                fail();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        // /////////////////////////////////////////////////////////////////////
        // native JPEG compression
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writeJPEG(outFile, "JPEG", 0.75f, true);
        readWorker = new ImageWorker(ImageIO.read(outFile));
        show(readWorker, "native JPEG");

        // /////////////////////////////////////////////////////////////////////
        // pure java JPEG compression
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writeJPEG(outFile, "JPEG", 0.75f, false);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "Pure Java JPEG");
        outFile.delete();

        // /////////////////////////////////////////////////////////////////////
        // test alpha channel is removed with nodata value
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(getIndexedRGBNodata());
        worker.forceIndexColorModel(false);
        worker.writeJPEG(outFile, "JPEG", 0.75f, true);
        // with the native imageIO, an exception would have been thrown by now.
        // with non-native, writing the alpha channel to JPEG will work
        // as well as reading it (!), but this is not generally supported
        // and will confuse other image readers,
        // so alpha should be removed for JPEG
        BufferedImage image = ImageIO.read(outFile);
        assertFalse(image.getColorModel().hasAlpha());
    }

    /**
     * Testing PNG capabilities.
     *
     * @throws IOException If an error occured while writting the image.
     */
    @Test
    public void testPNGWrite() throws IOException {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // Get the image of the world with transparency.
        final ImageWorker worker = new ImageWorker(worldImage);
        show(worker, "Input file");

        // /////////////////////////////////////////////////////////////////////
        // native png filtered compression 24 bits
        // /////////////////////////////////////////////////////////////////////
        final File outFile = TestData.temp(this, "temp.png");
        worker.writePNG(outFile, "FILTERED", 0.75f, true, false);
        final ImageWorker readWorker = new ImageWorker(ImageIO.read(outFile));
        show(readWorker, "Native PNG24");

        // /////////////////////////////////////////////////////////////////////
        // native png filtered compression 8 bits
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writePNG(outFile, "FILTERED", 0.75f, true, true);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "native PNG8");

        // /////////////////////////////////////////////////////////////////////
        // pure java png 24
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writePNG(outFile, "FILTERED", 0.75f, false, false);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "Pure  PNG24");

        // /////////////////////////////////////////////////////////////////////
        // pure java png 8
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writePNG(outFile, "FILTERED", 0.75f, false, true);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "Pure  PNG8");
        outFile.delete();

        // Check we are not expanding to RGB a paletted image
        worker.setImage(sstImage);
        assertTrue(sstImage.getColorModel() instanceof IndexColorModel);
        worker.writePNG(outFile, "FILTERED", 0.75f, false, false);
        readWorker.setImage(ImageIO.read(outFile));
        assertTrue(readWorker.getRenderedImage().getColorModel() instanceof IndexColorModel);
    }

    @Test
    public void test16BitGIF() throws Exception {
        // the resource has been compressed since the palette is way larger than the image itself,
        // and the palette does not get compressed
        try (InputStream gzippedStream = ImageWorkerTest.class
                        .getResource("test-data/sf-sfdem.tif.gz")
                        .openStream();
                GZIPInputStream is = new GZIPInputStream(gzippedStream)) {
            @SuppressWarnings("PMD.CloseResource") // closed along the reader
            ImageInputStream iis = ImageIO.createImageInputStream(is);

            ImageReader reader = new TIFFImageReaderSpi().createReaderInstance(iis);
            reader.setInput(iis);
            BufferedImage bi = reader.read(0);
            if (TestData.isInteractiveTest()) {
                ImageIOUtilities.visualize(bi, "before");
            }
            reader.dispose();
            iis.close();
            IndexColorModel icm = (IndexColorModel) bi.getColorModel();
            assertEquals(65536, icm.getMapSize());

            final File outFile = TestData.temp(this, "temp.gif");
            ImageWorker worker = new ImageWorker(bi);
            worker.writeGIF(outFile, "LZW", 0.75f);

            // Read it back.
            bi = ImageIO.read(outFile);
            if (TestData.isInteractiveTest()) {
                ImageIOUtilities.visualize(bi, "after");
            }
            ColorModel cm = bi.getColorModel();
            assertTrue("wrong color model", cm instanceof IndexColorModel);
            assertEquals("wrong transparency model", Transparency.OPAQUE, cm.getTransparency());
            final IndexColorModel indexColorModel = (IndexColorModel) cm;
            assertEquals("wrong transparent color index", -1, indexColorModel.getTransparentPixel());
            assertEquals("wrong component size", 8, indexColorModel.getComponentSize(0));
            outFile.delete();
        }
    }

    @Test
    public void test16BitPNG() throws Exception {
        // the resource has been compressed since the palette is way larger than the image itself,
        // and the palette does not get compressed
        InputStream gzippedStream =
                ImageWorkerTest.class.getResource("test-data/sf-sfdem.tif.gz").openStream();
        try (ImageInputStream iis = ImageIO.createImageInputStream(new GZIPInputStream(gzippedStream))) {
            ImageReader reader = new TIFFImageReaderSpi().createReaderInstance(iis);
            reader.setInput(iis);
            BufferedImage bi = reader.read(0);
            reader.dispose();
            IndexColorModel icm = (IndexColorModel) bi.getColorModel();
            assertEquals(65536, icm.getMapSize());

            final File outFile = TestData.temp(this, "temp.png");
            ImageWorker worker = new ImageWorker(bi);
            worker.writePNG(outFile, "FILTERED", 0.75f, true, false);
            worker.dispose();

            // make sure we can read it
            BufferedImage back = ImageIO.read(outFile);

            // we expect a RGB one
            ComponentColorModel ccm = (ComponentColorModel) back.getColorModel();
            assertEquals(3, ccm.getNumColorComponents());

            // now ask to write paletted
            worker = new ImageWorker(bi);
            worker.writePNG(outFile, "FILTERED", 0.75f, true, true);
            worker.dispose();

            // make sure we can read it
            back = ImageIO.read(outFile);

            // we expect a RGB one
            icm = (IndexColorModel) back.getColorModel();
            assertEquals(3, icm.getNumColorComponents());
            assertTrue(icm.getMapSize() <= 256);
        }
    }

    @Test
    public void test16BitPaletted() throws Exception {
        InputStream gzippedStream =
                ImageWorkerTest.class.getResource("test-data/sf-sfdem.tif.gz").openStream();
        try (ImageInputStream iis = ImageIO.createImageInputStream(new GZIPInputStream(gzippedStream))) {
            ImageReader reader = new TIFFImageReaderSpi().createReaderInstance(iis);
            reader.setInput(iis);
            BufferedImage bi = reader.read(0);
            reader.dispose();
            IndexColorModel icm = (IndexColorModel) bi.getColorModel();
            assertEquals(65536, icm.getMapSize());
            ImageWorker worker = new ImageWorker(bi).makeColorTransparent(Color.black);
            RenderedImage ri = worker.rescaleToBytes().getRenderedImage();

            // we expect a RGBA one
            ComponentColorModel ccm = (ComponentColorModel) ri.getColorModel();
            SampleModel sm = ri.getSampleModel();
            assertEquals(3, ccm.getNumColorComponents());
            assertTrue(ccm.hasAlpha());
            assertEquals(DataBuffer.TYPE_BYTE, sm.getDataType());
            assertArrayEquals(new int[] {8, 8, 8, 8}, sm.getSampleSize());
        }
    }

    @Test
    public void test16BitPalettedGray() throws Exception {
        byte[] red = new byte[65536];
        byte[] green = new byte[65536];
        byte[] blue = new byte[65536];
        byte[] alpha = new byte[65536];
        byte value;
        for (int i = 0; i < 65536; i++) {
            value = (byte) (i / 256 & 0xFF);
            red[i] = value;
            green[i] = value;
            blue[i] = value;
            alpha[i] = (byte) 0xFF;
        }
        alpha[0] = 0;

        IndexColorModel icm = new IndexColorModel(16, 65536, red, green, blue, alpha);
        SampleModel sm = new ComponentSampleModel(DataBuffer.TYPE_USHORT, 50, 50, 1, 50, new int[] {0});
        WritableRaster raster = RasterFactory.createWritableRaster(sm, null);
        BufferedImage bi = new BufferedImage(icm, raster, false, null);

        assertEquals(65536, icm.getMapSize());

        ImageWorker worker = new ImageWorker(bi).forceComponentColorModel();
        RenderedImage ri = worker.getRenderedImage();

        // we expect a gray one
        ComponentColorModel ccm = (ComponentColorModel) ri.getColorModel();
        sm = ri.getSampleModel();
        assertEquals(1, ccm.getNumColorComponents());
        assertTrue(ccm.hasAlpha());
        assertEquals(DataBuffer.TYPE_USHORT, sm.getDataType());
        assertArrayEquals(new int[] {16, 16}, sm.getSampleSize());
    }

    @Test
    public void test4BitPNG() throws Exception {

        // create test image
        IndexColorModel icm = new IndexColorModel(
                4,
                16,
                new byte[] {(byte) 255, 0, 0, 0, 16, 32, 64, (byte) 128, 1, 2, 3, 4, 5, 6, 7, 8},
                new byte[] {0, (byte) 255, 0, 0, 16, 32, 64, (byte) 128, 1, 2, 3, 4, 5, 6, 7, 8},
                new byte[] {0, 0, (byte) 255, 0, 16, 32, 64, (byte) 128, 1, 2, 3, 4, 5, 6, 7, 8});
        assertEquals(16, icm.getMapSize());

        // create random data
        WritableRaster data = com.sun.media.jai.codecimpl.util.RasterFactory.createWritableRaster(
                icm.createCompatibleSampleModel(32, 32), new Point(0, 0));
        for (int x = data.getMinX(); x < data.getMinX() + data.getWidth(); x++) {
            for (int y = data.getMinY(); y < data.getMinY() + data.getHeight(); y++) {
                data.setSample(x, y, 0, (x + y) % 8);
            }
        }

        final BufferedImage bi = new BufferedImage(icm, data, false, null);
        assertEquals(16, ((IndexColorModel) bi.getColorModel()).getMapSize());
        assertEquals(4, bi.getSampleModel().getSampleSize(0));
        bi.setData(data);
        if (TestData.isInteractiveTest()) {
            ImageIOUtilities.visualize(bi, "before");
        }

        // encode as png
        ImageWorker worker = new ImageWorker(bi);
        final File outFile = TestData.temp(this, "temp4.png");
        worker.writePNG(outFile, "FILTERED", 0.75f, true, false);
        worker.dispose();

        // make sure we can read it
        BufferedImage back = ImageIO.read(outFile);

        // we expect an IndexColorMolde one matching the old one
        IndexColorModel ccm = (IndexColorModel) back.getColorModel();
        assertEquals(3, ccm.getNumColorComponents());
        assertEquals(16, ccm.getMapSize());
        assertEquals(4, ccm.getPixelSize());
        if (TestData.isInteractiveTest()) {
            ImageIOUtilities.visualize(back, "after");
        }
    }

    /**
     * Testing TIFF capabilities.
     *
     * @throws IOException If an error occured while writting the image.
     */
    @Test
    public void testTIFFWrite() throws IOException {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // Get the image of the world with transparency.
        final ImageWorker worker = new ImageWorker(worldImage);
        show(worker, "Input file");

        // /////////////////////////////////////////////////////////////////////
        // tiff deflated untiled
        // /////////////////////////////////////////////////////////////////////
        final File outFile = TestData.temp(this, "temp.tiff");
        worker.writeTIFF(outFile, "Deflate", 0.75f, -1, -1);
        final ImageWorker readWorker = new ImageWorker(ImageIO.read(outFile));
        show(readWorker, "Tiff untiled");

        // /////////////////////////////////////////////////////////////////////
        // tiff deflated compressed tiled
        // /////////////////////////////////////////////////////////////////////
        worker.setImage(worldImage);
        worker.writeTIFF(outFile, "Deflate", 0.75f, 32, 32);
        readWorker.setImage(ImageIO.read(outFile));
        show(readWorker, "Tiff jpeg compressed, tiled");

        outFile.delete();
    }

    /** Tests the conversion between RGB and indexed color model. */
    @Test
    public void testRGB2Palette() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        final ImageWorker worker = new ImageWorker(worldImage);
        show(worker, "Input file");
        worker.forceIndexColorModelForGIF(true);
        assertNoData(worker, null);

        // Convert to to index color bitmask
        ColorModel cm = worker.getRenderedImage().getColorModel();
        assertTrue("wrong color model", cm instanceof IndexColorModel);
        assertEquals("wrong transparency model", Transparency.BITMASK, cm.getTransparency());
        assertEquals("wrong transparency index", 255, ((IndexColorModel) cm).getTransparentPixel());
        show(worker, "Paletted bitmask");

        // Go back to rgb.
        worker.forceComponentColorModel();
        cm = worker.getRenderedImage().getColorModel();
        assertTrue("wrong color model", cm instanceof ComponentColorModel);
        assertEquals("wrong bands number", 4, cm.getNumComponents());
        assertNoData(worker, null);

        show(worker, "RGB translucent");
        assertEquals("wrong transparency model", Transparency.TRANSLUCENT, cm.getTransparency());
        show(worker, "RGB translucent");
    }

    /** Tests the {@link #rescaleToBytes()} operation. */
    @Test
    public void rescaleToBytes() {

        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());

        // set up synthetic images for testing
        final RenderedImage test1 = ConstantDescriptor.create(128.0f, 128.0f, new Double[] {20000.0}, null);
        final RenderedImage test2 = ConstantDescriptor.create(128.0f, 128.0f, new Double[] {255.0}, null);
        final RenderedImage test3 = getSynthetic(20000);
        final RenderedImage test4 = getSynthetic(255);

        // starting to check the results

        // single band value exceed the byte upper bound and is constant
        final ImageWorker test1I = new ImageWorker(test1).rescaleToBytes();
        Assert.assertEquals("Format", test1I.getRenderedOperation().getOperationName());
        final double[] maximums1 = test1I.getMaximums();
        assertEquals(1, maximums1.length);
        Assert.assertEquals(255.0, maximums1[0], 1E-10);
        final double[] minimums1 = test1I.getMinimums();
        assertEquals(1, minimums1.length);
        Assert.assertEquals(255.0, minimums1[0], 1E-10);
        assertNoData(test1I, null);

        // single band value does not exceed the byte upper bound and is constant
        final ImageWorker test2I = new ImageWorker(test2).rescaleToBytes();
        Assert.assertEquals("Format", test2I.getRenderedOperation().getOperationName());
        final double[] maximums2 = test1I.getMaximums();
        assertEquals(1, maximums2.length);
        Assert.assertEquals(255.0, maximums2[0], 1E-10);
        final double[] minimums2 = test1I.getMinimums();
        assertEquals(1, minimums2.length);
        Assert.assertEquals(255.0, minimums2[0], 1E-10);
        assertNoData(test2I, null);

        // single band value exceed the byte upper bound
        ImageWorker test3I = new ImageWorker(test3);
        final double[] maximums3a = test3I.getMaximums();
        final double[] minimums3a = test3I.getMinimums();
        test3I.rescaleToBytes();
        Assert.assertEquals("Rescale", test3I.getRenderedOperation().getOperationName());
        final double[] maximums3b = test3I.getMaximums();
        final double[] minimums3b = test3I.getMinimums();
        assertNoData(test3I, null);

        if (maximums3a[0] > 255) {
            Assert.assertTrue(Math.abs(maximums3a[0] - maximums3b[0]) > 1E-10);
            Assert.assertTrue(Math.abs(255.0 - maximums3b[0]) >= 0);
        }

        if (minimums3a[0] < 0) {
            Assert.assertTrue(minimums3b[0] >= 0);
        }

        // single band value does not exceed the byte upper bound
        ImageWorker test4I = new ImageWorker(test4);
        final double[] maximums4a = test4I.getMaximums();
        final double[] minimums4a = test4I.getMinimums();
        test4I.rescaleToBytes();
        Assert.assertEquals("Format", test4I.getRenderedOperation().getOperationName());
        final double[] maximums4b = test4I.getMaximums();
        final double[] minimums4b = test4I.getMinimums();
        Assert.assertEquals(maximums4a[0], maximums4b[0], 1E-10);
        Assert.assertEquals(minimums4a[0], minimums4b[0], 1E-10);
        assertNoData(test4I, null);

        // now test multibands case
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(test2).addSource(test3);
        final RenderedImage multiband =
                JAI.create("BandMerge", pb, null); // BandMergeDescriptor.create(test2, test3, null);
        ImageWorker testmultibandI = new ImageWorker(multiband);
        final double[] maximums5a = testmultibandI.getMaximums();
        final double[] minimums5a = testmultibandI.getMinimums();
        testmultibandI.rescaleToBytes().setNoData(null);
        final double[] maximums5b = testmultibandI.getMaximums();
        final double[] minimums5b = testmultibandI.getMinimums();
        Assert.assertEquals(maximums5a[0], maximums5b[0], 1E-10);
        Assert.assertEquals(minimums5a[0], minimums5b[0], 1E-10);
        assertNoData(testmultibandI, null);

        Assert.assertTrue(Math.abs(maximums5a[1] - maximums5b[1]) > 1E-10);
        Assert.assertTrue(Math.abs(minimums5a[1] - minimums5b[1]) > 1E-10);
    }

    @Test
    public void rescaleToBytesNoDataNegative() {
        rescaleToBytesNoData(-10);
    }

    @Test
    public void rescaleToBytesNoDataZero() {
        rescaleToBytesNoData(0);
    }

    private static void rescaleToBytesNoData(int minValue) {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());

        // set up synthetic images for testing
        final RenderedImage test3 = getSynthetic(20000);
        ImageWorker worker = new ImageWorker(test3);
        Range noData = RangeFactory.convert(
                RangeFactory.create(minValue, minValue), test3.getSampleModel().getDataType());
        worker.setNoData(noData);
        worker.rescaleToBytes();
        RenderedImage image = worker.getRenderedImage();

        // check the nodata has been actually set and remapped to zero
        Object property = image.getProperty(NoDataContainer.GC_NODATA);
        assertNotEquals(property, Image.UndefinedProperty);
        assertThat(property, instanceOf(NoDataContainer.class));
        NoDataContainer nd = (NoDataContainer) property;
        assertEquals(0, nd.getAsSingleValue(), 0d);

        // check the min max are in the range 1 - 255 since 0 has been mapped to new no data in Byte
        // Range.
        double[] min = worker.getMinimums();
        double[] max = worker.getMaximums();
        assertTrue(1 <= min[0] && min[0] <= max[0] && max[0] <= 255);
    }

    /** Tests the {@link ImageWorker#makeColorTransparent} methods. Some trivial tests are performed before. */
    @Test
    public void testMakeColorTransparent() throws IllegalStateException, FileNotFoundException, IOException {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(sstImage);

        assertSame(sstImage, worker.getRenderedImage());
        assertEquals(1, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isBinary());
        assertTrue(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertFalse(worker.isTranslucent());
        assertNoData(worker, null);

        assertSame(
                "Expected no operation.",
                sstImage,
                worker.forceIndexColorModel(false).getRenderedImage());
        assertSame(
                "Expected no operation.",
                sstImage,
                worker.forceIndexColorModel(true).getRenderedImage());
        assertSame(
                "Expected no operation.", sstImage, worker.forceColorSpaceRGB().getRenderedImage());
        assertSame("Expected no operation.", sstImage, worker.retainFirstBand().getRenderedImage());
        assertSame("Expected no operation.", sstImage, worker.retainLastBand().getRenderedImage());

        // Following will change image, so we need to test after the above assertions.
        assertEquals(0, worker.getMinimums()[0], 0);
        assertEquals(255, worker.getMaximums()[0], 0);
        assertNotSame(sstImage, worker.getRenderedImage());
        assertSame(
                "Expected same databuffer, i.e. pixels should not be duplicated.",
                sstImage.getTile(0, 0).getDataBuffer(),
                worker.getRenderedImage().getTile(0, 0).getDataBuffer());

        assertSame(worker, worker.makeColorTransparent(Color.WHITE));
        assertEquals(255, worker.getTransparentPixel());
        assertFalse(worker.isTranslucent());
        assertSame(
                "Expected same databuffer, i.e. pixels should not be duplicated.",
                sstImage.getTile(0, 0).getDataBuffer(),
                worker.getRenderedImage().getTile(0, 0).getDataBuffer());
        assertNoData(worker, null);

        // INDEX TO INDEX-ALPHA
        worker = new ImageWorker(chlImage).makeColorTransparent(Color.black);
        show(worker, "CHL01195.png");
        assertEquals(1, worker.getNumBands());
        assertEquals(0, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertTrue(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertFalse(worker.isTranslucent());
        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof IndexColorModel);
        IndexColorModel iColorModel = (IndexColorModel) image.getColorModel();
        int transparentColor = iColorModel.getRGB(worker.getTransparentPixel()) & 0x00ffffff;
        assertEquals(0, transparentColor);
        assertNoData(image, null);

        // INDEX TO INDEX-ALPHA
        worker = new ImageWorker(bathy).makeColorTransparent(Color.WHITE);
        show(worker, "BATHY.png");
        assertEquals(1, worker.getNumBands());
        assertEquals(206, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertTrue(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertFalse(worker.isTranslucent());
        image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof IndexColorModel);
        iColorModel = (IndexColorModel) image.getColorModel();
        transparentColor = iColorModel.getRGB(worker.getTransparentPixel()) & 0x00ffffff;
        assertEquals(transparentColor, Color.WHITE.getRGB() & 0x00ffffff);
        assertNoData(image, null);

        // RGB TO RGBA
        worker = new ImageWorker(smallWorld).makeColorTransparent(new Color(11, 10, 50));
        show(worker, "small_world.png");
        assertEquals(4, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertTrue(worker.isTranslucent());
        image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertNoData(image, null);

        // RGBA to RGBA
        worker = new ImageWorker(worldImage).makeColorTransparent(Color.white);
        show(worker, "world.png");
        assertEquals(4, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertTrue(worker.isTranslucent());
        image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertNoData(image, null);

        // GRAY TO GRAY-ALPHA
        worker = new ImageWorker(gray).makeColorTransparent(Color.black);
        show(worker, "gray.png");
        assertEquals(2, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isIndexed());
        assertFalse(worker.isColorSpaceRGB());
        assertTrue(worker.isColorSpaceGRAYScale());
        assertTrue(worker.isTranslucent());
        image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertNoData(image, null);

        // GRAY-ALPHA TO GRAY-ALPHA.
        worker = new ImageWorker(grayAlpha).makeColorTransparent(Color.black);
        show(worker, "gray-alpha.png");
        assertEquals(2, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isIndexed());
        assertFalse(worker.isColorSpaceRGB());
        assertTrue(worker.isColorSpaceGRAYScale());
        assertTrue(worker.isTranslucent());
        image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertNoData(image, null);
    }

    /** Tests the {@link ImageWorker#tile()} methods. Some trivial tests are performed before. */
    @Test
    public void testReTile() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(worldImage);

        assertSame(worldImage, worker.getRenderedImage());
        assertNoData(worker.getRenderedImage(), null);
        assertEquals(4, worker.getNumBands());
        assertEquals(-1, worker.getTransparentPixel());
        assertTrue(worker.isBytes());
        assertFalse(worker.isBinary());
        assertFalse(worker.isIndexed());
        assertTrue(worker.isColorSpaceRGB());
        assertFalse(worker.isColorSpaceGRAYScale());
        assertTrue(worker.isTranslucent());

        assertSame("Expected no operation.", worldImage, worker.rescaleToBytes().getRenderedImage());
        assertSame(
                "Expected no operation.",
                worldImage,
                worker.forceComponentColorModel().getRenderedImage());
        assertSame(
                "Expected no operation.",
                worldImage,
                worker.forceColorSpaceRGB().getRenderedImage());
        assertSame("Expected no operation.", worldImage, worker.retainBands(4).getRenderedImage());

        // Following will change image, so we need to test after the above assertions.
        worker.setRenderingHint(
                JAI.KEY_IMAGE_LAYOUT,
                new ImageLayout()
                        .setTileGridXOffset(0)
                        .setTileGridYOffset(0)
                        .setTileHeight(64)
                        .setTileWidth(64));
        worker.tile();
        assertSame("Expected 64.", 64, worker.getRenderedImage().getTileWidth());
        assertSame("Expected 64.", 64, worker.getRenderedImage().getTileHeight());
    }

    /**
     * Visualize the content of given image if {@link #SHOW} is {@code true}.
     *
     * @param worker The worker for which to visualize the image.
     * @param title The title to be given to the windows.
     */
    private static void show(final ImageWorker worker, final String title) {
        if (SHOW) {
            Viewer.show(worker.getRenderedImage(), title);
        } else {
            assertNotNull(worker.getRenderedImage()
                    .getTile(
                            worker.getRenderedImage().getMinTileX(),
                            worker.getRenderedImage().getMinTileY())); // Force computation.
        }
    }

    @Test
    public void testOpacityAlphaRGBComponent() {
        testAlphaRGB(false);
    }

    @Test
    public void testOpacityAlphaRGBDirect() {
        testAlphaRGB(true);
    }

    @Test
    @SuppressWarnings("PMD.SystemPrintln")
    public void testYCbCr() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        // check the presence of the PYCC.pf file that contains the profile for the YCbCr color
        // space
        if (ImageWorker.CS_PYCC == null) {
            System.out.println("testYCbCr disabled since we are unable to locate the YCbCr color profile");
            return;
        }
        // RGB component color model
        ImageWorker worker = new ImageWorker(getSyntheticRGB(false));

        RenderedImage image = worker.getRenderedImage();
        assertNoData(image, null);
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertFalse(image.getColorModel().hasAlpha());
        int sample = image.getTile(0, 0).getSample(0, 0, 2);
        assertEquals(0, sample);

        assertFalse(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceYCbCr();
        assertTrue(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceRGB();
        assertFalse(worker.isColorSpaceYCbCr());
        assertTrue(worker.isColorSpaceRGB());

        // RGB Palette
        worker.forceBitmaskIndexColorModel();
        image = worker.getRenderedImage();
        assertNoData(image, null);
        assertTrue(image.getColorModel() instanceof IndexColorModel);
        assertFalse(image.getColorModel().hasAlpha());

        assertFalse(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceYCbCr();
        assertTrue(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceRGB();
        assertFalse(worker.isColorSpaceYCbCr());
        assertTrue(worker.isColorSpaceRGB());

        // RGB DirectColorModel
        worker = new ImageWorker(getSyntheticRGB(true));
        image = worker.getRenderedImage();
        assertNoData(image, null);
        assertTrue(image.getColorModel() instanceof DirectColorModel);
        assertFalse(image.getColorModel().hasAlpha());
        sample = image.getTile(0, 0).getSample(0, 0, 2);
        assertEquals(0, sample);

        assertFalse(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceYCbCr();
        assertTrue(worker.isColorSpaceYCbCr());
        worker.forceColorSpaceRGB();
        assertFalse(worker.isColorSpaceYCbCr());
        assertTrue(worker.isColorSpaceRGB());
    }

    private void testAlphaRGB(boolean direct) {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(getSyntheticRGB(direct));
        worker.applyOpacity(0.5f);

        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        assertNoData(image, null);
        int sample = image.getTile(0, 0).getSample(0, 0, 3);
        assertEquals(128, sample);
    }

    @Test
    public void testOpacityRGBA() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        assertTrue(worldImage.getColorModel().hasAlpha());
        assertTrue(worldImage.getColorModel() instanceof ComponentColorModel);
        ImageWorker worker = new ImageWorker(worldImage);
        worker.applyOpacity(0.5f);

        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        assertNoData(image, null);
        Raster tile = worldImage.getTile(0, 0);
        Raster outputTile = image.getTile(0, 0);
        for (int i = 0; i < tile.getWidth(); i++) {
            for (int j = 0; j < tile.getHeight(); j++) {
                int original = tile.getSample(i, j, 3);
                int result = outputTile.getSample(i, j, 3);
                assertEquals(Math.round(original * 0.5), result);
            }
        }
    }

    @Test
    public void testOpacityGray() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(gray);
        worker.applyOpacity(0.5f);

        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        int sample = image.getTile(0, 0).getSample(0, 0, 1);
        assertEquals(128, sample);
        assertNoData(image, null);
    }

    @Test
    public void testOpacityGrayROI() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(gray);
        worker.setROI(new ROIShape(new Rectangle(1, 1, 1, 1)));
        worker.applyOpacity(0.5f);

        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        int sample = image.getTile(0, 0).getSample(0, 0, 1);
        assertEquals(0, sample);
        assertNoData(image, null);
    }

    @Test
    public void testOpacityGrayNoData() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(gray);
        Range noData = RangeFactory.convert(
                RangeFactory.create(255, 255), gray.getSampleModel().getDataType());
        worker.setNoData(noData);
        worker.applyOpacity(0.5f);

        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        int sample = image.getTile(0, 0).getSample(0, 0, 1);
        assertEquals(0, sample);
        assertNoData(image, noData);
    }

    @Test
    public void testNoDataBackground() throws IOException {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        PlanarImage inputImage = PlanarImage.wrapRenderedImage(imageWithNodata);
        double noDataValue = -30000d;
        NoDataContainer noData = new NoDataContainer(noDataValue);
        inputImage.setProperty(NoDataContainer.GC_NODATA, noData);

        ImageWorker worker = new ImageWorker();
        final int w = imageWithNodata.getWidth();
        final int h = imageWithNodata.getHeight();

        // Specify an ImageLayout to build a mosaic being 2 times (along x and y) the size of the
        // original image.
        ImageLayout layout = new ImageLayout(
                imageWithNodata.getMinX(),
                imageWithNodata.getMinY(),
                w * 2,
                h * 2,
                imageWithNodata.getTileGridXOffset(),
                imageWithNodata.getTileGridYOffset(),
                imageWithNodata.getTileWidth(),
                imageWithNodata.getTileHeight(),
                imageWithNodata.getSampleModel(),
                imageWithNodata.getColorModel());
        worker.setRenderingHint(JAI.KEY_IMAGE_LAYOUT, layout);

        // Perform the mosaicking operation
        RenderedImage image = worker.mosaic(
                        new RenderedImage[] {inputImage}, MosaicDescriptor.MOSAIC_TYPE_OVERLAY, null, null, null, null)
                .getRenderedImage();

        // querying the pixels outside of the original image extent have been filled with nodata
        double sample = image.getData().getSample(w + 10, h + 10, 0);
        assertNoData(image, noData.getAsRange());
        assertEquals(noDataValue, sample, 1E-6);
    }

    @Test
    public void testOpacityGrayAlpha() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(gray);
        worker.applyOpacity(0.5f);

        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof ComponentColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        assertNoData(image, null);
        Raster tile = gray.getTile(0, 0);
        Raster outputTile = image.getTile(0, 0);
        for (int i = 0; i < tile.getWidth(); i++) {
            for (int j = 0; j < tile.getHeight(); j++) {
                int original = tile.getSample(i, j, 1);
                int result = outputTile.getSample(i, j, 1);
                assertEquals(Math.round(original * 0.5), result);
            }
        }
    }

    @Test
    public void testOpacityIndexed() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        assertFalse(worldDEMImage.getColorModel().hasAlpha());
        ImageWorker worker = new ImageWorker(worldDEMImage);
        worker.applyOpacity(0.5f);

        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof IndexColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        assertNoData(image, null);

        // check the resulting palette
        IndexColorModel index = (IndexColorModel) image.getColorModel();
        for (int i = 0; i < index.getMapSize(); i++) {
            assertEquals(128, index.getAlpha(i));
        }
    }

    @Test
    public void testOpacityIndexedTranslucent() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        assertFalse(worldDEMImage.getColorModel().hasAlpha());
        final BufferedImage input = getSyntheticTranslucentIndexed();
        ImageWorker worker = new ImageWorker(input);
        worker.applyOpacity(0.5f);

        RenderedImage image = worker.getRenderedImage();
        assertTrue(image.getColorModel() instanceof IndexColorModel);
        assertTrue(image.getColorModel().hasAlpha());
        assertNoData(image, null);

        // check the resulting palette
        IndexColorModel outputCM = (IndexColorModel) image.getColorModel();
        IndexColorModel inputCM = (IndexColorModel) input.getColorModel();
        for (int i = 0; i < inputCM.getMapSize(); i++) {
            assertEquals(Math.round(inputCM.getAlpha(i) * 0.5), outputCM.getAlpha(i));
        }
    }

    @Test
    public void testIndexedRGBNoDataPrepareForRendering() {
        RenderedImage image = getIndexedRGBNodata();
        ImageWorker iw = new ImageWorker(image);
        iw.prepareForRendering();
        RenderedImage expanded = iw.getRenderedImage();

        // nodata forced expansion
        assertTrue(expanded.getColorModel() instanceof ComponentColorModel);
        assertTrue(expanded.getColorModel().hasAlpha());
        assertNoData(expanded, null);
        // check the value has been made transparent
        assertEquals(
                0,
                expanded.getData().getSample(expanded.getMinX() + expanded.getWidth() / 3 * 2, expanded.getMinY(), 3));
    }

    @Test
    public void testIndexedRGBNoDataForceComponentColorModel() {
        RenderedImage image = getIndexedRGBNodata();
        ImageWorker iw = new ImageWorker(image);
        iw.setBackground(new double[] {0, 0, 255});
        iw.forceComponentColorModel();
        RenderedImage expanded = iw.getRenderedImage();

        // nodata forced expansion
        assertTrue(expanded.getColorModel() instanceof ComponentColorModel);
        assertTrue(expanded.getColorModel().hasAlpha());
        // assertNoData(expanded, null);
        // check the value has been made transparent
        assertEquals(
                0,
                expanded.getData().getSample(expanded.getMinX() + expanded.getWidth() / 3 * 2, expanded.getMinY(), 3));
    }

    @Test
    public void testIndexedRGBNoDataForceComponentColorModelNoAlpha() {
        RenderedImage image = getIndexedRGBNodata();
        ImageWorker iw = new ImageWorker(image);
        iw.setBackground(new double[] {0, 0, 255});
        iw.forceComponentColorModel(false, true, true);
        RenderedImage expanded = iw.getRenderedImage();

        // nodata forced expansion
        assertTrue(expanded.getColorModel() instanceof ComponentColorModel);

        // Make sure there is noAlpha
        assertFalse(expanded.getColorModel().hasAlpha());
        assertEquals(3, expanded.getColorModel().getNumComponents());
    }

    @Test
    public void testOptimizeAffine() throws Exception {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        ImageWorker iw = new ImageWorker(bi);

        // apply straight translation
        AffineTransform at = AffineTransform.getTranslateInstance(100, 100);
        iw.affine(at, null, null);
        RenderedImage t1 = iw.getRenderedImage();
        assertEquals(100, t1.getMinX());
        assertEquals(100, t1.getMinY());
        assertNoData(t1, null);

        // now go back
        AffineTransform atInverse = AffineTransform.getTranslateInstance(-100, -100);
        iw.affine(atInverse, null, null);
        RenderedImage t2 = iw.getRenderedImage();
        assertEquals(0, t2.getMinX());
        assertEquals(0, t2.getMinY());
        assertSame(bi, t2);
        assertNoData(t2, null);
    }

    @Test
    public void testTileSizeScale() throws Exception {
        // apply straight translation
        AffineTransform at = AffineTransform.getScaleInstance(1000, 1000);
        testTileSize(at);
    }

    @Test
    public void testTileSizeGenericAffine() throws Exception {
        // apply straight translation
        AffineTransform at = new AffineTransform(100, 0.5, -0.5, 100, 20, 20);
        testTileSize(at);
    }

    private void testTileSize(AffineTransform at) {
        BufferedImage bi = new BufferedImage(4, 4, BufferedImage.TYPE_3BYTE_BGR);
        ImageWorker iw = new ImageWorker(bi);
        iw.affine(at, null, null);
        RenderedImage t1 = iw.getRenderedImage();
        assertEquals(512, t1.getTileWidth());
        assertEquals(512, t1.getTileHeight());
    }

    @Test
    public void testAffineNegative() throws Exception {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        ImageWorker iw = new ImageWorker(bi);

        // flipping tx, not a scale, used to blow
        AffineTransform at = AffineTransform.getScaleInstance(-1, -1);
        iw.affine(at, null, null);
        RenderedImage t1 = iw.getRenderedImage();
        assertEquals(-100, t1.getMinX());
        assertEquals(-100, t1.getMinY());
        assertNoData(t1, null);
    }

    @Test
    public void testOptimizedWarp() throws Exception {
        // do it again, make sure the image does not turn black since
        GridCoverage2D ushortCoverage = EXAMPLES.get(5);
        GridCoverage2D coverage = project(ushortCoverage, CRS.parseWKT(GOOGLE_MERCATOR_WKT), null, "nearest", null);
        RenderedImage ri = coverage.getRenderedImage();

        ImageWorker.WARP_REDUCTION_ENABLED = false;
        AffineTransform at = new AffineTransform(0.4, 0, 0, 0.5, -200, -200);
        RenderedOp fullChain = (RenderedOp) new ImageWorker(ri)
                .affine(at, Interpolation.getInstance(Interpolation.INTERP_NEAREST), new double[] {0})
                .getRenderedImage();
        assertEquals(ImageWorker.SCALE_OP_NAME, fullChain.getOperationName());
        fullChain.getTiles();
        assertNoData(fullChain, null);

        ImageWorker.WARP_REDUCTION_ENABLED = true;
        RenderedOp reduced = (RenderedOp) new ImageWorker(ri)
                .affine(at, Interpolation.getInstance(Interpolation.INTERP_NEAREST), new double[] {0})
                .getRenderedImage();
        // force computation, to make sure it does not throw exceptions
        reduced.getTiles();
        // check the chain has been reduced
        assertEquals("Warp", reduced.getOperationName());
        assertEquals(1, reduced.getSources().size());
        assertSame(ushortCoverage.getRenderedImage(), reduced.getSourceImage(0));
        assertNoData(reduced, null);

        // check the bounds of the output image has not changed
        assertEquals(fullChain.getBounds(), reduced.getBounds());

        // check we are getting a reasonable tile size and origin (JAI warp_affine will generate
        // odd results otherwise
        assertEquals(0, reduced.getTileGridXOffset());
        assertEquals(0, reduced.getTileGridYOffset());
        assertEquals(ushortCoverage.getRenderedImage().getTileWidth(), reduced.getTileWidth());
        assertEquals(ushortCoverage.getRenderedImage().getTileHeight(), reduced.getTileHeight());
    }

    @Test
    public void testWarpAffinePreserveBackgorund() throws Exception {
        // pick a RGB image, do warp and then affine
        double[] background = {1, 2, 3};
        GridCoverage2D warped =
                project(EXAMPLES.get(0), CRS.parseWKT(GOOGLE_MERCATOR_WKT), null, "nearest", background, null);

        ImageWorker iwa = new ImageWorker(warped.getRenderedImage());
        iwa.affine(
                AffineTransform.getScaleInstance(0.5, 0.5),
                Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                null);
        RenderedOp reduced = (RenderedOp) iwa.getRenderedImage();
        assertEquals("Warp", reduced.getOperationName());
        assertEquals(background, reduced.getParameterBlock().getObjectParameter(2));
    }

    @Test
    public void testOptimizedWarpOnLargeUpscale() throws Exception {
        BufferedImage bi = new BufferedImage(4, 4, BufferedImage.TYPE_BYTE_INDEXED);
        GridCoverage2D source = new GridCoverageFactory()
                .create("Test", bi, new ReferencedEnvelope(0, 1, 0, 1, DefaultGeographicCRS.WGS84));
        GridCoverage2D coverage = project(source, CRS.parseWKT(GOOGLE_MERCATOR_WKT), null, "nearest", null);
        RenderedImage ri = coverage.getRenderedImage();

        checkTileSize(ri, AffineTransform.getScaleInstance(100, 100));
        checkTileSize(ri, new AffineTransform(100, 0, 0, 100, 10, 10));
    }

    private void checkTileSize(RenderedImage ri, AffineTransform scale) {
        final Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        RenderedOp reduced = (RenderedOp) new ImageWorker(ri)
                .affine(scale, interpolation, new double[] {0})
                .getRenderedImage();
        // the tile size is not 4x4
        assertEquals(400, reduced.getTileWidth());
        assertEquals(400, reduced.getTileHeight());
    }

    @Test
    public void testRescaleNoData() {
        ImageWorker w = new ImageWorker(imageWithNodata2);
        // Setting NoData
        Range noData = RangeFactory.create(-10000, -10000);
        w.setNoData(noData);
        w.setBackground(new double[] {0});
        w.rescale(new double[] {0.002}, new double[] {2});

        // Getting Minimum value, It cannot be equal or lower than the offset value
        double minimum = w.getMinimums()[0];
        assertTrue(minimum > 2);
        assertNoData(w.getRenderedImage(), RangeFactory.create((byte) 0, (byte) 0));
    }

    @Test
    public void testLookupROI() {
        // Getting input Palette image
        ImageWorker w = new ImageWorker(chlImage);
        // Forcing component colormodel
        w.forceComponentColorModel();
        // Applying a lookup table
        byte[] data = new byte[256];
        // Setting all the values to 50
        Arrays.fill(data, (byte) 50);
        LookupTable table = LookupTableFactory.create(data);
        // Add a ROI
        ROI roi = new ROIShape(new Rectangle(
                chlImage.getMinX(), chlImage.getMinY(), chlImage.getWidth() / 2, chlImage.getHeight() / 2));
        w.setROI(roi);
        // Setting Background to 0
        w.setBackground(new double[] {0});
        // Appliyng lookup
        w.lookup(table);
        // Removing NoData and ROI and calculate the statistics on the whole image
        w.setNoData(null);
        w.setROI(null);
        // Calculating the minimum and maximum
        double min = w.getMinimums()[0];
        double max = w.getMaximums()[0];

        // Ensuring minimum is 0 and maximum 50
        assertEquals(min, 0, 1E-7);
        assertEquals(max, 50, 1E-7);

        assertNoData(w.getRenderedImage(), null);
    }

    @Test
    public void testDoubleCrop() {
        ImageWorker iw = new ImageWorker(gray);
        iw.crop(10, 10, 50, 50);
        RenderedImage ri1 = iw.getRenderedImage();

        assertEquals(10, ri1.getMinX());
        assertEquals(10, ri1.getMinY());
        assertEquals(50, ri1.getWidth());
        assertEquals(50, ri1.getHeight());

        // the crop area overlaps with the image
        iw.crop(30, 30, 60, 60);
        RenderedImage ri2 = iw.getRenderedImage();
        assertEquals(30, ri2.getMinX());
        assertEquals(30, ri2.getMinY());
        assertEquals(30, ri2.getWidth());
        assertEquals(30, ri2.getHeight());

        // check intermediate crop elimination
        RenderedOp op = (RenderedOp) ri2;
        assertEquals(gray, op.getSourceObject(0));
        assertNoData(op, null);
    }

    @Test
    public void testNanCrop() {
        ImageWorker iw = new ImageWorker(imageWithNan);
        iw.crop(10, 10, 50, 50);
        RenderedImage ri = iw.getRenderedImage();

        assertEquals(10, ri.getMinX());
        assertEquals(10, ri.getMinY());
        assertEquals(50, ri.getWidth());
        assertEquals(50, ri.getHeight());

        // check the nodata has been preserved
        iw.setImage(ri);
        Range noData = iw.getNoData();
        assertNotNull(noData);
        assertTrue(Double.isNaN(noData.getMin().doubleValue()));
        assertTrue(Double.isNaN(noData.getMax().doubleValue()));
    }

    @Test
    public void testNanRescale() {
        ImageWorker iw = new ImageWorker(imageWithNan);
        iw.rescale(new double[] {0, 0.1}, new double[] {10});
        RenderedImage ri = iw.getRenderedImage();
        // check the nodata has been preserved
        iw.setImage(ri);
        Range noData = iw.getNoData();
        assertNotNull(noData);
        assertTrue(Double.isNaN(noData.getMin().doubleValue()));
        assertTrue(Double.isNaN(noData.getMax().doubleValue()));
    }

    @Test
    public void testAddBands() {
        ImageWorker iw = new ImageWorker(gray).retainBands(1);
        RenderedImage input = iw.getRenderedImage();
        RenderedImage image = iw.addBands(new RenderedImage[] {input, input, input, input}, false, null)
                .getRenderedImage();
        assertEquals(4, image.getTile(0, 0).getSampleModel().getNumBands());
        assertNoData(image, null);
    }

    @Test
    public void testBandMerge() {
        ImageWorker iw = new ImageWorker(gray).retainBands(1);
        RenderedImage image = iw.bandMerge(4).getRenderedImage();
        assertEquals(4, image.getTile(0, 0).getSampleModel().getNumBands());
        assertNoData(image, null);
    }

    @Test
    public void testBandMergeWithNodata() throws FileNotFoundException, IOException {
        double noDataValue = -10000d;
        Range nodata = RangeFactory.create(noDataValue, noDataValue);
        double[] background = {noDataValue};

        PlanarImage pi = PlanarImage.wrapRenderedImage(imageWithNodata2);
        pi.setProperty("GC_NODATA", new NoDataContainer(nodata));

        ImageWorker worker = new ImageWorker(pi).setNoData(nodata).setBackground(background);

        RenderedImage twoBands =
                worker.addBand(worker.getRenderedImage(), false).getRenderedImage();
        RenderedImage oneBand = new ImageWorker(twoBands).retainBands(1).getRenderedImage();
        RenderedImage threeBands =
                new ImageWorker(twoBands).addBand(oneBand, false).getRenderedImage();

        // Check that the noData holes are still noData after bandMerge
        double[] threeSamples = new double[3];
        threeBands.getData().getPixel(18, 18, threeSamples);
        for (double sample : threeSamples) {
            assertEquals(noDataValue, sample, 1E-6);
        }
    }

    static void assertNoData(ImageWorker worker, Range nodata) {
        assertNoData(worker.getRenderedImage(), nodata);
    }

    static void assertNoData(RenderedImage image, Range nodata) {
        Object property = image.getProperty(NoDataContainer.GC_NODATA);
        if (nodata == null) {
            // image properties return an instance of Object in case the property is not found
            assertEquals("We expect lack of noData, but one was found", Object.class, property.getClass());
        } else {
            NoDataContainer container = (NoDataContainer) property;
            assertEquals(nodata, container.getAsRange());
        }
    }

    @Test
    public void testMosaicRasterROI() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROI(new ROIShape(new Rectangle2D.Double(0, 0, 64, 64)).getAsImage());

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROI(new ROIShape(new Rectangle2D.Double(63, 63, 64, 64)).getAsImage());

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    @Test
    public void testMosaicRasterROI2() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROI(getSyntheticSolidGray((byte) 1), 1)
                .subtract(new ROIShape(new Rectangle(0, 64, 128, 128)))
                .subtract(new ROIShape(new Rectangle(64, 0, 64, 128)));

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROI(new ROIShape(new Rectangle2D.Double(63, 63, 64, 64)).getAsImage());

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    @Test
    public void testMosaicShapeROI() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROIShape(new Rectangle2D.Double(0, 0, 64, 64));

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROIShape(new Rectangle2D.Double(63, 63, 64, 64));

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    @Test
    public void testMosaicShapeRasterROI() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROIShape(new Rectangle2D.Double(0, 0, 64, 64));

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROI(new ROIShape(new Rectangle2D.Double(63, 63, 64, 64)).getAsImage());

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    @Test
    public void testMosaicRasterShapeROI() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROI(new ROIShape(new Rectangle2D.Double(0, 0, 64, 64)).getAsImage());

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROIShape(new Rectangle2D.Double(63, 63, 64, 64));

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    @Test
    public void testMosaicGeometryROI() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROIGeometry(JTS.toGeometry(new Envelope(0, 64, 0, 64)));

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROIGeometry(JTS.toGeometry(new Envelope(63, 127, 63, 127)));

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    @Test
    public void testMosaicGeometryShapeROI() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROIGeometry(JTS.toGeometry(new Envelope(0, 64, 0, 64)));

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROIShape(new Rectangle2D.Double(63, 63, 64, 64));

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    @Test
    public void testMosaicShapeGeometryROI() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROIShape(new Rectangle2D.Double(0, 0, 64, 64));

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROIGeometry(JTS.toGeometry(new Envelope(63, 127, 63, 127)));

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    private void testMosaicRedBlue(BufferedImage red, ROI redROI, BufferedImage blue, ROI blueROI) {
        ImageWorker iw = new ImageWorker();
        iw.mosaic(
                new RenderedImage[] {red, blue},
                MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                null,
                new ROI[] {redROI, blueROI},
                null,
                null);
        RenderedImage mosaicked = iw.getRenderedImage();
        Object roiProperty = mosaicked.getProperty("ROI");
        assertThat(roiProperty, instanceOf(ROI.class));
        ROI roi = (ROI) roiProperty;
        // check ROI
        assertTrue(roi.contains(20, 20));
        assertTrue(roi.contains(120, 120));
        assertFalse(roi.contains(20, 120));
        assertFalse(roi.contains(120, 20));
    }

    @Test
    public void testMosaicNullROI() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROI(new ROIShape(new Rectangle2D.Double(0, 0, 64, 64)).getAsImage());

        BufferedImage blue = getSyntheticRGB(Color.BLUE);

        // inject a null roi, should be treated as full image valid for that granule
        ImageWorker iw = new ImageWorker();
        iw.mosaic(
                new RenderedImage[] {red, blue},
                MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                null,
                new ROI[] {redROI, null},
                null,
                null);
        RenderedImage mosaicked = iw.getRenderedImage();
        Object roiProperty = mosaicked.getProperty("ROI");
        assertThat(roiProperty, instanceOf(ROI.class));
        ROI roi = (ROI) roiProperty;
        // check ROI, should be full
        assertTrue(roi.contains(new Rectangle(0, 0, mosaicked.getWidth(), mosaicked.getHeight())));
    }

    @Test
    public void testMosaicRasterGeometry() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROI(new ROIShape(new Rectangle2D.Double(0, 0, 64, 64)).getAsImage());

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROIGeometry(JTS.toGeometry(new Envelope(63, 127, 63, 127)));

        testMosaicRedBlue(red, redROI, blue, blueROI);
    }

    @Test
    public void testMosaicBackgroundColor() {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROI(new ROIShape(new Rectangle2D.Double(0, 0, 64, 64)).getAsImage());

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROIGeometry(JTS.toGeometry(new Envelope(63, 127, 63, 127)));

        ImageWorker iw = new ImageWorker();
        iw.setBackground(new double[] {255, 255, 255});
        iw.mosaic(
                new RenderedImage[] {red, blue},
                MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                null,
                new ROI[] {redROI, blueROI},
                null,
                null);
        RenderedImage mosaicked = iw.getRenderedImage();
        Object roiProperty = mosaicked.getProperty("ROI");
        assertThat(roiProperty, not(instanceOf(ROI.class)));
    }

    @Test
    public void testPrepareRenderingAlphaOnNoData() {

        // All image pixels are initialized to RED.
        BufferedImage red = getSyntheticRGB(Color.RED);
        final int w = red.getWidth();
        final int h = red.getHeight();
        final int specialPixelsOriginX = w / 4;
        final int specialPixelsOriginY = h / 4;

        // Setting the top left quarter of the image to all zeros
        int[] zero = {0, 0, 0};
        WritableRaster raster = red.getRaster();
        for (int i = 0; i < specialPixelsOriginX; i++) {
            for (int j = 0; j < specialPixelsOriginY; j++) {
                raster.setPixel(i, j, zero);
            }
        }

        // setting some special pixels to colors with up to 2 bands equal to zero.
        final int specialPixels = 9;
        final int[][] specialPixelsValues = new int[specialPixels][];

        specialPixelsValues[0] = new int[] {0, 0, 255};
        specialPixelsValues[1] = new int[] {0, 255, 0};
        specialPixelsValues[2] = new int[] {255, 0, 0};

        specialPixelsValues[3] = new int[] {0, 255, 255};
        specialPixelsValues[4] = new int[] {255, 0, 255};
        specialPixelsValues[5] = new int[] {255, 255, 0};

        specialPixelsValues[6] = new int[] {128, 0, 255};
        specialPixelsValues[7] = new int[] {0, 128, 64};
        specialPixelsValues[8] = new int[] {128, 128, 0};

        for (int k = 0; k < specialPixels; k++) {
            raster.setPixel(specialPixelsOriginX + k, specialPixelsOriginY + k, specialPixelsValues[k]);
        }

        ImageWorker iw = new ImageWorker(red);
        Range noData = RangeFactory.create(0, 0);
        iw.setNoData(noData);
        iw.prepareForRendering();

        // Check alpha has been properly handled
        BufferedImage result = iw.getBufferedImage();
        SampleModel sm = result.getSampleModel();
        assertEquals(4, sm.getNumBands());
        ColorModel cm = result.getColorModel();
        assertTrue(cm.hasAlpha());
        WritableRaster updatedRaster = result.getRaster();

        // Checking alpha component for pixels having all zeros on the input image
        for (int i = 0; i < specialPixelsOriginX; i++) {
            for (int j = 0; j < specialPixelsOriginY; j++) {
                assertEquals(0, updatedRaster.getSample(i, j, 3));
            }
        }

        // Checking alpha component for pixels having up to 2 bands with zeros on the input image
        for (int i = specialPixelsOriginX; i < w; i++) {
            for (int j = specialPixelsOriginY; j < h; j++) {
                assertEquals(255, updatedRaster.getSample(i, j, 3));
            }
        }
    }

    @Test
    public void testMosaicBackgroundColorWithImagesOwningROI() {
        BufferedImage red = getSyntheticRGB(Color.RED);
        ROI redROI = new ROI(new ROIShape(new Rectangle2D.Double(0, 0, 64, 64)).getAsImage());
        RenderedImage redWithROI = new ImageWorker(red).setROI(redROI).getRenderedImage();

        BufferedImage blue = getSyntheticRGB(Color.BLUE);
        ROI blueROI = new ROIGeometry(JTS.toGeometry(new Envelope(63, 127, 63, 127)));
        RenderedImage blueWithROI = new ImageWorker(blue).setROI(blueROI).getRenderedImage();

        ImageWorker iw = new ImageWorker();
        iw.setBackground(new double[] {255, 255, 255});
        iw.mosaic(
                new RenderedImage[] {redWithROI, blueWithROI},
                MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                null,
                new ROI[] {redROI, blueROI},
                null,
                null);
        RenderedImage mosaicked = iw.getRenderedImage();
        // it has been replaced with a ROI geometry as big as the image since it cannot be removed
        // due to JAI picking the ROI of the mosaic from the first source
        Object roiProperty = mosaicked.getProperty("ROI");
        assertThat(roiProperty, instanceOf(ROIGeometry.class));
    }

    @Test
    public void testMosaicIndexedBackgroundColor() {
        BufferedImage gray = getSyntheticGrayIndexed(128);

        // test the case where the color is in the palette
        ImageWorker iw = new ImageWorker();
        iw.setBackground(new double[] {10, 10, 10});
        iw.mosaic(new RenderedImage[] {gray}, MosaicDescriptor.MOSAIC_TYPE_OVERLAY, null, null, null, null);
        RenderedImage ri = iw.getRenderedImage();
        assertThat(ri.getColorModel(), instanceOf(IndexColorModel.class));

        // and the case where it's not and we have to expand
        iw = new ImageWorker();
        iw.setBackground(new double[] {255, 255, 255});
        iw.mosaic(new RenderedImage[] {gray}, MosaicDescriptor.MOSAIC_TYPE_OVERLAY, null, null, null, null);
        ri = iw.getRenderedImage();
        assertThat(ri.getColorModel(), instanceOf(ComponentColorModel.class));
    }

    @Test
    public void testForceComponentColorModelIncompletePalette() throws IOException {
        // small palette, but the image has values above it
        BufferedImage bi = getSyntheticGrayIndexed(10);
        ImageWorker iw = new ImageWorker(bi);
        iw.setBackground(new double[] {255, 255, 255});
        iw.forceComponentColorModel();
        // used to crash here
        RenderedImage ri = iw.getRenderedImage();
        int[] pixel = new int[2];
        ri.getData().getPixel(256, 256, pixel);
        assertEquals(255, pixel[0]);
        assertEquals(255, pixel[1]);
    }

    @Test
    public void testWarpROITileSize() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(gray);
        worker.setROI(new ROIShape(new Rectangle(0, 0, gray.getWidth(), gray.getHeight())));
        RenderedImage image = worker.getRenderedImage();
        assertNotEquals(java.awt.Image.UndefinedProperty, image.getProperty("ROI"));

        ImageWorker iw = new ImageWorker(image);
        iw.warp(
                new WarpAffine(AffineTransform.getScaleInstance(0.5, 0.5)),
                Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        RenderedImage warped = iw.getRenderedImage();

        assertNotEquals(java.awt.Image.UndefinedProperty, warped.getProperty("ROI"));
        ROI warpedROI = (ROI) warped.getProperty("ROI");
        // turned into raster, the ROI has the same tile structure as the image
        assertEquals(ROI.class, warpedROI.getClass());
        assertEquals(warped.getTileWidth(), warpedROI.getAsImage().getTileWidth());
        assertEquals(warped.getTileHeight(), warpedROI.getAsImage().getTileHeight());
    }

    @Test
    public void testAffineROITileSize() {
        assertTrue("Assertions should be enabled.", ImageWorker.class.desiredAssertionStatus());
        ImageWorker worker = new ImageWorker(gray);
        ROIShape roiShape = new ROIShape(new Rectangle(0, 0, gray.getWidth(), gray.getHeight()));
        ROI roi = new ROI(roiShape.getAsImage());
        worker.setROI(roi);
        RenderedImage image = worker.getRenderedImage();
        assertNotEquals(java.awt.Image.UndefinedProperty, image.getProperty("ROI"));

        ImageWorker iw = new ImageWorker(image);
        iw.affine(
                AffineTransform.getScaleInstance(0.5, 0.5),
                Interpolation.getInstance(Interpolation.INTERP_NEAREST),
                null);
        RenderedImage scaled = iw.getRenderedImage();

        assertNotEquals(java.awt.Image.UndefinedProperty, scaled.getProperty("ROI"));
        ROI scaledROI = (ROI) scaled.getProperty("ROI");
        assertEquals(ROI.class, scaledROI.getClass());
        assertEquals(scaled.getTileWidth(), scaledROI.getAsImage().getTileWidth());
        assertEquals(scaled.getTileHeight(), scaledROI.getAsImage().getTileHeight());
    }

    @Test
    public void testStatsAfterCrop() throws IOException {
        final int width = 100;
        final int height = 100;
        final WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, 1, null);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, x + y);
            }
        }
        final ColorModel cm = new ComponentColorModelJAI(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        final BufferedImage image = new BufferedImage(cm, raster, false, null);
        ImageWorker worker = new ImageWorker(image);
        double[] maxs = worker.getMaximums();
        assertEquals(width + height - 2, (int) maxs[0]);

        worker = worker.crop(0, 0, width / 2, height / 2);
        maxs = worker.getMaximums();
        assertEquals(width / 2 + height / 2 - 2, (int) maxs[0]);
    }

    @Test
    public void testWarpROIWithJAIExt() throws IOException, TransformException {
        // no init needed, jai-ext is already enabled
        assertWarpROI();
    }

    @Test
    public void testAlphaInterpolation() {

        // All image pixels are initialized to RED.
        BufferedImage red = getSyntheticRGB(Color.RED, 512);
        BufferedImage alphaChannel = getSyntheticSolidGray((byte) 255, 512);

        final int w = red.getWidth();
        final int h = red.getHeight();
        final int specialPixelsOriginX = w / 2;
        final int specialPixelsOriginY = h / 2;

        // Setting the top left quarter of the image to all zeros
        int[] zero = {0, 0, 0};
        int[] redValues = {255, 0, 0};
        WritableRaster raster = red.getRaster();
        WritableRaster alphaRaster = alphaChannel.getRaster();
        for (int i = 0; i < specialPixelsOriginX; i++) {
            for (int j = 0; j < specialPixelsOriginY; j++) {
                raster.setPixel(i, j, zero);
                alphaRaster.setSample(i, j, 0, 0);
            }
        }

        // Setting the edge of the quarter with some red teeth for future interpolation
        // Alpha follows the teeth edges.
        //      **
        //       *
        //      **
        //       *
        // * * * *
        // *******
        int[] values = null;
        for (int i = specialPixelsOriginX; i < specialPixelsOriginX + 3; i += 3) {
            for (int j = 0; j < specialPixelsOriginY; j += 6) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 6; l++) {
                        values = l > 2 ? redValues : zero;
                        raster.setPixel(i + k, j + l, values);
                        alphaRaster.setSample(i + k, j + l, 0, values[0]);
                    }
                }
            }
        }
        for (int i = 0; i < specialPixelsOriginX; i += 6) {
            for (int j = specialPixelsOriginY; j < specialPixelsOriginY + 3; j += 3) {
                for (int l = 0; l < 3; l++) {
                    for (int k = 0; k < 6; k++) {
                        values = k > 2 ? redValues : zero;
                        raster.setPixel(i + k, j + l, values);
                        alphaRaster.setSample(i + k, j + l, 0, values[0]);
                    }
                }
            }
        }
        // Applying transparency to create RGBA image
        ImageWorker iw = new ImageWorker(red);
        iw.addBand(alphaChannel, false, true, null);
        RenderedImage rgbA = iw.getRenderedImage();

        ImageWorker scalingWorker =
                new ImageWorker(rgbA).scale(0.5, 0.5, 0, 0, Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        RenderedImage result = scalingWorker.getRenderedImage();

        // Check alpha has not been interpolated
        SampleModel sm = result.getSampleModel();
        assertEquals(4, sm.getNumBands());
        ColorModel cm = result.getColorModel();
        assertTrue(cm.hasAlpha());
        Raster alpha = result.getData();

        // Checking alpha component for pixels is either fully opaque (255) or
        // fully transparent (0) (No interpolation occurred on alpha)
        for (int i = 0; i < specialPixelsOriginX; i++) {
            for (int j = 0; j < specialPixelsOriginY; j++) {
                int sample = alpha.getSample(i, j, 3);
                assertTrue(sample == 0 || sample == 255);
            }
        }
    }

    public void assertWarpROI() throws IOException, TransformException {
        ImageWorker worker = new ImageWorker(gray);
        // ROI covers just 1/4th
        ROIShape roiShape = new ROIShape(new Rectangle(0, 0, gray.getWidth() / 2, gray.getHeight() / 2));
        ROI roi = new ROI(roiShape.getAsImage());
        worker.setROI(roi);

        Warp warp = new WarpBuilder(0.3)
                .buildWarp(
                        new AffineTransform2D(AffineTransform.getScaleInstance(2, 2)),
                        new Rectangle(gray.getWidth(), gray.getHeight()));
        worker.warp(warp, Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        RenderedImage ri = worker.getRenderedImage();
        final Object roiValue = ri.getProperty("ROI");
        assertNotEquals(java.awt.Image.UndefinedProperty, roiValue);
        ROI warpedROI = (ROI) roiValue;
        PlanarImage warpedROIImage = warpedROI.getAsImage();
        // check it's not solid white (happens if the wrong warp is used)
        ImageWorker warpedROIWorker = new ImageWorker(warpedROIImage);
        assertEquals(1, warpedROIWorker.getMaximums()[0], 0d);
        assertEquals(0, warpedROIWorker.getMinimums()[0], 0d);
    }

    @Test
    public void testRemoveColorModel() {
        // cheap way to get a binary image, a ROI is always represented as one
        Area area = new Area(new Rectangle(0, 0, 32, 16));
        area.add(new Area(new Rectangle(128, 0, 16, 32)));
        ROI roi = new ROIShape(area);
        PlanarImage image = roi.getAsImage();
        assertThat(image.getColorModel(), instanceOf(IndexColorModel.class));
        ImageWorker worker = new ImageWorker(image);
        double[] maximumBefore = worker.getMaximums();
        assertTrue(worker.isBinary());
        // remove
        worker.removeIndexColorModel();
        RenderedImage result = worker.getRenderedImage();
        assertThat(result, not(is(image)));
        assertThat(result.getColorModel(), instanceOf(ComponentColorModel.class));
        // subtract the original
        double[] maximums = worker.getMaximums();
        assertThat(maximums, equalTo(maximumBefore));
    }

    @Test
    public void testExtremaSubsample() {
        // increase the subsampling factor, it should lead to progressive deterioration of
        // the extracted stats values. Images and workers are re-created over and over, since
        // the worker attaches the stats to the image as a property (so a new one is needed to
        // force re-calculation
        assertMinMax(getSynthetic(1000), 1, 1, 1, 1000);
        assertMinMax(getSynthetic(1000), 4, 4, 1, 1000);
        assertMinMax(getSynthetic(1000), 8, 8, 4, 985);
        assertMinMax(getSynthetic(1000), 16, 16, 9, 985);
        assertMinMax(getSynthetic(1000), 32, 32, 11, 931);
    }

    @Test(expected = Test.None.class)
    public void testImageLayout() {
        int tileWitdh = 161;
        int tileHeight = 21; // a small tileHeight
        PixelInterleavedSampleModel sm = new PixelInterleavedSampleModel(
                DataBuffer.TYPE_BYTE, tileWitdh, tileHeight, 1, tileWitdh, new int[] {0});
        ComponentColorModel cm = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        RenderedImage image = new TiledImage(0, 0, tileWitdh, tileHeight, 0, 0, sm, cm);
        ImageWorker worker = new ImageWorker(image);
        RenderingHints hints = worker.getRenderingHints();
        ImageLayout layout = (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT);

        // No IllegalArgumentException will be thrown at this point when creating a new ImageLayout
        // on top of a reference one.
        ImageLayout newLayout = new ImageLayout(
                layout.getTileGridXOffset(null),
                layout.getTileGridYOffset(null),
                layout.getTileWidth(null),
                layout.getTileHeight(null),
                sm,
                cm);
        assertEquals(tileWitdh, newLayout.getTileWidth(null));

        final int imageUtilitiesTileHeight = newLayout.getTileHeight(null);
        assertNotEquals(tileHeight, imageUtilitiesTileHeight);
        // That's the default tilesize provided by ImageUtilities
        // when the original tile size is smaller than a reference value (64).
        assertEquals(512, imageUtilitiesTileHeight);
        assertEquals(0, newLayout.getTileGridXOffset(null));
        assertEquals(0, newLayout.getTileGridYOffset(null));
    }

    private void assertMinMax(RenderedImage image, int xPeriod, int yPeriod, double expectedMin, double expectedMax) {
        ImageWorker iw = new ImageWorker(image).setXPeriod(xPeriod).setYPeriod(yPeriod);
        double[] minimums = iw.getMinimums();
        assertEquals(expectedMin, minimums[0], 1e-6);
        double[] maximums = iw.getMaximums();
        assertEquals(expectedMax, maximums[0], 1e-6);
    }

    @Test
    public void testMeanSubsample() {
        // increase the subsampling factor, it should lead to progressive deterioration of
        // the extracted stats values. Images and workers are re-created over and over, since
        // the worker attaches the stats to the image as a property (so a new one is needed to
        // force re-calculation
        assertMean(getSynthetic(1000), 1, 1, 500);
        assertMean(getSynthetic(1000), 4, 4, 500);
        assertMean(getSynthetic(1000), 8, 8, 490);
        assertMean(getSynthetic(1000), 16, 16, 495);
        assertMean(getSynthetic(1000), 32, 32, 455);
    }

    private void assertMean(RenderedImage image, int xPeriod, int yPeriod, double expectedMean) {
        ImageWorker iw = new ImageWorker(image).setXPeriod(xPeriod).setYPeriod(yPeriod);
        double[] means = iw.getMean();
        assertEquals(expectedMean, means[0], 1);
    }

    @Test
    public void testHistogramSubsample() {
        // increase the subsampling factor, it should lead to progressive deterioration of
        // the extracted stats values. Images and workers are re-created over and over, since
        // the worker attaches the stats to the image as a property (so a new one is needed to
        // force re-calculation.
        // In this case we have actual counts, so they will have to go down accordingly too
        assertHistogram(getSynthetic(1000), 1, 1, 5408, 5487, 5475);
        assertHistogram(getSynthetic(1000), 2, 2, 1372, 1384, 1339);
        assertHistogram(getSynthetic(1000), 4, 4, 324, 363, 336);
        assertHistogram(getSynthetic(1000), 8, 8, 83, 85, 87);
        assertHistogram(getSynthetic(1000), 16, 16, 22, 18, 23);
        assertHistogram(getSynthetic(1000), 32, 32, 6, 5, 4);
    }

    private void assertHistogram(
            RenderedImage image,
            int xPeriod,
            int yPeriod,
            int expectedCountBin1,
            int expectedCountBin2,
            int expectedCountBin3) {
        ImageWorker iw = new ImageWorker(image).setXPeriod(xPeriod).setYPeriod(yPeriod);
        double[] minimums = iw.getMinimums();
        double[] maximums = iw.getMaximums();
        Histogram histogram = iw.getHistogram(new int[] {3}, minimums, maximums);
        assertThat(
                histogram.getBins(0),
                CoreMatchers.equalTo(new int[] {expectedCountBin1, expectedCountBin2, expectedCountBin3}));
    }

    @Test
    public void testHistogramRecalculationWithDifferentParameters() {
        ImageWorker iw = new ImageWorker(getSynthetic(1000)).setXPeriod(32).setYPeriod(32);
        double[] minimums = iw.getMinimums();
        double[] maximums = iw.getMaximums();
        Histogram histogram = iw.getHistogram(new int[] {1}, minimums, maximums);
        Histogram histogram2 =
                iw.getHistogram(new int[] {1}, new double[] {minimums[0] + 1.0}, new double[] {maximums[0] - 1.0});
        assertNotSame(histogram.getBins(0)[0], histogram2.getBins(0)[0]);
    }

    @Test
    public void testGetCachedHistogramWithSameParameters() {
        ImageWorker iw = new ImageWorker(getSynthetic(1000)).setXPeriod(32).setYPeriod(32);
        double[] minimums = iw.getMinimums();
        double[] maximums = iw.getMaximums();
        Histogram histogram = iw.getHistogram(new int[] {1}, minimums, maximums);
        Histogram histogram2 = iw.getHistogram(new int[] {1}, minimums, maximums);
        assertEquals(histogram.getBins(0)[0], histogram2.getBins(0)[0]);
    }

    @Test(expected = Test.None.class /* No IllegalArgumentException */)
    public void testReattachScaledAlphaChannel() {
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
        ImageWorker iw = new ImageWorker(bi);
        RenderingHints hints = new RenderingHints(null);
        hints.put(JAI.KEY_IMAGE_LAYOUT, new ImageLayout(0, 0, 1, 1));
        iw.prepareForScaledAlphaChannel(bi, hints, bi.getColorModel(), bi.getSampleModel());
    }

    @Test
    public void testMax() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED); // 255 0 0
        BufferedImage blue = getSyntheticRGB(Color.BLUE); // 0 0 255

        RenderedImage max =
                new ImageWorker().max(new RenderedImage[] {red, blue}).getRenderedImage();
        int[] pixel = new int[3];
        max.getData().getPixel(0, 0, pixel);
        assertEquals(255, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(255, pixel[2]);
    }

    @Test
    public void testMin() throws Exception {
        BufferedImage red = getSyntheticRGB(Color.RED); // 255 0 0
        BufferedImage yellow = getSyntheticRGB(Color.YELLOW); // 255 255 0

        RenderedImage min =
                new ImageWorker().min(new RenderedImage[] {red, yellow}).getRenderedImage();
        int[] pixel = new int[3];
        min.getData().getPixel(0, 0, pixel);
        assertEquals(255, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
    }
}
