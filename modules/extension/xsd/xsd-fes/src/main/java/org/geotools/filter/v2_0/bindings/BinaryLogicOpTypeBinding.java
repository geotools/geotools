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

import org.geotools.filter.v1_0.OGCBinaryLogicOpTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.opengis.filter.FilterFactory;

/**
 * <pre>
 * &lt;xsd:complexType name="BinaryLogicOpType">
 *     &lt;xsd:complexContent>
 *        &lt;xsd:extension base="fes:LogicOpsType">
 *           &lt;xsd:choice minOccurs="2" maxOccurs="unbounded">
 *              &lt;xsd:group ref="fes:FilterPredicates"/>
 *           &lt;/xsd:choice>
 *        &lt;/xsd:extension>
 *     &lt;/xsd:complexContent>
 *  &lt;/xsd:complexType>
 *  <pre>
 *  
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class BinaryLogicOpTypeBinding extends OGCBinaryLogicOpTypeBinding {

    public BinaryLogicOpTypeBinding(FilterFactory factory) {
        super(factory);
    }

    @Override
    public QName getTarget() {
        return FES.BinaryLogicOpType;
    }

}
