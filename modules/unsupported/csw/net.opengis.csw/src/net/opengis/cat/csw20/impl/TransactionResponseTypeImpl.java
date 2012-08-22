/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.util.Collection;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.InsertResultType;
import net.opengis.cat.csw20.TransactionResponseType;
import net.opengis.cat.csw20.TransactionSummaryType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transaction Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionResponseTypeImpl#getTransactionSummary <em>Transaction Summary</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionResponseTypeImpl#getInsertResult <em>Insert Result</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionResponseTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransactionResponseTypeImpl extends EObjectImpl implements TransactionResponseType {
    /**
     * The cached value of the '{@link #getTransactionSummary() <em>Transaction Summary</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTransactionSummary()
     * @generated
     * @ordered
     */
    protected TransactionSummaryType transactionSummary;

    /**
     * The cached value of the '{@link #getInsertResult() <em>Insert Result</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInsertResult()
     * @generated
     * @ordered
     */
    protected EList<InsertResultType> insertResult;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TransactionResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.TRANSACTION_RESPONSE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TransactionSummaryType getTransactionSummary() {
        return transactionSummary;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTransactionSummary(TransactionSummaryType newTransactionSummary, NotificationChain msgs) {
        TransactionSummaryType oldTransactionSummary = transactionSummary;
        transactionSummary = newTransactionSummary;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY, oldTransactionSummary, newTransactionSummary);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTransactionSummary(TransactionSummaryType newTransactionSummary) {
        if (newTransactionSummary != transactionSummary) {
            NotificationChain msgs = null;
            if (transactionSummary != null)
                msgs = ((InternalEObject)transactionSummary).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY, null, msgs);
            if (newTransactionSummary != null)
                msgs = ((InternalEObject)newTransactionSummary).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY, null, msgs);
            msgs = basicSetTransactionSummary(newTransactionSummary, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY, newTransactionSummary, newTransactionSummary));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<InsertResultType> getInsertResult() {
        if (insertResult == null) {
            insertResult = new EObjectContainmentEList<InsertResultType>(InsertResultType.class, this, Csw20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULT);
        }
        return insertResult;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_RESPONSE_TYPE__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                return basicSetTransactionSummary(null, msgs);
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
                return ((InternalEList<?>)getInsertResult()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                return getTransactionSummary();
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
                return getInsertResult();
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__VERSION:
                return getVersion();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                setTransactionSummary((TransactionSummaryType)newValue);
                return;
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
                getInsertResult().clear();
                getInsertResult().addAll((Collection<? extends InsertResultType>)newValue);
                return;
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__VERSION:
                setVersion((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                setTransactionSummary((TransactionSummaryType)null);
                return;
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
                getInsertResult().clear();
                return;
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__VERSION:
                setVersion(VERSION_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                return transactionSummary != null;
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
                return insertResult != null && !insertResult.isEmpty();
            case Csw20Package.TRANSACTION_RESPONSE_TYPE__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (version: ");
        result.append(version);
        result.append(')');
        return result.toString();
    }

} //TransactionResponseTypeImpl
