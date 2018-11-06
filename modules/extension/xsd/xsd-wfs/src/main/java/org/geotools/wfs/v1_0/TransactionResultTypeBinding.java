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
package org.geotools.wfs.v1_0;

import javax.xml.namespace.QName;
import net.opengis.wfs.TransactionResultsType;
import net.opengis.wfs.WfsFactory;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding object for the type http://www.opengis.net/wfs:TransactionResultsType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="TransactionResultsType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              The TransactionResults element may be used to report exception
 *              codes and messages for all actions of a transaction that failed
 *              to complete successfully.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0" name="Action" type="wfs:ActionType"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The Action element reports an exception code
 *                    and exception message indicating why the
 *                    corresponding action of a transaction request
 *                    failed.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class TransactionResultTypeBinding extends AbstractComplexEMFBinding {
    public TransactionResultTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WFS.TransactionResultType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return TransactionResultsType.class;
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        Element e = super.encode(object, document, value);

        TransactionResultsType resultType = (TransactionResultsType) object;

        Element node = document.createElementNS(WFS.NAMESPACE, "Status");
        e.appendChild(node);

        if (resultType.getAction().isEmpty()) {
            node.appendChild(document.createElementNS(WFS.NAMESPACE, "SUCCESS"));
        } else {
            node.appendChild(document.createElementNS(WFS.NAMESPACE, "FAILED"));
        }

        return e;
    }
}
