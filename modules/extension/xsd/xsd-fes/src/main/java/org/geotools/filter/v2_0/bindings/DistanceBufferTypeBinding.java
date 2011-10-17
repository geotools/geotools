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

import java.util.List;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.*;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.DistanceBufferOperator;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/fes/2.0:DistanceBufferType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:complexType name="DistanceBufferType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="fes:SpatialOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" ref="fes:expression"/&gt;
 *                  &lt;xsd:any namespace="##other"/&gt;
 *                  &lt;xsd:element name="Distance" type="fes:MeasureType"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class DistanceBufferTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return FES.DistanceBufferType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return DistanceBufferOperator.class;
    }

//    @Override
//    public Object getProperty(Object object, QName name) throws Exception {
//        return FESParseEncodeUtil.getProperty((DistanceBufferOperator) object, name);
//    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        return FESParseEncodeUtil.getProperties((DistanceBufferOperator)object);
    }

}