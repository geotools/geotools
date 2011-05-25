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
package org.geotools.gml3.bindings;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Binding object for the type http://www.opengis.net/gml:LocationPropertyType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="LocationPropertyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Convenience property for generalised location.
 *        A representative location for plotting or analysis.
 *        Often augmented by one or more additional geometry properties with more specific semantics.&lt;/documentation&gt;
 *          &lt;documentation&gt;Deprecated in GML 3.1.0&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence minOccurs="0"&gt;
 *          &lt;choice&gt;
 *              &lt;element ref="gml:_Geometry"/&gt;
 *              &lt;element ref="gml:LocationKeyWord"/&gt;
 *              &lt;element ref="gml:LocationString"/&gt;
 *              &lt;element ref="gml:Null"/&gt;
 *          &lt;/choice&gt;
 *      &lt;/sequence&gt;
 *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
 *  &lt;/complexType&gt;
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
public class LocationPropertyTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.LocationPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Geometry.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return node.getChildValue(Geometry.class);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (GML._Geometry.equals(name)) {
            return object;
        }

        return null;
    }
}
