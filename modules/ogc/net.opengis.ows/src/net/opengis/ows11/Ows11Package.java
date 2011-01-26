/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

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
 * This XML Schema Document encodes the parts of ISO 19115 used by the common "ServiceIdentification" and "ServiceProvider" sections of the GetCapabilities operation response, known as the service metadata XML document. The parts encoded here are the MD_Keywords, CI_ResponsibleParty, and related classes. The UML package prefixes were omitted from XML names, and the XML element names were all capitalized, for consistency with other OWS Schemas. This document also provides a simple coding of text in multiple languages, simplified from Annex J of ISO 19115.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 
 *    See http://www.w3.org/XML/1998/namespace.html and
 *    http://www.w3.org/TR/REC-xml for information about this namespace.
 * 
 *     This schema document describes the XML namespace, in a form
 *     suitable for import by other schema documents.
 * 
 *     Note that local names in this namespace are intended to be defined
 *     only by the World Wide Web Consortium or its subgroups.  The
 *     following names are currently defined in this namespace and should
 *     not be used with conflicting semantics by any Working Group,
 *     specification, or document instance:
 * 
 *     base (as an attribute name): denotes an attribute whose value
 *          provides a URI to be used as the base for interpreting any
 *          relative URIs in the scope of the element on which it
 *          appears; its value is inherited.  This name is reserved
 *          by virtue of its definition in the XML Base specification.
 * 
 *     id   (as an attribute name): denotes an attribute whose value
 *          should be interpreted as if declared to be of type ID.
 *          This name is reserved by virtue of its definition in the
 *          xml:id specification.
 * 
 *     lang (as an attribute name): denotes an attribute whose value
 *          is a language code for the natural language of the content of
 *          any element; its value is inherited.  This name is reserved
 *          by virtue of its definition in the XML specification.
 * 
 *     space (as an attribute name): denotes an attribute whose
 *          value is a keyword indicating what whitespace processing
 *          discipline is intended for the content of the element; its
 *          value is inherited.  This name is reserved by virtue of its
 *          definition in the XML specification.
 * 
 *     Father (in any context at all): denotes Jon Bosak, the chair of
 *          the original XML Working Group.  This name is reserved by
 *          the following decision of the W3C XML Plenary and
 *          XML Coordination groups:
 * 
 *              In appreciation for his vision, leadership and dedication
 *              the W3C XML Plenary on this 10th day of February, 2000
 *              reserves for Jon Bosak in perpetuity the XML name
 *              xml:Father
 * 
 * This schema defines attributes and an attribute group
 *         suitable for use by
 *         schemas wishing to allow xml:base, xml:lang, xml:space or xml:id
 *         attributes on elements they define.
 * 
 *         To enable this, such a schema must import this schema
 *         for the XML namespace, e.g. as follows:
 *         &lt;schema . . .&gt;
 *          . . .
 *          &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                     schemaLocation="http://www.w3.org/2001/xml.xsd"/&gt;
 * 
 *         Subsequently, qualified reference to any of the attributes
 *         or the group defined below will have the desired effect, e.g.
 * 
 *         &lt;type . . .&gt;
 *          . . .
 *          &lt;attributeGroup ref="xml:specialAttrs"/&gt;
 * 
 *          will define a type which will schema-validate an instance
 *          element with any of those attributes
 * In keeping with the XML Schema WG's standard versioning
 *    policy, this schema document will persist at
 *    http://www.w3.org/2007/08/xml.xsd.
 *    At the date of issue it can also be found at
 *    http://www.w3.org/2001/xml.xsd.
 *    The schema document at that URI may however change in the future,
 *    in order to remain compatible with the latest version of XML Schema
 *    itself, or with the XML namespace itself.  In other words, if the XML
 *    Schema or XML namespaces change, the version of this document at
 *    http://www.w3.org/2001/xml.xsd will change
 *    accordingly; the version at
 *    http://www.w3.org/2007/08/xml.xsd will not change.
 * <!-- end-model-doc -->
 * @see net.opengis.ows11.Ows11Factory
 * @model kind="package"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface Ows11Package extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "ows11";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.opengis.net/ows/1.1";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "ows";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    Ows11Package eINSTANCE = net.opengis.ows11.impl.Ows11PackageImpl.init();

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl <em>Abstract Reference Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getAbstractReferenceBaseType()
     * @generated
     */
    int ABSTRACT_REFERENCE_BASE_TYPE = 0;

    /**
     * The feature id for the '<em><b>Actuate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE = 0;

    /**
     * The feature id for the '<em><b>Arcrole</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE = 1;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__HREF = 2;

    /**
     * The feature id for the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__ROLE = 3;

    /**
     * The feature id for the '<em><b>Show</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__SHOW = 4;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__TITLE = 5;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__TYPE = 6;

    /**
     * The number of structural features of the '<em>Abstract Reference Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.AcceptFormatsTypeImpl <em>Accept Formats Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.AcceptFormatsTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getAcceptFormatsType()
     * @generated
     */
    int ACCEPT_FORMATS_TYPE = 1;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT = 0;

    /**
     * The number of structural features of the '<em>Accept Formats Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCEPT_FORMATS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.AcceptVersionsTypeImpl <em>Accept Versions Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.AcceptVersionsTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getAcceptVersionsType()
     * @generated
     */
    int ACCEPT_VERSIONS_TYPE = 2;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCEPT_VERSIONS_TYPE__VERSION = 0;

    /**
     * The number of structural features of the '<em>Accept Versions Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCEPT_VERSIONS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.AddressTypeImpl <em>Address Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.AddressTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getAddressType()
     * @generated
     */
    int ADDRESS_TYPE = 3;

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
     * The meta object id for the '{@link net.opengis.ows11.impl.AllowedValuesTypeImpl <em>Allowed Values Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.AllowedValuesTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getAllowedValuesType()
     * @generated
     */
    int ALLOWED_VALUES_TYPE = 4;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOWED_VALUES_TYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOWED_VALUES_TYPE__VALUE = 1;

    /**
     * The feature id for the '<em><b>Range</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOWED_VALUES_TYPE__RANGE = 2;

    /**
     * The number of structural features of the '<em>Allowed Values Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOWED_VALUES_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.AnyValueTypeImpl <em>Any Value Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.AnyValueTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getAnyValueType()
     * @generated
     */
    int ANY_VALUE_TYPE = 5;

    /**
     * The number of structural features of the '<em>Any Value Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANY_VALUE_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.DescriptionTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getDescriptionType()
     * @generated
     */
    int DESCRIPTION_TYPE = 14;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__TITLE = 0;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__ABSTRACT = 1;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__KEYWORDS = 2;

    /**
     * The number of structural features of the '<em>Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.BasicIdentificationTypeImpl <em>Basic Identification Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.BasicIdentificationTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getBasicIdentificationType()
     * @generated
     */
    int BASIC_IDENTIFICATION_TYPE = 6;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__IDENTIFIER = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__METADATA = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Basic Identification Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.BoundingBoxTypeImpl <em>Bounding Box Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.BoundingBoxTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getBoundingBoxType()
     * @generated
     */
    int BOUNDING_BOX_TYPE = 7;

    /**
     * The feature id for the '<em><b>Lower Corner</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDING_BOX_TYPE__LOWER_CORNER = 0;

    /**
     * The feature id for the '<em><b>Upper Corner</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDING_BOX_TYPE__UPPER_CORNER = 1;

    /**
     * The feature id for the '<em><b>Crs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDING_BOX_TYPE__CRS = 2;

    /**
     * The feature id for the '<em><b>Dimensions</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDING_BOX_TYPE__DIMENSIONS = 3;

    /**
     * The number of structural features of the '<em>Bounding Box Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BOUNDING_BOX_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.CapabilitiesBaseTypeImpl <em>Capabilities Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.CapabilitiesBaseTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getCapabilitiesBaseType()
     * @generated
     */
    int CAPABILITIES_BASE_TYPE = 8;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION = 0;

    /**
     * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER = 1;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA = 2;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE = 3;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__VERSION = 4;

    /**
     * The number of structural features of the '<em>Capabilities Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.CodeTypeImpl <em>Code Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.CodeTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getCodeType()
     * @generated
     */
    int CODE_TYPE = 9;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CODE_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Code Space</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CODE_TYPE__CODE_SPACE = 1;

    /**
     * The number of structural features of the '<em>Code Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CODE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ContactTypeImpl <em>Contact Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ContactTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getContactType()
     * @generated
     */
    int CONTACT_TYPE = 10;

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
     * The feature id for the '<em><b>Hours Of Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTACT_TYPE__HOURS_OF_SERVICE = 3;

    /**
     * The feature id for the '<em><b>Contact Instructions</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTACT_TYPE__CONTACT_INSTRUCTIONS = 4;

    /**
     * The number of structural features of the '<em>Contact Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTACT_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ContentsBaseTypeImpl <em>Contents Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ContentsBaseTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getContentsBaseType()
     * @generated
     */
    int CONTENTS_BASE_TYPE = 11;

    /**
     * The feature id for the '<em><b>Dataset Description Summary</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY = 0;

    /**
     * The feature id for the '<em><b>Other Source</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_BASE_TYPE__OTHER_SOURCE = 1;

    /**
     * The number of structural features of the '<em>Contents Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_BASE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl <em>Dataset Description Summary Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getDatasetDescriptionSummaryBaseType()
     * @generated
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE = 12;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Bounding Box Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA = DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Dataset Description Summary</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY = DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Dataset Description Summary Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.DCPTypeImpl <em>DCP Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.DCPTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getDCPType()
     * @generated
     */
    int DCP_TYPE = 13;

    /**
     * The feature id for the '<em><b>HTTP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DCP_TYPE__HTTP = 0;

    /**
     * The number of structural features of the '<em>DCP Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DCP_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.DocumentRootImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getDocumentRoot()
     * @generated
     */
    int DOCUMENT_ROOT = 15;

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
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT = 3;

    /**
     * The feature id for the '<em><b>Abstract Meta Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT_META_DATA = 4;

    /**
     * The feature id for the '<em><b>Abstract Reference Base</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE = 5;

    /**
     * The feature id for the '<em><b>Access Constraints</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ACCESS_CONSTRAINTS = 6;

    /**
     * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ALLOWED_VALUES = 7;

    /**
     * The feature id for the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANY_VALUE = 8;

    /**
     * The feature id for the '<em><b>Available CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__AVAILABLE_CRS = 9;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__BOUNDING_BOX = 10;

    /**
     * The feature id for the '<em><b>Contact Info</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CONTACT_INFO = 11;

    /**
     * The feature id for the '<em><b>Dataset Description Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY = 12;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DATA_TYPE = 13;

    /**
     * The feature id for the '<em><b>DCP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DCP = 14;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DEFAULT_VALUE = 15;

    /**
     * The feature id for the '<em><b>Exception</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXCEPTION = 16;

    /**
     * The feature id for the '<em><b>Exception Report</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXCEPTION_REPORT = 17;

    /**
     * The feature id for the '<em><b>Extended Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXTENDED_CAPABILITIES = 18;

    /**
     * The feature id for the '<em><b>Fees</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FEES = 19;

    /**
     * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_CAPABILITIES = 20;

    /**
     * The feature id for the '<em><b>Get Resource By ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_RESOURCE_BY_ID = 21;

    /**
     * The feature id for the '<em><b>HTTP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__HTTP = 22;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__IDENTIFIER = 23;

    /**
     * The feature id for the '<em><b>Individual Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INDIVIDUAL_NAME = 24;

    /**
     * The feature id for the '<em><b>Input Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INPUT_DATA = 25;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__KEYWORDS = 26;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LANGUAGE = 27;

    /**
     * The feature id for the '<em><b>Manifest</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MANIFEST = 28;

    /**
     * The feature id for the '<em><b>Maximum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MAXIMUM_VALUE = 29;

    /**
     * The feature id for the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MEANING = 30;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__METADATA = 31;

    /**
     * The feature id for the '<em><b>Minimum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MINIMUM_VALUE = 32;

    /**
     * The feature id for the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NO_VALUES = 33;

    /**
     * The feature id for the '<em><b>Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OPERATION = 34;

    /**
     * The feature id for the '<em><b>Operation Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OPERATION_RESPONSE = 35;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OPERATIONS_METADATA = 36;

    /**
     * The feature id for the '<em><b>Organisation Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ORGANISATION_NAME = 37;

    /**
     * The feature id for the '<em><b>Other Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OTHER_SOURCE = 38;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OUTPUT_FORMAT = 39;

    /**
     * The feature id for the '<em><b>Point Of Contact</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__POINT_OF_CONTACT = 40;

    /**
     * The feature id for the '<em><b>Position Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__POSITION_NAME = 41;

    /**
     * The feature id for the '<em><b>Range</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RANGE = 42;

    /**
     * The feature id for the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__REFERENCE = 43;

    /**
     * The feature id for the '<em><b>Reference Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__REFERENCE_GROUP = 44;

    /**
     * The feature id for the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__REFERENCE_SYSTEM = 45;

    /**
     * The feature id for the '<em><b>Resource</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RESOURCE = 46;

    /**
     * The feature id for the '<em><b>Role</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ROLE = 47;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SERVICE_IDENTIFICATION = 48;

    /**
     * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SERVICE_PROVIDER = 49;

    /**
     * The feature id for the '<em><b>Service Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SERVICE_REFERENCE = 50;

    /**
     * The feature id for the '<em><b>Spacing</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SPACING = 51;

    /**
     * The feature id for the '<em><b>Supported CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SUPPORTED_CRS = 52;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TITLE = 53;

    /**
     * The feature id for the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__UOM = 54;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__VALUE = 55;

    /**
     * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__VALUES_REFERENCE = 56;

    /**
     * The feature id for the '<em><b>WGS84 Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__WGS84_BOUNDING_BOX = 57;

    /**
     * The feature id for the '<em><b>Range Closure</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RANGE_CLOSURE = 58;

    /**
     * The feature id for the '<em><b>Reference1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__REFERENCE1 = 59;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 60;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.DomainMetadataTypeImpl <em>Domain Metadata Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.DomainMetadataTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getDomainMetadataType()
     * @generated
     */
    int DOMAIN_METADATA_TYPE = 16;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_METADATA_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_METADATA_TYPE__REFERENCE = 1;

    /**
     * The number of structural features of the '<em>Domain Metadata Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_METADATA_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.UnNamedDomainTypeImpl <em>Un Named Domain Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.UnNamedDomainTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getUnNamedDomainType()
     * @generated
     */
    int UN_NAMED_DOMAIN_TYPE = 43;

    /**
     * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES = 0;

    /**
     * The feature id for the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__ANY_VALUE = 1;

    /**
     * The feature id for the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__NO_VALUES = 2;

    /**
     * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE = 3;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE = 4;

    /**
     * The feature id for the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__MEANING = 5;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__DATA_TYPE = 6;

    /**
     * The feature id for the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__UOM = 7;

    /**
     * The feature id for the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM = 8;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__METADATA = 9;

    /**
     * The number of structural features of the '<em>Un Named Domain Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE_FEATURE_COUNT = 10;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.DomainTypeImpl <em>Domain Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.DomainTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getDomainType()
     * @generated
     */
    int DOMAIN_TYPE = 17;

    /**
     * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__ALLOWED_VALUES = UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES;

    /**
     * The feature id for the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__ANY_VALUE = UN_NAMED_DOMAIN_TYPE__ANY_VALUE;

    /**
     * The feature id for the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__NO_VALUES = UN_NAMED_DOMAIN_TYPE__NO_VALUES;

    /**
     * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__VALUES_REFERENCE = UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__DEFAULT_VALUE = UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE;

    /**
     * The feature id for the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__MEANING = UN_NAMED_DOMAIN_TYPE__MEANING;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__DATA_TYPE = UN_NAMED_DOMAIN_TYPE__DATA_TYPE;

    /**
     * The feature id for the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__UOM = UN_NAMED_DOMAIN_TYPE__UOM;

    /**
     * The feature id for the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__REFERENCE_SYSTEM = UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__METADATA = UN_NAMED_DOMAIN_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__NAME = UN_NAMED_DOMAIN_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Domain Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE_FEATURE_COUNT = UN_NAMED_DOMAIN_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ExceptionReportTypeImpl <em>Exception Report Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ExceptionReportTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getExceptionReportType()
     * @generated
     */
    int EXCEPTION_REPORT_TYPE = 18;

    /**
     * The feature id for the '<em><b>Exception</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEPTION_REPORT_TYPE__EXCEPTION = 0;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEPTION_REPORT_TYPE__LANG = 1;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEPTION_REPORT_TYPE__VERSION = 2;

    /**
     * The number of structural features of the '<em>Exception Report Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEPTION_REPORT_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ExceptionTypeImpl <em>Exception Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ExceptionTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getExceptionType()
     * @generated
     */
    int EXCEPTION_TYPE = 19;

    /**
     * The feature id for the '<em><b>Exception Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEPTION_TYPE__EXCEPTION_TEXT = 0;

    /**
     * The feature id for the '<em><b>Exception Code</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEPTION_TYPE__EXCEPTION_CODE = 1;

    /**
     * The feature id for the '<em><b>Locator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEPTION_TYPE__LOCATOR = 2;

    /**
     * The number of structural features of the '<em>Exception Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXCEPTION_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.GetCapabilitiesTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getGetCapabilitiesType()
     * @generated
     */
    int GET_CAPABILITIES_TYPE = 20;

    /**
     * The feature id for the '<em><b>Accept Versions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = 0;

    /**
     * The feature id for the '<em><b>Sections</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SECTIONS = 1;

    /**
     * The feature id for the '<em><b>Accept Formats</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = 2;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = 3;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__BASE_URL = 4;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__NAMESPACE = 5;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES = 6;

				/**
     * The number of structural features of the '<em>Get Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.GetResourceByIdTypeImpl <em>Get Resource By Id Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.GetResourceByIdTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getGetResourceByIdType()
     * @generated
     */
    int GET_RESOURCE_BY_ID_TYPE = 21;

    /**
     * The feature id for the '<em><b>Resource ID</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RESOURCE_BY_ID_TYPE__RESOURCE_ID = 0;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RESOURCE_BY_ID_TYPE__OUTPUT_FORMAT = 1;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RESOURCE_BY_ID_TYPE__SERVICE = 2;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RESOURCE_BY_ID_TYPE__VERSION = 3;

    /**
     * The number of structural features of the '<em>Get Resource By Id Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RESOURCE_BY_ID_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.HTTPTypeImpl <em>HTTP Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.HTTPTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getHTTPType()
     * @generated
     */
    int HTTP_TYPE = 22;

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
     * The meta object id for the '{@link net.opengis.ows11.impl.IdentificationTypeImpl <em>Identification Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.IdentificationTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getIdentificationType()
     * @generated
     */
    int IDENTIFICATION_TYPE = 23;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__TITLE = BASIC_IDENTIFICATION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__ABSTRACT = BASIC_IDENTIFICATION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__KEYWORDS = BASIC_IDENTIFICATION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__IDENTIFIER = BASIC_IDENTIFICATION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__METADATA = BASIC_IDENTIFICATION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Bounding Box Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__BOUNDING_BOX = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__OUTPUT_FORMAT = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Available CRS Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Available CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__AVAILABLE_CRS = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Identification Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE_FEATURE_COUNT = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.KeywordsTypeImpl <em>Keywords Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.KeywordsTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getKeywordsType()
     * @generated
     */
    int KEYWORDS_TYPE = 24;

    /**
     * The feature id for the '<em><b>Keyword</b></em>' containment reference list.
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
     * The meta object id for the '{@link net.opengis.ows11.impl.LanguageStringTypeImpl <em>Language String Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.LanguageStringTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getLanguageStringType()
     * @generated
     */
    int LANGUAGE_STRING_TYPE = 25;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LANGUAGE_STRING_TYPE__VALUE = 0;

    /**
     * The number of structural features of the '<em>Language String Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LANGUAGE_STRING_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ManifestTypeImpl <em>Manifest Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ManifestTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getManifestType()
     * @generated
     */
    int MANIFEST_TYPE = 26;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__TITLE = BASIC_IDENTIFICATION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__ABSTRACT = BASIC_IDENTIFICATION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__KEYWORDS = BASIC_IDENTIFICATION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__IDENTIFIER = BASIC_IDENTIFICATION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__METADATA = BASIC_IDENTIFICATION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Reference Group</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__REFERENCE_GROUP = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Manifest Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE_FEATURE_COUNT = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.MetadataTypeImpl <em>Metadata Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.MetadataTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getMetadataType()
     * @generated
     */
    int METADATA_TYPE = 27;

    /**
     * The feature id for the '<em><b>Abstract Meta Data Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int METADATA_TYPE__ABSTRACT_META_DATA_GROUP = 0;

    /**
     * The feature id for the '<em><b>Abstract Meta Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int METADATA_TYPE__ABSTRACT_META_DATA = 1;

    /**
     * The feature id for the '<em><b>About</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int METADATA_TYPE__ABOUT = 2;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int METADATA_TYPE__TITLE = 3;

				/**
     * The number of structural features of the '<em>Metadata Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int METADATA_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.NoValuesTypeImpl <em>No Values Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.NoValuesTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getNoValuesType()
     * @generated
     */
    int NO_VALUES_TYPE = 28;

    /**
     * The number of structural features of the '<em>No Values Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NO_VALUES_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.OnlineResourceTypeImpl <em>Online Resource Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.OnlineResourceTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getOnlineResourceType()
     * @generated
     */
    int ONLINE_RESOURCE_TYPE = 29;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int ONLINE_RESOURCE_TYPE__HREF = 0;

				/**
     * The number of structural features of the '<em>Online Resource Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ONLINE_RESOURCE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.OperationsMetadataTypeImpl <em>Operations Metadata Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.OperationsMetadataTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getOperationsMetadataType()
     * @generated
     */
    int OPERATIONS_METADATA_TYPE = 30;

    /**
     * The feature id for the '<em><b>Operation</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE__OPERATION = 0;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE__PARAMETER = 1;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE__CONSTRAINT = 2;

    /**
     * The feature id for the '<em><b>Extended Capabilities</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES = 3;

    /**
     * The number of structural features of the '<em>Operations Metadata Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.OperationTypeImpl <em>Operation Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.OperationTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getOperationType()
     * @generated
     */
    int OPERATION_TYPE = 31;

    /**
     * The feature id for the '<em><b>DCP</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__DCP = 0;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__PARAMETER = 1;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__CONSTRAINT = 2;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__METADATA = 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__NAME = 4;

    /**
     * The number of structural features of the '<em>Operation Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.RangeTypeImpl <em>Range Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.RangeTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getRangeType()
     * @generated
     */
    int RANGE_TYPE = 32;

    /**
     * The feature id for the '<em><b>Minimum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__MINIMUM_VALUE = 0;

    /**
     * The feature id for the '<em><b>Maximum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__MAXIMUM_VALUE = 1;

    /**
     * The feature id for the '<em><b>Spacing</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__SPACING = 2;

    /**
     * The feature id for the '<em><b>Range Closure</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__RANGE_CLOSURE = 3;

    /**
     * The number of structural features of the '<em>Range Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ReferenceGroupTypeImpl <em>Reference Group Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ReferenceGroupTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getReferenceGroupType()
     * @generated
     */
    int REFERENCE_GROUP_TYPE = 33;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__TITLE = BASIC_IDENTIFICATION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__ABSTRACT = BASIC_IDENTIFICATION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__KEYWORDS = BASIC_IDENTIFICATION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__IDENTIFIER = BASIC_IDENTIFICATION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__METADATA = BASIC_IDENTIFICATION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Abstract Reference Base Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Abstract Reference Base</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Reference Group Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE_FEATURE_COUNT = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ReferenceTypeImpl <em>Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ReferenceTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getReferenceType()
     * @generated
     */
    int REFERENCE_TYPE = 34;

    /**
     * The feature id for the '<em><b>Actuate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__ACTUATE = ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE;

    /**
     * The feature id for the '<em><b>Arcrole</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__ARCROLE = ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__HREF = ABSTRACT_REFERENCE_BASE_TYPE__HREF;

    /**
     * The feature id for the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__ROLE = ABSTRACT_REFERENCE_BASE_TYPE__ROLE;

    /**
     * The feature id for the '<em><b>Show</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__SHOW = ABSTRACT_REFERENCE_BASE_TYPE__SHOW;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__TITLE = ABSTRACT_REFERENCE_BASE_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__TYPE = ABSTRACT_REFERENCE_BASE_TYPE__TYPE;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__IDENTIFIER = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__ABSTRACT = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__FORMAT = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__METADATA = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE_FEATURE_COUNT = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.RequestMethodTypeImpl <em>Request Method Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.RequestMethodTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getRequestMethodType()
     * @generated
     */
    int REQUEST_METHOD_TYPE = 35;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int REQUEST_METHOD_TYPE__HREF = ONLINE_RESOURCE_TYPE__HREF;

				/**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_METHOD_TYPE__CONSTRAINT = ONLINE_RESOURCE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Request Method Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_METHOD_TYPE_FEATURE_COUNT = ONLINE_RESOURCE_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ResponsiblePartySubsetTypeImpl <em>Responsible Party Subset Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ResponsiblePartySubsetTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getResponsiblePartySubsetType()
     * @generated
     */
    int RESPONSIBLE_PARTY_SUBSET_TYPE = 36;

    /**
     * The feature id for the '<em><b>Individual Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_SUBSET_TYPE__INDIVIDUAL_NAME = 0;

    /**
     * The feature id for the '<em><b>Position Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_SUBSET_TYPE__POSITION_NAME = 1;

    /**
     * The feature id for the '<em><b>Contact Info</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_SUBSET_TYPE__CONTACT_INFO = 2;

    /**
     * The feature id for the '<em><b>Role</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_SUBSET_TYPE__ROLE = 3;

    /**
     * The number of structural features of the '<em>Responsible Party Subset Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_SUBSET_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ResponsiblePartyTypeImpl <em>Responsible Party Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ResponsiblePartyTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getResponsiblePartyType()
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
     * The feature id for the '<em><b>Position Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_TYPE__POSITION_NAME = 2;

    /**
     * The feature id for the '<em><b>Contact Info</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_TYPE__CONTACT_INFO = 3;

    /**
     * The feature id for the '<em><b>Role</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_TYPE__ROLE = 4;

    /**
     * The number of structural features of the '<em>Responsible Party Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBLE_PARTY_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.SectionsTypeImpl <em>Sections Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.SectionsTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getSectionsType()
     * @generated
     */
    int SECTIONS_TYPE = 38;

    /**
     * The feature id for the '<em><b>Section</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SECTIONS_TYPE__SECTION = 0;

    /**
     * The number of structural features of the '<em>Sections Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SECTIONS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ServiceIdentificationTypeImpl <em>Service Identification Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ServiceIdentificationTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getServiceIdentificationType()
     * @generated
     */
    int SERVICE_IDENTIFICATION_TYPE = 39;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Service Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Service Type Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Profile</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__PROFILE = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Fees</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__FEES = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Access Constraints</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS = DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Service Identification Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ServiceProviderTypeImpl <em>Service Provider Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ServiceProviderTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getServiceProviderType()
     * @generated
     */
    int SERVICE_PROVIDER_TYPE = 40;

    /**
     * The feature id for the '<em><b>Provider Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PROVIDER_TYPE__PROVIDER_NAME = 0;

    /**
     * The feature id for the '<em><b>Provider Site</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PROVIDER_TYPE__PROVIDER_SITE = 1;

    /**
     * The feature id for the '<em><b>Service Contact</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PROVIDER_TYPE__SERVICE_CONTACT = 2;

    /**
     * The number of structural features of the '<em>Service Provider Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PROVIDER_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ServiceReferenceTypeImpl <em>Service Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ServiceReferenceTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getServiceReferenceType()
     * @generated
     */
    int SERVICE_REFERENCE_TYPE = 41;

    /**
     * The feature id for the '<em><b>Actuate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__ACTUATE = REFERENCE_TYPE__ACTUATE;

    /**
     * The feature id for the '<em><b>Arcrole</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__ARCROLE = REFERENCE_TYPE__ARCROLE;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__HREF = REFERENCE_TYPE__HREF;

    /**
     * The feature id for the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__ROLE = REFERENCE_TYPE__ROLE;

    /**
     * The feature id for the '<em><b>Show</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__SHOW = REFERENCE_TYPE__SHOW;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__TITLE = REFERENCE_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__TYPE = REFERENCE_TYPE__TYPE;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__IDENTIFIER = REFERENCE_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__ABSTRACT = REFERENCE_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__FORMAT = REFERENCE_TYPE__FORMAT;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__METADATA = REFERENCE_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Request Message</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE = REFERENCE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Request Message Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE = REFERENCE_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Service Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE_FEATURE_COUNT = REFERENCE_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.TelephoneTypeImpl <em>Telephone Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.TelephoneTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getTelephoneType()
     * @generated
     */
    int TELEPHONE_TYPE = 42;

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
     * The meta object id for the '{@link net.opengis.ows11.impl.ValuesReferenceTypeImpl <em>Values Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ValuesReferenceTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getValuesReferenceType()
     * @generated
     */
    int VALUES_REFERENCE_TYPE = 44;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE__REFERENCE = 1;

    /**
     * The number of structural features of the '<em>Values Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.ValueTypeImpl <em>Value Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.ValueTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getValueType()
     * @generated
     */
    int VALUE_TYPE = 45;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_TYPE__VALUE = 0;

    /**
     * The number of structural features of the '<em>Value Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.ows11.impl.WGS84BoundingBoxTypeImpl <em>WGS84 Bounding Box Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.impl.WGS84BoundingBoxTypeImpl
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getWGS84BoundingBoxType()
     * @generated
     */
    int WGS84_BOUNDING_BOX_TYPE = 46;

    /**
     * The feature id for the '<em><b>Lower Corner</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WGS84_BOUNDING_BOX_TYPE__LOWER_CORNER = BOUNDING_BOX_TYPE__LOWER_CORNER;

    /**
     * The feature id for the '<em><b>Upper Corner</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WGS84_BOUNDING_BOX_TYPE__UPPER_CORNER = BOUNDING_BOX_TYPE__UPPER_CORNER;

    /**
     * The feature id for the '<em><b>Crs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WGS84_BOUNDING_BOX_TYPE__CRS = BOUNDING_BOX_TYPE__CRS;

    /**
     * The feature id for the '<em><b>Dimensions</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WGS84_BOUNDING_BOX_TYPE__DIMENSIONS = BOUNDING_BOX_TYPE__DIMENSIONS;

    /**
     * The number of structural features of the '<em>WGS84 Bounding Box Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WGS84_BOUNDING_BOX_TYPE_FEATURE_COUNT = BOUNDING_BOX_TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.ows11.RangeClosureType <em>Range Closure Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.RangeClosureType
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getRangeClosureType()
     * @generated
     */
    int RANGE_CLOSURE_TYPE = 47;

    /**
     * The meta object id for the '<em>Mime Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getMimeType()
     * @generated
     */
    int MIME_TYPE = 48;

    /**
     * The meta object id for the '<em>Position Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getPositionType()
     * @generated
     */
    int POSITION_TYPE = 49;

    /**
     * The meta object id for the '<em>Position Type2 D</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getPositionType2D()
     * @generated
     */
    int POSITION_TYPE2_D = 50;

    /**
     * The meta object id for the '<em>Range Closure Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.ows11.RangeClosureType
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getRangeClosureTypeObject()
     * @generated
     */
    int RANGE_CLOSURE_TYPE_OBJECT = 51;

    /**
     * The meta object id for the '<em>Service Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getServiceType()
     * @generated
     */
    int SERVICE_TYPE = 52;

    /**
     * The meta object id for the '<em>Update Sequence Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getUpdateSequenceType()
     * @generated
     */
    int UPDATE_SEQUENCE_TYPE = 53;

    /**
     * The meta object id for the '<em>Version Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getVersionType()
     * @generated
     */
    int VERSION_TYPE = 54;

    /**
     * The meta object id for the '<em>Version Type1</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getVersionType1()
     * @generated
     */
    int VERSION_TYPE1 = 55;


    /**
     * The meta object id for the '<em>Map</em>' data type.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see java.util.Map
     * @see net.opengis.ows11.impl.Ows11PackageImpl#getMap()
     * @generated
     */
	int MAP = 56;


				/**
     * Returns the meta object for class '{@link net.opengis.ows11.AbstractReferenceBaseType <em>Abstract Reference Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Reference Base Type</em>'.
     * @see net.opengis.ows11.AbstractReferenceBaseType
     * @generated
     */
    EClass getAbstractReferenceBaseType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AbstractReferenceBaseType#getActuate <em>Actuate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Actuate</em>'.
     * @see net.opengis.ows11.AbstractReferenceBaseType#getActuate()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Actuate();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AbstractReferenceBaseType#getArcrole <em>Arcrole</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Arcrole</em>'.
     * @see net.opengis.ows11.AbstractReferenceBaseType#getArcrole()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Arcrole();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AbstractReferenceBaseType#getHref <em>Href</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Href</em>'.
     * @see net.opengis.ows11.AbstractReferenceBaseType#getHref()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Href();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AbstractReferenceBaseType#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Role</em>'.
     * @see net.opengis.ows11.AbstractReferenceBaseType#getRole()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Role();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AbstractReferenceBaseType#getShow <em>Show</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Show</em>'.
     * @see net.opengis.ows11.AbstractReferenceBaseType#getShow()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Show();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AbstractReferenceBaseType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Title</em>'.
     * @see net.opengis.ows11.AbstractReferenceBaseType#getTitle()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Title();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AbstractReferenceBaseType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see net.opengis.ows11.AbstractReferenceBaseType#getType()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Type();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.AcceptFormatsType <em>Accept Formats Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Accept Formats Type</em>'.
     * @see net.opengis.ows11.AcceptFormatsType
     * @generated
     */
    EClass getAcceptFormatsType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AcceptFormatsType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.ows11.AcceptFormatsType#getOutputFormat()
     * @see #getAcceptFormatsType()
     * @generated
     */
    EAttribute getAcceptFormatsType_OutputFormat();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.AcceptVersionsType <em>Accept Versions Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Accept Versions Type</em>'.
     * @see net.opengis.ows11.AcceptVersionsType
     * @generated
     */
    EClass getAcceptVersionsType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AcceptVersionsType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.ows11.AcceptVersionsType#getVersion()
     * @see #getAcceptVersionsType()
     * @generated
     */
    EAttribute getAcceptVersionsType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.AddressType <em>Address Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Address Type</em>'.
     * @see net.opengis.ows11.AddressType
     * @generated
     */
    EClass getAddressType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AddressType#getDeliveryPoint <em>Delivery Point</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Delivery Point</em>'.
     * @see net.opengis.ows11.AddressType#getDeliveryPoint()
     * @see #getAddressType()
     * @generated
     */
    EAttribute getAddressType_DeliveryPoint();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AddressType#getCity <em>City</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>City</em>'.
     * @see net.opengis.ows11.AddressType#getCity()
     * @see #getAddressType()
     * @generated
     */
    EAttribute getAddressType_City();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AddressType#getAdministrativeArea <em>Administrative Area</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Administrative Area</em>'.
     * @see net.opengis.ows11.AddressType#getAdministrativeArea()
     * @see #getAddressType()
     * @generated
     */
    EAttribute getAddressType_AdministrativeArea();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AddressType#getPostalCode <em>Postal Code</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Postal Code</em>'.
     * @see net.opengis.ows11.AddressType#getPostalCode()
     * @see #getAddressType()
     * @generated
     */
    EAttribute getAddressType_PostalCode();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AddressType#getCountry <em>Country</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Country</em>'.
     * @see net.opengis.ows11.AddressType#getCountry()
     * @see #getAddressType()
     * @generated
     */
    EAttribute getAddressType_Country();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.AddressType#getElectronicMailAddress <em>Electronic Mail Address</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Electronic Mail Address</em>'.
     * @see net.opengis.ows11.AddressType#getElectronicMailAddress()
     * @see #getAddressType()
     * @generated
     */
    EAttribute getAddressType_ElectronicMailAddress();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.AllowedValuesType <em>Allowed Values Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Allowed Values Type</em>'.
     * @see net.opengis.ows11.AllowedValuesType
     * @generated
     */
    EClass getAllowedValuesType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.ows11.AllowedValuesType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see net.opengis.ows11.AllowedValuesType#getGroup()
     * @see #getAllowedValuesType()
     * @generated
     */
    EAttribute getAllowedValuesType_Group();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.AllowedValuesType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Value</em>'.
     * @see net.opengis.ows11.AllowedValuesType#getValue()
     * @see #getAllowedValuesType()
     * @generated
     */
    EReference getAllowedValuesType_Value();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.AllowedValuesType#getRange <em>Range</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Range</em>'.
     * @see net.opengis.ows11.AllowedValuesType#getRange()
     * @see #getAllowedValuesType()
     * @generated
     */
    EReference getAllowedValuesType_Range();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.AnyValueType <em>Any Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Any Value Type</em>'.
     * @see net.opengis.ows11.AnyValueType
     * @generated
     */
    EClass getAnyValueType();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.BasicIdentificationType <em>Basic Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Basic Identification Type</em>'.
     * @see net.opengis.ows11.BasicIdentificationType
     * @generated
     */
    EClass getBasicIdentificationType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.BasicIdentificationType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.ows11.BasicIdentificationType#getIdentifier()
     * @see #getBasicIdentificationType()
     * @generated
     */
    EReference getBasicIdentificationType_Identifier();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.BasicIdentificationType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.ows11.BasicIdentificationType#getMetadata()
     * @see #getBasicIdentificationType()
     * @generated
     */
    EReference getBasicIdentificationType_Metadata();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.BoundingBoxType <em>Bounding Box Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bounding Box Type</em>'.
     * @see net.opengis.ows11.BoundingBoxType
     * @generated
     */
    EClass getBoundingBoxType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.BoundingBoxType#getLowerCorner <em>Lower Corner</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lower Corner</em>'.
     * @see net.opengis.ows11.BoundingBoxType#getLowerCorner()
     * @see #getBoundingBoxType()
     * @generated
     */
    EAttribute getBoundingBoxType_LowerCorner();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.BoundingBoxType#getUpperCorner <em>Upper Corner</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Upper Corner</em>'.
     * @see net.opengis.ows11.BoundingBoxType#getUpperCorner()
     * @see #getBoundingBoxType()
     * @generated
     */
    EAttribute getBoundingBoxType_UpperCorner();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.BoundingBoxType#getCrs <em>Crs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Crs</em>'.
     * @see net.opengis.ows11.BoundingBoxType#getCrs()
     * @see #getBoundingBoxType()
     * @generated
     */
    EAttribute getBoundingBoxType_Crs();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.BoundingBoxType#getDimensions <em>Dimensions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Dimensions</em>'.
     * @see net.opengis.ows11.BoundingBoxType#getDimensions()
     * @see #getBoundingBoxType()
     * @generated
     */
    EAttribute getBoundingBoxType_Dimensions();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Capabilities Base Type</em>'.
     * @see net.opengis.ows11.CapabilitiesBaseType
     * @generated
     */
    EClass getCapabilitiesBaseType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.CapabilitiesBaseType#getServiceIdentification <em>Service Identification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Identification</em>'.
     * @see net.opengis.ows11.CapabilitiesBaseType#getServiceIdentification()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EReference getCapabilitiesBaseType_ServiceIdentification();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.CapabilitiesBaseType#getServiceProvider <em>Service Provider</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Provider</em>'.
     * @see net.opengis.ows11.CapabilitiesBaseType#getServiceProvider()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EReference getCapabilitiesBaseType_ServiceProvider();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.CapabilitiesBaseType#getOperationsMetadata <em>Operations Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operations Metadata</em>'.
     * @see net.opengis.ows11.CapabilitiesBaseType#getOperationsMetadata()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EReference getCapabilitiesBaseType_OperationsMetadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.CapabilitiesBaseType#getUpdateSequence <em>Update Sequence</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Update Sequence</em>'.
     * @see net.opengis.ows11.CapabilitiesBaseType#getUpdateSequence()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EAttribute getCapabilitiesBaseType_UpdateSequence();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.CapabilitiesBaseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.ows11.CapabilitiesBaseType#getVersion()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EAttribute getCapabilitiesBaseType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.CodeType <em>Code Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Code Type</em>'.
     * @see net.opengis.ows11.CodeType
     * @generated
     */
    EClass getCodeType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.CodeType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.ows11.CodeType#getValue()
     * @see #getCodeType()
     * @generated
     */
    EAttribute getCodeType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.CodeType#getCodeSpace <em>Code Space</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Code Space</em>'.
     * @see net.opengis.ows11.CodeType#getCodeSpace()
     * @see #getCodeType()
     * @generated
     */
    EAttribute getCodeType_CodeSpace();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ContactType <em>Contact Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Contact Type</em>'.
     * @see net.opengis.ows11.ContactType
     * @generated
     */
    EClass getContactType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ContactType#getPhone <em>Phone</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Phone</em>'.
     * @see net.opengis.ows11.ContactType#getPhone()
     * @see #getContactType()
     * @generated
     */
    EReference getContactType_Phone();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ContactType#getAddress <em>Address</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Address</em>'.
     * @see net.opengis.ows11.ContactType#getAddress()
     * @see #getContactType()
     * @generated
     */
    EReference getContactType_Address();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ContactType#getOnlineResource <em>Online Resource</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Online Resource</em>'.
     * @see net.opengis.ows11.ContactType#getOnlineResource()
     * @see #getContactType()
     * @generated
     */
    EReference getContactType_OnlineResource();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ContactType#getHoursOfService <em>Hours Of Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hours Of Service</em>'.
     * @see net.opengis.ows11.ContactType#getHoursOfService()
     * @see #getContactType()
     * @generated
     */
    EAttribute getContactType_HoursOfService();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ContactType#getContactInstructions <em>Contact Instructions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Contact Instructions</em>'.
     * @see net.opengis.ows11.ContactType#getContactInstructions()
     * @see #getContactType()
     * @generated
     */
    EAttribute getContactType_ContactInstructions();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ContentsBaseType <em>Contents Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Contents Base Type</em>'.
     * @see net.opengis.ows11.ContentsBaseType
     * @generated
     */
    EClass getContentsBaseType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.ContentsBaseType#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Dataset Description Summary</em>'.
     * @see net.opengis.ows11.ContentsBaseType#getDatasetDescriptionSummary()
     * @see #getContentsBaseType()
     * @generated
     */
    EReference getContentsBaseType_DatasetDescriptionSummary();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.ContentsBaseType#getOtherSource <em>Other Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Other Source</em>'.
     * @see net.opengis.ows11.ContentsBaseType#getOtherSource()
     * @see #getContentsBaseType()
     * @generated
     */
    EReference getContentsBaseType_OtherSource();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType <em>Dataset Description Summary Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dataset Description Summary Base Type</em>'.
     * @see net.opengis.ows11.DatasetDescriptionSummaryBaseType
     * @generated
     */
    EClass getDatasetDescriptionSummaryBaseType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>WGS84 Bounding Box</em>'.
     * @see net.opengis.ows11.DatasetDescriptionSummaryBaseType#getWGS84BoundingBox()
     * @see #getDatasetDescriptionSummaryBaseType()
     * @generated
     */
    EReference getDatasetDescriptionSummaryBaseType_WGS84BoundingBox();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.ows11.DatasetDescriptionSummaryBaseType#getIdentifier()
     * @see #getDatasetDescriptionSummaryBaseType()
     * @generated
     */
    EReference getDatasetDescriptionSummaryBaseType_Identifier();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getBoundingBoxGroup <em>Bounding Box Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Bounding Box Group</em>'.
     * @see net.opengis.ows11.DatasetDescriptionSummaryBaseType#getBoundingBoxGroup()
     * @see #getDatasetDescriptionSummaryBaseType()
     * @generated
     */
    EAttribute getDatasetDescriptionSummaryBaseType_BoundingBoxGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bounding Box</em>'.
     * @see net.opengis.ows11.DatasetDescriptionSummaryBaseType#getBoundingBox()
     * @see #getDatasetDescriptionSummaryBaseType()
     * @generated
     */
    EReference getDatasetDescriptionSummaryBaseType_BoundingBox();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.ows11.DatasetDescriptionSummaryBaseType#getMetadata()
     * @see #getDatasetDescriptionSummaryBaseType()
     * @generated
     */
    EReference getDatasetDescriptionSummaryBaseType_Metadata();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Dataset Description Summary</em>'.
     * @see net.opengis.ows11.DatasetDescriptionSummaryBaseType#getDatasetDescriptionSummary()
     * @see #getDatasetDescriptionSummaryBaseType()
     * @generated
     */
    EReference getDatasetDescriptionSummaryBaseType_DatasetDescriptionSummary();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.DCPType <em>DCP Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>DCP Type</em>'.
     * @see net.opengis.ows11.DCPType
     * @generated
     */
    EClass getDCPType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DCPType#getHTTP <em>HTTP</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>HTTP</em>'.
     * @see net.opengis.ows11.DCPType#getHTTP()
     * @see #getDCPType()
     * @generated
     */
    EReference getDCPType_HTTP();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Description Type</em>'.
     * @see net.opengis.ows11.DescriptionType
     * @generated
     */
    EClass getDescriptionType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.DescriptionType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Title</em>'.
     * @see net.opengis.ows11.DescriptionType#getTitle()
     * @see #getDescriptionType()
     * @generated
     */
    EReference getDescriptionType_Title();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.DescriptionType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Abstract</em>'.
     * @see net.opengis.ows11.DescriptionType#getAbstract()
     * @see #getDescriptionType()
     * @generated
     */
    EReference getDescriptionType_Abstract();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.DescriptionType#getKeywords <em>Keywords</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Keywords</em>'.
     * @see net.opengis.ows11.DescriptionType#getKeywords()
     * @see #getDescriptionType()
     * @generated
     */
    EReference getDescriptionType_Keywords();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see net.opengis.ows11.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.ows11.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.ows11.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map '{@link net.opengis.ows11.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see net.opengis.ows11.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link net.opengis.ows11.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see net.opengis.ows11.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract</em>'.
     * @see net.opengis.ows11.DocumentRoot#getAbstract()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Abstract();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getAbstractMetaData <em>Abstract Meta Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Meta Data</em>'.
     * @see net.opengis.ows11.DocumentRoot#getAbstractMetaData()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AbstractMetaData();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getAbstractReferenceBase <em>Abstract Reference Base</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Reference Base</em>'.
     * @see net.opengis.ows11.DocumentRoot#getAbstractReferenceBase()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AbstractReferenceBase();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getAccessConstraints <em>Access Constraints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Access Constraints</em>'.
     * @see net.opengis.ows11.DocumentRoot#getAccessConstraints()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_AccessConstraints();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getAllowedValues <em>Allowed Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Allowed Values</em>'.
     * @see net.opengis.ows11.DocumentRoot#getAllowedValues()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AllowedValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getAnyValue <em>Any Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Any Value</em>'.
     * @see net.opengis.ows11.DocumentRoot#getAnyValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AnyValue();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getAvailableCRS <em>Available CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Available CRS</em>'.
     * @see net.opengis.ows11.DocumentRoot#getAvailableCRS()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_AvailableCRS();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bounding Box</em>'.
     * @see net.opengis.ows11.DocumentRoot#getBoundingBox()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_BoundingBox();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getContactInfo <em>Contact Info</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contact Info</em>'.
     * @see net.opengis.ows11.DocumentRoot#getContactInfo()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ContactInfo();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Dataset Description Summary</em>'.
     * @see net.opengis.ows11.DocumentRoot#getDatasetDescriptionSummary()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DatasetDescriptionSummary();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getDataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Type</em>'.
     * @see net.opengis.ows11.DocumentRoot#getDataType()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getDCP <em>DCP</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>DCP</em>'.
     * @see net.opengis.ows11.DocumentRoot#getDCP()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DCP();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getDefaultValue <em>Default Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default Value</em>'.
     * @see net.opengis.ows11.DocumentRoot#getDefaultValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DefaultValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getException <em>Exception</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Exception</em>'.
     * @see net.opengis.ows11.DocumentRoot#getException()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Exception();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getExceptionReport <em>Exception Report</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Exception Report</em>'.
     * @see net.opengis.ows11.DocumentRoot#getExceptionReport()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ExceptionReport();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getExtendedCapabilities <em>Extended Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extended Capabilities</em>'.
     * @see net.opengis.ows11.DocumentRoot#getExtendedCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ExtendedCapabilities();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getFees <em>Fees</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fees</em>'.
     * @see net.opengis.ows11.DocumentRoot#getFees()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Fees();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
     * @see net.opengis.ows11.DocumentRoot#getGetCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getGetResourceByID <em>Get Resource By ID</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Resource By ID</em>'.
     * @see net.opengis.ows11.DocumentRoot#getGetResourceByID()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetResourceByID();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getHTTP <em>HTTP</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>HTTP</em>'.
     * @see net.opengis.ows11.DocumentRoot#getHTTP()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_HTTP();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.ows11.DocumentRoot#getIdentifier()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Identifier();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getIndividualName <em>Individual Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Individual Name</em>'.
     * @see net.opengis.ows11.DocumentRoot#getIndividualName()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_IndividualName();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getInputData <em>Input Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Input Data</em>'.
     * @see net.opengis.ows11.DocumentRoot#getInputData()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_InputData();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getKeywords <em>Keywords</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Keywords</em>'.
     * @see net.opengis.ows11.DocumentRoot#getKeywords()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Keywords();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see net.opengis.ows11.DocumentRoot#getLanguage()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Language();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getManifest <em>Manifest</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Manifest</em>'.
     * @see net.opengis.ows11.DocumentRoot#getManifest()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Manifest();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getMaximumValue <em>Maximum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Maximum Value</em>'.
     * @see net.opengis.ows11.DocumentRoot#getMaximumValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_MaximumValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Meaning</em>'.
     * @see net.opengis.ows11.DocumentRoot#getMeaning()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Meaning();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Metadata</em>'.
     * @see net.opengis.ows11.DocumentRoot#getMetadata()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Metadata();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getMinimumValue <em>Minimum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Minimum Value</em>'.
     * @see net.opengis.ows11.DocumentRoot#getMinimumValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_MinimumValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getNoValues <em>No Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>No Values</em>'.
     * @see net.opengis.ows11.DocumentRoot#getNoValues()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NoValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getOperation <em>Operation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operation</em>'.
     * @see net.opengis.ows11.DocumentRoot#getOperation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Operation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getOperationResponse <em>Operation Response</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operation Response</em>'.
     * @see net.opengis.ows11.DocumentRoot#getOperationResponse()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OperationResponse();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getOperationsMetadata <em>Operations Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operations Metadata</em>'.
     * @see net.opengis.ows11.DocumentRoot#getOperationsMetadata()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OperationsMetadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getOrganisationName <em>Organisation Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Organisation Name</em>'.
     * @see net.opengis.ows11.DocumentRoot#getOrganisationName()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_OrganisationName();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getOtherSource <em>Other Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Other Source</em>'.
     * @see net.opengis.ows11.DocumentRoot#getOtherSource()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OtherSource();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.ows11.DocumentRoot#getOutputFormat()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_OutputFormat();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getPointOfContact <em>Point Of Contact</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Point Of Contact</em>'.
     * @see net.opengis.ows11.DocumentRoot#getPointOfContact()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PointOfContact();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getPositionName <em>Position Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Position Name</em>'.
     * @see net.opengis.ows11.DocumentRoot#getPositionName()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_PositionName();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getRange <em>Range</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Range</em>'.
     * @see net.opengis.ows11.DocumentRoot#getRange()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Range();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference</em>'.
     * @see net.opengis.ows11.DocumentRoot#getReference()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Reference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getReferenceGroup <em>Reference Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference Group</em>'.
     * @see net.opengis.ows11.DocumentRoot#getReferenceGroup()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ReferenceGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getReferenceSystem <em>Reference System</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference System</em>'.
     * @see net.opengis.ows11.DocumentRoot#getReferenceSystem()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ReferenceSystem();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getResource <em>Resource</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Resource</em>'.
     * @see net.opengis.ows11.DocumentRoot#getResource()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Resource();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Role</em>'.
     * @see net.opengis.ows11.DocumentRoot#getRole()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Role();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getServiceIdentification <em>Service Identification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Identification</em>'.
     * @see net.opengis.ows11.DocumentRoot#getServiceIdentification()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ServiceIdentification();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getServiceProvider <em>Service Provider</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Provider</em>'.
     * @see net.opengis.ows11.DocumentRoot#getServiceProvider()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ServiceProvider();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getServiceReference <em>Service Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Reference</em>'.
     * @see net.opengis.ows11.DocumentRoot#getServiceReference()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ServiceReference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getSpacing <em>Spacing</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spacing</em>'.
     * @see net.opengis.ows11.DocumentRoot#getSpacing()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Spacing();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Supported CRS</em>'.
     * @see net.opengis.ows11.DocumentRoot#getSupportedCRS()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_SupportedCRS();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Title</em>'.
     * @see net.opengis.ows11.DocumentRoot#getTitle()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Title();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getUOM <em>UOM</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>UOM</em>'.
     * @see net.opengis.ows11.DocumentRoot#getUOM()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_UOM();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see net.opengis.ows11.DocumentRoot#getValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Value();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getValuesReference <em>Values Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Values Reference</em>'.
     * @see net.opengis.ows11.DocumentRoot#getValuesReference()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ValuesReference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.DocumentRoot#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>WGS84 Bounding Box</em>'.
     * @see net.opengis.ows11.DocumentRoot#getWGS84BoundingBox()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_WGS84BoundingBox();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getRangeClosure <em>Range Closure</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Range Closure</em>'.
     * @see net.opengis.ows11.DocumentRoot#getRangeClosure()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_RangeClosure();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DocumentRoot#getReference1 <em>Reference1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reference1</em>'.
     * @see net.opengis.ows11.DocumentRoot#getReference1()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Reference1();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.DomainMetadataType <em>Domain Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Domain Metadata Type</em>'.
     * @see net.opengis.ows11.DomainMetadataType
     * @generated
     */
    EClass getDomainMetadataType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DomainMetadataType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.ows11.DomainMetadataType#getValue()
     * @see #getDomainMetadataType()
     * @generated
     */
    EAttribute getDomainMetadataType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DomainMetadataType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reference</em>'.
     * @see net.opengis.ows11.DomainMetadataType#getReference()
     * @see #getDomainMetadataType()
     * @generated
     */
    EAttribute getDomainMetadataType_Reference();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.DomainType <em>Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Domain Type</em>'.
     * @see net.opengis.ows11.DomainType
     * @generated
     */
    EClass getDomainType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.DomainType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.ows11.DomainType#getName()
     * @see #getDomainType()
     * @generated
     */
    EAttribute getDomainType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ExceptionReportType <em>Exception Report Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Exception Report Type</em>'.
     * @see net.opengis.ows11.ExceptionReportType
     * @generated
     */
    EClass getExceptionReportType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.ExceptionReportType#getException <em>Exception</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Exception</em>'.
     * @see net.opengis.ows11.ExceptionReportType#getException()
     * @see #getExceptionReportType()
     * @generated
     */
    EReference getExceptionReportType_Exception();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ExceptionReportType#getLang <em>Lang</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lang</em>'.
     * @see net.opengis.ows11.ExceptionReportType#getLang()
     * @see #getExceptionReportType()
     * @generated
     */
    EAttribute getExceptionReportType_Lang();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ExceptionReportType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.ows11.ExceptionReportType#getVersion()
     * @see #getExceptionReportType()
     * @generated
     */
    EAttribute getExceptionReportType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ExceptionType <em>Exception Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Exception Type</em>'.
     * @see net.opengis.ows11.ExceptionType
     * @generated
     */
    EClass getExceptionType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ExceptionType#getExceptionText <em>Exception Text</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Exception Text</em>'.
     * @see net.opengis.ows11.ExceptionType#getExceptionText()
     * @see #getExceptionType()
     * @generated
     */
    EAttribute getExceptionType_ExceptionText();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ExceptionType#getExceptionCode <em>Exception Code</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Exception Code</em>'.
     * @see net.opengis.ows11.ExceptionType#getExceptionCode()
     * @see #getExceptionType()
     * @generated
     */
    EAttribute getExceptionType_ExceptionCode();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ExceptionType#getLocator <em>Locator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Locator</em>'.
     * @see net.opengis.ows11.ExceptionType#getLocator()
     * @see #getExceptionType()
     * @generated
     */
    EAttribute getExceptionType_Locator();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Capabilities Type</em>'.
     * @see net.opengis.ows11.GetCapabilitiesType
     * @generated
     */
    EClass getGetCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Accept Versions</em>'.
     * @see net.opengis.ows11.GetCapabilitiesType#getAcceptVersions()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EReference getGetCapabilitiesType_AcceptVersions();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.GetCapabilitiesType#getSections <em>Sections</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Sections</em>'.
     * @see net.opengis.ows11.GetCapabilitiesType#getSections()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EReference getGetCapabilitiesType_Sections();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.GetCapabilitiesType#getAcceptFormats <em>Accept Formats</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Accept Formats</em>'.
     * @see net.opengis.ows11.GetCapabilitiesType#getAcceptFormats()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EReference getGetCapabilitiesType_AcceptFormats();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Update Sequence</em>'.
     * @see net.opengis.ows11.GetCapabilitiesType#getUpdateSequence()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_UpdateSequence();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Base Url</em>'.
     * @see net.opengis.ows11.GetCapabilitiesType#getBaseUrl()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_BaseUrl();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.GetCapabilitiesType#getNamespace <em>Namespace</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Namespace</em>'.
     * @see net.opengis.ows11.GetCapabilitiesType#getNamespace()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_Namespace();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extended Properties</em>'.
     * @see net.opengis.ows11.GetCapabilitiesType#getExtendedProperties()
     * @see #getGetCapabilitiesType()
     * @generated
     */
	EAttribute getGetCapabilitiesType_ExtendedProperties();

				/**
     * Returns the meta object for class '{@link net.opengis.ows11.GetResourceByIdType <em>Get Resource By Id Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Resource By Id Type</em>'.
     * @see net.opengis.ows11.GetResourceByIdType
     * @generated
     */
    EClass getGetResourceByIdType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.GetResourceByIdType#getResourceID <em>Resource ID</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Resource ID</em>'.
     * @see net.opengis.ows11.GetResourceByIdType#getResourceID()
     * @see #getGetResourceByIdType()
     * @generated
     */
    EAttribute getGetResourceByIdType_ResourceID();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.GetResourceByIdType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.ows11.GetResourceByIdType#getOutputFormat()
     * @see #getGetResourceByIdType()
     * @generated
     */
    EAttribute getGetResourceByIdType_OutputFormat();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.GetResourceByIdType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.ows11.GetResourceByIdType#getService()
     * @see #getGetResourceByIdType()
     * @generated
     */
    EAttribute getGetResourceByIdType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.GetResourceByIdType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.ows11.GetResourceByIdType#getVersion()
     * @see #getGetResourceByIdType()
     * @generated
     */
    EAttribute getGetResourceByIdType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.HTTPType <em>HTTP Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>HTTP Type</em>'.
     * @see net.opengis.ows11.HTTPType
     * @generated
     */
    EClass getHTTPType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.ows11.HTTPType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see net.opengis.ows11.HTTPType#getGroup()
     * @see #getHTTPType()
     * @generated
     */
    EAttribute getHTTPType_Group();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.HTTPType#getGet <em>Get</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Get</em>'.
     * @see net.opengis.ows11.HTTPType#getGet()
     * @see #getHTTPType()
     * @generated
     */
    EReference getHTTPType_Get();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.HTTPType#getPost <em>Post</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Post</em>'.
     * @see net.opengis.ows11.HTTPType#getPost()
     * @see #getHTTPType()
     * @generated
     */
    EReference getHTTPType_Post();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.IdentificationType <em>Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Identification Type</em>'.
     * @see net.opengis.ows11.IdentificationType
     * @generated
     */
    EClass getIdentificationType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.ows11.IdentificationType#getBoundingBoxGroup <em>Bounding Box Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Bounding Box Group</em>'.
     * @see net.opengis.ows11.IdentificationType#getBoundingBoxGroup()
     * @see #getIdentificationType()
     * @generated
     */
    EAttribute getIdentificationType_BoundingBoxGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.IdentificationType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bounding Box</em>'.
     * @see net.opengis.ows11.IdentificationType#getBoundingBox()
     * @see #getIdentificationType()
     * @generated
     */
    EReference getIdentificationType_BoundingBox();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.IdentificationType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.ows11.IdentificationType#getOutputFormat()
     * @see #getIdentificationType()
     * @generated
     */
    EAttribute getIdentificationType_OutputFormat();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.ows11.IdentificationType#getAvailableCRSGroup <em>Available CRS Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Available CRS Group</em>'.
     * @see net.opengis.ows11.IdentificationType#getAvailableCRSGroup()
     * @see #getIdentificationType()
     * @generated
     */
    EAttribute getIdentificationType_AvailableCRSGroup();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.IdentificationType#getAvailableCRS <em>Available CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Available CRS</em>'.
     * @see net.opengis.ows11.IdentificationType#getAvailableCRS()
     * @see #getIdentificationType()
     * @generated
     */
    EAttribute getIdentificationType_AvailableCRS();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.KeywordsType <em>Keywords Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Keywords Type</em>'.
     * @see net.opengis.ows11.KeywordsType
     * @generated
     */
    EClass getKeywordsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.KeywordsType#getKeyword <em>Keyword</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Keyword</em>'.
     * @see net.opengis.ows11.KeywordsType#getKeyword()
     * @see #getKeywordsType()
     * @generated
     */
    EReference getKeywordsType_Keyword();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.KeywordsType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see net.opengis.ows11.KeywordsType#getType()
     * @see #getKeywordsType()
     * @generated
     */
    EReference getKeywordsType_Type();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.LanguageStringType <em>Language String Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Language String Type</em>'.
     * @see net.opengis.ows11.LanguageStringType
     * @generated
     */
    EClass getLanguageStringType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.LanguageStringType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.ows11.LanguageStringType#getValue()
     * @see #getLanguageStringType()
     * @generated
     */
    EAttribute getLanguageStringType_Value();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ManifestType <em>Manifest Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Manifest Type</em>'.
     * @see net.opengis.ows11.ManifestType
     * @generated
     */
    EClass getManifestType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.ManifestType#getReferenceGroup <em>Reference Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Reference Group</em>'.
     * @see net.opengis.ows11.ManifestType#getReferenceGroup()
     * @see #getManifestType()
     * @generated
     */
    EReference getManifestType_ReferenceGroup();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.MetadataType <em>Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Metadata Type</em>'.
     * @see net.opengis.ows11.MetadataType
     * @generated
     */
    EClass getMetadataType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.ows11.MetadataType#getAbstractMetaDataGroup <em>Abstract Meta Data Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Abstract Meta Data Group</em>'.
     * @see net.opengis.ows11.MetadataType#getAbstractMetaDataGroup()
     * @see #getMetadataType()
     * @generated
     */
    EAttribute getMetadataType_AbstractMetaDataGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.MetadataType#getAbstractMetaData <em>Abstract Meta Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Meta Data</em>'.
     * @see net.opengis.ows11.MetadataType#getAbstractMetaData()
     * @see #getMetadataType()
     * @generated
     */
    EReference getMetadataType_AbstractMetaData();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.MetadataType#getAbout <em>About</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>About</em>'.
     * @see net.opengis.ows11.MetadataType#getAbout()
     * @see #getMetadataType()
     * @generated
     */
    EAttribute getMetadataType_About();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.MetadataType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Title</em>'.
     * @see net.opengis.ows11.MetadataType#getTitle()
     * @see #getMetadataType()
     * @generated
     */
	EAttribute getMetadataType_Title();

				/**
     * Returns the meta object for class '{@link net.opengis.ows11.NoValuesType <em>No Values Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>No Values Type</em>'.
     * @see net.opengis.ows11.NoValuesType
     * @generated
     */
    EClass getNoValuesType();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.OnlineResourceType <em>Online Resource Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Online Resource Type</em>'.
     * @see net.opengis.ows11.OnlineResourceType
     * @generated
     */
    EClass getOnlineResourceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.OnlineResourceType#getHref <em>Href</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Href</em>'.
     * @see net.opengis.ows11.OnlineResourceType#getHref()
     * @see #getOnlineResourceType()
     * @generated
     */
	EAttribute getOnlineResourceType_Href();

				/**
     * Returns the meta object for class '{@link net.opengis.ows11.OperationsMetadataType <em>Operations Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Operations Metadata Type</em>'.
     * @see net.opengis.ows11.OperationsMetadataType
     * @generated
     */
    EClass getOperationsMetadataType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.OperationsMetadataType#getOperation <em>Operation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Operation</em>'.
     * @see net.opengis.ows11.OperationsMetadataType#getOperation()
     * @see #getOperationsMetadataType()
     * @generated
     */
    EReference getOperationsMetadataType_Operation();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.OperationsMetadataType#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter</em>'.
     * @see net.opengis.ows11.OperationsMetadataType#getParameter()
     * @see #getOperationsMetadataType()
     * @generated
     */
    EReference getOperationsMetadataType_Parameter();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.OperationsMetadataType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.ows11.OperationsMetadataType#getConstraint()
     * @see #getOperationsMetadataType()
     * @generated
     */
    EReference getOperationsMetadataType_Constraint();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.OperationsMetadataType#getExtendedCapabilities <em>Extended Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extended Capabilities</em>'.
     * @see net.opengis.ows11.OperationsMetadataType#getExtendedCapabilities()
     * @see #getOperationsMetadataType()
     * @generated
     */
    EAttribute getOperationsMetadataType_ExtendedCapabilities();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.OperationType <em>Operation Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Operation Type</em>'.
     * @see net.opengis.ows11.OperationType
     * @generated
     */
    EClass getOperationType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.OperationType#getDCP <em>DCP</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>DCP</em>'.
     * @see net.opengis.ows11.OperationType#getDCP()
     * @see #getOperationType()
     * @generated
     */
    EReference getOperationType_DCP();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.OperationType#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter</em>'.
     * @see net.opengis.ows11.OperationType#getParameter()
     * @see #getOperationType()
     * @generated
     */
    EReference getOperationType_Parameter();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.OperationType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.ows11.OperationType#getConstraint()
     * @see #getOperationType()
     * @generated
     */
    EReference getOperationType_Constraint();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.OperationType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.ows11.OperationType#getMetadata()
     * @see #getOperationType()
     * @generated
     */
    EReference getOperationType_Metadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.OperationType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.ows11.OperationType#getName()
     * @see #getOperationType()
     * @generated
     */
    EAttribute getOperationType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.RangeType <em>Range Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Range Type</em>'.
     * @see net.opengis.ows11.RangeType
     * @generated
     */
    EClass getRangeType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.RangeType#getMinimumValue <em>Minimum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Minimum Value</em>'.
     * @see net.opengis.ows11.RangeType#getMinimumValue()
     * @see #getRangeType()
     * @generated
     */
    EReference getRangeType_MinimumValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.RangeType#getMaximumValue <em>Maximum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Maximum Value</em>'.
     * @see net.opengis.ows11.RangeType#getMaximumValue()
     * @see #getRangeType()
     * @generated
     */
    EReference getRangeType_MaximumValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.RangeType#getSpacing <em>Spacing</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spacing</em>'.
     * @see net.opengis.ows11.RangeType#getSpacing()
     * @see #getRangeType()
     * @generated
     */
    EReference getRangeType_Spacing();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.RangeType#getRangeClosure <em>Range Closure</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Range Closure</em>'.
     * @see net.opengis.ows11.RangeType#getRangeClosure()
     * @see #getRangeType()
     * @generated
     */
    EAttribute getRangeType_RangeClosure();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ReferenceGroupType <em>Reference Group Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Reference Group Type</em>'.
     * @see net.opengis.ows11.ReferenceGroupType
     * @generated
     */
    EClass getReferenceGroupType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.ows11.ReferenceGroupType#getAbstractReferenceBaseGroup <em>Abstract Reference Base Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Abstract Reference Base Group</em>'.
     * @see net.opengis.ows11.ReferenceGroupType#getAbstractReferenceBaseGroup()
     * @see #getReferenceGroupType()
     * @generated
     */
    EAttribute getReferenceGroupType_AbstractReferenceBaseGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.ReferenceGroupType#getAbstractReferenceBase <em>Abstract Reference Base</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Abstract Reference Base</em>'.
     * @see net.opengis.ows11.ReferenceGroupType#getAbstractReferenceBase()
     * @see #getReferenceGroupType()
     * @generated
     */
    EReference getReferenceGroupType_AbstractReferenceBase();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ReferenceType <em>Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Reference Type</em>'.
     * @see net.opengis.ows11.ReferenceType
     * @generated
     */
    EClass getReferenceType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ReferenceType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.ows11.ReferenceType#getIdentifier()
     * @see #getReferenceType()
     * @generated
     */
    EReference getReferenceType_Identifier();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.ReferenceType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Abstract</em>'.
     * @see net.opengis.ows11.ReferenceType#getAbstract()
     * @see #getReferenceType()
     * @generated
     */
    EReference getReferenceType_Abstract();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ReferenceType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.ows11.ReferenceType#getFormat()
     * @see #getReferenceType()
     * @generated
     */
    EAttribute getReferenceType_Format();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.ReferenceType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.ows11.ReferenceType#getMetadata()
     * @see #getReferenceType()
     * @generated
     */
    EReference getReferenceType_Metadata();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.RequestMethodType <em>Request Method Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Request Method Type</em>'.
     * @see net.opengis.ows11.RequestMethodType
     * @generated
     */
    EClass getRequestMethodType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.RequestMethodType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.ows11.RequestMethodType#getConstraint()
     * @see #getRequestMethodType()
     * @generated
     */
    EReference getRequestMethodType_Constraint();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ResponsiblePartySubsetType <em>Responsible Party Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Responsible Party Subset Type</em>'.
     * @see net.opengis.ows11.ResponsiblePartySubsetType
     * @generated
     */
    EClass getResponsiblePartySubsetType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ResponsiblePartySubsetType#getIndividualName <em>Individual Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Individual Name</em>'.
     * @see net.opengis.ows11.ResponsiblePartySubsetType#getIndividualName()
     * @see #getResponsiblePartySubsetType()
     * @generated
     */
    EAttribute getResponsiblePartySubsetType_IndividualName();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ResponsiblePartySubsetType#getPositionName <em>Position Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Position Name</em>'.
     * @see net.opengis.ows11.ResponsiblePartySubsetType#getPositionName()
     * @see #getResponsiblePartySubsetType()
     * @generated
     */
    EAttribute getResponsiblePartySubsetType_PositionName();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ResponsiblePartySubsetType#getContactInfo <em>Contact Info</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contact Info</em>'.
     * @see net.opengis.ows11.ResponsiblePartySubsetType#getContactInfo()
     * @see #getResponsiblePartySubsetType()
     * @generated
     */
    EReference getResponsiblePartySubsetType_ContactInfo();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ResponsiblePartySubsetType#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Role</em>'.
     * @see net.opengis.ows11.ResponsiblePartySubsetType#getRole()
     * @see #getResponsiblePartySubsetType()
     * @generated
     */
    EReference getResponsiblePartySubsetType_Role();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ResponsiblePartyType <em>Responsible Party Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Responsible Party Type</em>'.
     * @see net.opengis.ows11.ResponsiblePartyType
     * @generated
     */
    EClass getResponsiblePartyType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ResponsiblePartyType#getIndividualName <em>Individual Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Individual Name</em>'.
     * @see net.opengis.ows11.ResponsiblePartyType#getIndividualName()
     * @see #getResponsiblePartyType()
     * @generated
     */
    EAttribute getResponsiblePartyType_IndividualName();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ResponsiblePartyType#getOrganisationName <em>Organisation Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Organisation Name</em>'.
     * @see net.opengis.ows11.ResponsiblePartyType#getOrganisationName()
     * @see #getResponsiblePartyType()
     * @generated
     */
    EAttribute getResponsiblePartyType_OrganisationName();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ResponsiblePartyType#getPositionName <em>Position Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Position Name</em>'.
     * @see net.opengis.ows11.ResponsiblePartyType#getPositionName()
     * @see #getResponsiblePartyType()
     * @generated
     */
    EAttribute getResponsiblePartyType_PositionName();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ResponsiblePartyType#getContactInfo <em>Contact Info</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contact Info</em>'.
     * @see net.opengis.ows11.ResponsiblePartyType#getContactInfo()
     * @see #getResponsiblePartyType()
     * @generated
     */
    EReference getResponsiblePartyType_ContactInfo();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ResponsiblePartyType#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Role</em>'.
     * @see net.opengis.ows11.ResponsiblePartyType#getRole()
     * @see #getResponsiblePartyType()
     * @generated
     */
    EReference getResponsiblePartyType_Role();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.SectionsType <em>Sections Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sections Type</em>'.
     * @see net.opengis.ows11.SectionsType
     * @generated
     */
    EClass getSectionsType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.SectionsType#getSection <em>Section</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Section</em>'.
     * @see net.opengis.ows11.SectionsType#getSection()
     * @see #getSectionsType()
     * @generated
     */
    EAttribute getSectionsType_Section();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ServiceIdentificationType <em>Service Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Identification Type</em>'.
     * @see net.opengis.ows11.ServiceIdentificationType
     * @generated
     */
    EClass getServiceIdentificationType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ServiceIdentificationType#getServiceType <em>Service Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Type</em>'.
     * @see net.opengis.ows11.ServiceIdentificationType#getServiceType()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EReference getServiceIdentificationType_ServiceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ServiceIdentificationType#getServiceTypeVersion <em>Service Type Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service Type Version</em>'.
     * @see net.opengis.ows11.ServiceIdentificationType#getServiceTypeVersion()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_ServiceTypeVersion();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ServiceIdentificationType#getProfile <em>Profile</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Profile</em>'.
     * @see net.opengis.ows11.ServiceIdentificationType#getProfile()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_Profile();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ServiceIdentificationType#getFees <em>Fees</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fees</em>'.
     * @see net.opengis.ows11.ServiceIdentificationType#getFees()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_Fees();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ServiceIdentificationType#getAccessConstraints <em>Access Constraints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Access Constraints</em>'.
     * @see net.opengis.ows11.ServiceIdentificationType#getAccessConstraints()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_AccessConstraints();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ServiceProviderType <em>Service Provider Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Provider Type</em>'.
     * @see net.opengis.ows11.ServiceProviderType
     * @generated
     */
    EClass getServiceProviderType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ServiceProviderType#getProviderName <em>Provider Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Provider Name</em>'.
     * @see net.opengis.ows11.ServiceProviderType#getProviderName()
     * @see #getServiceProviderType()
     * @generated
     */
    EAttribute getServiceProviderType_ProviderName();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ServiceProviderType#getProviderSite <em>Provider Site</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Provider Site</em>'.
     * @see net.opengis.ows11.ServiceProviderType#getProviderSite()
     * @see #getServiceProviderType()
     * @generated
     */
    EReference getServiceProviderType_ProviderSite();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ServiceProviderType#getServiceContact <em>Service Contact</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Contact</em>'.
     * @see net.opengis.ows11.ServiceProviderType#getServiceContact()
     * @see #getServiceProviderType()
     * @generated
     */
    EReference getServiceProviderType_ServiceContact();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ServiceReferenceType <em>Service Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Reference Type</em>'.
     * @see net.opengis.ows11.ServiceReferenceType
     * @generated
     */
    EClass getServiceReferenceType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.ServiceReferenceType#getRequestMessage <em>Request Message</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Request Message</em>'.
     * @see net.opengis.ows11.ServiceReferenceType#getRequestMessage()
     * @see #getServiceReferenceType()
     * @generated
     */
    EReference getServiceReferenceType_RequestMessage();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ServiceReferenceType#getRequestMessageReference <em>Request Message Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Message Reference</em>'.
     * @see net.opengis.ows11.ServiceReferenceType#getRequestMessageReference()
     * @see #getServiceReferenceType()
     * @generated
     */
    EAttribute getServiceReferenceType_RequestMessageReference();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.TelephoneType <em>Telephone Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Telephone Type</em>'.
     * @see net.opengis.ows11.TelephoneType
     * @generated
     */
    EClass getTelephoneType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.TelephoneType#getVoice <em>Voice</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Voice</em>'.
     * @see net.opengis.ows11.TelephoneType#getVoice()
     * @see #getTelephoneType()
     * @generated
     */
    EAttribute getTelephoneType_Voice();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.TelephoneType#getFacsimile <em>Facsimile</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Facsimile</em>'.
     * @see net.opengis.ows11.TelephoneType#getFacsimile()
     * @see #getTelephoneType()
     * @generated
     */
    EAttribute getTelephoneType_Facsimile();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.UnNamedDomainType <em>Un Named Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Un Named Domain Type</em>'.
     * @see net.opengis.ows11.UnNamedDomainType
     * @generated
     */
    EClass getUnNamedDomainType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getAllowedValues <em>Allowed Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Allowed Values</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getAllowedValues()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_AllowedValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getAnyValue <em>Any Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Any Value</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getAnyValue()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_AnyValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getNoValues <em>No Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>No Values</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getNoValues()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_NoValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getValuesReference <em>Values Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Values Reference</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getValuesReference()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_ValuesReference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getDefaultValue <em>Default Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default Value</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getDefaultValue()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_DefaultValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Meaning</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getMeaning()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_Meaning();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getDataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Type</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getDataType()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_DataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getUOM <em>UOM</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>UOM</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getUOM()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_UOM();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.ows11.UnNamedDomainType#getReferenceSystem <em>Reference System</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference System</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getReferenceSystem()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_ReferenceSystem();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows11.UnNamedDomainType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.ows11.UnNamedDomainType#getMetadata()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_Metadata();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ValuesReferenceType <em>Values Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Values Reference Type</em>'.
     * @see net.opengis.ows11.ValuesReferenceType
     * @generated
     */
    EClass getValuesReferenceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ValuesReferenceType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.ows11.ValuesReferenceType#getValue()
     * @see #getValuesReferenceType()
     * @generated
     */
    EAttribute getValuesReferenceType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ValuesReferenceType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reference</em>'.
     * @see net.opengis.ows11.ValuesReferenceType#getReference()
     * @see #getValuesReferenceType()
     * @generated
     */
    EAttribute getValuesReferenceType_Reference();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.ValueType <em>Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Value Type</em>'.
     * @see net.opengis.ows11.ValueType
     * @generated
     */
    EClass getValueType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows11.ValueType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.ows11.ValueType#getValue()
     * @see #getValueType()
     * @generated
     */
    EAttribute getValueType_Value();

    /**
     * Returns the meta object for class '{@link net.opengis.ows11.WGS84BoundingBoxType <em>WGS84 Bounding Box Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>WGS84 Bounding Box Type</em>'.
     * @see net.opengis.ows11.WGS84BoundingBoxType
     * @generated
     */
    EClass getWGS84BoundingBoxType();

    /**
     * Returns the meta object for enum '{@link net.opengis.ows11.RangeClosureType <em>Range Closure Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Range Closure Type</em>'.
     * @see net.opengis.ows11.RangeClosureType
     * @generated
     */
    EEnum getRangeClosureType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Mime Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Mime Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='MimeType' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='(application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*'"
     * @generated
     */
    EDataType getMimeType();

    /**
     * Returns the meta object for data type '{@link java.util.List <em>Position Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Position Type</em>'.
     * @see java.util.List
     * @model instanceClass="java.util.List"
     *        extendedMetaData="name='PositionType' itemType='http://www.eclipse.org/emf/2003/XMLType#double'"
     * @generated
     */
    EDataType getPositionType();

    /**
     * Returns the meta object for data type '{@link java.util.List <em>Position Type2 D</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Position Type2 D</em>'.
     * @see java.util.List
     * @model instanceClass="java.util.List"
     *        extendedMetaData="name='PositionType2D' baseType='PositionType' length='2'"
     * @generated
     */
    EDataType getPositionType2D();

    /**
     * Returns the meta object for data type '{@link net.opengis.ows11.RangeClosureType <em>Range Closure Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Range Closure Type Object</em>'.
     * @see net.opengis.ows11.RangeClosureType
     * @model instanceClass="net.opengis.ows11.RangeClosureType"
     *        extendedMetaData="name='rangeClosure_._type:Object' baseType='rangeClosure_._type'"
     * @generated
     */
    EDataType getRangeClosureTypeObject();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Service Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Service Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='ServiceType' baseType='http://www.eclipse.org/emf/2003/XMLType#string'"
     * @generated
     */
    EDataType getServiceType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Update Sequence Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Update Sequence Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='UpdateSequenceType' baseType='http://www.eclipse.org/emf/2003/XMLType#string'"
     * @generated
     */
    EDataType getUpdateSequenceType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Version Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Version Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='VersionType' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='\\d+\\.\\d?\\d\\.\\d?\\d'"
     * @generated
     */
    EDataType getVersionType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Version Type1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Version Type1</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='version_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='\\d+\\.\\d?\\d\\.\\d?\\d'"
     * @generated
     */
    EDataType getVersionType1();

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
    Ows11Factory getOws11Factory();

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
         * The meta object literal for the '{@link net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl <em>Abstract Reference Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.AbstractReferenceBaseTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getAbstractReferenceBaseType()
         * @generated
         */
        EClass ABSTRACT_REFERENCE_BASE_TYPE = eINSTANCE.getAbstractReferenceBaseType();

        /**
         * The meta object literal for the '<em><b>Actuate</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE = eINSTANCE.getAbstractReferenceBaseType_Actuate();

        /**
         * The meta object literal for the '<em><b>Arcrole</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE = eINSTANCE.getAbstractReferenceBaseType_Arcrole();

        /**
         * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__HREF = eINSTANCE.getAbstractReferenceBaseType_Href();

        /**
         * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__ROLE = eINSTANCE.getAbstractReferenceBaseType_Role();

        /**
         * The meta object literal for the '<em><b>Show</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__SHOW = eINSTANCE.getAbstractReferenceBaseType_Show();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__TITLE = eINSTANCE.getAbstractReferenceBaseType_Title();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__TYPE = eINSTANCE.getAbstractReferenceBaseType_Type();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.AcceptFormatsTypeImpl <em>Accept Formats Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.AcceptFormatsTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getAcceptFormatsType()
         * @generated
         */
        EClass ACCEPT_FORMATS_TYPE = eINSTANCE.getAcceptFormatsType();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT = eINSTANCE.getAcceptFormatsType_OutputFormat();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.AcceptVersionsTypeImpl <em>Accept Versions Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.AcceptVersionsTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getAcceptVersionsType()
         * @generated
         */
        EClass ACCEPT_VERSIONS_TYPE = eINSTANCE.getAcceptVersionsType();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ACCEPT_VERSIONS_TYPE__VERSION = eINSTANCE.getAcceptVersionsType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.AddressTypeImpl <em>Address Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.AddressTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getAddressType()
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
         * The meta object literal for the '{@link net.opengis.ows11.impl.AllowedValuesTypeImpl <em>Allowed Values Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.AllowedValuesTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getAllowedValuesType()
         * @generated
         */
        EClass ALLOWED_VALUES_TYPE = eINSTANCE.getAllowedValuesType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ALLOWED_VALUES_TYPE__GROUP = eINSTANCE.getAllowedValuesType_Group();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALLOWED_VALUES_TYPE__VALUE = eINSTANCE.getAllowedValuesType_Value();

        /**
         * The meta object literal for the '<em><b>Range</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALLOWED_VALUES_TYPE__RANGE = eINSTANCE.getAllowedValuesType_Range();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.AnyValueTypeImpl <em>Any Value Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.AnyValueTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getAnyValueType()
         * @generated
         */
        EClass ANY_VALUE_TYPE = eINSTANCE.getAnyValueType();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.BasicIdentificationTypeImpl <em>Basic Identification Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.BasicIdentificationTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getBasicIdentificationType()
         * @generated
         */
        EClass BASIC_IDENTIFICATION_TYPE = eINSTANCE.getBasicIdentificationType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BASIC_IDENTIFICATION_TYPE__IDENTIFIER = eINSTANCE.getBasicIdentificationType_Identifier();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BASIC_IDENTIFICATION_TYPE__METADATA = eINSTANCE.getBasicIdentificationType_Metadata();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.BoundingBoxTypeImpl <em>Bounding Box Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.BoundingBoxTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getBoundingBoxType()
         * @generated
         */
        EClass BOUNDING_BOX_TYPE = eINSTANCE.getBoundingBoxType();

        /**
         * The meta object literal for the '<em><b>Lower Corner</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOUNDING_BOX_TYPE__LOWER_CORNER = eINSTANCE.getBoundingBoxType_LowerCorner();

        /**
         * The meta object literal for the '<em><b>Upper Corner</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOUNDING_BOX_TYPE__UPPER_CORNER = eINSTANCE.getBoundingBoxType_UpperCorner();

        /**
         * The meta object literal for the '<em><b>Crs</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOUNDING_BOX_TYPE__CRS = eINSTANCE.getBoundingBoxType_Crs();

        /**
         * The meta object literal for the '<em><b>Dimensions</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BOUNDING_BOX_TYPE__DIMENSIONS = eINSTANCE.getBoundingBoxType_Dimensions();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.CapabilitiesBaseTypeImpl <em>Capabilities Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.CapabilitiesBaseTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getCapabilitiesBaseType()
         * @generated
         */
        EClass CAPABILITIES_BASE_TYPE = eINSTANCE.getCapabilitiesBaseType();

        /**
         * The meta object literal for the '<em><b>Service Identification</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION = eINSTANCE.getCapabilitiesBaseType_ServiceIdentification();

        /**
         * The meta object literal for the '<em><b>Service Provider</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER = eINSTANCE.getCapabilitiesBaseType_ServiceProvider();

        /**
         * The meta object literal for the '<em><b>Operations Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA = eINSTANCE.getCapabilitiesBaseType_OperationsMetadata();

        /**
         * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE = eINSTANCE.getCapabilitiesBaseType_UpdateSequence();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CAPABILITIES_BASE_TYPE__VERSION = eINSTANCE.getCapabilitiesBaseType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.CodeTypeImpl <em>Code Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.CodeTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getCodeType()
         * @generated
         */
        EClass CODE_TYPE = eINSTANCE.getCodeType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CODE_TYPE__VALUE = eINSTANCE.getCodeType_Value();

        /**
         * The meta object literal for the '<em><b>Code Space</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CODE_TYPE__CODE_SPACE = eINSTANCE.getCodeType_CodeSpace();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ContactTypeImpl <em>Contact Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ContactTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getContactType()
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
         * The meta object literal for the '<em><b>Hours Of Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONTACT_TYPE__HOURS_OF_SERVICE = eINSTANCE.getContactType_HoursOfService();

        /**
         * The meta object literal for the '<em><b>Contact Instructions</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONTACT_TYPE__CONTACT_INSTRUCTIONS = eINSTANCE.getContactType_ContactInstructions();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ContentsBaseTypeImpl <em>Contents Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ContentsBaseTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getContentsBaseType()
         * @generated
         */
        EClass CONTENTS_BASE_TYPE = eINSTANCE.getContentsBaseType();

        /**
         * The meta object literal for the '<em><b>Dataset Description Summary</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY = eINSTANCE.getContentsBaseType_DatasetDescriptionSummary();

        /**
         * The meta object literal for the '<em><b>Other Source</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTENTS_BASE_TYPE__OTHER_SOURCE = eINSTANCE.getContentsBaseType_OtherSource();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl <em>Dataset Description Summary Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.DatasetDescriptionSummaryBaseTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getDatasetDescriptionSummaryBaseType()
         * @generated
         */
        EClass DATASET_DESCRIPTION_SUMMARY_BASE_TYPE = eINSTANCE.getDatasetDescriptionSummaryBaseType();

        /**
         * The meta object literal for the '<em><b>WGS84 Bounding Box</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX = eINSTANCE.getDatasetDescriptionSummaryBaseType_WGS84BoundingBox();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER = eINSTANCE.getDatasetDescriptionSummaryBaseType_Identifier();

        /**
         * The meta object literal for the '<em><b>Bounding Box Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP = eINSTANCE.getDatasetDescriptionSummaryBaseType_BoundingBoxGroup();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX = eINSTANCE.getDatasetDescriptionSummaryBaseType_BoundingBox();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA = eINSTANCE.getDatasetDescriptionSummaryBaseType_Metadata();

        /**
         * The meta object literal for the '<em><b>Dataset Description Summary</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY = eINSTANCE.getDatasetDescriptionSummaryBaseType_DatasetDescriptionSummary();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.DCPTypeImpl <em>DCP Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.DCPTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getDCPType()
         * @generated
         */
        EClass DCP_TYPE = eINSTANCE.getDCPType();

        /**
         * The meta object literal for the '<em><b>HTTP</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DCP_TYPE__HTTP = eINSTANCE.getDCPType_HTTP();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.DescriptionTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getDescriptionType()
         * @generated
         */
        EClass DESCRIPTION_TYPE = eINSTANCE.getDescriptionType();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIPTION_TYPE__TITLE = eINSTANCE.getDescriptionType_Title();

        /**
         * The meta object literal for the '<em><b>Abstract</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIPTION_TYPE__ABSTRACT = eINSTANCE.getDescriptionType_Abstract();

        /**
         * The meta object literal for the '<em><b>Keywords</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIPTION_TYPE__KEYWORDS = eINSTANCE.getDescriptionType_Keywords();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.DocumentRootImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getDocumentRoot()
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
         * The meta object literal for the '<em><b>Abstract</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT = eINSTANCE.getDocumentRoot_Abstract();

        /**
         * The meta object literal for the '<em><b>Abstract Meta Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT_META_DATA = eINSTANCE.getDocumentRoot_AbstractMetaData();

        /**
         * The meta object literal for the '<em><b>Abstract Reference Base</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE = eINSTANCE.getDocumentRoot_AbstractReferenceBase();

        /**
         * The meta object literal for the '<em><b>Access Constraints</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__ACCESS_CONSTRAINTS = eINSTANCE.getDocumentRoot_AccessConstraints();

        /**
         * The meta object literal for the '<em><b>Allowed Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ALLOWED_VALUES = eINSTANCE.getDocumentRoot_AllowedValues();

        /**
         * The meta object literal for the '<em><b>Any Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANY_VALUE = eINSTANCE.getDocumentRoot_AnyValue();

        /**
         * The meta object literal for the '<em><b>Available CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__AVAILABLE_CRS = eINSTANCE.getDocumentRoot_AvailableCRS();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__BOUNDING_BOX = eINSTANCE.getDocumentRoot_BoundingBox();

        /**
         * The meta object literal for the '<em><b>Contact Info</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__CONTACT_INFO = eINSTANCE.getDocumentRoot_ContactInfo();

        /**
         * The meta object literal for the '<em><b>Dataset Description Summary</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DATASET_DESCRIPTION_SUMMARY = eINSTANCE.getDocumentRoot_DatasetDescriptionSummary();

        /**
         * The meta object literal for the '<em><b>Data Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DATA_TYPE = eINSTANCE.getDocumentRoot_DataType();

        /**
         * The meta object literal for the '<em><b>DCP</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DCP = eINSTANCE.getDocumentRoot_DCP();

        /**
         * The meta object literal for the '<em><b>Default Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DEFAULT_VALUE = eINSTANCE.getDocumentRoot_DefaultValue();

        /**
         * The meta object literal for the '<em><b>Exception</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXCEPTION = eINSTANCE.getDocumentRoot_Exception();

        /**
         * The meta object literal for the '<em><b>Exception Report</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXCEPTION_REPORT = eINSTANCE.getDocumentRoot_ExceptionReport();

        /**
         * The meta object literal for the '<em><b>Extended Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXTENDED_CAPABILITIES = eINSTANCE.getDocumentRoot_ExtendedCapabilities();

        /**
         * The meta object literal for the '<em><b>Fees</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__FEES = eINSTANCE.getDocumentRoot_Fees();

        /**
         * The meta object literal for the '<em><b>Get Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_CAPABILITIES = eINSTANCE.getDocumentRoot_GetCapabilities();

        /**
         * The meta object literal for the '<em><b>Get Resource By ID</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_RESOURCE_BY_ID = eINSTANCE.getDocumentRoot_GetResourceByID();

        /**
         * The meta object literal for the '<em><b>HTTP</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__HTTP = eINSTANCE.getDocumentRoot_HTTP();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__IDENTIFIER = eINSTANCE.getDocumentRoot_Identifier();

        /**
         * The meta object literal for the '<em><b>Individual Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__INDIVIDUAL_NAME = eINSTANCE.getDocumentRoot_IndividualName();

        /**
         * The meta object literal for the '<em><b>Input Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__INPUT_DATA = eINSTANCE.getDocumentRoot_InputData();

        /**
         * The meta object literal for the '<em><b>Keywords</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__KEYWORDS = eINSTANCE.getDocumentRoot_Keywords();

        /**
         * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__LANGUAGE = eINSTANCE.getDocumentRoot_Language();

        /**
         * The meta object literal for the '<em><b>Manifest</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MANIFEST = eINSTANCE.getDocumentRoot_Manifest();

        /**
         * The meta object literal for the '<em><b>Maximum Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MAXIMUM_VALUE = eINSTANCE.getDocumentRoot_MaximumValue();

        /**
         * The meta object literal for the '<em><b>Meaning</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MEANING = eINSTANCE.getDocumentRoot_Meaning();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__METADATA = eINSTANCE.getDocumentRoot_Metadata();

        /**
         * The meta object literal for the '<em><b>Minimum Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MINIMUM_VALUE = eINSTANCE.getDocumentRoot_MinimumValue();

        /**
         * The meta object literal for the '<em><b>No Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__NO_VALUES = eINSTANCE.getDocumentRoot_NoValues();

        /**
         * The meta object literal for the '<em><b>Operation</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OPERATION = eINSTANCE.getDocumentRoot_Operation();

        /**
         * The meta object literal for the '<em><b>Operation Response</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OPERATION_RESPONSE = eINSTANCE.getDocumentRoot_OperationResponse();

        /**
         * The meta object literal for the '<em><b>Operations Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OPERATIONS_METADATA = eINSTANCE.getDocumentRoot_OperationsMetadata();

        /**
         * The meta object literal for the '<em><b>Organisation Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__ORGANISATION_NAME = eINSTANCE.getDocumentRoot_OrganisationName();

        /**
         * The meta object literal for the '<em><b>Other Source</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OTHER_SOURCE = eINSTANCE.getDocumentRoot_OtherSource();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__OUTPUT_FORMAT = eINSTANCE.getDocumentRoot_OutputFormat();

        /**
         * The meta object literal for the '<em><b>Point Of Contact</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__POINT_OF_CONTACT = eINSTANCE.getDocumentRoot_PointOfContact();

        /**
         * The meta object literal for the '<em><b>Position Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__POSITION_NAME = eINSTANCE.getDocumentRoot_PositionName();

        /**
         * The meta object literal for the '<em><b>Range</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__RANGE = eINSTANCE.getDocumentRoot_Range();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__REFERENCE = eINSTANCE.getDocumentRoot_Reference();

        /**
         * The meta object literal for the '<em><b>Reference Group</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__REFERENCE_GROUP = eINSTANCE.getDocumentRoot_ReferenceGroup();

        /**
         * The meta object literal for the '<em><b>Reference System</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__REFERENCE_SYSTEM = eINSTANCE.getDocumentRoot_ReferenceSystem();

        /**
         * The meta object literal for the '<em><b>Resource</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__RESOURCE = eINSTANCE.getDocumentRoot_Resource();

        /**
         * The meta object literal for the '<em><b>Role</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ROLE = eINSTANCE.getDocumentRoot_Role();

        /**
         * The meta object literal for the '<em><b>Service Identification</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SERVICE_IDENTIFICATION = eINSTANCE.getDocumentRoot_ServiceIdentification();

        /**
         * The meta object literal for the '<em><b>Service Provider</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SERVICE_PROVIDER = eINSTANCE.getDocumentRoot_ServiceProvider();

        /**
         * The meta object literal for the '<em><b>Service Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SERVICE_REFERENCE = eINSTANCE.getDocumentRoot_ServiceReference();

        /**
         * The meta object literal for the '<em><b>Spacing</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SPACING = eINSTANCE.getDocumentRoot_Spacing();

        /**
         * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__SUPPORTED_CRS = eINSTANCE.getDocumentRoot_SupportedCRS();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TITLE = eINSTANCE.getDocumentRoot_Title();

        /**
         * The meta object literal for the '<em><b>UOM</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__UOM = eINSTANCE.getDocumentRoot_UOM();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__VALUE = eINSTANCE.getDocumentRoot_Value();

        /**
         * The meta object literal for the '<em><b>Values Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__VALUES_REFERENCE = eINSTANCE.getDocumentRoot_ValuesReference();

        /**
         * The meta object literal for the '<em><b>WGS84 Bounding Box</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__WGS84_BOUNDING_BOX = eINSTANCE.getDocumentRoot_WGS84BoundingBox();

        /**
         * The meta object literal for the '<em><b>Range Closure</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__RANGE_CLOSURE = eINSTANCE.getDocumentRoot_RangeClosure();

        /**
         * The meta object literal for the '<em><b>Reference1</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__REFERENCE1 = eINSTANCE.getDocumentRoot_Reference1();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.DomainMetadataTypeImpl <em>Domain Metadata Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.DomainMetadataTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getDomainMetadataType()
         * @generated
         */
        EClass DOMAIN_METADATA_TYPE = eINSTANCE.getDomainMetadataType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_METADATA_TYPE__VALUE = eINSTANCE.getDomainMetadataType_Value();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_METADATA_TYPE__REFERENCE = eINSTANCE.getDomainMetadataType_Reference();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.DomainTypeImpl <em>Domain Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.DomainTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getDomainType()
         * @generated
         */
        EClass DOMAIN_TYPE = eINSTANCE.getDomainType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_TYPE__NAME = eINSTANCE.getDomainType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ExceptionReportTypeImpl <em>Exception Report Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ExceptionReportTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getExceptionReportType()
         * @generated
         */
        EClass EXCEPTION_REPORT_TYPE = eINSTANCE.getExceptionReportType();

        /**
         * The meta object literal for the '<em><b>Exception</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXCEPTION_REPORT_TYPE__EXCEPTION = eINSTANCE.getExceptionReportType_Exception();

        /**
         * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXCEPTION_REPORT_TYPE__LANG = eINSTANCE.getExceptionReportType_Lang();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXCEPTION_REPORT_TYPE__VERSION = eINSTANCE.getExceptionReportType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ExceptionTypeImpl <em>Exception Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ExceptionTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getExceptionType()
         * @generated
         */
        EClass EXCEPTION_TYPE = eINSTANCE.getExceptionType();

        /**
         * The meta object literal for the '<em><b>Exception Text</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXCEPTION_TYPE__EXCEPTION_TEXT = eINSTANCE.getExceptionType_ExceptionText();

        /**
         * The meta object literal for the '<em><b>Exception Code</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXCEPTION_TYPE__EXCEPTION_CODE = eINSTANCE.getExceptionType_ExceptionCode();

        /**
         * The meta object literal for the '<em><b>Locator</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXCEPTION_TYPE__LOCATOR = eINSTANCE.getExceptionType_Locator();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.GetCapabilitiesTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getGetCapabilitiesType()
         * @generated
         */
        EClass GET_CAPABILITIES_TYPE = eINSTANCE.getGetCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Accept Versions</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = eINSTANCE.getGetCapabilitiesType_AcceptVersions();

        /**
         * The meta object literal for the '<em><b>Sections</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_CAPABILITIES_TYPE__SECTIONS = eINSTANCE.getGetCapabilitiesType_Sections();

        /**
         * The meta object literal for the '<em><b>Accept Formats</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = eINSTANCE.getGetCapabilitiesType_AcceptFormats();

        /**
         * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = eINSTANCE.getGetCapabilitiesType_UpdateSequence();

        /**
         * The meta object literal for the '<em><b>Base Url</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_CAPABILITIES_TYPE__BASE_URL = eINSTANCE.getGetCapabilitiesType_BaseUrl();

        /**
         * The meta object literal for the '<em><b>Namespace</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_CAPABILITIES_TYPE__NAMESPACE = eINSTANCE.getGetCapabilitiesType_Namespace();

        /**
         * The meta object literal for the '<em><b>Extended Properties</b></em>' attribute feature.
         * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
         * @generated
         */
		EAttribute GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES = eINSTANCE.getGetCapabilitiesType_ExtendedProperties();

								/**
         * The meta object literal for the '{@link net.opengis.ows11.impl.GetResourceByIdTypeImpl <em>Get Resource By Id Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.GetResourceByIdTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getGetResourceByIdType()
         * @generated
         */
        EClass GET_RESOURCE_BY_ID_TYPE = eINSTANCE.getGetResourceByIdType();

        /**
         * The meta object literal for the '<em><b>Resource ID</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RESOURCE_BY_ID_TYPE__RESOURCE_ID = eINSTANCE.getGetResourceByIdType_ResourceID();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RESOURCE_BY_ID_TYPE__OUTPUT_FORMAT = eINSTANCE.getGetResourceByIdType_OutputFormat();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RESOURCE_BY_ID_TYPE__SERVICE = eINSTANCE.getGetResourceByIdType_Service();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RESOURCE_BY_ID_TYPE__VERSION = eINSTANCE.getGetResourceByIdType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.HTTPTypeImpl <em>HTTP Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.HTTPTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getHTTPType()
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
         * The meta object literal for the '{@link net.opengis.ows11.impl.IdentificationTypeImpl <em>Identification Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.IdentificationTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getIdentificationType()
         * @generated
         */
        EClass IDENTIFICATION_TYPE = eINSTANCE.getIdentificationType();

        /**
         * The meta object literal for the '<em><b>Bounding Box Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP = eINSTANCE.getIdentificationType_BoundingBoxGroup();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IDENTIFICATION_TYPE__BOUNDING_BOX = eINSTANCE.getIdentificationType_BoundingBox();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFICATION_TYPE__OUTPUT_FORMAT = eINSTANCE.getIdentificationType_OutputFormat();

        /**
         * The meta object literal for the '<em><b>Available CRS Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP = eINSTANCE.getIdentificationType_AvailableCRSGroup();

        /**
         * The meta object literal for the '<em><b>Available CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFICATION_TYPE__AVAILABLE_CRS = eINSTANCE.getIdentificationType_AvailableCRS();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.KeywordsTypeImpl <em>Keywords Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.KeywordsTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getKeywordsType()
         * @generated
         */
        EClass KEYWORDS_TYPE = eINSTANCE.getKeywordsType();

        /**
         * The meta object literal for the '<em><b>Keyword</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference KEYWORDS_TYPE__KEYWORD = eINSTANCE.getKeywordsType_Keyword();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference KEYWORDS_TYPE__TYPE = eINSTANCE.getKeywordsType_Type();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.LanguageStringTypeImpl <em>Language String Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.LanguageStringTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getLanguageStringType()
         * @generated
         */
        EClass LANGUAGE_STRING_TYPE = eINSTANCE.getLanguageStringType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LANGUAGE_STRING_TYPE__VALUE = eINSTANCE.getLanguageStringType_Value();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ManifestTypeImpl <em>Manifest Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ManifestTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getManifestType()
         * @generated
         */
        EClass MANIFEST_TYPE = eINSTANCE.getManifestType();

        /**
         * The meta object literal for the '<em><b>Reference Group</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MANIFEST_TYPE__REFERENCE_GROUP = eINSTANCE.getManifestType_ReferenceGroup();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.MetadataTypeImpl <em>Metadata Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.MetadataTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getMetadataType()
         * @generated
         */
        EClass METADATA_TYPE = eINSTANCE.getMetadataType();

        /**
         * The meta object literal for the '<em><b>Abstract Meta Data Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute METADATA_TYPE__ABSTRACT_META_DATA_GROUP = eINSTANCE.getMetadataType_AbstractMetaDataGroup();

        /**
         * The meta object literal for the '<em><b>Abstract Meta Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference METADATA_TYPE__ABSTRACT_META_DATA = eINSTANCE.getMetadataType_AbstractMetaData();

        /**
         * The meta object literal for the '<em><b>About</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute METADATA_TYPE__ABOUT = eINSTANCE.getMetadataType_About();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
         * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
         * @generated
         */
		EAttribute METADATA_TYPE__TITLE = eINSTANCE.getMetadataType_Title();

								/**
         * The meta object literal for the '{@link net.opengis.ows11.impl.NoValuesTypeImpl <em>No Values Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.NoValuesTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getNoValuesType()
         * @generated
         */
        EClass NO_VALUES_TYPE = eINSTANCE.getNoValuesType();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.OnlineResourceTypeImpl <em>Online Resource Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.OnlineResourceTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getOnlineResourceType()
         * @generated
         */
        EClass ONLINE_RESOURCE_TYPE = eINSTANCE.getOnlineResourceType();

        /**
         * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
         * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
         * @generated
         */
		EAttribute ONLINE_RESOURCE_TYPE__HREF = eINSTANCE.getOnlineResourceType_Href();

								/**
         * The meta object literal for the '{@link net.opengis.ows11.impl.OperationsMetadataTypeImpl <em>Operations Metadata Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.OperationsMetadataTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getOperationsMetadataType()
         * @generated
         */
        EClass OPERATIONS_METADATA_TYPE = eINSTANCE.getOperationsMetadataType();

        /**
         * The meta object literal for the '<em><b>Operation</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATIONS_METADATA_TYPE__OPERATION = eINSTANCE.getOperationsMetadataType_Operation();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATIONS_METADATA_TYPE__PARAMETER = eINSTANCE.getOperationsMetadataType_Parameter();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATIONS_METADATA_TYPE__CONSTRAINT = eINSTANCE.getOperationsMetadataType_Constraint();

        /**
         * The meta object literal for the '<em><b>Extended Capabilities</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES = eINSTANCE.getOperationsMetadataType_ExtendedCapabilities();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.OperationTypeImpl <em>Operation Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.OperationTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getOperationType()
         * @generated
         */
        EClass OPERATION_TYPE = eINSTANCE.getOperationType();

        /**
         * The meta object literal for the '<em><b>DCP</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_TYPE__DCP = eINSTANCE.getOperationType_DCP();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_TYPE__PARAMETER = eINSTANCE.getOperationType_Parameter();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_TYPE__CONSTRAINT = eINSTANCE.getOperationType_Constraint();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_TYPE__METADATA = eINSTANCE.getOperationType_Metadata();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OPERATION_TYPE__NAME = eINSTANCE.getOperationType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.RangeTypeImpl <em>Range Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.RangeTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getRangeType()
         * @generated
         */
        EClass RANGE_TYPE = eINSTANCE.getRangeType();

        /**
         * The meta object literal for the '<em><b>Minimum Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_TYPE__MINIMUM_VALUE = eINSTANCE.getRangeType_MinimumValue();

        /**
         * The meta object literal for the '<em><b>Maximum Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_TYPE__MAXIMUM_VALUE = eINSTANCE.getRangeType_MaximumValue();

        /**
         * The meta object literal for the '<em><b>Spacing</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_TYPE__SPACING = eINSTANCE.getRangeType_Spacing();

        /**
         * The meta object literal for the '<em><b>Range Closure</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RANGE_TYPE__RANGE_CLOSURE = eINSTANCE.getRangeType_RangeClosure();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ReferenceGroupTypeImpl <em>Reference Group Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ReferenceGroupTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getReferenceGroupType()
         * @generated
         */
        EClass REFERENCE_GROUP_TYPE = eINSTANCE.getReferenceGroupType();

        /**
         * The meta object literal for the '<em><b>Abstract Reference Base Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP = eINSTANCE.getReferenceGroupType_AbstractReferenceBaseGroup();

        /**
         * The meta object literal for the '<em><b>Abstract Reference Base</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE = eINSTANCE.getReferenceGroupType_AbstractReferenceBase();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ReferenceTypeImpl <em>Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ReferenceTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getReferenceType()
         * @generated
         */
        EClass REFERENCE_TYPE = eINSTANCE.getReferenceType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REFERENCE_TYPE__IDENTIFIER = eINSTANCE.getReferenceType_Identifier();

        /**
         * The meta object literal for the '<em><b>Abstract</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REFERENCE_TYPE__ABSTRACT = eINSTANCE.getReferenceType_Abstract();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_TYPE__FORMAT = eINSTANCE.getReferenceType_Format();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REFERENCE_TYPE__METADATA = eINSTANCE.getReferenceType_Metadata();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.RequestMethodTypeImpl <em>Request Method Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.RequestMethodTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getRequestMethodType()
         * @generated
         */
        EClass REQUEST_METHOD_TYPE = eINSTANCE.getRequestMethodType();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REQUEST_METHOD_TYPE__CONSTRAINT = eINSTANCE.getRequestMethodType_Constraint();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ResponsiblePartySubsetTypeImpl <em>Responsible Party Subset Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ResponsiblePartySubsetTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getResponsiblePartySubsetType()
         * @generated
         */
        EClass RESPONSIBLE_PARTY_SUBSET_TYPE = eINSTANCE.getResponsiblePartySubsetType();

        /**
         * The meta object literal for the '<em><b>Individual Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESPONSIBLE_PARTY_SUBSET_TYPE__INDIVIDUAL_NAME = eINSTANCE.getResponsiblePartySubsetType_IndividualName();

        /**
         * The meta object literal for the '<em><b>Position Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESPONSIBLE_PARTY_SUBSET_TYPE__POSITION_NAME = eINSTANCE.getResponsiblePartySubsetType_PositionName();

        /**
         * The meta object literal for the '<em><b>Contact Info</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESPONSIBLE_PARTY_SUBSET_TYPE__CONTACT_INFO = eINSTANCE.getResponsiblePartySubsetType_ContactInfo();

        /**
         * The meta object literal for the '<em><b>Role</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESPONSIBLE_PARTY_SUBSET_TYPE__ROLE = eINSTANCE.getResponsiblePartySubsetType_Role();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ResponsiblePartyTypeImpl <em>Responsible Party Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ResponsiblePartyTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getResponsiblePartyType()
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
         * The meta object literal for the '<em><b>Role</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESPONSIBLE_PARTY_TYPE__ROLE = eINSTANCE.getResponsiblePartyType_Role();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.SectionsTypeImpl <em>Sections Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.SectionsTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getSectionsType()
         * @generated
         */
        EClass SECTIONS_TYPE = eINSTANCE.getSectionsType();

        /**
         * The meta object literal for the '<em><b>Section</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SECTIONS_TYPE__SECTION = eINSTANCE.getSectionsType_Section();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ServiceIdentificationTypeImpl <em>Service Identification Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ServiceIdentificationTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getServiceIdentificationType()
         * @generated
         */
        EClass SERVICE_IDENTIFICATION_TYPE = eINSTANCE.getServiceIdentificationType();

        /**
         * The meta object literal for the '<em><b>Service Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE = eINSTANCE.getServiceIdentificationType_ServiceType();

        /**
         * The meta object literal for the '<em><b>Service Type Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION = eINSTANCE.getServiceIdentificationType_ServiceTypeVersion();

        /**
         * The meta object literal for the '<em><b>Profile</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__PROFILE = eINSTANCE.getServiceIdentificationType_Profile();

        /**
         * The meta object literal for the '<em><b>Fees</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__FEES = eINSTANCE.getServiceIdentificationType_Fees();

        /**
         * The meta object literal for the '<em><b>Access Constraints</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS = eINSTANCE.getServiceIdentificationType_AccessConstraints();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ServiceProviderTypeImpl <em>Service Provider Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ServiceProviderTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getServiceProviderType()
         * @generated
         */
        EClass SERVICE_PROVIDER_TYPE = eINSTANCE.getServiceProviderType();

        /**
         * The meta object literal for the '<em><b>Provider Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_PROVIDER_TYPE__PROVIDER_NAME = eINSTANCE.getServiceProviderType_ProviderName();

        /**
         * The meta object literal for the '<em><b>Provider Site</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_PROVIDER_TYPE__PROVIDER_SITE = eINSTANCE.getServiceProviderType_ProviderSite();

        /**
         * The meta object literal for the '<em><b>Service Contact</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_PROVIDER_TYPE__SERVICE_CONTACT = eINSTANCE.getServiceProviderType_ServiceContact();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ServiceReferenceTypeImpl <em>Service Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ServiceReferenceTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getServiceReferenceType()
         * @generated
         */
        EClass SERVICE_REFERENCE_TYPE = eINSTANCE.getServiceReferenceType();

        /**
         * The meta object literal for the '<em><b>Request Message</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE = eINSTANCE.getServiceReferenceType_RequestMessage();

        /**
         * The meta object literal for the '<em><b>Request Message Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE = eINSTANCE.getServiceReferenceType_RequestMessageReference();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.TelephoneTypeImpl <em>Telephone Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.TelephoneTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getTelephoneType()
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
         * The meta object literal for the '{@link net.opengis.ows11.impl.UnNamedDomainTypeImpl <em>Un Named Domain Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.UnNamedDomainTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getUnNamedDomainType()
         * @generated
         */
        EClass UN_NAMED_DOMAIN_TYPE = eINSTANCE.getUnNamedDomainType();

        /**
         * The meta object literal for the '<em><b>Allowed Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES = eINSTANCE.getUnNamedDomainType_AllowedValues();

        /**
         * The meta object literal for the '<em><b>Any Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__ANY_VALUE = eINSTANCE.getUnNamedDomainType_AnyValue();

        /**
         * The meta object literal for the '<em><b>No Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__NO_VALUES = eINSTANCE.getUnNamedDomainType_NoValues();

        /**
         * The meta object literal for the '<em><b>Values Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE = eINSTANCE.getUnNamedDomainType_ValuesReference();

        /**
         * The meta object literal for the '<em><b>Default Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE = eINSTANCE.getUnNamedDomainType_DefaultValue();

        /**
         * The meta object literal for the '<em><b>Meaning</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__MEANING = eINSTANCE.getUnNamedDomainType_Meaning();

        /**
         * The meta object literal for the '<em><b>Data Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__DATA_TYPE = eINSTANCE.getUnNamedDomainType_DataType();

        /**
         * The meta object literal for the '<em><b>UOM</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__UOM = eINSTANCE.getUnNamedDomainType_UOM();

        /**
         * The meta object literal for the '<em><b>Reference System</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM = eINSTANCE.getUnNamedDomainType_ReferenceSystem();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__METADATA = eINSTANCE.getUnNamedDomainType_Metadata();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ValuesReferenceTypeImpl <em>Values Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ValuesReferenceTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getValuesReferenceType()
         * @generated
         */
        EClass VALUES_REFERENCE_TYPE = eINSTANCE.getValuesReferenceType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUES_REFERENCE_TYPE__VALUE = eINSTANCE.getValuesReferenceType_Value();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUES_REFERENCE_TYPE__REFERENCE = eINSTANCE.getValuesReferenceType_Reference();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.ValueTypeImpl <em>Value Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.ValueTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getValueType()
         * @generated
         */
        EClass VALUE_TYPE = eINSTANCE.getValueType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUE_TYPE__VALUE = eINSTANCE.getValueType_Value();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.impl.WGS84BoundingBoxTypeImpl <em>WGS84 Bounding Box Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.impl.WGS84BoundingBoxTypeImpl
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getWGS84BoundingBoxType()
         * @generated
         */
        EClass WGS84_BOUNDING_BOX_TYPE = eINSTANCE.getWGS84BoundingBoxType();

        /**
         * The meta object literal for the '{@link net.opengis.ows11.RangeClosureType <em>Range Closure Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.RangeClosureType
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getRangeClosureType()
         * @generated
         */
        EEnum RANGE_CLOSURE_TYPE = eINSTANCE.getRangeClosureType();

        /**
         * The meta object literal for the '<em>Mime Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getMimeType()
         * @generated
         */
        EDataType MIME_TYPE = eINSTANCE.getMimeType();

        /**
         * The meta object literal for the '<em>Position Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.List
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getPositionType()
         * @generated
         */
        EDataType POSITION_TYPE = eINSTANCE.getPositionType();

        /**
         * The meta object literal for the '<em>Position Type2 D</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.List
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getPositionType2D()
         * @generated
         */
        EDataType POSITION_TYPE2_D = eINSTANCE.getPositionType2D();

        /**
         * The meta object literal for the '<em>Range Closure Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.ows11.RangeClosureType
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getRangeClosureTypeObject()
         * @generated
         */
        EDataType RANGE_CLOSURE_TYPE_OBJECT = eINSTANCE.getRangeClosureTypeObject();

        /**
         * The meta object literal for the '<em>Service Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getServiceType()
         * @generated
         */
        EDataType SERVICE_TYPE = eINSTANCE.getServiceType();

        /**
         * The meta object literal for the '<em>Update Sequence Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getUpdateSequenceType()
         * @generated
         */
        EDataType UPDATE_SEQUENCE_TYPE = eINSTANCE.getUpdateSequenceType();

        /**
         * The meta object literal for the '<em>Version Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getVersionType()
         * @generated
         */
        EDataType VERSION_TYPE = eINSTANCE.getVersionType();

        /**
         * The meta object literal for the '<em>Version Type1</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getVersionType1()
         * @generated
         */
        EDataType VERSION_TYPE1 = eINSTANCE.getVersionType1();

								/**
         * The meta object literal for the '<em>Map</em>' data type.
         * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
         * @see java.util.Map
         * @see net.opengis.ows11.impl.Ows11PackageImpl#getMap()
         * @generated
         */
		EDataType MAP = eINSTANCE.getMap();

    }

} //Ows11Package
