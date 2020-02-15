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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.namespace.QName;
import org.geotools.appschema.util.IndexQueryUtils;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.util.XPathUtil.Step;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.data.joining.JoiningNestedAttributeMapping;
import org.geotools.gml3.GML;
import org.geotools.xlink.XLINK;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * @since 2.4
 */
public class FeatureTypeMapping {
    /**
     * We should allow for both complex and simple feature source as we could now take in a data
     * access instead of a data store as the source data store
     */
    private FeatureSource<? extends FeatureType, ? extends Feature> source;

    private String sourceDatastoreId;

    // Index FeatureSource, optional
    private FeatureSource<SimpleFeatureType, SimpleFeature> indexSource;

    /** Encapsulates the name and type of target Features */
    private AttributeDescriptor target;

    /**
     * Map of <source expression>/<target property>, where target property is an XPath expression
     * addressing the mapped property of the target schema.
     */
    List<AttributeMapping> attributeMappings;

    NamespaceSupport namespaces;

    /**
     * A user-defined name for the mapping. This is optional, and used when there are more than one
     * mapping for the same type. When defined, this overrides the targetElement as the identifier.
     */
    private Name mappingName;

    private Expression featureFidMapping;

    private boolean isDenormalised;

    /**
     * User-provided XPath expression specifying a property to be used as default geometry for the
     * target feature type.
     */
    private String defaultGeometryXPath;

    /** No parameters constructor for use by the digester configuration engine as a JavaBean */
    public FeatureTypeMapping() {
        this(null, null, null, new LinkedList<AttributeMapping>(), new NamespaceSupport(), false);
    }

    public FeatureTypeMapping(
            FeatureSource<? extends FeatureType, ? extends Feature> source,
            AttributeDescriptor target,
            List<AttributeMapping> mappings,
            NamespaceSupport namespaces) {
        this(source, target, null, mappings, namespaces, false);
    }

    public FeatureTypeMapping(
            FeatureSource<? extends FeatureType, ? extends Feature> source,
            AttributeDescriptor target,
            String defaultGeometryXPath,
            List<AttributeMapping> mappings,
            NamespaceSupport namespaces,
            boolean isDenormalised) {
        this(source, null, target, defaultGeometryXPath, mappings, namespaces, isDenormalised);
    }

    public FeatureTypeMapping(
            FeatureSource<? extends FeatureType, ? extends Feature> source,
            FeatureSource<SimpleFeatureType, SimpleFeature> indexSource,
            AttributeDescriptor target,
            String defaultGeometryXPath,
            List<AttributeMapping> mappings,
            NamespaceSupport namespaces,
            boolean isDenormalised) {
        this.source = source;
        this.indexSource = indexSource;
        this.target = target;
        this.defaultGeometryXPath = defaultGeometryXPath;
        this.attributeMappings = new LinkedList<AttributeMapping>(mappings);
        this.namespaces = namespaces;
        this.isDenormalised = isDenormalised;

        // find id expression
        for (AttributeMapping attMapping : attributeMappings) {
            StepList targetXPath = attMapping.getTargetXPath();
            if (targetXPath.size() > 1) {
                continue;
            }
            Step step = (Step) targetXPath.get(0);
            QName stepName = step.getName();
            if (Types.equals(target.getName(), stepName)) {
                featureFidMapping = attMapping.getIdentifierExpression();
                break;
            }
        }
        if (featureFidMapping == null) {
            featureFidMapping = Expression.NIL;
        }
    }

    public List<AttributeMapping> getAttributeMappings() {
        return Collections.unmodifiableList(attributeMappings);
    }

    public List<NestedAttributeMapping> getNestedMappings() {
        List<NestedAttributeMapping> mappings = new ArrayList<NestedAttributeMapping>();
        for (AttributeMapping mapping : attributeMappings) {
            if (mapping instanceof NestedAttributeMapping) {
                mappings.add((NestedAttributeMapping) mapping);
            }
        }
        return mappings;
    }

    public Expression getFeatureIdExpression() {
        return featureFidMapping;
    }

    /**
     * Finds the attribute mappings for the given target location path. If the exactPath is not
     * indexed, it will get all the matching mappings ignoring index. If it is indexed, it will get
     * the one with matching index only.
     */
    public List<AttributeMapping> getAttributeMappingsIgnoreIndex(final StepList targetPath) {
        AttributeMapping attMapping;
        List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = (AttributeMapping) it.next();
            if (targetPath.equalsIgnoreIndex(attMapping.getTargetXPath())) {
                mappings.add(attMapping);
            }
        }
        return mappings;
    }

    /**
     * Finds the attribute mappings for the given source expression.
     *
     * @return list of matching attribute mappings
     */
    public List<AttributeMapping> getAttributeMappingsByExpression(
            final Expression sourceExpression) {
        AttributeMapping attMapping;
        List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = (AttributeMapping) it.next();
            if (sourceExpression.equals(attMapping.getSourceExpression())) {
                mappings.add(attMapping);
            }
        }
        return mappings;
    }

    /**
     * Finds the attribute mapping for the target expression <code>exactPath</code>
     *
     * @param exactPath the xpath expression on the target schema to find the mapping for
     * @return the attribute mapping that match 1:1 with <code>exactPath</code> or <code>null</code>
     *     if
     */
    public AttributeMapping getAttributeMapping(final StepList exactPath) {
        AttributeMapping attMapping;
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = (AttributeMapping) it.next();
            if (exactPath.equals(attMapping.getTargetXPath())) {
                return attMapping;
            }
        }
        return null;
    }

    /**
     * Finds the attribute mapping for the target expression <code>exactPath</code>.
     *
     * @param xpathExpression the xpath expression on the target schema to find the mapping for
     * @return the attribute mapping that match 1:1 with <code>exactPath</code> or <code>null</code>
     */
    public AttributeMapping getAttributeMapping(final String xpathExpression) {
        AttributeMapping attMapping;
        StepList stepList =
                XPath.steps(this.getTargetFeature(), xpathExpression, this.getNamespaces());
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = it.next();
            if (stepList.equals(attMapping.getTargetXPath())) {
                return attMapping;
            }
        }
        return null;
    }

    public NamespaceSupport getNamespaces() {
        return namespaces;
    }

    /** Has to be called after {@link #setTargetType(FeatureType)} */
    public void setTargetFeature(AttributeDescriptor feature) {
        this.target = feature;
    }

    public AttributeDescriptor getTargetFeature() {
        return this.target;
    }

    @SuppressWarnings("unchecked")
    public FeatureSource getSource() {
        return this.source;
    }

    public FeatureTypeMapping getUnderlyingComplexMapping() {
        if (source instanceof MappingFeatureSource) {
            return ((MappingFeatureSource) source).getMapping();
        }
        return null;
    }

    public void setName(Name name) {
        this.mappingName = name;
    }

    public Name getMappingName() {
        return mappingName;
    }

    /**
     * Return list of attribute mappings that are configured as list (isList = true).
     *
     * @return attribute mappings with isList enabled.
     */
    public List<AttributeMapping> getIsListMappings() {
        List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
        AttributeMapping attMapping;
        for (Iterator<AttributeMapping> it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = (AttributeMapping) it.next();
            if (attMapping.isList()) {
                mappings.add(attMapping);
            }
        }
        return mappings;
    }

    /**
     * Looks up for attribute mappings matching the xpath expression <code>propertyName</code>.
     *
     * <p>If any step in <code>propertyName</code> has index greater than 1, any mapping for the
     * same property applies, regardless of the mapping. For example, if there are mappings for
     * <code>gml:name[1]</code>, <code>gml:name[2]</code> and <code>gml:name[3]</code>, but
     * propertyName is just <code>gml:name</code>, all three mappings apply.
     */
    public List<Expression> findMappingsFor(
            final StepList propertyName, boolean includeNestedMappings) {
        // collect all the mappings for the given property
        List candidates;

        // get all matching mappings if index is not specified, otherwise
        // get the specified mapping
        if (!propertyName.toString().contains("[")) {
            candidates = getAttributeMappingsIgnoreIndex(propertyName);
        } else {
            candidates = new ArrayList<AttributeMapping>();
            AttributeMapping mapping = getAttributeMapping(propertyName);
            if (mapping != null) {
                candidates.add(mapping);
            }
        }
        if (candidates.size() == 0
                && propertyName.toString().equals("@gml:id")
                && getFeatureIdExpression() != null) {
            Expression idExpression = getFeatureIdExpression();
            candidates.add(new AttributeMapping(idExpression, idExpression, propertyName));
        }
        List expressions = getExpressions(candidates, includeNestedMappings);

        // Does the last step refer to a client property of the parent step?
        // The parent step could be the root element which may not be on the path.
        // i.e. a client property maps to an xml attribute, and the step list
        // could have been generated from an xpath of the form
        // @attName or propA/propB@attName
        if (candidates.size() == 0 && propertyName.size() > 0) {
            XPath.Step clientPropertyStep = (Step) propertyName.get(propertyName.size() - 1);
            if (clientPropertyStep.isXmlAttribute()) {
                Name clientPropertyName = Types.toTypeName(clientPropertyStep.getName());
                XPath.StepList parentPath;

                if (propertyName.size() == 1) {
                    parentPath = XPath.rootElementSteps(this.target, this.namespaces);
                } else {
                    parentPath = new XPath.StepList(propertyName);
                    parentPath.remove(parentPath.size() - 1);
                }

                candidates = getAttributeMappingsIgnoreIndex(parentPath);
                expressions =
                        getClientPropertyExpressions(candidates, clientPropertyName, parentPath);
                if (expressions.isEmpty()) {
                    // this might be a wrapper mapping for another complex mapping
                    // look for the client properties there
                    FeatureTypeMapping inputMapping = getUnderlyingComplexMapping();
                    if (inputMapping != null) {
                        return getClientPropertyExpressions(
                                inputMapping.getAttributeMappingsIgnoreIndex(parentPath),
                                clientPropertyName,
                                parentPath);
                    }
                }
            }
        }
        return expressions;
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private List<Expression> getClientPropertyExpressions(
            final List attributeMappings, final Name clientPropertyName, StepList parentPath) {
        List<Expression> clientPropertyExpressions =
                new ArrayList<Expression>(attributeMappings.size());

        AttributeMapping attMapping;
        Map clientProperties;
        Expression propertyExpression;
        for (Iterator it = attributeMappings.iterator(); it.hasNext(); ) {
            attMapping = (AttributeMapping) it.next();
            if (attMapping instanceof JoiningNestedAttributeMapping
                    && !Types.equals(clientPropertyName, XLINK.HREF)) {
                // if it's joining for simple content feature chaining it has to be empty
                // so it will be added to the post filter... unless this is feature chaining by
                // reference
                // and we are looking at the xlink:href property, whose source expression is
                // specified
                // in the nested attribute mapping of the parent feature
                clientPropertyExpressions.add(null);
            } else {
                clientProperties = attMapping.getClientProperties();
                // NC -added
                if (Types.equals(clientPropertyName, GML.id)) {
                    clientPropertyExpressions.add(attMapping.getIdentifierExpression());
                } else if (clientProperties.containsKey(clientPropertyName)) {
                    // end NC - added
                    propertyExpression = (Expression) clientProperties.get(clientPropertyName);
                    clientPropertyExpressions.add(propertyExpression);
                }
            }
        }

        return clientPropertyExpressions;
    }

    /** Extracts the source Expressions from a list of {@link AttributeMapping}s */
    private List getExpressions(List attributeMappings, boolean includeNestedMappings) {
        List expressions = new ArrayList(attributeMappings.size());
        AttributeMapping mapping;
        Expression sourceExpression;
        for (Iterator it = attributeMappings.iterator(); it.hasNext(); ) {
            mapping = (AttributeMapping) it.next();
            if (mapping instanceof JoiningNestedAttributeMapping) {
                if (!includeNestedMappings) {
                    // will be added to post filter
                    expressions.add(null);
                    continue;
                }
            }
            sourceExpression = mapping.getSourceExpression();
            if (!Expression.NIL.equals(sourceExpression)) {
                // some filters can't handle Expression.NIL and just dies
                expressions.add(sourceExpression);
            } else if (!Expression.NIL.equals(mapping.getIdentifierExpression())) {
                // this is a an ID expression
                expressions.add(mapping.getIdentifierExpression());
            }
        }
        return expressions;
    }

    public boolean isDenormalised() {
        return isDenormalised;
    }

    public void setDenormalised(boolean isDenormalised) {
        this.isDenormalised = isDenormalised;
    }

    /**
     * Returns the default geometry XPath expression that was specified in the mapping
     * configuration, or {@code null} if none was set.
     *
     * @see FeatureTypeMapping#setDefaultGeometryXPath(String)
     * @return XPath expression identifying the default geometry property
     */
    public String getDefaultGeometryXPath() {
        return defaultGeometryXPath;
    }

    /**
     * Specifies which property should be used as default geometry for the target feature type.
     *
     * <p>This is especially useful when automatic detection of the default geometry is difficult or
     * impossible to do, e.g. when the geometry property is nested inside another property, or
     * multiple geometry properties are present.
     *
     * <p>The specified geometry property may be a direct child of the target feature type, or may
     * be nested inside another property. If the evaluation of the provided expression yields
     * multiple values, an exception will be thrown at runtime.
     *
     * @param defaultGeometryXPath XPath expression identifying the default geometry property
     */
    public void setDefaultGeometryXPath(String defaultGeometryXPath) {
        this.defaultGeometryXPath = defaultGeometryXPath;
    }

    public void setSource(FeatureSource<? extends FeatureType, ? extends Feature> source) {
        this.source = source;
    }

    public FeatureSource<SimpleFeatureType, SimpleFeature> getIndexSource() {
        return indexSource;
    }

    public void setIndexSource(FeatureSource<SimpleFeatureType, SimpleFeature> indexSource) {
        this.indexSource = indexSource;
    }

    /**
     * Returns index attribute name linked to unrolled propertyName or null if is absent
     *
     * @return Index attribute name
     */
    public String getIndexAttributeName(String xpath) {
        AttributeMapping mapp = IndexQueryUtils.getIndexedAttribute(this, xpath);
        if (mapp != null) {
            return mapp.getIndexField();
        }
        return null;
    }

    /** Returns the source datastore id from mappings configurations. */
    public Optional<String> getSourceDatastoreId() {
        return Optional.ofNullable(sourceDatastoreId);
    }

    /** Sets the source datastore id from mappings configurations. */
    public void setSourceDatastoreId(String sourceDatastoreId) {
        this.sourceDatastoreId = sourceDatastoreId;
    }
}
