/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Or;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * Custom simplifying filter visitor that also tries to turn multiple or-ed
 * spatial filters into a single spatial filter (since ArcSDE query cannot
 * handle or-ed spatial conditions)
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class ArcSdeSimplifyingFilterVisitor extends SimplifyingFilterVisitor {
    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);
    
    // this is the list of spatial operations that we can merge upon (that is, it's not the full list)
    static final Set<Class<? extends BinarySpatialOperator>> SPATIAL_OPERATIONS = new HashSet<Class<? extends BinarySpatialOperator>>();
    static {
        SPATIAL_OPERATIONS.add(BBOX.class);
        SPATIAL_OPERATIONS.add(Intersects.class);
        SPATIAL_OPERATIONS.add(Crosses.class);
        SPATIAL_OPERATIONS.add(Overlaps.class);
        // commented out for the moment, we could merge but only if the distance is the same
        // SPATIAL_OPERATIONS.add(DWithin.class);
        // commented out for the moment, we could merge but only if they are not two overlapping
        // polygons, as the union will remove some borders on that case
        // SPATIAL_OPERATIONS.add(Touches.class);
    }

    private SimpleFeatureType schema;
    
    public ArcSdeSimplifyingFilterVisitor(SimpleFeatureType schema) {
        this.schema = schema;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        // perform the standard simplification
        Filter simplified = (Filter) super.visit(filter, extraData);
        
        // is it still an Or filter?
        if(simplified instanceof Or) {
            // collect spatial filters so that they are separated per attribute 
            Map<String, List<SpatialOperation>> spatialOps = new HashMap<String, List<SpatialOperation>>();
            List<Filter> otherFilters = new ArrayList<Filter>();
            List<Filter> children = ((Or) simplified).getChildren();
            for (Filter child : children) {
                // we know how to merge only bbox and intersects for the moment
                if(child instanceof BinarySpatialOperator) {
                    BinarySpatialOperator bso = (BinarySpatialOperator) child;
                    String name = null;
                    SpatialOperation so = null;
                    if(bso.getExpression1() instanceof PropertyName && bso.getExpression2() instanceof Literal) {
                        name = ((PropertyName) bso.getExpression1()).getPropertyName();
                        so = new SpatialOperation(bso);
                    } else if(bso.getExpression2() instanceof PropertyName && bso.getExpression1() instanceof Literal) {
                        name = ((PropertyName) bso.getExpression2()).getPropertyName();
                        so = new SpatialOperation(bso);
                    } 
                    
                    if(name != null && so != null) {
                        // handle the default geometry case
                        if("".equals(name) && schema.getGeometryDescriptor() != null) {
                            name = schema.getGeometryDescriptor().getLocalName();
                        }
                        
                        // collect into the specific geometry list
                        List<SpatialOperation> list = spatialOps.get(name);
                        if(list == null) {
                            list = new ArrayList<ArcSdeSimplifyingFilterVisitor.SpatialOperation>();
                            spatialOps.put(name, list);
                        }
                        list.add(so);
                    } else {
                        // cannot handle this one
                        otherFilters.add(child);
                    }
                } else {
                    otherFilters.add(child);
                }
            }
            
            // try to merge all filters that work agains the same attribute and perform the same
            // (or similar enough) operation
            List<Filter> mergedFilters = new ArrayList<Filter>();
            for (String property : spatialOps.keySet()) {
                List<SpatialOperation> propertyFilters = spatialOps.get(property);
                
                // we perform a reduction on the list of filters, trying to find groups that can be merged
                while(propertyFilters.size() > 0) {
                    SpatialOperation main = propertyFilters.get(0);
                    List<SpatialOperation> toMerge = new ArrayList<SpatialOperation>();
                    toMerge.add(main);
                    for (int j = 1; j < propertyFilters.size(); ) {
                        SpatialOperation secondary = propertyFilters.get(j);
                        // check if the two operations are compatible
                        if(secondary.operation == main.operation 
                                || (secondary.operation == BBOX.class && main.operation == Intersects.class) 
                                || (secondary.operation == Intersects.class && main.operation == BBOX.class)) {
                            toMerge.add(secondary);
                            propertyFilters.remove(j);
                        } else {
                            j++;
                        }
                    }
                    
                    if(toMerge.size() == 1) {
                        // could not be merged, put in the "others" list
                        otherFilters.add(main.op);
                    } else {
                        try {
                            Filter merged = mergeOperations(property, toMerge);
                            mergedFilters.add(merged);
                        } catch(Exception e) {
                            // the operation can go belly up because of topology exceptions, in
                            // that case we just add back all the operations to the main list
                            for (SpatialOperation so : toMerge) {
                                otherFilters.add(so.op);
                            }
                        }
                    }
                    propertyFilters.remove(0);
                }
            }
            
            // did we manage to squash anything?
            if(mergedFilters.size() == 1 && otherFilters.size() == 0) {
                simplified = mergedFilters.get(0);
            } else if(mergedFilters.size() > 0) {
                List<Filter> full = new ArrayList<Filter>();
                full.addAll(mergedFilters);
                full.addAll(otherFilters);
                simplified = FF.or(full);
            } 
        } 
            
        return simplified;
    }
    
    private Filter mergeOperations(String propertyName, List<SpatialOperation> ops) {
        // prepare the property name
        PropertyName property = FF.property(propertyName);
        
        // prepare united the geometry
        Geometry[] geomArray = new Geometry[ops.size()];
        for (int i = 0; i < geomArray.length; i++) {
            geomArray[i] = ops.get(i).geometry;
        }
        GeometryCollection collection = geomArray[0].getFactory().createGeometryCollection(geomArray);
        Geometry united = collection.union();
        Literal geometry = FF.literal(united);
        
        // rebuild the filter
        Class operation = ops.get(0).operation;
        if(BBOX.class.isAssignableFrom(operation) || Intersects.class.isAssignableFrom(operation)) {
            return FF.intersects(property, geometry);
        } else if(Crosses.class.isAssignableFrom(operation)) {
            return FF.crosses(property, geometry);
        } else if(Overlaps.class.isAssignableFrom(operation)) {
            return FF.overlaps(property, geometry);
        } else if(Touches.class.isAssignableFrom(operation)) {
            return FF.touches(property, geometry);
        } else {
            throw new IllegalArgumentException("Cannot merge operation " + operation.getName());
        }
    }


    static class SpatialOperation {
        BinarySpatialOperator op;
        Class operation;
        Geometry geometry;
        
        public SpatialOperation(BinarySpatialOperator op) {
            this.op = op;
            for (Class iface : op.getClass().getInterfaces()) {
                if(SPATIAL_OPERATIONS.contains(iface)) {
                    operation = iface;
                    break;
                }
            }
            if(op.getExpression1() instanceof Literal) {
                geometry = op.getExpression1().evaluate(null, Geometry.class);
            } else if(op.getExpression2() instanceof Literal) {
                geometry = op.getExpression2().evaluate(null, Geometry.class);
            } else {
                throw new IllegalArgumentException("Cannot find literal geometry in the spatial filter");
            }
        }
    }
}
