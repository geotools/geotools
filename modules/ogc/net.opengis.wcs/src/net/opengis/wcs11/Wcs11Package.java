/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import net.opengis.ows11.Ows11Package;

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
 * <!-- begin-model-doc -->
 * This XML Schema Document defines the DescribeCoverage operation request and response XML elements and types, used by the OGC Web Coverage Service (WCS).
 * 		Copyright (c) 2007 Open Geospatial Consortium, Inc, All Rights Reserved.
 * This XML Schema Document encodes the elements and types that are shared by multiple WCS operations.
 * 		Copyright (c) 2007 Open Geospatial Consortium, Inc, All Rights Reserved.
 * This XML Schema Document defines a GridCRS element that is much simpler but otherwise similar to a specialization of gml:DerivedCRS. This GridCRS roughly corresponds to the CV_RectifiedGrid class in ISO 19123, without inheritance from CV_Grid. This GridCRS is designed for use by the OGC Web Coverage Service (WCS) and elsewhere.
 * 		This XML Schema Document is not a GML Application Schema, although it uses the GML 3.1.1 profile for WCS 1.1.1.
 * 		Copyright (c) 2007 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document defines interpolation method elements and types, used by the OGC Web Coverage Service (WCS).
 * 		Copyright (c) 2007 Open Geospatial Consortium, Inc, All Rights Reserved.
 * This XML Schema Document includes and imports, directly and indirectly, all the XML Schemas defined by the OWS Common Implemetation Specification.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document encodes the GetResourceByID operation request message. This typical operation is specified as a base for profiling in specific OWS specifications. For information on the allowed changes and limitations in such profiling, see Subclause 9.4.1 of the OWS Common specification.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document encodes the parts of the MD_DataIdentification class of ISO 19115 (OGC Abstract Specification Topic 11) which are expected to be used for most datasets. This Schema also encodes the parts of this class that are expected to be useful for other metadata. Both may be used within the Contents section of OWS service metadata (Capabilities) documents.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document encodes various parameters and parameter types that can be used in OWS operation requests and responses.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
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
 * 
 * This XML Schema Document defines the GetCapabilities operation request and response XML elements and types, which are common to all OWSs. This XML Schema shall be edited by each OWS, for example, to specify a specific value for the "service" attribute.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document encodes the common "ServiceIdentification" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceIdentification class of ISO 19119 (OGC Abstract Specification Topic 12).
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document encodes the common "ServiceProvider" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceProvider class of ISO 19119 (OGC Abstract Specification Topic 12).
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document encodes the basic contents of the "OperationsMetadata" section of the GetCapabilities operation response, also known as the Capabilities XML document.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document encodes the allowed values (or domain) of a quantity, often for an input or output parameter to an OWS. Such a parameter is sometimes called a variable, quantity, literal, or typed literal. Such a parameter can use one of many data types, including double, integer, boolean, string, or URI. The allowed values can also be encoded for a quantity that is not explicit or not transferred, but is constrained by a server implementation.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document encodes the Exception Report response to all OWS operations.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema  Document encodes the typical Contents section of an OWS service metadata (Capabilities) document. This  Schema can be built upon to define the Contents section for a specific OWS. If the ContentsBaseType in this XML Schema cannot be restricted and extended to define the Contents section for a specific OWS, all other relevant parts defined in owsContents.xsd shall be used by the “ContentsType” in the wxsContents.xsd prepared for the specific OWS.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document specifies types and elements for input and output of operation data, allowing including multiple data items with each data item either included or referenced. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document specifies types and elements for document or resource references and for package manifests that contain multiple references. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * <!-- end-model-doc -->
 * @see net.opengis.wcs11.Wcs11Factory
 * @model kind="package"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface Wcs11Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "wcs11";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.opengis.net/wcs/1.1.1";

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
	Wcs11Package eINSTANCE = net.opengis.wcs11.impl.Wcs11PackageImpl.init();

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.AvailableKeysTypeImpl <em>Available Keys Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.AvailableKeysTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getAvailableKeysType()
	 * @generated
	 */
	int AVAILABLE_KEYS_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVAILABLE_KEYS_TYPE__KEY = 0;

	/**
	 * The number of structural features of the '<em>Available Keys Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AVAILABLE_KEYS_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.AxisSubsetTypeImpl <em>Axis Subset Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.AxisSubsetTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getAxisSubsetType()
	 * @generated
	 */
	int AXIS_SUBSET_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_SUBSET_TYPE__IDENTIFIER = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_SUBSET_TYPE__KEY = 1;

	/**
	 * The number of structural features of the '<em>Axis Subset Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_SUBSET_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.AxisTypeImpl <em>Axis Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.AxisTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getAxisType()
	 * @generated
	 */
	int AXIS_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Available Keys</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__AVAILABLE_KEYS = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Meaning</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__MEANING = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Data Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__DATA_TYPE = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>UOM</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__UOM = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Reference System</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__REFERENCE_SYSTEM = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__METADATA = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Axis Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.CapabilitiesTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCapabilitiesType()
	 * @generated
	 */
	int CAPABILITIES_TYPE = 3;

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
	 * The number of structural features of the '<em>Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CAPABILITIES_TYPE_FEATURE_COUNT = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.ContentsTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getContentsType()
	 * @generated
	 */
	int CONTENTS_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Coverage Summary</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENTS_TYPE__COVERAGE_SUMMARY = 0;

	/**
	 * The feature id for the '<em><b>Supported CRS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENTS_TYPE__SUPPORTED_CRS = 1;

	/**
	 * The feature id for the '<em><b>Supported Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENTS_TYPE__SUPPORTED_FORMAT = 2;

	/**
	 * The feature id for the '<em><b>Other Source</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENTS_TYPE__OTHER_SOURCE = 3;

	/**
	 * The number of structural features of the '<em>Contents Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENTS_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.CoverageDescriptionsTypeImpl <em>Coverage Descriptions Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.CoverageDescriptionsTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoverageDescriptionsType()
	 * @generated
	 */
	int COVERAGE_DESCRIPTIONS_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Coverage Description</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION = 0;

	/**
	 * The number of structural features of the '<em>Coverage Descriptions Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTIONS_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.CoverageDescriptionTypeImpl <em>Coverage Description Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.CoverageDescriptionTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoverageDescriptionType()
	 * @generated
	 */
	int COVERAGE_DESCRIPTION_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__METADATA = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__DOMAIN = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__RANGE = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Supported CRS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Supported Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Coverage Description Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DESCRIPTION_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.CoverageDomainTypeImpl <em>Coverage Domain Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.CoverageDomainTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoverageDomainType()
	 * @generated
	 */
	int COVERAGE_DOMAIN_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Spatial Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN = 0;

	/**
	 * The feature id for the '<em><b>Temporal Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN = 1;

	/**
	 * The number of structural features of the '<em>Coverage Domain Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_DOMAIN_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.CoveragesTypeImpl <em>Coverages Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.CoveragesTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoveragesType()
	 * @generated
	 */
	int COVERAGES_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Coverage</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGES_TYPE__COVERAGE = 0;

	/**
	 * The number of structural features of the '<em>Coverages Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGES_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl <em>Coverage Summary Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.CoverageSummaryTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoverageSummaryType()
	 * @generated
	 */
	int COVERAGE_SUMMARY_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__METADATA = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Supported CRS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Supported Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Coverage Summary</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Identifier1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE__IDENTIFIER1 = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Coverage Summary Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COVERAGE_SUMMARY_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.RequestBaseTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getRequestBaseType()
	 * @generated
	 */
	int REQUEST_BASE_TYPE = 25;

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
	 * The meta object id for the '{@link net.opengis.wcs11.impl.DescribeCoverageTypeImpl <em>Describe Coverage Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.DescribeCoverageTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getDescribeCoverageType()
	 * @generated
	 */
	int DESCRIBE_COVERAGE_TYPE = 10;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_COVERAGE_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_COVERAGE_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_COVERAGE_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

	/**
	 * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_COVERAGE_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_COVERAGE_TYPE__IDENTIFIER = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Describe Coverage Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_COVERAGE_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.DocumentRootImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 11;

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
	 * The feature id for the '<em><b>Available Keys</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__AVAILABLE_KEYS = 3;

	/**
	 * The feature id for the '<em><b>Axis Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__AXIS_SUBSET = 4;

	/**
	 * The feature id for the '<em><b>Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__CAPABILITIES = 5;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__CONTENTS = 6;

	/**
	 * The feature id for the '<em><b>Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__COVERAGE = 7;

	/**
	 * The feature id for the '<em><b>Coverage Descriptions</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS = 8;

	/**
	 * The feature id for the '<em><b>Coverages</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__COVERAGES = 9;

	/**
	 * The feature id for the '<em><b>Coverage Summary</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__COVERAGE_SUMMARY = 10;

	/**
	 * The feature id for the '<em><b>Describe Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DESCRIBE_COVERAGE = 11;

	/**
	 * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_CAPABILITIES = 12;

	/**
	 * The feature id for the '<em><b>Get Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_COVERAGE = 13;

	/**
	 * The feature id for the '<em><b>Grid Base CRS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GRID_BASE_CRS = 14;

	/**
	 * The feature id for the '<em><b>Grid CRS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GRID_CRS = 15;

	/**
	 * The feature id for the '<em><b>Grid CS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GRID_CS = 16;

	/**
	 * The feature id for the '<em><b>Grid Offsets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GRID_OFFSETS = 17;

	/**
	 * The feature id for the '<em><b>Grid Origin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GRID_ORIGIN = 18;

	/**
	 * The feature id for the '<em><b>Grid Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GRID_TYPE = 19;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__IDENTIFIER = 20;

	/**
	 * The feature id for the '<em><b>Interpolation Methods</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__INTERPOLATION_METHODS = 21;

	/**
	 * The feature id for the '<em><b>Temporal Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__TEMPORAL_DOMAIN = 22;

	/**
	 * The feature id for the '<em><b>Temporal Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__TEMPORAL_SUBSET = 23;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 24;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.DomainSubsetTypeImpl <em>Domain Subset Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.DomainSubsetTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getDomainSubsetType()
	 * @generated
	 */
	int DOMAIN_SUBSET_TYPE = 12;

	/**
	 * The feature id for the '<em><b>Bounding Box Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOMAIN_SUBSET_TYPE__BOUNDING_BOX_GROUP = 0;

	/**
	 * The feature id for the '<em><b>Bounding Box</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOMAIN_SUBSET_TYPE__BOUNDING_BOX = 1;

	/**
	 * The feature id for the '<em><b>Temporal Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET = 2;

	/**
	 * The number of structural features of the '<em>Domain Subset Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOMAIN_SUBSET_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.FieldSubsetTypeImpl <em>Field Subset Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.FieldSubsetTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getFieldSubsetType()
	 * @generated
	 */
	int FIELD_SUBSET_TYPE = 13;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_SUBSET_TYPE__IDENTIFIER = 0;

	/**
	 * The feature id for the '<em><b>Interpolation Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_SUBSET_TYPE__INTERPOLATION_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Axis Subset</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_SUBSET_TYPE__AXIS_SUBSET = 2;

	/**
	 * The number of structural features of the '<em>Field Subset Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_SUBSET_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.FieldTypeImpl <em>Field Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.FieldTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getFieldType()
	 * @generated
	 */
	int FIELD_TYPE = 14;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE__TITLE = Ows11Package.DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE__ABSTRACT = Ows11Package.DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE__KEYWORDS = Ows11Package.DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE__IDENTIFIER = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE__DEFINITION = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Null Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE__NULL_VALUE = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Interpolation Methods</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE__INTERPOLATION_METHODS = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Axis</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE__AXIS = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Field Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TYPE_FEATURE_COUNT = Ows11Package.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.GetCapabilitiesTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getGetCapabilitiesType()
	 * @generated
	 */
	int GET_CAPABILITIES_TYPE = 15;

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
	 * The meta object id for the '{@link net.opengis.wcs11.impl.GetCoverageTypeImpl <em>Get Coverage Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.GetCoverageTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getGetCoverageType()
	 * @generated
	 */
	int GET_COVERAGE_TYPE = 16;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

	/**
	 * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__IDENTIFIER = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Domain Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__DOMAIN_SUBSET = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Range Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__RANGE_SUBSET = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE__OUTPUT = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Get Coverage Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_COVERAGE_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.GridCrsTypeImpl <em>Grid Crs Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.GridCrsTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getGridCrsType()
	 * @generated
	 */
	int GRID_CRS_TYPE = 17;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_CRS_TYPE__SRS_NAME = 0;

	/**
	 * The feature id for the '<em><b>Grid Base CRS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_CRS_TYPE__GRID_BASE_CRS = 1;

	/**
	 * The feature id for the '<em><b>Grid Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_CRS_TYPE__GRID_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Grid Origin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_CRS_TYPE__GRID_ORIGIN = 3;

	/**
	 * The feature id for the '<em><b>Grid Offsets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_CRS_TYPE__GRID_OFFSETS = 4;

	/**
	 * The feature id for the '<em><b>Grid CS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_CRS_TYPE__GRID_CS = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_CRS_TYPE__ID = 6;

	/**
	 * The number of structural features of the '<em>Grid Crs Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_CRS_TYPE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.ImageCRSRefTypeImpl <em>Image CRS Ref Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.ImageCRSRefTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getImageCRSRefType()
	 * @generated
	 */
	int IMAGE_CRS_REF_TYPE = 18;

	/**
	 * The feature id for the '<em><b>Image CRS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE_CRS_REF_TYPE__IMAGE_CRS = 0;

	/**
	 * The number of structural features of the '<em>Image CRS Ref Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE_CRS_REF_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.InterpolationMethodBaseTypeImpl <em>Interpolation Method Base Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.InterpolationMethodBaseTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getInterpolationMethodBaseType()
	 * @generated
	 */
	int INTERPOLATION_METHOD_BASE_TYPE = 19;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHOD_BASE_TYPE__VALUE = Ows11Package.CODE_TYPE__VALUE;

	/**
	 * The feature id for the '<em><b>Code Space</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHOD_BASE_TYPE__CODE_SPACE = Ows11Package.CODE_TYPE__CODE_SPACE;

	/**
	 * The number of structural features of the '<em>Interpolation Method Base Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHOD_BASE_TYPE_FEATURE_COUNT = Ows11Package.CODE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.InterpolationMethodsTypeImpl <em>Interpolation Methods Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.InterpolationMethodsTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getInterpolationMethodsType()
	 * @generated
	 */
	int INTERPOLATION_METHODS_TYPE = 20;

	/**
	 * The feature id for the '<em><b>Interpolation Method</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHODS_TYPE__INTERPOLATION_METHOD = 0;

	/**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHODS_TYPE__DEFAULT = 1;

	/**
	 * The number of structural features of the '<em>Interpolation Methods Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHODS_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.InterpolationMethodTypeImpl <em>Interpolation Method Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.InterpolationMethodTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getInterpolationMethodType()
	 * @generated
	 */
	int INTERPOLATION_METHOD_TYPE = 21;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHOD_TYPE__VALUE = INTERPOLATION_METHOD_BASE_TYPE__VALUE;

	/**
	 * The feature id for the '<em><b>Code Space</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHOD_TYPE__CODE_SPACE = INTERPOLATION_METHOD_BASE_TYPE__CODE_SPACE;

	/**
	 * The feature id for the '<em><b>Null Resistance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE = INTERPOLATION_METHOD_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Interpolation Method Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERPOLATION_METHOD_TYPE_FEATURE_COUNT = INTERPOLATION_METHOD_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.OutputTypeImpl <em>Output Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.OutputTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getOutputType()
	 * @generated
	 */
	int OUTPUT_TYPE = 22;

	/**
	 * The feature id for the '<em><b>Grid CRS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TYPE__GRID_CRS = 0;

	/**
	 * The feature id for the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TYPE__FORMAT = 1;

	/**
	 * The feature id for the '<em><b>Store</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TYPE__STORE = 2;

	/**
	 * The number of structural features of the '<em>Output Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.RangeSubsetTypeImpl <em>Range Subset Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.RangeSubsetTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getRangeSubsetType()
	 * @generated
	 */
	int RANGE_SUBSET_TYPE = 23;

	/**
	 * The feature id for the '<em><b>Field Subset</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANGE_SUBSET_TYPE__FIELD_SUBSET = 0;

	/**
	 * The number of structural features of the '<em>Range Subset Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANGE_SUBSET_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.RangeTypeImpl <em>Range Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.RangeTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getRangeType()
	 * @generated
	 */
	int RANGE_TYPE = 24;

	/**
	 * The feature id for the '<em><b>Field</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANGE_TYPE__FIELD = 0;

	/**
	 * The number of structural features of the '<em>Range Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RANGE_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.SpatialDomainTypeImpl <em>Spatial Domain Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.SpatialDomainTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getSpatialDomainType()
	 * @generated
	 */
	int SPATIAL_DOMAIN_TYPE = 26;

	/**
	 * The feature id for the '<em><b>Bounding Box Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_DOMAIN_TYPE__BOUNDING_BOX_GROUP = 0;

	/**
	 * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_DOMAIN_TYPE__BOUNDING_BOX = 1;

	/**
	 * The feature id for the '<em><b>Grid CRS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_DOMAIN_TYPE__GRID_CRS = 2;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_DOMAIN_TYPE__TRANSFORMATION = 3;

	/**
	 * The feature id for the '<em><b>Image CRS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_DOMAIN_TYPE__IMAGE_CRS = 4;

	/**
	 * The feature id for the '<em><b>Polygon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_DOMAIN_TYPE__POLYGON = 5;

	/**
	 * The number of structural features of the '<em>Spatial Domain Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_DOMAIN_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wcs11.impl.TimePeriodTypeImpl <em>Time Period Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.TimePeriodTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getTimePeriodType()
	 * @generated
	 */
	int TIME_PERIOD_TYPE = 27;

	/**
	 * The feature id for the '<em><b>Begin Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_PERIOD_TYPE__BEGIN_POSITION = 0;

	/**
	 * The feature id for the '<em><b>End Position</b></em>' attribute.
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
	 * The meta object id for the '{@link net.opengis.wcs11.impl.TimeSequenceTypeImpl <em>Time Sequence Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wcs11.impl.TimeSequenceTypeImpl
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getTimeSequenceType()
	 * @generated
	 */
	int TIME_SEQUENCE_TYPE = 28;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_SEQUENCE_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Time Position</b></em>' attribute.
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
	 * The meta object id for the '<em>Identifier Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getIdentifierType()
	 * @generated
	 */
	int IDENTIFIER_TYPE = 29;

	/**
	 * The meta object id for the '<em>Interpolation Method Base Type Base</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getInterpolationMethodBaseTypeBase()
	 * @generated
	 */
	int INTERPOLATION_METHOD_BASE_TYPE_BASE = 30;

	/**
	 * The meta object id for the '<em>Time Duration Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getTimeDurationType()
	 * @generated
	 */
	int TIME_DURATION_TYPE = 31;

	/**
	 * The meta object id for the '<em>Map</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.Map
	 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getMap()
	 * @generated
	 */
	int MAP = 32;


	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.AvailableKeysType <em>Available Keys Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Available Keys Type</em>'.
	 * @see net.opengis.wcs11.AvailableKeysType
	 * @generated
	 */
	EClass getAvailableKeysType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.AvailableKeysType#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see net.opengis.wcs11.AvailableKeysType#getKey()
	 * @see #getAvailableKeysType()
	 * @generated
	 */
	EAttribute getAvailableKeysType_Key();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.AxisSubsetType <em>Axis Subset Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Axis Subset Type</em>'.
	 * @see net.opengis.wcs11.AxisSubsetType
	 * @generated
	 */
	EClass getAxisSubsetType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.AxisSubsetType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.AxisSubsetType#getIdentifier()
	 * @see #getAxisSubsetType()
	 * @generated
	 */
	EAttribute getAxisSubsetType_Identifier();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.AxisSubsetType#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see net.opengis.wcs11.AxisSubsetType#getKey()
	 * @see #getAxisSubsetType()
	 * @generated
	 */
	EAttribute getAxisSubsetType_Key();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.AxisType <em>Axis Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Axis Type</em>'.
	 * @see net.opengis.wcs11.AxisType
	 * @generated
	 */
	EClass getAxisType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.AxisType#getAvailableKeys <em>Available Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Available Keys</em>'.
	 * @see net.opengis.wcs11.AxisType#getAvailableKeys()
	 * @see #getAxisType()
	 * @generated
	 */
	EReference getAxisType_AvailableKeys();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.AxisType#getMeaning <em>Meaning</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Meaning</em>'.
	 * @see net.opengis.wcs11.AxisType#getMeaning()
	 * @see #getAxisType()
	 * @generated
	 */
	EReference getAxisType_Meaning();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.AxisType#getDataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data Type</em>'.
	 * @see net.opengis.wcs11.AxisType#getDataType()
	 * @see #getAxisType()
	 * @generated
	 */
	EReference getAxisType_DataType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.AxisType#getUOM <em>UOM</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>UOM</em>'.
	 * @see net.opengis.wcs11.AxisType#getUOM()
	 * @see #getAxisType()
	 * @generated
	 */
	EReference getAxisType_UOM();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.AxisType#getReferenceSystem <em>Reference System</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Reference System</em>'.
	 * @see net.opengis.wcs11.AxisType#getReferenceSystem()
	 * @see #getAxisType()
	 * @generated
	 */
	EReference getAxisType_ReferenceSystem();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.AxisType#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Metadata</em>'.
	 * @see net.opengis.wcs11.AxisType#getMetadata()
	 * @see #getAxisType()
	 * @generated
	 */
	EReference getAxisType_Metadata();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.AxisType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.AxisType#getIdentifier()
	 * @see #getAxisType()
	 * @generated
	 */
	EAttribute getAxisType_Identifier();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.CapabilitiesType <em>Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Capabilities Type</em>'.
	 * @see net.opengis.wcs11.CapabilitiesType
	 * @generated
	 */
	EClass getCapabilitiesType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.CapabilitiesType#getContents <em>Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Contents</em>'.
	 * @see net.opengis.wcs11.CapabilitiesType#getContents()
	 * @see #getCapabilitiesType()
	 * @generated
	 */
	EReference getCapabilitiesType_Contents();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.ContentsType <em>Contents Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contents Type</em>'.
	 * @see net.opengis.wcs11.ContentsType
	 * @generated
	 */
	EClass getContentsType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.ContentsType#getCoverageSummary <em>Coverage Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Coverage Summary</em>'.
	 * @see net.opengis.wcs11.ContentsType#getCoverageSummary()
	 * @see #getContentsType()
	 * @generated
	 */
	EReference getContentsType_CoverageSummary();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.ContentsType#getSupportedCRS <em>Supported CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Supported CRS</em>'.
	 * @see net.opengis.wcs11.ContentsType#getSupportedCRS()
	 * @see #getContentsType()
	 * @generated
	 */
	EAttribute getContentsType_SupportedCRS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.ContentsType#getSupportedFormat <em>Supported Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Supported Format</em>'.
	 * @see net.opengis.wcs11.ContentsType#getSupportedFormat()
	 * @see #getContentsType()
	 * @generated
	 */
	EAttribute getContentsType_SupportedFormat();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.ContentsType#getOtherSource <em>Other Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Other Source</em>'.
	 * @see net.opengis.wcs11.ContentsType#getOtherSource()
	 * @see #getContentsType()
	 * @generated
	 */
	EReference getContentsType_OtherSource();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.CoverageDescriptionsType <em>Coverage Descriptions Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coverage Descriptions Type</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionsType
	 * @generated
	 */
	EClass getCoverageDescriptionsType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.CoverageDescriptionsType#getCoverageDescription <em>Coverage Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Coverage Description</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionsType#getCoverageDescription()
	 * @see #getCoverageDescriptionsType()
	 * @generated
	 */
	EReference getCoverageDescriptionsType_CoverageDescription();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.CoverageDescriptionType <em>Coverage Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coverage Description Type</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionType
	 * @generated
	 */
	EClass getCoverageDescriptionType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.CoverageDescriptionType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionType#getIdentifier()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
	EAttribute getCoverageDescriptionType_Identifier();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.CoverageDescriptionType#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Metadata</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionType#getMetadata()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
	EReference getCoverageDescriptionType_Metadata();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.CoverageDescriptionType#getDomain <em>Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Domain</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionType#getDomain()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
	EReference getCoverageDescriptionType_Domain();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.CoverageDescriptionType#getRange <em>Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Range</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionType#getRange()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
	EReference getCoverageDescriptionType_Range();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.CoverageDescriptionType#getSupportedCRS <em>Supported CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Supported CRS</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionType#getSupportedCRS()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
	EAttribute getCoverageDescriptionType_SupportedCRS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.CoverageDescriptionType#getSupportedFormat <em>Supported Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Supported Format</em>'.
	 * @see net.opengis.wcs11.CoverageDescriptionType#getSupportedFormat()
	 * @see #getCoverageDescriptionType()
	 * @generated
	 */
	EAttribute getCoverageDescriptionType_SupportedFormat();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.CoverageDomainType <em>Coverage Domain Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coverage Domain Type</em>'.
	 * @see net.opengis.wcs11.CoverageDomainType
	 * @generated
	 */
	EClass getCoverageDomainType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.CoverageDomainType#getSpatialDomain <em>Spatial Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Spatial Domain</em>'.
	 * @see net.opengis.wcs11.CoverageDomainType#getSpatialDomain()
	 * @see #getCoverageDomainType()
	 * @generated
	 */
	EReference getCoverageDomainType_SpatialDomain();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.CoverageDomainType#getTemporalDomain <em>Temporal Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Domain</em>'.
	 * @see net.opengis.wcs11.CoverageDomainType#getTemporalDomain()
	 * @see #getCoverageDomainType()
	 * @generated
	 */
	EReference getCoverageDomainType_TemporalDomain();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.CoveragesType <em>Coverages Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coverages Type</em>'.
	 * @see net.opengis.wcs11.CoveragesType
	 * @generated
	 */
	EClass getCoveragesType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.CoveragesType#getCoverage <em>Coverage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Coverage</em>'.
	 * @see net.opengis.wcs11.CoveragesType#getCoverage()
	 * @see #getCoveragesType()
	 * @generated
	 */
	EReference getCoveragesType_Coverage();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.CoverageSummaryType <em>Coverage Summary Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coverage Summary Type</em>'.
	 * @see net.opengis.wcs11.CoverageSummaryType
	 * @generated
	 */
	EClass getCoverageSummaryType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.CoverageSummaryType#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Metadata</em>'.
	 * @see net.opengis.wcs11.CoverageSummaryType#getMetadata()
	 * @see #getCoverageSummaryType()
	 * @generated
	 */
	EReference getCoverageSummaryType_Metadata();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.CoverageSummaryType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>WGS84 Bounding Box</em>'.
	 * @see net.opengis.wcs11.CoverageSummaryType#getWGS84BoundingBox()
	 * @see #getCoverageSummaryType()
	 * @generated
	 */
	EReference getCoverageSummaryType_WGS84BoundingBox();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.CoverageSummaryType#getSupportedCRS <em>Supported CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Supported CRS</em>'.
	 * @see net.opengis.wcs11.CoverageSummaryType#getSupportedCRS()
	 * @see #getCoverageSummaryType()
	 * @generated
	 */
	EAttribute getCoverageSummaryType_SupportedCRS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.CoverageSummaryType#getSupportedFormat <em>Supported Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Supported Format</em>'.
	 * @see net.opengis.wcs11.CoverageSummaryType#getSupportedFormat()
	 * @see #getCoverageSummaryType()
	 * @generated
	 */
	EAttribute getCoverageSummaryType_SupportedFormat();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.CoverageSummaryType#getCoverageSummary <em>Coverage Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Coverage Summary</em>'.
	 * @see net.opengis.wcs11.CoverageSummaryType#getCoverageSummary()
	 * @see #getCoverageSummaryType()
	 * @generated
	 */
	EReference getCoverageSummaryType_CoverageSummary();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.CoverageSummaryType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.CoverageSummaryType#getIdentifier()
	 * @see #getCoverageSummaryType()
	 * @generated
	 */
	EAttribute getCoverageSummaryType_Identifier();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.CoverageSummaryType#getIdentifier1 <em>Identifier1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier1</em>'.
	 * @see net.opengis.wcs11.CoverageSummaryType#getIdentifier1()
	 * @see #getCoverageSummaryType()
	 * @generated
	 */
	EAttribute getCoverageSummaryType_Identifier1();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.DescribeCoverageType <em>Describe Coverage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Describe Coverage Type</em>'.
	 * @see net.opengis.wcs11.DescribeCoverageType
	 * @generated
	 */
	EClass getDescribeCoverageType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.DescribeCoverageType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.DescribeCoverageType#getIdentifier()
	 * @see #getDescribeCoverageType()
	 * @generated
	 */
	EAttribute getDescribeCoverageType_Identifier();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see net.opengis.wcs11.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs11.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link net.opengis.wcs11.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link net.opengis.wcs11.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getAvailableKeys <em>Available Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Available Keys</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getAvailableKeys()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_AvailableKeys();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getAxisSubset <em>Axis Subset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Axis Subset</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getAxisSubset()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_AxisSubset();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getCapabilities <em>Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Capabilities</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Capabilities();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getContents <em>Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Contents</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getContents()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Contents();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getCoverage <em>Coverage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Coverage</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getCoverage()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Coverage();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getCoverageDescriptions <em>Coverage Descriptions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Coverage Descriptions</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getCoverageDescriptions()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_CoverageDescriptions();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getCoverages <em>Coverages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Coverages</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getCoverages()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Coverages();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getCoverageSummary <em>Coverage Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Coverage Summary</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getCoverageSummary()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_CoverageSummary();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Describe Coverage</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getDescribeCoverage()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DescribeCoverage();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getGetCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetCapabilities();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getGetCoverage <em>Get Coverage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Coverage</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getGetCoverage()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetCoverage();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.DocumentRoot#getGridBaseCRS <em>Grid Base CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid Base CRS</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getGridBaseCRS()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_GridBaseCRS();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getGridCRS <em>Grid CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Grid CRS</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getGridCRS()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GridCRS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.DocumentRoot#getGridCS <em>Grid CS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid CS</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getGridCS()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_GridCS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.DocumentRoot#getGridOffsets <em>Grid Offsets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid Offsets</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getGridOffsets()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_GridOffsets();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.DocumentRoot#getGridOrigin <em>Grid Origin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid Origin</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getGridOrigin()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_GridOrigin();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.DocumentRoot#getGridType <em>Grid Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid Type</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getGridType()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_GridType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.DocumentRoot#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getIdentifier()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Identifier();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getInterpolationMethods <em>Interpolation Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Interpolation Methods</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getInterpolationMethods()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_InterpolationMethods();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getTemporalDomain <em>Temporal Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Domain</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getTemporalDomain()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_TemporalDomain();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DocumentRoot#getTemporalSubset <em>Temporal Subset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Subset</em>'.
	 * @see net.opengis.wcs11.DocumentRoot#getTemporalSubset()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_TemporalSubset();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.DomainSubsetType <em>Domain Subset Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Domain Subset Type</em>'.
	 * @see net.opengis.wcs11.DomainSubsetType
	 * @generated
	 */
	EClass getDomainSubsetType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs11.DomainSubsetType#getBoundingBoxGroup <em>Bounding Box Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Bounding Box Group</em>'.
	 * @see net.opengis.wcs11.DomainSubsetType#getBoundingBoxGroup()
	 * @see #getDomainSubsetType()
	 * @generated
	 */
	EAttribute getDomainSubsetType_BoundingBoxGroup();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DomainSubsetType#getBoundingBox <em>Bounding Box</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Bounding Box</em>'.
	 * @see net.opengis.wcs11.DomainSubsetType#getBoundingBox()
	 * @see #getDomainSubsetType()
	 * @generated
	 */
	EReference getDomainSubsetType_BoundingBox();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.DomainSubsetType#getTemporalSubset <em>Temporal Subset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temporal Subset</em>'.
	 * @see net.opengis.wcs11.DomainSubsetType#getTemporalSubset()
	 * @see #getDomainSubsetType()
	 * @generated
	 */
	EReference getDomainSubsetType_TemporalSubset();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.FieldSubsetType <em>Field Subset Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Field Subset Type</em>'.
	 * @see net.opengis.wcs11.FieldSubsetType
	 * @generated
	 */
	EClass getFieldSubsetType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.FieldSubsetType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.FieldSubsetType#getIdentifier()
	 * @see #getFieldSubsetType()
	 * @generated
	 */
	EReference getFieldSubsetType_Identifier();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.FieldSubsetType#getInterpolationType <em>Interpolation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interpolation Type</em>'.
	 * @see net.opengis.wcs11.FieldSubsetType#getInterpolationType()
	 * @see #getFieldSubsetType()
	 * @generated
	 */
	EAttribute getFieldSubsetType_InterpolationType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.FieldSubsetType#getAxisSubset <em>Axis Subset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Axis Subset</em>'.
	 * @see net.opengis.wcs11.FieldSubsetType#getAxisSubset()
	 * @see #getFieldSubsetType()
	 * @generated
	 */
	EReference getFieldSubsetType_AxisSubset();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.FieldType <em>Field Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Field Type</em>'.
	 * @see net.opengis.wcs11.FieldType
	 * @generated
	 */
	EClass getFieldType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.FieldType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.FieldType#getIdentifier()
	 * @see #getFieldType()
	 * @generated
	 */
	EAttribute getFieldType_Identifier();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.FieldType#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Definition</em>'.
	 * @see net.opengis.wcs11.FieldType#getDefinition()
	 * @see #getFieldType()
	 * @generated
	 */
	EReference getFieldType_Definition();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.FieldType#getNullValue <em>Null Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Null Value</em>'.
	 * @see net.opengis.wcs11.FieldType#getNullValue()
	 * @see #getFieldType()
	 * @generated
	 */
	EReference getFieldType_NullValue();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.FieldType#getInterpolationMethods <em>Interpolation Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Interpolation Methods</em>'.
	 * @see net.opengis.wcs11.FieldType#getInterpolationMethods()
	 * @see #getFieldType()
	 * @generated
	 */
	EReference getFieldType_InterpolationMethods();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.FieldType#getAxis <em>Axis</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Axis</em>'.
	 * @see net.opengis.wcs11.FieldType#getAxis()
	 * @see #getFieldType()
	 * @generated
	 */
	EReference getFieldType_Axis();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Capabilities Type</em>'.
	 * @see net.opengis.wcs11.GetCapabilitiesType
	 * @generated
	 */
	EClass getGetCapabilitiesType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.GetCapabilitiesType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wcs11.GetCapabilitiesType#getService()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
	EAttribute getGetCapabilitiesType_Service();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.GetCoverageType <em>Get Coverage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Coverage Type</em>'.
	 * @see net.opengis.wcs11.GetCoverageType
	 * @generated
	 */
	EClass getGetCoverageType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.GetCoverageType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Identifier</em>'.
	 * @see net.opengis.wcs11.GetCoverageType#getIdentifier()
	 * @see #getGetCoverageType()
	 * @generated
	 */
	EReference getGetCoverageType_Identifier();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.GetCoverageType#getDomainSubset <em>Domain Subset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Domain Subset</em>'.
	 * @see net.opengis.wcs11.GetCoverageType#getDomainSubset()
	 * @see #getGetCoverageType()
	 * @generated
	 */
	EReference getGetCoverageType_DomainSubset();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.GetCoverageType#getRangeSubset <em>Range Subset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Range Subset</em>'.
	 * @see net.opengis.wcs11.GetCoverageType#getRangeSubset()
	 * @see #getGetCoverageType()
	 * @generated
	 */
	EReference getGetCoverageType_RangeSubset();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.GetCoverageType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output</em>'.
	 * @see net.opengis.wcs11.GetCoverageType#getOutput()
	 * @see #getGetCoverageType()
	 * @generated
	 */
	EReference getGetCoverageType_Output();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.GridCrsType <em>Grid Crs Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Grid Crs Type</em>'.
	 * @see net.opengis.wcs11.GridCrsType
	 * @generated
	 */
	EClass getGridCrsType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.GridCrsType#getSrsName <em>Srs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Srs Name</em>'.
	 * @see net.opengis.wcs11.GridCrsType#getSrsName()
	 * @see #getGridCrsType()
	 * @generated
	 */
	EAttribute getGridCrsType_SrsName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.GridCrsType#getGridBaseCRS <em>Grid Base CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid Base CRS</em>'.
	 * @see net.opengis.wcs11.GridCrsType#getGridBaseCRS()
	 * @see #getGridCrsType()
	 * @generated
	 */
	EAttribute getGridCrsType_GridBaseCRS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.GridCrsType#getGridType <em>Grid Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid Type</em>'.
	 * @see net.opengis.wcs11.GridCrsType#getGridType()
	 * @see #getGridCrsType()
	 * @generated
	 */
	EAttribute getGridCrsType_GridType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.GridCrsType#getGridOrigin <em>Grid Origin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid Origin</em>'.
	 * @see net.opengis.wcs11.GridCrsType#getGridOrigin()
	 * @see #getGridCrsType()
	 * @generated
	 */
	EAttribute getGridCrsType_GridOrigin();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.GridCrsType#getGridOffsets <em>Grid Offsets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid Offsets</em>'.
	 * @see net.opengis.wcs11.GridCrsType#getGridOffsets()
	 * @see #getGridCrsType()
	 * @generated
	 */
	EAttribute getGridCrsType_GridOffsets();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.GridCrsType#getGridCS <em>Grid CS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grid CS</em>'.
	 * @see net.opengis.wcs11.GridCrsType#getGridCS()
	 * @see #getGridCrsType()
	 * @generated
	 */
	EAttribute getGridCrsType_GridCS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.GridCrsType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.opengis.wcs11.GridCrsType#getId()
	 * @see #getGridCrsType()
	 * @generated
	 */
	EAttribute getGridCrsType_Id();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.ImageCRSRefType <em>Image CRS Ref Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Image CRS Ref Type</em>'.
	 * @see net.opengis.wcs11.ImageCRSRefType
	 * @generated
	 */
	EClass getImageCRSRefType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.ImageCRSRefType#getImageCRS <em>Image CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Image CRS</em>'.
	 * @see net.opengis.wcs11.ImageCRSRefType#getImageCRS()
	 * @see #getImageCRSRefType()
	 * @generated
	 */
	EAttribute getImageCRSRefType_ImageCRS();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.InterpolationMethodBaseType <em>Interpolation Method Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interpolation Method Base Type</em>'.
	 * @see net.opengis.wcs11.InterpolationMethodBaseType
	 * @generated
	 */
	EClass getInterpolationMethodBaseType();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.InterpolationMethodsType <em>Interpolation Methods Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interpolation Methods Type</em>'.
	 * @see net.opengis.wcs11.InterpolationMethodsType
	 * @generated
	 */
	EClass getInterpolationMethodsType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.InterpolationMethodsType#getInterpolationMethod <em>Interpolation Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Interpolation Method</em>'.
	 * @see net.opengis.wcs11.InterpolationMethodsType#getInterpolationMethod()
	 * @see #getInterpolationMethodsType()
	 * @generated
	 */
	EReference getInterpolationMethodsType_InterpolationMethod();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.InterpolationMethodsType#getDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see net.opengis.wcs11.InterpolationMethodsType#getDefault()
	 * @see #getInterpolationMethodsType()
	 * @generated
	 */
	EAttribute getInterpolationMethodsType_Default();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.InterpolationMethodType <em>Interpolation Method Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interpolation Method Type</em>'.
	 * @see net.opengis.wcs11.InterpolationMethodType
	 * @generated
	 */
	EClass getInterpolationMethodType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.InterpolationMethodType#getNullResistance <em>Null Resistance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Null Resistance</em>'.
	 * @see net.opengis.wcs11.InterpolationMethodType#getNullResistance()
	 * @see #getInterpolationMethodType()
	 * @generated
	 */
	EAttribute getInterpolationMethodType_NullResistance();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.OutputType <em>Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Type</em>'.
	 * @see net.opengis.wcs11.OutputType
	 * @generated
	 */
	EClass getOutputType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.OutputType#getGridCRS <em>Grid CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Grid CRS</em>'.
	 * @see net.opengis.wcs11.OutputType#getGridCRS()
	 * @see #getOutputType()
	 * @generated
	 */
	EReference getOutputType_GridCRS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.OutputType#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Format</em>'.
	 * @see net.opengis.wcs11.OutputType#getFormat()
	 * @see #getOutputType()
	 * @generated
	 */
	EAttribute getOutputType_Format();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.OutputType#isStore <em>Store</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Store</em>'.
	 * @see net.opengis.wcs11.OutputType#isStore()
	 * @see #getOutputType()
	 * @generated
	 */
	EAttribute getOutputType_Store();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.RangeSubsetType <em>Range Subset Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Range Subset Type</em>'.
	 * @see net.opengis.wcs11.RangeSubsetType
	 * @generated
	 */
	EClass getRangeSubsetType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.RangeSubsetType#getFieldSubset <em>Field Subset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Field Subset</em>'.
	 * @see net.opengis.wcs11.RangeSubsetType#getFieldSubset()
	 * @see #getRangeSubsetType()
	 * @generated
	 */
	EReference getRangeSubsetType_FieldSubset();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.RangeType <em>Range Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Range Type</em>'.
	 * @see net.opengis.wcs11.RangeType
	 * @generated
	 */
	EClass getRangeType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.RangeType#getField <em>Field</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Field</em>'.
	 * @see net.opengis.wcs11.RangeType#getField()
	 * @see #getRangeType()
	 * @generated
	 */
	EReference getRangeType_Field();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.RequestBaseType <em>Request Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Request Base Type</em>'.
	 * @see net.opengis.wcs11.RequestBaseType
	 * @generated
	 */
	EClass getRequestBaseType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.RequestBaseType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wcs11.RequestBaseType#getService()
	 * @see #getRequestBaseType()
	 * @generated
	 */
	EAttribute getRequestBaseType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.RequestBaseType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wcs11.RequestBaseType#getVersion()
	 * @see #getRequestBaseType()
	 * @generated
	 */
	EAttribute getRequestBaseType_Version();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.RequestBaseType#getBaseUrl <em>Base Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Url</em>'.
	 * @see net.opengis.wcs11.RequestBaseType#getBaseUrl()
	 * @see #getRequestBaseType()
	 * @generated
	 */
	EAttribute getRequestBaseType_BaseUrl();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.RequestBaseType#getExtendedProperties <em>Extended Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extended Properties</em>'.
	 * @see net.opengis.wcs11.RequestBaseType#getExtendedProperties()
	 * @see #getRequestBaseType()
	 * @generated
	 */
	EAttribute getRequestBaseType_ExtendedProperties();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.SpatialDomainType <em>Spatial Domain Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spatial Domain Type</em>'.
	 * @see net.opengis.wcs11.SpatialDomainType
	 * @generated
	 */
	EClass getSpatialDomainType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs11.SpatialDomainType#getBoundingBoxGroup <em>Bounding Box Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Bounding Box Group</em>'.
	 * @see net.opengis.wcs11.SpatialDomainType#getBoundingBoxGroup()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
	EAttribute getSpatialDomainType_BoundingBoxGroup();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.SpatialDomainType#getBoundingBox <em>Bounding Box</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bounding Box</em>'.
	 * @see net.opengis.wcs11.SpatialDomainType#getBoundingBox()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
	EReference getSpatialDomainType_BoundingBox();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.SpatialDomainType#getGridCRS <em>Grid CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Grid CRS</em>'.
	 * @see net.opengis.wcs11.SpatialDomainType#getGridCRS()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
	EReference getSpatialDomainType_GridCRS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.SpatialDomainType#getTransformation <em>Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transformation</em>'.
	 * @see net.opengis.wcs11.SpatialDomainType#getTransformation()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
	EAttribute getSpatialDomainType_Transformation();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wcs11.SpatialDomainType#getImageCRS <em>Image CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Image CRS</em>'.
	 * @see net.opengis.wcs11.SpatialDomainType#getImageCRS()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
	EReference getSpatialDomainType_ImageCRS();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.SpatialDomainType#getPolygon <em>Polygon</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Polygon</em>'.
	 * @see net.opengis.wcs11.SpatialDomainType#getPolygon()
	 * @see #getSpatialDomainType()
	 * @generated
	 */
	EAttribute getSpatialDomainType_Polygon();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.TimePeriodType <em>Time Period Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Period Type</em>'.
	 * @see net.opengis.wcs11.TimePeriodType
	 * @generated
	 */
	EClass getTimePeriodType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.TimePeriodType#getBeginPosition <em>Begin Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Begin Position</em>'.
	 * @see net.opengis.wcs11.TimePeriodType#getBeginPosition()
	 * @see #getTimePeriodType()
	 * @generated
	 */
	EAttribute getTimePeriodType_BeginPosition();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.TimePeriodType#getEndPosition <em>End Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Position</em>'.
	 * @see net.opengis.wcs11.TimePeriodType#getEndPosition()
	 * @see #getTimePeriodType()
	 * @generated
	 */
	EAttribute getTimePeriodType_EndPosition();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.TimePeriodType#getTimeResolution <em>Time Resolution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Resolution</em>'.
	 * @see net.opengis.wcs11.TimePeriodType#getTimeResolution()
	 * @see #getTimePeriodType()
	 * @generated
	 */
	EAttribute getTimePeriodType_TimeResolution();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.TimePeriodType#getFrame <em>Frame</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Frame</em>'.
	 * @see net.opengis.wcs11.TimePeriodType#getFrame()
	 * @see #getTimePeriodType()
	 * @generated
	 */
	EAttribute getTimePeriodType_Frame();

	/**
	 * Returns the meta object for class '{@link net.opengis.wcs11.TimeSequenceType <em>Time Sequence Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Sequence Type</em>'.
	 * @see net.opengis.wcs11.TimeSequenceType
	 * @generated
	 */
	EClass getTimeSequenceType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wcs11.TimeSequenceType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wcs11.TimeSequenceType#getGroup()
	 * @see #getTimeSequenceType()
	 * @generated
	 */
	EAttribute getTimeSequenceType_Group();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wcs11.TimeSequenceType#getTimePosition <em>Time Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Position</em>'.
	 * @see net.opengis.wcs11.TimeSequenceType#getTimePosition()
	 * @see #getTimeSequenceType()
	 * @generated
	 */
	EAttribute getTimeSequenceType_TimePosition();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wcs11.TimeSequenceType#getTimePeriod <em>Time Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Time Period</em>'.
	 * @see net.opengis.wcs11.TimeSequenceType#getTimePeriod()
	 * @see #getTimeSequenceType()
	 * @generated
	 */
	EReference getTimeSequenceType_TimePeriod();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Identifier Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Identifier Type</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='IdentifierType' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='.+'"
	 * @generated
	 */
	EDataType getIdentifierType();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Interpolation Method Base Type Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Interpolation Method Base Type Base</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='InterpolationMethodBaseType_._base' baseType='http://www.eclipse.org/emf/2003/XMLType#string'"
	 * @generated
	 */
	EDataType getInterpolationMethodBaseTypeBase();

	/**
	 * Returns the meta object for data type '{@link java.lang.Object <em>Time Duration Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Time Duration Type</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object"
	 *        extendedMetaData="name='TimeDurationType' memberTypes='http://www.eclipse.org/emf/2003/XMLType#duration http://www.eclipse.org/emf/2003/XMLType#decimal'"
	 * @generated
	 */
	EDataType getTimeDurationType();

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
	Wcs11Factory getWcs11Factory();

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
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.AvailableKeysTypeImpl <em>Available Keys Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.AvailableKeysTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getAvailableKeysType()
		 * @generated
		 */
		EClass AVAILABLE_KEYS_TYPE = eINSTANCE.getAvailableKeysType();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AVAILABLE_KEYS_TYPE__KEY = eINSTANCE.getAvailableKeysType_Key();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.AxisSubsetTypeImpl <em>Axis Subset Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.AxisSubsetTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getAxisSubsetType()
		 * @generated
		 */
		EClass AXIS_SUBSET_TYPE = eINSTANCE.getAxisSubsetType();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AXIS_SUBSET_TYPE__IDENTIFIER = eINSTANCE.getAxisSubsetType_Identifier();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AXIS_SUBSET_TYPE__KEY = eINSTANCE.getAxisSubsetType_Key();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.AxisTypeImpl <em>Axis Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.AxisTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getAxisType()
		 * @generated
		 */
		EClass AXIS_TYPE = eINSTANCE.getAxisType();

		/**
		 * The meta object literal for the '<em><b>Available Keys</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AXIS_TYPE__AVAILABLE_KEYS = eINSTANCE.getAxisType_AvailableKeys();

		/**
		 * The meta object literal for the '<em><b>Meaning</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AXIS_TYPE__MEANING = eINSTANCE.getAxisType_Meaning();

		/**
		 * The meta object literal for the '<em><b>Data Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AXIS_TYPE__DATA_TYPE = eINSTANCE.getAxisType_DataType();

		/**
		 * The meta object literal for the '<em><b>UOM</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AXIS_TYPE__UOM = eINSTANCE.getAxisType_UOM();

		/**
		 * The meta object literal for the '<em><b>Reference System</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AXIS_TYPE__REFERENCE_SYSTEM = eINSTANCE.getAxisType_ReferenceSystem();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AXIS_TYPE__METADATA = eINSTANCE.getAxisType_Metadata();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AXIS_TYPE__IDENTIFIER = eINSTANCE.getAxisType_Identifier();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.CapabilitiesTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCapabilitiesType()
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
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.ContentsTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getContentsType()
		 * @generated
		 */
		EClass CONTENTS_TYPE = eINSTANCE.getContentsType();

		/**
		 * The meta object literal for the '<em><b>Coverage Summary</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTENTS_TYPE__COVERAGE_SUMMARY = eINSTANCE.getContentsType_CoverageSummary();

		/**
		 * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTENTS_TYPE__SUPPORTED_CRS = eINSTANCE.getContentsType_SupportedCRS();

		/**
		 * The meta object literal for the '<em><b>Supported Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTENTS_TYPE__SUPPORTED_FORMAT = eINSTANCE.getContentsType_SupportedFormat();

		/**
		 * The meta object literal for the '<em><b>Other Source</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTENTS_TYPE__OTHER_SOURCE = eINSTANCE.getContentsType_OtherSource();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.CoverageDescriptionsTypeImpl <em>Coverage Descriptions Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.CoverageDescriptionsTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoverageDescriptionsType()
		 * @generated
		 */
		EClass COVERAGE_DESCRIPTIONS_TYPE = eINSTANCE.getCoverageDescriptionsType();

		/**
		 * The meta object literal for the '<em><b>Coverage Description</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION = eINSTANCE.getCoverageDescriptionsType_CoverageDescription();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.CoverageDescriptionTypeImpl <em>Coverage Description Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.CoverageDescriptionTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoverageDescriptionType()
		 * @generated
		 */
		EClass COVERAGE_DESCRIPTION_TYPE = eINSTANCE.getCoverageDescriptionType();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COVERAGE_DESCRIPTION_TYPE__IDENTIFIER = eINSTANCE.getCoverageDescriptionType_Identifier();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_DESCRIPTION_TYPE__METADATA = eINSTANCE.getCoverageDescriptionType_Metadata();

		/**
		 * The meta object literal for the '<em><b>Domain</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_DESCRIPTION_TYPE__DOMAIN = eINSTANCE.getCoverageDescriptionType_Domain();

		/**
		 * The meta object literal for the '<em><b>Range</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_DESCRIPTION_TYPE__RANGE = eINSTANCE.getCoverageDescriptionType_Range();

		/**
		 * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS = eINSTANCE.getCoverageDescriptionType_SupportedCRS();

		/**
		 * The meta object literal for the '<em><b>Supported Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT = eINSTANCE.getCoverageDescriptionType_SupportedFormat();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.CoverageDomainTypeImpl <em>Coverage Domain Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.CoverageDomainTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoverageDomainType()
		 * @generated
		 */
		EClass COVERAGE_DOMAIN_TYPE = eINSTANCE.getCoverageDomainType();

		/**
		 * The meta object literal for the '<em><b>Spatial Domain</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN = eINSTANCE.getCoverageDomainType_SpatialDomain();

		/**
		 * The meta object literal for the '<em><b>Temporal Domain</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN = eINSTANCE.getCoverageDomainType_TemporalDomain();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.CoveragesTypeImpl <em>Coverages Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.CoveragesTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoveragesType()
		 * @generated
		 */
		EClass COVERAGES_TYPE = eINSTANCE.getCoveragesType();

		/**
		 * The meta object literal for the '<em><b>Coverage</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGES_TYPE__COVERAGE = eINSTANCE.getCoveragesType_Coverage();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl <em>Coverage Summary Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.CoverageSummaryTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getCoverageSummaryType()
		 * @generated
		 */
		EClass COVERAGE_SUMMARY_TYPE = eINSTANCE.getCoverageSummaryType();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_SUMMARY_TYPE__METADATA = eINSTANCE.getCoverageSummaryType_Metadata();

		/**
		 * The meta object literal for the '<em><b>WGS84 Bounding Box</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX = eINSTANCE.getCoverageSummaryType_WGS84BoundingBox();

		/**
		 * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS = eINSTANCE.getCoverageSummaryType_SupportedCRS();

		/**
		 * The meta object literal for the '<em><b>Supported Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT = eINSTANCE.getCoverageSummaryType_SupportedFormat();

		/**
		 * The meta object literal for the '<em><b>Coverage Summary</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY = eINSTANCE.getCoverageSummaryType_CoverageSummary();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COVERAGE_SUMMARY_TYPE__IDENTIFIER = eINSTANCE.getCoverageSummaryType_Identifier();

		/**
		 * The meta object literal for the '<em><b>Identifier1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COVERAGE_SUMMARY_TYPE__IDENTIFIER1 = eINSTANCE.getCoverageSummaryType_Identifier1();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.DescribeCoverageTypeImpl <em>Describe Coverage Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.DescribeCoverageTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getDescribeCoverageType()
		 * @generated
		 */
		EClass DESCRIBE_COVERAGE_TYPE = eINSTANCE.getDescribeCoverageType();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESCRIBE_COVERAGE_TYPE__IDENTIFIER = eINSTANCE.getDescribeCoverageType_Identifier();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.DocumentRootImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getDocumentRoot()
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
		 * The meta object literal for the '<em><b>Available Keys</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__AVAILABLE_KEYS = eINSTANCE.getDocumentRoot_AvailableKeys();

		/**
		 * The meta object literal for the '<em><b>Axis Subset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__AXIS_SUBSET = eINSTANCE.getDocumentRoot_AxisSubset();

		/**
		 * The meta object literal for the '<em><b>Capabilities</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__CAPABILITIES = eINSTANCE.getDocumentRoot_Capabilities();

		/**
		 * The meta object literal for the '<em><b>Contents</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__CONTENTS = eINSTANCE.getDocumentRoot_Contents();

		/**
		 * The meta object literal for the '<em><b>Coverage</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__COVERAGE = eINSTANCE.getDocumentRoot_Coverage();

		/**
		 * The meta object literal for the '<em><b>Coverage Descriptions</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS = eINSTANCE.getDocumentRoot_CoverageDescriptions();

		/**
		 * The meta object literal for the '<em><b>Coverages</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__COVERAGES = eINSTANCE.getDocumentRoot_Coverages();

		/**
		 * The meta object literal for the '<em><b>Coverage Summary</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__COVERAGE_SUMMARY = eINSTANCE.getDocumentRoot_CoverageSummary();

		/**
		 * The meta object literal for the '<em><b>Describe Coverage</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DESCRIBE_COVERAGE = eINSTANCE.getDocumentRoot_DescribeCoverage();

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
		 * The meta object literal for the '<em><b>Grid Base CRS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__GRID_BASE_CRS = eINSTANCE.getDocumentRoot_GridBaseCRS();

		/**
		 * The meta object literal for the '<em><b>Grid CRS</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GRID_CRS = eINSTANCE.getDocumentRoot_GridCRS();

		/**
		 * The meta object literal for the '<em><b>Grid CS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__GRID_CS = eINSTANCE.getDocumentRoot_GridCS();

		/**
		 * The meta object literal for the '<em><b>Grid Offsets</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__GRID_OFFSETS = eINSTANCE.getDocumentRoot_GridOffsets();

		/**
		 * The meta object literal for the '<em><b>Grid Origin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__GRID_ORIGIN = eINSTANCE.getDocumentRoot_GridOrigin();

		/**
		 * The meta object literal for the '<em><b>Grid Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__GRID_TYPE = eINSTANCE.getDocumentRoot_GridType();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__IDENTIFIER = eINSTANCE.getDocumentRoot_Identifier();

		/**
		 * The meta object literal for the '<em><b>Interpolation Methods</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__INTERPOLATION_METHODS = eINSTANCE.getDocumentRoot_InterpolationMethods();

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
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.DomainSubsetTypeImpl <em>Domain Subset Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.DomainSubsetTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getDomainSubsetType()
		 * @generated
		 */
		EClass DOMAIN_SUBSET_TYPE = eINSTANCE.getDomainSubsetType();

		/**
		 * The meta object literal for the '<em><b>Bounding Box Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOMAIN_SUBSET_TYPE__BOUNDING_BOX_GROUP = eINSTANCE.getDomainSubsetType_BoundingBoxGroup();

		/**
		 * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOMAIN_SUBSET_TYPE__BOUNDING_BOX = eINSTANCE.getDomainSubsetType_BoundingBox();

		/**
		 * The meta object literal for the '<em><b>Temporal Subset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET = eINSTANCE.getDomainSubsetType_TemporalSubset();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.FieldSubsetTypeImpl <em>Field Subset Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.FieldSubsetTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getFieldSubsetType()
		 * @generated
		 */
		EClass FIELD_SUBSET_TYPE = eINSTANCE.getFieldSubsetType();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FIELD_SUBSET_TYPE__IDENTIFIER = eINSTANCE.getFieldSubsetType_Identifier();

		/**
		 * The meta object literal for the '<em><b>Interpolation Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIELD_SUBSET_TYPE__INTERPOLATION_TYPE = eINSTANCE.getFieldSubsetType_InterpolationType();

		/**
		 * The meta object literal for the '<em><b>Axis Subset</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FIELD_SUBSET_TYPE__AXIS_SUBSET = eINSTANCE.getFieldSubsetType_AxisSubset();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.FieldTypeImpl <em>Field Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.FieldTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getFieldType()
		 * @generated
		 */
		EClass FIELD_TYPE = eINSTANCE.getFieldType();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIELD_TYPE__IDENTIFIER = eINSTANCE.getFieldType_Identifier();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FIELD_TYPE__DEFINITION = eINSTANCE.getFieldType_Definition();

		/**
		 * The meta object literal for the '<em><b>Null Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FIELD_TYPE__NULL_VALUE = eINSTANCE.getFieldType_NullValue();

		/**
		 * The meta object literal for the '<em><b>Interpolation Methods</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FIELD_TYPE__INTERPOLATION_METHODS = eINSTANCE.getFieldType_InterpolationMethods();

		/**
		 * The meta object literal for the '<em><b>Axis</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FIELD_TYPE__AXIS = eINSTANCE.getFieldType_Axis();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.GetCapabilitiesTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getGetCapabilitiesType()
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
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.GetCoverageTypeImpl <em>Get Coverage Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.GetCoverageTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getGetCoverageType()
		 * @generated
		 */
		EClass GET_COVERAGE_TYPE = eINSTANCE.getGetCoverageType();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GET_COVERAGE_TYPE__IDENTIFIER = eINSTANCE.getGetCoverageType_Identifier();

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
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GET_COVERAGE_TYPE__OUTPUT = eINSTANCE.getGetCoverageType_Output();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.GridCrsTypeImpl <em>Grid Crs Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.GridCrsTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getGridCrsType()
		 * @generated
		 */
		EClass GRID_CRS_TYPE = eINSTANCE.getGridCrsType();

		/**
		 * The meta object literal for the '<em><b>Srs Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_CRS_TYPE__SRS_NAME = eINSTANCE.getGridCrsType_SrsName();

		/**
		 * The meta object literal for the '<em><b>Grid Base CRS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_CRS_TYPE__GRID_BASE_CRS = eINSTANCE.getGridCrsType_GridBaseCRS();

		/**
		 * The meta object literal for the '<em><b>Grid Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_CRS_TYPE__GRID_TYPE = eINSTANCE.getGridCrsType_GridType();

		/**
		 * The meta object literal for the '<em><b>Grid Origin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_CRS_TYPE__GRID_ORIGIN = eINSTANCE.getGridCrsType_GridOrigin();

		/**
		 * The meta object literal for the '<em><b>Grid Offsets</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_CRS_TYPE__GRID_OFFSETS = eINSTANCE.getGridCrsType_GridOffsets();

		/**
		 * The meta object literal for the '<em><b>Grid CS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_CRS_TYPE__GRID_CS = eINSTANCE.getGridCrsType_GridCS();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_CRS_TYPE__ID = eINSTANCE.getGridCrsType_Id();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.ImageCRSRefTypeImpl <em>Image CRS Ref Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.ImageCRSRefTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getImageCRSRefType()
		 * @generated
		 */
		EClass IMAGE_CRS_REF_TYPE = eINSTANCE.getImageCRSRefType();

		/**
		 * The meta object literal for the '<em><b>Image CRS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMAGE_CRS_REF_TYPE__IMAGE_CRS = eINSTANCE.getImageCRSRefType_ImageCRS();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.InterpolationMethodBaseTypeImpl <em>Interpolation Method Base Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.InterpolationMethodBaseTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getInterpolationMethodBaseType()
		 * @generated
		 */
		EClass INTERPOLATION_METHOD_BASE_TYPE = eINSTANCE.getInterpolationMethodBaseType();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.InterpolationMethodsTypeImpl <em>Interpolation Methods Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.InterpolationMethodsTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getInterpolationMethodsType()
		 * @generated
		 */
		EClass INTERPOLATION_METHODS_TYPE = eINSTANCE.getInterpolationMethodsType();

		/**
		 * The meta object literal for the '<em><b>Interpolation Method</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INTERPOLATION_METHODS_TYPE__INTERPOLATION_METHOD = eINSTANCE.getInterpolationMethodsType_InterpolationMethod();

		/**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INTERPOLATION_METHODS_TYPE__DEFAULT = eINSTANCE.getInterpolationMethodsType_Default();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.InterpolationMethodTypeImpl <em>Interpolation Method Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.InterpolationMethodTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getInterpolationMethodType()
		 * @generated
		 */
		EClass INTERPOLATION_METHOD_TYPE = eINSTANCE.getInterpolationMethodType();

		/**
		 * The meta object literal for the '<em><b>Null Resistance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE = eINSTANCE.getInterpolationMethodType_NullResistance();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.OutputTypeImpl <em>Output Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.OutputTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getOutputType()
		 * @generated
		 */
		EClass OUTPUT_TYPE = eINSTANCE.getOutputType();

		/**
		 * The meta object literal for the '<em><b>Grid CRS</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_TYPE__GRID_CRS = eINSTANCE.getOutputType_GridCRS();

		/**
		 * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_TYPE__FORMAT = eINSTANCE.getOutputType_Format();

		/**
		 * The meta object literal for the '<em><b>Store</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_TYPE__STORE = eINSTANCE.getOutputType_Store();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.RangeSubsetTypeImpl <em>Range Subset Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.RangeSubsetTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getRangeSubsetType()
		 * @generated
		 */
		EClass RANGE_SUBSET_TYPE = eINSTANCE.getRangeSubsetType();

		/**
		 * The meta object literal for the '<em><b>Field Subset</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RANGE_SUBSET_TYPE__FIELD_SUBSET = eINSTANCE.getRangeSubsetType_FieldSubset();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.RangeTypeImpl <em>Range Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.RangeTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getRangeType()
		 * @generated
		 */
		EClass RANGE_TYPE = eINSTANCE.getRangeType();

		/**
		 * The meta object literal for the '<em><b>Field</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RANGE_TYPE__FIELD = eINSTANCE.getRangeType_Field();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.RequestBaseTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getRequestBaseType()
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
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.SpatialDomainTypeImpl <em>Spatial Domain Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.SpatialDomainTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getSpatialDomainType()
		 * @generated
		 */
		EClass SPATIAL_DOMAIN_TYPE = eINSTANCE.getSpatialDomainType();

		/**
		 * The meta object literal for the '<em><b>Bounding Box Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPATIAL_DOMAIN_TYPE__BOUNDING_BOX_GROUP = eINSTANCE.getSpatialDomainType_BoundingBoxGroup();

		/**
		 * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPATIAL_DOMAIN_TYPE__BOUNDING_BOX = eINSTANCE.getSpatialDomainType_BoundingBox();

		/**
		 * The meta object literal for the '<em><b>Grid CRS</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPATIAL_DOMAIN_TYPE__GRID_CRS = eINSTANCE.getSpatialDomainType_GridCRS();

		/**
		 * The meta object literal for the '<em><b>Transformation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPATIAL_DOMAIN_TYPE__TRANSFORMATION = eINSTANCE.getSpatialDomainType_Transformation();

		/**
		 * The meta object literal for the '<em><b>Image CRS</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPATIAL_DOMAIN_TYPE__IMAGE_CRS = eINSTANCE.getSpatialDomainType_ImageCRS();

		/**
		 * The meta object literal for the '<em><b>Polygon</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPATIAL_DOMAIN_TYPE__POLYGON = eINSTANCE.getSpatialDomainType_Polygon();

		/**
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.TimePeriodTypeImpl <em>Time Period Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.TimePeriodTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getTimePeriodType()
		 * @generated
		 */
		EClass TIME_PERIOD_TYPE = eINSTANCE.getTimePeriodType();

		/**
		 * The meta object literal for the '<em><b>Begin Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_PERIOD_TYPE__BEGIN_POSITION = eINSTANCE.getTimePeriodType_BeginPosition();

		/**
		 * The meta object literal for the '<em><b>End Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_PERIOD_TYPE__END_POSITION = eINSTANCE.getTimePeriodType_EndPosition();

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
		 * The meta object literal for the '{@link net.opengis.wcs11.impl.TimeSequenceTypeImpl <em>Time Sequence Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wcs11.impl.TimeSequenceTypeImpl
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getTimeSequenceType()
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
		 * The meta object literal for the '<em><b>Time Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_SEQUENCE_TYPE__TIME_POSITION = eINSTANCE.getTimeSequenceType_TimePosition();

		/**
		 * The meta object literal for the '<em><b>Time Period</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TIME_SEQUENCE_TYPE__TIME_PERIOD = eINSTANCE.getTimeSequenceType_TimePeriod();

		/**
		 * The meta object literal for the '<em>Identifier Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getIdentifierType()
		 * @generated
		 */
		EDataType IDENTIFIER_TYPE = eINSTANCE.getIdentifierType();

		/**
		 * The meta object literal for the '<em>Interpolation Method Base Type Base</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getInterpolationMethodBaseTypeBase()
		 * @generated
		 */
		EDataType INTERPOLATION_METHOD_BASE_TYPE_BASE = eINSTANCE.getInterpolationMethodBaseTypeBase();

		/**
		 * The meta object literal for the '<em>Time Duration Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getTimeDurationType()
		 * @generated
		 */
		EDataType TIME_DURATION_TYPE = eINSTANCE.getTimeDurationType();

		/**
		 * The meta object literal for the '<em>Map</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.Map
		 * @see net.opengis.wcs11.impl.Wcs11PackageImpl#getMap()
		 * @generated
		 */
		EDataType MAP = eINSTANCE.getMap();

	}

} //Wcs11Package
