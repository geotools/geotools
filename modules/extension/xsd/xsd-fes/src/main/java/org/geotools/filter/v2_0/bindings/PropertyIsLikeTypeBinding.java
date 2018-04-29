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
import org.geotools.filter.v1_0.OGCPropertyIsLikeTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsLike;

/**
 * Binding object for the type http://www.opengis.net/fes/2.0:PropertyIsLikeType.
 *
 * <p>
 *
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="PropertyIsLikeType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="fes:ComparisonOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element maxOccurs="2" minOccurs="2" ref="fes:expression"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="wildCard" type="xsd:string" use="required"/&gt;
 *              &lt;xsd:attribute name="singleChar" type="xsd:string" use="required"/&gt;
 *              &lt;xsd:attribute name="escapeChar" type="xsd:string" use="required"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *        </code>
 *       </pre>
 *
 * @generated
 */
public class PropertyIsLikeTypeBinding extends OGCPropertyIsLikeTypeBinding {
    FilterFactory factory;

    public PropertyIsLikeTypeBinding(FilterFactory factory) {
        super(factory);
        this.factory = factory;
    }

    public QName getTarget() {
        return FES.PropertyIsLikeType;
    }

    public Object getProperty(Object object, QName name) throws Exception {
        PropertyIsLike isLike = (PropertyIsLike) object;

        if (FES.expression.equals(name)) {
            return new Object[] {
                isLike.getExpression(),
                isLike.getLiteral() != null ? factory.literal(isLike.getLiteral()) : null
            };
        }

        return super.getProperty(object, name);
    }
}
