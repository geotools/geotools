/**
 */
package net.opengis.wps20.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.ows20.Ows20Package;

import net.opengis.wps20.BodyReferenceType;
import net.opengis.wps20.BoundingBoxDataType;
import net.opengis.wps20.ComplexDataType;
import net.opengis.wps20.ContentsType;
import net.opengis.wps20.DataDescriptionType;
import net.opengis.wps20.DataInputType;
import net.opengis.wps20.DataOutputType;
import net.opengis.wps20.DataTransmissionModeType;
import net.opengis.wps20.DataType;
import net.opengis.wps20.DescribeProcessType;
import net.opengis.wps20.DescriptionType;
import net.opengis.wps20.DismissType;
import net.opengis.wps20.DocumentRoot;
import net.opengis.wps20.ExecuteRequestType;
import net.opengis.wps20.ExtensionType;
import net.opengis.wps20.FormatType;
import net.opengis.wps20.GenericInputType;
import net.opengis.wps20.GenericOutputType;
import net.opengis.wps20.GenericProcessType;
import net.opengis.wps20.GetCapabilitiesType;
import net.opengis.wps20.GetResultType;
import net.opengis.wps20.GetStatusType;
import net.opengis.wps20.InputDescriptionType;
import net.opengis.wps20.JobControlOptionsTypeMember0;
import net.opengis.wps20.LiteralDataDomainType;
import net.opengis.wps20.LiteralDataDomainType1;
import net.opengis.wps20.LiteralDataType;
import net.opengis.wps20.LiteralValueType;
import net.opengis.wps20.ModeType;
import net.opengis.wps20.OutputDefinitionType;
import net.opengis.wps20.OutputDescriptionType;
import net.opengis.wps20.ProcessDescriptionType;
import net.opengis.wps20.ProcessOfferingType;
import net.opengis.wps20.ProcessOfferingsType;
import net.opengis.wps20.ProcessSummaryType;
import net.opengis.wps20.ReferenceType;
import net.opengis.wps20.RequestBaseType;
import net.opengis.wps20.ResponseType;
import net.opengis.wps20.ResultType;
import net.opengis.wps20.StatusInfoType;
import net.opengis.wps20.StatusTypeMember0;
import net.opengis.wps20.SupportedCRSType;
import net.opengis.wps20.WPSCapabilitiesType;
import net.opengis.wps20.Wps20Factory;
import net.opengis.wps20.Wps20Package;

import net.opengis.wps20.util.Wps20Validator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.namespace.XMLNamespacePackage;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.w3._2001.schema.SchemaPackage;

import org.w3._2001.schema.impl.SchemaPackageImpl;

import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wps20PackageImpl extends EPackageImpl implements Wps20Package {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass bodyReferenceTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass boundingBoxDataTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass complexDataTypeEClass = null;

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
	private EClass dataDescriptionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataInputTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataOutputTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass describeProcessTypeEClass = null;

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
	private EClass dismissTypeEClass = null;

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
	private EClass executeRequestTypeEClass = null;

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
	private EClass formatTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass genericInputTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass genericOutputTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass genericProcessTypeEClass = null;

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
	private EClass getResultTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass getStatusTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inputDescriptionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass literalDataDomainTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass literalDataDomainType1EClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass literalDataTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass literalValueTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass outputDefinitionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass outputDescriptionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass processDescriptionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass processOfferingsTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass processOfferingTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass processSummaryTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referenceTypeEClass = null;

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
	private EClass resultTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass statusInfoTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass supportedCRSTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass wpsCapabilitiesTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum dataTransmissionModeTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum jobControlOptionsTypeMember0EEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum modeTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum responseTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum statusTypeMember0EEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dataTransmissionModeTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType jobControlOptionsTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType jobControlOptionsType1EDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType jobControlOptionsTypeMember0ObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType jobControlOptionsTypeMember1EDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType modeTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType outputTransmissionTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType percentCompletedTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType responseTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType statusTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType statusTypeMember0ObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType statusTypeMember1EDataType = null;

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
	 * @see net.opengis.wps20.Wps20Package#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Wps20PackageImpl() {
		super(eNS_URI, Wps20Factory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link Wps20Package#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Wps20Package init() {
		if (isInited) return (Wps20Package)EPackage.Registry.INSTANCE.getEPackage(Wps20Package.eNS_URI);

		// Obtain or create and register package
		Object registeredWps20Package = EPackage.Registry.INSTANCE.get(eNS_URI);
		Wps20PackageImpl theWps20Package = registeredWps20Package instanceof Wps20PackageImpl ? (Wps20PackageImpl)registeredWps20Package : new Wps20PackageImpl();

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();
		EcorePackage.eINSTANCE.eClass();
		XlinkPackage.eINSTANCE.eClass();
		Ows20Package.eINSTANCE.eClass();
		XMLNamespacePackage.eINSTANCE.eClass();
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SchemaPackage.eNS_URI);
		SchemaPackageImpl theSchemaPackage = (SchemaPackageImpl)(registeredPackage instanceof SchemaPackageImpl ? registeredPackage : SchemaPackage.eINSTANCE);

		// Create package meta-data objects
		theWps20Package.createPackageContents();
		theSchemaPackage.createPackageContents();

		// Initialize created meta-data
		theWps20Package.initializePackageContents();
		theSchemaPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theWps20Package,
			 new EValidator.Descriptor() {
				 public EValidator getEValidator() {
					 return Wps20Validator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theWps20Package.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Wps20Package.eNS_URI, theWps20Package);
		return theWps20Package;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBodyReferenceType() {
		return bodyReferenceTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBodyReferenceType_Href() {
		return (EAttribute)bodyReferenceTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBoundingBoxDataType() {
		return boundingBoxDataTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBoundingBoxDataType_SupportedCRS() {
		return (EReference)boundingBoxDataTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComplexDataType() {
		return complexDataTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComplexDataType_Any() {
		return (EAttribute)complexDataTypeEClass.getEStructuralFeatures().get(0);
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
	public EReference getContentsType_ProcessSummary() {
		return (EReference)contentsTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataDescriptionType() {
		return dataDescriptionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataDescriptionType_Format() {
		return (EReference)dataDescriptionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataInputType() {
		return dataInputTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataInputType_Data() {
		return (EReference)dataInputTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataInputType_Reference() {
		return (EReference)dataInputTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataInputType_Input() {
		return (EReference)dataInputTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataInputType_Id() {
		return (EAttribute)dataInputTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataOutputType() {
		return dataOutputTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataOutputType_Data() {
		return (EReference)dataOutputTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataOutputType_Reference() {
		return (EReference)dataOutputTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataOutputType_Output() {
		return (EReference)dataOutputTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataOutputType_Id() {
		return (EAttribute)dataOutputTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataType() {
		return dataTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataType_Encoding() {
		return (EAttribute)dataTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataType_MimeType() {
		return (EAttribute)dataTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataType_Schema() {
		return (EAttribute)dataTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDescribeProcessType() {
		return describeProcessTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDescribeProcessType_Identifier() {
		return (EReference)describeProcessTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDescribeProcessType_Lang() {
		return (EAttribute)describeProcessTypeEClass.getEStructuralFeatures().get(1);
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
	public EClass getDismissType() {
		return dismissTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDismissType_JobID() {
		return (EAttribute)dismissTypeEClass.getEStructuralFeatures().get(0);
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
	public EReference getDocumentRoot_BoundingBoxData() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_DataDescription() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Capabilities() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_ComplexData() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Contents() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Data() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_DescribeProcess() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Dismiss() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Execute() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_ExpirationDate() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Format() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_GenericProcess() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_GetCapabilities() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_GetResult() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_GetStatus() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_JobID() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_LiteralData() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_LiteralValue() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Process() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_ProcessOffering() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_ProcessOfferings() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Reference() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Result() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_StatusInfo() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(26);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_SupportedCRS() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(27);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExecuteRequestType() {
		return executeRequestTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExecuteRequestType_Identifier() {
		return (EReference)executeRequestTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExecuteRequestType_Input() {
		return (EReference)executeRequestTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExecuteRequestType_Output() {
		return (EReference)executeRequestTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExecuteRequestType_Mode() {
		return (EAttribute)executeRequestTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExecuteRequestType_Response() {
		return (EAttribute)executeRequestTypeEClass.getEStructuralFeatures().get(4);
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
	public EClass getFormatType() {
		return formatTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormatType_Default() {
		return (EAttribute)formatTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormatType_Encoding() {
		return (EAttribute)formatTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormatType_MaximumMegabytes() {
		return (EAttribute)formatTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormatType_MimeType() {
		return (EAttribute)formatTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormatType_Schema() {
		return (EAttribute)formatTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGenericInputType() {
		return genericInputTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGenericInputType_Input() {
		return (EReference)genericInputTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGenericInputType_MaxOccurs() {
		return (EAttribute)genericInputTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGenericInputType_MinOccurs() {
		return (EAttribute)genericInputTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGenericOutputType() {
		return genericOutputTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGenericOutputType_Output() {
		return (EReference)genericOutputTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGenericProcessType() {
		return genericProcessTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGenericProcessType_Input() {
		return (EReference)genericProcessTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGenericProcessType_Output() {
		return (EReference)genericProcessTypeEClass.getEStructuralFeatures().get(1);
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
	public EClass getGetResultType() {
		return getResultTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetResultType_JobID() {
		return (EAttribute)getResultTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGetStatusType() {
		return getStatusTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetStatusType_JobID() {
		return (EAttribute)getStatusTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInputDescriptionType() {
		return inputDescriptionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInputDescriptionType_DataDescriptionGroup() {
		return (EAttribute)inputDescriptionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInputDescriptionType_DataDescription() {
		return (EReference)inputDescriptionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInputDescriptionType_Input() {
		return (EReference)inputDescriptionTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInputDescriptionType_MaxOccurs() {
		return (EAttribute)inputDescriptionTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInputDescriptionType_MinOccurs() {
		return (EAttribute)inputDescriptionTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLiteralDataDomainType() {
		return literalDataDomainTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLiteralDataDomainType_AllowedValues() {
		return (EReference)literalDataDomainTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLiteralDataDomainType_AnyValue() {
		return (EReference)literalDataDomainTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLiteralDataDomainType_ValuesReference() {
		return (EReference)literalDataDomainTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLiteralDataDomainType_DataType() {
		return (EReference)literalDataDomainTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLiteralDataDomainType_UOM() {
		return (EReference)literalDataDomainTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLiteralDataDomainType_DefaultValue() {
		return (EReference)literalDataDomainTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLiteralDataDomainType1() {
		return literalDataDomainType1EClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteralDataDomainType1_Default() {
		return (EAttribute)literalDataDomainType1EClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLiteralDataType() {
		return literalDataTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLiteralDataType_LiteralDataDomain() {
		return (EReference)literalDataTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLiteralValueType() {
		return literalValueTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteralValueType_DataType() {
		return (EAttribute)literalValueTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteralValueType_Uom() {
		return (EAttribute)literalValueTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOutputDefinitionType() {
		return outputDefinitionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOutputDefinitionType_Output() {
		return (EReference)outputDefinitionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutputDefinitionType_Encoding() {
		return (EAttribute)outputDefinitionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutputDefinitionType_Id() {
		return (EAttribute)outputDefinitionTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutputDefinitionType_MimeType() {
		return (EAttribute)outputDefinitionTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutputDefinitionType_Schema() {
		return (EAttribute)outputDefinitionTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutputDefinitionType_Transmission() {
		return (EAttribute)outputDefinitionTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOutputDescriptionType() {
		return outputDescriptionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutputDescriptionType_DataDescriptionGroup() {
		return (EAttribute)outputDescriptionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOutputDescriptionType_DataDescription() {
		return (EReference)outputDescriptionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOutputDescriptionType_Output() {
		return (EReference)outputDescriptionTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProcessDescriptionType() {
		return processDescriptionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProcessDescriptionType_Input() {
		return (EReference)processDescriptionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProcessDescriptionType_Output() {
		return (EReference)processDescriptionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessDescriptionType_Lang() {
		return (EAttribute)processDescriptionTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProcessOfferingsType() {
		return processOfferingsTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProcessOfferingsType_ProcessOffering() {
		return (EReference)processOfferingsTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProcessOfferingType() {
		return processOfferingTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProcessOfferingType_Process() {
		return (EReference)processOfferingTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessOfferingType_Any() {
		return (EAttribute)processOfferingTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessOfferingType_JobControlOptions() {
		return (EAttribute)processOfferingTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessOfferingType_OutputTransmission() {
		return (EAttribute)processOfferingTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessOfferingType_ProcessModel() {
		return (EAttribute)processOfferingTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessOfferingType_ProcessVersion() {
		return (EAttribute)processOfferingTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProcessSummaryType() {
		return processSummaryTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessSummaryType_JobControlOptions() {
		return (EAttribute)processSummaryTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessSummaryType_OutputTransmission() {
		return (EAttribute)processSummaryTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessSummaryType_ProcessModel() {
		return (EAttribute)processSummaryTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcessSummaryType_ProcessVersion() {
		return (EAttribute)processSummaryTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReferenceType() {
		return referenceTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceType_Body() {
		return (EReference)referenceTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceType_BodyReference() {
		return (EReference)referenceTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Encoding() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Href() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_MimeType() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Schema() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(5);
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
	public EClass getResultType() {
		return resultTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getResultType_JobID() {
		return (EAttribute)resultTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getResultType_ExpirationDate() {
		return (EAttribute)resultTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResultType_Output() {
		return (EReference)resultTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStatusInfoType() {
		return statusInfoTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatusInfoType_JobID() {
		return (EAttribute)statusInfoTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatusInfoType_Status() {
		return (EAttribute)statusInfoTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatusInfoType_ExpirationDate() {
		return (EAttribute)statusInfoTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatusInfoType_EstimatedCompletion() {
		return (EAttribute)statusInfoTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatusInfoType_NextPoll() {
		return (EAttribute)statusInfoTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatusInfoType_PercentCompleted() {
		return (EAttribute)statusInfoTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSupportedCRSType() {
		return supportedCRSTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSupportedCRSType_Value() {
		return (EAttribute)supportedCRSTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSupportedCRSType_Default() {
		return (EAttribute)supportedCRSTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWPSCapabilitiesType() {
		return wpsCapabilitiesTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWPSCapabilitiesType_Contents() {
		return (EReference)wpsCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWPSCapabilitiesType_Extension() {
		return (EReference)wpsCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWPSCapabilitiesType_Service() {
		return (EAttribute)wpsCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDataTransmissionModeType() {
		return dataTransmissionModeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getJobControlOptionsTypeMember0() {
		return jobControlOptionsTypeMember0EEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getModeType() {
		return modeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getResponseType() {
		return responseTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getStatusTypeMember0() {
		return statusTypeMember0EEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDataTransmissionModeTypeObject() {
		return dataTransmissionModeTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getJobControlOptionsType() {
		return jobControlOptionsTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getJobControlOptionsType1() {
		return jobControlOptionsType1EDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getJobControlOptionsTypeMember0Object() {
		return jobControlOptionsTypeMember0ObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getJobControlOptionsTypeMember1() {
		return jobControlOptionsTypeMember1EDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getModeTypeObject() {
		return modeTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getOutputTransmissionType() {
		return outputTransmissionTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPercentCompletedType() {
		return percentCompletedTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getResponseTypeObject() {
		return responseTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getStatusType() {
		return statusTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getStatusTypeMember0Object() {
		return statusTypeMember0ObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getStatusTypeMember1() {
		return statusTypeMember1EDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wps20Factory getWps20Factory() {
		return (Wps20Factory)getEFactoryInstance();
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
		bodyReferenceTypeEClass = createEClass(BODY_REFERENCE_TYPE);
		createEAttribute(bodyReferenceTypeEClass, BODY_REFERENCE_TYPE__HREF);

		boundingBoxDataTypeEClass = createEClass(BOUNDING_BOX_DATA_TYPE);
		createEReference(boundingBoxDataTypeEClass, BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS);

		complexDataTypeEClass = createEClass(COMPLEX_DATA_TYPE);
		createEAttribute(complexDataTypeEClass, COMPLEX_DATA_TYPE__ANY);

		contentsTypeEClass = createEClass(CONTENTS_TYPE);
		createEReference(contentsTypeEClass, CONTENTS_TYPE__PROCESS_SUMMARY);

		dataDescriptionTypeEClass = createEClass(DATA_DESCRIPTION_TYPE);
		createEReference(dataDescriptionTypeEClass, DATA_DESCRIPTION_TYPE__FORMAT);

		dataInputTypeEClass = createEClass(DATA_INPUT_TYPE);
		createEReference(dataInputTypeEClass, DATA_INPUT_TYPE__DATA);
		createEReference(dataInputTypeEClass, DATA_INPUT_TYPE__REFERENCE);
		createEReference(dataInputTypeEClass, DATA_INPUT_TYPE__INPUT);
		createEAttribute(dataInputTypeEClass, DATA_INPUT_TYPE__ID);

		dataOutputTypeEClass = createEClass(DATA_OUTPUT_TYPE);
		createEReference(dataOutputTypeEClass, DATA_OUTPUT_TYPE__DATA);
		createEReference(dataOutputTypeEClass, DATA_OUTPUT_TYPE__REFERENCE);
		createEReference(dataOutputTypeEClass, DATA_OUTPUT_TYPE__OUTPUT);
		createEAttribute(dataOutputTypeEClass, DATA_OUTPUT_TYPE__ID);

		dataTypeEClass = createEClass(DATA_TYPE);
		createEAttribute(dataTypeEClass, DATA_TYPE__ENCODING);
		createEAttribute(dataTypeEClass, DATA_TYPE__MIME_TYPE);
		createEAttribute(dataTypeEClass, DATA_TYPE__SCHEMA);

		describeProcessTypeEClass = createEClass(DESCRIBE_PROCESS_TYPE);
		createEReference(describeProcessTypeEClass, DESCRIBE_PROCESS_TYPE__IDENTIFIER);
		createEAttribute(describeProcessTypeEClass, DESCRIBE_PROCESS_TYPE__LANG);

		descriptionTypeEClass = createEClass(DESCRIPTION_TYPE);

		dismissTypeEClass = createEClass(DISMISS_TYPE);
		createEAttribute(dismissTypeEClass, DISMISS_TYPE__JOB_ID);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__BOUNDING_BOX_DATA);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DATA_DESCRIPTION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__CAPABILITIES);
		createEReference(documentRootEClass, DOCUMENT_ROOT__COMPLEX_DATA);
		createEReference(documentRootEClass, DOCUMENT_ROOT__CONTENTS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DATA);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_PROCESS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DISMISS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__EXECUTE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__EXPIRATION_DATE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FORMAT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GENERIC_PROCESS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GET_RESULT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GET_STATUS);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__JOB_ID);
		createEReference(documentRootEClass, DOCUMENT_ROOT__LITERAL_DATA);
		createEReference(documentRootEClass, DOCUMENT_ROOT__LITERAL_VALUE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__PROCESS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__PROCESS_OFFERING);
		createEReference(documentRootEClass, DOCUMENT_ROOT__PROCESS_OFFERINGS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__REFERENCE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__RESULT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__STATUS_INFO);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SUPPORTED_CRS);

		executeRequestTypeEClass = createEClass(EXECUTE_REQUEST_TYPE);
		createEReference(executeRequestTypeEClass, EXECUTE_REQUEST_TYPE__IDENTIFIER);
		createEReference(executeRequestTypeEClass, EXECUTE_REQUEST_TYPE__INPUT);
		createEReference(executeRequestTypeEClass, EXECUTE_REQUEST_TYPE__OUTPUT);
		createEAttribute(executeRequestTypeEClass, EXECUTE_REQUEST_TYPE__MODE);
		createEAttribute(executeRequestTypeEClass, EXECUTE_REQUEST_TYPE__RESPONSE);

		extensionTypeEClass = createEClass(EXTENSION_TYPE);
		createEAttribute(extensionTypeEClass, EXTENSION_TYPE__ANY);

		formatTypeEClass = createEClass(FORMAT_TYPE);
		createEAttribute(formatTypeEClass, FORMAT_TYPE__DEFAULT);
		createEAttribute(formatTypeEClass, FORMAT_TYPE__ENCODING);
		createEAttribute(formatTypeEClass, FORMAT_TYPE__MAXIMUM_MEGABYTES);
		createEAttribute(formatTypeEClass, FORMAT_TYPE__MIME_TYPE);
		createEAttribute(formatTypeEClass, FORMAT_TYPE__SCHEMA);

		genericInputTypeEClass = createEClass(GENERIC_INPUT_TYPE);
		createEReference(genericInputTypeEClass, GENERIC_INPUT_TYPE__INPUT);
		createEAttribute(genericInputTypeEClass, GENERIC_INPUT_TYPE__MAX_OCCURS);
		createEAttribute(genericInputTypeEClass, GENERIC_INPUT_TYPE__MIN_OCCURS);

		genericOutputTypeEClass = createEClass(GENERIC_OUTPUT_TYPE);
		createEReference(genericOutputTypeEClass, GENERIC_OUTPUT_TYPE__OUTPUT);

		genericProcessTypeEClass = createEClass(GENERIC_PROCESS_TYPE);
		createEReference(genericProcessTypeEClass, GENERIC_PROCESS_TYPE__INPUT);
		createEReference(genericProcessTypeEClass, GENERIC_PROCESS_TYPE__OUTPUT);

		getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);

		getResultTypeEClass = createEClass(GET_RESULT_TYPE);
		createEAttribute(getResultTypeEClass, GET_RESULT_TYPE__JOB_ID);

		getStatusTypeEClass = createEClass(GET_STATUS_TYPE);
		createEAttribute(getStatusTypeEClass, GET_STATUS_TYPE__JOB_ID);

		inputDescriptionTypeEClass = createEClass(INPUT_DESCRIPTION_TYPE);
		createEAttribute(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP);
		createEReference(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION);
		createEReference(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__INPUT);
		createEAttribute(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__MAX_OCCURS);
		createEAttribute(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__MIN_OCCURS);

		literalDataDomainTypeEClass = createEClass(LITERAL_DATA_DOMAIN_TYPE);
		createEReference(literalDataDomainTypeEClass, LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES);
		createEReference(literalDataDomainTypeEClass, LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE);
		createEReference(literalDataDomainTypeEClass, LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE);
		createEReference(literalDataDomainTypeEClass, LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE);
		createEReference(literalDataDomainTypeEClass, LITERAL_DATA_DOMAIN_TYPE__UOM);
		createEReference(literalDataDomainTypeEClass, LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE);

		literalDataDomainType1EClass = createEClass(LITERAL_DATA_DOMAIN_TYPE1);
		createEAttribute(literalDataDomainType1EClass, LITERAL_DATA_DOMAIN_TYPE1__DEFAULT);

		literalDataTypeEClass = createEClass(LITERAL_DATA_TYPE);
		createEReference(literalDataTypeEClass, LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN);

		literalValueTypeEClass = createEClass(LITERAL_VALUE_TYPE);
		createEAttribute(literalValueTypeEClass, LITERAL_VALUE_TYPE__DATA_TYPE);
		createEAttribute(literalValueTypeEClass, LITERAL_VALUE_TYPE__UOM);

		outputDefinitionTypeEClass = createEClass(OUTPUT_DEFINITION_TYPE);
		createEReference(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__OUTPUT);
		createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__ENCODING);
		createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__ID);
		createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__MIME_TYPE);
		createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__SCHEMA);
		createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__TRANSMISSION);

		outputDescriptionTypeEClass = createEClass(OUTPUT_DESCRIPTION_TYPE);
		createEAttribute(outputDescriptionTypeEClass, OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP);
		createEReference(outputDescriptionTypeEClass, OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION);
		createEReference(outputDescriptionTypeEClass, OUTPUT_DESCRIPTION_TYPE__OUTPUT);

		processDescriptionTypeEClass = createEClass(PROCESS_DESCRIPTION_TYPE);
		createEReference(processDescriptionTypeEClass, PROCESS_DESCRIPTION_TYPE__INPUT);
		createEReference(processDescriptionTypeEClass, PROCESS_DESCRIPTION_TYPE__OUTPUT);
		createEAttribute(processDescriptionTypeEClass, PROCESS_DESCRIPTION_TYPE__LANG);

		processOfferingsTypeEClass = createEClass(PROCESS_OFFERINGS_TYPE);
		createEReference(processOfferingsTypeEClass, PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING);

		processOfferingTypeEClass = createEClass(PROCESS_OFFERING_TYPE);
		createEReference(processOfferingTypeEClass, PROCESS_OFFERING_TYPE__PROCESS);
		createEAttribute(processOfferingTypeEClass, PROCESS_OFFERING_TYPE__ANY);
		createEAttribute(processOfferingTypeEClass, PROCESS_OFFERING_TYPE__JOB_CONTROL_OPTIONS);
		createEAttribute(processOfferingTypeEClass, PROCESS_OFFERING_TYPE__OUTPUT_TRANSMISSION);
		createEAttribute(processOfferingTypeEClass, PROCESS_OFFERING_TYPE__PROCESS_MODEL);
		createEAttribute(processOfferingTypeEClass, PROCESS_OFFERING_TYPE__PROCESS_VERSION);

		processSummaryTypeEClass = createEClass(PROCESS_SUMMARY_TYPE);
		createEAttribute(processSummaryTypeEClass, PROCESS_SUMMARY_TYPE__JOB_CONTROL_OPTIONS);
		createEAttribute(processSummaryTypeEClass, PROCESS_SUMMARY_TYPE__OUTPUT_TRANSMISSION);
		createEAttribute(processSummaryTypeEClass, PROCESS_SUMMARY_TYPE__PROCESS_MODEL);
		createEAttribute(processSummaryTypeEClass, PROCESS_SUMMARY_TYPE__PROCESS_VERSION);

		referenceTypeEClass = createEClass(REFERENCE_TYPE);
		createEReference(referenceTypeEClass, REFERENCE_TYPE__BODY);
		createEReference(referenceTypeEClass, REFERENCE_TYPE__BODY_REFERENCE);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__ENCODING);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__HREF);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__MIME_TYPE);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__SCHEMA);

		requestBaseTypeEClass = createEClass(REQUEST_BASE_TYPE);
		createEReference(requestBaseTypeEClass, REQUEST_BASE_TYPE__EXTENSION);
		createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__SERVICE);
		createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__VERSION);

		resultTypeEClass = createEClass(RESULT_TYPE);
		createEAttribute(resultTypeEClass, RESULT_TYPE__JOB_ID);
		createEAttribute(resultTypeEClass, RESULT_TYPE__EXPIRATION_DATE);
		createEReference(resultTypeEClass, RESULT_TYPE__OUTPUT);

		statusInfoTypeEClass = createEClass(STATUS_INFO_TYPE);
		createEAttribute(statusInfoTypeEClass, STATUS_INFO_TYPE__JOB_ID);
		createEAttribute(statusInfoTypeEClass, STATUS_INFO_TYPE__STATUS);
		createEAttribute(statusInfoTypeEClass, STATUS_INFO_TYPE__EXPIRATION_DATE);
		createEAttribute(statusInfoTypeEClass, STATUS_INFO_TYPE__ESTIMATED_COMPLETION);
		createEAttribute(statusInfoTypeEClass, STATUS_INFO_TYPE__NEXT_POLL);
		createEAttribute(statusInfoTypeEClass, STATUS_INFO_TYPE__PERCENT_COMPLETED);

		supportedCRSTypeEClass = createEClass(SUPPORTED_CRS_TYPE);
		createEAttribute(supportedCRSTypeEClass, SUPPORTED_CRS_TYPE__VALUE);
		createEAttribute(supportedCRSTypeEClass, SUPPORTED_CRS_TYPE__DEFAULT);

		wpsCapabilitiesTypeEClass = createEClass(WPS_CAPABILITIES_TYPE);
		createEReference(wpsCapabilitiesTypeEClass, WPS_CAPABILITIES_TYPE__CONTENTS);
		createEReference(wpsCapabilitiesTypeEClass, WPS_CAPABILITIES_TYPE__EXTENSION);
		createEAttribute(wpsCapabilitiesTypeEClass, WPS_CAPABILITIES_TYPE__SERVICE);

		// Create enums
		dataTransmissionModeTypeEEnum = createEEnum(DATA_TRANSMISSION_MODE_TYPE);
		jobControlOptionsTypeMember0EEnum = createEEnum(JOB_CONTROL_OPTIONS_TYPE_MEMBER0);
		modeTypeEEnum = createEEnum(MODE_TYPE);
		responseTypeEEnum = createEEnum(RESPONSE_TYPE);
		statusTypeMember0EEnum = createEEnum(STATUS_TYPE_MEMBER0);

		// Create data types
		dataTransmissionModeTypeObjectEDataType = createEDataType(DATA_TRANSMISSION_MODE_TYPE_OBJECT);
		jobControlOptionsTypeEDataType = createEDataType(JOB_CONTROL_OPTIONS_TYPE);
		jobControlOptionsType1EDataType = createEDataType(JOB_CONTROL_OPTIONS_TYPE1);
		jobControlOptionsTypeMember0ObjectEDataType = createEDataType(JOB_CONTROL_OPTIONS_TYPE_MEMBER0_OBJECT);
		jobControlOptionsTypeMember1EDataType = createEDataType(JOB_CONTROL_OPTIONS_TYPE_MEMBER1);
		modeTypeObjectEDataType = createEDataType(MODE_TYPE_OBJECT);
		outputTransmissionTypeEDataType = createEDataType(OUTPUT_TRANSMISSION_TYPE);
		percentCompletedTypeEDataType = createEDataType(PERCENT_COMPLETED_TYPE);
		responseTypeObjectEDataType = createEDataType(RESPONSE_TYPE_OBJECT);
		statusTypeEDataType = createEDataType(STATUS_TYPE);
		statusTypeMember0ObjectEDataType = createEDataType(STATUS_TYPE_MEMBER0_OBJECT);
		statusTypeMember1EDataType = createEDataType(STATUS_TYPE_MEMBER1);
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
		XlinkPackage theXlinkPackage = (XlinkPackage)EPackage.Registry.INSTANCE.getEPackage(XlinkPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage_1 = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
		Ows20Package theOws20Package = (Ows20Package)EPackage.Registry.INSTANCE.getEPackage(Ows20Package.eNS_URI);
		XMLNamespacePackage theXMLNamespacePackage = (XMLNamespacePackage)EPackage.Registry.INSTANCE.getEPackage(XMLNamespacePackage.eNS_URI);
		SchemaPackage theSchemaPackage = (SchemaPackage)EPackage.Registry.INSTANCE.getEPackage(SchemaPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		boundingBoxDataTypeEClass.getESuperTypes().add(this.getDataDescriptionType());
		complexDataTypeEClass.getESuperTypes().add(this.getDataDescriptionType());
		dataTypeEClass.getESuperTypes().add(theXMLTypePackage_1.getAnyType());
		describeProcessTypeEClass.getESuperTypes().add(this.getRequestBaseType());
		descriptionTypeEClass.getESuperTypes().add(theOws20Package.getBasicIdentificationType());
		dismissTypeEClass.getESuperTypes().add(this.getRequestBaseType());
		executeRequestTypeEClass.getESuperTypes().add(this.getRequestBaseType());
		genericInputTypeEClass.getESuperTypes().add(this.getDescriptionType());
		genericOutputTypeEClass.getESuperTypes().add(this.getDescriptionType());
		genericProcessTypeEClass.getESuperTypes().add(this.getDescriptionType());
		getCapabilitiesTypeEClass.getESuperTypes().add(theOws20Package.getGetCapabilitiesType());
		getResultTypeEClass.getESuperTypes().add(this.getRequestBaseType());
		getStatusTypeEClass.getESuperTypes().add(this.getRequestBaseType());
		inputDescriptionTypeEClass.getESuperTypes().add(this.getDescriptionType());
		literalDataDomainType1EClass.getESuperTypes().add(this.getLiteralDataDomainType());
		literalDataTypeEClass.getESuperTypes().add(this.getDataDescriptionType());
		literalValueTypeEClass.getESuperTypes().add(theOws20Package.getValueType());
		outputDescriptionTypeEClass.getESuperTypes().add(this.getDescriptionType());
		processDescriptionTypeEClass.getESuperTypes().add(this.getDescriptionType());
		processSummaryTypeEClass.getESuperTypes().add(this.getDescriptionType());
		wpsCapabilitiesTypeEClass.getESuperTypes().add(theOws20Package.getCapabilitiesBaseType());

		// Initialize classes and features; add operations and parameters
		initEClass(bodyReferenceTypeEClass, BodyReferenceType.class, "BodyReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBodyReferenceType_Href(), theXlinkPackage.getHrefType(), "href", null, 1, 1, BodyReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(boundingBoxDataTypeEClass, BoundingBoxDataType.class, "BoundingBoxDataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBoundingBoxDataType_SupportedCRS(), this.getSupportedCRSType(), null, "supportedCRS", null, 1, -1, BoundingBoxDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(complexDataTypeEClass, ComplexDataType.class, "ComplexDataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getComplexDataType_Any(), ecorePackage.getEFeatureMapEntry(), "any", null, 0, -1, ComplexDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contentsTypeEClass, ContentsType.class, "ContentsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContentsType_ProcessSummary(), this.getProcessSummaryType(), null, "processSummary", null, 1, -1, ContentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataDescriptionTypeEClass, DataDescriptionType.class, "DataDescriptionType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataDescriptionType_Format(), this.getFormatType(), null, "format", null, 1, -1, DataDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataInputTypeEClass, DataInputType.class, "DataInputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataInputType_Data(), this.getDataType(), null, "data", null, 0, 1, DataInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataInputType_Reference(), this.getReferenceType(), null, "reference", null, 0, 1, DataInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataInputType_Input(), this.getDataInputType(), null, "input", null, 0, -1, DataInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDataInputType_Id(), theXMLTypePackage_1.getAnyURI(), "id", null, 1, 1, DataInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataOutputTypeEClass, DataOutputType.class, "DataOutputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataOutputType_Data(), this.getDataType(), null, "data", null, 0, 1, DataOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataOutputType_Reference(), this.getReferenceType(), null, "reference", null, 0, 1, DataOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataOutputType_Output(), this.getDataOutputType(), null, "output", null, 0, 1, DataOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDataOutputType_Id(), theXMLTypePackage_1.getAnyURI(), "id", null, 1, 1, DataOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataTypeEClass, DataType.class, "DataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataType_Encoding(), theXMLTypePackage_1.getAnyURI(), "encoding", null, 0, 1, DataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDataType_MimeType(), theOws20Package.getMimeType(), "mimeType", null, 0, 1, DataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDataType_Schema(), theXMLTypePackage_1.getAnyURI(), "schema", null, 0, 1, DataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(describeProcessTypeEClass, DescribeProcessType.class, "DescribeProcessType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDescribeProcessType_Identifier(), theOws20Package.getCodeType(), null, "identifier", null, 1, -1, DescribeProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeProcessType_Lang(), theXMLNamespacePackage.getLangType(), "lang", null, 0, 1, DescribeProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(descriptionTypeEClass, DescriptionType.class, "DescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dismissTypeEClass, DismissType.class, "DismissType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDismissType_JobID(), theXMLTypePackage_1.getString(), "jobID", null, 1, 1, DismissType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_BoundingBoxData(), this.getBoundingBoxDataType(), null, "boundingBoxData", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_DataDescription(), this.getDataDescriptionType(), null, "dataDescription", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Capabilities(), this.getWPSCapabilitiesType(), null, "capabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_ComplexData(), this.getComplexDataType(), null, "complexData", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Contents(), this.getContentsType(), null, "contents", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Data(), this.getDataType(), null, "data", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_DescribeProcess(), this.getDescribeProcessType(), null, "describeProcess", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Dismiss(), this.getDismissType(), null, "dismiss", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Execute(), this.getExecuteRequestType(), null, "execute", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_ExpirationDate(), theXMLTypePackage_1.getDateTime(), "expirationDate", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Format(), this.getFormatType(), null, "format", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GenericProcess(), this.getGenericProcessType(), null, "genericProcess", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetResult(), this.getGetResultType(), null, "getResult", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetStatus(), this.getGetStatusType(), null, "getStatus", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_JobID(), theXMLTypePackage_1.getString(), "jobID", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_LiteralData(), this.getLiteralDataType(), null, "literalData", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_LiteralValue(), this.getLiteralValueType(), null, "literalValue", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Process(), this.getProcessDescriptionType(), null, "process", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_ProcessOffering(), this.getProcessOfferingType(), null, "processOffering", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_ProcessOfferings(), this.getProcessOfferingsType(), null, "processOfferings", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Reference(), this.getReferenceType(), null, "reference", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Result(), this.getResultType(), null, "result", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_StatusInfo(), this.getStatusInfoType(), null, "statusInfo", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_SupportedCRS(), this.getSupportedCRSType(), null, "supportedCRS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(executeRequestTypeEClass, ExecuteRequestType.class, "ExecuteRequestType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExecuteRequestType_Identifier(), theOws20Package.getCodeType(), null, "identifier", null, 1, 1, ExecuteRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecuteRequestType_Input(), this.getDataInputType(), null, "input", null, 0, -1, ExecuteRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecuteRequestType_Output(), this.getOutputDefinitionType(), null, "output", null, 1, -1, ExecuteRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecuteRequestType_Mode(), this.getModeType(), "mode", null, 1, 1, ExecuteRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecuteRequestType_Response(), this.getResponseType(), "response", null, 1, 1, ExecuteRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(extensionTypeEClass, ExtensionType.class, "ExtensionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExtensionType_Any(), ecorePackage.getEFeatureMapEntry(), "any", null, 0, -1, ExtensionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(formatTypeEClass, FormatType.class, "FormatType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFormatType_Default(), theXMLTypePackage_1.getBoolean(), "default", null, 0, 1, FormatType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormatType_Encoding(), theXMLTypePackage_1.getAnyURI(), "encoding", null, 0, 1, FormatType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormatType_MaximumMegabytes(), theXMLTypePackage_1.getPositiveInteger(), "maximumMegabytes", null, 0, 1, FormatType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormatType_MimeType(), theOws20Package.getMimeType(), "mimeType", null, 0, 1, FormatType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormatType_Schema(), theXMLTypePackage_1.getAnyURI(), "schema", null, 0, 1, FormatType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(genericInputTypeEClass, GenericInputType.class, "GenericInputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGenericInputType_Input(), this.getGenericInputType(), null, "input", null, 0, -1, GenericInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGenericInputType_MaxOccurs(), theSchemaPackage.getAllNNI(), "maxOccurs", "1", 0, 1, GenericInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGenericInputType_MinOccurs(), theXMLTypePackage_1.getNonNegativeInteger(), "minOccurs", "1", 0, 1, GenericInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(genericOutputTypeEClass, GenericOutputType.class, "GenericOutputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGenericOutputType_Output(), this.getGenericOutputType(), null, "output", null, 0, -1, GenericOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(genericProcessTypeEClass, GenericProcessType.class, "GenericProcessType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGenericProcessType_Input(), this.getGenericInputType(), null, "input", null, 0, -1, GenericProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGenericProcessType_Output(), this.getGenericOutputType(), null, "output", null, 1, -1, GenericProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGetCapabilitiesType_Service(), theOws20Package.getServiceType(), "service", "WPS", 1, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getResultTypeEClass, GetResultType.class, "GetResultType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGetResultType_JobID(), theXMLTypePackage_1.getString(), "jobID", null, 1, 1, GetResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getStatusTypeEClass, GetStatusType.class, "GetStatusType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGetStatusType_JobID(), theXMLTypePackage_1.getString(), "jobID", null, 1, 1, GetStatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inputDescriptionTypeEClass, InputDescriptionType.class, "InputDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInputDescriptionType_DataDescriptionGroup(), ecorePackage.getEFeatureMapEntry(), "dataDescriptionGroup", null, 0, 1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInputDescriptionType_DataDescription(), this.getDataDescriptionType(), null, "dataDescription", null, 0, 1, InputDescriptionType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getInputDescriptionType_Input(), this.getInputDescriptionType(), null, "input", null, 0, -1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInputDescriptionType_MaxOccurs(), theSchemaPackage.getAllNNI(), "maxOccurs", "1", 0, 1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInputDescriptionType_MinOccurs(), theXMLTypePackage_1.getNonNegativeInteger(), "minOccurs", "1", 0, 1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(literalDataDomainTypeEClass, LiteralDataDomainType.class, "LiteralDataDomainType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLiteralDataDomainType_AllowedValues(), theOws20Package.getAllowedValuesType(), null, "allowedValues", null, 0, 1, LiteralDataDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLiteralDataDomainType_AnyValue(), theOws20Package.getAnyValueType(), null, "anyValue", null, 0, 1, LiteralDataDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLiteralDataDomainType_ValuesReference(), theOws20Package.getValuesReferenceType(), null, "valuesReference", null, 0, 1, LiteralDataDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLiteralDataDomainType_DataType(), theOws20Package.getDomainMetadataType(), null, "dataType", null, 0, 1, LiteralDataDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLiteralDataDomainType_UOM(), theOws20Package.getDomainMetadataType(), null, "uOM", null, 0, 1, LiteralDataDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLiteralDataDomainType_DefaultValue(), theOws20Package.getValueType(), null, "defaultValue", null, 0, 1, LiteralDataDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(literalDataDomainType1EClass, LiteralDataDomainType1.class, "LiteralDataDomainType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLiteralDataDomainType1_Default(), theXMLTypePackage_1.getBoolean(), "default", null, 0, 1, LiteralDataDomainType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(literalDataTypeEClass, LiteralDataType.class, "LiteralDataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLiteralDataType_LiteralDataDomain(), this.getLiteralDataDomainType1(), null, "literalDataDomain", null, 1, -1, LiteralDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(literalValueTypeEClass, LiteralValueType.class, "LiteralValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLiteralValueType_DataType(), theXMLTypePackage_1.getAnyURI(), "dataType", null, 0, 1, LiteralValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteralValueType_Uom(), theXMLTypePackage_1.getAnyURI(), "uom", null, 0, 1, LiteralValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(outputDefinitionTypeEClass, OutputDefinitionType.class, "OutputDefinitionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOutputDefinitionType_Output(), this.getOutputDefinitionType(), null, "output", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOutputDefinitionType_Encoding(), theXMLTypePackage_1.getAnyURI(), "encoding", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOutputDefinitionType_Id(), theXMLTypePackage_1.getAnyURI(), "id", null, 1, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOutputDefinitionType_MimeType(), theOws20Package.getMimeType(), "mimeType", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOutputDefinitionType_Schema(), theXMLTypePackage_1.getAnyURI(), "schema", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOutputDefinitionType_Transmission(), this.getDataTransmissionModeType(), "transmission", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(outputDescriptionTypeEClass, OutputDescriptionType.class, "OutputDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOutputDescriptionType_DataDescriptionGroup(), ecorePackage.getEFeatureMapEntry(), "dataDescriptionGroup", null, 0, 1, OutputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOutputDescriptionType_DataDescription(), this.getDataDescriptionType(), null, "dataDescription", null, 0, 1, OutputDescriptionType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getOutputDescriptionType_Output(), this.getOutputDescriptionType(), null, "output", null, 0, -1, OutputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(processDescriptionTypeEClass, ProcessDescriptionType.class, "ProcessDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProcessDescriptionType_Input(), this.getInputDescriptionType(), null, "input", null, 0, -1, ProcessDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProcessDescriptionType_Output(), this.getOutputDescriptionType(), null, "output", null, 1, -1, ProcessDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessDescriptionType_Lang(), theXMLNamespacePackage.getLangType(), "lang", null, 0, 1, ProcessDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(processOfferingsTypeEClass, ProcessOfferingsType.class, "ProcessOfferingsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProcessOfferingsType_ProcessOffering(), this.getProcessOfferingType(), null, "processOffering", null, 1, -1, ProcessOfferingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(processOfferingTypeEClass, ProcessOfferingType.class, "ProcessOfferingType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProcessOfferingType_Process(), this.getProcessDescriptionType(), null, "process", null, 0, 1, ProcessOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessOfferingType_Any(), ecorePackage.getEFeatureMapEntry(), "any", null, 0, 1, ProcessOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessOfferingType_JobControlOptions(), this.getJobControlOptionsType1(), "jobControlOptions", null, 1, 1, ProcessOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessOfferingType_OutputTransmission(), this.getOutputTransmissionType(), "outputTransmission", null, 0, 1, ProcessOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessOfferingType_ProcessModel(), theXMLTypePackage_1.getString(), "processModel", "native", 0, 1, ProcessOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessOfferingType_ProcessVersion(), theOws20Package.getVersionType1(), "processVersion", null, 0, 1, ProcessOfferingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(processSummaryTypeEClass, ProcessSummaryType.class, "ProcessSummaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProcessSummaryType_JobControlOptions(), this.getJobControlOptionsType1(), "jobControlOptions", null, 1, 1, ProcessSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessSummaryType_OutputTransmission(), this.getOutputTransmissionType(), "outputTransmission", null, 0, 1, ProcessSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessSummaryType_ProcessModel(), theXMLTypePackage_1.getString(), "processModel", "native", 0, 1, ProcessSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProcessSummaryType_ProcessVersion(), theOws20Package.getVersionType1(), "processVersion", null, 0, 1, ProcessSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(referenceTypeEClass, ReferenceType.class, "ReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getReferenceType_Body(), ecorePackage.getEObject(), null, "body", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getReferenceType_BodyReference(), this.getBodyReferenceType(), null, "bodyReference", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Encoding(), theXMLTypePackage_1.getAnyURI(), "encoding", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Href(), theXlinkPackage.getHrefType(), "href", null, 1, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_MimeType(), theOws20Package.getMimeType(), "mimeType", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Schema(), theXMLTypePackage_1.getAnyURI(), "schema", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(requestBaseTypeEClass, RequestBaseType.class, "RequestBaseType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRequestBaseType_Extension(), ecorePackage.getEObject(), null, "extension", null, 0, -1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRequestBaseType_Service(), theXMLTypePackage_1.getString(), "service", "WPS", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRequestBaseType_Version(), theOws20Package.getVersionType1(), "version", "2.0.0", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resultTypeEClass, ResultType.class, "ResultType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getResultType_JobID(), theXMLTypePackage_1.getString(), "jobID", null, 0, 1, ResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResultType_ExpirationDate(), theXMLTypePackage_1.getDateTime(), "expirationDate", null, 0, 1, ResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResultType_Output(), this.getDataOutputType(), null, "output", null, 1, -1, ResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statusInfoTypeEClass, StatusInfoType.class, "StatusInfoType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStatusInfoType_JobID(), theXMLTypePackage_1.getString(), "jobID", null, 1, 1, StatusInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatusInfoType_Status(), this.getStatusType(), "status", null, 1, 1, StatusInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatusInfoType_ExpirationDate(), theXMLTypePackage_1.getDateTime(), "expirationDate", null, 0, 1, StatusInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatusInfoType_EstimatedCompletion(), theXMLTypePackage_1.getDateTime(), "estimatedCompletion", null, 0, 1, StatusInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatusInfoType_NextPoll(), theXMLTypePackage_1.getDateTime(), "nextPoll", null, 0, 1, StatusInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatusInfoType_PercentCompleted(), this.getPercentCompletedType(), "percentCompleted", null, 0, 1, StatusInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(supportedCRSTypeEClass, SupportedCRSType.class, "SupportedCRSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSupportedCRSType_Value(), theXMLTypePackage_1.getAnyURI(), "value", null, 0, 1, SupportedCRSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSupportedCRSType_Default(), theXMLTypePackage_1.getBoolean(), "default", null, 0, 1, SupportedCRSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wpsCapabilitiesTypeEClass, WPSCapabilitiesType.class, "WPSCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getWPSCapabilitiesType_Contents(), this.getContentsType(), null, "contents", null, 1, 1, WPSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWPSCapabilitiesType_Extension(), this.getExtensionType(), null, "extension", null, 0, 1, WPSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWPSCapabilitiesType_Service(), theXMLTypePackage_1.getAnySimpleType(), "service", "WPS", 1, 1, WPSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(dataTransmissionModeTypeEEnum, DataTransmissionModeType.class, "DataTransmissionModeType");
		addEEnumLiteral(dataTransmissionModeTypeEEnum, DataTransmissionModeType.VALUE);
		addEEnumLiteral(dataTransmissionModeTypeEEnum, DataTransmissionModeType.REFERENCE);

		initEEnum(jobControlOptionsTypeMember0EEnum, JobControlOptionsTypeMember0.class, "JobControlOptionsTypeMember0");
		addEEnumLiteral(jobControlOptionsTypeMember0EEnum, JobControlOptionsTypeMember0.SYNC_EXECUTE);
		addEEnumLiteral(jobControlOptionsTypeMember0EEnum, JobControlOptionsTypeMember0.ASYNC_EXECUTE);

		initEEnum(modeTypeEEnum, ModeType.class, "ModeType");
		addEEnumLiteral(modeTypeEEnum, ModeType.SYNC);
		addEEnumLiteral(modeTypeEEnum, ModeType.ASYNC);
		addEEnumLiteral(modeTypeEEnum, ModeType.AUTO);

		initEEnum(responseTypeEEnum, ResponseType.class, "ResponseType");
		addEEnumLiteral(responseTypeEEnum, ResponseType.RAW);
		addEEnumLiteral(responseTypeEEnum, ResponseType.DOCUMENT);

		initEEnum(statusTypeMember0EEnum, StatusTypeMember0.class, "StatusTypeMember0");
		addEEnumLiteral(statusTypeMember0EEnum, StatusTypeMember0.SUCCEEDED);
		addEEnumLiteral(statusTypeMember0EEnum, StatusTypeMember0.FAILED);
		addEEnumLiteral(statusTypeMember0EEnum, StatusTypeMember0.ACCEPTED);
		addEEnumLiteral(statusTypeMember0EEnum, StatusTypeMember0.RUNNING);

		// Initialize data types
		initEDataType(dataTransmissionModeTypeObjectEDataType, DataTransmissionModeType.class, "DataTransmissionModeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(jobControlOptionsTypeEDataType, Object.class, "JobControlOptionsType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(jobControlOptionsType1EDataType, List.class, "JobControlOptionsType1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(jobControlOptionsTypeMember0ObjectEDataType, JobControlOptionsTypeMember0.class, "JobControlOptionsTypeMember0Object", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(jobControlOptionsTypeMember1EDataType, String.class, "JobControlOptionsTypeMember1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(modeTypeObjectEDataType, ModeType.class, "ModeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(outputTransmissionTypeEDataType, List.class, "OutputTransmissionType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(percentCompletedTypeEDataType, BigInteger.class, "PercentCompletedType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(responseTypeObjectEDataType, ResponseType.class, "ResponseTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(statusTypeEDataType, Object.class, "StatusType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(statusTypeMember0ObjectEDataType, StatusTypeMember0.class, "StatusTypeMember0Object", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(statusTypeMember1EDataType, String.class, "StatusTypeMember1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// null
		createNullAnnotations();
		// http://www.w3.org/XML/1998/namespace
		createNamespaceAnnotations();
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>null</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNullAnnotations() {
		String source = null;
		addAnnotation
		  (this,
		   source,
		   new String[] {
			   "appinfo", "owsAll.xsd\r\nowsGetResourceByID.xsd\r\nowsExceptionReport.xsd\r\nowsDomainType.xsd\r\nowsContents.xsd\r\nowsInputOutputData.xsd\r\nowsAdditionalParameters.xsd\r\nowsCommon.xsd\r\nows19115subset.xsd\r\nowsManifest.xsd\r\nowsDataIdentification.xsd\r\nowsGetCapabilities.xsd\r\nowsServiceIdentification.xsd\r\nowsServiceProvider.xsd\r\nowsOperationsMetadata.xsd"
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
		  (bodyReferenceTypeEClass,
		   source,
		   new String[] {
			   "name", "BodyReference_._type",
			   "kind", "empty"
		   });
		addAnnotation
		  (getBodyReferenceType_Href(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "href",
			   "namespace", "http://www.w3.org/1999/xlink"
		   });
		addAnnotation
		  (boundingBoxDataTypeEClass,
		   source,
		   new String[] {
			   "name", "BoundingBoxData_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getBoundingBoxDataType_SupportedCRS(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "SupportedCRS",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (complexDataTypeEClass,
		   source,
		   new String[] {
			   "name", "ComplexDataType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getComplexDataType_Any(),
		   source,
		   new String[] {
			   "kind", "elementWildcard",
			   "wildcards", "##other",
			   "name", ":1",
			   "processing", "lax"
		   });
		addAnnotation
		  (contentsTypeEClass,
		   source,
		   new String[] {
			   "name", "Contents_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getContentsType_ProcessSummary(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ProcessSummary",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (dataDescriptionTypeEClass,
		   source,
		   new String[] {
			   "name", "DataDescriptionType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getDataDescriptionType_Format(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Format",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (dataInputTypeEClass,
		   source,
		   new String[] {
			   "name", "DataInputType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getDataInputType_Data(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Data",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDataInputType_Reference(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Reference",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDataInputType_Input(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Input",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDataInputType_Id(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "id"
		   });
		addAnnotation
		  (dataOutputTypeEClass,
		   source,
		   new String[] {
			   "name", "DataOutputType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getDataOutputType_Data(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Data",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDataOutputType_Reference(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Reference",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDataOutputType_Output(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Output",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDataOutputType_Id(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "id"
		   });
		addAnnotation
		  (dataTransmissionModeTypeEEnum,
		   source,
		   new String[] {
			   "name", "DataTransmissionModeType"
		   });
		addAnnotation
		  (dataTransmissionModeTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "DataTransmissionModeType:Object",
			   "baseType", "DataTransmissionModeType"
		   });
		addAnnotation
		  (dataTypeEClass,
		   source,
		   new String[] {
			   "name", "Data_._type",
			   "kind", "mixed"
		   });
		addAnnotation
		  (getDataType_Encoding(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "encoding"
		   });
		addAnnotation
		  (getDataType_MimeType(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mimeType"
		   });
		addAnnotation
		  (getDataType_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (describeProcessTypeEClass,
		   source,
		   new String[] {
			   "name", "DescribeProcess_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getDescribeProcessType_Identifier(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Identifier",
			   "namespace", "http://www.opengis.net/ows/2.0"
		   });
		addAnnotation
		  (getDescribeProcessType_Lang(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "lang",
			   "namespace", "http://www.w3.org/XML/1998/namespace"
		   });
		addAnnotation
		  (descriptionTypeEClass,
		   source,
		   new String[] {
			   "name", "DescriptionType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (dismissTypeEClass,
		   source,
		   new String[] {
			   "name", "Dismiss_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getDismissType_JobID(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "JobID",
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
		  (getDocumentRoot_BoundingBoxData(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "BoundingBoxData",
			   "namespace", "##targetNamespace",
			   "affiliation", "DataDescription"
		   });
		addAnnotation
		  (getDocumentRoot_DataDescription(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "DataDescription",
			   "namespace", "##targetNamespace"
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
		  (getDocumentRoot_ComplexData(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ComplexData",
			   "namespace", "##targetNamespace",
			   "affiliation", "DataDescription"
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
		  (getDocumentRoot_Data(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Data",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_DescribeProcess(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "DescribeProcess",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_Dismiss(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Dismiss",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_Execute(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Execute",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_ExpirationDate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ExpirationDate",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_Format(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Format",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_GenericProcess(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "GenericProcess",
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
		  (getDocumentRoot_GetResult(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "GetResult",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_GetStatus(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "GetStatus",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_JobID(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "JobID",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_LiteralData(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "LiteralData",
			   "namespace", "##targetNamespace",
			   "affiliation", "DataDescription"
		   });
		addAnnotation
		  (getDocumentRoot_LiteralValue(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "LiteralValue",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_Process(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Process",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_ProcessOffering(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ProcessOffering",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_ProcessOfferings(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ProcessOfferings",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_Reference(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Reference",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_Result(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Result",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_StatusInfo(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "StatusInfo",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getDocumentRoot_SupportedCRS(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "SupportedCRS",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (executeRequestTypeEClass,
		   source,
		   new String[] {
			   "name", "ExecuteRequestType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getExecuteRequestType_Identifier(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Identifier",
			   "namespace", "http://www.opengis.net/ows/2.0"
		   });
		addAnnotation
		  (getExecuteRequestType_Input(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Input",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getExecuteRequestType_Output(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Output",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getExecuteRequestType_Mode(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mode"
		   });
		addAnnotation
		  (getExecuteRequestType_Response(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "response"
		   });
		addAnnotation
		  (extensionTypeEClass,
		   source,
		   new String[] {
			   "name", "Extension_._type",
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
		  (formatTypeEClass,
		   source,
		   new String[] {
			   "name", "Format_._type",
			   "kind", "empty"
		   });
		addAnnotation
		  (getFormatType_Default(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "default"
		   });
		addAnnotation
		  (getFormatType_Encoding(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "encoding"
		   });
		addAnnotation
		  (getFormatType_MaximumMegabytes(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "maximumMegabytes"
		   });
		addAnnotation
		  (getFormatType_MimeType(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mimeType"
		   });
		addAnnotation
		  (getFormatType_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (genericInputTypeEClass,
		   source,
		   new String[] {
			   "name", "GenericInputType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getGenericInputType_Input(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Input",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getGenericInputType_MaxOccurs(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "maxOccurs"
		   });
		addAnnotation
		  (getGenericInputType_MinOccurs(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "minOccurs"
		   });
		addAnnotation
		  (genericOutputTypeEClass,
		   source,
		   new String[] {
			   "name", "GenericOutputType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getGenericOutputType_Output(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Output",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (genericProcessTypeEClass,
		   source,
		   new String[] {
			   "name", "GenericProcessType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getGenericProcessType_Input(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Input",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getGenericProcessType_Output(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Output",
			   "namespace", "##targetNamespace"
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
		  (getResultTypeEClass,
		   source,
		   new String[] {
			   "name", "GetResult_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getGetResultType_JobID(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "JobID",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getStatusTypeEClass,
		   source,
		   new String[] {
			   "name", "GetStatus_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getGetStatusType_JobID(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "JobID",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (inputDescriptionTypeEClass,
		   source,
		   new String[] {
			   "name", "InputDescriptionType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getInputDescriptionType_DataDescriptionGroup(),
		   source,
		   new String[] {
			   "kind", "group",
			   "name", "DataDescription:group",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getInputDescriptionType_DataDescription(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "DataDescription",
			   "namespace", "##targetNamespace",
			   "group", "DataDescription:group"
		   });
		addAnnotation
		  (getInputDescriptionType_Input(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Input",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getInputDescriptionType_MaxOccurs(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "maxOccurs"
		   });
		addAnnotation
		  (getInputDescriptionType_MinOccurs(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "minOccurs"
		   });
		addAnnotation
		  (jobControlOptionsTypeEDataType,
		   source,
		   new String[] {
			   "name", "JobControlOptionsType",
			   "memberTypes", "JobControlOptionsType_._member_._0 JobControlOptionsType_._member_._1"
		   });
		addAnnotation
		  (jobControlOptionsType1EDataType,
		   source,
		   new String[] {
			   "name", "jobControlOptions_._type",
			   "itemType", "JobControlOptionsType"
		   });
		addAnnotation
		  (jobControlOptionsTypeMember0EEnum,
		   source,
		   new String[] {
			   "name", "JobControlOptionsType_._member_._0"
		   });
		addAnnotation
		  (jobControlOptionsTypeMember0ObjectEDataType,
		   source,
		   new String[] {
			   "name", "JobControlOptionsType_._member_._0:Object",
			   "baseType", "JobControlOptionsType_._member_._0"
		   });
		addAnnotation
		  (jobControlOptionsTypeMember1EDataType,
		   source,
		   new String[] {
			   "name", "JobControlOptionsType_._member_._1",
			   "baseType", "http://www.eclipse.org/emf/2003/XMLType#string"
		   });
		addAnnotation
		  (literalDataDomainTypeEClass,
		   source,
		   new String[] {
			   "name", "LiteralDataDomainType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getLiteralDataDomainType_AllowedValues(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "AllowedValues",
			   "namespace", "http://www.opengis.net/ows/2.0"
		   });
		addAnnotation
		  (getLiteralDataDomainType_AnyValue(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "AnyValue",
			   "namespace", "http://www.opengis.net/ows/2.0"
		   });
		addAnnotation
		  (getLiteralDataDomainType_ValuesReference(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ValuesReference",
			   "namespace", "http://www.opengis.net/ows/2.0"
		   });
		addAnnotation
		  (getLiteralDataDomainType_DataType(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "DataType",
			   "namespace", "http://www.opengis.net/ows/2.0"
		   });
		addAnnotation
		  (getLiteralDataDomainType_UOM(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "UOM",
			   "namespace", "http://www.opengis.net/ows/2.0"
		   });
		addAnnotation
		  (getLiteralDataDomainType_DefaultValue(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "DefaultValue",
			   "namespace", "http://www.opengis.net/ows/2.0"
		   });
		addAnnotation
		  (literalDataDomainType1EClass,
		   source,
		   new String[] {
			   "name", "LiteralDataDomain_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getLiteralDataDomainType1_Default(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "default"
		   });
		addAnnotation
		  (literalDataTypeEClass,
		   source,
		   new String[] {
			   "name", "LiteralDataType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getLiteralDataType_LiteralDataDomain(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "LiteralDataDomain"
		   });
		addAnnotation
		  (literalValueTypeEClass,
		   source,
		   new String[] {
			   "name", "LiteralValue_._type",
			   "kind", "simple"
		   });
		addAnnotation
		  (getLiteralValueType_DataType(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "dataType"
		   });
		addAnnotation
		  (getLiteralValueType_Uom(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "uom"
		   });
		addAnnotation
		  (modeTypeEEnum,
		   source,
		   new String[] {
			   "name", "mode_._type"
		   });
		addAnnotation
		  (modeTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "mode_._type:Object",
			   "baseType", "mode_._type"
		   });
		addAnnotation
		  (outputDefinitionTypeEClass,
		   source,
		   new String[] {
			   "name", "OutputDefinitionType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getOutputDefinitionType_Output(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Output",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOutputDefinitionType_Encoding(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "encoding"
		   });
		addAnnotation
		  (getOutputDefinitionType_Id(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "id"
		   });
		addAnnotation
		  (getOutputDefinitionType_MimeType(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mimeType"
		   });
		addAnnotation
		  (getOutputDefinitionType_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
		   });
		addAnnotation
		  (getOutputDefinitionType_Transmission(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "transmission"
		   });
		addAnnotation
		  (outputDescriptionTypeEClass,
		   source,
		   new String[] {
			   "name", "OutputDescriptionType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getOutputDescriptionType_DataDescriptionGroup(),
		   source,
		   new String[] {
			   "kind", "group",
			   "name", "DataDescription:group",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getOutputDescriptionType_DataDescription(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "DataDescription",
			   "namespace", "##targetNamespace",
			   "group", "DataDescription:group"
		   });
		addAnnotation
		  (getOutputDescriptionType_Output(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Output",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (outputTransmissionTypeEDataType,
		   source,
		   new String[] {
			   "name", "outputTransmission_._type",
			   "itemType", "DataTransmissionModeType"
		   });
		addAnnotation
		  (percentCompletedTypeEDataType,
		   source,
		   new String[] {
			   "name", "PercentCompleted_._type",
			   "baseType", "http://www.eclipse.org/emf/2003/XMLType#integer",
			   "minInclusive", "0",
			   "maxInclusive", "100"
		   });
		addAnnotation
		  (processDescriptionTypeEClass,
		   source,
		   new String[] {
			   "name", "ProcessDescriptionType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getProcessDescriptionType_Input(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Input",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getProcessDescriptionType_Output(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Output",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getProcessDescriptionType_Lang(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "lang",
			   "namespace", "http://www.w3.org/XML/1998/namespace"
		   });
		addAnnotation
		  (processOfferingsTypeEClass,
		   source,
		   new String[] {
			   "name", "ProcessOfferings_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getProcessOfferingsType_ProcessOffering(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ProcessOffering",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (processOfferingTypeEClass,
		   source,
		   new String[] {
			   "name", "ProcessOffering_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getProcessOfferingType_Process(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Process",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getProcessOfferingType_Any(),
		   source,
		   new String[] {
			   "kind", "elementWildcard",
			   "wildcards", "##other",
			   "name", ":1",
			   "processing", "lax"
		   });
		addAnnotation
		  (getProcessOfferingType_JobControlOptions(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "jobControlOptions"
		   });
		addAnnotation
		  (getProcessOfferingType_OutputTransmission(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "outputTransmission"
		   });
		addAnnotation
		  (getProcessOfferingType_ProcessModel(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "processModel"
		   });
		addAnnotation
		  (getProcessOfferingType_ProcessVersion(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "processVersion"
		   });
		addAnnotation
		  (processSummaryTypeEClass,
		   source,
		   new String[] {
			   "name", "ProcessSummaryType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getProcessSummaryType_JobControlOptions(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "jobControlOptions"
		   });
		addAnnotation
		  (getProcessSummaryType_OutputTransmission(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "outputTransmission"
		   });
		addAnnotation
		  (getProcessSummaryType_ProcessModel(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "processModel"
		   });
		addAnnotation
		  (getProcessSummaryType_ProcessVersion(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "processVersion"
		   });
		addAnnotation
		  (referenceTypeEClass,
		   source,
		   new String[] {
			   "name", "ReferenceType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getReferenceType_Body(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Body",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getReferenceType_BodyReference(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "BodyReference",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getReferenceType_Encoding(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "encoding"
		   });
		addAnnotation
		  (getReferenceType_Href(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "href",
			   "namespace", "http://www.w3.org/1999/xlink"
		   });
		addAnnotation
		  (getReferenceType_MimeType(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "mimeType"
		   });
		addAnnotation
		  (getReferenceType_Schema(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "schema"
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
		  (responseTypeEEnum,
		   source,
		   new String[] {
			   "name", "response_._type"
		   });
		addAnnotation
		  (responseTypeObjectEDataType,
		   source,
		   new String[] {
			   "name", "response_._type:Object",
			   "baseType", "response_._type"
		   });
		addAnnotation
		  (resultTypeEClass,
		   source,
		   new String[] {
			   "name", "Result_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getResultType_JobID(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "JobID",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getResultType_ExpirationDate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ExpirationDate",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getResultType_Output(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Output",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (statusInfoTypeEClass,
		   source,
		   new String[] {
			   "name", "StatusInfo_._type",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getStatusInfoType_JobID(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "JobID",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getStatusInfoType_Status(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Status",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getStatusInfoType_ExpirationDate(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "ExpirationDate",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getStatusInfoType_EstimatedCompletion(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "EstimatedCompletion",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getStatusInfoType_NextPoll(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "NextPoll",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getStatusInfoType_PercentCompleted(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "PercentCompleted",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (statusTypeEDataType,
		   source,
		   new String[] {
			   "name", "Status_._type",
			   "memberTypes", "Status_._type_._member_._0 Status_._type_._member_._1"
		   });
		addAnnotation
		  (statusTypeMember0EEnum,
		   source,
		   new String[] {
			   "name", "Status_._type_._member_._0"
		   });
		addAnnotation
		  (statusTypeMember0ObjectEDataType,
		   source,
		   new String[] {
			   "name", "Status_._type_._member_._0:Object",
			   "baseType", "Status_._type_._member_._0"
		   });
		addAnnotation
		  (statusTypeMember1EDataType,
		   source,
		   new String[] {
			   "name", "Status_._type_._member_._1",
			   "baseType", "http://www.eclipse.org/emf/2003/XMLType#string"
		   });
		addAnnotation
		  (supportedCRSTypeEClass,
		   source,
		   new String[] {
			   "name", "SupportedCRS_._type",
			   "kind", "simple"
		   });
		addAnnotation
		  (getSupportedCRSType_Value(),
		   source,
		   new String[] {
			   "name", ":0",
			   "kind", "simple"
		   });
		addAnnotation
		  (getSupportedCRSType_Default(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "default"
		   });
		addAnnotation
		  (wpsCapabilitiesTypeEClass,
		   source,
		   new String[] {
			   "name", "WPSCapabilitiesType",
			   "kind", "elementOnly"
		   });
		addAnnotation
		  (getWPSCapabilitiesType_Contents(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Contents",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getWPSCapabilitiesType_Extension(),
		   source,
		   new String[] {
			   "kind", "element",
			   "name", "Extension",
			   "namespace", "##targetNamespace"
		   });
		addAnnotation
		  (getWPSCapabilitiesType_Service(),
		   source,
		   new String[] {
			   "kind", "attribute",
			   "name", "service"
		   });
	}

} //Wps20PackageImpl
