/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.util.Collection;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.Feature;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Helper class used by {@linkplain InfoTool} to query vector features in a {@linkplain
 * org.geotools.map.FeatureLayer}.
 *
 * @author Michael Bedward
 * @since 2.6
 * @version $URL$
 */
public class FeatureLayerHelper extends InfoToolHelper {

    /**
     * Default distance fraction used with line and point features. When the user clicks on the map,
     * this tool searches for features within a rectangle of width w centred on the mouse location,
     * where w is the average map side length multiplied by the value of this constant.
     */
    public static final double DEFAULT_DISTANCE_FRACTION = 0.01d;

    private static final GeometryFactory geometryFactory =
            JTSFactoryFinder.getGeometryFactory(null);
    private static final FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);

    private String attrName;
    private Geometries geomType;

    /** No argument constructor required by the helper lookup system. */
    public FeatureLayerHelper() {}

    /**
     * {@inheritDoc} The {@code layer} argument must be an instance of {@linkplain FeatureLayer}.
     */
    @Override
    public void setLayer(Layer layer) {
        if (!(layer instanceof FeatureLayer)) {
            throw new IllegalArgumentException("layer must be an instance of FeatureLayer");
        }

        super.setLayer(layer);

        GeometryDescriptor geomDesc = layer.getFeatureSource().getSchema().getGeometryDescriptor();
        attrName = geomDesc.getLocalName();

        Class<? extends Geometry> geomClass =
                (Class<? extends Geometry>) geomDesc.getType().getBinding();
        geomType = Geometries.getForBinding(geomClass);
    }

    @Override
    public boolean isSupportedLayer(Layer layer) {
        return layer instanceof FeatureLayer;
    }

    @Override
    public InfoToolResult getInfo(DirectPosition2D pos) throws Exception {
        InfoToolResult result = new InfoToolResult();

        if (isValid()) {
            Filter filter = null;
            if (geomType == Geometries.POLYGON || geomType == Geometries.MULTIPOLYGON) {
                Geometry posGeom = createSearchPoint(pos);
                filter =
                        filterFactory.intersects(
                                filterFactory.property(attrName), filterFactory.literal(posGeom));

            } else {
                ReferencedEnvelope env = createSearchEnv(pos);
                filter = filterFactory.bbox(filterFactory.property(attrName), env);
            }

            Query query = new Query(null, filter);
            query.setCoordinateSystemReproject(getMapContent().getCoordinateReferenceSystem());
            FeatureSource featureSource = getLayer().getFeatureSource();
            Collection<PropertyDescriptor> descriptors = featureSource.getSchema().getDescriptors();

            FeatureCollection queryResult = featureSource.getFeatures(query);
            FeatureIterator iter = queryResult.features();

            try {
                while (iter.hasNext()) {
                    Feature f = iter.next();
                    result.newFeature(f.getIdentifier().getID());
                    for (PropertyDescriptor desc : descriptors) {
                        Name name = desc.getName();
                        Object value = f.getProperty(name).getValue();

                        if (value != null) {
                            if (value instanceof Geometry) {
                                result.setFeatureValue(name, value.getClass().getSimpleName());
                            } else {
                                result.setFeatureValue(name, value);
                            }
                        } else {
                            result.setFeatureValue(name, "null");
                        }
                    }
                }

            } finally {
                iter.close();
            }
        }

        return result;
    }

    /**
     * Converts the query position, in map content coordinates, to a position in layer coordinates
     * and returns it as a JTS {@code Point}.
     *
     * @param pos query position in map content coordaintes
     * @return point in layer coordinates
     */
    private Geometry createSearchPoint(DirectPosition2D pos) {
        try {
            DirectPosition2D trPos = new DirectPosition2D();
            getContentToLayerTransform().transform(pos, trPos);
            Geometry point = geometryFactory.createPoint(new Coordinate(trPos.x, trPos.y));
            return point;

        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private ReferencedEnvelope createSearchEnv(DirectPosition2D pos) {
        ReferencedEnvelope mapBounds = getMapContent().getViewport().getBounds();
        if (mapBounds == null || mapBounds.isEmpty()) {
            // fall back to layer bounds
            Layer layer = getLayer();
            if (layer == null) {
                // this should never happen
                throw new IllegalStateException("Target layer has been lost");
            }
            mapBounds = getLayer().getBounds();
        }

        double halfWidth =
                0.5 * DEFAULT_DISTANCE_FRACTION * (mapBounds.getWidth() + mapBounds.getHeight());

        CoordinateReferenceSystem contentCRS = getMapContent().getCoordinateReferenceSystem();
        ReferencedEnvelope env =
                new ReferencedEnvelope(
                        pos.x - halfWidth,
                        pos.x + halfWidth,
                        pos.y - halfWidth,
                        pos.y + halfWidth,
                        contentCRS);

        if (isTransformRequired()) {
            CoordinateReferenceSystem layerCRS =
                    getLayer().getFeatureSource().getSchema().getCoordinateReferenceSystem();

            try {
                env = env.transform(layerCRS, true);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        return env;
    }
}
