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

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Request Status Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This element provides information about the status of the
 *             search request.
 * 
 *             status    - status of the search
 *             timestamp - the date and time when the result set was modified
 *                         (ISO 8601 format: YYYY-MM-DDThh:mm:ss[+|-]hh:mm).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.RequestStatusType#getTimestamp <em>Timestamp</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getRequestStatusType()
 * @model extendedMetaData="name='RequestStatusType' kind='empty'"
 * @generated
 */
public interface RequestStatusType extends EObject {
    /**
     * Returns the value of the '<em><b>Timestamp</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Timestamp</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Timestamp</em>' attribute.
     * @see #setTimestamp(XMLGregorianCalendar)
     * @see net.opengis.cat.csw20.Csw20Package#getRequestStatusType_Timestamp()
     * @model
     */
    Calendar getTimestamp();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.RequestStatusType#getTimestamp <em>Timestamp</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Timestamp</em>' attribute.
     * @see #getTimestamp()
     * @generated
     */
    void setTimestamp(Calendar value);

   
} // RequestStatusType
