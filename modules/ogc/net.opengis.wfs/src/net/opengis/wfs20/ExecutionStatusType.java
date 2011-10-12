/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Execution Status Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.ExecutionStatusType#getStatus <em>Status</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getExecutionStatusType()
 * @model extendedMetaData="name='ExecutionStatusType' kind='empty'"
 * @generated
 */
public interface ExecutionStatusType extends EObject {
    /**
     * Returns the value of the '<em><b>Status</b></em>' attribute.
     * The default value is <code>"OK"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Status</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Status</em>' attribute.
     * @see #isSetStatus()
     * @see #unsetStatus()
     * @see #setStatus(String)
     * @see net.opengis.wfs20.Wfs20Package#getExecutionStatusType_Status()
     * @model default="OK" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='status'"
     * @generated
     */
    String getStatus();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ExecutionStatusType#getStatus <em>Status</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Status</em>' attribute.
     * @see #isSetStatus()
     * @see #unsetStatus()
     * @see #getStatus()
     * @generated
     */
    void setStatus(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.ExecutionStatusType#getStatus <em>Status</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStatus()
     * @see #getStatus()
     * @see #setStatus(String)
     * @generated
     */
    void unsetStatus();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.ExecutionStatusType#getStatus <em>Status</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Status</em>' attribute is set.
     * @see #unsetStatus()
     * @see #getStatus()
     * @see #setStatus(String)
     * @generated
     */
    boolean isSetStatus();

} // ExecutionStatusType
