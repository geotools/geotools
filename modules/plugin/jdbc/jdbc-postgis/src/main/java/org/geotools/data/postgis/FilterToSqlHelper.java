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
package org.geotools.data.postgis;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.geometry.jts.JTS;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

class FilterToSqlHelper {
    
    protected static final String IO_ERROR = "io problem writing filter";
    
    private static final Envelope WORLD = new Envelope(-180, 180, -90, 90);
    
    FilterToSQL delegate;
    Writer out;
    boolean looseBBOXEnabled;

    public FilterToSqlHelper(FilterToSQL delegate) {
        this.delegate = delegate;
    }
    
    public static FilterCapabilities createFilterCapabilities() {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addAll(SQLDialect.BASE_DBMS_CAPABILITIES);

        // adding the spatial filters support
        caps.addType(BBOX.class);
        caps.addType(Contains.class);
        caps.addType(Crosses.class);
        caps.addType(Disjoint.class);
        caps.addType(Equals.class);
        caps.addType(Intersects.class);
        caps.addType(Overlaps.class);
        caps.addType(Touches.class);
        caps.addType(Within.class);
        caps.addType(DWithin.class);
        caps.addType(Beyond.class);

        return caps;
    }
    
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {
        try {
            if (filter instanceof DistanceBufferOperator) {
                visitDistanceSpatialOperator((DistanceBufferOperator) filter,
                        property, geometry, swapped, extraData);
            } else {
                visitComparisonSpatialOperator(filter, property, geometry,
                        swapped, extraData);
            }
        } catch (IOException e) {
            throw new RuntimeException(IO_ERROR, e);
        }
        return extraData;
    }
    
    void visitDistanceSpatialOperator(DistanceBufferOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) throws IOException {
        if ((filter instanceof DWithin && !swapped)
                || (filter instanceof Beyond && swapped)) {
            out.write("ST_DWithin(");
            property.accept(delegate, extraData);
            out.write(",");
            geometry.accept(delegate, extraData);
            out.write(",");
            out.write(Double.toString(filter.getDistance()));
            out.write(")");
        }
        if ((filter instanceof DWithin && swapped)
                || (filter instanceof Beyond && !swapped)) {
            out.write("ST_Distance(");
            property.accept(delegate, extraData);
            out.write(",");
            geometry.accept(delegate, extraData);
            out.write(") > ");
            out.write(Double.toString(filter.getDistance()));
        }
    }

    void visitComparisonSpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped, Object extraData)
            throws IOException {

        // if geography case, sanitize geometry first
        if(isCurrentGeography()) {
            geometry = clipToWorld(geometry);
            if(isWorld(geometry)) {
                // nothing to filter in this case
                out.write(" TRUE ");
                return;
            } else if(isEmpty(geometry)) {
                if(!(filter instanceof Disjoint)) {
                    out.write(" FALSE ");       
                } else {
                    out.write(" TRUE ");
                }
                return;
            }
        }
        
        // add && filter if possible
        if(!(filter instanceof Disjoint)) {
            
            property.accept(delegate, extraData);
            out.write(" && ");
            geometry.accept(delegate, extraData);
    
            // if we're just encoding a bbox in loose mode, we're done 
            if(filter instanceof BBOX && looseBBOXEnabled)
                return;
                
            out.write(" AND ");
        }

        String closingParenthesis = ")";
        if (filter instanceof Equals) {
            out.write("ST_Equals");
        } else if (filter instanceof Disjoint) {
            out.write("NOT (ST_Intersects");
            closingParenthesis += ")";
        } else if (filter instanceof Intersects || filter instanceof BBOX) {
            out.write("ST_Intersects");
        } else if (filter instanceof Crosses) {
            out.write("ST_Crosses");
        } else if (filter instanceof Within) {
            if(swapped)
                out.write("ST_Contains");
            else
                out.write("ST_Within");
        } else if (filter instanceof Contains) {
            if(swapped)
                out.write("ST_Within");
            else
                out.write("ST_Contains");
        } else if (filter instanceof Overlaps) {
            out.write("ST_Overlaps");
        } else if (filter instanceof Touches) {
            out.write("ST_Touches");
        } else {
            throw new RuntimeException("Unsupported filter type " + filter.getClass());
        }
        out.write("(");

        property.accept(delegate, extraData);
        out.write(", ");
        geometry.accept(delegate, extraData);

        out.write(closingParenthesis);
    }
    
    boolean isCurrentGeography() {
        AttributeDescriptor geom = null;
        if(delegate instanceof PostgisPSFilterToSql) {
            geom = ((PostgisPSFilterToSql) delegate).getCurrentGeometry();
        } else if(delegate instanceof PostgisFilterToSQL) {
            geom = ((PostgisFilterToSQL) delegate).getCurrentGeometry();
        } 
        
        return geom != null && 
            "geography".equals(geom.getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME));
    }

    private Literal clipToWorld(Literal geometry) {
        if(geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            if(g != null) {
                Envelope env = g.getEnvelopeInternal();
                // first, limit to world
                if(!WORLD.contains(env)) {
                    g = sanitizePolygons(g.intersection(JTS.toGeometry(WORLD)));
                }
                
                // second, postgis will always use the shortest distance between two
                // points, if an arc is longer than 180 degrees the opposite will
                // be used instead, so we have to slice the geometry in parts
                env = g.getEnvelopeInternal();
                if(Math.sqrt(env.getWidth() * env.getWidth() + env.getHeight() * env.getHeight()) >= 180) {
                    // slice in 90x90 degrees quadrants, none of them has a diagonal longer than 180
                    final List<Polygon> polygons = new ArrayList<Polygon>();
                    for(double lon = Math.floor(env.getMinX()); lon < env.getMaxX(); lon+= 90) {
                        for (double lat = Math.floor(env.getMinY()); lat < env.getMaxY(); lat += 90) {
                            Geometry quadrant = JTS.toGeometry(new Envelope(lon, lon + 90, lat, lat + 90));
                            Geometry cut = sanitizePolygons(g.intersection(quadrant));
                            if(!cut.isEmpty()) {
                                if(cut instanceof Polygon) {
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
     * Given a geometry that might contain heterogeneous components extracts only the polygonal ones
     * @param geometry
     * @return
     */
    private Geometry sanitizePolygons(Geometry geometry) {
        // already sane?
        if(geometry == null || geometry instanceof Polygon || geometry instanceof MultiPolygon) {
            return geometry;
        }
        
        // filter out only polygonal parts
        final List<Polygon> polygons = new ArrayList<Polygon>(); 
        geometry.apply(new GeometryComponentFilter() {
            
            public void filter(Geometry geom) {
                if(geom instanceof Polygon) {
                    polygons.add((Polygon) geom);
                }
            }
        });

        // turn filtered selection into a geometry
        return toPolygon(geometry.getFactory(), polygons);
    }

    private Geometry toPolygon(GeometryFactory gf, final List<Polygon> polygons) {
        if(polygons.size() == 0) {
            return gf.createGeometryCollection(null);
        } else if(polygons.size() == 1) {
            return polygons.get(0);
        } else {
            return gf.createMultiPolygon((Polygon[]) polygons.toArray(new Polygon[polygons.size()]));
        }
    }
    
    /**
     * Returns true if the geometry covers the entire world
     * @param geometry
     * @return
     */
    private boolean isWorld(Literal geometry) {
        if(geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            if(g != null) {
                return JTS.toGeometry(WORLD).equals(g.union());
            }
        }
        return false;
    }
    
    /**
     * Returns true if the geometry is fully empty
     * @param geometry
     * @return
     */
    private boolean isEmpty(Literal geometry) {
        if(geometry != null) {
            Geometry g = geometry.evaluate(null, Geometry.class);
            return g == null || g.isEmpty();
        }
        return false;
    }
    

    
}
