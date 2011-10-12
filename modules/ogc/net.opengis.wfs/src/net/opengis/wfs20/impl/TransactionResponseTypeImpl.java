/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import net.opengis.wfs20.ActionResultsType;
import net.opengis.wfs20.TransactionResponseType;
import net.opengis.wfs20.TransactionSummaryType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transaction Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.TransactionResponseTypeImpl#getTransactionSummary <em>Transaction Summary</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionResponseTypeImpl#getInsertResults <em>Insert Results</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionResponseTypeImpl#getUpdateResults <em>Update Results</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionResponseTypeImpl#getReplaceResults <em>Replace Results</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionResponseTypeImpl#getVersion <em>Version</em>}</li>
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
     * The cached value of the '{@link #getInsertResults() <em>Insert Results</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInsertResults()
     * @generated
     * @ordered
     */
    protected ActionResultsType insertResults;

    /**
     * The cached value of the '{@link #getUpdateResults() <em>Update Results</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpdateResults()
     * @generated
     * @ordered
     */
    protected ActionResultsType updateResults;

    /**
     * The cached value of the '{@link #getReplaceResults() <em>Replace Results</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReplaceResults()
     * @generated
     * @ordered
     */
    protected ActionResultsType replaceResults;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = "2.0.0";

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
     * This is true if the Version attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean versionESet;

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
        return Wfs20Package.Literals.TRANSACTION_RESPONSE_TYPE;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY, oldTransactionSummary, newTransactionSummary);
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
                msgs = ((InternalEObject)transactionSummary).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY, null, msgs);
            if (newTransactionSummary != null)
                msgs = ((InternalEObject)newTransactionSummary).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY, null, msgs);
            msgs = basicSetTransactionSummary(newTransactionSummary, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY, newTransactionSummary, newTransactionSummary));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionResultsType getInsertResults() {
        return insertResults;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInsertResults(ActionResultsType newInsertResults, NotificationChain msgs) {
        ActionResultsType oldInsertResults = insertResults;
        insertResults = newInsertResults;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS, oldInsertResults, newInsertResults);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInsertResults(ActionResultsType newInsertResults) {
        if (newInsertResults != insertResults) {
            NotificationChain msgs = null;
            if (insertResults != null)
                msgs = ((InternalEObject)insertResults).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS, null, msgs);
            if (newInsertResults != null)
                msgs = ((InternalEObject)newInsertResults).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS, null, msgs);
            msgs = basicSetInsertResults(newInsertResults, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS, newInsertResults, newInsertResults));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionResultsType getUpdateResults() {
        return updateResults;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUpdateResults(ActionResultsType newUpdateResults, NotificationChain msgs) {
        ActionResultsType oldUpdateResults = updateResults;
        updateResults = newUpdateResults;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS, oldUpdateResults, newUpdateResults);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUpdateResults(ActionResultsType newUpdateResults) {
        if (newUpdateResults != updateResults) {
            NotificationChain msgs = null;
            if (updateResults != null)
                msgs = ((InternalEObject)updateResults).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS, null, msgs);
            if (newUpdateResults != null)
                msgs = ((InternalEObject)newUpdateResults).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS, null, msgs);
            msgs = basicSetUpdateResults(newUpdateResults, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS, newUpdateResults, newUpdateResults));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionResultsType getReplaceResults() {
        return replaceResults;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReplaceResults(ActionResultsType newReplaceResults, NotificationChain msgs) {
        ActionResultsType oldReplaceResults = replaceResults;
        replaceResults = newReplaceResults;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS, oldReplaceResults, newReplaceResults);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReplaceResults(ActionResultsType newReplaceResults) {
        if (newReplaceResults != replaceResults) {
            NotificationChain msgs = null;
            if (replaceResults != null)
                msgs = ((InternalEObject)replaceResults).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS, null, msgs);
            if (newReplaceResults != null)
                msgs = ((InternalEObject)newReplaceResults).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS, null, msgs);
            msgs = basicSetReplaceResults(newReplaceResults, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS, newReplaceResults, newReplaceResults));
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
        boolean oldVersionESet = versionESet;
        versionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__VERSION, oldVersion, version, !oldVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetVersion() {
        String oldVersion = version;
        boolean oldVersionESet = versionESet;
        version = VERSION_EDEFAULT;
        versionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.TRANSACTION_RESPONSE_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetVersion() {
        return versionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                return basicSetTransactionSummary(null, msgs);
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS:
                return basicSetInsertResults(null, msgs);
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS:
                return basicSetUpdateResults(null, msgs);
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS:
                return basicSetReplaceResults(null, msgs);
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
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                return getTransactionSummary();
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS:
                return getInsertResults();
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS:
                return getUpdateResults();
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS:
                return getReplaceResults();
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__VERSION:
                return getVersion();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                setTransactionSummary((TransactionSummaryType)newValue);
                return;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS:
                setInsertResults((ActionResultsType)newValue);
                return;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS:
                setUpdateResults((ActionResultsType)newValue);
                return;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS:
                setReplaceResults((ActionResultsType)newValue);
                return;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__VERSION:
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
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                setTransactionSummary((TransactionSummaryType)null);
                return;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS:
                setInsertResults((ActionResultsType)null);
                return;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS:
                setUpdateResults((ActionResultsType)null);
                return;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS:
                setReplaceResults((ActionResultsType)null);
                return;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__VERSION:
                unsetVersion();
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
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY:
                return transactionSummary != null;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS:
                return insertResults != null;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS:
                return updateResults != null;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS:
                return replaceResults != null;
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE__VERSION:
                return isSetVersion();
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
        if (versionESet) result.append(version); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //TransactionResponseTypeImpl
