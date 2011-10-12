/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.GeometryOperandsType;
import net.opengis.fes20.SpatialOperatorType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spatial Operator Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.SpatialOperatorTypeImpl#getGeometryOperands <em>Geometry Operands</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.SpatialOperatorTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpatialOperatorTypeImpl extends EObjectImpl implements SpatialOperatorType {
    /**
     * The cached value of the '{@link #getGeometryOperands() <em>Geometry Operands</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryOperands()
     * @generated
     * @ordered
     */
    protected GeometryOperandsType geometryOperands;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final Object NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected Object name = NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SpatialOperatorTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.SPATIAL_OPERATOR_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometryOperandsType getGeometryOperands() {
        return geometryOperands;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGeometryOperands(GeometryOperandsType newGeometryOperands, NotificationChain msgs) {
        GeometryOperandsType oldGeometryOperands = geometryOperands;
        geometryOperands = newGeometryOperands;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS, oldGeometryOperands, newGeometryOperands);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGeometryOperands(GeometryOperandsType newGeometryOperands) {
        if (newGeometryOperands != geometryOperands) {
            NotificationChain msgs = null;
            if (geometryOperands != null)
                msgs = ((InternalEObject)geometryOperands).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS, null, msgs);
            if (newGeometryOperands != null)
                msgs = ((InternalEObject)newGeometryOperands).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS, null, msgs);
            msgs = basicSetGeometryOperands(newGeometryOperands, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS, newGeometryOperands, newGeometryOperands));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(Object newName) {
        Object oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.SPATIAL_OPERATOR_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS:
                return basicSetGeometryOperands(null, msgs);
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
            case Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS:
                return getGeometryOperands();
            case Fes20Package.SPATIAL_OPERATOR_TYPE__NAME:
                return getName();
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
            case Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS:
                setGeometryOperands((GeometryOperandsType)newValue);
                return;
            case Fes20Package.SPATIAL_OPERATOR_TYPE__NAME:
                setName(newValue);
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
            case Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS:
                setGeometryOperands((GeometryOperandsType)null);
                return;
            case Fes20Package.SPATIAL_OPERATOR_TYPE__NAME:
                setName(NAME_EDEFAULT);
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
            case Fes20Package.SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS:
                return geometryOperands != null;
            case Fes20Package.SPATIAL_OPERATOR_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
        result.append(" (name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }

} //SpatialOperatorTypeImpl
