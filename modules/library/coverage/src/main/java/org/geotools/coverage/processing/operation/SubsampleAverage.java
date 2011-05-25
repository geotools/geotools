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
 * This operation is simply a wrapper for the JAI SubsampleAverage operation which allows
 * me to arbitrarily scale a rendered image while smoothing it out.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @version $Id: SubsampleAverage.java 23157 2006-12-01 01:29:53Z desruisseaSubsampleAveragedCoverageator.SubsampleAverageDescriptor
 * @since 2.3
 * @see javax.media.jai.operator.SubsampleAverageDescriptor
 *
 * @source $URL$
 */
public class SubsampleAverage extends BaseScaleOperationJAI {
	
       /**
        * Serial number for interoperability with different versions.
        */
	private static final long serialVersionUID = 1L;

       /**
        * Constructs a default {@code "SubsampleAverage"} operation.
        */
	public SubsampleAverage() {
		super("SubsampleAverage");
	}



}
