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
package org.geotools.process;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.opengis.util.ProgressListener;

/**
 * A Process that returns a result and reports progress.
 * Implementors define a single method called <tt>execute</tt>
 * that accepts the inputs to process and a ProgressListener.
 *
 * <p>The <tt>Process</tt> interface is similar to {@link
 * java.lang.Callable}, in that both are designed for classes whose
 * instances are potentially executed by another thread.  A
 * <tt>Callable</tt>, however, does not report progress.
 *
 * <p> The {@link Executors} class contains utility methods to
 * convert from other common forms to <tt>Callable</tt> classes.
 *
 * @see Executor
 * 
/**
 * Used to process inputs and is reported using a ProgressListener.
 * Results are available after being run.
 *
 * @author gdavis
 *
 * @source $URL$
 */
public interface Process {
	/**
	 * Execute this process with the provided inputs.
	 * 
	 * @param input Input parameters for this process
	 * @param monitor listener for handling the progress of the process
	 * @return Map of results, (@see ProcessFactory for details), or null if canceled
	 */
	public Map<String,Object> execute(Map<String,Object> input, ProgressListener monitor)
        throws ProcessException;
}
