/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import net.opengis.gml.GmlPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * This schema defines the DescribeCoverage operation request and reply XML elements and types, used by an OGC Web Coverage Service (WCS).
 * JDE 2003-07-24 changes:
 * 		Changed first and third "include" below to "import" so as to bring more than one namespace
 * 		Made spatialDomain a global element so as to substitute for it in GetCoverage spatialSubset
 * 
 *  JDE 2003-07-28 - Made temporalSubset of type TimeSequenceType (from owsBase.xsd, uses gml:timePosition).
 * AEW 03/07/29 Changes made:
 * 			Edited documentation of some elements and types
 * 
 * AEW 03/07/30 Changes made:
 * 			Corrrected defintion of temporalDomain element, makiing first letter of name lower case
 * 
 * AEW 03/08/01 Changes made:
 * 			Edited documentation of many types
 * 
 * JDE 03/08/27 Changes made:
 * 		 	Made DescribeCoverage/@service and DescribeCoverage/@version required
 * 			Added CoverageDescription/@version (required) and CoverageDescription/@updateSequence (optional)
 * 
 * AEW 04/07/14 Changes made:
 * 		 	Changed two "import" statement for the "wcs" namespace to  "include" statements.
 * 		 	Added "import" statement for the "gml" namespace used in gml4wcs.xsd.
 * 
 * AEW 05/07/15 Changes made:
 * 		 	Changed documentation of updateSequence attribute in CoverageDescription element
 * 
 * This schema defines the Capabilities operation request and reply XML elements and types used by an OGC Web Coverage Service (WCS). This schema with the schemas it uses are believed to be GML Application Schemas.
 * AEW 03/07/22 Changes made:			Changed element name "Section" to "section" in GetCapabilities			Added documentation elements in GetCapabilities, CapabilitiesSectionType, ContentMetadata, AbstractDescriptionBaseType, AbstractDescriptionType			Expanded documentation of WCS_CapabilitiesType			Moved documentation from Service to ServiceType			Edited documentation of OnlineResourceType, electronicMailAddress in AddressType			Expanded separator comment before ContentMetadata elements and types			Added separator comment before CoverageOfferingBrief			Moved metadataLink from AbstractDescriptionType to AbstractDescriptionBaseType
 * in CoverageOfferingBrief, replaced boundedBy with a new lonLatBoundingBox (added to owsBase.xsd); and made temporal domain of type TimeSequenceType.
 * AEW 03/07/29 Changes made:			Changed optionality and documentation os "fees" and accessConstraints" elements in ServiceType			Changed reference to gml:description to reference to (wcs:)description in AbstractDescriptionBaseType
 * AEW 03/07/30 Changes made:			Added "version" and "updateSequence" attributes to ServiceType, WCSCapabilityType, and ContentMetadata
 * JDE 03/07/30 - Added "version" and "updateSequence" attributes to WCS_Capabilities type		03/08/27 - Made "version" attribute required
 * AEW 04/07/13 Changes made:			Added declaration of the "xlink" namespace. 			In wcs:AbstractDescriptionType, added the elements wcs:description and wcs:name, and the attribute "gml:id" with use="prohibited".
 * AEW 05/07/18 Changes made:		 	Changed documentation of updateSequence attribute in GetCapabilities element,  WCS_CapabilitiesType,  ServiceType, WCSCapabilityType, and ContentMetadata element.		 	Added documentation of all enumeration values in CapabilitiesSectionType.		 	Added documentation of WCSCapabilityType, DCPTypeType, CoverageOfferingBriefType, and AbstractDescriptionType.
 * JDE 2005/08/31 Changes made:		 	AbstractDescriptionBaseType: made metadataLink repeatable
 *  This schema defines the XML elements and types expected to be used by multiple OGC Web Services (OWSs). This schema is believed to be a GML Application Schema.
 * AEW 03/07/22 Changes made:
 * 			Added [xml:lang="en"] above
 * 			Moved interpolationMethod and InterpolationMethodType here from DescribeCoverage.xsd, because I know the WCTS will also use it.
 * 			Added documentation elements in InterpolationMethodType
 *                (JDE 2003-07-28) Added LonLatBoundingBox, TimeSequence, name and description.
 * 
 * AEW 03/07/29 Changes made:
 * 			Edited documentation of "keywords" element and various other parts
 * 
 * AEW 03/07/30 Changes made:
 * 			Changed Substitution Group of lonLatEnvelope to gml:Envelope
 * 
 * JDE 03/07/30 Changes made:
 * 			Removed the Substitution Group on lonLatEnvelope (we only want to use it in the context of CoverageOfferingBrief)
 * 			Added timeResolution to TimePeriodType
 * 
 * AEW 03/08/01 Changes made:
 * 			In SupportedCRSsType, changed element name from NativeCRSs to nativeCRSs
 * 			Edited documentation of many types
 * 
 * AEW 04/07/17 Changes made:
 * 		 	Changed fived value of srsName attribute in LonLatEnvelopeBaseType to URN value in "ogc" URN namespace, based on OWS Common and OGC 05-010. Also, defined srsName attribute as being anyURI type, and explicitly marked its use as optional.
 * 		 	Modified definition of wcs:name element to be in gml:name substitutionGroup.
 * 		 	Modified definition of wcs:description element to no longer be in gml:name substitutionGroup.
 * 
 * GML profile for WCS
 * AEW 03/07/22 Changes made:
 * 			Edited documentation of "name" element
 * 
 * JDE 2003-07-28:
 * 			Added indeterminatePosition attribute to timePosition
 * 			Added 11 new elements and types from geometryBasic2d.xsd (to define Polygon, used in CoverageDescription...SpatialDomain)
 * 
 * AEW 03/07/29 Changes made:
 * 			Rearranged elements and types and inserted comments indicating the GML schema from which groups of elements and types were copied or edited into this profile
 * 
 * JDE 2003-07-30 Added TimeDurationType
 *  This schema defines the values and enumeration elements and types used by the OGC Web Coverage Service (WCS).
 * JDE 2003-07-30 Removed empty "sequence" from valueEnumType
 * AEW 2005-07-15 Edited almost all documentation elements to remove TBRs and TBDs and to make them clearer. However, I do not know enough to resolve the TBD in the documentation of the "atomic" attribute in the valueRangeType. I also added documentation elements where I thought they were needed.
 * <!-- end-model-doc -->
 * @see net.opengis.wcs10.Wcs10Factory
 * @model kind="package"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface Wcs10Package extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    String eNAME = "wcs10";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://www.opengis.net/wcs";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "wcs";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    Wcs10Package eINSTANCE = net.opengis.wcs10.impl.Wcs10PackageImpl.init();

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.AbstractDescriptionBaseTypeImpl <em>Abstract Description Base Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.AbstractDescriptionBaseTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAbstractDescriptionBaseType()
	 * @generated
	 */
    int ABSTRACT_DESCRIPTION_BASE_TYPE = 0;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_BASE_TYPE__META_DATA_PROPERTY = GmlPackage.ABSTRACT_GML_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_BASE_TYPE__DESCRIPTION = GmlPackage.ABSTRACT_GML_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_BASE_TYPE__NAME = GmlPackage.ABSTRACT_GML_TYPE__NAME;

    /**
	 * The number of structural features of the '<em>Abstract Description Base Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_BASE_TYPE_FEATURE_COUNT = GmlPackage.ABSTRACT_GML_TYPE_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.AbstractDescriptionTypeImpl <em>Abstract Description Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.AbstractDescriptionTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAbstractDescriptionType()
	 * @generated
	 */
    int ABSTRACT_DESCRIPTION_TYPE = 1;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_TYPE__META_DATA_PROPERTY = ABSTRACT_DESCRIPTION_BASE_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION = ABSTRACT_DESCRIPTION_BASE_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_TYPE__NAME = ABSTRACT_DESCRIPTION_BASE_TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Metadata Link</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK = ABSTRACT_DESCRIPTION_BASE_TYPE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Description1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1 = ABSTRACT_DESCRIPTION_BASE_TYPE_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_TYPE__NAME1 = ABSTRACT_DESCRIPTION_BASE_TYPE_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_TYPE__LABEL = ABSTRACT_DESCRIPTION_BASE_TYPE_FEATURE_COUNT + 3;

    /**
	 * The number of structural features of the '<em>Abstract Description Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT = ABSTRACT_DESCRIPTION_BASE_TYPE_FEATURE_COUNT + 4;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.AddressTypeImpl <em>Address Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.AddressTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAddressType()
	 * @generated
	 */
    int ADDRESS_TYPE = 2;

    /**
	 * The feature id for the '<em><b>Delivery Point</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ADDRESS_TYPE__DELIVERY_POINT = 0;

    /**
	 * The feature id for the '<em><b>City</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ADDRESS_TYPE__CITY = 1;

    /**
	 * The feature id for the '<em><b>Administrative Area</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ADDRESS_TYPE__ADMINISTRATIVE_AREA = 2;

    /**
	 * The feature id for the '<em><b>Postal Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ADDRESS_TYPE__POSTAL_CODE = 3;

    /**
	 * The feature id for the '<em><b>Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ADDRESS_TYPE__COUNTRY = 4;

    /**
	 * The feature id for the '<em><b>Electronic Mail Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS = 5;

    /**
	 * The number of structural features of the '<em>Address Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ADDRESS_TYPE_FEATURE_COUNT = 6;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.AxisDescriptionTypeImpl <em>Axis Description Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.AxisDescriptionTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAxisDescriptionType()
	 * @generated
	 */
    int AXIS_DESCRIPTION_TYPE = 3;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__META_DATA_PROPERTY = ABSTRACT_DESCRIPTION_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__DESCRIPTION = ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__NAME = ABSTRACT_DESCRIPTION_TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Metadata Link</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__METADATA_LINK = ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK;

    /**
	 * The feature id for the '<em><b>Description1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__DESCRIPTION1 = ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1;

    /**
	 * The feature id for the '<em><b>Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__NAME1 = ABSTRACT_DESCRIPTION_TYPE__NAME1;

    /**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__LABEL = ABSTRACT_DESCRIPTION_TYPE__LABEL;

    /**
	 * The feature id for the '<em><b>Values</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__VALUES = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Ref Sys</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__REF_SYS = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Ref Sys Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__REF_SYS_LABEL = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE__SEMANTIC = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
	 * The number of structural features of the '<em>Axis Description Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE_FEATURE_COUNT = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.AxisDescriptionType1Impl <em>Axis Description Type1</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.AxisDescriptionType1Impl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAxisDescriptionType1()
	 * @generated
	 */
    int AXIS_DESCRIPTION_TYPE1 = 4;

    /**
	 * The feature id for the '<em><b>Axis Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION = 0;

    /**
	 * The number of structural features of the '<em>Axis Description Type1</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_DESCRIPTION_TYPE1_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ValueEnumBaseTypeImpl <em>Value Enum Base Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ValueEnumBaseTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getValueEnumBaseType()
	 * @generated
	 */
    int VALUE_ENUM_BASE_TYPE = 48;

    /**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_BASE_TYPE__GROUP = 0;

    /**
	 * The feature id for the '<em><b>Interval</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_BASE_TYPE__INTERVAL = 1;

    /**
	 * The feature id for the '<em><b>Single Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_BASE_TYPE__SINGLE_VALUE = 2;

    /**
	 * The number of structural features of the '<em>Value Enum Base Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_BASE_TYPE_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.AxisSubsetTypeImpl <em>Axis Subset Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.AxisSubsetTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAxisSubsetType()
	 * @generated
	 */
    int AXIS_SUBSET_TYPE = 5;

    /**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_SUBSET_TYPE__GROUP = VALUE_ENUM_BASE_TYPE__GROUP;

    /**
	 * The feature id for the '<em><b>Interval</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_SUBSET_TYPE__INTERVAL = VALUE_ENUM_BASE_TYPE__INTERVAL;

    /**
	 * The feature id for the '<em><b>Single Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_SUBSET_TYPE__SINGLE_VALUE = VALUE_ENUM_BASE_TYPE__SINGLE_VALUE;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_SUBSET_TYPE__NAME = VALUE_ENUM_BASE_TYPE_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Axis Subset Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AXIS_SUBSET_TYPE_FEATURE_COUNT = VALUE_ENUM_BASE_TYPE_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ContactTypeImpl <em>Contact Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ContactTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getContactType()
	 * @generated
	 */
    int CONTACT_TYPE = 6;

    /**
	 * The feature id for the '<em><b>Phone</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTACT_TYPE__PHONE = 0;

    /**
	 * The feature id for the '<em><b>Address</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTACT_TYPE__ADDRESS = 1;

    /**
	 * The feature id for the '<em><b>Online Resource</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTACT_TYPE__ONLINE_RESOURCE = 2;

    /**
	 * The number of structural features of the '<em>Contact Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTACT_TYPE_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl <em>Content Metadata Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ContentMetadataTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getContentMetadataType()
	 * @generated
	 */
    int CONTENT_METADATA_TYPE = 7;

    /**
	 * The feature id for the '<em><b>Coverage Offering Brief</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF = 0;

    /**
	 * The feature id for the '<em><b>Actuate</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__ACTUATE = 1;

    /**
	 * The feature id for the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__ARCROLE = 2;

    /**
	 * The feature id for the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__HREF = 3;

    /**
	 * The feature id for the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__REMOTE_SCHEMA = 4;

    /**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__ROLE = 5;

    /**
	 * The feature id for the '<em><b>Show</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__SHOW = 6;

    /**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__TITLE = 7;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__TYPE = 8;

    /**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__UPDATE_SEQUENCE = 9;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE__VERSION = 10;

    /**
	 * The number of structural features of the '<em>Content Metadata Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CONTENT_METADATA_TYPE_FEATURE_COUNT = 11;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.CoverageDescriptionTypeImpl <em>Coverage Description Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.CoverageDescriptionTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCoverageDescriptionType()
	 * @generated
	 */
    int COVERAGE_DESCRIPTION_TYPE = 8;

    /**
	 * The feature id for the '<em><b>Coverage Offering</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_DESCRIPTION_TYPE__COVERAGE_OFFERING = 0;

    /**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_DESCRIPTION_TYPE__UPDATE_SEQUENCE = 1;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_DESCRIPTION_TYPE__VERSION = 2;

    /**
	 * The number of structural features of the '<em>Coverage Description Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_DESCRIPTION_TYPE_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.CoverageOfferingBriefTypeImpl <em>Coverage Offering Brief Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.CoverageOfferingBriefTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCoverageOfferingBriefType()
	 * @generated
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE = 9;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__META_DATA_PROPERTY = ABSTRACT_DESCRIPTION_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__DESCRIPTION = ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__NAME = ABSTRACT_DESCRIPTION_TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Metadata Link</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__METADATA_LINK = ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK;

    /**
	 * The feature id for the '<em><b>Description1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__DESCRIPTION1 = ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1;

    /**
	 * The feature id for the '<em><b>Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__NAME1 = ABSTRACT_DESCRIPTION_TYPE__NAME1;

    /**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__LABEL = ABSTRACT_DESCRIPTION_TYPE__LABEL;

    /**
	 * The feature id for the '<em><b>Lon Lat Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Coverage Offering Brief Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_BRIEF_TYPE_FEATURE_COUNT = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.CoverageOfferingTypeImpl <em>Coverage Offering Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.CoverageOfferingTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCoverageOfferingType()
	 * @generated
	 */
    int COVERAGE_OFFERING_TYPE = 10;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__META_DATA_PROPERTY = COVERAGE_OFFERING_BRIEF_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__DESCRIPTION = COVERAGE_OFFERING_BRIEF_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__NAME = COVERAGE_OFFERING_BRIEF_TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Metadata Link</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__METADATA_LINK = COVERAGE_OFFERING_BRIEF_TYPE__METADATA_LINK;

    /**
	 * The feature id for the '<em><b>Description1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__DESCRIPTION1 = COVERAGE_OFFERING_BRIEF_TYPE__DESCRIPTION1;

    /**
	 * The feature id for the '<em><b>Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__NAME1 = COVERAGE_OFFERING_BRIEF_TYPE__NAME1;

    /**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__LABEL = COVERAGE_OFFERING_BRIEF_TYPE__LABEL;

    /**
	 * The feature id for the '<em><b>Lon Lat Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__LON_LAT_ENVELOPE = COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE;

    /**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__KEYWORDS = COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS;

    /**
	 * The feature id for the '<em><b>Domain Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__DOMAIN_SET = COVERAGE_OFFERING_BRIEF_TYPE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Range Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__RANGE_SET = COVERAGE_OFFERING_BRIEF_TYPE_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Supported CR Ss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS = COVERAGE_OFFERING_BRIEF_TYPE_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Supported Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS = COVERAGE_OFFERING_BRIEF_TYPE_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Supported Interpolations</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS = COVERAGE_OFFERING_BRIEF_TYPE_FEATURE_COUNT + 4;

    /**
	 * The number of structural features of the '<em>Coverage Offering Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int COVERAGE_OFFERING_TYPE_FEATURE_COUNT = COVERAGE_OFFERING_BRIEF_TYPE_FEATURE_COUNT + 5;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.DCPTypeTypeImpl <em>DCP Type Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.DCPTypeTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDCPTypeType()
	 * @generated
	 */
    int DCP_TYPE_TYPE = 11;

    /**
	 * The feature id for the '<em><b>HTTP</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DCP_TYPE_TYPE__HTTP = 0;

    /**
	 * The number of structural features of the '<em>DCP Type Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DCP_TYPE_TYPE_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.DescribeCoverageTypeImpl <em>Describe Coverage Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.DescribeCoverageTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDescribeCoverageType()
	 * @generated
	 */
    int DESCRIBE_COVERAGE_TYPE = 12;

    /**
	 * The feature id for the '<em><b>Coverage</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DESCRIBE_COVERAGE_TYPE__COVERAGE = 0;

    /**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DESCRIBE_COVERAGE_TYPE__SERVICE = 1;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DESCRIBE_COVERAGE_TYPE__VERSION = 2;

    /**
	 * The feature id for the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_COVERAGE_TYPE__BASE_URL = 3;

				/**
	 * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_COVERAGE_TYPE__EXTENDED_PROPERTIES = 4;

				/**
	 * The number of structural features of the '<em>Describe Coverage Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DESCRIBE_COVERAGE_TYPE_FEATURE_COUNT = 5;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.DescribeCoverageType1Impl <em>Describe Coverage Type1</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.DescribeCoverageType1Impl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDescribeCoverageType1()
	 * @generated
	 */
    int DESCRIBE_COVERAGE_TYPE1 = 13;

    /**
	 * The feature id for the '<em><b>DCP Type</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DESCRIBE_COVERAGE_TYPE1__DCP_TYPE = 0;

    /**
	 * The number of structural features of the '<em>Describe Coverage Type1</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DESCRIBE_COVERAGE_TYPE1_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.DocumentRootImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDocumentRoot()
	 * @generated
	 */
    int DOCUMENT_ROOT = 14;

    /**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__MIXED = 0;

    /**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

    /**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

    /**
	 * The feature id for the '<em><b>Axis Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__AXIS_DESCRIPTION = 3;

    /**
	 * The feature id for the '<em><b>Axis Description1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__AXIS_DESCRIPTION1 = 4;

    /**
	 * The feature id for the '<em><b>Capability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__CAPABILITY = 5;

    /**
	 * The feature id for the '<em><b>Content Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__CONTENT_METADATA = 6;

    /**
	 * The feature id for the '<em><b>Coverage Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__COVERAGE_DESCRIPTION = 7;

    /**
	 * The feature id for the '<em><b>Coverage Offering</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__COVERAGE_OFFERING = 8;

    /**
	 * The feature id for the '<em><b>Coverage Offering Brief</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF = 9;

    /**
	 * The feature id for the '<em><b>Describe Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__DESCRIBE_COVERAGE = 10;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__DESCRIPTION = 11;

    /**
	 * The feature id for the '<em><b>Domain Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__DOMAIN_SET = 12;

    /**
	 * The feature id for the '<em><b>Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__FORMATS = 13;

    /**
	 * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__GET_CAPABILITIES = 14;

    /**
	 * The feature id for the '<em><b>Get Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__GET_COVERAGE = 15;

    /**
	 * The feature id for the '<em><b>Interpolation Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__INTERPOLATION_METHOD = 16;

    /**
	 * The feature id for the '<em><b>Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__INTERVAL = 17;

    /**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__KEYWORDS = 18;

    /**
	 * The feature id for the '<em><b>Lon Lat Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__LON_LAT_ENVELOPE = 19;

    /**
	 * The feature id for the '<em><b>Metadata Link</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__METADATA_LINK = 20;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__NAME = 21;

    /**
	 * The feature id for the '<em><b>Range Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__RANGE_SET = 22;

    /**
	 * The feature id for the '<em><b>Range Set1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__RANGE_SET1 = 23;

    /**
	 * The feature id for the '<em><b>Service</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__SERVICE = 24;

    /**
	 * The feature id for the '<em><b>Single Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__SINGLE_VALUE = 25;

    /**
	 * The feature id for the '<em><b>Spatial Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__SPATIAL_DOMAIN = 26;

    /**
	 * The feature id for the '<em><b>Spatial Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__SPATIAL_SUBSET = 27;

    /**
	 * The feature id for the '<em><b>Supported CR Ss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__SUPPORTED_CR_SS = 28;

    /**
	 * The feature id for the '<em><b>Supported Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__SUPPORTED_FORMATS = 29;

    /**
	 * The feature id for the '<em><b>Supported Interpolations</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS = 30;

    /**
	 * The feature id for the '<em><b>Temporal Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__TEMPORAL_DOMAIN = 31;

    /**
	 * The feature id for the '<em><b>Temporal Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__TEMPORAL_SUBSET = 32;

    /**
	 * The feature id for the '<em><b>Time Period</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__TIME_PERIOD = 33;

    /**
	 * The feature id for the '<em><b>Time Sequence</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__TIME_SEQUENCE = 34;

    /**
	 * The feature id for the '<em><b>WCS Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__WCS_CAPABILITIES = 35;

    /**
	 * The feature id for the '<em><b>Closure</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__CLOSURE = 36;

    /**
	 * The feature id for the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__SEMANTIC = 37;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT__TYPE = 38;

    /**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOCUMENT_ROOT_FEATURE_COUNT = 39;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.DomainSetTypeImpl <em>Domain Set Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.DomainSetTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDomainSetType()
	 * @generated
	 */
    int DOMAIN_SET_TYPE = 15;

    /**
	 * The feature id for the '<em><b>Spatial Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_SET_TYPE__SPATIAL_DOMAIN = 0;

    /**
	 * The feature id for the '<em><b>Temporal Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_SET_TYPE__TEMPORAL_DOMAIN = 1;

    /**
	 * The feature id for the '<em><b>Temporal Domain1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_SET_TYPE__TEMPORAL_DOMAIN1 = 2;

    /**
	 * The number of structural features of the '<em>Domain Set Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_SET_TYPE_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.DomainSubsetTypeImpl <em>Domain Subset Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.DomainSubsetTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDomainSubsetType()
	 * @generated
	 */
    int DOMAIN_SUBSET_TYPE = 16;

    /**
	 * The feature id for the '<em><b>Spatial Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET = 0;

    /**
	 * The feature id for the '<em><b>Temporal Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET = 1;

    /**
	 * The feature id for the '<em><b>Temporal Subset1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1 = 2;

    /**
	 * The number of structural features of the '<em>Domain Subset Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int DOMAIN_SUBSET_TYPE_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ExceptionTypeImpl <em>Exception Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ExceptionTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getExceptionType()
	 * @generated
	 */
    int EXCEPTION_TYPE = 17;

    /**
	 * The feature id for the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int EXCEPTION_TYPE__FORMAT = 0;

    /**
	 * The number of structural features of the '<em>Exception Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int EXCEPTION_TYPE_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.GetCapabilitiesTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetCapabilitiesType()
	 * @generated
	 */
    int GET_CAPABILITIES_TYPE = 18;

    /**
	 * The feature id for the '<em><b>Section</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_CAPABILITIES_TYPE__SECTION = 0;

    /**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_CAPABILITIES_TYPE__SERVICE = 1;

    /**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = 2;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_CAPABILITIES_TYPE__VERSION = 3;

    /**
	 * The feature id for the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__BASE_URL = 4;

				/**
	 * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES = 5;

				/**
	 * The number of structural features of the '<em>Get Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_CAPABILITIES_TYPE_FEATURE_COUNT = 6;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.GetCapabilitiesType1Impl <em>Get Capabilities Type1</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.GetCapabilitiesType1Impl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetCapabilitiesType1()
	 * @generated
	 */
    int GET_CAPABILITIES_TYPE1 = 19;

    /**
	 * The feature id for the '<em><b>DCP Type</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_CAPABILITIES_TYPE1__DCP_TYPE = 0;

    /**
	 * The number of structural features of the '<em>Get Capabilities Type1</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_CAPABILITIES_TYPE1_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.GetCoverageTypeImpl <em>Get Coverage Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.GetCoverageTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetCoverageType()
	 * @generated
	 */
    int GET_COVERAGE_TYPE = 20;

    /**
	 * The feature id for the '<em><b>Source Coverage</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE__SOURCE_COVERAGE = 0;

    /**
	 * The feature id for the '<em><b>Domain Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE__DOMAIN_SUBSET = 1;

    /**
	 * The feature id for the '<em><b>Range Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE__RANGE_SUBSET = 2;

    /**
	 * The feature id for the '<em><b>Interpolation Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE__INTERPOLATION_METHOD = 3;

    /**
	 * The feature id for the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE__OUTPUT = 4;

    /**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE__SERVICE = 5;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE__VERSION = 6;

    /**
	 * The feature id for the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__BASE_URL = 7;

				/**
	 * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__EXTENDED_PROPERTIES = 8;

				/**
	 * The number of structural features of the '<em>Get Coverage Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE_FEATURE_COUNT = 9;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.GetCoverageType1Impl <em>Get Coverage Type1</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.GetCoverageType1Impl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetCoverageType1()
	 * @generated
	 */
    int GET_COVERAGE_TYPE1 = 21;

    /**
	 * The feature id for the '<em><b>DCP Type</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE1__DCP_TYPE = 0;

    /**
	 * The number of structural features of the '<em>Get Coverage Type1</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_COVERAGE_TYPE1_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.GetTypeImpl <em>Get Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.GetTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetType()
	 * @generated
	 */
    int GET_TYPE = 22;

    /**
	 * The feature id for the '<em><b>Online Resource</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_TYPE__ONLINE_RESOURCE = 0;

    /**
	 * The number of structural features of the '<em>Get Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int GET_TYPE_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.HTTPTypeImpl <em>HTTP Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.HTTPTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getHTTPType()
	 * @generated
	 */
    int HTTP_TYPE = 23;

    /**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int HTTP_TYPE__GROUP = 0;

    /**
	 * The feature id for the '<em><b>Get</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int HTTP_TYPE__GET = 1;

    /**
	 * The feature id for the '<em><b>Post</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int HTTP_TYPE__POST = 2;

    /**
	 * The number of structural features of the '<em>HTTP Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int HTTP_TYPE_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ValueRangeTypeImpl <em>Value Range Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ValueRangeTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getValueRangeType()
	 * @generated
	 */
    int VALUE_RANGE_TYPE = 50;

    /**
	 * The feature id for the '<em><b>Min</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_RANGE_TYPE__MIN = 0;

    /**
	 * The feature id for the '<em><b>Max</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_RANGE_TYPE__MAX = 1;

    /**
	 * The feature id for the '<em><b>Atomic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_RANGE_TYPE__ATOMIC = 2;

    /**
	 * The feature id for the '<em><b>Closure</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_RANGE_TYPE__CLOSURE = 3;

    /**
	 * The feature id for the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_RANGE_TYPE__SEMANTIC = 4;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_RANGE_TYPE__TYPE = 5;

    /**
	 * The number of structural features of the '<em>Value Range Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_RANGE_TYPE_FEATURE_COUNT = 6;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.IntervalTypeImpl <em>Interval Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.IntervalTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getIntervalType()
	 * @generated
	 */
    int INTERVAL_TYPE = 24;

    /**
	 * The feature id for the '<em><b>Min</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERVAL_TYPE__MIN = VALUE_RANGE_TYPE__MIN;

    /**
	 * The feature id for the '<em><b>Max</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERVAL_TYPE__MAX = VALUE_RANGE_TYPE__MAX;

    /**
	 * The feature id for the '<em><b>Atomic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERVAL_TYPE__ATOMIC = VALUE_RANGE_TYPE__ATOMIC;

    /**
	 * The feature id for the '<em><b>Closure</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERVAL_TYPE__CLOSURE = VALUE_RANGE_TYPE__CLOSURE;

    /**
	 * The feature id for the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERVAL_TYPE__SEMANTIC = VALUE_RANGE_TYPE__SEMANTIC;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERVAL_TYPE__TYPE = VALUE_RANGE_TYPE__TYPE;

    /**
	 * The feature id for the '<em><b>Res</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERVAL_TYPE__RES = VALUE_RANGE_TYPE_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Interval Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERVAL_TYPE_FEATURE_COUNT = VALUE_RANGE_TYPE_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.KeywordsTypeImpl <em>Keywords Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.KeywordsTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getKeywordsType()
	 * @generated
	 */
    int KEYWORDS_TYPE = 25;

    /**
	 * The feature id for the '<em><b>Keyword</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int KEYWORDS_TYPE__KEYWORD = 0;

    /**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int KEYWORDS_TYPE__TYPE = 1;

    /**
	 * The number of structural features of the '<em>Keywords Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int KEYWORDS_TYPE_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.LonLatEnvelopeBaseTypeImpl <em>Lon Lat Envelope Base Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.LonLatEnvelopeBaseTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getLonLatEnvelopeBaseType()
	 * @generated
	 */
    int LON_LAT_ENVELOPE_BASE_TYPE = 26;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_BASE_TYPE__META_DATA_PROPERTY = GmlPackage.ENVELOPE_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_BASE_TYPE__DESCRIPTION = GmlPackage.ENVELOPE_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_BASE_TYPE__NAME = GmlPackage.ENVELOPE_TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_BASE_TYPE__SRS_NAME = GmlPackage.ENVELOPE_TYPE__SRS_NAME;

    /**
	 * The feature id for the '<em><b>Pos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_BASE_TYPE__POS = GmlPackage.ENVELOPE_TYPE__POS;

    /**
	 * The number of structural features of the '<em>Lon Lat Envelope Base Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_BASE_TYPE_FEATURE_COUNT = GmlPackage.ENVELOPE_TYPE_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.LonLatEnvelopeTypeImpl <em>Lon Lat Envelope Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.LonLatEnvelopeTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getLonLatEnvelopeType()
	 * @generated
	 */
    int LON_LAT_ENVELOPE_TYPE = 27;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_TYPE__META_DATA_PROPERTY = LON_LAT_ENVELOPE_BASE_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_TYPE__DESCRIPTION = LON_LAT_ENVELOPE_BASE_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_TYPE__NAME = LON_LAT_ENVELOPE_BASE_TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_TYPE__SRS_NAME = LON_LAT_ENVELOPE_BASE_TYPE__SRS_NAME;

    /**
	 * The feature id for the '<em><b>Pos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_TYPE__POS = LON_LAT_ENVELOPE_BASE_TYPE__POS;

    /**
	 * The feature id for the '<em><b>Time Position</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_TYPE__TIME_POSITION = LON_LAT_ENVELOPE_BASE_TYPE_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Lon Lat Envelope Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int LON_LAT_ENVELOPE_TYPE_FEATURE_COUNT = LON_LAT_ENVELOPE_BASE_TYPE_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.MetadataAssociationTypeImpl <em>Metadata Association Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.MetadataAssociationTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMetadataAssociationType()
	 * @generated
	 */
    int METADATA_ASSOCIATION_TYPE = 28;

    /**
	 * The feature id for the '<em><b>Meta Data Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__META_DATA_GROUP = GmlPackage.META_DATA_PROPERTY_TYPE__META_DATA_GROUP;

    /**
	 * The feature id for the '<em><b>Meta Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__META_DATA = GmlPackage.META_DATA_PROPERTY_TYPE__META_DATA;

    /**
	 * The feature id for the '<em><b>About</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__ABOUT = GmlPackage.META_DATA_PROPERTY_TYPE__ABOUT;

    /**
	 * The feature id for the '<em><b>Actuate</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__ACTUATE = GmlPackage.META_DATA_PROPERTY_TYPE__ACTUATE;

    /**
	 * The feature id for the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__ARCROLE = GmlPackage.META_DATA_PROPERTY_TYPE__ARCROLE;

    /**
	 * The feature id for the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__HREF = GmlPackage.META_DATA_PROPERTY_TYPE__HREF;

    /**
	 * The feature id for the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__REMOTE_SCHEMA = GmlPackage.META_DATA_PROPERTY_TYPE__REMOTE_SCHEMA;

    /**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__ROLE = GmlPackage.META_DATA_PROPERTY_TYPE__ROLE;

    /**
	 * The feature id for the '<em><b>Show</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__SHOW = GmlPackage.META_DATA_PROPERTY_TYPE__SHOW;

    /**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__TITLE = GmlPackage.META_DATA_PROPERTY_TYPE__TITLE;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE__TYPE = GmlPackage.META_DATA_PROPERTY_TYPE__TYPE;

    /**
	 * The number of structural features of the '<em>Metadata Association Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_ASSOCIATION_TYPE_FEATURE_COUNT = GmlPackage.META_DATA_PROPERTY_TYPE_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.MetadataLinkTypeImpl <em>Metadata Link Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.MetadataLinkTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMetadataLinkType()
	 * @generated
	 */
    int METADATA_LINK_TYPE = 29;

    /**
	 * The feature id for the '<em><b>Meta Data Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__META_DATA_GROUP = METADATA_ASSOCIATION_TYPE__META_DATA_GROUP;

    /**
	 * The feature id for the '<em><b>Meta Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__META_DATA = METADATA_ASSOCIATION_TYPE__META_DATA;

    /**
	 * The feature id for the '<em><b>About</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__ABOUT = METADATA_ASSOCIATION_TYPE__ABOUT;

    /**
	 * The feature id for the '<em><b>Actuate</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__ACTUATE = METADATA_ASSOCIATION_TYPE__ACTUATE;

    /**
	 * The feature id for the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__ARCROLE = METADATA_ASSOCIATION_TYPE__ARCROLE;

    /**
	 * The feature id for the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__HREF = METADATA_ASSOCIATION_TYPE__HREF;

    /**
	 * The feature id for the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__REMOTE_SCHEMA = METADATA_ASSOCIATION_TYPE__REMOTE_SCHEMA;

    /**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__ROLE = METADATA_ASSOCIATION_TYPE__ROLE;

    /**
	 * The feature id for the '<em><b>Show</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__SHOW = METADATA_ASSOCIATION_TYPE__SHOW;

    /**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__TITLE = METADATA_ASSOCIATION_TYPE__TITLE;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__TYPE = METADATA_ASSOCIATION_TYPE__TYPE;

    /**
	 * The feature id for the '<em><b>Metadata Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE__METADATA_TYPE = METADATA_ASSOCIATION_TYPE_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Metadata Link Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METADATA_LINK_TYPE_FEATURE_COUNT = METADATA_ASSOCIATION_TYPE_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.OnlineResourceTypeImpl <em>Online Resource Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.OnlineResourceTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getOnlineResourceType()
	 * @generated
	 */
    int ONLINE_RESOURCE_TYPE = 30;

    /**
	 * The feature id for the '<em><b>Actuate</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ONLINE_RESOURCE_TYPE__ACTUATE = 0;

    /**
	 * The feature id for the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ONLINE_RESOURCE_TYPE__ARCROLE = 1;

    /**
	 * The feature id for the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ONLINE_RESOURCE_TYPE__HREF = 2;

    /**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ONLINE_RESOURCE_TYPE__ROLE = 3;

    /**
	 * The feature id for the '<em><b>Show</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ONLINE_RESOURCE_TYPE__SHOW = 4;

    /**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ONLINE_RESOURCE_TYPE__TITLE = 5;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ONLINE_RESOURCE_TYPE__TYPE = 6;

    /**
	 * The number of structural features of the '<em>Online Resource Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ONLINE_RESOURCE_TYPE_FEATURE_COUNT = 7;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.OutputTypeImpl <em>Output Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.OutputTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getOutputType()
	 * @generated
	 */
    int OUTPUT_TYPE = 31;

    /**
	 * The feature id for the '<em><b>Crs</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OUTPUT_TYPE__CRS = 0;

    /**
	 * The feature id for the '<em><b>Format</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OUTPUT_TYPE__FORMAT = 1;

    /**
	 * The number of structural features of the '<em>Output Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OUTPUT_TYPE_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.PostTypeImpl <em>Post Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.PostTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getPostType()
	 * @generated
	 */
    int POST_TYPE = 32;

    /**
	 * The feature id for the '<em><b>Online Resource</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int POST_TYPE__ONLINE_RESOURCE = 0;

    /**
	 * The number of structural features of the '<em>Post Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int POST_TYPE_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.RangeSetTypeImpl <em>Range Set Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.RangeSetTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getRangeSetType()
	 * @generated
	 */
    int RANGE_SET_TYPE = 33;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__META_DATA_PROPERTY = ABSTRACT_DESCRIPTION_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__DESCRIPTION = ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__NAME = ABSTRACT_DESCRIPTION_TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Metadata Link</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__METADATA_LINK = ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK;

    /**
	 * The feature id for the '<em><b>Description1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__DESCRIPTION1 = ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1;

    /**
	 * The feature id for the '<em><b>Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__NAME1 = ABSTRACT_DESCRIPTION_TYPE__NAME1;

    /**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__LABEL = ABSTRACT_DESCRIPTION_TYPE__LABEL;

    /**
	 * The feature id for the '<em><b>Axis Description</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__AXIS_DESCRIPTION = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Null Values</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__NULL_VALUES = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Ref Sys</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__REF_SYS = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Ref Sys Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__REF_SYS_LABEL = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE__SEMANTIC = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
	 * The number of structural features of the '<em>Range Set Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE_FEATURE_COUNT = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.RangeSetType1Impl <em>Range Set Type1</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.RangeSetType1Impl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getRangeSetType1()
	 * @generated
	 */
    int RANGE_SET_TYPE1 = 34;

    /**
	 * The feature id for the '<em><b>Range Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE1__RANGE_SET = 0;

    /**
	 * The number of structural features of the '<em>Range Set Type1</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SET_TYPE1_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.RangeSubsetTypeImpl <em>Range Subset Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.RangeSubsetTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getRangeSubsetType()
	 * @generated
	 */
    int RANGE_SUBSET_TYPE = 35;

    /**
	 * The feature id for the '<em><b>Axis Subset</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SUBSET_TYPE__AXIS_SUBSET = 0;

    /**
	 * The number of structural features of the '<em>Range Subset Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RANGE_SUBSET_TYPE_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.RequestTypeImpl <em>Request Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.RequestTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getRequestType()
	 * @generated
	 */
    int REQUEST_TYPE = 36;

    /**
	 * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int REQUEST_TYPE__GET_CAPABILITIES = 0;

    /**
	 * The feature id for the '<em><b>Describe Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int REQUEST_TYPE__DESCRIBE_COVERAGE = 1;

    /**
	 * The feature id for the '<em><b>Get Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int REQUEST_TYPE__GET_COVERAGE = 2;

    /**
	 * The number of structural features of the '<em>Request Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int REQUEST_TYPE_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ResponsiblePartyTypeImpl <em>Responsible Party Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ResponsiblePartyTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getResponsiblePartyType()
	 * @generated
	 */
    int RESPONSIBLE_PARTY_TYPE = 37;

    /**
	 * The feature id for the '<em><b>Individual Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME = 0;

    /**
	 * The feature id for the '<em><b>Organisation Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME = 1;

    /**
	 * The feature id for the '<em><b>Organisation Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME1 = 2;

    /**
	 * The feature id for the '<em><b>Position Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RESPONSIBLE_PARTY_TYPE__POSITION_NAME = 3;

    /**
	 * The feature id for the '<em><b>Contact Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RESPONSIBLE_PARTY_TYPE__CONTACT_INFO = 4;

    /**
	 * The number of structural features of the '<em>Responsible Party Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int RESPONSIBLE_PARTY_TYPE_FEATURE_COUNT = 5;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ServiceTypeImpl <em>Service Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ServiceTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getServiceType()
	 * @generated
	 */
    int SERVICE_TYPE = 38;

    /**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__META_DATA_PROPERTY = ABSTRACT_DESCRIPTION_TYPE__META_DATA_PROPERTY;

    /**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__DESCRIPTION = ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__NAME = ABSTRACT_DESCRIPTION_TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Metadata Link</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__METADATA_LINK = ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK;

    /**
	 * The feature id for the '<em><b>Description1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__DESCRIPTION1 = ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1;

    /**
	 * The feature id for the '<em><b>Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__NAME1 = ABSTRACT_DESCRIPTION_TYPE__NAME1;

    /**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__LABEL = ABSTRACT_DESCRIPTION_TYPE__LABEL;

    /**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__KEYWORDS = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Responsible Party</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__RESPONSIBLE_PARTY = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Fees</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__FEES = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Access Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__ACCESS_CONSTRAINTS = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__UPDATE_SEQUENCE = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE__VERSION = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
	 * The number of structural features of the '<em>Service Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SERVICE_TYPE_FEATURE_COUNT = ABSTRACT_DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.SpatialDomainTypeImpl <em>Spatial Domain Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.SpatialDomainTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSpatialDomainType()
	 * @generated
	 */
    int SPATIAL_DOMAIN_TYPE = 39;

    /**
	 * The feature id for the '<em><b>Envelope</b></em>' reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_DOMAIN_TYPE__ENVELOPE = 0;

    /**
	 * The feature id for the '<em><b>Grid Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_DOMAIN_TYPE__GRID_GROUP = 1;

    /**
	 * The feature id for the '<em><b>Grid</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_DOMAIN_TYPE__GRID = 2;

    /**
	 * The feature id for the '<em><b>Polygon</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_DOMAIN_TYPE__POLYGON = 3;

    /**
	 * The number of structural features of the '<em>Spatial Domain Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_DOMAIN_TYPE_FEATURE_COUNT = 4;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.SpatialSubsetTypeImpl <em>Spatial Subset Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.SpatialSubsetTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSpatialSubsetType()
	 * @generated
	 */
    int SPATIAL_SUBSET_TYPE = 40;

    /**
	 * The feature id for the '<em><b>Envelope</b></em>' reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_SUBSET_TYPE__ENVELOPE = SPATIAL_DOMAIN_TYPE__ENVELOPE;

    /**
	 * The feature id for the '<em><b>Grid Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_SUBSET_TYPE__GRID_GROUP = SPATIAL_DOMAIN_TYPE__GRID_GROUP;

    /**
	 * The feature id for the '<em><b>Grid</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_SUBSET_TYPE__GRID = SPATIAL_DOMAIN_TYPE__GRID;

    /**
	 * The feature id for the '<em><b>Polygon</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_SUBSET_TYPE__POLYGON = SPATIAL_DOMAIN_TYPE__POLYGON;

    /**
	 * The number of structural features of the '<em>Spatial Subset Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SPATIAL_SUBSET_TYPE_FEATURE_COUNT = SPATIAL_DOMAIN_TYPE_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.SupportedCRSsTypeImpl <em>Supported CR Ss Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.SupportedCRSsTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSupportedCRSsType()
	 * @generated
	 */
    int SUPPORTED_CR_SS_TYPE = 41;

    /**
	 * The feature id for the '<em><b>Request Response CR Ss</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS = 0;

    /**
	 * The feature id for the '<em><b>Request CR Ss</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS = 1;

    /**
	 * The feature id for the '<em><b>Response CR Ss</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS = 2;

    /**
	 * The feature id for the '<em><b>Native CR Ss</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS = 3;

    /**
	 * The number of structural features of the '<em>Supported CR Ss Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_CR_SS_TYPE_FEATURE_COUNT = 4;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.SupportedFormatsTypeImpl <em>Supported Formats Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.SupportedFormatsTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSupportedFormatsType()
	 * @generated
	 */
    int SUPPORTED_FORMATS_TYPE = 42;

    /**
	 * The feature id for the '<em><b>Formats</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_FORMATS_TYPE__FORMATS = 0;

    /**
	 * The feature id for the '<em><b>Native Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_FORMATS_TYPE__NATIVE_FORMAT = 1;

    /**
	 * The number of structural features of the '<em>Supported Formats Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_FORMATS_TYPE_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.SupportedInterpolationsTypeImpl <em>Supported Interpolations Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.SupportedInterpolationsTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSupportedInterpolationsType()
	 * @generated
	 */
    int SUPPORTED_INTERPOLATIONS_TYPE = 43;

    /**
	 * The feature id for the '<em><b>Interpolation Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_INTERPOLATIONS_TYPE__INTERPOLATION_METHOD = 0;

    /**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT = 1;

    /**
	 * The number of structural features of the '<em>Supported Interpolations Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SUPPORTED_INTERPOLATIONS_TYPE_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.TelephoneTypeImpl <em>Telephone Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.TelephoneTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getTelephoneType()
	 * @generated
	 */
    int TELEPHONE_TYPE = 44;

    /**
	 * The feature id for the '<em><b>Voice</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TELEPHONE_TYPE__VOICE = 0;

    /**
	 * The feature id for the '<em><b>Facsimile</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TELEPHONE_TYPE__FACSIMILE = 1;

    /**
	 * The number of structural features of the '<em>Telephone Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TELEPHONE_TYPE_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.TimePeriodTypeImpl <em>Time Period Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.TimePeriodTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getTimePeriodType()
	 * @generated
	 */
    int TIME_PERIOD_TYPE = 45;

    /**
	 * The feature id for the '<em><b>Begin Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_PERIOD_TYPE__BEGIN_POSITION = 0;

    /**
	 * The feature id for the '<em><b>End Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_PERIOD_TYPE__END_POSITION = 1;

    /**
	 * The feature id for the '<em><b>Time Resolution</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_PERIOD_TYPE__TIME_RESOLUTION = 2;

    /**
	 * The feature id for the '<em><b>Frame</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_PERIOD_TYPE__FRAME = 3;

    /**
	 * The number of structural features of the '<em>Time Period Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_PERIOD_TYPE_FEATURE_COUNT = 4;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.TimeSequenceTypeImpl <em>Time Sequence Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.TimeSequenceTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getTimeSequenceType()
	 * @generated
	 */
    int TIME_SEQUENCE_TYPE = 46;

    /**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_SEQUENCE_TYPE__GROUP = 0;

    /**
	 * The feature id for the '<em><b>Time Position</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_SEQUENCE_TYPE__TIME_POSITION = 1;

    /**
	 * The feature id for the '<em><b>Time Period</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_SEQUENCE_TYPE__TIME_PERIOD = 2;

    /**
	 * The number of structural features of the '<em>Time Sequence Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TIME_SEQUENCE_TYPE_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.TypedLiteralTypeImpl <em>Typed Literal Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.TypedLiteralTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getTypedLiteralType()
	 * @generated
	 */
    int TYPED_LITERAL_TYPE = 47;

    /**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TYPED_LITERAL_TYPE__VALUE = 0;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TYPED_LITERAL_TYPE__TYPE = 1;

    /**
	 * The number of structural features of the '<em>Typed Literal Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TYPED_LITERAL_TYPE_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ValueEnumTypeImpl <em>Value Enum Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ValueEnumTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getValueEnumType()
	 * @generated
	 */
    int VALUE_ENUM_TYPE = 49;

    /**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_TYPE__GROUP = VALUE_ENUM_BASE_TYPE__GROUP;

    /**
	 * The feature id for the '<em><b>Interval</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_TYPE__INTERVAL = VALUE_ENUM_BASE_TYPE__INTERVAL;

    /**
	 * The feature id for the '<em><b>Single Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_TYPE__SINGLE_VALUE = VALUE_ENUM_BASE_TYPE__SINGLE_VALUE;

    /**
	 * The feature id for the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_TYPE__SEMANTIC = VALUE_ENUM_BASE_TYPE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_TYPE__TYPE = VALUE_ENUM_BASE_TYPE_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Value Enum Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUE_ENUM_TYPE_FEATURE_COUNT = VALUE_ENUM_BASE_TYPE_FEATURE_COUNT + 2;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.ValuesTypeImpl <em>Values Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.ValuesTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getValuesType()
	 * @generated
	 */
    int VALUES_TYPE = 51;

    /**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUES_TYPE__GROUP = VALUE_ENUM_TYPE__GROUP;

    /**
	 * The feature id for the '<em><b>Interval</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUES_TYPE__INTERVAL = VALUE_ENUM_TYPE__INTERVAL;

    /**
	 * The feature id for the '<em><b>Single Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUES_TYPE__SINGLE_VALUE = VALUE_ENUM_TYPE__SINGLE_VALUE;

    /**
	 * The feature id for the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUES_TYPE__SEMANTIC = VALUE_ENUM_TYPE__SEMANTIC;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUES_TYPE__TYPE = VALUE_ENUM_TYPE__TYPE;

    /**
	 * The feature id for the '<em><b>Default</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUES_TYPE__DEFAULT = VALUE_ENUM_TYPE_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Values Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VALUES_TYPE_FEATURE_COUNT = VALUE_ENUM_TYPE_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.VendorSpecificCapabilitiesTypeImpl <em>Vendor Specific Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.VendorSpecificCapabilitiesTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getVendorSpecificCapabilitiesType()
	 * @generated
	 */
    int VENDOR_SPECIFIC_CAPABILITIES_TYPE = 52;

    /**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VENDOR_SPECIFIC_CAPABILITIES_TYPE__ANY = 0;

    /**
	 * The number of structural features of the '<em>Vendor Specific Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int VENDOR_SPECIFIC_CAPABILITIES_TYPE_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl <em>WCS Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getWCSCapabilitiesType()
	 * @generated
	 */
    int WCS_CAPABILITIES_TYPE = 53;

    /**
	 * The feature id for the '<em><b>Service</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITIES_TYPE__SERVICE = 0;

    /**
	 * The feature id for the '<em><b>Capability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITIES_TYPE__CAPABILITY = 1;

    /**
	 * The feature id for the '<em><b>Content Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITIES_TYPE__CONTENT_METADATA = 2;

    /**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITIES_TYPE__UPDATE_SEQUENCE = 3;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITIES_TYPE__VERSION = 4;

    /**
	 * The number of structural features of the '<em>WCS Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITIES_TYPE_FEATURE_COUNT = 5;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.impl.WCSCapabilityTypeImpl <em>WCS Capability Type</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.impl.WCSCapabilityTypeImpl
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getWCSCapabilityType()
	 * @generated
	 */
    int WCS_CAPABILITY_TYPE = 54;

    /**
	 * The feature id for the '<em><b>Request</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITY_TYPE__REQUEST = 0;

    /**
	 * The feature id for the '<em><b>Exception</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITY_TYPE__EXCEPTION = 1;

    /**
	 * The feature id for the '<em><b>Vendor Specific Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES = 2;

    /**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITY_TYPE__UPDATE_SEQUENCE = 3;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITY_TYPE__VERSION = 4;

    /**
	 * The number of structural features of the '<em>WCS Capability Type</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WCS_CAPABILITY_TYPE_FEATURE_COUNT = 5;

    /**
	 * The meta object id for the '{@link com.vividsolutions.jts.geom.Envelope <em>Envelope</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.vividsolutions.jts.geom.Envelope
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getEnvelope()
	 * @generated
	 */
	int ENVELOPE = 55;

				/**
	 * The number of structural features of the '<em>Envelope</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_FEATURE_COUNT = 0;

				/**
	 * The meta object id for the '{@link org.geotools.geometry.GeneralEnvelope <em>General Envelope</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.geotools.geometry.GeneralEnvelope
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGeneralEnvelope()
	 * @generated
	 */
	int GENERAL_ENVELOPE = 56;

				/**
	 * The number of structural features of the '<em>General Envelope</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_ENVELOPE_FEATURE_COUNT = 0;

				/**
	 * The meta object id for the '{@link net.opengis.wcs10.CapabilitiesSectionType <em>Capabilities Section Type</em>}' enum.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.CapabilitiesSectionType
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCapabilitiesSectionType()
	 * @generated
	 */
    int CAPABILITIES_SECTION_TYPE = 57;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.ClosureType <em>Closure Type</em>}' enum.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.ClosureType
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getClosureType()
	 * @generated
	 */
    int CLOSURE_TYPE = 58;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.InterpolationMethodType <em>Interpolation Method Type</em>}' enum.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getInterpolationMethodType()
	 * @generated
	 */
    int INTERPOLATION_METHOD_TYPE = 59;

    /**
	 * The meta object id for the '{@link net.opengis.wcs10.MetadataTypeType <em>Metadata Type Type</em>}' enum.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.MetadataTypeType
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMetadataTypeType()
	 * @generated
	 */
    int METADATA_TYPE_TYPE = 60;

    /**
	 * The meta object id for the '<em>Capabilities Section Type Object</em>' data type.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.CapabilitiesSectionType
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCapabilitiesSectionTypeObject()
	 * @generated
	 */
    int CAPABILITIES_SECTION_TYPE_OBJECT = 61;

    /**
	 * The meta object id for the '<em>Closure Type Object</em>' data type.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.ClosureType
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getClosureTypeObject()
	 * @generated
	 */
    int CLOSURE_TYPE_OBJECT = 62;

    /**
	 * The meta object id for the '<em>Interpolation Method Type Object</em>' data type.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getInterpolationMethodTypeObject()
	 * @generated
	 */
    int INTERPOLATION_METHOD_TYPE_OBJECT = 63;

    /**
	 * The meta object id for the '<em>Metadata Type Type Object</em>' data type.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see net.opengis.wcs10.MetadataTypeType
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMetadataTypeTypeObject()
	 * @generated
	 */
    int METADATA_TYPE_TYPE_OBJECT = 64;


    /**
	 * The meta object id for the '<em>Map</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.Map
	 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMap()
	 * @generated
	 */
	int MAP = 65;


				/**
	 * Returns the meta object for class '{@link net.opengis.wcs10.AbstractDescriptionBaseType <em>Abstract Description Base Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Description Base Type</em>'.
	 * @see net.opengis.wcs10.AbstractDescriptionBaseType
	 * @generated
	 */
    EClass getAbstractDescriptionBaseType();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.AbstractDescriptionType <em>Abstract Description Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Description Type</em>'.
	 * @see net.opengis.wcs10.AbstractDescriptionType
	 * @generated
	 */
    EClass getAbstractDescriptionType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.AbstractDescriptionType#getMetadataLink <em>Metadata Link</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Metadata Link</em>'.
	 * @see net.opengis.wcs10.AbstractDescriptionType#getMetadataLink()
	 * @see #getAbstractDescriptionType()
	 * @generated
	 */
    EReference getAbstractDescriptionType_MetadataLink();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AbstractDescriptionType#getDescription1 <em>Description1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description1</em>'.
	 * @see net.opengis.wcs10.AbstractDescriptionType#getDescription1()
	 * @see #getAbstractDescriptionType()
	 * @generated
	 */
    EAttribute getAbstractDescriptionType_Description1();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AbstractDescriptionType#getName1 <em>Name1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name1</em>'.
	 * @see net.opengis.wcs10.AbstractDescriptionType#getName1()
	 * @see #getAbstractDescriptionType()
	 * @generated
	 */
    EAttribute getAbstractDescriptionType_Name1();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AbstractDescriptionType#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see net.opengis.wcs10.AbstractDescriptionType#getLabel()
	 * @see #getAbstractDescriptionType()
	 * @generated
	 */
    EAttribute getAbstractDescriptionType_Label();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.AddressType <em>Address Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Address Type</em>'.
	 * @see net.opengis.wcs10.AddressType
	 * @generated
	 */
    EClass getAddressType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AddressType#getDeliveryPoint <em>Delivery Point</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delivery Point</em>'.
	 * @see net.opengis.wcs10.AddressType#getDeliveryPoint()
	 * @see #getAddressType()
	 * @generated
	 */
    EAttribute getAddressType_DeliveryPoint();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AddressType#getCity <em>City</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>City</em>'.
	 * @see net.opengis.wcs10.AddressType#getCity()
	 * @see #getAddressType()
	 * @generated
	 */
    EAttribute getAddressType_City();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AddressType#getAdministrativeArea <em>Administrative Area</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Administrative Area</em>'.
	 * @see net.opengis.wcs10.AddressType#getAdministrativeArea()
	 * @see #getAddressType()
	 * @generated
	 */
    EAttribute getAddressType_AdministrativeArea();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AddressType#getPostalCode <em>Postal Code</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Postal Code</em>'.
	 * @see net.opengis.wcs10.AddressType#getPostalCode()
	 * @see #getAddressType()
	 * @generated
	 */
    EAttribute getAddressType_PostalCode();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AddressType#getCountry <em>Country</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Country</em>'.
	 * @see net.opengis.wcs10.AddressType#getCountry()
	 * @see #getAddressType()
	 * @generated
	 */
    EAttribute getAddressType_Country();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AddressType#getElectronicMailAddress <em>Electronic Mail Address</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Electronic Mail Address</em>'.
	 * @see net.opengis.wcs10.AddressType#getElectronicMailAddress()
	 * @see #getAddressType()
	 * @generated
	 */
    EAttribute getAddressType_ElectronicMailAddress();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.AxisDescriptionType <em>Axis Description Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Axis Description Type</em>'.
	 * @see net.opengis.wcs10.AxisDescriptionType
	 * @generated
	 */
    EClass getAxisDescriptionType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.AxisDescriptionType#getValues <em>Values</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Values</em>'.
	 * @see net.opengis.wcs10.AxisDescriptionType#getValues()
	 * @see #getAxisDescriptionType()
	 * @generated
	 */
    EReference getAxisDescriptionType_Values();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AxisDescriptionType#getRefSys <em>Ref Sys</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ref Sys</em>'.
	 * @see net.opengis.wcs10.AxisDescriptionType#getRefSys()
	 * @see #getAxisDescriptionType()
	 * @generated
	 */
    EAttribute getAxisDescriptionType_RefSys();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AxisDescriptionType#getRefSysLabel <em>Ref Sys Label</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ref Sys Label</em>'.
	 * @see net.opengis.wcs10.AxisDescriptionType#getRefSysLabel()
	 * @see #getAxisDescriptionType()
	 * @generated
	 */
    EAttribute getAxisDescriptionType_RefSysLabel();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AxisDescriptionType#getSemantic <em>Semantic</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Semantic</em>'.
	 * @see net.opengis.wcs10.AxisDescriptionType#getSemantic()
	 * @see #getAxisDescriptionType()
	 * @generated
	 */
    EAttribute getAxisDescriptionType_Semantic();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.AxisDescriptionType1 <em>Axis Description Type1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Axis Description Type1</em>'.
	 * @see net.opengis.wcs10.AxisDescriptionType1
	 * @generated
	 */
    EClass getAxisDescriptionType1();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.AxisDescriptionType1#getAxisDescription <em>Axis Description</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Axis Description</em>'.
	 * @see net.opengis.wcs10.AxisDescriptionType1#getAxisDescription()
	 * @see #getAxisDescriptionType1()
	 * @generated
	 */
    EReference getAxisDescriptionType1_AxisDescription();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.AxisSubsetType <em>Axis Subset Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Axis Subset Type</em>'.
	 * @see net.opengis.wcs10.AxisSubsetType
	 * @generated
	 */
    EClass getAxisSubsetType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.AxisSubsetType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.opengis.wcs10.AxisSubsetType#getName()
	 * @see #getAxisSubsetType()
	 * @generated
	 */
    EAttribute getAxisSubsetType_Name();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ContactType <em>Contact Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contact Type</em>'.
	 * @see net.opengis.wcs10.ContactType
	 * @generated
	 */
    EClass getContactType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ContactType#getPhone <em>Phone</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Phone</em>'.
	 * @see net.opengis.wcs10.ContactType#getPhone()
	 * @see #getContactType()
	 * @generated
	 */
    EReference getContactType_Phone();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ContactType#getAddress <em>Address</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Address</em>'.
	 * @see net.opengis.wcs10.ContactType#getAddress()
	 * @see #getContactType()
	 * @generated
	 */
    EReference getContactType_Address();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ContactType#getOnlineResource <em>Online Resource</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Online Resource</em>'.
	 * @see net.opengis.wcs10.ContactType#getOnlineResource()
	 * @see #getContactType()
	 * @generated
	 */
    EReference getContactType_OnlineResource();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ContentMetadataType <em>Content Metadata Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Content Metadata Type</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType
	 * @generated
	 */
    EClass getContentMetadataType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.ContentMetadataType#getCoverageOfferingBrief <em>Coverage Offering Brief</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Coverage Offering Brief</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getCoverageOfferingBrief()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EReference getContentMetadataType_CoverageOfferingBrief();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getActuate <em>Actuate</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Actuate</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getActuate()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_Actuate();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getArcrole <em>Arcrole</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arcrole</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getArcrole()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_Arcrole();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getHref <em>Href</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Href</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getHref()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_Href();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getRemoteSchema <em>Remote Schema</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote Schema</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getRemoteSchema()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_RemoteSchema();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getRole <em>Role</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Role</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getRole()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_Role();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getShow <em>Show</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getShow()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_Show();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getTitle()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_Title();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getType()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_Type();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getUpdateSequence <em>Update Sequence</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update Sequence</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getUpdateSequence()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_UpdateSequence();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ContentMetadataType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs10.ContentMetadataType#getVersion()
	 * @see #getContentMetadataType()
	 * @generated
	 */
    EAttribute getContentMetadataType_Version();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.CoverageDescriptionType <em>Coverage Description Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coverage Description Type</em>'.
	 * @see net.opengis.wcs10.CoverageDescriptionType
	 * @generated
	 */
    EClass getCoverageDescriptionType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.CoverageDescriptionType#getCoverageOffering <em>Coverage Offering</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Coverage Offering</em>'.
	 * @see net.opengis.wcs10.CoverageDescriptionType#getCoverageOffering()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
    EReference getCoverageDescriptionType_CoverageOffering();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.CoverageDescriptionType#getUpdateSequence <em>Update Sequence</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update Sequence</em>'.
	 * @see net.opengis.wcs10.CoverageDescriptionType#getUpdateSequence()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
    EAttribute getCoverageDescriptionType_UpdateSequence();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.CoverageDescriptionType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs10.CoverageDescriptionType#getVersion()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
    EAttribute getCoverageDescriptionType_Version();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.CoverageOfferingBriefType <em>Coverage Offering Brief Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coverage Offering Brief Type</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingBriefType
	 * @generated
	 */
    EClass getCoverageOfferingBriefType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.CoverageOfferingBriefType#getLonLatEnvelope <em>Lon Lat Envelope</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lon Lat Envelope</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingBriefType#getLonLatEnvelope()
	 * @see #getCoverageOfferingBriefType()
	 * @generated
	 */
    EReference getCoverageOfferingBriefType_LonLatEnvelope();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.CoverageOfferingBriefType#getKeywords <em>Keywords</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Keywords</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingBriefType#getKeywords()
	 * @see #getCoverageOfferingBriefType()
	 * @generated
	 */
    EReference getCoverageOfferingBriefType_Keywords();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.CoverageOfferingType <em>Coverage Offering Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coverage Offering Type</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingType
	 * @generated
	 */
    EClass getCoverageOfferingType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.CoverageOfferingType#getDomainSet <em>Domain Set</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Domain Set</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingType#getDomainSet()
	 * @see #getCoverageOfferingType()
	 * @generated
	 */
    EReference getCoverageOfferingType_DomainSet();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.CoverageOfferingType#getRangeSet <em>Range Set</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Range Set</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingType#getRangeSet()
	 * @see #getCoverageOfferingType()
	 * @generated
	 */
    EReference getCoverageOfferingType_RangeSet();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.CoverageOfferingType#getSupportedCRSs <em>Supported CR Ss</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supported CR Ss</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingType#getSupportedCRSs()
	 * @see #getCoverageOfferingType()
	 * @generated
	 */
    EReference getCoverageOfferingType_SupportedCRSs();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.CoverageOfferingType#getSupportedFormats <em>Supported Formats</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supported Formats</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingType#getSupportedFormats()
	 * @see #getCoverageOfferingType()
	 * @generated
	 */
    EReference getCoverageOfferingType_SupportedFormats();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.CoverageOfferingType#getSupportedInterpolations <em>Supported Interpolations</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supported Interpolations</em>'.
	 * @see net.opengis.wcs10.CoverageOfferingType#getSupportedInterpolations()
	 * @see #getCoverageOfferingType()
	 * @generated
	 */
    EReference getCoverageOfferingType_SupportedInterpolations();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.DCPTypeType <em>DCP Type Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DCP Type Type</em>'.
	 * @see net.opengis.wcs10.DCPTypeType
	 * @generated
	 */
    EClass getDCPTypeType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DCPTypeType#getHTTP <em>HTTP</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>HTTP</em>'.
	 * @see net.opengis.wcs10.DCPTypeType#getHTTP()
	 * @see #getDCPTypeType()
	 * @generated
	 */
    EReference getDCPTypeType_HTTP();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.DescribeCoverageType <em>Describe Coverage Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Describe Coverage Type</em>'.
	 * @see net.opengis.wcs10.DescribeCoverageType
	 * @generated
	 */
    EClass getDescribeCoverageType();

    /**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs10.DescribeCoverageType#getCoverage <em>Coverage</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Coverage</em>'.
	 * @see net.opengis.wcs10.DescribeCoverageType#getCoverage()
	 * @see #getDescribeCoverageType()
	 * @generated
	 */
    EAttribute getDescribeCoverageType_Coverage();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DescribeCoverageType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wcs10.DescribeCoverageType#getService()
	 * @see #getDescribeCoverageType()
	 * @generated
	 */
    EAttribute getDescribeCoverageType_Service();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DescribeCoverageType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs10.DescribeCoverageType#getVersion()
	 * @see #getDescribeCoverageType()
	 * @generated
	 */
    EAttribute getDescribeCoverageType_Version();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DescribeCoverageType#getBaseUrl <em>Base Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Url</em>'.
	 * @see net.opengis.wcs10.DescribeCoverageType#getBaseUrl()
	 * @see #getDescribeCoverageType()
	 * @generated
	 */
	EAttribute getDescribeCoverageType_BaseUrl();

				/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DescribeCoverageType#getExtendedProperties <em>Extended Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extended Properties</em>'.
	 * @see net.opengis.wcs10.DescribeCoverageType#getExtendedProperties()
	 * @see #getDescribeCoverageType()
	 * @generated
	 */
	EAttribute getDescribeCoverageType_ExtendedProperties();

				/**
	 * Returns the meta object for class '{@link net.opengis.wcs10.DescribeCoverageType1 <em>Describe Coverage Type1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Describe Coverage Type1</em>'.
	 * @see net.opengis.wcs10.DescribeCoverageType1
	 * @generated
	 */
    EClass getDescribeCoverageType1();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.DescribeCoverageType1#getDCPType <em>DCP Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>DCP Type</em>'.
	 * @see net.opengis.wcs10.DescribeCoverageType1#getDCPType()
	 * @see #getDescribeCoverageType1()
	 * @generated
	 */
    EReference getDescribeCoverageType1_DCPType();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see net.opengis.wcs10.DocumentRoot
	 * @generated
	 */
    EClass getDocumentRoot();

    /**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs10.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EAttribute getDocumentRoot_Mixed();

    /**
	 * Returns the meta object for the map '{@link net.opengis.wcs10.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
	 * Returns the meta object for the map '{@link net.opengis.wcs10.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getAxisDescription <em>Axis Description</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Axis Description</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getAxisDescription()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_AxisDescription();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getAxisDescription1 <em>Axis Description1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Axis Description1</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getAxisDescription1()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_AxisDescription1();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getCapability <em>Capability</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Capability</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getCapability()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_Capability();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getContentMetadata <em>Content Metadata</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Content Metadata</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getContentMetadata()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_ContentMetadata();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getCoverageDescription <em>Coverage Description</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Coverage Description</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getCoverageDescription()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_CoverageDescription();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getCoverageOffering <em>Coverage Offering</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Coverage Offering</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getCoverageOffering()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_CoverageOffering();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getCoverageOfferingBrief <em>Coverage Offering Brief</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Coverage Offering Brief</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getCoverageOfferingBrief()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_CoverageOfferingBrief();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Describe Coverage</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getDescribeCoverage()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_DescribeCoverage();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DocumentRoot#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getDescription()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EAttribute getDocumentRoot_Description();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getDomainSet <em>Domain Set</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Domain Set</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getDomainSet()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_DomainSet();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getFormats <em>Formats</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Formats</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getFormats()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_Formats();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getGetCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_GetCapabilities();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getGetCoverage <em>Get Coverage</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Coverage</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getGetCoverage()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_GetCoverage();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DocumentRoot#getInterpolationMethod <em>Interpolation Method</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interpolation Method</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getInterpolationMethod()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EAttribute getDocumentRoot_InterpolationMethod();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getInterval <em>Interval</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Interval</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getInterval()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_Interval();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getKeywords <em>Keywords</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Keywords</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getKeywords()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_Keywords();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getLonLatEnvelope <em>Lon Lat Envelope</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lon Lat Envelope</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getLonLatEnvelope()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_LonLatEnvelope();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getMetadataLink <em>Metadata Link</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metadata Link</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getMetadataLink()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_MetadataLink();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DocumentRoot#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getName()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EAttribute getDocumentRoot_Name();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getRangeSet <em>Range Set</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Range Set</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getRangeSet()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_RangeSet();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getRangeSet1 <em>Range Set1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Range Set1</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getRangeSet1()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_RangeSet1();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Service</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getService()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_Service();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getSingleValue <em>Single Value</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Single Value</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getSingleValue()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_SingleValue();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getSpatialDomain <em>Spatial Domain</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Spatial Domain</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getSpatialDomain()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_SpatialDomain();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getSpatialSubset <em>Spatial Subset</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Spatial Subset</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getSpatialSubset()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_SpatialSubset();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getSupportedCRSs <em>Supported CR Ss</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supported CR Ss</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getSupportedCRSs()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_SupportedCRSs();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getSupportedFormats <em>Supported Formats</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supported Formats</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getSupportedFormats()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_SupportedFormats();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getSupportedInterpolations <em>Supported Interpolations</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supported Interpolations</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getSupportedInterpolations()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_SupportedInterpolations();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getTemporalDomain <em>Temporal Domain</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Domain</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getTemporalDomain()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_TemporalDomain();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getTemporalSubset <em>Temporal Subset</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Subset</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getTemporalSubset()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_TemporalSubset();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getTimePeriod <em>Time Period</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Time Period</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getTimePeriod()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_TimePeriod();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getTimeSequence <em>Time Sequence</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Time Sequence</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getTimeSequence()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_TimeSequence();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DocumentRoot#getWCSCapabilities <em>WCS Capabilities</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>WCS Capabilities</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getWCSCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EReference getDocumentRoot_WCSCapabilities();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DocumentRoot#getClosure <em>Closure</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Closure</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getClosure()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EAttribute getDocumentRoot_Closure();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DocumentRoot#getSemantic <em>Semantic</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Semantic</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getSemantic()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EAttribute getDocumentRoot_Semantic();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.DocumentRoot#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.wcs10.DocumentRoot#getType()
	 * @see #getDocumentRoot()
	 * @generated
	 */
    EAttribute getDocumentRoot_Type();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.DomainSetType <em>Domain Set Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Domain Set Type</em>'.
	 * @see net.opengis.wcs10.DomainSetType
	 * @generated
	 */
    EClass getDomainSetType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DomainSetType#getSpatialDomain <em>Spatial Domain</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Spatial Domain</em>'.
	 * @see net.opengis.wcs10.DomainSetType#getSpatialDomain()
	 * @see #getDomainSetType()
	 * @generated
	 */
    EReference getDomainSetType_SpatialDomain();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DomainSetType#getTemporalDomain <em>Temporal Domain</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Domain</em>'.
	 * @see net.opengis.wcs10.DomainSetType#getTemporalDomain()
	 * @see #getDomainSetType()
	 * @generated
	 */
    EReference getDomainSetType_TemporalDomain();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DomainSetType#getTemporalDomain1 <em>Temporal Domain1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Domain1</em>'.
	 * @see net.opengis.wcs10.DomainSetType#getTemporalDomain1()
	 * @see #getDomainSetType()
	 * @generated
	 */
    EReference getDomainSetType_TemporalDomain1();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.DomainSubsetType <em>Domain Subset Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Domain Subset Type</em>'.
	 * @see net.opengis.wcs10.DomainSubsetType
	 * @generated
	 */
    EClass getDomainSubsetType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DomainSubsetType#getSpatialSubset <em>Spatial Subset</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Spatial Subset</em>'.
	 * @see net.opengis.wcs10.DomainSubsetType#getSpatialSubset()
	 * @see #getDomainSubsetType()
	 * @generated
	 */
    EReference getDomainSubsetType_SpatialSubset();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DomainSubsetType#getTemporalSubset <em>Temporal Subset</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Subset</em>'.
	 * @see net.opengis.wcs10.DomainSubsetType#getTemporalSubset()
	 * @see #getDomainSubsetType()
	 * @generated
	 */
    EReference getDomainSubsetType_TemporalSubset();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.DomainSubsetType#getTemporalSubset1 <em>Temporal Subset1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Subset1</em>'.
	 * @see net.opengis.wcs10.DomainSubsetType#getTemporalSubset1()
	 * @see #getDomainSubsetType()
	 * @generated
	 */
    EReference getDomainSubsetType_TemporalSubset1();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ExceptionType <em>Exception Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exception Type</em>'.
	 * @see net.opengis.wcs10.ExceptionType
	 * @generated
	 */
    EClass getExceptionType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ExceptionType#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Format</em>'.
	 * @see net.opengis.wcs10.ExceptionType#getFormat()
	 * @see #getExceptionType()
	 * @generated
	 */
    EAttribute getExceptionType_Format();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Capabilities Type</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType
	 * @generated
	 */
    EClass getGetCapabilitiesType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCapabilitiesType#getSection <em>Section</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Section</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType#getSection()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
    EAttribute getGetCapabilitiesType_Section();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCapabilitiesType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType#getService()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
    EAttribute getGetCapabilitiesType_Service();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update Sequence</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType#getUpdateSequence()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
    EAttribute getGetCapabilitiesType_UpdateSequence();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCapabilitiesType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType#getVersion()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
    EAttribute getGetCapabilitiesType_Version();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Url</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType#getBaseUrl()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
	EAttribute getGetCapabilitiesType_BaseUrl();

				/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extended Properties</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType#getExtendedProperties()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
	EAttribute getGetCapabilitiesType_ExtendedProperties();

				/**
	 * Returns the meta object for class '{@link net.opengis.wcs10.GetCapabilitiesType1 <em>Get Capabilities Type1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Capabilities Type1</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType1
	 * @generated
	 */
    EClass getGetCapabilitiesType1();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.GetCapabilitiesType1#getDCPType <em>DCP Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>DCP Type</em>'.
	 * @see net.opengis.wcs10.GetCapabilitiesType1#getDCPType()
	 * @see #getGetCapabilitiesType1()
	 * @generated
	 */
    EReference getGetCapabilitiesType1_DCPType();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.GetCoverageType <em>Get Coverage Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Coverage Type</em>'.
	 * @see net.opengis.wcs10.GetCoverageType
	 * @generated
	 */
    EClass getGetCoverageType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCoverageType#getSourceCoverage <em>Source Coverage</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Coverage</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getSourceCoverage()
	 * @see #getGetCoverageType()
	 * @generated
	 */
    EAttribute getGetCoverageType_SourceCoverage();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.GetCoverageType#getDomainSubset <em>Domain Subset</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Domain Subset</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getDomainSubset()
	 * @see #getGetCoverageType()
	 * @generated
	 */
    EReference getGetCoverageType_DomainSubset();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.GetCoverageType#getRangeSubset <em>Range Subset</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Range Subset</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getRangeSubset()
	 * @see #getGetCoverageType()
	 * @generated
	 */
    EReference getGetCoverageType_RangeSubset();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCoverageType#getInterpolationMethod <em>Interpolation Method</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interpolation Method</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getInterpolationMethod()
	 * @see #getGetCoverageType()
	 * @generated
	 */
    EAttribute getGetCoverageType_InterpolationMethod();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.GetCoverageType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getOutput()
	 * @see #getGetCoverageType()
	 * @generated
	 */
    EReference getGetCoverageType_Output();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCoverageType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getService()
	 * @see #getGetCoverageType()
	 * @generated
	 */
    EAttribute getGetCoverageType_Service();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCoverageType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getVersion()
	 * @see #getGetCoverageType()
	 * @generated
	 */
    EAttribute getGetCoverageType_Version();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCoverageType#getBaseUrl <em>Base Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Url</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getBaseUrl()
	 * @see #getGetCoverageType()
	 * @generated
	 */
	EAttribute getGetCoverageType_BaseUrl();

				/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.GetCoverageType#getExtendedProperties <em>Extended Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extended Properties</em>'.
	 * @see net.opengis.wcs10.GetCoverageType#getExtendedProperties()
	 * @see #getGetCoverageType()
	 * @generated
	 */
	EAttribute getGetCoverageType_ExtendedProperties();

				/**
	 * Returns the meta object for class '{@link net.opengis.wcs10.GetCoverageType1 <em>Get Coverage Type1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Coverage Type1</em>'.
	 * @see net.opengis.wcs10.GetCoverageType1
	 * @generated
	 */
    EClass getGetCoverageType1();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.GetCoverageType1#getDCPType <em>DCP Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>DCP Type</em>'.
	 * @see net.opengis.wcs10.GetCoverageType1#getDCPType()
	 * @see #getGetCoverageType1()
	 * @generated
	 */
    EReference getGetCoverageType1_DCPType();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.GetType <em>Get Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Type</em>'.
	 * @see net.opengis.wcs10.GetType
	 * @generated
	 */
    EClass getGetType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.GetType#getOnlineResource <em>Online Resource</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Online Resource</em>'.
	 * @see net.opengis.wcs10.GetType#getOnlineResource()
	 * @see #getGetType()
	 * @generated
	 */
    EReference getGetType_OnlineResource();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.HTTPType <em>HTTP Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>HTTP Type</em>'.
	 * @see net.opengis.wcs10.HTTPType
	 * @generated
	 */
    EClass getHTTPType();

    /**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs10.HTTPType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wcs10.HTTPType#getGroup()
	 * @see #getHTTPType()
	 * @generated
	 */
    EAttribute getHTTPType_Group();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.HTTPType#getGet <em>Get</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Get</em>'.
	 * @see net.opengis.wcs10.HTTPType#getGet()
	 * @see #getHTTPType()
	 * @generated
	 */
    EReference getHTTPType_Get();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.HTTPType#getPost <em>Post</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Post</em>'.
	 * @see net.opengis.wcs10.HTTPType#getPost()
	 * @see #getHTTPType()
	 * @generated
	 */
    EReference getHTTPType_Post();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.IntervalType <em>Interval Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interval Type</em>'.
	 * @see net.opengis.wcs10.IntervalType
	 * @generated
	 */
    EClass getIntervalType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.IntervalType#getRes <em>Res</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Res</em>'.
	 * @see net.opengis.wcs10.IntervalType#getRes()
	 * @see #getIntervalType()
	 * @generated
	 */
    EReference getIntervalType_Res();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.KeywordsType <em>Keywords Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Keywords Type</em>'.
	 * @see net.opengis.wcs10.KeywordsType
	 * @generated
	 */
    EClass getKeywordsType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.KeywordsType#getKeyword <em>Keyword</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Keyword</em>'.
	 * @see net.opengis.wcs10.KeywordsType#getKeyword()
	 * @see #getKeywordsType()
	 * @generated
	 */
    EAttribute getKeywordsType_Keyword();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.KeywordsType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Type</em>'.
	 * @see net.opengis.wcs10.KeywordsType#getType()
	 * @see #getKeywordsType()
	 * @generated
	 */
    EReference getKeywordsType_Type();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.LonLatEnvelopeBaseType <em>Lon Lat Envelope Base Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lon Lat Envelope Base Type</em>'.
	 * @see net.opengis.wcs10.LonLatEnvelopeBaseType
	 * @generated
	 */
    EClass getLonLatEnvelopeBaseType();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.LonLatEnvelopeType <em>Lon Lat Envelope Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lon Lat Envelope Type</em>'.
	 * @see net.opengis.wcs10.LonLatEnvelopeType
	 * @generated
	 */
    EClass getLonLatEnvelopeType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.LonLatEnvelopeType#getTimePosition <em>Time Position</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Time Position</em>'.
	 * @see net.opengis.wcs10.LonLatEnvelopeType#getTimePosition()
	 * @see #getLonLatEnvelopeType()
	 * @generated
	 */
    EReference getLonLatEnvelopeType_TimePosition();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.MetadataAssociationType <em>Metadata Association Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metadata Association Type</em>'.
	 * @see net.opengis.wcs10.MetadataAssociationType
	 * @generated
	 */
    EClass getMetadataAssociationType();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.MetadataLinkType <em>Metadata Link Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metadata Link Type</em>'.
	 * @see net.opengis.wcs10.MetadataLinkType
	 * @generated
	 */
    EClass getMetadataLinkType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.MetadataLinkType#getMetadataType <em>Metadata Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Metadata Type</em>'.
	 * @see net.opengis.wcs10.MetadataLinkType#getMetadataType()
	 * @see #getMetadataLinkType()
	 * @generated
	 */
    EAttribute getMetadataLinkType_MetadataType();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.OnlineResourceType <em>Online Resource Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Online Resource Type</em>'.
	 * @see net.opengis.wcs10.OnlineResourceType
	 * @generated
	 */
    EClass getOnlineResourceType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.OnlineResourceType#getActuate <em>Actuate</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Actuate</em>'.
	 * @see net.opengis.wcs10.OnlineResourceType#getActuate()
	 * @see #getOnlineResourceType()
	 * @generated
	 */
    EAttribute getOnlineResourceType_Actuate();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.OnlineResourceType#getArcrole <em>Arcrole</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arcrole</em>'.
	 * @see net.opengis.wcs10.OnlineResourceType#getArcrole()
	 * @see #getOnlineResourceType()
	 * @generated
	 */
    EAttribute getOnlineResourceType_Arcrole();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.OnlineResourceType#getHref <em>Href</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Href</em>'.
	 * @see net.opengis.wcs10.OnlineResourceType#getHref()
	 * @see #getOnlineResourceType()
	 * @generated
	 */
    EAttribute getOnlineResourceType_Href();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.OnlineResourceType#getRole <em>Role</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Role</em>'.
	 * @see net.opengis.wcs10.OnlineResourceType#getRole()
	 * @see #getOnlineResourceType()
	 * @generated
	 */
    EAttribute getOnlineResourceType_Role();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.OnlineResourceType#getShow <em>Show</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show</em>'.
	 * @see net.opengis.wcs10.OnlineResourceType#getShow()
	 * @see #getOnlineResourceType()
	 * @generated
	 */
    EAttribute getOnlineResourceType_Show();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.OnlineResourceType#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see net.opengis.wcs10.OnlineResourceType#getTitle()
	 * @see #getOnlineResourceType()
	 * @generated
	 */
    EAttribute getOnlineResourceType_Title();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.OnlineResourceType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.wcs10.OnlineResourceType#getType()
	 * @see #getOnlineResourceType()
	 * @generated
	 */
    EAttribute getOnlineResourceType_Type();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.OutputType <em>Output Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Type</em>'.
	 * @see net.opengis.wcs10.OutputType
	 * @generated
	 */
    EClass getOutputType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.OutputType#getCrs <em>Crs</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Crs</em>'.
	 * @see net.opengis.wcs10.OutputType#getCrs()
	 * @see #getOutputType()
	 * @generated
	 */
    EReference getOutputType_Crs();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.OutputType#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Format</em>'.
	 * @see net.opengis.wcs10.OutputType#getFormat()
	 * @see #getOutputType()
	 * @generated
	 */
    EReference getOutputType_Format();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.PostType <em>Post Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Post Type</em>'.
	 * @see net.opengis.wcs10.PostType
	 * @generated
	 */
    EClass getPostType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.PostType#getOnlineResource <em>Online Resource</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Online Resource</em>'.
	 * @see net.opengis.wcs10.PostType#getOnlineResource()
	 * @see #getPostType()
	 * @generated
	 */
    EReference getPostType_OnlineResource();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.RangeSetType <em>Range Set Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Range Set Type</em>'.
	 * @see net.opengis.wcs10.RangeSetType
	 * @generated
	 */
    EClass getRangeSetType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.RangeSetType#getAxisDescription <em>Axis Description</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Axis Description</em>'.
	 * @see net.opengis.wcs10.RangeSetType#getAxisDescription()
	 * @see #getRangeSetType()
	 * @generated
	 */
    EReference getRangeSetType_AxisDescription();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.RangeSetType#getNullValues <em>Null Values</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Null Values</em>'.
	 * @see net.opengis.wcs10.RangeSetType#getNullValues()
	 * @see #getRangeSetType()
	 * @generated
	 */
    EReference getRangeSetType_NullValues();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.RangeSetType#getRefSys <em>Ref Sys</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ref Sys</em>'.
	 * @see net.opengis.wcs10.RangeSetType#getRefSys()
	 * @see #getRangeSetType()
	 * @generated
	 */
    EAttribute getRangeSetType_RefSys();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.RangeSetType#getRefSysLabel <em>Ref Sys Label</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ref Sys Label</em>'.
	 * @see net.opengis.wcs10.RangeSetType#getRefSysLabel()
	 * @see #getRangeSetType()
	 * @generated
	 */
    EAttribute getRangeSetType_RefSysLabel();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.RangeSetType#getSemantic <em>Semantic</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Semantic</em>'.
	 * @see net.opengis.wcs10.RangeSetType#getSemantic()
	 * @see #getRangeSetType()
	 * @generated
	 */
    EAttribute getRangeSetType_Semantic();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.RangeSetType1 <em>Range Set Type1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Range Set Type1</em>'.
	 * @see net.opengis.wcs10.RangeSetType1
	 * @generated
	 */
    EClass getRangeSetType1();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.RangeSetType1#getRangeSet <em>Range Set</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Range Set</em>'.
	 * @see net.opengis.wcs10.RangeSetType1#getRangeSet()
	 * @see #getRangeSetType1()
	 * @generated
	 */
    EReference getRangeSetType1_RangeSet();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.RangeSubsetType <em>Range Subset Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Range Subset Type</em>'.
	 * @see net.opengis.wcs10.RangeSubsetType
	 * @generated
	 */
    EClass getRangeSubsetType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.RangeSubsetType#getAxisSubset <em>Axis Subset</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Axis Subset</em>'.
	 * @see net.opengis.wcs10.RangeSubsetType#getAxisSubset()
	 * @see #getRangeSubsetType()
	 * @generated
	 */
    EReference getRangeSubsetType_AxisSubset();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.RequestType <em>Request Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Request Type</em>'.
	 * @see net.opengis.wcs10.RequestType
	 * @generated
	 */
    EClass getRequestType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.RequestType#getGetCapabilities <em>Get Capabilities</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
	 * @see net.opengis.wcs10.RequestType#getGetCapabilities()
	 * @see #getRequestType()
	 * @generated
	 */
    EReference getRequestType_GetCapabilities();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.RequestType#getDescribeCoverage <em>Describe Coverage</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Describe Coverage</em>'.
	 * @see net.opengis.wcs10.RequestType#getDescribeCoverage()
	 * @see #getRequestType()
	 * @generated
	 */
    EReference getRequestType_DescribeCoverage();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.RequestType#getGetCoverage <em>Get Coverage</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Coverage</em>'.
	 * @see net.opengis.wcs10.RequestType#getGetCoverage()
	 * @see #getRequestType()
	 * @generated
	 */
    EReference getRequestType_GetCoverage();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ResponsiblePartyType <em>Responsible Party Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Responsible Party Type</em>'.
	 * @see net.opengis.wcs10.ResponsiblePartyType
	 * @generated
	 */
    EClass getResponsiblePartyType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ResponsiblePartyType#getIndividualName <em>Individual Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Individual Name</em>'.
	 * @see net.opengis.wcs10.ResponsiblePartyType#getIndividualName()
	 * @see #getResponsiblePartyType()
	 * @generated
	 */
    EAttribute getResponsiblePartyType_IndividualName();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ResponsiblePartyType#getOrganisationName <em>Organisation Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Organisation Name</em>'.
	 * @see net.opengis.wcs10.ResponsiblePartyType#getOrganisationName()
	 * @see #getResponsiblePartyType()
	 * @generated
	 */
    EAttribute getResponsiblePartyType_OrganisationName();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ResponsiblePartyType#getOrganisationName1 <em>Organisation Name1</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Organisation Name1</em>'.
	 * @see net.opengis.wcs10.ResponsiblePartyType#getOrganisationName1()
	 * @see #getResponsiblePartyType()
	 * @generated
	 */
    EAttribute getResponsiblePartyType_OrganisationName1();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ResponsiblePartyType#getPositionName <em>Position Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Position Name</em>'.
	 * @see net.opengis.wcs10.ResponsiblePartyType#getPositionName()
	 * @see #getResponsiblePartyType()
	 * @generated
	 */
    EAttribute getResponsiblePartyType_PositionName();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ResponsiblePartyType#getContactInfo <em>Contact Info</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Contact Info</em>'.
	 * @see net.opengis.wcs10.ResponsiblePartyType#getContactInfo()
	 * @see #getResponsiblePartyType()
	 * @generated
	 */
    EReference getResponsiblePartyType_ContactInfo();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ServiceType <em>Service Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Service Type</em>'.
	 * @see net.opengis.wcs10.ServiceType
	 * @generated
	 */
    EClass getServiceType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.ServiceType#getKeywords <em>Keywords</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Keywords</em>'.
	 * @see net.opengis.wcs10.ServiceType#getKeywords()
	 * @see #getServiceType()
	 * @generated
	 */
    EReference getServiceType_Keywords();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ServiceType#getResponsibleParty <em>Responsible Party</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Responsible Party</em>'.
	 * @see net.opengis.wcs10.ServiceType#getResponsibleParty()
	 * @see #getServiceType()
	 * @generated
	 */
    EReference getServiceType_ResponsibleParty();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ServiceType#getFees <em>Fees</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fees</em>'.
	 * @see net.opengis.wcs10.ServiceType#getFees()
	 * @see #getServiceType()
	 * @generated
	 */
    EReference getServiceType_Fees();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.ServiceType#getAccessConstraints <em>Access Constraints</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Access Constraints</em>'.
	 * @see net.opengis.wcs10.ServiceType#getAccessConstraints()
	 * @see #getServiceType()
	 * @generated
	 */
    EReference getServiceType_AccessConstraints();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ServiceType#getUpdateSequence <em>Update Sequence</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update Sequence</em>'.
	 * @see net.opengis.wcs10.ServiceType#getUpdateSequence()
	 * @see #getServiceType()
	 * @generated
	 */
    EAttribute getServiceType_UpdateSequence();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ServiceType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs10.ServiceType#getVersion()
	 * @see #getServiceType()
	 * @generated
	 */
    EAttribute getServiceType_Version();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.SpatialDomainType <em>Spatial Domain Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spatial Domain Type</em>'.
	 * @see net.opengis.wcs10.SpatialDomainType
	 * @generated
	 */
    EClass getSpatialDomainType();

    /**
	 * Returns the meta object for the reference list '{@link net.opengis.wcs10.SpatialDomainType#getEnvelope <em>Envelope</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Envelope</em>'.
	 * @see net.opengis.wcs10.SpatialDomainType#getEnvelope()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
    EReference getSpatialDomainType_Envelope();

    /**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs10.SpatialDomainType#getGridGroup <em>Grid Group</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Grid Group</em>'.
	 * @see net.opengis.wcs10.SpatialDomainType#getGridGroup()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
    EAttribute getSpatialDomainType_GridGroup();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.SpatialDomainType#getGrid <em>Grid</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Grid</em>'.
	 * @see net.opengis.wcs10.SpatialDomainType#getGrid()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
    EReference getSpatialDomainType_Grid();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.SpatialDomainType#getPolygon <em>Polygon</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Polygon</em>'.
	 * @see net.opengis.wcs10.SpatialDomainType#getPolygon()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
    EReference getSpatialDomainType_Polygon();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.SpatialSubsetType <em>Spatial Subset Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spatial Subset Type</em>'.
	 * @see net.opengis.wcs10.SpatialSubsetType
	 * @generated
	 */
    EClass getSpatialSubsetType();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.SupportedCRSsType <em>Supported CR Ss Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Supported CR Ss Type</em>'.
	 * @see net.opengis.wcs10.SupportedCRSsType
	 * @generated
	 */
    EClass getSupportedCRSsType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.SupportedCRSsType#getRequestResponseCRSs <em>Request Response CR Ss</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Request Response CR Ss</em>'.
	 * @see net.opengis.wcs10.SupportedCRSsType#getRequestResponseCRSs()
	 * @see #getSupportedCRSsType()
	 * @generated
	 */
    EReference getSupportedCRSsType_RequestResponseCRSs();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.SupportedCRSsType#getRequestCRSs <em>Request CR Ss</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Request CR Ss</em>'.
	 * @see net.opengis.wcs10.SupportedCRSsType#getRequestCRSs()
	 * @see #getSupportedCRSsType()
	 * @generated
	 */
    EReference getSupportedCRSsType_RequestCRSs();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.SupportedCRSsType#getResponseCRSs <em>Response CR Ss</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Response CR Ss</em>'.
	 * @see net.opengis.wcs10.SupportedCRSsType#getResponseCRSs()
	 * @see #getSupportedCRSsType()
	 * @generated
	 */
    EReference getSupportedCRSsType_ResponseCRSs();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.SupportedCRSsType#getNativeCRSs <em>Native CR Ss</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Native CR Ss</em>'.
	 * @see net.opengis.wcs10.SupportedCRSsType#getNativeCRSs()
	 * @see #getSupportedCRSsType()
	 * @generated
	 */
    EReference getSupportedCRSsType_NativeCRSs();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.SupportedFormatsType <em>Supported Formats Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Supported Formats Type</em>'.
	 * @see net.opengis.wcs10.SupportedFormatsType
	 * @generated
	 */
    EClass getSupportedFormatsType();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.SupportedFormatsType#getFormats <em>Formats</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Formats</em>'.
	 * @see net.opengis.wcs10.SupportedFormatsType#getFormats()
	 * @see #getSupportedFormatsType()
	 * @generated
	 */
    EReference getSupportedFormatsType_Formats();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.SupportedFormatsType#getNativeFormat <em>Native Format</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Native Format</em>'.
	 * @see net.opengis.wcs10.SupportedFormatsType#getNativeFormat()
	 * @see #getSupportedFormatsType()
	 * @generated
	 */
    EAttribute getSupportedFormatsType_NativeFormat();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.SupportedInterpolationsType <em>Supported Interpolations Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Supported Interpolations Type</em>'.
	 * @see net.opengis.wcs10.SupportedInterpolationsType
	 * @generated
	 */
    EClass getSupportedInterpolationsType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.SupportedInterpolationsType#getInterpolationMethod <em>Interpolation Method</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interpolation Method</em>'.
	 * @see net.opengis.wcs10.SupportedInterpolationsType#getInterpolationMethod()
	 * @see #getSupportedInterpolationsType()
	 * @generated
	 */
    EAttribute getSupportedInterpolationsType_InterpolationMethod();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.SupportedInterpolationsType#getDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see net.opengis.wcs10.SupportedInterpolationsType#getDefault()
	 * @see #getSupportedInterpolationsType()
	 * @generated
	 */
    EAttribute getSupportedInterpolationsType_Default();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.TelephoneType <em>Telephone Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Telephone Type</em>'.
	 * @see net.opengis.wcs10.TelephoneType
	 * @generated
	 */
    EClass getTelephoneType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.TelephoneType#getVoice <em>Voice</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Voice</em>'.
	 * @see net.opengis.wcs10.TelephoneType#getVoice()
	 * @see #getTelephoneType()
	 * @generated
	 */
    EAttribute getTelephoneType_Voice();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.TelephoneType#getFacsimile <em>Facsimile</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Facsimile</em>'.
	 * @see net.opengis.wcs10.TelephoneType#getFacsimile()
	 * @see #getTelephoneType()
	 * @generated
	 */
    EAttribute getTelephoneType_Facsimile();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.TimePeriodType <em>Time Period Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Period Type</em>'.
	 * @see net.opengis.wcs10.TimePeriodType
	 * @generated
	 */
    EClass getTimePeriodType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.TimePeriodType#getBeginPosition <em>Begin Position</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Begin Position</em>'.
	 * @see net.opengis.wcs10.TimePeriodType#getBeginPosition()
	 * @see #getTimePeriodType()
	 * @generated
	 */
    EReference getTimePeriodType_BeginPosition();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.TimePeriodType#getEndPosition <em>End Position</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>End Position</em>'.
	 * @see net.opengis.wcs10.TimePeriodType#getEndPosition()
	 * @see #getTimePeriodType()
	 * @generated
	 */
    EReference getTimePeriodType_EndPosition();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.TimePeriodType#getTimeResolution <em>Time Resolution</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Resolution</em>'.
	 * @see net.opengis.wcs10.TimePeriodType#getTimeResolution()
	 * @see #getTimePeriodType()
	 * @generated
	 */
    EAttribute getTimePeriodType_TimeResolution();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.TimePeriodType#getFrame <em>Frame</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Frame</em>'.
	 * @see net.opengis.wcs10.TimePeriodType#getFrame()
	 * @see #getTimePeriodType()
	 * @generated
	 */
    EAttribute getTimePeriodType_Frame();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.TimeSequenceType <em>Time Sequence Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Sequence Type</em>'.
	 * @see net.opengis.wcs10.TimeSequenceType
	 * @generated
	 */
    EClass getTimeSequenceType();

    /**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs10.TimeSequenceType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wcs10.TimeSequenceType#getGroup()
	 * @see #getTimeSequenceType()
	 * @generated
	 */
    EAttribute getTimeSequenceType_Group();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.TimeSequenceType#getTimePosition <em>Time Position</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Time Position</em>'.
	 * @see net.opengis.wcs10.TimeSequenceType#getTimePosition()
	 * @see #getTimeSequenceType()
	 * @generated
	 */
    EReference getTimeSequenceType_TimePosition();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.TimeSequenceType#getTimePeriod <em>Time Period</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Time Period</em>'.
	 * @see net.opengis.wcs10.TimeSequenceType#getTimePeriod()
	 * @see #getTimeSequenceType()
	 * @generated
	 */
    EReference getTimeSequenceType_TimePeriod();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.TypedLiteralType <em>Typed Literal Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Typed Literal Type</em>'.
	 * @see net.opengis.wcs10.TypedLiteralType
	 * @generated
	 */
    EClass getTypedLiteralType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.TypedLiteralType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.wcs10.TypedLiteralType#getValue()
	 * @see #getTypedLiteralType()
	 * @generated
	 */
    EAttribute getTypedLiteralType_Value();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.TypedLiteralType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.wcs10.TypedLiteralType#getType()
	 * @see #getTypedLiteralType()
	 * @generated
	 */
    EAttribute getTypedLiteralType_Type();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ValueEnumBaseType <em>Value Enum Base Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Value Enum Base Type</em>'.
	 * @see net.opengis.wcs10.ValueEnumBaseType
	 * @generated
	 */
    EClass getValueEnumBaseType();

    /**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs10.ValueEnumBaseType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wcs10.ValueEnumBaseType#getGroup()
	 * @see #getValueEnumBaseType()
	 * @generated
	 */
    EAttribute getValueEnumBaseType_Group();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.ValueEnumBaseType#getInterval <em>Interval</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Interval</em>'.
	 * @see net.opengis.wcs10.ValueEnumBaseType#getInterval()
	 * @see #getValueEnumBaseType()
	 * @generated
	 */
    EReference getValueEnumBaseType_Interval();

    /**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs10.ValueEnumBaseType#getSingleValue <em>Single Value</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Single Value</em>'.
	 * @see net.opengis.wcs10.ValueEnumBaseType#getSingleValue()
	 * @see #getValueEnumBaseType()
	 * @generated
	 */
    EReference getValueEnumBaseType_SingleValue();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ValueEnumType <em>Value Enum Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Value Enum Type</em>'.
	 * @see net.opengis.wcs10.ValueEnumType
	 * @generated
	 */
    EClass getValueEnumType();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ValueEnumType#getSemantic <em>Semantic</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Semantic</em>'.
	 * @see net.opengis.wcs10.ValueEnumType#getSemantic()
	 * @see #getValueEnumType()
	 * @generated
	 */
    EAttribute getValueEnumType_Semantic();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ValueEnumType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.wcs10.ValueEnumType#getType()
	 * @see #getValueEnumType()
	 * @generated
	 */
    EAttribute getValueEnumType_Type();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ValueRangeType <em>Value Range Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Value Range Type</em>'.
	 * @see net.opengis.wcs10.ValueRangeType
	 * @generated
	 */
    EClass getValueRangeType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ValueRangeType#getMin <em>Min</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Min</em>'.
	 * @see net.opengis.wcs10.ValueRangeType#getMin()
	 * @see #getValueRangeType()
	 * @generated
	 */
    EReference getValueRangeType_Min();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ValueRangeType#getMax <em>Max</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Max</em>'.
	 * @see net.opengis.wcs10.ValueRangeType#getMax()
	 * @see #getValueRangeType()
	 * @generated
	 */
    EReference getValueRangeType_Max();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ValueRangeType#isAtomic <em>Atomic</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Atomic</em>'.
	 * @see net.opengis.wcs10.ValueRangeType#isAtomic()
	 * @see #getValueRangeType()
	 * @generated
	 */
    EAttribute getValueRangeType_Atomic();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ValueRangeType#getClosure <em>Closure</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Closure</em>'.
	 * @see net.opengis.wcs10.ValueRangeType#getClosure()
	 * @see #getValueRangeType()
	 * @generated
	 */
    EAttribute getValueRangeType_Closure();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ValueRangeType#getSemantic <em>Semantic</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Semantic</em>'.
	 * @see net.opengis.wcs10.ValueRangeType#getSemantic()
	 * @see #getValueRangeType()
	 * @generated
	 */
    EAttribute getValueRangeType_Semantic();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.ValueRangeType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.wcs10.ValueRangeType#getType()
	 * @see #getValueRangeType()
	 * @generated
	 */
    EAttribute getValueRangeType_Type();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.ValuesType <em>Values Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Values Type</em>'.
	 * @see net.opengis.wcs10.ValuesType
	 * @generated
	 */
    EClass getValuesType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.ValuesType#getDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Default</em>'.
	 * @see net.opengis.wcs10.ValuesType#getDefault()
	 * @see #getValuesType()
	 * @generated
	 */
    EReference getValuesType_Default();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.VendorSpecificCapabilitiesType <em>Vendor Specific Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vendor Specific Capabilities Type</em>'.
	 * @see net.opengis.wcs10.VendorSpecificCapabilitiesType
	 * @generated
	 */
    EClass getVendorSpecificCapabilitiesType();

    /**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs10.VendorSpecificCapabilitiesType#getAny <em>Any</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Any</em>'.
	 * @see net.opengis.wcs10.VendorSpecificCapabilitiesType#getAny()
	 * @see #getVendorSpecificCapabilitiesType()
	 * @generated
	 */
    EAttribute getVendorSpecificCapabilitiesType_Any();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.WCSCapabilitiesType <em>WCS Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>WCS Capabilities Type</em>'.
	 * @see net.opengis.wcs10.WCSCapabilitiesType
	 * @generated
	 */
    EClass getWCSCapabilitiesType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.WCSCapabilitiesType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Service</em>'.
	 * @see net.opengis.wcs10.WCSCapabilitiesType#getService()
	 * @see #getWCSCapabilitiesType()
	 * @generated
	 */
    EReference getWCSCapabilitiesType_Service();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.WCSCapabilitiesType#getCapability <em>Capability</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Capability</em>'.
	 * @see net.opengis.wcs10.WCSCapabilitiesType#getCapability()
	 * @see #getWCSCapabilitiesType()
	 * @generated
	 */
    EReference getWCSCapabilitiesType_Capability();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.WCSCapabilitiesType#getContentMetadata <em>Content Metadata</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Content Metadata</em>'.
	 * @see net.opengis.wcs10.WCSCapabilitiesType#getContentMetadata()
	 * @see #getWCSCapabilitiesType()
	 * @generated
	 */
    EReference getWCSCapabilitiesType_ContentMetadata();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.WCSCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update Sequence</em>'.
	 * @see net.opengis.wcs10.WCSCapabilitiesType#getUpdateSequence()
	 * @see #getWCSCapabilitiesType()
	 * @generated
	 */
    EAttribute getWCSCapabilitiesType_UpdateSequence();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.WCSCapabilitiesType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs10.WCSCapabilitiesType#getVersion()
	 * @see #getWCSCapabilitiesType()
	 * @generated
	 */
    EAttribute getWCSCapabilitiesType_Version();

    /**
	 * Returns the meta object for class '{@link net.opengis.wcs10.WCSCapabilityType <em>WCS Capability Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>WCS Capability Type</em>'.
	 * @see net.opengis.wcs10.WCSCapabilityType
	 * @generated
	 */
    EClass getWCSCapabilityType();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.WCSCapabilityType#getRequest <em>Request</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Request</em>'.
	 * @see net.opengis.wcs10.WCSCapabilityType#getRequest()
	 * @see #getWCSCapabilityType()
	 * @generated
	 */
    EReference getWCSCapabilityType_Request();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.WCSCapabilityType#getException <em>Exception</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Exception</em>'.
	 * @see net.opengis.wcs10.WCSCapabilityType#getException()
	 * @see #getWCSCapabilityType()
	 * @generated
	 */
    EReference getWCSCapabilityType_Exception();

    /**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs10.WCSCapabilityType#getVendorSpecificCapabilities <em>Vendor Specific Capabilities</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Vendor Specific Capabilities</em>'.
	 * @see net.opengis.wcs10.WCSCapabilityType#getVendorSpecificCapabilities()
	 * @see #getWCSCapabilityType()
	 * @generated
	 */
    EReference getWCSCapabilityType_VendorSpecificCapabilities();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.WCSCapabilityType#getUpdateSequence <em>Update Sequence</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update Sequence</em>'.
	 * @see net.opengis.wcs10.WCSCapabilityType#getUpdateSequence()
	 * @see #getWCSCapabilityType()
	 * @generated
	 */
    EAttribute getWCSCapabilityType_UpdateSequence();

    /**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs10.WCSCapabilityType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs10.WCSCapabilityType#getVersion()
	 * @see #getWCSCapabilityType()
	 * @generated
	 */
    EAttribute getWCSCapabilityType_Version();

    /**
	 * Returns the meta object for class '{@link com.vividsolutions.jts.geom.Envelope <em>Envelope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Envelope</em>'.
	 * @see com.vividsolutions.jts.geom.Envelope
	 * @model instanceClass="com.vividsolutions.jts.geom.Envelope"
	 * @generated
	 */
	EClass getEnvelope();

				/**
	 * Returns the meta object for class '{@link org.geotools.geometry.GeneralEnvelope <em>General Envelope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>General Envelope</em>'.
	 * @see org.geotools.geometry.GeneralEnvelope
	 * @model instanceClass="org.geotools.geometry.GeneralEnvelope"
	 * @generated
	 */
	EClass getGeneralEnvelope();

				/**
	 * Returns the meta object for enum '{@link net.opengis.wcs10.CapabilitiesSectionType <em>Capabilities Section Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Capabilities Section Type</em>'.
	 * @see net.opengis.wcs10.CapabilitiesSectionType
	 * @generated
	 */
    EEnum getCapabilitiesSectionType();

    /**
	 * Returns the meta object for enum '{@link net.opengis.wcs10.ClosureType <em>Closure Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Closure Type</em>'.
	 * @see net.opengis.wcs10.ClosureType
	 * @generated
	 */
    EEnum getClosureType();

    /**
	 * Returns the meta object for enum '{@link net.opengis.wcs10.InterpolationMethodType <em>Interpolation Method Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Interpolation Method Type</em>'.
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @generated
	 */
    EEnum getInterpolationMethodType();

    /**
	 * Returns the meta object for enum '{@link net.opengis.wcs10.MetadataTypeType <em>Metadata Type Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Metadata Type Type</em>'.
	 * @see net.opengis.wcs10.MetadataTypeType
	 * @generated
	 */
    EEnum getMetadataTypeType();

    /**
	 * Returns the meta object for data type '{@link net.opengis.wcs10.CapabilitiesSectionType <em>Capabilities Section Type Object</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Capabilities Section Type Object</em>'.
	 * @see net.opengis.wcs10.CapabilitiesSectionType
	 * @model instanceClass="net.opengis.wcs10.CapabilitiesSectionType"
	 *        extendedMetaData="name='CapabilitiesSectionType:Object' baseType='CapabilitiesSectionType'"
	 * @generated
	 */
    EDataType getCapabilitiesSectionTypeObject();

    /**
	 * Returns the meta object for data type '{@link net.opengis.wcs10.ClosureType <em>Closure Type Object</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Closure Type Object</em>'.
	 * @see net.opengis.wcs10.ClosureType
	 * @model instanceClass="net.opengis.wcs10.ClosureType"
	 *        extendedMetaData="name='closure_._type:Object' baseType='closure_._type'"
	 * @generated
	 */
    EDataType getClosureTypeObject();

    /**
	 * Returns the meta object for data type '{@link net.opengis.wcs10.InterpolationMethodType <em>Interpolation Method Type Object</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Interpolation Method Type Object</em>'.
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @model instanceClass="net.opengis.wcs10.InterpolationMethodType"
	 *        extendedMetaData="name='InterpolationMethodType:Object' baseType='InterpolationMethodType'"
	 * @generated
	 */
    EDataType getInterpolationMethodTypeObject();

    /**
	 * Returns the meta object for data type '{@link net.opengis.wcs10.MetadataTypeType <em>Metadata Type Type Object</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Metadata Type Type Object</em>'.
	 * @see net.opengis.wcs10.MetadataTypeType
	 * @model instanceClass="net.opengis.wcs10.MetadataTypeType"
	 *        extendedMetaData="name='metadataType_._type:Object' baseType='metadataType_._type'"
	 * @generated
	 */
    EDataType getMetadataTypeTypeObject();

    /**
	 * Returns the meta object for data type '{@link java.util.Map <em>Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Map</em>'.
	 * @see java.util.Map
	 * @model instanceClass="java.util.Map"
	 * @generated
	 */
	EDataType getMap();

				/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    Wcs10Factory getWcs10Factory();

    /**
	 * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {
        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.AbstractDescriptionBaseTypeImpl <em>Abstract Description Base Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.AbstractDescriptionBaseTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAbstractDescriptionBaseType()
		 * @generated
		 */
        EClass ABSTRACT_DESCRIPTION_BASE_TYPE = eINSTANCE.getAbstractDescriptionBaseType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.AbstractDescriptionTypeImpl <em>Abstract Description Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.AbstractDescriptionTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAbstractDescriptionType()
		 * @generated
		 */
        EClass ABSTRACT_DESCRIPTION_TYPE = eINSTANCE.getAbstractDescriptionType();

        /**
		 * The meta object literal for the '<em><b>Metadata Link</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK = eINSTANCE.getAbstractDescriptionType_MetadataLink();

        /**
		 * The meta object literal for the '<em><b>Description1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1 = eINSTANCE.getAbstractDescriptionType_Description1();

        /**
		 * The meta object literal for the '<em><b>Name1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ABSTRACT_DESCRIPTION_TYPE__NAME1 = eINSTANCE.getAbstractDescriptionType_Name1();

        /**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ABSTRACT_DESCRIPTION_TYPE__LABEL = eINSTANCE.getAbstractDescriptionType_Label();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.AddressTypeImpl <em>Address Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.AddressTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAddressType()
		 * @generated
		 */
        EClass ADDRESS_TYPE = eINSTANCE.getAddressType();

        /**
		 * The meta object literal for the '<em><b>Delivery Point</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ADDRESS_TYPE__DELIVERY_POINT = eINSTANCE.getAddressType_DeliveryPoint();

        /**
		 * The meta object literal for the '<em><b>City</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ADDRESS_TYPE__CITY = eINSTANCE.getAddressType_City();

        /**
		 * The meta object literal for the '<em><b>Administrative Area</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ADDRESS_TYPE__ADMINISTRATIVE_AREA = eINSTANCE.getAddressType_AdministrativeArea();

        /**
		 * The meta object literal for the '<em><b>Postal Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ADDRESS_TYPE__POSTAL_CODE = eINSTANCE.getAddressType_PostalCode();

        /**
		 * The meta object literal for the '<em><b>Country</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ADDRESS_TYPE__COUNTRY = eINSTANCE.getAddressType_Country();

        /**
		 * The meta object literal for the '<em><b>Electronic Mail Address</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ADDRESS_TYPE__ELECTRONIC_MAIL_ADDRESS = eINSTANCE.getAddressType_ElectronicMailAddress();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.AxisDescriptionTypeImpl <em>Axis Description Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.AxisDescriptionTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAxisDescriptionType()
		 * @generated
		 */
        EClass AXIS_DESCRIPTION_TYPE = eINSTANCE.getAxisDescriptionType();

        /**
		 * The meta object literal for the '<em><b>Values</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference AXIS_DESCRIPTION_TYPE__VALUES = eINSTANCE.getAxisDescriptionType_Values();

        /**
		 * The meta object literal for the '<em><b>Ref Sys</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute AXIS_DESCRIPTION_TYPE__REF_SYS = eINSTANCE.getAxisDescriptionType_RefSys();

        /**
		 * The meta object literal for the '<em><b>Ref Sys Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute AXIS_DESCRIPTION_TYPE__REF_SYS_LABEL = eINSTANCE.getAxisDescriptionType_RefSysLabel();

        /**
		 * The meta object literal for the '<em><b>Semantic</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute AXIS_DESCRIPTION_TYPE__SEMANTIC = eINSTANCE.getAxisDescriptionType_Semantic();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.AxisDescriptionType1Impl <em>Axis Description Type1</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.AxisDescriptionType1Impl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAxisDescriptionType1()
		 * @generated
		 */
        EClass AXIS_DESCRIPTION_TYPE1 = eINSTANCE.getAxisDescriptionType1();

        /**
		 * The meta object literal for the '<em><b>Axis Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION = eINSTANCE.getAxisDescriptionType1_AxisDescription();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.AxisSubsetTypeImpl <em>Axis Subset Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.AxisSubsetTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getAxisSubsetType()
		 * @generated
		 */
        EClass AXIS_SUBSET_TYPE = eINSTANCE.getAxisSubsetType();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute AXIS_SUBSET_TYPE__NAME = eINSTANCE.getAxisSubsetType_Name();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ContactTypeImpl <em>Contact Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ContactTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getContactType()
		 * @generated
		 */
        EClass CONTACT_TYPE = eINSTANCE.getContactType();

        /**
		 * The meta object literal for the '<em><b>Phone</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CONTACT_TYPE__PHONE = eINSTANCE.getContactType_Phone();

        /**
		 * The meta object literal for the '<em><b>Address</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CONTACT_TYPE__ADDRESS = eINSTANCE.getContactType_Address();

        /**
		 * The meta object literal for the '<em><b>Online Resource</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CONTACT_TYPE__ONLINE_RESOURCE = eINSTANCE.getContactType_OnlineResource();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl <em>Content Metadata Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ContentMetadataTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getContentMetadataType()
		 * @generated
		 */
        EClass CONTENT_METADATA_TYPE = eINSTANCE.getContentMetadataType();

        /**
		 * The meta object literal for the '<em><b>Coverage Offering Brief</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF = eINSTANCE.getContentMetadataType_CoverageOfferingBrief();

        /**
		 * The meta object literal for the '<em><b>Actuate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__ACTUATE = eINSTANCE.getContentMetadataType_Actuate();

        /**
		 * The meta object literal for the '<em><b>Arcrole</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__ARCROLE = eINSTANCE.getContentMetadataType_Arcrole();

        /**
		 * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__HREF = eINSTANCE.getContentMetadataType_Href();

        /**
		 * The meta object literal for the '<em><b>Remote Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__REMOTE_SCHEMA = eINSTANCE.getContentMetadataType_RemoteSchema();

        /**
		 * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__ROLE = eINSTANCE.getContentMetadataType_Role();

        /**
		 * The meta object literal for the '<em><b>Show</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__SHOW = eINSTANCE.getContentMetadataType_Show();

        /**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__TITLE = eINSTANCE.getContentMetadataType_Title();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__TYPE = eINSTANCE.getContentMetadataType_Type();

        /**
		 * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__UPDATE_SEQUENCE = eINSTANCE.getContentMetadataType_UpdateSequence();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CONTENT_METADATA_TYPE__VERSION = eINSTANCE.getContentMetadataType_Version();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.CoverageDescriptionTypeImpl <em>Coverage Description Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.CoverageDescriptionTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCoverageDescriptionType()
		 * @generated
		 */
        EClass COVERAGE_DESCRIPTION_TYPE = eINSTANCE.getCoverageDescriptionType();

        /**
		 * The meta object literal for the '<em><b>Coverage Offering</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COVERAGE_DESCRIPTION_TYPE__COVERAGE_OFFERING = eINSTANCE.getCoverageDescriptionType_CoverageOffering();

        /**
		 * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute COVERAGE_DESCRIPTION_TYPE__UPDATE_SEQUENCE = eINSTANCE.getCoverageDescriptionType_UpdateSequence();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute COVERAGE_DESCRIPTION_TYPE__VERSION = eINSTANCE.getCoverageDescriptionType_Version();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.CoverageOfferingBriefTypeImpl <em>Coverage Offering Brief Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.CoverageOfferingBriefTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCoverageOfferingBriefType()
		 * @generated
		 */
        EClass COVERAGE_OFFERING_BRIEF_TYPE = eINSTANCE.getCoverageOfferingBriefType();

        /**
		 * The meta object literal for the '<em><b>Lon Lat Envelope</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COVERAGE_OFFERING_BRIEF_TYPE__LON_LAT_ENVELOPE = eINSTANCE.getCoverageOfferingBriefType_LonLatEnvelope();

        /**
		 * The meta object literal for the '<em><b>Keywords</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COVERAGE_OFFERING_BRIEF_TYPE__KEYWORDS = eINSTANCE.getCoverageOfferingBriefType_Keywords();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.CoverageOfferingTypeImpl <em>Coverage Offering Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.CoverageOfferingTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCoverageOfferingType()
		 * @generated
		 */
        EClass COVERAGE_OFFERING_TYPE = eINSTANCE.getCoverageOfferingType();

        /**
		 * The meta object literal for the '<em><b>Domain Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COVERAGE_OFFERING_TYPE__DOMAIN_SET = eINSTANCE.getCoverageOfferingType_DomainSet();

        /**
		 * The meta object literal for the '<em><b>Range Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COVERAGE_OFFERING_TYPE__RANGE_SET = eINSTANCE.getCoverageOfferingType_RangeSet();

        /**
		 * The meta object literal for the '<em><b>Supported CR Ss</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COVERAGE_OFFERING_TYPE__SUPPORTED_CR_SS = eINSTANCE.getCoverageOfferingType_SupportedCRSs();

        /**
		 * The meta object literal for the '<em><b>Supported Formats</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COVERAGE_OFFERING_TYPE__SUPPORTED_FORMATS = eINSTANCE.getCoverageOfferingType_SupportedFormats();

        /**
		 * The meta object literal for the '<em><b>Supported Interpolations</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference COVERAGE_OFFERING_TYPE__SUPPORTED_INTERPOLATIONS = eINSTANCE.getCoverageOfferingType_SupportedInterpolations();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.DCPTypeTypeImpl <em>DCP Type Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.DCPTypeTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDCPTypeType()
		 * @generated
		 */
        EClass DCP_TYPE_TYPE = eINSTANCE.getDCPTypeType();

        /**
		 * The meta object literal for the '<em><b>HTTP</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DCP_TYPE_TYPE__HTTP = eINSTANCE.getDCPTypeType_HTTP();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.DescribeCoverageTypeImpl <em>Describe Coverage Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.DescribeCoverageTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDescribeCoverageType()
		 * @generated
		 */
        EClass DESCRIBE_COVERAGE_TYPE = eINSTANCE.getDescribeCoverageType();

        /**
		 * The meta object literal for the '<em><b>Coverage</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DESCRIBE_COVERAGE_TYPE__COVERAGE = eINSTANCE.getDescribeCoverageType_Coverage();

        /**
		 * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DESCRIBE_COVERAGE_TYPE__SERVICE = eINSTANCE.getDescribeCoverageType_Service();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DESCRIBE_COVERAGE_TYPE__VERSION = eINSTANCE.getDescribeCoverageType_Version();

        /**
		 * The meta object literal for the '<em><b>Base Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESCRIBE_COVERAGE_TYPE__BASE_URL = eINSTANCE.getDescribeCoverageType_BaseUrl();

								/**
		 * The meta object literal for the '<em><b>Extended Properties</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESCRIBE_COVERAGE_TYPE__EXTENDED_PROPERTIES = eINSTANCE.getDescribeCoverageType_ExtendedProperties();

								/**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.DescribeCoverageType1Impl <em>Describe Coverage Type1</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.DescribeCoverageType1Impl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDescribeCoverageType1()
		 * @generated
		 */
        EClass DESCRIBE_COVERAGE_TYPE1 = eINSTANCE.getDescribeCoverageType1();

        /**
		 * The meta object literal for the '<em><b>DCP Type</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DESCRIBE_COVERAGE_TYPE1__DCP_TYPE = eINSTANCE.getDescribeCoverageType1_DCPType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.DocumentRootImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDocumentRoot()
		 * @generated
		 */
        EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

        /**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

        /**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

        /**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

        /**
		 * The meta object literal for the '<em><b>Axis Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__AXIS_DESCRIPTION = eINSTANCE.getDocumentRoot_AxisDescription();

        /**
		 * The meta object literal for the '<em><b>Axis Description1</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__AXIS_DESCRIPTION1 = eINSTANCE.getDocumentRoot_AxisDescription1();

        /**
		 * The meta object literal for the '<em><b>Capability</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__CAPABILITY = eINSTANCE.getDocumentRoot_Capability();

        /**
		 * The meta object literal for the '<em><b>Content Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__CONTENT_METADATA = eINSTANCE.getDocumentRoot_ContentMetadata();

        /**
		 * The meta object literal for the '<em><b>Coverage Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__COVERAGE_DESCRIPTION = eINSTANCE.getDocumentRoot_CoverageDescription();

        /**
		 * The meta object literal for the '<em><b>Coverage Offering</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__COVERAGE_OFFERING = eINSTANCE.getDocumentRoot_CoverageOffering();

        /**
		 * The meta object literal for the '<em><b>Coverage Offering Brief</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__COVERAGE_OFFERING_BRIEF = eINSTANCE.getDocumentRoot_CoverageOfferingBrief();

        /**
		 * The meta object literal for the '<em><b>Describe Coverage</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__DESCRIBE_COVERAGE = eINSTANCE.getDocumentRoot_DescribeCoverage();

        /**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DOCUMENT_ROOT__DESCRIPTION = eINSTANCE.getDocumentRoot_Description();

        /**
		 * The meta object literal for the '<em><b>Domain Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__DOMAIN_SET = eINSTANCE.getDocumentRoot_DomainSet();

        /**
		 * The meta object literal for the '<em><b>Formats</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__FORMATS = eINSTANCE.getDocumentRoot_Formats();

        /**
		 * The meta object literal for the '<em><b>Get Capabilities</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__GET_CAPABILITIES = eINSTANCE.getDocumentRoot_GetCapabilities();

        /**
		 * The meta object literal for the '<em><b>Get Coverage</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__GET_COVERAGE = eINSTANCE.getDocumentRoot_GetCoverage();

        /**
		 * The meta object literal for the '<em><b>Interpolation Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DOCUMENT_ROOT__INTERPOLATION_METHOD = eINSTANCE.getDocumentRoot_InterpolationMethod();

        /**
		 * The meta object literal for the '<em><b>Interval</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__INTERVAL = eINSTANCE.getDocumentRoot_Interval();

        /**
		 * The meta object literal for the '<em><b>Keywords</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__KEYWORDS = eINSTANCE.getDocumentRoot_Keywords();

        /**
		 * The meta object literal for the '<em><b>Lon Lat Envelope</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__LON_LAT_ENVELOPE = eINSTANCE.getDocumentRoot_LonLatEnvelope();

        /**
		 * The meta object literal for the '<em><b>Metadata Link</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__METADATA_LINK = eINSTANCE.getDocumentRoot_MetadataLink();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DOCUMENT_ROOT__NAME = eINSTANCE.getDocumentRoot_Name();

        /**
		 * The meta object literal for the '<em><b>Range Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__RANGE_SET = eINSTANCE.getDocumentRoot_RangeSet();

        /**
		 * The meta object literal for the '<em><b>Range Set1</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__RANGE_SET1 = eINSTANCE.getDocumentRoot_RangeSet1();

        /**
		 * The meta object literal for the '<em><b>Service</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__SERVICE = eINSTANCE.getDocumentRoot_Service();

        /**
		 * The meta object literal for the '<em><b>Single Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__SINGLE_VALUE = eINSTANCE.getDocumentRoot_SingleValue();

        /**
		 * The meta object literal for the '<em><b>Spatial Domain</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__SPATIAL_DOMAIN = eINSTANCE.getDocumentRoot_SpatialDomain();

        /**
		 * The meta object literal for the '<em><b>Spatial Subset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__SPATIAL_SUBSET = eINSTANCE.getDocumentRoot_SpatialSubset();

        /**
		 * The meta object literal for the '<em><b>Supported CR Ss</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__SUPPORTED_CR_SS = eINSTANCE.getDocumentRoot_SupportedCRSs();

        /**
		 * The meta object literal for the '<em><b>Supported Formats</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__SUPPORTED_FORMATS = eINSTANCE.getDocumentRoot_SupportedFormats();

        /**
		 * The meta object literal for the '<em><b>Supported Interpolations</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__SUPPORTED_INTERPOLATIONS = eINSTANCE.getDocumentRoot_SupportedInterpolations();

        /**
		 * The meta object literal for the '<em><b>Temporal Domain</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__TEMPORAL_DOMAIN = eINSTANCE.getDocumentRoot_TemporalDomain();

        /**
		 * The meta object literal for the '<em><b>Temporal Subset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__TEMPORAL_SUBSET = eINSTANCE.getDocumentRoot_TemporalSubset();

        /**
		 * The meta object literal for the '<em><b>Time Period</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__TIME_PERIOD = eINSTANCE.getDocumentRoot_TimePeriod();

        /**
		 * The meta object literal for the '<em><b>Time Sequence</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__TIME_SEQUENCE = eINSTANCE.getDocumentRoot_TimeSequence();

        /**
		 * The meta object literal for the '<em><b>WCS Capabilities</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOCUMENT_ROOT__WCS_CAPABILITIES = eINSTANCE.getDocumentRoot_WCSCapabilities();

        /**
		 * The meta object literal for the '<em><b>Closure</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DOCUMENT_ROOT__CLOSURE = eINSTANCE.getDocumentRoot_Closure();

        /**
		 * The meta object literal for the '<em><b>Semantic</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DOCUMENT_ROOT__SEMANTIC = eINSTANCE.getDocumentRoot_Semantic();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute DOCUMENT_ROOT__TYPE = eINSTANCE.getDocumentRoot_Type();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.DomainSetTypeImpl <em>Domain Set Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.DomainSetTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDomainSetType()
		 * @generated
		 */
        EClass DOMAIN_SET_TYPE = eINSTANCE.getDomainSetType();

        /**
		 * The meta object literal for the '<em><b>Spatial Domain</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOMAIN_SET_TYPE__SPATIAL_DOMAIN = eINSTANCE.getDomainSetType_SpatialDomain();

        /**
		 * The meta object literal for the '<em><b>Temporal Domain</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOMAIN_SET_TYPE__TEMPORAL_DOMAIN = eINSTANCE.getDomainSetType_TemporalDomain();

        /**
		 * The meta object literal for the '<em><b>Temporal Domain1</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOMAIN_SET_TYPE__TEMPORAL_DOMAIN1 = eINSTANCE.getDomainSetType_TemporalDomain1();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.DomainSubsetTypeImpl <em>Domain Subset Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.DomainSubsetTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getDomainSubsetType()
		 * @generated
		 */
        EClass DOMAIN_SUBSET_TYPE = eINSTANCE.getDomainSubsetType();

        /**
		 * The meta object literal for the '<em><b>Spatial Subset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOMAIN_SUBSET_TYPE__SPATIAL_SUBSET = eINSTANCE.getDomainSubsetType_SpatialSubset();

        /**
		 * The meta object literal for the '<em><b>Temporal Subset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET = eINSTANCE.getDomainSubsetType_TemporalSubset();

        /**
		 * The meta object literal for the '<em><b>Temporal Subset1</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET1 = eINSTANCE.getDomainSubsetType_TemporalSubset1();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ExceptionTypeImpl <em>Exception Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ExceptionTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getExceptionType()
		 * @generated
		 */
        EClass EXCEPTION_TYPE = eINSTANCE.getExceptionType();

        /**
		 * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute EXCEPTION_TYPE__FORMAT = eINSTANCE.getExceptionType_Format();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.GetCapabilitiesTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetCapabilitiesType()
		 * @generated
		 */
        EClass GET_CAPABILITIES_TYPE = eINSTANCE.getGetCapabilitiesType();

        /**
		 * The meta object literal for the '<em><b>Section</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute GET_CAPABILITIES_TYPE__SECTION = eINSTANCE.getGetCapabilitiesType_Section();

        /**
		 * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute GET_CAPABILITIES_TYPE__SERVICE = eINSTANCE.getGetCapabilitiesType_Service();

        /**
		 * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = eINSTANCE.getGetCapabilitiesType_UpdateSequence();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute GET_CAPABILITIES_TYPE__VERSION = eINSTANCE.getGetCapabilitiesType_Version();

        /**
		 * The meta object literal for the '<em><b>Base Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GET_CAPABILITIES_TYPE__BASE_URL = eINSTANCE.getGetCapabilitiesType_BaseUrl();

								/**
		 * The meta object literal for the '<em><b>Extended Properties</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES = eINSTANCE.getGetCapabilitiesType_ExtendedProperties();

								/**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.GetCapabilitiesType1Impl <em>Get Capabilities Type1</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.GetCapabilitiesType1Impl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetCapabilitiesType1()
		 * @generated
		 */
        EClass GET_CAPABILITIES_TYPE1 = eINSTANCE.getGetCapabilitiesType1();

        /**
		 * The meta object literal for the '<em><b>DCP Type</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference GET_CAPABILITIES_TYPE1__DCP_TYPE = eINSTANCE.getGetCapabilitiesType1_DCPType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.GetCoverageTypeImpl <em>Get Coverage Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.GetCoverageTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetCoverageType()
		 * @generated
		 */
        EClass GET_COVERAGE_TYPE = eINSTANCE.getGetCoverageType();

        /**
		 * The meta object literal for the '<em><b>Source Coverage</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute GET_COVERAGE_TYPE__SOURCE_COVERAGE = eINSTANCE.getGetCoverageType_SourceCoverage();

        /**
		 * The meta object literal for the '<em><b>Domain Subset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference GET_COVERAGE_TYPE__DOMAIN_SUBSET = eINSTANCE.getGetCoverageType_DomainSubset();

        /**
		 * The meta object literal for the '<em><b>Range Subset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference GET_COVERAGE_TYPE__RANGE_SUBSET = eINSTANCE.getGetCoverageType_RangeSubset();

        /**
		 * The meta object literal for the '<em><b>Interpolation Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute GET_COVERAGE_TYPE__INTERPOLATION_METHOD = eINSTANCE.getGetCoverageType_InterpolationMethod();

        /**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference GET_COVERAGE_TYPE__OUTPUT = eINSTANCE.getGetCoverageType_Output();

        /**
		 * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute GET_COVERAGE_TYPE__SERVICE = eINSTANCE.getGetCoverageType_Service();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute GET_COVERAGE_TYPE__VERSION = eINSTANCE.getGetCoverageType_Version();

        /**
		 * The meta object literal for the '<em><b>Base Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GET_COVERAGE_TYPE__BASE_URL = eINSTANCE.getGetCoverageType_BaseUrl();

								/**
		 * The meta object literal for the '<em><b>Extended Properties</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GET_COVERAGE_TYPE__EXTENDED_PROPERTIES = eINSTANCE.getGetCoverageType_ExtendedProperties();

								/**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.GetCoverageType1Impl <em>Get Coverage Type1</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.GetCoverageType1Impl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetCoverageType1()
		 * @generated
		 */
        EClass GET_COVERAGE_TYPE1 = eINSTANCE.getGetCoverageType1();

        /**
		 * The meta object literal for the '<em><b>DCP Type</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference GET_COVERAGE_TYPE1__DCP_TYPE = eINSTANCE.getGetCoverageType1_DCPType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.GetTypeImpl <em>Get Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.GetTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGetType()
		 * @generated
		 */
        EClass GET_TYPE = eINSTANCE.getGetType();

        /**
		 * The meta object literal for the '<em><b>Online Resource</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference GET_TYPE__ONLINE_RESOURCE = eINSTANCE.getGetType_OnlineResource();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.HTTPTypeImpl <em>HTTP Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.HTTPTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getHTTPType()
		 * @generated
		 */
        EClass HTTP_TYPE = eINSTANCE.getHTTPType();

        /**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute HTTP_TYPE__GROUP = eINSTANCE.getHTTPType_Group();

        /**
		 * The meta object literal for the '<em><b>Get</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference HTTP_TYPE__GET = eINSTANCE.getHTTPType_Get();

        /**
		 * The meta object literal for the '<em><b>Post</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference HTTP_TYPE__POST = eINSTANCE.getHTTPType_Post();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.IntervalTypeImpl <em>Interval Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.IntervalTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getIntervalType()
		 * @generated
		 */
        EClass INTERVAL_TYPE = eINSTANCE.getIntervalType();

        /**
		 * The meta object literal for the '<em><b>Res</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference INTERVAL_TYPE__RES = eINSTANCE.getIntervalType_Res();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.KeywordsTypeImpl <em>Keywords Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.KeywordsTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getKeywordsType()
		 * @generated
		 */
        EClass KEYWORDS_TYPE = eINSTANCE.getKeywordsType();

        /**
		 * The meta object literal for the '<em><b>Keyword</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute KEYWORDS_TYPE__KEYWORD = eINSTANCE.getKeywordsType_Keyword();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference KEYWORDS_TYPE__TYPE = eINSTANCE.getKeywordsType_Type();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.LonLatEnvelopeBaseTypeImpl <em>Lon Lat Envelope Base Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.LonLatEnvelopeBaseTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getLonLatEnvelopeBaseType()
		 * @generated
		 */
        EClass LON_LAT_ENVELOPE_BASE_TYPE = eINSTANCE.getLonLatEnvelopeBaseType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.LonLatEnvelopeTypeImpl <em>Lon Lat Envelope Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.LonLatEnvelopeTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getLonLatEnvelopeType()
		 * @generated
		 */
        EClass LON_LAT_ENVELOPE_TYPE = eINSTANCE.getLonLatEnvelopeType();

        /**
		 * The meta object literal for the '<em><b>Time Position</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference LON_LAT_ENVELOPE_TYPE__TIME_POSITION = eINSTANCE.getLonLatEnvelopeType_TimePosition();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.MetadataAssociationTypeImpl <em>Metadata Association Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.MetadataAssociationTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMetadataAssociationType()
		 * @generated
		 */
        EClass METADATA_ASSOCIATION_TYPE = eINSTANCE.getMetadataAssociationType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.MetadataLinkTypeImpl <em>Metadata Link Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.MetadataLinkTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMetadataLinkType()
		 * @generated
		 */
        EClass METADATA_LINK_TYPE = eINSTANCE.getMetadataLinkType();

        /**
		 * The meta object literal for the '<em><b>Metadata Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute METADATA_LINK_TYPE__METADATA_TYPE = eINSTANCE.getMetadataLinkType_MetadataType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.OnlineResourceTypeImpl <em>Online Resource Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.OnlineResourceTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getOnlineResourceType()
		 * @generated
		 */
        EClass ONLINE_RESOURCE_TYPE = eINSTANCE.getOnlineResourceType();

        /**
		 * The meta object literal for the '<em><b>Actuate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ONLINE_RESOURCE_TYPE__ACTUATE = eINSTANCE.getOnlineResourceType_Actuate();

        /**
		 * The meta object literal for the '<em><b>Arcrole</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ONLINE_RESOURCE_TYPE__ARCROLE = eINSTANCE.getOnlineResourceType_Arcrole();

        /**
		 * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ONLINE_RESOURCE_TYPE__HREF = eINSTANCE.getOnlineResourceType_Href();

        /**
		 * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ONLINE_RESOURCE_TYPE__ROLE = eINSTANCE.getOnlineResourceType_Role();

        /**
		 * The meta object literal for the '<em><b>Show</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ONLINE_RESOURCE_TYPE__SHOW = eINSTANCE.getOnlineResourceType_Show();

        /**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ONLINE_RESOURCE_TYPE__TITLE = eINSTANCE.getOnlineResourceType_Title();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ONLINE_RESOURCE_TYPE__TYPE = eINSTANCE.getOnlineResourceType_Type();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.OutputTypeImpl <em>Output Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.OutputTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getOutputType()
		 * @generated
		 */
        EClass OUTPUT_TYPE = eINSTANCE.getOutputType();

        /**
		 * The meta object literal for the '<em><b>Crs</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference OUTPUT_TYPE__CRS = eINSTANCE.getOutputType_Crs();

        /**
		 * The meta object literal for the '<em><b>Format</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference OUTPUT_TYPE__FORMAT = eINSTANCE.getOutputType_Format();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.PostTypeImpl <em>Post Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.PostTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getPostType()
		 * @generated
		 */
        EClass POST_TYPE = eINSTANCE.getPostType();

        /**
		 * The meta object literal for the '<em><b>Online Resource</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference POST_TYPE__ONLINE_RESOURCE = eINSTANCE.getPostType_OnlineResource();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.RangeSetTypeImpl <em>Range Set Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.RangeSetTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getRangeSetType()
		 * @generated
		 */
        EClass RANGE_SET_TYPE = eINSTANCE.getRangeSetType();

        /**
		 * The meta object literal for the '<em><b>Axis Description</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference RANGE_SET_TYPE__AXIS_DESCRIPTION = eINSTANCE.getRangeSetType_AxisDescription();

        /**
		 * The meta object literal for the '<em><b>Null Values</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference RANGE_SET_TYPE__NULL_VALUES = eINSTANCE.getRangeSetType_NullValues();

        /**
		 * The meta object literal for the '<em><b>Ref Sys</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute RANGE_SET_TYPE__REF_SYS = eINSTANCE.getRangeSetType_RefSys();

        /**
		 * The meta object literal for the '<em><b>Ref Sys Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute RANGE_SET_TYPE__REF_SYS_LABEL = eINSTANCE.getRangeSetType_RefSysLabel();

        /**
		 * The meta object literal for the '<em><b>Semantic</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute RANGE_SET_TYPE__SEMANTIC = eINSTANCE.getRangeSetType_Semantic();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.RangeSetType1Impl <em>Range Set Type1</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.RangeSetType1Impl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getRangeSetType1()
		 * @generated
		 */
        EClass RANGE_SET_TYPE1 = eINSTANCE.getRangeSetType1();

        /**
		 * The meta object literal for the '<em><b>Range Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference RANGE_SET_TYPE1__RANGE_SET = eINSTANCE.getRangeSetType1_RangeSet();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.RangeSubsetTypeImpl <em>Range Subset Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.RangeSubsetTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getRangeSubsetType()
		 * @generated
		 */
        EClass RANGE_SUBSET_TYPE = eINSTANCE.getRangeSubsetType();

        /**
		 * The meta object literal for the '<em><b>Axis Subset</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference RANGE_SUBSET_TYPE__AXIS_SUBSET = eINSTANCE.getRangeSubsetType_AxisSubset();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.RequestTypeImpl <em>Request Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.RequestTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getRequestType()
		 * @generated
		 */
        EClass REQUEST_TYPE = eINSTANCE.getRequestType();

        /**
		 * The meta object literal for the '<em><b>Get Capabilities</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference REQUEST_TYPE__GET_CAPABILITIES = eINSTANCE.getRequestType_GetCapabilities();

        /**
		 * The meta object literal for the '<em><b>Describe Coverage</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference REQUEST_TYPE__DESCRIBE_COVERAGE = eINSTANCE.getRequestType_DescribeCoverage();

        /**
		 * The meta object literal for the '<em><b>Get Coverage</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference REQUEST_TYPE__GET_COVERAGE = eINSTANCE.getRequestType_GetCoverage();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ResponsiblePartyTypeImpl <em>Responsible Party Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ResponsiblePartyTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getResponsiblePartyType()
		 * @generated
		 */
        EClass RESPONSIBLE_PARTY_TYPE = eINSTANCE.getResponsiblePartyType();

        /**
		 * The meta object literal for the '<em><b>Individual Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute RESPONSIBLE_PARTY_TYPE__INDIVIDUAL_NAME = eINSTANCE.getResponsiblePartyType_IndividualName();

        /**
		 * The meta object literal for the '<em><b>Organisation Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME = eINSTANCE.getResponsiblePartyType_OrganisationName();

        /**
		 * The meta object literal for the '<em><b>Organisation Name1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute RESPONSIBLE_PARTY_TYPE__ORGANISATION_NAME1 = eINSTANCE.getResponsiblePartyType_OrganisationName1();

        /**
		 * The meta object literal for the '<em><b>Position Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute RESPONSIBLE_PARTY_TYPE__POSITION_NAME = eINSTANCE.getResponsiblePartyType_PositionName();

        /**
		 * The meta object literal for the '<em><b>Contact Info</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference RESPONSIBLE_PARTY_TYPE__CONTACT_INFO = eINSTANCE.getResponsiblePartyType_ContactInfo();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ServiceTypeImpl <em>Service Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ServiceTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getServiceType()
		 * @generated
		 */
        EClass SERVICE_TYPE = eINSTANCE.getServiceType();

        /**
		 * The meta object literal for the '<em><b>Keywords</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SERVICE_TYPE__KEYWORDS = eINSTANCE.getServiceType_Keywords();

        /**
		 * The meta object literal for the '<em><b>Responsible Party</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SERVICE_TYPE__RESPONSIBLE_PARTY = eINSTANCE.getServiceType_ResponsibleParty();

        /**
		 * The meta object literal for the '<em><b>Fees</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SERVICE_TYPE__FEES = eINSTANCE.getServiceType_Fees();

        /**
		 * The meta object literal for the '<em><b>Access Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SERVICE_TYPE__ACCESS_CONSTRAINTS = eINSTANCE.getServiceType_AccessConstraints();

        /**
		 * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SERVICE_TYPE__UPDATE_SEQUENCE = eINSTANCE.getServiceType_UpdateSequence();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SERVICE_TYPE__VERSION = eINSTANCE.getServiceType_Version();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.SpatialDomainTypeImpl <em>Spatial Domain Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.SpatialDomainTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSpatialDomainType()
		 * @generated
		 */
        EClass SPATIAL_DOMAIN_TYPE = eINSTANCE.getSpatialDomainType();

        /**
		 * The meta object literal for the '<em><b>Envelope</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SPATIAL_DOMAIN_TYPE__ENVELOPE = eINSTANCE.getSpatialDomainType_Envelope();

        /**
		 * The meta object literal for the '<em><b>Grid Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SPATIAL_DOMAIN_TYPE__GRID_GROUP = eINSTANCE.getSpatialDomainType_GridGroup();

        /**
		 * The meta object literal for the '<em><b>Grid</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SPATIAL_DOMAIN_TYPE__GRID = eINSTANCE.getSpatialDomainType_Grid();

        /**
		 * The meta object literal for the '<em><b>Polygon</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SPATIAL_DOMAIN_TYPE__POLYGON = eINSTANCE.getSpatialDomainType_Polygon();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.SpatialSubsetTypeImpl <em>Spatial Subset Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.SpatialSubsetTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSpatialSubsetType()
		 * @generated
		 */
        EClass SPATIAL_SUBSET_TYPE = eINSTANCE.getSpatialSubsetType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.SupportedCRSsTypeImpl <em>Supported CR Ss Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.SupportedCRSsTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSupportedCRSsType()
		 * @generated
		 */
        EClass SUPPORTED_CR_SS_TYPE = eINSTANCE.getSupportedCRSsType();

        /**
		 * The meta object literal for the '<em><b>Request Response CR Ss</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SUPPORTED_CR_SS_TYPE__REQUEST_RESPONSE_CR_SS = eINSTANCE.getSupportedCRSsType_RequestResponseCRSs();

        /**
		 * The meta object literal for the '<em><b>Request CR Ss</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SUPPORTED_CR_SS_TYPE__REQUEST_CR_SS = eINSTANCE.getSupportedCRSsType_RequestCRSs();

        /**
		 * The meta object literal for the '<em><b>Response CR Ss</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SUPPORTED_CR_SS_TYPE__RESPONSE_CR_SS = eINSTANCE.getSupportedCRSsType_ResponseCRSs();

        /**
		 * The meta object literal for the '<em><b>Native CR Ss</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SUPPORTED_CR_SS_TYPE__NATIVE_CR_SS = eINSTANCE.getSupportedCRSsType_NativeCRSs();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.SupportedFormatsTypeImpl <em>Supported Formats Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.SupportedFormatsTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSupportedFormatsType()
		 * @generated
		 */
        EClass SUPPORTED_FORMATS_TYPE = eINSTANCE.getSupportedFormatsType();

        /**
		 * The meta object literal for the '<em><b>Formats</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SUPPORTED_FORMATS_TYPE__FORMATS = eINSTANCE.getSupportedFormatsType_Formats();

        /**
		 * The meta object literal for the '<em><b>Native Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SUPPORTED_FORMATS_TYPE__NATIVE_FORMAT = eINSTANCE.getSupportedFormatsType_NativeFormat();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.SupportedInterpolationsTypeImpl <em>Supported Interpolations Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.SupportedInterpolationsTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getSupportedInterpolationsType()
		 * @generated
		 */
        EClass SUPPORTED_INTERPOLATIONS_TYPE = eINSTANCE.getSupportedInterpolationsType();

        /**
		 * The meta object literal for the '<em><b>Interpolation Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SUPPORTED_INTERPOLATIONS_TYPE__INTERPOLATION_METHOD = eINSTANCE.getSupportedInterpolationsType_InterpolationMethod();

        /**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SUPPORTED_INTERPOLATIONS_TYPE__DEFAULT = eINSTANCE.getSupportedInterpolationsType_Default();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.TelephoneTypeImpl <em>Telephone Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.TelephoneTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getTelephoneType()
		 * @generated
		 */
        EClass TELEPHONE_TYPE = eINSTANCE.getTelephoneType();

        /**
		 * The meta object literal for the '<em><b>Voice</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TELEPHONE_TYPE__VOICE = eINSTANCE.getTelephoneType_Voice();

        /**
		 * The meta object literal for the '<em><b>Facsimile</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TELEPHONE_TYPE__FACSIMILE = eINSTANCE.getTelephoneType_Facsimile();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.TimePeriodTypeImpl <em>Time Period Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.TimePeriodTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getTimePeriodType()
		 * @generated
		 */
        EClass TIME_PERIOD_TYPE = eINSTANCE.getTimePeriodType();

        /**
		 * The meta object literal for the '<em><b>Begin Position</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TIME_PERIOD_TYPE__BEGIN_POSITION = eINSTANCE.getTimePeriodType_BeginPosition();

        /**
		 * The meta object literal for the '<em><b>End Position</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TIME_PERIOD_TYPE__END_POSITION = eINSTANCE.getTimePeriodType_EndPosition();

        /**
		 * The meta object literal for the '<em><b>Time Resolution</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TIME_PERIOD_TYPE__TIME_RESOLUTION = eINSTANCE.getTimePeriodType_TimeResolution();

        /**
		 * The meta object literal for the '<em><b>Frame</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TIME_PERIOD_TYPE__FRAME = eINSTANCE.getTimePeriodType_Frame();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.TimeSequenceTypeImpl <em>Time Sequence Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.TimeSequenceTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getTimeSequenceType()
		 * @generated
		 */
        EClass TIME_SEQUENCE_TYPE = eINSTANCE.getTimeSequenceType();

        /**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TIME_SEQUENCE_TYPE__GROUP = eINSTANCE.getTimeSequenceType_Group();

        /**
		 * The meta object literal for the '<em><b>Time Position</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TIME_SEQUENCE_TYPE__TIME_POSITION = eINSTANCE.getTimeSequenceType_TimePosition();

        /**
		 * The meta object literal for the '<em><b>Time Period</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference TIME_SEQUENCE_TYPE__TIME_PERIOD = eINSTANCE.getTimeSequenceType_TimePeriod();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.TypedLiteralTypeImpl <em>Typed Literal Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.TypedLiteralTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getTypedLiteralType()
		 * @generated
		 */
        EClass TYPED_LITERAL_TYPE = eINSTANCE.getTypedLiteralType();

        /**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TYPED_LITERAL_TYPE__VALUE = eINSTANCE.getTypedLiteralType_Value();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TYPED_LITERAL_TYPE__TYPE = eINSTANCE.getTypedLiteralType_Type();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ValueEnumBaseTypeImpl <em>Value Enum Base Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ValueEnumBaseTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getValueEnumBaseType()
		 * @generated
		 */
        EClass VALUE_ENUM_BASE_TYPE = eINSTANCE.getValueEnumBaseType();

        /**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute VALUE_ENUM_BASE_TYPE__GROUP = eINSTANCE.getValueEnumBaseType_Group();

        /**
		 * The meta object literal for the '<em><b>Interval</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference VALUE_ENUM_BASE_TYPE__INTERVAL = eINSTANCE.getValueEnumBaseType_Interval();

        /**
		 * The meta object literal for the '<em><b>Single Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference VALUE_ENUM_BASE_TYPE__SINGLE_VALUE = eINSTANCE.getValueEnumBaseType_SingleValue();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ValueEnumTypeImpl <em>Value Enum Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ValueEnumTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getValueEnumType()
		 * @generated
		 */
        EClass VALUE_ENUM_TYPE = eINSTANCE.getValueEnumType();

        /**
		 * The meta object literal for the '<em><b>Semantic</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute VALUE_ENUM_TYPE__SEMANTIC = eINSTANCE.getValueEnumType_Semantic();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute VALUE_ENUM_TYPE__TYPE = eINSTANCE.getValueEnumType_Type();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ValueRangeTypeImpl <em>Value Range Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ValueRangeTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getValueRangeType()
		 * @generated
		 */
        EClass VALUE_RANGE_TYPE = eINSTANCE.getValueRangeType();

        /**
		 * The meta object literal for the '<em><b>Min</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference VALUE_RANGE_TYPE__MIN = eINSTANCE.getValueRangeType_Min();

        /**
		 * The meta object literal for the '<em><b>Max</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference VALUE_RANGE_TYPE__MAX = eINSTANCE.getValueRangeType_Max();

        /**
		 * The meta object literal for the '<em><b>Atomic</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute VALUE_RANGE_TYPE__ATOMIC = eINSTANCE.getValueRangeType_Atomic();

        /**
		 * The meta object literal for the '<em><b>Closure</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute VALUE_RANGE_TYPE__CLOSURE = eINSTANCE.getValueRangeType_Closure();

        /**
		 * The meta object literal for the '<em><b>Semantic</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute VALUE_RANGE_TYPE__SEMANTIC = eINSTANCE.getValueRangeType_Semantic();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute VALUE_RANGE_TYPE__TYPE = eINSTANCE.getValueRangeType_Type();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.ValuesTypeImpl <em>Values Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.ValuesTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getValuesType()
		 * @generated
		 */
        EClass VALUES_TYPE = eINSTANCE.getValuesType();

        /**
		 * The meta object literal for the '<em><b>Default</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference VALUES_TYPE__DEFAULT = eINSTANCE.getValuesType_Default();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.VendorSpecificCapabilitiesTypeImpl <em>Vendor Specific Capabilities Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.VendorSpecificCapabilitiesTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getVendorSpecificCapabilitiesType()
		 * @generated
		 */
        EClass VENDOR_SPECIFIC_CAPABILITIES_TYPE = eINSTANCE.getVendorSpecificCapabilitiesType();

        /**
		 * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute VENDOR_SPECIFIC_CAPABILITIES_TYPE__ANY = eINSTANCE.getVendorSpecificCapabilitiesType_Any();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl <em>WCS Capabilities Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getWCSCapabilitiesType()
		 * @generated
		 */
        EClass WCS_CAPABILITIES_TYPE = eINSTANCE.getWCSCapabilitiesType();

        /**
		 * The meta object literal for the '<em><b>Service</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference WCS_CAPABILITIES_TYPE__SERVICE = eINSTANCE.getWCSCapabilitiesType_Service();

        /**
		 * The meta object literal for the '<em><b>Capability</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference WCS_CAPABILITIES_TYPE__CAPABILITY = eINSTANCE.getWCSCapabilitiesType_Capability();

        /**
		 * The meta object literal for the '<em><b>Content Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference WCS_CAPABILITIES_TYPE__CONTENT_METADATA = eINSTANCE.getWCSCapabilitiesType_ContentMetadata();

        /**
		 * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WCS_CAPABILITIES_TYPE__UPDATE_SEQUENCE = eINSTANCE.getWCSCapabilitiesType_UpdateSequence();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WCS_CAPABILITIES_TYPE__VERSION = eINSTANCE.getWCSCapabilitiesType_Version();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.impl.WCSCapabilityTypeImpl <em>WCS Capability Type</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.impl.WCSCapabilityTypeImpl
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getWCSCapabilityType()
		 * @generated
		 */
        EClass WCS_CAPABILITY_TYPE = eINSTANCE.getWCSCapabilityType();

        /**
		 * The meta object literal for the '<em><b>Request</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference WCS_CAPABILITY_TYPE__REQUEST = eINSTANCE.getWCSCapabilityType_Request();

        /**
		 * The meta object literal for the '<em><b>Exception</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference WCS_CAPABILITY_TYPE__EXCEPTION = eINSTANCE.getWCSCapabilityType_Exception();

        /**
		 * The meta object literal for the '<em><b>Vendor Specific Capabilities</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES = eINSTANCE.getWCSCapabilityType_VendorSpecificCapabilities();

        /**
		 * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WCS_CAPABILITY_TYPE__UPDATE_SEQUENCE = eINSTANCE.getWCSCapabilityType_UpdateSequence();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WCS_CAPABILITY_TYPE__VERSION = eINSTANCE.getWCSCapabilityType_Version();

        /**
		 * The meta object literal for the '{@link com.vividsolutions.jts.geom.Envelope <em>Envelope</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.vividsolutions.jts.geom.Envelope
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getEnvelope()
		 * @generated
		 */
		EClass ENVELOPE = eINSTANCE.getEnvelope();

								/**
		 * The meta object literal for the '{@link org.geotools.geometry.GeneralEnvelope <em>General Envelope</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.geotools.geometry.GeneralEnvelope
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getGeneralEnvelope()
		 * @generated
		 */
		EClass GENERAL_ENVELOPE = eINSTANCE.getGeneralEnvelope();

								/**
		 * The meta object literal for the '{@link net.opengis.wcs10.CapabilitiesSectionType <em>Capabilities Section Type</em>}' enum.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.CapabilitiesSectionType
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCapabilitiesSectionType()
		 * @generated
		 */
        EEnum CAPABILITIES_SECTION_TYPE = eINSTANCE.getCapabilitiesSectionType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.ClosureType <em>Closure Type</em>}' enum.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.ClosureType
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getClosureType()
		 * @generated
		 */
        EEnum CLOSURE_TYPE = eINSTANCE.getClosureType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.InterpolationMethodType <em>Interpolation Method Type</em>}' enum.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.InterpolationMethodType
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getInterpolationMethodType()
		 * @generated
		 */
        EEnum INTERPOLATION_METHOD_TYPE = eINSTANCE.getInterpolationMethodType();

        /**
		 * The meta object literal for the '{@link net.opengis.wcs10.MetadataTypeType <em>Metadata Type Type</em>}' enum.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.MetadataTypeType
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMetadataTypeType()
		 * @generated
		 */
        EEnum METADATA_TYPE_TYPE = eINSTANCE.getMetadataTypeType();

        /**
		 * The meta object literal for the '<em>Capabilities Section Type Object</em>' data type.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.CapabilitiesSectionType
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getCapabilitiesSectionTypeObject()
		 * @generated
		 */
        EDataType CAPABILITIES_SECTION_TYPE_OBJECT = eINSTANCE.getCapabilitiesSectionTypeObject();

        /**
		 * The meta object literal for the '<em>Closure Type Object</em>' data type.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.ClosureType
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getClosureTypeObject()
		 * @generated
		 */
        EDataType CLOSURE_TYPE_OBJECT = eINSTANCE.getClosureTypeObject();

        /**
		 * The meta object literal for the '<em>Interpolation Method Type Object</em>' data type.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.InterpolationMethodType
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getInterpolationMethodTypeObject()
		 * @generated
		 */
        EDataType INTERPOLATION_METHOD_TYPE_OBJECT = eINSTANCE.getInterpolationMethodTypeObject();

        /**
		 * The meta object literal for the '<em>Metadata Type Type Object</em>' data type.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see net.opengis.wcs10.MetadataTypeType
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMetadataTypeTypeObject()
		 * @generated
		 */
        EDataType METADATA_TYPE_TYPE_OBJECT = eINSTANCE.getMetadataTypeTypeObject();

								/**
		 * The meta object literal for the '<em>Map</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.Map
		 * @see net.opengis.wcs10.impl.Wcs10PackageImpl#getMap()
		 * @generated
		 */
		EDataType MAP = eINSTANCE.getMap();

    }

} //Wcs10Package
