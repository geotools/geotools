/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import com.vividsolutions.jts.geom.Envelope;
import java.util.Map;
import net.opengis.gml.GmlPackage;

import net.opengis.gml.impl.GmlPackageImpl;
import net.opengis.ows11.Ows11Package;
import net.opengis.wcs10.AbstractDescriptionBaseType;
import net.opengis.wcs10.AbstractDescriptionType;
import net.opengis.wcs10.AddressType;
import net.opengis.wcs10.AxisDescriptionType;
import net.opengis.wcs10.AxisDescriptionType1;
import net.opengis.wcs10.AxisSubsetType;
import net.opengis.wcs10.CapabilitiesSectionType;
import net.opengis.wcs10.ClosureType;
import net.opengis.wcs10.ContactType;
import net.opengis.wcs10.ContentMetadataType;
import net.opengis.wcs10.CoverageDescriptionType;
import net.opengis.wcs10.CoverageOfferingBriefType;
import net.opengis.wcs10.CoverageOfferingType;
import net.opengis.wcs10.DCPTypeType;
import net.opengis.wcs10.DescribeCoverageType;
import net.opengis.wcs10.DescribeCoverageType1;
import net.opengis.wcs10.DocumentRoot;
import net.opengis.wcs10.DomainSetType;
import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.ExceptionType;
import net.opengis.wcs10.GetCapabilitiesType;
import net.opengis.wcs10.GetCapabilitiesType1;
import net.opengis.wcs10.GetCoverageType;
import net.opengis.wcs10.GetCoverageType1;
import net.opengis.wcs10.GetType;
import net.opengis.wcs10.HTTPType;
import net.opengis.wcs10.InterpolationMethodType;
import net.opengis.wcs10.IntervalType;
import net.opengis.wcs10.KeywordsType;
import net.opengis.wcs10.LonLatEnvelopeBaseType;
import net.opengis.wcs10.LonLatEnvelopeType;
import net.opengis.wcs10.MetadataAssociationType;
import net.opengis.wcs10.MetadataLinkType;
import net.opengis.wcs10.MetadataTypeType;
import net.opengis.wcs10.OnlineResourceType;
import net.opengis.wcs10.OutputType;
import net.opengis.wcs10.PostType;
import net.opengis.wcs10.RangeSetType;
import net.opengis.wcs10.RangeSetType1;
import net.opengis.wcs10.RangeSubsetType;
import net.opengis.wcs10.RequestType;
import net.opengis.wcs10.ResponsiblePartyType;
import net.opengis.wcs10.ServiceType;
import net.opengis.wcs10.SpatialDomainType;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.SupportedCRSsType;
import net.opengis.wcs10.SupportedFormatsType;
import net.opengis.wcs10.SupportedInterpolationsType;
import net.opengis.wcs10.TelephoneType;
import net.opengis.wcs10.TimePeriodType;
import net.opengis.wcs10.TimeSequenceType;
import net.opengis.wcs10.TypedLiteralType;
import net.opengis.wcs10.ValueEnumBaseType;
import net.opengis.wcs10.ValueEnumType;
import net.opengis.wcs10.ValueRangeType;
import net.opengis.wcs10.ValuesType;
import net.opengis.wcs10.VendorSpecificCapabilitiesType;
import net.opengis.wcs10.WCSCapabilitiesType;
import net.opengis.wcs10.WCSCapabilityType;
import net.opengis.wcs10.Wcs10Factory;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.geotools.geometry.GeneralEnvelope;
import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wcs10PackageImpl extends EPackageImpl implements Wcs10Package {
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass abstractDescriptionBaseTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass abstractDescriptionTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass addressTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass axisDescriptionTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass axisDescriptionType1EClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass axisSubsetTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass contactTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass contentMetadataTypeEClass = null;

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
    private EClass coverageOfferingBriefTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass coverageOfferingTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass dcpTypeTypeEClass = null;

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
    private EClass describeCoverageType1EClass = null;

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
    private EClass domainSetTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass domainSubsetTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass exceptionTypeEClass = null;

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
    private EClass getCapabilitiesType1EClass = null;

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
    private EClass getCoverageType1EClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass getTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass httpTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass intervalTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass keywordsTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass lonLatEnvelopeBaseTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass lonLatEnvelopeTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass metadataAssociationTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass metadataLinkTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass onlineResourceTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass outputTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass postTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass rangeSetTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass rangeSetType1EClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass rangeSubsetTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass requestTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass responsiblePartyTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass serviceTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass spatialDomainTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass spatialSubsetTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass supportedCRSsTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass supportedFormatsTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass supportedInterpolationsTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass telephoneTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass timePeriodTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass timeSequenceTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass typedLiteralTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass valueEnumBaseTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass valueEnumTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass valueRangeTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass valuesTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass vendorSpecificCapabilitiesTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass wcsCapabilitiesTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass wcsCapabilityTypeEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass envelopeEClass = null;

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass generalEnvelopeEClass = null;

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum capabilitiesSectionTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum closureTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum interpolationMethodTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum metadataTypeTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EDataType capabilitiesSectionTypeObjectEDataType = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EDataType closureTypeObjectEDataType = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EDataType interpolationMethodTypeObjectEDataType = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EDataType metadataTypeTypeObjectEDataType = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType mapEDataType = null;

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
	 * @see net.opengis.wcs10.Wcs10Package#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private Wcs10PackageImpl() {
		super(eNS_URI, Wcs10Factory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link Wcs10Package#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static Wcs10Package init() {
		if (isInited) return (Wcs10Package)EPackage.Registry.INSTANCE.getEPackage(Wcs10Package.eNS_URI);

		// Obtain or create and register package
		Wcs10PackageImpl theWcs10Package = (Wcs10PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Wcs10PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Wcs10PackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XlinkPackage.eINSTANCE.eClass();
		Ows11Package.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		GmlPackageImpl theGmlPackage = (GmlPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(GmlPackage.eNS_URI) instanceof GmlPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(GmlPackage.eNS_URI) : GmlPackage.eINSTANCE);

		// Create package meta-data objects
		theWcs10Package.createPackageContents();
		theGmlPackage.createPackageContents();

		// Initialize created meta-data
		theWcs10Package.initializePackageContents();
		theGmlPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theWcs10Package.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Wcs10Package.eNS_URI, theWcs10Package);
		return theWcs10Package;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAbstractDescriptionBaseType() {
		return abstractDescriptionBaseTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAbstractDescriptionType() {
		return abstractDescriptionTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getAbstractDescriptionType_MetadataLink() {
		return (EReference)abstractDescriptionTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractDescriptionType_Description1() {
		return (EAttribute)abstractDescriptionTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractDescriptionType_Name1() {
		return (EAttribute)abstractDescriptionTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractDescriptionType_Label() {
		return (EAttribute)abstractDescriptionTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAddressType() {
		return addressTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressType_DeliveryPoint() {
		return (EAttribute)addressTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressType_City() {
		return (EAttribute)addressTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressType_AdministrativeArea() {
		return (EAttribute)addressTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressType_PostalCode() {
		return (EAttribute)addressTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressType_Country() {
		return (EAttribute)addressTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressType_ElectronicMailAddress() {
		return (EAttribute)addressTypeEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAxisDescriptionType() {
		return axisDescriptionTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getAxisDescriptionType_Values() {
		return (EReference)axisDescriptionTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAxisDescriptionType_RefSys() {
		return (EAttribute)axisDescriptionTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAxisDescriptionType_RefSysLabel() {
		return (EAttribute)axisDescriptionTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAxisDescriptionType_Semantic() {
		return (EAttribute)axisDescriptionTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAxisDescriptionType1() {
		return axisDescriptionType1EClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getAxisDescriptionType1_AxisDescription() {
		return (EReference)axisDescriptionType1EClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAxisSubsetType() {
		return axisSubsetTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAxisSubsetType_Name() {
		return (EAttribute)axisSubsetTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getContactType() {
		return contactTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getContactType_Phone() {
		return (EReference)contactTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getContactType_Address() {
		return (EReference)contactTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getContactType_OnlineResource() {
		return (EReference)contactTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getContentMetadataType() {
		return contentMetadataTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getContentMetadataType_CoverageOfferingBrief() {
		return (EReference)contentMetadataTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_Actuate() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_Arcrole() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_Href() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_RemoteSchema() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_Role() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_Show() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_Title() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_Type() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_UpdateSequence() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getContentMetadataType_Version() {
		return (EAttribute)contentMetadataTypeEClass.getEStructuralFeatures().get(10);
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
    public EReference getCoverageDescriptionType_CoverageOffering() {
		return (EReference)coverageDescriptionTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoverageDescriptionType_UpdateSequence() {
		return (EAttribute)coverageDescriptionTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoverageDescriptionType_Version() {
		return (EAttribute)coverageDescriptionTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCoverageOfferingBriefType() {
		return coverageOfferingBriefTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoverageOfferingBriefType_LonLatEnvelope() {
		return (EReference)coverageOfferingBriefTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoverageOfferingBriefType_Keywords() {
		return (EReference)coverageOfferingBriefTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCoverageOfferingType() {
		return coverageOfferingTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoverageOfferingType_DomainSet() {
		return (EReference)coverageOfferingTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoverageOfferingType_RangeSet() {
		return (EReference)coverageOfferingTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoverageOfferingType_SupportedCRSs() {
		return (EReference)coverageOfferingTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoverageOfferingType_SupportedFormats() {
		return (EReference)coverageOfferingTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoverageOfferingType_SupportedInterpolations() {
		return (EReference)coverageOfferingTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDCPTypeType() {
		return dcpTypeTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDCPTypeType_HTTP() {
		return (EReference)dcpTypeTypeEClass.getEStructuralFeatures().get(0);
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
    public EAttribute getDescribeCoverageType_Coverage() {
		return (EAttribute)describeCoverageTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDescribeCoverageType_Service() {
		return (EAttribute)describeCoverageTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDescribeCoverageType_Version() {
		return (EAttribute)describeCoverageTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDescribeCoverageType_BaseUrl() {
		return (EAttribute)describeCoverageTypeEClass.getEStructuralFeatures().get(3);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDescribeCoverageType_ExtendedProperties() {
		return (EAttribute)describeCoverageTypeEClass.getEStructuralFeatures().get(4);
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDescribeCoverageType1() {
		return describeCoverageType1EClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDescribeCoverageType1_DCPType() {
		return (EReference)describeCoverageType1EClass.getEStructuralFeatures().get(0);
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
    public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_AxisDescription() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_AxisDescription1() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_Capability() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_ContentMetadata() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_CoverageDescription() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_CoverageOffering() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_CoverageOfferingBrief() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_DescribeCoverage() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Description() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(11);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_DomainSet() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_Formats() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_GetCapabilities() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_GetCoverage() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_InterpolationMethod() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(16);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_Interval() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_Keywords() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_LonLatEnvelope() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_MetadataLink() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Name() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(21);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_RangeSet() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(22);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_RangeSet1() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(23);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_Service() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(24);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_SingleValue() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(25);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_SpatialDomain() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(26);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_SpatialSubset() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(27);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_SupportedCRSs() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(28);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_SupportedFormats() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(29);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_SupportedInterpolations() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(30);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_TemporalDomain() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(31);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_TemporalSubset() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(32);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_TimePeriod() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(33);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_TimeSequence() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(34);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDocumentRoot_WCSCapabilities() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(35);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Closure() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(36);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Semantic() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(37);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Type() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(38);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDomainSetType() {
		return domainSetTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDomainSetType_SpatialDomain() {
		return (EReference)domainSetTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDomainSetType_TemporalDomain() {
		return (EReference)domainSetTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDomainSetType_TemporalDomain1() {
		return (EReference)domainSetTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDomainSubsetType() {
		return domainSubsetTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDomainSubsetType_SpatialSubset() {
		return (EReference)domainSubsetTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDomainSubsetType_TemporalSubset() {
		return (EReference)domainSubsetTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDomainSubsetType_TemporalSubset1() {
		return (EReference)domainSubsetTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getExceptionType() {
		return exceptionTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getExceptionType_Format() {
		return (EAttribute)exceptionTypeEClass.getEStructuralFeatures().get(0);
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
    public EAttribute getGetCapabilitiesType_Section() {
		return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGetCapabilitiesType_Service() {
		return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGetCapabilitiesType_UpdateSequence() {
		return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGetCapabilitiesType_Version() {
		return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetCapabilitiesType_BaseUrl() {
		return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(4);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetCapabilitiesType_ExtendedProperties() {
		return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(5);
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getGetCapabilitiesType1() {
		return getCapabilitiesType1EClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getGetCapabilitiesType1_DCPType() {
		return (EReference)getCapabilitiesType1EClass.getEStructuralFeatures().get(0);
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
    public EAttribute getGetCoverageType_SourceCoverage() {
		return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getGetCoverageType_DomainSubset() {
		return (EReference)getCoverageTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getGetCoverageType_RangeSubset() {
		return (EReference)getCoverageTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGetCoverageType_InterpolationMethod() {
		return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getGetCoverageType_Output() {
		return (EReference)getCoverageTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGetCoverageType_Service() {
		return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGetCoverageType_Version() {
		return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetCoverageType_BaseUrl() {
		return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(7);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetCoverageType_ExtendedProperties() {
		return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(8);
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getGetCoverageType1() {
		return getCoverageType1EClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getGetCoverageType1_DCPType() {
		return (EReference)getCoverageType1EClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getGetType() {
		return getTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getGetType_OnlineResource() {
		return (EReference)getTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getHTTPType() {
		return httpTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getHTTPType_Group() {
		return (EAttribute)httpTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getHTTPType_Get() {
		return (EReference)httpTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getHTTPType_Post() {
		return (EReference)httpTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getIntervalType() {
		return intervalTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getIntervalType_Res() {
		return (EReference)intervalTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getKeywordsType() {
		return keywordsTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getKeywordsType_Keyword() {
		return (EAttribute)keywordsTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getKeywordsType_Type() {
		return (EReference)keywordsTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getLonLatEnvelopeBaseType() {
		return lonLatEnvelopeBaseTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getLonLatEnvelopeType() {
		return lonLatEnvelopeTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getLonLatEnvelopeType_TimePosition() {
		return (EReference)lonLatEnvelopeTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getMetadataAssociationType() {
		return metadataAssociationTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getMetadataLinkType() {
		return metadataLinkTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMetadataLinkType_MetadataType() {
		return (EAttribute)metadataLinkTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getOnlineResourceType() {
		return onlineResourceTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOnlineResourceType_Actuate() {
		return (EAttribute)onlineResourceTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOnlineResourceType_Arcrole() {
		return (EAttribute)onlineResourceTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOnlineResourceType_Href() {
		return (EAttribute)onlineResourceTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOnlineResourceType_Role() {
		return (EAttribute)onlineResourceTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOnlineResourceType_Show() {
		return (EAttribute)onlineResourceTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOnlineResourceType_Title() {
		return (EAttribute)onlineResourceTypeEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOnlineResourceType_Type() {
		return (EAttribute)onlineResourceTypeEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getOutputType() {
		return outputTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getOutputType_Crs() {
		return (EReference)outputTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getOutputType_Format() {
		return (EReference)outputTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getPostType() {
		return postTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getPostType_OnlineResource() {
		return (EReference)postTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRangeSetType() {
		return rangeSetTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRangeSetType_AxisDescription() {
		return (EReference)rangeSetTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRangeSetType_NullValues() {
		return (EReference)rangeSetTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getRangeSetType_RefSys() {
		return (EAttribute)rangeSetTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getRangeSetType_RefSysLabel() {
		return (EAttribute)rangeSetTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getRangeSetType_Semantic() {
		return (EAttribute)rangeSetTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRangeSetType1() {
		return rangeSetType1EClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRangeSetType1_RangeSet() {
		return (EReference)rangeSetType1EClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRangeSubsetType() {
		return rangeSubsetTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRangeSubsetType_AxisSubset() {
		return (EReference)rangeSubsetTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRequestType() {
		return requestTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRequestType_GetCapabilities() {
		return (EReference)requestTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRequestType_DescribeCoverage() {
		return (EReference)requestTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRequestType_GetCoverage() {
		return (EReference)requestTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getResponsiblePartyType() {
		return responsiblePartyTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getResponsiblePartyType_IndividualName() {
		return (EAttribute)responsiblePartyTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getResponsiblePartyType_OrganisationName() {
		return (EAttribute)responsiblePartyTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getResponsiblePartyType_OrganisationName1() {
		return (EAttribute)responsiblePartyTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getResponsiblePartyType_PositionName() {
		return (EAttribute)responsiblePartyTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getResponsiblePartyType_ContactInfo() {
		return (EReference)responsiblePartyTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getServiceType() {
		return serviceTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getServiceType_Keywords() {
		return (EReference)serviceTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getServiceType_ResponsibleParty() {
		return (EReference)serviceTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getServiceType_Fees() {
		return (EReference)serviceTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getServiceType_AccessConstraints() {
		return (EReference)serviceTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getServiceType_UpdateSequence() {
		return (EAttribute)serviceTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getServiceType_Version() {
		return (EAttribute)serviceTypeEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSpatialDomainType() {
		return spatialDomainTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSpatialDomainType_Envelope() {
		return (EReference)spatialDomainTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSpatialDomainType_GridGroup() {
		return (EAttribute)spatialDomainTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSpatialDomainType_Grid() {
		return (EReference)spatialDomainTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSpatialDomainType_Polygon() {
		return (EReference)spatialDomainTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSpatialSubsetType() {
		return spatialSubsetTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSupportedCRSsType() {
		return supportedCRSsTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSupportedCRSsType_RequestResponseCRSs() {
		return (EReference)supportedCRSsTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSupportedCRSsType_RequestCRSs() {
		return (EReference)supportedCRSsTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSupportedCRSsType_ResponseCRSs() {
		return (EReference)supportedCRSsTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSupportedCRSsType_NativeCRSs() {
		return (EReference)supportedCRSsTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSupportedFormatsType() {
		return supportedFormatsTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSupportedFormatsType_Formats() {
		return (EReference)supportedFormatsTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSupportedFormatsType_NativeFormat() {
		return (EAttribute)supportedFormatsTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSupportedInterpolationsType() {
		return supportedInterpolationsTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSupportedInterpolationsType_InterpolationMethod() {
		return (EAttribute)supportedInterpolationsTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSupportedInterpolationsType_Default() {
		return (EAttribute)supportedInterpolationsTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTelephoneType() {
		return telephoneTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTelephoneType_Voice() {
		return (EAttribute)telephoneTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTelephoneType_Facsimile() {
		return (EAttribute)telephoneTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTimePeriodType() {
		return timePeriodTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTimePeriodType_BeginPosition() {
		return (EReference)timePeriodTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTimePeriodType_EndPosition() {
		return (EReference)timePeriodTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTimePeriodType_TimeResolution() {
		return (EAttribute)timePeriodTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTimePeriodType_Frame() {
		return (EAttribute)timePeriodTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTimeSequenceType() {
		return timeSequenceTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTimeSequenceType_Group() {
		return (EAttribute)timeSequenceTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTimeSequenceType_TimePosition() {
		return (EReference)timeSequenceTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTimeSequenceType_TimePeriod() {
		return (EReference)timeSequenceTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTypedLiteralType() {
		return typedLiteralTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTypedLiteralType_Value() {
		return (EAttribute)typedLiteralTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTypedLiteralType_Type() {
		return (EAttribute)typedLiteralTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getValueEnumBaseType() {
		return valueEnumBaseTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getValueEnumBaseType_Group() {
		return (EAttribute)valueEnumBaseTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getValueEnumBaseType_Interval() {
		return (EReference)valueEnumBaseTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getValueEnumBaseType_SingleValue() {
		return (EReference)valueEnumBaseTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getValueEnumType() {
		return valueEnumTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getValueEnumType_Semantic() {
		return (EAttribute)valueEnumTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getValueEnumType_Type() {
		return (EAttribute)valueEnumTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getValueRangeType() {
		return valueRangeTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getValueRangeType_Min() {
		return (EReference)valueRangeTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getValueRangeType_Max() {
		return (EReference)valueRangeTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getValueRangeType_Atomic() {
		return (EAttribute)valueRangeTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getValueRangeType_Closure() {
		return (EAttribute)valueRangeTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getValueRangeType_Semantic() {
		return (EAttribute)valueRangeTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getValueRangeType_Type() {
		return (EAttribute)valueRangeTypeEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getValuesType() {
		return valuesTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getValuesType_Default() {
		return (EReference)valuesTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getVendorSpecificCapabilitiesType() {
		return vendorSpecificCapabilitiesTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getVendorSpecificCapabilitiesType_Any() {
		return (EAttribute)vendorSpecificCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getWCSCapabilitiesType() {
		return wcsCapabilitiesTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getWCSCapabilitiesType_Service() {
		return (EReference)wcsCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getWCSCapabilitiesType_Capability() {
		return (EReference)wcsCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getWCSCapabilitiesType_ContentMetadata() {
		return (EReference)wcsCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getWCSCapabilitiesType_UpdateSequence() {
		return (EAttribute)wcsCapabilitiesTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getWCSCapabilitiesType_Version() {
		return (EAttribute)wcsCapabilitiesTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getWCSCapabilityType() {
		return wcsCapabilityTypeEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getWCSCapabilityType_Request() {
		return (EReference)wcsCapabilityTypeEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getWCSCapabilityType_Exception() {
		return (EReference)wcsCapabilityTypeEClass.getEStructuralFeatures().get(1);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getWCSCapabilityType_VendorSpecificCapabilities() {
		return (EReference)wcsCapabilityTypeEClass.getEStructuralFeatures().get(2);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getWCSCapabilityType_UpdateSequence() {
		return (EAttribute)wcsCapabilityTypeEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getWCSCapabilityType_Version() {
		return (EAttribute)wcsCapabilityTypeEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnvelope() {
		return envelopeEClass;
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeneralEnvelope() {
		return generalEnvelopeEClass;
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getCapabilitiesSectionType() {
		return capabilitiesSectionTypeEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getClosureType() {
		return closureTypeEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getInterpolationMethodType() {
		return interpolationMethodTypeEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getMetadataTypeType() {
		return metadataTypeTypeEEnum;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EDataType getCapabilitiesSectionTypeObject() {
		return capabilitiesSectionTypeObjectEDataType;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EDataType getClosureTypeObject() {
		return closureTypeObjectEDataType;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EDataType getInterpolationMethodTypeObject() {
		return interpolationMethodTypeObjectEDataType;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EDataType getMetadataTypeTypeObject() {
		return metadataTypeTypeObjectEDataType;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getMap() {
		return mapEDataType;
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Wcs10Factory getWcs10Factory() {
		return (Wcs10Factory)getEFactoryInstance();
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
		abstractDescriptionBaseTypeEClass = createEClass(ABSTRACT_DESCRIPTION_BASE_TYPE);

		abstractDescriptionTypeEClass = createEClass(ABSTRACT_DESCRIPTION_TYPE);
		createEReference(abstractDescriptionTypeEClass, ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK);
		createEAttribute(abstractDescriptionTypeEClass, ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1);
		createEAttribute(abstractDescriptionTypeEClass, ABSTRACT_DESCRIPTION_TYPE__NAME1);
		createEAttribute(abstractDescriptionTypeEClass, ABSTRACT_DESCRIPTION_TYPE__LABEL);

		addressTypeEClass = createEClass(ADDRESS_TYPE);
		createEAttribute(addressTypeEClass, ADDRESS_TYPE__DELIVERY_POINT);
		createEAttribute(addressTypeEClass, ADDRESS_TYPE__CITY);
		createEAttribute(addressTypeEClass, ADDRESS_TYPE__ADMINISTRATIVE_AREA);
		createEAttribute(addressTypeEClass, ADDRESS_TYPE__POSTAL_CODE);
		createEAttribute(addressTypeEClass, ADDRESS_TYPE__COUNTRY);
		createEAttribute(addressTypeEClass, ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS);

		axisDescriptionTypeEClass = createEClass(AXIS_DESCRIPTION_TYPE);
		createEReference(axisDescriptionTypeEClass, AXIS_DESCRIPTION_TYPE__VALUES);
		createEAttribute(axisDescriptionTypeEClass, AXIS_DESCRIPTION_TYPE__REF_SYS);
		createEAttribute(axisDescriptionTypeEClass, AXIS_DESCRIPTION_TYPE__REF_SYS_LABEL);
		createEAttribute(axisDescriptionTypeEClass, AXIS_DESCRIPTION_TYPE__SEMANTIC);

		axisDescriptionType1EClass = createEClass(AXIS_DESCRIPTION_TYPE1);
		createEReference(axisDescriptionType1EClass, AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION);

		axisSubsetTypeEClass = createEClass(AXIS_SUBSET_TYPE);
		createEAttribute(axisSubsetTypeEClass, AXIS_SUBSET_TYPE__NAME);

		contactTypeEClass = createEClass(CONTACT_TYPE);
		createEReference(contactTypeEClass, CONTACT_TYPE__PHONE);
		createEReference(contactTypeEClass, CONTACT_TYPE__ADDRESS);
		createEReference(contactTypeEClass, CONTACT_TYPE__ONLINE_RESOURCE);

		contentMetadataTypeEClass = createEClass(CONTENT_METADATA_TYPE);
		createEReference(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__ACTUATE);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__ARCROLE);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__HREF);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__REMOTE_SCHEMA);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__ROLE);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__SHOW);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__TITLE);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__TYPE);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__UPDATE_SEQUENCE);
		createEAttribute(contentMetadataTypeEClass, CONTENT_METADATA_TYPE__VERSION);

		coverageDescriptionTypeEClass = createEClass(COVERAGE_DESCRIPTION_TYPE);
		createEReference(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__COVERAGE_OFFERING);
		createEAttribute(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__UPDATE_SEQUENCE);
		createEAttribute(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__VERSION);

		coverageOfferingBriefTypeEClass = createEClass(COVERAGE_OFFERING_BRIEF_TYPE);
		createEReference(coverageOfferingBriefTypeEClass, COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE);
		createEReference(coverageOfferingBriefTypeEClass, COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS);

		coverageOfferingTypeEClass = createEClass(COVERAGE_OFFERING_TYPE);
		createEReference(coverageOfferingTypeEClass, COVERAGE_OFFERING_TYPE__DOMAIN_SET);
		createEReference(coverageOfferingTypeEClass, COVERAGE_OFFERING_TYPE__RANGE_SET);
		createEReference(coverageOfferingTypeEClass, COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS);
		createEReference(coverageOfferingTypeEClass, COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS);
		createEReference(coverageOfferingTypeEClass, COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS);

		dcpTypeTypeEClass = createEClass(DCP_TYPE_TYPE);
		createEReference(dcpTypeTypeEClass, DCP_TYPE_TYPE__HTTP);

		describeCoverageTypeEClass = createEClass(DESCRIBE_COVERAGE_TYPE);
		createEAttribute(describeCoverageTypeEClass, DESCRIBE_COVERAGE_TYPE__COVERAGE);
		createEAttribute(describeCoverageTypeEClass, DESCRIBE_COVERAGE_TYPE__SERVICE);
		createEAttribute(describeCoverageTypeEClass, DESCRIBE_COVERAGE_TYPE__VERSION);
		createEAttribute(describeCoverageTypeEClass, DESCRIBE_COVERAGE_TYPE__BASE_URL);
		createEAttribute(describeCoverageTypeEClass, DESCRIBE_COVERAGE_TYPE__EXTENDED_PROPERTIES);

		describeCoverageType1EClass = createEClass(DESCRIBE_COVERAGE_TYPE1);
		createEReference(describeCoverageType1EClass, DESCRIBE_COVERAGE_TYPE1__DCP_TYPE);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__AXIS_DESCRIPTION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__AXIS_DESCRIPTION1);
		createEReference(documentRootEClass, DOCUMENT_ROOT__CAPABILITY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__CONTENT_METADATA);
		createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_DESCRIPTION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_OFFERING);
		createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_COVERAGE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__DESCRIPTION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DOMAIN_SET);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FORMATS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GET_COVERAGE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__INTERPOLATION_METHOD);
		createEReference(documentRootEClass, DOCUMENT_ROOT__INTERVAL);
		createEReference(documentRootEClass, DOCUMENT_ROOT__KEYWORDS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__LON_LAT_ENVELOPE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__METADATA_LINK);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__NAME);
		createEReference(documentRootEClass, DOCUMENT_ROOT__RANGE_SET);
		createEReference(documentRootEClass, DOCUMENT_ROOT__RANGE_SET1);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SERVICE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SINGLE_VALUE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SPATIAL_DOMAIN);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SPATIAL_SUBSET);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SUPPORTED_CR_SS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SUPPORTED_FORMATS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TEMPORAL_DOMAIN);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TEMPORAL_SUBSET);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TIME_PERIOD);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TIME_SEQUENCE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__WCS_CAPABILITIES);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__CLOSURE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__SEMANTIC);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__TYPE);

		domainSetTypeEClass = createEClass(DOMAIN_SET_TYPE);
		createEReference(domainSetTypeEClass, DOMAIN_SET_TYPE__SPATIAL_DOMAIN);
		createEReference(domainSetTypeEClass, DOMAIN_SET_TYPE__TEMPORAL_DOMAIN);
		createEReference(domainSetTypeEClass, DOMAIN_SET_TYPE__TEMPORAL_DOMAIN1);

		domainSubsetTypeEClass = createEClass(DOMAIN_SUBSET_TYPE);
		createEReference(domainSubsetTypeEClass, DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET);
		createEReference(domainSubsetTypeEClass, DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET);
		createEReference(domainSubsetTypeEClass, DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1);

		exceptionTypeEClass = createEClass(EXCEPTION_TYPE);
		createEAttribute(exceptionTypeEClass, EXCEPTION_TYPE__FORMAT);

		getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SECTION);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__VERSION);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__BASE_URL);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES);

		getCapabilitiesType1EClass = createEClass(GET_CAPABILITIES_TYPE1);
		createEReference(getCapabilitiesType1EClass, GET_CAPABILITIES_TYPE1__DCP_TYPE);

		getCoverageTypeEClass = createEClass(GET_COVERAGE_TYPE);
		createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__SOURCE_COVERAGE);
		createEReference(getCoverageTypeEClass, GET_COVERAGE_TYPE__DOMAIN_SUBSET);
		createEReference(getCoverageTypeEClass, GET_COVERAGE_TYPE__RANGE_SUBSET);
		createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__INTERPOLATION_METHOD);
		createEReference(getCoverageTypeEClass, GET_COVERAGE_TYPE__OUTPUT);
		createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__SERVICE);
		createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__VERSION);
		createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__BASE_URL);
		createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__EXTENDED_PROPERTIES);

		getCoverageType1EClass = createEClass(GET_COVERAGE_TYPE1);
		createEReference(getCoverageType1EClass, GET_COVERAGE_TYPE1__DCP_TYPE);

		getTypeEClass = createEClass(GET_TYPE);
		createEReference(getTypeEClass, GET_TYPE__ONLINE_RESOURCE);

		httpTypeEClass = createEClass(HTTP_TYPE);
		createEAttribute(httpTypeEClass, HTTP_TYPE__GROUP);
		createEReference(httpTypeEClass, HTTP_TYPE__GET);
		createEReference(httpTypeEClass, HTTP_TYPE__POST);

		intervalTypeEClass = createEClass(INTERVAL_TYPE);
		createEReference(intervalTypeEClass, INTERVAL_TYPE__RES);

		keywordsTypeEClass = createEClass(KEYWORDS_TYPE);
		createEAttribute(keywordsTypeEClass, KEYWORDS_TYPE__KEYWORD);
		createEReference(keywordsTypeEClass, KEYWORDS_TYPE__TYPE);

		lonLatEnvelopeBaseTypeEClass = createEClass(LON_LAT_ENVELOPE_BASE_TYPE);

		lonLatEnvelopeTypeEClass = createEClass(LON_LAT_ENVELOPE_TYPE);
		createEReference(lonLatEnvelopeTypeEClass, LON_LAT_ENVELOPE_TYPE__TIME_POSITION);

		metadataAssociationTypeEClass = createEClass(METADATA_ASSOCIATION_TYPE);

		metadataLinkTypeEClass = createEClass(METADATA_LINK_TYPE);
		createEAttribute(metadataLinkTypeEClass, METADATA_LINK_TYPE__METADATA_TYPE);

		onlineResourceTypeEClass = createEClass(ONLINE_RESOURCE_TYPE);
		createEAttribute(onlineResourceTypeEClass, ONLINE_RESOURCE_TYPE__ACTUATE);
		createEAttribute(onlineResourceTypeEClass, ONLINE_RESOURCE_TYPE__ARCROLE);
		createEAttribute(onlineResourceTypeEClass, ONLINE_RESOURCE_TYPE__HREF);
		createEAttribute(onlineResourceTypeEClass, ONLINE_RESOURCE_TYPE__ROLE);
		createEAttribute(onlineResourceTypeEClass, ONLINE_RESOURCE_TYPE__SHOW);
		createEAttribute(onlineResourceTypeEClass, ONLINE_RESOURCE_TYPE__TITLE);
		createEAttribute(onlineResourceTypeEClass, ONLINE_RESOURCE_TYPE__TYPE);

		outputTypeEClass = createEClass(OUTPUT_TYPE);
		createEReference(outputTypeEClass, OUTPUT_TYPE__CRS);
		createEReference(outputTypeEClass, OUTPUT_TYPE__FORMAT);

		postTypeEClass = createEClass(POST_TYPE);
		createEReference(postTypeEClass, POST_TYPE__ONLINE_RESOURCE);

		rangeSetTypeEClass = createEClass(RANGE_SET_TYPE);
		createEReference(rangeSetTypeEClass, RANGE_SET_TYPE__AXIS_DESCRIPTION);
		createEReference(rangeSetTypeEClass, RANGE_SET_TYPE__NULL_VALUES);
		createEAttribute(rangeSetTypeEClass, RANGE_SET_TYPE__REF_SYS);
		createEAttribute(rangeSetTypeEClass, RANGE_SET_TYPE__REF_SYS_LABEL);
		createEAttribute(rangeSetTypeEClass, RANGE_SET_TYPE__SEMANTIC);

		rangeSetType1EClass = createEClass(RANGE_SET_TYPE1);
		createEReference(rangeSetType1EClass, RANGE_SET_TYPE1__RANGE_SET);

		rangeSubsetTypeEClass = createEClass(RANGE_SUBSET_TYPE);
		createEReference(rangeSubsetTypeEClass, RANGE_SUBSET_TYPE__AXIS_SUBSET);

		requestTypeEClass = createEClass(REQUEST_TYPE);
		createEReference(requestTypeEClass, REQUEST_TYPE__GET_CAPABILITIES);
		createEReference(requestTypeEClass, REQUEST_TYPE__DESCRIBE_COVERAGE);
		createEReference(requestTypeEClass, REQUEST_TYPE__GET_COVERAGE);

		responsiblePartyTypeEClass = createEClass(RESPONSIBLE_PARTY_TYPE);
		createEAttribute(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME);
		createEAttribute(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME);
		createEAttribute(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME1);
		createEAttribute(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__POSITION_NAME);
		createEReference(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__CONTACT_INFO);

		serviceTypeEClass = createEClass(SERVICE_TYPE);
		createEReference(serviceTypeEClass, SERVICE_TYPE__KEYWORDS);
		createEReference(serviceTypeEClass, SERVICE_TYPE__RESPONSIBLE_PARTY);
		createEReference(serviceTypeEClass, SERVICE_TYPE__FEES);
		createEReference(serviceTypeEClass, SERVICE_TYPE__ACCESS_CONSTRAINTS);
		createEAttribute(serviceTypeEClass, SERVICE_TYPE__UPDATE_SEQUENCE);
		createEAttribute(serviceTypeEClass, SERVICE_TYPE__VERSION);

		spatialDomainTypeEClass = createEClass(SPATIAL_DOMAIN_TYPE);
		createEReference(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__ENVELOPE);
		createEAttribute(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__GRID_GROUP);
		createEReference(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__GRID);
		createEReference(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__POLYGON);

		spatialSubsetTypeEClass = createEClass(SPATIAL_SUBSET_TYPE);

		supportedCRSsTypeEClass = createEClass(SUPPORTED_CR_SS_TYPE);
		createEReference(supportedCRSsTypeEClass, SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS);
		createEReference(supportedCRSsTypeEClass, SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS);
		createEReference(supportedCRSsTypeEClass, SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS);
		createEReference(supportedCRSsTypeEClass, SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS);

		supportedFormatsTypeEClass = createEClass(SUPPORTED_FORMATS_TYPE);
		createEReference(supportedFormatsTypeEClass, SUPPORTED_FORMATS_TYPE__FORMATS);
		createEAttribute(supportedFormatsTypeEClass, SUPPORTED_FORMATS_TYPE__NATIVE_FORMAT);

		supportedInterpolationsTypeEClass = createEClass(SUPPORTED_INTERPOLATIONS_TYPE);
		createEAttribute(supportedInterpolationsTypeEClass, SUPPORTED_INTERPOLATIONS_TYPE__INTERPOLATION_METHOD);
		createEAttribute(supportedInterpolationsTypeEClass, SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT);

		telephoneTypeEClass = createEClass(TELEPHONE_TYPE);
		createEAttribute(telephoneTypeEClass, TELEPHONE_TYPE__VOICE);
		createEAttribute(telephoneTypeEClass, TELEPHONE_TYPE__FACSIMILE);

		timePeriodTypeEClass = createEClass(TIME_PERIOD_TYPE);
		createEReference(timePeriodTypeEClass, TIME_PERIOD_TYPE__BEGIN_POSITION);
		createEReference(timePeriodTypeEClass, TIME_PERIOD_TYPE__END_POSITION);
		createEAttribute(timePeriodTypeEClass, TIME_PERIOD_TYPE__TIME_RESOLUTION);
		createEAttribute(timePeriodTypeEClass, TIME_PERIOD_TYPE__FRAME);

		timeSequenceTypeEClass = createEClass(TIME_SEQUENCE_TYPE);
		createEAttribute(timeSequenceTypeEClass, TIME_SEQUENCE_TYPE__GROUP);
		createEReference(timeSequenceTypeEClass, TIME_SEQUENCE_TYPE__TIME_POSITION);
		createEReference(timeSequenceTypeEClass, TIME_SEQUENCE_TYPE__TIME_PERIOD);

		typedLiteralTypeEClass = createEClass(TYPED_LITERAL_TYPE);
		createEAttribute(typedLiteralTypeEClass, TYPED_LITERAL_TYPE__VALUE);
		createEAttribute(typedLiteralTypeEClass, TYPED_LITERAL_TYPE__TYPE);

		valueEnumBaseTypeEClass = createEClass(VALUE_ENUM_BASE_TYPE);
		createEAttribute(valueEnumBaseTypeEClass, VALUE_ENUM_BASE_TYPE__GROUP);
		createEReference(valueEnumBaseTypeEClass, VALUE_ENUM_BASE_TYPE__INTERVAL);
		createEReference(valueEnumBaseTypeEClass, VALUE_ENUM_BASE_TYPE__SINGLE_VALUE);

		valueEnumTypeEClass = createEClass(VALUE_ENUM_TYPE);
		createEAttribute(valueEnumTypeEClass, VALUE_ENUM_TYPE__SEMANTIC);
		createEAttribute(valueEnumTypeEClass, VALUE_ENUM_TYPE__TYPE);

		valueRangeTypeEClass = createEClass(VALUE_RANGE_TYPE);
		createEReference(valueRangeTypeEClass, VALUE_RANGE_TYPE__MIN);
		createEReference(valueRangeTypeEClass, VALUE_RANGE_TYPE__MAX);
		createEAttribute(valueRangeTypeEClass, VALUE_RANGE_TYPE__ATOMIC);
		createEAttribute(valueRangeTypeEClass, VALUE_RANGE_TYPE__CLOSURE);
		createEAttribute(valueRangeTypeEClass, VALUE_RANGE_TYPE__SEMANTIC);
		createEAttribute(valueRangeTypeEClass, VALUE_RANGE_TYPE__TYPE);

		valuesTypeEClass = createEClass(VALUES_TYPE);
		createEReference(valuesTypeEClass, VALUES_TYPE__DEFAULT);

		vendorSpecificCapabilitiesTypeEClass = createEClass(VENDOR_SPECIFIC_CAPABILITIES_TYPE);
		createEAttribute(vendorSpecificCapabilitiesTypeEClass, VENDOR_SPECIFIC_CAPABILITIES_TYPE__ANY);

		wcsCapabilitiesTypeEClass = createEClass(WCS_CAPABILITIES_TYPE);
		createEReference(wcsCapabilitiesTypeEClass, WCS_CAPABILITIES_TYPE__SERVICE);
		createEReference(wcsCapabilitiesTypeEClass, WCS_CAPABILITIES_TYPE__CAPABILITY);
		createEReference(wcsCapabilitiesTypeEClass, WCS_CAPABILITIES_TYPE__CONTENT_METADATA);
		createEAttribute(wcsCapabilitiesTypeEClass, WCS_CAPABILITIES_TYPE__UPDATE_SEQUENCE);
		createEAttribute(wcsCapabilitiesTypeEClass, WCS_CAPABILITIES_TYPE__VERSION);

		wcsCapabilityTypeEClass = createEClass(WCS_CAPABILITY_TYPE);
		createEReference(wcsCapabilityTypeEClass, WCS_CAPABILITY_TYPE__REQUEST);
		createEReference(wcsCapabilityTypeEClass, WCS_CAPABILITY_TYPE__EXCEPTION);
		createEReference(wcsCapabilityTypeEClass, WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES);
		createEAttribute(wcsCapabilityTypeEClass, WCS_CAPABILITY_TYPE__UPDATE_SEQUENCE);
		createEAttribute(wcsCapabilityTypeEClass, WCS_CAPABILITY_TYPE__VERSION);

		envelopeEClass = createEClass(ENVELOPE);

		generalEnvelopeEClass = createEClass(GENERAL_ENVELOPE);

		// Create enums
		capabilitiesSectionTypeEEnum = createEEnum(CAPABILITIES_SECTION_TYPE);
		closureTypeEEnum = createEEnum(CLOSURE_TYPE);
		interpolationMethodTypeEEnum = createEEnum(INTERPOLATION_METHOD_TYPE);
		metadataTypeTypeEEnum = createEEnum(METADATA_TYPE_TYPE);

		// Create data types
		capabilitiesSectionTypeObjectEDataType = createEDataType(CAPABILITIES_SECTION_TYPE_OBJECT);
		closureTypeObjectEDataType = createEDataType(CLOSURE_TYPE_OBJECT);
		interpolationMethodTypeObjectEDataType = createEDataType(INTERPOLATION_METHOD_TYPE_OBJECT);
		metadataTypeTypeObjectEDataType = createEDataType(METADATA_TYPE_TYPE_OBJECT);
		mapEDataType = createEDataType(MAP);
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
		GmlPackage theGmlPackage = (GmlPackage)EPackage.Registry.INSTANCE.getEPackage(GmlPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
		XlinkPackage theXlinkPackage = (XlinkPackage)EPackage.Registry.INSTANCE.getEPackage(XlinkPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Add supertypes to classes
		abstractDescriptionBaseTypeEClass.getESuperTypes().add(theGmlPackage.getAbstractGMLType());
		abstractDescriptionTypeEClass.getESuperTypes().add(this.getAbstractDescriptionBaseType());
		axisDescriptionTypeEClass.getESuperTypes().add(this.getAbstractDescriptionType());
		axisSubsetTypeEClass.getESuperTypes().add(this.getValueEnumBaseType());
		coverageOfferingBriefTypeEClass.getESuperTypes().add(this.getAbstractDescriptionType());
		coverageOfferingTypeEClass.getESuperTypes().add(this.getCoverageOfferingBriefType());
		intervalTypeEClass.getESuperTypes().add(this.getValueRangeType());
		lonLatEnvelopeBaseTypeEClass.getESuperTypes().add(theGmlPackage.getEnvelopeType());
		lonLatEnvelopeTypeEClass.getESuperTypes().add(this.getLonLatEnvelopeBaseType());
		metadataAssociationTypeEClass.getESuperTypes().add(theGmlPackage.getMetaDataPropertyType());
		metadataLinkTypeEClass.getESuperTypes().add(this.getMetadataAssociationType());
		rangeSetTypeEClass.getESuperTypes().add(this.getAbstractDescriptionType());
		serviceTypeEClass.getESuperTypes().add(this.getAbstractDescriptionType());
		spatialSubsetTypeEClass.getESuperTypes().add(this.getSpatialDomainType());
		valueEnumTypeEClass.getESuperTypes().add(this.getValueEnumBaseType());
		valuesTypeEClass.getESuperTypes().add(this.getValueEnumType());

		// Initialize classes and features; add operations and parameters
		initEClass(abstractDescriptionBaseTypeEClass, AbstractDescriptionBaseType.class, "AbstractDescriptionBaseType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(abstractDescriptionTypeEClass, AbstractDescriptionType.class, "AbstractDescriptionType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAbstractDescriptionType_MetadataLink(), this.getMetadataLinkType(), null, "metadataLink", null, 0, -1, AbstractDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractDescriptionType_Description1(), theXMLTypePackage.getString(), "description1", null, 0, 1, AbstractDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractDescriptionType_Name1(), theXMLTypePackage.getString(), "name1", null, 1, 1, AbstractDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractDescriptionType_Label(), theXMLTypePackage.getString(), "label", null, 1, 1, AbstractDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(addressTypeEClass, AddressType.class, "AddressType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAddressType_DeliveryPoint(), theXMLTypePackage.getString(), "deliveryPoint", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAddressType_City(), theXMLTypePackage.getString(), "city", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAddressType_AdministrativeArea(), theXMLTypePackage.getString(), "administrativeArea", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAddressType_PostalCode(), theXMLTypePackage.getString(), "postalCode", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAddressType_Country(), theXMLTypePackage.getString(), "country", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAddressType_ElectronicMailAddress(), theXMLTypePackage.getString(), "electronicMailAddress", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(axisDescriptionTypeEClass, AxisDescriptionType.class, "AxisDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAxisDescriptionType_Values(), this.getValuesType(), null, "values", null, 1, 1, AxisDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAxisDescriptionType_RefSys(), theXMLTypePackage.getAnyURI(), "refSys", null, 0, 1, AxisDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAxisDescriptionType_RefSysLabel(), theXMLTypePackage.getString(), "refSysLabel", null, 0, 1, AxisDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAxisDescriptionType_Semantic(), theXMLTypePackage.getAnyURI(), "semantic", null, 0, 1, AxisDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(axisDescriptionType1EClass, AxisDescriptionType1.class, "AxisDescriptionType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAxisDescriptionType1_AxisDescription(), this.getAxisDescriptionType(), null, "axisDescription", null, 1, 1, AxisDescriptionType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(axisSubsetTypeEClass, AxisSubsetType.class, "AxisSubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAxisSubsetType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, AxisSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contactTypeEClass, ContactType.class, "ContactType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContactType_Phone(), this.getTelephoneType(), null, "phone", null, 0, 1, ContactType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContactType_Address(), this.getAddressType(), null, "address", null, 0, 1, ContactType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContactType_OnlineResource(), this.getOnlineResourceType(), null, "onlineResource", null, 0, 1, ContactType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contentMetadataTypeEClass, ContentMetadataType.class, "ContentMetadataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContentMetadataType_CoverageOfferingBrief(), this.getCoverageOfferingBriefType(), null, "coverageOfferingBrief", null, 0, -1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_Actuate(), theXlinkPackage.getActuateType(), "actuate", "onLoad", 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_RemoteSchema(), theXMLTypePackage.getAnyURI(), "remoteSchema", null, 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_Show(), theXlinkPackage.getShowType(), "show", "new", 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_UpdateSequence(), theXMLTypePackage.getString(), "updateSequence", null, 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContentMetadataType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 0, 1, ContentMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(coverageDescriptionTypeEClass, CoverageDescriptionType.class, "CoverageDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCoverageDescriptionType_CoverageOffering(), this.getCoverageOfferingType(), null, "coverageOffering", null, 1, -1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCoverageDescriptionType_UpdateSequence(), theXMLTypePackage.getString(), "updateSequence", null, 0, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCoverageDescriptionType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(coverageOfferingBriefTypeEClass, CoverageOfferingBriefType.class, "CoverageOfferingBriefType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCoverageOfferingBriefType_LonLatEnvelope(), this.getLonLatEnvelopeType(), null, "lonLatEnvelope", null, 1, 1, CoverageOfferingBriefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCoverageOfferingBriefType_Keywords(), this.getKeywordsType(), null, "keywords", null, 0, -1, CoverageOfferingBriefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(coverageOfferingTypeEClass, CoverageOfferingType.class, "CoverageOfferingType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCoverageOfferingType_DomainSet(), this.getDomainSetType(), null, "domainSet", null, 1, 1, CoverageOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCoverageOfferingType_RangeSet(), this.getRangeSetType1(), null, "rangeSet", null, 1, 1, CoverageOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCoverageOfferingType_SupportedCRSs(), this.getSupportedCRSsType(), null, "supportedCRSs", null, 1, 1, CoverageOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCoverageOfferingType_SupportedFormats(), this.getSupportedFormatsType(), null, "supportedFormats", null, 1, 1, CoverageOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCoverageOfferingType_SupportedInterpolations(), this.getSupportedInterpolationsType(), null, "supportedInterpolations", null, 0, 1, CoverageOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dcpTypeTypeEClass, DCPTypeType.class, "DCPTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDCPTypeType_HTTP(), this.getHTTPType(), null, "hTTP", null, 1, 1, DCPTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(describeCoverageTypeEClass, DescribeCoverageType.class, "DescribeCoverageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDescribeCoverageType_Coverage(), ecorePackage.getEString(), "coverage", null, 0, -1, DescribeCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeCoverageType_Service(), theXMLTypePackage.getString(), "service", "WCS", 1, 1, DescribeCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeCoverageType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, DescribeCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeCoverageType_BaseUrl(), ecorePackage.getEString(), "baseUrl", null, 0, 1, DescribeCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeCoverageType_ExtendedProperties(), this.getMap(), "extendedProperties", null, 0, 1, DescribeCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(describeCoverageType1EClass, DescribeCoverageType1.class, "DescribeCoverageType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDescribeCoverageType1_DCPType(), this.getDCPTypeType(), null, "dCPType", null, 1, -1, DescribeCoverageType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_AxisDescription(), this.getAxisDescriptionType1(), null, "axisDescription", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_AxisDescription1(), this.getAxisDescriptionType(), null, "axisDescription1", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Capability(), this.getWCSCapabilityType(), null, "capability", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_ContentMetadata(), this.getContentMetadataType(), null, "contentMetadata", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_CoverageDescription(), this.getCoverageDescriptionType(), null, "coverageDescription", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_CoverageOffering(), this.getCoverageOfferingType(), null, "coverageOffering", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_CoverageOfferingBrief(), this.getCoverageOfferingBriefType(), null, "coverageOfferingBrief", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_DescribeCoverage(), this.getDescribeCoverageType(), null, "describeCoverage", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Description(), theXMLTypePackage.getString(), "description", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_DomainSet(), this.getDomainSetType(), null, "domainSet", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Formats(), theGmlPackage.getCodeListType(), null, "formats", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetCoverage(), this.getGetCoverageType(), null, "getCoverage", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_InterpolationMethod(), this.getInterpolationMethodType(), "interpolationMethod", "nearest neighbor", 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Interval(), this.getIntervalType(), null, "interval", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Keywords(), this.getKeywordsType(), null, "keywords", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_LonLatEnvelope(), this.getLonLatEnvelopeType(), null, "lonLatEnvelope", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_MetadataLink(), this.getMetadataLinkType(), null, "metadataLink", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Name(), theXMLTypePackage.getString(), "name", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_RangeSet(), this.getRangeSetType1(), null, "rangeSet", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_RangeSet1(), this.getRangeSetType(), null, "rangeSet1", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Service(), this.getServiceType(), null, "service", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_SingleValue(), this.getTypedLiteralType(), null, "singleValue", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_SpatialDomain(), this.getSpatialDomainType(), null, "spatialDomain", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_SpatialSubset(), this.getSpatialSubsetType(), null, "spatialSubset", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_SupportedCRSs(), this.getSupportedCRSsType(), null, "supportedCRSs", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_SupportedFormats(), this.getSupportedFormatsType(), null, "supportedFormats", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_SupportedInterpolations(), this.getSupportedInterpolationsType(), null, "supportedInterpolations", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_TemporalDomain(), this.getTimeSequenceType(), null, "temporalDomain", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_TemporalSubset(), this.getTimeSequenceType(), null, "temporalSubset", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_TimePeriod(), this.getTimePeriodType(), null, "timePeriod", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_TimeSequence(), this.getTimeSequenceType(), null, "timeSequence", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_WCSCapabilities(), this.getWCSCapabilitiesType(), null, "wCSCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Closure(), this.getClosureType(), "closure", "closed", 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Semantic(), theXMLTypePackage.getAnyURI(), "semantic", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Type(), theXMLTypePackage.getAnyURI(), "type", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(domainSetTypeEClass, DomainSetType.class, "DomainSetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDomainSetType_SpatialDomain(), this.getSpatialDomainType(), null, "spatialDomain", null, 0, 1, DomainSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDomainSetType_TemporalDomain(), this.getTimeSequenceType(), null, "temporalDomain", null, 0, 1, DomainSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDomainSetType_TemporalDomain1(), this.getTimeSequenceType(), null, "temporalDomain1", null, 0, 1, DomainSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(domainSubsetTypeEClass, DomainSubsetType.class, "DomainSubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDomainSubsetType_SpatialSubset(), this.getSpatialSubsetType(), null, "spatialSubset", null, 0, 1, DomainSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDomainSubsetType_TemporalSubset(), this.getTimeSequenceType(), null, "temporalSubset", null, 0, 1, DomainSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDomainSubsetType_TemporalSubset1(), this.getTimeSequenceType(), null, "temporalSubset1", null, 0, 1, DomainSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(exceptionTypeEClass, ExceptionType.class, "ExceptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExceptionType_Format(), theXMLTypePackage.getString(), "format", null, 1, 1, ExceptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGetCapabilitiesType_Section(), this.getCapabilitiesSectionType(), "section", "/", 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCapabilitiesType_Service(), theXMLTypePackage.getString(), "service", "WCS", 1, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCapabilitiesType_UpdateSequence(), theXMLTypePackage.getString(), "updateSequence", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCapabilitiesType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCapabilitiesType_BaseUrl(), ecorePackage.getEString(), "baseUrl", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCapabilitiesType_ExtendedProperties(), this.getMap(), "extendedProperties", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getCapabilitiesType1EClass, GetCapabilitiesType1.class, "GetCapabilitiesType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGetCapabilitiesType1_DCPType(), this.getDCPTypeType(), null, "dCPType", null, 1, -1, GetCapabilitiesType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getCoverageTypeEClass, GetCoverageType.class, "GetCoverageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGetCoverageType_SourceCoverage(), theXMLTypePackage.getString(), "sourceCoverage", null, 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGetCoverageType_DomainSubset(), this.getDomainSubsetType(), null, "domainSubset", null, 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGetCoverageType_RangeSubset(), this.getRangeSubsetType(), null, "rangeSubset", null, 0, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCoverageType_InterpolationMethod(), this.getInterpolationMethodType(), "interpolationMethod", "nearest neighbor", 0, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGetCoverageType_Output(), this.getOutputType(), null, "output", null, 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCoverageType_Service(), theXMLTypePackage.getString(), "service", "WCS", 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCoverageType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCoverageType_BaseUrl(), ecorePackage.getEString(), "baseUrl", null, 0, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCoverageType_ExtendedProperties(), this.getMap(), "extendedProperties", null, 0, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getCoverageType1EClass, GetCoverageType1.class, "GetCoverageType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGetCoverageType1_DCPType(), this.getDCPTypeType(), null, "dCPType", null, 1, -1, GetCoverageType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getTypeEClass, GetType.class, "GetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGetType_OnlineResource(), this.getOnlineResourceType(), null, "onlineResource", null, 1, 1, GetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(httpTypeEClass, HTTPType.class, "HTTPType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHTTPType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, HTTPType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getHTTPType_Get(), this.getGetType(), null, "get", null, 0, -1, HTTPType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getHTTPType_Post(), this.getPostType(), null, "post", null, 0, -1, HTTPType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(intervalTypeEClass, IntervalType.class, "IntervalType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIntervalType_Res(), this.getTypedLiteralType(), null, "res", null, 0, 1, IntervalType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(keywordsTypeEClass, KeywordsType.class, "KeywordsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getKeywordsType_Keyword(), theXMLTypePackage.getString(), "keyword", null, 1, 1, KeywordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getKeywordsType_Type(), theGmlPackage.getCodeType(), null, "type", null, 0, 1, KeywordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lonLatEnvelopeBaseTypeEClass, LonLatEnvelopeBaseType.class, "LonLatEnvelopeBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(lonLatEnvelopeTypeEClass, LonLatEnvelopeType.class, "LonLatEnvelopeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLonLatEnvelopeType_TimePosition(), theGmlPackage.getTimePositionType(), null, "timePosition", null, 0, 2, LonLatEnvelopeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metadataAssociationTypeEClass, MetadataAssociationType.class, "MetadataAssociationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(metadataLinkTypeEClass, MetadataLinkType.class, "MetadataLinkType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMetadataLinkType_MetadataType(), this.getMetadataTypeType(), "metadataType", "TC211", 1, 1, MetadataLinkType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(onlineResourceTypeEClass, OnlineResourceType.class, "OnlineResourceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOnlineResourceType_Actuate(), theXlinkPackage.getActuateType(), "actuate", "onLoad", 0, 1, OnlineResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOnlineResourceType_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, OnlineResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOnlineResourceType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, OnlineResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOnlineResourceType_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, OnlineResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOnlineResourceType_Show(), theXlinkPackage.getShowType(), "show", "new", 0, 1, OnlineResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOnlineResourceType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, OnlineResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOnlineResourceType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, OnlineResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(outputTypeEClass, OutputType.class, "OutputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOutputType_Crs(), theGmlPackage.getCodeType(), null, "crs", null, 0, 1, OutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOutputType_Format(), theGmlPackage.getCodeType(), null, "format", null, 1, 1, OutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(postTypeEClass, PostType.class, "PostType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPostType_OnlineResource(), this.getOnlineResourceType(), null, "onlineResource", null, 1, 1, PostType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(rangeSetTypeEClass, RangeSetType.class, "RangeSetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRangeSetType_AxisDescription(), this.getAxisDescriptionType1(), null, "axisDescription", null, 0, -1, RangeSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRangeSetType_NullValues(), this.getValueEnumType(), null, "nullValues", null, 0, 1, RangeSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRangeSetType_RefSys(), theXMLTypePackage.getAnyURI(), "refSys", null, 0, 1, RangeSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRangeSetType_RefSysLabel(), theXMLTypePackage.getString(), "refSysLabel", null, 0, 1, RangeSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRangeSetType_Semantic(), theXMLTypePackage.getAnyURI(), "semantic", null, 0, 1, RangeSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(rangeSetType1EClass, RangeSetType1.class, "RangeSetType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRangeSetType1_RangeSet(), this.getRangeSetType(), null, "rangeSet", null, 1, 1, RangeSetType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(rangeSubsetTypeEClass, RangeSubsetType.class, "RangeSubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRangeSubsetType_AxisSubset(), this.getAxisSubsetType(), null, "axisSubset", null, 1, -1, RangeSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(requestTypeEClass, RequestType.class, "RequestType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRequestType_GetCapabilities(), this.getGetCapabilitiesType1(), null, "getCapabilities", null, 1, 1, RequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRequestType_DescribeCoverage(), this.getDescribeCoverageType1(), null, "describeCoverage", null, 1, 1, RequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRequestType_GetCoverage(), this.getGetCoverageType1(), null, "getCoverage", null, 1, 1, RequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(responsiblePartyTypeEClass, ResponsiblePartyType.class, "ResponsiblePartyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getResponsiblePartyType_IndividualName(), theXMLTypePackage.getString(), "individualName", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResponsiblePartyType_OrganisationName(), theXMLTypePackage.getString(), "organisationName", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResponsiblePartyType_OrganisationName1(), theXMLTypePackage.getString(), "organisationName1", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResponsiblePartyType_PositionName(), theXMLTypePackage.getString(), "positionName", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResponsiblePartyType_ContactInfo(), this.getContactType(), null, "contactInfo", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(serviceTypeEClass, ServiceType.class, "ServiceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getServiceType_Keywords(), this.getKeywordsType(), null, "keywords", null, 0, -1, ServiceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getServiceType_ResponsibleParty(), this.getResponsiblePartyType(), null, "responsibleParty", null, 0, 1, ServiceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getServiceType_Fees(), theGmlPackage.getCodeListType(), null, "fees", null, 1, 1, ServiceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getServiceType_AccessConstraints(), theGmlPackage.getCodeListType(), null, "accessConstraints", null, 1, -1, ServiceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getServiceType_UpdateSequence(), theXMLTypePackage.getString(), "updateSequence", null, 0, 1, ServiceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getServiceType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 0, 1, ServiceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spatialDomainTypeEClass, SpatialDomainType.class, "SpatialDomainType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpatialDomainType_Envelope(), this.getGeneralEnvelope(), null, "envelope", null, 0, -1, SpatialDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSpatialDomainType_GridGroup(), theEcorePackage.getEFeatureMapEntry(), "gridGroup", null, 0, -1, SpatialDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpatialDomainType_Grid(), theGmlPackage.getGridType(), null, "grid", null, 0, -1, SpatialDomainType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSpatialDomainType_Polygon(), theGmlPackage.getPolygonType(), null, "polygon", null, 0, -1, SpatialDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spatialSubsetTypeEClass, SpatialSubsetType.class, "SpatialSubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(supportedCRSsTypeEClass, SupportedCRSsType.class, "SupportedCRSsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSupportedCRSsType_RequestResponseCRSs(), theGmlPackage.getCodeListType(), null, "requestResponseCRSs", null, 0, -1, SupportedCRSsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSupportedCRSsType_RequestCRSs(), theGmlPackage.getCodeListType(), null, "requestCRSs", null, 0, -1, SupportedCRSsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSupportedCRSsType_ResponseCRSs(), theGmlPackage.getCodeListType(), null, "responseCRSs", null, 0, -1, SupportedCRSsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSupportedCRSsType_NativeCRSs(), theGmlPackage.getCodeListType(), null, "nativeCRSs", null, 0, -1, SupportedCRSsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(supportedFormatsTypeEClass, SupportedFormatsType.class, "SupportedFormatsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSupportedFormatsType_Formats(), theGmlPackage.getCodeListType(), null, "formats", null, 1, -1, SupportedFormatsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSupportedFormatsType_NativeFormat(), theXMLTypePackage.getString(), "nativeFormat", null, 0, 1, SupportedFormatsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(supportedInterpolationsTypeEClass, SupportedInterpolationsType.class, "SupportedInterpolationsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSupportedInterpolationsType_InterpolationMethod(), this.getInterpolationMethodType(), "interpolationMethod", null, 1, 1, SupportedInterpolationsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSupportedInterpolationsType_Default(), this.getInterpolationMethodType(), "default", "nearest neighbor", 0, 1, SupportedInterpolationsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(telephoneTypeEClass, TelephoneType.class, "TelephoneType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTelephoneType_Voice(), theXMLTypePackage.getString(), "voice", null, 0, 1, TelephoneType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTelephoneType_Facsimile(), theXMLTypePackage.getString(), "facsimile", null, 0, 1, TelephoneType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(timePeriodTypeEClass, TimePeriodType.class, "TimePeriodType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTimePeriodType_BeginPosition(), theGmlPackage.getTimePositionType(), null, "beginPosition", null, 1, 1, TimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTimePeriodType_EndPosition(), theGmlPackage.getTimePositionType(), null, "endPosition", null, 1, 1, TimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTimePeriodType_TimeResolution(), theGmlPackage.getTimeDurationType(), "timeResolution", null, 0, 1, TimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTimePeriodType_Frame(), theXMLTypePackage.getAnyURI(), "frame", "#ISO-8601", 0, 1, TimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(timeSequenceTypeEClass, TimeSequenceType.class, "TimeSequenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTimeSequenceType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, TimeSequenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTimeSequenceType_TimePosition(), theGmlPackage.getTimePositionType(), null, "timePosition", null, 0, -1, TimeSequenceType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getTimeSequenceType_TimePeriod(), this.getTimePeriodType(), null, "timePeriod", null, 0, -1, TimeSequenceType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(typedLiteralTypeEClass, TypedLiteralType.class, "TypedLiteralType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTypedLiteralType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, TypedLiteralType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTypedLiteralType_Type(), theXMLTypePackage.getAnyURI(), "type", null, 0, 1, TypedLiteralType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(valueEnumBaseTypeEClass, ValueEnumBaseType.class, "ValueEnumBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getValueEnumBaseType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, ValueEnumBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getValueEnumBaseType_Interval(), this.getIntervalType(), null, "interval", null, 0, -1, ValueEnumBaseType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getValueEnumBaseType_SingleValue(), this.getTypedLiteralType(), null, "singleValue", null, 0, -1, ValueEnumBaseType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(valueEnumTypeEClass, ValueEnumType.class, "ValueEnumType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getValueEnumType_Semantic(), theXMLTypePackage.getAnyURI(), "semantic", null, 0, 1, ValueEnumType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueEnumType_Type(), theXMLTypePackage.getAnyURI(), "type", null, 0, 1, ValueEnumType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(valueRangeTypeEClass, ValueRangeType.class, "ValueRangeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getValueRangeType_Min(), this.getTypedLiteralType(), null, "min", null, 0, 1, ValueRangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getValueRangeType_Max(), this.getTypedLiteralType(), null, "max", null, 0, 1, ValueRangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueRangeType_Atomic(), theXMLTypePackage.getBoolean(), "atomic", "false", 0, 1, ValueRangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueRangeType_Closure(), this.getClosureType(), "closure", "closed", 0, 1, ValueRangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueRangeType_Semantic(), theXMLTypePackage.getAnyURI(), "semantic", null, 0, 1, ValueRangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueRangeType_Type(), theXMLTypePackage.getAnyURI(), "type", null, 0, 1, ValueRangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(valuesTypeEClass, ValuesType.class, "ValuesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getValuesType_Default(), this.getTypedLiteralType(), null, "default", null, 0, 1, ValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vendorSpecificCapabilitiesTypeEClass, VendorSpecificCapabilitiesType.class, "VendorSpecificCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVendorSpecificCapabilitiesType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 1, 1, VendorSpecificCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wcsCapabilitiesTypeEClass, WCSCapabilitiesType.class, "WCSCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getWCSCapabilitiesType_Service(), this.getServiceType(), null, "service", null, 1, 1, WCSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWCSCapabilitiesType_Capability(), this.getWCSCapabilityType(), null, "capability", null, 1, 1, WCSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWCSCapabilitiesType_ContentMetadata(), this.getContentMetadataType(), null, "contentMetadata", null, 1, 1, WCSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWCSCapabilitiesType_UpdateSequence(), theXMLTypePackage.getString(), "updateSequence", null, 0, 1, WCSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWCSCapabilitiesType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, WCSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wcsCapabilityTypeEClass, WCSCapabilityType.class, "WCSCapabilityType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getWCSCapabilityType_Request(), this.getRequestType(), null, "request", null, 1, 1, WCSCapabilityType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWCSCapabilityType_Exception(), this.getExceptionType(), null, "exception", null, 1, 1, WCSCapabilityType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWCSCapabilityType_VendorSpecificCapabilities(), this.getVendorSpecificCapabilitiesType(), null, "vendorSpecificCapabilities", null, 0, 1, WCSCapabilityType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWCSCapabilityType_UpdateSequence(), theXMLTypePackage.getString(), "updateSequence", null, 0, 1, WCSCapabilityType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWCSCapabilityType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 0, 1, WCSCapabilityType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(envelopeEClass, Envelope.class, "Envelope", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		initEClass(generalEnvelopeEClass, GeneralEnvelope.class, "GeneralEnvelope", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(capabilitiesSectionTypeEEnum, CapabilitiesSectionType.class, "CapabilitiesSectionType");
		addEEnumLiteral(capabilitiesSectionTypeEEnum, CapabilitiesSectionType.__LITERAL);
		addEEnumLiteral(capabilitiesSectionTypeEEnum, CapabilitiesSectionType.WCS_CAPABILITIES_SERVICE_LITERAL);
		addEEnumLiteral(capabilitiesSectionTypeEEnum, CapabilitiesSectionType.WCS_CAPABILITIES_CAPABILITY_LITERAL);
		addEEnumLiteral(capabilitiesSectionTypeEEnum, CapabilitiesSectionType.WCS_CAPABILITIES_CONTENT_METADATA_LITERAL);

		initEEnum(closureTypeEEnum, ClosureType.class, "ClosureType");
		addEEnumLiteral(closureTypeEEnum, ClosureType.CLOSED_LITERAL);
		addEEnumLiteral(closureTypeEEnum, ClosureType.OPEN_LITERAL);
		addEEnumLiteral(closureTypeEEnum, ClosureType.OPEN_CLOSED_LITERAL);
		addEEnumLiteral(closureTypeEEnum, ClosureType.CLOSED_OPEN_LITERAL);

		initEEnum(interpolationMethodTypeEEnum, InterpolationMethodType.class, "InterpolationMethodType");
		addEEnumLiteral(interpolationMethodTypeEEnum, InterpolationMethodType.NEAREST_NEIGHBOR_LITERAL);
		addEEnumLiteral(interpolationMethodTypeEEnum, InterpolationMethodType.BILINEAR_LITERAL);
		addEEnumLiteral(interpolationMethodTypeEEnum, InterpolationMethodType.BICUBIC_LITERAL);
		addEEnumLiteral(interpolationMethodTypeEEnum, InterpolationMethodType.LOST_AREA_LITERAL);
		addEEnumLiteral(interpolationMethodTypeEEnum, InterpolationMethodType.BARYCENTRIC_LITERAL);
		addEEnumLiteral(interpolationMethodTypeEEnum, InterpolationMethodType.NONE_LITERAL);

		initEEnum(metadataTypeTypeEEnum, MetadataTypeType.class, "MetadataTypeType");
		addEEnumLiteral(metadataTypeTypeEEnum, MetadataTypeType.TC211_LITERAL);
		addEEnumLiteral(metadataTypeTypeEEnum, MetadataTypeType.FGDC_LITERAL);
		addEEnumLiteral(metadataTypeTypeEEnum, MetadataTypeType.OTHER_LITERAL);

		// Initialize data types
		initEDataType(capabilitiesSectionTypeObjectEDataType, CapabilitiesSectionType.class, "CapabilitiesSectionTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(closureTypeObjectEDataType, ClosureType.class, "ClosureTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(interpolationMethodTypeObjectEDataType, InterpolationMethodType.class, "InterpolationMethodTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(metadataTypeTypeObjectEDataType, MetadataTypeType.class, "MetadataTypeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(mapEDataType, Map.class, "Map", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.w3.org/XML/1998/namespace
		createNamespaceAnnotations();
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
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
		  (abstractDescriptionBaseTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractDescriptionBaseType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (abstractDescriptionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractDescriptionType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getAbstractDescriptionType_MetadataLink(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "metadataLink",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getAbstractDescriptionType_Description1(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "description",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAbstractDescriptionType_Name1(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "name",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAbstractDescriptionType_Label(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "label",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (addressTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AddressType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getAddressType_DeliveryPoint(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "deliveryPoint",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAddressType_City(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "city",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAddressType_AdministrativeArea(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "administrativeArea",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAddressType_PostalCode(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "postalCode",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAddressType_Country(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "country",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAddressType_ElectronicMailAddress(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "electronicMailAddress",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (axisDescriptionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AxisDescriptionType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getAxisDescriptionType_Values(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "values",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAxisDescriptionType_RefSys(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "refSys"
		   });			
		addAnnotation
		  (getAxisDescriptionType_RefSysLabel(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "refSysLabel"
		   });			
		addAnnotation
		  (getAxisDescriptionType_Semantic(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "semantic",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (axisDescriptionType1EClass, 
		   source, 
		   new String[] {
			 "name", "axisDescription_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getAxisDescriptionType1_AxisDescription(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "AxisDescription",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (axisSubsetTypeEClass, 
		   source, 
		   new String[] {
			 "name", "axisSubset_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getAxisSubsetType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });			
		addAnnotation
		  (contactTypeEClass, 
		   source, 
		   new String[] {
			 "name", "ContactType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getContactType_Phone(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "phone",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getContactType_Address(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "address",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getContactType_OnlineResource(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "onlineResource",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (contentMetadataTypeEClass, 
		   source, 
		   new String[] {
			 "name", "ContentMetadata_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getContentMetadataType_CoverageOfferingBrief(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "CoverageOfferingBrief",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getContentMetadataType_Actuate(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "actuate",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getContentMetadataType_Arcrole(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "arcrole",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getContentMetadataType_Href(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "href",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getContentMetadataType_RemoteSchema(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "remoteSchema",
			 "namespace", "http://www.opengis.net/gml"
		   });			
		addAnnotation
		  (getContentMetadataType_Role(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "role",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getContentMetadataType_Show(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "show",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getContentMetadataType_Title(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "title",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getContentMetadataType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getContentMetadataType_UpdateSequence(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateSequence"
		   });			
		addAnnotation
		  (getContentMetadataType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (coverageDescriptionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "CoverageDescription_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getCoverageDescriptionType_CoverageOffering(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "CoverageOffering",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getCoverageDescriptionType_UpdateSequence(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateSequence"
		   });			
		addAnnotation
		  (getCoverageDescriptionType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (coverageOfferingBriefTypeEClass, 
		   source, 
		   new String[] {
			 "name", "CoverageOfferingBriefType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getCoverageOfferingBriefType_LonLatEnvelope(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "lonLatEnvelope",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getCoverageOfferingBriefType_Keywords(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "keywords",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (coverageOfferingTypeEClass, 
		   source, 
		   new String[] {
			 "name", "CoverageOfferingType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getCoverageOfferingType_DomainSet(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "domainSet",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getCoverageOfferingType_RangeSet(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "rangeSet",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getCoverageOfferingType_SupportedCRSs(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "supportedCRSs",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getCoverageOfferingType_SupportedFormats(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "supportedFormats",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getCoverageOfferingType_SupportedInterpolations(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "supportedInterpolations",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (dcpTypeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "DCPTypeType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getDCPTypeType_HTTP(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "HTTP",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (describeCoverageTypeEClass, 
		   source, 
		   new String[] {
			 "name", "DescribeCoverage_._1_._type",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getDescribeCoverageType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getDescribeCoverageType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (describeCoverageType1EClass, 
		   source, 
		   new String[] {
			 "name", "DescribeCoverage_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getDescribeCoverageType1_DCPType(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "DCPType",
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
		  (getDocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
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
		  (getDocumentRoot_AxisDescription(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "axisDescription",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_AxisDescription1(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "AxisDescription",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.opengis.net/gml#_GML"
		   });		
		addAnnotation
		  (getDocumentRoot_Capability(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Capability",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_ContentMetadata(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "ContentMetadata",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_CoverageDescription(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "CoverageDescription",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_CoverageOffering(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "CoverageOffering",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.opengis.net/gml#_GML"
		   });		
		addAnnotation
		  (getDocumentRoot_CoverageOfferingBrief(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "CoverageOfferingBrief",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.opengis.net/gml#_GML"
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
		  (getDocumentRoot_Description(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "description",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_DomainSet(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "domainSet",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Formats(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "formats",
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
		  (getDocumentRoot_InterpolationMethod(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "interpolationMethod",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Interval(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "interval",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Keywords(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "keywords",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_LonLatEnvelope(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "lonLatEnvelope",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_MetadataLink(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "metadataLink",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.opengis.net/gml#metaDataProperty"
		   });		
		addAnnotation
		  (getDocumentRoot_Name(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "name",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_RangeSet(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "rangeSet",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_RangeSet1(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "RangeSet",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.opengis.net/gml#_GML"
		   });		
		addAnnotation
		  (getDocumentRoot_Service(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Service",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.opengis.net/gml#_GML"
		   });		
		addAnnotation
		  (getDocumentRoot_SingleValue(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "singleValue",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_SpatialDomain(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "spatialDomain",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_SpatialSubset(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "spatialSubset",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_SupportedCRSs(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "supportedCRSs",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_SupportedFormats(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "supportedFormats",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_SupportedInterpolations(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "supportedInterpolations",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_TemporalDomain(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "temporalDomain",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_TemporalSubset(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "temporalSubset",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_TimePeriod(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "timePeriod",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_TimeSequence(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "TimeSequence",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_WCSCapabilities(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "WCS_Capabilities",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Closure(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "closure",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Semantic(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "semantic",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (domainSetTypeEClass, 
		   source, 
		   new String[] {
			 "name", "DomainSetType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getDomainSetType_SpatialDomain(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "spatialDomain",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDomainSetType_TemporalDomain(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "temporalDomain",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDomainSetType_TemporalDomain1(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "temporalDomain",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (domainSubsetTypeEClass, 
		   source, 
		   new String[] {
			 "name", "DomainSubsetType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getDomainSubsetType_SpatialSubset(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "spatialSubset",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDomainSubsetType_TemporalSubset(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "temporalSubset",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDomainSubsetType_TemporalSubset1(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "temporalSubset",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (exceptionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "Exception_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getExceptionType_Format(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Format",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getCapabilitiesTypeEClass, 
		   source, 
		   new String[] {
			 "name", "GetCapabilities_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getGetCapabilitiesType_Section(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "section",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getGetCapabilitiesType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getGetCapabilitiesType_UpdateSequence(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateSequence"
		   });			
		addAnnotation
		  (getGetCapabilitiesType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (getCapabilitiesType1EClass, 
		   source, 
		   new String[] {
			 "name", "GetCapabilities_._1_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getGetCapabilitiesType1_DCPType(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "DCPType",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getCoverageTypeEClass, 
		   source, 
		   new String[] {
			 "name", "GetCoverage_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getGetCoverageType_SourceCoverage(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "sourceCoverage",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getGetCoverageType_DomainSubset(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "domainSubset",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getGetCoverageType_RangeSubset(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "rangeSubset",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getGetCoverageType_InterpolationMethod(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "interpolationMethod",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getGetCoverageType_Output(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "output",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getGetCoverageType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getGetCoverageType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (getCoverageType1EClass, 
		   source, 
		   new String[] {
			 "name", "GetCoverage_._1_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getGetCoverageType1_DCPType(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "DCPType",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getTypeEClass, 
		   source, 
		   new String[] {
			 "name", "Get_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getGetType_OnlineResource(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "OnlineResource",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (httpTypeEClass, 
		   source, 
		   new String[] {
			 "name", "HTTP_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getHTTPType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:0"
		   });		
		addAnnotation
		  (getHTTPType_Get(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Get",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });		
		addAnnotation
		  (getHTTPType_Post(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Post",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });		
		addAnnotation
		  (intervalTypeEClass, 
		   source, 
		   new String[] {
			 "name", "intervalType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getIntervalType_Res(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "res",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (keywordsTypeEClass, 
		   source, 
		   new String[] {
			 "name", "keywords_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getKeywordsType_Keyword(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "keyword",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getKeywordsType_Type(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "type",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (lonLatEnvelopeBaseTypeEClass, 
		   source, 
		   new String[] {
			 "name", "LonLatEnvelopeBaseType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (lonLatEnvelopeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "LonLatEnvelopeType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getLonLatEnvelopeType_TimePosition(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "timePosition",
			 "namespace", "http://www.opengis.net/gml"
		   });			
		addAnnotation
		  (metadataAssociationTypeEClass, 
		   source, 
		   new String[] {
			 "name", "MetadataAssociationType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (metadataLinkTypeEClass, 
		   source, 
		   new String[] {
			 "name", "MetadataLinkType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getMetadataLinkType_MetadataType(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "metadataType"
		   });		
		addAnnotation
		  (onlineResourceTypeEClass, 
		   source, 
		   new String[] {
			 "name", "OnlineResourceType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getOnlineResourceType_Actuate(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "actuate",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getOnlineResourceType_Arcrole(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "arcrole",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getOnlineResourceType_Href(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "href",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getOnlineResourceType_Role(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "role",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getOnlineResourceType_Show(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "show",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getOnlineResourceType_Title(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "title",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getOnlineResourceType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (outputTypeEClass, 
		   source, 
		   new String[] {
			 "name", "OutputType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getOutputType_Crs(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "crs",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getOutputType_Format(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "format",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (postTypeEClass, 
		   source, 
		   new String[] {
			 "name", "Post_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getPostType_OnlineResource(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "OnlineResource",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (rangeSetTypeEClass, 
		   source, 
		   new String[] {
			 "name", "RangeSetType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getRangeSetType_AxisDescription(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "axisDescription",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getRangeSetType_NullValues(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "nullValues",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getRangeSetType_RefSys(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "refSys"
		   });			
		addAnnotation
		  (getRangeSetType_RefSysLabel(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "refSysLabel"
		   });			
		addAnnotation
		  (getRangeSetType_Semantic(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "semantic",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (rangeSetType1EClass, 
		   source, 
		   new String[] {
			 "name", "rangeSet_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getRangeSetType1_RangeSet(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "RangeSet",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (rangeSubsetTypeEClass, 
		   source, 
		   new String[] {
			 "name", "RangeSubsetType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getRangeSubsetType_AxisSubset(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "axisSubset",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (requestTypeEClass, 
		   source, 
		   new String[] {
			 "name", "Request_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getRequestType_GetCapabilities(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "GetCapabilities",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getRequestType_DescribeCoverage(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "DescribeCoverage",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getRequestType_GetCoverage(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "GetCoverage",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (responsiblePartyTypeEClass, 
		   source, 
		   new String[] {
			 "name", "ResponsiblePartyType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getResponsiblePartyType_IndividualName(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "individualName",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getResponsiblePartyType_OrganisationName(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "organisationName",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getResponsiblePartyType_OrganisationName1(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "organisationName",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getResponsiblePartyType_PositionName(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "positionName",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getResponsiblePartyType_ContactInfo(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "contactInfo",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (serviceTypeEClass, 
		   source, 
		   new String[] {
			 "name", "ServiceType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getServiceType_Keywords(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "keywords",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getServiceType_ResponsibleParty(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "responsibleParty",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getServiceType_Fees(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "fees",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getServiceType_AccessConstraints(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "accessConstraints",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getServiceType_UpdateSequence(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateSequence"
		   });			
		addAnnotation
		  (getServiceType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (spatialDomainTypeEClass, 
		   source, 
		   new String[] {
			 "name", "SpatialDomainType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getSpatialDomainType_GridGroup(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "Grid:group",
			 "namespace", "http://www.opengis.net/gml"
		   });		
		addAnnotation
		  (getSpatialDomainType_Grid(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Grid",
			 "namespace", "http://www.opengis.net/gml",
			 "group", "http://www.opengis.net/gml#Grid:group"
		   });		
		addAnnotation
		  (getSpatialDomainType_Polygon(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Polygon",
			 "namespace", "http://www.opengis.net/gml"
		   });		
		addAnnotation
		  (spatialSubsetTypeEClass, 
		   source, 
		   new String[] {
			 "name", "SpatialSubsetType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (supportedCRSsTypeEClass, 
		   source, 
		   new String[] {
			 "name", "SupportedCRSsType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getSupportedCRSsType_RequestResponseCRSs(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "requestResponseCRSs",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getSupportedCRSsType_RequestCRSs(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "requestCRSs",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getSupportedCRSsType_ResponseCRSs(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "responseCRSs",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getSupportedCRSsType_NativeCRSs(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "nativeCRSs",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (supportedFormatsTypeEClass, 
		   source, 
		   new String[] {
			 "name", "SupportedFormatsType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getSupportedFormatsType_Formats(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "formats",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getSupportedFormatsType_NativeFormat(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "nativeFormat"
		   });			
		addAnnotation
		  (supportedInterpolationsTypeEClass, 
		   source, 
		   new String[] {
			 "name", "SupportedInterpolationsType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getSupportedInterpolationsType_InterpolationMethod(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "interpolationMethod",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSupportedInterpolationsType_Default(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "default"
		   });		
		addAnnotation
		  (telephoneTypeEClass, 
		   source, 
		   new String[] {
			 "name", "TelephoneType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getTelephoneType_Voice(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "voice",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getTelephoneType_Facsimile(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "facsimile",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (timePeriodTypeEClass, 
		   source, 
		   new String[] {
			 "name", "TimePeriodType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getTimePeriodType_BeginPosition(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "beginPosition",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getTimePeriodType_EndPosition(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "endPosition",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getTimePeriodType_TimeResolution(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "timeResolution",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getTimePeriodType_Frame(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "frame"
		   });		
		addAnnotation
		  (timeSequenceTypeEClass, 
		   source, 
		   new String[] {
			 "name", "TimeSequenceType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getTimeSequenceType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:0"
		   });		
		addAnnotation
		  (getTimeSequenceType_TimePosition(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "timePosition",
			 "namespace", "http://www.opengis.net/gml",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (getTimeSequenceType_TimePeriod(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "timePeriod",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });		
		addAnnotation
		  (typedLiteralTypeEClass, 
		   source, 
		   new String[] {
			 "name", "TypedLiteralType",
			 "kind", "simple"
		   });			
		addAnnotation
		  (getTypedLiteralType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getTypedLiteralType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (valueEnumBaseTypeEClass, 
		   source, 
		   new String[] {
			 "name", "valueEnumBaseType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getValueEnumBaseType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:0"
		   });		
		addAnnotation
		  (getValueEnumBaseType_Interval(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "interval",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });		
		addAnnotation
		  (getValueEnumBaseType_SingleValue(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "singleValue",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (valueEnumTypeEClass, 
		   source, 
		   new String[] {
			 "name", "valueEnumType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getValueEnumType_Semantic(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "semantic",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getValueEnumType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (valueRangeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "valueRangeType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getValueRangeType_Min(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "min",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getValueRangeType_Max(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "max",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getValueRangeType_Atomic(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "atomic"
		   });			
		addAnnotation
		  (getValueRangeType_Closure(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "closure",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getValueRangeType_Semantic(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "semantic",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getValueRangeType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (valuesTypeEClass, 
		   source, 
		   new String[] {
			 "name", "values_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getValuesType_Default(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "default",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (vendorSpecificCapabilitiesTypeEClass, 
		   source, 
		   new String[] {
			 "name", "VendorSpecificCapabilities_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getVendorSpecificCapabilitiesType_Any(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "wildcards", "##any",
			 "name", ":0",
			 "processing", "strict"
		   });		
		addAnnotation
		  (wcsCapabilitiesTypeEClass, 
		   source, 
		   new String[] {
			 "name", "WCS_CapabilitiesType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getWCSCapabilitiesType_Service(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Service",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getWCSCapabilitiesType_Capability(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Capability",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getWCSCapabilitiesType_ContentMetadata(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "ContentMetadata",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getWCSCapabilitiesType_UpdateSequence(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateSequence"
		   });			
		addAnnotation
		  (getWCSCapabilitiesType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (wcsCapabilityTypeEClass, 
		   source, 
		   new String[] {
			 "name", "WCSCapabilityType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getWCSCapabilityType_Request(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Request",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getWCSCapabilityType_Exception(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Exception",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getWCSCapabilityType_VendorSpecificCapabilities(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "VendorSpecificCapabilities",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getWCSCapabilityType_UpdateSequence(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateSequence"
		   });			
		addAnnotation
		  (getWCSCapabilityType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (capabilitiesSectionTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "CapabilitiesSectionType"
		   });							
		addAnnotation
		  (closureTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "closure_._type"
		   });						
		addAnnotation
		  (interpolationMethodTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "InterpolationMethodType"
		   });				
		addAnnotation
		  (metadataTypeTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "metadataType_._type"
		   });					
		addAnnotation
		  (capabilitiesSectionTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "CapabilitiesSectionType:Object",
			 "baseType", "CapabilitiesSectionType"
		   });		
		addAnnotation
		  (closureTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "closure_._type:Object",
			 "baseType", "closure_._type"
		   });		
		addAnnotation
		  (interpolationMethodTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "InterpolationMethodType:Object",
			 "baseType", "InterpolationMethodType"
		   });		
		addAnnotation
		  (metadataTypeTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "metadataType_._type:Object",
			 "baseType", "metadataType_._type"
		   });
	}

} //Wcs10PackageImpl
