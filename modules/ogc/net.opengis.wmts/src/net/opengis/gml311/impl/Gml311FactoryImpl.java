/**
 */
package net.opengis.gml311.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.gml311.*;

import org.eclipse.emf.common.util.Enumerator;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.util.Diagnostician;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Gml311FactoryImpl extends EFactoryImpl implements Gml311Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Gml311Factory init() {
        try {
            Gml311Factory theGml311Factory = (Gml311Factory)EPackage.Registry.INSTANCE.getEFactory(Gml311Package.eNS_URI);
            if (theGml311Factory != null) {
                return theGml311Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Gml311FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Gml311FactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case Gml311Package.ABSOLUTE_EXTERNAL_POSITIONAL_ACCURACY_TYPE: return createAbsoluteExternalPositionalAccuracyType();
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_REF_TYPE: return createAbstractGeneralOperationParameterRefType();
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE: return createAbstractGriddedSurfaceType();
            case Gml311Package.ABSTRACT_PARAMETRIC_CURVE_SURFACE_TYPE: return createAbstractParametricCurveSurfaceType();
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE: return createAbstractRingPropertyType();
            case Gml311Package.ABSTRACT_SOLID_TYPE: return createAbstractSolidType();
            case Gml311Package.ABSTRACT_SURFACE_TYPE: return createAbstractSurfaceType();
            case Gml311Package.AFFINE_PLACEMENT_TYPE: return createAffinePlacementType();
            case Gml311Package.ANGLE_CHOICE_TYPE: return createAngleChoiceType();
            case Gml311Package.ANGLE_TYPE: return createAngleType();
            case Gml311Package.ARC_BY_BULGE_TYPE: return createArcByBulgeType();
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE: return createArcByCenterPointType();
            case Gml311Package.ARC_STRING_BY_BULGE_TYPE: return createArcStringByBulgeType();
            case Gml311Package.ARC_STRING_TYPE: return createArcStringType();
            case Gml311Package.ARC_TYPE: return createArcType();
            case Gml311Package.AREA_TYPE: return createAreaType();
            case Gml311Package.ARRAY_ASSOCIATION_TYPE: return createArrayAssociationType();
            case Gml311Package.ARRAY_TYPE: return createArrayType();
            case Gml311Package.ASSOCIATION_TYPE: return createAssociationType();
            case Gml311Package.BAG_TYPE: return createBagType();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE: return createBaseStyleDescriptorType();
            case Gml311Package.BASE_UNIT_TYPE: return createBaseUnitType();
            case Gml311Package.BEZIER_TYPE: return createBezierType();
            case Gml311Package.BOOLEAN_PROPERTY_TYPE: return createBooleanPropertyType();
            case Gml311Package.BOUNDING_SHAPE_TYPE: return createBoundingShapeType();
            case Gml311Package.BSPLINE_TYPE: return createBSplineType();
            case Gml311Package.CARTESIAN_CS_REF_TYPE: return createCartesianCSRefType();
            case Gml311Package.CARTESIAN_CS_TYPE: return createCartesianCSType();
            case Gml311Package.CATEGORY_EXTENT_TYPE: return createCategoryExtentType();
            case Gml311Package.CATEGORY_PROPERTY_TYPE: return createCategoryPropertyType();
            case Gml311Package.CIRCLE_BY_CENTER_POINT_TYPE: return createCircleByCenterPointType();
            case Gml311Package.CIRCLE_TYPE: return createCircleType();
            case Gml311Package.CLOTHOID_TYPE: return createClothoidType();
            case Gml311Package.CODE_LIST_TYPE: return createCodeListType();
            case Gml311Package.CODE_OR_NULL_LIST_TYPE: return createCodeOrNullListType();
            case Gml311Package.CODE_TYPE: return createCodeType();
            case Gml311Package.COMPOSITE_CURVE_PROPERTY_TYPE: return createCompositeCurvePropertyType();
            case Gml311Package.COMPOSITE_CURVE_TYPE: return createCompositeCurveType();
            case Gml311Package.COMPOSITE_SOLID_PROPERTY_TYPE: return createCompositeSolidPropertyType();
            case Gml311Package.COMPOSITE_SOLID_TYPE: return createCompositeSolidType();
            case Gml311Package.COMPOSITE_SURFACE_PROPERTY_TYPE: return createCompositeSurfacePropertyType();
            case Gml311Package.COMPOSITE_SURFACE_TYPE: return createCompositeSurfaceType();
            case Gml311Package.COMPOSITE_VALUE_TYPE: return createCompositeValueType();
            case Gml311Package.COMPOUND_CRS_REF_TYPE: return createCompoundCRSRefType();
            case Gml311Package.COMPOUND_CRS_TYPE: return createCompoundCRSType();
            case Gml311Package.CONCATENATED_OPERATION_REF_TYPE: return createConcatenatedOperationRefType();
            case Gml311Package.CONCATENATED_OPERATION_TYPE: return createConcatenatedOperationType();
            case Gml311Package.CONE_TYPE: return createConeType();
            case Gml311Package.CONTAINER_PROPERTY_TYPE: return createContainerPropertyType();
            case Gml311Package.CONTROL_POINT_TYPE: return createControlPointType();
            case Gml311Package.CONVENTIONAL_UNIT_TYPE: return createConventionalUnitType();
            case Gml311Package.CONVERSION_REF_TYPE: return createConversionRefType();
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE: return createConversionToPreferredUnitType();
            case Gml311Package.CONVERSION_TYPE: return createConversionType();
            case Gml311Package.COORDINATE_OPERATION_REF_TYPE: return createCoordinateOperationRefType();
            case Gml311Package.COORDINATE_REFERENCE_SYSTEM_REF_TYPE: return createCoordinateReferenceSystemRefType();
            case Gml311Package.COORDINATES_TYPE: return createCoordinatesType();
            case Gml311Package.COORDINATE_SYSTEM_AXIS_REF_TYPE: return createCoordinateSystemAxisRefType();
            case Gml311Package.COORDINATE_SYSTEM_AXIS_TYPE: return createCoordinateSystemAxisType();
            case Gml311Package.COORDINATE_SYSTEM_REF_TYPE: return createCoordinateSystemRefType();
            case Gml311Package.COORD_TYPE: return createCoordType();
            case Gml311Package.COUNT_PROPERTY_TYPE: return createCountPropertyType();
            case Gml311Package.COVARIANCE_ELEMENT_TYPE: return createCovarianceElementType();
            case Gml311Package.COVARIANCE_MATRIX_TYPE: return createCovarianceMatrixType();
            case Gml311Package.COVERAGE_FUNCTION_TYPE: return createCoverageFunctionType();
            case Gml311Package.CRS_REF_TYPE: return createCRSRefType();
            case Gml311Package.CUBIC_SPLINE_TYPE: return createCubicSplineType();
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE: return createCurveArrayPropertyType();
            case Gml311Package.CURVE_PROPERTY_TYPE: return createCurvePropertyType();
            case Gml311Package.CURVE_SEGMENT_ARRAY_PROPERTY_TYPE: return createCurveSegmentArrayPropertyType();
            case Gml311Package.CURVE_TYPE: return createCurveType();
            case Gml311Package.CYLINDER_TYPE: return createCylinderType();
            case Gml311Package.CYLINDRICAL_CS_REF_TYPE: return createCylindricalCSRefType();
            case Gml311Package.CYLINDRICAL_CS_TYPE: return createCylindricalCSType();
            case Gml311Package.DATA_BLOCK_TYPE: return createDataBlockType();
            case Gml311Package.DATUM_REF_TYPE: return createDatumRefType();
            case Gml311Package.DEFAULT_STYLE_PROPERTY_TYPE: return createDefaultStylePropertyType();
            case Gml311Package.DEFINITION_PROXY_TYPE: return createDefinitionProxyType();
            case Gml311Package.DEFINITION_TYPE: return createDefinitionType();
            case Gml311Package.DEGREES_TYPE: return createDegreesType();
            case Gml311Package.DERIVATION_UNIT_TERM_TYPE: return createDerivationUnitTermType();
            case Gml311Package.DERIVED_CRS_REF_TYPE: return createDerivedCRSRefType();
            case Gml311Package.DERIVED_CRS_TYPE: return createDerivedCRSType();
            case Gml311Package.DERIVED_CRS_TYPE_TYPE: return createDerivedCRSTypeType();
            case Gml311Package.DERIVED_UNIT_TYPE: return createDerivedUnitType();
            case Gml311Package.DICTIONARY_ENTRY_TYPE: return createDictionaryEntryType();
            case Gml311Package.DICTIONARY_TYPE: return createDictionaryType();
            case Gml311Package.DIRECTED_EDGE_PROPERTY_TYPE: return createDirectedEdgePropertyType();
            case Gml311Package.DIRECTED_FACE_PROPERTY_TYPE: return createDirectedFacePropertyType();
            case Gml311Package.DIRECTED_NODE_PROPERTY_TYPE: return createDirectedNodePropertyType();
            case Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE: return createDirectedObservationAtDistanceType();
            case Gml311Package.DIRECTED_OBSERVATION_TYPE: return createDirectedObservationType();
            case Gml311Package.DIRECTED_TOPO_SOLID_PROPERTY_TYPE: return createDirectedTopoSolidPropertyType();
            case Gml311Package.DIRECTION_PROPERTY_TYPE: return createDirectionPropertyType();
            case Gml311Package.DIRECTION_VECTOR_TYPE: return createDirectionVectorType();
            case Gml311Package.DIRECT_POSITION_LIST_TYPE: return createDirectPositionListType();
            case Gml311Package.DIRECT_POSITION_TYPE: return createDirectPositionType();
            case Gml311Package.DMS_ANGLE_TYPE: return createDMSAngleType();
            case Gml311Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Gml311Package.DOMAIN_SET_TYPE: return createDomainSetType();
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE: return createDynamicFeatureCollectionType();
            case Gml311Package.DYNAMIC_FEATURE_TYPE: return createDynamicFeatureType();
            case Gml311Package.EDGE_TYPE: return createEdgeType();
            case Gml311Package.ELLIPSOIDAL_CS_REF_TYPE: return createEllipsoidalCSRefType();
            case Gml311Package.ELLIPSOIDAL_CS_TYPE: return createEllipsoidalCSType();
            case Gml311Package.ELLIPSOID_REF_TYPE: return createEllipsoidRefType();
            case Gml311Package.ELLIPSOID_TYPE: return createEllipsoidType();
            case Gml311Package.ENGINEERING_CRS_REF_TYPE: return createEngineeringCRSRefType();
            case Gml311Package.ENGINEERING_CRS_TYPE: return createEngineeringCRSType();
            case Gml311Package.ENGINEERING_DATUM_REF_TYPE: return createEngineeringDatumRefType();
            case Gml311Package.ENGINEERING_DATUM_TYPE: return createEngineeringDatumType();
            case Gml311Package.ENVELOPE_TYPE: return createEnvelopeType();
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE: return createEnvelopeWithTimePeriodType();
            case Gml311Package.EXTENT_TYPE: return createExtentType();
            case Gml311Package.FACE_TYPE: return createFaceType();
            case Gml311Package.FEATURE_ARRAY_PROPERTY_TYPE: return createFeatureArrayPropertyType();
            case Gml311Package.FEATURE_COLLECTION_TYPE: return createFeatureCollectionType();
            case Gml311Package.FEATURE_PROPERTY_TYPE: return createFeaturePropertyType();
            case Gml311Package.FEATURE_STYLE_PROPERTY_TYPE: return createFeatureStylePropertyType();
            case Gml311Package.FEATURE_STYLE_TYPE: return createFeatureStyleType();
            case Gml311Package.FILE_TYPE: return createFileType();
            case Gml311Package.FORMULA_TYPE: return createFormulaType();
            case Gml311Package.GENERAL_CONVERSION_REF_TYPE: return createGeneralConversionRefType();
            case Gml311Package.GENERAL_TRANSFORMATION_REF_TYPE: return createGeneralTransformationRefType();
            case Gml311Package.GENERIC_META_DATA_TYPE: return createGenericMetaDataType();
            case Gml311Package.GEOCENTRIC_CRS_REF_TYPE: return createGeocentricCRSRefType();
            case Gml311Package.GEOCENTRIC_CRS_TYPE: return createGeocentricCRSType();
            case Gml311Package.GEODESIC_STRING_TYPE: return createGeodesicStringType();
            case Gml311Package.GEODESIC_TYPE: return createGeodesicType();
            case Gml311Package.GEODETIC_DATUM_REF_TYPE: return createGeodeticDatumRefType();
            case Gml311Package.GEODETIC_DATUM_TYPE: return createGeodeticDatumType();
            case Gml311Package.GEOGRAPHIC_CRS_REF_TYPE: return createGeographicCRSRefType();
            case Gml311Package.GEOGRAPHIC_CRS_TYPE: return createGeographicCRSType();
            case Gml311Package.GEOMETRIC_COMPLEX_PROPERTY_TYPE: return createGeometricComplexPropertyType();
            case Gml311Package.GEOMETRIC_COMPLEX_TYPE: return createGeometricComplexType();
            case Gml311Package.GEOMETRIC_PRIMITIVE_PROPERTY_TYPE: return createGeometricPrimitivePropertyType();
            case Gml311Package.GEOMETRY_ARRAY_PROPERTY_TYPE: return createGeometryArrayPropertyType();
            case Gml311Package.GEOMETRY_PROPERTY_TYPE: return createGeometryPropertyType();
            case Gml311Package.GEOMETRY_STYLE_PROPERTY_TYPE: return createGeometryStylePropertyType();
            case Gml311Package.GEOMETRY_STYLE_TYPE: return createGeometryStyleType();
            case Gml311Package.GRAPH_STYLE_PROPERTY_TYPE: return createGraphStylePropertyType();
            case Gml311Package.GRAPH_STYLE_TYPE: return createGraphStyleType();
            case Gml311Package.GRID_COVERAGE_TYPE: return createGridCoverageType();
            case Gml311Package.GRID_DOMAIN_TYPE: return createGridDomainType();
            case Gml311Package.GRID_ENVELOPE_TYPE: return createGridEnvelopeType();
            case Gml311Package.GRID_FUNCTION_TYPE: return createGridFunctionType();
            case Gml311Package.GRID_LENGTH_TYPE: return createGridLengthType();
            case Gml311Package.GRID_LIMITS_TYPE: return createGridLimitsType();
            case Gml311Package.GRID_TYPE: return createGridType();
            case Gml311Package.HISTORY_PROPERTY_TYPE: return createHistoryPropertyType();
            case Gml311Package.IDENTIFIER_TYPE: return createIdentifierType();
            case Gml311Package.IMAGE_CRS_REF_TYPE: return createImageCRSRefType();
            case Gml311Package.IMAGE_CRS_TYPE: return createImageCRSType();
            case Gml311Package.IMAGE_DATUM_REF_TYPE: return createImageDatumRefType();
            case Gml311Package.IMAGE_DATUM_TYPE: return createImageDatumType();
            case Gml311Package.INDEX_MAP_TYPE: return createIndexMapType();
            case Gml311Package.INDIRECT_ENTRY_TYPE: return createIndirectEntryType();
            case Gml311Package.ISOLATED_PROPERTY_TYPE: return createIsolatedPropertyType();
            case Gml311Package.KNOT_PROPERTY_TYPE: return createKnotPropertyType();
            case Gml311Package.KNOT_TYPE: return createKnotType();
            case Gml311Package.LABEL_STYLE_PROPERTY_TYPE: return createLabelStylePropertyType();
            case Gml311Package.LABEL_STYLE_TYPE: return createLabelStyleType();
            case Gml311Package.LABEL_TYPE: return createLabelType();
            case Gml311Package.LENGTH_TYPE: return createLengthType();
            case Gml311Package.LINEAR_CS_REF_TYPE: return createLinearCSRefType();
            case Gml311Package.LINEAR_CS_TYPE: return createLinearCSType();
            case Gml311Package.LINEAR_RING_PROPERTY_TYPE: return createLinearRingPropertyType();
            case Gml311Package.LINEAR_RING_TYPE: return createLinearRingType();
            case Gml311Package.LINE_STRING_PROPERTY_TYPE: return createLineStringPropertyType();
            case Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE: return createLineStringSegmentArrayPropertyType();
            case Gml311Package.LINE_STRING_SEGMENT_TYPE: return createLineStringSegmentType();
            case Gml311Package.LINE_STRING_TYPE: return createLineStringType();
            case Gml311Package.LOCATION_PROPERTY_TYPE: return createLocationPropertyType();
            case Gml311Package.MEASURE_LIST_TYPE: return createMeasureListType();
            case Gml311Package.MEASURE_OR_NULL_LIST_TYPE: return createMeasureOrNullListType();
            case Gml311Package.MEASURE_TYPE: return createMeasureType();
            case Gml311Package.META_DATA_PROPERTY_TYPE: return createMetaDataPropertyType();
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE: return createMovingObjectStatusType();
            case Gml311Package.MULTI_CURVE_COVERAGE_TYPE: return createMultiCurveCoverageType();
            case Gml311Package.MULTI_CURVE_DOMAIN_TYPE: return createMultiCurveDomainType();
            case Gml311Package.MULTI_CURVE_PROPERTY_TYPE: return createMultiCurvePropertyType();
            case Gml311Package.MULTI_CURVE_TYPE: return createMultiCurveType();
            case Gml311Package.MULTI_GEOMETRY_PROPERTY_TYPE: return createMultiGeometryPropertyType();
            case Gml311Package.MULTI_GEOMETRY_TYPE: return createMultiGeometryType();
            case Gml311Package.MULTI_LINE_STRING_PROPERTY_TYPE: return createMultiLineStringPropertyType();
            case Gml311Package.MULTI_LINE_STRING_TYPE: return createMultiLineStringType();
            case Gml311Package.MULTI_POINT_COVERAGE_TYPE: return createMultiPointCoverageType();
            case Gml311Package.MULTI_POINT_DOMAIN_TYPE: return createMultiPointDomainType();
            case Gml311Package.MULTI_POINT_PROPERTY_TYPE: return createMultiPointPropertyType();
            case Gml311Package.MULTI_POINT_TYPE: return createMultiPointType();
            case Gml311Package.MULTI_POLYGON_PROPERTY_TYPE: return createMultiPolygonPropertyType();
            case Gml311Package.MULTI_POLYGON_TYPE: return createMultiPolygonType();
            case Gml311Package.MULTI_SOLID_COVERAGE_TYPE: return createMultiSolidCoverageType();
            case Gml311Package.MULTI_SOLID_DOMAIN_TYPE: return createMultiSolidDomainType();
            case Gml311Package.MULTI_SOLID_PROPERTY_TYPE: return createMultiSolidPropertyType();
            case Gml311Package.MULTI_SOLID_TYPE: return createMultiSolidType();
            case Gml311Package.MULTI_SURFACE_COVERAGE_TYPE: return createMultiSurfaceCoverageType();
            case Gml311Package.MULTI_SURFACE_DOMAIN_TYPE: return createMultiSurfaceDomainType();
            case Gml311Package.MULTI_SURFACE_PROPERTY_TYPE: return createMultiSurfacePropertyType();
            case Gml311Package.MULTI_SURFACE_TYPE: return createMultiSurfaceType();
            case Gml311Package.NODE_TYPE: return createNodeType();
            case Gml311Package.OBLIQUE_CARTESIAN_CS_REF_TYPE: return createObliqueCartesianCSRefType();
            case Gml311Package.OBLIQUE_CARTESIAN_CS_TYPE: return createObliqueCartesianCSType();
            case Gml311Package.OBSERVATION_TYPE: return createObservationType();
            case Gml311Package.OFFSET_CURVE_TYPE: return createOffsetCurveType();
            case Gml311Package.OPERATION_METHOD_REF_TYPE: return createOperationMethodRefType();
            case Gml311Package.OPERATION_METHOD_TYPE: return createOperationMethodType();
            case Gml311Package.OPERATION_PARAMETER_GROUP_REF_TYPE: return createOperationParameterGroupRefType();
            case Gml311Package.OPERATION_PARAMETER_GROUP_TYPE: return createOperationParameterGroupType();
            case Gml311Package.OPERATION_PARAMETER_REF_TYPE: return createOperationParameterRefType();
            case Gml311Package.OPERATION_PARAMETER_TYPE: return createOperationParameterType();
            case Gml311Package.OPERATION_REF_TYPE: return createOperationRefType();
            case Gml311Package.ORIENTABLE_CURVE_TYPE: return createOrientableCurveType();
            case Gml311Package.ORIENTABLE_SURFACE_TYPE: return createOrientableSurfaceType();
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE: return createParameterValueGroupType();
            case Gml311Package.PARAMETER_VALUE_TYPE: return createParameterValueType();
            case Gml311Package.PASS_THROUGH_OPERATION_REF_TYPE: return createPassThroughOperationRefType();
            case Gml311Package.PASS_THROUGH_OPERATION_TYPE: return createPassThroughOperationType();
            case Gml311Package.PIXEL_IN_CELL_TYPE: return createPixelInCellType();
            case Gml311Package.POINT_ARRAY_PROPERTY_TYPE: return createPointArrayPropertyType();
            case Gml311Package.POINT_PROPERTY_TYPE: return createPointPropertyType();
            case Gml311Package.POINT_TYPE: return createPointType();
            case Gml311Package.POLAR_CS_REF_TYPE: return createPolarCSRefType();
            case Gml311Package.POLAR_CS_TYPE: return createPolarCSType();
            case Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE: return createPolygonPatchArrayPropertyType();
            case Gml311Package.POLYGON_PATCH_TYPE: return createPolygonPatchType();
            case Gml311Package.POLYGON_PROPERTY_TYPE: return createPolygonPropertyType();
            case Gml311Package.POLYGON_TYPE: return createPolygonType();
            case Gml311Package.POLYHEDRAL_SURFACE_TYPE: return createPolyhedralSurfaceType();
            case Gml311Package.PRIME_MERIDIAN_REF_TYPE: return createPrimeMeridianRefType();
            case Gml311Package.PRIME_MERIDIAN_TYPE: return createPrimeMeridianType();
            case Gml311Package.PRIORITY_LOCATION_PROPERTY_TYPE: return createPriorityLocationPropertyType();
            case Gml311Package.PROJECTED_CRS_REF_TYPE: return createProjectedCRSRefType();
            case Gml311Package.PROJECTED_CRS_TYPE: return createProjectedCRSType();
            case Gml311Package.QUANTITY_EXTENT_TYPE: return createQuantityExtentType();
            case Gml311Package.QUANTITY_PROPERTY_TYPE: return createQuantityPropertyType();
            case Gml311Package.RANGE_PARAMETERS_TYPE: return createRangeParametersType();
            case Gml311Package.RANGE_SET_TYPE: return createRangeSetType();
            case Gml311Package.RECTANGLE_TYPE: return createRectangleType();
            case Gml311Package.RECTIFIED_GRID_COVERAGE_TYPE: return createRectifiedGridCoverageType();
            case Gml311Package.RECTIFIED_GRID_DOMAIN_TYPE: return createRectifiedGridDomainType();
            case Gml311Package.RECTIFIED_GRID_TYPE: return createRectifiedGridType();
            case Gml311Package.REFERENCE_SYSTEM_REF_TYPE: return createReferenceSystemRefType();
            case Gml311Package.REFERENCE_TYPE: return createReferenceType();
            case Gml311Package.REF_LOCATION_TYPE: return createRefLocationType();
            case Gml311Package.RELATED_TIME_TYPE: return createRelatedTimeType();
            case Gml311Package.RELATIVE_INTERNAL_POSITIONAL_ACCURACY_TYPE: return createRelativeInternalPositionalAccuracyType();
            case Gml311Package.RING_PROPERTY_TYPE: return createRingPropertyType();
            case Gml311Package.RING_TYPE: return createRingType();
            case Gml311Package.ROW_TYPE: return createRowType();
            case Gml311Package.SCALAR_VALUE_PROPERTY_TYPE: return createScalarValuePropertyType();
            case Gml311Package.SCALE_TYPE: return createScaleType();
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE: return createSecondDefiningParameterType();
            case Gml311Package.SEQUENCE_RULE_TYPE: return createSequenceRuleType();
            case Gml311Package.SINGLE_OPERATION_REF_TYPE: return createSingleOperationRefType();
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE: return createSolidArrayPropertyType();
            case Gml311Package.SOLID_PROPERTY_TYPE: return createSolidPropertyType();
            case Gml311Package.SOLID_TYPE: return createSolidType();
            case Gml311Package.SPEED_TYPE: return createSpeedType();
            case Gml311Package.SPHERE_TYPE: return createSphereType();
            case Gml311Package.SPHERICAL_CS_REF_TYPE: return createSphericalCSRefType();
            case Gml311Package.SPHERICAL_CS_TYPE: return createSphericalCSType();
            case Gml311Package.STRING_OR_REF_TYPE: return createStringOrRefType();
            case Gml311Package.STYLE_TYPE: return createStyleType();
            case Gml311Package.STYLE_VARIATION_TYPE: return createStyleVariationType();
            case Gml311Package.SURFACE_ARRAY_PROPERTY_TYPE: return createSurfaceArrayPropertyType();
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE: return createSurfacePatchArrayPropertyType();
            case Gml311Package.SURFACE_PROPERTY_TYPE: return createSurfacePropertyType();
            case Gml311Package.SURFACE_TYPE: return createSurfaceType();
            case Gml311Package.SYMBOL_TYPE: return createSymbolType();
            case Gml311Package.TARGET_PROPERTY_TYPE: return createTargetPropertyType();
            case Gml311Package.TEMPORAL_CRS_REF_TYPE: return createTemporalCRSRefType();
            case Gml311Package.TEMPORAL_CRS_TYPE: return createTemporalCRSType();
            case Gml311Package.TEMPORAL_CS_REF_TYPE: return createTemporalCSRefType();
            case Gml311Package.TEMPORAL_CS_TYPE: return createTemporalCSType();
            case Gml311Package.TEMPORAL_DATUM_REF_TYPE: return createTemporalDatumRefType();
            case Gml311Package.TEMPORAL_DATUM_TYPE: return createTemporalDatumType();
            case Gml311Package.TIME_CALENDAR_ERA_PROPERTY_TYPE: return createTimeCalendarEraPropertyType();
            case Gml311Package.TIME_CALENDAR_ERA_TYPE: return createTimeCalendarEraType();
            case Gml311Package.TIME_CALENDAR_PROPERTY_TYPE: return createTimeCalendarPropertyType();
            case Gml311Package.TIME_CALENDAR_TYPE: return createTimeCalendarType();
            case Gml311Package.TIME_CLOCK_PROPERTY_TYPE: return createTimeClockPropertyType();
            case Gml311Package.TIME_CLOCK_TYPE: return createTimeClockType();
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE: return createTimeCoordinateSystemType();
            case Gml311Package.TIME_EDGE_PROPERTY_TYPE: return createTimeEdgePropertyType();
            case Gml311Package.TIME_EDGE_TYPE: return createTimeEdgeType();
            case Gml311Package.TIME_GEOMETRIC_PRIMITIVE_PROPERTY_TYPE: return createTimeGeometricPrimitivePropertyType();
            case Gml311Package.TIME_INSTANT_PROPERTY_TYPE: return createTimeInstantPropertyType();
            case Gml311Package.TIME_INSTANT_TYPE: return createTimeInstantType();
            case Gml311Package.TIME_INTERVAL_LENGTH_TYPE: return createTimeIntervalLengthType();
            case Gml311Package.TIME_NODE_PROPERTY_TYPE: return createTimeNodePropertyType();
            case Gml311Package.TIME_NODE_TYPE: return createTimeNodeType();
            case Gml311Package.TIME_ORDINAL_ERA_PROPERTY_TYPE: return createTimeOrdinalEraPropertyType();
            case Gml311Package.TIME_ORDINAL_ERA_TYPE: return createTimeOrdinalEraType();
            case Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE: return createTimeOrdinalReferenceSystemType();
            case Gml311Package.TIME_PERIOD_PROPERTY_TYPE: return createTimePeriodPropertyType();
            case Gml311Package.TIME_PERIOD_TYPE: return createTimePeriodType();
            case Gml311Package.TIME_POSITION_TYPE: return createTimePositionType();
            case Gml311Package.TIME_PRIMITIVE_PROPERTY_TYPE: return createTimePrimitivePropertyType();
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_PROPERTY_TYPE: return createTimeTopologyComplexPropertyType();
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE: return createTimeTopologyComplexType();
            case Gml311Package.TIME_TOPOLOGY_PRIMITIVE_PROPERTY_TYPE: return createTimeTopologyPrimitivePropertyType();
            case Gml311Package.TIME_TYPE: return createTimeType();
            case Gml311Package.TIN_TYPE: return createTinType();
            case Gml311Package.TOPO_COMPLEX_MEMBER_TYPE: return createTopoComplexMemberType();
            case Gml311Package.TOPO_COMPLEX_TYPE: return createTopoComplexType();
            case Gml311Package.TOPO_CURVE_PROPERTY_TYPE: return createTopoCurvePropertyType();
            case Gml311Package.TOPO_CURVE_TYPE: return createTopoCurveType();
            case Gml311Package.TOPOLOGY_STYLE_PROPERTY_TYPE: return createTopologyStylePropertyType();
            case Gml311Package.TOPOLOGY_STYLE_TYPE: return createTopologyStyleType();
            case Gml311Package.TOPO_POINT_PROPERTY_TYPE: return createTopoPointPropertyType();
            case Gml311Package.TOPO_POINT_TYPE: return createTopoPointType();
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE: return createTopoPrimitiveArrayAssociationType();
            case Gml311Package.TOPO_PRIMITIVE_MEMBER_TYPE: return createTopoPrimitiveMemberType();
            case Gml311Package.TOPO_SOLID_TYPE: return createTopoSolidType();
            case Gml311Package.TOPO_SURFACE_PROPERTY_TYPE: return createTopoSurfacePropertyType();
            case Gml311Package.TOPO_SURFACE_TYPE: return createTopoSurfaceType();
            case Gml311Package.TOPO_VOLUME_PROPERTY_TYPE: return createTopoVolumePropertyType();
            case Gml311Package.TOPO_VOLUME_TYPE: return createTopoVolumeType();
            case Gml311Package.TRACK_TYPE: return createTrackType();
            case Gml311Package.TRANSFORMATION_REF_TYPE: return createTransformationRefType();
            case Gml311Package.TRANSFORMATION_TYPE: return createTransformationType();
            case Gml311Package.TRIANGLE_PATCH_ARRAY_PROPERTY_TYPE: return createTrianglePatchArrayPropertyType();
            case Gml311Package.TRIANGLE_TYPE: return createTriangleType();
            case Gml311Package.TRIANGULATED_SURFACE_TYPE: return createTriangulatedSurfaceType();
            case Gml311Package.UNIT_DEFINITION_TYPE: return createUnitDefinitionType();
            case Gml311Package.UNIT_OF_MEASURE_TYPE: return createUnitOfMeasureType();
            case Gml311Package.USER_DEFINED_CS_REF_TYPE: return createUserDefinedCSRefType();
            case Gml311Package.USER_DEFINED_CS_TYPE: return createUserDefinedCSType();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE: return createValueArrayPropertyType();
            case Gml311Package.VALUE_ARRAY_TYPE: return createValueArrayType();
            case Gml311Package.VALUE_PROPERTY_TYPE: return createValuePropertyType();
            case Gml311Package.VECTOR_TYPE: return createVectorType();
            case Gml311Package.VERTICAL_CRS_REF_TYPE: return createVerticalCRSRefType();
            case Gml311Package.VERTICAL_CRS_TYPE: return createVerticalCRSType();
            case Gml311Package.VERTICAL_CS_REF_TYPE: return createVerticalCSRefType();
            case Gml311Package.VERTICAL_CS_TYPE: return createVerticalCSType();
            case Gml311Package.VERTICAL_DATUM_REF_TYPE: return createVerticalDatumRefType();
            case Gml311Package.VERTICAL_DATUM_TYPE: return createVerticalDatumType();
            case Gml311Package.VERTICAL_DATUM_TYPE_TYPE: return createVerticalDatumTypeType();
            case Gml311Package.VOLUME_TYPE: return createVolumeType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case Gml311Package.AESHETIC_CRITERIA_TYPE:
                return createAesheticCriteriaTypeFromString(eDataType, initialValue);
            case Gml311Package.COMPASS_POINT_ENUMERATION:
                return createCompassPointEnumerationFromString(eDataType, initialValue);
            case Gml311Package.CURVE_INTERPOLATION_TYPE:
                return createCurveInterpolationTypeFromString(eDataType, initialValue);
            case Gml311Package.DIRECTION_TYPE_MEMBER0:
                return createDirectionTypeMember0FromString(eDataType, initialValue);
            case Gml311Package.DRAWING_TYPE_TYPE:
                return createDrawingTypeTypeFromString(eDataType, initialValue);
            case Gml311Package.FILE_VALUE_MODEL_TYPE:
                return createFileValueModelTypeFromString(eDataType, initialValue);
            case Gml311Package.GRAPH_TYPE_TYPE:
                return createGraphTypeTypeFromString(eDataType, initialValue);
            case Gml311Package.INCREMENT_ORDER:
                return createIncrementOrderFromString(eDataType, initialValue);
            case Gml311Package.IS_SPHERE_TYPE:
                return createIsSphereTypeFromString(eDataType, initialValue);
            case Gml311Package.KNOT_TYPES_TYPE:
                return createKnotTypesTypeFromString(eDataType, initialValue);
            case Gml311Package.LINE_TYPE_TYPE:
                return createLineTypeTypeFromString(eDataType, initialValue);
            case Gml311Package.NULL_ENUMERATION_MEMBER0:
                return createNullEnumerationMember0FromString(eDataType, initialValue);
            case Gml311Package.QUERY_GRAMMAR_ENUMERATION:
                return createQueryGrammarEnumerationFromString(eDataType, initialValue);
            case Gml311Package.RELATIVE_POSITION_TYPE:
                return createRelativePositionTypeFromString(eDataType, initialValue);
            case Gml311Package.SEQUENCE_RULE_NAMES:
                return createSequenceRuleNamesFromString(eDataType, initialValue);
            case Gml311Package.SIGN_TYPE:
                return createSignTypeFromString(eDataType, initialValue);
            case Gml311Package.SUCCESSION_TYPE:
                return createSuccessionTypeFromString(eDataType, initialValue);
            case Gml311Package.SURFACE_INTERPOLATION_TYPE:
                return createSurfaceInterpolationTypeFromString(eDataType, initialValue);
            case Gml311Package.SYMBOL_TYPE_ENUMERATION:
                return createSymbolTypeEnumerationFromString(eDataType, initialValue);
            case Gml311Package.TIME_INDETERMINATE_VALUE_TYPE:
                return createTimeIndeterminateValueTypeFromString(eDataType, initialValue);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER0:
                return createTimeUnitTypeMember0FromString(eDataType, initialValue);
            case Gml311Package.AESHETIC_CRITERIA_TYPE_OBJECT:
                return createAesheticCriteriaTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.ARC_MINUTES_TYPE:
                return createArcMinutesTypeFromString(eDataType, initialValue);
            case Gml311Package.ARC_SECONDS_TYPE:
                return createArcSecondsTypeFromString(eDataType, initialValue);
            case Gml311Package.BOOLEAN_LIST:
                return createBooleanListFromString(eDataType, initialValue);
            case Gml311Package.BOOLEAN_OR_NULL:
                return createBooleanOrNullFromString(eDataType, initialValue);
            case Gml311Package.BOOLEAN_OR_NULL_LIST:
                return createBooleanOrNullListFromString(eDataType, initialValue);
            case Gml311Package.CAL_DATE:
                return createCalDateFromString(eDataType, initialValue);
            case Gml311Package.COMPASS_POINT_ENUMERATION_OBJECT:
                return createCompassPointEnumerationObjectFromString(eDataType, initialValue);
            case Gml311Package.COUNT_EXTENT_TYPE:
                return createCountExtentTypeFromString(eDataType, initialValue);
            case Gml311Package.CURVE_INTERPOLATION_TYPE_OBJECT:
                return createCurveInterpolationTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.DECIMAL_MINUTES_TYPE:
                return createDecimalMinutesTypeFromString(eDataType, initialValue);
            case Gml311Package.DEGREE_VALUE_TYPE:
                return createDegreeValueTypeFromString(eDataType, initialValue);
            case Gml311Package.DIRECTION_TYPE:
                return createDirectionTypeFromString(eDataType, initialValue);
            case Gml311Package.DIRECTION_TYPE_MEMBER0_OBJECT:
                return createDirectionTypeMember0ObjectFromString(eDataType, initialValue);
            case Gml311Package.DIRECTION_TYPE_MEMBER1:
                return createDirectionTypeMember1FromString(eDataType, initialValue);
            case Gml311Package.DOUBLE_LIST:
                return createDoubleListFromString(eDataType, initialValue);
            case Gml311Package.DOUBLE_OR_NULL:
                return createDoubleOrNullFromString(eDataType, initialValue);
            case Gml311Package.DOUBLE_OR_NULL_LIST:
                return createDoubleOrNullListFromString(eDataType, initialValue);
            case Gml311Package.DRAWING_TYPE_TYPE_OBJECT:
                return createDrawingTypeTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.FILE_VALUE_MODEL_TYPE_OBJECT:
                return createFileValueModelTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.GRAPH_TYPE_TYPE_OBJECT:
                return createGraphTypeTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.INCREMENT_ORDER_OBJECT:
                return createIncrementOrderObjectFromString(eDataType, initialValue);
            case Gml311Package.INTEGER_LIST:
                return createIntegerListFromString(eDataType, initialValue);
            case Gml311Package.INTEGER_OR_NULL:
                return createIntegerOrNullFromString(eDataType, initialValue);
            case Gml311Package.INTEGER_OR_NULL_LIST:
                return createIntegerOrNullListFromString(eDataType, initialValue);
            case Gml311Package.IS_SPHERE_TYPE_OBJECT:
                return createIsSphereTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.KNOT_TYPES_TYPE_OBJECT:
                return createKnotTypesTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.LINE_TYPE_TYPE_OBJECT:
                return createLineTypeTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.NAME_LIST:
                return createNameListFromString(eDataType, initialValue);
            case Gml311Package.NAME_OR_NULL:
                return createNameOrNullFromString(eDataType, initialValue);
            case Gml311Package.NAME_OR_NULL_LIST:
                return createNameOrNullListFromString(eDataType, initialValue);
            case Gml311Package.NC_NAME_LIST:
                return createNCNameListFromString(eDataType, initialValue);
            case Gml311Package.NULL_ENUMERATION:
                return createNullEnumerationFromString(eDataType, initialValue);
            case Gml311Package.NULL_ENUMERATION_MEMBER0_OBJECT:
                return createNullEnumerationMember0ObjectFromString(eDataType, initialValue);
            case Gml311Package.NULL_ENUMERATION_MEMBER1:
                return createNullEnumerationMember1FromString(eDataType, initialValue);
            case Gml311Package.NULL_TYPE:
                return createNullTypeFromString(eDataType, initialValue);
            case Gml311Package.QNAME_LIST:
                return createQNameListFromString(eDataType, initialValue);
            case Gml311Package.QUERY_GRAMMAR_ENUMERATION_OBJECT:
                return createQueryGrammarEnumerationObjectFromString(eDataType, initialValue);
            case Gml311Package.RELATIVE_POSITION_TYPE_OBJECT:
                return createRelativePositionTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.SEQUENCE_RULE_NAMES_OBJECT:
                return createSequenceRuleNamesObjectFromString(eDataType, initialValue);
            case Gml311Package.SIGN_TYPE_OBJECT:
                return createSignTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.STRING_OR_NULL:
                return createStringOrNullFromString(eDataType, initialValue);
            case Gml311Package.SUCCESSION_TYPE_OBJECT:
                return createSuccessionTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.SURFACE_INTERPOLATION_TYPE_OBJECT:
                return createSurfaceInterpolationTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.SYMBOL_TYPE_ENUMERATION_OBJECT:
                return createSymbolTypeEnumerationObjectFromString(eDataType, initialValue);
            case Gml311Package.TIME_INDETERMINATE_VALUE_TYPE_OBJECT:
                return createTimeIndeterminateValueTypeObjectFromString(eDataType, initialValue);
            case Gml311Package.TIME_POSITION_UNION:
                return createTimePositionUnionFromString(eDataType, initialValue);
            case Gml311Package.TIME_UNIT_TYPE:
                return createTimeUnitTypeFromString(eDataType, initialValue);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER0_OBJECT:
                return createTimeUnitTypeMember0ObjectFromString(eDataType, initialValue);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER1:
                return createTimeUnitTypeMember1FromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case Gml311Package.AESHETIC_CRITERIA_TYPE:
                return convertAesheticCriteriaTypeToString(eDataType, instanceValue);
            case Gml311Package.COMPASS_POINT_ENUMERATION:
                return convertCompassPointEnumerationToString(eDataType, instanceValue);
            case Gml311Package.CURVE_INTERPOLATION_TYPE:
                return convertCurveInterpolationTypeToString(eDataType, instanceValue);
            case Gml311Package.DIRECTION_TYPE_MEMBER0:
                return convertDirectionTypeMember0ToString(eDataType, instanceValue);
            case Gml311Package.DRAWING_TYPE_TYPE:
                return convertDrawingTypeTypeToString(eDataType, instanceValue);
            case Gml311Package.FILE_VALUE_MODEL_TYPE:
                return convertFileValueModelTypeToString(eDataType, instanceValue);
            case Gml311Package.GRAPH_TYPE_TYPE:
                return convertGraphTypeTypeToString(eDataType, instanceValue);
            case Gml311Package.INCREMENT_ORDER:
                return convertIncrementOrderToString(eDataType, instanceValue);
            case Gml311Package.IS_SPHERE_TYPE:
                return convertIsSphereTypeToString(eDataType, instanceValue);
            case Gml311Package.KNOT_TYPES_TYPE:
                return convertKnotTypesTypeToString(eDataType, instanceValue);
            case Gml311Package.LINE_TYPE_TYPE:
                return convertLineTypeTypeToString(eDataType, instanceValue);
            case Gml311Package.NULL_ENUMERATION_MEMBER0:
                return convertNullEnumerationMember0ToString(eDataType, instanceValue);
            case Gml311Package.QUERY_GRAMMAR_ENUMERATION:
                return convertQueryGrammarEnumerationToString(eDataType, instanceValue);
            case Gml311Package.RELATIVE_POSITION_TYPE:
                return convertRelativePositionTypeToString(eDataType, instanceValue);
            case Gml311Package.SEQUENCE_RULE_NAMES:
                return convertSequenceRuleNamesToString(eDataType, instanceValue);
            case Gml311Package.SIGN_TYPE:
                return convertSignTypeToString(eDataType, instanceValue);
            case Gml311Package.SUCCESSION_TYPE:
                return convertSuccessionTypeToString(eDataType, instanceValue);
            case Gml311Package.SURFACE_INTERPOLATION_TYPE:
                return convertSurfaceInterpolationTypeToString(eDataType, instanceValue);
            case Gml311Package.SYMBOL_TYPE_ENUMERATION:
                return convertSymbolTypeEnumerationToString(eDataType, instanceValue);
            case Gml311Package.TIME_INDETERMINATE_VALUE_TYPE:
                return convertTimeIndeterminateValueTypeToString(eDataType, instanceValue);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER0:
                return convertTimeUnitTypeMember0ToString(eDataType, instanceValue);
            case Gml311Package.AESHETIC_CRITERIA_TYPE_OBJECT:
                return convertAesheticCriteriaTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.ARC_MINUTES_TYPE:
                return convertArcMinutesTypeToString(eDataType, instanceValue);
            case Gml311Package.ARC_SECONDS_TYPE:
                return convertArcSecondsTypeToString(eDataType, instanceValue);
            case Gml311Package.BOOLEAN_LIST:
                return convertBooleanListToString(eDataType, instanceValue);
            case Gml311Package.BOOLEAN_OR_NULL:
                return convertBooleanOrNullToString(eDataType, instanceValue);
            case Gml311Package.BOOLEAN_OR_NULL_LIST:
                return convertBooleanOrNullListToString(eDataType, instanceValue);
            case Gml311Package.CAL_DATE:
                return convertCalDateToString(eDataType, instanceValue);
            case Gml311Package.COMPASS_POINT_ENUMERATION_OBJECT:
                return convertCompassPointEnumerationObjectToString(eDataType, instanceValue);
            case Gml311Package.COUNT_EXTENT_TYPE:
                return convertCountExtentTypeToString(eDataType, instanceValue);
            case Gml311Package.CURVE_INTERPOLATION_TYPE_OBJECT:
                return convertCurveInterpolationTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.DECIMAL_MINUTES_TYPE:
                return convertDecimalMinutesTypeToString(eDataType, instanceValue);
            case Gml311Package.DEGREE_VALUE_TYPE:
                return convertDegreeValueTypeToString(eDataType, instanceValue);
            case Gml311Package.DIRECTION_TYPE:
                return convertDirectionTypeToString(eDataType, instanceValue);
            case Gml311Package.DIRECTION_TYPE_MEMBER0_OBJECT:
                return convertDirectionTypeMember0ObjectToString(eDataType, instanceValue);
            case Gml311Package.DIRECTION_TYPE_MEMBER1:
                return convertDirectionTypeMember1ToString(eDataType, instanceValue);
            case Gml311Package.DOUBLE_LIST:
                return convertDoubleListToString(eDataType, instanceValue);
            case Gml311Package.DOUBLE_OR_NULL:
                return convertDoubleOrNullToString(eDataType, instanceValue);
            case Gml311Package.DOUBLE_OR_NULL_LIST:
                return convertDoubleOrNullListToString(eDataType, instanceValue);
            case Gml311Package.DRAWING_TYPE_TYPE_OBJECT:
                return convertDrawingTypeTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.FILE_VALUE_MODEL_TYPE_OBJECT:
                return convertFileValueModelTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.GRAPH_TYPE_TYPE_OBJECT:
                return convertGraphTypeTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.INCREMENT_ORDER_OBJECT:
                return convertIncrementOrderObjectToString(eDataType, instanceValue);
            case Gml311Package.INTEGER_LIST:
                return convertIntegerListToString(eDataType, instanceValue);
            case Gml311Package.INTEGER_OR_NULL:
                return convertIntegerOrNullToString(eDataType, instanceValue);
            case Gml311Package.INTEGER_OR_NULL_LIST:
                return convertIntegerOrNullListToString(eDataType, instanceValue);
            case Gml311Package.IS_SPHERE_TYPE_OBJECT:
                return convertIsSphereTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.KNOT_TYPES_TYPE_OBJECT:
                return convertKnotTypesTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.LINE_TYPE_TYPE_OBJECT:
                return convertLineTypeTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.NAME_LIST:
                return convertNameListToString(eDataType, instanceValue);
            case Gml311Package.NAME_OR_NULL:
                return convertNameOrNullToString(eDataType, instanceValue);
            case Gml311Package.NAME_OR_NULL_LIST:
                return convertNameOrNullListToString(eDataType, instanceValue);
            case Gml311Package.NC_NAME_LIST:
                return convertNCNameListToString(eDataType, instanceValue);
            case Gml311Package.NULL_ENUMERATION:
                return convertNullEnumerationToString(eDataType, instanceValue);
            case Gml311Package.NULL_ENUMERATION_MEMBER0_OBJECT:
                return convertNullEnumerationMember0ObjectToString(eDataType, instanceValue);
            case Gml311Package.NULL_ENUMERATION_MEMBER1:
                return convertNullEnumerationMember1ToString(eDataType, instanceValue);
            case Gml311Package.NULL_TYPE:
                return convertNullTypeToString(eDataType, instanceValue);
            case Gml311Package.QNAME_LIST:
                return convertQNameListToString(eDataType, instanceValue);
            case Gml311Package.QUERY_GRAMMAR_ENUMERATION_OBJECT:
                return convertQueryGrammarEnumerationObjectToString(eDataType, instanceValue);
            case Gml311Package.RELATIVE_POSITION_TYPE_OBJECT:
                return convertRelativePositionTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.SEQUENCE_RULE_NAMES_OBJECT:
                return convertSequenceRuleNamesObjectToString(eDataType, instanceValue);
            case Gml311Package.SIGN_TYPE_OBJECT:
                return convertSignTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.STRING_OR_NULL:
                return convertStringOrNullToString(eDataType, instanceValue);
            case Gml311Package.SUCCESSION_TYPE_OBJECT:
                return convertSuccessionTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.SURFACE_INTERPOLATION_TYPE_OBJECT:
                return convertSurfaceInterpolationTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.SYMBOL_TYPE_ENUMERATION_OBJECT:
                return convertSymbolTypeEnumerationObjectToString(eDataType, instanceValue);
            case Gml311Package.TIME_INDETERMINATE_VALUE_TYPE_OBJECT:
                return convertTimeIndeterminateValueTypeObjectToString(eDataType, instanceValue);
            case Gml311Package.TIME_POSITION_UNION:
                return convertTimePositionUnionToString(eDataType, instanceValue);
            case Gml311Package.TIME_UNIT_TYPE:
                return convertTimeUnitTypeToString(eDataType, instanceValue);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER0_OBJECT:
                return convertTimeUnitTypeMember0ObjectToString(eDataType, instanceValue);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER1:
                return convertTimeUnitTypeMember1ToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbsoluteExternalPositionalAccuracyType createAbsoluteExternalPositionalAccuracyType() {
        AbsoluteExternalPositionalAccuracyTypeImpl absoluteExternalPositionalAccuracyType = new AbsoluteExternalPositionalAccuracyTypeImpl();
        return absoluteExternalPositionalAccuracyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractGeneralOperationParameterRefType createAbstractGeneralOperationParameterRefType() {
        AbstractGeneralOperationParameterRefTypeImpl abstractGeneralOperationParameterRefType = new AbstractGeneralOperationParameterRefTypeImpl();
        return abstractGeneralOperationParameterRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractGriddedSurfaceType createAbstractGriddedSurfaceType() {
        AbstractGriddedSurfaceTypeImpl abstractGriddedSurfaceType = new AbstractGriddedSurfaceTypeImpl();
        return abstractGriddedSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractParametricCurveSurfaceType createAbstractParametricCurveSurfaceType() {
        AbstractParametricCurveSurfaceTypeImpl abstractParametricCurveSurfaceType = new AbstractParametricCurveSurfaceTypeImpl();
        return abstractParametricCurveSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractRingPropertyType createAbstractRingPropertyType() {
        AbstractRingPropertyTypeImpl abstractRingPropertyType = new AbstractRingPropertyTypeImpl();
        return abstractRingPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractSolidType createAbstractSolidType() {
        AbstractSolidTypeImpl abstractSolidType = new AbstractSolidTypeImpl();
        return abstractSolidType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractSurfaceType createAbstractSurfaceType() {
        AbstractSurfaceTypeImpl abstractSurfaceType = new AbstractSurfaceTypeImpl();
        return abstractSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AffinePlacementType createAffinePlacementType() {
        AffinePlacementTypeImpl affinePlacementType = new AffinePlacementTypeImpl();
        return affinePlacementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AngleChoiceType createAngleChoiceType() {
        AngleChoiceTypeImpl angleChoiceType = new AngleChoiceTypeImpl();
        return angleChoiceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AngleType createAngleType() {
        AngleTypeImpl angleType = new AngleTypeImpl();
        return angleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArcByBulgeType createArcByBulgeType() {
        ArcByBulgeTypeImpl arcByBulgeType = new ArcByBulgeTypeImpl();
        return arcByBulgeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArcByCenterPointType createArcByCenterPointType() {
        ArcByCenterPointTypeImpl arcByCenterPointType = new ArcByCenterPointTypeImpl();
        return arcByCenterPointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArcStringByBulgeType createArcStringByBulgeType() {
        ArcStringByBulgeTypeImpl arcStringByBulgeType = new ArcStringByBulgeTypeImpl();
        return arcStringByBulgeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArcStringType createArcStringType() {
        ArcStringTypeImpl arcStringType = new ArcStringTypeImpl();
        return arcStringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArcType createArcType() {
        ArcTypeImpl arcType = new ArcTypeImpl();
        return arcType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AreaType createAreaType() {
        AreaTypeImpl areaType = new AreaTypeImpl();
        return areaType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArrayAssociationType createArrayAssociationType() {
        ArrayAssociationTypeImpl arrayAssociationType = new ArrayAssociationTypeImpl();
        return arrayAssociationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArrayType createArrayType() {
        ArrayTypeImpl arrayType = new ArrayTypeImpl();
        return arrayType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AssociationType createAssociationType() {
        AssociationTypeImpl associationType = new AssociationTypeImpl();
        return associationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BagType createBagType() {
        BagTypeImpl bagType = new BagTypeImpl();
        return bagType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BaseStyleDescriptorType createBaseStyleDescriptorType() {
        BaseStyleDescriptorTypeImpl baseStyleDescriptorType = new BaseStyleDescriptorTypeImpl();
        return baseStyleDescriptorType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BaseUnitType createBaseUnitType() {
        BaseUnitTypeImpl baseUnitType = new BaseUnitTypeImpl();
        return baseUnitType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BezierType createBezierType() {
        BezierTypeImpl bezierType = new BezierTypeImpl();
        return bezierType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BooleanPropertyType createBooleanPropertyType() {
        BooleanPropertyTypeImpl booleanPropertyType = new BooleanPropertyTypeImpl();
        return booleanPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BoundingShapeType createBoundingShapeType() {
        BoundingShapeTypeImpl boundingShapeType = new BoundingShapeTypeImpl();
        return boundingShapeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BSplineType createBSplineType() {
        BSplineTypeImpl bSplineType = new BSplineTypeImpl();
        return bSplineType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CartesianCSRefType createCartesianCSRefType() {
        CartesianCSRefTypeImpl cartesianCSRefType = new CartesianCSRefTypeImpl();
        return cartesianCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CartesianCSType createCartesianCSType() {
        CartesianCSTypeImpl cartesianCSType = new CartesianCSTypeImpl();
        return cartesianCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CategoryExtentType createCategoryExtentType() {
        CategoryExtentTypeImpl categoryExtentType = new CategoryExtentTypeImpl();
        return categoryExtentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CategoryPropertyType createCategoryPropertyType() {
        CategoryPropertyTypeImpl categoryPropertyType = new CategoryPropertyTypeImpl();
        return categoryPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CircleByCenterPointType createCircleByCenterPointType() {
        CircleByCenterPointTypeImpl circleByCenterPointType = new CircleByCenterPointTypeImpl();
        return circleByCenterPointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CircleType createCircleType() {
        CircleTypeImpl circleType = new CircleTypeImpl();
        return circleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ClothoidType createClothoidType() {
        ClothoidTypeImpl clothoidType = new ClothoidTypeImpl();
        return clothoidType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeListType createCodeListType() {
        CodeListTypeImpl codeListType = new CodeListTypeImpl();
        return codeListType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeOrNullListType createCodeOrNullListType() {
        CodeOrNullListTypeImpl codeOrNullListType = new CodeOrNullListTypeImpl();
        return codeOrNullListType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType createCodeType() {
        CodeTypeImpl codeType = new CodeTypeImpl();
        return codeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeCurvePropertyType createCompositeCurvePropertyType() {
        CompositeCurvePropertyTypeImpl compositeCurvePropertyType = new CompositeCurvePropertyTypeImpl();
        return compositeCurvePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeCurveType createCompositeCurveType() {
        CompositeCurveTypeImpl compositeCurveType = new CompositeCurveTypeImpl();
        return compositeCurveType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeSolidPropertyType createCompositeSolidPropertyType() {
        CompositeSolidPropertyTypeImpl compositeSolidPropertyType = new CompositeSolidPropertyTypeImpl();
        return compositeSolidPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeSolidType createCompositeSolidType() {
        CompositeSolidTypeImpl compositeSolidType = new CompositeSolidTypeImpl();
        return compositeSolidType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeSurfacePropertyType createCompositeSurfacePropertyType() {
        CompositeSurfacePropertyTypeImpl compositeSurfacePropertyType = new CompositeSurfacePropertyTypeImpl();
        return compositeSurfacePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeSurfaceType createCompositeSurfaceType() {
        CompositeSurfaceTypeImpl compositeSurfaceType = new CompositeSurfaceTypeImpl();
        return compositeSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompositeValueType createCompositeValueType() {
        CompositeValueTypeImpl compositeValueType = new CompositeValueTypeImpl();
        return compositeValueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompoundCRSRefType createCompoundCRSRefType() {
        CompoundCRSRefTypeImpl compoundCRSRefType = new CompoundCRSRefTypeImpl();
        return compoundCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompoundCRSType createCompoundCRSType() {
        CompoundCRSTypeImpl compoundCRSType = new CompoundCRSTypeImpl();
        return compoundCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConcatenatedOperationRefType createConcatenatedOperationRefType() {
        ConcatenatedOperationRefTypeImpl concatenatedOperationRefType = new ConcatenatedOperationRefTypeImpl();
        return concatenatedOperationRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConcatenatedOperationType createConcatenatedOperationType() {
        ConcatenatedOperationTypeImpl concatenatedOperationType = new ConcatenatedOperationTypeImpl();
        return concatenatedOperationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConeType createConeType() {
        ConeTypeImpl coneType = new ConeTypeImpl();
        return coneType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContainerPropertyType createContainerPropertyType() {
        ContainerPropertyTypeImpl containerPropertyType = new ContainerPropertyTypeImpl();
        return containerPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ControlPointType createControlPointType() {
        ControlPointTypeImpl controlPointType = new ControlPointTypeImpl();
        return controlPointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConventionalUnitType createConventionalUnitType() {
        ConventionalUnitTypeImpl conventionalUnitType = new ConventionalUnitTypeImpl();
        return conventionalUnitType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConversionRefType createConversionRefType() {
        ConversionRefTypeImpl conversionRefType = new ConversionRefTypeImpl();
        return conversionRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConversionToPreferredUnitType createConversionToPreferredUnitType() {
        ConversionToPreferredUnitTypeImpl conversionToPreferredUnitType = new ConversionToPreferredUnitTypeImpl();
        return conversionToPreferredUnitType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConversionType createConversionType() {
        ConversionTypeImpl conversionType = new ConversionTypeImpl();
        return conversionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinateOperationRefType createCoordinateOperationRefType() {
        CoordinateOperationRefTypeImpl coordinateOperationRefType = new CoordinateOperationRefTypeImpl();
        return coordinateOperationRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinateReferenceSystemRefType createCoordinateReferenceSystemRefType() {
        CoordinateReferenceSystemRefTypeImpl coordinateReferenceSystemRefType = new CoordinateReferenceSystemRefTypeImpl();
        return coordinateReferenceSystemRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinatesType createCoordinatesType() {
        CoordinatesTypeImpl coordinatesType = new CoordinatesTypeImpl();
        return coordinatesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinateSystemAxisRefType createCoordinateSystemAxisRefType() {
        CoordinateSystemAxisRefTypeImpl coordinateSystemAxisRefType = new CoordinateSystemAxisRefTypeImpl();
        return coordinateSystemAxisRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinateSystemAxisType createCoordinateSystemAxisType() {
        CoordinateSystemAxisTypeImpl coordinateSystemAxisType = new CoordinateSystemAxisTypeImpl();
        return coordinateSystemAxisType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinateSystemRefType createCoordinateSystemRefType() {
        CoordinateSystemRefTypeImpl coordinateSystemRefType = new CoordinateSystemRefTypeImpl();
        return coordinateSystemRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordType createCoordType() {
        CoordTypeImpl coordType = new CoordTypeImpl();
        return coordType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CountPropertyType createCountPropertyType() {
        CountPropertyTypeImpl countPropertyType = new CountPropertyTypeImpl();
        return countPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CovarianceElementType createCovarianceElementType() {
        CovarianceElementTypeImpl covarianceElementType = new CovarianceElementTypeImpl();
        return covarianceElementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CovarianceMatrixType createCovarianceMatrixType() {
        CovarianceMatrixTypeImpl covarianceMatrixType = new CovarianceMatrixTypeImpl();
        return covarianceMatrixType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageFunctionType createCoverageFunctionType() {
        CoverageFunctionTypeImpl coverageFunctionType = new CoverageFunctionTypeImpl();
        return coverageFunctionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CRSRefType createCRSRefType() {
        CRSRefTypeImpl crsRefType = new CRSRefTypeImpl();
        return crsRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CubicSplineType createCubicSplineType() {
        CubicSplineTypeImpl cubicSplineType = new CubicSplineTypeImpl();
        return cubicSplineType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurveArrayPropertyType createCurveArrayPropertyType() {
        CurveArrayPropertyTypeImpl curveArrayPropertyType = new CurveArrayPropertyTypeImpl();
        return curveArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurvePropertyType createCurvePropertyType() {
        CurvePropertyTypeImpl curvePropertyType = new CurvePropertyTypeImpl();
        return curvePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurveSegmentArrayPropertyType createCurveSegmentArrayPropertyType() {
        CurveSegmentArrayPropertyTypeImpl curveSegmentArrayPropertyType = new CurveSegmentArrayPropertyTypeImpl();
        return curveSegmentArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurveType createCurveType() {
        CurveTypeImpl curveType = new CurveTypeImpl();
        return curveType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CylinderType createCylinderType() {
        CylinderTypeImpl cylinderType = new CylinderTypeImpl();
        return cylinderType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CylindricalCSRefType createCylindricalCSRefType() {
        CylindricalCSRefTypeImpl cylindricalCSRefType = new CylindricalCSRefTypeImpl();
        return cylindricalCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CylindricalCSType createCylindricalCSType() {
        CylindricalCSTypeImpl cylindricalCSType = new CylindricalCSTypeImpl();
        return cylindricalCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataBlockType createDataBlockType() {
        DataBlockTypeImpl dataBlockType = new DataBlockTypeImpl();
        return dataBlockType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DatumRefType createDatumRefType() {
        DatumRefTypeImpl datumRefType = new DatumRefTypeImpl();
        return datumRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefaultStylePropertyType createDefaultStylePropertyType() {
        DefaultStylePropertyTypeImpl defaultStylePropertyType = new DefaultStylePropertyTypeImpl();
        return defaultStylePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefinitionProxyType createDefinitionProxyType() {
        DefinitionProxyTypeImpl definitionProxyType = new DefinitionProxyTypeImpl();
        return definitionProxyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefinitionType createDefinitionType() {
        DefinitionTypeImpl definitionType = new DefinitionTypeImpl();
        return definitionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DegreesType createDegreesType() {
        DegreesTypeImpl degreesType = new DegreesTypeImpl();
        return degreesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DerivationUnitTermType createDerivationUnitTermType() {
        DerivationUnitTermTypeImpl derivationUnitTermType = new DerivationUnitTermTypeImpl();
        return derivationUnitTermType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DerivedCRSRefType createDerivedCRSRefType() {
        DerivedCRSRefTypeImpl derivedCRSRefType = new DerivedCRSRefTypeImpl();
        return derivedCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DerivedCRSType createDerivedCRSType() {
        DerivedCRSTypeImpl derivedCRSType = new DerivedCRSTypeImpl();
        return derivedCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DerivedCRSTypeType createDerivedCRSTypeType() {
        DerivedCRSTypeTypeImpl derivedCRSTypeType = new DerivedCRSTypeTypeImpl();
        return derivedCRSTypeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DerivedUnitType createDerivedUnitType() {
        DerivedUnitTypeImpl derivedUnitType = new DerivedUnitTypeImpl();
        return derivedUnitType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DictionaryEntryType createDictionaryEntryType() {
        DictionaryEntryTypeImpl dictionaryEntryType = new DictionaryEntryTypeImpl();
        return dictionaryEntryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DictionaryType createDictionaryType() {
        DictionaryTypeImpl dictionaryType = new DictionaryTypeImpl();
        return dictionaryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectedEdgePropertyType createDirectedEdgePropertyType() {
        DirectedEdgePropertyTypeImpl directedEdgePropertyType = new DirectedEdgePropertyTypeImpl();
        return directedEdgePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectedFacePropertyType createDirectedFacePropertyType() {
        DirectedFacePropertyTypeImpl directedFacePropertyType = new DirectedFacePropertyTypeImpl();
        return directedFacePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectedNodePropertyType createDirectedNodePropertyType() {
        DirectedNodePropertyTypeImpl directedNodePropertyType = new DirectedNodePropertyTypeImpl();
        return directedNodePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectedObservationAtDistanceType createDirectedObservationAtDistanceType() {
        DirectedObservationAtDistanceTypeImpl directedObservationAtDistanceType = new DirectedObservationAtDistanceTypeImpl();
        return directedObservationAtDistanceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectedObservationType createDirectedObservationType() {
        DirectedObservationTypeImpl directedObservationType = new DirectedObservationTypeImpl();
        return directedObservationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectedTopoSolidPropertyType createDirectedTopoSolidPropertyType() {
        DirectedTopoSolidPropertyTypeImpl directedTopoSolidPropertyType = new DirectedTopoSolidPropertyTypeImpl();
        return directedTopoSolidPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectionPropertyType createDirectionPropertyType() {
        DirectionPropertyTypeImpl directionPropertyType = new DirectionPropertyTypeImpl();
        return directionPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectionVectorType createDirectionVectorType() {
        DirectionVectorTypeImpl directionVectorType = new DirectionVectorTypeImpl();
        return directionVectorType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectPositionListType createDirectPositionListType() {
        DirectPositionListTypeImpl directPositionListType = new DirectPositionListTypeImpl();
        return directPositionListType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectPositionType createDirectPositionType() {
        DirectPositionTypeImpl directPositionType = new DirectPositionTypeImpl();
        return directPositionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DMSAngleType createDMSAngleType() {
        DMSAngleTypeImpl dmsAngleType = new DMSAngleTypeImpl();
        return dmsAngleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DocumentRoot createDocumentRoot() {
        DocumentRootImpl documentRoot = new DocumentRootImpl();
        return documentRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainSetType createDomainSetType() {
        DomainSetTypeImpl domainSetType = new DomainSetTypeImpl();
        return domainSetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DynamicFeatureCollectionType createDynamicFeatureCollectionType() {
        DynamicFeatureCollectionTypeImpl dynamicFeatureCollectionType = new DynamicFeatureCollectionTypeImpl();
        return dynamicFeatureCollectionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DynamicFeatureType createDynamicFeatureType() {
        DynamicFeatureTypeImpl dynamicFeatureType = new DynamicFeatureTypeImpl();
        return dynamicFeatureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EdgeType createEdgeType() {
        EdgeTypeImpl edgeType = new EdgeTypeImpl();
        return edgeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EllipsoidalCSRefType createEllipsoidalCSRefType() {
        EllipsoidalCSRefTypeImpl ellipsoidalCSRefType = new EllipsoidalCSRefTypeImpl();
        return ellipsoidalCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EllipsoidalCSType createEllipsoidalCSType() {
        EllipsoidalCSTypeImpl ellipsoidalCSType = new EllipsoidalCSTypeImpl();
        return ellipsoidalCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EllipsoidRefType createEllipsoidRefType() {
        EllipsoidRefTypeImpl ellipsoidRefType = new EllipsoidRefTypeImpl();
        return ellipsoidRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EllipsoidType createEllipsoidType() {
        EllipsoidTypeImpl ellipsoidType = new EllipsoidTypeImpl();
        return ellipsoidType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EngineeringCRSRefType createEngineeringCRSRefType() {
        EngineeringCRSRefTypeImpl engineeringCRSRefType = new EngineeringCRSRefTypeImpl();
        return engineeringCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EngineeringCRSType createEngineeringCRSType() {
        EngineeringCRSTypeImpl engineeringCRSType = new EngineeringCRSTypeImpl();
        return engineeringCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EngineeringDatumRefType createEngineeringDatumRefType() {
        EngineeringDatumRefTypeImpl engineeringDatumRefType = new EngineeringDatumRefTypeImpl();
        return engineeringDatumRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EngineeringDatumType createEngineeringDatumType() {
        EngineeringDatumTypeImpl engineeringDatumType = new EngineeringDatumTypeImpl();
        return engineeringDatumType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EnvelopeType createEnvelopeType() {
        EnvelopeTypeImpl envelopeType = new EnvelopeTypeImpl();
        return envelopeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EnvelopeWithTimePeriodType createEnvelopeWithTimePeriodType() {
        EnvelopeWithTimePeriodTypeImpl envelopeWithTimePeriodType = new EnvelopeWithTimePeriodTypeImpl();
        return envelopeWithTimePeriodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtentType createExtentType() {
        ExtentTypeImpl extentType = new ExtentTypeImpl();
        return extentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FaceType createFaceType() {
        FaceTypeImpl faceType = new FaceTypeImpl();
        return faceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureArrayPropertyType createFeatureArrayPropertyType() {
        FeatureArrayPropertyTypeImpl featureArrayPropertyType = new FeatureArrayPropertyTypeImpl();
        return featureArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureCollectionType createFeatureCollectionType() {
        FeatureCollectionTypeImpl featureCollectionType = new FeatureCollectionTypeImpl();
        return featureCollectionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeaturePropertyType createFeaturePropertyType() {
        FeaturePropertyTypeImpl featurePropertyType = new FeaturePropertyTypeImpl();
        return featurePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureStylePropertyType createFeatureStylePropertyType() {
        FeatureStylePropertyTypeImpl featureStylePropertyType = new FeatureStylePropertyTypeImpl();
        return featureStylePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureStyleType createFeatureStyleType() {
        FeatureStyleTypeImpl featureStyleType = new FeatureStyleTypeImpl();
        return featureStyleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FileType createFileType() {
        FileTypeImpl fileType = new FileTypeImpl();
        return fileType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FormulaType createFormulaType() {
        FormulaTypeImpl formulaType = new FormulaTypeImpl();
        return formulaType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeneralConversionRefType createGeneralConversionRefType() {
        GeneralConversionRefTypeImpl generalConversionRefType = new GeneralConversionRefTypeImpl();
        return generalConversionRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeneralTransformationRefType createGeneralTransformationRefType() {
        GeneralTransformationRefTypeImpl generalTransformationRefType = new GeneralTransformationRefTypeImpl();
        return generalTransformationRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GenericMetaDataType createGenericMetaDataType() {
        GenericMetaDataTypeImpl genericMetaDataType = new GenericMetaDataTypeImpl();
        return genericMetaDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeocentricCRSRefType createGeocentricCRSRefType() {
        GeocentricCRSRefTypeImpl geocentricCRSRefType = new GeocentricCRSRefTypeImpl();
        return geocentricCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeocentricCRSType createGeocentricCRSType() {
        GeocentricCRSTypeImpl geocentricCRSType = new GeocentricCRSTypeImpl();
        return geocentricCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeodesicStringType createGeodesicStringType() {
        GeodesicStringTypeImpl geodesicStringType = new GeodesicStringTypeImpl();
        return geodesicStringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeodesicType createGeodesicType() {
        GeodesicTypeImpl geodesicType = new GeodesicTypeImpl();
        return geodesicType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeodeticDatumRefType createGeodeticDatumRefType() {
        GeodeticDatumRefTypeImpl geodeticDatumRefType = new GeodeticDatumRefTypeImpl();
        return geodeticDatumRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeodeticDatumType createGeodeticDatumType() {
        GeodeticDatumTypeImpl geodeticDatumType = new GeodeticDatumTypeImpl();
        return geodeticDatumType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeographicCRSRefType createGeographicCRSRefType() {
        GeographicCRSRefTypeImpl geographicCRSRefType = new GeographicCRSRefTypeImpl();
        return geographicCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeographicCRSType createGeographicCRSType() {
        GeographicCRSTypeImpl geographicCRSType = new GeographicCRSTypeImpl();
        return geographicCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometricComplexPropertyType createGeometricComplexPropertyType() {
        GeometricComplexPropertyTypeImpl geometricComplexPropertyType = new GeometricComplexPropertyTypeImpl();
        return geometricComplexPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometricComplexType createGeometricComplexType() {
        GeometricComplexTypeImpl geometricComplexType = new GeometricComplexTypeImpl();
        return geometricComplexType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometricPrimitivePropertyType createGeometricPrimitivePropertyType() {
        GeometricPrimitivePropertyTypeImpl geometricPrimitivePropertyType = new GeometricPrimitivePropertyTypeImpl();
        return geometricPrimitivePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometryArrayPropertyType createGeometryArrayPropertyType() {
        GeometryArrayPropertyTypeImpl geometryArrayPropertyType = new GeometryArrayPropertyTypeImpl();
        return geometryArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometryPropertyType createGeometryPropertyType() {
        GeometryPropertyTypeImpl geometryPropertyType = new GeometryPropertyTypeImpl();
        return geometryPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometryStylePropertyType createGeometryStylePropertyType() {
        GeometryStylePropertyTypeImpl geometryStylePropertyType = new GeometryStylePropertyTypeImpl();
        return geometryStylePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeometryStyleType createGeometryStyleType() {
        GeometryStyleTypeImpl geometryStyleType = new GeometryStyleTypeImpl();
        return geometryStyleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GraphStylePropertyType createGraphStylePropertyType() {
        GraphStylePropertyTypeImpl graphStylePropertyType = new GraphStylePropertyTypeImpl();
        return graphStylePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GraphStyleType createGraphStyleType() {
        GraphStyleTypeImpl graphStyleType = new GraphStyleTypeImpl();
        return graphStyleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridCoverageType createGridCoverageType() {
        GridCoverageTypeImpl gridCoverageType = new GridCoverageTypeImpl();
        return gridCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridDomainType createGridDomainType() {
        GridDomainTypeImpl gridDomainType = new GridDomainTypeImpl();
        return gridDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridEnvelopeType createGridEnvelopeType() {
        GridEnvelopeTypeImpl gridEnvelopeType = new GridEnvelopeTypeImpl();
        return gridEnvelopeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridFunctionType createGridFunctionType() {
        GridFunctionTypeImpl gridFunctionType = new GridFunctionTypeImpl();
        return gridFunctionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridLengthType createGridLengthType() {
        GridLengthTypeImpl gridLengthType = new GridLengthTypeImpl();
        return gridLengthType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridLimitsType createGridLimitsType() {
        GridLimitsTypeImpl gridLimitsType = new GridLimitsTypeImpl();
        return gridLimitsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridType createGridType() {
        GridTypeImpl gridType = new GridTypeImpl();
        return gridType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HistoryPropertyType createHistoryPropertyType() {
        HistoryPropertyTypeImpl historyPropertyType = new HistoryPropertyTypeImpl();
        return historyPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IdentifierType createIdentifierType() {
        IdentifierTypeImpl identifierType = new IdentifierTypeImpl();
        return identifierType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImageCRSRefType createImageCRSRefType() {
        ImageCRSRefTypeImpl imageCRSRefType = new ImageCRSRefTypeImpl();
        return imageCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImageCRSType createImageCRSType() {
        ImageCRSTypeImpl imageCRSType = new ImageCRSTypeImpl();
        return imageCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImageDatumRefType createImageDatumRefType() {
        ImageDatumRefTypeImpl imageDatumRefType = new ImageDatumRefTypeImpl();
        return imageDatumRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImageDatumType createImageDatumType() {
        ImageDatumTypeImpl imageDatumType = new ImageDatumTypeImpl();
        return imageDatumType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndexMapType createIndexMapType() {
        IndexMapTypeImpl indexMapType = new IndexMapTypeImpl();
        return indexMapType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IndirectEntryType createIndirectEntryType() {
        IndirectEntryTypeImpl indirectEntryType = new IndirectEntryTypeImpl();
        return indirectEntryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IsolatedPropertyType createIsolatedPropertyType() {
        IsolatedPropertyTypeImpl isolatedPropertyType = new IsolatedPropertyTypeImpl();
        return isolatedPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public KnotPropertyType createKnotPropertyType() {
        KnotPropertyTypeImpl knotPropertyType = new KnotPropertyTypeImpl();
        return knotPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public KnotType createKnotType() {
        KnotTypeImpl knotType = new KnotTypeImpl();
        return knotType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LabelStylePropertyType createLabelStylePropertyType() {
        LabelStylePropertyTypeImpl labelStylePropertyType = new LabelStylePropertyTypeImpl();
        return labelStylePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LabelStyleType createLabelStyleType() {
        LabelStyleTypeImpl labelStyleType = new LabelStyleTypeImpl();
        return labelStyleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LabelType createLabelType() {
        LabelTypeImpl labelType = new LabelTypeImpl();
        return labelType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LengthType createLengthType() {
        LengthTypeImpl lengthType = new LengthTypeImpl();
        return lengthType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LinearCSRefType createLinearCSRefType() {
        LinearCSRefTypeImpl linearCSRefType = new LinearCSRefTypeImpl();
        return linearCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LinearCSType createLinearCSType() {
        LinearCSTypeImpl linearCSType = new LinearCSTypeImpl();
        return linearCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LinearRingPropertyType createLinearRingPropertyType() {
        LinearRingPropertyTypeImpl linearRingPropertyType = new LinearRingPropertyTypeImpl();
        return linearRingPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LinearRingType createLinearRingType() {
        LinearRingTypeImpl linearRingType = new LinearRingTypeImpl();
        return linearRingType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LineStringPropertyType createLineStringPropertyType() {
        LineStringPropertyTypeImpl lineStringPropertyType = new LineStringPropertyTypeImpl();
        return lineStringPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LineStringSegmentArrayPropertyType createLineStringSegmentArrayPropertyType() {
        LineStringSegmentArrayPropertyTypeImpl lineStringSegmentArrayPropertyType = new LineStringSegmentArrayPropertyTypeImpl();
        return lineStringSegmentArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LineStringSegmentType createLineStringSegmentType() {
        LineStringSegmentTypeImpl lineStringSegmentType = new LineStringSegmentTypeImpl();
        return lineStringSegmentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LineStringType createLineStringType() {
        LineStringTypeImpl lineStringType = new LineStringTypeImpl();
        return lineStringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LocationPropertyType createLocationPropertyType() {
        LocationPropertyTypeImpl locationPropertyType = new LocationPropertyTypeImpl();
        return locationPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureListType createMeasureListType() {
        MeasureListTypeImpl measureListType = new MeasureListTypeImpl();
        return measureListType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureOrNullListType createMeasureOrNullListType() {
        MeasureOrNullListTypeImpl measureOrNullListType = new MeasureOrNullListTypeImpl();
        return measureOrNullListType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType createMeasureType() {
        MeasureTypeImpl measureType = new MeasureTypeImpl();
        return measureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MetaDataPropertyType createMetaDataPropertyType() {
        MetaDataPropertyTypeImpl metaDataPropertyType = new MetaDataPropertyTypeImpl();
        return metaDataPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MovingObjectStatusType createMovingObjectStatusType() {
        MovingObjectStatusTypeImpl movingObjectStatusType = new MovingObjectStatusTypeImpl();
        return movingObjectStatusType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiCurveCoverageType createMultiCurveCoverageType() {
        MultiCurveCoverageTypeImpl multiCurveCoverageType = new MultiCurveCoverageTypeImpl();
        return multiCurveCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiCurveDomainType createMultiCurveDomainType() {
        MultiCurveDomainTypeImpl multiCurveDomainType = new MultiCurveDomainTypeImpl();
        return multiCurveDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiCurvePropertyType createMultiCurvePropertyType() {
        MultiCurvePropertyTypeImpl multiCurvePropertyType = new MultiCurvePropertyTypeImpl();
        return multiCurvePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiCurveType createMultiCurveType() {
        MultiCurveTypeImpl multiCurveType = new MultiCurveTypeImpl();
        return multiCurveType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiGeometryPropertyType createMultiGeometryPropertyType() {
        MultiGeometryPropertyTypeImpl multiGeometryPropertyType = new MultiGeometryPropertyTypeImpl();
        return multiGeometryPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiGeometryType createMultiGeometryType() {
        MultiGeometryTypeImpl multiGeometryType = new MultiGeometryTypeImpl();
        return multiGeometryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiLineStringPropertyType createMultiLineStringPropertyType() {
        MultiLineStringPropertyTypeImpl multiLineStringPropertyType = new MultiLineStringPropertyTypeImpl();
        return multiLineStringPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiLineStringType createMultiLineStringType() {
        MultiLineStringTypeImpl multiLineStringType = new MultiLineStringTypeImpl();
        return multiLineStringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiPointCoverageType createMultiPointCoverageType() {
        MultiPointCoverageTypeImpl multiPointCoverageType = new MultiPointCoverageTypeImpl();
        return multiPointCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiPointDomainType createMultiPointDomainType() {
        MultiPointDomainTypeImpl multiPointDomainType = new MultiPointDomainTypeImpl();
        return multiPointDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiPointPropertyType createMultiPointPropertyType() {
        MultiPointPropertyTypeImpl multiPointPropertyType = new MultiPointPropertyTypeImpl();
        return multiPointPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiPointType createMultiPointType() {
        MultiPointTypeImpl multiPointType = new MultiPointTypeImpl();
        return multiPointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiPolygonPropertyType createMultiPolygonPropertyType() {
        MultiPolygonPropertyTypeImpl multiPolygonPropertyType = new MultiPolygonPropertyTypeImpl();
        return multiPolygonPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiPolygonType createMultiPolygonType() {
        MultiPolygonTypeImpl multiPolygonType = new MultiPolygonTypeImpl();
        return multiPolygonType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiSolidCoverageType createMultiSolidCoverageType() {
        MultiSolidCoverageTypeImpl multiSolidCoverageType = new MultiSolidCoverageTypeImpl();
        return multiSolidCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiSolidDomainType createMultiSolidDomainType() {
        MultiSolidDomainTypeImpl multiSolidDomainType = new MultiSolidDomainTypeImpl();
        return multiSolidDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiSolidPropertyType createMultiSolidPropertyType() {
        MultiSolidPropertyTypeImpl multiSolidPropertyType = new MultiSolidPropertyTypeImpl();
        return multiSolidPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiSolidType createMultiSolidType() {
        MultiSolidTypeImpl multiSolidType = new MultiSolidTypeImpl();
        return multiSolidType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiSurfaceCoverageType createMultiSurfaceCoverageType() {
        MultiSurfaceCoverageTypeImpl multiSurfaceCoverageType = new MultiSurfaceCoverageTypeImpl();
        return multiSurfaceCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiSurfaceDomainType createMultiSurfaceDomainType() {
        MultiSurfaceDomainTypeImpl multiSurfaceDomainType = new MultiSurfaceDomainTypeImpl();
        return multiSurfaceDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiSurfacePropertyType createMultiSurfacePropertyType() {
        MultiSurfacePropertyTypeImpl multiSurfacePropertyType = new MultiSurfacePropertyTypeImpl();
        return multiSurfacePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiSurfaceType createMultiSurfaceType() {
        MultiSurfaceTypeImpl multiSurfaceType = new MultiSurfaceTypeImpl();
        return multiSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NodeType createNodeType() {
        NodeTypeImpl nodeType = new NodeTypeImpl();
        return nodeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ObliqueCartesianCSRefType createObliqueCartesianCSRefType() {
        ObliqueCartesianCSRefTypeImpl obliqueCartesianCSRefType = new ObliqueCartesianCSRefTypeImpl();
        return obliqueCartesianCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ObliqueCartesianCSType createObliqueCartesianCSType() {
        ObliqueCartesianCSTypeImpl obliqueCartesianCSType = new ObliqueCartesianCSTypeImpl();
        return obliqueCartesianCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ObservationType createObservationType() {
        ObservationTypeImpl observationType = new ObservationTypeImpl();
        return observationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OffsetCurveType createOffsetCurveType() {
        OffsetCurveTypeImpl offsetCurveType = new OffsetCurveTypeImpl();
        return offsetCurveType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationMethodRefType createOperationMethodRefType() {
        OperationMethodRefTypeImpl operationMethodRefType = new OperationMethodRefTypeImpl();
        return operationMethodRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationMethodType createOperationMethodType() {
        OperationMethodTypeImpl operationMethodType = new OperationMethodTypeImpl();
        return operationMethodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationParameterGroupRefType createOperationParameterGroupRefType() {
        OperationParameterGroupRefTypeImpl operationParameterGroupRefType = new OperationParameterGroupRefTypeImpl();
        return operationParameterGroupRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationParameterGroupType createOperationParameterGroupType() {
        OperationParameterGroupTypeImpl operationParameterGroupType = new OperationParameterGroupTypeImpl();
        return operationParameterGroupType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationParameterRefType createOperationParameterRefType() {
        OperationParameterRefTypeImpl operationParameterRefType = new OperationParameterRefTypeImpl();
        return operationParameterRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationParameterType createOperationParameterType() {
        OperationParameterTypeImpl operationParameterType = new OperationParameterTypeImpl();
        return operationParameterType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationRefType createOperationRefType() {
        OperationRefTypeImpl operationRefType = new OperationRefTypeImpl();
        return operationRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OrientableCurveType createOrientableCurveType() {
        OrientableCurveTypeImpl orientableCurveType = new OrientableCurveTypeImpl();
        return orientableCurveType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OrientableSurfaceType createOrientableSurfaceType() {
        OrientableSurfaceTypeImpl orientableSurfaceType = new OrientableSurfaceTypeImpl();
        return orientableSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ParameterValueGroupType createParameterValueGroupType() {
        ParameterValueGroupTypeImpl parameterValueGroupType = new ParameterValueGroupTypeImpl();
        return parameterValueGroupType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ParameterValueType createParameterValueType() {
        ParameterValueTypeImpl parameterValueType = new ParameterValueTypeImpl();
        return parameterValueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PassThroughOperationRefType createPassThroughOperationRefType() {
        PassThroughOperationRefTypeImpl passThroughOperationRefType = new PassThroughOperationRefTypeImpl();
        return passThroughOperationRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PassThroughOperationType createPassThroughOperationType() {
        PassThroughOperationTypeImpl passThroughOperationType = new PassThroughOperationTypeImpl();
        return passThroughOperationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PixelInCellType createPixelInCellType() {
        PixelInCellTypeImpl pixelInCellType = new PixelInCellTypeImpl();
        return pixelInCellType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PointArrayPropertyType createPointArrayPropertyType() {
        PointArrayPropertyTypeImpl pointArrayPropertyType = new PointArrayPropertyTypeImpl();
        return pointArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PointPropertyType createPointPropertyType() {
        PointPropertyTypeImpl pointPropertyType = new PointPropertyTypeImpl();
        return pointPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PointType createPointType() {
        PointTypeImpl pointType = new PointTypeImpl();
        return pointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PolarCSRefType createPolarCSRefType() {
        PolarCSRefTypeImpl polarCSRefType = new PolarCSRefTypeImpl();
        return polarCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PolarCSType createPolarCSType() {
        PolarCSTypeImpl polarCSType = new PolarCSTypeImpl();
        return polarCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PolygonPatchArrayPropertyType createPolygonPatchArrayPropertyType() {
        PolygonPatchArrayPropertyTypeImpl polygonPatchArrayPropertyType = new PolygonPatchArrayPropertyTypeImpl();
        return polygonPatchArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PolygonPatchType createPolygonPatchType() {
        PolygonPatchTypeImpl polygonPatchType = new PolygonPatchTypeImpl();
        return polygonPatchType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PolygonPropertyType createPolygonPropertyType() {
        PolygonPropertyTypeImpl polygonPropertyType = new PolygonPropertyTypeImpl();
        return polygonPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PolygonType createPolygonType() {
        PolygonTypeImpl polygonType = new PolygonTypeImpl();
        return polygonType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PolyhedralSurfaceType createPolyhedralSurfaceType() {
        PolyhedralSurfaceTypeImpl polyhedralSurfaceType = new PolyhedralSurfaceTypeImpl();
        return polyhedralSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrimeMeridianRefType createPrimeMeridianRefType() {
        PrimeMeridianRefTypeImpl primeMeridianRefType = new PrimeMeridianRefTypeImpl();
        return primeMeridianRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrimeMeridianType createPrimeMeridianType() {
        PrimeMeridianTypeImpl primeMeridianType = new PrimeMeridianTypeImpl();
        return primeMeridianType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PriorityLocationPropertyType createPriorityLocationPropertyType() {
        PriorityLocationPropertyTypeImpl priorityLocationPropertyType = new PriorityLocationPropertyTypeImpl();
        return priorityLocationPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProjectedCRSRefType createProjectedCRSRefType() {
        ProjectedCRSRefTypeImpl projectedCRSRefType = new ProjectedCRSRefTypeImpl();
        return projectedCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProjectedCRSType createProjectedCRSType() {
        ProjectedCRSTypeImpl projectedCRSType = new ProjectedCRSTypeImpl();
        return projectedCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QuantityExtentType createQuantityExtentType() {
        QuantityExtentTypeImpl quantityExtentType = new QuantityExtentTypeImpl();
        return quantityExtentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QuantityPropertyType createQuantityPropertyType() {
        QuantityPropertyTypeImpl quantityPropertyType = new QuantityPropertyTypeImpl();
        return quantityPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeParametersType createRangeParametersType() {
        RangeParametersTypeImpl rangeParametersType = new RangeParametersTypeImpl();
        return rangeParametersType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeSetType createRangeSetType() {
        RangeSetTypeImpl rangeSetType = new RangeSetTypeImpl();
        return rangeSetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RectangleType createRectangleType() {
        RectangleTypeImpl rectangleType = new RectangleTypeImpl();
        return rectangleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RectifiedGridCoverageType createRectifiedGridCoverageType() {
        RectifiedGridCoverageTypeImpl rectifiedGridCoverageType = new RectifiedGridCoverageTypeImpl();
        return rectifiedGridCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RectifiedGridDomainType createRectifiedGridDomainType() {
        RectifiedGridDomainTypeImpl rectifiedGridDomainType = new RectifiedGridDomainTypeImpl();
        return rectifiedGridDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RectifiedGridType createRectifiedGridType() {
        RectifiedGridTypeImpl rectifiedGridType = new RectifiedGridTypeImpl();
        return rectifiedGridType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceSystemRefType createReferenceSystemRefType() {
        ReferenceSystemRefTypeImpl referenceSystemRefType = new ReferenceSystemRefTypeImpl();
        return referenceSystemRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceType createReferenceType() {
        ReferenceTypeImpl referenceType = new ReferenceTypeImpl();
        return referenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RefLocationType createRefLocationType() {
        RefLocationTypeImpl refLocationType = new RefLocationTypeImpl();
        return refLocationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelatedTimeType createRelatedTimeType() {
        RelatedTimeTypeImpl relatedTimeType = new RelatedTimeTypeImpl();
        return relatedTimeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelativeInternalPositionalAccuracyType createRelativeInternalPositionalAccuracyType() {
        RelativeInternalPositionalAccuracyTypeImpl relativeInternalPositionalAccuracyType = new RelativeInternalPositionalAccuracyTypeImpl();
        return relativeInternalPositionalAccuracyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RingPropertyType createRingPropertyType() {
        RingPropertyTypeImpl ringPropertyType = new RingPropertyTypeImpl();
        return ringPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RingType createRingType() {
        RingTypeImpl ringType = new RingTypeImpl();
        return ringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RowType createRowType() {
        RowTypeImpl rowType = new RowTypeImpl();
        return rowType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScalarValuePropertyType createScalarValuePropertyType() {
        ScalarValuePropertyTypeImpl scalarValuePropertyType = new ScalarValuePropertyTypeImpl();
        return scalarValuePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleType createScaleType() {
        ScaleTypeImpl scaleType = new ScaleTypeImpl();
        return scaleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SecondDefiningParameterType createSecondDefiningParameterType() {
        SecondDefiningParameterTypeImpl secondDefiningParameterType = new SecondDefiningParameterTypeImpl();
        return secondDefiningParameterType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SequenceRuleType createSequenceRuleType() {
        SequenceRuleTypeImpl sequenceRuleType = new SequenceRuleTypeImpl();
        return sequenceRuleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SingleOperationRefType createSingleOperationRefType() {
        SingleOperationRefTypeImpl singleOperationRefType = new SingleOperationRefTypeImpl();
        return singleOperationRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SolidArrayPropertyType createSolidArrayPropertyType() {
        SolidArrayPropertyTypeImpl solidArrayPropertyType = new SolidArrayPropertyTypeImpl();
        return solidArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SolidPropertyType createSolidPropertyType() {
        SolidPropertyTypeImpl solidPropertyType = new SolidPropertyTypeImpl();
        return solidPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SolidType createSolidType() {
        SolidTypeImpl solidType = new SolidTypeImpl();
        return solidType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SpeedType createSpeedType() {
        SpeedTypeImpl speedType = new SpeedTypeImpl();
        return speedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SphereType createSphereType() {
        SphereTypeImpl sphereType = new SphereTypeImpl();
        return sphereType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SphericalCSRefType createSphericalCSRefType() {
        SphericalCSRefTypeImpl sphericalCSRefType = new SphericalCSRefTypeImpl();
        return sphericalCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SphericalCSType createSphericalCSType() {
        SphericalCSTypeImpl sphericalCSType = new SphericalCSTypeImpl();
        return sphericalCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StringOrRefType createStringOrRefType() {
        StringOrRefTypeImpl stringOrRefType = new StringOrRefTypeImpl();
        return stringOrRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StyleType createStyleType() {
        StyleTypeImpl styleType = new StyleTypeImpl();
        return styleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StyleVariationType createStyleVariationType() {
        StyleVariationTypeImpl styleVariationType = new StyleVariationTypeImpl();
        return styleVariationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfaceArrayPropertyType createSurfaceArrayPropertyType() {
        SurfaceArrayPropertyTypeImpl surfaceArrayPropertyType = new SurfaceArrayPropertyTypeImpl();
        return surfaceArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfacePatchArrayPropertyType createSurfacePatchArrayPropertyType() {
        SurfacePatchArrayPropertyTypeImpl surfacePatchArrayPropertyType = new SurfacePatchArrayPropertyTypeImpl();
        return surfacePatchArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfacePropertyType createSurfacePropertyType() {
        SurfacePropertyTypeImpl surfacePropertyType = new SurfacePropertyTypeImpl();
        return surfacePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfaceType createSurfaceType() {
        SurfaceTypeImpl surfaceType = new SurfaceTypeImpl();
        return surfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SymbolType createSymbolType() {
        SymbolTypeImpl symbolType = new SymbolTypeImpl();
        return symbolType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TargetPropertyType createTargetPropertyType() {
        TargetPropertyTypeImpl targetPropertyType = new TargetPropertyTypeImpl();
        return targetPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TemporalCRSRefType createTemporalCRSRefType() {
        TemporalCRSRefTypeImpl temporalCRSRefType = new TemporalCRSRefTypeImpl();
        return temporalCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TemporalCRSType createTemporalCRSType() {
        TemporalCRSTypeImpl temporalCRSType = new TemporalCRSTypeImpl();
        return temporalCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TemporalCSRefType createTemporalCSRefType() {
        TemporalCSRefTypeImpl temporalCSRefType = new TemporalCSRefTypeImpl();
        return temporalCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TemporalCSType createTemporalCSType() {
        TemporalCSTypeImpl temporalCSType = new TemporalCSTypeImpl();
        return temporalCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TemporalDatumRefType createTemporalDatumRefType() {
        TemporalDatumRefTypeImpl temporalDatumRefType = new TemporalDatumRefTypeImpl();
        return temporalDatumRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TemporalDatumType createTemporalDatumType() {
        TemporalDatumTypeImpl temporalDatumType = new TemporalDatumTypeImpl();
        return temporalDatumType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeCalendarEraPropertyType createTimeCalendarEraPropertyType() {
        TimeCalendarEraPropertyTypeImpl timeCalendarEraPropertyType = new TimeCalendarEraPropertyTypeImpl();
        return timeCalendarEraPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeCalendarEraType createTimeCalendarEraType() {
        TimeCalendarEraTypeImpl timeCalendarEraType = new TimeCalendarEraTypeImpl();
        return timeCalendarEraType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeCalendarPropertyType createTimeCalendarPropertyType() {
        TimeCalendarPropertyTypeImpl timeCalendarPropertyType = new TimeCalendarPropertyTypeImpl();
        return timeCalendarPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeCalendarType createTimeCalendarType() {
        TimeCalendarTypeImpl timeCalendarType = new TimeCalendarTypeImpl();
        return timeCalendarType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeClockPropertyType createTimeClockPropertyType() {
        TimeClockPropertyTypeImpl timeClockPropertyType = new TimeClockPropertyTypeImpl();
        return timeClockPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeClockType createTimeClockType() {
        TimeClockTypeImpl timeClockType = new TimeClockTypeImpl();
        return timeClockType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeCoordinateSystemType createTimeCoordinateSystemType() {
        TimeCoordinateSystemTypeImpl timeCoordinateSystemType = new TimeCoordinateSystemTypeImpl();
        return timeCoordinateSystemType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeEdgePropertyType createTimeEdgePropertyType() {
        TimeEdgePropertyTypeImpl timeEdgePropertyType = new TimeEdgePropertyTypeImpl();
        return timeEdgePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeEdgeType createTimeEdgeType() {
        TimeEdgeTypeImpl timeEdgeType = new TimeEdgeTypeImpl();
        return timeEdgeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeGeometricPrimitivePropertyType createTimeGeometricPrimitivePropertyType() {
        TimeGeometricPrimitivePropertyTypeImpl timeGeometricPrimitivePropertyType = new TimeGeometricPrimitivePropertyTypeImpl();
        return timeGeometricPrimitivePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeInstantPropertyType createTimeInstantPropertyType() {
        TimeInstantPropertyTypeImpl timeInstantPropertyType = new TimeInstantPropertyTypeImpl();
        return timeInstantPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeInstantType createTimeInstantType() {
        TimeInstantTypeImpl timeInstantType = new TimeInstantTypeImpl();
        return timeInstantType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeIntervalLengthType createTimeIntervalLengthType() {
        TimeIntervalLengthTypeImpl timeIntervalLengthType = new TimeIntervalLengthTypeImpl();
        return timeIntervalLengthType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeNodePropertyType createTimeNodePropertyType() {
        TimeNodePropertyTypeImpl timeNodePropertyType = new TimeNodePropertyTypeImpl();
        return timeNodePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeNodeType createTimeNodeType() {
        TimeNodeTypeImpl timeNodeType = new TimeNodeTypeImpl();
        return timeNodeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeOrdinalEraPropertyType createTimeOrdinalEraPropertyType() {
        TimeOrdinalEraPropertyTypeImpl timeOrdinalEraPropertyType = new TimeOrdinalEraPropertyTypeImpl();
        return timeOrdinalEraPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeOrdinalEraType createTimeOrdinalEraType() {
        TimeOrdinalEraTypeImpl timeOrdinalEraType = new TimeOrdinalEraTypeImpl();
        return timeOrdinalEraType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeOrdinalReferenceSystemType createTimeOrdinalReferenceSystemType() {
        TimeOrdinalReferenceSystemTypeImpl timeOrdinalReferenceSystemType = new TimeOrdinalReferenceSystemTypeImpl();
        return timeOrdinalReferenceSystemType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePeriodPropertyType createTimePeriodPropertyType() {
        TimePeriodPropertyTypeImpl timePeriodPropertyType = new TimePeriodPropertyTypeImpl();
        return timePeriodPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePeriodType createTimePeriodType() {
        TimePeriodTypeImpl timePeriodType = new TimePeriodTypeImpl();
        return timePeriodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePositionType createTimePositionType() {
        TimePositionTypeImpl timePositionType = new TimePositionTypeImpl();
        return timePositionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePrimitivePropertyType createTimePrimitivePropertyType() {
        TimePrimitivePropertyTypeImpl timePrimitivePropertyType = new TimePrimitivePropertyTypeImpl();
        return timePrimitivePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeTopologyComplexPropertyType createTimeTopologyComplexPropertyType() {
        TimeTopologyComplexPropertyTypeImpl timeTopologyComplexPropertyType = new TimeTopologyComplexPropertyTypeImpl();
        return timeTopologyComplexPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeTopologyComplexType createTimeTopologyComplexType() {
        TimeTopologyComplexTypeImpl timeTopologyComplexType = new TimeTopologyComplexTypeImpl();
        return timeTopologyComplexType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeTopologyPrimitivePropertyType createTimeTopologyPrimitivePropertyType() {
        TimeTopologyPrimitivePropertyTypeImpl timeTopologyPrimitivePropertyType = new TimeTopologyPrimitivePropertyTypeImpl();
        return timeTopologyPrimitivePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeType createTimeType() {
        TimeTypeImpl timeType = new TimeTypeImpl();
        return timeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TinType createTinType() {
        TinTypeImpl tinType = new TinTypeImpl();
        return tinType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoComplexMemberType createTopoComplexMemberType() {
        TopoComplexMemberTypeImpl topoComplexMemberType = new TopoComplexMemberTypeImpl();
        return topoComplexMemberType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoComplexType createTopoComplexType() {
        TopoComplexTypeImpl topoComplexType = new TopoComplexTypeImpl();
        return topoComplexType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoCurvePropertyType createTopoCurvePropertyType() {
        TopoCurvePropertyTypeImpl topoCurvePropertyType = new TopoCurvePropertyTypeImpl();
        return topoCurvePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoCurveType createTopoCurveType() {
        TopoCurveTypeImpl topoCurveType = new TopoCurveTypeImpl();
        return topoCurveType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopologyStylePropertyType createTopologyStylePropertyType() {
        TopologyStylePropertyTypeImpl topologyStylePropertyType = new TopologyStylePropertyTypeImpl();
        return topologyStylePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopologyStyleType createTopologyStyleType() {
        TopologyStyleTypeImpl topologyStyleType = new TopologyStyleTypeImpl();
        return topologyStyleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoPointPropertyType createTopoPointPropertyType() {
        TopoPointPropertyTypeImpl topoPointPropertyType = new TopoPointPropertyTypeImpl();
        return topoPointPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoPointType createTopoPointType() {
        TopoPointTypeImpl topoPointType = new TopoPointTypeImpl();
        return topoPointType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoPrimitiveArrayAssociationType createTopoPrimitiveArrayAssociationType() {
        TopoPrimitiveArrayAssociationTypeImpl topoPrimitiveArrayAssociationType = new TopoPrimitiveArrayAssociationTypeImpl();
        return topoPrimitiveArrayAssociationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoPrimitiveMemberType createTopoPrimitiveMemberType() {
        TopoPrimitiveMemberTypeImpl topoPrimitiveMemberType = new TopoPrimitiveMemberTypeImpl();
        return topoPrimitiveMemberType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoSolidType createTopoSolidType() {
        TopoSolidTypeImpl topoSolidType = new TopoSolidTypeImpl();
        return topoSolidType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoSurfacePropertyType createTopoSurfacePropertyType() {
        TopoSurfacePropertyTypeImpl topoSurfacePropertyType = new TopoSurfacePropertyTypeImpl();
        return topoSurfacePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoSurfaceType createTopoSurfaceType() {
        TopoSurfaceTypeImpl topoSurfaceType = new TopoSurfaceTypeImpl();
        return topoSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoVolumePropertyType createTopoVolumePropertyType() {
        TopoVolumePropertyTypeImpl topoVolumePropertyType = new TopoVolumePropertyTypeImpl();
        return topoVolumePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TopoVolumeType createTopoVolumeType() {
        TopoVolumeTypeImpl topoVolumeType = new TopoVolumeTypeImpl();
        return topoVolumeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TrackType createTrackType() {
        TrackTypeImpl trackType = new TrackTypeImpl();
        return trackType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TransformationRefType createTransformationRefType() {
        TransformationRefTypeImpl transformationRefType = new TransformationRefTypeImpl();
        return transformationRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TransformationType createTransformationType() {
        TransformationTypeImpl transformationType = new TransformationTypeImpl();
        return transformationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TrianglePatchArrayPropertyType createTrianglePatchArrayPropertyType() {
        TrianglePatchArrayPropertyTypeImpl trianglePatchArrayPropertyType = new TrianglePatchArrayPropertyTypeImpl();
        return trianglePatchArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TriangleType createTriangleType() {
        TriangleTypeImpl triangleType = new TriangleTypeImpl();
        return triangleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TriangulatedSurfaceType createTriangulatedSurfaceType() {
        TriangulatedSurfaceTypeImpl triangulatedSurfaceType = new TriangulatedSurfaceTypeImpl();
        return triangulatedSurfaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UnitDefinitionType createUnitDefinitionType() {
        UnitDefinitionTypeImpl unitDefinitionType = new UnitDefinitionTypeImpl();
        return unitDefinitionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UnitOfMeasureType createUnitOfMeasureType() {
        UnitOfMeasureTypeImpl unitOfMeasureType = new UnitOfMeasureTypeImpl();
        return unitOfMeasureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UserDefinedCSRefType createUserDefinedCSRefType() {
        UserDefinedCSRefTypeImpl userDefinedCSRefType = new UserDefinedCSRefTypeImpl();
        return userDefinedCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UserDefinedCSType createUserDefinedCSType() {
        UserDefinedCSTypeImpl userDefinedCSType = new UserDefinedCSTypeImpl();
        return userDefinedCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueArrayPropertyType createValueArrayPropertyType() {
        ValueArrayPropertyTypeImpl valueArrayPropertyType = new ValueArrayPropertyTypeImpl();
        return valueArrayPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueArrayType createValueArrayType() {
        ValueArrayTypeImpl valueArrayType = new ValueArrayTypeImpl();
        return valueArrayType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValuePropertyType createValuePropertyType() {
        ValuePropertyTypeImpl valuePropertyType = new ValuePropertyTypeImpl();
        return valuePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VectorType createVectorType() {
        VectorTypeImpl vectorType = new VectorTypeImpl();
        return vectorType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalCRSRefType createVerticalCRSRefType() {
        VerticalCRSRefTypeImpl verticalCRSRefType = new VerticalCRSRefTypeImpl();
        return verticalCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalCRSType createVerticalCRSType() {
        VerticalCRSTypeImpl verticalCRSType = new VerticalCRSTypeImpl();
        return verticalCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalCSRefType createVerticalCSRefType() {
        VerticalCSRefTypeImpl verticalCSRefType = new VerticalCSRefTypeImpl();
        return verticalCSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalCSType createVerticalCSType() {
        VerticalCSTypeImpl verticalCSType = new VerticalCSTypeImpl();
        return verticalCSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalDatumRefType createVerticalDatumRefType() {
        VerticalDatumRefTypeImpl verticalDatumRefType = new VerticalDatumRefTypeImpl();
        return verticalDatumRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalDatumType createVerticalDatumType() {
        VerticalDatumTypeImpl verticalDatumType = new VerticalDatumTypeImpl();
        return verticalDatumType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VerticalDatumTypeType createVerticalDatumTypeType() {
        VerticalDatumTypeTypeImpl verticalDatumTypeType = new VerticalDatumTypeTypeImpl();
        return verticalDatumTypeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VolumeType createVolumeType() {
        VolumeTypeImpl volumeType = new VolumeTypeImpl();
        return volumeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AesheticCriteriaType createAesheticCriteriaTypeFromString(EDataType eDataType, String initialValue) {
        AesheticCriteriaType result = AesheticCriteriaType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAesheticCriteriaTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompassPointEnumeration createCompassPointEnumerationFromString(EDataType eDataType, String initialValue) {
        CompassPointEnumeration result = CompassPointEnumeration.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCompassPointEnumerationToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurveInterpolationType createCurveInterpolationTypeFromString(EDataType eDataType, String initialValue) {
        CurveInterpolationType result = CurveInterpolationType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCurveInterpolationTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectionTypeMember0 createDirectionTypeMember0FromString(EDataType eDataType, String initialValue) {
        DirectionTypeMember0 result = DirectionTypeMember0.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDirectionTypeMember0ToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DrawingTypeType createDrawingTypeTypeFromString(EDataType eDataType, String initialValue) {
        DrawingTypeType result = DrawingTypeType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDrawingTypeTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FileValueModelType createFileValueModelTypeFromString(EDataType eDataType, String initialValue) {
        FileValueModelType result = FileValueModelType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFileValueModelTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GraphTypeType createGraphTypeTypeFromString(EDataType eDataType, String initialValue) {
        GraphTypeType result = GraphTypeType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertGraphTypeTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IncrementOrder createIncrementOrderFromString(EDataType eDataType, String initialValue) {
        IncrementOrder result = IncrementOrder.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIncrementOrderToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IsSphereType createIsSphereTypeFromString(EDataType eDataType, String initialValue) {
        IsSphereType result = IsSphereType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIsSphereTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public KnotTypesType createKnotTypesTypeFromString(EDataType eDataType, String initialValue) {
        KnotTypesType result = KnotTypesType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertKnotTypesTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LineTypeType createLineTypeTypeFromString(EDataType eDataType, String initialValue) {
        LineTypeType result = LineTypeType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertLineTypeTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NullEnumerationMember0 createNullEnumerationMember0FromString(EDataType eDataType, String initialValue) {
        NullEnumerationMember0 result = NullEnumerationMember0.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNullEnumerationMember0ToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryGrammarEnumeration createQueryGrammarEnumerationFromString(EDataType eDataType, String initialValue) {
        QueryGrammarEnumeration result = QueryGrammarEnumeration.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertQueryGrammarEnumerationToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelativePositionType createRelativePositionTypeFromString(EDataType eDataType, String initialValue) {
        RelativePositionType result = RelativePositionType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRelativePositionTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SequenceRuleNames createSequenceRuleNamesFromString(EDataType eDataType, String initialValue) {
        SequenceRuleNames result = SequenceRuleNames.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSequenceRuleNamesToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SignType createSignTypeFromString(EDataType eDataType, String initialValue) {
        SignType result = SignType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSignTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SuccessionType createSuccessionTypeFromString(EDataType eDataType, String initialValue) {
        SuccessionType result = SuccessionType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSuccessionTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfaceInterpolationType createSurfaceInterpolationTypeFromString(EDataType eDataType, String initialValue) {
        SurfaceInterpolationType result = SurfaceInterpolationType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSurfaceInterpolationTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SymbolTypeEnumeration createSymbolTypeEnumerationFromString(EDataType eDataType, String initialValue) {
        SymbolTypeEnumeration result = SymbolTypeEnumeration.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSymbolTypeEnumerationToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeIndeterminateValueType createTimeIndeterminateValueTypeFromString(EDataType eDataType, String initialValue) {
        TimeIndeterminateValueType result = TimeIndeterminateValueType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTimeIndeterminateValueTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeUnitTypeMember0 createTimeUnitTypeMember0FromString(EDataType eDataType, String initialValue) {
        TimeUnitTypeMember0 result = TimeUnitTypeMember0.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTimeUnitTypeMember0ToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AesheticCriteriaType createAesheticCriteriaTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createAesheticCriteriaTypeFromString(Gml311Package.eINSTANCE.getAesheticCriteriaType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAesheticCriteriaTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertAesheticCriteriaTypeToString(Gml311Package.eINSTANCE.getAesheticCriteriaType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger createArcMinutesTypeFromString(EDataType eDataType, String initialValue) {
        return (BigInteger)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.NON_NEGATIVE_INTEGER, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertArcMinutesTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.NON_NEGATIVE_INTEGER, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigDecimal createArcSecondsTypeFromString(EDataType eDataType, String initialValue) {
        return (BigDecimal)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DECIMAL, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertArcSecondsTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DECIMAL, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Boolean> createBooleanListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<Boolean> result = new ArrayList<Boolean>();
        for (String item : split(initialValue)) {
            result.add((Boolean)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.BOOLEAN, item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertBooleanListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.BOOLEAN, item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createBooleanOrNullFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createNullEnumerationFromString(Gml311Package.eINSTANCE.getNullEnumeration(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.BOOLEAN, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_URI, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertBooleanOrNullToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(instanceValue)) {
            try {
                String value = convertNullEnumerationToString(Gml311Package.eINSTANCE.getNullEnumeration(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.BOOLEAN.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.BOOLEAN, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.ANY_URI.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_URI, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> createBooleanOrNullListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<Object> result = new ArrayList<Object>();
        for (String item : split(initialValue)) {
            result.add(createBooleanOrNullFromString(Gml311Package.eINSTANCE.getBooleanOrNull(), item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertBooleanOrNullListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(convertBooleanOrNullToString(Gml311Package.eINSTANCE.getBooleanOrNull(), item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XMLGregorianCalendar createCalDateFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        XMLGregorianCalendar result = null;
        RuntimeException exception = null;
        try {
            result = (XMLGregorianCalendar)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DATE, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = (XMLGregorianCalendar)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.GYEAR_MONTH, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = (XMLGregorianCalendar)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.GYEAR, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCalDateToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (XMLTypePackage.Literals.DATE.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DATE, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.GYEAR_MONTH.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.GYEAR_MONTH, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.GYEAR.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.GYEAR, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CompassPointEnumeration createCompassPointEnumerationObjectFromString(EDataType eDataType, String initialValue) {
        return createCompassPointEnumerationFromString(Gml311Package.eINSTANCE.getCompassPointEnumeration(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCompassPointEnumerationObjectToString(EDataType eDataType, Object instanceValue) {
        return convertCompassPointEnumerationToString(Gml311Package.eINSTANCE.getCompassPointEnumeration(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> createCountExtentTypeFromString(EDataType eDataType, String initialValue) {
        return createIntegerOrNullListFromString(Gml311Package.eINSTANCE.getIntegerOrNullList(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCountExtentTypeToString(EDataType eDataType, Object instanceValue) {
        return convertIntegerOrNullListToString(Gml311Package.eINSTANCE.getIntegerOrNullList(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurveInterpolationType createCurveInterpolationTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createCurveInterpolationTypeFromString(Gml311Package.eINSTANCE.getCurveInterpolationType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCurveInterpolationTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertCurveInterpolationTypeToString(Gml311Package.eINSTANCE.getCurveInterpolationType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigDecimal createDecimalMinutesTypeFromString(EDataType eDataType, String initialValue) {
        return (BigDecimal)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DECIMAL, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDecimalMinutesTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DECIMAL, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger createDegreeValueTypeFromString(EDataType eDataType, String initialValue) {
        return (BigInteger)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.NON_NEGATIVE_INTEGER, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDegreeValueTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.NON_NEGATIVE_INTEGER, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Enumerator createDirectionTypeFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Enumerator result = null;
        RuntimeException exception = null;
        try {
            result = (Enumerator)createDirectionTypeMember0FromString(Gml311Package.eINSTANCE.getDirectionTypeMember0(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = (Enumerator)createDirectionTypeMember1FromString(Gml311Package.eINSTANCE.getDirectionTypeMember1(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDirectionTypeToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getDirectionTypeMember0().isInstance(instanceValue)) {
            try {
                String value = convertDirectionTypeMember0ToString(Gml311Package.eINSTANCE.getDirectionTypeMember0(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (Gml311Package.eINSTANCE.getDirectionTypeMember1().isInstance(instanceValue)) {
            try {
                String value = convertDirectionTypeMember1ToString(Gml311Package.eINSTANCE.getDirectionTypeMember1(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectionTypeMember0 createDirectionTypeMember0ObjectFromString(EDataType eDataType, String initialValue) {
        return createDirectionTypeMember0FromString(Gml311Package.eINSTANCE.getDirectionTypeMember0(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDirectionTypeMember0ObjectToString(EDataType eDataType, Object instanceValue) {
        return convertDirectionTypeMember0ToString(Gml311Package.eINSTANCE.getDirectionTypeMember0(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SignType createDirectionTypeMember1FromString(EDataType eDataType, String initialValue) {
        return createSignTypeFromString(Gml311Package.eINSTANCE.getSignType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDirectionTypeMember1ToString(EDataType eDataType, Object instanceValue) {
        return convertSignTypeToString(Gml311Package.eINSTANCE.getSignType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Double> createDoubleListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<Double> result = new ArrayList<Double>();
        for (String item : split(initialValue)) {
            result.add((Double)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DOUBLE, item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDoubleListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DOUBLE, item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createDoubleOrNullFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createNullEnumerationFromString(Gml311Package.eINSTANCE.getNullEnumeration(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DOUBLE, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_URI, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDoubleOrNullToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(instanceValue)) {
            try {
                String value = convertNullEnumerationToString(Gml311Package.eINSTANCE.getNullEnumeration(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.DOUBLE.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DOUBLE, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.ANY_URI.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_URI, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> createDoubleOrNullListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<Object> result = new ArrayList<Object>();
        for (String item : split(initialValue)) {
            result.add(createDoubleOrNullFromString(Gml311Package.eINSTANCE.getDoubleOrNull(), item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDoubleOrNullListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(convertDoubleOrNullToString(Gml311Package.eINSTANCE.getDoubleOrNull(), item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DrawingTypeType createDrawingTypeTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createDrawingTypeTypeFromString(Gml311Package.eINSTANCE.getDrawingTypeType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDrawingTypeTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertDrawingTypeTypeToString(Gml311Package.eINSTANCE.getDrawingTypeType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FileValueModelType createFileValueModelTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createFileValueModelTypeFromString(Gml311Package.eINSTANCE.getFileValueModelType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFileValueModelTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertFileValueModelTypeToString(Gml311Package.eINSTANCE.getFileValueModelType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GraphTypeType createGraphTypeTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createGraphTypeTypeFromString(Gml311Package.eINSTANCE.getGraphTypeType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertGraphTypeTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertGraphTypeTypeToString(Gml311Package.eINSTANCE.getGraphTypeType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IncrementOrder createIncrementOrderObjectFromString(EDataType eDataType, String initialValue) {
        return createIncrementOrderFromString(Gml311Package.eINSTANCE.getIncrementOrder(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIncrementOrderObjectToString(EDataType eDataType, Object instanceValue) {
        return convertIncrementOrderToString(Gml311Package.eINSTANCE.getIncrementOrder(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<BigInteger> createIntegerListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<BigInteger> result = new ArrayList<BigInteger>();
        for (String item : split(initialValue)) {
            result.add((BigInteger)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INTEGER, item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIntegerListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INTEGER, item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createIntegerOrNullFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createNullEnumerationFromString(Gml311Package.eINSTANCE.getNullEnumeration(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INTEGER, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_URI, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIntegerOrNullToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(instanceValue)) {
            try {
                String value = convertNullEnumerationToString(Gml311Package.eINSTANCE.getNullEnumeration(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.INTEGER.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INTEGER, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.ANY_URI.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_URI, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> createIntegerOrNullListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<Object> result = new ArrayList<Object>();
        for (String item : split(initialValue)) {
            result.add(createIntegerOrNullFromString(Gml311Package.eINSTANCE.getIntegerOrNull(), item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIntegerOrNullListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(convertIntegerOrNullToString(Gml311Package.eINSTANCE.getIntegerOrNull(), item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IsSphereType createIsSphereTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createIsSphereTypeFromString(Gml311Package.eINSTANCE.getIsSphereType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIsSphereTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertIsSphereTypeToString(Gml311Package.eINSTANCE.getIsSphereType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public KnotTypesType createKnotTypesTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createKnotTypesTypeFromString(Gml311Package.eINSTANCE.getKnotTypesType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertKnotTypesTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertKnotTypesTypeToString(Gml311Package.eINSTANCE.getKnotTypesType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LineTypeType createLineTypeTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createLineTypeTypeFromString(Gml311Package.eINSTANCE.getLineTypeType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertLineTypeTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertLineTypeTypeToString(Gml311Package.eINSTANCE.getLineTypeType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<String> createNameListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<String> result = new ArrayList<String>();
        for (String item : split(initialValue)) {
            result.add((String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.NAME, item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNameListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.NAME, item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createNameOrNullFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createNullEnumerationFromString(Gml311Package.eINSTANCE.getNullEnumeration(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.NAME, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_URI, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNameOrNullToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(instanceValue)) {
            try {
                String value = convertNullEnumerationToString(Gml311Package.eINSTANCE.getNullEnumeration(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.NAME.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.NAME, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.ANY_URI.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_URI, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> createNameOrNullListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<Object> result = new ArrayList<Object>();
        for (String item : split(initialValue)) {
            result.add(createNameOrNullFromString(Gml311Package.eINSTANCE.getNameOrNull(), item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNameOrNullListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(convertNameOrNullToString(Gml311Package.eINSTANCE.getNameOrNull(), item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<String> createNCNameListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<String> result = new ArrayList<String>();
        for (String item : split(initialValue)) {
            result.add((String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.NC_NAME, item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNCNameListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.NC_NAME, item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createNullEnumerationFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createNullEnumerationMember0FromString(Gml311Package.eINSTANCE.getNullEnumerationMember0(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = createNullEnumerationMember1FromString(Gml311Package.eINSTANCE.getNullEnumerationMember1(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNullEnumerationToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getNullEnumerationMember0().isInstance(instanceValue)) {
            try {
                String value = convertNullEnumerationMember0ToString(Gml311Package.eINSTANCE.getNullEnumerationMember0(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (Gml311Package.eINSTANCE.getNullEnumerationMember1().isInstance(instanceValue)) {
            try {
                String value = convertNullEnumerationMember1ToString(Gml311Package.eINSTANCE.getNullEnumerationMember1(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NullEnumerationMember0 createNullEnumerationMember0ObjectFromString(EDataType eDataType, String initialValue) {
        return createNullEnumerationMember0FromString(Gml311Package.eINSTANCE.getNullEnumerationMember0(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNullEnumerationMember0ObjectToString(EDataType eDataType, Object instanceValue) {
        return convertNullEnumerationMember0ToString(Gml311Package.eINSTANCE.getNullEnumerationMember0(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createNullEnumerationMember1FromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNullEnumerationMember1ToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createNullTypeFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createNullEnumerationFromString(Gml311Package.eINSTANCE.getNullEnumeration(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_URI, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNullTypeToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(instanceValue)) {
            try {
                String value = convertNullEnumerationToString(Gml311Package.eINSTANCE.getNullEnumeration(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.ANY_URI.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_URI, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Object> createQNameListFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<Object> result = new ArrayList<Object>();
        for (String item : split(initialValue)) {
            result.add(XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.QNAME, item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertQNameListToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.QNAME, item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryGrammarEnumeration createQueryGrammarEnumerationObjectFromString(EDataType eDataType, String initialValue) {
        return createQueryGrammarEnumerationFromString(Gml311Package.eINSTANCE.getQueryGrammarEnumeration(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertQueryGrammarEnumerationObjectToString(EDataType eDataType, Object instanceValue) {
        return convertQueryGrammarEnumerationToString(Gml311Package.eINSTANCE.getQueryGrammarEnumeration(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RelativePositionType createRelativePositionTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createRelativePositionTypeFromString(Gml311Package.eINSTANCE.getRelativePositionType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRelativePositionTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertRelativePositionTypeToString(Gml311Package.eINSTANCE.getRelativePositionType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SequenceRuleNames createSequenceRuleNamesObjectFromString(EDataType eDataType, String initialValue) {
        return createSequenceRuleNamesFromString(Gml311Package.eINSTANCE.getSequenceRuleNames(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSequenceRuleNamesObjectToString(EDataType eDataType, Object instanceValue) {
        return convertSequenceRuleNamesToString(Gml311Package.eINSTANCE.getSequenceRuleNames(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SignType createSignTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createSignTypeFromString(Gml311Package.eINSTANCE.getSignType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSignTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertSignTypeToString(Gml311Package.eINSTANCE.getSignType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createStringOrNullFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createNullEnumerationFromString(Gml311Package.eINSTANCE.getNullEnumeration(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_URI, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStringOrNullToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(instanceValue)) {
            try {
                String value = convertNullEnumerationToString(Gml311Package.eINSTANCE.getNullEnumeration(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.STRING.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.ANY_URI.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_URI, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SuccessionType createSuccessionTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createSuccessionTypeFromString(Gml311Package.eINSTANCE.getSuccessionType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSuccessionTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertSuccessionTypeToString(Gml311Package.eINSTANCE.getSuccessionType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfaceInterpolationType createSurfaceInterpolationTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createSurfaceInterpolationTypeFromString(Gml311Package.eINSTANCE.getSurfaceInterpolationType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSurfaceInterpolationTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertSurfaceInterpolationTypeToString(Gml311Package.eINSTANCE.getSurfaceInterpolationType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SymbolTypeEnumeration createSymbolTypeEnumerationObjectFromString(EDataType eDataType, String initialValue) {
        return createSymbolTypeEnumerationFromString(Gml311Package.eINSTANCE.getSymbolTypeEnumeration(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSymbolTypeEnumerationObjectToString(EDataType eDataType, Object instanceValue) {
        return convertSymbolTypeEnumerationToString(Gml311Package.eINSTANCE.getSymbolTypeEnumeration(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeIndeterminateValueType createTimeIndeterminateValueTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createTimeIndeterminateValueTypeFromString(Gml311Package.eINSTANCE.getTimeIndeterminateValueType(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTimeIndeterminateValueTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertTimeIndeterminateValueTypeToString(Gml311Package.eINSTANCE.getTimeIndeterminateValueType(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createTimePositionUnionFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createCalDateFromString(Gml311Package.eINSTANCE.getCalDate(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.TIME, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DATE_TIME, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_URI, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DECIMAL, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTimePositionUnionToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getCalDate().isInstance(instanceValue)) {
            try {
                String value = convertCalDateToString(Gml311Package.eINSTANCE.getCalDate(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.TIME.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.TIME, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.DATE_TIME.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DATE_TIME, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.ANY_URI.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_URI, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.DECIMAL.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DECIMAL, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createTimeUnitTypeFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createTimeUnitTypeMember0FromString(Gml311Package.eINSTANCE.getTimeUnitTypeMember0(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = createTimeUnitTypeMember1FromString(Gml311Package.eINSTANCE.getTimeUnitTypeMember1(), initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTimeUnitTypeToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Gml311Package.eINSTANCE.getTimeUnitTypeMember0().isInstance(instanceValue)) {
            try {
                String value = convertTimeUnitTypeMember0ToString(Gml311Package.eINSTANCE.getTimeUnitTypeMember0(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (Gml311Package.eINSTANCE.getTimeUnitTypeMember1().isInstance(instanceValue)) {
            try {
                String value = convertTimeUnitTypeMember1ToString(Gml311Package.eINSTANCE.getTimeUnitTypeMember1(), instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeUnitTypeMember0 createTimeUnitTypeMember0ObjectFromString(EDataType eDataType, String initialValue) {
        return createTimeUnitTypeMember0FromString(Gml311Package.eINSTANCE.getTimeUnitTypeMember0(), initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTimeUnitTypeMember0ObjectToString(EDataType eDataType, Object instanceValue) {
        return convertTimeUnitTypeMember0ToString(Gml311Package.eINSTANCE.getTimeUnitTypeMember0(), instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createTimeUnitTypeMember1FromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTimeUnitTypeMember1ToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Gml311Package getGml311Package() {
        return (Gml311Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static Gml311Package getPackage() {
        return Gml311Package.eINSTANCE;
    }

} //Gml311FactoryImpl
