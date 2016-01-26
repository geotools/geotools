/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package mil.nga.giat.data.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mil.nga.giat.data.elasticsearch.ElasticLayerConfiguration.GEOMETRY_TYPE;
import mil.nga.giat.data.elasticsearch.ElasticAttribute.ElasticGeometryType;

import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTS;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

class FilterToElasticHelper {
    
    protected double lat, lon, distance;
    
    protected String key;
    
    protected Literal geometry;
    
    protected ShapeRelation shapeRelation;
    
    protected ShapeBuilder shapeBuilder;

    /**
     * Conversion factor from common units to meter
     */
    protected static final Map<String, Double> UNITS_MAP = new HashMap<String, Double>() {
        {
            put("kilometers", 1000.0);
            put("kilometer", 1000.0);
            put("mm", 0.001);
            put("millimeter", 0.001);
            put("mi", 1609.344);
            put("miles", 1609.344);
            put("NM", 1852d);
            put("feet", 0.3048);
            put("ft", 0.3048);
            put("in", 0.0254);
        }
    };

    protected static final Envelope WORLD = new Envelope(-180, 180, -90, 90);

    FilterToElastic delegate;

    public FilterToElasticHelper(FilterToElastic delegate) {
        this.delegate = delegate;
    }
    
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {

        if (filter instanceof DistanceBufferOperator) {
            visitDistanceSpatialOperator((DistanceBufferOperator) filter,
                    property, geometry, swapped, extraData);
        } else {
            visitComparisonSpatialOperator(filter, property, geometry,
                    swapped, extraData);
        }
        return extraData;
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, Expression e2,
            Object extraData) {

        visitBinarySpatialOperator(filter, e1, e2, false, extraData);
        return extraData;
    }

    protected void visitDistanceSpatialOperator(DistanceBufferOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {

        property.accept(delegate, extraData);
        key = (String) delegate.field;
        geometry.accept(delegate, extraData);
        final Geometry geo = delegate.currentGeometry;
        lat = geo.getCentroid().getY();
        lon = geo.getCentroid().getX();
        final double inputDistance = filter.getDistance();
        final String inputUnits = filter.getDistanceUnits();
        distance = Double.valueOf(toMeters(inputDistance, inputUnits));
    }

    private String toMeters(double distance, String unit) {
        // only geography uses metric units
        if (isCurrentGeography()) {
            Double conversion = UNITS_MAP.get(unit);
            if (conversion != null) {
                return String.valueOf(distance * conversion);
            }
        }

        // in case unknown unit or not geography, use as-is
        return String.valueOf(distance);
    }

    void visitComparisonSpatialOperator(BinarySpatialOperator filter, PropertyName property, Literal geometry,
            boolean swapped, Object extraData) throws IOException {

        // if geography case, sanitize geometry first
        this.geometry = geometry;
        if(isCurrentGeography()) {
            this.geometry = clipToWorld(geometry);
        }

        visitBinarySpatialOperator(filter, (Expression)property, (Expression)this.geometry, swapped, extraData);
    }

    void visitBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, Expression e2, boolean swapped,
            Object extraData) throws IOException {

        AttributeDescriptor attType;
        attType = (AttributeDescriptor)e1.evaluate(delegate.featureType);

        ElasticGeometryType geometryType;
        geometryType = (ElasticGeometryType) attType.getUserData().get(GEOMETRY_TYPE);
        if (geometryType == ElasticGeometryType.GEO_POINT) {
            visitGeoPointBinarySpatialOperator(filter, e1, e2, swapped, extraData);
        } else {
            visitGeoShapeBinarySpatialOperator(filter, e1, e2, swapped, extraData);
        }
    }

    void visitGeoShapeBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, Expression e2, boolean swapped,
            Object extraData) throws IOException {

        if (filter instanceof Disjoint) {
            shapeRelation = ShapeRelation.DISJOINT;
        } else if ((!swapped && filter instanceof Within) || (swapped && filter instanceof Contains)) {
            shapeRelation = ShapeRelation.WITHIN;
        } else if (filter instanceof Intersects || filter instanceof BBOX) {
            shapeRelation = ShapeRelation.INTERSECTS;
        } else {
            FilterToElastic.LOGGER.fine(filter.getClass().getSimpleName() + " is unsupported for geo_shape types");
            shapeRelation = null;
            delegate.fullySupported = false;
        }

        if (shapeRelation != null) {
            e1.accept(delegate, extraData);
            key = (String) delegate.field;
            e2.accept(delegate, extraData);
            shapeBuilder = delegate.currentShapeBuilder;
        }
    }

    void visitGeoPointBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, Expression e2, boolean swapped,
            Object extraData) throws IOException {

        e1.accept(delegate, extraData);
        key = (String) delegate.field;
        e2.accept(delegate, extraData);
    }

    boolean isCurrentGeography() {
        return true;
    }

    protected Literal clipToWorld(Literal geometry) {
        if(geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            if (g != null) {
                Envelope env = g.getEnvelopeInternal();
                // first, limit to world
                if (!WORLD.contains(env)) {
                    g = sanitizePolygons(g.intersection(JTS.toGeometry(WORLD)));
                }

                // second, postgis will always use the shortest distance between
                // two
                // points, if an arc is longer than 180 degrees the opposite
                // will
                // be used instead, so we have to slice the geometry in parts
                // TODO: Remove this if not relevant to Elasticsearch
                env = g.getEnvelopeInternal();
                if (Math.sqrt(env.getWidth() * env.getWidth() + env.getHeight() * env.getHeight()) >= 180) {
                    // slice in 90x90 degrees quadrants, none of them has a
                    // diagonal longer than 180
                    final List<Polygon> polygons = new ArrayList<Polygon>();
                    for (double lon = Math.floor(env.getMinX()); lon < env.getMaxX(); lon += 90) {
                        for (double lat = Math.floor(env.getMinY()); lat < env.getMaxY(); lat += 90) {
                            Geometry quadrant = JTS.toGeometry(new Envelope(lon, lon + 90, lat, lat + 90));
                            Geometry cut = sanitizePolygons(g.intersection(quadrant));
                            if (!cut.isEmpty()) {
                                if (cut instanceof Polygon) {
                                    polygons.add((Polygon) cut);
                                } else {
                                    for (int i = 0; i < cut.getNumGeometries(); i++) {
                                        polygons.add((Polygon) cut.getGeometryN(i));
                                    }
                                }
                            }
                        }
                    }
                    g = toPolygon(g.getFactory(), polygons);
                }

                geometry = CommonFactoryFinder.getFilterFactory(null).literal(g);

            }
        }

        return geometry;
    }

    /**
     * Given a geometry that might contain heterogeneous components extracts
     * only the polygonal ones
     * 
     * @param geometry
     * @return
     */
    protected Geometry sanitizePolygons(Geometry geometry) {
        // already sane?
        if (geometry == null || geometry instanceof Polygon || geometry instanceof MultiPolygon) {
            return geometry;
        }

        // filter out only polygonal parts
        final List<Polygon> polygons = new ArrayList<Polygon>();
        geometry.apply(new GeometryComponentFilter() {

            public void filter(Geometry geom) {
                if (geom instanceof Polygon) {
                    polygons.add((Polygon) geom);
                }
            }
        });

        // turn filtered selection into a geometry
        return toPolygon(geometry.getFactory(), polygons);
    }

    protected Geometry toPolygon(GeometryFactory gf, final List<Polygon> polygons) {
        if(polygons.size() == 0) {
            return gf.createGeometryCollection(null);
        } else if (polygons.size() == 1) {
            return polygons.get(0);
        } else {
            return gf.createMultiPolygon((Polygon[]) polygons.toArray(new Polygon[polygons.size()]));
        }
    }

    /**
     * Returns true if the geometry covers the entire world
     * 
     * @param geometry
     * @return
     */
    protected boolean isWorld(Literal geometry) {
        boolean result = false;
        if(geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            if (g != null) {
                result = JTS.toGeometry(WORLD).equalsTopo(g.union());
            }
        }
        return result;
    }

    /**
     * Returns true if the geometry is fully empty
     * 
     * @param geometry
     * @return
     */
    protected boolean isEmpty(Literal geometry) {
        boolean result = false;
        if(geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            result = g == null || g.isEmpty();
        }
        return result;
    }

}
