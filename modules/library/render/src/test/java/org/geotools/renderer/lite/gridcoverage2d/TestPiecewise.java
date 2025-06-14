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

import it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageReader;
import it.geosolutions.imageio.plugins.arcgrid.spi.AsciiGridsImageReaderSpi;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.jaiext.piecewise.DefaultConstantPiecewiseTransformElement;
import it.geosolutions.jaiext.piecewise.DefaultLinearPiecewiseTransform1DElement;
import it.geosolutions.jaiext.piecewise.DefaultPassthroughPiecewiseTransform1DElement;
import it.geosolutions.jaiext.piecewise.DefaultPiecewiseTransform1D;
import it.geosolutions.jaiext.piecewise.DefaultPiecewiseTransform1DElement;
import it.geosolutions.jaiext.piecewise.GenericPiecewiseOpImage;
import it.geosolutions.jaiext.piecewise.MathTransformation;
import it.geosolutions.jaiext.piecewise.PiecewiseUtilities;
import it.geosolutions.jaiext.piecewise.Position;
import it.geosolutions.jaiext.piecewise.SingleDimensionTransformation;
import it.geosolutions.jaiext.piecewise.TransformationException;
import it.geosolutions.jaiext.range.RangeFactory;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.stream.FileImageInputStream;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import org.geotools.TestData;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.image.ImageWorker;
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
        Assert.assertEquals(((DefaultLinearPiecewiseTransform1DElement) e0).getOutputMinimum(), e0.transform(0), 0.0);
        Assert.assertEquals(
                ((DefaultLinearPiecewiseTransform1DElement) e0).getOutputMaximum(),
                e0.transform(e0.getInputMaximum()),
                0.0);
        Assert.assertEquals(0.0, ((DefaultLinearPiecewiseTransform1DElement) e0).getOffset(), 0.0);
        Assert.assertEquals(2.0, ((DefaultLinearPiecewiseTransform1DElement) e0).getScale(), 0.0);
        Assert.assertFalse(e0.isIdentity());
        Assert.assertEquals(1, e0.getSourceDimensions());
        Assert.assertEquals(1, e0.getTargetDimensions());

        try {
            Assert.assertEquals(e0.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);
            Assert.fail();
        } catch (Exception e) {
            // TODO: handle exception
        }

        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});

        // checks
        Assert.assertEquals(0.0, transform.transform(0), 0);
        Assert.assertEquals(1, transform.getSourceDimensions());
        Assert.assertEquals(1, transform.getTargetDimensions());
    }
    /** Testing {@link org.geotools.renderer.lite.gridcoverage2d.MathTransformationAdapter}. */
    @Test
    public void mathTransform1DAdapter() throws IOException, TransformException {
        // default adapter
        final MathTransformationAdapter defaultAdapter = new MathTransformationAdapter();
        Assert.assertEquals(defaultAdapter.getSourceDimensions(), 1);
        Assert.assertEquals(defaultAdapter.getTargetDimensions(), 1);

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
     * @throws it.geosolutions.jaiext.piecewise.NoninvertibleTransformException
     */
    @Test
    @SuppressWarnings("SelfEquals")
    public void constantTransform()
            throws IOException, TransformException, it.geosolutions.jaiext.piecewise.NoninvertibleTransformException,
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
        Assert.assertEquals(0.0, e0.transform(0), 0.0);
        Assert.assertEquals(e0.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);
        try {
            e0.inverse();
            Assert.fail();
        } catch (Exception e) {

        }

        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});
        // checks
        Assert.assertEquals(0.0, transform.transform(0), 0);
        Assert.assertEquals(transform.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);

        // /////////////////////////////////////////////////////////////////////
        //
        // int
        //
        // /////////////////////////////////////////////////////////////////////
        e0 = DefaultPiecewiseTransform1DElement.create(
                "zero", RangeFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), 0);
        Assert.assertTrue(e0 instanceof DefaultConstantPiecewiseTransformElement);
        // checks
        Assert.assertEquals(0.0, e0.transform(0), 0.0);
        Assert.assertEquals(e0.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);
        try {
            e0.inverse();
            Assert.fail();
        } catch (Exception e) {
            // TODO: handle exception
        }

        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform1 =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});

        // checks
        Assert.assertEquals(0.0, transform1.transform(0), 0);
        Assert.assertEquals(transform1.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);

        // hashcode and equals
        Assert.assertNotEquals(transform, transform1);
        Assert.assertNotEquals(transform1, transform);
        Assert.assertNotEquals(transform, transform);
        Assert.assertNotEquals(transform1, transform1);
        Assert.assertEquals(transform1.hashCode(), transform.hashCode());

        // /////////////////////////////////////////////////////////////////////
        //
        // double
        //
        // /////////////////////////////////////////////////////////////////////
        e0 = DefaultPiecewiseTransform1DElement.create(
                "zero", RangeFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), 0.0);
        Assert.assertTrue(e0 instanceof DefaultConstantPiecewiseTransformElement);
        // checks
        Assert.assertEquals(0.0, e0.transform(0), 0.0);
        Assert.assertEquals(e0.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);
        try {
            e0.inverse();
            Assert.fail();
        } catch (Exception e) {
            // TODO: handle exception
        }

        transform = new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});

        // checks
        Assert.assertEquals(0.0, transform.transform(0), 0);
        Assert.assertEquals(transform.transform(Double.POSITIVE_INFINITY), 0.0, 0.0);

        // /////////////////////////////////////////////////////////////////////
        //
        // invertible
        //
        // /////////////////////////////////////////////////////////////////////
        e0 = DefaultPiecewiseTransform1DElement.create("zero", RangeFactory.create(3, 3), 0.0);
        Assert.assertTrue(e0 instanceof DefaultConstantPiecewiseTransformElement);
        // checks
        Assert.assertEquals(0.0, e0.transform(3), 0.0);
        Assert.assertEquals(3, e0.inverse().transform(new Position(0), null).getOrdinatePosition(), 0);

        transform = new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {e0});

        // checks
        Assert.assertEquals(0.0, e0.transform(3), 0);
        Assert.assertEquals(transform.transform(3), 0.0, 0.0);
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
        Assert.assertEquals(0.0, transform.transform(0), 0);
        Assert.assertEquals(0.0, transform.transform(1), 0);
        Assert.assertEquals(Math.log(255.0), transform.transform(255), 0);
        Assert.assertEquals(Math.log(124.0), transform.transform(124), 0);

        boolean exceptionFound = false;
        try {
            Assert.assertEquals(Math.log(255.0), transform.transform(256), 0);
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
        Assert.assertEquals(0.0, transform.transform(0), 0);
        Assert.assertEquals(0.0, transform.transform(1), 0);
        Assert.assertEquals(Math.log(255.0), transform.transform(255), 0);
        Assert.assertEquals(Math.log(124.0), transform.transform(124), 0);

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
        Assert.assertEquals(t0.transform(0.5), 200.5, 0.0);
        Assert.assertTrue(t0.contains(0.5));
        Assert.assertTrue(t0.contains(RangeFactory.create(0.1, 0.9)));
        Assert.assertFalse(t0.contains(1.5));
        Assert.assertFalse(t0.contains(RangeFactory.create(0.1, 1.9)));
        Assert.assertEquals(t0, t0);
        Assert.assertEquals(t0.transform(new Position(0.5), null).getOrdinatePosition(), 200.5, 0.0);
        Assert.assertEquals(t0.inverse().transform(new Position(200.5), null).getOrdinatePosition(), 0.5, 0.0);
        // Assert.assertEquals(t0.derivative(1.0), 1.0, 0.0);

        t0 = DefaultPiecewiseTransform1DElement.create(
                "t0", RangeFactory.create(0.0, true, 1.0, true), RangeFactory.create(200, 201));
        Assert.assertNotEquals(
                t0,
                DefaultPiecewiseTransform1DElement.create(
                        "t0", RangeFactory.create(0.0, true, 1.0, true), RangeFactory.create(200, 202)));
        Assert.assertEquals(t0.transform(0.5), 200.5, 0.0);
        Assert.assertEquals(t0.transform(new Position(0.5), null).getOrdinatePosition(), 200.5, 0.0);
        Assert.assertEquals(t0.inverse().transform(new Position(200.5), null).getOrdinatePosition(), 0.5, 0.0);
        // Assert.assertEquals(t0.derivative(1.0), 1.0, 0.0);

        ////
        //
        // Create second element and test it
        //
        /////
        DefaultPiecewiseTransform1DElement t1 =
                DefaultPiecewiseTransform1DElement.create("t1", RangeFactory.create(1.0, false, 2.0, true), 201);
        Assert.assertEquals(t1.transform(1.5), 201, 0.0);
        Assert.assertEquals(t1.transform(1.6), 201, 0.0);
        Assert.assertNotEquals(t0, t1);
        Assert.assertEquals(t1.transform(new Position(1.8), null).getOrdinatePosition(), 201, 0.0);

        // Assert.assertEquals(t1.derivative(2.0), 0.0, 0.0);

        t1 = new DefaultConstantPiecewiseTransformElement("t1", RangeFactory.create(1.0, false, 2.0, true), 201);
        Assert.assertEquals(t1.transform(1.5), 201, 0.0);
        Assert.assertEquals(t1.transform(1.6), 201, 0.0);
        Assert.assertEquals(t1.transform(new Position(1.8), null).getOrdinatePosition(), 201, 0.0);

        // Assert.assertEquals(t1.derivative(2.0), 0.0, 0.0);

        DefaultPiecewiseTransform1D<DefaultPiecewiseTransform1DElement> transform =
                new DefaultPiecewiseTransform1D<>(new DefaultPiecewiseTransform1DElement[] {t1}, 12);
        Assert.assertEquals(transform.getName().toString(), t1.getName().toString());
        Assert.assertEquals(transform.getApproximateDomainRange().getMin().doubleValue(), 1.0, 0.0);
        Assert.assertEquals(transform.getApproximateDomainRange().getMax().doubleValue(), 2.0, 0.0);
        Assert.assertEquals(transform.transform(1.5), 201, 0.0);
        Assert.assertEquals(transform.transform(new Position(1.5), null).getOrdinatePosition(), 201, 0.0);
        Assert.assertEquals(transform.transform(2.5), 0.0, 12.0);

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
        Assert.assertEquals(p0.getTargetDimensions(), 1);
        Assert.assertEquals(p0.getSourceDimensions(), 1);
        Assert.assertTrue(p0.isIdentity());
        Assert.assertEquals(p0.inverse(), SingleDimensionTransformation.IDENTITY);
        Assert.assertEquals(p0.transform(0.5), 0.5, 0.0);
        Assert.assertEquals(p0.transform(new Position(0.5), null).getOrdinatePosition(), 0.5, 0.0);
        Assert.assertEquals(p0.inverse().transform(new Position(0.5), null).getOrdinatePosition(), 0.5, 0.0);
        // Assert.assertEquals(p0.derivative(1.0), 1.0, 0.0);
        final Position inDP = new Position(0.6);
        final Position outDP = p0.transform(inDP, null);
        Assert.assertEquals(0.6, outDP.getOrdinatePosition(), 0.0);

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

        Assert.assertEquals(piecewise.getApproximateDomainRange().getMin().doubleValue(), 0.0, 0.0);
        Assert.assertEquals(piecewise.getApproximateDomainRange().getMax().doubleValue(), 1.0, 0.0);
        Assert.assertEquals(piecewise.transform(0.5), 0.5, 0.0);
        Assert.assertEquals(piecewise.transform(new Position(0.5), null).getOrdinatePosition(), 0.5, 0.0);
        Assert.assertEquals(piecewise.transform(1.5), 0.0, 11.0);

        ////
        //
        // testing the passthrough through indirect instantion
        //
        ////
        final DefaultPassthroughPiecewiseTransform1DElement p1 =
                new DefaultPassthroughPiecewiseTransform1DElement("p1");
        Assert.assertEquals(p1.getTargetDimensions(), 1);
        Assert.assertEquals(p1.getSourceDimensions(), 1);
        Assert.assertTrue(p1.isIdentity());
        Assert.assertEquals(p1.inverse(), SingleDimensionTransformation.IDENTITY);
        Assert.assertEquals(p1.transform(0.5), 0.5, 0.0);
        Assert.assertEquals(p1.transform(111.5), 111.5, 0.0);
        Assert.assertEquals(p1.transform(new Position(123.5), null).getOrdinatePosition(), 123.5, 0.0);
        Assert.assertEquals(p1.inverse().transform(new Position(657.5), null).getOrdinatePosition(), 657.5, 0.0);
        // Assert.assertEquals(p1.derivative(1.0), 1.0, 0.0);
        final Position inDP1 = new Position(0.6);
        final Position outDP1 = p1.transform(inDP1, null);
        Assert.assertEquals(0.6, outDP1.getOrdinatePosition(), 0.0);

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
        final RenderedImage image = new ImageWorker(JAI.create("ImageRead", TestData.file(this, "usa.png")))
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
        // final ParameterBlockJAI pbj = new ParameterBlockJAI(
        // GenericPiecewiseOpImage.OPERATION_NAME);
        // pbj.addSource(image);
        // pbj.setParameter("Domain1D", list);
        // final RenderedOp finalimage = JAI.create(
        // GenericPiecewiseOpImage.OPERATION_NAME, pbj);
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

        // final ParameterBlockJAI pbj = new ParameterBlockJAI(
        // GenericPiecewiseOpImage.OPERATION_NAME);
        // pbj.addSource(image);
        // pbj.setParameter("Domain1D", transform);
        boolean exceptionThrown = false;
        try {
            // //
            // forcing a bad band selection ...
            // //
            // pbj.setParameter("bandIndex", Integer.valueOf(2));
            // final RenderedOp d = JAI.create(
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
        // final RenderedOp finalImage = JAI.create(
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
            new ParameterBlockJAI(GenericPiecewiseOpImage.OPERATION_NAME);

        } catch (Exception e) {
            // GenericPiecewiseOpImage.register(JAI.getDefaultInstance());
        }

        // check that it exisits
        File file = TestData.copy(this, "arcgrid/arcgrid.zip");
        Assert.assertTrue(file.exists());

        // unzip it
        TestData.unzipFile(this, "arcgrid/arcgrid.zip");
    }
}
