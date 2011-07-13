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
package org.geotools.swing.event;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.swing.MapPane;
import org.geotools.swing.tool.CursorTool;
import org.geotools.swing.tool.MapToolManager;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Mock map pane class for testing. 
 * 
 * @author michael
 */
class MockPane extends Component implements MapPane {
    private MapContent mapContent;
    private List<MapPaneListener> mapPaneListeners;
    private MapToolManager toolManager;

    public MockPane() {
        mapContent = new MapContent();
        mapContent.getViewport().setMatchingAspectRatio(true);
        
        mapPaneListeners = new ArrayList<MapPaneListener>();
        toolManager = new MapToolManager(this);
    }

    public void setMapContent(MapContent content) {
        mapContent = content;
        mapContent.getViewport().setMatchingAspectRatio(true);
    }

    public MapContent getMapContent() {
        return mapContent;
    }

    public void setRenderer(GTRenderer renderer) {
    }

    public GTRenderer getRenderer() {
        return new StreamingRenderer();
    }

    public ReferencedEnvelope getDisplayArea() {
        return mapContent.getViewport().getBounds();
    }

    public void setDisplayArea(Envelope envelope) {
        CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
        if (crs == null) {
            // assume that it is the current CRS
            crs = mapContent.getCoordinateReferenceSystem();
        }
        ReferencedEnvelope refEnv = new ReferencedEnvelope(
                envelope.getMinimum(0), envelope.getMaximum(0), 
                envelope.getMinimum(1), envelope.getMaximum(1), crs);
        mapContent.getViewport().setBounds(refEnv);
    }
    
    public void setScreenArea(Rectangle screenArea) {
        mapContent.getViewport().setScreenArea(screenArea);
    }

    public void reset() {
        // do nothing
    }

    public AffineTransform getScreenToWorldTransform() {
        return mapContent.getViewport().getScreenToWorld();
    }

    public AffineTransform getWorldToScreenTransform() {
        return mapContent.getViewport().getWorldToScreen();
    }
    
    public void addMapPaneListener(MapPaneListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("null listener arg");
        }
        mapPaneListeners.add(listener);
    }

    public void removeMapPaneListener(MapPaneListener listener) {
        if (listener != null) {
            mapPaneListeners.remove(listener);
        }
    }

    public void addMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("null listener arg");
        }
        toolManager.addMouseListener(listener);
    }

    public void removeMouseListener(MapMouseListener listener) {
        if (listener != null) {
            toolManager.removeMouseListener(listener);
        }
    }

    @Override
    public void setCursorTool(CursorTool tool) {
        // empty method
    }

}
