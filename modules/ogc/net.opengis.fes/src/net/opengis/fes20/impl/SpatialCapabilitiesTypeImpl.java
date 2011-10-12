/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.GeometryOperandsType;
import net.opengis.fes20.SpatialCapabilitiesType;
import net.opengis.fes20.SpatialOperatorsType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spatial Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.SpatialCapabilitiesTypeImpl#getGeometryOperands <em>Geometry Operands</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.SpatialCapabilitiesTypeImpl#getSpatialOperators <em>Spatial Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpatialCapabilitiesTypeImpl extends EObjectImpl implements SpatialCapabilitiesType {
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
     * The cached value of the '{@link #getSpatialOperators() <em>Spatial Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatialOperators()
     * @generated
     * @ordered
     */
    protected SpatialOperatorsType spatialOperators;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SpatialCapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.SPATIAL_CAPABILITIES_TYPE;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS, oldGeometryOperands, newGeometryOperands);
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
                msgs = ((InternalEObject)geometryOperands).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS, null, msgs);
            if (newGeometryOperands != null)
                msgs = ((InternalEObject)newGeometryOperands).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS, null, msgs);
            msgs = basicSetGeometryOperands(newGeometryOperands, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS, newGeometryOperands, newGeometryOperands));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SpatialOperatorsType getSpatialOperators() {
        return spatialOperators;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpatialOperators(SpatialOperatorsType newSpatialOperators, NotificationChain msgs) {
        SpatialOperatorsType oldSpatialOperators = spatialOperators;
        spatialOperators = newSpatialOperators;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS, oldSpatialOperators, newSpatialOperators);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSpatialOperators(SpatialOperatorsType newSpatialOperators) {
        if (newSpatialOperators != spatialOperators) {
            NotificationChain msgs = null;
            if (spatialOperators != null)
                msgs = ((InternalEObject)spatialOperators).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS, null, msgs);
            if (newSpatialOperators != null)
                msgs = ((InternalEObject)newSpatialOperators).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS, null, msgs);
            msgs = basicSetSpatialOperators(newSpatialOperators, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS, newSpatialOperators, newSpatialOperators));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS:
                return basicSetGeometryOperands(null, msgs);
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS:
                return basicSetSpatialOperators(null, msgs);
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
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS:
                return getGeometryOperands();
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS:
                return getSpatialOperators();
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
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS:
                setGeometryOperands((GeometryOperandsType)newValue);
                return;
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS:
                setSpatialOperators((SpatialOperatorsType)newValue);
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
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS:
                setGeometryOperands((GeometryOperandsType)null);
                return;
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS:
                setSpatialOperators((SpatialOperatorsType)null);
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
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS:
                return geometryOperands != null;
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS:
                return spatialOperators != null;
        }
        return super.eIsSet(featureID);
    }

} //SpatialCapabilitiesTypeImpl
