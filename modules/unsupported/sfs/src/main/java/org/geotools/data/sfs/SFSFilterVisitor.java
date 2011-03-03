/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.sfs;

import java.io.StringWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.FilterVisitor;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import java.net.URLEncoder;

import org.geotools.geojson.geom.GeometryJSON;

/**
 *
 * This class is pulled from GeoRest DataStore implementation and modified
 * @author
 */
class SFSFilterVisitor implements FilterVisitor {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.simplefeatureservice");
    private Map<String, String> properties = new HashMap<String, String>();
    private List<String> queryable = new ArrayList<String>();
    private boolean sortProperties;
    private boolean hasProperties;
    private static final String strEncoding="UTF-8";

    public SFSFilterVisitor() {
    }

    public SFSFilterVisitor(boolean sortProperties) {
        this.sortProperties = sortProperties;
    }

    public boolean isSortProperties() {
        return sortProperties;
    }

    public void setSortProperties(boolean sortProperties) {
        this.sortProperties = sortProperties;
    }

    /**
     * Finish up building the URL, and return it as a string.
     *
     * @param builder
     *            StringBuilder that contains the base URL from which to start adding URL
     *            properties.
     * @param hasProperties
     *            Does the given URL already contains other properties?
     * @return Returns the URL with the newly added properties for filtering.
     */
    public String finish(StringBuilder builder, boolean hasProperties) throws UnsupportedEncodingException {

        this.hasProperties = hasProperties;

        List<String> props = new ArrayList<String>(properties.keySet());
        if (sortProperties) {
            Collections.sort(queryable);
            Collections.sort(props);
        }

        // Add all properties to the URL:
        for (String property : props) {
            builder.append(getGlueChar());
            builder.append(URLEncoder.encode(property, strEncoding));

            builder.append("=");
            builder.append(URLEncoder.encode(properties.get(property), strEncoding));
        }

        // Add queryable properties to the URL if needed:
        if (queryable != null && queryable.size() > 0) {
            builder.append(getGlueChar());
            builder.append("queryable=");
            for (int i = 0; i < queryable.size(); i++) {
                
                builder.append(URLEncoder.encode(queryable.get(i), strEncoding));
                if (i < queryable.size() - 1) {
                    builder.append(",");
                }
            }
        }

        // Cleanup:
        properties.clear();
        queryable.clear();
        return builder.toString();
    }

    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    public Object visit(ExcludeFilter filter, Object extraData) {
        throw new UnsupportedOperationException("visit (ExcludeFilter filter, Object extraData)");
    }

    public Object visit(IncludeFilter filter, Object extraData) {
        return extraData;
    }

    public Object visit(And filter, Object extraData) {
        Iterator<?> iter = filter.getChildren().iterator();
        while (iter.hasNext()) {
            Filter element = (Filter) iter.next();
            element.accept(this, extraData);
        }
        return extraData;
    }

    public Object visit(Id filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Not filter, Object extraData)");
    }

    public Object visit(Not filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Not filter, Object extraData)");
    }

    public Object visit(Or filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Or filter, Object extraData)");
    }

    public Object visit(PropertyIsBetween filter, Object extraData) {
        throw new UnsupportedOperationException(
                "visit (PropertyIsBetween filter, Object extraData)");
    }

    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
        checkPropertyFilter(propertyName, "__eq", filter);
        return extraData;
    }

    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
        checkPropertyFilter(propertyName, "__ne", filter);
        return extraData;
    }

    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
        checkPropertyFilter(propertyName, "__gt", filter);
        return extraData;
    }

    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
        checkPropertyFilter(propertyName, "__gte", filter);
        return extraData;
    }

    public Object visit(PropertyIsLessThan filter, Object extraData) {
        String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
        checkPropertyFilter(propertyName, "__lt", filter);
        return extraData;
    }

    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        String propertyName = ((PropertyName) filter.getExpression1()).getPropertyName();
        checkPropertyFilter(propertyName, "__lte", filter);
        return extraData;
    }

    public Object visit(PropertyIsLike filter, Object extraData) {
        String propertyName = ((PropertyName) filter.getExpression()).getPropertyName();
        String operator = filter.isMatchingCase() ? "__like" : "__ilike";
        if (!properties.containsKey(propertyName + operator)) {
            String value = filter.getLiteral();
            if (value != null) {
                properties.put(propertyName + operator, value);
                if (!queryable.contains(propertyName)) {
                    queryable.add(propertyName);
                }
            }
        }

        return extraData;
    }

    public Object visit(PropertyIsNull filter, Object extraData) {
        throw new UnsupportedOperationException("visit (PropertyIsNull filter, Object extraData)");
    }

    public Object visit(BBOX filter, Object extraData) {
        Polygon polygon = (Polygon) filter.getExpression2().evaluate(extraData);
        Envelope box = polygon.getEnvelopeInternal();
        if (!properties.containsKey("bbox")) {
            properties.put("bbox", box.getMinX() + "," + box.getMinY() + "," + box.getMaxX() + ","
                    + box.getMaxY());
        } else {
            throw new IllegalArgumentException("Filter cannot contain more than one bounding box.");
        }
        if (!properties.containsKey("epsg") && polygon.getSRID() != 0) {
            properties.put("epsg", polygon.getSRID() + "");
        }
        return extraData;
    }

    public Object visit(Beyond filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Beyond filter, Object extraData)");
    }

    public Object visit(Contains filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Contains filter, Object extraData)");
    }

    public Object visit(Crosses filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Crosses filter, Object extraData)");
    }

    public Object visit(Disjoint filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Disjoint filter, Object extraData)");
    }

    public Object visit(DWithin filter, Object extraData) {
        // check we actually support this thing
        boolean valid = true;
        if(!(filter.getExpression1() instanceof PropertyName)) {
            valid = false;
        } 
        if(!(filter.getExpression2() instanceof Literal)) {
            valid = false;
        }
        if(!valid) {
            throw new UnsupportedOperationException("DWithin filter on this store is supported only " +
            		"if the first operand is the default geometry property and the second " +
            		"is a geometry literal");
        }
        
        
        Geometry geometry = (Geometry) filter.getExpression2().evaluate(extraData);

        if(geometry instanceof Point) {
            Point point = (Point) geometry;
            if (!properties.containsKey("lon")) {
                properties.put("lon", String.valueOf(point.getX()));
            } else {
                throw new IllegalArgumentException("Long. is already set");
            }
    
            if (!properties.containsKey("lat")) {
                properties.put("lat", String.valueOf(point.getY()));
            } else {
                throw new IllegalArgumentException("Lat is already set");
            }
        } else {
            writeGeometry(geometry);
        }
        
        if (!properties.containsKey("tolerance")) {
            properties.put("tolerance", String.valueOf(filter.getDistance()));
        } else {
            throw new IllegalArgumentException("tolerance is already set");
        }

        if (!properties.containsKey("epsg") ) {
            properties.put("epsg", filter.getDistanceUnits() + "");
        }
        
        return extraData;
    }

    public Object visit(Equals filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Equals filter, Object extraData)");
    }

    public Object visit(Intersects filter, Object extraData) {
        
        Geometry goem = (Geometry) filter.getExpression2().evaluate(null);
        writeGeometry(goem);
        
        return extraData;
    }

    private void writeGeometry(Geometry geom) {
        if (!properties.containsKey("geometry")) {
            String strGeoJSON = "";
            GeometryJSON gjson = new GeometryJSON();
            try {
                StringWriter sw = new StringWriter();
                gjson.write(geom, sw);
                strGeoJSON = sw.toString();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, " Exception at visit  : Intersect Filter " + ex.getMessage(), ex);
            }
            properties.put("geometry", strGeoJSON);
        } else {
            throw new IllegalArgumentException("Geometry is already sey");
        }
    }

    public Object visit(Overlaps filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Overlaps filter, Object extraData)");
    }

    public Object visit(Touches filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Touches filter, Object extraData)");
    }

    public Object visit(Within filter, Object extraData) {
        throw new UnsupportedOperationException("visit (Within filter, Object extraData)");
    }

    // Private methods:
    private String getGlueChar() {
        if (!hasProperties) {
            hasProperties = true;
            return "?";
        }
        return "&";
    }

    private void checkPropertyFilter(String propertyName, String extension,
            BinaryComparisonOperator filter) {
        String combined = propertyName + extension;
        if (!properties.containsKey(combined)) {
            Object value = ((Literal) filter.getExpression2()).getValue();
            if (value != null) {
                properties.put(combined, value.toString());
                if (!queryable.contains(propertyName)) {
                    queryable.add(propertyName);
                }
            }
        }
    }
}
