/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.renderer.lite.StreamingRenderer.RenderableFeature;

/**
 * Paints a group of {@link LiteFeatureTypeStyle} all associated with the same {@link FeatureSource}
 *
 * @author Andrea Aime - GeoSolutions
 */
class ZGroupLayerPainter implements AutoCloseable {

    Feature currentFeature;

    MarkFeatureIterator iterator;

    List<LiteFeatureTypeStyle> lfts;

    SortKey currentKey;

    SortBy[] sortBy;

    StreamingRenderer renderer;

    RenderableFeature renderable;

    boolean complete = false;

    public ZGroupLayerPainter(
            MarkFeatureIterator iterator, List<LiteFeatureTypeStyle> lfts, StreamingRenderer renderer, String layerId)
            throws IOException {
        super();
        this.iterator = iterator;
        this.lfts = lfts;
        this.renderer = renderer;
        this.sortBy = lfts.get(0).sortBy;
        this.currentKey = new SortKey(sortBy.length);
        this.renderer = renderer;
        // when doing z-ordering we paint multiple time the same feature, so we
        // have to force geometry cloning, otherwise we'll transform it in place multiple times
        this.renderable = renderer.createRenderableFeature(layerId, true);
        next();
    }

    public void paintKey(SortKey reference) throws IOException {
        // anything to paint?
        if (!reference.equals(currentKey)) {
            return;
        }
        Feature startFeature = currentFeature;
        iterator.mark();
        for (int i = 0; i < lfts.size(); i++) {
            while (!complete && reference.equals(currentKey)) {
                renderable.setFeature(currentFeature);
                LiteFeatureTypeStyle lftsi = lfts.get(i);
                renderer.processFeature(renderable, lftsi, lftsi.projectionHandler);
                if (renderer.renderingStopRequested) {
                    return;
                }
                next();
            }
            // we have to restart from the beginning with the next fts, remember
            // the first feature was already loaded
            if (i < lfts.size() - 1) {
                iterator.reset();
                complete = false;
                currentFeature = startFeature;
                currentKey.copy(reference);
            }
        }
    }

    private void next() {
        if (iterator.hasNext()) {
            this.currentFeature = iterator.next();
            // update the current key
            for (int i = 0; i < sortBy.length; i++) {
                SortBy sb = sortBy[i];
                if (sb == SortBy.NATURAL_ORDER || sb == SortBy.REVERSE_ORDER) {
                    currentKey.components[i] = currentFeature.getIdentifier().getID();
                } else {
                    Object value = sb.getPropertyName().evaluate(currentFeature);
                    currentKey.components[i] = value;
                }
            }
        } else {
            // mark as complete but don't close, we might have
            // other fts to paint
            complete = true;
        }
    }

    SortKey getCurrentKey() {
        return complete ? null : this.currentKey;
    }

    public boolean complete() {
        return complete;
    }

    @Override
    public void close() {
        if (iterator != null) {
            iterator.close();
            iterator = null;
            currentFeature = null;
            currentKey = null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ZGroupLayerPainter [currentFeature="
                + currentFeature
                + ", iterator="
                + iterator
                + ", lfts="
                + lfts
                + ", currentKey="
                + currentKey
                + ", sortBy="
                + Arrays.toString(sortBy)
                + ", complete="
                + complete
                + "]";
    }
}
