/**
 */
package net.opengis.ows20.util;

import net.opengis.ows20.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.ows20.Ows20Package
 * @generated
 */
public class Ows20AdapterFactory extends AdapterFactoryImpl {
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static Ows20Package modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Ows20AdapterFactory() {
    if (modelPackage == null) {
      modelPackage = Ows20Package.eINSTANCE;
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
  protected Ows20Switch<Adapter> modelSwitch =
    new Ows20Switch<Adapter>() {
      @Override
      public Adapter caseAbstractReferenceBaseType(AbstractReferenceBaseType object) {
        return createAbstractReferenceBaseTypeAdapter();
      }
      @Override
      public Adapter caseAcceptFormatsType(AcceptFormatsType object) {
        return createAcceptFormatsTypeAdapter();
      }
      @Override
      public Adapter caseAcceptLanguagesType(AcceptLanguagesType object) {
        return createAcceptLanguagesTypeAdapter();
      }
      @Override
      public Adapter caseAcceptVersionsType(AcceptVersionsType object) {
        return createAcceptVersionsTypeAdapter();
      }
      @Override
      public Adapter caseAdditionalParametersBaseType(AdditionalParametersBaseType object) {
        return createAdditionalParametersBaseTypeAdapter();
      }
      @Override
      public Adapter caseAdditionalParametersType(AdditionalParametersType object) {
        return createAdditionalParametersTypeAdapter();
      }
      @Override
      public Adapter caseAdditionalParameterType(AdditionalParameterType object) {
        return createAdditionalParameterTypeAdapter();
      }
      @Override
      public Adapter caseAddressType(AddressType object) {
        return createAddressTypeAdapter();
      }
      @Override
      public Adapter caseAllowedValuesType(AllowedValuesType object) {
        return createAllowedValuesTypeAdapter();
      }
      @Override
      public Adapter caseAnyValueType(AnyValueType object) {
        return createAnyValueTypeAdapter();
      }
      @Override
      public Adapter caseBasicIdentificationType(BasicIdentificationType object) {
        return createBasicIdentificationTypeAdapter();
      }
      @Override
      public Adapter caseBoundingBoxType(BoundingBoxType object) {
        return createBoundingBoxTypeAdapter();
      }
      @Override
      public Adapter caseCapabilitiesBaseType(CapabilitiesBaseType object) {
        return createCapabilitiesBaseTypeAdapter();
      }
      @Override
      public Adapter caseCodeType(CodeType object) {
        return createCodeTypeAdapter();
      }
      @Override
      public Adapter caseContactType(ContactType object) {
        return createContactTypeAdapter();
      }
      @Override
      public Adapter caseContentsBaseType(ContentsBaseType object) {
        return createContentsBaseTypeAdapter();
      }
      @Override
      public Adapter caseDatasetDescriptionSummaryBaseType(DatasetDescriptionSummaryBaseType object) {
        return createDatasetDescriptionSummaryBaseTypeAdapter();
      }
      @Override
      public Adapter caseDCPType(DCPType object) {
        return createDCPTypeAdapter();
      }
      @Override
      public Adapter caseDescriptionType(DescriptionType object) {
        return createDescriptionTypeAdapter();
      }
      @Override
      public Adapter caseDocumentRoot(DocumentRoot object) {
        return createDocumentRootAdapter();
      }
      @Override
      public Adapter caseDomainMetadataType(DomainMetadataType object) {
        return createDomainMetadataTypeAdapter();
      }
      @Override
      public Adapter caseDomainType(DomainType object) {
        return createDomainTypeAdapter();
      }
      @Override
      public Adapter caseExceptionReportType(ExceptionReportType object) {
        return createExceptionReportTypeAdapter();
      }
      @Override
      public Adapter caseExceptionType(ExceptionType object) {
        return createExceptionTypeAdapter();
      }
      @Override
      public Adapter caseGetCapabilitiesType(GetCapabilitiesType object) {
        return createGetCapabilitiesTypeAdapter();
      }
      @Override
      public Adapter caseGetResourceByIdType(GetResourceByIdType object) {
        return createGetResourceByIdTypeAdapter();
      }
      @Override
      public Adapter caseHTTPType(HTTPType object) {
        return createHTTPTypeAdapter();
      }
      @Override
      public Adapter caseIdentificationType(IdentificationType object) {
        return createIdentificationTypeAdapter();
      }
      @Override
      public Adapter caseKeywordsType(KeywordsType object) {
        return createKeywordsTypeAdapter();
      }
      @Override
      public Adapter caseLanguageStringType(LanguageStringType object) {
        return createLanguageStringTypeAdapter();
      }
      @Override
      public Adapter caseLanguagesType(LanguagesType object) {
        return createLanguagesTypeAdapter();
      }
      @Override
      public Adapter caseManifestType(ManifestType object) {
        return createManifestTypeAdapter();
      }
      @Override
      public Adapter caseMetadataType(MetadataType object) {
        return createMetadataTypeAdapter();
      }
      @Override
      public Adapter caseNilValueType(NilValueType object) {
        return createNilValueTypeAdapter();
      }
      @Override
      public Adapter caseNoValuesType(NoValuesType object) {
        return createNoValuesTypeAdapter();
      }
      @Override
      public Adapter caseOnlineResourceType(OnlineResourceType object) {
        return createOnlineResourceTypeAdapter();
      }
      @Override
      public Adapter caseOperationsMetadataType(OperationsMetadataType object) {
        return createOperationsMetadataTypeAdapter();
      }
      @Override
      public Adapter caseOperationType(OperationType object) {
        return createOperationTypeAdapter();
      }
      @Override
      public Adapter caseRangeType(RangeType object) {
        return createRangeTypeAdapter();
      }
      @Override
      public Adapter caseReferenceGroupType(ReferenceGroupType object) {
        return createReferenceGroupTypeAdapter();
      }
      @Override
      public Adapter caseReferenceType(ReferenceType object) {
        return createReferenceTypeAdapter();
      }
      @Override
      public Adapter caseRequestMethodType(RequestMethodType object) {
        return createRequestMethodTypeAdapter();
      }
      @Override
      public Adapter caseResponsiblePartySubsetType(ResponsiblePartySubsetType object) {
        return createResponsiblePartySubsetTypeAdapter();
      }
      @Override
      public Adapter caseResponsiblePartyType(ResponsiblePartyType object) {
        return createResponsiblePartyTypeAdapter();
      }
      @Override
      public Adapter caseSectionsType(SectionsType object) {
        return createSectionsTypeAdapter();
      }
      @Override
      public Adapter caseServiceIdentificationType(ServiceIdentificationType object) {
        return createServiceIdentificationTypeAdapter();
      }
      @Override
      public Adapter caseServiceProviderType(ServiceProviderType object) {
        return createServiceProviderTypeAdapter();
      }
      @Override
      public Adapter caseServiceReferenceType(ServiceReferenceType object) {
        return createServiceReferenceTypeAdapter();
      }
      @Override
      public Adapter caseTelephoneType(TelephoneType object) {
        return createTelephoneTypeAdapter();
      }
      @Override
      public Adapter caseUnNamedDomainType(UnNamedDomainType object) {
        return createUnNamedDomainTypeAdapter();
      }
      @Override
      public Adapter caseValuesReferenceType(ValuesReferenceType object) {
        return createValuesReferenceTypeAdapter();
      }
      @Override
      public Adapter caseValueType(ValueType object) {
        return createValueTypeAdapter();
      }
      @Override
      public Adapter caseWGS84BoundingBoxType(WGS84BoundingBoxType object) {
        return createWGS84BoundingBoxTypeAdapter();
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
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AbstractReferenceBaseType <em>Abstract Reference Base Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AbstractReferenceBaseType
   * @generated
   */
  public Adapter createAbstractReferenceBaseTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AcceptFormatsType <em>Accept Formats Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AcceptFormatsType
   * @generated
   */
  public Adapter createAcceptFormatsTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AcceptLanguagesType <em>Accept Languages Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AcceptLanguagesType
   * @generated
   */
  public Adapter createAcceptLanguagesTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AcceptVersionsType <em>Accept Versions Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AcceptVersionsType
   * @generated
   */
  public Adapter createAcceptVersionsTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AdditionalParametersBaseType <em>Additional Parameters Base Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AdditionalParametersBaseType
   * @generated
   */
  public Adapter createAdditionalParametersBaseTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AdditionalParametersType <em>Additional Parameters Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AdditionalParametersType
   * @generated
   */
  public Adapter createAdditionalParametersTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AdditionalParameterType <em>Additional Parameter Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AdditionalParameterType
   * @generated
   */
  public Adapter createAdditionalParameterTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AddressType <em>Address Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AddressType
   * @generated
   */
  public Adapter createAddressTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AllowedValuesType <em>Allowed Values Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AllowedValuesType
   * @generated
   */
  public Adapter createAllowedValuesTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.AnyValueType <em>Any Value Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.AnyValueType
   * @generated
   */
  public Adapter createAnyValueTypeAdapter() {
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
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.BoundingBoxType <em>Bounding Box Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.BoundingBoxType
   * @generated
   */
  public Adapter createBoundingBoxTypeAdapter() {
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
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.CodeType <em>Code Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.CodeType
   * @generated
   */
  public Adapter createCodeTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ContactType <em>Contact Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ContactType
   * @generated
   */
  public Adapter createContactTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ContentsBaseType <em>Contents Base Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ContentsBaseType
   * @generated
   */
  public Adapter createContentsBaseTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType <em>Dataset Description Summary Base Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.DatasetDescriptionSummaryBaseType
   * @generated
   */
  public Adapter createDatasetDescriptionSummaryBaseTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.DCPType <em>DCP Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.DCPType
   * @generated
   */
  public Adapter createDCPTypeAdapter() {
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
  public Adapter createDescriptionTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.DocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.DocumentRoot
   * @generated
   */
  public Adapter createDocumentRootAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.DomainMetadataType <em>Domain Metadata Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.DomainMetadataType
   * @generated
   */
  public Adapter createDomainMetadataTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.DomainType <em>Domain Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.DomainType
   * @generated
   */
  public Adapter createDomainTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ExceptionReportType <em>Exception Report Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ExceptionReportType
   * @generated
   */
  public Adapter createExceptionReportTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ExceptionType <em>Exception Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ExceptionType
   * @generated
   */
  public Adapter createExceptionTypeAdapter() {
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
  public Adapter createGetCapabilitiesTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.GetResourceByIdType <em>Get Resource By Id Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.GetResourceByIdType
   * @generated
   */
  public Adapter createGetResourceByIdTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.HTTPType <em>HTTP Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.HTTPType
   * @generated
   */
  public Adapter createHTTPTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.IdentificationType <em>Identification Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.IdentificationType
   * @generated
   */
  public Adapter createIdentificationTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.KeywordsType <em>Keywords Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.KeywordsType
   * @generated
   */
  public Adapter createKeywordsTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.LanguageStringType <em>Language String Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.LanguageStringType
   * @generated
   */
  public Adapter createLanguageStringTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.LanguagesType <em>Languages Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.LanguagesType
   * @generated
   */
  public Adapter createLanguagesTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ManifestType <em>Manifest Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ManifestType
   * @generated
   */
  public Adapter createManifestTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.MetadataType <em>Metadata Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.MetadataType
   * @generated
   */
  public Adapter createMetadataTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.NilValueType <em>Nil Value Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.NilValueType
   * @generated
   */
  public Adapter createNilValueTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.NoValuesType <em>No Values Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.NoValuesType
   * @generated
   */
  public Adapter createNoValuesTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.OnlineResourceType <em>Online Resource Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.OnlineResourceType
   * @generated
   */
  public Adapter createOnlineResourceTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.OperationsMetadataType <em>Operations Metadata Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.OperationsMetadataType
   * @generated
   */
  public Adapter createOperationsMetadataTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.OperationType <em>Operation Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.OperationType
   * @generated
   */
  public Adapter createOperationTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.RangeType <em>Range Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.RangeType
   * @generated
   */
  public Adapter createRangeTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ReferenceGroupType <em>Reference Group Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ReferenceGroupType
   * @generated
   */
  public Adapter createReferenceGroupTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ReferenceType <em>Reference Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ReferenceType
   * @generated
   */
  public Adapter createReferenceTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.RequestMethodType <em>Request Method Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.RequestMethodType
   * @generated
   */
  public Adapter createRequestMethodTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ResponsiblePartySubsetType <em>Responsible Party Subset Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ResponsiblePartySubsetType
   * @generated
   */
  public Adapter createResponsiblePartySubsetTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ResponsiblePartyType <em>Responsible Party Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ResponsiblePartyType
   * @generated
   */
  public Adapter createResponsiblePartyTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.SectionsType <em>Sections Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.SectionsType
   * @generated
   */
  public Adapter createSectionsTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ServiceIdentificationType <em>Service Identification Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ServiceIdentificationType
   * @generated
   */
  public Adapter createServiceIdentificationTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ServiceProviderType <em>Service Provider Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ServiceProviderType
   * @generated
   */
  public Adapter createServiceProviderTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ServiceReferenceType <em>Service Reference Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ServiceReferenceType
   * @generated
   */
  public Adapter createServiceReferenceTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.TelephoneType <em>Telephone Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.TelephoneType
   * @generated
   */
  public Adapter createTelephoneTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.UnNamedDomainType <em>Un Named Domain Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.UnNamedDomainType
   * @generated
   */
  public Adapter createUnNamedDomainTypeAdapter() {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.ValuesReferenceType <em>Values Reference Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.ValuesReferenceType
   * @generated
   */
  public Adapter createValuesReferenceTypeAdapter() {
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
   * Creates a new adapter for an object of class '{@link net.opengis.ows20.WGS84BoundingBoxType <em>WGS84 Bounding Box Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see net.opengis.ows20.WGS84BoundingBoxType
   * @generated
   */
  public Adapter createWGS84BoundingBoxTypeAdapter() {
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

} //Ows20AdapterFactory
