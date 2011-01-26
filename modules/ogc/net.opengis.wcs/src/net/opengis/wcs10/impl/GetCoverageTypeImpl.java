/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.HashMap;
import java.util.Map;
import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.GetCoverageType;
import net.opengis.wcs10.InterpolationMethodType;
import net.opengis.wcs10.OutputType;
import net.opengis.wcs10.RangeSubsetType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getSourceCoverage <em>Source Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getDomainSubset <em>Domain Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getRangeSubset <em>Range Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.GetCoverageTypeImpl#getExtendedProperties <em>Extended Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetCoverageTypeImpl extends EObjectImpl implements GetCoverageType {
    /**
	 * The default value of the '{@link #getSourceCoverage() <em>Source Coverage</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSourceCoverage()
	 * @generated
	 * @ordered
	 */
    protected static final String SOURCE_COVERAGE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSourceCoverage() <em>Source Coverage</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSourceCoverage()
	 * @generated
	 * @ordered
	 */
    protected String sourceCoverage = SOURCE_COVERAGE_EDEFAULT;

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
	 * This is true if the Interpolation Method attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean interpolationMethodESet;

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
	 * The default value of the '{@link #getService() <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getService()
	 * @generated
	 * @ordered
	 */
    protected static final String SERVICE_EDEFAULT = "WCS";

    /**
	 * The cached value of the '{@link #getService() <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getService()
	 * @generated
	 * @ordered
	 */
    protected String service = SERVICE_EDEFAULT;

    /**
	 * This is true if the Service attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean serviceESet;

    /**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected static final String VERSION_EDEFAULT = "1.0.0";

    /**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected String version = VERSION_EDEFAULT;

    /**
	 * This is true if the Version attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean versionESet;

    /**
	 * The default value of the '{@link #getBaseUrl() <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseUrl()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_URL_EDEFAULT = null;

				/**
	 * The cached value of the '{@link #getBaseUrl() <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseUrl()
	 * @generated
	 * @ordered
	 */
	protected String baseUrl = BASE_URL_EDEFAULT;

				/**
	 * The default value of the '{@link #getExtendedProperties() <em>Extended Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtendedProperties()
	 * @generated
	 * @ordered
	 */
	protected static final Map EXTENDED_PROPERTIES_EDEFAULT = null;

				/**
	 * The cached value of the '{@link #getExtendedProperties() <em>Extended Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtendedProperties()
	 * @generated NOT
	 * @ordered
	 */
	protected Map extendedProperties = new HashMap();

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
		return Wcs10Package.Literals.GET_COVERAGE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSourceCoverage() {
		return sourceCoverage;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSourceCoverage(String newSourceCoverage) {
		String oldSourceCoverage = sourceCoverage;
		sourceCoverage = newSourceCoverage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__SOURCE_COVERAGE, oldSourceCoverage, sourceCoverage));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET, oldDomainSubset, newDomainSubset);
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
				msgs = ((InternalEObject)domainSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET, null, msgs);
			if (newDomainSubset != null)
				msgs = ((InternalEObject)newDomainSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET, null, msgs);
			msgs = basicSetDomainSubset(newDomainSubset, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET, newDomainSubset, newDomainSubset));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET, oldRangeSubset, newRangeSubset);
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
				msgs = ((InternalEObject)rangeSubset).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET, null, msgs);
			if (newRangeSubset != null)
				msgs = ((InternalEObject)newRangeSubset).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET, null, msgs);
			msgs = basicSetRangeSubset(newRangeSubset, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET, newRangeSubset, newRangeSubset));
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
		boolean oldInterpolationMethodESet = interpolationMethodESet;
		interpolationMethodESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__INTERPOLATION_METHOD, oldInterpolationMethod, interpolationMethod, !oldInterpolationMethodESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetInterpolationMethod() {
		InterpolationMethodType oldInterpolationMethod = interpolationMethod;
		boolean oldInterpolationMethodESet = interpolationMethodESet;
		interpolationMethod = INTERPOLATION_METHOD_EDEFAULT;
		interpolationMethodESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.GET_COVERAGE_TYPE__INTERPOLATION_METHOD, oldInterpolationMethod, INTERPOLATION_METHOD_EDEFAULT, oldInterpolationMethodESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetInterpolationMethod() {
		return interpolationMethodESet;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__OUTPUT, oldOutput, newOutput);
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
				msgs = ((InternalEObject)output).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.GET_COVERAGE_TYPE__OUTPUT, null, msgs);
			if (newOutput != null)
				msgs = ((InternalEObject)newOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.GET_COVERAGE_TYPE__OUTPUT, null, msgs);
			msgs = basicSetOutput(newOutput, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__OUTPUT, newOutput, newOutput));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getService() {
		return service;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setService(String newService) {
		String oldService = service;
		service = newService;
		boolean oldServiceESet = serviceESet;
		serviceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__SERVICE, oldService, service, !oldServiceESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetService() {
		String oldService = service;
		boolean oldServiceESet = serviceESet;
		service = SERVICE_EDEFAULT;
		serviceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.GET_COVERAGE_TYPE__SERVICE, oldService, SERVICE_EDEFAULT, oldServiceESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetService() {
		return serviceESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getVersion() {
		return version;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		boolean oldVersionESet = versionESet;
		versionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__VERSION, oldVersion, version, !oldVersionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetVersion() {
		String oldVersion = version;
		boolean oldVersionESet = versionESet;
		version = VERSION_EDEFAULT;
		versionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.GET_COVERAGE_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetVersion() {
		return versionESet;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseUrl(String newBaseUrl) {
		String oldBaseUrl = baseUrl;
		baseUrl = newBaseUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__BASE_URL, oldBaseUrl, baseUrl));
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map getExtendedProperties() {
		return extendedProperties;
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtendedProperties(Map newExtendedProperties) {
		Map oldExtendedProperties = extendedProperties;
		extendedProperties = newExtendedProperties;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.GET_COVERAGE_TYPE__EXTENDED_PROPERTIES, oldExtendedProperties, extendedProperties));
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				return basicSetDomainSubset(null, msgs);
			case Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				return basicSetRangeSubset(null, msgs);
			case Wcs10Package.GET_COVERAGE_TYPE__OUTPUT:
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
			case Wcs10Package.GET_COVERAGE_TYPE__SOURCE_COVERAGE:
				return getSourceCoverage();
			case Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				return getDomainSubset();
			case Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				return getRangeSubset();
			case Wcs10Package.GET_COVERAGE_TYPE__INTERPOLATION_METHOD:
				return getInterpolationMethod();
			case Wcs10Package.GET_COVERAGE_TYPE__OUTPUT:
				return getOutput();
			case Wcs10Package.GET_COVERAGE_TYPE__SERVICE:
				return getService();
			case Wcs10Package.GET_COVERAGE_TYPE__VERSION:
				return getVersion();
			case Wcs10Package.GET_COVERAGE_TYPE__BASE_URL:
				return getBaseUrl();
			case Wcs10Package.GET_COVERAGE_TYPE__EXTENDED_PROPERTIES:
				return getExtendedProperties();
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
			case Wcs10Package.GET_COVERAGE_TYPE__SOURCE_COVERAGE:
				setSourceCoverage((String)newValue);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				setDomainSubset((DomainSubsetType)newValue);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				setRangeSubset((RangeSubsetType)newValue);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__INTERPOLATION_METHOD:
				setInterpolationMethod((InterpolationMethodType)newValue);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__OUTPUT:
				setOutput((OutputType)newValue);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__SERVICE:
				setService((String)newValue);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__VERSION:
				setVersion((String)newValue);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__BASE_URL:
				setBaseUrl((String)newValue);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__EXTENDED_PROPERTIES:
				setExtendedProperties((Map)newValue);
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
			case Wcs10Package.GET_COVERAGE_TYPE__SOURCE_COVERAGE:
				setSourceCoverage(SOURCE_COVERAGE_EDEFAULT);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				setDomainSubset((DomainSubsetType)null);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				setRangeSubset((RangeSubsetType)null);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__INTERPOLATION_METHOD:
				unsetInterpolationMethod();
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__OUTPUT:
				setOutput((OutputType)null);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__SERVICE:
				unsetService();
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__VERSION:
				unsetVersion();
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__BASE_URL:
				setBaseUrl(BASE_URL_EDEFAULT);
				return;
			case Wcs10Package.GET_COVERAGE_TYPE__EXTENDED_PROPERTIES:
				setExtendedProperties(EXTENDED_PROPERTIES_EDEFAULT);
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
			case Wcs10Package.GET_COVERAGE_TYPE__SOURCE_COVERAGE:
				return SOURCE_COVERAGE_EDEFAULT == null ? sourceCoverage != null : !SOURCE_COVERAGE_EDEFAULT.equals(sourceCoverage);
			case Wcs10Package.GET_COVERAGE_TYPE__DOMAIN_SUBSET:
				return domainSubset != null;
			case Wcs10Package.GET_COVERAGE_TYPE__RANGE_SUBSET:
				return rangeSubset != null;
			case Wcs10Package.GET_COVERAGE_TYPE__INTERPOLATION_METHOD:
				return isSetInterpolationMethod();
			case Wcs10Package.GET_COVERAGE_TYPE__OUTPUT:
				return output != null;
			case Wcs10Package.GET_COVERAGE_TYPE__SERVICE:
				return isSetService();
			case Wcs10Package.GET_COVERAGE_TYPE__VERSION:
				return isSetVersion();
			case Wcs10Package.GET_COVERAGE_TYPE__BASE_URL:
				return BASE_URL_EDEFAULT == null ? baseUrl != null : !BASE_URL_EDEFAULT.equals(baseUrl);
			case Wcs10Package.GET_COVERAGE_TYPE__EXTENDED_PROPERTIES:
				return EXTENDED_PROPERTIES_EDEFAULT == null ? extendedProperties != null : !EXTENDED_PROPERTIES_EDEFAULT.equals(extendedProperties);
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
		result.append(" (sourceCoverage: ");
		result.append(sourceCoverage);
		result.append(", interpolationMethod: ");
		if (interpolationMethodESet) result.append(interpolationMethod); else result.append("<unset>");
		result.append(", service: ");
		if (serviceESet) result.append(service); else result.append("<unset>");
		result.append(", version: ");
		if (versionESet) result.append(version); else result.append("<unset>");
		result.append(", baseUrl: ");
		result.append(baseUrl);
		result.append(", extendedProperties: ");
		result.append(extendedProperties);
		result.append(')');
		return result.toString();
	}

} //GetCoverageTypeImpl
