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

import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.SpatialOperator;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:SpatialOperatorType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="SpatialOperatorType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs="0" name="GeometryOperands" type="ogc:GeometryOperandsType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="name" type="ogc:SpatialOperatorNameType"/&gt;
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
public class SpatialOperatorTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public SpatialOperatorTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.SpatialOperatorType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SpatialOperator.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //&lt;xsd:element minOccurs="0" name="GeometryOperands" type="ogc:GeometryOperandsType"/&gt;
        GeometryOperand[] gos = (GeometryOperand[]) node.getChildValue(GeometryOperand[].class);

        //&lt;xsd:attribute name="name" type="ogc:SpatialOperatorNameType"/&gt;
        return factory.spatialOperator((String) node.getAttributeValue("name"), gos);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        SpatialOperator sop = (SpatialOperator) object;

        if ("GeometryOperands".equals(name.getLocalPart())) {
            return sop.getGeometryOperands();
        }

        if ("name".equals(name.getLocalPart())) {
            return sop.getName();
        }

        return null;
    }
}
