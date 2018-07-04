/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ConstantDescriptor;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.Viewer;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.jai.Registry;
import org.geotools.referencing.crs.DefaultDerivedCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Tests the crop operation.
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @author Martin Desruisseaux (Geomatys)
 * @author Emanuele Tajariol (GeoSolutions)
 * @since 2.3
 */
public final class CropTest extends GridProcessingTestBase {
    /** The grid coverage to test. */
    private GridCoverage2D coverage;

    /** Set up common objects used for all tests. */
    @Before
    public void setUp() {
        coverage = EXAMPLES.get(0);
    }

    /**
     * Tests the "Crop" operation.
     *
     * @throws TransformException if a transformation was required and failed.
     */
    @Test
    public void testCrop() throws TransformException {
        final GridCoverage2D source = coverage;

        testCrop(source);
    }

    private GridCoverage2D testCrop(final GridCoverage2D source) {
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        /*
         * Get the source coverage and build the cropped envelope.
         */

        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 3 / 8,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 3 / 8
                        },
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 5 / 8,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 5 / 8
                        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(cropEnvelope);
        GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
            Viewer.show(coverage);
            Viewer.show(cropped);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(cropped.getRenderedImage()).getTiles());
        }
        RenderedImage raster = cropped.getRenderedImage();
        assertEquals(168, raster.getMinX());
        assertEquals(172, raster.getMinY());
        assertEquals(114, raster.getWidth());
        assertEquals(116, raster.getHeight());
        assertEquals(
                source.getGridGeometry().getGridToCRS2D(),
                cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));

        // check we did not use mosaic for this simple case
        RenderedOp op = (RenderedOp) raster;
        assertEquals("Crop", op.getOperationName());

        // check there is no ROI set (none in input, none in output)
        assertEquals(java.awt.Image.UndefinedProperty, raster.getProperty("ROI"));

        return cropped;
    }

    @Test
    public void testCropNoData() {
        Map<String, Object> properties = new HashMap<>();
        final Double theNoData = new Double(-123);
        CoverageUtilities.setNoDataProperty(properties, theNoData);
        GridCoverage2D source =
                new GridCoverageFactory()
                        .create(
                                coverage.getName().toString(),
                                coverage.getRenderedImage(),
                                coverage.getEnvelope(),
                                coverage.getSampleDimensions(),
                                null,
                                properties);

        // check the grid coverage
        GridCoverage2D cropped = testCrop(source);
        NoDataContainer noData = CoverageUtilities.getNoDataProperty(cropped);
        assertEquals(theNoData, noData.getAsSingleValue(), 0d);

        // but also check the image
        RenderedImage renderedImage = cropped.getRenderedImage();
        Object property = renderedImage.getProperty(NoDataContainer.GC_NODATA);
        assertNotEquals(property, Image.UndefinedProperty);
        assertThat(property, instanceOf(NoDataContainer.class));
        NoDataContainer nd = (NoDataContainer) property;
        assertEquals(-123, nd.getAsSingleValue(), 0d);
    }

    /**
     * Tests the "Crop" operation by providing different implementations of the opengis's Envelope
     * interface instead of GeneralEnvelope in order to fix GEOT-3434.
     *
     * @throws TransformException if a transformation was required and failed.
     */
    @Test
    public void testCropEnvelopeSupport() throws TransformException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage;
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 3 / 8,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 3 / 8
                        },
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 5 / 8,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 5 / 8
                        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());
        Envelope2D env2D = new Envelope2D(cropEnvelope);

        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(env2D);
        GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
            Viewer.show(coverage);
            Viewer.show(cropped);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(cropped.getRenderedImage()).getTiles());
        }
        RenderedImage raster = cropped.getRenderedImage();
        assertEquals(168, raster.getMinX());
        assertEquals(172, raster.getMinY());
        assertEquals(114, raster.getWidth());
        assertEquals(116, raster.getHeight());
        assertEquals(
                source.getGridGeometry().getGridToCRS2D(),
                cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));

        ReferencedEnvelope refEnv = new ReferencedEnvelope(cropEnvelope);
        param.parameter("Envelope").setValue(refEnv);
        cropped = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
            Viewer.show(coverage);
            Viewer.show(cropped);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(cropped.getRenderedImage()).getTiles());
        }
        raster = cropped.getRenderedImage();
        assertEquals(168, raster.getMinX());
        assertEquals(172, raster.getMinY());
        assertEquals(114, raster.getWidth());
        assertEquals(116, raster.getHeight());
        assertEquals(
                source.getGridGeometry().getGridToCRS2D(),
                cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));
    }

    /** Tests the specific catchable exception thrown when the area has no overlap */
    @Test
    public void testCropOutsideCoverageRealWorld() throws TransformException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        ReferencedEnvelope re = ReferencedEnvelope.reference(coverage.getEnvelope2D());
        // push it fully outside of the coverage area
        re.translate(re.getWidth() + 10, 0);

        /*
         * Do the crop
         */
        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(re);
        try {
            processor.doOperation(param);
            fail("Should have thrown an exception here, there is no overlap");
        } catch (EmptyIntersectionException e) {
            assertEquals("Crop envelope does not intersect in model space", e.getMessage());
        }
    }

    /** Tests the specific catchable exception thrown when the area has no overlap */
    @Test
    public void testCropOutsideCoverageRasterSpace() throws TransformException {
        // Reproducing a real world setup that exhibits a numerical issue. Unsure if the
        // same issue can be reproduced on all platforms, we might have to guard this one
        // for OS/architecture
        final int width = 450;
        final int height = 225;
        final BufferedImage image =
                new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
        final WritableRaster raster = (WritableRaster) image.getData();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, (int) (1 + (x + y) * 65534.0 / 1000.0));
            }
        }
        image.setData(raster);
        final GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(0, 0, 450, 225),
                        new AffineTransform2D(0.8, 0, 0, -0.8, -179.6, 89.6),
                        DefaultGeographicCRS.WGS84);
        GridCoverage2D coverage = factory.create("UInt16 coverage", image, gg, null, null, null);

        final CoverageProcessor processor = CoverageProcessor.getInstance();
        ReferencedEnvelope re =
                new ReferencedEnvelope(-180, 0, -270, -90, DefaultGeographicCRS.WGS84);

        /*
         * Do the crop
         */
        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(re);
        try {
            processor.doOperation(param);
            fail("Should have thrown an exception here, there is no overlap");
        } catch (EmptyIntersectionException e) {
            assertEquals(
                    "Crop envelope intersects in model space, but not in raster space",
                    e.getMessage());
        }
    }

    /**
     * Tests the "Crop" operation when there exists a rotation in the world to grid transformation
     *
     * @throws TransformException if a transformation was required and failed.
     */
    @Test
    public void testCropRotated() throws TransformException {
        /*
         * Get the test coverage.
         */
        final GridCoverage2D source = coverage;
        /*
         * Get the grid-to-world and apply a transformation in order to get a
         * rotated coverage in the end.
         */
        final AffineTransform gridToCRS = AffineTransform.getRotateInstance(Math.PI / 4.0);
        gridToCRS.concatenate(getAffineTransform(source));
        final MathTransform tr = ProjectiveTransform.create(gridToCRS);
        CoordinateReferenceSystem crs = source.getCoordinateReferenceSystem();
        crs = new DefaultDerivedCRS("Rotated CRS", crs, tr, crs.getCoordinateSystem());
        final GridCoverage2D rotated = project(source, crs, null, null);
        /*
         * Preparing the crop. We want to get a rectangle that is locate at the
         * center of this coverage envelope that is large 1/4 f the original
         * width and tall 1/4 of the original height.
         */
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        final Envelope oldEnvelope = rotated.getEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 3 / 8,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 3 / 8
                        },
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 5 / 8,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 5 / 8
                        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());
        /*
         * Do the crop without trying to conserve the envelope.
         */
        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(rotated);
        param.parameter("Envelope").setValue(cropEnvelope);
        GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
            Viewer.show(coverage);
            Viewer.show(cropped);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(cropped.getRenderedImage()).getTiles());
            assertNotNull(PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles());
        }
        RenderedImage raster = cropped.getRenderedImage();
        assertEquals(111, raster.getMinX());
        assertEquals(116, raster.getMinY());
        assertEquals(228, raster.getWidth());
        assertEquals(228, raster.getHeight());
        assertEquals(
                rotated.getGridGeometry().getGridToCRS2D(),
                cropped.getGridGeometry().getGridToCRS2D());
        /*
         * Get the roi and test it against the crop area
         */
        Object property = CoverageUtilities.getROIProperty(cropped);
        assertNotNull(property);
        assertTrue(property instanceof ROI);
        ROI roi = (ROI) property;
        assertEquals(
                new Rectangle(
                        raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight()),
                roi.getBounds());
    }

    /** Tests the "Crop" operation with a ROI set. */
    @Test
    public void testCropWithROI()
            throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage;
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelope.getMinimum(0) /*+ oldEnvelope.getSpan(0) * 3 / 8*/,
                            oldEnvelope.getMinimum(1) /*+ oldEnvelope.getSpan(1) * 3 / 8*/
                        },
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 5 / 8,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 5 / 8
                        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());

        GeometryFactory geometryFactory =
                JTSFactoryFinder.getGeometryFactory(JTSFactoryFinder.EMPTY_HINTS);

        double mid0 = oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 2;
        double mid1 = oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 4 / 10;
        double qspan0 = oldEnvelope.getSpan(0) * 2 / 16;
        double qspan1 = oldEnvelope.getSpan(1) * 2 / 16;

        double half0 = oldEnvelope.getSpan(0) * 1 / 10;
        double half1 = oldEnvelope.getSpan(1) * 1 / 10;

        // overlapping square #1 - rotated by 45deg
        CoordinateSequence cs1 = new CoordinateArraySequence(5);
        cs1.setOrdinate(0, 0, mid0);
        cs1.setOrdinate(0, 1, mid1 + qspan1);
        cs1.setOrdinate(1, 0, mid0 + qspan0);
        cs1.setOrdinate(1, 1, mid1);
        cs1.setOrdinate(2, 0, mid0);
        cs1.setOrdinate(2, 1, mid1 - qspan1);
        cs1.setOrdinate(3, 0, mid0 - qspan0);
        cs1.setOrdinate(3, 1, mid1);
        cs1.setOrdinate(4, 0, mid0);
        cs1.setOrdinate(4, 1, mid1 + qspan1);

        LinearRing shape1 = geometryFactory.createLinearRing(cs1);
        org.locationtech.jts.geom.Polygon poly1 = geometryFactory.createPolygon(shape1, null);

        // overlapping square #2
        CoordinateSequence cs2 = new CoordinateArraySequence(5);
        cs2.setOrdinate(0, 0, mid0 + half0);
        cs2.setOrdinate(0, 1, mid1 + half1);
        cs2.setOrdinate(1, 0, mid0 + half0);
        cs2.setOrdinate(1, 1, mid1 - half1);
        cs2.setOrdinate(2, 0, mid0 - half0);
        cs2.setOrdinate(2, 1, mid1 - half1);
        cs2.setOrdinate(3, 0, mid0 - half0);
        cs2.setOrdinate(3, 1, mid1 + half1);
        cs2.setOrdinate(4, 0, mid0 + half0);
        cs2.setOrdinate(4, 1, mid1 + half1);

        LinearRing shape2 = geometryFactory.createLinearRing(cs2);
        org.locationtech.jts.geom.Polygon poly2 = geometryFactory.createPolygon(shape2, null);

        // Disjuncted square lower left -- will be included
        CoordinateSequence cs3 = new CoordinateArraySequence(5);
        cs3.setOrdinate(0, 0, oldEnvelope.getMinimum(0));
        cs3.setOrdinate(0, 1, oldEnvelope.getMinimum(1));
        cs3.setOrdinate(1, 0, oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 8);
        cs3.setOrdinate(1, 1, oldEnvelope.getMinimum(1));
        cs3.setOrdinate(2, 0, oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 8);
        cs3.setOrdinate(2, 1, oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) / 8);
        cs3.setOrdinate(3, 0, oldEnvelope.getMinimum(0));
        cs3.setOrdinate(3, 1, oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) / 8);
        cs3.setOrdinate(4, 0, oldEnvelope.getMinimum(0));
        cs3.setOrdinate(4, 1, oldEnvelope.getMinimum(1));

        LinearRing shape3 = geometryFactory.createLinearRing(cs3);
        org.locationtech.jts.geom.Polygon poly3 = geometryFactory.createPolygon(shape3, null);

        // Disjuncted square lower right -- will be excluded by Envelope
        CoordinateSequence cs4 = new CoordinateArraySequence(5);
        cs4.setOrdinate(0, 0, oldEnvelope.getMaximum(0));
        cs4.setOrdinate(0, 1, oldEnvelope.getMinimum(1));
        cs4.setOrdinate(1, 0, oldEnvelope.getMaximum(0) - oldEnvelope.getSpan(0) / 8);
        cs4.setOrdinate(1, 1, oldEnvelope.getMinimum(1));
        cs4.setOrdinate(2, 0, oldEnvelope.getMaximum(0) - oldEnvelope.getSpan(0) / 8);
        cs4.setOrdinate(2, 1, oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) / 8);
        cs4.setOrdinate(3, 0, oldEnvelope.getMaximum(0));
        cs4.setOrdinate(3, 1, oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) / 8);
        cs4.setOrdinate(4, 0, oldEnvelope.getMaximum(0));
        cs4.setOrdinate(4, 1, oldEnvelope.getMinimum(1));

        LinearRing shape4 = geometryFactory.createLinearRing(cs4);
        org.locationtech.jts.geom.Polygon poly4 = geometryFactory.createPolygon(shape4, null);

        Geometry mpoly =
                geometryFactory.createMultiPolygon(
                        new org.locationtech.jts.geom.Polygon[] {poly1, poly2});

        //        Geometry union = geometryFactory.createGeometryCollection(new Geometry[]{
        //            poly1, poly2, poly3, poly4});
        Geometry union =
                geometryFactory.createGeometryCollection(new Geometry[] {mpoly, poly3, poly4});

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(cropEnvelope);
        param.parameter("ROI").setValue(union);

        GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
            Viewer.show(coverage, "Original");
            Viewer.show(cropped, "Cropped");
            Thread.sleep(10000);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(cropped.getRenderedImage()).getTiles());
        }
        RenderedImage raster = cropped.getRenderedImage();

        assertEquals(0, raster.getMinX());
        assertEquals(219, raster.getMinY());
        assertEquals(281, raster.getWidth());
        assertEquals(241, raster.getHeight());
        assertEquals(
                source.getGridGeometry().getGridToCRS2D(),
                cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));
    }

    /** Tests the "Crop" operation with a ROI set. */
    @Test
    public void testCropWithROIForceMosaic()
            throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage;
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelope.getMinimum(0) /*+ oldEnvelope.getSpan(0) * 3 / 8*/,
                            oldEnvelope.getMinimum(1) /*+ oldEnvelope.getSpan(1) * 3 / 8*/
                        },
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0), // * 5 / 8,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) // * 5 / 8
                        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());

        GeometryFactory geometryFactory =
                JTSFactoryFinder.getGeometryFactory(JTSFactoryFinder.EMPTY_HINTS);

        // Use this crop ROI
        //   (E) *---------* (A)
        //      /          |
        // (D) *           |
        //     |           |
        //     |           |
        //     |           |
        // (C) *-----------* (B)
        double min0 = cropEnvelope.getMinimum(0);
        double min1 = cropEnvelope.getMinimum(1);

        double max0 = cropEnvelope.getMaximum(0);
        double max1 = cropEnvelope.getMaximum(1);

        double mid0_E = min0 + cropEnvelope.getSpan(0) / 16;
        double mid1_D = max1 - cropEnvelope.getSpan(1) / 16;

        CoordinateSequence cs1 = new CoordinateArraySequence(6);
        // A
        cs1.setOrdinate(0, 0, max0);
        cs1.setOrdinate(0, 1, max1);

        // B
        cs1.setOrdinate(1, 0, max0);
        cs1.setOrdinate(1, 1, min1);

        // C
        cs1.setOrdinate(2, 0, min0);
        cs1.setOrdinate(2, 1, min1);

        // D
        cs1.setOrdinate(3, 0, min0);
        cs1.setOrdinate(3, 1, mid1_D);

        // E
        cs1.setOrdinate(4, 0, mid0_E);
        cs1.setOrdinate(4, 1, max1);

        // Close
        cs1.setOrdinate(5, 0, max0);
        cs1.setOrdinate(5, 1, max1);

        LinearRing shape1 = geometryFactory.createLinearRing(cs1);
        org.locationtech.jts.geom.Polygon mask = geometryFactory.createPolygon(shape1, null);

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(cropEnvelope);
        param.parameter("ROI").setValue(mask);

        GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
        cropped = (GridCoverage2D) processor.doOperation(param);
        RenderedImage raster = cropped.getRenderedImage();

        // The value should be zero since we have cut away the corner
        assertEquals(0, raster.getTile(0, 0).getSample(0, 0, 0));
        assertTrue(cropEnvelope.equals(cropped.getEnvelope()));
    }

    /** Tests the intersection of the ROI and the cropEnvelope in the "Crop" operation. */
    @Test
    public void testCropWithROIIntersection()
            throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage;
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelope.getMinimum(0) /*+ oldEnvelope.getSpan(0) * 3 / 8*/,
                            oldEnvelope.getMinimum(1) /*+ oldEnvelope.getSpan(1) * 3 / 8*/
                        },
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 4,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1)
                        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());

        GeometryFactory geometryFactory =
                JTSFactoryFinder.getGeometryFactory(JTSFactoryFinder.EMPTY_HINTS);

        // overlapping square #1 - rotated by 45deg
        CoordinateSequence cs1 = new CoordinateArraySequence(5);
        cs1.setOrdinate(0, 0, oldEnvelope.getMedian(0));
        cs1.setOrdinate(0, 1, oldEnvelope.getMaximum(1));
        cs1.setOrdinate(1, 0, oldEnvelope.getMaximum(0));
        cs1.setOrdinate(1, 1, oldEnvelope.getMedian(1));
        cs1.setOrdinate(2, 0, oldEnvelope.getMedian(0));
        cs1.setOrdinate(2, 1, oldEnvelope.getMinimum(1));
        cs1.setOrdinate(3, 0, oldEnvelope.getMinimum(0));
        cs1.setOrdinate(3, 1, oldEnvelope.getMedian(1));
        cs1.setOrdinate(4, 0, oldEnvelope.getMedian(0));
        cs1.setOrdinate(4, 1, oldEnvelope.getMaximum(1));

        LinearRing shape1 = geometryFactory.createLinearRing(cs1);
        org.locationtech.jts.geom.Polygon poly1 = geometryFactory.createPolygon(shape1, null);

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(cropEnvelope);
        param.parameter("ROI").setValue(poly1);

        GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
            Viewer.show(coverage, "Original");
            Viewer.show(cropped, "Cropped");
            Thread.sleep(10000);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(cropped.getRenderedImage()).getTiles());
        }
        RenderedImage raster = cropped.getRenderedImage();

        assertEquals(0, raster.getMinX());
        assertEquals(115, raster.getMinY());
        assertEquals(113, raster.getWidth());
        assertEquals(230, raster.getHeight());
        assertEquals(
                source.getGridGeometry().getGridToCRS2D(),
                cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));
    }

    /** Tests cropping to an external ROI. */
    @Test
    public void testCropWithExternalROI()
            throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage;
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {oldEnvelope.getMinimum(0), oldEnvelope.getMinimum(1)},
                        new double[] {
                            oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 4,
                            oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1)
                        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());

        GeometryFactory geometryFactory =
                JTSFactoryFinder.getGeometryFactory(JTSFactoryFinder.EMPTY_HINTS);

        // external tri
        CoordinateSequence cs1 = new CoordinateArraySequence(4);
        cs1.setOrdinate(0, 0, oldEnvelope.getMaximum(0) + 1);
        cs1.setOrdinate(0, 1, oldEnvelope.getMaximum(1));
        cs1.setOrdinate(1, 0, oldEnvelope.getMaximum(0) + 2);
        cs1.setOrdinate(1, 1, oldEnvelope.getMaximum(1));
        cs1.setOrdinate(2, 0, oldEnvelope.getMaximum(0) + 2);
        cs1.setOrdinate(2, 1, oldEnvelope.getMinimum(1));
        cs1.setOrdinate(3, 0, oldEnvelope.getMaximum(0) + 1);
        cs1.setOrdinate(3, 1, oldEnvelope.getMaximum(1));

        LinearRing shape1 = geometryFactory.createLinearRing(cs1);
        org.locationtech.jts.geom.Polygon poly1 = geometryFactory.createPolygon(shape1, null);

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(cropEnvelope);
        param.parameter("ROI").setValue(poly1);

        try {
            processor.doOperation(param);
            fail("Cropping outside bounds");
        } catch (CannotCropException expected) {
        }
    }

    /** Test robustness checks */
    @Test
    public void testCropWithoutNeededParams()
            throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage;

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);

        try {
            processor.doOperation(param);
            fail("Allowing missing parameters");
        } catch (CannotCropException expected) {
        }
    }

    /** Tests the "Crop" operation with a ROI set, clipping at half pixel. */
    @Test
    public void testCropWithROIHalfPixel()
            throws TransformException, InterruptedException, FactoryException {
        // Disable MediaLib for this test
        // Getting initial value
        String disableMediaLibKey = "com.sun.media.jai.disableMediaLib";
        String oldDisableMediaLib = System.getProperty(disableMediaLibKey, "false");
        Registry.setNativeAccelerationAllowed("Mosaic", false);
        // getting CoverageProcessor
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Create a simple Red image
         */
        Byte[] red = new Byte[] {(byte) 255, 0, 0};
        RenderedOp image =
                ConstantDescriptor.create(Float.valueOf(40), Float.valueOf(37), red, null);
        final Envelope envelope =
                new ReferencedEnvelope(-1d, 1d, -1d, 1d, DefaultGeographicCRS.WGS84);
        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverageFactory factory = new GridCoverageFactory(GeoTools.getDefaultHints());
        final GridCoverage2D source = factory.create("test", image, envelope);

        // Creating ROI for cropping
        final ReferencedEnvelope cropBounds =
                new ReferencedEnvelope(0d, 1d, 0d, 1d, DefaultGeographicCRS.WGS84);
        org.locationtech.jts.geom.Polygon polygon = JTS.toGeometry(cropBounds);
        Geometry roi =
                polygon.getFactory()
                        .createMultiPolygon(new org.locationtech.jts.geom.Polygon[] {polygon});

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(cropBounds);
        param.parameter("ROI").setValue(roi);

        GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
            Viewer.show(source, "Original");
            Viewer.show(cropped, "Cropped");
            Thread.sleep(10000);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(cropped.getRenderedImage()).getTiles());
        }
        RenderedImage raster = cropped.getRenderedImage();

        assertEquals(20, raster.getMinX());
        assertEquals(0, raster.getMinY());
        assertEquals(20, raster.getWidth());
        assertEquals(19, raster.getHeight());
        assertEquals(
                source.getGridGeometry().getGridToCRS2D(),
                cropped.getGridGeometry().getGridToCRS2D());

        // Ensure the pixels are RED
        byte[] result = new byte[3];
        // Upper Left
        cropped.evaluate(new DirectPosition2D(0d, 0d), result);
        assertEquals((byte) red[0], result[0]);
        assertEquals((byte) red[1], result[1]);
        assertEquals((byte) red[2], result[2]);

        // Setting old acceleration value for Mosaic
        Registry.setNativeAccelerationAllowed("Mosaic", Boolean.valueOf(oldDisableMediaLib));
    }

    /** Tests the "Crop" with a topologically invalid ROI */
    @Test
    public void testCropWithToplogicalInvalidROI()
            throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage;
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {oldEnvelope.getMinimum(0), oldEnvelope.getMinimum(1)},
                        new double[] {oldEnvelope.getMaximum(0), oldEnvelope.getMaximum(1)});
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());

        // hourglass shaped polygon, self intersecting
        // ---
        // \ /
        //  X
        // / \
        // ---
        LiteCoordinateSequence cs =
                new LiteCoordinateSequence(
                        oldEnvelope.getMinimum(0),
                        oldEnvelope.getMinimum(1),
                        oldEnvelope.getMaximum(0),
                        oldEnvelope.getMinimum(1),
                        oldEnvelope.getMinimum(0),
                        oldEnvelope.getMaximum(1),
                        oldEnvelope.getMaximum(0),
                        oldEnvelope.getMaximum(1),
                        oldEnvelope.getMinimum(0),
                        oldEnvelope.getMinimum(1));
        GeometryFactory geometryFactory =
                JTSFactoryFinder.getGeometryFactory(JTSFactoryFinder.EMPTY_HINTS);
        Geometry mask = geometryFactory.createPolygon(cs);

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(cropEnvelope);
        param.parameter("ROI").setValue(mask);

        // first, does not blow up
        GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);
        cropped = (GridCoverage2D) processor.doOperation(param);
        RenderedImage raster = cropped.getRenderedImage();

        // The value should be zero since that portion has been cut away
        assertEquals(
                0, raster.getTile(0, 0).getSample(0, raster.getMinY() + raster.getHeight() / 2, 0));
        assertTrue(cropEnvelope.equals(cropped.getEnvelope()));
    }
}
