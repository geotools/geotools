/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.utils.progress;

import java.util.EventObject;

/**
 * @author Simone Giannecchini, GeoSolutions.
 * 
 *
 *
 * @source $URL$
 */
public class ProcessingEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6930580659705360225L;

	private String message = null;

	private double percentage = 0;

	/**
	 * @param source
	 */
	public ProcessingEvent(final Object source, final String message,
			final double percentage) {
		super(source);
		this.message = message;
		this.percentage = percentage;
	}

	public double getPercentage() {
		return percentage;
	}
    
    public String getMessage() {
        return message;
    }

}
