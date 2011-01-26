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

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiLineString;


/**
 * Binding object for the type http://www.opengis.net/gml:MultiLineStringType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="MultiLineStringType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         A MultiLineString is defined by one or
 *              more LineStrings, referenced          through
 *              lineStringMember elements.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base="gml:GeometryCollectionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="gml:lineStringMember" maxOccurs="unbounded"/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute name="gid" type="ID" use="optional"/&gt;
 *              &lt;attribute name="srsName" type="anyURI" use="required"/&gt;
 *          &lt;/restriction&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class GMLMultiLineStringTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public GMLMultiLineStringTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.MultiLineString;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return MultiLineString.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return GML2ParsingUtils.GeometryCollectionType_parse(node, MultiLineString.class, gFactory);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (GML.lineStringMember.equals(name)) {
            return GML2ParsingUtils.asCollection((MultiLineString) object);
        }

        return GML2ParsingUtils.GeometryCollectionType_getProperty(object, name);
    }
}
