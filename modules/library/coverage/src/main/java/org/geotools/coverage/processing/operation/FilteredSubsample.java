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



import org.geotools.coverage.processing.BaseScaleOperationJAI;

/**
 * This operation is simply a wrapper for the JAI FilteredSubsample operation which allows
 * me to arbitrarly scale a rendered image while smoothing it out.
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3
 *
 * @see javax.media.jai.operator.FilteredSubsampleDescriptor
 */
public class FilteredSubsample extends BaseScaleOperationJAI {
	/**
	 * Serial number for cross-version compatibility.
	 */
	private static final long serialVersionUID = 652535074064952517L;

        /**
         * Constructs a default {@code "FilteredSubsample"} operation.
         */
	public FilteredSubsample() {
		super("FilteredSubsample");
	}
}	
