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
package org.geotools.data.oracle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.jdbc.SQLDialect;
import org.opengis.filter.Filter;
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
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Oracle specific filter encoder.
 * 
 * @author Justin Deoliveira, OpenGEO
 * @author Andrea Aime, OpenGEO
 *
 * @source $URL$
 */
public class OracleFilterToSQL extends PreparedFilterToSQL {
    
    /** Logger - for logging */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.filter.SQLEncoderOracle");

    /** Contains filter type to SDO_RELATE mask type mappings */
    private static final Map<Class, String> SDO_RELATE_MASK_MAP = new HashMap<Class, String>() {
        {
            put(Contains.class, "contains");
            put(Crosses.class, "overlapbdydisjoint");
            put(Equals.class, "equal");
            put(Overlaps.class, "overlapbdyintersect");
            put(Touches.class, "touch");
            put(Within.class, "inside");
            put(Disjoint.class, "disjoint");
            put(BBOX.class, "anyinteract");
            put(Intersects.class, "anyinteract");
        }
    };
    
    /**
     * The whole world in WGS84
     */
    private static final Envelope WORLD = new Envelope(-179.99,179.99,-89.99,89.99);

    /**
     * If we have to turn <code>a op b</code> into <code>b op2 a</code>, what's the op2 that returns
     * the same result?
     */
    private static final Map<String, String> INVERSE_OPERATOR_MAP = new HashMap<String, String>() {
        {
            // asymmetric operators, op2 = !op
            put("contains", "inside");
            put("inside", "contains");
            // symmetric operators, op2 = op
            put("overlapbdydisjoint", "overlapbdydisjoint");
            put("overlapbdyintersect", "overlapbdyintersect");
            put("touch", "touch");
            put("equal", "equal");
            put("anyinteract", "anyinteract");
            put("disjoint", "disjoint");
        }
    };

    /**
     * Whether BBOX should be encoded as just a primary filter or primary+secondary
     */
    protected boolean looseBBOXEnabled;

    public OracleFilterToSQL(PreparedStatementSQLDialect dialect) {
        super(dialect);
        setSqlNameEscape("\"");
    }
    
    public boolean isLooseBBOXEnabled() {
        return looseBBOXEnabled;
    }

    public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
        this.looseBBOXEnabled = looseBBOXEnabled;
    }
    
    @Override
    protected FilterCapabilities createFilterCapabilities() {
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
    
    @Override
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, PropertyName property,
            Literal geometry, boolean swapped, Object extraData) {
        try {
            Geometry eval = geometry.evaluate(filter, Geometry.class);
            // Oracle cannot deal with filters using geometries that span beyond the whole world
            // in case the 
            if (dialect != null && isCurrentGeometryGeodetic() &&
                    !WORLD.contains(eval.getEnvelopeInternal())) {
                Geometry result = eval.intersection(JTS.toGeometry(WORLD));
                
                if (result != null && !result.isEmpty()) {
                    if(result instanceof GeometryCollection) {
                        result = distillSameTypeGeometries((GeometryCollection) result, eval);
                    } 
                    geometry = new FilterFactoryImpl().createLiteralExpression(result);
                }
            }
            
            if(filter instanceof Beyond || filter instanceof DWithin)
                doSDODistance(filter, property, geometry, extraData);
            else if(filter instanceof BBOX && looseBBOXEnabled) {
                doSDOFilter(filter, property, geometry, extraData);
            } else
                doSDORelate(filter, property, geometry, swapped, extraData);
        } catch (IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }
    
    /**
     * Returns true if the current geometry has the geodetic marker raised
     * @return
     */
    boolean isCurrentGeometryGeodetic() {
        if(currentGeometry != null) {
            Boolean geodetic = (Boolean) currentGeometry.getUserData().get(OracleDialect.GEODETIC);
            return geodetic != null && geodetic;
        }
        return false;    
    }

    protected Geometry distillSameTypeGeometries(GeometryCollection coll, Geometry original) {
        if(original instanceof Polygon || original instanceof MultiPolygon) {
            List<Polygon> polys = new ArrayList<Polygon>();
            accumulateGeometries(polys, coll, Polygon.class);
            return original.getFactory().createMultiPolygon(((Polygon[]) polys.toArray(new Polygon[polys.size()])));
        } else if(original instanceof LineString || original instanceof MultiLineString) {
            List<LineString> ls = new ArrayList<LineString>();
            accumulateGeometries(ls, coll, LineString.class);
            return original.getFactory().createMultiLineString((LineString[]) ls.toArray(new LineString[ls.size()]));
        } else if(original instanceof Point || original instanceof MultiPoint) {
            List<LineString> points = new ArrayList<LineString>();
            accumulateGeometries(points, coll, LineString.class);
            return original.getFactory().createMultiPoint((Point[]) points.toArray(new Point[points.size()]));
        } else {
            return original;
        }
    }
    
    protected <T> void accumulateGeometries(List<T> collection, Geometry g, Class<? extends T> target) {
        if(target.isInstance(g)) {
            collection.add((T) g);
        } else if(g instanceof GeometryCollection) {
            GeometryCollection coll = (GeometryCollection) g;
            for (int i = 0; i < coll.getNumGeometries(); i++) {
                accumulateGeometries(collection, coll.getGeometryN(i), target);
            }
        }
    }
    
    protected void doSDOFilter(Filter filter, PropertyName property, Literal geometry, Object extraData) throws IOException {
        out.write("SDO_FILTER(");
        property.accept(this, extraData);
        out.write(", ");
        geometry.accept(this, extraData);
        // for backwards compatibility with Oracle 9 we add the mask and querytypes params
        out.write(", 'mask=anyinteract querytype=WINDOW') = 'TRUE' ");
    }
    
    /**
     * Encodes an SDO relate
     * @param filter
     * @param property
     * @param geometry
     * @param extraData
     */
    protected void doSDORelate(Filter filter, PropertyName property, Literal geometry, boolean swapped, Object extraData) throws IOException {
        // grab the operating mask
        String mask = null;
        for (Class filterClass : SDO_RELATE_MASK_MAP.keySet()) {
            if(filterClass.isAssignableFrom(filter.getClass()))
                mask = SDO_RELATE_MASK_MAP.get(filterClass);
        }
        if(mask == null)
            throw new IllegalArgumentException("Cannot encode filter " + filter.getClass() + " into a SDO_RELATE");
        if(swapped)
            mask = INVERSE_OPERATOR_MAP.get(mask);
        
        // ok, ready to write out the SDO_RELATE
        out.write("SDO_RELATE(");
        property.accept(this, extraData);
        out.write(", ");
        geometry.accept(this, extraData);
        // for disjoint we ask for no interaction, anyinteract == false
        if(filter instanceof Disjoint) {
            out.write(", 'mask=ANYINTERACT querytype=WINDOW') <> 'TRUE' ");
        } else {
            out.write(", 'mask=" + mask + " querytype=WINDOW') = 'TRUE' ");
        }
    }
    
    protected void doSDODistance(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, Object extraData) throws IOException {
        double distance = ((DistanceBufferOperator) filter).getDistance();
        String unit = ((DistanceBufferOperator) filter).getDistanceUnits();
        String within = filter instanceof DWithin ? "TRUE" : "FALSE"; 
        
        out.write("SDO_WITHIN_DISTANCE(");
        property.accept(this, extraData);
        out.write(",");
        geometry.accept(this, extraData);
        
        // encode the unit verbatim when available
        if(unit != null && !"".equals(unit.trim()))
            out.write(",'distance=" + distance + " unit=" + unit + "') = '" + within + "' ");
        else
            out.write(",'distance=" + distance + "') = '" + within + "' ");
    }
   
}
