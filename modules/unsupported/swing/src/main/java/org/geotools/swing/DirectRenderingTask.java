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
import java.util.concurrent.atomic.AtomicBoolean;

import org.geotools.map.DirectLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

/**
 * A rendering task used to draw {@linkplain DirectLayer} instances.
 * It is run on a background thread by a {@code RenderingExecutor}, 
 * <p>
 * Note: this class does not handle other {@code Layer} classes which
 * must be passed to a renderer for drawing.
 * 
 * @see DirectRenderingTask
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/main/java/org/geotools/swing/RenderingTask.java $
 * @version $Id: DefaultRenderingTask.java 37862 2011-08-18 13:09:50Z mbedward $
 */
public class DirectRenderingTask implements RenderingTask {

    private final MapContent mapContent;
    private final MapViewport viewport;
    private final Graphics2D destinationGraphics;

    private final AtomicBoolean running;
    private final AtomicBoolean cancelled;
    
    public DirectRenderingTask(MapContent mapContent, Graphics2D graphics) {
        this(mapContent, mapContent.getViewport(), graphics);
    }
    
    public DirectRenderingTask(MapContent mapContent, MapViewport viewport, Graphics2D graphics) {
        this.mapContent = mapContent;
        this.viewport = viewport;
        this.destinationGraphics = graphics;
        this.running = new AtomicBoolean();
        this.cancelled = new AtomicBoolean();
    }

    @Override
    public Boolean call() throws Exception {
        if (!cancelled.get()) {
            try {
                running.set(true);
                for (Layer layer : mapContent.layers()) {
                    if (layer instanceof DirectLayer) {
                        DirectLayer dlayer = (DirectLayer) layer;
                        dlayer.draw(destinationGraphics, mapContent, viewport);
                    }
                }
            } finally {
                running.set(false);
            }
        }

        return !isCancelled();
    }

    @Override
    public void cancel() {
        cancelled.set(true);
    }

    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }
    
}
