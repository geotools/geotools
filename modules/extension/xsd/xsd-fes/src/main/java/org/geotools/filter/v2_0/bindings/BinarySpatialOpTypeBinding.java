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
import org.opengis.filter.spatial.BinarySpatialOperator;

/**
 * <pre>
 *  &lt;xsd:complexType name="BinarySpatialOpType">
 *     &lt;xsd:complexContent>
 *        &lt;xsd:extension base="fes:SpatialOpsType">
 *           &lt;xsd:sequence>
 *              &lt;xsd:element ref="fes:ValueReference"/>
 *            &lt;xsd:choice>
 *                 &lt;xsd:element ref="fes:expression"/>
 *                 &lt;xsd:any namespace="##other"/>
 *              &lt;/xsd:choice>
 *           &lt;/xsd:sequence>
 *        &lt;/xsd:extension>
 *     &lt;/xsd:complexContent>
 *  &lt;/xsd:complexType>
 *  <pre>
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class BinarySpatialOpTypeBinding extends AbstractComplexBinding {

    @Override
    public QName getTarget() {
        return FES.BinarySpatialOpType;
    }

    @Override
    public Class getType() {
        return BinarySpatialOperator.class;
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        return FESParseEncodeUtil.getProperty((BinarySpatialOperator) object, name);
    }
}
