/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.ows11.DomainMetadataType;

import net.opengis.wps10.LiteralOutputType;
import net.opengis.wps10.SupportedUOMsType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Literal Output Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.LiteralOutputTypeImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.LiteralOutputTypeImpl#getUOMs <em>UO Ms</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LiteralOutputTypeImpl extends EObjectImpl implements LiteralOutputType {
    /**
     * The cached value of the '{@link #getDataType() <em>Data Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataType()
     * @generated
     * @ordered
     */
    protected DomainMetadataType dataType;

    /**
     * The cached value of the '{@link #getUOMs() <em>UO Ms</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUOMs()
     * @generated
     * @ordered
     */
    protected SupportedUOMsType uOMs;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LiteralOutputTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.LITERAL_OUTPUT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getDataType() {
        return dataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDataType(DomainMetadataType newDataType, NotificationChain msgs) {
        DomainMetadataType oldDataType = dataType;
        dataType = newDataType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE, oldDataType, newDataType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataType(DomainMetadataType newDataType) {
        if (newDataType != dataType) {
            NotificationChain msgs = null;
            if (dataType != null)
                msgs = ((InternalEObject)dataType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE, null, msgs);
            if (newDataType != null)
                msgs = ((InternalEObject)newDataType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE, null, msgs);
            msgs = basicSetDataType(newDataType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE, newDataType, newDataType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SupportedUOMsType getUOMs() {
        return uOMs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUOMs(SupportedUOMsType newUOMs, NotificationChain msgs) {
        SupportedUOMsType oldUOMs = uOMs;
        uOMs = newUOMs;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS, oldUOMs, newUOMs);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUOMs(SupportedUOMsType newUOMs) {
        if (newUOMs != uOMs) {
            NotificationChain msgs = null;
            if (uOMs != null)
                msgs = ((InternalEObject)uOMs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS, null, msgs);
            if (newUOMs != null)
                msgs = ((InternalEObject)newUOMs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS, null, msgs);
            msgs = basicSetUOMs(newUOMs, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS, newUOMs, newUOMs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE:
                return basicSetDataType(null, msgs);
            case Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS:
                return basicSetUOMs(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE:
                return getDataType();
            case Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS:
                return getUOMs();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE:
                setDataType((DomainMetadataType)newValue);
                return;
            case Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS:
                setUOMs((SupportedUOMsType)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE:
                setDataType((DomainMetadataType)null);
                return;
            case Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS:
                setUOMs((SupportedUOMsType)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wps10Package.LITERAL_OUTPUT_TYPE__DATA_TYPE:
                return dataType != null;
            case Wps10Package.LITERAL_OUTPUT_TYPE__UO_MS:
                return uOMs != null;
        }
        return super.eIsSet(featureID);
    }

} //LiteralOutputTypeImpl
