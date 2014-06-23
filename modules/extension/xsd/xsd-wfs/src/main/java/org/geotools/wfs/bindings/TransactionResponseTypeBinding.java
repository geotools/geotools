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

import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.v1_1.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;


/**
 * Binding object for the type http://www.opengis.net/wfs:TransactionResponseType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="TransactionResponseType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation xml:lang="en"&gt;
 *              The response for a transaction request that was successfully
 *              completed. If the transaction failed for any reason, an
 *              exception report is returned instead.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="TransactionSummary" type="wfs:TransactionSummaryType"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation xml:lang="en"&gt;
 *                    The TransactionSummary element is used to summarize
 *                    the number of feature instances affected by the
 *                    transaction.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element minOccurs="0" name="TransactionResults" type="wfs:TransactionResultsType"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation xml:lang="en"&gt;
 *                    For systems that do not support atomic transactions,
 *                    the TransactionResults element may be used to report
 *                    exception codes and messages for all actions of a
 *                    transaction that failed to execute successfully.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element name="InsertResults" type="wfs:InsertResultsType"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation xml:lang="en"&gt;
 *                    A transaction is a collection of Insert,Update and Delete
 *                    actions.  The Update and Delete actions modify features
 *                    that already exist.  The Insert action, however, creates
 *                    new features.  The InsertResults element is used to
 *                    report the identifiers of the newly created features.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute fixed="1.1.0" name="version" type="xsd:string" use="required"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The version attribute contains the version of the request
 *                 that generated this response.  So a V1.1.0 transaction
 *                 request generates a V1.1.0 transaction response.
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class TransactionResponseTypeBinding extends AbstractComplexEMFBinding {
    public TransactionResponseTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.TransactionResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return TransactionResponseType.class;
    }
}
