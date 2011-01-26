/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.DescribeProcessType;
import net.opengis.wps10.DocumentRoot;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.GetCapabilitiesType;
import net.opengis.wps10.LanguagesType1;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.WSDLType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getDescribeProcess <em>Describe Process</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getExecute <em>Execute</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getExecuteResponse <em>Execute Response</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getLanguages <em>Languages</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getProcessDescriptions <em>Process Descriptions</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getProcessOfferings <em>Process Offerings</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.DocumentRootImpl#getProcessVersion <em>Process Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
    protected EMap xMLNSPrefixMap;

    /**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
    protected EMap xSISchemaLocation;

    /**
     * The default value of the '{@link #getProcessVersion() <em>Process Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessVersion()
     * @generated
     * @ordered
     */
    protected static final String PROCESS_VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProcessVersion() <em>Process Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessVersion()
     * @generated
     * @ordered
     */
    protected String processVersion = PROCESS_VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DocumentRootImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, Wps10Package.DOCUMENT_ROOT__MIXED);
        }
        return mixed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap getXMLNSPrefixMap() {
        if (xMLNSPrefixMap == null) {
            xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Wps10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        }
        return xMLNSPrefixMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap getXSISchemaLocation() {
        if (xSISchemaLocation == null) {
            xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Wps10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WPSCapabilitiesType getCapabilities() {
        return (WPSCapabilitiesType)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCapabilities(WPSCapabilitiesType newCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__CAPABILITIES, newCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCapabilities(WPSCapabilitiesType newCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__CAPABILITIES, newCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeProcessType getDescribeProcess() {
        return (DescribeProcessType)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__DESCRIBE_PROCESS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDescribeProcess(DescribeProcessType newDescribeProcess, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__DESCRIBE_PROCESS, newDescribeProcess, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDescribeProcess(DescribeProcessType newDescribeProcess) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__DESCRIBE_PROCESS, newDescribeProcess);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecuteType getExecute() {
        return (ExecuteType)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__EXECUTE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExecute(ExecuteType newExecute, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__EXECUTE, newExecute, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExecute(ExecuteType newExecute) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__EXECUTE, newExecute);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecuteResponseType getExecuteResponse() {
        return (ExecuteResponseType)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__EXECUTE_RESPONSE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExecuteResponse(ExecuteResponseType newExecuteResponse, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__EXECUTE_RESPONSE, newExecuteResponse, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExecuteResponse(ExecuteResponseType newExecuteResponse) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__EXECUTE_RESPONSE, newExecuteResponse);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType getGetCapabilities() {
        return (GetCapabilitiesType)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguagesType1 getLanguages() {
        return (LanguagesType1)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__LANGUAGES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLanguages(LanguagesType1 newLanguages, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__LANGUAGES, newLanguages, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLanguages(LanguagesType1 newLanguages) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__LANGUAGES, newLanguages);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessDescriptionsType getProcessDescriptions() {
        return (ProcessDescriptionsType)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__PROCESS_DESCRIPTIONS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetProcessDescriptions(ProcessDescriptionsType newProcessDescriptions, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__PROCESS_DESCRIPTIONS, newProcessDescriptions, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessDescriptions(ProcessDescriptionsType newProcessDescriptions) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__PROCESS_DESCRIPTIONS, newProcessDescriptions);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessOfferingsType getProcessOfferings() {
        return (ProcessOfferingsType)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERINGS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetProcessOfferings(ProcessOfferingsType newProcessOfferings, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERINGS, newProcessOfferings, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessOfferings(ProcessOfferingsType newProcessOfferings) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERINGS, newProcessOfferings);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WSDLType getWSDL() {
        return (WSDLType)getMixed().get(Wps10Package.Literals.DOCUMENT_ROOT__WSDL, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWSDL(WSDLType newWSDL, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Wps10Package.Literals.DOCUMENT_ROOT__WSDL, newWSDL, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWSDL(WSDLType newWSDL) {
        ((FeatureMap.Internal)getMixed()).set(Wps10Package.Literals.DOCUMENT_ROOT__WSDL, newWSDL);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProcessVersion() {
        return processVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessVersion(String newProcessVersion) {
        String oldProcessVersion = processVersion;
        processVersion = newProcessVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DOCUMENT_ROOT__PROCESS_VERSION, oldProcessVersion, processVersion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.DOCUMENT_ROOT__MIXED:
                return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
            case Wps10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case Wps10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case Wps10Package.DOCUMENT_ROOT__CAPABILITIES:
                return basicSetCapabilities(null, msgs);
            case Wps10Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
                return basicSetDescribeProcess(null, msgs);
            case Wps10Package.DOCUMENT_ROOT__EXECUTE:
                return basicSetExecute(null, msgs);
            case Wps10Package.DOCUMENT_ROOT__EXECUTE_RESPONSE:
                return basicSetExecuteResponse(null, msgs);
            case Wps10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return basicSetGetCapabilities(null, msgs);
            case Wps10Package.DOCUMENT_ROOT__LANGUAGES:
                return basicSetLanguages(null, msgs);
            case Wps10Package.DOCUMENT_ROOT__PROCESS_DESCRIPTIONS:
                return basicSetProcessDescriptions(null, msgs);
            case Wps10Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
                return basicSetProcessOfferings(null, msgs);
            case Wps10Package.DOCUMENT_ROOT__WSDL:
                return basicSetWSDL(null, msgs);
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
            case Wps10Package.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case Wps10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case Wps10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case Wps10Package.DOCUMENT_ROOT__CAPABILITIES:
                return getCapabilities();
            case Wps10Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
                return getDescribeProcess();
            case Wps10Package.DOCUMENT_ROOT__EXECUTE:
                return getExecute();
            case Wps10Package.DOCUMENT_ROOT__EXECUTE_RESPONSE:
                return getExecuteResponse();
            case Wps10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities();
            case Wps10Package.DOCUMENT_ROOT__LANGUAGES:
                return getLanguages();
            case Wps10Package.DOCUMENT_ROOT__PROCESS_DESCRIPTIONS:
                return getProcessDescriptions();
            case Wps10Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
                return getProcessOfferings();
            case Wps10Package.DOCUMENT_ROOT__WSDL:
                return getWSDL();
            case Wps10Package.DOCUMENT_ROOT__PROCESS_VERSION:
                return getProcessVersion();
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
            case Wps10Package.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__CAPABILITIES:
                setCapabilities((WPSCapabilitiesType)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
                setDescribeProcess((DescribeProcessType)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__EXECUTE:
                setExecute((ExecuteType)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__EXECUTE_RESPONSE:
                setExecuteResponse((ExecuteResponseType)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__LANGUAGES:
                setLanguages((LanguagesType1)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_DESCRIPTIONS:
                setProcessDescriptions((ProcessDescriptionsType)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
                setProcessOfferings((ProcessOfferingsType)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__WSDL:
                setWSDL((WSDLType)newValue);
                return;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_VERSION:
                setProcessVersion((String)newValue);
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
            case Wps10Package.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case Wps10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case Wps10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case Wps10Package.DOCUMENT_ROOT__CAPABILITIES:
                setCapabilities((WPSCapabilitiesType)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
                setDescribeProcess((DescribeProcessType)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__EXECUTE:
                setExecute((ExecuteType)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__EXECUTE_RESPONSE:
                setExecuteResponse((ExecuteResponseType)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__LANGUAGES:
                setLanguages((LanguagesType1)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_DESCRIPTIONS:
                setProcessDescriptions((ProcessDescriptionsType)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
                setProcessOfferings((ProcessOfferingsType)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__WSDL:
                setWSDL((WSDLType)null);
                return;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_VERSION:
                setProcessVersion(PROCESS_VERSION_EDEFAULT);
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
            case Wps10Package.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case Wps10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case Wps10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case Wps10Package.DOCUMENT_ROOT__CAPABILITIES:
                return getCapabilities() != null;
            case Wps10Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
                return getDescribeProcess() != null;
            case Wps10Package.DOCUMENT_ROOT__EXECUTE:
                return getExecute() != null;
            case Wps10Package.DOCUMENT_ROOT__EXECUTE_RESPONSE:
                return getExecuteResponse() != null;
            case Wps10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities() != null;
            case Wps10Package.DOCUMENT_ROOT__LANGUAGES:
                return getLanguages() != null;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_DESCRIPTIONS:
                return getProcessDescriptions() != null;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
                return getProcessOfferings() != null;
            case Wps10Package.DOCUMENT_ROOT__WSDL:
                return getWSDL() != null;
            case Wps10Package.DOCUMENT_ROOT__PROCESS_VERSION:
                return PROCESS_VERSION_EDEFAULT == null ? processVersion != null : !PROCESS_VERSION_EDEFAULT.equals(processVersion);
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
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(", processVersion: ");
        result.append(processVersion);
        result.append(')');
        return result.toString();
    }

} //DocumentRootImpl
