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

import static org.geotools.data.complex.util.ComplexFeatureConstants.DEFAULT_GEOMETRY_LOCAL_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import net.opengis.wfs20.ResolveValueType;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureFactory;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.appschema.feature.AppSchemaFeatureFactoryImpl;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.complex.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.util.factory.Hints;
import org.geotools.xlink.XLINK;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Base class for several MappingFeatureImplementation's.
 *
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 */
public abstract class AbstractMappingFeatureIterator implements IMappingFeatureIterator {
    /** The logger for the filter module. */
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(AbstractMappingFeatureIterator.class);

    public static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));

    protected FilterFactory filterFac = CommonFactoryFinder.getFilterFactory(null);

    protected FeatureTypeFactory ftf = new ComplexFeatureTypeFactoryImpl();

    /** Name representation of xlink:href */
    public static final Name XLINK_HREF_NAME = Types.toTypeName(XLINK.HREF);

    /** Key value for Attribute userData Map for indicating the presence of a multi value classifier. */
    public static final String MULTI_VALUE_TYPE = "multi_value_type";

    /** Value for Attribute userData Map for indicating an anonymous unbounded sequence classifier. */
    public static final String UNBOUNDED_MULTI_VALUE = "unbounded-multi-value";

    /** Milliseconds between polls of resolver thread. */
    public static final long RESOLVE_TIMEOUT_POLL_INTERVAL = 100;

    /** The mappings for the source and target schemas */
    protected FeatureTypeMapping mapping;

    /** Mappings after Property Selection is applied */
    protected List<AttributeMapping> selectedMapping;

    /** Selected Properties for Feature Chaining */
    protected Map<AttributeMapping, List<PropertyName>> selectedProperties;

    protected boolean includeMandatory;

    /** Factory used to create the target feature and attributes */
    protected FeatureFactory attf;

    protected AppSchemaDataAccess store;

    protected final XPath xpathAttributeBuilder;

    protected FilterFactory namespaceAwareFilterFactory;

    /**
     * maxFeatures restriction value as provided by query. After the data query has run, *this* limit is also applied to
     * the result.
     */
    protected final int requestMaxFeatures;

    /**
     * maximum number of features to request when running the data(base?) query. For denormalised data sources, this
     * neesd to be be Query.DEFAULT_MAX to trigger a full table scan. In all other cases it will be the same value as
     * requestMaxFeatures
     */
    protected final int dataMaxFeatures;

    /** counter to ensure maxFeatures is not exceeded */
    protected int featureCounter;

    protected NamespaceSupport namespaces;

    protected int resolveDepth;

    protected Integer resolveTimeOut;

    protected Transaction transaction;

    protected Query query;

    public Transaction getTransaction() {
        return transaction;
    }

    /** True if hasNext has been called prior to calling next() */
    private boolean hasNextCalled = false;

    public AbstractMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping, Query query)
            throws IOException {
        this(store, mapping, query, null);
    }

    public AbstractMappingFeatureIterator(
            AppSchemaDataAccess store, FeatureTypeMapping mapping, Query query, Query unrolledQuery)
            throws IOException {
        this(store, mapping, query, unrolledQuery, false, false);
    }

    public AbstractMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Query unrolledQuery,
            boolean hasPostFilter)
            throws IOException {
        this(store, mapping, query, unrolledQuery, false, hasPostFilter);
    }

    // NC - changed
    // possibility to pass on both query and unrolled query
    // so that property names can be taken out of query, also when a custom unrolled query is
    // passed.
    // one of them can be null, but not both!
    public AbstractMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Query unrolledQuery,
            boolean removeQueryLimitIfDenormalised,
            boolean hasPostFilter)
            throws IOException {
        this(store, mapping, query, unrolledQuery, removeQueryLimitIfDenormalised, hasPostFilter, null);
    }

    public AbstractMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Query unrolledQuery,
            boolean removeQueryLimitIfDenormalised,
            boolean hasPostFilter,
            Transaction tx)
            throws IOException {
        this.store = store;
        this.attf = new AppSchemaFeatureFactoryImpl();

        this.mapping = mapping;

        this.transaction = tx;

        // validate and initialise resolve options
        Hints hints = query.getHints();
        ResolveValueType resolveVal = (ResolveValueType) hints.get(Hints.RESOLVE);
        boolean resolve = ResolveValueType.ALL.equals(resolveVal) || ResolveValueType.LOCAL.equals(resolveVal);
        if (!resolve && resolveVal != null && !ResolveValueType.NONE.equals(resolveVal)) {
            throw new IllegalArgumentException("Resolve:" + resolveVal.getName() + " is not supported in app-schema!");
        }
        Integer atd = (Integer) hints.get(Hints.ASSOCIATION_TRAVERSAL_DEPTH);
        resolveDepth = resolve ? atd == null ? 0 : atd : 0;
        resolveTimeOut = (Integer) hints.get(Hints.RESOLVE_TIMEOUT);

        namespaces = mapping.getNamespaces();
        namespaceAwareFilterFactory = new FilterFactoryImplNamespaceAware(namespaces);

        Object includeProps = query.getHints().get(Query.INCLUDE_MANDATORY_PROPS);
        includeMandatory = includeProps instanceof Boolean && ((Boolean) includeProps).booleanValue();

        if (mapping.isDenormalised()) {
            // we need to disable the max number of features retrieved so we can
            // sort them manually just in case the data is denormalised.  Do this
            // by overriding the max features for this query just before executing
            // it.  Note that the original maxFeatures value was copied to
            // this.requestMaxFeatures in the constructor and will be re-applied after
            // the rows have been returned
            if (removeQueryLimitIfDenormalised) {
                this.dataMaxFeatures = 1000000;
                if (hasPostFilter) {
                    // true max features will be handled in PostFilteringMappingFeatureIterator
                    this.requestMaxFeatures = 1000000;
                } else {
                    this.requestMaxFeatures = query.getMaxFeatures();
                }
            } else {
                this.dataMaxFeatures = query.getMaxFeatures();
                this.requestMaxFeatures = query.getMaxFeatures();
            }
        } else {
            this.requestMaxFeatures = query.getMaxFeatures();
            this.dataMaxFeatures = query.getMaxFeatures();
        }

        if (unrolledQuery == null) {
            unrolledQuery = getUnrolledQuery(query);
            if (query instanceof JoiningQuery && unrolledQuery instanceof JoiningQuery) {
                ((JoiningQuery) unrolledQuery).setRootMapping(((JoiningQuery) query).getRootMapping());
            }
        }

        // NC - property names
        if (query.getProperties() != null) {
            setPropertyNames(query.getProperties());
        } else {
            setPropertyNames(null); // we need the actual property names (not surrogates) to do
            // this...
        }
        xpathAttributeBuilder = new XPath();
        xpathAttributeBuilder.setFeatureFactory(attf);
        initialiseSourceFeatures(mapping, unrolledQuery, query.getCoordinateSystemReproject());
        xpathAttributeBuilder.setFilterFactory(namespaceAwareFilterFactory);
        this.query = unrolledQuery;
    }

    // properties can only be set by constructor, before initialising source features
    // (for joining nested mappings)
    private void setPropertyNames(Collection<PropertyName> propertyNames) {
        selectedProperties = new HashMap<>();

        if (propertyNames == null) {
            selectedMapping = mapping.getAttributeMappings();
        } else {
            final AttributeDescriptor targetDescriptor = mapping.getTargetFeature();
            selectedMapping = new ArrayList<>();

            for (AttributeMapping attMapping : mapping.getAttributeMappings()) {
                final StepList targetSteps = attMapping.getTargetXPath();
                boolean alreadyAdded = false;

                if (includeMandatory) {
                    PropertyName targetProp = namespaceAwareFilterFactory.property(targetSteps.toString());
                    Object descr = targetProp.evaluate(targetDescriptor.getType());
                    if (descr instanceof PropertyDescriptor) {
                        if (((PropertyDescriptor) descr).getMinOccurs() >= 1) {
                            selectedMapping.add(attMapping);
                            selectedProperties.put(attMapping, new ArrayList<>());
                            alreadyAdded = true;
                        }
                    }
                }

                for (PropertyName requestedProperty : propertyNames) {
                    // replace the artificial DEFAULT_GEOMETRY property with the actual one
                    if (DEFAULT_GEOMETRY_LOCAL_NAME.equals(requestedProperty.getPropertyName())) {
                        String defGeomPath = mapping.getDefaultGeometryXPath();
                        requestedProperty = namespaceAwareFilterFactory.property(defGeomPath);
                    }

                    StepList requestedPropertySteps;
                    if (requestedProperty.getNamespaceContext() == null) {
                        requestedPropertySteps =
                                XPath.steps(targetDescriptor, requestedProperty.getPropertyName(), namespaces);
                    } else {
                        requestedPropertySteps = XPath.steps(
                                targetDescriptor,
                                requestedProperty.getPropertyName(),
                                requestedProperty.getNamespaceContext());
                    }
                    if (requestedPropertySteps == null
                            ? AppSchemaDataAccess.matchProperty(requestedProperty.getPropertyName(), targetSteps)
                            : AppSchemaDataAccess.matchProperty(requestedPropertySteps, targetSteps)) {
                        if (!alreadyAdded) {
                            selectedMapping.add(attMapping);
                            selectedProperties.put(attMapping, new ArrayList<>());
                            alreadyAdded = true;
                        }
                        if (requestedPropertySteps != null && requestedPropertySteps.size() > targetSteps.size()) {
                            List<PropertyName> pnList = selectedProperties.get(attMapping);
                            StepList subProperty =
                                    requestedPropertySteps.subList(targetSteps.size(), requestedPropertySteps.size());
                            pnList.add(filterFac.property(
                                    subProperty.toString(), requestedProperty.getNamespaceContext()));
                        }
                    }
                }
            }
        }
    }

    /** Shall not be called, just throws an UnsupportedOperationException */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /** Closes the underlying FeatureIterator */
    @Override
    public void close() {
        closeSourceFeatures();
    }

    /**
     * Based on the set of xpath expression/id extracting expression, finds the ID for the attribute <code>idExpression
     * </code> from the source complex attribute.
     *
     * @param idExpression the location path of the attribute to be created, for which to obtain the id by evaluating
     *     the corresponding <code>org.geotools.filter.Expression</code> from <code>sourceInstance</code>.
     * @param sourceInstance a complex attribute which is the source of the mapping.
     * @return the ID to be applied to a new attribute instance addressed by <code>attributeXPath
     *     </code>, or <code>null</code> if there is no an id mapping for that attribute.
     */
    protected abstract String extractIdForAttribute(final Expression idExpression, Object sourceInstance);
    /**
     * Return a query appropriate to its underlying feature source.
     *
     * @param query the original query against the output schema
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
    @Override
    public Feature next() {
        boolean hasNext = false;
        try {
            hasNext = hasNext();
        } catch (Throwable e) {
            close();
            throw new RuntimeException(e);
        }

        if (!hasNext) {
            throw new NoSuchElementException("there are no more features in this iterator");
        }

        Feature next;
        try {
            next = computeNext();
        } catch (Throwable e) {
            close();
            throw new RuntimeException(e);
        }
        ++featureCounter;

        setHasNextCalled(false);

        return next;
    }

    protected Map<Name, Expression> getClientProperties(Property attribute) throws DataSourceException {

        Map<Object, Object> userData = attribute.getUserData();
        Map<Name, Expression> clientProperties = new HashMap<>();
        if (userData != null && userData.containsKey(Attributes.class)) {
            @SuppressWarnings("unchecked")
            Map<Name, Expression> props = (Map) userData.get(Attributes.class);
            if (!props.isEmpty()) {
                clientProperties.putAll(props);
            }
        }
        return clientProperties;
    }

    private static class FeatureFinder implements Runnable {

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
                feature = DataAccessRegistry.getInstance().findFeature(new FeatureIdImpl(refId), hints);
            } catch (IOException e) {
                // ignore, no resolve
            }
        }
    }

    protected static String referenceToIdentifier(String reference) {

        // TODO: support custom rules in mapping file

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

    protected Attribute setAttributeContent(
            Attribute target,
            StepList xpath,
            Object value,
            String id,
            AttributeType targetNodeType,
            boolean isXlinkRef,
            Expression sourceExpression,
            Object source,
            final Map<Name, Expression> clientProperties,
            boolean ignoreXlinkHref) {
        Attribute instance = null;

        Map<Name, Expression> properties = new HashMap<>(clientProperties);

        if (ignoreXlinkHref) {
            properties.remove(XLINK_HREF_NAME);
        }

        if (properties.containsKey(XLINK_HREF_NAME) && resolveDepth > 0) {
            // local resolve

            String refid = referenceToIdentifier(
                    getValue(properties.get(XLINK_HREF_NAME), source).toString());

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
                        boolean withinTimeout = false;
                        while (thread.isAlive()
                                && (withinTimeout = (System.currentTimeMillis() - startTime) / 1000 < resolveTimeOut)) {
                            Thread.sleep(RESOLVE_TIMEOUT_POLL_INTERVAL);
                        }
                        // in case of time out, don't even try to get the feature (this
                        // ensures we can write tests where the timeout is guaranteed)
                        if (withinTimeout) {
                            thread.interrupt();
                            // joining ensures synchronisation
                            thread.join();
                            foundFeature = finder.getFeature();
                        }
                    } catch (InterruptedException e) {
                        // clean up as best we can
                        thread.interrupt();
                        throw new RuntimeException("Interrupted while resolving resource " + refid);
                    }
                }

                if (foundFeature != null) {
                    // found it
                    instance = xpathAttributeBuilder.set(
                            target,
                            xpath,
                            Collections.singletonList(foundFeature),
                            id,
                            targetNodeType,
                            false,
                            sourceExpression);
                    properties.remove(XLINK_HREF_NAME);
                }
            }
        }

        if (instance == null) {
            instance = xpathAttributeBuilder.set(target, xpath, value, id, targetNodeType, false, sourceExpression);
        }

        setClientProperties(instance, source, properties);

        return instance;
    }

    protected void setClientProperties(
            final Attribute target, final Object source, final Map<Name, Expression> clientProperties) {
        if (target == null) {
            return;
        }

        if (source == null && clientProperties.isEmpty()) {
            return;
        }

        // NC - first calculate target attributes
        final Map<Name, Object> targetAttributes = new HashMap<>();
        if (target.getUserData().containsValue(Attributes.class)) {
            @SuppressWarnings("unchecked")
            Map<? extends Name, ?> map =
                    (Map<? extends Name, ? extends Object>) target.getUserData().get(Attributes.class);
            targetAttributes.putAll(map);
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
        if (!targetAttributes.isEmpty()) {
            target.getUserData().put(Attributes.class, targetAttributes);
        }

        setGeometryUserData(target, targetAttributes);
    }

    protected void setGeometryUserData(Attribute target, Map<Name, Object> targetAttributes) {
        // with geometry objects, set ID and attributes in geometry object
        if (target instanceof GeometryAttribute && (!targetAttributes.isEmpty() || target.getIdentifier() != null)) {
            Geometry geom;
            if (target.getValue() == null) {
                // create empty geometry if null but attributes
                geom = GEOMETRY_FACTORY.createGeometryCollection();
            } else {
                // need to clone because it seems the same geometry object from the
                // db is reused instead of regenerated if different attributes refer
                // to the same database row... so if we change the userData, we have
                // to clone it
                geom = ((Geometry) target.getValue()).copy();
            }

            if (geom != null) {

                Object userData = geom.getUserData();
                Map<Object, Object> newUserData = new HashMap<>();
                if (userData != null) {
                    if (userData instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<Object, Object> map = (Map) userData;
                        newUserData.putAll(map);
                    } else if (userData instanceof CoordinateReferenceSystem) {
                        newUserData.put(CoordinateReferenceSystem.class, userData);
                    }
                }
                // set gml:id and attributes in Geometry userData
                if (target.getIdentifier() != null) {
                    newUserData.put("gml:id", target.getIdentifier().toString());
                }
                if (!targetAttributes.isEmpty()) {
                    newUserData.put(Attributes.class, targetAttributes);
                }

                geom.setUserData(newUserData);
                target.setValue(geom);
            }
        }
    }

    protected abstract void closeSourceFeatures();

    protected abstract FeatureIterator<? extends Feature> getSourceFeatureIterator();

    protected abstract void initialiseSourceFeatures(
            FeatureTypeMapping mapping, Query query, CoordinateReferenceSystem crs) throws IOException;

    protected abstract boolean unprocessedFeatureExists();

    protected abstract boolean sourceFeatureIteratorHasNext();

    protected abstract boolean isNextSourceFeatureNull();

    protected abstract Feature populateFeatureData(String id) throws IOException;

    protected abstract Object getValue(Expression expression, Object sourceFeature);

    protected abstract boolean isSourceFeatureIteratorNull();

    protected abstract Feature computeNext() throws IOException;

    @Override
    public abstract boolean hasNext();
}
