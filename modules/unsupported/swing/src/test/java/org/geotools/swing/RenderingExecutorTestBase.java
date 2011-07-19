/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public abstract class RenderingExecutorTestBase {

    protected static final ReferencedEnvelope WORLD = 
            new ReferencedEnvelope(150, 152, -33, -35, DefaultGeographicCRS.WGS84);
    
    protected static final Rectangle PANE = new Rectangle(200, 150);
    
    protected static final long WAIT_TIMEOUT = 500;
    
    protected RenderingExecutor executor;
    protected Graphics2D graphics;
    protected BufferedImage image;
    protected WaitingListener listener;
    protected MapContent mapContent;
    protected MockRenderer renderer;
    
    protected void setup() {
        executor = new SingleTaskRenderingExecutor();
        listener = new WaitingListener();
        mapContent = new MapContent();
        mapContent.getViewport().setBounds(WORLD);
        mapContent.getViewport().setScreenArea(PANE);
    }

    protected void createSubmitObjects() {
        image = new BufferedImage(SingleTaskRenderingExecutorTest.PANE.width, SingleTaskRenderingExecutorTest.PANE.height, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        renderer = new MockRenderer(mapContent);
    }

}
