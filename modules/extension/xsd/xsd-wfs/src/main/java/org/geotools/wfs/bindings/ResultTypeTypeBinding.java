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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.ResultTypeType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wfs:ResultTypeType.
 * 
 * <p>
 * 
 * <pre>
 *         <code>
 *  &lt;xsd:simpleType name=&quot;ResultTypeType&quot;&gt;
 *      &lt;xsd:restriction base=&quot;xsd:string&quot;&gt;
 *          &lt;xsd:enumeration value=&quot;results&quot;&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    Indicates that a complete response should be generated
 *                    by the WFS.  That is, all response feature instances
 *                    should be returned to the client.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:enumeration&gt;
 *          &lt;xsd:enumeration value=&quot;hits&quot;&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    Indicates that an empty response should be generated with
 *                    the &quot;numberOfFeatures&quot; attribute set (i.e. no feature
 *                    instances should be returned).  In this manner a client may
 *                    determine the number of feature instances that a GetFeature
 *                    request will return without having to actually get the
 *                    entire result set back.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:enumeration&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 * </code>
 *         </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL$
 */
public class ResultTypeTypeBinding extends AbstractSimpleBinding {
    public ResultTypeTypeBinding(WfsFactory factory) {
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.ResultTypeType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return ResultTypeType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        assert value instanceof String;
        String literal = (String) value;
        ResultTypeType resultTypeType = ResultTypeType.get(literal);
        return resultTypeType;
    }
}
