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

package org.geotools.data.complex;

import java.util.Collections;
import java.util.Map;

import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.util.Utilities;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;

/**
 * @author Gabriel Roldan, Axios Engineering
 * @author Rini Angreani, Curtin University of Technology
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class AttributeMapping {

    /** Expression to set the Attribute's ID from, or {@linkplain Expression#NIL} */
    private Expression identifierExpression;

    private Expression sourceExpression;

    private StepList targetXPath;

    private boolean isMultiValued;

    /**
     * If present, represents our way to deal polymorphic attribute instances, so this node should
     * be of a subtype of the one referenced by {@link  #targetXPath}
     */
    AttributeType targetNodeInstance;

    private Map<Name, Expression> clientProperties;

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
        this(idExpression, sourceExpression, targetXPath, null, false, null);
    }

    public AttributeMapping(Expression idExpression, Expression sourceExpression,
            StepList targetXPath, AttributeType targetNodeInstance, boolean isMultiValued,
            Map<Name, Expression> clientProperties) {

        this.identifierExpression = idExpression == null ? Expression.NIL : idExpression;
        this.sourceExpression = sourceExpression == null ? Expression.NIL : sourceExpression;
        this.isMultiValued = isMultiValued;
        if (this.sourceExpression == null) {
            this.sourceExpression = Expression.NIL;
        }
        this.targetXPath = targetXPath;
        this.targetNodeInstance = targetNodeInstance;
        this.clientProperties = clientProperties == null ? Collections
                .<Name, Expression> emptyMap() : clientProperties;
    }

    public boolean isMultiValued() {
        return isMultiValued;
    }

    public Expression getSourceExpression() {
        return sourceExpression;
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
    
    /**
     * This is overridden by TreeAttributeMapping
     * 
     * @return always return false
     */
    public boolean isTreeAttribute() {
        return false;
    }
    
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
                && Utilities.equals(targetNodeInstance, other.targetNodeInstance);
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
