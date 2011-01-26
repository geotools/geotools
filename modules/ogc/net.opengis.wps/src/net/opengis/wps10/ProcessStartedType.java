/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Started Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Indicates that this process has been has been accepted by the server, and processing has begun.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ProcessStartedType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wps10.ProcessStartedType#getPercentCompleted <em>Percent Completed</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getProcessStartedType()
 * @model extendedMetaData="name='ProcessStartedType' kind='simple'"
 * @generated
 */
public interface ProcessStartedType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A human-readable text string whose contents are left open to definition by each WPS server, but is expected to include any messages the server may wish to let the clients know. Such information could include how much longer the process may take to execute, or any warning conditions that may have been encountered to date. The client may display this text to a human user.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see net.opengis.wps10.Wps10Package#getProcessStartedType_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessStartedType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

    /**
     * Returns the value of the '<em><b>Percent Completed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Percentage of process that has been completed, where 0 means the process has just started, and 99 means the process is almost complete.  This value is expected to be accurate to within ten percent.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Percent Completed</em>' attribute.
     * @see #setPercentCompleted(BigInteger)
     * @see net.opengis.wps10.Wps10Package#getProcessStartedType_PercentCompleted()
     * @model dataType="net.opengis.wps10.PercentCompletedType"
     *        extendedMetaData="kind='attribute' name='percentCompleted'"
     * @generated
     */
    BigInteger getPercentCompleted();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessStartedType#getPercentCompleted <em>Percent Completed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Percent Completed</em>' attribute.
     * @see #getPercentCompleted()
     * @generated
     */
    void setPercentCompleted(BigInteger value);

} // ProcessStartedType
