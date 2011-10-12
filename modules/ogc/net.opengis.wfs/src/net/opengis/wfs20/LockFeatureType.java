/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.math.BigInteger;

import net.opengis.fes20.AbstractQueryExpressionType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lock Feature Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.LockFeatureType#getAbstractQueryExpressionGroup <em>Abstract Query Expression Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.LockFeatureType#getAbstractQueryExpression <em>Abstract Query Expression</em>}</li>
 *   <li>{@link net.opengis.wfs20.LockFeatureType#getExpiry <em>Expiry</em>}</li>
 *   <li>{@link net.opengis.wfs20.LockFeatureType#getLockAction <em>Lock Action</em>}</li>
 *   <li>{@link net.opengis.wfs20.LockFeatureType#getLockId <em>Lock Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getLockFeatureType()
 * @model extendedMetaData="name='LockFeatureType' kind='elementOnly'"
 * @generated
 */
public interface LockFeatureType extends BaseRequestType {
    /**
     * Returns the value of the '<em><b>Abstract Query Expression Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Query Expression Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Query Expression Group</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getLockFeatureType_AbstractQueryExpressionGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
     *        extendedMetaData="kind='group' name='AbstractQueryExpression:group' namespace='http://www.opengis.net/fes/2.0'"
     * @generated
     */
    FeatureMap getAbstractQueryExpressionGroup();

    /**
     * Returns the value of the '<em><b>Abstract Query Expression</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.AbstractQueryExpressionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Query Expression</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Query Expression</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getLockFeatureType_AbstractQueryExpression()
     * @model containment="true" required="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractQueryExpression' namespace='http://www.opengis.net/fes/2.0' group='http://www.opengis.net/fes/2.0#AbstractQueryExpression:group'"
     * @generated
     */
    EList<AbstractQueryExpressionType> getAbstractQueryExpression();

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
     * @see net.opengis.wfs20.Wfs20Package#getLockFeatureType_Expiry()
     * @model default="300" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='expiry'"
     * @generated
     */
    BigInteger getExpiry();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.LockFeatureType#getExpiry <em>Expiry</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.wfs20.LockFeatureType#getExpiry <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetExpiry()
     * @see #getExpiry()
     * @see #setExpiry(BigInteger)
     * @generated
     */
    void unsetExpiry();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.LockFeatureType#getExpiry <em>Expiry</em>}' attribute is set.
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
     * @see net.opengis.wfs20.Wfs20Package#getLockFeatureType_LockAction()
     * @model default="ALL" unsettable="true"
     *        extendedMetaData="kind='attribute' name='lockAction'"
     * @generated
     */
    AllSomeType getLockAction();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.wfs20.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetLockAction()
     * @see #getLockAction()
     * @see #setLockAction(AllSomeType)
     * @generated
     */
    void unsetLockAction();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Lock Action</em>' attribute is set.
     * @see #unsetLockAction()
     * @see #getLockAction()
     * @see #setLockAction(AllSomeType)
     * @generated
     */
    boolean isSetLockAction();

    /**
     * Returns the value of the '<em><b>Lock Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lock Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lock Id</em>' attribute.
     * @see #setLockId(String)
     * @see net.opengis.wfs20.Wfs20Package#getLockFeatureType_LockId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='lockId'"
     * @generated
     */
    String getLockId();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.LockFeatureType#getLockId <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Id</em>' attribute.
     * @see #getLockId()
     * @generated
     */
    void setLockId(String value);

} // LockFeatureType
