/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/gml:AbstractGMLType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType abstract="true" name="AbstractGMLType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt; The optional attribute "id" is omitted from this profile. &lt;/documentation&gt;
 *          &lt;documentation&gt;All complexContent GML elements are directly or indirectly derived from this abstract supertype to establish a hierarchy of GML types that may be distinguished from other XML types by their ancestry. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:metaDataProperty"/&gt;
 *          &lt;element minOccurs="0" ref="gml:description"/&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:name"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Multiple names may be provided.  These will often be distinguished by being assigned by different authorities, as indicated by the value of the codeSpace attribute.  In an instance document there will usually only be one name per authority. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class AbstractGMLTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return GML.AbstractGMLType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
