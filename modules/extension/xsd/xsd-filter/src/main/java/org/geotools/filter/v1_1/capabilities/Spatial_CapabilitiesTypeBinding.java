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
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperators;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:Spatial_CapabilitiesType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="Spatial_CapabilitiesType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="GeometryOperands" type="ogc:GeometryOperandsType"/&gt;
 *          &lt;xsd:element name="SpatialOperators" type="ogc:SpatialOperatorsType"/&gt;
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
public class Spatial_CapabilitiesTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public Spatial_CapabilitiesTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.Spatial_CapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SpatialCapabilities.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return factory.spatialCapabilities((GeometryOperand[]) node.getChildValue(
                GeometryOperand[].class),
            (SpatialOperators) node.getChildValue(SpatialOperators.class));
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        SpatialCapabilities spatial = (SpatialCapabilities) object;

        if ("SpatialOperators".equals(name.getLocalPart())) {
            return spatial.getSpatialOperators();
        }

        if ("GeometryOperands".equals(name.getLocalPart())) {
            return spatial.getGeometryOperands();
        }

        return null;
    }
}
