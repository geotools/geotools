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

import com.vividsolutions.jts.geom.Envelope;


/**
 * Binding object for the type http://www.opengis.net/gml:BoundingShapeType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="BoundingShapeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;         Bounding shapes--a Box or a null element
 *              are currently allowed.       &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;choice&gt;
 *              &lt;element ref="gml:Box"/&gt;
 *              &lt;element name="null" type="gml:NullType"/&gt;
 *          &lt;/choice&gt;
 *      &lt;/sequence&gt;
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
public class GMLBoundingShapeTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.BoundingShapeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Envelope.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //do the null check
        if (node.getChild("null") != null) {
            //ignore the description as to why its null
            Envelope e = new Envelope();
            e.setToNull();

            return e;
        }

        //has to be a valid bounding box
        return (Envelope) node.getChildValue(0);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        Envelope e = (Envelope) object;

        if (GML.Box.equals(name) && !e.isNull()) {
            return e;
        }

        if ("null".equals(name.getLocalPart()) && e.isNull()) {
            return e;
        }

        return null;
    }
}
