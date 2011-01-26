/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.gml.CodeListType;

import net.opengis.wcs10.AxisDescriptionType;
import net.opengis.wcs10.AxisDescriptionType1;
import net.opengis.wcs10.ClosureType;
import net.opengis.wcs10.ContentMetadataType;
import net.opengis.wcs10.CoverageDescriptionType;
import net.opengis.wcs10.CoverageOfferingBriefType;
import net.opengis.wcs10.CoverageOfferingType;
import net.opengis.wcs10.DescribeCoverageType;
import net.opengis.wcs10.DocumentRoot;
import net.opengis.wcs10.DomainSetType;
import net.opengis.wcs10.GetCapabilitiesType;
import net.opengis.wcs10.GetCoverageType;
import net.opengis.wcs10.InterpolationMethodType;
import net.opengis.wcs10.IntervalType;
import net.opengis.wcs10.KeywordsType;
import net.opengis.wcs10.LonLatEnvelopeType;
import net.opengis.wcs10.MetadataLinkType;
import net.opengis.wcs10.RangeSetType;
import net.opengis.wcs10.RangeSetType1;
import net.opengis.wcs10.ServiceType;
import net.opengis.wcs10.SpatialDomainType;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.SupportedCRSsType;
import net.opengis.wcs10.SupportedFormatsType;
import net.opengis.wcs10.SupportedInterpolationsType;
import net.opengis.wcs10.TimePeriodType;
import net.opengis.wcs10.TimeSequenceType;
import net.opengis.wcs10.TypedLiteralType;
import net.opengis.wcs10.WCSCapabilitiesType;
import net.opengis.wcs10.WCSCapabilityType;
import net.opengis.wcs10.Wcs10Package;

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
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getAxisDescription <em>Axis Description</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getAxisDescription1 <em>Axis Description1</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getCapability <em>Capability</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getContentMetadata <em>Content Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getCoverageDescription <em>Coverage Description</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getCoverageOffering <em>Coverage Offering</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getCoverageOfferingBrief <em>Coverage Offering Brief</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getDescribeCoverage <em>Describe Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getFormats <em>Formats</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getGetCoverage <em>Get Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getInterval <em>Interval</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getLonLatEnvelope <em>Lon Lat Envelope</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getMetadataLink <em>Metadata Link</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getRangeSet <em>Range Set</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getRangeSet1 <em>Range Set1</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getSingleValue <em>Single Value</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getSpatialDomain <em>Spatial Domain</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getSpatialSubset <em>Spatial Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getSupportedCRSs <em>Supported CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getSupportedFormats <em>Supported Formats</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getSupportedInterpolations <em>Supported Interpolations</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getTemporalDomain <em>Temporal Domain</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getTemporalSubset <em>Temporal Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getTimePeriod <em>Time Period</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getTimeSequence <em>Time Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getWCSCapabilities <em>WCS Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getClosure <em>Closure</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getSemantic <em>Semantic</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.DocumentRootImpl#getType <em>Type</em>}</li>
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
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected static final String DESCRIPTION_EDEFAULT = null;

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
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The default value of the '{@link #getClosure() <em>Closure</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getClosure()
	 * @generated
	 * @ordered
	 */
    protected static final ClosureType CLOSURE_EDEFAULT = ClosureType.CLOSED_LITERAL;

    /**
	 * The cached value of the '{@link #getClosure() <em>Closure</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getClosure()
	 * @generated
	 * @ordered
	 */
    protected ClosureType closure = CLOSURE_EDEFAULT;

    /**
	 * This is true if the Closure attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean closureESet;

    /**
	 * The default value of the '{@link #getSemantic() <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSemantic()
	 * @generated
	 * @ordered
	 */
    protected static final String SEMANTIC_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSemantic() <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getSemantic()
	 * @generated
	 * @ordered
	 */
    protected String semantic = SEMANTIC_EDEFAULT;

    /**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected static final String TYPE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected String type = TYPE_EDEFAULT;

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
		return Wcs10Package.Literals.DOCUMENT_ROOT;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, Wcs10Package.DOCUMENT_ROOT__MIXED);
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
			xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Wcs10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
			xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Wcs10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AxisDescriptionType1 getAxisDescription() {
		return (AxisDescriptionType1)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__AXIS_DESCRIPTION, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAxisDescription(AxisDescriptionType1 newAxisDescription, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__AXIS_DESCRIPTION, newAxisDescription, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAxisDescription(AxisDescriptionType1 newAxisDescription) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__AXIS_DESCRIPTION, newAxisDescription);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AxisDescriptionType getAxisDescription1() {
		return (AxisDescriptionType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__AXIS_DESCRIPTION1, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAxisDescription1(AxisDescriptionType newAxisDescription1, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__AXIS_DESCRIPTION1, newAxisDescription1, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAxisDescription1(AxisDescriptionType newAxisDescription1) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__AXIS_DESCRIPTION1, newAxisDescription1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public WCSCapabilityType getCapability() {
		return (WCSCapabilityType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__CAPABILITY, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCapability(WCSCapabilityType newCapability, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__CAPABILITY, newCapability, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCapability(WCSCapabilityType newCapability) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__CAPABILITY, newCapability);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ContentMetadataType getContentMetadata() {
		return (ContentMetadataType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__CONTENT_METADATA, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetContentMetadata(ContentMetadataType newContentMetadata, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__CONTENT_METADATA, newContentMetadata, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setContentMetadata(ContentMetadataType newContentMetadata) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__CONTENT_METADATA, newContentMetadata);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CoverageDescriptionType getCoverageDescription() {
		return (CoverageDescriptionType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_DESCRIPTION, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCoverageDescription(CoverageDescriptionType newCoverageDescription, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_DESCRIPTION, newCoverageDescription, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCoverageDescription(CoverageDescriptionType newCoverageDescription) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_DESCRIPTION, newCoverageDescription);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CoverageOfferingType getCoverageOffering() {
		return (CoverageOfferingType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_OFFERING, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCoverageOffering(CoverageOfferingType newCoverageOffering, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_OFFERING, newCoverageOffering, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCoverageOffering(CoverageOfferingType newCoverageOffering) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_OFFERING, newCoverageOffering);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CoverageOfferingBriefType getCoverageOfferingBrief() {
		return (CoverageOfferingBriefType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCoverageOfferingBrief(CoverageOfferingBriefType newCoverageOfferingBrief, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF, newCoverageOfferingBrief, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCoverageOfferingBrief(CoverageOfferingBriefType newCoverageOfferingBrief) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF, newCoverageOfferingBrief);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public DescribeCoverageType getDescribeCoverage() {
		return (DescribeCoverageType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__DESCRIBE_COVERAGE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDescribeCoverage(DescribeCoverageType newDescribeCoverage, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__DESCRIBE_COVERAGE, newDescribeCoverage, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescribeCoverage(DescribeCoverageType newDescribeCoverage) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__DESCRIBE_COVERAGE, newDescribeCoverage);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription() {
		return (String)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__DESCRIPTION, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(String newDescription) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__DESCRIPTION, newDescription);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainSetType getDomainSet() {
		return (DomainSetType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__DOMAIN_SET, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDomainSet(DomainSetType newDomainSet, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__DOMAIN_SET, newDomainSet, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDomainSet(DomainSetType newDomainSet) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__DOMAIN_SET, newDomainSet);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CodeListType getFormats() {
		return (CodeListType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__FORMATS, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetFormats(CodeListType newFormats, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__FORMATS, newFormats, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFormats(CodeListType newFormats) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__FORMATS, newFormats);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public GetCapabilitiesType getGetCapabilities() {
		return (GetCapabilitiesType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public GetCoverageType getGetCoverage() {
		return (GetCoverageType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__GET_COVERAGE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGetCoverage(GetCoverageType newGetCoverage, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__GET_COVERAGE, newGetCoverage, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGetCoverage(GetCoverageType newGetCoverage) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__GET_COVERAGE, newGetCoverage);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public InterpolationMethodType getInterpolationMethod() {
		return (InterpolationMethodType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__INTERPOLATION_METHOD, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInterpolationMethod(InterpolationMethodType newInterpolationMethod) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__INTERPOLATION_METHOD, newInterpolationMethod);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public IntervalType getInterval() {
		return (IntervalType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__INTERVAL, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetInterval(IntervalType newInterval, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__INTERVAL, newInterval, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInterval(IntervalType newInterval) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__INTERVAL, newInterval);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public KeywordsType getKeywords() {
		return (KeywordsType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__KEYWORDS, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetKeywords(KeywordsType newKeywords, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__KEYWORDS, newKeywords, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setKeywords(KeywordsType newKeywords) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__KEYWORDS, newKeywords);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public LonLatEnvelopeType getLonLatEnvelope() {
		return (LonLatEnvelopeType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__LON_LAT_ENVELOPE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLonLatEnvelope(LonLatEnvelopeType newLonLatEnvelope, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__LON_LAT_ENVELOPE, newLonLatEnvelope, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLonLatEnvelope(LonLatEnvelopeType newLonLatEnvelope) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__LON_LAT_ENVELOPE, newLonLatEnvelope);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public MetadataLinkType getMetadataLink() {
		return (MetadataLinkType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__METADATA_LINK, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMetadataLink(MetadataLinkType newMetadataLink, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__METADATA_LINK, newMetadataLink, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMetadataLink(MetadataLinkType newMetadataLink) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__METADATA_LINK, newMetadataLink);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
		return (String)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__NAME, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__NAME, newName);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public RangeSetType1 getRangeSet() {
		return (RangeSetType1)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__RANGE_SET, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRangeSet(RangeSetType1 newRangeSet, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__RANGE_SET, newRangeSet, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRangeSet(RangeSetType1 newRangeSet) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__RANGE_SET, newRangeSet);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public RangeSetType getRangeSet1() {
		return (RangeSetType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__RANGE_SET1, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRangeSet1(RangeSetType newRangeSet1, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__RANGE_SET1, newRangeSet1, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRangeSet1(RangeSetType newRangeSet1) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__RANGE_SET1, newRangeSet1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ServiceType getService() {
		return (ServiceType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__SERVICE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetService(ServiceType newService, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__SERVICE, newService, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setService(ServiceType newService) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__SERVICE, newService);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TypedLiteralType getSingleValue() {
		return (TypedLiteralType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__SINGLE_VALUE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSingleValue(TypedLiteralType newSingleValue, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__SINGLE_VALUE, newSingleValue, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSingleValue(TypedLiteralType newSingleValue) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__SINGLE_VALUE, newSingleValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SpatialDomainType getSpatialDomain() {
		return (SpatialDomainType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__SPATIAL_DOMAIN, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSpatialDomain(SpatialDomainType newSpatialDomain, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__SPATIAL_DOMAIN, newSpatialDomain, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSpatialDomain(SpatialDomainType newSpatialDomain) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__SPATIAL_DOMAIN, newSpatialDomain);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SpatialSubsetType getSpatialSubset() {
		return (SpatialSubsetType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__SPATIAL_SUBSET, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSpatialSubset(SpatialSubsetType newSpatialSubset, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__SPATIAL_SUBSET, newSpatialSubset, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSpatialSubset(SpatialSubsetType newSpatialSubset) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__SPATIAL_SUBSET, newSpatialSubset);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SupportedCRSsType getSupportedCRSs() {
		return (SupportedCRSsType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_CR_SS, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSupportedCRSs(SupportedCRSsType newSupportedCRSs, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_CR_SS, newSupportedCRSs, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSupportedCRSs(SupportedCRSsType newSupportedCRSs) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_CR_SS, newSupportedCRSs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SupportedFormatsType getSupportedFormats() {
		return (SupportedFormatsType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_FORMATS, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSupportedFormats(SupportedFormatsType newSupportedFormats, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_FORMATS, newSupportedFormats, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSupportedFormats(SupportedFormatsType newSupportedFormats) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_FORMATS, newSupportedFormats);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public SupportedInterpolationsType getSupportedInterpolations() {
		return (SupportedInterpolationsType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSupportedInterpolations(SupportedInterpolationsType newSupportedInterpolations, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS, newSupportedInterpolations, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSupportedInterpolations(SupportedInterpolationsType newSupportedInterpolations) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS, newSupportedInterpolations);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimeSequenceType getTemporalDomain() {
		return (TimeSequenceType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__TEMPORAL_DOMAIN, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTemporalDomain(TimeSequenceType newTemporalDomain, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__TEMPORAL_DOMAIN, newTemporalDomain, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTemporalDomain(TimeSequenceType newTemporalDomain) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__TEMPORAL_DOMAIN, newTemporalDomain);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimeSequenceType getTemporalSubset() {
		return (TimeSequenceType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__TEMPORAL_SUBSET, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTemporalSubset(TimeSequenceType newTemporalSubset, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__TEMPORAL_SUBSET, newTemporalSubset, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTemporalSubset(TimeSequenceType newTemporalSubset) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__TEMPORAL_SUBSET, newTemporalSubset);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimePeriodType getTimePeriod() {
		return (TimePeriodType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__TIME_PERIOD, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTimePeriod(TimePeriodType newTimePeriod, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__TIME_PERIOD, newTimePeriod, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTimePeriod(TimePeriodType newTimePeriod) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__TIME_PERIOD, newTimePeriod);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TimeSequenceType getTimeSequence() {
		return (TimeSequenceType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__TIME_SEQUENCE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTimeSequence(TimeSequenceType newTimeSequence, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__TIME_SEQUENCE, newTimeSequence, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTimeSequence(TimeSequenceType newTimeSequence) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__TIME_SEQUENCE, newTimeSequence);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public WCSCapabilitiesType getWCSCapabilities() {
		return (WCSCapabilitiesType)getMixed().get(Wcs10Package.Literals.DOCUMENT_ROOT__WCS_CAPABILITIES, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetWCSCapabilities(WCSCapabilitiesType newWCSCapabilities, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Wcs10Package.Literals.DOCUMENT_ROOT__WCS_CAPABILITIES, newWCSCapabilities, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setWCSCapabilities(WCSCapabilitiesType newWCSCapabilities) {
		((FeatureMap.Internal)getMixed()).set(Wcs10Package.Literals.DOCUMENT_ROOT__WCS_CAPABILITIES, newWCSCapabilities);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ClosureType getClosure() {
		return closure;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setClosure(ClosureType newClosure) {
		ClosureType oldClosure = closure;
		closure = newClosure == null ? CLOSURE_EDEFAULT : newClosure;
		boolean oldClosureESet = closureESet;
		closureESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.DOCUMENT_ROOT__CLOSURE, oldClosure, closure, !oldClosureESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetClosure() {
		ClosureType oldClosure = closure;
		boolean oldClosureESet = closureESet;
		closure = CLOSURE_EDEFAULT;
		closureESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.DOCUMENT_ROOT__CLOSURE, oldClosure, CLOSURE_EDEFAULT, oldClosureESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetClosure() {
		return closureESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSemantic() {
		return semantic;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSemantic(String newSemantic) {
		String oldSemantic = semantic;
		semantic = newSemantic;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.DOCUMENT_ROOT__SEMANTIC, oldSemantic, semantic));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getType() {
		return type;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.DOCUMENT_ROOT__TYPE, oldType, type));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.DOCUMENT_ROOT__MIXED:
				return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
			case Wcs10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
			case Wcs10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION:
				return basicSetAxisDescription(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION1:
				return basicSetAxisDescription1(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__CAPABILITY:
				return basicSetCapability(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__CONTENT_METADATA:
				return basicSetContentMetadata(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
				return basicSetCoverageDescription(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING:
				return basicSetCoverageOffering(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF:
				return basicSetCoverageOfferingBrief(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
				return basicSetDescribeCoverage(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__DOMAIN_SET:
				return basicSetDomainSet(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__FORMATS:
				return basicSetFormats(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				return basicSetGetCapabilities(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__GET_COVERAGE:
				return basicSetGetCoverage(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__INTERVAL:
				return basicSetInterval(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__KEYWORDS:
				return basicSetKeywords(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__LON_LAT_ENVELOPE:
				return basicSetLonLatEnvelope(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__METADATA_LINK:
				return basicSetMetadataLink(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET:
				return basicSetRangeSet(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET1:
				return basicSetRangeSet1(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__SERVICE:
				return basicSetService(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__SINGLE_VALUE:
				return basicSetSingleValue(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_DOMAIN:
				return basicSetSpatialDomain(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_SUBSET:
				return basicSetSpatialSubset(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_CR_SS:
				return basicSetSupportedCRSs(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_FORMATS:
				return basicSetSupportedFormats(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS:
				return basicSetSupportedInterpolations(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
				return basicSetTemporalDomain(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_SUBSET:
				return basicSetTemporalSubset(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__TIME_PERIOD:
				return basicSetTimePeriod(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__TIME_SEQUENCE:
				return basicSetTimeSequence(null, msgs);
			case Wcs10Package.DOCUMENT_ROOT__WCS_CAPABILITIES:
				return basicSetWCSCapabilities(null, msgs);
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
			case Wcs10Package.DOCUMENT_ROOT__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case Wcs10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				if (coreType) return getXMLNSPrefixMap();
				else return getXMLNSPrefixMap().map();
			case Wcs10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				if (coreType) return getXSISchemaLocation();
				else return getXSISchemaLocation().map();
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION:
				return getAxisDescription();
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION1:
				return getAxisDescription1();
			case Wcs10Package.DOCUMENT_ROOT__CAPABILITY:
				return getCapability();
			case Wcs10Package.DOCUMENT_ROOT__CONTENT_METADATA:
				return getContentMetadata();
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
				return getCoverageDescription();
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING:
				return getCoverageOffering();
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF:
				return getCoverageOfferingBrief();
			case Wcs10Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
				return getDescribeCoverage();
			case Wcs10Package.DOCUMENT_ROOT__DESCRIPTION:
				return getDescription();
			case Wcs10Package.DOCUMENT_ROOT__DOMAIN_SET:
				return getDomainSet();
			case Wcs10Package.DOCUMENT_ROOT__FORMATS:
				return getFormats();
			case Wcs10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				return getGetCapabilities();
			case Wcs10Package.DOCUMENT_ROOT__GET_COVERAGE:
				return getGetCoverage();
			case Wcs10Package.DOCUMENT_ROOT__INTERPOLATION_METHOD:
				return getInterpolationMethod();
			case Wcs10Package.DOCUMENT_ROOT__INTERVAL:
				return getInterval();
			case Wcs10Package.DOCUMENT_ROOT__KEYWORDS:
				return getKeywords();
			case Wcs10Package.DOCUMENT_ROOT__LON_LAT_ENVELOPE:
				return getLonLatEnvelope();
			case Wcs10Package.DOCUMENT_ROOT__METADATA_LINK:
				return getMetadataLink();
			case Wcs10Package.DOCUMENT_ROOT__NAME:
				return getName();
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET:
				return getRangeSet();
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET1:
				return getRangeSet1();
			case Wcs10Package.DOCUMENT_ROOT__SERVICE:
				return getService();
			case Wcs10Package.DOCUMENT_ROOT__SINGLE_VALUE:
				return getSingleValue();
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_DOMAIN:
				return getSpatialDomain();
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_SUBSET:
				return getSpatialSubset();
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_CR_SS:
				return getSupportedCRSs();
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_FORMATS:
				return getSupportedFormats();
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS:
				return getSupportedInterpolations();
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
				return getTemporalDomain();
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_SUBSET:
				return getTemporalSubset();
			case Wcs10Package.DOCUMENT_ROOT__TIME_PERIOD:
				return getTimePeriod();
			case Wcs10Package.DOCUMENT_ROOT__TIME_SEQUENCE:
				return getTimeSequence();
			case Wcs10Package.DOCUMENT_ROOT__WCS_CAPABILITIES:
				return getWCSCapabilities();
			case Wcs10Package.DOCUMENT_ROOT__CLOSURE:
				return getClosure();
			case Wcs10Package.DOCUMENT_ROOT__SEMANTIC:
				return getSemantic();
			case Wcs10Package.DOCUMENT_ROOT__TYPE:
				return getType();
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
			case Wcs10Package.DOCUMENT_ROOT__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION:
				setAxisDescription((AxisDescriptionType1)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION1:
				setAxisDescription1((AxisDescriptionType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__CAPABILITY:
				setCapability((WCSCapabilityType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__CONTENT_METADATA:
				setContentMetadata((ContentMetadataType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
				setCoverageDescription((CoverageDescriptionType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING:
				setCoverageOffering((CoverageOfferingType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF:
				setCoverageOfferingBrief((CoverageOfferingBriefType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
				setDescribeCoverage((DescribeCoverageType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__DOMAIN_SET:
				setDomainSet((DomainSetType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__FORMATS:
				setFormats((CodeListType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__GET_COVERAGE:
				setGetCoverage((GetCoverageType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__INTERPOLATION_METHOD:
				setInterpolationMethod((InterpolationMethodType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__INTERVAL:
				setInterval((IntervalType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__KEYWORDS:
				setKeywords((KeywordsType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__LON_LAT_ENVELOPE:
				setLonLatEnvelope((LonLatEnvelopeType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__METADATA_LINK:
				setMetadataLink((MetadataLinkType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__NAME:
				setName((String)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET:
				setRangeSet((RangeSetType1)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET1:
				setRangeSet1((RangeSetType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SERVICE:
				setService((ServiceType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SINGLE_VALUE:
				setSingleValue((TypedLiteralType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_DOMAIN:
				setSpatialDomain((SpatialDomainType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_SUBSET:
				setSpatialSubset((SpatialSubsetType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_CR_SS:
				setSupportedCRSs((SupportedCRSsType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_FORMATS:
				setSupportedFormats((SupportedFormatsType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS:
				setSupportedInterpolations((SupportedInterpolationsType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
				setTemporalDomain((TimeSequenceType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_SUBSET:
				setTemporalSubset((TimeSequenceType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TIME_PERIOD:
				setTimePeriod((TimePeriodType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TIME_SEQUENCE:
				setTimeSequence((TimeSequenceType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__WCS_CAPABILITIES:
				setWCSCapabilities((WCSCapabilitiesType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__CLOSURE:
				setClosure((ClosureType)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SEMANTIC:
				setSemantic((String)newValue);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TYPE:
				setType((String)newValue);
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
			case Wcs10Package.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case Wcs10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case Wcs10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION:
				setAxisDescription((AxisDescriptionType1)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION1:
				setAxisDescription1((AxisDescriptionType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__CAPABILITY:
				setCapability((WCSCapabilityType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__CONTENT_METADATA:
				setContentMetadata((ContentMetadataType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
				setCoverageDescription((CoverageDescriptionType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING:
				setCoverageOffering((CoverageOfferingType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF:
				setCoverageOfferingBrief((CoverageOfferingBriefType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
				setDescribeCoverage((DescribeCoverageType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case Wcs10Package.DOCUMENT_ROOT__DOMAIN_SET:
				setDomainSet((DomainSetType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__FORMATS:
				setFormats((CodeListType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__GET_COVERAGE:
				setGetCoverage((GetCoverageType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__INTERPOLATION_METHOD:
				setInterpolationMethod(INTERPOLATION_METHOD_EDEFAULT);
				return;
			case Wcs10Package.DOCUMENT_ROOT__INTERVAL:
				setInterval((IntervalType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__KEYWORDS:
				setKeywords((KeywordsType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__LON_LAT_ENVELOPE:
				setLonLatEnvelope((LonLatEnvelopeType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__METADATA_LINK:
				setMetadataLink((MetadataLinkType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET:
				setRangeSet((RangeSetType1)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET1:
				setRangeSet1((RangeSetType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SERVICE:
				setService((ServiceType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SINGLE_VALUE:
				setSingleValue((TypedLiteralType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_DOMAIN:
				setSpatialDomain((SpatialDomainType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_SUBSET:
				setSpatialSubset((SpatialSubsetType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_CR_SS:
				setSupportedCRSs((SupportedCRSsType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_FORMATS:
				setSupportedFormats((SupportedFormatsType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS:
				setSupportedInterpolations((SupportedInterpolationsType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
				setTemporalDomain((TimeSequenceType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_SUBSET:
				setTemporalSubset((TimeSequenceType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TIME_PERIOD:
				setTimePeriod((TimePeriodType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TIME_SEQUENCE:
				setTimeSequence((TimeSequenceType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__WCS_CAPABILITIES:
				setWCSCapabilities((WCSCapabilitiesType)null);
				return;
			case Wcs10Package.DOCUMENT_ROOT__CLOSURE:
				unsetClosure();
				return;
			case Wcs10Package.DOCUMENT_ROOT__SEMANTIC:
				setSemantic(SEMANTIC_EDEFAULT);
				return;
			case Wcs10Package.DOCUMENT_ROOT__TYPE:
				setType(TYPE_EDEFAULT);
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
			case Wcs10Package.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case Wcs10Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case Wcs10Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION:
				return getAxisDescription() != null;
			case Wcs10Package.DOCUMENT_ROOT__AXIS_DESCRIPTION1:
				return getAxisDescription1() != null;
			case Wcs10Package.DOCUMENT_ROOT__CAPABILITY:
				return getCapability() != null;
			case Wcs10Package.DOCUMENT_ROOT__CONTENT_METADATA:
				return getContentMetadata() != null;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
				return getCoverageDescription() != null;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING:
				return getCoverageOffering() != null;
			case Wcs10Package.DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF:
				return getCoverageOfferingBrief() != null;
			case Wcs10Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
				return getDescribeCoverage() != null;
			case Wcs10Package.DOCUMENT_ROOT__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? getDescription() != null : !DESCRIPTION_EDEFAULT.equals(getDescription());
			case Wcs10Package.DOCUMENT_ROOT__DOMAIN_SET:
				return getDomainSet() != null;
			case Wcs10Package.DOCUMENT_ROOT__FORMATS:
				return getFormats() != null;
			case Wcs10Package.DOCUMENT_ROOT__GET_CAPABILITIES:
				return getGetCapabilities() != null;
			case Wcs10Package.DOCUMENT_ROOT__GET_COVERAGE:
				return getGetCoverage() != null;
			case Wcs10Package.DOCUMENT_ROOT__INTERPOLATION_METHOD:
				return getInterpolationMethod() != INTERPOLATION_METHOD_EDEFAULT;
			case Wcs10Package.DOCUMENT_ROOT__INTERVAL:
				return getInterval() != null;
			case Wcs10Package.DOCUMENT_ROOT__KEYWORDS:
				return getKeywords() != null;
			case Wcs10Package.DOCUMENT_ROOT__LON_LAT_ENVELOPE:
				return getLonLatEnvelope() != null;
			case Wcs10Package.DOCUMENT_ROOT__METADATA_LINK:
				return getMetadataLink() != null;
			case Wcs10Package.DOCUMENT_ROOT__NAME:
				return NAME_EDEFAULT == null ? getName() != null : !NAME_EDEFAULT.equals(getName());
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET:
				return getRangeSet() != null;
			case Wcs10Package.DOCUMENT_ROOT__RANGE_SET1:
				return getRangeSet1() != null;
			case Wcs10Package.DOCUMENT_ROOT__SERVICE:
				return getService() != null;
			case Wcs10Package.DOCUMENT_ROOT__SINGLE_VALUE:
				return getSingleValue() != null;
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_DOMAIN:
				return getSpatialDomain() != null;
			case Wcs10Package.DOCUMENT_ROOT__SPATIAL_SUBSET:
				return getSpatialSubset() != null;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_CR_SS:
				return getSupportedCRSs() != null;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_FORMATS:
				return getSupportedFormats() != null;
			case Wcs10Package.DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS:
				return getSupportedInterpolations() != null;
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
				return getTemporalDomain() != null;
			case Wcs10Package.DOCUMENT_ROOT__TEMPORAL_SUBSET:
				return getTemporalSubset() != null;
			case Wcs10Package.DOCUMENT_ROOT__TIME_PERIOD:
				return getTimePeriod() != null;
			case Wcs10Package.DOCUMENT_ROOT__TIME_SEQUENCE:
				return getTimeSequence() != null;
			case Wcs10Package.DOCUMENT_ROOT__WCS_CAPABILITIES:
				return getWCSCapabilities() != null;
			case Wcs10Package.DOCUMENT_ROOT__CLOSURE:
				return isSetClosure();
			case Wcs10Package.DOCUMENT_ROOT__SEMANTIC:
				return SEMANTIC_EDEFAULT == null ? semantic != null : !SEMANTIC_EDEFAULT.equals(semantic);
			case Wcs10Package.DOCUMENT_ROOT__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
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
		result.append(", closure: ");
		if (closureESet) result.append(closure); else result.append("<unset>");
		result.append(", semantic: ");
		result.append(semantic);
		result.append(", type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
