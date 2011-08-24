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
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A rendering task used to pass a {@code MapContent} to a renderer.
 * It is run on a background thread by a {@code RenderingExecutor}, 
 * <p>
 * Note: this class does not handle {@linkplain DirectLayer}, which paints
 * itself rather than using a renderer.
 * 
 * @see DirectRenderingTask
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class DefaultRenderingTask implements RenderingTask, RenderListener {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");
    
    private final Graphics2D destinationGraphics;
    private final Rectangle deviceArea;
    private final ReferencedEnvelope worldArea;
    private final AffineTransform worldToScreenTransform;
    private final GTRenderer renderer;

    private final AtomicBoolean running;
    private final AtomicBoolean failed;
    private final AtomicBoolean cancelled;
    
    /**
     * Creates a new rendering task to paint the layers contained within
     * {@code mapContent} into a single graphics destination using the
     * given renderer.
     * <p>
     * If {@code mapContent} has not already been linked to {@code renderer}
     * via a call to {@linkplain GTRenderer#setMapContent(MapContent)}, it will
     * be set here. If the renderer has a different, non-null {@code MapContent}
     * set, it will be replaced and a warning message logged.
     * 
     * @param mapContent map content containing the layers to be rendered
     * @param graphics the common graphics destination for the rendered layers
     * @param renderer the renderer used to paint the layers
     * 
     * @throws IllegalArgumentException if any argument is {@code null}
     *     
     */
    public DefaultRenderingTask(MapContent mapContent, 
            Graphics2D destinationGraphics,
            GTRenderer renderer) {
        
        if (mapContent == null) {
            throw new IllegalArgumentException("mapContent must not be null");
        }
        if (destinationGraphics == null) {
            throw new IllegalArgumentException("graphics must not be null");
        }
        if (renderer == null) {
            throw new IllegalArgumentException("renderer must not be null");
        }
        
        if (renderer.getMapContent() != mapContent) {
            if (!(renderer.getMapContent() == null)) {
                LOGGER.warning(
                        "Forcing map content for renderer passed to DefaultRenderingTask");
            }
            renderer.setMapContent(mapContent);
        }
        
        this.destinationGraphics = destinationGraphics;
        this.deviceArea = mapContent.getViewport().getScreenArea();
        this.worldArea = mapContent.getViewport().getBounds();
        this.worldToScreenTransform = mapContent.getViewport().getWorldToScreen();
        this.renderer = renderer;
        
        running = new AtomicBoolean(false);
        failed = new AtomicBoolean(false);
        cancelled = new AtomicBoolean(false);
    }
    
    /**
     * Cancels this rendering task. May be called prior to the rendering
     * task starting.
     */
    @Override
    public void cancel() {
        if (running.get()) {
            renderer.stopRendering();
        }
        
        cancelled.set(true);
    }

    /**
     * Called by the {@code RenderingExecutor} to run this task.
     *
     * @return {@code true} if the task was completed successfully;
     *     {@code false} otherwise
     * 
     * @throws Exception
     */
    @Override
    public Boolean call() {
        if (!cancelled.get()) {
            try {
                renderer.addRenderListener(this);
                running.set(true);
                renderer.paint(destinationGraphics,
                        deviceArea,
                        worldArea,
                        worldToScreenTransform);
            } finally {
                renderer.removeRenderListener(this);
                running.set(false);
            }
        }

        return !(isFailed() || isCancelled());
    }

    /**
     * Called by the renderer when each feature is drawn. This
     * implementation does nothing.
     *
     * @param feature the feature just drawn
     */
    @Override
    public void featureRenderer(SimpleFeature feature) {}

    /**
     * Called by the renderer on error
     *
     * @param e cause of the error
     */
    @Override
    public void errorOccurred(Exception e) {
        running.set(false);
        failed.set(true);
    }
    
    @Override
    public boolean isRunning() {
        return running.get();
    }
    
    public boolean isFailed() {
        return failed.get();
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }

    /**
     * Creates a new map content by copying the given instance, skipping 
     * any {@code DirectLayers}.
     * 
     * @param mapContent input map content
     * @return the new instance
     */
    private MapContent copyMapContent(MapContent mapContent) {
        MapContent mc = new MapContent();
        
        MapViewport vp = new MapViewport(mapContent.getViewport());
        vp.setEditable(false);
        mc.setViewport(vp);
        
        for (Layer layer : mapContent.layers()) {
            if (!(layer instanceof DirectLayer)) {
                mc.addLayer(layer);
            }
        }
        
        return mc;
    }
    
}
