/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Map;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.locationtech.jts.geom.Envelope;

/**
 * GeoTools renderer for rendering spatial content into a Graphics2D. Typical usage:
 *
 * <pre>
 *          Rectangle paintArea = new Rectangle(width, height);
 *          ReferencedEnvelope mapArea = map.getBounds();
 *
 *          renderer = new StreamingRenderer();
 *          renderer.setContext(map);
 *
 *          RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
 *          renderer.setJava2DHints(hints);
 *
 *          Map<Object,Object> rendererParams = new HashMap<Object,Object>();
 *          rendererParams.put("optimizedDataLoadingEnabled",Boolean.valueOf(true) );
 *
 *          renderer.setRendererHints( renderParams );
 *
 *          renderer.paint(graphic, paintArea, mapArea);
 * </pre>
 *
 * @author David Blasby
 * @author Simone Giannecchini
 */
public interface GTRenderer {

    /**
     * If you call this method from another thread than the one that called <code>paint</code> or
     * <code>render</code> the rendering will be forcefully stopped before termination.
     */
    public void stopRendering();

    /**
     * adds a listener that responds to error events of feature rendered events.
     *
     * @see RenderListener
     * @param listener the listener to add.
     */
    public void addRenderListener(RenderListener listener);

    /**
     * Removes a render listener.
     *
     * @see RenderListener
     * @param listener the listener to remove.
     */
    public void removeRenderListener(RenderListener listener);

    /** Hints used to configure Java2D Graphics prior to rendering. */
    public void setJava2DHints(RenderingHints hints);

    /**
     * Hints used to configure Java2D Graphics prior to rendering.
     *
     * @return Hints
     */
    public RenderingHints getJava2DHints();

    /** Hints used to configure rendering process. */
    public void setRendererHints(Map<Object, Object> hints);

    /**
     * Hints used to configure rendering process
     *
     * @return Hints used to configure rendering process
     */
    public Map<Object, Object> getRendererHints();

    /**
     * Sets the {@code MapContent} which contains the data to be rendered.
     *
     * @param mapContent the map content
     */
    public void setMapContent(MapContent mapContent);

    /**
     * Gets the {@code MapContent} instance which contains the data being rendered.
     *
     * @return the {@code MapContent} instance
     */
    public MapContent getMapContent();

    /**
     * Renders features based on the map layers and their styles as specified in the map content
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that the size of the output area and the transformation
     * from coordinates to pixels are known. The latter determines the map scale. The viewport (the
     * visible part of the map) will be calculated internally.
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param worldToScreen A transform which converts World coordinates to Screen coordinates.
     */
    // TASK: Need to check if the Layer CoordinateSystem is different to the BoundingBox rendering
    //       CoordinateSystem and if so, then transform the coordinates.
    public void paint(Graphics2D graphics, Rectangle paintArea, AffineTransform worldToScreen);

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that the area of the visible part of the map and the
     * size of the output area are known. The transform between the two is calculated internally.
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param mapArea the map's visible area (viewport) in map coordinates.
     */
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea);

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that the area of the visible part of the map and the
     * size of the output area are known. The transform between the two is calculated internally.
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param mapArea the map's visible area (viewport) in map coordinates.
     */
    public void paint(Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope mapArea);

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that paint area, enelope and worldToScreen transform
     * are already computed and in sync. Use this method to avoid recomputation. <b>Note however
     * that no check is performed that they are really synchronized!</b>
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param mapArea the map's visible area (viewport) in map coordinates.
     * @param worldToScreen A transform which converts World coordinates to Screen coordinates.
     */
    public void paint(
            Graphics2D graphics,
            Rectangle paintArea,
            Envelope mapArea,
            AffineTransform worldToScreen);

    /**
     * Renders features based on the map layers and their styles as specified in the map context
     * using <code>setContext</code>.
     *
     * <p>This version of the method assumes that paint area, enelope and worldToScreen transform
     * are already computed and in sync. Use this method to avoid recomputation. <b>Note however
     * that no check is performed that they are really synchronized!</b>
     *
     * @param graphics The graphics object to draw to.
     * @param paintArea The size of the output area in output units (eg: pixels).
     * @param mapArea the map's visible area (viewport) in map coordinates.
     * @param worldToScreen A transform which converts World coordinates to Screen coordinates.
     */
    public void paint(
            Graphics2D graphics,
            Rectangle paintArea,
            ReferencedEnvelope mapArea,
            AffineTransform worldToScreen);
}
