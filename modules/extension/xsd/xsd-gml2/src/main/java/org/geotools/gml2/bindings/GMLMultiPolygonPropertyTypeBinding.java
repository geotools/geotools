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
package org.geotools.gml2.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml2.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * Binding object for the type http://www.opengis.net/gml:MultiPolygonPropertyType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="MultiPolygonPropertyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         Encapsulates a MultiPolygon to represent
 *              the following discontiguous          geometric properties:
 *              multiCoverage, multiExtentOf.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base="gml:GeometryAssociationType"&gt;
 *              &lt;sequence minOccurs="0"&gt;
 *                  &lt;element ref="gml:MultiPolygon"/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attributeGroup ref="xlink:simpleLink"/&gt;
 *              &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
 *          &lt;/restriction&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class GMLMultiPolygonPropertyTypeBinding extends AbstractComplexBinding {
    /** @generated */
    @Override
    public QName getTarget() {
        return GML.MultiPolygonPropertyType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return MultiPolygon.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return node.getChildValue(MultiPolygon.class);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        return GML2EncodingUtils.GeometryPropertyType_getProperty((MultiPolygon) object, name, false);
    }

    @Override
    public List<Object[]> getProperties(Object object, XSDElementDeclaration element) throws Exception {
        return GML2EncodingUtils.GeometryPropertyType_getProperties((MultiPolygon) object);
    }
}
