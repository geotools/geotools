/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.util.Map;
import javax.media.jai.Interpolation;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.Viewer;
import org.junit.*;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Tests the scale operation.
 *
 * @version $Id$
 * @author Simone Giannecchini (GeoSolutions)
 * @since 12.0
 */
public class AffineTest extends GridProcessingTestBase {
    /** The processor to be used for all tests. */
    private CoverageProcessor processor;

    /** Set up common objects used for all tests. */
    @Before
    public void setUp() {
        JAIExt.initJAIEXT();
        processor = CoverageProcessor.getInstance(null);
    }

    /**
     * Tests the "Scale" operation.
     *
     * @todo Disabled for now because seems to be trapped in a never ending loop.
     */
    @Test
    public void testAffine() {
        final GridCoverage2D originallyIndexedCoverage = EXAMPLES.get(0);
        final GridCoverage2D indexedCoverage = EXAMPLES.get(2);
        final GridCoverage2D indexedCoverageWithTransparency = EXAMPLES.get(3);
        final GridCoverage2D floatCoverage = EXAMPLES.get(4);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor interpolation
        //
        ///////////////////////////////////////////////////////////////////////
        Interpolation interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        affine(originallyIndexedCoverage, interp, null, null);
        affine(indexedCoverage, interp, null, null);
        affine(indexedCoverageWithTransparency, interp, null, null);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor interpolation and ROI / NoData.
        //
        ///////////////////////////////////////////////////////////////////////
        RenderedImage src = originallyIndexedCoverage.getRenderedImage();
        ROI roi =
                new ROIShape(
                        new Rectangle(
                                src.getMinX() + 1,
                                src.getMinY() + 1,
                                src.getWidth() / 2,
                                src.getHeight() / 2));
        Range nodata = RangeFactory.create(12, 12);
        affine(originallyIndexedCoverage, interp, roi, null);
        affine(originallyIndexedCoverage, interp, null, nodata);

        ///////////////////////////////////////////////////////////////////////
        //
        // Bilinear interpolation
        //
        ///////////////////////////////////////////////////////////////////////
        interp = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
        affine(indexedCoverage, interp, null, null);
        affine(indexedCoverageWithTransparency, interp, null, null);

        ///////////////////////////////////////////////////////////////////////
        //
        // Nearest neighbor  interpolation  for a float coverage
        //
        ///////////////////////////////////////////////////////////////////////
        interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        affine(floatCoverage, interp, null, null);

        // Play with a rotated coverage
        affine(rotate(floatCoverage, Math.PI / 4), null, null, null);
    }

    /**
     * Applies an affine operation on the photographic view of the given coverage.
     *
     * @param coverage The coverage to transfor.
     * @param interp The interpolation to use.
     */
    private void affine(
            final GridCoverage2D coverage,
            final Interpolation interp,
            final ROI roi,
            final Range nodata) {
        // Caching initial properties.
        final RenderedImage originalImage = coverage.getRenderedImage();
        final int w = originalImage.getWidth();
        final int h = originalImage.getHeight();

        // Getting parameters for doing a scale.
        final ParameterValueGroup param = processor.getOperation("Affine").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("transform").setValue(new AffineTransform(0.5, 0.0, 0.0, 0.5, 0.0, 0.0));
        param.parameter("Interpolation").setValue(interp);
        boolean jaiextAffine = JAIExt.isJAIExtOperation("Affine");
        if (roi != null && jaiextAffine) {
            param.parameter("roi").setValue(roi);
        }

        // Doing a first scale.
        GridCoverage2D scaled = (GridCoverage2D) processor.doOperation(param);
        assertEnvelopeEquals(coverage, scaled);
        RenderedImage scaledImage = scaled.getRenderedImage();
        assertEquals(w / 2.0, scaledImage.getWidth(), EPS);
        assertEquals(h / 2.0, scaledImage.getHeight(), EPS);
        if (SHOW) {
            Viewer.show(coverage);
            Viewer.show(scaled);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles());
            assertNotNull(PlanarImage.wrapRenderedImage(scaledImage).getTiles());
        }
        // Ensure a new ROI property has been created
        Map<String, Object> properties = scaled.getProperties();
        if (jaiextAffine && roi != null) {
            assertNotNull(properties);
            assertTrue(properties.containsKey("GC_ROI"));
            assertTrue(properties.get("GC_ROI") instanceof ROI);
        }

        // Doing another scale using the default processor.
        scaled =
                (GridCoverage2D)
                        Operations.DEFAULT.affine(
                                scaled, AffineTransform.getScaleInstance(3, 3), interp, null);
        scaledImage = scaled.getRenderedImage();
        assertEnvelopeEquals(coverage, scaled);
        assertEquals(w * 1.5, scaledImage.getWidth(), EPS);
        assertEquals(h * 1.5, scaledImage.getHeight(), EPS);
        if (SHOW) {
            Viewer.show(scaled);
        } else {
            // Force computation
            assertNotNull(scaledImage.getData());
        }
    }
}
