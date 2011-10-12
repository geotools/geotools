/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v1_0.OGCPropertyIsBetweenTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the type http://www.opengis.net/fes/2.0:PropertyIsBetweenType.
 *
 * <p>
 *      <pre>
 *       <code>
 *  &lt;xsd:complexType name="PropertyIsBetweenType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="fes:ComparisonOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="fes:expression"/&gt;
 *                  &lt;xsd:element name="LowerBoundary" type="fes:LowerBoundaryType"/&gt;
 *                  &lt;xsd:element name="UpperBoundary" type="fes:UpperBoundaryType"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 *       </pre>
 * </p>
 *
 * @generated
 */
public class PropertyIsBetweenTypeBinding extends OGCPropertyIsBetweenTypeBinding {
    
    public PropertyIsBetweenTypeBinding(FilterFactory factory) {
        super(factory);
    }

    public QName getTarget() {
        return FES.PropertyIsBetweenType;
    }
}
