/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.util;

import java.math.BigInteger;

import java.util.Map;

import javax.measure.unit.Unit;
import javax.xml.namespace.QName;
import net.opengis.wps10.*;

import org.eclipse.emf.common.util.DiagnosticChain;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wps10.Wps10Package
 * @generated
 */
public class Wps10Validator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final Wps10Validator INSTANCE = new Wps10Validator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "net.opengis.wps10";

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

    /**
     * The cached base package validator.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XMLTypeValidator xmlTypeValidator;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wps10Validator() {
        super();
        xmlTypeValidator = XMLTypeValidator.INSTANCE;
    }

    /**
     * Returns the package of this validator switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EPackage getEPackage() {
      return Wps10Package.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresponding classifier of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map context) {
        switch (classifierID) {
            case Wps10Package.BODY_REFERENCE_TYPE:
                return validateBodyReferenceType((BodyReferenceType)value, diagnostics, context);
            case Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE:
                return validateComplexDataCombinationsType((ComplexDataCombinationsType)value, diagnostics, context);
            case Wps10Package.COMPLEX_DATA_COMBINATION_TYPE:
                return validateComplexDataCombinationType((ComplexDataCombinationType)value, diagnostics, context);
            case Wps10Package.COMPLEX_DATA_DESCRIPTION_TYPE:
                return validateComplexDataDescriptionType((ComplexDataDescriptionType)value, diagnostics, context);
            case Wps10Package.COMPLEX_DATA_TYPE:
                return validateComplexDataType((ComplexDataType)value, diagnostics, context);
            case Wps10Package.CR_SS_TYPE:
                return validateCRSsType((CRSsType)value, diagnostics, context);
            case Wps10Package.DATA_INPUTS_TYPE:
                return validateDataInputsType((DataInputsType)value, diagnostics, context);
            case Wps10Package.DATA_INPUTS_TYPE1:
                return validateDataInputsType1((DataInputsType1)value, diagnostics, context);
            case Wps10Package.DATA_TYPE:
                return validateDataType((DataType)value, diagnostics, context);
            case Wps10Package.DEFAULT_TYPE:
                return validateDefaultType((DefaultType)value, diagnostics, context);
            case Wps10Package.DEFAULT_TYPE1:
                return validateDefaultType1((DefaultType1)value, diagnostics, context);
            case Wps10Package.DEFAULT_TYPE2:
                return validateDefaultType2((DefaultType2)value, diagnostics, context);
            case Wps10Package.DESCRIBE_PROCESS_TYPE:
                return validateDescribeProcessType((DescribeProcessType)value, diagnostics, context);
            case Wps10Package.DESCRIPTION_TYPE:
                return validateDescriptionType((DescriptionType)value, diagnostics, context);
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE:
                return validateDocumentOutputDefinitionType((DocumentOutputDefinitionType)value, diagnostics, context);
            case Wps10Package.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case Wps10Package.EXECUTE_RESPONSE_TYPE:
                return validateExecuteResponseType((ExecuteResponseType)value, diagnostics, context);
            case Wps10Package.EXECUTE_TYPE:
                return validateExecuteType((ExecuteType)value, diagnostics, context);
            case Wps10Package.GET_CAPABILITIES_TYPE:
                return validateGetCapabilitiesType((GetCapabilitiesType)value, diagnostics, context);
            case Wps10Package.HEADER_TYPE:
                return validateHeaderType((HeaderType)value, diagnostics, context);
            case Wps10Package.INPUT_DESCRIPTION_TYPE:
                return validateInputDescriptionType((InputDescriptionType)value, diagnostics, context);
            case Wps10Package.INPUT_REFERENCE_TYPE:
                return validateInputReferenceType((InputReferenceType)value, diagnostics, context);
            case Wps10Package.INPUT_TYPE:
                return validateInputType((InputType)value, diagnostics, context);
            case Wps10Package.LANGUAGES_TYPE:
                return validateLanguagesType((LanguagesType)value, diagnostics, context);
            case Wps10Package.LANGUAGES_TYPE1:
                return validateLanguagesType1((LanguagesType1)value, diagnostics, context);
            case Wps10Package.LITERAL_DATA_TYPE:
                return validateLiteralDataType((LiteralDataType)value, diagnostics, context);
            case Wps10Package.LITERAL_INPUT_TYPE:
                return validateLiteralInputType((LiteralInputType)value, diagnostics, context);
            case Wps10Package.LITERAL_OUTPUT_TYPE:
                return validateLiteralOutputType((LiteralOutputType)value, diagnostics, context);
            case Wps10Package.OUTPUT_DATA_TYPE:
                return validateOutputDataType((OutputDataType)value, diagnostics, context);
            case Wps10Package.OUTPUT_DEFINITIONS_TYPE:
                return validateOutputDefinitionsType((OutputDefinitionsType)value, diagnostics, context);
            case Wps10Package.OUTPUT_DEFINITION_TYPE:
                return validateOutputDefinitionType((OutputDefinitionType)value, diagnostics, context);
            case Wps10Package.OUTPUT_DESCRIPTION_TYPE:
                return validateOutputDescriptionType((OutputDescriptionType)value, diagnostics, context);
            case Wps10Package.OUTPUT_REFERENCE_TYPE:
                return validateOutputReferenceType((OutputReferenceType)value, diagnostics, context);
            case Wps10Package.PROCESS_BRIEF_TYPE:
                return validateProcessBriefType((ProcessBriefType)value, diagnostics, context);
            case Wps10Package.PROCESS_DESCRIPTIONS_TYPE:
                return validateProcessDescriptionsType((ProcessDescriptionsType)value, diagnostics, context);
            case Wps10Package.PROCESS_DESCRIPTION_TYPE:
                return validateProcessDescriptionType((ProcessDescriptionType)value, diagnostics, context);
            case Wps10Package.PROCESS_FAILED_TYPE:
                return validateProcessFailedType((ProcessFailedType)value, diagnostics, context);
            case Wps10Package.PROCESS_OFFERINGS_TYPE:
                return validateProcessOfferingsType((ProcessOfferingsType)value, diagnostics, context);
            case Wps10Package.PROCESS_OUTPUTS_TYPE:
                return validateProcessOutputsType((ProcessOutputsType)value, diagnostics, context);
            case Wps10Package.PROCESS_OUTPUTS_TYPE1:
                return validateProcessOutputsType1((ProcessOutputsType1)value, diagnostics, context);
            case Wps10Package.PROCESS_STARTED_TYPE:
                return validateProcessStartedType((ProcessStartedType)value, diagnostics, context);
            case Wps10Package.REQUEST_BASE_TYPE:
                return validateRequestBaseType((RequestBaseType)value, diagnostics, context);
            case Wps10Package.RESPONSE_BASE_TYPE:
                return validateResponseBaseType((ResponseBaseType)value, diagnostics, context);
            case Wps10Package.RESPONSE_DOCUMENT_TYPE:
                return validateResponseDocumentType((ResponseDocumentType)value, diagnostics, context);
            case Wps10Package.RESPONSE_FORM_TYPE:
                return validateResponseFormType((ResponseFormType)value, diagnostics, context);
            case Wps10Package.STATUS_TYPE:
                return validateStatusType((StatusType)value, diagnostics, context);
            case Wps10Package.SUPPORTED_COMPLEX_DATA_INPUT_TYPE:
                return validateSupportedComplexDataInputType((SupportedComplexDataInputType)value, diagnostics, context);
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE:
                return validateSupportedComplexDataType((SupportedComplexDataType)value, diagnostics, context);
            case Wps10Package.SUPPORTED_CR_SS_TYPE:
                return validateSupportedCRSsType((SupportedCRSsType)value, diagnostics, context);
            case Wps10Package.SUPPORTED_UO_MS_TYPE:
                return validateSupportedUOMsType((SupportedUOMsType)value, diagnostics, context);
            case Wps10Package.UO_MS_TYPE:
                return validateUOMsType((UOMsType)value, diagnostics, context);
            case Wps10Package.VALUES_REFERENCE_TYPE:
                return validateValuesReferenceType((ValuesReferenceType)value, diagnostics, context);
            case Wps10Package.WPS_CAPABILITIES_TYPE:
                return validateWPSCapabilitiesType((WPSCapabilitiesType)value, diagnostics, context);
            case Wps10Package.WSDL_TYPE:
                return validateWSDLType((WSDLType)value, diagnostics, context);
            case Wps10Package.UNIT:
                return validateUnit((Unit)value, diagnostics, context);
            case Wps10Package.METHOD_TYPE:
                return validateMethodType((MethodType)value, diagnostics, context);
            case Wps10Package.METHOD_TYPE_OBJECT:
                return validateMethodTypeObject((MethodType)value, diagnostics, context);
            case Wps10Package.PERCENT_COMPLETED_TYPE:
                return validatePercentCompletedType((BigInteger)value, diagnostics, context);
            case Wps10Package.MAP:
                return validateMap((Map)value, diagnostics, context);
            case Wps10Package.QNAME:
                return validateQName((QName)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBodyReferenceType(BodyReferenceType bodyReferenceType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(bodyReferenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComplexDataCombinationsType(ComplexDataCombinationsType complexDataCombinationsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(complexDataCombinationsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComplexDataCombinationType(ComplexDataCombinationType complexDataCombinationType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(complexDataCombinationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComplexDataDescriptionType(ComplexDataDescriptionType complexDataDescriptionType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(complexDataDescriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComplexDataType(ComplexDataType complexDataType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(complexDataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCRSsType(CRSsType crSsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(crSsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDataInputsType(DataInputsType dataInputsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(dataInputsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDataInputsType1(DataInputsType1 dataInputsType1, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(dataInputsType1, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDataType(DataType dataType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(dataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDefaultType(DefaultType defaultType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(defaultType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDefaultType1(DefaultType1 defaultType1, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(defaultType1, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDefaultType2(DefaultType2 defaultType2, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(defaultType2, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDescribeProcessType(DescribeProcessType describeProcessType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(describeProcessType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDescriptionType(DescriptionType descriptionType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(descriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDocumentOutputDefinitionType(DocumentOutputDefinitionType documentOutputDefinitionType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(documentOutputDefinitionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExecuteResponseType(ExecuteResponseType executeResponseType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(executeResponseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExecuteType(ExecuteType executeType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(executeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetCapabilitiesType(GetCapabilitiesType getCapabilitiesType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(getCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateHeaderType(HeaderType headerType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(headerType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateInputDescriptionType(InputDescriptionType inputDescriptionType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(inputDescriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateInputReferenceType(InputReferenceType inputReferenceType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(inputReferenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateInputType(InputType inputType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(inputType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLanguagesType(LanguagesType languagesType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(languagesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLanguagesType1(LanguagesType1 languagesType1, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(languagesType1, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLiteralDataType(LiteralDataType literalDataType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(literalDataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLiteralInputType(LiteralInputType literalInputType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(literalInputType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLiteralOutputType(LiteralOutputType literalOutputType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(literalOutputType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOutputDataType(OutputDataType outputDataType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(outputDataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOutputDefinitionsType(OutputDefinitionsType outputDefinitionsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(outputDefinitionsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOutputDefinitionType(OutputDefinitionType outputDefinitionType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(outputDefinitionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOutputDescriptionType(OutputDescriptionType outputDescriptionType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(outputDescriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOutputReferenceType(OutputReferenceType outputReferenceType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(outputReferenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessBriefType(ProcessBriefType processBriefType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(processBriefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessDescriptionsType(ProcessDescriptionsType processDescriptionsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(processDescriptionsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessDescriptionType(ProcessDescriptionType processDescriptionType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(processDescriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessFailedType(ProcessFailedType processFailedType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(processFailedType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessOfferingsType(ProcessOfferingsType processOfferingsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(processOfferingsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessOutputsType(ProcessOutputsType processOutputsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(processOutputsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessOutputsType1(ProcessOutputsType1 processOutputsType1, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(processOutputsType1, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProcessStartedType(ProcessStartedType processStartedType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(processStartedType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRequestBaseType(RequestBaseType requestBaseType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(requestBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResponseBaseType(ResponseBaseType responseBaseType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(responseBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResponseDocumentType(ResponseDocumentType responseDocumentType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(responseDocumentType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResponseFormType(ResponseFormType responseFormType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(responseFormType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStatusType(StatusType statusType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(statusType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSupportedComplexDataInputType(SupportedComplexDataInputType supportedComplexDataInputType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(supportedComplexDataInputType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSupportedComplexDataType(SupportedComplexDataType supportedComplexDataType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(supportedComplexDataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSupportedCRSsType(SupportedCRSsType supportedCRSsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(supportedCRSsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSupportedUOMsType(SupportedUOMsType supportedUOMsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(supportedUOMsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUOMsType(UOMsType uoMsType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(uoMsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValuesReferenceType(ValuesReferenceType valuesReferenceType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(valuesReferenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateWPSCapabilitiesType(WPSCapabilitiesType wpsCapabilitiesType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(wpsCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateWSDLType(WSDLType wsdlType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(wsdlType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnit(Unit unit, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint((EObject)unit, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMethodType(MethodType methodType, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMethodTypeObject(MethodType methodTypeObject, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePercentCompletedType(BigInteger percentCompletedType, DiagnosticChain diagnostics, Map context) {
        boolean result = validatePercentCompletedType_Min(percentCompletedType, diagnostics, context);
        if (result || diagnostics != null) result &= validatePercentCompletedType_Max(percentCompletedType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validatePercentCompletedType_Min
     */
    public static final BigInteger PERCENT_COMPLETED_TYPE__MIN__VALUE = new BigInteger("0");

    /**
     * Validates the Min constraint of '<em>Percent Completed Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePercentCompletedType_Min(BigInteger percentCompletedType, DiagnosticChain diagnostics, Map context) {
        boolean result = percentCompletedType.compareTo(PERCENT_COMPLETED_TYPE__MIN__VALUE) >= 0;
        if (!result && diagnostics != null)
            reportMinViolation(Wps10Package.Literals.PERCENT_COMPLETED_TYPE, percentCompletedType, PERCENT_COMPLETED_TYPE__MIN__VALUE, true, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validatePercentCompletedType_Max
     */
    public static final BigInteger PERCENT_COMPLETED_TYPE__MAX__VALUE = new BigInteger("99");

    /**
     * Validates the Max constraint of '<em>Percent Completed Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePercentCompletedType_Max(BigInteger percentCompletedType, DiagnosticChain diagnostics, Map context) {
        boolean result = percentCompletedType.compareTo(PERCENT_COMPLETED_TYPE__MAX__VALUE) <= 0;
        if (!result && diagnostics != null)
            reportMaxViolation(Wps10Package.Literals.PERCENT_COMPLETED_TYPE, percentCompletedType, PERCENT_COMPLETED_TYPE__MAX__VALUE, true, diagnostics, context);
        return result;
    }

				/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean validateMap(Map map, DiagnosticChain diagnostics, Map context) {
        return true;
    }

                /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQName(QName qName, DiagnosticChain diagnostics, Map context) {
        return true;
    }

} //Wps10Validator
