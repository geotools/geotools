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
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/**
 * An {@link Executor} that provides methods to manage termination and
 * methods that can produce a {@link Progress} for tracking one or more
 * asynchronous tasks.
 * <p>
 * Method <tt>submit</tt> extends base method {@link
 * ExecutorService#submit} by creating and returning a
 * {@link Progress} that can be used to track how a process
 * is doing in addition to cancelling execution and/or waiting
 * for completion.
 * 
 * @author Jody
 *
 *
 * @source $URL$
 */
public interface ProcessExecutor extends ExecutorService {
    /**
     * Submits a process for execution and returns a Progress
     * representing the pending results of the task. 
     *
     * <p>
     * If you would like to immediately block waiting
     * for a task, you can use constructions of the form
     * <tt>result = exec.submit(aProcess).get();</tt>
     *
     * <p> Note: The {@link Processors} class includes a set of methods
     * that can convert some other common closure-like objects,
     * for example, {@link Callable} to {@link Process} form so
     * they can be submitted.
     *
     * @param task the task to submit
     * @return a Progress representing pending completion of the task
     * @throws RejectedExecutionException if task cannot be scheduled for execution
     * @throws NullPointerException if task null
     */
    public Progress submit( Process task, Map<String,Object> input );    
}
