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
package org.geotools.data.shapefile;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.CloseableIterator;
import org.geotools.data.shapefile.dbf.index.ExpressionFilterVisitor;
import org.geotools.data.shapefile.dbf.index.ResultBuffer;
import org.geotools.data.shapefile.index.Data;
import org.geotools.filter.Capabilities;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;

import org.opengis.filter.Filter;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Envelope;

/**
 * FilterVisitor to resolve indexed expressions defined in a filter.
 * 
 * @author Alvaro Huarte
 */
class ShapeFileExpressionFilterVisitor extends ExpressionFilterVisitor {
    
    private static final Logger LOGGER = Logging.getLogger(ShapeFileExpressionFilterVisitor.class);
    
    private IndexManager indexManager;
    
    // Describes the allowed filters we support for Spatial-Dbase queries.
    protected static final Capabilities capabilities = new Capabilities();
    
    // Static constructor of ShapeFileExpressionFilterVisitor class.
    static {
        capabilities.addAll(ExpressionFilterVisitor.capabilities);
        capabilities.addType(BBOX.class);
        capabilities.addType(Contains.class);
        capabilities.addType(Crosses.class);
        capabilities.addType(DWithin.class);
        capabilities.addType(Equals.class);
        capabilities.addType(Intersects.class);
        capabilities.addType(Overlaps.class);
        capabilities.addType(Touches.class);
        capabilities.addType(Within.class);
    }
    
    /**
     * Creates a new ShapeFileExpressionFilterVisitor object.
     */
    public ShapeFileExpressionFilterVisitor(IndexManager indexManager) {
        this.indexManager = indexManager;
    }
    
    /**
     * Determines if the filter and all its sub filters and expressions are supported.
     */
    @Override
    public boolean fullySupports(Filter filter) {
        return ShapeFileExpressionFilterVisitor.capabilities.fullySupports(filter);
    }
    
    /**
     * Visit and resolve the specified BinarySpatialOperator. 
     */
    private Object visitBinarySpatialOperator(BinarySpatialOperator filter, Object data) {
        
        if (data != null) { //-> Stop?
            
            Envelope bbox = new ReferencedEnvelope();
            bbox = (Envelope)filter.accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
            
            if (bbox != null && !bbox.isNull() && !Double.isInfinite(bbox.getWidth()) && !Double.isInfinite(bbox.getHeight())) {
                try {
                    CloseableIterator<Data> records = indexManager.querySpatialIndex(bbox, true);
                    
                    if (records != null) {
                        Map<Integer,Integer> result = new HashMap<Integer,Integer>();
                        
                        while (records.hasNext()) {
                            Integer recno = (Integer)records.next().getValue(0) - 1;
                            result.put(recno, recno);
                        }
                        records.close();
                        
                        return new ResultBuffer(result);
                    }
                }
                catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error processing spatial index. Filter=("+filter.toString()+")", e);
                    return null;
                }
            }
            // Returns 'null' to force the normal processing of the Query without indexing.
            return null;
        }
        return data;
    }
    @Override
    public Object visit(final BBOX filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
    @Override
    public Object visit(Contains filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
    @Override
    public Object visit(Crosses filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
    @Override
    public Object visit(DWithin filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
    @Override
    public Object visit(Equals filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
    @Override
    public Object visit(Intersects filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
    @Override
    public Object visit(Overlaps filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
    @Override
    public Object visit(Touches filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
    @Override
    public Object visit(Within filter, Object data) {
        return visitBinarySpatialOperator(filter, data);
    }
}
