/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.netcdf.log;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {

    public static void logElapsedTime(Logger logger, Date beginDate,
	    String additionalMessage) {
	// element 0 is the stack trace itself, element 1 is this LogUtil,
	// element 2 is the caller.
	StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
	String className = ste.getClassName();
	String methodName = ste.getMethodName();
	long elapsedTime = new Date().getTime() - beginDate.getTime();

	StringBuffer logMsg = new StringBuffer();
	logMsg.append("{0}:{1} elapsed time(millis):{2}");

	if (additionalMessage != null) {
	    logMsg.append(" {3}");
	}

	Object[] logMsgObjects = new Object[] { className, methodName,
		elapsedTime, additionalMessage };

	// ok for our tests, logs w/o className and methodName on server.
	// logger.logp(Level.INFO, className, methodName, logMsg.toString());
	logger.log(Level.INFO, logMsg.toString(), logMsgObjects);
    }
}
