/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.appschema.feature.AppSchemaAttributeBuilder;
import org.geotools.appschema.jdbc.JoiningJDBCFeatureSource;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator.ComplexNameImpl;
import org.geotools.data.complex.config.JdbcMultipleValue;
import org.geotools.data.complex.config.MultipleValue;
import org.geotools.data.complex.config.NonFeatureTypeProxy;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.data.complex.util.XPathUtil.Step;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.data.joining.JoiningNestedAttributeMapping;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.referencing.CRS;
import org.xml.sax.Attributes;

/**
 * A Feature iterator that operates over the FeatureSource of a
 * {@linkplain org.geotools.data.complex.FeatureTypeMapping} and produces Features of the output schema by applying the
 * mapping rules to the Features of the source schema.
 *
 * <p>This iterator acts like a one-to-one mapping, producing a Feature of the target type for each feature of the
 * source type.
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @author Russell Petty (GeoScience Victoria)
 * @since 2.4
 */
public class DataAccessMappingFeatureIterator extends AbstractMappingFeatureIterator {

    /** Hold on to iterator to allow features to be streamed. */
    private FeatureIterator<? extends Feature> sourceFeatureIterator;

    /** Reprojected CRS from the source simple features, or null */
    protected CoordinateReferenceSystem reprojection;

    /** This is the feature that will be processed in next() */
    protected Feature curSrcFeature;

    protected FeatureSource<? extends FeatureType, ? extends Feature> mappedSource;

    protected FeatureCollection<? extends FeatureType, ? extends Feature> sourceFeatures;

    protected List<Expression> foreignIds;

    protected AttributeDescriptor targetFeature;

    private Map<JdbcMultipleValue, Map<Object, List<MultiValueContainer>>> jdbcMultiValues = new HashMap<>();

    /**
     * True if joining is turned off and pre filter exists. There's a need to run extra query to get features by id
     * because they might come from denormalised view. The rows might not match the filter therefore doesn't exist in
     * the mapped source but match the id of other rows.
     */
    private boolean isFiltered;

    private ArrayList<String> filteredFeatures;
    /** Temporary/experimental changes for enabling subsetting for isList only. */
    private Filter listFilter;

    private boolean isTransactionOwner;

    public boolean isTransactionOwner() {
        return isTransactionOwner;
    }

    public DataAccessMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            boolean isFiltered,
            boolean removeQueryLimitIfDenormalised)
            throws IOException {
        this(store, mapping, query, isFiltered, removeQueryLimitIfDenormalised, false);
    }

    public DataAccessMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            boolean isFiltered,
            boolean removeQueryLimitIfDenormalised,
            boolean hasPostFilter)
            throws IOException {
        this(store, mapping, query, isFiltered, removeQueryLimitIfDenormalised, hasPostFilter, null);
    }

    public DataAccessMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            boolean isFiltered,
            boolean removeQueryLimitIfDenormalised,
            boolean hasPostFilter,
            Transaction transaction)
            throws IOException {
        super(store, mapping, query, null, removeQueryLimitIfDenormalised, hasPostFilter, transaction);
        this.isFiltered = isFiltered;
        if (isFiltered) {
            filteredFeatures = new ArrayList<>();
        }
    }

    public DataAccessMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping, Query query)
            throws IOException {
        this(store, mapping, query, null, false);
    }

    /**
     * @param mapping place holder for the target type, the surrogate FeatureSource and the mappings between them.
     * @param query the query over the target feature type, that is to be unpacked to its equivalent over the surrogate
     *     feature type.
     */
    public DataAccessMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Query unrolledQuery,
            boolean removeQueryLimitIfDenormalised)
            throws IOException {
        this(store, mapping, query, unrolledQuery, removeQueryLimitIfDenormalised, null);
    }

    public DataAccessMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Query unrolledQuery,
            boolean removeQueryLimitIfDenormalised,
            Transaction transaction)
            throws IOException {
        super(store, mapping, query, unrolledQuery, removeQueryLimitIfDenormalised, false, transaction);
    }

    @Override
    public boolean hasNext() {
        boolean exists = !isNextSourceFeatureNull();

        if (!isHasNextCalled()) {
            if (featureCounter < requestMaxFeatures) {
                if (!exists
                        && getSourceFeatureIterator() != null
                        && getSourceFeatureIterator().hasNext()) {
                    this.curSrcFeature = getSourceFeatureIterator().next();
                    exists = true;
                }
                if (exists && filteredFeatures != null) {
                    // get the next one if this row has already been added to the target
                    // feature from setNextFilteredFeature
                    while (exists && filteredFeatures.contains(extractIdForFeature(this.curSrcFeature))) {
                        if (getSourceFeatureIterator() != null
                                && getSourceFeatureIterator().hasNext()) {
                            this.curSrcFeature = getSourceFeatureIterator().next();
                            exists = true;
                        } else {
                            exists = false;
                        }
                    }
                }
                // HACK HACK HACK
                // evaluate filter that applies to this list as we want a subset
                // instead of full result
                // this is a temporary solution for Bureau of Meteorology
                // requirement for timePositionList
                if (listFilter != null) {
                    while (exists && !listFilter.evaluate(curSrcFeature)) {
                        // only add to subset if filter matches value
                        if (getSourceFeatureIterator() != null
                                && getSourceFeatureIterator().hasNext()) {
                            this.curSrcFeature = getSourceFeatureIterator().next();
                            exists = true;
                        } else {
                            exists = false;
                        }
                    }
                }
                // END OF HACK
            } else {
                exists = false;
            }
        }

        if (!exists) {
            LOGGER.finest("no more features, produced " + featureCounter);
            close();
            curSrcFeature = null;
        }

        setHasNextCalled(true);

        return exists;
    }

    @Override
    protected FeatureIterator<? extends Feature> getSourceFeatureIterator() {
        return sourceFeatureIterator;
    }

    @Override
    protected boolean isSourceFeatureIteratorNull() {
        return getSourceFeatureIterator() == null;
    }

    protected Object peekValue(Object source, Expression prop) {
        Object o = prop.evaluate(source);
        if (o instanceof Attribute) {
            o = ((Attribute) o).getValue();
        }
        return o;
    }

    public Object peekNextValue(Expression prop) {
        return peekValue(curSrcFeature, prop);
    }

    /**
     * Only used for Joining, to make sure that rows with different foreign id's aren't interpreted as one feature and
     * merged.
     */
    public void setForeignIds(List<Expression> ids) {
        foreignIds = ids;
    }

    /**
     * Only used for Joining, to make sure that rows with different foreign id's aren't interpreted as one feature and
     * merged.
     */
    public List<Object> getForeignIdValues(Object source) {
        if (foreignIds != null) {
            List<Object> foreignIdValues = new ArrayList<>();
            for (int i = 0; i < foreignIds.size(); i++) {
                foreignIdValues.add(i, peekValue(source, foreignIds.get(i)));
            }
            return foreignIdValues;
        }
        return null;
    }

    /**
     * Only used for Joining, to make sure that rows with different foreign id's aren't interpreted as one feature and
     * merged.
     */
    protected boolean checkForeignIdValues(List<Object> foreignIdValues, Feature next) {
        if (foreignIds != null) {
            for (int i = 0; i < foreignIds.size(); i++) {
                if (!peekValue(next, foreignIds.get(i))
                        .toString()
                        .equals(foreignIdValues.get(i).toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Only used for Joining, to make sure that rows with different foreign id's aren't interpreted as one feature and
     * merged.
     */
    public List<Object> getIdValues(Object source) {
        List<Object> ids = new ArrayList<>();
        Expression idExpression = mapping.getFeatureIdExpression();
        if (Expression.NIL.equals(idExpression) || idExpression instanceof Literal) {
            // GEOT-4554: if idExpression is not specified, should use PK
            if (source instanceof Feature) {
                for (Property p : ((Feature) source).getProperties()) {
                    if (p.getName().getLocalPart().startsWith(JoiningJDBCFeatureSource.PRIMARY_KEY)) {
                        ids.add(p.getValue());
                    }
                }
            }
        } else {
            FilterAttributeExtractor extractor = new FilterAttributeExtractor();
            idExpression.accept(extractor, null);
            for (String att : extractor.getAttributeNameSet()) {
                ids.add(peekValue(source, namespaceAwareFilterFactory.property(att)));
            }
        }
        if (foreignIds != null) {
            ids.addAll(getForeignIdValues(source));
        }
        return ids;
    }

    /**
     * Only used for Joining, to make sure that rows with different foreign id's aren't interpreted as one feature and
     * merged.
     */
    public boolean checkForeignIdValues(List<Object> foreignIdValues) {
        return checkForeignIdValues(foreignIdValues, curSrcFeature);
    }

    @Override
    protected void initialiseSourceFeatures(
            FeatureTypeMapping mapping, Query query, CoordinateReferenceSystem targetCRS) throws IOException {
        mappedSource = mapping.getSource();

        // NC - joining query
        if (query instanceof JoiningQuery) {
            JoiningJDBCFeatureSource joiningJdbcFS = null;
            if (mappedSource instanceof JDBCFeatureSource) {
                joiningJdbcFS = new JoiningJDBCFeatureSource((JDBCFeatureSource) mappedSource);
            } else if (mappedSource instanceof JDBCFeatureStore) {
                joiningJdbcFS = new JoiningJDBCFeatureSource((JDBCFeatureStore) mappedSource);
            } else {
                throw new IllegalArgumentException("Joining queries are only supported on JDBC data stores");
            }

            // we have a database backend and Joining is enabled: make sure a transaction is
            // available
            // to allow downstream iterators (i.e. iterators going through nested features) to share
            // the
            // connection stored in the transaction's state (note that this is possible only if the
            // source data store of the nested feature types is the same as their parent's).
            if (this.transaction != null) {
                // transaction provided by calling code: iterator shall not consider itself owner of
                // the
                // transaction and hence shall not close it when done
                this.isTransactionOwner = false;
            } else {
                this.transaction = new DefaultTransaction();
                // iterator owns the transaction it has created and hence shall close it when done
                this.isTransactionOwner = true;
            }

            joiningJdbcFS.setTransaction(transaction);
            mappedSource = joiningJdbcFS;
        }
        String version =
                (String) this.mapping.getTargetFeature().getType().getUserData().get("targetVersion");
        // might be because top level feature has no geometry
        // GEOT-4550: exclude this part for WMS requests because the reprojection happens during
        // rendering
        // not at ReprojectingFilterVisitor.
        // The original CRS should be preserved so the reprojection could happen at rendering.
        if (targetCRS == null && version != null && !version.contains("wms")) {
            // figure out the crs the data is in
            CoordinateReferenceSystem crs = null;
            try {
                crs = this.mappedSource.getSchema().getCoordinateReferenceSystem();
            } catch (UnsupportedOperationException e) {
                // do nothing as mappedSource is a WSFeatureSource
            }
            // gather declared CRS
            CoordinateReferenceSystem declaredCRS = this.getDeclaredCrs(crs, version);
            CoordinateReferenceSystem target;
            Object crsobject =
                    this.mapping.getTargetFeature().getType().getUserData().get("targetCrs");
            if (crsobject instanceof CoordinateReferenceSystem) {
                target = (CoordinateReferenceSystem) crsobject;
            } else if (crsobject instanceof URI) {

                URI uri = (URI) crsobject;
                if (uri != null) {
                    try {
                        target = CRS.decode(uri.toString());
                    } catch (Exception e) {
                        String msg = "Unable to support srsName: " + uri;
                        throw new UnsupportedOperationException(msg, e);
                    }
                } else {
                    target = declaredCRS;
                }
            } else {
                target = declaredCRS;
            }
            this.reprojection = target;

        } else {
            this.reprojection = targetCRS;
        }

        // clean up user data related to request
        mapping.getTargetFeature().getType().getUserData().put("targetVersion", null);
        mapping.getTargetFeature().getType().getUserData().put("targetCrs", null);

        // reproject target feature
        targetFeature = reprojectAttribute(mapping.getTargetFeature());
        query.setMaxFeatures(dataMaxFeatures);
        sourceFeatures = mappedSource.getFeatures(query);
        if (reprojection != null) {
            xpathAttributeBuilder.setCRS(reprojection);
            if (sourceFeatures.getSchema().getGeometryDescriptor() == null
                    || this.isReprojectionCrsEqual(
                            this.mappedSource.getSchema().getCoordinateReferenceSystem(), this.reprojection)) {
                // VT: No point trying to re-project without any geometry.
                query.setCoordinateSystemReproject(null);
            }
        }
        if (!(this instanceof XmlMappingFeatureIterator)) {
            this.sourceFeatureIterator = sourceFeatures.features();
        }

        // NC - joining nested atts
        for (AttributeMapping attMapping : selectedMapping) {

            if (attMapping instanceof JoiningNestedAttributeMapping) {
                ((JoiningNestedAttributeMapping) attMapping).open(this, query, mapping);
            }
        }
    }

    @Override
    protected boolean unprocessedFeatureExists() {

        boolean exists = getSourceFeatureIterator().hasNext();
        if (exists && this.curSrcFeature == null) {
            this.curSrcFeature = getSourceFeatureIterator().next();
        }

        return exists;
    }

    protected String extractIdForFeature(Feature feature) {
        if (mapping.getFeatureIdExpression().equals(Expression.NIL)) {
            if (feature.getIdentifier() == null) {
                return null;
            } else {
                return feature.getIdentifier().getID();
            }
        }
        return mapping.getFeatureIdExpression().evaluate(feature, String.class);
    }

    @Override
    protected String extractIdForAttribute(final Expression idExpression, Object sourceInstance) {
        String value = idExpression.evaluate(sourceInstance, String.class);
        return value;
    }

    @Override
    protected boolean isNextSourceFeatureNull() {
        return curSrcFeature == null;
    }

    @Override
    protected boolean sourceFeatureIteratorHasNext() {
        return getSourceFeatureIterator().hasNext();
    }

    protected Object getValues(boolean isMultiValued, Expression expression, Object sourceFeatureInput) {
        if (isMultiValued
                && sourceFeatureInput instanceof FeatureImpl
                && expression instanceof AttributeExpressionImpl) {
            // RA: Feature Chaining
            // complex features can have multiple nodes of the same attribute.. and if they are used
            // as input to an app-schema data access to be nested inside another feature type of a
            // different XML type, it has to be mapped like this:
            // <AttributeMapping>
            // <targetAttribute>
            // gsml:composition
            // </targetAttribute>
            // <sourceExpression>
            // <inputAttribute>mo:composition</inputAttribute>
            // <linkElement>gsml:CompositionPart</linkElement>
            // <linkField>gml:name</linkField>
            // </sourceExpression>
            // <isMultiple>true</isMultiple>
            // </AttributeMapping>
            // As there can be multiple nodes of mo:composition in this case, we need to retrieve
            // all of them
            AttributeExpressionImpl attribExpression = (AttributeExpressionImpl) expression;
            String xpath = attribExpression.getPropertyName();
            ComplexAttribute sourceFeature = (ComplexAttribute) sourceFeatureInput;
            StepList xpathSteps = XPath.steps(sourceFeature.getDescriptor(), xpath, namespaces);
            return getProperties(sourceFeature, xpathSteps);
        }
        return expression.evaluate(sourceFeatureInput);
    }

    /**
     * Sets the values of grouping attributes.
     *
     * @return Feature. Target feature sets with simple attributes
     */
    protected Attribute setAttributeValue(
            Attribute target,
            String id,
            final Object source,
            final AttributeMapping attMapping,
            Object values,
            StepList inputXpath,
            List<PropertyName> selectedProperties)
            throws IOException {

        final Expression sourceExpression = attMapping.getSourceExpression();
        final AttributeType targetNodeType = attMapping.getTargetNodeInstance();
        StepList xpath = inputXpath == null ? attMapping.getTargetXPath().clone() : inputXpath;

        Map<Name, Expression> clientPropsMappings = attMapping.getClientProperties();
        boolean isNestedFeature = attMapping.isNestedAttribute();

        if (id == null && Expression.NIL != attMapping.getIdentifierExpression()) {
            id = extractIdForAttribute(attMapping.getIdentifierExpression(), source);
        }
        if (attMapping.isNestedAttribute()) {
            NestedAttributeMapping nestedMapping = (NestedAttributeMapping) attMapping;
            Object mappingName = nestedMapping.getNestedFeatureType(source);
            if (mappingName != null) {
                if (nestedMapping.isSameSource() && mappingName instanceof Name) {
                    // data type polymorphism mapping
                    return setPolymorphicValues(
                            (Name) mappingName, target, id, nestedMapping, source, xpath, clientPropsMappings);
                } else if (mappingName instanceof String) {
                    // referential polymorphism mapping
                    if (attMapping instanceof JoiningNestedAttributeMapping) {
                        // GEOT-4417: update skipped ids when skipping with
                        // toXlinkHref
                        if (values == null && source != null) {
                            values = getValues(attMapping.isMultiValued(), sourceExpression, source);
                        }
                        if (values != null) {
                            List<Object> idValues = getIdValues(source);
                            if (values instanceof Collection) {
                                for (Object singleVal : (Collection) values) {
                                    ((JoiningNestedAttributeMapping) attMapping).skip(this, singleVal, idValues);
                                }
                            } else {
                                ((JoiningNestedAttributeMapping) attMapping).skip(this, values, idValues);
                            }
                        }
                    }
                    return setPolymorphicReference(
                            (String) mappingName, clientPropsMappings, target, xpath, targetNodeType);
                }
            } else {
                // polymorphism could result in null, to skip the attribute
                return null;
            }
        }
        if (source instanceof Feature && attMapping.isMultiValued() && attMapping.getMultipleValue() != null) {
            // extract the multiple value for the current multiple values attributes
            values = extractMultipleValues((Feature) source, attMapping);
        } else if (values == null && source != null) {
            values = getValues(attMapping.isMultiValued(), sourceExpression, source);
        }
        boolean isHRefLink = isByReference(clientPropsMappings, isNestedFeature);
        int newResolveDepth = resolveDepth;
        // if resolving, no xlink:href for chained feature
        boolean ignoreXlinkHref = false;
        if (isHRefLink && newResolveDepth > 0) {
            isHRefLink = false;
            newResolveDepth--;
            ignoreXlinkHref = true;
        }
        if (isNestedFeature) {
            if (values instanceof Collection) {
                ArrayList<Attribute> nestedFeatures = new ArrayList<>(((Collection) values).size());
                for (Object val : (Collection) values) {
                    if (val instanceof Attribute) {
                        val = ((Attribute) val).getValue();
                        if (val instanceof Collection) {
                            val = ((Collection) val).iterator().next();
                        }
                        while (val instanceof Attribute) {
                            val = ((Attribute) val).getValue();
                        }
                    }
                    if (isHRefLink) {
                        // get the input features to avoid infinite loop in case the nested
                        // feature type also have a reference back to this type
                        // eg. gsml:GeologicUnit/gsml:occurence/gsml:MappedFeature
                        // and gsml:MappedFeature/gsml:specification/gsml:GeologicUnit
                        nestedFeatures.addAll(((NestedAttributeMapping) attMapping)
                                .getInputFeatures(
                                        this,
                                        val,
                                        getIdValues(source),
                                        source,
                                        reprojection,
                                        selectedProperties,
                                        includeMandatory));
                    } else {
                        nestedFeatures.addAll(((NestedAttributeMapping) attMapping)
                                .getFeatures(
                                        this,
                                        val,
                                        getIdValues(source),
                                        reprojection,
                                        source,
                                        selectedProperties,
                                        includeMandatory,
                                        newResolveDepth,
                                        resolveTimeOut));
                    }
                }
                values = nestedFeatures;
            } else if (isHRefLink) {
                // get the input features to avoid infinite loop in case the nested
                // feature type also have a reference back to this type
                // eg. gsml:GeologicUnit/gsml:occurence/gsml:MappedFeature
                // and gsml:MappedFeature/gsml:specification/gsml:GeologicUnit
                values = ((NestedAttributeMapping) attMapping)
                        .getInputFeatures(
                                this,
                                values,
                                getIdValues(source),
                                source,
                                reprojection,
                                selectedProperties,
                                includeMandatory);
            } else {
                values = ((NestedAttributeMapping) attMapping)
                        .getFeatures(
                                this,
                                values,
                                getIdValues(source),
                                reprojection,
                                source,
                                selectedProperties,
                                includeMandatory,
                                newResolveDepth,
                                resolveTimeOut);
            }
            if (isHRefLink) {
                // only need to set the href link value, not the nested feature properties
                setXlinkReference(target, clientPropsMappings, values, xpath, targetNodeType);
                return null;
            }
        }
        Attribute instance = null;
        // check if is a MultiValue inner element
        if (isMultiElement(values, clientPropsMappings)) {
            // It is a multiValue inner element, so process it
            generateInnerElementMultiValue(target, values, targetNodeType, xpath, clientPropsMappings);
        } else if (values instanceof Collection) {
            // nested feature type could have multiple instances as the whole purpose
            // of feature chaining is to cater for multi-valued properties
            @SuppressWarnings("unchecked")
            Collection<Object> cobjs = (Collection<Object>) values;
            for (Object singleVal : cobjs) {
                ArrayList<Object> valueList = new ArrayList<>();
                Map<Name, Expression> clientProperties = clientPropsMappings;
                if (!isNestedFeature) {
                    if (singleVal instanceof Attribute) {
                        singleVal = ((Attribute) singleVal).getValue();
                    }
                    if (singleVal instanceof Collection) {
                        @SuppressWarnings("unchecked")
                        Collection<Object> collection = (Collection) singleVal;
                        valueList.addAll(collection);
                    }
                    if (singleVal instanceof MultiValueContainer) {
                        MultiValueContainer multiValue = (MultiValueContainer) singleVal;
                        valueList.add(multiValue.value);
                        clientProperties = MultiValueContainer.resolve(filterFac, multiValue, clientProperties);
                    } else {
                        valueList.add(singleVal);
                    }
                } else {
                    valueList.add(singleVal);
                }
                instance = setAttributeContent(
                        target,
                        xpath,
                        valueList,
                        id,
                        targetNodeType,
                        false,
                        sourceExpression,
                        source,
                        clientProperties,
                        ignoreXlinkHref);
            }
        } else {
            if (values instanceof Attribute) {
                // copy client properties from input features if they're complex features
                // wrapped in app-schema data access
                Map<Name, Expression> newClientProps = getClientProperties((Attribute) values);
                if (!newClientProps.isEmpty()) {
                    newClientProps.putAll(clientPropsMappings);
                    clientPropsMappings = newClientProps;
                }
                values = ((Attribute) values).getValue();
            }

            instance = setAttributeContent(
                    target,
                    xpath,
                    values,
                    id,
                    targetNodeType,
                    false,
                    sourceExpression,
                    source,
                    cleanFromAnonymousAttribute(clientPropsMappings),
                    ignoreXlinkHref);
        }
        if (attMapping.encodeIfEmpty()) {
            if (instance == null) {
                instance = setAttributeContent(
                        target,
                        xpath,
                        values,
                        id,
                        targetNodeType,
                        false,
                        sourceExpression,
                        source,
                        clientPropsMappings,
                        ignoreXlinkHref);
            }
            instance.getDescriptor().getUserData().put("encodeIfEmpty", attMapping.encodeIfEmpty());
        }
        return instance;
    }

    private Map<Name, Expression> cleanFromAnonymousAttribute(Map<Name, Expression> clientProps) {
        return clientProps.entrySet().stream()
                .filter(e -> !(e.getKey() instanceof ComplexNameImpl))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private void generateInnerElementMultiValue(
            Attribute target,
            Object values,
            final AttributeType targetNodeType,
            StepList xpath,
            Map<Name, Expression> clientPropsMappings) {
        @SuppressWarnings("unchecked")
        final Collection<MultiValueContainer> multiValues = (Collection) values;
        // generate the parent attribute
        final Attribute parentAttribute =
                xpathAttributeBuilder.set(target, xpath, null, null, targetNodeType, false, null);
        // add a metadata for unbounded sequences
        final boolean allComplexNames =
                clientPropsMappings.entrySet().stream().allMatch(e -> e.getKey() instanceof ComplexNameImpl);
        if (!multiValues.isEmpty() && !clientPropsMappings.isEmpty() && allComplexNames) {
            parentAttribute.getUserData().put(MULTI_VALUE_TYPE, UNBOUNDED_MULTI_VALUE);
        }
        // generate every child attributes
        for (MultiValueContainer mv : multiValues) {
            Map<Name, Expression> clientProperties = clientPropsMappings;
            clientProperties = MultiValueContainer.resolve(filterFac, mv, clientProperties);
            for (Entry<Name, Expression> entry : clientProperties.entrySet()) {
                // create new xpath
                final Step newStep = new Step(
                        new QName(
                                entry.getKey().getNamespaceURI(),
                                entry.getKey().getLocalPart(),
                                xpath.get(0).getName().getPrefix()),
                        xpath.size() + 1);
                final StepList slist = xpath.clone();
                slist.add(newStep);
                xpathAttributeBuilder.set(
                        parentAttribute, slist, entry.getValue(), null, targetNodeType, false, entry.getValue());
            }
        }
    }

    /**
     * Determines if it's a case of MultiValue Inner Elements.
     *
     * @param values values to evaluate
     * @param clientPropsMappings client mappings
     * @return true if validation passes
     */
    private boolean isMultiElement(Object values, final Map<Name, Expression> clientPropsMappings) {
        // values needs to be a collection
        if (!(values instanceof Collection)) return false;
        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) values;
        // values should be not-empty and MultiValueContainer instances
        if (collection.isEmpty() || !collection.stream().allMatch(o -> o instanceof MultiValueContainer)) return false;
        final List<Entry<Name, Expression>> expressionEntryList = collection.stream()
                .map(o -> (MultiValueContainer) o)
                .flatMap(m -> {
                    Map<Name, Expression> clientProperties = clientPropsMappings;
                    clientProperties = MultiValueContainer.resolve(filterFac, m, clientProperties);
                    return clientProperties.entrySet().stream();
                })
                .collect(Collectors.toList());
        if (expressionEntryList.isEmpty()) return false;
        return expressionEntryList.stream().allMatch(y -> y.getKey() instanceof ComplexNameImpl);
    }

    /** Helper class that holds the relations between a feature and multiple value element. */
    private static final class MultiValueContainer {

        final Feature feature;
        final Object value;

        MultiValueContainer(Feature feature, Object value) {
            this.feature = feature;
            this.value = value;
        }

        static List<MultiValueContainer> toList(Feature feature, List<Object> values) {
            List<MultiValueContainer> list = new ArrayList<>();
            for (Object value : values) {
                list.add(new MultiValueContainer(feature, value));
            }
            return list;
        }

        /** Updates the client properties associated with an attribute mapping that has a multivalued value. */
        static Map<Name, Expression> resolve(
                FilterFactory filterFactory, MultiValueContainer multiValue, Map<Name, Expression> properties) {
            Map<Name, Expression> resolved = new HashMap<>();
            for (Map.Entry<Name, Expression> entry : properties.entrySet()) {
                Name key = entry.getKey();
                Expression expression = entry.getValue();
                Object value = expression.evaluate(multiValue.feature);
                resolved.put(key, filterFactory.literal(value));
            }
            return resolved;
        }
    }

    /** Helper method that gets the values associated with multivalued mapping. */
    private List<MultiValueContainer> extractMultipleValues(Feature sourceFeature, AttributeMapping attributeMapping)
            throws IOException {
        MultipleValue multipleValue = attributeMapping.getMultipleValue();
        if (!(multipleValue instanceof JdbcMultipleValue)) {
            // extension point for multiple values support (e.g. Solr)
            List<Object> values = multipleValue.getValues(sourceFeature, attributeMapping);
            return MultiValueContainer.toList(sourceFeature, values);
        }
        // jdbc multiple values are explicitly handled
        JdbcMultipleValue jdbcMultipleValue = (JdbcMultipleValue) multipleValue;
        // let's see if we have the multiple values already in cache
        Map<Object, List<MultiValueContainer>> candidates = jdbcMultiValues.get(jdbcMultipleValue);
        if (candidates == null) {
            candidates = new HashMap<>();
            // we need to get the values from the jdbc based data source
            if (!(mappedSource instanceof JoiningJDBCFeatureSource)) {
                // ouch, this should a jdbc based data source
                throw new RuntimeException(String.format(
                        "JDBC multi values only work with JDBC based data sources, got '%s'.", mappedSource.getName()));
            }
            JoiningJDBCFeatureSource jdbcDataSource = (JoiningJDBCFeatureSource) mappedSource;
            // query the multiple values
            try (FeatureReader<SimpleFeatureType, SimpleFeature> featuresReader =
                    jdbcDataSource.getJoiningReaderInternal(jdbcMultipleValue, (JoiningQuery) this.query)) {
                // read and cache the multiple values obtained
                while (featuresReader.hasNext()) {
                    SimpleFeature readFeature = featuresReader.next();
                    // get the read feature foreign key associated value
                    Object targetColumnValue = readFeature
                            .getProperty(jdbcMultipleValue.getTargetColumn())
                            .getValue();
                    List<MultiValueContainer> candidatesValues = candidates.get(targetColumnValue);
                    if (candidatesValues == null) {
                        // no values yet for the current foreign key value
                        candidatesValues = new ArrayList<>();
                        candidates.put(targetColumnValue, candidatesValues);
                    }
                    Object targetValue = jdbcMultipleValue.getTargetValue() != null
                            ? jdbcMultipleValue.getTargetValue().evaluate(readFeature)
                            : null;
                    candidatesValues.add(new MultiValueContainer(readFeature, targetValue));
                }
            }
            jdbcMultiValues.put(jdbcMultipleValue, candidates);
        }
        // get the multiple values for the current jdbc multiple values attribute
        Object sourceColumnValue =
                sourceFeature.getProperty(jdbcMultipleValue.getSourceColumn()).getValue();

        List<MultiValueContainer> list = candidates.get(sourceColumnValue);
        // make sure we never return null, instead return an empty list
        return list != null ? list : Collections.emptyList();
    }

    /**
     * Special handling for polymorphic mapping where the value of the attribute determines that this attribute should
     * be a placeholder for an xlink:href.
     *
     * @param uri the xlink:href URI
     * @param clientPropsMappings client properties
     * @param target the complex feature being built
     * @param xpath the xpath of attribute
     * @param targetNodeType the type of the attribute to be cast to, if any
     */
    private Attribute setPolymorphicReference(
            String uri,
            Map<Name, Expression> clientPropsMappings,
            Attribute target,
            StepList xpath,
            AttributeType targetNodeType) {

        if (uri != null) {
            Attribute instance = xpathAttributeBuilder.set(target, xpath, null, "", targetNodeType, true, null);
            Map<Name, Expression> newClientProps = new HashMap<>();
            newClientProps.putAll(clientPropsMappings);
            newClientProps.put(XLINK_HREF_NAME, namespaceAwareFilterFactory.literal(uri));
            setClientProperties(instance, null, newClientProps);
            return instance;
        }
        return null;
    }

    /**
     * Special handling for polymorphic mapping. Works out the polymorphic type name by evaluating the function on the
     * feature, then set the relevant sub-type values.
     *
     * @param target The target feature to be encoded
     * @param id The target feature id
     * @param nestedMapping The mapping that is polymorphic
     * @param source The source simple feature
     * @param xpath The xpath of polymorphic type
     * @param clientPropsMappings Client properties
     */
    private Attribute setPolymorphicValues(
            Name mappingName,
            Attribute target,
            String id,
            NestedAttributeMapping nestedMapping,
            Object source,
            StepList xpath,
            Map<Name, Expression> clientPropsMappings)
            throws IOException {
        // process sub-type mapping
        DataAccess<FeatureType, Feature> da = DataAccessRegistry.getDataAccess(mappingName);
        if (da instanceof AppSchemaDataAccess) {
            // why wouldn't it be? check just to be safe
            FeatureTypeMapping fTypeMapping = ((AppSchemaDataAccess) da).getMappingByName(mappingName);
            List<AttributeMapping> polymorphicMappings = fTypeMapping.getAttributeMappings();
            AttributeDescriptor attDescriptor = fTypeMapping.getTargetFeature();
            AttributeType type = attDescriptor.getType();
            Name polymorphicTypeName = attDescriptor.getName();
            StepList prefixedXpath = xpath.clone();
            prefixedXpath.add(new Step(
                    new QName(
                            polymorphicTypeName.getNamespaceURI(),
                            polymorphicTypeName.getLocalPart(),
                            this.namespaces.getPrefix(polymorphicTypeName.getNamespaceURI())),
                    1));
            if (!fTypeMapping.getFeatureIdExpression().equals(Expression.NIL)) {
                id = fTypeMapping.getFeatureIdExpression().evaluate(source, String.class);
            }
            Attribute instance =
                    xpathAttributeBuilder.set(target, prefixedXpath, null, id, type, false, attDescriptor, null);
            setClientProperties(instance, source, clientPropsMappings);
            for (AttributeMapping mapping : polymorphicMappings) {
                if (skipTopElement(polymorphicTypeName, mapping, type)) {
                    // if the top level mapping for the Feature itself, the attribute instance
                    // has already been created.. just need to set the client properties
                    setClientProperties(instance, source, mapping.getClientProperties());
                    continue;
                }
                setAttributeValue(instance, null, source, mapping, null, null, selectedProperties.get(mapping));
            }
            return instance;
        }
        return null;
    }

    /**
     * Set xlink:href client property for multi-valued chained features. This has to be specially handled because we
     * don't want to encode the nested features attributes, since it's already an xLink. Also we need to eliminate
     * duplicates.
     *
     * @param target The target attribute
     * @param clientPropsMappings Client properties mappings
     * @param value Nested features
     * @param xpath Attribute xPath where the client properties are to be set
     * @param targetNodeType Target node type
     */
    protected void setXlinkReference(
            Attribute target,
            Map<Name, Expression> clientPropsMappings,
            Object value,
            StepList xpath,
            AttributeType targetNodeType) {
        Expression linkExpression = clientPropsMappings.get(XLINK_HREF_NAME);

        for (Object singleVal : (Collection) value) {
            // Make sure the same value isn't already set
            // in case it comes from a denormalized view for many-to-many relationship.
            // (1) Get the first existing value
            Collection<Property> existingAttributes = getProperties((ComplexAttribute) target, xpath);
            boolean exists = false;

            if (existingAttributes != null) {
                for (Property existingAttribute : existingAttributes) {
                    Object existingValue = existingAttribute.getUserData().get(Attributes.class);
                    if (existingValue != null) {
                        assert existingValue instanceof HashMap;
                        existingValue = ((Map) existingValue).get(XLINK_HREF_NAME);
                    }
                    if (existingValue != null) {
                        Object hrefValue = linkExpression.evaluate(singleVal);
                        if (hrefValue != null && hrefValue.equals(existingValue)) {
                            // (2) if one of the new values matches the first existing value,
                            // that means this comes from a denormalized view,
                            // and this set has already been set
                            exists = true;
                            // stop looking once found
                            break;
                        }
                    }
                }
            }
            if (!exists) {
                Attribute instance = xpathAttributeBuilder.set(target, xpath, null, null, targetNodeType, true, null);
                setClientProperties(instance, singleVal, clientPropsMappings);
            }
        }
    }

    protected List<Feature> setNextFeature(String fId, List<Object> foreignIdValues) throws IOException {
        List<Feature> features = new ArrayList<>();
        features.add(curSrcFeature);
        curSrcFeature = null;

        while (getSourceFeatureIterator().hasNext()) {
            Feature next = getSourceFeatureIterator().next();
            if (extractIdForFeature(next).equals(fId) && checkForeignIdValues(foreignIdValues, next)) {
                // HACK HACK HACK
                // evaluate filter that applies to this list as we want a subset
                // instead of full result
                // this is a temporary solution for Bureau of Meteorology
                // requirement for timePositionList
                if (listFilter != null) {
                    if (listFilter.evaluate(next)) {
                        features.add(next);
                    }
                    // END OF HACK
                } else {
                    features.add(next);
                }
                // HACK HACK HACK
                // evaluate filter that applies to this list as we want a subset
                // instead of full result
                // this is a temporary solution for Bureau of Meteorology
                // requirement for timePositionList
            } else if (listFilter == null || listFilter.evaluate(next)) {
                // END OF HACK
                curSrcFeature = next;
                break;
            }
        }
        return features;
    }

    /**
     * Only used when joining is not used and pre-filter exists because the sources will match the prefilter but there
     * might be denormalised rows with same id that don't.
     */
    private List<Feature> setNextFilteredFeature(String fId) throws IOException {
        Query query = new Query();
        if (reprojection != null) {
            if (sourceFeatures.getSchema().getGeometryDescriptor() != null
                    && !this.isReprojectionCrsEqual(
                            this.mappedSource.getSchema().getCoordinateReferenceSystem(), this.reprojection)) {
                query.setCoordinateSystemReproject(reprojection);
            }
        }

        Filter fidFilter;

        if (mapping.getFeatureIdExpression().equals(Expression.NIL)) {
            // no real feature id mapping,
            // so let's find by database row id
            Set<FeatureId> ids = new HashSet<>();
            FeatureId featureId = namespaceAwareFilterFactory.featureId(fId);
            ids.add(featureId);
            fidFilter = namespaceAwareFilterFactory.id(ids);
        } else {
            // in case the expression is wrapped in a function, eg. strConcat
            // that's why we don't always filter by id, but do a PropertyIsEqualTo
            fidFilter = namespaceAwareFilterFactory.equals(
                    mapping.getFeatureIdExpression(), namespaceAwareFilterFactory.literal(fId));
        }

        // HACK HACK HACK
        // evaluate filter that applies to this list as we want a subset
        // instead of full result
        // this is a temporary solution for Bureau of Meteorology
        // requirement for timePositionList
        if (listFilter != null) {
            List<Filter> filters = new ArrayList<>();
            filters.add(listFilter);
            filters.add(fidFilter);
            fidFilter = namespaceAwareFilterFactory.and(filters);
        }
        // END OF HACK

        query.setFilter(fidFilter);
        FeatureCollection<? extends FeatureType, ? extends Feature> matchingFeatures =
                this.mappedSource.getFeatures(query);

        List<Feature> features = new ArrayList<>();
        try (FeatureIterator<? extends Feature> iterator = matchingFeatures.features()) {
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
            // Probably cause there is no primary key nor idExpression
            if (features.isEmpty()) {
                features.add(curSrcFeature);
            }

            filteredFeatures.add(fId);
        }

        curSrcFeature = null;

        return features;
    }

    public void skipNestedMapping(AttributeMapping attMapping, List<Feature> sources) throws IOException {
        if (attMapping instanceof JoiningNestedAttributeMapping) {

            for (Feature source : sources) {
                Object value = getValues(attMapping.isMultiValued(), attMapping.getSourceExpression(), source);

                if (value instanceof Collection) {
                    for (Object val : (Collection) value) {
                        ((JoiningNestedAttributeMapping) attMapping).skip(this, val, getIdValues(source));
                    }
                } else {
                    ((JoiningNestedAttributeMapping) attMapping).skip(this, value, getIdValues(source));
                }
            }
        }
    }

    public List<Feature> skip() throws IOException {
        setHasNextCalled(false);

        List<Feature> sources = getSources(extractIdForFeature(curSrcFeature));
        for (AttributeMapping attMapping : selectedMapping) {
            skipNestedMapping(attMapping, sources);
        }
        return sources;
    }

    private GeometryDescriptor reprojectGeometry(GeometryDescriptor descr) {
        if (descr == null) {
            return null;
        }
        GeometryType type = ftf.createGeometryType(
                descr.getType().getName(),
                descr.getType().getBinding(),
                reprojection,
                descr.getType().isIdentified(),
                descr.getType().isAbstract(),
                descr.getType().getRestrictions(),
                descr.getType().getSuper(),
                descr.getType().getDescription());
        type.getUserData().putAll(descr.getType().getUserData());
        GeometryDescriptor gd = ftf.createGeometryDescriptor(
                type,
                descr.getName(),
                descr.getMinOccurs(),
                descr.getMaxOccurs(),
                descr.isNillable(),
                descr.getDefaultValue());
        gd.getUserData().putAll(descr.getUserData());
        return gd;
    }

    private FeatureType reprojectType(FeatureType type) {
        Collection<PropertyDescriptor> schema = new ArrayList<>();

        for (PropertyDescriptor descr : type.getDescriptors()) {
            if (descr instanceof GeometryDescriptor) {
                schema.add(reprojectGeometry((GeometryDescriptor) descr));
            } else {
                schema.add(descr);
            }
        }

        FeatureType ft;
        if (type instanceof NonFeatureTypeProxy) {
            ft = new NonFeatureTypeProxy(((NonFeatureTypeProxy) type).getSubject(), mapping, schema);
        } else {
            ft = ftf.createFeatureType(
                    type.getName(),
                    schema,
                    reprojectGeometry(type.getGeometryDescriptor()),
                    type.isAbstract(),
                    type.getRestrictions(),
                    type.getSuper(),
                    type.getDescription());
        }
        ft.getUserData().putAll(type.getUserData());
        return ft;
    }

    private AttributeDescriptor reprojectAttribute(AttributeDescriptor descr) {

        if (reprojection != null && descr.getType() instanceof FeatureType) {
            AttributeDescriptor ad = ftf.createAttributeDescriptor(
                    reprojectType((FeatureType) descr.getType()),
                    descr.getName(),
                    descr.getMinOccurs(),
                    descr.getMaxOccurs(),
                    descr.isNillable(),
                    descr.getDefaultValue());
            ad.getUserData().putAll(descr.getUserData());
            return ad;
        } else {
            return descr;
        }
    }

    @Override
    protected Feature computeNext() throws IOException {

        String id = getNextFeatureId();
        List<Feature> sources = getSources(id);

        final Name targetNodeName = targetFeature.getName();

        AppSchemaAttributeBuilder builder = new AppSchemaAttributeBuilder(attf);
        builder.setDescriptor(targetFeature);
        Feature target = (Feature) builder.build(id);

        for (AttributeMapping attMapping : selectedMapping) {
            try {
                if (skipTopElement(targetNodeName, attMapping, targetFeature.getType())) {
                    // ignore the top level mapping for the Feature itself
                    // as it was already set, but make sure client properties are set
                    setClientPropertiesRootEl(target, sources, attMapping);
                    continue;
                }
                if (attMapping.isList()) {
                    Attribute instance = setAttributeValue(
                            target, null, sources.get(0), attMapping, null, null, selectedProperties.get(attMapping));
                    if (sources.size() > 1 && instance != null) {
                        List<Object> values = new ArrayList<>();
                        Expression sourceExpr = attMapping.getSourceExpression();
                        for (Feature source : sources) {
                            values.add(getValue(sourceExpr, source));
                        }
                        String valueString = StringUtils.join(values.iterator(), " ");
                        StepList fullPath = attMapping.getTargetXPath();
                        StepList leafPath = fullPath.subList(fullPath.size() - 1, fullPath.size());
                        if (instance instanceof ComplexAttributeImpl) {
                            // xpath builder will work out the leaf attribute to set values on
                            xpathAttributeBuilder.set(instance, leafPath, valueString, null, null, false, sourceExpr);
                        } else {
                            // simple attributes
                            instance.setValue(valueString);
                        }
                    }
                } else if (attMapping.isMultiValued()) {
                    // extract the values from multiple source features of the same id
                    // and set them to one built feature
                    for (Feature source : sources) {
                        setAttributeValue(
                                target, null, source, attMapping, null, null, selectedProperties.get(attMapping));
                    }
                } else {
                    String indexString = attMapping.getSourceIndex();
                    // if not specified, get the first row by default
                    int index = 0;
                    if (indexString != null) {
                        if (ComplexFeatureConstants.LAST_INDEX.equals(indexString)) {
                            index = sources.size() - 1;
                        } else {
                            index = Integer.parseInt(indexString);
                        }
                    }
                    setAttributeValue(
                            target,
                            null,
                            sources.get(index),
                            attMapping,
                            null,
                            null,
                            selectedProperties.get(attMapping));
                    // When a feature is not multi-valued but still has multiple rows with the same
                    // ID in
                    // a denormalised table, by default app-schema only takes the first row and
                    // ignores
                    // the rest (see above). The following line is to make sure that the cursors in
                    // the
                    // 'joining nested mappings'skip any extra rows that were linked to those rows
                    // that are being ignored.
                    // Otherwise the cursor will stay there in the wrong spot and none of the
                    // following feature chaining
                    // will work. That can really only occur if the foreign key is not unique for
                    // the ID of the parent
                    // feature (otherwise all of those rows would be already passed when creating
                    // the feature based on
                    // the first row). This never really occurs in practice I have noticed, but it
                    // is a theoretic
                    // possibility, as there is no requirement for the foreign key to be unique per
                    // id.
                    skipNestedMapping(attMapping, sources.subList(1, sources.size()));
                }
            } catch (Exception e) {
                throw new RuntimeException(
                        "Error applying mapping with targetAttribute " + attMapping.getTargetXPath(), e);
            }
        }

        // if a default geometry attribute was configured in the mapping, set its value
        setDefaultGeometryAttribute(target);

        cleanEmptyElements(target);

        return target;
    }

    private void setClientPropertiesRootEl(Feature target, List<Feature> sources, AttributeMapping attMapping) {
        Map<Name, Expression> clientProps = attMapping.getClientProperties();
        if (MapUtils.isNotEmpty(clientProps) && CollectionUtils.isNotEmpty(sources)) {
            sources.forEach(f -> setClientProperties(target, f, clientProps));
        }
    }

    private void setDefaultGeometryAttribute(Feature feature) {
        String defaultGeomXPath = mapping.getDefaultGeometryXPath();
        if (defaultGeomXPath != null && !defaultGeomXPath.isEmpty()) {
            GeometryDescriptor defaultGeomDescr = feature.getType().getGeometryDescriptor();
            if (defaultGeomDescr != null) {
                PropertyName geomProperty = filterFac.property(defaultGeomXPath, namespaces);
                Object geomValue = geomProperty.evaluate(feature);
                if (geomValue instanceof Collection) {
                    throw new RuntimeException("Error setting default geometry value: multiple values were found");
                }

                String geomName = Types.toPrefixedName(defaultGeomDescr.getName(), namespaces);
                StepList fakeDefaultGeomXPath = XPath.steps(targetFeature, geomName, namespaces);
                xpathAttributeBuilder.set(feature, fakeDefaultGeomXPath, geomValue, null, null, false, null);
            }
        }
    }

    /**
     * Get all source features of the provided id. This assumes the source features are grouped by id.
     *
     * @param id The feature id
     * @return list of source features
     */
    protected List<Feature> getSources(String id) throws IOException {
        if (isFiltered) {
            return setNextFilteredFeature(id);
        } else {
            return setNextFeature(id, getForeignIdValues(curSrcFeature));
        }
    }

    protected String getNextFeatureId() {
        return extractIdForFeature(curSrcFeature);
    }

    protected void cleanEmptyElements(Feature target) throws DataSourceException {
        try {
            List<Property> values = new ArrayList<>();
            for (Property property : target.getValue()) {
                if (hasChild(property) || property.getDescriptor().getMinOccurs() > 0 || getEncodeIfEmpty(property)) {
                    values.add(property);
                }
            }
            target.setValue(values);
        } catch (DataSourceException e) {
            throw new DataSourceException("Unable to clean empty element", e);
        }
    }

    private boolean hasChild(Property property) throws DataSourceException {
        boolean result = false;

        if (hasChildElementOnUserData(property)) {
            return true;
        } else if (property.getValue() instanceof Collection) {

            Collection collection = (Collection) property.getValue();

            List<Property> values = new ArrayList<>();
            for (Object o : collection) {
                if (o instanceof Property) {
                    Property p = (Property) o;
                    if (hasChild(p)) {
                        values.add(p);
                        result = true;
                    } else if (getEncodeIfEmpty(p)) {
                        values.add(p);
                        result = true;
                    } else if (p.getDescriptor().getMinOccurs() > 0) {
                        if (p.getDescriptor().isNillable()) {
                            // add nil mandatory property
                            values.add(p);
                        }
                    }
                }
            }
            property.setValue(values);
            if (this.getClientProperties(property).containsKey(XLINK_HREF_NAME)) {
                return true;
            }
        } else if (property.getName().equals(ComplexFeatureConstants.FEATURE_CHAINING_LINK_NAME)) {
            // ignore fake attribute FEATURE_LINK
            result = false;
        } else if (property.getValue() != null && property.getValue().toString().length() > 0) {
            result = true;
        }
        return result;
    }

    private boolean hasChildElementOnUserData(Property property) {
        if (property.getUserData() != null
                && property.getUserData().containsKey(Attributes.class)
                && property.getUserData().get(Attributes.class) instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> childsMap =
                    (Map<Object, Object>) property.getUserData().get(Attributes.class);
            // check if we have at least one ComplexNameImpl on keys
            for (Object objectKey : childsMap.keySet()) {
                if (objectKey instanceof ComplexNameImpl) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean skipTopElement(Name topElement, AttributeMapping attMapping, AttributeType type) {
        // don't skip if there's OCQL
        return XPath.equals(topElement, attMapping.getTargetXPath())
                && (attMapping.getSourceExpression() == null
                        || Expression.NIL.equals(attMapping.getSourceExpression()));
    }

    @Override
    protected Feature populateFeatureData(String id) throws IOException {
        throw new UnsupportedOperationException("populateFeatureData should not be called!");
    }

    @Override
    protected void closeSourceFeatures() {
        if (sourceFeatures != null && getSourceFeatureIterator() != null) {
            sourceFeatureIterator.close();
            sourceFeatureIterator = null;
            sourceFeatures = null;
            filteredFeatures = null;
            listFilter = null;

            // NC - joining nested atts
            for (AttributeMapping attMapping : selectedMapping) {
                if (attMapping instanceof JoiningNestedAttributeMapping) {
                    ((JoiningNestedAttributeMapping) attMapping).close(this);
                }
            }

            if (transaction != null && isTransactionOwner) {
                try {
                    transaction.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Exception occurred closing transaction: " + e.getMessage(), e);
                }
            }
        }
    }

    @Override
    protected Object getValue(final Expression expression, Object sourceFeature) {
        Object value = expression.evaluate(sourceFeature);
        if (value instanceof Attribute) {
            value = ((Attribute) value).getValue();
        }
        return value;
    }

    /**
     * Return all matching properties from provided root attribute and xPath.
     *
     * @param root The root attribute to start searching from
     * @param xpath The xPath matching the attribute
     * @return The matching attributes collection
     */
    private Collection<Property> getProperties(ComplexAttribute root, StepList xpath) {

        final StepList steps = new StepList(xpath);

        Iterator<Step> stepsIterator = steps.iterator();
        Collection<Property> properties = null;
        Step step = null;
        if (stepsIterator.hasNext()) {
            step = stepsIterator.next();
            properties = root.getProperties(Types.toTypeName(step.getName()));
        }

        while (stepsIterator.hasNext()) {
            step = stepsIterator.next();
            Collection<Property> nestedProperties = new ArrayList<>();
            for (Property property : properties) {
                assert property instanceof ComplexAttribute;
                Collection<Property> tempProperties =
                        ((ComplexAttribute) property).getProperties(Types.toTypeName(step.getName()));
                if (!tempProperties.isEmpty()) {
                    nestedProperties.addAll(tempProperties);
                }
            }
            properties.clear();
            if (nestedProperties.isEmpty()) {
                return properties;
            }
            properties.addAll(nestedProperties);
        }
        return properties;
    }

    /**
     * Checks if client property has xlink:ref in it, if the attribute is for chained features.
     *
     * @param clientPropsMappings the client properties mappings
     * @param isNested true if we're dealing with chained/nested features
     */
    protected boolean isByReference(Map<Name, Expression> clientPropsMappings, boolean isNested) {
        // only care for chained features
        return isNested
                ? clientPropsMappings.isEmpty()
                        ? false
                        : clientPropsMappings.get(XLINK_HREF_NAME) == null ? false : true
                : false;
    }

    /** Returns the declared CRS given the native CRS and the request WFS version */
    private CoordinateReferenceSystem getDeclaredCrs(CoordinateReferenceSystem nativeCRS, String wfsVersion) {
        try {
            if (nativeCRS == null) return null;

            if (wfsVersion.equals("1.0.0")) {
                return nativeCRS;
            } else {
                String srsName = GML2EncodingUtils.toURI(nativeCRS, SrsSyntax.OGC_URN_EXPERIMENTAL, false);
                // it's possible that we can't do the CRS -> code -> CRS conversion...so we'll just
                // return what we have
                if (srsName == null) return nativeCRS;
                return CRS.decode(srsName);
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("We have had issues trying to flip axis of " + nativeCRS, e);
        }
    }

    public boolean isReprojectionCrsEqual(CoordinateReferenceSystem source, CoordinateReferenceSystem target) {
        return CRS.equalsIgnoreMetadata(source, target);
    }

    public void setListFilter(Filter filter) {
        listFilter = filter;
    }

    private boolean getEncodeIfEmpty(Property p) {
        Object o = p.getDescriptor().getUserData().get("encodeIfEmpty");
        if (o == null) {
            return false;
        }
        return (Boolean) o;
    }

    /**
     * For testing purposes.
     *
     * @return the feature source providing input features to be mapped to target features
     */
    public FeatureSource<? extends FeatureType, ? extends Feature> getMappedSource() {
        return mappedSource;
    }
}
