/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

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
 * 
 * 			<description>This XML Schema Document encodes the WPS GetCapabilities operation response.</description>
 * 			<copyright>Copyright (c) 2007 OGC, All Rights Reserved.
 * For conditions, see OGC Software Notice http://www.opengeospatial.org/ogc/software</copyright>
 * 
 * 
 * 			<description>Location of a WSDL document.</description>
 * 			<copyright>Copyright (c) 2007 OGC, All Rights Reserved.
 * For conditions, see OGC Software Notice http://www.opengeospatial.org/ogc/software</copyright>
 * 
 * 
 * 			GML 3.0 candidate xlinks schema. Copyright (c) 2001 OGC, All Rights Reserved.
 * 
 * 
 * 			<description>Brief description of a Process, designed for Process discovery. </description>
 * 			<copyright>Copyright (c) 2007 OGC, All Rights Reserved.
 * For conditions, see OGC Software Notice http://www.opengeospatial.org/ogc/software</copyright>
 * 
 * 
 * 			<description>This XML Schema Document encodes elements and types that are shared by multiple WPS operations.</description>
 * 			<copyright>Copyright (c) 2007 OGC, All Rights Reserved.
 * For conditions, see OGC Software Notice http://www.opengeospatial.org/ogc/software</copyright>
 * 
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
 * This XML Schema  Document encodes the typical Contents section of an OWS service metadata (Capabilities) document. This  Schema can be built upon to define the Contents section for a specific OWS. If the ContentsBaseType in this XML Schema cannot be restricted and extended to define the Contents section for a specific OWS, all other relevant parts defined in owsContents.xsd shall be used by the "ContentsType" in the wxsContents.xsd prepared for the specific OWS.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document specifies types and elements for input and output of operation data, allowing including multiple data items with each data item either included or referenced. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * This XML Schema Document specifies types and elements for document or resource references and for package manifests that contain multiple references. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 
 * 			<description>This XML Schema Document encodes elements and types that are shared by multiple WPS operations.</description>
 * 			<copyright>Copyright (c) 2007 OGC, All Rights Reserved.
 * For conditions, see OGC Software Notice http://www.opengeospatial.org/ogc/software</copyright>
 * <!-- end-model-doc -->
 * @see net.opengis.wps10.Wps10Factory
 * @model kind="package"
 *        annotation="urn:opengis:specification:gml:schema-xlinks:v3.0c2 appinfo='xlinks.xsd v3.0b2 2001-07'"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface Wps10Package extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "wps10";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.opengis.net/wps/1.0.0";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "wps10";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    Wps10Package eINSTANCE = net.opengis.wps10.impl.Wps10PackageImpl.init();

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.BodyReferenceTypeImpl <em>Body Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.BodyReferenceTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getBodyReferenceType()
     * @generated
     */
    int BODY_REFERENCE_TYPE = 0;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BODY_REFERENCE_TYPE__HREF = 0;

    /**
     * The number of structural features of the '<em>Body Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BODY_REFERENCE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ComplexDataCombinationsTypeImpl <em>Complex Data Combinations Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ComplexDataCombinationsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getComplexDataCombinationsType()
     * @generated
     */
    int COMPLEX_DATA_COMBINATIONS_TYPE = 1;

    /**
     * The feature id for the '<em><b>Format</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT = 0;

    /**
     * The number of structural features of the '<em>Complex Data Combinations Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_COMBINATIONS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ComplexDataCombinationTypeImpl <em>Complex Data Combination Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ComplexDataCombinationTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getComplexDataCombinationType()
     * @generated
     */
    int COMPLEX_DATA_COMBINATION_TYPE = 2;

    /**
     * The feature id for the '<em><b>Format</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_COMBINATION_TYPE__FORMAT = 0;

    /**
     * The number of structural features of the '<em>Complex Data Combination Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_COMBINATION_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ComplexDataDescriptionTypeImpl <em>Complex Data Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ComplexDataDescriptionTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getComplexDataDescriptionType()
     * @generated
     */
    int COMPLEX_DATA_DESCRIPTION_TYPE = 3;

    /**
     * The feature id for the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_DESCRIPTION_TYPE__MIME_TYPE = 0;

    /**
     * The feature id for the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_DESCRIPTION_TYPE__ENCODING = 1;

    /**
     * The feature id for the '<em><b>Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_DESCRIPTION_TYPE__SCHEMA = 2;

    /**
     * The number of structural features of the '<em>Complex Data Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_DESCRIPTION_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ComplexDataTypeImpl <em>Complex Data Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ComplexDataTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getComplexDataType()
     * @generated
     */
    int COMPLEX_DATA_TYPE = 4;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_TYPE__MIXED = XMLTypePackage.ANY_TYPE__MIXED;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_TYPE__ANY = XMLTypePackage.ANY_TYPE__ANY;

    /**
     * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_TYPE__ANY_ATTRIBUTE = XMLTypePackage.ANY_TYPE__ANY_ATTRIBUTE;

    /**
     * The feature id for the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_TYPE__ENCODING = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_TYPE__MIME_TYPE = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_TYPE__SCHEMA = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Data</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_TYPE__DATA = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Complex Data Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPLEX_DATA_TYPE_FEATURE_COUNT = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.CRSsTypeImpl <em>CR Ss Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.CRSsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getCRSsType()
     * @generated
     */
    int CR_SS_TYPE = 5;

    /**
     * The feature id for the '<em><b>CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CR_SS_TYPE__CRS = 0;

    /**
     * The number of structural features of the '<em>CR Ss Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CR_SS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DataInputsTypeImpl <em>Data Inputs Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DataInputsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDataInputsType()
     * @generated
     */
    int DATA_INPUTS_TYPE = 6;

    /**
     * The feature id for the '<em><b>Input</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_INPUTS_TYPE__INPUT = 0;

    /**
     * The number of structural features of the '<em>Data Inputs Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_INPUTS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DataInputsType1Impl <em>Data Inputs Type1</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DataInputsType1Impl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDataInputsType1()
     * @generated
     */
    int DATA_INPUTS_TYPE1 = 7;

    /**
     * The feature id for the '<em><b>Input</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_INPUTS_TYPE1__INPUT = 0;

    /**
     * The number of structural features of the '<em>Data Inputs Type1</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_INPUTS_TYPE1_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DataTypeImpl <em>Data Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DataTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDataType()
     * @generated
     */
    int DATA_TYPE = 8;

    /**
     * The feature id for the '<em><b>Complex Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_TYPE__COMPLEX_DATA = 0;

    /**
     * The feature id for the '<em><b>Literal Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_TYPE__LITERAL_DATA = 1;

    /**
     * The feature id for the '<em><b>Bounding Box Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_TYPE__BOUNDING_BOX_DATA = 2;

    /**
     * The number of structural features of the '<em>Data Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DefaultTypeImpl <em>Default Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DefaultTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDefaultType()
     * @generated
     */
    int DEFAULT_TYPE = 9;

    /**
     * The feature id for the '<em><b>CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_TYPE__CRS = 0;

    /**
     * The number of structural features of the '<em>Default Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DefaultType1Impl <em>Default Type1</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DefaultType1Impl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDefaultType1()
     * @generated
     */
    int DEFAULT_TYPE1 = 10;

    /**
     * The feature id for the '<em><b>UOM</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_TYPE1__UOM = 0;

    /**
     * The number of structural features of the '<em>Default Type1</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_TYPE1_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DefaultType2Impl <em>Default Type2</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DefaultType2Impl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDefaultType2()
     * @generated
     */
    int DEFAULT_TYPE2 = 11;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_TYPE2__LANGUAGE = 0;

    /**
     * The number of structural features of the '<em>Default Type2</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEFAULT_TYPE2_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.RequestBaseTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getRequestBaseType()
     * @generated
     */
    int REQUEST_BASE_TYPE = 41;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__LANGUAGE = 0;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__SERVICE = 1;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__VERSION = 2;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__BASE_URL = 3;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int REQUEST_BASE_TYPE__EXTENDED_PROPERTIES = 4;

				/**
     * The number of structural features of the '<em>Request Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DescribeProcessTypeImpl <em>Describe Process Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DescribeProcessTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDescribeProcessType()
     * @generated
     */
    int DESCRIBE_PROCESS_TYPE = 12;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_PROCESS_TYPE__LANGUAGE = REQUEST_BASE_TYPE__LANGUAGE;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_PROCESS_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_PROCESS_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_PROCESS_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int DESCRIBE_PROCESS_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

				/**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_PROCESS_TYPE__IDENTIFIER = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Describe Process Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_PROCESS_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DescriptionTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDescriptionType()
     * @generated
     */
    int DESCRIPTION_TYPE = 13;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__IDENTIFIER = 0;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__TITLE = 1;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__ABSTRACT = 2;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__METADATA = 3;

    /**
     * The number of structural features of the '<em>Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.OutputDefinitionTypeImpl <em>Output Definition Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.OutputDefinitionTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputDefinitionType()
     * @generated
     */
    int OUTPUT_DEFINITION_TYPE = 30;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DEFINITION_TYPE__IDENTIFIER = 0;

    /**
     * The feature id for the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DEFINITION_TYPE__ENCODING = 1;

    /**
     * The feature id for the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DEFINITION_TYPE__MIME_TYPE = 2;

    /**
     * The feature id for the '<em><b>Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DEFINITION_TYPE__SCHEMA = 3;

    /**
     * The feature id for the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DEFINITION_TYPE__UOM = 4;

    /**
     * The number of structural features of the '<em>Output Definition Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DEFINITION_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DocumentOutputDefinitionTypeImpl <em>Document Output Definition Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DocumentOutputDefinitionTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDocumentOutputDefinitionType()
     * @generated
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE = 14;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE__IDENTIFIER = OUTPUT_DEFINITION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE__ENCODING = OUTPUT_DEFINITION_TYPE__ENCODING;

    /**
     * The feature id for the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE__MIME_TYPE = OUTPUT_DEFINITION_TYPE__MIME_TYPE;

    /**
     * The feature id for the '<em><b>Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE__SCHEMA = OUTPUT_DEFINITION_TYPE__SCHEMA;

    /**
     * The feature id for the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE__UOM = OUTPUT_DEFINITION_TYPE__UOM;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE = OUTPUT_DEFINITION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT = OUTPUT_DEFINITION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>As Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE = OUTPUT_DEFINITION_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Document Output Definition Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_OUTPUT_DEFINITION_TYPE_FEATURE_COUNT = OUTPUT_DEFINITION_TYPE_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.DocumentRootImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getDocumentRoot()
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
     * The feature id for the '<em><b>Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CAPABILITIES = 3;

    /**
     * The feature id for the '<em><b>Describe Process</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DESCRIBE_PROCESS = 4;

    /**
     * The feature id for the '<em><b>Execute</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXECUTE = 5;

    /**
     * The feature id for the '<em><b>Execute Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXECUTE_RESPONSE = 6;

    /**
     * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_CAPABILITIES = 7;

    /**
     * The feature id for the '<em><b>Languages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LANGUAGES = 8;

    /**
     * The feature id for the '<em><b>Process Descriptions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROCESS_DESCRIPTIONS = 9;

    /**
     * The feature id for the '<em><b>Process Offerings</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROCESS_OFFERINGS = 10;

    /**
     * The feature id for the '<em><b>WSDL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__WSDL = 11;

    /**
     * The feature id for the '<em><b>Process Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROCESS_VERSION = 12;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 13;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ResponseBaseTypeImpl <em>Response Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ResponseBaseTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getResponseBaseType()
     * @generated
     */
    int RESPONSE_BASE_TYPE = 42;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_BASE_TYPE__LANG = 0;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_BASE_TYPE__SERVICE = 1;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_BASE_TYPE__VERSION = 2;

    /**
     * The number of structural features of the '<em>Response Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_BASE_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ExecuteResponseTypeImpl <em>Execute Response Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ExecuteResponseTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getExecuteResponseType()
     * @generated
     */
    int EXECUTE_RESPONSE_TYPE = 16;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__LANG = RESPONSE_BASE_TYPE__LANG;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__SERVICE = RESPONSE_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__VERSION = RESPONSE_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Process</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__PROCESS = RESPONSE_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Status</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__STATUS = RESPONSE_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Data Inputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__DATA_INPUTS = RESPONSE_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Output Definitions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__OUTPUT_DEFINITIONS = RESPONSE_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Process Outputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__PROCESS_OUTPUTS = RESPONSE_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Service Instance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__SERVICE_INSTANCE = RESPONSE_BASE_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Status Location</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE__STATUS_LOCATION = RESPONSE_BASE_TYPE_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Execute Response Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_RESPONSE_TYPE_FEATURE_COUNT = RESPONSE_BASE_TYPE_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ExecuteTypeImpl <em>Execute Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ExecuteTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getExecuteType()
     * @generated
     */
    int EXECUTE_TYPE = 17;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_TYPE__LANGUAGE = REQUEST_BASE_TYPE__LANGUAGE;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_TYPE__BASE_URL = REQUEST_BASE_TYPE__BASE_URL;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int EXECUTE_TYPE__EXTENDED_PROPERTIES = REQUEST_BASE_TYPE__EXTENDED_PROPERTIES;

				/**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_TYPE__IDENTIFIER = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Data Inputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_TYPE__DATA_INPUTS = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Response Form</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_TYPE__RESPONSE_FORM = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Execute Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTE_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.GetCapabilitiesTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getGetCapabilitiesType()
     * @generated
     */
    int GET_CAPABILITIES_TYPE = 18;

    /**
     * The feature id for the '<em><b>Accept Versions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = 0;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__LANGUAGE = 1;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SERVICE = 2;

    /**
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__BASE_URL = 3;

    /**
     * The feature id for the '<em><b>Extended Properties</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	int GET_CAPABILITIES_TYPE__EXTENDED_PROPERTIES = 4;

				/**
     * The number of structural features of the '<em>Get Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.HeaderTypeImpl <em>Header Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.HeaderTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getHeaderType()
     * @generated
     */
    int HEADER_TYPE = 19;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_TYPE__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_TYPE__VALUE = 1;

    /**
     * The number of structural features of the '<em>Header Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HEADER_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.InputDescriptionTypeImpl <em>Input Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.InputDescriptionTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getInputDescriptionType()
     * @generated
     */
    int INPUT_DESCRIPTION_TYPE = 20;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Complex Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__COMPLEX_DATA = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Literal Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__LITERAL_DATA = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Bounding Box Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Max Occurs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__MAX_OCCURS = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Min Occurs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE__MIN_OCCURS = DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Input Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_DESCRIPTION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.InputReferenceTypeImpl <em>Input Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.InputReferenceTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getInputReferenceType()
     * @generated
     */
    int INPUT_REFERENCE_TYPE = 21;

    /**
     * The feature id for the '<em><b>Header</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE__HEADER = 0;

    /**
     * The feature id for the '<em><b>Body</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE__BODY = 1;

    /**
     * The feature id for the '<em><b>Body Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE__BODY_REFERENCE = 2;

    /**
     * The feature id for the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE__ENCODING = 3;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE__HREF = 4;

    /**
     * The feature id for the '<em><b>Method</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE__METHOD = 5;

    /**
     * The feature id for the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE__MIME_TYPE = 6;

    /**
     * The feature id for the '<em><b>Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE__SCHEMA = 7;

    /**
     * The number of structural features of the '<em>Input Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_REFERENCE_TYPE_FEATURE_COUNT = 8;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.InputTypeImpl <em>Input Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.InputTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getInputType()
     * @generated
     */
    int INPUT_TYPE = 22;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_TYPE__IDENTIFIER = 0;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_TYPE__TITLE = 1;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_TYPE__ABSTRACT = 2;

    /**
     * The feature id for the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_TYPE__REFERENCE = 3;

    /**
     * The feature id for the '<em><b>Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_TYPE__DATA = 4;

    /**
     * The number of structural features of the '<em>Input Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INPUT_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.LanguagesTypeImpl <em>Languages Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.LanguagesTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getLanguagesType()
     * @generated
     */
    int LANGUAGES_TYPE = 23;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LANGUAGES_TYPE__LANGUAGE = 0;

    /**
     * The number of structural features of the '<em>Languages Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LANGUAGES_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.LanguagesType1Impl <em>Languages Type1</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.LanguagesType1Impl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getLanguagesType1()
     * @generated
     */
    int LANGUAGES_TYPE1 = 24;

    /**
     * The feature id for the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LANGUAGES_TYPE1__DEFAULT = 0;

    /**
     * The feature id for the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LANGUAGES_TYPE1__SUPPORTED = 1;

    /**
     * The number of structural features of the '<em>Languages Type1</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LANGUAGES_TYPE1_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.LiteralDataTypeImpl <em>Literal Data Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.LiteralDataTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getLiteralDataType()
     * @generated
     */
    int LITERAL_DATA_TYPE = 25;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_DATA_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_DATA_TYPE__DATA_TYPE = 1;

    /**
     * The feature id for the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_DATA_TYPE__UOM = 2;

    /**
     * The number of structural features of the '<em>Literal Data Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_DATA_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.LiteralOutputTypeImpl <em>Literal Output Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.LiteralOutputTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getLiteralOutputType()
     * @generated
     */
    int LITERAL_OUTPUT_TYPE = 27;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_OUTPUT_TYPE__DATA_TYPE = 0;

    /**
     * The feature id for the '<em><b>UO Ms</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_OUTPUT_TYPE__UO_MS = 1;

    /**
     * The number of structural features of the '<em>Literal Output Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_OUTPUT_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.LiteralInputTypeImpl <em>Literal Input Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.LiteralInputTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getLiteralInputType()
     * @generated
     */
    int LITERAL_INPUT_TYPE = 26;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_INPUT_TYPE__DATA_TYPE = LITERAL_OUTPUT_TYPE__DATA_TYPE;

    /**
     * The feature id for the '<em><b>UO Ms</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_INPUT_TYPE__UO_MS = LITERAL_OUTPUT_TYPE__UO_MS;

    /**
     * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_INPUT_TYPE__ALLOWED_VALUES = LITERAL_OUTPUT_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_INPUT_TYPE__ANY_VALUE = LITERAL_OUTPUT_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_INPUT_TYPE__VALUES_REFERENCE = LITERAL_OUTPUT_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_INPUT_TYPE__DEFAULT_VALUE = LITERAL_OUTPUT_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Literal Input Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_INPUT_TYPE_FEATURE_COUNT = LITERAL_OUTPUT_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.OutputDataTypeImpl <em>Output Data Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.OutputDataTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputDataType()
     * @generated
     */
    int OUTPUT_DATA_TYPE = 28;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DATA_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DATA_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DATA_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DATA_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DATA_TYPE__REFERENCE = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DATA_TYPE__DATA = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Output Data Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DATA_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.OutputDefinitionsTypeImpl <em>Output Definitions Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.OutputDefinitionsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputDefinitionsType()
     * @generated
     */
    int OUTPUT_DEFINITIONS_TYPE = 29;

    /**
     * The feature id for the '<em><b>Output</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DEFINITIONS_TYPE__OUTPUT = 0;

    /**
     * The number of structural features of the '<em>Output Definitions Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DEFINITIONS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.OutputDescriptionTypeImpl <em>Output Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.OutputDescriptionTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputDescriptionType()
     * @generated
     */
    int OUTPUT_DESCRIPTION_TYPE = 31;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DESCRIPTION_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DESCRIPTION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DESCRIPTION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DESCRIPTION_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Complex Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DESCRIPTION_TYPE__COMPLEX_OUTPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Literal Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DESCRIPTION_TYPE__LITERAL_OUTPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Bounding Box Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DESCRIPTION_TYPE__BOUNDING_BOX_OUTPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Output Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_DESCRIPTION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.OutputReferenceTypeImpl <em>Output Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.OutputReferenceTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputReferenceType()
     * @generated
     */
    int OUTPUT_REFERENCE_TYPE = 32;

    /**
     * The feature id for the '<em><b>Encoding</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_REFERENCE_TYPE__ENCODING = 0;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_REFERENCE_TYPE__HREF = 1;

    /**
     * The feature id for the '<em><b>Mime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_REFERENCE_TYPE__MIME_TYPE = 2;

    /**
     * The feature id for the '<em><b>Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_REFERENCE_TYPE__SCHEMA = 3;

    /**
     * The number of structural features of the '<em>Output Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_REFERENCE_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ProcessBriefTypeImpl <em>Process Brief Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ProcessBriefTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessBriefType()
     * @generated
     */
    int PROCESS_BRIEF_TYPE = 33;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_BRIEF_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_BRIEF_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_BRIEF_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_BRIEF_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Profile</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_BRIEF_TYPE__PROFILE = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>WSDL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_BRIEF_TYPE__WSDL = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Process Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_BRIEF_TYPE__PROCESS_VERSION = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Process Brief Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_BRIEF_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ProcessDescriptionsTypeImpl <em>Process Descriptions Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ProcessDescriptionsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessDescriptionsType()
     * @generated
     */
    int PROCESS_DESCRIPTIONS_TYPE = 34;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTIONS_TYPE__LANG = RESPONSE_BASE_TYPE__LANG;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTIONS_TYPE__SERVICE = RESPONSE_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTIONS_TYPE__VERSION = RESPONSE_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Process Description</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION = RESPONSE_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Process Descriptions Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTIONS_TYPE_FEATURE_COUNT = RESPONSE_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ProcessDescriptionTypeImpl <em>Process Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ProcessDescriptionTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessDescriptionType()
     * @generated
     */
    int PROCESS_DESCRIPTION_TYPE = 35;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__IDENTIFIER = PROCESS_BRIEF_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__TITLE = PROCESS_BRIEF_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__ABSTRACT = PROCESS_BRIEF_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__METADATA = PROCESS_BRIEF_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Profile</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__PROFILE = PROCESS_BRIEF_TYPE__PROFILE;

    /**
     * The feature id for the '<em><b>WSDL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__WSDL = PROCESS_BRIEF_TYPE__WSDL;

    /**
     * The feature id for the '<em><b>Process Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__PROCESS_VERSION = PROCESS_BRIEF_TYPE__PROCESS_VERSION;

    /**
     * The feature id for the '<em><b>Data Inputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__DATA_INPUTS = PROCESS_BRIEF_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Process Outputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS = PROCESS_BRIEF_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Status Supported</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED = PROCESS_BRIEF_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Store Supported</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED = PROCESS_BRIEF_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Process Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_DESCRIPTION_TYPE_FEATURE_COUNT = PROCESS_BRIEF_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ProcessFailedTypeImpl <em>Process Failed Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ProcessFailedTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessFailedType()
     * @generated
     */
    int PROCESS_FAILED_TYPE = 36;

    /**
     * The feature id for the '<em><b>Exception Report</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_FAILED_TYPE__EXCEPTION_REPORT = 0;

    /**
     * The number of structural features of the '<em>Process Failed Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_FAILED_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ProcessOfferingsTypeImpl <em>Process Offerings Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ProcessOfferingsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessOfferingsType()
     * @generated
     */
    int PROCESS_OFFERINGS_TYPE = 37;

    /**
     * The feature id for the '<em><b>Process</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_OFFERINGS_TYPE__PROCESS = 0;

    /**
     * The number of structural features of the '<em>Process Offerings Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_OFFERINGS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ProcessOutputsTypeImpl <em>Process Outputs Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ProcessOutputsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessOutputsType()
     * @generated
     */
    int PROCESS_OUTPUTS_TYPE = 38;

    /**
     * The feature id for the '<em><b>Output</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_OUTPUTS_TYPE__OUTPUT = 0;

    /**
     * The number of structural features of the '<em>Process Outputs Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_OUTPUTS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ProcessOutputsType1Impl <em>Process Outputs Type1</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ProcessOutputsType1Impl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessOutputsType1()
     * @generated
     */
    int PROCESS_OUTPUTS_TYPE1 = 39;

    /**
     * The feature id for the '<em><b>Output</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_OUTPUTS_TYPE1__OUTPUT = 0;

    /**
     * The number of structural features of the '<em>Process Outputs Type1</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_OUTPUTS_TYPE1_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ProcessStartedTypeImpl <em>Process Started Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ProcessStartedTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessStartedType()
     * @generated
     */
    int PROCESS_STARTED_TYPE = 40;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_STARTED_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Percent Completed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_STARTED_TYPE__PERCENT_COMPLETED = 1;

    /**
     * The number of structural features of the '<em>Process Started Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROCESS_STARTED_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ResponseDocumentTypeImpl <em>Response Document Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ResponseDocumentTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getResponseDocumentType()
     * @generated
     */
    int RESPONSE_DOCUMENT_TYPE = 43;

    /**
     * The feature id for the '<em><b>Output</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_DOCUMENT_TYPE__OUTPUT = 0;

    /**
     * The feature id for the '<em><b>Lineage</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_DOCUMENT_TYPE__LINEAGE = 1;

    /**
     * The feature id for the '<em><b>Status</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_DOCUMENT_TYPE__STATUS = 2;

    /**
     * The feature id for the '<em><b>Store Execute Response</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE = 3;

    /**
     * The number of structural features of the '<em>Response Document Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_DOCUMENT_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ResponseFormTypeImpl <em>Response Form Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ResponseFormTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getResponseFormType()
     * @generated
     */
    int RESPONSE_FORM_TYPE = 44;

    /**
     * The feature id for the '<em><b>Response Document</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT = 0;

    /**
     * The feature id for the '<em><b>Raw Data Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT = 1;

    /**
     * The number of structural features of the '<em>Response Form Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSE_FORM_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.StatusTypeImpl <em>Status Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.StatusTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getStatusType()
     * @generated
     */
    int STATUS_TYPE = 45;

    /**
     * The feature id for the '<em><b>Process Accepted</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS_TYPE__PROCESS_ACCEPTED = 0;

    /**
     * The feature id for the '<em><b>Process Started</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS_TYPE__PROCESS_STARTED = 1;

    /**
     * The feature id for the '<em><b>Process Paused</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS_TYPE__PROCESS_PAUSED = 2;

    /**
     * The feature id for the '<em><b>Process Succeeded</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS_TYPE__PROCESS_SUCCEEDED = 3;

    /**
     * The feature id for the '<em><b>Process Failed</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS_TYPE__PROCESS_FAILED = 4;

    /**
     * The feature id for the '<em><b>Creation Time</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS_TYPE__CREATION_TIME = 5;

    /**
     * The number of structural features of the '<em>Status Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int STATUS_TYPE_FEATURE_COUNT = 6;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.SupportedComplexDataTypeImpl <em>Supported Complex Data Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.SupportedComplexDataTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getSupportedComplexDataType()
     * @generated
     */
    int SUPPORTED_COMPLEX_DATA_TYPE = 47;

    /**
     * The feature id for the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT = 0;

    /**
     * The feature id for the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED = 1;

    /**
     * The number of structural features of the '<em>Supported Complex Data Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_COMPLEX_DATA_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.SupportedComplexDataInputTypeImpl <em>Supported Complex Data Input Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.SupportedComplexDataInputTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getSupportedComplexDataInputType()
     * @generated
     */
    int SUPPORTED_COMPLEX_DATA_INPUT_TYPE = 46;

    /**
     * The feature id for the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_COMPLEX_DATA_INPUT_TYPE__DEFAULT = SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT;

    /**
     * The feature id for the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_COMPLEX_DATA_INPUT_TYPE__SUPPORTED = SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED;

    /**
     * The feature id for the '<em><b>Maximum Megabytes</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_COMPLEX_DATA_INPUT_TYPE__MAXIMUM_MEGABYTES = SUPPORTED_COMPLEX_DATA_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Supported Complex Data Input Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_COMPLEX_DATA_INPUT_TYPE_FEATURE_COUNT = SUPPORTED_COMPLEX_DATA_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.SupportedCRSsTypeImpl <em>Supported CR Ss Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.SupportedCRSsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getSupportedCRSsType()
     * @generated
     */
    int SUPPORTED_CR_SS_TYPE = 48;

    /**
     * The feature id for the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_CR_SS_TYPE__DEFAULT = 0;

    /**
     * The feature id for the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_CR_SS_TYPE__SUPPORTED = 1;

    /**
     * The number of structural features of the '<em>Supported CR Ss Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_CR_SS_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.SupportedUOMsTypeImpl <em>Supported UO Ms Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.SupportedUOMsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getSupportedUOMsType()
     * @generated
     */
    int SUPPORTED_UO_MS_TYPE = 49;

    /**
     * The feature id for the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_UO_MS_TYPE__DEFAULT = 0;

    /**
     * The feature id for the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_UO_MS_TYPE__SUPPORTED = 1;

    /**
     * The number of structural features of the '<em>Supported UO Ms Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPPORTED_UO_MS_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.UOMsTypeImpl <em>UO Ms Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.UOMsTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getUOMsType()
     * @generated
     */
    int UO_MS_TYPE = 50;

    /**
     * The feature id for the '<em><b>UOM</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UO_MS_TYPE__UOM = 0;

    /**
     * The number of structural features of the '<em>UO Ms Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UO_MS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.ValuesReferenceTypeImpl <em>Values Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.ValuesReferenceTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getValuesReferenceType()
     * @generated
     */
    int VALUES_REFERENCE_TYPE = 51;

    /**
     * The feature id for the '<em><b>Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE__REFERENCE = 0;

    /**
     * The feature id for the '<em><b>Values Form</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE__VALUES_FORM = 1;

    /**
     * The number of structural features of the '<em>Values Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.WPSCapabilitiesTypeImpl <em>WPS Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.WPSCapabilitiesTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getWPSCapabilitiesType()
     * @generated
     */
    int WPS_CAPABILITIES_TYPE = 52;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__SERVICE_IDENTIFICATION = Ows11Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION;

    /**
     * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__SERVICE_PROVIDER = Ows11Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__OPERATIONS_METADATA = Ows11Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__UPDATE_SEQUENCE = Ows11Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__VERSION = Ows11Package.CAPABILITIES_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Process Offerings</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Languages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__LANGUAGES = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>WSDL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__WSDL = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__LANG = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE__SERVICE = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>WPS Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WPS_CAPABILITIES_TYPE_FEATURE_COUNT = Ows11Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.wps10.impl.WSDLTypeImpl <em>WSDL Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.impl.WSDLTypeImpl
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getWSDLType()
     * @generated
     */
    int WSDL_TYPE = 53;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_TYPE__HREF = 0;

    /**
     * The number of structural features of the '<em>WSDL Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int WSDL_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link javax.measure.unit.Unit <em>Unit</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.measure.unit.Unit
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getUnit()
     * @generated
     */
    int UNIT = 54;

    /**
     * The number of structural features of the '<em>Unit</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNIT_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wps10.MethodType <em>Method Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.MethodType
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getMethodType()
     * @generated
     */
    int METHOD_TYPE = 55;

    /**
     * The meta object id for the '<em>Method Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wps10.MethodType
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getMethodTypeObject()
     * @generated
     */
    int METHOD_TYPE_OBJECT = 56;

    /**
     * The meta object id for the '<em>Percent Completed Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.math.BigInteger
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getPercentCompletedType()
     * @generated
     */
    int PERCENT_COMPLETED_TYPE = 57;


    /**
     * The meta object id for the '<em>Map</em>' data type.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see java.util.Map
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getMap()
     * @generated
     */
	int MAP = 58;


				/**
     * The meta object id for the '<em>QName</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.xml.namespace.QName
     * @see net.opengis.wps10.impl.Wps10PackageImpl#getQName()
     * @generated
     */
    int QNAME = 59;


                /**
     * Returns the meta object for class '{@link net.opengis.wps10.BodyReferenceType <em>Body Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Body Reference Type</em>'.
     * @see net.opengis.wps10.BodyReferenceType
     * @generated
     */
    EClass getBodyReferenceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.BodyReferenceType#getHref <em>Href</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Href</em>'.
     * @see net.opengis.wps10.BodyReferenceType#getHref()
     * @see #getBodyReferenceType()
     * @generated
     */
    EAttribute getBodyReferenceType_Href();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ComplexDataCombinationsType <em>Complex Data Combinations Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Complex Data Combinations Type</em>'.
     * @see net.opengis.wps10.ComplexDataCombinationsType
     * @generated
     */
    EClass getComplexDataCombinationsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.ComplexDataCombinationsType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Format</em>'.
     * @see net.opengis.wps10.ComplexDataCombinationsType#getFormat()
     * @see #getComplexDataCombinationsType()
     * @generated
     */
    EReference getComplexDataCombinationsType_Format();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ComplexDataCombinationType <em>Complex Data Combination Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Complex Data Combination Type</em>'.
     * @see net.opengis.wps10.ComplexDataCombinationType
     * @generated
     */
    EClass getComplexDataCombinationType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ComplexDataCombinationType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Format</em>'.
     * @see net.opengis.wps10.ComplexDataCombinationType#getFormat()
     * @see #getComplexDataCombinationType()
     * @generated
     */
    EReference getComplexDataCombinationType_Format();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ComplexDataDescriptionType <em>Complex Data Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Complex Data Description Type</em>'.
     * @see net.opengis.wps10.ComplexDataDescriptionType
     * @generated
     */
    EClass getComplexDataDescriptionType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ComplexDataDescriptionType#getMimeType <em>Mime Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mime Type</em>'.
     * @see net.opengis.wps10.ComplexDataDescriptionType#getMimeType()
     * @see #getComplexDataDescriptionType()
     * @generated
     */
    EAttribute getComplexDataDescriptionType_MimeType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ComplexDataDescriptionType#getEncoding <em>Encoding</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Encoding</em>'.
     * @see net.opengis.wps10.ComplexDataDescriptionType#getEncoding()
     * @see #getComplexDataDescriptionType()
     * @generated
     */
    EAttribute getComplexDataDescriptionType_Encoding();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ComplexDataDescriptionType#getSchema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Schema</em>'.
     * @see net.opengis.wps10.ComplexDataDescriptionType#getSchema()
     * @see #getComplexDataDescriptionType()
     * @generated
     */
    EAttribute getComplexDataDescriptionType_Schema();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ComplexDataType <em>Complex Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Complex Data Type</em>'.
     * @see net.opengis.wps10.ComplexDataType
     * @generated
     */
    EClass getComplexDataType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ComplexDataType#getEncoding <em>Encoding</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Encoding</em>'.
     * @see net.opengis.wps10.ComplexDataType#getEncoding()
     * @see #getComplexDataType()
     * @generated
     */
    EAttribute getComplexDataType_Encoding();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ComplexDataType#getMimeType <em>Mime Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mime Type</em>'.
     * @see net.opengis.wps10.ComplexDataType#getMimeType()
     * @see #getComplexDataType()
     * @generated
     */
    EAttribute getComplexDataType_MimeType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ComplexDataType#getSchema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Schema</em>'.
     * @see net.opengis.wps10.ComplexDataType#getSchema()
     * @see #getComplexDataType()
     * @generated
     */
    EAttribute getComplexDataType_Schema();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wps10.ComplexDataType#getData <em>Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Data</em>'.
     * @see net.opengis.wps10.ComplexDataType#getData()
     * @see #getComplexDataType()
     * @generated
     */
    EAttribute getComplexDataType_Data();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.CRSsType <em>CR Ss Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>CR Ss Type</em>'.
     * @see net.opengis.wps10.CRSsType
     * @generated
     */
    EClass getCRSsType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.CRSsType#getCRS <em>CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>CRS</em>'.
     * @see net.opengis.wps10.CRSsType#getCRS()
     * @see #getCRSsType()
     * @generated
     */
    EAttribute getCRSsType_CRS();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DataInputsType <em>Data Inputs Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Inputs Type</em>'.
     * @see net.opengis.wps10.DataInputsType
     * @generated
     */
    EClass getDataInputsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.DataInputsType#getInput <em>Input</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Input</em>'.
     * @see net.opengis.wps10.DataInputsType#getInput()
     * @see #getDataInputsType()
     * @generated
     */
    EReference getDataInputsType_Input();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DataInputsType1 <em>Data Inputs Type1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Inputs Type1</em>'.
     * @see net.opengis.wps10.DataInputsType1
     * @generated
     */
    EClass getDataInputsType1();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.DataInputsType1#getInput <em>Input</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Input</em>'.
     * @see net.opengis.wps10.DataInputsType1#getInput()
     * @see #getDataInputsType1()
     * @generated
     */
    EReference getDataInputsType1_Input();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Type</em>'.
     * @see net.opengis.wps10.DataType
     * @generated
     */
    EClass getDataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DataType#getComplexData <em>Complex Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Complex Data</em>'.
     * @see net.opengis.wps10.DataType#getComplexData()
     * @see #getDataType()
     * @generated
     */
    EReference getDataType_ComplexData();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DataType#getLiteralData <em>Literal Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Literal Data</em>'.
     * @see net.opengis.wps10.DataType#getLiteralData()
     * @see #getDataType()
     * @generated
     */
    EReference getDataType_LiteralData();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DataType#getBoundingBoxData <em>Bounding Box Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bounding Box Data</em>'.
     * @see net.opengis.wps10.DataType#getBoundingBoxData()
     * @see #getDataType()
     * @generated
     */
    EReference getDataType_BoundingBoxData();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DefaultType <em>Default Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Default Type</em>'.
     * @see net.opengis.wps10.DefaultType
     * @generated
     */
    EClass getDefaultType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.DefaultType#getCRS <em>CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>CRS</em>'.
     * @see net.opengis.wps10.DefaultType#getCRS()
     * @see #getDefaultType()
     * @generated
     */
    EAttribute getDefaultType_CRS();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DefaultType1 <em>Default Type1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Default Type1</em>'.
     * @see net.opengis.wps10.DefaultType1
     * @generated
     */
    EClass getDefaultType1();

    /**
     * Returns the meta object for the reference '{@link net.opengis.wps10.DefaultType1#getUOM <em>UOM</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>UOM</em>'.
     * @see net.opengis.wps10.DefaultType1#getUOM()
     * @see #getDefaultType1()
     * @generated
     */
    EReference getDefaultType1_UOM();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DefaultType2 <em>Default Type2</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Default Type2</em>'.
     * @see net.opengis.wps10.DefaultType2
     * @generated
     */
    EClass getDefaultType2();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.DefaultType2#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see net.opengis.wps10.DefaultType2#getLanguage()
     * @see #getDefaultType2()
     * @generated
     */
    EAttribute getDefaultType2_Language();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DescribeProcessType <em>Describe Process Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Describe Process Type</em>'.
     * @see net.opengis.wps10.DescribeProcessType
     * @generated
     */
    EClass getDescribeProcessType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.DescribeProcessType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Identifier</em>'.
     * @see net.opengis.wps10.DescribeProcessType#getIdentifier()
     * @see #getDescribeProcessType()
     * @generated
     */
    EReference getDescribeProcessType_Identifier();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Description Type</em>'.
     * @see net.opengis.wps10.DescriptionType
     * @generated
     */
    EClass getDescriptionType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DescriptionType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wps10.DescriptionType#getIdentifier()
     * @see #getDescriptionType()
     * @generated
     */
    EReference getDescriptionType_Identifier();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DescriptionType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Title</em>'.
     * @see net.opengis.wps10.DescriptionType#getTitle()
     * @see #getDescriptionType()
     * @generated
     */
    EReference getDescriptionType_Title();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DescriptionType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract</em>'.
     * @see net.opengis.wps10.DescriptionType#getAbstract()
     * @see #getDescriptionType()
     * @generated
     */
    EReference getDescriptionType_Abstract();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.DescriptionType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.wps10.DescriptionType#getMetadata()
     * @see #getDescriptionType()
     * @generated
     */
    EReference getDescriptionType_Metadata();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DocumentOutputDefinitionType <em>Document Output Definition Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Output Definition Type</em>'.
     * @see net.opengis.wps10.DocumentOutputDefinitionType
     * @generated
     */
    EClass getDocumentOutputDefinitionType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentOutputDefinitionType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Title</em>'.
     * @see net.opengis.wps10.DocumentOutputDefinitionType#getTitle()
     * @see #getDocumentOutputDefinitionType()
     * @generated
     */
    EReference getDocumentOutputDefinitionType_Title();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentOutputDefinitionType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract</em>'.
     * @see net.opengis.wps10.DocumentOutputDefinitionType#getAbstract()
     * @see #getDocumentOutputDefinitionType()
     * @generated
     */
    EReference getDocumentOutputDefinitionType_Abstract();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.DocumentOutputDefinitionType#isAsReference <em>As Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>As Reference</em>'.
     * @see net.opengis.wps10.DocumentOutputDefinitionType#isAsReference()
     * @see #getDocumentOutputDefinitionType()
     * @generated
     */
    EAttribute getDocumentOutputDefinitionType_AsReference();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see net.opengis.wps10.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wps10.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.wps10.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map '{@link net.opengis.wps10.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see net.opengis.wps10.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link net.opengis.wps10.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see net.opengis.wps10.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getCapabilities <em>Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Capabilities</em>'.
     * @see net.opengis.wps10.DocumentRoot#getCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Capabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getDescribeProcess <em>Describe Process</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Describe Process</em>'.
     * @see net.opengis.wps10.DocumentRoot#getDescribeProcess()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DescribeProcess();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getExecute <em>Execute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Execute</em>'.
     * @see net.opengis.wps10.DocumentRoot#getExecute()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Execute();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getExecuteResponse <em>Execute Response</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Execute Response</em>'.
     * @see net.opengis.wps10.DocumentRoot#getExecuteResponse()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ExecuteResponse();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
     * @see net.opengis.wps10.DocumentRoot#getGetCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getLanguages <em>Languages</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Languages</em>'.
     * @see net.opengis.wps10.DocumentRoot#getLanguages()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Languages();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getProcessDescriptions <em>Process Descriptions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process Descriptions</em>'.
     * @see net.opengis.wps10.DocumentRoot#getProcessDescriptions()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ProcessDescriptions();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getProcessOfferings <em>Process Offerings</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process Offerings</em>'.
     * @see net.opengis.wps10.DocumentRoot#getProcessOfferings()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ProcessOfferings();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.DocumentRoot#getWSDL <em>WSDL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>WSDL</em>'.
     * @see net.opengis.wps10.DocumentRoot#getWSDL()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_WSDL();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.DocumentRoot#getProcessVersion <em>Process Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Process Version</em>'.
     * @see net.opengis.wps10.DocumentRoot#getProcessVersion()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_ProcessVersion();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ExecuteResponseType <em>Execute Response Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execute Response Type</em>'.
     * @see net.opengis.wps10.ExecuteResponseType
     * @generated
     */
    EClass getExecuteResponseType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ExecuteResponseType#getProcess <em>Process</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process</em>'.
     * @see net.opengis.wps10.ExecuteResponseType#getProcess()
     * @see #getExecuteResponseType()
     * @generated
     */
    EReference getExecuteResponseType_Process();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ExecuteResponseType#getStatus <em>Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Status</em>'.
     * @see net.opengis.wps10.ExecuteResponseType#getStatus()
     * @see #getExecuteResponseType()
     * @generated
     */
    EReference getExecuteResponseType_Status();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ExecuteResponseType#getDataInputs <em>Data Inputs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Inputs</em>'.
     * @see net.opengis.wps10.ExecuteResponseType#getDataInputs()
     * @see #getExecuteResponseType()
     * @generated
     */
    EReference getExecuteResponseType_DataInputs();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ExecuteResponseType#getOutputDefinitions <em>Output Definitions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Output Definitions</em>'.
     * @see net.opengis.wps10.ExecuteResponseType#getOutputDefinitions()
     * @see #getExecuteResponseType()
     * @generated
     */
    EReference getExecuteResponseType_OutputDefinitions();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ExecuteResponseType#getProcessOutputs <em>Process Outputs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process Outputs</em>'.
     * @see net.opengis.wps10.ExecuteResponseType#getProcessOutputs()
     * @see #getExecuteResponseType()
     * @generated
     */
    EReference getExecuteResponseType_ProcessOutputs();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ExecuteResponseType#getServiceInstance <em>Service Instance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service Instance</em>'.
     * @see net.opengis.wps10.ExecuteResponseType#getServiceInstance()
     * @see #getExecuteResponseType()
     * @generated
     */
    EAttribute getExecuteResponseType_ServiceInstance();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ExecuteResponseType#getStatusLocation <em>Status Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Status Location</em>'.
     * @see net.opengis.wps10.ExecuteResponseType#getStatusLocation()
     * @see #getExecuteResponseType()
     * @generated
     */
    EAttribute getExecuteResponseType_StatusLocation();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ExecuteType <em>Execute Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execute Type</em>'.
     * @see net.opengis.wps10.ExecuteType
     * @generated
     */
    EClass getExecuteType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ExecuteType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wps10.ExecuteType#getIdentifier()
     * @see #getExecuteType()
     * @generated
     */
    EReference getExecuteType_Identifier();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ExecuteType#getDataInputs <em>Data Inputs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Inputs</em>'.
     * @see net.opengis.wps10.ExecuteType#getDataInputs()
     * @see #getExecuteType()
     * @generated
     */
    EReference getExecuteType_DataInputs();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ExecuteType#getResponseForm <em>Response Form</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Response Form</em>'.
     * @see net.opengis.wps10.ExecuteType#getResponseForm()
     * @see #getExecuteType()
     * @generated
     */
    EReference getExecuteType_ResponseForm();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Capabilities Type</em>'.
     * @see net.opengis.wps10.GetCapabilitiesType
     * @generated
     */
    EClass getGetCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Accept Versions</em>'.
     * @see net.opengis.wps10.GetCapabilitiesType#getAcceptVersions()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EReference getGetCapabilitiesType_AcceptVersions();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.GetCapabilitiesType#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see net.opengis.wps10.GetCapabilitiesType#getLanguage()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_Language();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.GetCapabilitiesType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wps10.GetCapabilitiesType#getService()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Base Url</em>'.
     * @see net.opengis.wps10.GetCapabilitiesType#getBaseUrl()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_BaseUrl();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extended Properties</em>'.
     * @see net.opengis.wps10.GetCapabilitiesType#getExtendedProperties()
     * @see #getGetCapabilitiesType()
     * @generated
     */
	EAttribute getGetCapabilitiesType_ExtendedProperties();

				/**
     * Returns the meta object for class '{@link net.opengis.wps10.HeaderType <em>Header Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Header Type</em>'.
     * @see net.opengis.wps10.HeaderType
     * @generated
     */
    EClass getHeaderType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.HeaderType#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see net.opengis.wps10.HeaderType#getKey()
     * @see #getHeaderType()
     * @generated
     */
    EAttribute getHeaderType_Key();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.HeaderType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.wps10.HeaderType#getValue()
     * @see #getHeaderType()
     * @generated
     */
    EAttribute getHeaderType_Value();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.InputDescriptionType <em>Input Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Input Description Type</em>'.
     * @see net.opengis.wps10.InputDescriptionType
     * @generated
     */
    EClass getInputDescriptionType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputDescriptionType#getComplexData <em>Complex Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Complex Data</em>'.
     * @see net.opengis.wps10.InputDescriptionType#getComplexData()
     * @see #getInputDescriptionType()
     * @generated
     */
    EReference getInputDescriptionType_ComplexData();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputDescriptionType#getLiteralData <em>Literal Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Literal Data</em>'.
     * @see net.opengis.wps10.InputDescriptionType#getLiteralData()
     * @see #getInputDescriptionType()
     * @generated
     */
    EReference getInputDescriptionType_LiteralData();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputDescriptionType#getBoundingBoxData <em>Bounding Box Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bounding Box Data</em>'.
     * @see net.opengis.wps10.InputDescriptionType#getBoundingBoxData()
     * @see #getInputDescriptionType()
     * @generated
     */
    EReference getInputDescriptionType_BoundingBoxData();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.InputDescriptionType#getMaxOccurs <em>Max Occurs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max Occurs</em>'.
     * @see net.opengis.wps10.InputDescriptionType#getMaxOccurs()
     * @see #getInputDescriptionType()
     * @generated
     */
    EAttribute getInputDescriptionType_MaxOccurs();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.InputDescriptionType#getMinOccurs <em>Min Occurs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Min Occurs</em>'.
     * @see net.opengis.wps10.InputDescriptionType#getMinOccurs()
     * @see #getInputDescriptionType()
     * @generated
     */
    EAttribute getInputDescriptionType_MinOccurs();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.InputReferenceType <em>Input Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Input Reference Type</em>'.
     * @see net.opengis.wps10.InputReferenceType
     * @generated
     */
    EClass getInputReferenceType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.InputReferenceType#getHeader <em>Header</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Header</em>'.
     * @see net.opengis.wps10.InputReferenceType#getHeader()
     * @see #getInputReferenceType()
     * @generated
     */
    EReference getInputReferenceType_Header();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.InputReferenceType#getBody <em>Body</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Body</em>'.
     * @see net.opengis.wps10.InputReferenceType#getBody()
     * @see #getInputReferenceType()
     * @generated
     */
    EAttribute getInputReferenceType_Body();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputReferenceType#getBodyReference <em>Body Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Body Reference</em>'.
     * @see net.opengis.wps10.InputReferenceType#getBodyReference()
     * @see #getInputReferenceType()
     * @generated
     */
    EReference getInputReferenceType_BodyReference();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.InputReferenceType#getEncoding <em>Encoding</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Encoding</em>'.
     * @see net.opengis.wps10.InputReferenceType#getEncoding()
     * @see #getInputReferenceType()
     * @generated
     */
    EAttribute getInputReferenceType_Encoding();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.InputReferenceType#getHref <em>Href</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Href</em>'.
     * @see net.opengis.wps10.InputReferenceType#getHref()
     * @see #getInputReferenceType()
     * @generated
     */
    EAttribute getInputReferenceType_Href();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.InputReferenceType#getMethod <em>Method</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Method</em>'.
     * @see net.opengis.wps10.InputReferenceType#getMethod()
     * @see #getInputReferenceType()
     * @generated
     */
    EAttribute getInputReferenceType_Method();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.InputReferenceType#getMimeType <em>Mime Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mime Type</em>'.
     * @see net.opengis.wps10.InputReferenceType#getMimeType()
     * @see #getInputReferenceType()
     * @generated
     */
    EAttribute getInputReferenceType_MimeType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.InputReferenceType#getSchema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Schema</em>'.
     * @see net.opengis.wps10.InputReferenceType#getSchema()
     * @see #getInputReferenceType()
     * @generated
     */
    EAttribute getInputReferenceType_Schema();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.InputType <em>Input Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Input Type</em>'.
     * @see net.opengis.wps10.InputType
     * @generated
     */
    EClass getInputType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wps10.InputType#getIdentifier()
     * @see #getInputType()
     * @generated
     */
    EReference getInputType_Identifier();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Title</em>'.
     * @see net.opengis.wps10.InputType#getTitle()
     * @see #getInputType()
     * @generated
     */
    EReference getInputType_Title();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract</em>'.
     * @see net.opengis.wps10.InputType#getAbstract()
     * @see #getInputType()
     * @generated
     */
    EReference getInputType_Abstract();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference</em>'.
     * @see net.opengis.wps10.InputType#getReference()
     * @see #getInputType()
     * @generated
     */
    EReference getInputType_Reference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.InputType#getData <em>Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data</em>'.
     * @see net.opengis.wps10.InputType#getData()
     * @see #getInputType()
     * @generated
     */
    EReference getInputType_Data();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.LanguagesType <em>Languages Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Languages Type</em>'.
     * @see net.opengis.wps10.LanguagesType
     * @generated
     */
    EClass getLanguagesType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wps10.LanguagesType#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Language</em>'.
     * @see net.opengis.wps10.LanguagesType#getLanguage()
     * @see #getLanguagesType()
     * @generated
     */
    EAttribute getLanguagesType_Language();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.LanguagesType1 <em>Languages Type1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Languages Type1</em>'.
     * @see net.opengis.wps10.LanguagesType1
     * @generated
     */
    EClass getLanguagesType1();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.LanguagesType1#getDefault <em>Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default</em>'.
     * @see net.opengis.wps10.LanguagesType1#getDefault()
     * @see #getLanguagesType1()
     * @generated
     */
    EReference getLanguagesType1_Default();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.LanguagesType1#getSupported <em>Supported</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Supported</em>'.
     * @see net.opengis.wps10.LanguagesType1#getSupported()
     * @see #getLanguagesType1()
     * @generated
     */
    EReference getLanguagesType1_Supported();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.LiteralDataType <em>Literal Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Literal Data Type</em>'.
     * @see net.opengis.wps10.LiteralDataType
     * @generated
     */
    EClass getLiteralDataType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.LiteralDataType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.wps10.LiteralDataType#getValue()
     * @see #getLiteralDataType()
     * @generated
     */
    EAttribute getLiteralDataType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.LiteralDataType#getDataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Data Type</em>'.
     * @see net.opengis.wps10.LiteralDataType#getDataType()
     * @see #getLiteralDataType()
     * @generated
     */
    EAttribute getLiteralDataType_DataType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.LiteralDataType#getUom <em>Uom</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Uom</em>'.
     * @see net.opengis.wps10.LiteralDataType#getUom()
     * @see #getLiteralDataType()
     * @generated
     */
    EAttribute getLiteralDataType_Uom();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.LiteralInputType <em>Literal Input Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Literal Input Type</em>'.
     * @see net.opengis.wps10.LiteralInputType
     * @generated
     */
    EClass getLiteralInputType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.LiteralInputType#getAllowedValues <em>Allowed Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Allowed Values</em>'.
     * @see net.opengis.wps10.LiteralInputType#getAllowedValues()
     * @see #getLiteralInputType()
     * @generated
     */
    EReference getLiteralInputType_AllowedValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.LiteralInputType#getAnyValue <em>Any Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Any Value</em>'.
     * @see net.opengis.wps10.LiteralInputType#getAnyValue()
     * @see #getLiteralInputType()
     * @generated
     */
    EReference getLiteralInputType_AnyValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.LiteralInputType#getValuesReference <em>Values Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Values Reference</em>'.
     * @see net.opengis.wps10.LiteralInputType#getValuesReference()
     * @see #getLiteralInputType()
     * @generated
     */
    EReference getLiteralInputType_ValuesReference();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.LiteralInputType#getDefaultValue <em>Default Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Default Value</em>'.
     * @see net.opengis.wps10.LiteralInputType#getDefaultValue()
     * @see #getLiteralInputType()
     * @generated
     */
    EAttribute getLiteralInputType_DefaultValue();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.LiteralOutputType <em>Literal Output Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Literal Output Type</em>'.
     * @see net.opengis.wps10.LiteralOutputType
     * @generated
     */
    EClass getLiteralOutputType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.LiteralOutputType#getDataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Type</em>'.
     * @see net.opengis.wps10.LiteralOutputType#getDataType()
     * @see #getLiteralOutputType()
     * @generated
     */
    EReference getLiteralOutputType_DataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.LiteralOutputType#getUOMs <em>UO Ms</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>UO Ms</em>'.
     * @see net.opengis.wps10.LiteralOutputType#getUOMs()
     * @see #getLiteralOutputType()
     * @generated
     */
    EReference getLiteralOutputType_UOMs();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.OutputDataType <em>Output Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Output Data Type</em>'.
     * @see net.opengis.wps10.OutputDataType
     * @generated
     */
    EClass getOutputDataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.OutputDataType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference</em>'.
     * @see net.opengis.wps10.OutputDataType#getReference()
     * @see #getOutputDataType()
     * @generated
     */
    EReference getOutputDataType_Reference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.OutputDataType#getData <em>Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data</em>'.
     * @see net.opengis.wps10.OutputDataType#getData()
     * @see #getOutputDataType()
     * @generated
     */
    EReference getOutputDataType_Data();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.OutputDefinitionsType <em>Output Definitions Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Output Definitions Type</em>'.
     * @see net.opengis.wps10.OutputDefinitionsType
     * @generated
     */
    EClass getOutputDefinitionsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.OutputDefinitionsType#getOutput <em>Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Output</em>'.
     * @see net.opengis.wps10.OutputDefinitionsType#getOutput()
     * @see #getOutputDefinitionsType()
     * @generated
     */
    EReference getOutputDefinitionsType_Output();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.OutputDefinitionType <em>Output Definition Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Output Definition Type</em>'.
     * @see net.opengis.wps10.OutputDefinitionType
     * @generated
     */
    EClass getOutputDefinitionType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.OutputDefinitionType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Identifier</em>'.
     * @see net.opengis.wps10.OutputDefinitionType#getIdentifier()
     * @see #getOutputDefinitionType()
     * @generated
     */
    EReference getOutputDefinitionType_Identifier();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.OutputDefinitionType#getEncoding <em>Encoding</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Encoding</em>'.
     * @see net.opengis.wps10.OutputDefinitionType#getEncoding()
     * @see #getOutputDefinitionType()
     * @generated
     */
    EAttribute getOutputDefinitionType_Encoding();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.OutputDefinitionType#getMimeType <em>Mime Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mime Type</em>'.
     * @see net.opengis.wps10.OutputDefinitionType#getMimeType()
     * @see #getOutputDefinitionType()
     * @generated
     */
    EAttribute getOutputDefinitionType_MimeType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.OutputDefinitionType#getSchema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Schema</em>'.
     * @see net.opengis.wps10.OutputDefinitionType#getSchema()
     * @see #getOutputDefinitionType()
     * @generated
     */
    EAttribute getOutputDefinitionType_Schema();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.OutputDefinitionType#getUom <em>Uom</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Uom</em>'.
     * @see net.opengis.wps10.OutputDefinitionType#getUom()
     * @see #getOutputDefinitionType()
     * @generated
     */
    EAttribute getOutputDefinitionType_Uom();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.OutputDescriptionType <em>Output Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Output Description Type</em>'.
     * @see net.opengis.wps10.OutputDescriptionType
     * @generated
     */
    EClass getOutputDescriptionType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.OutputDescriptionType#getComplexOutput <em>Complex Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Complex Output</em>'.
     * @see net.opengis.wps10.OutputDescriptionType#getComplexOutput()
     * @see #getOutputDescriptionType()
     * @generated
     */
    EReference getOutputDescriptionType_ComplexOutput();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.OutputDescriptionType#getLiteralOutput <em>Literal Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Literal Output</em>'.
     * @see net.opengis.wps10.OutputDescriptionType#getLiteralOutput()
     * @see #getOutputDescriptionType()
     * @generated
     */
    EReference getOutputDescriptionType_LiteralOutput();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.OutputDescriptionType#getBoundingBoxOutput <em>Bounding Box Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Bounding Box Output</em>'.
     * @see net.opengis.wps10.OutputDescriptionType#getBoundingBoxOutput()
     * @see #getOutputDescriptionType()
     * @generated
     */
    EReference getOutputDescriptionType_BoundingBoxOutput();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.OutputReferenceType <em>Output Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Output Reference Type</em>'.
     * @see net.opengis.wps10.OutputReferenceType
     * @generated
     */
    EClass getOutputReferenceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.OutputReferenceType#getEncoding <em>Encoding</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Encoding</em>'.
     * @see net.opengis.wps10.OutputReferenceType#getEncoding()
     * @see #getOutputReferenceType()
     * @generated
     */
    EAttribute getOutputReferenceType_Encoding();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.OutputReferenceType#getHref <em>Href</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Href</em>'.
     * @see net.opengis.wps10.OutputReferenceType#getHref()
     * @see #getOutputReferenceType()
     * @generated
     */
    EAttribute getOutputReferenceType_Href();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.OutputReferenceType#getMimeType <em>Mime Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mime Type</em>'.
     * @see net.opengis.wps10.OutputReferenceType#getMimeType()
     * @see #getOutputReferenceType()
     * @generated
     */
    EAttribute getOutputReferenceType_MimeType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.OutputReferenceType#getSchema <em>Schema</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Schema</em>'.
     * @see net.opengis.wps10.OutputReferenceType#getSchema()
     * @see #getOutputReferenceType()
     * @generated
     */
    EAttribute getOutputReferenceType_Schema();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ProcessBriefType <em>Process Brief Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Brief Type</em>'.
     * @see net.opengis.wps10.ProcessBriefType
     * @generated
     */
    EClass getProcessBriefType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ProcessBriefType#getProfile <em>Profile</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Profile</em>'.
     * @see net.opengis.wps10.ProcessBriefType#getProfile()
     * @see #getProcessBriefType()
     * @generated
     */
    EAttribute getProcessBriefType_Profile();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ProcessBriefType#getWSDL <em>WSDL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>WSDL</em>'.
     * @see net.opengis.wps10.ProcessBriefType#getWSDL()
     * @see #getProcessBriefType()
     * @generated
     */
    EReference getProcessBriefType_WSDL();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ProcessBriefType#getProcessVersion <em>Process Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Process Version</em>'.
     * @see net.opengis.wps10.ProcessBriefType#getProcessVersion()
     * @see #getProcessBriefType()
     * @generated
     */
    EAttribute getProcessBriefType_ProcessVersion();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ProcessDescriptionsType <em>Process Descriptions Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Descriptions Type</em>'.
     * @see net.opengis.wps10.ProcessDescriptionsType
     * @generated
     */
    EClass getProcessDescriptionsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.ProcessDescriptionsType#getProcessDescription <em>Process Description</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Process Description</em>'.
     * @see net.opengis.wps10.ProcessDescriptionsType#getProcessDescription()
     * @see #getProcessDescriptionsType()
     * @generated
     */
    EReference getProcessDescriptionsType_ProcessDescription();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ProcessDescriptionType <em>Process Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Description Type</em>'.
     * @see net.opengis.wps10.ProcessDescriptionType
     * @generated
     */
    EClass getProcessDescriptionType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ProcessDescriptionType#getDataInputs <em>Data Inputs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Inputs</em>'.
     * @see net.opengis.wps10.ProcessDescriptionType#getDataInputs()
     * @see #getProcessDescriptionType()
     * @generated
     */
    EReference getProcessDescriptionType_DataInputs();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ProcessDescriptionType#getProcessOutputs <em>Process Outputs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process Outputs</em>'.
     * @see net.opengis.wps10.ProcessDescriptionType#getProcessOutputs()
     * @see #getProcessDescriptionType()
     * @generated
     */
    EReference getProcessDescriptionType_ProcessOutputs();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ProcessDescriptionType#isStatusSupported <em>Status Supported</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Status Supported</em>'.
     * @see net.opengis.wps10.ProcessDescriptionType#isStatusSupported()
     * @see #getProcessDescriptionType()
     * @generated
     */
    EAttribute getProcessDescriptionType_StatusSupported();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ProcessDescriptionType#isStoreSupported <em>Store Supported</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Store Supported</em>'.
     * @see net.opengis.wps10.ProcessDescriptionType#isStoreSupported()
     * @see #getProcessDescriptionType()
     * @generated
     */
    EAttribute getProcessDescriptionType_StoreSupported();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ProcessFailedType <em>Process Failed Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Failed Type</em>'.
     * @see net.opengis.wps10.ProcessFailedType
     * @generated
     */
    EClass getProcessFailedType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ProcessFailedType#getExceptionReport <em>Exception Report</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Exception Report</em>'.
     * @see net.opengis.wps10.ProcessFailedType#getExceptionReport()
     * @see #getProcessFailedType()
     * @generated
     */
    EReference getProcessFailedType_ExceptionReport();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ProcessOfferingsType <em>Process Offerings Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Offerings Type</em>'.
     * @see net.opengis.wps10.ProcessOfferingsType
     * @generated
     */
    EClass getProcessOfferingsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.ProcessOfferingsType#getProcess <em>Process</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Process</em>'.
     * @see net.opengis.wps10.ProcessOfferingsType#getProcess()
     * @see #getProcessOfferingsType()
     * @generated
     */
    EReference getProcessOfferingsType_Process();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ProcessOutputsType <em>Process Outputs Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Outputs Type</em>'.
     * @see net.opengis.wps10.ProcessOutputsType
     * @generated
     */
    EClass getProcessOutputsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.ProcessOutputsType#getOutput <em>Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Output</em>'.
     * @see net.opengis.wps10.ProcessOutputsType#getOutput()
     * @see #getProcessOutputsType()
     * @generated
     */
    EReference getProcessOutputsType_Output();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ProcessOutputsType1 <em>Process Outputs Type1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Outputs Type1</em>'.
     * @see net.opengis.wps10.ProcessOutputsType1
     * @generated
     */
    EClass getProcessOutputsType1();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.ProcessOutputsType1#getOutput <em>Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Output</em>'.
     * @see net.opengis.wps10.ProcessOutputsType1#getOutput()
     * @see #getProcessOutputsType1()
     * @generated
     */
    EReference getProcessOutputsType1_Output();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ProcessStartedType <em>Process Started Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Process Started Type</em>'.
     * @see net.opengis.wps10.ProcessStartedType
     * @generated
     */
    EClass getProcessStartedType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ProcessStartedType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.wps10.ProcessStartedType#getValue()
     * @see #getProcessStartedType()
     * @generated
     */
    EAttribute getProcessStartedType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ProcessStartedType#getPercentCompleted <em>Percent Completed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Percent Completed</em>'.
     * @see net.opengis.wps10.ProcessStartedType#getPercentCompleted()
     * @see #getProcessStartedType()
     * @generated
     */
    EAttribute getProcessStartedType_PercentCompleted();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.RequestBaseType <em>Request Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Request Base Type</em>'.
     * @see net.opengis.wps10.RequestBaseType
     * @generated
     */
    EClass getRequestBaseType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.RequestBaseType#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see net.opengis.wps10.RequestBaseType#getLanguage()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Language();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.RequestBaseType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wps10.RequestBaseType#getService()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.RequestBaseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.wps10.RequestBaseType#getVersion()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Version();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.RequestBaseType#getBaseUrl <em>Base Url</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Base Url</em>'.
     * @see net.opengis.wps10.RequestBaseType#getBaseUrl()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_BaseUrl();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.RequestBaseType#getExtendedProperties <em>Extended Properties</em>}'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Extended Properties</em>'.
     * @see net.opengis.wps10.RequestBaseType#getExtendedProperties()
     * @see #getRequestBaseType()
     * @generated
     */
	EAttribute getRequestBaseType_ExtendedProperties();

				/**
     * Returns the meta object for class '{@link net.opengis.wps10.ResponseBaseType <em>Response Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Response Base Type</em>'.
     * @see net.opengis.wps10.ResponseBaseType
     * @generated
     */
    EClass getResponseBaseType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ResponseBaseType#getLang <em>Lang</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lang</em>'.
     * @see net.opengis.wps10.ResponseBaseType#getLang()
     * @see #getResponseBaseType()
     * @generated
     */
    EAttribute getResponseBaseType_Lang();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ResponseBaseType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wps10.ResponseBaseType#getService()
     * @see #getResponseBaseType()
     * @generated
     */
    EAttribute getResponseBaseType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ResponseBaseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.wps10.ResponseBaseType#getVersion()
     * @see #getResponseBaseType()
     * @generated
     */
    EAttribute getResponseBaseType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ResponseDocumentType <em>Response Document Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Response Document Type</em>'.
     * @see net.opengis.wps10.ResponseDocumentType
     * @generated
     */
    EClass getResponseDocumentType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wps10.ResponseDocumentType#getOutput <em>Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Output</em>'.
     * @see net.opengis.wps10.ResponseDocumentType#getOutput()
     * @see #getResponseDocumentType()
     * @generated
     */
    EReference getResponseDocumentType_Output();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ResponseDocumentType#isLineage <em>Lineage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lineage</em>'.
     * @see net.opengis.wps10.ResponseDocumentType#isLineage()
     * @see #getResponseDocumentType()
     * @generated
     */
    EAttribute getResponseDocumentType_Lineage();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ResponseDocumentType#isStatus <em>Status</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Status</em>'.
     * @see net.opengis.wps10.ResponseDocumentType#isStatus()
     * @see #getResponseDocumentType()
     * @generated
     */
    EAttribute getResponseDocumentType_Status();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ResponseDocumentType#isStoreExecuteResponse <em>Store Execute Response</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Store Execute Response</em>'.
     * @see net.opengis.wps10.ResponseDocumentType#isStoreExecuteResponse()
     * @see #getResponseDocumentType()
     * @generated
     */
    EAttribute getResponseDocumentType_StoreExecuteResponse();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ResponseFormType <em>Response Form Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Response Form Type</em>'.
     * @see net.opengis.wps10.ResponseFormType
     * @generated
     */
    EClass getResponseFormType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ResponseFormType#getResponseDocument <em>Response Document</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Response Document</em>'.
     * @see net.opengis.wps10.ResponseFormType#getResponseDocument()
     * @see #getResponseFormType()
     * @generated
     */
    EReference getResponseFormType_ResponseDocument();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.ResponseFormType#getRawDataOutput <em>Raw Data Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Raw Data Output</em>'.
     * @see net.opengis.wps10.ResponseFormType#getRawDataOutput()
     * @see #getResponseFormType()
     * @generated
     */
    EReference getResponseFormType_RawDataOutput();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.StatusType <em>Status Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Status Type</em>'.
     * @see net.opengis.wps10.StatusType
     * @generated
     */
    EClass getStatusType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.StatusType#getProcessAccepted <em>Process Accepted</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Process Accepted</em>'.
     * @see net.opengis.wps10.StatusType#getProcessAccepted()
     * @see #getStatusType()
     * @generated
     */
    EAttribute getStatusType_ProcessAccepted();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.StatusType#getProcessStarted <em>Process Started</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process Started</em>'.
     * @see net.opengis.wps10.StatusType#getProcessStarted()
     * @see #getStatusType()
     * @generated
     */
    EReference getStatusType_ProcessStarted();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.StatusType#getProcessPaused <em>Process Paused</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process Paused</em>'.
     * @see net.opengis.wps10.StatusType#getProcessPaused()
     * @see #getStatusType()
     * @generated
     */
    EReference getStatusType_ProcessPaused();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.StatusType#getProcessSucceeded <em>Process Succeeded</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Process Succeeded</em>'.
     * @see net.opengis.wps10.StatusType#getProcessSucceeded()
     * @see #getStatusType()
     * @generated
     */
    EAttribute getStatusType_ProcessSucceeded();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.StatusType#getProcessFailed <em>Process Failed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process Failed</em>'.
     * @see net.opengis.wps10.StatusType#getProcessFailed()
     * @see #getStatusType()
     * @generated
     */
    EReference getStatusType_ProcessFailed();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.StatusType#getCreationTime <em>Creation Time</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Creation Time</em>'.
     * @see net.opengis.wps10.StatusType#getCreationTime()
     * @see #getStatusType()
     * @generated
     */
    EAttribute getStatusType_CreationTime();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.SupportedComplexDataInputType <em>Supported Complex Data Input Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Supported Complex Data Input Type</em>'.
     * @see net.opengis.wps10.SupportedComplexDataInputType
     * @generated
     */
    EClass getSupportedComplexDataInputType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.SupportedComplexDataInputType#getMaximumMegabytes <em>Maximum Megabytes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Maximum Megabytes</em>'.
     * @see net.opengis.wps10.SupportedComplexDataInputType#getMaximumMegabytes()
     * @see #getSupportedComplexDataInputType()
     * @generated
     */
    EAttribute getSupportedComplexDataInputType_MaximumMegabytes();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.SupportedComplexDataType <em>Supported Complex Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Supported Complex Data Type</em>'.
     * @see net.opengis.wps10.SupportedComplexDataType
     * @generated
     */
    EClass getSupportedComplexDataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.SupportedComplexDataType#getDefault <em>Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default</em>'.
     * @see net.opengis.wps10.SupportedComplexDataType#getDefault()
     * @see #getSupportedComplexDataType()
     * @generated
     */
    EReference getSupportedComplexDataType_Default();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.SupportedComplexDataType#getSupported <em>Supported</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Supported</em>'.
     * @see net.opengis.wps10.SupportedComplexDataType#getSupported()
     * @see #getSupportedComplexDataType()
     * @generated
     */
    EReference getSupportedComplexDataType_Supported();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.SupportedCRSsType <em>Supported CR Ss Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Supported CR Ss Type</em>'.
     * @see net.opengis.wps10.SupportedCRSsType
     * @generated
     */
    EClass getSupportedCRSsType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.SupportedCRSsType#getDefault <em>Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default</em>'.
     * @see net.opengis.wps10.SupportedCRSsType#getDefault()
     * @see #getSupportedCRSsType()
     * @generated
     */
    EReference getSupportedCRSsType_Default();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.SupportedCRSsType#getSupported <em>Supported</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Supported</em>'.
     * @see net.opengis.wps10.SupportedCRSsType#getSupported()
     * @see #getSupportedCRSsType()
     * @generated
     */
    EReference getSupportedCRSsType_Supported();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.SupportedUOMsType <em>Supported UO Ms Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Supported UO Ms Type</em>'.
     * @see net.opengis.wps10.SupportedUOMsType
     * @generated
     */
    EClass getSupportedUOMsType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.SupportedUOMsType#getDefault <em>Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default</em>'.
     * @see net.opengis.wps10.SupportedUOMsType#getDefault()
     * @see #getSupportedUOMsType()
     * @generated
     */
    EReference getSupportedUOMsType_Default();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.SupportedUOMsType#getSupported <em>Supported</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Supported</em>'.
     * @see net.opengis.wps10.SupportedUOMsType#getSupported()
     * @see #getSupportedUOMsType()
     * @generated
     */
    EReference getSupportedUOMsType_Supported();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.UOMsType <em>UO Ms Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>UO Ms Type</em>'.
     * @see net.opengis.wps10.UOMsType
     * @generated
     */
    EClass getUOMsType();

    /**
     * Returns the meta object for the reference list '{@link net.opengis.wps10.UOMsType#getUOM <em>UOM</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>UOM</em>'.
     * @see net.opengis.wps10.UOMsType#getUOM()
     * @see #getUOMsType()
     * @generated
     */
    EReference getUOMsType_UOM();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.ValuesReferenceType <em>Values Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Values Reference Type</em>'.
     * @see net.opengis.wps10.ValuesReferenceType
     * @generated
     */
    EClass getValuesReferenceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ValuesReferenceType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reference</em>'.
     * @see net.opengis.wps10.ValuesReferenceType#getReference()
     * @see #getValuesReferenceType()
     * @generated
     */
    EAttribute getValuesReferenceType_Reference();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.ValuesReferenceType#getValuesForm <em>Values Form</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Values Form</em>'.
     * @see net.opengis.wps10.ValuesReferenceType#getValuesForm()
     * @see #getValuesReferenceType()
     * @generated
     */
    EAttribute getValuesReferenceType_ValuesForm();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.WPSCapabilitiesType <em>WPS Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>WPS Capabilities Type</em>'.
     * @see net.opengis.wps10.WPSCapabilitiesType
     * @generated
     */
    EClass getWPSCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.WPSCapabilitiesType#getProcessOfferings <em>Process Offerings</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Process Offerings</em>'.
     * @see net.opengis.wps10.WPSCapabilitiesType#getProcessOfferings()
     * @see #getWPSCapabilitiesType()
     * @generated
     */
    EReference getWPSCapabilitiesType_ProcessOfferings();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.WPSCapabilitiesType#getLanguages <em>Languages</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Languages</em>'.
     * @see net.opengis.wps10.WPSCapabilitiesType#getLanguages()
     * @see #getWPSCapabilitiesType()
     * @generated
     */
    EReference getWPSCapabilitiesType_Languages();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wps10.WPSCapabilitiesType#getWSDL <em>WSDL</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>WSDL</em>'.
     * @see net.opengis.wps10.WPSCapabilitiesType#getWSDL()
     * @see #getWPSCapabilitiesType()
     * @generated
     */
    EReference getWPSCapabilitiesType_WSDL();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.WPSCapabilitiesType#getLang <em>Lang</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lang</em>'.
     * @see net.opengis.wps10.WPSCapabilitiesType#getLang()
     * @see #getWPSCapabilitiesType()
     * @generated
     */
    EAttribute getWPSCapabilitiesType_Lang();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.WPSCapabilitiesType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wps10.WPSCapabilitiesType#getService()
     * @see #getWPSCapabilitiesType()
     * @generated
     */
    EAttribute getWPSCapabilitiesType_Service();

    /**
     * Returns the meta object for class '{@link net.opengis.wps10.WSDLType <em>WSDL Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>WSDL Type</em>'.
     * @see net.opengis.wps10.WSDLType
     * @generated
     */
    EClass getWSDLType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wps10.WSDLType#getHref <em>Href</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Href</em>'.
     * @see net.opengis.wps10.WSDLType#getHref()
     * @see #getWSDLType()
     * @generated
     */
    EAttribute getWSDLType_Href();

    /**
     * Returns the meta object for class '{@link javax.measure.unit.Unit <em>Unit</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unit</em>'.
     * @see javax.measure.unit.Unit
     * @model instanceClass="javax.measure.unit.Unit"
     * @generated
     */
    EClass getUnit();

    /**
     * Returns the meta object for enum '{@link net.opengis.wps10.MethodType <em>Method Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Method Type</em>'.
     * @see net.opengis.wps10.MethodType
     * @generated
     */
    EEnum getMethodType();

    /**
     * Returns the meta object for data type '{@link net.opengis.wps10.MethodType <em>Method Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Method Type Object</em>'.
     * @see net.opengis.wps10.MethodType
     * @model instanceClass="net.opengis.wps10.MethodType"
     *        extendedMetaData="name='method_._type:Object' baseType='method_._type'"
     * @generated
     */
    EDataType getMethodTypeObject();

    /**
     * Returns the meta object for data type '{@link java.math.BigInteger <em>Percent Completed Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Percent Completed Type</em>'.
     * @see java.math.BigInteger
     * @model instanceClass="java.math.BigInteger"
     *        extendedMetaData="name='percentCompleted_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#integer' minInclusive='0' maxInclusive='99'"
     * @generated
     */
    EDataType getPercentCompletedType();

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
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    Wps10Factory getWps10Factory();

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
         * The meta object literal for the '{@link net.opengis.wps10.impl.BodyReferenceTypeImpl <em>Body Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.BodyReferenceTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getBodyReferenceType()
         * @generated
         */
        EClass BODY_REFERENCE_TYPE = eINSTANCE.getBodyReferenceType();

        /**
         * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BODY_REFERENCE_TYPE__HREF = eINSTANCE.getBodyReferenceType_Href();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ComplexDataCombinationsTypeImpl <em>Complex Data Combinations Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ComplexDataCombinationsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getComplexDataCombinationsType()
         * @generated
         */
        EClass COMPLEX_DATA_COMBINATIONS_TYPE = eINSTANCE.getComplexDataCombinationsType();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPLEX_DATA_COMBINATIONS_TYPE__FORMAT = eINSTANCE.getComplexDataCombinationsType_Format();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ComplexDataCombinationTypeImpl <em>Complex Data Combination Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ComplexDataCombinationTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getComplexDataCombinationType()
         * @generated
         */
        EClass COMPLEX_DATA_COMBINATION_TYPE = eINSTANCE.getComplexDataCombinationType();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPLEX_DATA_COMBINATION_TYPE__FORMAT = eINSTANCE.getComplexDataCombinationType_Format();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ComplexDataDescriptionTypeImpl <em>Complex Data Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ComplexDataDescriptionTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getComplexDataDescriptionType()
         * @generated
         */
        EClass COMPLEX_DATA_DESCRIPTION_TYPE = eINSTANCE.getComplexDataDescriptionType();

        /**
         * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPLEX_DATA_DESCRIPTION_TYPE__MIME_TYPE = eINSTANCE.getComplexDataDescriptionType_MimeType();

        /**
         * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPLEX_DATA_DESCRIPTION_TYPE__ENCODING = eINSTANCE.getComplexDataDescriptionType_Encoding();

        /**
         * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPLEX_DATA_DESCRIPTION_TYPE__SCHEMA = eINSTANCE.getComplexDataDescriptionType_Schema();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ComplexDataTypeImpl <em>Complex Data Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ComplexDataTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getComplexDataType()
         * @generated
         */
        EClass COMPLEX_DATA_TYPE = eINSTANCE.getComplexDataType();

        /**
         * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPLEX_DATA_TYPE__ENCODING = eINSTANCE.getComplexDataType_Encoding();

        /**
         * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPLEX_DATA_TYPE__MIME_TYPE = eINSTANCE.getComplexDataType_MimeType();

        /**
         * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPLEX_DATA_TYPE__SCHEMA = eINSTANCE.getComplexDataType_Schema();

        /**
         * The meta object literal for the '<em><b>Data</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPLEX_DATA_TYPE__DATA = eINSTANCE.getComplexDataType_Data();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.CRSsTypeImpl <em>CR Ss Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.CRSsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getCRSsType()
         * @generated
         */
        EClass CR_SS_TYPE = eINSTANCE.getCRSsType();

        /**
         * The meta object literal for the '<em><b>CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CR_SS_TYPE__CRS = eINSTANCE.getCRSsType_CRS();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DataInputsTypeImpl <em>Data Inputs Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DataInputsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDataInputsType()
         * @generated
         */
        EClass DATA_INPUTS_TYPE = eINSTANCE.getDataInputsType();

        /**
         * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_INPUTS_TYPE__INPUT = eINSTANCE.getDataInputsType_Input();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DataInputsType1Impl <em>Data Inputs Type1</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DataInputsType1Impl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDataInputsType1()
         * @generated
         */
        EClass DATA_INPUTS_TYPE1 = eINSTANCE.getDataInputsType1();

        /**
         * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_INPUTS_TYPE1__INPUT = eINSTANCE.getDataInputsType1_Input();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DataTypeImpl <em>Data Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DataTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDataType()
         * @generated
         */
        EClass DATA_TYPE = eINSTANCE.getDataType();

        /**
         * The meta object literal for the '<em><b>Complex Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_TYPE__COMPLEX_DATA = eINSTANCE.getDataType_ComplexData();

        /**
         * The meta object literal for the '<em><b>Literal Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_TYPE__LITERAL_DATA = eINSTANCE.getDataType_LiteralData();

        /**
         * The meta object literal for the '<em><b>Bounding Box Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_TYPE__BOUNDING_BOX_DATA = eINSTANCE.getDataType_BoundingBoxData();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DefaultTypeImpl <em>Default Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DefaultTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDefaultType()
         * @generated
         */
        EClass DEFAULT_TYPE = eINSTANCE.getDefaultType();

        /**
         * The meta object literal for the '<em><b>CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DEFAULT_TYPE__CRS = eINSTANCE.getDefaultType_CRS();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DefaultType1Impl <em>Default Type1</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DefaultType1Impl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDefaultType1()
         * @generated
         */
        EClass DEFAULT_TYPE1 = eINSTANCE.getDefaultType1();

        /**
         * The meta object literal for the '<em><b>UOM</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DEFAULT_TYPE1__UOM = eINSTANCE.getDefaultType1_UOM();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DefaultType2Impl <em>Default Type2</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DefaultType2Impl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDefaultType2()
         * @generated
         */
        EClass DEFAULT_TYPE2 = eINSTANCE.getDefaultType2();

        /**
         * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DEFAULT_TYPE2__LANGUAGE = eINSTANCE.getDefaultType2_Language();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DescribeProcessTypeImpl <em>Describe Process Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DescribeProcessTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDescribeProcessType()
         * @generated
         */
        EClass DESCRIBE_PROCESS_TYPE = eINSTANCE.getDescribeProcessType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIBE_PROCESS_TYPE__IDENTIFIER = eINSTANCE.getDescribeProcessType_Identifier();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DescriptionTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDescriptionType()
         * @generated
         */
        EClass DESCRIPTION_TYPE = eINSTANCE.getDescriptionType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIPTION_TYPE__IDENTIFIER = eINSTANCE.getDescriptionType_Identifier();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIPTION_TYPE__TITLE = eINSTANCE.getDescriptionType_Title();

        /**
         * The meta object literal for the '<em><b>Abstract</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIPTION_TYPE__ABSTRACT = eINSTANCE.getDescriptionType_Abstract();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DESCRIPTION_TYPE__METADATA = eINSTANCE.getDescriptionType_Metadata();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DocumentOutputDefinitionTypeImpl <em>Document Output Definition Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DocumentOutputDefinitionTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDocumentOutputDefinitionType()
         * @generated
         */
        EClass DOCUMENT_OUTPUT_DEFINITION_TYPE = eINSTANCE.getDocumentOutputDefinitionType();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_OUTPUT_DEFINITION_TYPE__TITLE = eINSTANCE.getDocumentOutputDefinitionType_Title();

        /**
         * The meta object literal for the '<em><b>Abstract</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_OUTPUT_DEFINITION_TYPE__ABSTRACT = eINSTANCE.getDocumentOutputDefinitionType_Abstract();

        /**
         * The meta object literal for the '<em><b>As Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_OUTPUT_DEFINITION_TYPE__AS_REFERENCE = eINSTANCE.getDocumentOutputDefinitionType_AsReference();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.DocumentRootImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getDocumentRoot()
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
         * The meta object literal for the '<em><b>Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__CAPABILITIES = eINSTANCE.getDocumentRoot_Capabilities();

        /**
         * The meta object literal for the '<em><b>Describe Process</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DESCRIBE_PROCESS = eINSTANCE.getDocumentRoot_DescribeProcess();

        /**
         * The meta object literal for the '<em><b>Execute</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXECUTE = eINSTANCE.getDocumentRoot_Execute();

        /**
         * The meta object literal for the '<em><b>Execute Response</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXECUTE_RESPONSE = eINSTANCE.getDocumentRoot_ExecuteResponse();

        /**
         * The meta object literal for the '<em><b>Get Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_CAPABILITIES = eINSTANCE.getDocumentRoot_GetCapabilities();

        /**
         * The meta object literal for the '<em><b>Languages</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__LANGUAGES = eINSTANCE.getDocumentRoot_Languages();

        /**
         * The meta object literal for the '<em><b>Process Descriptions</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROCESS_DESCRIPTIONS = eINSTANCE.getDocumentRoot_ProcessDescriptions();

        /**
         * The meta object literal for the '<em><b>Process Offerings</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROCESS_OFFERINGS = eINSTANCE.getDocumentRoot_ProcessOfferings();

        /**
         * The meta object literal for the '<em><b>WSDL</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__WSDL = eINSTANCE.getDocumentRoot_WSDL();

        /**
         * The meta object literal for the '<em><b>Process Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__PROCESS_VERSION = eINSTANCE.getDocumentRoot_ProcessVersion();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ExecuteResponseTypeImpl <em>Execute Response Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ExecuteResponseTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getExecuteResponseType()
         * @generated
         */
        EClass EXECUTE_RESPONSE_TYPE = eINSTANCE.getExecuteResponseType();

        /**
         * The meta object literal for the '<em><b>Process</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTE_RESPONSE_TYPE__PROCESS = eINSTANCE.getExecuteResponseType_Process();

        /**
         * The meta object literal for the '<em><b>Status</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTE_RESPONSE_TYPE__STATUS = eINSTANCE.getExecuteResponseType_Status();

        /**
         * The meta object literal for the '<em><b>Data Inputs</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTE_RESPONSE_TYPE__DATA_INPUTS = eINSTANCE.getExecuteResponseType_DataInputs();

        /**
         * The meta object literal for the '<em><b>Output Definitions</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTE_RESPONSE_TYPE__OUTPUT_DEFINITIONS = eINSTANCE.getExecuteResponseType_OutputDefinitions();

        /**
         * The meta object literal for the '<em><b>Process Outputs</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTE_RESPONSE_TYPE__PROCESS_OUTPUTS = eINSTANCE.getExecuteResponseType_ProcessOutputs();

        /**
         * The meta object literal for the '<em><b>Service Instance</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTE_RESPONSE_TYPE__SERVICE_INSTANCE = eINSTANCE.getExecuteResponseType_ServiceInstance();

        /**
         * The meta object literal for the '<em><b>Status Location</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXECUTE_RESPONSE_TYPE__STATUS_LOCATION = eINSTANCE.getExecuteResponseType_StatusLocation();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ExecuteTypeImpl <em>Execute Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ExecuteTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getExecuteType()
         * @generated
         */
        EClass EXECUTE_TYPE = eINSTANCE.getExecuteType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTE_TYPE__IDENTIFIER = eINSTANCE.getExecuteType_Identifier();

        /**
         * The meta object literal for the '<em><b>Data Inputs</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTE_TYPE__DATA_INPUTS = eINSTANCE.getExecuteType_DataInputs();

        /**
         * The meta object literal for the '<em><b>Response Form</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTE_TYPE__RESPONSE_FORM = eINSTANCE.getExecuteType_ResponseForm();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.GetCapabilitiesTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getGetCapabilitiesType()
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
         * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_CAPABILITIES_TYPE__LANGUAGE = eINSTANCE.getGetCapabilitiesType_Language();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_CAPABILITIES_TYPE__SERVICE = eINSTANCE.getGetCapabilitiesType_Service();

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
         * The meta object literal for the '{@link net.opengis.wps10.impl.HeaderTypeImpl <em>Header Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.HeaderTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getHeaderType()
         * @generated
         */
        EClass HEADER_TYPE = eINSTANCE.getHeaderType();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HEADER_TYPE__KEY = eINSTANCE.getHeaderType_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HEADER_TYPE__VALUE = eINSTANCE.getHeaderType_Value();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.InputDescriptionTypeImpl <em>Input Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.InputDescriptionTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getInputDescriptionType()
         * @generated
         */
        EClass INPUT_DESCRIPTION_TYPE = eINSTANCE.getInputDescriptionType();

        /**
         * The meta object literal for the '<em><b>Complex Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_DESCRIPTION_TYPE__COMPLEX_DATA = eINSTANCE.getInputDescriptionType_ComplexData();

        /**
         * The meta object literal for the '<em><b>Literal Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_DESCRIPTION_TYPE__LITERAL_DATA = eINSTANCE.getInputDescriptionType_LiteralData();

        /**
         * The meta object literal for the '<em><b>Bounding Box Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_DESCRIPTION_TYPE__BOUNDING_BOX_DATA = eINSTANCE.getInputDescriptionType_BoundingBoxData();

        /**
         * The meta object literal for the '<em><b>Max Occurs</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INPUT_DESCRIPTION_TYPE__MAX_OCCURS = eINSTANCE.getInputDescriptionType_MaxOccurs();

        /**
         * The meta object literal for the '<em><b>Min Occurs</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INPUT_DESCRIPTION_TYPE__MIN_OCCURS = eINSTANCE.getInputDescriptionType_MinOccurs();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.InputReferenceTypeImpl <em>Input Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.InputReferenceTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getInputReferenceType()
         * @generated
         */
        EClass INPUT_REFERENCE_TYPE = eINSTANCE.getInputReferenceType();

        /**
         * The meta object literal for the '<em><b>Header</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_REFERENCE_TYPE__HEADER = eINSTANCE.getInputReferenceType_Header();

        /**
         * The meta object literal for the '<em><b>Body</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INPUT_REFERENCE_TYPE__BODY = eINSTANCE.getInputReferenceType_Body();

        /**
         * The meta object literal for the '<em><b>Body Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_REFERENCE_TYPE__BODY_REFERENCE = eINSTANCE.getInputReferenceType_BodyReference();

        /**
         * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INPUT_REFERENCE_TYPE__ENCODING = eINSTANCE.getInputReferenceType_Encoding();

        /**
         * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INPUT_REFERENCE_TYPE__HREF = eINSTANCE.getInputReferenceType_Href();

        /**
         * The meta object literal for the '<em><b>Method</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INPUT_REFERENCE_TYPE__METHOD = eINSTANCE.getInputReferenceType_Method();

        /**
         * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INPUT_REFERENCE_TYPE__MIME_TYPE = eINSTANCE.getInputReferenceType_MimeType();

        /**
         * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INPUT_REFERENCE_TYPE__SCHEMA = eINSTANCE.getInputReferenceType_Schema();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.InputTypeImpl <em>Input Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.InputTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getInputType()
         * @generated
         */
        EClass INPUT_TYPE = eINSTANCE.getInputType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_TYPE__IDENTIFIER = eINSTANCE.getInputType_Identifier();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_TYPE__TITLE = eINSTANCE.getInputType_Title();

        /**
         * The meta object literal for the '<em><b>Abstract</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_TYPE__ABSTRACT = eINSTANCE.getInputType_Abstract();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_TYPE__REFERENCE = eINSTANCE.getInputType_Reference();

        /**
         * The meta object literal for the '<em><b>Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INPUT_TYPE__DATA = eINSTANCE.getInputType_Data();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.LanguagesTypeImpl <em>Languages Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.LanguagesTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getLanguagesType()
         * @generated
         */
        EClass LANGUAGES_TYPE = eINSTANCE.getLanguagesType();

        /**
         * The meta object literal for the '<em><b>Language</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LANGUAGES_TYPE__LANGUAGE = eINSTANCE.getLanguagesType_Language();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.LanguagesType1Impl <em>Languages Type1</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.LanguagesType1Impl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getLanguagesType1()
         * @generated
         */
        EClass LANGUAGES_TYPE1 = eINSTANCE.getLanguagesType1();

        /**
         * The meta object literal for the '<em><b>Default</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LANGUAGES_TYPE1__DEFAULT = eINSTANCE.getLanguagesType1_Default();

        /**
         * The meta object literal for the '<em><b>Supported</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LANGUAGES_TYPE1__SUPPORTED = eINSTANCE.getLanguagesType1_Supported();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.LiteralDataTypeImpl <em>Literal Data Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.LiteralDataTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getLiteralDataType()
         * @generated
         */
        EClass LITERAL_DATA_TYPE = eINSTANCE.getLiteralDataType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LITERAL_DATA_TYPE__VALUE = eINSTANCE.getLiteralDataType_Value();

        /**
         * The meta object literal for the '<em><b>Data Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LITERAL_DATA_TYPE__DATA_TYPE = eINSTANCE.getLiteralDataType_DataType();

        /**
         * The meta object literal for the '<em><b>Uom</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LITERAL_DATA_TYPE__UOM = eINSTANCE.getLiteralDataType_Uom();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.LiteralInputTypeImpl <em>Literal Input Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.LiteralInputTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getLiteralInputType()
         * @generated
         */
        EClass LITERAL_INPUT_TYPE = eINSTANCE.getLiteralInputType();

        /**
         * The meta object literal for the '<em><b>Allowed Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LITERAL_INPUT_TYPE__ALLOWED_VALUES = eINSTANCE.getLiteralInputType_AllowedValues();

        /**
         * The meta object literal for the '<em><b>Any Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LITERAL_INPUT_TYPE__ANY_VALUE = eINSTANCE.getLiteralInputType_AnyValue();

        /**
         * The meta object literal for the '<em><b>Values Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LITERAL_INPUT_TYPE__VALUES_REFERENCE = eINSTANCE.getLiteralInputType_ValuesReference();

        /**
         * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LITERAL_INPUT_TYPE__DEFAULT_VALUE = eINSTANCE.getLiteralInputType_DefaultValue();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.LiteralOutputTypeImpl <em>Literal Output Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.LiteralOutputTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getLiteralOutputType()
         * @generated
         */
        EClass LITERAL_OUTPUT_TYPE = eINSTANCE.getLiteralOutputType();

        /**
         * The meta object literal for the '<em><b>Data Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LITERAL_OUTPUT_TYPE__DATA_TYPE = eINSTANCE.getLiteralOutputType_DataType();

        /**
         * The meta object literal for the '<em><b>UO Ms</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LITERAL_OUTPUT_TYPE__UO_MS = eINSTANCE.getLiteralOutputType_UOMs();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.OutputDataTypeImpl <em>Output Data Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.OutputDataTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputDataType()
         * @generated
         */
        EClass OUTPUT_DATA_TYPE = eINSTANCE.getOutputDataType();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OUTPUT_DATA_TYPE__REFERENCE = eINSTANCE.getOutputDataType_Reference();

        /**
         * The meta object literal for the '<em><b>Data</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OUTPUT_DATA_TYPE__DATA = eINSTANCE.getOutputDataType_Data();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.OutputDefinitionsTypeImpl <em>Output Definitions Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.OutputDefinitionsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputDefinitionsType()
         * @generated
         */
        EClass OUTPUT_DEFINITIONS_TYPE = eINSTANCE.getOutputDefinitionsType();

        /**
         * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OUTPUT_DEFINITIONS_TYPE__OUTPUT = eINSTANCE.getOutputDefinitionsType_Output();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.OutputDefinitionTypeImpl <em>Output Definition Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.OutputDefinitionTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputDefinitionType()
         * @generated
         */
        EClass OUTPUT_DEFINITION_TYPE = eINSTANCE.getOutputDefinitionType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OUTPUT_DEFINITION_TYPE__IDENTIFIER = eINSTANCE.getOutputDefinitionType_Identifier();

        /**
         * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_DEFINITION_TYPE__ENCODING = eINSTANCE.getOutputDefinitionType_Encoding();

        /**
         * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_DEFINITION_TYPE__MIME_TYPE = eINSTANCE.getOutputDefinitionType_MimeType();

        /**
         * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_DEFINITION_TYPE__SCHEMA = eINSTANCE.getOutputDefinitionType_Schema();

        /**
         * The meta object literal for the '<em><b>Uom</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_DEFINITION_TYPE__UOM = eINSTANCE.getOutputDefinitionType_Uom();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.OutputDescriptionTypeImpl <em>Output Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.OutputDescriptionTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputDescriptionType()
         * @generated
         */
        EClass OUTPUT_DESCRIPTION_TYPE = eINSTANCE.getOutputDescriptionType();

        /**
         * The meta object literal for the '<em><b>Complex Output</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OUTPUT_DESCRIPTION_TYPE__COMPLEX_OUTPUT = eINSTANCE.getOutputDescriptionType_ComplexOutput();

        /**
         * The meta object literal for the '<em><b>Literal Output</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OUTPUT_DESCRIPTION_TYPE__LITERAL_OUTPUT = eINSTANCE.getOutputDescriptionType_LiteralOutput();

        /**
         * The meta object literal for the '<em><b>Bounding Box Output</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OUTPUT_DESCRIPTION_TYPE__BOUNDING_BOX_OUTPUT = eINSTANCE.getOutputDescriptionType_BoundingBoxOutput();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.OutputReferenceTypeImpl <em>Output Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.OutputReferenceTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getOutputReferenceType()
         * @generated
         */
        EClass OUTPUT_REFERENCE_TYPE = eINSTANCE.getOutputReferenceType();

        /**
         * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_REFERENCE_TYPE__ENCODING = eINSTANCE.getOutputReferenceType_Encoding();

        /**
         * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_REFERENCE_TYPE__HREF = eINSTANCE.getOutputReferenceType_Href();

        /**
         * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_REFERENCE_TYPE__MIME_TYPE = eINSTANCE.getOutputReferenceType_MimeType();

        /**
         * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_REFERENCE_TYPE__SCHEMA = eINSTANCE.getOutputReferenceType_Schema();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ProcessBriefTypeImpl <em>Process Brief Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ProcessBriefTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessBriefType()
         * @generated
         */
        EClass PROCESS_BRIEF_TYPE = eINSTANCE.getProcessBriefType();

        /**
         * The meta object literal for the '<em><b>Profile</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROCESS_BRIEF_TYPE__PROFILE = eINSTANCE.getProcessBriefType_Profile();

        /**
         * The meta object literal for the '<em><b>WSDL</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_BRIEF_TYPE__WSDL = eINSTANCE.getProcessBriefType_WSDL();

        /**
         * The meta object literal for the '<em><b>Process Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROCESS_BRIEF_TYPE__PROCESS_VERSION = eINSTANCE.getProcessBriefType_ProcessVersion();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ProcessDescriptionsTypeImpl <em>Process Descriptions Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ProcessDescriptionsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessDescriptionsType()
         * @generated
         */
        EClass PROCESS_DESCRIPTIONS_TYPE = eINSTANCE.getProcessDescriptionsType();

        /**
         * The meta object literal for the '<em><b>Process Description</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_DESCRIPTIONS_TYPE__PROCESS_DESCRIPTION = eINSTANCE.getProcessDescriptionsType_ProcessDescription();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ProcessDescriptionTypeImpl <em>Process Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ProcessDescriptionTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessDescriptionType()
         * @generated
         */
        EClass PROCESS_DESCRIPTION_TYPE = eINSTANCE.getProcessDescriptionType();

        /**
         * The meta object literal for the '<em><b>Data Inputs</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_DESCRIPTION_TYPE__DATA_INPUTS = eINSTANCE.getProcessDescriptionType_DataInputs();

        /**
         * The meta object literal for the '<em><b>Process Outputs</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS = eINSTANCE.getProcessDescriptionType_ProcessOutputs();

        /**
         * The meta object literal for the '<em><b>Status Supported</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED = eINSTANCE.getProcessDescriptionType_StatusSupported();

        /**
         * The meta object literal for the '<em><b>Store Supported</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED = eINSTANCE.getProcessDescriptionType_StoreSupported();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ProcessFailedTypeImpl <em>Process Failed Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ProcessFailedTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessFailedType()
         * @generated
         */
        EClass PROCESS_FAILED_TYPE = eINSTANCE.getProcessFailedType();

        /**
         * The meta object literal for the '<em><b>Exception Report</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_FAILED_TYPE__EXCEPTION_REPORT = eINSTANCE.getProcessFailedType_ExceptionReport();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ProcessOfferingsTypeImpl <em>Process Offerings Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ProcessOfferingsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessOfferingsType()
         * @generated
         */
        EClass PROCESS_OFFERINGS_TYPE = eINSTANCE.getProcessOfferingsType();

        /**
         * The meta object literal for the '<em><b>Process</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_OFFERINGS_TYPE__PROCESS = eINSTANCE.getProcessOfferingsType_Process();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ProcessOutputsTypeImpl <em>Process Outputs Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ProcessOutputsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessOutputsType()
         * @generated
         */
        EClass PROCESS_OUTPUTS_TYPE = eINSTANCE.getProcessOutputsType();

        /**
         * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_OUTPUTS_TYPE__OUTPUT = eINSTANCE.getProcessOutputsType_Output();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ProcessOutputsType1Impl <em>Process Outputs Type1</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ProcessOutputsType1Impl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessOutputsType1()
         * @generated
         */
        EClass PROCESS_OUTPUTS_TYPE1 = eINSTANCE.getProcessOutputsType1();

        /**
         * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROCESS_OUTPUTS_TYPE1__OUTPUT = eINSTANCE.getProcessOutputsType1_Output();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ProcessStartedTypeImpl <em>Process Started Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ProcessStartedTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getProcessStartedType()
         * @generated
         */
        EClass PROCESS_STARTED_TYPE = eINSTANCE.getProcessStartedType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROCESS_STARTED_TYPE__VALUE = eINSTANCE.getProcessStartedType_Value();

        /**
         * The meta object literal for the '<em><b>Percent Completed</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROCESS_STARTED_TYPE__PERCENT_COMPLETED = eINSTANCE.getProcessStartedType_PercentCompleted();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.RequestBaseTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getRequestBaseType()
         * @generated
         */
        EClass REQUEST_BASE_TYPE = eINSTANCE.getRequestBaseType();

        /**
         * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_BASE_TYPE__LANGUAGE = eINSTANCE.getRequestBaseType_Language();

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
         * The meta object literal for the '{@link net.opengis.wps10.impl.ResponseBaseTypeImpl <em>Response Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ResponseBaseTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getResponseBaseType()
         * @generated
         */
        EClass RESPONSE_BASE_TYPE = eINSTANCE.getResponseBaseType();

        /**
         * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESPONSE_BASE_TYPE__LANG = eINSTANCE.getResponseBaseType_Lang();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESPONSE_BASE_TYPE__SERVICE = eINSTANCE.getResponseBaseType_Service();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESPONSE_BASE_TYPE__VERSION = eINSTANCE.getResponseBaseType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ResponseDocumentTypeImpl <em>Response Document Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ResponseDocumentTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getResponseDocumentType()
         * @generated
         */
        EClass RESPONSE_DOCUMENT_TYPE = eINSTANCE.getResponseDocumentType();

        /**
         * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESPONSE_DOCUMENT_TYPE__OUTPUT = eINSTANCE.getResponseDocumentType_Output();

        /**
         * The meta object literal for the '<em><b>Lineage</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESPONSE_DOCUMENT_TYPE__LINEAGE = eINSTANCE.getResponseDocumentType_Lineage();

        /**
         * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESPONSE_DOCUMENT_TYPE__STATUS = eINSTANCE.getResponseDocumentType_Status();

        /**
         * The meta object literal for the '<em><b>Store Execute Response</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE = eINSTANCE.getResponseDocumentType_StoreExecuteResponse();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ResponseFormTypeImpl <em>Response Form Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ResponseFormTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getResponseFormType()
         * @generated
         */
        EClass RESPONSE_FORM_TYPE = eINSTANCE.getResponseFormType();

        /**
         * The meta object literal for the '<em><b>Response Document</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT = eINSTANCE.getResponseFormType_ResponseDocument();

        /**
         * The meta object literal for the '<em><b>Raw Data Output</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT = eINSTANCE.getResponseFormType_RawDataOutput();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.StatusTypeImpl <em>Status Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.StatusTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getStatusType()
         * @generated
         */
        EClass STATUS_TYPE = eINSTANCE.getStatusType();

        /**
         * The meta object literal for the '<em><b>Process Accepted</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STATUS_TYPE__PROCESS_ACCEPTED = eINSTANCE.getStatusType_ProcessAccepted();

        /**
         * The meta object literal for the '<em><b>Process Started</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference STATUS_TYPE__PROCESS_STARTED = eINSTANCE.getStatusType_ProcessStarted();

        /**
         * The meta object literal for the '<em><b>Process Paused</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference STATUS_TYPE__PROCESS_PAUSED = eINSTANCE.getStatusType_ProcessPaused();

        /**
         * The meta object literal for the '<em><b>Process Succeeded</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STATUS_TYPE__PROCESS_SUCCEEDED = eINSTANCE.getStatusType_ProcessSucceeded();

        /**
         * The meta object literal for the '<em><b>Process Failed</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference STATUS_TYPE__PROCESS_FAILED = eINSTANCE.getStatusType_ProcessFailed();

        /**
         * The meta object literal for the '<em><b>Creation Time</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute STATUS_TYPE__CREATION_TIME = eINSTANCE.getStatusType_CreationTime();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.SupportedComplexDataInputTypeImpl <em>Supported Complex Data Input Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.SupportedComplexDataInputTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getSupportedComplexDataInputType()
         * @generated
         */
        EClass SUPPORTED_COMPLEX_DATA_INPUT_TYPE = eINSTANCE.getSupportedComplexDataInputType();

        /**
         * The meta object literal for the '<em><b>Maximum Megabytes</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SUPPORTED_COMPLEX_DATA_INPUT_TYPE__MAXIMUM_MEGABYTES = eINSTANCE.getSupportedComplexDataInputType_MaximumMegabytes();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.SupportedComplexDataTypeImpl <em>Supported Complex Data Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.SupportedComplexDataTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getSupportedComplexDataType()
         * @generated
         */
        EClass SUPPORTED_COMPLEX_DATA_TYPE = eINSTANCE.getSupportedComplexDataType();

        /**
         * The meta object literal for the '<em><b>Default</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT = eINSTANCE.getSupportedComplexDataType_Default();

        /**
         * The meta object literal for the '<em><b>Supported</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED = eINSTANCE.getSupportedComplexDataType_Supported();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.SupportedCRSsTypeImpl <em>Supported CR Ss Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.SupportedCRSsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getSupportedCRSsType()
         * @generated
         */
        EClass SUPPORTED_CR_SS_TYPE = eINSTANCE.getSupportedCRSsType();

        /**
         * The meta object literal for the '<em><b>Default</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUPPORTED_CR_SS_TYPE__DEFAULT = eINSTANCE.getSupportedCRSsType_Default();

        /**
         * The meta object literal for the '<em><b>Supported</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUPPORTED_CR_SS_TYPE__SUPPORTED = eINSTANCE.getSupportedCRSsType_Supported();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.SupportedUOMsTypeImpl <em>Supported UO Ms Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.SupportedUOMsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getSupportedUOMsType()
         * @generated
         */
        EClass SUPPORTED_UO_MS_TYPE = eINSTANCE.getSupportedUOMsType();

        /**
         * The meta object literal for the '<em><b>Default</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUPPORTED_UO_MS_TYPE__DEFAULT = eINSTANCE.getSupportedUOMsType_Default();

        /**
         * The meta object literal for the '<em><b>Supported</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SUPPORTED_UO_MS_TYPE__SUPPORTED = eINSTANCE.getSupportedUOMsType_Supported();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.UOMsTypeImpl <em>UO Ms Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.UOMsTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getUOMsType()
         * @generated
         */
        EClass UO_MS_TYPE = eINSTANCE.getUOMsType();

        /**
         * The meta object literal for the '<em><b>UOM</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UO_MS_TYPE__UOM = eINSTANCE.getUOMsType_UOM();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.ValuesReferenceTypeImpl <em>Values Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.ValuesReferenceTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getValuesReferenceType()
         * @generated
         */
        EClass VALUES_REFERENCE_TYPE = eINSTANCE.getValuesReferenceType();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUES_REFERENCE_TYPE__REFERENCE = eINSTANCE.getValuesReferenceType_Reference();

        /**
         * The meta object literal for the '<em><b>Values Form</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUES_REFERENCE_TYPE__VALUES_FORM = eINSTANCE.getValuesReferenceType_ValuesForm();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.WPSCapabilitiesTypeImpl <em>WPS Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.WPSCapabilitiesTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getWPSCapabilitiesType()
         * @generated
         */
        EClass WPS_CAPABILITIES_TYPE = eINSTANCE.getWPSCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Process Offerings</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS = eINSTANCE.getWPSCapabilitiesType_ProcessOfferings();

        /**
         * The meta object literal for the '<em><b>Languages</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WPS_CAPABILITIES_TYPE__LANGUAGES = eINSTANCE.getWPSCapabilitiesType_Languages();

        /**
         * The meta object literal for the '<em><b>WSDL</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference WPS_CAPABILITIES_TYPE__WSDL = eINSTANCE.getWPSCapabilitiesType_WSDL();

        /**
         * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WPS_CAPABILITIES_TYPE__LANG = eINSTANCE.getWPSCapabilitiesType_Lang();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WPS_CAPABILITIES_TYPE__SERVICE = eINSTANCE.getWPSCapabilitiesType_Service();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.impl.WSDLTypeImpl <em>WSDL Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.impl.WSDLTypeImpl
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getWSDLType()
         * @generated
         */
        EClass WSDL_TYPE = eINSTANCE.getWSDLType();

        /**
         * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute WSDL_TYPE__HREF = eINSTANCE.getWSDLType_Href();

        /**
         * The meta object literal for the '{@link javax.measure.unit.Unit <em>Unit</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see javax.measure.unit.Unit
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getUnit()
         * @generated
         */
        EClass UNIT = eINSTANCE.getUnit();

        /**
         * The meta object literal for the '{@link net.opengis.wps10.MethodType <em>Method Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.MethodType
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getMethodType()
         * @generated
         */
        EEnum METHOD_TYPE = eINSTANCE.getMethodType();

        /**
         * The meta object literal for the '<em>Method Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wps10.MethodType
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getMethodTypeObject()
         * @generated
         */
        EDataType METHOD_TYPE_OBJECT = eINSTANCE.getMethodTypeObject();

        /**
         * The meta object literal for the '<em>Percent Completed Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.math.BigInteger
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getPercentCompletedType()
         * @generated
         */
        EDataType PERCENT_COMPLETED_TYPE = eINSTANCE.getPercentCompletedType();

								/**
         * The meta object literal for the '<em>Map</em>' data type.
         * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
         * @see java.util.Map
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getMap()
         * @generated
         */
		EDataType MAP = eINSTANCE.getMap();

                                /**
         * The meta object literal for the '<em>QName</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see javax.xml.namespace.QName
         * @see net.opengis.wps10.impl.Wps10PackageImpl#getQName()
         * @generated
         */
        EDataType QNAME = eINSTANCE.getQName();

    }

} //Wps10Package
