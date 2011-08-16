/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing;

import java.awt.Graphics2D;
import java.util.List;

import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;

/**
 * Defines the core methods for executors used to run drawing tasks on 
 * background threads.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public interface RenderingExecutor {
    
    /** 
     * Value returned by the {@linkplain #submit} method if a task is rejected
     * by the executor.
     */
    public static final long TASK_REJECTED = -1;
    
    /**
     * Get the interval for polling the result of a rendering task
     *
     * @return polling interval in milliseconds
     */
    long getPollingInterval();

    /**
     * Set the interval for polling the result of a rendering task
     *
     * @param interval interval in milliseconds (values {@code <=} 0 are ignored)
     */
    void setPollingInterval(long interval);

    /**
     * Submits a new rendering task to the executor. The specified listener
     * will be notified of task progress. If the task is rejected by the
     * executor for any reason, this method returns {@link #TASK_REJECTED}
     * instead of a task ID value.
     *
     * @param mapContent the map content holding the layers to be rendered
     * @param renderer the renderer to use
     * @param graphics the graphics object to draw into
     * @param listener the listener to be notified of task progress
     *
     * @return either a task ID value (which should be positive and unique across 
     *     all executors and rendering tasks) if the task was accepted; 
     *     or {@link #TASK_REJECTED}
     * 
     * 
     * @throws IllegalArgumentException if any arguments are {@code null}
     * @throws IllegalStateException if called after the executor has been shut down
     */
     long submit(MapContent mapContent, GTRenderer renderer, Graphics2D graphics,
             RenderingExecutorListener listener);
     
    /**
     * Submits a new rendering task to the executor. The specified listener
     * will be notified of task progress. If the task is rejected by the
     * executor for any reason, this method returns {@link #TASK_REJECTED}
     * instead of a task ID value.
     *
     * @param mapContent the map content holding the layers to be rendered
     * @param operands operands for each of the sub-tasks composing this task
     * @param listener the listener to be notified of task progress
     *
     * @return either a task ID value (which should be positive and unique across 
     *     all executors and rendering tasks) if the task was accepted; 
     *     or {@link #TASK_REJECTED}
     * 
     * 
     * @throws IllegalArgumentException if any arguments are {@code null}
     * @throws IllegalStateException if called after the executor has been shut down
     */
     long submit(MapContent mapContent, List<RenderingOperands> operands,
            RenderingExecutorListener listener);
     
     /**
      * Cancels a specific rendering task.
      * 
      * @param taskId the ID value of the task
      */
     void cancel(long taskId);
     
     void cancelAll();

     /**
      * Stops any current rendering tasks and cleans up resources. After calling
      * this method the executor is no longer usable.
      */
     void shutdown();
    
     /**
      * Checks whether {@link #shutdown()} has been called.
      * 
      * @return {@code true} if the executor has been shut down
      */
     boolean isShutdown();

}
