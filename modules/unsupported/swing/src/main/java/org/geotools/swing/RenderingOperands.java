/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing;

import java.awt.Graphics2D;
import org.geotools.map.Layer;
import org.geotools.renderer.GTRenderer;

/**
 *
 * @author michael
 */
public class RenderingOperands {

    private final Layer layer;
    private final Graphics2D graphics;
    private final GTRenderer renderer;

    public RenderingOperands(Layer layer, Graphics2D graphics) {
        this(layer, graphics, null);
    }

    public RenderingOperands(Layer layer, Graphics2D graphics, GTRenderer renderer) {
        this.layer = layer;
        this.graphics = graphics;
        this.renderer = renderer;
    }

    public Layer getLayer() {
        return layer;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public GTRenderer getRenderer() {
        return renderer;
    }
}
