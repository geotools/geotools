/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.gml.GmlPackage;

import net.opengis.gml.GridLimitsType;
import net.opengis.gml.GridType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.opengis.coverage.grid.GridEnvelope;
import org.eclipse.emf.ecore.util.EDataTypeEList;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.gml.impl.GridTypeImpl#getLimits <em>Limits</em>}</li>
 *   <li>{@link net.opengis.gml.impl.GridTypeImpl#getAxisName <em>Axis Name</em>}</li>
 *   <li>{@link net.opengis.gml.impl.GridTypeImpl#getDimension <em>Dimension</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GridTypeImpl extends AbstractGeometryTypeImpl implements GridType {
    /**
	 * The default value of the '{@link #getLimits() <em>Limits</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimits()
	 * @generated
	 * @ordered
	 */
	protected static final GridEnvelope LIMITS_EDEFAULT = null;

				/**
	 * The cached value of the '{@link #getLimits() <em>Limits</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getLimits()
	 * @generated
	 * @ordered
	 */
    protected GridEnvelope limits = LIMITS_EDEFAULT;

    /**
	 * The cached value of the '{@link #getAxisName() <em>Axis Name</em>}' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getAxisName()
	 * @generated
	 * @ordered
	 */
    protected EList axisName;

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
    protected EClass eStaticClass() {
		return GmlPackage.Literals.GRID_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public GridEnvelope getLimits() {
		return limits;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLimits(GridEnvelope newLimits) {
		GridEnvelope oldLimits = limits;
		limits = newLimits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.GRID_TYPE__LIMITS, oldLimits, limits));
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getAxisName() {
		if (axisName == null) {
			axisName = new EDataTypeUniqueEList(String.class, this, GmlPackage.GRID_TYPE__AXIS_NAME);
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
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.GRID_TYPE__DIMENSION, oldDimension, dimension));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GmlPackage.GRID_TYPE__LIMITS:
				return getLimits();
			case GmlPackage.GRID_TYPE__AXIS_NAME:
				return getAxisName();
			case GmlPackage.GRID_TYPE__DIMENSION:
				return getDimension();
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
			case GmlPackage.GRID_TYPE__LIMITS:
				setLimits((GridEnvelope)newValue);
				return;
			case GmlPackage.GRID_TYPE__AXIS_NAME:
				getAxisName().clear();
				getAxisName().addAll((Collection)newValue);
				return;
			case GmlPackage.GRID_TYPE__DIMENSION:
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
    public void eUnset(int featureID) {
		switch (featureID) {
			case GmlPackage.GRID_TYPE__LIMITS:
				setLimits(LIMITS_EDEFAULT);
				return;
			case GmlPackage.GRID_TYPE__AXIS_NAME:
				getAxisName().clear();
				return;
			case GmlPackage.GRID_TYPE__DIMENSION:
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
    public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GmlPackage.GRID_TYPE__LIMITS:
				return LIMITS_EDEFAULT == null ? limits != null : !LIMITS_EDEFAULT.equals(limits);
			case GmlPackage.GRID_TYPE__AXIS_NAME:
				return axisName != null && !axisName.isEmpty();
			case GmlPackage.GRID_TYPE__DIMENSION:
				return DIMENSION_EDEFAULT == null ? dimension != null : !DIMENSION_EDEFAULT.equals(dimension);
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
		result.append(" (limits: ");
		result.append(limits);
		result.append(", axisName: ");
		result.append(axisName);
		result.append(", dimension: ");
		result.append(dimension);
		result.append(')');
		return result.toString();
	}

} //GridTypeImpl
