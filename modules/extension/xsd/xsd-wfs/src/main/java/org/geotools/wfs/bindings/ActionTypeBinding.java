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
import net.opengis.wfs.ActionType;
import net.opengis.wfs.WfsFactory;
import org.geotools.wfs.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wfs:ActionType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="ActionType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="1" minOccurs="0" name="Message" type="xsd:string"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    If an action fails, the message element may be used
 *                    to supply an exception message.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="locator" type="xsd:string" use="required"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The locator attribute is used to locate an action
 *                 within a &lt;Transaction&gt; element.  The value
 *                 of the locator attribute is either a string that
 *                 is equal to the value of the handle attribute
 *                 specified on an  &lt;Insert&gt;, &lt;Update&gt;
 *                 or &lt;Delete&gt; action.  If a value is not
 *                 specified for the handle attribute then a WFS
 *                 may employ some other means of locating the
 *                 action.  For example, the value of the locator
 *                 attribute may be an integer indicating the order
 *                 of the action (i.e. 1=First action, 2=Second action,
 *                 etc.).
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute name="code" type="xsd:string" use="optional"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The code attribute may be used to specify an
 *                 exception code indicating why an action failed.
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class ActionTypeBinding extends AbstractComplexEMFBinding {
    public ActionTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WFS.ActionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ActionType.class;
    }
}
