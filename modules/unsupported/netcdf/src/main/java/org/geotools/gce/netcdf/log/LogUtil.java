/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
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
