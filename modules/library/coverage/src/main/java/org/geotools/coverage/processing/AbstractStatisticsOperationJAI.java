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
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.ROIShape;
import javax.media.jai.StatisticsOpImage;
import javax.media.jai.registry.RenderedRegistryMode;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.ImagingParameterDescriptors;
import org.geotools.parameter.ImagingParameters;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.logging.Logging;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This class is the root class for the Statistics operations based on
 * {@link JAI}'s {@link StatisticsOpImage} like Extrema and Histogram. It
 * provides basic capabilities for management of geospatial parameters like
 * {@link javax.media.jai.ROI}s and subsampling factors.
 * 
 * @author Simone Giannecchini
 * @since 2.4.x
 * 
 *
 *
 * @source $URL$
 */
public abstract class AbstractStatisticsOperationJAI extends
		OperationJAI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6830028735162290160L;

	/** {@link Logger} for this class. */
	public final static Logger LOGGER = Logging.getLogger("org.geotools.coverage.processing");

	/**
	 * The parameter descriptor for the SPATIAL_SUBSAMPLING_X
	 */
	public static final ParameterDescriptor<Double> SPATIAL_SUBSAMPLING_X = new DefaultParameterDescriptor<Double>(
			Citations.JAI, "xPeriod", Double.class, // Value class (mandatory)
			null, // Array of valid values
			null, // Default value
			null, // Minimal value
			null, // Maximal value
			null, // Unit of measure
			true);

	/**
	 * The parameter descriptor for the SPATIAL_SUBSAMPLING_Y
	 */
	public static final ParameterDescriptor<Double> SPATIAL_SUBSAMPLING_Y = new DefaultParameterDescriptor<Double>(
			Citations.JAI, "yPeriod", Double.class, // Value class (mandatory)
			null, // Array of valid values
			null, // Default value
			null, // Minimal value
			null, // Maximal value
			null, // Unit of measure
			true);

	/**
	 * The parameter descriptor for the Region Of Interest.
	 */
	public static final ParameterDescriptor<Polygon> ROI = new DefaultParameterDescriptor<Polygon>(
			Citations.JAI, "roi", Polygon.class, // Value class (mandatory)
			null, // Array of valid values
			null, // Default value
			null, // Minimal value
			null, // Maximal value
			null, // Unit of measure
			true);

	private static Set<ParameterDescriptor> REPLACED_DESCRIPTORS;

	static {
		final Set<ParameterDescriptor> replacedDescriptors = new HashSet<ParameterDescriptor>();
		replacedDescriptors.add( SPATIAL_SUBSAMPLING_X);
		replacedDescriptors.add( SPATIAL_SUBSAMPLING_Y);
		replacedDescriptors.add( ROI);
		REPLACED_DESCRIPTORS = Collections.unmodifiableSet(replacedDescriptors);
	}

	/**
	 * Constructor for {@link AbstractStatisticsOperationJAI}.
	 * 
	 * @param operationDescriptor
	 *            {@link OperationDescriptor} for the underlying JAI operation.
	 */
	public AbstractStatisticsOperationJAI(OperationDescriptor operationDescriptor) {
		super(operationDescriptor, new ImagingParameterDescriptors(
				getOperationDescriptor(operationDescriptor.getName()),
				REPLACED_DESCRIPTORS));
	}

	/**
	 * Constructor for {@link AbstractStatisticsOperationJAI}.
	 * 
	 * @param operationDescriptor
	 *            {@link OperationDescriptor} for the underlying JAI operation.
	 * @param replacements
	 *            {@link ImagingParameterDescriptors} that should replace the
	 *            correspondent {@link ImagingParameters} in order to change the
	 *            default behavior they have inside JAI.
	 */
	public AbstractStatisticsOperationJAI(
			OperationDescriptor operationDescriptor,
			ImagingParameterDescriptors replacements) {
		super(operationDescriptor, new ImagingParameterDescriptors(
				ImagingParameterDescriptors.properties(operationDescriptor),
				operationDescriptor, RenderedRegistryMode.MODE_NAME,
				ImagingParameterDescriptors.DEFAULT_SOURCE_TYPE_MAP, REPLACED_DESCRIPTORS));
	}
    /**
	 * Constructor for {@link AbstractStatisticsOperationJAI}.
	 * @param name of the underlying JAI operation.
	 */
	public AbstractStatisticsOperationJAI(String name) {
		super(getOperationDescriptor(name),new ImagingParameterDescriptors(
				getOperationDescriptor(name),
				new HashSet<ParameterDescriptor>(REPLACED_DESCRIPTORS)));
	}

	/**
     * Copies parameter values from the specified {@link ParameterValueGroup} to the
     * {@link ParameterBlockJAI}
     *
     * @param parameters
     *            The {@link ParameterValueGroup} to be copied.
     * @return A copy of the provided {@link ParameterValueGroup} as a JAI block.
     *
     * @see org.geotools.coverage.processing.OperationJAI#prepareParameters(org.opengis.parameter.ParameterValueGroup)
	 */
	protected ParameterBlockJAI prepareParameters(ParameterValueGroup parameters) {
		// /////////////////////////////////////////////////////////////////////
		//
		// Make a copy of the input parameters.
		//
		// ///////////////////////////////////////////////////////////////////
		final ImagingParameters copy = (ImagingParameters) descriptor
				.createValue();
		final ParameterBlockJAI block = (ParameterBlockJAI) copy.parameters;
		try {

			// /////////////////////////////////////////////////////////////////////
			//
			//
			// Now transcode the parameters as needed by this operation.
			//
			//
			// ///////////////////////////////////////////////////////////////////
			// XXX make it robust
			final GridCoverage2D source = (GridCoverage2D) parameters
					.parameter(operation.getSourceNames()[PRIMARY_SOURCE_INDEX])
					.getValue();
			final AffineTransform gridToWorldTransformCorrected = new AffineTransform(
					(AffineTransform) ((GridGeometry2D) source
							.getGridGeometry())
							.getGridToCRS2D(PixelOrientation.UPPER_LEFT));
			final MathTransform worldToGridTransform;
			try {
				worldToGridTransform = ProjectiveTransform
						.create(gridToWorldTransformCorrected.createInverse());
			} catch (NoninvertibleTransformException e) {
				// //
				//
				// Something bad happened here, namely the transformation to go
				// from grid to world was not invertible. Let's wrap and
				// propagate the error.
				//
				// //
				final CoverageProcessingException ce = new CoverageProcessingException(
						e);
				throw ce;
			}
			
			// //
			//
			// get the original envelope and the crs
			//
			// //
			final CoordinateReferenceSystem crs = source
					.getCoordinateReferenceSystem2D();
			final Envelope2D envelope = source.getEnvelope2D();			

			// /////////////////////////////////////////////////////////////////////
			//
			// Transcode the xPeriod and yPeriod parameters by applying the
			// WorldToGrid transformation for the source coverage.
			//
			// I am assuming that the supplied values are in the same CRS as the 
			// source coverage. We here apply
			//
			// /////////////////////////////////////////////////////////////////////
			final double xPeriod = parameters.parameter("xPeriod")
					.doubleValue();
			final double yPeriod = parameters.parameter("yPeriod")
					.doubleValue();
			if(!Double.isNaN(xPeriod)&&!Double.isNaN(yPeriod)){

				// build the new one that spans over the requested area
				// NOTE:
				final DirectPosition2D LLC = new DirectPosition2D(crs, envelope.x,
						envelope.y);
				LLC.setCoordinateReferenceSystem(crs);
				final DirectPosition2D URC = new DirectPosition2D(crs, envelope.x
						+ xPeriod, envelope.y + yPeriod);
				URC.setCoordinateReferenceSystem(crs);
				final Envelope2D shrinkedEnvelope = new Envelope2D(LLC, URC);
	
				// transform back into raster space
				final Rectangle2D transformedEnv = CRS.transform(
						worldToGridTransform, shrinkedEnvelope).toRectangle2D();
	
				// block settings
				block.setParameter("xPeriod", Integer.valueOf((int) transformedEnv
						.getWidth()));
				block.setParameter("yPeriod", Integer.valueOf((int) transformedEnv
						.getHeight()));
			}
			// /////////////////////////////////////////////////////////////////////
			//
			// Transcode the polygon parameter into a roi.
			//
			// I am assuming that the supplied values are in the same
			// CRS as the source coverage. We here apply
			//
			// /////////////////////////////////////////////////////////////////////
			final Object o = parameters.parameter("roi").getValue();
			if (o != null && o instanceof Polygon) {
				final Polygon roiInput = (Polygon) o;
				if (new ReferencedEnvelope(roiInput.getEnvelopeInternal(),
						source.getCoordinateReferenceSystem2D())
						.intersects((Envelope) new ReferencedEnvelope(envelope))) {
					final java.awt.Polygon shapePolygon = convertPolygon(
							roiInput, worldToGridTransform);

					block.setParameter("roi", new ROIShape(shapePolygon));
				}
			}
			return block;
		} catch (Exception e) {
			// //
			//
			// Something bad happened here Let's wrap and propagate the error.
			//
			// //
			final CoverageProcessingException ce = new CoverageProcessingException(
					e);
			throw ce;
		}
	}

	/**
	 * Converte a JTS {@link Polygon}, which represents a ROI, into an AWT
	 * {@link java.awt.Polygon} by means of the provided {@link MathTransform}.
	 * 
	 * @param roiInput
	 *            the input ROI as a JTS {@link Polygon}.
	 * @param worldToGridTransform
	 *            the {@link MathTransform} to apply to the input ROI.
	 * @return an AWT {@link java.awt.Polygon}.
	 * @throws TransformException
	 *             in case the provided {@link MathTransform} chokes.
	 */
	private static java.awt.Polygon convertPolygon(final Polygon roiInput,
			MathTransform worldToGridTransform) throws TransformException {
		final boolean isIdentity = worldToGridTransform.isIdentity();
		final java.awt.Polygon retValue = new java.awt.Polygon();
		final double coords[] = new double[2];
		final LineString exteriorRing = roiInput.getExteriorRing();
		final CoordinateSequence exteriorRingCS = exteriorRing
				.getCoordinateSequence();
		final int numCoords = exteriorRingCS.size();
		for (int i = 0; i < numCoords; i++) {
			// get the actual coord
			coords[0] = exteriorRingCS.getX(i);
			coords[1] = exteriorRingCS.getY(i);

			// transform it
			if (!isIdentity)
				worldToGridTransform.transform(coords, 0, coords, 0, 1);

			// send it back to the returned polygon
			retValue.addPoint((int) (coords[0] + 0.5d),
					(int) (coords[1] + 0.5d));

		}

		// return the created polygon.
		return retValue;
	}
}
