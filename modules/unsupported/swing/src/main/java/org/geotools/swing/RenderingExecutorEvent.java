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

import java.util.EventObject;

/**
 * An event type used by {@code RenderingExecutor} to communicate task
 * status to client objects.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class RenderingExecutorEvent extends EventObject {
    
    private final long taskId;

    /**
     * Creates a new event.
     * 
     * @param executor the executor publishing this event
     * 
     * @param taskId the rendering executor task ID
     */
    public RenderingExecutorEvent(RenderingExecutor executor, long taskId) {
        super(executor);
        this.taskId = taskId;
    }

    /**
     * Gets the rendering executor which published this event.
     * 
     * @return the source executor
     */
    @Override
    public RenderingExecutor getSource() {
        return (RenderingExecutor) super.getSource();
    }

    /**
     * Gets the integer ID of the rendering task associated with this event.
     * 
     * @return task ID
     */
    public long getTaskId() {
        return taskId;
    }
    
}
