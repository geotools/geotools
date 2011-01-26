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
package org.geotools.coverage.processing.operation;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CannotCropException;
import org.geotools.coverage.processing.Operation2D;
import org.geotools.factory.Hints;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.coverage.Coverage;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * The crop operation is responsible for selecting geographic subarea of the
 * source coverage. The CoverageCrop operation does not merely wrap the JAI Crop
 * operation but it goes beyond that as far as capabilities.
 * 
 * <p>
 * The key point is that the CoverageCrop operation aims to perform a spatial
 * crop, i.e. cropping the underlying raster by providing a spatial
 * {@link Envelope} (if the envelope is not 2D only the 2D part of it will be
 * used). This means that, depending on the grid-to-world transformation
 * existing for the raster we want to crop, the crop area in the raster space
 * might not be a rectangle, hence JAI's crop may not suffice in order to shrink
 * the raster area we would obtain. For this purpose this operation make use of
 * either the JAI's Crop or Mosaic operations depending on the conditions in
 * which we are working.
 * 
 * 
 * <p>
 * <strong>Meaning of the ROI_OPTIMISATION_TOLERANCE parameter</strong> <br>
 * In general if the grid-to-world transform is a simple scale and translate
 * using JAI's crop should suffice, but when the g2w transform contains
 * rotations or skew then we need something more elaborate since a rectangle in
 * model space may not map to a rectangle in raster space. We would still be
 * able to crop using JAI's crop on this polygon bounds but, depending on how
 * this rectangle is built, we would be highly inefficient. In order to overcome
 * this problems we use a combination of JAI's crop and mosaic since the mosaic
 * can be used to crop a raster using a general ROI instead of a simple
 * rectangle. There is a negative effect though. Crop would not create a new
 * raster but simply forwards requests back to the original one (it basically
 * create a viewport on the source raster) while the mosaic operation creates a
 * new raster. We try to address this trade-off by providing the parameter
 * {@link Crop#ROI_OPTIMISATION_TOLERANCE}, which basically tells this
 * operation "Use the mosaic operation only if the area that we would load with
 * the Mosaic is strictly smaller then (ROI_OPTIMISATION_TOLERANCE)* A' where A'
 * is the area of the polygon resulting from converting the crop area from the
 * model space to the raster space.
 * 
 * <p>
 * <strong>Meaning of the CONSERVE_ENVELOPE parameter</strong> <br>
 * Deprecated.
 * 
 * <p>
 * <strong>NOTE</strong> that in case we will use the Mosaic operation with a
 * ROI, such a ROI will be added as a synthetic property to the resulting
 * coverage. The key for this property will be GC_ROI and the type of the object
 * {@link Polygon}.
 * 
 * @source $URL$
 * @todo make this operation work with a general polygon. instead of an envelope.
 * @todo make this operation more t,z friendly
 * @version $Id$
 * @author Simone Giannecchini
 * @since 2.3
 *
 * @see javax.media.jai.operator.ScaleDescriptor
 */
public class Crop extends Operation2D {
	/**
	 * Serial number for cross-version compatibility.
	 */
	private static final long serialVersionUID = 4466072819239413456L;

	/**
	 * The parameter descriptor used to pass this operation the envelope to use
	 * when doing the spatial crop.
	 */
	public static final ParameterDescriptor<Envelope> CROP_ENVELOPE = new DefaultParameterDescriptor<Envelope>(
			Citations.GEOTOOLS, "Envelope", 
			Envelope.class, // Value class
			null, // Array of valid values
			null, // Default value
			null, // Minimal value
			null, // Maximal value
			null, // Unit of measure
			false); // Parameter is optional

	/**
	 * The parameter descriptor used to tell this operation to optimize the crop
	 * using a Mosaic in where the area of the image we would not load is smaller
	 * than ROI_OPTIMISATION_TOLERANCE*FULL_CROP.
	 */
	public static final ParameterDescriptor<Double> ROI_OPTIMISATION_TOLERANCE = new DefaultParameterDescriptor<Double>(
			Citations.GEOTOOLS, "ROITolerance", 
			Double.class, // Value class
			null, // Array of valid values
			0.6,  // Default value
			0.0,  // Minimal value
			1.0,  // Maximal value
			null, // Unit of measure
			true); // Parameter is optional

	/**
         * The parameter descriptor is basically a simple boolean that tells
         * this operation to try to conserve the envelope that it gets as input.
         * 
         * <p>
         * <strong> Note that this might mean obtaining a coverage whose grid to
         * world 2D has been slightly changed to account for the rounding which
         * is applied in order to get integer coordinate for the raster to crop.
         * </strong>
         * <p>
         * 
         * See this class javadocs for an explanation.
         * @deprecated
         */
	public static final ParameterDescriptor<Boolean> CONSERVE_ENVELOPE = new DefaultParameterDescriptor<Boolean>(
			Citations.GEOTOOLS, "ConserveEnvelope", Boolean.class, // Value
			// class
			new Boolean[]{Boolean.TRUE,Boolean.FALSE}, // Array of valid values
			Boolean.FALSE, // Default value
			null, // Minimal value
			null, // Maximal value
			null, // Unit of measure
			true); // Parameter is optional

        /**
         * Constructs a default {@code "Crop"} operation.
         */
	public Crop() {
		super(new DefaultParameterDescriptorGroup(Citations.GEOTOOLS,
				"CoverageCrop", new ParameterDescriptor[] { SOURCE_0,
						CROP_ENVELOPE, CONSERVE_ENVELOPE,
						ROI_OPTIMISATION_TOLERANCE }));

	}

	/**
	 * Applies a crop operation to a coverage.
	 * 
	 * @see org.geotools.coverage.processing.AbstractOperation#doOperation(org.opengis.parameter.ParameterValueGroup,
	 *      org.geotools.factory.Hints)
	 */
	public Coverage doOperation(ParameterValueGroup parameters, Hints hints) {
		// /////////////////////////////////////////////////////////////////////
		//
		// Checking input parameters
		//
		// ///////////////////////////////////////////////////////////////////
		// source coverage
		final ParameterValue sourceParameter = parameters.parameter("Source");
		if (sourceParameter == null
				|| !(sourceParameter.getValue() instanceof GridCoverage2D)) {
			throw new CannotCropException(Errors.format(
					ErrorKeys.NULL_PARAMETER_$2, "Source", GridCoverage2D.class
							.toString()));
		}
		// crop envelope
		final ParameterValue envelopeParameter = parameters.parameter("Envelope");
		if (envelopeParameter == null || !(envelopeParameter.getValue() instanceof Envelope))
			throw new CannotCropException(Errors.format(ErrorKeys.NULL_PARAMETER_$2, "Envelope",GeneralEnvelope.class.toString()));
		// should we conserve the crop envelope
		final ParameterValue conserveEnvelopeParameter = parameters.parameter("ConserveEnvelope");
		if (conserveEnvelopeParameter == null|| !(conserveEnvelopeParameter.getValue() instanceof Boolean))
			throw new CannotCropException(Errors.format(ErrorKeys.NULL_PARAMETER_$2, "ConserveEnvelope",Double.class.toString()));
		// /////////////////////////////////////////////////////////////////////
		//
		// Initialization
		//
		// We take the crop envelope and the source envelope then we check their
		// crs and we also check if they ever overlap.
		//
		// /////////////////////////////////////////////////////////////////////
		final GridCoverage2D source = (GridCoverage2D) sourceParameter
				.getValue();
		// envelope of the source coverage
		final Envelope2D sourceEnvelope = source.getEnvelope2D();
		// crop envelope
		Envelope2D destinationEnvelope = new Envelope2D(
				(Envelope) envelopeParameter.getValue());
		CoordinateReferenceSystem sourceCRS, destinationCRS;
		sourceCRS = sourceEnvelope.getCoordinateReferenceSystem();
		destinationCRS = destinationEnvelope.getCoordinateReferenceSystem();
		if (destinationCRS == null) {
			// Do not change the user provided object - clone it first.
			final Envelope2D ge = new Envelope2D(destinationEnvelope);
			destinationCRS = source.getCoordinateReferenceSystem2D();
			ge.setCoordinateReferenceSystem(destinationCRS);
			destinationEnvelope = ge;
		}

		// //
		//
		// Source and destination crs must to be equals
		//
		// //
		if (!CRS.equalsIgnoreMetadata(sourceCRS, destinationCRS)) {
			throw new CannotCropException(Errors.format(ErrorKeys.MISMATCHED_ENVELOPE_CRS_$2, sourceCRS.getName().getCode(), destinationCRS.getName().getCode()));
		}
		// //
		//
		// Check the intersection and, if needed, do the crop operation.
		//
		// //
		final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope((Envelope) destinationEnvelope);
		intersectionEnvelope.setCoordinateReferenceSystem(source.getCoordinateReferenceSystem());
		// intersect the envelopes
		intersectionEnvelope.intersect(sourceEnvelope);
		if (intersectionEnvelope.isEmpty())
			throw new CannotCropException(Errors.format(ErrorKeys.CANT_CROP));
		// //
		//
		// Get the grid-to-world transform by keeping into account translation
		// of grid geometry constructor for respecting OGC PIXEL-IS-CENTER
		// ImageDatum assumption.
		//
		// //
		final AffineTransform sourceGridToWorld = (AffineTransform) ((GridGeometry2D) source.getGridGeometry()).getGridToCRS2D(PixelOrientation.UPPER_LEFT);
		// //
		//
		// I set the tolerance as half the scale factor of the grid-to-world
		// transform. This should more or less means in most cases "don't bother
		// to crop if the new envelope is as close to the old one that we go
		// deep under pixel size."
		//
		// //
		final double tolerance = XAffineTransform.getScale(sourceGridToWorld);
		if (!intersectionEnvelope.equals(sourceEnvelope, tolerance / 2.0, false)) {
			envelopeParameter.setValue(intersectionEnvelope.clone());
			return CroppedCoverage2D.create(parameters,(hints instanceof Hints) ? (Hints) hints: new Hints(hints), source,sourceGridToWorld, tolerance);
		} else {
			// //
			//
			// Note that in case we don't crop at all, WE DO NOT UPDATE the
			// envelope. If we did we might end up doing multiple successive
			// crop without actually cropping the image but, still, we would
			// shrink the envelope each time. Just think about having a loop
			// that crops recursively the same coverage specifying each time an
			// envelope whose URC is only a a scale quarter close to the LLC of
			// the old one. We would never crop the raster but we would modify
			// the grid-to-world transform each time.
			//
			// //
			return source;
		}
	}

    /**
     * Function to calculate the area of a polygon, according to the algorithm
     * defined at http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * 
     * @param polyPoints
     *            array of points in the polygon
     * @return area of the polygon defined by pgPoints
     */
    static double area(Point2D[] polyPoints) {
    	int i, j, n = polyPoints.length;
    	double area = 0;
    
    	for (i = 0; i < n; i++) {
    		j = (i + 1) % n;
    		area += polyPoints[i].getX() * polyPoints[j].getY();
    		area -= polyPoints[j].getX() * polyPoints[i].getY();
    	}
    	area /= 2.0;
    	return (area);
    }

//    /**
//         * In order to conserve the original envelope we used for this request
//         * we have to slightly correct the original grid to world transform in
//         * order to take into account the fact that we snapped the underlying
//         * raster to the integer grid. This would involve a "scale and
//         * translate" transformation which we are here accounting for.
//         * 
//         * @param cornerGridToWorld
//         *                original grid to world transform referred to the
//         *                corner of the cells in raster space.
//         * @param minX minimum x coordinate for the cropped raster in integer raster space.
//         * @param minY minimum y coordinate for the cropped raster in integer raster space.
//         * @param width width for the cropped raster in integer raster space.
//         * @param height height for the cropped raster in integer raster space.
//         * @param floatingPointRange range of the cropped raster in floating point raster space.
//         * @return a {@link MathTransform} which conserves the original envelope.
//         */
//        static MathTransform createCorrectedTranform(
//            final AffineTransform cornerGridToWorld, 
//            final double minX, 
//            final double minY,
//            final double width,
//            final double height,
//            final Rectangle2D floatingPointRange) {
//            
//            // computing the factor for the corrections affine transform to map
//            // from the integer raster space to the floating point raster space.
//            final double scaleX=floatingPointRange.getWidth()/width;
//            final double scaleY=floatingPointRange.getHeight()/height;
//            final double tx=floatingPointRange.getMinX()-minX*scaleX;
//            final double ty=floatingPointRange.getMinY()-minY*scaleY;
//            final AffineTransform translationTransform= new AffineTransform(scaleX,0,0,scaleY,tx,ty);
//            
//            //now correct the original grid to world transform
//            translationTransform.preConcatenate(cornerGridToWorld);
//            return ProjectiveTransform.create(translationTransform);
//    
//    }
}
