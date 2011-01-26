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

import net.opengis.wfs.TransactionSummaryType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type
 * http://www.opengis.net/wfs:TransactionSummaryType.
 * 
 * <p>
 * 
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name=&quot;TransactionSummaryType&quot;&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation xml:lang=&quot;en&quot;&gt;
 *              Reports the total number of features affected by some kind
 *              of write action (i.e, insert, update, delete).
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs=&quot;0&quot; name=&quot;totalInserted&quot; type=&quot;xsd:nonNegativeInteger&quot;/&gt;
 *          &lt;xsd:element minOccurs=&quot;0&quot; name=&quot;totalUpdated&quot; type=&quot;xsd:nonNegativeInteger&quot;/&gt;
 *          &lt;xsd:element minOccurs=&quot;0&quot; name=&quot;totalDeleted&quot; type=&quot;xsd:nonNegativeInteger&quot;/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 * </code>
 *         </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL$
 */
public class TransactionSummaryTypeBinding extends AbstractComplexEMFBinding {
    private WfsFactory factory;

    public TransactionSummaryTypeBinding(WfsFactory factory) {
        super(factory);
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.TransactionSummaryType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return TransactionSummaryType.class;
    }

    public Object parse(ElementInstance instance, Node node, Object value)
    throws Exception {
        //TransactionSummaryType ts = factory.createTransactionSummaryType();
     return super.parse(instance, node, value);   
    }
}
