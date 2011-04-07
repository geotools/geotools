/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import javax.media.jai.PlanarImage;

import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.Viewer;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultDerivedCRS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;

import org.junit.*;
import static org.junit.Assert.*;



/**
 * Tests the crop operation.
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @author Martin Desruisseaux (Geomatys)
 * @author Emanuele Tajariol (GeoSolutions)
 *
 * @since 2.3
 */
public final class CropTest extends GridProcessingTestBase {
    /**
     * The grid coverage to test.
     */
    private GridCoverage2D coverage;

    /**
     * Set up common objects used for all tests.
     */
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
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage.view(ViewType.NATIVE);
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
                oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 3 / 8,
                oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 3 / 8
        }, new double[] {
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
        assertEquals(source.getGridGeometry().getGridToCRS2D(),
                    cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));

    }
    
    /**
     * Tests the "Crop" operation by providing different implementations of the
     * opengis's Envelope interface instead of GeneralEnvelope in order to fix 
     * GEOT-3434.
     *
     * @throws TransformException if a transformation was required and failed.
     */
    @Test
    public void testCropEnvelopeSupport() throws TransformException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage.view(ViewType.NATIVE);
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
                oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 3 / 8,
                oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 3 / 8
        }, new double[] {
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
        assertEquals(source.getGridGeometry().getGridToCRS2D(),
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
        assertEquals(source.getGridGeometry().getGridToCRS2D(),
                    cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));

    }

    /**
     * Tests the "Crop" operation when there exists a rotation in the world to
     * grid transformation
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
        final GridCoverage2D rotated = project(source, crs, null, null, true);
        /*
         * Preparing the crop. We want to get a rectangle that is locate at the
         * center of this coverage envelope that is large 1/4 f the original
         * width and tall 1/4 of the original height.
         */
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        final Envelope oldEnvelope = rotated.getEnvelope();
        final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
                oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 3 / 8,
                oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 3 / 8
        }, new double[] {
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
        assertEquals(rotated.getGridGeometry().getGridToCRS2D(),
                     cropped.getGridGeometry().getGridToCRS2D());
        /*
         * Get the roi and test it against the crop area
         */
        Object property = cropped.getProperty("GC_ROI");
        assertNotNull(property);
        assertTrue(property instanceof Polygon);
        Polygon roi = (Polygon) property;
        assertEquals(new Rectangle(raster.getMinX(), raster.getMinY(),
                raster.getWidth(), raster.getHeight()), roi.getBounds());

    }

    /**
     * Tests the "Crop" operation with a ROI set.
     */
    @Test
    public void testCropWithROI() throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage.view(ViewType.NATIVE);
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
                oldEnvelope.getMinimum(0) /*+ oldEnvelope.getSpan(0) * 3 / 8*/,
                oldEnvelope.getMinimum(1) /*+ oldEnvelope.getSpan(1) * 3 / 8*/
        }, new double[] {
                oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) * 5 / 8,
                oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) * 5 / 8
        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());


        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( JTSFactoryFinder.EMPTY_HINTS );

        double mid0 = oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0)/2;
        double mid1 = oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1)* 4/10;
        double qspan0 = oldEnvelope.getSpan(0) * 2/16;
        double qspan1 = oldEnvelope.getSpan(1) * 2/16;

        double half0 = oldEnvelope.getSpan(0) * 1/10;
        double half1 = oldEnvelope.getSpan(1) * 1/10;

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
        com.vividsolutions.jts.geom.Polygon poly1 = geometryFactory.createPolygon(shape1, null);

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
        com.vividsolutions.jts.geom.Polygon poly2 = geometryFactory.createPolygon(shape2, null);

        // Disjuncted square lower left -- will be included
        CoordinateSequence cs3 = new CoordinateArraySequence(5);
        cs3.setOrdinate(0, 0, oldEnvelope.getMinimum(0));
        cs3.setOrdinate(0, 1, oldEnvelope.getMinimum(1));
        cs3.setOrdinate(1, 0, oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 8);
        cs3.setOrdinate(1, 1, oldEnvelope.getMinimum(1));
        cs3.setOrdinate(2, 0, oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 8);
        cs3.setOrdinate(2, 1, oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) / 8);
        cs3.setOrdinate(3, 0, oldEnvelope.getMinimum(0) );
        cs3.setOrdinate(3, 1, oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) / 8);
        cs3.setOrdinate(4, 0, oldEnvelope.getMinimum(0));
        cs3.setOrdinate(4, 1, oldEnvelope.getMinimum(1));

        LinearRing shape3 = geometryFactory.createLinearRing(cs3);
        com.vividsolutions.jts.geom.Polygon poly3 = geometryFactory.createPolygon(shape3, null);

        // Disjuncted square lower right -- will be excluded by Envelope
        CoordinateSequence cs4 = new CoordinateArraySequence(5);
        cs4.setOrdinate(0, 0, oldEnvelope.getMaximum(0));
        cs4.setOrdinate(0, 1, oldEnvelope.getMinimum(1));
        cs4.setOrdinate(1, 0, oldEnvelope.getMaximum(0) - oldEnvelope.getSpan(0) / 8);
        cs4.setOrdinate(1, 1, oldEnvelope.getMinimum(1));
        cs4.setOrdinate(2, 0, oldEnvelope.getMaximum(0) - oldEnvelope.getSpan(0) / 8);
        cs4.setOrdinate(2, 1, oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) / 8);
        cs4.setOrdinate(3, 0, oldEnvelope.getMaximum(0) );
        cs4.setOrdinate(3, 1, oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1) / 8);
        cs4.setOrdinate(4, 0, oldEnvelope.getMaximum(0));
        cs4.setOrdinate(4, 1, oldEnvelope.getMinimum(1));

        LinearRing shape4 = geometryFactory.createLinearRing(cs4);
        com.vividsolutions.jts.geom.Polygon poly4 = geometryFactory.createPolygon(shape4, null);

        Geometry mpoly = geometryFactory.createMultiPolygon(new com.vividsolutions.jts.geom.Polygon[]{poly1, poly2});

//        Geometry union = geometryFactory.createGeometryCollection(new Geometry[]{
//            poly1, poly2, poly3, poly4});
        Geometry union = geometryFactory.createGeometryCollection(new Geometry[]{
            mpoly, poly3, poly4});

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
        assertEquals(source.getGridGeometry().getGridToCRS2D(),
                    cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));

    }

    /**
     * Tests the intersection of the ROI and the cropEnvelope in the "Crop" operation.
     *
     */
    @Test
    public void testCropWithROIIntersection() throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage.view(ViewType.NATIVE);
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
                oldEnvelope.getMinimum(0) /*+ oldEnvelope.getSpan(0) * 3 / 8*/,
                oldEnvelope.getMinimum(1) /*+ oldEnvelope.getSpan(1) * 3 / 8*/
        }, new double[] {
                oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 4,
                oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1)
        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( JTSFactoryFinder.EMPTY_HINTS );

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
        com.vividsolutions.jts.geom.Polygon poly1 = geometryFactory.createPolygon(shape1, null);

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
        assertEquals(source.getGridGeometry().getGridToCRS2D(),
                    cropped.getGridGeometry().getGridToCRS2D());
        assertFalse(cropEnvelope.equals(cropped.getEnvelope()));

    }

    /**
     * Tests cropping to an external ROI.
     */
    @Test
    public void testCropWithExternalROI() throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage.view(ViewType.NATIVE);
        final Envelope oldEnvelope = source.getEnvelope();
        final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
                oldEnvelope.getMinimum(0),
                oldEnvelope.getMinimum(1)
        }, new double[] {
                oldEnvelope.getMinimum(0) + oldEnvelope.getSpan(0) / 4,
                oldEnvelope.getMinimum(1) + oldEnvelope.getSpan(1)
        });
        cropEnvelope.setCoordinateReferenceSystem(oldEnvelope.getCoordinateReferenceSystem());


        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( JTSFactoryFinder.EMPTY_HINTS );

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
        com.vividsolutions.jts.geom.Polygon poly1 = geometryFactory.createPolygon(shape1, null);

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);
        param.parameter("Envelope").setValue(cropEnvelope);
        param.parameter("ROI").setValue(poly1);

        try {
            processor.doOperation(param);
            fail("Cropping outside bounds");
        } catch (CannotCropException _) {
        }

    }

    /**
     * Test robustness checks
     */
    @Test
    public void testCropWithoutNeededParams() throws TransformException, InterruptedException, FactoryException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();

        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = coverage.view(ViewType.NATIVE);

        ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(source);

        try {
            processor.doOperation(param);
            fail("Allowing missing parameters");
        } catch (CannotCropException _) {
        }

    }

}