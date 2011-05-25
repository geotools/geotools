/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 * @author Gabriel Roldan, Axios Engineering
 * @author Rini Angreani, Curtin University of Technology
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class AttributeMapping implements Serializable {
    private static final long serialVersionUID = 3624951889528331592L;

    /**
     * XPath expression addressing the target attribute in a target FeatureType.
     */
    private String targetAttributePath;
    /**
     * XPath expression addressing the input attribute in the input FeatureType if the source
     * is a data access containing complex features.
     */
    private String inputAttributePath;
    /**
     * XPath expression indicating the node in xml of an individual feature.
     */ 
    private String identifierPath;
    /**
     * Expression whose evaluation result against a Feature of the source FeatureType is going to be
     * the value of the target attribute in output FeatureType.
     * 
     * <p>
     * At this stage, the expression must be a valid OpenGIS Common Query Language expression.
     * </p>
     */
    private String sourceExpression;

    /**
     * Label used to refer to an attribute.
     */
    private String label;

    /**
     * Reference to other attribute identified with 'label'.
     */
    private String parentLabel;

    /**
     * Filters will refer to this element via this label.
     */
    private String targetQueryString;
    
    /**
     * Reference to instance xpath.
     */
    private String instancePath;
    
    /**
     * Name of the linked element type of which this attribute is nesting/targeting.
     */
    private String linkElement;

    /**
     * XPath expression addressing the target attribute in the linked target feature type.
     */
    private String linkField;

    /**
     * Expression whose evaluation result against a Feature of the source FeatureType is going to be
     * the value of the id attribute property
     * 
     * <p>
     * At this stage, the expression must be a valid OpenGIS Common Query Language expression.
     * </p>
     */
    private String identifierExpression;

    /**
     * Name of the target element instance this attribute mapping applies to, or <code>null</code>
     * if its fully addressable by the FeatureType.
     * 
     * <p>
     * for example, the target FeatureType may define a property as GeometryAttributeType, but the
     * actual instance should be PointPropertyType.
     * </p>
     */
    private String targetAttributeSchemaElement;

    /**
     * If <code>true</code>, indicates that one instance of this attribute mapping must be
     * created for every repeating group of attributes. In other words, indicates wether this
     * attribute corresponds to a multivalued or a single valued attribute.
     */
    private boolean isMultiple;

    /**
     * Client properties definitions for instances of the target attribute. The map is keys are
     * strings representing the name of the client properties, and the map values are strings
     * representing OCG's CQL expressions whose evaluated value against the instances of the source
     * features are going to be the client properties values.
     * <p>
     * for example: srsName/strConcat("#bh.", BGS_ID)
     * </p>
     */
    private Map clientProperties;

    /**
     * Returns the expression whose evaluation result against a Feature of the source FeatureType is
     * going to be the value of the target attribute in output FeatureType.
     * 
     * <p>
     * At this stage, the expression must be a valid OpenGIS Common Query Language expression.
     * </p>
     * 
     * @return OGC CQL expression for the attribute value
     */
    public String getSourceExpression() {
        return sourceExpression;
    }

    /**
     * Sets the OGC CQL expression for the attribute value.
     * 
     * @param sourceExpression
     *                OGC CQL expression for the attribute value.
     */
    public void setSourceExpression(String sourceExpression) {
        this.sourceExpression = sourceExpression;
    }
    
    /**
     * Return the input XPath expression
     * @return the input XPath expression
     */
    public String getInputAttributePath() {
        return inputAttributePath;
    }
    
    /**
     * Set the input XPath expression where we are getting the features from a data access
     * instead of a data store. 
     * @param inputAttributePath
     */
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

    /**
     * Sets the name of the linked element type of which this attribute is nesting/targeting.
     * 
     * @param linkElement
     */
    public void setLinkElement(String linkElement) {
        this.linkElement = linkElement;
    }

    /**
     * Returns the XPath expression addressing the target attribute in the linked target feature
     * type
     * 
     * @return the linked field
     */
    public String getLinkField() {
        return linkField;
    }

    /**
     * Sets the XPath expression addressing the target attribute in the linked target feature type
     * 
     * @param linkField
     */
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
     * @param targetAttributePath
     *                the XPath location path for the target attribute of the mapping.
     */
    public void setTargetAttributePath(String targetAttributePath) {
        this.targetAttributePath = targetAttributePath;
    }

    /**
     * Returns the name of the target element instance this attribute mapping applies to, or
     * <code>null</code> if its fully addressable by the FeatureType.
     * 
     * <p>
     * For example, the target FeatureType may define a property as GeometryAttributeType, but the
     * actual instance should be PointPropertyType. In which case, it should be set to
     * "gml:PointPropertyType" so AppSchemaDataAccess knows it should create a point property an thus
     * its subelements are to be addressable by subsequent mappings.
     * </p>
     * 
     * @return name of the target element instance in the output schema or <code>null</code> if
     *         not set.
     */
    public String getTargetAttributeSchemaElement() {
        return targetAttributeSchemaElement;
    }

    /**
     * Sets the name of the target element instance in the output schema.
     * 
     * @param targetAttributeSchemaElement
     *                name of the target element instance in the output schema. Could be prefixed,
     *                in which case the prefix mapping has to be available in the corresponding
     *                {@link AppSchemaDataAccessDTO#getNamespaces()}
     */
    public void setTargetAttributeSchemaElement(String targetAttributeSchemaElement) {
        this.targetAttributeSchemaElement = targetAttributeSchemaElement;
    }

    /**
     * Returns wether this attribute should be treated as a single or multi valued property.
     * 
     * @return <code>true</code> if this attribute corresponds to a multivalued property,
     *         <code>false</code> otherwise.
     */
    public boolean isMultiple() {
        return isMultiple;
    }

    /**
     * Sets wether this attribute should be treated as a single or multi valued property.
     * 
     * @param isMultiple
     *                <code>true</code> if this attribute corresponds to a multivalued property,
     *                <code>false</code> otherwise.
     */
    public void setMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    /**
     * Helper method to allow config digester passing a string.
     * 
     * @see #setMultiple(boolean)
     * @param isMultiple
     */
    public void setMultiple(String isMultiple) {
        boolean multiple = Boolean.valueOf(isMultiple).booleanValue();
        setMultiple(multiple);
    }

    /**
     * Returns a string representation of this config object.
     * 
     * @return String representation of this config object.
     */
    public String toString() {
        return "AttributeMappingDTO[id > "
                + identifierExpression
                + ", "
                + ((sourceExpression == null) ? ((inputAttributePath == null ? ""
                        : inputAttributePath)) : sourceExpression)
                + " -> "
                + targetAttributePath
                + ", isMultiple: "
                + isMultiple
                + ((targetAttributeSchemaElement == null) ? ""
                        : (", target node: " + targetAttributeSchemaElement))
                + ((linkElement == null) ? "" : (", linkElement: " + linkElement))
                + ((linkField == null) ? "" : (", linkField: " + linkField)) + "]";
    }

    public Map getClientProperties() {
        return clientProperties == null ? Collections.EMPTY_MAP : clientProperties;
    }

    public void setClientProperties(Map clientProperties) {
        this.clientProperties = clientProperties == null ? null : new HashMap(clientProperties);
    }

    public void putClientProperty(String name, String expression) {
        if (name == null || expression == null) {
            throw new NullPointerException("name=" + name + ", expression=" + expression);
        }
        if (clientProperties == null) {
            clientProperties = new HashMap();
        }
        clientProperties.put(name, expression);
    }

    public String getIdentifierExpression() {
        return identifierExpression;
    }

    public void setIdentifierExpression(String identifierExpression) {
        this.identifierExpression = identifierExpression;
    }
}
