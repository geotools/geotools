/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.InterpolationMethodType;
import net.opengis.wcs10.SupportedInterpolationsType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Supported Interpolations Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.SupportedInterpolationsTypeImpl#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.SupportedInterpolationsTypeImpl#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SupportedInterpolationsTypeImpl extends EObjectImpl implements SupportedInterpolationsType {
    /**
	 * The default value of the '{@link #getInterpolationMethod() <em>Interpolation Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterpolationMethod()
	 * @generated
	 * @ordered
	 */
	protected static final InterpolationMethodType INTERPOLATION_METHOD_EDEFAULT = InterpolationMethodType.NEAREST_NEIGHBOR_LITERAL;

				/**
	 * The cached value of the '{@link #getInterpolationMethod() <em>Interpolation Method</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getInterpolationMethod()
	 * @generated
	 * @ordered
	 */
    protected InterpolationMethodType interpolationMethod = INTERPOLATION_METHOD_EDEFAULT;

    /**
	 * The default value of the '{@link #getDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDefault()
	 * @generated
	 * @ordered
	 */
    protected static final InterpolationMethodType DEFAULT_EDEFAULT = InterpolationMethodType.NEAREST_NEIGHBOR_LITERAL;

    /**
	 * The cached value of the '{@link #getDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDefault()
	 * @generated
	 * @ordered
	 */
    protected InterpolationMethodType default_ = DEFAULT_EDEFAULT;

    /**
	 * This is true if the Default attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean defaultESet;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected SupportedInterpolationsTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.SUPPORTED_INTERPOLATIONS_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public InterpolationMethodType getInterpolationMethod() {
		return interpolationMethod;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInterpolationMethod(InterpolationMethodType newInterpolationMethod) {
		InterpolationMethodType oldInterpolationMethod = interpolationMethod;
		interpolationMethod = newInterpolationMethod == null ? INTERPOLATION_METHOD_EDEFAULT : newInterpolationMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__INTERPOLATION_METHOD, oldInterpolationMethod, interpolationMethod));
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public InterpolationMethodType getDefault() {
		return default_;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDefault(InterpolationMethodType newDefault) {
		InterpolationMethodType oldDefault = default_;
		default_ = newDefault == null ? DEFAULT_EDEFAULT : newDefault;
		boolean oldDefaultESet = defaultESet;
		defaultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT, oldDefault, default_, !oldDefaultESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetDefault() {
		InterpolationMethodType oldDefault = default_;
		boolean oldDefaultESet = defaultESet;
		default_ = DEFAULT_EDEFAULT;
		defaultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT, oldDefault, DEFAULT_EDEFAULT, oldDefaultESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetDefault() {
		return defaultESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__INTERPOLATION_METHOD:
				return getInterpolationMethod();
			case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT:
				return getDefault();
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
			case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__INTERPOLATION_METHOD:
				setInterpolationMethod((InterpolationMethodType)newValue);
				return;
			case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT:
				setDefault((InterpolationMethodType)newValue);
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
			case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__INTERPOLATION_METHOD:
				setInterpolationMethod(INTERPOLATION_METHOD_EDEFAULT);
				return;
			case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT:
				unsetDefault();
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
			case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__INTERPOLATION_METHOD:
				return interpolationMethod != INTERPOLATION_METHOD_EDEFAULT;
			case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT:
				return isSetDefault();
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
		result.append(" (interpolationMethod: ");
		result.append(interpolationMethod);
		result.append(", default: ");
		if (defaultESet) result.append(default_); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //SupportedInterpolationsTypeImpl
