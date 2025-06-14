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

package org.geotools.data.complex.config;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration object for the mapping of a community schema attribute.
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 * @since 2.4
 */
public class AttributeMapping implements Serializable {
    private static final long serialVersionUID = 3624951889528331592L;

    /** XPath expression addressing the target attribute in a target FeatureType. */
    private String targetAttributePath;
    /**
     * XPath expression addressing the input attribute in the input FeatureType if the source is a data access
     * containing complex features.
     */
    private String inputAttributePath;
    /** XPath expression indicating the node in xml of an individual feature. */
    private String identifierPath;
    /**
     * Expression whose evaluation result against a Feature of the source FeatureType is going to be the value of the
     * target attribute in output FeatureType.
     *
     * <p>At this stage, the expression must be a valid OpenGIS Common Query Language expression.
     */
    private String sourceExpression;

    /**
     * Expression whose evaluation result in numeric value to indicate row number to extract
     * {@link this#sourceExpression} from denormalised database rows.
     *
     * <p>At this stage, the expression must be a valid integer, or LAST would work to get the last dynamic result.
     */
    private String sourceIndex;

    /** Label used to refer to an attribute. */
    private String label;

    /** Reference to other attribute identified with 'label'. */
    private String parentLabel;

    /** Filters will refer to this element via this label. */
    private String targetQueryString;

    /** Reference to instance xpath. */
    private String instancePath;

    /** Name of the linked element type of which this attribute is nesting/targeting. */
    private String linkElement;

    /** XPath expression addressing the target attribute in the linked target feature type. */
    private String linkField;

    /**
     * Expression whose evaluation result against a Feature of the source FeatureType is going to be the value of the id
     * attribute property
     *
     * <p>At this stage, the expression must be a valid OpenGIS Common Query Language expression.
     */
    private String identifierExpression;

    /**
     * Name of the target element instance this attribute mapping applies to, or <code>null</code> if its fully
     * addressable by the FeatureType.
     *
     * <p>for example, the target FeatureType may define a property as GeometryAttributeType, but the actual instance
     * should be PointPropertyType.
     */
    private String targetAttributeSchemaElement;

    /**
     * If <code>true</code>, indicates that one instance of this attribute mapping must be created for every repeating
     * group of attributes. In other words, indicates whether this attribute corresponds to a multivalued or a single
     * valued attribute.
     */
    private boolean isMultiple;

    /** If <code>true</code>, indicates that one this attribute should be encode if it contains null or empty value. */
    private boolean encodeIfEmpty;

    /**
     * If <code>true</code>, indicates that this attribute corresponds to a list of values. This is similar to
     * isMultiple, except the values are concatenated as a big String inside the attribute.
     */
    private boolean isList;

    /**
     * Client properties definitions for instances of the target attribute. The map is keys are strings representing the
     * name of the client properties, and the map values are strings representing OCG's CQL expressions whose evaluated
     * value against the instances of the source features are going to be the client properties values.
     *
     * <p>for example: srsName/strConcat("#bh.", BGS_ID)
     */
    private Map<String, String> clientProperties;

    /** Field name in external index layer */
    private String indexField;

    /**
     * Mapping of attributes with 1..N cardinality. Each data store is free to contribute is own syntax (e.g. Solr multi
     * values to support multi valuated fields).
     */
    private MultipleValue multipleValue;

    private Map<String, String> anonymousAttributes = new HashMap<>();

    /**
     * Returns the expression whose evaluation result against a Feature of the source FeatureType is going to be the
     * value of the target attribute in output FeatureType.
     *
     * <p>At this stage, the expression must be a valid OpenGIS Common Query Language expression.
     *
     * @return OGC CQL expression for the attribute value
     */
    public String getSourceExpression() {
        return sourceExpression;
    }

    /**
     * Sets the OGC CQL expression for the attribute value.
     *
     * @param sourceExpression OGC CQL expression for the attribute value.
     */
    public void setSourceExpression(String sourceExpression) {
        this.sourceExpression = sourceExpression;
    }

    /**
     * Returns the expression whose evaluation result in numeric value to indicate row number to extract
     * {@link this#sourceExpression} from denormalised database rows.
     *
     * <p>At this stage, the expression must be a valid integer, or LAST would work to get the last dynamic result.
     *
     * @return OGC CQL expression for the attribute value
     */
    public String getSourceIndex() {
        return sourceIndex;
    }

    /**
     * Sets the OGC CQL expression index for the attribute value.
     *
     * @param sourceIndex OGC CQL expression index for the attribute value.
     */
    public void setSourceIndex(String sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    /**
     * Return the input XPath expression
     *
     * @return the input XPath expression
     */
    public String getInputAttributePath() {
        return inputAttributePath;
    }

    /** Set the input XPath expression where we are getting the features from a data access instead of a data store. */
    public void setInputAttributePath(String inputAttributePath) {
        this.inputAttributePath = inputAttributePath;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
    }

    public String getTargetQueryString() {
        return targetQueryString;
    }

    public void setTargetQueryString(String targetQueryString) {
        this.targetQueryString = targetQueryString;
    }

    public String getInstancePath() {
        return instancePath;
    }

    public void setInstancePath(String instancePath) {
        this.instancePath = instancePath;
    }

    public String getIdentifierPath() {
        return identifierPath;
    }

    public void setIdentifierPath(String identifierPath) {
        this.identifierPath = identifierPath;
    }

    /**
     * Returns the name of the linked element type of which this attribute is nesting/targeting.
     *
     * @return the link element name
     */
    public String getLinkElement() {
        return linkElement;
    }

    /** Sets the name of the linked element type of which this attribute is nesting/targeting. */
    public void setLinkElement(String linkElement) {
        this.linkElement = linkElement;
    }

    /**
     * Returns the XPath expression addressing the target attribute in the linked target feature type
     *
     * @return the linked field
     */
    public String getLinkField() {
        return linkField;
    }

    /** Sets the XPath expression addressing the target attribute in the linked target feature type */
    public void setLinkField(String linkField) {
        this.linkField = linkField;
    }

    /**
     * Returns the XPath expression addressing the target attribute in a target FeatureType.
     *
     * @return the XPath location path for the target attribute of the mapping.
     */
    public String getTargetAttributePath() {
        return targetAttributePath;
    }

    /**
     * Sets the XPath expression addressing the target attribute in a target FeatureType.
     *
     * @param targetAttributePath the XPath location path for the target attribute of the mapping.
     */
    public void setTargetAttributePath(String targetAttributePath) {
        this.targetAttributePath = targetAttributePath;
    }

    /**
     * Returns the name of the target element instance this attribute mapping applies to, or <code>
     * null</code> if its fully addressable by the FeatureType.
     *
     * <p>For example, the target FeatureType may define a property as GeometryAttributeType, but the actual instance
     * should be PointPropertyType. In which case, it should be set to "gml:PointPropertyType" so AppSchemaDataAccess
     * knows it should create a point property an thus its subelements are to be addressable by subsequent mappings.
     *
     * @return name of the target element instance in the output schema or <code>null</code> if not set.
     */
    public String getTargetAttributeSchemaElement() {
        return targetAttributeSchemaElement;
    }

    /**
     * Sets the name of the target element instance in the output schema.
     *
     * @param targetAttributeSchemaElement name of the target element instance in the output schema. Could be prefixed,
     *     in which case the prefix mapping has to be available in the corresponding
     *     {@link AppSchemaDataAccessDTO#getNamespaces()}
     */
    public void setTargetAttributeSchemaElement(String targetAttributeSchemaElement) {
        this.targetAttributeSchemaElement = targetAttributeSchemaElement;
    }

    /**
     * Returns whether this attribute should be treated as a single or multi valued property.
     *
     * @return <code>true</code> if this attribute corresponds to a multivalued property, <code>
     *     false</code> otherwise.
     */
    public boolean isMultiple() {
        return isMultiple;
    }

    /**
     * Sets whether this attribute should be treated as a single or multi valued property.
     *
     * @param isMultiple <code>true</code> if this attribute corresponds to a multivalued property, <code>false</code>
     *     otherwise.
     */
    public void setMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    /**
     * Returns whether this attribute should encode when empty;
     *
     * @return <code>true</code> encode when the value is empty, <code>false</code> otherwise.
     */
    public boolean encodeIfEmpty() {
        return encodeIfEmpty;
    }

    /**
     * Returns whether this attribute should encode when empty;
     *
     * @param encodeIfEmpty <code>true</code> encode when the value is empty, <code>false</code> otherwise.
     */
    public void setEncodeIfEmpty(boolean encodeIfEmpty) {
        this.encodeIfEmpty = encodeIfEmpty;
    }

    /**
     * Returns whether this attribute should encode when empty;
     *
     * @param encodeIfEmpty <code>true</code> encode when the value is empty, <code>false</code> otherwise.
     */
    public void setEncodeIfEmpty(String encodeIfEmpty) {
        this.encodeIfEmpty = Boolean.parseBoolean(encodeIfEmpty);
    }

    /**
     * Sets whether this attribute should be treated as a list valued property.
     *
     * @param isList <code>true</code> if this attribute corresponds to a list valued property, <code>false</code>
     *     otherwise.
     */
    public void setList(boolean isList) {
        this.isList = isList;
    }

    /**
     * Helper method to allow config digester passing a string.
     *
     * @see #setList(boolean)
     */
    public void setList(String list) {
        boolean isList = Boolean.parseBoolean(list);
        setList(isList);
    }

    /**
     * Returns whether this attribute should be treated as a list valued property.
     *
     * @return <code>true</code> if this attribute corresponds to a list valued property, <code>
     *     false</code> otherwise.
     */
    public boolean isList() {
        return isList;
    }

    /**
     * Helper method to allow config digester passing a string.
     *
     * @see #setMultiple(boolean)
     */
    public void setMultiple(String isMultiple) {
        boolean multiple = Boolean.parseBoolean(isMultiple);
        setMultiple(multiple);
    }

    /**
     * Returns a string representation of this config object.
     *
     * @return String representation of this config object.
     */
    @Override
    public String toString() {
        return "AttributeMappingDTO[id > "
                + identifierExpression
                + ", "
                + (sourceExpression == null ? inputAttributePath == null ? "" : inputAttributePath : sourceExpression)
                + " -> "
                + targetAttributePath
                + ", isMultiple: "
                + isMultiple
                + ", encodeIfEmpty: "
                + encodeIfEmpty
                + (targetAttributeSchemaElement == null ? "" : ", target node: " + targetAttributeSchemaElement)
                + (linkElement == null ? "" : ", linkElement: " + linkElement)
                + (linkField == null ? "" : ", linkField: " + linkField)
                + "]";
    }

    public Map<String, String> getClientProperties() {
        return clientProperties == null ? Collections.emptyMap() : clientProperties;
    }

    public void setClientProperties(Map<String, String> clientProperties) {
        this.clientProperties = clientProperties == null ? null : new HashMap<>(clientProperties);
    }

    public void putClientProperty(String name, String expression) {
        if (name == null || expression == null) {
            throw new NullPointerException("name=" + name + ", expression=" + expression);
        }
        if (clientProperties == null) {
            clientProperties = new HashMap<>();
        }
        clientProperties.put(name, expression);
    }

    public String getIdentifierExpression() {
        return identifierExpression;
    }

    public void setIdentifierExpression(String identifierExpression) {
        this.identifierExpression = identifierExpression;
    }

    public MultipleValue getMultipleValue() {
        return multipleValue;
    }

    public void setMultipleValue(MultipleValue multipleValue) {
        this.multipleValue = multipleValue;
    }

    public String getIndexField() {
        return indexField;
    }

    public void setIndexField(String indexField) {
        this.indexField = indexField;
    }

    /** Attribute definition map for anonymous unbounded sequences on complexType Elements. */
    public Map<String, String> getAnonymousAttributes() {
        return anonymousAttributes;
    }

    public void setAnonymousAttributes(Map<String, String> anonymousAttributes) {
        if (anonymousAttributes == null) throw new IllegalArgumentException("Map is not nullable");
        this.anonymousAttributes = anonymousAttributes;
    }

    public void putAnonymousAttribute(String name, String expression) {
        if (name == null || expression == null) {
            throw new IllegalArgumentException("name=" + name + ", expression=" + expression);
        }
        getAnonymousAttributes().put(name, expression);
    }
}
