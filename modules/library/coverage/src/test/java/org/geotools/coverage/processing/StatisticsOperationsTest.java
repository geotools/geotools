/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.AffineTransform;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import javax.media.jai.RasterFactory;

import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.operation.Extrema;
import org.geotools.coverage.processing.operation.Histogram;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Testing Extrema and {@link Histogram} operations.
 *
 * @author Simone Giannecchini (GeoSolutions)
 * @author Martin Desruisseaux (Geomatys)
 *
 * @source $URL$
 */
public final class StatisticsOperationsTest extends GridProcessingTestBase {
    /**
     * Creates a raster of the given type. Because we use only one tile with one band, the
     * code below is pretty similar to the code we would have if we were just setting the
     * values in a matrix.
     */
    private static GridCoverage2D createRaster(final int type) {
        final int width  = 500;
        final int height = 500;
        final WritableRaster raster = RasterFactory.createBandedRaster(type, width, height, 1, null);
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                // We exploit the clamping capabilities of the sample model.
                raster.setSample(x, y, 0, x + y);
            }
        }
        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        final Envelope envelope = new Envelope2D(crs, 0, 0, 30, 30);
        final GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        return factory.create("My grayscale float coverage", raster, envelope);
    }

    /**
     * Tests the "Extrema" operation with a raster of floating point values.
     * This test compare the operation results with the expected ones using
     * different subsampling values.
     */
    @Test
    public void testExtrema() {
        final GridCoverage2D sampleFloatCoverage = createRaster(DataBuffer.TYPE_FLOAT);
        assertEquals(DataBuffer.TYPE_FLOAT, sampleFloatCoverage.getRenderedImage().getSampleModel().getDataType());
        final AffineTransform gridToCRS = getAffineTransform(sampleFloatCoverage);
        assertNotNull(gridToCRS);
        /*
         * Create the ROI as a JTS polygon.
         */
        final PrecisionModel   pm = new PrecisionModel();
        final GeometryFactory  gf = new GeometryFactory(pm, 0);
        final Envelope2D envelope = sampleFloatCoverage.getEnvelope2D();
        final double minX = envelope.getMinX();
        final double minY = envelope.getMinY();
        final double maxX = envelope.getCenterX();
        final double maxY = envelope.getCenterY();
        final Coordinate[] corners = new Coordinate[] {
                new Coordinate(minX, minY),
                new Coordinate(maxX, minY),
                new Coordinate(maxX, maxY),
                new Coordinate(minX, maxY),
                new Coordinate(minX, minY)
        };
        final LinearRing ring = gf.createLinearRing(corners);
        final Polygon roi = new Polygon(ring, null, gf);
        /*
         * Create the operation for the Extrema with a ROI.
         * No subsampling should be applied.
         */
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        final AbstractOperation op = (AbstractOperation) processor.getOperation("Extrema");
        assertTrue(op instanceof Extrema);
        final ParameterValueGroup params = op.getParameters();
        params.parameter("Source") .setValue(sampleFloatCoverage);
        params.parameter("xPeriod").setValue(1 * XAffineTransform.getScaleX0(gridToCRS));
        params.parameter("yPeriod").setValue(1 * XAffineTransform.getScaleY0(gridToCRS));
        params.parameter("roi").setValue(roi);
        GridCoverage2D coverage = (GridCoverage2D) op.doOperation(params, null);
        double[] minimum = (double[]) coverage.getProperty("minimum");
        double[] maximum = (double[]) coverage.getProperty("maximum");
        assertEquals(1, minimum.length);
        assertEquals(1, maximum.length);
        assertEquals(250.0, minimum[0], 0);
        assertEquals(748.0, maximum[0], 0);
        /*
         * Create the operation for the Extrema with a ROI and subsampling by 2. The
         * source and the ROI are left unchanged. The maximal value is expected to be
         * slightly different than the above test because of the change in subsampling.
         */
        params.parameter("xPeriod").setValue(2 * XAffineTransform.getScaleX0(gridToCRS));
        params.parameter("yPeriod").setValue(2 * XAffineTransform.getScaleY0(gridToCRS));
        coverage = (GridCoverage2D) op.doOperation(params, null);
        minimum = (double[]) coverage.getProperty("minimum");
        maximum = (double[]) coverage.getProperty("maximum");
        assertEquals(1, minimum.length);
        assertEquals(1, maximum.length);
        assertEquals(250.0, minimum[0], 0);
        assertEquals(746.0, maximum[0], 0);
    }

    /**
     * Tests the "Histogram" operation with a raster of byte values.
     * This test compare the operation results with the expected ones
     * using different subsampling values.
     */
    @Test
    public void testHistogram() {
        final GridCoverage2D sampleByteCoverage  = createRaster(DataBuffer.TYPE_BYTE );
        assertEquals(DataBuffer.TYPE_BYTE,  sampleByteCoverage .getRenderedImage().getSampleModel().getDataType());
        final AffineTransform gridToCRS = getAffineTransform(sampleByteCoverage);
        assertNotNull(gridToCRS);
        /*
         * Create the ROI as a JTS polygon.
         */
        final PrecisionModel   pm = new PrecisionModel();
        final GeometryFactory  gf = new GeometryFactory(pm, 0);
        final Envelope2D envelope = sampleByteCoverage.getEnvelope2D();
        final double minX = envelope.getMinX();
        final double maxY = envelope.getMaxY();
        final double minY = maxY - envelope.getHeight() / 16;
        final double maxX = minX + envelope.getWidth()  / 16;
        final Coordinate[] coord = new Coordinate[] {
                new Coordinate(minX, maxY),
                new Coordinate(maxX, maxY),
                new Coordinate(maxX, minY),
                new Coordinate(minX, minY),
                new Coordinate(minX, maxY)
        };
        final LinearRing ring = gf.createLinearRing(coord);
        final Polygon roi = new Polygon(ring, null, gf);
        /*
         * Create the operation for the Histogram with a ROI.
         * No subsampling should be applied.
         */
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        final AbstractOperation op = (AbstractOperation) processor.getOperation("Histogram");
        assertTrue(op instanceof Histogram);
        final ParameterValueGroup params = op.getParameters();
        params.parameter("Source").setValue(sampleByteCoverage);
        params.parameter("xPeriod").setValue(1 * XAffineTransform.getScaleX0(gridToCRS));
        params.parameter("yPeriod").setValue(1 * XAffineTransform.getScaleY0(gridToCRS));

        params.parameter("roi").setValue(roi);

        GridCoverage2D coverage = (GridCoverage2D) op.doOperation(params, null);
        javax.media.jai.Histogram histogram = (javax.media.jai.Histogram) coverage
                .getProperty(Histogram.GT_SYNTHETIC_PROPERTY_HISTOGRAM);
        assertEquals(0, histogram.getBinSize(0, 255));
        assertEquals(1, histogram.getBinSize(0, 60));
        /*
         * Create the operation for the Histogram with a ROI and subsampling by 7. The
         * source and the ROI are left unchanged. The histogram values are expected to
         * be slightly different than the above test because of the change in subsampling.
         */
        params.parameter("xPeriod").setValue(7 * XAffineTransform.getScaleX0(gridToCRS));
        params.parameter("yPeriod").setValue(7 * XAffineTransform.getScaleY0(gridToCRS));
        coverage = (GridCoverage2D) op.doOperation(params, null);
        histogram = (javax.media.jai.Histogram) coverage
                .getProperty(Histogram.GT_SYNTHETIC_PROPERTY_HISTOGRAM);
        assertEquals(0, histogram.getBinSize(0, 255));
        assertEquals(0, histogram.getBinSize(0, 60));
        assertEquals(1, histogram.getBinSize(0, 56));
    }
}
