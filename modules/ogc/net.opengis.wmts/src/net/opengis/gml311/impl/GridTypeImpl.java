/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.GridLimitsType;
import net.opengis.gml311.GridType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GridTypeImpl#getLimits <em>Limits</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GridTypeImpl#getAxisName <em>Axis Name</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GridTypeImpl#getDimension <em>Dimension</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GridTypeImpl extends AbstractGeometryTypeImpl implements GridType {
    /**
     * The cached value of the '{@link #getLimits() <em>Limits</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLimits()
     * @generated
     * @ordered
     */
    protected GridLimitsType limits;

    /**
     * The cached value of the '{@link #getAxisName() <em>Axis Name</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxisName()
     * @generated
     * @ordered
     */
    protected EList<String> axisName;

    /**
     * The default value of the '{@link #getDimension() <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimension()
     * @generated
     * @ordered
     */
    protected static final BigInteger DIMENSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDimension() <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimension()
     * @generated
     * @ordered
     */
    protected BigInteger dimension = DIMENSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GridTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGridType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridLimitsType getLimits() {
        return limits;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLimits(GridLimitsType newLimits, NotificationChain msgs) {
        GridLimitsType oldLimits = limits;
        limits = newLimits;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_TYPE__LIMITS, oldLimits, newLimits);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLimits(GridLimitsType newLimits) {
        if (newLimits != limits) {
            NotificationChain msgs = null;
            if (limits != null)
                msgs = ((InternalEObject)limits).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GRID_TYPE__LIMITS, null, msgs);
            if (newLimits != null)
                msgs = ((InternalEObject)newLimits).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GRID_TYPE__LIMITS, null, msgs);
            msgs = basicSetLimits(newLimits, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_TYPE__LIMITS, newLimits, newLimits));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getAxisName() {
        if (axisName == null) {
            axisName = new EDataTypeEList<String>(String.class, this, Gml311Package.GRID_TYPE__AXIS_NAME);
        }
        return axisName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getDimension() {
        return dimension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDimension(BigInteger newDimension) {
        BigInteger oldDimension = dimension;
        dimension = newDimension;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_TYPE__DIMENSION, oldDimension, dimension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GRID_TYPE__LIMITS:
                return basicSetLimits(null, msgs);
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
            case Gml311Package.GRID_TYPE__LIMITS:
                return getLimits();
            case Gml311Package.GRID_TYPE__AXIS_NAME:
                return getAxisName();
            case Gml311Package.GRID_TYPE__DIMENSION:
                return getDimension();
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
            case Gml311Package.GRID_TYPE__LIMITS:
                setLimits((GridLimitsType)newValue);
                return;
            case Gml311Package.GRID_TYPE__AXIS_NAME:
                getAxisName().clear();
                getAxisName().addAll((Collection<? extends String>)newValue);
                return;
            case Gml311Package.GRID_TYPE__DIMENSION:
                setDimension((BigInteger)newValue);
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
            case Gml311Package.GRID_TYPE__LIMITS:
                setLimits((GridLimitsType)null);
                return;
            case Gml311Package.GRID_TYPE__AXIS_NAME:
                getAxisName().clear();
                return;
            case Gml311Package.GRID_TYPE__DIMENSION:
                setDimension(DIMENSION_EDEFAULT);
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
            case Gml311Package.GRID_TYPE__LIMITS:
                return limits != null;
            case Gml311Package.GRID_TYPE__AXIS_NAME:
                return axisName != null && !axisName.isEmpty();
            case Gml311Package.GRID_TYPE__DIMENSION:
                return DIMENSION_EDEFAULT == null ? dimension != null : !DIMENSION_EDEFAULT.equals(dimension);
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
        result.append(" (axisName: ");
        result.append(axisName);
        result.append(", dimension: ");
        result.append(dimension);
        result.append(')');
        return result.toString();
    }

} //GridTypeImpl
