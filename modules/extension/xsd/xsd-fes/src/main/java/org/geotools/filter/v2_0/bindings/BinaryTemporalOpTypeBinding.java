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
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.temporal.BinaryTemporalOperator;

/**
 * Binding object for the type http://www.opengis.net/fes/2.0:BinaryTemporalOpType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:complexType name="BinaryTemporalOpType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="fes:TemporalOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="fes:ValueReference"/&gt;
 *                  &lt;xsd:choice&gt;
 *                      &lt;xsd:element ref="fes:expression"/&gt;
 *                      &lt;xsd:any namespace="##other"/&gt;
 *                  &lt;/xsd:choice&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class BinaryTemporalOpTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return FES.BinaryTemporalOpType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BinaryTemporalOperator.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return null;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        return FESParseEncodeUtil.getProperty((BinaryTemporalOperator) object, name);
    }
}
