/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import net.opengis.ows10.Ows10Package;

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
 * @see net.opengis.cat.csw20.Csw20Factory
 * @model kind="package"
 * @generated
 */
public interface Csw20Package extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "csw20";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http:///net/opengis/cat/csw20.ecore";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "net.opengis.cat.csw20";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    Csw20Package eINSTANCE = net.opengis.cat.csw20.impl.Csw20PackageImpl.init();

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.AbstractQueryTypeImpl <em>Abstract Query Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.AbstractQueryTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getAbstractQueryType()
     * @generated
     */
    int ABSTRACT_QUERY_TYPE = 0;

    /**
     * The number of structural features of the '<em>Abstract Query Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_QUERY_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.AbstractRecordTypeImpl <em>Abstract Record Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.AbstractRecordTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getAbstractRecordType()
     * @generated
     */
    int ABSTRACT_RECORD_TYPE = 1;

    /**
     * The number of structural features of the '<em>Abstract Record Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_RECORD_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.AcknowledgementTypeImpl <em>Acknowledgement Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.AcknowledgementTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getAcknowledgementType()
     * @generated
     */
    int ACKNOWLEDGEMENT_TYPE = 2;

    /**
     * The feature id for the '<em><b>Echoed Request</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST = 0;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACKNOWLEDGEMENT_TYPE__REQUEST_ID = 1;

    /**
     * The feature id for the '<em><b>Time Stamp</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACKNOWLEDGEMENT_TYPE__TIME_STAMP = 2;

    /**
     * The number of structural features of the '<em>Acknowledgement Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACKNOWLEDGEMENT_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl <em>Brief Record Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.BriefRecordTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getBriefRecordType()
     * @generated
     */
    int BRIEF_RECORD_TYPE = 3;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRIEF_RECORD_TYPE__IDENTIFIER = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Title</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRIEF_RECORD_TYPE__TITLE = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRIEF_RECORD_TYPE__TYPE = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRIEF_RECORD_TYPE__BOUNDING_BOX = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Brief Record Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BRIEF_RECORD_TYPE_FEATURE_COUNT = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.CapabilitiesTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getCapabilitiesType()
     * @generated
     */
    int CAPABILITIES_TYPE = 4;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_IDENTIFICATION = Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION;

    /**
     * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_PROVIDER = Ows10Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__OPERATIONS_METADATA = Ows10Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__UPDATE_SEQUENCE = Ows10Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__VERSION = Ows10Package.CAPABILITIES_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Filter Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__FILTER_CAPABILITIES = Ows10Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE_FEATURE_COUNT = Ows10Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.ConceptualSchemeTypeImpl <em>Conceptual Scheme Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.ConceptualSchemeTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getConceptualSchemeType()
     * @generated
     */
    int CONCEPTUAL_SCHEME_TYPE = 5;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONCEPTUAL_SCHEME_TYPE__NAME = 0;

    /**
     * The feature id for the '<em><b>Document</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONCEPTUAL_SCHEME_TYPE__DOCUMENT = 1;

    /**
     * The feature id for the '<em><b>Authority</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONCEPTUAL_SCHEME_TYPE__AUTHORITY = 2;

    /**
     * The number of structural features of the '<em>Conceptual Scheme Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONCEPTUAL_SCHEME_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.DeleteTypeImpl <em>Delete Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.DeleteTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDeleteType()
     * @generated
     */
    int DELETE_TYPE = 6;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELETE_TYPE__CONSTRAINT = 0;

    /**
     * The feature id for the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELETE_TYPE__HANDLE = 1;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELETE_TYPE__TYPE_NAME = 2;

    /**
     * The number of structural features of the '<em>Delete Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DELETE_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.DescribeRecordResponseTypeImpl <em>Describe Record Response Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.DescribeRecordResponseTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDescribeRecordResponseType()
     * @generated
     */
    int DESCRIBE_RECORD_RESPONSE_TYPE = 7;

    /**
     * The feature id for the '<em><b>Schema Component</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT = 0;

    /**
     * The number of structural features of the '<em>Describe Record Response Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_RESPONSE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.RequestBaseTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRequestBaseType()
     * @generated
     */
    int REQUEST_BASE_TYPE = 29;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__SERVICE = 0;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__VERSION = 1;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__BASE_URL = 2;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__EXTENDED_PROPERTIES = 3;

    /**
     * The number of structural features of the '<em>Request Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.DescribeRecordTypeImpl <em>Describe Record Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.DescribeRecordTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDescribeRecordType()
     * @generated
     */
    int DESCRIBE_RECORD_TYPE = 8;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_TYPE__TYPE_NAME = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Schema Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Describe Record Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_RECORD_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.DistributedSearchTypeImpl <em>Distributed Search Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.DistributedSearchTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDistributedSearchType()
     * @generated
     */
    int DISTRIBUTED_SEARCH_TYPE = 9;

    /**
     * The feature id for the '<em><b>Hop Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTED_SEARCH_TYPE__HOP_COUNT = 0;

    /**
     * The number of structural features of the '<em>Distributed Search Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTRIBUTED_SEARCH_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl <em>Domain Values Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.DomainValuesTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDomainValuesType()
     * @generated
     */
    int DOMAIN_VALUES_TYPE = 10;

    /**
     * The feature id for the '<em><b>Property Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_VALUES_TYPE__PROPERTY_NAME = 0;

    /**
     * The feature id for the '<em><b>Parameter Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_VALUES_TYPE__PARAMETER_NAME = 1;

    /**
     * The feature id for the '<em><b>List Of Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_VALUES_TYPE__LIST_OF_VALUES = 2;

    /**
     * The feature id for the '<em><b>Conceptual Scheme</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME = 3;

    /**
     * The feature id for the '<em><b>Range Of Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_VALUES_TYPE__RANGE_OF_VALUES = 4;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_VALUES_TYPE__TYPE = 5;

    /**
     * The feature id for the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_VALUES_TYPE__UOM = 6;

    /**
     * The number of structural features of the '<em>Domain Values Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_VALUES_TYPE_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.EchoedRequestTypeImpl <em>Echoed Request Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.EchoedRequestTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getEchoedRequestType()
     * @generated
     */
    int ECHOED_REQUEST_TYPE = 11;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ECHOED_REQUEST_TYPE__ANY = 0;

    /**
     * The number of structural features of the '<em>Echoed Request Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ECHOED_REQUEST_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.ElementSetNameTypeImpl <em>Element Set Name Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.ElementSetNameTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getElementSetNameType()
     * @generated
     */
    int ELEMENT_SET_NAME_TYPE = 12;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELEMENT_SET_NAME_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Type Names</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELEMENT_SET_NAME_TYPE__TYPE_NAMES = 1;

    /**
     * The number of structural features of the '<em>Element Set Name Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ELEMENT_SET_NAME_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.EmptyTypeImpl <em>Empty Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.EmptyTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getEmptyType()
     * @generated
     */
    int EMPTY_TYPE = 13;

    /**
     * The number of structural features of the '<em>Empty Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.GetCapabilitiesTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetCapabilitiesType()
     * @generated
     */
    int GET_CAPABILITIES_TYPE = 14;

    /**
     * The feature id for the '<em><b>Accept Versions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = Ows10Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS;

    /**
     * The feature id for the '<em><b>Sections</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SECTIONS = Ows10Package.GET_CAPABILITIES_TYPE__SECTIONS;

    /**
     * The feature id for the '<em><b>Accept Formats</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = Ows10Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = Ows10Package.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__BASE_URL = Ows10Package.GET_CAPABILITIES_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__NAMESPACE = Ows10Package.GET_CAPABILITIES_TYPE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES = Ows10Package.GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SERVICE = Ows10Package.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Get Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE_FEATURE_COUNT = Ows10Package.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.GetDomainResponseTypeImpl <em>Get Domain Response Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.GetDomainResponseTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetDomainResponseType()
     * @generated
     */
    int GET_DOMAIN_RESPONSE_TYPE = 15;

    /**
     * The feature id for the '<em><b>Domain Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES = 0;

    /**
     * The number of structural features of the '<em>Get Domain Response Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_RESPONSE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.GetDomainTypeImpl <em>Get Domain Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.GetDomainTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetDomainType()
     * @generated
     */
    int GET_DOMAIN_TYPE = 16;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

    /**
     * The feature id for the '<em><b>Property Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_TYPE__PROPERTY_NAME = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Parameter Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_TYPE__PARAMETER_NAME = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Get Domain Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_DOMAIN_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.GetRecordByIdTypeImpl <em>Get Record By Id Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.GetRecordByIdTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetRecordByIdType()
     * @generated
     */
    int GET_RECORD_BY_ID_TYPE = 17;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE__ID = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Element Set Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Output Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE__OUTPUT_SCHEMA = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Get Record By Id Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORD_BY_ID_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.GetRecordsResponseTypeImpl <em>Get Records Response Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.GetRecordsResponseTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetRecordsResponseType()
     * @generated
     */
    int GET_RECORDS_RESPONSE_TYPE = 18;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_RESPONSE_TYPE__REQUEST_ID = 0;

    /**
     * The feature id for the '<em><b>Search Status</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS = 1;

    /**
     * The feature id for the '<em><b>Search Results</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS = 2;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_RESPONSE_TYPE__VERSION = 3;

    /**
     * The number of structural features of the '<em>Get Records Response Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_RESPONSE_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl <em>Get Records Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.GetRecordsTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetRecordsType()
     * @generated
     */
    int GET_RECORDS_TYPE = 19;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

    /**
     * The feature id for the '<em><b>Distributed Search</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__DISTRIBUTED_SEARCH = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Response Handler</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__RESPONSE_HANDLER = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__ANY = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Max Records</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__MAX_RECORDS = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__OUTPUT_FORMAT = REQUEST_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Output Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__OUTPUT_SCHEMA = REQUEST_BASE_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__REQUEST_ID = REQUEST_BASE_TYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Result Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__RESULT_TYPE = REQUEST_BASE_TYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Start Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__START_POSITION = REQUEST_BASE_TYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Query</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE__QUERY = REQUEST_BASE_TYPE_FEATURE_COUNT + 9;

    /**
     * The number of structural features of the '<em>Get Records Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_RECORDS_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 10;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.HarvestResponseTypeImpl <em>Harvest Response Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.HarvestResponseTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getHarvestResponseType()
     * @generated
     */
    int HARVEST_RESPONSE_TYPE = 20;

    /**
     * The feature id for the '<em><b>Acknowledgement</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT = 0;

    /**
     * The feature id for the '<em><b>Transaction Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE = 1;

    /**
     * The number of structural features of the '<em>Harvest Response Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_RESPONSE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.HarvestTypeImpl <em>Harvest Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.HarvestTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getHarvestType()
     * @generated
     */
    int HARVEST_TYPE = 21;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

    /**
     * The feature id for the '<em><b>Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__SOURCE = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Resource Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__RESOURCE_TYPE = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Resource Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__RESOURCE_FORMAT = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Harvest Interval</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__HARVEST_INTERVAL = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Response Handler</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE__RESPONSE_HANDLER = REQUEST_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Harvest Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HARVEST_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.InsertResultTypeImpl <em>Insert Result Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.InsertResultTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getInsertResultType()
     * @generated
     */
    int INSERT_RESULT_TYPE = 22;

    /**
     * The feature id for the '<em><b>Brief Record</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSERT_RESULT_TYPE__BRIEF_RECORD = 0;

    /**
     * The feature id for the '<em><b>Handle Ref</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSERT_RESULT_TYPE__HANDLE_REF = 1;

    /**
     * The number of structural features of the '<em>Insert Result Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSERT_RESULT_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.InsertTypeImpl <em>Insert Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.InsertTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getInsertType()
     * @generated
     */
    int INSERT_TYPE = 23;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSERT_TYPE__ANY = 0;

    /**
     * The feature id for the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSERT_TYPE__HANDLE = 1;

    /**
     * The feature id for the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSERT_TYPE__TYPE_NAME = 2;

    /**
     * The number of structural features of the '<em>Insert Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INSERT_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.ListOfValuesTypeImpl <em>List Of Values Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.ListOfValuesTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getListOfValuesType()
     * @generated
     */
    int LIST_OF_VALUES_TYPE = 24;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_OF_VALUES_TYPE__VALUE = 0;

    /**
     * The number of structural features of the '<em>List Of Values Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LIST_OF_VALUES_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.QueryConstraintTypeImpl <em>Query Constraint Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.QueryConstraintTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getQueryConstraintType()
     * @generated
     */
    int QUERY_CONSTRAINT_TYPE = 25;

    /**
     * The feature id for the '<em><b>Filter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_CONSTRAINT_TYPE__FILTER = 0;

    /**
     * The feature id for the '<em><b>Cql Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_CONSTRAINT_TYPE__CQL_TEXT = 1;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_CONSTRAINT_TYPE__VERSION = 2;

    /**
     * The number of structural features of the '<em>Query Constraint Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_CONSTRAINT_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.QueryTypeImpl <em>Query Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.QueryTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getQueryType()
     * @generated
     */
    int QUERY_TYPE = 26;

    /**
     * The feature id for the '<em><b>Element Set Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_TYPE__ELEMENT_SET_NAME = ABSTRACT_QUERY_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Element Name</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_TYPE__ELEMENT_NAME = ABSTRACT_QUERY_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_TYPE__CONSTRAINT = ABSTRACT_QUERY_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Sort By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_TYPE__SORT_BY = ABSTRACT_QUERY_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Type Names</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_TYPE__TYPE_NAMES = ABSTRACT_QUERY_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Query Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int QUERY_TYPE_FEATURE_COUNT = ABSTRACT_QUERY_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.RangeOfValuesTypeImpl <em>Range Of Values Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.RangeOfValuesTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRangeOfValuesType()
     * @generated
     */
    int RANGE_OF_VALUES_TYPE = 27;

    /**
     * The feature id for the '<em><b>Min Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_OF_VALUES_TYPE__MIN_VALUE = 0;

    /**
     * The feature id for the '<em><b>Max Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_OF_VALUES_TYPE__MAX_VALUE = 1;

    /**
     * The number of structural features of the '<em>Range Of Values Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_OF_VALUES_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.RecordPropertyTypeImpl <em>Record Property Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.RecordPropertyTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRecordPropertyType()
     * @generated
     */
    int RECORD_PROPERTY_TYPE = 28;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECORD_PROPERTY_TYPE__NAME = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECORD_PROPERTY_TYPE__VALUE = 1;

    /**
     * The number of structural features of the '<em>Record Property Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECORD_PROPERTY_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.RequestStatusTypeImpl <em>Request Status Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.RequestStatusTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRequestStatusType()
     * @generated
     */
    int REQUEST_STATUS_TYPE = 30;

    /**
     * The feature id for the '<em><b>Timestamp</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_STATUS_TYPE__TIMESTAMP = 0;

    /**
     * The number of structural features of the '<em>Request Status Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_STATUS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.SchemaComponentTypeImpl <em>Schema Component Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.SchemaComponentTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSchemaComponentType()
     * @generated
     */
    int SCHEMA_COMPONENT_TYPE = 31;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCHEMA_COMPONENT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCHEMA_COMPONENT_TYPE__ANY = 1;

    /**
     * The feature id for the '<em><b>Parent Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCHEMA_COMPONENT_TYPE__PARENT_SCHEMA = 2;

    /**
     * The feature id for the '<em><b>Schema Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCHEMA_COMPONENT_TYPE__SCHEMA_LANGUAGE = 3;

    /**
     * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCHEMA_COMPONENT_TYPE__TARGET_NAMESPACE = 4;

    /**
     * The number of structural features of the '<em>Schema Component Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCHEMA_COMPONENT_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl <em>Search Results Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.SearchResultsTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSearchResultsType()
     * @generated
     */
    int SEARCH_RESULTS_TYPE = 32;

    /**
     * The feature id for the '<em><b>Abstract Record Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP = 0;

    /**
     * The feature id for the '<em><b>Abstract Record</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__ABSTRACT_RECORD = 1;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__ANY = 2;

    /**
     * The feature id for the '<em><b>Element Set</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__ELEMENT_SET = 3;

    /**
     * The feature id for the '<em><b>Expires</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__EXPIRES = 4;

    /**
     * The feature id for the '<em><b>Next Record</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__NEXT_RECORD = 5;

    /**
     * The feature id for the '<em><b>Number Of Records Matched</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_MATCHED = 6;

    /**
     * The feature id for the '<em><b>Number Of Records Returned</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_RETURNED = 7;

    /**
     * The feature id for the '<em><b>Record Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__RECORD_SCHEMA = 8;

    /**
     * The feature id for the '<em><b>Result Set Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE__RESULT_SET_ID = 9;

    /**
     * The number of structural features of the '<em>Search Results Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SEARCH_RESULTS_TYPE_FEATURE_COUNT = 10;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.TransactionResponseTypeImpl <em>Transaction Response Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.TransactionResponseTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTransactionResponseType()
     * @generated
     */
    int TRANSACTION_RESPONSE_TYPE = 33;

    /**
     * The feature id for the '<em><b>Transaction Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY = 0;

    /**
     * The feature id for the '<em><b>Insert Result</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_RESPONSE_TYPE__INSERT_RESULT = 1;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_RESPONSE_TYPE__VERSION = 2;

    /**
     * The number of structural features of the '<em>Transaction Response Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_RESPONSE_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.TransactionSummaryTypeImpl <em>Transaction Summary Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.TransactionSummaryTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTransactionSummaryType()
     * @generated
     */
    int TRANSACTION_SUMMARY_TYPE = 34;

    /**
     * The feature id for the '<em><b>Total Inserted</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED = 0;

    /**
     * The feature id for the '<em><b>Total Updated</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED = 1;

    /**
     * The feature id for the '<em><b>Total Deleted</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED = 2;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_SUMMARY_TYPE__REQUEST_ID = 3;

    /**
     * The number of structural features of the '<em>Transaction Summary Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_SUMMARY_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.TransactionTypeImpl <em>Transaction Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.TransactionTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTransactionType()
     * @generated
     */
    int TRANSACTION_TYPE = 35;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__GROUP = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Insert</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__INSERT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Update</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__UPDATE = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Delete</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__DELETE = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__REQUEST_ID = REQUEST_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Verbose Response</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE__VERBOSE_RESPONSE = REQUEST_BASE_TYPE_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Transaction Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSACTION_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 6;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.UpdateTypeImpl <em>Update Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.UpdateTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getUpdateType()
     * @generated
     */
    int UPDATE_TYPE = 36;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPDATE_TYPE__ANY = 0;

    /**
     * The feature id for the '<em><b>Record Property</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPDATE_TYPE__RECORD_PROPERTY = 1;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPDATE_TYPE__CONSTRAINT = 2;

    /**
     * The feature id for the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPDATE_TYPE__HANDLE = 3;

    /**
     * The number of structural features of the '<em>Update Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPDATE_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link java.lang.String <em>String</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getString()
     * @generated
     */
    int STRING = 37;

    /**
     * The number of structural features of the '<em>String</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STRING_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link org.opengis.filter.capability.FilterCapabilities <em>Filter Capabilities</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.opengis.filter.capability.FilterCapabilities
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getFilterCapabilities()
     * @generated
     */
    int FILTER_CAPABILITIES = 38;

    /**
     * The number of structural features of the '<em>Filter Capabilities</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link org.opengis.filter.Filter <em>Filter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.opengis.filter.Filter
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getFilter()
     * @generated
     */
    int FILTER = 39;

    /**
     * The number of structural features of the '<em>Filter</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link org.opengis.filter.sort.SortBy <em>Sort By</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.opengis.filter.sort.SortBy
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSortBy()
     * @generated
     */
    int SORT_BY = 40;

    /**
     * The number of structural features of the '<em>Sort By</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SORT_BY_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.DCMIRecordTypeImpl <em>DCMI Record Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.DCMIRecordTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDCMIRecordType()
     * @generated
     */
    int DCMI_RECORD_TYPE = 41;

    /**
     * The feature id for the '<em><b>DC Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DCMI_RECORD_TYPE__DC_ELEMENT = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>DCMI Record Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DCMI_RECORD_TYPE_FEATURE_COUNT = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.RecordTypeImpl <em>Record Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.RecordTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRecordType()
     * @generated
     */
    int RECORD_TYPE = 42;

    /**
     * The feature id for the '<em><b>DC Element</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECORD_TYPE__DC_ELEMENT = DCMI_RECORD_TYPE__DC_ELEMENT;

    /**
     * The feature id for the '<em><b>Any Text</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECORD_TYPE__ANY_TEXT = DCMI_RECORD_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECORD_TYPE__BOUNDING_BOX = DCMI_RECORD_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Record Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RECORD_TYPE_FEATURE_COUNT = DCMI_RECORD_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.SimpleLiteralImpl <em>Simple Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.SimpleLiteralImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSimpleLiteral()
     * @generated
     */
    int SIMPLE_LITERAL = 43;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_LITERAL__VALUE = 0;

    /**
     * The feature id for the '<em><b>Scheme</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_LITERAL__SCHEME = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_LITERAL__NAME = 2;

    /**
     * The number of structural features of the '<em>Simple Literal</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SIMPLE_LITERAL_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl <em>Summary Record Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.SummaryRecordTypeImpl
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSummaryRecordType()
     * @generated
     */
    int SUMMARY_RECORD_TYPE = 44;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__IDENTIFIER = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__TITLE = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__TYPE = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Subject</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__SUBJECT = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Format</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__FORMAT = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Relation</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__RELATION = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Modified</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__MODIFIED = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__ABSTRACT = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Spatial</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__SPATIAL = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE__BOUNDING_BOX = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 9;

    /**
     * The number of structural features of the '<em>Summary Record Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUMMARY_RECORD_TYPE_FEATURE_COUNT = ABSTRACT_RECORD_TYPE_FEATURE_COUNT + 10;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.ElementSetType <em>Element Set Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.ElementSetType
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getElementSetType()
     * @generated
     */
    int ELEMENT_SET_TYPE = 45;

    /**
     * The meta object id for the '{@link net.opengis.cat.csw20.ResultType <em>Result Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.ResultType
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getResultType()
     * @generated
     */
    int RESULT_TYPE = 46;

    /**
     * The meta object id for the '<em>Type Name List Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTypeNameListType()
     * @generated
     */
    int TYPE_NAME_LIST_TYPE = 47;

    /**
     * The meta object id for the '<em>Service Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getServiceType()
     * @generated
     */
    int SERVICE_TYPE = 48;

    /**
     * The meta object id for the '<em>Type Name List Type 1</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTypeNameListType_1()
     * @generated
     */
    int TYPE_NAME_LIST_TYPE_1 = 49;

    /**
     * The meta object id for the '<em>Service Type 1</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getServiceType_1()
     * @generated
     */
    int SERVICE_TYPE_1 = 50;

    /**
     * The meta object id for the '<em>Version Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getVersionType()
     * @generated
     */
    int VERSION_TYPE = 51;


    /**
     * The meta object id for the '<em>Calendar</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Calendar
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getCalendar()
     * @generated
     */
    int CALENDAR = 52;


    /**
     * The meta object id for the '<em>Set</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Set
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSet()
     * @generated
     */
    int SET = 53;


    /**
     * The meta object id for the '<em>URI</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.net.URI
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getURI()
     * @generated
     */
    int URI = 54;


    /**
     * The meta object id for the '<em>QName</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.xml.namespace.QName
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getQName()
     * @generated
     */
    int QNAME = 55;


    /**
     * The meta object id for the '<em>Duration</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.xml.datatype.Duration
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDuration()
     * @generated
     */
    int DURATION = 56;


    /**
     * The meta object id for the '<em>Map</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Map
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getMap()
     * @generated
     */
    int MAP = 57;


    /**
     * The meta object id for the '<em>Sort By Array</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSortByArray()
     * @generated
     */
    int SORT_BY_ARRAY = 58;


    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.AbstractQueryType <em>Abstract Query Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Query Type</em>'.
     * @see net.opengis.cat.csw20.AbstractQueryType
     * @generated
     */
    EClass getAbstractQueryType();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.AbstractRecordType <em>Abstract Record Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Record Type</em>'.
     * @see net.opengis.cat.csw20.AbstractRecordType
     * @generated
     */
    EClass getAbstractRecordType();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.AcknowledgementType <em>Acknowledgement Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Acknowledgement Type</em>'.
     * @see net.opengis.cat.csw20.AcknowledgementType
     * @generated
     */
    EClass getAcknowledgementType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.AcknowledgementType#getEchoedRequest <em>Echoed Request</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Echoed Request</em>'.
     * @see net.opengis.cat.csw20.AcknowledgementType#getEchoedRequest()
     * @see #getAcknowledgementType()
     * @generated
     */
    EReference getAcknowledgementType_EchoedRequest();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.AcknowledgementType#getRequestId <em>Request Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Id</em>'.
     * @see net.opengis.cat.csw20.AcknowledgementType#getRequestId()
     * @see #getAcknowledgementType()
     * @generated
     */
    EAttribute getAcknowledgementType_RequestId();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.AcknowledgementType#getTimeStamp <em>Time Stamp</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Time Stamp</em>'.
     * @see net.opengis.cat.csw20.AcknowledgementType#getTimeStamp()
     * @see #getAcknowledgementType()
     * @generated
     */
    EAttribute getAcknowledgementType_TimeStamp();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.BriefRecordType <em>Brief Record Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Brief Record Type</em>'.
     * @see net.opengis.cat.csw20.BriefRecordType
     * @generated
     */
    EClass getBriefRecordType();

    /**
     * Returns the meta object for the reference list '{@link net.opengis.cat.csw20.BriefRecordType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Identifier</em>'.
     * @see net.opengis.cat.csw20.BriefRecordType#getIdentifier()
     * @see #getBriefRecordType()
     * @generated
     */
    EReference getBriefRecordType_Identifier();

    /**
     * Returns the meta object for the reference list '{@link net.opengis.cat.csw20.BriefRecordType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Title</em>'.
     * @see net.opengis.cat.csw20.BriefRecordType#getTitle()
     * @see #getBriefRecordType()
     * @generated
     */
    EReference getBriefRecordType_Title();

    /**
     * Returns the meta object for the reference '{@link net.opengis.cat.csw20.BriefRecordType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Type</em>'.
     * @see net.opengis.cat.csw20.BriefRecordType#getType()
     * @see #getBriefRecordType()
     * @generated
     */
    EReference getBriefRecordType_Type();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.BriefRecordType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bounding Box</em>'.
     * @see net.opengis.cat.csw20.BriefRecordType#getBoundingBox()
     * @see #getBriefRecordType()
     * @generated
     */
    EReference getBriefRecordType_BoundingBox();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.CapabilitiesType <em>Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Capabilities Type</em>'.
     * @see net.opengis.cat.csw20.CapabilitiesType
     * @generated
     */
    EClass getCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.CapabilitiesType#getFilterCapabilities <em>Filter Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Filter Capabilities</em>'.
     * @see net.opengis.cat.csw20.CapabilitiesType#getFilterCapabilities()
     * @see #getCapabilitiesType()
     * @generated
     */
    EReference getCapabilitiesType_FilterCapabilities();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.ConceptualSchemeType <em>Conceptual Scheme Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Conceptual Scheme Type</em>'.
     * @see net.opengis.cat.csw20.ConceptualSchemeType
     * @generated
     */
    EClass getConceptualSchemeType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.ConceptualSchemeType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.cat.csw20.ConceptualSchemeType#getName()
     * @see #getConceptualSchemeType()
     * @generated
     */
    EAttribute getConceptualSchemeType_Name();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.ConceptualSchemeType#getDocument <em>Document</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Document</em>'.
     * @see net.opengis.cat.csw20.ConceptualSchemeType#getDocument()
     * @see #getConceptualSchemeType()
     * @generated
     */
    EAttribute getConceptualSchemeType_Document();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.ConceptualSchemeType#getAuthority <em>Authority</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Authority</em>'.
     * @see net.opengis.cat.csw20.ConceptualSchemeType#getAuthority()
     * @see #getConceptualSchemeType()
     * @generated
     */
    EAttribute getConceptualSchemeType_Authority();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.DeleteType <em>Delete Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Delete Type</em>'.
     * @see net.opengis.cat.csw20.DeleteType
     * @generated
     */
    EClass getDeleteType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.DeleteType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Constraint</em>'.
     * @see net.opengis.cat.csw20.DeleteType#getConstraint()
     * @see #getDeleteType()
     * @generated
     */
    EReference getDeleteType_Constraint();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DeleteType#getHandle <em>Handle</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Handle</em>'.
     * @see net.opengis.cat.csw20.DeleteType#getHandle()
     * @see #getDeleteType()
     * @generated
     */
    EAttribute getDeleteType_Handle();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DeleteType#getTypeName <em>Type Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type Name</em>'.
     * @see net.opengis.cat.csw20.DeleteType#getTypeName()
     * @see #getDeleteType()
     * @generated
     */
    EAttribute getDeleteType_TypeName();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.DescribeRecordResponseType <em>Describe Record Response Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Describe Record Response Type</em>'.
     * @see net.opengis.cat.csw20.DescribeRecordResponseType
     * @generated
     */
    EClass getDescribeRecordResponseType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.DescribeRecordResponseType#getSchemaComponent <em>Schema Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Schema Component</em>'.
     * @see net.opengis.cat.csw20.DescribeRecordResponseType#getSchemaComponent()
     * @see #getDescribeRecordResponseType()
     * @generated
     */
    EReference getDescribeRecordResponseType_SchemaComponent();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.DescribeRecordType <em>Describe Record Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Describe Record Type</em>'.
     * @see net.opengis.cat.csw20.DescribeRecordType
     * @generated
     */
    EClass getDescribeRecordType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.DescribeRecordType#getTypeName <em>Type Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Type Name</em>'.
     * @see net.opengis.cat.csw20.DescribeRecordType#getTypeName()
     * @see #getDescribeRecordType()
     * @generated
     */
    EAttribute getDescribeRecordType_TypeName();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DescribeRecordType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.cat.csw20.DescribeRecordType#getOutputFormat()
     * @see #getDescribeRecordType()
     * @generated
     */
    EAttribute getDescribeRecordType_OutputFormat();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DescribeRecordType#getSchemaLanguage <em>Schema Language</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Schema Language</em>'.
     * @see net.opengis.cat.csw20.DescribeRecordType#getSchemaLanguage()
     * @see #getDescribeRecordType()
     * @generated
     */
    EAttribute getDescribeRecordType_SchemaLanguage();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.DistributedSearchType <em>Distributed Search Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Distributed Search Type</em>'.
     * @see net.opengis.cat.csw20.DistributedSearchType
     * @generated
     */
    EClass getDistributedSearchType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DistributedSearchType#getHopCount <em>Hop Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Hop Count</em>'.
     * @see net.opengis.cat.csw20.DistributedSearchType#getHopCount()
     * @see #getDistributedSearchType()
     * @generated
     */
    EAttribute getDistributedSearchType_HopCount();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.DomainValuesType <em>Domain Values Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Domain Values Type</em>'.
     * @see net.opengis.cat.csw20.DomainValuesType
     * @generated
     */
    EClass getDomainValuesType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DomainValuesType#getPropertyName <em>Property Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Property Name</em>'.
     * @see net.opengis.cat.csw20.DomainValuesType#getPropertyName()
     * @see #getDomainValuesType()
     * @generated
     */
    EAttribute getDomainValuesType_PropertyName();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DomainValuesType#getParameterName <em>Parameter Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Parameter Name</em>'.
     * @see net.opengis.cat.csw20.DomainValuesType#getParameterName()
     * @see #getDomainValuesType()
     * @generated
     */
    EAttribute getDomainValuesType_ParameterName();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.DomainValuesType#getListOfValues <em>List Of Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>List Of Values</em>'.
     * @see net.opengis.cat.csw20.DomainValuesType#getListOfValues()
     * @see #getDomainValuesType()
     * @generated
     */
    EReference getDomainValuesType_ListOfValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.DomainValuesType#getConceptualScheme <em>Conceptual Scheme</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Conceptual Scheme</em>'.
     * @see net.opengis.cat.csw20.DomainValuesType#getConceptualScheme()
     * @see #getDomainValuesType()
     * @generated
     */
    EReference getDomainValuesType_ConceptualScheme();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.DomainValuesType#getRangeOfValues <em>Range Of Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Range Of Values</em>'.
     * @see net.opengis.cat.csw20.DomainValuesType#getRangeOfValues()
     * @see #getDomainValuesType()
     * @generated
     */
    EReference getDomainValuesType_RangeOfValues();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DomainValuesType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see net.opengis.cat.csw20.DomainValuesType#getType()
     * @see #getDomainValuesType()
     * @generated
     */
    EAttribute getDomainValuesType_Type();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.DomainValuesType#getUom <em>Uom</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Uom</em>'.
     * @see net.opengis.cat.csw20.DomainValuesType#getUom()
     * @see #getDomainValuesType()
     * @generated
     */
    EAttribute getDomainValuesType_Uom();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.EchoedRequestType <em>Echoed Request Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Echoed Request Type</em>'.
     * @see net.opengis.cat.csw20.EchoedRequestType
     * @generated
     */
    EClass getEchoedRequestType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.EchoedRequestType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.cat.csw20.EchoedRequestType#getAny()
     * @see #getEchoedRequestType()
     * @generated
     */
    EAttribute getEchoedRequestType_Any();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.ElementSetNameType <em>Element Set Name Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Element Set Name Type</em>'.
     * @see net.opengis.cat.csw20.ElementSetNameType
     * @generated
     */
    EClass getElementSetNameType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.ElementSetNameType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.cat.csw20.ElementSetNameType#getValue()
     * @see #getElementSetNameType()
     * @generated
     */
    EAttribute getElementSetNameType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.ElementSetNameType#getTypeNames <em>Type Names</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type Names</em>'.
     * @see net.opengis.cat.csw20.ElementSetNameType#getTypeNames()
     * @see #getElementSetNameType()
     * @generated
     */
    EAttribute getElementSetNameType_TypeNames();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.EmptyType <em>Empty Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Empty Type</em>'.
     * @see net.opengis.cat.csw20.EmptyType
     * @generated
     */
    EClass getEmptyType();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Capabilities Type</em>'.
     * @see net.opengis.cat.csw20.GetCapabilitiesType
     * @generated
     */
    EClass getGetCapabilitiesType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetCapabilitiesType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.cat.csw20.GetCapabilitiesType#getService()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_Service();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.GetDomainResponseType <em>Get Domain Response Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Domain Response Type</em>'.
     * @see net.opengis.cat.csw20.GetDomainResponseType
     * @generated
     */
    EClass getGetDomainResponseType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.GetDomainResponseType#getDomainValues <em>Domain Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Domain Values</em>'.
     * @see net.opengis.cat.csw20.GetDomainResponseType#getDomainValues()
     * @see #getGetDomainResponseType()
     * @generated
     */
    EReference getGetDomainResponseType_DomainValues();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.GetDomainType <em>Get Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Domain Type</em>'.
     * @see net.opengis.cat.csw20.GetDomainType
     * @generated
     */
    EClass getGetDomainType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetDomainType#getPropertyName <em>Property Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Property Name</em>'.
     * @see net.opengis.cat.csw20.GetDomainType#getPropertyName()
     * @see #getGetDomainType()
     * @generated
     */
    EAttribute getGetDomainType_PropertyName();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetDomainType#getParameterName <em>Parameter Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Parameter Name</em>'.
     * @see net.opengis.cat.csw20.GetDomainType#getParameterName()
     * @see #getGetDomainType()
     * @generated
     */
    EAttribute getGetDomainType_ParameterName();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.GetRecordByIdType <em>Get Record By Id Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Record By Id Type</em>'.
     * @see net.opengis.cat.csw20.GetRecordByIdType
     * @generated
     */
    EClass getGetRecordByIdType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.GetRecordByIdType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Id</em>'.
     * @see net.opengis.cat.csw20.GetRecordByIdType#getId()
     * @see #getGetRecordByIdType()
     * @generated
     */
    EAttribute getGetRecordByIdType_Id();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.GetRecordByIdType#getElementSetName <em>Element Set Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Element Set Name</em>'.
     * @see net.opengis.cat.csw20.GetRecordByIdType#getElementSetName()
     * @see #getGetRecordByIdType()
     * @generated
     */
    EReference getGetRecordByIdType_ElementSetName();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordByIdType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.cat.csw20.GetRecordByIdType#getOutputFormat()
     * @see #getGetRecordByIdType()
     * @generated
     */
    EAttribute getGetRecordByIdType_OutputFormat();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordByIdType#getOutputSchema <em>Output Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Schema</em>'.
     * @see net.opengis.cat.csw20.GetRecordByIdType#getOutputSchema()
     * @see #getGetRecordByIdType()
     * @generated
     */
    EAttribute getGetRecordByIdType_OutputSchema();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.GetRecordsResponseType <em>Get Records Response Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Records Response Type</em>'.
     * @see net.opengis.cat.csw20.GetRecordsResponseType
     * @generated
     */
    EClass getGetRecordsResponseType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsResponseType#getRequestId <em>Request Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Id</em>'.
     * @see net.opengis.cat.csw20.GetRecordsResponseType#getRequestId()
     * @see #getGetRecordsResponseType()
     * @generated
     */
    EAttribute getGetRecordsResponseType_RequestId();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.GetRecordsResponseType#getSearchStatus <em>Search Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Search Status</em>'.
     * @see net.opengis.cat.csw20.GetRecordsResponseType#getSearchStatus()
     * @see #getGetRecordsResponseType()
     * @generated
     */
    EReference getGetRecordsResponseType_SearchStatus();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.GetRecordsResponseType#getSearchResults <em>Search Results</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Search Results</em>'.
     * @see net.opengis.cat.csw20.GetRecordsResponseType#getSearchResults()
     * @see #getGetRecordsResponseType()
     * @generated
     */
    EReference getGetRecordsResponseType_SearchResults();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsResponseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.cat.csw20.GetRecordsResponseType#getVersion()
     * @see #getGetRecordsResponseType()
     * @generated
     */
    EAttribute getGetRecordsResponseType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.GetRecordsType <em>Get Records Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Records Type</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType
     * @generated
     */
    EClass getGetRecordsType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.GetRecordsType#getDistributedSearch <em>Distributed Search</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Distributed Search</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getDistributedSearch()
     * @see #getGetRecordsType()
     * @generated
     */
    EReference getGetRecordsType_DistributedSearch();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsType#getResponseHandler <em>Response Handler</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Response Handler</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getResponseHandler()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_ResponseHandler();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.GetRecordsType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getAny()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_Any();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsType#getMaxRecords <em>Max Records</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max Records</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getMaxRecords()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_MaxRecords();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getOutputFormat()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_OutputFormat();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsType#getOutputSchema <em>Output Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Schema</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getOutputSchema()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_OutputSchema();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsType#getRequestId <em>Request Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Id</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getRequestId()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_RequestId();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsType#getResultType <em>Result Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Result Type</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getResultType()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_ResultType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsType#getStartPosition <em>Start Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Position</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getStartPosition()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_StartPosition();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.GetRecordsType#getQuery <em>Query</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Query</em>'.
     * @see net.opengis.cat.csw20.GetRecordsType#getQuery()
     * @see #getGetRecordsType()
     * @generated
     */
    EAttribute getGetRecordsType_Query();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.HarvestResponseType <em>Harvest Response Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Harvest Response Type</em>'.
     * @see net.opengis.cat.csw20.HarvestResponseType
     * @generated
     */
    EClass getHarvestResponseType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.HarvestResponseType#getAcknowledgement <em>Acknowledgement</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Acknowledgement</em>'.
     * @see net.opengis.cat.csw20.HarvestResponseType#getAcknowledgement()
     * @see #getHarvestResponseType()
     * @generated
     */
    EReference getHarvestResponseType_Acknowledgement();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.HarvestResponseType#getTransactionResponse <em>Transaction Response</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Transaction Response</em>'.
     * @see net.opengis.cat.csw20.HarvestResponseType#getTransactionResponse()
     * @see #getHarvestResponseType()
     * @generated
     */
    EReference getHarvestResponseType_TransactionResponse();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.HarvestType <em>Harvest Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Harvest Type</em>'.
     * @see net.opengis.cat.csw20.HarvestType
     * @generated
     */
    EClass getHarvestType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.HarvestType#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Source</em>'.
     * @see net.opengis.cat.csw20.HarvestType#getSource()
     * @see #getHarvestType()
     * @generated
     */
    EAttribute getHarvestType_Source();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.HarvestType#getResourceType <em>Resource Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Resource Type</em>'.
     * @see net.opengis.cat.csw20.HarvestType#getResourceType()
     * @see #getHarvestType()
     * @generated
     */
    EAttribute getHarvestType_ResourceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.HarvestType#getResourceFormat <em>Resource Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Resource Format</em>'.
     * @see net.opengis.cat.csw20.HarvestType#getResourceFormat()
     * @see #getHarvestType()
     * @generated
     */
    EAttribute getHarvestType_ResourceFormat();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.HarvestType#getHarvestInterval <em>Harvest Interval</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Harvest Interval</em>'.
     * @see net.opengis.cat.csw20.HarvestType#getHarvestInterval()
     * @see #getHarvestType()
     * @generated
     */
    EAttribute getHarvestType_HarvestInterval();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.HarvestType#getResponseHandler <em>Response Handler</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Response Handler</em>'.
     * @see net.opengis.cat.csw20.HarvestType#getResponseHandler()
     * @see #getHarvestType()
     * @generated
     */
    EAttribute getHarvestType_ResponseHandler();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.InsertResultType <em>Insert Result Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Insert Result Type</em>'.
     * @see net.opengis.cat.csw20.InsertResultType
     * @generated
     */
    EClass getInsertResultType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.InsertResultType#getBriefRecord <em>Brief Record</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Brief Record</em>'.
     * @see net.opengis.cat.csw20.InsertResultType#getBriefRecord()
     * @see #getInsertResultType()
     * @generated
     */
    EReference getInsertResultType_BriefRecord();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.InsertResultType#getHandleRef <em>Handle Ref</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Handle Ref</em>'.
     * @see net.opengis.cat.csw20.InsertResultType#getHandleRef()
     * @see #getInsertResultType()
     * @generated
     */
    EAttribute getInsertResultType_HandleRef();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.InsertType <em>Insert Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Insert Type</em>'.
     * @see net.opengis.cat.csw20.InsertType
     * @generated
     */
    EClass getInsertType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.InsertType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.cat.csw20.InsertType#getAny()
     * @see #getInsertType()
     * @generated
     */
    EAttribute getInsertType_Any();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.InsertType#getHandle <em>Handle</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Handle</em>'.
     * @see net.opengis.cat.csw20.InsertType#getHandle()
     * @see #getInsertType()
     * @generated
     */
    EAttribute getInsertType_Handle();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.InsertType#getTypeName <em>Type Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type Name</em>'.
     * @see net.opengis.cat.csw20.InsertType#getTypeName()
     * @see #getInsertType()
     * @generated
     */
    EAttribute getInsertType_TypeName();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.ListOfValuesType <em>List Of Values Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>List Of Values Type</em>'.
     * @see net.opengis.cat.csw20.ListOfValuesType
     * @generated
     */
    EClass getListOfValuesType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.ListOfValuesType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Value</em>'.
     * @see net.opengis.cat.csw20.ListOfValuesType#getValue()
     * @see #getListOfValuesType()
     * @generated
     */
    EReference getListOfValuesType_Value();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.QueryConstraintType <em>Query Constraint Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Query Constraint Type</em>'.
     * @see net.opengis.cat.csw20.QueryConstraintType
     * @generated
     */
    EClass getQueryConstraintType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.QueryConstraintType#getFilter <em>Filter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Filter</em>'.
     * @see net.opengis.cat.csw20.QueryConstraintType#getFilter()
     * @see #getQueryConstraintType()
     * @generated
     */
    EReference getQueryConstraintType_Filter();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.QueryConstraintType#getCqlText <em>Cql Text</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Cql Text</em>'.
     * @see net.opengis.cat.csw20.QueryConstraintType#getCqlText()
     * @see #getQueryConstraintType()
     * @generated
     */
    EAttribute getQueryConstraintType_CqlText();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.QueryConstraintType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.cat.csw20.QueryConstraintType#getVersion()
     * @see #getQueryConstraintType()
     * @generated
     */
    EAttribute getQueryConstraintType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.QueryType <em>Query Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Query Type</em>'.
     * @see net.opengis.cat.csw20.QueryType
     * @generated
     */
    EClass getQueryType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.QueryType#getElementSetName <em>Element Set Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Element Set Name</em>'.
     * @see net.opengis.cat.csw20.QueryType#getElementSetName()
     * @see #getQueryType()
     * @generated
     */
    EReference getQueryType_ElementSetName();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.QueryType#getElementName <em>Element Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Element Name</em>'.
     * @see net.opengis.cat.csw20.QueryType#getElementName()
     * @see #getQueryType()
     * @generated
     */
    EAttribute getQueryType_ElementName();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.QueryType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Constraint</em>'.
     * @see net.opengis.cat.csw20.QueryType#getConstraint()
     * @see #getQueryType()
     * @generated
     */
    EReference getQueryType_Constraint();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.QueryType#getSortBy <em>Sort By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sort By</em>'.
     * @see net.opengis.cat.csw20.QueryType#getSortBy()
     * @see #getQueryType()
     * @generated
     */
    EAttribute getQueryType_SortBy();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.QueryType#getTypeNames <em>Type Names</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type Names</em>'.
     * @see net.opengis.cat.csw20.QueryType#getTypeNames()
     * @see #getQueryType()
     * @generated
     */
    EAttribute getQueryType_TypeNames();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.RangeOfValuesType <em>Range Of Values Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Range Of Values Type</em>'.
     * @see net.opengis.cat.csw20.RangeOfValuesType
     * @generated
     */
    EClass getRangeOfValuesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.RangeOfValuesType#getMinValue <em>Min Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Min Value</em>'.
     * @see net.opengis.cat.csw20.RangeOfValuesType#getMinValue()
     * @see #getRangeOfValuesType()
     * @generated
     */
    EReference getRangeOfValuesType_MinValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.RangeOfValuesType#getMaxValue <em>Max Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Max Value</em>'.
     * @see net.opengis.cat.csw20.RangeOfValuesType#getMaxValue()
     * @see #getRangeOfValuesType()
     * @generated
     */
    EReference getRangeOfValuesType_MaxValue();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.RecordPropertyType <em>Record Property Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Record Property Type</em>'.
     * @see net.opengis.cat.csw20.RecordPropertyType
     * @generated
     */
    EClass getRecordPropertyType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.RecordPropertyType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.cat.csw20.RecordPropertyType#getName()
     * @see #getRecordPropertyType()
     * @generated
     */
    EAttribute getRecordPropertyType_Name();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.RecordPropertyType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see net.opengis.cat.csw20.RecordPropertyType#getValue()
     * @see #getRecordPropertyType()
     * @generated
     */
    EReference getRecordPropertyType_Value();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.RequestBaseType <em>Request Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Request Base Type</em>'.
     * @see net.opengis.cat.csw20.RequestBaseType
     * @generated
     */
    EClass getRequestBaseType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.RequestBaseType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.cat.csw20.RequestBaseType#getService()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.RequestBaseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.cat.csw20.RequestBaseType#getVersion()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Version();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.RequestBaseType#getBaseUrl <em>Base Url</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Base Url</em>'.
     * @see net.opengis.cat.csw20.RequestBaseType#getBaseUrl()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_BaseUrl();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.RequestBaseType#getExtendedProperties <em>Extended Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extended Properties</em>'.
     * @see net.opengis.cat.csw20.RequestBaseType#getExtendedProperties()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_ExtendedProperties();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.RequestStatusType <em>Request Status Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Request Status Type</em>'.
     * @see net.opengis.cat.csw20.RequestStatusType
     * @generated
     */
    EClass getRequestStatusType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.RequestStatusType#getTimestamp <em>Timestamp</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Timestamp</em>'.
     * @see net.opengis.cat.csw20.RequestStatusType#getTimestamp()
     * @see #getRequestStatusType()
     * @generated
     */
    EAttribute getRequestStatusType_Timestamp();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.SchemaComponentType <em>Schema Component Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Schema Component Type</em>'.
     * @see net.opengis.cat.csw20.SchemaComponentType
     * @generated
     */
    EClass getSchemaComponentType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.SchemaComponentType#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.cat.csw20.SchemaComponentType#getMixed()
     * @see #getSchemaComponentType()
     * @generated
     */
    EAttribute getSchemaComponentType_Mixed();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.SchemaComponentType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.cat.csw20.SchemaComponentType#getAny()
     * @see #getSchemaComponentType()
     * @generated
     */
    EAttribute getSchemaComponentType_Any();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SchemaComponentType#getParentSchema <em>Parent Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Parent Schema</em>'.
     * @see net.opengis.cat.csw20.SchemaComponentType#getParentSchema()
     * @see #getSchemaComponentType()
     * @generated
     */
    EAttribute getSchemaComponentType_ParentSchema();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SchemaComponentType#getSchemaLanguage <em>Schema Language</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Schema Language</em>'.
     * @see net.opengis.cat.csw20.SchemaComponentType#getSchemaLanguage()
     * @see #getSchemaComponentType()
     * @generated
     */
    EAttribute getSchemaComponentType_SchemaLanguage();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SchemaComponentType#getTargetNamespace <em>Target Namespace</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Target Namespace</em>'.
     * @see net.opengis.cat.csw20.SchemaComponentType#getTargetNamespace()
     * @see #getSchemaComponentType()
     * @generated
     */
    EAttribute getSchemaComponentType_TargetNamespace();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.SearchResultsType <em>Search Results Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Search Results Type</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType
     * @generated
     */
    EClass getSearchResultsType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.SearchResultsType#getAbstractRecordGroup <em>Abstract Record Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Abstract Record Group</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getAbstractRecordGroup()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_AbstractRecordGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SearchResultsType#getAbstractRecord <em>Abstract Record</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Abstract Record</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getAbstractRecord()
     * @see #getSearchResultsType()
     * @generated
     */
    EReference getSearchResultsType_AbstractRecord();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.SearchResultsType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getAny()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_Any();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SearchResultsType#getElementSet <em>Element Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Element Set</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getElementSet()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_ElementSet();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SearchResultsType#getExpires <em>Expires</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Expires</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getExpires()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_Expires();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SearchResultsType#getNextRecord <em>Next Record</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Next Record</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getNextRecord()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_NextRecord();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SearchResultsType#getNumberOfRecordsMatched <em>Number Of Records Matched</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Number Of Records Matched</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getNumberOfRecordsMatched()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_NumberOfRecordsMatched();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SearchResultsType#getNumberOfRecordsReturned <em>Number Of Records Returned</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Number Of Records Returned</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getNumberOfRecordsReturned()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_NumberOfRecordsReturned();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SearchResultsType#getRecordSchema <em>Record Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Record Schema</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getRecordSchema()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_RecordSchema();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SearchResultsType#getResultSetId <em>Result Set Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Result Set Id</em>'.
     * @see net.opengis.cat.csw20.SearchResultsType#getResultSetId()
     * @see #getSearchResultsType()
     * @generated
     */
    EAttribute getSearchResultsType_ResultSetId();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.TransactionResponseType <em>Transaction Response Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Transaction Response Type</em>'.
     * @see net.opengis.cat.csw20.TransactionResponseType
     * @generated
     */
    EClass getTransactionResponseType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.TransactionResponseType#getTransactionSummary <em>Transaction Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Transaction Summary</em>'.
     * @see net.opengis.cat.csw20.TransactionResponseType#getTransactionSummary()
     * @see #getTransactionResponseType()
     * @generated
     */
    EReference getTransactionResponseType_TransactionSummary();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.TransactionResponseType#getInsertResult <em>Insert Result</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Insert Result</em>'.
     * @see net.opengis.cat.csw20.TransactionResponseType#getInsertResult()
     * @see #getTransactionResponseType()
     * @generated
     */
    EReference getTransactionResponseType_InsertResult();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.TransactionResponseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.cat.csw20.TransactionResponseType#getVersion()
     * @see #getTransactionResponseType()
     * @generated
     */
    EAttribute getTransactionResponseType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.TransactionSummaryType <em>Transaction Summary Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Transaction Summary Type</em>'.
     * @see net.opengis.cat.csw20.TransactionSummaryType
     * @generated
     */
    EClass getTransactionSummaryType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalInserted <em>Total Inserted</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Total Inserted</em>'.
     * @see net.opengis.cat.csw20.TransactionSummaryType#getTotalInserted()
     * @see #getTransactionSummaryType()
     * @generated
     */
    EAttribute getTransactionSummaryType_TotalInserted();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalUpdated <em>Total Updated</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Total Updated</em>'.
     * @see net.opengis.cat.csw20.TransactionSummaryType#getTotalUpdated()
     * @see #getTransactionSummaryType()
     * @generated
     */
    EAttribute getTransactionSummaryType_TotalUpdated();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.TransactionSummaryType#getTotalDeleted <em>Total Deleted</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Total Deleted</em>'.
     * @see net.opengis.cat.csw20.TransactionSummaryType#getTotalDeleted()
     * @see #getTransactionSummaryType()
     * @generated
     */
    EAttribute getTransactionSummaryType_TotalDeleted();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.TransactionSummaryType#getRequestId <em>Request Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Id</em>'.
     * @see net.opengis.cat.csw20.TransactionSummaryType#getRequestId()
     * @see #getTransactionSummaryType()
     * @generated
     */
    EAttribute getTransactionSummaryType_RequestId();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.TransactionType <em>Transaction Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Transaction Type</em>'.
     * @see net.opengis.cat.csw20.TransactionType
     * @generated
     */
    EClass getTransactionType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.TransactionType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see net.opengis.cat.csw20.TransactionType#getGroup()
     * @see #getTransactionType()
     * @generated
     */
    EAttribute getTransactionType_Group();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.TransactionType#getInsert <em>Insert</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Insert</em>'.
     * @see net.opengis.cat.csw20.TransactionType#getInsert()
     * @see #getTransactionType()
     * @generated
     */
    EReference getTransactionType_Insert();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.TransactionType#getUpdate <em>Update</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Update</em>'.
     * @see net.opengis.cat.csw20.TransactionType#getUpdate()
     * @see #getTransactionType()
     * @generated
     */
    EReference getTransactionType_Update();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.TransactionType#getDelete <em>Delete</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Delete</em>'.
     * @see net.opengis.cat.csw20.TransactionType#getDelete()
     * @see #getTransactionType()
     * @generated
     */
    EReference getTransactionType_Delete();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.TransactionType#getRequestId <em>Request Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Id</em>'.
     * @see net.opengis.cat.csw20.TransactionType#getRequestId()
     * @see #getTransactionType()
     * @generated
     */
    EAttribute getTransactionType_RequestId();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.TransactionType#isVerboseResponse <em>Verbose Response</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Verbose Response</em>'.
     * @see net.opengis.cat.csw20.TransactionType#isVerboseResponse()
     * @see #getTransactionType()
     * @generated
     */
    EAttribute getTransactionType_VerboseResponse();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.UpdateType <em>Update Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Update Type</em>'.
     * @see net.opengis.cat.csw20.UpdateType
     * @generated
     */
    EClass getUpdateType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.cat.csw20.UpdateType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.cat.csw20.UpdateType#getAny()
     * @see #getUpdateType()
     * @generated
     */
    EAttribute getUpdateType_Any();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.UpdateType#getRecordProperty <em>Record Property</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Record Property</em>'.
     * @see net.opengis.cat.csw20.UpdateType#getRecordProperty()
     * @see #getUpdateType()
     * @generated
     */
    EReference getUpdateType_RecordProperty();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.cat.csw20.UpdateType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Constraint</em>'.
     * @see net.opengis.cat.csw20.UpdateType#getConstraint()
     * @see #getUpdateType()
     * @generated
     */
    EReference getUpdateType_Constraint();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.UpdateType#getHandle <em>Handle</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Handle</em>'.
     * @see net.opengis.cat.csw20.UpdateType#getHandle()
     * @see #getUpdateType()
     * @generated
     */
    EAttribute getUpdateType_Handle();

    /**
     * Returns the meta object for class '{@link java.lang.String <em>String</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>String</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     * @generated
     */
    EClass getString();

    /**
     * Returns the meta object for class '{@link org.opengis.filter.capability.FilterCapabilities <em>Filter Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Filter Capabilities</em>'.
     * @see org.opengis.filter.capability.FilterCapabilities
     * @model instanceClass="org.opengis.filter.capability.FilterCapabilities"
     * @generated
     */
    EClass getFilterCapabilities();

    /**
     * Returns the meta object for class '{@link org.opengis.filter.Filter <em>Filter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Filter</em>'.
     * @see org.opengis.filter.Filter
     * @model instanceClass="org.opengis.filter.Filter"
     * @generated
     */
    EClass getFilter();

    /**
     * Returns the meta object for class '{@link org.opengis.filter.sort.SortBy <em>Sort By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sort By</em>'.
     * @see org.opengis.filter.sort.SortBy
     * @model instanceClass="org.opengis.filter.sort.SortBy"
     * @generated
     */
    EClass getSortBy();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.DCMIRecordType <em>DCMI Record Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>DCMI Record Type</em>'.
     * @see net.opengis.cat.csw20.DCMIRecordType
     * @generated
     */
    EClass getDCMIRecordType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.DCMIRecordType#getDCElement <em>DC Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>DC Element</em>'.
     * @see net.opengis.cat.csw20.DCMIRecordType#getDCElement()
     * @see #getDCMIRecordType()
     * @generated
     */
    EReference getDCMIRecordType_DCElement();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.RecordType <em>Record Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Record Type</em>'.
     * @see net.opengis.cat.csw20.RecordType
     * @generated
     */
    EClass getRecordType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.RecordType#getAnyText <em>Any Text</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Any Text</em>'.
     * @see net.opengis.cat.csw20.RecordType#getAnyText()
     * @see #getRecordType()
     * @generated
     */
    EReference getRecordType_AnyText();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.RecordType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bounding Box</em>'.
     * @see net.opengis.cat.csw20.RecordType#getBoundingBox()
     * @see #getRecordType()
     * @generated
     */
    EReference getRecordType_BoundingBox();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.SimpleLiteral <em>Simple Literal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Simple Literal</em>'.
     * @see net.opengis.cat.csw20.SimpleLiteral
     * @generated
     */
    EClass getSimpleLiteral();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SimpleLiteral#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.cat.csw20.SimpleLiteral#getValue()
     * @see #getSimpleLiteral()
     * @generated
     */
    EAttribute getSimpleLiteral_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.cat.csw20.SimpleLiteral#getScheme <em>Scheme</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Scheme</em>'.
     * @see net.opengis.cat.csw20.SimpleLiteral#getScheme()
     * @see #getSimpleLiteral()
     * @generated
     */
    EAttribute getSimpleLiteral_Scheme();

    /**
     * Returns the meta object for the reference '{@link net.opengis.cat.csw20.SimpleLiteral#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Name</em>'.
     * @see net.opengis.cat.csw20.SimpleLiteral#getName()
     * @see #getSimpleLiteral()
     * @generated
     */
    EReference getSimpleLiteral_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.cat.csw20.SummaryRecordType <em>Summary Record Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Summary Record Type</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType
     * @generated
     */
    EClass getSummaryRecordType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Identifier</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getIdentifier()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Identifier();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Title</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getTitle()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Title();

    /**
     * Returns the meta object for the reference '{@link net.opengis.cat.csw20.SummaryRecordType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Type</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getType()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Type();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getSubject <em>Subject</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Subject</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getSubject()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Subject();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Format</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getFormat()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Format();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getRelation <em>Relation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Relation</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getRelation()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Relation();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getModified <em>Modified</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Modified</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getModified()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Modified();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Abstract</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getAbstract()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Abstract();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getSpatial <em>Spatial</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Spatial</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getSpatial()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_Spatial();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.cat.csw20.SummaryRecordType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bounding Box</em>'.
     * @see net.opengis.cat.csw20.SummaryRecordType#getBoundingBox()
     * @see #getSummaryRecordType()
     * @generated
     */
    EReference getSummaryRecordType_BoundingBox();

    /**
     * Returns the meta object for enum '{@link net.opengis.cat.csw20.ElementSetType <em>Element Set Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Element Set Type</em>'.
     * @see net.opengis.cat.csw20.ElementSetType
     * @generated
     */
    EEnum getElementSetType();

    /**
     * Returns the meta object for enum '{@link net.opengis.cat.csw20.ResultType <em>Result Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Result Type</em>'.
     * @see net.opengis.cat.csw20.ResultType
     * @generated
     */
    EEnum getResultType();

    /**
     * Returns the meta object for data type '{@link java.util.List <em>Type Name List Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Type Name List Type</em>'.
     * @see java.util.List
     * @model instanceClass="java.util.List<javax.xml.namespace.QName>"
     * @generated
     */
    EDataType getTypeNameListType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Service Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Service Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     * @generated
     */
    EDataType getServiceType();

    /**
     * Returns the meta object for data type '{@link java.util.List <em>Type Name List Type 1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Type Name List Type 1</em>'.
     * @see java.util.List
     * @model instanceClass="java.util.List<javax.xml.namespace.QName>"
     * @generated
     */
    EDataType getTypeNameListType_1();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Service Type 1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Service Type 1</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     * @generated
     */
    EDataType getServiceType_1();

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
     * Returns the meta object for data type '{@link java.util.Calendar <em>Calendar</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Calendar</em>'.
     * @see java.util.Calendar
     * @model instanceClass="java.util.Calendar"
     * @generated
     */
    EDataType getCalendar();

    /**
     * Returns the meta object for data type '{@link java.util.Set <em>Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Set</em>'.
     * @see java.util.Set
     * @model instanceClass="java.util.Set" typeParameters="T"
     * @generated
     */
    EDataType getSet();

    /**
     * Returns the meta object for data type '{@link java.net.URI <em>URI</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>URI</em>'.
     * @see java.net.URI
     * @model instanceClass="java.net.URI"
     * @generated
     */
    EDataType getURI();

    /**
     * Returns the meta object for data type '{@link javax.xml.namespace.QName <em>QName</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>QName</em>'.
     * @see javax.xml.namespace.QName
     * @model instanceClass="javax.xml.namespace.QName"
     * @generated
     */
    EDataType getQName();

    /**
     * Returns the meta object for data type '{@link javax.xml.datatype.Duration <em>Duration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Duration</em>'.
     * @see javax.xml.datatype.Duration
     * @model instanceClass="javax.xml.datatype.Duration"
     * @generated
     */
    EDataType getDuration();

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
     * Returns the meta object for data type '<em>Sort By Array</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Sort By Array</em>'.
     * @model instanceClass="org.opengis.filter.sort.SortBy[]"
     * @generated
     */
    EDataType getSortByArray();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    Csw20Factory getCsw20Factory();

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
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.AbstractQueryTypeImpl <em>Abstract Query Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.AbstractQueryTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getAbstractQueryType()
         * @generated
         */
        EClass ABSTRACT_QUERY_TYPE = eINSTANCE.getAbstractQueryType();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.AbstractRecordTypeImpl <em>Abstract Record Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.AbstractRecordTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getAbstractRecordType()
         * @generated
         */
        EClass ABSTRACT_RECORD_TYPE = eINSTANCE.getAbstractRecordType();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.AcknowledgementTypeImpl <em>Acknowledgement Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.AcknowledgementTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getAcknowledgementType()
         * @generated
         */
        EClass ACKNOWLEDGEMENT_TYPE = eINSTANCE.getAcknowledgementType();

        /**
         * The meta object literal for the '<em><b>Echoed Request</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST = eINSTANCE.getAcknowledgementType_EchoedRequest();

        /**
         * The meta object literal for the '<em><b>Request Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ACKNOWLEDGEMENT_TYPE__REQUEST_ID = eINSTANCE.getAcknowledgementType_RequestId();

        /**
         * The meta object literal for the '<em><b>Time Stamp</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ACKNOWLEDGEMENT_TYPE__TIME_STAMP = eINSTANCE.getAcknowledgementType_TimeStamp();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.BriefRecordTypeImpl <em>Brief Record Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.BriefRecordTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getBriefRecordType()
         * @generated
         */
        EClass BRIEF_RECORD_TYPE = eINSTANCE.getBriefRecordType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BRIEF_RECORD_TYPE__IDENTIFIER = eINSTANCE.getBriefRecordType_Identifier();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BRIEF_RECORD_TYPE__TITLE = eINSTANCE.getBriefRecordType_Title();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BRIEF_RECORD_TYPE__TYPE = eINSTANCE.getBriefRecordType_Type();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BRIEF_RECORD_TYPE__BOUNDING_BOX = eINSTANCE.getBriefRecordType_BoundingBox();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.CapabilitiesTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getCapabilitiesType()
         * @generated
         */
        EClass CAPABILITIES_TYPE = eINSTANCE.getCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Filter Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_TYPE__FILTER_CAPABILITIES = eINSTANCE.getCapabilitiesType_FilterCapabilities();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.ConceptualSchemeTypeImpl <em>Conceptual Scheme Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.ConceptualSchemeTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getConceptualSchemeType()
         * @generated
         */
        EClass CONCEPTUAL_SCHEME_TYPE = eINSTANCE.getConceptualSchemeType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONCEPTUAL_SCHEME_TYPE__NAME = eINSTANCE.getConceptualSchemeType_Name();

        /**
         * The meta object literal for the '<em><b>Document</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONCEPTUAL_SCHEME_TYPE__DOCUMENT = eINSTANCE.getConceptualSchemeType_Document();

        /**
         * The meta object literal for the '<em><b>Authority</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONCEPTUAL_SCHEME_TYPE__AUTHORITY = eINSTANCE.getConceptualSchemeType_Authority();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.DeleteTypeImpl <em>Delete Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.DeleteTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDeleteType()
         * @generated
         */
        EClass DELETE_TYPE = eINSTANCE.getDeleteType();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DELETE_TYPE__CONSTRAINT = eINSTANCE.getDeleteType_Constraint();

        /**
         * The meta object literal for the '<em><b>Handle</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DELETE_TYPE__HANDLE = eINSTANCE.getDeleteType_Handle();

        /**
         * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DELETE_TYPE__TYPE_NAME = eINSTANCE.getDeleteType_TypeName();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.DescribeRecordResponseTypeImpl <em>Describe Record Response Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.DescribeRecordResponseTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDescribeRecordResponseType()
         * @generated
         */
        EClass DESCRIBE_RECORD_RESPONSE_TYPE = eINSTANCE.getDescribeRecordResponseType();

        /**
         * The meta object literal for the '<em><b>Schema Component</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT = eINSTANCE.getDescribeRecordResponseType_SchemaComponent();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.DescribeRecordTypeImpl <em>Describe Record Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.DescribeRecordTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDescribeRecordType()
         * @generated
         */
        EClass DESCRIBE_RECORD_TYPE = eINSTANCE.getDescribeRecordType();

        /**
         * The meta object literal for the '<em><b>Type Name</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DESCRIBE_RECORD_TYPE__TYPE_NAME = eINSTANCE.getDescribeRecordType_TypeName();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT = eINSTANCE.getDescribeRecordType_OutputFormat();

        /**
         * The meta object literal for the '<em><b>Schema Language</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE = eINSTANCE.getDescribeRecordType_SchemaLanguage();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.DistributedSearchTypeImpl <em>Distributed Search Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.DistributedSearchTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDistributedSearchType()
         * @generated
         */
        EClass DISTRIBUTED_SEARCH_TYPE = eINSTANCE.getDistributedSearchType();

        /**
         * The meta object literal for the '<em><b>Hop Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DISTRIBUTED_SEARCH_TYPE__HOP_COUNT = eINSTANCE.getDistributedSearchType_HopCount();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.DomainValuesTypeImpl <em>Domain Values Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.DomainValuesTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDomainValuesType()
         * @generated
         */
        EClass DOMAIN_VALUES_TYPE = eINSTANCE.getDomainValuesType();

        /**
         * The meta object literal for the '<em><b>Property Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_VALUES_TYPE__PROPERTY_NAME = eINSTANCE.getDomainValuesType_PropertyName();

        /**
         * The meta object literal for the '<em><b>Parameter Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_VALUES_TYPE__PARAMETER_NAME = eINSTANCE.getDomainValuesType_ParameterName();

        /**
         * The meta object literal for the '<em><b>List Of Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOMAIN_VALUES_TYPE__LIST_OF_VALUES = eINSTANCE.getDomainValuesType_ListOfValues();

        /**
         * The meta object literal for the '<em><b>Conceptual Scheme</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME = eINSTANCE.getDomainValuesType_ConceptualScheme();

        /**
         * The meta object literal for the '<em><b>Range Of Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOMAIN_VALUES_TYPE__RANGE_OF_VALUES = eINSTANCE.getDomainValuesType_RangeOfValues();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_VALUES_TYPE__TYPE = eINSTANCE.getDomainValuesType_Type();

        /**
         * The meta object literal for the '<em><b>Uom</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_VALUES_TYPE__UOM = eINSTANCE.getDomainValuesType_Uom();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.EchoedRequestTypeImpl <em>Echoed Request Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.EchoedRequestTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getEchoedRequestType()
         * @generated
         */
        EClass ECHOED_REQUEST_TYPE = eINSTANCE.getEchoedRequestType();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ECHOED_REQUEST_TYPE__ANY = eINSTANCE.getEchoedRequestType_Any();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.ElementSetNameTypeImpl <em>Element Set Name Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.ElementSetNameTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getElementSetNameType()
         * @generated
         */
        EClass ELEMENT_SET_NAME_TYPE = eINSTANCE.getElementSetNameType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ELEMENT_SET_NAME_TYPE__VALUE = eINSTANCE.getElementSetNameType_Value();

        /**
         * The meta object literal for the '<em><b>Type Names</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ELEMENT_SET_NAME_TYPE__TYPE_NAMES = eINSTANCE.getElementSetNameType_TypeNames();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.EmptyTypeImpl <em>Empty Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.EmptyTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getEmptyType()
         * @generated
         */
        EClass EMPTY_TYPE = eINSTANCE.getEmptyType();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.GetCapabilitiesTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetCapabilitiesType()
         * @generated
         */
        EClass GET_CAPABILITIES_TYPE = eINSTANCE.getGetCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_CAPABILITIES_TYPE__SERVICE = eINSTANCE.getGetCapabilitiesType_Service();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.GetDomainResponseTypeImpl <em>Get Domain Response Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.GetDomainResponseTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetDomainResponseType()
         * @generated
         */
        EClass GET_DOMAIN_RESPONSE_TYPE = eINSTANCE.getGetDomainResponseType();

        /**
         * The meta object literal for the '<em><b>Domain Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES = eINSTANCE.getGetDomainResponseType_DomainValues();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.GetDomainTypeImpl <em>Get Domain Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.GetDomainTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetDomainType()
         * @generated
         */
        EClass GET_DOMAIN_TYPE = eINSTANCE.getGetDomainType();

        /**
         * The meta object literal for the '<em><b>Property Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_DOMAIN_TYPE__PROPERTY_NAME = eINSTANCE.getGetDomainType_PropertyName();

        /**
         * The meta object literal for the '<em><b>Parameter Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_DOMAIN_TYPE__PARAMETER_NAME = eINSTANCE.getGetDomainType_ParameterName();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.GetRecordByIdTypeImpl <em>Get Record By Id Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.GetRecordByIdTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetRecordByIdType()
         * @generated
         */
        EClass GET_RECORD_BY_ID_TYPE = eINSTANCE.getGetRecordByIdType();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORD_BY_ID_TYPE__ID = eINSTANCE.getGetRecordByIdType_Id();

        /**
         * The meta object literal for the '<em><b>Element Set Name</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME = eINSTANCE.getGetRecordByIdType_ElementSetName();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT = eINSTANCE.getGetRecordByIdType_OutputFormat();

        /**
         * The meta object literal for the '<em><b>Output Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORD_BY_ID_TYPE__OUTPUT_SCHEMA = eINSTANCE.getGetRecordByIdType_OutputSchema();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.GetRecordsResponseTypeImpl <em>Get Records Response Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.GetRecordsResponseTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetRecordsResponseType()
         * @generated
         */
        EClass GET_RECORDS_RESPONSE_TYPE = eINSTANCE.getGetRecordsResponseType();

        /**
         * The meta object literal for the '<em><b>Request Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_RESPONSE_TYPE__REQUEST_ID = eINSTANCE.getGetRecordsResponseType_RequestId();

        /**
         * The meta object literal for the '<em><b>Search Status</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS = eINSTANCE.getGetRecordsResponseType_SearchStatus();

        /**
         * The meta object literal for the '<em><b>Search Results</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS = eINSTANCE.getGetRecordsResponseType_SearchResults();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_RESPONSE_TYPE__VERSION = eINSTANCE.getGetRecordsResponseType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl <em>Get Records Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.GetRecordsTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getGetRecordsType()
         * @generated
         */
        EClass GET_RECORDS_TYPE = eINSTANCE.getGetRecordsType();

        /**
         * The meta object literal for the '<em><b>Distributed Search</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_RECORDS_TYPE__DISTRIBUTED_SEARCH = eINSTANCE.getGetRecordsType_DistributedSearch();

        /**
         * The meta object literal for the '<em><b>Response Handler</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__RESPONSE_HANDLER = eINSTANCE.getGetRecordsType_ResponseHandler();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__ANY = eINSTANCE.getGetRecordsType_Any();

        /**
         * The meta object literal for the '<em><b>Max Records</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__MAX_RECORDS = eINSTANCE.getGetRecordsType_MaxRecords();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__OUTPUT_FORMAT = eINSTANCE.getGetRecordsType_OutputFormat();

        /**
         * The meta object literal for the '<em><b>Output Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__OUTPUT_SCHEMA = eINSTANCE.getGetRecordsType_OutputSchema();

        /**
         * The meta object literal for the '<em><b>Request Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__REQUEST_ID = eINSTANCE.getGetRecordsType_RequestId();

        /**
         * The meta object literal for the '<em><b>Result Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__RESULT_TYPE = eINSTANCE.getGetRecordsType_ResultType();

        /**
         * The meta object literal for the '<em><b>Start Position</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__START_POSITION = eINSTANCE.getGetRecordsType_StartPosition();

        /**
         * The meta object literal for the '<em><b>Query</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_RECORDS_TYPE__QUERY = eINSTANCE.getGetRecordsType_Query();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.HarvestResponseTypeImpl <em>Harvest Response Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.HarvestResponseTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getHarvestResponseType()
         * @generated
         */
        EClass HARVEST_RESPONSE_TYPE = eINSTANCE.getHarvestResponseType();

        /**
         * The meta object literal for the '<em><b>Acknowledgement</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT = eINSTANCE.getHarvestResponseType_Acknowledgement();

        /**
         * The meta object literal for the '<em><b>Transaction Response</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE = eINSTANCE.getHarvestResponseType_TransactionResponse();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.HarvestTypeImpl <em>Harvest Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.HarvestTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getHarvestType()
         * @generated
         */
        EClass HARVEST_TYPE = eINSTANCE.getHarvestType();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HARVEST_TYPE__SOURCE = eINSTANCE.getHarvestType_Source();

        /**
         * The meta object literal for the '<em><b>Resource Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HARVEST_TYPE__RESOURCE_TYPE = eINSTANCE.getHarvestType_ResourceType();

        /**
         * The meta object literal for the '<em><b>Resource Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HARVEST_TYPE__RESOURCE_FORMAT = eINSTANCE.getHarvestType_ResourceFormat();

        /**
         * The meta object literal for the '<em><b>Harvest Interval</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HARVEST_TYPE__HARVEST_INTERVAL = eINSTANCE.getHarvestType_HarvestInterval();

        /**
         * The meta object literal for the '<em><b>Response Handler</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HARVEST_TYPE__RESPONSE_HANDLER = eINSTANCE.getHarvestType_ResponseHandler();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.InsertResultTypeImpl <em>Insert Result Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.InsertResultTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getInsertResultType()
         * @generated
         */
        EClass INSERT_RESULT_TYPE = eINSTANCE.getInsertResultType();

        /**
         * The meta object literal for the '<em><b>Brief Record</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INSERT_RESULT_TYPE__BRIEF_RECORD = eINSTANCE.getInsertResultType_BriefRecord();

        /**
         * The meta object literal for the '<em><b>Handle Ref</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INSERT_RESULT_TYPE__HANDLE_REF = eINSTANCE.getInsertResultType_HandleRef();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.InsertTypeImpl <em>Insert Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.InsertTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getInsertType()
         * @generated
         */
        EClass INSERT_TYPE = eINSTANCE.getInsertType();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INSERT_TYPE__ANY = eINSTANCE.getInsertType_Any();

        /**
         * The meta object literal for the '<em><b>Handle</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INSERT_TYPE__HANDLE = eINSTANCE.getInsertType_Handle();

        /**
         * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INSERT_TYPE__TYPE_NAME = eINSTANCE.getInsertType_TypeName();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.ListOfValuesTypeImpl <em>List Of Values Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.ListOfValuesTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getListOfValuesType()
         * @generated
         */
        EClass LIST_OF_VALUES_TYPE = eINSTANCE.getListOfValuesType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LIST_OF_VALUES_TYPE__VALUE = eINSTANCE.getListOfValuesType_Value();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.QueryConstraintTypeImpl <em>Query Constraint Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.QueryConstraintTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getQueryConstraintType()
         * @generated
         */
        EClass QUERY_CONSTRAINT_TYPE = eINSTANCE.getQueryConstraintType();

        /**
         * The meta object literal for the '<em><b>Filter</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference QUERY_CONSTRAINT_TYPE__FILTER = eINSTANCE.getQueryConstraintType_Filter();

        /**
         * The meta object literal for the '<em><b>Cql Text</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute QUERY_CONSTRAINT_TYPE__CQL_TEXT = eINSTANCE.getQueryConstraintType_CqlText();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute QUERY_CONSTRAINT_TYPE__VERSION = eINSTANCE.getQueryConstraintType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.QueryTypeImpl <em>Query Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.QueryTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getQueryType()
         * @generated
         */
        EClass QUERY_TYPE = eINSTANCE.getQueryType();

        /**
         * The meta object literal for the '<em><b>Element Set Name</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference QUERY_TYPE__ELEMENT_SET_NAME = eINSTANCE.getQueryType_ElementSetName();

        /**
         * The meta object literal for the '<em><b>Element Name</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute QUERY_TYPE__ELEMENT_NAME = eINSTANCE.getQueryType_ElementName();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference QUERY_TYPE__CONSTRAINT = eINSTANCE.getQueryType_Constraint();

        /**
         * The meta object literal for the '<em><b>Sort By</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute QUERY_TYPE__SORT_BY = eINSTANCE.getQueryType_SortBy();

        /**
         * The meta object literal for the '<em><b>Type Names</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute QUERY_TYPE__TYPE_NAMES = eINSTANCE.getQueryType_TypeNames();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.RangeOfValuesTypeImpl <em>Range Of Values Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.RangeOfValuesTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRangeOfValuesType()
         * @generated
         */
        EClass RANGE_OF_VALUES_TYPE = eINSTANCE.getRangeOfValuesType();

        /**
         * The meta object literal for the '<em><b>Min Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_OF_VALUES_TYPE__MIN_VALUE = eINSTANCE.getRangeOfValuesType_MinValue();

        /**
         * The meta object literal for the '<em><b>Max Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_OF_VALUES_TYPE__MAX_VALUE = eINSTANCE.getRangeOfValuesType_MaxValue();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.RecordPropertyTypeImpl <em>Record Property Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.RecordPropertyTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRecordPropertyType()
         * @generated
         */
        EClass RECORD_PROPERTY_TYPE = eINSTANCE.getRecordPropertyType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RECORD_PROPERTY_TYPE__NAME = eINSTANCE.getRecordPropertyType_Name();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RECORD_PROPERTY_TYPE__VALUE = eINSTANCE.getRecordPropertyType_Value();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.RequestBaseTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRequestBaseType()
         * @generated
         */
        EClass REQUEST_BASE_TYPE = eINSTANCE.getRequestBaseType();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_BASE_TYPE__SERVICE = eINSTANCE.getRequestBaseType_Service();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_BASE_TYPE__VERSION = eINSTANCE.getRequestBaseType_Version();

        /**
         * The meta object literal for the '<em><b>Base Url</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_BASE_TYPE__BASE_URL = eINSTANCE.getRequestBaseType_BaseUrl();

        /**
         * The meta object literal for the '<em><b>Extended Properties</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_BASE_TYPE__EXTENDED_PROPERTIES = eINSTANCE.getRequestBaseType_ExtendedProperties();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.RequestStatusTypeImpl <em>Request Status Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.RequestStatusTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRequestStatusType()
         * @generated
         */
        EClass REQUEST_STATUS_TYPE = eINSTANCE.getRequestStatusType();

        /**
         * The meta object literal for the '<em><b>Timestamp</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_STATUS_TYPE__TIMESTAMP = eINSTANCE.getRequestStatusType_Timestamp();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.SchemaComponentTypeImpl <em>Schema Component Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.SchemaComponentTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSchemaComponentType()
         * @generated
         */
        EClass SCHEMA_COMPONENT_TYPE = eINSTANCE.getSchemaComponentType();

        /**
         * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SCHEMA_COMPONENT_TYPE__MIXED = eINSTANCE.getSchemaComponentType_Mixed();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SCHEMA_COMPONENT_TYPE__ANY = eINSTANCE.getSchemaComponentType_Any();

        /**
         * The meta object literal for the '<em><b>Parent Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SCHEMA_COMPONENT_TYPE__PARENT_SCHEMA = eINSTANCE.getSchemaComponentType_ParentSchema();

        /**
         * The meta object literal for the '<em><b>Schema Language</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SCHEMA_COMPONENT_TYPE__SCHEMA_LANGUAGE = eINSTANCE.getSchemaComponentType_SchemaLanguage();

        /**
         * The meta object literal for the '<em><b>Target Namespace</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SCHEMA_COMPONENT_TYPE__TARGET_NAMESPACE = eINSTANCE.getSchemaComponentType_TargetNamespace();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl <em>Search Results Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.SearchResultsTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSearchResultsType()
         * @generated
         */
        EClass SEARCH_RESULTS_TYPE = eINSTANCE.getSearchResultsType();

        /**
         * The meta object literal for the '<em><b>Abstract Record Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP = eINSTANCE.getSearchResultsType_AbstractRecordGroup();

        /**
         * The meta object literal for the '<em><b>Abstract Record</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SEARCH_RESULTS_TYPE__ABSTRACT_RECORD = eINSTANCE.getSearchResultsType_AbstractRecord();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__ANY = eINSTANCE.getSearchResultsType_Any();

        /**
         * The meta object literal for the '<em><b>Element Set</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__ELEMENT_SET = eINSTANCE.getSearchResultsType_ElementSet();

        /**
         * The meta object literal for the '<em><b>Expires</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__EXPIRES = eINSTANCE.getSearchResultsType_Expires();

        /**
         * The meta object literal for the '<em><b>Next Record</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__NEXT_RECORD = eINSTANCE.getSearchResultsType_NextRecord();

        /**
         * The meta object literal for the '<em><b>Number Of Records Matched</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_MATCHED = eINSTANCE.getSearchResultsType_NumberOfRecordsMatched();

        /**
         * The meta object literal for the '<em><b>Number Of Records Returned</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_RETURNED = eINSTANCE.getSearchResultsType_NumberOfRecordsReturned();

        /**
         * The meta object literal for the '<em><b>Record Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__RECORD_SCHEMA = eINSTANCE.getSearchResultsType_RecordSchema();

        /**
         * The meta object literal for the '<em><b>Result Set Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SEARCH_RESULTS_TYPE__RESULT_SET_ID = eINSTANCE.getSearchResultsType_ResultSetId();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.TransactionResponseTypeImpl <em>Transaction Response Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.TransactionResponseTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTransactionResponseType()
         * @generated
         */
        EClass TRANSACTION_RESPONSE_TYPE = eINSTANCE.getTransactionResponseType();

        /**
         * The meta object literal for the '<em><b>Transaction Summary</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY = eINSTANCE.getTransactionResponseType_TransactionSummary();

        /**
         * The meta object literal for the '<em><b>Insert Result</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSACTION_RESPONSE_TYPE__INSERT_RESULT = eINSTANCE.getTransactionResponseType_InsertResult();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSACTION_RESPONSE_TYPE__VERSION = eINSTANCE.getTransactionResponseType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.TransactionSummaryTypeImpl <em>Transaction Summary Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.TransactionSummaryTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTransactionSummaryType()
         * @generated
         */
        EClass TRANSACTION_SUMMARY_TYPE = eINSTANCE.getTransactionSummaryType();

        /**
         * The meta object literal for the '<em><b>Total Inserted</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED = eINSTANCE.getTransactionSummaryType_TotalInserted();

        /**
         * The meta object literal for the '<em><b>Total Updated</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED = eINSTANCE.getTransactionSummaryType_TotalUpdated();

        /**
         * The meta object literal for the '<em><b>Total Deleted</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED = eINSTANCE.getTransactionSummaryType_TotalDeleted();

        /**
         * The meta object literal for the '<em><b>Request Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSACTION_SUMMARY_TYPE__REQUEST_ID = eINSTANCE.getTransactionSummaryType_RequestId();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.TransactionTypeImpl <em>Transaction Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.TransactionTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTransactionType()
         * @generated
         */
        EClass TRANSACTION_TYPE = eINSTANCE.getTransactionType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSACTION_TYPE__GROUP = eINSTANCE.getTransactionType_Group();

        /**
         * The meta object literal for the '<em><b>Insert</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSACTION_TYPE__INSERT = eINSTANCE.getTransactionType_Insert();

        /**
         * The meta object literal for the '<em><b>Update</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSACTION_TYPE__UPDATE = eINSTANCE.getTransactionType_Update();

        /**
         * The meta object literal for the '<em><b>Delete</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSACTION_TYPE__DELETE = eINSTANCE.getTransactionType_Delete();

        /**
         * The meta object literal for the '<em><b>Request Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSACTION_TYPE__REQUEST_ID = eINSTANCE.getTransactionType_RequestId();

        /**
         * The meta object literal for the '<em><b>Verbose Response</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSACTION_TYPE__VERBOSE_RESPONSE = eINSTANCE.getTransactionType_VerboseResponse();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.UpdateTypeImpl <em>Update Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.UpdateTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getUpdateType()
         * @generated
         */
        EClass UPDATE_TYPE = eINSTANCE.getUpdateType();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UPDATE_TYPE__ANY = eINSTANCE.getUpdateType_Any();

        /**
         * The meta object literal for the '<em><b>Record Property</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UPDATE_TYPE__RECORD_PROPERTY = eINSTANCE.getUpdateType_RecordProperty();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UPDATE_TYPE__CONSTRAINT = eINSTANCE.getUpdateType_Constraint();

        /**
         * The meta object literal for the '<em><b>Handle</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UPDATE_TYPE__HANDLE = eINSTANCE.getUpdateType_Handle();

        /**
         * The meta object literal for the '{@link java.lang.String <em>String</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getString()
         * @generated
         */
        EClass STRING = eINSTANCE.getString();

        /**
         * The meta object literal for the '{@link org.opengis.filter.capability.FilterCapabilities <em>Filter Capabilities</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.opengis.filter.capability.FilterCapabilities
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getFilterCapabilities()
         * @generated
         */
        EClass FILTER_CAPABILITIES = eINSTANCE.getFilterCapabilities();

        /**
         * The meta object literal for the '{@link org.opengis.filter.Filter <em>Filter</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.opengis.filter.Filter
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getFilter()
         * @generated
         */
        EClass FILTER = eINSTANCE.getFilter();

        /**
         * The meta object literal for the '{@link org.opengis.filter.sort.SortBy <em>Sort By</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.opengis.filter.sort.SortBy
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSortBy()
         * @generated
         */
        EClass SORT_BY = eINSTANCE.getSortBy();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.DCMIRecordTypeImpl <em>DCMI Record Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.DCMIRecordTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDCMIRecordType()
         * @generated
         */
        EClass DCMI_RECORD_TYPE = eINSTANCE.getDCMIRecordType();

        /**
         * The meta object literal for the '<em><b>DC Element</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DCMI_RECORD_TYPE__DC_ELEMENT = eINSTANCE.getDCMIRecordType_DCElement();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.RecordTypeImpl <em>Record Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.RecordTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getRecordType()
         * @generated
         */
        EClass RECORD_TYPE = eINSTANCE.getRecordType();

        /**
         * The meta object literal for the '<em><b>Any Text</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RECORD_TYPE__ANY_TEXT = eINSTANCE.getRecordType_AnyText();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RECORD_TYPE__BOUNDING_BOX = eINSTANCE.getRecordType_BoundingBox();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.SimpleLiteralImpl <em>Simple Literal</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.SimpleLiteralImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSimpleLiteral()
         * @generated
         */
        EClass SIMPLE_LITERAL = eINSTANCE.getSimpleLiteral();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SIMPLE_LITERAL__VALUE = eINSTANCE.getSimpleLiteral_Value();

        /**
         * The meta object literal for the '<em><b>Scheme</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SIMPLE_LITERAL__SCHEME = eINSTANCE.getSimpleLiteral_Scheme();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SIMPLE_LITERAL__NAME = eINSTANCE.getSimpleLiteral_Name();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.impl.SummaryRecordTypeImpl <em>Summary Record Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.SummaryRecordTypeImpl
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSummaryRecordType()
         * @generated
         */
        EClass SUMMARY_RECORD_TYPE = eINSTANCE.getSummaryRecordType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__IDENTIFIER = eINSTANCE.getSummaryRecordType_Identifier();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__TITLE = eINSTANCE.getSummaryRecordType_Title();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__TYPE = eINSTANCE.getSummaryRecordType_Type();

        /**
         * The meta object literal for the '<em><b>Subject</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__SUBJECT = eINSTANCE.getSummaryRecordType_Subject();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__FORMAT = eINSTANCE.getSummaryRecordType_Format();

        /**
         * The meta object literal for the '<em><b>Relation</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__RELATION = eINSTANCE.getSummaryRecordType_Relation();

        /**
         * The meta object literal for the '<em><b>Modified</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__MODIFIED = eINSTANCE.getSummaryRecordType_Modified();

        /**
         * The meta object literal for the '<em><b>Abstract</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__ABSTRACT = eINSTANCE.getSummaryRecordType_Abstract();

        /**
         * The meta object literal for the '<em><b>Spatial</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__SPATIAL = eINSTANCE.getSummaryRecordType_Spatial();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUMMARY_RECORD_TYPE__BOUNDING_BOX = eINSTANCE.getSummaryRecordType_BoundingBox();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.ElementSetType <em>Element Set Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.ElementSetType
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getElementSetType()
         * @generated
         */
        EEnum ELEMENT_SET_TYPE = eINSTANCE.getElementSetType();

        /**
         * The meta object literal for the '{@link net.opengis.cat.csw20.ResultType <em>Result Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.ResultType
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getResultType()
         * @generated
         */
        EEnum RESULT_TYPE = eINSTANCE.getResultType();

        /**
         * The meta object literal for the '<em>Type Name List Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.List
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTypeNameListType()
         * @generated
         */
        EDataType TYPE_NAME_LIST_TYPE = eINSTANCE.getTypeNameListType();

        /**
         * The meta object literal for the '<em>Service Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getServiceType()
         * @generated
         */
        EDataType SERVICE_TYPE = eINSTANCE.getServiceType();

        /**
         * The meta object literal for the '<em>Type Name List Type 1</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.List
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getTypeNameListType_1()
         * @generated
         */
        EDataType TYPE_NAME_LIST_TYPE_1 = eINSTANCE.getTypeNameListType_1();

        /**
         * The meta object literal for the '<em>Service Type 1</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getServiceType_1()
         * @generated
         */
        EDataType SERVICE_TYPE_1 = eINSTANCE.getServiceType_1();

        /**
         * The meta object literal for the '<em>Version Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getVersionType()
         * @generated
         */
        EDataType VERSION_TYPE = eINSTANCE.getVersionType();

        /**
         * The meta object literal for the '<em>Calendar</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.Calendar
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getCalendar()
         * @generated
         */
        EDataType CALENDAR = eINSTANCE.getCalendar();

        /**
         * The meta object literal for the '<em>Set</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.Set
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSet()
         * @generated
         */
        EDataType SET = eINSTANCE.getSet();

        /**
         * The meta object literal for the '<em>URI</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.net.URI
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getURI()
         * @generated
         */
        EDataType URI = eINSTANCE.getURI();

        /**
         * The meta object literal for the '<em>QName</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see javax.xml.namespace.QName
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getQName()
         * @generated
         */
        EDataType QNAME = eINSTANCE.getQName();

        /**
         * The meta object literal for the '<em>Duration</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see javax.xml.datatype.Duration
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getDuration()
         * @generated
         */
        EDataType DURATION = eINSTANCE.getDuration();

        /**
         * The meta object literal for the '<em>Map</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.Map
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getMap()
         * @generated
         */
        EDataType MAP = eINSTANCE.getMap();

        /**
         * The meta object literal for the '<em>Sort By Array</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.cat.csw20.impl.Csw20PackageImpl#getSortByArray()
         * @generated
         */
        EDataType SORT_BY_ARRAY = eINSTANCE.getSortByArray();

    }

} //Csw20Package
