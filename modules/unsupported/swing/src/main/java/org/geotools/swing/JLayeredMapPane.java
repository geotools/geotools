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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.renderer.lite.SynchronizedLabelCache;

/**
 *
 *
 * @source $URL$
 */
public class JLayeredMapPane extends AbstractMapPane {

    private static class LayerOperands {
        BufferedImage image;
        Graphics2D graphics;
        GTRenderer renderer;
    }

    private final Map<Layer, LayerOperands> operandLookup;
    private final Map<Object, Object> renderingHints;
    
    
    public JLayeredMapPane() {
        this(null);
    }
    
    public JLayeredMapPane(MapContent content) {
        this(content, null);
    }

    public JLayeredMapPane(MapContent content, RenderingExecutor executor) {
        super(content, executor);
        operandLookup = new HashMap<Layer, LayerOperands>();
        labelCache = new SynchronizedLabelCache();
        
        renderingHints = new HashMap<Object, Object>();
        renderingHints.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
    }

    @Override
    protected void drawLayers(boolean recreate) {
        drawingLock.lock();
        try {
            if (mapContent != null
                    && !mapContent.layers().isEmpty()
                    && !mapContent.getViewport().isEmpty()
                    && acceptRepaintRequests.get()) {

                getRenderingExecutor().submit(mapContent, getOperands(recreate), this);
            }
        } finally {
            drawingLock.unlock();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (drawingLock.tryLock()) {
            try {
                if (mapContent != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    for (Layer layer : mapContent.layers()) {
                        LayerOperands op = operandLookup.get(layer);
                        if (op != null) {
                            g2.drawImage(op.image, imageOrigin.x, imageOrigin.y, null);
                        }
                    }
                }
            } finally {
                drawingLock.unlock();
            }
        }

    }
    
    private List<RenderingOperands> getOperands(boolean recreate) {
        List<RenderingOperands> ops = new ArrayList<RenderingOperands>();
        Rectangle r = getVisibleRect();
        
        for (Layer layer : mapContent.layers()) {
            ops.add(getRenderingOperands(layer, r, recreate));
        }
        
        return ops;
    }

    private RenderingOperands getRenderingOperands(Layer layer, Rectangle r, boolean recreate) {
        LayerOperands op = operandLookup.get(layer);
        if (op == null) {
            op = new LayerOperands();
            operandLookup.put(layer, op);
        }
        
        if (op.image == null || recreate) {
            op.image = GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice().getDefaultConfiguration().
                    createCompatibleImage(r.width, r.height, Transparency.TRANSLUCENT);

            if (op.graphics != null) {
                op.graphics.dispose();
            }

            op.graphics = op.image.createGraphics();
            // op.graphics.setBackground(getBackground());

        } else {
            // op.graphics.setBackground(getBackground());
            // op.graphics.clearRect(0, 0, r.width, r.height);
        }
        
        if (op.renderer == null) {
            op.renderer = new StreamingRenderer();
            op.renderer.setRendererHints(renderingHints);
        }
        
        return new RenderingOperands(layer, op.graphics, op.renderer);
    }
    
}
