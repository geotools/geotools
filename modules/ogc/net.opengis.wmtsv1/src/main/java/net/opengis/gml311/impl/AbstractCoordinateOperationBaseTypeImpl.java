/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractCoordinateOperationBaseType;
import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Coordinate Operation Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractCoordinateOperationBaseTypeImpl#getCoordinateOperationName <em>Coordinate Operation Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractCoordinateOperationBaseTypeImpl extends DefinitionTypeImpl implements AbstractCoordinateOperationBaseType {
    /**
     * The cached value of the '{@link #getCoordinateOperationName() <em>Coordinate Operation Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoordinateOperationName()
     * @generated
     * @ordered
     */
    protected CodeType coordinateOperationName;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractCoordinateOperationBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractCoordinateOperationBaseType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getCoordinateOperationName() {
        return coordinateOperationName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoordinateOperationName(CodeType newCoordinateOperationName, NotificationChain msgs) {
        CodeType oldCoordinateOperationName = coordinateOperationName;
        coordinateOperationName = newCoordinateOperationName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME, oldCoordinateOperationName, newCoordinateOperationName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoordinateOperationName(CodeType newCoordinateOperationName) {
        if (newCoordinateOperationName != coordinateOperationName) {
            NotificationChain msgs = null;
            if (coordinateOperationName != null)
                msgs = ((InternalEObject)coordinateOperationName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME, null, msgs);
            if (newCoordinateOperationName != null)
                msgs = ((InternalEObject)newCoordinateOperationName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME, null, msgs);
            msgs = basicSetCoordinateOperationName(newCoordinateOperationName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME, newCoordinateOperationName, newCoordinateOperationName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME:
                return basicSetCoordinateOperationName(null, msgs);
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
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME:
                return getCoordinateOperationName();
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
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME:
                setCoordinateOperationName((CodeType)newValue);
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
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME:
                setCoordinateOperationName((CodeType)null);
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
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE__COORDINATE_OPERATION_NAME:
                return coordinateOperationName != null;
        }
        return super.eIsSet(featureID);
    }

} //AbstractCoordinateOperationBaseTypeImpl
