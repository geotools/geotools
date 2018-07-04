/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractCoordinateSystemBaseType;
import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Coordinate System Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateSystemBaseTypeImpl#getCsName <em>Cs Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractCoordinateSystemBaseTypeImpl extends DefinitionTypeImpl implements AbstractCoordinateSystemBaseType {
    /**
     * The cached value of the '{@link #getCsName() <em>Cs Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCsName()
     * @generated
     * @ordered
     */
    protected CodeType csName;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractCoordinateSystemBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractCoordinateSystemBaseType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getCsName() {
        return csName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCsName(CodeType newCsName, NotificationChain msgs) {
        CodeType oldCsName = csName;
        csName = newCsName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME, oldCsName, newCsName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCsName(CodeType newCsName) {
        if (newCsName != csName) {
            NotificationChain msgs = null;
            if (csName != null)
                msgs = ((InternalEObject)csName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME, null, msgs);
            if (newCsName != null)
                msgs = ((InternalEObject)newCsName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME, null, msgs);
            msgs = basicSetCsName(newCsName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME, newCsName, newCsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME:
                return basicSetCsName(null, msgs);
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
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME:
                return getCsName();
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
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME:
                setCsName((CodeType)newValue);
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
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME:
                setCsName((CodeType)null);
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
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE__CS_NAME:
                return csName != null;
        }
        return super.eIsSet(featureID);
    }

} //AbstractCoordinateSystemBaseTypeImpl
