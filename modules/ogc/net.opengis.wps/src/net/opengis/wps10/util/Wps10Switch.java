/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.util;

import java.util.List;

import javax.measure.unit.Unit;
import net.opengis.ows11.CapabilitiesBaseType;

import net.opengis.wps10.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

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
 * @see net.opengis.wps10.Wps10Package
 * @generated
 */
public class Wps10Switch {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Wps10Package modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wps10Switch() {
        if (modelPackage == null) {
            modelPackage = Wps10Package.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public Object doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else {
            List eSuperTypes = theEClass.getESuperTypes();
            return
                eSuperTypes.isEmpty() ?
                    defaultCase(theEObject) :
                    doSwitch((EClass)eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case Wps10Package.BODY_REFERENCE_TYPE: {
                BodyReferenceType bodyReferenceType = (BodyReferenceType)theEObject;
                Object result = caseBodyReferenceType(bodyReferenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.COMPLEX_DATA_COMBINATIONS_TYPE: {
                ComplexDataCombinationsType complexDataCombinationsType = (ComplexDataCombinationsType)theEObject;
                Object result = caseComplexDataCombinationsType(complexDataCombinationsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.COMPLEX_DATA_COMBINATION_TYPE: {
                ComplexDataCombinationType complexDataCombinationType = (ComplexDataCombinationType)theEObject;
                Object result = caseComplexDataCombinationType(complexDataCombinationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.COMPLEX_DATA_DESCRIPTION_TYPE: {
                ComplexDataDescriptionType complexDataDescriptionType = (ComplexDataDescriptionType)theEObject;
                Object result = caseComplexDataDescriptionType(complexDataDescriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.COMPLEX_DATA_TYPE: {
                ComplexDataType complexDataType = (ComplexDataType)theEObject;
                Object result = caseComplexDataType(complexDataType);
                if (result == null) result = caseAnyType(complexDataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.CR_SS_TYPE: {
                CRSsType crSsType = (CRSsType)theEObject;
                Object result = caseCRSsType(crSsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DATA_INPUTS_TYPE: {
                DataInputsType dataInputsType = (DataInputsType)theEObject;
                Object result = caseDataInputsType(dataInputsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DATA_INPUTS_TYPE1: {
                DataInputsType1 dataInputsType1 = (DataInputsType1)theEObject;
                Object result = caseDataInputsType1(dataInputsType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DATA_TYPE: {
                DataType dataType = (DataType)theEObject;
                Object result = caseDataType(dataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DEFAULT_TYPE: {
                DefaultType defaultType = (DefaultType)theEObject;
                Object result = caseDefaultType(defaultType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DEFAULT_TYPE1: {
                DefaultType1 defaultType1 = (DefaultType1)theEObject;
                Object result = caseDefaultType1(defaultType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DEFAULT_TYPE2: {
                DefaultType2 defaultType2 = (DefaultType2)theEObject;
                Object result = caseDefaultType2(defaultType2);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DESCRIBE_PROCESS_TYPE: {
                DescribeProcessType describeProcessType = (DescribeProcessType)theEObject;
                Object result = caseDescribeProcessType(describeProcessType);
                if (result == null) result = caseRequestBaseType(describeProcessType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DESCRIPTION_TYPE: {
                DescriptionType descriptionType = (DescriptionType)theEObject;
                Object result = caseDescriptionType(descriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DOCUMENT_OUTPUT_DEFINITION_TYPE: {
                DocumentOutputDefinitionType documentOutputDefinitionType = (DocumentOutputDefinitionType)theEObject;
                Object result = caseDocumentOutputDefinitionType(documentOutputDefinitionType);
                if (result == null) result = caseOutputDefinitionType(documentOutputDefinitionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                Object result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.EXECUTE_RESPONSE_TYPE: {
                ExecuteResponseType executeResponseType = (ExecuteResponseType)theEObject;
                Object result = caseExecuteResponseType(executeResponseType);
                if (result == null) result = caseResponseBaseType(executeResponseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.EXECUTE_TYPE: {
                ExecuteType executeType = (ExecuteType)theEObject;
                Object result = caseExecuteType(executeType);
                if (result == null) result = caseRequestBaseType(executeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.GET_CAPABILITIES_TYPE: {
                GetCapabilitiesType getCapabilitiesType = (GetCapabilitiesType)theEObject;
                Object result = caseGetCapabilitiesType(getCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.HEADER_TYPE: {
                HeaderType headerType = (HeaderType)theEObject;
                Object result = caseHeaderType(headerType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.INPUT_DESCRIPTION_TYPE: {
                InputDescriptionType inputDescriptionType = (InputDescriptionType)theEObject;
                Object result = caseInputDescriptionType(inputDescriptionType);
                if (result == null) result = caseDescriptionType(inputDescriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.INPUT_REFERENCE_TYPE: {
                InputReferenceType inputReferenceType = (InputReferenceType)theEObject;
                Object result = caseInputReferenceType(inputReferenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.INPUT_TYPE: {
                InputType inputType = (InputType)theEObject;
                Object result = caseInputType(inputType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.LANGUAGES_TYPE: {
                LanguagesType languagesType = (LanguagesType)theEObject;
                Object result = caseLanguagesType(languagesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.LANGUAGES_TYPE1: {
                LanguagesType1 languagesType1 = (LanguagesType1)theEObject;
                Object result = caseLanguagesType1(languagesType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.LITERAL_DATA_TYPE: {
                LiteralDataType literalDataType = (LiteralDataType)theEObject;
                Object result = caseLiteralDataType(literalDataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.LITERAL_INPUT_TYPE: {
                LiteralInputType literalInputType = (LiteralInputType)theEObject;
                Object result = caseLiteralInputType(literalInputType);
                if (result == null) result = caseLiteralOutputType(literalInputType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.LITERAL_OUTPUT_TYPE: {
                LiteralOutputType literalOutputType = (LiteralOutputType)theEObject;
                Object result = caseLiteralOutputType(literalOutputType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.OUTPUT_DATA_TYPE: {
                OutputDataType outputDataType = (OutputDataType)theEObject;
                Object result = caseOutputDataType(outputDataType);
                if (result == null) result = caseDescriptionType(outputDataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.OUTPUT_DEFINITIONS_TYPE: {
                OutputDefinitionsType outputDefinitionsType = (OutputDefinitionsType)theEObject;
                Object result = caseOutputDefinitionsType(outputDefinitionsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.OUTPUT_DEFINITION_TYPE: {
                OutputDefinitionType outputDefinitionType = (OutputDefinitionType)theEObject;
                Object result = caseOutputDefinitionType(outputDefinitionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.OUTPUT_DESCRIPTION_TYPE: {
                OutputDescriptionType outputDescriptionType = (OutputDescriptionType)theEObject;
                Object result = caseOutputDescriptionType(outputDescriptionType);
                if (result == null) result = caseDescriptionType(outputDescriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.OUTPUT_REFERENCE_TYPE: {
                OutputReferenceType outputReferenceType = (OutputReferenceType)theEObject;
                Object result = caseOutputReferenceType(outputReferenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.PROCESS_BRIEF_TYPE: {
                ProcessBriefType processBriefType = (ProcessBriefType)theEObject;
                Object result = caseProcessBriefType(processBriefType);
                if (result == null) result = caseDescriptionType(processBriefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.PROCESS_DESCRIPTIONS_TYPE: {
                ProcessDescriptionsType processDescriptionsType = (ProcessDescriptionsType)theEObject;
                Object result = caseProcessDescriptionsType(processDescriptionsType);
                if (result == null) result = caseResponseBaseType(processDescriptionsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.PROCESS_DESCRIPTION_TYPE: {
                ProcessDescriptionType processDescriptionType = (ProcessDescriptionType)theEObject;
                Object result = caseProcessDescriptionType(processDescriptionType);
                if (result == null) result = caseProcessBriefType(processDescriptionType);
                if (result == null) result = caseDescriptionType(processDescriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.PROCESS_FAILED_TYPE: {
                ProcessFailedType processFailedType = (ProcessFailedType)theEObject;
                Object result = caseProcessFailedType(processFailedType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.PROCESS_OFFERINGS_TYPE: {
                ProcessOfferingsType processOfferingsType = (ProcessOfferingsType)theEObject;
                Object result = caseProcessOfferingsType(processOfferingsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.PROCESS_OUTPUTS_TYPE: {
                ProcessOutputsType processOutputsType = (ProcessOutputsType)theEObject;
                Object result = caseProcessOutputsType(processOutputsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.PROCESS_OUTPUTS_TYPE1: {
                ProcessOutputsType1 processOutputsType1 = (ProcessOutputsType1)theEObject;
                Object result = caseProcessOutputsType1(processOutputsType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.PROCESS_STARTED_TYPE: {
                ProcessStartedType processStartedType = (ProcessStartedType)theEObject;
                Object result = caseProcessStartedType(processStartedType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.REQUEST_BASE_TYPE: {
                RequestBaseType requestBaseType = (RequestBaseType)theEObject;
                Object result = caseRequestBaseType(requestBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.RESPONSE_BASE_TYPE: {
                ResponseBaseType responseBaseType = (ResponseBaseType)theEObject;
                Object result = caseResponseBaseType(responseBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.RESPONSE_DOCUMENT_TYPE: {
                ResponseDocumentType responseDocumentType = (ResponseDocumentType)theEObject;
                Object result = caseResponseDocumentType(responseDocumentType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.RESPONSE_FORM_TYPE: {
                ResponseFormType responseFormType = (ResponseFormType)theEObject;
                Object result = caseResponseFormType(responseFormType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.STATUS_TYPE: {
                StatusType statusType = (StatusType)theEObject;
                Object result = caseStatusType(statusType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.SUPPORTED_COMPLEX_DATA_INPUT_TYPE: {
                SupportedComplexDataInputType supportedComplexDataInputType = (SupportedComplexDataInputType)theEObject;
                Object result = caseSupportedComplexDataInputType(supportedComplexDataInputType);
                if (result == null) result = caseSupportedComplexDataType(supportedComplexDataInputType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE: {
                SupportedComplexDataType supportedComplexDataType = (SupportedComplexDataType)theEObject;
                Object result = caseSupportedComplexDataType(supportedComplexDataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.SUPPORTED_CR_SS_TYPE: {
                SupportedCRSsType supportedCRSsType = (SupportedCRSsType)theEObject;
                Object result = caseSupportedCRSsType(supportedCRSsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.SUPPORTED_UO_MS_TYPE: {
                SupportedUOMsType supportedUOMsType = (SupportedUOMsType)theEObject;
                Object result = caseSupportedUOMsType(supportedUOMsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.UO_MS_TYPE: {
                UOMsType uoMsType = (UOMsType)theEObject;
                Object result = caseUOMsType(uoMsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.VALUES_REFERENCE_TYPE: {
                ValuesReferenceType valuesReferenceType = (ValuesReferenceType)theEObject;
                Object result = caseValuesReferenceType(valuesReferenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.WPS_CAPABILITIES_TYPE: {
                WPSCapabilitiesType wpsCapabilitiesType = (WPSCapabilitiesType)theEObject;
                Object result = caseWPSCapabilitiesType(wpsCapabilitiesType);
                if (result == null) result = caseCapabilitiesBaseType(wpsCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wps10Package.WSDL_TYPE: {
                WSDLType wsdlType = (WSDLType)theEObject;
                Object result = caseWSDLType(wsdlType);
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
    public Object caseBodyReferenceType(BodyReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Complex Data Combinations Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Complex Data Combinations Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseComplexDataCombinationsType(ComplexDataCombinationsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Complex Data Combination Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Complex Data Combination Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseComplexDataCombinationType(ComplexDataCombinationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Complex Data Description Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Complex Data Description Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseComplexDataDescriptionType(ComplexDataDescriptionType object) {
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
    public Object caseComplexDataType(ComplexDataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CR Ss Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CR Ss Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCRSsType(CRSsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Inputs Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Inputs Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDataInputsType(DataInputsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Inputs Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Inputs Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDataInputsType1(DataInputsType1 object) {
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
    public Object caseDataType(DataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Default Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Default Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDefaultType(DefaultType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Default Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Default Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDefaultType1(DefaultType1 object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Default Type2</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Default Type2</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDefaultType2(DefaultType2 object) {
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
    public Object caseDescribeProcessType(DescribeProcessType object) {
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
    public Object caseDescriptionType(DescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Output Definition Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Output Definition Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDocumentOutputDefinitionType(DocumentOutputDefinitionType object) {
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
    public Object caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execute Response Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execute Response Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecuteResponseType(ExecuteResponseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execute Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execute Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExecuteType(ExecuteType object) {
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
    public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Header Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Header Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseHeaderType(HeaderType object) {
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
    public Object caseInputDescriptionType(InputDescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Input Reference Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Input Reference Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseInputReferenceType(InputReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Input Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Input Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseInputType(InputType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Languages Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Languages Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLanguagesType(LanguagesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Languages Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Languages Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLanguagesType1(LanguagesType1 object) {
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
    public Object caseLiteralDataType(LiteralDataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Input Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Input Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLiteralInputType(LiteralInputType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Output Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Output Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLiteralOutputType(LiteralOutputType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Output Data Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Output Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOutputDataType(OutputDataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Output Definitions Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Output Definitions Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOutputDefinitionsType(OutputDefinitionsType object) {
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
    public Object caseOutputDefinitionType(OutputDefinitionType object) {
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
    public Object caseOutputDescriptionType(OutputDescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Output Reference Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Output Reference Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOutputReferenceType(OutputReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Brief Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Brief Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseProcessBriefType(ProcessBriefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Descriptions Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Descriptions Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseProcessDescriptionsType(ProcessDescriptionsType object) {
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
    public Object caseProcessDescriptionType(ProcessDescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Failed Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Failed Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseProcessFailedType(ProcessFailedType object) {
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
    public Object caseProcessOfferingsType(ProcessOfferingsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Outputs Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Outputs Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseProcessOutputsType(ProcessOutputsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Outputs Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Outputs Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseProcessOutputsType1(ProcessOutputsType1 object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Process Started Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Process Started Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseProcessStartedType(ProcessStartedType object) {
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
    public Object caseRequestBaseType(RequestBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Response Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Response Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseResponseBaseType(ResponseBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Response Document Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Response Document Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseResponseDocumentType(ResponseDocumentType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Response Form Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Response Form Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseResponseFormType(ResponseFormType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Status Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Status Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseStatusType(StatusType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Supported Complex Data Input Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Supported Complex Data Input Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSupportedComplexDataInputType(SupportedComplexDataInputType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Supported Complex Data Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Supported Complex Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSupportedComplexDataType(SupportedComplexDataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Supported CR Ss Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Supported CR Ss Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSupportedCRSsType(SupportedCRSsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Supported UO Ms Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Supported UO Ms Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSupportedUOMsType(SupportedUOMsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>UO Ms Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>UO Ms Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseUOMsType(UOMsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Values Reference Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Values Reference Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseValuesReferenceType(ValuesReferenceType object) {
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
    public Object caseWPSCapabilitiesType(WPSCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>WSDL Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WSDL Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseWSDLType(WSDLType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unit</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unit</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseUnit(Unit object) {
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
    public Object caseAnyType(AnyType object) {
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
    public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
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
    public Object defaultCase(EObject object) {
        return null;
    }

} //Wps10Switch
