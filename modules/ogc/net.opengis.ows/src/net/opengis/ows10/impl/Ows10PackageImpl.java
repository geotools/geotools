/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import java.lang.Object;
import java.util.List;

import java.util.Map;
import net.opengis.ows10.AcceptFormatsType;
import net.opengis.ows10.AcceptVersionsType;
import net.opengis.ows10.AddressType;
import net.opengis.ows10.BoundingBoxType;
import net.opengis.ows10.CapabilitiesBaseType;
import net.opengis.ows10.CodeType;
import net.opengis.ows10.ContactType;
import net.opengis.ows10.DCPType;
import net.opengis.ows10.DescriptionType;
import net.opengis.ows10.DocumentRoot;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.ExceptionReportType;
import net.opengis.ows10.ExceptionType;
import net.opengis.ows10.GetCapabilitiesType;
import net.opengis.ows10.HTTPType;
import net.opengis.ows10.IdentificationType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.MetadataType;
import net.opengis.ows10.OnlineResourceType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.Ows10Package;
import net.opengis.ows10.RequestMethodType;
import net.opengis.ows10.ResponsiblePartySubsetType;
import net.opengis.ows10.ResponsiblePartyType;
import net.opengis.ows10.SectionsType;
import net.opengis.ows10.ServiceIdentificationType;
import net.opengis.ows10.ServiceProviderType;
import net.opengis.ows10.TelephoneType;
import net.opengis.ows10.WGS84BoundingBoxType;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.namespace.XMLNamespacePackage;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Ows10PackageImpl extends EPackageImpl implements Ows10Package {
	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass acceptFormatsTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass acceptVersionsTypeEClass = null;

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
	private EClass boundingBoxTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass capabilitiesBaseTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass codeTypeEClass = null;

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
	private EClass dcpTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass descriptionTypeEClass = null;

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
	private EClass domainTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass exceptionReportTypeEClass = null;

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
	private EClass httpTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass identificationTypeEClass = null;

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
	private EClass metadataTypeEClass = null;

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
	private EClass operationTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass operationsMetadataTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass requestMethodTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass responsiblePartySubsetTypeEClass = null;

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
	private EClass sectionsTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass serviceIdentificationTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass serviceProviderTypeEClass = null;

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
	private EClass wgs84BoundingBoxTypeEClass = null;

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
	private EDataType mimeTypeEDataType = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EDataType versionTypeEDataType = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EDataType positionTypeEDataType = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EDataType updateSequenceTypeEDataType = null;

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
     * @see net.opengis.ows10.Ows10Package#eNS_URI
     * @see #init()
     * @generated
     */
	private Ows10PackageImpl() {
        super(eNS_URI, Ows10Factory.eINSTANCE);
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
     * <p>This method is used to initialize {@link Ows10Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
	public static Ows10Package init() {
        if (isInited) return (Ows10Package)EPackage.Registry.INSTANCE.getEPackage(Ows10Package.eNS_URI);

        // Obtain or create and register package
        Ows10PackageImpl theOws10Package = (Ows10PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Ows10PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Ows10PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackage.eINSTANCE.eClass();
        EcorePackage.eINSTANCE.eClass();
        XMLNamespacePackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theOws10Package.createPackageContents();

        // Initialize created meta-data
        theOws10Package.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theOws10Package.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(Ows10Package.eNS_URI, theOws10Package);
        return theOws10Package;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getAcceptFormatsType() {
        return acceptFormatsTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getAcceptFormatsType_OutputFormat() {
        return (EAttribute)acceptFormatsTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getAcceptVersionsType() {
        return acceptVersionsTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getAcceptVersionsType_Version() {
        return (EAttribute)acceptVersionsTypeEClass.getEStructuralFeatures().get(0);
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
	public EClass getBoundingBoxType() {
        return boundingBoxTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getBoundingBoxType_LowerCorner() {
        return (EAttribute)boundingBoxTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getBoundingBoxType_UpperCorner() {
        return (EAttribute)boundingBoxTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getBoundingBoxType_Crs() {
        return (EAttribute)boundingBoxTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getBoundingBoxType_Dimensions() {
        return (EAttribute)boundingBoxTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getCapabilitiesBaseType() {
        return capabilitiesBaseTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getCapabilitiesBaseType_ServiceIdentification() {
        return (EReference)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getCapabilitiesBaseType_ServiceProvider() {
        return (EReference)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getCapabilitiesBaseType_OperationsMetadata() {
        return (EReference)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getCapabilitiesBaseType_UpdateSequence() {
        return (EAttribute)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getCapabilitiesBaseType_Version() {
        return (EAttribute)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getCodeType() {
        return codeTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getCodeType_Value() {
        return (EAttribute)codeTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getCodeType_CodeSpace() {
        return (EAttribute)codeTypeEClass.getEStructuralFeatures().get(1);
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
	public EAttribute getContactType_HoursOfService() {
        return (EAttribute)contactTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getContactType_ContactInstructions() {
        return (EAttribute)contactTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getDCPType() {
        return dcpTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDCPType_HTTP() {
        return (EReference)dcpTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getDescriptionType() {
        return descriptionTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDescriptionType_Title() {
        return (EAttribute)descriptionTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDescriptionType_Abstract() {
        return (EAttribute)descriptionTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDescriptionType_Keywords() {
        return (EReference)descriptionTypeEClass.getEStructuralFeatures().get(2);
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
	public EAttribute getDocumentRoot_Abstract() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_ContactInfo() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_IndividualName() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(5);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Keywords() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_OrganisationName() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(7);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_PointOfContact() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_PositionName() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(9);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Role() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_Title() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(11);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_AbstractMetaData() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_AccessConstraints() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(13);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_AvailableCRS() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(14);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_BoundingBox() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Dcp() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Exception() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_ExceptionReport() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_ExtendedCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_Fees() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(20);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_GetCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(21);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Http() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(22);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Identifier() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(23);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_Language() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(24);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Metadata() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(25);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Operation() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(26);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_OperationsMetadata() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(27);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_OutputFormat() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(28);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_ServiceIdentification() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(29);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_ServiceProvider() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(30);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_SupportedCRS() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(31);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_WgS84BoundingBox() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(32);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getDomainType() {
        return domainTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDomainType_Value() {
        return (EAttribute)domainTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDomainType_Metadata() {
        return (EReference)domainTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDomainType_Name() {
        return (EAttribute)domainTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getExceptionReportType() {
        return exceptionReportTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getExceptionReportType_Exception() {
        return (EReference)exceptionReportTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getExceptionReportType_Language() {
        return (EAttribute)exceptionReportTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getExceptionReportType_Version() {
        return (EAttribute)exceptionReportTypeEClass.getEStructuralFeatures().get(2);
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
	public EAttribute getExceptionType_ExceptionText() {
        return (EAttribute)exceptionTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getExceptionType_ExceptionCode() {
        return (EAttribute)exceptionTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getExceptionType_Locator() {
        return (EAttribute)exceptionTypeEClass.getEStructuralFeatures().get(2);
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
	public EReference getGetCapabilitiesType_AcceptVersions() {
        return (EReference)getCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getGetCapabilitiesType_Sections() {
        return (EReference)getCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getGetCapabilitiesType_AcceptFormats() {
        return (EReference)getCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetCapabilitiesType_UpdateSequence() {
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
    public EAttribute getGetCapabilitiesType_Namespace() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetCapabilitiesType_ExtendedProperties() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(6);
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
	public EClass getIdentificationType() {
        return identificationTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getIdentificationType_Identifier() {
        return (EReference)identificationTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getIdentificationType_BoundingBoxGroup() {
        return (EAttribute)identificationTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getIdentificationType_BoundingBox() {
        return (EReference)identificationTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getIdentificationType_OutputFormat() {
        return (EAttribute)identificationTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getIdentificationType_AvailableCRSGroup() {
        return (EAttribute)identificationTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getIdentificationType_AvailableCRS() {
        return (EAttribute)identificationTypeEClass.getEStructuralFeatures().get(5);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getIdentificationType_Metadata() {
        return (EReference)identificationTypeEClass.getEStructuralFeatures().get(6);
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
	public EClass getMetadataType() {
        return metadataTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getMetadataType_AbstractMetaDataGroup() {
        return (EAttribute)metadataTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getMetadataType_AbstractMetaData() {
        return (EReference)metadataTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getMetadataType_About() {
        return (EAttribute)metadataTypeEClass.getEStructuralFeatures().get(2);
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
	public EAttribute getOnlineResourceType_Href() {
        return (EAttribute)onlineResourceTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getOperationType() {
        return operationTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getOperationType_DCP() {
        return (EReference)operationTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getOperationType_Parameter() {
        return (EReference)operationTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getOperationType_Constraint() {
        return (EReference)operationTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getOperationType_Metadata() {
        return (EReference)operationTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getOperationType_Name() {
        return (EAttribute)operationTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getOperationsMetadataType() {
        return operationsMetadataTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getOperationsMetadataType_Operation() {
        return (EReference)operationsMetadataTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getOperationsMetadataType_Parameter() {
        return (EReference)operationsMetadataTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getOperationsMetadataType_Constraint() {
        return (EReference)operationsMetadataTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getOperationsMetadataType_ExtendedCapabilities() {
        return (EReference)operationsMetadataTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getRequestMethodType() {
        return requestMethodTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getRequestMethodType_Constraint() {
        return (EReference)requestMethodTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getResponsiblePartySubsetType() {
        return responsiblePartySubsetTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getResponsiblePartySubsetType_IndividualName() {
        return (EAttribute)responsiblePartySubsetTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getResponsiblePartySubsetType_PositionName() {
        return (EAttribute)responsiblePartySubsetTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getResponsiblePartySubsetType_ContactInfo() {
        return (EReference)responsiblePartySubsetTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getResponsiblePartySubsetType_Role() {
        return (EReference)responsiblePartySubsetTypeEClass.getEStructuralFeatures().get(3);
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
	public EAttribute getResponsiblePartyType_PositionName() {
        return (EAttribute)responsiblePartyTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getResponsiblePartyType_ContactInfo() {
        return (EReference)responsiblePartyTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getResponsiblePartyType_Role() {
        return (EReference)responsiblePartyTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getSectionsType() {
        return sectionsTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getSectionsType_Section() {
        return (EAttribute)sectionsTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getServiceIdentificationType() {
        return serviceIdentificationTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getServiceIdentificationType_ServiceType() {
        return (EReference)serviceIdentificationTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getServiceIdentificationType_ServiceTypeVersion() {
        return (EAttribute)serviceIdentificationTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getServiceIdentificationType_Fees() {
        return (EAttribute)serviceIdentificationTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getServiceIdentificationType_AccessConstraints() {
        return (EAttribute)serviceIdentificationTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getServiceProviderType() {
        return serviceProviderTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getServiceProviderType_ProviderName() {
        return (EAttribute)serviceProviderTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getServiceProviderType_ProviderSite() {
        return (EReference)serviceProviderTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getServiceProviderType_ServiceContact() {
        return (EReference)serviceProviderTypeEClass.getEStructuralFeatures().get(2);
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
	public EClass getWGS84BoundingBoxType() {
        return wgs84BoundingBoxTypeEClass;
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
	public EDataType getMimeType() {
        return mimeTypeEDataType;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EDataType getVersionType() {
        return versionTypeEDataType;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EDataType getPositionType() {
        return positionTypeEDataType;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EDataType getUpdateSequenceType() {
        return updateSequenceTypeEDataType;
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
	public Ows10Factory getOws10Factory() {
        return (Ows10Factory)getEFactoryInstance();
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
        acceptFormatsTypeEClass = createEClass(ACCEPT_FORMATS_TYPE);
        createEAttribute(acceptFormatsTypeEClass, ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT);

        acceptVersionsTypeEClass = createEClass(ACCEPT_VERSIONS_TYPE);
        createEAttribute(acceptVersionsTypeEClass, ACCEPT_VERSIONS_TYPE__VERSION);

        addressTypeEClass = createEClass(ADDRESS_TYPE);
        createEAttribute(addressTypeEClass, ADDRESS_TYPE__DELIVERY_POINT);
        createEAttribute(addressTypeEClass, ADDRESS_TYPE__CITY);
        createEAttribute(addressTypeEClass, ADDRESS_TYPE__ADMINISTRATIVE_AREA);
        createEAttribute(addressTypeEClass, ADDRESS_TYPE__POSTAL_CODE);
        createEAttribute(addressTypeEClass, ADDRESS_TYPE__COUNTRY);
        createEAttribute(addressTypeEClass, ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS);

        boundingBoxTypeEClass = createEClass(BOUNDING_BOX_TYPE);
        createEAttribute(boundingBoxTypeEClass, BOUNDING_BOX_TYPE__LOWER_CORNER);
        createEAttribute(boundingBoxTypeEClass, BOUNDING_BOX_TYPE__UPPER_CORNER);
        createEAttribute(boundingBoxTypeEClass, BOUNDING_BOX_TYPE__CRS);
        createEAttribute(boundingBoxTypeEClass, BOUNDING_BOX_TYPE__DIMENSIONS);

        capabilitiesBaseTypeEClass = createEClass(CAPABILITIES_BASE_TYPE);
        createEReference(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION);
        createEReference(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER);
        createEReference(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA);
        createEAttribute(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE);
        createEAttribute(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__VERSION);

        codeTypeEClass = createEClass(CODE_TYPE);
        createEAttribute(codeTypeEClass, CODE_TYPE__VALUE);
        createEAttribute(codeTypeEClass, CODE_TYPE__CODE_SPACE);

        contactTypeEClass = createEClass(CONTACT_TYPE);
        createEReference(contactTypeEClass, CONTACT_TYPE__PHONE);
        createEReference(contactTypeEClass, CONTACT_TYPE__ADDRESS);
        createEReference(contactTypeEClass, CONTACT_TYPE__ONLINE_RESOURCE);
        createEAttribute(contactTypeEClass, CONTACT_TYPE__HOURS_OF_SERVICE);
        createEAttribute(contactTypeEClass, CONTACT_TYPE__CONTACT_INSTRUCTIONS);

        dcpTypeEClass = createEClass(DCP_TYPE);
        createEReference(dcpTypeEClass, DCP_TYPE__HTTP);

        descriptionTypeEClass = createEClass(DESCRIPTION_TYPE);
        createEAttribute(descriptionTypeEClass, DESCRIPTION_TYPE__TITLE);
        createEAttribute(descriptionTypeEClass, DESCRIPTION_TYPE__ABSTRACT);
        createEReference(descriptionTypeEClass, DESCRIPTION_TYPE__KEYWORDS);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__ABSTRACT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CONTACT_INFO);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__INDIVIDUAL_NAME);
        createEReference(documentRootEClass, DOCUMENT_ROOT__KEYWORDS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__ORGANISATION_NAME);
        createEReference(documentRootEClass, DOCUMENT_ROOT__POINT_OF_CONTACT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__POSITION_NAME);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ROLE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__TITLE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT_META_DATA);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__ACCESS_CONSTRAINTS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__AVAILABLE_CRS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__BOUNDING_BOX);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DCP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXCEPTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXCEPTION_REPORT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXTENDED_CAPABILITIES);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__FEES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__HTTP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__IDENTIFIER);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__LANGUAGE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__METADATA);
        createEReference(documentRootEClass, DOCUMENT_ROOT__OPERATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__OPERATIONS_METADATA);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__OUTPUT_FORMAT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SERVICE_IDENTIFICATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SERVICE_PROVIDER);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__SUPPORTED_CRS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__WG_S84_BOUNDING_BOX);

        domainTypeEClass = createEClass(DOMAIN_TYPE);
        createEAttribute(domainTypeEClass, DOMAIN_TYPE__VALUE);
        createEReference(domainTypeEClass, DOMAIN_TYPE__METADATA);
        createEAttribute(domainTypeEClass, DOMAIN_TYPE__NAME);

        exceptionReportTypeEClass = createEClass(EXCEPTION_REPORT_TYPE);
        createEReference(exceptionReportTypeEClass, EXCEPTION_REPORT_TYPE__EXCEPTION);
        createEAttribute(exceptionReportTypeEClass, EXCEPTION_REPORT_TYPE__LANGUAGE);
        createEAttribute(exceptionReportTypeEClass, EXCEPTION_REPORT_TYPE__VERSION);

        exceptionTypeEClass = createEClass(EXCEPTION_TYPE);
        createEAttribute(exceptionTypeEClass, EXCEPTION_TYPE__EXCEPTION_TEXT);
        createEAttribute(exceptionTypeEClass, EXCEPTION_TYPE__EXCEPTION_CODE);
        createEAttribute(exceptionTypeEClass, EXCEPTION_TYPE__LOCATOR);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEReference(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS);
        createEReference(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SECTIONS);
        createEReference(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__ACCEPT_FORMATS);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__BASE_URL);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__NAMESPACE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES);

        httpTypeEClass = createEClass(HTTP_TYPE);
        createEAttribute(httpTypeEClass, HTTP_TYPE__GROUP);
        createEReference(httpTypeEClass, HTTP_TYPE__GET);
        createEReference(httpTypeEClass, HTTP_TYPE__POST);

        identificationTypeEClass = createEClass(IDENTIFICATION_TYPE);
        createEReference(identificationTypeEClass, IDENTIFICATION_TYPE__IDENTIFIER);
        createEAttribute(identificationTypeEClass, IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP);
        createEReference(identificationTypeEClass, IDENTIFICATION_TYPE__BOUNDING_BOX);
        createEAttribute(identificationTypeEClass, IDENTIFICATION_TYPE__OUTPUT_FORMAT);
        createEAttribute(identificationTypeEClass, IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP);
        createEAttribute(identificationTypeEClass, IDENTIFICATION_TYPE__AVAILABLE_CRS);
        createEReference(identificationTypeEClass, IDENTIFICATION_TYPE__METADATA);

        keywordsTypeEClass = createEClass(KEYWORDS_TYPE);
        createEAttribute(keywordsTypeEClass, KEYWORDS_TYPE__KEYWORD);
        createEReference(keywordsTypeEClass, KEYWORDS_TYPE__TYPE);

        metadataTypeEClass = createEClass(METADATA_TYPE);
        createEAttribute(metadataTypeEClass, METADATA_TYPE__ABSTRACT_META_DATA_GROUP);
        createEReference(metadataTypeEClass, METADATA_TYPE__ABSTRACT_META_DATA);
        createEAttribute(metadataTypeEClass, METADATA_TYPE__ABOUT);

        onlineResourceTypeEClass = createEClass(ONLINE_RESOURCE_TYPE);
        createEAttribute(onlineResourceTypeEClass, ONLINE_RESOURCE_TYPE__HREF);

        operationTypeEClass = createEClass(OPERATION_TYPE);
        createEReference(operationTypeEClass, OPERATION_TYPE__DCP);
        createEReference(operationTypeEClass, OPERATION_TYPE__PARAMETER);
        createEReference(operationTypeEClass, OPERATION_TYPE__CONSTRAINT);
        createEReference(operationTypeEClass, OPERATION_TYPE__METADATA);
        createEAttribute(operationTypeEClass, OPERATION_TYPE__NAME);

        operationsMetadataTypeEClass = createEClass(OPERATIONS_METADATA_TYPE);
        createEReference(operationsMetadataTypeEClass, OPERATIONS_METADATA_TYPE__OPERATION);
        createEReference(operationsMetadataTypeEClass, OPERATIONS_METADATA_TYPE__PARAMETER);
        createEReference(operationsMetadataTypeEClass, OPERATIONS_METADATA_TYPE__CONSTRAINT);
        createEReference(operationsMetadataTypeEClass, OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES);

        requestMethodTypeEClass = createEClass(REQUEST_METHOD_TYPE);
        createEReference(requestMethodTypeEClass, REQUEST_METHOD_TYPE__CONSTRAINT);

        responsiblePartySubsetTypeEClass = createEClass(RESPONSIBLE_PARTY_SUBSET_TYPE);
        createEAttribute(responsiblePartySubsetTypeEClass, RESPONSIBLE_PARTY_SUBSET_TYPE__INDIVIDUAL_NAME);
        createEAttribute(responsiblePartySubsetTypeEClass, RESPONSIBLE_PARTY_SUBSET_TYPE__POSITION_NAME);
        createEReference(responsiblePartySubsetTypeEClass, RESPONSIBLE_PARTY_SUBSET_TYPE__CONTACT_INFO);
        createEReference(responsiblePartySubsetTypeEClass, RESPONSIBLE_PARTY_SUBSET_TYPE__ROLE);

        responsiblePartyTypeEClass = createEClass(RESPONSIBLE_PARTY_TYPE);
        createEAttribute(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME);
        createEAttribute(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME);
        createEAttribute(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__POSITION_NAME);
        createEReference(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__CONTACT_INFO);
        createEReference(responsiblePartyTypeEClass, RESPONSIBLE_PARTY_TYPE__ROLE);

        sectionsTypeEClass = createEClass(SECTIONS_TYPE);
        createEAttribute(sectionsTypeEClass, SECTIONS_TYPE__SECTION);

        serviceIdentificationTypeEClass = createEClass(SERVICE_IDENTIFICATION_TYPE);
        createEReference(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE);
        createEAttribute(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION);
        createEAttribute(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__FEES);
        createEAttribute(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS);

        serviceProviderTypeEClass = createEClass(SERVICE_PROVIDER_TYPE);
        createEAttribute(serviceProviderTypeEClass, SERVICE_PROVIDER_TYPE__PROVIDER_NAME);
        createEReference(serviceProviderTypeEClass, SERVICE_PROVIDER_TYPE__PROVIDER_SITE);
        createEReference(serviceProviderTypeEClass, SERVICE_PROVIDER_TYPE__SERVICE_CONTACT);

        telephoneTypeEClass = createEClass(TELEPHONE_TYPE);
        createEAttribute(telephoneTypeEClass, TELEPHONE_TYPE__VOICE);
        createEAttribute(telephoneTypeEClass, TELEPHONE_TYPE__FACSIMILE);

        wgs84BoundingBoxTypeEClass = createEClass(WGS84_BOUNDING_BOX_TYPE);

        objectEClass = createEClass(OBJECT);

        // Create data types
        mimeTypeEDataType = createEDataType(MIME_TYPE);
        versionTypeEDataType = createEDataType(VERSION_TYPE);
        positionTypeEDataType = createEDataType(POSITION_TYPE);
        updateSequenceTypeEDataType = createEDataType(UPDATE_SEQUENCE_TYPE);
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
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

        // Add supertypes to classes
        identificationTypeEClass.getESuperTypes().add(this.getDescriptionType());
        requestMethodTypeEClass.getESuperTypes().add(this.getOnlineResourceType());
        serviceIdentificationTypeEClass.getESuperTypes().add(this.getDescriptionType());
        wgs84BoundingBoxTypeEClass.getESuperTypes().add(this.getBoundingBoxType());

        // Initialize classes and features; add operations and parameters
        initEClass(acceptFormatsTypeEClass, AcceptFormatsType.class, "AcceptFormatsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAcceptFormatsType_OutputFormat(), this.getMimeType(), "outputFormat", null, 0, 1, AcceptFormatsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(acceptVersionsTypeEClass, AcceptVersionsType.class, "AcceptVersionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAcceptVersionsType_Version(), this.getMimeType(), "version", null, 0, 1, AcceptVersionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(addressTypeEClass, AddressType.class, "AddressType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAddressType_DeliveryPoint(), theXMLTypePackage.getString(), "deliveryPoint", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressType_City(), theXMLTypePackage.getString(), "city", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressType_AdministrativeArea(), theXMLTypePackage.getString(), "administrativeArea", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressType_PostalCode(), theXMLTypePackage.getString(), "postalCode", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressType_Country(), theXMLTypePackage.getString(), "country", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressType_ElectronicMailAddress(), theXMLTypePackage.getString(), "electronicMailAddress", null, 0, 1, AddressType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(boundingBoxTypeEClass, BoundingBoxType.class, "BoundingBoxType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBoundingBoxType_LowerCorner(), this.getPositionType(), "lowerCorner", null, 1, 1, BoundingBoxType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBoundingBoxType_UpperCorner(), this.getPositionType(), "upperCorner", null, 1, 1, BoundingBoxType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBoundingBoxType_Crs(), theXMLTypePackage.getAnyURI(), "crs", null, 0, 1, BoundingBoxType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBoundingBoxType_Dimensions(), theXMLTypePackage.getPositiveInteger(), "dimensions", null, 0, 1, BoundingBoxType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(capabilitiesBaseTypeEClass, CapabilitiesBaseType.class, "CapabilitiesBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCapabilitiesBaseType_ServiceIdentification(), this.getServiceIdentificationType(), null, "serviceIdentification", null, 0, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCapabilitiesBaseType_ServiceProvider(), this.getServiceProviderType(), null, "serviceProvider", null, 0, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCapabilitiesBaseType_OperationsMetadata(), this.getOperationsMetadataType(), null, "operationsMetadata", null, 0, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCapabilitiesBaseType_UpdateSequence(), this.getUpdateSequenceType(), "updateSequence", null, 0, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCapabilitiesBaseType_Version(), this.getVersionType(), "version", null, 1, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(codeTypeEClass, CodeType.class, "CodeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCodeType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, CodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCodeType_CodeSpace(), theXMLTypePackage.getAnyURI(), "codeSpace", null, 0, 1, CodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(contactTypeEClass, ContactType.class, "ContactType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getContactType_Phone(), this.getTelephoneType(), null, "phone", null, 0, 1, ContactType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getContactType_Address(), this.getAddressType(), null, "address", null, 0, 1, ContactType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getContactType_OnlineResource(), this.getOnlineResourceType(), null, "onlineResource", null, 0, 1, ContactType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContactType_HoursOfService(), theXMLTypePackage.getString(), "hoursOfService", null, 0, 1, ContactType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContactType_ContactInstructions(), theXMLTypePackage.getString(), "contactInstructions", null, 0, 1, ContactType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dcpTypeEClass, DCPType.class, "DCPType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDCPType_HTTP(), this.getHTTPType(), null, "hTTP", null, 0, 1, DCPType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(descriptionTypeEClass, DescriptionType.class, "DescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescriptionType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDescriptionType_Abstract(), theXMLTypePackage.getString(), "abstract", null, 0, 1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDescriptionType_Keywords(), this.getKeywordsType(), null, "keywords", null, 0, -1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Abstract(), theXMLTypePackage.getString(), "abstract", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ContactInfo(), this.getContactType(), null, "contactInfo", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_IndividualName(), theXMLTypePackage.getString(), "individualName", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Keywords(), this.getKeywordsType(), null, "keywords", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_OrganisationName(), theXMLTypePackage.getString(), "organisationName", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PointOfContact(), this.getResponsiblePartyType(), null, "pointOfContact", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_PositionName(), theXMLTypePackage.getString(), "positionName", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Role(), this.getCodeType(), null, "role", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Title(), theXMLTypePackage.getString(), "title", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AbstractMetaData(), theEcorePackage.getEObject(), null, "abstractMetaData", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_AccessConstraints(), theXMLTypePackage.getString(), "accessConstraints", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_AvailableCRS(), theXMLTypePackage.getAnyURI(), "availableCRS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_BoundingBox(), this.getBoundingBoxType(), null, "boundingBox", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Dcp(), this.getDCPType(), null, "dcp", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Exception(), this.getExceptionType(), null, "exception", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ExceptionReport(), this.getExceptionReportType(), null, "exceptionReport", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ExtendedCapabilities(), theEcorePackage.getEObject(), null, "extendedCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Fees(), theXMLTypePackage.getString(), "fees", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Http(), this.getHTTPType(), null, "http", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Identifier(), this.getCodeType(), null, "identifier", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Language(), theXMLTypePackage.getLanguage(), "language", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Metadata(), this.getMetadataType(), null, "metadata", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Operation(), this.getOperationType(), null, "operation", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_OperationsMetadata(), this.getOperationsMetadataType(), null, "operationsMetadata", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_OutputFormat(), this.getMimeType(), "outputFormat", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ServiceIdentification(), this.getServiceIdentificationType(), null, "serviceIdentification", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ServiceProvider(), this.getServiceProviderType(), null, "serviceProvider", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_SupportedCRS(), theXMLTypePackage.getAnyURI(), "supportedCRS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_WgS84BoundingBox(), this.getWGS84BoundingBoxType(), null, "wgS84BoundingBox", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(domainTypeEClass, DomainType.class, "DomainType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDomainType_Value(), theXMLTypePackage.getString(), "value", null, 1, 1, DomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDomainType_Metadata(), this.getMetadataType(), null, "metadata", null, 0, -1, DomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDomainType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, DomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(exceptionReportTypeEClass, ExceptionReportType.class, "ExceptionReportType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExceptionReportType_Exception(), this.getExceptionType(), null, "exception", null, 1, -1, ExceptionReportType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExceptionReportType_Language(), theXMLTypePackage.getLanguage(), "language", null, 0, 1, ExceptionReportType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExceptionReportType_Version(), theXMLTypePackage.getString(), "version", null, 1, 1, ExceptionReportType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(exceptionTypeEClass, ExceptionType.class, "ExceptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getExceptionType_ExceptionText(), this.getMimeType(), "exceptionText", null, 0, 1, ExceptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExceptionType_ExceptionCode(), theXMLTypePackage.getString(), "exceptionCode", null, 1, 1, ExceptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExceptionType_Locator(), theXMLTypePackage.getString(), "locator", null, 0, 1, ExceptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetCapabilitiesType_AcceptVersions(), this.getAcceptVersionsType(), null, "acceptVersions", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetCapabilitiesType_Sections(), this.getSectionsType(), null, "sections", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetCapabilitiesType_AcceptFormats(), this.getAcceptFormatsType(), null, "acceptFormats", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_UpdateSequence(), this.getUpdateSequenceType(), "updateSequence", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_BaseUrl(), theXMLTypePackage.getString(), "baseUrl", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_Namespace(), theXMLTypePackage.getString(), "namespace", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_ExtendedProperties(), this.getMap(), "extendedProperties", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(httpTypeEClass, HTTPType.class, "HTTPType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getHTTPType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, HTTPType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getHTTPType_Get(), this.getRequestMethodType(), null, "get", null, 0, -1, HTTPType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getHTTPType_Post(), this.getRequestMethodType(), null, "post", null, 0, -1, HTTPType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(identificationTypeEClass, IdentificationType.class, "IdentificationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIdentificationType_Identifier(), this.getCodeType(), null, "identifier", null, 0, 1, IdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIdentificationType_BoundingBoxGroup(), theEcorePackage.getEFeatureMapEntry(), "boundingBoxGroup", null, 0, -1, IdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getIdentificationType_BoundingBox(), this.getBoundingBoxType(), null, "boundingBox", null, 0, -1, IdentificationType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getIdentificationType_OutputFormat(), this.getMimeType(), "outputFormat", null, 0, 1, IdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIdentificationType_AvailableCRSGroup(), theEcorePackage.getEFeatureMapEntry(), "availableCRSGroup", null, 0, -1, IdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIdentificationType_AvailableCRS(), theXMLTypePackage.getAnyURI(), "availableCRS", null, 0, 1, IdentificationType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getIdentificationType_Metadata(), this.getMetadataType(), null, "metadata", null, 0, -1, IdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(keywordsTypeEClass, KeywordsType.class, "KeywordsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getKeywordsType_Keyword(), this.getMimeType(), "keyword", null, 0, 1, KeywordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getKeywordsType_Type(), this.getCodeType(), null, "type", null, 0, 1, KeywordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(metadataTypeEClass, MetadataType.class, "MetadataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMetadataType_AbstractMetaDataGroup(), theEcorePackage.getEFeatureMapEntry(), "abstractMetaDataGroup", null, 0, -1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMetadataType_AbstractMetaData(), theEcorePackage.getEObject(), null, "abstractMetaData", null, 0, 1, MetadataType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataType_About(), theXMLTypePackage.getAnyURI(), "about", null, 0, 1, MetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(onlineResourceTypeEClass, OnlineResourceType.class, "OnlineResourceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOnlineResourceType_Href(), ecorePackage.getEString(), "href", null, 0, 1, OnlineResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(operationTypeEClass, OperationType.class, "OperationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOperationType_DCP(), this.getDCPType(), null, "dCP", null, 1, -1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationType_Parameter(), this.getDomainType(), null, "parameter", null, 0, -1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationType_Constraint(), this.getDomainType(), null, "constraint", null, 0, -1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationType_Metadata(), this.getMetadataType(), null, "metadata", null, 0, -1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOperationType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(operationsMetadataTypeEClass, OperationsMetadataType.class, "OperationsMetadataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOperationsMetadataType_Operation(), this.getOperationType(), null, "operation", null, 2, -1, OperationsMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationsMetadataType_Parameter(), this.getDomainType(), null, "parameter", null, 0, -1, OperationsMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationsMetadataType_Constraint(), this.getDomainType(), null, "constraint", null, 0, -1, OperationsMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationsMetadataType_ExtendedCapabilities(), this.getObject(), null, "extendedCapabilities", null, 0, 1, OperationsMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(requestMethodTypeEClass, RequestMethodType.class, "RequestMethodType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRequestMethodType_Constraint(), this.getDomainType(), null, "constraint", null, 0, -1, RequestMethodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(responsiblePartySubsetTypeEClass, ResponsiblePartySubsetType.class, "ResponsiblePartySubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getResponsiblePartySubsetType_IndividualName(), theXMLTypePackage.getString(), "individualName", null, 0, 1, ResponsiblePartySubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResponsiblePartySubsetType_PositionName(), theXMLTypePackage.getString(), "positionName", null, 0, 1, ResponsiblePartySubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getResponsiblePartySubsetType_ContactInfo(), this.getContactType(), null, "contactInfo", null, 0, 1, ResponsiblePartySubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getResponsiblePartySubsetType_Role(), this.getCodeType(), null, "role", null, 0, 1, ResponsiblePartySubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(responsiblePartyTypeEClass, ResponsiblePartyType.class, "ResponsiblePartyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getResponsiblePartyType_IndividualName(), theXMLTypePackage.getString(), "individualName", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResponsiblePartyType_OrganisationName(), theXMLTypePackage.getString(), "organisationName", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResponsiblePartyType_PositionName(), theXMLTypePackage.getString(), "positionName", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getResponsiblePartyType_ContactInfo(), this.getContactType(), null, "contactInfo", null, 0, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getResponsiblePartyType_Role(), this.getCodeType(), null, "role", null, 1, 1, ResponsiblePartyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sectionsTypeEClass, SectionsType.class, "SectionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSectionsType_Section(), this.getMimeType(), "section", null, 0, 1, SectionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(serviceIdentificationTypeEClass, ServiceIdentificationType.class, "ServiceIdentificationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getServiceIdentificationType_ServiceType(), this.getCodeType(), null, "serviceType", null, 1, 1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceIdentificationType_ServiceTypeVersion(), this.getVersionType(), "serviceTypeVersion", null, 1, 1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceIdentificationType_Fees(), theXMLTypePackage.getString(), "fees", null, 0, 1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceIdentificationType_AccessConstraints(), theXMLTypePackage.getString(), "accessConstraints", null, 0, 1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(serviceProviderTypeEClass, ServiceProviderType.class, "ServiceProviderType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getServiceProviderType_ProviderName(), theXMLTypePackage.getString(), "providerName", null, 1, 1, ServiceProviderType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getServiceProviderType_ProviderSite(), this.getOnlineResourceType(), null, "providerSite", null, 0, 1, ServiceProviderType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getServiceProviderType_ServiceContact(), this.getResponsiblePartySubsetType(), null, "serviceContact", null, 1, 1, ServiceProviderType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(telephoneTypeEClass, TelephoneType.class, "TelephoneType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTelephoneType_Voice(), theXMLTypePackage.getString(), "voice", null, 0, 1, TelephoneType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTelephoneType_Facsimile(), theXMLTypePackage.getString(), "facsimile", null, 0, 1, TelephoneType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wgs84BoundingBoxTypeEClass, WGS84BoundingBoxType.class, "WGS84BoundingBoxType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(objectEClass, Object.class, "Object", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

        // Initialize data types
        initEDataType(mimeTypeEDataType, String.class, "MimeType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(versionTypeEDataType, String.class, "VersionType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(positionTypeEDataType, List.class, "PositionType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(updateSequenceTypeEDataType, String.class, "UpdateSequenceType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(mapEDataType, Map.class, "Map", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
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
          (acceptFormatsTypeEClass, 
           source, 
           new String[] {
             "name", "AcceptFormatsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (acceptVersionsTypeEClass, 
           source, 
           new String[] {
             "name", "AcceptVersionsType",
             "kind", "elementOnly"
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
             "name", "DeliveryPoint",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getAddressType_City(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "City",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getAddressType_AdministrativeArea(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AdministrativeArea",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getAddressType_PostalCode(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PostalCode",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getAddressType_Country(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Country",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getAddressType_ElectronicMailAddress(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ElectronicMailAddress",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (boundingBoxTypeEClass, 
           source, 
           new String[] {
             "name", "BoundingBoxType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getBoundingBoxType_LowerCorner(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LowerCorner",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getBoundingBoxType_UpperCorner(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UpperCorner",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getBoundingBoxType_Crs(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "crs"
           });			
        addAnnotation
          (getBoundingBoxType_Dimensions(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "dimensions"
           });			
        addAnnotation
          (capabilitiesBaseTypeEClass, 
           source, 
           new String[] {
             "name", "CapabilitiesBaseType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getCapabilitiesBaseType_ServiceIdentification(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceIdentification",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getCapabilitiesBaseType_ServiceProvider(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceProvider",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getCapabilitiesBaseType_OperationsMetadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OperationsMetadata",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getCapabilitiesBaseType_UpdateSequence(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "updateSequence"
           });		
        addAnnotation
          (getCapabilitiesBaseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });		
        addAnnotation
          (codeTypeEClass, 
           source, 
           new String[] {
             "name", "CodeType",
             "kind", "simple"
           });			
        addAnnotation
          (getCodeType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (getCodeType_CodeSpace(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "codeSpace"
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
             "name", "Phone",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getContactType_Address(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Address",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getContactType_OnlineResource(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OnlineResource",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getContactType_HoursOfService(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "HoursOfService",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getContactType_ContactInstructions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ContactInstructions",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (dcpTypeEClass, 
           source, 
           new String[] {
             "name", "DCP_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDCPType_HTTP(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "HTTP",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (descriptionTypeEClass, 
           source, 
           new String[] {
             "name", "DescriptionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDescriptionType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDescriptionType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDescriptionType_Keywords(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Keywords",
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
          (getDocumentRoot_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ContactInfo(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ContactInfo",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_IndividualName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "IndividualName",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Keywords(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Keywords",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_OrganisationName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OrganisationName",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_PointOfContact(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PointOfContact",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_PositionName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PositionName",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Role(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Role",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_AbstractMetaData(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractMetaData",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_AccessConstraints(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AccessConstraints",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_AvailableCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AvailableCRS",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Dcp(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DCP",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Exception(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Exception",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_ExceptionReport(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ExceptionReport",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ExtendedCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ExtendedCapabilities",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Fees(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Fees",
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
          (getDocumentRoot_Http(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "HTTP",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Language(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Language",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Operation(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Operation",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_OperationsMetadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OperationsMetadata",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_OutputFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OutputFormat",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ServiceIdentification(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceIdentification",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ServiceProvider(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceProvider",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_SupportedCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedCRS",
             "namespace", "##targetNamespace",
             "affiliation", "AvailableCRS"
           });			
        addAnnotation
          (getDocumentRoot_WgS84BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WGS84BoundingBox",
             "namespace", "##targetNamespace",
             "affiliation", "BoundingBox"
           });		
        addAnnotation
          (domainTypeEClass, 
           source, 
           new String[] {
             "name", "DomainType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDomainType_Value(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Value",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDomainType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDomainType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });			
        addAnnotation
          (exceptionReportTypeEClass, 
           source, 
           new String[] {
             "name", "ExceptionReport_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getExceptionReportType_Exception(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Exception",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getExceptionReportType_Language(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "language"
           });			
        addAnnotation
          (getExceptionReportType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });			
        addAnnotation
          (exceptionTypeEClass, 
           source, 
           new String[] {
             "name", "ExceptionType",
             "kind", "elementOnly"
           });				
        addAnnotation
          (getExceptionType_ExceptionCode(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "exceptionCode"
           });			
        addAnnotation
          (getExceptionType_Locator(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "locator"
           });			
        addAnnotation
          (getCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "GetCapabilitiesType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetCapabilitiesType_AcceptVersions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AcceptVersions",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCapabilitiesType_Sections(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Sections",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCapabilitiesType_AcceptFormats(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AcceptFormats",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCapabilitiesType_UpdateSequence(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "updateSequence"
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
          (identificationTypeEClass, 
           source, 
           new String[] {
             "name", "IdentificationType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getIdentificationType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getIdentificationType_BoundingBoxGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "BoundingBox:group",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getIdentificationType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "##targetNamespace",
             "group", "BoundingBox:group"
           });			
        addAnnotation
          (getIdentificationType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OutputFormat",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getIdentificationType_AvailableCRSGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "AvailableCRS:group",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getIdentificationType_AvailableCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AvailableCRS",
             "namespace", "##targetNamespace",
             "group", "AvailableCRS:group"
           });			
        addAnnotation
          (getIdentificationType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (keywordsTypeEClass, 
           source, 
           new String[] {
             "name", "KeywordsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getKeywordsType_Type(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Type",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (metadataTypeEClass, 
           source, 
           new String[] {
             "name", "MetadataType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getMetadataType_AbstractMetaDataGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "AbstractMetaData:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getMetadataType_AbstractMetaData(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractMetaData",
             "namespace", "##targetNamespace",
             "group", "AbstractMetaData:group"
           });		
        addAnnotation
          (getMetadataType_About(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "about"
           });			
        addAnnotation
          (onlineResourceTypeEClass, 
           source, 
           new String[] {
             "name", "OnlineResourceType",
             "kind", "empty"
           });			
        addAnnotation
          (operationTypeEClass, 
           source, 
           new String[] {
             "name", "Operation_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getOperationType_DCP(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DCP",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationType_Parameter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Parameter",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });			
        addAnnotation
          (operationsMetadataTypeEClass, 
           source, 
           new String[] {
             "name", "OperationsMetadata_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getOperationsMetadataType_Operation(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Operation",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationsMetadataType_Parameter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Parameter",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationsMetadataType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (requestMethodTypeEClass, 
           source, 
           new String[] {
             "name", "RequestMethodType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getRequestMethodType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (responsiblePartySubsetTypeEClass, 
           source, 
           new String[] {
             "name", "ResponsiblePartySubsetType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getResponsiblePartySubsetType_IndividualName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "IndividualName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResponsiblePartySubsetType_PositionName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PositionName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResponsiblePartySubsetType_ContactInfo(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ContactInfo",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResponsiblePartySubsetType_Role(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Role",
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
             "name", "IndividualName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResponsiblePartyType_OrganisationName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OrganisationName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResponsiblePartyType_PositionName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PositionName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResponsiblePartyType_ContactInfo(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ContactInfo",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResponsiblePartyType_Role(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Role",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (sectionsTypeEClass, 
           source, 
           new String[] {
             "name", "SectionsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (serviceIdentificationTypeEClass, 
           source, 
           new String[] {
             "name", "ServiceIdentification_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getServiceIdentificationType_ServiceType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceType",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceIdentificationType_ServiceTypeVersion(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceTypeVersion",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceIdentificationType_Fees(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Fees",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceIdentificationType_AccessConstraints(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AccessConstraints",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (serviceProviderTypeEClass, 
           source, 
           new String[] {
             "name", "ServiceProvider_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getServiceProviderType_ProviderName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProviderName",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceProviderType_ProviderSite(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProviderSite",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceProviderType_ServiceContact(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceContact",
             "namespace", "##targetNamespace"
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
             "name", "Voice",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getTelephoneType_Facsimile(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Facsimile",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (wgs84BoundingBoxTypeEClass, 
           source, 
           new String[] {
             "name", "WGS84BoundingBoxType",
             "kind", "elementOnly"
           });	
    }

} //Ows10PackageImpl
