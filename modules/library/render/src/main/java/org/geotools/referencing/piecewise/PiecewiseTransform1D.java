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
package org.geotools.referencing.piecewise;

import org.opengis.referencing.operation.MathTransform1D;

/**
 * The {@link PiecewiseTransform1D} interface extends the {@link Domain1D}
 * adding transformation capabilities to it.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * 
 *
 *
 * @source $URL$
 */
public interface PiecewiseTransform1D<T extends PiecewiseTransform1DElement> extends Domain1D<T>, MathTransform1D {

	/**
	 * Indicates whether or not this {@link PiecewiseTransform1D} has a default value which
	 * will be returned when asked to transform a value outside the valid domain elements.
	 * 
	 * @return a <code>boolean</code> to indicate whether or not this {@link PiecewiseTransform1D} has a default value.
	 */
	public boolean hasDefaultValue() ;


	/**
	 * The default value which will be returned when asked to transform a value outside the valid domain elements.
	 * 
	 * <p>
	 * In case {@link #hasDefaultValue()} return <code>false</code> this value has no meaning.
	 * 
	 * @return The default value which will be returned when asked to transform a value outside the valid domain elements.
	 */
	public double getDefaultValue() ;
}
