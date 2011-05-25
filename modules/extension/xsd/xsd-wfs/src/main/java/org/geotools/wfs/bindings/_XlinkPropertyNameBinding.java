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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.WfsFactory;
import net.opengis.wfs.XlinkPropertyNameType;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ComplexBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


/**
 * Binding object for the type http://www.opengis.net/wfs:_XlinkPropertyName.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="_XlinkPropertyName"&gt;
 *      &lt;xsd:simpleContent&gt;
 *          &lt;xsd:extension base="xsd:string"&gt;
 *              &lt;xsd:attribute name="traverseXlinkDepth" type="xsd:string" use="required"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                    This attribute indicates the depth to which nested property
 *                    XLink linking element locator attribute (href) XLinks are
 *                    traversed and resolved if possible.  A value of "1" indicates
 *                    that one linking element locator attribute (href) Xlink
 *                    will be traversed and the referenced element returned if
 *                    possible, but nested property XLink linking element locator
 *                    attribute (href) XLinks in the returned element are not
 *                    traversed.  A value of  "*" indicates that all nested property
 *                    XLink linking element locator attribute (href) XLinks will be
 *                    traversed and the referenced elements returned if possible.
 *                    The range of valid values for this attribute consists of
 *                    positive integers plus "*".
 *                       &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *              &lt;xsd:attribute name="traverseXlinkExpiry"
 *                  type="xsd:positiveInteger" use="optional"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                    The traverseXlinkExpiry attribute value is specified in
 *                    minutes It indicates how long a Web Feature Service should
 *                    wait to receive a response to a nested GetGmlObject request.
 *                       &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:simpleContent&gt;
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
public class _XlinkPropertyNameBinding extends AbstractComplexEMFBinding {
    public _XlinkPropertyNameBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS._XlinkPropertyName;
    }

    /**
     * Explicit implementation of
     * {@link ComplexBinding#encode(Object, Document, Element)} as
     * {@link AbstractComplexEMFBinding#encode(Object, Document, Element)} does
     * not set the value.
     *
     * @see ComplexBinding#encode(Object, Document, Element).
     */
    public Element encode(final Object object, final Document document, Element value)
        throws Exception {
        final XlinkPropertyNameType xlink = (XlinkPropertyNameType) object;
        final String textValue = xlink.getValue();
        final Text textNode = document.createTextNode(textValue);
        value.appendChild(textNode);

        return value;
    }
}
