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

/**
 * Event launched when an exception occurrs. Percentage and message may be missing, in this case
 * they will be -1 and the exception message (localized if available, standard otherwise)
 * 
 * @author aaime, TOPP.
 * 
 *
 * @source $URL$
 */
public class ExceptionEvent extends ProcessingEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2272452028229922551L;
	private Throwable exception;

    public ExceptionEvent(Object source, String message, double percentage, Throwable exception) {
        super(source, message, percentage);
        this.exception = exception;
    }

    public ExceptionEvent(Object source, Throwable exception) {
        super(source, getMessageFromException(exception), -1);
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }
    
    static String getMessageFromException(Throwable exception) {
        if(exception.getLocalizedMessage() != null)
            return exception.getLocalizedMessage();
        else
            return exception.getMessage();
    }

}
