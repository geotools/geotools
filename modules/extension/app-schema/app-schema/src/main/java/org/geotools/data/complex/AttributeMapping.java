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

import java.util.Collections;
import java.util.Map;

import org.geotools.data.complex.filter.XPathUtil.StepList;
import org.geotools.util.Utilities;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 *
 *
 *
 * @source $URL$
 * @since 2.4
 */
public class AttributeMapping {

    /** Expression to set the Attribute's ID from, or {@linkplain Expression#NIL} */
    private Expression identifierExpression;

    protected Expression sourceExpression;

    protected StepList targetXPath;

    private boolean isMultiValued;
    
    private boolean encodeIfEmpty;
    
    private boolean isList;

    /**
     * If present, represents our way to deal polymorphic attribute instances, so this node should
     * be of a subtype of the one referenced by {@link  #targetXPath}
     */
    AttributeType targetNodeInstance;

    private Map<Name, Expression> clientProperties;

    private String label;

    private String parentLabel;

    private String instancePath;

    private String sourceIndex;

    /**
     * Creates a new AttributeMapping object.
     * 
     * @param sourceExpression
     *                DOCUMENT ME!
     * @param targetXPath
     *                DOCUMENT ME!
     */
    public AttributeMapping(Expression idExpression, Expression sourceExpression,
            StepList targetXPath) {
        this(idExpression, sourceExpression, null, targetXPath, null, false, null);
    }

    public AttributeMapping(Expression idExpression, Expression sourceExpression, String sourceIndex,
            StepList targetXPath, AttributeType targetNodeInstance, boolean isMultiValued,
            Map<Name, Expression> clientProperties) {

        this.identifierExpression = idExpression == null ? Expression.NIL : idExpression;
        this.sourceExpression = sourceExpression == null ? Expression.NIL : sourceExpression;
        this.isMultiValued = isMultiValued;
        if (this.sourceExpression == null) {
            this.sourceExpression = Expression.NIL;
        }
        this.sourceIndex = sourceIndex;
        this.targetXPath = targetXPath;
        this.targetNodeInstance = targetNodeInstance;
        this.clientProperties = clientProperties == null ? Collections
                .<Name, Expression> emptyMap() : clientProperties;
    }

    public boolean isMultiValued() {
        return isMultiValued;
    }
    
    public boolean encodeIfEmpty() {
        return encodeIfEmpty;
    }
    
    public boolean isList() {
        return isList;
    }

    public Expression getSourceExpression() {
        return sourceExpression;
    }
    
    public String getSourceIndex() {
        return sourceIndex;
    }

    public StepList getTargetXPath() {
        return targetXPath;
    }

    public AttributeType getTargetNodeInstance() {
        return targetNodeInstance;
    }

    /**
     * This is overridden by NestedAttributeMapping
     * 
     * @return always return false
     */
    public boolean isNestedAttribute() {
        return false;
    }   
    
    /**********************************************************************
     * Label, parentLabel and instancePath are for web service backend only
     **********************************************************************/
    public String getLabel() {
        return label;
    }

    public String getParentLabel() {
        return parentLabel;
    }  
    
    public String getInstanceXpath() {
        return instancePath;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }

    public void setParentLabel(String label) {
        parentLabel = label;
    }  
    
    public void setInstanceXpath(String instancePath) {
        this.instancePath = instancePath;
    }
    
    public void setEncodeIfEmpty(boolean encodeIfEmpty) {
        this.encodeIfEmpty = encodeIfEmpty;
    }
    
    public void setList(boolean isList) {
        this.isList = isList;
    }
    
    /********END specific web service methods*******************/
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AttributeMapping)) {
            return false;
        }

        AttributeMapping other = (AttributeMapping) o;

        return Utilities.equals(identifierExpression, other.identifierExpression)
                && Utilities.equals(sourceExpression, other.sourceExpression)
                && Utilities.equals(targetXPath, other.targetXPath)
                && Utilities.equals(targetNodeInstance, other.targetNodeInstance)
                && Utilities.equals(isList, other.isList)
                && Utilities.equals(isMultiValued, other.isMultiValued)
                && Utilities.equals(clientProperties, other.clientProperties)
                && Utilities.equals(label, other.label)
                && Utilities.equals(parentLabel, other.parentLabel);
    }

    public int hashCode() {
        return (37 * identifierExpression.hashCode() + 37 * sourceExpression.hashCode())
                ^ targetXPath.hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("AttributeMapping[");
        sb.append("sourceExpression='").append(sourceExpression).append("', targetXPath='").append(
                targetXPath);
        if (targetNodeInstance != null) {
            sb.append(", target instance type=").append(targetNodeInstance);
        }
        sb.append("']");

        return sb.toString();
    }

    public Map<Name, Expression> getClientProperties() {
        return clientProperties == null ? Collections.<Name, Expression> emptyMap()
                : clientProperties;
    }

    public Expression getIdentifierExpression() {
        return identifierExpression;
    }

    public void setIdentifierExpression(Expression identifierExpression) {
        this.identifierExpression = identifierExpression;
    }

}
