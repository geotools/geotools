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

import com.vividsolutions.jts.geom.Envelope;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.Map;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;

/**
 *
 * @author michael
 */
public class MockRenderer implements GTRenderer {
    private MapContent mapContent;

    @Override
    public void stopRendering() {
    }

    @Override
    public void addRenderListener(RenderListener listener) {
    }

    @Override
    public void removeRenderListener(RenderListener listener) {
    }

    @Override
    public void setJava2DHints(RenderingHints hints) {
    }

    @Override
    public RenderingHints getJava2DHints() {
        return new Hints();
    }

    @Override
    public void setRendererHints(Map<Object, Object> hints) {
    }

    @Override
    public Map<Object, Object> getRendererHints() {
        return Collections.emptyMap();
    }

    @Override
    public void setContext(MapContext context) {
    }

    @Override
    public void setMapContent(MapContent mapContent) {
        this.mapContent = mapContent;
    }

    @Override
    public MapContext getContext() {
        throw new UnsupportedOperationException("Should not be called");
    }

    @Override
    public MapContent getMapContent() {
        return mapContent;
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, AffineTransform worldToScreen) {
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea) {
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope mapArea) {
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea, AffineTransform worldToScreen) {
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope mapArea, AffineTransform worldToScreen) {
    }
    
}
