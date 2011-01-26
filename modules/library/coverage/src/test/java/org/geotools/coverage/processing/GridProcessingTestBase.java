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
package org.geotools.coverage.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;

import javax.media.jai.RenderedOp;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageTestBase;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.Viewer;
import org.geotools.factory.Hints;
import org.geotools.referencing.crs.DefaultDerivedCRS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;


/**
 * Base class for grid processing tests. This class provides a few convenience
 * methods performing some operations on {@link GridCoverage2D}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class GridProcessingTestBase extends GridCoverageTestBase {
    /**
     * Rotates the given coverage by the given angle. This method replaces
     * the coverage CRS by a derived one containing the rotated axes.
     *
     * @param  coverage The coverage to rotate.
     * @param  angle The rotation angle, in degrees.
     * @return The rotated coverage.
     */
    protected static GridCoverage2D rotate(final GridCoverage2D coverage, final double angle) {
        final AffineTransform atr = AffineTransform.getRotateInstance(Math.toRadians(angle));
        atr.concatenate(getAffineTransform(coverage));
        final MathTransform tr = ProjectiveTransform.create(atr);
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
        crs = new DefaultDerivedCRS("Rotation " + angle + "°", crs, tr, crs.getCoordinateSystem());
        return project(coverage, crs, null, null, true);
    }

    /**
     * Projects the specified coverage to the specified CRS using the specified hints.
     *
     * @param coverage  The coverage to project.
     * @param targetCRS The target CRS, or {@code null} if the same.
     * @param geometry  The target geometry, or {@code null} if the same.
     * @param hints     An optional set of hints, or {@code null} if none.
     * @param useGeophysics {@code true} for projecting the geophysics view.
     * @return The operation name which was applied on the image, or {@code null} if none.
     */
    protected static GridCoverage2D project(GridCoverage2D            coverage,
                                      final CoordinateReferenceSystem targetCRS,
                                      final GridGeometry2D            geometry,
                                      final Hints                     hints,
                                      final boolean                   useGeophysics)
    {
        return project(coverage, targetCRS, geometry, "bilinear", hints, useGeophysics);

        
    }
    
    /**
     * Projects the specified coverage to the specified CRS using the specified hints.
     *
     * @param coverage  The coverage to project.
     * @param targetCRS The target CRS, or {@code null} if the same.
     * @param geometry  The target geometry, or {@code null} if the same.
     * @param interpolationType The target interpolation.
     * @param hints     An optional set of hints, or {@code null} if none.
     * @param useGeophysics {@code true} for projecting the geophysics view.
     * @return The operation name which was applied on the image, or {@code null} if none.
     */
    protected static GridCoverage2D project(GridCoverage2D            coverage,
                                      final CoordinateReferenceSystem targetCRS,
                                      final GridGeometry2D            geometry,
                                      final String                    interpolationType,
                                      final Hints                     hints,
                                      final boolean                   useGeophysics)
    {
        final CoverageProcessor processor = CoverageProcessor.getInstance(hints);
        coverage = coverage.view(useGeophysics ? ViewType.GEOPHYSICS : ViewType.PACKED);
        final ParameterValueGroup param = processor.getOperation("Resample").getParameters();
        param.parameter("Source").setValue(coverage);
        if (targetCRS != null) {
            param.parameter("CoordinateReferenceSystem").setValue(targetCRS);
        }
        if (geometry != null) {
            param.parameter( "GridGeometry").setValue(geometry);
        }
        if (interpolationType != null&&interpolationType.length()!=0) {
            param.parameter( "InterpolationType").setValue(interpolationType);
        }
        coverage = (GridCoverage2D) processor.doOperation(param);
        return coverage;
    }

    /**
     * Projects the specified coverage to the specified CRS using the specified hints.
     * The result will be displayed in a window if {@link #SHOW} is set to {@code true}.
     *
     * @param coverage  The coverage to project.
     * @param targetCRS The target CRS, or {@code null} if the same.
     * @param geometry  The target geometry, or {@code null} if the same.
     * @param hints     An optional set of hints, or {@code null} if none.
     * @param useGeophysics {@code true} for projecting the geophysics view.
     * @return The operation name which was applied on the image, or {@code null} if none.
     */
    protected static String showProjected(GridCoverage2D            coverage,
                                    final CoordinateReferenceSystem targetCRS,
                                    final GridGeometry2D            geometry,
                                    final Hints                     hints,
                                    final boolean                   useGeophysics)
    {
        coverage = project(coverage, targetCRS, geometry, hints, useGeophysics);
        final RenderedImage image = coverage.getRenderedImage();
        String operation = null;
        if (image instanceof RenderedOp) {
            operation = ((RenderedOp) image).getOperationName();
            CoverageProcessor.LOGGER.fine("Applied \"" + operation + "\" JAI operation.");
        }
        coverage = coverage.view(ViewType.PACKED);
        if (SHOW) {
            /*
             * Note: In current Resample implementation, simple affine transforms like
             *       translations will not be visible with the simple viewer used here.
             *       It would be visible however with more elaborated viewers.
             */
            Viewer.show(coverage, operation);
        } else {
            // Forces computation in order to check if an exception is thrown.
            assertNotNull(coverage.getRenderedImage().getData());
        }
        return operation;
    }

    /**
     * Performs an affine transformation on the provided coverage. The transformation is
     * a translation by 5 units along x and y axes. The result will be displayed in a window
     * if {@link #SHOW} is set to {@code true}.
     *
     * @param coverage
     *          The coverage to apply the operation on.
     * @param hints
     *          An optional set of hints, or {@code null} if none.
     * @param useGeophysics
     *          {@code true} for performing the operation on the geophysics view.
     * @param asCRS
     *          The expected operation name if the resampling is performed as a CRS change.
     * @param asGG
     *          The expected operation name if the resampling is perofrmed as a Grid Geometry change.
     */
    protected static void showTranslated(GridCoverage2D coverage,
                                   final Hints    hints,
                                   final boolean  useGeophysics,
                                   final String   asCRS,
                                   final String   asGG)
    {
        final AffineTransform atr = AffineTransform.getTranslateInstance(5, 5);
        atr.concatenate(getAffineTransform(coverage));
        final MathTransform tr = ProjectiveTransform.create(atr);
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
        crs = new DefaultDerivedCRS("Translated", crs, tr, crs.getCoordinateSystem());
        assertEquals(asCRS,
                showProjected(coverage, crs, null, hints, useGeophysics));

        // Same operation, given the translation in the GridGeometry argument rather than the CRS.
        final GridGeometry2D gg = new GridGeometry2D(null, tr, null);
        assertEquals(asGG,
                showProjected(coverage, null, gg, hints, useGeophysics));

        // TODO: we should probably invoke "assertRasterEquals" with both coverages.
    }
}
