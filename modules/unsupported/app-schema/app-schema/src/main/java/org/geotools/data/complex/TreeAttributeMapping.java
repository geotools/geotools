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
import java.util.Map;

import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.util.Utilities;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;

/**
 * An attributeMapping implementation that is part of a tree hierachy, defined in a single mapping file.
 * 
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 */
public class TreeAttributeMapping extends AttributeMapping {
    private String label;
    private String parentLabel;
    private String targetQueryString;
    private String instanceXpath;
    
    /**
     * Sole constructor
     * 
     * @param idExpression
     * @param parentExpression
     * @param targetXPath
     * @param targetNodeInstance
     * @param isMultiValued
     * @param clientProperties
     * @param sourceElement
     *            parent feature element type
     * @param sourcePath
     *            XPath link to nested feature
     * @param parentSource
     *            parent feature source
     * @throws IOException
     */
    public TreeAttributeMapping(Expression idExpression, Expression parentExpression,
            StepList targetXPath, AttributeType expectedInstanceOf, boolean isMultiValued, Map<Name, Expression> clientProperties,
            String label, String parentLabel, String targetQueryString, String instanceXpath) {
        super(idExpression, parentExpression, targetXPath, expectedInstanceOf, isMultiValued, clientProperties);
        this.label = label;
        this.parentLabel = parentLabel;
        this.targetQueryString = targetQueryString;
        this.instanceXpath = instanceXpath;        
    }
    
    public String getLabel() {
        return label;
    }

    public String getParentLabel() {
        return parentLabel;
    }
        
    public String getTargetQueryString() {
        return targetQueryString;
    }

    public String getInstanceXpath() {
        return instanceXpath;
    }

    @Override
    /*
     * @see org.geotools.data.complex.AttributeMapping#isNestedAttribute()
     */
    public boolean isTreeAttribute() {
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TreeAttributeMapping)) {
            return false;
        }

        TreeAttributeMapping other = (TreeAttributeMapping) o;
        
        return Utilities.equals(this.getIdentifierExpression(), other.getIdentifierExpression())
                && Utilities.equals(this.getSourceExpression(), other.getSourceExpression())
                && Utilities.equals(this.getTargetXPath(), other.getTargetXPath())            
                && Utilities.equals(this.parentLabel, other.parentLabel)
                && Utilities.equals(this.label, other.label)
                && Utilities.equals(this.targetQueryString, other.targetQueryString)
                && Utilities.equals(this.instanceXpath, other.instanceXpath);
    }

    public int hashCode() {
        int seed = 17;
        return Utilities.hash(getIdentifierExpression(), seed) + Utilities.hash(getSourceExpression(), seed) + 
            Utilities.hash(getTargetXPath(), seed) + 
            Utilities.hash(parentLabel, seed) + Utilities.hash(label, seed) + 
            Utilities.hash(targetQueryString, seed) + Utilities.hash(instanceXpath, seed);        
    }
}
