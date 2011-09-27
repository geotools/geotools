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

package org.geotools.swing.testutils;

import org.geotools.swing.RenderingExecutorEvent;
import org.geotools.swing.RenderingExecutorListener;


/**
 * A RenderingExecutorListener that can be set to expect specified events
 * and test if they are received.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class WaitingRenderingExecutorListener 
        extends WaitingListener<RenderingExecutorEvent, WaitingRenderingExecutorListener.Type> 
        implements RenderingExecutorListener {

    public static enum Type {
        STARTED,
        COMPLETED,
        FAILED;
    }
    
    public WaitingRenderingExecutorListener() {
        super(Type.values().length);
    }
    
    @Override
    public void onRenderingStarted(RenderingExecutorEvent ev) {
        catchEvent(Type.STARTED.ordinal(), ev);
    }

    @Override
    public void onRenderingCompleted(RenderingExecutorEvent ev) {
        catchEvent(Type.COMPLETED.ordinal(), ev);
    }

    @Override
    public void onRenderingFailed(RenderingExecutorEvent ev) {
        catchEvent(Type.FAILED.ordinal(), ev);
    }
    
}
