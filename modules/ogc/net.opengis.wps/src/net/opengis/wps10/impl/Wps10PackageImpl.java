/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.math.BigInteger;

import java.util.Map;
import javax.measure.unit.Unit;
import javax.xml.namespace.QName;
import net.opengis.ows11.Ows11Package;

import net.opengis.wps10.BodyReferenceType;
import net.opengis.wps10.CRSsType;
import net.opengis.wps10.ComplexDataCombinationType;
import net.opengis.wps10.ComplexDataCombinationsType;
import net.opengis.wps10.ComplexDataDescriptionType;
import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.DataInputsType;
import net.opengis.wps10.DataInputsType1;
import net.opengis.wps10.DataType;
import net.opengis.wps10.DefaultType;
import net.opengis.wps10.DefaultType1;
import net.opengis.wps10.DefaultType2;
import net.opengis.wps10.DescribeProcessType;
import net.opengis.wps10.DescriptionType;
import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.DocumentRoot;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.GetCapabilitiesType;
import net.opengis.wps10.HeaderType;
import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.InputType;
import net.opengis.wps10.LanguagesType;
import net.opengis.wps10.LanguagesType1;
import net.opengis.wps10.LiteralDataType;
import net.opengis.wps10.LiteralInputType;
import net.opengis.wps10.LiteralOutputType;
import net.opengis.wps10.MethodType;
import net.opengis.wps10.OutputDataType;
import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.OutputDefinitionsType;
import net.opengis.wps10.OutputDescriptionType;
import net.opengis.wps10.OutputReferenceType;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessFailedType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.ProcessOutputsType;
import net.opengis.wps10.ProcessOutputsType1;
import net.opengis.wps10.ProcessStartedType;
import net.opengis.wps10.RequestBaseType;
import net.opengis.wps10.ResponseBaseType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.StatusType;
import net.opengis.wps10.SupportedCRSsType;
import net.opengis.wps10.SupportedComplexDataInputType;
import net.opengis.wps10.SupportedComplexDataType;
import net.opengis.wps10.SupportedUOMsType;
import net.opengis.wps10.UOMsType;
import net.opengis.wps10.ValuesReferenceType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.WSDLType;
import net.opengis.wps10.Wps10Factory;
import net.opengis.wps10.Wps10Package;

import net.opengis.wps10.util.Wps10Validator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wps10PackageImpl extends EPackageImpl implements Wps10Package {
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
    private EClass complexDataCombinationsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass complexDataCombinationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass complexDataDescriptionTypeEClass = null;

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
    private EClass crSsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dataInputsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dataInputsType1EClass = null;

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
    private EClass defaultTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass defaultType1EClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass defaultType2EClass = null;

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
    private EClass documentOutputDefinitionTypeEClass = null;

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
    private EClass executeResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass executeTypeEClass = null;

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
    private EClass headerTypeEClass = null;

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
    private EClass inputReferenceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass inputTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass languagesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass languagesType1EClass = null;

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
    private EClass literalInputTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass literalOutputTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass outputDataTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass outputDefinitionsTypeEClass = null;

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
    private EClass outputReferenceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass processBriefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass processDescriptionsTypeEClass = null;

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
    private EClass processFailedTypeEClass = null;

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
    private EClass processOutputsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass processOutputsType1EClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass processStartedTypeEClass = null;

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
    private EClass responseBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass responseDocumentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass responseFormTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass statusTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass supportedComplexDataInputTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass supportedComplexDataTypeEClass = null;

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
    private EClass supportedUOMsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass uoMsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valuesReferenceTypeEClass = null;

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
    private EClass wsdlTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unitEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum methodTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType methodTypeObjectEDataType = null;

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
	private EDataType mapEDataType = null;

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
     * @see net.opengis.wps10.Wps10Package#eNS_URI
     * @see #init()
     * @generated
     */
    private Wps10PackageImpl() {
        super(eNS_URI, Wps10Factory.eINSTANCE);
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
     * <p>This method is used to initialize {@link Wps10Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static Wps10Package init() {
        if (isInited) return (Wps10Package)EPackage.Registry.INSTANCE.getEPackage(Wps10Package.eNS_URI);

        // Obtain or create and register package
        Wps10PackageImpl theWps10Package = (Wps10PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Wps10PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Wps10PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows11Package.eINSTANCE.eClass();

        // Create package meta-data objects
        theWps10Package.createPackageContents();

        // Initialize created meta-data
        theWps10Package.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (theWps10Package, 
             new EValidator.Descriptor() {
                 public EValidator getEValidator() {
                     return Wps10Validator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        theWps10Package.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(Wps10Package.eNS_URI, theWps10Package);
        return theWps10Package;
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
    public EClass getComplexDataCombinationsType() {
        return complexDataCombinationsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComplexDataCombinationsType_Format() {
        return (EReference)complexDataCombinationsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComplexDataCombinationType() {
        return complexDataCombinationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComplexDataCombinationType_Format() {
        return (EReference)complexDataCombinationTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComplexDataDescriptionType() {
        return complexDataDescriptionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComplexDataDescriptionType_MimeType() {
        return (EAttribute)complexDataDescriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComplexDataDescriptionType_Encoding() {
        return (EAttribute)complexDataDescriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComplexDataDescriptionType_Schema() {
        return (EAttribute)complexDataDescriptionTypeEClass.getEStructuralFeatures().get(2);
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
    public EAttribute getComplexDataType_Encoding() {
        return (EAttribute)complexDataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComplexDataType_MimeType() {
        return (EAttribute)complexDataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComplexDataType_Schema() {
        return (EAttribute)complexDataTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComplexDataType_Data() {
        return (EAttribute)complexDataTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCRSsType() {
        return crSsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSsType_CRS() {
        return (EAttribute)crSsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDataInputsType() {
        return dataInputsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataInputsType_Input() {
        return (EReference)dataInputsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDataInputsType1() {
        return dataInputsType1EClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataInputsType1_Input() {
        return (EReference)dataInputsType1EClass.getEStructuralFeatures().get(0);
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
    public EReference getDataType_ComplexData() {
        return (EReference)dataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataType_LiteralData() {
        return (EReference)dataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataType_BoundingBoxData() {
        return (EReference)dataTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDefaultType() {
        return defaultTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultType_CRS() {
        return (EAttribute)defaultTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDefaultType1() {
        return defaultType1EClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDefaultType1_UOM() {
        return (EReference)defaultType1EClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDefaultType2() {
        return defaultType2EClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultType2_Language() {
        return (EAttribute)defaultType2EClass.getEStructuralFeatures().get(0);
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
    public EClass getDescriptionType() {
        return descriptionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDescriptionType_Identifier() {
        return (EReference)descriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDescriptionType_Title() {
        return (EReference)descriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDescriptionType_Abstract() {
        return (EReference)descriptionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDescriptionType_Metadata() {
        return (EReference)descriptionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDocumentOutputDefinitionType() {
        return documentOutputDefinitionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentOutputDefinitionType_Title() {
        return (EReference)documentOutputDefinitionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentOutputDefinitionType_Abstract() {
        return (EReference)documentOutputDefinitionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentOutputDefinitionType_AsReference() {
        return (EAttribute)documentOutputDefinitionTypeEClass.getEStructuralFeatures().get(2);
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
    public EReference getDocumentRoot_Capabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DescribeProcess() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Execute() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ExecuteResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Languages() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ProcessDescriptions() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ProcessOfferings() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_WSDL() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_ProcessVersion() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExecuteResponseType() {
        return executeResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecuteResponseType_Process() {
        return (EReference)executeResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecuteResponseType_Status() {
        return (EReference)executeResponseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecuteResponseType_DataInputs() {
        return (EReference)executeResponseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecuteResponseType_OutputDefinitions() {
        return (EReference)executeResponseTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecuteResponseType_ProcessOutputs() {
        return (EReference)executeResponseTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getExecuteResponseType_ServiceInstance() {
        return (EAttribute)executeResponseTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getExecuteResponseType_StatusLocation() {
        return (EAttribute)executeResponseTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExecuteType() {
        return executeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecuteType_Identifier() {
        return (EReference)executeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecuteType_DataInputs() {
        return (EReference)executeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecuteType_ResponseForm() {
        return (EReference)executeTypeEClass.getEStructuralFeatures().get(2);
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
    public EAttribute getGetCapabilitiesType_Language() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCapabilitiesType_Service() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCapabilitiesType_BaseUrl() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetCapabilitiesType_ExtendedProperties() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(4);
    }

				/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getHeaderType() {
        return headerTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHeaderType_Key() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHeaderType_Value() {
        return (EAttribute)headerTypeEClass.getEStructuralFeatures().get(1);
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
    public EReference getInputDescriptionType_ComplexData() {
        return (EReference)inputDescriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputDescriptionType_LiteralData() {
        return (EReference)inputDescriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputDescriptionType_BoundingBoxData() {
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
    public EClass getInputReferenceType() {
        return inputReferenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputReferenceType_Header() {
        return (EReference)inputReferenceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInputReferenceType_Body() {
        return (EAttribute)inputReferenceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputReferenceType_BodyReference() {
        return (EReference)inputReferenceTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInputReferenceType_Encoding() {
        return (EAttribute)inputReferenceTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInputReferenceType_Href() {
        return (EAttribute)inputReferenceTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInputReferenceType_Method() {
        return (EAttribute)inputReferenceTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInputReferenceType_MimeType() {
        return (EAttribute)inputReferenceTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInputReferenceType_Schema() {
        return (EAttribute)inputReferenceTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInputType() {
        return inputTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputType_Identifier() {
        return (EReference)inputTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputType_Title() {
        return (EReference)inputTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputType_Abstract() {
        return (EReference)inputTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputType_Reference() {
        return (EReference)inputTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInputType_Data() {
        return (EReference)inputTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLanguagesType() {
        return languagesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLanguagesType_Language() {
        return (EAttribute)languagesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLanguagesType1() {
        return languagesType1EClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLanguagesType1_Default() {
        return (EReference)languagesType1EClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLanguagesType1_Supported() {
        return (EReference)languagesType1EClass.getEStructuralFeatures().get(1);
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
    public EAttribute getLiteralDataType_Value() {
        return (EAttribute)literalDataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLiteralDataType_DataType() {
        return (EAttribute)literalDataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLiteralDataType_Uom() {
        return (EAttribute)literalDataTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLiteralInputType() {
        return literalInputTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLiteralInputType_AllowedValues() {
        return (EReference)literalInputTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLiteralInputType_AnyValue() {
        return (EReference)literalInputTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLiteralInputType_ValuesReference() {
        return (EReference)literalInputTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLiteralInputType_DefaultValue() {
        return (EAttribute)literalInputTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLiteralOutputType() {
        return literalOutputTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLiteralOutputType_DataType() {
        return (EReference)literalOutputTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLiteralOutputType_UOMs() {
        return (EReference)literalOutputTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOutputDataType() {
        return outputDataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOutputDataType_Reference() {
        return (EReference)outputDataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOutputDataType_Data() {
        return (EReference)outputDataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOutputDefinitionsType() {
        return outputDefinitionsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOutputDefinitionsType_Output() {
        return (EReference)outputDefinitionsTypeEClass.getEStructuralFeatures().get(0);
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
    public EReference getOutputDefinitionType_Identifier() {
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
    public EAttribute getOutputDefinitionType_MimeType() {
        return (EAttribute)outputDefinitionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputDefinitionType_Schema() {
        return (EAttribute)outputDefinitionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputDefinitionType_Uom() {
        return (EAttribute)outputDefinitionTypeEClass.getEStructuralFeatures().get(4);
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
    public EReference getOutputDescriptionType_ComplexOutput() {
        return (EReference)outputDescriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOutputDescriptionType_LiteralOutput() {
        return (EReference)outputDescriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOutputDescriptionType_BoundingBoxOutput() {
        return (EReference)outputDescriptionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOutputReferenceType() {
        return outputReferenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputReferenceType_Encoding() {
        return (EAttribute)outputReferenceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputReferenceType_Href() {
        return (EAttribute)outputReferenceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputReferenceType_MimeType() {
        return (EAttribute)outputReferenceTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputReferenceType_Schema() {
        return (EAttribute)outputReferenceTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProcessBriefType() {
        return processBriefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessBriefType_Profile() {
        return (EAttribute)processBriefTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessBriefType_WSDL() {
        return (EReference)processBriefTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessBriefType_ProcessVersion() {
        return (EAttribute)processBriefTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProcessDescriptionsType() {
        return processDescriptionsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessDescriptionsType_ProcessDescription() {
        return (EReference)processDescriptionsTypeEClass.getEStructuralFeatures().get(0);
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
    public EReference getProcessDescriptionType_DataInputs() {
        return (EReference)processDescriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessDescriptionType_ProcessOutputs() {
        return (EReference)processDescriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessDescriptionType_StatusSupported() {
        return (EAttribute)processDescriptionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessDescriptionType_StoreSupported() {
        return (EAttribute)processDescriptionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProcessFailedType() {
        return processFailedTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessFailedType_ExceptionReport() {
        return (EReference)processFailedTypeEClass.getEStructuralFeatures().get(0);
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
    public EReference getProcessOfferingsType_Process() {
        return (EReference)processOfferingsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProcessOutputsType() {
        return processOutputsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessOutputsType_Output() {
        return (EReference)processOutputsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProcessOutputsType1() {
        return processOutputsType1EClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProcessOutputsType1_Output() {
        return (EReference)processOutputsType1EClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProcessStartedType() {
        return processStartedTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessStartedType_Value() {
        return (EAttribute)processStartedTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProcessStartedType_PercentCompleted() {
        return (EAttribute)processStartedTypeEClass.getEStructuralFeatures().get(1);
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
    public EAttribute getRequestBaseType_Language() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(0);
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
    public EAttribute getRequestBaseType_BaseUrl() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getRequestBaseType_ExtendedProperties() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(4);
    }

				/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResponseBaseType() {
        return responseBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResponseBaseType_Lang() {
        return (EAttribute)responseBaseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResponseBaseType_Service() {
        return (EAttribute)responseBaseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResponseBaseType_Version() {
        return (EAttribute)responseBaseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResponseDocumentType() {
        return responseDocumentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResponseDocumentType_Output() {
        return (EReference)responseDocumentTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResponseDocumentType_Lineage() {
        return (EAttribute)responseDocumentTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResponseDocumentType_Status() {
        return (EAttribute)responseDocumentTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResponseDocumentType_StoreExecuteResponse() {
        return (EAttribute)responseDocumentTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResponseFormType() {
        return responseFormTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResponseFormType_ResponseDocument() {
        return (EReference)responseFormTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResponseFormType_RawDataOutput() {
        return (EReference)responseFormTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStatusType() {
        return statusTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStatusType_ProcessAccepted() {
        return (EAttribute)statusTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStatusType_ProcessStarted() {
        return (EReference)statusTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStatusType_ProcessPaused() {
        return (EReference)statusTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStatusType_ProcessSucceeded() {
        return (EAttribute)statusTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStatusType_ProcessFailed() {
        return (EReference)statusTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStatusType_CreationTime() {
        return (EAttribute)statusTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSupportedComplexDataInputType() {
        return supportedComplexDataInputTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSupportedComplexDataInputType_MaximumMegabytes() {
        return (EAttribute)supportedComplexDataInputTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSupportedComplexDataType() {
        return supportedComplexDataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSupportedComplexDataType_Default() {
        return (EReference)supportedComplexDataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSupportedComplexDataType_Supported() {
        return (EReference)supportedComplexDataTypeEClass.getEStructuralFeatures().get(1);
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
    public EReference getSupportedCRSsType_Default() {
        return (EReference)supportedCRSsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSupportedCRSsType_Supported() {
        return (EReference)supportedCRSsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSupportedUOMsType() {
        return supportedUOMsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSupportedUOMsType_Default() {
        return (EReference)supportedUOMsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSupportedUOMsType_Supported() {
        return (EReference)supportedUOMsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUOMsType() {
        return uoMsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUOMsType_UOM() {
        return (EReference)uoMsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValuesReferenceType() {
        return valuesReferenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuesReferenceType_Reference() {
        return (EAttribute)valuesReferenceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuesReferenceType_ValuesForm() {
        return (EAttribute)valuesReferenceTypeEClass.getEStructuralFeatures().get(1);
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
    public EReference getWPSCapabilitiesType_ProcessOfferings() {
        return (EReference)wpsCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getWPSCapabilitiesType_Languages() {
        return (EReference)wpsCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getWPSCapabilitiesType_WSDL() {
        return (EReference)wpsCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWPSCapabilitiesType_Lang() {
        return (EAttribute)wpsCapabilitiesTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWPSCapabilitiesType_Service() {
        return (EAttribute)wpsCapabilitiesTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getWSDLType() {
        return wsdlTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLType_Href() {
        return (EAttribute)wsdlTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUnit() {
        return unitEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getMethodType() {
        return methodTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getMethodTypeObject() {
        return methodTypeObjectEDataType;
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
	public EDataType getMap() {
        return mapEDataType;
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
    public Wps10Factory getWps10Factory() {
        return (Wps10Factory)getEFactoryInstance();
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

        complexDataCombinationsTypeEClass = createEClass(COMPLEX_DATA_COMBINATIONS_TYPE);
        createEReference(complexDataCombinationsTypeEClass, COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT);

        complexDataCombinationTypeEClass = createEClass(COMPLEX_DATA_COMBINATION_TYPE);
        createEReference(complexDataCombinationTypeEClass, COMPLEX_DATA_COMBINATION_TYPE__FORMAT);

        complexDataDescriptionTypeEClass = createEClass(COMPLEX_DATA_DESCRIPTION_TYPE);
        createEAttribute(complexDataDescriptionTypeEClass, COMPLEX_DATA_DESCRIPTION_TYPE__MIME_TYPE);
        createEAttribute(complexDataDescriptionTypeEClass, COMPLEX_DATA_DESCRIPTION_TYPE__ENCODING);
        createEAttribute(complexDataDescriptionTypeEClass, COMPLEX_DATA_DESCRIPTION_TYPE__SCHEMA);

        complexDataTypeEClass = createEClass(COMPLEX_DATA_TYPE);
        createEAttribute(complexDataTypeEClass, COMPLEX_DATA_TYPE__ENCODING);
        createEAttribute(complexDataTypeEClass, COMPLEX_DATA_TYPE__MIME_TYPE);
        createEAttribute(complexDataTypeEClass, COMPLEX_DATA_TYPE__SCHEMA);
        createEAttribute(complexDataTypeEClass, COMPLEX_DATA_TYPE__DATA);

        crSsTypeEClass = createEClass(CR_SS_TYPE);
        createEAttribute(crSsTypeEClass, CR_SS_TYPE__CRS);

        dataInputsTypeEClass = createEClass(DATA_INPUTS_TYPE);
        createEReference(dataInputsTypeEClass, DATA_INPUTS_TYPE__INPUT);

        dataInputsType1EClass = createEClass(DATA_INPUTS_TYPE1);
        createEReference(dataInputsType1EClass, DATA_INPUTS_TYPE1__INPUT);

        dataTypeEClass = createEClass(DATA_TYPE);
        createEReference(dataTypeEClass, DATA_TYPE__COMPLEX_DATA);
        createEReference(dataTypeEClass, DATA_TYPE__LITERAL_DATA);
        createEReference(dataTypeEClass, DATA_TYPE__BOUNDING_BOX_DATA);

        defaultTypeEClass = createEClass(DEFAULT_TYPE);
        createEAttribute(defaultTypeEClass, DEFAULT_TYPE__CRS);

        defaultType1EClass = createEClass(DEFAULT_TYPE1);
        createEReference(defaultType1EClass, DEFAULT_TYPE1__UOM);

        defaultType2EClass = createEClass(DEFAULT_TYPE2);
        createEAttribute(defaultType2EClass, DEFAULT_TYPE2__LANGUAGE);

        describeProcessTypeEClass = createEClass(DESCRIBE_PROCESS_TYPE);
        createEReference(describeProcessTypeEClass, DESCRIBE_PROCESS_TYPE__IDENTIFIER);

        descriptionTypeEClass = createEClass(DESCRIPTION_TYPE);
        createEReference(descriptionTypeEClass, DESCRIPTION_TYPE__IDENTIFIER);
        createEReference(descriptionTypeEClass, DESCRIPTION_TYPE__TITLE);
        createEReference(descriptionTypeEClass, DESCRIPTION_TYPE__ABSTRACT);
        createEReference(descriptionTypeEClass, DESCRIPTION_TYPE__METADATA);

        documentOutputDefinitionTypeEClass = createEClass(DOCUMENT_OUTPUT_DEFINITION_TYPE);
        createEReference(documentOutputDefinitionTypeEClass, DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE);
        createEReference(documentOutputDefinitionTypeEClass, DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT);
        createEAttribute(documentOutputDefinitionTypeEClass, DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_PROCESS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXECUTE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXECUTE_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LANGUAGES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROCESS_DESCRIPTIONS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROCESS_OFFERINGS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__WSDL);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__PROCESS_VERSION);

        executeResponseTypeEClass = createEClass(EXECUTE_RESPONSE_TYPE);
        createEReference(executeResponseTypeEClass, EXECUTE_RESPONSE_TYPE__PROCESS);
        createEReference(executeResponseTypeEClass, EXECUTE_RESPONSE_TYPE__STATUS);
        createEReference(executeResponseTypeEClass, EXECUTE_RESPONSE_TYPE__DATA_INPUTS);
        createEReference(executeResponseTypeEClass, EXECUTE_RESPONSE_TYPE__OUTPUT_DEFINITIONS);
        createEReference(executeResponseTypeEClass, EXECUTE_RESPONSE_TYPE__PROCESS_OUTPUTS);
        createEAttribute(executeResponseTypeEClass, EXECUTE_RESPONSE_TYPE__SERVICE_INSTANCE);
        createEAttribute(executeResponseTypeEClass, EXECUTE_RESPONSE_TYPE__STATUS_LOCATION);

        executeTypeEClass = createEClass(EXECUTE_TYPE);
        createEReference(executeTypeEClass, EXECUTE_TYPE__IDENTIFIER);
        createEReference(executeTypeEClass, EXECUTE_TYPE__DATA_INPUTS);
        createEReference(executeTypeEClass, EXECUTE_TYPE__RESPONSE_FORM);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEReference(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__LANGUAGE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__BASE_URL);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES);

        headerTypeEClass = createEClass(HEADER_TYPE);
        createEAttribute(headerTypeEClass, HEADER_TYPE__KEY);
        createEAttribute(headerTypeEClass, HEADER_TYPE__VALUE);

        inputDescriptionTypeEClass = createEClass(INPUT_DESCRIPTION_TYPE);
        createEReference(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__COMPLEX_DATA);
        createEReference(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__LITERAL_DATA);
        createEReference(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA);
        createEAttribute(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__MAX_OCCURS);
        createEAttribute(inputDescriptionTypeEClass, INPUT_DESCRIPTION_TYPE__MIN_OCCURS);

        inputReferenceTypeEClass = createEClass(INPUT_REFERENCE_TYPE);
        createEReference(inputReferenceTypeEClass, INPUT_REFERENCE_TYPE__HEADER);
        createEAttribute(inputReferenceTypeEClass, INPUT_REFERENCE_TYPE__BODY);
        createEReference(inputReferenceTypeEClass, INPUT_REFERENCE_TYPE__BODY_REFERENCE);
        createEAttribute(inputReferenceTypeEClass, INPUT_REFERENCE_TYPE__ENCODING);
        createEAttribute(inputReferenceTypeEClass, INPUT_REFERENCE_TYPE__HREF);
        createEAttribute(inputReferenceTypeEClass, INPUT_REFERENCE_TYPE__METHOD);
        createEAttribute(inputReferenceTypeEClass, INPUT_REFERENCE_TYPE__MIME_TYPE);
        createEAttribute(inputReferenceTypeEClass, INPUT_REFERENCE_TYPE__SCHEMA);

        inputTypeEClass = createEClass(INPUT_TYPE);
        createEReference(inputTypeEClass, INPUT_TYPE__IDENTIFIER);
        createEReference(inputTypeEClass, INPUT_TYPE__TITLE);
        createEReference(inputTypeEClass, INPUT_TYPE__ABSTRACT);
        createEReference(inputTypeEClass, INPUT_TYPE__REFERENCE);
        createEReference(inputTypeEClass, INPUT_TYPE__DATA);

        languagesTypeEClass = createEClass(LANGUAGES_TYPE);
        createEAttribute(languagesTypeEClass, LANGUAGES_TYPE__LANGUAGE);

        languagesType1EClass = createEClass(LANGUAGES_TYPE1);
        createEReference(languagesType1EClass, LANGUAGES_TYPE1__DEFAULT);
        createEReference(languagesType1EClass, LANGUAGES_TYPE1__SUPPORTED);

        literalDataTypeEClass = createEClass(LITERAL_DATA_TYPE);
        createEAttribute(literalDataTypeEClass, LITERAL_DATA_TYPE__VALUE);
        createEAttribute(literalDataTypeEClass, LITERAL_DATA_TYPE__DATA_TYPE);
        createEAttribute(literalDataTypeEClass, LITERAL_DATA_TYPE__UOM);

        literalInputTypeEClass = createEClass(LITERAL_INPUT_TYPE);
        createEReference(literalInputTypeEClass, LITERAL_INPUT_TYPE__ALLOWED_VALUES);
        createEReference(literalInputTypeEClass, LITERAL_INPUT_TYPE__ANY_VALUE);
        createEReference(literalInputTypeEClass, LITERAL_INPUT_TYPE__VALUES_REFERENCE);
        createEAttribute(literalInputTypeEClass, LITERAL_INPUT_TYPE__DEFAULT_VALUE);

        literalOutputTypeEClass = createEClass(LITERAL_OUTPUT_TYPE);
        createEReference(literalOutputTypeEClass, LITERAL_OUTPUT_TYPE__DATA_TYPE);
        createEReference(literalOutputTypeEClass, LITERAL_OUTPUT_TYPE__UO_MS);

        outputDataTypeEClass = createEClass(OUTPUT_DATA_TYPE);
        createEReference(outputDataTypeEClass, OUTPUT_DATA_TYPE__REFERENCE);
        createEReference(outputDataTypeEClass, OUTPUT_DATA_TYPE__DATA);

        outputDefinitionsTypeEClass = createEClass(OUTPUT_DEFINITIONS_TYPE);
        createEReference(outputDefinitionsTypeEClass, OUTPUT_DEFINITIONS_TYPE__OUTPUT);

        outputDefinitionTypeEClass = createEClass(OUTPUT_DEFINITION_TYPE);
        createEReference(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__IDENTIFIER);
        createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__ENCODING);
        createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__MIME_TYPE);
        createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__SCHEMA);
        createEAttribute(outputDefinitionTypeEClass, OUTPUT_DEFINITION_TYPE__UOM);

        outputDescriptionTypeEClass = createEClass(OUTPUT_DESCRIPTION_TYPE);
        createEReference(outputDescriptionTypeEClass, OUTPUT_DESCRIPTION_TYPE__COMPLEX_OUTPUT);
        createEReference(outputDescriptionTypeEClass, OUTPUT_DESCRIPTION_TYPE__LITERAL_OUTPUT);
        createEReference(outputDescriptionTypeEClass, OUTPUT_DESCRIPTION_TYPE__BOUNDING_BOX_OUTPUT);

        outputReferenceTypeEClass = createEClass(OUTPUT_REFERENCE_TYPE);
        createEAttribute(outputReferenceTypeEClass, OUTPUT_REFERENCE_TYPE__ENCODING);
        createEAttribute(outputReferenceTypeEClass, OUTPUT_REFERENCE_TYPE__HREF);
        createEAttribute(outputReferenceTypeEClass, OUTPUT_REFERENCE_TYPE__MIME_TYPE);
        createEAttribute(outputReferenceTypeEClass, OUTPUT_REFERENCE_TYPE__SCHEMA);

        processBriefTypeEClass = createEClass(PROCESS_BRIEF_TYPE);
        createEAttribute(processBriefTypeEClass, PROCESS_BRIEF_TYPE__PROFILE);
        createEReference(processBriefTypeEClass, PROCESS_BRIEF_TYPE__WSDL);
        createEAttribute(processBriefTypeEClass, PROCESS_BRIEF_TYPE__PROCESS_VERSION);

        processDescriptionsTypeEClass = createEClass(PROCESS_DESCRIPTIONS_TYPE);
        createEReference(processDescriptionsTypeEClass, PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION);

        processDescriptionTypeEClass = createEClass(PROCESS_DESCRIPTION_TYPE);
        createEReference(processDescriptionTypeEClass, PROCESS_DESCRIPTION_TYPE__DATA_INPUTS);
        createEReference(processDescriptionTypeEClass, PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS);
        createEAttribute(processDescriptionTypeEClass, PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED);
        createEAttribute(processDescriptionTypeEClass, PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED);

        processFailedTypeEClass = createEClass(PROCESS_FAILED_TYPE);
        createEReference(processFailedTypeEClass, PROCESS_FAILED_TYPE__EXCEPTION_REPORT);

        processOfferingsTypeEClass = createEClass(PROCESS_OFFERINGS_TYPE);
        createEReference(processOfferingsTypeEClass, PROCESS_OFFERINGS_TYPE__PROCESS);

        processOutputsTypeEClass = createEClass(PROCESS_OUTPUTS_TYPE);
        createEReference(processOutputsTypeEClass, PROCESS_OUTPUTS_TYPE__OUTPUT);

        processOutputsType1EClass = createEClass(PROCESS_OUTPUTS_TYPE1);
        createEReference(processOutputsType1EClass, PROCESS_OUTPUTS_TYPE1__OUTPUT);

        processStartedTypeEClass = createEClass(PROCESS_STARTED_TYPE);
        createEAttribute(processStartedTypeEClass, PROCESS_STARTED_TYPE__VALUE);
        createEAttribute(processStartedTypeEClass, PROCESS_STARTED_TYPE__PERCENT_COMPLETED);

        requestBaseTypeEClass = createEClass(REQUEST_BASE_TYPE);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__LANGUAGE);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__SERVICE);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__VERSION);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__BASE_URL);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__EXTENDED_PROPERTIES);

        responseBaseTypeEClass = createEClass(RESPONSE_BASE_TYPE);
        createEAttribute(responseBaseTypeEClass, RESPONSE_BASE_TYPE__LANG);
        createEAttribute(responseBaseTypeEClass, RESPONSE_BASE_TYPE__SERVICE);
        createEAttribute(responseBaseTypeEClass, RESPONSE_BASE_TYPE__VERSION);

        responseDocumentTypeEClass = createEClass(RESPONSE_DOCUMENT_TYPE);
        createEReference(responseDocumentTypeEClass, RESPONSE_DOCUMENT_TYPE__OUTPUT);
        createEAttribute(responseDocumentTypeEClass, RESPONSE_DOCUMENT_TYPE__LINEAGE);
        createEAttribute(responseDocumentTypeEClass, RESPONSE_DOCUMENT_TYPE__STATUS);
        createEAttribute(responseDocumentTypeEClass, RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE);

        responseFormTypeEClass = createEClass(RESPONSE_FORM_TYPE);
        createEReference(responseFormTypeEClass, RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT);
        createEReference(responseFormTypeEClass, RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT);

        statusTypeEClass = createEClass(STATUS_TYPE);
        createEAttribute(statusTypeEClass, STATUS_TYPE__PROCESS_ACCEPTED);
        createEReference(statusTypeEClass, STATUS_TYPE__PROCESS_STARTED);
        createEReference(statusTypeEClass, STATUS_TYPE__PROCESS_PAUSED);
        createEAttribute(statusTypeEClass, STATUS_TYPE__PROCESS_SUCCEEDED);
        createEReference(statusTypeEClass, STATUS_TYPE__PROCESS_FAILED);
        createEAttribute(statusTypeEClass, STATUS_TYPE__CREATION_TIME);

        supportedComplexDataInputTypeEClass = createEClass(SUPPORTED_COMPLEX_DATA_INPUT_TYPE);
        createEAttribute(supportedComplexDataInputTypeEClass, SUPPORTED_COMPLEX_DATA_INPUT_TYPE__MAXIMUM_MEGABYTES);

        supportedComplexDataTypeEClass = createEClass(SUPPORTED_COMPLEX_DATA_TYPE);
        createEReference(supportedComplexDataTypeEClass, SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT);
        createEReference(supportedComplexDataTypeEClass, SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED);

        supportedCRSsTypeEClass = createEClass(SUPPORTED_CR_SS_TYPE);
        createEReference(supportedCRSsTypeEClass, SUPPORTED_CR_SS_TYPE__DEFAULT);
        createEReference(supportedCRSsTypeEClass, SUPPORTED_CR_SS_TYPE__SUPPORTED);

        supportedUOMsTypeEClass = createEClass(SUPPORTED_UO_MS_TYPE);
        createEReference(supportedUOMsTypeEClass, SUPPORTED_UO_MS_TYPE__DEFAULT);
        createEReference(supportedUOMsTypeEClass, SUPPORTED_UO_MS_TYPE__SUPPORTED);

        uoMsTypeEClass = createEClass(UO_MS_TYPE);
        createEReference(uoMsTypeEClass, UO_MS_TYPE__UOM);

        valuesReferenceTypeEClass = createEClass(VALUES_REFERENCE_TYPE);
        createEAttribute(valuesReferenceTypeEClass, VALUES_REFERENCE_TYPE__REFERENCE);
        createEAttribute(valuesReferenceTypeEClass, VALUES_REFERENCE_TYPE__VALUES_FORM);

        wpsCapabilitiesTypeEClass = createEClass(WPS_CAPABILITIES_TYPE);
        createEReference(wpsCapabilitiesTypeEClass, WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS);
        createEReference(wpsCapabilitiesTypeEClass, WPS_CAPABILITIES_TYPE__LANGUAGES);
        createEReference(wpsCapabilitiesTypeEClass, WPS_CAPABILITIES_TYPE__WSDL);
        createEAttribute(wpsCapabilitiesTypeEClass, WPS_CAPABILITIES_TYPE__LANG);
        createEAttribute(wpsCapabilitiesTypeEClass, WPS_CAPABILITIES_TYPE__SERVICE);

        wsdlTypeEClass = createEClass(WSDL_TYPE);
        createEAttribute(wsdlTypeEClass, WSDL_TYPE__HREF);

        unitEClass = createEClass(UNIT);

        // Create enums
        methodTypeEEnum = createEEnum(METHOD_TYPE);

        // Create data types
        methodTypeObjectEDataType = createEDataType(METHOD_TYPE_OBJECT);
        percentCompletedTypeEDataType = createEDataType(PERCENT_COMPLETED_TYPE);
        mapEDataType = createEDataType(MAP);
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
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
        Ows11Package theOws11Package = (Ows11Package)EPackage.Registry.INSTANCE.getEPackage(Ows11Package.eNS_URI);
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

        // Add supertypes to classes
        complexDataTypeEClass.getESuperTypes().add(theXMLTypePackage.getAnyType());
        describeProcessTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        documentOutputDefinitionTypeEClass.getESuperTypes().add(this.getOutputDefinitionType());
        executeResponseTypeEClass.getESuperTypes().add(this.getResponseBaseType());
        executeTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        inputDescriptionTypeEClass.getESuperTypes().add(this.getDescriptionType());
        literalInputTypeEClass.getESuperTypes().add(this.getLiteralOutputType());
        outputDataTypeEClass.getESuperTypes().add(this.getDescriptionType());
        outputDescriptionTypeEClass.getESuperTypes().add(this.getDescriptionType());
        processBriefTypeEClass.getESuperTypes().add(this.getDescriptionType());
        processDescriptionsTypeEClass.getESuperTypes().add(this.getResponseBaseType());
        processDescriptionTypeEClass.getESuperTypes().add(this.getProcessBriefType());
        supportedComplexDataInputTypeEClass.getESuperTypes().add(this.getSupportedComplexDataType());
        wpsCapabilitiesTypeEClass.getESuperTypes().add(theOws11Package.getCapabilitiesBaseType());

        // Initialize classes and features; add operations and parameters
        initEClass(bodyReferenceTypeEClass, BodyReferenceType.class, "BodyReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBodyReferenceType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 1, 1, BodyReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(complexDataCombinationsTypeEClass, ComplexDataCombinationsType.class, "ComplexDataCombinationsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComplexDataCombinationsType_Format(), this.getComplexDataDescriptionType(), null, "format", null, 1, -1, ComplexDataCombinationsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(complexDataCombinationTypeEClass, ComplexDataCombinationType.class, "ComplexDataCombinationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComplexDataCombinationType_Format(), this.getComplexDataDescriptionType(), null, "format", null, 1, 1, ComplexDataCombinationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(complexDataDescriptionTypeEClass, ComplexDataDescriptionType.class, "ComplexDataDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getComplexDataDescriptionType_MimeType(), theOws11Package.getMimeType(), "mimeType", null, 1, 1, ComplexDataDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComplexDataDescriptionType_Encoding(), theXMLTypePackage.getAnyURI(), "encoding", null, 0, 1, ComplexDataDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComplexDataDescriptionType_Schema(), theXMLTypePackage.getAnyURI(), "schema", null, 0, 1, ComplexDataDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(complexDataTypeEClass, ComplexDataType.class, "ComplexDataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getComplexDataType_Encoding(), theXMLTypePackage.getAnyURI(), "encoding", null, 0, 1, ComplexDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComplexDataType_MimeType(), theOws11Package.getMimeType(), "mimeType", null, 0, 1, ComplexDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComplexDataType_Schema(), theXMLTypePackage.getAnyURI(), "schema", null, 0, 1, ComplexDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComplexDataType_Data(), ecorePackage.getEJavaObject(), "data", null, 0, -1, ComplexDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(crSsTypeEClass, CRSsType.class, "CRSsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCRSsType_CRS(), theXMLTypePackage.getAnyURI(), "cRS", null, 1, 1, CRSsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dataInputsTypeEClass, DataInputsType.class, "DataInputsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDataInputsType_Input(), this.getInputDescriptionType(), null, "input", null, 1, -1, DataInputsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dataInputsType1EClass, DataInputsType1.class, "DataInputsType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDataInputsType1_Input(), this.getInputType(), null, "input", null, 1, -1, DataInputsType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dataTypeEClass, DataType.class, "DataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDataType_ComplexData(), this.getComplexDataType(), null, "complexData", null, 0, 1, DataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDataType_LiteralData(), this.getLiteralDataType(), null, "literalData", null, 0, 1, DataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDataType_BoundingBoxData(), theOws11Package.getBoundingBoxType(), null, "boundingBoxData", null, 0, 1, DataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(defaultTypeEClass, DefaultType.class, "DefaultType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDefaultType_CRS(), theXMLTypePackage.getAnyURI(), "cRS", null, 1, 1, DefaultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(defaultType1EClass, DefaultType1.class, "DefaultType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDefaultType1_UOM(), this.getUnit(), null, "uOM", null, 0, 1, DefaultType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(defaultType2EClass, DefaultType2.class, "DefaultType2", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDefaultType2_Language(), theXMLTypePackage.getLanguage(), "language", null, 1, 1, DefaultType2.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeProcessTypeEClass, DescribeProcessType.class, "DescribeProcessType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDescribeProcessType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, -1, DescribeProcessType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(descriptionTypeEClass, DescriptionType.class, "DescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDescriptionType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDescriptionType_Title(), theOws11Package.getLanguageStringType(), null, "title", null, 1, 1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDescriptionType_Abstract(), theOws11Package.getLanguageStringType(), null, "abstract", null, 0, 1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDescriptionType_Metadata(), theOws11Package.getMetadataType(), null, "metadata", null, 0, -1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentOutputDefinitionTypeEClass, DocumentOutputDefinitionType.class, "DocumentOutputDefinitionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDocumentOutputDefinitionType_Title(), theOws11Package.getLanguageStringType(), null, "title", null, 0, 1, DocumentOutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentOutputDefinitionType_Abstract(), theOws11Package.getLanguageStringType(), null, "abstract", null, 0, 1, DocumentOutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentOutputDefinitionType_AsReference(), theXMLTypePackage.getBoolean(), "asReference", "false", 0, 1, DocumentOutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Capabilities(), this.getWPSCapabilitiesType(), null, "capabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DescribeProcess(), this.getDescribeProcessType(), null, "describeProcess", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Execute(), this.getExecuteType(), null, "execute", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ExecuteResponse(), this.getExecuteResponseType(), null, "executeResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Languages(), this.getLanguagesType1(), null, "languages", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ProcessDescriptions(), this.getProcessDescriptionsType(), null, "processDescriptions", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ProcessOfferings(), this.getProcessOfferingsType(), null, "processOfferings", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_WSDL(), this.getWSDLType(), null, "wSDL", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_ProcessVersion(), theXMLTypePackage.getString(), "processVersion", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(executeResponseTypeEClass, ExecuteResponseType.class, "ExecuteResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExecuteResponseType_Process(), this.getProcessBriefType(), null, "process", null, 1, 1, ExecuteResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getExecuteResponseType_Status(), this.getStatusType(), null, "status", null, 1, 1, ExecuteResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getExecuteResponseType_DataInputs(), this.getDataInputsType1(), null, "dataInputs", null, 0, 1, ExecuteResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getExecuteResponseType_OutputDefinitions(), this.getOutputDefinitionsType(), null, "outputDefinitions", null, 0, 1, ExecuteResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getExecuteResponseType_ProcessOutputs(), this.getProcessOutputsType1(), null, "processOutputs", null, 0, 1, ExecuteResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExecuteResponseType_ServiceInstance(), theXMLTypePackage.getAnyURI(), "serviceInstance", null, 1, 1, ExecuteResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExecuteResponseType_StatusLocation(), theXMLTypePackage.getAnyURI(), "statusLocation", null, 0, 1, ExecuteResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(executeTypeEClass, ExecuteType.class, "ExecuteType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExecuteType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, ExecuteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getExecuteType_DataInputs(), this.getDataInputsType1(), null, "dataInputs", null, 0, 1, ExecuteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getExecuteType_ResponseForm(), this.getResponseFormType(), null, "responseForm", null, 0, 1, ExecuteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetCapabilitiesType_AcceptVersions(), theOws11Package.getAcceptVersionsType(), null, "acceptVersions", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_Language(), theXMLTypePackage.getString(), "language", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_Service(), theOws11Package.getServiceType(), "service", "WPS", 1, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_BaseUrl(), ecorePackage.getEString(), "baseUrl", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_ExtendedProperties(), this.getMap(), "extendedProperties", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(headerTypeEClass, HeaderType.class, "HeaderType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getHeaderType_Key(), theXMLTypePackage.getString(), "key", null, 1, 1, HeaderType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHeaderType_Value(), theXMLTypePackage.getString(), "value", null, 1, 1, HeaderType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(inputDescriptionTypeEClass, InputDescriptionType.class, "InputDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInputDescriptionType_ComplexData(), this.getSupportedComplexDataInputType(), null, "complexData", null, 0, 1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInputDescriptionType_LiteralData(), this.getLiteralInputType(), null, "literalData", null, 0, 1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInputDescriptionType_BoundingBoxData(), this.getSupportedCRSsType(), null, "boundingBoxData", null, 0, 1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInputDescriptionType_MaxOccurs(), theXMLTypePackage.getPositiveInteger(), "maxOccurs", null, 1, 1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInputDescriptionType_MinOccurs(), theXMLTypePackage.getNonNegativeInteger(), "minOccurs", null, 1, 1, InputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(inputReferenceTypeEClass, InputReferenceType.class, "InputReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInputReferenceType_Header(), this.getHeaderType(), null, "header", null, 0, -1, InputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInputReferenceType_Body(), ecorePackage.getEJavaObject(), "body", null, 0, 1, InputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInputReferenceType_BodyReference(), this.getBodyReferenceType(), null, "bodyReference", null, 0, 1, InputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInputReferenceType_Encoding(), theXMLTypePackage.getAnyURI(), "encoding", null, 0, 1, InputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInputReferenceType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 1, 1, InputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInputReferenceType_Method(), this.getMethodType(), "method", "GET", 0, 1, InputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInputReferenceType_MimeType(), theOws11Package.getMimeType(), "mimeType", null, 0, 1, InputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInputReferenceType_Schema(), theXMLTypePackage.getAnyURI(), "schema", null, 0, 1, InputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(inputTypeEClass, InputType.class, "InputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInputType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, InputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInputType_Title(), theOws11Package.getLanguageStringType(), null, "title", null, 0, 1, InputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInputType_Abstract(), theOws11Package.getLanguageStringType(), null, "abstract", null, 0, 1, InputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInputType_Reference(), this.getInputReferenceType(), null, "reference", null, 0, 1, InputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInputType_Data(), this.getDataType(), null, "data", null, 0, 1, InputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(languagesTypeEClass, LanguagesType.class, "LanguagesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLanguagesType_Language(), ecorePackage.getEString(), "language", null, 0, -1, LanguagesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(languagesType1EClass, LanguagesType1.class, "LanguagesType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLanguagesType1_Default(), this.getDefaultType2(), null, "default", null, 1, 1, LanguagesType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLanguagesType1_Supported(), this.getLanguagesType(), null, "supported", null, 1, 1, LanguagesType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(literalDataTypeEClass, LiteralDataType.class, "LiteralDataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLiteralDataType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, LiteralDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLiteralDataType_DataType(), theXMLTypePackage.getAnyURI(), "dataType", null, 0, 1, LiteralDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLiteralDataType_Uom(), theXMLTypePackage.getAnyURI(), "uom", null, 0, 1, LiteralDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(literalInputTypeEClass, LiteralInputType.class, "LiteralInputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLiteralInputType_AllowedValues(), theOws11Package.getAllowedValuesType(), null, "allowedValues", null, 0, 1, LiteralInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLiteralInputType_AnyValue(), theOws11Package.getAnyValueType(), null, "anyValue", null, 0, 1, LiteralInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLiteralInputType_ValuesReference(), this.getValuesReferenceType(), null, "valuesReference", null, 0, 1, LiteralInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLiteralInputType_DefaultValue(), theXMLTypePackage.getString(), "defaultValue", null, 0, 1, LiteralInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(literalOutputTypeEClass, LiteralOutputType.class, "LiteralOutputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLiteralOutputType_DataType(), theOws11Package.getDomainMetadataType(), null, "dataType", null, 0, 1, LiteralOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLiteralOutputType_UOMs(), this.getSupportedUOMsType(), null, "uOMs", null, 0, 1, LiteralOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(outputDataTypeEClass, OutputDataType.class, "OutputDataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOutputDataType_Reference(), this.getOutputReferenceType(), null, "reference", null, 0, 1, OutputDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOutputDataType_Data(), this.getDataType(), null, "data", null, 0, 1, OutputDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(outputDefinitionsTypeEClass, OutputDefinitionsType.class, "OutputDefinitionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOutputDefinitionsType_Output(), this.getDocumentOutputDefinitionType(), null, "output", null, 1, -1, OutputDefinitionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(outputDefinitionTypeEClass, OutputDefinitionType.class, "OutputDefinitionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOutputDefinitionType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputDefinitionType_Encoding(), theXMLTypePackage.getAnyURI(), "encoding", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputDefinitionType_MimeType(), theOws11Package.getMimeType(), "mimeType", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputDefinitionType_Schema(), theXMLTypePackage.getAnyURI(), "schema", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputDefinitionType_Uom(), theXMLTypePackage.getAnyURI(), "uom", null, 0, 1, OutputDefinitionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(outputDescriptionTypeEClass, OutputDescriptionType.class, "OutputDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOutputDescriptionType_ComplexOutput(), this.getSupportedComplexDataType(), null, "complexOutput", null, 0, 1, OutputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOutputDescriptionType_LiteralOutput(), this.getLiteralOutputType(), null, "literalOutput", null, 0, 1, OutputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOutputDescriptionType_BoundingBoxOutput(), this.getSupportedCRSsType(), null, "boundingBoxOutput", null, 0, 1, OutputDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(outputReferenceTypeEClass, OutputReferenceType.class, "OutputReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOutputReferenceType_Encoding(), theXMLTypePackage.getAnyURI(), "encoding", null, 0, 1, OutputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputReferenceType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 1, 1, OutputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputReferenceType_MimeType(), theOws11Package.getMimeType(), "mimeType", null, 0, 1, OutputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputReferenceType_Schema(), theXMLTypePackage.getAnyURI(), "schema", null, 0, 1, OutputReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processBriefTypeEClass, ProcessBriefType.class, "ProcessBriefType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getProcessBriefType_Profile(), theXMLTypePackage.getAnyURI(), "profile", null, 0, 1, ProcessBriefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessBriefType_WSDL(), this.getWSDLType(), null, "wSDL", null, 0, 1, ProcessBriefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessBriefType_ProcessVersion(), theXMLTypePackage.getString(), "processVersion", null, 1, 1, ProcessBriefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processDescriptionsTypeEClass, ProcessDescriptionsType.class, "ProcessDescriptionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProcessDescriptionsType_ProcessDescription(), this.getProcessDescriptionType(), null, "processDescription", null, 1, -1, ProcessDescriptionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processDescriptionTypeEClass, ProcessDescriptionType.class, "ProcessDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProcessDescriptionType_DataInputs(), this.getDataInputsType(), null, "dataInputs", null, 0, 1, ProcessDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessDescriptionType_ProcessOutputs(), this.getProcessOutputsType(), null, "processOutputs", null, 1, 1, ProcessDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessDescriptionType_StatusSupported(), theXMLTypePackage.getBoolean(), "statusSupported", "false", 0, 1, ProcessDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessDescriptionType_StoreSupported(), theXMLTypePackage.getBoolean(), "storeSupported", "false", 0, 1, ProcessDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processFailedTypeEClass, ProcessFailedType.class, "ProcessFailedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProcessFailedType_ExceptionReport(), theOws11Package.getExceptionReportType(), null, "exceptionReport", null, 1, 1, ProcessFailedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processOfferingsTypeEClass, ProcessOfferingsType.class, "ProcessOfferingsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProcessOfferingsType_Process(), this.getProcessBriefType(), null, "process", null, 1, -1, ProcessOfferingsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processOutputsTypeEClass, ProcessOutputsType.class, "ProcessOutputsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProcessOutputsType_Output(), this.getOutputDescriptionType(), null, "output", null, 1, -1, ProcessOutputsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processOutputsType1EClass, ProcessOutputsType1.class, "ProcessOutputsType1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getProcessOutputsType1_Output(), this.getOutputDataType(), null, "output", null, 1, -1, ProcessOutputsType1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(processStartedTypeEClass, ProcessStartedType.class, "ProcessStartedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getProcessStartedType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ProcessStartedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessStartedType_PercentCompleted(), this.getPercentCompletedType(), "percentCompleted", null, 0, 1, ProcessStartedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(requestBaseTypeEClass, RequestBaseType.class, "RequestBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRequestBaseType_Language(), theXMLTypePackage.getString(), "language", null, 0, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequestBaseType_Service(), theXMLTypePackage.getString(), "service", "WPS", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequestBaseType_Version(), theOws11Package.getVersionType1(), "version", "1.0.0", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequestBaseType_BaseUrl(), ecorePackage.getEString(), "baseUrl", null, 0, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequestBaseType_ExtendedProperties(), this.getMap(), "extendedProperties", null, 0, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(responseBaseTypeEClass, ResponseBaseType.class, "ResponseBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getResponseBaseType_Lang(), theXMLTypePackage.getString(), "lang", null, 1, 1, ResponseBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResponseBaseType_Service(), theXMLTypePackage.getString(), "service", "WPS", 1, 1, ResponseBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResponseBaseType_Version(), theOws11Package.getVersionType1(), "version", "1.0.0", 1, 1, ResponseBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(responseDocumentTypeEClass, ResponseDocumentType.class, "ResponseDocumentType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResponseDocumentType_Output(), this.getDocumentOutputDefinitionType(), null, "output", null, 1, -1, ResponseDocumentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResponseDocumentType_Lineage(), theXMLTypePackage.getBoolean(), "lineage", "false", 0, 1, ResponseDocumentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResponseDocumentType_Status(), theXMLTypePackage.getBoolean(), "status", "false", 0, 1, ResponseDocumentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResponseDocumentType_StoreExecuteResponse(), theXMLTypePackage.getBoolean(), "storeExecuteResponse", "false", 0, 1, ResponseDocumentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(responseFormTypeEClass, ResponseFormType.class, "ResponseFormType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResponseFormType_ResponseDocument(), this.getResponseDocumentType(), null, "responseDocument", null, 0, 1, ResponseFormType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getResponseFormType_RawDataOutput(), this.getOutputDefinitionType(), null, "rawDataOutput", null, 0, 1, ResponseFormType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(statusTypeEClass, StatusType.class, "StatusType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStatusType_ProcessAccepted(), theXMLTypePackage.getString(), "processAccepted", null, 0, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStatusType_ProcessStarted(), this.getProcessStartedType(), null, "processStarted", null, 0, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStatusType_ProcessPaused(), this.getProcessStartedType(), null, "processPaused", null, 0, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStatusType_ProcessSucceeded(), theXMLTypePackage.getString(), "processSucceeded", null, 0, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStatusType_ProcessFailed(), this.getProcessFailedType(), null, "processFailed", null, 0, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStatusType_CreationTime(), theXMLTypePackage.getDateTime(), "creationTime", null, 1, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(supportedComplexDataInputTypeEClass, SupportedComplexDataInputType.class, "SupportedComplexDataInputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSupportedComplexDataInputType_MaximumMegabytes(), theXMLTypePackage.getInteger(), "maximumMegabytes", null, 0, 1, SupportedComplexDataInputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(supportedComplexDataTypeEClass, SupportedComplexDataType.class, "SupportedComplexDataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSupportedComplexDataType_Default(), this.getComplexDataCombinationType(), null, "default", null, 1, 1, SupportedComplexDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSupportedComplexDataType_Supported(), this.getComplexDataCombinationsType(), null, "supported", null, 1, 1, SupportedComplexDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(supportedCRSsTypeEClass, SupportedCRSsType.class, "SupportedCRSsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSupportedCRSsType_Default(), this.getDefaultType(), null, "default", null, 1, 1, SupportedCRSsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSupportedCRSsType_Supported(), this.getCRSsType(), null, "supported", null, 1, 1, SupportedCRSsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(supportedUOMsTypeEClass, SupportedUOMsType.class, "SupportedUOMsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSupportedUOMsType_Default(), this.getDefaultType1(), null, "default", null, 1, 1, SupportedUOMsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSupportedUOMsType_Supported(), this.getUOMsType(), null, "supported", null, 1, 1, SupportedUOMsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(uoMsTypeEClass, UOMsType.class, "UOMsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUOMsType_UOM(), this.getUnit(), null, "uOM", null, 0, -1, UOMsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(valuesReferenceTypeEClass, ValuesReferenceType.class, "ValuesReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getValuesReferenceType_Reference(), theXMLTypePackage.getAnyURI(), "reference", null, 0, 1, ValuesReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValuesReferenceType_ValuesForm(), theXMLTypePackage.getAnyURI(), "valuesForm", null, 0, 1, ValuesReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wpsCapabilitiesTypeEClass, WPSCapabilitiesType.class, "WPSCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getWPSCapabilitiesType_ProcessOfferings(), this.getProcessOfferingsType(), null, "processOfferings", null, 1, 1, WPSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWPSCapabilitiesType_Languages(), this.getLanguagesType1(), null, "languages", null, 1, 1, WPSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWPSCapabilitiesType_WSDL(), this.getWSDLType(), null, "wSDL", null, 0, 1, WPSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWPSCapabilitiesType_Lang(), theXMLTypePackage.getString(), "lang", null, 1, 1, WPSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWPSCapabilitiesType_Service(), theXMLTypePackage.getString(), "service", "WPS", 1, 1, WPSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wsdlTypeEClass, WSDLType.class, "WSDLType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getWSDLType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 1, 1, WSDLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(unitEClass, Unit.class, "Unit", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

        // Initialize enums and add enum literals
        initEEnum(methodTypeEEnum, MethodType.class, "MethodType");
        addEEnumLiteral(methodTypeEEnum, MethodType.GET_LITERAL);
        addEEnumLiteral(methodTypeEEnum, MethodType.POST_LITERAL);

        // Initialize data types
        initEDataType(methodTypeObjectEDataType, MethodType.class, "MethodTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(percentCompletedTypeEDataType, BigInteger.class, "PercentCompletedType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(mapEDataType, Map.class, "Map", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(qNameEDataType, QName.class, "QName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // urn:opengis:specification:gml:schema-xlinks:v3.0c2
        createUrnopengisspecificationgmlschemaxlinksv3Annotations();
        // http://www.w3.org/XML/1998/namespace
        createNamespaceAnnotations();
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>urn:opengis:specification:gml:schema-xlinks:v3.0c2</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnopengisspecificationgmlschemaxlinksv3Annotations() {
        String source = "urn:opengis:specification:gml:schema-xlinks:v3.0c2";		
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "xlinks.xsd v3.0b2 2001-07"
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
          (complexDataCombinationsTypeEClass, 
           source, 
           new String[] {
             "name", "ComplexDataCombinationsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getComplexDataCombinationsType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Format"
           });			
        addAnnotation
          (complexDataCombinationTypeEClass, 
           source, 
           new String[] {
             "name", "ComplexDataCombinationType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getComplexDataCombinationType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Format"
           });			
        addAnnotation
          (complexDataDescriptionTypeEClass, 
           source, 
           new String[] {
             "name", "ComplexDataDescriptionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getComplexDataDescriptionType_MimeType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MimeType"
           });			
        addAnnotation
          (getComplexDataDescriptionType_Encoding(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Encoding"
           });			
        addAnnotation
          (getComplexDataDescriptionType_Schema(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Schema"
           });			
        addAnnotation
          (complexDataTypeEClass, 
           source, 
           new String[] {
             "name", "ComplexDataType",
             "kind", "mixed"
           });			
        addAnnotation
          (getComplexDataType_Encoding(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "encoding"
           });			
        addAnnotation
          (getComplexDataType_MimeType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "mimeType"
           });			
        addAnnotation
          (getComplexDataType_Schema(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "schema"
           });			
        addAnnotation
          (crSsTypeEClass, 
           source, 
           new String[] {
             "name", "CRSsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getCRSsType_CRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CRS"
           });			
        addAnnotation
          (dataInputsTypeEClass, 
           source, 
           new String[] {
             "name", "DataInputs_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDataInputsType_Input(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Input"
           });			
        addAnnotation
          (dataInputsType1EClass, 
           source, 
           new String[] {
             "name", "DataInputsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDataInputsType1_Input(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Input",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (dataTypeEClass, 
           source, 
           new String[] {
             "name", "DataType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDataType_ComplexData(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ComplexData",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDataType_LiteralData(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LiteralData",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDataType_BoundingBoxData(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBoxData",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (defaultTypeEClass, 
           source, 
           new String[] {
             "name", "Default_._1_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDefaultType_CRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CRS"
           });			
        addAnnotation
          (defaultType1EClass, 
           source, 
           new String[] {
             "name", "Default_._2_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (defaultType2EClass, 
           source, 
           new String[] {
             "name", "Default_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDefaultType2_Language(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Language",
             "namespace", "http://www.opengis.net/ows/1.1"
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
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (descriptionTypeEClass, 
           source, 
           new String[] {
             "name", "DescriptionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDescriptionType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getDescriptionType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getDescriptionType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getDescriptionType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (documentOutputDefinitionTypeEClass, 
           source, 
           new String[] {
             "name", "DocumentOutputDefinitionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDocumentOutputDefinitionType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getDocumentOutputDefinitionType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getDocumentOutputDefinitionType_AsReference(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "asReference"
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
          (getDocumentRoot_Capabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Capabilities",
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
          (getDocumentRoot_Execute(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Execute",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ExecuteResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ExecuteResponse",
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
          (getDocumentRoot_Languages(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Languages",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ProcessDescriptions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessDescriptions",
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
          (getDocumentRoot_WSDL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WSDL",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ProcessVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "processVersion",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (executeResponseTypeEClass, 
           source, 
           new String[] {
             "name", "ExecuteResponse_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getExecuteResponseType_Process(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Process",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getExecuteResponseType_Status(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Status",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getExecuteResponseType_DataInputs(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DataInputs",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getExecuteResponseType_OutputDefinitions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OutputDefinitions",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getExecuteResponseType_ProcessOutputs(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessOutputs",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getExecuteResponseType_ServiceInstance(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "serviceInstance"
           });			
        addAnnotation
          (getExecuteResponseType_StatusLocation(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "statusLocation"
           });			
        addAnnotation
          (executeTypeEClass, 
           source, 
           new String[] {
             "name", "Execute_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getExecuteType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getExecuteType_DataInputs(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DataInputs",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getExecuteType_ResponseForm(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResponseForm",
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
          (getGetCapabilitiesType_AcceptVersions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AcceptVersions",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCapabilitiesType_Language(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "language"
           });			
        addAnnotation
          (getGetCapabilitiesType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });			
        addAnnotation
          (headerTypeEClass, 
           source, 
           new String[] {
             "name", "Header_._type",
             "kind", "empty"
           });		
        addAnnotation
          (getHeaderType_Key(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "key"
           });			
        addAnnotation
          (getHeaderType_Value(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "value"
           });			
        addAnnotation
          (inputDescriptionTypeEClass, 
           source, 
           new String[] {
             "name", "InputDescriptionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInputDescriptionType_ComplexData(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ComplexData"
           });			
        addAnnotation
          (getInputDescriptionType_LiteralData(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LiteralData"
           });			
        addAnnotation
          (getInputDescriptionType_BoundingBoxData(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBoxData"
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
          (inputReferenceTypeEClass, 
           source, 
           new String[] {
             "name", "InputReferenceType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInputReferenceType_Header(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Header",
             "namespace", "##targetNamespace"
           });				
        addAnnotation
          (getInputReferenceType_BodyReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BodyReference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getInputReferenceType_Encoding(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "encoding"
           });			
        addAnnotation
          (getInputReferenceType_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getInputReferenceType_Method(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "method"
           });			
        addAnnotation
          (getInputReferenceType_MimeType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "mimeType"
           });			
        addAnnotation
          (getInputReferenceType_Schema(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "schema"
           });			
        addAnnotation
          (inputTypeEClass, 
           source, 
           new String[] {
             "name", "InputType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInputType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getInputType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getInputType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getInputType_Reference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Reference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getInputType_Data(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Data",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (languagesTypeEClass, 
           source, 
           new String[] {
             "name", "LanguagesType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (languagesType1EClass, 
           source, 
           new String[] {
             "name", "Languages_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getLanguagesType1_Default(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Default",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getLanguagesType1_Supported(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Supported",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (literalDataTypeEClass, 
           source, 
           new String[] {
             "name", "LiteralDataType",
             "kind", "simple"
           });			
        addAnnotation
          (getLiteralDataType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });			
        addAnnotation
          (getLiteralDataType_DataType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "dataType"
           });			
        addAnnotation
          (getLiteralDataType_Uom(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "uom"
           });			
        addAnnotation
          (literalInputTypeEClass, 
           source, 
           new String[] {
             "name", "LiteralInputType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getLiteralInputType_AllowedValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AllowedValues",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getLiteralInputType_AnyValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AnyValue",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getLiteralInputType_ValuesReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValuesReference"
           });			
        addAnnotation
          (getLiteralInputType_DefaultValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DefaultValue"
           });			
        addAnnotation
          (literalOutputTypeEClass, 
           source, 
           new String[] {
             "name", "LiteralOutputType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getLiteralOutputType_DataType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DataType",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getLiteralOutputType_UOMs(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UOMs"
           });			
        addAnnotation
          (outputDataTypeEClass, 
           source, 
           new String[] {
             "name", "OutputDataType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getOutputDataType_Reference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Reference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOutputDataType_Data(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Data",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (outputDefinitionsTypeEClass, 
           source, 
           new String[] {
             "name", "OutputDefinitionsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getOutputDefinitionsType_Output(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Output",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (outputDefinitionTypeEClass, 
           source, 
           new String[] {
             "name", "OutputDefinitionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getOutputDefinitionType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getOutputDefinitionType_Encoding(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "encoding"
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
          (getOutputDefinitionType_Uom(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "uom"
           });			
        addAnnotation
          (outputDescriptionTypeEClass, 
           source, 
           new String[] {
             "name", "OutputDescriptionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getOutputDescriptionType_ComplexOutput(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ComplexOutput"
           });			
        addAnnotation
          (getOutputDescriptionType_LiteralOutput(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LiteralOutput"
           });			
        addAnnotation
          (getOutputDescriptionType_BoundingBoxOutput(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBoxOutput"
           });			
        addAnnotation
          (outputReferenceTypeEClass, 
           source, 
           new String[] {
             "name", "OutputReferenceType",
             "kind", "empty"
           });			
        addAnnotation
          (getOutputReferenceType_Encoding(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "encoding"
           });			
        addAnnotation
          (getOutputReferenceType_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href"
           });			
        addAnnotation
          (getOutputReferenceType_MimeType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "mimeType"
           });			
        addAnnotation
          (getOutputReferenceType_Schema(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "schema"
           });			
        addAnnotation
          (processBriefTypeEClass, 
           source, 
           new String[] {
             "name", "ProcessBriefType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getProcessBriefType_Profile(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Profile",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getProcessBriefType_WSDL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WSDL",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getProcessBriefType_ProcessVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "processVersion",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (processDescriptionsTypeEClass, 
           source, 
           new String[] {
             "name", "ProcessDescriptions_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getProcessDescriptionsType_ProcessDescription(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessDescription"
           });			
        addAnnotation
          (processDescriptionTypeEClass, 
           source, 
           new String[] {
             "name", "ProcessDescriptionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getProcessDescriptionType_DataInputs(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DataInputs"
           });			
        addAnnotation
          (getProcessDescriptionType_ProcessOutputs(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessOutputs"
           });			
        addAnnotation
          (getProcessDescriptionType_StatusSupported(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "statusSupported"
           });			
        addAnnotation
          (getProcessDescriptionType_StoreSupported(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "storeSupported"
           });			
        addAnnotation
          (processFailedTypeEClass, 
           source, 
           new String[] {
             "name", "ProcessFailedType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getProcessFailedType_ExceptionReport(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ExceptionReport",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (processOfferingsTypeEClass, 
           source, 
           new String[] {
             "name", "ProcessOfferings_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getProcessOfferingsType_Process(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Process",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (processOutputsTypeEClass, 
           source, 
           new String[] {
             "name", "ProcessOutputs_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getProcessOutputsType_Output(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Output"
           });			
        addAnnotation
          (processOutputsType1EClass, 
           source, 
           new String[] {
             "name", "ProcessOutputs_._1_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getProcessOutputsType1_Output(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Output",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (processStartedTypeEClass, 
           source, 
           new String[] {
             "name", "ProcessStartedType",
             "kind", "simple"
           });			
        addAnnotation
          (getProcessStartedType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });			
        addAnnotation
          (getProcessStartedType_PercentCompleted(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "percentCompleted"
           });			
        addAnnotation
          (requestBaseTypeEClass, 
           source, 
           new String[] {
             "name", "RequestBaseType",
             "kind", "empty"
           });			
        addAnnotation
          (getRequestBaseType_Language(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "language"
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
          (responseBaseTypeEClass, 
           source, 
           new String[] {
             "name", "ResponseBaseType",
             "kind", "empty"
           });			
        addAnnotation
          (getResponseBaseType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });			
        addAnnotation
          (getResponseBaseType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });			
        addAnnotation
          (getResponseBaseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });			
        addAnnotation
          (responseDocumentTypeEClass, 
           source, 
           new String[] {
             "name", "ResponseDocumentType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getResponseDocumentType_Output(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Output",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getResponseDocumentType_Lineage(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lineage"
           });			
        addAnnotation
          (getResponseDocumentType_Status(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "status"
           });			
        addAnnotation
          (getResponseDocumentType_StoreExecuteResponse(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "storeExecuteResponse"
           });			
        addAnnotation
          (responseFormTypeEClass, 
           source, 
           new String[] {
             "name", "ResponseFormType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getResponseFormType_ResponseDocument(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResponseDocument",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getResponseFormType_RawDataOutput(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RawDataOutput",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (statusTypeEClass, 
           source, 
           new String[] {
             "name", "StatusType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getStatusType_ProcessAccepted(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessAccepted",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getStatusType_ProcessStarted(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessStarted",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getStatusType_ProcessPaused(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessPaused",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getStatusType_ProcessSucceeded(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessSucceeded",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getStatusType_ProcessFailed(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessFailed",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getStatusType_CreationTime(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "creationTime"
           });			
        addAnnotation
          (supportedComplexDataInputTypeEClass, 
           source, 
           new String[] {
             "name", "SupportedComplexDataInputType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getSupportedComplexDataInputType_MaximumMegabytes(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "maximumMegabytes"
           });			
        addAnnotation
          (supportedComplexDataTypeEClass, 
           source, 
           new String[] {
             "name", "SupportedComplexDataType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getSupportedComplexDataType_Default(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Default"
           });			
        addAnnotation
          (getSupportedComplexDataType_Supported(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Supported"
           });			
        addAnnotation
          (supportedCRSsTypeEClass, 
           source, 
           new String[] {
             "name", "SupportedCRSsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getSupportedCRSsType_Default(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Default"
           });			
        addAnnotation
          (getSupportedCRSsType_Supported(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Supported"
           });			
        addAnnotation
          (supportedUOMsTypeEClass, 
           source, 
           new String[] {
             "name", "SupportedUOMsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getSupportedUOMsType_Default(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Default"
           });			
        addAnnotation
          (getSupportedUOMsType_Supported(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Supported"
           });			
        addAnnotation
          (uoMsTypeEClass, 
           source, 
           new String[] {
             "name", "UOMsType",
             "kind", "elementOnly"
           });				
        addAnnotation
          (valuesReferenceTypeEClass, 
           source, 
           new String[] {
             "name", "ValuesReferenceType",
             "kind", "empty"
           });			
        addAnnotation
          (getValuesReferenceType_Reference(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "reference",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (getValuesReferenceType_ValuesForm(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "valuesForm"
           });			
        addAnnotation
          (wpsCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "WPSCapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getWPSCapabilitiesType_ProcessOfferings(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ProcessOfferings",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getWPSCapabilitiesType_Languages(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Languages",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getWPSCapabilitiesType_WSDL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WSDL",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getWPSCapabilitiesType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });			
        addAnnotation
          (getWPSCapabilitiesType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });		
        addAnnotation
          (wsdlTypeEClass, 
           source, 
           new String[] {
             "name", "WSDL_._type",
             "kind", "empty"
           });		
        addAnnotation
          (getWSDLType_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (methodTypeEEnum, 
           source, 
           new String[] {
             "name", "method_._type"
           });		
        addAnnotation
          (methodTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "method_._type:Object",
             "baseType", "method_._type"
           });		
        addAnnotation
          (percentCompletedTypeEDataType, 
           source, 
           new String[] {
             "name", "percentCompleted_._type",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#integer",
             "minInclusive", "0",
             "maxInclusive", "99"
           });
    }

} //Wps10PackageImpl
