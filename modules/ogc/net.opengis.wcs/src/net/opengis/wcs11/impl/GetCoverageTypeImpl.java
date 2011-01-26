/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.ows11.CodeType;

import net.opengis.wcs11.DomainSubsetType;
import net.opengis.wcs11.GetCoverageType;
import net.opengis.wcs11.OutputType;
import net.opengis.wcs11.RangeSubsetType;
import net.opengis.wcs11.Wcs11Package;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.GetCoverageTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GetCoverageTypeImpl#getDomainSubset <em>Domain Subset</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GetCoverageTypeImpl#getRangeSubset <em>Range Subset</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GetCoverageTypeImpl#getOutput <em>Output</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetCoverageTypeImpl extends RequestBaseTypeImpl implements GetCoverageType {
    /**
	 * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
    protected CodeType identifier;

    /**
	 * The cached value of the '{@link #getDomainSubset() <em>Domain Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDomainSubset()
	 * @generated
	 * @ordered
	 */
    protected DomainSubsetType domainSubset;

    /**
	 * The cached value of the '{@link #getRangeSubset() <em>Range Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRangeSubset()
	 * @generated
	 * @ordered
	 */
    protected RangeSubsetType rangeSubset;

    /**
	 * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getOutput()
	 * @generated
	 * @ordered
	 */
    protected OutputType output;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected GetCoverageTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs11Package.Literals.GET_COVERAGE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CodeType getIdentifier() {
		return identifier;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
		CodeType oldIdentifier = identifier;
		identifier = newIdentifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIdentifier(CodeType newIdentifier) {
		if (newIdentifier != identifier) {
			NotificationChain msgs = null;
			if (identifier != null)
				msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER, null, msgs);
			if (newIdentifier != null)
				msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER, null, msgs);
			msgs = basicSetIdentifier(newIdentifier, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainSubsetType getDomainSubset() {
		return domainSubset;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDomainSubset(DomainSubsetType newDomainSubset, NotificationChain msgs) {
		DomainSubsetType oldDomainSubset = domainSubset;
		domainSubset = newDomainSubset;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET, oldDomainSubset, newDomainSubset);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDomainSubset(DomainSubsetType newDomainSubset) {
		if (newDomainSubset != domainSubset) {
			NotificationChain msgs = null;
			if (domainSubset != null)
				msgs = ((InternalEObject)domainSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET, null, msgs);
			if (newDomainSubset != null)
				msgs = ((InternalEObject)newDomainSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET, null, msgs);
			msgs = basicSetDomainSubset(newDomainSubset, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET, newDomainSubset, newDomainSubset));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public RangeSubsetType getRangeSubset() {
		return rangeSubset;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRangeSubset(RangeSubsetType newRangeSubset, NotificationChain msgs) {
		RangeSubsetType oldRangeSubset = rangeSubset;
		rangeSubset = newRangeSubset;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET, oldRangeSubset, newRangeSubset);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRangeSubset(RangeSubsetType newRangeSubset) {
		if (newRangeSubset != rangeSubset) {
			NotificationChain msgs = null;
			if (rangeSubset != null)
				msgs = ((InternalEObject)rangeSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET, null, msgs);
			if (newRangeSubset != null)
				msgs = ((InternalEObject)newRangeSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET, null, msgs);
			msgs = basicSetRangeSubset(newRangeSubset, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET, newRangeSubset, newRangeSubset));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public OutputType getOutput() {
		return output;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetOutput(OutputType newOutput, NotificationChain msgs) {
		OutputType oldOutput = output;
		output = newOutput;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs11Package.GET_COVERAGE_TYPE__OUTPUT, oldOutput, newOutput);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOutput(OutputType newOutput) {
		if (newOutput != output) {
			NotificationChain msgs = null;
			if (output != null)
				msgs = ((InternalEObject)output).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs11Package.GET_COVERAGE_TYPE__OUTPUT, null, msgs);
			if (newOutput != null)
				msgs = ((InternalEObject)newOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs11Package.GET_COVERAGE_TYPE__OUTPUT, null, msgs);
			msgs = basicSetOutput(newOutput, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs11Package.GET_COVERAGE_TYPE__OUTPUT, newOutput, newOutput));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER:
				return basicSetIdentifier(null, msgs);
			case Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				return basicSetDomainSubset(null, msgs);
			case Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				return basicSetRangeSubset(null, msgs);
			case Wcs11Package.GET_COVERAGE_TYPE__OUTPUT:
				return basicSetOutput(null, msgs);
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
			case Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER:
				return getIdentifier();
			case Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				return getDomainSubset();
			case Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				return getRangeSubset();
			case Wcs11Package.GET_COVERAGE_TYPE__OUTPUT:
				return getOutput();
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
			case Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER:
				setIdentifier((CodeType)newValue);
				return;
			case Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				setDomainSubset((DomainSubsetType)newValue);
				return;
			case Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				setRangeSubset((RangeSubsetType)newValue);
				return;
			case Wcs11Package.GET_COVERAGE_TYPE__OUTPUT:
				setOutput((OutputType)newValue);
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
			case Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER:
				setIdentifier((CodeType)null);
				return;
			case Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				setDomainSubset((DomainSubsetType)null);
				return;
			case Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				setRangeSubset((RangeSubsetType)null);
				return;
			case Wcs11Package.GET_COVERAGE_TYPE__OUTPUT:
				setOutput((OutputType)null);
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
			case Wcs11Package.GET_COVERAGE_TYPE__IDENTIFIER:
				return identifier != null;
			case Wcs11Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				return domainSubset != null;
			case Wcs11Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				return rangeSubset != null;
			case Wcs11Package.GET_COVERAGE_TYPE__OUTPUT:
				return output != null;
		}
		return super.eIsSet(featureID);
	}

} //GetCoverageTypeImpl
