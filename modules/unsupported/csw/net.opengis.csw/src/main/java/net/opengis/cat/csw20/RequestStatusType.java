/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

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
     * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime"
     *        extendedMetaData="kind='attribute' name='timestamp'"
     * @generated
     */
    XMLGregorianCalendar getTimestamp();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.RequestStatusType#getTimestamp <em>Timestamp</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Timestamp</em>' attribute.
     * @see #getTimestamp()
     * @generated
     */
    void setTimestamp(XMLGregorianCalendar value);

} // RequestStatusType
