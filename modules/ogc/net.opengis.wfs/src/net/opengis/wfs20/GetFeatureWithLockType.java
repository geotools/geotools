/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.math.BigInteger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Feature With Lock Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.GetFeatureWithLockType#getExpiry <em>Expiry</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureWithLockType#getLockAction <em>Lock Action</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getGetFeatureWithLockType()
 * @model extendedMetaData="name='GetFeatureWithLockType' kind='elementOnly'"
 * @generated
 */
public interface GetFeatureWithLockType extends GetFeatureType {
    /**
     * Returns the value of the '<em><b>Expiry</b></em>' attribute.
     * The default value is <code>"300"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expiry</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expiry</em>' attribute.
     * @see #isSetExpiry()
     * @see #unsetExpiry()
     * @see #setExpiry(BigInteger)
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureWithLockType_Expiry()
     * @model default="300" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='expiry'"
     * @generated
     */
    BigInteger getExpiry();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureWithLockType#getExpiry <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Expiry</em>' attribute.
     * @see #isSetExpiry()
     * @see #unsetExpiry()
     * @see #getExpiry()
     * @generated
     */
    void setExpiry(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.GetFeatureWithLockType#getExpiry <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetExpiry()
     * @see #getExpiry()
     * @see #setExpiry(BigInteger)
     * @generated
     */
    void unsetExpiry();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.GetFeatureWithLockType#getExpiry <em>Expiry</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Expiry</em>' attribute is set.
     * @see #unsetExpiry()
     * @see #getExpiry()
     * @see #setExpiry(BigInteger)
     * @generated
     */
    boolean isSetExpiry();

    /**
     * Returns the value of the '<em><b>Lock Action</b></em>' attribute.
     * The default value is <code>"ALL"</code>.
     * The literals are from the enumeration {@link net.opengis.wfs20.AllSomeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lock Action</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lock Action</em>' attribute.
     * @see net.opengis.wfs20.AllSomeType
     * @see #isSetLockAction()
     * @see #unsetLockAction()
     * @see #setLockAction(AllSomeType)
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureWithLockType_LockAction()
     * @model default="ALL" unsettable="true"
     *        extendedMetaData="kind='attribute' name='lockAction'"
     * @generated
     */
    AllSomeType getLockAction();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureWithLockType#getLockAction <em>Lock Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Action</em>' attribute.
     * @see net.opengis.wfs20.AllSomeType
     * @see #isSetLockAction()
     * @see #unsetLockAction()
     * @see #getLockAction()
     * @generated
     */
    void setLockAction(AllSomeType value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.GetFeatureWithLockType#getLockAction <em>Lock Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetLockAction()
     * @see #getLockAction()
     * @see #setLockAction(AllSomeType)
     * @generated
     */
    void unsetLockAction();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.GetFeatureWithLockType#getLockAction <em>Lock Action</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Lock Action</em>' attribute is set.
     * @see #unsetLockAction()
     * @see #getLockAction()
     * @see #setLockAction(AllSomeType)
     * @generated
     */
    boolean isSetLockAction();

} // GetFeatureWithLockType
