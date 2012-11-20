/**
 */
package net.opengis.wcs20.impl;

import java.lang.Object;
import javax.xml.namespace.QName;

import net.opengis.ows11.Ows11Package;
import net.opengis.ows20.Ows20Package;
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
import net.opengis.wcs20.RequestBaseType;
import net.opengis.wcs20.ServiceMetadataType;
import net.opengis.wcs20.ServiceParametersType;
import net.opengis.wcs20.Wcs20Factory;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wcs20PackageImpl extends EPackageImpl implements Wcs20Package {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass capabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass contentsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageDescriptionsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageDescriptionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageOfferingsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageSubtypeParentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageSummaryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass describeCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dimensionSliceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dimensionSubsetTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dimensionTrimTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass documentRootEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass extensionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass offeredCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requestBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass serviceMetadataTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass serviceParametersTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass objectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType versionStringTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType versionStringType_1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType qNameEDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see net.opengis.wcs20.Wcs20Package#eNS_URI
     * @see #init()
     * @generated
     */
    private Wcs20PackageImpl() {
        super(eNS_URI, Wcs20Factory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link Wcs20Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static Wcs20Package init() {
        if (isInited) return (Wcs20Package)EPackage.Registry.INSTANCE.getEPackage(Wcs20Package.eNS_URI);

        // Obtain or create and register package
        Wcs20PackageImpl theWcs20Package = (Wcs20PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Wcs20PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Wcs20PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        XlinkPackage.eINSTANCE.eClass();
        Ows11Package.eINSTANCE.eClass();
        Ows20Package.eINSTANCE.eClass();

        // Create package meta-data objects
        theWcs20Package.createPackageContents();

        // Initialize created meta-data
        theWcs20Package.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theWcs20Package.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(Wcs20Package.eNS_URI, theWcs20Package);
        return theWcs20Package;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCapabilitiesType() {
        return capabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesType_ServiceMetadata() {
        return (EReference)capabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesType_Contents() {
        return (EReference)capabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getContentsType() {
        return contentsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getContentsType_CoverageSummary() {
        return (EReference)contentsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getContentsType_Extension() {
        return (EReference)contentsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageDescriptionsType() {
        return coverageDescriptionsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionsType_CoverageDescription() {
        return (EReference)coverageDescriptionsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageDescriptionType() {
        return coverageDescriptionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageDescriptionType_CoverageId() {
        return (EAttribute)coverageDescriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionType_CoverageFunction() {
        return (EReference)coverageDescriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionType_Metadata() {
        return (EReference)coverageDescriptionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionType_DomainSet() {
        return (EReference)coverageDescriptionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionType_RangeType() {
        return (EReference)coverageDescriptionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionType_ServiceParameters() {
        return (EReference)coverageDescriptionTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageOfferingsType() {
        return coverageOfferingsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageOfferingsType_ServiceMetadata() {
        return (EReference)coverageOfferingsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageOfferingsType_OfferedCoverage() {
        return (EReference)coverageOfferingsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageSubtypeParentType() {
        return coverageSubtypeParentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSubtypeParentType_CoverageSubtype() {
        return (EAttribute)coverageSubtypeParentTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageSubtypeParentType_CoverageSubtypeParent() {
        return (EReference)coverageSubtypeParentTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageSummaryType() {
        return coverageSummaryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageSummaryType_WGS84BoundingBox() {
        return (EReference)coverageSummaryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_CoverageId() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_CoverageSubtype() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageSummaryType_CoverageSubtypeParent() {
        return (EReference)coverageSummaryTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_BoundingBoxGroup() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageSummaryType_BoundingBox() {
        return (EReference)coverageSummaryTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_MetadataGroup() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageSummaryType_Metadata() {
        return (EReference)coverageSummaryTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescribeCoverageType() {
        return describeCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeCoverageType_CoverageId() {
        return (EAttribute)describeCoverageTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDimensionSliceType() {
        return dimensionSliceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionSliceType_SlicePoint() {
        return (EAttribute)dimensionSliceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDimensionSubsetType() {
        return dimensionSubsetTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionSubsetType_Dimension() {
        return (EAttribute)dimensionSubsetTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDimensionTrimType() {
        return dimensionTrimTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionTrimType_TrimLow() {
        return (EAttribute)dimensionTrimTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionTrimType_TrimHigh() {
        return (EAttribute)dimensionTrimTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDocumentRoot() {
        return documentRootEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XMLNSPrefixMap() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XSISchemaLocation() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Capabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Contents() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoverageDescription() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoverageDescriptions() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_CoverageId() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoverageOfferings() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_CoverageSubtype() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoverageSubtypeParent() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoverageSummary() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DescribeCoverage() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DimensionSlice() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DimensionSubset() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DimensionTrim() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Extension() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetCoverage() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OfferedCoverage() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ServiceMetadata() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ServiceParameters() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExtensionType() {
        return extensionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getExtensionType_Any() {
        return (EAttribute)extensionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetCapabilitiesType() {
        return getCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCapabilitiesType_Service() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetCoverageType() {
        return getCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCoverageType_CoverageId() {
        return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCoverageType_DimensionSubsetGroup() {
        return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetCoverageType_DimensionSubset() {
        return (EReference)getCoverageTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCoverageType_Format() {
        return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCoverageType_MediaType() {
        return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOfferedCoverageType() {
        return offeredCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOfferedCoverageType_AbstractCoverage() {
        return (EReference)offeredCoverageTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOfferedCoverageType_ServiceParameters() {
        return (EReference)offeredCoverageTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequestBaseType() {
        return requestBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRequestBaseType_Extension() {
        return (EReference)requestBaseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRequestBaseType_Service() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRequestBaseType_Version() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getServiceMetadataType() {
        return serviceMetadataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceMetadataType_FormatSupported() {
        return (EAttribute)serviceMetadataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getServiceMetadataType_Extension() {
        return (EReference)serviceMetadataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getServiceParametersType() {
        return serviceParametersTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceParametersType_CoverageSubtype() {
        return (EAttribute)serviceParametersTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getServiceParametersType_CoverageSubtypeParent() {
        return (EReference)serviceParametersTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceParametersType_NativeFormat() {
        return (EAttribute)serviceParametersTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getServiceParametersType_Extension() {
        return (EReference)serviceParametersTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getObject() {
        return objectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getVersionStringType() {
        return versionStringTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getVersionStringType_1() {
        return versionStringType_1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getQName() {
        return qNameEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wcs20Factory getWcs20Factory() {
        return (Wcs20Factory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        capabilitiesTypeEClass = createEClass(CAPABILITIES_TYPE);
        createEReference(capabilitiesTypeEClass, CAPABILITIES_TYPE__SERVICE_METADATA);
        createEReference(capabilitiesTypeEClass, CAPABILITIES_TYPE__CONTENTS);

        contentsTypeEClass = createEClass(CONTENTS_TYPE);
        createEReference(contentsTypeEClass, CONTENTS_TYPE__COVERAGE_SUMMARY);
        createEReference(contentsTypeEClass, CONTENTS_TYPE__EXTENSION);

        coverageDescriptionsTypeEClass = createEClass(COVERAGE_DESCRIPTIONS_TYPE);
        createEReference(coverageDescriptionsTypeEClass, COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION);

        coverageDescriptionTypeEClass = createEClass(COVERAGE_DESCRIPTION_TYPE);
        createEAttribute(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__COVERAGE_ID);
        createEReference(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION);
        createEReference(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__METADATA);
        createEReference(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__DOMAIN_SET);
        createEReference(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE);
        createEReference(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS);

        coverageOfferingsTypeEClass = createEClass(COVERAGE_OFFERINGS_TYPE);
        createEReference(coverageOfferingsTypeEClass, COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA);
        createEReference(coverageOfferingsTypeEClass, COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE);

        coverageSubtypeParentTypeEClass = createEClass(COVERAGE_SUBTYPE_PARENT_TYPE);
        createEAttribute(coverageSubtypeParentTypeEClass, COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE);
        createEReference(coverageSubtypeParentTypeEClass, COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT);

        coverageSummaryTypeEClass = createEClass(COVERAGE_SUMMARY_TYPE);
        createEReference(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__COVERAGE_ID);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE);
        createEReference(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP);
        createEReference(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__BOUNDING_BOX);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__METADATA_GROUP);
        createEReference(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__METADATA);

        describeCoverageTypeEClass = createEClass(DESCRIBE_COVERAGE_TYPE);
        createEAttribute(describeCoverageTypeEClass, DESCRIBE_COVERAGE_TYPE__COVERAGE_ID);

        dimensionSliceTypeEClass = createEClass(DIMENSION_SLICE_TYPE);
        createEAttribute(dimensionSliceTypeEClass, DIMENSION_SLICE_TYPE__SLICE_POINT);

        dimensionSubsetTypeEClass = createEClass(DIMENSION_SUBSET_TYPE);
        createEAttribute(dimensionSubsetTypeEClass, DIMENSION_SUBSET_TYPE__DIMENSION);

        dimensionTrimTypeEClass = createEClass(DIMENSION_TRIM_TYPE);
        createEAttribute(dimensionTrimTypeEClass, DIMENSION_TRIM_TYPE__TRIM_LOW);
        createEAttribute(dimensionTrimTypeEClass, DIMENSION_TRIM_TYPE__TRIM_HIGH);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CONTENTS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_DESCRIPTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__COVERAGE_ID);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_OFFERINGS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__COVERAGE_SUBTYPE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_SUBTYPE_PARENT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_SUMMARY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_COVERAGE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DIMENSION_SLICE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DIMENSION_SUBSET);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DIMENSION_TRIM);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXTENSION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_COVERAGE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__OFFERED_COVERAGE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SERVICE_METADATA);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SERVICE_PARAMETERS);

        extensionTypeEClass = createEClass(EXTENSION_TYPE);
        createEAttribute(extensionTypeEClass, EXTENSION_TYPE__ANY);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);

        getCoverageTypeEClass = createEClass(GET_COVERAGE_TYPE);
        createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__COVERAGE_ID);
        createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP);
        createEReference(getCoverageTypeEClass, GET_COVERAGE_TYPE__DIMENSION_SUBSET);
        createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__FORMAT);
        createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__MEDIA_TYPE);

        offeredCoverageTypeEClass = createEClass(OFFERED_COVERAGE_TYPE);
        createEReference(offeredCoverageTypeEClass, OFFERED_COVERAGE_TYPE__ABSTRACT_COVERAGE);
        createEReference(offeredCoverageTypeEClass, OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS);

        requestBaseTypeEClass = createEClass(REQUEST_BASE_TYPE);
        createEReference(requestBaseTypeEClass, REQUEST_BASE_TYPE__EXTENSION);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__SERVICE);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__VERSION);

        serviceMetadataTypeEClass = createEClass(SERVICE_METADATA_TYPE);
        createEAttribute(serviceMetadataTypeEClass, SERVICE_METADATA_TYPE__FORMAT_SUPPORTED);
        createEReference(serviceMetadataTypeEClass, SERVICE_METADATA_TYPE__EXTENSION);

        serviceParametersTypeEClass = createEClass(SERVICE_PARAMETERS_TYPE);
        createEAttribute(serviceParametersTypeEClass, SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE);
        createEReference(serviceParametersTypeEClass, SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT);
        createEAttribute(serviceParametersTypeEClass, SERVICE_PARAMETERS_TYPE__NATIVE_FORMAT);
        createEReference(serviceParametersTypeEClass, SERVICE_PARAMETERS_TYPE__EXTENSION);

        objectEClass = createEClass(OBJECT);

        // Create data types
        versionStringTypeEDataType = createEDataType(VERSION_STRING_TYPE);
        versionStringType_1EDataType = createEDataType(VERSION_STRING_TYPE_1);
        qNameEDataType = createEDataType(QNAME);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        Ows20Package theOws20Package = (Ows20Package)EPackage.Registry.INSTANCE.getEPackage(Ows20Package.eNS_URI);
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        capabilitiesTypeEClass.getESuperTypes().add(theOws20Package.getCapabilitiesBaseType());
        contentsTypeEClass.getESuperTypes().add(theOws20Package.getContentsBaseType());
        coverageSummaryTypeEClass.getESuperTypes().add(theOws20Package.getDescriptionType());
        describeCoverageTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        dimensionSliceTypeEClass.getESuperTypes().add(this.getDimensionSubsetType());
        dimensionTrimTypeEClass.getESuperTypes().add(this.getDimensionSubsetType());
        getCapabilitiesTypeEClass.getESuperTypes().add(theOws20Package.getGetCapabilitiesType());
        getCoverageTypeEClass.getESuperTypes().add(this.getRequestBaseType());

        // Initialize classes and features; add operations and parameters
        initEClass(capabilitiesTypeEClass, CapabilitiesType.class, "CapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCapabilitiesType_ServiceMetadata(), this.getServiceMetadataType(), null, "serviceMetadata", null, 0, 1, CapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCapabilitiesType_Contents(), this.getContentsType(), null, "contents", null, 0, 1, CapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(contentsTypeEClass, ContentsType.class, "ContentsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getContentsType_CoverageSummary(), this.getCoverageSummaryType(), null, "coverageSummary", null, 0, -1, ContentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getContentsType_Extension(), this.getExtensionType(), null, "extension", null, 0, 1, ContentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageDescriptionsTypeEClass, CoverageDescriptionsType.class, "CoverageDescriptionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCoverageDescriptionsType_CoverageDescription(), this.getCoverageDescriptionType(), null, "coverageDescription", null, 0, -1, CoverageDescriptionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageDescriptionTypeEClass, CoverageDescriptionType.class, "CoverageDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCoverageDescriptionType_CoverageId(), theXMLTypePackage.getNCName(), "coverageId", null, 1, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageDescriptionType_CoverageFunction(), this.getObject(), null, "coverageFunction", null, 0, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageDescriptionType_Metadata(), this.getObject(), null, "metadata", null, 0, -1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageDescriptionType_DomainSet(), this.getObject(), null, "domainSet", null, 1, 1, CoverageDescriptionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageDescriptionType_RangeType(), this.getObject(), null, "rangeType", null, 1, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageDescriptionType_ServiceParameters(), this.getServiceParametersType(), null, "serviceParameters", null, 1, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageOfferingsTypeEClass, CoverageOfferingsType.class, "CoverageOfferingsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCoverageOfferingsType_ServiceMetadata(), this.getServiceMetadataType(), null, "serviceMetadata", null, 1, 1, CoverageOfferingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageOfferingsType_OfferedCoverage(), this.getOfferedCoverageType(), null, "offeredCoverage", null, 0, -1, CoverageOfferingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageSubtypeParentTypeEClass, CoverageSubtypeParentType.class, "CoverageSubtypeParentType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCoverageSubtypeParentType_CoverageSubtype(), this.getQName(), "coverageSubtype", null, 1, 1, CoverageSubtypeParentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageSubtypeParentType_CoverageSubtypeParent(), this.getCoverageSubtypeParentType(), null, "coverageSubtypeParent", null, 0, 1, CoverageSubtypeParentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageSummaryTypeEClass, CoverageSummaryType.class, "CoverageSummaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCoverageSummaryType_WGS84BoundingBox(), theOws20Package.getWGS84BoundingBoxType(), null, "wGS84BoundingBox", null, 0, -1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_CoverageId(), theXMLTypePackage.getNCName(), "coverageId", null, 1, 1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_CoverageSubtype(), this.getQName(), "coverageSubtype", null, 1, 1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageSummaryType_CoverageSubtypeParent(), this.getCoverageSubtypeParentType(), null, "coverageSubtypeParent", null, 0, 1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_BoundingBoxGroup(), theEcorePackage.getEFeatureMapEntry(), "boundingBoxGroup", null, 0, -1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageSummaryType_BoundingBox(), theOws20Package.getBoundingBoxType(), null, "boundingBox", null, 0, -1, CoverageSummaryType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_MetadataGroup(), theEcorePackage.getEFeatureMapEntry(), "metadataGroup", null, 0, -1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageSummaryType_Metadata(), theOws20Package.getMetadataType(), null, "metadata", null, 0, -1, CoverageSummaryType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(describeCoverageTypeEClass, DescribeCoverageType.class, "DescribeCoverageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescribeCoverageType_CoverageId(), theXMLTypePackage.getNCName(), "coverageId", null, 1, 1, DescribeCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dimensionSliceTypeEClass, DimensionSliceType.class, "DimensionSliceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDimensionSliceType_SlicePoint(), theXMLTypePackage.getString(), "slicePoint", null, 1, 1, DimensionSliceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dimensionSubsetTypeEClass, DimensionSubsetType.class, "DimensionSubsetType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDimensionSubsetType_Dimension(), theXMLTypePackage.getNCName(), "dimension", null, 1, 1, DimensionSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dimensionTrimTypeEClass, DimensionTrimType.class, "DimensionTrimType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDimensionTrimType_TrimLow(), theXMLTypePackage.getString(), "trimLow", null, 0, 1, DimensionTrimType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDimensionTrimType_TrimHigh(), theXMLTypePackage.getString(), "trimHigh", null, 0, 1, DimensionTrimType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Capabilities(), this.getCapabilitiesType(), null, "capabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Contents(), this.getContentsType(), null, "contents", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CoverageDescription(), this.getCoverageDescriptionType(), null, "coverageDescription", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CoverageDescriptions(), this.getCoverageDescriptionsType(), null, "coverageDescriptions", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_CoverageId(), theXMLTypePackage.getNCName(), "coverageId", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CoverageOfferings(), this.getCoverageOfferingsType(), null, "coverageOfferings", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_CoverageSubtype(), this.getQName(), "coverageSubtype", null, 0, 1, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CoverageSubtypeParent(), this.getCoverageSubtypeParentType(), null, "coverageSubtypeParent", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CoverageSummary(), this.getCoverageSummaryType(), null, "coverageSummary", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DescribeCoverage(), this.getDescribeCoverageType(), null, "describeCoverage", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DimensionSlice(), this.getDimensionSliceType(), null, "dimensionSlice", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DimensionSubset(), this.getDimensionSubsetType(), null, "dimensionSubset", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DimensionTrim(), this.getDimensionTrimType(), null, "dimensionTrim", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Extension(), this.getExtensionType(), null, "extension", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCoverage(), this.getGetCoverageType(), null, "getCoverage", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_OfferedCoverage(), this.getOfferedCoverageType(), null, "offeredCoverage", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ServiceMetadata(), this.getServiceMetadataType(), null, "serviceMetadata", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ServiceParameters(), this.getServiceParametersType(), null, "serviceParameters", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(extensionTypeEClass, ExtensionType.class, "ExtensionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getExtensionType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, -1, ExtensionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetCapabilitiesType_Service(), theOws20Package.getServiceType(), "service", "WCS", 1, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCoverageTypeEClass, GetCoverageType.class, "GetCoverageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetCoverageType_CoverageId(), theXMLTypePackage.getNCName(), "coverageId", null, 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCoverageType_DimensionSubsetGroup(), theEcorePackage.getEFeatureMapEntry(), "dimensionSubsetGroup", null, 0, -1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetCoverageType_DimensionSubset(), this.getDimensionSubsetType(), null, "dimensionSubset", null, 0, -1, GetCoverageType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCoverageType_Format(), theXMLTypePackage.getAnyURI(), "format", null, 0, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCoverageType_MediaType(), theXMLTypePackage.getAnyURI(), "mediaType", null, 0, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(offeredCoverageTypeEClass, OfferedCoverageType.class, "OfferedCoverageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOfferedCoverageType_AbstractCoverage(), this.getObject(), null, "abstractCoverage", null, 1, 1, OfferedCoverageType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getOfferedCoverageType_ServiceParameters(), this.getServiceParametersType(), null, "serviceParameters", null, 1, 1, OfferedCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(requestBaseTypeEClass, RequestBaseType.class, "RequestBaseType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRequestBaseType_Extension(), this.getExtensionType(), null, "extension", null, 0, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequestBaseType_Service(), theXMLTypePackage.getString(), "service", "WCS", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequestBaseType_Version(), this.getVersionStringType_1(), "version", null, 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(serviceMetadataTypeEClass, ServiceMetadataType.class, "ServiceMetadataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getServiceMetadataType_FormatSupported(), theXMLTypePackage.getAnyURI(), "formatSupported", null, 1, 1, ServiceMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getServiceMetadataType_Extension(), this.getExtensionType(), null, "extension", null, 0, 1, ServiceMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(serviceParametersTypeEClass, ServiceParametersType.class, "ServiceParametersType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getServiceParametersType_CoverageSubtype(), this.getQName(), "coverageSubtype", null, 1, 1, ServiceParametersType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getServiceParametersType_CoverageSubtypeParent(), this.getCoverageSubtypeParentType(), null, "coverageSubtypeParent", null, 0, 1, ServiceParametersType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceParametersType_NativeFormat(), theXMLTypePackage.getAnyURI(), "nativeFormat", null, 1, 1, ServiceParametersType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getServiceParametersType_Extension(), this.getExtensionType(), null, "extension", null, 0, 1, ServiceParametersType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(objectEClass, Object.class, "Object", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

        // Initialize data types
        initEDataType(versionStringTypeEDataType, String.class, "VersionStringType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(versionStringType_1EDataType, String.class, "VersionStringType_1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(qNameEDataType, QName.class, "QName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // urn:x-ogc:specification:gml:schema-xsd:gml:3.2.1
        createUrnxogcspecificationgmlschemaxsdgml3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:dynamicFeature:3.2.1
        createUrnxogcspecificationgmlschemaxsddynamicFeature3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:topology:3.2.1
        createUrnxogcspecificationgmlschemaxsdtopology3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:coverage:3.2.1
        createUrnxogcspecificationgmlschemaxsdcoverage3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:coordinateReferenceSystems:3.2.1
        createUrnxogcspecificationgmlschemaxsdcoordinateReferenceSystems3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:observation:3.2.1
        createUrnxogcspecificationgmlschemaxsdobservation3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:temporalReferenceSystems:3.2.1
        createUrnxogcspecificationgmlschemaxsdtemporalReferenceSystems3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:deprecatedTypes:3.2.1
        createUrnxogcspecificationgmlschemaxsddeprecatedTypes3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:temporalTopology:3.2.1
        createUrnxogcspecificationgmlschemaxsdtemporalTopology3Annotations();
        // urn:opengis:specification:gml:schema-xsd:dictionary:v3.2.1
        createUrnopengisspecificationgmlschemaxsddictionaryv3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:gmlBase:3.2.1
        createUrnxogcspecificationgmlschemaxsdgmlBase3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:temporal:3.2.1
        createUrnxogcspecificationgmlschemaxsdtemporal3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:feature:3.2.1
        createUrnxogcspecificationgmlschemaxsdfeature3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:direction:3.2.1
        createUrnxogcspecificationgmlschemaxsddirection3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:valueObjects:3.2.1
        createUrnxogcspecificationgmlschemaxsdvalueObjects3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:geometryBasic0d1d:3.2.1
        createUrnxogcspecificationgmlschemaxsdgeometryBasic0d1d3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:coordinateSystems:3.2.1
        createUrnxogcspecificationgmlschemaxsdcoordinateSystems3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:datums:3.2.1
        createUrnxogcspecificationgmlschemaxsddatums3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:coordinateOperations:3.2.1
        createUrnxogcspecificationgmlschemaxsdcoordinateOperations3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:referenceSystems:3.2.1
        createUrnxogcspecificationgmlschemaxsdreferenceSystems3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:measures:3.2.1
        createUrnxogcspecificationgmlschemaxsdmeasures3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:units:3.2.1
        createUrnxogcspecificationgmlschemaxsdunits3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:grids:3.2.1
        createUrnxogcspecificationgmlschemaxsdgrids3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:geometryAggregates:3.2.1
        createUrnxogcspecificationgmlschemaxsdgeometryAggregates3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:geometryPrimitives:3.2.1
        createUrnxogcspecificationgmlschemaxsdgeometryPrimitives3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:geometryComplexes:3.2.1
        createUrnxogcspecificationgmlschemaxsdgeometryComplexes3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:basicTypes:3.2.1
        createUrnxogcspecificationgmlschemaxsdbasicTypes3Annotations();
        // urn:x-ogc:specification:gml:schema-xsd:geometryBasic2d:3.2.1
        createUrnxogcspecificationgmlschemaxsdgeometryBasic2d3Annotations();
        // http://www.w3.org/XML/1998/namespace
        createNamespaceAnnotations();
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:gml:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdgml3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:gml:3.2.1";		
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "gml.xsd"
           });																																																																																																																																																																									
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:dynamicFeature:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsddynamicFeature3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:dynamicFeature:3.2.1";			
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "dynamicFeature.xsd"
           });																																																																																																																																																																								
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:topology:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdtopology3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:topology:3.2.1";				
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "topology.xsd"
           });																																																																																																																																																																							
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:coverage:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdcoverage3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:coverage:3.2.1";					
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "coverage.xsd"
           });																																																																																																																																																																						
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:coordinateReferenceSystems:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdcoordinateReferenceSystems3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:coordinateReferenceSystems:3.2.1";						
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "coordinateReferenceSystems.xsd"
           });																																																																																																																																																																					
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:observation:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdobservation3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:observation:3.2.1";							
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "observation.xsd"
           });																																																																																																																																																																				
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:temporalReferenceSystems:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdtemporalReferenceSystems3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:temporalReferenceSystems:3.2.1";								
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "temporalReferenceSystems.xsd"
           });																																																																																																																																																																			
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:deprecatedTypes:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsddeprecatedTypes3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:deprecatedTypes:3.2.1";									
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "deprecatedTypes.xsd"
           });																																																																																																																																																																		
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:temporalTopology:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdtemporalTopology3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:temporalTopology:3.2.1";										
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "temporalTopology.xsd"
           });																																																																																																																																																																	
    }

    /**
     * Initializes the annotations for <b>urn:opengis:specification:gml:schema-xsd:dictionary:v3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnopengisspecificationgmlschemaxsddictionaryv3Annotations() {
        String source = "urn:opengis:specification:gml:schema-xsd:dictionary:v3.2.1";											
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "dictionary.xsd"
           });																																																																																																																																																																
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:gmlBase:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdgmlBase3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:gmlBase:3.2.1";												
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "gmlBase.xsd"
           });																																																																																																																																																															
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:temporal:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdtemporal3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:temporal:3.2.1";													
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "temporal.xsd"
           });																																																																																																																																																														
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:feature:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdfeature3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:feature:3.2.1";														
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "feature.xsd"
           });																																																																																																																																																													
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:direction:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsddirection3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:direction:3.2.1";															
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "direction.xsd"
           });																																																																																																																																																												
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:valueObjects:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdvalueObjects3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:valueObjects:3.2.1";																
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "valueObjects.xsd"
           });																																																																																																																																																											
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:geometryBasic0d1d:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdgeometryBasic0d1d3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:geometryBasic0d1d:3.2.1";																	
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "geometryBasic0d1d.xsd"
           });																																																																																																																																																										
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:coordinateSystems:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdcoordinateSystems3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:coordinateSystems:3.2.1";																		
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "coordinateSystems.xsd"
           });																																																																																																																																																									
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:datums:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsddatums3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:datums:3.2.1";																			
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "datums.xsd"
           });																																																																																																																																																								
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:coordinateOperations:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdcoordinateOperations3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:coordinateOperations:3.2.1";																				
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "coordinateOperations.xsd"
           });																																																																																																																																																							
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:referenceSystems:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdreferenceSystems3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:referenceSystems:3.2.1";																					
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "referenceSystems.xsd"
           });																																																																																																																																																						
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:measures:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdmeasures3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:measures:3.2.1";																						
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "measures.xsd"
           });																																																																																																																																																					
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:units:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdunits3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:units:3.2.1";																							
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "units.xsd"
           });																																																																																																																																																				
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:grids:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdgrids3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:grids:3.2.1";																								
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "grids.xsd"
           });																																																																																																																																																			
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:geometryAggregates:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdgeometryAggregates3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:geometryAggregates:3.2.1";																									
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "geometryAggregates.xsd"
           });																																																																																																																																																		
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:geometryPrimitives:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdgeometryPrimitives3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:geometryPrimitives:3.2.1";																										
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "geometryPrimitives.xsd"
           });																																																																																																																																																	
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:geometryComplexes:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdgeometryComplexes3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:geometryComplexes:3.2.1";																											
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "geometryComplexes.xsd"
           });																																																																																																																																																
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:basicTypes:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdbasicTypes3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:basicTypes:3.2.1";																												
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "basicTypes.xsd"
           });																																																																																																																																															
    }

    /**
     * Initializes the annotations for <b>urn:x-ogc:specification:gml:schema-xsd:geometryBasic2d:3.2.1</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnxogcspecificationgmlschemaxsdgeometryBasic2d3Annotations() {
        String source = "urn:x-ogc:specification:gml:schema-xsd:geometryBasic2d:3.2.1";																													
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "geometryBasic2d.xsd"
           });																																																																																																																																														
    }

    /**
     * Initializes the annotations for <b>http://www.w3.org/XML/1998/namespace</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createNamespaceAnnotations() {
        String source = "http://www.w3.org/XML/1998/namespace";																														
        addAnnotation
          (this, 
           source, 
           new String[] {
             "lang", "en"
           });																																																																																																																																													
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createExtendedMetaDataAnnotations() {
        String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";																																
        addAnnotation
          (capabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "CapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCapabilitiesType_ServiceMetadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceMetadata",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCapabilitiesType_Contents(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Contents",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (contentsTypeEClass, 
           source, 
           new String[] {
             "name", "ContentsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getContentsType_CoverageSummary(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSummary",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getContentsType_Extension(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Extension",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (coverageDescriptionsTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageDescriptionsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCoverageDescriptionsType_CoverageDescription(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageDescription",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (coverageDescriptionTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageDescriptionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCoverageDescriptionType_CoverageId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageId",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageDescriptionType_CoverageFunction(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "coverageFunction",
             "namespace", "http://www.opengis.net/gml/3.2"
           });			
        addAnnotation
          (getCoverageDescriptionType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "metadata",
             "namespace", "http://www.opengis.net/gmlcov/1.0"
           });			
        addAnnotation
          (getCoverageDescriptionType_DomainSet(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "domainSet",
             "namespace", "http://www.opengis.net/gml/3.2",
             "group", "http://www.opengis.net/gml/3.2#domainSet:group"
           });			
        addAnnotation
          (getCoverageDescriptionType_RangeType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "rangeType",
             "namespace", "http://www.opengis.net/gmlcov/1.0"
           });			
        addAnnotation
          (getCoverageDescriptionType_ServiceParameters(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceParameters",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (coverageOfferingsTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageOfferingsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCoverageOfferingsType_ServiceMetadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceMetadata",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageOfferingsType_OfferedCoverage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OfferedCoverage",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (coverageSubtypeParentTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageSubtypeParentType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCoverageSubtypeParentType_CoverageSubtype(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSubtype",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageSubtypeParentType_CoverageSubtypeParent(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSubtypeParent",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (coverageSummaryTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageSummaryType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCoverageSummaryType_WGS84BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WGS84BoundingBox",
             "namespace", "http://www.opengis.net/ows/2.0"
           });		
        addAnnotation
          (getCoverageSummaryType_CoverageId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageId",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageSummaryType_CoverageSubtype(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSubtype",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageSummaryType_CoverageSubtypeParent(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSubtypeParent",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageSummaryType_BoundingBoxGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "BoundingBox:group",
             "namespace", "http://www.opengis.net/ows/2.0"
           });		
        addAnnotation
          (getCoverageSummaryType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "http://www.opengis.net/ows/2.0",
             "group", "http://www.opengis.net/ows/2.0#BoundingBox:group"
           });		
        addAnnotation
          (getCoverageSummaryType_MetadataGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "Metadata:group",
             "namespace", "http://www.opengis.net/ows/2.0"
           });		
        addAnnotation
          (getCoverageSummaryType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows/2.0",
             "group", "http://www.opengis.net/ows/2.0#Metadata:group"
           });		
        addAnnotation
          (describeCoverageTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeCoverageType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDescribeCoverageType_CoverageId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageId",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (dimensionSliceTypeEClass, 
           source, 
           new String[] {
             "name", "DimensionSliceType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDimensionSliceType_SlicePoint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SlicePoint",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (dimensionSubsetTypeEClass, 
           source, 
           new String[] {
             "name", "DimensionSubsetType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDimensionSubsetType_Dimension(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Dimension",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (dimensionTrimTypeEClass, 
           source, 
           new String[] {
             "name", "DimensionTrimType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDimensionTrimType_TrimLow(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TrimLow",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDimensionTrimType_TrimHigh(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TrimHigh",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (documentRootEClass, 
           source, 
           new String[] {
             "name", "",
             "kind", "mixed"
           });		
        addAnnotation
          (getDocumentRoot_XMLNSPrefixMap(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "xmlns:prefix"
           });		
        addAnnotation
          (getDocumentRoot_XSISchemaLocation(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "xsi:schemaLocation"
           });		
        addAnnotation
          (getDocumentRoot_Capabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Capabilities",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Contents(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Contents",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_CoverageDescription(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageDescription",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/gml/3.2#AbstractFeature"
           });			
        addAnnotation
          (getDocumentRoot_CoverageDescriptions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageDescriptions",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_CoverageId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageId",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_CoverageOfferings(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageOfferings",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_CoverageSubtype(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSubtype",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_CoverageSubtypeParent(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSubtypeParent",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_CoverageSummary(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSummary",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_DescribeCoverage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DescribeCoverage",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_DimensionSlice(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DimensionSlice",
             "namespace", "##targetNamespace",
             "affiliation", "DimensionSubset"
           });			
        addAnnotation
          (getDocumentRoot_DimensionSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DimensionSubset",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_DimensionTrim(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DimensionTrim",
             "namespace", "##targetNamespace",
             "affiliation", "DimensionSubset"
           });			
        addAnnotation
          (getDocumentRoot_Extension(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Extension",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GetCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetCapabilities",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GetCoverage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetCoverage",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_OfferedCoverage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OfferedCoverage",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ServiceMetadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceMetadata",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ServiceParameters(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceParameters",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (extensionTypeEClass, 
           source, 
           new String[] {
             "name", "ExtensionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getExtensionType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":0",
             "processing", "lax"
           });		
        addAnnotation
          (getCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "GetCapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGetCapabilitiesType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });		
        addAnnotation
          (getCoverageTypeEClass, 
           source, 
           new String[] {
             "name", "GetCoverageType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGetCoverageType_CoverageId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageId",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCoverageType_DimensionSubsetGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "DimensionSubset:group",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCoverageType_DimensionSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DimensionSubset",
             "namespace", "##targetNamespace",
             "group", "DimensionSubset:group"
           });			
        addAnnotation
          (getGetCoverageType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "format",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCoverageType_MediaType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "mediaType",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (offeredCoverageTypeEClass, 
           source, 
           new String[] {
             "name", "OfferedCoverageType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getOfferedCoverageType_AbstractCoverage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractCoverage",
             "namespace", "http://www.opengis.net/gmlcov/1.0",
             "group", "http://www.opengis.net/gmlcov/1.0#AbstractCoverage:group"
           });			
        addAnnotation
          (getOfferedCoverageType_ServiceParameters(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceParameters",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (requestBaseTypeEClass, 
           source, 
           new String[] {
             "name", "RequestBaseType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getRequestBaseType_Extension(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Extension",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getRequestBaseType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });			
        addAnnotation
          (getRequestBaseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });			
        addAnnotation
          (serviceMetadataTypeEClass, 
           source, 
           new String[] {
             "name", "ServiceMetadataType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getServiceMetadataType_FormatSupported(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "formatSupported",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getServiceMetadataType_Extension(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Extension",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (serviceParametersTypeEClass, 
           source, 
           new String[] {
             "name", "ServiceParametersType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getServiceParametersType_CoverageSubtype(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSubtype",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceParametersType_CoverageSubtypeParent(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSubtypeParent",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceParametersType_NativeFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "nativeFormat",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getServiceParametersType_Extension(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Extension",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (versionStringTypeEDataType, 
           source, 
           new String[] {
             "name", "VersionStringType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "2\\.0\\.\\d+"
           });
    }

} //Wcs20PackageImpl
