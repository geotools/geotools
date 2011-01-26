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
package org.geotools.coverage.processing.operation;

import java.awt.Dimension;

import javax.media.jai.Interpolation;
import javax.media.jai.operator.AffineDescriptor;
import javax.media.jai.operator.WarpDescriptor;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CannotReprojectException;
import org.geotools.coverage.processing.Operation2D;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


/**
 * Resample a grid coverage using a different grid geometry. This operation provides the following
 * functionality:
 * <p>
 * <UL>
 *   <LI><strong>Resampling</strong><br>
 *       The grid coverage can be resampled at a different cell resolution. Some implementations
 *       may be able to do resampling efficiently at any resolution. Also a non-rectilinear grid
 *       coverage can be accessed as rectilinear grid coverage with this operation.</LI>
 *   <LI><strong>Reprojecting</strong><br>
 *       The new grid geometry can have a different coordinate reference system than the underlying
 *       grid geometry. For example, a grid coverage can be reprojected from a geodetic coordinate
 *       reference system to Universal Transverse Mercator CRS.</LI>
 *   <LI><strong>Subsetting</strong><br>
 *       A subset of a grid can be viewed as a separate coverage by using this operation with a
 *       grid geometry which as the same geoferencing and a region. Grid range in the grid geometry
 *       defines the region to subset in the grid coverage.</LI>
 * </UL>
 * <p>
 * <strong>Geotools extension:</strong><br>
 * The {@code "Resample"} operation use the default
 * {@link org.opengis.referencing.operation.CoordinateOperationFactory} for creating a
 * transformation from the source to the destination coordinate reference systems.
 * If a custom factory is desired, it may be supplied as a rendering hint with the
 * {@link org.geotools.factory.Hints#COORDINATE_OPERATION_FACTORY} key. Rendering
 * hints can be supplied to {@link org.geotools.coverage.processing.DefaultProcessor}
 * at construction time.
 *
 * <P><STRONG>Name:</STRONG>&nbsp;<CODE>"Resample"</CODE><BR>
 *    <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain AffineDescriptor Affine}"</CODE>
 *            or <CODE>"{@linkplain WarpDescriptor Warp}"</CODE><BR>
 *    <STRONG>Parameters:</STRONG></P>
 * <table border='3' cellpadding='6' bgcolor='F4F8FF'>
 *   <tr bgcolor='#B9DCFF'>
 *     <th>Name</th>
 *     <th>Class</th>
 *     <th>Default value</th>
 *     <th>Minimum value</th>
 *     <th>Maximum value</th>
 *   </tr>
 *   <tr>
 *     <td>{@code "Source"}</td>
 *     <td>{@link org.geotools.coverage.grid.GridCoverage2D}</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "InterpolationType"}</td>
 *     <td>{@link java.lang.CharSequence}</td>
 *     <td>"NearestNeighbor"</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "CoordinateReferenceSystem"}</td>
 *     <td>{@link org.opengis.referencing.crs.CoordinateReferenceSystem}</td>
 *     <td>Same as source grid coverage</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "GridGeometry"}</td>
 *     <td>{@link org.opengis.coverage.grid.GridGeometry}</td>
 *     <td>(automatic)</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 *    <tr>
 *     <td>{@code "BackgroundValues"}</td>
 *     <td>{@code double[]}</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *     <td align="center">N/A</td>
 *   </tr>
 * </table>
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Giannecchini Simone, GeoSolutions SAS
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 * @see org.geotools.coverage.processing.Operations#resample
 * @see WarpDescriptor
 */
public class Resample extends Operation2D {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2022393087647420577L;

    /**
     * The parameter descriptor for the interpolation type.
     */
    public static final ParameterDescriptor<Object> INTERPOLATION_TYPE =
            new DefaultParameterDescriptor<Object>(Citations.OGC, "InterpolationType",
            	Object.class,                       // Value class (mandatory)
                null,                               // Array of valid values
                "NearestNeighbor",                  // Default value
                null,                               // Minimal value
                null,                               // Maximal value
                null,                               // Unit of measure
                false);                             // Parameter is optional

    /**
     * The parameter descriptor for the coordinate reference system.
     */
    public static final ParameterDescriptor<CoordinateReferenceSystem> COORDINATE_REFERENCE_SYSTEM =
            new DefaultParameterDescriptor<CoordinateReferenceSystem>(Citations.OGC, "CoordinateReferenceSystem",
                CoordinateReferenceSystem.class,    // Value class (mandatory)
                null,                               // Array of valid values
                null,                               // Default value
                null,                               // Minimal value
                null,                               // Maximal value
                null,                               // Unit of measure
                false);                             // Parameter is optional

    /**
     * The parameter descriptor for the grid geometry.
     */
    public static final ParameterDescriptor<GridGeometry> GRID_GEOMETRY =
            new DefaultParameterDescriptor<GridGeometry>(Citations.OGC, "GridGeometry",
                GridGeometry.class,                 // Value class (mandatory)
                null,                               // Array of valid values
                null,                               // Default value
                null,                               // Minimal value
                null,                               // Maximal value
                null,                               // Unit of measure
                false);                             // Parameter is optional
    
    /**
     * The parameter descriptor for the BackgroundValues.
     */
    public static final ParameterDescriptor<double[]> BACKGROUND_VALUES =
            new DefaultParameterDescriptor<double[]>(Citations.JAI, "BackgroundValues",
                double[].class,                     // Value class (mandatory)
                null,                               // Array of valid values
                null,                               // Default value
                null,                               // Minimal value
                null,                               // Maximal value
                null,                               // Unit of measure
                false);                             // Parameter is optional
            
    /**
     * Key for the reprojection operation being used (null if no operation is performed)
     */
    public static final String OPERATION = "method";
    
    /**
     * Key for the warp operation {@link Class}, null otherwise
     */
    public static final String WARP_TYPE = "warpType";
    
    /**
     * Key for the warp grid dimensions, available only if a WarpGrid is being used.
     * Returned as a {@link Dimension} object
     */
    public static final String GRID_DIMENSIONS = "gridDimensions";


    /**
     * Constructs a {@code "Resample"} operation.
     */
    public Resample() {
        super(new DefaultParameterDescriptorGroup(Citations.OGC, "Resample",
              new ParameterDescriptor[] {
                    SOURCE_0,
                    INTERPOLATION_TYPE,
                    COORDINATE_REFERENCE_SYSTEM,
                    GRID_GEOMETRY,
                    BACKGROUND_VALUES
        }));
    }

    /**
     * Resamples a grid coverage. This method is invoked by
     * {@link org.geotools.coverage.processing.DefaultProcessor}
     * for the {@code "Resample"} operation.
     */
    @SuppressWarnings("unchecked")
    public Coverage doOperation(final ParameterValueGroup parameters, final Hints hints) {
        final GridCoverage2D source = (GridCoverage2D) parameters.parameter("Source").getValue();
        final Interpolation interpolation = ImageUtilities.toInterpolation(
                parameters.parameter("InterpolationType").getValue());
        CoordinateReferenceSystem targetCRS = (CoordinateReferenceSystem)
                parameters.parameter("CoordinateReferenceSystem").getValue();
        if (targetCRS == null) {
            targetCRS = source.getCoordinateReferenceSystem();
        }
        final GridGeometry2D targetGG = GridGeometry2D.wrap(
                (GridGeometry) parameters.parameter("GridGeometry").getValue());
        final Object bgValueParam = parameters.parameter("BackgroundValues");
        final double [] bgValues;
        if (bgValueParam != null && bgValueParam instanceof Parameter<?>){
            bgValues = ((Parameter<double[]>)bgValueParam).getValue();
        } else {
            bgValues = null;
        }
        try {
            return Resampler2D.reproject(source, targetCRS, targetGG, interpolation,
                (hints instanceof Hints) ? hints : new Hints(hints), bgValues);
        } catch (FactoryException exception) {
            throw new CannotReprojectException(Errors.format(
                    ErrorKeys.CANT_REPROJECT_$1, source.getName()), exception);
        } catch (TransformException exception) {
            throw new CannotReprojectException(Errors.format(
                    ErrorKeys.CANT_REPROJECT_$1, source.getName()), exception);
        }
    }

    /**
     * Computes a grid geometry from a source coverage and a target envelope. This is a convenience
     * method for computing the {@link #GRID_GEOMETRY} argument of a {@code "resample"} operation
     * from an envelope. The target envelope may contains a different coordinate reference system,
     * in which case a reprojection will be performed.
     *
     * @param source The source coverage.
     * @param target The target envelope, including a possibly different coordinate reference system.
     * @return A grid geometry inferred from the target envelope.
     * @throws TransformException If a transformation was required and failed.
     *
     * @since 2.5
     */
    public static GridGeometry computeGridGeometry(final GridCoverage source, final Envelope target)
            throws TransformException
    {
        final CoordinateReferenceSystem targetCRS = target.getCoordinateReferenceSystem();
        final CoordinateReferenceSystem sourceCRS = source.getCoordinateReferenceSystem();
        final CoordinateReferenceSystem reducedCRS;
        if (target.getDimension() == 2 && sourceCRS.getCoordinateSystem().getDimension() != 2) {
            reducedCRS = CoverageUtilities.getCRS2D(source);
        } else {
            reducedCRS = sourceCRS;
        }
        GridGeometry gridGeometry = source.getGridGeometry();
        if (targetCRS == null || CRS.equalsIgnoreMetadata(reducedCRS, targetCRS)) {
            /*
             * Same CRS (or unknown target CRS, which we treat as same), so we will keep the same
             * "gridToCRS" transform. Basically the result will be the same as if we did a crop,
             * except that we need to take in account a change from nD to 2D.
             */
            final MathTransform gridToCRS;
            if (reducedCRS == sourceCRS) {
                gridToCRS = gridGeometry.getGridToCRS();
            } else {
                gridToCRS = GridGeometry2D.wrap(gridGeometry).getGridToCRS2D();
            }
            gridGeometry = new GridGeometry2D(PixelInCell.CELL_CENTER, gridToCRS, target, null);
        } else {
            /*
             * Different CRS. We need to infer an image size, which may be the same than the
             * original size or something smaller if the envelope is a subarea. We process by
             * transforming the target envelope to the source CRS and compute a new grid geometry
             * with that envelope. The grid range of that grid geometry is the new image size.
             * Note that failure to transform the envelope is non-fatal (we will assume that the
             * target image should have the same size). Then create again a new grid geometry,
             * this time with the target envelope.
             */
            GridEnvelope gridRange;
            try {
                final GeneralEnvelope transformed;
                transformed = CRS.transform(CRS.getCoordinateOperationFactory(true)
                        .createOperation(targetCRS, reducedCRS), target);
                final Envelope reduced;
                final MathTransform gridToCRS;
                if (reducedCRS == sourceCRS) {
                    reduced   = source.getEnvelope();
                    gridToCRS = gridGeometry.getGridToCRS();
                } else {
                    reduced   = CoverageUtilities.getEnvelope2D(source);
                    gridToCRS = GridGeometry2D.wrap(gridGeometry).getGridToCRS2D();
                }
                transformed.intersect(reduced);
                gridGeometry = new GridGeometry2D(PixelInCell.CELL_CENTER, gridToCRS, transformed, null);
            } catch (FactoryException exception) {
                recoverableException("resample", exception);
            } catch (TransformException exception) {
                recoverableException("resample", exception);
                // Will use the grid range from the original geometry,
                // which will result in keeping the same image size.
            }
            gridRange = gridGeometry.getGridRange();
            gridGeometry = new GridGeometry2D(gridRange, target);
        }
        return gridGeometry;
    }

    /**
     * Invoked when an error occurred but the application can fallback on a reasonable default.
     *
     * @param method The method where the error occurred.
     * @param exception The error.
     */
    private static void recoverableException(final String method, final Exception exception) {
        Logging.recoverableException(Resample.class, method, exception);
    }
}
