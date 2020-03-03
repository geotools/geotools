/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;

/**
 * The central class that perform transformations on filters, queries and feature types. Can invert
 * itself and return a {@link Transformer} that goes the other direction.
 *
 * @author Andrea Aime - GeoSolutions
 */
class Transformer {

    static final Logger LOGGER = Logging.getLogger(Transformer.class);

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    SimpleFeatureSource source;

    Name name;

    List<Definition> definitions;

    Map<String, Expression> expressions;

    SimpleFeatureType schema;

    public Transformer(
            SimpleFeatureSource source,
            Name name,
            List<Definition> definitions,
            SimpleFeatureType targetSchema)
            throws IOException {
        this.source = source;
        this.name = name;
        this.definitions = definitions;
        this.expressions = new HashMap<String, Expression>();
        for (Definition property : definitions) {
            expressions.put(property.getName(), property.getExpression());
        }

        if (targetSchema == null) {
            this.schema = computeTargetSchema(name, definitions);
        } else {
            this.schema = targetSchema;
        }
    }

    /** Locates all geometry properties in the transformed type */
    List<String> getGeometryPropertyNames() {
        List<String> result = new ArrayList<String>();

        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            if (ad instanceof GeometryDescriptor) {
                result.add(ad.getLocalName());
            }
        }

        return result;
    }

    /**
     * Computes the target schema, first trying a static analysis, and if that one does not work,
     * evaluating the expressions against a sample feature
     */
    private SimpleFeatureType computeTargetSchema(Name typeName, List<Definition> definitions)
            throws IOException {
        SimpleFeatureType target =
                computeTargetSchemaStatically(source.getSchema(), typeName, definitions);
        if (target != null) {
            return target;
        }

        // get a sample feature, used as a last resort in case we cannot get a fix on the type
        // by static analysis (we don't use it first since the feature coudl contain null
        // values that result the expression into returning us a null
        SimpleFeature sample = null;
        SimpleFeatureIterator iterator = null;
        try {
            iterator = source.getFeatures().features();
            if (iterator.hasNext()) {
                sample = iterator.next();
            }
        } finally {
            if (iterator != null) {
                iterator.close();
            }
        }

        if (sample == null) {
            throw new IllegalStateException(
                    "Cannot compute the target feature type from the "
                            + "definitions by static analysis, and the source does not have any feature "
                            + "that we can use as a sample to compute the target type dynamically");
        }

        // build the output feature type
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(typeName);
        for (Definition definition : definitions) {
            AttributeDescriptor ad = definition.getAttributeDescriptor(sample);
            tb.add(ad);
        }

        return tb.buildFeatureType();
    }

    private SimpleFeatureType computeTargetSchemaStatically(
            SimpleFeatureType originalSchema, Name typeName, List<Definition> definitions) {
        // build the output feature type
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(typeName);
        for (Definition definition : definitions) {
            AttributeDescriptor ad = definition.getAttributeDescriptor(originalSchema);
            if (ad == null) {
                return null;
            }
            tb.add(ad);
        }

        return tb.buildFeatureType();
    }

    /**
     * Utility method to transform feature ids based on the convention &lt;type name&gt;.&lt;id&gt;.
     *
     * <p>Should be invoked by classes using this Transformer instance to build transformed
     * features.
     *
     * @param sourceFeature the source feature
     * @return the transformed feature identifier
     */
    String transformFid(SimpleFeature sourceFeature) {
        String origFid = sourceFeature.getID();
        String origFidPrefix = sourceFeature.getType().getTypeName() + ".";
        if (origFid.startsWith(origFidPrefix)) {
            String id = origFid.substring(origFidPrefix.length());
            return schema.getTypeName() + "." + id;
        } else {
            return origFid;
        }
    }

    public SimpleFeatureType getSchema() {
        return schema;
    }

    Expression getExpression(String attributeName) {
        return expressions.get(attributeName);
    }

    public Name getName() {
        return name;
    }

    public SimpleFeatureSource getSource() {
        return source;
    }

    /**
     * Returns the list of original names for the specified properties. If a property does not have
     * an equivalent original name (it is not a simple rename) it won't be returned
     */
    public List<String> getOriginalNames(List<String> names) {

        List<String> originalNames = new ArrayList<String>();
        for (String name : names) {
            Expression ex = expressions.get(name);
            if (ex instanceof PropertyName) {
                // rename or pass through
                PropertyName pn = (PropertyName) ex;
                originalNames.add(pn.getPropertyName());
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                            Level.FINE,
                            "The attribute {0} has a general transformation "
                                    + "{1}, can't associate it with an original attribute name ",
                            new Object[] {name, ex});
                }
            }
        }

        return originalNames;
    }

    /**
     * Injects the transformed attribute expressions into the filter to make it runnable against the
     * original data
     */
    Filter transformFilter(Filter filter) {
        TransformFilterVisitor transformer = new TransformFilterVisitor(expressions);
        return (Filter) filter.accept(transformer, null);
    }

    /**
     * Injects the transformed attribute expressions into the expression to make it runnable against
     * the original data
     */
    Expression transformExpression(Expression expression) {
        TransformFilterVisitor transformer = new TransformFilterVisitor(expressions);
        return (Expression) expression.accept(transformer, null);
    }

    /**
     * Transforms a query so that it can be run against the original feature source and provides all
     * the necessary attributes to evaluate the requested expressions
     */
    Query transformQuery(Query query) {
        Filter txFilter = transformFilter(query.getFilter());
        Query txQuery = new Query(query);
        txQuery.setTypeName(source.getSchema().getTypeName());
        txQuery.setPropertyNames(getRequiredAttributes(query));
        txQuery.setSortBy(getTransformedSortBy(query));
        txQuery.setFilter(txFilter);

        // can we support the required sorting?
        QueryCapabilities caps = source.getQueryCapabilities();
        if (query.getStartIndex() != null && !caps.isJoiningSupported()) {
            txQuery.setStartIndex(null);
        }
        if (query.getSortBy() != null && !caps.supportsSorting(txQuery.getSortBy())) {
            txQuery.setSortBy(null);
        }

        // if the original query had sorting but we cannot pass it down we
        // have to remove offset and limit too
        if (query.getSortBy() != null && txQuery.getSortBy() == null) {
            txQuery.setStartIndex(null);
            txQuery.setMaxFeatures(Query.DEFAULT_MAX);
        }

        // if the wrapped store cannot apply offsets we have to remove them too
        if (!caps.isOffsetSupported()) {
            txQuery.setStartIndex(null);
        }

        return txQuery;
    }

    /** Transforms a SortBy[] so that it can be sent down to the original store */
    SortBy[] getTransformedSortBy(Query query) {
        SortBy[] original = query.getSortBy();
        if (original == null) {
            return original;
        }

        List<SortBy> transformed = new ArrayList<SortBy>();
        for (SortBy sort : original) {
            if (sort == SortBy.NATURAL_ORDER || sort == SortBy.REVERSE_ORDER) {
                transformed.add(sort);
            }
            PropertyName pname = sort.getPropertyName();
            Expression ex = expressions.get(pname.getPropertyName());
            if (ex == null) {
                throw new IllegalArgumentException(
                        "Attribute " + pname + " is not part of the output schema");
            } else if (ex instanceof PropertyName) {
                PropertyName pn = (PropertyName) ex;
                transformed.add(FF.sort(pn.getPropertyName(), sort.getSortOrder()));
            } else if (!(ex instanceof Literal)) {
                // ok, this one cannot be sent down, so we need to do sorting on our own anyways
                return null;
            }
        }

        return transformed.toArray(new SortBy[transformed.size()]);
    }

    /** Builds the list of original attributes required to run the specified query */
    String[] getRequiredAttributes(Query query) {
        Set<String> attributes = new HashSet<String>();

        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        if (query.getPropertyNames() == Query.ALL_NAMES) {
            for (Expression ex : expressions.values()) {
                ex.accept(extractor, null);
            }
        } else {
            for (String name : query.getPropertyNames()) {
                Expression ex = expressions.get(name);
                ex.accept(extractor, null);
            }
        }

        attributes.addAll(extractor.getAttributeNameSet());
        return attributes.toArray(new String[attributes.size()]);
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    @Override
    public String toString() {
        return "Transformer[ feature source= "
                + source
                + " , type="
                + source.getSchema()
                + ", definitions="
                + definitions;
    }
}
