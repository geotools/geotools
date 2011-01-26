/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.util;

import java.util.List;

import net.opengis.ows11.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

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
 * @see net.opengis.ows11.Ows11Package
 * @generated
 */
public class Ows11Switch {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Ows11Package modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Ows11Switch() {
        if (modelPackage == null) {
            modelPackage = Ows11Package.eINSTANCE;
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
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE: {
                AbstractReferenceBaseType abstractReferenceBaseType = (AbstractReferenceBaseType)theEObject;
                Object result = caseAbstractReferenceBaseType(abstractReferenceBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.ACCEPT_FORMATS_TYPE: {
                AcceptFormatsType acceptFormatsType = (AcceptFormatsType)theEObject;
                Object result = caseAcceptFormatsType(acceptFormatsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.ACCEPT_VERSIONS_TYPE: {
                AcceptVersionsType acceptVersionsType = (AcceptVersionsType)theEObject;
                Object result = caseAcceptVersionsType(acceptVersionsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.ADDRESS_TYPE: {
                AddressType addressType = (AddressType)theEObject;
                Object result = caseAddressType(addressType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.ALLOWED_VALUES_TYPE: {
                AllowedValuesType allowedValuesType = (AllowedValuesType)theEObject;
                Object result = caseAllowedValuesType(allowedValuesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.ANY_VALUE_TYPE: {
                AnyValueType anyValueType = (AnyValueType)theEObject;
                Object result = caseAnyValueType(anyValueType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.BASIC_IDENTIFICATION_TYPE: {
                BasicIdentificationType basicIdentificationType = (BasicIdentificationType)theEObject;
                Object result = caseBasicIdentificationType(basicIdentificationType);
                if (result == null) result = caseDescriptionType(basicIdentificationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.BOUNDING_BOX_TYPE: {
                BoundingBoxType boundingBoxType = (BoundingBoxType)theEObject;
                Object result = caseBoundingBoxType(boundingBoxType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.CAPABILITIES_BASE_TYPE: {
                CapabilitiesBaseType capabilitiesBaseType = (CapabilitiesBaseType)theEObject;
                Object result = caseCapabilitiesBaseType(capabilitiesBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.CODE_TYPE: {
                CodeType codeType = (CodeType)theEObject;
                Object result = caseCodeType(codeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.CONTACT_TYPE: {
                ContactType contactType = (ContactType)theEObject;
                Object result = caseContactType(contactType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.CONTENTS_BASE_TYPE: {
                ContentsBaseType contentsBaseType = (ContentsBaseType)theEObject;
                Object result = caseContentsBaseType(contentsBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE: {
                DatasetDescriptionSummaryBaseType datasetDescriptionSummaryBaseType = (DatasetDescriptionSummaryBaseType)theEObject;
                Object result = caseDatasetDescriptionSummaryBaseType(datasetDescriptionSummaryBaseType);
                if (result == null) result = caseDescriptionType(datasetDescriptionSummaryBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.DCP_TYPE: {
                DCPType dcpType = (DCPType)theEObject;
                Object result = caseDCPType(dcpType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.DESCRIPTION_TYPE: {
                DescriptionType descriptionType = (DescriptionType)theEObject;
                Object result = caseDescriptionType(descriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                Object result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.DOMAIN_METADATA_TYPE: {
                DomainMetadataType domainMetadataType = (DomainMetadataType)theEObject;
                Object result = caseDomainMetadataType(domainMetadataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.DOMAIN_TYPE: {
                DomainType domainType = (DomainType)theEObject;
                Object result = caseDomainType(domainType);
                if (result == null) result = caseUnNamedDomainType(domainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.EXCEPTION_REPORT_TYPE: {
                ExceptionReportType exceptionReportType = (ExceptionReportType)theEObject;
                Object result = caseExceptionReportType(exceptionReportType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.EXCEPTION_TYPE: {
                ExceptionType exceptionType = (ExceptionType)theEObject;
                Object result = caseExceptionType(exceptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.GET_CAPABILITIES_TYPE: {
                GetCapabilitiesType getCapabilitiesType = (GetCapabilitiesType)theEObject;
                Object result = caseGetCapabilitiesType(getCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.GET_RESOURCE_BY_ID_TYPE: {
                GetResourceByIdType getResourceByIdType = (GetResourceByIdType)theEObject;
                Object result = caseGetResourceByIdType(getResourceByIdType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.HTTP_TYPE: {
                HTTPType httpType = (HTTPType)theEObject;
                Object result = caseHTTPType(httpType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.IDENTIFICATION_TYPE: {
                IdentificationType identificationType = (IdentificationType)theEObject;
                Object result = caseIdentificationType(identificationType);
                if (result == null) result = caseBasicIdentificationType(identificationType);
                if (result == null) result = caseDescriptionType(identificationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.KEYWORDS_TYPE: {
                KeywordsType keywordsType = (KeywordsType)theEObject;
                Object result = caseKeywordsType(keywordsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.LANGUAGE_STRING_TYPE: {
                LanguageStringType languageStringType = (LanguageStringType)theEObject;
                Object result = caseLanguageStringType(languageStringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.MANIFEST_TYPE: {
                ManifestType manifestType = (ManifestType)theEObject;
                Object result = caseManifestType(manifestType);
                if (result == null) result = caseBasicIdentificationType(manifestType);
                if (result == null) result = caseDescriptionType(manifestType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.METADATA_TYPE: {
                MetadataType metadataType = (MetadataType)theEObject;
                Object result = caseMetadataType(metadataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.NO_VALUES_TYPE: {
                NoValuesType noValuesType = (NoValuesType)theEObject;
                Object result = caseNoValuesType(noValuesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.ONLINE_RESOURCE_TYPE: {
                OnlineResourceType onlineResourceType = (OnlineResourceType)theEObject;
                Object result = caseOnlineResourceType(onlineResourceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.OPERATIONS_METADATA_TYPE: {
                OperationsMetadataType operationsMetadataType = (OperationsMetadataType)theEObject;
                Object result = caseOperationsMetadataType(operationsMetadataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.OPERATION_TYPE: {
                OperationType operationType = (OperationType)theEObject;
                Object result = caseOperationType(operationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.RANGE_TYPE: {
                RangeType rangeType = (RangeType)theEObject;
                Object result = caseRangeType(rangeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.REFERENCE_GROUP_TYPE: {
                ReferenceGroupType referenceGroupType = (ReferenceGroupType)theEObject;
                Object result = caseReferenceGroupType(referenceGroupType);
                if (result == null) result = caseBasicIdentificationType(referenceGroupType);
                if (result == null) result = caseDescriptionType(referenceGroupType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.REFERENCE_TYPE: {
                ReferenceType referenceType = (ReferenceType)theEObject;
                Object result = caseReferenceType(referenceType);
                if (result == null) result = caseAbstractReferenceBaseType(referenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.REQUEST_METHOD_TYPE: {
                RequestMethodType requestMethodType = (RequestMethodType)theEObject;
                Object result = caseRequestMethodType(requestMethodType);
                if (result == null) result = caseOnlineResourceType(requestMethodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.RESPONSIBLE_PARTY_SUBSET_TYPE: {
                ResponsiblePartySubsetType responsiblePartySubsetType = (ResponsiblePartySubsetType)theEObject;
                Object result = caseResponsiblePartySubsetType(responsiblePartySubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.RESPONSIBLE_PARTY_TYPE: {
                ResponsiblePartyType responsiblePartyType = (ResponsiblePartyType)theEObject;
                Object result = caseResponsiblePartyType(responsiblePartyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.SECTIONS_TYPE: {
                SectionsType sectionsType = (SectionsType)theEObject;
                Object result = caseSectionsType(sectionsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE: {
                ServiceIdentificationType serviceIdentificationType = (ServiceIdentificationType)theEObject;
                Object result = caseServiceIdentificationType(serviceIdentificationType);
                if (result == null) result = caseDescriptionType(serviceIdentificationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.SERVICE_PROVIDER_TYPE: {
                ServiceProviderType serviceProviderType = (ServiceProviderType)theEObject;
                Object result = caseServiceProviderType(serviceProviderType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.SERVICE_REFERENCE_TYPE: {
                ServiceReferenceType serviceReferenceType = (ServiceReferenceType)theEObject;
                Object result = caseServiceReferenceType(serviceReferenceType);
                if (result == null) result = caseReferenceType(serviceReferenceType);
                if (result == null) result = caseAbstractReferenceBaseType(serviceReferenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.TELEPHONE_TYPE: {
                TelephoneType telephoneType = (TelephoneType)theEObject;
                Object result = caseTelephoneType(telephoneType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.UN_NAMED_DOMAIN_TYPE: {
                UnNamedDomainType unNamedDomainType = (UnNamedDomainType)theEObject;
                Object result = caseUnNamedDomainType(unNamedDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.VALUES_REFERENCE_TYPE: {
                ValuesReferenceType valuesReferenceType = (ValuesReferenceType)theEObject;
                Object result = caseValuesReferenceType(valuesReferenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.VALUE_TYPE: {
                ValueType valueType = (ValueType)theEObject;
                Object result = caseValueType(valueType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Ows11Package.WGS84_BOUNDING_BOX_TYPE: {
                WGS84BoundingBoxType wgs84BoundingBoxType = (WGS84BoundingBoxType)theEObject;
                Object result = caseWGS84BoundingBoxType(wgs84BoundingBoxType);
                if (result == null) result = caseBoundingBoxType(wgs84BoundingBoxType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Reference Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Reference Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAbstractReferenceBaseType(AbstractReferenceBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Accept Formats Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Accept Formats Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAcceptFormatsType(AcceptFormatsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Accept Versions Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Accept Versions Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAcceptVersionsType(AcceptVersionsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Address Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Address Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAddressType(AddressType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allowed Values Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allowed Values Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAllowedValuesType(AllowedValuesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Any Value Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Any Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAnyValueType(AnyValueType object) {
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
    public Object caseBasicIdentificationType(BasicIdentificationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bounding Box Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bounding Box Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseBoundingBoxType(BoundingBoxType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Code Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Code Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCodeType(CodeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Contact Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Contact Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseContactType(ContactType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Contents Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Contents Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseContentsBaseType(ContentsBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dataset Description Summary Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dataset Description Summary Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDatasetDescriptionSummaryBaseType(DatasetDescriptionSummaryBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>DCP Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>DCP Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDCPType(DCPType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Domain Metadata Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Domain Metadata Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDomainMetadataType(DomainMetadataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDomainType(DomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Exception Report Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Exception Report Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExceptionReportType(ExceptionReportType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Exception Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Exception Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseExceptionType(ExceptionType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Get Resource By Id Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Resource By Id Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetResourceByIdType(GetResourceByIdType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>HTTP Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>HTTP Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseHTTPType(HTTPType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Identification Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Identification Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseIdentificationType(IdentificationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Keywords Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Keywords Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseKeywordsType(KeywordsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Language String Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Language String Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLanguageStringType(LanguageStringType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Manifest Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Manifest Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseManifestType(ManifestType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseMetadataType(MetadataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>No Values Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>No Values Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseNoValuesType(NoValuesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Online Resource Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Online Resource Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOnlineResourceType(OnlineResourceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operations Metadata Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operations Metadata Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOperationsMetadataType(OperationsMetadataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOperationType(OperationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRangeType(RangeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference Group Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference Group Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseReferenceGroupType(ReferenceGroupType object) {
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
    public Object caseReferenceType(ReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Request Method Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Request Method Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRequestMethodType(RequestMethodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Responsible Party Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Responsible Party Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseResponsiblePartySubsetType(ResponsiblePartySubsetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Responsible Party Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Responsible Party Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseResponsiblePartyType(ResponsiblePartyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sections Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sections Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSectionsType(SectionsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Service Identification Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Service Identification Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseServiceIdentificationType(ServiceIdentificationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Service Provider Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Service Provider Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseServiceProviderType(ServiceProviderType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Service Reference Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Service Reference Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseServiceReferenceType(ServiceReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Telephone Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Telephone Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseTelephoneType(TelephoneType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Un Named Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Un Named Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseUnNamedDomainType(UnNamedDomainType object) {
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
    public Object caseValueType(ValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>WGS84 Bounding Box Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WGS84 Bounding Box Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseWGS84BoundingBoxType(WGS84BoundingBoxType object) {
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

} //Ows11Switch
