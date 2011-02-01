/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.Query;
import org.geotools.data.complex.filter.XPath;
import org.geotools.feature.AppSchemaFeatureFactoryImpl;
import org.geotools.feature.Types;
import org.geotools.filter.FilterFactoryImplNamespaceAware;
import org.geotools.xlink.XLINK;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.EmptyGeometry;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Base class for several MappingFeatureImplementation's. 
 * 
 * @author Russell Petty, GSV
 * @version $Id$
 * @source $URL$
 */
public abstract class AbstractMappingFeatureIterator implements IMappingFeatureIterator {
    /** The logger for the filter module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.complex");

    /**
     * Name representation of xlink:href
     */
    public static final Name XLINK_HREF_NAME = Types.toTypeName(XLINK.HREF);

    /**
     * The mappings for the source and target schemas
     */
    protected FeatureTypeMapping mapping;

    /**
     * Factory used to create the target feature and attributes
     */
    protected FeatureFactory attf;

    protected AppSchemaDataAccess store;

    final protected XPath xpathAttributeBuilder;

    protected FilterFactory namespaceAwareFilterFactory;

    /**
     * maxFeatures restriction value as provided by query
     */
    protected final int maxFeatures;

    /** counter to ensure maxFeatures is not exceeded */
    protected int featureCounter;

    protected NamespaceSupport namespaces;

    /**
     * True if hasNext has been called prior to calling next()
     */
    private boolean hasNextCalled = false;
    
    protected Set<String> propertyNames;

    public AbstractMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query) throws IOException {
        this(store, mapping, query, false);
    }
    
    public AbstractMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, boolean isQueryUnrolled) throws IOException {
        this.store = store;
        this.attf = new AppSchemaFeatureFactoryImpl();

        Name name = mapping.getTargetFeature().getName();
        
        propertyNames = query.getPropertyNames()==null? null: new HashSet<String>(Arrays.asList(query.getPropertyNames()));

        this.mapping = mapping;
        
        this.maxFeatures = query.getMaxFeatures();
                
        if (!isQueryUnrolled) {
            query = getUnrolledQuery(query);
        }
        xpathAttributeBuilder = new XPath();
        xpathAttributeBuilder.setFeatureFactory(attf);
        initialiseSourceFeatures(mapping, query);
        namespaces = mapping.getNamespaces();
        namespaceAwareFilterFactory = new FilterFactoryImplNamespaceAware(namespaces);
        xpathAttributeBuilder.setFilterFactory(namespaceAwareFilterFactory);
    }

    /**
     * Shall not be called, just throws an UnsupportedOperationException
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Closes the underlying FeatureIterator
     */
    public void close() {
        closeSourceFeatures();
    }

    /**
     * Based on the set of xpath expression/id extracting expression, finds the ID for the attribute
     * <code>idExpression</code> from the source complex attribute.
     * 
     * @param idExpression
     *            the location path of the attribute to be created, for which to obtain the id by
     *            evaluating the corresponding <code>org.geotools.filter.Expression</code> from
     *            <code>sourceInstance</code>.
     * @param sourceInstance
     *            a complex attribute which is the source of the mapping.
     * @return the ID to be applied to a new attribute instance addressed by
     *         <code>attributeXPath</code>, or <code>null</code> if there is no an id mapping for
     *         that attribute.
     */
    protected abstract String extractIdForAttribute(final Expression idExpression,
            Object sourceInstance);    
    /**
     * Return a query appropriate to its underlying feature source.
     * 
     * @param query
     *            the original query against the output schema
     * @return a query appropriate to be executed over the underlying feature source.
     */
    protected Query getUnrolledQuery(Query query) {
        return store.unrollQuery(query, mapping);
    }

    protected boolean isHasNextCalled() {
        return hasNextCalled;
    }
    
    protected void setHasNextCalled(boolean hasNextCalled) {
        this.hasNextCalled = hasNextCalled;
    }
    
    /**
     * Return next feature.
     * 
     * @see java.util.Iterator#next()
     */
    public Feature next() {      
        if (!isHasNextCalled()) {
            LOGGER.warning("hasNext not called before calling next() in the iterator!");
            if (!hasNext()) {
                return null;
            }
        }
        Feature next;
        try {
            next = computeNext();
            this.cleanEmptyElements(next);
        } catch (IOException e) {
            close();
            throw new RuntimeException(e);
        }
        ++featureCounter;
                
        return next;
    }
    
    private void cleanEmptyElements(Feature target) throws DataSourceException {
        try {
            ArrayList values = new ArrayList<Property>();
            for (Iterator i = target.getValue().iterator(); i.hasNext();) {
                Property p = (Property) i.next();

                if (hasChild(p) || p.getDescriptor().getMinOccurs() > 0) {
                    values.add(p);
                }
            }
            target.setValue(values);
        } catch (DataSourceException e) {
            throw new DataSourceException("Unable to clean empty element", e);
        }
    }
    
    private boolean hasChild(Property p) throws DataSourceException {
        boolean result = false;
        if (p.getValue() instanceof Collection) {

            Collection c = (Collection) p.getValue();
            
            if (this.getClientProperties(p).containsKey(XLINK_HREF_NAME)) {
                return true;
            }
            
            ArrayList values = new ArrayList();
            for (Object o : c) {
                if (o instanceof Property) {
                    if (hasChild((Property) o)) {
                        values.add(o);
                        result = true;
                    } else if (((Property) o).getDescriptor().getMinOccurs() > 0) {
                        if (((Property) o).getDescriptor().isNillable()) {
                            // add nil mandatory property
                            values.add(o);
                        }
                    }
                }
            }
            p.setValue(values);
        } else if (p.getName().equals(ComplexFeatureConstants.FEATURE_CHAINING_LINK_NAME)) {
            // ignore fake attribute FEATURE_LINK
            result = false;
        } else if (p.getValue() != null && p.getValue().toString().length() > 0) {
            result = true;
        }
        return result;
    }
    
    protected Map getClientProperties(Property attribute) throws DataSourceException {

        Map<Object, Object> userData = attribute.getUserData();
        Map clientProperties = new HashMap<Name, Expression>();
        if (userData != null && userData.containsKey(Attributes.class)) {
            Map props = (Map) userData.get(Attributes.class);
            if (!props.isEmpty()) {
                clientProperties.putAll(props);
            }
        }
        return clientProperties;
    }
    
    protected void setClientProperties(final Attribute target, final Object source,
            final Map<Name, Expression> clientProperties) {
        if (target == null) {
            return;
        }
        
        if (source == null) {
            return;
        }

        // NC - first calculate target attributes
        final Map<Name, Object> targetAttributes = new HashMap<Name, Object>();
        if (target.getUserData().containsValue(Attributes.class)) {
            targetAttributes.putAll((Map<? extends Name, ? extends Object>) target.getUserData()
                    .get(Attributes.class));
        }        
        for (Map.Entry<Name, Expression> entry : clientProperties.entrySet()) {
            Name propName = entry.getKey();
            Object propExpr = entry.getValue();
            Object propValue;
            if (propExpr instanceof Expression) {
                propValue = getValue((Expression) propExpr, source);
            } else {
                propValue = propExpr;
            }
            if (propValue != null) {
                if (propValue instanceof Collection) {
                    if (!((Collection)propValue).isEmpty()) {                
                        propValue = ((Collection)propValue).iterator().next();
                        targetAttributes.put(propName, propValue);
                    }
                } else {
                    targetAttributes.put(propName, propValue);
                }
            } 
        }
        // FIXME should set a child Property.. but be careful for things that
        // are smuggled in there internally and don't exist in the schema, like
        // XSDTypeDefinition, CRS etc.
        if (targetAttributes.size() > 0) {
            target.getUserData().put(Attributes.class, targetAttributes);
        }
        
        setGeometryUserData(target, targetAttributes);
    }
    
    protected void setGeometryUserData(Attribute target, Map<Name, Object> targetAttributes) {
     // with geometry objects, set ID and attributes in geometry object
        if (target instanceof GeometryAttribute
                && (targetAttributes.size() > 0 || target.getIdentifier() != null)) {
            Geometry geom;
            if (target.getValue() == null) {
                // create empty geometry if null but attributes
                geom = new EmptyGeometry();
            } else {
                // need to clone because it seems the same geometry object from the
                // db is reused instead of regenerated if different attributes refer
                // to the same database row... so if we change the userData, we have
                // to clone it
                geom = (Geometry) ((Geometry) target.getValue()).clone();
            }

            if (geom != null) {

                Object userData = geom.getUserData();
                Map newUserData = new HashMap<Object, Object>();
                if (userData != null) {
                    if (userData instanceof Map) {
                        newUserData.putAll((Map) userData);
                    } else if (userData instanceof CoordinateReferenceSystem) {
                        newUserData.put(CoordinateReferenceSystem.class, userData);
                    }
                }
                // set gml:id and attributes in Geometry userData
                if (target.getIdentifier() != null) {
                    newUserData.put("gml:id", target.getIdentifier().toString());
                }
                if (targetAttributes.size() > 0) {
                    newUserData.put(Attributes.class, targetAttributes);
                }

                geom.setUserData(newUserData);
                target.setValue(geom);
            }
        }
    }
    
    protected abstract void closeSourceFeatures();

    protected abstract Iterator<SimpleFeature> getSourceFeatureIterator();

    protected abstract void initialiseSourceFeatures(FeatureTypeMapping mapping, Query query)
            throws IOException;

    protected abstract boolean unprocessedFeatureExists();

    protected abstract boolean sourceFeatureIteratorHasNext();

    protected abstract String extractIdForFeature();

    protected abstract boolean isNextSourceFeatureNull();

    protected abstract Feature populateFeatureData(String id) throws IOException;

    protected abstract Object getValue(Expression expression, Object sourceFeature);

    protected abstract boolean isSourceFeatureIteratorNull();
    
    protected abstract Feature computeNext() throws IOException;   

    public abstract boolean hasNext();
}
