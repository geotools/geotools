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

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Requests the actual values of some specified request parameter
 *         or other data element.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.GetDomainType#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetDomainType#getParameterName <em>Parameter Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getGetDomainType()
 * @model extendedMetaData="name='GetDomainType' kind='elementOnly'"
 * @generated
 */
public interface GetDomainType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Property Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Property Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Property Name</em>' attribute.
     * @see #setPropertyName(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetDomainType_PropertyName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='PropertyName' namespace='##targetNamespace'"
     * @generated
     */
    String getPropertyName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetDomainType#getPropertyName <em>Property Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Property Name</em>' attribute.
     * @see #getPropertyName()
     * @generated
     */
    void setPropertyName(String value);

    /**
     * Returns the value of the '<em><b>Parameter Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Name</em>' attribute.
     * @see #setParameterName(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetDomainType_ParameterName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='ParameterName' namespace='##targetNamespace'"
     * @generated
     */
    String getParameterName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetDomainType#getParameterName <em>Parameter Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter Name</em>' attribute.
     * @see #getParameterName()
     * @generated
     */
    void setParameterName(String value);

} // GetDomainType
