/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.gml.CodeListType;

import net.opengis.wcs10.SupportedCRSsType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Supported CR Ss Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.SupportedCRSsTypeImpl#getRequestResponseCRSs <em>Request Response CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.SupportedCRSsTypeImpl#getRequestCRSs <em>Request CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.SupportedCRSsTypeImpl#getResponseCRSs <em>Response CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.SupportedCRSsTypeImpl#getNativeCRSs <em>Native CR Ss</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SupportedCRSsTypeImpl extends EObjectImpl implements SupportedCRSsType {
    /**
	 * The cached value of the '{@link #getRequestResponseCRSs() <em>Request Response CR Ss</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRequestResponseCRSs()
	 * @generated
	 * @ordered
	 */
    protected EList requestResponseCRSs;

    /**
	 * The cached value of the '{@link #getRequestCRSs() <em>Request CR Ss</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRequestCRSs()
	 * @generated
	 * @ordered
	 */
    protected EList requestCRSs;

    /**
	 * The cached value of the '{@link #getResponseCRSs() <em>Response CR Ss</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getResponseCRSs()
	 * @generated
	 * @ordered
	 */
    protected EList responseCRSs;

    /**
	 * The cached value of the '{@link #getNativeCRSs() <em>Native CR Ss</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getNativeCRSs()
	 * @generated
	 * @ordered
	 */
    protected EList nativeCRSs;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected SupportedCRSsTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.SUPPORTED_CR_SS_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getRequestResponseCRSs() {
		if (requestResponseCRSs == null) {
			requestResponseCRSs = new EObjectContainmentEList(CodeListType.class, this, Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS);
		}
		return requestResponseCRSs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getRequestCRSs() {
		if (requestCRSs == null) {
			requestCRSs = new EObjectContainmentEList(CodeListType.class, this, Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS);
		}
		return requestCRSs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getResponseCRSs() {
		if (responseCRSs == null) {
			responseCRSs = new EObjectContainmentEList(CodeListType.class, this, Wcs10Package.SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS);
		}
		return responseCRSs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getNativeCRSs() {
		if (nativeCRSs == null) {
			nativeCRSs = new EObjectContainmentEList(CodeListType.class, this, Wcs10Package.SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS);
		}
		return nativeCRSs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS:
				return ((InternalEList)getRequestResponseCRSs()).basicRemove(otherEnd, msgs);
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS:
				return ((InternalEList)getRequestCRSs()).basicRemove(otherEnd, msgs);
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS:
				return ((InternalEList)getResponseCRSs()).basicRemove(otherEnd, msgs);
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS:
				return ((InternalEList)getNativeCRSs()).basicRemove(otherEnd, msgs);
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
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS:
				return getRequestResponseCRSs();
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS:
				return getRequestCRSs();
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS:
				return getResponseCRSs();
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS:
				return getNativeCRSs();
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
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS:
				getRequestResponseCRSs().clear();
				getRequestResponseCRSs().addAll((Collection)newValue);
				return;
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS:
				getRequestCRSs().clear();
				getRequestCRSs().addAll((Collection)newValue);
				return;
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS:
				getResponseCRSs().clear();
				getResponseCRSs().addAll((Collection)newValue);
				return;
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS:
				getNativeCRSs().clear();
				getNativeCRSs().addAll((Collection)newValue);
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
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS:
				getRequestResponseCRSs().clear();
				return;
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS:
				getRequestCRSs().clear();
				return;
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS:
				getResponseCRSs().clear();
				return;
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS:
				getNativeCRSs().clear();
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
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS:
				return requestResponseCRSs != null && !requestResponseCRSs.isEmpty();
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS:
				return requestCRSs != null && !requestCRSs.isEmpty();
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS:
				return responseCRSs != null && !responseCRSs.isEmpty();
			case Wcs10Package.SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS:
				return nativeCRSs != null && !nativeCRSs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SupportedCRSsTypeImpl
