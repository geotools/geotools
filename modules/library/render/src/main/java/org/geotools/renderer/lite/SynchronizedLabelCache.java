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
package org.geotools.renderer.lite;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.opengis.feature.Feature;

/**
 * Allow multiple thread to modify LabelCache.
 *
 * @author Jesse
 */
public class SynchronizedLabelCache implements LabelCache {

    private final LabelCache wrapped;

    public SynchronizedLabelCache() {
        this(new LabelCacheImpl());
    }

    public SynchronizedLabelCache(LabelCache cache) {
        wrapped = cache;
    }

    @Override
    public synchronized void start() {
        wrapped.start();
    }

    @Override
    public synchronized void clear() {
        wrapped.clear();
    }

    @Override
    public synchronized void clear(String layerId) {
        wrapped.clear(layerId);
    }

    @Override
    public synchronized void enableLayer(String layerId) {
        wrapped.enableLayer(layerId);
    }

    @Override
    public synchronized void end(Graphics2D graphics, Rectangle displayArea) {
        wrapped.end(graphics, displayArea);
    }

    @Override
    public synchronized void endLayer(String layerId, Graphics2D graphics, Rectangle displayArea) {
        wrapped.endLayer(layerId, graphics, displayArea);
    }

    @Override
    public synchronized void put(
            String layerId,
            TextSymbolizer symbolizer,
            Feature feature,
            LiteShape2 shape,
            NumberRange<Double> scaleRange) {
        wrapped.put(layerId, symbolizer, feature, shape, scaleRange);
    }

    @Override
    public synchronized void put(Rectangle2D area) {
        wrapped.put(area);
    }

    @Override
    public synchronized void startLayer(String layerId) {
        wrapped.startLayer(layerId);
    }

    @Override
    public synchronized void stop() {
        wrapped.stop();
    }

    @Override
    public synchronized void disableLayer(String layerId) {
        wrapped.disableLayer(layerId);
    }

    @Override
    public synchronized List orderedLabels() {
        return wrapped.orderedLabels();
    }
}
