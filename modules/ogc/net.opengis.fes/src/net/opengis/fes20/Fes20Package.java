/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

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
 * 
 *          Filter Encoding is an OGC Standard.
 *          Copyright (c) 2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 *          To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * 
 *          Filter Encoding is an OGC Standard.
 *          Copyright (c) 2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 *          To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * 
 *          Filter Encoding is an OGC Standard.
 *          Copyright (c) 2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 *          To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * 
 *          This XML Schema defines OGC query filter capabilities documents.
 * 
 *          Filter Encoding is an OGC Standard.
 *          Copyright (c) 2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 *          To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document includes and imports, directly and indirectly, all the XML Schemas defined by the OWS Common Implemetation Specification.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document encodes the GetResourceByID operation request message. This typical operation is specified as a base for profiling in specific OWS specifications. For information on the allowed changes and limitations in such profiling, see Subclause 9.4.1 of the OWS Common specification.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document encodes the parts of the MD_DataIdentification class of ISO 19115 (OGC Abstract Specification Topic 11) which are expected to be used for most datasets. This Schema also encodes the parts of this class that are expected to be useful for other metadata. Both may be used within the Contents section of OWS service metadata (Capabilities) documents.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document encodes various parameters and parameter types that can be used in OWS operation requests and responses.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * 
 * 			GML 3.0 candidate xlinks schema. Copyright (c) 2001 OGC, All Rights Reserved.
 * 
 * This XML Schema Document encodes the parts of ISO 19115 used by the common "ServiceIdentification" and "ServiceProvider" sections of the GetCapabilities operation response, known as the service metadata XML document. The parts encoded here are the MD_Keywords, CI_ResponsibleParty, and related classes. The UML package prefixes were omitted from XML names, and the XML element names were all capitalized, for consistency with other OWS Schemas. This document also provides a simple coding of text in multiple languages, simplified from Annex J of ISO 19115.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
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
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document encodes the common "ServiceIdentification" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceIdentification class of ISO 19119 (OGC Abstract Specification Topic 12).
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document encodes the common "ServiceProvider" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceProvider class of ISO 19119 (OGC Abstract Specification Topic 12).
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document encodes the basic contents of the "OperationsMetadata" section of the GetCapabilities operation response, also known as the Capabilities XML document.
 * 
 * 			OWS is an OGC Standard.
 * 			Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document encodes the allowed values (or domain) of a quantity, often for an input or output parameter to an OWS. Such a parameter is sometimes called a variable, quantity, literal, or typed literal. Such a parameter can use one of many data types, including double, integer, boolean, string, or URI. The allowed values can also be encoded for a quantity that is not explicit or not transferred, but is constrained by a server implementation.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document encodes the Exception Report response to all OWS operations.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema  Document encodes the typical Contents section of an OWS service metadata (Capabilities) document. This  Schema can be built upon to define the Contents section for a specific OWS. If the ContentsBaseType in this XML Schema cannot be restricted and extended to define the Contents section for a specific OWS, all other relevant parts defined in owsContents.xsd shall be used by the "ContentsType" in the wxsContents.xsd prepared for the specific OWS.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document specifies types and elements for input and output of operation data, allowing including multiple data items with each data item either included or referenced. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * This XML Schema Document specifies types and elements for document or resource references and for package manifests that contain multiple references. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 
 * 		OWS is an OGC Standard.
 * 		Copyright (c) 2006,2010 Open Geospatial Consortium, Inc. All Rights Reserved.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * <!-- end-model-doc -->
 * @see net.opengis.fes20.Fes20Factory
 * @model kind="package"
 *        annotation="urn:opengis:specification:gml:schema-xlinks:v3.0c2 appinfo='xlinks.xsd v3.0b2 2001-07'"
 * @generated
 */
public interface Fes20Package extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "fes20";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.opengis.net/fes/2.0";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "fes20";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    Fes20Package eINSTANCE = net.opengis.fes20.impl.Fes20PackageImpl.init();

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AbstractQueryExpressionTypeImpl <em>Abstract Query Expression Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AbstractQueryExpressionTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractQueryExpressionType()
     * @generated
     */
    int ABSTRACT_QUERY_EXPRESSION_TYPE = 3;

    /**
     * The feature id for the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_QUERY_EXPRESSION_TYPE__HANDLE = 0;

    /**
     * The number of structural features of the '<em>Abstract Query Expression Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_QUERY_EXPRESSION_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl <em>Abstract Adhoc Query Expression Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractAdhocQueryExpressionType()
     * @generated
     */
    int ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE = 0;

    /**
     * The feature id for the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__HANDLE = ABSTRACT_QUERY_EXPRESSION_TYPE__HANDLE;

    /**
     * The feature id for the '<em><b>Abstract Projection Clause</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_PROJECTION_CLAUSE = ABSTRACT_QUERY_EXPRESSION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Abstract Selection Clause</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SELECTION_CLAUSE = ABSTRACT_QUERY_EXPRESSION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Abstract Sorting Clause</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SORTING_CLAUSE = ABSTRACT_QUERY_EXPRESSION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Aliases</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ALIASES = ABSTRACT_QUERY_EXPRESSION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Type Names</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__TYPE_NAMES = ABSTRACT_QUERY_EXPRESSION_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Abstract Adhoc Query Expression Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE_FEATURE_COUNT = ABSTRACT_QUERY_EXPRESSION_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AbstractIdTypeImpl <em>Abstract Id Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AbstractIdTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractIdType()
     * @generated
     */
    int ABSTRACT_ID_TYPE = 1;

    /**
     * The number of structural features of the '<em>Abstract Id Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ID_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AbstractProjectionClauseTypeImpl <em>Abstract Projection Clause Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AbstractProjectionClauseTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractProjectionClauseType()
     * @generated
     */
    int ABSTRACT_PROJECTION_CLAUSE_TYPE = 2;

    /**
     * The number of structural features of the '<em>Abstract Projection Clause Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_PROJECTION_CLAUSE_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AbstractSelectionClauseTypeImpl <em>Abstract Selection Clause Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AbstractSelectionClauseTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractSelectionClauseType()
     * @generated
     */
    int ABSTRACT_SELECTION_CLAUSE_TYPE = 4;

    /**
     * The number of structural features of the '<em>Abstract Selection Clause Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AbstractSortingClauseTypeImpl <em>Abstract Sorting Clause Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AbstractSortingClauseTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractSortingClauseType()
     * @generated
     */
    int ABSTRACT_SORTING_CLAUSE_TYPE = 5;

    /**
     * The number of structural features of the '<em>Abstract Sorting Clause Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_SORTING_CLAUSE_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AdditionalOperatorsTypeImpl <em>Additional Operators Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AdditionalOperatorsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAdditionalOperatorsType()
     * @generated
     */
    int ADDITIONAL_OPERATORS_TYPE = 6;

    /**
     * The feature id for the '<em><b>Operator</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADDITIONAL_OPERATORS_TYPE__OPERATOR = 0;

    /**
     * The number of structural features of the '<em>Additional Operators Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ADDITIONAL_OPERATORS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ArgumentsTypeImpl <em>Arguments Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ArgumentsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getArgumentsType()
     * @generated
     */
    int ARGUMENTS_TYPE = 7;

    /**
     * The feature id for the '<em><b>Argument</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARGUMENTS_TYPE__ARGUMENT = 0;

    /**
     * The number of structural features of the '<em>Arguments Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARGUMENTS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ArgumentTypeImpl <em>Argument Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ArgumentTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getArgumentType()
     * @generated
     */
    int ARGUMENT_TYPE = 8;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARGUMENT_TYPE__METADATA = 0;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARGUMENT_TYPE__TYPE = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARGUMENT_TYPE__NAME = 2;

    /**
     * The number of structural features of the '<em>Argument Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ARGUMENT_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AvailableFunctionsTypeImpl <em>Available Functions Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AvailableFunctionsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAvailableFunctionsType()
     * @generated
     */
    int AVAILABLE_FUNCTIONS_TYPE = 9;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_FUNCTIONS_TYPE__FUNCTION = 0;

    /**
     * The number of structural features of the '<em>Available Functions Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_FUNCTIONS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.AvailableFunctionTypeImpl <em>Available Function Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.AvailableFunctionTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAvailableFunctionType()
     * @generated
     */
    int AVAILABLE_FUNCTION_TYPE = 10;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_FUNCTION_TYPE__METADATA = 0;

    /**
     * The feature id for the '<em><b>Returns</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_FUNCTION_TYPE__RETURNS = 1;

    /**
     * The feature id for the '<em><b>Arguments</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_FUNCTION_TYPE__ARGUMENTS = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_FUNCTION_TYPE__NAME = 3;

    /**
     * The number of structural features of the '<em>Available Function Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_FUNCTION_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.SpatialOpsTypeImpl <em>Spatial Ops Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.SpatialOpsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOpsType()
     * @generated
     */
    int SPATIAL_OPS_TYPE = 48;

    /**
     * The number of structural features of the '<em>Spatial Ops Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_OPS_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.BBOXTypeImpl <em>BBOX Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.BBOXTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getBBOXType()
     * @generated
     */
    int BBOX_TYPE = 11;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BBOX_TYPE__EXPRESSION_GROUP = SPATIAL_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BBOX_TYPE__EXPRESSION = SPATIAL_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BBOX_TYPE__ANY = SPATIAL_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>BBOX Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BBOX_TYPE_FEATURE_COUNT = SPATIAL_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ComparisonOpsTypeImpl <em>Comparison Ops Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ComparisonOpsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOpsType()
     * @generated
     */
    int COMPARISON_OPS_TYPE = 18;

    /**
     * The number of structural features of the '<em>Comparison Ops Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPARISON_OPS_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.BinaryComparisonOpTypeImpl <em>Binary Comparison Op Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.BinaryComparisonOpTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getBinaryComparisonOpType()
     * @generated
     */
    int BINARY_COMPARISON_OP_TYPE = 12;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP = COMPARISON_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_COMPARISON_OP_TYPE__EXPRESSION = COMPARISON_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Match Action</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_COMPARISON_OP_TYPE__MATCH_ACTION = COMPARISON_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Match Case</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_COMPARISON_OP_TYPE__MATCH_CASE = COMPARISON_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Binary Comparison Op Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_COMPARISON_OP_TYPE_FEATURE_COUNT = COMPARISON_OPS_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.LogicOpsTypeImpl <em>Logic Ops Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.LogicOpsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getLogicOpsType()
     * @generated
     */
    int LOGIC_OPS_TYPE = 33;

    /**
     * The number of structural features of the '<em>Logic Ops Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOGIC_OPS_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.BinaryLogicOpTypeImpl <em>Binary Logic Op Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.BinaryLogicOpTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getBinaryLogicOpType()
     * @generated
     */
    int BINARY_LOGIC_OP_TYPE = 13;

    /**
     * The feature id for the '<em><b>Filter Predicates</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__FILTER_PREDICATES = LOGIC_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Comparison Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__COMPARISON_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Comparison Ops</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__COMPARISON_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Spatial Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__SPATIAL_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Spatial Ops</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__SPATIAL_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Temporal Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__TEMPORAL_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Temporal Ops</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__TEMPORAL_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Logic Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__LOGIC_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Logic Ops</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__LOGIC_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Extension Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__EXTENSION_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Extension Ops</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__EXTENSION_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__FUNCTION = LOGIC_OPS_TYPE_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Id Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__ID_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 12;

    /**
     * The feature id for the '<em><b>Id</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE__ID = LOGIC_OPS_TYPE_FEATURE_COUNT + 13;

    /**
     * The number of structural features of the '<em>Binary Logic Op Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_LOGIC_OP_TYPE_FEATURE_COUNT = LOGIC_OPS_TYPE_FEATURE_COUNT + 14;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.BinarySpatialOpTypeImpl <em>Binary Spatial Op Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.BinarySpatialOpTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getBinarySpatialOpType()
     * @generated
     */
    int BINARY_SPATIAL_OP_TYPE = 14;

    /**
     * The feature id for the '<em><b>Value Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_SPATIAL_OP_TYPE__VALUE_REFERENCE = SPATIAL_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP = SPATIAL_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_SPATIAL_OP_TYPE__EXPRESSION = SPATIAL_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_SPATIAL_OP_TYPE__ANY = SPATIAL_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Binary Spatial Op Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_SPATIAL_OP_TYPE_FEATURE_COUNT = SPATIAL_OPS_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.TemporalOpsTypeImpl <em>Temporal Ops Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.TemporalOpsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOpsType()
     * @generated
     */
    int TEMPORAL_OPS_TYPE = 54;

    /**
     * The number of structural features of the '<em>Temporal Ops Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPS_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.BinaryTemporalOpTypeImpl <em>Binary Temporal Op Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.BinaryTemporalOpTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getBinaryTemporalOpType()
     * @generated
     */
    int BINARY_TEMPORAL_OP_TYPE = 15;

    /**
     * The feature id for the '<em><b>Value Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_TEMPORAL_OP_TYPE__VALUE_REFERENCE = TEMPORAL_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_TEMPORAL_OP_TYPE__EXPRESSION_GROUP = TEMPORAL_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_TEMPORAL_OP_TYPE__EXPRESSION = TEMPORAL_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_TEMPORAL_OP_TYPE__ANY = TEMPORAL_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Binary Temporal Op Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BINARY_TEMPORAL_OP_TYPE_FEATURE_COUNT = TEMPORAL_OPS_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ComparisonOperatorsTypeImpl <em>Comparison Operators Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ComparisonOperatorsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorsType()
     * @generated
     */
    int COMPARISON_OPERATORS_TYPE = 16;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPARISON_OPERATORS_TYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>Comparison Operator</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPARISON_OPERATORS_TYPE__COMPARISON_OPERATOR = 1;

    /**
     * The number of structural features of the '<em>Comparison Operators Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPARISON_OPERATORS_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ComparisonOperatorTypeImpl <em>Comparison Operator Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ComparisonOperatorTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorType()
     * @generated
     */
    int COMPARISON_OPERATOR_TYPE = 17;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPARISON_OPERATOR_TYPE__NAME = 0;

    /**
     * The number of structural features of the '<em>Comparison Operator Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPARISON_OPERATOR_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ConformanceTypeImpl <em>Conformance Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ConformanceTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getConformanceType()
     * @generated
     */
    int CONFORMANCE_TYPE = 19;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFORMANCE_TYPE__CONSTRAINT = 0;

    /**
     * The number of structural features of the '<em>Conformance Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFORMANCE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.DistanceBufferTypeImpl <em>Distance Buffer Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.DistanceBufferTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getDistanceBufferType()
     * @generated
     */
    int DISTANCE_BUFFER_TYPE = 20;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP = SPATIAL_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTANCE_BUFFER_TYPE__EXPRESSION = SPATIAL_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTANCE_BUFFER_TYPE__ANY = SPATIAL_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Distance</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTANCE_BUFFER_TYPE__DISTANCE = SPATIAL_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Distance Buffer Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISTANCE_BUFFER_TYPE_FEATURE_COUNT = SPATIAL_OPS_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.DocumentRootImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getDocumentRoot()
     * @generated
     */
    int DOCUMENT_ROOT = 21;

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
     * The feature id for the '<em><b>Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ID = 3;

    /**
     * The feature id for the '<em><b>Abstract Adhoc Query Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT_ADHOC_QUERY_EXPRESSION = 4;

    /**
     * The feature id for the '<em><b>Abstract Query Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT_QUERY_EXPRESSION = 5;

    /**
     * The feature id for the '<em><b>Abstract Projection Clause</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT_PROJECTION_CLAUSE = 6;

    /**
     * The feature id for the '<em><b>Abstract Selection Clause</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT_SELECTION_CLAUSE = 7;

    /**
     * The feature id for the '<em><b>Abstract Sorting Clause</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT_SORTING_CLAUSE = 8;

    /**
     * The feature id for the '<em><b>After</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__AFTER = 9;

    /**
     * The feature id for the '<em><b>Temporal Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TEMPORAL_OPS = 10;

    /**
     * The feature id for the '<em><b>And</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__AND = 11;

    /**
     * The feature id for the '<em><b>Logic Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LOGIC_OPS = 12;

    /**
     * The feature id for the '<em><b>Any Interacts</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANY_INTERACTS = 13;

    /**
     * The feature id for the '<em><b>BBOX</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__BBOX = 14;

    /**
     * The feature id for the '<em><b>Spatial Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SPATIAL_OPS = 15;

    /**
     * The feature id for the '<em><b>Before</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__BEFORE = 16;

    /**
     * The feature id for the '<em><b>Begins</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__BEGINS = 17;

    /**
     * The feature id for the '<em><b>Begun By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__BEGUN_BY = 18;

    /**
     * The feature id for the '<em><b>Beyond</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__BEYOND = 19;

    /**
     * The feature id for the '<em><b>Comparison Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COMPARISON_OPS = 20;

    /**
     * The feature id for the '<em><b>Contains</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CONTAINS = 21;

    /**
     * The feature id for the '<em><b>Crosses</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CROSSES = 22;

    /**
     * The feature id for the '<em><b>Disjoint</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DISJOINT = 23;

    /**
     * The feature id for the '<em><b>During</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DURING = 24;

    /**
     * The feature id for the '<em><b>DWithin</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DWITHIN = 25;

    /**
     * The feature id for the '<em><b>Ended By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ENDED_BY = 26;

    /**
     * The feature id for the '<em><b>Ends</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ENDS = 27;

    /**
     * The feature id for the '<em><b>Equals</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EQUALS = 28;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXPRESSION = 29;

    /**
     * The feature id for the '<em><b>Extension Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXTENSION_OPS = 30;

    /**
     * The feature id for the '<em><b>Filter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FILTER = 31;

    /**
     * The feature id for the '<em><b>Filter Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FILTER_CAPABILITIES = 32;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FUNCTION = 33;

    /**
     * The feature id for the '<em><b>Intersects</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INTERSECTS = 34;

    /**
     * The feature id for the '<em><b>Literal</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LITERAL = 35;

    /**
     * The feature id for the '<em><b>Logical Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LOGICAL_OPERATORS = 36;

    /**
     * The feature id for the '<em><b>Meets</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MEETS = 37;

    /**
     * The feature id for the '<em><b>Met By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MET_BY = 38;

    /**
     * The feature id for the '<em><b>Not</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NOT = 39;

    /**
     * The feature id for the '<em><b>Or</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OR = 40;

    /**
     * The feature id for the '<em><b>Overlapped By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OVERLAPPED_BY = 41;

    /**
     * The feature id for the '<em><b>Overlaps</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OVERLAPS = 42;

    /**
     * The feature id for the '<em><b>Property Is Between</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_BETWEEN = 43;

    /**
     * The feature id for the '<em><b>Property Is Equal To</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO = 44;

    /**
     * The feature id for the '<em><b>Property Is Greater Than</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN = 45;

    /**
     * The feature id for the '<em><b>Property Is Greater Than Or Equal To</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO = 46;

    /**
     * The feature id for the '<em><b>Property Is Less Than</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN = 47;

    /**
     * The feature id for the '<em><b>Property Is Less Than Or Equal To</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO = 48;

    /**
     * The feature id for the '<em><b>Property Is Like</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_LIKE = 49;

    /**
     * The feature id for the '<em><b>Property Is Nil</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_NIL = 50;

    /**
     * The feature id for the '<em><b>Property Is Not Equal To</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO = 51;

    /**
     * The feature id for the '<em><b>Property Is Null</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROPERTY_IS_NULL = 52;

    /**
     * The feature id for the '<em><b>Resource Id</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RESOURCE_ID = 53;

    /**
     * The feature id for the '<em><b>Sort By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SORT_BY = 54;

    /**
     * The feature id for the '<em><b>TContains</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TCONTAINS = 55;

    /**
     * The feature id for the '<em><b>TEquals</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TEQUALS = 56;

    /**
     * The feature id for the '<em><b>Touches</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TOUCHES = 57;

    /**
     * The feature id for the '<em><b>TOverlaps</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TOVERLAPS = 58;

    /**
     * The feature id for the '<em><b>Value Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__VALUE_REFERENCE = 59;

    /**
     * The feature id for the '<em><b>Within</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__WITHIN = 60;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 61;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ExtendedCapabilitiesTypeImpl <em>Extended Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ExtendedCapabilitiesTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getExtendedCapabilitiesType()
     * @generated
     */
    int EXTENDED_CAPABILITIES_TYPE = 22;

    /**
     * The feature id for the '<em><b>Additional Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS = 0;

    /**
     * The number of structural features of the '<em>Extended Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENDED_CAPABILITIES_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ExtensionOperatorTypeImpl <em>Extension Operator Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ExtensionOperatorTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getExtensionOperatorType()
     * @generated
     */
    int EXTENSION_OPERATOR_TYPE = 23;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_OPERATOR_TYPE__NAME = 0;

    /**
     * The number of structural features of the '<em>Extension Operator Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_OPERATOR_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ExtensionOpsTypeImpl <em>Extension Ops Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ExtensionOpsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getExtensionOpsType()
     * @generated
     */
    int EXTENSION_OPS_TYPE = 24;

    /**
     * The number of structural features of the '<em>Extension Ops Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_OPS_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl <em>Filter Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.FilterCapabilitiesTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getFilterCapabilitiesType()
     * @generated
     */
    int FILTER_CAPABILITIES_TYPE = 25;

    /**
     * The feature id for the '<em><b>Conformance</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_TYPE__CONFORMANCE = 0;

    /**
     * The feature id for the '<em><b>Id Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES = 1;

    /**
     * The feature id for the '<em><b>Scalar Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES = 2;

    /**
     * The feature id for the '<em><b>Spatial Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES = 3;

    /**
     * The feature id for the '<em><b>Temporal Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES = 4;

    /**
     * The feature id for the '<em><b>Functions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_TYPE__FUNCTIONS = 5;

    /**
     * The feature id for the '<em><b>Extended Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES = 6;

    /**
     * The number of structural features of the '<em>Filter Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_CAPABILITIES_TYPE_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.FilterTypeImpl <em>Filter Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.FilterTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getFilterType()
     * @generated
     */
    int FILTER_TYPE = 26;

    /**
     * The feature id for the '<em><b>Comparison Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__COMPARISON_OPS_GROUP = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Comparison Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__COMPARISON_OPS = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Spatial Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__SPATIAL_OPS_GROUP = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Spatial Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__SPATIAL_OPS = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Temporal Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__TEMPORAL_OPS_GROUP = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Temporal Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__TEMPORAL_OPS = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Logic Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__LOGIC_OPS_GROUP = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Logic Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__LOGIC_OPS = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Extension Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__EXTENSION_OPS_GROUP = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Extension Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__EXTENSION_OPS = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__FUNCTION = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Id Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__ID_GROUP = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Id</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE__ID = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 12;

    /**
     * The number of structural features of the '<em>Filter Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FILTER_TYPE_FEATURE_COUNT = ABSTRACT_SELECTION_CLAUSE_TYPE_FEATURE_COUNT + 13;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.FunctionTypeImpl <em>Function Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.FunctionTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getFunctionType()
     * @generated
     */
    int FUNCTION_TYPE = 27;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_TYPE__EXPRESSION_GROUP = 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_TYPE__EXPRESSION = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_TYPE__NAME = 2;

    /**
     * The number of structural features of the '<em>Function Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FUNCTION_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.GeometryOperandsTypeImpl <em>Geometry Operands Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.GeometryOperandsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getGeometryOperandsType()
     * @generated
     */
    int GEOMETRY_OPERANDS_TYPE = 28;

    /**
     * The feature id for the '<em><b>Geometry Operand</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND = 0;

    /**
     * The number of structural features of the '<em>Geometry Operands Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GEOMETRY_OPERANDS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.GeometryOperandTypeImpl <em>Geometry Operand Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.GeometryOperandTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getGeometryOperandType()
     * @generated
     */
    int GEOMETRY_OPERAND_TYPE = 29;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GEOMETRY_OPERAND_TYPE__NAME = 0;

    /**
     * The number of structural features of the '<em>Geometry Operand Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GEOMETRY_OPERAND_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.IdCapabilitiesTypeImpl <em>Id Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.IdCapabilitiesTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getIdCapabilitiesType()
     * @generated
     */
    int ID_CAPABILITIES_TYPE = 30;

    /**
     * The feature id for the '<em><b>Resource Identifier</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER = 0;

    /**
     * The number of structural features of the '<em>Id Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ID_CAPABILITIES_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.LiteralTypeImpl <em>Literal Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.LiteralTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getLiteralType()
     * @generated
     */
    int LITERAL_TYPE = 31;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_TYPE__ANY = 1;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_TYPE__TYPE = 2;

    /**
     * The number of structural features of the '<em>Literal Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LITERAL_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.LogicalOperatorsTypeImpl <em>Logical Operators Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.LogicalOperatorsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getLogicalOperatorsType()
     * @generated
     */
    int LOGICAL_OPERATORS_TYPE = 32;

    /**
     * The number of structural features of the '<em>Logical Operators Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOGICAL_OPERATORS_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.LowerBoundaryTypeImpl <em>Lower Boundary Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.LowerBoundaryTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getLowerBoundaryType()
     * @generated
     */
    int LOWER_BOUNDARY_TYPE = 34;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_BOUNDARY_TYPE__EXPRESSION_GROUP = 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_BOUNDARY_TYPE__EXPRESSION = 1;

    /**
     * The number of structural features of the '<em>Lower Boundary Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LOWER_BOUNDARY_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.MeasureTypeImpl <em>Measure Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.MeasureTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getMeasureType()
     * @generated
     */
    int MEASURE_TYPE = 35;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEASURE_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEASURE_TYPE__UOM = 1;

    /**
     * The number of structural features of the '<em>Measure Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MEASURE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.PropertyIsBetweenTypeImpl <em>Property Is Between Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.PropertyIsBetweenTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getPropertyIsBetweenType()
     * @generated
     */
    int PROPERTY_IS_BETWEEN_TYPE = 36;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_BETWEEN_TYPE__EXPRESSION_GROUP = COMPARISON_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_BETWEEN_TYPE__EXPRESSION = COMPARISON_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Lower Boundary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_BETWEEN_TYPE__LOWER_BOUNDARY = COMPARISON_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Upper Boundary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_BETWEEN_TYPE__UPPER_BOUNDARY = COMPARISON_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Property Is Between Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_BETWEEN_TYPE_FEATURE_COUNT = COMPARISON_OPS_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.PropertyIsLikeTypeImpl <em>Property Is Like Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.PropertyIsLikeTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getPropertyIsLikeType()
     * @generated
     */
    int PROPERTY_IS_LIKE_TYPE = 37;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP = COMPARISON_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_LIKE_TYPE__EXPRESSION = COMPARISON_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Escape Char</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_LIKE_TYPE__ESCAPE_CHAR = COMPARISON_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Single Char</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_LIKE_TYPE__SINGLE_CHAR = COMPARISON_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Wild Card</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_LIKE_TYPE__WILD_CARD = COMPARISON_OPS_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Property Is Like Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_LIKE_TYPE_FEATURE_COUNT = COMPARISON_OPS_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.PropertyIsNilTypeImpl <em>Property Is Nil Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.PropertyIsNilTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getPropertyIsNilType()
     * @generated
     */
    int PROPERTY_IS_NIL_TYPE = 38;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_NIL_TYPE__EXPRESSION_GROUP = COMPARISON_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_NIL_TYPE__EXPRESSION = COMPARISON_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Nil Reason</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_NIL_TYPE__NIL_REASON = COMPARISON_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Property Is Nil Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_NIL_TYPE_FEATURE_COUNT = COMPARISON_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.PropertyIsNullTypeImpl <em>Property Is Null Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.PropertyIsNullTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getPropertyIsNullType()
     * @generated
     */
    int PROPERTY_IS_NULL_TYPE = 39;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_NULL_TYPE__EXPRESSION_GROUP = COMPARISON_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_NULL_TYPE__EXPRESSION = COMPARISON_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Property Is Null Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_IS_NULL_TYPE_FEATURE_COUNT = COMPARISON_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ResourceIdentifierTypeImpl <em>Resource Identifier Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ResourceIdentifierTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getResourceIdentifierType()
     * @generated
     */
    int RESOURCE_IDENTIFIER_TYPE = 40;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_IDENTIFIER_TYPE__METADATA = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_IDENTIFIER_TYPE__NAME = 1;

    /**
     * The number of structural features of the '<em>Resource Identifier Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_IDENTIFIER_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ResourceIdTypeImpl <em>Resource Id Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ResourceIdTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getResourceIdType()
     * @generated
     */
    int RESOURCE_ID_TYPE = 41;

    /**
     * The feature id for the '<em><b>End Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ID_TYPE__END_DATE = ABSTRACT_ID_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Previous Rid</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ID_TYPE__PREVIOUS_RID = ABSTRACT_ID_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Rid</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ID_TYPE__RID = ABSTRACT_ID_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Start Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ID_TYPE__START_DATE = ABSTRACT_ID_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ID_TYPE__VERSION = ABSTRACT_ID_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Resource Id Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_ID_TYPE_FEATURE_COUNT = ABSTRACT_ID_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.ScalarCapabilitiesTypeImpl <em>Scalar Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.ScalarCapabilitiesTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getScalarCapabilitiesType()
     * @generated
     */
    int SCALAR_CAPABILITIES_TYPE = 42;

    /**
     * The feature id for the '<em><b>Logical Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS = 0;

    /**
     * The feature id for the '<em><b>Comparison Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS = 1;

    /**
     * The number of structural features of the '<em>Scalar Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SCALAR_CAPABILITIES_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.SortByTypeImpl <em>Sort By Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.SortByTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSortByType()
     * @generated
     */
    int SORT_BY_TYPE = 43;

    /**
     * The feature id for the '<em><b>Sort Property</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SORT_BY_TYPE__SORT_PROPERTY = 0;

    /**
     * The number of structural features of the '<em>Sort By Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SORT_BY_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.SortPropertyTypeImpl <em>Sort Property Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.SortPropertyTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSortPropertyType()
     * @generated
     */
    int SORT_PROPERTY_TYPE = 44;

    /**
     * The feature id for the '<em><b>Value Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SORT_PROPERTY_TYPE__VALUE_REFERENCE = 0;

    /**
     * The feature id for the '<em><b>Sort Order</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SORT_PROPERTY_TYPE__SORT_ORDER = 1;

    /**
     * The number of structural features of the '<em>Sort Property Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SORT_PROPERTY_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.SpatialCapabilitiesTypeImpl <em>Spatial Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.SpatialCapabilitiesTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialCapabilitiesType()
     * @generated
     */
    int SPATIAL_CAPABILITIES_TYPE = 45;

    /**
     * The feature id for the '<em><b>Geometry Operands</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS = 0;

    /**
     * The feature id for the '<em><b>Spatial Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS = 1;

    /**
     * The number of structural features of the '<em>Spatial Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_CAPABILITIES_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.SpatialOperatorsTypeImpl <em>Spatial Operators Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.SpatialOperatorsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorsType()
     * @generated
     */
    int SPATIAL_OPERATORS_TYPE = 46;

    /**
     * The feature id for the '<em><b>Spatial Operator</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR = 0;

    /**
     * The number of structural features of the '<em>Spatial Operators Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_OPERATORS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.SpatialOperatorTypeImpl <em>Spatial Operator Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.SpatialOperatorTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorType()
     * @generated
     */
    int SPATIAL_OPERATOR_TYPE = 47;

    /**
     * The feature id for the '<em><b>Geometry Operands</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_OPERATOR_TYPE__NAME = 1;

    /**
     * The number of structural features of the '<em>Spatial Operator Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_OPERATOR_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.TemporalCapabilitiesTypeImpl <em>Temporal Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.TemporalCapabilitiesTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalCapabilitiesType()
     * @generated
     */
    int TEMPORAL_CAPABILITIES_TYPE = 49;

    /**
     * The feature id for the '<em><b>Temporal Operands</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_CAPABILITIES_TYPE__TEMPORAL_OPERANDS = 0;

    /**
     * The feature id for the '<em><b>Temporal Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_CAPABILITIES_TYPE__TEMPORAL_OPERATORS = 1;

    /**
     * The number of structural features of the '<em>Temporal Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_CAPABILITIES_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.TemporalOperandsTypeImpl <em>Temporal Operands Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.TemporalOperandsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperandsType()
     * @generated
     */
    int TEMPORAL_OPERANDS_TYPE = 50;

    /**
     * The feature id for the '<em><b>Temporal Operand</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERANDS_TYPE__TEMPORAL_OPERAND = 0;

    /**
     * The number of structural features of the '<em>Temporal Operands Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERANDS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.TemporalOperandTypeImpl <em>Temporal Operand Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.TemporalOperandTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperandType()
     * @generated
     */
    int TEMPORAL_OPERAND_TYPE = 51;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERAND_TYPE__NAME = 0;

    /**
     * The number of structural features of the '<em>Temporal Operand Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERAND_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.TemporalOperatorsTypeImpl <em>Temporal Operators Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.TemporalOperatorsTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorsType()
     * @generated
     */
    int TEMPORAL_OPERATORS_TYPE = 52;

    /**
     * The feature id for the '<em><b>Temporal Operator</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR = 0;

    /**
     * The number of structural features of the '<em>Temporal Operators Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERATORS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.TemporalOperatorTypeImpl <em>Temporal Operator Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.TemporalOperatorTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorType()
     * @generated
     */
    int TEMPORAL_OPERATOR_TYPE = 53;

    /**
     * The feature id for the '<em><b>Temporal Operands</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERATOR_TYPE__TEMPORAL_OPERANDS = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERATOR_TYPE__NAME = 1;

    /**
     * The number of structural features of the '<em>Temporal Operator Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEMPORAL_OPERATOR_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.UnaryLogicOpTypeImpl <em>Unary Logic Op Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.UnaryLogicOpTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getUnaryLogicOpType()
     * @generated
     */
    int UNARY_LOGIC_OP_TYPE = 55;

    /**
     * The feature id for the '<em><b>Comparison Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__COMPARISON_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Comparison Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__COMPARISON_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Spatial Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__SPATIAL_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Spatial Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__SPATIAL_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Temporal Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__TEMPORAL_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Temporal Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__TEMPORAL_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Logic Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__LOGIC_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Logic Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__LOGIC_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Extension Ops Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__EXTENSION_OPS_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Extension Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__EXTENSION_OPS = LOGIC_OPS_TYPE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__FUNCTION = LOGIC_OPS_TYPE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Id Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__ID_GROUP = LOGIC_OPS_TYPE_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Id</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE__ID = LOGIC_OPS_TYPE_FEATURE_COUNT + 12;

    /**
     * The number of structural features of the '<em>Unary Logic Op Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNARY_LOGIC_OP_TYPE_FEATURE_COUNT = LOGIC_OPS_TYPE_FEATURE_COUNT + 13;

    /**
     * The meta object id for the '{@link net.opengis.fes20.impl.UpperBoundaryTypeImpl <em>Upper Boundary Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.impl.UpperBoundaryTypeImpl
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getUpperBoundaryType()
     * @generated
     */
    int UPPER_BOUNDARY_TYPE = 56;

    /**
     * The feature id for the '<em><b>Expression Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_BOUNDARY_TYPE__EXPRESSION_GROUP = 0;

    /**
     * The feature id for the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_BOUNDARY_TYPE__EXPRESSION = 1;

    /**
     * The number of structural features of the '<em>Upper Boundary Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UPPER_BOUNDARY_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.fes20.ComparisonOperatorNameTypeMember0 <em>Comparison Operator Name Type Member0</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.ComparisonOperatorNameTypeMember0
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorNameTypeMember0()
     * @generated
     */
    int COMPARISON_OPERATOR_NAME_TYPE_MEMBER0 = 57;

    /**
     * The meta object id for the '{@link net.opengis.fes20.MatchActionType <em>Match Action Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.MatchActionType
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getMatchActionType()
     * @generated
     */
    int MATCH_ACTION_TYPE = 58;

    /**
     * The meta object id for the '{@link net.opengis.fes20.SortOrderType <em>Sort Order Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.SortOrderType
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSortOrderType()
     * @generated
     */
    int SORT_ORDER_TYPE = 59;

    /**
     * The meta object id for the '{@link net.opengis.fes20.SpatialOperatorNameTypeMember0 <em>Spatial Operator Name Type Member0</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.SpatialOperatorNameTypeMember0
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorNameTypeMember0()
     * @generated
     */
    int SPATIAL_OPERATOR_NAME_TYPE_MEMBER0 = 60;

    /**
     * The meta object id for the '{@link net.opengis.fes20.TemporalOperatorNameTypeMember0 <em>Temporal Operator Name Type Member0</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.TemporalOperatorNameTypeMember0
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorNameTypeMember0()
     * @generated
     */
    int TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0 = 61;

    /**
     * The meta object id for the '{@link net.opengis.fes20.VersionActionTokens <em>Version Action Tokens</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.VersionActionTokens
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getVersionActionTokens()
     * @generated
     */
    int VERSION_ACTION_TOKENS = 62;

    /**
     * The meta object id for the '<em>Aliases Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getAliasesType()
     * @generated
     */
    int ALIASES_TYPE = 63;

    /**
     * The meta object id for the '<em>Comparison Operator Name Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorNameType()
     * @generated
     */
    int COMPARISON_OPERATOR_NAME_TYPE = 64;

    /**
     * The meta object id for the '<em>Comparison Operator Name Type Member0 Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.ComparisonOperatorNameTypeMember0
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorNameTypeMember0Object()
     * @generated
     */
    int COMPARISON_OPERATOR_NAME_TYPE_MEMBER0_OBJECT = 65;

    /**
     * The meta object id for the '<em>Comparison Operator Name Type Member1</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorNameTypeMember1()
     * @generated
     */
    int COMPARISON_OPERATOR_NAME_TYPE_MEMBER1 = 66;

    /**
     * The meta object id for the '<em>Match Action Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.MatchActionType
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getMatchActionTypeObject()
     * @generated
     */
    int MATCH_ACTION_TYPE_OBJECT = 67;

    /**
     * The meta object id for the '<em>Schema Element</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSchemaElement()
     * @generated
     */
    int SCHEMA_ELEMENT = 68;

    /**
     * The meta object id for the '<em>Sort Order Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.SortOrderType
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSortOrderTypeObject()
     * @generated
     */
    int SORT_ORDER_TYPE_OBJECT = 69;

    /**
     * The meta object id for the '<em>Spatial Operator Name Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorNameType()
     * @generated
     */
    int SPATIAL_OPERATOR_NAME_TYPE = 70;

    /**
     * The meta object id for the '<em>Spatial Operator Name Type Member0 Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.SpatialOperatorNameTypeMember0
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorNameTypeMember0Object()
     * @generated
     */
    int SPATIAL_OPERATOR_NAME_TYPE_MEMBER0_OBJECT = 71;

    /**
     * The meta object id for the '<em>Spatial Operator Name Type Member1</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorNameTypeMember1()
     * @generated
     */
    int SPATIAL_OPERATOR_NAME_TYPE_MEMBER1 = 72;

    /**
     * The meta object id for the '<em>Temporal Operator Name Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorNameType()
     * @generated
     */
    int TEMPORAL_OPERATOR_NAME_TYPE = 73;

    /**
     * The meta object id for the '<em>Temporal Operator Name Type Member0 Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.TemporalOperatorNameTypeMember0
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorNameTypeMember0Object()
     * @generated
     */
    int TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0_OBJECT = 74;

    /**
     * The meta object id for the '<em>Temporal Operator Name Type Member1</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorNameTypeMember1()
     * @generated
     */
    int TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1 = 75;

    /**
     * The meta object id for the '<em>Type Names List Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTypeNamesListType()
     * @generated
     */
    int TYPE_NAMES_LIST_TYPE = 76;

    /**
     * The meta object id for the '<em>Type Names Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getTypeNamesType()
     * @generated
     */
    int TYPE_NAMES_TYPE = 77;

    /**
     * The meta object id for the '<em>Uom Identifier</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getUomIdentifier()
     * @generated
     */
    int UOM_IDENTIFIER = 78;

    /**
     * The meta object id for the '<em>Uom Symbol</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getUomSymbol()
     * @generated
     */
    int UOM_SYMBOL = 79;

    /**
     * The meta object id for the '<em>Uom URI</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getUomURI()
     * @generated
     */
    int UOM_URI = 80;

    /**
     * The meta object id for the '<em>Version Action Tokens Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.fes20.VersionActionTokens
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getVersionActionTokensObject()
     * @generated
     */
    int VERSION_ACTION_TOKENS_OBJECT = 81;

    /**
     * The meta object id for the '<em>Version Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.fes20.impl.Fes20PackageImpl#getVersionType()
     * @generated
     */
    int VERSION_TYPE = 82;


    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType <em>Abstract Adhoc Query Expression Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Adhoc Query Expression Type</em>'.
     * @see net.opengis.fes20.AbstractAdhocQueryExpressionType
     * @generated
     */
    EClass getAbstractAdhocQueryExpressionType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractProjectionClause <em>Abstract Projection Clause</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Abstract Projection Clause</em>'.
     * @see net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractProjectionClause()
     * @see #getAbstractAdhocQueryExpressionType()
     * @generated
     */
    EAttribute getAbstractAdhocQueryExpressionType_AbstractProjectionClause();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractSelectionClause <em>Abstract Selection Clause</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Abstract Selection Clause</em>'.
     * @see net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractSelectionClause()
     * @see #getAbstractAdhocQueryExpressionType()
     * @generated
     */
    EAttribute getAbstractAdhocQueryExpressionType_AbstractSelectionClause();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractSortingClause <em>Abstract Sorting Clause</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Abstract Sorting Clause</em>'.
     * @see net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractSortingClause()
     * @see #getAbstractAdhocQueryExpressionType()
     * @generated
     */
    EAttribute getAbstractAdhocQueryExpressionType_AbstractSortingClause();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAliases <em>Aliases</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Aliases</em>'.
     * @see net.opengis.fes20.AbstractAdhocQueryExpressionType#getAliases()
     * @see #getAbstractAdhocQueryExpressionType()
     * @generated
     */
    EAttribute getAbstractAdhocQueryExpressionType_Aliases();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getTypeNames <em>Type Names</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Type Names</em>'.
     * @see net.opengis.fes20.AbstractAdhocQueryExpressionType#getTypeNames()
     * @see #getAbstractAdhocQueryExpressionType()
     * @generated
     */
    EAttribute getAbstractAdhocQueryExpressionType_TypeNames();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AbstractIdType <em>Abstract Id Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Id Type</em>'.
     * @see net.opengis.fes20.AbstractIdType
     * @generated
     */
    EClass getAbstractIdType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AbstractProjectionClauseType <em>Abstract Projection Clause Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Projection Clause Type</em>'.
     * @see net.opengis.fes20.AbstractProjectionClauseType
     * @generated
     */
    EClass getAbstractProjectionClauseType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AbstractQueryExpressionType <em>Abstract Query Expression Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Query Expression Type</em>'.
     * @see net.opengis.fes20.AbstractQueryExpressionType
     * @generated
     */
    EClass getAbstractQueryExpressionType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.AbstractQueryExpressionType#getHandle <em>Handle</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Handle</em>'.
     * @see net.opengis.fes20.AbstractQueryExpressionType#getHandle()
     * @see #getAbstractQueryExpressionType()
     * @generated
     */
    EAttribute getAbstractQueryExpressionType_Handle();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AbstractSelectionClauseType <em>Abstract Selection Clause Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Selection Clause Type</em>'.
     * @see net.opengis.fes20.AbstractSelectionClauseType
     * @generated
     */
    EClass getAbstractSelectionClauseType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AbstractSortingClauseType <em>Abstract Sorting Clause Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Sorting Clause Type</em>'.
     * @see net.opengis.fes20.AbstractSortingClauseType
     * @generated
     */
    EClass getAbstractSortingClauseType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AdditionalOperatorsType <em>Additional Operators Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Additional Operators Type</em>'.
     * @see net.opengis.fes20.AdditionalOperatorsType
     * @generated
     */
    EClass getAdditionalOperatorsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.AdditionalOperatorsType#getOperator <em>Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Operator</em>'.
     * @see net.opengis.fes20.AdditionalOperatorsType#getOperator()
     * @see #getAdditionalOperatorsType()
     * @generated
     */
    EReference getAdditionalOperatorsType_Operator();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ArgumentsType <em>Arguments Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Arguments Type</em>'.
     * @see net.opengis.fes20.ArgumentsType
     * @generated
     */
    EClass getArgumentsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.ArgumentsType#getArgument <em>Argument</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Argument</em>'.
     * @see net.opengis.fes20.ArgumentsType#getArgument()
     * @see #getArgumentsType()
     * @generated
     */
    EReference getArgumentsType_Argument();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ArgumentType <em>Argument Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Argument Type</em>'.
     * @see net.opengis.fes20.ArgumentType
     * @generated
     */
    EClass getArgumentType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.ArgumentType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Metadata</em>'.
     * @see net.opengis.fes20.ArgumentType#getMetadata()
     * @see #getArgumentType()
     * @generated
     */
    EReference getArgumentType_Metadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ArgumentType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see net.opengis.fes20.ArgumentType#getType()
     * @see #getArgumentType()
     * @generated
     */
    EAttribute getArgumentType_Type();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ArgumentType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.ArgumentType#getName()
     * @see #getArgumentType()
     * @generated
     */
    EAttribute getArgumentType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AvailableFunctionsType <em>Available Functions Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Available Functions Type</em>'.
     * @see net.opengis.fes20.AvailableFunctionsType
     * @generated
     */
    EClass getAvailableFunctionsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.AvailableFunctionsType#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Function</em>'.
     * @see net.opengis.fes20.AvailableFunctionsType#getFunction()
     * @see #getAvailableFunctionsType()
     * @generated
     */
    EReference getAvailableFunctionsType_Function();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.AvailableFunctionType <em>Available Function Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Available Function Type</em>'.
     * @see net.opengis.fes20.AvailableFunctionType
     * @generated
     */
    EClass getAvailableFunctionType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.AvailableFunctionType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Metadata</em>'.
     * @see net.opengis.fes20.AvailableFunctionType#getMetadata()
     * @see #getAvailableFunctionType()
     * @generated
     */
    EReference getAvailableFunctionType_Metadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.AvailableFunctionType#getReturns <em>Returns</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Returns</em>'.
     * @see net.opengis.fes20.AvailableFunctionType#getReturns()
     * @see #getAvailableFunctionType()
     * @generated
     */
    EAttribute getAvailableFunctionType_Returns();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.AvailableFunctionType#getArguments <em>Arguments</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Arguments</em>'.
     * @see net.opengis.fes20.AvailableFunctionType#getArguments()
     * @see #getAvailableFunctionType()
     * @generated
     */
    EReference getAvailableFunctionType_Arguments();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.AvailableFunctionType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.AvailableFunctionType#getName()
     * @see #getAvailableFunctionType()
     * @generated
     */
    EAttribute getAvailableFunctionType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.BBOXType <em>BBOX Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>BBOX Type</em>'.
     * @see net.opengis.fes20.BBOXType
     * @generated
     */
    EClass getBBOXType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BBOXType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.BBOXType#getExpressionGroup()
     * @see #getBBOXType()
     * @generated
     */
    EAttribute getBBOXType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.BBOXType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.BBOXType#getExpression()
     * @see #getBBOXType()
     * @generated
     */
    EReference getBBOXType_Expression();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BBOXType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.fes20.BBOXType#getAny()
     * @see #getBBOXType()
     * @generated
     */
    EAttribute getBBOXType_Any();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.BinaryComparisonOpType <em>Binary Comparison Op Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Binary Comparison Op Type</em>'.
     * @see net.opengis.fes20.BinaryComparisonOpType
     * @generated
     */
    EClass getBinaryComparisonOpType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryComparisonOpType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.BinaryComparisonOpType#getExpressionGroup()
     * @see #getBinaryComparisonOpType()
     * @generated
     */
    EAttribute getBinaryComparisonOpType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.BinaryComparisonOpType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Expression</em>'.
     * @see net.opengis.fes20.BinaryComparisonOpType#getExpression()
     * @see #getBinaryComparisonOpType()
     * @generated
     */
    EReference getBinaryComparisonOpType_Expression();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.BinaryComparisonOpType#getMatchAction <em>Match Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Match Action</em>'.
     * @see net.opengis.fes20.BinaryComparisonOpType#getMatchAction()
     * @see #getBinaryComparisonOpType()
     * @generated
     */
    EAttribute getBinaryComparisonOpType_MatchAction();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.BinaryComparisonOpType#isMatchCase <em>Match Case</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Match Case</em>'.
     * @see net.opengis.fes20.BinaryComparisonOpType#isMatchCase()
     * @see #getBinaryComparisonOpType()
     * @generated
     */
    EAttribute getBinaryComparisonOpType_MatchCase();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.BinaryLogicOpType <em>Binary Logic Op Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Binary Logic Op Type</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType
     * @generated
     */
    EClass getBinaryLogicOpType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryLogicOpType#getFilterPredicates <em>Filter Predicates</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Filter Predicates</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getFilterPredicates()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EAttribute getBinaryLogicOpType_FilterPredicates();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryLogicOpType#getComparisonOpsGroup <em>Comparison Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Comparison Ops Group</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getComparisonOpsGroup()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EAttribute getBinaryLogicOpType_ComparisonOpsGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.BinaryLogicOpType#getComparisonOps <em>Comparison Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Comparison Ops</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getComparisonOps()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EReference getBinaryLogicOpType_ComparisonOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryLogicOpType#getSpatialOpsGroup <em>Spatial Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Spatial Ops Group</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getSpatialOpsGroup()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EAttribute getBinaryLogicOpType_SpatialOpsGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.BinaryLogicOpType#getSpatialOps <em>Spatial Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Spatial Ops</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getSpatialOps()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EReference getBinaryLogicOpType_SpatialOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryLogicOpType#getTemporalOpsGroup <em>Temporal Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Temporal Ops Group</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getTemporalOpsGroup()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EAttribute getBinaryLogicOpType_TemporalOpsGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.BinaryLogicOpType#getTemporalOps <em>Temporal Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Temporal Ops</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getTemporalOps()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EReference getBinaryLogicOpType_TemporalOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryLogicOpType#getLogicOpsGroup <em>Logic Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Logic Ops Group</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getLogicOpsGroup()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EAttribute getBinaryLogicOpType_LogicOpsGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.BinaryLogicOpType#getLogicOps <em>Logic Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Logic Ops</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getLogicOps()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EReference getBinaryLogicOpType_LogicOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryLogicOpType#getExtensionOpsGroup <em>Extension Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Extension Ops Group</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getExtensionOpsGroup()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EAttribute getBinaryLogicOpType_ExtensionOpsGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.BinaryLogicOpType#getExtensionOps <em>Extension Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Extension Ops</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getExtensionOps()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EReference getBinaryLogicOpType_ExtensionOps();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.BinaryLogicOpType#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Function</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getFunction()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EReference getBinaryLogicOpType_Function();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryLogicOpType#getIdGroup <em>Id Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Id Group</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getIdGroup()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EAttribute getBinaryLogicOpType_IdGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.BinaryLogicOpType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Id</em>'.
     * @see net.opengis.fes20.BinaryLogicOpType#getId()
     * @see #getBinaryLogicOpType()
     * @generated
     */
    EReference getBinaryLogicOpType_Id();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.BinarySpatialOpType <em>Binary Spatial Op Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Binary Spatial Op Type</em>'.
     * @see net.opengis.fes20.BinarySpatialOpType
     * @generated
     */
    EClass getBinarySpatialOpType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.BinarySpatialOpType#getValueReference <em>Value Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value Reference</em>'.
     * @see net.opengis.fes20.BinarySpatialOpType#getValueReference()
     * @see #getBinarySpatialOpType()
     * @generated
     */
    EAttribute getBinarySpatialOpType_ValueReference();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinarySpatialOpType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.BinarySpatialOpType#getExpressionGroup()
     * @see #getBinarySpatialOpType()
     * @generated
     */
    EAttribute getBinarySpatialOpType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.BinarySpatialOpType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.BinarySpatialOpType#getExpression()
     * @see #getBinarySpatialOpType()
     * @generated
     */
    EReference getBinarySpatialOpType_Expression();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinarySpatialOpType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.fes20.BinarySpatialOpType#getAny()
     * @see #getBinarySpatialOpType()
     * @generated
     */
    EAttribute getBinarySpatialOpType_Any();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.BinaryTemporalOpType <em>Binary Temporal Op Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Binary Temporal Op Type</em>'.
     * @see net.opengis.fes20.BinaryTemporalOpType
     * @generated
     */
    EClass getBinaryTemporalOpType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.BinaryTemporalOpType#getValueReference <em>Value Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value Reference</em>'.
     * @see net.opengis.fes20.BinaryTemporalOpType#getValueReference()
     * @see #getBinaryTemporalOpType()
     * @generated
     */
    EAttribute getBinaryTemporalOpType_ValueReference();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryTemporalOpType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.BinaryTemporalOpType#getExpressionGroup()
     * @see #getBinaryTemporalOpType()
     * @generated
     */
    EAttribute getBinaryTemporalOpType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.BinaryTemporalOpType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.BinaryTemporalOpType#getExpression()
     * @see #getBinaryTemporalOpType()
     * @generated
     */
    EReference getBinaryTemporalOpType_Expression();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.BinaryTemporalOpType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.fes20.BinaryTemporalOpType#getAny()
     * @see #getBinaryTemporalOpType()
     * @generated
     */
    EAttribute getBinaryTemporalOpType_Any();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ComparisonOperatorsType <em>Comparison Operators Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Comparison Operators Type</em>'.
     * @see net.opengis.fes20.ComparisonOperatorsType
     * @generated
     */
    EClass getComparisonOperatorsType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.ComparisonOperatorsType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see net.opengis.fes20.ComparisonOperatorsType#getGroup()
     * @see #getComparisonOperatorsType()
     * @generated
     */
    EAttribute getComparisonOperatorsType_Group();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.ComparisonOperatorsType#getComparisonOperator <em>Comparison Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Comparison Operator</em>'.
     * @see net.opengis.fes20.ComparisonOperatorsType#getComparisonOperator()
     * @see #getComparisonOperatorsType()
     * @generated
     */
    EReference getComparisonOperatorsType_ComparisonOperator();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ComparisonOperatorType <em>Comparison Operator Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Comparison Operator Type</em>'.
     * @see net.opengis.fes20.ComparisonOperatorType
     * @generated
     */
    EClass getComparisonOperatorType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ComparisonOperatorType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.ComparisonOperatorType#getName()
     * @see #getComparisonOperatorType()
     * @generated
     */
    EAttribute getComparisonOperatorType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ComparisonOpsType <em>Comparison Ops Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Comparison Ops Type</em>'.
     * @see net.opengis.fes20.ComparisonOpsType
     * @generated
     */
    EClass getComparisonOpsType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ConformanceType <em>Conformance Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Conformance Type</em>'.
     * @see net.opengis.fes20.ConformanceType
     * @generated
     */
    EClass getConformanceType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.ConformanceType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.fes20.ConformanceType#getConstraint()
     * @see #getConformanceType()
     * @generated
     */
    EReference getConformanceType_Constraint();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.DistanceBufferType <em>Distance Buffer Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Distance Buffer Type</em>'.
     * @see net.opengis.fes20.DistanceBufferType
     * @generated
     */
    EClass getDistanceBufferType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.DistanceBufferType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.DistanceBufferType#getExpressionGroup()
     * @see #getDistanceBufferType()
     * @generated
     */
    EAttribute getDistanceBufferType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DistanceBufferType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.DistanceBufferType#getExpression()
     * @see #getDistanceBufferType()
     * @generated
     */
    EReference getDistanceBufferType_Expression();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.DistanceBufferType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.fes20.DistanceBufferType#getAny()
     * @see #getDistanceBufferType()
     * @generated
     */
    EAttribute getDistanceBufferType_Any();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DistanceBufferType#getDistance <em>Distance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Distance</em>'.
     * @see net.opengis.fes20.DistanceBufferType#getDistance()
     * @see #getDistanceBufferType()
     * @generated
     */
    EReference getDistanceBufferType_Distance();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see net.opengis.fes20.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.fes20.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map '{@link net.opengis.fes20.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see net.opengis.fes20.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link net.opengis.fes20.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see net.opengis.fes20.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Id</em>'.
     * @see net.opengis.fes20.DocumentRoot#getId()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Id();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getAbstractAdhocQueryExpression <em>Abstract Adhoc Query Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Adhoc Query Expression</em>'.
     * @see net.opengis.fes20.DocumentRoot#getAbstractAdhocQueryExpression()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AbstractAdhocQueryExpression();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getAbstractQueryExpression <em>Abstract Query Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Query Expression</em>'.
     * @see net.opengis.fes20.DocumentRoot#getAbstractQueryExpression()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AbstractQueryExpression();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getAbstractProjectionClause <em>Abstract Projection Clause</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Projection Clause</em>'.
     * @see net.opengis.fes20.DocumentRoot#getAbstractProjectionClause()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AbstractProjectionClause();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getAbstractSelectionClause <em>Abstract Selection Clause</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Selection Clause</em>'.
     * @see net.opengis.fes20.DocumentRoot#getAbstractSelectionClause()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AbstractSelectionClause();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getAbstractSortingClause <em>Abstract Sorting Clause</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Sorting Clause</em>'.
     * @see net.opengis.fes20.DocumentRoot#getAbstractSortingClause()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AbstractSortingClause();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getAfter <em>After</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>After</em>'.
     * @see net.opengis.fes20.DocumentRoot#getAfter()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_After();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getTemporalOps <em>Temporal Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Ops</em>'.
     * @see net.opengis.fes20.DocumentRoot#getTemporalOps()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TemporalOps();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getAnd <em>And</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>And</em>'.
     * @see net.opengis.fes20.DocumentRoot#getAnd()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_And();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getLogicOps <em>Logic Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Logic Ops</em>'.
     * @see net.opengis.fes20.DocumentRoot#getLogicOps()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_LogicOps();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getAnyInteracts <em>Any Interacts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Any Interacts</em>'.
     * @see net.opengis.fes20.DocumentRoot#getAnyInteracts()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AnyInteracts();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getBBOX <em>BBOX</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>BBOX</em>'.
     * @see net.opengis.fes20.DocumentRoot#getBBOX()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_BBOX();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getSpatialOps <em>Spatial Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spatial Ops</em>'.
     * @see net.opengis.fes20.DocumentRoot#getSpatialOps()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_SpatialOps();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getBefore <em>Before</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Before</em>'.
     * @see net.opengis.fes20.DocumentRoot#getBefore()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Before();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getBegins <em>Begins</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Begins</em>'.
     * @see net.opengis.fes20.DocumentRoot#getBegins()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Begins();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getBegunBy <em>Begun By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Begun By</em>'.
     * @see net.opengis.fes20.DocumentRoot#getBegunBy()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_BegunBy();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getBeyond <em>Beyond</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Beyond</em>'.
     * @see net.opengis.fes20.DocumentRoot#getBeyond()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Beyond();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getComparisonOps <em>Comparison Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Comparison Ops</em>'.
     * @see net.opengis.fes20.DocumentRoot#getComparisonOps()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ComparisonOps();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getContains <em>Contains</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contains</em>'.
     * @see net.opengis.fes20.DocumentRoot#getContains()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Contains();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getCrosses <em>Crosses</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Crosses</em>'.
     * @see net.opengis.fes20.DocumentRoot#getCrosses()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Crosses();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getDisjoint <em>Disjoint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Disjoint</em>'.
     * @see net.opengis.fes20.DocumentRoot#getDisjoint()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Disjoint();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getDuring <em>During</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>During</em>'.
     * @see net.opengis.fes20.DocumentRoot#getDuring()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_During();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getDWithin <em>DWithin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>DWithin</em>'.
     * @see net.opengis.fes20.DocumentRoot#getDWithin()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DWithin();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getEndedBy <em>Ended By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Ended By</em>'.
     * @see net.opengis.fes20.DocumentRoot#getEndedBy()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndedBy();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getEnds <em>Ends</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Ends</em>'.
     * @see net.opengis.fes20.DocumentRoot#getEnds()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Ends();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getEquals <em>Equals</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Equals</em>'.
     * @see net.opengis.fes20.DocumentRoot#getEquals()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Equals();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.DocumentRoot#getExpression()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Expression();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getExtensionOps <em>Extension Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extension Ops</em>'.
     * @see net.opengis.fes20.DocumentRoot#getExtensionOps()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ExtensionOps();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getFilter <em>Filter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Filter</em>'.
     * @see net.opengis.fes20.DocumentRoot#getFilter()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Filter();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getFilterCapabilities <em>Filter Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Filter Capabilities</em>'.
     * @see net.opengis.fes20.DocumentRoot#getFilterCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_FilterCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Function</em>'.
     * @see net.opengis.fes20.DocumentRoot#getFunction()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Function();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getIntersects <em>Intersects</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Intersects</em>'.
     * @see net.opengis.fes20.DocumentRoot#getIntersects()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Intersects();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getLiteral <em>Literal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Literal</em>'.
     * @see net.opengis.fes20.DocumentRoot#getLiteral()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Literal();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getLogicalOperators <em>Logical Operators</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Logical Operators</em>'.
     * @see net.opengis.fes20.DocumentRoot#getLogicalOperators()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_LogicalOperators();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getMeets <em>Meets</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Meets</em>'.
     * @see net.opengis.fes20.DocumentRoot#getMeets()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Meets();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getMetBy <em>Met By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Met By</em>'.
     * @see net.opengis.fes20.DocumentRoot#getMetBy()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_MetBy();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getNot <em>Not</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Not</em>'.
     * @see net.opengis.fes20.DocumentRoot#getNot()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Not();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getOr <em>Or</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Or</em>'.
     * @see net.opengis.fes20.DocumentRoot#getOr()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Or();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getOverlappedBy <em>Overlapped By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Overlapped By</em>'.
     * @see net.opengis.fes20.DocumentRoot#getOverlappedBy()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OverlappedBy();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getOverlaps <em>Overlaps</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Overlaps</em>'.
     * @see net.opengis.fes20.DocumentRoot#getOverlaps()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Overlaps();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsBetween <em>Property Is Between</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Between</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsBetween()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsBetween();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsEqualTo <em>Property Is Equal To</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Equal To</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsEqualTo()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsEqualTo();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsGreaterThan <em>Property Is Greater Than</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Greater Than</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsGreaterThan()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsGreaterThan();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsGreaterThanOrEqualTo <em>Property Is Greater Than Or Equal To</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Greater Than Or Equal To</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsGreaterThanOrEqualTo()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsGreaterThanOrEqualTo();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsLessThan <em>Property Is Less Than</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Less Than</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsLessThan()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsLessThan();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsLessThanOrEqualTo <em>Property Is Less Than Or Equal To</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Less Than Or Equal To</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsLessThanOrEqualTo()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsLessThanOrEqualTo();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsLike <em>Property Is Like</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Like</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsLike()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsLike();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsNil <em>Property Is Nil</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Nil</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsNil()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsNil();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsNotEqualTo <em>Property Is Not Equal To</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Not Equal To</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsNotEqualTo()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsNotEqualTo();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getPropertyIsNull <em>Property Is Null</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Property Is Null</em>'.
     * @see net.opengis.fes20.DocumentRoot#getPropertyIsNull()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PropertyIsNull();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getResourceId <em>Resource Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Resource Id</em>'.
     * @see net.opengis.fes20.DocumentRoot#getResourceId()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ResourceId();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getSortBy <em>Sort By</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Sort By</em>'.
     * @see net.opengis.fes20.DocumentRoot#getSortBy()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_SortBy();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getTContains <em>TContains</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>TContains</em>'.
     * @see net.opengis.fes20.DocumentRoot#getTContains()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TContains();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getTEquals <em>TEquals</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>TEquals</em>'.
     * @see net.opengis.fes20.DocumentRoot#getTEquals()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TEquals();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getTouches <em>Touches</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Touches</em>'.
     * @see net.opengis.fes20.DocumentRoot#getTouches()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Touches();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getTOverlaps <em>TOverlaps</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>TOverlaps</em>'.
     * @see net.opengis.fes20.DocumentRoot#getTOverlaps()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TOverlaps();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.DocumentRoot#getValueReference <em>Value Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value Reference</em>'.
     * @see net.opengis.fes20.DocumentRoot#getValueReference()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_ValueReference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.DocumentRoot#getWithin <em>Within</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Within</em>'.
     * @see net.opengis.fes20.DocumentRoot#getWithin()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Within();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ExtendedCapabilitiesType <em>Extended Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Extended Capabilities Type</em>'.
     * @see net.opengis.fes20.ExtendedCapabilitiesType
     * @generated
     */
    EClass getExtendedCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.ExtendedCapabilitiesType#getAdditionalOperators <em>Additional Operators</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Additional Operators</em>'.
     * @see net.opengis.fes20.ExtendedCapabilitiesType#getAdditionalOperators()
     * @see #getExtendedCapabilitiesType()
     * @generated
     */
    EReference getExtendedCapabilitiesType_AdditionalOperators();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ExtensionOperatorType <em>Extension Operator Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Extension Operator Type</em>'.
     * @see net.opengis.fes20.ExtensionOperatorType
     * @generated
     */
    EClass getExtensionOperatorType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ExtensionOperatorType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.ExtensionOperatorType#getName()
     * @see #getExtensionOperatorType()
     * @generated
     */
    EAttribute getExtensionOperatorType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ExtensionOpsType <em>Extension Ops Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Extension Ops Type</em>'.
     * @see net.opengis.fes20.ExtensionOpsType
     * @generated
     */
    EClass getExtensionOpsType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.FilterCapabilitiesType <em>Filter Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Filter Capabilities Type</em>'.
     * @see net.opengis.fes20.FilterCapabilitiesType
     * @generated
     */
    EClass getFilterCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterCapabilitiesType#getConformance <em>Conformance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Conformance</em>'.
     * @see net.opengis.fes20.FilterCapabilitiesType#getConformance()
     * @see #getFilterCapabilitiesType()
     * @generated
     */
    EReference getFilterCapabilitiesType_Conformance();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterCapabilitiesType#getIdCapabilities <em>Id Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Id Capabilities</em>'.
     * @see net.opengis.fes20.FilterCapabilitiesType#getIdCapabilities()
     * @see #getFilterCapabilitiesType()
     * @generated
     */
    EReference getFilterCapabilitiesType_IdCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterCapabilitiesType#getScalarCapabilities <em>Scalar Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Scalar Capabilities</em>'.
     * @see net.opengis.fes20.FilterCapabilitiesType#getScalarCapabilities()
     * @see #getFilterCapabilitiesType()
     * @generated
     */
    EReference getFilterCapabilitiesType_ScalarCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterCapabilitiesType#getSpatialCapabilities <em>Spatial Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spatial Capabilities</em>'.
     * @see net.opengis.fes20.FilterCapabilitiesType#getSpatialCapabilities()
     * @see #getFilterCapabilitiesType()
     * @generated
     */
    EReference getFilterCapabilitiesType_SpatialCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterCapabilitiesType#getTemporalCapabilities <em>Temporal Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Capabilities</em>'.
     * @see net.opengis.fes20.FilterCapabilitiesType#getTemporalCapabilities()
     * @see #getFilterCapabilitiesType()
     * @generated
     */
    EReference getFilterCapabilitiesType_TemporalCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterCapabilitiesType#getFunctions <em>Functions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Functions</em>'.
     * @see net.opengis.fes20.FilterCapabilitiesType#getFunctions()
     * @see #getFilterCapabilitiesType()
     * @generated
     */
    EReference getFilterCapabilitiesType_Functions();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterCapabilitiesType#getExtendedCapabilities <em>Extended Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extended Capabilities</em>'.
     * @see net.opengis.fes20.FilterCapabilitiesType#getExtendedCapabilities()
     * @see #getFilterCapabilitiesType()
     * @generated
     */
    EReference getFilterCapabilitiesType_ExtendedCapabilities();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.FilterType <em>Filter Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Filter Type</em>'.
     * @see net.opengis.fes20.FilterType
     * @generated
     */
    EClass getFilterType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.FilterType#getComparisonOpsGroup <em>Comparison Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Comparison Ops Group</em>'.
     * @see net.opengis.fes20.FilterType#getComparisonOpsGroup()
     * @see #getFilterType()
     * @generated
     */
    EAttribute getFilterType_ComparisonOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterType#getComparisonOps <em>Comparison Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Comparison Ops</em>'.
     * @see net.opengis.fes20.FilterType#getComparisonOps()
     * @see #getFilterType()
     * @generated
     */
    EReference getFilterType_ComparisonOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.FilterType#getSpatialOpsGroup <em>Spatial Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Spatial Ops Group</em>'.
     * @see net.opengis.fes20.FilterType#getSpatialOpsGroup()
     * @see #getFilterType()
     * @generated
     */
    EAttribute getFilterType_SpatialOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterType#getSpatialOps <em>Spatial Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spatial Ops</em>'.
     * @see net.opengis.fes20.FilterType#getSpatialOps()
     * @see #getFilterType()
     * @generated
     */
    EReference getFilterType_SpatialOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.FilterType#getTemporalOpsGroup <em>Temporal Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Temporal Ops Group</em>'.
     * @see net.opengis.fes20.FilterType#getTemporalOpsGroup()
     * @see #getFilterType()
     * @generated
     */
    EAttribute getFilterType_TemporalOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterType#getTemporalOps <em>Temporal Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Ops</em>'.
     * @see net.opengis.fes20.FilterType#getTemporalOps()
     * @see #getFilterType()
     * @generated
     */
    EReference getFilterType_TemporalOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.FilterType#getLogicOpsGroup <em>Logic Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Logic Ops Group</em>'.
     * @see net.opengis.fes20.FilterType#getLogicOpsGroup()
     * @see #getFilterType()
     * @generated
     */
    EAttribute getFilterType_LogicOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterType#getLogicOps <em>Logic Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Logic Ops</em>'.
     * @see net.opengis.fes20.FilterType#getLogicOps()
     * @see #getFilterType()
     * @generated
     */
    EReference getFilterType_LogicOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.FilterType#getExtensionOpsGroup <em>Extension Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Extension Ops Group</em>'.
     * @see net.opengis.fes20.FilterType#getExtensionOpsGroup()
     * @see #getFilterType()
     * @generated
     */
    EAttribute getFilterType_ExtensionOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterType#getExtensionOps <em>Extension Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extension Ops</em>'.
     * @see net.opengis.fes20.FilterType#getExtensionOps()
     * @see #getFilterType()
     * @generated
     */
    EReference getFilterType_ExtensionOps();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.FilterType#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Function</em>'.
     * @see net.opengis.fes20.FilterType#getFunction()
     * @see #getFilterType()
     * @generated
     */
    EReference getFilterType_Function();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.FilterType#getIdGroup <em>Id Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Id Group</em>'.
     * @see net.opengis.fes20.FilterType#getIdGroup()
     * @see #getFilterType()
     * @generated
     */
    EAttribute getFilterType_IdGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.FilterType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Id</em>'.
     * @see net.opengis.fes20.FilterType#getId()
     * @see #getFilterType()
     * @generated
     */
    EReference getFilterType_Id();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.FunctionType <em>Function Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Function Type</em>'.
     * @see net.opengis.fes20.FunctionType
     * @generated
     */
    EClass getFunctionType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.FunctionType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.FunctionType#getExpressionGroup()
     * @see #getFunctionType()
     * @generated
     */
    EAttribute getFunctionType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.FunctionType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Expression</em>'.
     * @see net.opengis.fes20.FunctionType#getExpression()
     * @see #getFunctionType()
     * @generated
     */
    EReference getFunctionType_Expression();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.FunctionType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.FunctionType#getName()
     * @see #getFunctionType()
     * @generated
     */
    EAttribute getFunctionType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.GeometryOperandsType <em>Geometry Operands Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Geometry Operands Type</em>'.
     * @see net.opengis.fes20.GeometryOperandsType
     * @generated
     */
    EClass getGeometryOperandsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.GeometryOperandsType#getGeometryOperand <em>Geometry Operand</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Geometry Operand</em>'.
     * @see net.opengis.fes20.GeometryOperandsType#getGeometryOperand()
     * @see #getGeometryOperandsType()
     * @generated
     */
    EReference getGeometryOperandsType_GeometryOperand();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.GeometryOperandType <em>Geometry Operand Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Geometry Operand Type</em>'.
     * @see net.opengis.fes20.GeometryOperandType
     * @generated
     */
    EClass getGeometryOperandType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.GeometryOperandType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.GeometryOperandType#getName()
     * @see #getGeometryOperandType()
     * @generated
     */
    EAttribute getGeometryOperandType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.IdCapabilitiesType <em>Id Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Id Capabilities Type</em>'.
     * @see net.opengis.fes20.IdCapabilitiesType
     * @generated
     */
    EClass getIdCapabilitiesType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.IdCapabilitiesType#getResourceIdentifier <em>Resource Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Resource Identifier</em>'.
     * @see net.opengis.fes20.IdCapabilitiesType#getResourceIdentifier()
     * @see #getIdCapabilitiesType()
     * @generated
     */
    EReference getIdCapabilitiesType_ResourceIdentifier();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.LiteralType <em>Literal Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Literal Type</em>'.
     * @see net.opengis.fes20.LiteralType
     * @generated
     */
    EClass getLiteralType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.LiteralType#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.fes20.LiteralType#getMixed()
     * @see #getLiteralType()
     * @generated
     */
    EAttribute getLiteralType_Mixed();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.LiteralType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see net.opengis.fes20.LiteralType#getAny()
     * @see #getLiteralType()
     * @generated
     */
    EAttribute getLiteralType_Any();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.LiteralType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see net.opengis.fes20.LiteralType#getType()
     * @see #getLiteralType()
     * @generated
     */
    EAttribute getLiteralType_Type();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.LogicalOperatorsType <em>Logical Operators Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Logical Operators Type</em>'.
     * @see net.opengis.fes20.LogicalOperatorsType
     * @generated
     */
    EClass getLogicalOperatorsType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.LogicOpsType <em>Logic Ops Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Logic Ops Type</em>'.
     * @see net.opengis.fes20.LogicOpsType
     * @generated
     */
    EClass getLogicOpsType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.LowerBoundaryType <em>Lower Boundary Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Lower Boundary Type</em>'.
     * @see net.opengis.fes20.LowerBoundaryType
     * @generated
     */
    EClass getLowerBoundaryType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.LowerBoundaryType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.LowerBoundaryType#getExpressionGroup()
     * @see #getLowerBoundaryType()
     * @generated
     */
    EAttribute getLowerBoundaryType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.LowerBoundaryType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.LowerBoundaryType#getExpression()
     * @see #getLowerBoundaryType()
     * @generated
     */
    EReference getLowerBoundaryType_Expression();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.MeasureType <em>Measure Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Measure Type</em>'.
     * @see net.opengis.fes20.MeasureType
     * @generated
     */
    EClass getMeasureType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.MeasureType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.fes20.MeasureType#getValue()
     * @see #getMeasureType()
     * @generated
     */
    EAttribute getMeasureType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.MeasureType#getUom <em>Uom</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Uom</em>'.
     * @see net.opengis.fes20.MeasureType#getUom()
     * @see #getMeasureType()
     * @generated
     */
    EAttribute getMeasureType_Uom();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.PropertyIsBetweenType <em>Property Is Between Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Property Is Between Type</em>'.
     * @see net.opengis.fes20.PropertyIsBetweenType
     * @generated
     */
    EClass getPropertyIsBetweenType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.PropertyIsBetweenType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.PropertyIsBetweenType#getExpressionGroup()
     * @see #getPropertyIsBetweenType()
     * @generated
     */
    EAttribute getPropertyIsBetweenType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.PropertyIsBetweenType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.PropertyIsBetweenType#getExpression()
     * @see #getPropertyIsBetweenType()
     * @generated
     */
    EReference getPropertyIsBetweenType_Expression();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.PropertyIsBetweenType#getLowerBoundary <em>Lower Boundary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Lower Boundary</em>'.
     * @see net.opengis.fes20.PropertyIsBetweenType#getLowerBoundary()
     * @see #getPropertyIsBetweenType()
     * @generated
     */
    EReference getPropertyIsBetweenType_LowerBoundary();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.PropertyIsBetweenType#getUpperBoundary <em>Upper Boundary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Upper Boundary</em>'.
     * @see net.opengis.fes20.PropertyIsBetweenType#getUpperBoundary()
     * @see #getPropertyIsBetweenType()
     * @generated
     */
    EReference getPropertyIsBetweenType_UpperBoundary();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.PropertyIsLikeType <em>Property Is Like Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Property Is Like Type</em>'.
     * @see net.opengis.fes20.PropertyIsLikeType
     * @generated
     */
    EClass getPropertyIsLikeType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.PropertyIsLikeType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.PropertyIsLikeType#getExpressionGroup()
     * @see #getPropertyIsLikeType()
     * @generated
     */
    EAttribute getPropertyIsLikeType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.PropertyIsLikeType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Expression</em>'.
     * @see net.opengis.fes20.PropertyIsLikeType#getExpression()
     * @see #getPropertyIsLikeType()
     * @generated
     */
    EReference getPropertyIsLikeType_Expression();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.PropertyIsLikeType#getEscapeChar <em>Escape Char</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Escape Char</em>'.
     * @see net.opengis.fes20.PropertyIsLikeType#getEscapeChar()
     * @see #getPropertyIsLikeType()
     * @generated
     */
    EAttribute getPropertyIsLikeType_EscapeChar();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.PropertyIsLikeType#getSingleChar <em>Single Char</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Single Char</em>'.
     * @see net.opengis.fes20.PropertyIsLikeType#getSingleChar()
     * @see #getPropertyIsLikeType()
     * @generated
     */
    EAttribute getPropertyIsLikeType_SingleChar();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.PropertyIsLikeType#getWildCard <em>Wild Card</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Wild Card</em>'.
     * @see net.opengis.fes20.PropertyIsLikeType#getWildCard()
     * @see #getPropertyIsLikeType()
     * @generated
     */
    EAttribute getPropertyIsLikeType_WildCard();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.PropertyIsNilType <em>Property Is Nil Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Property Is Nil Type</em>'.
     * @see net.opengis.fes20.PropertyIsNilType
     * @generated
     */
    EClass getPropertyIsNilType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.PropertyIsNilType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.PropertyIsNilType#getExpressionGroup()
     * @see #getPropertyIsNilType()
     * @generated
     */
    EAttribute getPropertyIsNilType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.PropertyIsNilType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.PropertyIsNilType#getExpression()
     * @see #getPropertyIsNilType()
     * @generated
     */
    EReference getPropertyIsNilType_Expression();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.PropertyIsNilType#getNilReason <em>Nil Reason</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Nil Reason</em>'.
     * @see net.opengis.fes20.PropertyIsNilType#getNilReason()
     * @see #getPropertyIsNilType()
     * @generated
     */
    EAttribute getPropertyIsNilType_NilReason();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.PropertyIsNullType <em>Property Is Null Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Property Is Null Type</em>'.
     * @see net.opengis.fes20.PropertyIsNullType
     * @generated
     */
    EClass getPropertyIsNullType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.PropertyIsNullType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.PropertyIsNullType#getExpressionGroup()
     * @see #getPropertyIsNullType()
     * @generated
     */
    EAttribute getPropertyIsNullType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.PropertyIsNullType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.PropertyIsNullType#getExpression()
     * @see #getPropertyIsNullType()
     * @generated
     */
    EReference getPropertyIsNullType_Expression();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ResourceIdentifierType <em>Resource Identifier Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Resource Identifier Type</em>'.
     * @see net.opengis.fes20.ResourceIdentifierType
     * @generated
     */
    EClass getResourceIdentifierType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.ResourceIdentifierType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Metadata</em>'.
     * @see net.opengis.fes20.ResourceIdentifierType#getMetadata()
     * @see #getResourceIdentifierType()
     * @generated
     */
    EReference getResourceIdentifierType_Metadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ResourceIdentifierType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.ResourceIdentifierType#getName()
     * @see #getResourceIdentifierType()
     * @generated
     */
    EAttribute getResourceIdentifierType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ResourceIdType <em>Resource Id Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Resource Id Type</em>'.
     * @see net.opengis.fes20.ResourceIdType
     * @generated
     */
    EClass getResourceIdType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ResourceIdType#getEndDate <em>End Date</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Date</em>'.
     * @see net.opengis.fes20.ResourceIdType#getEndDate()
     * @see #getResourceIdType()
     * @generated
     */
    EAttribute getResourceIdType_EndDate();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ResourceIdType#getPreviousRid <em>Previous Rid</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Previous Rid</em>'.
     * @see net.opengis.fes20.ResourceIdType#getPreviousRid()
     * @see #getResourceIdType()
     * @generated
     */
    EAttribute getResourceIdType_PreviousRid();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ResourceIdType#getRid <em>Rid</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Rid</em>'.
     * @see net.opengis.fes20.ResourceIdType#getRid()
     * @see #getResourceIdType()
     * @generated
     */
    EAttribute getResourceIdType_Rid();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ResourceIdType#getStartDate <em>Start Date</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Start Date</em>'.
     * @see net.opengis.fes20.ResourceIdType#getStartDate()
     * @see #getResourceIdType()
     * @generated
     */
    EAttribute getResourceIdType_StartDate();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.ResourceIdType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.fes20.ResourceIdType#getVersion()
     * @see #getResourceIdType()
     * @generated
     */
    EAttribute getResourceIdType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.ScalarCapabilitiesType <em>Scalar Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Scalar Capabilities Type</em>'.
     * @see net.opengis.fes20.ScalarCapabilitiesType
     * @generated
     */
    EClass getScalarCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.ScalarCapabilitiesType#getLogicalOperators <em>Logical Operators</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Logical Operators</em>'.
     * @see net.opengis.fes20.ScalarCapabilitiesType#getLogicalOperators()
     * @see #getScalarCapabilitiesType()
     * @generated
     */
    EReference getScalarCapabilitiesType_LogicalOperators();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.ScalarCapabilitiesType#getComparisonOperators <em>Comparison Operators</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Comparison Operators</em>'.
     * @see net.opengis.fes20.ScalarCapabilitiesType#getComparisonOperators()
     * @see #getScalarCapabilitiesType()
     * @generated
     */
    EReference getScalarCapabilitiesType_ComparisonOperators();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.SortByType <em>Sort By Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sort By Type</em>'.
     * @see net.opengis.fes20.SortByType
     * @generated
     */
    EClass getSortByType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.SortByType#getSortProperty <em>Sort Property</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Sort Property</em>'.
     * @see net.opengis.fes20.SortByType#getSortProperty()
     * @see #getSortByType()
     * @generated
     */
    EReference getSortByType_SortProperty();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.SortPropertyType <em>Sort Property Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sort Property Type</em>'.
     * @see net.opengis.fes20.SortPropertyType
     * @generated
     */
    EClass getSortPropertyType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.SortPropertyType#getValueReference <em>Value Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value Reference</em>'.
     * @see net.opengis.fes20.SortPropertyType#getValueReference()
     * @see #getSortPropertyType()
     * @generated
     */
    EAttribute getSortPropertyType_ValueReference();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sort Order</em>'.
     * @see net.opengis.fes20.SortPropertyType#getSortOrder()
     * @see #getSortPropertyType()
     * @generated
     */
    EAttribute getSortPropertyType_SortOrder();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.SpatialCapabilitiesType <em>Spatial Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Spatial Capabilities Type</em>'.
     * @see net.opengis.fes20.SpatialCapabilitiesType
     * @generated
     */
    EClass getSpatialCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.SpatialCapabilitiesType#getGeometryOperands <em>Geometry Operands</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Geometry Operands</em>'.
     * @see net.opengis.fes20.SpatialCapabilitiesType#getGeometryOperands()
     * @see #getSpatialCapabilitiesType()
     * @generated
     */
    EReference getSpatialCapabilitiesType_GeometryOperands();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.SpatialCapabilitiesType#getSpatialOperators <em>Spatial Operators</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spatial Operators</em>'.
     * @see net.opengis.fes20.SpatialCapabilitiesType#getSpatialOperators()
     * @see #getSpatialCapabilitiesType()
     * @generated
     */
    EReference getSpatialCapabilitiesType_SpatialOperators();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.SpatialOperatorsType <em>Spatial Operators Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Spatial Operators Type</em>'.
     * @see net.opengis.fes20.SpatialOperatorsType
     * @generated
     */
    EClass getSpatialOperatorsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.SpatialOperatorsType#getSpatialOperator <em>Spatial Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Spatial Operator</em>'.
     * @see net.opengis.fes20.SpatialOperatorsType#getSpatialOperator()
     * @see #getSpatialOperatorsType()
     * @generated
     */
    EReference getSpatialOperatorsType_SpatialOperator();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.SpatialOperatorType <em>Spatial Operator Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Spatial Operator Type</em>'.
     * @see net.opengis.fes20.SpatialOperatorType
     * @generated
     */
    EClass getSpatialOperatorType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.SpatialOperatorType#getGeometryOperands <em>Geometry Operands</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Geometry Operands</em>'.
     * @see net.opengis.fes20.SpatialOperatorType#getGeometryOperands()
     * @see #getSpatialOperatorType()
     * @generated
     */
    EReference getSpatialOperatorType_GeometryOperands();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.SpatialOperatorType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.SpatialOperatorType#getName()
     * @see #getSpatialOperatorType()
     * @generated
     */
    EAttribute getSpatialOperatorType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.SpatialOpsType <em>Spatial Ops Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Spatial Ops Type</em>'.
     * @see net.opengis.fes20.SpatialOpsType
     * @generated
     */
    EClass getSpatialOpsType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.TemporalCapabilitiesType <em>Temporal Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Temporal Capabilities Type</em>'.
     * @see net.opengis.fes20.TemporalCapabilitiesType
     * @generated
     */
    EClass getTemporalCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperands <em>Temporal Operands</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Operands</em>'.
     * @see net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperands()
     * @see #getTemporalCapabilitiesType()
     * @generated
     */
    EReference getTemporalCapabilitiesType_TemporalOperands();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperators <em>Temporal Operators</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Operators</em>'.
     * @see net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperators()
     * @see #getTemporalCapabilitiesType()
     * @generated
     */
    EReference getTemporalCapabilitiesType_TemporalOperators();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.TemporalOperandsType <em>Temporal Operands Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Temporal Operands Type</em>'.
     * @see net.opengis.fes20.TemporalOperandsType
     * @generated
     */
    EClass getTemporalOperandsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.TemporalOperandsType#getTemporalOperand <em>Temporal Operand</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Temporal Operand</em>'.
     * @see net.opengis.fes20.TemporalOperandsType#getTemporalOperand()
     * @see #getTemporalOperandsType()
     * @generated
     */
    EReference getTemporalOperandsType_TemporalOperand();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.TemporalOperandType <em>Temporal Operand Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Temporal Operand Type</em>'.
     * @see net.opengis.fes20.TemporalOperandType
     * @generated
     */
    EClass getTemporalOperandType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.TemporalOperandType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.TemporalOperandType#getName()
     * @see #getTemporalOperandType()
     * @generated
     */
    EAttribute getTemporalOperandType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.TemporalOperatorsType <em>Temporal Operators Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Temporal Operators Type</em>'.
     * @see net.opengis.fes20.TemporalOperatorsType
     * @generated
     */
    EClass getTemporalOperatorsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.TemporalOperatorsType#getTemporalOperator <em>Temporal Operator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Temporal Operator</em>'.
     * @see net.opengis.fes20.TemporalOperatorsType#getTemporalOperator()
     * @see #getTemporalOperatorsType()
     * @generated
     */
    EReference getTemporalOperatorsType_TemporalOperator();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.TemporalOperatorType <em>Temporal Operator Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Temporal Operator Type</em>'.
     * @see net.opengis.fes20.TemporalOperatorType
     * @generated
     */
    EClass getTemporalOperatorType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.TemporalOperatorType#getTemporalOperands <em>Temporal Operands</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Operands</em>'.
     * @see net.opengis.fes20.TemporalOperatorType#getTemporalOperands()
     * @see #getTemporalOperatorType()
     * @generated
     */
    EReference getTemporalOperatorType_TemporalOperands();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.fes20.TemporalOperatorType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.fes20.TemporalOperatorType#getName()
     * @see #getTemporalOperatorType()
     * @generated
     */
    EAttribute getTemporalOperatorType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.TemporalOpsType <em>Temporal Ops Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Temporal Ops Type</em>'.
     * @see net.opengis.fes20.TemporalOpsType
     * @generated
     */
    EClass getTemporalOpsType();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.UnaryLogicOpType <em>Unary Logic Op Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Unary Logic Op Type</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType
     * @generated
     */
    EClass getUnaryLogicOpType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.UnaryLogicOpType#getComparisonOpsGroup <em>Comparison Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Comparison Ops Group</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getComparisonOpsGroup()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EAttribute getUnaryLogicOpType_ComparisonOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.UnaryLogicOpType#getComparisonOps <em>Comparison Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Comparison Ops</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getComparisonOps()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EReference getUnaryLogicOpType_ComparisonOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.UnaryLogicOpType#getSpatialOpsGroup <em>Spatial Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Spatial Ops Group</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getSpatialOpsGroup()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EAttribute getUnaryLogicOpType_SpatialOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.UnaryLogicOpType#getSpatialOps <em>Spatial Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spatial Ops</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getSpatialOps()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EReference getUnaryLogicOpType_SpatialOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.UnaryLogicOpType#getTemporalOpsGroup <em>Temporal Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Temporal Ops Group</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getTemporalOpsGroup()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EAttribute getUnaryLogicOpType_TemporalOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.UnaryLogicOpType#getTemporalOps <em>Temporal Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Ops</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getTemporalOps()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EReference getUnaryLogicOpType_TemporalOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.UnaryLogicOpType#getLogicOpsGroup <em>Logic Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Logic Ops Group</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getLogicOpsGroup()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EAttribute getUnaryLogicOpType_LogicOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.UnaryLogicOpType#getLogicOps <em>Logic Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Logic Ops</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getLogicOps()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EReference getUnaryLogicOpType_LogicOps();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.UnaryLogicOpType#getExtensionOpsGroup <em>Extension Ops Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Extension Ops Group</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getExtensionOpsGroup()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EAttribute getUnaryLogicOpType_ExtensionOpsGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.UnaryLogicOpType#getExtensionOps <em>Extension Ops</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extension Ops</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getExtensionOps()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EReference getUnaryLogicOpType_ExtensionOps();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.UnaryLogicOpType#getFunction <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Function</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getFunction()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EReference getUnaryLogicOpType_Function();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.UnaryLogicOpType#getIdGroup <em>Id Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Id Group</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getIdGroup()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EAttribute getUnaryLogicOpType_IdGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.fes20.UnaryLogicOpType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Id</em>'.
     * @see net.opengis.fes20.UnaryLogicOpType#getId()
     * @see #getUnaryLogicOpType()
     * @generated
     */
    EReference getUnaryLogicOpType_Id();

    /**
     * Returns the meta object for class '{@link net.opengis.fes20.UpperBoundaryType <em>Upper Boundary Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Upper Boundary Type</em>'.
     * @see net.opengis.fes20.UpperBoundaryType
     * @generated
     */
    EClass getUpperBoundaryType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.fes20.UpperBoundaryType#getExpressionGroup <em>Expression Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Expression Group</em>'.
     * @see net.opengis.fes20.UpperBoundaryType#getExpressionGroup()
     * @see #getUpperBoundaryType()
     * @generated
     */
    EAttribute getUpperBoundaryType_ExpressionGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.fes20.UpperBoundaryType#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Expression</em>'.
     * @see net.opengis.fes20.UpperBoundaryType#getExpression()
     * @see #getUpperBoundaryType()
     * @generated
     */
    EReference getUpperBoundaryType_Expression();

    /**
     * Returns the meta object for enum '{@link net.opengis.fes20.ComparisonOperatorNameTypeMember0 <em>Comparison Operator Name Type Member0</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Comparison Operator Name Type Member0</em>'.
     * @see net.opengis.fes20.ComparisonOperatorNameTypeMember0
     * @generated
     */
    EEnum getComparisonOperatorNameTypeMember0();

    /**
     * Returns the meta object for enum '{@link net.opengis.fes20.MatchActionType <em>Match Action Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Match Action Type</em>'.
     * @see net.opengis.fes20.MatchActionType
     * @generated
     */
    EEnum getMatchActionType();

    /**
     * Returns the meta object for enum '{@link net.opengis.fes20.SortOrderType <em>Sort Order Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Sort Order Type</em>'.
     * @see net.opengis.fes20.SortOrderType
     * @generated
     */
    EEnum getSortOrderType();

    /**
     * Returns the meta object for enum '{@link net.opengis.fes20.SpatialOperatorNameTypeMember0 <em>Spatial Operator Name Type Member0</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Spatial Operator Name Type Member0</em>'.
     * @see net.opengis.fes20.SpatialOperatorNameTypeMember0
     * @generated
     */
    EEnum getSpatialOperatorNameTypeMember0();

    /**
     * Returns the meta object for enum '{@link net.opengis.fes20.TemporalOperatorNameTypeMember0 <em>Temporal Operator Name Type Member0</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Temporal Operator Name Type Member0</em>'.
     * @see net.opengis.fes20.TemporalOperatorNameTypeMember0
     * @generated
     */
    EEnum getTemporalOperatorNameTypeMember0();

    /**
     * Returns the meta object for enum '{@link net.opengis.fes20.VersionActionTokens <em>Version Action Tokens</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Version Action Tokens</em>'.
     * @see net.opengis.fes20.VersionActionTokens
     * @generated
     */
    EEnum getVersionActionTokens();

    /**
     * Returns the meta object for data type '{@link java.util.List <em>Aliases Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Aliases Type</em>'.
     * @see java.util.List
     * @model instanceClass="java.util.List"
     *        extendedMetaData="name='AliasesType' itemType='http://www.eclipse.org/emf/2003/XMLType#NCName'"
     * @generated
     */
    EDataType getAliasesType();

    /**
     * Returns the meta object for data type '{@link java.lang.Object <em>Comparison Operator Name Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Comparison Operator Name Type</em>'.
     * @see java.lang.Object
     * @model instanceClass="java.lang.Object"
     *        extendedMetaData="name='ComparisonOperatorNameType' memberTypes='ComparisonOperatorNameType_._member_._0 ComparisonOperatorNameType_._member_._1'"
     * @generated
     */
    EDataType getComparisonOperatorNameType();

    /**
     * Returns the meta object for data type '{@link net.opengis.fes20.ComparisonOperatorNameTypeMember0 <em>Comparison Operator Name Type Member0 Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Comparison Operator Name Type Member0 Object</em>'.
     * @see net.opengis.fes20.ComparisonOperatorNameTypeMember0
     * @model instanceClass="net.opengis.fes20.ComparisonOperatorNameTypeMember0"
     *        extendedMetaData="name='ComparisonOperatorNameType_._member_._0:Object' baseType='ComparisonOperatorNameType_._member_._0'"
     * @generated
     */
    EDataType getComparisonOperatorNameTypeMember0Object();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Comparison Operator Name Type Member1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Comparison Operator Name Type Member1</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='ComparisonOperatorNameType_._member_._1' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='extension:\\w{2,}'"
     * @generated
     */
    EDataType getComparisonOperatorNameTypeMember1();

    /**
     * Returns the meta object for data type '{@link net.opengis.fes20.MatchActionType <em>Match Action Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Match Action Type Object</em>'.
     * @see net.opengis.fes20.MatchActionType
     * @model instanceClass="net.opengis.fes20.MatchActionType"
     *        extendedMetaData="name='MatchActionType:Object' baseType='MatchActionType'"
     * @generated
     */
    EDataType getMatchActionTypeObject();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Schema Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Schema Element</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='SchemaElement' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='schema\\-element\\(.+\\)'"
     * @generated
     */
    EDataType getSchemaElement();

    /**
     * Returns the meta object for data type '{@link net.opengis.fes20.SortOrderType <em>Sort Order Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Sort Order Type Object</em>'.
     * @see net.opengis.fes20.SortOrderType
     * @model instanceClass="net.opengis.fes20.SortOrderType"
     *        extendedMetaData="name='SortOrderType:Object' baseType='SortOrderType'"
     * @generated
     */
    EDataType getSortOrderTypeObject();

    /**
     * Returns the meta object for data type '{@link java.lang.Object <em>Spatial Operator Name Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Spatial Operator Name Type</em>'.
     * @see java.lang.Object
     * @model instanceClass="java.lang.Object"
     *        extendedMetaData="name='SpatialOperatorNameType' memberTypes='SpatialOperatorNameType_._member_._0 SpatialOperatorNameType_._member_._1'"
     * @generated
     */
    EDataType getSpatialOperatorNameType();

    /**
     * Returns the meta object for data type '{@link net.opengis.fes20.SpatialOperatorNameTypeMember0 <em>Spatial Operator Name Type Member0 Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Spatial Operator Name Type Member0 Object</em>'.
     * @see net.opengis.fes20.SpatialOperatorNameTypeMember0
     * @model instanceClass="net.opengis.fes20.SpatialOperatorNameTypeMember0"
     *        extendedMetaData="name='SpatialOperatorNameType_._member_._0:Object' baseType='SpatialOperatorNameType_._member_._0'"
     * @generated
     */
    EDataType getSpatialOperatorNameTypeMember0Object();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Spatial Operator Name Type Member1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Spatial Operator Name Type Member1</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='SpatialOperatorNameType_._member_._1' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='extension:\\w{2,}'"
     * @generated
     */
    EDataType getSpatialOperatorNameTypeMember1();

    /**
     * Returns the meta object for data type '{@link java.lang.Object <em>Temporal Operator Name Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Temporal Operator Name Type</em>'.
     * @see java.lang.Object
     * @model instanceClass="java.lang.Object"
     *        extendedMetaData="name='TemporalOperatorNameType' memberTypes='TemporalOperatorNameType_._member_._0 TemporalOperatorNameType_._member_._1'"
     * @generated
     */
    EDataType getTemporalOperatorNameType();

    /**
     * Returns the meta object for data type '{@link net.opengis.fes20.TemporalOperatorNameTypeMember0 <em>Temporal Operator Name Type Member0 Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Temporal Operator Name Type Member0 Object</em>'.
     * @see net.opengis.fes20.TemporalOperatorNameTypeMember0
     * @model instanceClass="net.opengis.fes20.TemporalOperatorNameTypeMember0"
     *        extendedMetaData="name='TemporalOperatorNameType_._member_._0:Object' baseType='TemporalOperatorNameType_._member_._0'"
     * @generated
     */
    EDataType getTemporalOperatorNameTypeMember0Object();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Temporal Operator Name Type Member1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Temporal Operator Name Type Member1</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='TemporalOperatorNameType_._member_._1' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='extension:\\w{2,}'"
     * @generated
     */
    EDataType getTemporalOperatorNameTypeMember1();

    /**
     * Returns the meta object for data type '{@link java.util.List <em>Type Names List Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Type Names List Type</em>'.
     * @see java.util.List
     * @model instanceClass="java.util.List"
     *        extendedMetaData="name='TypeNamesListType' itemType='TypeNamesType'"
     * @generated
     */
    EDataType getTypeNamesListType();

    /**
     * Returns the meta object for data type '{@link java.lang.Object <em>Type Names Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Type Names Type</em>'.
     * @see java.lang.Object
     * @model instanceClass="java.lang.Object"
     *        extendedMetaData="name='TypeNamesType' memberTypes='SchemaElement http://www.eclipse.org/emf/2003/XMLType#QName'"
     * @generated
     */
    EDataType getTypeNamesType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Uom Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Uom Identifier</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='UomIdentifier' memberTypes='UomSymbol UomURI'"
     * @generated
     */
    EDataType getUomIdentifier();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Uom Symbol</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Uom Symbol</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='UomSymbol' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='[^:%20\\n\\r\\t]+'"
     * @generated
     */
    EDataType getUomSymbol();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Uom URI</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Uom URI</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='UomURI' baseType='http://www.eclipse.org/emf/2003/XMLType#anyURI' pattern='([a-zA-Z][a-zA-Z0-9\\-\\+\\.]*:|\\.\\./|\\./|#).*'"
     * @generated
     */
    EDataType getUomURI();

    /**
     * Returns the meta object for data type '{@link net.opengis.fes20.VersionActionTokens <em>Version Action Tokens Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Version Action Tokens Object</em>'.
     * @see net.opengis.fes20.VersionActionTokens
     * @model instanceClass="net.opengis.fes20.VersionActionTokens"
     *        extendedMetaData="name='VersionActionTokens:Object' baseType='VersionActionTokens'"
     * @generated
     */
    EDataType getVersionActionTokensObject();

    /**
     * Returns the meta object for data type '{@link java.lang.Object <em>Version Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Version Type</em>'.
     * @see java.lang.Object
     * @model instanceClass="java.lang.Object"
     *        extendedMetaData="name='VersionType' memberTypes='VersionActionTokens http://www.eclipse.org/emf/2003/XMLType#positiveInteger http://www.eclipse.org/emf/2003/XMLType#dateTime'"
     * @generated
     */
    EDataType getVersionType();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    Fes20Factory getFes20Factory();

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
         * The meta object literal for the '{@link net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl <em>Abstract Adhoc Query Expression Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractAdhocQueryExpressionType()
         * @generated
         */
        EClass ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE = eINSTANCE.getAbstractAdhocQueryExpressionType();

        /**
         * The meta object literal for the '<em><b>Abstract Projection Clause</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_PROJECTION_CLAUSE = eINSTANCE.getAbstractAdhocQueryExpressionType_AbstractProjectionClause();

        /**
         * The meta object literal for the '<em><b>Abstract Selection Clause</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SELECTION_CLAUSE = eINSTANCE.getAbstractAdhocQueryExpressionType_AbstractSelectionClause();

        /**
         * The meta object literal for the '<em><b>Abstract Sorting Clause</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SORTING_CLAUSE = eINSTANCE.getAbstractAdhocQueryExpressionType_AbstractSortingClause();

        /**
         * The meta object literal for the '<em><b>Aliases</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ALIASES = eINSTANCE.getAbstractAdhocQueryExpressionType_Aliases();

        /**
         * The meta object literal for the '<em><b>Type Names</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__TYPE_NAMES = eINSTANCE.getAbstractAdhocQueryExpressionType_TypeNames();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.AbstractIdTypeImpl <em>Abstract Id Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AbstractIdTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractIdType()
         * @generated
         */
        EClass ABSTRACT_ID_TYPE = eINSTANCE.getAbstractIdType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.AbstractProjectionClauseTypeImpl <em>Abstract Projection Clause Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AbstractProjectionClauseTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractProjectionClauseType()
         * @generated
         */
        EClass ABSTRACT_PROJECTION_CLAUSE_TYPE = eINSTANCE.getAbstractProjectionClauseType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.AbstractQueryExpressionTypeImpl <em>Abstract Query Expression Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AbstractQueryExpressionTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractQueryExpressionType()
         * @generated
         */
        EClass ABSTRACT_QUERY_EXPRESSION_TYPE = eINSTANCE.getAbstractQueryExpressionType();

        /**
         * The meta object literal for the '<em><b>Handle</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_QUERY_EXPRESSION_TYPE__HANDLE = eINSTANCE.getAbstractQueryExpressionType_Handle();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.AbstractSelectionClauseTypeImpl <em>Abstract Selection Clause Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AbstractSelectionClauseTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractSelectionClauseType()
         * @generated
         */
        EClass ABSTRACT_SELECTION_CLAUSE_TYPE = eINSTANCE.getAbstractSelectionClauseType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.AbstractSortingClauseTypeImpl <em>Abstract Sorting Clause Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AbstractSortingClauseTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAbstractSortingClauseType()
         * @generated
         */
        EClass ABSTRACT_SORTING_CLAUSE_TYPE = eINSTANCE.getAbstractSortingClauseType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.AdditionalOperatorsTypeImpl <em>Additional Operators Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AdditionalOperatorsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAdditionalOperatorsType()
         * @generated
         */
        EClass ADDITIONAL_OPERATORS_TYPE = eINSTANCE.getAdditionalOperatorsType();

        /**
         * The meta object literal for the '<em><b>Operator</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ADDITIONAL_OPERATORS_TYPE__OPERATOR = eINSTANCE.getAdditionalOperatorsType_Operator();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ArgumentsTypeImpl <em>Arguments Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ArgumentsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getArgumentsType()
         * @generated
         */
        EClass ARGUMENTS_TYPE = eINSTANCE.getArgumentsType();

        /**
         * The meta object literal for the '<em><b>Argument</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARGUMENTS_TYPE__ARGUMENT = eINSTANCE.getArgumentsType_Argument();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ArgumentTypeImpl <em>Argument Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ArgumentTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getArgumentType()
         * @generated
         */
        EClass ARGUMENT_TYPE = eINSTANCE.getArgumentType();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ARGUMENT_TYPE__METADATA = eINSTANCE.getArgumentType_Metadata();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARGUMENT_TYPE__TYPE = eINSTANCE.getArgumentType_Type();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ARGUMENT_TYPE__NAME = eINSTANCE.getArgumentType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.AvailableFunctionsTypeImpl <em>Available Functions Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AvailableFunctionsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAvailableFunctionsType()
         * @generated
         */
        EClass AVAILABLE_FUNCTIONS_TYPE = eINSTANCE.getAvailableFunctionsType();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AVAILABLE_FUNCTIONS_TYPE__FUNCTION = eINSTANCE.getAvailableFunctionsType_Function();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.AvailableFunctionTypeImpl <em>Available Function Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.AvailableFunctionTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAvailableFunctionType()
         * @generated
         */
        EClass AVAILABLE_FUNCTION_TYPE = eINSTANCE.getAvailableFunctionType();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AVAILABLE_FUNCTION_TYPE__METADATA = eINSTANCE.getAvailableFunctionType_Metadata();

        /**
         * The meta object literal for the '<em><b>Returns</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AVAILABLE_FUNCTION_TYPE__RETURNS = eINSTANCE.getAvailableFunctionType_Returns();

        /**
         * The meta object literal for the '<em><b>Arguments</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AVAILABLE_FUNCTION_TYPE__ARGUMENTS = eINSTANCE.getAvailableFunctionType_Arguments();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AVAILABLE_FUNCTION_TYPE__NAME = eINSTANCE.getAvailableFunctionType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.BBOXTypeImpl <em>BBOX Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.BBOXTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getBBOXType()
         * @generated
         */
        EClass BBOX_TYPE = eINSTANCE.getBBOXType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BBOX_TYPE__EXPRESSION_GROUP = eINSTANCE.getBBOXType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BBOX_TYPE__EXPRESSION = eINSTANCE.getBBOXType_Expression();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BBOX_TYPE__ANY = eINSTANCE.getBBOXType_Any();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.BinaryComparisonOpTypeImpl <em>Binary Comparison Op Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.BinaryComparisonOpTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getBinaryComparisonOpType()
         * @generated
         */
        EClass BINARY_COMPARISON_OP_TYPE = eINSTANCE.getBinaryComparisonOpType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP = eINSTANCE.getBinaryComparisonOpType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_COMPARISON_OP_TYPE__EXPRESSION = eINSTANCE.getBinaryComparisonOpType_Expression();

        /**
         * The meta object literal for the '<em><b>Match Action</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_COMPARISON_OP_TYPE__MATCH_ACTION = eINSTANCE.getBinaryComparisonOpType_MatchAction();

        /**
         * The meta object literal for the '<em><b>Match Case</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_COMPARISON_OP_TYPE__MATCH_CASE = eINSTANCE.getBinaryComparisonOpType_MatchCase();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.BinaryLogicOpTypeImpl <em>Binary Logic Op Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.BinaryLogicOpTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getBinaryLogicOpType()
         * @generated
         */
        EClass BINARY_LOGIC_OP_TYPE = eINSTANCE.getBinaryLogicOpType();

        /**
         * The meta object literal for the '<em><b>Filter Predicates</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_LOGIC_OP_TYPE__FILTER_PREDICATES = eINSTANCE.getBinaryLogicOpType_FilterPredicates();

        /**
         * The meta object literal for the '<em><b>Comparison Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_LOGIC_OP_TYPE__COMPARISON_OPS_GROUP = eINSTANCE.getBinaryLogicOpType_ComparisonOpsGroup();

        /**
         * The meta object literal for the '<em><b>Comparison Ops</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_LOGIC_OP_TYPE__COMPARISON_OPS = eINSTANCE.getBinaryLogicOpType_ComparisonOps();

        /**
         * The meta object literal for the '<em><b>Spatial Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_LOGIC_OP_TYPE__SPATIAL_OPS_GROUP = eINSTANCE.getBinaryLogicOpType_SpatialOpsGroup();

        /**
         * The meta object literal for the '<em><b>Spatial Ops</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_LOGIC_OP_TYPE__SPATIAL_OPS = eINSTANCE.getBinaryLogicOpType_SpatialOps();

        /**
         * The meta object literal for the '<em><b>Temporal Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_LOGIC_OP_TYPE__TEMPORAL_OPS_GROUP = eINSTANCE.getBinaryLogicOpType_TemporalOpsGroup();

        /**
         * The meta object literal for the '<em><b>Temporal Ops</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_LOGIC_OP_TYPE__TEMPORAL_OPS = eINSTANCE.getBinaryLogicOpType_TemporalOps();

        /**
         * The meta object literal for the '<em><b>Logic Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_LOGIC_OP_TYPE__LOGIC_OPS_GROUP = eINSTANCE.getBinaryLogicOpType_LogicOpsGroup();

        /**
         * The meta object literal for the '<em><b>Logic Ops</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_LOGIC_OP_TYPE__LOGIC_OPS = eINSTANCE.getBinaryLogicOpType_LogicOps();

        /**
         * The meta object literal for the '<em><b>Extension Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_LOGIC_OP_TYPE__EXTENSION_OPS_GROUP = eINSTANCE.getBinaryLogicOpType_ExtensionOpsGroup();

        /**
         * The meta object literal for the '<em><b>Extension Ops</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_LOGIC_OP_TYPE__EXTENSION_OPS = eINSTANCE.getBinaryLogicOpType_ExtensionOps();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_LOGIC_OP_TYPE__FUNCTION = eINSTANCE.getBinaryLogicOpType_Function();

        /**
         * The meta object literal for the '<em><b>Id Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_LOGIC_OP_TYPE__ID_GROUP = eINSTANCE.getBinaryLogicOpType_IdGroup();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_LOGIC_OP_TYPE__ID = eINSTANCE.getBinaryLogicOpType_Id();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.BinarySpatialOpTypeImpl <em>Binary Spatial Op Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.BinarySpatialOpTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getBinarySpatialOpType()
         * @generated
         */
        EClass BINARY_SPATIAL_OP_TYPE = eINSTANCE.getBinarySpatialOpType();

        /**
         * The meta object literal for the '<em><b>Value Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_SPATIAL_OP_TYPE__VALUE_REFERENCE = eINSTANCE.getBinarySpatialOpType_ValueReference();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP = eINSTANCE.getBinarySpatialOpType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_SPATIAL_OP_TYPE__EXPRESSION = eINSTANCE.getBinarySpatialOpType_Expression();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_SPATIAL_OP_TYPE__ANY = eINSTANCE.getBinarySpatialOpType_Any();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.BinaryTemporalOpTypeImpl <em>Binary Temporal Op Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.BinaryTemporalOpTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getBinaryTemporalOpType()
         * @generated
         */
        EClass BINARY_TEMPORAL_OP_TYPE = eINSTANCE.getBinaryTemporalOpType();

        /**
         * The meta object literal for the '<em><b>Value Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_TEMPORAL_OP_TYPE__VALUE_REFERENCE = eINSTANCE.getBinaryTemporalOpType_ValueReference();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_TEMPORAL_OP_TYPE__EXPRESSION_GROUP = eINSTANCE.getBinaryTemporalOpType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BINARY_TEMPORAL_OP_TYPE__EXPRESSION = eINSTANCE.getBinaryTemporalOpType_Expression();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BINARY_TEMPORAL_OP_TYPE__ANY = eINSTANCE.getBinaryTemporalOpType_Any();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ComparisonOperatorsTypeImpl <em>Comparison Operators Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ComparisonOperatorsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorsType()
         * @generated
         */
        EClass COMPARISON_OPERATORS_TYPE = eINSTANCE.getComparisonOperatorsType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPARISON_OPERATORS_TYPE__GROUP = eINSTANCE.getComparisonOperatorsType_Group();

        /**
         * The meta object literal for the '<em><b>Comparison Operator</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPARISON_OPERATORS_TYPE__COMPARISON_OPERATOR = eINSTANCE.getComparisonOperatorsType_ComparisonOperator();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ComparisonOperatorTypeImpl <em>Comparison Operator Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ComparisonOperatorTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorType()
         * @generated
         */
        EClass COMPARISON_OPERATOR_TYPE = eINSTANCE.getComparisonOperatorType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPARISON_OPERATOR_TYPE__NAME = eINSTANCE.getComparisonOperatorType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ComparisonOpsTypeImpl <em>Comparison Ops Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ComparisonOpsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOpsType()
         * @generated
         */
        EClass COMPARISON_OPS_TYPE = eINSTANCE.getComparisonOpsType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ConformanceTypeImpl <em>Conformance Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ConformanceTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getConformanceType()
         * @generated
         */
        EClass CONFORMANCE_TYPE = eINSTANCE.getConformanceType();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONFORMANCE_TYPE__CONSTRAINT = eINSTANCE.getConformanceType_Constraint();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.DistanceBufferTypeImpl <em>Distance Buffer Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.DistanceBufferTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getDistanceBufferType()
         * @generated
         */
        EClass DISTANCE_BUFFER_TYPE = eINSTANCE.getDistanceBufferType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP = eINSTANCE.getDistanceBufferType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISTANCE_BUFFER_TYPE__EXPRESSION = eINSTANCE.getDistanceBufferType_Expression();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DISTANCE_BUFFER_TYPE__ANY = eINSTANCE.getDistanceBufferType_Any();

        /**
         * The meta object literal for the '<em><b>Distance</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISTANCE_BUFFER_TYPE__DISTANCE = eINSTANCE.getDistanceBufferType_Distance();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.DocumentRootImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getDocumentRoot()
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
         * The meta object literal for the '<em><b>Id</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ID = eINSTANCE.getDocumentRoot_Id();

        /**
         * The meta object literal for the '<em><b>Abstract Adhoc Query Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT_ADHOC_QUERY_EXPRESSION = eINSTANCE.getDocumentRoot_AbstractAdhocQueryExpression();

        /**
         * The meta object literal for the '<em><b>Abstract Query Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT_QUERY_EXPRESSION = eINSTANCE.getDocumentRoot_AbstractQueryExpression();

        /**
         * The meta object literal for the '<em><b>Abstract Projection Clause</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT_PROJECTION_CLAUSE = eINSTANCE.getDocumentRoot_AbstractProjectionClause();

        /**
         * The meta object literal for the '<em><b>Abstract Selection Clause</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT_SELECTION_CLAUSE = eINSTANCE.getDocumentRoot_AbstractSelectionClause();

        /**
         * The meta object literal for the '<em><b>Abstract Sorting Clause</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT_SORTING_CLAUSE = eINSTANCE.getDocumentRoot_AbstractSortingClause();

        /**
         * The meta object literal for the '<em><b>After</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__AFTER = eINSTANCE.getDocumentRoot_After();

        /**
         * The meta object literal for the '<em><b>Temporal Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TEMPORAL_OPS = eINSTANCE.getDocumentRoot_TemporalOps();

        /**
         * The meta object literal for the '<em><b>And</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__AND = eINSTANCE.getDocumentRoot_And();

        /**
         * The meta object literal for the '<em><b>Logic Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__LOGIC_OPS = eINSTANCE.getDocumentRoot_LogicOps();

        /**
         * The meta object literal for the '<em><b>Any Interacts</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANY_INTERACTS = eINSTANCE.getDocumentRoot_AnyInteracts();

        /**
         * The meta object literal for the '<em><b>BBOX</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__BBOX = eINSTANCE.getDocumentRoot_BBOX();

        /**
         * The meta object literal for the '<em><b>Spatial Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SPATIAL_OPS = eINSTANCE.getDocumentRoot_SpatialOps();

        /**
         * The meta object literal for the '<em><b>Before</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__BEFORE = eINSTANCE.getDocumentRoot_Before();

        /**
         * The meta object literal for the '<em><b>Begins</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__BEGINS = eINSTANCE.getDocumentRoot_Begins();

        /**
         * The meta object literal for the '<em><b>Begun By</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__BEGUN_BY = eINSTANCE.getDocumentRoot_BegunBy();

        /**
         * The meta object literal for the '<em><b>Beyond</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__BEYOND = eINSTANCE.getDocumentRoot_Beyond();

        /**
         * The meta object literal for the '<em><b>Comparison Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COMPARISON_OPS = eINSTANCE.getDocumentRoot_ComparisonOps();

        /**
         * The meta object literal for the '<em><b>Contains</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__CONTAINS = eINSTANCE.getDocumentRoot_Contains();

        /**
         * The meta object literal for the '<em><b>Crosses</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__CROSSES = eINSTANCE.getDocumentRoot_Crosses();

        /**
         * The meta object literal for the '<em><b>Disjoint</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DISJOINT = eINSTANCE.getDocumentRoot_Disjoint();

        /**
         * The meta object literal for the '<em><b>During</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DURING = eINSTANCE.getDocumentRoot_During();

        /**
         * The meta object literal for the '<em><b>DWithin</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DWITHIN = eINSTANCE.getDocumentRoot_DWithin();

        /**
         * The meta object literal for the '<em><b>Ended By</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ENDED_BY = eINSTANCE.getDocumentRoot_EndedBy();

        /**
         * The meta object literal for the '<em><b>Ends</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ENDS = eINSTANCE.getDocumentRoot_Ends();

        /**
         * The meta object literal for the '<em><b>Equals</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EQUALS = eINSTANCE.getDocumentRoot_Equals();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXPRESSION = eINSTANCE.getDocumentRoot_Expression();

        /**
         * The meta object literal for the '<em><b>Extension Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXTENSION_OPS = eINSTANCE.getDocumentRoot_ExtensionOps();

        /**
         * The meta object literal for the '<em><b>Filter</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__FILTER = eINSTANCE.getDocumentRoot_Filter();

        /**
         * The meta object literal for the '<em><b>Filter Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__FILTER_CAPABILITIES = eINSTANCE.getDocumentRoot_FilterCapabilities();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__FUNCTION = eINSTANCE.getDocumentRoot_Function();

        /**
         * The meta object literal for the '<em><b>Intersects</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__INTERSECTS = eINSTANCE.getDocumentRoot_Intersects();

        /**
         * The meta object literal for the '<em><b>Literal</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__LITERAL = eINSTANCE.getDocumentRoot_Literal();

        /**
         * The meta object literal for the '<em><b>Logical Operators</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__LOGICAL_OPERATORS = eINSTANCE.getDocumentRoot_LogicalOperators();

        /**
         * The meta object literal for the '<em><b>Meets</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MEETS = eINSTANCE.getDocumentRoot_Meets();

        /**
         * The meta object literal for the '<em><b>Met By</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MET_BY = eINSTANCE.getDocumentRoot_MetBy();

        /**
         * The meta object literal for the '<em><b>Not</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__NOT = eINSTANCE.getDocumentRoot_Not();

        /**
         * The meta object literal for the '<em><b>Or</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OR = eINSTANCE.getDocumentRoot_Or();

        /**
         * The meta object literal for the '<em><b>Overlapped By</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OVERLAPPED_BY = eINSTANCE.getDocumentRoot_OverlappedBy();

        /**
         * The meta object literal for the '<em><b>Overlaps</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OVERLAPS = eINSTANCE.getDocumentRoot_Overlaps();

        /**
         * The meta object literal for the '<em><b>Property Is Between</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_BETWEEN = eINSTANCE.getDocumentRoot_PropertyIsBetween();

        /**
         * The meta object literal for the '<em><b>Property Is Equal To</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO = eINSTANCE.getDocumentRoot_PropertyIsEqualTo();

        /**
         * The meta object literal for the '<em><b>Property Is Greater Than</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN = eINSTANCE.getDocumentRoot_PropertyIsGreaterThan();

        /**
         * The meta object literal for the '<em><b>Property Is Greater Than Or Equal To</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO = eINSTANCE.getDocumentRoot_PropertyIsGreaterThanOrEqualTo();

        /**
         * The meta object literal for the '<em><b>Property Is Less Than</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN = eINSTANCE.getDocumentRoot_PropertyIsLessThan();

        /**
         * The meta object literal for the '<em><b>Property Is Less Than Or Equal To</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO = eINSTANCE.getDocumentRoot_PropertyIsLessThanOrEqualTo();

        /**
         * The meta object literal for the '<em><b>Property Is Like</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_LIKE = eINSTANCE.getDocumentRoot_PropertyIsLike();

        /**
         * The meta object literal for the '<em><b>Property Is Nil</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_NIL = eINSTANCE.getDocumentRoot_PropertyIsNil();

        /**
         * The meta object literal for the '<em><b>Property Is Not Equal To</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO = eINSTANCE.getDocumentRoot_PropertyIsNotEqualTo();

        /**
         * The meta object literal for the '<em><b>Property Is Null</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__PROPERTY_IS_NULL = eINSTANCE.getDocumentRoot_PropertyIsNull();

        /**
         * The meta object literal for the '<em><b>Resource Id</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__RESOURCE_ID = eINSTANCE.getDocumentRoot_ResourceId();

        /**
         * The meta object literal for the '<em><b>Sort By</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SORT_BY = eINSTANCE.getDocumentRoot_SortBy();

        /**
         * The meta object literal for the '<em><b>TContains</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TCONTAINS = eINSTANCE.getDocumentRoot_TContains();

        /**
         * The meta object literal for the '<em><b>TEquals</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TEQUALS = eINSTANCE.getDocumentRoot_TEquals();

        /**
         * The meta object literal for the '<em><b>Touches</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TOUCHES = eINSTANCE.getDocumentRoot_Touches();

        /**
         * The meta object literal for the '<em><b>TOverlaps</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TOVERLAPS = eINSTANCE.getDocumentRoot_TOverlaps();

        /**
         * The meta object literal for the '<em><b>Value Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__VALUE_REFERENCE = eINSTANCE.getDocumentRoot_ValueReference();

        /**
         * The meta object literal for the '<em><b>Within</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__WITHIN = eINSTANCE.getDocumentRoot_Within();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ExtendedCapabilitiesTypeImpl <em>Extended Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ExtendedCapabilitiesTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getExtendedCapabilitiesType()
         * @generated
         */
        EClass EXTENDED_CAPABILITIES_TYPE = eINSTANCE.getExtendedCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Additional Operators</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS = eINSTANCE.getExtendedCapabilitiesType_AdditionalOperators();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ExtensionOperatorTypeImpl <em>Extension Operator Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ExtensionOperatorTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getExtensionOperatorType()
         * @generated
         */
        EClass EXTENSION_OPERATOR_TYPE = eINSTANCE.getExtensionOperatorType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXTENSION_OPERATOR_TYPE__NAME = eINSTANCE.getExtensionOperatorType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ExtensionOpsTypeImpl <em>Extension Ops Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ExtensionOpsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getExtensionOpsType()
         * @generated
         */
        EClass EXTENSION_OPS_TYPE = eINSTANCE.getExtensionOpsType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.FilterCapabilitiesTypeImpl <em>Filter Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.FilterCapabilitiesTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getFilterCapabilitiesType()
         * @generated
         */
        EClass FILTER_CAPABILITIES_TYPE = eINSTANCE.getFilterCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Conformance</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_CAPABILITIES_TYPE__CONFORMANCE = eINSTANCE.getFilterCapabilitiesType_Conformance();

        /**
         * The meta object literal for the '<em><b>Id Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES = eINSTANCE.getFilterCapabilitiesType_IdCapabilities();

        /**
         * The meta object literal for the '<em><b>Scalar Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES = eINSTANCE.getFilterCapabilitiesType_ScalarCapabilities();

        /**
         * The meta object literal for the '<em><b>Spatial Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES = eINSTANCE.getFilterCapabilitiesType_SpatialCapabilities();

        /**
         * The meta object literal for the '<em><b>Temporal Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES = eINSTANCE.getFilterCapabilitiesType_TemporalCapabilities();

        /**
         * The meta object literal for the '<em><b>Functions</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_CAPABILITIES_TYPE__FUNCTIONS = eINSTANCE.getFilterCapabilitiesType_Functions();

        /**
         * The meta object literal for the '<em><b>Extended Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES = eINSTANCE.getFilterCapabilitiesType_ExtendedCapabilities();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.FilterTypeImpl <em>Filter Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.FilterTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getFilterType()
         * @generated
         */
        EClass FILTER_TYPE = eINSTANCE.getFilterType();

        /**
         * The meta object literal for the '<em><b>Comparison Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FILTER_TYPE__COMPARISON_OPS_GROUP = eINSTANCE.getFilterType_ComparisonOpsGroup();

        /**
         * The meta object literal for the '<em><b>Comparison Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_TYPE__COMPARISON_OPS = eINSTANCE.getFilterType_ComparisonOps();

        /**
         * The meta object literal for the '<em><b>Spatial Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FILTER_TYPE__SPATIAL_OPS_GROUP = eINSTANCE.getFilterType_SpatialOpsGroup();

        /**
         * The meta object literal for the '<em><b>Spatial Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_TYPE__SPATIAL_OPS = eINSTANCE.getFilterType_SpatialOps();

        /**
         * The meta object literal for the '<em><b>Temporal Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FILTER_TYPE__TEMPORAL_OPS_GROUP = eINSTANCE.getFilterType_TemporalOpsGroup();

        /**
         * The meta object literal for the '<em><b>Temporal Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_TYPE__TEMPORAL_OPS = eINSTANCE.getFilterType_TemporalOps();

        /**
         * The meta object literal for the '<em><b>Logic Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FILTER_TYPE__LOGIC_OPS_GROUP = eINSTANCE.getFilterType_LogicOpsGroup();

        /**
         * The meta object literal for the '<em><b>Logic Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_TYPE__LOGIC_OPS = eINSTANCE.getFilterType_LogicOps();

        /**
         * The meta object literal for the '<em><b>Extension Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FILTER_TYPE__EXTENSION_OPS_GROUP = eINSTANCE.getFilterType_ExtensionOpsGroup();

        /**
         * The meta object literal for the '<em><b>Extension Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_TYPE__EXTENSION_OPS = eINSTANCE.getFilterType_ExtensionOps();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_TYPE__FUNCTION = eINSTANCE.getFilterType_Function();

        /**
         * The meta object literal for the '<em><b>Id Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FILTER_TYPE__ID_GROUP = eINSTANCE.getFilterType_IdGroup();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FILTER_TYPE__ID = eINSTANCE.getFilterType_Id();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.FunctionTypeImpl <em>Function Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.FunctionTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getFunctionType()
         * @generated
         */
        EClass FUNCTION_TYPE = eINSTANCE.getFunctionType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FUNCTION_TYPE__EXPRESSION_GROUP = eINSTANCE.getFunctionType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FUNCTION_TYPE__EXPRESSION = eINSTANCE.getFunctionType_Expression();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FUNCTION_TYPE__NAME = eINSTANCE.getFunctionType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.GeometryOperandsTypeImpl <em>Geometry Operands Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.GeometryOperandsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getGeometryOperandsType()
         * @generated
         */
        EClass GEOMETRY_OPERANDS_TYPE = eINSTANCE.getGeometryOperandsType();

        /**
         * The meta object literal for the '<em><b>Geometry Operand</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND = eINSTANCE.getGeometryOperandsType_GeometryOperand();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.GeometryOperandTypeImpl <em>Geometry Operand Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.GeometryOperandTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getGeometryOperandType()
         * @generated
         */
        EClass GEOMETRY_OPERAND_TYPE = eINSTANCE.getGeometryOperandType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GEOMETRY_OPERAND_TYPE__NAME = eINSTANCE.getGeometryOperandType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.IdCapabilitiesTypeImpl <em>Id Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.IdCapabilitiesTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getIdCapabilitiesType()
         * @generated
         */
        EClass ID_CAPABILITIES_TYPE = eINSTANCE.getIdCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Resource Identifier</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER = eINSTANCE.getIdCapabilitiesType_ResourceIdentifier();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.LiteralTypeImpl <em>Literal Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.LiteralTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getLiteralType()
         * @generated
         */
        EClass LITERAL_TYPE = eINSTANCE.getLiteralType();

        /**
         * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LITERAL_TYPE__MIXED = eINSTANCE.getLiteralType_Mixed();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LITERAL_TYPE__ANY = eINSTANCE.getLiteralType_Any();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LITERAL_TYPE__TYPE = eINSTANCE.getLiteralType_Type();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.LogicalOperatorsTypeImpl <em>Logical Operators Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.LogicalOperatorsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getLogicalOperatorsType()
         * @generated
         */
        EClass LOGICAL_OPERATORS_TYPE = eINSTANCE.getLogicalOperatorsType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.LogicOpsTypeImpl <em>Logic Ops Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.LogicOpsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getLogicOpsType()
         * @generated
         */
        EClass LOGIC_OPS_TYPE = eINSTANCE.getLogicOpsType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.LowerBoundaryTypeImpl <em>Lower Boundary Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.LowerBoundaryTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getLowerBoundaryType()
         * @generated
         */
        EClass LOWER_BOUNDARY_TYPE = eINSTANCE.getLowerBoundaryType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute LOWER_BOUNDARY_TYPE__EXPRESSION_GROUP = eINSTANCE.getLowerBoundaryType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LOWER_BOUNDARY_TYPE__EXPRESSION = eINSTANCE.getLowerBoundaryType_Expression();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.MeasureTypeImpl <em>Measure Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.MeasureTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getMeasureType()
         * @generated
         */
        EClass MEASURE_TYPE = eINSTANCE.getMeasureType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MEASURE_TYPE__VALUE = eINSTANCE.getMeasureType_Value();

        /**
         * The meta object literal for the '<em><b>Uom</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MEASURE_TYPE__UOM = eINSTANCE.getMeasureType_Uom();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.PropertyIsBetweenTypeImpl <em>Property Is Between Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.PropertyIsBetweenTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getPropertyIsBetweenType()
         * @generated
         */
        EClass PROPERTY_IS_BETWEEN_TYPE = eINSTANCE.getPropertyIsBetweenType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY_IS_BETWEEN_TYPE__EXPRESSION_GROUP = eINSTANCE.getPropertyIsBetweenType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROPERTY_IS_BETWEEN_TYPE__EXPRESSION = eINSTANCE.getPropertyIsBetweenType_Expression();

        /**
         * The meta object literal for the '<em><b>Lower Boundary</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROPERTY_IS_BETWEEN_TYPE__LOWER_BOUNDARY = eINSTANCE.getPropertyIsBetweenType_LowerBoundary();

        /**
         * The meta object literal for the '<em><b>Upper Boundary</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROPERTY_IS_BETWEEN_TYPE__UPPER_BOUNDARY = eINSTANCE.getPropertyIsBetweenType_UpperBoundary();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.PropertyIsLikeTypeImpl <em>Property Is Like Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.PropertyIsLikeTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getPropertyIsLikeType()
         * @generated
         */
        EClass PROPERTY_IS_LIKE_TYPE = eINSTANCE.getPropertyIsLikeType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP = eINSTANCE.getPropertyIsLikeType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROPERTY_IS_LIKE_TYPE__EXPRESSION = eINSTANCE.getPropertyIsLikeType_Expression();

        /**
         * The meta object literal for the '<em><b>Escape Char</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY_IS_LIKE_TYPE__ESCAPE_CHAR = eINSTANCE.getPropertyIsLikeType_EscapeChar();

        /**
         * The meta object literal for the '<em><b>Single Char</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY_IS_LIKE_TYPE__SINGLE_CHAR = eINSTANCE.getPropertyIsLikeType_SingleChar();

        /**
         * The meta object literal for the '<em><b>Wild Card</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY_IS_LIKE_TYPE__WILD_CARD = eINSTANCE.getPropertyIsLikeType_WildCard();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.PropertyIsNilTypeImpl <em>Property Is Nil Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.PropertyIsNilTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getPropertyIsNilType()
         * @generated
         */
        EClass PROPERTY_IS_NIL_TYPE = eINSTANCE.getPropertyIsNilType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY_IS_NIL_TYPE__EXPRESSION_GROUP = eINSTANCE.getPropertyIsNilType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROPERTY_IS_NIL_TYPE__EXPRESSION = eINSTANCE.getPropertyIsNilType_Expression();

        /**
         * The meta object literal for the '<em><b>Nil Reason</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY_IS_NIL_TYPE__NIL_REASON = eINSTANCE.getPropertyIsNilType_NilReason();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.PropertyIsNullTypeImpl <em>Property Is Null Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.PropertyIsNullTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getPropertyIsNullType()
         * @generated
         */
        EClass PROPERTY_IS_NULL_TYPE = eINSTANCE.getPropertyIsNullType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY_IS_NULL_TYPE__EXPRESSION_GROUP = eINSTANCE.getPropertyIsNullType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROPERTY_IS_NULL_TYPE__EXPRESSION = eINSTANCE.getPropertyIsNullType_Expression();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ResourceIdentifierTypeImpl <em>Resource Identifier Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ResourceIdentifierTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getResourceIdentifierType()
         * @generated
         */
        EClass RESOURCE_IDENTIFIER_TYPE = eINSTANCE.getResourceIdentifierType();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESOURCE_IDENTIFIER_TYPE__METADATA = eINSTANCE.getResourceIdentifierType_Metadata();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESOURCE_IDENTIFIER_TYPE__NAME = eINSTANCE.getResourceIdentifierType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ResourceIdTypeImpl <em>Resource Id Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ResourceIdTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getResourceIdType()
         * @generated
         */
        EClass RESOURCE_ID_TYPE = eINSTANCE.getResourceIdType();

        /**
         * The meta object literal for the '<em><b>End Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESOURCE_ID_TYPE__END_DATE = eINSTANCE.getResourceIdType_EndDate();

        /**
         * The meta object literal for the '<em><b>Previous Rid</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESOURCE_ID_TYPE__PREVIOUS_RID = eINSTANCE.getResourceIdType_PreviousRid();

        /**
         * The meta object literal for the '<em><b>Rid</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESOURCE_ID_TYPE__RID = eINSTANCE.getResourceIdType_Rid();

        /**
         * The meta object literal for the '<em><b>Start Date</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESOURCE_ID_TYPE__START_DATE = eINSTANCE.getResourceIdType_StartDate();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESOURCE_ID_TYPE__VERSION = eINSTANCE.getResourceIdType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.ScalarCapabilitiesTypeImpl <em>Scalar Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.ScalarCapabilitiesTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getScalarCapabilitiesType()
         * @generated
         */
        EClass SCALAR_CAPABILITIES_TYPE = eINSTANCE.getScalarCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Logical Operators</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS = eINSTANCE.getScalarCapabilitiesType_LogicalOperators();

        /**
         * The meta object literal for the '<em><b>Comparison Operators</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS = eINSTANCE.getScalarCapabilitiesType_ComparisonOperators();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.SortByTypeImpl <em>Sort By Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.SortByTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSortByType()
         * @generated
         */
        EClass SORT_BY_TYPE = eINSTANCE.getSortByType();

        /**
         * The meta object literal for the '<em><b>Sort Property</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SORT_BY_TYPE__SORT_PROPERTY = eINSTANCE.getSortByType_SortProperty();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.SortPropertyTypeImpl <em>Sort Property Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.SortPropertyTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSortPropertyType()
         * @generated
         */
        EClass SORT_PROPERTY_TYPE = eINSTANCE.getSortPropertyType();

        /**
         * The meta object literal for the '<em><b>Value Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SORT_PROPERTY_TYPE__VALUE_REFERENCE = eINSTANCE.getSortPropertyType_ValueReference();

        /**
         * The meta object literal for the '<em><b>Sort Order</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SORT_PROPERTY_TYPE__SORT_ORDER = eINSTANCE.getSortPropertyType_SortOrder();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.SpatialCapabilitiesTypeImpl <em>Spatial Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.SpatialCapabilitiesTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialCapabilitiesType()
         * @generated
         */
        EClass SPATIAL_CAPABILITIES_TYPE = eINSTANCE.getSpatialCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Geometry Operands</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS = eINSTANCE.getSpatialCapabilitiesType_GeometryOperands();

        /**
         * The meta object literal for the '<em><b>Spatial Operators</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS = eINSTANCE.getSpatialCapabilitiesType_SpatialOperators();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.SpatialOperatorsTypeImpl <em>Spatial Operators Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.SpatialOperatorsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorsType()
         * @generated
         */
        EClass SPATIAL_OPERATORS_TYPE = eINSTANCE.getSpatialOperatorsType();

        /**
         * The meta object literal for the '<em><b>Spatial Operator</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR = eINSTANCE.getSpatialOperatorsType_SpatialOperator();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.SpatialOperatorTypeImpl <em>Spatial Operator Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.SpatialOperatorTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorType()
         * @generated
         */
        EClass SPATIAL_OPERATOR_TYPE = eINSTANCE.getSpatialOperatorType();

        /**
         * The meta object literal for the '<em><b>Geometry Operands</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS = eINSTANCE.getSpatialOperatorType_GeometryOperands();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SPATIAL_OPERATOR_TYPE__NAME = eINSTANCE.getSpatialOperatorType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.SpatialOpsTypeImpl <em>Spatial Ops Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.SpatialOpsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOpsType()
         * @generated
         */
        EClass SPATIAL_OPS_TYPE = eINSTANCE.getSpatialOpsType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.TemporalCapabilitiesTypeImpl <em>Temporal Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.TemporalCapabilitiesTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalCapabilitiesType()
         * @generated
         */
        EClass TEMPORAL_CAPABILITIES_TYPE = eINSTANCE.getTemporalCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Temporal Operands</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEMPORAL_CAPABILITIES_TYPE__TEMPORAL_OPERANDS = eINSTANCE.getTemporalCapabilitiesType_TemporalOperands();

        /**
         * The meta object literal for the '<em><b>Temporal Operators</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEMPORAL_CAPABILITIES_TYPE__TEMPORAL_OPERATORS = eINSTANCE.getTemporalCapabilitiesType_TemporalOperators();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.TemporalOperandsTypeImpl <em>Temporal Operands Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.TemporalOperandsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperandsType()
         * @generated
         */
        EClass TEMPORAL_OPERANDS_TYPE = eINSTANCE.getTemporalOperandsType();

        /**
         * The meta object literal for the '<em><b>Temporal Operand</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEMPORAL_OPERANDS_TYPE__TEMPORAL_OPERAND = eINSTANCE.getTemporalOperandsType_TemporalOperand();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.TemporalOperandTypeImpl <em>Temporal Operand Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.TemporalOperandTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperandType()
         * @generated
         */
        EClass TEMPORAL_OPERAND_TYPE = eINSTANCE.getTemporalOperandType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEMPORAL_OPERAND_TYPE__NAME = eINSTANCE.getTemporalOperandType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.TemporalOperatorsTypeImpl <em>Temporal Operators Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.TemporalOperatorsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorsType()
         * @generated
         */
        EClass TEMPORAL_OPERATORS_TYPE = eINSTANCE.getTemporalOperatorsType();

        /**
         * The meta object literal for the '<em><b>Temporal Operator</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR = eINSTANCE.getTemporalOperatorsType_TemporalOperator();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.TemporalOperatorTypeImpl <em>Temporal Operator Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.TemporalOperatorTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorType()
         * @generated
         */
        EClass TEMPORAL_OPERATOR_TYPE = eINSTANCE.getTemporalOperatorType();

        /**
         * The meta object literal for the '<em><b>Temporal Operands</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TEMPORAL_OPERATOR_TYPE__TEMPORAL_OPERANDS = eINSTANCE.getTemporalOperatorType_TemporalOperands();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEMPORAL_OPERATOR_TYPE__NAME = eINSTANCE.getTemporalOperatorType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.TemporalOpsTypeImpl <em>Temporal Ops Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.TemporalOpsTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOpsType()
         * @generated
         */
        EClass TEMPORAL_OPS_TYPE = eINSTANCE.getTemporalOpsType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.UnaryLogicOpTypeImpl <em>Unary Logic Op Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.UnaryLogicOpTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getUnaryLogicOpType()
         * @generated
         */
        EClass UNARY_LOGIC_OP_TYPE = eINSTANCE.getUnaryLogicOpType();

        /**
         * The meta object literal for the '<em><b>Comparison Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNARY_LOGIC_OP_TYPE__COMPARISON_OPS_GROUP = eINSTANCE.getUnaryLogicOpType_ComparisonOpsGroup();

        /**
         * The meta object literal for the '<em><b>Comparison Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_LOGIC_OP_TYPE__COMPARISON_OPS = eINSTANCE.getUnaryLogicOpType_ComparisonOps();

        /**
         * The meta object literal for the '<em><b>Spatial Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNARY_LOGIC_OP_TYPE__SPATIAL_OPS_GROUP = eINSTANCE.getUnaryLogicOpType_SpatialOpsGroup();

        /**
         * The meta object literal for the '<em><b>Spatial Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_LOGIC_OP_TYPE__SPATIAL_OPS = eINSTANCE.getUnaryLogicOpType_SpatialOps();

        /**
         * The meta object literal for the '<em><b>Temporal Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNARY_LOGIC_OP_TYPE__TEMPORAL_OPS_GROUP = eINSTANCE.getUnaryLogicOpType_TemporalOpsGroup();

        /**
         * The meta object literal for the '<em><b>Temporal Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_LOGIC_OP_TYPE__TEMPORAL_OPS = eINSTANCE.getUnaryLogicOpType_TemporalOps();

        /**
         * The meta object literal for the '<em><b>Logic Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNARY_LOGIC_OP_TYPE__LOGIC_OPS_GROUP = eINSTANCE.getUnaryLogicOpType_LogicOpsGroup();

        /**
         * The meta object literal for the '<em><b>Logic Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_LOGIC_OP_TYPE__LOGIC_OPS = eINSTANCE.getUnaryLogicOpType_LogicOps();

        /**
         * The meta object literal for the '<em><b>Extension Ops Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNARY_LOGIC_OP_TYPE__EXTENSION_OPS_GROUP = eINSTANCE.getUnaryLogicOpType_ExtensionOpsGroup();

        /**
         * The meta object literal for the '<em><b>Extension Ops</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_LOGIC_OP_TYPE__EXTENSION_OPS = eINSTANCE.getUnaryLogicOpType_ExtensionOps();

        /**
         * The meta object literal for the '<em><b>Function</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_LOGIC_OP_TYPE__FUNCTION = eINSTANCE.getUnaryLogicOpType_Function();

        /**
         * The meta object literal for the '<em><b>Id Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UNARY_LOGIC_OP_TYPE__ID_GROUP = eINSTANCE.getUnaryLogicOpType_IdGroup();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UNARY_LOGIC_OP_TYPE__ID = eINSTANCE.getUnaryLogicOpType_Id();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.impl.UpperBoundaryTypeImpl <em>Upper Boundary Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.impl.UpperBoundaryTypeImpl
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getUpperBoundaryType()
         * @generated
         */
        EClass UPPER_BOUNDARY_TYPE = eINSTANCE.getUpperBoundaryType();

        /**
         * The meta object literal for the '<em><b>Expression Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UPPER_BOUNDARY_TYPE__EXPRESSION_GROUP = eINSTANCE.getUpperBoundaryType_ExpressionGroup();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UPPER_BOUNDARY_TYPE__EXPRESSION = eINSTANCE.getUpperBoundaryType_Expression();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.ComparisonOperatorNameTypeMember0 <em>Comparison Operator Name Type Member0</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.ComparisonOperatorNameTypeMember0
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorNameTypeMember0()
         * @generated
         */
        EEnum COMPARISON_OPERATOR_NAME_TYPE_MEMBER0 = eINSTANCE.getComparisonOperatorNameTypeMember0();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.MatchActionType <em>Match Action Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.MatchActionType
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getMatchActionType()
         * @generated
         */
        EEnum MATCH_ACTION_TYPE = eINSTANCE.getMatchActionType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.SortOrderType <em>Sort Order Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.SortOrderType
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSortOrderType()
         * @generated
         */
        EEnum SORT_ORDER_TYPE = eINSTANCE.getSortOrderType();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.SpatialOperatorNameTypeMember0 <em>Spatial Operator Name Type Member0</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.SpatialOperatorNameTypeMember0
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorNameTypeMember0()
         * @generated
         */
        EEnum SPATIAL_OPERATOR_NAME_TYPE_MEMBER0 = eINSTANCE.getSpatialOperatorNameTypeMember0();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.TemporalOperatorNameTypeMember0 <em>Temporal Operator Name Type Member0</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.TemporalOperatorNameTypeMember0
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorNameTypeMember0()
         * @generated
         */
        EEnum TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0 = eINSTANCE.getTemporalOperatorNameTypeMember0();

        /**
         * The meta object literal for the '{@link net.opengis.fes20.VersionActionTokens <em>Version Action Tokens</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.VersionActionTokens
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getVersionActionTokens()
         * @generated
         */
        EEnum VERSION_ACTION_TOKENS = eINSTANCE.getVersionActionTokens();

        /**
         * The meta object literal for the '<em>Aliases Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.List
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getAliasesType()
         * @generated
         */
        EDataType ALIASES_TYPE = eINSTANCE.getAliasesType();

        /**
         * The meta object literal for the '<em>Comparison Operator Name Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.Object
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorNameType()
         * @generated
         */
        EDataType COMPARISON_OPERATOR_NAME_TYPE = eINSTANCE.getComparisonOperatorNameType();

        /**
         * The meta object literal for the '<em>Comparison Operator Name Type Member0 Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.ComparisonOperatorNameTypeMember0
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorNameTypeMember0Object()
         * @generated
         */
        EDataType COMPARISON_OPERATOR_NAME_TYPE_MEMBER0_OBJECT = eINSTANCE.getComparisonOperatorNameTypeMember0Object();

        /**
         * The meta object literal for the '<em>Comparison Operator Name Type Member1</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getComparisonOperatorNameTypeMember1()
         * @generated
         */
        EDataType COMPARISON_OPERATOR_NAME_TYPE_MEMBER1 = eINSTANCE.getComparisonOperatorNameTypeMember1();

        /**
         * The meta object literal for the '<em>Match Action Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.MatchActionType
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getMatchActionTypeObject()
         * @generated
         */
        EDataType MATCH_ACTION_TYPE_OBJECT = eINSTANCE.getMatchActionTypeObject();

        /**
         * The meta object literal for the '<em>Schema Element</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSchemaElement()
         * @generated
         */
        EDataType SCHEMA_ELEMENT = eINSTANCE.getSchemaElement();

        /**
         * The meta object literal for the '<em>Sort Order Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.SortOrderType
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSortOrderTypeObject()
         * @generated
         */
        EDataType SORT_ORDER_TYPE_OBJECT = eINSTANCE.getSortOrderTypeObject();

        /**
         * The meta object literal for the '<em>Spatial Operator Name Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.Object
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorNameType()
         * @generated
         */
        EDataType SPATIAL_OPERATOR_NAME_TYPE = eINSTANCE.getSpatialOperatorNameType();

        /**
         * The meta object literal for the '<em>Spatial Operator Name Type Member0 Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.SpatialOperatorNameTypeMember0
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorNameTypeMember0Object()
         * @generated
         */
        EDataType SPATIAL_OPERATOR_NAME_TYPE_MEMBER0_OBJECT = eINSTANCE.getSpatialOperatorNameTypeMember0Object();

        /**
         * The meta object literal for the '<em>Spatial Operator Name Type Member1</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getSpatialOperatorNameTypeMember1()
         * @generated
         */
        EDataType SPATIAL_OPERATOR_NAME_TYPE_MEMBER1 = eINSTANCE.getSpatialOperatorNameTypeMember1();

        /**
         * The meta object literal for the '<em>Temporal Operator Name Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.Object
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorNameType()
         * @generated
         */
        EDataType TEMPORAL_OPERATOR_NAME_TYPE = eINSTANCE.getTemporalOperatorNameType();

        /**
         * The meta object literal for the '<em>Temporal Operator Name Type Member0 Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.TemporalOperatorNameTypeMember0
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorNameTypeMember0Object()
         * @generated
         */
        EDataType TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0_OBJECT = eINSTANCE.getTemporalOperatorNameTypeMember0Object();

        /**
         * The meta object literal for the '<em>Temporal Operator Name Type Member1</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTemporalOperatorNameTypeMember1()
         * @generated
         */
        EDataType TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1 = eINSTANCE.getTemporalOperatorNameTypeMember1();

        /**
         * The meta object literal for the '<em>Type Names List Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.List
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTypeNamesListType()
         * @generated
         */
        EDataType TYPE_NAMES_LIST_TYPE = eINSTANCE.getTypeNamesListType();

        /**
         * The meta object literal for the '<em>Type Names Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.Object
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getTypeNamesType()
         * @generated
         */
        EDataType TYPE_NAMES_TYPE = eINSTANCE.getTypeNamesType();

        /**
         * The meta object literal for the '<em>Uom Identifier</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getUomIdentifier()
         * @generated
         */
        EDataType UOM_IDENTIFIER = eINSTANCE.getUomIdentifier();

        /**
         * The meta object literal for the '<em>Uom Symbol</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getUomSymbol()
         * @generated
         */
        EDataType UOM_SYMBOL = eINSTANCE.getUomSymbol();

        /**
         * The meta object literal for the '<em>Uom URI</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getUomURI()
         * @generated
         */
        EDataType UOM_URI = eINSTANCE.getUomURI();

        /**
         * The meta object literal for the '<em>Version Action Tokens Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.fes20.VersionActionTokens
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getVersionActionTokensObject()
         * @generated
         */
        EDataType VERSION_ACTION_TOKENS_OBJECT = eINSTANCE.getVersionActionTokensObject();

        /**
         * The meta object literal for the '<em>Version Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.Object
         * @see net.opengis.fes20.impl.Fes20PackageImpl#getVersionType()
         * @generated
         */
        EDataType VERSION_TYPE = eINSTANCE.getVersionType();

    }

} //Fes20Package
