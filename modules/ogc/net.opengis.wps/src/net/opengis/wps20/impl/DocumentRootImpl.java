/**
 */
package net.opengis.wps20.impl;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.wps20.BoundingBoxDataType;
import net.opengis.wps20.ComplexDataType;
import net.opengis.wps20.ContentsType;
import net.opengis.wps20.DataDescriptionType;
import net.opengis.wps20.DataType;
import net.opengis.wps20.DescribeProcessType;
import net.opengis.wps20.DismissType;
import net.opengis.wps20.DocumentRoot;
import net.opengis.wps20.ExecuteRequestType;
import net.opengis.wps20.FormatType;
import net.opengis.wps20.GenericProcessType;
import net.opengis.wps20.GetCapabilitiesType;
import net.opengis.wps20.GetResultType;
import net.opengis.wps20.GetStatusType;
import net.opengis.wps20.LiteralDataType;
import net.opengis.wps20.LiteralValueType;
import net.opengis.wps20.ProcessDescriptionType;
import net.opengis.wps20.ProcessOfferingType;
import net.opengis.wps20.ProcessOfferingsType;
import net.opengis.wps20.ReferenceType;
import net.opengis.wps20.ResultType;
import net.opengis.wps20.StatusInfoType;
import net.opengis.wps20.SupportedCRSType;
import net.opengis.wps20.WPSCapabilitiesType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

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
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getBoundingBoxData <em>Bounding Box Data</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getDataDescription <em>Data Description</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getComplexData <em>Complex Data</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getData <em>Data</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getDescribeProcess <em>Describe Process</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getDismiss <em>Dismiss</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getExecute <em>Execute</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getExpirationDate <em>Expiration Date</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getGenericProcess <em>Generic Process</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getGetResult <em>Get Result</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getGetStatus <em>Get Status</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getJobID <em>Job ID</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getLiteralData <em>Literal Data</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getLiteralValue <em>Literal Value</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getProcess <em>Process</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getProcessOffering <em>Process Offering</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getProcessOfferings <em>Process Offerings</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getResult <em>Result</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getStatusInfo <em>Status Info</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.DocumentRootImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DocumentRootImpl extends MinimalEObjectImpl.Container implements DocumentRoot {
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
	protected EMap<String, String> xMLNSPrefixMap;

	/**
	 * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> xSISchemaLocation;

	/**
	 * The default value of the '{@link #getExpirationDate() <em>Expiration Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpirationDate()
	 * @generated
	 * @ordered
	 */
	protected static final XMLGregorianCalendar EXPIRATION_DATE_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getJobID() <em>Job ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJobID()
	 * @generated
	 * @ordered
	 */
	protected static final String JOB_ID_EDEFAULT = null;

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
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, Wps20Package.DOCUMENT_ROOT__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, String> getXMLNSPrefixMap() {
		if (xMLNSPrefixMap == null) {
			xMLNSPrefixMap = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Wps20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		}
		return xMLNSPrefixMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, String> getXSISchemaLocation() {
		if (xSISchemaLocation == null) {
			xSISchemaLocation = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Wps20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BoundingBoxDataType getBoundingBoxData() {
		return (BoundingBoxDataType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__BOUNDING_BOX_DATA, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBoundingBoxData(BoundingBoxDataType newBoundingBoxData, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__BOUNDING_BOX_DATA, newBoundingBoxData, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBoundingBoxData(BoundingBoxDataType newBoundingBoxData) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__BOUNDING_BOX_DATA, newBoundingBoxData);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataDescriptionType getDataDescription() {
		return (DataDescriptionType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__DATA_DESCRIPTION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDataDescription(DataDescriptionType newDataDescription, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__DATA_DESCRIPTION, newDataDescription, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WPSCapabilitiesType getCapabilities() {
		return (WPSCapabilitiesType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__CAPABILITIES, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCapabilities(WPSCapabilitiesType newCapabilities, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__CAPABILITIES, newCapabilities, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCapabilities(WPSCapabilitiesType newCapabilities) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__CAPABILITIES, newCapabilities);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexDataType getComplexData() {
		return (ComplexDataType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__COMPLEX_DATA, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComplexData(ComplexDataType newComplexData, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__COMPLEX_DATA, newComplexData, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComplexData(ComplexDataType newComplexData) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__COMPLEX_DATA, newComplexData);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContentsType getContents() {
		return (ContentsType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__CONTENTS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContents(ContentsType newContents, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__CONTENTS, newContents, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContents(ContentsType newContents) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__CONTENTS, newContents);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataType getData() {
		return (DataType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__DATA, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetData(DataType newData, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__DATA, newData, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setData(DataType newData) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__DATA, newData);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DescribeProcessType getDescribeProcess() {
		return (DescribeProcessType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__DESCRIBE_PROCESS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDescribeProcess(DescribeProcessType newDescribeProcess, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__DESCRIBE_PROCESS, newDescribeProcess, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescribeProcess(DescribeProcessType newDescribeProcess) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__DESCRIBE_PROCESS, newDescribeProcess);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DismissType getDismiss() {
		return (DismissType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__DISMISS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDismiss(DismissType newDismiss, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__DISMISS, newDismiss, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDismiss(DismissType newDismiss) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__DISMISS, newDismiss);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExecuteRequestType getExecute() {
		return (ExecuteRequestType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__EXECUTE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExecute(ExecuteRequestType newExecute, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__EXECUTE, newExecute, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecute(ExecuteRequestType newExecute) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__EXECUTE, newExecute);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XMLGregorianCalendar getExpirationDate() {
		return (XMLGregorianCalendar)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__EXPIRATION_DATE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpirationDate(XMLGregorianCalendar newExpirationDate) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__EXPIRATION_DATE, newExpirationDate);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FormatType getFormat() {
		return (FormatType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__FORMAT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFormat(FormatType newFormat, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__FORMAT, newFormat, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFormat(FormatType newFormat) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__FORMAT, newFormat);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenericProcessType getGenericProcess() {
		return (GenericProcessType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__GENERIC_PROCESS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGenericProcess(GenericProcessType newGenericProcess, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__GENERIC_PROCESS, newGenericProcess, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGenericProcess(GenericProcessType newGenericProcess) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__GENERIC_PROCESS, newGenericProcess);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetCapabilitiesType getGetCapabilities() {
		return (GetCapabilitiesType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetResultType getGetResult() {
		return (GetResultType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__GET_RESULT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGetResult(GetResultType newGetResult, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__GET_RESULT, newGetResult, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGetResult(GetResultType newGetResult) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__GET_RESULT, newGetResult);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetStatusType getGetStatus() {
		return (GetStatusType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__GET_STATUS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGetStatus(GetStatusType newGetStatus, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__GET_STATUS, newGetStatus, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGetStatus(GetStatusType newGetStatus) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__GET_STATUS, newGetStatus);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getJobID() {
		return (String)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__JOB_ID, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJobID(String newJobID) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__JOB_ID, newJobID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LiteralDataType getLiteralData() {
		return (LiteralDataType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__LITERAL_DATA, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLiteralData(LiteralDataType newLiteralData, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__LITERAL_DATA, newLiteralData, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLiteralData(LiteralDataType newLiteralData) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__LITERAL_DATA, newLiteralData);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LiteralValueType getLiteralValue() {
		return (LiteralValueType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__LITERAL_VALUE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLiteralValue(LiteralValueType newLiteralValue, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__LITERAL_VALUE, newLiteralValue, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLiteralValue(LiteralValueType newLiteralValue) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__LITERAL_VALUE, newLiteralValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessDescriptionType getProcess() {
		return (ProcessDescriptionType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProcess(ProcessDescriptionType newProcess, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS, newProcess, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProcess(ProcessDescriptionType newProcess) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS, newProcess);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessOfferingType getProcessOffering() {
		return (ProcessOfferingType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERING, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProcessOffering(ProcessOfferingType newProcessOffering, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERING, newProcessOffering, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProcessOffering(ProcessOfferingType newProcessOffering) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERING, newProcessOffering);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessOfferingsType getProcessOfferings() {
		return (ProcessOfferingsType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERINGS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProcessOfferings(ProcessOfferingsType newProcessOfferings, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERINGS, newProcessOfferings, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProcessOfferings(ProcessOfferingsType newProcessOfferings) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__PROCESS_OFFERINGS, newProcessOfferings);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceType getReference() {
		return (ReferenceType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__REFERENCE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetReference(ReferenceType newReference, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__REFERENCE, newReference, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReference(ReferenceType newReference) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__REFERENCE, newReference);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultType getResult() {
		return (ResultType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__RESULT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResult(ResultType newResult, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__RESULT, newResult, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResult(ResultType newResult) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__RESULT, newResult);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusInfoType getStatusInfo() {
		return (StatusInfoType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__STATUS_INFO, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStatusInfo(StatusInfoType newStatusInfo, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__STATUS_INFO, newStatusInfo, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatusInfo(StatusInfoType newStatusInfo) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__STATUS_INFO, newStatusInfo);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SupportedCRSType getSupportedCRS() {
		return (SupportedCRSType)getMixed().get(Wps20Package.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSupportedCRS(SupportedCRSType newSupportedCRS, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wps20Package.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, newSupportedCRS, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSupportedCRS(SupportedCRSType newSupportedCRS) {
		((FeatureMap.Internal)getMixed()).set(Wps20Package.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, newSupportedCRS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wps20Package.DOCUMENT_ROOT__MIXED:
				return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
			case Wps20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
			case Wps20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
			case Wps20Package.DOCUMENT_ROOT__BOUNDING_BOX_DATA:
				return basicSetBoundingBoxData(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__DATA_DESCRIPTION:
				return basicSetDataDescription(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__CAPABILITIES:
				return basicSetCapabilities(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__COMPLEX_DATA:
				return basicSetComplexData(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__CONTENTS:
				return basicSetContents(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__DATA:
				return basicSetData(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
				return basicSetDescribeProcess(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__DISMISS:
				return basicSetDismiss(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__EXECUTE:
				return basicSetExecute(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__FORMAT:
				return basicSetFormat(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__GENERIC_PROCESS:
				return basicSetGenericProcess(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				return basicSetGetCapabilities(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__GET_RESULT:
				return basicSetGetResult(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__GET_STATUS:
				return basicSetGetStatus(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__LITERAL_DATA:
				return basicSetLiteralData(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__LITERAL_VALUE:
				return basicSetLiteralValue(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__PROCESS:
				return basicSetProcess(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERING:
				return basicSetProcessOffering(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
				return basicSetProcessOfferings(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__REFERENCE:
				return basicSetReference(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__RESULT:
				return basicSetResult(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__STATUS_INFO:
				return basicSetStatusInfo(null, msgs);
			case Wps20Package.DOCUMENT_ROOT__SUPPORTED_CRS:
				return basicSetSupportedCRS(null, msgs);
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
			case Wps20Package.DOCUMENT_ROOT__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case Wps20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				if (coreType) return getXMLNSPrefixMap();
				else return getXMLNSPrefixMap().map();
			case Wps20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				if (coreType) return getXSISchemaLocation();
				else return getXSISchemaLocation().map();
			case Wps20Package.DOCUMENT_ROOT__BOUNDING_BOX_DATA:
				return getBoundingBoxData();
			case Wps20Package.DOCUMENT_ROOT__DATA_DESCRIPTION:
				return getDataDescription();
			case Wps20Package.DOCUMENT_ROOT__CAPABILITIES:
				return getCapabilities();
			case Wps20Package.DOCUMENT_ROOT__COMPLEX_DATA:
				return getComplexData();
			case Wps20Package.DOCUMENT_ROOT__CONTENTS:
				return getContents();
			case Wps20Package.DOCUMENT_ROOT__DATA:
				return getData();
			case Wps20Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
				return getDescribeProcess();
			case Wps20Package.DOCUMENT_ROOT__DISMISS:
				return getDismiss();
			case Wps20Package.DOCUMENT_ROOT__EXECUTE:
				return getExecute();
			case Wps20Package.DOCUMENT_ROOT__EXPIRATION_DATE:
				return getExpirationDate();
			case Wps20Package.DOCUMENT_ROOT__FORMAT:
				return getFormat();
			case Wps20Package.DOCUMENT_ROOT__GENERIC_PROCESS:
				return getGenericProcess();
			case Wps20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				return getGetCapabilities();
			case Wps20Package.DOCUMENT_ROOT__GET_RESULT:
				return getGetResult();
			case Wps20Package.DOCUMENT_ROOT__GET_STATUS:
				return getGetStatus();
			case Wps20Package.DOCUMENT_ROOT__JOB_ID:
				return getJobID();
			case Wps20Package.DOCUMENT_ROOT__LITERAL_DATA:
				return getLiteralData();
			case Wps20Package.DOCUMENT_ROOT__LITERAL_VALUE:
				return getLiteralValue();
			case Wps20Package.DOCUMENT_ROOT__PROCESS:
				return getProcess();
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERING:
				return getProcessOffering();
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
				return getProcessOfferings();
			case Wps20Package.DOCUMENT_ROOT__REFERENCE:
				return getReference();
			case Wps20Package.DOCUMENT_ROOT__RESULT:
				return getResult();
			case Wps20Package.DOCUMENT_ROOT__STATUS_INFO:
				return getStatusInfo();
			case Wps20Package.DOCUMENT_ROOT__SUPPORTED_CRS:
				return getSupportedCRS();
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
			case Wps20Package.DOCUMENT_ROOT__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__BOUNDING_BOX_DATA:
				setBoundingBoxData((BoundingBoxDataType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__CAPABILITIES:
				setCapabilities((WPSCapabilitiesType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__COMPLEX_DATA:
				setComplexData((ComplexDataType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__CONTENTS:
				setContents((ContentsType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__DATA:
				setData((DataType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
				setDescribeProcess((DescribeProcessType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__DISMISS:
				setDismiss((DismissType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__EXECUTE:
				setExecute((ExecuteRequestType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__EXPIRATION_DATE:
				setExpirationDate((XMLGregorianCalendar)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__FORMAT:
				setFormat((FormatType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__GENERIC_PROCESS:
				setGenericProcess((GenericProcessType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__GET_RESULT:
				setGetResult((GetResultType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__GET_STATUS:
				setGetStatus((GetStatusType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__JOB_ID:
				setJobID((String)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__LITERAL_DATA:
				setLiteralData((LiteralDataType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__LITERAL_VALUE:
				setLiteralValue((LiteralValueType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__PROCESS:
				setProcess((ProcessDescriptionType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERING:
				setProcessOffering((ProcessOfferingType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
				setProcessOfferings((ProcessOfferingsType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__REFERENCE:
				setReference((ReferenceType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__RESULT:
				setResult((ResultType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__STATUS_INFO:
				setStatusInfo((StatusInfoType)newValue);
				return;
			case Wps20Package.DOCUMENT_ROOT__SUPPORTED_CRS:
				setSupportedCRS((SupportedCRSType)newValue);
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
			case Wps20Package.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case Wps20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case Wps20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case Wps20Package.DOCUMENT_ROOT__BOUNDING_BOX_DATA:
				setBoundingBoxData((BoundingBoxDataType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__CAPABILITIES:
				setCapabilities((WPSCapabilitiesType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__COMPLEX_DATA:
				setComplexData((ComplexDataType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__CONTENTS:
				setContents((ContentsType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__DATA:
				setData((DataType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
				setDescribeProcess((DescribeProcessType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__DISMISS:
				setDismiss((DismissType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__EXECUTE:
				setExecute((ExecuteRequestType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__EXPIRATION_DATE:
				setExpirationDate(EXPIRATION_DATE_EDEFAULT);
				return;
			case Wps20Package.DOCUMENT_ROOT__FORMAT:
				setFormat((FormatType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__GENERIC_PROCESS:
				setGenericProcess((GenericProcessType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__GET_RESULT:
				setGetResult((GetResultType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__GET_STATUS:
				setGetStatus((GetStatusType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__JOB_ID:
				setJobID(JOB_ID_EDEFAULT);
				return;
			case Wps20Package.DOCUMENT_ROOT__LITERAL_DATA:
				setLiteralData((LiteralDataType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__LITERAL_VALUE:
				setLiteralValue((LiteralValueType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__PROCESS:
				setProcess((ProcessDescriptionType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERING:
				setProcessOffering((ProcessOfferingType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
				setProcessOfferings((ProcessOfferingsType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__REFERENCE:
				setReference((ReferenceType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__RESULT:
				setResult((ResultType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__STATUS_INFO:
				setStatusInfo((StatusInfoType)null);
				return;
			case Wps20Package.DOCUMENT_ROOT__SUPPORTED_CRS:
				setSupportedCRS((SupportedCRSType)null);
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
			case Wps20Package.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case Wps20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case Wps20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case Wps20Package.DOCUMENT_ROOT__BOUNDING_BOX_DATA:
				return getBoundingBoxData() != null;
			case Wps20Package.DOCUMENT_ROOT__DATA_DESCRIPTION:
				return getDataDescription() != null;
			case Wps20Package.DOCUMENT_ROOT__CAPABILITIES:
				return getCapabilities() != null;
			case Wps20Package.DOCUMENT_ROOT__COMPLEX_DATA:
				return getComplexData() != null;
			case Wps20Package.DOCUMENT_ROOT__CONTENTS:
				return getContents() != null;
			case Wps20Package.DOCUMENT_ROOT__DATA:
				return getData() != null;
			case Wps20Package.DOCUMENT_ROOT__DESCRIBE_PROCESS:
				return getDescribeProcess() != null;
			case Wps20Package.DOCUMENT_ROOT__DISMISS:
				return getDismiss() != null;
			case Wps20Package.DOCUMENT_ROOT__EXECUTE:
				return getExecute() != null;
			case Wps20Package.DOCUMENT_ROOT__EXPIRATION_DATE:
				return EXPIRATION_DATE_EDEFAULT == null ? getExpirationDate() != null : !EXPIRATION_DATE_EDEFAULT.equals(getExpirationDate());
			case Wps20Package.DOCUMENT_ROOT__FORMAT:
				return getFormat() != null;
			case Wps20Package.DOCUMENT_ROOT__GENERIC_PROCESS:
				return getGenericProcess() != null;
			case Wps20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				return getGetCapabilities() != null;
			case Wps20Package.DOCUMENT_ROOT__GET_RESULT:
				return getGetResult() != null;
			case Wps20Package.DOCUMENT_ROOT__GET_STATUS:
				return getGetStatus() != null;
			case Wps20Package.DOCUMENT_ROOT__JOB_ID:
				return JOB_ID_EDEFAULT == null ? getJobID() != null : !JOB_ID_EDEFAULT.equals(getJobID());
			case Wps20Package.DOCUMENT_ROOT__LITERAL_DATA:
				return getLiteralData() != null;
			case Wps20Package.DOCUMENT_ROOT__LITERAL_VALUE:
				return getLiteralValue() != null;
			case Wps20Package.DOCUMENT_ROOT__PROCESS:
				return getProcess() != null;
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERING:
				return getProcessOffering() != null;
			case Wps20Package.DOCUMENT_ROOT__PROCESS_OFFERINGS:
				return getProcessOfferings() != null;
			case Wps20Package.DOCUMENT_ROOT__REFERENCE:
				return getReference() != null;
			case Wps20Package.DOCUMENT_ROOT__RESULT:
				return getResult() != null;
			case Wps20Package.DOCUMENT_ROOT__STATUS_INFO:
				return getStatusInfo() != null;
			case Wps20Package.DOCUMENT_ROOT__SUPPORTED_CRS:
				return getSupportedCRS() != null;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
