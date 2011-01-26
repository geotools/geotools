/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import net.opengis.ows11.AbstractReferenceBaseType;
import net.opengis.ows11.AllowedValuesType;
import net.opengis.ows11.AnyValueType;
import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.ContactType;
import net.opengis.ows11.DCPType;
import net.opengis.ows11.DatasetDescriptionSummaryBaseType;
import net.opengis.ows11.DocumentRoot;
import net.opengis.ows11.DomainMetadataType;
import net.opengis.ows11.ExceptionReportType;
import net.opengis.ows11.ExceptionType;
import net.opengis.ows11.GetCapabilitiesType;
import net.opengis.ows11.GetResourceByIdType;
import net.opengis.ows11.HTTPType;
import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.ManifestType;
import net.opengis.ows11.MetadataType;
import net.opengis.ows11.NoValuesType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.Ows11Package;
import net.opengis.ows11.RangeClosureType;
import net.opengis.ows11.RangeType;
import net.opengis.ows11.ReferenceGroupType;
import net.opengis.ows11.ReferenceType;
import net.opengis.ows11.ResponsiblePartyType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.ServiceProviderType;
import net.opengis.ows11.ServiceReferenceType;
import net.opengis.ows11.ValueType;
import net.opengis.ows11.ValuesReferenceType;
import net.opengis.ows11.WGS84BoundingBoxType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
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
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getAbstractMetaData <em>Abstract Meta Data</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getAbstractReferenceBase <em>Abstract Reference Base</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getAccessConstraints <em>Access Constraints</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getAvailableCRS <em>Available CRS</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getContactInfo <em>Contact Info</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getDCP <em>DCP</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getException <em>Exception</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getExceptionReport <em>Exception Report</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getGetResourceByID <em>Get Resource By ID</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getHTTP <em>HTTP</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getIndividualName <em>Individual Name</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getInputData <em>Input Data</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getManifest <em>Manifest</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getMaximumValue <em>Maximum Value</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getMeaning <em>Meaning</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getMinimumValue <em>Minimum Value</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getNoValues <em>No Values</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getOperationResponse <em>Operation Response</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getOperationsMetadata <em>Operations Metadata</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getOrganisationName <em>Organisation Name</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getOtherSource <em>Other Source</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getPointOfContact <em>Point Of Contact</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getPositionName <em>Position Name</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getRange <em>Range</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getReferenceGroup <em>Reference Group</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getResource <em>Resource</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getServiceIdentification <em>Service Identification</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getServiceProvider <em>Service Provider</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getServiceReference <em>Service Reference</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getSpacing <em>Spacing</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getRangeClosure <em>Range Closure</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DocumentRootImpl#getReference1 <em>Reference1</em>}</li>
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
     * The default value of the '{@link #getAccessConstraints() <em>Access Constraints</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAccessConstraints()
     * @generated
     * @ordered
     */
    protected static final String ACCESS_CONSTRAINTS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getAvailableCRS() <em>Available CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAvailableCRS()
     * @generated
     * @ordered
     */
    protected static final String AVAILABLE_CRS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getFees() <em>Fees</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFees()
     * @generated
     * @ordered
     */
    protected static final String FEES_EDEFAULT = null;

    /**
     * The default value of the '{@link #getIndividualName() <em>Individual Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIndividualName()
     * @generated
     * @ordered
     */
    protected static final String INDIVIDUAL_NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected static final String LANGUAGE_EDEFAULT = null;

    /**
     * The default value of the '{@link #getOrganisationName() <em>Organisation Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrganisationName()
     * @generated
     * @ordered
     */
    protected static final String ORGANISATION_NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FORMAT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getPositionName() <em>Position Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPositionName()
     * @generated
     * @ordered
     */
    protected static final String POSITION_NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getSupportedCRS() <em>Supported CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupportedCRS()
     * @generated
     * @ordered
     */
    protected static final String SUPPORTED_CRS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getRangeClosure() <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeClosure()
     * @generated
     * @ordered
     */
    protected static final RangeClosureType RANGE_CLOSURE_EDEFAULT = RangeClosureType.CLOSED_LITERAL;

    /**
     * The cached value of the '{@link #getRangeClosure() <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeClosure()
     * @generated
     * @ordered
     */
    protected RangeClosureType rangeClosure = RANGE_CLOSURE_EDEFAULT;

    /**
     * This is true if the Range Closure attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean rangeClosureESet;

    /**
     * The default value of the '{@link #getReference1() <em>Reference1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReference1()
     * @generated
     * @ordered
     */
    protected static final String REFERENCE1_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReference1() <em>Reference1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReference1()
     * @generated
     * @ordered
     */
    protected String reference1 = REFERENCE1_EDEFAULT;

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
        return Ows11Package.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, Ows11Package.DOCUMENT_ROOT__MIXED);
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
            xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Ows11Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
            xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Ows11Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageStringType getAbstract() {
        return (LanguageStringType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__ABSTRACT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstract(LanguageStringType newAbstract, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__ABSTRACT, newAbstract, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAbstract(LanguageStringType newAbstract) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__ABSTRACT, newAbstract);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getAbstractMetaData() {
        return (EObject)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__ABSTRACT_META_DATA, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractMetaData(EObject newAbstractMetaData, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__ABSTRACT_META_DATA, newAbstractMetaData, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractReferenceBaseType getAbstractReferenceBase() {
        return (AbstractReferenceBaseType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractReferenceBase(AbstractReferenceBaseType newAbstractReferenceBase, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE, newAbstractReferenceBase, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAccessConstraints() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__ACCESS_CONSTRAINTS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAccessConstraints(String newAccessConstraints) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__ACCESS_CONSTRAINTS, newAccessConstraints);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllowedValuesType getAllowedValues() {
        return (AllowedValuesType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__ALLOWED_VALUES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAllowedValues(AllowedValuesType newAllowedValues, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__ALLOWED_VALUES, newAllowedValues, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAllowedValues(AllowedValuesType newAllowedValues) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__ALLOWED_VALUES, newAllowedValues);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnyValueType getAnyValue() {
        return (AnyValueType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__ANY_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnyValue(AnyValueType newAnyValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__ANY_VALUE, newAnyValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnyValue(AnyValueType newAnyValue) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__ANY_VALUE, newAnyValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAvailableCRS() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__AVAILABLE_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAvailableCRS(String newAvailableCRS) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__AVAILABLE_CRS, newAvailableCRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BoundingBoxType getBoundingBox() {
        return (BoundingBoxType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__BOUNDING_BOX, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBoundingBox(BoundingBoxType newBoundingBox, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__BOUNDING_BOX, newBoundingBox, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBoundingBox(BoundingBoxType newBoundingBox) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__BOUNDING_BOX, newBoundingBox);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContactType getContactInfo() {
        return (ContactType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__CONTACT_INFO, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetContactInfo(ContactType newContactInfo, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__CONTACT_INFO, newContactInfo, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setContactInfo(ContactType newContactInfo) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__CONTACT_INFO, newContactInfo);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DatasetDescriptionSummaryBaseType getDatasetDescriptionSummary() {
        return (DatasetDescriptionSummaryBaseType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDatasetDescriptionSummary(DatasetDescriptionSummaryBaseType newDatasetDescriptionSummary, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY, newDatasetDescriptionSummary, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDatasetDescriptionSummary(DatasetDescriptionSummaryBaseType newDatasetDescriptionSummary) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY, newDatasetDescriptionSummary);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getDataType() {
        return (DomainMetadataType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__DATA_TYPE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDataType(DomainMetadataType newDataType, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__DATA_TYPE, newDataType, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataType(DomainMetadataType newDataType) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__DATA_TYPE, newDataType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DCPType getDCP() {
        return (DCPType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__DCP, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDCP(DCPType newDCP, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__DCP, newDCP, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDCP(DCPType newDCP) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__DCP, newDCP);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getDefaultValue() {
        return (ValueType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__DEFAULT_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefaultValue(ValueType newDefaultValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__DEFAULT_VALUE, newDefaultValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultValue(ValueType newDefaultValue) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__DEFAULT_VALUE, newDefaultValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExceptionType getException() {
        return (ExceptionType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__EXCEPTION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetException(ExceptionType newException, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__EXCEPTION, newException, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setException(ExceptionType newException) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__EXCEPTION, newException);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExceptionReportType getExceptionReport() {
        return (ExceptionReportType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__EXCEPTION_REPORT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExceptionReport(ExceptionReportType newExceptionReport, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__EXCEPTION_REPORT, newExceptionReport, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExceptionReport(ExceptionReportType newExceptionReport) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__EXCEPTION_REPORT, newExceptionReport);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getExtendedCapabilities() {
        return (EObject)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExtendedCapabilities(EObject newExtendedCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, newExtendedCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtendedCapabilities(EObject newExtendedCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, newExtendedCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFees() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__FEES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFees(String newFees) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__FEES, newFees);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType getGetCapabilities() {
        return (GetCapabilitiesType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetResourceByIdType getGetResourceByID() {
        return (GetResourceByIdType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__GET_RESOURCE_BY_ID, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetResourceByID(GetResourceByIdType newGetResourceByID, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__GET_RESOURCE_BY_ID, newGetResourceByID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetResourceByID(GetResourceByIdType newGetResourceByID) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__GET_RESOURCE_BY_ID, newGetResourceByID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HTTPType getHTTP() {
        return (HTTPType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__HTTP, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetHTTP(HTTPType newHTTP, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__HTTP, newHTTP, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHTTP(HTTPType newHTTP) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__HTTP, newHTTP);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return (CodeType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__IDENTIFIER, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__IDENTIFIER, newIdentifier, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__IDENTIFIER, newIdentifier);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getIndividualName() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__INDIVIDUAL_NAME, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIndividualName(String newIndividualName) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__INDIVIDUAL_NAME, newIndividualName);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ManifestType getInputData() {
        return (ManifestType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__INPUT_DATA, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInputData(ManifestType newInputData, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__INPUT_DATA, newInputData, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInputData(ManifestType newInputData) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__INPUT_DATA, newInputData);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public KeywordsType getKeywords() {
        return (KeywordsType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__KEYWORDS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetKeywords(KeywordsType newKeywords, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__KEYWORDS, newKeywords, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setKeywords(KeywordsType newKeywords) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__KEYWORDS, newKeywords);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLanguage() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__LANGUAGE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLanguage(String newLanguage) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__LANGUAGE, newLanguage);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ManifestType getManifest() {
        return (ManifestType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__MANIFEST, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetManifest(ManifestType newManifest, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__MANIFEST, newManifest, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setManifest(ManifestType newManifest) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__MANIFEST, newManifest);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getMaximumValue() {
        return (ValueType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__MAXIMUM_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMaximumValue(ValueType newMaximumValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__MAXIMUM_VALUE, newMaximumValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaximumValue(ValueType newMaximumValue) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__MAXIMUM_VALUE, newMaximumValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getMeaning() {
        return (DomainMetadataType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__MEANING, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMeaning(DomainMetadataType newMeaning, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__MEANING, newMeaning, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMeaning(DomainMetadataType newMeaning) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__MEANING, newMeaning);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MetadataType getMetadata() {
        return (MetadataType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__METADATA, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMetadata(MetadataType newMetadata, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__METADATA, newMetadata, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMetadata(MetadataType newMetadata) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__METADATA, newMetadata);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getMinimumValue() {
        return (ValueType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__MINIMUM_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMinimumValue(ValueType newMinimumValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__MINIMUM_VALUE, newMinimumValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinimumValue(ValueType newMinimumValue) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__MINIMUM_VALUE, newMinimumValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoValuesType getNoValues() {
        return (NoValuesType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__NO_VALUES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetNoValues(NoValuesType newNoValues, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__NO_VALUES, newNoValues, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNoValues(NoValuesType newNoValues) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__NO_VALUES, newNoValues);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationType getOperation() {
        return (OperationType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__OPERATION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOperation(OperationType newOperation, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__OPERATION, newOperation, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperation(OperationType newOperation) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__OPERATION, newOperation);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ManifestType getOperationResponse() {
        return (ManifestType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__OPERATION_RESPONSE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOperationResponse(ManifestType newOperationResponse, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__OPERATION_RESPONSE, newOperationResponse, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperationResponse(ManifestType newOperationResponse) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__OPERATION_RESPONSE, newOperationResponse);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationsMetadataType getOperationsMetadata() {
        return (OperationsMetadataType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOperationsMetadata(OperationsMetadataType newOperationsMetadata, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, newOperationsMetadata, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperationsMetadata(OperationsMetadataType newOperationsMetadata) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, newOperationsMetadata);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOrganisationName() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__ORGANISATION_NAME, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOrganisationName(String newOrganisationName) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__ORGANISATION_NAME, newOrganisationName);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MetadataType getOtherSource() {
        return (MetadataType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__OTHER_SOURCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOtherSource(MetadataType newOtherSource, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__OTHER_SOURCE, newOtherSource, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOtherSource(MetadataType newOtherSource) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__OTHER_SOURCE, newOtherSource);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputFormat() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__OUTPUT_FORMAT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFormat(String newOutputFormat) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__OUTPUT_FORMAT, newOutputFormat);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponsiblePartyType getPointOfContact() {
        return (ResponsiblePartyType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__POINT_OF_CONTACT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPointOfContact(ResponsiblePartyType newPointOfContact, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__POINT_OF_CONTACT, newPointOfContact, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPointOfContact(ResponsiblePartyType newPointOfContact) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__POINT_OF_CONTACT, newPointOfContact);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPositionName() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__POSITION_NAME, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPositionName(String newPositionName) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__POSITION_NAME, newPositionName);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeType getRange() {
        return (RangeType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__RANGE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRange(RangeType newRange, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__RANGE, newRange, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRange(RangeType newRange) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__RANGE, newRange);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceType getReference() {
        return (ReferenceType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReference(ReferenceType newReference, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE, newReference, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReference(ReferenceType newReference) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE, newReference);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceGroupType getReferenceGroup() {
        return (ReferenceGroupType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE_GROUP, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReferenceGroup(ReferenceGroupType newReferenceGroup, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE_GROUP, newReferenceGroup, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceGroup(ReferenceGroupType newReferenceGroup) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE_GROUP, newReferenceGroup);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getReferenceSystem() {
        return (DomainMetadataType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE_SYSTEM, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReferenceSystem(DomainMetadataType newReferenceSystem, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE_SYSTEM, newReferenceSystem, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceSystem(DomainMetadataType newReferenceSystem) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__REFERENCE_SYSTEM, newReferenceSystem);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getResource() {
        return (EObject)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__RESOURCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetResource(EObject newResource, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__RESOURCE, newResource, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResource(EObject newResource) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__RESOURCE, newResource);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getRole() {
        return (CodeType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__ROLE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRole(CodeType newRole, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__ROLE, newRole, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRole(CodeType newRole) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__ROLE, newRole);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceIdentificationType getServiceIdentification() {
        return (ServiceIdentificationType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceIdentification(ServiceIdentificationType newServiceIdentification, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, newServiceIdentification, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceIdentification(ServiceIdentificationType newServiceIdentification) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, newServiceIdentification);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceProviderType getServiceProvider() {
        return (ServiceProviderType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_PROVIDER, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceProvider(ServiceProviderType newServiceProvider, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_PROVIDER, newServiceProvider, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceProvider(ServiceProviderType newServiceProvider) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_PROVIDER, newServiceProvider);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceReferenceType getServiceReference() {
        return (ServiceReferenceType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_REFERENCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceReference(ServiceReferenceType newServiceReference, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_REFERENCE, newServiceReference, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceReference(ServiceReferenceType newServiceReference) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__SERVICE_REFERENCE, newServiceReference);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getSpacing() {
        return (ValueType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__SPACING, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpacing(ValueType newSpacing, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__SPACING, newSpacing, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSpacing(ValueType newSpacing) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__SPACING, newSpacing);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSupportedCRS() {
        return (String)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSupportedCRS(String newSupportedCRS) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, newSupportedCRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageStringType getTitle() {
        return (LanguageStringType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__TITLE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTitle(LanguageStringType newTitle, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__TITLE, newTitle, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTitle(LanguageStringType newTitle) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__TITLE, newTitle);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getUOM() {
        return (DomainMetadataType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__UOM, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUOM(DomainMetadataType newUOM, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__UOM, newUOM, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUOM(DomainMetadataType newUOM) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__UOM, newUOM);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getValue() {
        return (ValueType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValue(ValueType newValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__VALUE, newValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(ValueType newValue) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__VALUE, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValuesReferenceType getValuesReference() {
        return (ValuesReferenceType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__VALUES_REFERENCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValuesReference(ValuesReferenceType newValuesReference, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__VALUES_REFERENCE, newValuesReference, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValuesReference(ValuesReferenceType newValuesReference) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__VALUES_REFERENCE, newValuesReference);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WGS84BoundingBoxType getWGS84BoundingBox() {
        return (WGS84BoundingBoxType)getMixed().get(Ows11Package.Literals.DOCUMENT_ROOT__WGS84_BOUNDING_BOX, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWGS84BoundingBox(WGS84BoundingBoxType newWGS84BoundingBox, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(Ows11Package.Literals.DOCUMENT_ROOT__WGS84_BOUNDING_BOX, newWGS84BoundingBox, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWGS84BoundingBox(WGS84BoundingBoxType newWGS84BoundingBox) {
        ((FeatureMap.Internal)getMixed()).set(Ows11Package.Literals.DOCUMENT_ROOT__WGS84_BOUNDING_BOX, newWGS84BoundingBox);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeClosureType getRangeClosure() {
        return rangeClosure;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeClosure(RangeClosureType newRangeClosure) {
        RangeClosureType oldRangeClosure = rangeClosure;
        rangeClosure = newRangeClosure == null ? RANGE_CLOSURE_EDEFAULT : newRangeClosure;
        boolean oldRangeClosureESet = rangeClosureESet;
        rangeClosureESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.DOCUMENT_ROOT__RANGE_CLOSURE, oldRangeClosure, rangeClosure, !oldRangeClosureESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetRangeClosure() {
        RangeClosureType oldRangeClosure = rangeClosure;
        boolean oldRangeClosureESet = rangeClosureESet;
        rangeClosure = RANGE_CLOSURE_EDEFAULT;
        rangeClosureESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Ows11Package.DOCUMENT_ROOT__RANGE_CLOSURE, oldRangeClosure, RANGE_CLOSURE_EDEFAULT, oldRangeClosureESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetRangeClosure() {
        return rangeClosureESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getReference1() {
        return reference1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReference1(String newReference1) {
        String oldReference1 = reference1;
        reference1 = newReference1;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.DOCUMENT_ROOT__REFERENCE1, oldReference1, reference1));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.DOCUMENT_ROOT__MIXED:
                return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
            case Ows11Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case Ows11Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT:
                return basicSetAbstract(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT_META_DATA:
                return basicSetAbstractMetaData(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE:
                return basicSetAbstractReferenceBase(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__ALLOWED_VALUES:
                return basicSetAllowedValues(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__ANY_VALUE:
                return basicSetAnyValue(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__BOUNDING_BOX:
                return basicSetBoundingBox(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__CONTACT_INFO:
                return basicSetContactInfo(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY:
                return basicSetDatasetDescriptionSummary(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__DATA_TYPE:
                return basicSetDataType(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__DCP:
                return basicSetDCP(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__DEFAULT_VALUE:
                return basicSetDefaultValue(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION:
                return basicSetException(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION_REPORT:
                return basicSetExceptionReport(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return basicSetExtendedCapabilities(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return basicSetGetCapabilities(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__GET_RESOURCE_BY_ID:
                return basicSetGetResourceByID(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__HTTP:
                return basicSetHTTP(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__INPUT_DATA:
                return basicSetInputData(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__KEYWORDS:
                return basicSetKeywords(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__MANIFEST:
                return basicSetManifest(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__MAXIMUM_VALUE:
                return basicSetMaximumValue(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__MEANING:
                return basicSetMeaning(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__METADATA:
                return basicSetMetadata(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__MINIMUM_VALUE:
                return basicSetMinimumValue(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__NO_VALUES:
                return basicSetNoValues(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__OPERATION:
                return basicSetOperation(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__OPERATION_RESPONSE:
                return basicSetOperationResponse(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return basicSetOperationsMetadata(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__OTHER_SOURCE:
                return basicSetOtherSource(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__POINT_OF_CONTACT:
                return basicSetPointOfContact(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__RANGE:
                return basicSetRange(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__REFERENCE:
                return basicSetReference(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_GROUP:
                return basicSetReferenceGroup(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                return basicSetReferenceSystem(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__RESOURCE:
                return basicSetResource(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__ROLE:
                return basicSetRole(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return basicSetServiceIdentification(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__SERVICE_PROVIDER:
                return basicSetServiceProvider(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__SERVICE_REFERENCE:
                return basicSetServiceReference(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__SPACING:
                return basicSetSpacing(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__TITLE:
                return basicSetTitle(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__UOM:
                return basicSetUOM(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__VALUE:
                return basicSetValue(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__VALUES_REFERENCE:
                return basicSetValuesReference(null, msgs);
            case Ows11Package.DOCUMENT_ROOT__WGS84_BOUNDING_BOX:
                return basicSetWGS84BoundingBox(null, msgs);
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
            case Ows11Package.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case Ows11Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case Ows11Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT:
                return getAbstract();
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT_META_DATA:
                return getAbstractMetaData();
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE:
                return getAbstractReferenceBase();
            case Ows11Package.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                return getAccessConstraints();
            case Ows11Package.DOCUMENT_ROOT__ALLOWED_VALUES:
                return getAllowedValues();
            case Ows11Package.DOCUMENT_ROOT__ANY_VALUE:
                return getAnyValue();
            case Ows11Package.DOCUMENT_ROOT__AVAILABLE_CRS:
                return getAvailableCRS();
            case Ows11Package.DOCUMENT_ROOT__BOUNDING_BOX:
                return getBoundingBox();
            case Ows11Package.DOCUMENT_ROOT__CONTACT_INFO:
                return getContactInfo();
            case Ows11Package.DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY:
                return getDatasetDescriptionSummary();
            case Ows11Package.DOCUMENT_ROOT__DATA_TYPE:
                return getDataType();
            case Ows11Package.DOCUMENT_ROOT__DCP:
                return getDCP();
            case Ows11Package.DOCUMENT_ROOT__DEFAULT_VALUE:
                return getDefaultValue();
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION:
                return getException();
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION_REPORT:
                return getExceptionReport();
            case Ows11Package.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities();
            case Ows11Package.DOCUMENT_ROOT__FEES:
                return getFees();
            case Ows11Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities();
            case Ows11Package.DOCUMENT_ROOT__GET_RESOURCE_BY_ID:
                return getGetResourceByID();
            case Ows11Package.DOCUMENT_ROOT__HTTP:
                return getHTTP();
            case Ows11Package.DOCUMENT_ROOT__IDENTIFIER:
                return getIdentifier();
            case Ows11Package.DOCUMENT_ROOT__INDIVIDUAL_NAME:
                return getIndividualName();
            case Ows11Package.DOCUMENT_ROOT__INPUT_DATA:
                return getInputData();
            case Ows11Package.DOCUMENT_ROOT__KEYWORDS:
                return getKeywords();
            case Ows11Package.DOCUMENT_ROOT__LANGUAGE:
                return getLanguage();
            case Ows11Package.DOCUMENT_ROOT__MANIFEST:
                return getManifest();
            case Ows11Package.DOCUMENT_ROOT__MAXIMUM_VALUE:
                return getMaximumValue();
            case Ows11Package.DOCUMENT_ROOT__MEANING:
                return getMeaning();
            case Ows11Package.DOCUMENT_ROOT__METADATA:
                return getMetadata();
            case Ows11Package.DOCUMENT_ROOT__MINIMUM_VALUE:
                return getMinimumValue();
            case Ows11Package.DOCUMENT_ROOT__NO_VALUES:
                return getNoValues();
            case Ows11Package.DOCUMENT_ROOT__OPERATION:
                return getOperation();
            case Ows11Package.DOCUMENT_ROOT__OPERATION_RESPONSE:
                return getOperationResponse();
            case Ows11Package.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return getOperationsMetadata();
            case Ows11Package.DOCUMENT_ROOT__ORGANISATION_NAME:
                return getOrganisationName();
            case Ows11Package.DOCUMENT_ROOT__OTHER_SOURCE:
                return getOtherSource();
            case Ows11Package.DOCUMENT_ROOT__OUTPUT_FORMAT:
                return getOutputFormat();
            case Ows11Package.DOCUMENT_ROOT__POINT_OF_CONTACT:
                return getPointOfContact();
            case Ows11Package.DOCUMENT_ROOT__POSITION_NAME:
                return getPositionName();
            case Ows11Package.DOCUMENT_ROOT__RANGE:
                return getRange();
            case Ows11Package.DOCUMENT_ROOT__REFERENCE:
                return getReference();
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_GROUP:
                return getReferenceGroup();
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                return getReferenceSystem();
            case Ows11Package.DOCUMENT_ROOT__RESOURCE:
                return getResource();
            case Ows11Package.DOCUMENT_ROOT__ROLE:
                return getRole();
            case Ows11Package.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return getServiceIdentification();
            case Ows11Package.DOCUMENT_ROOT__SERVICE_PROVIDER:
                return getServiceProvider();
            case Ows11Package.DOCUMENT_ROOT__SERVICE_REFERENCE:
                return getServiceReference();
            case Ows11Package.DOCUMENT_ROOT__SPACING:
                return getSpacing();
            case Ows11Package.DOCUMENT_ROOT__SUPPORTED_CRS:
                return getSupportedCRS();
            case Ows11Package.DOCUMENT_ROOT__TITLE:
                return getTitle();
            case Ows11Package.DOCUMENT_ROOT__UOM:
                return getUOM();
            case Ows11Package.DOCUMENT_ROOT__VALUE:
                return getValue();
            case Ows11Package.DOCUMENT_ROOT__VALUES_REFERENCE:
                return getValuesReference();
            case Ows11Package.DOCUMENT_ROOT__WGS84_BOUNDING_BOX:
                return getWGS84BoundingBox();
            case Ows11Package.DOCUMENT_ROOT__RANGE_CLOSURE:
                return getRangeClosure();
            case Ows11Package.DOCUMENT_ROOT__REFERENCE1:
                return getReference1();
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
            case Ows11Package.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT:
                setAbstract((LanguageStringType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                setAccessConstraints((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__ALLOWED_VALUES:
                setAllowedValues((AllowedValuesType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__ANY_VALUE:
                setAnyValue((AnyValueType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__AVAILABLE_CRS:
                setAvailableCRS((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__BOUNDING_BOX:
                setBoundingBox((BoundingBoxType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__CONTACT_INFO:
                setContactInfo((ContactType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY:
                setDatasetDescriptionSummary((DatasetDescriptionSummaryBaseType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__DATA_TYPE:
                setDataType((DomainMetadataType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__DCP:
                setDCP((DCPType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__DEFAULT_VALUE:
                setDefaultValue((ValueType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION:
                setException((ExceptionType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION_REPORT:
                setExceptionReport((ExceptionReportType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((EObject)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__FEES:
                setFees((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__GET_RESOURCE_BY_ID:
                setGetResourceByID((GetResourceByIdType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__HTTP:
                setHTTP((HTTPType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__INDIVIDUAL_NAME:
                setIndividualName((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__INPUT_DATA:
                setInputData((ManifestType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__KEYWORDS:
                setKeywords((KeywordsType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__LANGUAGE:
                setLanguage((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__MANIFEST:
                setManifest((ManifestType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__MAXIMUM_VALUE:
                setMaximumValue((ValueType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__MEANING:
                setMeaning((DomainMetadataType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__METADATA:
                setMetadata((MetadataType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__MINIMUM_VALUE:
                setMinimumValue((ValueType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__NO_VALUES:
                setNoValues((NoValuesType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__OPERATION:
                setOperation((OperationType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__OPERATION_RESPONSE:
                setOperationResponse((ManifestType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__OPERATIONS_METADATA:
                setOperationsMetadata((OperationsMetadataType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__ORGANISATION_NAME:
                setOrganisationName((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__OTHER_SOURCE:
                setOtherSource((MetadataType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__POINT_OF_CONTACT:
                setPointOfContact((ResponsiblePartyType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__POSITION_NAME:
                setPositionName((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__RANGE:
                setRange((RangeType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE:
                setReference((ReferenceType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_GROUP:
                setReferenceGroup((ReferenceGroupType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                setReferenceSystem((DomainMetadataType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__RESOURCE:
                setResource((EObject)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__ROLE:
                setRole((CodeType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                setServiceIdentification((ServiceIdentificationType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_PROVIDER:
                setServiceProvider((ServiceProviderType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_REFERENCE:
                setServiceReference((ServiceReferenceType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__SPACING:
                setSpacing((ValueType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__SUPPORTED_CRS:
                setSupportedCRS((String)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__TITLE:
                setTitle((LanguageStringType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__UOM:
                setUOM((DomainMetadataType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__VALUE:
                setValue((ValueType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__VALUES_REFERENCE:
                setValuesReference((ValuesReferenceType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__WGS84_BOUNDING_BOX:
                setWGS84BoundingBox((WGS84BoundingBoxType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__RANGE_CLOSURE:
                setRangeClosure((RangeClosureType)newValue);
                return;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE1:
                setReference1((String)newValue);
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
            case Ows11Package.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case Ows11Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case Ows11Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT:
                setAbstract((LanguageStringType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                setAccessConstraints(ACCESS_CONSTRAINTS_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__ALLOWED_VALUES:
                setAllowedValues((AllowedValuesType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__ANY_VALUE:
                setAnyValue((AnyValueType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__AVAILABLE_CRS:
                setAvailableCRS(AVAILABLE_CRS_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__BOUNDING_BOX:
                setBoundingBox((BoundingBoxType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__CONTACT_INFO:
                setContactInfo((ContactType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY:
                setDatasetDescriptionSummary((DatasetDescriptionSummaryBaseType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__DATA_TYPE:
                setDataType((DomainMetadataType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__DCP:
                setDCP((DCPType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__DEFAULT_VALUE:
                setDefaultValue((ValueType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION:
                setException((ExceptionType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION_REPORT:
                setExceptionReport((ExceptionReportType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((EObject)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__FEES:
                setFees(FEES_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__GET_RESOURCE_BY_ID:
                setGetResourceByID((GetResourceByIdType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__HTTP:
                setHTTP((HTTPType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__INDIVIDUAL_NAME:
                setIndividualName(INDIVIDUAL_NAME_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__INPUT_DATA:
                setInputData((ManifestType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__KEYWORDS:
                setKeywords((KeywordsType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__LANGUAGE:
                setLanguage(LANGUAGE_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__MANIFEST:
                setManifest((ManifestType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__MAXIMUM_VALUE:
                setMaximumValue((ValueType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__MEANING:
                setMeaning((DomainMetadataType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__METADATA:
                setMetadata((MetadataType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__MINIMUM_VALUE:
                setMinimumValue((ValueType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__NO_VALUES:
                setNoValues((NoValuesType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__OPERATION:
                setOperation((OperationType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__OPERATION_RESPONSE:
                setOperationResponse((ManifestType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__OPERATIONS_METADATA:
                setOperationsMetadata((OperationsMetadataType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__ORGANISATION_NAME:
                setOrganisationName(ORGANISATION_NAME_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__OTHER_SOURCE:
                setOtherSource((MetadataType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__OUTPUT_FORMAT:
                setOutputFormat(OUTPUT_FORMAT_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__POINT_OF_CONTACT:
                setPointOfContact((ResponsiblePartyType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__POSITION_NAME:
                setPositionName(POSITION_NAME_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__RANGE:
                setRange((RangeType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE:
                setReference((ReferenceType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_GROUP:
                setReferenceGroup((ReferenceGroupType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                setReferenceSystem((DomainMetadataType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__RESOURCE:
                setResource((EObject)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__ROLE:
                setRole((CodeType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                setServiceIdentification((ServiceIdentificationType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_PROVIDER:
                setServiceProvider((ServiceProviderType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_REFERENCE:
                setServiceReference((ServiceReferenceType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__SPACING:
                setSpacing((ValueType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__SUPPORTED_CRS:
                setSupportedCRS(SUPPORTED_CRS_EDEFAULT);
                return;
            case Ows11Package.DOCUMENT_ROOT__TITLE:
                setTitle((LanguageStringType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__UOM:
                setUOM((DomainMetadataType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__VALUE:
                setValue((ValueType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__VALUES_REFERENCE:
                setValuesReference((ValuesReferenceType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__WGS84_BOUNDING_BOX:
                setWGS84BoundingBox((WGS84BoundingBoxType)null);
                return;
            case Ows11Package.DOCUMENT_ROOT__RANGE_CLOSURE:
                unsetRangeClosure();
                return;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE1:
                setReference1(REFERENCE1_EDEFAULT);
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
            case Ows11Package.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case Ows11Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case Ows11Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT:
                return getAbstract() != null;
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT_META_DATA:
                return getAbstractMetaData() != null;
            case Ows11Package.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE:
                return getAbstractReferenceBase() != null;
            case Ows11Package.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                return ACCESS_CONSTRAINTS_EDEFAULT == null ? getAccessConstraints() != null : !ACCESS_CONSTRAINTS_EDEFAULT.equals(getAccessConstraints());
            case Ows11Package.DOCUMENT_ROOT__ALLOWED_VALUES:
                return getAllowedValues() != null;
            case Ows11Package.DOCUMENT_ROOT__ANY_VALUE:
                return getAnyValue() != null;
            case Ows11Package.DOCUMENT_ROOT__AVAILABLE_CRS:
                return AVAILABLE_CRS_EDEFAULT == null ? getAvailableCRS() != null : !AVAILABLE_CRS_EDEFAULT.equals(getAvailableCRS());
            case Ows11Package.DOCUMENT_ROOT__BOUNDING_BOX:
                return getBoundingBox() != null;
            case Ows11Package.DOCUMENT_ROOT__CONTACT_INFO:
                return getContactInfo() != null;
            case Ows11Package.DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY:
                return getDatasetDescriptionSummary() != null;
            case Ows11Package.DOCUMENT_ROOT__DATA_TYPE:
                return getDataType() != null;
            case Ows11Package.DOCUMENT_ROOT__DCP:
                return getDCP() != null;
            case Ows11Package.DOCUMENT_ROOT__DEFAULT_VALUE:
                return getDefaultValue() != null;
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION:
                return getException() != null;
            case Ows11Package.DOCUMENT_ROOT__EXCEPTION_REPORT:
                return getExceptionReport() != null;
            case Ows11Package.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities() != null;
            case Ows11Package.DOCUMENT_ROOT__FEES:
                return FEES_EDEFAULT == null ? getFees() != null : !FEES_EDEFAULT.equals(getFees());
            case Ows11Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities() != null;
            case Ows11Package.DOCUMENT_ROOT__GET_RESOURCE_BY_ID:
                return getGetResourceByID() != null;
            case Ows11Package.DOCUMENT_ROOT__HTTP:
                return getHTTP() != null;
            case Ows11Package.DOCUMENT_ROOT__IDENTIFIER:
                return getIdentifier() != null;
            case Ows11Package.DOCUMENT_ROOT__INDIVIDUAL_NAME:
                return INDIVIDUAL_NAME_EDEFAULT == null ? getIndividualName() != null : !INDIVIDUAL_NAME_EDEFAULT.equals(getIndividualName());
            case Ows11Package.DOCUMENT_ROOT__INPUT_DATA:
                return getInputData() != null;
            case Ows11Package.DOCUMENT_ROOT__KEYWORDS:
                return getKeywords() != null;
            case Ows11Package.DOCUMENT_ROOT__LANGUAGE:
                return LANGUAGE_EDEFAULT == null ? getLanguage() != null : !LANGUAGE_EDEFAULT.equals(getLanguage());
            case Ows11Package.DOCUMENT_ROOT__MANIFEST:
                return getManifest() != null;
            case Ows11Package.DOCUMENT_ROOT__MAXIMUM_VALUE:
                return getMaximumValue() != null;
            case Ows11Package.DOCUMENT_ROOT__MEANING:
                return getMeaning() != null;
            case Ows11Package.DOCUMENT_ROOT__METADATA:
                return getMetadata() != null;
            case Ows11Package.DOCUMENT_ROOT__MINIMUM_VALUE:
                return getMinimumValue() != null;
            case Ows11Package.DOCUMENT_ROOT__NO_VALUES:
                return getNoValues() != null;
            case Ows11Package.DOCUMENT_ROOT__OPERATION:
                return getOperation() != null;
            case Ows11Package.DOCUMENT_ROOT__OPERATION_RESPONSE:
                return getOperationResponse() != null;
            case Ows11Package.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return getOperationsMetadata() != null;
            case Ows11Package.DOCUMENT_ROOT__ORGANISATION_NAME:
                return ORGANISATION_NAME_EDEFAULT == null ? getOrganisationName() != null : !ORGANISATION_NAME_EDEFAULT.equals(getOrganisationName());
            case Ows11Package.DOCUMENT_ROOT__OTHER_SOURCE:
                return getOtherSource() != null;
            case Ows11Package.DOCUMENT_ROOT__OUTPUT_FORMAT:
                return OUTPUT_FORMAT_EDEFAULT == null ? getOutputFormat() != null : !OUTPUT_FORMAT_EDEFAULT.equals(getOutputFormat());
            case Ows11Package.DOCUMENT_ROOT__POINT_OF_CONTACT:
                return getPointOfContact() != null;
            case Ows11Package.DOCUMENT_ROOT__POSITION_NAME:
                return POSITION_NAME_EDEFAULT == null ? getPositionName() != null : !POSITION_NAME_EDEFAULT.equals(getPositionName());
            case Ows11Package.DOCUMENT_ROOT__RANGE:
                return getRange() != null;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE:
                return getReference() != null;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_GROUP:
                return getReferenceGroup() != null;
            case Ows11Package.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                return getReferenceSystem() != null;
            case Ows11Package.DOCUMENT_ROOT__RESOURCE:
                return getResource() != null;
            case Ows11Package.DOCUMENT_ROOT__ROLE:
                return getRole() != null;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return getServiceIdentification() != null;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_PROVIDER:
                return getServiceProvider() != null;
            case Ows11Package.DOCUMENT_ROOT__SERVICE_REFERENCE:
                return getServiceReference() != null;
            case Ows11Package.DOCUMENT_ROOT__SPACING:
                return getSpacing() != null;
            case Ows11Package.DOCUMENT_ROOT__SUPPORTED_CRS:
                return SUPPORTED_CRS_EDEFAULT == null ? getSupportedCRS() != null : !SUPPORTED_CRS_EDEFAULT.equals(getSupportedCRS());
            case Ows11Package.DOCUMENT_ROOT__TITLE:
                return getTitle() != null;
            case Ows11Package.DOCUMENT_ROOT__UOM:
                return getUOM() != null;
            case Ows11Package.DOCUMENT_ROOT__VALUE:
                return getValue() != null;
            case Ows11Package.DOCUMENT_ROOT__VALUES_REFERENCE:
                return getValuesReference() != null;
            case Ows11Package.DOCUMENT_ROOT__WGS84_BOUNDING_BOX:
                return getWGS84BoundingBox() != null;
            case Ows11Package.DOCUMENT_ROOT__RANGE_CLOSURE:
                return isSetRangeClosure();
            case Ows11Package.DOCUMENT_ROOT__REFERENCE1:
                return REFERENCE1_EDEFAULT == null ? reference1 != null : !REFERENCE1_EDEFAULT.equals(reference1);
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
        result.append(", rangeClosure: ");
        if (rangeClosureESet) result.append(rangeClosure); else result.append("<unset>");
        result.append(", reference1: ");
        result.append(reference1);
        result.append(')');
        return result.toString();
    }

} //DocumentRootImpl
