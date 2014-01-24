/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import net.opengis.wfs20.ResolveValueType;

import org.geotools.data.DataSourceException;
import org.geotools.data.Query;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.filter.XPathUtil.StepList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.AppSchemaFeatureFactoryImpl;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.Types;
import org.geotools.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.filter.FilterFactoryImplNamespaceAware;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.xlink.XLINK;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.EmptyGeometry;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Base class for several MappingFeatureImplementation's. 
 * 
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 *
 *
 *
 * @source $URL$
 */
public abstract class AbstractMappingFeatureIterator implements IMappingFeatureIterator {
    /** The logger for the filter module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.complex");
    
    private FilterFactory2 filterFac = CommonFactoryFinder.getFilterFactory2(null);
    
    protected FeatureTypeFactory ftf = new ComplexFeatureTypeFactoryImpl();

    /**
     * Name representation of xlink:href
     */
    public static final Name XLINK_HREF_NAME = Types.toTypeName(XLINK.HREF);

    /**
     * Milliseconds between polls of resolver thread.
     */
    public static final long RESOLVE_TIMEOUT_POLL_INTERVAL = 100;

    /**
     * The mappings for the source and target schemas
     */
    protected FeatureTypeMapping mapping;
    
    /**
     * Mappings after Property Selection is applied
     */
    protected List<AttributeMapping> selectedMapping;
    
    /**
     * Selected Properties for Feature Chaining
     */
    protected Map<AttributeMapping, List<PropertyName>> selectedProperties;
    
    protected boolean includeMandatory;

    /**
     * Factory used to create the target feature and attributes
     */
    protected FeatureFactory attf;

    protected AppSchemaDataAccess store;

    final protected XPath xpathAttributeBuilder;

    protected FilterFactory namespaceAwareFilterFactory;

    /**
     * maxFeatures restriction value as provided by query.  After the data query has run, *this*
     * limit is also applied to the result.
     */
    protected final int requestMaxFeatures;

    /**
     * maximum number of features to request when running the data(base?) query.  For denormalised
     * data sources, this neesd to be be Query.DEFAULT_MAX to trigger a full table scan.  In all
     * other cases it will be the same value as requestMaxFeatures
     */
    protected final int dataMaxFeatures;

    /** counter to ensure maxFeatures is not exceeded */
    protected int featureCounter;

    protected NamespaceSupport namespaces;
    
    protected int resolveDepth;
    
    protected Integer resolveTimeOut;

    /**
     * True if hasNext has been called prior to calling next()
     */
    private boolean hasNextCalled = false;
    
    public AbstractMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query) throws IOException {
        this(store, mapping, query, null);
    }

    public AbstractMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, Query unrolledQuery) throws IOException {
        this(store, mapping, query, unrolledQuery, false);
    }


    //NC - changed
    //possibility to pass on both query and unrolled query
    //so that property names can be taken out of query, also when a custom unrolled query is passed.    
    //one of them can be null, but not both!
    
    public AbstractMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, Query unrolledQuery, boolean removeQueryLimitIfDenormalised) throws IOException {
        this.store = store;
        this.attf = new AppSchemaFeatureFactoryImpl();

        this.mapping = mapping;

        // validate and initialise resolve options
        Hints hints = query.getHints();
        ResolveValueType resolveVal = (ResolveValueType) hints.get( Hints.RESOLVE );
        boolean resolve = ResolveValueType.ALL.equals(resolveVal) || ResolveValueType.LOCAL.equals(resolveVal);
        if (!resolve && resolveVal!=null && !ResolveValueType.NONE.equals(resolveVal)) {
            throw new IllegalArgumentException("Resolve:" + resolveVal.getName() + " is not supported in app-schema!");
        }
        Integer atd = (Integer) hints.get(Hints.ASSOCIATION_TRAVERSAL_DEPTH);
        resolveDepth = resolve ? atd==null? 0 : atd  : 0;
        resolveTimeOut = (Integer) hints.get( Hints.RESOLVE_TIMEOUT );

        namespaces = mapping.getNamespaces();
        namespaceAwareFilterFactory = new FilterFactoryImplNamespaceAware(namespaces);
        
        Object includeProps = query.getHints().get(Query.INCLUDE_MANDATORY_PROPS);
        includeMandatory = includeProps instanceof Boolean && ((Boolean)includeProps).booleanValue();
        
        // NC - property names
        if (query != null && query.getProperties() != null) {
            setPropertyNames(query.getProperties());
        } else {
            setPropertyNames(null); // we need the actual property names (not surrogates) to do
                                    // this...
        }
        
        
        if (mapping.isDenormalised()) {
            // we need to disable the max number of features retrieved so we can
            // sort them manually just in case the data is denormalised.  Do this
            // by overriding the max features for this query just before executing
            // it.  Note that the original maxFeatures value was copied to
            // this.requestMaxFeatures in the constructor and will be re-applied after
            // the rows have been returned
            this.dataMaxFeatures = Query.DEFAULT_MAX;
            if (removeQueryLimitIfDenormalised) {
                this.requestMaxFeatures = Query.DEFAULT_MAX;
            } else {
                this.requestMaxFeatures = query.getMaxFeatures();
            }
        } else {
            this.dataMaxFeatures = query.getMaxFeatures();
            this.requestMaxFeatures = query.getMaxFeatures();
        }
                
        if (unrolledQuery==null) {
            unrolledQuery = getUnrolledQuery(query);
        }
        xpathAttributeBuilder = new XPath();
        xpathAttributeBuilder.setFeatureFactory(attf);
        initialiseSourceFeatures(mapping, unrolledQuery, query.getCoordinateSystemReproject());
        xpathAttributeBuilder.setFilterFactory(namespaceAwareFilterFactory);
    }
    
    //properties can only be set by constructor, before initialising source features 
    //(for joining nested mappings)
    private void setPropertyNames(Collection<PropertyName> propertyNames) {
        selectedProperties = new HashMap<AttributeMapping, List<PropertyName>>();

        if (propertyNames == null) {
            selectedMapping = mapping.getAttributeMappings();
        } else {
            final AttributeDescriptor targetDescriptor = mapping.getTargetFeature();
            selectedMapping = new ArrayList<AttributeMapping>();

            for (AttributeMapping attMapping : mapping.getAttributeMappings()) {
                final StepList targetSteps = attMapping.getTargetXPath();
                boolean alreadyAdded = false;

                if (includeMandatory) {
                    PropertyName targetProp = namespaceAwareFilterFactory.property(targetSteps
                            .toString());
                    Object descr = targetProp.evaluate(targetDescriptor.getType());
                    if (descr instanceof PropertyDescriptor) {
                        if (((PropertyDescriptor) descr).getMinOccurs() >= 1) {
                            selectedMapping.add(attMapping);
                            selectedProperties.put(attMapping, new ArrayList<PropertyName>());
                            alreadyAdded = true;
                        }
                    }
                }

                for (PropertyName requestedProperty : propertyNames) {
                    StepList requestedPropertySteps = requestedProperty.getNamespaceContext() == null ? null
                            : XPath.steps(targetDescriptor, requestedProperty.getPropertyName(),
                                    requestedProperty.getNamespaceContext());
                    if (requestedPropertySteps == null ? AppSchemaDataAccess.matchProperty(
                            requestedProperty.getPropertyName(), targetSteps) : AppSchemaDataAccess
                            .matchProperty(requestedPropertySteps, targetSteps)) {
                        if (!alreadyAdded) {
                            selectedMapping.add(attMapping);
                            selectedProperties.put(attMapping, new ArrayList<PropertyName>());
                            alreadyAdded = true;
                        }
                        if (requestedPropertySteps != null
                                && requestedPropertySteps.size() > targetSteps.size()) {
                            List<PropertyName> pnList = selectedProperties.get(attMapping);
                            StepList subProperty = requestedPropertySteps.subList(
                                    targetSteps.size(), requestedPropertySteps.size());
                            pnList.add(filterFac.property(subProperty.toString(),
                                    requestedProperty.getNamespaceContext()));
                        }
                    }
                }
            }
        }
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
        if (!hasNext()) {
            throw new IllegalStateException("there are no more features in this iterator");
        }

        Feature next;
        try {
            next = computeNext();
        } catch (IOException e) {
            close();
            throw new RuntimeException(e);
        }
        ++featureCounter;        
        
        setHasNextCalled(false);
                
        return next;
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

    private class FeatureFinder implements Runnable {

        private Feature feature = null;

        private String refId;

        private Hints hints;

        public Feature getFeature() {
            return feature;
        }

        public FeatureFinder(String refId, Hints hints) {
            this.refId = refId;
            this.hints = hints;
        }

        @Override
        public void run() {
            try {
                feature = DataAccessRegistry.getInstance().findFeature(new FeatureIdImpl(refId),
                        hints);
            } catch (IOException e) {
                // ignore, no resolve
            }
        }

    };

    protected static String referenceToIdentifier(String reference) {
        
        //TODO: support custom rules in mapping file
        
        String[] urn = reference.split(":");
        
        String lastPart = urn[urn.length - 1];
        
        if (lastPart.contains("#")) {
            lastPart = lastPart.substring(lastPart.lastIndexOf("#"));
        }
        
        if ("missing".equals(urn[urn.length - 1]) || "unknown".equals(urn[urn.length - 1])) {
            return null;
        }
        
        return lastPart;
    }
    
    protected Attribute setAttributeContent(Attribute target, StepList xpath, Object value,
            String id, AttributeType targetNodeType, boolean isXlinkRef,
            Expression sourceExpression, Object source,
            final Map<Name, Expression> clientProperties, boolean ignoreXlinkHref) {
        Attribute instance = null;

        Map<Name, Expression> properties = new HashMap<Name, Expression>(clientProperties);

        if (ignoreXlinkHref) {
            properties.remove(XLINK_HREF_NAME);
        }

        if (properties.containsKey(XLINK_HREF_NAME) && resolveDepth > 0) {
            // local resolve

            String refid = referenceToIdentifier(getValue(properties.get(XLINK_HREF_NAME), source)
                    .toString());

            if (refid != null) {

                final Hints hints = new Hints();
                if (resolveDepth > 1) {
                    hints.put(Hints.RESOLVE, ResolveValueType.ALL);
                    // only the top-level resolve thread should monitor timeout
                    hints.put(Hints.RESOLVE_TIMEOUT, Integer.MAX_VALUE);
                    hints.put(Hints.ASSOCIATION_TRAVERSAL_DEPTH, resolveDepth - 1);
                } else {
                    hints.put(Hints.RESOLVE, ResolveValueType.NONE);
                }

                // let's try finding it
                FeatureFinder finder = new FeatureFinder(refid, hints);
                // this will be null if joining or sleeping is interrupted
                Feature foundFeature = null;
                if (resolveTimeOut == Integer.MAX_VALUE) {
                    // not the top-level resolve thread so do not monitor timeout
                    finder.run();
                    foundFeature = finder.getFeature();
                } else {
                    Thread thread = new Thread(finder);
                    long startTime = System.currentTimeMillis();
                    thread.start();
                    try {
                        while (thread.isAlive()
                                && (System.currentTimeMillis() - startTime) / 1000 < resolveTimeOut) {
                            Thread.sleep(RESOLVE_TIMEOUT_POLL_INTERVAL);
                        }
                        thread.interrupt();
                        // joining ensures synchronisation
                        thread.join();
                        foundFeature = finder.getFeature();
                    } catch (InterruptedException e) {
                        // clean up as best we can
                        thread.interrupt();
                        throw new RuntimeException("Interrupted while resolving resource " + refid);
                    }
                }

                if (foundFeature != null) {
                    // found it
                    instance = xpathAttributeBuilder.set(target, xpath,
                            Collections.singletonList(foundFeature), id, targetNodeType, false,
                            sourceExpression);
                    properties.remove(XLINK_HREF_NAME);
                }
            }

        }

        if (instance == null) {
            instance = xpathAttributeBuilder.set(target, xpath, value, id, targetNodeType, false,
                    sourceExpression);
        }

        setClientProperties(instance, source, properties);

        return instance;
    }
    
    protected void setClientProperties(final Attribute target, final Object source, final Map<Name, Expression> clientProperties) {
        if (target == null) {
            return;
        }
        
        if (source == null && clientProperties.isEmpty()) {
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
					if (!((Collection) propValue).isEmpty()) {
						propValue = ((Collection) propValue).iterator().next();
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

    protected abstract FeatureIterator<? extends Feature> getSourceFeatureIterator();

    protected abstract void initialiseSourceFeatures(FeatureTypeMapping mapping, Query query,
            CoordinateReferenceSystem crs) throws IOException;

    protected abstract boolean unprocessedFeatureExists();

    protected abstract boolean sourceFeatureIteratorHasNext();

    protected abstract boolean isNextSourceFeatureNull();

    protected abstract Feature populateFeatureData(String id) throws IOException;

    protected abstract Object getValue(Expression expression, Object sourceFeature);

    protected abstract boolean isSourceFeatureIteratorNull();
    
    protected abstract Feature computeNext() throws IOException;   

    public abstract boolean hasNext();

    public int getRequestMaxFeatures() {
        return requestMaxFeatures;
    }

    public int getDataMaxFeatures() {
        return dataMaxFeatures;
    }

}
