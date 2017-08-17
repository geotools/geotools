/**
 */
package net.opengis.wmts.v_1;

import net.opengis.ows11.Ows11Package;

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
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 			This XML Schema Document initially was intended to encode SOAP 
 * 			response for a WMTS GetTile request but in the future it might be used 
 * 			and part of a WMTS service (or even in any OWS service) that needs a 
 * 			binary encoding. 
 * 			
 * 			WMTS is an OGC Standard.
 * 			Copyright (c) 2009,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * 
 * 			This XML Schema Document includes all WMTS schemas and 
 * 			is useful for SOAP messages.
 * 			
 * 			WMTS is an OGC Standard.
 * 			Copyright (c) 2009,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * 
 * 			This XML Schema Document defines XML WMTS GetTile 
 * 			request that can be used in SOAP encodings.
 * 			
 * 			WMTS is an OGC Standard.
 * 			Copyright (c) 2009,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * 
 * 			This XML Schema Document defines the XML WMTS 
 * 			GetCapabilites request that can be used in SOAP encodings.
 * 			
 * 			WMTS is an OGC Standard.
 * 			Copyright (c) 2009,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * 
 * 			This XML Schema Document encodes the WMTS GetCapabilities 
 * 			operations response message.
 * 			
 * 			WMTS is an OGC Standard.
 * 			Copyright (c) 2009,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * 
 * 			This XML Schema Document encodes XML WMTS GetTile 
 * 			request that can be used in SOAP encodings.
 * 			
 * 			WMTS is an OGC Standard.
 * 			Copyright (c) 2009,2010 Open Geospatial Consortium.
 * 		    	To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * This XML Schema Document includes and imports, directly and indirectly, all the XML Schemas defined by the OWS Common Implemetation Specification.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes the GetResourceByID operation request message. This typical operation is specified as a base for profiling in specific OWS specifications. For information on the allowed changes and limitations in such profiling, see Subclause 9.4.1 of the OWS Common specification.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes the Exception Report response to all OWS operations.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes the allowed values (or domain) of a quantity, often for an input or output parameter to an OWS. Such a parameter is sometimes called a variable, quantity, literal, or typed literal. Such a parameter can use one of many data types, including double, integer, boolean, string, or URI. The allowed values can also be encoded for a quantity that is not explicit or not transferred, but is constrained by a server implementation.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema  Document encodes the typical Contents section of an OWS service metadata (Capabilities) document. This  Schema can be built upon to define the Contents section for a specific OWS. If the ContentsBaseType in this XML Schema cannot be restricted and extended to define the Contents section for a specific OWS, all other relevant parts defined in owsContents.xsd shall be used by the "ContentsType" in the wxsContents.xsd prepared for the specific OWS.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document specifies types and elements for input and output of operation data, allowing including multiple data items with each data item either included or referenced. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document specifies types and elements for document or resource references and for package manifests that contain multiple references. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes the parts of the MD_DataIdentification class of ISO 19115 (OGC Abstract Specification Topic 11) which are expected to be used for most datasets. This Schema also encodes the parts of this class that are expected to be useful for other metadata. Both may be used within the Contents section of OWS service metadata (Capabilities) documents.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes various parameters and parameter types that can be used in OWS operation requests and responses.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This schema document provides attribute declarations and
 * attribute group, complex type and simple type definitions which can be used in
 * the construction of user schemas to define the structure of particular linking
 * constructs, e.g.
 * 
 * <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
 *            xmlns:xl="http://www.w3.org/1999/xlink">
 * 
 *  <xs:import namespace="http://www.w3.org/1999/xlink"
 *             location="http://www.w3.org/1999/xlink.xsd">
 * 
 *  <xs:element name="mySimple">
 *   <xs:complexType>
 *    ...
 *    <xs:attributeGroup ref="xl:simpleAttrs"/>
 *    ...
 *   </xs:complexType>
 *  </xs:element>
 *  ...
 * </xs:schema>
 * 
 *    <div xmlns="http://www.w3.org/1999/xhtml">
 *     <h1>About the XML namespace</h1>
 * 
 *     <div class="bodytext">
 *      <p>
 *       This schema document describes the XML namespace, in a form
 *       suitable for import by other schema documents.
 *      </p>
 *      <p>
 *       See <a href="http://www.w3.org/XML/1998/namespace.html">
 *       http://www.w3.org/XML/1998/namespace.html</a> and
 *       <a href="http://www.w3.org/TR/REC-xml">
 *       http://www.w3.org/TR/REC-xml</a> for information 
 *       about this namespace.
 *      </p>
 *      <p>
 *       Note that local names in this namespace are intended to be
 *       defined only by the World Wide Web Consortium or its subgroups.
 *       The names currently defined in this namespace are listed below.
 *       They should not be used with conflicting semantics by any Working
 *       Group, specification, or document instance.
 *      </p>
 *      <p>   
 *       See further below in this document for more information about <a href="#usage">how to refer to this schema document from your own
 *       XSD schema documents</a> and about <a href="#nsversioning">the
 *       namespace-versioning policy governing this schema document</a>.
 *      </p>
 *     </div>
 *    </div>
 *   
 * 
 *    <div xmlns="http://www.w3.org/1999/xhtml">
 *    
 *     <h3>Father (in any context at all)</h3> 
 * 
 *     <div class="bodytext">
 *      <p>
 *       denotes Jon Bosak, the chair of 
 *       the original XML Working Group.  This name is reserved by 
 *       the following decision of the W3C XML Plenary and 
 *       XML Coordination groups:
 *      </p>
 *      <blockquote>
 *        <p>
 * 	In appreciation for his vision, leadership and
 * 	dedication the W3C XML Plenary on this 10th day of
 * 	February, 2000, reserves for Jon Bosak in perpetuity
 * 	the XML name "xml:Father".
 *        </p>
 *      </blockquote>
 *     </div>
 *    </div>
 *   
 * 
 *    <div id="usage" xml:id="usage" xmlns="http://www.w3.org/1999/xhtml">
 *     <h2>
 *       <a name="usage">About this schema document</a>
 *     </h2>
 * 
 *     <div class="bodytext">
 *      <p>
 *       This schema defines attributes and an attribute group suitable
 *       for use by schemas wishing to allow <code>xml:base</code>,
 *       <code>xml:lang</code>, <code>xml:space</code> or
 *       <code>xml:id</code> attributes on elements they define.
 *      </p>
 *      <p>
 *       To enable this, such a schema must import this schema for
 *       the XML namespace, e.g. as follows:
 *      </p>
 *      <pre>
 *           &lt;schema . . .&gt;
 *            . . .
 *            &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                       schemaLocation="http://www.w3.org/2001/xml.xsd"/&gt;
 *      </pre>
 *      <p>
 *       or
 *      </p>
 *      <pre>
 *            &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                       schemaLocation="http://www.w3.org/2009/01/xml.xsd"/&gt;
 *      </pre>
 *      <p>
 *       Subsequently, qualified reference to any of the attributes or the
 *       group defined below will have the desired effect, e.g.
 *      </p>
 *      <pre>
 *           &lt;type . . .&gt;
 *            . . .
 *            &lt;attributeGroup ref="xml:specialAttrs"/&gt;
 *      </pre>
 *      <p>
 *       will define a type which will schema-validate an instance element
 *       with any of those attributes.
 *      </p>
 *     </div>
 *    </div>
 *   
 * 
 *    <div id="nsversioning" xml:id="nsversioning" xmlns="http://www.w3.org/1999/xhtml">
 *     <h2>
 *       <a name="nsversioning">Versioning policy for this schema document</a>
 *     </h2>
 *     <div class="bodytext">
 *      <p>
 *       In keeping with the XML Schema WG's standard versioning
 *       policy, this schema document will persist at
 *       <a href="http://www.w3.org/2009/01/xml.xsd">
 *        http://www.w3.org/2009/01/xml.xsd</a>.
 *      </p>
 *      <p>
 *       At the date of issue it can also be found at
 *       <a href="http://www.w3.org/2001/xml.xsd">
 *        http://www.w3.org/2001/xml.xsd</a>.
 *      </p>
 *      <p>
 *       The schema document at that URI may however change in the future,
 *       in order to remain compatible with the latest version of XML
 *       Schema itself, or with the XML namespace itself.  In other words,
 *       if the XML Schema or XML namespaces change, the version of this
 *       document at <a href="http://www.w3.org/2001/xml.xsd">
 *        http://www.w3.org/2001/xml.xsd 
 *       </a> 
 *       will change accordingly; the version at 
 *       <a href="http://www.w3.org/2009/01/xml.xsd">
 *        http://www.w3.org/2009/01/xml.xsd 
 *       </a> 
 *       will not change.
 *      </p>
 *      <p>
 *       Previous dated (and unchanging) versions of this schema 
 *       document are at:
 *      </p>
 *      <ul>
 *       <li>
 *           <a href="http://www.w3.org/2009/01/xml.xsd">
 * 	http://www.w3.org/2009/01/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2007/08/xml.xsd">
 * 	http://www.w3.org/2007/08/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2004/10/xml.xsd">
 * 	http://www.w3.org/2004/10/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2001/03/xml.xsd">
 * 	http://www.w3.org/2001/03/xml.xsd</a>
 *         </li>
 *      </ul>
 *     </div>
 *    </div>
 *   
 * This XML Schema Document defines the GetCapabilities operation request and response XML elements and types, which are common to all OWSs. This XML Schema shall be edited by each OWS, for example, to specify a specific value for the "service" attribute.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes the common "ServiceIdentification" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceIdentification class of ISO 19119 (OGC Abstract Specification Topic 12). 
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes the common "ServiceProvider" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceProvider class of ISO 19119 (OGC Abstract Specification Topic 12). 
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes the basic contents of the "OperationsMetadata" section of the GetCapabilities operation response, also known as the Capabilities XML document.
 * 			
 * 			OWS is an OGC Standard.
 * 			Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * This XML Schema Document encodes the parts of ISO 19115 used by the common "ServiceIdentification" and "ServiceProvider" sections of the GetCapabilities operation response, known as the service metadata XML document. The parts encoded here are the MD_Keywords, CI_ResponsibleParty, and related classes. The UML package prefixes were omitted from XML names, and the XML element names were all capitalized, for consistency with other OWS Schemas. This document also provides a simple coding of text in multiple languages, simplified from Annex J of ISO 19115.
 * 		
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 		
 * <!-- end-model-doc -->
 * @see net.opengis.wmts.v_1.wmtsv_1Factory
 * @model kind="package"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface wmtsv_1Package extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "v_1";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.opengis.net/wmts/1.0";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "v_1";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    wmtsv_1Package eINSTANCE = net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl.init();

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.BinaryPayloadTypeImpl <em>Binary Payload Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.BinaryPayloadTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getBinaryPayloadType()
     * @generated
     */
    int BINARY_PAYLOAD_TYPE = 0;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_PAYLOAD_TYPE__FORMAT = 0;

    /**
     * The feature id for the '<em><b>Binary Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_PAYLOAD_TYPE__BINARY_CONTENT = 1;

    /**
     * The number of structural features of the '<em>Binary Payload Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_PAYLOAD_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Binary Payload Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_PAYLOAD_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.CapabilitiesTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getCapabilitiesType()
     * @generated
     */
    int CAPABILITIES_TYPE = 1;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_IDENTIFICATION = Ows11Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION;

    /**
     * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_PROVIDER = Ows11Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__OPERATIONS_METADATA = Ows11Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__UPDATE_SEQUENCE = Ows11Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__VERSION = Ows11Package.CAPABILITIES_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__CONTENTS = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Themes</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__THEMES = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>WSDL</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__WSDL = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Service Metadata URL</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_METADATA_URL = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE_FEATURE_COUNT = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of operations of the '<em>Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE_OPERATION_COUNT = Ows11Package.CAPABILITIES_BASE_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.ContentsTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getContentsType()
     * @generated
     */
    int CONTENTS_TYPE = 2;

    /**
     * The feature id for the '<em><b>Dataset Description Summary</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__DATASET_DESCRIPTION_SUMMARY = Ows11Package.CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY;

    /**
     * The feature id for the '<em><b>Other Source</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__OTHER_SOURCE = Ows11Package.CONTENTS_BASE_TYPE__OTHER_SOURCE;

    /**
     * The feature id for the '<em><b>Tile Matrix Set</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__TILE_MATRIX_SET = Ows11Package.CONTENTS_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Contents Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE_FEATURE_COUNT = Ows11Package.CONTENTS_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Contents Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE_OPERATION_COUNT = Ows11Package.CONTENTS_BASE_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.DimensionNameValueTypeImpl <em>Dimension Name Value Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.DimensionNameValueTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getDimensionNameValueType()
     * @generated
     */
    int DIMENSION_NAME_VALUE_TYPE = 3;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_NAME_VALUE_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_NAME_VALUE_TYPE__NAME = 1;

    /**
     * The number of structural features of the '<em>Dimension Name Value Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_NAME_VALUE_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Dimension Name Value Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_NAME_VALUE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.DimensionTypeImpl <em>Dimension Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.DimensionTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getDimensionType()
     * @generated
     */
    int DIMENSION_TYPE = 4;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__UOM = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Unit Symbol</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__UNIT_SYMBOL = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__DEFAULT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Current</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__CURRENT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE__VALUE = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Dimension Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
     * The number of operations of the '<em>Dimension Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TYPE_OPERATION_COUNT = Ows11Package.DESCRIPTION_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.DocumentRootImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getDocumentRoot()
     * @generated
     */
    int DOCUMENT_ROOT = 5;

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
     * The feature id for the '<em><b>Binary Payload</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__BINARY_PAYLOAD = 3;

    /**
     * The feature id for the '<em><b>Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CAPABILITIES = 4;

    /**
     * The feature id for the '<em><b>Dimension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DIMENSION = 5;

    /**
     * The feature id for the '<em><b>Dimension Name Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DIMENSION_NAME_VALUE = 6;

    /**
     * The feature id for the '<em><b>Feature Info Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FEATURE_INFO_RESPONSE = 7;

    /**
     * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_CAPABILITIES = 8;

    /**
     * The feature id for the '<em><b>Get Feature Info</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_FEATURE_INFO = 9;

    /**
     * The feature id for the '<em><b>Get Tile</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_TILE = 10;

    /**
     * The feature id for the '<em><b>Layer</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LAYER = 11;

    /**
     * The feature id for the '<em><b>Legend URL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LEGEND_URL = 12;

    /**
     * The feature id for the '<em><b>Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__STYLE = 13;

    /**
     * The feature id for the '<em><b>Text Payload</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TEXT_PAYLOAD = 14;

    /**
     * The feature id for the '<em><b>Theme</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__THEME = 15;

    /**
     * The feature id for the '<em><b>Themes</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__THEMES = 16;

    /**
     * The feature id for the '<em><b>Tile Matrix</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TILE_MATRIX = 17;

    /**
     * The feature id for the '<em><b>Tile Matrix Limits</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TILE_MATRIX_LIMITS = 18;

    /**
     * The feature id for the '<em><b>Tile Matrix Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TILE_MATRIX_SET = 19;

    /**
     * The feature id for the '<em><b>Tile Matrix Set Limits</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS = 20;

    /**
     * The feature id for the '<em><b>Tile Matrix Set Link</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TILE_MATRIX_SET_LINK = 21;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 22;

    /**
     * The number of operations of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl <em>Feature Info Response Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getFeatureInfoResponseType()
     * @generated
     */
    int FEATURE_INFO_RESPONSE_TYPE = 6;

    /**
     * The feature id for the '<em><b>Feature Collection Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP = 0;

    /**
     * The feature id for the '<em><b>Feature Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION = 1;

    /**
     * The feature id for the '<em><b>Text Payload</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD = 2;

    /**
     * The feature id for the '<em><b>Binary Payload</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD = 3;

    /**
     * The feature id for the '<em><b>Any Content</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT = 4;

    /**
     * The number of structural features of the '<em>Feature Info Response Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE_INFO_RESPONSE_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Feature Info Response Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FEATURE_INFO_RESPONSE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.GetCapabilitiesTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetCapabilitiesType()
     * @generated
     */
    int GET_CAPABILITIES_TYPE = 7;

    /**
     * The feature id for the '<em><b>Accept Versions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = Ows11Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS;

    /**
     * The feature id for the '<em><b>Sections</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SECTIONS = Ows11Package.GET_CAPABILITIES_TYPE__SECTIONS;

    /**
     * The feature id for the '<em><b>Accept Formats</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = Ows11Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = Ows11Package.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__BASE_URL = Ows11Package.GET_CAPABILITIES_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__NAMESPACE = Ows11Package.GET_CAPABILITIES_TYPE__NAMESPACE;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES = Ows11Package.GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SERVICE = Ows11Package.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Get Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE_FEATURE_COUNT = Ows11Package.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Get Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE_OPERATION_COUNT = Ows11Package.GET_CAPABILITIES_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl <em>Get Feature Info Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetFeatureInfoType()
     * @generated
     */
    int GET_FEATURE_INFO_TYPE = 8;

    /**
     * The feature id for the '<em><b>Get Tile</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_FEATURE_INFO_TYPE__GET_TILE = 0;

    /**
     * The feature id for the '<em><b>J</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_FEATURE_INFO_TYPE__J = 1;

    /**
     * The feature id for the '<em><b>I</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_FEATURE_INFO_TYPE__I = 2;

    /**
     * The feature id for the '<em><b>Info Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_FEATURE_INFO_TYPE__INFO_FORMAT = 3;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_FEATURE_INFO_TYPE__SERVICE = 4;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_FEATURE_INFO_TYPE__VERSION = 5;

    /**
     * The number of structural features of the '<em>Get Feature Info Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_FEATURE_INFO_TYPE_FEATURE_COUNT = 6;

    /**
     * The number of operations of the '<em>Get Feature Info Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_FEATURE_INFO_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl <em>Get Tile Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.GetTileTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetTileType()
     * @generated
     */
    int GET_TILE_TYPE = 9;

    /**
     * The feature id for the '<em><b>Layer</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__LAYER = 0;

    /**
     * The feature id for the '<em><b>Style</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__STYLE = 1;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__FORMAT = 2;

    /**
     * The feature id for the '<em><b>Dimension Name Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__DIMENSION_NAME_VALUE = 3;

    /**
     * The feature id for the '<em><b>Tile Matrix Set</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__TILE_MATRIX_SET = 4;

    /**
     * The feature id for the '<em><b>Tile Matrix</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__TILE_MATRIX = 5;

    /**
     * The feature id for the '<em><b>Tile Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__TILE_ROW = 6;

    /**
     * The feature id for the '<em><b>Tile Col</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__TILE_COL = 7;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__SERVICE = 8;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE__VERSION = 9;

    /**
     * The number of structural features of the '<em>Get Tile Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE_FEATURE_COUNT = 10;

    /**
     * The number of operations of the '<em>Get Tile Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_TILE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.LayerTypeImpl <em>Layer Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.LayerTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getLayerType()
     * @generated
     */
    int LAYER_TYPE = 10;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__TITLE = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__ABSTRACT = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__KEYWORDS = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__WGS84_BOUNDING_BOX = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__WGS84_BOUNDING_BOX;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__IDENTIFIER = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Bounding Box Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__BOUNDING_BOX_GROUP = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX_GROUP;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__BOUNDING_BOX = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__BOUNDING_BOX;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__METADATA = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Dataset Description Summary</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__DATASET_DESCRIPTION_SUMMARY = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY;

    /**
     * The feature id for the '<em><b>Style</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__STYLE = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__FORMAT = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Info Format</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__INFO_FORMAT = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Dimension</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__DIMENSION = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Tile Matrix Set Link</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__TILE_MATRIX_SET_LINK = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Resource URL</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE__RESOURCE_URL = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Layer Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE_FEATURE_COUNT = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_FEATURE_COUNT + 6;

    /**
     * The number of operations of the '<em>Layer Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LAYER_TYPE_OPERATION_COUNT = Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.LegendURLTypeImpl <em>Legend URL Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.LegendURLTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getLegendURLType()
     * @generated
     */
    int LEGEND_URL_TYPE = 11;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LEGEND_URL_TYPE__HREF = Ows11Package.ONLINE_RESOURCE_TYPE__HREF;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LEGEND_URL_TYPE__FORMAT = Ows11Package.ONLINE_RESOURCE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Height</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LEGEND_URL_TYPE__HEIGHT = Ows11Package.ONLINE_RESOURCE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Max Scale Denominator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR = Ows11Package.ONLINE_RESOURCE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Min Scale Denominator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR = Ows11Package.ONLINE_RESOURCE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LEGEND_URL_TYPE__WIDTH = Ows11Package.ONLINE_RESOURCE_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Legend URL Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LEGEND_URL_TYPE_FEATURE_COUNT = Ows11Package.ONLINE_RESOURCE_TYPE_FEATURE_COUNT + 5;

    /**
     * The number of operations of the '<em>Legend URL Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LEGEND_URL_TYPE_OPERATION_COUNT = Ows11Package.ONLINE_RESOURCE_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.StyleTypeImpl <em>Style Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.StyleTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getStyleType()
     * @generated
     */
    int STYLE_TYPE = 12;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Legend URL</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_TYPE__LEGEND_URL = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Is Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_TYPE__IS_DEFAULT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Style Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Style Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STYLE_TYPE_OPERATION_COUNT = Ows11Package.DESCRIPTION_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.TextPayloadTypeImpl <em>Text Payload Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.TextPayloadTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTextPayloadType()
     * @generated
     */
    int TEXT_PAYLOAD_TYPE = 13;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PAYLOAD_TYPE__FORMAT = 0;

    /**
     * The feature id for the '<em><b>Text Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PAYLOAD_TYPE__TEXT_CONTENT = 1;

    /**
     * The number of structural features of the '<em>Text Payload Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PAYLOAD_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Text Payload Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_PAYLOAD_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.ThemesTypeImpl <em>Themes Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.ThemesTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getThemesType()
     * @generated
     */
    int THEMES_TYPE = 14;

    /**
     * The feature id for the '<em><b>Theme</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEMES_TYPE__THEME = 0;

    /**
     * The number of structural features of the '<em>Themes Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEMES_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Themes Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEMES_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.ThemeTypeImpl <em>Theme Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.ThemeTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getThemeType()
     * @generated
     */
    int THEME_TYPE = 15;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEME_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEME_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEME_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEME_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Theme</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEME_TYPE__THEME = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Layer Ref</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEME_TYPE__LAYER_REF = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Theme Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEME_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Theme Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int THEME_TYPE_OPERATION_COUNT = Ows11Package.DESCRIPTION_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl <em>Tile Matrix Limits Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixLimitsType()
     * @generated
     */
    int TILE_MATRIX_LIMITS_TYPE = 16;

    /**
     * The feature id for the '<em><b>Tile Matrix</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_LIMITS_TYPE__TILE_MATRIX = 0;

    /**
     * The feature id for the '<em><b>Min Tile Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_LIMITS_TYPE__MIN_TILE_ROW = 1;

    /**
     * The feature id for the '<em><b>Max Tile Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_LIMITS_TYPE__MAX_TILE_ROW = 2;

    /**
     * The feature id for the '<em><b>Min Tile Col</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_LIMITS_TYPE__MIN_TILE_COL = 3;

    /**
     * The feature id for the '<em><b>Max Tile Col</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_LIMITS_TYPE__MAX_TILE_COL = 4;

    /**
     * The number of structural features of the '<em>Tile Matrix Limits Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_LIMITS_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Tile Matrix Limits Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_LIMITS_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.TileMatrixSetLimitsTypeImpl <em>Tile Matrix Set Limits Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.TileMatrixSetLimitsTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixSetLimitsType()
     * @generated
     */
    int TILE_MATRIX_SET_LIMITS_TYPE = 17;

    /**
     * The feature id for the '<em><b>Tile Matrix Limits</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS = 0;

    /**
     * The number of structural features of the '<em>Tile Matrix Set Limits Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_LIMITS_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Tile Matrix Set Limits Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_LIMITS_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.TileMatrixSetLinkTypeImpl <em>Tile Matrix Set Link Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.TileMatrixSetLinkTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixSetLinkType()
     * @generated
     */
    int TILE_MATRIX_SET_LINK_TYPE = 18;

    /**
     * The feature id for the '<em><b>Tile Matrix Set</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET = 0;

    /**
     * The feature id for the '<em><b>Tile Matrix Set Limits</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS = 1;

    /**
     * The number of structural features of the '<em>Tile Matrix Set Link Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_LINK_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Tile Matrix Set Link Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_LINK_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl <em>Tile Matrix Set Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixSetType()
     * @generated
     */
    int TILE_MATRIX_SET_TYPE = 19;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Bounding Box Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__BOUNDING_BOX = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Supported CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__SUPPORTED_CRS = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Well Known Scale Set</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__WELL_KNOWN_SCALE_SET = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Tile Matrix</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE__TILE_MATRIX = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Tile Matrix Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
     * The number of operations of the '<em>Tile Matrix Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_SET_TYPE_OPERATION_COUNT = Ows11Package.DESCRIPTION_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl <em>Tile Matrix Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.TileMatrixTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixType()
     * @generated
     */
    int TILE_MATRIX_TYPE = 20;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Scale Denominator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__SCALE_DENOMINATOR = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Top Left Corner</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__TOP_LEFT_CORNER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Tile Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__TILE_WIDTH = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Tile Height</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__TILE_HEIGHT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Matrix Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__MATRIX_WIDTH = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Matrix Height</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE__MATRIX_HEIGHT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Tile Matrix Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 7;

    /**
     * The number of operations of the '<em>Tile Matrix Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TILE_MATRIX_TYPE_OPERATION_COUNT = Ows11Package.DESCRIPTION_TYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.impl.URLTemplateTypeImpl <em>URL Template Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.impl.URLTemplateTypeImpl
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getURLTemplateType()
     * @generated
     */
    int URL_TEMPLATE_TYPE = 21;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URL_TEMPLATE_TYPE__FORMAT = 0;

    /**
     * The feature id for the '<em><b>Resource Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URL_TEMPLATE_TYPE__RESOURCE_TYPE = 1;

    /**
     * The feature id for the '<em><b>Template</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URL_TEMPLATE_TYPE__TEMPLATE = 2;

    /**
     * The number of structural features of the '<em>URL Template Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URL_TEMPLATE_TYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>URL Template Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URL_TEMPLATE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.GetCapabilitiesValueType <em>Get Capabilities Value Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.GetCapabilitiesValueType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetCapabilitiesValueType()
     * @generated
     */
    int GET_CAPABILITIES_VALUE_TYPE = 22;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.GetFeatureInfoValueType <em>Get Feature Info Value Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.GetFeatureInfoValueType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetFeatureInfoValueType()
     * @generated
     */
    int GET_FEATURE_INFO_VALUE_TYPE = 23;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.GetTileValueType <em>Get Tile Value Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.GetTileValueType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetTileValueType()
     * @generated
     */
    int GET_TILE_VALUE_TYPE = 24;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.RequestServiceType <em>Request Service Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.RequestServiceType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getRequestServiceType()
     * @generated
     */
    int REQUEST_SERVICE_TYPE = 25;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.ResourceTypeType <em>Resource Type Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.ResourceTypeType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getResourceTypeType()
     * @generated
     */
    int RESOURCE_TYPE_TYPE = 26;

    /**
     * The meta object id for the '{@link net.opengis.wmts.v_1.VersionType <em>Version Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.VersionType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getVersionType()
     * @generated
     */
    int VERSION_TYPE = 27;

    /**
     * The meta object id for the '<em>Accepted Formats Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getAcceptedFormatsType()
     * @generated
     */
    int ACCEPTED_FORMATS_TYPE = 28;

    /**
     * The meta object id for the '<em>Get Capabilities Value Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.GetCapabilitiesValueType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetCapabilitiesValueTypeObject()
     * @generated
     */
    int GET_CAPABILITIES_VALUE_TYPE_OBJECT = 29;

    /**
     * The meta object id for the '<em>Get Feature Info Value Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.GetFeatureInfoValueType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetFeatureInfoValueTypeObject()
     * @generated
     */
    int GET_FEATURE_INFO_VALUE_TYPE_OBJECT = 30;

    /**
     * The meta object id for the '<em>Get Tile Value Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.GetTileValueType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetTileValueTypeObject()
     * @generated
     */
    int GET_TILE_VALUE_TYPE_OBJECT = 31;

    /**
     * The meta object id for the '<em>Request Service Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.RequestServiceType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getRequestServiceTypeObject()
     * @generated
     */
    int REQUEST_SERVICE_TYPE_OBJECT = 32;

    /**
     * The meta object id for the '<em>Resource Type Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.ResourceTypeType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getResourceTypeTypeObject()
     * @generated
     */
    int RESOURCE_TYPE_TYPE_OBJECT = 33;

    /**
     * The meta object id for the '<em>Sections Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getSectionsType()
     * @generated
     */
    int SECTIONS_TYPE = 34;

    /**
     * The meta object id for the '<em>Template Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTemplateType()
     * @generated
     */
    int TEMPLATE_TYPE = 35;

    /**
     * The meta object id for the '<em>Version Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wmts.v_1.VersionType
     * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getVersionTypeObject()
     * @generated
     */
    int VERSION_TYPE_OBJECT = 36;


    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.BinaryPayloadType <em>Binary Payload Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Binary Payload Type</em>'.
     * @see net.opengis.wmts.v_1.BinaryPayloadType
     * @generated
     */
    EClass getBinaryPayloadType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.BinaryPayloadType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.wmts.v_1.BinaryPayloadType#getFormat()
     * @see #getBinaryPayloadType()
     * @generated
     */
    EAttribute getBinaryPayloadType_Format();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.BinaryPayloadType#getBinaryContent <em>Binary Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Binary Content</em>'.
     * @see net.opengis.wmts.v_1.BinaryPayloadType#getBinaryContent()
     * @see #getBinaryPayloadType()
     * @generated
     */
    EAttribute getBinaryPayloadType_BinaryContent();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.CapabilitiesType <em>Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Capabilities Type</em>'.
     * @see net.opengis.wmts.v_1.CapabilitiesType
     * @generated
     */
    EClass getCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.CapabilitiesType#getContents <em>Contents</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contents</em>'.
     * @see net.opengis.wmts.v_1.CapabilitiesType#getContents()
     * @see #getCapabilitiesType()
     * @generated
     */
    EReference getCapabilitiesType_Contents();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.CapabilitiesType#getThemes <em>Themes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Themes</em>'.
     * @see net.opengis.wmts.v_1.CapabilitiesType#getThemes()
     * @see #getCapabilitiesType()
     * @generated
     */
    EReference getCapabilitiesType_Themes();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.CapabilitiesType#getWSDL <em>WSDL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>WSDL</em>'.
     * @see net.opengis.wmts.v_1.CapabilitiesType#getWSDL()
     * @see #getCapabilitiesType()
     * @generated
     */
    EReference getCapabilitiesType_WSDL();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.CapabilitiesType#getServiceMetadataURL <em>Service Metadata URL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Service Metadata URL</em>'.
     * @see net.opengis.wmts.v_1.CapabilitiesType#getServiceMetadataURL()
     * @see #getCapabilitiesType()
     * @generated
     */
    EReference getCapabilitiesType_ServiceMetadataURL();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.ContentsType <em>Contents Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Contents Type</em>'.
     * @see net.opengis.wmts.v_1.ContentsType
     * @generated
     */
    EClass getContentsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.ContentsType#getTileMatrixSet <em>Tile Matrix Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Tile Matrix Set</em>'.
     * @see net.opengis.wmts.v_1.ContentsType#getTileMatrixSet()
     * @see #getContentsType()
     * @generated
     */
    EReference getContentsType_TileMatrixSet();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.DimensionNameValueType <em>Dimension Name Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dimension Name Value Type</em>'.
     * @see net.opengis.wmts.v_1.DimensionNameValueType
     * @generated
     */
    EClass getDimensionNameValueType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.DimensionNameValueType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.wmts.v_1.DimensionNameValueType#getValue()
     * @see #getDimensionNameValueType()
     * @generated
     */
    EAttribute getDimensionNameValueType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.DimensionNameValueType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.wmts.v_1.DimensionNameValueType#getName()
     * @see #getDimensionNameValueType()
     * @generated
     */
    EAttribute getDimensionNameValueType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.DimensionType <em>Dimension Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dimension Type</em>'.
     * @see net.opengis.wmts.v_1.DimensionType
     * @generated
     */
    EClass getDimensionType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DimensionType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wmts.v_1.DimensionType#getIdentifier()
     * @see #getDimensionType()
     * @generated
     */
    EReference getDimensionType_Identifier();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DimensionType#getUOM <em>UOM</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>UOM</em>'.
     * @see net.opengis.wmts.v_1.DimensionType#getUOM()
     * @see #getDimensionType()
     * @generated
     */
    EReference getDimensionType_UOM();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.DimensionType#getUnitSymbol <em>Unit Symbol</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Unit Symbol</em>'.
     * @see net.opengis.wmts.v_1.DimensionType#getUnitSymbol()
     * @see #getDimensionType()
     * @generated
     */
    EAttribute getDimensionType_UnitSymbol();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.DimensionType#getDefault <em>Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Default</em>'.
     * @see net.opengis.wmts.v_1.DimensionType#getDefault()
     * @see #getDimensionType()
     * @generated
     */
    EAttribute getDimensionType_Default();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.DimensionType#isCurrent <em>Current</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Current</em>'.
     * @see net.opengis.wmts.v_1.DimensionType#isCurrent()
     * @see #getDimensionType()
     * @generated
     */
    EAttribute getDimensionType_Current();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wmts.v_1.DimensionType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Value</em>'.
     * @see net.opengis.wmts.v_1.DimensionType#getValue()
     * @see #getDimensionType()
     * @generated
     */
    EAttribute getDimensionType_Value();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wmts.v_1.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map '{@link net.opengis.wmts.v_1.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link net.opengis.wmts.v_1.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getBinaryPayload <em>Binary Payload</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Binary Payload</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getBinaryPayload()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_BinaryPayload();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getCapabilities <em>Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Capabilities</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Capabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getDimension <em>Dimension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Dimension</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getDimension()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Dimension();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getDimensionNameValue <em>Dimension Name Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Dimension Name Value</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getDimensionNameValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DimensionNameValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getFeatureInfoResponse <em>Feature Info Response</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Feature Info Response</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getFeatureInfoResponse()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_FeatureInfoResponse();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getGetCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getGetFeatureInfo <em>Get Feature Info</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Feature Info</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getGetFeatureInfo()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetFeatureInfo();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getGetTile <em>Get Tile</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Tile</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getGetTile()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetTile();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getLayer <em>Layer</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Layer</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getLayer()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Layer();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getLegendURL <em>Legend URL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Legend URL</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getLegendURL()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_LegendURL();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getStyle <em>Style</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Style</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getStyle()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Style();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getTextPayload <em>Text Payload</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Text Payload</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getTextPayload()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TextPayload();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getTheme <em>Theme</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Theme</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getTheme()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Theme();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getThemes <em>Themes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Themes</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getThemes()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Themes();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrix <em>Tile Matrix</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Tile Matrix</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getTileMatrix()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TileMatrix();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixLimits <em>Tile Matrix Limits</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Tile Matrix Limits</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getTileMatrixLimits()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TileMatrixLimits();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSet <em>Tile Matrix Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Tile Matrix Set</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSet()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TileMatrixSet();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSetLimits <em>Tile Matrix Set Limits</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Tile Matrix Set Limits</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSetLimits()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TileMatrixSetLimits();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSetLink <em>Tile Matrix Set Link</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Tile Matrix Set Link</em>'.
     * @see net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSetLink()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TileMatrixSetLink();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.FeatureInfoResponseType <em>Feature Info Response Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Feature Info Response Type</em>'.
     * @see net.opengis.wmts.v_1.FeatureInfoResponseType
     * @generated
     */
    EClass getFeatureInfoResponseType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getFeatureCollectionGroup <em>Feature Collection Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Feature Collection Group</em>'.
     * @see net.opengis.wmts.v_1.FeatureInfoResponseType#getFeatureCollectionGroup()
     * @see #getFeatureInfoResponseType()
     * @generated
     */
    EAttribute getFeatureInfoResponseType_FeatureCollectionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getFeatureCollection <em>Feature Collection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Feature Collection</em>'.
     * @see net.opengis.wmts.v_1.FeatureInfoResponseType#getFeatureCollection()
     * @see #getFeatureInfoResponseType()
     * @generated
     */
    EReference getFeatureInfoResponseType_FeatureCollection();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getTextPayload <em>Text Payload</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Text Payload</em>'.
     * @see net.opengis.wmts.v_1.FeatureInfoResponseType#getTextPayload()
     * @see #getFeatureInfoResponseType()
     * @generated
     */
    EReference getFeatureInfoResponseType_TextPayload();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getBinaryPayload <em>Binary Payload</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Binary Payload</em>'.
     * @see net.opengis.wmts.v_1.FeatureInfoResponseType#getBinaryPayload()
     * @see #getFeatureInfoResponseType()
     * @generated
     */
    EReference getFeatureInfoResponseType_BinaryPayload();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getAnyContent <em>Any Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Any Content</em>'.
     * @see net.opengis.wmts.v_1.FeatureInfoResponseType#getAnyContent()
     * @see #getFeatureInfoResponseType()
     * @generated
     */
    EReference getFeatureInfoResponseType_AnyContent();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Capabilities Type</em>'.
     * @see net.opengis.wmts.v_1.GetCapabilitiesType
     * @generated
     */
    EClass getGetCapabilitiesType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetCapabilitiesType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wmts.v_1.GetCapabilitiesType#getService()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_Service();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.GetFeatureInfoType <em>Get Feature Info Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Feature Info Type</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoType
     * @generated
     */
    EClass getGetFeatureInfoType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getGetTile <em>Get Tile</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Tile</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoType#getGetTile()
     * @see #getGetFeatureInfoType()
     * @generated
     */
    EReference getGetFeatureInfoType_GetTile();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getJ <em>J</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>J</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoType#getJ()
     * @see #getGetFeatureInfoType()
     * @generated
     */
    EAttribute getGetFeatureInfoType_J();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getI <em>I</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>I</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoType#getI()
     * @see #getGetFeatureInfoType()
     * @generated
     */
    EAttribute getGetFeatureInfoType_I();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getInfoFormat <em>Info Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Info Format</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoType#getInfoFormat()
     * @see #getGetFeatureInfoType()
     * @generated
     */
    EAttribute getGetFeatureInfoType_InfoFormat();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoType#getService()
     * @see #getGetFeatureInfoType()
     * @generated
     */
    EAttribute getGetFeatureInfoType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoType#getVersion()
     * @see #getGetFeatureInfoType()
     * @generated
     */
    EAttribute getGetFeatureInfoType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.GetTileType <em>Get Tile Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Tile Type</em>'.
     * @see net.opengis.wmts.v_1.GetTileType
     * @generated
     */
    EClass getGetTileType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getLayer <em>Layer</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Layer</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getLayer()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_Layer();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getStyle <em>Style</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Style</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getStyle()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_Style();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getFormat()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_Format();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.GetTileType#getDimensionNameValue <em>Dimension Name Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Dimension Name Value</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getDimensionNameValue()
     * @see #getGetTileType()
     * @generated
     */
    EReference getGetTileType_DimensionNameValue();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getTileMatrixSet <em>Tile Matrix Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tile Matrix Set</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getTileMatrixSet()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_TileMatrixSet();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getTileMatrix <em>Tile Matrix</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tile Matrix</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getTileMatrix()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_TileMatrix();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getTileRow <em>Tile Row</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tile Row</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getTileRow()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_TileRow();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getTileCol <em>Tile Col</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tile Col</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getTileCol()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_TileCol();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getService()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.GetTileType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.wmts.v_1.GetTileType#getVersion()
     * @see #getGetTileType()
     * @generated
     */
    EAttribute getGetTileType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.LayerType <em>Layer Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Layer Type</em>'.
     * @see net.opengis.wmts.v_1.LayerType
     * @generated
     */
    EClass getLayerType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.LayerType#getStyle <em>Style</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Style</em>'.
     * @see net.opengis.wmts.v_1.LayerType#getStyle()
     * @see #getLayerType()
     * @generated
     */
    EReference getLayerType_Style();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wmts.v_1.LayerType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Format</em>'.
     * @see net.opengis.wmts.v_1.LayerType#getFormat()
     * @see #getLayerType()
     * @generated
     */
    EAttribute getLayerType_Format();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wmts.v_1.LayerType#getInfoFormat <em>Info Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Info Format</em>'.
     * @see net.opengis.wmts.v_1.LayerType#getInfoFormat()
     * @see #getLayerType()
     * @generated
     */
    EAttribute getLayerType_InfoFormat();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.LayerType#getDimension <em>Dimension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Dimension</em>'.
     * @see net.opengis.wmts.v_1.LayerType#getDimension()
     * @see #getLayerType()
     * @generated
     */
    EReference getLayerType_Dimension();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.LayerType#getTileMatrixSetLink <em>Tile Matrix Set Link</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Tile Matrix Set Link</em>'.
     * @see net.opengis.wmts.v_1.LayerType#getTileMatrixSetLink()
     * @see #getLayerType()
     * @generated
     */
    EReference getLayerType_TileMatrixSetLink();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.LayerType#getResourceURL <em>Resource URL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Resource URL</em>'.
     * @see net.opengis.wmts.v_1.LayerType#getResourceURL()
     * @see #getLayerType()
     * @generated
     */
    EReference getLayerType_ResourceURL();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.LegendURLType <em>Legend URL Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Legend URL Type</em>'.
     * @see net.opengis.wmts.v_1.LegendURLType
     * @generated
     */
    EClass getLegendURLType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.LegendURLType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.wmts.v_1.LegendURLType#getFormat()
     * @see #getLegendURLType()
     * @generated
     */
    EAttribute getLegendURLType_Format();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.LegendURLType#getHeight <em>Height</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Height</em>'.
     * @see net.opengis.wmts.v_1.LegendURLType#getHeight()
     * @see #getLegendURLType()
     * @generated
     */
    EAttribute getLegendURLType_Height();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.LegendURLType#getMaxScaleDenominator <em>Max Scale Denominator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max Scale Denominator</em>'.
     * @see net.opengis.wmts.v_1.LegendURLType#getMaxScaleDenominator()
     * @see #getLegendURLType()
     * @generated
     */
    EAttribute getLegendURLType_MaxScaleDenominator();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.LegendURLType#getMinScaleDenominator <em>Min Scale Denominator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Min Scale Denominator</em>'.
     * @see net.opengis.wmts.v_1.LegendURLType#getMinScaleDenominator()
     * @see #getLegendURLType()
     * @generated
     */
    EAttribute getLegendURLType_MinScaleDenominator();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.LegendURLType#getWidth <em>Width</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Width</em>'.
     * @see net.opengis.wmts.v_1.LegendURLType#getWidth()
     * @see #getLegendURLType()
     * @generated
     */
    EAttribute getLegendURLType_Width();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.StyleType <em>Style Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Style Type</em>'.
     * @see net.opengis.wmts.v_1.StyleType
     * @generated
     */
    EClass getStyleType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.StyleType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wmts.v_1.StyleType#getIdentifier()
     * @see #getStyleType()
     * @generated
     */
    EReference getStyleType_Identifier();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.StyleType#getLegendURL <em>Legend URL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Legend URL</em>'.
     * @see net.opengis.wmts.v_1.StyleType#getLegendURL()
     * @see #getStyleType()
     * @generated
     */
    EReference getStyleType_LegendURL();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.StyleType#isIsDefault <em>Is Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Is Default</em>'.
     * @see net.opengis.wmts.v_1.StyleType#isIsDefault()
     * @see #getStyleType()
     * @generated
     */
    EAttribute getStyleType_IsDefault();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.TextPayloadType <em>Text Payload Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Text Payload Type</em>'.
     * @see net.opengis.wmts.v_1.TextPayloadType
     * @generated
     */
    EClass getTextPayloadType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TextPayloadType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.wmts.v_1.TextPayloadType#getFormat()
     * @see #getTextPayloadType()
     * @generated
     */
    EAttribute getTextPayloadType_Format();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TextPayloadType#getTextContent <em>Text Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text Content</em>'.
     * @see net.opengis.wmts.v_1.TextPayloadType#getTextContent()
     * @see #getTextPayloadType()
     * @generated
     */
    EAttribute getTextPayloadType_TextContent();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.ThemesType <em>Themes Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Themes Type</em>'.
     * @see net.opengis.wmts.v_1.ThemesType
     * @generated
     */
    EClass getThemesType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.ThemesType#getTheme <em>Theme</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Theme</em>'.
     * @see net.opengis.wmts.v_1.ThemesType#getTheme()
     * @see #getThemesType()
     * @generated
     */
    EReference getThemesType_Theme();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.ThemeType <em>Theme Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Theme Type</em>'.
     * @see net.opengis.wmts.v_1.ThemeType
     * @generated
     */
    EClass getThemeType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.ThemeType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wmts.v_1.ThemeType#getIdentifier()
     * @see #getThemeType()
     * @generated
     */
    EReference getThemeType_Identifier();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.ThemeType#getTheme <em>Theme</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Theme</em>'.
     * @see net.opengis.wmts.v_1.ThemeType#getTheme()
     * @see #getThemeType()
     * @generated
     */
    EReference getThemeType_Theme();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wmts.v_1.ThemeType#getLayerRef <em>Layer Ref</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Layer Ref</em>'.
     * @see net.opengis.wmts.v_1.ThemeType#getLayerRef()
     * @see #getThemeType()
     * @generated
     */
    EAttribute getThemeType_LayerRef();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.TileMatrixLimitsType <em>Tile Matrix Limits Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tile Matrix Limits Type</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixLimitsType
     * @generated
     */
    EClass getTileMatrixLimitsType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getTileMatrix <em>Tile Matrix</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tile Matrix</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixLimitsType#getTileMatrix()
     * @see #getTileMatrixLimitsType()
     * @generated
     */
    EAttribute getTileMatrixLimitsType_TileMatrix();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMinTileRow <em>Min Tile Row</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Min Tile Row</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixLimitsType#getMinTileRow()
     * @see #getTileMatrixLimitsType()
     * @generated
     */
    EAttribute getTileMatrixLimitsType_MinTileRow();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMaxTileRow <em>Max Tile Row</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max Tile Row</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixLimitsType#getMaxTileRow()
     * @see #getTileMatrixLimitsType()
     * @generated
     */
    EAttribute getTileMatrixLimitsType_MaxTileRow();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMinTileCol <em>Min Tile Col</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Min Tile Col</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixLimitsType#getMinTileCol()
     * @see #getTileMatrixLimitsType()
     * @generated
     */
    EAttribute getTileMatrixLimitsType_MinTileCol();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMaxTileCol <em>Max Tile Col</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max Tile Col</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixLimitsType#getMaxTileCol()
     * @see #getTileMatrixLimitsType()
     * @generated
     */
    EAttribute getTileMatrixLimitsType_MaxTileCol();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.TileMatrixSetLimitsType <em>Tile Matrix Set Limits Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tile Matrix Set Limits Type</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetLimitsType
     * @generated
     */
    EClass getTileMatrixSetLimitsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.TileMatrixSetLimitsType#getTileMatrixLimits <em>Tile Matrix Limits</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Tile Matrix Limits</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetLimitsType#getTileMatrixLimits()
     * @see #getTileMatrixSetLimitsType()
     * @generated
     */
    EReference getTileMatrixSetLimitsType_TileMatrixLimits();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.TileMatrixSetLinkType <em>Tile Matrix Set Link Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tile Matrix Set Link Type</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetLinkType
     * @generated
     */
    EClass getTileMatrixSetLinkType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixSetLinkType#getTileMatrixSet <em>Tile Matrix Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tile Matrix Set</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetLinkType#getTileMatrixSet()
     * @see #getTileMatrixSetLinkType()
     * @generated
     */
    EAttribute getTileMatrixSetLinkType_TileMatrixSet();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.TileMatrixSetLinkType#getTileMatrixSetLimits <em>Tile Matrix Set Limits</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Tile Matrix Set Limits</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetLinkType#getTileMatrixSetLimits()
     * @see #getTileMatrixSetLinkType()
     * @generated
     */
    EReference getTileMatrixSetLinkType_TileMatrixSetLimits();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.TileMatrixSetType <em>Tile Matrix Set Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tile Matrix Set Type</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetType
     * @generated
     */
    EClass getTileMatrixSetType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.TileMatrixSetType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetType#getIdentifier()
     * @see #getTileMatrixSetType()
     * @generated
     */
    EReference getTileMatrixSetType_Identifier();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wmts.v_1.TileMatrixSetType#getBoundingBoxGroup <em>Bounding Box Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Bounding Box Group</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetType#getBoundingBoxGroup()
     * @see #getTileMatrixSetType()
     * @generated
     */
    EAttribute getTileMatrixSetType_BoundingBoxGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.TileMatrixSetType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bounding Box</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetType#getBoundingBox()
     * @see #getTileMatrixSetType()
     * @generated
     */
    EReference getTileMatrixSetType_BoundingBox();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixSetType#getSupportedCRS <em>Supported CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Supported CRS</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetType#getSupportedCRS()
     * @see #getTileMatrixSetType()
     * @generated
     */
    EAttribute getTileMatrixSetType_SupportedCRS();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixSetType#getWellKnownScaleSet <em>Well Known Scale Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Well Known Scale Set</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetType#getWellKnownScaleSet()
     * @see #getTileMatrixSetType()
     * @generated
     */
    EAttribute getTileMatrixSetType_WellKnownScaleSet();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wmts.v_1.TileMatrixSetType#getTileMatrix <em>Tile Matrix</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Tile Matrix</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixSetType#getTileMatrix()
     * @see #getTileMatrixSetType()
     * @generated
     */
    EReference getTileMatrixSetType_TileMatrix();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.TileMatrixType <em>Tile Matrix Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Tile Matrix Type</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixType
     * @generated
     */
    EClass getTileMatrixType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wmts.v_1.TileMatrixType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixType#getIdentifier()
     * @see #getTileMatrixType()
     * @generated
     */
    EReference getTileMatrixType_Identifier();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixType#getScaleDenominator <em>Scale Denominator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Scale Denominator</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixType#getScaleDenominator()
     * @see #getTileMatrixType()
     * @generated
     */
    EAttribute getTileMatrixType_ScaleDenominator();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixType#getTopLeftCorner <em>Top Left Corner</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Top Left Corner</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixType#getTopLeftCorner()
     * @see #getTileMatrixType()
     * @generated
     */
    EAttribute getTileMatrixType_TopLeftCorner();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixType#getTileWidth <em>Tile Width</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tile Width</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixType#getTileWidth()
     * @see #getTileMatrixType()
     * @generated
     */
    EAttribute getTileMatrixType_TileWidth();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixType#getTileHeight <em>Tile Height</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Tile Height</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixType#getTileHeight()
     * @see #getTileMatrixType()
     * @generated
     */
    EAttribute getTileMatrixType_TileHeight();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixType#getMatrixWidth <em>Matrix Width</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Matrix Width</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixType#getMatrixWidth()
     * @see #getTileMatrixType()
     * @generated
     */
    EAttribute getTileMatrixType_MatrixWidth();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.TileMatrixType#getMatrixHeight <em>Matrix Height</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Matrix Height</em>'.
     * @see net.opengis.wmts.v_1.TileMatrixType#getMatrixHeight()
     * @see #getTileMatrixType()
     * @generated
     */
    EAttribute getTileMatrixType_MatrixHeight();

    /**
     * Returns the meta object for class '{@link net.opengis.wmts.v_1.URLTemplateType <em>URL Template Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>URL Template Type</em>'.
     * @see net.opengis.wmts.v_1.URLTemplateType
     * @generated
     */
    EClass getURLTemplateType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.URLTemplateType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.wmts.v_1.URLTemplateType#getFormat()
     * @see #getURLTemplateType()
     * @generated
     */
    EAttribute getURLTemplateType_Format();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.URLTemplateType#getResourceType <em>Resource Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Resource Type</em>'.
     * @see net.opengis.wmts.v_1.URLTemplateType#getResourceType()
     * @see #getURLTemplateType()
     * @generated
     */
    EAttribute getURLTemplateType_ResourceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wmts.v_1.URLTemplateType#getTemplate <em>Template</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Template</em>'.
     * @see net.opengis.wmts.v_1.URLTemplateType#getTemplate()
     * @see #getURLTemplateType()
     * @generated
     */
    EAttribute getURLTemplateType_Template();

    /**
     * Returns the meta object for enum '{@link net.opengis.wmts.v_1.GetCapabilitiesValueType <em>Get Capabilities Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Get Capabilities Value Type</em>'.
     * @see net.opengis.wmts.v_1.GetCapabilitiesValueType
     * @generated
     */
    EEnum getGetCapabilitiesValueType();

    /**
     * Returns the meta object for enum '{@link net.opengis.wmts.v_1.GetFeatureInfoValueType <em>Get Feature Info Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Get Feature Info Value Type</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoValueType
     * @generated
     */
    EEnum getGetFeatureInfoValueType();

    /**
     * Returns the meta object for enum '{@link net.opengis.wmts.v_1.GetTileValueType <em>Get Tile Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Get Tile Value Type</em>'.
     * @see net.opengis.wmts.v_1.GetTileValueType
     * @generated
     */
    EEnum getGetTileValueType();

    /**
     * Returns the meta object for enum '{@link net.opengis.wmts.v_1.RequestServiceType <em>Request Service Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Request Service Type</em>'.
     * @see net.opengis.wmts.v_1.RequestServiceType
     * @generated
     */
    EEnum getRequestServiceType();

    /**
     * Returns the meta object for enum '{@link net.opengis.wmts.v_1.ResourceTypeType <em>Resource Type Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Resource Type Type</em>'.
     * @see net.opengis.wmts.v_1.ResourceTypeType
     * @generated
     */
    EEnum getResourceTypeType();

    /**
     * Returns the meta object for enum '{@link net.opengis.wmts.v_1.VersionType <em>Version Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Version Type</em>'.
     * @see net.opengis.wmts.v_1.VersionType
     * @generated
     */
    EEnum getVersionType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Accepted Formats Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Accepted Formats Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='AcceptedFormatsType' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='((application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*)(,(application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*)'"
     * @generated
     */
    EDataType getAcceptedFormatsType();

    /**
     * Returns the meta object for data type '{@link net.opengis.wmts.v_1.GetCapabilitiesValueType <em>Get Capabilities Value Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Get Capabilities Value Type Object</em>'.
     * @see net.opengis.wmts.v_1.GetCapabilitiesValueType
     * @model instanceClass="net.opengis.wmts.v_1.GetCapabilitiesValueType"
     *        extendedMetaData="name='GetCapabilitiesValueType:Object' baseType='GetCapabilitiesValueType'"
     * @generated
     */
    EDataType getGetCapabilitiesValueTypeObject();

    /**
     * Returns the meta object for data type '{@link net.opengis.wmts.v_1.GetFeatureInfoValueType <em>Get Feature Info Value Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Get Feature Info Value Type Object</em>'.
     * @see net.opengis.wmts.v_1.GetFeatureInfoValueType
     * @model instanceClass="net.opengis.wmts.v_1.GetFeatureInfoValueType"
     *        extendedMetaData="name='GetFeatureInfoValueType:Object' baseType='GetFeatureInfoValueType'"
     * @generated
     */
    EDataType getGetFeatureInfoValueTypeObject();

    /**
     * Returns the meta object for data type '{@link net.opengis.wmts.v_1.GetTileValueType <em>Get Tile Value Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Get Tile Value Type Object</em>'.
     * @see net.opengis.wmts.v_1.GetTileValueType
     * @model instanceClass="net.opengis.wmts.v_1.GetTileValueType"
     *        extendedMetaData="name='GetTileValueType:Object' baseType='GetTileValueType'"
     * @generated
     */
    EDataType getGetTileValueTypeObject();

    /**
     * Returns the meta object for data type '{@link net.opengis.wmts.v_1.RequestServiceType <em>Request Service Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Request Service Type Object</em>'.
     * @see net.opengis.wmts.v_1.RequestServiceType
     * @model instanceClass="net.opengis.wmts.v_1.RequestServiceType"
     *        extendedMetaData="name='RequestServiceType:Object' baseType='RequestServiceType'"
     * @generated
     */
    EDataType getRequestServiceTypeObject();

    /**
     * Returns the meta object for data type '{@link net.opengis.wmts.v_1.ResourceTypeType <em>Resource Type Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Resource Type Type Object</em>'.
     * @see net.opengis.wmts.v_1.ResourceTypeType
     * @model instanceClass="net.opengis.wmts.v_1.ResourceTypeType"
     *        extendedMetaData="name='resourceType_._type:Object' baseType='resourceType_._type'"
     * @generated
     */
    EDataType getResourceTypeTypeObject();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Sections Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Sections Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='SectionsType' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='(ServiceIdentification|ServiceProvider|OperationsMetadata|Contents|Themes)(,(ServiceIdentification|ServiceProvider|OperationsMetadata|Contents|Themes))*'"
     * @generated
     */
    EDataType getSectionsType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Template Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Template Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='template_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='([A-Za-z0-9\\-_\\.!~\\*\'\\(\\);/\\?:@\\+:$,#\\{\\}=&]|%25[A-Fa-f0-9][A-Fa-f0-9])+'"
     * @generated
     */
    EDataType getTemplateType();

    /**
     * Returns the meta object for data type '{@link net.opengis.wmts.v_1.VersionType <em>Version Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Version Type Object</em>'.
     * @see net.opengis.wmts.v_1.VersionType
     * @model instanceClass="net.opengis.wmts.v_1.VersionType"
     *        extendedMetaData="name='VersionType:Object' baseType='VersionType'"
     * @generated
     */
    EDataType getVersionTypeObject();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    wmtsv_1Factory getwmtsv_1Factory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.BinaryPayloadTypeImpl <em>Binary Payload Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.BinaryPayloadTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getBinaryPayloadType()
         * @generated
         */
        EClass BINARY_PAYLOAD_TYPE = eINSTANCE.getBinaryPayloadType();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_PAYLOAD_TYPE__FORMAT = eINSTANCE.getBinaryPayloadType_Format();

        /**
         * The meta object literal for the '<em><b>Binary Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_PAYLOAD_TYPE__BINARY_CONTENT = eINSTANCE.getBinaryPayloadType_BinaryContent();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.CapabilitiesTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getCapabilitiesType()
         * @generated
         */
        EClass CAPABILITIES_TYPE = eINSTANCE.getCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Contents</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_TYPE__CONTENTS = eINSTANCE.getCapabilitiesType_Contents();

        /**
         * The meta object literal for the '<em><b>Themes</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_TYPE__THEMES = eINSTANCE.getCapabilitiesType_Themes();

        /**
         * The meta object literal for the '<em><b>WSDL</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_TYPE__WSDL = eINSTANCE.getCapabilitiesType_WSDL();

        /**
         * The meta object literal for the '<em><b>Service Metadata URL</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_TYPE__SERVICE_METADATA_URL = eINSTANCE.getCapabilitiesType_ServiceMetadataURL();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.ContentsTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getContentsType()
         * @generated
         */
        EClass CONTENTS_TYPE = eINSTANCE.getContentsType();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Set</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTENTS_TYPE__TILE_MATRIX_SET = eINSTANCE.getContentsType_TileMatrixSet();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.DimensionNameValueTypeImpl <em>Dimension Name Value Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.DimensionNameValueTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getDimensionNameValueType()
         * @generated
         */
        EClass DIMENSION_NAME_VALUE_TYPE = eINSTANCE.getDimensionNameValueType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_NAME_VALUE_TYPE__VALUE = eINSTANCE.getDimensionNameValueType_Value();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_NAME_VALUE_TYPE__NAME = eINSTANCE.getDimensionNameValueType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.DimensionTypeImpl <em>Dimension Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.DimensionTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getDimensionType()
         * @generated
         */
        EClass DIMENSION_TYPE = eINSTANCE.getDimensionType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIMENSION_TYPE__IDENTIFIER = eINSTANCE.getDimensionType_Identifier();

        /**
         * The meta object literal for the '<em><b>UOM</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DIMENSION_TYPE__UOM = eINSTANCE.getDimensionType_UOM();

        /**
         * The meta object literal for the '<em><b>Unit Symbol</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_TYPE__UNIT_SYMBOL = eINSTANCE.getDimensionType_UnitSymbol();

        /**
         * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_TYPE__DEFAULT = eINSTANCE.getDimensionType_Default();

        /**
         * The meta object literal for the '<em><b>Current</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_TYPE__CURRENT = eINSTANCE.getDimensionType_Current();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_TYPE__VALUE = eINSTANCE.getDimensionType_Value();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.DocumentRootImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getDocumentRoot()
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
         * The meta object literal for the '<em><b>Binary Payload</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__BINARY_PAYLOAD = eINSTANCE.getDocumentRoot_BinaryPayload();

        /**
         * The meta object literal for the '<em><b>Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__CAPABILITIES = eINSTANCE.getDocumentRoot_Capabilities();

        /**
         * The meta object literal for the '<em><b>Dimension</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DIMENSION = eINSTANCE.getDocumentRoot_Dimension();

        /**
         * The meta object literal for the '<em><b>Dimension Name Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DIMENSION_NAME_VALUE = eINSTANCE.getDocumentRoot_DimensionNameValue();

        /**
         * The meta object literal for the '<em><b>Feature Info Response</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__FEATURE_INFO_RESPONSE = eINSTANCE.getDocumentRoot_FeatureInfoResponse();

        /**
         * The meta object literal for the '<em><b>Get Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_CAPABILITIES = eINSTANCE.getDocumentRoot_GetCapabilities();

        /**
         * The meta object literal for the '<em><b>Get Feature Info</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_FEATURE_INFO = eINSTANCE.getDocumentRoot_GetFeatureInfo();

        /**
         * The meta object literal for the '<em><b>Get Tile</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_TILE = eINSTANCE.getDocumentRoot_GetTile();

        /**
         * The meta object literal for the '<em><b>Layer</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__LAYER = eINSTANCE.getDocumentRoot_Layer();

        /**
         * The meta object literal for the '<em><b>Legend URL</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__LEGEND_URL = eINSTANCE.getDocumentRoot_LegendURL();

        /**
         * The meta object literal for the '<em><b>Style</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__STYLE = eINSTANCE.getDocumentRoot_Style();

        /**
         * The meta object literal for the '<em><b>Text Payload</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TEXT_PAYLOAD = eINSTANCE.getDocumentRoot_TextPayload();

        /**
         * The meta object literal for the '<em><b>Theme</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__THEME = eINSTANCE.getDocumentRoot_Theme();

        /**
         * The meta object literal for the '<em><b>Themes</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__THEMES = eINSTANCE.getDocumentRoot_Themes();

        /**
         * The meta object literal for the '<em><b>Tile Matrix</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TILE_MATRIX = eINSTANCE.getDocumentRoot_TileMatrix();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Limits</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TILE_MATRIX_LIMITS = eINSTANCE.getDocumentRoot_TileMatrixLimits();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Set</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TILE_MATRIX_SET = eINSTANCE.getDocumentRoot_TileMatrixSet();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Set Limits</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS = eINSTANCE.getDocumentRoot_TileMatrixSetLimits();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Set Link</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TILE_MATRIX_SET_LINK = eINSTANCE.getDocumentRoot_TileMatrixSetLink();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl <em>Feature Info Response Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.FeatureInfoResponseTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getFeatureInfoResponseType()
         * @generated
         */
        EClass FEATURE_INFO_RESPONSE_TYPE = eINSTANCE.getFeatureInfoResponseType();

        /**
         * The meta object literal for the '<em><b>Feature Collection Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP = eINSTANCE.getFeatureInfoResponseType_FeatureCollectionGroup();

        /**
         * The meta object literal for the '<em><b>Feature Collection</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION = eINSTANCE.getFeatureInfoResponseType_FeatureCollection();

        /**
         * The meta object literal for the '<em><b>Text Payload</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD = eINSTANCE.getFeatureInfoResponseType_TextPayload();

        /**
         * The meta object literal for the '<em><b>Binary Payload</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD = eINSTANCE.getFeatureInfoResponseType_BinaryPayload();

        /**
         * The meta object literal for the '<em><b>Any Content</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT = eINSTANCE.getFeatureInfoResponseType_AnyContent();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.GetCapabilitiesTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetCapabilitiesType()
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
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl <em>Get Feature Info Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetFeatureInfoType()
         * @generated
         */
        EClass GET_FEATURE_INFO_TYPE = eINSTANCE.getGetFeatureInfoType();

        /**
         * The meta object literal for the '<em><b>Get Tile</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_FEATURE_INFO_TYPE__GET_TILE = eINSTANCE.getGetFeatureInfoType_GetTile();

        /**
         * The meta object literal for the '<em><b>J</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_FEATURE_INFO_TYPE__J = eINSTANCE.getGetFeatureInfoType_J();

        /**
         * The meta object literal for the '<em><b>I</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_FEATURE_INFO_TYPE__I = eINSTANCE.getGetFeatureInfoType_I();

        /**
         * The meta object literal for the '<em><b>Info Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_FEATURE_INFO_TYPE__INFO_FORMAT = eINSTANCE.getGetFeatureInfoType_InfoFormat();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_FEATURE_INFO_TYPE__SERVICE = eINSTANCE.getGetFeatureInfoType_Service();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_FEATURE_INFO_TYPE__VERSION = eINSTANCE.getGetFeatureInfoType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl <em>Get Tile Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.GetTileTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetTileType()
         * @generated
         */
        EClass GET_TILE_TYPE = eINSTANCE.getGetTileType();

        /**
         * The meta object literal for the '<em><b>Layer</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__LAYER = eINSTANCE.getGetTileType_Layer();

        /**
         * The meta object literal for the '<em><b>Style</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__STYLE = eINSTANCE.getGetTileType_Style();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__FORMAT = eINSTANCE.getGetTileType_Format();

        /**
         * The meta object literal for the '<em><b>Dimension Name Value</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_TILE_TYPE__DIMENSION_NAME_VALUE = eINSTANCE.getGetTileType_DimensionNameValue();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Set</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__TILE_MATRIX_SET = eINSTANCE.getGetTileType_TileMatrixSet();

        /**
         * The meta object literal for the '<em><b>Tile Matrix</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__TILE_MATRIX = eINSTANCE.getGetTileType_TileMatrix();

        /**
         * The meta object literal for the '<em><b>Tile Row</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__TILE_ROW = eINSTANCE.getGetTileType_TileRow();

        /**
         * The meta object literal for the '<em><b>Tile Col</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__TILE_COL = eINSTANCE.getGetTileType_TileCol();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__SERVICE = eINSTANCE.getGetTileType_Service();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_TILE_TYPE__VERSION = eINSTANCE.getGetTileType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.LayerTypeImpl <em>Layer Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.LayerTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getLayerType()
         * @generated
         */
        EClass LAYER_TYPE = eINSTANCE.getLayerType();

        /**
         * The meta object literal for the '<em><b>Style</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LAYER_TYPE__STYLE = eINSTANCE.getLayerType_Style();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LAYER_TYPE__FORMAT = eINSTANCE.getLayerType_Format();

        /**
         * The meta object literal for the '<em><b>Info Format</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LAYER_TYPE__INFO_FORMAT = eINSTANCE.getLayerType_InfoFormat();

        /**
         * The meta object literal for the '<em><b>Dimension</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LAYER_TYPE__DIMENSION = eINSTANCE.getLayerType_Dimension();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Set Link</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LAYER_TYPE__TILE_MATRIX_SET_LINK = eINSTANCE.getLayerType_TileMatrixSetLink();

        /**
         * The meta object literal for the '<em><b>Resource URL</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LAYER_TYPE__RESOURCE_URL = eINSTANCE.getLayerType_ResourceURL();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.LegendURLTypeImpl <em>Legend URL Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.LegendURLTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getLegendURLType()
         * @generated
         */
        EClass LEGEND_URL_TYPE = eINSTANCE.getLegendURLType();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LEGEND_URL_TYPE__FORMAT = eINSTANCE.getLegendURLType_Format();

        /**
         * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LEGEND_URL_TYPE__HEIGHT = eINSTANCE.getLegendURLType_Height();

        /**
         * The meta object literal for the '<em><b>Max Scale Denominator</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR = eINSTANCE.getLegendURLType_MaxScaleDenominator();

        /**
         * The meta object literal for the '<em><b>Min Scale Denominator</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR = eINSTANCE.getLegendURLType_MinScaleDenominator();

        /**
         * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LEGEND_URL_TYPE__WIDTH = eINSTANCE.getLegendURLType_Width();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.StyleTypeImpl <em>Style Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.StyleTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getStyleType()
         * @generated
         */
        EClass STYLE_TYPE = eINSTANCE.getStyleType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference STYLE_TYPE__IDENTIFIER = eINSTANCE.getStyleType_Identifier();

        /**
         * The meta object literal for the '<em><b>Legend URL</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference STYLE_TYPE__LEGEND_URL = eINSTANCE.getStyleType_LegendURL();

        /**
         * The meta object literal for the '<em><b>Is Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STYLE_TYPE__IS_DEFAULT = eINSTANCE.getStyleType_IsDefault();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.TextPayloadTypeImpl <em>Text Payload Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.TextPayloadTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTextPayloadType()
         * @generated
         */
        EClass TEXT_PAYLOAD_TYPE = eINSTANCE.getTextPayloadType();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PAYLOAD_TYPE__FORMAT = eINSTANCE.getTextPayloadType_Format();

        /**
         * The meta object literal for the '<em><b>Text Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_PAYLOAD_TYPE__TEXT_CONTENT = eINSTANCE.getTextPayloadType_TextContent();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.ThemesTypeImpl <em>Themes Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.ThemesTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getThemesType()
         * @generated
         */
        EClass THEMES_TYPE = eINSTANCE.getThemesType();

        /**
         * The meta object literal for the '<em><b>Theme</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference THEMES_TYPE__THEME = eINSTANCE.getThemesType_Theme();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.ThemeTypeImpl <em>Theme Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.ThemeTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getThemeType()
         * @generated
         */
        EClass THEME_TYPE = eINSTANCE.getThemeType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference THEME_TYPE__IDENTIFIER = eINSTANCE.getThemeType_Identifier();

        /**
         * The meta object literal for the '<em><b>Theme</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference THEME_TYPE__THEME = eINSTANCE.getThemeType_Theme();

        /**
         * The meta object literal for the '<em><b>Layer Ref</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute THEME_TYPE__LAYER_REF = eINSTANCE.getThemeType_LayerRef();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl <em>Tile Matrix Limits Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixLimitsType()
         * @generated
         */
        EClass TILE_MATRIX_LIMITS_TYPE = eINSTANCE.getTileMatrixLimitsType();

        /**
         * The meta object literal for the '<em><b>Tile Matrix</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_LIMITS_TYPE__TILE_MATRIX = eINSTANCE.getTileMatrixLimitsType_TileMatrix();

        /**
         * The meta object literal for the '<em><b>Min Tile Row</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_LIMITS_TYPE__MIN_TILE_ROW = eINSTANCE.getTileMatrixLimitsType_MinTileRow();

        /**
         * The meta object literal for the '<em><b>Max Tile Row</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_LIMITS_TYPE__MAX_TILE_ROW = eINSTANCE.getTileMatrixLimitsType_MaxTileRow();

        /**
         * The meta object literal for the '<em><b>Min Tile Col</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_LIMITS_TYPE__MIN_TILE_COL = eINSTANCE.getTileMatrixLimitsType_MinTileCol();

        /**
         * The meta object literal for the '<em><b>Max Tile Col</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_LIMITS_TYPE__MAX_TILE_COL = eINSTANCE.getTileMatrixLimitsType_MaxTileCol();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.TileMatrixSetLimitsTypeImpl <em>Tile Matrix Set Limits Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.TileMatrixSetLimitsTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixSetLimitsType()
         * @generated
         */
        EClass TILE_MATRIX_SET_LIMITS_TYPE = eINSTANCE.getTileMatrixSetLimitsType();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Limits</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS = eINSTANCE.getTileMatrixSetLimitsType_TileMatrixLimits();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.TileMatrixSetLinkTypeImpl <em>Tile Matrix Set Link Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.TileMatrixSetLinkTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixSetLinkType()
         * @generated
         */
        EClass TILE_MATRIX_SET_LINK_TYPE = eINSTANCE.getTileMatrixSetLinkType();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Set</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET = eINSTANCE.getTileMatrixSetLinkType_TileMatrixSet();

        /**
         * The meta object literal for the '<em><b>Tile Matrix Set Limits</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS = eINSTANCE.getTileMatrixSetLinkType_TileMatrixSetLimits();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl <em>Tile Matrix Set Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixSetType()
         * @generated
         */
        EClass TILE_MATRIX_SET_TYPE = eINSTANCE.getTileMatrixSetType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TILE_MATRIX_SET_TYPE__IDENTIFIER = eINSTANCE.getTileMatrixSetType_Identifier();

        /**
         * The meta object literal for the '<em><b>Bounding Box Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP = eINSTANCE.getTileMatrixSetType_BoundingBoxGroup();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TILE_MATRIX_SET_TYPE__BOUNDING_BOX = eINSTANCE.getTileMatrixSetType_BoundingBox();

        /**
         * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_SET_TYPE__SUPPORTED_CRS = eINSTANCE.getTileMatrixSetType_SupportedCRS();

        /**
         * The meta object literal for the '<em><b>Well Known Scale Set</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_SET_TYPE__WELL_KNOWN_SCALE_SET = eINSTANCE.getTileMatrixSetType_WellKnownScaleSet();

        /**
         * The meta object literal for the '<em><b>Tile Matrix</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TILE_MATRIX_SET_TYPE__TILE_MATRIX = eINSTANCE.getTileMatrixSetType_TileMatrix();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl <em>Tile Matrix Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.TileMatrixTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTileMatrixType()
         * @generated
         */
        EClass TILE_MATRIX_TYPE = eINSTANCE.getTileMatrixType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TILE_MATRIX_TYPE__IDENTIFIER = eINSTANCE.getTileMatrixType_Identifier();

        /**
         * The meta object literal for the '<em><b>Scale Denominator</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_TYPE__SCALE_DENOMINATOR = eINSTANCE.getTileMatrixType_ScaleDenominator();

        /**
         * The meta object literal for the '<em><b>Top Left Corner</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_TYPE__TOP_LEFT_CORNER = eINSTANCE.getTileMatrixType_TopLeftCorner();

        /**
         * The meta object literal for the '<em><b>Tile Width</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_TYPE__TILE_WIDTH = eINSTANCE.getTileMatrixType_TileWidth();

        /**
         * The meta object literal for the '<em><b>Tile Height</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_TYPE__TILE_HEIGHT = eINSTANCE.getTileMatrixType_TileHeight();

        /**
         * The meta object literal for the '<em><b>Matrix Width</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_TYPE__MATRIX_WIDTH = eINSTANCE.getTileMatrixType_MatrixWidth();

        /**
         * The meta object literal for the '<em><b>Matrix Height</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TILE_MATRIX_TYPE__MATRIX_HEIGHT = eINSTANCE.getTileMatrixType_MatrixHeight();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.impl.URLTemplateTypeImpl <em>URL Template Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.impl.URLTemplateTypeImpl
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getURLTemplateType()
         * @generated
         */
        EClass URL_TEMPLATE_TYPE = eINSTANCE.getURLTemplateType();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute URL_TEMPLATE_TYPE__FORMAT = eINSTANCE.getURLTemplateType_Format();

        /**
         * The meta object literal for the '<em><b>Resource Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute URL_TEMPLATE_TYPE__RESOURCE_TYPE = eINSTANCE.getURLTemplateType_ResourceType();

        /**
         * The meta object literal for the '<em><b>Template</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute URL_TEMPLATE_TYPE__TEMPLATE = eINSTANCE.getURLTemplateType_Template();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.GetCapabilitiesValueType <em>Get Capabilities Value Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.GetCapabilitiesValueType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetCapabilitiesValueType()
         * @generated
         */
        EEnum GET_CAPABILITIES_VALUE_TYPE = eINSTANCE.getGetCapabilitiesValueType();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.GetFeatureInfoValueType <em>Get Feature Info Value Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.GetFeatureInfoValueType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetFeatureInfoValueType()
         * @generated
         */
        EEnum GET_FEATURE_INFO_VALUE_TYPE = eINSTANCE.getGetFeatureInfoValueType();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.GetTileValueType <em>Get Tile Value Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.GetTileValueType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetTileValueType()
         * @generated
         */
        EEnum GET_TILE_VALUE_TYPE = eINSTANCE.getGetTileValueType();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.RequestServiceType <em>Request Service Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.RequestServiceType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getRequestServiceType()
         * @generated
         */
        EEnum REQUEST_SERVICE_TYPE = eINSTANCE.getRequestServiceType();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.ResourceTypeType <em>Resource Type Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.ResourceTypeType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getResourceTypeType()
         * @generated
         */
        EEnum RESOURCE_TYPE_TYPE = eINSTANCE.getResourceTypeType();

        /**
         * The meta object literal for the '{@link net.opengis.wmts.v_1.VersionType <em>Version Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.VersionType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getVersionType()
         * @generated
         */
        EEnum VERSION_TYPE = eINSTANCE.getVersionType();

        /**
         * The meta object literal for the '<em>Accepted Formats Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getAcceptedFormatsType()
         * @generated
         */
        EDataType ACCEPTED_FORMATS_TYPE = eINSTANCE.getAcceptedFormatsType();

        /**
         * The meta object literal for the '<em>Get Capabilities Value Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.GetCapabilitiesValueType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetCapabilitiesValueTypeObject()
         * @generated
         */
        EDataType GET_CAPABILITIES_VALUE_TYPE_OBJECT = eINSTANCE.getGetCapabilitiesValueTypeObject();

        /**
         * The meta object literal for the '<em>Get Feature Info Value Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.GetFeatureInfoValueType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetFeatureInfoValueTypeObject()
         * @generated
         */
        EDataType GET_FEATURE_INFO_VALUE_TYPE_OBJECT = eINSTANCE.getGetFeatureInfoValueTypeObject();

        /**
         * The meta object literal for the '<em>Get Tile Value Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.GetTileValueType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getGetTileValueTypeObject()
         * @generated
         */
        EDataType GET_TILE_VALUE_TYPE_OBJECT = eINSTANCE.getGetTileValueTypeObject();

        /**
         * The meta object literal for the '<em>Request Service Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.RequestServiceType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getRequestServiceTypeObject()
         * @generated
         */
        EDataType REQUEST_SERVICE_TYPE_OBJECT = eINSTANCE.getRequestServiceTypeObject();

        /**
         * The meta object literal for the '<em>Resource Type Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.ResourceTypeType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getResourceTypeTypeObject()
         * @generated
         */
        EDataType RESOURCE_TYPE_TYPE_OBJECT = eINSTANCE.getResourceTypeTypeObject();

        /**
         * The meta object literal for the '<em>Sections Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getSectionsType()
         * @generated
         */
        EDataType SECTIONS_TYPE = eINSTANCE.getSectionsType();

        /**
         * The meta object literal for the '<em>Template Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getTemplateType()
         * @generated
         */
        EDataType TEMPLATE_TYPE = eINSTANCE.getTemplateType();

        /**
         * The meta object literal for the '<em>Version Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wmts.v_1.VersionType
         * @see net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl#getVersionTypeObject()
         * @generated
         */
        EDataType VERSION_TYPE_OBJECT = eINSTANCE.getVersionTypeObject();

    }

} //wmtsv_1Package
