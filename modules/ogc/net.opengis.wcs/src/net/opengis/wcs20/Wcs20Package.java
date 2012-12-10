/**
 */
package net.opengis.wcs20;

import net.opengis.ows20.Ows20Package;

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
 * This XML Schema Document defines the GetCapabilities operation request and response XML elements and types, used by the OGC Web Coverage Service (WCS).
 *             Last updated: 2012-feb-06
 *             Copyright (c) 2012 Open Geospatial Consortium.
 *             To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 
 * This XML Schema Document defines the XML elements and types that are shared by multiple WCS operations.
 *             Last updated: 2012-feb-06
 *             Copyright (c) 2012 Open Geospatial Consortium.
 *             To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 
 * This XML Schema Document includes, directly and indirectly, all the XML Schema Documents defined by the OGC Web Coverage Service (WCS).
 *             Last updated: 2012-feb-06
 *             Copyright (c) 2012 Open Geospatial Consortium.
 *             To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 
 * This is the XML Schema for the GML 3.2.1 Application Schema for Coverages.
 *             This XML Schema Document includes, directly and indirectly, all the XML Schema Documents defined by the this standard.
 *             Last updated: 2012-Jul-10
 *             Copyright (c) 2012 Open Geospatial Consortium.
 *             To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 
 * Component of GML 3.2.1 Application Schema for Coverages.
 *         Last updated: 2012-Jul-10
 *         Copyright (c) 2012 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 
 * This XML Schema Document encodes extensions to GML 3.2.1 for grids that are referenced by a Transformation, named "ReferencedGridByTransformation". This document also extends the alternatives allowed in gml:ParameterValueType as expected to be needed by such Transformations.
 *         Last updated: 2012-Jul-10
 *         Copyright (c) 2012 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/.
 * 
 * 
 * 			GML is an OGC Standard.
 * 			Copyright (c) 2007,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 15.6.
 * A number of types and relationships are defined to represent the time-varying properties of geographic features.
 * In a comprehensive treatment of spatiotemporal modeling, Langran (see Bibliography) distinguished three principal temporal entities: states, events, and evidence; the schema specified in the following Subclauses incorporates elements for each.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 Clause 14.
 * Topology is the branch of mathematics describing the properties of objects which are invariant under continuous deformation. For example, a circle is topologically equivalent to an ellipse because one can be transformed into the other by stretching. In geographic modelling, the foremost use of topology is in accelerating computational geometry. The constructs of topology allow characterisation of the spatial relationships between objects using simple combinatorial or algebraic algorithms. Topology, realised by the appropriate geometry, also allows a compact and unambiguous mechanism for expressing shared geometry among geographic features.
 * There are four instantiable classes of primitive topology objects, one for each dimension up to 3D. In addition, topological complexes are supported, too.
 * There is strong symmetry in the (topological boundary and coboundary) relationships between topology primitives of adjacent dimensions. Topology primitives are bounded by directed primitives of one lower dimension. The coboundary of each topology primitive is formed from directed topology primitives of one higher dimension.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 20.3.
 * A coverage incorporates a mapping from a spatiotemporal domain to a range set, the latter providing the set in which the attribute values live.  The range set may be an arbitrary set including discrete lists, integer or floating point ranges, and multi-dimensional vector spaces.
 * A coverage can be viewed as the graph of the coverage function f:A à B, that is as the set of ordered pairs {(x, f(x)) | where x is in A}. This view is especially applicable to the GML encoding of a coverage.  In the case of a discrete coverage, the domain set A is partitioned into a collection of subsets (typically a disjoint collection) A = UAi and the function f is constant on each Ai. For a spatial domain, the Ai are geometry elements, hence the coverage can be viewed as a collection of (geometry,value) pairs, where the value is an element of the range set.  If the spatial domain A is a topological space then the coverage can be viewed as a collection of (topology,value) pairs, where the topology element in the pair is a topological n-chain (in GML terms this is a gml:TopoPoint, gml:TopoCurve, gml:TopoSurface or gml:TopoSolid).
 * A coverage is implemented as a GML feature. We can thus speak of a "temperature distribution feature", or a "remotely sensed image feature", or a "soil distribution feature".
 * As is the case for any GML object, a coverage object may also be the value of a property of a feature.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 13.3.
 * The spatial-temporal coordinate reference systems schema components are divided into two logical parts. One part defines elements and types for XML encoding of abstract coordinate reference systems definitions. The larger part defines specialized constructs for XML encoding of definitions of the multiple concrete types of spatial-temporal coordinate reference systems.
 * These schema components encode the Coordinate Reference System packages of the UML Models of ISO 19111 Clause 8 and ISO/DIS 19136 D.3.10, with the exception of the abstract "SC_CRS" class.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 Clause 19.
 * A GML observation models the act of observing, often with a camera, a person or some form of instrument.  An observation feature describes the "metadata" associated with an information capture event, together with a value for the result of the observation.  This covers a broad range of cases, from a tourist photo (not the photo but the act of taking the photo), to images acquired by space borne sensors or the measurement of a temperature 5 meters below the surfaces of a lake.
 * The basic structures introduced in this schema are intended to serve as the foundation for more comprehensive schemas for scientific, technical and engineering measurement schemas.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 15.5.
 * A value in the time domain is measured relative to a temporal reference system. Common types of reference systems include calendars, ordinal temporal reference systems, and temporal coordinate systems (time elapsed since some epoch).  The primary temporal reference system for use with geographic information is the Gregorian Calendar and 24 hour local or Coordinated Universal Time (UTC), but special applications may entail the use of alternative reference systems.  The Julian day numbering system is a temporal coordinate system that has an origin earlier than any known calendar, at noon on 1 January 4713 BC in the Julian proleptic calendar, and is useful in transformations between dates in different calendars.
 * In GML seven concrete elements are used to describe temporal reference systems: gml:TimeReferenceSystem, gml:TimeCoordinateSystem, gml:TimeCalendar, gml:TimeCalendarEra, gml:TimeClock, gml:TimeOrdinalReferenceSystem, and gml:TimeOrdinalEra.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * All global schema components that are part of the GML schema, but were deprecated. See Annex I.
 * 
 * 			GML is an OGC Standard.
 * 			Copyright (c) 2007,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 15.3.
 * Temporal topology is described in terms of time complexes, nodes, and edges, and the connectivity between these. Temporal topology does not directly provide information about temporal position. It is used in the case of describing a lineage or a history (e.g. a family tree expressing evolution of species, an ecological cycle, a lineage of lands or buildings, or a history of separation and merger of administrative boundaries). The following Subclauses specifies the temporal topology as temporal characteristics of features in compliance with ISO 19108.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 Clause 16.
 * Many applications require definitions of terms which are used within instance documents as the values of certain properties or as reference information to tie properties to standard information values in some way.  Units of measure and descriptions of measurable phenomena are two particular examples.
 * It will often be convenient to use definitions provided by external authorities. These may already be packaged for delivery in various ways, both online and offline. In order that they may be referred to from GML documents it is generally necessary that a URI be available for each definition. Where this is the case then it is usually preferable to refer to these directly.
 * Alternatively, it may be convenient or necessary to capture definitions in XML, either embedded within an instance document containing features or as a separate document. The definitions may be transcriptions from an external source, or may be new definitions for a local purpose. In order to support this case, some simple components are provided in GML in the form of
 * -	a generic gml:Definition, which may serve as the basis for more specialized definitions
 * -	a generic gml:Dictionary, which allows a set of definitions or references to definitions to be collected
 * These components may be used directly, but also serve as the basis for more specialised definition elements in GML, in particular: coordinate operations, coordinate reference systems, datums, temporal reference systems, and units of measure.
 * Note that the GML definition and dictionary components implement a simple nested hierarchy of definitions with identifiers. The latter provide handles which may be used in the description of more complex relationships between terms. However, the GML dictionary components are not intended to provide direct support for complex taxonomies, ontologies or thesauri.  Specialised XML tools are available to satisfy the more sophisticated requirements.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 7.2.
 * The gmlBase schema components establish the GML model and syntax, in particular
 * -	a root XML type from which XML types for all GML objects should be derived,
 * -	a pattern and components for GML properties,
 * -	patterns for collections and arrays, and components for generic collections and arrays,
 * -	components for associating metadata with GML objects,
 * -	components for constructing definitions and dictionaries.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 15.2.
 * The GML temporal schemas include components for describing temporal geometry and topology, temporal reference systems, and the temporal characteristics of geographic data. The model underlying the representation constitutes a profile of the conceptual schema described in ISO 19108. The underlying spatiotemporal model strives to accommodate both feature-level and attribute-level time stamping; basic support for tracking moving objects is also included.
 * Time is measured on two types of scales: interval and ordinal.  An interval scale offers a basis for measuring duration, an ordinal scale provides information only about relative position in time.
 * Two other ISO standards are relevant to describing temporal objects:  ISO 8601 describes encodings for time instants and time periods, as text strings with particular structure and punctuation; ISO 11404 provides a detailed description of time intervals as part of a general discussion of language independent datatypes.
 * The temporal schemas cover two interrelated topics and provide basic schema components for representing temporal instants and periods, temporal topology, and reference systems; more specialized schema components defines components used for dynamic features. Instances of temporal geometric types are used as values for the temporal properties of geographic features.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 Clause 9.
 * A GML feature is a (representation of a) identifiable real-world object in a selected domain of discourse. The feature schema provides a framework for the creation of GML features and feature collections.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 Clause 18.
 * The direction schema components provide the GML Application Schema developer with a standard property element to describe direction, and associated objects that may be used to express orientation, direction, heading, bearing or other directional aspects of geographic features.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 17.5.
 * The elements declared in this Clause build on other GML schema components, in particular gml:AbstractTimeObject, gml:AbstractGeometry, and the following types:  gml:MeasureType, gml:MeasureListType, gml:CodeType, gml:CodeOrNilReasonListType, gml:BooleanOrNilReasonListType, gml:IntegerOrNilReasonList.
 * Of particular interest are elements that are the heads of substitution groups, and one named choice group. These are the primary reasons for the value objects schema, since they may act as variables in the definition of content models, such as Observations, when it is desired to permit alternative value types to occur some of which may have complex content such as arrays, geometry and time objects, and where it is useful not to prescribe the actual value type in advance. The members of the groups include quantities, category classifications, boolean, count, temporal and spatial values, and aggregates of these.
 * The value objects are defined in a hierarchy. The following relationships are defined:
 * -	Concrete elements gml:Quantity, gml:Category, gml:Count and gml:Boolean are substitutable for the abstract element gml:AbstractScalarValue.
 * -	Concrete elements gml:QuantityList, gml:CategoryList, gml:CountList and gml:BooleanList are substitutable for the abstract element gml:AbstractScalarValueList.
 * -	Concrete element gml:ValueArray is substitutable for the concrete element gml:CompositeValue.
 * -	Abstract elements gml:AbstractScalarValue and gml:AbstractScalarValueList, and concrete elements gml:CompositeValue, gml:ValueExtent, gml:CategoryExtent, gml:CountExtent and gml:QuantityExtent are substitutable for abstract element gml:AbstractValue.
 * -	Abstract elements gml:AbstractValue, gml:AbstractTimeObject and gml:AbstractGeometry are all in a choice group named gml:Value, which is used for compositing in gml:CompositeValue and gml:ValueExtent.
 * -	Schemas which need values may use the abstract element gml:AbstractValue in a content model in order to permit any of the gml:AbstractScalarValues, gml:AbstractScalarValueLists, gml:CompositeValue or gml:ValueExtent to occur in an instance, or the named group gml:Value to also permit gml:AbstractTimeObjects, gml:AbstractGeometrys.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 Clause 10.
 * Any geometry element that inherits the semantics of AbstractGeometryType may be viewed as a set of direct positions.
 * All of the classes derived from AbstractGeometryType inherit an optional association to a coordinate reference system. All direct positions shall directly or indirectly be associated with a coordinate reference system. When geometry elements are aggregated in another geometry element (such as a MultiGeometry or GeometricComplex), which already has a coordinate reference system specified, then these elements are assumed to be in that same coordinate reference system unless otherwise specified.
 * The geometry model distinguishes geometric primitives, aggregates and complexes.
 * Geometric primitives, i.e. instances of a subtype of AbstractGeometricPrimitiveType, will be open, that is, they will not contain their boundary points; curves will not contain their end points, surfaces will not contain their boundary curves, and solids will not contain their bounding surfaces.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 13.4.
 * The coordinate systems schema components can be divded into  three logical parts, which define elements and types for XML encoding of the definitions of:
 * -	Coordinate system axes
 * -	Abstract coordinate system
 * -	Multiple concrete types of spatial-temporal coordinate systems
 * These schema components encode the Coordinate System packages of the UML Models of ISO 19111 Clause 9 and ISO/DIS 19136 D.3.10.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 13.5
 * The datums schema components can be divided into three logical parts, which define elements and types for XML encoding of the definitions of:
 * -	Abstract datum
 * -	Geodetic datums, including ellipsoid and prime meridian
 * -	Multiple other concrete types of spatial or temporal datums
 * These schema components encode the Datum packages of the UML Models of ISO 19111 Clause 10 and ISO/DIS 19136 D.3.10.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 13.6.
 * The spatial or temporal coordinate operations schema components can be divided into five logical parts, which define elements and types for XML encoding of the definitions of:
 * -	Multiple abstract coordinate operations
 * -	Multiple concrete types of coordinate operations, including Transformations and Conversions
 * -	Abstract and concrete parameter values and groups
 * -	Operation methods
 * -	Abstract and concrete operation parameters and groups
 * These schema component encodes the Coordinate Operation package of the UML Model for ISO 19111 Clause 11.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 13.2.
 * The reference systems schema components have two logical parts, which define elements and types for XML encoding of the definitions of:
 * -	Identified Object, inherited by the ten types of GML objects used for coordinate reference systems and coordinate operations
 * -	High-level part of the definitions of coordinate reference systems
 * This schema encodes the Identified Object and Reference System packages of the UML Model for ISO 19111.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 17.3.
 * gml:MeasureType is defined in the basicTypes schema.  The measure types defined here correspond with a set of convenience measure types described in ISO/TS 19103.  The XML implementation is based on the XML Schema simple type "double" which supports both decimal and scientific notation, and includes an XML attribute "uom" which refers to the units of measure for the value.  Note that, there is no requirement to store values using any particular format, and applications receiving elements of this type may choose to coerce the data to any other type as convenient.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 17.2.
 * Several GML Schema components concern or require a reference scale or units of measure.  Units are required for quantities that may occur as values of properties of feature types, as the results of observations, in the range parameters of a coverage, and for measures used in Coordinate Reference System definitions.
 * The basic unit definition is an extension of the general gml:Definition element defined in 16.2.1.  Three specialized elements for unit definition are further derived from this.
 * This model is based on the SI system of units [ISO 1000], which distinguishes between Base Units and Derived Units.
 * -	Base Units are the preferred units for a set of orthogonal fundamental quantities which define the particular system of units, which may not be derived by combination of other base units.
 * -	Derived Units are the preferred units for other quantities in the system, which may be defined by algebraic combination of the base units.
 * In some application areas Conventional units are used, which may be converted to the preferred units using a scaling factor or a formula which defines a re-scaling and offset.  The set of preferred units for all physical quantity types in a particular system of units is composed of the union of its base units and derived units.
 * Unit definitions are substitutable for the gml:Definition element declared as part of the dictionary model.  A dictionary that contains only unit definitions and references to unit definitions is a units dictionary.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This metadataApplication.xsd schema implements the UML conceptual schema defined in A.2.12 of ISO 19115:2003. It contains the implementation of the class: MD_ApplicationSchemaInformation.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This metadataEntity.xsd schema implements the UML conceptual schema defined in A.2.1 of ISO 19115:2003. It contains the implementation of the class MD_Metadata.
 * Geographic COmmon (GCO) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GCO includes all the definitions of http://www.isotc211.org/2005/gco namespace. The root document of this namespace is the file gco.xsd.
 * Geographic COmmon (GCO) extensible markup language is a component of the XML Schema Implementation of Geographic
 * Information Metadata documented in ISO/TS 19139:2007. GCO includes all the definitions of http://www.isotc211.org/2005/gco namespace. The root document of this namespace is the file gco.xsd. This basicTypes.xsd schema implements concepts from the "basic types" package of ISO/TS 19103.
 * Geographic COmmon (GCO) extensible markup language is a component of the XML Schema Implementation of Geographic
 * Information Metadata documented in ISO/TS 19139:2007. GCO includes all the definitions of http://www.isotc211.org/2005/gco namespace. The root document of this namespace is the file gco.xsd. This gcoBase.xsd schema provides:
 * 		1.  tools to handle specific objects like "code lists" and "record";
 * 		2. Some XML types representing that do not follow the general encoding rules.
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
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This portrayalCatalogue.xsd schema implements the UML conceptual schema defined in A.2.6 of ISO 19115:2003. It contains the implementation of the following classes: MD_GridSpatialRepresentation, MD_VectorSpatialRepresentation, MD_SpatialRepresentation, MD_Georeferenceable, MD_Dimension, MD_Georectified, MD_GeometricObjects, MD_TopologyLevelCode, MD_GeometricObjectTypeCode, MD_CellGeometryCode, MD_DimensionNameTypeCode, MD_PixelOrientationCode.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This metadataExtension.xsd schema implements the UML conceptual schema defined in A.2.11 of ISO 19115:2003. It contains the implementation of the following classes: MD_ExtendedElementInformation, MD_MetadataExtensionInformation, MD_ObligationCode, MD_DatatypeCode.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This content.xsd schema implements the UML conceptual schema defined in ISO 19115:2003, A.2.8. It contains the implementation of the following classes: MD_FeatureCatalogueDescription, MD_CoverageDescription,
 * MD_ImageDescription, MD_ContentInformation, MD_RangeDimension, MD_Band, MD_CoverageContentTypeCode, MD_ImagingConditionCode.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This applicationSchema.xsd schema implements the UML conceptual schema defined in A.2.12 of ISO 19115:2003. It contains the implementation of the class MD_ApplicationSchemaInformation.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This portrayalCatalogue.xsd schema implements the UML conceptual schema defined in A.2.9 of ISO 19115:2003. It contains the implementation of the class MD_PortrayalCatalogueReference.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This dataQuality.xsd schema implements the UML conceptual schema defined in A.2.4 of ISO 19115:2003. It contains the implementation of the following classes: LI_ProcessStep, LI_Source, LI_Lineage,
 * DQ_ConformanceResult, DQ_QuantitativeResult, DQ_Result, DQ_TemporalValidity, DQ_AccuracyOfATimeMeasurement, DQ_QuantitativeAttributeAccuracy, DQ_NonQuantitativeAttributeAccuracy, DQ_ThematicClassificationCorrectness, DQ_RelativeInternalPositionalAccuracy, DQ_GriddedDataPositionalAccuracy, DQ_AbsoluteExternalPositionalAccuracy, DQ_TopologicalConsistency, DQ_FormatConsistency, DQ_DomainConsistency, DQ_ConceptualConsistency, DQ_CompletenessOmission, DQ_CompletenessCommission, DQ_TemporalAccuracy, DQ_ThematicAccuracy, DQ_PositionalAccuracy, DQ_LogicalConsistency, DQ_Completeness, DQ_Element, DQ_DataQuality.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This freeText.xsd schema implements cultural and linguistic adaptability extensions defined in 7.3 of ISO/TS 19139:2007. This extension essentially formalizes the free text concept described in Annex J of ISO 19115:2003. For this reason, and in order to simplify the organization of overall geographic metadata XML schema, this schema has been included as part of the gmd namespace instead of the gmx namespace.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This identification.xsd schema implements the UML conceptual schema defined in A.2.2 of ISO 19115:2003. It contains the implementation of the following classes: MD_Identification, MD_BrowseGraphic, MD_DataIdentification, MD_ServiceIdentification, MD_RepresentativeFraction, MD_Usage, MD_Keywords, DS_Association, MD_AggregateInformation, MD_CharacterSetCode, MD_SpatialRepresentationTypeCode, MD_TopicCategoryCode, MD_ProgressCode, MD_KeywordTypeCode, DS_AssociationTypeCode, DS_InitiativeTypeCode, MD_ResolutionType.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This constraints.xsd schema implements the UML conceptual schema defined in A.2.3 of ISO 19115:2003. It contains the implementation of the following classes: MD_Constraints, MD_LegalConstraints, MD_SecurityConstraints, MD_ClassificationCode, MD_RestrictionCode.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This distribution.xsd schema implements the UML conceptual schema defined in A.2.10 of ISO 19115:2003. It contains the implementation of the following classes: MD_Medium, MD_DigitalTransferOptions, MD_StandardOrderProcess, MD_Distributor, MD_Distribution, MD_Format, MD_MediumFormatCode, MD_MediumNameCode.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This maintenance.xsd schema implements the UML conceptual schema defined in A.2.5 of ISO 19115:2003. It contains the implementation of the following classes: MD_MaintenanceInformation, MD_MaintenanceFrequencyCode, MD_ScopeCode, MD_ScopeDescription.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This citation.xsd schema implements the UML conceptual schema defined in A.3.2 of ISO 19115:2003. It contains the implementation of the following classes: CI_ResponsibleParty, CI_Citation, CI_Address, CI_OnlineResource, CI_Contact, CI_Telephone, URL, CI_Date, CI_Series, CI_RoleCode, CI_PresentationFormCode, CI_OnLineFunctionCode, CI_DateTypeCode.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This referenceSystem.xsd schema implements the UML conceptual schema defined in A.2.7 of ISO 19115:2003 and ISO 19115:2003/Cor. 1:2006. It contains the implementation of the following classes: RS_Identifier, MD_ReferenceSystem, MD_Identifier and RS_Reference System.
 * Geographic Temporal Schema (GTS) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GTS includes all the definitions of http://www.isotc211.org/2005/gts namespace. The root document of this namespace is the file gts.xsd.
 * Geographic Temporal Schema (GTS) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GTS includes all the definitions of http://www.isotc211.org/2005/gts namespace. The root document of this namespace is the file gts.xsd. The temporalObjects.xsd schema contains the XML implementation of TM_Object, TM_Primitive and TM_PeriodDuration from ISO 19108. The encoding of these classes is mapped to ISO 19136 temporal types and W3C built-in types.
 * Geographic Spatial Schema (GSS) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GSS includes all the definitions of http://www.isotc211.org/2005/gss namespace. The root document of this namespace is the file gss.xsd.
 * Geographic Spatial Schema (GSS) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GSS includes all the definitions of http://www.isotc211.org/2005/gss namespace. The root document of this namespace is the file gss.xsd. This geometry.xsd schema contains the implementation of GM_Object and GM_Point. The encoding of these classes is mapped to ISO 19136 geometric types.
 * Geographic MetaData (GMD) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GMD includes all the definitions of http://www.isotc211.org/2005/gmd namespace. The root document of this namespace is the file gmd.xsd. This extent.xsd schema implements the UML conceptual schema defined in A.3.1 of ISO 19115:2003 and the associated corrigendum. It contains the implementation of the following classes: EX_TemporalExtent, EX_VerticalExtent, EX_BoundingPolygon, EX_Extent, EX_GeographicExtent, EX_GeographicBoundingBox, EX_SpatialTemporalExtent, EX_GeographicDescription.
 * Geographic Spatial Referencing (GSR) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GSR includes all the definitions of http://www.isotc211.org/2005/gsr namespace. The root document of this namespace is the file gsr.xsd.
 * Geographic Spatial Referencing (GSR) extensible markup language is a component of the XML Schema Implementation of Geographic Information Metadata documented in ISO/TS 19139:2007. GSR includes all the definitions of http://www.isotc211.org/2005/gsr namespace. The root document of this namespace is the file gsr.xsd. This spatialReferencing.xsd schema contains the implementation of SC_CRS. The encoding of this class is mapped to an ISO 19136 XML type.
 * See ISO/DIS 19136 20.2.
 * An implicit description of geometry is one in which the items of the geometry do not explicitly appear in the encoding.  Instead, a compact notation records a set of parameters, and a set of objects may be generated using a rule with these parameters.  This Clause provides grid geometries that are used in the description of gridded coverages and other applications.
 * In GML two grid structures are defined, namely gml:Grid and gml:RectifiedGrid.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 12.3.
 * Geometric aggregates (i.e. instances of a subtype of gml:AbstractGeometricAggregateType) are arbitrary aggregations of geometry elements. They are not assumed to have any additional internal structure and are used to "collect" pieces of geometry of a specified type. Application schemas may use aggregates for features that use multiple geometric objects in their representations.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 Clause 11.
 * Beside the "simple" geometric primitives specified in the previous Clause, this Clause specifies additional primitives to describe real world situations which require a more expressive geometry model.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 12.2.
 * Geometric complexes (i.e. instances of gml:GeometricComplexType) are closed collections of geometric primitives, i.e. they will contain their boundaries.
 * A geometric complex (gml:GeometricComplex) is defined by ISO 19107:2003, 6.6.1 as "a set of primitive geometric objects (in a common coordinate system) whose interiors are disjoint. Further, if a primitive is in a geometric complex, then there exists a set of primitives in that complex whose point-wise union is the boundary of this first primitive."
 * A geometric composite (gml:CompositeCurve, gml:CompositeSurface and gml:CompositeSolid) represents a geometric complex with an underlying core geometry that is isomorphic to a primitive, i.e. it can be viewed as a primitive and as a complex. See ISO 19107:2003, 6.1 and 6.6.3 for more details on the nature of composite geometries.
 * Geometric complexes and composites are intended to be used in application schemas where the sharing of geometry is important.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 8.2.
 * W3C XML Schema provides a set of built-in "simple" types which define methods for representing values as literals without internal markup.  These are described in W3C XML Schema Part 2:2001.  Because GML is an XML encoding in which instances are described using XML Schema, these simple types shall be used as far as possible and practical for the representation of data types.  W3C XML Schema also provides methods for defining
 * -	new simple types by restriction and combination of the built-in types, and
 * -	complex types, with simple content, but which also have XML attributes.
 * In many places where a suitable built-in simple type is not available, simple content types derived using the XML Schema mechanisms are used for the representation of data types in GML.
 * A set of these simple content types that are required by several GML components are defined in the basicTypes schema, as well as some elements based on them. These are primarily based around components needed to record amounts, counts, flags and terms, together with support for exceptions or null values.
 * 
 * GML is an OGC Standard.
 * Copyright (c) 2007,2010 Open Geospatial Consortium.
 * To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * See ISO/DIS 19136 Clause 10.
 * 
 * 			GML is an OGC Standard.
 * 			Copyright (c) 2007,2010 Open Geospatial Consortium.
 * 			To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * All-components schema for the SWE Common Data Model namespace. See Conformance Class urn:ogc:spec:SWECommonDataModel:2.0:XXX of the SWE Common Data Model 2.0 specification
 * 
 *         SWE Common is an OGC Standard.
 *         Copyright (c) 2010 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * SWE Common Data Model schema for record data components. See requirements class http://www.opengis.net/spec/SWE/2.0/req/xsd-record-components/
 * 
 *         SWE Common is an OGC Standard.
 *         Copyright (c) 2010 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * SWE Common Data Model schema for data arrays and data stream descriptors. See requirements class http://www.opengis.net/spec/SWE/2.0/req/xsd-block-components/
 * 
 *         SWE Common is an OGC Standard.
 *         Copyright (c) 2010 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * SWE Common Data Model schema for specifying parameters of advanced encoding methods (Raw Binary, Base64 Binary, Compressed Binary). See requirements class http://www.opengis.net/spec/SWE/2.0/req/xsd-advanced-encodings/
 * 
 *         SWE Common is an OGC Standard.
 *         Copyright (c) 2010 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * SWE Common Data Model schema for choice data components (i.e. disjoint unions). See requirements class http://www.opengis.net/spec/SWE/2.0/req/xsd-choice-components/
 * 
 *         SWE Common is an OGC Standard.
 *         Copyright (c) 2010 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * SWE Common Data Model schema for simple data components (i.e. without descendants). See requirements class http://www.opengis.net/spec/SWE/2.0/req/xsd-simple-components/
 * 
 *         SWE Common is an OGC Standard.
 *         Copyright (c) 2010 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * SWE Common Data Model schema for specifying parameters of simple encoding methods (Text, XML). See requirements class http://www.opengis.net/spec/SWE/2.0/req/xsd-simple-encodings/
 * 
 *         SWE Common is an OGC Standard.
 *         Copyright (c) 2010 Open Geospatial Consortium.
 *         To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
 * 
 * Schema of simple basic types used in various places in the SWE Common Data Model.
 * 
 * 		SWE Common is an OGC Standard.
 * 		Copyright (c) 2010 Open Geospatial Consortium.
 * 		To obtain additional rights of use, visit http://www.opengeospatial.org/legal/ .
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
 * <!-- end-model-doc -->
 * @see net.opengis.wcs20.Wcs20Factory
 * @model kind="package"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:gml:3.2.1 appinfo='gml.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:dynamicFeature:3.2.1 appinfo='dynamicFeature.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:topology:3.2.1 appinfo='topology.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:coverage:3.2.1 appinfo='coverage.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:coordinateReferenceSystems:3.2.1 appinfo='coordinateReferenceSystems.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:observation:3.2.1 appinfo='observation.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:temporalReferenceSystems:3.2.1 appinfo='temporalReferenceSystems.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:deprecatedTypes:3.2.1 appinfo='deprecatedTypes.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:temporalTopology:3.2.1 appinfo='temporalTopology.xsd'"
 *        annotation="urn:opengis:specification:gml:schema-xsd:dictionary:v3.2.1 appinfo='dictionary.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:gmlBase:3.2.1 appinfo='gmlBase.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:temporal:3.2.1 appinfo='temporal.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:feature:3.2.1 appinfo='feature.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:direction:3.2.1 appinfo='direction.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:valueObjects:3.2.1 appinfo='valueObjects.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:geometryBasic0d1d:3.2.1 appinfo='geometryBasic0d1d.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:coordinateSystems:3.2.1 appinfo='coordinateSystems.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:datums:3.2.1 appinfo='datums.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:coordinateOperations:3.2.1 appinfo='coordinateOperations.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:referenceSystems:3.2.1 appinfo='referenceSystems.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:measures:3.2.1 appinfo='measures.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:units:3.2.1 appinfo='units.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:grids:3.2.1 appinfo='grids.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:geometryAggregates:3.2.1 appinfo='geometryAggregates.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:geometryPrimitives:3.2.1 appinfo='geometryPrimitives.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:geometryComplexes:3.2.1 appinfo='geometryComplexes.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:basicTypes:3.2.1 appinfo='basicTypes.xsd'"
 *        annotation="urn:x-ogc:specification:gml:schema-xsd:geometryBasic2d:3.2.1 appinfo='geometryBasic2d.xsd'"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface Wcs20Package extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "wcs20";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.opengis.net/wcs/2.0";

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
    Wcs20Package eINSTANCE = net.opengis.wcs20.impl.Wcs20PackageImpl.init();

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.CapabilitiesTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCapabilitiesType()
     * @generated
     */
    int CAPABILITIES_TYPE = 0;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_IDENTIFICATION = Ows20Package.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION;

    /**
     * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_PROVIDER = Ows20Package.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__OPERATIONS_METADATA = Ows20Package.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA;

    /**
     * The feature id for the '<em><b>Languages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__LANGUAGES = Ows20Package.CAPABILITIES_BASE_TYPE__LANGUAGES;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__UPDATE_SEQUENCE = Ows20Package.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__VERSION = Ows20Package.CAPABILITIES_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Service Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_METADATA = Ows20Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__CONTENTS = Ows20Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE_FEATURE_COUNT = Ows20Package.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.ContentsTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getContentsType()
     * @generated
     */
    int CONTENTS_TYPE = 1;

    /**
     * The feature id for the '<em><b>Dataset Description Summary</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__DATASET_DESCRIPTION_SUMMARY = Ows20Package.CONTENTS_BASE_TYPE__DATASET_DESCRIPTION_SUMMARY;

    /**
     * The feature id for the '<em><b>Other Source</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__OTHER_SOURCE = Ows20Package.CONTENTS_BASE_TYPE__OTHER_SOURCE;

    /**
     * The feature id for the '<em><b>Coverage Summary</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__COVERAGE_SUMMARY = Ows20Package.CONTENTS_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__EXTENSION = Ows20Package.CONTENTS_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Contents Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE_FEATURE_COUNT = Ows20Package.CONTENTS_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.CoverageDescriptionsTypeImpl <em>Coverage Descriptions Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.CoverageDescriptionsTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageDescriptionsType()
     * @generated
     */
    int COVERAGE_DESCRIPTIONS_TYPE = 2;

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
     * The meta object id for the '{@link net.opengis.wcs20.impl.CoverageDescriptionTypeImpl <em>Coverage Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.CoverageDescriptionTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageDescriptionType()
     * @generated
     */
    int COVERAGE_DESCRIPTION_TYPE = 3;

    /**
     * The feature id for the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__COVERAGE_ID = 0;

    /**
     * The feature id for the '<em><b>Coverage Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION = 1;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__METADATA = 2;

    /**
     * The feature id for the '<em><b>Domain Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__DOMAIN_SET = 3;

    /**
     * The feature id for the '<em><b>Range Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE = 4;

    /**
     * The feature id for the '<em><b>Service Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS = 5;

    /**
     * The number of structural features of the '<em>Coverage Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE_FEATURE_COUNT = 6;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.CoverageOfferingsTypeImpl <em>Coverage Offerings Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.CoverageOfferingsTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageOfferingsType()
     * @generated
     */
    int COVERAGE_OFFERINGS_TYPE = 4;

    /**
     * The feature id for the '<em><b>Service Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA = 0;

    /**
     * The feature id for the '<em><b>Offered Coverage</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE = 1;

    /**
     * The number of structural features of the '<em>Coverage Offerings Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_OFFERINGS_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.CoverageSubtypeParentTypeImpl <em>Coverage Subtype Parent Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.CoverageSubtypeParentTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageSubtypeParentType()
     * @generated
     */
    int COVERAGE_SUBTYPE_PARENT_TYPE = 5;

    /**
     * The feature id for the '<em><b>Coverage Subtype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE = 0;

    /**
     * The feature id for the '<em><b>Coverage Subtype Parent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT = 1;

    /**
     * The number of structural features of the '<em>Coverage Subtype Parent Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUBTYPE_PARENT_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl <em>Coverage Summary Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.CoverageSummaryTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageSummaryType()
     * @generated
     */
    int COVERAGE_SUMMARY_TYPE = 6;

    /**
     * The feature id for the '<em><b>Title</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__TITLE = Ows20Package.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__ABSTRACT = Ows20Package.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__KEYWORDS = Ows20Package.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__COVERAGE_ID = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Coverage Subtype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Coverage Subtype Parent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Bounding Box Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__BOUNDING_BOX = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Metadata Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__METADATA_GROUP = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__METADATA = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 7;

    /**
     * The number of structural features of the '<em>Coverage Summary Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE_FEATURE_COUNT = Ows20Package.DESCRIPTION_TYPE_FEATURE_COUNT + 8;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.RequestBaseTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getRequestBaseType()
     * @generated
     */
    int REQUEST_BASE_TYPE = 16;

    /**
     * The feature id for the '<em><b>Extension</b></em>' containment reference.
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
     * The feature id for the '<em><b>Base Url</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__BASE_URL = 3;

    /**
     * The number of structural features of the '<em>Request Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.DescribeCoverageTypeImpl <em>Describe Coverage Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.DescribeCoverageTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDescribeCoverageType()
     * @generated
     */
    int DESCRIBE_COVERAGE_TYPE = 7;

    /**
     * The feature id for the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_COVERAGE_TYPE__EXTENSION = REQUEST_BASE_TYPE__EXTENSION;

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
     * The feature id for the '<em><b>Coverage Id</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_COVERAGE_TYPE__COVERAGE_ID = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Describe Coverage Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_COVERAGE_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.DimensionSubsetTypeImpl <em>Dimension Subset Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.DimensionSubsetTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDimensionSubsetType()
     * @generated
     */
    int DIMENSION_SUBSET_TYPE = 9;

    /**
     * The feature id for the '<em><b>Dimension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_SUBSET_TYPE__DIMENSION = 0;

    /**
     * The feature id for the '<em><b>CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_SUBSET_TYPE__CRS = 1;

    /**
     * The number of structural features of the '<em>Dimension Subset Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_SUBSET_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.DimensionSliceTypeImpl <em>Dimension Slice Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.DimensionSliceTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDimensionSliceType()
     * @generated
     */
    int DIMENSION_SLICE_TYPE = 8;

    /**
     * The feature id for the '<em><b>Dimension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_SLICE_TYPE__DIMENSION = DIMENSION_SUBSET_TYPE__DIMENSION;

    /**
     * The feature id for the '<em><b>CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_SLICE_TYPE__CRS = DIMENSION_SUBSET_TYPE__CRS;

    /**
     * The feature id for the '<em><b>Slice Point</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_SLICE_TYPE__SLICE_POINT = DIMENSION_SUBSET_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Dimension Slice Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_SLICE_TYPE_FEATURE_COUNT = DIMENSION_SUBSET_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.DimensionTrimTypeImpl <em>Dimension Trim Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.DimensionTrimTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDimensionTrimType()
     * @generated
     */
    int DIMENSION_TRIM_TYPE = 10;

    /**
     * The feature id for the '<em><b>Dimension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TRIM_TYPE__DIMENSION = DIMENSION_SUBSET_TYPE__DIMENSION;

    /**
     * The feature id for the '<em><b>CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TRIM_TYPE__CRS = DIMENSION_SUBSET_TYPE__CRS;

    /**
     * The feature id for the '<em><b>Trim Low</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TRIM_TYPE__TRIM_LOW = DIMENSION_SUBSET_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Trim High</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TRIM_TYPE__TRIM_HIGH = DIMENSION_SUBSET_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Dimension Trim Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DIMENSION_TRIM_TYPE_FEATURE_COUNT = DIMENSION_SUBSET_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.DocumentRootImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDocumentRoot()
     * @generated
     */
    int DOCUMENT_ROOT = 11;

    /**
     * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 0;

    /**
     * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 1;

    /**
     * The feature id for the '<em><b>Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CAPABILITIES = 2;

    /**
     * The feature id for the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CONTENTS = 3;

    /**
     * The feature id for the '<em><b>Coverage Description</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE_DESCRIPTION = 4;

    /**
     * The feature id for the '<em><b>Coverage Descriptions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS = 5;

    /**
     * The feature id for the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE_ID = 6;

    /**
     * The feature id for the '<em><b>Coverage Offerings</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE_OFFERINGS = 7;

    /**
     * The feature id for the '<em><b>Coverage Subtype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE_SUBTYPE = 8;

    /**
     * The feature id for the '<em><b>Coverage Subtype Parent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE_SUBTYPE_PARENT = 9;

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
     * The feature id for the '<em><b>Dimension Slice</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DIMENSION_SLICE = 12;

    /**
     * The feature id for the '<em><b>Dimension Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DIMENSION_SUBSET = 13;

    /**
     * The feature id for the '<em><b>Dimension Trim</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DIMENSION_TRIM = 14;

    /**
     * The feature id for the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXTENSION = 15;

    /**
     * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_CAPABILITIES = 16;

    /**
     * The feature id for the '<em><b>Get Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_COVERAGE = 17;

    /**
     * The feature id for the '<em><b>Offered Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OFFERED_COVERAGE = 18;

    /**
     * The feature id for the '<em><b>Service Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SERVICE_METADATA = 19;

    /**
     * The feature id for the '<em><b>Service Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SERVICE_PARAMETERS = 20;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 21;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.ExtensionTypeImpl <em>Extension Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.ExtensionTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getExtensionType()
     * @generated
     */
    int EXTENSION_TYPE = 12;

    /**
     * The feature id for the '<em><b>Contents</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_TYPE__CONTENTS = 0;

    /**
     * The number of structural features of the '<em>Extension Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.GetCapabilitiesTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getGetCapabilitiesType()
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
     * The meta object id for the '{@link net.opengis.wcs20.impl.GetCoverageTypeImpl <em>Get Coverage Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.GetCoverageTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getGetCoverageType()
     * @generated
     */
    int GET_COVERAGE_TYPE = 14;

    /**
     * The feature id for the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__EXTENSION = REQUEST_BASE_TYPE__EXTENSION;

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
     * The feature id for the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__COVERAGE_ID = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Dimension Subset Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Dimension Subset</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__DIMENSION_SUBSET = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__FORMAT = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Media Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__MEDIA_TYPE = REQUEST_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Get Coverage Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.OfferedCoverageTypeImpl <em>Offered Coverage Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.OfferedCoverageTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getOfferedCoverageType()
     * @generated
     */
    int OFFERED_COVERAGE_TYPE = 15;

    /**
     * The feature id for the '<em><b>Abstract Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OFFERED_COVERAGE_TYPE__ABSTRACT_COVERAGE = 0;

    /**
     * The feature id for the '<em><b>Service Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS = 1;

    /**
     * The number of structural features of the '<em>Offered Coverage Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OFFERED_COVERAGE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.ServiceMetadataTypeImpl <em>Service Metadata Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.ServiceMetadataTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getServiceMetadataType()
     * @generated
     */
    int SERVICE_METADATA_TYPE = 17;

    /**
     * The feature id for the '<em><b>Format Supported</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_METADATA_TYPE__FORMAT_SUPPORTED = 0;

    /**
     * The feature id for the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_METADATA_TYPE__EXTENSION = 1;

    /**
     * The number of structural features of the '<em>Service Metadata Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_METADATA_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.ServiceParametersTypeImpl <em>Service Parameters Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.ServiceParametersTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getServiceParametersType()
     * @generated
     */
    int SERVICE_PARAMETERS_TYPE = 18;

    /**
     * The feature id for the '<em><b>Coverage Subtype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE = 0;

    /**
     * The feature id for the '<em><b>Coverage Subtype Parent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT = 1;

    /**
     * The feature id for the '<em><b>Native Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PARAMETERS_TYPE__NATIVE_FORMAT = 2;

    /**
     * The feature id for the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PARAMETERS_TYPE__EXTENSION = 3;

    /**
     * The number of structural features of the '<em>Service Parameters Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_PARAMETERS_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link java.lang.Object <em>Object</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getObject()
     * @generated
     */
    int OBJECT = 19;

    /**
     * The number of structural features of the '<em>Object</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OBJECT_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wcs20.impl.ExtensionItemTypeImpl <em>Extension Item Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs20.impl.ExtensionItemTypeImpl
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getExtensionItemType()
     * @generated
     */
    int EXTENSION_ITEM_TYPE = 20;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_ITEM_TYPE__NAME = 0;

    /**
     * The feature id for the '<em><b>Namespace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_ITEM_TYPE__NAMESPACE = 1;

    /**
     * The feature id for the '<em><b>Simple Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_ITEM_TYPE__SIMPLE_CONTENT = 2;

    /**
     * The feature id for the '<em><b>Object Content</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_ITEM_TYPE__OBJECT_CONTENT = 3;

    /**
     * The number of structural features of the '<em>Extension Item Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXTENSION_ITEM_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '<em>Version String Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getVersionStringType()
     * @generated
     */
    int VERSION_STRING_TYPE = 21;


    /**
     * The meta object id for the '<em>Version String Type 1</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getVersionStringType_1()
     * @generated
     */
    int VERSION_STRING_TYPE_1 = 22;


    /**
     * The meta object id for the '<em>QName</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.xml.namespace.QName
     * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getQName()
     * @generated
     */
    int QNAME = 23;


    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.CapabilitiesType <em>Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Capabilities Type</em>'.
     * @see net.opengis.wcs20.CapabilitiesType
     * @generated
     */
    EClass getCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CapabilitiesType#getServiceMetadata <em>Service Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Metadata</em>'.
     * @see net.opengis.wcs20.CapabilitiesType#getServiceMetadata()
     * @see #getCapabilitiesType()
     * @generated
     */
    EReference getCapabilitiesType_ServiceMetadata();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CapabilitiesType#getContents <em>Contents</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contents</em>'.
     * @see net.opengis.wcs20.CapabilitiesType#getContents()
     * @see #getCapabilitiesType()
     * @generated
     */
    EReference getCapabilitiesType_Contents();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.ContentsType <em>Contents Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Contents Type</em>'.
     * @see net.opengis.wcs20.ContentsType
     * @generated
     */
    EClass getContentsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs20.ContentsType#getCoverageSummary <em>Coverage Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Coverage Summary</em>'.
     * @see net.opengis.wcs20.ContentsType#getCoverageSummary()
     * @see #getContentsType()
     * @generated
     */
    EReference getContentsType_CoverageSummary();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.ContentsType#getExtension <em>Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extension</em>'.
     * @see net.opengis.wcs20.ContentsType#getExtension()
     * @see #getContentsType()
     * @generated
     */
    EReference getContentsType_Extension();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.CoverageDescriptionsType <em>Coverage Descriptions Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Descriptions Type</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionsType
     * @generated
     */
    EClass getCoverageDescriptionsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs20.CoverageDescriptionsType#getCoverageDescription <em>Coverage Description</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Coverage Description</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionsType#getCoverageDescription()
     * @see #getCoverageDescriptionsType()
     * @generated
     */
    EReference getCoverageDescriptionsType_CoverageDescription();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.CoverageDescriptionType <em>Coverage Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Description Type</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionType
     * @generated
     */
    EClass getCoverageDescriptionType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.CoverageDescriptionType#getCoverageId <em>Coverage Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Coverage Id</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionType#getCoverageId()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EAttribute getCoverageDescriptionType_CoverageId();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CoverageDescriptionType#getCoverageFunction <em>Coverage Function</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Function</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionType#getCoverageFunction()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EReference getCoverageDescriptionType_CoverageFunction();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs20.CoverageDescriptionType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionType#getMetadata()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EReference getCoverageDescriptionType_Metadata();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CoverageDescriptionType#getDomainSet <em>Domain Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Domain Set</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionType#getDomainSet()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EReference getCoverageDescriptionType_DomainSet();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CoverageDescriptionType#getRangeType <em>Range Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Range Type</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionType#getRangeType()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EReference getCoverageDescriptionType_RangeType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CoverageDescriptionType#getServiceParameters <em>Service Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Parameters</em>'.
     * @see net.opengis.wcs20.CoverageDescriptionType#getServiceParameters()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EReference getCoverageDescriptionType_ServiceParameters();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.CoverageOfferingsType <em>Coverage Offerings Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Offerings Type</em>'.
     * @see net.opengis.wcs20.CoverageOfferingsType
     * @generated
     */
    EClass getCoverageOfferingsType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CoverageOfferingsType#getServiceMetadata <em>Service Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Metadata</em>'.
     * @see net.opengis.wcs20.CoverageOfferingsType#getServiceMetadata()
     * @see #getCoverageOfferingsType()
     * @generated
     */
    EReference getCoverageOfferingsType_ServiceMetadata();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs20.CoverageOfferingsType#getOfferedCoverage <em>Offered Coverage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Offered Coverage</em>'.
     * @see net.opengis.wcs20.CoverageOfferingsType#getOfferedCoverage()
     * @see #getCoverageOfferingsType()
     * @generated
     */
    EReference getCoverageOfferingsType_OfferedCoverage();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.CoverageSubtypeParentType <em>Coverage Subtype Parent Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Subtype Parent Type</em>'.
     * @see net.opengis.wcs20.CoverageSubtypeParentType
     * @generated
     */
    EClass getCoverageSubtypeParentType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.CoverageSubtypeParentType#getCoverageSubtype <em>Coverage Subtype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Coverage Subtype</em>'.
     * @see net.opengis.wcs20.CoverageSubtypeParentType#getCoverageSubtype()
     * @see #getCoverageSubtypeParentType()
     * @generated
     */
    EAttribute getCoverageSubtypeParentType_CoverageSubtype();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CoverageSubtypeParentType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Subtype Parent</em>'.
     * @see net.opengis.wcs20.CoverageSubtypeParentType#getCoverageSubtypeParent()
     * @see #getCoverageSubtypeParentType()
     * @generated
     */
    EReference getCoverageSubtypeParentType_CoverageSubtypeParent();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.CoverageSummaryType <em>Coverage Summary Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Summary Type</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType
     * @generated
     */
    EClass getCoverageSummaryType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs20.CoverageSummaryType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>WGS84 Bounding Box</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType#getWGS84BoundingBox()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EReference getCoverageSummaryType_WGS84BoundingBox();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.CoverageSummaryType#getCoverageId <em>Coverage Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Coverage Id</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType#getCoverageId()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_CoverageId();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.CoverageSummaryType#getCoverageSubtype <em>Coverage Subtype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Coverage Subtype</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType#getCoverageSubtype()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_CoverageSubtype();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.CoverageSummaryType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Subtype Parent</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType#getCoverageSubtypeParent()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EReference getCoverageSummaryType_CoverageSubtypeParent();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs20.CoverageSummaryType#getBoundingBoxGroup <em>Bounding Box Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Bounding Box Group</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType#getBoundingBoxGroup()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_BoundingBoxGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs20.CoverageSummaryType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bounding Box</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType#getBoundingBox()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EReference getCoverageSummaryType_BoundingBox();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs20.CoverageSummaryType#getMetadataGroup <em>Metadata Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Metadata Group</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType#getMetadataGroup()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_MetadataGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs20.CoverageSummaryType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Metadata</em>'.
     * @see net.opengis.wcs20.CoverageSummaryType#getMetadata()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EReference getCoverageSummaryType_Metadata();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.DescribeCoverageType <em>Describe Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Describe Coverage Type</em>'.
     * @see net.opengis.wcs20.DescribeCoverageType
     * @generated
     */
    EClass getDescribeCoverageType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs20.DescribeCoverageType#getCoverageId <em>Coverage Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Coverage Id</em>'.
     * @see net.opengis.wcs20.DescribeCoverageType#getCoverageId()
     * @see #getDescribeCoverageType()
     * @generated
     */
    EAttribute getDescribeCoverageType_CoverageId();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.DimensionSliceType <em>Dimension Slice Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dimension Slice Type</em>'.
     * @see net.opengis.wcs20.DimensionSliceType
     * @generated
     */
    EClass getDimensionSliceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.DimensionSliceType#getSlicePoint <em>Slice Point</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Slice Point</em>'.
     * @see net.opengis.wcs20.DimensionSliceType#getSlicePoint()
     * @see #getDimensionSliceType()
     * @generated
     */
    EAttribute getDimensionSliceType_SlicePoint();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.DimensionSubsetType <em>Dimension Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dimension Subset Type</em>'.
     * @see net.opengis.wcs20.DimensionSubsetType
     * @generated
     */
    EClass getDimensionSubsetType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.DimensionSubsetType#getDimension <em>Dimension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Dimension</em>'.
     * @see net.opengis.wcs20.DimensionSubsetType#getDimension()
     * @see #getDimensionSubsetType()
     * @generated
     */
    EAttribute getDimensionSubsetType_Dimension();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.DimensionSubsetType#getCRS <em>CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>CRS</em>'.
     * @see net.opengis.wcs20.DimensionSubsetType#getCRS()
     * @see #getDimensionSubsetType()
     * @generated
     */
    EAttribute getDimensionSubsetType_CRS();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.DimensionTrimType <em>Dimension Trim Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dimension Trim Type</em>'.
     * @see net.opengis.wcs20.DimensionTrimType
     * @generated
     */
    EClass getDimensionTrimType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.DimensionTrimType#getTrimLow <em>Trim Low</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Trim Low</em>'.
     * @see net.opengis.wcs20.DimensionTrimType#getTrimLow()
     * @see #getDimensionTrimType()
     * @generated
     */
    EAttribute getDimensionTrimType_TrimLow();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.DimensionTrimType#getTrimHigh <em>Trim High</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Trim High</em>'.
     * @see net.opengis.wcs20.DimensionTrimType#getTrimHigh()
     * @see #getDimensionTrimType()
     * @generated
     */
    EAttribute getDimensionTrimType_TrimHigh();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see net.opengis.wcs20.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the map '{@link net.opengis.wcs20.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link net.opengis.wcs20.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getCapabilities <em>Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Capabilities</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Capabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getContents <em>Contents</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contents</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getContents()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Contents();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getCoverageDescription <em>Coverage Description</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Description</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getCoverageDescription()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CoverageDescription();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getCoverageDescriptions <em>Coverage Descriptions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Descriptions</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getCoverageDescriptions()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CoverageDescriptions();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.DocumentRoot#getCoverageId <em>Coverage Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Coverage Id</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getCoverageId()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_CoverageId();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getCoverageOfferings <em>Coverage Offerings</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Offerings</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getCoverageOfferings()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CoverageOfferings();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.DocumentRoot#getCoverageSubtype <em>Coverage Subtype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Coverage Subtype</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getCoverageSubtype()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_CoverageSubtype();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Subtype Parent</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getCoverageSubtypeParent()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CoverageSubtypeParent();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getCoverageSummary <em>Coverage Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Summary</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getCoverageSummary()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CoverageSummary();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Describe Coverage</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getDescribeCoverage()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DescribeCoverage();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getDimensionSlice <em>Dimension Slice</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Dimension Slice</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getDimensionSlice()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DimensionSlice();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getDimensionSubset <em>Dimension Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Dimension Subset</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getDimensionSubset()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DimensionSubset();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getDimensionTrim <em>Dimension Trim</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Dimension Trim</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getDimensionTrim()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DimensionTrim();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getExtension <em>Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extension</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getExtension()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Extension();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getGetCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getGetCoverage <em>Get Coverage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Coverage</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getGetCoverage()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetCoverage();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getOfferedCoverage <em>Offered Coverage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Offered Coverage</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getOfferedCoverage()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OfferedCoverage();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getServiceMetadata <em>Service Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Metadata</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getServiceMetadata()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ServiceMetadata();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.DocumentRoot#getServiceParameters <em>Service Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Parameters</em>'.
     * @see net.opengis.wcs20.DocumentRoot#getServiceParameters()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ServiceParameters();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.ExtensionType <em>Extension Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Extension Type</em>'.
     * @see net.opengis.wcs20.ExtensionType
     * @generated
     */
    EClass getExtensionType();

    /**
     * Returns the meta object for the reference list '{@link net.opengis.wcs20.ExtensionType#getContents <em>Contents</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Contents</em>'.
     * @see net.opengis.wcs20.ExtensionType#getContents()
     * @see #getExtensionType()
     * @generated
     */
    EReference getExtensionType_Contents();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Capabilities Type</em>'.
     * @see net.opengis.wcs20.GetCapabilitiesType
     * @generated
     */
    EClass getGetCapabilitiesType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.GetCapabilitiesType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wcs20.GetCapabilitiesType#getService()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_Service();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.GetCoverageType <em>Get Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Coverage Type</em>'.
     * @see net.opengis.wcs20.GetCoverageType
     * @generated
     */
    EClass getGetCoverageType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.GetCoverageType#getCoverageId <em>Coverage Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Coverage Id</em>'.
     * @see net.opengis.wcs20.GetCoverageType#getCoverageId()
     * @see #getGetCoverageType()
     * @generated
     */
    EAttribute getGetCoverageType_CoverageId();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs20.GetCoverageType#getDimensionSubsetGroup <em>Dimension Subset Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Dimension Subset Group</em>'.
     * @see net.opengis.wcs20.GetCoverageType#getDimensionSubsetGroup()
     * @see #getGetCoverageType()
     * @generated
     */
    EAttribute getGetCoverageType_DimensionSubsetGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs20.GetCoverageType#getDimensionSubset <em>Dimension Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Dimension Subset</em>'.
     * @see net.opengis.wcs20.GetCoverageType#getDimensionSubset()
     * @see #getGetCoverageType()
     * @generated
     */
    EReference getGetCoverageType_DimensionSubset();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.GetCoverageType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.wcs20.GetCoverageType#getFormat()
     * @see #getGetCoverageType()
     * @generated
     */
    EAttribute getGetCoverageType_Format();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.GetCoverageType#getMediaType <em>Media Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Media Type</em>'.
     * @see net.opengis.wcs20.GetCoverageType#getMediaType()
     * @see #getGetCoverageType()
     * @generated
     */
    EAttribute getGetCoverageType_MediaType();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.OfferedCoverageType <em>Offered Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Offered Coverage Type</em>'.
     * @see net.opengis.wcs20.OfferedCoverageType
     * @generated
     */
    EClass getOfferedCoverageType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.OfferedCoverageType#getAbstractCoverage <em>Abstract Coverage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Coverage</em>'.
     * @see net.opengis.wcs20.OfferedCoverageType#getAbstractCoverage()
     * @see #getOfferedCoverageType()
     * @generated
     */
    EReference getOfferedCoverageType_AbstractCoverage();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.OfferedCoverageType#getServiceParameters <em>Service Parameters</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Parameters</em>'.
     * @see net.opengis.wcs20.OfferedCoverageType#getServiceParameters()
     * @see #getOfferedCoverageType()
     * @generated
     */
    EReference getOfferedCoverageType_ServiceParameters();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.RequestBaseType <em>Request Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Request Base Type</em>'.
     * @see net.opengis.wcs20.RequestBaseType
     * @generated
     */
    EClass getRequestBaseType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.RequestBaseType#getExtension <em>Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extension</em>'.
     * @see net.opengis.wcs20.RequestBaseType#getExtension()
     * @see #getRequestBaseType()
     * @generated
     */
    EReference getRequestBaseType_Extension();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.RequestBaseType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wcs20.RequestBaseType#getService()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.RequestBaseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.wcs20.RequestBaseType#getVersion()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Version();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.RequestBaseType#getBaseUrl <em>Base Url</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Base Url</em>'.
     * @see net.opengis.wcs20.RequestBaseType#getBaseUrl()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_BaseUrl();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.ServiceMetadataType <em>Service Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Metadata Type</em>'.
     * @see net.opengis.wcs20.ServiceMetadataType
     * @generated
     */
    EClass getServiceMetadataType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.ServiceMetadataType#getFormatSupported <em>Format Supported</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format Supported</em>'.
     * @see net.opengis.wcs20.ServiceMetadataType#getFormatSupported()
     * @see #getServiceMetadataType()
     * @generated
     */
    EAttribute getServiceMetadataType_FormatSupported();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.ServiceMetadataType#getExtension <em>Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extension</em>'.
     * @see net.opengis.wcs20.ServiceMetadataType#getExtension()
     * @see #getServiceMetadataType()
     * @generated
     */
    EReference getServiceMetadataType_Extension();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs20.ServiceParametersType <em>Service Parameters Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Parameters Type</em>'.
     * @see net.opengis.wcs20.ServiceParametersType
     * @generated
     */
    EClass getServiceParametersType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.ServiceParametersType#getCoverageSubtype <em>Coverage Subtype</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Coverage Subtype</em>'.
     * @see net.opengis.wcs20.ServiceParametersType#getCoverageSubtype()
     * @see #getServiceParametersType()
     * @generated
     */
    EAttribute getServiceParametersType_CoverageSubtype();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.ServiceParametersType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Subtype Parent</em>'.
     * @see net.opengis.wcs20.ServiceParametersType#getCoverageSubtypeParent()
     * @see #getServiceParametersType()
     * @generated
     */
    EReference getServiceParametersType_CoverageSubtypeParent();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.ServiceParametersType#getNativeFormat <em>Native Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Native Format</em>'.
     * @see net.opengis.wcs20.ServiceParametersType#getNativeFormat()
     * @see #getServiceParametersType()
     * @generated
     */
    EAttribute getServiceParametersType_NativeFormat();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs20.ServiceParametersType#getExtension <em>Extension</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extension</em>'.
     * @see net.opengis.wcs20.ServiceParametersType#getExtension()
     * @see #getServiceParametersType()
     * @generated
     */
    EReference getServiceParametersType_Extension();

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
     * Returns the meta object for class '{@link net.opengis.wcs20.ExtensionItemType <em>Extension Item Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Extension Item Type</em>'.
     * @see net.opengis.wcs20.ExtensionItemType
     * @generated
     */
    EClass getExtensionItemType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.ExtensionItemType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.wcs20.ExtensionItemType#getName()
     * @see #getExtensionItemType()
     * @generated
     */
    EAttribute getExtensionItemType_Name();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.ExtensionItemType#getNamespace <em>Namespace</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Namespace</em>'.
     * @see net.opengis.wcs20.ExtensionItemType#getNamespace()
     * @see #getExtensionItemType()
     * @generated
     */
    EAttribute getExtensionItemType_Namespace();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs20.ExtensionItemType#getSimpleContent <em>Simple Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Simple Content</em>'.
     * @see net.opengis.wcs20.ExtensionItemType#getSimpleContent()
     * @see #getExtensionItemType()
     * @generated
     */
    EAttribute getExtensionItemType_SimpleContent();

    /**
     * Returns the meta object for the reference '{@link net.opengis.wcs20.ExtensionItemType#getObjectContent <em>Object Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Object Content</em>'.
     * @see net.opengis.wcs20.ExtensionItemType#getObjectContent()
     * @see #getExtensionItemType()
     * @generated
     */
    EReference getExtensionItemType_ObjectContent();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Version String Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Version String Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='VersionStringType' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='2\\.0\\.\\d+'"
     * @generated
     */
    EDataType getVersionStringType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Version String Type 1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Version String Type 1</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     * @generated
     */
    EDataType getVersionStringType_1();

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
    Wcs20Factory getWcs20Factory();

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
         * The meta object literal for the '{@link net.opengis.wcs20.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.CapabilitiesTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCapabilitiesType()
         * @generated
         */
        EClass CAPABILITIES_TYPE = eINSTANCE.getCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Service Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_TYPE__SERVICE_METADATA = eINSTANCE.getCapabilitiesType_ServiceMetadata();

        /**
         * The meta object literal for the '<em><b>Contents</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_TYPE__CONTENTS = eINSTANCE.getCapabilitiesType_Contents();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.ContentsTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getContentsType()
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
         * The meta object literal for the '<em><b>Extension</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTENTS_TYPE__EXTENSION = eINSTANCE.getContentsType_Extension();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.CoverageDescriptionsTypeImpl <em>Coverage Descriptions Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.CoverageDescriptionsTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageDescriptionsType()
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
         * The meta object literal for the '{@link net.opengis.wcs20.impl.CoverageDescriptionTypeImpl <em>Coverage Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.CoverageDescriptionTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageDescriptionType()
         * @generated
         */
        EClass COVERAGE_DESCRIPTION_TYPE = eINSTANCE.getCoverageDescriptionType();

        /**
         * The meta object literal for the '<em><b>Coverage Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_DESCRIPTION_TYPE__COVERAGE_ID = eINSTANCE.getCoverageDescriptionType_CoverageId();

        /**
         * The meta object literal for the '<em><b>Coverage Function</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DESCRIPTION_TYPE__COVERAGE_FUNCTION = eINSTANCE.getCoverageDescriptionType_CoverageFunction();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DESCRIPTION_TYPE__METADATA = eINSTANCE.getCoverageDescriptionType_Metadata();

        /**
         * The meta object literal for the '<em><b>Domain Set</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DESCRIPTION_TYPE__DOMAIN_SET = eINSTANCE.getCoverageDescriptionType_DomainSet();

        /**
         * The meta object literal for the '<em><b>Range Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DESCRIPTION_TYPE__RANGE_TYPE = eINSTANCE.getCoverageDescriptionType_RangeType();

        /**
         * The meta object literal for the '<em><b>Service Parameters</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DESCRIPTION_TYPE__SERVICE_PARAMETERS = eINSTANCE.getCoverageDescriptionType_ServiceParameters();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.CoverageOfferingsTypeImpl <em>Coverage Offerings Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.CoverageOfferingsTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageOfferingsType()
         * @generated
         */
        EClass COVERAGE_OFFERINGS_TYPE = eINSTANCE.getCoverageOfferingsType();

        /**
         * The meta object literal for the '<em><b>Service Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_OFFERINGS_TYPE__SERVICE_METADATA = eINSTANCE.getCoverageOfferingsType_ServiceMetadata();

        /**
         * The meta object literal for the '<em><b>Offered Coverage</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_OFFERINGS_TYPE__OFFERED_COVERAGE = eINSTANCE.getCoverageOfferingsType_OfferedCoverage();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.CoverageSubtypeParentTypeImpl <em>Coverage Subtype Parent Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.CoverageSubtypeParentTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageSubtypeParentType()
         * @generated
         */
        EClass COVERAGE_SUBTYPE_PARENT_TYPE = eINSTANCE.getCoverageSubtypeParentType();

        /**
         * The meta object literal for the '<em><b>Coverage Subtype</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE = eINSTANCE.getCoverageSubtypeParentType_CoverageSubtype();

        /**
         * The meta object literal for the '<em><b>Coverage Subtype Parent</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_SUBTYPE_PARENT_TYPE__COVERAGE_SUBTYPE_PARENT = eINSTANCE.getCoverageSubtypeParentType_CoverageSubtypeParent();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl <em>Coverage Summary Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.CoverageSummaryTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getCoverageSummaryType()
         * @generated
         */
        EClass COVERAGE_SUMMARY_TYPE = eINSTANCE.getCoverageSummaryType();

        /**
         * The meta object literal for the '<em><b>WGS84 Bounding Box</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX = eINSTANCE.getCoverageSummaryType_WGS84BoundingBox();

        /**
         * The meta object literal for the '<em><b>Coverage Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__COVERAGE_ID = eINSTANCE.getCoverageSummaryType_CoverageId();

        /**
         * The meta object literal for the '<em><b>Coverage Subtype</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE = eINSTANCE.getCoverageSummaryType_CoverageSubtype();

        /**
         * The meta object literal for the '<em><b>Coverage Subtype Parent</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT = eINSTANCE.getCoverageSummaryType_CoverageSubtypeParent();

        /**
         * The meta object literal for the '<em><b>Bounding Box Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP = eINSTANCE.getCoverageSummaryType_BoundingBoxGroup();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_SUMMARY_TYPE__BOUNDING_BOX = eINSTANCE.getCoverageSummaryType_BoundingBox();

        /**
         * The meta object literal for the '<em><b>Metadata Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__METADATA_GROUP = eINSTANCE.getCoverageSummaryType_MetadataGroup();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_SUMMARY_TYPE__METADATA = eINSTANCE.getCoverageSummaryType_Metadata();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.DescribeCoverageTypeImpl <em>Describe Coverage Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.DescribeCoverageTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDescribeCoverageType()
         * @generated
         */
        EClass DESCRIBE_COVERAGE_TYPE = eINSTANCE.getDescribeCoverageType();

        /**
         * The meta object literal for the '<em><b>Coverage Id</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DESCRIBE_COVERAGE_TYPE__COVERAGE_ID = eINSTANCE.getDescribeCoverageType_CoverageId();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.DimensionSliceTypeImpl <em>Dimension Slice Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.DimensionSliceTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDimensionSliceType()
         * @generated
         */
        EClass DIMENSION_SLICE_TYPE = eINSTANCE.getDimensionSliceType();

        /**
         * The meta object literal for the '<em><b>Slice Point</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_SLICE_TYPE__SLICE_POINT = eINSTANCE.getDimensionSliceType_SlicePoint();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.DimensionSubsetTypeImpl <em>Dimension Subset Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.DimensionSubsetTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDimensionSubsetType()
         * @generated
         */
        EClass DIMENSION_SUBSET_TYPE = eINSTANCE.getDimensionSubsetType();

        /**
         * The meta object literal for the '<em><b>Dimension</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_SUBSET_TYPE__DIMENSION = eINSTANCE.getDimensionSubsetType_Dimension();

        /**
         * The meta object literal for the '<em><b>CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_SUBSET_TYPE__CRS = eINSTANCE.getDimensionSubsetType_CRS();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.DimensionTrimTypeImpl <em>Dimension Trim Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.DimensionTrimTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDimensionTrimType()
         * @generated
         */
        EClass DIMENSION_TRIM_TYPE = eINSTANCE.getDimensionTrimType();

        /**
         * The meta object literal for the '<em><b>Trim Low</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_TRIM_TYPE__TRIM_LOW = eINSTANCE.getDimensionTrimType_TrimLow();

        /**
         * The meta object literal for the '<em><b>Trim High</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DIMENSION_TRIM_TYPE__TRIM_HIGH = eINSTANCE.getDimensionTrimType_TrimHigh();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.DocumentRootImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getDocumentRoot()
         * @generated
         */
        EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

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
         * The meta object literal for the '<em><b>Contents</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__CONTENTS = eINSTANCE.getDocumentRoot_Contents();

        /**
         * The meta object literal for the '<em><b>Coverage Description</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COVERAGE_DESCRIPTION = eINSTANCE.getDocumentRoot_CoverageDescription();

        /**
         * The meta object literal for the '<em><b>Coverage Descriptions</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS = eINSTANCE.getDocumentRoot_CoverageDescriptions();

        /**
         * The meta object literal for the '<em><b>Coverage Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__COVERAGE_ID = eINSTANCE.getDocumentRoot_CoverageId();

        /**
         * The meta object literal for the '<em><b>Coverage Offerings</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COVERAGE_OFFERINGS = eINSTANCE.getDocumentRoot_CoverageOfferings();

        /**
         * The meta object literal for the '<em><b>Coverage Subtype</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__COVERAGE_SUBTYPE = eINSTANCE.getDocumentRoot_CoverageSubtype();

        /**
         * The meta object literal for the '<em><b>Coverage Subtype Parent</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COVERAGE_SUBTYPE_PARENT = eINSTANCE.getDocumentRoot_CoverageSubtypeParent();

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
         * The meta object literal for the '<em><b>Dimension Slice</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DIMENSION_SLICE = eINSTANCE.getDocumentRoot_DimensionSlice();

        /**
         * The meta object literal for the '<em><b>Dimension Subset</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DIMENSION_SUBSET = eINSTANCE.getDocumentRoot_DimensionSubset();

        /**
         * The meta object literal for the '<em><b>Dimension Trim</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DIMENSION_TRIM = eINSTANCE.getDocumentRoot_DimensionTrim();

        /**
         * The meta object literal for the '<em><b>Extension</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXTENSION = eINSTANCE.getDocumentRoot_Extension();

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
         * The meta object literal for the '<em><b>Offered Coverage</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OFFERED_COVERAGE = eINSTANCE.getDocumentRoot_OfferedCoverage();

        /**
         * The meta object literal for the '<em><b>Service Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SERVICE_METADATA = eINSTANCE.getDocumentRoot_ServiceMetadata();

        /**
         * The meta object literal for the '<em><b>Service Parameters</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SERVICE_PARAMETERS = eINSTANCE.getDocumentRoot_ServiceParameters();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.ExtensionTypeImpl <em>Extension Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.ExtensionTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getExtensionType()
         * @generated
         */
        EClass EXTENSION_TYPE = eINSTANCE.getExtensionType();

        /**
         * The meta object literal for the '<em><b>Contents</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXTENSION_TYPE__CONTENTS = eINSTANCE.getExtensionType_Contents();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.GetCapabilitiesTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getGetCapabilitiesType()
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
         * The meta object literal for the '{@link net.opengis.wcs20.impl.GetCoverageTypeImpl <em>Get Coverage Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.GetCoverageTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getGetCoverageType()
         * @generated
         */
        EClass GET_COVERAGE_TYPE = eINSTANCE.getGetCoverageType();

        /**
         * The meta object literal for the '<em><b>Coverage Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_COVERAGE_TYPE__COVERAGE_ID = eINSTANCE.getGetCoverageType_CoverageId();

        /**
         * The meta object literal for the '<em><b>Dimension Subset Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP = eINSTANCE.getGetCoverageType_DimensionSubsetGroup();

        /**
         * The meta object literal for the '<em><b>Dimension Subset</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_COVERAGE_TYPE__DIMENSION_SUBSET = eINSTANCE.getGetCoverageType_DimensionSubset();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_COVERAGE_TYPE__FORMAT = eINSTANCE.getGetCoverageType_Format();

        /**
         * The meta object literal for the '<em><b>Media Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_COVERAGE_TYPE__MEDIA_TYPE = eINSTANCE.getGetCoverageType_MediaType();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.OfferedCoverageTypeImpl <em>Offered Coverage Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.OfferedCoverageTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getOfferedCoverageType()
         * @generated
         */
        EClass OFFERED_COVERAGE_TYPE = eINSTANCE.getOfferedCoverageType();

        /**
         * The meta object literal for the '<em><b>Abstract Coverage</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OFFERED_COVERAGE_TYPE__ABSTRACT_COVERAGE = eINSTANCE.getOfferedCoverageType_AbstractCoverage();

        /**
         * The meta object literal for the '<em><b>Service Parameters</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS = eINSTANCE.getOfferedCoverageType_ServiceParameters();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.RequestBaseTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getRequestBaseType()
         * @generated
         */
        EClass REQUEST_BASE_TYPE = eINSTANCE.getRequestBaseType();

        /**
         * The meta object literal for the '<em><b>Extension</b></em>' containment reference feature.
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
         * The meta object literal for the '<em><b>Base Url</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_BASE_TYPE__BASE_URL = eINSTANCE.getRequestBaseType_BaseUrl();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.ServiceMetadataTypeImpl <em>Service Metadata Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.ServiceMetadataTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getServiceMetadataType()
         * @generated
         */
        EClass SERVICE_METADATA_TYPE = eINSTANCE.getServiceMetadataType();

        /**
         * The meta object literal for the '<em><b>Format Supported</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_METADATA_TYPE__FORMAT_SUPPORTED = eINSTANCE.getServiceMetadataType_FormatSupported();

        /**
         * The meta object literal for the '<em><b>Extension</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_METADATA_TYPE__EXTENSION = eINSTANCE.getServiceMetadataType_Extension();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.ServiceParametersTypeImpl <em>Service Parameters Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.ServiceParametersTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getServiceParametersType()
         * @generated
         */
        EClass SERVICE_PARAMETERS_TYPE = eINSTANCE.getServiceParametersType();

        /**
         * The meta object literal for the '<em><b>Coverage Subtype</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE = eINSTANCE.getServiceParametersType_CoverageSubtype();

        /**
         * The meta object literal for the '<em><b>Coverage Subtype Parent</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_PARAMETERS_TYPE__COVERAGE_SUBTYPE_PARENT = eINSTANCE.getServiceParametersType_CoverageSubtypeParent();

        /**
         * The meta object literal for the '<em><b>Native Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_PARAMETERS_TYPE__NATIVE_FORMAT = eINSTANCE.getServiceParametersType_NativeFormat();

        /**
         * The meta object literal for the '<em><b>Extension</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_PARAMETERS_TYPE__EXTENSION = eINSTANCE.getServiceParametersType_Extension();

        /**
         * The meta object literal for the '{@link java.lang.Object <em>Object</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.Object
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getObject()
         * @generated
         */
        EClass OBJECT = eINSTANCE.getObject();

        /**
         * The meta object literal for the '{@link net.opengis.wcs20.impl.ExtensionItemTypeImpl <em>Extension Item Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs20.impl.ExtensionItemTypeImpl
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getExtensionItemType()
         * @generated
         */
        EClass EXTENSION_ITEM_TYPE = eINSTANCE.getExtensionItemType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXTENSION_ITEM_TYPE__NAME = eINSTANCE.getExtensionItemType_Name();

        /**
         * The meta object literal for the '<em><b>Namespace</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXTENSION_ITEM_TYPE__NAMESPACE = eINSTANCE.getExtensionItemType_Namespace();

        /**
         * The meta object literal for the '<em><b>Simple Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EXTENSION_ITEM_TYPE__SIMPLE_CONTENT = eINSTANCE.getExtensionItemType_SimpleContent();

        /**
         * The meta object literal for the '<em><b>Object Content</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXTENSION_ITEM_TYPE__OBJECT_CONTENT = eINSTANCE.getExtensionItemType_ObjectContent();

        /**
         * The meta object literal for the '<em>Version String Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getVersionStringType()
         * @generated
         */
        EDataType VERSION_STRING_TYPE = eINSTANCE.getVersionStringType();

        /**
         * The meta object literal for the '<em>Version String Type 1</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getVersionStringType_1()
         * @generated
         */
        EDataType VERSION_STRING_TYPE_1 = eINSTANCE.getVersionStringType_1();

        /**
         * The meta object literal for the '<em>QName</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see javax.xml.namespace.QName
         * @see net.opengis.wcs20.impl.Wcs20PackageImpl#getQName()
         * @generated
         */
        EDataType QNAME = eINSTANCE.getQName();

    }

} //Wcs20Package
