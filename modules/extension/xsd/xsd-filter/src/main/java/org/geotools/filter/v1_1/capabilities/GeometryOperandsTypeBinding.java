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
package org.geotools.filter.v1_1.capabilities;

import java.util.List;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.GeometryOperand;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:GeometryOperandsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="GeometryOperandsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="GeometryOperand" type="ogc:GeometryOperandType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class GeometryOperandsTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.GeometryOperandsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GeometryOperand[].class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List gops = node.getChildValues(GeometryOperand.class);

        return gops.toArray(new GeometryOperand[gops.size()]);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("GeometryOperand".equals(name.getLocalPart())) {
            return object;
        }

        return null;
    }
}
