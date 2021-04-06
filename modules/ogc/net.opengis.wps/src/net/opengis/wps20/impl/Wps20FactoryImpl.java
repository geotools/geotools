/**
 */
package net.opengis.wps20.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import net.opengis.wps20.BodyReferenceType;
import net.opengis.wps20.BoundingBoxDataType;
import net.opengis.wps20.ComplexDataType;
import net.opengis.wps20.ContentsType;
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
import net.opengis.wps20.ResponseType;
import net.opengis.wps20.ResultType;
import net.opengis.wps20.StatusInfoType;
import net.opengis.wps20.StatusTypeMember0;
import net.opengis.wps20.SupportedCRSType;
import net.opengis.wps20.WPSCapabilitiesType;
import net.opengis.wps20.Wps20Factory;
import net.opengis.wps20.Wps20Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wps20FactoryImpl extends EFactoryImpl implements Wps20Factory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Wps20Factory init() {
		try {
			Wps20Factory theWps20Factory = (Wps20Factory)EPackage.Registry.INSTANCE.getEFactory(Wps20Package.eNS_URI);
			if (theWps20Factory != null) {
				return theWps20Factory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Wps20FactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wps20FactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Wps20Package.BODY_REFERENCE_TYPE: return createBodyReferenceType();
			case Wps20Package.BOUNDING_BOX_DATA_TYPE: return createBoundingBoxDataType();
			case Wps20Package.COMPLEX_DATA_TYPE: return createComplexDataType();
			case Wps20Package.CONTENTS_TYPE: return createContentsType();
			case Wps20Package.DATA_INPUT_TYPE: return createDataInputType();
			case Wps20Package.DATA_OUTPUT_TYPE: return createDataOutputType();
			case Wps20Package.DATA_TYPE: return createDataType();
			case Wps20Package.DESCRIBE_PROCESS_TYPE: return createDescribeProcessType();
			case Wps20Package.DESCRIPTION_TYPE: return createDescriptionType();
			case Wps20Package.DISMISS_TYPE: return createDismissType();
			case Wps20Package.DOCUMENT_ROOT: return createDocumentRoot();
			case Wps20Package.EXECUTE_REQUEST_TYPE: return createExecuteRequestType();
			case Wps20Package.EXTENSION_TYPE: return createExtensionType();
			case Wps20Package.FORMAT_TYPE: return createFormatType();
			case Wps20Package.GENERIC_INPUT_TYPE: return createGenericInputType();
			case Wps20Package.GENERIC_OUTPUT_TYPE: return createGenericOutputType();
			case Wps20Package.GENERIC_PROCESS_TYPE: return createGenericProcessType();
			case Wps20Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
			case Wps20Package.GET_RESULT_TYPE: return createGetResultType();
			case Wps20Package.GET_STATUS_TYPE: return createGetStatusType();
			case Wps20Package.INPUT_DESCRIPTION_TYPE: return createInputDescriptionType();
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE: return createLiteralDataDomainType();
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE1: return createLiteralDataDomainType1();
			case Wps20Package.LITERAL_DATA_TYPE: return createLiteralDataType();
			case Wps20Package.LITERAL_VALUE_TYPE: return createLiteralValueType();
			case Wps20Package.OUTPUT_DEFINITION_TYPE: return createOutputDefinitionType();
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE: return createOutputDescriptionType();
			case Wps20Package.PROCESS_DESCRIPTION_TYPE: return createProcessDescriptionType();
			case Wps20Package.PROCESS_OFFERINGS_TYPE: return createProcessOfferingsType();
			case Wps20Package.PROCESS_OFFERING_TYPE: return createProcessOfferingType();
			case Wps20Package.PROCESS_SUMMARY_TYPE: return createProcessSummaryType();
			case Wps20Package.REFERENCE_TYPE: return createReferenceType();
			case Wps20Package.RESULT_TYPE: return createResultType();
			case Wps20Package.STATUS_INFO_TYPE: return createStatusInfoType();
			case Wps20Package.SUPPORTED_CRS_TYPE: return createSupportedCRSType();
			case Wps20Package.WPS_CAPABILITIES_TYPE: return createWPSCapabilitiesType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case Wps20Package.DATA_TRANSMISSION_MODE_TYPE:
				return createDataTransmissionModeTypeFromString(eDataType, initialValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER0:
				return createJobControlOptionsTypeMember0FromString(eDataType, initialValue);
			case Wps20Package.MODE_TYPE:
				return createModeTypeFromString(eDataType, initialValue);
			case Wps20Package.RESPONSE_TYPE:
				return createResponseTypeFromString(eDataType, initialValue);
			case Wps20Package.STATUS_TYPE_MEMBER0:
				return createStatusTypeMember0FromString(eDataType, initialValue);
			case Wps20Package.DATA_TRANSMISSION_MODE_TYPE_OBJECT:
				return createDataTransmissionModeTypeObjectFromString(eDataType, initialValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE:
				return createJobControlOptionsTypeFromString(eDataType, initialValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE1:
				return createJobControlOptionsType1FromString(eDataType, initialValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER0_OBJECT:
				return createJobControlOptionsTypeMember0ObjectFromString(eDataType, initialValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER1:
				return createJobControlOptionsTypeMember1FromString(eDataType, initialValue);
			case Wps20Package.MODE_TYPE_OBJECT:
				return createModeTypeObjectFromString(eDataType, initialValue);
			case Wps20Package.OUTPUT_TRANSMISSION_TYPE:
				return createOutputTransmissionTypeFromString(eDataType, initialValue);
			case Wps20Package.PERCENT_COMPLETED_TYPE:
				return createPercentCompletedTypeFromString(eDataType, initialValue);
			case Wps20Package.RESPONSE_TYPE_OBJECT:
				return createResponseTypeObjectFromString(eDataType, initialValue);
			case Wps20Package.STATUS_TYPE:
				return createStatusTypeFromString(eDataType, initialValue);
			case Wps20Package.STATUS_TYPE_MEMBER0_OBJECT:
				return createStatusTypeMember0ObjectFromString(eDataType, initialValue);
			case Wps20Package.STATUS_TYPE_MEMBER1:
				return createStatusTypeMember1FromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case Wps20Package.DATA_TRANSMISSION_MODE_TYPE:
				return convertDataTransmissionModeTypeToString(eDataType, instanceValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER0:
				return convertJobControlOptionsTypeMember0ToString(eDataType, instanceValue);
			case Wps20Package.MODE_TYPE:
				return convertModeTypeToString(eDataType, instanceValue);
			case Wps20Package.RESPONSE_TYPE:
				return convertResponseTypeToString(eDataType, instanceValue);
			case Wps20Package.STATUS_TYPE_MEMBER0:
				return convertStatusTypeMember0ToString(eDataType, instanceValue);
			case Wps20Package.DATA_TRANSMISSION_MODE_TYPE_OBJECT:
				return convertDataTransmissionModeTypeObjectToString(eDataType, instanceValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE:
				return convertJobControlOptionsTypeToString(eDataType, instanceValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE1:
				return convertJobControlOptionsType1ToString(eDataType, instanceValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER0_OBJECT:
				return convertJobControlOptionsTypeMember0ObjectToString(eDataType, instanceValue);
			case Wps20Package.JOB_CONTROL_OPTIONS_TYPE_MEMBER1:
				return convertJobControlOptionsTypeMember1ToString(eDataType, instanceValue);
			case Wps20Package.MODE_TYPE_OBJECT:
				return convertModeTypeObjectToString(eDataType, instanceValue);
			case Wps20Package.OUTPUT_TRANSMISSION_TYPE:
				return convertOutputTransmissionTypeToString(eDataType, instanceValue);
			case Wps20Package.PERCENT_COMPLETED_TYPE:
				return convertPercentCompletedTypeToString(eDataType, instanceValue);
			case Wps20Package.RESPONSE_TYPE_OBJECT:
				return convertResponseTypeObjectToString(eDataType, instanceValue);
			case Wps20Package.STATUS_TYPE:
				return convertStatusTypeToString(eDataType, instanceValue);
			case Wps20Package.STATUS_TYPE_MEMBER0_OBJECT:
				return convertStatusTypeMember0ObjectToString(eDataType, instanceValue);
			case Wps20Package.STATUS_TYPE_MEMBER1:
				return convertStatusTypeMember1ToString(eDataType, instanceValue);
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
	public BoundingBoxDataType createBoundingBoxDataType() {
		BoundingBoxDataTypeImpl boundingBoxDataType = new BoundingBoxDataTypeImpl();
		return boundingBoxDataType;
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
	public ContentsType createContentsType() {
		ContentsTypeImpl contentsType = new ContentsTypeImpl();
		return contentsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataInputType createDataInputType() {
		DataInputTypeImpl dataInputType = new DataInputTypeImpl();
		return dataInputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataOutputType createDataOutputType() {
		DataOutputTypeImpl dataOutputType = new DataOutputTypeImpl();
		return dataOutputType;
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
	public DismissType createDismissType() {
		DismissTypeImpl dismissType = new DismissTypeImpl();
		return dismissType;
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
	public ExecuteRequestType createExecuteRequestType() {
		ExecuteRequestTypeImpl executeRequestType = new ExecuteRequestTypeImpl();
		return executeRequestType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtensionType createExtensionType() {
		ExtensionTypeImpl extensionType = new ExtensionTypeImpl();
		return extensionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FormatType createFormatType() {
		FormatTypeImpl formatType = new FormatTypeImpl();
		return formatType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenericInputType createGenericInputType() {
		GenericInputTypeImpl genericInputType = new GenericInputTypeImpl();
		return genericInputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenericOutputType createGenericOutputType() {
		GenericOutputTypeImpl genericOutputType = new GenericOutputTypeImpl();
		return genericOutputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenericProcessType createGenericProcessType() {
		GenericProcessTypeImpl genericProcessType = new GenericProcessTypeImpl();
		return genericProcessType;
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
	public GetResultType createGetResultType() {
		GetResultTypeImpl getResultType = new GetResultTypeImpl();
		return getResultType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetStatusType createGetStatusType() {
		GetStatusTypeImpl getStatusType = new GetStatusTypeImpl();
		return getStatusType;
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
	public LiteralDataDomainType createLiteralDataDomainType() {
		LiteralDataDomainTypeImpl literalDataDomainType = new LiteralDataDomainTypeImpl();
		return literalDataDomainType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LiteralDataDomainType1 createLiteralDataDomainType1() {
		LiteralDataDomainType1Impl literalDataDomainType1 = new LiteralDataDomainType1Impl();
		return literalDataDomainType1;
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
	public LiteralValueType createLiteralValueType() {
		LiteralValueTypeImpl literalValueType = new LiteralValueTypeImpl();
		return literalValueType;
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
	public ProcessDescriptionType createProcessDescriptionType() {
		ProcessDescriptionTypeImpl processDescriptionType = new ProcessDescriptionTypeImpl();
		return processDescriptionType;
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
	public ProcessOfferingType createProcessOfferingType() {
		ProcessOfferingTypeImpl processOfferingType = new ProcessOfferingTypeImpl();
		return processOfferingType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessSummaryType createProcessSummaryType() {
		ProcessSummaryTypeImpl processSummaryType = new ProcessSummaryTypeImpl();
		return processSummaryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceType createReferenceType() {
		ReferenceTypeImpl referenceType = new ReferenceTypeImpl();
		return referenceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultType createResultType() {
		ResultTypeImpl resultType = new ResultTypeImpl();
		return resultType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusInfoType createStatusInfoType() {
		StatusInfoTypeImpl statusInfoType = new StatusInfoTypeImpl();
		return statusInfoType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SupportedCRSType createSupportedCRSType() {
		SupportedCRSTypeImpl supportedCRSType = new SupportedCRSTypeImpl();
		return supportedCRSType;
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
	public DataTransmissionModeType createDataTransmissionModeTypeFromString(EDataType eDataType, String initialValue) {
		DataTransmissionModeType result = DataTransmissionModeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDataTransmissionModeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JobControlOptionsTypeMember0 createJobControlOptionsTypeMember0FromString(EDataType eDataType, String initialValue) {
		JobControlOptionsTypeMember0 result = JobControlOptionsTypeMember0.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertJobControlOptionsTypeMember0ToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModeType createModeTypeFromString(EDataType eDataType, String initialValue) {
		ModeType result = ModeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertModeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResponseType createResponseTypeFromString(EDataType eDataType, String initialValue) {
		ResponseType result = ResponseType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertResponseTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusTypeMember0 createStatusTypeMember0FromString(EDataType eDataType, String initialValue) {
		StatusTypeMember0 result = StatusTypeMember0.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertStatusTypeMember0ToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataTransmissionModeType createDataTransmissionModeTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createDataTransmissionModeTypeFromString(Wps20Package.Literals.DATA_TRANSMISSION_MODE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDataTransmissionModeTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDataTransmissionModeTypeToString(Wps20Package.Literals.DATA_TRANSMISSION_MODE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createJobControlOptionsTypeFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		Object result = null;
		RuntimeException exception = null;
		try {
			result = createJobControlOptionsTypeMember0FromString(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER0, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = createJobControlOptionsTypeMember1FromString(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER1, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		if (result != null || exception == null) return result;
    
		throw exception;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertJobControlOptionsTypeToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		if (Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER0.isInstance(instanceValue)) {
			try {
				String value = convertJobControlOptionsTypeMember0ToString(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER0, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER1.isInstance(instanceValue)) {
			try {
				String value = convertJobControlOptionsTypeMember1ToString(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER1, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<Object> createJobControlOptionsType1FromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		List<Object> result = new ArrayList<Object>();
		for (String item : split(initialValue)) {
			result.add(createJobControlOptionsTypeFromString(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE, item));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertJobControlOptionsType1ToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		List<?> list = (List<?>)instanceValue;
		if (list.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Object item : list) {
			result.append(convertJobControlOptionsTypeToString(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE, item));
			result.append(' ');
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JobControlOptionsTypeMember0 createJobControlOptionsTypeMember0ObjectFromString(EDataType eDataType, String initialValue) {
		return createJobControlOptionsTypeMember0FromString(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER0, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertJobControlOptionsTypeMember0ObjectToString(EDataType eDataType, Object instanceValue) {
		return convertJobControlOptionsTypeMember0ToString(Wps20Package.Literals.JOB_CONTROL_OPTIONS_TYPE_MEMBER0, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createJobControlOptionsTypeMember1FromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertJobControlOptionsTypeMember1ToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModeType createModeTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createModeTypeFromString(Wps20Package.Literals.MODE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertModeTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertModeTypeToString(Wps20Package.Literals.MODE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<DataTransmissionModeType> createOutputTransmissionTypeFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		List<DataTransmissionModeType> result = new ArrayList<DataTransmissionModeType>();
		for (String item : split(initialValue)) {
			result.add(createDataTransmissionModeTypeFromString(Wps20Package.Literals.DATA_TRANSMISSION_MODE_TYPE, item));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOutputTransmissionTypeToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		List<?> list = (List<?>)instanceValue;
		if (list.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Object item : list) {
			result.append(convertDataTransmissionModeTypeToString(Wps20Package.Literals.DATA_TRANSMISSION_MODE_TYPE, item));
			result.append(' ');
		}
		return result.substring(0, result.length() - 1);
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
	public ResponseType createResponseTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createResponseTypeFromString(Wps20Package.Literals.RESPONSE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertResponseTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertResponseTypeToString(Wps20Package.Literals.RESPONSE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createStatusTypeFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		Object result = null;
		RuntimeException exception = null;
		try {
			result = createStatusTypeMember0FromString(Wps20Package.Literals.STATUS_TYPE_MEMBER0, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = createStatusTypeMember1FromString(Wps20Package.Literals.STATUS_TYPE_MEMBER1, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		if (result != null || exception == null) return result;
    
		throw exception;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertStatusTypeToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		if (Wps20Package.Literals.STATUS_TYPE_MEMBER0.isInstance(instanceValue)) {
			try {
				String value = convertStatusTypeMember0ToString(Wps20Package.Literals.STATUS_TYPE_MEMBER0, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (Wps20Package.Literals.STATUS_TYPE_MEMBER1.isInstance(instanceValue)) {
			try {
				String value = convertStatusTypeMember1ToString(Wps20Package.Literals.STATUS_TYPE_MEMBER1, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusTypeMember0 createStatusTypeMember0ObjectFromString(EDataType eDataType, String initialValue) {
		return createStatusTypeMember0FromString(Wps20Package.Literals.STATUS_TYPE_MEMBER0, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertStatusTypeMember0ObjectToString(EDataType eDataType, Object instanceValue) {
		return convertStatusTypeMember0ToString(Wps20Package.Literals.STATUS_TYPE_MEMBER0, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createStatusTypeMember1FromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertStatusTypeMember1ToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wps20Package getWps20Package() {
		return (Wps20Package)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static Wps20Package getPackage() {
		return Wps20Package.eINSTANCE;
	}

} //Wps20FactoryImpl
