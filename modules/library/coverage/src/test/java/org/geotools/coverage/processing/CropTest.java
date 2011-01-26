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

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import javax.media.jai.PlanarImage;

import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.Viewer;
import org.geotools.geometry.GeneralEnvelope;
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
        final AbstractProcessor processor = AbstractProcessor.getInstance();
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
        final AbstractProcessor processor = AbstractProcessor.getInstance();
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
}
