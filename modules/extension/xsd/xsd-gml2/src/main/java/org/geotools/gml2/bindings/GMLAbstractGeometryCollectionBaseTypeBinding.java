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
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.GeometryCollection;


/**
 * Binding object for the type http://www.opengis.net/gml:AbstractGeometryCollectionBaseType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="AbstractGeometryCollectionBaseType" abstract="true"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         This abstract base type for geometry
 *              collections just makes the          srsName attribute
 *              mandatory.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base="gml:AbstractGeometryType"&gt;
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
 *
 * @source $URL$
 * JD: this binding should not longer be used
 */
public class GMLAbstractGeometryCollectionBaseTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.AbstractGeometryCollectionBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GeometryCollection.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return null;
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        return GML2ParsingUtils.GeometryCollectionType_getProperty(object, name);
    }
}
