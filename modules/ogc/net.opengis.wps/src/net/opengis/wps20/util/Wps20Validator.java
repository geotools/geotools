/**
 */
package net.opengis.wps20.util;

import java.math.BigInteger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.opengis.wps20.*;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wps20.Wps20Package
 * @generated
 */
public class Wps20Validator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final Wps20Validator INSTANCE = new Wps20Validator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "net.opengis.wps20";

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
	public Wps20Validator() {
		super();
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return Wps20Package.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		switch (classifierID) {
			case Wps20Package.BODY_REFERENCE_TYPE:
				return validateBodyReferenceType((BodyReferenceType)value, diagnostics, context);
			case Wps20Package.BOUNDING_BOX_DATA_TYPE:
				return validateBoundingBoxDataType((BoundingBoxDataType)value, diagnostics, context);
			case Wps20Package.COMPLEX_DATA_TYPE:
				return validateComplexDataType((ComplexDataType)value, diagnostics, context);
			case Wps20Package.CONTENTS_TYPE:
				return validateContentsType((ContentsType)value, diagnostics, context);
			case Wps20Package.DATA_DESCRIPTION_TYPE:
				return validateDataDescriptionType((DataDescriptionType)value, diagnostics, context);
			case Wps20Package.DATA_INPUT_TYPE:
				return validateDataInputType((DataInputType)value, diagnostics, context);
			case Wps20Package.DATA_OUTPUT_TYPE:
				return validateDataOutputType((DataOutputType)value, diagnostics, context);
			case Wps20Package.DATA_TYPE:
				return validateDataType((DataType)value, diagnostics, context);
			case Wps20Package.DESCRIBE_PROCESS_TYPE:
				return validateDescribeProcessType((DescribeProcessType)value, diagnostics, context);
			case Wps20Package.DESCRIPTION_TYPE:
				return validateDescriptionType((DescriptionType)value, diagnostics, context);
			case Wps20Package.DISMISS_TYPE:
				return validateDismissType((DismissType)value, diagnostics, context);
			case Wps20Package.DOCUMENT_ROOT:
				return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
			case Wps20Package.EXECUTE_REQUEST_TYPE:
				return validateExecuteRequestType((ExecuteRequestType)value, diagnostics, context);
			case Wps20Package.EXTENSION_TYPE:
				return validateExtensionType((ExtensionType)value, diagnostics, context);
			case Wps20Package.FORMAT_TYPE:
				return validateFormatType((FormatType)value, diagnostics, context);
			case Wps20Package.GENERIC_INPUT_TYPE:
				return validateGenericInputType((GenericInputType)value, diagnostics, context);
			case Wps20Package.GENERIC_OUTPUT_TYPE:
				return validateGenericOutputType((GenericOutputType)value, diagnostics, context);
			case Wps20Package.GENERIC_PROCESS_TYPE:
				return validateGenericProcessType((GenericProcessType)value, diagnostics, context);
			case Wps20Package.GET_CAPABILITIES_TYPE:
				return validateGetCapabilitiesType((GetCapabilitiesType)value, diagnostics, context);
			case Wps20Package.GET_RESULT_TYPE:
				return validateGetResultType((GetResultType)value, diagnostics, context);
			case Wps20Package.GET_STATUS_TYPE:
				return validateGetStatusType((GetStatusType)value, diagnostics, context);
			case Wps20Package.INPUT_DESCRIPTION_TYPE:
				return validateInputDescriptionType((InputDescriptionType)value, diagnostics, context);
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE:
				return validateLiteralDataDomainType((LiteralDataDomainType)value, diagnostics, context);
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE1:
				return validateLiteralDataDomainType1((LiteralDataDomainType1)value, diagnostics, context);
			case Wps20Package.LITERAL_DATA_TYPE:
				return validateLiteralDataType((LiteralDataType)value, diagnostics, context);
			case Wps20Package.LITERAL_VALUE_TYPE:
				return validateLiteralValueType((LiteralValueType)value, diagnostics, context);
			case Wps20Package.OUTPUT_DEFINITION_TYPE:
				return validateOutputDefinitionType((OutputDefinitionType)value, diagnostics, context);
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE:
				return validateOutputDescriptionType((OutputDescriptionType)value, diagnostics, context);
			case Wps20Package.PROCESS_DESCRIPTION_TYPE:
				return validateProcessDescriptionType((ProcessDescriptionType)value, diagnostics, context);
			case Wps20Package.PROCESS_OFFERINGS_TYPE:
				return validateProcessOfferingsType((ProcessOfferingsType)value, diagnostics, context);
			case Wps20Package.PROCESS_OFFERING_TYPE:
				return validateProcessOfferingType((ProcessOfferingType)value, diagnostics, context);
			case Wps20Package.PROCESS_SUMMARY_TYPE:
				return validateProcessSummaryType((ProcessSummaryType)value, diagnostics, context);
			case Wps20Package.REFERENCE_TYPE:
				return validateReferenceType((ReferenceType)value, diagnostics, context);
			case Wps20Package.REQUEST_BASE_TYPE:
				return validateRequestBaseType((RequestBaseType)value, diagnostics, context);
			case Wps20Package.RESULT_TYPE:
				return validateResultType((ResultType)value, diagnostics, context);
			case Wps20Package.STATUS_INFO_TYPE:
				return validateStatusInfoType((StatusInfoType)value, diagnostics, context);
			case Wps20Package.SUPPORTED_CRS_TYPE:
				return validateSupportedCRSType((SupportedCRSType)value, diagnostics, context);
			case Wps20Package.WPS_CAPABILITIES_TYPE:
				return validateWPSCapabilitiesType((WPSCapabilitiesType)value, diagnostics, context);
			case Wps20Package.DATA_TRANSMISSION_MODE_TYPE:
				return validateDataTransmissionModeType((DataTransmissionModeType)value, diagnostics, context);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER0:
				return validateJobControlOptionsTypeMember0((JobControlOptionsTypeMember0)value, diagnostics, context);
			case Wps20Package.MODE_TYPE:
				return validateModeType((ModeType)value, diagnostics, context);
			case Wps20Package.RESPONSE_TYPE:
				return validateResponseType((ResponseType)value, diagnostics, context);
			case Wps20Package.STATUS_TYPE_MEMBER0:
				return validateStatusTypeMember0((StatusTypeMember0)value, diagnostics, context);
			case Wps20Package.DATA_TRANSMISSION_MODE_TYPE_OBJECT:
				return validateDataTransmissionModeTypeObject((DataTransmissionModeType)value, diagnostics, context);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE:
				return validateJobControlOptionsType(value, diagnostics, context);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE1:
				return validateJobControlOptionsType1((List<?>)value, diagnostics, context);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER0_OBJECT:
				return validateJobControlOptionsTypeMember0Object((JobControlOptionsTypeMember0)value, diagnostics, context);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER1:
				return validateJobControlOptionsTypeMember1((String)value, diagnostics, context);
			case Wps20Package.MODE_TYPE_OBJECT:
				return validateModeTypeObject((ModeType)value, diagnostics, context);
			case Wps20Package.OUTPUT_TRANSMISSION_TYPE:
				return validateOutputTransmissionType((List<?>)value, diagnostics, context);
			case Wps20Package.PERCENT_COMPLETED_TYPE:
				return validatePercentCompletedType((BigInteger)value, diagnostics, context);
			case Wps20Package.RESPONSE_TYPE_OBJECT:
				return validateResponseTypeObject((ResponseType)value, diagnostics, context);
			case Wps20Package.STATUS_TYPE:
				return validateStatusType(value, diagnostics, context);
			case Wps20Package.STATUS_TYPE_MEMBER0_OBJECT:
				return validateStatusTypeMember0Object((StatusTypeMember0)value, diagnostics, context);
			case Wps20Package.STATUS_TYPE_MEMBER1:
				return validateStatusTypeMember1((String)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBodyReferenceType(BodyReferenceType bodyReferenceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(bodyReferenceType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBoundingBoxDataType(BoundingBoxDataType boundingBoxDataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(boundingBoxDataType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateComplexDataType(ComplexDataType complexDataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(complexDataType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateContentsType(ContentsType contentsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(contentsType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDataDescriptionType(DataDescriptionType dataDescriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(dataDescriptionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDataInputType(DataInputType dataInputType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(dataInputType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDataOutputType(DataOutputType dataOutputType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(dataOutputType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDataType(DataType dataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(dataType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDescribeProcessType(DescribeProcessType describeProcessType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(describeProcessType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDescriptionType(DescriptionType descriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(descriptionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDismissType(DismissType dismissType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(dismissType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateExecuteRequestType(ExecuteRequestType executeRequestType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(executeRequestType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateExtensionType(ExtensionType extensionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(extensionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFormatType(FormatType formatType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(formatType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGenericInputType(GenericInputType genericInputType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(genericInputType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGenericOutputType(GenericOutputType genericOutputType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(genericOutputType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGenericProcessType(GenericProcessType genericProcessType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(genericProcessType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGetCapabilitiesType(GetCapabilitiesType getCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(getCapabilitiesType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGetResultType(GetResultType getResultType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(getResultType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGetStatusType(GetStatusType getStatusType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(getStatusType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateInputDescriptionType(InputDescriptionType inputDescriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(inputDescriptionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLiteralDataDomainType(LiteralDataDomainType literalDataDomainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(literalDataDomainType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLiteralDataDomainType1(LiteralDataDomainType1 literalDataDomainType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(literalDataDomainType1, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLiteralDataType(LiteralDataType literalDataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(literalDataType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLiteralValueType(LiteralValueType literalValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(literalValueType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOutputDefinitionType(OutputDefinitionType outputDefinitionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(outputDefinitionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOutputDescriptionType(OutputDescriptionType outputDescriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(outputDescriptionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateProcessDescriptionType(ProcessDescriptionType processDescriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(processDescriptionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateProcessOfferingsType(ProcessOfferingsType processOfferingsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(processOfferingsType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateProcessOfferingType(ProcessOfferingType processOfferingType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(processOfferingType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateProcessSummaryType(ProcessSummaryType processSummaryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(processSummaryType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateReferenceType(ReferenceType referenceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(referenceType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRequestBaseType(RequestBaseType requestBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(requestBaseType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateResultType(ResultType resultType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(resultType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStatusInfoType(StatusInfoType statusInfoType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(statusInfoType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSupportedCRSType(SupportedCRSType supportedCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(supportedCRSType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateWPSCapabilitiesType(WPSCapabilitiesType wpsCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(wpsCapabilitiesType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDataTransmissionModeType(DataTransmissionModeType dataTransmissionModeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateJobControlOptionsTypeMember0(JobControlOptionsTypeMember0 jobControlOptionsTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateModeType(ModeType modeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateResponseType(ResponseType responseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStatusTypeMember0(StatusTypeMember0 statusTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDataTransmissionModeTypeObject(DataTransmissionModeType dataTransmissionModeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateJobControlOptionsType(Object jobControlOptionsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateJobControlOptionsType_MemberTypes(jobControlOptionsType, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Job Control Options Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateJobControlOptionsType_MemberTypes(Object jobControlOptionsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER0.isInstance(jobControlOptionsType)) {
				if (validateJobControlOptionsTypeMember0((JobControlOptionsTypeMember0)jobControlOptionsType, tempDiagnostics, context)) return true;
			}
			if (Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER1.isInstance(jobControlOptionsType)) {
				if (validateJobControlOptionsTypeMember1((String)jobControlOptionsType, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER0.isInstance(jobControlOptionsType)) {
				if (validateJobControlOptionsTypeMember0((JobControlOptionsTypeMember0)jobControlOptionsType, null, context)) return true;
			}
			if (Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER1.isInstance(jobControlOptionsType)) {
				if (validateJobControlOptionsTypeMember1((String)jobControlOptionsType, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateJobControlOptionsType1(List<?> jobControlOptionsType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateJobControlOptionsType1_ItemType(jobControlOptionsType1, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Job Control Options Type1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateJobControlOptionsType1_ItemType(List<?> jobControlOptionsType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = jobControlOptionsType1.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE.isInstance(item)) {
				result &= validateJobControlOptionsType(item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateJobControlOptionsTypeMember0Object(JobControlOptionsTypeMember0 jobControlOptionsTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateJobControlOptionsTypeMember1(String jobControlOptionsTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateModeTypeObject(ModeType modeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOutputTransmissionType(List<?> outputTransmissionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateOutputTransmissionType_ItemType(outputTransmissionType, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Output Transmission Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOutputTransmissionType_ItemType(List<?> outputTransmissionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = outputTransmissionType.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (Wps20Package.Literals.DATA_TRANSMISSION_MODE_TYPE.isInstance(item)) {
				result &= validateDataTransmissionModeType((DataTransmissionModeType)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(Wps20Package.Literals.DATA_TRANSMISSION_MODE_TYPE, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePercentCompletedType(BigInteger percentCompletedType, DiagnosticChain diagnostics, Map<Object, Object> context) {
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
	public boolean validatePercentCompletedType_Min(BigInteger percentCompletedType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = percentCompletedType.compareTo(PERCENT_COMPLETED_TYPE__MIN__VALUE) >= 0;
		if (!result && diagnostics != null)
			reportMinViolation(Wps20Package.Literals.PERCENT_COMPLETED_TYPE, percentCompletedType, PERCENT_COMPLETED_TYPE__MIN__VALUE, true, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validatePercentCompletedType_Max
	 */
	public static final BigInteger PERCENT_COMPLETED_TYPE__MAX__VALUE = new BigInteger("100");

	/**
	 * Validates the Max constraint of '<em>Percent Completed Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePercentCompletedType_Max(BigInteger percentCompletedType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = percentCompletedType.compareTo(PERCENT_COMPLETED_TYPE__MAX__VALUE) <= 0;
		if (!result && diagnostics != null)
			reportMaxViolation(Wps20Package.Literals.PERCENT_COMPLETED_TYPE, percentCompletedType, PERCENT_COMPLETED_TYPE__MAX__VALUE, true, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateResponseTypeObject(ResponseType responseTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStatusType(Object statusType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateStatusType_MemberTypes(statusType, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Status Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStatusType_MemberTypes(Object statusType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (Wps20Package.Literals.STATUS_TYPE_MEMBER0.isInstance(statusType)) {
				if (validateStatusTypeMember0((StatusTypeMember0)statusType, tempDiagnostics, context)) return true;
			}
			if (Wps20Package.Literals.STATUS_TYPE_MEMBER1.isInstance(statusType)) {
				if (validateStatusTypeMember1((String)statusType, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (Wps20Package.Literals.STATUS_TYPE_MEMBER0.isInstance(statusType)) {
				if (validateStatusTypeMember0((StatusTypeMember0)statusType, null, context)) return true;
			}
			if (Wps20Package.Literals.STATUS_TYPE_MEMBER1.isInstance(statusType)) {
				if (validateStatusTypeMember1((String)statusType, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStatusTypeMember0Object(StatusTypeMember0 statusTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStatusTypeMember1(String statusTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} //Wps20Validator
