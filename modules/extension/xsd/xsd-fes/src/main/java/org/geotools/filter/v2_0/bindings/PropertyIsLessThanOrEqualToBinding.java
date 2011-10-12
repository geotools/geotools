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
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.expression.Expression;
import org.geotools.filter.v1_0.OGCPropertyIsLessThanOrEqualToBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the element http://www.opengis.net/fes/2.0:PropertyIsLessThanOrEqualTo.
 *
 * <p>
 *      <pre>
 *       <code>
 *  &lt;xsd:element name="PropertyIsLessThanOrEqualTo"
 *      substitutionGroup="fes:comparisonOps" type="fes:BinaryComparisonOpType"/&gt; 
 *              
 *        </code>
 *       </pre>
 * </p>
 *
 * @generated
 */
public class PropertyIsLessThanOrEqualToBinding extends OGCPropertyIsLessThanOrEqualToBinding {

    public PropertyIsLessThanOrEqualToBinding(FilterFactory filterfactory) {
        super(filterfactory);
    }

    public QName getTarget() {
        return FES.PropertyIsLessThanOrEqualTo;
    }
}
