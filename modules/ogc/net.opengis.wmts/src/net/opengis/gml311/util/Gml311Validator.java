/**
 */
package net.opengis.gml311.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.gml311.*;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.gml311.Gml311Package
 * @generated
 */
public class Gml311Validator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final Gml311Validator INSTANCE = new Gml311Validator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "net.opengis.gml311";

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

    /**
     * The cached base package validator.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XMLTypeValidator xmlTypeValidator;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Gml311Validator() {
        super();
        xmlTypeValidator = XMLTypeValidator.INSTANCE;
    }

    /**
     * Returns the package of this validator switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EPackage getEPackage() {
      return Gml311Package.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresponding classifier of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
        switch (classifierID) {
            case Gml311Package.ABSOLUTE_EXTERNAL_POSITIONAL_ACCURACY_TYPE:
                return validateAbsoluteExternalPositionalAccuracyType((AbsoluteExternalPositionalAccuracyType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_CONTINUOUS_COVERAGE_TYPE:
                return validateAbstractContinuousCoverageType((AbstractContinuousCoverageType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE:
                return validateAbstractCoordinateOperationBaseType((AbstractCoordinateOperationBaseType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE:
                return validateAbstractCoordinateOperationType((AbstractCoordinateOperationType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE:
                return validateAbstractCoordinateSystemBaseType((AbstractCoordinateSystemBaseType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_TYPE:
                return validateAbstractCoordinateSystemType((AbstractCoordinateSystemType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_COVERAGE_TYPE:
                return validateAbstractCoverageType((AbstractCoverageType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE:
                return validateAbstractCurveSegmentType((AbstractCurveSegmentType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_CURVE_TYPE:
                return validateAbstractCurveType((AbstractCurveType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_DATUM_BASE_TYPE:
                return validateAbstractDatumBaseType((AbstractDatumBaseType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_DATUM_TYPE:
                return validateAbstractDatumType((AbstractDatumType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_DISCRETE_COVERAGE_TYPE:
                return validateAbstractDiscreteCoverageType((AbstractDiscreteCoverageType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE:
                return validateAbstractFeatureCollectionType((AbstractFeatureCollectionType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_FEATURE_TYPE:
                return validateAbstractFeatureType((AbstractFeatureType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GENERAL_CONVERSION_TYPE:
                return validateAbstractGeneralConversionType((AbstractGeneralConversionType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE:
                return validateAbstractGeneralDerivedCRSType((AbstractGeneralDerivedCRSType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_REF_TYPE:
                return validateAbstractGeneralOperationParameterRefType((AbstractGeneralOperationParameterRefType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_TYPE:
                return validateAbstractGeneralOperationParameterType((AbstractGeneralOperationParameterType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GENERAL_PARAMETER_VALUE_TYPE:
                return validateAbstractGeneralParameterValueType((AbstractGeneralParameterValueType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GENERAL_TRANSFORMATION_TYPE:
                return validateAbstractGeneralTransformationType((AbstractGeneralTransformationType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GEOMETRIC_AGGREGATE_TYPE:
                return validateAbstractGeometricAggregateType((AbstractGeometricAggregateType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE:
                return validateAbstractGeometricPrimitiveType((AbstractGeometricPrimitiveType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GEOMETRY_TYPE:
                return validateAbstractGeometryType((AbstractGeometryType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GML_TYPE:
                return validateAbstractGMLType((AbstractGMLType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE:
                return validateAbstractGriddedSurfaceType((AbstractGriddedSurfaceType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_META_DATA_TYPE:
                return validateAbstractMetaDataType((AbstractMetaDataType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_PARAMETRIC_CURVE_SURFACE_TYPE:
                return validateAbstractParametricCurveSurfaceType((AbstractParametricCurveSurfaceType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_POSITIONAL_ACCURACY_TYPE:
                return validateAbstractPositionalAccuracyType((AbstractPositionalAccuracyType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE:
                return validateAbstractReferenceSystemBaseType((AbstractReferenceSystemBaseType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_TYPE:
                return validateAbstractReferenceSystemType((AbstractReferenceSystemType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE:
                return validateAbstractRingPropertyType((AbstractRingPropertyType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_RING_TYPE:
                return validateAbstractRingType((AbstractRingType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_SOLID_TYPE:
                return validateAbstractSolidType((AbstractSolidType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_STYLE_TYPE:
                return validateAbstractStyleType((AbstractStyleType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_SURFACE_PATCH_TYPE:
                return validateAbstractSurfacePatchType((AbstractSurfacePatchType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_SURFACE_TYPE:
                return validateAbstractSurfaceType((AbstractSurfaceType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TIME_COMPLEX_TYPE:
                return validateAbstractTimeComplexType((AbstractTimeComplexType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TIME_GEOMETRIC_PRIMITIVE_TYPE:
                return validateAbstractTimeGeometricPrimitiveType((AbstractTimeGeometricPrimitiveType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TIME_OBJECT_TYPE:
                return validateAbstractTimeObjectType((AbstractTimeObjectType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TIME_PRIMITIVE_TYPE:
                return validateAbstractTimePrimitiveType((AbstractTimePrimitiveType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TIME_REFERENCE_SYSTEM_TYPE:
                return validateAbstractTimeReferenceSystemType((AbstractTimeReferenceSystemType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TIME_SLICE_TYPE:
                return validateAbstractTimeSliceType((AbstractTimeSliceType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE:
                return validateAbstractTimeTopologyPrimitiveType((AbstractTimeTopologyPrimitiveType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TOPOLOGY_TYPE:
                return validateAbstractTopologyType((AbstractTopologyType)value, diagnostics, context);
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE:
                return validateAbstractTopoPrimitiveType((AbstractTopoPrimitiveType)value, diagnostics, context);
            case Gml311Package.AFFINE_PLACEMENT_TYPE:
                return validateAffinePlacementType((AffinePlacementType)value, diagnostics, context);
            case Gml311Package.ANGLE_CHOICE_TYPE:
                return validateAngleChoiceType((AngleChoiceType)value, diagnostics, context);
            case Gml311Package.ANGLE_TYPE:
                return validateAngleType((AngleType)value, diagnostics, context);
            case Gml311Package.ARC_BY_BULGE_TYPE:
                return validateArcByBulgeType((ArcByBulgeType)value, diagnostics, context);
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE:
                return validateArcByCenterPointType((ArcByCenterPointType)value, diagnostics, context);
            case Gml311Package.ARC_STRING_BY_BULGE_TYPE:
                return validateArcStringByBulgeType((ArcStringByBulgeType)value, diagnostics, context);
            case Gml311Package.ARC_STRING_TYPE:
                return validateArcStringType((ArcStringType)value, diagnostics, context);
            case Gml311Package.ARC_TYPE:
                return validateArcType((ArcType)value, diagnostics, context);
            case Gml311Package.AREA_TYPE:
                return validateAreaType((AreaType)value, diagnostics, context);
            case Gml311Package.ARRAY_ASSOCIATION_TYPE:
                return validateArrayAssociationType((ArrayAssociationType)value, diagnostics, context);
            case Gml311Package.ARRAY_TYPE:
                return validateArrayType((ArrayType)value, diagnostics, context);
            case Gml311Package.ASSOCIATION_TYPE:
                return validateAssociationType((AssociationType)value, diagnostics, context);
            case Gml311Package.BAG_TYPE:
                return validateBagType((BagType)value, diagnostics, context);
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE:
                return validateBaseStyleDescriptorType((BaseStyleDescriptorType)value, diagnostics, context);
            case Gml311Package.BASE_UNIT_TYPE:
                return validateBaseUnitType((BaseUnitType)value, diagnostics, context);
            case Gml311Package.BEZIER_TYPE:
                return validateBezierType((BezierType)value, diagnostics, context);
            case Gml311Package.BOOLEAN_PROPERTY_TYPE:
                return validateBooleanPropertyType((BooleanPropertyType)value, diagnostics, context);
            case Gml311Package.BOUNDED_FEATURE_TYPE:
                return validateBoundedFeatureType((BoundedFeatureType)value, diagnostics, context);
            case Gml311Package.BOUNDING_SHAPE_TYPE:
                return validateBoundingShapeType((BoundingShapeType)value, diagnostics, context);
            case Gml311Package.BSPLINE_TYPE:
                return validateBSplineType((BSplineType)value, diagnostics, context);
            case Gml311Package.CARTESIAN_CS_REF_TYPE:
                return validateCartesianCSRefType((CartesianCSRefType)value, diagnostics, context);
            case Gml311Package.CARTESIAN_CS_TYPE:
                return validateCartesianCSType((CartesianCSType)value, diagnostics, context);
            case Gml311Package.CATEGORY_EXTENT_TYPE:
                return validateCategoryExtentType((CategoryExtentType)value, diagnostics, context);
            case Gml311Package.CATEGORY_PROPERTY_TYPE:
                return validateCategoryPropertyType((CategoryPropertyType)value, diagnostics, context);
            case Gml311Package.CIRCLE_BY_CENTER_POINT_TYPE:
                return validateCircleByCenterPointType((CircleByCenterPointType)value, diagnostics, context);
            case Gml311Package.CIRCLE_TYPE:
                return validateCircleType((CircleType)value, diagnostics, context);
            case Gml311Package.CLOTHOID_TYPE:
                return validateClothoidType((ClothoidType)value, diagnostics, context);
            case Gml311Package.CODE_LIST_TYPE:
                return validateCodeListType((CodeListType)value, diagnostics, context);
            case Gml311Package.CODE_OR_NULL_LIST_TYPE:
                return validateCodeOrNullListType((CodeOrNullListType)value, diagnostics, context);
            case Gml311Package.CODE_TYPE:
                return validateCodeType((CodeType)value, diagnostics, context);
            case Gml311Package.COMPOSITE_CURVE_PROPERTY_TYPE:
                return validateCompositeCurvePropertyType((CompositeCurvePropertyType)value, diagnostics, context);
            case Gml311Package.COMPOSITE_CURVE_TYPE:
                return validateCompositeCurveType((CompositeCurveType)value, diagnostics, context);
            case Gml311Package.COMPOSITE_SOLID_PROPERTY_TYPE:
                return validateCompositeSolidPropertyType((CompositeSolidPropertyType)value, diagnostics, context);
            case Gml311Package.COMPOSITE_SOLID_TYPE:
                return validateCompositeSolidType((CompositeSolidType)value, diagnostics, context);
            case Gml311Package.COMPOSITE_SURFACE_PROPERTY_TYPE:
                return validateCompositeSurfacePropertyType((CompositeSurfacePropertyType)value, diagnostics, context);
            case Gml311Package.COMPOSITE_SURFACE_TYPE:
                return validateCompositeSurfaceType((CompositeSurfaceType)value, diagnostics, context);
            case Gml311Package.COMPOSITE_VALUE_TYPE:
                return validateCompositeValueType((CompositeValueType)value, diagnostics, context);
            case Gml311Package.COMPOUND_CRS_REF_TYPE:
                return validateCompoundCRSRefType((CompoundCRSRefType)value, diagnostics, context);
            case Gml311Package.COMPOUND_CRS_TYPE:
                return validateCompoundCRSType((CompoundCRSType)value, diagnostics, context);
            case Gml311Package.CONCATENATED_OPERATION_REF_TYPE:
                return validateConcatenatedOperationRefType((ConcatenatedOperationRefType)value, diagnostics, context);
            case Gml311Package.CONCATENATED_OPERATION_TYPE:
                return validateConcatenatedOperationType((ConcatenatedOperationType)value, diagnostics, context);
            case Gml311Package.CONE_TYPE:
                return validateConeType((ConeType)value, diagnostics, context);
            case Gml311Package.CONTAINER_PROPERTY_TYPE:
                return validateContainerPropertyType((ContainerPropertyType)value, diagnostics, context);
            case Gml311Package.CONTROL_POINT_TYPE:
                return validateControlPointType((ControlPointType)value, diagnostics, context);
            case Gml311Package.CONVENTIONAL_UNIT_TYPE:
                return validateConventionalUnitType((ConventionalUnitType)value, diagnostics, context);
            case Gml311Package.CONVERSION_REF_TYPE:
                return validateConversionRefType((ConversionRefType)value, diagnostics, context);
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE:
                return validateConversionToPreferredUnitType((ConversionToPreferredUnitType)value, diagnostics, context);
            case Gml311Package.CONVERSION_TYPE:
                return validateConversionType((ConversionType)value, diagnostics, context);
            case Gml311Package.COORDINATE_OPERATION_REF_TYPE:
                return validateCoordinateOperationRefType((CoordinateOperationRefType)value, diagnostics, context);
            case Gml311Package.COORDINATE_REFERENCE_SYSTEM_REF_TYPE:
                return validateCoordinateReferenceSystemRefType((CoordinateReferenceSystemRefType)value, diagnostics, context);
            case Gml311Package.COORDINATES_TYPE:
                return validateCoordinatesType((CoordinatesType)value, diagnostics, context);
            case Gml311Package.COORDINATE_SYSTEM_AXIS_BASE_TYPE:
                return validateCoordinateSystemAxisBaseType((CoordinateSystemAxisBaseType)value, diagnostics, context);
            case Gml311Package.COORDINATE_SYSTEM_AXIS_REF_TYPE:
                return validateCoordinateSystemAxisRefType((CoordinateSystemAxisRefType)value, diagnostics, context);
            case Gml311Package.COORDINATE_SYSTEM_AXIS_TYPE:
                return validateCoordinateSystemAxisType((CoordinateSystemAxisType)value, diagnostics, context);
            case Gml311Package.COORDINATE_SYSTEM_REF_TYPE:
                return validateCoordinateSystemRefType((CoordinateSystemRefType)value, diagnostics, context);
            case Gml311Package.COORD_TYPE:
                return validateCoordType((CoordType)value, diagnostics, context);
            case Gml311Package.COUNT_PROPERTY_TYPE:
                return validateCountPropertyType((CountPropertyType)value, diagnostics, context);
            case Gml311Package.COVARIANCE_ELEMENT_TYPE:
                return validateCovarianceElementType((CovarianceElementType)value, diagnostics, context);
            case Gml311Package.COVARIANCE_MATRIX_TYPE:
                return validateCovarianceMatrixType((CovarianceMatrixType)value, diagnostics, context);
            case Gml311Package.COVERAGE_FUNCTION_TYPE:
                return validateCoverageFunctionType((CoverageFunctionType)value, diagnostics, context);
            case Gml311Package.CRS_REF_TYPE:
                return validateCRSRefType((CRSRefType)value, diagnostics, context);
            case Gml311Package.CUBIC_SPLINE_TYPE:
                return validateCubicSplineType((CubicSplineType)value, diagnostics, context);
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE:
                return validateCurveArrayPropertyType((CurveArrayPropertyType)value, diagnostics, context);
            case Gml311Package.CURVE_PROPERTY_TYPE:
                return validateCurvePropertyType((CurvePropertyType)value, diagnostics, context);
            case Gml311Package.CURVE_SEGMENT_ARRAY_PROPERTY_TYPE:
                return validateCurveSegmentArrayPropertyType((CurveSegmentArrayPropertyType)value, diagnostics, context);
            case Gml311Package.CURVE_TYPE:
                return validateCurveType((CurveType)value, diagnostics, context);
            case Gml311Package.CYLINDER_TYPE:
                return validateCylinderType((CylinderType)value, diagnostics, context);
            case Gml311Package.CYLINDRICAL_CS_REF_TYPE:
                return validateCylindricalCSRefType((CylindricalCSRefType)value, diagnostics, context);
            case Gml311Package.CYLINDRICAL_CS_TYPE:
                return validateCylindricalCSType((CylindricalCSType)value, diagnostics, context);
            case Gml311Package.DATA_BLOCK_TYPE:
                return validateDataBlockType((DataBlockType)value, diagnostics, context);
            case Gml311Package.DATUM_REF_TYPE:
                return validateDatumRefType((DatumRefType)value, diagnostics, context);
            case Gml311Package.DEFAULT_STYLE_PROPERTY_TYPE:
                return validateDefaultStylePropertyType((DefaultStylePropertyType)value, diagnostics, context);
            case Gml311Package.DEFINITION_PROXY_TYPE:
                return validateDefinitionProxyType((DefinitionProxyType)value, diagnostics, context);
            case Gml311Package.DEFINITION_TYPE:
                return validateDefinitionType((DefinitionType)value, diagnostics, context);
            case Gml311Package.DEGREES_TYPE:
                return validateDegreesType((DegreesType)value, diagnostics, context);
            case Gml311Package.DERIVATION_UNIT_TERM_TYPE:
                return validateDerivationUnitTermType((DerivationUnitTermType)value, diagnostics, context);
            case Gml311Package.DERIVED_CRS_REF_TYPE:
                return validateDerivedCRSRefType((DerivedCRSRefType)value, diagnostics, context);
            case Gml311Package.DERIVED_CRS_TYPE:
                return validateDerivedCRSType((DerivedCRSType)value, diagnostics, context);
            case Gml311Package.DERIVED_CRS_TYPE_TYPE:
                return validateDerivedCRSTypeType((DerivedCRSTypeType)value, diagnostics, context);
            case Gml311Package.DERIVED_UNIT_TYPE:
                return validateDerivedUnitType((DerivedUnitType)value, diagnostics, context);
            case Gml311Package.DICTIONARY_ENTRY_TYPE:
                return validateDictionaryEntryType((DictionaryEntryType)value, diagnostics, context);
            case Gml311Package.DICTIONARY_TYPE:
                return validateDictionaryType((DictionaryType)value, diagnostics, context);
            case Gml311Package.DIRECTED_EDGE_PROPERTY_TYPE:
                return validateDirectedEdgePropertyType((DirectedEdgePropertyType)value, diagnostics, context);
            case Gml311Package.DIRECTED_FACE_PROPERTY_TYPE:
                return validateDirectedFacePropertyType((DirectedFacePropertyType)value, diagnostics, context);
            case Gml311Package.DIRECTED_NODE_PROPERTY_TYPE:
                return validateDirectedNodePropertyType((DirectedNodePropertyType)value, diagnostics, context);
            case Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE:
                return validateDirectedObservationAtDistanceType((DirectedObservationAtDistanceType)value, diagnostics, context);
            case Gml311Package.DIRECTED_OBSERVATION_TYPE:
                return validateDirectedObservationType((DirectedObservationType)value, diagnostics, context);
            case Gml311Package.DIRECTED_TOPO_SOLID_PROPERTY_TYPE:
                return validateDirectedTopoSolidPropertyType((DirectedTopoSolidPropertyType)value, diagnostics, context);
            case Gml311Package.DIRECTION_PROPERTY_TYPE:
                return validateDirectionPropertyType((DirectionPropertyType)value, diagnostics, context);
            case Gml311Package.DIRECTION_VECTOR_TYPE:
                return validateDirectionVectorType((DirectionVectorType)value, diagnostics, context);
            case Gml311Package.DIRECT_POSITION_LIST_TYPE:
                return validateDirectPositionListType((DirectPositionListType)value, diagnostics, context);
            case Gml311Package.DIRECT_POSITION_TYPE:
                return validateDirectPositionType((DirectPositionType)value, diagnostics, context);
            case Gml311Package.DMS_ANGLE_TYPE:
                return validateDMSAngleType((DMSAngleType)value, diagnostics, context);
            case Gml311Package.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case Gml311Package.DOMAIN_SET_TYPE:
                return validateDomainSetType((DomainSetType)value, diagnostics, context);
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE:
                return validateDynamicFeatureCollectionType((DynamicFeatureCollectionType)value, diagnostics, context);
            case Gml311Package.DYNAMIC_FEATURE_TYPE:
                return validateDynamicFeatureType((DynamicFeatureType)value, diagnostics, context);
            case Gml311Package.EDGE_TYPE:
                return validateEdgeType((EdgeType)value, diagnostics, context);
            case Gml311Package.ELLIPSOIDAL_CS_REF_TYPE:
                return validateEllipsoidalCSRefType((EllipsoidalCSRefType)value, diagnostics, context);
            case Gml311Package.ELLIPSOIDAL_CS_TYPE:
                return validateEllipsoidalCSType((EllipsoidalCSType)value, diagnostics, context);
            case Gml311Package.ELLIPSOID_BASE_TYPE:
                return validateEllipsoidBaseType((EllipsoidBaseType)value, diagnostics, context);
            case Gml311Package.ELLIPSOID_REF_TYPE:
                return validateEllipsoidRefType((EllipsoidRefType)value, diagnostics, context);
            case Gml311Package.ELLIPSOID_TYPE:
                return validateEllipsoidType((EllipsoidType)value, diagnostics, context);
            case Gml311Package.ENGINEERING_CRS_REF_TYPE:
                return validateEngineeringCRSRefType((EngineeringCRSRefType)value, diagnostics, context);
            case Gml311Package.ENGINEERING_CRS_TYPE:
                return validateEngineeringCRSType((EngineeringCRSType)value, diagnostics, context);
            case Gml311Package.ENGINEERING_DATUM_REF_TYPE:
                return validateEngineeringDatumRefType((EngineeringDatumRefType)value, diagnostics, context);
            case Gml311Package.ENGINEERING_DATUM_TYPE:
                return validateEngineeringDatumType((EngineeringDatumType)value, diagnostics, context);
            case Gml311Package.ENVELOPE_TYPE:
                return validateEnvelopeType((EnvelopeType)value, diagnostics, context);
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE:
                return validateEnvelopeWithTimePeriodType((EnvelopeWithTimePeriodType)value, diagnostics, context);
            case Gml311Package.EXTENT_TYPE:
                return validateExtentType((ExtentType)value, diagnostics, context);
            case Gml311Package.FACE_TYPE:
                return validateFaceType((FaceType)value, diagnostics, context);
            case Gml311Package.FEATURE_ARRAY_PROPERTY_TYPE:
                return validateFeatureArrayPropertyType((FeatureArrayPropertyType)value, diagnostics, context);
            case Gml311Package.FEATURE_COLLECTION_TYPE:
                return validateFeatureCollectionType((FeatureCollectionType)value, diagnostics, context);
            case Gml311Package.FEATURE_PROPERTY_TYPE:
                return validateFeaturePropertyType((FeaturePropertyType)value, diagnostics, context);
            case Gml311Package.FEATURE_STYLE_PROPERTY_TYPE:
                return validateFeatureStylePropertyType((FeatureStylePropertyType)value, diagnostics, context);
            case Gml311Package.FEATURE_STYLE_TYPE:
                return validateFeatureStyleType((FeatureStyleType)value, diagnostics, context);
            case Gml311Package.FILE_TYPE:
                return validateFileType((FileType)value, diagnostics, context);
            case Gml311Package.FORMULA_TYPE:
                return validateFormulaType((FormulaType)value, diagnostics, context);
            case Gml311Package.GENERAL_CONVERSION_REF_TYPE:
                return validateGeneralConversionRefType((GeneralConversionRefType)value, diagnostics, context);
            case Gml311Package.GENERAL_TRANSFORMATION_REF_TYPE:
                return validateGeneralTransformationRefType((GeneralTransformationRefType)value, diagnostics, context);
            case Gml311Package.GENERIC_META_DATA_TYPE:
                return validateGenericMetaDataType((GenericMetaDataType)value, diagnostics, context);
            case Gml311Package.GEOCENTRIC_CRS_REF_TYPE:
                return validateGeocentricCRSRefType((GeocentricCRSRefType)value, diagnostics, context);
            case Gml311Package.GEOCENTRIC_CRS_TYPE:
                return validateGeocentricCRSType((GeocentricCRSType)value, diagnostics, context);
            case Gml311Package.GEODESIC_STRING_TYPE:
                return validateGeodesicStringType((GeodesicStringType)value, diagnostics, context);
            case Gml311Package.GEODESIC_TYPE:
                return validateGeodesicType((GeodesicType)value, diagnostics, context);
            case Gml311Package.GEODETIC_DATUM_REF_TYPE:
                return validateGeodeticDatumRefType((GeodeticDatumRefType)value, diagnostics, context);
            case Gml311Package.GEODETIC_DATUM_TYPE:
                return validateGeodeticDatumType((GeodeticDatumType)value, diagnostics, context);
            case Gml311Package.GEOGRAPHIC_CRS_REF_TYPE:
                return validateGeographicCRSRefType((GeographicCRSRefType)value, diagnostics, context);
            case Gml311Package.GEOGRAPHIC_CRS_TYPE:
                return validateGeographicCRSType((GeographicCRSType)value, diagnostics, context);
            case Gml311Package.GEOMETRIC_COMPLEX_PROPERTY_TYPE:
                return validateGeometricComplexPropertyType((GeometricComplexPropertyType)value, diagnostics, context);
            case Gml311Package.GEOMETRIC_COMPLEX_TYPE:
                return validateGeometricComplexType((GeometricComplexType)value, diagnostics, context);
            case Gml311Package.GEOMETRIC_PRIMITIVE_PROPERTY_TYPE:
                return validateGeometricPrimitivePropertyType((GeometricPrimitivePropertyType)value, diagnostics, context);
            case Gml311Package.GEOMETRY_ARRAY_PROPERTY_TYPE:
                return validateGeometryArrayPropertyType((GeometryArrayPropertyType)value, diagnostics, context);
            case Gml311Package.GEOMETRY_PROPERTY_TYPE:
                return validateGeometryPropertyType((GeometryPropertyType)value, diagnostics, context);
            case Gml311Package.GEOMETRY_STYLE_PROPERTY_TYPE:
                return validateGeometryStylePropertyType((GeometryStylePropertyType)value, diagnostics, context);
            case Gml311Package.GEOMETRY_STYLE_TYPE:
                return validateGeometryStyleType((GeometryStyleType)value, diagnostics, context);
            case Gml311Package.GRAPH_STYLE_PROPERTY_TYPE:
                return validateGraphStylePropertyType((GraphStylePropertyType)value, diagnostics, context);
            case Gml311Package.GRAPH_STYLE_TYPE:
                return validateGraphStyleType((GraphStyleType)value, diagnostics, context);
            case Gml311Package.GRID_COVERAGE_TYPE:
                return validateGridCoverageType((GridCoverageType)value, diagnostics, context);
            case Gml311Package.GRID_DOMAIN_TYPE:
                return validateGridDomainType((GridDomainType)value, diagnostics, context);
            case Gml311Package.GRID_ENVELOPE_TYPE:
                return validateGridEnvelopeType((GridEnvelopeType)value, diagnostics, context);
            case Gml311Package.GRID_FUNCTION_TYPE:
                return validateGridFunctionType((GridFunctionType)value, diagnostics, context);
            case Gml311Package.GRID_LENGTH_TYPE:
                return validateGridLengthType((GridLengthType)value, diagnostics, context);
            case Gml311Package.GRID_LIMITS_TYPE:
                return validateGridLimitsType((GridLimitsType)value, diagnostics, context);
            case Gml311Package.GRID_TYPE:
                return validateGridType((GridType)value, diagnostics, context);
            case Gml311Package.HISTORY_PROPERTY_TYPE:
                return validateHistoryPropertyType((HistoryPropertyType)value, diagnostics, context);
            case Gml311Package.IDENTIFIER_TYPE:
                return validateIdentifierType((IdentifierType)value, diagnostics, context);
            case Gml311Package.IMAGE_CRS_REF_TYPE:
                return validateImageCRSRefType((ImageCRSRefType)value, diagnostics, context);
            case Gml311Package.IMAGE_CRS_TYPE:
                return validateImageCRSType((ImageCRSType)value, diagnostics, context);
            case Gml311Package.IMAGE_DATUM_REF_TYPE:
                return validateImageDatumRefType((ImageDatumRefType)value, diagnostics, context);
            case Gml311Package.IMAGE_DATUM_TYPE:
                return validateImageDatumType((ImageDatumType)value, diagnostics, context);
            case Gml311Package.INDEX_MAP_TYPE:
                return validateIndexMapType((IndexMapType)value, diagnostics, context);
            case Gml311Package.INDIRECT_ENTRY_TYPE:
                return validateIndirectEntryType((IndirectEntryType)value, diagnostics, context);
            case Gml311Package.ISOLATED_PROPERTY_TYPE:
                return validateIsolatedPropertyType((IsolatedPropertyType)value, diagnostics, context);
            case Gml311Package.KNOT_PROPERTY_TYPE:
                return validateKnotPropertyType((KnotPropertyType)value, diagnostics, context);
            case Gml311Package.KNOT_TYPE:
                return validateKnotType((KnotType)value, diagnostics, context);
            case Gml311Package.LABEL_STYLE_PROPERTY_TYPE:
                return validateLabelStylePropertyType((LabelStylePropertyType)value, diagnostics, context);
            case Gml311Package.LABEL_STYLE_TYPE:
                return validateLabelStyleType((LabelStyleType)value, diagnostics, context);
            case Gml311Package.LABEL_TYPE:
                return validateLabelType((LabelType)value, diagnostics, context);
            case Gml311Package.LENGTH_TYPE:
                return validateLengthType((LengthType)value, diagnostics, context);
            case Gml311Package.LINEAR_CS_REF_TYPE:
                return validateLinearCSRefType((LinearCSRefType)value, diagnostics, context);
            case Gml311Package.LINEAR_CS_TYPE:
                return validateLinearCSType((LinearCSType)value, diagnostics, context);
            case Gml311Package.LINEAR_RING_PROPERTY_TYPE:
                return validateLinearRingPropertyType((LinearRingPropertyType)value, diagnostics, context);
            case Gml311Package.LINEAR_RING_TYPE:
                return validateLinearRingType((LinearRingType)value, diagnostics, context);
            case Gml311Package.LINE_STRING_PROPERTY_TYPE:
                return validateLineStringPropertyType((LineStringPropertyType)value, diagnostics, context);
            case Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE:
                return validateLineStringSegmentArrayPropertyType((LineStringSegmentArrayPropertyType)value, diagnostics, context);
            case Gml311Package.LINE_STRING_SEGMENT_TYPE:
                return validateLineStringSegmentType((LineStringSegmentType)value, diagnostics, context);
            case Gml311Package.LINE_STRING_TYPE:
                return validateLineStringType((LineStringType)value, diagnostics, context);
            case Gml311Package.LOCATION_PROPERTY_TYPE:
                return validateLocationPropertyType((LocationPropertyType)value, diagnostics, context);
            case Gml311Package.MEASURE_LIST_TYPE:
                return validateMeasureListType((MeasureListType)value, diagnostics, context);
            case Gml311Package.MEASURE_OR_NULL_LIST_TYPE:
                return validateMeasureOrNullListType((MeasureOrNullListType)value, diagnostics, context);
            case Gml311Package.MEASURE_TYPE:
                return validateMeasureType((MeasureType)value, diagnostics, context);
            case Gml311Package.META_DATA_PROPERTY_TYPE:
                return validateMetaDataPropertyType((MetaDataPropertyType)value, diagnostics, context);
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE:
                return validateMovingObjectStatusType((MovingObjectStatusType)value, diagnostics, context);
            case Gml311Package.MULTI_CURVE_COVERAGE_TYPE:
                return validateMultiCurveCoverageType((MultiCurveCoverageType)value, diagnostics, context);
            case Gml311Package.MULTI_CURVE_DOMAIN_TYPE:
                return validateMultiCurveDomainType((MultiCurveDomainType)value, diagnostics, context);
            case Gml311Package.MULTI_CURVE_PROPERTY_TYPE:
                return validateMultiCurvePropertyType((MultiCurvePropertyType)value, diagnostics, context);
            case Gml311Package.MULTI_CURVE_TYPE:
                return validateMultiCurveType((MultiCurveType)value, diagnostics, context);
            case Gml311Package.MULTI_GEOMETRY_PROPERTY_TYPE:
                return validateMultiGeometryPropertyType((MultiGeometryPropertyType)value, diagnostics, context);
            case Gml311Package.MULTI_GEOMETRY_TYPE:
                return validateMultiGeometryType((MultiGeometryType)value, diagnostics, context);
            case Gml311Package.MULTI_LINE_STRING_PROPERTY_TYPE:
                return validateMultiLineStringPropertyType((MultiLineStringPropertyType)value, diagnostics, context);
            case Gml311Package.MULTI_LINE_STRING_TYPE:
                return validateMultiLineStringType((MultiLineStringType)value, diagnostics, context);
            case Gml311Package.MULTI_POINT_COVERAGE_TYPE:
                return validateMultiPointCoverageType((MultiPointCoverageType)value, diagnostics, context);
            case Gml311Package.MULTI_POINT_DOMAIN_TYPE:
                return validateMultiPointDomainType((MultiPointDomainType)value, diagnostics, context);
            case Gml311Package.MULTI_POINT_PROPERTY_TYPE:
                return validateMultiPointPropertyType((MultiPointPropertyType)value, diagnostics, context);
            case Gml311Package.MULTI_POINT_TYPE:
                return validateMultiPointType((MultiPointType)value, diagnostics, context);
            case Gml311Package.MULTI_POLYGON_PROPERTY_TYPE:
                return validateMultiPolygonPropertyType((MultiPolygonPropertyType)value, diagnostics, context);
            case Gml311Package.MULTI_POLYGON_TYPE:
                return validateMultiPolygonType((MultiPolygonType)value, diagnostics, context);
            case Gml311Package.MULTI_SOLID_COVERAGE_TYPE:
                return validateMultiSolidCoverageType((MultiSolidCoverageType)value, diagnostics, context);
            case Gml311Package.MULTI_SOLID_DOMAIN_TYPE:
                return validateMultiSolidDomainType((MultiSolidDomainType)value, diagnostics, context);
            case Gml311Package.MULTI_SOLID_PROPERTY_TYPE:
                return validateMultiSolidPropertyType((MultiSolidPropertyType)value, diagnostics, context);
            case Gml311Package.MULTI_SOLID_TYPE:
                return validateMultiSolidType((MultiSolidType)value, diagnostics, context);
            case Gml311Package.MULTI_SURFACE_COVERAGE_TYPE:
                return validateMultiSurfaceCoverageType((MultiSurfaceCoverageType)value, diagnostics, context);
            case Gml311Package.MULTI_SURFACE_DOMAIN_TYPE:
                return validateMultiSurfaceDomainType((MultiSurfaceDomainType)value, diagnostics, context);
            case Gml311Package.MULTI_SURFACE_PROPERTY_TYPE:
                return validateMultiSurfacePropertyType((MultiSurfacePropertyType)value, diagnostics, context);
            case Gml311Package.MULTI_SURFACE_TYPE:
                return validateMultiSurfaceType((MultiSurfaceType)value, diagnostics, context);
            case Gml311Package.NODE_TYPE:
                return validateNodeType((NodeType)value, diagnostics, context);
            case Gml311Package.OBLIQUE_CARTESIAN_CS_REF_TYPE:
                return validateObliqueCartesianCSRefType((ObliqueCartesianCSRefType)value, diagnostics, context);
            case Gml311Package.OBLIQUE_CARTESIAN_CS_TYPE:
                return validateObliqueCartesianCSType((ObliqueCartesianCSType)value, diagnostics, context);
            case Gml311Package.OBSERVATION_TYPE:
                return validateObservationType((ObservationType)value, diagnostics, context);
            case Gml311Package.OFFSET_CURVE_TYPE:
                return validateOffsetCurveType((OffsetCurveType)value, diagnostics, context);
            case Gml311Package.OPERATION_METHOD_BASE_TYPE:
                return validateOperationMethodBaseType((OperationMethodBaseType)value, diagnostics, context);
            case Gml311Package.OPERATION_METHOD_REF_TYPE:
                return validateOperationMethodRefType((OperationMethodRefType)value, diagnostics, context);
            case Gml311Package.OPERATION_METHOD_TYPE:
                return validateOperationMethodType((OperationMethodType)value, diagnostics, context);
            case Gml311Package.OPERATION_PARAMETER_BASE_TYPE:
                return validateOperationParameterBaseType((OperationParameterBaseType)value, diagnostics, context);
            case Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE:
                return validateOperationParameterGroupBaseType((OperationParameterGroupBaseType)value, diagnostics, context);
            case Gml311Package.OPERATION_PARAMETER_GROUP_REF_TYPE:
                return validateOperationParameterGroupRefType((OperationParameterGroupRefType)value, diagnostics, context);
            case Gml311Package.OPERATION_PARAMETER_GROUP_TYPE:
                return validateOperationParameterGroupType((OperationParameterGroupType)value, diagnostics, context);
            case Gml311Package.OPERATION_PARAMETER_REF_TYPE:
                return validateOperationParameterRefType((OperationParameterRefType)value, diagnostics, context);
            case Gml311Package.OPERATION_PARAMETER_TYPE:
                return validateOperationParameterType((OperationParameterType)value, diagnostics, context);
            case Gml311Package.OPERATION_REF_TYPE:
                return validateOperationRefType((OperationRefType)value, diagnostics, context);
            case Gml311Package.ORIENTABLE_CURVE_TYPE:
                return validateOrientableCurveType((OrientableCurveType)value, diagnostics, context);
            case Gml311Package.ORIENTABLE_SURFACE_TYPE:
                return validateOrientableSurfaceType((OrientableSurfaceType)value, diagnostics, context);
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE:
                return validateParameterValueGroupType((ParameterValueGroupType)value, diagnostics, context);
            case Gml311Package.PARAMETER_VALUE_TYPE:
                return validateParameterValueType((ParameterValueType)value, diagnostics, context);
            case Gml311Package.PASS_THROUGH_OPERATION_REF_TYPE:
                return validatePassThroughOperationRefType((PassThroughOperationRefType)value, diagnostics, context);
            case Gml311Package.PASS_THROUGH_OPERATION_TYPE:
                return validatePassThroughOperationType((PassThroughOperationType)value, diagnostics, context);
            case Gml311Package.PIXEL_IN_CELL_TYPE:
                return validatePixelInCellType((PixelInCellType)value, diagnostics, context);
            case Gml311Package.POINT_ARRAY_PROPERTY_TYPE:
                return validatePointArrayPropertyType((PointArrayPropertyType)value, diagnostics, context);
            case Gml311Package.POINT_PROPERTY_TYPE:
                return validatePointPropertyType((PointPropertyType)value, diagnostics, context);
            case Gml311Package.POINT_TYPE:
                return validatePointType((PointType)value, diagnostics, context);
            case Gml311Package.POLAR_CS_REF_TYPE:
                return validatePolarCSRefType((PolarCSRefType)value, diagnostics, context);
            case Gml311Package.POLAR_CS_TYPE:
                return validatePolarCSType((PolarCSType)value, diagnostics, context);
            case Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE:
                return validatePolygonPatchArrayPropertyType((PolygonPatchArrayPropertyType)value, diagnostics, context);
            case Gml311Package.POLYGON_PATCH_TYPE:
                return validatePolygonPatchType((PolygonPatchType)value, diagnostics, context);
            case Gml311Package.POLYGON_PROPERTY_TYPE:
                return validatePolygonPropertyType((PolygonPropertyType)value, diagnostics, context);
            case Gml311Package.POLYGON_TYPE:
                return validatePolygonType((PolygonType)value, diagnostics, context);
            case Gml311Package.POLYHEDRAL_SURFACE_TYPE:
                return validatePolyhedralSurfaceType((PolyhedralSurfaceType)value, diagnostics, context);
            case Gml311Package.PRIME_MERIDIAN_BASE_TYPE:
                return validatePrimeMeridianBaseType((PrimeMeridianBaseType)value, diagnostics, context);
            case Gml311Package.PRIME_MERIDIAN_REF_TYPE:
                return validatePrimeMeridianRefType((PrimeMeridianRefType)value, diagnostics, context);
            case Gml311Package.PRIME_MERIDIAN_TYPE:
                return validatePrimeMeridianType((PrimeMeridianType)value, diagnostics, context);
            case Gml311Package.PRIORITY_LOCATION_PROPERTY_TYPE:
                return validatePriorityLocationPropertyType((PriorityLocationPropertyType)value, diagnostics, context);
            case Gml311Package.PROJECTED_CRS_REF_TYPE:
                return validateProjectedCRSRefType((ProjectedCRSRefType)value, diagnostics, context);
            case Gml311Package.PROJECTED_CRS_TYPE:
                return validateProjectedCRSType((ProjectedCRSType)value, diagnostics, context);
            case Gml311Package.QUANTITY_EXTENT_TYPE:
                return validateQuantityExtentType((QuantityExtentType)value, diagnostics, context);
            case Gml311Package.QUANTITY_PROPERTY_TYPE:
                return validateQuantityPropertyType((QuantityPropertyType)value, diagnostics, context);
            case Gml311Package.RANGE_PARAMETERS_TYPE:
                return validateRangeParametersType((RangeParametersType)value, diagnostics, context);
            case Gml311Package.RANGE_SET_TYPE:
                return validateRangeSetType((RangeSetType)value, diagnostics, context);
            case Gml311Package.RECTANGLE_TYPE:
                return validateRectangleType((RectangleType)value, diagnostics, context);
            case Gml311Package.RECTIFIED_GRID_COVERAGE_TYPE:
                return validateRectifiedGridCoverageType((RectifiedGridCoverageType)value, diagnostics, context);
            case Gml311Package.RECTIFIED_GRID_DOMAIN_TYPE:
                return validateRectifiedGridDomainType((RectifiedGridDomainType)value, diagnostics, context);
            case Gml311Package.RECTIFIED_GRID_TYPE:
                return validateRectifiedGridType((RectifiedGridType)value, diagnostics, context);
            case Gml311Package.REFERENCE_SYSTEM_REF_TYPE:
                return validateReferenceSystemRefType((ReferenceSystemRefType)value, diagnostics, context);
            case Gml311Package.REFERENCE_TYPE:
                return validateReferenceType((ReferenceType)value, diagnostics, context);
            case Gml311Package.REF_LOCATION_TYPE:
                return validateRefLocationType((RefLocationType)value, diagnostics, context);
            case Gml311Package.RELATED_TIME_TYPE:
                return validateRelatedTimeType((RelatedTimeType)value, diagnostics, context);
            case Gml311Package.RELATIVE_INTERNAL_POSITIONAL_ACCURACY_TYPE:
                return validateRelativeInternalPositionalAccuracyType((RelativeInternalPositionalAccuracyType)value, diagnostics, context);
            case Gml311Package.RING_PROPERTY_TYPE:
                return validateRingPropertyType((RingPropertyType)value, diagnostics, context);
            case Gml311Package.RING_TYPE:
                return validateRingType((RingType)value, diagnostics, context);
            case Gml311Package.ROW_TYPE:
                return validateRowType((RowType)value, diagnostics, context);
            case Gml311Package.SCALAR_VALUE_PROPERTY_TYPE:
                return validateScalarValuePropertyType((ScalarValuePropertyType)value, diagnostics, context);
            case Gml311Package.SCALE_TYPE:
                return validateScaleType((ScaleType)value, diagnostics, context);
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE:
                return validateSecondDefiningParameterType((SecondDefiningParameterType)value, diagnostics, context);
            case Gml311Package.SEQUENCE_RULE_TYPE:
                return validateSequenceRuleType((SequenceRuleType)value, diagnostics, context);
            case Gml311Package.SINGLE_OPERATION_REF_TYPE:
                return validateSingleOperationRefType((SingleOperationRefType)value, diagnostics, context);
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE:
                return validateSolidArrayPropertyType((SolidArrayPropertyType)value, diagnostics, context);
            case Gml311Package.SOLID_PROPERTY_TYPE:
                return validateSolidPropertyType((SolidPropertyType)value, diagnostics, context);
            case Gml311Package.SOLID_TYPE:
                return validateSolidType((SolidType)value, diagnostics, context);
            case Gml311Package.SPEED_TYPE:
                return validateSpeedType((SpeedType)value, diagnostics, context);
            case Gml311Package.SPHERE_TYPE:
                return validateSphereType((SphereType)value, diagnostics, context);
            case Gml311Package.SPHERICAL_CS_REF_TYPE:
                return validateSphericalCSRefType((SphericalCSRefType)value, diagnostics, context);
            case Gml311Package.SPHERICAL_CS_TYPE:
                return validateSphericalCSType((SphericalCSType)value, diagnostics, context);
            case Gml311Package.STRING_OR_REF_TYPE:
                return validateStringOrRefType((StringOrRefType)value, diagnostics, context);
            case Gml311Package.STYLE_TYPE:
                return validateStyleType((StyleType)value, diagnostics, context);
            case Gml311Package.STYLE_VARIATION_TYPE:
                return validateStyleVariationType((StyleVariationType)value, diagnostics, context);
            case Gml311Package.SURFACE_ARRAY_PROPERTY_TYPE:
                return validateSurfaceArrayPropertyType((SurfaceArrayPropertyType)value, diagnostics, context);
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE:
                return validateSurfacePatchArrayPropertyType((SurfacePatchArrayPropertyType)value, diagnostics, context);
            case Gml311Package.SURFACE_PROPERTY_TYPE:
                return validateSurfacePropertyType((SurfacePropertyType)value, diagnostics, context);
            case Gml311Package.SURFACE_TYPE:
                return validateSurfaceType((SurfaceType)value, diagnostics, context);
            case Gml311Package.SYMBOL_TYPE:
                return validateSymbolType((SymbolType)value, diagnostics, context);
            case Gml311Package.TARGET_PROPERTY_TYPE:
                return validateTargetPropertyType((TargetPropertyType)value, diagnostics, context);
            case Gml311Package.TEMPORAL_CRS_REF_TYPE:
                return validateTemporalCRSRefType((TemporalCRSRefType)value, diagnostics, context);
            case Gml311Package.TEMPORAL_CRS_TYPE:
                return validateTemporalCRSType((TemporalCRSType)value, diagnostics, context);
            case Gml311Package.TEMPORAL_CS_REF_TYPE:
                return validateTemporalCSRefType((TemporalCSRefType)value, diagnostics, context);
            case Gml311Package.TEMPORAL_CS_TYPE:
                return validateTemporalCSType((TemporalCSType)value, diagnostics, context);
            case Gml311Package.TEMPORAL_DATUM_BASE_TYPE:
                return validateTemporalDatumBaseType((TemporalDatumBaseType)value, diagnostics, context);
            case Gml311Package.TEMPORAL_DATUM_REF_TYPE:
                return validateTemporalDatumRefType((TemporalDatumRefType)value, diagnostics, context);
            case Gml311Package.TEMPORAL_DATUM_TYPE:
                return validateTemporalDatumType((TemporalDatumType)value, diagnostics, context);
            case Gml311Package.TIME_CALENDAR_ERA_PROPERTY_TYPE:
                return validateTimeCalendarEraPropertyType((TimeCalendarEraPropertyType)value, diagnostics, context);
            case Gml311Package.TIME_CALENDAR_ERA_TYPE:
                return validateTimeCalendarEraType((TimeCalendarEraType)value, diagnostics, context);
            case Gml311Package.TIME_CALENDAR_PROPERTY_TYPE:
                return validateTimeCalendarPropertyType((TimeCalendarPropertyType)value, diagnostics, context);
            case Gml311Package.TIME_CALENDAR_TYPE:
                return validateTimeCalendarType((TimeCalendarType)value, diagnostics, context);
            case Gml311Package.TIME_CLOCK_PROPERTY_TYPE:
                return validateTimeClockPropertyType((TimeClockPropertyType)value, diagnostics, context);
            case Gml311Package.TIME_CLOCK_TYPE:
                return validateTimeClockType((TimeClockType)value, diagnostics, context);
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE:
                return validateTimeCoordinateSystemType((TimeCoordinateSystemType)value, diagnostics, context);
            case Gml311Package.TIME_EDGE_PROPERTY_TYPE:
                return validateTimeEdgePropertyType((TimeEdgePropertyType)value, diagnostics, context);
            case Gml311Package.TIME_EDGE_TYPE:
                return validateTimeEdgeType((TimeEdgeType)value, diagnostics, context);
            case Gml311Package.TIME_GEOMETRIC_PRIMITIVE_PROPERTY_TYPE:
                return validateTimeGeometricPrimitivePropertyType((TimeGeometricPrimitivePropertyType)value, diagnostics, context);
            case Gml311Package.TIME_INSTANT_PROPERTY_TYPE:
                return validateTimeInstantPropertyType((TimeInstantPropertyType)value, diagnostics, context);
            case Gml311Package.TIME_INSTANT_TYPE:
                return validateTimeInstantType((TimeInstantType)value, diagnostics, context);
            case Gml311Package.TIME_INTERVAL_LENGTH_TYPE:
                return validateTimeIntervalLengthType((TimeIntervalLengthType)value, diagnostics, context);
            case Gml311Package.TIME_NODE_PROPERTY_TYPE:
                return validateTimeNodePropertyType((TimeNodePropertyType)value, diagnostics, context);
            case Gml311Package.TIME_NODE_TYPE:
                return validateTimeNodeType((TimeNodeType)value, diagnostics, context);
            case Gml311Package.TIME_ORDINAL_ERA_PROPERTY_TYPE:
                return validateTimeOrdinalEraPropertyType((TimeOrdinalEraPropertyType)value, diagnostics, context);
            case Gml311Package.TIME_ORDINAL_ERA_TYPE:
                return validateTimeOrdinalEraType((TimeOrdinalEraType)value, diagnostics, context);
            case Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE:
                return validateTimeOrdinalReferenceSystemType((TimeOrdinalReferenceSystemType)value, diagnostics, context);
            case Gml311Package.TIME_PERIOD_PROPERTY_TYPE:
                return validateTimePeriodPropertyType((TimePeriodPropertyType)value, diagnostics, context);
            case Gml311Package.TIME_PERIOD_TYPE:
                return validateTimePeriodType((TimePeriodType)value, diagnostics, context);
            case Gml311Package.TIME_POSITION_TYPE:
                return validateTimePositionType((TimePositionType)value, diagnostics, context);
            case Gml311Package.TIME_PRIMITIVE_PROPERTY_TYPE:
                return validateTimePrimitivePropertyType((TimePrimitivePropertyType)value, diagnostics, context);
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_PROPERTY_TYPE:
                return validateTimeTopologyComplexPropertyType((TimeTopologyComplexPropertyType)value, diagnostics, context);
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE:
                return validateTimeTopologyComplexType((TimeTopologyComplexType)value, diagnostics, context);
            case Gml311Package.TIME_TOPOLOGY_PRIMITIVE_PROPERTY_TYPE:
                return validateTimeTopologyPrimitivePropertyType((TimeTopologyPrimitivePropertyType)value, diagnostics, context);
            case Gml311Package.TIME_TYPE:
                return validateTimeType((TimeType)value, diagnostics, context);
            case Gml311Package.TIN_TYPE:
                return validateTinType((TinType)value, diagnostics, context);
            case Gml311Package.TOPO_COMPLEX_MEMBER_TYPE:
                return validateTopoComplexMemberType((TopoComplexMemberType)value, diagnostics, context);
            case Gml311Package.TOPO_COMPLEX_TYPE:
                return validateTopoComplexType((TopoComplexType)value, diagnostics, context);
            case Gml311Package.TOPO_CURVE_PROPERTY_TYPE:
                return validateTopoCurvePropertyType((TopoCurvePropertyType)value, diagnostics, context);
            case Gml311Package.TOPO_CURVE_TYPE:
                return validateTopoCurveType((TopoCurveType)value, diagnostics, context);
            case Gml311Package.TOPOLOGY_STYLE_PROPERTY_TYPE:
                return validateTopologyStylePropertyType((TopologyStylePropertyType)value, diagnostics, context);
            case Gml311Package.TOPOLOGY_STYLE_TYPE:
                return validateTopologyStyleType((TopologyStyleType)value, diagnostics, context);
            case Gml311Package.TOPO_POINT_PROPERTY_TYPE:
                return validateTopoPointPropertyType((TopoPointPropertyType)value, diagnostics, context);
            case Gml311Package.TOPO_POINT_TYPE:
                return validateTopoPointType((TopoPointType)value, diagnostics, context);
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE:
                return validateTopoPrimitiveArrayAssociationType((TopoPrimitiveArrayAssociationType)value, diagnostics, context);
            case Gml311Package.TOPO_PRIMITIVE_MEMBER_TYPE:
                return validateTopoPrimitiveMemberType((TopoPrimitiveMemberType)value, diagnostics, context);
            case Gml311Package.TOPO_SOLID_TYPE:
                return validateTopoSolidType((TopoSolidType)value, diagnostics, context);
            case Gml311Package.TOPO_SURFACE_PROPERTY_TYPE:
                return validateTopoSurfacePropertyType((TopoSurfacePropertyType)value, diagnostics, context);
            case Gml311Package.TOPO_SURFACE_TYPE:
                return validateTopoSurfaceType((TopoSurfaceType)value, diagnostics, context);
            case Gml311Package.TOPO_VOLUME_PROPERTY_TYPE:
                return validateTopoVolumePropertyType((TopoVolumePropertyType)value, diagnostics, context);
            case Gml311Package.TOPO_VOLUME_TYPE:
                return validateTopoVolumeType((TopoVolumeType)value, diagnostics, context);
            case Gml311Package.TRACK_TYPE:
                return validateTrackType((TrackType)value, diagnostics, context);
            case Gml311Package.TRANSFORMATION_REF_TYPE:
                return validateTransformationRefType((TransformationRefType)value, diagnostics, context);
            case Gml311Package.TRANSFORMATION_TYPE:
                return validateTransformationType((TransformationType)value, diagnostics, context);
            case Gml311Package.TRIANGLE_PATCH_ARRAY_PROPERTY_TYPE:
                return validateTrianglePatchArrayPropertyType((TrianglePatchArrayPropertyType)value, diagnostics, context);
            case Gml311Package.TRIANGLE_TYPE:
                return validateTriangleType((TriangleType)value, diagnostics, context);
            case Gml311Package.TRIANGULATED_SURFACE_TYPE:
                return validateTriangulatedSurfaceType((TriangulatedSurfaceType)value, diagnostics, context);
            case Gml311Package.UNIT_DEFINITION_TYPE:
                return validateUnitDefinitionType((UnitDefinitionType)value, diagnostics, context);
            case Gml311Package.UNIT_OF_MEASURE_TYPE:
                return validateUnitOfMeasureType((UnitOfMeasureType)value, diagnostics, context);
            case Gml311Package.USER_DEFINED_CS_REF_TYPE:
                return validateUserDefinedCSRefType((UserDefinedCSRefType)value, diagnostics, context);
            case Gml311Package.USER_DEFINED_CS_TYPE:
                return validateUserDefinedCSType((UserDefinedCSType)value, diagnostics, context);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE:
                return validateValueArrayPropertyType((ValueArrayPropertyType)value, diagnostics, context);
            case Gml311Package.VALUE_ARRAY_TYPE:
                return validateValueArrayType((ValueArrayType)value, diagnostics, context);
            case Gml311Package.VALUE_PROPERTY_TYPE:
                return validateValuePropertyType((ValuePropertyType)value, diagnostics, context);
            case Gml311Package.VECTOR_TYPE:
                return validateVectorType((VectorType)value, diagnostics, context);
            case Gml311Package.VERTICAL_CRS_REF_TYPE:
                return validateVerticalCRSRefType((VerticalCRSRefType)value, diagnostics, context);
            case Gml311Package.VERTICAL_CRS_TYPE:
                return validateVerticalCRSType((VerticalCRSType)value, diagnostics, context);
            case Gml311Package.VERTICAL_CS_REF_TYPE:
                return validateVerticalCSRefType((VerticalCSRefType)value, diagnostics, context);
            case Gml311Package.VERTICAL_CS_TYPE:
                return validateVerticalCSType((VerticalCSType)value, diagnostics, context);
            case Gml311Package.VERTICAL_DATUM_REF_TYPE:
                return validateVerticalDatumRefType((VerticalDatumRefType)value, diagnostics, context);
            case Gml311Package.VERTICAL_DATUM_TYPE:
                return validateVerticalDatumType((VerticalDatumType)value, diagnostics, context);
            case Gml311Package.VERTICAL_DATUM_TYPE_TYPE:
                return validateVerticalDatumTypeType((VerticalDatumTypeType)value, diagnostics, context);
            case Gml311Package.VOLUME_TYPE:
                return validateVolumeType((VolumeType)value, diagnostics, context);
            case Gml311Package.AESHETIC_CRITERIA_TYPE:
                return validateAesheticCriteriaType((AesheticCriteriaType)value, diagnostics, context);
            case Gml311Package.COMPASS_POINT_ENUMERATION:
                return validateCompassPointEnumeration((CompassPointEnumeration)value, diagnostics, context);
            case Gml311Package.CURVE_INTERPOLATION_TYPE:
                return validateCurveInterpolationType((CurveInterpolationType)value, diagnostics, context);
            case Gml311Package.DIRECTION_TYPE_MEMBER0:
                return validateDirectionTypeMember0((DirectionTypeMember0)value, diagnostics, context);
            case Gml311Package.DRAWING_TYPE_TYPE:
                return validateDrawingTypeType((DrawingTypeType)value, diagnostics, context);
            case Gml311Package.FILE_VALUE_MODEL_TYPE:
                return validateFileValueModelType((FileValueModelType)value, diagnostics, context);
            case Gml311Package.GRAPH_TYPE_TYPE:
                return validateGraphTypeType((GraphTypeType)value, diagnostics, context);
            case Gml311Package.INCREMENT_ORDER:
                return validateIncrementOrder((IncrementOrder)value, diagnostics, context);
            case Gml311Package.IS_SPHERE_TYPE:
                return validateIsSphereType((IsSphereType)value, diagnostics, context);
            case Gml311Package.KNOT_TYPES_TYPE:
                return validateKnotTypesType((KnotTypesType)value, diagnostics, context);
            case Gml311Package.LINE_TYPE_TYPE:
                return validateLineTypeType((LineTypeType)value, diagnostics, context);
            case Gml311Package.NULL_ENUMERATION_MEMBER0:
                return validateNullEnumerationMember0((NullEnumerationMember0)value, diagnostics, context);
            case Gml311Package.QUERY_GRAMMAR_ENUMERATION:
                return validateQueryGrammarEnumeration((QueryGrammarEnumeration)value, diagnostics, context);
            case Gml311Package.RELATIVE_POSITION_TYPE:
                return validateRelativePositionType((RelativePositionType)value, diagnostics, context);
            case Gml311Package.SEQUENCE_RULE_NAMES:
                return validateSequenceRuleNames((SequenceRuleNames)value, diagnostics, context);
            case Gml311Package.SIGN_TYPE:
                return validateSignType((SignType)value, diagnostics, context);
            case Gml311Package.SUCCESSION_TYPE:
                return validateSuccessionType((SuccessionType)value, diagnostics, context);
            case Gml311Package.SURFACE_INTERPOLATION_TYPE:
                return validateSurfaceInterpolationType((SurfaceInterpolationType)value, diagnostics, context);
            case Gml311Package.SYMBOL_TYPE_ENUMERATION:
                return validateSymbolTypeEnumeration((SymbolTypeEnumeration)value, diagnostics, context);
            case Gml311Package.TIME_INDETERMINATE_VALUE_TYPE:
                return validateTimeIndeterminateValueType((TimeIndeterminateValueType)value, diagnostics, context);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER0:
                return validateTimeUnitTypeMember0((TimeUnitTypeMember0)value, diagnostics, context);
            case Gml311Package.AESHETIC_CRITERIA_TYPE_OBJECT:
                return validateAesheticCriteriaTypeObject((AesheticCriteriaType)value, diagnostics, context);
            case Gml311Package.ARC_MINUTES_TYPE:
                return validateArcMinutesType((BigInteger)value, diagnostics, context);
            case Gml311Package.ARC_SECONDS_TYPE:
                return validateArcSecondsType((BigDecimal)value, diagnostics, context);
            case Gml311Package.BOOLEAN_LIST:
                return validateBooleanList((List<?>)value, diagnostics, context);
            case Gml311Package.BOOLEAN_OR_NULL:
                return validateBooleanOrNull(value, diagnostics, context);
            case Gml311Package.BOOLEAN_OR_NULL_LIST:
                return validateBooleanOrNullList((List<?>)value, diagnostics, context);
            case Gml311Package.CAL_DATE:
                return validateCalDate((XMLGregorianCalendar)value, diagnostics, context);
            case Gml311Package.COMPASS_POINT_ENUMERATION_OBJECT:
                return validateCompassPointEnumerationObject((CompassPointEnumeration)value, diagnostics, context);
            case Gml311Package.COUNT_EXTENT_TYPE:
                return validateCountExtentType((List<?>)value, diagnostics, context);
            case Gml311Package.CURVE_INTERPOLATION_TYPE_OBJECT:
                return validateCurveInterpolationTypeObject((CurveInterpolationType)value, diagnostics, context);
            case Gml311Package.DECIMAL_MINUTES_TYPE:
                return validateDecimalMinutesType((BigDecimal)value, diagnostics, context);
            case Gml311Package.DEGREE_VALUE_TYPE:
                return validateDegreeValueType((BigInteger)value, diagnostics, context);
            case Gml311Package.DIRECTION_TYPE:
                return validateDirectionType((Enumerator)value, diagnostics, context);
            case Gml311Package.DIRECTION_TYPE_MEMBER0_OBJECT:
                return validateDirectionTypeMember0Object((DirectionTypeMember0)value, diagnostics, context);
            case Gml311Package.DIRECTION_TYPE_MEMBER1:
                return validateDirectionTypeMember1((SignType)value, diagnostics, context);
            case Gml311Package.DOUBLE_LIST:
                return validateDoubleList((List<?>)value, diagnostics, context);
            case Gml311Package.DOUBLE_OR_NULL:
                return validateDoubleOrNull(value, diagnostics, context);
            case Gml311Package.DOUBLE_OR_NULL_LIST:
                return validateDoubleOrNullList((List<?>)value, diagnostics, context);
            case Gml311Package.DRAWING_TYPE_TYPE_OBJECT:
                return validateDrawingTypeTypeObject((DrawingTypeType)value, diagnostics, context);
            case Gml311Package.FILE_VALUE_MODEL_TYPE_OBJECT:
                return validateFileValueModelTypeObject((FileValueModelType)value, diagnostics, context);
            case Gml311Package.GRAPH_TYPE_TYPE_OBJECT:
                return validateGraphTypeTypeObject((GraphTypeType)value, diagnostics, context);
            case Gml311Package.INCREMENT_ORDER_OBJECT:
                return validateIncrementOrderObject((IncrementOrder)value, diagnostics, context);
            case Gml311Package.INTEGER_LIST:
                return validateIntegerList((List<?>)value, diagnostics, context);
            case Gml311Package.INTEGER_OR_NULL:
                return validateIntegerOrNull(value, diagnostics, context);
            case Gml311Package.INTEGER_OR_NULL_LIST:
                return validateIntegerOrNullList((List<?>)value, diagnostics, context);
            case Gml311Package.IS_SPHERE_TYPE_OBJECT:
                return validateIsSphereTypeObject((IsSphereType)value, diagnostics, context);
            case Gml311Package.KNOT_TYPES_TYPE_OBJECT:
                return validateKnotTypesTypeObject((KnotTypesType)value, diagnostics, context);
            case Gml311Package.LINE_TYPE_TYPE_OBJECT:
                return validateLineTypeTypeObject((LineTypeType)value, diagnostics, context);
            case Gml311Package.NAME_LIST:
                return validateNameList((List<?>)value, diagnostics, context);
            case Gml311Package.NAME_OR_NULL:
                return validateNameOrNull(value, diagnostics, context);
            case Gml311Package.NAME_OR_NULL_LIST:
                return validateNameOrNullList((List<?>)value, diagnostics, context);
            case Gml311Package.NC_NAME_LIST:
                return validateNCNameList((List<?>)value, diagnostics, context);
            case Gml311Package.NULL_ENUMERATION:
                return validateNullEnumeration(value, diagnostics, context);
            case Gml311Package.NULL_ENUMERATION_MEMBER0_OBJECT:
                return validateNullEnumerationMember0Object((NullEnumerationMember0)value, diagnostics, context);
            case Gml311Package.NULL_ENUMERATION_MEMBER1:
                return validateNullEnumerationMember1((String)value, diagnostics, context);
            case Gml311Package.NULL_TYPE:
                return validateNullType(value, diagnostics, context);
            case Gml311Package.QNAME_LIST:
                return validateQNameList((List<?>)value, diagnostics, context);
            case Gml311Package.QUERY_GRAMMAR_ENUMERATION_OBJECT:
                return validateQueryGrammarEnumerationObject((QueryGrammarEnumeration)value, diagnostics, context);
            case Gml311Package.RELATIVE_POSITION_TYPE_OBJECT:
                return validateRelativePositionTypeObject((RelativePositionType)value, diagnostics, context);
            case Gml311Package.SEQUENCE_RULE_NAMES_OBJECT:
                return validateSequenceRuleNamesObject((SequenceRuleNames)value, diagnostics, context);
            case Gml311Package.SIGN_TYPE_OBJECT:
                return validateSignTypeObject((SignType)value, diagnostics, context);
            case Gml311Package.STRING_OR_NULL:
                return validateStringOrNull(value, diagnostics, context);
            case Gml311Package.SUCCESSION_TYPE_OBJECT:
                return validateSuccessionTypeObject((SuccessionType)value, diagnostics, context);
            case Gml311Package.SURFACE_INTERPOLATION_TYPE_OBJECT:
                return validateSurfaceInterpolationTypeObject((SurfaceInterpolationType)value, diagnostics, context);
            case Gml311Package.SYMBOL_TYPE_ENUMERATION_OBJECT:
                return validateSymbolTypeEnumerationObject((SymbolTypeEnumeration)value, diagnostics, context);
            case Gml311Package.TIME_INDETERMINATE_VALUE_TYPE_OBJECT:
                return validateTimeIndeterminateValueTypeObject((TimeIndeterminateValueType)value, diagnostics, context);
            case Gml311Package.TIME_POSITION_UNION:
                return validateTimePositionUnion(value, diagnostics, context);
            case Gml311Package.TIME_UNIT_TYPE:
                return validateTimeUnitType(value, diagnostics, context);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER0_OBJECT:
                return validateTimeUnitTypeMember0Object((TimeUnitTypeMember0)value, diagnostics, context);
            case Gml311Package.TIME_UNIT_TYPE_MEMBER1:
                return validateTimeUnitTypeMember1((String)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbsoluteExternalPositionalAccuracyType(AbsoluteExternalPositionalAccuracyType absoluteExternalPositionalAccuracyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(absoluteExternalPositionalAccuracyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractContinuousCoverageType(AbstractContinuousCoverageType abstractContinuousCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractContinuousCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractCoordinateOperationBaseType(AbstractCoordinateOperationBaseType abstractCoordinateOperationBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractCoordinateOperationBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractCoordinateOperationType(AbstractCoordinateOperationType abstractCoordinateOperationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractCoordinateOperationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractCoordinateSystemBaseType(AbstractCoordinateSystemBaseType abstractCoordinateSystemBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractCoordinateSystemBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractCoordinateSystemType(AbstractCoordinateSystemType abstractCoordinateSystemType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractCoordinateSystemType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractCoverageType(AbstractCoverageType abstractCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractCurveSegmentType(AbstractCurveSegmentType abstractCurveSegmentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractCurveSegmentType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractCurveType(AbstractCurveType abstractCurveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractCurveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractDatumBaseType(AbstractDatumBaseType abstractDatumBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractDatumBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractDatumType(AbstractDatumType abstractDatumType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractDatumType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractDiscreteCoverageType(AbstractDiscreteCoverageType abstractDiscreteCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractDiscreteCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractFeatureCollectionType(AbstractFeatureCollectionType abstractFeatureCollectionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractFeatureCollectionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractFeatureType(AbstractFeatureType abstractFeatureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractFeatureType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeneralConversionType(AbstractGeneralConversionType abstractGeneralConversionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeneralConversionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeneralDerivedCRSType(AbstractGeneralDerivedCRSType abstractGeneralDerivedCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeneralDerivedCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeneralOperationParameterRefType(AbstractGeneralOperationParameterRefType abstractGeneralOperationParameterRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeneralOperationParameterRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeneralOperationParameterType(AbstractGeneralOperationParameterType abstractGeneralOperationParameterType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeneralOperationParameterType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeneralParameterValueType(AbstractGeneralParameterValueType abstractGeneralParameterValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeneralParameterValueType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeneralTransformationType(AbstractGeneralTransformationType abstractGeneralTransformationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeneralTransformationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeometricAggregateType(AbstractGeometricAggregateType abstractGeometricAggregateType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeometricAggregateType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeometricPrimitiveType(AbstractGeometricPrimitiveType abstractGeometricPrimitiveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeometricPrimitiveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGeometryType(AbstractGeometryType abstractGeometryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGeometryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGMLType(AbstractGMLType abstractGMLType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGMLType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractGriddedSurfaceType(AbstractGriddedSurfaceType abstractGriddedSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractGriddedSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractMetaDataType(AbstractMetaDataType abstractMetaDataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractMetaDataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractParametricCurveSurfaceType(AbstractParametricCurveSurfaceType abstractParametricCurveSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractParametricCurveSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractPositionalAccuracyType(AbstractPositionalAccuracyType abstractPositionalAccuracyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractPositionalAccuracyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractReferenceSystemBaseType(AbstractReferenceSystemBaseType abstractReferenceSystemBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractReferenceSystemBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractReferenceSystemType(AbstractReferenceSystemType abstractReferenceSystemType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractReferenceSystemType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractRingPropertyType(AbstractRingPropertyType abstractRingPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractRingPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractRingType(AbstractRingType abstractRingType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractRingType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractSolidType(AbstractSolidType abstractSolidType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractSolidType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractStyleType(AbstractStyleType abstractStyleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractStyleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractSurfacePatchType(AbstractSurfacePatchType abstractSurfacePatchType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractSurfacePatchType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractSurfaceType(AbstractSurfaceType abstractSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTimeComplexType(AbstractTimeComplexType abstractTimeComplexType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTimeComplexType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTimeGeometricPrimitiveType(AbstractTimeGeometricPrimitiveType abstractTimeGeometricPrimitiveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTimeGeometricPrimitiveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTimeObjectType(AbstractTimeObjectType abstractTimeObjectType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTimeObjectType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTimePrimitiveType(AbstractTimePrimitiveType abstractTimePrimitiveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTimePrimitiveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTimeReferenceSystemType(AbstractTimeReferenceSystemType abstractTimeReferenceSystemType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTimeReferenceSystemType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTimeSliceType(AbstractTimeSliceType abstractTimeSliceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTimeSliceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTimeTopologyPrimitiveType(AbstractTimeTopologyPrimitiveType abstractTimeTopologyPrimitiveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTimeTopologyPrimitiveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTopologyType(AbstractTopologyType abstractTopologyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTopologyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTopoPrimitiveType(AbstractTopoPrimitiveType abstractTopoPrimitiveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTopoPrimitiveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAffinePlacementType(AffinePlacementType affinePlacementType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(affinePlacementType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAngleChoiceType(AngleChoiceType angleChoiceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(angleChoiceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAngleType(AngleType angleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(angleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcByBulgeType(ArcByBulgeType arcByBulgeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(arcByBulgeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcByCenterPointType(ArcByCenterPointType arcByCenterPointType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(arcByCenterPointType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcStringByBulgeType(ArcStringByBulgeType arcStringByBulgeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(arcStringByBulgeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcStringType(ArcStringType arcStringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(arcStringType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcType(ArcType arcType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(arcType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAreaType(AreaType areaType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(areaType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArrayAssociationType(ArrayAssociationType arrayAssociationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(arrayAssociationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArrayType(ArrayType arrayType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(arrayType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAssociationType(AssociationType associationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(associationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBagType(BagType bagType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(bagType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBaseStyleDescriptorType(BaseStyleDescriptorType baseStyleDescriptorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(baseStyleDescriptorType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBaseUnitType(BaseUnitType baseUnitType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(baseUnitType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBezierType(BezierType bezierType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(bezierType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBooleanPropertyType(BooleanPropertyType booleanPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(booleanPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBoundedFeatureType(BoundedFeatureType boundedFeatureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(boundedFeatureType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBoundingShapeType(BoundingShapeType boundingShapeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(boundingShapeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBSplineType(BSplineType bSplineType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(bSplineType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCartesianCSRefType(CartesianCSRefType cartesianCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(cartesianCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCartesianCSType(CartesianCSType cartesianCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(cartesianCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCategoryExtentType(CategoryExtentType categoryExtentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(categoryExtentType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCategoryPropertyType(CategoryPropertyType categoryPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(categoryPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCircleByCenterPointType(CircleByCenterPointType circleByCenterPointType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(circleByCenterPointType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCircleType(CircleType circleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(circleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateClothoidType(ClothoidType clothoidType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(clothoidType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCodeListType(CodeListType codeListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(codeListType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCodeOrNullListType(CodeOrNullListType codeOrNullListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(codeOrNullListType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCodeType(CodeType codeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(codeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompositeCurvePropertyType(CompositeCurvePropertyType compositeCurvePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compositeCurvePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompositeCurveType(CompositeCurveType compositeCurveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compositeCurveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompositeSolidPropertyType(CompositeSolidPropertyType compositeSolidPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compositeSolidPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompositeSolidType(CompositeSolidType compositeSolidType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compositeSolidType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompositeSurfacePropertyType(CompositeSurfacePropertyType compositeSurfacePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compositeSurfacePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompositeSurfaceType(CompositeSurfaceType compositeSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compositeSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompositeValueType(CompositeValueType compositeValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compositeValueType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompoundCRSRefType(CompoundCRSRefType compoundCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compoundCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompoundCRSType(CompoundCRSType compoundCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(compoundCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConcatenatedOperationRefType(ConcatenatedOperationRefType concatenatedOperationRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(concatenatedOperationRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConcatenatedOperationType(ConcatenatedOperationType concatenatedOperationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(concatenatedOperationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConeType(ConeType coneType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coneType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateContainerPropertyType(ContainerPropertyType containerPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(containerPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateControlPointType(ControlPointType controlPointType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(controlPointType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConventionalUnitType(ConventionalUnitType conventionalUnitType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(conventionalUnitType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConversionRefType(ConversionRefType conversionRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(conversionRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConversionToPreferredUnitType(ConversionToPreferredUnitType conversionToPreferredUnitType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(conversionToPreferredUnitType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConversionType(ConversionType conversionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(conversionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoordinateOperationRefType(CoordinateOperationRefType coordinateOperationRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coordinateOperationRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoordinateReferenceSystemRefType(CoordinateReferenceSystemRefType coordinateReferenceSystemRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coordinateReferenceSystemRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoordinatesType(CoordinatesType coordinatesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coordinatesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoordinateSystemAxisBaseType(CoordinateSystemAxisBaseType coordinateSystemAxisBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coordinateSystemAxisBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoordinateSystemAxisRefType(CoordinateSystemAxisRefType coordinateSystemAxisRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coordinateSystemAxisRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoordinateSystemAxisType(CoordinateSystemAxisType coordinateSystemAxisType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coordinateSystemAxisType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoordinateSystemRefType(CoordinateSystemRefType coordinateSystemRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coordinateSystemRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoordType(CoordType coordType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coordType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCountPropertyType(CountPropertyType countPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(countPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCovarianceElementType(CovarianceElementType covarianceElementType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(covarianceElementType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCovarianceMatrixType(CovarianceMatrixType covarianceMatrixType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(covarianceMatrixType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageFunctionType(CoverageFunctionType coverageFunctionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coverageFunctionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCRSRefType(CRSRefType crsRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(crsRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCubicSplineType(CubicSplineType cubicSplineType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(cubicSplineType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCurveArrayPropertyType(CurveArrayPropertyType curveArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(curveArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCurvePropertyType(CurvePropertyType curvePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(curvePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCurveSegmentArrayPropertyType(CurveSegmentArrayPropertyType curveSegmentArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(curveSegmentArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCurveType(CurveType curveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(curveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCylinderType(CylinderType cylinderType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(cylinderType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCylindricalCSRefType(CylindricalCSRefType cylindricalCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(cylindricalCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCylindricalCSType(CylindricalCSType cylindricalCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(cylindricalCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDataBlockType(DataBlockType dataBlockType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dataBlockType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDatumRefType(DatumRefType datumRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(datumRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDefaultStylePropertyType(DefaultStylePropertyType defaultStylePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(defaultStylePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDefinitionProxyType(DefinitionProxyType definitionProxyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(definitionProxyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDefinitionType(DefinitionType definitionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(definitionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDegreesType(DegreesType degreesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(degreesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDerivationUnitTermType(DerivationUnitTermType derivationUnitTermType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(derivationUnitTermType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDerivedCRSRefType(DerivedCRSRefType derivedCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(derivedCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDerivedCRSType(DerivedCRSType derivedCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(derivedCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDerivedCRSTypeType(DerivedCRSTypeType derivedCRSTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(derivedCRSTypeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDerivedUnitType(DerivedUnitType derivedUnitType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(derivedUnitType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDictionaryEntryType(DictionaryEntryType dictionaryEntryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dictionaryEntryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDictionaryType(DictionaryType dictionaryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dictionaryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectedEdgePropertyType(DirectedEdgePropertyType directedEdgePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directedEdgePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectedFacePropertyType(DirectedFacePropertyType directedFacePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directedFacePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectedNodePropertyType(DirectedNodePropertyType directedNodePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directedNodePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectedObservationAtDistanceType(DirectedObservationAtDistanceType directedObservationAtDistanceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directedObservationAtDistanceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectedObservationType(DirectedObservationType directedObservationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directedObservationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectedTopoSolidPropertyType(DirectedTopoSolidPropertyType directedTopoSolidPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directedTopoSolidPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectionPropertyType(DirectionPropertyType directionPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directionPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectionVectorType(DirectionVectorType directionVectorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directionVectorType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectPositionListType(DirectPositionListType directPositionListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directPositionListType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectPositionType(DirectPositionType directPositionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(directPositionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDMSAngleType(DMSAngleType dmsAngleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dmsAngleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDomainSetType(DomainSetType domainSetType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(domainSetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDynamicFeatureCollectionType(DynamicFeatureCollectionType dynamicFeatureCollectionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dynamicFeatureCollectionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDynamicFeatureType(DynamicFeatureType dynamicFeatureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dynamicFeatureType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEdgeType(EdgeType edgeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(edgeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEllipsoidalCSRefType(EllipsoidalCSRefType ellipsoidalCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(ellipsoidalCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEllipsoidalCSType(EllipsoidalCSType ellipsoidalCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(ellipsoidalCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEllipsoidBaseType(EllipsoidBaseType ellipsoidBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(ellipsoidBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEllipsoidRefType(EllipsoidRefType ellipsoidRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(ellipsoidRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEllipsoidType(EllipsoidType ellipsoidType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(ellipsoidType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEngineeringCRSRefType(EngineeringCRSRefType engineeringCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(engineeringCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEngineeringCRSType(EngineeringCRSType engineeringCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(engineeringCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEngineeringDatumRefType(EngineeringDatumRefType engineeringDatumRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(engineeringDatumRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEngineeringDatumType(EngineeringDatumType engineeringDatumType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(engineeringDatumType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEnvelopeType(EnvelopeType envelopeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(envelopeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEnvelopeWithTimePeriodType(EnvelopeWithTimePeriodType envelopeWithTimePeriodType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(envelopeWithTimePeriodType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExtentType(ExtentType extentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(extentType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFaceType(FaceType faceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(faceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureArrayPropertyType(FeatureArrayPropertyType featureArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featureArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureCollectionType(FeatureCollectionType featureCollectionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featureCollectionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeaturePropertyType(FeaturePropertyType featurePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featurePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureStylePropertyType(FeatureStylePropertyType featureStylePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featureStylePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureStyleType(FeatureStyleType featureStyleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featureStyleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFileType(FileType fileType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(fileType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFormulaType(FormulaType formulaType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(formulaType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeneralConversionRefType(GeneralConversionRefType generalConversionRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(generalConversionRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeneralTransformationRefType(GeneralTransformationRefType generalTransformationRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(generalTransformationRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGenericMetaDataType(GenericMetaDataType genericMetaDataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(genericMetaDataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeocentricCRSRefType(GeocentricCRSRefType geocentricCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geocentricCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeocentricCRSType(GeocentricCRSType geocentricCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geocentricCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeodesicStringType(GeodesicStringType geodesicStringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geodesicStringType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeodesicType(GeodesicType geodesicType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geodesicType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeodeticDatumRefType(GeodeticDatumRefType geodeticDatumRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geodeticDatumRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeodeticDatumType(GeodeticDatumType geodeticDatumType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geodeticDatumType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeographicCRSRefType(GeographicCRSRefType geographicCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geographicCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeographicCRSType(GeographicCRSType geographicCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geographicCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometricComplexPropertyType(GeometricComplexPropertyType geometricComplexPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometricComplexPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometricComplexType(GeometricComplexType geometricComplexType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometricComplexType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometricPrimitivePropertyType(GeometricPrimitivePropertyType geometricPrimitivePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometricPrimitivePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometryArrayPropertyType(GeometryArrayPropertyType geometryArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometryArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometryPropertyType(GeometryPropertyType geometryPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometryPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometryStylePropertyType(GeometryStylePropertyType geometryStylePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometryStylePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometryStyleType(GeometryStyleType geometryStyleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometryStyleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGraphStylePropertyType(GraphStylePropertyType graphStylePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(graphStylePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGraphStyleType(GraphStyleType graphStyleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(graphStyleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGridCoverageType(GridCoverageType gridCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(gridCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGridDomainType(GridDomainType gridDomainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(gridDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGridEnvelopeType(GridEnvelopeType gridEnvelopeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(gridEnvelopeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGridFunctionType(GridFunctionType gridFunctionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(gridFunctionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGridLengthType(GridLengthType gridLengthType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(gridLengthType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGridLimitsType(GridLimitsType gridLimitsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(gridLimitsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGridType(GridType gridType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(gridType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateHistoryPropertyType(HistoryPropertyType historyPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(historyPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIdentifierType(IdentifierType identifierType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(identifierType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateImageCRSRefType(ImageCRSRefType imageCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(imageCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateImageCRSType(ImageCRSType imageCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(imageCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateImageDatumRefType(ImageDatumRefType imageDatumRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(imageDatumRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateImageDatumType(ImageDatumType imageDatumType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(imageDatumType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIndexMapType(IndexMapType indexMapType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(indexMapType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIndirectEntryType(IndirectEntryType indirectEntryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(indirectEntryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIsolatedPropertyType(IsolatedPropertyType isolatedPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(isolatedPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateKnotPropertyType(KnotPropertyType knotPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(knotPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateKnotType(KnotType knotType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(knotType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLabelStylePropertyType(LabelStylePropertyType labelStylePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(labelStylePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLabelStyleType(LabelStyleType labelStyleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(labelStyleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLabelType(LabelType labelType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(labelType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLengthType(LengthType lengthType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(lengthType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLinearCSRefType(LinearCSRefType linearCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(linearCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLinearCSType(LinearCSType linearCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(linearCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLinearRingPropertyType(LinearRingPropertyType linearRingPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(linearRingPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLinearRingType(LinearRingType linearRingType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(linearRingType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLineStringPropertyType(LineStringPropertyType lineStringPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(lineStringPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLineStringSegmentArrayPropertyType(LineStringSegmentArrayPropertyType lineStringSegmentArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(lineStringSegmentArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLineStringSegmentType(LineStringSegmentType lineStringSegmentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(lineStringSegmentType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLineStringType(LineStringType lineStringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(lineStringType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLocationPropertyType(LocationPropertyType locationPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(locationPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMeasureListType(MeasureListType measureListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(measureListType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMeasureOrNullListType(MeasureOrNullListType measureOrNullListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(measureOrNullListType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMeasureType(MeasureType measureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(measureType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMetaDataPropertyType(MetaDataPropertyType metaDataPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(metaDataPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMovingObjectStatusType(MovingObjectStatusType movingObjectStatusType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(movingObjectStatusType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiCurveCoverageType(MultiCurveCoverageType multiCurveCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiCurveCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiCurveDomainType(MultiCurveDomainType multiCurveDomainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiCurveDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiCurvePropertyType(MultiCurvePropertyType multiCurvePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiCurvePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiCurveType(MultiCurveType multiCurveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiCurveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiGeometryPropertyType(MultiGeometryPropertyType multiGeometryPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiGeometryPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiGeometryType(MultiGeometryType multiGeometryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiGeometryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiLineStringPropertyType(MultiLineStringPropertyType multiLineStringPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiLineStringPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiLineStringType(MultiLineStringType multiLineStringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiLineStringType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiPointCoverageType(MultiPointCoverageType multiPointCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiPointCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiPointDomainType(MultiPointDomainType multiPointDomainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiPointDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiPointPropertyType(MultiPointPropertyType multiPointPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiPointPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiPointType(MultiPointType multiPointType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiPointType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiPolygonPropertyType(MultiPolygonPropertyType multiPolygonPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiPolygonPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiPolygonType(MultiPolygonType multiPolygonType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiPolygonType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiSolidCoverageType(MultiSolidCoverageType multiSolidCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiSolidCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiSolidDomainType(MultiSolidDomainType multiSolidDomainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiSolidDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiSolidPropertyType(MultiSolidPropertyType multiSolidPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiSolidPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiSolidType(MultiSolidType multiSolidType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiSolidType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiSurfaceCoverageType(MultiSurfaceCoverageType multiSurfaceCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiSurfaceCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiSurfaceDomainType(MultiSurfaceDomainType multiSurfaceDomainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiSurfaceDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiSurfacePropertyType(MultiSurfacePropertyType multiSurfacePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiSurfacePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMultiSurfaceType(MultiSurfaceType multiSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(multiSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNodeType(NodeType nodeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(nodeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateObliqueCartesianCSRefType(ObliqueCartesianCSRefType obliqueCartesianCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(obliqueCartesianCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateObliqueCartesianCSType(ObliqueCartesianCSType obliqueCartesianCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(obliqueCartesianCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateObservationType(ObservationType observationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(observationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOffsetCurveType(OffsetCurveType offsetCurveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(offsetCurveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationMethodBaseType(OperationMethodBaseType operationMethodBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationMethodBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationMethodRefType(OperationMethodRefType operationMethodRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationMethodRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationMethodType(OperationMethodType operationMethodType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationMethodType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationParameterBaseType(OperationParameterBaseType operationParameterBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationParameterBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationParameterGroupBaseType(OperationParameterGroupBaseType operationParameterGroupBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationParameterGroupBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationParameterGroupRefType(OperationParameterGroupRefType operationParameterGroupRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationParameterGroupRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationParameterGroupType(OperationParameterGroupType operationParameterGroupType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationParameterGroupType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationParameterRefType(OperationParameterRefType operationParameterRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationParameterRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationParameterType(OperationParameterType operationParameterType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationParameterType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationRefType(OperationRefType operationRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOrientableCurveType(OrientableCurveType orientableCurveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(orientableCurveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOrientableSurfaceType(OrientableSurfaceType orientableSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(orientableSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateParameterValueGroupType(ParameterValueGroupType parameterValueGroupType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(parameterValueGroupType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateParameterValueType(ParameterValueType parameterValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(parameterValueType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePassThroughOperationRefType(PassThroughOperationRefType passThroughOperationRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(passThroughOperationRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePassThroughOperationType(PassThroughOperationType passThroughOperationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(passThroughOperationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePixelInCellType(PixelInCellType pixelInCellType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(pixelInCellType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePointArrayPropertyType(PointArrayPropertyType pointArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(pointArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePointPropertyType(PointPropertyType pointPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(pointPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePointType(PointType pointType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(pointType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePolarCSRefType(PolarCSRefType polarCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(polarCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePolarCSType(PolarCSType polarCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(polarCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePolygonPatchArrayPropertyType(PolygonPatchArrayPropertyType polygonPatchArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(polygonPatchArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePolygonPatchType(PolygonPatchType polygonPatchType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(polygonPatchType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePolygonPropertyType(PolygonPropertyType polygonPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(polygonPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePolygonType(PolygonType polygonType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(polygonType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePolyhedralSurfaceType(PolyhedralSurfaceType polyhedralSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(polyhedralSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePrimeMeridianBaseType(PrimeMeridianBaseType primeMeridianBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(primeMeridianBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePrimeMeridianRefType(PrimeMeridianRefType primeMeridianRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(primeMeridianRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePrimeMeridianType(PrimeMeridianType primeMeridianType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(primeMeridianType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePriorityLocationPropertyType(PriorityLocationPropertyType priorityLocationPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(priorityLocationPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProjectedCRSRefType(ProjectedCRSRefType projectedCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(projectedCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateProjectedCRSType(ProjectedCRSType projectedCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(projectedCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQuantityExtentType(QuantityExtentType quantityExtentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(quantityExtentType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQuantityPropertyType(QuantityPropertyType quantityPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(quantityPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRangeParametersType(RangeParametersType rangeParametersType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(rangeParametersType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRangeSetType(RangeSetType rangeSetType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(rangeSetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRectangleType(RectangleType rectangleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(rectangleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRectifiedGridCoverageType(RectifiedGridCoverageType rectifiedGridCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(rectifiedGridCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRectifiedGridDomainType(RectifiedGridDomainType rectifiedGridDomainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(rectifiedGridDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRectifiedGridType(RectifiedGridType rectifiedGridType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(rectifiedGridType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReferenceSystemRefType(ReferenceSystemRefType referenceSystemRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(referenceSystemRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReferenceType(ReferenceType referenceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(referenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRefLocationType(RefLocationType refLocationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(refLocationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRelatedTimeType(RelatedTimeType relatedTimeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(relatedTimeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRelativeInternalPositionalAccuracyType(RelativeInternalPositionalAccuracyType relativeInternalPositionalAccuracyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(relativeInternalPositionalAccuracyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRingPropertyType(RingPropertyType ringPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(ringPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRingType(RingType ringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(ringType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRowType(RowType rowType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(rowType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateScalarValuePropertyType(ScalarValuePropertyType scalarValuePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(scalarValuePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateScaleType(ScaleType scaleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(scaleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSecondDefiningParameterType(SecondDefiningParameterType secondDefiningParameterType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(secondDefiningParameterType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSequenceRuleType(SequenceRuleType sequenceRuleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(sequenceRuleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSingleOperationRefType(SingleOperationRefType singleOperationRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(singleOperationRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSolidArrayPropertyType(SolidArrayPropertyType solidArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(solidArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSolidPropertyType(SolidPropertyType solidPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(solidPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSolidType(SolidType solidType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(solidType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpeedType(SpeedType speedType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(speedType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSphereType(SphereType sphereType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(sphereType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSphericalCSRefType(SphericalCSRefType sphericalCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(sphericalCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSphericalCSType(SphericalCSType sphericalCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(sphericalCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStringOrRefType(StringOrRefType stringOrRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(stringOrRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStyleType(StyleType styleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(styleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStyleVariationType(StyleVariationType styleVariationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(styleVariationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSurfaceArrayPropertyType(SurfaceArrayPropertyType surfaceArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(surfaceArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSurfacePatchArrayPropertyType(SurfacePatchArrayPropertyType surfacePatchArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(surfacePatchArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSurfacePropertyType(SurfacePropertyType surfacePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(surfacePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSurfaceType(SurfaceType surfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(surfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSymbolType(SymbolType symbolType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(symbolType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTargetPropertyType(TargetPropertyType targetPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(targetPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalCRSRefType(TemporalCRSRefType temporalCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalCRSType(TemporalCRSType temporalCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalCSRefType(TemporalCSRefType temporalCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalCSType(TemporalCSType temporalCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalDatumBaseType(TemporalDatumBaseType temporalDatumBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalDatumBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalDatumRefType(TemporalDatumRefType temporalDatumRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalDatumRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalDatumType(TemporalDatumType temporalDatumType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalDatumType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeCalendarEraPropertyType(TimeCalendarEraPropertyType timeCalendarEraPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeCalendarEraPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeCalendarEraType(TimeCalendarEraType timeCalendarEraType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeCalendarEraType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeCalendarPropertyType(TimeCalendarPropertyType timeCalendarPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeCalendarPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeCalendarType(TimeCalendarType timeCalendarType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeCalendarType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeClockPropertyType(TimeClockPropertyType timeClockPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeClockPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeClockType(TimeClockType timeClockType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeClockType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeCoordinateSystemType(TimeCoordinateSystemType timeCoordinateSystemType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeCoordinateSystemType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeEdgePropertyType(TimeEdgePropertyType timeEdgePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeEdgePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeEdgeType(TimeEdgeType timeEdgeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeEdgeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeGeometricPrimitivePropertyType(TimeGeometricPrimitivePropertyType timeGeometricPrimitivePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeGeometricPrimitivePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeInstantPropertyType(TimeInstantPropertyType timeInstantPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeInstantPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeInstantType(TimeInstantType timeInstantType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeInstantType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeIntervalLengthType(TimeIntervalLengthType timeIntervalLengthType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeIntervalLengthType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeNodePropertyType(TimeNodePropertyType timeNodePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeNodePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeNodeType(TimeNodeType timeNodeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeNodeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeOrdinalEraPropertyType(TimeOrdinalEraPropertyType timeOrdinalEraPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeOrdinalEraPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeOrdinalEraType(TimeOrdinalEraType timeOrdinalEraType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeOrdinalEraType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeOrdinalReferenceSystemType(TimeOrdinalReferenceSystemType timeOrdinalReferenceSystemType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeOrdinalReferenceSystemType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimePeriodPropertyType(TimePeriodPropertyType timePeriodPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timePeriodPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimePeriodType(TimePeriodType timePeriodType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timePeriodType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimePositionType(TimePositionType timePositionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timePositionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimePrimitivePropertyType(TimePrimitivePropertyType timePrimitivePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timePrimitivePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeTopologyComplexPropertyType(TimeTopologyComplexPropertyType timeTopologyComplexPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeTopologyComplexPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeTopologyComplexType(TimeTopologyComplexType timeTopologyComplexType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeTopologyComplexType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeTopologyPrimitivePropertyType(TimeTopologyPrimitivePropertyType timeTopologyPrimitivePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeTopologyPrimitivePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeType(TimeType timeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(timeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTinType(TinType tinType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(tinType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoComplexMemberType(TopoComplexMemberType topoComplexMemberType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoComplexMemberType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoComplexType(TopoComplexType topoComplexType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoComplexType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoCurvePropertyType(TopoCurvePropertyType topoCurvePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoCurvePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoCurveType(TopoCurveType topoCurveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoCurveType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopologyStylePropertyType(TopologyStylePropertyType topologyStylePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topologyStylePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopologyStyleType(TopologyStyleType topologyStyleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topologyStyleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoPointPropertyType(TopoPointPropertyType topoPointPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoPointPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoPointType(TopoPointType topoPointType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoPointType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoPrimitiveArrayAssociationType(TopoPrimitiveArrayAssociationType topoPrimitiveArrayAssociationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoPrimitiveArrayAssociationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoPrimitiveMemberType(TopoPrimitiveMemberType topoPrimitiveMemberType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoPrimitiveMemberType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoSolidType(TopoSolidType topoSolidType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoSolidType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoSurfacePropertyType(TopoSurfacePropertyType topoSurfacePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoSurfacePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoSurfaceType(TopoSurfaceType topoSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoVolumePropertyType(TopoVolumePropertyType topoVolumePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoVolumePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTopoVolumeType(TopoVolumeType topoVolumeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(topoVolumeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTrackType(TrackType trackType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(trackType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTransformationRefType(TransformationRefType transformationRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(transformationRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTransformationType(TransformationType transformationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(transformationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTrianglePatchArrayPropertyType(TrianglePatchArrayPropertyType trianglePatchArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(trianglePatchArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTriangleType(TriangleType triangleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(triangleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTriangulatedSurfaceType(TriangulatedSurfaceType triangulatedSurfaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(triangulatedSurfaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnitDefinitionType(UnitDefinitionType unitDefinitionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(unitDefinitionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnitOfMeasureType(UnitOfMeasureType unitOfMeasureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(unitOfMeasureType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUserDefinedCSRefType(UserDefinedCSRefType userDefinedCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(userDefinedCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUserDefinedCSType(UserDefinedCSType userDefinedCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(userDefinedCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValueArrayPropertyType(ValueArrayPropertyType valueArrayPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(valueArrayPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValueArrayType(ValueArrayType valueArrayType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(valueArrayType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValuePropertyType(ValuePropertyType valuePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(valuePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVectorType(VectorType vectorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(vectorType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVerticalCRSRefType(VerticalCRSRefType verticalCRSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(verticalCRSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVerticalCRSType(VerticalCRSType verticalCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(verticalCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVerticalCSRefType(VerticalCSRefType verticalCSRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(verticalCSRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVerticalCSType(VerticalCSType verticalCSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(verticalCSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVerticalDatumRefType(VerticalDatumRefType verticalDatumRefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(verticalDatumRefType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVerticalDatumType(VerticalDatumType verticalDatumType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(verticalDatumType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVerticalDatumTypeType(VerticalDatumTypeType verticalDatumTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(verticalDatumTypeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVolumeType(VolumeType volumeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(volumeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAesheticCriteriaType(AesheticCriteriaType aesheticCriteriaType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompassPointEnumeration(CompassPointEnumeration compassPointEnumeration, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCurveInterpolationType(CurveInterpolationType curveInterpolationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectionTypeMember0(DirectionTypeMember0 directionTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDrawingTypeType(DrawingTypeType drawingTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFileValueModelType(FileValueModelType fileValueModelType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGraphTypeType(GraphTypeType graphTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIncrementOrder(IncrementOrder incrementOrder, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIsSphereType(IsSphereType isSphereType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateKnotTypesType(KnotTypesType knotTypesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLineTypeType(LineTypeType lineTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNullEnumerationMember0(NullEnumerationMember0 nullEnumerationMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQueryGrammarEnumeration(QueryGrammarEnumeration queryGrammarEnumeration, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRelativePositionType(RelativePositionType relativePositionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSequenceRuleNames(SequenceRuleNames sequenceRuleNames, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSignType(SignType signType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSuccessionType(SuccessionType successionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSurfaceInterpolationType(SurfaceInterpolationType surfaceInterpolationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSymbolTypeEnumeration(SymbolTypeEnumeration symbolTypeEnumeration, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeIndeterminateValueType(TimeIndeterminateValueType timeIndeterminateValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeUnitTypeMember0(TimeUnitTypeMember0 timeUnitTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAesheticCriteriaTypeObject(AesheticCriteriaType aesheticCriteriaTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcMinutesType(BigInteger arcMinutesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = xmlTypeValidator.validateNonNegativeInteger_Min(arcMinutesType, diagnostics, context);
        if (result || diagnostics != null) result &= validateArcMinutesType_Max(arcMinutesType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateArcMinutesType_Max
     */
    public static final BigInteger ARC_MINUTES_TYPE__MAX__VALUE = new BigInteger("59");

    /**
     * Validates the Max constraint of '<em>Arc Minutes Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcMinutesType_Max(BigInteger arcMinutesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = arcMinutesType.compareTo(ARC_MINUTES_TYPE__MAX__VALUE) <= 0;
        if (!result && diagnostics != null)
            reportMaxViolation(Gml311Package.eINSTANCE.getArcMinutesType(), arcMinutesType, ARC_MINUTES_TYPE__MAX__VALUE, true, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcSecondsType(BigDecimal arcSecondsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateArcSecondsType_Min(arcSecondsType, diagnostics, context);
        if (result || diagnostics != null) result &= validateArcSecondsType_Max(arcSecondsType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateArcSecondsType_Min
     */
    public static final BigDecimal ARC_SECONDS_TYPE__MIN__VALUE = new BigDecimal("0.00");

    /**
     * Validates the Min constraint of '<em>Arc Seconds Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcSecondsType_Min(BigDecimal arcSecondsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = arcSecondsType.compareTo(ARC_SECONDS_TYPE__MIN__VALUE) >= 0;
        if (!result && diagnostics != null)
            reportMinViolation(Gml311Package.eINSTANCE.getArcSecondsType(), arcSecondsType, ARC_SECONDS_TYPE__MIN__VALUE, true, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateArcSecondsType_Max
     */
    public static final BigDecimal ARC_SECONDS_TYPE__MAX__VALUE = new BigDecimal("60.00");

    /**
     * Validates the Max constraint of '<em>Arc Seconds Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcSecondsType_Max(BigDecimal arcSecondsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = arcSecondsType.compareTo(ARC_SECONDS_TYPE__MAX__VALUE) < 0;
        if (!result && diagnostics != null)
            reportMaxViolation(Gml311Package.eINSTANCE.getArcSecondsType(), arcSecondsType, ARC_SECONDS_TYPE__MAX__VALUE, false, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBooleanList(List<?> booleanList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateBooleanList_ItemType(booleanList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Boolean List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBooleanList_ItemType(List<?> booleanList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = booleanList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (XMLTypePackage.Literals.BOOLEAN.isInstance(item)) {
                result &= xmlTypeValidator.validateBoolean((Boolean)item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(XMLTypePackage.Literals.BOOLEAN, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBooleanOrNull(Object booleanOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateBooleanOrNull_MemberTypes(booleanOrNull, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Boolean Or Null</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBooleanOrNull_MemberTypes(Object booleanOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(booleanOrNull)) {
                if (validateNullEnumeration(booleanOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.BOOLEAN.isInstance(booleanOrNull)) {
                if (xmlTypeValidator.validateBoolean((Boolean)booleanOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(booleanOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)booleanOrNull, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(booleanOrNull)) {
                if (validateNullEnumeration(booleanOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.BOOLEAN.isInstance(booleanOrNull)) {
                if (xmlTypeValidator.validateBoolean((Boolean)booleanOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(booleanOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)booleanOrNull, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBooleanOrNullList(List<?> booleanOrNullList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateBooleanOrNullList_ItemType(booleanOrNullList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Boolean Or Null List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBooleanOrNullList_ItemType(List<?> booleanOrNullList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = booleanOrNullList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (Gml311Package.eINSTANCE.getBooleanOrNull().isInstance(item)) {
                result &= validateBooleanOrNull(item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(Gml311Package.eINSTANCE.getBooleanOrNull(), item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCalDate(XMLGregorianCalendar calDate, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateCalDate_MemberTypes(calDate, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Cal Date</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCalDate_MemberTypes(XMLGregorianCalendar calDate, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (XMLTypePackage.Literals.DATE.isInstance(calDate)) {
                if (xmlTypeValidator.validateDate(calDate, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.GYEAR_MONTH.isInstance(calDate)) {
                if (xmlTypeValidator.validateGYearMonth(calDate, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.GYEAR.isInstance(calDate)) {
                if (xmlTypeValidator.validateGYear(calDate, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (XMLTypePackage.Literals.DATE.isInstance(calDate)) {
                if (xmlTypeValidator.validateDate(calDate, null, context)) return true;
            }
            if (XMLTypePackage.Literals.GYEAR_MONTH.isInstance(calDate)) {
                if (xmlTypeValidator.validateGYearMonth(calDate, null, context)) return true;
            }
            if (XMLTypePackage.Literals.GYEAR.isInstance(calDate)) {
                if (xmlTypeValidator.validateGYear(calDate, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCompassPointEnumerationObject(CompassPointEnumeration compassPointEnumerationObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCountExtentType(List<?> countExtentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateIntegerOrNullList_ItemType(countExtentType, diagnostics, context);
        if (result || diagnostics != null) result &= validateCountExtentType_MinLength(countExtentType, diagnostics, context);
        if (result || diagnostics != null) result &= validateCountExtentType_MaxLength(countExtentType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MinLength constraint of '<em>Count Extent Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCountExtentType_MinLength(List<?> countExtentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        int length = countExtentType.size();
        boolean result = length >= 2;
        if (!result && diagnostics != null)
            reportMinLengthViolation(Gml311Package.eINSTANCE.getCountExtentType(), countExtentType, length, 2, diagnostics, context);
        return result;
    }

    /**
     * Validates the MaxLength constraint of '<em>Count Extent Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCountExtentType_MaxLength(List<?> countExtentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        int length = countExtentType.size();
        boolean result = length <= 2;
        if (!result && diagnostics != null)
            reportMaxLengthViolation(Gml311Package.eINSTANCE.getCountExtentType(), countExtentType, length, 2, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCurveInterpolationTypeObject(CurveInterpolationType curveInterpolationTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDecimalMinutesType(BigDecimal decimalMinutesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateDecimalMinutesType_Min(decimalMinutesType, diagnostics, context);
        if (result || diagnostics != null) result &= validateDecimalMinutesType_Max(decimalMinutesType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateDecimalMinutesType_Min
     */
    public static final BigDecimal DECIMAL_MINUTES_TYPE__MIN__VALUE = new BigDecimal("0.00");

    /**
     * Validates the Min constraint of '<em>Decimal Minutes Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDecimalMinutesType_Min(BigDecimal decimalMinutesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = decimalMinutesType.compareTo(DECIMAL_MINUTES_TYPE__MIN__VALUE) >= 0;
        if (!result && diagnostics != null)
            reportMinViolation(Gml311Package.eINSTANCE.getDecimalMinutesType(), decimalMinutesType, DECIMAL_MINUTES_TYPE__MIN__VALUE, true, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateDecimalMinutesType_Max
     */
    public static final BigDecimal DECIMAL_MINUTES_TYPE__MAX__VALUE = new BigDecimal("60.00");

    /**
     * Validates the Max constraint of '<em>Decimal Minutes Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDecimalMinutesType_Max(BigDecimal decimalMinutesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = decimalMinutesType.compareTo(DECIMAL_MINUTES_TYPE__MAX__VALUE) < 0;
        if (!result && diagnostics != null)
            reportMaxViolation(Gml311Package.eINSTANCE.getDecimalMinutesType(), decimalMinutesType, DECIMAL_MINUTES_TYPE__MAX__VALUE, false, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDegreeValueType(BigInteger degreeValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = xmlTypeValidator.validateNonNegativeInteger_Min(degreeValueType, diagnostics, context);
        if (result || diagnostics != null) result &= validateDegreeValueType_Max(degreeValueType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateDegreeValueType_Max
     */
    public static final BigInteger DEGREE_VALUE_TYPE__MAX__VALUE = new BigInteger("359");

    /**
     * Validates the Max constraint of '<em>Degree Value Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDegreeValueType_Max(BigInteger degreeValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = degreeValueType.compareTo(DEGREE_VALUE_TYPE__MAX__VALUE) <= 0;
        if (!result && diagnostics != null)
            reportMaxViolation(Gml311Package.eINSTANCE.getDegreeValueType(), degreeValueType, DEGREE_VALUE_TYPE__MAX__VALUE, true, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectionType(Enumerator directionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateDirectionType_MemberTypes(directionType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Direction Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectionType_MemberTypes(Enumerator directionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getDirectionTypeMember0().isInstance(directionType)) {
                if (validateDirectionTypeMember0((DirectionTypeMember0)directionType, tempDiagnostics, context)) return true;
            }
            if (Gml311Package.eINSTANCE.getDirectionTypeMember1().isInstance(directionType)) {
                if (validateDirectionTypeMember1((SignType)directionType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getDirectionTypeMember0().isInstance(directionType)) {
                if (validateDirectionTypeMember0((DirectionTypeMember0)directionType, null, context)) return true;
            }
            if (Gml311Package.eINSTANCE.getDirectionTypeMember1().isInstance(directionType)) {
                if (validateDirectionTypeMember1((SignType)directionType, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectionTypeMember0Object(DirectionTypeMember0 directionTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDirectionTypeMember1(SignType directionTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDoubleList(List<?> doubleList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateDoubleList_ItemType(doubleList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Double List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDoubleList_ItemType(List<?> doubleList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = doubleList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (XMLTypePackage.Literals.DOUBLE.isInstance(item)) {
                result &= xmlTypeValidator.validateDouble((Double)item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(XMLTypePackage.Literals.DOUBLE, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDoubleOrNull(Object doubleOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateDoubleOrNull_MemberTypes(doubleOrNull, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Double Or Null</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDoubleOrNull_MemberTypes(Object doubleOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(doubleOrNull)) {
                if (validateNullEnumeration(doubleOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.DOUBLE.isInstance(doubleOrNull)) {
                if (xmlTypeValidator.validateDouble((Double)doubleOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(doubleOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)doubleOrNull, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(doubleOrNull)) {
                if (validateNullEnumeration(doubleOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.DOUBLE.isInstance(doubleOrNull)) {
                if (xmlTypeValidator.validateDouble((Double)doubleOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(doubleOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)doubleOrNull, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDoubleOrNullList(List<?> doubleOrNullList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateDoubleOrNullList_ItemType(doubleOrNullList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Double Or Null List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDoubleOrNullList_ItemType(List<?> doubleOrNullList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = doubleOrNullList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (Gml311Package.eINSTANCE.getDoubleOrNull().isInstance(item)) {
                result &= validateDoubleOrNull(item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(Gml311Package.eINSTANCE.getDoubleOrNull(), item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDrawingTypeTypeObject(DrawingTypeType drawingTypeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFileValueModelTypeObject(FileValueModelType fileValueModelTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGraphTypeTypeObject(GraphTypeType graphTypeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIncrementOrderObject(IncrementOrder incrementOrderObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIntegerList(List<?> integerList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateIntegerList_ItemType(integerList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Integer List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIntegerList_ItemType(List<?> integerList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = integerList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (XMLTypePackage.Literals.INTEGER.isInstance(item)) {
                result &= xmlTypeValidator.validateInteger((BigInteger)item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(XMLTypePackage.Literals.INTEGER, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIntegerOrNull(Object integerOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateIntegerOrNull_MemberTypes(integerOrNull, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Integer Or Null</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIntegerOrNull_MemberTypes(Object integerOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(integerOrNull)) {
                if (validateNullEnumeration(integerOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.INTEGER.isInstance(integerOrNull)) {
                if (xmlTypeValidator.validateInteger((BigInteger)integerOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(integerOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)integerOrNull, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(integerOrNull)) {
                if (validateNullEnumeration(integerOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.INTEGER.isInstance(integerOrNull)) {
                if (xmlTypeValidator.validateInteger((BigInteger)integerOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(integerOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)integerOrNull, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIntegerOrNullList(List<?> integerOrNullList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateIntegerOrNullList_ItemType(integerOrNullList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Integer Or Null List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIntegerOrNullList_ItemType(List<?> integerOrNullList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = integerOrNullList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (Gml311Package.eINSTANCE.getIntegerOrNull().isInstance(item)) {
                result &= validateIntegerOrNull(item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(Gml311Package.eINSTANCE.getIntegerOrNull(), item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIsSphereTypeObject(IsSphereType isSphereTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateKnotTypesTypeObject(KnotTypesType knotTypesTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLineTypeTypeObject(LineTypeType lineTypeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNameList(List<?> nameList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNameList_ItemType(nameList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Name List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNameList_ItemType(List<?> nameList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = nameList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (XMLTypePackage.Literals.NAME.isInstance(item)) {
                result &= xmlTypeValidator.validateName((String)item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(XMLTypePackage.Literals.NAME, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNameOrNull(Object nameOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNameOrNull_MemberTypes(nameOrNull, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Name Or Null</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNameOrNull_MemberTypes(Object nameOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(nameOrNull)) {
                if (validateNullEnumeration(nameOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.NAME.isInstance(nameOrNull)) {
                if (xmlTypeValidator.validateName((String)nameOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(nameOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)nameOrNull, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(nameOrNull)) {
                if (validateNullEnumeration(nameOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.NAME.isInstance(nameOrNull)) {
                if (xmlTypeValidator.validateName((String)nameOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(nameOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)nameOrNull, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNameOrNullList(List<?> nameOrNullList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNameOrNullList_ItemType(nameOrNullList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Name Or Null List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNameOrNullList_ItemType(List<?> nameOrNullList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = nameOrNullList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (Gml311Package.eINSTANCE.getNameOrNull().isInstance(item)) {
                result &= validateNameOrNull(item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(Gml311Package.eINSTANCE.getNameOrNull(), item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNCNameList(List<?> ncNameList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNCNameList_ItemType(ncNameList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>NC Name List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNCNameList_ItemType(List<?> ncNameList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = ncNameList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (XMLTypePackage.Literals.NC_NAME.isInstance(item)) {
                result &= xmlTypeValidator.validateNCName((String)item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(XMLTypePackage.Literals.NC_NAME, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNullEnumeration(Object nullEnumeration, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNullEnumeration_MemberTypes(nullEnumeration, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Null Enumeration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNullEnumeration_MemberTypes(Object nullEnumeration, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getNullEnumerationMember0().isInstance(nullEnumeration)) {
                if (validateNullEnumerationMember0((NullEnumerationMember0)nullEnumeration, tempDiagnostics, context)) return true;
            }
            if (Gml311Package.eINSTANCE.getNullEnumerationMember1().isInstance(nullEnumeration)) {
                if (validateNullEnumerationMember1((String)nullEnumeration, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getNullEnumerationMember0().isInstance(nullEnumeration)) {
                if (validateNullEnumerationMember0((NullEnumerationMember0)nullEnumeration, null, context)) return true;
            }
            if (Gml311Package.eINSTANCE.getNullEnumerationMember1().isInstance(nullEnumeration)) {
                if (validateNullEnumerationMember1((String)nullEnumeration, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNullEnumerationMember0Object(NullEnumerationMember0 nullEnumerationMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNullEnumerationMember1(String nullEnumerationMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNullEnumerationMember1_Pattern(nullEnumerationMember1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateNullEnumerationMember1_Pattern
     */
    public static final  PatternMatcher [][] NULL_ENUMERATION_MEMBER1__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("other:\\w{2,}")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Null Enumeration Member1</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNullEnumerationMember1_Pattern(String nullEnumerationMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Gml311Package.eINSTANCE.getNullEnumerationMember1(), nullEnumerationMember1, NULL_ENUMERATION_MEMBER1__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNullType(Object nullType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNullType_MemberTypes(nullType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Null Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNullType_MemberTypes(Object nullType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(nullType)) {
                if (validateNullEnumeration(nullType, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(nullType)) {
                if (xmlTypeValidator.validateAnyURI((String)nullType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(nullType)) {
                if (validateNullEnumeration(nullType, null, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(nullType)) {
                if (xmlTypeValidator.validateAnyURI((String)nullType, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQNameList(List<?> qNameList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateQNameList_ItemType(qNameList, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>QName List</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQNameList_ItemType(List<?> qNameList, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = qNameList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (XMLTypePackage.Literals.QNAME.isInstance(item)) {
                result &= xmlTypeValidator.validateQName(item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(XMLTypePackage.Literals.QNAME, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQueryGrammarEnumerationObject(QueryGrammarEnumeration queryGrammarEnumerationObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRelativePositionTypeObject(RelativePositionType relativePositionTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSequenceRuleNamesObject(SequenceRuleNames sequenceRuleNamesObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSignTypeObject(SignType signTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStringOrNull(Object stringOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateStringOrNull_MemberTypes(stringOrNull, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>String Or Null</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStringOrNull_MemberTypes(Object stringOrNull, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(stringOrNull)) {
                if (validateNullEnumeration(stringOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.STRING.isInstance(stringOrNull)) {
                if (xmlTypeValidator.validateString((String)stringOrNull, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(stringOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)stringOrNull, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getNullEnumeration().isInstance(stringOrNull)) {
                if (validateNullEnumeration(stringOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.STRING.isInstance(stringOrNull)) {
                if (xmlTypeValidator.validateString((String)stringOrNull, null, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(stringOrNull)) {
                if (xmlTypeValidator.validateAnyURI((String)stringOrNull, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSuccessionTypeObject(SuccessionType successionTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSurfaceInterpolationTypeObject(SurfaceInterpolationType surfaceInterpolationTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSymbolTypeEnumerationObject(SymbolTypeEnumeration symbolTypeEnumerationObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeIndeterminateValueTypeObject(TimeIndeterminateValueType timeIndeterminateValueTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimePositionUnion(Object timePositionUnion, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateTimePositionUnion_MemberTypes(timePositionUnion, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Time Position Union</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimePositionUnion_MemberTypes(Object timePositionUnion, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getCalDate().isInstance(timePositionUnion)) {
                if (validateCalDate((XMLGregorianCalendar)timePositionUnion, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.TIME.isInstance(timePositionUnion)) {
                if (xmlTypeValidator.validateTime(timePositionUnion, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.DATE_TIME.isInstance(timePositionUnion)) {
                if (xmlTypeValidator.validateDateTime(timePositionUnion, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(timePositionUnion)) {
                if (xmlTypeValidator.validateAnyURI((String)timePositionUnion, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.DECIMAL.isInstance(timePositionUnion)) {
                if (xmlTypeValidator.validateDecimal((BigDecimal)timePositionUnion, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getCalDate().isInstance(timePositionUnion)) {
                if (validateCalDate((XMLGregorianCalendar)timePositionUnion, null, context)) return true;
            }
            if (XMLTypePackage.Literals.TIME.isInstance(timePositionUnion)) {
                if (xmlTypeValidator.validateTime(timePositionUnion, null, context)) return true;
            }
            if (XMLTypePackage.Literals.DATE_TIME.isInstance(timePositionUnion)) {
                if (xmlTypeValidator.validateDateTime(timePositionUnion, null, context)) return true;
            }
            if (XMLTypePackage.Literals.ANY_URI.isInstance(timePositionUnion)) {
                if (xmlTypeValidator.validateAnyURI((String)timePositionUnion, null, context)) return true;
            }
            if (XMLTypePackage.Literals.DECIMAL.isInstance(timePositionUnion)) {
                if (xmlTypeValidator.validateDecimal((BigDecimal)timePositionUnion, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeUnitType(Object timeUnitType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateTimeUnitType_MemberTypes(timeUnitType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Time Unit Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeUnitType_MemberTypes(Object timeUnitType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Gml311Package.eINSTANCE.getTimeUnitTypeMember0().isInstance(timeUnitType)) {
                if (validateTimeUnitTypeMember0((TimeUnitTypeMember0)timeUnitType, tempDiagnostics, context)) return true;
            }
            if (Gml311Package.eINSTANCE.getTimeUnitTypeMember1().isInstance(timeUnitType)) {
                if (validateTimeUnitTypeMember1((String)timeUnitType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Gml311Package.eINSTANCE.getTimeUnitTypeMember0().isInstance(timeUnitType)) {
                if (validateTimeUnitTypeMember0((TimeUnitTypeMember0)timeUnitType, null, context)) return true;
            }
            if (Gml311Package.eINSTANCE.getTimeUnitTypeMember1().isInstance(timeUnitType)) {
                if (validateTimeUnitTypeMember1((String)timeUnitType, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeUnitTypeMember0Object(TimeUnitTypeMember0 timeUnitTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeUnitTypeMember1(String timeUnitTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateTimeUnitTypeMember1_Pattern(timeUnitTypeMember1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateTimeUnitTypeMember1_Pattern
     */
    public static final  PatternMatcher [][] TIME_UNIT_TYPE_MEMBER1__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("other:\\w{2,}")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Time Unit Type Member1</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTimeUnitTypeMember1_Pattern(String timeUnitTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Gml311Package.eINSTANCE.getTimeUnitTypeMember1(), timeUnitTypeMember1, TIME_UNIT_TYPE_MEMBER1__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        // TODO
        // Specialize this to return a resource locator for messages specific to this validator.
        // Ensure that you remove @generated or mark it @generated NOT
        return super.getResourceLocator();
    }

} //Gml311Validator
