/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.Collection;

import net.opengis.ows11.DomainMetadataType;
import net.opengis.ows11.MetadataType;

import net.opengis.ows11.impl.DescriptionTypeImpl;

import net.opengis.wcs11.AvailableKeysType;
import net.opengis.wcs11.AxisType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Axis Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.AxisTypeImpl#getAvailableKeys <em>Available Keys</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.AxisTypeImpl#getMeaning <em>Meaning</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.AxisTypeImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.AxisTypeImpl#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.AxisTypeImpl#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.AxisTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.AxisTypeImpl#getIdentifier <em>Identifier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AxisTypeImpl extends DescriptionTypeImpl implements AxisType {
    /**
     * The cached value of the '{@link #getAvailableKeys() <em>Available Keys</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAvailableKeys()
     * @generated
     * @ordered
     */
    protected AvailableKeysType availableKeys;

    /**
     * The cached value of the '{@link #getMeaning() <em>Meaning</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMeaning()
     * @generated
     * @ordered
     */
    protected DomainMetadataType meaning;

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
     * The cached value of the '{@link #getUOM() <em>UOM</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUOM()
     * @generated
     * @ordered
     */
    protected DomainMetadataType uOM;

    /**
     * The cached value of the '{@link #getReferenceSystem() <em>Reference System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceSystem()
     * @generated
     * @ordered
     */
    protected DomainMetadataType referenceSystem;

    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected EList metadata;

    /**
     * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected static final String IDENTIFIER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected String identifier = IDENTIFIER_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AxisTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.AXIS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AvailableKeysType getAvailableKeys() {
        return availableKeys;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAvailableKeys(AvailableKeysType newAvailableKeys, NotificationChain msgs) {
        AvailableKeysType oldAvailableKeys = availableKeys;
        availableKeys = newAvailableKeys;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS, oldAvailableKeys, newAvailableKeys);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAvailableKeys(AvailableKeysType newAvailableKeys) {
        if (newAvailableKeys != availableKeys) {
            NotificationChain msgs = null;
            if (availableKeys != null)
                msgs = ((InternalEObject)availableKeys).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS, null, msgs);
            if (newAvailableKeys != null)
                msgs = ((InternalEObject)newAvailableKeys).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS, null, msgs);
            msgs = basicSetAvailableKeys(newAvailableKeys, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS, newAvailableKeys, newAvailableKeys));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getMeaning() {
        return meaning;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMeaning(DomainMetadataType newMeaning, NotificationChain msgs) {
        DomainMetadataType oldMeaning = meaning;
        meaning = newMeaning;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__MEANING, oldMeaning, newMeaning);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMeaning(DomainMetadataType newMeaning) {
        if (newMeaning != meaning) {
            NotificationChain msgs = null;
            if (meaning != null)
                msgs = ((InternalEObject)meaning).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__MEANING, null, msgs);
            if (newMeaning != null)
                msgs = ((InternalEObject)newMeaning).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__MEANING, null, msgs);
            msgs = basicSetMeaning(newMeaning, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__MEANING, newMeaning, newMeaning));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__DATA_TYPE, oldDataType, newDataType);
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
                msgs = ((InternalEObject)dataType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__DATA_TYPE, null, msgs);
            if (newDataType != null)
                msgs = ((InternalEObject)newDataType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__DATA_TYPE, null, msgs);
            msgs = basicSetDataType(newDataType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__DATA_TYPE, newDataType, newDataType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getUOM() {
        return uOM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUOM(DomainMetadataType newUOM, NotificationChain msgs) {
        DomainMetadataType oldUOM = uOM;
        uOM = newUOM;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__UOM, oldUOM, newUOM);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUOM(DomainMetadataType newUOM) {
        if (newUOM != uOM) {
            NotificationChain msgs = null;
            if (uOM != null)
                msgs = ((InternalEObject)uOM).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__UOM, null, msgs);
            if (newUOM != null)
                msgs = ((InternalEObject)newUOM).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__UOM, null, msgs);
            msgs = basicSetUOM(newUOM, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__UOM, newUOM, newUOM));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getReferenceSystem() {
        return referenceSystem;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReferenceSystem(DomainMetadataType newReferenceSystem, NotificationChain msgs) {
        DomainMetadataType oldReferenceSystem = referenceSystem;
        referenceSystem = newReferenceSystem;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM, oldReferenceSystem, newReferenceSystem);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceSystem(DomainMetadataType newReferenceSystem) {
        if (newReferenceSystem != referenceSystem) {
            NotificationChain msgs = null;
            if (referenceSystem != null)
                msgs = ((InternalEObject)referenceSystem).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM, null, msgs);
            if (newReferenceSystem != null)
                msgs = ((InternalEObject)newReferenceSystem).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM, null, msgs);
            msgs = basicSetReferenceSystem(newReferenceSystem, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM, newReferenceSystem, newReferenceSystem));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getMetadata() {
        if (metadata == null) {
            metadata = new EObjectContainmentEList(MetadataType.class, this, Wcs111Package.AXIS_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(String newIdentifier) {
        String oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.AXIS_TYPE__IDENTIFIER, oldIdentifier, identifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS:
                return basicSetAvailableKeys(null, msgs);
            case Wcs111Package.AXIS_TYPE__MEANING:
                return basicSetMeaning(null, msgs);
            case Wcs111Package.AXIS_TYPE__DATA_TYPE:
                return basicSetDataType(null, msgs);
            case Wcs111Package.AXIS_TYPE__UOM:
                return basicSetUOM(null, msgs);
            case Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM:
                return basicSetReferenceSystem(null, msgs);
            case Wcs111Package.AXIS_TYPE__METADATA:
                return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
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
            case Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS:
                return getAvailableKeys();
            case Wcs111Package.AXIS_TYPE__MEANING:
                return getMeaning();
            case Wcs111Package.AXIS_TYPE__DATA_TYPE:
                return getDataType();
            case Wcs111Package.AXIS_TYPE__UOM:
                return getUOM();
            case Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM:
                return getReferenceSystem();
            case Wcs111Package.AXIS_TYPE__METADATA:
                return getMetadata();
            case Wcs111Package.AXIS_TYPE__IDENTIFIER:
                return getIdentifier();
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
            case Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS:
                setAvailableKeys((AvailableKeysType)newValue);
                return;
            case Wcs111Package.AXIS_TYPE__MEANING:
                setMeaning((DomainMetadataType)newValue);
                return;
            case Wcs111Package.AXIS_TYPE__DATA_TYPE:
                setDataType((DomainMetadataType)newValue);
                return;
            case Wcs111Package.AXIS_TYPE__UOM:
                setUOM((DomainMetadataType)newValue);
                return;
            case Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM:
                setReferenceSystem((DomainMetadataType)newValue);
                return;
            case Wcs111Package.AXIS_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection)newValue);
                return;
            case Wcs111Package.AXIS_TYPE__IDENTIFIER:
                setIdentifier((String)newValue);
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
            case Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS:
                setAvailableKeys((AvailableKeysType)null);
                return;
            case Wcs111Package.AXIS_TYPE__MEANING:
                setMeaning((DomainMetadataType)null);
                return;
            case Wcs111Package.AXIS_TYPE__DATA_TYPE:
                setDataType((DomainMetadataType)null);
                return;
            case Wcs111Package.AXIS_TYPE__UOM:
                setUOM((DomainMetadataType)null);
                return;
            case Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM:
                setReferenceSystem((DomainMetadataType)null);
                return;
            case Wcs111Package.AXIS_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Wcs111Package.AXIS_TYPE__IDENTIFIER:
                setIdentifier(IDENTIFIER_EDEFAULT);
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
            case Wcs111Package.AXIS_TYPE__AVAILABLE_KEYS:
                return availableKeys != null;
            case Wcs111Package.AXIS_TYPE__MEANING:
                return meaning != null;
            case Wcs111Package.AXIS_TYPE__DATA_TYPE:
                return dataType != null;
            case Wcs111Package.AXIS_TYPE__UOM:
                return uOM != null;
            case Wcs111Package.AXIS_TYPE__REFERENCE_SYSTEM:
                return referenceSystem != null;
            case Wcs111Package.AXIS_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
            case Wcs111Package.AXIS_TYPE__IDENTIFIER:
                return IDENTIFIER_EDEFAULT == null ? identifier != null : !IDENTIFIER_EDEFAULT.equals(identifier);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (identifier: ");
        result.append(identifier);
        result.append(')');
        return result.toString();
    }

} //AxisTypeImpl
