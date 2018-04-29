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
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.WfsFactory;
import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wfs:TransactionType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="TransactionType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              The TransactionType defines the Transaction operation.  A
 *              Transaction element contains one or more Insert, Update
 *              Delete and Native elements that allow a client application
 *              to create, modify or remove feature instances from the
 *              feature repository that a Web Feature Service controls.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="wfs:BaseRequestType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" ref="wfs:LockId"&gt;
 *                      &lt;xsd:annotation&gt;
 *                          &lt;xsd:documentation&gt;
 *                          In order for a client application to operate upon
 *                          locked feature instances, the Transaction request
 *                          must include the LockId element.  The content of
 *                          this element must be the lock identifier the client
 *                          application obtained from a previous
 *                          GetFeatureWithLock or LockFeature operation.
 *
 *                          If the correct lock identifier is specified the Web
 *                          Feature Service knows that the client application may
 *                          operate upon the locked feature instances.
 *
 *                          No LockId element needs to be specified to operate upon
 *                          unlocked features.
 *                       &lt;/xsd:documentation&gt;
 *                      &lt;/xsd:annotation&gt;
 *                  &lt;/xsd:element&gt;
 *                  &lt;xsd:choice maxOccurs="unbounded" minOccurs="0"&gt;
 *                      &lt;xsd:element ref="wfs:Insert"/&gt;
 *                      &lt;xsd:element ref="wfs:Update"/&gt;
 *                      &lt;xsd:element ref="wfs:Delete"/&gt;
 *                      &lt;xsd:element ref="wfs:Native"/&gt;
 *                  &lt;/xsd:choice&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="releaseAction" type="wfs:AllSomeType" use="optional"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                       The releaseAction attribute is used to control how a Web
 *                       Feature service releases locks on feature instances after
 *                       a Transaction request has been processed.
 *
 *                       Valid values are ALL or SOME.
 *
 *                       A value of ALL means that the Web Feature Service should
 *                       release the locks of all feature instances locked with the
 *                       specified lockId regardless or whether or not the features
 *                       were actually modified.
 *
 *                       A value of SOME means that the Web Feature Service will
 *                       only release the locks held on feature instances that
 *                       were actually operated upon by the transaction.  The
 *                       lockId that the client application obtained shall remain
 *                       valid and the other, unmodified, feature instances shall
 *                       remain locked.
 *
 *                       If the expiry attribute was specified in the original
 *                       operation that locked the feature instances, then the
 *                       expiry counter will be reset to give the client
 *                       application that same amount of time to post subsequent
 *                       transactions against the locked features.
 *                    &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 * @source $URL$
 */
public class TransactionTypeBinding extends AbstractComplexEMFBinding {
    public TransactionTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WFS.TransactionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class<?> getType() {
        return TransactionType.class;
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
