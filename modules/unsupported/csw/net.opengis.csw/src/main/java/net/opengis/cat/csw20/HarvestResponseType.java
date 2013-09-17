/**
 * <copyright>
 * (C) 2012, Open Source Geospatial Foundation (OSGeo)
 * (C) 2004,2010 Open Geospatial Consortium.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Harvest Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.HarvestResponseType#getAcknowledgement <em>Acknowledgement</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.HarvestResponseType#getTransactionResponse <em>Transaction Response</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getHarvestResponseType()
 * @model extendedMetaData="name='HarvestResponseType' kind='elementOnly'"
 * @generated
 */
public interface HarvestResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Acknowledgement</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Acknowledgement</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Acknowledgement</em>' containment reference.
     * @see #setAcknowledgement(AcknowledgementType)
     * @see net.opengis.cat.csw20.Csw20Package#getHarvestResponseType_Acknowledgement()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Acknowledgement' namespace='##targetNamespace'"
     * @generated
     */
    AcknowledgementType getAcknowledgement();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.HarvestResponseType#getAcknowledgement <em>Acknowledgement</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Acknowledgement</em>' containment reference.
     * @see #getAcknowledgement()
     * @generated
     */
    void setAcknowledgement(AcknowledgementType value);

    /**
     * Returns the value of the '<em><b>Transaction Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Transaction Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Transaction Response</em>' containment reference.
     * @see #setTransactionResponse(TransactionResponseType)
     * @see net.opengis.cat.csw20.Csw20Package#getHarvestResponseType_TransactionResponse()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TransactionResponse' namespace='##targetNamespace'"
     * @generated
     */
    TransactionResponseType getTransactionResponse();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.HarvestResponseType#getTransactionResponse <em>Transaction Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transaction Response</em>' containment reference.
     * @see #getTransactionResponse()
     * @generated
     */
    void setTransactionResponse(TransactionResponseType value);

} // HarvestResponseType
