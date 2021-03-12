/**
 */
package net.opengis.wps20.util;

import net.opengis.ows20.BasicIdentificationType;
import net.opengis.ows20.CapabilitiesBaseType;
import net.opengis.ows20.ValueType;

import net.opengis.wps20.*;

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
 * @see net.opengis.wps20.Wps20Package
 * @generated
 */
public class Wps20AdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Wps20Package modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wps20AdapterFactory() {
		if (modelPackage == null) {
			modelPackage = Wps20Package.eINSTANCE;
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
	@Override
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
	protected Wps20Switch<Adapter> modelSwitch =
		new Wps20Switch<Adapter>() {
			@Override
			public Adapter caseBodyReferenceType(BodyReferenceType object) {
				return createBodyReferenceTypeAdapter();
			}
			@Override
			public Adapter caseBoundingBoxDataType(BoundingBoxDataType object) {
				return createBoundingBoxDataTypeAdapter();
			}
			@Override
			public Adapter caseComplexDataType(ComplexDataType object) {
				return createComplexDataTypeAdapter();
			}
			@Override
			public Adapter caseContentsType(ContentsType object) {
				return createContentsTypeAdapter();
			}
			@Override
			public Adapter caseDataDescriptionType(DataDescriptionType object) {
				return createDataDescriptionTypeAdapter();
			}
			@Override
			public Adapter caseDataInputType(DataInputType object) {
				return createDataInputTypeAdapter();
			}
			@Override
			public Adapter caseDataOutputType(DataOutputType object) {
				return createDataOutputTypeAdapter();
			}
			@Override
			public Adapter caseDataType(DataType object) {
				return createDataTypeAdapter();
			}
			@Override
			public Adapter caseDescribeProcessType(DescribeProcessType object) {
				return createDescribeProcessTypeAdapter();
			}
			@Override
			public Adapter caseDescriptionType(DescriptionType object) {
				return createDescriptionTypeAdapter();
			}
			@Override
			public Adapter caseDismissType(DismissType object) {
				return createDismissTypeAdapter();
			}
			@Override
			public Adapter caseDocumentRoot(DocumentRoot object) {
				return createDocumentRootAdapter();
			}
			@Override
			public Adapter caseExecuteRequestType(ExecuteRequestType object) {
				return createExecuteRequestTypeAdapter();
			}
			@Override
			public Adapter caseExtensionType(ExtensionType object) {
				return createExtensionTypeAdapter();
			}
			@Override
			public Adapter caseFormatType(FormatType object) {
				return createFormatTypeAdapter();
			}
			@Override
			public Adapter caseGenericInputType(GenericInputType object) {
				return createGenericInputTypeAdapter();
			}
			@Override
			public Adapter caseGenericOutputType(GenericOutputType object) {
				return createGenericOutputTypeAdapter();
			}
			@Override
			public Adapter caseGenericProcessType(GenericProcessType object) {
				return createGenericProcessTypeAdapter();
			}
			@Override
			public Adapter caseGetCapabilitiesType(GetCapabilitiesType object) {
				return createGetCapabilitiesTypeAdapter();
			}
			@Override
			public Adapter caseGetResultType(GetResultType object) {
				return createGetResultTypeAdapter();
			}
			@Override
			public Adapter caseGetStatusType(GetStatusType object) {
				return createGetStatusTypeAdapter();
			}
			@Override
			public Adapter caseInputDescriptionType(InputDescriptionType object) {
				return createInputDescriptionTypeAdapter();
			}
			@Override
			public Adapter caseLiteralDataDomainType(LiteralDataDomainType object) {
				return createLiteralDataDomainTypeAdapter();
			}
			@Override
			public Adapter caseLiteralDataDomainType1(LiteralDataDomainType1 object) {
				return createLiteralDataDomainType1Adapter();
			}
			@Override
			public Adapter caseLiteralDataType(LiteralDataType object) {
				return createLiteralDataTypeAdapter();
			}
			@Override
			public Adapter caseLiteralValueType(LiteralValueType object) {
				return createLiteralValueTypeAdapter();
			}
			@Override
			public Adapter caseOutputDefinitionType(OutputDefinitionType object) {
				return createOutputDefinitionTypeAdapter();
			}
			@Override
			public Adapter caseOutputDescriptionType(OutputDescriptionType object) {
				return createOutputDescriptionTypeAdapter();
			}
			@Override
			public Adapter caseProcessDescriptionType(ProcessDescriptionType object) {
				return createProcessDescriptionTypeAdapter();
			}
			@Override
			public Adapter caseProcessOfferingsType(ProcessOfferingsType object) {
				return createProcessOfferingsTypeAdapter();
			}
			@Override
			public Adapter caseProcessOfferingType(ProcessOfferingType object) {
				return createProcessOfferingTypeAdapter();
			}
			@Override
			public Adapter caseProcessSummaryType(ProcessSummaryType object) {
				return createProcessSummaryTypeAdapter();
			}
			@Override
			public Adapter caseReferenceType(ReferenceType object) {
				return createReferenceTypeAdapter();
			}
			@Override
			public Adapter caseRequestBaseType(RequestBaseType object) {
				return createRequestBaseTypeAdapter();
			}
			@Override
			public Adapter caseResultType(ResultType object) {
				return createResultTypeAdapter();
			}
			@Override
			public Adapter caseStatusInfoType(StatusInfoType object) {
				return createStatusInfoTypeAdapter();
			}
			@Override
			public Adapter caseSupportedCRSType(SupportedCRSType object) {
				return createSupportedCRSTypeAdapter();
			}
			@Override
			public Adapter caseWPSCapabilitiesType(WPSCapabilitiesType object) {
				return createWPSCapabilitiesTypeAdapter();
			}
			@Override
			public Adapter caseAnyType(AnyType object) {
				return createAnyTypeAdapter();
			}
			@Override
			public Adapter caseOws20_DescriptionType(net.opengis.ows20.DescriptionType object) {
				return createOws20_DescriptionTypeAdapter();
			}
			@Override
			public Adapter caseBasicIdentificationType(BasicIdentificationType object) {
				return createBasicIdentificationTypeAdapter();
			}
			@Override
			public Adapter caseOws20_GetCapabilitiesType(net.opengis.ows20.GetCapabilitiesType object) {
				return createOws20_GetCapabilitiesTypeAdapter();
			}
			@Override
			public Adapter caseValueType(ValueType object) {
				return createValueTypeAdapter();
			}
			@Override
			public Adapter caseCapabilitiesBaseType(CapabilitiesBaseType object) {
				return createCapabilitiesBaseTypeAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
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
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.BodyReferenceType <em>Body Reference Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.BodyReferenceType
	 * @generated
	 */
	public Adapter createBodyReferenceTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.BoundingBoxDataType <em>Bounding Box Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.BoundingBoxDataType
	 * @generated
	 */
	public Adapter createBoundingBoxDataTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ComplexDataType <em>Complex Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ComplexDataType
	 * @generated
	 */
	public Adapter createComplexDataTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ContentsType <em>Contents Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ContentsType
	 * @generated
	 */
	public Adapter createContentsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.DataDescriptionType <em>Data Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.DataDescriptionType
	 * @generated
	 */
	public Adapter createDataDescriptionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.DataInputType <em>Data Input Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.DataInputType
	 * @generated
	 */
	public Adapter createDataInputTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.DataOutputType <em>Data Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.DataOutputType
	 * @generated
	 */
	public Adapter createDataOutputTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.DataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.DataType
	 * @generated
	 */
	public Adapter createDataTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.DescribeProcessType <em>Describe Process Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.DescribeProcessType
	 * @generated
	 */
	public Adapter createDescribeProcessTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.DescriptionType <em>Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.DescriptionType
	 * @generated
	 */
	public Adapter createDescriptionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.DismissType <em>Dismiss Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.DismissType
	 * @generated
	 */
	public Adapter createDismissTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.DocumentRoot
	 * @generated
	 */
	public Adapter createDocumentRootAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ExecuteRequestType <em>Execute Request Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ExecuteRequestType
	 * @generated
	 */
	public Adapter createExecuteRequestTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ExtensionType <em>Extension Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ExtensionType
	 * @generated
	 */
	public Adapter createExtensionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.FormatType <em>Format Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.FormatType
	 * @generated
	 */
	public Adapter createFormatTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.GenericInputType <em>Generic Input Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.GenericInputType
	 * @generated
	 */
	public Adapter createGenericInputTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.GenericOutputType <em>Generic Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.GenericOutputType
	 * @generated
	 */
	public Adapter createGenericOutputTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.GenericProcessType <em>Generic Process Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.GenericProcessType
	 * @generated
	 */
	public Adapter createGenericProcessTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.GetCapabilitiesType
	 * @generated
	 */
	public Adapter createGetCapabilitiesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.GetResultType <em>Get Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.GetResultType
	 * @generated
	 */
	public Adapter createGetResultTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.GetStatusType <em>Get Status Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.GetStatusType
	 * @generated
	 */
	public Adapter createGetStatusTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.InputDescriptionType <em>Input Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.InputDescriptionType
	 * @generated
	 */
	public Adapter createInputDescriptionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.LiteralDataDomainType <em>Literal Data Domain Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.LiteralDataDomainType
	 * @generated
	 */
	public Adapter createLiteralDataDomainTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.LiteralDataDomainType1 <em>Literal Data Domain Type1</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.LiteralDataDomainType1
	 * @generated
	 */
	public Adapter createLiteralDataDomainType1Adapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.LiteralDataType <em>Literal Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.LiteralDataType
	 * @generated
	 */
	public Adapter createLiteralDataTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.LiteralValueType <em>Literal Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.LiteralValueType
	 * @generated
	 */
	public Adapter createLiteralValueTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.OutputDefinitionType <em>Output Definition Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.OutputDefinitionType
	 * @generated
	 */
	public Adapter createOutputDefinitionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.OutputDescriptionType <em>Output Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.OutputDescriptionType
	 * @generated
	 */
	public Adapter createOutputDescriptionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ProcessDescriptionType <em>Process Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ProcessDescriptionType
	 * @generated
	 */
	public Adapter createProcessDescriptionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ProcessOfferingsType <em>Process Offerings Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ProcessOfferingsType
	 * @generated
	 */
	public Adapter createProcessOfferingsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ProcessOfferingType <em>Process Offering Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ProcessOfferingType
	 * @generated
	 */
	public Adapter createProcessOfferingTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ProcessSummaryType <em>Process Summary Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ProcessSummaryType
	 * @generated
	 */
	public Adapter createProcessSummaryTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ReferenceType <em>Reference Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ReferenceType
	 * @generated
	 */
	public Adapter createReferenceTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.RequestBaseType <em>Request Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.RequestBaseType
	 * @generated
	 */
	public Adapter createRequestBaseTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.ResultType <em>Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.ResultType
	 * @generated
	 */
	public Adapter createResultTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.StatusInfoType <em>Status Info Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.StatusInfoType
	 * @generated
	 */
	public Adapter createStatusInfoTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.SupportedCRSType <em>Supported CRS Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.SupportedCRSType
	 * @generated
	 */
	public Adapter createSupportedCRSTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wps20.WPSCapabilitiesType <em>WPS Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wps20.WPSCapabilitiesType
	 * @generated
	 */
	public Adapter createWPSCapabilitiesTypeAdapter() {
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
	 * Creates a new adapter for an object of class '{@link net.opengis.ows20.DescriptionType <em>Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows20.DescriptionType
	 * @generated
	 */
	public Adapter createOws20_DescriptionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows20.BasicIdentificationType <em>Basic Identification Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows20.BasicIdentificationType
	 * @generated
	 */
	public Adapter createBasicIdentificationTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows20.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows20.GetCapabilitiesType
	 * @generated
	 */
	public Adapter createOws20_GetCapabilitiesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows20.ValueType <em>Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows20.ValueType
	 * @generated
	 */
	public Adapter createValueTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows20.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows20.CapabilitiesBaseType
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

} //Wps20AdapterFactory
