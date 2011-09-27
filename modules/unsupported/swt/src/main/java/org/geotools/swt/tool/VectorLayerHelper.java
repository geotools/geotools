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
package org.geotools.swt.tool;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Helper class used by {@code InfoTool} to query {@code MapLayers}
 * with vector feature data.
 * <p>
 * Implementation note: this class keeps only a weak reference to
 * the {@code MapLayer} it is working with to avoid memory leaks if
 * the layer is deleted.
 *
 * @see InfoTool
 * @see GridLayerHelper
 *
 * @author Michael Bedward
 * @since 2.6
 *
 *
 * @source $URL$
 */
public class VectorLayerHelper extends InfoToolHelper<SimpleFeatureCollection> {

    private static final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    private static final FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);
    private final WeakReference<Layer> layerRef;
    private final String attrName;
    private final boolean isPolygonGeometry;

    /**
     * Create a new helper to work with a {@code MapLayer} that has vector feature data.
     *
     * @param layer the map layer
     *
     * @param geomAttributeName the name of the geometry attribute for {@code Features}
     *
     * @param geomClass the geometry class
     */
    @SuppressWarnings("unchecked")
    public VectorLayerHelper( MapContent context, Layer layer ) {
        super(context, layer.getFeatureSource().getSchema().getCoordinateReferenceSystem());

        this.layerRef = new WeakReference<Layer>(layer);

        final GeometryDescriptor geomDesc = layer.getFeatureSource().getSchema().getGeometryDescriptor();
        this.attrName = geomDesc.getLocalName();

        final Class< ? extends Geometry> geomClass = (Class< ? extends Geometry>) geomDesc.getType().getBinding();
        final Geometries type = Geometries.getForBinding(geomClass);
        this.isPolygonGeometry = (type == Geometries.POLYGON || type == Geometries.MULTIPOLYGON);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid() {
        return getMapContent() != null && layerRef != null && layerRef.get() != null;
    }

    /**
     * Get the {@code MapLayer} that this helper is working with.
     *
     * @return the {@code MapLayer} or null if the original layer is no longer valid
     *
     * @see #isValid()
     */
    public Layer getMapLayer() {
        return layerRef != null ? layerRef.get() : null;
    }

    /*
     * Get feature data at the given position.
     *
     * @param pos the location to query
     *
     *
     */
    /**
     * {@inheritDoc}
     *
     * @param params a {@code Double} value for the search radius to use with
     *        point or line features
     *
     * @return the features that lie within the search radius of {@code pos}; if
     *         this helper is not valid an empty collection will be returned
     *
     * @throws IOException if the feature source for the layer cannot be accessed
     */
    public SimpleFeatureCollection getInfo( DirectPosition2D pos, Object... params ) throws IOException {

        SimpleFeatureCollection collection = null;
        Layer layer = layerRef.get();

        if (layer != null) {
            Filter filter = null;
            if (isPolygonGeometry) {
                /*
                 * Polygon features - use an intersects filter
                 */
                Geometry posGeom = createSearchPos(pos);
                filter = filterFactory.intersects(filterFactory.property(attrName), filterFactory.literal(posGeom));

            } else {
                /*
                 * Line or point features - use a bounding box filter
                 */
                double radius = ((Number) params[0]).doubleValue();
                ReferencedEnvelope env = createSearchEnv(pos, radius);
                filter = filterFactory.bbox(filterFactory.property(attrName), env);
            }

            Query query = new Query(null, filter);
            query.setCoordinateSystemReproject(getMapContent().getCoordinateReferenceSystem());
            collection = (SimpleFeatureCollection) layer.getFeatureSource().getFeatures(query);
        }

        return collection;
    }

    private Geometry createSearchPos( DirectPosition2D pos ) {
        Geometry point = geometryFactory.createPoint(new Coordinate(pos.x, pos.y));
        if (isTransformRequired()) {
            MathTransform transform = getTransform();
            if (transform != null) {
                try {
                    point = JTS.transform(point, transform);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }

        return point;
    }

    private ReferencedEnvelope createSearchEnv( DirectPosition2D pos, double radius ) {
        final CoordinateReferenceSystem contextCRS = getMapContent().getCoordinateReferenceSystem();
        ReferencedEnvelope env = new ReferencedEnvelope(pos.x - radius, pos.x + radius, pos.y - radius, pos.y + radius,
                contextCRS);
        if (isTransformRequired()) {
            CoordinateReferenceSystem layerCRS = layerRef.get().getFeatureSource().getSchema().getCoordinateReferenceSystem();
            try {
                env = env.transform(layerCRS, true);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        return env;
    }
}
