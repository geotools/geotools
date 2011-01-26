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
package org.geotools.ows.bindings;

import net.opengis.ows10.HTTPType;
import net.opengis.ows10.Ows10Factory;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ows:_HTTP.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="_HTTP"&gt;
 *      &lt;choice maxOccurs="unbounded"&gt;
 *          &lt;element name="Get" type="ows:RequestMethodType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Connect point URL prefix and any constraints for the HTTP "Get" request method for this operation request. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element name="Post" type="ows:RequestMethodType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Connect point URL and any constraints for the HTTP "Post" request method for this operation request. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/choice&gt;
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
public class _HTTPBinding extends AbstractComplexEMFBinding {
    public _HTTPBinding(Ows10Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS._HTTP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
