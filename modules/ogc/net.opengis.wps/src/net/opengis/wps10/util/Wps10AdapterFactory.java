/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.util;

import javax.measure.unit.Unit;
import net.opengis.ows11.CapabilitiesBaseType;

import net.opengis.wps10.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.xml.type.AnyType;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wps10.Wps10Package
 * @generated
 */
public class Wps10AdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Wps10Package modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wps10AdapterFactory() {
        if (modelPackage == null) {
            modelPackage = Wps10Package.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Wps10Switch modelSwitch =
        new Wps10Switch() {
            public Object caseBodyReferenceType(BodyReferenceType object) {
                return createBodyReferenceTypeAdapter();
            }
            public Object caseComplexDataCombinationsType(ComplexDataCombinationsType object) {
                return createComplexDataCombinationsTypeAdapter();
            }
            public Object caseComplexDataCombinationType(ComplexDataCombinationType object) {
                return createComplexDataCombinationTypeAdapter();
            }
            public Object caseComplexDataDescriptionType(ComplexDataDescriptionType object) {
                return createComplexDataDescriptionTypeAdapter();
            }
            public Object caseComplexDataType(ComplexDataType object) {
                return createComplexDataTypeAdapter();
            }
            public Object caseCRSsType(CRSsType object) {
                return createCRSsTypeAdapter();
            }
            public Object caseDataInputsType(DataInputsType object) {
                return createDataInputsTypeAdapter();
            }
            public Object caseDataInputsType1(DataInputsType1 object) {
                return createDataInputsType1Adapter();
            }
            public Object caseDataType(DataType object) {
                return createDataTypeAdapter();
            }
            public Object caseDefaultType(DefaultType object) {
                return createDefaultTypeAdapter();
            }
            public Object caseDefaultType1(DefaultType1 object) {
                return createDefaultType1Adapter();
            }
            public Object caseDefaultType2(DefaultType2 object) {
                return createDefaultType2Adapter();
            }
            public Object caseDescribeProcessType(DescribeProcessType object) {
                return createDescribeProcessTypeAdapter();
            }
            public Object caseDescriptionType(DescriptionType object) {
                return createDescriptionTypeAdapter();
            }
            public Object caseDocumentOutputDefinitionType(DocumentOutputDefinitionType object) {
                return createDocumentOutputDefinitionTypeAdapter();
            }
            public Object caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            public Object caseExecuteResponseType(ExecuteResponseType object) {
                return createExecuteResponseTypeAdapter();
            }
            public Object caseExecuteType(ExecuteType object) {
                return createExecuteTypeAdapter();
            }
            public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
                return createGetCapabilitiesTypeAdapter();
            }
            public Object caseHeaderType(HeaderType object) {
                return createHeaderTypeAdapter();
            }
            public Object caseInputDescriptionType(InputDescriptionType object) {
                return createInputDescriptionTypeAdapter();
            }
            public Object caseInputReferenceType(InputReferenceType object) {
                return createInputReferenceTypeAdapter();
            }
            public Object caseInputType(InputType object) {
                return createInputTypeAdapter();
            }
            public Object caseLanguagesType(LanguagesType object) {
                return createLanguagesTypeAdapter();
            }
            public Object caseLanguagesType1(LanguagesType1 object) {
                return createLanguagesType1Adapter();
            }
            public Object caseLiteralDataType(LiteralDataType object) {
                return createLiteralDataTypeAdapter();
            }
            public Object caseLiteralInputType(LiteralInputType object) {
                return createLiteralInputTypeAdapter();
            }
            public Object caseLiteralOutputType(LiteralOutputType object) {
                return createLiteralOutputTypeAdapter();
            }
            public Object caseOutputDataType(OutputDataType object) {
                return createOutputDataTypeAdapter();
            }
            public Object caseOutputDefinitionsType(OutputDefinitionsType object) {
                return createOutputDefinitionsTypeAdapter();
            }
            public Object caseOutputDefinitionType(OutputDefinitionType object) {
                return createOutputDefinitionTypeAdapter();
            }
            public Object caseOutputDescriptionType(OutputDescriptionType object) {
                return createOutputDescriptionTypeAdapter();
            }
            public Object caseOutputReferenceType(OutputReferenceType object) {
                return createOutputReferenceTypeAdapter();
            }
            public Object caseProcessBriefType(ProcessBriefType object) {
                return createProcessBriefTypeAdapter();
            }
            public Object caseProcessDescriptionsType(ProcessDescriptionsType object) {
                return createProcessDescriptionsTypeAdapter();
            }
            public Object caseProcessDescriptionType(ProcessDescriptionType object) {
                return createProcessDescriptionTypeAdapter();
            }
            public Object caseProcessFailedType(ProcessFailedType object) {
                return createProcessFailedTypeAdapter();
            }
            public Object caseProcessOfferingsType(ProcessOfferingsType object) {
                return createProcessOfferingsTypeAdapter();
            }
            public Object caseProcessOutputsType(ProcessOutputsType object) {
                return createProcessOutputsTypeAdapter();
            }
            public Object caseProcessOutputsType1(ProcessOutputsType1 object) {
                return createProcessOutputsType1Adapter();
            }
            public Object caseProcessStartedType(ProcessStartedType object) {
                return createProcessStartedTypeAdapter();
            }
            public Object caseRequestBaseType(RequestBaseType object) {
                return createRequestBaseTypeAdapter();
            }
            public Object caseResponseBaseType(ResponseBaseType object) {
                return createResponseBaseTypeAdapter();
            }
            public Object caseResponseDocumentType(ResponseDocumentType object) {
                return createResponseDocumentTypeAdapter();
            }
            public Object caseResponseFormType(ResponseFormType object) {
                return createResponseFormTypeAdapter();
            }
            public Object caseStatusType(StatusType object) {
                return createStatusTypeAdapter();
            }
            public Object caseSupportedComplexDataInputType(SupportedComplexDataInputType object) {
                return createSupportedComplexDataInputTypeAdapter();
            }
            public Object caseSupportedComplexDataType(SupportedComplexDataType object) {
                return createSupportedComplexDataTypeAdapter();
            }
            public Object caseSupportedCRSsType(SupportedCRSsType object) {
                return createSupportedCRSsTypeAdapter();
            }
            public Object caseSupportedUOMsType(SupportedUOMsType object) {
                return createSupportedUOMsTypeAdapter();
            }
            public Object caseUOMsType(UOMsType object) {
                return createUOMsTypeAdapter();
            }
            public Object caseValuesReferenceType(ValuesReferenceType object) {
                return createValuesReferenceTypeAdapter();
            }
            public Object caseWPSCapabilitiesType(WPSCapabilitiesType object) {
                return createWPSCapabilitiesTypeAdapter();
            }
            public Object caseWSDLType(WSDLType object) {
                return createWSDLTypeAdapter();
            }
            public Object caseUnit(Unit object) {
                return createUnitAdapter();
            }
            public Object caseAnyType(AnyType object) {
                return createAnyTypeAdapter();
            }
            public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
                return createCapabilitiesBaseTypeAdapter();
            }
            public Object defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    public Adapter createAdapter(Notifier target) {
        return (Adapter)modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.BodyReferenceType <em>Body Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.BodyReferenceType
     * @generated
     */
    public Adapter createBodyReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ComplexDataCombinationsType <em>Complex Data Combinations Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ComplexDataCombinationsType
     * @generated
     */
    public Adapter createComplexDataCombinationsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ComplexDataCombinationType <em>Complex Data Combination Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ComplexDataCombinationType
     * @generated
     */
    public Adapter createComplexDataCombinationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ComplexDataDescriptionType <em>Complex Data Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ComplexDataDescriptionType
     * @generated
     */
    public Adapter createComplexDataDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ComplexDataType <em>Complex Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ComplexDataType
     * @generated
     */
    public Adapter createComplexDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.CRSsType <em>CR Ss Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.CRSsType
     * @generated
     */
    public Adapter createCRSsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DataInputsType <em>Data Inputs Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DataInputsType
     * @generated
     */
    public Adapter createDataInputsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DataInputsType1 <em>Data Inputs Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DataInputsType1
     * @generated
     */
    public Adapter createDataInputsType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DataType
     * @generated
     */
    public Adapter createDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DefaultType <em>Default Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DefaultType
     * @generated
     */
    public Adapter createDefaultTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DefaultType1 <em>Default Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DefaultType1
     * @generated
     */
    public Adapter createDefaultType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DefaultType2 <em>Default Type2</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DefaultType2
     * @generated
     */
    public Adapter createDefaultType2Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DescribeProcessType <em>Describe Process Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DescribeProcessType
     * @generated
     */
    public Adapter createDescribeProcessTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DescriptionType
     * @generated
     */
    public Adapter createDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DocumentOutputDefinitionType <em>Document Output Definition Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DocumentOutputDefinitionType
     * @generated
     */
    public Adapter createDocumentOutputDefinitionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ExecuteResponseType <em>Execute Response Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ExecuteResponseType
     * @generated
     */
    public Adapter createExecuteResponseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ExecuteType <em>Execute Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ExecuteType
     * @generated
     */
    public Adapter createExecuteTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.GetCapabilitiesType
     * @generated
     */
    public Adapter createGetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.HeaderType <em>Header Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.HeaderType
     * @generated
     */
    public Adapter createHeaderTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.InputDescriptionType <em>Input Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.InputDescriptionType
     * @generated
     */
    public Adapter createInputDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.InputReferenceType <em>Input Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.InputReferenceType
     * @generated
     */
    public Adapter createInputReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.InputType <em>Input Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.InputType
     * @generated
     */
    public Adapter createInputTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.LanguagesType <em>Languages Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.LanguagesType
     * @generated
     */
    public Adapter createLanguagesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.LanguagesType1 <em>Languages Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.LanguagesType1
     * @generated
     */
    public Adapter createLanguagesType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.LiteralDataType <em>Literal Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.LiteralDataType
     * @generated
     */
    public Adapter createLiteralDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.LiteralInputType <em>Literal Input Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.LiteralInputType
     * @generated
     */
    public Adapter createLiteralInputTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.LiteralOutputType <em>Literal Output Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.LiteralOutputType
     * @generated
     */
    public Adapter createLiteralOutputTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.OutputDataType <em>Output Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.OutputDataType
     * @generated
     */
    public Adapter createOutputDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.OutputDefinitionsType <em>Output Definitions Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.OutputDefinitionsType
     * @generated
     */
    public Adapter createOutputDefinitionsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.OutputDefinitionType <em>Output Definition Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.OutputDefinitionType
     * @generated
     */
    public Adapter createOutputDefinitionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.OutputDescriptionType <em>Output Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.OutputDescriptionType
     * @generated
     */
    public Adapter createOutputDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.OutputReferenceType <em>Output Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.OutputReferenceType
     * @generated
     */
    public Adapter createOutputReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ProcessBriefType <em>Process Brief Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ProcessBriefType
     * @generated
     */
    public Adapter createProcessBriefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ProcessDescriptionsType <em>Process Descriptions Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ProcessDescriptionsType
     * @generated
     */
    public Adapter createProcessDescriptionsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ProcessDescriptionType <em>Process Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ProcessDescriptionType
     * @generated
     */
    public Adapter createProcessDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ProcessFailedType <em>Process Failed Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ProcessFailedType
     * @generated
     */
    public Adapter createProcessFailedTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ProcessOfferingsType <em>Process Offerings Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ProcessOfferingsType
     * @generated
     */
    public Adapter createProcessOfferingsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ProcessOutputsType <em>Process Outputs Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ProcessOutputsType
     * @generated
     */
    public Adapter createProcessOutputsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ProcessOutputsType1 <em>Process Outputs Type1</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ProcessOutputsType1
     * @generated
     */
    public Adapter createProcessOutputsType1Adapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ProcessStartedType <em>Process Started Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ProcessStartedType
     * @generated
     */
    public Adapter createProcessStartedTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.RequestBaseType <em>Request Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.RequestBaseType
     * @generated
     */
    public Adapter createRequestBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ResponseBaseType <em>Response Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ResponseBaseType
     * @generated
     */
    public Adapter createResponseBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ResponseDocumentType <em>Response Document Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ResponseDocumentType
     * @generated
     */
    public Adapter createResponseDocumentTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ResponseFormType <em>Response Form Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ResponseFormType
     * @generated
     */
    public Adapter createResponseFormTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.StatusType <em>Status Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.StatusType
     * @generated
     */
    public Adapter createStatusTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.SupportedComplexDataInputType <em>Supported Complex Data Input Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.SupportedComplexDataInputType
     * @generated
     */
    public Adapter createSupportedComplexDataInputTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.SupportedComplexDataType <em>Supported Complex Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.SupportedComplexDataType
     * @generated
     */
    public Adapter createSupportedComplexDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.SupportedCRSsType <em>Supported CR Ss Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.SupportedCRSsType
     * @generated
     */
    public Adapter createSupportedCRSsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.SupportedUOMsType <em>Supported UO Ms Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.SupportedUOMsType
     * @generated
     */
    public Adapter createSupportedUOMsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.UOMsType <em>UO Ms Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.UOMsType
     * @generated
     */
    public Adapter createUOMsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.ValuesReferenceType <em>Values Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.ValuesReferenceType
     * @generated
     */
    public Adapter createValuesReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.WPSCapabilitiesType <em>WPS Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.WPSCapabilitiesType
     * @generated
     */
    public Adapter createWPSCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wps10.WSDLType <em>WSDL Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wps10.WSDLType
     * @generated
     */
    public Adapter createWSDLTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link javax.measure.unit.Unit <em>Unit</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see javax.measure.unit.Unit
     * @generated
     */
    public Adapter createUnitAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.xml.type.AnyType <em>Any Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.emf.ecore.xml.type.AnyType
     * @generated
     */
    public Adapter createAnyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.CapabilitiesBaseType
     * @generated
     */
    public Adapter createCapabilitiesBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //Wps10AdapterFactory
