/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractReferenceSystemBaseType;
import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Reference System Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractReferenceSystemBaseTypeImpl#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractReferenceSystemBaseTypeImpl extends DefinitionTypeImpl implements AbstractReferenceSystemBaseType {
    /**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected CodeType srsName;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractReferenceSystemBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractReferenceSystemBaseType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getSrsName() {
        return srsName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSrsName(CodeType newSrsName, NotificationChain msgs) {
        CodeType oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME, oldSrsName, newSrsName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsName(CodeType newSrsName) {
        if (newSrsName != srsName) {
            NotificationChain msgs = null;
            if (srsName != null)
                msgs = ((InternalEObject)srsName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME, null, msgs);
            if (newSrsName != null)
                msgs = ((InternalEObject)newSrsName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME, null, msgs);
            msgs = basicSetSrsName(newSrsName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME, newSrsName, newSrsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME:
                return basicSetSrsName(null, msgs);
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
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME:
                return getSrsName();
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
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME:
                setSrsName((CodeType)newValue);
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
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME:
                setSrsName((CodeType)null);
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
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE__SRS_NAME:
                return srsName != null;
        }
        return super.eIsSet(featureID);
    }

} //AbstractReferenceSystemBaseTypeImpl
