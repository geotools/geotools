/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see net.opengis.ows10.Ows10Factory
 * @model kind="package"
 * @generated
 */
public interface Ows10Package extends EPackage {
	/**
     * The package name.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	String eNAME = "ows10";

	/**
     * The package namespace URI.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	String eNS_URI = "http://www.opengis.net/ows";

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
	Ows10Package eINSTANCE = net.opengis.ows10.impl.Ows10PackageImpl.init();

	/**
     * The meta object id for the '{@link net.opengis.ows10.impl.AcceptFormatsTypeImpl <em>Accept Formats Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.AcceptFormatsTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getAcceptFormatsType()
     * @generated
     */
	int ACCEPT_FORMATS_TYPE = 0;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.AcceptVersionsTypeImpl <em>Accept Versions Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.AcceptVersionsTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getAcceptVersionsType()
     * @generated
     */
	int ACCEPT_VERSIONS_TYPE = 1;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.AddressTypeImpl <em>Address Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.AddressTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getAddressType()
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
     * The meta object id for the '{@link net.opengis.ows10.impl.BoundingBoxTypeImpl <em>Bounding Box Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.BoundingBoxTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getBoundingBoxType()
     * @generated
     */
	int BOUNDING_BOX_TYPE = 3;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.CapabilitiesBaseTypeImpl <em>Capabilities Base Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.CapabilitiesBaseTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getCapabilitiesBaseType()
     * @generated
     */
	int CAPABILITIES_BASE_TYPE = 4;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.CodeTypeImpl <em>Code Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.CodeTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getCodeType()
     * @generated
     */
	int CODE_TYPE = 5;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.ContactTypeImpl <em>Contact Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.ContactTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getContactType()
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
     * The meta object id for the '{@link net.opengis.ows10.impl.DCPTypeImpl <em>DCP Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.DCPTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getDCPType()
     * @generated
     */
	int DCP_TYPE = 7;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.DescriptionTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getDescriptionType()
     * @generated
     */
	int DESCRIPTION_TYPE = 8;

	/**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DESCRIPTION_TYPE__TITLE = 0;

	/**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
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
     * The meta object id for the '{@link net.opengis.ows10.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.DocumentRootImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getDocumentRoot()
     * @generated
     */
	int DOCUMENT_ROOT = 9;

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
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__ABSTRACT = 3;

	/**
     * The feature id for the '<em><b>Contact Info</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__CONTACT_INFO = 4;

	/**
     * The feature id for the '<em><b>Individual Name</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__INDIVIDUAL_NAME = 5;

	/**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__KEYWORDS = 6;

	/**
     * The feature id for the '<em><b>Organisation Name</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__ORGANISATION_NAME = 7;

	/**
     * The feature id for the '<em><b>Point Of Contact</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__POINT_OF_CONTACT = 8;

	/**
     * The feature id for the '<em><b>Position Name</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__POSITION_NAME = 9;

	/**
     * The feature id for the '<em><b>Role</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__ROLE = 10;

	/**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__TITLE = 11;

	/**
     * The feature id for the '<em><b>Abstract Meta Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__ABSTRACT_META_DATA = 12;

	/**
     * The feature id for the '<em><b>Access Constraints</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__ACCESS_CONSTRAINTS = 13;

	/**
     * The feature id for the '<em><b>Available CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__AVAILABLE_CRS = 14;

	/**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__BOUNDING_BOX = 15;

	/**
     * The feature id for the '<em><b>Dcp</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__DCP = 16;

	/**
     * The feature id for the '<em><b>Exception</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__EXCEPTION = 17;

	/**
     * The feature id for the '<em><b>Exception Report</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__EXCEPTION_REPORT = 18;

	/**
     * The feature id for the '<em><b>Extended Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__EXTENDED_CAPABILITIES = 19;

	/**
     * The feature id for the '<em><b>Fees</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__FEES = 20;

	/**
     * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__GET_CAPABILITIES = 21;

	/**
     * The feature id for the '<em><b>Http</b></em>' containment reference.
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
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__LANGUAGE = 24;

	/**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__METADATA = 25;

	/**
     * The feature id for the '<em><b>Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__OPERATION = 26;

	/**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__OPERATIONS_METADATA = 27;

	/**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__OUTPUT_FORMAT = 28;

	/**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__SERVICE_IDENTIFICATION = 29;

	/**
     * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__SERVICE_PROVIDER = 30;

	/**
     * The feature id for the '<em><b>Supported CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__SUPPORTED_CRS = 31;

	/**
     * The feature id for the '<em><b>Wg S84 Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT__WG_S84_BOUNDING_BOX = 32;

	/**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOCUMENT_ROOT_FEATURE_COUNT = 33;

	/**
     * The meta object id for the '{@link net.opengis.ows10.impl.DomainTypeImpl <em>Domain Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.DomainTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getDomainType()
     * @generated
     */
	int DOMAIN_TYPE = 10;

	/**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOMAIN_TYPE__VALUE = 0;

	/**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOMAIN_TYPE__METADATA = 1;

	/**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOMAIN_TYPE__NAME = 2;

	/**
     * The number of structural features of the '<em>Domain Type</em>' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DOMAIN_TYPE_FEATURE_COUNT = 3;

	/**
     * The meta object id for the '{@link net.opengis.ows10.impl.ExceptionReportTypeImpl <em>Exception Report Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.ExceptionReportTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getExceptionReportType()
     * @generated
     */
	int EXCEPTION_REPORT_TYPE = 11;

	/**
     * The feature id for the '<em><b>Exception</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int EXCEPTION_REPORT_TYPE__EXCEPTION = 0;

	/**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int EXCEPTION_REPORT_TYPE__LANGUAGE = 1;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.ExceptionTypeImpl <em>Exception Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.ExceptionTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getExceptionType()
     * @generated
     */
	int EXCEPTION_TYPE = 12;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.GetCapabilitiesTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getGetCapabilitiesType()
     * @generated
     */
	int GET_CAPABILITIES_TYPE = 13;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.HTTPTypeImpl <em>HTTP Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.HTTPTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getHTTPType()
     * @generated
     */
	int HTTP_TYPE = 14;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.IdentificationTypeImpl <em>Identification Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.IdentificationTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getIdentificationType()
     * @generated
     */
	int IDENTIFICATION_TYPE = 15;

	/**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

	/**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

	/**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__IDENTIFIER = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
     * The feature id for the '<em><b>Bounding Box Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__BOUNDING_BOX_GROUP = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__BOUNDING_BOX = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__OUTPUT_FORMAT = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
     * The feature id for the '<em><b>Available CRS Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP = DESCRIPTION_TYPE_FEATURE_COUNT + 4;

	/**
     * The feature id for the '<em><b>Available CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__AVAILABLE_CRS = DESCRIPTION_TYPE_FEATURE_COUNT + 5;

	/**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE__METADATA = DESCRIPTION_TYPE_FEATURE_COUNT + 6;

	/**
     * The number of structural features of the '<em>Identification Type</em>' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int IDENTIFICATION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 7;

	/**
     * The meta object id for the '{@link net.opengis.ows10.impl.KeywordsTypeImpl <em>Keywords Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.KeywordsTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getKeywordsType()
     * @generated
     */
	int KEYWORDS_TYPE = 16;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.MetadataTypeImpl <em>Metadata Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.MetadataTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getMetadataType()
     * @generated
     */
	int METADATA_TYPE = 17;

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
     * The number of structural features of the '<em>Metadata Type</em>' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int METADATA_TYPE_FEATURE_COUNT = 3;

	/**
     * The meta object id for the '{@link net.opengis.ows10.impl.OnlineResourceTypeImpl <em>Online Resource Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.OnlineResourceTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getOnlineResourceType()
     * @generated
     */
	int ONLINE_RESOURCE_TYPE = 18;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.OperationTypeImpl <em>Operation Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.OperationTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getOperationType()
     * @generated
     */
	int OPERATION_TYPE = 19;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.OperationsMetadataTypeImpl <em>Operations Metadata Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.OperationsMetadataTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getOperationsMetadataType()
     * @generated
     */
	int OPERATIONS_METADATA_TYPE = 20;

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
     * The feature id for the '<em><b>Extended Capabilities</b></em>' reference.
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
     * The meta object id for the '{@link net.opengis.ows10.impl.RequestMethodTypeImpl <em>Request Method Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.RequestMethodTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getRequestMethodType()
     * @generated
     */
	int REQUEST_METHOD_TYPE = 21;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.ResponsiblePartySubsetTypeImpl <em>Responsible Party Subset Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.ResponsiblePartySubsetTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getResponsiblePartySubsetType()
     * @generated
     */
	int RESPONSIBLE_PARTY_SUBSET_TYPE = 22;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.ResponsiblePartyTypeImpl <em>Responsible Party Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.ResponsiblePartyTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getResponsiblePartyType()
     * @generated
     */
	int RESPONSIBLE_PARTY_TYPE = 23;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.SectionsTypeImpl <em>Sections Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.SectionsTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getSectionsType()
     * @generated
     */
	int SECTIONS_TYPE = 24;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.ServiceIdentificationTypeImpl <em>Service Identification Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.ServiceIdentificationTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getServiceIdentificationType()
     * @generated
     */
	int SERVICE_IDENTIFICATION_TYPE = 25;

	/**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int SERVICE_IDENTIFICATION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
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
     * The feature id for the '<em><b>Fees</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int SERVICE_IDENTIFICATION_TYPE__FEES = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
     * The feature id for the '<em><b>Access Constraints</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
     * The number of structural features of the '<em>Service Identification Type</em>' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int SERVICE_IDENTIFICATION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 4;

	/**
     * The meta object id for the '{@link net.opengis.ows10.impl.ServiceProviderTypeImpl <em>Service Provider Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.ServiceProviderTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getServiceProviderType()
     * @generated
     */
	int SERVICE_PROVIDER_TYPE = 26;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.TelephoneTypeImpl <em>Telephone Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.TelephoneTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getTelephoneType()
     * @generated
     */
	int TELEPHONE_TYPE = 27;

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
     * The meta object id for the '{@link net.opengis.ows10.impl.WGS84BoundingBoxTypeImpl <em>WGS84 Bounding Box Type</em>}' class.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see net.opengis.ows10.impl.WGS84BoundingBoxTypeImpl
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getWGS84BoundingBoxType()
     * @generated
     */
	int WGS84_BOUNDING_BOX_TYPE = 28;

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
     * The meta object id for the '{@link java.lang.Object <em>Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getObject()
     * @generated
     */
    int OBJECT = 29;

    /**
     * The number of structural features of the '<em>Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '<em>Mime Type</em>' data type.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getMimeType()
     * @generated
     */
	int MIME_TYPE = 30;

	/**
     * The meta object id for the '<em>Version Type</em>' data type.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getVersionType()
     * @generated
     */
	int VERSION_TYPE = 31;

	/**
     * The meta object id for the '<em>Position Type</em>' data type.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see java.util.List
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getPositionType()
     * @generated
     */
	int POSITION_TYPE = 32;

	/**
     * The meta object id for the '<em>Update Sequence Type</em>' data type.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getUpdateSequenceType()
     * @generated
     */
	int UPDATE_SEQUENCE_TYPE = 33;


	/**
     * The meta object id for the '<em>Map</em>' data type.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see java.util.Map
     * @see net.opengis.ows10.impl.Ows10PackageImpl#getMap()
     * @generated
     */
	int MAP = 34;


	/**
     * Returns the meta object for class '{@link net.opengis.ows10.AcceptFormatsType <em>Accept Formats Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Accept Formats Type</em>'.
     * @see net.opengis.ows10.AcceptFormatsType
     * @generated
     */
	EClass getAcceptFormatsType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.AcceptFormatsType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.ows10.AcceptFormatsType#getOutputFormat()
     * @see #getAcceptFormatsType()
     * @generated
     */
	EAttribute getAcceptFormatsType_OutputFormat();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.AcceptVersionsType <em>Accept Versions Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Accept Versions Type</em>'.
     * @see net.opengis.ows10.AcceptVersionsType
     * @generated
     */
	EClass getAcceptVersionsType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.AcceptVersionsType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.ows10.AcceptVersionsType#getVersion()
     * @see #getAcceptVersionsType()
     * @generated
     */
	EAttribute getAcceptVersionsType_Version();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.AddressType <em>Address Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Address Type</em>'.
     * @see net.opengis.ows10.AddressType
     * @generated
     */
	EClass getAddressType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.AddressType#getDeliveryPoint <em>Delivery Point</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Delivery Point</em>'.
     * @see net.opengis.ows10.AddressType#getDeliveryPoint()
     * @see #getAddressType()
     * @generated
     */
	EAttribute getAddressType_DeliveryPoint();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.AddressType#getCity <em>City</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>City</em>'.
     * @see net.opengis.ows10.AddressType#getCity()
     * @see #getAddressType()
     * @generated
     */
	EAttribute getAddressType_City();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.AddressType#getAdministrativeArea <em>Administrative Area</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Administrative Area</em>'.
     * @see net.opengis.ows10.AddressType#getAdministrativeArea()
     * @see #getAddressType()
     * @generated
     */
	EAttribute getAddressType_AdministrativeArea();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.AddressType#getPostalCode <em>Postal Code</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Postal Code</em>'.
     * @see net.opengis.ows10.AddressType#getPostalCode()
     * @see #getAddressType()
     * @generated
     */
	EAttribute getAddressType_PostalCode();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.AddressType#getCountry <em>Country</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Country</em>'.
     * @see net.opengis.ows10.AddressType#getCountry()
     * @see #getAddressType()
     * @generated
     */
	EAttribute getAddressType_Country();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.AddressType#getElectronicMailAddress <em>Electronic Mail Address</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Electronic Mail Address</em>'.
     * @see net.opengis.ows10.AddressType#getElectronicMailAddress()
     * @see #getAddressType()
     * @generated
     */
	EAttribute getAddressType_ElectronicMailAddress();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.BoundingBoxType <em>Bounding Box Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Bounding Box Type</em>'.
     * @see net.opengis.ows10.BoundingBoxType
     * @generated
     */
	EClass getBoundingBoxType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.BoundingBoxType#getLowerCorner <em>Lower Corner</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lower Corner</em>'.
     * @see net.opengis.ows10.BoundingBoxType#getLowerCorner()
     * @see #getBoundingBoxType()
     * @generated
     */
	EAttribute getBoundingBoxType_LowerCorner();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.BoundingBoxType#getUpperCorner <em>Upper Corner</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Upper Corner</em>'.
     * @see net.opengis.ows10.BoundingBoxType#getUpperCorner()
     * @see #getBoundingBoxType()
     * @generated
     */
	EAttribute getBoundingBoxType_UpperCorner();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.BoundingBoxType#getCrs <em>Crs</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Crs</em>'.
     * @see net.opengis.ows10.BoundingBoxType#getCrs()
     * @see #getBoundingBoxType()
     * @generated
     */
	EAttribute getBoundingBoxType_Crs();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.BoundingBoxType#getDimensions <em>Dimensions</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Dimensions</em>'.
     * @see net.opengis.ows10.BoundingBoxType#getDimensions()
     * @see #getBoundingBoxType()
     * @generated
     */
	EAttribute getBoundingBoxType_Dimensions();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Capabilities Base Type</em>'.
     * @see net.opengis.ows10.CapabilitiesBaseType
     * @generated
     */
	EClass getCapabilitiesBaseType();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.CapabilitiesBaseType#getServiceIdentification <em>Service Identification</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Identification</em>'.
     * @see net.opengis.ows10.CapabilitiesBaseType#getServiceIdentification()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
	EReference getCapabilitiesBaseType_ServiceIdentification();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.CapabilitiesBaseType#getServiceProvider <em>Service Provider</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Provider</em>'.
     * @see net.opengis.ows10.CapabilitiesBaseType#getServiceProvider()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
	EReference getCapabilitiesBaseType_ServiceProvider();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.CapabilitiesBaseType#getOperationsMetadata <em>Operations Metadata</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operations Metadata</em>'.
     * @see net.opengis.ows10.CapabilitiesBaseType#getOperationsMetadata()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
	EReference getCapabilitiesBaseType_OperationsMetadata();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.CapabilitiesBaseType#getUpdateSequence <em>Update Sequence</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Update Sequence</em>'.
     * @see net.opengis.ows10.CapabilitiesBaseType#getUpdateSequence()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
	EAttribute getCapabilitiesBaseType_UpdateSequence();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.CapabilitiesBaseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.ows10.CapabilitiesBaseType#getVersion()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
	EAttribute getCapabilitiesBaseType_Version();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.CodeType <em>Code Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Code Type</em>'.
     * @see net.opengis.ows10.CodeType
     * @generated
     */
	EClass getCodeType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.CodeType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.ows10.CodeType#getValue()
     * @see #getCodeType()
     * @generated
     */
	EAttribute getCodeType_Value();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.CodeType#getCodeSpace <em>Code Space</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Code Space</em>'.
     * @see net.opengis.ows10.CodeType#getCodeSpace()
     * @see #getCodeType()
     * @generated
     */
	EAttribute getCodeType_CodeSpace();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.ContactType <em>Contact Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Contact Type</em>'.
     * @see net.opengis.ows10.ContactType
     * @generated
     */
	EClass getContactType();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ContactType#getPhone <em>Phone</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Phone</em>'.
     * @see net.opengis.ows10.ContactType#getPhone()
     * @see #getContactType()
     * @generated
     */
	EReference getContactType_Phone();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ContactType#getAddress <em>Address</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Address</em>'.
     * @see net.opengis.ows10.ContactType#getAddress()
     * @see #getContactType()
     * @generated
     */
	EReference getContactType_Address();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ContactType#getOnlineResource <em>Online Resource</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Online Resource</em>'.
     * @see net.opengis.ows10.ContactType#getOnlineResource()
     * @see #getContactType()
     * @generated
     */
	EReference getContactType_OnlineResource();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ContactType#getHoursOfService <em>Hours Of Service</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hours Of Service</em>'.
     * @see net.opengis.ows10.ContactType#getHoursOfService()
     * @see #getContactType()
     * @generated
     */
	EAttribute getContactType_HoursOfService();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ContactType#getContactInstructions <em>Contact Instructions</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Contact Instructions</em>'.
     * @see net.opengis.ows10.ContactType#getContactInstructions()
     * @see #getContactType()
     * @generated
     */
	EAttribute getContactType_ContactInstructions();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.DCPType <em>DCP Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>DCP Type</em>'.
     * @see net.opengis.ows10.DCPType
     * @generated
     */
	EClass getDCPType();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DCPType#getHTTP <em>HTTP</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>HTTP</em>'.
     * @see net.opengis.ows10.DCPType#getHTTP()
     * @see #getDCPType()
     * @generated
     */
	EReference getDCPType_HTTP();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Description Type</em>'.
     * @see net.opengis.ows10.DescriptionType
     * @generated
     */
	EClass getDescriptionType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DescriptionType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Title</em>'.
     * @see net.opengis.ows10.DescriptionType#getTitle()
     * @see #getDescriptionType()
     * @generated
     */
	EAttribute getDescriptionType_Title();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DescriptionType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Abstract</em>'.
     * @see net.opengis.ows10.DescriptionType#getAbstract()
     * @see #getDescriptionType()
     * @generated
     */
	EAttribute getDescriptionType_Abstract();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.DescriptionType#getKeywords <em>Keywords</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Keywords</em>'.
     * @see net.opengis.ows10.DescriptionType#getKeywords()
     * @see #getDescriptionType()
     * @generated
     */
	EReference getDescriptionType_Keywords();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see net.opengis.ows10.DocumentRoot
     * @generated
     */
	EClass getDocumentRoot();

	/**
     * Returns the meta object for the attribute list '{@link net.opengis.ows10.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.ows10.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_Mixed();

	/**
     * Returns the meta object for the map '{@link net.opengis.ows10.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see net.opengis.ows10.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
     * Returns the meta object for the map '{@link net.opengis.ows10.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see net.opengis.ows10.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Abstract</em>'.
     * @see net.opengis.ows10.DocumentRoot#getAbstract()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_Abstract();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getContactInfo <em>Contact Info</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contact Info</em>'.
     * @see net.opengis.ows10.DocumentRoot#getContactInfo()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_ContactInfo();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getIndividualName <em>Individual Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Individual Name</em>'.
     * @see net.opengis.ows10.DocumentRoot#getIndividualName()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_IndividualName();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getKeywords <em>Keywords</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Keywords</em>'.
     * @see net.opengis.ows10.DocumentRoot#getKeywords()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_Keywords();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getOrganisationName <em>Organisation Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Organisation Name</em>'.
     * @see net.opengis.ows10.DocumentRoot#getOrganisationName()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_OrganisationName();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getPointOfContact <em>Point Of Contact</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Point Of Contact</em>'.
     * @see net.opengis.ows10.DocumentRoot#getPointOfContact()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_PointOfContact();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getPositionName <em>Position Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Position Name</em>'.
     * @see net.opengis.ows10.DocumentRoot#getPositionName()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_PositionName();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Role</em>'.
     * @see net.opengis.ows10.DocumentRoot#getRole()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_Role();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Title</em>'.
     * @see net.opengis.ows10.DocumentRoot#getTitle()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_Title();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getAbstractMetaData <em>Abstract Meta Data</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Meta Data</em>'.
     * @see net.opengis.ows10.DocumentRoot#getAbstractMetaData()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_AbstractMetaData();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getAccessConstraints <em>Access Constraints</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Access Constraints</em>'.
     * @see net.opengis.ows10.DocumentRoot#getAccessConstraints()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_AccessConstraints();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getAvailableCRS <em>Available CRS</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Available CRS</em>'.
     * @see net.opengis.ows10.DocumentRoot#getAvailableCRS()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_AvailableCRS();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bounding Box</em>'.
     * @see net.opengis.ows10.DocumentRoot#getBoundingBox()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_BoundingBox();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getDcp <em>Dcp</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Dcp</em>'.
     * @see net.opengis.ows10.DocumentRoot#getDcp()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_Dcp();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getException <em>Exception</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Exception</em>'.
     * @see net.opengis.ows10.DocumentRoot#getException()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_Exception();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getExceptionReport <em>Exception Report</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Exception Report</em>'.
     * @see net.opengis.ows10.DocumentRoot#getExceptionReport()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_ExceptionReport();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getExtendedCapabilities <em>Extended Capabilities</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extended Capabilities</em>'.
     * @see net.opengis.ows10.DocumentRoot#getExtendedCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_ExtendedCapabilities();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getFees <em>Fees</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fees</em>'.
     * @see net.opengis.ows10.DocumentRoot#getFees()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_Fees();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
     * @see net.opengis.ows10.DocumentRoot#getGetCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_GetCapabilities();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getHttp <em>Http</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Http</em>'.
     * @see net.opengis.ows10.DocumentRoot#getHttp()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_Http();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.ows10.DocumentRoot#getIdentifier()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_Identifier();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see net.opengis.ows10.DocumentRoot#getLanguage()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_Language();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Metadata</em>'.
     * @see net.opengis.ows10.DocumentRoot#getMetadata()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_Metadata();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getOperation <em>Operation</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operation</em>'.
     * @see net.opengis.ows10.DocumentRoot#getOperation()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_Operation();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getOperationsMetadata <em>Operations Metadata</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operations Metadata</em>'.
     * @see net.opengis.ows10.DocumentRoot#getOperationsMetadata()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_OperationsMetadata();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.ows10.DocumentRoot#getOutputFormat()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_OutputFormat();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getServiceIdentification <em>Service Identification</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Identification</em>'.
     * @see net.opengis.ows10.DocumentRoot#getServiceIdentification()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_ServiceIdentification();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getServiceProvider <em>Service Provider</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Provider</em>'.
     * @see net.opengis.ows10.DocumentRoot#getServiceProvider()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_ServiceProvider();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Supported CRS</em>'.
     * @see net.opengis.ows10.DocumentRoot#getSupportedCRS()
     * @see #getDocumentRoot()
     * @generated
     */
	EAttribute getDocumentRoot_SupportedCRS();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.DocumentRoot#getWgS84BoundingBox <em>Wg S84 Bounding Box</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Wg S84 Bounding Box</em>'.
     * @see net.opengis.ows10.DocumentRoot#getWgS84BoundingBox()
     * @see #getDocumentRoot()
     * @generated
     */
	EReference getDocumentRoot_WgS84BoundingBox();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.DomainType <em>Domain Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Domain Type</em>'.
     * @see net.opengis.ows10.DomainType
     * @generated
     */
	EClass getDomainType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DomainType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.ows10.DomainType#getValue()
     * @see #getDomainType()
     * @generated
     */
	EAttribute getDomainType_Value();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.DomainType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.ows10.DomainType#getMetadata()
     * @see #getDomainType()
     * @generated
     */
	EReference getDomainType_Metadata();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.DomainType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.ows10.DomainType#getName()
     * @see #getDomainType()
     * @generated
     */
	EAttribute getDomainType_Name();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.ExceptionReportType <em>Exception Report Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Exception Report Type</em>'.
     * @see net.opengis.ows10.ExceptionReportType
     * @generated
     */
	EClass getExceptionReportType();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.ExceptionReportType#getException <em>Exception</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Exception</em>'.
     * @see net.opengis.ows10.ExceptionReportType#getException()
     * @see #getExceptionReportType()
     * @generated
     */
	EReference getExceptionReportType_Exception();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ExceptionReportType#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see net.opengis.ows10.ExceptionReportType#getLanguage()
     * @see #getExceptionReportType()
     * @generated
     */
	EAttribute getExceptionReportType_Language();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ExceptionReportType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.ows10.ExceptionReportType#getVersion()
     * @see #getExceptionReportType()
     * @generated
     */
	EAttribute getExceptionReportType_Version();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.ExceptionType <em>Exception Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Exception Type</em>'.
     * @see net.opengis.ows10.ExceptionType
     * @generated
     */
	EClass getExceptionType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ExceptionType#getExceptionText <em>Exception Text</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Exception Text</em>'.
     * @see net.opengis.ows10.ExceptionType#getExceptionText()
     * @see #getExceptionType()
     * @generated
     */
	EAttribute getExceptionType_ExceptionText();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ExceptionType#getExceptionCode <em>Exception Code</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Exception Code</em>'.
     * @see net.opengis.ows10.ExceptionType#getExceptionCode()
     * @see #getExceptionType()
     * @generated
     */
	EAttribute getExceptionType_ExceptionCode();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ExceptionType#getLocator <em>Locator</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Locator</em>'.
     * @see net.opengis.ows10.ExceptionType#getLocator()
     * @see #getExceptionType()
     * @generated
     */
	EAttribute getExceptionType_Locator();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Capabilities Type</em>'.
     * @see net.opengis.ows10.GetCapabilitiesType
     * @generated
     */
	EClass getGetCapabilitiesType();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Accept Versions</em>'.
     * @see net.opengis.ows10.GetCapabilitiesType#getAcceptVersions()
     * @see #getGetCapabilitiesType()
     * @generated
     */
	EReference getGetCapabilitiesType_AcceptVersions();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.GetCapabilitiesType#getSections <em>Sections</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Sections</em>'.
     * @see net.opengis.ows10.GetCapabilitiesType#getSections()
     * @see #getGetCapabilitiesType()
     * @generated
     */
	EReference getGetCapabilitiesType_Sections();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.GetCapabilitiesType#getAcceptFormats <em>Accept Formats</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Accept Formats</em>'.
     * @see net.opengis.ows10.GetCapabilitiesType#getAcceptFormats()
     * @see #getGetCapabilitiesType()
     * @generated
     */
	EReference getGetCapabilitiesType_AcceptFormats();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Update Sequence</em>'.
     * @see net.opengis.ows10.GetCapabilitiesType#getUpdateSequence()
     * @see #getGetCapabilitiesType()
     * @generated
     */
	EAttribute getGetCapabilitiesType_UpdateSequence();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Base Url</em>'.
     * @see net.opengis.ows10.GetCapabilitiesType#getBaseUrl()
     * @see #getGetCapabilitiesType()
     * @generated
     */
	EAttribute getGetCapabilitiesType_BaseUrl();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.GetCapabilitiesType#getNamespace <em>Namespace</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Namespace</em>'.
     * @see net.opengis.ows10.GetCapabilitiesType#getNamespace()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_Namespace();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extended Properties</em>'.
     * @see net.opengis.ows10.GetCapabilitiesType#getExtendedProperties()
     * @see #getGetCapabilitiesType()
     * @generated
     */
	EAttribute getGetCapabilitiesType_ExtendedProperties();

				/**
     * Returns the meta object for class '{@link net.opengis.ows10.HTTPType <em>HTTP Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>HTTP Type</em>'.
     * @see net.opengis.ows10.HTTPType
     * @generated
     */
	EClass getHTTPType();

	/**
     * Returns the meta object for the attribute list '{@link net.opengis.ows10.HTTPType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see net.opengis.ows10.HTTPType#getGroup()
     * @see #getHTTPType()
     * @generated
     */
	EAttribute getHTTPType_Group();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.HTTPType#getGet <em>Get</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Get</em>'.
     * @see net.opengis.ows10.HTTPType#getGet()
     * @see #getHTTPType()
     * @generated
     */
	EReference getHTTPType_Get();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.HTTPType#getPost <em>Post</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Post</em>'.
     * @see net.opengis.ows10.HTTPType#getPost()
     * @see #getHTTPType()
     * @generated
     */
	EReference getHTTPType_Post();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.IdentificationType <em>Identification Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Identification Type</em>'.
     * @see net.opengis.ows10.IdentificationType
     * @generated
     */
	EClass getIdentificationType();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.IdentificationType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.ows10.IdentificationType#getIdentifier()
     * @see #getIdentificationType()
     * @generated
     */
	EReference getIdentificationType_Identifier();

	/**
     * Returns the meta object for the attribute list '{@link net.opengis.ows10.IdentificationType#getBoundingBoxGroup <em>Bounding Box Group</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Bounding Box Group</em>'.
     * @see net.opengis.ows10.IdentificationType#getBoundingBoxGroup()
     * @see #getIdentificationType()
     * @generated
     */
	EAttribute getIdentificationType_BoundingBoxGroup();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.IdentificationType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bounding Box</em>'.
     * @see net.opengis.ows10.IdentificationType#getBoundingBox()
     * @see #getIdentificationType()
     * @generated
     */
	EReference getIdentificationType_BoundingBox();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.IdentificationType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.ows10.IdentificationType#getOutputFormat()
     * @see #getIdentificationType()
     * @generated
     */
	EAttribute getIdentificationType_OutputFormat();

	/**
     * Returns the meta object for the attribute list '{@link net.opengis.ows10.IdentificationType#getAvailableCRSGroup <em>Available CRS Group</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Available CRS Group</em>'.
     * @see net.opengis.ows10.IdentificationType#getAvailableCRSGroup()
     * @see #getIdentificationType()
     * @generated
     */
	EAttribute getIdentificationType_AvailableCRSGroup();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.IdentificationType#getAvailableCRS <em>Available CRS</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Available CRS</em>'.
     * @see net.opengis.ows10.IdentificationType#getAvailableCRS()
     * @see #getIdentificationType()
     * @generated
     */
	EAttribute getIdentificationType_AvailableCRS();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.IdentificationType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.ows10.IdentificationType#getMetadata()
     * @see #getIdentificationType()
     * @generated
     */
	EReference getIdentificationType_Metadata();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.KeywordsType <em>Keywords Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Keywords Type</em>'.
     * @see net.opengis.ows10.KeywordsType
     * @generated
     */
	EClass getKeywordsType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.KeywordsType#getKeyword <em>Keyword</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Keyword</em>'.
     * @see net.opengis.ows10.KeywordsType#getKeyword()
     * @see #getKeywordsType()
     * @generated
     */
	EAttribute getKeywordsType_Keyword();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.KeywordsType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Type</em>'.
     * @see net.opengis.ows10.KeywordsType#getType()
     * @see #getKeywordsType()
     * @generated
     */
	EReference getKeywordsType_Type();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.MetadataType <em>Metadata Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Metadata Type</em>'.
     * @see net.opengis.ows10.MetadataType
     * @generated
     */
	EClass getMetadataType();

	/**
     * Returns the meta object for the attribute list '{@link net.opengis.ows10.MetadataType#getAbstractMetaDataGroup <em>Abstract Meta Data Group</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Abstract Meta Data Group</em>'.
     * @see net.opengis.ows10.MetadataType#getAbstractMetaDataGroup()
     * @see #getMetadataType()
     * @generated
     */
	EAttribute getMetadataType_AbstractMetaDataGroup();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.MetadataType#getAbstractMetaData <em>Abstract Meta Data</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Meta Data</em>'.
     * @see net.opengis.ows10.MetadataType#getAbstractMetaData()
     * @see #getMetadataType()
     * @generated
     */
	EReference getMetadataType_AbstractMetaData();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.MetadataType#getAbout <em>About</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>About</em>'.
     * @see net.opengis.ows10.MetadataType#getAbout()
     * @see #getMetadataType()
     * @generated
     */
	EAttribute getMetadataType_About();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.OnlineResourceType <em>Online Resource Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Online Resource Type</em>'.
     * @see net.opengis.ows10.OnlineResourceType
     * @generated
     */
	EClass getOnlineResourceType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.OnlineResourceType#getHref <em>Href</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Href</em>'.
     * @see net.opengis.ows10.OnlineResourceType#getHref()
     * @see #getOnlineResourceType()
     * @generated
     */
	EAttribute getOnlineResourceType_Href();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.OperationType <em>Operation Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Operation Type</em>'.
     * @see net.opengis.ows10.OperationType
     * @generated
     */
	EClass getOperationType();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.OperationType#getDCP <em>DCP</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>DCP</em>'.
     * @see net.opengis.ows10.OperationType#getDCP()
     * @see #getOperationType()
     * @generated
     */
	EReference getOperationType_DCP();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.OperationType#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter</em>'.
     * @see net.opengis.ows10.OperationType#getParameter()
     * @see #getOperationType()
     * @generated
     */
	EReference getOperationType_Parameter();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.OperationType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.ows10.OperationType#getConstraint()
     * @see #getOperationType()
     * @generated
     */
	EReference getOperationType_Constraint();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.OperationType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.ows10.OperationType#getMetadata()
     * @see #getOperationType()
     * @generated
     */
	EReference getOperationType_Metadata();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.OperationType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.ows10.OperationType#getName()
     * @see #getOperationType()
     * @generated
     */
	EAttribute getOperationType_Name();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.OperationsMetadataType <em>Operations Metadata Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Operations Metadata Type</em>'.
     * @see net.opengis.ows10.OperationsMetadataType
     * @generated
     */
	EClass getOperationsMetadataType();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.OperationsMetadataType#getOperation <em>Operation</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Operation</em>'.
     * @see net.opengis.ows10.OperationsMetadataType#getOperation()
     * @see #getOperationsMetadataType()
     * @generated
     */
	EReference getOperationsMetadataType_Operation();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.OperationsMetadataType#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter</em>'.
     * @see net.opengis.ows10.OperationsMetadataType#getParameter()
     * @see #getOperationsMetadataType()
     * @generated
     */
	EReference getOperationsMetadataType_Parameter();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.OperationsMetadataType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.ows10.OperationsMetadataType#getConstraint()
     * @see #getOperationsMetadataType()
     * @generated
     */
	EReference getOperationsMetadataType_Constraint();

	/**
     * Returns the meta object for the reference '{@link net.opengis.ows10.OperationsMetadataType#getExtendedCapabilities <em>Extended Capabilities</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Extended Capabilities</em>'.
     * @see net.opengis.ows10.OperationsMetadataType#getExtendedCapabilities()
     * @see #getOperationsMetadataType()
     * @generated
     */
	EReference getOperationsMetadataType_ExtendedCapabilities();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.RequestMethodType <em>Request Method Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Request Method Type</em>'.
     * @see net.opengis.ows10.RequestMethodType
     * @generated
     */
	EClass getRequestMethodType();

	/**
     * Returns the meta object for the containment reference list '{@link net.opengis.ows10.RequestMethodType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.ows10.RequestMethodType#getConstraint()
     * @see #getRequestMethodType()
     * @generated
     */
	EReference getRequestMethodType_Constraint();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.ResponsiblePartySubsetType <em>Responsible Party Subset Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Responsible Party Subset Type</em>'.
     * @see net.opengis.ows10.ResponsiblePartySubsetType
     * @generated
     */
	EClass getResponsiblePartySubsetType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ResponsiblePartySubsetType#getIndividualName <em>Individual Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Individual Name</em>'.
     * @see net.opengis.ows10.ResponsiblePartySubsetType#getIndividualName()
     * @see #getResponsiblePartySubsetType()
     * @generated
     */
	EAttribute getResponsiblePartySubsetType_IndividualName();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ResponsiblePartySubsetType#getPositionName <em>Position Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Position Name</em>'.
     * @see net.opengis.ows10.ResponsiblePartySubsetType#getPositionName()
     * @see #getResponsiblePartySubsetType()
     * @generated
     */
	EAttribute getResponsiblePartySubsetType_PositionName();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ResponsiblePartySubsetType#getContactInfo <em>Contact Info</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contact Info</em>'.
     * @see net.opengis.ows10.ResponsiblePartySubsetType#getContactInfo()
     * @see #getResponsiblePartySubsetType()
     * @generated
     */
	EReference getResponsiblePartySubsetType_ContactInfo();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ResponsiblePartySubsetType#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Role</em>'.
     * @see net.opengis.ows10.ResponsiblePartySubsetType#getRole()
     * @see #getResponsiblePartySubsetType()
     * @generated
     */
	EReference getResponsiblePartySubsetType_Role();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.ResponsiblePartyType <em>Responsible Party Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Responsible Party Type</em>'.
     * @see net.opengis.ows10.ResponsiblePartyType
     * @generated
     */
	EClass getResponsiblePartyType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ResponsiblePartyType#getIndividualName <em>Individual Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Individual Name</em>'.
     * @see net.opengis.ows10.ResponsiblePartyType#getIndividualName()
     * @see #getResponsiblePartyType()
     * @generated
     */
	EAttribute getResponsiblePartyType_IndividualName();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ResponsiblePartyType#getOrganisationName <em>Organisation Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Organisation Name</em>'.
     * @see net.opengis.ows10.ResponsiblePartyType#getOrganisationName()
     * @see #getResponsiblePartyType()
     * @generated
     */
	EAttribute getResponsiblePartyType_OrganisationName();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ResponsiblePartyType#getPositionName <em>Position Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Position Name</em>'.
     * @see net.opengis.ows10.ResponsiblePartyType#getPositionName()
     * @see #getResponsiblePartyType()
     * @generated
     */
	EAttribute getResponsiblePartyType_PositionName();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ResponsiblePartyType#getContactInfo <em>Contact Info</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contact Info</em>'.
     * @see net.opengis.ows10.ResponsiblePartyType#getContactInfo()
     * @see #getResponsiblePartyType()
     * @generated
     */
	EReference getResponsiblePartyType_ContactInfo();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ResponsiblePartyType#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Role</em>'.
     * @see net.opengis.ows10.ResponsiblePartyType#getRole()
     * @see #getResponsiblePartyType()
     * @generated
     */
	EReference getResponsiblePartyType_Role();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.SectionsType <em>Sections Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sections Type</em>'.
     * @see net.opengis.ows10.SectionsType
     * @generated
     */
	EClass getSectionsType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.SectionsType#getSection <em>Section</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Section</em>'.
     * @see net.opengis.ows10.SectionsType#getSection()
     * @see #getSectionsType()
     * @generated
     */
	EAttribute getSectionsType_Section();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.ServiceIdentificationType <em>Service Identification Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Identification Type</em>'.
     * @see net.opengis.ows10.ServiceIdentificationType
     * @generated
     */
	EClass getServiceIdentificationType();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ServiceIdentificationType#getServiceType <em>Service Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Type</em>'.
     * @see net.opengis.ows10.ServiceIdentificationType#getServiceType()
     * @see #getServiceIdentificationType()
     * @generated
     */
	EReference getServiceIdentificationType_ServiceType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ServiceIdentificationType#getServiceTypeVersion <em>Service Type Version</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service Type Version</em>'.
     * @see net.opengis.ows10.ServiceIdentificationType#getServiceTypeVersion()
     * @see #getServiceIdentificationType()
     * @generated
     */
	EAttribute getServiceIdentificationType_ServiceTypeVersion();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ServiceIdentificationType#getFees <em>Fees</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fees</em>'.
     * @see net.opengis.ows10.ServiceIdentificationType#getFees()
     * @see #getServiceIdentificationType()
     * @generated
     */
	EAttribute getServiceIdentificationType_Fees();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ServiceIdentificationType#getAccessConstraints <em>Access Constraints</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Access Constraints</em>'.
     * @see net.opengis.ows10.ServiceIdentificationType#getAccessConstraints()
     * @see #getServiceIdentificationType()
     * @generated
     */
	EAttribute getServiceIdentificationType_AccessConstraints();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.ServiceProviderType <em>Service Provider Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Provider Type</em>'.
     * @see net.opengis.ows10.ServiceProviderType
     * @generated
     */
	EClass getServiceProviderType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.ServiceProviderType#getProviderName <em>Provider Name</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Provider Name</em>'.
     * @see net.opengis.ows10.ServiceProviderType#getProviderName()
     * @see #getServiceProviderType()
     * @generated
     */
	EAttribute getServiceProviderType_ProviderName();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ServiceProviderType#getProviderSite <em>Provider Site</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Provider Site</em>'.
     * @see net.opengis.ows10.ServiceProviderType#getProviderSite()
     * @see #getServiceProviderType()
     * @generated
     */
	EReference getServiceProviderType_ProviderSite();

	/**
     * Returns the meta object for the containment reference '{@link net.opengis.ows10.ServiceProviderType#getServiceContact <em>Service Contact</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Contact</em>'.
     * @see net.opengis.ows10.ServiceProviderType#getServiceContact()
     * @see #getServiceProviderType()
     * @generated
     */
	EReference getServiceProviderType_ServiceContact();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.TelephoneType <em>Telephone Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>Telephone Type</em>'.
     * @see net.opengis.ows10.TelephoneType
     * @generated
     */
	EClass getTelephoneType();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.TelephoneType#getVoice <em>Voice</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Voice</em>'.
     * @see net.opengis.ows10.TelephoneType#getVoice()
     * @see #getTelephoneType()
     * @generated
     */
	EAttribute getTelephoneType_Voice();

	/**
     * Returns the meta object for the attribute '{@link net.opengis.ows10.TelephoneType#getFacsimile <em>Facsimile</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Facsimile</em>'.
     * @see net.opengis.ows10.TelephoneType#getFacsimile()
     * @see #getTelephoneType()
     * @generated
     */
	EAttribute getTelephoneType_Facsimile();

	/**
     * Returns the meta object for class '{@link net.opengis.ows10.WGS84BoundingBoxType <em>WGS84 Bounding Box Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for class '<em>WGS84 Bounding Box Type</em>'.
     * @see net.opengis.ows10.WGS84BoundingBoxType
     * @generated
     */
	EClass getWGS84BoundingBoxType();

	/**
     * Returns the meta object for class '{@link java.lang.Object <em>Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Object</em>'.
     * @see java.lang.Object
     * @model instanceClass="java.lang.Object"
     * @generated
     */
    EClass getObject();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Mime Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Mime Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     * @generated
     */
	EDataType getMimeType();

	/**
     * Returns the meta object for data type '{@link java.lang.String <em>Version Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Version Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     * @generated
     */
	EDataType getVersionType();

	/**
     * Returns the meta object for data type '{@link java.util.List <em>Position Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Position Type</em>'.
     * @see java.util.List
     * @model instanceClass="java.util.List"
     * @generated
     */
	EDataType getPositionType();

	/**
     * Returns the meta object for data type '{@link java.lang.String <em>Update Sequence Type</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Update Sequence Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     * @generated
     */
	EDataType getUpdateSequenceType();

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
	Ows10Factory getOws10Factory();

} //Ows10Package
