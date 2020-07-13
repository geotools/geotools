/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageReader;
import it.geosolutions.imageio.plugins.arcgrid.spi.AsciiGridsImageReaderSpi;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.jaiext.classifier.LinearColorMap;
import it.geosolutions.jaiext.classifier.LinearColorMapElement;
import it.geosolutions.jaiext.piecewise.TransformationException;
import it.geosolutions.jaiext.range.RangeFactory;
import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.stream.FileImageInputStream;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import org.geotools.TestData;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ComponentColorModelJAI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.operation.TransformException;

/** @author Simone Giannecchini, GeoSolutions */
public class TestLinearClassifier extends Assert {

    @Before
    public void before() throws Exception {

        // check that it exisits
        File file = TestData.copy(this, "arcgrid/arcgrid.zip");
        assertTrue(file.exists());

        // unzip it
        TestData.unzipFile(this, "arcgrid/arcgrid.zip");
    }

    private static final int TEST_NUM = 1;

    /** Synthetic with Double Sample Model! */
    @Test
    public void Synthetic_Double() throws IOException {

        // /////////////////////////////////////////////////////////////////////
        //
        // This test is interesting since it can be used to force the
        // creation of a sample model that uses a USHORT datatype since the
        // number of requested colors is pretty high. We are also using some
        // synthetic data where there is no NoData.
        //
        // /////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////
        //
        // Set the pixel values. Because we use only one tile with one band,
        // the
        // code below is pretty similar to the code we would have if we were
        // just setting the values in a matrix.
        //
        // /////////////////////////////////////////////////////////////////////
        final BufferedImage image = getSynthetic_Double();
        for (int i = 0; i < TEST_NUM; i++) {
            // /////////////////////////////////////////////////////////////////////
            //
            // Build the categories
            //
            // /////////////////////////////////////////////////////////////////////
            final LinearColorMapElement c0 =
                    LinearColorMapElement.create(
                            "c0",
                            Color.BLACK,
                            RangeFactory.create(Double.NEGATIVE_INFINITY, false, 10, true),
                            0);

            final LinearColorMapElement c1 =
                    LinearColorMapElement.create(
                            "c2", Color.blue, RangeFactory.create(10.0, false, 100.0, true), 1);

            final LinearColorMapElement c3 =
                    LinearColorMapElement.create(
                            "c3", Color.green, RangeFactory.create(100.0, false, 300.0, true), 2);

            final LinearColorMapElement c4 =
                    LinearColorMapElement.create(
                            "c4",
                            new Color[] {Color.green, Color.red},
                            RangeFactory.create(300.0, false, 400, true),
                            RangeFactory.create(3, 1000));

            final LinearColorMapElement c5 =
                    LinearColorMapElement.create(
                            "c5",
                            new Color[] {Color.red, Color.white},
                            RangeFactory.create(400.0, false, 1000, true),
                            RangeFactory.create(1001, 2000));

            final LinearColorMapElement c6 =
                    LinearColorMapElement.create("c6", Color.red, 1001.0, 2001);

            final LinearColorMapElement c7 =
                    LinearColorMapElement.create(
                            "nodata",
                            new Color(0, 0, 0, 0),
                            RangeFactory.create(Double.NaN, Double.NaN),
                            2201);

            final LinearColorMap list =
                    new LinearColorMap(
                            "",
                            new LinearColorMapElement[] {c0, c1, c3, c4, c5, c6},
                            new LinearColorMapElement[] {c7});

            ImageWorker w = new ImageWorker(image);
            // final ParameterBlockJAI pbj = new ParameterBlockJAI(
            // RasterClassifierOpImage.OPERATION_NAME);
            // pbj.addSource(image);
            // pbj.setParameter("Domain1D", list);
            final RenderedOp finalimage = w.classify(list, null).getRenderedOperation();
            // JAI.create(
            // RasterClassifierOpImage.OPERATION_NAME, pbj);

            if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(finalimage, "synthetic");
            else finalimage.getTiles();
            finalimage.dispose();
        }
    }

    /**
     * Synthetic with Float Sample Model!
     *
     * @return {@linkplain BufferedImage}
     */
    private BufferedImage getSynthetic_Double() {
        final int width = 500;
        final int height = 500;
        final WritableRaster raster =
                RasterFactory.createBandedRaster(DataBuffer.TYPE_DOUBLE, width, height, 1, null);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, (x + y));
            }
        }
        final ColorModel cm =
                new ComponentColorModelJAI(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY),
                        false,
                        false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_DOUBLE);
        final BufferedImage image = new BufferedImage(cm, raster, false, null);
        return image;
    }

    /**
     * Building a synthetic image upon a DOUBLE sample-model.
     *
     * @return {@linkplain BufferedImage}
     */
    @Test
    public void Synthetic_Float() throws IOException {

        // /////////////////////////////////////////////////////////////////////
        //
        // This test is interesting since it can be used to force the
        // creation of a sample model that uses a USHORT datatype since the
        // number of requested colors is pretty high. We are also using some
        // synthetic data where there is no NoData.
        //
        // /////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////
        //
        // Set the pixel values. Because we use only one tile with one band,
        // the
        // code below is pretty similar to the code we would have if we were
        // just setting the values in a matrix.
        //
        // /////////////////////////////////////////////////////////////////////
        final BufferedImage image = getSynthetic_Float();
        for (int i = 0; i < TEST_NUM; i++) {
            // /////////////////////////////////////////////////////////////////////
            //
            // Build the categories
            //
            // /////////////////////////////////////////////////////////////////////
            final LinearColorMapElement c0 =
                    LinearColorMapElement.create(
                            "c0",
                            Color.BLACK,
                            RangeFactory.create(Double.NEGATIVE_INFINITY, false, 10, true),
                            0);

            final LinearColorMapElement c1 =
                    LinearColorMapElement.create(
                            "c2", Color.blue, RangeFactory.create(10.0f, false, 100.0f, true), 1);

            final LinearColorMapElement c3 =
                    LinearColorMapElement.create(
                            "c3", Color.green, RangeFactory.create(100.0f, false, 300.0f, true), 2);

            final LinearColorMapElement c4 =
                    LinearColorMapElement.create(
                            "c4",
                            new Color[] {Color.green, Color.red},
                            RangeFactory.create(300.0f, false, 400.0f, true),
                            RangeFactory.create(3, 1000));

            final LinearColorMapElement c5 =
                    LinearColorMapElement.create(
                            "c5",
                            new Color[] {Color.red, Color.white},
                            RangeFactory.create(400.0f, false, 1000.0f, true),
                            RangeFactory.create(1001, 2000));

            final LinearColorMapElement c6 =
                    LinearColorMapElement.create("c6", Color.red, 1001.0f, 2001);

            final LinearColorMapElement c7 =
                    LinearColorMapElement.create(
                            "nodata",
                            new Color(0, 0, 0, 0),
                            RangeFactory.create(Double.NaN, Double.NaN),
                            2201);

            final LinearColorMap list =
                    new LinearColorMap(
                            "",
                            new LinearColorMapElement[] {c0, c1, c3, c4, c5, c6},
                            new LinearColorMapElement[] {c7});

            ImageWorker w = new ImageWorker(image);
            final RenderedOp finalimage = w.classify(list, null).getRenderedOperation();

            if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(finalimage, "synthetic");
            else finalimage.getTiles();
            finalimage.dispose();
        }
    }

    /**
     * Building a synthetic image upon a FLOAT sample-model.
     *
     * @return {@linkplain BufferedImage}
     */
    private BufferedImage getSynthetic_Float() {
        final int width = 500;
        final int height = 500;
        final WritableRaster raster =
                RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, (x + y));
            }
        }
        final ColorModel cm =
                new ComponentColorModelJAI(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY),
                        false,
                        false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_FLOAT);
        final BufferedImage image = new BufferedImage(cm, raster, false, null);
        return image;
    }

    /** Spearfish test-case. */
    @Test
    public void spearfish() throws IOException {

        // /////////////////////////////////////////////////////////////////////
        //
        // This test is quite standard since the NoData category specified
        // is for NoData values since the input file is a GRASS ascii file
        // where the missing values are represented by * which are substituted
        // with NaN during reads. The only strange thing that we try here is
        // that we map two different classes to the same color with the same
        // index.
        //
        // /////////////////////////////////////////////////////////////////////
        final RenderedImage image = getSpearfhisDemo();

        for (int i = 0; i < TEST_NUM; i++) {
            final LinearColorMapElement c0 =
                    LinearColorMapElement.create(
                            "c0",
                            Color.yellow,
                            RangeFactory.create(Double.NEGATIVE_INFINITY, false, 1100, true),
                            5);

            final LinearColorMapElement c1 =
                    LinearColorMapElement.create(
                            "c2", Color.blue, RangeFactory.create(1100.0, false, 1200.0, true), 1);

            final LinearColorMapElement c3 =
                    LinearColorMapElement.create(
                            "c3", Color.green, RangeFactory.create(1200.0, false, 1400.0, true), 7);

            final LinearColorMapElement c4 =
                    LinearColorMapElement.create(
                            "c4", Color.blue, RangeFactory.create(1400.0, false, 1600, true), 1);

            final LinearColorMapElement c5 =
                    LinearColorMapElement.create(
                            "c4",
                            Color.CYAN,
                            RangeFactory.create(1600.0, false, Double.POSITIVE_INFINITY, true),
                            11);

            final LinearColorMapElement c6 =
                    LinearColorMapElement.create(
                            "nodata",
                            new Color(0, 0, 0, 0),
                            RangeFactory.create(Double.NaN, Double.NaN),
                            0);

            final LinearColorMap list =
                    new LinearColorMap(
                            "",
                            new LinearColorMapElement[] {c0, c1, c3, c4, c5},
                            new LinearColorMapElement[] {c6});

            ImageWorker w = new ImageWorker(image);
            final RenderedOp finalimage = w.classify(list, null).getRenderedOperation();

            if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(finalimage, "spearfish");
            else finalimage.getTiles();
            finalimage.dispose();
        }
    }

    /**
     * Building an image based on Spearfish data.
     *
     * @return {@linkplain BufferedImage}
     */
    private RenderedImage getSpearfhisDemo() throws IOException, FileNotFoundException {
        final AsciiGridsImageReader reader =
                (AsciiGridsImageReader) new AsciiGridsImageReaderSpi().createReaderInstance();
        reader.setInput(new FileImageInputStream(TestData.file(this, "arcgrid/spearfish_dem.arx")));
        final RenderedImage image = reader.readAsRenderedImage(0, null);
        return image;
    }

    /** SWAN test-case. */
    @Test
    public void SWAN() throws IOException {
        // /////////////////////////////////////////////////////////////////////
        //
        // This test is interesting since it can be used to simulate the
        // case where someone specifies a ColorMap that overlaps with the native
        // NoData value. For this SWAN data the NoData value is -9999.0 and no
        // NaN which falls right into the first category.
        //
        // We overcome this problem by simply giving higher priority to the
        // NoData category over the other categories when doing a search for
        // the right category given a certain value. This force us to
        // first evaluate the no data category and then evaluate a possible
        // provided overlapping value.
        //
        // This test is also interesting since we create a color map by
        // providing output indexes that are not ordered and also that are not
        // all contained in a closed natural interval. As you can notice by
        // inspecting the different classes below there is an index, 51, which
        // is way outside the range of the others.
        //
        // /////////////////////////////////////////////////////////////////////
        final RenderedImage image = getSWAN();

        for (int i = 0; i < TEST_NUM; i++) {
            final LinearColorMapElement c0 =
                    LinearColorMapElement.create(
                            "c0",
                            Color.green,
                            RangeFactory.create(Double.NEGATIVE_INFINITY, 0.3),
                            51);

            final LinearColorMapElement c1 =
                    LinearColorMapElement.create(
                            "c2", Color.yellow, RangeFactory.create(0.3, false, 0.6, true), 1);

            final LinearColorMapElement c1b =
                    LinearColorMapElement.create(
                            "c2", Color.BLACK, RangeFactory.create(0.3, false, 0.6, true), 1);
            final LinearColorMapElement c1c =
                    LinearColorMapElement.create(
                            "c2", Color.yellow, RangeFactory.create(0.3, false, 0.6, true), 1);
            assertFalse(c1.equals(c1b));
            assertTrue(c1.equals(c1c));

            final LinearColorMapElement c3 =
                    LinearColorMapElement.create(
                            "c3", Color.red, RangeFactory.create(0.60, false, 0.90, true), 2);

            final LinearColorMapElement c4 =
                    LinearColorMapElement.create(
                            "c4",
                            Color.BLUE,
                            RangeFactory.create(0.9, false, Double.POSITIVE_INFINITY, true),
                            3);

            final LinearColorMapElement nodata =
                    LinearColorMapElement.create(
                            "nodata", new Color(0, 0, 0, 0), RangeFactory.create(-9.0, -9.0), 4);

            final LinearColorMap list =
                    new LinearColorMap(
                            "testSWAN",
                            new LinearColorMapElement[] {c0, c1, c3, c4},
                            new LinearColorMapElement[] {nodata},
                            new Color(0, 0, 0));

            assertEquals(list.getSourceDimensions(), 1);
            assertEquals(list.getTargetDimensions(), 1);
            assertEquals(list.getName().toString(), "testSWAN");
            assertNotNull(c0.toString());

            ImageWorker w = new ImageWorker(image);
            boolean exceptionThrown = false;
            try {
                // //
                // forcing a bad band selection ...
                // //
                final RenderedOp d = w.classify(list, Integer.valueOf(2)).getRenderedOperation();
                d.getTiles();
                // we should not be here!

            } catch (Exception e) {
                // //
                // ... ok, Exception wanted!
                // //
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);

            // pbj.setParameter("bandIndex", Integer.valueOf(0));
            final RenderedOp finalImage =
                    w.classify(list, Integer.valueOf(0)).getRenderedOperation();
            // JAI.create(
            // RasterClassifierOpImage.OPERATION_NAME, pbj);
            if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(finalImage, "testSWAN1");
            else finalImage.getTiles();
            finalImage.dispose();
        }
    }

    /** SWAN test-case. */
    @Test
    public void SWANGAP() throws IOException {

        // /////////////////////////////////////////////////////////////////////
        //
        // This test is interesting since it can be used to simulate the
        // case where someone specifies a ColorMap that overlaps with the native
        // NoData value. For this SWAN data the NoData value is -9999.0 and no
        // NaN which falls right into the first category.
        //
        // We overcome this problem by simply giving higher priority to the
        // NoData category over the other categories when doing a search for
        // the right category given a certain value. This force us to
        // first evaluate the no data category and then evaluate a possible
        // provided overlapping value.
        //
        // This test is also interesting since we create a color map by
        // providing output indexes that are not ordered and also that are not
        // all contained in a closed natural interval. As you can notice by
        // inspecting the different classes below there is an index, 51, which
        // is way outside the range of the others.
        //
        // /////////////////////////////////////////////////////////////////////
        final RenderedImage image = getSWAN();

        for (int i = 0; i < TEST_NUM; i++) {
            final LinearColorMapElement c0 =
                    LinearColorMapElement.create(
                            "c0",
                            Color.green,
                            RangeFactory.create(Double.NEGATIVE_INFINITY, 0.3),
                            51);

            final LinearColorMapElement c1 =
                    LinearColorMapElement.create(
                            "c2", Color.yellow, RangeFactory.create(0.3, false, 0.6, true), 1);

            final LinearColorMapElement c3 =
                    LinearColorMapElement.create(
                            "c3", Color.red, RangeFactory.create(0.70, false, 0.90, true), 2);

            final LinearColorMapElement c4 =
                    LinearColorMapElement.create(
                            "c4",
                            Color.BLUE,
                            RangeFactory.create(0.9, false, Double.POSITIVE_INFINITY, true),
                            3);

            final LinearColorMapElement nodata =
                    LinearColorMapElement.create(
                            "nodata", Color.red, RangeFactory.create(-9.0, -9.0), 4);

            final LinearColorMap list =
                    new LinearColorMap(
                            "testSWAN",
                            new LinearColorMapElement[] {c0, c1, c3, c4},
                            new LinearColorMapElement[] {nodata},
                            new Color(0, 0, 0, 0));

            ImageWorker w = new ImageWorker(image);
            boolean exceptionThrown = false;
            try {
                // //
                // forcing a bad band selection ...
                // //
                final RenderedOp d = w.classify(list, Integer.valueOf(2)).getRenderedOperation();
                d.getTiles();
                // we should not be here!

            } catch (Exception e) {
                // //
                // ... ok, Exception wanted!
                // //
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown);

            final RenderedOp finalImage =
                    w.classify(list, Integer.valueOf(0)).getRenderedOperation();
            final IndexColorModel icm = (IndexColorModel) finalImage.getColorModel();
            assertEquals(icm.getRed(4), 255);
            assertEquals(icm.getRed(2), 255);

            if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(finalImage, "testSWANGAP");
            else finalImage.getTiles();
            finalImage.dispose();
        }
    }
    /**
     * Building an image based on SWAN data.
     *
     * @return {@linkplain BufferedImage}
     */
    private RenderedImage getSWAN() throws IOException, FileNotFoundException {
        final AsciiGridsImageReader reader =
                (AsciiGridsImageReader) new AsciiGridsImageReaderSpi().createReaderInstance();
        reader.setInput(
                new FileImageInputStream(
                        TestData.file(this, "arcgrid/SWAN_NURC_LigurianSeaL07_HSIGN.asc")));
        final RenderedImage image = reader.readAsRenderedImage(0, null);
        return image;
    }

    /** NoData only test-case. */
    @Test
    public void noDataOnly() throws IOException, TransformException, TransformationException {

        // /////////////////////////////////////////////////////////////////////
        //
        // We are covering here a case that can often be verified, i.e. the case
        // when only NoData values are known and thus explicitly mapped by the
        // user to a defined nodata DomainElement, but not the same for the
        // others.
        // In such case we want CatrgoryLists automatically map unknown data to
        // a Passthrough DomainElement, which identically maps raster data to
        // category
        // data.
        //
        // /////////////////////////////////////////////////////////////////////

        for (int i = 0; i < TEST_NUM; i++) {

            final LinearColorMapElement n0 =
                    LinearColorMapElement.create(
                            "nodata",
                            new Color(0, 0, 0, 0),
                            RangeFactory.create(Double.NaN, Double.NaN),
                            9999);

            final LinearColorMap list = new LinearColorMap("", new LinearColorMapElement[] {n0});

            double testNum = Math.random();
            try {
                assertEquals(list.transform(testNum), testNum, 0.0);
                assertTrue(false);
            } catch (Exception e) {
                // TODO: handle exception
            }
            assertEquals(list.transform(Double.NaN), 9999, 0.0);
        }
    }
}
