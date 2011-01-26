/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

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
 * <!-- end-model-doc -->
 * @see net.opengis.gml.GmlFactory
 * @model kind="package"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface GmlPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "gml";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.opengis.net/gml";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "gml";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GmlPackage eINSTANCE = net.opengis.gml.impl.GmlPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.AbstractGMLTypeImpl <em>Abstract GML Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.AbstractGMLTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractGMLType()
	 * @generated
	 */
	int ABSTRACT_GML_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GML_TYPE__META_DATA_PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GML_TYPE__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GML_TYPE__NAME = 2;

	/**
	 * The number of structural features of the '<em>Abstract GML Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GML_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.AbstractGeometryBaseTypeImpl <em>Abstract Geometry Base Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.AbstractGeometryBaseTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractGeometryBaseType()
	 * @generated
	 */
	int ABSTRACT_GEOMETRY_BASE_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_BASE_TYPE__META_DATA_PROPERTY = ABSTRACT_GML_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_BASE_TYPE__DESCRIPTION = ABSTRACT_GML_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_BASE_TYPE__NAME = ABSTRACT_GML_TYPE__NAME;

	/**
	 * The number of structural features of the '<em>Abstract Geometry Base Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_BASE_TYPE_FEATURE_COUNT = ABSTRACT_GML_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.AbstractGeometryTypeImpl <em>Abstract Geometry Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.AbstractGeometryTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractGeometryType()
	 * @generated
	 */
	int ABSTRACT_GEOMETRY_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_TYPE__META_DATA_PROPERTY = ABSTRACT_GEOMETRY_BASE_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_TYPE__DESCRIPTION = ABSTRACT_GEOMETRY_BASE_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_TYPE__NAME = ABSTRACT_GEOMETRY_BASE_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_TYPE__SRS_NAME = ABSTRACT_GEOMETRY_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Abstract Geometry Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT = ABSTRACT_GEOMETRY_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.AbstractGeometricPrimitiveTypeImpl <em>Abstract Geometric Primitive Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.AbstractGeometricPrimitiveTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractGeometricPrimitiveType()
	 * @generated
	 */
	int ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE__META_DATA_PROPERTY = ABSTRACT_GEOMETRY_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE__DESCRIPTION = ABSTRACT_GEOMETRY_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE__NAME = ABSTRACT_GEOMETRY_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE__SRS_NAME = ABSTRACT_GEOMETRY_TYPE__SRS_NAME;

	/**
	 * The number of structural features of the '<em>Abstract Geometric Primitive Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE_FEATURE_COUNT = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.AbstractMetaDataTypeImpl <em>Abstract Meta Data Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.AbstractMetaDataTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractMetaDataType()
	 * @generated
	 */
	int ABSTRACT_META_DATA_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_META_DATA_TYPE__MIXED = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_META_DATA_TYPE__ID = 1;

	/**
	 * The number of structural features of the '<em>Abstract Meta Data Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_META_DATA_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.AbstractRingPropertyTypeImpl <em>Abstract Ring Property Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.AbstractRingPropertyTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractRingPropertyType()
	 * @generated
	 */
	int ABSTRACT_RING_PROPERTY_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Linear Ring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING = 0;

	/**
	 * The number of structural features of the '<em>Abstract Ring Property Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_RING_PROPERTY_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.AbstractRingTypeImpl <em>Abstract Ring Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.AbstractRingTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractRingType()
	 * @generated
	 */
	int ABSTRACT_RING_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_RING_TYPE__META_DATA_PROPERTY = ABSTRACT_GEOMETRY_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_RING_TYPE__DESCRIPTION = ABSTRACT_GEOMETRY_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_RING_TYPE__NAME = ABSTRACT_GEOMETRY_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_RING_TYPE__SRS_NAME = ABSTRACT_GEOMETRY_TYPE__SRS_NAME;

	/**
	 * The number of structural features of the '<em>Abstract Ring Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_RING_TYPE_FEATURE_COUNT = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.AbstractSurfaceTypeImpl <em>Abstract Surface Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.AbstractSurfaceTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractSurfaceType()
	 * @generated
	 */
	int ABSTRACT_SURFACE_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SURFACE_TYPE__META_DATA_PROPERTY = ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SURFACE_TYPE__DESCRIPTION = ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SURFACE_TYPE__NAME = ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SURFACE_TYPE__SRS_NAME = ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE__SRS_NAME;

	/**
	 * The number of structural features of the '<em>Abstract Surface Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_SURFACE_TYPE_FEATURE_COUNT = ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.BoundingShapeTypeImpl <em>Bounding Shape Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.BoundingShapeTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getBoundingShapeType()
	 * @generated
	 */
	int BOUNDING_SHAPE_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Envelope Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP = 0;

	/**
	 * The feature id for the '<em><b>Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDING_SHAPE_TYPE__ENVELOPE = 1;

	/**
	 * The number of structural features of the '<em>Bounding Shape Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDING_SHAPE_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.CodeListTypeImpl <em>Code List Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.CodeListTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getCodeListType()
	 * @generated
	 */
	int CODE_LIST_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_LIST_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Code Space</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_LIST_TYPE__CODE_SPACE = 1;

	/**
	 * The number of structural features of the '<em>Code List Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_LIST_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.CodeTypeImpl <em>Code Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.CodeTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getCodeType()
	 * @generated
	 */
	int CODE_TYPE = 10;

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
	 * The meta object id for the '{@link net.opengis.gml.impl.DirectPositionTypeImpl <em>Direct Position Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.DirectPositionTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getDirectPositionType()
	 * @generated
	 */
	int DIRECT_POSITION_TYPE = 11;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT_POSITION_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Dimension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT_POSITION_TYPE__DIMENSION = 1;

	/**
	 * The number of structural features of the '<em>Direct Position Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT_POSITION_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.DocumentRootImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 12;

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
	 * The feature id for the '<em><b>Geometric Primitive</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GEOMETRIC_PRIMITIVE = 3;

	/**
	 * The feature id for the '<em><b>Geometry</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GEOMETRY = 4;

	/**
	 * The feature id for the '<em><b>GML</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GML = 5;

	/**
	 * The feature id for the '<em><b>Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__OBJECT = 6;

	/**
	 * The feature id for the '<em><b>Meta Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__META_DATA = 7;

	/**
	 * The feature id for the '<em><b>Ring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__RING = 8;

	/**
	 * The feature id for the '<em><b>Surface</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__SURFACE = 9;

	/**
	 * The feature id for the '<em><b>Bounded By</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__BOUNDED_BY = 10;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DESCRIPTION = 11;

	/**
	 * The feature id for the '<em><b>Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__ENVELOPE = 12;

	/**
	 * The feature id for the '<em><b>Envelope With Time Period</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD = 13;

	/**
	 * The feature id for the '<em><b>Exterior</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__EXTERIOR = 14;

	/**
	 * The feature id for the '<em><b>Grid</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GRID = 15;

	/**
	 * The feature id for the '<em><b>Interior</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__INTERIOR = 16;

	/**
	 * The feature id for the '<em><b>Linear Ring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__LINEAR_RING = 17;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__META_DATA_PROPERTY = 18;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__NAME = 19;

	/**
	 * The feature id for the '<em><b>Polygon</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__POLYGON = 20;

	/**
	 * The feature id for the '<em><b>Pos</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__POS = 21;

	/**
	 * The feature id for the '<em><b>Rectified Grid</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__RECTIFIED_GRID = 22;

	/**
	 * The feature id for the '<em><b>Time Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__TIME_POSITION = 23;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__ID = 24;

	/**
	 * The feature id for the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__REMOTE_SCHEMA = 25;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 26;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.EnvelopeTypeImpl <em>Envelope Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.EnvelopeTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getEnvelopeType()
	 * @generated
	 */
	int ENVELOPE_TYPE = 13;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_TYPE__META_DATA_PROPERTY = ABSTRACT_GEOMETRY_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_TYPE__DESCRIPTION = ABSTRACT_GEOMETRY_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_TYPE__NAME = ABSTRACT_GEOMETRY_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_TYPE__SRS_NAME = ABSTRACT_GEOMETRY_TYPE__SRS_NAME;

	/**
	 * The feature id for the '<em><b>Pos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_TYPE__POS = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Envelope Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_TYPE_FEATURE_COUNT = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.EnvelopeWithTimePeriodTypeImpl <em>Envelope With Time Period Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.EnvelopeWithTimePeriodTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getEnvelopeWithTimePeriodType()
	 * @generated
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE = 14;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE__META_DATA_PROPERTY = ENVELOPE_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE__DESCRIPTION = ENVELOPE_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE__NAME = ENVELOPE_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE__SRS_NAME = ENVELOPE_TYPE__SRS_NAME;

	/**
	 * The feature id for the '<em><b>Pos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE__POS = ENVELOPE_TYPE__POS;

	/**
	 * The feature id for the '<em><b>Time Position</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION = ENVELOPE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Frame</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME = ENVELOPE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Envelope With Time Period Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENVELOPE_WITH_TIME_PERIOD_TYPE_FEATURE_COUNT = ENVELOPE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.GridEnvelopeTypeImpl <em>Grid Envelope Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.GridEnvelopeTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getGridEnvelopeType()
	 * @generated
	 */
	int GRID_ENVELOPE_TYPE = 15;

	/**
	 * The feature id for the '<em><b>Low</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_ENVELOPE_TYPE__LOW = 0;

	/**
	 * The feature id for the '<em><b>High</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_ENVELOPE_TYPE__HIGH = 1;

	/**
	 * The number of structural features of the '<em>Grid Envelope Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_ENVELOPE_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.GridLimitsTypeImpl <em>Grid Limits Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.GridLimitsTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getGridLimitsType()
	 * @generated
	 */
	int GRID_LIMITS_TYPE = 16;

	/**
	 * The feature id for the '<em><b>Grid Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_LIMITS_TYPE__GRID_ENVELOPE = 0;

	/**
	 * The number of structural features of the '<em>Grid Limits Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_LIMITS_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.GridTypeImpl <em>Grid Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.GridTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getGridType()
	 * @generated
	 */
	int GRID_TYPE = 17;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_TYPE__META_DATA_PROPERTY = ABSTRACT_GEOMETRY_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_TYPE__DESCRIPTION = ABSTRACT_GEOMETRY_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_TYPE__NAME = ABSTRACT_GEOMETRY_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_TYPE__SRS_NAME = ABSTRACT_GEOMETRY_TYPE__SRS_NAME;

	/**
	 * The feature id for the '<em><b>Limits</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_TYPE__LIMITS = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Axis Name</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_TYPE__AXIS_NAME = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Dimension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_TYPE__DIMENSION = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Grid Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRID_TYPE_FEATURE_COUNT = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.LinearRingTypeImpl <em>Linear Ring Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.LinearRingTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getLinearRingType()
	 * @generated
	 */
	int LINEAR_RING_TYPE = 18;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINEAR_RING_TYPE__META_DATA_PROPERTY = ABSTRACT_RING_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINEAR_RING_TYPE__DESCRIPTION = ABSTRACT_RING_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINEAR_RING_TYPE__NAME = ABSTRACT_RING_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINEAR_RING_TYPE__SRS_NAME = ABSTRACT_RING_TYPE__SRS_NAME;

	/**
	 * The feature id for the '<em><b>Pos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINEAR_RING_TYPE__POS = ABSTRACT_RING_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Linear Ring Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINEAR_RING_TYPE_FEATURE_COUNT = ABSTRACT_RING_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.MetaDataPropertyTypeImpl <em>Meta Data Property Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.MetaDataPropertyTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getMetaDataPropertyType()
	 * @generated
	 */
	int META_DATA_PROPERTY_TYPE = 19;

	/**
	 * The feature id for the '<em><b>Meta Data Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__META_DATA_GROUP = 0;

	/**
	 * The feature id for the '<em><b>Meta Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__META_DATA = 1;

	/**
	 * The feature id for the '<em><b>About</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__ABOUT = 2;

	/**
	 * The feature id for the '<em><b>Actuate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__ACTUATE = 3;

	/**
	 * The feature id for the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__ARCROLE = 4;

	/**
	 * The feature id for the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__HREF = 5;

	/**
	 * The feature id for the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__REMOTE_SCHEMA = 6;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__ROLE = 7;

	/**
	 * The feature id for the '<em><b>Show</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__SHOW = 8;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__TITLE = 9;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE__TYPE = 10;

	/**
	 * The number of structural features of the '<em>Meta Data Property Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_DATA_PROPERTY_TYPE_FEATURE_COUNT = 11;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.PointTypeImpl <em>Point Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.PointTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getPointType()
	 * @generated
	 */
	int POINT_TYPE = 20;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT_TYPE__META_DATA_PROPERTY = ABSTRACT_GEOMETRY_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT_TYPE__DESCRIPTION = ABSTRACT_GEOMETRY_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT_TYPE__NAME = ABSTRACT_GEOMETRY_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT_TYPE__SRS_NAME = ABSTRACT_GEOMETRY_TYPE__SRS_NAME;

	/**
	 * The feature id for the '<em><b>Pos</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT_TYPE__POS = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Point Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT_TYPE_FEATURE_COUNT = ABSTRACT_GEOMETRY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.PolygonTypeImpl <em>Polygon Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.PolygonTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getPolygonType()
	 * @generated
	 */
	int POLYGON_TYPE = 21;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYGON_TYPE__META_DATA_PROPERTY = ABSTRACT_SURFACE_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYGON_TYPE__DESCRIPTION = ABSTRACT_SURFACE_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYGON_TYPE__NAME = ABSTRACT_SURFACE_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYGON_TYPE__SRS_NAME = ABSTRACT_SURFACE_TYPE__SRS_NAME;

	/**
	 * The feature id for the '<em><b>Exterior</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYGON_TYPE__EXTERIOR = ABSTRACT_SURFACE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Interior</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYGON_TYPE__INTERIOR = ABSTRACT_SURFACE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Polygon Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYGON_TYPE_FEATURE_COUNT = ABSTRACT_SURFACE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.RectifiedGridTypeImpl <em>Rectified Grid Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.RectifiedGridTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getRectifiedGridType()
	 * @generated
	 */
	int RECTIFIED_GRID_TYPE = 22;

	/**
	 * The feature id for the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__META_DATA_PROPERTY = GRID_TYPE__META_DATA_PROPERTY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__DESCRIPTION = GRID_TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__NAME = GRID_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__SRS_NAME = GRID_TYPE__SRS_NAME;

	/**
	 * The feature id for the '<em><b>Limits</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__LIMITS = GRID_TYPE__LIMITS;

	/**
	 * The feature id for the '<em><b>Axis Name</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__AXIS_NAME = GRID_TYPE__AXIS_NAME;

	/**
	 * The feature id for the '<em><b>Dimension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__DIMENSION = GRID_TYPE__DIMENSION;

	/**
	 * The feature id for the '<em><b>Origin</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__ORIGIN = GRID_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Offset Vector</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE__OFFSET_VECTOR = GRID_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Rectified Grid Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECTIFIED_GRID_TYPE_FEATURE_COUNT = GRID_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.ReferenceTypeImpl <em>Reference Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.ReferenceTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getReferenceType()
	 * @generated
	 */
	int REFERENCE_TYPE = 23;

	/**
	 * The feature id for the '<em><b>Actuate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__ACTUATE = 0;

	/**
	 * The feature id for the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__ARCROLE = 1;

	/**
	 * The feature id for the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__HREF = 2;

	/**
	 * The feature id for the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__REMOTE_SCHEMA = 3;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__ROLE = 4;

	/**
	 * The feature id for the '<em><b>Show</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__SHOW = 5;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__TITLE = 6;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE__TYPE = 7;

	/**
	 * The number of structural features of the '<em>Reference Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TYPE_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.StringOrRefTypeImpl <em>String Or Ref Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.StringOrRefTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getStringOrRefType()
	 * @generated
	 */
	int STRING_OR_REF_TYPE = 24;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Actuate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__ACTUATE = 1;

	/**
	 * The feature id for the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__ARCROLE = 2;

	/**
	 * The feature id for the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__HREF = 3;

	/**
	 * The feature id for the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__REMOTE_SCHEMA = 4;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__ROLE = 5;

	/**
	 * The feature id for the '<em><b>Show</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__SHOW = 6;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__TITLE = 7;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE__TYPE = 8;

	/**
	 * The number of structural features of the '<em>String Or Ref Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_OR_REF_TYPE_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.TimePositionTypeImpl <em>Time Position Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.TimePositionTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getTimePositionType()
	 * @generated
	 */
	int TIME_POSITION_TYPE = 25;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_POSITION_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Calendar Era Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_POSITION_TYPE__CALENDAR_ERA_NAME = 1;

	/**
	 * The feature id for the '<em><b>Frame</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_POSITION_TYPE__FRAME = 2;

	/**
	 * The feature id for the '<em><b>Indeterminate Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_POSITION_TYPE__INDETERMINATE_POSITION = 3;

	/**
	 * The number of structural features of the '<em>Time Position Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_POSITION_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link net.opengis.gml.impl.VectorTypeImpl <em>Vector Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.impl.VectorTypeImpl
	 * @see net.opengis.gml.impl.GmlPackageImpl#getVectorType()
	 * @generated
	 */
	int VECTOR_TYPE = 26;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VECTOR_TYPE__VALUE = DIRECT_POSITION_TYPE__VALUE;

	/**
	 * The feature id for the '<em><b>Dimension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VECTOR_TYPE__DIMENSION = DIRECT_POSITION_TYPE__DIMENSION;

	/**
	 * The number of structural features of the '<em>Vector Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VECTOR_TYPE_FEATURE_COUNT = DIRECT_POSITION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.opengis.gml.TimeIndeterminateValueType <em>Time Indeterminate Value Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.TimeIndeterminateValueType
	 * @see net.opengis.gml.impl.GmlPackageImpl#getTimeIndeterminateValueType()
	 * @generated
	 */
	int TIME_INDETERMINATE_VALUE_TYPE = 27;

	/**
	 * The meta object id for the '<em>Double List</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see net.opengis.gml.impl.GmlPackageImpl#getDoubleList()
	 * @generated
	 */
	int DOUBLE_LIST = 28;

	/**
	 * The meta object id for the '<em>Integer List</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see net.opengis.gml.impl.GmlPackageImpl#getIntegerList()
	 * @generated
	 */
	int INTEGER_LIST = 29;

	/**
	 * The meta object id for the '<em>Name List</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see net.opengis.gml.impl.GmlPackageImpl#getNameList()
	 * @generated
	 */
	int NAME_LIST = 30;

	/**
	 * The meta object id for the '<em>Temporal Position Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see net.opengis.gml.impl.GmlPackageImpl#getTemporalPositionType()
	 * @generated
	 */
	int TEMPORAL_POSITION_TYPE = 31;

	/**
	 * The meta object id for the '<em>Time Duration Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see net.opengis.gml.impl.GmlPackageImpl#getTimeDurationType()
	 * @generated
	 */
	int TIME_DURATION_TYPE = 32;

	/**
	 * The meta object id for the '<em>Time Indeterminate Value Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.gml.TimeIndeterminateValueType
	 * @see net.opengis.gml.impl.GmlPackageImpl#getTimeIndeterminateValueTypeObject()
	 * @generated
	 */
	int TIME_INDETERMINATE_VALUE_TYPE_OBJECT = 33;

	/**
	 * The meta object id for the '<em>Vector Type Base</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see net.opengis.gml.impl.GmlPackageImpl#getVectorTypeBase()
	 * @generated
	 */
	int VECTOR_TYPE_BASE = 34;


	/**
	 * The meta object id for the '<em>Grid Envelope</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.opengis.coverage.grid.GridEnvelope
	 * @see net.opengis.gml.impl.GmlPackageImpl#getGridEnvelope()
	 * @generated
	 */
	int GRID_ENVELOPE = 35;


	/**
	 * Returns the meta object for class '{@link net.opengis.gml.AbstractGeometricPrimitiveType <em>Abstract Geometric Primitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Geometric Primitive Type</em>'.
	 * @see net.opengis.gml.AbstractGeometricPrimitiveType
	 * @generated
	 */
	EClass getAbstractGeometricPrimitiveType();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.AbstractGeometryBaseType <em>Abstract Geometry Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Geometry Base Type</em>'.
	 * @see net.opengis.gml.AbstractGeometryBaseType
	 * @generated
	 */
	EClass getAbstractGeometryBaseType();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.AbstractGeometryType <em>Abstract Geometry Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Geometry Type</em>'.
	 * @see net.opengis.gml.AbstractGeometryType
	 * @generated
	 */
	EClass getAbstractGeometryType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.AbstractGeometryType#getSrsName <em>Srs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Srs Name</em>'.
	 * @see net.opengis.gml.AbstractGeometryType#getSrsName()
	 * @see #getAbstractGeometryType()
	 * @generated
	 */
	EAttribute getAbstractGeometryType_SrsName();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.AbstractGMLType <em>Abstract GML Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract GML Type</em>'.
	 * @see net.opengis.gml.AbstractGMLType
	 * @generated
	 */
	EClass getAbstractGMLType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.gml.AbstractGMLType#getMetaDataProperty <em>Meta Data Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Meta Data Property</em>'.
	 * @see net.opengis.gml.AbstractGMLType#getMetaDataProperty()
	 * @see #getAbstractGMLType()
	 * @generated
	 */
	EReference getAbstractGMLType_MetaDataProperty();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.AbstractGMLType#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Description</em>'.
	 * @see net.opengis.gml.AbstractGMLType#getDescription()
	 * @see #getAbstractGMLType()
	 * @generated
	 */
	EReference getAbstractGMLType_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.gml.AbstractGMLType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Name</em>'.
	 * @see net.opengis.gml.AbstractGMLType#getName()
	 * @see #getAbstractGMLType()
	 * @generated
	 */
	EReference getAbstractGMLType_Name();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.AbstractMetaDataType <em>Abstract Meta Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Meta Data Type</em>'.
	 * @see net.opengis.gml.AbstractMetaDataType
	 * @generated
	 */
	EClass getAbstractMetaDataType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.gml.AbstractMetaDataType#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see net.opengis.gml.AbstractMetaDataType#getMixed()
	 * @see #getAbstractMetaDataType()
	 * @generated
	 */
	EAttribute getAbstractMetaDataType_Mixed();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.AbstractMetaDataType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.opengis.gml.AbstractMetaDataType#getId()
	 * @see #getAbstractMetaDataType()
	 * @generated
	 */
	EAttribute getAbstractMetaDataType_Id();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.AbstractRingPropertyType <em>Abstract Ring Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Ring Property Type</em>'.
	 * @see net.opengis.gml.AbstractRingPropertyType
	 * @generated
	 */
	EClass getAbstractRingPropertyType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.AbstractRingPropertyType#getLinearRing <em>Linear Ring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Linear Ring</em>'.
	 * @see net.opengis.gml.AbstractRingPropertyType#getLinearRing()
	 * @see #getAbstractRingPropertyType()
	 * @generated
	 */
	EReference getAbstractRingPropertyType_LinearRing();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.AbstractRingType <em>Abstract Ring Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Ring Type</em>'.
	 * @see net.opengis.gml.AbstractRingType
	 * @generated
	 */
	EClass getAbstractRingType();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.AbstractSurfaceType <em>Abstract Surface Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Surface Type</em>'.
	 * @see net.opengis.gml.AbstractSurfaceType
	 * @generated
	 */
	EClass getAbstractSurfaceType();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.BoundingShapeType <em>Bounding Shape Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bounding Shape Type</em>'.
	 * @see net.opengis.gml.BoundingShapeType
	 * @generated
	 */
	EClass getBoundingShapeType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.gml.BoundingShapeType#getEnvelopeGroup <em>Envelope Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Envelope Group</em>'.
	 * @see net.opengis.gml.BoundingShapeType#getEnvelopeGroup()
	 * @see #getBoundingShapeType()
	 * @generated
	 */
	EAttribute getBoundingShapeType_EnvelopeGroup();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.BoundingShapeType#getEnvelope <em>Envelope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Envelope</em>'.
	 * @see net.opengis.gml.BoundingShapeType#getEnvelope()
	 * @see #getBoundingShapeType()
	 * @generated
	 */
	EReference getBoundingShapeType_Envelope();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.CodeListType <em>Code List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Code List Type</em>'.
	 * @see net.opengis.gml.CodeListType
	 * @generated
	 */
	EClass getCodeListType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.CodeListType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.gml.CodeListType#getValue()
	 * @see #getCodeListType()
	 * @generated
	 */
	EAttribute getCodeListType_Value();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.CodeListType#getCodeSpace <em>Code Space</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Code Space</em>'.
	 * @see net.opengis.gml.CodeListType#getCodeSpace()
	 * @see #getCodeListType()
	 * @generated
	 */
	EAttribute getCodeListType_CodeSpace();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.CodeType <em>Code Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Code Type</em>'.
	 * @see net.opengis.gml.CodeType
	 * @generated
	 */
	EClass getCodeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.CodeType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.gml.CodeType#getValue()
	 * @see #getCodeType()
	 * @generated
	 */
	EAttribute getCodeType_Value();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.CodeType#getCodeSpace <em>Code Space</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Code Space</em>'.
	 * @see net.opengis.gml.CodeType#getCodeSpace()
	 * @see #getCodeType()
	 * @generated
	 */
	EAttribute getCodeType_CodeSpace();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.DirectPositionType <em>Direct Position Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Direct Position Type</em>'.
	 * @see net.opengis.gml.DirectPositionType
	 * @generated
	 */
	EClass getDirectPositionType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.DirectPositionType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.gml.DirectPositionType#getValue()
	 * @see #getDirectPositionType()
	 * @generated
	 */
	EAttribute getDirectPositionType_Value();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.DirectPositionType#getDimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dimension</em>'.
	 * @see net.opengis.gml.DirectPositionType#getDimension()
	 * @see #getDirectPositionType()
	 * @generated
	 */
	EAttribute getDirectPositionType_Dimension();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see net.opengis.gml.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.gml.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see net.opengis.gml.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link net.opengis.gml.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see net.opengis.gml.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link net.opengis.gml.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see net.opengis.gml.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getGeometricPrimitive <em>Geometric Primitive</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Geometric Primitive</em>'.
	 * @see net.opengis.gml.DocumentRoot#getGeometricPrimitive()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GeometricPrimitive();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getGeometry <em>Geometry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Geometry</em>'.
	 * @see net.opengis.gml.DocumentRoot#getGeometry()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Geometry();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getGML <em>GML</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>GML</em>'.
	 * @see net.opengis.gml.DocumentRoot#getGML()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GML();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Object</em>'.
	 * @see net.opengis.gml.DocumentRoot#getObject()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Object();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getMetaData <em>Meta Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Meta Data</em>'.
	 * @see net.opengis.gml.DocumentRoot#getMetaData()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_MetaData();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getRing <em>Ring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ring</em>'.
	 * @see net.opengis.gml.DocumentRoot#getRing()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Ring();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getSurface <em>Surface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Surface</em>'.
	 * @see net.opengis.gml.DocumentRoot#getSurface()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Surface();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getBoundedBy <em>Bounded By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Bounded By</em>'.
	 * @see net.opengis.gml.DocumentRoot#getBoundedBy()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_BoundedBy();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Description</em>'.
	 * @see net.opengis.gml.DocumentRoot#getDescription()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Description();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getEnvelope <em>Envelope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Envelope</em>'.
	 * @see net.opengis.gml.DocumentRoot#getEnvelope()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Envelope();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getEnvelopeWithTimePeriod <em>Envelope With Time Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Envelope With Time Period</em>'.
	 * @see net.opengis.gml.DocumentRoot#getEnvelopeWithTimePeriod()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_EnvelopeWithTimePeriod();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getExterior <em>Exterior</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Exterior</em>'.
	 * @see net.opengis.gml.DocumentRoot#getExterior()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Exterior();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getGrid <em>Grid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Grid</em>'.
	 * @see net.opengis.gml.DocumentRoot#getGrid()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Grid();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getInterior <em>Interior</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Interior</em>'.
	 * @see net.opengis.gml.DocumentRoot#getInterior()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Interior();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getLinearRing <em>Linear Ring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Linear Ring</em>'.
	 * @see net.opengis.gml.DocumentRoot#getLinearRing()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_LinearRing();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getMetaDataProperty <em>Meta Data Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Meta Data Property</em>'.
	 * @see net.opengis.gml.DocumentRoot#getMetaDataProperty()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_MetaDataProperty();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Name</em>'.
	 * @see net.opengis.gml.DocumentRoot#getName()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Name();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getPolygon <em>Polygon</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Polygon</em>'.
	 * @see net.opengis.gml.DocumentRoot#getPolygon()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Polygon();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getPos <em>Pos</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Pos</em>'.
	 * @see net.opengis.gml.DocumentRoot#getPos()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Pos();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getRectifiedGrid <em>Rectified Grid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Rectified Grid</em>'.
	 * @see net.opengis.gml.DocumentRoot#getRectifiedGrid()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_RectifiedGrid();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.DocumentRoot#getTimePosition <em>Time Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Time Position</em>'.
	 * @see net.opengis.gml.DocumentRoot#getTimePosition()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_TimePosition();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.DocumentRoot#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.opengis.gml.DocumentRoot#getId()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Id();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.DocumentRoot#getRemoteSchema <em>Remote Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote Schema</em>'.
	 * @see net.opengis.gml.DocumentRoot#getRemoteSchema()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_RemoteSchema();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.EnvelopeType <em>Envelope Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Envelope Type</em>'.
	 * @see net.opengis.gml.EnvelopeType
	 * @generated
	 */
	EClass getEnvelopeType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.gml.EnvelopeType#getPos <em>Pos</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Pos</em>'.
	 * @see net.opengis.gml.EnvelopeType#getPos()
	 * @see #getEnvelopeType()
	 * @generated
	 */
	EReference getEnvelopeType_Pos();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.EnvelopeWithTimePeriodType <em>Envelope With Time Period Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Envelope With Time Period Type</em>'.
	 * @see net.opengis.gml.EnvelopeWithTimePeriodType
	 * @generated
	 */
	EClass getEnvelopeWithTimePeriodType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.gml.EnvelopeWithTimePeriodType#getTimePosition <em>Time Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Time Position</em>'.
	 * @see net.opengis.gml.EnvelopeWithTimePeriodType#getTimePosition()
	 * @see #getEnvelopeWithTimePeriodType()
	 * @generated
	 */
	EReference getEnvelopeWithTimePeriodType_TimePosition();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.EnvelopeWithTimePeriodType#getFrame <em>Frame</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Frame</em>'.
	 * @see net.opengis.gml.EnvelopeWithTimePeriodType#getFrame()
	 * @see #getEnvelopeWithTimePeriodType()
	 * @generated
	 */
	EAttribute getEnvelopeWithTimePeriodType_Frame();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.GridEnvelopeType <em>Grid Envelope Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Grid Envelope Type</em>'.
	 * @see net.opengis.gml.GridEnvelopeType
	 * @generated
	 */
	EClass getGridEnvelopeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.GridEnvelopeType#getLow <em>Low</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Low</em>'.
	 * @see net.opengis.gml.GridEnvelopeType#getLow()
	 * @see #getGridEnvelopeType()
	 * @generated
	 */
	EAttribute getGridEnvelopeType_Low();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.GridEnvelopeType#getHigh <em>High</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>High</em>'.
	 * @see net.opengis.gml.GridEnvelopeType#getHigh()
	 * @see #getGridEnvelopeType()
	 * @generated
	 */
	EAttribute getGridEnvelopeType_High();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.GridLimitsType <em>Grid Limits Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Grid Limits Type</em>'.
	 * @see net.opengis.gml.GridLimitsType
	 * @generated
	 */
	EClass getGridLimitsType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.GridLimitsType#getGridEnvelope <em>Grid Envelope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Grid Envelope</em>'.
	 * @see net.opengis.gml.GridLimitsType#getGridEnvelope()
	 * @see #getGridLimitsType()
	 * @generated
	 */
	EReference getGridLimitsType_GridEnvelope();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.GridType <em>Grid Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Grid Type</em>'.
	 * @see net.opengis.gml.GridType
	 * @generated
	 */
	EClass getGridType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.GridType#getLimits <em>Limits</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Limits</em>'.
	 * @see net.opengis.gml.GridType#getLimits()
	 * @see #getGridType()
	 * @generated
	 */
	EAttribute getGridType_Limits();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.gml.GridType#getAxisName <em>Axis Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Axis Name</em>'.
	 * @see net.opengis.gml.GridType#getAxisName()
	 * @see #getGridType()
	 * @generated
	 */
	EAttribute getGridType_AxisName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.GridType#getDimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dimension</em>'.
	 * @see net.opengis.gml.GridType#getDimension()
	 * @see #getGridType()
	 * @generated
	 */
	EAttribute getGridType_Dimension();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.LinearRingType <em>Linear Ring Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Linear Ring Type</em>'.
	 * @see net.opengis.gml.LinearRingType
	 * @generated
	 */
	EClass getLinearRingType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.gml.LinearRingType#getPos <em>Pos</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Pos</em>'.
	 * @see net.opengis.gml.LinearRingType#getPos()
	 * @see #getLinearRingType()
	 * @generated
	 */
	EReference getLinearRingType_Pos();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.MetaDataPropertyType <em>Meta Data Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Meta Data Property Type</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType
	 * @generated
	 */
	EClass getMetaDataPropertyType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.gml.MetaDataPropertyType#getMetaDataGroup <em>Meta Data Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Meta Data Group</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getMetaDataGroup()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_MetaDataGroup();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.MetaDataPropertyType#getMetaData <em>Meta Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Meta Data</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getMetaData()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EReference getMetaDataPropertyType_MetaData();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getAbout <em>About</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>About</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getAbout()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_About();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getActuate <em>Actuate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Actuate</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getActuate()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_Actuate();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getArcrole <em>Arcrole</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arcrole</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getArcrole()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_Arcrole();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getHref <em>Href</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Href</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getHref()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_Href();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getRemoteSchema <em>Remote Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote Schema</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getRemoteSchema()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_RemoteSchema();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getRole <em>Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Role</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getRole()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_Role();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getShow <em>Show</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getShow()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_Show();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getTitle()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_Title();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.MetaDataPropertyType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.gml.MetaDataPropertyType#getType()
	 * @see #getMetaDataPropertyType()
	 * @generated
	 */
	EAttribute getMetaDataPropertyType_Type();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.PointType <em>Point Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Point Type</em>'.
	 * @see net.opengis.gml.PointType
	 * @generated
	 */
	EClass getPointType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.PointType#getPos <em>Pos</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Pos</em>'.
	 * @see net.opengis.gml.PointType#getPos()
	 * @see #getPointType()
	 * @generated
	 */
	EReference getPointType_Pos();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.PolygonType <em>Polygon Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Polygon Type</em>'.
	 * @see net.opengis.gml.PolygonType
	 * @generated
	 */
	EClass getPolygonType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.PolygonType#getExterior <em>Exterior</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Exterior</em>'.
	 * @see net.opengis.gml.PolygonType#getExterior()
	 * @see #getPolygonType()
	 * @generated
	 */
	EReference getPolygonType_Exterior();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.gml.PolygonType#getInterior <em>Interior</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Interior</em>'.
	 * @see net.opengis.gml.PolygonType#getInterior()
	 * @see #getPolygonType()
	 * @generated
	 */
	EReference getPolygonType_Interior();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.RectifiedGridType <em>Rectified Grid Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rectified Grid Type</em>'.
	 * @see net.opengis.gml.RectifiedGridType
	 * @generated
	 */
	EClass getRectifiedGridType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.gml.RectifiedGridType#getOrigin <em>Origin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Origin</em>'.
	 * @see net.opengis.gml.RectifiedGridType#getOrigin()
	 * @see #getRectifiedGridType()
	 * @generated
	 */
	EReference getRectifiedGridType_Origin();

	/**
	 * Returns the meta object for the reference list '{@link net.opengis.gml.RectifiedGridType#getOffsetVector <em>Offset Vector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Offset Vector</em>'.
	 * @see net.opengis.gml.RectifiedGridType#getOffsetVector()
	 * @see #getRectifiedGridType()
	 * @generated
	 */
	EReference getRectifiedGridType_OffsetVector();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.ReferenceType <em>Reference Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Type</em>'.
	 * @see net.opengis.gml.ReferenceType
	 * @generated
	 */
	EClass getReferenceType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.ReferenceType#getActuate <em>Actuate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Actuate</em>'.
	 * @see net.opengis.gml.ReferenceType#getActuate()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Actuate();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.ReferenceType#getArcrole <em>Arcrole</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arcrole</em>'.
	 * @see net.opengis.gml.ReferenceType#getArcrole()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Arcrole();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.ReferenceType#getHref <em>Href</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Href</em>'.
	 * @see net.opengis.gml.ReferenceType#getHref()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Href();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.ReferenceType#getRemoteSchema <em>Remote Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote Schema</em>'.
	 * @see net.opengis.gml.ReferenceType#getRemoteSchema()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_RemoteSchema();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.ReferenceType#getRole <em>Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Role</em>'.
	 * @see net.opengis.gml.ReferenceType#getRole()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Role();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.ReferenceType#getShow <em>Show</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show</em>'.
	 * @see net.opengis.gml.ReferenceType#getShow()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Show();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.ReferenceType#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see net.opengis.gml.ReferenceType#getTitle()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Title();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.ReferenceType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.gml.ReferenceType#getType()
	 * @see #getReferenceType()
	 * @generated
	 */
	EAttribute getReferenceType_Type();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.StringOrRefType <em>String Or Ref Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>String Or Ref Type</em>'.
	 * @see net.opengis.gml.StringOrRefType
	 * @generated
	 */
	EClass getStringOrRefType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.gml.StringOrRefType#getValue()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_Value();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getActuate <em>Actuate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Actuate</em>'.
	 * @see net.opengis.gml.StringOrRefType#getActuate()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_Actuate();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getArcrole <em>Arcrole</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arcrole</em>'.
	 * @see net.opengis.gml.StringOrRefType#getArcrole()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_Arcrole();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getHref <em>Href</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Href</em>'.
	 * @see net.opengis.gml.StringOrRefType#getHref()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_Href();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getRemoteSchema <em>Remote Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Remote Schema</em>'.
	 * @see net.opengis.gml.StringOrRefType#getRemoteSchema()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_RemoteSchema();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getRole <em>Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Role</em>'.
	 * @see net.opengis.gml.StringOrRefType#getRole()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_Role();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getShow <em>Show</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show</em>'.
	 * @see net.opengis.gml.StringOrRefType#getShow()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_Show();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see net.opengis.gml.StringOrRefType#getTitle()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_Title();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.StringOrRefType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.gml.StringOrRefType#getType()
	 * @see #getStringOrRefType()
	 * @generated
	 */
	EAttribute getStringOrRefType_Type();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.TimePositionType <em>Time Position Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Position Type</em>'.
	 * @see net.opengis.gml.TimePositionType
	 * @generated
	 */
	EClass getTimePositionType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.TimePositionType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.gml.TimePositionType#getValue()
	 * @see #getTimePositionType()
	 * @generated
	 */
	EAttribute getTimePositionType_Value();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.TimePositionType#getCalendarEraName <em>Calendar Era Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Calendar Era Name</em>'.
	 * @see net.opengis.gml.TimePositionType#getCalendarEraName()
	 * @see #getTimePositionType()
	 * @generated
	 */
	EAttribute getTimePositionType_CalendarEraName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.TimePositionType#getFrame <em>Frame</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Frame</em>'.
	 * @see net.opengis.gml.TimePositionType#getFrame()
	 * @see #getTimePositionType()
	 * @generated
	 */
	EAttribute getTimePositionType_Frame();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.gml.TimePositionType#getIndeterminatePosition <em>Indeterminate Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Indeterminate Position</em>'.
	 * @see net.opengis.gml.TimePositionType#getIndeterminatePosition()
	 * @see #getTimePositionType()
	 * @generated
	 */
	EAttribute getTimePositionType_IndeterminatePosition();

	/**
	 * Returns the meta object for class '{@link net.opengis.gml.VectorType <em>Vector Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vector Type</em>'.
	 * @see net.opengis.gml.VectorType
	 * @generated
	 */
	EClass getVectorType();

	/**
	 * Returns the meta object for enum '{@link net.opengis.gml.TimeIndeterminateValueType <em>Time Indeterminate Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Time Indeterminate Value Type</em>'.
	 * @see net.opengis.gml.TimeIndeterminateValueType
	 * @generated
	 */
	EEnum getTimeIndeterminateValueType();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Double List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Double List</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='doubleList' itemType='http://www.eclipse.org/emf/2003/XMLType#double'"
	 * @generated
	 */
	EDataType getDoubleList();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Integer List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Integer List</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='integerList' itemType='http://www.eclipse.org/emf/2003/XMLType#integer'"
	 * @generated
	 */
	EDataType getIntegerList();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Name List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Name List</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='NameList' itemType='http://www.eclipse.org/emf/2003/XMLType#Name'"
	 * @generated
	 */
	EDataType getNameList();

	/**
	 * Returns the meta object for data type '{@link java.lang.Object <em>Temporal Position Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Temporal Position Type</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object"
	 *        extendedMetaData="name='TemporalPositionType' memberTypes='http://www.eclipse.org/emf/2003/XMLType#dateTime http://www.eclipse.org/emf/2003/XMLType#date http://www.eclipse.org/emf/2003/XMLType#gYearMonth http://www.eclipse.org/emf/2003/XMLType#gYear http://www.eclipse.org/emf/2003/XMLType#anyURI http://www.eclipse.org/emf/2003/XMLType#decimal'"
	 * @generated
	 */
	EDataType getTemporalPositionType();

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
	 * Returns the meta object for data type '{@link net.opengis.gml.TimeIndeterminateValueType <em>Time Indeterminate Value Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Time Indeterminate Value Type Object</em>'.
	 * @see net.opengis.gml.TimeIndeterminateValueType
	 * @model instanceClass="net.opengis.gml.TimeIndeterminateValueType"
	 *        extendedMetaData="name='TimeIndeterminateValueType:Object' baseType='TimeIndeterminateValueType'"
	 * @generated
	 */
	EDataType getTimeIndeterminateValueTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Vector Type Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Vector Type Base</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='VectorType_._base' baseType='doubleList'"
	 * @generated
	 */
	EDataType getVectorTypeBase();

	/**
	 * Returns the meta object for data type '{@link org.opengis.coverage.grid.GridEnvelope <em>Grid Envelope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Grid Envelope</em>'.
	 * @see org.opengis.coverage.grid.GridEnvelope
	 * @model instanceClass="org.opengis.coverage.grid.GridEnvelope"
	 * @generated
	 */
	EDataType getGridEnvelope();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GmlFactory getGmlFactory();

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
		 * The meta object literal for the '{@link net.opengis.gml.impl.AbstractGeometricPrimitiveTypeImpl <em>Abstract Geometric Primitive Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.AbstractGeometricPrimitiveTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractGeometricPrimitiveType()
		 * @generated
		 */
		EClass ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE = eINSTANCE.getAbstractGeometricPrimitiveType();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.AbstractGeometryBaseTypeImpl <em>Abstract Geometry Base Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.AbstractGeometryBaseTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractGeometryBaseType()
		 * @generated
		 */
		EClass ABSTRACT_GEOMETRY_BASE_TYPE = eINSTANCE.getAbstractGeometryBaseType();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.AbstractGeometryTypeImpl <em>Abstract Geometry Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.AbstractGeometryTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractGeometryType()
		 * @generated
		 */
		EClass ABSTRACT_GEOMETRY_TYPE = eINSTANCE.getAbstractGeometryType();

		/**
		 * The meta object literal for the '<em><b>Srs Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_GEOMETRY_TYPE__SRS_NAME = eINSTANCE.getAbstractGeometryType_SrsName();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.AbstractGMLTypeImpl <em>Abstract GML Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.AbstractGMLTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractGMLType()
		 * @generated
		 */
		EClass ABSTRACT_GML_TYPE = eINSTANCE.getAbstractGMLType();

		/**
		 * The meta object literal for the '<em><b>Meta Data Property</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_GML_TYPE__META_DATA_PROPERTY = eINSTANCE.getAbstractGMLType_MetaDataProperty();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_GML_TYPE__DESCRIPTION = eINSTANCE.getAbstractGMLType_Description();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_GML_TYPE__NAME = eINSTANCE.getAbstractGMLType_Name();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.AbstractMetaDataTypeImpl <em>Abstract Meta Data Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.AbstractMetaDataTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractMetaDataType()
		 * @generated
		 */
		EClass ABSTRACT_META_DATA_TYPE = eINSTANCE.getAbstractMetaDataType();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_META_DATA_TYPE__MIXED = eINSTANCE.getAbstractMetaDataType_Mixed();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_META_DATA_TYPE__ID = eINSTANCE.getAbstractMetaDataType_Id();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.AbstractRingPropertyTypeImpl <em>Abstract Ring Property Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.AbstractRingPropertyTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractRingPropertyType()
		 * @generated
		 */
		EClass ABSTRACT_RING_PROPERTY_TYPE = eINSTANCE.getAbstractRingPropertyType();

		/**
		 * The meta object literal for the '<em><b>Linear Ring</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING = eINSTANCE.getAbstractRingPropertyType_LinearRing();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.AbstractRingTypeImpl <em>Abstract Ring Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.AbstractRingTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractRingType()
		 * @generated
		 */
		EClass ABSTRACT_RING_TYPE = eINSTANCE.getAbstractRingType();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.AbstractSurfaceTypeImpl <em>Abstract Surface Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.AbstractSurfaceTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getAbstractSurfaceType()
		 * @generated
		 */
		EClass ABSTRACT_SURFACE_TYPE = eINSTANCE.getAbstractSurfaceType();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.BoundingShapeTypeImpl <em>Bounding Shape Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.BoundingShapeTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getBoundingShapeType()
		 * @generated
		 */
		EClass BOUNDING_SHAPE_TYPE = eINSTANCE.getBoundingShapeType();

		/**
		 * The meta object literal for the '<em><b>Envelope Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP = eINSTANCE.getBoundingShapeType_EnvelopeGroup();

		/**
		 * The meta object literal for the '<em><b>Envelope</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BOUNDING_SHAPE_TYPE__ENVELOPE = eINSTANCE.getBoundingShapeType_Envelope();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.CodeListTypeImpl <em>Code List Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.CodeListTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getCodeListType()
		 * @generated
		 */
		EClass CODE_LIST_TYPE = eINSTANCE.getCodeListType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_LIST_TYPE__VALUE = eINSTANCE.getCodeListType_Value();

		/**
		 * The meta object literal for the '<em><b>Code Space</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_LIST_TYPE__CODE_SPACE = eINSTANCE.getCodeListType_CodeSpace();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.CodeTypeImpl <em>Code Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.CodeTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getCodeType()
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
		 * The meta object literal for the '{@link net.opengis.gml.impl.DirectPositionTypeImpl <em>Direct Position Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.DirectPositionTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getDirectPositionType()
		 * @generated
		 */
		EClass DIRECT_POSITION_TYPE = eINSTANCE.getDirectPositionType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT_POSITION_TYPE__VALUE = eINSTANCE.getDirectPositionType_Value();

		/**
		 * The meta object literal for the '<em><b>Dimension</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT_POSITION_TYPE__DIMENSION = eINSTANCE.getDirectPositionType_Dimension();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.DocumentRootImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getDocumentRoot()
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
		 * The meta object literal for the '<em><b>Geometric Primitive</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GEOMETRIC_PRIMITIVE = eINSTANCE.getDocumentRoot_GeometricPrimitive();

		/**
		 * The meta object literal for the '<em><b>Geometry</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GEOMETRY = eINSTANCE.getDocumentRoot_Geometry();

		/**
		 * The meta object literal for the '<em><b>GML</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GML = eINSTANCE.getDocumentRoot_GML();

		/**
		 * The meta object literal for the '<em><b>Object</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__OBJECT = eINSTANCE.getDocumentRoot_Object();

		/**
		 * The meta object literal for the '<em><b>Meta Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__META_DATA = eINSTANCE.getDocumentRoot_MetaData();

		/**
		 * The meta object literal for the '<em><b>Ring</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__RING = eINSTANCE.getDocumentRoot_Ring();

		/**
		 * The meta object literal for the '<em><b>Surface</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__SURFACE = eINSTANCE.getDocumentRoot_Surface();

		/**
		 * The meta object literal for the '<em><b>Bounded By</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__BOUNDED_BY = eINSTANCE.getDocumentRoot_BoundedBy();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DESCRIPTION = eINSTANCE.getDocumentRoot_Description();

		/**
		 * The meta object literal for the '<em><b>Envelope</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__ENVELOPE = eINSTANCE.getDocumentRoot_Envelope();

		/**
		 * The meta object literal for the '<em><b>Envelope With Time Period</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD = eINSTANCE.getDocumentRoot_EnvelopeWithTimePeriod();

		/**
		 * The meta object literal for the '<em><b>Exterior</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__EXTERIOR = eINSTANCE.getDocumentRoot_Exterior();

		/**
		 * The meta object literal for the '<em><b>Grid</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GRID = eINSTANCE.getDocumentRoot_Grid();

		/**
		 * The meta object literal for the '<em><b>Interior</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__INTERIOR = eINSTANCE.getDocumentRoot_Interior();

		/**
		 * The meta object literal for the '<em><b>Linear Ring</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__LINEAR_RING = eINSTANCE.getDocumentRoot_LinearRing();

		/**
		 * The meta object literal for the '<em><b>Meta Data Property</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__META_DATA_PROPERTY = eINSTANCE.getDocumentRoot_MetaDataProperty();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__NAME = eINSTANCE.getDocumentRoot_Name();

		/**
		 * The meta object literal for the '<em><b>Polygon</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__POLYGON = eINSTANCE.getDocumentRoot_Polygon();

		/**
		 * The meta object literal for the '<em><b>Pos</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__POS = eINSTANCE.getDocumentRoot_Pos();

		/**
		 * The meta object literal for the '<em><b>Rectified Grid</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__RECTIFIED_GRID = eINSTANCE.getDocumentRoot_RectifiedGrid();

		/**
		 * The meta object literal for the '<em><b>Time Position</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__TIME_POSITION = eINSTANCE.getDocumentRoot_TimePosition();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__ID = eINSTANCE.getDocumentRoot_Id();

		/**
		 * The meta object literal for the '<em><b>Remote Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__REMOTE_SCHEMA = eINSTANCE.getDocumentRoot_RemoteSchema();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.EnvelopeTypeImpl <em>Envelope Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.EnvelopeTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getEnvelopeType()
		 * @generated
		 */
		EClass ENVELOPE_TYPE = eINSTANCE.getEnvelopeType();

		/**
		 * The meta object literal for the '<em><b>Pos</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENVELOPE_TYPE__POS = eINSTANCE.getEnvelopeType_Pos();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.EnvelopeWithTimePeriodTypeImpl <em>Envelope With Time Period Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.EnvelopeWithTimePeriodTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getEnvelopeWithTimePeriodType()
		 * @generated
		 */
		EClass ENVELOPE_WITH_TIME_PERIOD_TYPE = eINSTANCE.getEnvelopeWithTimePeriodType();

		/**
		 * The meta object literal for the '<em><b>Time Position</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION = eINSTANCE.getEnvelopeWithTimePeriodType_TimePosition();

		/**
		 * The meta object literal for the '<em><b>Frame</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME = eINSTANCE.getEnvelopeWithTimePeriodType_Frame();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.GridEnvelopeTypeImpl <em>Grid Envelope Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.GridEnvelopeTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getGridEnvelopeType()
		 * @generated
		 */
		EClass GRID_ENVELOPE_TYPE = eINSTANCE.getGridEnvelopeType();

		/**
		 * The meta object literal for the '<em><b>Low</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_ENVELOPE_TYPE__LOW = eINSTANCE.getGridEnvelopeType_Low();

		/**
		 * The meta object literal for the '<em><b>High</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_ENVELOPE_TYPE__HIGH = eINSTANCE.getGridEnvelopeType_High();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.GridLimitsTypeImpl <em>Grid Limits Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.GridLimitsTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getGridLimitsType()
		 * @generated
		 */
		EClass GRID_LIMITS_TYPE = eINSTANCE.getGridLimitsType();

		/**
		 * The meta object literal for the '<em><b>Grid Envelope</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GRID_LIMITS_TYPE__GRID_ENVELOPE = eINSTANCE.getGridLimitsType_GridEnvelope();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.GridTypeImpl <em>Grid Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.GridTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getGridType()
		 * @generated
		 */
		EClass GRID_TYPE = eINSTANCE.getGridType();

		/**
		 * The meta object literal for the '<em><b>Limits</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_TYPE__LIMITS = eINSTANCE.getGridType_Limits();

		/**
		 * The meta object literal for the '<em><b>Axis Name</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_TYPE__AXIS_NAME = eINSTANCE.getGridType_AxisName();

		/**
		 * The meta object literal for the '<em><b>Dimension</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRID_TYPE__DIMENSION = eINSTANCE.getGridType_Dimension();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.LinearRingTypeImpl <em>Linear Ring Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.LinearRingTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getLinearRingType()
		 * @generated
		 */
		EClass LINEAR_RING_TYPE = eINSTANCE.getLinearRingType();

		/**
		 * The meta object literal for the '<em><b>Pos</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LINEAR_RING_TYPE__POS = eINSTANCE.getLinearRingType_Pos();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.MetaDataPropertyTypeImpl <em>Meta Data Property Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.MetaDataPropertyTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getMetaDataPropertyType()
		 * @generated
		 */
		EClass META_DATA_PROPERTY_TYPE = eINSTANCE.getMetaDataPropertyType();

		/**
		 * The meta object literal for the '<em><b>Meta Data Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__META_DATA_GROUP = eINSTANCE.getMetaDataPropertyType_MetaDataGroup();

		/**
		 * The meta object literal for the '<em><b>Meta Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference META_DATA_PROPERTY_TYPE__META_DATA = eINSTANCE.getMetaDataPropertyType_MetaData();

		/**
		 * The meta object literal for the '<em><b>About</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__ABOUT = eINSTANCE.getMetaDataPropertyType_About();

		/**
		 * The meta object literal for the '<em><b>Actuate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__ACTUATE = eINSTANCE.getMetaDataPropertyType_Actuate();

		/**
		 * The meta object literal for the '<em><b>Arcrole</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__ARCROLE = eINSTANCE.getMetaDataPropertyType_Arcrole();

		/**
		 * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__HREF = eINSTANCE.getMetaDataPropertyType_Href();

		/**
		 * The meta object literal for the '<em><b>Remote Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__REMOTE_SCHEMA = eINSTANCE.getMetaDataPropertyType_RemoteSchema();

		/**
		 * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__ROLE = eINSTANCE.getMetaDataPropertyType_Role();

		/**
		 * The meta object literal for the '<em><b>Show</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__SHOW = eINSTANCE.getMetaDataPropertyType_Show();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__TITLE = eINSTANCE.getMetaDataPropertyType_Title();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_DATA_PROPERTY_TYPE__TYPE = eINSTANCE.getMetaDataPropertyType_Type();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.PointTypeImpl <em>Point Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.PointTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getPointType()
		 * @generated
		 */
		EClass POINT_TYPE = eINSTANCE.getPointType();

		/**
		 * The meta object literal for the '<em><b>Pos</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference POINT_TYPE__POS = eINSTANCE.getPointType_Pos();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.PolygonTypeImpl <em>Polygon Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.PolygonTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getPolygonType()
		 * @generated
		 */
		EClass POLYGON_TYPE = eINSTANCE.getPolygonType();

		/**
		 * The meta object literal for the '<em><b>Exterior</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference POLYGON_TYPE__EXTERIOR = eINSTANCE.getPolygonType_Exterior();

		/**
		 * The meta object literal for the '<em><b>Interior</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference POLYGON_TYPE__INTERIOR = eINSTANCE.getPolygonType_Interior();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.RectifiedGridTypeImpl <em>Rectified Grid Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.RectifiedGridTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getRectifiedGridType()
		 * @generated
		 */
		EClass RECTIFIED_GRID_TYPE = eINSTANCE.getRectifiedGridType();

		/**
		 * The meta object literal for the '<em><b>Origin</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RECTIFIED_GRID_TYPE__ORIGIN = eINSTANCE.getRectifiedGridType_Origin();

		/**
		 * The meta object literal for the '<em><b>Offset Vector</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RECTIFIED_GRID_TYPE__OFFSET_VECTOR = eINSTANCE.getRectifiedGridType_OffsetVector();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.ReferenceTypeImpl <em>Reference Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.ReferenceTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getReferenceType()
		 * @generated
		 */
		EClass REFERENCE_TYPE = eINSTANCE.getReferenceType();

		/**
		 * The meta object literal for the '<em><b>Actuate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__ACTUATE = eINSTANCE.getReferenceType_Actuate();

		/**
		 * The meta object literal for the '<em><b>Arcrole</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__ARCROLE = eINSTANCE.getReferenceType_Arcrole();

		/**
		 * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__HREF = eINSTANCE.getReferenceType_Href();

		/**
		 * The meta object literal for the '<em><b>Remote Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__REMOTE_SCHEMA = eINSTANCE.getReferenceType_RemoteSchema();

		/**
		 * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__ROLE = eINSTANCE.getReferenceType_Role();

		/**
		 * The meta object literal for the '<em><b>Show</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__SHOW = eINSTANCE.getReferenceType_Show();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__TITLE = eINSTANCE.getReferenceType_Title();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE_TYPE__TYPE = eINSTANCE.getReferenceType_Type();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.StringOrRefTypeImpl <em>String Or Ref Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.StringOrRefTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getStringOrRefType()
		 * @generated
		 */
		EClass STRING_OR_REF_TYPE = eINSTANCE.getStringOrRefType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__VALUE = eINSTANCE.getStringOrRefType_Value();

		/**
		 * The meta object literal for the '<em><b>Actuate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__ACTUATE = eINSTANCE.getStringOrRefType_Actuate();

		/**
		 * The meta object literal for the '<em><b>Arcrole</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__ARCROLE = eINSTANCE.getStringOrRefType_Arcrole();

		/**
		 * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__HREF = eINSTANCE.getStringOrRefType_Href();

		/**
		 * The meta object literal for the '<em><b>Remote Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__REMOTE_SCHEMA = eINSTANCE.getStringOrRefType_RemoteSchema();

		/**
		 * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__ROLE = eINSTANCE.getStringOrRefType_Role();

		/**
		 * The meta object literal for the '<em><b>Show</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__SHOW = eINSTANCE.getStringOrRefType_Show();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__TITLE = eINSTANCE.getStringOrRefType_Title();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_OR_REF_TYPE__TYPE = eINSTANCE.getStringOrRefType_Type();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.TimePositionTypeImpl <em>Time Position Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.TimePositionTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getTimePositionType()
		 * @generated
		 */
		EClass TIME_POSITION_TYPE = eINSTANCE.getTimePositionType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_POSITION_TYPE__VALUE = eINSTANCE.getTimePositionType_Value();

		/**
		 * The meta object literal for the '<em><b>Calendar Era Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_POSITION_TYPE__CALENDAR_ERA_NAME = eINSTANCE.getTimePositionType_CalendarEraName();

		/**
		 * The meta object literal for the '<em><b>Frame</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_POSITION_TYPE__FRAME = eINSTANCE.getTimePositionType_Frame();

		/**
		 * The meta object literal for the '<em><b>Indeterminate Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_POSITION_TYPE__INDETERMINATE_POSITION = eINSTANCE.getTimePositionType_IndeterminatePosition();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.impl.VectorTypeImpl <em>Vector Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.impl.VectorTypeImpl
		 * @see net.opengis.gml.impl.GmlPackageImpl#getVectorType()
		 * @generated
		 */
		EClass VECTOR_TYPE = eINSTANCE.getVectorType();

		/**
		 * The meta object literal for the '{@link net.opengis.gml.TimeIndeterminateValueType <em>Time Indeterminate Value Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.TimeIndeterminateValueType
		 * @see net.opengis.gml.impl.GmlPackageImpl#getTimeIndeterminateValueType()
		 * @generated
		 */
		EEnum TIME_INDETERMINATE_VALUE_TYPE = eINSTANCE.getTimeIndeterminateValueType();

		/**
		 * The meta object literal for the '<em>Double List</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see net.opengis.gml.impl.GmlPackageImpl#getDoubleList()
		 * @generated
		 */
		EDataType DOUBLE_LIST = eINSTANCE.getDoubleList();

		/**
		 * The meta object literal for the '<em>Integer List</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see net.opengis.gml.impl.GmlPackageImpl#getIntegerList()
		 * @generated
		 */
		EDataType INTEGER_LIST = eINSTANCE.getIntegerList();

		/**
		 * The meta object literal for the '<em>Name List</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see net.opengis.gml.impl.GmlPackageImpl#getNameList()
		 * @generated
		 */
		EDataType NAME_LIST = eINSTANCE.getNameList();

		/**
		 * The meta object literal for the '<em>Temporal Position Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see net.opengis.gml.impl.GmlPackageImpl#getTemporalPositionType()
		 * @generated
		 */
		EDataType TEMPORAL_POSITION_TYPE = eINSTANCE.getTemporalPositionType();

		/**
		 * The meta object literal for the '<em>Time Duration Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see net.opengis.gml.impl.GmlPackageImpl#getTimeDurationType()
		 * @generated
		 */
		EDataType TIME_DURATION_TYPE = eINSTANCE.getTimeDurationType();

		/**
		 * The meta object literal for the '<em>Time Indeterminate Value Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.opengis.gml.TimeIndeterminateValueType
		 * @see net.opengis.gml.impl.GmlPackageImpl#getTimeIndeterminateValueTypeObject()
		 * @generated
		 */
		EDataType TIME_INDETERMINATE_VALUE_TYPE_OBJECT = eINSTANCE.getTimeIndeterminateValueTypeObject();

		/**
		 * The meta object literal for the '<em>Vector Type Base</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see net.opengis.gml.impl.GmlPackageImpl#getVectorTypeBase()
		 * @generated
		 */
		EDataType VECTOR_TYPE_BASE = eINSTANCE.getVectorTypeBase();

		/**
		 * The meta object literal for the '<em>Grid Envelope</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.opengis.coverage.grid.GridEnvelope
		 * @see net.opengis.gml.impl.GmlPackageImpl#getGridEnvelope()
		 * @generated
		 */
		EDataType GRID_ENVELOPE = eINSTANCE.getGridEnvelope();

	}

} //GmlPackage
