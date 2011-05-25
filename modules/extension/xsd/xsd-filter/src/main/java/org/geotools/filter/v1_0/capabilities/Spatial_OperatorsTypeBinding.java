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
package org.geotools.filter.v1_0.capabilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:Spatial_OperatorsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="Spatial_OperatorsType"&gt;
 *      &lt;xsd:choice maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:BBOX"/&gt;
 *          &lt;xsd:element ref="ogc:Equals"/&gt;
 *          &lt;xsd:element ref="ogc:Disjoint"/&gt;
 *          &lt;xsd:element ref="ogc:Intersect"/&gt;
 *          &lt;xsd:element ref="ogc:Touches"/&gt;
 *          &lt;xsd:element ref="ogc:Crosses"/&gt;
 *          &lt;xsd:element ref="ogc:Within"/&gt;
 *          &lt;xsd:element ref="ogc:Contains"/&gt;
 *          &lt;xsd:element ref="ogc:Overlaps"/&gt;
 *          &lt;xsd:element ref="ogc:Beyond"/&gt;
 *          &lt;xsd:element ref="ogc:DWithin"/&gt;
 *      &lt;/xsd:choice&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class Spatial_OperatorsTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public Spatial_OperatorsTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.Spatial_OperatorsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SpatialOperators.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List ops = new ArrayList();

        for (Iterator i = node.getChildren().iterator(); i.hasNext();) {
            Node child = (Node) i.next();
            ops.add(factory.spatialOperator(child.getComponent().getName(), null));
        }

        return factory.spatialOperators((SpatialOperator[]) ops.toArray(
                new SpatialOperator[ops.size()]));
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        SpatialOperators spatial = (SpatialOperators) object;

        return spatial.getOperator(name.getLocalPart());
    }
}
