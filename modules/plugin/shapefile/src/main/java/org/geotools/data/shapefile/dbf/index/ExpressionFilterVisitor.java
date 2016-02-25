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
package org.geotools.data.shapefile.dbf.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.filter.Capabilities;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.util.logging.Logging;

import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.identity.Identifier;

/**
 * FilterVisitor to resolve alphanumeric indexed expressions defined in a filter.
 * 
 * @author Alvaro Huarte
 */
public class ExpressionFilterVisitor extends DefaultFilterVisitor implements NodeVisitor {
    
    private static final Logger LOGGER = Logging.getLogger(ExpressionFilterVisitor.class);
    
    private Map<String,Node> indexNodeMap;
    
    // Describes the allowed filters we support for alphanumeric Dbase queries.
    protected static final Capabilities capabilities = new Capabilities();
    
    // Static constructor of ExpressionFilterVisitor class.
    static {
        capabilities.addAll(Capabilities.LOGICAL_OPENGIS);
        capabilities.addAll(Capabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(Add.class);
        capabilities.addType(Subtract.class);
        capabilities.addType(Multiply.class);
        capabilities.addType(Divide.class);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(PropertyIsLike.class);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(PropertyIsNil.class);
        capabilities.addType(Function.class);
    }
    
    /**
     * Set the index-node map to use by this ExpressionFilterVisitor.
     */
    public void setIndexNodeMap(Map<String,Node> indexNodeMap) {
        this.indexNodeMap = indexNodeMap;
    }
    
    /**
     * Determines if the filter and all its sub filters and expressions are supported.
     */
    public boolean fullySupports(Filter filter) {
        return ExpressionFilterVisitor.capabilities.fullySupports(filter);
    }
    
    /**
     * Overrides AttributeImpl to avoid the costly 'parse' method.
     */
    class FastAttributeImpl extends AttributeImpl {
        
        public FastAttributeImpl(Object content, AttributeType type, Identifier id) {
            super(content, type, id);
        }
        @Override
        protected Object parse(Object value) throws IllegalArgumentException {
            return value;
        }
    }
    /**
     * Overrides SimpleFeatureTypeImpl to wrap FilterVisitorArgs as a SimpleFeature.
     */
    class WrapSimpleFeatureTypeImpl extends SimpleFeatureTypeImpl {
        
        public WrapSimpleFeatureTypeImpl() {
            super(new NameImpl("FilterVisitorArgs"), Collections.emptyList(), null, false, Collections.emptyList(), null, null);
        }
        
        public void setFilterVisitorArgs(FilterVisitorArgs filterVisitorArgs) {
            this.filterVisitorArgs = filterVisitorArgs;
        }
        FilterVisitorArgs filterVisitorArgs;
        
        /**
         * Returns the AttributeDescriptor of the especified name.
         */
        @Override
        public AttributeDescriptor getDescriptor(String propertyName) {
            Property property = filterVisitorArgs.getProperty(propertyName);
            return property != null ? (AttributeDescriptor) property.getDescriptor() : null;            
        }
    }
    /**
     * Helper argument class of one filter evaluation.
     */
    class FilterVisitorArgs extends SimpleFeatureImpl implements NodeVisitorArgs {
        
        public FilterVisitorArgs(Filter filter) {
            
            super(Collections.emptyList(), new WrapSimpleFeatureTypeImpl(), new FeatureIdImpl("FilterVisitorArgs"));
            
            WrapSimpleFeatureTypeImpl featureType = (WrapSimpleFeatureTypeImpl)this.getFeatureType();
            featureType.setFilterVisitorArgs(this);
            
            FilterAttributeExtractor attributeExtractor = new FilterAttributeExtractor();
            filter.accept(attributeExtractor, null);
            Set<PropertyName> propertyNameList = attributeExtractor.getPropertyNameSet();
            
            this.propertyNameArray = propertyNameList.toArray(new PropertyName[0]);
            this.propertyArray = new Property[propertyNameList.size()];
            this.filter = filter;
            this.values = propertyArray;
        }
        PropertyName[] propertyNameArray;
        Property[] propertyArray;
        Filter filter;
        Map<Integer,Integer> result = new HashMap<Integer,Integer>();
        List<Integer> buffer = new ArrayList<Integer>();
        int evaluateCount;
        boolean cancel;
        
        /**
         * Returns the current filter being evaluated.
         */
        @Override
        public Filter filter() {
            return filter;
        }
        
        /**
         * Returns {@code true} if this job is cancelled.
         */
        @Override
        public boolean isCanceled() {
            return cancel;
        }
        
        /**
         * Indicates that task should be cancelled.
         */
        @Override
        public void setCanceled(boolean cancel) {
            this.cancel = cancel;
        }
        
        /**
         * Returns a shared buffer to efficiently visit nodes.
         */
        @Override
        public byte[] sharedBuffer(int bufferLength) {
            byte[] buffer = sharedBuffer;
            
            if (buffer==null || buffer.length<bufferLength) {
                sharedBuffer = new byte[bufferLength];
                buffer = sharedBuffer;
            }
            return buffer;
        }
        private byte[] sharedBuffer;
        
        /**
         * Returns a single property of the complex object which matches the specified name.
         */
        @Override
        public Property getProperty(String propertyName) {
            int propertyIndex = index.get(propertyName);
            return propertyIndex != -1 ? propertyArray[propertyIndex] : null;
        }
        /**
         * Returns a single property of the complex object which matches the specified index.
         */
        public Property getProperty(Node node, int propertyIndex) {
            Property property = propertyArray[propertyIndex];
            
            if (property==null) {
                String propertyName = propertyNameArray[propertyIndex].getPropertyName();
                AttributeType attributeType = new AttributeTypeImpl(new NameImpl(propertyName), node.getKey().getBinding(), false, false, null, null, null);
                property = new FastAttributeImpl(propertyName, attributeType, null);
                propertyArray[propertyIndex] = property;
                index.put(propertyName, propertyIndex);
            }
            return property;
        }
    }
    
    /**
     * Visit the specified node.
     */
    @Override
    public Object visit(Node node, Object extraData) {
        
        if (extraData!=null) { //-> Stop?
            
            Object[] paramData = (Object[])extraData;
            
            // Gets current filter parameters to process in the Node.
            Filter filter = (Filter)paramData[0];
            Object data = paramData[1];
            FilterVisitorArgs visitorArgs = (FilterVisitorArgs)paramData[2];
            int propertyIndex = (Integer)paramData[3];
            Property[] propertyArray = visitorArgs.propertyArray;
            
            // Assign a Property to pass to the evaluation of the filter.
            Property property = visitorArgs.getProperty(node, propertyIndex);
            property.setValue(node.getKey().getValue());
            
            // Visit or execute the filter.
            if (propertyIndex < propertyArray.length-1) {
                data = visitFilter(filter, data, visitorArgs, propertyIndex + 1);
                if (data==null) return null;
            }
            if (propertyIndex==propertyArray.length-1) {
                
                if (filter.evaluate(visitorArgs)) {
                    try {
                        List<Integer> bufferList = visitorArgs.buffer;
                        bufferList.clear();
                        
                        if (node.fillRecordIds(bufferList, visitorArgs)>0) {
                            for (int id : bufferList) visitorArgs.result.put(id, id);
                        }
                    }
                    catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Error processing Dbase index. Filter=("+filter.toString()+")", e);
                        return null;
                    }
                }
                visitorArgs.evaluateCount++;
            }
        }
        return extraData;
    }
    
    /**
     * Visit and resolve the specified Filter with properties to replace from Dbase index.
     */
    private Object visitFilter(Filter filter, Object data, FilterVisitorArgs visitorArgs, Integer propertyIndex) {
        
        if (data!=null && indexNodeMap!=null) { //-> Stop?
            
            Object[] paramData = new Object[]{ filter, data, visitorArgs, propertyIndex };
            
            String propertyName = visitorArgs.propertyNameArray[propertyIndex].getPropertyName();
            Node node = indexNodeMap.get(propertyName);
            if (node==null) return null;
            
            try {
                data = node.accept(this, paramData, visitorArgs);
                return data;
            }
            catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error processing Dbase index. Filter=("+filter.toString()+")", e);
                return null;
            }
        }
        return null;
    }
    /**
     * Visit and resolve the specified Filter.
     */
    private Object visitFilter(Filter filter, Object data, FilterVisitorArgs visitorArgs) {
        
        if (data!=null) { //-> Stop?
            
            int propertyCount = visitorArgs.propertyNameArray.length;
            
            // For the time, we do not evaluate filters with more than one property,
            // It is likely to be slower than by brute force process, this process
            // evaluates N*M times for each pair of properties.
            if (propertyCount>1) return null;
            
            // Evaluate recursively the current PropertyName collection.
            if (propertyCount>0) {
                data = visitFilter(filter, data, visitorArgs, 0)!=null ? new ResultBuffer(visitorArgs.result) : null;
                LOGGER.log(Level.FINEST, "Dbase Index Tree was visited " + visitorArgs.evaluateCount + " times");
            }
            else {
                data = filter.evaluate(data) ? ResultBuffer.makeFull() : ResultBuffer.makeEmpty();
            }
            return data;
        }
        return null;
    }
    
    @Override
    public Object visit(PropertyIsEqualTo filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    @Override
    public Object visit(PropertyIsGreaterThan filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    @Override
    public Object visit(PropertyIsLessThan filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    
    @Override
    public Object visit(PropertyIsBetween filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    @Override
    public Object visit(PropertyIsLike filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    @Override
    public Object visit(PropertyIsNull filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    @Override
    public Object visit(PropertyIsNil filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    
    @Override
    public Object visit(Not filter, Object data) {
        return visitFilter(filter, data, new FilterVisitorArgs(filter));
    }
    
    @Override
    public Object visit(And filter, Object data) {
        
        List<Filter> childList = filter.getChildren();
        data = ResultBuffer.makeUndefined();
        
        if (childList!=null && data!=null) {
            for (Filter child : childList) {
                if (child!=null) {
                    Object childData = child.accept(this, data);
                    if (childData==null) return null;
                    ResultBuffer r = ResultBuffer.makeAnd((ResultBuffer)data, (ResultBuffer)childData);
                    if (r.isEmpty()) return r;
                    data = r;
                }
            }
        }
        return data;
    }
    
    @Override
    public Object visit(Or filter, Object data) {
        
        List<Filter> childList = filter.getChildren();
        data = ResultBuffer.makeUndefined();
        
        if (childList!=null && data!=null) {
            for (Filter child : childList) {
                if (child!=null) {
                    Object childData = child.accept(this, data);
                    if (childData==null) return null;
                    ResultBuffer r = ResultBuffer.makeOr((ResultBuffer)data, (ResultBuffer)childData);
                    if (r.isFull()) return r;
                    data = r;
                }
            }
        }
        return data;
    }
}
