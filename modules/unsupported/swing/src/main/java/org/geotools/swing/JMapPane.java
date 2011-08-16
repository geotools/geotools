/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.Map;

import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.renderer.lite.StreamingRenderer;

/**
 * A lightweight map pane which uses a single renderer and backing image.
 * Used by {@linkplain JMapFrame} for the GeoTools tutorial applications.
 *
 * @author Michael Bedward
 * @author Ian Turton
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class JMapPane extends AbstractMapPane {

    protected GTRenderer renderer;

    protected BufferedImage baseImage;
    protected Graphics2D baseImageGraphics;
    
    /**
     * Creates a new map pane. 
     */
    public JMapPane() {
        this(null, null);
    }

    /**
     * Creates a new map pane with the given renderer and map content.
     * Either or both of {@code renderer} and {@code content} may be
     * {@code null} when the {@link #setRenderer(GTRenderer)} and 
     * {@link #setMapContent(MapContent)} methods are to be called 
     * subsequently.
     *
     * @param renderer the renderer to use for drawing layers
     * @param content the {@code MapContent} instance containing layers to 
     *     display
     */
    public JMapPane(GTRenderer renderer, MapContent content) {
        this(renderer, content, null);
    }
    
    /**
     * Creates a new map pane with the given renderer and map content.
     * Either or both of {@code renderer} and {@code content} may be
     * {@code null} when the {@link #setRenderer(GTRenderer)} and 
     * {@link #setMapContent(MapContent)} methods are to be called 
     * subsequently. If {@code executor} is {@code null}, a default
     * rendering executor (an instance of {@linkplain DefaultRenderingExecutor})
     * will be set.
     *
     * @param renderer the renderer to use for drawing layers
     * @param content the {@code MapContent} instance containing layers to 
     *     display
     * @param executor the rendering executor
     */
    public JMapPane(GTRenderer renderer, MapContent content, RenderingExecutor executor) {
        super(content, executor);
        doSetRenderer(renderer);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapContent(MapContent content) {
        super.setMapContent(content);
        if (content != null && renderer != null) {
            renderer.setMapContent(mapContent);
        }
    }

    /**
     * Gets the renderer used by this map pane.
     *
     * @return the renderer
     */
    public GTRenderer getRenderer() {
        return renderer;
    }

    /**
     * Sets the renderer to be used by this map pane.
     *
     * @param renderer the renderer to use
     */
    public void setRenderer(GTRenderer renderer) {
        doSetRenderer(renderer);
    }

    private void doSetRenderer(GTRenderer newRenderer) {
        if (newRenderer != null) {
            Map<Object, Object> hints = newRenderer.getRendererHints();
            if (hints == null) {
                hints = new HashMap<Object, Object>();
            }
            
            if (newRenderer instanceof StreamingRenderer) {
                if (hints.containsKey(StreamingRenderer.LABEL_CACHE_KEY)) {
                    labelCache = (LabelCache) hints.get(StreamingRenderer.LABEL_CACHE_KEY);
                } else {
                    labelCache = new LabelCacheImpl();
                    hints.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
                }
            }
            
            newRenderer.setRendererHints(hints);

            if (mapContent != null) {
                newRenderer.setMapContent(mapContent);
            }
        }
        
        renderer = newRenderer;
    }
    
    /**
     * Retrieve the map pane's current base image.
     * <p>
     * The map pane caches the most recent rendering of map layers
     * as an image to avoid time-consuming rendering requests whenever
     * possible. The base image will be re-drawn whenever there is a
     * change to map layer data, style or visibility; and it will be
     * replaced by a new image when the pane is resized.
     * <p>
     * This method returns a <b>live</b> reference to the current
     * base image. Use with caution.
     *
     * @return a live reference to the current base image
     */
    public RenderedImage getBaseImage() {
        return this.baseImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (baseImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(baseImage, imageOrigin.x, imageOrigin.y, null);
            return;
        }
    }

    @Override
    protected void drawLayers(boolean createNewImage) {
        if (mapContent != null
                && !mapContent.getViewport().isEmpty()
                && acceptRepaintRequests.get()) {
            
            Rectangle r = getVisibleRect();
            if (baseImage == null || createNewImage) {
                baseImage = GraphicsEnvironment.getLocalGraphicsEnvironment().
                        getDefaultScreenDevice().getDefaultConfiguration().
                        createCompatibleImage(r.width, r.height, Transparency.TRANSLUCENT);

                if (baseImageGraphics != null) {
                    baseImageGraphics.dispose();
                }

                baseImageGraphics = baseImage.createGraphics();
                clearLabelCache.set(true);
                
            } else {
                baseImageGraphics.setBackground(getBackground());
                baseImageGraphics.clearRect(0, 0, r.width, r.height);
            }

            if (renderer != null && mapContent != null && !mapContent.layers().isEmpty()) {
                renderingExecutor.submit(mapContent, renderer, baseImageGraphics, this);
            }
        }
    }

}
