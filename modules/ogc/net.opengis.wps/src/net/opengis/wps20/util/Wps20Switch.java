/**
 */
package net.opengis.wps20.util;

import net.opengis.ows20.BasicIdentificationType;
import net.opengis.ows20.CapabilitiesBaseType;
import net.opengis.ows20.ValueType;

import net.opengis.wps20.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.emf.ecore.xml.type.AnyType;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see net.opengis.wps20.Wps20Package
 * @generated
 */
public class Wps20Switch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Wps20Package modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wps20Switch() {
		if (modelPackage == null) {
			modelPackage = Wps20Package.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case Wps20Package.BODY_REFERENCE_TYPE: {
				BodyReferenceType bodyReferenceType = (BodyReferenceType)theEObject;
				T result = caseBodyReferenceType(bodyReferenceType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.BOUNDING_BOX_DATA_TYPE: {
				BoundingBoxDataType boundingBoxDataType = (BoundingBoxDataType)theEObject;
				T result = caseBoundingBoxDataType(boundingBoxDataType);
				if (result == null) result = caseDataDescriptionType(boundingBoxDataType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.COMPLEX_DATA_TYPE: {
				ComplexDataType complexDataType = (ComplexDataType)theEObject;
				T result = caseComplexDataType(complexDataType);
				if (result == null) result = caseDataDescriptionType(complexDataType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.CONTENTS_TYPE: {
				ContentsType contentsType = (ContentsType)theEObject;
				T result = caseContentsType(contentsType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.DATA_DESCRIPTION_TYPE: {
				DataDescriptionType dataDescriptionType = (DataDescriptionType)theEObject;
				T result = caseDataDescriptionType(dataDescriptionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.DATA_INPUT_TYPE: {
				DataInputType dataInputType = (DataInputType)theEObject;
				T result = caseDataInputType(dataInputType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.DATA_OUTPUT_TYPE: {
				DataOutputType dataOutputType = (DataOutputType)theEObject;
				T result = caseDataOutputType(dataOutputType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.DATA_TYPE: {
				DataType dataType = (DataType)theEObject;
				T result = caseDataType(dataType);
				if (result == null) result = caseAnyType(dataType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.DESCRIBE_PROCESS_TYPE: {
				DescribeProcessType describeProcessType = (DescribeProcessType)theEObject;
				T result = caseDescribeProcessType(describeProcessType);
				if (result == null) result = caseRequestBaseType(describeProcessType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.DESCRIPTION_TYPE: {
				DescriptionType descriptionType = (DescriptionType)theEObject;
				T result = caseDescriptionType(descriptionType);
				if (result == null) result = caseBasicIdentificationType(descriptionType);
				if (result == null) result = caseOws20_DescriptionType(descriptionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.DISMISS_TYPE: {
				DismissType dismissType = (DismissType)theEObject;
				T result = caseDismissType(dismissType);
				if (result == null) result = caseRequestBaseType(dismissType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.DOCUMENT_ROOT: {
				DocumentRoot documentRoot = (DocumentRoot)theEObject;
				T result = caseDocumentRoot(documentRoot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.EXECUTE_REQUEST_TYPE: {
				ExecuteRequestType executeRequestType = (ExecuteRequestType)theEObject;
				T result = caseExecuteRequestType(executeRequestType);
				if (result == null) result = caseRequestBaseType(executeRequestType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.EXTENSION_TYPE: {
				ExtensionType extensionType = (ExtensionType)theEObject;
				T result = caseExtensionType(extensionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.FORMAT_TYPE: {
				FormatType formatType = (FormatType)theEObject;
				T result = caseFormatType(formatType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.GENERIC_INPUT_TYPE: {
				GenericInputType genericInputType = (GenericInputType)theEObject;
				T result = caseGenericInputType(genericInputType);
				if (result == null) result = caseDescriptionType(genericInputType);
				if (result == null) result = caseBasicIdentificationType(genericInputType);
				if (result == null) result = caseOws20_DescriptionType(genericInputType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.GENERIC_OUTPUT_TYPE: {
				GenericOutputType genericOutputType = (GenericOutputType)theEObject;
				T result = caseGenericOutputType(genericOutputType);
				if (result == null) result = caseDescriptionType(genericOutputType);
				if (result == null) result = caseBasicIdentificationType(genericOutputType);
				if (result == null) result = caseOws20_DescriptionType(genericOutputType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.GENERIC_PROCESS_TYPE: {
				GenericProcessType genericProcessType = (GenericProcessType)theEObject;
				T result = caseGenericProcessType(genericProcessType);
				if (result == null) result = caseDescriptionType(genericProcessType);
				if (result == null) result = caseBasicIdentificationType(genericProcessType);
				if (result == null) result = caseOws20_DescriptionType(genericProcessType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.GET_CAPABILITIES_TYPE: {
				GetCapabilitiesType getCapabilitiesType = (GetCapabilitiesType)theEObject;
				T result = caseGetCapabilitiesType(getCapabilitiesType);
				if (result == null) result = caseOws20_GetCapabilitiesType(getCapabilitiesType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.GET_RESULT_TYPE: {
				GetResultType getResultType = (GetResultType)theEObject;
				T result = caseGetResultType(getResultType);
				if (result == null) result = caseRequestBaseType(getResultType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.GET_STATUS_TYPE: {
				GetStatusType getStatusType = (GetStatusType)theEObject;
				T result = caseGetStatusType(getStatusType);
				if (result == null) result = caseRequestBaseType(getStatusType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.INPUT_DESCRIPTION_TYPE: {
				InputDescriptionType inputDescriptionType = (InputDescriptionType)theEObject;
				T result = caseInputDescriptionType(inputDescriptionType);
				if (result == null) result = caseDescriptionType(inputDescriptionType);
				if (result == null) result = caseBasicIdentificationType(inputDescriptionType);
				if (result == null) result = caseOws20_DescriptionType(inputDescriptionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE: {
				LiteralDataDomainType literalDataDomainType = (LiteralDataDomainType)theEObject;
				T result = caseLiteralDataDomainType(literalDataDomainType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.LITERAL_DATA_DOMAIN_TYPE1: {
				LiteralDataDomainType1 literalDataDomainType1 = (LiteralDataDomainType1)theEObject;
				T result = caseLiteralDataDomainType1(literalDataDomainType1);
				if (result == null) result = caseLiteralDataDomainType(literalDataDomainType1);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.LITERAL_DATA_TYPE: {
				LiteralDataType literalDataType = (LiteralDataType)theEObject;
				T result = caseLiteralDataType(literalDataType);
				if (result == null) result = caseDataDescriptionType(literalDataType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.LITERAL_VALUE_TYPE: {
				LiteralValueType literalValueType = (LiteralValueType)theEObject;
				T result = caseLiteralValueType(literalValueType);
				if (result == null) result = caseValueType(literalValueType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.OUTPUT_DEFINITION_TYPE: {
				OutputDefinitionType outputDefinitionType = (OutputDefinitionType)theEObject;
				T result = caseOutputDefinitionType(outputDefinitionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.OUTPUT_DESCRIPTION_TYPE: {
				OutputDescriptionType outputDescriptionType = (OutputDescriptionType)theEObject;
				T result = caseOutputDescriptionType(outputDescriptionType);
				if (result == null) result = caseDescriptionType(outputDescriptionType);
				if (result == null) result = caseBasicIdentificationType(outputDescriptionType);
				if (result == null) result = caseOws20_DescriptionType(outputDescriptionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.PROCESS_DESCRIPTION_TYPE: {
				ProcessDescriptionType processDescriptionType = (ProcessDescriptionType)theEObject;
				T result = caseProcessDescriptionType(processDescriptionType);
				if (result == null) result = caseDescriptionType(processDescriptionType);
				if (result == null) result = caseBasicIdentificationType(processDescriptionType);
				if (result == null) result = caseOws20_DescriptionType(processDescriptionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.PROCESS_OFFERINGS_TYPE: {
				ProcessOfferingsType processOfferingsType = (ProcessOfferingsType)theEObject;
				T result = caseProcessOfferingsType(processOfferingsType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.PROCESS_OFFERING_TYPE: {
				ProcessOfferingType processOfferingType = (ProcessOfferingType)theEObject;
				T result = caseProcessOfferingType(processOfferingType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.PROCESS_SUMMARY_TYPE: {
				ProcessSummaryType processSummaryType = (ProcessSummaryType)theEObject;
				T result = caseProcessSummaryType(processSummaryType);
				if (result == null) result = caseDescriptionType(processSummaryType);
				if (result == null) result = caseBasicIdentificationType(processSummaryType);
				if (result == null) result = caseOws20_DescriptionType(processSummaryType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.REFERENCE_TYPE: {
				ReferenceType referenceType = (ReferenceType)theEObject;
				T result = caseReferenceType(referenceType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.REQUEST_BASE_TYPE: {
				RequestBaseType requestBaseType = (RequestBaseType)theEObject;
				T result = caseRequestBaseType(requestBaseType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.RESULT_TYPE: {
				ResultType resultType = (ResultType)theEObject;
				T result = caseResultType(resultType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.STATUS_INFO_TYPE: {
				StatusInfoType statusInfoType = (StatusInfoType)theEObject;
				T result = caseStatusInfoType(statusInfoType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.SUPPORTED_CRS_TYPE: {
				SupportedCRSType supportedCRSType = (SupportedCRSType)theEObject;
				T result = caseSupportedCRSType(supportedCRSType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Wps20Package.WPS_CAPABILITIES_TYPE: {
				WPSCapabilitiesType wpsCapabilitiesType = (WPSCapabilitiesType)theEObject;
				T result = caseWPSCapabilitiesType(wpsCapabilitiesType);
				if (result == null) result = caseCapabilitiesBaseType(wpsCapabilitiesType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Body Reference Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Body Reference Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBodyReferenceType(BodyReferenceType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Bounding Box Data Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Bounding Box Data Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBoundingBoxDataType(BoundingBoxDataType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Complex Data Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Complex Data Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComplexDataType(ComplexDataType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contents Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contents Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContentsType(ContentsType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Description Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Description Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataDescriptionType(DataDescriptionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Input Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Input Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataInputType(DataInputType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Output Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Output Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataOutputType(DataOutputType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataType(DataType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Describe Process Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Describe Process Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDescribeProcessType(DescribeProcessType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Description Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Description Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDescriptionType(DescriptionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dismiss Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dismiss Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDismissType(DismissType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDocumentRoot(DocumentRoot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Execute Request Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Execute Request Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExecuteRequestType(ExecuteRequestType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Extension Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Extension Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExtensionType(ExtensionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Format Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Format Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFormatType(FormatType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Input Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Input Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGenericInputType(GenericInputType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Output Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Output Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGenericOutputType(GenericOutputType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Process Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Process Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGenericProcessType(GenericProcessType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGetCapabilitiesType(GetCapabilitiesType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Get Result Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Get Result Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGetResultType(GetResultType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Get Status Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Get Status Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGetStatusType(GetStatusType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Input Description Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Input Description Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInputDescriptionType(InputDescriptionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Literal Data Domain Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Literal Data Domain Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLiteralDataDomainType(LiteralDataDomainType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Literal Data Domain Type1</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Literal Data Domain Type1</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLiteralDataDomainType1(LiteralDataDomainType1 object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Literal Data Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Literal Data Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLiteralDataType(LiteralDataType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Literal Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Literal Value Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLiteralValueType(LiteralValueType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Output Definition Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Output Definition Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOutputDefinitionType(OutputDefinitionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Output Description Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Output Description Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOutputDescriptionType(OutputDescriptionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Process Description Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Process Description Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProcessDescriptionType(ProcessDescriptionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Process Offerings Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Process Offerings Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProcessOfferingsType(ProcessOfferingsType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Process Offering Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Process Offering Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProcessOfferingType(ProcessOfferingType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Process Summary Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Process Summary Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProcessSummaryType(ProcessSummaryType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceType(ReferenceType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Request Base Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Request Base Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRequestBaseType(RequestBaseType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Result Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Result Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResultType(ResultType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Status Info Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Status Info Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatusInfoType(StatusInfoType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Supported CRS Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Supported CRS Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSupportedCRSType(SupportedCRSType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>WPS Capabilities Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>WPS Capabilities Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseWPSCapabilitiesType(WPSCapabilitiesType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Any Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Any Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnyType(AnyType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Description Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Description Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOws20_DescriptionType(net.opengis.ows20.DescriptionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Basic Identification Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Basic Identification Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBasicIdentificationType(BasicIdentificationType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOws20_GetCapabilitiesType(net.opengis.ows20.GetCapabilitiesType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Value Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseValueType(ValueType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Capabilities Base Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Capabilities Base Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCapabilitiesBaseType(CapabilitiesBaseType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //Wps20Switch
