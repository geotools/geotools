/**
 */
package net.opengis.wps20;

import net.opengis.ows20.Ows20Package;

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
 * 			WPS is an OGC Standard.
 * 			Copyright (c) 2015 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * 
 * 			WPS is an OGC Standard.
 * 			Copyright (c) 2015 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * 
 * 			WPS is an OGC Standard.
 * 			Copyright (c) 2015 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 		
 * This XML Schema Document includes and imports, directly or
 *       indirectly, all the XML Schemas defined by the OWS Common Implemetation
 *       Specification.
 *       
 *       OWS is an OGC Standard.
 *       Copyright (c) 2009 Open Geospatial Consortium.
 *       To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes the GetResourceByID
 *     operation request message. This typical operation is specified as a base
 *     for profiling in specific OWS specifications. For information on the
 *     allowed changes and limitations in such profiling, see Subclause 9.4.1 of
 *     the OWS Common specification.
 *     
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes the Exception Report
 *     response to all OWS operations.
 * 	
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes the allowed values (or
 *     domain) of a quantity, often for an input or output parameter to an OWS.
 *     Such a parameter is sometimes called a variable, quantity, literal, or
 *     typed literal. Such a parameter can use one of many data types, including
 *     double, integer, boolean, string, or URI. The allowed values can also be
 *     encoded for a quantity that is not explicit or not transferred, but is
 *     constrained by a server implementation.
 *     
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/
 *     
 * This XML Schema Document encodes the typical Contents
 *     section of an OWS service metadata (Capabilities) document. This Schema
 *     can be built upon to define the Contents section for a specific OWS. If
 *     the ContentsBaseType in this XML Schema cannot be restricted and extended
 *     to define the Contents section for a specific OWS, all other relevant
 *     parts defined in owsContents.xsd shall be used by the "ContentsType" in
 *     the wxsContents.xsd prepared for the specific OWS.
 *     
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/
 *     
 * This XML Schema Document specifies types and elements for
 *     input and output of operation data, allowing including multiple data items
 *     with each data item either included or referenced. The contents of each
 *     type and element specified here can be restricted and/or extended for each
 *     use in a specific OWS specification.
 * 		
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes a new AdditionalParameters
 *     element that contains one or more AdditionalParameter elements, which each
 *     contain a specific parameter name and one or more values of that parameter.
 *     This AdditionalParameters element is substitutable for ows:Metadata,
 *     anywhere that element is allowed. The document also encodes a new nilValue
 *     element of a newly defined NilValue type that allows the specification of
 *     a nilReason attribute.
 * 
 *    OWS is an OGC Standard.
 *    Copyright (c) 2009 Open Geospatial Consortium.
 *    To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes various parameters and
 *     parameter types that can be used in OWS operation requests and responses.
 *     
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/
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
 *      
 *   <div xmlns="http://www.w3.org/1999/xhtml">
 *         
 *     <h1>About the XML namespace</h1>
 *         
 *     <div class="bodytext">
 *            
 *       <p>
 *       This schema document describes the XML namespace, in a form
 *       suitable for import by other schema documents.
 *      </p>
 *            
 *       <p>
 *               See 
 *         <a href="http://www.w3.org/XML/1998/namespace.html">
 *       http://www.w3.org/XML/1998/namespace.html</a>
 *          and
 *       
 *         <a href="http://www.w3.org/TR/REC-xml">
 *       http://www.w3.org/TR/REC-xml</a>
 *          for information 
 *       about this namespace.
 *      
 *       </p>
 *            
 *       <p>
 *       Note that local names in this namespace are intended to be
 *       defined only by the World Wide Web Consortium or its subgroups.
 *       The names currently defined in this namespace are listed below.
 *       They should not be used with conflicting semantics by any Working
 *       Group, specification, or document instance.
 *      </p>
 *            
 *       <p>
 *            
 *       See further below in this document for more information about 
 *         <a href="#usage">how to refer to this schema document from your own
 *       XSD schema documents</a>
 *          and about 
 *         <a href="#nsversioning">the
 *       namespace-versioning policy governing this schema document</a>
 *         .
 *      
 *       </p>
 *           
 *     </div>
 *        
 *   </div>
 *     
 * 
 * 
 *      
 *   <div xmlns="http://www.w3.org/1999/xhtml">
 *        
 *     
 *     <h3>Father (in any context at all)</h3>
 *      
 * 
 *     
 *     <div class="bodytext">
 *            
 *       <p>
 *       denotes Jon Bosak, the chair of 
 *       the original XML Working Group.  This name is reserved by 
 *       the following decision of the W3C XML Plenary and 
 *       XML Coordination groups:
 *      </p>
 *            
 *       <blockquote>
 *                
 *         <p>
 * 	In appreciation for his vision, leadership and
 * 	dedication the W3C XML Plenary on this 10th day of
 * 	February, 2000, reserves for Jon Bosak in perpetuity
 * 	the XML name "xml:Father".
 *        </p>
 *              
 *       </blockquote>
 *           
 *     </div>
 *        
 *   </div>
 *     
 * 
 * 
 *      
 *   <div id="usage" xml:id="usage" xmlns="http://www.w3.org/1999/xhtml">
 *         
 *     <h2>
 *       <a name="usage">About this schema document</a>
 *     </h2>
 *         
 *     <div class="bodytext">
 *            
 *       <p>
 *               This schema defines attributes and an attribute group suitable
 *       for use by schemas wishing to allow 
 *         <code>xml:base</code>
 *         ,
 *       
 *         <code>xml:lang</code>
 *         , 
 *         <code>xml:space</code>
 *          or
 *       
 *         <code>xml:id</code>
 *          attributes on elements they define.
 *      
 *       </p>
 *            
 *       <p>
 *       To enable this, such a schema must import this schema for
 *       the XML namespace, e.g. as follows:
 *      </p>
 *            
 *       <pre>
 *           &lt;schema . . .&gt;
 *            . . .
 *            &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                       schemaLocation="http://www.w3.org/2001/xml.xsd"/&gt;
 *      </pre>
 *            
 *       <p>
 *       or
 *      </p>
 *            
 *       <pre>
 *            &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                       schemaLocation="http://www.w3.org/2009/01/xml.xsd"/&gt;
 *      </pre>
 *            
 *       <p>
 *       Subsequently, qualified reference to any of the attributes or the
 *       group defined below will have the desired effect, e.g.
 *      </p>
 *            
 *       <pre>
 *           &lt;type . . .&gt;
 *            . . .
 *            &lt;attributeGroup ref="xml:specialAttrs"/&gt;
 *      </pre>
 *            
 *       <p>
 *       will define a type which will schema-validate an instance element
 *       with any of those attributes.
 *      </p>
 *           
 *     </div>
 *        
 *   </div>
 *     
 * 
 * 
 *      
 *   <div id="nsversioning" xml:id="nsversioning" xmlns="http://www.w3.org/1999/xhtml">
 *         
 *     <h2>
 *       <a name="nsversioning">Versioning policy for this schema document</a>
 *     </h2>
 *         
 *     <div class="bodytext">
 *            
 *       <p>
 *               In keeping with the XML Schema WG's standard versioning
 *       policy, this schema document will persist at
 *       
 *         <a href="http://www.w3.org/2009/01/xml.xsd">
 *        http://www.w3.org/2009/01/xml.xsd</a>
 *         .
 *      
 *       </p>
 *            
 *       <p>
 *               At the date of issue it can also be found at
 *       
 *         <a href="http://www.w3.org/2001/xml.xsd">
 *        http://www.w3.org/2001/xml.xsd</a>
 *         .
 *      
 *       </p>
 *            
 *       <p>
 *               The schema document at that URI may however change in the future,
 *       in order to remain compatible with the latest version of XML
 *       Schema itself, or with the XML namespace itself.  In other words,
 *       if the XML Schema or XML namespaces change, the version of this
 *       document at 
 *         <a href="http://www.w3.org/2001/xml.xsd">
 *        http://www.w3.org/2001/xml.xsd 
 *       </a>
 *          
 *       will change accordingly; the version at 
 *       
 *         <a href="http://www.w3.org/2009/01/xml.xsd">
 *        http://www.w3.org/2009/01/xml.xsd 
 *       </a>
 *          
 *       will not change.
 *      
 *       </p>
 *            
 *       <p>
 *       Previous dated (and unchanging) versions of this schema 
 *       document are at:
 *      </p>
 *            
 *       <ul>
 *               
 *         <li>
 *           <a href="http://www.w3.org/2009/01/xml.xsd">
 * 	http://www.w3.org/2009/01/xml.xsd</a>
 *         </li>
 *               
 *         <li>
 *           <a href="http://www.w3.org/2007/08/xml.xsd">
 * 	http://www.w3.org/2007/08/xml.xsd</a>
 *         </li>
 *               
 *         <li>
 *           <a href="http://www.w3.org/2004/10/xml.xsd">
 * 	http://www.w3.org/2004/10/xml.xsd</a>
 *         </li>
 *               
 *         <li>
 *           <a href="http://www.w3.org/2001/03/xml.xsd">
 * 	http://www.w3.org/2001/03/xml.xsd</a>
 *         </li>
 *              
 *       </ul>
 *           
 *     </div>
 *        
 *   </div>
 *     
 * 
 * This XML Schema Document encodes the parts of ISO 19115 used
 *     by the common "ServiceIdentification" and "ServiceProvider" sections of the
 *     GetCapabilities operation response, known as the service metadata XML
 *     document. The parts encoded here are the MD_Keywords, CI_ResponsibleParty,
 *     and related classes. The UML package prefixes were omitted from XML names,
 *     and the XML element names were all capitalized, for consistency with other
 *     OWS Schemas. This document also provides a simple coding of text in
 *     multiple languages, simplified from Annex J of ISO 19115.
 * 		
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document specifies types and elements for
 *     document or resource references and for package manifests that contain
 *     multiple references. The contents of each type and element specified here
 *     can be restricted and/or extended for each use in a specific OWS
 *     specification.
 * 
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes the parts of the
 *     MD_DataIdentification class of ISO 19115 (OGC Abstract Specification Topic
 *     11) which are expected to be used for most datasets. This Schema also
 *     encodes the parts of this class that are expected to be useful for other
 *     metadata. Both may be used within the Contents section of OWS service
 *     metadata (Capabilities) documents.
 *     
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/
 *     
 * This XML Schema Document defines the GetCapabilities
 *     operation request and response XML elements and types, which are common to
 *     all OWSs. This XML Schema shall be edited by each OWS, for example, to
 *     specify a specific value for the "service" attribute.
 *     
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes the common
 *     "ServiceIdentification" section of the GetCapabilities operation response,
 *     known as the Capabilities XML document. This section encodes the
 *     SV_ServiceIdentification class of ISO 19119 (OGC Abstract Specification
 *     Topic 12). 
 * 
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes the common
 *     "ServiceProvider" section of the GetCapabilities operation response, known
 *     as the Capabilities XML document. This section encodes the
 *     SV_ServiceProvider class of ISO 19119 (OGC Abstract Specification Topic 12). 
 * 
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * This XML Schema Document encodes the basic contents of the
 *     "OperationsMetadata" section of the GetCapabilities operation response,
 *     also known as the Capabilities XML document.
 * 
 *     OWS is an OGC Standard.
 *     Copyright (c) 2009 Open Geospatial Consortium.
 *     To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 *     
 * 
 *     Part 1 version: Id: structures.xsd,v 1.2 2004/01/15 11:34:25 ht Exp 
 *     Part 2 version: Id: datatypes.xsd,v 1.3 2004/01/23 18:11:13 ht Exp 
 *   
 * 
 *    The schema corresponding to this document is normative,
 *    with respect to the syntactic constraints it expresses in the
 *    XML Schema language.  The documentation (within <documentation> elements)
 *    below, is not normative, but rather highlights important aspects of
 *    the W3C Recommendation of which this is a part
 * 
 *    The simpleType element and all of its members are defined
 *       towards the end of this schema document
 * 
 *    simple type for the value of the 'namespace' attr of
 *    'any' and 'anyAttribute'
 * 
 *    Value is
 *               ##any      - - any non-conflicting WFXML/attribute at all
 * 
 *               ##other    - - any non-conflicting WFXML/attribute from
 *                               namespace other than targetNS
 * 
 *               ##local    - - any unqualified non-conflicting WFXML/attribute 
 * 
 *               one or     - - any non-conflicting WFXML/attribute from
 *               more URI        the listed namespaces
 *               references
 *               (space separated)
 * 
 *     ##targetNamespace or ##local may appear in the above list, to
 *         refer to the targetNamespace of the enclosing
 *         schema or an absent targetNamespace respectively
 * 
 *    notations for use within XML Schema schemas
 * 
 *       First the built-in primitive datatypes.  These definitions are for
 *       information only, the real built-in definitions are magic.
 *     
 * 
 *       For each built-in datatype in this schema (both primitive and
 *       derived) can be uniquely addressed via a URI constructed
 *       as follows:
 *         1) the base URI is the URI of the XML Schema namespace
 *         2) the fragment identifier is the name of the datatype
 * 
 *       For example, to address the int datatype, the URI is:
 * 
 *         http://www.w3.org/2001/XMLSchema#int
 * 
 *       Additionally, each facet definition element can be uniquely
 *       addressed via a URI constructed as follows:
 *         1) the base URI is the URI of the XML Schema namespace
 *         2) the fragment identifier is the name of the facet
 * 
 *       For example, to address the maxInclusive facet, the URI is:
 * 
 *         http://www.w3.org/2001/XMLSchema#maxInclusive
 * 
 *       Additionally, each facet usage in a built-in datatype definition
 *       can be uniquely addressed via a URI constructed as follows:
 *         1) the base URI is the URI of the XML Schema namespace
 *         2) the fragment identifier is the name of the datatype, followed
 *            by a period (".") followed by the name of the facet
 * 
 *       For example, to address the usage of the maxInclusive facet in
 *       the definition of int, the URI is:
 * 
 *         http://www.w3.org/2001/XMLSchema#int.maxInclusive
 * 
 *     
 * 
 *       Now the derived primitive types
 *     
 * <!-- end-model-doc -->
 * @see net.opengis.wps20.Wps20Factory
 * @model kind="package"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface Wps20Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "wps20";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.opengis.net/wps/2.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "wps20";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Wps20Package eINSTANCE = net.opengis.wps20.impl.Wps20PackageImpl.init();

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.BodyReferenceTypeImpl <em>Body Reference Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.BodyReferenceTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getBodyReferenceType()
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
	 * The meta object id for the '{@link net.opengis.wps20.impl.DataDescriptionTypeImpl <em>Data Description Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.DataDescriptionTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataDescriptionType()
	 * @generated
	 */
	int DATA_DESCRIPTION_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Format</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_DESCRIPTION_TYPE__FORMAT = 0;

	/**
	 * The number of structural features of the '<em>Data Description Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_DESCRIPTION_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.BoundingBoxDataTypeImpl <em>Bounding Box Data Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.BoundingBoxDataTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getBoundingBoxDataType()
	 * @generated
	 */
	int BOUNDING_BOX_DATA_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Format</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDING_BOX_DATA_TYPE__FORMAT = DATA_DESCRIPTION_TYPE__FORMAT;

	/**
	 * The feature id for the '<em><b>Supported CRS</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS = DATA_DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Bounding Box Data Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDING_BOX_DATA_TYPE_FEATURE_COUNT = DATA_DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ComplexDataTypeImpl <em>Complex Data Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ComplexDataTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getComplexDataType()
	 * @generated
	 */
	int COMPLEX_DATA_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Format</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_DATA_TYPE__FORMAT = DATA_DESCRIPTION_TYPE__FORMAT;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_DATA_TYPE__ANY = DATA_DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Complex Data Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPLEX_DATA_TYPE_FEATURE_COUNT = DATA_DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ContentsTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getContentsType()
	 * @generated
	 */
	int CONTENTS_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Process Summary</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENTS_TYPE__PROCESS_SUMMARY = 0;

	/**
	 * The number of structural features of the '<em>Contents Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTENTS_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.DataInputTypeImpl <em>Data Input Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.DataInputTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataInputType()
	 * @generated
	 */
	int DATA_INPUT_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT_TYPE__DATA = 0;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT_TYPE__REFERENCE = 1;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT_TYPE__INPUT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT_TYPE__ID = 3;

	/**
	 * The number of structural features of the '<em>Data Input Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.DataOutputTypeImpl <em>Data Output Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.DataOutputTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataOutputType()
	 * @generated
	 */
	int DATA_OUTPUT_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_OUTPUT_TYPE__DATA = 0;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_OUTPUT_TYPE__REFERENCE = 1;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_OUTPUT_TYPE__OUTPUT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_OUTPUT_TYPE__ID = 3;

	/**
	 * The number of structural features of the '<em>Data Output Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_OUTPUT_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.DataTypeImpl <em>Data Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.DataTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataType()
	 * @generated
	 */
	int DATA_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE__MIXED = XMLTypePackage.ANY_TYPE__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE__ANY = XMLTypePackage.ANY_TYPE__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE__ANY_ATTRIBUTE = XMLTypePackage.ANY_TYPE__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE__ENCODING = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Mime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE__MIME_TYPE = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE__SCHEMA = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Data Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_FEATURE_COUNT = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.RequestBaseTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getRequestBaseType()
	 * @generated
	 */
	int REQUEST_BASE_TYPE = 33;

	/**
	 * The feature id for the '<em><b>Extension</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_BASE_TYPE__EXTENSION = 0;

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
	 * The number of structural features of the '<em>Request Base Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_BASE_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.DescribeProcessTypeImpl <em>Describe Process Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.DescribeProcessTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDescribeProcessType()
	 * @generated
	 */
	int DESCRIBE_PROCESS_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Extension</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_PROCESS_TYPE__EXTENSION = REQUEST_BASE_TYPE__EXTENSION;

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
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_PROCESS_TYPE__IDENTIFIER = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Lang</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_PROCESS_TYPE__LANG = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Describe Process Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_PROCESS_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.DescriptionTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDescriptionType()
	 * @generated
	 */
	int DESCRIPTION_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIPTION_TYPE__TITLE = Ows20Package.BASIC_IDENTIFICATION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIPTION_TYPE__ABSTRACT = Ows20Package.BASIC_IDENTIFICATION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIPTION_TYPE__KEYWORDS = Ows20Package.BASIC_IDENTIFICATION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIPTION_TYPE__IDENTIFIER = Ows20Package.BASIC_IDENTIFICATION_TYPE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIPTION_TYPE__METADATA_GROUP = Ows20Package.BASIC_IDENTIFICATION_TYPE__METADATA_GROUP;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIPTION_TYPE__METADATA = Ows20Package.BASIC_IDENTIFICATION_TYPE__METADATA;

	/**
	 * The number of structural features of the '<em>Description Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIPTION_TYPE_FEATURE_COUNT = Ows20Package.BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.DismissTypeImpl <em>Dismiss Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.DismissTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDismissType()
	 * @generated
	 */
	int DISMISS_TYPE = 10;

	/**
	 * The feature id for the '<em><b>Extension</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISMISS_TYPE__EXTENSION = REQUEST_BASE_TYPE__EXTENSION;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISMISS_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISMISS_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Job ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISMISS_TYPE__JOB_ID = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Dismiss Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISMISS_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.DocumentRootImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDocumentRoot()
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
	 * The feature id for the '<em><b>Bounding Box Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__BOUNDING_BOX_DATA = 3;

	/**
	 * The feature id for the '<em><b>Data Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DATA_DESCRIPTION = 4;

	/**
	 * The feature id for the '<em><b>Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__CAPABILITIES = 5;

	/**
	 * The feature id for the '<em><b>Complex Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__COMPLEX_DATA = 6;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__CONTENTS = 7;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DATA = 8;

	/**
	 * The feature id for the '<em><b>Describe Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DESCRIBE_PROCESS = 9;

	/**
	 * The feature id for the '<em><b>Dismiss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DISMISS = 10;

	/**
	 * The feature id for the '<em><b>Execute</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__EXECUTE = 11;

	/**
	 * The feature id for the '<em><b>Expiration Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__EXPIRATION_DATE = 12;

	/**
	 * The feature id for the '<em><b>Format</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__FORMAT = 13;

	/**
	 * The feature id for the '<em><b>Generic Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GENERIC_PROCESS = 14;

	/**
	 * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_CAPABILITIES = 15;

	/**
	 * The feature id for the '<em><b>Get Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_RESULT = 16;

	/**
	 * The feature id for the '<em><b>Get Status</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_STATUS = 17;

	/**
	 * The feature id for the '<em><b>Job ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__JOB_ID = 18;

	/**
	 * The feature id for the '<em><b>Literal Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__LITERAL_DATA = 19;

	/**
	 * The feature id for the '<em><b>Literal Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__LITERAL_VALUE = 20;

	/**
	 * The feature id for the '<em><b>Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROCESS = 21;

	/**
	 * The feature id for the '<em><b>Process Offering</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROCESS_OFFERING = 22;

	/**
	 * The feature id for the '<em><b>Process Offerings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROCESS_OFFERINGS = 23;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__REFERENCE = 24;

	/**
	 * The feature id for the '<em><b>Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__RESULT = 25;

	/**
	 * The feature id for the '<em><b>Status Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__STATUS_INFO = 26;

	/**
	 * The feature id for the '<em><b>Supported CRS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__SUPPORTED_CRS = 27;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 28;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ExecuteRequestTypeImpl <em>Execute Request Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ExecuteRequestTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getExecuteRequestType()
	 * @generated
	 */
	int EXECUTE_REQUEST_TYPE = 12;

	/**
	 * The feature id for the '<em><b>Extension</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE__EXTENSION = REQUEST_BASE_TYPE__EXTENSION;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE__IDENTIFIER = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE__INPUT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE__OUTPUT = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE__MODE = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Response</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE__RESPONSE = REQUEST_BASE_TYPE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Execute Request Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTE_REQUEST_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ExtensionTypeImpl <em>Extension Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ExtensionTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getExtensionType()
	 * @generated
	 */
	int EXTENSION_TYPE = 13;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTENSION_TYPE__ANY = 0;

	/**
	 * The number of structural features of the '<em>Extension Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTENSION_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.FormatTypeImpl <em>Format Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.FormatTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getFormatType()
	 * @generated
	 */
	int FORMAT_TYPE = 14;

	/**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORMAT_TYPE__DEFAULT = 0;

	/**
	 * The feature id for the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORMAT_TYPE__ENCODING = 1;

	/**
	 * The feature id for the '<em><b>Maximum Megabytes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORMAT_TYPE__MAXIMUM_MEGABYTES = 2;

	/**
	 * The feature id for the '<em><b>Mime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORMAT_TYPE__MIME_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORMAT_TYPE__SCHEMA = 4;

	/**
	 * The number of structural features of the '<em>Format Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORMAT_TYPE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.GenericInputTypeImpl <em>Generic Input Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.GenericInputTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGenericInputType()
	 * @generated
	 */
	int GENERIC_INPUT_TYPE = 15;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__METADATA_GROUP = DESCRIPTION_TYPE__METADATA_GROUP;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__INPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Max Occurs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__MAX_OCCURS = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Min Occurs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE__MIN_OCCURS = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Generic Input Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_INPUT_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.GenericOutputTypeImpl <em>Generic Output Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.GenericOutputTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGenericOutputType()
	 * @generated
	 */
	int GENERIC_OUTPUT_TYPE = 16;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_OUTPUT_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_OUTPUT_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_OUTPUT_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_OUTPUT_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_OUTPUT_TYPE__METADATA_GROUP = DESCRIPTION_TYPE__METADATA_GROUP;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_OUTPUT_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_OUTPUT_TYPE__OUTPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Generic Output Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_OUTPUT_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.GenericProcessTypeImpl <em>Generic Process Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.GenericProcessTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGenericProcessType()
	 * @generated
	 */
	int GENERIC_PROCESS_TYPE = 17;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE__METADATA_GROUP = DESCRIPTION_TYPE__METADATA_GROUP;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE__INPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE__OUTPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Generic Process Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_PROCESS_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.GetCapabilitiesTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGetCapabilitiesType()
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
	int GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS;

	/**
	 * The feature id for the '<em><b>Sections</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__SECTIONS = Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS;

	/**
	 * The feature id for the '<em><b>Accept Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS;

	/**
	 * The feature id for the '<em><b>Accept Languages</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES = Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES;

	/**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = Ows20Package.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE;

	/**
	 * The feature id for the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__BASE_URL = Ows20Package.GET_CAPABILITIES_TYPE__BASE_URL;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__SERVICE = Ows20Package.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Get Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE_FEATURE_COUNT = Ows20Package.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.GetResultTypeImpl <em>Get Result Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.GetResultTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGetResultType()
	 * @generated
	 */
	int GET_RESULT_TYPE = 19;

	/**
	 * The feature id for the '<em><b>Extension</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_RESULT_TYPE__EXTENSION = REQUEST_BASE_TYPE__EXTENSION;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_RESULT_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_RESULT_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Job ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_RESULT_TYPE__JOB_ID = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Get Result Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_RESULT_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.GetStatusTypeImpl <em>Get Status Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.GetStatusTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGetStatusType()
	 * @generated
	 */
	int GET_STATUS_TYPE = 20;

	/**
	 * The feature id for the '<em><b>Extension</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_STATUS_TYPE__EXTENSION = REQUEST_BASE_TYPE__EXTENSION;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_STATUS_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_STATUS_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Job ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_STATUS_TYPE__JOB_ID = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Get Status Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_STATUS_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.net.opengis.wps20.impl.InputDescriptionTypeImpl <em>Input Description Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.net.opengis.wps20.impl.InputDescriptionTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getInputDescriptionType()
	 * @generated
	 */
	int INPUT_DESCRIPTION_TYPE = 21;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__METADATA_GROUP = DESCRIPTION_TYPE__METADATA_GROUP;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

	/**
	 * The feature id for the '<em><b>Data Description Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Data Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_DESCRIPTION_TYPE__INPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

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
	 * The meta object id for the '{@link net.opengis.wps20.impl.LiteralDataDomainTypeImpl <em>Literal Data Domain Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.LiteralDataDomainTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getLiteralDataDomainType()
	 * @generated
	 */
	int LITERAL_DATA_DOMAIN_TYPE = 22;

	/**
	 * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES = 0;

	/**
	 * The feature id for the '<em><b>Any Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE = 1;

	/**
	 * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE = 2;

	/**
	 * The feature id for the '<em><b>Data Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE = 3;

	/**
	 * The feature id for the '<em><b>UOM</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE__UOM = 4;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE = 5;

	/**
	 * The number of structural features of the '<em>Literal Data Domain Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.LiteralDataDomainType1Impl <em>Literal Data Domain Type1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.LiteralDataDomainType1Impl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getLiteralDataDomainType1()
	 * @generated
	 */
	int LITERAL_DATA_DOMAIN_TYPE1 = 23;

	/**
	 * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE1__ALLOWED_VALUES = LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES;

	/**
	 * The feature id for the '<em><b>Any Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE1__ANY_VALUE = LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE;

	/**
	 * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE1__VALUES_REFERENCE = LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE;

	/**
	 * The feature id for the '<em><b>Data Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE1__DATA_TYPE = LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE;

	/**
	 * The feature id for the '<em><b>UOM</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE1__UOM = LITERAL_DATA_DOMAIN_TYPE__UOM;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE1__DEFAULT_VALUE = LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE1__DEFAULT = LITERAL_DATA_DOMAIN_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Literal Data Domain Type1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_DOMAIN_TYPE1_FEATURE_COUNT = LITERAL_DATA_DOMAIN_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.LiteralDataTypeImpl <em>Literal Data Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.LiteralDataTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getLiteralDataType()
	 * @generated
	 */
	int LITERAL_DATA_TYPE = 24;

	/**
	 * The feature id for the '<em><b>Format</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_TYPE__FORMAT = DATA_DESCRIPTION_TYPE__FORMAT;

	/**
	 * The feature id for the '<em><b>Literal Data Domain</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN = DATA_DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Literal Data Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_DATA_TYPE_FEATURE_COUNT = DATA_DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.LiteralValueTypeImpl <em>Literal Value Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.LiteralValueTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getLiteralValueType()
	 * @generated
	 */
	int LITERAL_VALUE_TYPE = 25;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_VALUE_TYPE__VALUE = Ows20Package.VALUE_TYPE__VALUE;

	/**
	 * The feature id for the '<em><b>Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_VALUE_TYPE__DATA_TYPE = Ows20Package.VALUE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Uom</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_VALUE_TYPE__UOM = Ows20Package.VALUE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Literal Value Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_VALUE_TYPE_FEATURE_COUNT = Ows20Package.VALUE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.OutputDefinitionTypeImpl <em>Output Definition Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.OutputDefinitionTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getOutputDefinitionType()
	 * @generated
	 */
	int OUTPUT_DEFINITION_TYPE = 26;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DEFINITION_TYPE__OUTPUT = 0;

	/**
	 * The feature id for the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DEFINITION_TYPE__ENCODING = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DEFINITION_TYPE__ID = 2;

	/**
	 * The feature id for the '<em><b>Mime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DEFINITION_TYPE__MIME_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DEFINITION_TYPE__SCHEMA = 4;

	/**
	 * The feature id for the '<em><b>Transmission</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DEFINITION_TYPE__TRANSMISSION = 5;

	/**
	 * The number of structural features of the '<em>Output Definition Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DEFINITION_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.OutputDescriptionTypeImpl <em>Output Description Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.OutputDescriptionTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getOutputDescriptionType()
	 * @generated
	 */
	int OUTPUT_DESCRIPTION_TYPE = 27;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__METADATA_GROUP = DESCRIPTION_TYPE__METADATA_GROUP;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

	/**
	 * The feature id for the '<em><b>Data Description Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Data Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE__OUTPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Output Description Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_DESCRIPTION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ProcessDescriptionTypeImpl <em>Process Description Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ProcessDescriptionTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getProcessDescriptionType()
	 * @generated
	 */
	int PROCESS_DESCRIPTION_TYPE = 28;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__METADATA_GROUP = DESCRIPTION_TYPE__METADATA_GROUP;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__INPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__OUTPUT = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Lang</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE__LANG = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Process Description Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_DESCRIPTION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ProcessOfferingsTypeImpl <em>Process Offerings Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ProcessOfferingsTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getProcessOfferingsType()
	 * @generated
	 */
	int PROCESS_OFFERINGS_TYPE = 29;

	/**
	 * The feature id for the '<em><b>Process Offering</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING = 0;

	/**
	 * The number of structural features of the '<em>Process Offerings Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERINGS_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ProcessOfferingTypeImpl <em>Process Offering Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ProcessOfferingTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getProcessOfferingType()
	 * @generated
	 */
	int PROCESS_OFFERING_TYPE = 30;

	/**
	 * The feature id for the '<em><b>Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERING_TYPE__PROCESS = 0;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERING_TYPE__ANY = 1;

	/**
	 * The feature id for the '<em><b>Job Control Options</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERING_TYPE__JOB_CONTROL_OPTIONS = 2;

	/**
	 * The feature id for the '<em><b>Output Transmission</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERING_TYPE__OUTPUT_TRANSMISSION = 3;

	/**
	 * The feature id for the '<em><b>Process Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERING_TYPE__PROCESS_MODEL = 4;

	/**
	 * The feature id for the '<em><b>Process Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERING_TYPE__PROCESS_VERSION = 5;

	/**
	 * The number of structural features of the '<em>Process Offering Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OFFERING_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ProcessSummaryTypeImpl <em>Process Summary Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ProcessSummaryTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getProcessSummaryType()
	 * @generated
	 */
	int PROCESS_SUMMARY_TYPE = 31;

	/**
	 * The feature id for the '<em><b>Title</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__IDENTIFIER = DESCRIPTION_TYPE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__METADATA_GROUP = DESCRIPTION_TYPE__METADATA_GROUP;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__METADATA = DESCRIPTION_TYPE__METADATA;

	/**
	 * The feature id for the '<em><b>Job Control Options</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__JOB_CONTROL_OPTIONS = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Output Transmission</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__OUTPUT_TRANSMISSION = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Process Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__PROCESS_MODEL = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Process Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE__PROCESS_VERSION = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Process Summary Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_SUMMARY_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ReferenceTypeImpl <em>Reference Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ReferenceTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getReferenceType()
	 * @generated
	 */
	int REFERENCE_TYPE = 32;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__BODY = 0;

	/**
	 * The feature id for the '<em><b>Body Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__BODY_REFERENCE = 1;

	/**
	 * The feature id for the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__ENCODING = 2;

	/**
	 * The feature id for the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__HREF = 3;

	/**
	 * The feature id for the '<em><b>Mime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__MIME_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__SCHEMA = 5;

	/**
	 * The number of structural features of the '<em>Reference Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.ResultTypeImpl <em>Result Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.ResultTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getResultType()
	 * @generated
	 */
	int RESULT_TYPE = 34;

	/**
	 * The feature id for the '<em><b>Job ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_TYPE__JOB_ID = 0;

	/**
	 * The feature id for the '<em><b>Expiration Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_TYPE__EXPIRATION_DATE = 1;

	/**
	 * The feature id for the '<em><b>Output</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_TYPE__OUTPUT = 2;

	/**
	 * The number of structural features of the '<em>Result Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.StatusInfoTypeImpl <em>Status Info Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.StatusInfoTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusInfoType()
	 * @generated
	 */
	int STATUS_INFO_TYPE = 35;

	/**
	 * The feature id for the '<em><b>Job ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_INFO_TYPE__JOB_ID = 0;

	/**
	 * The feature id for the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_INFO_TYPE__STATUS = 1;

	/**
	 * The feature id for the '<em><b>Expiration Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_INFO_TYPE__EXPIRATION_DATE = 2;

	/**
	 * The feature id for the '<em><b>Estimated Completion</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_INFO_TYPE__ESTIMATED_COMPLETION = 3;

	/**
	 * The feature id for the '<em><b>Next Poll</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_INFO_TYPE__NEXT_POLL = 4;

	/**
	 * The feature id for the '<em><b>Percent Completed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_INFO_TYPE__PERCENT_COMPLETED = 5;

	/**
	 * The number of structural features of the '<em>Status Info Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_INFO_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.SupportedCRSTypeImpl <em>Supported CRS Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.SupportedCRSTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getSupportedCRSType()
	 * @generated
	 */
	int SUPPORTED_CRS_TYPE = 36;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORTED_CRS_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORTED_CRS_TYPE__DEFAULT = 1;

	/**
	 * The number of structural features of the '<em>Supported CRS Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORTED_CRS_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.impl.WPSCapabilitiesTypeImpl <em>WPS Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.impl.WPSCapabilitiesTypeImpl
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getWPSCapabilitiesType()
	 * @generated
	 */
	int WPS_CAPABILITIES_TYPE = 37;

	/**
	 * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__SERVICE_IDENTIFICATION = Ows20Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION;

	/**
	 * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__SERVICE_PROVIDER = Ows20Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER;

	/**
	 * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__OPERATIONS_METADATA = Ows20Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA;

	/**
	 * The feature id for the '<em><b>Languages</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__LANGUAGES = Ows20Package.CAPABILITIES_BASE_TYPE__LANGUAGES;

	/**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__UPDATE_SEQUENCE = Ows20Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__VERSION = Ows20Package.CAPABILITIES_BASE_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__CONTENTS = Ows20Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__EXTENSION = Ows20Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE__SERVICE = Ows20Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>WPS Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WPS_CAPABILITIES_TYPE_FEATURE_COUNT = Ows20Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.DataTransmissionModeType <em>Data Transmission Mode Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.DataTransmissionModeType
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataTransmissionModeType()
	 * @generated
	 */
	int DATA_TRANSMISSION_MODE_TYPE = 38;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.JobControlOptionsTypeMember0 <em>Job Control Options Type Member0</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.JobControlOptionsTypeMember0
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsTypeMember0()
	 * @generated
	 */
	int JOB_CONTROL_OPTIONS_TYPE_MEMBER0 = 39;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.ModeType <em>Mode Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.ModeType
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getModeType()
	 * @generated
	 */
	int MODE_TYPE = 40;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.ResponseType <em>Response Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.ResponseType
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getResponseType()
	 * @generated
	 */
	int RESPONSE_TYPE = 41;

	/**
	 * The meta object id for the '{@link net.opengis.wps20.StatusTypeMember0 <em>Status Type Member0</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.StatusTypeMember0
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusTypeMember0()
	 * @generated
	 */
	int STATUS_TYPE_MEMBER0 = 42;

	/**
	 * The meta object id for the '<em>Data Transmission Mode Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.DataTransmissionModeType
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataTransmissionModeTypeObject()
	 * @generated
	 */
	int DATA_TRANSMISSION_MODE_TYPE_OBJECT = 43;

	/**
	 * The meta object id for the '<em>Job Control Options Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsType()
	 * @generated
	 */
	int JOB_CONTROL_OPTIONS_TYPE = 44;

	/**
	 * The meta object id for the '<em>Job Control Options Type1</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsType1()
	 * @generated
	 */
	int JOB_CONTROL_OPTIONS_TYPE1 = 45;

	/**
	 * The meta object id for the '<em>Job Control Options Type Member0 Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.JobControlOptionsTypeMember0
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsTypeMember0Object()
	 * @generated
	 */
	int JOB_CONTROL_OPTIONS_TYPE_MEMBER0_OBJECT = 46;

	/**
	 * The meta object id for the '<em>Job Control Options Type Member1</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsTypeMember1()
	 * @generated
	 */
	int JOB_CONTROL_OPTIONS_TYPE_MEMBER1 = 47;

	/**
	 * The meta object id for the '<em>Mode Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.ModeType
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getModeTypeObject()
	 * @generated
	 */
	int MODE_TYPE_OBJECT = 48;

	/**
	 * The meta object id for the '<em>Output Transmission Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getOutputTransmissionType()
	 * @generated
	 */
	int OUTPUT_TRANSMISSION_TYPE = 49;

	/**
	 * The meta object id for the '<em>Percent Completed Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.math.BigInteger
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getPercentCompletedType()
	 * @generated
	 */
	int PERCENT_COMPLETED_TYPE = 50;

	/**
	 * The meta object id for the '<em>Response Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.ResponseType
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getResponseTypeObject()
	 * @generated
	 */
	int RESPONSE_TYPE_OBJECT = 51;

	/**
	 * The meta object id for the '<em>Status Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusType()
	 * @generated
	 */
	int STATUS_TYPE = 52;

	/**
	 * The meta object id for the '<em>Status Type Member0 Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wps20.StatusTypeMember0
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusTypeMember0Object()
	 * @generated
	 */
	int STATUS_TYPE_MEMBER0_OBJECT = 53;

	/**
	 * The meta object id for the '<em>Status Type Member1</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusTypeMember1()
	 * @generated
	 */
	int STATUS_TYPE_MEMBER1 = 54;


	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.BodyReferenceType <em>Body Reference Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Body Reference Type</em>'.
	 * @see net.opengis.wps20.BodyReferenceType
	 * @generated
	 */
	EClass getBodyReferenceType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.BodyReferenceType#getHref <em>Href</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Href</em>'.
	 * @see net.opengis.wps20.BodyReferenceType#getHref()
	 * @see #getBodyReferenceType()
	 * @generated
	 */
	EAttribute getBodyReferenceType_Href();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.BoundingBoxDataType <em>Bounding Box Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bounding Box Data Type</em>'.
	 * @see net.opengis.wps20.BoundingBoxDataType
	 * @generated
	 */
	EClass getBoundingBoxDataType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.BoundingBoxDataType#getSupportedCRS <em>Supported CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Supported CRS</em>'.
	 * @see net.opengis.wps20.BoundingBoxDataType#getSupportedCRS()
	 * @see #getBoundingBoxDataType()
	 * @generated
	 */
	EReference getBoundingBoxDataType_SupportedCRS();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ComplexDataType <em>Complex Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Complex Data Type</em>'.
	 * @see net.opengis.wps20.ComplexDataType
	 * @generated
	 */
	EClass getComplexDataType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wps20.ComplexDataType#getAny <em>Any</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Any</em>'.
	 * @see net.opengis.wps20.ComplexDataType#getAny()
	 * @see #getComplexDataType()
	 * @generated
	 */
	EAttribute getComplexDataType_Any();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ContentsType <em>Contents Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contents Type</em>'.
	 * @see net.opengis.wps20.ContentsType
	 * @generated
	 */
	EClass getContentsType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.ContentsType#getProcessSummary <em>Process Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Process Summary</em>'.
	 * @see net.opengis.wps20.ContentsType#getProcessSummary()
	 * @see #getContentsType()
	 * @generated
	 */
	EReference getContentsType_ProcessSummary();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.DataDescriptionType <em>Data Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Description Type</em>'.
	 * @see net.opengis.wps20.DataDescriptionType
	 * @generated
	 */
	EClass getDataDescriptionType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.DataDescriptionType#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Format</em>'.
	 * @see net.opengis.wps20.DataDescriptionType#getFormat()
	 * @see #getDataDescriptionType()
	 * @generated
	 */
	EReference getDataDescriptionType_Format();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.DataInputType <em>Data Input Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Input Type</em>'.
	 * @see net.opengis.wps20.DataInputType
	 * @generated
	 */
	EClass getDataInputType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DataInputType#getData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data</em>'.
	 * @see net.opengis.wps20.DataInputType#getData()
	 * @see #getDataInputType()
	 * @generated
	 */
	EReference getDataInputType_Data();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DataInputType#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Reference</em>'.
	 * @see net.opengis.wps20.DataInputType#getReference()
	 * @see #getDataInputType()
	 * @generated
	 */
	EReference getDataInputType_Reference();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.DataInputType#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input</em>'.
	 * @see net.opengis.wps20.DataInputType#getInput()
	 * @see #getDataInputType()
	 * @generated
	 */
	EReference getDataInputType_Input();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DataInputType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.opengis.wps20.DataInputType#getId()
	 * @see #getDataInputType()
	 * @generated
	 */
	EAttribute getDataInputType_Id();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.DataOutputType <em>Data Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Output Type</em>'.
	 * @see net.opengis.wps20.DataOutputType
	 * @generated
	 */
	EClass getDataOutputType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DataOutputType#getData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data</em>'.
	 * @see net.opengis.wps20.DataOutputType#getData()
	 * @see #getDataOutputType()
	 * @generated
	 */
	EReference getDataOutputType_Data();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DataOutputType#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Reference</em>'.
	 * @see net.opengis.wps20.DataOutputType#getReference()
	 * @see #getDataOutputType()
	 * @generated
	 */
	EReference getDataOutputType_Reference();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DataOutputType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output</em>'.
	 * @see net.opengis.wps20.DataOutputType#getOutput()
	 * @see #getDataOutputType()
	 * @generated
	 */
	EReference getDataOutputType_Output();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DataOutputType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.opengis.wps20.DataOutputType#getId()
	 * @see #getDataOutputType()
	 * @generated
	 */
	EAttribute getDataOutputType_Id();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.DataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Type</em>'.
	 * @see net.opengis.wps20.DataType
	 * @generated
	 */
	EClass getDataType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DataType#getEncoding <em>Encoding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Encoding</em>'.
	 * @see net.opengis.wps20.DataType#getEncoding()
	 * @see #getDataType()
	 * @generated
	 */
	EAttribute getDataType_Encoding();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DataType#getMimeType <em>Mime Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mime Type</em>'.
	 * @see net.opengis.wps20.DataType#getMimeType()
	 * @see #getDataType()
	 * @generated
	 */
	EAttribute getDataType_MimeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DataType#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Schema</em>'.
	 * @see net.opengis.wps20.DataType#getSchema()
	 * @see #getDataType()
	 * @generated
	 */
	EAttribute getDataType_Schema();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.DescribeProcessType <em>Describe Process Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Describe Process Type</em>'.
	 * @see net.opengis.wps20.DescribeProcessType
	 * @generated
	 */
	EClass getDescribeProcessType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.DescribeProcessType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Identifier</em>'.
	 * @see net.opengis.wps20.DescribeProcessType#getIdentifier()
	 * @see #getDescribeProcessType()
	 * @generated
	 */
	EReference getDescribeProcessType_Identifier();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DescribeProcessType#getLang <em>Lang</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lang</em>'.
	 * @see net.opengis.wps20.DescribeProcessType#getLang()
	 * @see #getDescribeProcessType()
	 * @generated
	 */
	EAttribute getDescribeProcessType_Lang();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.DescriptionType <em>Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Description Type</em>'.
	 * @see net.opengis.wps20.DescriptionType
	 * @generated
	 */
	EClass getDescriptionType();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.DismissType <em>Dismiss Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dismiss Type</em>'.
	 * @see net.opengis.wps20.DismissType
	 * @generated
	 */
	EClass getDismissType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DismissType#getJobID <em>Job ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job ID</em>'.
	 * @see net.opengis.wps20.DismissType#getJobID()
	 * @see #getDismissType()
	 * @generated
	 */
	EAttribute getDismissType_JobID();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see net.opengis.wps20.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wps20.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link net.opengis.wps20.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link net.opengis.wps20.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getBoundingBoxData <em>Bounding Box Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Bounding Box Data</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getBoundingBoxData()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_BoundingBoxData();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getDataDescription <em>Data Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data Description</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getDataDescription()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DataDescription();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getCapabilities <em>Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Capabilities</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Capabilities();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getComplexData <em>Complex Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Complex Data</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getComplexData()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_ComplexData();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getContents <em>Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Contents</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getContents()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Contents();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getData()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Data();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getDescribeProcess <em>Describe Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Describe Process</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getDescribeProcess()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DescribeProcess();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getDismiss <em>Dismiss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Dismiss</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getDismiss()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Dismiss();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getExecute <em>Execute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Execute</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getExecute()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Execute();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DocumentRoot#getExpirationDate <em>Expiration Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expiration Date</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getExpirationDate()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_ExpirationDate();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Format</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getFormat()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Format();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getGenericProcess <em>Generic Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Generic Process</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getGenericProcess()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GenericProcess();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getGetCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetCapabilities();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getGetResult <em>Get Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Result</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getGetResult()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetResult();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getGetStatus <em>Get Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Status</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getGetStatus()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetStatus();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.DocumentRoot#getJobID <em>Job ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job ID</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getJobID()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_JobID();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getLiteralData <em>Literal Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Literal Data</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getLiteralData()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_LiteralData();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getLiteralValue <em>Literal Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Literal Value</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getLiteralValue()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_LiteralValue();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getProcess <em>Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Process</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getProcess()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Process();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getProcessOffering <em>Process Offering</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Process Offering</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getProcessOffering()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_ProcessOffering();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getProcessOfferings <em>Process Offerings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Process Offerings</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getProcessOfferings()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_ProcessOfferings();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Reference</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getReference()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Reference();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getResult <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Result</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getResult()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Result();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getStatusInfo <em>Status Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Status Info</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getStatusInfo()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_StatusInfo();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supported CRS</em>'.
	 * @see net.opengis.wps20.DocumentRoot#getSupportedCRS()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_SupportedCRS();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ExecuteRequestType <em>Execute Request Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Execute Request Type</em>'.
	 * @see net.opengis.wps20.ExecuteRequestType
	 * @generated
	 */
	EClass getExecuteRequestType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.ExecuteRequestType#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Identifier</em>'.
	 * @see net.opengis.wps20.ExecuteRequestType#getIdentifier()
	 * @see #getExecuteRequestType()
	 * @generated
	 */
	EReference getExecuteRequestType_Identifier();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.ExecuteRequestType#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input</em>'.
	 * @see net.opengis.wps20.ExecuteRequestType#getInput()
	 * @see #getExecuteRequestType()
	 * @generated
	 */
	EReference getExecuteRequestType_Input();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.ExecuteRequestType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output</em>'.
	 * @see net.opengis.wps20.ExecuteRequestType#getOutput()
	 * @see #getExecuteRequestType()
	 * @generated
	 */
	EReference getExecuteRequestType_Output();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ExecuteRequestType#getMode <em>Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mode</em>'.
	 * @see net.opengis.wps20.ExecuteRequestType#getMode()
	 * @see #getExecuteRequestType()
	 * @generated
	 */
	EAttribute getExecuteRequestType_Mode();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ExecuteRequestType#getResponse <em>Response</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Response</em>'.
	 * @see net.opengis.wps20.ExecuteRequestType#getResponse()
	 * @see #getExecuteRequestType()
	 * @generated
	 */
	EAttribute getExecuteRequestType_Response();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ExtensionType <em>Extension Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Extension Type</em>'.
	 * @see net.opengis.wps20.ExtensionType
	 * @generated
	 */
	EClass getExtensionType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wps20.ExtensionType#getAny <em>Any</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Any</em>'.
	 * @see net.opengis.wps20.ExtensionType#getAny()
	 * @see #getExtensionType()
	 * @generated
	 */
	EAttribute getExtensionType_Any();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.FormatType <em>Format Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Format Type</em>'.
	 * @see net.opengis.wps20.FormatType
	 * @generated
	 */
	EClass getFormatType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.FormatType#isDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see net.opengis.wps20.FormatType#isDefault()
	 * @see #getFormatType()
	 * @generated
	 */
	EAttribute getFormatType_Default();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.FormatType#getEncoding <em>Encoding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Encoding</em>'.
	 * @see net.opengis.wps20.FormatType#getEncoding()
	 * @see #getFormatType()
	 * @generated
	 */
	EAttribute getFormatType_Encoding();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.FormatType#getMaximumMegabytes <em>Maximum Megabytes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum Megabytes</em>'.
	 * @see net.opengis.wps20.FormatType#getMaximumMegabytes()
	 * @see #getFormatType()
	 * @generated
	 */
	EAttribute getFormatType_MaximumMegabytes();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.FormatType#getMimeType <em>Mime Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mime Type</em>'.
	 * @see net.opengis.wps20.FormatType#getMimeType()
	 * @see #getFormatType()
	 * @generated
	 */
	EAttribute getFormatType_MimeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.FormatType#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Schema</em>'.
	 * @see net.opengis.wps20.FormatType#getSchema()
	 * @see #getFormatType()
	 * @generated
	 */
	EAttribute getFormatType_Schema();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.GenericInputType <em>Generic Input Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generic Input Type</em>'.
	 * @see net.opengis.wps20.GenericInputType
	 * @generated
	 */
	EClass getGenericInputType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.GenericInputType#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input</em>'.
	 * @see net.opengis.wps20.GenericInputType#getInput()
	 * @see #getGenericInputType()
	 * @generated
	 */
	EReference getGenericInputType_Input();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.GenericInputType#getMaxOccurs <em>Max Occurs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Occurs</em>'.
	 * @see net.opengis.wps20.GenericInputType#getMaxOccurs()
	 * @see #getGenericInputType()
	 * @generated
	 */
	EAttribute getGenericInputType_MaxOccurs();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.GenericInputType#getMinOccurs <em>Min Occurs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Occurs</em>'.
	 * @see net.opengis.wps20.GenericInputType#getMinOccurs()
	 * @see #getGenericInputType()
	 * @generated
	 */
	EAttribute getGenericInputType_MinOccurs();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.GenericOutputType <em>Generic Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generic Output Type</em>'.
	 * @see net.opengis.wps20.GenericOutputType
	 * @generated
	 */
	EClass getGenericOutputType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.GenericOutputType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output</em>'.
	 * @see net.opengis.wps20.GenericOutputType#getOutput()
	 * @see #getGenericOutputType()
	 * @generated
	 */
	EReference getGenericOutputType_Output();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.GenericProcessType <em>Generic Process Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generic Process Type</em>'.
	 * @see net.opengis.wps20.GenericProcessType
	 * @generated
	 */
	EClass getGenericProcessType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.GenericProcessType#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input</em>'.
	 * @see net.opengis.wps20.GenericProcessType#getInput()
	 * @see #getGenericProcessType()
	 * @generated
	 */
	EReference getGenericProcessType_Input();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.GenericProcessType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output</em>'.
	 * @see net.opengis.wps20.GenericProcessType#getOutput()
	 * @see #getGenericProcessType()
	 * @generated
	 */
	EReference getGenericProcessType_Output();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Capabilities Type</em>'.
	 * @see net.opengis.wps20.GetCapabilitiesType
	 * @generated
	 */
	EClass getGetCapabilitiesType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.GetCapabilitiesType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wps20.GetCapabilitiesType#getService()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
	EAttribute getGetCapabilitiesType_Service();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.GetResultType <em>Get Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Result Type</em>'.
	 * @see net.opengis.wps20.GetResultType
	 * @generated
	 */
	EClass getGetResultType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.GetResultType#getJobID <em>Job ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job ID</em>'.
	 * @see net.opengis.wps20.GetResultType#getJobID()
	 * @see #getGetResultType()
	 * @generated
	 */
	EAttribute getGetResultType_JobID();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.GetStatusType <em>Get Status Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Status Type</em>'.
	 * @see net.opengis.wps20.GetStatusType
	 * @generated
	 */
	EClass getGetStatusType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.GetStatusType#getJobID <em>Job ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job ID</em>'.
	 * @see net.opengis.wps20.GetStatusType#getJobID()
	 * @see #getGetStatusType()
	 * @generated
	 */
	EAttribute getGetStatusType_JobID();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.InputDescriptionType <em>Input Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Description Type</em>'.
	 * @see net.opengis.wps20.InputDescriptionType
	 * @generated
	 */
	EClass getInputDescriptionType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wps20.InputDescriptionType#getDataDescriptionGroup <em>Data Description Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Data Description Group</em>'.
	 * @see net.opengis.wps20.InputDescriptionType#getDataDescriptionGroup()
	 * @see #getInputDescriptionType()
	 * @generated
	 */
	EAttribute getInputDescriptionType_DataDescriptionGroup();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.InputDescriptionType#getDataDescription <em>Data Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data Description</em>'.
	 * @see net.opengis.wps20.InputDescriptionType#getDataDescription()
	 * @see #getInputDescriptionType()
	 * @generated
	 */
	EReference getInputDescriptionType_DataDescription();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.InputDescriptionType#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input</em>'.
	 * @see net.opengis.wps20.InputDescriptionType#getInput()
	 * @see #getInputDescriptionType()
	 * @generated
	 */
	EReference getInputDescriptionType_Input();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.InputDescriptionType#getMaxOccurs <em>Max Occurs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Occurs</em>'.
	 * @see net.opengis.wps20.InputDescriptionType#getMaxOccurs()
	 * @see #getInputDescriptionType()
	 * @generated
	 */
	EAttribute getInputDescriptionType_MaxOccurs();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.InputDescriptionType#getMinOccurs <em>Min Occurs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Occurs</em>'.
	 * @see net.opengis.wps20.InputDescriptionType#getMinOccurs()
	 * @see #getInputDescriptionType()
	 * @generated
	 */
	EAttribute getInputDescriptionType_MinOccurs();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.LiteralDataDomainType <em>Literal Data Domain Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Literal Data Domain Type</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType
	 * @generated
	 */
	EClass getLiteralDataDomainType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.LiteralDataDomainType#getAllowedValues <em>Allowed Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Allowed Values</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType#getAllowedValues()
	 * @see #getLiteralDataDomainType()
	 * @generated
	 */
	EReference getLiteralDataDomainType_AllowedValues();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.LiteralDataDomainType#getAnyValue <em>Any Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Any Value</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType#getAnyValue()
	 * @see #getLiteralDataDomainType()
	 * @generated
	 */
	EReference getLiteralDataDomainType_AnyValue();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.LiteralDataDomainType#getValuesReference <em>Values Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Values Reference</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType#getValuesReference()
	 * @see #getLiteralDataDomainType()
	 * @generated
	 */
	EReference getLiteralDataDomainType_ValuesReference();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.LiteralDataDomainType#getDataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data Type</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType#getDataType()
	 * @see #getLiteralDataDomainType()
	 * @generated
	 */
	EReference getLiteralDataDomainType_DataType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.LiteralDataDomainType#getUOM <em>UOM</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>UOM</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType#getUOM()
	 * @see #getLiteralDataDomainType()
	 * @generated
	 */
	EReference getLiteralDataDomainType_UOM();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.LiteralDataDomainType#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Default Value</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType#getDefaultValue()
	 * @see #getLiteralDataDomainType()
	 * @generated
	 */
	EReference getLiteralDataDomainType_DefaultValue();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.LiteralDataDomainType1 <em>Literal Data Domain Type1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Literal Data Domain Type1</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType1
	 * @generated
	 */
	EClass getLiteralDataDomainType1();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.LiteralDataDomainType1#isDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see net.opengis.wps20.LiteralDataDomainType1#isDefault()
	 * @see #getLiteralDataDomainType1()
	 * @generated
	 */
	EAttribute getLiteralDataDomainType1_Default();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.LiteralDataType <em>Literal Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Literal Data Type</em>'.
	 * @see net.opengis.wps20.LiteralDataType
	 * @generated
	 */
	EClass getLiteralDataType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.LiteralDataType#getLiteralDataDomain <em>Literal Data Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Literal Data Domain</em>'.
	 * @see net.opengis.wps20.LiteralDataType#getLiteralDataDomain()
	 * @see #getLiteralDataType()
	 * @generated
	 */
	EReference getLiteralDataType_LiteralDataDomain();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.LiteralValueType <em>Literal Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Literal Value Type</em>'.
	 * @see net.opengis.wps20.LiteralValueType
	 * @generated
	 */
	EClass getLiteralValueType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.LiteralValueType#getDataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Type</em>'.
	 * @see net.opengis.wps20.LiteralValueType#getDataType()
	 * @see #getLiteralValueType()
	 * @generated
	 */
	EAttribute getLiteralValueType_DataType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.LiteralValueType#getUom <em>Uom</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uom</em>'.
	 * @see net.opengis.wps20.LiteralValueType#getUom()
	 * @see #getLiteralValueType()
	 * @generated
	 */
	EAttribute getLiteralValueType_Uom();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.OutputDefinitionType <em>Output Definition Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Definition Type</em>'.
	 * @see net.opengis.wps20.OutputDefinitionType
	 * @generated
	 */
	EClass getOutputDefinitionType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.OutputDefinitionType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output</em>'.
	 * @see net.opengis.wps20.OutputDefinitionType#getOutput()
	 * @see #getOutputDefinitionType()
	 * @generated
	 */
	EReference getOutputDefinitionType_Output();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.OutputDefinitionType#getEncoding <em>Encoding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Encoding</em>'.
	 * @see net.opengis.wps20.OutputDefinitionType#getEncoding()
	 * @see #getOutputDefinitionType()
	 * @generated
	 */
	EAttribute getOutputDefinitionType_Encoding();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.OutputDefinitionType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.opengis.wps20.OutputDefinitionType#getId()
	 * @see #getOutputDefinitionType()
	 * @generated
	 */
	EAttribute getOutputDefinitionType_Id();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.OutputDefinitionType#getMimeType <em>Mime Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mime Type</em>'.
	 * @see net.opengis.wps20.OutputDefinitionType#getMimeType()
	 * @see #getOutputDefinitionType()
	 * @generated
	 */
	EAttribute getOutputDefinitionType_MimeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.OutputDefinitionType#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Schema</em>'.
	 * @see net.opengis.wps20.OutputDefinitionType#getSchema()
	 * @see #getOutputDefinitionType()
	 * @generated
	 */
	EAttribute getOutputDefinitionType_Schema();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.OutputDefinitionType#getTransmission <em>Transmission</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transmission</em>'.
	 * @see net.opengis.wps20.OutputDefinitionType#getTransmission()
	 * @see #getOutputDefinitionType()
	 * @generated
	 */
	EAttribute getOutputDefinitionType_Transmission();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.OutputDescriptionType <em>Output Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Description Type</em>'.
	 * @see net.opengis.wps20.OutputDescriptionType
	 * @generated
	 */
	EClass getOutputDescriptionType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wps20.OutputDescriptionType#getDataDescriptionGroup <em>Data Description Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Data Description Group</em>'.
	 * @see net.opengis.wps20.OutputDescriptionType#getDataDescriptionGroup()
	 * @see #getOutputDescriptionType()
	 * @generated
	 */
	EAttribute getOutputDescriptionType_DataDescriptionGroup();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.OutputDescriptionType#getDataDescription <em>Data Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data Description</em>'.
	 * @see net.opengis.wps20.OutputDescriptionType#getDataDescription()
	 * @see #getOutputDescriptionType()
	 * @generated
	 */
	EReference getOutputDescriptionType_DataDescription();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.OutputDescriptionType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output</em>'.
	 * @see net.opengis.wps20.OutputDescriptionType#getOutput()
	 * @see #getOutputDescriptionType()
	 * @generated
	 */
	EReference getOutputDescriptionType_Output();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ProcessDescriptionType <em>Process Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process Description Type</em>'.
	 * @see net.opengis.wps20.ProcessDescriptionType
	 * @generated
	 */
	EClass getProcessDescriptionType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.ProcessDescriptionType#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input</em>'.
	 * @see net.opengis.wps20.ProcessDescriptionType#getInput()
	 * @see #getProcessDescriptionType()
	 * @generated
	 */
	EReference getProcessDescriptionType_Input();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.ProcessDescriptionType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output</em>'.
	 * @see net.opengis.wps20.ProcessDescriptionType#getOutput()
	 * @see #getProcessDescriptionType()
	 * @generated
	 */
	EReference getProcessDescriptionType_Output();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessDescriptionType#getLang <em>Lang</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lang</em>'.
	 * @see net.opengis.wps20.ProcessDescriptionType#getLang()
	 * @see #getProcessDescriptionType()
	 * @generated
	 */
	EAttribute getProcessDescriptionType_Lang();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ProcessOfferingsType <em>Process Offerings Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process Offerings Type</em>'.
	 * @see net.opengis.wps20.ProcessOfferingsType
	 * @generated
	 */
	EClass getProcessOfferingsType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.ProcessOfferingsType#getProcessOffering <em>Process Offering</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Process Offering</em>'.
	 * @see net.opengis.wps20.ProcessOfferingsType#getProcessOffering()
	 * @see #getProcessOfferingsType()
	 * @generated
	 */
	EReference getProcessOfferingsType_ProcessOffering();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ProcessOfferingType <em>Process Offering Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process Offering Type</em>'.
	 * @see net.opengis.wps20.ProcessOfferingType
	 * @generated
	 */
	EClass getProcessOfferingType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.ProcessOfferingType#getProcess <em>Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Process</em>'.
	 * @see net.opengis.wps20.ProcessOfferingType#getProcess()
	 * @see #getProcessOfferingType()
	 * @generated
	 */
	EReference getProcessOfferingType_Process();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wps20.ProcessOfferingType#getAny <em>Any</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Any</em>'.
	 * @see net.opengis.wps20.ProcessOfferingType#getAny()
	 * @see #getProcessOfferingType()
	 * @generated
	 */
	EAttribute getProcessOfferingType_Any();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessOfferingType#getJobControlOptions <em>Job Control Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job Control Options</em>'.
	 * @see net.opengis.wps20.ProcessOfferingType#getJobControlOptions()
	 * @see #getProcessOfferingType()
	 * @generated
	 */
	EAttribute getProcessOfferingType_JobControlOptions();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessOfferingType#getOutputTransmission <em>Output Transmission</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Transmission</em>'.
	 * @see net.opengis.wps20.ProcessOfferingType#getOutputTransmission()
	 * @see #getProcessOfferingType()
	 * @generated
	 */
	EAttribute getProcessOfferingType_OutputTransmission();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessOfferingType#getProcessModel <em>Process Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Process Model</em>'.
	 * @see net.opengis.wps20.ProcessOfferingType#getProcessModel()
	 * @see #getProcessOfferingType()
	 * @generated
	 */
	EAttribute getProcessOfferingType_ProcessModel();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessOfferingType#getProcessVersion <em>Process Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Process Version</em>'.
	 * @see net.opengis.wps20.ProcessOfferingType#getProcessVersion()
	 * @see #getProcessOfferingType()
	 * @generated
	 */
	EAttribute getProcessOfferingType_ProcessVersion();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ProcessSummaryType <em>Process Summary Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process Summary Type</em>'.
	 * @see net.opengis.wps20.ProcessSummaryType
	 * @generated
	 */
	EClass getProcessSummaryType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessSummaryType#getJobControlOptions <em>Job Control Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job Control Options</em>'.
	 * @see net.opengis.wps20.ProcessSummaryType#getJobControlOptions()
	 * @see #getProcessSummaryType()
	 * @generated
	 */
	EAttribute getProcessSummaryType_JobControlOptions();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessSummaryType#getOutputTransmission <em>Output Transmission</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Transmission</em>'.
	 * @see net.opengis.wps20.ProcessSummaryType#getOutputTransmission()
	 * @see #getProcessSummaryType()
	 * @generated
	 */
	EAttribute getProcessSummaryType_OutputTransmission();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessSummaryType#getProcessModel <em>Process Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Process Model</em>'.
	 * @see net.opengis.wps20.ProcessSummaryType#getProcessModel()
	 * @see #getProcessSummaryType()
	 * @generated
	 */
	EAttribute getProcessSummaryType_ProcessModel();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ProcessSummaryType#getProcessVersion <em>Process Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Process Version</em>'.
	 * @see net.opengis.wps20.ProcessSummaryType#getProcessVersion()
	 * @see #getProcessSummaryType()
	 * @generated
	 */
	EAttribute getProcessSummaryType_ProcessVersion();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ReferenceType <em>Reference Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Type</em>'.
	 * @see net.opengis.wps20.ReferenceType
	 * @generated
	 */
	EClass getReferenceType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.ReferenceType#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Body</em>'.
	 * @see net.opengis.wps20.ReferenceType#getBody()
	 * @see #getReferenceType()
	 * @generated
	 */
	EReference getReferenceType_Body();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.ReferenceType#getBodyReference <em>Body Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Body Reference</em>'.
	 * @see net.opengis.wps20.ReferenceType#getBodyReference()
	 * @see #getReferenceType()
	 * @generated
	 */
	EReference getReferenceType_BodyReference();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ReferenceType#getEncoding <em>Encoding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Encoding</em>'.
	 * @see net.opengis.wps20.ReferenceType#getEncoding()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Encoding();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ReferenceType#getHref <em>Href</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Href</em>'.
	 * @see net.opengis.wps20.ReferenceType#getHref()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Href();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ReferenceType#getMimeType <em>Mime Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mime Type</em>'.
	 * @see net.opengis.wps20.ReferenceType#getMimeType()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_MimeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ReferenceType#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Schema</em>'.
	 * @see net.opengis.wps20.ReferenceType#getSchema()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Schema();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.RequestBaseType <em>Request Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Request Base Type</em>'.
	 * @see net.opengis.wps20.RequestBaseType
	 * @generated
	 */
	EClass getRequestBaseType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.RequestBaseType#getExtension <em>Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extension</em>'.
	 * @see net.opengis.wps20.RequestBaseType#getExtension()
	 * @see #getRequestBaseType()
	 * @generated
	 */
	EReference getRequestBaseType_Extension();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.RequestBaseType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wps20.RequestBaseType#getService()
	 * @see #getRequestBaseType()
	 * @generated
	 */
	EAttribute getRequestBaseType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.RequestBaseType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wps20.RequestBaseType#getVersion()
	 * @see #getRequestBaseType()
	 * @generated
	 */
	EAttribute getRequestBaseType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.ResultType <em>Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Result Type</em>'.
	 * @see net.opengis.wps20.ResultType
	 * @generated
	 */
	EClass getResultType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ResultType#getJobID <em>Job ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job ID</em>'.
	 * @see net.opengis.wps20.ResultType#getJobID()
	 * @see #getResultType()
	 * @generated
	 */
	EAttribute getResultType_JobID();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.ResultType#getExpirationDate <em>Expiration Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expiration Date</em>'.
	 * @see net.opengis.wps20.ResultType#getExpirationDate()
	 * @see #getResultType()
	 * @generated
	 */
	EAttribute getResultType_ExpirationDate();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wps20.ResultType#getOutput <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output</em>'.
	 * @see net.opengis.wps20.ResultType#getOutput()
	 * @see #getResultType()
	 * @generated
	 */
	EReference getResultType_Output();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.StatusInfoType <em>Status Info Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Status Info Type</em>'.
	 * @see net.opengis.wps20.StatusInfoType
	 * @generated
	 */
	EClass getStatusInfoType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.StatusInfoType#getJobID <em>Job ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Job ID</em>'.
	 * @see net.opengis.wps20.StatusInfoType#getJobID()
	 * @see #getStatusInfoType()
	 * @generated
	 */
	EAttribute getStatusInfoType_JobID();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.StatusInfoType#getStatus <em>Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Status</em>'.
	 * @see net.opengis.wps20.StatusInfoType#getStatus()
	 * @see #getStatusInfoType()
	 * @generated
	 */
	EAttribute getStatusInfoType_Status();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.StatusInfoType#getExpirationDate <em>Expiration Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expiration Date</em>'.
	 * @see net.opengis.wps20.StatusInfoType#getExpirationDate()
	 * @see #getStatusInfoType()
	 * @generated
	 */
	EAttribute getStatusInfoType_ExpirationDate();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.StatusInfoType#getEstimatedCompletion <em>Estimated Completion</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Estimated Completion</em>'.
	 * @see net.opengis.wps20.StatusInfoType#getEstimatedCompletion()
	 * @see #getStatusInfoType()
	 * @generated
	 */
	EAttribute getStatusInfoType_EstimatedCompletion();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.StatusInfoType#getNextPoll <em>Next Poll</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Next Poll</em>'.
	 * @see net.opengis.wps20.StatusInfoType#getNextPoll()
	 * @see #getStatusInfoType()
	 * @generated
	 */
	EAttribute getStatusInfoType_NextPoll();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.StatusInfoType#getPercentCompleted <em>Percent Completed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Percent Completed</em>'.
	 * @see net.opengis.wps20.StatusInfoType#getPercentCompleted()
	 * @see #getStatusInfoType()
	 * @generated
	 */
	EAttribute getStatusInfoType_PercentCompleted();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.SupportedCRSType <em>Supported CRS Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Supported CRS Type</em>'.
	 * @see net.opengis.wps20.SupportedCRSType
	 * @generated
	 */
	EClass getSupportedCRSType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.SupportedCRSType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.wps20.SupportedCRSType#getValue()
	 * @see #getSupportedCRSType()
	 * @generated
	 */
	EAttribute getSupportedCRSType_Value();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.SupportedCRSType#isDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see net.opengis.wps20.SupportedCRSType#isDefault()
	 * @see #getSupportedCRSType()
	 * @generated
	 */
	EAttribute getSupportedCRSType_Default();

	/**
	 * Returns the meta object for class '{@link net.opengis.wps20.WPSCapabilitiesType <em>WPS Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>WPS Capabilities Type</em>'.
	 * @see net.opengis.wps20.WPSCapabilitiesType
	 * @generated
	 */
	EClass getWPSCapabilitiesType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.WPSCapabilitiesType#getContents <em>Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Contents</em>'.
	 * @see net.opengis.wps20.WPSCapabilitiesType#getContents()
	 * @see #getWPSCapabilitiesType()
	 * @generated
	 */
	EReference getWPSCapabilitiesType_Contents();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wps20.WPSCapabilitiesType#getExtension <em>Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Extension</em>'.
	 * @see net.opengis.wps20.WPSCapabilitiesType#getExtension()
	 * @see #getWPSCapabilitiesType()
	 * @generated
	 */
	EReference getWPSCapabilitiesType_Extension();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wps20.WPSCapabilitiesType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wps20.WPSCapabilitiesType#getService()
	 * @see #getWPSCapabilitiesType()
	 * @generated
	 */
	EAttribute getWPSCapabilitiesType_Service();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wps20.DataTransmissionModeType <em>Data Transmission Mode Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Data Transmission Mode Type</em>'.
	 * @see net.opengis.wps20.DataTransmissionModeType
	 * @generated
	 */
	EEnum getDataTransmissionModeType();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wps20.JobControlOptionsTypeMember0 <em>Job Control Options Type Member0</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Job Control Options Type Member0</em>'.
	 * @see net.opengis.wps20.JobControlOptionsTypeMember0
	 * @generated
	 */
	EEnum getJobControlOptionsTypeMember0();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wps20.ModeType <em>Mode Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Mode Type</em>'.
	 * @see net.opengis.wps20.ModeType
	 * @generated
	 */
	EEnum getModeType();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wps20.ResponseType <em>Response Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Response Type</em>'.
	 * @see net.opengis.wps20.ResponseType
	 * @generated
	 */
	EEnum getResponseType();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wps20.StatusTypeMember0 <em>Status Type Member0</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Status Type Member0</em>'.
	 * @see net.opengis.wps20.StatusTypeMember0
	 * @generated
	 */
	EEnum getStatusTypeMember0();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wps20.DataTransmissionModeType <em>Data Transmission Mode Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Data Transmission Mode Type Object</em>'.
	 * @see net.opengis.wps20.DataTransmissionModeType
	 * @model instanceClass="net.opengis.wps20.DataTransmissionModeType"
	 *        extendedMetaData="name='DataTransmissionModeType:Object' baseType='DataTransmissionModeType'"
	 * @generated
	 */
	EDataType getDataTransmissionModeTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.lang.Object <em>Job Control Options Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 				This attribute type is used to specify process control options. 
     * 				The WPS specification only defines "execute-sync" and "execute-async",
     * 				each with an associated execution protocol.
     * 				Extensions may specify additional control options, such as "dimiss" which is
     * 				defined in the WPS dismiss extension.
     * 			
     * <!-- end-model-doc -->
	 * @return the meta object for data type '<em>Job Control Options Type</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object"
	 *        extendedMetaData="name='JobControlOptionsType' memberTypes='JobControlOptionsType_._member_._0 JobControlOptionsType_._member_._1'"
	 * @generated
	 */
	EDataType getJobControlOptionsType();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Job Control Options Type1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							Allowed execution modes are expressed in an XML list.
     * 						
     * <!-- end-model-doc -->
	 * @return the meta object for data type '<em>Job Control Options Type1</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='jobControlOptions_._type' itemType='JobControlOptionsType'"
	 * @generated
	 */
	EDataType getJobControlOptionsType1();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wps20.JobControlOptionsTypeMember0 <em>Job Control Options Type Member0 Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Job Control Options Type Member0 Object</em>'.
	 * @see net.opengis.wps20.JobControlOptionsTypeMember0
	 * @model instanceClass="net.opengis.wps20.JobControlOptionsTypeMember0"
	 *        extendedMetaData="name='JobControlOptionsType_._member_._0:Object' baseType='JobControlOptionsType_._member_._0'"
	 * @generated
	 */
	EDataType getJobControlOptionsTypeMember0Object();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Job Control Options Type Member1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Job Control Options Type Member1</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='JobControlOptionsType_._member_._1' baseType='http://www.eclipse.org/emf/2003/XMLType#string'"
	 * @generated
	 */
	EDataType getJobControlOptionsTypeMember1();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wps20.ModeType <em>Mode Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Mode Type Object</em>'.
	 * @see net.opengis.wps20.ModeType
	 * @model instanceClass="net.opengis.wps20.ModeType"
	 *        extendedMetaData="name='mode_._type:Object' baseType='mode_._type'"
	 * @generated
	 */
	EDataType getModeTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Output Transmission Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							Allowed data transmission modes are expressed in an XML list.
     * 						
     * <!-- end-model-doc -->
	 * @return the meta object for data type '<em>Output Transmission Type</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='outputTransmission_._type' itemType='DataTransmissionModeType'"
	 * @generated
	 */
	EDataType getOutputTransmissionType();

	/**
	 * Returns the meta object for data type '{@link java.math.BigInteger <em>Percent Completed Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Percent Completed Type</em>'.
	 * @see java.math.BigInteger
	 * @model instanceClass="java.math.BigInteger"
	 *        extendedMetaData="name='PercentCompleted_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#integer' minInclusive='0' maxInclusive='100'"
	 * @generated
	 */
	EDataType getPercentCompletedType();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wps20.ResponseType <em>Response Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Response Type Object</em>'.
	 * @see net.opengis.wps20.ResponseType
	 * @model instanceClass="net.opengis.wps20.ResponseType"
	 *        extendedMetaData="name='response_._type:Object' baseType='response_._type'"
	 * @generated
	 */
	EDataType getResponseTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.lang.Object <em>Status Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 								Basic status set to communicate the status of a server-side job to the client.
     * 								Extensions of this specification may introduce additional states for fine-grained
     * 								monitoring or domain-specific purposes.
     * 							
     * <!-- end-model-doc -->
	 * @return the meta object for data type '<em>Status Type</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object"
	 *        extendedMetaData="name='Status_._type' memberTypes='Status_._type_._member_._0 Status_._type_._member_._1'"
	 * @generated
	 */
	EDataType getStatusType();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wps20.StatusTypeMember0 <em>Status Type Member0 Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Status Type Member0 Object</em>'.
	 * @see net.opengis.wps20.StatusTypeMember0
	 * @model instanceClass="net.opengis.wps20.StatusTypeMember0"
	 *        extendedMetaData="name='Status_._type_._member_._0:Object' baseType='Status_._type_._member_._0'"
	 * @generated
	 */
	EDataType getStatusTypeMember0Object();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Status Type Member1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Status Type Member1</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='Status_._type_._member_._1' baseType='http://www.eclipse.org/emf/2003/XMLType#string'"
	 * @generated
	 */
	EDataType getStatusTypeMember1();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Wps20Factory getWps20Factory();

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
		 * The meta object literal for the '{@link net.opengis.wps20.impl.BodyReferenceTypeImpl <em>Body Reference Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.BodyReferenceTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getBodyReferenceType()
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
		 * The meta object literal for the '{@link net.opengis.wps20.impl.BoundingBoxDataTypeImpl <em>Bounding Box Data Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.BoundingBoxDataTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getBoundingBoxDataType()
		 * @generated
		 */
		EClass BOUNDING_BOX_DATA_TYPE = eINSTANCE.getBoundingBoxDataType();

		/**
		 * The meta object literal for the '<em><b>Supported CRS</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BOUNDING_BOX_DATA_TYPE__SUPPORTED_CRS = eINSTANCE.getBoundingBoxDataType_SupportedCRS();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ComplexDataTypeImpl <em>Complex Data Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ComplexDataTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getComplexDataType()
		 * @generated
		 */
		EClass COMPLEX_DATA_TYPE = eINSTANCE.getComplexDataType();

		/**
		 * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPLEX_DATA_TYPE__ANY = eINSTANCE.getComplexDataType_Any();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ContentsTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getContentsType()
		 * @generated
		 */
		EClass CONTENTS_TYPE = eINSTANCE.getContentsType();

		/**
		 * The meta object literal for the '<em><b>Process Summary</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONTENTS_TYPE__PROCESS_SUMMARY = eINSTANCE.getContentsType_ProcessSummary();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.DataDescriptionTypeImpl <em>Data Description Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.DataDescriptionTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataDescriptionType()
		 * @generated
		 */
		EClass DATA_DESCRIPTION_TYPE = eINSTANCE.getDataDescriptionType();

		/**
		 * The meta object literal for the '<em><b>Format</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_DESCRIPTION_TYPE__FORMAT = eINSTANCE.getDataDescriptionType_Format();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.DataInputTypeImpl <em>Data Input Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.DataInputTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataInputType()
		 * @generated
		 */
		EClass DATA_INPUT_TYPE = eINSTANCE.getDataInputType();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_INPUT_TYPE__DATA = eINSTANCE.getDataInputType_Data();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_INPUT_TYPE__REFERENCE = eINSTANCE.getDataInputType_Reference();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_INPUT_TYPE__INPUT = eINSTANCE.getDataInputType_Input();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_INPUT_TYPE__ID = eINSTANCE.getDataInputType_Id();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.DataOutputTypeImpl <em>Data Output Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.DataOutputTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataOutputType()
		 * @generated
		 */
		EClass DATA_OUTPUT_TYPE = eINSTANCE.getDataOutputType();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_OUTPUT_TYPE__DATA = eINSTANCE.getDataOutputType_Data();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_OUTPUT_TYPE__REFERENCE = eINSTANCE.getDataOutputType_Reference();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_OUTPUT_TYPE__OUTPUT = eINSTANCE.getDataOutputType_Output();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_OUTPUT_TYPE__ID = eINSTANCE.getDataOutputType_Id();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.DataTypeImpl <em>Data Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.DataTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataType()
		 * @generated
		 */
		EClass DATA_TYPE = eINSTANCE.getDataType();

		/**
		 * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_TYPE__ENCODING = eINSTANCE.getDataType_Encoding();

		/**
		 * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_TYPE__MIME_TYPE = eINSTANCE.getDataType_MimeType();

		/**
		 * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_TYPE__SCHEMA = eINSTANCE.getDataType_Schema();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.DescribeProcessTypeImpl <em>Describe Process Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.DescribeProcessTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDescribeProcessType()
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
		 * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DESCRIBE_PROCESS_TYPE__LANG = eINSTANCE.getDescribeProcessType_Lang();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.DescriptionTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDescriptionType()
		 * @generated
		 */
		EClass DESCRIPTION_TYPE = eINSTANCE.getDescriptionType();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.DismissTypeImpl <em>Dismiss Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.DismissTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDismissType()
		 * @generated
		 */
		EClass DISMISS_TYPE = eINSTANCE.getDismissType();

		/**
		 * The meta object literal for the '<em><b>Job ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISMISS_TYPE__JOB_ID = eINSTANCE.getDismissType_JobID();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.DocumentRootImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDocumentRoot()
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
		 * The meta object literal for the '<em><b>Bounding Box Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__BOUNDING_BOX_DATA = eINSTANCE.getDocumentRoot_BoundingBoxData();

		/**
		 * The meta object literal for the '<em><b>Data Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DATA_DESCRIPTION = eINSTANCE.getDocumentRoot_DataDescription();

		/**
		 * The meta object literal for the '<em><b>Capabilities</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__CAPABILITIES = eINSTANCE.getDocumentRoot_Capabilities();

		/**
		 * The meta object literal for the '<em><b>Complex Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__COMPLEX_DATA = eINSTANCE.getDocumentRoot_ComplexData();

		/**
		 * The meta object literal for the '<em><b>Contents</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__CONTENTS = eINSTANCE.getDocumentRoot_Contents();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DATA = eINSTANCE.getDocumentRoot_Data();

		/**
		 * The meta object literal for the '<em><b>Describe Process</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DESCRIBE_PROCESS = eINSTANCE.getDocumentRoot_DescribeProcess();

		/**
		 * The meta object literal for the '<em><b>Dismiss</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DISMISS = eINSTANCE.getDocumentRoot_Dismiss();

		/**
		 * The meta object literal for the '<em><b>Execute</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__EXECUTE = eINSTANCE.getDocumentRoot_Execute();

		/**
		 * The meta object literal for the '<em><b>Expiration Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__EXPIRATION_DATE = eINSTANCE.getDocumentRoot_ExpirationDate();

		/**
		 * The meta object literal for the '<em><b>Format</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__FORMAT = eINSTANCE.getDocumentRoot_Format();

		/**
		 * The meta object literal for the '<em><b>Generic Process</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GENERIC_PROCESS = eINSTANCE.getDocumentRoot_GenericProcess();

		/**
		 * The meta object literal for the '<em><b>Get Capabilities</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GET_CAPABILITIES = eINSTANCE.getDocumentRoot_GetCapabilities();

		/**
		 * The meta object literal for the '<em><b>Get Result</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GET_RESULT = eINSTANCE.getDocumentRoot_GetResult();

		/**
		 * The meta object literal for the '<em><b>Get Status</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GET_STATUS = eINSTANCE.getDocumentRoot_GetStatus();

		/**
		 * The meta object literal for the '<em><b>Job ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__JOB_ID = eINSTANCE.getDocumentRoot_JobID();

		/**
		 * The meta object literal for the '<em><b>Literal Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__LITERAL_DATA = eINSTANCE.getDocumentRoot_LiteralData();

		/**
		 * The meta object literal for the '<em><b>Literal Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__LITERAL_VALUE = eINSTANCE.getDocumentRoot_LiteralValue();

		/**
		 * The meta object literal for the '<em><b>Process</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__PROCESS = eINSTANCE.getDocumentRoot_Process();

		/**
		 * The meta object literal for the '<em><b>Process Offering</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__PROCESS_OFFERING = eINSTANCE.getDocumentRoot_ProcessOffering();

		/**
		 * The meta object literal for the '<em><b>Process Offerings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__PROCESS_OFFERINGS = eINSTANCE.getDocumentRoot_ProcessOfferings();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__REFERENCE = eINSTANCE.getDocumentRoot_Reference();

		/**
		 * The meta object literal for the '<em><b>Result</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__RESULT = eINSTANCE.getDocumentRoot_Result();

		/**
		 * The meta object literal for the '<em><b>Status Info</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__STATUS_INFO = eINSTANCE.getDocumentRoot_StatusInfo();

		/**
		 * The meta object literal for the '<em><b>Supported CRS</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__SUPPORTED_CRS = eINSTANCE.getDocumentRoot_SupportedCRS();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ExecuteRequestTypeImpl <em>Execute Request Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ExecuteRequestTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getExecuteRequestType()
		 * @generated
		 */
		EClass EXECUTE_REQUEST_TYPE = eINSTANCE.getExecuteRequestType();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTE_REQUEST_TYPE__IDENTIFIER = eINSTANCE.getExecuteRequestType_Identifier();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTE_REQUEST_TYPE__INPUT = eINSTANCE.getExecuteRequestType_Input();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTE_REQUEST_TYPE__OUTPUT = eINSTANCE.getExecuteRequestType_Output();

		/**
		 * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXECUTE_REQUEST_TYPE__MODE = eINSTANCE.getExecuteRequestType_Mode();

		/**
		 * The meta object literal for the '<em><b>Response</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXECUTE_REQUEST_TYPE__RESPONSE = eINSTANCE.getExecuteRequestType_Response();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ExtensionTypeImpl <em>Extension Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ExtensionTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getExtensionType()
		 * @generated
		 */
		EClass EXTENSION_TYPE = eINSTANCE.getExtensionType();

		/**
		 * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXTENSION_TYPE__ANY = eINSTANCE.getExtensionType_Any();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.FormatTypeImpl <em>Format Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.FormatTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getFormatType()
		 * @generated
		 */
		EClass FORMAT_TYPE = eINSTANCE.getFormatType();

		/**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FORMAT_TYPE__DEFAULT = eINSTANCE.getFormatType_Default();

		/**
		 * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FORMAT_TYPE__ENCODING = eINSTANCE.getFormatType_Encoding();

		/**
		 * The meta object literal for the '<em><b>Maximum Megabytes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FORMAT_TYPE__MAXIMUM_MEGABYTES = eINSTANCE.getFormatType_MaximumMegabytes();

		/**
		 * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FORMAT_TYPE__MIME_TYPE = eINSTANCE.getFormatType_MimeType();

		/**
		 * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FORMAT_TYPE__SCHEMA = eINSTANCE.getFormatType_Schema();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.GenericInputTypeImpl <em>Generic Input Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.GenericInputTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGenericInputType()
		 * @generated
		 */
		EClass GENERIC_INPUT_TYPE = eINSTANCE.getGenericInputType();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERIC_INPUT_TYPE__INPUT = eINSTANCE.getGenericInputType_Input();

		/**
		 * The meta object literal for the '<em><b>Max Occurs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERIC_INPUT_TYPE__MAX_OCCURS = eINSTANCE.getGenericInputType_MaxOccurs();

		/**
		 * The meta object literal for the '<em><b>Min Occurs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERIC_INPUT_TYPE__MIN_OCCURS = eINSTANCE.getGenericInputType_MinOccurs();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.GenericOutputTypeImpl <em>Generic Output Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.GenericOutputTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGenericOutputType()
		 * @generated
		 */
		EClass GENERIC_OUTPUT_TYPE = eINSTANCE.getGenericOutputType();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERIC_OUTPUT_TYPE__OUTPUT = eINSTANCE.getGenericOutputType_Output();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.GenericProcessTypeImpl <em>Generic Process Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.GenericProcessTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGenericProcessType()
		 * @generated
		 */
		EClass GENERIC_PROCESS_TYPE = eINSTANCE.getGenericProcessType();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERIC_PROCESS_TYPE__INPUT = eINSTANCE.getGenericProcessType_Input();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERIC_PROCESS_TYPE__OUTPUT = eINSTANCE.getGenericProcessType_Output();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.GetCapabilitiesTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGetCapabilitiesType()
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
		 * The meta object literal for the '{@link net.opengis.wps20.impl.GetResultTypeImpl <em>Get Result Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.GetResultTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGetResultType()
		 * @generated
		 */
		EClass GET_RESULT_TYPE = eINSTANCE.getGetResultType();

		/**
		 * The meta object literal for the '<em><b>Job ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GET_RESULT_TYPE__JOB_ID = eINSTANCE.getGetResultType_JobID();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.GetStatusTypeImpl <em>Get Status Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.GetStatusTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getGetStatusType()
		 * @generated
		 */
		EClass GET_STATUS_TYPE = eINSTANCE.getGetStatusType();

		/**
		 * The meta object literal for the '<em><b>Job ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GET_STATUS_TYPE__JOB_ID = eINSTANCE.getGetStatusType_JobID();

		/**
		 * The meta object literal for the '{@link net.opengis.net.opengis.wps20.impl.InputDescriptionTypeImpl <em>Input Description Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.net.opengis.wps20.impl.InputDescriptionTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getInputDescriptionType()
		 * @generated
		 */
		EClass INPUT_DESCRIPTION_TYPE = eINSTANCE.getInputDescriptionType();

		/**
		 * The meta object literal for the '<em><b>Data Description Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP = eINSTANCE.getInputDescriptionType_DataDescriptionGroup();

		/**
		 * The meta object literal for the '<em><b>Data Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION = eINSTANCE.getInputDescriptionType_DataDescription();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_DESCRIPTION_TYPE__INPUT = eINSTANCE.getInputDescriptionType_Input();

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
		 * The meta object literal for the '{@link net.opengis.wps20.impl.LiteralDataDomainTypeImpl <em>Literal Data Domain Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.LiteralDataDomainTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getLiteralDataDomainType()
		 * @generated
		 */
		EClass LITERAL_DATA_DOMAIN_TYPE = eINSTANCE.getLiteralDataDomainType();

		/**
		 * The meta object literal for the '<em><b>Allowed Values</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LITERAL_DATA_DOMAIN_TYPE__ALLOWED_VALUES = eINSTANCE.getLiteralDataDomainType_AllowedValues();

		/**
		 * The meta object literal for the '<em><b>Any Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LITERAL_DATA_DOMAIN_TYPE__ANY_VALUE = eINSTANCE.getLiteralDataDomainType_AnyValue();

		/**
		 * The meta object literal for the '<em><b>Values Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LITERAL_DATA_DOMAIN_TYPE__VALUES_REFERENCE = eINSTANCE.getLiteralDataDomainType_ValuesReference();

		/**
		 * The meta object literal for the '<em><b>Data Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LITERAL_DATA_DOMAIN_TYPE__DATA_TYPE = eINSTANCE.getLiteralDataDomainType_DataType();

		/**
		 * The meta object literal for the '<em><b>UOM</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LITERAL_DATA_DOMAIN_TYPE__UOM = eINSTANCE.getLiteralDataDomainType_UOM();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LITERAL_DATA_DOMAIN_TYPE__DEFAULT_VALUE = eINSTANCE.getLiteralDataDomainType_DefaultValue();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.LiteralDataDomainType1Impl <em>Literal Data Domain Type1</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.LiteralDataDomainType1Impl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getLiteralDataDomainType1()
		 * @generated
		 */
		EClass LITERAL_DATA_DOMAIN_TYPE1 = eINSTANCE.getLiteralDataDomainType1();

		/**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERAL_DATA_DOMAIN_TYPE1__DEFAULT = eINSTANCE.getLiteralDataDomainType1_Default();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.LiteralDataTypeImpl <em>Literal Data Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.LiteralDataTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getLiteralDataType()
		 * @generated
		 */
		EClass LITERAL_DATA_TYPE = eINSTANCE.getLiteralDataType();

		/**
		 * The meta object literal for the '<em><b>Literal Data Domain</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LITERAL_DATA_TYPE__LITERAL_DATA_DOMAIN = eINSTANCE.getLiteralDataType_LiteralDataDomain();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.LiteralValueTypeImpl <em>Literal Value Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.LiteralValueTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getLiteralValueType()
		 * @generated
		 */
		EClass LITERAL_VALUE_TYPE = eINSTANCE.getLiteralValueType();

		/**
		 * The meta object literal for the '<em><b>Data Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERAL_VALUE_TYPE__DATA_TYPE = eINSTANCE.getLiteralValueType_DataType();

		/**
		 * The meta object literal for the '<em><b>Uom</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERAL_VALUE_TYPE__UOM = eINSTANCE.getLiteralValueType_Uom();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.OutputDefinitionTypeImpl <em>Output Definition Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.OutputDefinitionTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getOutputDefinitionType()
		 * @generated
		 */
		EClass OUTPUT_DEFINITION_TYPE = eINSTANCE.getOutputDefinitionType();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_DEFINITION_TYPE__OUTPUT = eINSTANCE.getOutputDefinitionType_Output();

		/**
		 * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_DEFINITION_TYPE__ENCODING = eINSTANCE.getOutputDefinitionType_Encoding();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_DEFINITION_TYPE__ID = eINSTANCE.getOutputDefinitionType_Id();

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
		 * The meta object literal for the '<em><b>Transmission</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_DEFINITION_TYPE__TRANSMISSION = eINSTANCE.getOutputDefinitionType_Transmission();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.OutputDescriptionTypeImpl <em>Output Description Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.OutputDescriptionTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getOutputDescriptionType()
		 * @generated
		 */
		EClass OUTPUT_DESCRIPTION_TYPE = eINSTANCE.getOutputDescriptionType();

		/**
		 * The meta object literal for the '<em><b>Data Description Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION_GROUP = eINSTANCE.getOutputDescriptionType_DataDescriptionGroup();

		/**
		 * The meta object literal for the '<em><b>Data Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION = eINSTANCE.getOutputDescriptionType_DataDescription();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_DESCRIPTION_TYPE__OUTPUT = eINSTANCE.getOutputDescriptionType_Output();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ProcessDescriptionTypeImpl <em>Process Description Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ProcessDescriptionTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getProcessDescriptionType()
		 * @generated
		 */
		EClass PROCESS_DESCRIPTION_TYPE = eINSTANCE.getProcessDescriptionType();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESS_DESCRIPTION_TYPE__INPUT = eINSTANCE.getProcessDescriptionType_Input();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESS_DESCRIPTION_TYPE__OUTPUT = eINSTANCE.getProcessDescriptionType_Output();

		/**
		 * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_DESCRIPTION_TYPE__LANG = eINSTANCE.getProcessDescriptionType_Lang();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ProcessOfferingsTypeImpl <em>Process Offerings Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ProcessOfferingsTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getProcessOfferingsType()
		 * @generated
		 */
		EClass PROCESS_OFFERINGS_TYPE = eINSTANCE.getProcessOfferingsType();

		/**
		 * The meta object literal for the '<em><b>Process Offering</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESS_OFFERINGS_TYPE__PROCESS_OFFERING = eINSTANCE.getProcessOfferingsType_ProcessOffering();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ProcessOfferingTypeImpl <em>Process Offering Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ProcessOfferingTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getProcessOfferingType()
		 * @generated
		 */
		EClass PROCESS_OFFERING_TYPE = eINSTANCE.getProcessOfferingType();

		/**
		 * The meta object literal for the '<em><b>Process</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESS_OFFERING_TYPE__PROCESS = eINSTANCE.getProcessOfferingType_Process();

		/**
		 * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_OFFERING_TYPE__ANY = eINSTANCE.getProcessOfferingType_Any();

		/**
		 * The meta object literal for the '<em><b>Job Control Options</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_OFFERING_TYPE__JOB_CONTROL_OPTIONS = eINSTANCE.getProcessOfferingType_JobControlOptions();

		/**
		 * The meta object literal for the '<em><b>Output Transmission</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_OFFERING_TYPE__OUTPUT_TRANSMISSION = eINSTANCE.getProcessOfferingType_OutputTransmission();

		/**
		 * The meta object literal for the '<em><b>Process Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_OFFERING_TYPE__PROCESS_MODEL = eINSTANCE.getProcessOfferingType_ProcessModel();

		/**
		 * The meta object literal for the '<em><b>Process Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_OFFERING_TYPE__PROCESS_VERSION = eINSTANCE.getProcessOfferingType_ProcessVersion();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ProcessSummaryTypeImpl <em>Process Summary Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ProcessSummaryTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getProcessSummaryType()
		 * @generated
		 */
		EClass PROCESS_SUMMARY_TYPE = eINSTANCE.getProcessSummaryType();

		/**
		 * The meta object literal for the '<em><b>Job Control Options</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_SUMMARY_TYPE__JOB_CONTROL_OPTIONS = eINSTANCE.getProcessSummaryType_JobControlOptions();

		/**
		 * The meta object literal for the '<em><b>Output Transmission</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_SUMMARY_TYPE__OUTPUT_TRANSMISSION = eINSTANCE.getProcessSummaryType_OutputTransmission();

		/**
		 * The meta object literal for the '<em><b>Process Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_SUMMARY_TYPE__PROCESS_MODEL = eINSTANCE.getProcessSummaryType_ProcessModel();

		/**
		 * The meta object literal for the '<em><b>Process Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_SUMMARY_TYPE__PROCESS_VERSION = eINSTANCE.getProcessSummaryType_ProcessVersion();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ReferenceTypeImpl <em>Reference Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ReferenceTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getReferenceType()
		 * @generated
		 */
		EClass REFERENCE_TYPE = eINSTANCE.getReferenceType();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_TYPE__BODY = eINSTANCE.getReferenceType_Body();

		/**
		 * The meta object literal for the '<em><b>Body Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_TYPE__BODY_REFERENCE = eINSTANCE.getReferenceType_BodyReference();

		/**
		 * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__ENCODING = eINSTANCE.getReferenceType_Encoding();

		/**
		 * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__HREF = eINSTANCE.getReferenceType_Href();

		/**
		 * The meta object literal for the '<em><b>Mime Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__MIME_TYPE = eINSTANCE.getReferenceType_MimeType();

		/**
		 * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__SCHEMA = eINSTANCE.getReferenceType_Schema();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.RequestBaseTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getRequestBaseType()
		 * @generated
		 */
		EClass REQUEST_BASE_TYPE = eINSTANCE.getRequestBaseType();

		/**
		 * The meta object literal for the '<em><b>Extension</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUEST_BASE_TYPE__EXTENSION = eINSTANCE.getRequestBaseType_Extension();

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
		 * The meta object literal for the '{@link net.opengis.wps20.impl.ResultTypeImpl <em>Result Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.ResultTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getResultType()
		 * @generated
		 */
		EClass RESULT_TYPE = eINSTANCE.getResultType();

		/**
		 * The meta object literal for the '<em><b>Job ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_TYPE__JOB_ID = eINSTANCE.getResultType_JobID();

		/**
		 * The meta object literal for the '<em><b>Expiration Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_TYPE__EXPIRATION_DATE = eINSTANCE.getResultType_ExpirationDate();

		/**
		 * The meta object literal for the '<em><b>Output</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESULT_TYPE__OUTPUT = eINSTANCE.getResultType_Output();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.StatusInfoTypeImpl <em>Status Info Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.StatusInfoTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusInfoType()
		 * @generated
		 */
		EClass STATUS_INFO_TYPE = eINSTANCE.getStatusInfoType();

		/**
		 * The meta object literal for the '<em><b>Job ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATUS_INFO_TYPE__JOB_ID = eINSTANCE.getStatusInfoType_JobID();

		/**
		 * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATUS_INFO_TYPE__STATUS = eINSTANCE.getStatusInfoType_Status();

		/**
		 * The meta object literal for the '<em><b>Expiration Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATUS_INFO_TYPE__EXPIRATION_DATE = eINSTANCE.getStatusInfoType_ExpirationDate();

		/**
		 * The meta object literal for the '<em><b>Estimated Completion</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATUS_INFO_TYPE__ESTIMATED_COMPLETION = eINSTANCE.getStatusInfoType_EstimatedCompletion();

		/**
		 * The meta object literal for the '<em><b>Next Poll</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATUS_INFO_TYPE__NEXT_POLL = eINSTANCE.getStatusInfoType_NextPoll();

		/**
		 * The meta object literal for the '<em><b>Percent Completed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATUS_INFO_TYPE__PERCENT_COMPLETED = eINSTANCE.getStatusInfoType_PercentCompleted();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.SupportedCRSTypeImpl <em>Supported CRS Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.SupportedCRSTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getSupportedCRSType()
		 * @generated
		 */
		EClass SUPPORTED_CRS_TYPE = eINSTANCE.getSupportedCRSType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUPPORTED_CRS_TYPE__VALUE = eINSTANCE.getSupportedCRSType_Value();

		/**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUPPORTED_CRS_TYPE__DEFAULT = eINSTANCE.getSupportedCRSType_Default();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.impl.WPSCapabilitiesTypeImpl <em>WPS Capabilities Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.impl.WPSCapabilitiesTypeImpl
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getWPSCapabilitiesType()
		 * @generated
		 */
		EClass WPS_CAPABILITIES_TYPE = eINSTANCE.getWPSCapabilitiesType();

		/**
		 * The meta object literal for the '<em><b>Contents</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WPS_CAPABILITIES_TYPE__CONTENTS = eINSTANCE.getWPSCapabilitiesType_Contents();

		/**
		 * The meta object literal for the '<em><b>Extension</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WPS_CAPABILITIES_TYPE__EXTENSION = eINSTANCE.getWPSCapabilitiesType_Extension();

		/**
		 * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WPS_CAPABILITIES_TYPE__SERVICE = eINSTANCE.getWPSCapabilitiesType_Service();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.DataTransmissionModeType <em>Data Transmission Mode Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.DataTransmissionModeType
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataTransmissionModeType()
		 * @generated
		 */
		EEnum DATA_TRANSMISSION_MODE_TYPE = eINSTANCE.getDataTransmissionModeType();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.JobControlOptionsTypeMember0 <em>Job Control Options Type Member0</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.JobControlOptionsTypeMember0
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsTypeMember0()
		 * @generated
		 */
		EEnum JOB_CONTROL_OPTIONS_TYPE_MEMBER0 = eINSTANCE.getJobControlOptionsTypeMember0();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.ModeType <em>Mode Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.ModeType
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getModeType()
		 * @generated
		 */
		EEnum MODE_TYPE = eINSTANCE.getModeType();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.ResponseType <em>Response Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.ResponseType
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getResponseType()
		 * @generated
		 */
		EEnum RESPONSE_TYPE = eINSTANCE.getResponseType();

		/**
		 * The meta object literal for the '{@link net.opengis.wps20.StatusTypeMember0 <em>Status Type Member0</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.StatusTypeMember0
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusTypeMember0()
		 * @generated
		 */
		EEnum STATUS_TYPE_MEMBER0 = eINSTANCE.getStatusTypeMember0();

		/**
		 * The meta object literal for the '<em>Data Transmission Mode Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.DataTransmissionModeType
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getDataTransmissionModeTypeObject()
		 * @generated
		 */
		EDataType DATA_TRANSMISSION_MODE_TYPE_OBJECT = eINSTANCE.getDataTransmissionModeTypeObject();

		/**
		 * The meta object literal for the '<em>Job Control Options Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsType()
		 * @generated
		 */
		EDataType JOB_CONTROL_OPTIONS_TYPE = eINSTANCE.getJobControlOptionsType();

		/**
		 * The meta object literal for the '<em>Job Control Options Type1</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsType1()
		 * @generated
		 */
		EDataType JOB_CONTROL_OPTIONS_TYPE1 = eINSTANCE.getJobControlOptionsType1();

		/**
		 * The meta object literal for the '<em>Job Control Options Type Member0 Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.JobControlOptionsTypeMember0
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsTypeMember0Object()
		 * @generated
		 */
		EDataType JOB_CONTROL_OPTIONS_TYPE_MEMBER0_OBJECT = eINSTANCE.getJobControlOptionsTypeMember0Object();

		/**
		 * The meta object literal for the '<em>Job Control Options Type Member1</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getJobControlOptionsTypeMember1()
		 * @generated
		 */
		EDataType JOB_CONTROL_OPTIONS_TYPE_MEMBER1 = eINSTANCE.getJobControlOptionsTypeMember1();

		/**
		 * The meta object literal for the '<em>Mode Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.ModeType
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getModeTypeObject()
		 * @generated
		 */
		EDataType MODE_TYPE_OBJECT = eINSTANCE.getModeTypeObject();

		/**
		 * The meta object literal for the '<em>Output Transmission Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getOutputTransmissionType()
		 * @generated
		 */
		EDataType OUTPUT_TRANSMISSION_TYPE = eINSTANCE.getOutputTransmissionType();

		/**
		 * The meta object literal for the '<em>Percent Completed Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.math.BigInteger
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getPercentCompletedType()
		 * @generated
		 */
		EDataType PERCENT_COMPLETED_TYPE = eINSTANCE.getPercentCompletedType();

		/**
		 * The meta object literal for the '<em>Response Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.ResponseType
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getResponseTypeObject()
		 * @generated
		 */
		EDataType RESPONSE_TYPE_OBJECT = eINSTANCE.getResponseTypeObject();

		/**
		 * The meta object literal for the '<em>Status Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusType()
		 * @generated
		 */
		EDataType STATUS_TYPE = eINSTANCE.getStatusType();

		/**
		 * The meta object literal for the '<em>Status Type Member0 Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.wps20.StatusTypeMember0
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusTypeMember0Object()
		 * @generated
		 */
		EDataType STATUS_TYPE_MEMBER0_OBJECT = eINSTANCE.getStatusTypeMember0Object();

		/**
		 * The meta object literal for the '<em>Status Type Member1</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see net.opengis.wps20.impl.Wps20PackageImpl#getStatusTypeMember1()
		 * @generated
		 */
		EDataType STATUS_TYPE_MEMBER1 = eINSTANCE.getStatusTypeMember1();

	}

} //Wps20Package
