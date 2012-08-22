/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import net.opengis.cat.csw20.AcknowledgementType;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.HarvestResponseType;
import net.opengis.cat.csw20.TransactionResponseType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Harvest Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.HarvestResponseTypeImpl#getAcknowledgement <em>Acknowledgement</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.HarvestResponseTypeImpl#getTransactionResponse <em>Transaction Response</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HarvestResponseTypeImpl extends EObjectImpl implements HarvestResponseType {
    /**
     * The cached value of the '{@link #getAcknowledgement() <em>Acknowledgement</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAcknowledgement()
     * @generated
     * @ordered
     */
    protected AcknowledgementType acknowledgement;

    /**
     * The cached value of the '{@link #getTransactionResponse() <em>Transaction Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTransactionResponse()
     * @generated
     * @ordered
     */
    protected TransactionResponseType transactionResponse;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected HarvestResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.HARVEST_RESPONSE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcknowledgementType getAcknowledgement() {
        return acknowledgement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAcknowledgement(AcknowledgementType newAcknowledgement, NotificationChain msgs) {
        AcknowledgementType oldAcknowledgement = acknowledgement;
        acknowledgement = newAcknowledgement;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT, oldAcknowledgement, newAcknowledgement);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAcknowledgement(AcknowledgementType newAcknowledgement) {
        if (newAcknowledgement != acknowledgement) {
            NotificationChain msgs = null;
            if (acknowledgement != null)
                msgs = ((InternalEObject)acknowledgement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT, null, msgs);
            if (newAcknowledgement != null)
                msgs = ((InternalEObject)newAcknowledgement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT, null, msgs);
            msgs = basicSetAcknowledgement(newAcknowledgement, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT, newAcknowledgement, newAcknowledgement));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TransactionResponseType getTransactionResponse() {
        return transactionResponse;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTransactionResponse(TransactionResponseType newTransactionResponse, NotificationChain msgs) {
        TransactionResponseType oldTransactionResponse = transactionResponse;
        transactionResponse = newTransactionResponse;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE, oldTransactionResponse, newTransactionResponse);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTransactionResponse(TransactionResponseType newTransactionResponse) {
        if (newTransactionResponse != transactionResponse) {
            NotificationChain msgs = null;
            if (transactionResponse != null)
                msgs = ((InternalEObject)transactionResponse).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE, null, msgs);
            if (newTransactionResponse != null)
                msgs = ((InternalEObject)newTransactionResponse).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE, null, msgs);
            msgs = basicSetTransactionResponse(newTransactionResponse, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE, newTransactionResponse, newTransactionResponse));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT:
                return basicSetAcknowledgement(null, msgs);
            case Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE:
                return basicSetTransactionResponse(null, msgs);
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
            case Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT:
                return getAcknowledgement();
            case Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE:
                return getTransactionResponse();
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
            case Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT:
                setAcknowledgement((AcknowledgementType)newValue);
                return;
            case Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE:
                setTransactionResponse((TransactionResponseType)newValue);
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
            case Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT:
                setAcknowledgement((AcknowledgementType)null);
                return;
            case Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE:
                setTransactionResponse((TransactionResponseType)null);
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
            case Csw20Package.HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT:
                return acknowledgement != null;
            case Csw20Package.HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE:
                return transactionResponse != null;
        }
        return super.eIsSet(featureID);
    }

} //HarvestResponseTypeImpl
