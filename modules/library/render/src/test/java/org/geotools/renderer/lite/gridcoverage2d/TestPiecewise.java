/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;

import it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageReader;
import it.geosolutions.imageio.plugins.arcgrid.spi.AsciiGridsImageReaderSpi;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.stream.FileImageInputStream;
import org.eclipse.imagen.ImageN;
import org.eclipse.imagen.ParameterBlockImageN;
import org.eclipse.imagen.ROI;
import org.eclipse.imagen.RasterFactory;
import org.eclipse.imagen.RenderedOp;
import org.eclipse.imagen.media.piecewise.DefaultConstantPiecewiseTransformElement;
import org.eclipse.imagen.media.piecewise.DefaultLinearPiecewiseTransform1DElement;
import org.eclipse.imagen.media.piecewise.DefaultPassthroughPiecewiseTransform1DElement;
import org.eclipse.imagen.media.piecewise.DefaultPiecewiseTransform1D;
import org.eclipse.imagen.media.piecewise.DefaultPiecewiseTransform1DElement;
import org.eclipse.imagen.media.piecewise.GenericPiecewiseOpImage;
import org.eclipse.imagen.media.piecewise.MathTransformation;
import org.eclipse.imagen.media.piecewise.PiecewiseUtilities;
import org.eclipse.imagen.media.piecewise.Position;
import org.eclipse.imagen.media.piecewise.SingleDimensionTransformation;
import org.eclipse.imagen.media.piecewise.TransformationException;
import org.eclipse.imagen.media.range.RangeFactory;
import org.geotools.TestData;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ComponentColorModelJAI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author Simone Giannecchini, GeoSolutions. */
public class TestPiecewise {

    /** Testing {@link DefaultConstantPiecewiseTransformElement}. */
    @Test
    public void linearTransform() throws IOException, TransformException, TransformationException {

        // /////////////////////////////////////////////////////////////////////
        //
        // byte
        //
        // /////////////////////////////////////////////////////////////////////
        DefaultPiecewiseTransform1DElement e0 = DefaultPiecewiseTransform1DElement.create(
                "zero", RangeFactory.create(0, 100), RangeFactory.create(0, 200));
        Assert.assertTrue(e0 instanceof DefaultLinearPiecewiseTransform1DElement);
        // checks
        assertEquals(((DefaultLinearPiecewiseTransform1DElement) e0).getOutputMinimum(), e0.transform(0), 0.0);
        assertEquals(
                ((DefaultLinearPiecewiseTransform1DElement) e0).getOutputMaximum(),
                e0.transform(e0.getInputMaximum()),
                0.0);
        assertEquals(0.0, ((DefaultLinearPiecewiseTransform1DElement) e0).getOffset(), 0.0);
        assertEquals(2.0, ((DefaultLinearPiecewiseTransform1DElement) e0).getScale(), 0.0);
        Assert.assertFalse(e0.isIdentity());
        assertEquals(1, e0.getSourceDimensions());
        assertEquals(1, e0.getTargetDimensions());

        try {
            assertEquals(e0.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);
            Assert.fail();
        } catch (Exception e) {
            // TODO: handle exception
        }

        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});

        // checks
        assertEquals(0.0, transform.transform(0), 0);
        assertEquals(1, transform.getSourceDimensions());
        assertEquals(1, transform.getTargetDimensions());
    }
    /** Testing {@link org.geotools.renderer.lite.gridcoverage2d.MathTransformationAdapter}. */
    @Test
    public void mathTransform1DAdapter() throws IOException, TransformException {
        // default adapter
        final MathTransformationAdapter defaultAdapter = new MathTransformationAdapter();
        assertEquals(defaultAdapter.getSourceDimensions(), 1);
        assertEquals(defaultAdapter.getTargetDimensions(), 1);

        try {
            defaultAdapter.inverseTransform();
            Assert.fail();
        } catch (UnsupportedOperationException e) {

        }

        try {
            defaultAdapter.transform(0.0);
            Assert.fail();
        } catch (UnsupportedOperationException e) {

        }
    }
    /**
     * Testing {@link DefaultConstantPiecewiseTransformElement}.
     *
     * @throws org.eclipse.imagen.media.piecewise.NoninvertibleTransformException
     */
    @Test
    @SuppressWarnings("SelfEquals")
    public void constantTransform()
            throws IOException, TransformException, org.eclipse.imagen.media.piecewise.NoninvertibleTransformException,
                    TransformationException {

        // /////////////////////////////////////////////////////////////////////
        //
        // byte
        //
        // /////////////////////////////////////////////////////////////////////
        DefaultPiecewiseTransform1DElement e0 = DefaultPiecewiseTransform1DElement.create(
                "zero", RangeFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), (byte) 0);
        Assert.assertTrue(e0 instanceof DefaultConstantPiecewiseTransformElement);
        // checks
        assertEquals(0.0, e0.transform(0), 0.0);
        assertEquals(e0.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);
        try {
            e0.inverse();
            Assert.fail();
        } catch (Exception e) {

        }

        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});
        // checks
        assertEquals(0.0, transform.transform(0), 0);
        assertEquals(transform.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);

        // /////////////////////////////////////////////////////////////////////
        //
        // int
        //
        // /////////////////////////////////////////////////////////////////////
        e0 = DefaultPiecewiseTransform1DElement.create(
                "zero", RangeFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), 0);
        Assert.assertTrue(e0 instanceof DefaultConstantPiecewiseTransformElement);
        // checks
        assertEquals(0.0, e0.transform(0), 0.0);
        assertEquals(e0.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);
        try {
            e0.inverse();
            Assert.fail();
        } catch (Exception e) {
            // TODO: handle exception
        }

        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform1 =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});

        // checks
        assertEquals(0.0, transform1.transform(0), 0);
        assertEquals(transform1.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);

        // hashcode and equals
        Assert.assertNotEquals(transform, transform1);
        Assert.assertNotEquals(transform1, transform);
        Assert.assertNotEquals(transform, transform);
        Assert.assertNotEquals(transform1, transform1);
        assertEquals(transform1.hashCode(), transform.hashCode());

        // /////////////////////////////////////////////////////////////////////
        //
        // double
        //
        // /////////////////////////////////////////////////////////////////////
        e0 = DefaultPiecewiseTransform1DElement.create(
                "zero", RangeFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), 0.0);
        Assert.assertTrue(e0 instanceof DefaultConstantPiecewiseTransformElement);
        // checks
        assertEquals(0.0, e0.transform(0), 0.0);
        assertEquals(e0.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);
        try {
            e0.inverse();
            Assert.fail();
        } catch (Exception e) {
            // TODO: handle exception
        }

        transform = new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});

        // checks
        assertEquals(0.0, transform.transform(0), 0);
        assertEquals(transform.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);

        // /////////////////////////////////////////////////////////////////////
        //
        // invertible
        //
        // /////////////////////////////////////////////////////////////////////
        e0 = DefaultPiecewiseTransform1DElement.create("zero", RangeFactory.create(3, 3), 0.0);
        Assert.assertTrue(e0 instanceof DefaultConstantPiecewiseTransformElement);
        // checks
        assertEquals(0.0, e0.transform(3), 0.0);
        assertEquals(3, e0.inverse().transform(new Position(0), null).getOrdinatePosition(), 0);

        transform = new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});

        // checks
        assertEquals(0.0, e0.transform(3), 0);
        assertEquals(transform.transform(3), 0.0, 0.0);
    }
    /** Testing testPiecewiseLogarithm. */
    @Test
    public void piecewiseLogarithm() throws IOException, TransformException, TransformationException {

        // /////////////////////////////////////////////////////////////////////
        //
        // prepare the transform without no data management, which means gaps
        // won't be filled and exception will be thrown when trying to
        //
        // /////////////////////////////////////////////////////////////////////
        final DefaultPiecewiseTransform1DElement zero =
                DefaultPiecewiseTransform1DElement.create("zero", RangeFactory.create(0, 0), 0);
        final DefaultPiecewiseTransform1DElement mainElement = new DefaultPiecewiseTransform1DElement(
                "natural logarithm", RangeFactory.create(0, false, 255, true), new MathTransformation() {

                    @Override
                    public double derivative(double arg0) throws TransformException {

                        return 1 / arg0;
                    }

                    @Override
                    public double transform(double arg0) {
                        return Math.log(arg0);
                    }

                    @Override
                    public int getSourceDimensions() {

                        return 01;
                    }

                    @Override
                    public int getTargetDimensions() {

                        return 1;
                    }

                    @Override
                    public boolean isIdentity() {
                        return false;
                    }

                    @Override
                    public MathTransformation inverseTransform() {
                        return null;
                    }

                    @Override
                    public Position transform(Position ptSrc, Position ptDst) throws TransformationException {
                        if (ptDst == null) {
                            ptDst = new Position();
                        }
                        ptDst.setOrdinatePosition(transform(ptSrc.getOrdinatePosition()));
                        return ptDst;
                    }
                });
        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {zero, mainElement});

        // checks
        assertEquals(0.0, transform.transform(0), 0);
        assertEquals(0.0, transform.transform(1), 0);
        assertEquals(Math.log(255.0), transform.transform(255), 0);
        assertEquals(Math.log(124.0), transform.transform(124), 0);

        boolean exceptionFound = false;
        try {
            assertEquals(Math.log(255.0), transform.transform(256), 0);
        } catch (TransformationException e) {
            exceptionFound = true;
        }
        Assert.assertTrue(exceptionFound);

        // /////////////////////////////////////////////////////////////////////
        //
        // prepare the transform without no data management, which means gaps
        // won't be filled and exception will be thrown when trying to
        //
        // /////////////////////////////////////////////////////////////////////
        final DefaultPiecewiseTransform1DElement nodata =
                DefaultPiecewiseTransform1DElement.create("no-data", RangeFactory.create(-1, -1), Double.NaN);
        transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {zero, mainElement, nodata});

        // checks
        assertEquals(0.0, transform.transform(0), 0);
        assertEquals(0.0, transform.transform(1), 0);
        assertEquals(Math.log(255.0), transform.transform(255), 0);
        assertEquals(Math.log(124.0), transform.transform(124), 0);

        exceptionFound = false;

        try {
            Assert.assertTrue(Double.isNaN(transform.transform(256)));
        } catch (TransformationException e1) {
            exceptionFound = true;
        }
        Assert.assertTrue(exceptionFound);

        // /////////////////////////////////////////////////////////////////////
        //
        // prepare the transform with categories that overlap, we should try an exception
        //
        // /////////////////////////////////////////////////////////////////////
        final DefaultPiecewiseTransform1DElement overlap =
                DefaultPiecewiseTransform1DElement.create("overlap", RangeFactory.create(-100, 12), Double.NaN);
        exceptionFound = false;
        try {
            transform = new DefaultPiecewiseTransform1D<>(
                    new DefaultPiecewiseTransform1DElement[] {zero, mainElement, overlap, nodata});
        } catch (Throwable e) {
            exceptionFound = true;
        }
        Assert.assertTrue(exceptionFound);
    }

    /** Testing DefaultPiecewiseTransform1DElement. */
    @Test
    public void defaultTransform() throws IOException, TransformException, TransformationException {
        ////
        //
        // Create first element and test it
        //
        /////
        DefaultPiecewiseTransform1DElement t0 = new DefaultPiecewiseTransform1DElement(
                "t0",
                RangeFactory.create(0.0, true, 1.0, true),
                PiecewiseUtilities.createLinearTransform1D(
                        RangeFactory.create(0.0, true, 1.0, true), RangeFactory.create(200, 201)));
        assertEquals(t0.transform(0.5), 200.5, 0.0);
        Assert.assertTrue(t0.contains(0.5));
        Assert.assertTrue(t0.contains(RangeFactory.create(0.1, 0.9)));
        Assert.assertFalse(t0.contains(1.5));
        Assert.assertFalse(t0.contains(RangeFactory.create(0.1, 1.9)));
        assertEquals(t0, t0);
        assertEquals(t0.transform(new Position(0.5), null).getOrdinatePosition(), 200.5, 0.0);
        assertEquals(t0.inverse().transform(new Position(200.5), null).getOrdinatePosition(), 0.5, 0.0);
        // Assert.assertEquals(t0.derivative(1.0), 1.0, 0.0);

        t0 = DefaultPiecewiseTransform1DElement.create(
                "t0", RangeFactory.create(0.0, true, 1.0, true), RangeFactory.create(200, 201));
        Assert.assertNotEquals(
                t0,
                DefaultPiecewiseTransform1DElement.create(
                        "t0", RangeFactory.create(0.0, true, 1.0, true), RangeFactory.create(200, 202)));
        assertEquals(t0.transform(0.5), 200.5, 0.0);
        assertEquals(t0.transform(new Position(0.5), null).getOrdinatePosition(), 200.5, 0.0);
        assertEquals(t0.inverse().transform(new Position(200.5), null).getOrdinatePosition(), 0.5, 0.0);
        // Assert.assertEquals(t0.derivative(1.0), 1.0, 0.0);

        ////
        //
        // Create second element and test it
        //
        /////
        DefaultPiecewiseTransform1DElement t1 =
                DefaultPiecewiseTransform1DElement.create("t1", RangeFactory.create(1.0, false, 2.0, true), 201);
        assertEquals(t1.transform(1.5), 201, 0.0);
        assertEquals(t1.transform(1.6), 201, 0.0);
        Assert.assertNotEquals(t0, t1);
        assertEquals(t1.transform(new Position(1.8), null).getOrdinatePosition(), 201, 0.0);

        // Assert.assertEquals(t1.derivative(2.0), 0.0, 0.0);

        t1 = new DefaultConstantPiecewiseTransformElement("t1", RangeFactory.create(1.0, false, 2.0, true), 201);
        assertEquals(t1.transform(1.5), 201, 0.0);
        assertEquals(t1.transform(1.6), 201, 0.0);
        assertEquals(t1.transform(new Position(1.8), null).getOrdinatePosition(), 201, 0.0);

        // Assert.assertEquals(t1.derivative(2.0), 0.0, 0.0);

        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {t1}, 12);
        assertEquals(transform.getName().toString(), t1.getName().toString());
        assertEquals(transform.getApproximateDomainRange().getMin().doubleValue(), 1.0, 0.0);
        assertEquals(transform.getApproximateDomainRange().getMax().doubleValue(), 2.0, 0.0);
        assertEquals(transform.transform(1.5), 201, 0.0);
        assertEquals(transform.transform(new Position(1.5), null).getOrdinatePosition(), 201, 0.0);
        assertEquals(transform.transform(2.5), 0.0, 12.0);

        ////
        //
        // test bad cases
        //
        /////
        try {
            transform = new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {
                DefaultLinearPiecewiseTransform1DElement.create(
                        "",
                        RangeFactory.create(0, 100),
                        RangeFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY))
            });
            Assert.fail();
        } catch (IllegalArgumentException e) {

        }
    }

    /** Testing DefaultPassthroughPiecewiseTransform1DElement . */
    @Test
    public void passthroughTransform() throws IOException, TransformException, TransformationException {
        ////
        //
        // testing the passthrough through direct instantion
        //
        ////
        final DefaultPassthroughPiecewiseTransform1DElement p0 =
                new DefaultPassthroughPiecewiseTransform1DElement("p0", RangeFactory.create(0.0, true, 1.0, true));
        assertEquals(p0.getTargetDimensions(), 1);
        assertEquals(p0.getSourceDimensions(), 1);
        Assert.assertTrue(p0.isIdentity());
        assertEquals(p0.inverse(), SingleDimensionTransformation.IDENTITY);
        assertEquals(p0.transform(0.5), 0.5, 0.0);
        assertEquals(p0.transform(new Position(0.5), null).getOrdinatePosition(), 0.5, 0.0);
        assertEquals(p0.inverse().transform(new Position(0.5), null).getOrdinatePosition(), 0.5, 0.0);
        // Assert.assertEquals(p0.derivative(1.0), 1.0, 0.0);
        final Position inDP = new Position(0.6);
        final Position outDP = p0.transform(inDP, null);
        assertEquals(0.6, outDP.getOrdinatePosition(), 0.0);

        //			Matrix m= p0.derivative(inDP);
        //			Assert.assertTrue(m.getNumCol()==1);
        //			Assert.assertTrue(m.getNumRow()==1);
        //			Assert.assertTrue(m.getElement(0, 0)==1);

        ////
        //
        // testing the transform
        //
        ////
        final DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> piecewise =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {p0}, 11);

        assertEquals(piecewise.getApproximateDomainRange().getMin().doubleValue(), 0.0, 0.0);
        assertEquals(piecewise.getApproximateDomainRange().getMax().doubleValue(), 1.0, 0.0);
        assertEquals(piecewise.transform(0.5), 0.5, 0.0);
        assertEquals(piecewise.transform(new Position(0.5), null).getOrdinatePosition(), 0.5, 0.0);
        assertEquals(piecewise.transform(1.5), 0.0, 11.0);

        ////
        //
        // testing the passthrough through indirect instantion
        //
        ////
        final DefaultPassthroughPiecewiseTransform1DElement p1 =
                new DefaultPassthroughPiecewiseTransform1DElement("p1");
        assertEquals(p1.getTargetDimensions(), 1);
        assertEquals(p1.getSourceDimensions(), 1);
        Assert.assertTrue(p1.isIdentity());
        assertEquals(p1.inverse(), SingleDimensionTransformation.IDENTITY);
        assertEquals(p1.transform(0.5), 0.5, 0.0);
        assertEquals(p1.transform(111.5), 111.5, 0.0);
        assertEquals(p1.transform(new Position(123.5), null).getOrdinatePosition(), 123.5, 0.0);
        assertEquals(p1.inverse().transform(new Position(657.5), null).getOrdinatePosition(), 657.5, 0.0);
        // Assert.assertEquals(p1.derivative(1.0), 1.0, 0.0);
        final Position inDP1 = new Position(0.6);
        final Position outDP1 = p1.transform(inDP1, null);
        assertEquals(0.6, outDP1.getOrdinatePosition(), 0.0);

        //			Matrix m1= p1.derivative(inDP1);
        //			Assert.assertTrue(m1.getNumCol()==1);
        //			Assert.assertTrue(m1.getNumRow()==1);
        //			Assert.assertTrue(m1.getElement(0, 0)==1);

    }

    /** Testing Short input values. */
    @Test
    public void lookupByte() throws IOException, TransformException {

        // /////////////////////////////////////////////////////////////////////
        //
        //
        //
        // /////////////////////////////////////////////////////////////////////
        final RenderedImage image = new ImageWorker(ImageN.create("ImageRead", TestData.file(this, "usa.png")))
                .forceComponentColorModel()
                .retainFirstBand()
                .getRenderedImage();
        if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(image, "testLookupByte");

        // /////////////////////////////////////////////////////////////////////
        //
        // Build the categories
        //
        // /////////////////////////////////////////////////////////////////////
        final DefaultPiecewiseTransform1DElement c1 = DefaultLinearPiecewiseTransform1DElement.create(
                "c1", RangeFactory.create(1, 128), RangeFactory.create(1, 255));
        final DefaultPiecewiseTransform1DElement c0 = DefaultLinearPiecewiseTransform1DElement.create(
                "c0", RangeFactory.create(129, 255), RangeFactory.create(255, 255));
        final DefaultPiecewiseTransform1DElement nodata =
                DefaultLinearPiecewiseTransform1DElement.create("nodata", RangeFactory.create(0, 0), 0);
        final DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> list =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {c0, c1, nodata});
        ImageWorker w = new ImageWorker(image);
        final RenderedOp finalimage = w.piecewise(list, null).getRenderedOperation();

        if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(finalimage, "testLookupByte");
        else finalimage.getTiles();
    }

    /** SWAN test-case. */
    @Test
    public void SWANLOGARITHMIC() throws IOException {
        // /////////////////////////////////////////////////////////////////////
        //
        //
        // /////////////////////////////////////////////////////////////////////
        final RenderedImage image = getSWAN();
        if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(image, "testSWANLOGARITHMIC");
        ImageWorker w = new ImageWorker(image)
                .setROI(new ROI(new ImageWorker(image).binarize(0).getRenderedImage()));
        // final RenderedOp statistics = ExtremaDescriptor.create(image, new ROI(new
        // ImageWorker(image).binarize(0).getRenderedImage()),
        // Integer.valueOf(1), Integer.valueOf(1), Boolean.FALSE, Integer.valueOf(1),
        // null);
        // final double[] minimum=(double[]) statistics.getProperty("minimum");
        // final double[] maximum=(double[]) statistics.getProperty("maximum");
        final double[] minimum = w.getMinimums();
        final double[] maximum = w.getMaximums();

        final DefaultPiecewiseTransform1DElement mainElement = new DefaultPiecewiseTransform1DElement(
                "natural logarithm", RangeFactory.create(minimum[0], maximum[0]), new MathTransformationAdapter() {

                    @Override
                    public double derivative(double arg0) throws TransformException {

                        return 1 / arg0;
                    }

                    @Override
                    public double transform(double arg0) {

                        return minimum[0]
                                + 1.2
                                        * Math.log(arg0 / minimum[0])
                                        * ((maximum[0] - minimum[0]) / Math.log(maximum[0] / minimum[0]));
                    }

                    @Override
                    public boolean isIdentity() {
                        return false;
                    }
                });
        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {mainElement}, 0);

        // final ParameterBlockImageN pbj = new ParameterBlockImageN(
        // GenericPiecewiseOpImage.OPERATION_NAME);
        // pbj.addSource(image);
        // pbj.setParameter("Domain1D", transform);
        boolean exceptionThrown = false;
        try {
            // //
            // forcing a bad band selection ...
            // //
            // pbj.setParameter("bandIndex", Integer.valueOf(2));
            // final RenderedOp d = ImageN.create(
            // GenericPiecewiseOpImage.OPERATION_NAME, pbj);
            final RenderedOp d = w.piecewise(transform, Integer.valueOf(2)).getRenderedOperation();
            d.getTiles();
            // we should not be here!
        } catch (Exception e) {
            // //
            // ... ok, Exception wanted!
            // //
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);

        // pbj.setParameter("bandIndex", Integer.valueOf(0));
        // final RenderedOp finalImage = ImageN.create(
        // GenericPiecewiseOpImage.OPERATION_NAME, pbj);
        final RenderedOp finalImage = w.piecewise(transform, Integer.valueOf(0)).getRenderedOperation();
        if (TestData.isInteractiveTest()) ImageIOUtilities.visualize(finalImage, "testSWANLOGARITHMIC");
        else finalImage.getTiles();
        finalImage.dispose();
    }

    /**
     * Building an image based on SWAN data.
     *
     * @return {@linkplain BufferedImage}
     */
    private RenderedImage getSWAN() throws IOException, FileNotFoundException {
        final AsciiGridsImageReader reader =
                (AsciiGridsImageReader) new AsciiGridsImageReaderSpi().createReaderInstance();
        reader.setInput(new FileImageInputStream(TestData.file(this, "arcgrid/SWAN_NURC_LigurianSeaL07_HSIGN.asc")));
        final RenderedImage image = reader.readAsRenderedImage(0, null);
        return image;
    }

    @Before
    public void setUp() throws Exception {
        try {
            new ParameterBlockImageN(GenericPiecewiseOpImage.OPERATION_NAME);

        } catch (Exception e) {
            // GenericPiecewiseOpImage.register(ImageN.getDefaultInstance());
        }

        // check that it exisits
        File file = TestData.copy(this, "arcgrid/arcgrid.zip");
        Assert.assertTrue(file.exists());

        // unzip it
        TestData.unzipFile(this, "arcgrid/arcgrid.zip");
    }

    @Test
    public void testPiecewiseNoData() throws IOException, TransformException, TransformationException {
        // create an image with short pixels, nodata value -10000 filling the upper triangle, and a range of other
        // values
        final int width = 100;
        final int height = 100;
        final short noDataValue = -10000;
        final WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_SHORT, width, height, 1, null);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > y) raster.setSample(x, y, 0, noDataValue);
                else raster.setSample(x, y, 0, (x + y));
            }
        }
        ColorModel cm = new ComponentColorModelJAI(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE, DataBuffer.TYPE_SHORT);
        final RenderedImage image = new ImageWorker(new BufferedImage(cm, raster, false, null))
                .setNoData(RangeFactory.create(noDataValue, noDataValue))
                .getRenderedImage();

        // create categories for a clamp
        final DefaultPiecewiseTransform1DElement cBelow =
                DefaultLinearPiecewiseTransform1DElement.create("cBelow", RangeFactory.create(0, 20), 20);
        final DefaultPiecewiseTransform1DElement cLinear = DefaultLinearPiecewiseTransform1DElement.create(
                "cLinear", RangeFactory.create(21, 49), RangeFactory.create(21, 49));
        final DefaultPiecewiseTransform1DElement cAbove =
                DefaultLinearPiecewiseTransform1DElement.create("cAbove", RangeFactory.create(50, Short.MAX_VALUE), 50);
        final DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(
                        new DefaultPiecewiseTransform1DElement[] {cBelow, cLinear, cAbove}, 0);
        ImageWorker w = new ImageWorker(image);
        w.piecewise(transform, 0);

        // piecewise clamped
        assertEquals(20, w.getMinimums()[0], 0);
        assertEquals(50, w.getMaximums()[0], 0);

        // nodata is set
        assertEquals(RangeFactory.create(0d, 0d), w.getNoData());

        // the pixes that were at -10000 have been mapped to 0
        Raster outputRaster = w.getRenderedImage().getData();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > y) assertEquals(0d, outputRaster.getSample(x, y, 0), 0d);
            }
        }
    }
}
