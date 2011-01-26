/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.util;

import java.util.List;

import net.opengis.gml.AbstractGMLType;
import net.opengis.gml.AbstractGeometryBaseType;
import net.opengis.gml.AbstractGeometryType;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.MetaDataPropertyType;

import net.opengis.wcs10.*;

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
 * @see net.opengis.wcs10.Wcs10Package
 * @generated
 */
public class Wcs10Switch {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Wcs10Package modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wcs10Switch() {
        if (modelPackage == null) {
            modelPackage = Wcs10Package.eINSTANCE;
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
            case Wcs10Package.ABSTRACT_DESCRIPTION_BASE_TYPE: {
                AbstractDescriptionBaseType abstractDescriptionBaseType = (AbstractDescriptionBaseType)theEObject;
                Object result = caseAbstractDescriptionBaseType(abstractDescriptionBaseType);
                if (result == null) result = caseAbstractGMLType(abstractDescriptionBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE: {
                AbstractDescriptionType abstractDescriptionType = (AbstractDescriptionType)theEObject;
                Object result = caseAbstractDescriptionType(abstractDescriptionType);
                if (result == null) result = caseAbstractDescriptionBaseType(abstractDescriptionType);
                if (result == null) result = caseAbstractGMLType(abstractDescriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.ADDRESS_TYPE: {
                AddressType addressType = (AddressType)theEObject;
                Object result = caseAddressType(addressType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.AXIS_DESCRIPTION_TYPE: {
                AxisDescriptionType axisDescriptionType = (AxisDescriptionType)theEObject;
                Object result = caseAxisDescriptionType(axisDescriptionType);
                if (result == null) result = caseAbstractDescriptionType(axisDescriptionType);
                if (result == null) result = caseAbstractDescriptionBaseType(axisDescriptionType);
                if (result == null) result = caseAbstractGMLType(axisDescriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.AXIS_DESCRIPTION_TYPE1: {
                AxisDescriptionType1 axisDescriptionType1 = (AxisDescriptionType1)theEObject;
                Object result = caseAxisDescriptionType1(axisDescriptionType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.AXIS_SUBSET_TYPE: {
                AxisSubsetType axisSubsetType = (AxisSubsetType)theEObject;
                Object result = caseAxisSubsetType(axisSubsetType);
                if (result == null) result = caseValueEnumBaseType(axisSubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.CONTACT_TYPE: {
                ContactType contactType = (ContactType)theEObject;
                Object result = caseContactType(contactType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.CONTENT_METADATA_TYPE: {
                ContentMetadataType contentMetadataType = (ContentMetadataType)theEObject;
                Object result = caseContentMetadataType(contentMetadataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.COVERAGE_DESCRIPTION_TYPE: {
                CoverageDescriptionType coverageDescriptionType = (CoverageDescriptionType)theEObject;
                Object result = caseCoverageDescriptionType(coverageDescriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.COVERAGE_OFFERING_BRIEF_TYPE: {
                CoverageOfferingBriefType coverageOfferingBriefType = (CoverageOfferingBriefType)theEObject;
                Object result = caseCoverageOfferingBriefType(coverageOfferingBriefType);
                if (result == null) result = caseAbstractDescriptionType(coverageOfferingBriefType);
                if (result == null) result = caseAbstractDescriptionBaseType(coverageOfferingBriefType);
                if (result == null) result = caseAbstractGMLType(coverageOfferingBriefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.COVERAGE_OFFERING_TYPE: {
                CoverageOfferingType coverageOfferingType = (CoverageOfferingType)theEObject;
                Object result = caseCoverageOfferingType(coverageOfferingType);
                if (result == null) result = caseCoverageOfferingBriefType(coverageOfferingType);
                if (result == null) result = caseAbstractDescriptionType(coverageOfferingType);
                if (result == null) result = caseAbstractDescriptionBaseType(coverageOfferingType);
                if (result == null) result = caseAbstractGMLType(coverageOfferingType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.DCP_TYPE_TYPE: {
                DCPTypeType dcpTypeType = (DCPTypeType)theEObject;
                Object result = caseDCPTypeType(dcpTypeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.DESCRIBE_COVERAGE_TYPE: {
                DescribeCoverageType describeCoverageType = (DescribeCoverageType)theEObject;
                Object result = caseDescribeCoverageType(describeCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.DESCRIBE_COVERAGE_TYPE1: {
                DescribeCoverageType1 describeCoverageType1 = (DescribeCoverageType1)theEObject;
                Object result = caseDescribeCoverageType1(describeCoverageType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                Object result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.DOMAIN_SET_TYPE: {
                DomainSetType domainSetType = (DomainSetType)theEObject;
                Object result = caseDomainSetType(domainSetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.DOMAIN_SUBSET_TYPE: {
                DomainSubsetType domainSubsetType = (DomainSubsetType)theEObject;
                Object result = caseDomainSubsetType(domainSubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.EXCEPTION_TYPE: {
                ExceptionType exceptionType = (ExceptionType)theEObject;
                Object result = caseExceptionType(exceptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.GET_CAPABILITIES_TYPE: {
                GetCapabilitiesType getCapabilitiesType = (GetCapabilitiesType)theEObject;
                Object result = caseGetCapabilitiesType(getCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.GET_CAPABILITIES_TYPE1: {
                GetCapabilitiesType1 getCapabilitiesType1 = (GetCapabilitiesType1)theEObject;
                Object result = caseGetCapabilitiesType1(getCapabilitiesType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.GET_COVERAGE_TYPE: {
                GetCoverageType getCoverageType = (GetCoverageType)theEObject;
                Object result = caseGetCoverageType(getCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.GET_COVERAGE_TYPE1: {
                GetCoverageType1 getCoverageType1 = (GetCoverageType1)theEObject;
                Object result = caseGetCoverageType1(getCoverageType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.GET_TYPE: {
                GetType getType = (GetType)theEObject;
                Object result = caseGetType(getType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.HTTP_TYPE: {
                HTTPType httpType = (HTTPType)theEObject;
                Object result = caseHTTPType(httpType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.INTERVAL_TYPE: {
                IntervalType intervalType = (IntervalType)theEObject;
                Object result = caseIntervalType(intervalType);
                if (result == null) result = caseValueRangeType(intervalType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.KEYWORDS_TYPE: {
                KeywordsType keywordsType = (KeywordsType)theEObject;
                Object result = caseKeywordsType(keywordsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.LON_LAT_ENVELOPE_BASE_TYPE: {
                LonLatEnvelopeBaseType lonLatEnvelopeBaseType = (LonLatEnvelopeBaseType)theEObject;
                Object result = caseLonLatEnvelopeBaseType(lonLatEnvelopeBaseType);
                if (result == null) result = caseEnvelopeType(lonLatEnvelopeBaseType);
                if (result == null) result = caseAbstractGeometryType(lonLatEnvelopeBaseType);
                if (result == null) result = caseAbstractGeometryBaseType(lonLatEnvelopeBaseType);
                if (result == null) result = caseAbstractGMLType(lonLatEnvelopeBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.LON_LAT_ENVELOPE_TYPE: {
                LonLatEnvelopeType lonLatEnvelopeType = (LonLatEnvelopeType)theEObject;
                Object result = caseLonLatEnvelopeType(lonLatEnvelopeType);
                if (result == null) result = caseLonLatEnvelopeBaseType(lonLatEnvelopeType);
                if (result == null) result = caseEnvelopeType(lonLatEnvelopeType);
                if (result == null) result = caseAbstractGeometryType(lonLatEnvelopeType);
                if (result == null) result = caseAbstractGeometryBaseType(lonLatEnvelopeType);
                if (result == null) result = caseAbstractGMLType(lonLatEnvelopeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.METADATA_ASSOCIATION_TYPE: {
                MetadataAssociationType metadataAssociationType = (MetadataAssociationType)theEObject;
                Object result = caseMetadataAssociationType(metadataAssociationType);
                if (result == null) result = caseMetaDataPropertyType(metadataAssociationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.METADATA_LINK_TYPE: {
                MetadataLinkType metadataLinkType = (MetadataLinkType)theEObject;
                Object result = caseMetadataLinkType(metadataLinkType);
                if (result == null) result = caseMetadataAssociationType(metadataLinkType);
                if (result == null) result = caseMetaDataPropertyType(metadataLinkType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.ONLINE_RESOURCE_TYPE: {
                OnlineResourceType onlineResourceType = (OnlineResourceType)theEObject;
                Object result = caseOnlineResourceType(onlineResourceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.OUTPUT_TYPE: {
                OutputType outputType = (OutputType)theEObject;
                Object result = caseOutputType(outputType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.POST_TYPE: {
                PostType postType = (PostType)theEObject;
                Object result = casePostType(postType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.RANGE_SET_TYPE: {
                RangeSetType rangeSetType = (RangeSetType)theEObject;
                Object result = caseRangeSetType(rangeSetType);
                if (result == null) result = caseAbstractDescriptionType(rangeSetType);
                if (result == null) result = caseAbstractDescriptionBaseType(rangeSetType);
                if (result == null) result = caseAbstractGMLType(rangeSetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.RANGE_SET_TYPE1: {
                RangeSetType1 rangeSetType1 = (RangeSetType1)theEObject;
                Object result = caseRangeSetType1(rangeSetType1);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.RANGE_SUBSET_TYPE: {
                RangeSubsetType rangeSubsetType = (RangeSubsetType)theEObject;
                Object result = caseRangeSubsetType(rangeSubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.REQUEST_TYPE: {
                RequestType requestType = (RequestType)theEObject;
                Object result = caseRequestType(requestType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.RESPONSIBLE_PARTY_TYPE: {
                ResponsiblePartyType responsiblePartyType = (ResponsiblePartyType)theEObject;
                Object result = caseResponsiblePartyType(responsiblePartyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.SERVICE_TYPE: {
                ServiceType serviceType = (ServiceType)theEObject;
                Object result = caseServiceType(serviceType);
                if (result == null) result = caseAbstractDescriptionType(serviceType);
                if (result == null) result = caseAbstractDescriptionBaseType(serviceType);
                if (result == null) result = caseAbstractGMLType(serviceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.SPATIAL_DOMAIN_TYPE: {
                SpatialDomainType spatialDomainType = (SpatialDomainType)theEObject;
                Object result = caseSpatialDomainType(spatialDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.SPATIAL_SUBSET_TYPE: {
                SpatialSubsetType spatialSubsetType = (SpatialSubsetType)theEObject;
                Object result = caseSpatialSubsetType(spatialSubsetType);
                if (result == null) result = caseSpatialDomainType(spatialSubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.SUPPORTED_CR_SS_TYPE: {
                SupportedCRSsType supportedCRSsType = (SupportedCRSsType)theEObject;
                Object result = caseSupportedCRSsType(supportedCRSsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.SUPPORTED_FORMATS_TYPE: {
                SupportedFormatsType supportedFormatsType = (SupportedFormatsType)theEObject;
                Object result = caseSupportedFormatsType(supportedFormatsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.SUPPORTED_INTERPOLATIONS_TYPE: {
                SupportedInterpolationsType supportedInterpolationsType = (SupportedInterpolationsType)theEObject;
                Object result = caseSupportedInterpolationsType(supportedInterpolationsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.TELEPHONE_TYPE: {
                TelephoneType telephoneType = (TelephoneType)theEObject;
                Object result = caseTelephoneType(telephoneType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.TIME_PERIOD_TYPE: {
                TimePeriodType timePeriodType = (TimePeriodType)theEObject;
                Object result = caseTimePeriodType(timePeriodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.TIME_SEQUENCE_TYPE: {
                TimeSequenceType timeSequenceType = (TimeSequenceType)theEObject;
                Object result = caseTimeSequenceType(timeSequenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.TYPED_LITERAL_TYPE: {
                TypedLiteralType typedLiteralType = (TypedLiteralType)theEObject;
                Object result = caseTypedLiteralType(typedLiteralType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.VALUE_ENUM_BASE_TYPE: {
                ValueEnumBaseType valueEnumBaseType = (ValueEnumBaseType)theEObject;
                Object result = caseValueEnumBaseType(valueEnumBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.VALUE_ENUM_TYPE: {
                ValueEnumType valueEnumType = (ValueEnumType)theEObject;
                Object result = caseValueEnumType(valueEnumType);
                if (result == null) result = caseValueEnumBaseType(valueEnumType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.VALUE_RANGE_TYPE: {
                ValueRangeType valueRangeType = (ValueRangeType)theEObject;
                Object result = caseValueRangeType(valueRangeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.VALUES_TYPE: {
                ValuesType valuesType = (ValuesType)theEObject;
                Object result = caseValuesType(valuesType);
                if (result == null) result = caseValueEnumType(valuesType);
                if (result == null) result = caseValueEnumBaseType(valuesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.VENDOR_SPECIFIC_CAPABILITIES_TYPE: {
                VendorSpecificCapabilitiesType vendorSpecificCapabilitiesType = (VendorSpecificCapabilitiesType)theEObject;
                Object result = caseVendorSpecificCapabilitiesType(vendorSpecificCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.WCS_CAPABILITIES_TYPE: {
                WCSCapabilitiesType wcsCapabilitiesType = (WCSCapabilitiesType)theEObject;
                Object result = caseWCSCapabilitiesType(wcsCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs10Package.WCS_CAPABILITY_TYPE: {
                WCSCapabilityType wcsCapabilityType = (WCSCapabilityType)theEObject;
                Object result = caseWCSCapabilityType(wcsCapabilityType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Description Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Description Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAbstractDescriptionBaseType(AbstractDescriptionBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Description Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Description Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAbstractDescriptionType(AbstractDescriptionType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Axis Description Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Axis Description Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAxisDescriptionType(AxisDescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Axis Description Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Axis Description Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAxisDescriptionType1(AxisDescriptionType1 object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Axis Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Axis Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAxisSubsetType(AxisSubsetType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Content Metadata Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Content Metadata Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseContentMetadataType(ContentMetadataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverage Description Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverage Description Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoverageDescriptionType(CoverageDescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverage Offering Brief Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverage Offering Brief Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoverageOfferingBriefType(CoverageOfferingBriefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverage Offering Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverage Offering Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoverageOfferingType(CoverageOfferingType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>DCP Type Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>DCP Type Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDCPTypeType(DCPTypeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Describe Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Describe Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDescribeCoverageType(DescribeCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Describe Coverage Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Describe Coverage Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDescribeCoverageType1(DescribeCoverageType1 object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Domain Set Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Domain Set Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDomainSetType(DomainSetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Domain Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Domain Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDomainSubsetType(DomainSubsetType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Get Capabilities Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Capabilities Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetCapabilitiesType1(GetCapabilitiesType1 object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetCoverageType(GetCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Coverage Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Coverage Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetCoverageType1(GetCoverageType1 object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetType(GetType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Interval Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interval Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseIntervalType(IntervalType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Lon Lat Envelope Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Lon Lat Envelope Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLonLatEnvelopeBaseType(LonLatEnvelopeBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Lon Lat Envelope Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Lon Lat Envelope Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLonLatEnvelopeType(LonLatEnvelopeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Association Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Association Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseMetadataAssociationType(MetadataAssociationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Link Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Link Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseMetadataLinkType(MetadataLinkType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Output Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Output Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOutputType(OutputType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Post Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Post Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object casePostType(PostType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Set Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Set Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRangeSetType(RangeSetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Set Type1</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Set Type1</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRangeSetType1(RangeSetType1 object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRangeSubsetType(RangeSubsetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Request Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Request Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRequestType(RequestType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Service Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Service Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseServiceType(ServiceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Spatial Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Spatial Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSpatialDomainType(SpatialDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Spatial Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Spatial Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSpatialSubsetType(SpatialSubsetType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Supported Formats Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Supported Formats Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSupportedFormatsType(SupportedFormatsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Supported Interpolations Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Supported Interpolations Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSupportedInterpolationsType(SupportedInterpolationsType object) {
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
     * Returns the result of interpreting the object as an instance of '<em>Time Period Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Period Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseTimePeriodType(TimePeriodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Sequence Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Sequence Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseTimeSequenceType(TimeSequenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Typed Literal Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Typed Literal Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseTypedLiteralType(TypedLiteralType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Enum Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Enum Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseValueEnumBaseType(ValueEnumBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Enum Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Enum Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseValueEnumType(ValueEnumType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Range Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Range Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseValueRangeType(ValueRangeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Values Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Values Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseValuesType(ValuesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vendor Specific Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vendor Specific Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseVendorSpecificCapabilitiesType(VendorSpecificCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>WCS Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WCS Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseWCSCapabilitiesType(WCSCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>WCS Capability Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WCS Capability Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseWCSCapabilityType(WCSCapabilityType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract GML Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract GML Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAbstractGMLType(AbstractGMLType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Geometry Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Geometry Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAbstractGeometryBaseType(AbstractGeometryBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Geometry Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Geometry Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAbstractGeometryType(AbstractGeometryType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Envelope Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Envelope Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseEnvelopeType(EnvelopeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Meta Data Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Meta Data Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseMetaDataPropertyType(MetaDataPropertyType object) {
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

} //Wcs10Switch
