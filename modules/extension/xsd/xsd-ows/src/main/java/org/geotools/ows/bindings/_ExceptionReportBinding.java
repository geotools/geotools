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

import net.opengis.ows10.ExceptionReportType;
import net.opengis.ows10.Ows10Factory;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ows:_ExceptionReport.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="_ExceptionReport"&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" ref="ows:Exception"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Unordered list of one or more Exception elements that each describes an error. These Exception elements shall be interpreted by clients as being independent of one another (not hierarchical). &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute name="version" type="string" use="required"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Specification version for OWS operation. The string value shall contain one x.y.z "version" value (e.g., "2.1.3"). A version number shall contain three non-negative integers separated by decimal points, in the form "x.y.z". The integers y and z shall not exceed 99. Each version shall be for the Implementation Specification (document) and the associated XML Schemas to which requested operations will conform. An Implementation Specification version normally specifies XML Schemas against which an XML encoded operation response must conform and should be validated. See Version negotiation subclause for more information. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *      &lt;attribute name="language" type="language" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Identifier of the language used by all included exception text values. These language identifiers shall be as specified in IETF RFC 1766. When this attribute is omitted, the language used is not identified. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
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
public class _ExceptionReportBinding extends AbstractComplexEMFBinding {
    public _ExceptionReportBinding(Ows10Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS._ExceptionReport;
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
