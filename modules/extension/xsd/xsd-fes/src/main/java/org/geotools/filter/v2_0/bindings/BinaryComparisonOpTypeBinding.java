/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.expression.Expression;

/**
 * <pre>
 * &lt;xsd:complexType name="BinaryComparisonOpType">
 *     &lt;xsd:complexContent>
 *        &lt;xsd:extension base="fes:ComparisonOpsType">
 *           &lt;xsd:sequence>
 *              &lt;xsd:element ref="fes:expression" minOccurs="2" maxOccurs="2"/>
 *           &lt;/xsd:sequence>
 *           &lt;xsd:attribute name="matchCase" type="xsd:boolean"
 *                          use="optional" default="true"/>
 *           &lt;xsd:attribute name="matchAction" type="fes:MatchActionType"
 *                          use="optional" default="Any"/>
 *        &lt;/xsd:extension>
 *     &lt;/xsd:complexContent>
 *  &lt;/xsd:complexType>
 *  <pre>
 *  
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class BinaryComparisonOpTypeBinding extends AbstractComplexBinding {

    @Override
    public QName getTarget() {
        return FES.BinaryComparisonOpType;
    }

    @Override
    public Class getType() {
        return BinaryComparisonOperator.class;
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        BinaryComparisonOperator op = (BinaryComparisonOperator) object;
        if ("matchAction".equals(name.getLocalPart())) {
            return op.getMatchAction().name();
        }
        if ("matchCase".equals(name.getLocalPart())) {
            return op.isMatchingCase();
        }
        if (FES.expression.equals(name)) {
            return new Expression[]{op.getExpression1(), op.getExpression2()};
        }
        return null;
    }
}
