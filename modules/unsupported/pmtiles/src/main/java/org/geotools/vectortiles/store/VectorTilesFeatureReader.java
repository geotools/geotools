/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectortiles.store;

import static org.geotools.vectortiles.store.VectorTilesDataStore.DEFAULT_GEOMETRY_FACTORY;

import io.tileverse.vectortile.model.VectorTile;
import io.tileverse.vectortile.model.VectorTile.Layer.Feature;
import java.util.stream.Stream;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.geometry.jts.JTS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

class VectorTilesFeatureReader extends StreamFeatureReader<VectorTile.Layer.Feature> {

    public VectorTilesFeatureReader(SimpleFeatureType targetSchema, Stream<VectorTile.Layer.Feature> features) {
        super(targetSchema, features);
        super.mapper(this::vectorTilesFeatureToGeoToolsFeature);
    }

    protected SimpleFeature vectorTilesFeatureToGeoToolsFeature(VectorTile.Layer.Feature vectorTilesFeature) {

        if (!intersectsTileBounds(vectorTilesFeature)) {
            // don't even bother if the feature lays completely outside the tile bounds
            return null;
        }

        VectorTilesSimpleFeature feature = new VectorTilesSimpleFeature(targetSchema, vectorTilesFeature);

        Polygon clip = getClip(vectorTilesFeature);
        if (clip != null) {
            feature.getUserData().put(Hints.GEOMETRY_CLIP, clip);
        }
        return feature;
    }

    private boolean intersectsTileBounds(Feature vectorTilesFeature) {
        VectorTile tile = vectorTilesFeature.getLayer().getTile();
        Envelope boundingBox = tile.boundingBox().orElse(null);
        if (boundingBox == null || targetSchema.getGeometryDescriptor() == null) {
            return true;
        }
        Geometry geometry = vectorTilesFeature.getGeometry();
        if (geometry instanceof Point point) {
            return boundingBox.contains(point.getX(), point.getY());
        }
        return boundingBox.intersects(geometry.getEnvelopeInternal());
    }

    private Polygon getClip(Feature vectorTilesFeature) {
        if (targetSchema.getGeometryDescriptor() == null) {
            return null;
        }
        VectorTile tile = vectorTilesFeature.getLayer().getTile();
        Envelope tileBounds = tile.boundingBox().orElse(null);
        Geometry geometry = vectorTilesFeature.getGeometry();
        boolean addClip = false;
        if (geometry != null && tileBounds != null) {
            addClip = !tileBounds.covers(geometry.getEnvelopeInternal());
        }
        return addClip ? JTS.toGeometry(tileBounds, DEFAULT_GEOMETRY_FACTORY) : null;
    }
}
