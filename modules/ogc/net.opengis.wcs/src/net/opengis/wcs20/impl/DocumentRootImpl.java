/**
 */
package net.opengis.wcs20.impl;

import javax.xml.namespace.QName;

import net.opengis.wcs20.CapabilitiesType;
import net.opengis.wcs20.ContentsType;
import net.opengis.wcs20.CoverageDescriptionType;
import net.opengis.wcs20.CoverageDescriptionsType;
import net.opengis.wcs20.CoverageOfferingsType;
import net.opengis.wcs20.CoverageSubtypeParentType;
import net.opengis.wcs20.CoverageSummaryType;
import net.opengis.wcs20.DescribeCoverageType;
import net.opengis.wcs20.DimensionSliceType;
import net.opengis.wcs20.DimensionSubsetType;
import net.opengis.wcs20.DimensionTrimType;
import net.opengis.wcs20.DocumentRoot;
import net.opengis.wcs20.ExtensionType;
import net.opengis.wcs20.GetCapabilitiesType;
import net.opengis.wcs20.GetCoverageType;
import net.opengis.wcs20.OfferedCoverageType;
import net.opengis.wcs20.ServiceMetadataType;
import net.opengis.wcs20.ServiceParametersType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

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
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getCoverageDescription <em>Coverage Description</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getCoverageDescriptions <em>Coverage Descriptions</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getCoverageId <em>Coverage Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getCoverageOfferings <em>Coverage Offerings</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getCoverageSubtype <em>Coverage Subtype</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getDescribeCoverage <em>Describe Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getDimensionSlice <em>Dimension Slice</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getDimensionSubset <em>Dimension Subset</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getDimensionTrim <em>Dimension Trim</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getExtension <em>Extension</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getGetCoverage <em>Get Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getOfferedCoverage <em>Offered Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getServiceMetadata <em>Service Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DocumentRootImpl#getServiceParameters <em>Service Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
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
     * The default value of the '{@link #getCoverageId() <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageId()
     * @generated
     * @ordered
     */
    protected static final String COVERAGE_ID_EDEFAULT = null;

    /**
     * The default value of the '{@link #getCoverageSubtype() <em>Coverage Subtype</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSubtype()
     * @generated
     * @ordered
     */
    protected static final QName COVERAGE_SUBTYPE_EDEFAULT = null;

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
        return Wcs20Package.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap<String, String> getXMLNSPrefixMap() {
        if (xMLNSPrefixMap == null) {
            xMLNSPrefixMap = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Wcs20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
            xSISchemaLocation = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Wcs20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CapabilitiesType getCapabilities() {
        // TODO: implement this method to return the 'Capabilities' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCapabilities(CapabilitiesType newCapabilities, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Capabilities' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCapabilities(CapabilitiesType newCapabilities) {
        // TODO: implement this method to set the 'Capabilities' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContentsType getContents() {
        // TODO: implement this method to return the 'Contents' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetContents(ContentsType newContents, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Contents' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setContents(ContentsType newContents) {
        // TODO: implement this method to set the 'Contents' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDescriptionType getCoverageDescription() {
        // TODO: implement this method to return the 'Coverage Description' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageDescription(CoverageDescriptionType newCoverageDescription, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Coverage Description' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageDescription(CoverageDescriptionType newCoverageDescription) {
        // TODO: implement this method to set the 'Coverage Description' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDescriptionsType getCoverageDescriptions() {
        // TODO: implement this method to return the 'Coverage Descriptions' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageDescriptions(CoverageDescriptionsType newCoverageDescriptions, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Coverage Descriptions' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageDescriptions(CoverageDescriptionsType newCoverageDescriptions) {
        // TODO: implement this method to set the 'Coverage Descriptions' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCoverageId() {
        // TODO: implement this method to return the 'Coverage Id' attribute
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageId(String newCoverageId) {
        // TODO: implement this method to set the 'Coverage Id' attribute
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageOfferingsType getCoverageOfferings() {
        // TODO: implement this method to return the 'Coverage Offerings' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageOfferings(CoverageOfferingsType newCoverageOfferings, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Coverage Offerings' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageOfferings(CoverageOfferingsType newCoverageOfferings) {
        // TODO: implement this method to set the 'Coverage Offerings' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getCoverageSubtype() {
        // TODO: implement this method to return the 'Coverage Subtype' attribute
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageSubtype(QName newCoverageSubtype) {
        // TODO: implement this method to set the 'Coverage Subtype' attribute
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageSubtypeParentType getCoverageSubtypeParent() {
        // TODO: implement this method to return the 'Coverage Subtype Parent' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageSubtypeParent(CoverageSubtypeParentType newCoverageSubtypeParent, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Coverage Subtype Parent' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageSubtypeParent(CoverageSubtypeParentType newCoverageSubtypeParent) {
        // TODO: implement this method to set the 'Coverage Subtype Parent' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageSummaryType getCoverageSummary() {
        // TODO: implement this method to return the 'Coverage Summary' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageSummary(CoverageSummaryType newCoverageSummary, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Coverage Summary' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageSummary(CoverageSummaryType newCoverageSummary) {
        // TODO: implement this method to set the 'Coverage Summary' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeCoverageType getDescribeCoverage() {
        // TODO: implement this method to return the 'Describe Coverage' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDescribeCoverage(DescribeCoverageType newDescribeCoverage, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Describe Coverage' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDescribeCoverage(DescribeCoverageType newDescribeCoverage) {
        // TODO: implement this method to set the 'Describe Coverage' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DimensionSliceType getDimensionSlice() {
        // TODO: implement this method to return the 'Dimension Slice' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDimensionSlice(DimensionSliceType newDimensionSlice, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Dimension Slice' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDimensionSlice(DimensionSliceType newDimensionSlice) {
        // TODO: implement this method to set the 'Dimension Slice' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DimensionSubsetType getDimensionSubset() {
        // TODO: implement this method to return the 'Dimension Subset' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDimensionSubset(DimensionSubsetType newDimensionSubset, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Dimension Subset' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DimensionTrimType getDimensionTrim() {
        // TODO: implement this method to return the 'Dimension Trim' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDimensionTrim(DimensionTrimType newDimensionTrim, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Dimension Trim' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDimensionTrim(DimensionTrimType newDimensionTrim) {
        // TODO: implement this method to set the 'Dimension Trim' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtensionType getExtension() {
        // TODO: implement this method to return the 'Extension' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExtension(ExtensionType newExtension, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Extension' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtension(ExtensionType newExtension) {
        // TODO: implement this method to set the 'Extension' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType getGetCapabilities() {
        // TODO: implement this method to return the 'Get Capabilities' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Get Capabilities' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
        // TODO: implement this method to set the 'Get Capabilities' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCoverageType getGetCoverage() {
        // TODO: implement this method to return the 'Get Coverage' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetCoverage(GetCoverageType newGetCoverage, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Get Coverage' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetCoverage(GetCoverageType newGetCoverage) {
        // TODO: implement this method to set the 'Get Coverage' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OfferedCoverageType getOfferedCoverage() {
        // TODO: implement this method to return the 'Offered Coverage' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOfferedCoverage(OfferedCoverageType newOfferedCoverage, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Offered Coverage' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOfferedCoverage(OfferedCoverageType newOfferedCoverage) {
        // TODO: implement this method to set the 'Offered Coverage' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceMetadataType getServiceMetadata() {
        // TODO: implement this method to return the 'Service Metadata' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceMetadata(ServiceMetadataType newServiceMetadata, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Service Metadata' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceMetadata(ServiceMetadataType newServiceMetadata) {
        // TODO: implement this method to set the 'Service Metadata' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceParametersType getServiceParameters() {
        // TODO: implement this method to return the 'Service Parameters' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceParameters(ServiceParametersType newServiceParameters, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Service Parameters' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceParameters(ServiceParametersType newServiceParameters) {
        // TODO: implement this method to set the 'Service Parameters' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case Wcs20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case Wcs20Package.DOCUMENT_ROOT__CAPABILITIES:
                return basicSetCapabilities(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__CONTENTS:
                return basicSetContents(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
                return basicSetCoverageDescription(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                return basicSetCoverageDescriptions(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_OFFERINGS:
                return basicSetCoverageOfferings(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE_PARENT:
                return basicSetCoverageSubtypeParent(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                return basicSetCoverageSummary(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                return basicSetDescribeCoverage(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_SLICE:
                return basicSetDimensionSlice(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_SUBSET:
                return basicSetDimensionSubset(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_TRIM:
                return basicSetDimensionTrim(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__EXTENSION:
                return basicSetExtension(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return basicSetGetCapabilities(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__GET_COVERAGE:
                return basicSetGetCoverage(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__OFFERED_COVERAGE:
                return basicSetOfferedCoverage(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_METADATA:
                return basicSetServiceMetadata(null, msgs);
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_PARAMETERS:
                return basicSetServiceParameters(null, msgs);
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
            case Wcs20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case Wcs20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case Wcs20Package.DOCUMENT_ROOT__CAPABILITIES:
                return getCapabilities();
            case Wcs20Package.DOCUMENT_ROOT__CONTENTS:
                return getContents();
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
                return getCoverageDescription();
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                return getCoverageDescriptions();
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_ID:
                return getCoverageId();
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_OFFERINGS:
                return getCoverageOfferings();
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE:
                return getCoverageSubtype();
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE_PARENT:
                return getCoverageSubtypeParent();
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                return getCoverageSummary();
            case Wcs20Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                return getDescribeCoverage();
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_SLICE:
                return getDimensionSlice();
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_SUBSET:
                return getDimensionSubset();
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_TRIM:
                return getDimensionTrim();
            case Wcs20Package.DOCUMENT_ROOT__EXTENSION:
                return getExtension();
            case Wcs20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities();
            case Wcs20Package.DOCUMENT_ROOT__GET_COVERAGE:
                return getGetCoverage();
            case Wcs20Package.DOCUMENT_ROOT__OFFERED_COVERAGE:
                return getOfferedCoverage();
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_METADATA:
                return getServiceMetadata();
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_PARAMETERS:
                return getServiceParameters();
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
            case Wcs20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__CAPABILITIES:
                setCapabilities((CapabilitiesType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__CONTENTS:
                setContents((ContentsType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
                setCoverageDescription((CoverageDescriptionType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                setCoverageDescriptions((CoverageDescriptionsType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_ID:
                setCoverageId((String)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_OFFERINGS:
                setCoverageOfferings((CoverageOfferingsType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE:
                setCoverageSubtype((QName)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE_PARENT:
                setCoverageSubtypeParent((CoverageSubtypeParentType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                setCoverageSummary((CoverageSummaryType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                setDescribeCoverage((DescribeCoverageType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_SLICE:
                setDimensionSlice((DimensionSliceType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_TRIM:
                setDimensionTrim((DimensionTrimType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__EXTENSION:
                setExtension((ExtensionType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__GET_COVERAGE:
                setGetCoverage((GetCoverageType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__OFFERED_COVERAGE:
                setOfferedCoverage((OfferedCoverageType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_METADATA:
                setServiceMetadata((ServiceMetadataType)newValue);
                return;
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_PARAMETERS:
                setServiceParameters((ServiceParametersType)newValue);
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
            case Wcs20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case Wcs20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case Wcs20Package.DOCUMENT_ROOT__CAPABILITIES:
                setCapabilities((CapabilitiesType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__CONTENTS:
                setContents((ContentsType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
                setCoverageDescription((CoverageDescriptionType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                setCoverageDescriptions((CoverageDescriptionsType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_ID:
                setCoverageId(COVERAGE_ID_EDEFAULT);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_OFFERINGS:
                setCoverageOfferings((CoverageOfferingsType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE:
                setCoverageSubtype(COVERAGE_SUBTYPE_EDEFAULT);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE_PARENT:
                setCoverageSubtypeParent((CoverageSubtypeParentType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                setCoverageSummary((CoverageSummaryType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                setDescribeCoverage((DescribeCoverageType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_SLICE:
                setDimensionSlice((DimensionSliceType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_TRIM:
                setDimensionTrim((DimensionTrimType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__EXTENSION:
                setExtension((ExtensionType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__GET_COVERAGE:
                setGetCoverage((GetCoverageType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__OFFERED_COVERAGE:
                setOfferedCoverage((OfferedCoverageType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_METADATA:
                setServiceMetadata((ServiceMetadataType)null);
                return;
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_PARAMETERS:
                setServiceParameters((ServiceParametersType)null);
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
            case Wcs20Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case Wcs20Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case Wcs20Package.DOCUMENT_ROOT__CAPABILITIES:
                return getCapabilities() != null;
            case Wcs20Package.DOCUMENT_ROOT__CONTENTS:
                return getContents() != null;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTION:
                return getCoverageDescription() != null;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                return getCoverageDescriptions() != null;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_ID:
                return COVERAGE_ID_EDEFAULT == null ? getCoverageId() != null : !COVERAGE_ID_EDEFAULT.equals(getCoverageId());
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_OFFERINGS:
                return getCoverageOfferings() != null;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE:
                return COVERAGE_SUBTYPE_EDEFAULT == null ? getCoverageSubtype() != null : !COVERAGE_SUBTYPE_EDEFAULT.equals(getCoverageSubtype());
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUBTYPE_PARENT:
                return getCoverageSubtypeParent() != null;
            case Wcs20Package.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                return getCoverageSummary() != null;
            case Wcs20Package.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                return getDescribeCoverage() != null;
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_SLICE:
                return getDimensionSlice() != null;
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_SUBSET:
                return getDimensionSubset() != null;
            case Wcs20Package.DOCUMENT_ROOT__DIMENSION_TRIM:
                return getDimensionTrim() != null;
            case Wcs20Package.DOCUMENT_ROOT__EXTENSION:
                return getExtension() != null;
            case Wcs20Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities() != null;
            case Wcs20Package.DOCUMENT_ROOT__GET_COVERAGE:
                return getGetCoverage() != null;
            case Wcs20Package.DOCUMENT_ROOT__OFFERED_COVERAGE:
                return getOfferedCoverage() != null;
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_METADATA:
                return getServiceMetadata() != null;
            case Wcs20Package.DOCUMENT_ROOT__SERVICE_PARAMETERS:
                return getServiceParameters() != null;
        }
        return super.eIsSet(featureID);
    }

} //DocumentRootImpl
