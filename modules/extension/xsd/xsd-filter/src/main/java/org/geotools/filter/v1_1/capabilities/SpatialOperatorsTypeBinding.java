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
import org.geotools.filter.v1_1.OGC;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;

/**
 * Binding object for the type http://www.opengis.net/ogc:SpatialOperatorsType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="SpatialOperatorsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="SpatialOperator" type="ogc:SpatialOperatorType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SpatialOperatorsTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public SpatialOperatorsTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return OGC.SpatialOperatorsType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SpatialOperators.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        List sops = node.getChildValues(SpatialOperator.class);

        return factory.spatialOperators(
                (SpatialOperator[]) sops.toArray(new SpatialOperator[sops.size()]));
    }

    public Object getProperty(Object object, QName name) throws Exception {
        SpatialOperators sops = (SpatialOperators) object;

        if ("SpatialOperator".equals(name.getLocalPart())) {
            return sops.getOperators();
        }

        return null;
    }
}
