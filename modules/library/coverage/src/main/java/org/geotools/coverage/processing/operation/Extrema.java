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
package org.geotools.coverage.processing.operation;

import java.awt.Shape;
import java.awt.image.RenderedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ExtremaDescriptor;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.AbstractStatisticsOperationJAI;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * This operation simply wraps JAI Extrema operations described by
 * {@link ExtremaDescriptor} inside a GeoTools operation in order to make it
 * spatial-aware.
 * 
 * <p>
 * For the moment this is a very simple wrap. Plans on the 2.4 and successive
 * versions of this operation are to add the ability to use spatial ROIs and
 * to specific Spatial subsampling. As of now, ROI has to be a Java2D
 * {@link Shape} subclass and the parameters to control x and y subsamplings got
 * to be Integer, which means pixel-aware.
 * 
 * <p>
 * For more information on how the underlying {@link JAI} operators works you
 * can have a look here: <a
 * href="http://download.java.net/media/jai/javadoc/1.1.3/jai-apidocs/javax/media/jai/operator/ExtremaDescriptor.html">ExtremaDescriptor</a>
 * 
 * <p>
 * <strong>How to use this operation</strong> Here is a very simple example on
 * how to use this operation in order to the minimum and maximum of the source
 * coverage.
 * 
 * <code>
 * final OperationJAI op=new OperationJAI("Extrema");
 * ParameterValueGroup params = op.getParameters();
 * params.parameter("Source").setValue(coverage);
 * coverage=(GridCoverage2D) op.doOperation(params,null);
 * System.out.println(((double[])coverage.getProperty("minimum"))[0]);
 * System.out.println(((double[])coverage.getProperty("minimum"))[1]);
 * System.out.println(((double[])coverage.getProperty("minimum"))[2]);
 * System.out.println(((double[])coverage.getProperty("maximum"))[0]);
 * System.out.println(((double[])coverage.getProperty("maximum"))[1]);
 * System.out.println(((double[])coverage.getProperty("maximum"))[2]);
 * </code>
 * 
 * @author Simone Giannecchini
 * @since 2.4
 * 
 *
 *
 * @source $URL$
 */
public class Extrema extends AbstractStatisticsOperationJAI {

	/**
	 * Serial number for interoperability with different versions.
	 */
	private static final long serialVersionUID = 7731039381590398047L;

	/** {@link Logger} for this class. */
	public final static Logger LOGGER = Logging.getLogger("org.geotools.coverage.processing.operation");

	/** {@link String} key for getting the minimum vector. */
	public final static String GT_SYNTHETIC_PROPERTY_MINIMUM = "minimum";

	/** {@link String} key for getting the maximum vector. */
	public final static String GT_SYNTHETIC_PROPERTY_MAXIMUM = "maximum";
	/**Locations of min values. */
	public final static String GT_SYNTHETIC_PROPERTY_MIN_LOCATIONS="minLocations";
	
	/**Locations of max values. */
	public final static String GT_SYNTHETIC_PROPERTY_MAX_LOCATIONS="maxLocations";


	/**
	 * Constructs a default {@code "Extrema"} operation.
	 */
	public Extrema() throws OperationNotFoundException {
		super(getOperationDescriptor("Extrema"));

	}

	/**
	 * This operation MUST be performed on the geophysics data for this
	 * {@link GridCoverage2D}.
	 * 
	 * @param parameters
	 *            {@link ParameterValueGroup} that describes this operation
	 * @return always true.
	 */
	protected boolean computeOnGeophysicsValues(ParameterValueGroup parameters) {
		return true;
	}

	/**
	 * Prepare the minimum and maximum properties for this extream operation.
	 * 
	 * <p>
	 * See <a
	 * href="http://download.java.net/media/jai/javadoc/1.1.3/jai-apidocs/javax/media/jai/operator/ExtremaDescriptor.html">ExtremaDescriptor</a>
	 * for more info.
	 * 
	 * @see OperationJAI#getProperties(RenderedImage, CoordinateReferenceSystem,
	 *      InternationalString, MathTransform, GridCoverage2D[],
	 *      org.geotools.coverage.processing.OperationJAI.Parameters),
	 */
	@SuppressWarnings("unchecked")
	protected Map<String,?> getProperties(RenderedImage data,
			CoordinateReferenceSystem crs, InternationalString name,
			MathTransform toCRS, GridCoverage2D[] sources, Parameters parameters) {
		// /////////////////////////////////////////////////////////////////////
		//
		// If and only if data is a RenderedOp we prepare the properties for
		// minimum and maximum as the output of the extrema operation.
		//
		// /////////////////////////////////////////////////////////////////////
		if (data instanceof RenderedOp) {
			final RenderedOp result = (RenderedOp) data;
			final Map<String, Object> synthProp = new HashMap<String, Object>();			

			// get the properties
			final double[] maximums = (double[]) result
					.getProperty(GT_SYNTHETIC_PROPERTY_MAXIMUM);
			final double[] minimums = (double[]) result
					.getProperty(GT_SYNTHETIC_PROPERTY_MINIMUM);
			Object property=result.getProperty(GT_SYNTHETIC_PROPERTY_MIN_LOCATIONS);
			if((property instanceof List[]))
				synthProp.put(GT_SYNTHETIC_PROPERTY_MIN_LOCATIONS, (List<int[]>[])property);
			property=result.getProperty(GT_SYNTHETIC_PROPERTY_MAX_LOCATIONS);
			if((property instanceof List[]))
				synthProp.put(GT_SYNTHETIC_PROPERTY_MAX_LOCATIONS, (List<int[]>[])property);

			// return the map
			synthProp.put(GT_SYNTHETIC_PROPERTY_MINIMUM, minimums);
			synthProp.put(GT_SYNTHETIC_PROPERTY_MAXIMUM, maximums);
			return Collections.unmodifiableMap(synthProp);

		}
		return super.getProperties(data, crs, name, toCRS, sources, parameters);
	}
}
