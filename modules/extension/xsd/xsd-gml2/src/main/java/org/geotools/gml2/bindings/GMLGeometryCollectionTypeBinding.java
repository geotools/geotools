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

import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 * Binding object for the type http://www.opengis.net/gml:GeometryCollectionType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="GeometryCollectionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         A geometry collection must include one
 *              or more geometries, referenced          through
 *              geometryMember elements. User-defined geometry collections
 *              that accept GML geometry classes as members must
 *              instantiate--or          derive from--this type.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometryCollectionBaseType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="gml:geometryMember" maxOccurs="unbounded"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
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
public class GMLGeometryCollectionTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public GMLGeometryCollectionTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.GeometryCollectionType;
    }

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
        return GeometryCollection.class;
    }

    /**
     * <!-- begin-user-doc -->
     * This method returns an object of type @link GeometryCollection
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return GML2ParsingUtils.GeometryCollectionType_parse(node, GeometryCollection.class, gFactory);
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (GML.geometryMember.equals(name)) {
            return GML2ParsingUtils.asCollection((GeometryCollection) object);
        }
        
        return GML2ParsingUtils.GeometryCollectionType_getProperty(object, name);
    }
}
