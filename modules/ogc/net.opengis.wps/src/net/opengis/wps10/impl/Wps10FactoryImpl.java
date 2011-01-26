/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.math.BigInteger;

import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.wps10.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wps10FactoryImpl extends EFactoryImpl implements Wps10Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Wps10Factory init() {
        try {
            Wps10Factory theWps10Factory = (Wps10Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/wps/1.0.0"); 
            if (theWps10Factory != null) {
                return theWps10Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Wps10FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wps10FactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case Wps10Package.BODY_REFERENCE_TYPE: return createBodyReferenceType();
            case Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE: return createComplexDataCombinationsType();
            case Wps10Package.COMPLEX_DATA_COMBINATION_TYPE: return createComplexDataCombinationType();
            case Wps10Package.COMPLEX_DATA_DESCRIPTION_TYPE: return createComplexDataDescriptionType();
            case Wps10Package.COMPLEX_DATA_TYPE: return createComplexDataType();
            case Wps10Package.CR_SS_TYPE: return createCRSsType();
            case Wps10Package.DATA_INPUTS_TYPE: return createDataInputsType();
            case Wps10Package.DATA_INPUTS_TYPE1: return createDataInputsType1();
            case Wps10Package.DATA_TYPE: return createDataType();
            case Wps10Package.DEFAULT_TYPE: return createDefaultType();
            case Wps10Package.DEFAULT_TYPE1: return createDefaultType1();
            case Wps10Package.DEFAULT_TYPE2: return createDefaultType2();
            case Wps10Package.DESCRIBE_PROCESS_TYPE: return createDescribeProcessType();
            case Wps10Package.DESCRIPTION_TYPE: return createDescriptionType();
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE: return createDocumentOutputDefinitionType();
            case Wps10Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Wps10Package.EXECUTE_RESPONSE_TYPE: return createExecuteResponseType();
            case Wps10Package.EXECUTE_TYPE: return createExecuteType();
            case Wps10Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case Wps10Package.HEADER_TYPE: return createHeaderType();
            case Wps10Package.INPUT_DESCRIPTION_TYPE: return createInputDescriptionType();
            case Wps10Package.INPUT_REFERENCE_TYPE: return createInputReferenceType();
            case Wps10Package.INPUT_TYPE: return createInputType();
            case Wps10Package.LANGUAGES_TYPE: return createLanguagesType();
            case Wps10Package.LANGUAGES_TYPE1: return createLanguagesType1();
            case Wps10Package.LITERAL_DATA_TYPE: return createLiteralDataType();
            case Wps10Package.LITERAL_INPUT_TYPE: return createLiteralInputType();
            case Wps10Package.LITERAL_OUTPUT_TYPE: return createLiteralOutputType();
            case Wps10Package.OUTPUT_DATA_TYPE: return createOutputDataType();
            case Wps10Package.OUTPUT_DEFINITIONS_TYPE: return createOutputDefinitionsType();
            case Wps10Package.OUTPUT_DEFINITION_TYPE: return createOutputDefinitionType();
            case Wps10Package.OUTPUT_DESCRIPTION_TYPE: return createOutputDescriptionType();
            case Wps10Package.OUTPUT_REFERENCE_TYPE: return createOutputReferenceType();
            case Wps10Package.PROCESS_BRIEF_TYPE: return createProcessBriefType();
            case Wps10Package.PROCESS_DESCRIPTIONS_TYPE: return createProcessDescriptionsType();
            case Wps10Package.PROCESS_DESCRIPTION_TYPE: return createProcessDescriptionType();
            case Wps10Package.PROCESS_FAILED_TYPE: return createProcessFailedType();
            case Wps10Package.PROCESS_OFFERINGS_TYPE: return createProcessOfferingsType();
            case Wps10Package.PROCESS_OUTPUTS_TYPE: return createProcessOutputsType();
            case Wps10Package.PROCESS_OUTPUTS_TYPE1: return createProcessOutputsType1();
            case Wps10Package.PROCESS_STARTED_TYPE: return createProcessStartedType();
            case Wps10Package.REQUEST_BASE_TYPE: return createRequestBaseType();
            case Wps10Package.RESPONSE_BASE_TYPE: return createResponseBaseType();
            case Wps10Package.RESPONSE_DOCUMENT_TYPE: return createResponseDocumentType();
            case Wps10Package.RESPONSE_FORM_TYPE: return createResponseFormType();
            case Wps10Package.STATUS_TYPE: return createStatusType();
            case Wps10Package.SUPPORTED_COMPLEX_DATA_INPUT_TYPE: return createSupportedComplexDataInputType();
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE: return createSupportedComplexDataType();
            case Wps10Package.SUPPORTED_CR_SS_TYPE: return createSupportedCRSsType();
            case Wps10Package.SUPPORTED_UO_MS_TYPE: return createSupportedUOMsType();
            case Wps10Package.UO_MS_TYPE: return createUOMsType();
            case Wps10Package.VALUES_REFERENCE_TYPE: return createValuesReferenceType();
            case Wps10Package.WPS_CAPABILITIES_TYPE: return createWPSCapabilitiesType();
            case Wps10Package.WSDL_TYPE: return createWSDLType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case Wps10Package.METHOD_TYPE:
                return createMethodTypeFromString(eDataType, initialValue);
            case Wps10Package.METHOD_TYPE_OBJECT:
                return createMethodTypeObjectFromString(eDataType, initialValue);
            case Wps10Package.PERCENT_COMPLETED_TYPE:
                return createPercentCompletedTypeFromString(eDataType, initialValue);
            case Wps10Package.MAP:
                return createMapFromString(eDataType, initialValue);
            case Wps10Package.QNAME:
                return createQNameFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case Wps10Package.METHOD_TYPE:
                return convertMethodTypeToString(eDataType, instanceValue);
            case Wps10Package.METHOD_TYPE_OBJECT:
                return convertMethodTypeObjectToString(eDataType, instanceValue);
            case Wps10Package.PERCENT_COMPLETED_TYPE:
                return convertPercentCompletedTypeToString(eDataType, instanceValue);
            case Wps10Package.MAP:
                return convertMapToString(eDataType, instanceValue);
            case Wps10Package.QNAME:
                return convertQNameToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BodyReferenceType createBodyReferenceType() {
        BodyReferenceTypeImpl bodyReferenceType = new BodyReferenceTypeImpl();
        return bodyReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComplexDataCombinationsType createComplexDataCombinationsType() {
        ComplexDataCombinationsTypeImpl complexDataCombinationsType = new ComplexDataCombinationsTypeImpl();
        return complexDataCombinationsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComplexDataCombinationType createComplexDataCombinationType() {
        ComplexDataCombinationTypeImpl complexDataCombinationType = new ComplexDataCombinationTypeImpl();
        return complexDataCombinationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComplexDataDescriptionType createComplexDataDescriptionType() {
        ComplexDataDescriptionTypeImpl complexDataDescriptionType = new ComplexDataDescriptionTypeImpl();
        return complexDataDescriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComplexDataType createComplexDataType() {
        ComplexDataTypeImpl complexDataType = new ComplexDataTypeImpl();
        return complexDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CRSsType createCRSsType() {
        CRSsTypeImpl crSsType = new CRSsTypeImpl();
        return crSsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataInputsType createDataInputsType() {
        DataInputsTypeImpl dataInputsType = new DataInputsTypeImpl();
        return dataInputsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataInputsType1 createDataInputsType1() {
        DataInputsType1Impl dataInputsType1 = new DataInputsType1Impl();
        return dataInputsType1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataType createDataType() {
        DataTypeImpl dataType = new DataTypeImpl();
        return dataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefaultType createDefaultType() {
        DefaultTypeImpl defaultType = new DefaultTypeImpl();
        return defaultType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefaultType1 createDefaultType1() {
        DefaultType1Impl defaultType1 = new DefaultType1Impl();
        return defaultType1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefaultType2 createDefaultType2() {
        DefaultType2Impl defaultType2 = new DefaultType2Impl();
        return defaultType2;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeProcessType createDescribeProcessType() {
        DescribeProcessTypeImpl describeProcessType = new DescribeProcessTypeImpl();
        return describeProcessType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescriptionType createDescriptionType() {
        DescriptionTypeImpl descriptionType = new DescriptionTypeImpl();
        return descriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DocumentOutputDefinitionType createDocumentOutputDefinitionType() {
        DocumentOutputDefinitionTypeImpl documentOutputDefinitionType = new DocumentOutputDefinitionTypeImpl();
        return documentOutputDefinitionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DocumentRoot createDocumentRoot() {
        DocumentRootImpl documentRoot = new DocumentRootImpl();
        return documentRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecuteResponseType createExecuteResponseType() {
        ExecuteResponseTypeImpl executeResponseType = new ExecuteResponseTypeImpl();
        return executeResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecuteType createExecuteType() {
        ExecuteTypeImpl executeType = new ExecuteTypeImpl();
        return executeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType createGetCapabilitiesType() {
        GetCapabilitiesTypeImpl getCapabilitiesType = new GetCapabilitiesTypeImpl();
        return getCapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HeaderType createHeaderType() {
        HeaderTypeImpl headerType = new HeaderTypeImpl();
        return headerType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InputDescriptionType createInputDescriptionType() {
        InputDescriptionTypeImpl inputDescriptionType = new InputDescriptionTypeImpl();
        return inputDescriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InputReferenceType createInputReferenceType() {
        InputReferenceTypeImpl inputReferenceType = new InputReferenceTypeImpl();
        return inputReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InputType createInputType() {
        InputTypeImpl inputType = new InputTypeImpl();
        return inputType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguagesType createLanguagesType() {
        LanguagesTypeImpl languagesType = new LanguagesTypeImpl();
        return languagesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguagesType1 createLanguagesType1() {
        LanguagesType1Impl languagesType1 = new LanguagesType1Impl();
        return languagesType1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LiteralDataType createLiteralDataType() {
        LiteralDataTypeImpl literalDataType = new LiteralDataTypeImpl();
        return literalDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LiteralInputType createLiteralInputType() {
        LiteralInputTypeImpl literalInputType = new LiteralInputTypeImpl();
        return literalInputType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LiteralOutputType createLiteralOutputType() {
        LiteralOutputTypeImpl literalOutputType = new LiteralOutputTypeImpl();
        return literalOutputType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputDataType createOutputDataType() {
        OutputDataTypeImpl outputDataType = new OutputDataTypeImpl();
        return outputDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputDefinitionsType createOutputDefinitionsType() {
        OutputDefinitionsTypeImpl outputDefinitionsType = new OutputDefinitionsTypeImpl();
        return outputDefinitionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputDefinitionType createOutputDefinitionType() {
        OutputDefinitionTypeImpl outputDefinitionType = new OutputDefinitionTypeImpl();
        return outputDefinitionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputDescriptionType createOutputDescriptionType() {
        OutputDescriptionTypeImpl outputDescriptionType = new OutputDescriptionTypeImpl();
        return outputDescriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputReferenceType createOutputReferenceType() {
        OutputReferenceTypeImpl outputReferenceType = new OutputReferenceTypeImpl();
        return outputReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessBriefType createProcessBriefType() {
        ProcessBriefTypeImpl processBriefType = new ProcessBriefTypeImpl();
        return processBriefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessDescriptionsType createProcessDescriptionsType() {
        ProcessDescriptionsTypeImpl processDescriptionsType = new ProcessDescriptionsTypeImpl();
        return processDescriptionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessDescriptionType createProcessDescriptionType() {
        ProcessDescriptionTypeImpl processDescriptionType = new ProcessDescriptionTypeImpl();
        return processDescriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessFailedType createProcessFailedType() {
        ProcessFailedTypeImpl processFailedType = new ProcessFailedTypeImpl();
        return processFailedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessOfferingsType createProcessOfferingsType() {
        ProcessOfferingsTypeImpl processOfferingsType = new ProcessOfferingsTypeImpl();
        return processOfferingsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessOutputsType createProcessOutputsType() {
        ProcessOutputsTypeImpl processOutputsType = new ProcessOutputsTypeImpl();
        return processOutputsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessOutputsType1 createProcessOutputsType1() {
        ProcessOutputsType1Impl processOutputsType1 = new ProcessOutputsType1Impl();
        return processOutputsType1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessStartedType createProcessStartedType() {
        ProcessStartedTypeImpl processStartedType = new ProcessStartedTypeImpl();
        return processStartedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequestBaseType createRequestBaseType() {
        RequestBaseTypeImpl requestBaseType = new RequestBaseTypeImpl();
        return requestBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponseBaseType createResponseBaseType() {
        ResponseBaseTypeImpl responseBaseType = new ResponseBaseTypeImpl();
        return responseBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponseDocumentType createResponseDocumentType() {
        ResponseDocumentTypeImpl responseDocumentType = new ResponseDocumentTypeImpl();
        return responseDocumentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponseFormType createResponseFormType() {
        ResponseFormTypeImpl responseFormType = new ResponseFormTypeImpl();
        return responseFormType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StatusType createStatusType() {
        StatusTypeImpl statusType = new StatusTypeImpl();
        return statusType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SupportedComplexDataInputType createSupportedComplexDataInputType() {
        SupportedComplexDataInputTypeImpl supportedComplexDataInputType = new SupportedComplexDataInputTypeImpl();
        return supportedComplexDataInputType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SupportedComplexDataType createSupportedComplexDataType() {
        SupportedComplexDataTypeImpl supportedComplexDataType = new SupportedComplexDataTypeImpl();
        return supportedComplexDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SupportedCRSsType createSupportedCRSsType() {
        SupportedCRSsTypeImpl supportedCRSsType = new SupportedCRSsTypeImpl();
        return supportedCRSsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SupportedUOMsType createSupportedUOMsType() {
        SupportedUOMsTypeImpl supportedUOMsType = new SupportedUOMsTypeImpl();
        return supportedUOMsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UOMsType createUOMsType() {
        UOMsTypeImpl uoMsType = new UOMsTypeImpl();
        return uoMsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValuesReferenceType createValuesReferenceType() {
        ValuesReferenceTypeImpl valuesReferenceType = new ValuesReferenceTypeImpl();
        return valuesReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WPSCapabilitiesType createWPSCapabilitiesType() {
        WPSCapabilitiesTypeImpl wpsCapabilitiesType = new WPSCapabilitiesTypeImpl();
        return wpsCapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WSDLType createWSDLType() {
        WSDLTypeImpl wsdlType = new WSDLTypeImpl();
        return wsdlType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MethodType createMethodTypeFromString(EDataType eDataType, String initialValue) {
        MethodType result = MethodType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertMethodTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MethodType createMethodTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createMethodTypeFromString(Wps10Package.Literals.METHOD_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertMethodTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertMethodTypeToString(Wps10Package.Literals.METHOD_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger createPercentCompletedTypeFromString(EDataType eDataType, String initialValue) {
        return (BigInteger)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INTEGER, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertPercentCompletedTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INTEGER, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Map createMapFromString(EDataType eDataType, String initialValue) {
        return (Map)super.createFromString(eDataType, initialValue);
    }

				/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String convertMapToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

				/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName createQNameFromString(EDataType eDataType, String initialValue) {
        return (QName)super.createFromString(eDataType, initialValue);
    }

                /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertQNameToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

                /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wps10Package getWps10Package() {
        return (Wps10Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static Wps10Package getPackage() {
        return Wps10Package.eINSTANCE;
    }

} //Wps10FactoryImpl
