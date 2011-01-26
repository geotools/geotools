/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.shape;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.lite.LabelCache;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.opengis.feature.Feature;

/**
 * Wrapper around another {@link LabelCache} that disables the following methods:
 * <ul>
 * <li> {@link LabelCache#start()}</li>
 * <li> {@link LabelCache#stop()}</li>
 * <li> {@link LabelCache#clear()}</li>
 * <li> {@link LabelCache#end()}</li>
 * </ul>
 * This class can be used to share a label cache with another renderer and making sure
 * the other renderer cannot wipe out the cache contents or trigger the actual labelling start,
 * allowing a "top" renderer to control the labelling lifecycle.
 * @author Andrea Aime - TOPP
 *
 */
class IntegratingLabelCache implements LabelCache {
    
    LabelCache wrapped;

    public IntegratingLabelCache(LabelCache wrapped) {
        this.wrapped = wrapped;
    }

    public void clear() {
        //
    }

    public void clear(String layerId) {
        wrapped.clear(layerId);
    }

    public void disableLayer(String layerId) {
        wrapped.disableLayer(layerId);
    }

    public void enableLayer(String layerId) {
        wrapped.enableLayer(layerId);
    }

    public void end(Graphics2D graphics, Rectangle displayArea) {
        //
    }

    public void endLayer(String layerId, Graphics2D graphics, Rectangle displayArea) {
        wrapped.endLayer(layerId, graphics, displayArea);
    }

    public List orderedLabels() {
        return wrapped.orderedLabels();
    }

    public void put(String layerId, TextSymbolizer symbolizer, Feature feature, LiteShape2 shape,
            NumberRange scaleRange) {
        wrapped.put(layerId, symbolizer, feature, shape, scaleRange);
    }

    public void put( Rectangle2D area) {
        wrapped.put( area );
    }
    
    public void start() {
        //
    }

    public void startLayer(String layerId) {
        wrapped.startLayer(layerId);
    }

    public void stop() {
        //
    }
    
    

}
